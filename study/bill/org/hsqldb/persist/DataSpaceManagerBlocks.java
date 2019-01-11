package org.hsqldb.persist;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Lock;
import org.hsqldb.Database;
import org.hsqldb.error.Error;
import org.hsqldb.lib.ArrayUtil;
import org.hsqldb.lib.Collection;
import org.hsqldb.lib.DoubleIntIndex;
import org.hsqldb.lib.IntIndex;
import org.hsqldb.lib.IntKeyHashMap;
import org.hsqldb.lib.Iterator;
import org.hsqldb.map.BitMap;

public class DataSpaceManagerBlocks
  implements DataSpaceManager
{
  DataFileCache cache;
  TableSpaceManagerBlocks defaultSpaceManager;
  TableSpaceManagerBlocks directorySpaceManager;
  IntKeyHashMap spaceManagerList;
  BlockObjectStore rootStore;
  BlockObjectStore directoryStore;
  BlockObjectStore bitMapStore;
  IntArrayCachedObject rootBlock;
  AtomicInteger spaceIdSequence = new AtomicInteger(8);
  IntIndex emptySpaceList;
  int released = 0;
  static final int blockSize = 2048;
  static final int fileBlockItemCountLimit = 65536;
  int bitmapIntSize;
  int bitmapStorageSize;
  int fileBlockItemCount;
  int fileBlockSize;
  int dataFileScale;
  BlockAccessor ba;
  
  public DataSpaceManagerBlocks(DataFileCache paramDataFileCache)
  {
    this.cache = paramDataFileCache;
    this.dataFileScale = this.cache.getDataFileScale();
    this.fileBlockSize = (this.cache.database.logger.getDataFileSpaces() * 1024 * 1024);
    this.fileBlockItemCount = (this.fileBlockSize / this.dataFileScale);
    this.bitmapIntSize = (this.fileBlockItemCount / 32);
    this.bitmapStorageSize = (4 * this.bitmapIntSize);
    if (this.bitmapStorageSize < 4096) {
      this.bitmapStorageSize = 4096;
    }
    this.ba = new BlockAccessor(null);
    this.spaceManagerList = new IntKeyHashMap();
    this.emptySpaceList = new IntIndex(32, false);
    this.directorySpaceManager = new TableSpaceManagerBlocks(this, 1, this.fileBlockSize, 16, this.dataFileScale, 0);
    this.defaultSpaceManager = new TableSpaceManagerBlocks(this, 7, this.fileBlockSize, this.cache.database.logger.propMaxFreeBlocks, this.dataFileScale, this.cache.database.logger.propMinReuse);
    this.spaceManagerList.put(1, this.directorySpaceManager);
    this.spaceManagerList.put(7, this.defaultSpaceManager);
    this.rootStore = new BlockObjectStore(this.cache, this.directorySpaceManager, IntArrayCachedObject.class, 8192, 2048);
    this.directoryStore = new BlockObjectStore(this.cache, this.directorySpaceManager, DirectoryBlockCachedObject.class, 24576, 2048);
    this.bitMapStore = new BlockObjectStore(this.cache, this.directorySpaceManager, BitMapCachedObject.class, this.bitmapStorageSize, this.bitmapIntSize);
    if (this.cache.spaceManagerPosition == 0L)
    {
      initialiseNewSpaceDirectory();
      this.cache.spaceManagerPosition = (this.rootBlock.getPos() * this.dataFileScale);
    }
    else
    {
      long l = this.cache.spaceManagerPosition / this.dataFileScale;
      this.rootBlock = ((IntArrayCachedObject)this.rootStore.get(l, true));
      if (getBlockIndexLimit() == 0) {
        throw Error.error(452);
      }
      initialiseSpaceList();
      initialiseTableSpace(this.directorySpaceManager);
      initialiseTableSpace(this.defaultSpaceManager);
    }
  }
  
  private void initialiseNewSpaceDirectory()
  {
    long l1 = this.cache.getFileFreePos();
    long l2 = l1 / this.fileBlockSize + 1L;
    long l3 = this.cache.enlargeFileSpace(l2 * this.fileBlockSize - l1);
    this.defaultSpaceManager.initialiseFileBlock(null, l3, this.cache.getFileFreePos());
    long l4 = l2;
    long l5 = calculateDirectorySpaceBlocks(l2);
    l3 = this.cache.enlargeFileSpace(l5 * this.fileBlockSize);
    this.directorySpaceManager.initialiseFileBlock(null, l3, this.cache.getFileFreePos());
    IntArrayCachedObject localIntArrayCachedObject = new IntArrayCachedObject(2048);
    this.rootStore.add(localIntArrayCachedObject, true);
    this.rootBlock = localIntArrayCachedObject;
    createFileBlocksInDirectory((int)l4, (int)l5, 1);
    createFileBlocksInDirectory(0, (int)l4, 7);
  }
  
  private long calculateDirectorySpaceBlocks(long paramLong)
  {
    long l1 = calculateDirectorySpaceSize(paramLong);
    long l2 = l1 / this.fileBlockSize + 1L;
    l1 += calculateDirectorySpaceSize(l2);
    l2 = l1 / this.fileBlockSize + 1L;
    return l2;
  }
  
  private long calculateDirectorySpaceSize(long paramLong)
  {
    long l1 = ArrayUtil.getBinaryMultipleCeiling(paramLong + 1L, 2048L);
    long l2 = 4L * l1;
    l2 += 12L * l1;
    l2 += this.bitmapStorageSize * (paramLong + 1L);
    return l2;
  }
  
  private void ensureDirectorySpaceAvailable(int paramInt)
  {
    int i = this.bitmapStorageSize * paramInt + 24576;
    if (!this.directorySpaceManager.hasFileRoom(i))
    {
      int j = getBlockIndexLimit();
      int k = i / this.fileBlockSize + 1;
      long l = this.cache.enlargeFileSpace(k * this.fileBlockSize);
      this.directorySpaceManager.addFileBlock(l, l + k * this.fileBlockSize);
      createFileBlocksInDirectory(j, k, 1);
      j = getBlockIndexLimit();
      if (j * this.fileBlockSize != this.cache.getFileFreePos()) {
        this.cache.logSevereEvent("space manager end file pos different from data file: " + j * this.fileBlockSize + ", " + this.cache.getFileFreePos(), null);
      }
    }
  }
  
  public long getFileBlocks(int paramInt1, int paramInt2)
  {
    this.cache.writeLock.lock();
    try
    {
      long l1 = getExistingBlockIndex(paramInt1, paramInt2);
      if (l1 > 0L)
      {
        l2 = l1 * this.fileBlockSize;
        return l2;
      }
      long l2 = getNewFileBlocks(paramInt1, paramInt2);
      return l2;
    }
    finally
    {
      this.cache.writeLock.unlock();
    }
  }
  
  private long getNewFileBlocks(int paramInt1, int paramInt2)
  {
    ensureDirectorySpaceAvailable(paramInt2);
    return getNewFileBlocksNoCheck(paramInt1, paramInt2);
  }
  
  private long getNewFileBlocksNoCheck(int paramInt1, int paramInt2)
  {
    long l1 = getBlockIndexLimit();
    long l2 = l1 * this.fileBlockSize;
    long l3 = l2 + paramInt2 * this.fileBlockSize - this.cache.getFileFreePos();
    if (l3 > 0L) {
      this.cache.enlargeFileSpace(l3);
    }
    createFileBlocksInDirectory((int)l1, paramInt2, paramInt1);
    return l2;
  }
  
  private void createFileBlocksInDirectory(int paramInt1, int paramInt2, int paramInt3)
  {
    for (int i = 0; i < paramInt2; i++) {
      createFileBlockInDirectory(paramInt1 + i, paramInt3);
    }
  }
  
  private void createFileBlockInDirectory(int paramInt1, int paramInt2)
  {
    BitMapCachedObject localBitMapCachedObject = new BitMapCachedObject(this.bitmapIntSize);
    this.bitMapStore.add(localBitMapCachedObject, false);
    int i = (int)(localBitMapCachedObject.getPos() * this.dataFileScale / 4096L);
    int j = paramInt1 % 2048;
    DirectoryBlockCachedObject localDirectoryBlockCachedObject = getDirectory(paramInt1, true);
    if (localDirectoryBlockCachedObject == null)
    {
      createDirectory(paramInt1);
      localDirectoryBlockCachedObject = getDirectory(paramInt1, true);
    }
    localDirectoryBlockCachedObject.getTableIdArray()[j] = paramInt2;
    localDirectoryBlockCachedObject.getBitmapAddressArray()[j] = i;
    localDirectoryBlockCachedObject.setChanged(true);
    localDirectoryBlockCachedObject.keepInMemory(false);
  }
  
  private DirectoryBlockCachedObject getDirectory(int paramInt, boolean paramBoolean)
  {
    int i = paramInt / 2048;
    long l = this.rootBlock.getIntArray()[i];
    if (l == 0L) {
      return null;
    }
    l *= 4096 / this.dataFileScale;
    DirectoryBlockCachedObject localDirectoryBlockCachedObject = (DirectoryBlockCachedObject)this.directoryStore.get(l, paramBoolean);
    return localDirectoryBlockCachedObject;
  }
  
  private void createDirectory(int paramInt)
  {
    DirectoryBlockCachedObject localDirectoryBlockCachedObject = new DirectoryBlockCachedObject(2048);
    this.directoryStore.add(localDirectoryBlockCachedObject, false);
    int i = paramInt / 2048;
    int j = (int)(localDirectoryBlockCachedObject.getPos() * this.dataFileScale / 4096L);
    this.rootBlock.getIntArray()[i] = j;
    this.rootBlock.setChanged(true);
  }
  
  private int getBlockIndexLimit()
  {
    int[] arrayOfInt1 = this.rootBlock.getIntArray();
    for (int i = 0; (i < arrayOfInt1.length) && (arrayOfInt1[i] != 0); i++) {}
    if (i == 0) {
      return 0;
    }
    i--;
    long l = arrayOfInt1[i];
    l *= 4096 / this.dataFileScale;
    DirectoryBlockCachedObject localDirectoryBlockCachedObject = (DirectoryBlockCachedObject)this.directoryStore.get(l, false);
    int[] arrayOfInt2 = localDirectoryBlockCachedObject.getBitmapAddressArray();
    for (int j = 0; (j < arrayOfInt2.length) && (arrayOfInt2[j] != 0); j++) {}
    return i * 2048 + j;
  }
  
  private void initialiseSpaceList()
  {
    int i = 7;
    this.ba.initialise(false);
    for (;;)
    {
      boolean bool = this.ba.nextBlock();
      if (!bool) {
        break;
      }
      int j = this.ba.getTableId();
      if (j > i) {
        i = j;
      }
      if (j == 0) {
        this.emptySpaceList.addUnique(this.ba.currentBlockIndex);
      }
    }
    this.ba.reset();
    this.spaceIdSequence.set(i + 2 & 0xFFFFFFFE);
  }
  
  private int getExistingBlockIndex(int paramInt1, int paramInt2)
  {
    int i = this.emptySpaceList.removeFirstConsecutiveKeys(paramInt2, -1);
    if (i > 0) {
      setDirectoryBlocksAsTable(paramInt1, i, paramInt2);
    }
    return i;
  }
  
  private void setDirectoryBlocksAsTable(int paramInt1, int paramInt2, int paramInt3)
  {
    int i = -1;
    DirectoryBlockCachedObject localDirectoryBlockCachedObject = null;
    for (int j = paramInt2; j < paramInt2 + paramInt3; j++)
    {
      if (i != j / 2048)
      {
        if (localDirectoryBlockCachedObject != null) {
          localDirectoryBlockCachedObject.keepInMemory(false);
        }
        localDirectoryBlockCachedObject = getDirectory(j, true);
        i = j / 2048;
      }
      int k = j % 2048;
      localDirectoryBlockCachedObject.getTableIdArray()[k] = paramInt1;
    }
    localDirectoryBlockCachedObject.keepInMemory(false);
  }
  
  public TableSpaceManager getDefaultTableSpace()
  {
    return this.defaultSpaceManager;
  }
  
  public TableSpaceManager getTableSpace(int paramInt)
  {
    if (paramInt == 7) {
      return this.defaultSpaceManager;
    }
    if (paramInt >= this.spaceIdSequence.get()) {
      this.spaceIdSequence.set(paramInt + 2 & 0xFFFFFFFE);
    }
    this.cache.writeLock.lock();
    try
    {
      TableSpaceManagerBlocks localTableSpaceManagerBlocks1 = (TableSpaceManagerBlocks)this.spaceManagerList.get(paramInt);
      if (localTableSpaceManagerBlocks1 == null)
      {
        int i = this.cache.database.logger.propMinReuse;
        localTableSpaceManagerBlocks1 = new TableSpaceManagerBlocks(this, paramInt, this.fileBlockSize, this.cache.database.logger.propMaxFreeBlocks, this.dataFileScale, i);
        initialiseTableSpace(localTableSpaceManagerBlocks1);
        this.spaceManagerList.put(paramInt, localTableSpaceManagerBlocks1);
      }
      TableSpaceManagerBlocks localTableSpaceManagerBlocks2 = localTableSpaceManagerBlocks1;
      return localTableSpaceManagerBlocks2;
    }
    finally
    {
      this.cache.writeLock.unlock();
    }
  }
  
  public int getNewTableSpaceID()
  {
    return this.spaceIdSequence.getAndAdd(2);
  }
  
  public void freeTableSpace(int paramInt)
  {
    if ((paramInt == 7) || (paramInt == 1)) {
      return;
    }
    this.cache.writeLock.lock();
    try
    {
      TableSpaceManager localTableSpaceManager = (TableSpaceManager)this.spaceManagerList.get(paramInt);
      if (localTableSpaceManager != null)
      {
        localTableSpaceManager.reset();
        this.spaceManagerList.remove(paramInt);
      }
      IntIndex localIntIndex = new IntIndex(16, false);
      this.ba.initialise(true);
      while (this.ba.nextBlockForTable(paramInt))
      {
        localIntIndex.addUnsorted(this.ba.currentBlockIndex);
        this.ba.setTable(0);
        this.emptySpaceList.addUnique(this.ba.currentBlockIndex);
      }
      this.ba.reset();
      this.cache.releaseRange(localIntIndex, this.fileBlockItemCount);
    }
    finally
    {
      this.cache.writeLock.unlock();
    }
  }
  
  public void freeTableSpace(int paramInt, DoubleIntIndex paramDoubleIntIndex, long paramLong1, long paramLong2, boolean paramBoolean)
  {
    if ((paramDoubleIntIndex.size() == 0) && (paramLong1 == paramLong2)) {
      return;
    }
    paramDoubleIntIndex.compactLookupAsIntervals();
    if (!paramBoolean)
    {
      int i = paramDoubleIntIndex.capacity() - paramDoubleIntIndex.size();
      if (i > paramDoubleIntIndex.capacity() / 4)
      {
        paramDoubleIntIndex.setValuesSearchTarget();
        paramDoubleIntIndex.sort();
        return;
      }
    }
    this.cache.writeLock.lock();
    try
    {
      this.ba.initialise(true);
      int[] arrayOfInt1 = paramDoubleIntIndex.getKeys();
      int[] arrayOfInt2 = paramDoubleIntIndex.getValues();
      for (int j = 0; j < paramDoubleIntIndex.size(); j++)
      {
        int k = arrayOfInt1[j];
        m = arrayOfInt2[j];
        freeTableSpacePart(k, m);
      }
      long l = paramLong1 / this.dataFileScale;
      int m = (int)((paramLong2 - paramLong1) / this.dataFileScale);
      freeTableSpacePart(l, m);
      this.ba.reset();
    }
    finally
    {
      this.cache.writeLock.unlock();
    }
    paramDoubleIntIndex.clear();
    paramDoubleIntIndex.setValuesSearchTarget();
  }
  
  private void freeTableSpacePart(long paramLong, int paramInt)
  {
    while (paramInt > 0)
    {
      int i = (int)(paramLong / this.fileBlockItemCount);
      int j = (int)(paramLong % this.fileBlockItemCount);
      int k = this.fileBlockItemCount - j;
      if (k > paramInt) {
        k = paramInt;
      }
      this.ba.moveToBlock(i);
      int m = this.ba.setRange(j, k);
      if (m != k)
      {
        this.ba.unsetRange(j, k);
        this.cache.logSevereEvent("space manager error - recovered", null);
      }
      paramInt -= k;
      paramLong += k;
    }
  }
  
  public int findTableSpace(long paramLong)
  {
    int i = (int)(paramLong / this.fileBlockItemCount);
    this.cache.writeLock.lock();
    try
    {
      this.ba.initialise(false);
      boolean bool = this.ba.moveToBlock(i);
      if (!bool)
      {
        this.ba.reset();
        j = 7;
        return j;
      }
      int j = this.ba.getTableId();
      this.ba.reset();
      int k = j;
      return k;
    }
    finally
    {
      this.cache.writeLock.unlock();
    }
  }
  
  public long getLostBlocksSize()
  {
    long l = 0L;
    this.cache.writeLock.lock();
    try
    {
      this.ba.initialise(false);
      for (;;)
      {
        boolean bool = this.ba.nextBlock();
        if (!bool) {
          break;
        }
        if (this.ba.getTableId() != 1)
        {
          l += this.ba.getFreeSpaceValue() * this.dataFileScale;
          if (this.ba.getTableId() == 0) {
            l += this.fileBlockSize;
          }
        }
      }
      this.ba.reset();
    }
    finally
    {
      this.cache.writeLock.unlock();
    }
    return l;
  }
  
  public int getFileBlockSize()
  {
    return this.fileBlockSize;
  }
  
  public boolean isModified()
  {
    return true;
  }
  
  public void initialiseSpaces()
  {
    this.cache.writeLock.lock();
    try
    {
      Iterator localIterator = this.spaceManagerList.values().iterator();
      while (localIterator.hasNext())
      {
        TableSpaceManagerBlocks localTableSpaceManagerBlocks = (TableSpaceManagerBlocks)localIterator.next();
        initialiseTableSpace(localTableSpaceManagerBlocks);
      }
    }
    finally
    {
      this.cache.writeLock.unlock();
    }
  }
  
  public void reset()
  {
    this.cache.writeLock.lock();
    try
    {
      Iterator localIterator = this.spaceManagerList.values().iterator();
      while (localIterator.hasNext())
      {
        TableSpaceManagerBlocks localTableSpaceManagerBlocks = (TableSpaceManagerBlocks)localIterator.next();
        localTableSpaceManagerBlocks.reset();
      }
    }
    finally
    {
      this.cache.writeLock.unlock();
    }
  }
  
  public boolean isMultiSpace()
  {
    return true;
  }
  
  public int getFileBlockItemCount()
  {
    return this.fileBlockItemCount;
  }
  
  public DirectoryBlockCachedObject[] getDirectoryList()
  {
    int i = 0;
    int[] arrayOfInt = this.rootBlock.getIntArray();
    while (arrayOfInt[i] != 0) {
      i++;
    }
    DirectoryBlockCachedObject[] arrayOfDirectoryBlockCachedObject = new DirectoryBlockCachedObject[i];
    for (int j = 0; j < arrayOfDirectoryBlockCachedObject.length; j++) {
      arrayOfDirectoryBlockCachedObject[j] = getDirectory(j * 2048, false);
    }
    return arrayOfDirectoryBlockCachedObject;
  }
  
  private void initialiseTableSpace(TableSpaceManagerBlocks paramTableSpaceManagerBlocks)
  {
    int i = paramTableSpaceManagerBlocks.getSpaceID();
    int j = 0;
    int k = -1;
    int m = paramTableSpaceManagerBlocks.getFileBlockIndex();
    if (m >= 0)
    {
      this.ba.initialise(false);
      boolean bool = this.ba.moveToBlock(m);
      if ((bool) && (this.ba.getTableId() == i) && (this.ba.getFreeBlockValue() > 0)) {
        k = m;
      }
      this.ba.reset();
    }
    if (k < 0)
    {
      this.ba.initialise(false);
      while (this.ba.nextBlockForTable(i))
      {
        n = this.ba.getFreeBlockValue();
        if (n > j)
        {
          k = this.ba.currentBlockIndex;
          j = n;
        }
      }
      this.ba.reset();
    }
    if (k < 0) {
      return;
    }
    this.ba.initialise(true);
    this.ba.moveToBlock(k);
    int n = this.ba.getFreeBlockValue();
    long l = k * this.fileBlockSize;
    int i1 = this.ba.unsetRange(this.fileBlockItemCount - n, n);
    if (i1 == n) {
      paramTableSpaceManagerBlocks.initialiseFileBlock(null, l + (this.fileBlockSize - n * this.dataFileScale), l + this.fileBlockSize);
    } else {
      this.cache.logSevereEvent("space manager error - recovered", null);
    }
    this.ba.reset();
  }
  
  private class BlockAccessor
  {
    boolean currentKeep;
    int currentBlockIndex = -1;
    int currentDirIndex = -1;
    int currentBlockOffset = -1;
    DirectoryBlockCachedObject currentDir = null;
    BitMapCachedObject currentBitMap = null;
    
    private BlockAccessor() {}
    
    void initialise(boolean paramBoolean)
    {
      this.currentKeep = paramBoolean;
    }
    
    boolean nextBlock()
    {
      boolean bool = moveToBlock(this.currentBlockIndex + 1);
      return bool;
    }
    
    boolean nextBlockForTable(int paramInt)
    {
      for (;;)
      {
        boolean bool = moveToBlock(this.currentBlockIndex + 1);
        if (!bool) {
          return false;
        }
        if (getTableId() == paramInt) {
          return true;
        }
      }
    }
    
    boolean moveToBlock(int paramInt)
    {
      if (this.currentBlockIndex != paramInt)
      {
        endBlockUpdate();
        this.currentBitMap = null;
        if (this.currentDirIndex != paramInt / 2048)
        {
          reset();
          this.currentDirIndex = (paramInt / 2048);
          this.currentDir = DataSpaceManagerBlocks.this.getDirectory(paramInt, this.currentKeep);
        }
        if (this.currentDir == null)
        {
          reset();
          return false;
        }
        this.currentBlockIndex = paramInt;
        this.currentBlockOffset = (paramInt % 2048);
        long l = this.currentDir.getBitmapAddressArray()[this.currentBlockOffset];
        if (l == 0L)
        {
          reset();
          return false;
        }
        if (this.currentKeep)
        {
          l *= 4096 / DataSpaceManagerBlocks.this.dataFileScale;
          this.currentBitMap = ((BitMapCachedObject)DataSpaceManagerBlocks.this.bitMapStore.get(l, true));
        }
      }
      return true;
    }
    
    int setRange(int paramInt1, int paramInt2)
    {
      this.currentBitMap.setChanged(true);
      return this.currentBitMap.bitMap.setRange(paramInt1, paramInt2);
    }
    
    int unsetRange(int paramInt1, int paramInt2)
    {
      this.currentBitMap.setChanged(true);
      return this.currentBitMap.bitMap.unsetRange(paramInt1, paramInt2);
    }
    
    void reset()
    {
      endBlockUpdate();
      if ((this.currentDir != null) && (this.currentKeep)) {
        this.currentDir.keepInMemory(false);
      }
      this.currentBlockIndex = -1;
      this.currentDirIndex = -1;
      this.currentBlockOffset = -1;
      this.currentDir = null;
      this.currentBitMap = null;
    }
    
    private void endBlockUpdate()
    {
      if (this.currentBitMap == null) {
        return;
      }
      if (!this.currentBitMap.hasChanged())
      {
        this.currentBitMap.keepInMemory(false);
        return;
      }
      int i = this.currentBitMap.bitMap.countSetBits();
      int j = this.currentBitMap.bitMap.countSetBitsEnd();
      this.currentBitMap.keepInMemory(false);
      if (i == DataSpaceManagerBlocks.this.fileBlockItemCount)
      {
        setTable(0);
        DataSpaceManagerBlocks.this.emptySpaceList.addUnique(this.currentBlockIndex);
        DataSpaceManagerBlocks.this.released += 1;
        return;
      }
      this.currentDir.getFreeSpaceArray()[this.currentBlockOffset] = ((char)i);
      this.currentDir.getFreeBlockArray()[this.currentBlockOffset] = ((char)j);
      this.currentDir.setChanged(true);
    }
    
    void setTable(int paramInt)
    {
      this.currentDir.getTableIdArray()[this.currentBlockOffset] = paramInt;
      this.currentDir.getFreeSpaceArray()[this.currentBlockOffset] = 0;
      this.currentDir.getFreeBlockArray()[this.currentBlockOffset] = 0;
      this.currentDir.setChanged(true);
      this.currentBitMap.bitMap.reset();
      this.currentBitMap.setChanged(true);
    }
    
    int getTableId()
    {
      return this.currentDir.getTableIdArray()[this.currentBlockOffset];
    }
    
    char getFreeSpaceValue()
    {
      return this.currentDir.getFreeSpaceArray()[this.currentBlockOffset];
    }
    
    char getFreeBlockValue()
    {
      return this.currentDir.getFreeBlockArray()[this.currentBlockOffset];
    }
  }
}


/* Location:              E:\java\java学习\hutubill\lib\all.jar!\org\hsqldb\persist\DataSpaceManagerBlocks.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */
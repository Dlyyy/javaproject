package org.hsqldb.persist;

import org.hsqldb.error.Error;
import org.hsqldb.lib.ArrayUtil;
import org.hsqldb.lib.DoubleIntIndex;

public class TableSpaceManagerBlocks
  implements TableSpaceManager
{
  DataSpaceManager spaceManager;
  private final int scale;
  final int mainBlockSize;
  final int spaceID;
  final int minReuse;
  private DoubleIntIndex lookup;
  private final int capacity;
  private long requestGetCount;
  private long releaseCount;
  private long requestCount;
  private long requestSize;
  boolean isModified;
  long freshBlockFreePos = 0L;
  long freshBlockLimit = 0L;
  int fileBlockIndex = -1;
  
  public TableSpaceManagerBlocks(DataSpaceManager paramDataSpaceManager, int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5)
  {
    this.spaceManager = paramDataSpaceManager;
    this.scale = paramInt4;
    this.spaceID = paramInt1;
    this.mainBlockSize = paramInt2;
    this.minReuse = paramInt5;
    this.lookup = new DoubleIntIndex(paramInt3, true);
    this.lookup.setValuesSearchTarget();
    this.capacity = paramInt3;
  }
  
  public boolean hasFileRoom(long paramLong)
  {
    return this.freshBlockLimit - this.freshBlockFreePos > paramLong;
  }
  
  public void addFileBlock(long paramLong1, long paramLong2)
  {
    int i = (int)(this.freshBlockLimit - this.freshBlockFreePos);
    if (i > 0) {
      release(this.freshBlockFreePos / this.scale, i);
    }
    initialiseFileBlock(null, paramLong1, paramLong2);
  }
  
  public void initialiseFileBlock(DoubleIntIndex paramDoubleIntIndex, long paramLong1, long paramLong2)
  {
    this.freshBlockFreePos = paramLong1;
    this.freshBlockLimit = paramLong2;
    if (paramDoubleIntIndex != null) {
      paramDoubleIntIndex.copyTo(this.lookup);
    }
  }
  
  boolean getNewMainBlock(long paramLong)
  {
    long l1 = (this.mainBlockSize + paramLong) / this.mainBlockSize;
    long l2 = l1 * this.mainBlockSize;
    long l3 = this.spaceManager.getFileBlocks(this.spaceID, (int)l1);
    if (l3 < 0L) {
      return false;
    }
    if (l3 != this.freshBlockLimit)
    {
      long l4 = this.freshBlockLimit - this.freshBlockFreePos;
      if (l4 > 0L) {
        release(this.freshBlockFreePos / this.scale, (int)l4);
      }
      this.freshBlockFreePos = l3;
      this.freshBlockLimit = l3;
    }
    this.freshBlockLimit += l2;
    return true;
  }
  
  long getNewBlock(long paramLong, boolean paramBoolean)
  {
    if (paramBoolean) {
      paramLong = (int)ArrayUtil.getBinaryMultipleCeiling(paramLong, 4096L);
    }
    if (this.freshBlockFreePos + paramLong > this.freshBlockLimit)
    {
      boolean bool = getNewMainBlock(paramLong);
      if (!bool) {
        throw Error.error(468);
      }
    }
    long l1 = this.freshBlockFreePos;
    if (paramBoolean)
    {
      l1 = ArrayUtil.getBinaryMultipleCeiling(l1, 4096L);
      long l2 = l1 - this.freshBlockFreePos;
      if (l2 > 0L)
      {
        release(this.freshBlockFreePos / this.scale, (int)l2);
        this.freshBlockFreePos = l1;
      }
    }
    this.freshBlockFreePos += paramLong;
    return l1 / this.scale;
  }
  
  public int getSpaceID()
  {
    return this.spaceID;
  }
  
  public synchronized void release(long paramLong, int paramInt)
  {
    this.isModified = true;
    this.releaseCount += 1L;
    if (this.lookup.size() == this.capacity) {
      resetList();
    }
    if (paramLong >= 2147483647L) {
      return;
    }
    this.lookup.add(paramLong, paramInt / this.scale);
  }
  
  public synchronized long getFilePosition(int paramInt, boolean paramBoolean)
  {
    this.requestGetCount += 1L;
    if (this.capacity == 0) {
      return getNewBlock(paramInt, paramBoolean);
    }
    if (paramBoolean) {
      paramInt = (int)ArrayUtil.getBinaryMultipleCeiling(paramInt, 4096L);
    }
    int i = -1;
    int j = paramInt / this.scale;
    if ((paramInt >= this.minReuse) && (this.lookup.size() > 0)) {
      if (this.lookup.getValue(0) >= j) {
        i = 0;
      } else if (paramInt > Integer.MAX_VALUE) {
        i = -1;
      } else {
        i = this.lookup.findFirstGreaterEqualKeyIndex(j);
      }
    }
    if (i == -1) {
      return getNewBlock(paramInt, paramBoolean);
    }
    if (paramBoolean)
    {
      while (i < this.lookup.size())
      {
        long l = this.lookup.getKey(i);
        if (l % (4096 / this.scale) == 0L) {
          break;
        }
        i++;
      }
      if (i == this.lookup.size()) {
        return getNewBlock(paramInt, paramBoolean);
      }
    }
    this.requestCount += 1L;
    this.requestSize += paramInt;
    int k = this.lookup.getKey(i);
    int m = this.lookup.getValue(i);
    int n = m - j;
    this.lookup.remove(i);
    if (n > 0)
    {
      int i1 = k + j;
      this.lookup.add(i1, n);
    }
    return k;
  }
  
  public void reset()
  {
    if (this.freshBlockFreePos == 0L) {
      this.fileBlockIndex = -1;
    } else {
      this.fileBlockIndex = ((int)(this.freshBlockFreePos / this.mainBlockSize));
    }
    this.spaceManager.freeTableSpace(this.spaceID, this.lookup, this.freshBlockFreePos, this.freshBlockLimit, true);
    this.freshBlockFreePos = 0L;
    this.freshBlockLimit = 0L;
  }
  
  public long getLostBlocksSize()
  {
    long l = this.freshBlockLimit - this.freshBlockFreePos + this.lookup.getTotalValues() * this.scale;
    return l;
  }
  
  public boolean isDefaultSpace()
  {
    return this.spaceID == 7;
  }
  
  public int getFileBlockIndex()
  {
    return this.fileBlockIndex;
  }
  
  private void resetList()
  {
    this.spaceManager.freeTableSpace(this.spaceID, this.lookup, 0L, 0L, false);
  }
}


/* Location:              E:\java\java学习\hutubill\lib\all.jar!\org\hsqldb\persist\TableSpaceManagerBlocks.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */
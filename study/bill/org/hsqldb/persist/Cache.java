package org.hsqldb.persist;

import org.hsqldb.Database;
import org.hsqldb.TransactionManager;
import org.hsqldb.error.Error;
import org.hsqldb.lib.ArraySort;
import org.hsqldb.lib.IntIndex;
import org.hsqldb.lib.Iterator;
import org.hsqldb.lib.ObjectComparator;
import org.hsqldb.lib.StopWatch;
import org.hsqldb.map.BaseHashMap;
import org.hsqldb.map.BaseHashMap.BaseHashIterator;

public class Cache
  extends BaseHashMap
{
  private int reserveCount;
  final DataFileCache dataFileCache;
  private int capacity;
  private long bytesCapacity;
  private final CachedObjectComparator rowComparator;
  private final BaseHashMap.BaseHashIterator objectIterator;
  private boolean updateAccess;
  private CachedObject[] rowTable;
  private long cacheBytesLength;
  StopWatch saveAllTimer = new StopWatch(false);
  StopWatch shadowTimer = new StopWatch(false);
  int saveRowCount = 0;
  
  Cache(DataFileCache paramDataFileCache)
  {
    super(paramDataFileCache.capacity(), 3, 0, true);
    this.maxCapacity = paramDataFileCache.capacity();
    this.dataFileCache = paramDataFileCache;
    this.capacity = paramDataFileCache.capacity();
    this.bytesCapacity = paramDataFileCache.bytesCapacity();
    this.rowComparator = new CachedObjectComparator();
    this.rowTable = new CachedObject[this.capacity];
    this.cacheBytesLength = 0L;
    this.objectIterator = new BaseHashMap.BaseHashIterator(this, true);
    this.updateAccess = true;
    this.comparator = this.rowComparator;
    this.reserveCount = (((paramDataFileCache instanceof TextCache)) || ((paramDataFileCache instanceof DataFileCacheSession)) ? 0 : 8);
  }
  
  long getTotalCachedBlockSize()
  {
    return this.cacheBytesLength;
  }
  
  public CachedObject get(long paramLong)
  {
    if (this.accessCount > 2146435071)
    {
      updateAccessCounts();
      resetAccessCount();
      updateObjectAccessCounts();
    }
    int i = getObjectLookup(paramLong);
    if (i == -1) {
      return null;
    }
    this.accessTable[i] = (++this.accessCount);
    CachedObject localCachedObject = (CachedObject)this.objectKeyTable[i];
    return localCachedObject;
  }
  
  void put(CachedObject paramCachedObject)
  {
    int i = paramCachedObject.getStorageSize();
    if (preparePut(i))
    {
      putNoCheck(paramCachedObject);
    }
    else
    {
      long l = size() + this.reserveCount >= this.capacity ? this.capacity : this.bytesCapacity / 1024L;
      throw Error.error(471, String.valueOf(l));
    }
  }
  
  void putUsingReserve(CachedObject paramCachedObject)
  {
    int i = paramCachedObject.getStorageSize();
    preparePut(i);
    if (size() >= this.capacity) {
      throw Error.error(471, String.valueOf(this.capacity));
    }
    putNoCheck(paramCachedObject);
  }
  
  boolean preparePut(int paramInt)
  {
    int i = size() + this.reserveCount >= this.capacity ? 1 : 0;
    int j = paramInt + this.cacheBytesLength > this.bytesCapacity ? 1 : 0;
    if ((i != 0) || (j != 0))
    {
      cleanUp(false);
      i = size() + this.reserveCount >= this.capacity ? 1 : 0;
      j = paramInt + this.cacheBytesLength > this.bytesCapacity ? 1 : 0;
      if ((i != 0) || (j != 0)) {
        clearUnchanged();
      } else {
        return true;
      }
      i = size() + this.reserveCount >= this.capacity ? 1 : 0;
      j = paramInt + this.cacheBytesLength > this.bytesCapacity ? 1 : 0;
      if ((i != 0) || (j != 0)) {
        cleanUp(true);
      } else {
        return true;
      }
      i = size() + this.reserveCount >= this.capacity ? 1 : 0;
      j = paramInt + this.cacheBytesLength > this.bytesCapacity ? 1 : 0;
      if (i != 0) {
        this.dataFileCache.logInfoEvent("dataFileCache CACHE ROWS limit reached");
      }
      if (j != 0) {
        this.dataFileCache.logInfoEvent("dataFileCache CACHE SIZE limit reached");
      }
      if ((i != 0) || (j != 0)) {
        return false;
      }
    }
    return true;
  }
  
  private void putNoCheck(CachedObject paramCachedObject)
  {
    if (this.accessCount > 2146435071)
    {
      updateAccessCounts();
      resetAccessCount();
      updateObjectAccessCounts();
    }
    Object localObject = super.addOrRemoveObject(paramCachedObject, paramCachedObject.getPos(), false);
    if (localObject != null) {
      this.dataFileCache.logSevereEvent("existing object in Cache.put() " + paramCachedObject.getPos() + " " + paramCachedObject.getStorageSize(), null);
    }
    paramCachedObject.setInMemory(true);
    this.cacheBytesLength += paramCachedObject.getStorageSize();
  }
  
  CachedObject release(long paramLong)
  {
    CachedObject localCachedObject = (CachedObject)super.addOrRemoveObject(null, paramLong, true);
    if (localCachedObject == null) {
      return null;
    }
    this.cacheBytesLength -= localCachedObject.getStorageSize();
    localCachedObject.setInMemory(false);
    return localCachedObject;
  }
  
  public void releaseRange(IntIndex paramIntIndex, int paramInt)
  {
    this.objectIterator.reset();
    while (this.objectIterator.hasNext())
    {
      CachedObject localCachedObject = (CachedObject)this.objectIterator.next();
      long l = localCachedObject.getPos();
      int i = (int)(l / paramInt);
      int j = paramIntIndex.findFirstEqualKeyIndex(i);
      if (j >= 0)
      {
        localCachedObject.setInMemory(false);
        this.objectIterator.remove();
        this.cacheBytesLength -= localCachedObject.getStorageSize();
      }
    }
  }
  
  public void releaseRange(long paramLong1, long paramLong2)
  {
    this.objectIterator.reset();
    while (this.objectIterator.hasNext())
    {
      CachedObject localCachedObject = (CachedObject)this.objectIterator.next();
      long l = localCachedObject.getPos();
      if ((l >= paramLong1) && (l < paramLong2))
      {
        localCachedObject.setInMemory(false);
        this.objectIterator.remove();
        this.cacheBytesLength -= localCachedObject.getStorageSize();
      }
    }
  }
  
  private void updateAccessCounts()
  {
    if (this.updateAccess) {
      for (int j = 0; j < this.objectKeyTable.length; j++)
      {
        CachedObject localCachedObject = (CachedObject)this.objectKeyTable[j];
        if (localCachedObject != null)
        {
          int i = localCachedObject.getAccessCount();
          if (i > this.accessTable[j]) {
            this.accessTable[j] = i;
          }
        }
      }
    }
  }
  
  private void updateObjectAccessCounts()
  {
    if (this.updateAccess) {
      for (int j = 0; j < this.objectKeyTable.length; j++)
      {
        CachedObject localCachedObject = (CachedObject)this.objectKeyTable[j];
        if (localCachedObject != null)
        {
          int i = this.accessTable[j];
          localCachedObject.updateAccessCount(i);
        }
      }
    }
  }
  
  private void cleanUp(boolean paramBoolean)
  {
    updateAccessCounts();
    int i = 0;
    int j = size() / 2;
    int k = paramBoolean ? this.accessCount + 1 : getAccessCountCeiling(j, j / 8);
    int m = paramBoolean ? this.accessCount + 1 : (this.accessMin + k) / 2;
    this.objectIterator.reset();
    while (this.objectIterator.hasNext())
    {
      CachedObject localCachedObject = (CachedObject)this.objectIterator.next();
      int n = this.objectIterator.getAccessCount();
      int i1 = (n < k) && (!localCachedObject.isKeepInMemory()) ? 1 : 0;
      int i2 = (localCachedObject.isNew()) && (localCachedObject.getStorageSize() >= 4096) ? 1 : 0;
      int i3 = (localCachedObject.hasChanged()) && ((i1 != 0) || (i2 != 0)) ? 1 : 0;
      this.objectIterator.setAccessCount(k);
      synchronized (localCachedObject)
      {
        if (i3 != 0) {
          this.rowTable[(i++)] = localCachedObject;
        }
        if (i1 != 0)
        {
          localCachedObject.setInMemory(false);
          this.objectIterator.remove();
          this.cacheBytesLength -= localCachedObject.getStorageSize();
        }
      }
      if (i == this.rowTable.length)
      {
        saveRows(i);
        i = 0;
      }
    }
    saveRows(i);
    setAccessCountFloor(k);
    this.accessCount += 1;
  }
  
  void clearUnchanged()
  {
    this.objectIterator.reset();
    while (this.objectIterator.hasNext())
    {
      CachedObject localCachedObject = (CachedObject)this.objectIterator.next();
      synchronized (localCachedObject)
      {
        if ((!localCachedObject.isKeepInMemory()) && (!localCachedObject.hasChanged()))
        {
          localCachedObject.setInMemory(false);
          this.objectIterator.remove();
          this.cacheBytesLength -= localCachedObject.getStorageSize();
        }
      }
    }
  }
  
  private synchronized void saveRows(int paramInt)
  {
    if (paramInt == 0) {
      return;
    }
    this.rowComparator.setType(1);
    ArraySort.sort(this.rowTable, 0, paramInt, this.rowComparator);
    this.dataFileCache.saveRows(this.rowTable, 0, paramInt);
    this.saveRowCount += paramInt;
  }
  
  void saveAll()
  {
    int i = 0;
    this.objectIterator.reset();
    while (this.objectIterator.hasNext())
    {
      if (i == this.rowTable.length)
      {
        saveRows(i);
        i = 0;
      }
      CachedObject localCachedObject = (CachedObject)this.objectIterator.next();
      if (localCachedObject.hasChanged())
      {
        this.rowTable[i] = localCachedObject;
        i++;
      }
    }
    saveRows(i);
  }
  
  void logSaveRowsEvent(int paramInt, long paramLong1, long paramLong2)
  {
    long l = this.saveAllTimer.elapsedTime();
    StringBuffer localStringBuffer = new StringBuffer();
    localStringBuffer.append("cache save rows total [count,time] ");
    localStringBuffer.append(this.saveRowCount + paramInt);
    localStringBuffer.append(',').append(l).append(' ');
    localStringBuffer.append("operation [count,size,time]").append(paramInt).append(' ');
    localStringBuffer.append(paramLong1).append(',');
    localStringBuffer.append(l - paramLong2).append(' ');
    localStringBuffer.append("tx-ts ");
    localStringBuffer.append(this.dataFileCache.database.txManager.getGlobalChangeTimestamp());
    this.dataFileCache.logDetailEvent(localStringBuffer.toString());
  }
  
  public void clear()
  {
    super.clear();
    this.cacheBytesLength = 0L;
  }
  
  public Iterator getIterator()
  {
    this.objectIterator.reset();
    return this.objectIterator;
  }
  
  protected int incrementAccessCount()
  {
    return super.incrementAccessCount();
  }
  
  static final class CachedObjectComparator
    implements ObjectComparator
  {
    static final int COMPARE_LAST_ACCESS = 0;
    static final int COMPARE_POSITION = 1;
    static final int COMPARE_SIZE = 2;
    private int compareType = 1;
    
    void setType(int paramInt)
    {
      this.compareType = paramInt;
    }
    
    public int compare(Object paramObject1, Object paramObject2)
    {
      long l;
      switch (this.compareType)
      {
      case 1: 
        l = ((CachedObject)paramObject1).getPos() - ((CachedObject)paramObject2).getPos();
        break;
      case 2: 
        l = ((CachedObject)paramObject1).getStorageSize() - ((CachedObject)paramObject2).getStorageSize();
        break;
      default: 
        return 0;
      }
      return l > 0L ? 1 : l == 0L ? 0 : -1;
    }
    
    public int hashCode(Object paramObject)
    {
      return paramObject.hashCode();
    }
    
    public long longKey(Object paramObject)
    {
      return ((CachedObject)paramObject).getPos();
    }
  }
}


/* Location:              E:\java\java学习\hutubill\lib\all.jar!\org\hsqldb\persist\Cache.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */
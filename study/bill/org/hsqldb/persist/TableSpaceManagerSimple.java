package org.hsqldb.persist;

import java.util.concurrent.locks.Lock;
import org.hsqldb.error.Error;
import org.hsqldb.lib.DoubleIntIndex;

public class TableSpaceManagerSimple
  implements TableSpaceManager
{
  DataFileCache cache;
  final int scale;
  
  public TableSpaceManagerSimple(DataFileCache paramDataFileCache)
  {
    this.cache = paramDataFileCache;
    this.scale = paramDataFileCache.getDataFileScale();
  }
  
  public int getSpaceID()
  {
    return 7;
  }
  
  public void release(long paramLong, int paramInt) {}
  
  public long getFilePosition(int paramInt, boolean paramBoolean)
  {
    this.cache.writeLock.lock();
    try
    {
      long l1 = this.cache.getFileFreePos() / this.scale;
      long l2 = this.cache.getFileFreePos() + paramInt;
      if (l2 > this.cache.maxDataFileSize)
      {
        this.cache.logSevereEvent("data file reached maximum size " + this.cache.dataFileName, null);
        throw Error.error(468);
      }
      this.cache.fileFreePosition = l2;
      long l3 = l1;
      return l3;
    }
    finally
    {
      this.cache.writeLock.unlock();
    }
  }
  
  public boolean hasFileRoom(long paramLong)
  {
    return true;
  }
  
  public void addFileBlock(long paramLong1, long paramLong2) {}
  
  public void initialiseFileBlock(DoubleIntIndex paramDoubleIntIndex, long paramLong1, long paramLong2) {}
  
  public void reset() {}
  
  public long getLostBlocksSize()
  {
    return 0L;
  }
  
  public boolean isDefaultSpace()
  {
    return true;
  }
}


/* Location:              E:\java\java学习\hutubill\lib\all.jar!\org\hsqldb\persist\TableSpaceManagerSimple.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */
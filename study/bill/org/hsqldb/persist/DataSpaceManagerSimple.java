package org.hsqldb.persist;

import java.util.concurrent.locks.Lock;
import org.hsqldb.Database;
import org.hsqldb.lib.DoubleIntIndex;

public class DataSpaceManagerSimple
  implements DataSpaceManager
{
  DataFileCache cache;
  TableSpaceManager defaultSpaceManager;
  int fileBlockSize = 4096;
  long totalFragmentSize;
  int spaceIdSequence = 8;
  DoubleIntIndex lookup;
  
  DataSpaceManagerSimple(DataFileCache paramDataFileCache, boolean paramBoolean)
  {
    this.cache = paramDataFileCache;
    if ((paramDataFileCache instanceof DataFileCacheSession))
    {
      this.defaultSpaceManager = new TableSpaceManagerSimple(paramDataFileCache);
    }
    else if ((paramDataFileCache instanceof TextCache))
    {
      this.defaultSpaceManager = new TableSpaceManagerSimple(paramDataFileCache);
    }
    else
    {
      int i = paramDataFileCache.database.logger.propMaxFreeBlocks;
      this.defaultSpaceManager = new TableSpaceManagerBlocks(this, 7, this.fileBlockSize, i, paramDataFileCache.getDataFileScale(), 0);
      if (!paramBoolean)
      {
        initialiseSpaces();
        paramDataFileCache.spaceManagerPosition = 0L;
      }
    }
    this.totalFragmentSize = paramDataFileCache.lostSpaceSize;
  }
  
  public TableSpaceManager getDefaultTableSpace()
  {
    return this.defaultSpaceManager;
  }
  
  public TableSpaceManager getTableSpace(int paramInt)
  {
    if (paramInt >= this.spaceIdSequence) {
      this.spaceIdSequence = (paramInt + 1);
    }
    return this.defaultSpaceManager;
  }
  
  public int getNewTableSpaceID()
  {
    return this.spaceIdSequence++;
  }
  
  public long getFileBlocks(int paramInt1, int paramInt2)
  {
    long l = this.cache.enlargeFileSpace(paramInt2 * this.fileBlockSize);
    return l;
  }
  
  public void freeTableSpace(int paramInt) {}
  
  public void freeTableSpace(int paramInt, DoubleIntIndex paramDoubleIntIndex, long paramLong1, long paramLong2, boolean paramBoolean)
  {
    this.totalFragmentSize += paramDoubleIntIndex.getTotalValues() * this.cache.getDataFileScale();
    if (paramBoolean)
    {
      if (this.cache.fileFreePosition == paramLong2)
      {
        this.cache.writeLock.lock();
        try
        {
          this.cache.fileFreePosition = paramLong1;
        }
        finally
        {
          this.cache.writeLock.unlock();
        }
      }
      else
      {
        this.totalFragmentSize += paramLong2 - paramLong1;
      }
      if (paramDoubleIntIndex.size() != 0)
      {
        this.lookup = new DoubleIntIndex(paramDoubleIntIndex.size(), true);
        paramDoubleIntIndex.copyTo(this.lookup);
        paramDoubleIntIndex.clear();
      }
    }
    else
    {
      paramDoubleIntIndex.compactLookupAsIntervals();
      paramDoubleIntIndex.setValuesSearchTarget();
      paramDoubleIntIndex.sort();
      int i = paramDoubleIntIndex.size() - paramDoubleIntIndex.capacity() / 2;
      if (i > 0)
      {
        paramDoubleIntIndex.removeRange(0, i);
        this.totalFragmentSize -= paramDoubleIntIndex.getTotalValues() * this.cache.getDataFileScale();
      }
    }
  }
  
  public long getLostBlocksSize()
  {
    return this.totalFragmentSize + this.defaultSpaceManager.getLostBlocksSize();
  }
  
  public int getFileBlockSize()
  {
    return 1048576 * this.cache.getDataFileScale() / 16;
  }
  
  public boolean isModified()
  {
    return true;
  }
  
  public void initialiseSpaces()
  {
    long l1 = this.cache.getFileFreePos();
    long l2 = (l1 + this.fileBlockSize) / this.fileBlockSize;
    long l3 = this.cache.enlargeFileSpace(l2 * this.fileBlockSize - l1);
    this.defaultSpaceManager.initialiseFileBlock(this.lookup, l3, this.cache.getFileFreePos());
    if (this.lookup != null)
    {
      this.totalFragmentSize -= this.lookup.getTotalValues() * this.cache.getDataFileScale();
      this.lookup = null;
    }
  }
  
  public void reset()
  {
    this.defaultSpaceManager.reset();
  }
  
  public boolean isMultiSpace()
  {
    return false;
  }
  
  public int getFileBlockItemCount()
  {
    return 65536;
  }
  
  public DirectoryBlockCachedObject[] getDirectoryList()
  {
    return new DirectoryBlockCachedObject[0];
  }
}


/* Location:              E:\java\java学习\hutubill\lib\all.jar!\org\hsqldb\persist\DataSpaceManagerSimple.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */
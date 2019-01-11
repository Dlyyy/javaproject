package org.hsqldb.persist;

import java.util.concurrent.locks.Lock;
import org.hsqldb.Database;
import org.hsqldb.error.Error;
import org.hsqldb.lib.FileAccess;
import org.hsqldb.lib.FileUtil;
import org.hsqldb.lib.Iterator;

public class DataFileCacheSession
  extends DataFileCache
{
  public DataFileCacheSession(Database paramDatabase, String paramString)
  {
    super(paramDatabase, paramString);
    this.logEvents = false;
  }
  
  protected void initParams(Database paramDatabase, String paramString, boolean paramBoolean)
  {
    this.dataFileName = (paramString + ".data.tmp");
    this.database = paramDatabase;
    this.fa = FileUtil.getFileUtil();
    this.dataFileScale = 64;
    this.cachedRowPadding = this.dataFileScale;
    this.initialFreePos = this.dataFileScale;
    this.maxCacheRows = 2048;
    this.maxCacheBytes = (this.maxCacheRows * 1024L);
    this.maxDataFileSize = (2147483647L * this.dataFileScale);
  }
  
  public void open(boolean paramBoolean)
  {
    try
    {
      this.dataFile = new RAFile(this.database.logger, this.dataFileName, false, false, false);
      this.fileFreePosition = this.initialFreePos;
      initBuffers();
      this.spaceManager = new DataSpaceManagerSimple(this, false);
    }
    catch (Throwable localThrowable)
    {
      this.database.logger.logWarningEvent("Failed to open Session RA file", localThrowable);
      release();
      throw Error.error(localThrowable, 452, 52, new Object[] { localThrowable.toString(), this.dataFileName });
    }
  }
  
  protected void setFileModified() {}
  
  public void close()
  {
    this.writeLock.lock();
    try
    {
      clear();
      if (this.dataFile != null)
      {
        this.dataFile.close();
        this.dataFile = null;
        this.fa.removeElement(this.dataFileName);
      }
    }
    catch (Throwable localThrowable)
    {
      this.database.logger.logWarningEvent("Failed to close Session RA file", localThrowable);
      throw Error.error(localThrowable, 452, 53, new Object[] { localThrowable.toString(), this.dataFileName });
    }
    finally
    {
      this.writeLock.unlock();
    }
  }
  
  public void adjustStoreCount(int paramInt)
  {
    this.writeLock.lock();
    try
    {
      this.storeCount += paramInt;
      if (this.storeCount == 0) {
        clear();
      }
    }
    finally
    {
      this.writeLock.unlock();
    }
  }
  
  protected void clear()
  {
    Iterator localIterator = this.cache.getIterator();
    while (localIterator.hasNext())
    {
      CachedObject localCachedObject = (CachedObject)localIterator.next();
      localCachedObject.setInMemory(false);
      localCachedObject.destroy();
    }
    this.cache.clear();
    this.fileStartFreePosition = (this.fileFreePosition = this.initialFreePos);
    initBuffers();
  }
}


/* Location:              E:\java\java学习\hutubill\lib\all.jar!\org\hsqldb\persist\DataFileCacheSession.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */
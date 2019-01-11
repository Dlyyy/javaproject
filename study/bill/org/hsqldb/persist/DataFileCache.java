package org.hsqldb.persist;

import java.io.IOException;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import org.hsqldb.Database;
import org.hsqldb.HsqlException;
import org.hsqldb.SchemaManager;
import org.hsqldb.Session;
import org.hsqldb.error.Error;
import org.hsqldb.lib.FileAccess;
import org.hsqldb.lib.FileArchiver;
import org.hsqldb.lib.FileUtil;
import org.hsqldb.lib.HsqlByteArrayOutputStream;
import org.hsqldb.lib.IntIndex;
import org.hsqldb.lib.StopWatch;
import org.hsqldb.map.BitMap;
import org.hsqldb.rowio.RowInputBinary180;
import org.hsqldb.rowio.RowInputBinaryDecode;
import org.hsqldb.rowio.RowInputInterface;
import org.hsqldb.rowio.RowOutputBinary180;
import org.hsqldb.rowio.RowOutputBinaryEncode;
import org.hsqldb.rowio.RowOutputInterface;

public class DataFileCache
{
  protected FileAccess fa;
  public static final int FLAG_ISSHADOWED = 1;
  public static final int FLAG_ISSAVED = 2;
  public static final int FLAG_ROWINFO = 3;
  public static final int FLAG_190 = 4;
  public static final int FLAG_HX = 5;
  static final int LONG_EMPTY_SIZE = 4;
  static final int LONG_FREE_POS_POS = 12;
  static final int INT_SPACE_PROPS_POS = 20;
  static final int INT_SPACE_LIST_POS = 24;
  static final int FLAGS_POS = 28;
  static final int MIN_INITIAL_FREE_POS = 32;
  public DataSpaceManager spaceManager;
  static final int initIOBufferSize = 4096;
  private static final int diskBlockSize = 4096;
  protected String dataFileName;
  protected String backupFileName;
  protected Database database;
  protected boolean logEvents = true;
  protected boolean fileModified;
  protected boolean cacheModified;
  protected int dataFileScale;
  protected boolean cacheReadonly;
  protected int cachedRowPadding;
  protected long initialFreePos;
  protected long lostSpaceSize;
  protected long spaceManagerPosition;
  protected long fileStartFreePosition;
  protected boolean hasRowInfo = false;
  protected int storeCount;
  protected RowInputInterface rowIn;
  public RowOutputInterface rowOut;
  public long maxDataFileSize;
  boolean is180;
  protected RandomAccessInterface dataFile;
  protected volatile long fileFreePosition;
  protected int maxCacheRows;
  protected long maxCacheBytes;
  protected Cache cache;
  private RAShadowFile shadowFile;
  ReadWriteLock lock = new ReentrantReadWriteLock();
  Lock readLock = this.lock.readLock();
  Lock writeLock = this.lock.writeLock();
  
  public DataFileCache(Database paramDatabase, String paramString)
  {
    initParams(paramDatabase, paramString, false);
    this.cache = new Cache(this);
  }
  
  public DataFileCache(Database paramDatabase, String paramString, boolean paramBoolean)
  {
    initParams(paramDatabase, paramString, true);
    this.cache = new Cache(this);
    try
    {
      if (this.database.logger.isStoredFileAccess()) {
        this.dataFile = RAFile.newScaledRAFile(this.database, this.dataFileName, false, 3);
      } else {
        this.dataFile = new RAFileSimple(this.database.logger, this.dataFileName, "rw");
      }
    }
    catch (Throwable localThrowable)
    {
      throw Error.error(452, localThrowable);
    }
    initNewFile();
    initBuffers();
    if (this.database.logger.getDataFileSpaces() > 0) {
      this.spaceManager = new DataSpaceManagerBlocks(this);
    } else {
      this.spaceManager = new DataSpaceManagerSimple(this, false);
    }
  }
  
  protected void initParams(Database paramDatabase, String paramString, boolean paramBoolean)
  {
    this.dataFileName = (paramString + ".data");
    this.backupFileName = (paramString + ".backup");
    this.database = paramDatabase;
    this.fa = paramDatabase.logger.getFileAccess();
    this.dataFileScale = paramDatabase.logger.getDataFileScale();
    this.cachedRowPadding = 8;
    if (this.dataFileScale > 8) {
      this.cachedRowPadding = this.dataFileScale;
    }
    this.initialFreePos = 32L;
    if (this.initialFreePos < this.dataFileScale) {
      this.initialFreePos = this.dataFileScale;
    }
    this.cacheReadonly = paramDatabase.isFilesReadOnly();
    this.maxCacheRows = paramDatabase.logger.getCacheMaxRows();
    this.maxCacheBytes = paramDatabase.logger.getCacheSize();
    this.maxDataFileSize = (2147483647L * this.dataFileScale * paramDatabase.logger.getDataFileFactor());
    if (paramBoolean)
    {
      this.dataFileName += ".new";
      this.backupFileName += ".new";
      this.maxCacheRows = 1024;
      this.maxCacheBytes = 4194304L;
    }
  }
  
  public void open(boolean paramBoolean)
  {
    if (this.database.logger.isStoredFileAccess())
    {
      openStoredFileAccess(paramBoolean);
      return;
    }
    this.fileFreePosition = this.initialFreePos;
    logInfoEvent("dataFileCache open start");
    try
    {
      boolean bool1 = this.database.logger.propNioDataFile;
      int i;
      if (this.database.isFilesInJar()) {
        i = 2;
      } else if (bool1) {
        i = 1;
      } else {
        i = 0;
      }
      if ((paramBoolean) || (this.database.isFilesInJar()))
      {
        this.dataFile = RAFile.newScaledRAFile(this.database, this.dataFileName, true, i);
        int j = getFlags();
        this.is180 = (!BitMap.isSet(j, 4));
        if (BitMap.isSet(j, 5)) {
          throw Error.error(453);
        }
        this.dataFile.seek(12L);
        this.fileFreePosition = this.dataFile.readLong();
        this.dataFile.seek(24L);
        this.spaceManagerPosition = (this.dataFile.readInt() * 4096L);
        initBuffers();
        this.spaceManager = new DataSpaceManagerSimple(this, true);
        return;
      }
      boolean bool2 = this.fa.isStreamElement(this.dataFileName);
      boolean bool3 = this.database.logger.propIncrementBackup;
      boolean bool4 = false;
      if (bool2)
      {
        this.dataFile = new RAFileSimple(this.database.logger, this.dataFileName, "r");
        long l = this.dataFile.length();
        int m = 0;
        if (l >= this.initialFreePos)
        {
          int n = getFlags();
          bool4 = BitMap.isSet(n, 2);
          bool3 = BitMap.isSet(n, 1);
          this.is180 = (!BitMap.isSet(n, 4));
          if (BitMap.isSet(n, 5)) {
            m = 1;
          }
        }
        this.dataFile.close();
        if (m != 0) {
          throw Error.error(453);
        }
        if ((!this.database.logger.propLargeData) && (l > this.maxDataFileSize / 8L * 7L))
        {
          this.database.logger.propLargeData = true;
          this.maxDataFileSize = (2147483647L * this.dataFileScale * this.database.logger.getDataFileFactor());
        }
        if (l > this.maxDataFileSize) {
          throw Error.error(468, String.valueOf(this.maxDataFileSize));
        }
        if ((bool4) && (bool3))
        {
          boolean bool6 = this.fa.isStreamElement(this.backupFileName);
          if (bool6) {
            logInfoEvent("data file was not modified but inc backup exists");
          }
        }
      }
      if (bool4)
      {
        if (bool3)
        {
          deleteBackup();
        }
        else
        {
          boolean bool5 = this.fa.isStreamElement(this.backupFileName);
          if (!bool5) {
            backupDataFile(false);
          }
        }
      }
      else if (bool3)
      {
        if (bool2)
        {
          bool2 = restoreBackupIncremental();
          if (!bool2)
          {
            this.database.logger.logSevereEvent("DataFileCache data file modified but no backup exists", null);
            throw Error.error(454);
          }
        }
      }
      else {
        bool2 = restoreBackup();
      }
      this.dataFile = RAFile.newScaledRAFile(this.database, this.dataFileName, paramBoolean, i);
      if (bool2)
      {
        int k = getFlags();
        this.is180 = (!BitMap.isSet(k, 4));
        this.dataFile.seek(4L);
        this.lostSpaceSize = this.dataFile.readLong();
        this.dataFile.seek(12L);
        this.fileFreePosition = this.dataFile.readLong();
        this.fileStartFreePosition = this.fileFreePosition;
        this.dataFile.seek(24L);
        this.spaceManagerPosition = (this.dataFile.readInt() * 4096L);
        openShadowFile();
      }
      else
      {
        initNewFile();
      }
      initBuffers();
      this.fileModified = false;
      this.cacheModified = false;
      if (this.database.logger.getDataFileSpaces() > 0) {
        this.spaceManager = new DataSpaceManagerBlocks(this);
      } else {
        this.spaceManager = new DataSpaceManagerSimple(this, false);
      }
      logInfoEvent("dataFileCache open end");
    }
    catch (HsqlException localHsqlException)
    {
      throw localHsqlException;
    }
    catch (Throwable localThrowable)
    {
      logSevereEvent("DataFileCache.open", localThrowable);
      release();
      throw Error.error(localThrowable, 452, 52, new Object[] { localThrowable.toString(), this.dataFileName });
    }
  }
  
  boolean setDataSpaceManager()
  {
    this.writeLock.lock();
    int i = this.database.logger.propFileSpaceValue;
    try
    {
      if ((i > 0) && (this.spaceManagerPosition == 0L))
      {
        this.spaceManager.reset();
        this.spaceManager = new DataSpaceManagerBlocks(this);
        bool = true;
        return bool;
      }
      if ((i == 0) && (this.spaceManagerPosition != 0L))
      {
        this.spaceManager.reset();
        this.spaceManager = new DataSpaceManagerSimple(this, false);
        bool = true;
        return bool;
      }
      boolean bool = false;
      return bool;
    }
    finally
    {
      this.writeLock.unlock();
    }
  }
  
  void openStoredFileAccess(boolean paramBoolean)
  {
    this.fileFreePosition = this.initialFreePos;
    logInfoEvent("dataFileCache open start");
    try
    {
      int i = 3;
      if (paramBoolean)
      {
        this.dataFile = RAFile.newScaledRAFile(this.database, this.dataFileName, paramBoolean, i);
        this.dataFile.seek(28L);
        int j = this.dataFile.readInt();
        this.is180 = (!BitMap.isSet(j, 4));
        this.dataFile.seek(12L);
        this.fileFreePosition = this.dataFile.readLong();
        initBuffers();
        return;
      }
      long l = 0L;
      boolean bool1 = this.fa.isStreamElement(this.dataFileName);
      boolean bool2 = this.database.logger.propIncrementBackup;
      int k = this.database.getProperties().getDBModified() == 1 ? 1 : 0;
      if ((bool1) && (k != 0)) {
        if (bool2) {
          bool1 = restoreBackupIncremental();
        } else {
          bool1 = restoreBackup();
        }
      }
      this.dataFile = RAFile.newScaledRAFile(this.database, this.dataFileName, paramBoolean, i);
      if (bool1)
      {
        this.dataFile.seek(4L);
        l = this.dataFile.readLong();
        this.dataFile.seek(12L);
        this.fileFreePosition = this.dataFile.readLong();
        this.fileStartFreePosition = this.fileFreePosition;
        this.dataFile.seek(28L);
        int m = this.dataFile.readInt();
        this.is180 = (!BitMap.isSet(m, 4));
        openShadowFile();
      }
      else
      {
        initNewFile();
      }
      initBuffers();
      this.fileModified = false;
      this.cacheModified = false;
      this.spaceManager = new DataSpaceManagerSimple(this, false);
      logInfoEvent("dataFileCache open end");
    }
    catch (Throwable localThrowable)
    {
      logSevereEvent("dataFileCache open failed", localThrowable);
      release();
      throw Error.error(localThrowable, 452, 52, new Object[] { localThrowable.toString(), this.dataFileName });
    }
  }
  
  void initNewFile()
  {
    try
    {
      this.fileFreePosition = this.initialFreePos;
      this.fileStartFreePosition = this.initialFreePos;
      this.dataFile.seek(12L);
      this.dataFile.writeLong(this.fileFreePosition);
      int i = this.dataFileScale;
      i |= this.database.logger.getDataFileSpaces() << 16;
      this.dataFile.seek(20L);
      this.dataFile.writeInt(i);
      int j = 0;
      if (this.database.logger.propIncrementBackup) {
        j = BitMap.set(j, 1);
      }
      j = BitMap.set(j, 2);
      j = BitMap.set(j, 4);
      setFlags(j);
      this.is180 = false;
    }
    catch (Throwable localThrowable)
    {
      throw Error.error(452, localThrowable);
    }
  }
  
  private void openShadowFile()
  {
    if ((this.database.logger.propIncrementBackup) && (this.fileFreePosition != this.initialFreePos)) {
      this.shadowFile = new RAShadowFile(this.database, this.dataFile, this.backupFileName, this.fileFreePosition, 16384);
    }
  }
  
  void setIncrementBackup(boolean paramBoolean)
  {
    this.writeLock.lock();
    try
    {
      int i = getFlags();
      if (paramBoolean) {
        i = BitMap.set(i, 1);
      } else {
        i = BitMap.unset(i, 1);
      }
      setFlags(i);
      this.fileModified = true;
    }
    catch (Throwable localThrowable)
    {
      logSevereEvent("DataFileCache.setIncrementalBackup", localThrowable);
    }
    finally
    {
      this.writeLock.unlock();
    }
  }
  
  private boolean restoreBackup()
  {
    try
    {
      FileAccess localFileAccess = this.database.logger.getFileAccess();
      deleteFile(this.database, this.dataFileName);
      if (localFileAccess.isStreamElement(this.backupFileName))
      {
        FileArchiver.unarchive(this.backupFileName, this.dataFileName, localFileAccess, 1);
        return true;
      }
      return false;
    }
    catch (Throwable localThrowable)
    {
      this.database.logger.logSevereEvent("DataFileCache.restoreBackup", localThrowable);
      throw Error.error(localThrowable, 452, 26, new Object[] { localThrowable.toString(), this.backupFileName });
    }
  }
  
  private boolean restoreBackupIncremental()
  {
    try
    {
      FileAccess localFileAccess = this.database.logger.getFileAccess();
      if (localFileAccess.isStreamElement(this.backupFileName))
      {
        RAShadowFile.restoreFile(this.database, this.backupFileName, this.dataFileName);
        deleteFile(this.database, this.backupFileName);
        return true;
      }
      return false;
    }
    catch (Throwable localThrowable)
    {
      this.database.logger.logSevereEvent("DataFileCache.restoreBackupIncremental", localThrowable);
      throw Error.error(452, localThrowable);
    }
  }
  
  public void release()
  {
    this.writeLock.lock();
    try
    {
      if (this.dataFile == null) {
        return;
      }
      if (this.shadowFile != null)
      {
        this.shadowFile.close();
        this.shadowFile = null;
      }
      this.dataFile.close();
      logDetailEvent("dataFileCache file closed");
      this.dataFile = null;
    }
    catch (Throwable localThrowable)
    {
      logSevereEvent("DataFileCache.release", localThrowable);
    }
    finally
    {
      this.writeLock.unlock();
    }
  }
  
  public void close()
  {
    this.writeLock.lock();
    try
    {
      if (this.dataFile == null) {
        return;
      }
      reset();
      this.dataFile.close();
      logDetailEvent("dataFileCache file close end");
      this.dataFile = null;
      int i = this.fileFreePosition == this.initialFreePos ? 1 : 0;
      if (i != 0)
      {
        deleteFile();
        deleteBackup();
      }
    }
    catch (HsqlException localHsqlException)
    {
      throw localHsqlException;
    }
    catch (Throwable localThrowable)
    {
      logSevereEvent("DataFileCache.close", localThrowable);
      throw Error.error(localThrowable, 452, 53, new Object[] { localThrowable.toString(), this.dataFileName });
    }
    finally
    {
      this.writeLock.unlock();
    }
  }
  
  protected void clear()
  {
    this.writeLock.lock();
    try
    {
      this.cache.clear();
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
    }
    finally
    {
      this.writeLock.unlock();
    }
  }
  
  public void reopen()
  {
    this.writeLock.lock();
    try
    {
      openShadowFile();
      this.spaceManager.initialiseSpaces();
    }
    finally
    {
      this.writeLock.unlock();
    }
  }
  
  public void reset()
  {
    this.writeLock.lock();
    try
    {
      if (this.cacheReadonly) {
        return;
      }
      logInfoEvent("dataFileCache commit start");
      this.spaceManager.reset();
      this.cache.saveAll();
      long l = this.spaceManager.getLostBlocksSize();
      this.dataFile.seek(4L);
      this.dataFile.writeLong(l);
      this.dataFile.seek(12L);
      this.dataFile.writeLong(this.fileFreePosition);
      int i = this.dataFileScale;
      i |= this.database.logger.getDataFileSpaces() << 16;
      this.dataFile.seek(20L);
      this.dataFile.writeInt(i);
      int j = (int)(this.spaceManagerPosition / 4096L);
      this.dataFile.seek(24L);
      this.dataFile.writeInt(j);
      int k = getFlags();
      k = BitMap.set(k, 2);
      setFlags(k);
      logDetailEvent("file sync end");
      this.fileModified = false;
      this.cacheModified = false;
      this.fileStartFreePosition = this.fileFreePosition;
      if (this.shadowFile != null)
      {
        this.shadowFile.close();
        this.shadowFile = null;
      }
      logInfoEvent("dataFileCache commit end");
    }
    catch (Throwable localThrowable)
    {
      logSevereEvent("DataFileCache.reset commit", localThrowable);
      throw Error.error(localThrowable, 452, 53, new Object[] { localThrowable.toString(), this.dataFileName });
    }
    finally
    {
      this.writeLock.unlock();
    }
  }
  
  protected void initBuffers()
  {
    if (this.rowOut == null) {
      if (this.is180) {
        this.rowOut = new RowOutputBinary180(4096, this.cachedRowPadding);
      } else {
        this.rowOut = new RowOutputBinaryEncode(this.database.logger.getCrypto(), 4096, this.cachedRowPadding);
      }
    }
    if (this.rowIn == null) {
      if (this.is180) {
        this.rowIn = new RowInputBinary180(new byte['က']);
      } else {
        this.rowIn = new RowInputBinaryDecode(this.database.logger.getCrypto(), new byte['က']);
      }
    }
  }
  
  DataFileDefrag defrag(Session paramSession)
  {
    this.writeLock.lock();
    try
    {
      this.cache.saveAll();
      DataFileDefrag localDataFileDefrag1 = new DataFileDefrag(this.database, this);
      localDataFileDefrag1.process(paramSession);
      close();
      this.cache.clear();
      if (!this.database.logger.propIncrementBackup) {
        backupNewDataFile(true);
      }
      this.database.schemaManager.setTempIndexRoots(localDataFileDefrag1.getIndexRoots());
      try
      {
        this.database.logger.log.writeScript(false);
      }
      finally
      {
        this.database.schemaManager.setTempIndexRoots((long[][])null);
      }
      this.database.getProperties().setProperty("hsqldb.script_format", this.database.logger.propScriptFormat);
      this.database.getProperties().setDBModified(2);
      this.database.logger.log.closeLog();
      this.database.logger.log.deleteLog();
      this.database.logger.log.renameNewScript();
      renameBackupFile();
      renameDataFile();
      this.database.getProperties().setDBModified(0);
      open(false);
      if (this.database.logger.log.dbLogWriter != null) {
        this.database.logger.log.openLog();
      }
      DataFileDefrag localDataFileDefrag2 = localDataFileDefrag1;
      return localDataFileDefrag2;
    }
    finally
    {
      this.writeLock.unlock();
    }
  }
  
  public void remove(CachedObject paramCachedObject)
  {
    release(paramCachedObject.getPos());
  }
  
  public void removePersistence(CachedObject paramCachedObject) {}
  
  public void add(CachedObject paramCachedObject, boolean paramBoolean)
  {
    this.writeLock.lock();
    try
    {
      this.cacheModified = true;
      this.cache.put(paramCachedObject);
      if (paramBoolean) {
        paramCachedObject.keepInMemory(true);
      }
      if (paramCachedObject.getStorageSize() > 4096) {
        this.rowOut.reset(paramCachedObject.getStorageSize());
      }
    }
    finally
    {
      this.writeLock.unlock();
    }
  }
  
  public CachedObject get(CachedObject paramCachedObject, PersistentStore paramPersistentStore, boolean paramBoolean)
  {
    this.readLock.lock();
    long l;
    try
    {
      CachedObject localCachedObject;
      if (paramCachedObject.isInMemory())
      {
        if (paramBoolean) {
          paramCachedObject.keepInMemory(true);
        }
        localCachedObject = paramCachedObject;
        return localCachedObject;
      }
      l = paramCachedObject.getPos();
      if (l < 0L)
      {
        localCachedObject = null;
        return localCachedObject;
      }
      paramCachedObject = this.cache.get(l);
      if (paramCachedObject != null)
      {
        if (paramBoolean) {
          paramCachedObject.keepInMemory(true);
        }
        localCachedObject = paramCachedObject;
        return localCachedObject;
      }
    }
    finally
    {
      this.readLock.unlock();
    }
    return getFromFile(l, paramPersistentStore, paramBoolean);
  }
  
  public CachedObject get(long paramLong, int paramInt, PersistentStore paramPersistentStore, boolean paramBoolean)
  {
    if (paramLong < 0L) {
      return null;
    }
    this.readLock.lock();
    try
    {
      CachedObject localCachedObject1 = this.cache.get(paramLong);
      if (localCachedObject1 != null)
      {
        if (paramBoolean) {
          localCachedObject1.keepInMemory(true);
        }
        CachedObject localCachedObject2 = localCachedObject1;
        return localCachedObject2;
      }
    }
    finally
    {
      this.readLock.unlock();
    }
    return getFromFile(paramLong, paramInt, paramPersistentStore, paramBoolean);
  }
  
  public CachedObject get(long paramLong, PersistentStore paramPersistentStore, boolean paramBoolean)
  {
    if (paramLong < 0L) {
      return null;
    }
    this.readLock.lock();
    try
    {
      CachedObject localCachedObject1 = this.cache.get(paramLong);
      if (localCachedObject1 != null)
      {
        if (paramBoolean) {
          localCachedObject1.keepInMemory(true);
        }
        CachedObject localCachedObject2 = localCachedObject1;
        return localCachedObject2;
      }
    }
    finally
    {
      this.readLock.unlock();
    }
    return getFromFile(paramLong, paramPersistentStore, paramBoolean);
  }
  
  private CachedObject getFromFile(long paramLong, PersistentStore paramPersistentStore, boolean paramBoolean)
  {
    CachedObject localCachedObject1 = null;
    this.writeLock.lock();
    try
    {
      localCachedObject1 = this.cache.get(paramLong);
      if (localCachedObject1 != null)
      {
        if (paramBoolean) {
          localCachedObject1.keepInMemory(true);
        }
        CachedObject localCachedObject2 = localCachedObject1;
        return localCachedObject2;
      }
      for (int i = 0; i < 2; i++) {
        try
        {
          readObject(paramLong);
          localCachedObject1 = paramPersistentStore.get(this.rowIn);
          if (localCachedObject1 == null) {
            throw Error.error(467, "position " + paramLong);
          }
        }
        catch (Throwable localThrowable)
        {
          HsqlException localHsqlException2;
          if ((localThrowable instanceof OutOfMemoryError))
          {
            this.cache.clearUnchanged();
            System.gc();
            if (i > 0)
            {
              logInfoEvent(this.dataFileName + " getFromFile out of mem " + paramLong);
              localHsqlException2 = Error.error(460, localThrowable);
              localHsqlException2.info = this.rowIn;
              throw localHsqlException2;
            }
          }
          else
          {
            if ((localThrowable instanceof HsqlException))
            {
              ((HsqlException)localThrowable).info = this.rowIn;
              throw ((HsqlException)localThrowable);
            }
            localHsqlException2 = Error.error(467, localThrowable);
            localHsqlException2.info = this.rowIn;
            throw localHsqlException2;
          }
        }
      }
      this.cache.put(localCachedObject1);
      if (paramBoolean) {
        localCachedObject1.keepInMemory(true);
      }
      paramPersistentStore.set(localCachedObject1);
      CachedObject localCachedObject3 = localCachedObject1;
      return localCachedObject3;
    }
    catch (HsqlException localHsqlException1)
    {
      logSevereEvent(this.dataFileName + " getFromFile failed " + paramLong, localHsqlException1);
      throw localHsqlException1;
    }
    finally
    {
      this.writeLock.unlock();
    }
  }
  
  private CachedObject getFromFile(long paramLong, int paramInt, PersistentStore paramPersistentStore, boolean paramBoolean)
  {
    CachedObject localCachedObject1 = null;
    this.writeLock.lock();
    try
    {
      localCachedObject1 = this.cache.get(paramLong);
      if (localCachedObject1 != null)
      {
        if (paramBoolean) {
          localCachedObject1.keepInMemory(true);
        }
        CachedObject localCachedObject2 = localCachedObject1;
        return localCachedObject2;
      }
      int i = 0;
      while (i < 2) {
        try
        {
          readObject(paramLong, paramInt);
          localCachedObject1 = paramPersistentStore.get(this.rowIn);
        }
        catch (OutOfMemoryError localOutOfMemoryError)
        {
          this.cache.clearUnchanged();
          System.gc();
          if (i > 0)
          {
            logSevereEvent(this.dataFileName + " getFromFile out of mem " + paramLong, localOutOfMemoryError);
            throw localOutOfMemoryError;
          }
          i++;
        }
      }
      this.cache.putUsingReserve(localCachedObject1);
      if (paramBoolean) {
        localCachedObject1.keepInMemory(true);
      }
      paramPersistentStore.set(localCachedObject1);
      CachedObject localCachedObject3 = localCachedObject1;
      return localCachedObject3;
    }
    catch (HsqlException localHsqlException)
    {
      logSevereEvent(this.dataFileName + " getFromFile failed " + paramLong, localHsqlException);
      throw localHsqlException;
    }
    finally
    {
      this.writeLock.unlock();
    }
  }
  
  RowInputInterface getRaw(long paramLong)
  {
    this.writeLock.lock();
    try
    {
      readObject(paramLong);
      RowInputInterface localRowInputInterface = this.rowIn;
      return localRowInputInterface;
    }
    finally
    {
      this.writeLock.unlock();
    }
  }
  
  private void readObject(long paramLong)
  {
    try
    {
      this.dataFile.seek(paramLong * this.dataFileScale);
      int i = this.dataFile.readInt();
      this.rowIn.resetRow(paramLong, i);
      this.dataFile.read(this.rowIn.getBuffer(), 4, i - 4);
    }
    catch (Throwable localThrowable)
    {
      logSevereEvent("DataFileCache.readObject", localThrowable, paramLong);
      HsqlException localHsqlException = Error.error(466, localThrowable);
      if (this.rowIn.getPos() != paramLong) {
        this.rowIn.resetRow(paramLong, 0);
      }
      localHsqlException.info = this.rowIn;
      throw localHsqlException;
    }
  }
  
  protected void readObject(long paramLong, int paramInt)
  {
    try
    {
      this.rowIn.resetBlock(paramLong, paramInt);
      this.dataFile.seek(paramLong * this.dataFileScale);
      this.dataFile.read(this.rowIn.getBuffer(), 0, paramInt);
    }
    catch (Throwable localThrowable)
    {
      logSevereEvent("DataFileCache.readObject", localThrowable, paramLong);
      HsqlException localHsqlException = Error.error(466, localThrowable);
      localHsqlException.info = this.rowIn;
      throw localHsqlException;
    }
  }
  
  public void releaseRange(long paramLong1, long paramLong2)
  {
    this.writeLock.lock();
    try
    {
      this.cacheModified = true;
      this.cache.releaseRange(paramLong1, paramLong2);
    }
    finally
    {
      this.writeLock.unlock();
    }
  }
  
  public void releaseRange(IntIndex paramIntIndex, int paramInt)
  {
    this.writeLock.lock();
    try
    {
      this.cacheModified = true;
      this.cache.releaseRange(paramIntIndex, paramInt);
    }
    finally
    {
      this.writeLock.unlock();
    }
  }
  
  public CachedObject release(long paramLong)
  {
    this.writeLock.lock();
    try
    {
      this.cacheModified = true;
      CachedObject localCachedObject = this.cache.release(paramLong);
      return localCachedObject;
    }
    finally
    {
      this.writeLock.unlock();
    }
  }
  
  protected void saveRows(CachedObject[] paramArrayOfCachedObject, int paramInt1, int paramInt2)
  {
    if (paramInt2 == 0) {
      return;
    }
    int i = copyShadow(paramArrayOfCachedObject, paramInt1, paramInt2);
    long l1 = this.cache.saveAllTimer.elapsedTime();
    long l2 = 0L;
    this.cache.saveAllTimer.start();
    if (i > 0) {
      setFileModified();
    }
    for (int j = paramInt1; j < paramInt1 + paramInt2; j++)
    {
      CachedObject localCachedObject = paramArrayOfCachedObject[j];
      saveRowNoLock(localCachedObject);
      paramArrayOfCachedObject[j] = null;
      l2 += localCachedObject.getStorageSize();
    }
    this.cache.saveAllTimer.stop();
    this.cache.logSaveRowsEvent(paramInt2, l2, l1);
  }
  
  public void saveRow(CachedObject paramCachedObject)
  {
    this.writeLock.lock();
    try
    {
      copyShadow(paramCachedObject);
      setFileModified();
      saveRowNoLock(paramCachedObject);
    }
    finally
    {
      this.writeLock.unlock();
    }
  }
  
  public void saveRowOutput(long paramLong)
  {
    try
    {
      this.dataFile.seek(paramLong * this.dataFileScale);
      this.dataFile.write(this.rowOut.getOutputStream().getBuffer(), 0, this.rowOut.getOutputStream().size());
    }
    catch (Throwable localThrowable)
    {
      logSevereEvent("DataFileCache.saveRowOutput", localThrowable, paramLong);
      throw Error.error(466, localThrowable);
    }
  }
  
  protected void saveRowNoLock(CachedObject paramCachedObject)
  {
    try
    {
      this.rowOut.reset();
      paramCachedObject.write(this.rowOut);
      this.dataFile.seek(paramCachedObject.getPos() * this.dataFileScale);
      this.dataFile.write(this.rowOut.getOutputStream().getBuffer(), 0, this.rowOut.getOutputStream().size());
      paramCachedObject.setChanged(false);
    }
    catch (Throwable localThrowable)
    {
      logSevereEvent("DataFileCache.saveRowNoLock", localThrowable, paramCachedObject.getPos());
      throw Error.error(466, localThrowable);
    }
  }
  
  protected int copyShadow(CachedObject[] paramArrayOfCachedObject, int paramInt1, int paramInt2)
  {
    int i = 0;
    if (this.shadowFile != null)
    {
      long l1 = this.cache.shadowTimer.elapsedTime();
      long l2 = 0L;
      this.cache.shadowTimer.start();
      try
      {
        for (int j = paramInt1; j < paramInt1 + paramInt2; j++)
        {
          CachedObject localCachedObject = paramArrayOfCachedObject[j];
          l2 = localCachedObject.getPos() * this.dataFileScale;
          i += this.shadowFile.copy(l2, localCachedObject.getStorageSize());
        }
        if (i > 0) {
          this.shadowFile.synch();
        }
      }
      catch (Throwable localThrowable)
      {
        logSevereEvent("DataFileCache.copyShadow", localThrowable, l2);
        throw Error.error(466, localThrowable);
      }
      this.cache.shadowTimer.stop();
      if (i > 0)
      {
        l1 = this.cache.shadowTimer.elapsedTime() - l1;
        logDetailEvent("copyShadow [size, time] " + this.shadowFile.getSavedLength() + " " + l1);
      }
    }
    return i;
  }
  
  protected int copyShadow(CachedObject paramCachedObject)
  {
    if (this.shadowFile != null)
    {
      long l = paramCachedObject.getPos() * this.dataFileScale;
      try
      {
        int i = this.shadowFile.copy(l, paramCachedObject.getStorageSize());
        this.shadowFile.synch();
        return i;
      }
      catch (Throwable localThrowable)
      {
        logSevereEvent("DataFileCache.copyShadow", localThrowable, paramCachedObject.getPos());
        throw Error.error(466, localThrowable);
      }
    }
    return 0;
  }
  
  void backupDataFile(boolean paramBoolean)
  {
    backupFile(this.database, this.dataFileName, this.backupFileName, paramBoolean);
  }
  
  void backupNewDataFile(boolean paramBoolean)
  {
    backupFile(this.database, this.dataFileName + ".new", this.backupFileName, paramBoolean);
  }
  
  static void backupFile(Database paramDatabase, String paramString1, String paramString2, boolean paramBoolean)
  {
    try
    {
      FileAccess localFileAccess = paramDatabase.logger.getFileAccess();
      if (paramDatabase.logger.propIncrementBackup)
      {
        if (localFileAccess.isStreamElement(paramString2))
        {
          deleteFile(paramDatabase, paramString2);
          if (localFileAccess.isStreamElement(paramString2)) {
            throw Error.error(466, "cannot delete old backup file");
          }
        }
        return;
      }
      if (localFileAccess.isStreamElement(paramString1))
      {
        if (paramBoolean)
        {
          paramString2 = paramString2 + ".new";
        }
        else
        {
          deleteFile(paramDatabase, paramString2);
          if (localFileAccess.isStreamElement(paramString2)) {
            throw Error.error(466, "cannot delete old backup file");
          }
        }
        FileArchiver.archive(paramString1, paramString2, localFileAccess, 1);
      }
    }
    catch (Throwable localThrowable)
    {
      paramDatabase.logger.logSevereEvent("DataFileCache.backupFile", localThrowable);
      throw Error.error(466, localThrowable);
    }
  }
  
  void renameBackupFile()
  {
    renameBackupFile(this.database, this.backupFileName);
  }
  
  static void renameBackupFile(Database paramDatabase, String paramString)
  {
    FileAccess localFileAccess = paramDatabase.logger.getFileAccess();
    if (paramDatabase.logger.propIncrementBackup)
    {
      deleteFile(paramDatabase, paramString);
      return;
    }
    if (localFileAccess.isStreamElement(paramString + ".new"))
    {
      deleteFile(paramDatabase, paramString);
      localFileAccess.renameElement(paramString + ".new", paramString);
    }
  }
  
  void renameDataFile()
  {
    renameDataFile(this.database, this.dataFileName);
  }
  
  static void renameDataFile(Database paramDatabase, String paramString)
  {
    FileAccess localFileAccess = paramDatabase.logger.getFileAccess();
    if (localFileAccess.isStreamElement(paramString + ".new"))
    {
      deleteFile(paramDatabase, paramString);
      localFileAccess.renameElement(paramString + ".new", paramString);
    }
  }
  
  void deleteFile()
  {
    deleteFile(this.database, this.dataFileName);
  }
  
  static void deleteFile(Database paramDatabase, String paramString)
  {
    FileAccess localFileAccess = paramDatabase.logger.getFileAccess();
    localFileAccess.removeElement(paramString);
    if (paramDatabase.logger.isStoredFileAccess()) {
      return;
    }
    if (localFileAccess.isStreamElement(paramString))
    {
      paramDatabase.logger.log.deleteOldDataFiles();
      localFileAccess.removeElement(paramString);
      if (localFileAccess.isStreamElement(paramString))
      {
        String str = FileUtil.newDiscardFileName(paramString);
        localFileAccess.renameElement(paramString, str);
      }
    }
  }
  
  void deleteBackup()
  {
    deleteFile(this.database, this.backupFileName);
  }
  
  public long enlargeFileSpace(long paramLong)
  {
    this.writeLock.lock();
    try
    {
      long l1 = this.fileFreePosition;
      if (l1 + paramLong > this.maxDataFileSize)
      {
        logSevereEvent("data file reached maximum allowed size: " + this.dataFileName + " " + this.maxDataFileSize, null);
        throw Error.error(468);
      }
      boolean bool = this.dataFile.ensureLength(l1 + paramLong);
      if (!bool)
      {
        logSevereEvent("data file cannot be enlarged - disk space: " + this.dataFileName + " " + (l1 + paramLong), null);
        throw Error.error(468);
      }
      this.fileFreePosition += paramLong;
      long l2 = l1;
      return l2;
    }
    finally
    {
      this.writeLock.unlock();
    }
  }
  
  public int capacity()
  {
    return this.maxCacheRows;
  }
  
  public long bytesCapacity()
  {
    return this.maxCacheBytes;
  }
  
  public long getTotalCachedBlockSize()
  {
    return this.cache.getTotalCachedBlockSize();
  }
  
  public long getLostBlockSize()
  {
    return this.spaceManager.getLostBlocksSize();
  }
  
  public long getFileFreePos()
  {
    return this.fileFreePosition;
  }
  
  public int getCachedObjectCount()
  {
    return this.cache.size();
  }
  
  public String getFileName()
  {
    return this.dataFileName;
  }
  
  public int getDataFileScale()
  {
    return this.dataFileScale;
  }
  
  public boolean hasRowInfo()
  {
    return this.hasRowInfo;
  }
  
  public boolean isFileModified()
  {
    return this.fileModified;
  }
  
  public boolean isModified()
  {
    return this.cacheModified;
  }
  
  public boolean isFileOpen()
  {
    return this.dataFile != null;
  }
  
  protected void setFileModified()
  {
    try
    {
      if (!this.fileModified)
      {
        int i = getFlags();
        i = BitMap.unset(i, 2);
        setFlags(i);
        logDetailEvent("setFileModified flag set ");
        this.fileModified = true;
      }
    }
    catch (Throwable localThrowable)
    {
      logSevereEvent("DataFileCache.setFileModified", localThrowable);
      throw Error.error(466, localThrowable);
    }
  }
  
  public int getFlags()
    throws IOException
  {
    this.dataFile.seek(28L);
    int i = this.dataFile.readInt();
    return i;
  }
  
  private void setFlags(int paramInt)
    throws IOException
  {
    this.dataFile.seek(28L);
    this.dataFile.writeInt(paramInt);
    this.dataFile.synch();
  }
  
  public boolean isDataReadOnly()
  {
    return this.cacheReadonly;
  }
  
  public RAShadowFile getShadowFile()
  {
    return this.shadowFile;
  }
  
  private void logSevereEvent(String paramString, Throwable paramThrowable, long paramLong)
  {
    if (this.logEvents)
    {
      StringBuffer localStringBuffer = new StringBuffer(paramString);
      localStringBuffer.append(' ').append(paramLong);
      paramString = localStringBuffer.toString();
      this.database.logger.logSevereEvent(paramString, paramThrowable);
    }
  }
  
  void logSevereEvent(String paramString, Throwable paramThrowable)
  {
    if (this.logEvents) {
      this.database.logger.logSevereEvent(paramString, paramThrowable);
    }
  }
  
  void logInfoEvent(String paramString)
  {
    if (this.logEvents) {
      this.database.logger.logInfoEvent(paramString);
    }
  }
  
  void logDetailEvent(String paramString)
  {
    if (this.logEvents) {
      this.database.logger.logDetailEvent(paramString);
    }
  }
}


/* Location:              E:\java\java学习\hutubill\lib\all.jar!\org\hsqldb\persist\DataFileCache.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */
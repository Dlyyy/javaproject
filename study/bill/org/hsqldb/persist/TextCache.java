package org.hsqldb.persist;

import java.io.UnsupportedEncodingException;
import java.util.concurrent.locks.Lock;
import org.hsqldb.Database;
import org.hsqldb.DatabaseType;
import org.hsqldb.HsqlException;
import org.hsqldb.Table;
import org.hsqldb.error.Error;
import org.hsqldb.lib.FileUtil;
import org.hsqldb.lib.HsqlByteArrayOutputStream;
import org.hsqldb.lib.LongKeyHashMap;
import org.hsqldb.rowio.RowInputText;
import org.hsqldb.rowio.RowInputTextQuoted;
import org.hsqldb.rowio.RowOutputInterface;
import org.hsqldb.rowio.RowOutputText;
import org.hsqldb.rowio.RowOutputTextQuoted;

public class TextCache
  extends DataFileCache
{
  TextFileSettings textFileSettings;
  protected String header;
  protected Table table;
  private LongKeyHashMap uncommittedCache;
  HsqlByteArrayOutputStream buffer = new HsqlByteArrayOutputStream(128);
  
  TextCache(Table paramTable, String paramString)
  {
    super(paramTable.database, paramString);
    this.table = paramTable;
    this.uncommittedCache = new LongKeyHashMap();
  }
  
  protected void initParams(Database paramDatabase, String paramString, boolean paramBoolean)
  {
    this.database = paramDatabase;
    this.fa = FileUtil.getFileUtil();
    this.textFileSettings = new TextFileSettings(paramDatabase, paramString);
    this.dataFileName = this.textFileSettings.getFileName();
    if (this.dataFileName == null) {
      throw Error.error(301);
    }
    this.dataFileName = ((FileUtil)this.fa).canonicalOrAbsolutePath(this.dataFileName);
    this.maxCacheRows = this.textFileSettings.getMaxCacheRows();
    this.maxCacheBytes = this.textFileSettings.getMaxCacheBytes();
    this.maxDataFileSize = 274877906816L;
    this.cachedRowPadding = 1;
    this.dataFileScale = 1;
  }
  
  protected void initBuffers()
  {
    if ((this.textFileSettings.isQuoted) || (this.textFileSettings.isAllQuoted))
    {
      this.rowIn = new RowInputTextQuoted(this.textFileSettings);
      this.rowOut = new RowOutputTextQuoted(this.textFileSettings);
    }
    else
    {
      this.rowIn = new RowInputText(this.textFileSettings);
      this.rowOut = new RowOutputText(this.textFileSettings);
    }
  }
  
  public void open(boolean paramBoolean)
  {
    this.fileFreePosition = 0L;
    try
    {
      int i = this.database.getType() == DatabaseType.DB_RES ? 2 : 5;
      this.dataFile = RAFile.newScaledRAFile(this.database, this.dataFileName, paramBoolean, i);
      this.fileFreePosition = this.dataFile.length();
      if (this.fileFreePosition > this.maxDataFileSize) {
        throw Error.error(468);
      }
      initBuffers();
      this.spaceManager = new DataSpaceManagerSimple(this, paramBoolean);
    }
    catch (Throwable localThrowable)
    {
      throw Error.error(localThrowable, 452, 42, new Object[] { localThrowable.toString(), this.dataFileName });
    }
    this.cacheReadonly = paramBoolean;
  }
  
  public void release()
  {
    close();
  }
  
  public void close()
  {
    if (this.dataFile == null) {
      return;
    }
    this.writeLock.lock();
    try
    {
      this.cache.saveAll();
      int i = this.dataFile.length() <= this.textFileSettings.bytesForLineEnd.length ? 1 : 0;
      this.dataFile.synch();
      this.dataFile.close();
      this.dataFile = null;
      if ((i != 0) && (!this.cacheReadonly)) {
        FileUtil.getFileUtil().delete(this.dataFileName);
      }
      this.uncommittedCache.clear();
    }
    catch (Throwable localThrowable)
    {
      throw Error.error(localThrowable, 452, 43, new Object[] { localThrowable.toString(), this.dataFileName });
    }
    finally
    {
      this.writeLock.unlock();
    }
  }
  
  void purge()
  {
    this.writeLock.lock();
    try
    {
      this.uncommittedCache.clear();
      if (this.cacheReadonly)
      {
        release();
      }
      else
      {
        if (this.dataFile != null)
        {
          this.dataFile.close();
          this.dataFile = null;
        }
        FileUtil.getFileUtil().delete(this.dataFileName);
      }
    }
    catch (Throwable localThrowable)
    {
      throw Error.error(localThrowable, 452, 44, new Object[] { localThrowable.toString(), this.dataFileName });
    }
    finally
    {
      this.writeLock.unlock();
    }
  }
  
  public void remove(CachedObject paramCachedObject)
  {
    this.writeLock.lock();
    try
    {
      long l = paramCachedObject.getPos();
      CachedObject localCachedObject = (CachedObject)this.uncommittedCache.remove(l);
      if (localCachedObject != null) {
        return;
      }
      this.cache.release(l);
    }
    finally
    {
      this.writeLock.unlock();
    }
  }
  
  public void removePersistence(CachedObject paramCachedObject)
  {
    this.writeLock.lock();
    try
    {
      clearRowImage(paramCachedObject);
    }
    finally
    {
      this.writeLock.unlock();
    }
  }
  
  private void clearRowImage(CachedObject paramCachedObject)
  {
    try
    {
      int i = paramCachedObject.getStorageSize();
      int j = i - this.textFileSettings.bytesForLineEnd.length;
      this.rowOut.reset();
      HsqlByteArrayOutputStream localHsqlByteArrayOutputStream = this.rowOut.getOutputStream();
      while (j > 0)
      {
        localHsqlByteArrayOutputStream.write(this.textFileSettings.bytesForSpace);
        j -= this.textFileSettings.bytesForSpace.length;
      }
      localHsqlByteArrayOutputStream.write(this.textFileSettings.bytesForLineEnd);
      this.dataFile.seek(paramCachedObject.getPos());
      this.dataFile.write(localHsqlByteArrayOutputStream.getBuffer(), 0, localHsqlByteArrayOutputStream.size());
    }
    catch (Throwable localThrowable)
    {
      throw Error.runtimeError(201, localThrowable.getMessage());
    }
  }
  
  public void addInit(CachedObject paramCachedObject)
  {
    this.writeLock.lock();
    try
    {
      this.cache.put(paramCachedObject);
    }
    finally
    {
      this.writeLock.unlock();
    }
  }
  
  public void add(CachedObject paramCachedObject, boolean paramBoolean)
  {
    this.writeLock.lock();
    try
    {
      this.uncommittedCache.put(paramCachedObject.getPos(), paramCachedObject);
    }
    finally
    {
      this.writeLock.unlock();
    }
  }
  
  /* Error */
  public CachedObject get(CachedObject paramCachedObject, PersistentStore paramPersistentStore, boolean paramBoolean)
  {
    // Byte code:
    //   0: aload_1
    //   1: ifnonnull +5 -> 6
    //   4: aconst_null
    //   5: areturn
    //   6: aload_0
    //   7: getfield 58	org/hsqldb/persist/TextCache:writeLock	Ljava/util/concurrent/locks/Lock;
    //   10: invokeinterface 59 1 0
    //   15: aload_0
    //   16: getfield 60	org/hsqldb/persist/TextCache:cache	Lorg/hsqldb/persist/Cache;
    //   19: aload_1
    //   20: invokeinterface 69 1 0
    //   25: invokevirtual 87	org/hsqldb/persist/Cache:get	(J)Lorg/hsqldb/persist/CachedObject;
    //   28: astore 4
    //   30: aload 4
    //   32: ifnull +18 -> 50
    //   35: aload_1
    //   36: astore 5
    //   38: aload_0
    //   39: getfield 58	org/hsqldb/persist/TextCache:writeLock	Ljava/util/concurrent/locks/Lock;
    //   42: invokeinterface 67 1 0
    //   47: aload 5
    //   49: areturn
    //   50: aload_0
    //   51: getfield 5	org/hsqldb/persist/TextCache:buffer	Lorg/hsqldb/lib/HsqlByteArrayOutputStream;
    //   54: aload_1
    //   55: invokeinterface 74 1 0
    //   60: invokevirtual 88	org/hsqldb/lib/HsqlByteArrayOutputStream:reset	(I)V
    //   63: aload_0
    //   64: getfield 46	org/hsqldb/persist/TextCache:dataFile	Lorg/hsqldb/persist/RandomAccessInterface;
    //   67: aload_1
    //   68: invokeinterface 69 1 0
    //   73: invokeinterface 79 3 0
    //   78: aload_0
    //   79: getfield 46	org/hsqldb/persist/TextCache:dataFile	Lorg/hsqldb/persist/RandomAccessInterface;
    //   82: aload_0
    //   83: getfield 5	org/hsqldb/persist/TextCache:buffer	Lorg/hsqldb/lib/HsqlByteArrayOutputStream;
    //   86: invokevirtual 80	org/hsqldb/lib/HsqlByteArrayOutputStream:getBuffer	()[B
    //   89: iconst_0
    //   90: aload_1
    //   91: invokeinterface 74 1 0
    //   96: invokeinterface 89 4 0
    //   101: aload_0
    //   102: getfield 5	org/hsqldb/persist/TextCache:buffer	Lorg/hsqldb/lib/HsqlByteArrayOutputStream;
    //   105: aload_1
    //   106: invokeinterface 74 1 0
    //   111: invokevirtual 90	org/hsqldb/lib/HsqlByteArrayOutputStream:setSize	(I)V
    //   114: aload_0
    //   115: getfield 5	org/hsqldb/persist/TextCache:buffer	Lorg/hsqldb/lib/HsqlByteArrayOutputStream;
    //   118: aload_0
    //   119: getfield 15	org/hsqldb/persist/TextCache:textFileSettings	Lorg/hsqldb/persist/TextFileSettings;
    //   122: getfield 91	org/hsqldb/persist/TextFileSettings:charEncoding	Ljava/lang/String;
    //   125: invokevirtual 92	org/hsqldb/lib/HsqlByteArrayOutputStream:toString	(Ljava/lang/String;)Ljava/lang/String;
    //   128: astore 5
    //   130: aload_0
    //   131: getfield 34	org/hsqldb/persist/TextCache:rowIn	Lorg/hsqldb/rowio/RowInputInterface;
    //   134: checkcast 38	org/hsqldb/rowio/RowInputText
    //   137: aload 5
    //   139: aload_1
    //   140: invokeinterface 69 1 0
    //   145: aload_0
    //   146: getfield 5	org/hsqldb/persist/TextCache:buffer	Lorg/hsqldb/lib/HsqlByteArrayOutputStream;
    //   149: invokevirtual 81	org/hsqldb/lib/HsqlByteArrayOutputStream:size	()I
    //   152: invokevirtual 93	org/hsqldb/rowio/RowInputText:setSource	(Ljava/lang/String;JI)V
    //   155: aload_2
    //   156: aload_1
    //   157: aload_0
    //   158: getfield 34	org/hsqldb/persist/TextCache:rowIn	Lorg/hsqldb/rowio/RowInputInterface;
    //   161: invokeinterface 94 3 0
    //   166: pop
    //   167: aload_0
    //   168: getfield 60	org/hsqldb/persist/TextCache:cache	Lorg/hsqldb/persist/Cache;
    //   171: aload_1
    //   172: invokevirtual 85	org/hsqldb/persist/Cache:put	(Lorg/hsqldb/persist/CachedObject;)V
    //   175: aload_1
    //   176: astore 6
    //   178: aload_0
    //   179: getfield 58	org/hsqldb/persist/TextCache:writeLock	Ljava/util/concurrent/locks/Lock;
    //   182: invokeinterface 67 1 0
    //   187: aload 6
    //   189: areturn
    //   190: astore 5
    //   192: aload_0
    //   193: getfield 10	org/hsqldb/persist/TextCache:database	Lorg/hsqldb/Database;
    //   196: getfield 95	org/hsqldb/Database:logger	Lorg/hsqldb/persist/Logger;
    //   199: new 96	java/lang/StringBuilder
    //   202: dup
    //   203: invokespecial 97	java/lang/StringBuilder:<init>	()V
    //   206: aload_0
    //   207: getfield 17	org/hsqldb/persist/TextCache:dataFileName	Ljava/lang/String;
    //   210: invokevirtual 98	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   213: ldc 99
    //   215: invokevirtual 98	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   218: aload_1
    //   219: invokeinterface 69 1 0
    //   224: invokevirtual 100	java/lang/StringBuilder:append	(J)Ljava/lang/StringBuilder;
    //   227: invokevirtual 101	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   230: aload 5
    //   232: invokevirtual 102	org/hsqldb/persist/Logger:logSevereEvent	(Ljava/lang/String;Ljava/lang/Throwable;)V
    //   235: aload_0
    //   236: getfield 60	org/hsqldb/persist/TextCache:cache	Lorg/hsqldb/persist/Cache;
    //   239: invokevirtual 103	org/hsqldb/persist/Cache:clearUnchanged	()V
    //   242: invokestatic 104	java/lang/System:gc	()V
    //   245: aload_1
    //   246: astore 6
    //   248: aload_0
    //   249: getfield 58	org/hsqldb/persist/TextCache:writeLock	Ljava/util/concurrent/locks/Lock;
    //   252: invokeinterface 67 1 0
    //   257: aload 6
    //   259: areturn
    //   260: astore 7
    //   262: aload_0
    //   263: getfield 58	org/hsqldb/persist/TextCache:writeLock	Ljava/util/concurrent/locks/Lock;
    //   266: invokeinterface 67 1 0
    //   271: aload 7
    //   273: athrow
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	274	0	this	TextCache
    //   0	274	1	paramCachedObject	CachedObject
    //   0	274	2	paramPersistentStore	PersistentStore
    //   0	274	3	paramBoolean	boolean
    //   28	3	4	localCachedObject1	CachedObject
    //   36	102	5	localObject1	Object
    //   190	41	5	localThrowable	Throwable
    //   176	82	6	localCachedObject2	CachedObject
    //   260	12	7	localObject2	Object
    // Exception table:
    //   from	to	target	type
    //   50	178	190	java/lang/Throwable
    //   15	38	260	finally
    //   50	178	260	finally
    //   190	248	260	finally
    //   260	262	260	finally
  }
  
  public CachedObject get(long paramLong, PersistentStore paramPersistentStore, boolean paramBoolean)
  {
    throw Error.runtimeError(201, "TextCache");
  }
  
  protected void saveRows(CachedObject[] paramArrayOfCachedObject, int paramInt1, int paramInt2) {}
  
  public void saveRow(CachedObject paramCachedObject)
  {
    this.writeLock.lock();
    try
    {
      setFileModified();
      saveRowNoLock(paramCachedObject);
      this.uncommittedCache.remove(paramCachedObject.getPos());
      this.cache.put(paramCachedObject);
    }
    catch (Throwable localThrowable)
    {
      this.database.logger.logSevereEvent("saveRow failed", localThrowable);
      throw Error.error(466, localThrowable);
    }
    finally
    {
      this.writeLock.unlock();
    }
  }
  
  public String getHeader()
  {
    return this.header;
  }
  
  public void setHeaderInitialise(String paramString)
  {
    this.header = paramString;
  }
  
  public void setHeader(String paramString)
  {
    if ((this.textFileSettings.ignoreFirst) && (this.fileFreePosition == 0L))
    {
      try
      {
        writeHeader(paramString);
        this.header = paramString;
      }
      catch (HsqlException localHsqlException)
      {
        throw new HsqlException(localHsqlException, Error.getMessage(467), 467);
      }
      return;
    }
    throw Error.error(486);
  }
  
  private void writeHeader(String paramString)
  {
    try
    {
      byte[] arrayOfByte = null;
      String str = paramString + TextFileSettings.NL;
      try
      {
        arrayOfByte = str.getBytes(this.textFileSettings.charEncoding);
      }
      catch (UnsupportedEncodingException localUnsupportedEncodingException)
      {
        arrayOfByte = str.getBytes();
      }
      this.dataFile.seek(0L);
      this.dataFile.write(arrayOfByte, 0, arrayOfByte.length);
      this.fileFreePosition = arrayOfByte.length;
    }
    catch (Throwable localThrowable)
    {
      throw Error.error(484, localThrowable);
    }
  }
  
  public long getLineNumber()
  {
    return ((RowInputText)this.rowIn).getLineNumber();
  }
  
  public TextFileSettings getTextFileSettings()
  {
    return this.textFileSettings;
  }
  
  public boolean isIgnoreFirstLine()
  {
    return this.textFileSettings.ignoreFirst;
  }
  
  protected void setFileModified()
  {
    this.fileModified = true;
  }
  
  public TextFileReader getTextFileReader()
  {
    return TextFileReader8.newTextFileReader(this.dataFile, this.textFileSettings, this.rowIn, this.cacheReadonly);
  }
}


/* Location:              E:\java\java学习\hutubill\lib\all.jar!\org\hsqldb\persist\TextCache.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */
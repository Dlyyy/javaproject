package org.hsqldb.persist;

import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.io.UnsupportedEncodingException;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.zip.DataFormatException;
import java.util.zip.Deflater;
import java.util.zip.Inflater;
import org.hsqldb.Database;
import org.hsqldb.DatabaseType;
import org.hsqldb.HsqlException;
import org.hsqldb.HsqlNameManager.HsqlName;
import org.hsqldb.SchemaManager;
import org.hsqldb.Session;
import org.hsqldb.SessionContext;
import org.hsqldb.SessionData;
import org.hsqldb.SessionManager;
import org.hsqldb.Statement;
import org.hsqldb.Table;
import org.hsqldb.error.Error;
import org.hsqldb.lib.ArrayUtil;
import org.hsqldb.lib.HashMappedList;
import org.hsqldb.lib.HsqlByteArrayInputStream;
import org.hsqldb.lib.LineGroupReader;
import org.hsqldb.map.ValuePool;
import org.hsqldb.navigator.RowSetNavigator;
import org.hsqldb.result.Result;
import org.hsqldb.result.ResultLob;
import org.hsqldb.result.ResultMetaData;
import org.hsqldb.types.BinaryData;
import org.hsqldb.types.BlobData;
import org.hsqldb.types.BlobDataID;
import org.hsqldb.types.ClobData;
import org.hsqldb.types.ClobDataID;
import org.hsqldb.types.Collation;

public class LobManager
{
  static final String resourceFileName = "/org/hsqldb/resources/lob-schema.sql";
  static final String[] starters = { "/*" };
  Database database;
  LobStore lobStore;
  Session sysLobSession;
  volatile boolean storeModified;
  byte[] byteBuffer;
  Inflater inflater;
  Deflater deflater;
  byte[] dataBuffer;
  boolean cryptLobs;
  boolean compressLobs;
  int lobBlockSize;
  int largeLobBlockSize = 524288;
  int totalBlockLimitCount = Integer.MAX_VALUE;
  Statement getLob;
  Statement getSpanningBlocks;
  Statement deleteLobCall;
  Statement deleteLobPartCall;
  Statement divideLobPartCall;
  Statement createLob;
  Statement createLobPartCall;
  Statement createSingleLobPartCall;
  Statement updateLobLength;
  Statement updateLobUsage;
  Statement getNextLobId;
  Statement deleteUnusedLobs;
  Statement mergeUnusedSpace;
  Statement getLobUseLimit;
  Statement getLobCount;
  Statement getSpanningParts;
  Statement getLastPart;
  Statement createPart;
  boolean usageChanged;
  ReadWriteLock lock = new ReentrantReadWriteLock();
  Lock writeLock = this.lock.writeLock();
  private static final String existsBlocksSQL = "SELECT * FROM SYSTEM_LOBS.BLOCKS LIMIT 1";
  private static final String initialiseBlocksSQL = "INSERT INTO SYSTEM_LOBS.BLOCKS VALUES(?,?,?)";
  private static final String getLobSQL = "SELECT * FROM SYSTEM_LOBS.LOB_IDS WHERE LOB_IDS.LOB_ID = ?";
  private static final String getLobPartSQL = "SELECT * FROM SYSTEM_LOBS.LOBS WHERE LOBS.LOB_ID = ? AND BLOCK_OFFSET + BLOCK_COUNT > ? AND BLOCK_OFFSET < ? ORDER BY BLOCK_OFFSET";
  private static final String deleteLobPartCallSQL = "CALL SYSTEM_LOBS.DELETE_BLOCKS(?,?,?,?)";
  private static final String createLobSQL = "INSERT INTO SYSTEM_LOBS.LOB_IDS VALUES(?, ?, ?, ?)";
  private static final String updateLobLengthSQL = "UPDATE SYSTEM_LOBS.LOB_IDS SET LOB_LENGTH = ? WHERE LOB_IDS.LOB_ID = ?";
  private static final String createLobPartCallSQL = "CALL SYSTEM_LOBS.ALLOC_BLOCKS(?, ?, ?)";
  private static final String createSingleLobPartCallSQL = "CALL SYSTEM_LOBS.ALLOC_SINGLE_BLOCK(?, ?, ?)";
  private static final String divideLobPartCallSQL = "CALL SYSTEM_LOBS.DIVIDE_BLOCK(?, ?)";
  private static final String updateLobUsageSQL = "UPDATE SYSTEM_LOBS.LOB_IDS SET LOB_USAGE_COUNT = LOB_USAGE_COUNT + ? WHERE LOB_IDS.LOB_ID = ?";
  private static final String getNextLobIdSQL = "VALUES NEXT VALUE FOR SYSTEM_LOBS.LOB_ID";
  private static final String deleteLobCallSQL = "CALL SYSTEM_LOBS.DELETE_LOB(?, ?)";
  private static final String deleteUnusedCallSQL = "CALL SYSTEM_LOBS.DELETE_UNUSED_LOBS(?,?)";
  private static final String mergeUnusedSpaceSQL = "CALL SYSTEM_LOBS.MERGE_EMPTY_BLOCKS()";
  private static final String getLobUseLimitSQL = "SELECT * FROM SYSTEM_LOBS.LOBS WHERE BLOCK_ADDR = (SELECT MAX(BLOCK_ADDR) FROM SYSTEM_LOBS.LOBS)";
  private static final String getLobCountSQL = "SELECT COUNT(*) FROM SYSTEM_LOBS.LOB_IDS";
  private static final String getPartsSQL = "SELECT BLOCK_COUNT, BLOCK_OFFSET, PART_OFFSET, PART_LENGTH, PART_BYTES, LOB_ID FROM SYSTEM_LOBS.PARTS WHERE LOB_ID = ?  AND PART_OFFSET + PART_LENGTH > ? AND PART_OFFSET < ? ORDER BY BLOCK_OFFSET";
  private static final String getLastPartSQL = "SELECT BLOCK_COUNT, BLOCK_OFFSET, PART_OFFSET, PART_LENGTH, PART_BYTES, LOB_ID FROM SYSTEM_LOBS.PARTS WHERE LOB_ID = ? ORDER BY LOB_ID DESC, BLOCK_OFFSET DESC LIMIT 1";
  private static final String createPartSQL = "INSERT INTO SYSTEM_LOBS.PARTS VALUES ?,?,?,?,?,?";
  
  public LobManager(Database paramDatabase)
  {
    this.database = paramDatabase;
  }
  
  public void lock()
  {
    this.writeLock.lock();
  }
  
  public void unlock()
  {
    this.writeLock.unlock();
  }
  
  public void createSchema()
  {
    this.sysLobSession = this.database.sessionManager.getSysLobSession();
    InputStream localInputStream = (InputStream)AccessController.doPrivileged(new PrivilegedAction()
    {
      public InputStream run()
      {
        return getClass().getResourceAsStream("/org/hsqldb/resources/lob-schema.sql");
      }
    });
    InputStreamReader localInputStreamReader = null;
    try
    {
      localInputStreamReader = new InputStreamReader(localInputStream, "ISO-8859-1");
    }
    catch (UnsupportedEncodingException localUnsupportedEncodingException)
    {
      localInputStreamReader = new InputStreamReader(localInputStream);
    }
    LineNumberReader localLineNumberReader = new LineNumberReader(localInputStreamReader);
    LineGroupReader localLineGroupReader = new LineGroupReader(localLineNumberReader, starters);
    HashMappedList localHashMappedList = localLineGroupReader.getAsMap();
    localLineGroupReader.close();
    String str = (String)localHashMappedList.get("/*lob_schema_definition*/");
    Statement localStatement = this.sysLobSession.compileStatement(str);
    Result localResult = localStatement.execute(this.sysLobSession);
    if (localResult.isError()) {
      throw localResult.getException();
    }
    HsqlNameManager.HsqlName localHsqlName = this.database.schemaManager.getSchemaHsqlName("SYSTEM_LOBS");
    Table localTable = this.database.schemaManager.getUserTable("BLOCKS", "SYSTEM_LOBS");
    compileStatements();
  }
  
  public void compileStatements()
  {
    this.writeLock.lock();
    try
    {
      this.getLob = this.sysLobSession.compileStatement("SELECT * FROM SYSTEM_LOBS.LOB_IDS WHERE LOB_IDS.LOB_ID = ?");
      this.getSpanningBlocks = this.sysLobSession.compileStatement("SELECT * FROM SYSTEM_LOBS.LOBS WHERE LOBS.LOB_ID = ? AND BLOCK_OFFSET + BLOCK_COUNT > ? AND BLOCK_OFFSET < ? ORDER BY BLOCK_OFFSET");
      this.createLob = this.sysLobSession.compileStatement("INSERT INTO SYSTEM_LOBS.LOB_IDS VALUES(?, ?, ?, ?)");
      this.createLobPartCall = this.sysLobSession.compileStatement("CALL SYSTEM_LOBS.ALLOC_BLOCKS(?, ?, ?)");
      this.createSingleLobPartCall = this.sysLobSession.compileStatement("CALL SYSTEM_LOBS.ALLOC_SINGLE_BLOCK(?, ?, ?)");
      this.divideLobPartCall = this.sysLobSession.compileStatement("CALL SYSTEM_LOBS.DIVIDE_BLOCK(?, ?)");
      this.deleteLobCall = this.sysLobSession.compileStatement("CALL SYSTEM_LOBS.DELETE_LOB(?, ?)");
      this.deleteLobPartCall = this.sysLobSession.compileStatement("CALL SYSTEM_LOBS.DELETE_BLOCKS(?,?,?,?)");
      this.updateLobLength = this.sysLobSession.compileStatement("UPDATE SYSTEM_LOBS.LOB_IDS SET LOB_LENGTH = ? WHERE LOB_IDS.LOB_ID = ?");
      this.updateLobUsage = this.sysLobSession.compileStatement("UPDATE SYSTEM_LOBS.LOB_IDS SET LOB_USAGE_COUNT = LOB_USAGE_COUNT + ? WHERE LOB_IDS.LOB_ID = ?");
      this.getNextLobId = this.sysLobSession.compileStatement("VALUES NEXT VALUE FOR SYSTEM_LOBS.LOB_ID");
      this.deleteUnusedLobs = this.sysLobSession.compileStatement("CALL SYSTEM_LOBS.DELETE_UNUSED_LOBS(?,?)");
      this.mergeUnusedSpace = this.sysLobSession.compileStatement("CALL SYSTEM_LOBS.MERGE_EMPTY_BLOCKS()");
      this.getLobUseLimit = this.sysLobSession.compileStatement("SELECT * FROM SYSTEM_LOBS.LOBS WHERE BLOCK_ADDR = (SELECT MAX(BLOCK_ADDR) FROM SYSTEM_LOBS.LOBS)");
      this.getLobCount = this.sysLobSession.compileStatement("SELECT COUNT(*) FROM SYSTEM_LOBS.LOB_IDS");
      this.getSpanningParts = this.sysLobSession.compileStatement("SELECT BLOCK_COUNT, BLOCK_OFFSET, PART_OFFSET, PART_LENGTH, PART_BYTES, LOB_ID FROM SYSTEM_LOBS.PARTS WHERE LOB_ID = ?  AND PART_OFFSET + PART_LENGTH > ? AND PART_OFFSET < ? ORDER BY BLOCK_OFFSET");
      this.getLastPart = this.sysLobSession.compileStatement("SELECT BLOCK_COUNT, BLOCK_OFFSET, PART_OFFSET, PART_LENGTH, PART_BYTES, LOB_ID FROM SYSTEM_LOBS.PARTS WHERE LOB_ID = ? ORDER BY LOB_ID DESC, BLOCK_OFFSET DESC LIMIT 1");
      this.createPart = this.sysLobSession.compileStatement("INSERT INTO SYSTEM_LOBS.PARTS VALUES ?,?,?,?,?,?");
    }
    finally
    {
      this.writeLock.unlock();
    }
  }
  
  private void initialiseLobSpace()
  {
    Statement localStatement = this.sysLobSession.compileStatement("SELECT * FROM SYSTEM_LOBS.BLOCKS LIMIT 1");
    Result localResult = localStatement.execute(this.sysLobSession);
    if (localResult.isError()) {
      throw localResult.getException();
    }
    RowSetNavigator localRowSetNavigator = localResult.getNavigator();
    int i = localRowSetNavigator.getSize();
    if (i > 0) {
      return;
    }
    localStatement = this.sysLobSession.compileStatement("INSERT INTO SYSTEM_LOBS.BLOCKS VALUES(?,?,?)");
    Object[] arrayOfObject = new Object[3];
    arrayOfObject[0] = ValuePool.INTEGER_0;
    arrayOfObject[1] = ValuePool.getInt(this.totalBlockLimitCount);
    arrayOfObject[2] = ValuePool.getLong(0L);
    this.sysLobSession.executeCompiledStatement(localStatement, arrayOfObject, 0);
  }
  
  public void open()
  {
    this.lobBlockSize = this.database.logger.getLobBlockSize();
    this.cryptLobs = this.database.logger.cryptLobs;
    this.compressLobs = this.database.logger.propCompressLobs;
    if ((this.compressLobs) || (this.cryptLobs))
    {
      int i = this.largeLobBlockSize + 4096;
      this.inflater = new Inflater();
      this.deflater = new Deflater(1);
      this.dataBuffer = new byte[i];
    }
    if (this.database.getType() == DatabaseType.DB_RES)
    {
      this.lobStore = new LobStoreInJar(this.database, this.lobBlockSize);
    }
    else if (this.database.getType() == DatabaseType.DB_FILE)
    {
      this.lobStore = new LobStoreRAFile(this.database, this.lobBlockSize);
      if (!this.database.isFilesReadOnly())
      {
        this.byteBuffer = new byte[this.lobBlockSize];
        initialiseLobSpace();
      }
    }
    else
    {
      this.lobStore = new LobStoreMem(this.lobBlockSize);
      this.byteBuffer = new byte[this.lobBlockSize];
      initialiseLobSpace();
    }
  }
  
  public void close()
  {
    if (this.lobStore != null) {
      this.lobStore.close();
    }
    this.lobStore = null;
  }
  
  public LobStore getLobStore()
  {
    if (this.lobStore == null) {
      open();
    }
    return this.lobStore;
  }
  
  private Long getNewLobID()
  {
    Result localResult = this.getNextLobId.execute(this.sysLobSession);
    if (localResult.isError()) {
      return Long.valueOf(0L);
    }
    RowSetNavigator localRowSetNavigator = localResult.getNavigator();
    boolean bool = localRowSetNavigator.next();
    if (!bool)
    {
      localRowSetNavigator.release();
      return Long.valueOf(0L);
    }
    Object[] arrayOfObject = localRowSetNavigator.getCurrent();
    localRowSetNavigator.release();
    return (Long)arrayOfObject[0];
  }
  
  private Object[] getLobHeader(long paramLong)
  {
    ResultMetaData localResultMetaData = this.getLob.getParametersMetaData();
    Object[] arrayOfObject1 = new Object[localResultMetaData.getColumnCount()];
    arrayOfObject1[0] = ValuePool.getLong(paramLong);
    this.sysLobSession.sessionContext.pushDynamicArguments(arrayOfObject1);
    Result localResult = this.getLob.execute(this.sysLobSession);
    this.sysLobSession.sessionContext.pop();
    if (localResult.isError()) {
      throw localResult.getException();
    }
    RowSetNavigator localRowSetNavigator = localResult.getNavigator();
    boolean bool = localRowSetNavigator.next();
    Object[] arrayOfObject2 = null;
    if (bool) {
      arrayOfObject2 = localRowSetNavigator.getCurrent();
    }
    localRowSetNavigator.release();
    return arrayOfObject2;
  }
  
  public BlobData getBlob(long paramLong)
  {
    this.writeLock.lock();
    try
    {
      Object[] arrayOfObject = getLobHeader(paramLong);
      if (arrayOfObject == null)
      {
        localObject1 = null;
        return (BlobData)localObject1;
      }
      Object localObject1 = new BlobDataID(paramLong);
      Object localObject2 = localObject1;
      return (BlobData)localObject2;
    }
    finally
    {
      this.writeLock.unlock();
    }
  }
  
  public ClobData getClob(long paramLong)
  {
    this.writeLock.lock();
    try
    {
      Object[] arrayOfObject = getLobHeader(paramLong);
      if (arrayOfObject == null)
      {
        localObject1 = null;
        return (ClobData)localObject1;
      }
      Object localObject1 = new ClobDataID(paramLong);
      Object localObject2 = localObject1;
      return (ClobData)localObject2;
    }
    finally
    {
      this.writeLock.unlock();
    }
  }
  
  public long createBlob(Session paramSession, long paramLong)
  {
    this.writeLock.lock();
    try
    {
      Long localLong = getNewLobID();
      ResultMetaData localResultMetaData = this.createLob.getParametersMetaData();
      Object[] arrayOfObject = new Object[localResultMetaData.getColumnCount()];
      arrayOfObject[0] = localLong;
      arrayOfObject[1] = ValuePool.getLong(paramLong);
      arrayOfObject[2] = ValuePool.INTEGER_0;
      arrayOfObject[3] = ValuePool.getInt(30);
      Result localResult = this.sysLobSession.executeCompiledStatement(this.createLob, arrayOfObject, 0);
      this.usageChanged = true;
      long l = localLong.longValue();
      return l;
    }
    finally
    {
      this.writeLock.unlock();
    }
  }
  
  public long createClob(Session paramSession, long paramLong)
  {
    this.writeLock.lock();
    try
    {
      Long localLong = getNewLobID();
      ResultMetaData localResultMetaData = this.createLob.getParametersMetaData();
      Object[] arrayOfObject = new Object[localResultMetaData.getColumnCount()];
      arrayOfObject[0] = localLong;
      arrayOfObject[1] = ValuePool.getLong(paramLong);
      arrayOfObject[2] = ValuePool.INTEGER_0;
      arrayOfObject[3] = ValuePool.getInt(40);
      Result localResult = this.sysLobSession.executeCompiledStatement(this.createLob, arrayOfObject, 0);
      this.usageChanged = true;
      long l = localLong.longValue();
      return l;
    }
    finally
    {
      this.writeLock.unlock();
    }
  }
  
  public Result deleteLob(long paramLong)
  {
    this.writeLock.lock();
    try
    {
      ResultMetaData localResultMetaData = this.deleteLobCall.getParametersMetaData();
      Object[] arrayOfObject = new Object[localResultMetaData.getColumnCount()];
      arrayOfObject[0] = ValuePool.getLong(paramLong);
      arrayOfObject[1] = ValuePool.getLong(0L);
      Result localResult1 = this.sysLobSession.executeCompiledStatement(this.deleteLobCall, arrayOfObject, 0);
      this.usageChanged = true;
      Result localResult2 = localResult1;
      return localResult2;
    }
    finally
    {
      this.writeLock.unlock();
    }
  }
  
  public void setUsageChanged()
  {
    this.usageChanged = true;
  }
  
  public Result deleteUnusedLobs()
  {
    this.writeLock.lock();
    try
    {
      if ((this.lobStore == null) || (this.byteBuffer == null) || (!this.usageChanged))
      {
        localObject1 = Result.updateZeroResult;
        return (Result)localObject1;
      }
      Object localObject1 = this.database.sessionManager.getAllSessions();
      long l1 = Long.MAX_VALUE;
      for (int i = 0; i < localObject1.length; i++) {
        if (!localObject1[i].isClosed())
        {
          long l2 = localObject1[i].sessionData.getFirstLobID();
          if ((l2 != 0L) && (l2 < l1)) {
            l1 = l2;
          }
        }
      }
      ResultMetaData localResultMetaData = this.deleteUnusedLobs.getParametersMetaData();
      Object[] arrayOfObject1 = new Object[localResultMetaData.getColumnCount()];
      arrayOfObject1[0] = new Long(l1);
      Result localResult1 = this.sysLobSession.executeCompiledStatement(this.deleteUnusedLobs, arrayOfObject1, 0);
      if (localResult1.isError())
      {
        Result localResult2 = localResult1;
        return localResult2;
      }
      if (arrayOfObject1[1] != null)
      {
        int j = ((Number)arrayOfObject1[1]).intValue();
        if (j < 1)
        {
          Result localResult4 = Result.updateZeroResult;
          return localResult4;
        }
      }
      localResult1 = this.sysLobSession.executeCompiledStatement(this.mergeUnusedSpace, ValuePool.emptyObjectArray, 0);
      Result localResult3;
      if (localResult1.isError())
      {
        localResult3 = localResult1;
        return localResult3;
      }
      localResult1 = this.sysLobSession.executeCompiledStatement(this.getLobUseLimit, ValuePool.emptyObjectArray, 0);
      if (localResult1.isError())
      {
        localResult3 = localResult1;
        return localResult3;
      }
      this.usageChanged = false;
      long l3 = 0L;
      RowSetNavigator localRowSetNavigator = localResult1.getNavigator();
      boolean bool = localRowSetNavigator.next();
      if (bool)
      {
        Object[] arrayOfObject2 = localRowSetNavigator.getCurrent();
        if ((arrayOfObject2[0] == null) || (arrayOfObject2[1] == null))
        {
          Result localResult5 = Result.updateOneResult;
          return localResult5;
        }
        l3 = ((Integer)arrayOfObject2[0]).intValue() + ((Integer)arrayOfObject2[1]).intValue();
        l3 *= this.lobBlockSize;
      }
      long l4 = this.lobStore.getLength();
      if (l4 > l3)
      {
        this.database.logger.logInfoEvent("lob file truncated to usage");
        this.lobStore.setLength(l3);
        try
        {
          this.lobStore.synch();
        }
        catch (Throwable localThrowable) {}
      }
      else if (l4 < l3)
      {
        this.database.logger.logInfoEvent("lob file reported smaller than usage");
      }
      Result localResult6 = Result.updateOneResult;
      return localResult6;
    }
    finally
    {
      this.writeLock.unlock();
    }
  }
  
  public Result getLength(long paramLong)
  {
    this.writeLock.lock();
    try
    {
      Object[] arrayOfObject = getLobHeader(paramLong);
      if (arrayOfObject == null) {
        throw Error.error(1852);
      }
      long l = ((Long)arrayOfObject[1]).longValue();
      int i = ((Integer)arrayOfObject[3]).intValue();
      ResultLob localResultLob = ResultLob.newLobSetResponse(paramLong, l);
      return localResultLob;
    }
    catch (HsqlException localHsqlException)
    {
      Result localResult = Result.newErrorResult(localHsqlException);
      return localResult;
    }
    finally
    {
      this.writeLock.unlock();
    }
  }
  
  public int compare(BlobData paramBlobData, byte[] paramArrayOfByte)
  {
    this.writeLock.lock();
    try
    {
      Object[] arrayOfObject = getLobHeader(paramBlobData.getId());
      long l = ((Long)arrayOfObject[1]).longValue();
      int[][] arrayOfInt = getBlockAddresses(paramBlobData.getId(), 0, Integer.MAX_VALUE);
      int i = 0;
      int j = 0;
      int k = 0;
      if (l == 0L)
      {
        m = paramArrayOfByte.length == 0 ? 0 : -1;
        return m;
      }
      if (paramArrayOfByte.length == 0)
      {
        m = 1;
        return m;
      }
      for (;;)
      {
        m = arrayOfInt[i][0] + k;
        byte[] arrayOfByte = getLobStore().getBlockBytes(m, 1);
        for (int n = 0; n < arrayOfByte.length; n++)
        {
          int i1;
          if (j + n >= paramArrayOfByte.length)
          {
            if (l == paramArrayOfByte.length)
            {
              i1 = 0;
              return i1;
            }
            i1 = 1;
            return i1;
          }
          if (arrayOfByte[n] != paramArrayOfByte[(j + n)])
          {
            i1 = (arrayOfByte[n] & 0xFF) > (paramArrayOfByte[(j + n)] & 0xFF) ? 1 : -1;
            return i1;
          }
        }
        k++;
        j += this.lobBlockSize;
        if (k == arrayOfInt[i][1])
        {
          k = 0;
          i++;
        }
        if ((i == arrayOfInt.length) || (j >= paramArrayOfByte.length)) {
          break;
        }
      }
      if (l == paramArrayOfByte.length)
      {
        m = 0;
        return m;
      }
      int m = l > paramArrayOfByte.length ? 1 : -1;
      return m;
    }
    finally
    {
      this.writeLock.unlock();
    }
  }
  
  public int compare(BlobData paramBlobData1, BlobData paramBlobData2)
  {
    if (paramBlobData1.getId() == paramBlobData2.getId()) {
      return 0;
    }
    this.writeLock.lock();
    try
    {
      if ((this.compressLobs) || (this.cryptLobs))
      {
        i = compareBytesCompressed(paramBlobData1.getId(), paramBlobData2.getId());
        return i;
      }
      int i = compareBytesNormal(paramBlobData1.getId(), paramBlobData2.getId());
      return i;
    }
    finally
    {
      this.writeLock.unlock();
    }
  }
  
  public int compare(Collation paramCollation, ClobData paramClobData, String paramString)
  {
    this.writeLock.lock();
    try
    {
      Object[] arrayOfObject = getLobHeader(paramClobData.getId());
      long l1 = ((Long)arrayOfObject[1]).longValue();
      int[][] arrayOfInt = getBlockAddresses(paramClobData.getId(), 0, Integer.MAX_VALUE);
      int i = 0;
      int j = 0;
      int k = 0;
      if (l1 == 0L)
      {
        m = paramString.length() == 0 ? 0 : -1;
        return m;
      }
      if (paramString.length() == 0)
      {
        m = 1;
        return m;
      }
      for (;;)
      {
        m = arrayOfInt[i][0] + k;
        byte[] arrayOfByte = getLobStore().getBlockBytes(m, 1);
        long l2 = l1 - (arrayOfInt[i][2] + k) * this.lobBlockSize / 2L;
        if (l2 > this.lobBlockSize / 2) {
          l2 = this.lobBlockSize / 2;
        }
        String str1 = new String(ArrayUtil.byteArrayToChars(arrayOfByte), 0, (int)l2);
        int n = paramString.length() - j;
        if (n > this.lobBlockSize / 2) {
          n = this.lobBlockSize / 2;
        }
        String str2 = paramString.substring(j, j + n);
        int i1 = paramCollation.compare(str1, str2);
        if (i1 != 0)
        {
          int i2 = i1;
          return i2;
        }
        k++;
        j += this.lobBlockSize / 2;
        if (k == arrayOfInt[i][1])
        {
          k = 0;
          i++;
        }
        if ((i == arrayOfInt.length) || (j >= paramString.length())) {
          break;
        }
      }
      if (l1 == paramString.length())
      {
        m = 0;
        return m;
      }
      int m = l1 > paramString.length() ? 1 : -1;
      return m;
    }
    finally
    {
      this.writeLock.unlock();
    }
  }
  
  public int compare(Collation paramCollation, ClobData paramClobData1, ClobData paramClobData2)
  {
    if (paramClobData1.getId() == paramClobData2.getId()) {
      return 0;
    }
    this.writeLock.lock();
    try
    {
      if ((this.compressLobs) || (this.cryptLobs))
      {
        i = compareTextCompressed(paramCollation, paramClobData1.getId(), paramClobData2.getId());
        return i;
      }
      int i = compareTextNormal(paramCollation, paramClobData1.getId(), paramClobData2.getId());
      return i;
    }
    finally
    {
      this.writeLock.unlock();
    }
  }
  
  private int compareBytesNormal(long paramLong1, long paramLong2)
  {
    Object[] arrayOfObject = getLobHeader(paramLong1);
    long l1 = ((Long)arrayOfObject[1]).longValue();
    arrayOfObject = getLobHeader(paramLong2);
    long l2 = ((Long)arrayOfObject[1]).longValue();
    int[][] arrayOfInt1 = getBlockAddresses(paramLong1, 0, Integer.MAX_VALUE);
    int[][] arrayOfInt2 = getBlockAddresses(paramLong2, 0, Integer.MAX_VALUE);
    int i = 0;
    int j = 0;
    int k = 0;
    int m = 0;
    if (l1 == 0L) {
      return l2 == 0L ? 0 : -1;
    }
    if (l2 == 0L) {
      return 1;
    }
    for (;;)
    {
      int n = arrayOfInt1[i][0] + k;
      int i1 = arrayOfInt2[j][0] + m;
      byte[] arrayOfByte1 = getLobStore().getBlockBytes(n, 1);
      byte[] arrayOfByte2 = getLobStore().getBlockBytes(i1, 1);
      int i2 = ArrayUtil.compare(arrayOfByte1, arrayOfByte2);
      if (i2 != 0) {
        return i2;
      }
      k++;
      m++;
      if (k == arrayOfInt1[i][1])
      {
        k = 0;
        i++;
      }
      if (m == arrayOfInt2[j][1])
      {
        m = 0;
        j++;
      }
      if ((i == arrayOfInt1.length) || (j == arrayOfInt2.length)) {
        break;
      }
    }
    if (l1 == l2) {
      return 0;
    }
    return l1 > l2 ? 1 : -1;
  }
  
  private int compareTextNormal(Collation paramCollation, long paramLong1, long paramLong2)
  {
    Object[] arrayOfObject = getLobHeader(paramLong1);
    long l1 = ((Long)arrayOfObject[1]).longValue();
    arrayOfObject = getLobHeader(paramLong2);
    long l2 = ((Long)arrayOfObject[1]).longValue();
    int[][] arrayOfInt1 = getBlockAddresses(paramLong1, 0, Integer.MAX_VALUE);
    int[][] arrayOfInt2 = getBlockAddresses(paramLong2, 0, Integer.MAX_VALUE);
    int i = 0;
    int j = 0;
    int k = 0;
    int m = 0;
    if (l1 == 0L) {
      return l2 == 0L ? 0 : -1;
    }
    if (l2 == 0L) {
      return 1;
    }
    for (;;)
    {
      int n = arrayOfInt1[i][0] + k;
      int i1 = arrayOfInt2[j][0] + m;
      byte[] arrayOfByte1 = getLobStore().getBlockBytes(n, 1);
      byte[] arrayOfByte2 = getLobStore().getBlockBytes(i1, 1);
      long l3 = l1 - (arrayOfInt1[i][2] + k) * this.lobBlockSize / 2L;
      if (l3 > this.lobBlockSize / 2) {
        l3 = this.lobBlockSize / 2;
      }
      long l4 = l2 - (arrayOfInt2[j][2] + m) * this.lobBlockSize / 2L;
      if (l4 > this.lobBlockSize / 2) {
        l4 = this.lobBlockSize / 2;
      }
      String str1 = new String(ArrayUtil.byteArrayToChars(arrayOfByte1), 0, (int)l3);
      String str2 = new String(ArrayUtil.byteArrayToChars(arrayOfByte2), 0, (int)l4);
      int i2 = paramCollation.compare(str1, str2);
      if (i2 != 0) {
        return i2;
      }
      k++;
      m++;
      if (k == arrayOfInt1[i][1])
      {
        k = 0;
        i++;
      }
      if (m == arrayOfInt2[j][1])
      {
        m = 0;
        j++;
      }
      if ((i == arrayOfInt1.length) || (j == arrayOfInt2.length)) {
        break;
      }
    }
    if (l1 == l2) {
      return 0;
    }
    return l1 > l2 ? 1 : -1;
  }
  
  public Result getLob(long paramLong1, long paramLong2, long paramLong3)
  {
    if (paramLong2 == 0L) {
      return createDuplicateLob(paramLong1, paramLong3);
    }
    throw Error.runtimeError(201, "LobManager");
  }
  
  public Result createDuplicateLob(long paramLong)
  {
    Result localResult = getLength(paramLong);
    if (localResult.isError()) {
      return localResult;
    }
    return createDuplicateLob(paramLong, ((ResultLob)localResult).getBlockLength());
  }
  
  public Result createDuplicateLob(long paramLong1, long paramLong2)
  {
    if (this.byteBuffer == null) {
      throw Error.error(456);
    }
    this.writeLock.lock();
    try
    {
      Object[] arrayOfObject1 = getLobHeader(paramLong1);
      if (arrayOfObject1 == null)
      {
        localObject1 = Result.newErrorResult(Error.error(1852));
        return (Result)localObject1;
      }
      Object localObject1 = getNewLobID();
      Object[] arrayOfObject2 = new Object[arrayOfObject1.length];
      arrayOfObject2[0] = localObject1;
      arrayOfObject2[1] = Long.valueOf(paramLong2);
      arrayOfObject2[2] = ValuePool.INTEGER_0;
      arrayOfObject2[3] = arrayOfObject1[3];
      Result localResult = this.sysLobSession.executeCompiledStatement(this.createLob, arrayOfObject2, 0);
      Object localObject2;
      if (localResult.isError())
      {
        localObject2 = localResult;
        return (Result)localObject2;
      }
      this.usageChanged = true;
      if (paramLong2 == 0L)
      {
        localObject2 = ResultLob.newLobSetResponse(((Long)localObject1).longValue(), paramLong2);
        return (Result)localObject2;
      }
      long l = paramLong2;
      int i = ((Integer)arrayOfObject1[3]).intValue();
      if (i == 40) {
        l *= 2L;
      }
      int j = (int)(l / this.lobBlockSize);
      if (l % this.lobBlockSize != 0L) {
        j++;
      }
      createBlockAddresses(((Long)localObject1).longValue(), 0, j);
      int[][] arrayOfInt1 = getBlockAddresses(paramLong1, 0, Integer.MAX_VALUE);
      int[][] arrayOfInt2 = getBlockAddresses(((Long)localObject1).longValue(), 0, Integer.MAX_VALUE);
      try
      {
        copyBlockSet(arrayOfInt1, arrayOfInt2);
      }
      catch (HsqlException localHsqlException)
      {
        localObject3 = Result.newErrorResult(localHsqlException);
        return (Result)localObject3;
      }
      int k = (int)(l % this.lobBlockSize);
      if (k != 0)
      {
        localObject3 = arrayOfInt2[(arrayOfInt2.length - 1)];
        int m = localObject3[0] + localObject3[1] - 1;
        byte[] arrayOfByte = getLobStore().getBlockBytes(m, 1);
        ArrayUtil.fillArray(arrayOfByte, k, (byte)0);
        getLobStore().setBlockBytes(arrayOfByte, m, 1);
      }
      this.lobStore.synch();
      Object localObject3 = ResultLob.newLobSetResponse(((Long)localObject1).longValue(), paramLong2);
      return (Result)localObject3;
    }
    finally
    {
      this.writeLock.unlock();
    }
  }
  
  public Result getTruncateLength(long paramLong)
  {
    this.writeLock.lock();
    try
    {
      Object[] arrayOfObject = getLobHeader(paramLong);
      if (arrayOfObject == null) {
        throw Error.error(1852);
      }
      long l = ((Long)arrayOfObject[1]).longValue();
      int i = ((Integer)arrayOfObject[3]).intValue();
      ResultLob localResultLob = ResultLob.newLobSetResponse(paramLong, l);
      return localResultLob;
    }
    finally
    {
      this.writeLock.unlock();
    }
  }
  
  private void copyBlockSet(int[][] paramArrayOfInt1, int[][] paramArrayOfInt2)
  {
    int i = 0;
    int j = 0;
    int k = 0;
    int m = 0;
    for (;;)
    {
      byte[] arrayOfByte = getLobStore().getBlockBytes(paramArrayOfInt1[i][0] + k, 1);
      getLobStore().setBlockBytes(arrayOfByte, paramArrayOfInt2[j][0] + m, 1);
      k++;
      m++;
      if (k == paramArrayOfInt1[i][1])
      {
        k = 0;
        i++;
      }
      if (m == paramArrayOfInt2[j][1])
      {
        m = 0;
        j++;
      }
      if ((i == paramArrayOfInt1.length) || (j == paramArrayOfInt2.length)) {
        break;
      }
    }
    this.storeModified = true;
  }
  
  public Result getChars(long paramLong1, long paramLong2, int paramInt)
  {
    this.writeLock.lock();
    Result localResult;
    try
    {
      if ((this.compressLobs) || (this.cryptLobs)) {
        localResult = getBytesCompressed(paramLong1, paramLong2 * 2L, paramInt * 2, true);
      } else {
        localResult = getBytesNormal(paramLong1, paramLong2 * 2L, paramInt * 2);
      }
    }
    finally
    {
      this.writeLock.unlock();
    }
    if (localResult.isError()) {
      return localResult;
    }
    byte[] arrayOfByte = ((ResultLob)localResult).getByteArray();
    char[] arrayOfChar = ArrayUtil.byteArrayToChars(arrayOfByte);
    return ResultLob.newLobGetCharsResponse(paramLong1, paramLong2, arrayOfChar);
  }
  
  public Result getBytes(long paramLong1, long paramLong2, int paramInt)
  {
    this.writeLock.lock();
    try
    {
      if ((this.compressLobs) || (this.cryptLobs))
      {
        localResult = getBytesCompressed(paramLong1, paramLong2, paramInt, false);
        return localResult;
      }
      Result localResult = getBytesNormal(paramLong1, paramLong2, paramInt);
      return localResult;
    }
    finally
    {
      this.writeLock.unlock();
    }
  }
  
  private Result getBytesNormal(long paramLong1, long paramLong2, int paramInt)
  {
    int i = (int)(paramLong2 / this.lobBlockSize);
    int j = (int)(paramLong2 % this.lobBlockSize);
    int k = (int)((paramLong2 + paramInt) / this.lobBlockSize);
    int m = (int)((paramLong2 + paramInt) % this.lobBlockSize);
    if (m == 0) {
      m = this.lobBlockSize;
    } else {
      k++;
    }
    if (paramInt == 0) {
      return ResultLob.newLobGetBytesResponse(paramLong1, paramLong2, BinaryData.zeroLengthBytes);
    }
    int n = 0;
    byte[] arrayOfByte1 = new byte[paramInt];
    int[][] arrayOfInt = getBlockAddresses(paramLong1, i, k);
    if (arrayOfInt.length == 0) {
      return Result.newErrorResult(Error.error(1852));
    }
    int i1 = 0;
    int i2 = arrayOfInt[i1][1] + arrayOfInt[i1][2] - i;
    if (arrayOfInt[i1][1] + arrayOfInt[i1][2] > k) {
      i2 -= arrayOfInt[i1][1] + arrayOfInt[i1][2] - k;
    }
    byte[] arrayOfByte2;
    try
    {
      arrayOfByte2 = getLobStore().getBlockBytes(arrayOfInt[i1][0] - arrayOfInt[i1][2] + i, i2);
    }
    catch (HsqlException localHsqlException1)
    {
      return Result.newErrorResult(localHsqlException1);
    }
    int i3 = this.lobBlockSize * i2 - j;
    if (i3 > paramInt) {
      i3 = paramInt;
    }
    System.arraycopy(arrayOfByte2, j, arrayOfByte1, n, i3);
    n += i3;
    i1++;
    while ((i1 < arrayOfInt.length) && (n < paramInt))
    {
      i2 = arrayOfInt[i1][1];
      if (arrayOfInt[i1][1] + arrayOfInt[i1][2] > k) {
        i2 -= arrayOfInt[i1][1] + arrayOfInt[i1][2] - k;
      }
      try
      {
        arrayOfByte2 = getLobStore().getBlockBytes(arrayOfInt[i1][0], i2);
      }
      catch (HsqlException localHsqlException2)
      {
        return Result.newErrorResult(localHsqlException2);
      }
      i3 = this.lobBlockSize * i2;
      if (i3 > paramInt - n) {
        i3 = paramInt - n;
      }
      System.arraycopy(arrayOfByte2, 0, arrayOfByte1, n, i3);
      n += i3;
      i1++;
    }
    return ResultLob.newLobGetBytesResponse(paramLong1, paramLong2, arrayOfByte1);
  }
  
  private Result setBytesBA(long paramLong1, long paramLong2, byte[] paramArrayOfByte, int paramInt, boolean paramBoolean)
  {
    if (paramInt == 0) {
      return ResultLob.newLobSetResponse(paramLong1, 0L);
    }
    this.writeLock.lock();
    try
    {
      if ((this.compressLobs) || (this.cryptLobs))
      {
        localResult = setBytesBACompressed(paramLong1, paramLong2, paramArrayOfByte, paramInt, paramBoolean);
        return localResult;
      }
      Result localResult = setBytesBANormal(paramLong1, paramLong2, paramArrayOfByte, paramInt);
      return localResult;
    }
    finally
    {
      this.writeLock.unlock();
    }
  }
  
  private Result setBytesBANormal(long paramLong1, long paramLong2, byte[] paramArrayOfByte, int paramInt)
  {
    int i = 0;
    int j = (int)(paramLong2 / this.lobBlockSize);
    int k = (int)(paramLong2 % this.lobBlockSize);
    int m = (int)((paramLong2 + paramInt) / this.lobBlockSize);
    int n = (int)((paramLong2 + paramInt) % this.lobBlockSize);
    if (n == 0) {
      n = this.lobBlockSize;
    } else {
      m++;
    }
    int[][] arrayOfInt = getBlockAddresses(paramLong1, j, m);
    int i1 = j;
    if (arrayOfInt.length > 0) {
      i1 = arrayOfInt[(arrayOfInt.length - 1)][2] + arrayOfInt[(arrayOfInt.length - 1)][1];
    }
    if (i1 < m)
    {
      createBlockAddresses(paramLong1, i1, m - i1);
      arrayOfInt = getBlockAddresses(paramLong1, j, m);
      i = 1;
    }
    int i2 = 0;
    int i3 = paramInt;
    try
    {
      for (int i4 = 0; i4 < arrayOfInt.length; i4++)
      {
        long l1 = arrayOfInt[i4][2] * this.lobBlockSize;
        long l2 = arrayOfInt[i4][1] * this.lobBlockSize;
        long l3 = arrayOfInt[i4][0] * this.lobBlockSize;
        int i5 = 0;
        if (paramLong2 > l1)
        {
          l2 -= paramLong2 - l1;
          l3 += paramLong2 - l1;
        }
        if (i3 < l2)
        {
          if (i != 0) {
            i5 = (int)((l2 - i3) % this.lobBlockSize);
          }
          l2 = i3;
        }
        getLobStore().setBlockBytes(paramArrayOfByte, l3, i2, (int)l2);
        if (i5 != 0)
        {
          ArrayUtil.fillArray(this.byteBuffer, 0, (byte)0);
          getLobStore().setBlockBytes(this.byteBuffer, l3 + l2, 0, i5);
        }
        i2 = (int)(i2 + l2);
        i3 = (int)(i3 - l2);
      }
    }
    catch (HsqlException localHsqlException)
    {
      return Result.newErrorResult(localHsqlException);
    }
    this.lobStore.synch();
    this.storeModified = true;
    return ResultLob.newLobSetResponse(paramLong1, paramInt);
  }
  
  private Result setBytesIS(long paramLong1, InputStream paramInputStream, long paramLong2, boolean paramBoolean)
  {
    if (paramLong2 == 0L) {
      return ResultLob.newLobSetResponse(paramLong1, 0L);
    }
    if ((this.compressLobs) || (this.cryptLobs)) {
      return setBytesISCompressed(paramLong1, paramInputStream, paramLong2, paramBoolean);
    }
    return setBytesISNormal(paramLong1, paramInputStream, paramLong2);
  }
  
  private Result setBytesISNormal(long paramLong1, InputStream paramInputStream, long paramLong2)
  {
    long l = 0L;
    int i = (int)(paramLong2 / this.lobBlockSize);
    int j = (int)(paramLong2 % this.lobBlockSize);
    if (j == 0) {
      j = this.lobBlockSize;
    } else {
      i++;
    }
    createBlockAddresses(paramLong1, 0, i);
    int[][] arrayOfInt = getBlockAddresses(paramLong1, 0, i);
    for (int k = 0; k < arrayOfInt.length; k++) {
      for (int m = 0; m < arrayOfInt[k][1]; m++)
      {
        int n = this.lobBlockSize;
        ArrayUtil.fillArray(this.byteBuffer, 0, (byte)0);
        if ((k == arrayOfInt.length - 1) && (m == arrayOfInt[k][1] - 1)) {
          n = j;
        }
        try
        {
          int i1 = 0;
          while (n > 0)
          {
            int i2 = paramInputStream.read(this.byteBuffer, i1, n);
            if (i2 == -1) {
              return Result.newErrorResult(new EOFException());
            }
            n -= i2;
            i1 += i2;
          }
          l += i1;
        }
        catch (IOException localIOException)
        {
          return Result.newErrorResult(localIOException);
        }
        try
        {
          getLobStore().setBlockBytes(this.byteBuffer, arrayOfInt[k][0] + m, 1);
        }
        catch (HsqlException localHsqlException)
        {
          return Result.newErrorResult(localHsqlException);
        }
      }
    }
    this.storeModified = true;
    this.lobStore.synch();
    return ResultLob.newLobSetResponse(paramLong1, l);
  }
  
  public Result setBytes(long paramLong1, long paramLong2, byte[] paramArrayOfByte, int paramInt)
  {
    if (this.byteBuffer == null) {
      throw Error.error(456);
    }
    this.writeLock.lock();
    try
    {
      Object[] arrayOfObject = getLobHeader(paramLong1);
      if (arrayOfObject == null)
      {
        Result localResult = Result.newErrorResult(Error.error(1852));
        return localResult;
      }
      long l = ((Long)arrayOfObject[1]).longValue();
      if (paramInt == 0)
      {
        localObject1 = ResultLob.newLobSetResponse(paramLong1, l);
        return (Result)localObject1;
      }
      Object localObject1 = setBytesBA(paramLong1, paramLong2, paramArrayOfByte, paramInt, false);
      if (((Result)localObject1).isError())
      {
        localObject2 = localObject1;
        return (Result)localObject2;
      }
      if (paramLong2 + paramInt > l)
      {
        l = paramLong2 + paramInt;
        localObject1 = setLength(paramLong1, l);
        if (((Result)localObject1).isError())
        {
          localObject2 = localObject1;
          return (Result)localObject2;
        }
      }
      Object localObject2 = ResultLob.newLobSetResponse(paramLong1, l);
      return (Result)localObject2;
    }
    finally
    {
      this.writeLock.unlock();
    }
  }
  
  public Result setBytesForNewBlob(long paramLong1, InputStream paramInputStream, long paramLong2)
  {
    if (paramLong2 == 0L) {
      return ResultLob.newLobSetResponse(paramLong1, 0L);
    }
    if (this.byteBuffer == null) {
      throw Error.error(456);
    }
    this.writeLock.lock();
    try
    {
      Result localResult1 = setBytesIS(paramLong1, paramInputStream, paramLong2, false);
      Result localResult2 = localResult1;
      return localResult2;
    }
    finally
    {
      this.writeLock.unlock();
    }
  }
  
  public Result setChars(long paramLong1, long paramLong2, char[] paramArrayOfChar, int paramInt)
  {
    if (this.byteBuffer == null) {
      throw Error.error(456);
    }
    this.writeLock.lock();
    try
    {
      Object[] arrayOfObject = getLobHeader(paramLong1);
      if (arrayOfObject == null)
      {
        Result localResult1 = Result.newErrorResult(Error.error(1852));
        return localResult1;
      }
      long l = ((Long)arrayOfObject[1]).longValue();
      if (paramInt == 0)
      {
        localObject1 = ResultLob.newLobSetResponse(paramLong1, l);
        return (Result)localObject1;
      }
      Object localObject1 = ArrayUtil.charArrayToBytes(paramArrayOfChar, paramInt);
      Result localResult2 = setBytesBA(paramLong1, paramLong2 * 2L, (byte[])localObject1, paramInt * 2, true);
      if (localResult2.isError())
      {
        localObject2 = localResult2;
        return (Result)localObject2;
      }
      if (paramLong2 + paramInt > l)
      {
        l = paramLong2 + paramInt;
        localResult2 = setLength(paramLong1, l);
        if (localResult2.isError())
        {
          localObject2 = localResult2;
          return (Result)localObject2;
        }
      }
      Object localObject2 = ResultLob.newLobSetResponse(paramLong1, l);
      return (Result)localObject2;
    }
    finally
    {
      this.writeLock.unlock();
    }
  }
  
  public Result setCharsForNewClob(long paramLong1, InputStream paramInputStream, long paramLong2)
  {
    if (paramLong2 == 0L) {
      return ResultLob.newLobSetResponse(paramLong1, 0L);
    }
    if (this.byteBuffer == null) {
      throw Error.error(456);
    }
    this.writeLock.lock();
    try
    {
      Result localResult1 = setBytesIS(paramLong1, paramInputStream, paramLong2 * 2L, false);
      if (localResult1.isError())
      {
        Result localResult2 = localResult1;
        return localResult2;
      }
      long l = ((ResultLob)localResult1).getBlockLength();
      if (l < paramLong2) {
        localResult3 = truncate(paramLong1, l);
      }
      Result localResult3 = localResult1;
      return localResult3;
    }
    finally
    {
      this.writeLock.unlock();
    }
  }
  
  public Result truncate(long paramLong1, long paramLong2)
  {
    if (this.byteBuffer == null) {
      throw Error.error(456);
    }
    this.writeLock.lock();
    try
    {
      Object[] arrayOfObject1 = getLobHeader(paramLong1);
      if (arrayOfObject1 == null)
      {
        Result localResult1 = Result.newErrorResult(Error.error(1852));
        return localResult1;
      }
      long l1 = ((Long)arrayOfObject1[1]).longValue();
      long l2 = paramLong2;
      if (((Integer)arrayOfObject1[3]).intValue() == 40) {
        l2 *= 2L;
      }
      int i = (int)((l2 + this.lobBlockSize - 1L) / this.lobBlockSize);
      ResultMetaData localResultMetaData = this.deleteLobPartCall.getParametersMetaData();
      Object[] arrayOfObject2 = new Object[localResultMetaData.getColumnCount()];
      arrayOfObject2[0] = ValuePool.getLong(paramLong1);
      arrayOfObject2[1] = Integer.valueOf(i);
      arrayOfObject2[2] = ValuePool.INTEGER_MAX;
      arrayOfObject2[3] = ValuePool.getLong(this.sysLobSession.getTransactionTimestamp());
      Result localResult2 = this.sysLobSession.executeCompiledStatement(this.deleteLobPartCall, arrayOfObject2, 0);
      setLength(paramLong1, paramLong2);
      ResultLob localResultLob = ResultLob.newLobTruncateResponse(paramLong1, paramLong2);
      return localResultLob;
    }
    finally
    {
      this.writeLock.unlock();
    }
  }
  
  private Result setLength(long paramLong1, long paramLong2)
  {
    ResultMetaData localResultMetaData = this.updateLobLength.getParametersMetaData();
    Object[] arrayOfObject = new Object[localResultMetaData.getColumnCount()];
    arrayOfObject[0] = ValuePool.getLong(paramLong2);
    arrayOfObject[1] = ValuePool.getLong(paramLong1);
    Result localResult = this.sysLobSession.executeCompiledStatement(this.updateLobLength, arrayOfObject, 0);
    return localResult;
  }
  
  public Result adjustUsageCount(Session paramSession, long paramLong, int paramInt)
  {
    ResultMetaData localResultMetaData = this.updateLobUsage.getParametersMetaData();
    Object[] arrayOfObject = new Object[localResultMetaData.getColumnCount()];
    arrayOfObject[0] = ValuePool.getInt(paramInt);
    arrayOfObject[1] = ValuePool.getLong(paramLong);
    paramSession.sessionContext.pushDynamicArguments(arrayOfObject);
    Result localResult = this.updateLobUsage.execute(paramSession);
    this.usageChanged = true;
    paramSession.sessionContext.pop();
    return localResult;
  }
  
  private int[][] getBlockAddresses(long paramLong, int paramInt1, int paramInt2)
  {
    ResultMetaData localResultMetaData = this.getSpanningBlocks.getParametersMetaData();
    Object[] arrayOfObject1 = new Object[localResultMetaData.getColumnCount()];
    arrayOfObject1[0] = ValuePool.getLong(paramLong);
    arrayOfObject1[1] = ValuePool.getInt(paramInt1);
    arrayOfObject1[2] = ValuePool.getInt(paramInt2);
    this.sysLobSession.sessionContext.pushDynamicArguments(arrayOfObject1);
    Result localResult = this.getSpanningBlocks.execute(this.sysLobSession);
    this.sysLobSession.sessionContext.pop();
    RowSetNavigator localRowSetNavigator = localResult.getNavigator();
    int i = localRowSetNavigator.getSize();
    int[][] arrayOfInt = new int[i][3];
    for (int j = 0; j < i; j++)
    {
      localRowSetNavigator.absolute(j);
      Object[] arrayOfObject2 = localRowSetNavigator.getCurrent();
      arrayOfInt[j][0] = ((Integer)arrayOfObject2[0]).intValue();
      arrayOfInt[j][1] = ((Integer)arrayOfObject2[1]).intValue();
      arrayOfInt[j][2] = ((Integer)arrayOfObject2[2]).intValue();
    }
    localRowSetNavigator.release();
    return arrayOfInt;
  }
  
  private void deleteBlockAddresses(long paramLong, int paramInt1, int paramInt2)
  {
    ResultMetaData localResultMetaData = this.deleteLobPartCall.getParametersMetaData();
    Object[] arrayOfObject = new Object[localResultMetaData.getColumnCount()];
    arrayOfObject[0] = ValuePool.getLong(paramLong);
    arrayOfObject[1] = ValuePool.getInt(paramInt1);
    arrayOfObject[2] = ValuePool.getInt(paramInt2);
    arrayOfObject[3] = ValuePool.getLong(this.sysLobSession.getTransactionTimestamp());
    Result localResult = this.sysLobSession.executeCompiledStatement(this.deleteLobPartCall, arrayOfObject, 0);
  }
  
  private void divideBlockAddresses(long paramLong, int paramInt)
  {
    ResultMetaData localResultMetaData = this.divideLobPartCall.getParametersMetaData();
    Object[] arrayOfObject = new Object[localResultMetaData.getColumnCount()];
    arrayOfObject[0] = ValuePool.getInt(paramInt);
    arrayOfObject[1] = ValuePool.getLong(paramLong);
    Result localResult = this.sysLobSession.executeCompiledStatement(this.divideLobPartCall, arrayOfObject, 0);
  }
  
  private Result createBlockAddresses(long paramLong, int paramInt1, int paramInt2)
  {
    ResultMetaData localResultMetaData = this.createLobPartCall.getParametersMetaData();
    Object[] arrayOfObject = new Object[localResultMetaData.getColumnCount()];
    arrayOfObject[0] = ValuePool.getInt(paramInt2);
    arrayOfObject[1] = ValuePool.getInt(paramInt1);
    arrayOfObject[2] = ValuePool.getLong(paramLong);
    Result localResult = this.sysLobSession.executeCompiledStatement(this.createLobPartCall, arrayOfObject, 0);
    return localResult;
  }
  
  private Result createFullBlockAddresses(long paramLong, int paramInt1, int paramInt2)
  {
    ResultMetaData localResultMetaData = this.createSingleLobPartCall.getParametersMetaData();
    Object[] arrayOfObject = new Object[localResultMetaData.getColumnCount()];
    arrayOfObject[0] = ValuePool.getInt(paramInt2);
    arrayOfObject[1] = ValuePool.getInt(paramInt1);
    arrayOfObject[2] = ValuePool.getLong(paramLong);
    Result localResult = this.sysLobSession.executeCompiledStatement(this.createSingleLobPartCall, arrayOfObject, 0);
    return localResult;
  }
  
  private Result createPart(long paramLong1, long paramLong2, int paramInt1, int paramInt2, int paramInt3, int paramInt4)
  {
    ResultMetaData localResultMetaData = this.createPart.getParametersMetaData();
    Object[] arrayOfObject = new Object[localResultMetaData.getColumnCount()];
    arrayOfObject[0] = ValuePool.getInt(paramInt4);
    arrayOfObject[1] = ValuePool.getInt(paramInt3);
    arrayOfObject[2] = ValuePool.getLong(paramLong2);
    arrayOfObject[3] = ValuePool.getLong(paramInt1);
    arrayOfObject[4] = ValuePool.getLong(paramInt2);
    arrayOfObject[5] = ValuePool.getLong(paramLong1);
    Result localResult = this.sysLobSession.executeCompiledStatement(this.createPart, arrayOfObject, 0);
    return localResult;
  }
  
  private int getBlockAddress(int[][] paramArrayOfInt, int paramInt)
  {
    for (int i = 0; i < paramArrayOfInt.length; i++) {
      if (paramArrayOfInt[i][2] + paramArrayOfInt[i][1] > paramInt) {
        return paramArrayOfInt[i][0] - paramArrayOfInt[i][2] + paramInt;
      }
    }
    return -1;
  }
  
  public int getLobCount()
  {
    this.writeLock.lock();
    try
    {
      this.sysLobSession.sessionContext.pushDynamicArguments(new Object[0]);
      Result localResult = this.getLobCount.execute(this.sysLobSession);
      this.sysLobSession.sessionContext.pop();
      RowSetNavigator localRowSetNavigator = localResult.getNavigator();
      boolean bool = localRowSetNavigator.next();
      if (!bool)
      {
        localRowSetNavigator.release();
        int i = 0;
        return i;
      }
      Object[] arrayOfObject = localRowSetNavigator.getCurrent();
      int j = ((Number)arrayOfObject[0]).intValue();
      return j;
    }
    finally
    {
      this.writeLock.unlock();
    }
  }
  
  public void synch()
  {
    if ((this.storeModified) && (this.lobStore != null))
    {
      this.writeLock.lock();
      try
      {
        try
        {
          this.lobStore.synch();
        }
        catch (Throwable localThrowable) {}
        this.storeModified = false;
      }
      finally
      {
        this.writeLock.unlock();
      }
    }
  }
  
  private long[][] getParts(long paramLong1, long paramLong2, long paramLong3)
  {
    ResultMetaData localResultMetaData = this.getSpanningParts.getParametersMetaData();
    Object[] arrayOfObject1 = new Object[localResultMetaData.getColumnCount()];
    arrayOfObject1[0] = ValuePool.getLong(paramLong1);
    arrayOfObject1[1] = ValuePool.getLong(paramLong2);
    arrayOfObject1[2] = ValuePool.getLong(paramLong3);
    this.sysLobSession.sessionContext.pushDynamicArguments(arrayOfObject1);
    Result localResult = this.getSpanningParts.execute(this.sysLobSession);
    this.sysLobSession.sessionContext.pop();
    RowSetNavigator localRowSetNavigator = localResult.getNavigator();
    int i = localRowSetNavigator.getSize();
    long[][] arrayOfLong = new long[i][6];
    for (int j = 0; j < i; j++)
    {
      localRowSetNavigator.absolute(j);
      Object[] arrayOfObject2 = localRowSetNavigator.getCurrent();
      for (int k = 0; k < arrayOfLong[j].length; k++) {
        arrayOfLong[j][k] = ((Number)arrayOfObject2[k]).longValue();
      }
    }
    localRowSetNavigator.release();
    return arrayOfLong;
  }
  
  private void inflate(byte[] paramArrayOfByte, int paramInt, boolean paramBoolean)
  {
    if (this.cryptLobs) {
      paramInt = this.database.logger.getCrypto().decode(paramArrayOfByte, 0, paramInt, paramArrayOfByte, 0);
    }
    try
    {
      this.inflater.setInput(paramArrayOfByte, 0, paramInt);
      paramInt = this.inflater.inflate(this.dataBuffer);
      this.inflater.reset();
    }
    catch (DataFormatException localDataFormatException) {}catch (Throwable localThrowable) {}
    int i = (int)ArrayUtil.getBinaryMultipleCeiling(paramInt, this.lobBlockSize);
    for (int j = paramInt; j < i; j++) {
      this.dataBuffer[j] = 0;
    }
  }
  
  private int deflate(byte[] paramArrayOfByte, int paramInt1, int paramInt2, boolean paramBoolean)
  {
    this.deflater.setInput(paramArrayOfByte, paramInt1, paramInt2);
    this.deflater.finish();
    paramInt2 = this.deflater.deflate(this.dataBuffer);
    this.deflater.reset();
    if (this.cryptLobs) {
      paramInt2 = this.database.logger.getCrypto().encode(this.dataBuffer, 0, paramInt2, this.dataBuffer, 0);
    }
    int i = (int)ArrayUtil.getBinaryMultipleCeiling(paramInt2, this.lobBlockSize);
    for (int j = paramInt2; j < i; j++) {
      this.dataBuffer[j] = 0;
    }
    return paramInt2;
  }
  
  private int compareBytesCompressed(long paramLong1, long paramLong2)
  {
    long[][] arrayOfLong1 = getParts(paramLong1, 0L, Long.MAX_VALUE);
    long[][] arrayOfLong2 = getParts(paramLong2, 0L, Long.MAX_VALUE);
    for (int i = 0; (i < arrayOfLong1.length) && (i < arrayOfLong2.length); i++)
    {
      int j = (int)arrayOfLong1[i][3];
      getPartBytesCompressedInBuffer(paramLong1, arrayOfLong1[i], false);
      byte[] arrayOfByte = new byte[j];
      System.arraycopy(this.dataBuffer, 0, arrayOfByte, 0, j);
      int k = (int)arrayOfLong2[i][3];
      getPartBytesCompressedInBuffer(paramLong1, arrayOfLong2[i], false);
      int m = ArrayUtil.compare(arrayOfByte, 0, j, this.byteBuffer, k);
      if (m != 0) {
        return m;
      }
    }
    if (arrayOfLong1.length == arrayOfLong2.length) {
      return 0;
    }
    return arrayOfLong1.length > arrayOfLong2.length ? 1 : -1;
  }
  
  private int compareTextCompressed(Collation paramCollation, long paramLong1, long paramLong2)
  {
    long[][] arrayOfLong1 = getParts(paramLong1, 0L, Long.MAX_VALUE);
    long[][] arrayOfLong2 = getParts(paramLong2, 0L, Long.MAX_VALUE);
    for (int i = 0; (i < arrayOfLong1.length) && (i < arrayOfLong2.length); i++)
    {
      int j = (int)arrayOfLong1[i][3];
      getPartBytesCompressedInBuffer(paramLong1, arrayOfLong1[i], true);
      String str1 = new String(ArrayUtil.byteArrayToChars(this.dataBuffer, j));
      int k = (int)arrayOfLong2[i][3];
      getPartBytesCompressedInBuffer(paramLong2, arrayOfLong2[i], true);
      String str2 = new String(ArrayUtil.byteArrayToChars(this.dataBuffer, k));
      int m = paramCollation.compare(str1, str2);
      if (m != 0) {
        return m;
      }
    }
    if (arrayOfLong1.length == arrayOfLong2.length) {
      return 0;
    }
    return arrayOfLong1.length > arrayOfLong2.length ? 1 : -1;
  }
  
  private Result setBytesISCompressed(long paramLong1, InputStream paramInputStream, long paramLong2, boolean paramBoolean)
  {
    long l = 0L;
    byte[] arrayOfByte;
    if (paramLong2 < this.largeLobBlockSize) {
      arrayOfByte = new byte[(int)paramLong2];
    } else {
      arrayOfByte = new byte[this.largeLobBlockSize];
    }
    for (;;)
    {
      int i = arrayOfByte.length;
      if (i > paramLong2 - l) {
        i = (int)(paramLong2 - l);
      }
      int j = 0;
      try
      {
        while (i > 0)
        {
          int k = paramInputStream.read(arrayOfByte, j, i);
          if (k == -1) {
            return Result.newErrorResult(new EOFException());
          }
          i -= k;
          j += k;
        }
      }
      catch (IOException localIOException)
      {
        return Result.newErrorResult(localIOException);
      }
      Result localResult = setBytesBACompressedPart(paramLong1, l, arrayOfByte, j, paramBoolean);
      if (localResult.isError()) {
        return localResult;
      }
      l += j;
      if (l == paramLong2) {
        break;
      }
    }
    this.storeModified = true;
    return ResultLob.newLobSetResponse(paramLong1, paramLong2);
  }
  
  private Result setBytesBACompressed(long paramLong1, long paramLong2, byte[] paramArrayOfByte, int paramInt, boolean paramBoolean)
  {
    if (paramInt == 0) {
      return ResultLob.newLobSetResponse(paramLong1, 0L);
    }
    if (paramInt <= this.largeLobBlockSize) {
      return setBytesBACompressedPart(paramLong1, paramLong2, paramArrayOfByte, paramInt, paramBoolean);
    }
    HsqlByteArrayInputStream localHsqlByteArrayInputStream = new HsqlByteArrayInputStream(paramArrayOfByte, 0, paramInt);
    return setBytesISCompressed(paramLong1, localHsqlByteArrayInputStream, paramInt, paramBoolean);
  }
  
  private Result setBytesBACompressedPart(long paramLong1, long paramLong2, byte[] paramArrayOfByte, int paramInt, boolean paramBoolean)
  {
    long[] arrayOfLong = getLastPart(paramLong1);
    int i = (int)arrayOfLong[1] + (int)arrayOfLong[0];
    long l1 = arrayOfLong[2] + arrayOfLong[3];
    if ((l1 != paramLong2) || (l1 % this.largeLobBlockSize != 0L)) {
      return Result.newErrorResult(Error.error(1551, "compressed lobs"));
    }
    int j = deflate(paramArrayOfByte, 0, paramInt, paramBoolean);
    int k = (j + this.lobBlockSize - 1) / this.lobBlockSize;
    Result localResult = createFullBlockAddresses(paramLong1, i, k);
    if (localResult.isError()) {
      return localResult;
    }
    localResult = createPart(paramLong1, paramLong2, paramInt, j, i, k);
    if (localResult.isError()) {
      return localResult;
    }
    long l2 = i * this.lobBlockSize;
    int m = (int)ArrayUtil.getBinaryMultipleCeiling(j, this.lobBlockSize);
    setBytesBANormal(paramLong1, l2, this.dataBuffer, m);
    this.storeModified = true;
    return ResultLob.newLobSetResponse(paramLong1, paramInt);
  }
  
  private Result getBytesCompressed(long paramLong1, long paramLong2, int paramInt, boolean paramBoolean)
  {
    byte[] arrayOfByte = new byte[paramInt];
    long[][] arrayOfLong = getParts(paramLong1, paramLong2, paramLong2 + paramInt);
    for (int i = 0; i < arrayOfLong.length; i++)
    {
      long[] arrayOfLong1 = arrayOfLong[i];
      long l = arrayOfLong1[2];
      int j = (int)arrayOfLong1[3];
      Result localResult = getPartBytesCompressedInBuffer(paramLong1, arrayOfLong1, paramBoolean);
      if (localResult.isError()) {
        return localResult;
      }
      ArrayUtil.copyBytes(l, this.dataBuffer, 0, j, paramLong2, arrayOfByte, paramInt);
    }
    return ResultLob.newLobGetBytesResponse(paramLong1, paramLong2, arrayOfByte);
  }
  
  private Result getPartBytesCompressedInBuffer(long paramLong, long[] paramArrayOfLong, boolean paramBoolean)
  {
    long l1 = paramArrayOfLong[1];
    long l2 = paramArrayOfLong[2];
    long l3 = paramArrayOfLong[3];
    int i = (int)paramArrayOfLong[4];
    long l4 = l1 * this.lobBlockSize;
    Result localResult = getBytesNormal(paramLong, l4, i);
    if (localResult.isError()) {
      return localResult;
    }
    byte[] arrayOfByte = ((ResultLob)localResult).getByteArray();
    inflate(arrayOfByte, i, paramBoolean);
    return ResultLob.newLobSetResponse(paramLong, l3);
  }
  
  private long[] getLastPart(long paramLong)
  {
    ResultMetaData localResultMetaData = this.getLastPart.getParametersMetaData();
    Object[] arrayOfObject1 = new Object[localResultMetaData.getColumnCount()];
    arrayOfObject1[0] = ValuePool.getLong(paramLong);
    this.sysLobSession.sessionContext.pushDynamicArguments(arrayOfObject1);
    Result localResult = this.getLastPart.execute(this.sysLobSession);
    this.sysLobSession.sessionContext.pop();
    RowSetNavigator localRowSetNavigator = localResult.getNavigator();
    int i = localRowSetNavigator.getSize();
    long[] arrayOfLong = new long[6];
    if (i == 0)
    {
      arrayOfLong[5] = paramLong;
    }
    else
    {
      localRowSetNavigator.absolute(0);
      Object[] arrayOfObject2 = localRowSetNavigator.getCurrent();
      for (int j = 0; j < arrayOfLong.length; j++) {
        arrayOfLong[j] = ((Number)arrayOfObject2[j]).longValue();
      }
    }
    localRowSetNavigator.release();
    return arrayOfLong;
  }
  
  private static abstract interface ALLOC_PART
  {
    public static final int BLOCK_COUNT = 0;
    public static final int BLOCK_OFFSET = 1;
    public static final int PART_OFFSET = 2;
    public static final int PART_LENGTH = 3;
    public static final int PART_BYTES = 4;
    public static final int LOB_ID = 5;
  }
  
  private static abstract interface UPDATE_LENGTH
  {
    public static final int LOB_LENGTH = 0;
    public static final int LOB_ID = 1;
  }
  
  private static abstract interface UPDATE_USAGE
  {
    public static final int BLOCK_COUNT = 0;
    public static final int LOB_ID = 1;
  }
  
  private static abstract interface ALLOC_BLOCKS
  {
    public static final int BLOCK_COUNT = 0;
    public static final int BLOCK_OFFSET = 1;
    public static final int LOB_ID = 2;
  }
  
  private static abstract interface DELETE_BLOCKS
  {
    public static final int LOB_ID = 0;
    public static final int BLOCK_OFFSET = 1;
    public static final int BLOCK_LIMIT = 2;
    public static final int TX_ID = 3;
  }
  
  private static abstract interface DIVIDE_BLOCK
  {
    public static final int BLOCK_OFFSET = 0;
    public static final int LOB_ID = 1;
  }
  
  private static abstract interface GET_LOB_PART
  {
    public static final int LOB_ID = 0;
    public static final int BLOCK_OFFSET = 1;
    public static final int BLOCK_LIMIT = 2;
  }
  
  private static abstract interface LOB_IDS
  {
    public static final int LOB_ID = 0;
    public static final int LOB_LENGTH = 1;
    public static final int LOB_USAGE_COUNT = 2;
    public static final int LOB_TYPE = 3;
  }
  
  private static abstract interface LOBS
  {
    public static final int BLOCK_ADDR = 0;
    public static final int BLOCK_COUNT = 1;
    public static final int BLOCK_OFFSET = 2;
    public static final int LOB_ID = 3;
  }
}


/* Location:              E:\java\java学习\hutubill\lib\all.jar!\org\hsqldb\persist\LobManager.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */
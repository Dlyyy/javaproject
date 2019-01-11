package org.hsqldb.persist;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.lang.reflect.Constructor;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.atomic.AtomicInteger;
import org.hsqldb.Database;
import org.hsqldb.DatabaseType;
import org.hsqldb.HsqlNameManager.HsqlName;
import org.hsqldb.NumberSequence;
import org.hsqldb.Row;
import org.hsqldb.SchemaManager;
import org.hsqldb.Session;
import org.hsqldb.Statement;
import org.hsqldb.Table;
import org.hsqldb.TableBase;
import org.hsqldb.TransactionManager;
import org.hsqldb.TransactionManagerMV2PL;
import org.hsqldb.TransactionManagerMVCC;
import org.hsqldb.error.Error;
import org.hsqldb.index.Index;
import org.hsqldb.index.IndexAVL;
import org.hsqldb.index.IndexAVLMemory;
import org.hsqldb.lib.ArrayUtil;
import org.hsqldb.lib.FileAccess;
import org.hsqldb.lib.FileUtil;
import org.hsqldb.lib.FrameworkLogger;
import org.hsqldb.lib.HsqlArrayList;
import org.hsqldb.lib.InputStreamInterface;
import org.hsqldb.lib.InputStreamWrapper;
import org.hsqldb.lib.SimpleLog;
import org.hsqldb.lib.StringUtil;
import org.hsqldb.lib.tar.DbBackup;
import org.hsqldb.lib.tar.TarMalformatException;
import org.hsqldb.navigator.RowSetNavigator;
import org.hsqldb.result.Result;
import org.hsqldb.result.ResultMetaData;
import org.hsqldb.rights.GranteeManager;
import org.hsqldb.scriptio.ScriptWriterText;
import org.hsqldb.types.Collation;
import org.hsqldb.types.RowType;
import org.hsqldb.types.Type;

public class Logger
  implements EventLogInterface
{
  public SimpleLog appLog;
  public SimpleLog sqlLog;
  FrameworkLogger fwLogger;
  FrameworkLogger sqlLogger;
  private Database database;
  private boolean logsStatements;
  private boolean loggingEnabled;
  private boolean syncFile = false;
  private boolean propIsFileDatabase;
  boolean propIncrementBackup;
  boolean propNioDataFile;
  long propNioMaxSize = 268435456L;
  int propMaxFreeBlocks = 512;
  int propMinReuse = 0;
  private int propCacheMaxRows;
  private long propCacheMaxSize;
  int propCacheDefragLimit;
  private int propDataFileScale;
  String propTextSourceDefault = "";
  boolean propTextAllowFullPath;
  private int propWriteDelay;
  private int propLogSize;
  private boolean propLogData = true;
  private int propEventLogLevel;
  int propSqlLogLevel;
  int propGC;
  int propTxMode = 0;
  boolean propRefIntegrity = true;
  int propLobBlockSize = 32768;
  boolean propCompressLobs;
  int propScriptFormat = 0;
  boolean propLargeData;
  int propFileSpaceValue;
  long propFileTimestamp;
  Log log;
  private LockFile lockFile;
  private Crypto crypto;
  boolean cryptLobs;
  public FileAccess fileAccess;
  public boolean isStoredFileAccess;
  public boolean isNewStoredFileAccess;
  String tempDirectoryPath;
  public TextTableStorageManager textTableManager = new TextTableStorageManager();
  public boolean isNewDatabase;
  public boolean isSingleFile;
  AtomicInteger backupState = new AtomicInteger();
  AtomicInteger checkpointState = new AtomicInteger();
  static final int largeDataFactor = 128;
  static final int stateNormal = 0;
  static final int stateBackup = 1;
  static final int stateCheckpoint = 2;
  static final int stateCheckpointNormal = 0;
  static final int stateCheckpointRequired = 1;
  static final int stateCheckpointDue = 2;
  public static final String oldFileExtension = ".old";
  public static final String newFileExtension = ".new";
  public static final String appLogFileExtension = ".app.log";
  public static final String sqlLogFileExtension = ".sql.log";
  public static final String logFileExtension = ".log";
  public static final String scriptFileExtension = ".script";
  public static final String propertiesFileExtension = ".properties";
  public static final String dataFileExtension = ".data";
  public static final String backupFileExtension = ".backup";
  public static final String lobsFileExtension = ".lobs";
  public static final String lockFileExtension = ".lck";
  SimpleDateFormat backupFileFormat = new SimpleDateFormat("yyyyMMdd'T'HHmmss");
  private static char runtimeFileDelim = System.getProperty("file.separator").charAt(0);
  DbBackup backup;
  
  public Logger(Database paramDatabase)
  {
    this.database = paramDatabase;
  }
  
  public void open()
  {
    String str1 = this.database.getURLProperties().getProperty("fileaccess_class_name");
    String str2 = this.database.getURLProperties().getProperty("storage_class_name");
    String str3 = 0;
    String str4 = 0;
    String str5;
    if (str1 != null)
    {
      str5 = this.database.getURLProperties().getProperty("storage_key");
      try
      {
        Class localClass1 = null;
        Class localClass2 = null;
        try
        {
          ClassLoader localClassLoader = Thread.currentThread().getContextClassLoader();
          localClass1 = localClassLoader.loadClass(str1);
          localClass2 = localClassLoader.loadClass(str2);
        }
        catch (ClassNotFoundException localClassNotFoundException2)
        {
          localClass1 = Class.forName(str1);
          localClass2 = Class.forName(str2);
        }
        if (localClass2.isAssignableFrom(RandomAccessInterface.class)) {
          this.isNewStoredFileAccess = true;
        }
        Constructor localConstructor = localClass1.getConstructor(new Class[] { Object.class });
        this.fileAccess = ((FileAccess)localConstructor.newInstance(new Object[] { str5 }));
        this.isStoredFileAccess = true;
      }
      catch (ClassNotFoundException localClassNotFoundException1)
      {
        System.out.println("ClassNotFoundException");
      }
      catch (InstantiationException localInstantiationException)
      {
        System.out.println("InstantiationException");
      }
      catch (IllegalAccessException localIllegalAccessException)
      {
        System.out.println("IllegalAccessException");
      }
      catch (Exception localException)
      {
        System.out.println("Exception");
      }
    }
    else
    {
      this.fileAccess = FileUtil.getFileAccess(this.database.isFilesInJar());
    }
    this.propIsFileDatabase = this.database.getType().isFileBased();
    this.database.databaseProperties = new HsqlDatabaseProperties(this.database);
    this.propTextAllowFullPath = this.database.databaseProperties.isPropertyTrue("textdb.allow_full_path");
    if (this.propIsFileDatabase)
    {
      str3 = this.database.databaseProperties.load();
      str4 = this.fileAccess.isStreamElement(this.database.getPath() + ".script");
      boolean bool1;
      if (this.database.databaseProperties.isVersion18())
      {
        str5 = str3;
        this.database.databaseProperties.setProperty("hsqldb.inc_backup", false);
      }
      else
      {
        str5 = str4;
        if (str5 == 0)
        {
          bool1 = this.fileAccess.isStreamElement(this.database.getPath() + ".script" + ".new");
          if (bool1) {
            this.database.databaseProperties.setDBModified(2);
          }
        }
      }
      this.isNewDatabase = (!bool1);
    }
    else
    {
      this.isNewDatabase = true;
    }
    if (this.isNewDatabase)
    {
      str6 = newUniqueName();
      this.database.setDatabaseName(str6);
      boolean bool2 = this.database.isFilesInJar();
      bool2 |= ((this.database.urlProperties.isPropertyTrue("ifexists")) || (!this.database.urlProperties.isPropertyTrue("create", true)));
      if (bool2) {
        throw Error.error(465, this.database.getPath());
      }
      this.database.databaseProperties.setURLProperties(this.database.urlProperties);
    }
    else
    {
      if (str3 == 0) {
        this.database.databaseProperties.setDBModified(1);
      }
      if (this.database.urlProperties.isPropertyTrue("files_readonly")) {
        this.database.databaseProperties.setProperty("files_readonly", true);
      }
      if (this.database.urlProperties.isPropertyTrue("readonly")) {
        this.database.databaseProperties.setProperty("readonly", true);
      }
      if (!this.database.urlProperties.isPropertyTrue("hsqldb.lock_file", true)) {
        this.database.databaseProperties.setProperty("hsqldb.lock_file", false);
      }
    }
    setVariables();
    String str6 = null;
    String str7 = null;
    if ((this.propIsFileDatabase) && (!this.database.isFilesReadOnly()))
    {
      str6 = this.database.getPath() + ".app.log";
      str7 = this.database.getPath() + ".sql.log";
    }
    this.appLog = new SimpleLog(str6, this.propEventLogLevel, false);
    this.sqlLog = new SimpleLog(str7, this.propSqlLogLevel, true);
    this.database.setReferentialIntegrity(this.propRefIntegrity);
    if (!isFileDatabase()) {
      return;
    }
    this.checkpointState.set(0);
    this.logsStatements = false;
    boolean bool3 = this.database.getProperties().isPropertyTrue("hsqldb.lock_file");
    if ((bool3) && (!this.database.isFilesReadOnly())) {
      acquireLock(this.database.getPath());
    }
    boolean bool4 = this.database.databaseProperties.isVersion18();
    if (bool4)
    {
      this.database.setDatabaseName(newUniqueName());
      this.database.schemaManager.createPublicSchema();
      HsqlNameManager.HsqlName localHsqlName = this.database.schemaManager.findSchemaHsqlName("PUBLIC");
      this.database.schemaManager.setDefaultSchemaHsqlName(localHsqlName);
    }
    this.log = new Log(this.database);
    this.log.open();
    this.logsStatements = true;
    this.loggingEnabled = ((this.propLogData) && (!this.database.isFilesReadOnly()));
    if (bool4) {
      checkpoint(null, false, false);
    }
    if (this.database.getNameString() == null) {
      this.database.setDatabaseName(newUniqueName());
    }
    int i = this.database.urlProperties.getIntegerProperty("hsqldb.applog", -1);
    if (i >= 0) {
      setEventLogLevel(i, false);
    }
    i = this.database.urlProperties.getIntegerProperty("hsqldb.sqllog", -1);
    if (i >= 0) {
      setEventLogLevel(i, true);
    }
  }
  
  private void setVariables()
  {
    String str1 = this.database.urlProperties.getProperty("crypt_key");
    if (str1 != null)
    {
      String str2 = this.database.urlProperties.getProperty("crypt_type");
      str3 = this.database.urlProperties.getProperty("crypt_provider");
      this.crypto = new Crypto(str1, str2, str3);
      this.cryptLobs = this.database.urlProperties.isPropertyTrue("crypt_lobs", true);
    }
    if (this.database.databaseProperties.isPropertyTrue("readonly")) {
      this.database.setReadOnly();
    }
    if (this.database.databaseProperties.isPropertyTrue("files_readonly")) {
      this.database.setFilesReadOnly();
    }
    if (!this.database.isFilesReadOnly())
    {
      if ((this.database.getType() == DatabaseType.DB_MEM) || (this.isStoredFileAccess)) {
        this.tempDirectoryPath = this.database.getProperties().getStringProperty("hsqldb.temp_directory");
      } else {
        this.tempDirectoryPath = (this.database.getPath() + ".tmp");
      }
      if (this.tempDirectoryPath != null) {
        this.tempDirectoryPath = FileUtil.makeDirectories(this.tempDirectoryPath);
      }
    }
    this.propScriptFormat = this.database.databaseProperties.getIntegerProperty("hsqldb.script_format");
    boolean bool = this.database.databaseProperties.isVersion18();
    this.propMaxFreeBlocks = this.database.databaseProperties.getIntegerProperty("hsqldb.cache_free_count");
    this.propMaxFreeBlocks = ArrayUtil.getTwoPowerFloor(this.propMaxFreeBlocks);
    if (this.database.urlProperties.isPropertyTrue("hsqldb.large_data", false)) {
      this.propLargeData = true;
    }
    if (!this.database.databaseProperties.isPropertyTrue("sql.pad_space", true)) {
      this.database.collation.setPadding(false);
    }
    if ((bool) && (this.isStoredFileAccess)) {
      this.database.collation.setPadding(false);
    }
    String str3 = this.database.getProperties().getStringProperty("hsqldb.digest");
    this.database.granteeManager.setDigestAlgo(str3);
    if ((!this.isNewDatabase) && (!bool)) {
      return;
    }
    str3 = this.database.databaseProperties.getStringProperty("hsqldb.digest");
    this.database.granteeManager.setDigestAlgo(str3);
    if (this.tempDirectoryPath != null)
    {
      int i = this.database.databaseProperties.getIntegerProperty("hsqldb.result_max_memory_rows");
      this.database.setResultMaxMemoryRows(i);
    }
    String str4 = this.database.databaseProperties.getStringProperty("hsqldb.default_table_type");
    if ("CACHED".equalsIgnoreCase(str4)) {
      this.database.schemaManager.setDefaultTableType(5);
    }
    String str5 = this.database.databaseProperties.getStringProperty("hsqldb.tx");
    if ("MVCC".equalsIgnoreCase(str5)) {
      this.propTxMode = 2;
    } else if ("MVLOCKS".equalsIgnoreCase(str5)) {
      this.propTxMode = 1;
    } else if ("LOCKS".equalsIgnoreCase(str5)) {
      this.propTxMode = 0;
    }
    switch (this.propTxMode)
    {
    case 0: 
      break;
    case 1: 
      this.database.txManager = new TransactionManagerMV2PL(this.database);
      break;
    case 2: 
      this.database.txManager = new TransactionManagerMVCC(this.database);
    }
    String str6 = this.database.databaseProperties.getStringProperty("hsqldb.tx_level");
    if ("SERIALIZABLE".equalsIgnoreCase(str6)) {
      this.database.defaultIsolationLevel = 8;
    } else {
      this.database.defaultIsolationLevel = 2;
    }
    this.database.txConflictRollback = this.database.databaseProperties.isPropertyTrue("hsqldb.tx_conflict_rollback");
    this.database.sqlEnforceNames = this.database.databaseProperties.isPropertyTrue("sql.enforce_names");
    this.database.sqlRegularNames = this.database.databaseProperties.isPropertyTrue("sql.regular_names");
    this.database.sqlEnforceRefs = this.database.databaseProperties.isPropertyTrue("sql.enforce_refs");
    this.database.sqlEnforceSize = this.database.databaseProperties.isPropertyTrue("sql.enforce_size");
    this.database.sqlEnforceTypes = this.database.databaseProperties.isPropertyTrue("sql.enforce_types");
    this.database.sqlEnforceTDCD = this.database.databaseProperties.isPropertyTrue("sql.enforce_tdc_delete");
    this.database.sqlEnforceTDCU = this.database.databaseProperties.isPropertyTrue("sql.enforce_tdc_update");
    this.database.sqlTranslateTTI = this.database.databaseProperties.isPropertyTrue("jdbc.translate_tti_types");
    this.database.sqlLiveObject = this.database.databaseProperties.isPropertyTrue("sql.live_object");
    this.database.sqlCharLiteral = this.database.databaseProperties.isPropertyTrue("sql.char_literal");
    this.database.sqlConcatNulls = this.database.databaseProperties.isPropertyTrue("sql.concat_nulls");
    this.database.sqlNullsFirst = this.database.databaseProperties.isPropertyTrue("sql.nulls_first");
    this.database.sqlNullsOrder = this.database.databaseProperties.isPropertyTrue("sql.nulls_order");
    this.database.sqlUniqueNulls = this.database.databaseProperties.isPropertyTrue("sql.unique_nulls");
    this.database.sqlConvertTruncate = this.database.databaseProperties.isPropertyTrue("sql.convert_trunc");
    this.database.sqlAvgScale = this.database.databaseProperties.getIntegerProperty("sql.avg_scale");
    this.database.sqlDoubleNaN = this.database.databaseProperties.isPropertyTrue("sql.double_nan");
    this.database.sqlLongvarIsLob = this.database.databaseProperties.isPropertyTrue("sql.longvar_is_lob");
    this.database.sqlIgnoreCase = this.database.databaseProperties.isPropertyTrue("sql.ignore_case");
    this.database.sqlSyntaxDb2 = this.database.databaseProperties.isPropertyTrue("sql.syntax_db2");
    this.database.sqlSyntaxMss = this.database.databaseProperties.isPropertyTrue("sql.syntax_mss");
    this.database.sqlSyntaxMys = this.database.databaseProperties.isPropertyTrue("sql.syntax_mys");
    this.database.sqlSyntaxOra = this.database.databaseProperties.isPropertyTrue("sql.syntax_ora");
    this.database.sqlSyntaxPgs = this.database.databaseProperties.isPropertyTrue("sql.syntax_pgs");
    if (this.database.databaseProperties.isPropertyTrue("sql.compare_in_locale")) {
      this.database.collation.setCollationAsLocale();
    }
    this.propEventLogLevel = this.database.databaseProperties.getIntegerProperty("hsqldb.applog");
    this.propSqlLogLevel = this.database.databaseProperties.getIntegerProperty("hsqldb.sqllog");
    if (this.database.databaseProperties.isPropertyTrue("files_readonly")) {
      this.database.setFilesReadOnly();
    }
    if (this.database.databaseProperties.isPropertyTrue("readonly")) {
      this.database.setReadOnly();
    }
    this.propIncrementBackup = this.database.databaseProperties.isPropertyTrue("hsqldb.inc_backup");
    this.propNioDataFile = this.database.databaseProperties.isPropertyTrue("hsqldb.nio_data_file");
    this.propNioMaxSize = (this.database.databaseProperties.getIntegerProperty("hsqldb.nio_max_size") * 1024L * 1024L);
    this.propCacheMaxRows = this.database.databaseProperties.getIntegerProperty("hsqldb.cache_rows");
    this.propCacheMaxSize = (this.database.databaseProperties.getIntegerProperty("hsqldb.cache_size") * 1024L);
    setLobFileScaleNoCheck(this.database.databaseProperties.getIntegerProperty("hsqldb.lob_file_scale"));
    setLobFileCompressedNoCheck(this.database.databaseProperties.isPropertyTrue("hsqldb.lob_compressed"));
    setDataFileScaleNoCheck(this.database.databaseProperties.getIntegerProperty("hsqldb.cache_file_scale"));
    int j = this.database.databaseProperties.getIntegerProperty("hsqldb.files_space", 0);
    if (j != 0) {
      setDataFileSpaces(j);
    }
    this.propCacheDefragLimit = this.database.databaseProperties.getIntegerProperty("hsqldb.defrag_limit");
    this.propWriteDelay = this.database.databaseProperties.getIntegerProperty("hsqldb.write_delay_millis");
    if (!this.database.databaseProperties.isPropertyTrue("hsqldb.write_delay")) {
      this.propWriteDelay = 0;
    }
    this.propLogSize = this.database.databaseProperties.getIntegerProperty("hsqldb.log_size");
    this.propLogData = this.database.databaseProperties.isPropertyTrue("hsqldb.log_data");
    this.propGC = this.database.databaseProperties.getIntegerProperty("runtime.gc_interval");
    this.propRefIntegrity = this.database.databaseProperties.isPropertyTrue("sql.ref_integrity");
    setCacheMinReuseSize(this.database.databaseProperties.getIntegerProperty("hsqldb.min_reuse"));
  }
  
  public boolean close(int paramInt)
  {
    boolean bool = true;
    if (this.log == null)
    {
      this.textTableManager.closeAllTextCaches(false);
      return true;
    }
    this.log.synchLog();
    this.database.lobManager.synch();
    try
    {
      switch (paramInt)
      {
      case 1: 
        this.log.shutdown();
        break;
      case 2: 
        this.log.close(false);
        break;
      case 3: 
      case 4: 
        this.log.close(true);
      }
      this.database.persistentStoreCollection.release();
    }
    catch (Throwable localThrowable)
    {
      this.database.logger.logSevereEvent("error closing log", localThrowable);
      bool = false;
    }
    logInfoEvent("Database closed");
    this.log = null;
    this.appLog.close();
    this.sqlLog.close();
    this.logsStatements = false;
    this.loggingEnabled = false;
    return bool;
  }
  
  String newUniqueName()
  {
    String str = StringUtil.toPaddedString(Long.toHexString(System.currentTimeMillis()), 16, '0', false);
    str = "HSQLDB" + str.substring(6).toUpperCase(Locale.ENGLISH);
    return str;
  }
  
  public boolean isLogged()
  {
    return (this.propIsFileDatabase) && (!this.database.isFilesReadOnly());
  }
  
  public boolean isCurrentlyLogged()
  {
    return this.loggingEnabled;
  }
  
  public boolean isAllowedFullPath()
  {
    return this.propTextAllowFullPath;
  }
  
  private void getEventLogger()
  {
    if (this.fwLogger != null) {
      return;
    }
    String str = this.database.getNameString();
    if (str == null) {
      return;
    }
    this.fwLogger = FrameworkLogger.getLog("ENGINE", "hsqldb.db." + this.database.getNameString());
  }
  
  public void setEventLogLevel(int paramInt, boolean paramBoolean)
  {
    if ((paramInt < 0) || (paramInt > 4)) {
      throw Error.error(5556);
    }
    if (paramBoolean)
    {
      this.propSqlLogLevel = paramInt;
      this.sqlLog.setLevel(paramInt);
    }
    else
    {
      if (paramInt > 3) {
        paramInt = 3;
      }
      this.propEventLogLevel = paramInt;
      this.appLog.setLevel(paramInt);
    }
  }
  
  public void logSevereEvent(String paramString, Throwable paramThrowable)
  {
    getEventLogger();
    if (this.fwLogger != null) {
      this.fwLogger.severe(paramString, paramThrowable);
    }
    if (this.appLog != null) {
      if (paramThrowable == null) {
        this.appLog.logContext(1, paramString);
      } else {
        this.appLog.logContext(paramThrowable, paramString, 1);
      }
    }
  }
  
  public void logWarningEvent(String paramString, Throwable paramThrowable)
  {
    getEventLogger();
    if (this.fwLogger != null) {
      this.fwLogger.warning(paramString, paramThrowable);
    }
    this.appLog.logContext(paramThrowable, paramString, 1);
  }
  
  public void logInfoEvent(String paramString)
  {
    getEventLogger();
    if (this.fwLogger != null) {
      this.fwLogger.info(paramString);
    }
    this.appLog.logContext(2, paramString);
  }
  
  public void logDetailEvent(String paramString)
  {
    getEventLogger();
    if (this.fwLogger != null) {
      this.fwLogger.finest(paramString);
    }
    if (this.appLog != null) {
      this.appLog.logContext(3, paramString);
    }
  }
  
  public void logStatementEvent(Session paramSession, Statement paramStatement, Object[] paramArrayOfObject, Result paramResult, int paramInt)
  {
    if ((this.sqlLog != null) && (paramInt <= this.propSqlLogLevel))
    {
      String str1 = Long.toString(paramSession.getId());
      String str2 = paramStatement.getSQL();
      String str3 = "";
      int i = 0;
      if (this.propSqlLogLevel < 3)
      {
        if (str2.length() > 256) {
          str2 = str2.substring(0, 256);
        }
        i = 32;
      }
      if ((paramArrayOfObject != null) && (paramArrayOfObject.length > 0)) {
        str3 = RowType.convertToSQLString(paramArrayOfObject, paramStatement.getParametersMetaData().getParameterTypes(), i);
      }
      if (this.propSqlLogLevel == 4)
      {
        StringBuffer localStringBuffer = new StringBuffer(str3);
        localStringBuffer.append(' ').append('[');
        if (paramResult.isError()) {
          localStringBuffer.append(paramResult.getErrorCode());
        } else if (paramResult.isData()) {
          localStringBuffer.append(paramResult.getNavigator().getSize());
        } else if (paramResult.isUpdateCount()) {
          localStringBuffer.append(paramResult.getUpdateCount());
        }
        localStringBuffer.append(']');
        str3 = localStringBuffer.toString();
      }
      this.sqlLog.logContext(paramInt, str1, str2, str3);
    }
  }
  
  public int getSqlEventLogLevel()
  {
    return this.propSqlLogLevel;
  }
  
  public DataFileCache getCache()
  {
    if (this.log == null) {
      return null;
    }
    return this.log.getCache();
  }
  
  public boolean hasCache()
  {
    if (this.log == null) {
      return false;
    }
    return this.log.hasCache();
  }
  
  public synchronized void writeOtherStatement(Session paramSession, String paramString)
  {
    if (this.loggingEnabled) {
      this.log.writeOtherStatement(paramSession, paramString);
    }
  }
  
  public synchronized void writeInsertStatement(Session paramSession, Row paramRow, Table paramTable)
  {
    if (this.loggingEnabled) {
      this.log.writeInsertStatement(paramSession, paramRow, paramTable);
    }
  }
  
  public synchronized void writeDeleteStatement(Session paramSession, Table paramTable, Object[] paramArrayOfObject)
  {
    if (this.loggingEnabled) {
      this.log.writeDeleteStatement(paramSession, paramTable, paramArrayOfObject);
    }
  }
  
  public synchronized void writeSequenceStatement(Session paramSession, NumberSequence paramNumberSequence)
  {
    if (this.loggingEnabled) {
      this.log.writeSequenceStatement(paramSession, paramNumberSequence);
    }
  }
  
  public synchronized void writeCommitStatement(Session paramSession)
  {
    if (this.loggingEnabled) {
      this.log.writeCommitStatement(paramSession);
    }
  }
  
  public synchronized void synchLog()
  {
    if (this.loggingEnabled) {
      this.log.synchLog();
    }
  }
  
  public void checkpoint(Session paramSession, boolean paramBoolean1, boolean paramBoolean2)
  {
    if (!this.backupState.compareAndSet(0, 2)) {
      throw Error.error(457);
    }
    this.database.lobManager.lock();
    try
    {
      synchronized (this)
      {
        checkpointInternal(paramSession, paramBoolean1);
        if (paramBoolean2) {
          this.database.lobManager.deleteUnusedLobs();
        }
      }
    }
    finally
    {
      this.backupState.set(0);
      this.checkpointState.set(0);
      this.database.lobManager.unlock();
    }
  }
  
  private void checkpointInternal(Session paramSession, boolean paramBoolean)
  {
    if (this.logsStatements)
    {
      logInfoEvent("Checkpoint start");
      this.log.checkpoint(paramSession, paramBoolean);
      logInfoEvent("Checkpoint end - txts: " + this.database.txManager.getGlobalChangeTimestamp());
    }
  }
  
  public synchronized void setLogSize(int paramInt)
  {
    this.propLogSize = paramInt;
    if (this.log != null) {
      this.log.setLogSize(this.propLogSize);
    }
  }
  
  public synchronized void setLogData(boolean paramBoolean)
  {
    this.propLogData = paramBoolean;
    this.loggingEnabled = ((this.propLogData) && (!this.database.isFilesReadOnly()));
    this.loggingEnabled &= this.logsStatements;
  }
  
  public synchronized void setScriptType(int paramInt)
  {
    if (paramInt == this.propScriptFormat) {
      return;
    }
    this.propScriptFormat = paramInt;
    this.checkpointState.compareAndSet(0, 1);
  }
  
  public synchronized void setWriteDelay(int paramInt)
  {
    this.propWriteDelay = paramInt;
    if (this.log != null)
    {
      this.syncFile = (paramInt == 0);
      this.log.setWriteDelay(paramInt);
    }
  }
  
  public Crypto getCrypto()
  {
    return this.crypto;
  }
  
  public int getWriteDelay()
  {
    return this.propWriteDelay;
  }
  
  public int getLogSize()
  {
    return this.propLogSize;
  }
  
  public int getLobBlockSize()
  {
    return this.propLobBlockSize;
  }
  
  public synchronized void setIncrementBackup(boolean paramBoolean)
  {
    if (paramBoolean == this.propIncrementBackup) {
      return;
    }
    if (this.log != null)
    {
      this.log.setIncrementBackup(paramBoolean);
      if (this.log.hasCache()) {
        this.checkpointState.compareAndSet(0, 1);
      }
    }
    this.propIncrementBackup = paramBoolean;
  }
  
  public void setCacheMaxRows(int paramInt)
  {
    this.propCacheMaxRows = paramInt;
  }
  
  public int getCacheMaxRows()
  {
    return this.propCacheMaxRows;
  }
  
  public void setCacheSize(int paramInt)
  {
    this.propCacheMaxSize = (paramInt * 1024L);
  }
  
  public long getCacheSize()
  {
    return this.propCacheMaxSize;
  }
  
  public void setCacheMinReuseSize(int paramInt)
  {
    this.propMinReuse = ArrayUtil.getTwoPowerFloor(paramInt);
  }
  
  public void setDataFileScale(int paramInt)
  {
    if (this.propDataFileScale == paramInt) {
      return;
    }
    checkPower(paramInt, 10);
    if ((paramInt < 8) && (paramInt != 1)) {
      throw Error.error(5556);
    }
    if (hasCache()) {
      throw Error.error(469);
    }
    this.propDataFileScale = paramInt;
  }
  
  public void setDataFileScaleNoCheck(int paramInt)
  {
    checkPower(paramInt, 10);
    if ((paramInt < 8) && (paramInt != 1)) {
      throw Error.error(5556);
    }
    this.propDataFileScale = paramInt;
  }
  
  public int getDataFileScale()
  {
    return this.propDataFileScale;
  }
  
  public int getDataFileFactor()
  {
    return this.propLargeData ? 128 : 1;
  }
  
  public void setDataFileSpaces(boolean paramBoolean)
  {
    if (paramBoolean) {
      setDataFileSpaces(this.propDataFileScale / 16);
    } else {
      setDataFileSpaces(0);
    }
  }
  
  public void setDataFileSpaces(int paramInt)
  {
    if (this.propFileSpaceValue == paramInt) {
      return;
    }
    if (paramInt != 0) {
      checkPower(paramInt, 6);
    }
    if (paramInt > this.propDataFileScale / 16) {
      paramInt = this.propDataFileScale / 16;
    }
    this.propFileSpaceValue = paramInt;
    if (hasCache())
    {
      DataFileCache localDataFileCache = getCache();
      boolean bool = localDataFileCache.setDataSpaceManager();
      if (!bool) {
        return;
      }
      this.database.persistentStoreCollection.setNewTableSpaces();
    }
  }
  
  public int getDataFileSpaces()
  {
    return this.propFileSpaceValue;
  }
  
  public long getFilesTimestamp()
  {
    return this.propFileTimestamp;
  }
  
  public void setFilesTimestamp(long paramLong)
  {
    this.propFileTimestamp = paramLong;
  }
  
  public void setLobFileScale(int paramInt)
  {
    if (this.propLobBlockSize == paramInt * 1024) {
      return;
    }
    checkPower(paramInt, 5);
    if (this.database.lobManager.getLobCount() > 0) {
      throw Error.error(469);
    }
    this.propLobBlockSize = (paramInt * 1024);
    this.database.lobManager.close();
    this.database.lobManager.open();
  }
  
  public void setLobFileScaleNoCheck(int paramInt)
  {
    checkPower(paramInt, 5);
    this.propLobBlockSize = (paramInt * 1024);
  }
  
  public int getLobFileScale()
  {
    return this.propLobBlockSize / 1024;
  }
  
  public void setLobFileCompressed(boolean paramBoolean)
  {
    if (this.propCompressLobs == paramBoolean) {
      return;
    }
    if (this.database.lobManager.getLobCount() > 0) {
      throw Error.error(469);
    }
    this.propCompressLobs = paramBoolean;
    this.database.lobManager.close();
    this.database.lobManager.open();
  }
  
  public void setLobFileCompressedNoCheck(boolean paramBoolean)
  {
    this.propCompressLobs = paramBoolean;
  }
  
  public void setDefagLimit(int paramInt)
  {
    this.propCacheDefragLimit = paramInt;
  }
  
  public int getDefragLimit()
  {
    return this.propCacheDefragLimit;
  }
  
  public void setDefaultTextTableProperties(String paramString, HsqlProperties paramHsqlProperties)
  {
    paramHsqlProperties.setProperty("check_props", true);
    this.database.getProperties().setURLProperties(paramHsqlProperties);
    this.propTextSourceDefault = paramString;
  }
  
  public void setNioDataFile(boolean paramBoolean)
  {
    this.propNioDataFile = paramBoolean;
  }
  
  public void setNioMaxSize(int paramInt)
  {
    if (paramInt < 8) {
      throw Error.error(5556);
    }
    if ((!ArrayUtil.isTwoPower(paramInt, 10)) && ((paramInt < 1024) || (paramInt % 512 != 0))) {
      throw Error.error(5556);
    }
    this.propNioMaxSize = (paramInt * 1024L * 1024L);
  }
  
  public FileAccess getFileAccess()
  {
    return this.fileAccess;
  }
  
  public boolean isStoredFileAccess()
  {
    return this.isStoredFileAccess;
  }
  
  public boolean isNewStoredFileAccess()
  {
    return this.isNewStoredFileAccess;
  }
  
  public boolean isFileDatabase()
  {
    return this.propIsFileDatabase;
  }
  
  public String getTempDirectoryPath()
  {
    return this.tempDirectoryPath;
  }
  
  static void checkPower(int paramInt1, int paramInt2)
  {
    if (!ArrayUtil.isTwoPower(paramInt1, paramInt2)) {
      throw Error.error(5556);
    }
  }
  
  public void setCheckpointRequired()
  {
    this.checkpointState.compareAndSet(0, 1);
  }
  
  public boolean needsCheckpointReset()
  {
    return this.checkpointState.compareAndSet(1, 2);
  }
  
  public boolean hasLockFile()
  {
    return this.lockFile != null;
  }
  
  public void acquireLock(String paramString)
  {
    if (this.lockFile != null) {
      return;
    }
    this.lockFile = LockFile.newLockFileLock(paramString);
  }
  
  public void releaseLock()
  {
    try
    {
      if (this.lockFile != null) {
        this.lockFile.tryRelease();
      }
    }
    catch (Exception localException) {}
    this.lockFile = null;
  }
  
  public PersistentStore newStore(Session paramSession, PersistentStoreCollection paramPersistentStoreCollection, TableBase paramTableBase)
  {
    switch (paramTableBase.getTableType())
    {
    case 5: 
      DataFileCache localDataFileCache = getCache();
      if (localDataFileCache != null) {
        return new RowStoreAVLDisk(localDataFileCache, (Table)paramTableBase);
      }
      break;
    case 4: 
    case 12: 
      return new RowStoreAVLMemory((Table)paramTableBase);
    case 7: 
      return new RowStoreAVLDiskData((Table)paramTableBase);
    case 1: 
      return new RowStoreAVLHybridExtended(paramSession, paramTableBase, false);
    case 3: 
      return new RowStoreAVLHybridExtended(paramSession, paramTableBase, true);
    case 13: 
      return new RowStoreDataChange(paramSession, paramPersistentStoreCollection, paramTableBase);
    case 2: 
    case 8: 
    case 9: 
    case 10: 
    case 11: 
      if (paramSession == null) {
        return null;
      }
      return new RowStoreAVLHybrid(paramSession, paramTableBase, true);
    }
    throw Error.runtimeError(201, "Logger");
  }
  
  public Index newIndex(HsqlNameManager.HsqlName paramHsqlName, long paramLong, TableBase paramTableBase, int[] paramArrayOfInt, boolean[] paramArrayOfBoolean1, boolean[] paramArrayOfBoolean2, Type[] paramArrayOfType, boolean paramBoolean1, boolean paramBoolean2, boolean paramBoolean3, boolean paramBoolean4)
  {
    switch (paramTableBase.getTableType())
    {
    case 1: 
    case 4: 
    case 12: 
      return new IndexAVLMemory(paramHsqlName, paramLong, paramTableBase, paramArrayOfInt, paramArrayOfBoolean1, paramArrayOfBoolean2, paramArrayOfType, paramBoolean1, paramBoolean2, paramBoolean3, paramBoolean4);
    case 2: 
    case 3: 
    case 5: 
    case 7: 
    case 8: 
    case 9: 
    case 10: 
    case 11: 
    case 13: 
      return new IndexAVL(paramHsqlName, paramLong, paramTableBase, paramArrayOfInt, paramArrayOfBoolean1, paramArrayOfBoolean2, paramArrayOfType, paramBoolean1, paramBoolean2, paramBoolean3, paramBoolean4);
    }
    throw Error.runtimeError(201, "Logger");
  }
  
  public String getValueStringForProperty(String paramString)
  {
    String str = "";
    if ("hsqldb.tx".equals(paramString))
    {
      switch (this.database.txManager.getTransactionControl())
      {
      case 2: 
        str = "MVCC".toLowerCase();
        break;
      case 1: 
        str = "MVLOCKS".toLowerCase();
        break;
      case 0: 
        str = "LOCKS".toLowerCase();
      }
      return str;
    }
    if ("hsqldb.tx_level".equals(paramString))
    {
      switch (this.database.defaultIsolationLevel)
      {
      case 2: 
        str = ("READ" + ' ' + "COMMITTED").toLowerCase();
        break;
      case 8: 
        str = "SERIALIZABLE".toLowerCase();
      }
      return str;
    }
    if ("hsqldb.applog".equals(paramString)) {
      return String.valueOf(this.appLog.getLevel());
    }
    if ("hsqldb.sqllog".equals(paramString)) {
      return String.valueOf(this.sqlLog.getLevel());
    }
    if ("hsqldb.lob_file_scale".equals(paramString)) {
      return String.valueOf(this.propLobBlockSize / 1024);
    }
    if ("hsqldb.lob_compressed".equals(paramString)) {
      return String.valueOf(this.propCompressLobs);
    }
    if ("hsqldb.cache_file_scale".equals(paramString)) {
      return String.valueOf(this.propDataFileScale);
    }
    if ("hsqldb.cache_free_count".equals(paramString)) {
      return String.valueOf(this.propMaxFreeBlocks);
    }
    if ("hsqldb.cache_rows".equals(paramString)) {
      return String.valueOf(this.propCacheMaxRows);
    }
    if ("hsqldb.cache_size".equals(paramString)) {
      return String.valueOf(this.propCacheMaxSize / 1024L);
    }
    if ("hsqldb.default_table_type".equals(paramString)) {
      return this.database.schemaManager.getDefaultTableType() == 5 ? "CACHED" : "MEMORY";
    }
    if ("hsqldb.defrag_limit".equals(paramString)) {
      return String.valueOf(this.propCacheDefragLimit);
    }
    if ("hsqldb.files_space".equals(paramString)) {
      return String.valueOf(this.propFileSpaceValue);
    }
    if ("files_readonly".equals(paramString)) {
      return this.database.databaseProperties.getPropertyString("files_readonly");
    }
    if ("hsqldb.inc_backup".equals(paramString)) {
      return String.valueOf(this.propIncrementBackup);
    }
    if ("hsqldb.large_data".equals(paramString)) {
      return String.valueOf(this.propLargeData);
    }
    if ("hsqldb.large_data".equals(paramString)) {
      return String.valueOf(this.propLargeData);
    }
    if ("hsqldb.lock_file".equals(paramString)) {
      return this.database.databaseProperties.getPropertyString("hsqldb.lock_file");
    }
    if ("hsqldb.log_data".equals(paramString)) {
      return String.valueOf(this.propLogData);
    }
    if ("hsqldb.log_size".equals(paramString)) {
      return String.valueOf(this.propLogSize);
    }
    if ("hsqldb.nio_data_file".equals(paramString)) {
      return String.valueOf(this.propNioDataFile);
    }
    if ("hsqldb.nio_max_size".equals(paramString)) {
      return String.valueOf(this.propNioMaxSize / 1048576L);
    }
    if ("hsqldb.script_format".equals(paramString)) {
      return org.hsqldb.scriptio.ScriptWriterBase.LIST_SCRIPT_FORMATS[this.propScriptFormat].toLowerCase();
    }
    if ("hsqldb.temp_directory".equals(paramString)) {
      return this.tempDirectoryPath;
    }
    if ("hsqldb.tx_conflict_rollback".equals(paramString)) {
      return String.valueOf(this.database.txConflictRollback);
    }
    if ("hsqldb.result_max_memory_rows".equals(paramString)) {
      return String.valueOf(this.database.getResultMaxMemoryRows());
    }
    if ("hsqldb.write_delay".equals(paramString)) {
      return String.valueOf(this.propWriteDelay != 0);
    }
    if ("hsqldb.write_delay_millis".equals(paramString)) {
      return String.valueOf(this.propWriteDelay);
    }
    if ("hsqldb.digest".equals(paramString)) {
      return this.database.granteeManager.getDigestAlgo();
    }
    if ("sql.avg_scale".equals(paramString)) {
      return String.valueOf(this.database.sqlAvgScale);
    }
    if ("sql.char_literal".equals(paramString)) {
      return String.valueOf(this.database.sqlCharLiteral);
    }
    if ("sql.concat_nulls".equals(paramString)) {
      return String.valueOf(this.database.sqlConcatNulls);
    }
    if ("sql.convert_trunc".equals(paramString)) {
      return String.valueOf(this.database.sqlConvertTruncate);
    }
    if ("sql.double_nan".equals(paramString)) {
      return String.valueOf(this.database.sqlDoubleNaN);
    }
    if ("sql.enforce_names".equals(paramString)) {
      return String.valueOf(this.database.sqlEnforceNames);
    }
    if ("sql.enforce_refs".equals(paramString)) {
      return String.valueOf(this.database.sqlEnforceRefs);
    }
    if ("sql.enforce_size".equals(paramString)) {
      return String.valueOf(this.database.sqlEnforceSize);
    }
    if ("sql.enforce_tdc_delete".equals(paramString)) {
      return String.valueOf(this.database.sqlEnforceTDCD);
    }
    if ("sql.enforce_tdc_update".equals(paramString)) {
      return String.valueOf(this.database.sqlEnforceTDCU);
    }
    if ("sql.enforce_types".equals(paramString)) {
      return String.valueOf(this.database.sqlEnforceTypes);
    }
    if ("sql.ignore_case".equals(paramString)) {
      return String.valueOf(this.database.sqlIgnoreCase);
    }
    if ("sql.longvar_is_lob".equals(paramString)) {
      return String.valueOf(this.database.sqlLongvarIsLob);
    }
    if ("sql.nulls_first".equals(paramString)) {
      return String.valueOf(this.database.sqlNullsFirst);
    }
    if ("sql.nulls_order".equals(paramString)) {
      return String.valueOf(this.database.sqlNullsOrder);
    }
    if ("sql.syntax_db2".equals(paramString)) {
      return String.valueOf(this.database.sqlSyntaxDb2);
    }
    if ("sql.syntax_mss".equals(paramString)) {
      return String.valueOf(this.database.sqlSyntaxMss);
    }
    if ("sql.syntax_mys".equals(paramString)) {
      return String.valueOf(this.database.sqlSyntaxMys);
    }
    if ("sql.syntax_ora".equals(paramString)) {
      return String.valueOf(this.database.sqlSyntaxOra);
    }
    if ("sql.syntax_pgs".equals(paramString)) {
      return String.valueOf(this.database.sqlSyntaxPgs);
    }
    if ("sql.ref_integrity".equals(paramString)) {
      return String.valueOf(this.database.isReferentialIntegrity());
    }
    if ("sql.regular_names".equals(paramString)) {
      return String.valueOf(this.database.sqlRegularNames);
    }
    if ("sql.unique_nulls".equals(paramString)) {
      return String.valueOf(this.database.sqlUniqueNulls);
    }
    if ("sql.live_object".equals(paramString)) {
      return String.valueOf(this.database.sqlLiveObject);
    }
    if ("jdbc.translate_tti_types".equals(paramString)) {
      return String.valueOf(this.database.sqlTranslateTTI);
    }
    if ("hsqldb.min_reuse".equals(paramString)) {
      return String.valueOf(this.propMinReuse);
    }
    return null;
  }
  
  public String[] getPropertiesSQL(boolean paramBoolean)
  {
    HsqlArrayList localHsqlArrayList = new HsqlArrayList();
    StringBuffer localStringBuffer = new StringBuffer();
    localStringBuffer.append("SET DATABASE ").append("UNIQUE").append(' ');
    localStringBuffer.append("NAME").append(' ').append(this.database.getNameString());
    localHsqlArrayList.add(localStringBuffer.toString());
    localStringBuffer.setLength(0);
    localStringBuffer.append("SET DATABASE ").append("GC").append(' ');
    localStringBuffer.append(this.propGC);
    localHsqlArrayList.add(localStringBuffer.toString());
    localStringBuffer.setLength(0);
    localStringBuffer.append("SET DATABASE ").append("DEFAULT").append(' ');
    localStringBuffer.append("RESULT").append(' ').append("MEMORY");
    localStringBuffer.append(' ').append("ROWS").append(' ');
    localStringBuffer.append(this.database.getResultMaxMemoryRows());
    localHsqlArrayList.add(localStringBuffer.toString());
    localStringBuffer.setLength(0);
    localStringBuffer.append("SET DATABASE ").append("EVENT").append(' ');
    localStringBuffer.append("LOG").append(' ').append("LEVEL");
    localStringBuffer.append(' ').append(this.propEventLogLevel);
    localHsqlArrayList.add(localStringBuffer.toString());
    localStringBuffer.setLength(0);
    if (this.propSqlLogLevel != 0)
    {
      localStringBuffer.append("SET DATABASE ").append("EVENT").append(' ');
      localStringBuffer.append("LOG").append(' ').append("SQL");
      localStringBuffer.append(' ').append("LEVEL");
      localStringBuffer.append(' ').append(this.propEventLogLevel);
      localHsqlArrayList.add(localStringBuffer.toString());
      localStringBuffer.setLength(0);
    }
    localStringBuffer.append("SET DATABASE ").append("TRANSACTION");
    localStringBuffer.append(' ').append("CONTROL").append(' ');
    switch (this.database.txManager.getTransactionControl())
    {
    case 2: 
      localStringBuffer.append("MVCC");
      break;
    case 1: 
      localStringBuffer.append("MVLOCKS");
      break;
    case 0: 
      localStringBuffer.append("LOCKS");
    }
    localHsqlArrayList.add(localStringBuffer.toString());
    localStringBuffer.setLength(0);
    localStringBuffer.append("SET DATABASE ").append("DEFAULT").append(' ');
    localStringBuffer.append("ISOLATION").append(' ').append("LEVEL");
    localStringBuffer.append(' ');
    switch (this.database.defaultIsolationLevel)
    {
    case 2: 
      localStringBuffer.append("READ").append(' ').append("COMMITTED");
      break;
    case 8: 
      localStringBuffer.append("SERIALIZABLE");
    }
    localHsqlArrayList.add(localStringBuffer.toString());
    localStringBuffer.setLength(0);
    localStringBuffer.append("SET DATABASE ").append("TRANSACTION");
    localStringBuffer.append(' ').append("ROLLBACK").append(' ');
    localStringBuffer.append("ON").append(' ');
    localStringBuffer.append("CONFLICT").append(' ');
    localStringBuffer.append(this.database.txConflictRollback ? "TRUE" : "FALSE");
    localHsqlArrayList.add(localStringBuffer.toString());
    localStringBuffer.setLength(0);
    localStringBuffer.append("SET DATABASE ").append("TEXT").append(' ');
    localStringBuffer.append("TABLE").append(' ').append("DEFAULTS");
    localStringBuffer.append(' ').append('\'');
    localStringBuffer.append(this.propTextSourceDefault).append('\'');
    localHsqlArrayList.add(localStringBuffer.toString());
    localStringBuffer.setLength(0);
    String str = this.database.getProperties().getStringPropertyDefault("hsqldb.digest");
    if (!str.equals(this.database.granteeManager.getDigestAlgo()))
    {
      localStringBuffer.append("SET DATABASE ").append(' ').append("PASSWORD");
      localStringBuffer.append(' ').append("DIGEST").append(' ').append('\'');
      localStringBuffer.append(this.database.granteeManager.getDigestAlgo()).append('\'');
      localHsqlArrayList.add(localStringBuffer.toString());
      localStringBuffer.setLength(0);
    }
    if (this.database.schemaManager.getDefaultTableType() == 5) {
      localHsqlArrayList.add("SET DATABASE DEFAULT TABLE TYPE CACHED");
    }
    localStringBuffer.append("SET DATABASE ").append("SQL").append(' ');
    localStringBuffer.append("NAMES").append(' ');
    localStringBuffer.append(this.database.sqlEnforceNames ? "TRUE" : "FALSE");
    localHsqlArrayList.add(localStringBuffer.toString());
    localStringBuffer.setLength(0);
    if (!this.database.sqlRegularNames)
    {
      localStringBuffer.append("SET DATABASE ").append("SQL").append(' ');
      localStringBuffer.append("REGULAR").append(' ');
      localStringBuffer.append("NAMES").append(' ');
      localStringBuffer.append(this.database.sqlRegularNames ? "TRUE" : "FALSE");
      localHsqlArrayList.add(localStringBuffer.toString());
      localStringBuffer.setLength(0);
    }
    localStringBuffer.append("SET DATABASE ").append("SQL").append(' ');
    localStringBuffer.append("REFERENCES").append(' ');
    localStringBuffer.append(this.database.sqlEnforceRefs ? "TRUE" : "FALSE");
    localHsqlArrayList.add(localStringBuffer.toString());
    localStringBuffer.setLength(0);
    localStringBuffer.append("SET DATABASE ").append("SQL").append(' ');
    localStringBuffer.append("SIZE").append(' ');
    localStringBuffer.append(this.database.sqlEnforceSize ? "TRUE" : "FALSE");
    localHsqlArrayList.add(localStringBuffer.toString());
    localStringBuffer.setLength(0);
    localStringBuffer.append("SET DATABASE ").append("SQL").append(' ');
    localStringBuffer.append("TYPES").append(' ');
    localStringBuffer.append(this.database.sqlEnforceTypes ? "TRUE" : "FALSE");
    localHsqlArrayList.add(localStringBuffer.toString());
    localStringBuffer.setLength(0);
    localStringBuffer.append("SET DATABASE ").append("SQL").append(' ');
    localStringBuffer.append("TDC").append(' ');
    localStringBuffer.append("DELETE").append(' ');
    localStringBuffer.append(this.database.sqlEnforceTDCD ? "TRUE" : "FALSE");
    localHsqlArrayList.add(localStringBuffer.toString());
    localStringBuffer.setLength(0);
    localStringBuffer.append("SET DATABASE ").append("SQL").append(' ');
    localStringBuffer.append("TDC").append(' ');
    localStringBuffer.append("UPDATE").append(' ');
    localStringBuffer.append(this.database.sqlEnforceTDCU ? "TRUE" : "FALSE");
    localHsqlArrayList.add(localStringBuffer.toString());
    localStringBuffer.setLength(0);
    localStringBuffer.append("SET DATABASE ").append("SQL").append(' ');
    localStringBuffer.append("TRANSLATE").append(' ').append("TTI");
    localStringBuffer.append(' ').append("TYPES").append(' ');
    localStringBuffer.append(this.database.sqlTranslateTTI ? "TRUE" : "FALSE");
    if (!this.database.sqlCharLiteral)
    {
      localHsqlArrayList.add(localStringBuffer.toString());
      localStringBuffer.setLength(0);
      localStringBuffer.append("SET DATABASE ").append("SQL").append(' ');
      localStringBuffer.append("CHARACTER").append(' ');
      localStringBuffer.append("LITERAL").append(' ');
      localStringBuffer.append(this.database.sqlCharLiteral ? "TRUE" : "FALSE");
    }
    localHsqlArrayList.add(localStringBuffer.toString());
    localHsqlArrayList.add(localStringBuffer.toString());
    localStringBuffer.setLength(0);
    localStringBuffer.append("SET DATABASE ").append("SQL").append(' ');
    localStringBuffer.append("CONCAT").append(' ');
    localStringBuffer.append("NULLS").append(' ');
    localStringBuffer.append(this.database.sqlConcatNulls ? "TRUE" : "FALSE");
    localHsqlArrayList.add(localStringBuffer.toString());
    if (!this.database.sqlNullsFirst)
    {
      localStringBuffer.setLength(0);
      localStringBuffer.append("SET DATABASE ").append("SQL").append(' ');
      localStringBuffer.append("NULLS").append(' ');
      localStringBuffer.append("FIRST").append(' ');
      localStringBuffer.append(this.database.sqlNullsFirst ? "TRUE" : "FALSE");
      localHsqlArrayList.add(localStringBuffer.toString());
    }
    if (!this.database.sqlNullsOrder)
    {
      localStringBuffer.setLength(0);
      localStringBuffer.append("SET DATABASE ").append("SQL").append(' ');
      localStringBuffer.append("NULLS").append(' ');
      localStringBuffer.append("ORDER").append(' ');
      localStringBuffer.append(this.database.sqlNullsOrder ? "TRUE" : "FALSE");
      localHsqlArrayList.add(localStringBuffer.toString());
    }
    localStringBuffer.setLength(0);
    localStringBuffer.append("SET DATABASE ").append("SQL").append(' ');
    localStringBuffer.append("UNIQUE").append(' ');
    localStringBuffer.append("NULLS").append(' ');
    localStringBuffer.append(this.database.sqlUniqueNulls ? "TRUE" : "FALSE");
    localHsqlArrayList.add(localStringBuffer.toString());
    localStringBuffer.setLength(0);
    localStringBuffer.append("SET DATABASE ").append("SQL").append(' ');
    localStringBuffer.append("CONVERT").append(' ');
    localStringBuffer.append("TRUNCATE").append(' ');
    localStringBuffer.append(this.database.sqlConvertTruncate ? "TRUE" : "FALSE");
    localHsqlArrayList.add(localStringBuffer.toString());
    localStringBuffer.setLength(0);
    localStringBuffer.append("SET DATABASE ").append("SQL").append(' ');
    localStringBuffer.append("AVG").append(' ');
    localStringBuffer.append("SCALE").append(' ');
    localStringBuffer.append(this.database.sqlAvgScale);
    localHsqlArrayList.add(localStringBuffer.toString());
    localStringBuffer.setLength(0);
    localStringBuffer.append("SET DATABASE ").append("SQL").append(' ');
    localStringBuffer.append("DOUBLE").append(' ');
    localStringBuffer.append("NAN").append(' ');
    localStringBuffer.append(this.database.sqlDoubleNaN ? "TRUE" : "FALSE");
    localHsqlArrayList.add(localStringBuffer.toString());
    localStringBuffer.setLength(0);
    if (this.database.sqlLongvarIsLob)
    {
      localStringBuffer.append("SET DATABASE ").append("SQL").append(' ');
      localStringBuffer.append("LONGVAR").append(' ');
      localStringBuffer.append("IS").append(' ');
      localStringBuffer.append("LOB").append(' ');
      localStringBuffer.append(this.database.sqlLongvarIsLob ? "TRUE" : "FALSE");
      localHsqlArrayList.add(localStringBuffer.toString());
      localStringBuffer.setLength(0);
    }
    if (this.database.sqlIgnoreCase)
    {
      localStringBuffer.append("SET DATABASE ").append("SQL").append(' ');
      localStringBuffer.append("IGNORECASE").append(' ');
      localStringBuffer.append(this.database.sqlIgnoreCase ? "TRUE" : "FALSE");
      localHsqlArrayList.add(localStringBuffer.toString());
      localStringBuffer.setLength(0);
    }
    if (this.database.sqlSyntaxDb2)
    {
      localStringBuffer.append("SET DATABASE ").append("SQL").append(' ');
      localStringBuffer.append("SYNTAX").append(' ');
      localStringBuffer.append("DB2").append(' ');
      localStringBuffer.append(this.database.sqlSyntaxDb2 ? "TRUE" : "FALSE");
      localHsqlArrayList.add(localStringBuffer.toString());
      localStringBuffer.setLength(0);
    }
    if (this.database.sqlSyntaxMss)
    {
      localStringBuffer.append("SET DATABASE ").append("SQL").append(' ');
      localStringBuffer.append("SYNTAX").append(' ');
      localStringBuffer.append("MSS").append(' ');
      localStringBuffer.append(this.database.sqlSyntaxMss ? "TRUE" : "FALSE");
      localHsqlArrayList.add(localStringBuffer.toString());
      localStringBuffer.setLength(0);
    }
    if (this.database.sqlSyntaxMys)
    {
      localStringBuffer.append("SET DATABASE ").append("SQL").append(' ');
      localStringBuffer.append("SYNTAX").append(' ');
      localStringBuffer.append("MYS").append(' ');
      localStringBuffer.append(this.database.sqlSyntaxMys ? "TRUE" : "FALSE");
      localHsqlArrayList.add(localStringBuffer.toString());
      localStringBuffer.setLength(0);
    }
    if (this.database.sqlSyntaxOra)
    {
      localStringBuffer.append("SET DATABASE ").append("SQL").append(' ');
      localStringBuffer.append("SYNTAX").append(' ');
      localStringBuffer.append("ORA").append(' ');
      localStringBuffer.append(this.database.sqlSyntaxOra ? "TRUE" : "FALSE");
      localHsqlArrayList.add(localStringBuffer.toString());
      localStringBuffer.setLength(0);
    }
    if (this.database.sqlSyntaxPgs)
    {
      localStringBuffer.append("SET DATABASE ").append("SQL").append(' ');
      localStringBuffer.append("SYNTAX").append(' ');
      localStringBuffer.append("PGS").append(' ');
      localStringBuffer.append(this.database.sqlSyntaxPgs ? "TRUE" : "FALSE");
      localHsqlArrayList.add(localStringBuffer.toString());
      localStringBuffer.setLength(0);
    }
    int i = this.propWriteDelay;
    int j = (i > 0) && (i < 1000) ? 1 : 0;
    if (j != 0)
    {
      if (i < 20) {
        i = 20;
      }
    }
    else {
      i /= 1000;
    }
    localStringBuffer.append("SET FILES ").append("WRITE").append(' ');
    localStringBuffer.append("DELAY").append(' ').append(i);
    if (j != 0) {
      localStringBuffer.append(' ').append("MILLIS");
    }
    localHsqlArrayList.add(localStringBuffer.toString());
    localStringBuffer.setLength(0);
    localStringBuffer.append("SET FILES ").append("BACKUP");
    localStringBuffer.append(' ').append("INCREMENT").append(' ');
    localStringBuffer.append(this.propIncrementBackup ? "TRUE" : "FALSE");
    localHsqlArrayList.add(localStringBuffer.toString());
    localStringBuffer.setLength(0);
    localStringBuffer.append("SET FILES ").append("CACHE");
    localStringBuffer.append(' ').append("SIZE").append(' ');
    localStringBuffer.append(this.propCacheMaxSize / 1024L);
    localHsqlArrayList.add(localStringBuffer.toString());
    localStringBuffer.setLength(0);
    localStringBuffer.append("SET FILES ").append("CACHE");
    localStringBuffer.append(' ').append("ROWS").append(' ');
    localStringBuffer.append(this.propCacheMaxRows);
    localHsqlArrayList.add(localStringBuffer.toString());
    int k = this.propDataFileScale;
    if ((!paramBoolean) && (k < 32)) {
      k = 32;
    }
    localStringBuffer.setLength(0);
    localStringBuffer.append("SET FILES ").append("SCALE");
    localStringBuffer.append(' ').append(k);
    localHsqlArrayList.add(localStringBuffer.toString());
    localStringBuffer.setLength(0);
    localStringBuffer.append("SET FILES ").append("LOB").append(' ').append("SCALE");
    localStringBuffer.append(' ').append(getLobFileScale());
    localHsqlArrayList.add(localStringBuffer.toString());
    localStringBuffer.setLength(0);
    if (this.propCompressLobs)
    {
      localStringBuffer.append("SET FILES ").append("LOB").append(' ').append("COMPRESSED");
      localStringBuffer.append(' ').append(this.propCompressLobs ? "TRUE" : "FALSE");
      localHsqlArrayList.add(localStringBuffer.toString());
      localStringBuffer.setLength(0);
    }
    localStringBuffer.append("SET FILES ").append("DEFRAG");
    localStringBuffer.append(' ').append(this.propCacheDefragLimit);
    localHsqlArrayList.add(localStringBuffer.toString());
    localStringBuffer.setLength(0);
    localStringBuffer.append("SET FILES ").append("NIO");
    localStringBuffer.append(' ').append(this.propNioDataFile ? "TRUE" : "FALSE");
    localHsqlArrayList.add(localStringBuffer.toString());
    localStringBuffer.setLength(0);
    localStringBuffer.append("SET FILES ").append("NIO").append(' ').append("SIZE");
    localStringBuffer.append(' ').append(this.propNioMaxSize / 1048576L);
    localHsqlArrayList.add(localStringBuffer.toString());
    localStringBuffer.setLength(0);
    localStringBuffer.append("SET FILES ").append("LOG").append(' ');
    localStringBuffer.append(this.propLogData ? "TRUE" : "FALSE");
    localHsqlArrayList.add(localStringBuffer.toString());
    localStringBuffer.setLength(0);
    localStringBuffer.append("SET FILES ").append("LOG").append(' ');
    localStringBuffer.append("SIZE").append(' ').append(this.propLogSize);
    localHsqlArrayList.add(localStringBuffer.toString());
    localStringBuffer.setLength(0);
    if (this.propFileTimestamp != 0L)
    {
      localStringBuffer.append("SET FILES ").append("CHECK").append(' ');
      localStringBuffer.append(this.propFileTimestamp);
      localHsqlArrayList.add(localStringBuffer.toString());
      localStringBuffer.setLength(0);
    }
    if (this.propFileSpaceValue != 0)
    {
      localStringBuffer.append("SET FILES ").append("SPACE").append(' ');
      localStringBuffer.append(this.propFileSpaceValue);
      localHsqlArrayList.add(localStringBuffer.toString());
      localStringBuffer.setLength(0);
    }
    String[] arrayOfString = new String[localHsqlArrayList.size()];
    localHsqlArrayList.toArray(arrayOfString);
    return arrayOfString;
  }
  
  public void backup(String paramString, boolean paramBoolean1, boolean paramBoolean2, boolean paramBoolean3, boolean paramBoolean4)
  {
    if (!this.backupState.compareAndSet(0, 1)) {
      throw Error.error(470, "backup in progress");
    }
    if (paramBoolean2)
    {
      this.database.lobManager.lock();
      try
      {
        synchronized (this)
        {
          backupInternal(paramString, paramBoolean1, paramBoolean2, paramBoolean3, paramBoolean4);
        }
      }
      finally
      {
        this.backupState.set(0);
        this.database.lobManager.unlock();
      }
    }
    else
    {
      try
      {
        backupInternal(paramString, paramBoolean1, paramBoolean2, paramBoolean3, paramBoolean4);
      }
      finally
      {
        this.backupState.set(0);
      }
    }
  }
  
  void backupInternal(String paramString, boolean paramBoolean1, boolean paramBoolean2, boolean paramBoolean3, boolean paramBoolean4)
  {
    String str1 = null;
    String str2 = this.database.getPath();
    String str3 = new File(str2).getName();
    int i = paramString.charAt(paramString.length() - 1);
    int j = (i == 47) || (i == runtimeFileDelim) ? 1 : 0;
    File localFile;
    Object localObject1;
    if (paramBoolean4)
    {
      if (j == 0) {
        throw Error.error(null, 462, 0, new String[] { "", "/" });
      }
      paramString = getSecurePath(paramString, true, false);
      if (paramString == null) {
        throw Error.error(470, "access to directory denied");
      }
      localFile = new File(paramString);
      localFile.mkdirs();
      localObject1 = FileUtil.getDatabaseMainFileList(paramString + str3);
      if ((localObject1 == null) || (localObject1.length != 0)) {
        throw Error.error(470, "files exist in directory");
      }
    }
    else
    {
      localObject1 = paramBoolean3 ? ".tar.gz" : ".tar";
      if (j != 0) {
        localFile = new File(paramString.substring(0, paramString.length() - 1), str3 + '-' + this.backupFileFormat.format(new Date()) + (String)localObject1);
      } else {
        localFile = new File(paramString);
      }
      boolean bool = (localFile.getName().endsWith(".tar.gz")) || (localFile.getName().endsWith(".tgz"));
      if ((!bool) && (!localFile.getName().endsWith(".tar"))) {
        throw Error.error(null, 462, 0, new String[] { localFile.getName(), ".tar, .tar.gz, .tgz" });
      }
      if (paramBoolean3 != bool) {
        throw Error.error(null, 463, 0, new Object[] { Boolean.valueOf(paramBoolean3), localFile.getName() });
      }
      if (localFile.exists()) {
        throw Error.error(470, "file exists :" + localFile.getName());
      }
    }
    if (paramBoolean2) {
      this.log.checkpointClose();
    }
    try
    {
      logInfoEvent("Initiating backup of instance '" + str3 + "'");
      Object localObject2;
      if (paramBoolean1)
      {
        localObject1 = getTempDirectoryPath();
        if (localObject1 == null) {
          return;
        }
        localObject1 = (String)localObject1 + "/" + new File(this.database.getPath()).getName();
        str1 = (String)localObject1 + ".script";
        localObject2 = new ScriptWriterText(this.database, str1, true, true, true);
        ((ScriptWriterText)localObject2).writeAll();
        ((ScriptWriterText)localObject2).close();
        this.backup = new DbBackup(localFile, (String)localObject1, true);
        this.backup.write();
      }
      else
      {
        this.backup = new DbBackup(localFile, str2);
        this.backup.setAbortUponModify(false);
        if (!paramBoolean2)
        {
          localObject2 = null;
          if (hasCache())
          {
            DataFileCache localDataFileCache = getCache();
            RAShadowFile localRAShadowFile = localDataFileCache.getShadowFile();
            if (localRAShadowFile == null)
            {
              this.backup.setFileIgnore(".data");
            }
            else
            {
              localObject2 = new File(localDataFileCache.dataFileName);
              localObject1 = new InputStreamWrapper(new FileInputStream((File)localObject2));
              ((InputStreamWrapper)localObject1).setSizeLimit(localDataFileCache.fileStartFreePosition);
              this.backup.setStream(".data", (InputStreamInterface)localObject1);
              InputStreamInterface localInputStreamInterface = localRAShadowFile.getInputStream();
              this.backup.setStream(".backup", localInputStreamInterface);
            }
          }
          localObject2 = new File(this.log.getLogFileName());
          long l = ((File)localObject2).length();
          if (l == 0L)
          {
            this.backup.setFileIgnore(".log");
          }
          else
          {
            localObject1 = new InputStreamWrapper(new FileInputStream((File)localObject2));
            ((InputStreamWrapper)localObject1).setSizeLimit(l);
            this.backup.setStream(".log", (InputStreamInterface)localObject1);
          }
        }
        if (paramBoolean4) {
          this.backup.writeAsFiles();
        } else {
          this.backup.write();
        }
      }
      logInfoEvent("Successfully backed up instance '" + str3 + "' to '" + paramString + "'");
    }
    catch (IOException localIOException)
    {
      throw Error.error(452, localIOException.toString());
    }
    catch (TarMalformatException localTarMalformatException)
    {
      throw Error.error(452, localTarMalformatException.toString());
    }
    finally
    {
      if (str1 != null) {
        FileUtil.getFileUtil().delete(str1);
      }
      if (paramBoolean2) {
        this.log.checkpointReopen();
      }
    }
  }
  
  public String getSecurePath(String paramString, boolean paramBoolean1, boolean paramBoolean2)
  {
    if (this.database.getType() == DatabaseType.DB_RES)
    {
      if (paramBoolean2) {
        return paramString;
      }
      return null;
    }
    if (this.database.getType() == DatabaseType.DB_MEM)
    {
      if (this.propTextAllowFullPath) {
        return paramString;
      }
      return null;
    }
    if ((paramString.startsWith("/")) || (paramString.startsWith("\\")) || (paramString.indexOf(":") > -1))
    {
      if ((paramBoolean1) || (this.propTextAllowFullPath)) {
        return paramString;
      }
      return null;
    }
    if ((paramString.indexOf("..") > -1) && (!paramBoolean1) && (!this.propTextAllowFullPath)) {
      return null;
    }
    String str = new File(new File(this.database.getPath() + ".properties").getAbsolutePath()).getParent();
    if (str != null) {
      paramString = str + File.separator + paramString;
    }
    return paramString;
  }
  
  public boolean isNewDatabase()
  {
    return this.isNewDatabase;
  }
}


/* Location:              E:\java\java学习\hutubill\lib\all.jar!\org\hsqldb\persist\Logger.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */
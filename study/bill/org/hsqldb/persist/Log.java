package org.hsqldb.persist;

import java.io.File;
import java.io.IOException;
import org.hsqldb.Database;
import org.hsqldb.HsqlException;
import org.hsqldb.HsqlNameManager.HsqlName;
import org.hsqldb.NumberSequence;
import org.hsqldb.Row;
import org.hsqldb.SchemaManager;
import org.hsqldb.Session;
import org.hsqldb.SessionManager;
import org.hsqldb.Table;
import org.hsqldb.error.Error;
import org.hsqldb.lib.FileAccess;
import org.hsqldb.scriptio.ScriptReaderBase;
import org.hsqldb.scriptio.ScriptReaderDecode;
import org.hsqldb.scriptio.ScriptReaderText;
import org.hsqldb.scriptio.ScriptWriterBase;
import org.hsqldb.scriptio.ScriptWriterEncode;
import org.hsqldb.scriptio.ScriptWriterText;

public class Log
{
  private HsqlDatabaseProperties properties;
  private String baseFileName;
  private Database database;
  private FileAccess fa;
  ScriptWriterBase dbLogWriter;
  private String scriptFileName;
  private String logFileName;
  private boolean filesReadOnly;
  private long maxLogSize;
  private int writeDelay;
  private DataFileCache cache;
  private boolean isModified;
  
  Log(Database paramDatabase)
  {
    this.database = paramDatabase;
    this.fa = paramDatabase.logger.getFileAccess();
    this.baseFileName = paramDatabase.getPath();
    this.properties = paramDatabase.getProperties();
  }
  
  void initParams()
  {
    this.maxLogSize = (this.database.logger.getLogSize() * 1024L * 1024L);
    this.writeDelay = this.database.logger.getWriteDelay();
    this.filesReadOnly = this.database.isFilesReadOnly();
    this.scriptFileName = (this.baseFileName + ".script");
    this.logFileName = (this.baseFileName + ".log");
  }
  
  void open()
  {
    initParams();
    int i = this.properties.getDBModified();
    switch (i)
    {
    case 3: 
      break;
    case 1: 
      this.database.logger.logInfoEvent("open start - state modified");
      deleteNewAndOldFiles();
      deleteOldTempFiles();
      if (this.properties.isVersion18())
      {
        if (this.fa.isStreamElement(this.scriptFileName)) {
          processScript();
        } else {
          this.database.schemaManager.createPublicSchema();
        }
        HsqlNameManager.HsqlName localHsqlName = this.database.schemaManager.findSchemaHsqlName("PUBLIC");
        if (localHsqlName != null) {
          this.database.schemaManager.setDefaultSchemaHsqlName(localHsqlName);
        }
      }
      else
      {
        processScript();
      }
      processLog();
      checkpoint();
      break;
    case 2: 
      this.database.logger.logInfoEvent("open start - state new files");
      renameNewDataFile();
      renameNewScript();
      deleteLog();
      backupData();
      this.properties.setDBModified(0);
    case 0: 
      deleteLog();
      this.database.logger.logInfoEvent("open start - state not modified");
      processScript();
      if ((!this.filesReadOnly) && (isAnyCacheModified()))
      {
        this.properties.setDBModified(1);
        checkpoint();
      }
      break;
    }
    if (!this.filesReadOnly) {
      openLog();
    }
  }
  
  void close(boolean paramBoolean)
  {
    closeLog();
    deleteOldDataFiles();
    deleteOldTempFiles();
    deleteTempFileDirectory();
    writeScript(paramBoolean);
    this.database.logger.textTableManager.closeAllTextCaches(paramBoolean);
    if (this.cache != null) {
      this.cache.close();
    }
    this.properties.setProperty("hsqldb.script_format", this.database.logger.propScriptFormat);
    this.properties.setDBModified(2);
    deleteLog();
    int i = 1;
    if (this.cache != null) {
      if (paramBoolean)
      {
        this.cache.deleteFile();
        this.cache.deleteBackup();
        if (this.fa.isStreamElement(this.cache.dataFileName))
        {
          this.database.logger.logInfoEvent("delete .data file failed ");
          i = 0;
        }
        if (this.fa.isStreamElement(this.cache.backupFileName))
        {
          this.database.logger.logInfoEvent("delete .backup file failed ");
          i = 0;
        }
      }
      else
      {
        this.cache.backupDataFile(false);
      }
    }
    if (this.fa.isStreamElement(this.logFileName))
    {
      this.database.logger.logInfoEvent("delete .log file failed ");
      i = 0;
    }
    renameNewScript();
    if (i != 0) {
      this.properties.setDBModified(0);
    }
  }
  
  void shutdown()
  {
    if (this.cache != null) {
      this.cache.release();
    }
    this.database.logger.textTableManager.closeAllTextCaches(false);
    closeLog();
  }
  
  void deleteNewAndOldFiles()
  {
    deleteOldDataFiles();
    this.fa.removeElement(this.baseFileName + ".data" + ".new");
    this.fa.removeElement(this.baseFileName + ".backup" + ".new");
    this.fa.removeElement(this.scriptFileName + ".new");
  }
  
  void deleteBackup()
  {
    this.fa.removeElement(this.baseFileName + ".backup");
  }
  
  void backupData()
  {
    DataFileCache.backupFile(this.database, this.baseFileName + ".data", this.baseFileName + ".backup", false);
  }
  
  void renameNewDataFile()
  {
    DataFileCache.renameDataFile(this.database, this.baseFileName + ".data");
  }
  
  void renameNewBackup()
  {
    DataFileCache.renameBackupFile(this.database, this.baseFileName + ".backup");
  }
  
  void renameNewScript()
  {
    if (this.fa.isStreamElement(this.scriptFileName + ".new"))
    {
      this.fa.removeElement(this.scriptFileName);
      this.fa.renameElement(this.scriptFileName + ".new", this.scriptFileName);
    }
  }
  
  boolean renameNewDataFileDone()
  {
    return (this.fa.isStreamElement(this.baseFileName + ".data")) && (!this.fa.isStreamElement(this.baseFileName + ".data" + ".new"));
  }
  
  boolean renameNewScriptDone()
  {
    return (this.fa.isStreamElement(this.scriptFileName)) && (!this.fa.isStreamElement(this.scriptFileName + ".new"));
  }
  
  void deleteNewScript()
  {
    this.fa.removeElement(this.scriptFileName + ".new");
  }
  
  void deleteNewBackup()
  {
    this.fa.removeElement(this.baseFileName + ".backup" + ".new");
  }
  
  void deleteLog()
  {
    this.fa.removeElement(this.logFileName);
  }
  
  boolean isAnyCacheModified()
  {
    if ((this.cache != null) && (this.cache.isModified())) {
      return true;
    }
    return this.database.logger.textTableManager.isAnyTextCacheModified();
  }
  
  private boolean checkpoint()
  {
    if (this.filesReadOnly) {
      return true;
    }
    boolean bool1 = checkpointClose();
    boolean bool2 = checkpointReopen();
    if (!bool1) {
      this.database.logger.logSevereEvent("checkpoint failed - see previous error", null);
    }
    return bool2;
  }
  
  void checkpoint(Session paramSession, boolean paramBoolean)
  {
    if (this.filesReadOnly) {
      return;
    }
    if (this.cache == null) {
      paramBoolean = false;
    } else if (forceDefrag()) {
      paramBoolean = true;
    }
    if (paramBoolean) {
      defrag(paramSession);
    } else {
      checkpoint();
    }
  }
  
  boolean checkpointClose()
  {
    if (this.filesReadOnly) {
      return true;
    }
    this.database.logger.logInfoEvent("checkpointClose start");
    synchLog();
    this.database.lobManager.synch();
    this.database.logger.logInfoEvent("checkpointClose synched");
    deleteOldDataFiles();
    try
    {
      writeScript(false);
      this.database.logger.logInfoEvent("checkpointClose script done");
      if (this.cache != null)
      {
        this.cache.reset();
        this.cache.backupDataFile(true);
      }
      this.properties.setProperty("hsqldb.script_format", this.database.logger.propScriptFormat);
      this.properties.setDBModified(2);
    }
    catch (Throwable localThrowable1)
    {
      deleteNewScript();
      deleteNewBackup();
      this.database.logger.logSevereEvent("checkpoint failed - recovered", localThrowable1);
      return false;
    }
    closeLog();
    deleteLog();
    renameNewScript();
    renameNewBackup();
    try
    {
      this.properties.setDBModified(0);
    }
    catch (Throwable localThrowable2)
    {
      this.database.logger.logSevereEvent("logger.checkpointClose properties file save failed", localThrowable2);
    }
    this.database.logger.logInfoEvent("checkpointClose end");
    return true;
  }
  
  boolean checkpointReopen()
  {
    if (this.filesReadOnly) {
      return true;
    }
    this.database.sessionManager.resetLoggedSchemas();
    try
    {
      if (this.cache != null) {
        this.cache.reopen();
      }
      if (this.dbLogWriter != null) {
        openLog();
      }
    }
    catch (Throwable localThrowable)
    {
      return false;
    }
    return true;
  }
  
  public void defrag(Session paramSession)
  {
    this.database.logger.logInfoEvent("defrag start");
    try
    {
      synchLog();
      this.database.lobManager.synch();
      deleteOldDataFiles();
      DataFileDefrag localDataFileDefrag = this.cache.defrag(paramSession);
      this.database.persistentStoreCollection.setNewTableSpaces();
      this.database.schemaManager.setIndexRoots(localDataFileDefrag.getIndexRoots());
      this.database.sessionManager.resetLoggedSchemas();
    }
    catch (HsqlException localHsqlException)
    {
      throw localHsqlException;
    }
    catch (Throwable localThrowable)
    {
      this.database.logger.logSevereEvent("defrag failure", localThrowable);
      throw Error.error(466, localThrowable);
    }
    this.database.logger.logInfoEvent("defrag end");
  }
  
  boolean forceDefrag()
  {
    long l1 = this.database.logger.propCacheDefragLimit * this.cache.getFileFreePos() / 100L;
    if (l1 == 0L) {
      return false;
    }
    long l2 = this.database.logger.propFileSpaceValue * 1024L * 1024L;
    if (l2 > l1) {
      l1 = l2;
    }
    long l3 = this.cache.getLostBlockSize();
    return l3 > l1;
  }
  
  boolean hasCache()
  {
    return this.cache != null;
  }
  
  DataFileCache getCache()
  {
    if (this.cache == null)
    {
      this.cache = new DataFileCache(this.database, this.baseFileName);
      this.cache.open(this.filesReadOnly);
    }
    return this.cache;
  }
  
  void setLogSize(int paramInt)
  {
    this.maxLogSize = (paramInt * 1024L * 1024L);
  }
  
  int getWriteDelay()
  {
    return this.writeDelay;
  }
  
  void setWriteDelay(int paramInt)
  {
    this.writeDelay = paramInt;
    if ((this.dbLogWriter != null) && (this.dbLogWriter.getWriteDelay() != paramInt))
    {
      this.dbLogWriter.forceSync();
      this.dbLogWriter.stop();
      this.dbLogWriter.setWriteDelay(paramInt);
      this.dbLogWriter.start();
    }
  }
  
  public void setIncrementBackup(boolean paramBoolean)
  {
    if (this.cache != null) {
      this.cache.setIncrementBackup(paramBoolean);
    }
  }
  
  void writeOtherStatement(Session paramSession, String paramString)
  {
    try
    {
      this.dbLogWriter.writeOtherStatement(paramSession, paramString);
    }
    catch (IOException localIOException)
    {
      throw Error.error(452, getLogFileName());
    }
    if ((this.maxLogSize > 0L) && (this.dbLogWriter.size() > this.maxLogSize)) {
      this.database.logger.setCheckpointRequired();
    }
    setModified();
  }
  
  void writeInsertStatement(Session paramSession, Row paramRow, Table paramTable)
  {
    try
    {
      this.dbLogWriter.writeInsertStatement(paramSession, paramRow, paramTable);
    }
    catch (IOException localIOException)
    {
      throw Error.error(452, getLogFileName());
    }
  }
  
  void writeDeleteStatement(Session paramSession, Table paramTable, Object[] paramArrayOfObject)
  {
    try
    {
      this.dbLogWriter.writeDeleteStatement(paramSession, paramTable, paramArrayOfObject);
    }
    catch (IOException localIOException)
    {
      throw Error.error(452, getLogFileName());
    }
  }
  
  void writeSequenceStatement(Session paramSession, NumberSequence paramNumberSequence)
  {
    try
    {
      this.dbLogWriter.writeSequenceStatement(paramSession, paramNumberSequence);
    }
    catch (IOException localIOException)
    {
      throw Error.error(452, getLogFileName());
    }
    setModified();
  }
  
  void writeCommitStatement(Session paramSession)
  {
    try
    {
      this.dbLogWriter.writeCommitStatement(paramSession);
    }
    catch (IOException localIOException)
    {
      throw Error.error(452, getLogFileName());
    }
    if ((this.maxLogSize > 0L) && (this.dbLogWriter.size() > this.maxLogSize)) {
      this.database.logger.setCheckpointRequired();
    }
    setModified();
  }
  
  private void setModified()
  {
    if (!this.isModified)
    {
      this.database.databaseProperties.setDBModified(1);
      this.isModified = true;
    }
  }
  
  void synchLog()
  {
    if (this.dbLogWriter != null) {
      this.dbLogWriter.forceSync();
    }
  }
  
  void openLog()
  {
    if (this.filesReadOnly) {
      return;
    }
    Crypto localCrypto = this.database.logger.getCrypto();
    try
    {
      if (localCrypto == null) {
        this.dbLogWriter = new ScriptWriterText(this.database, this.logFileName, false, false, false);
      } else {
        this.dbLogWriter = new ScriptWriterEncode(this.database, this.logFileName, localCrypto);
      }
      this.dbLogWriter.setWriteDelay(this.writeDelay);
      this.dbLogWriter.start();
      this.isModified = false;
    }
    catch (Throwable localThrowable)
    {
      throw Error.error(452, this.logFileName);
    }
  }
  
  void closeLog()
  {
    if (this.dbLogWriter != null)
    {
      this.database.logger.logDetailEvent("log close size: " + this.dbLogWriter.size());
      this.dbLogWriter.close();
    }
  }
  
  void writeScript(boolean paramBoolean)
  {
    deleteNewScript();
    Crypto localCrypto = this.database.logger.getCrypto();
    if (localCrypto == null)
    {
      boolean bool = this.database.logger.propScriptFormat == 3;
      localObject = new ScriptWriterText(this.database, this.scriptFileName + ".new", paramBoolean, bool);
    }
    else
    {
      localObject = new ScriptWriterEncode(this.database, this.scriptFileName + ".new", paramBoolean, localCrypto);
    }
    ((ScriptWriterBase)localObject).writeAll();
    ((ScriptWriterBase)localObject).close();
    Object localObject = null;
  }
  
  private void processScript()
  {
    Object localObject = null;
    try
    {
      Crypto localCrypto = this.database.logger.getCrypto();
      if (localCrypto == null)
      {
        boolean bool = this.database.logger.propScriptFormat == 3;
        localObject = new ScriptReaderText(this.database, this.scriptFileName, bool);
      }
      else
      {
        localObject = new ScriptReaderDecode(this.database, this.scriptFileName, localCrypto, false);
      }
      Session localSession = this.database.sessionManager.getSysSessionForScript(this.database);
      ((ScriptReaderBase)localObject).readAll(localSession);
      ((ScriptReaderBase)localObject).close();
    }
    catch (Throwable localThrowable)
    {
      if (localObject != null)
      {
        ((ScriptReaderBase)localObject).close();
        if (this.cache != null) {
          this.cache.release();
        }
        this.database.logger.textTableManager.closeAllTextCaches(false);
      }
      this.database.logger.logWarningEvent("Script processing failure", localThrowable);
      if ((localThrowable instanceof HsqlException)) {
        throw ((HsqlException)localThrowable);
      }
      if ((localThrowable instanceof IOException)) {
        throw Error.error(452, localThrowable);
      }
      if ((localThrowable instanceof OutOfMemoryError)) {
        throw Error.error(460);
      }
      throw Error.error(458, localThrowable);
    }
  }
  
  private void processLog()
  {
    if (this.fa.isStreamElement(this.logFileName))
    {
      boolean bool = this.database.getURLProperties().isPropertyTrue("hsqldb.full_log_replay");
      ScriptRunner.runScript(this.database, this.logFileName, bool);
    }
  }
  
  void deleteOldDataFiles()
  {
    if (this.database.logger.isStoredFileAccess()) {
      return;
    }
    try
    {
      File localFile = new File(this.database.getCanonicalPath());
      File[] arrayOfFile = localFile.getParentFile().listFiles();
      if (arrayOfFile == null) {
        return;
      }
      for (int i = 0; i < arrayOfFile.length; i++) {
        if ((arrayOfFile[i].getName().startsWith(localFile.getName())) && (arrayOfFile[i].getName().endsWith(".old"))) {
          arrayOfFile[i].delete();
        }
      }
    }
    catch (Throwable localThrowable) {}
  }
  
  void deleteOldTempFiles()
  {
    try
    {
      if (this.database.logger.tempDirectoryPath == null) {
        return;
      }
      File localFile = new File(this.database.logger.tempDirectoryPath);
      File[] arrayOfFile = localFile.listFiles();
      if (arrayOfFile == null) {
        return;
      }
      for (int i = 0; i < arrayOfFile.length; i++) {
        arrayOfFile[i].delete();
      }
    }
    catch (Throwable localThrowable) {}
  }
  
  void deleteTempFileDirectory()
  {
    try
    {
      if (this.database.logger.tempDirectoryPath == null) {
        return;
      }
      File localFile = new File(this.database.logger.tempDirectoryPath);
      localFile.delete();
    }
    catch (Throwable localThrowable) {}
  }
  
  String getLogFileName()
  {
    return this.logFileName;
  }
}


/* Location:              E:\java\java学习\hutubill\lib\all.jar!\org\hsqldb\persist\Log.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */
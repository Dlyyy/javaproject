package org.hsqldb.scriptio;

import java.io.BufferedInputStream;
import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;
import java.util.zip.GZIPInputStream;
import org.hsqldb.Database;
import org.hsqldb.HsqlException;
import org.hsqldb.HsqlNameManager.HsqlName;
import org.hsqldb.SchemaManager;
import org.hsqldb.Session;
import org.hsqldb.Statement;
import org.hsqldb.Table;
import org.hsqldb.error.Error;
import org.hsqldb.lib.FileAccess;
import org.hsqldb.lib.LineReader;
import org.hsqldb.lib.StringConverter;
import org.hsqldb.map.ValuePool;
import org.hsqldb.persist.HsqlDatabaseProperties;
import org.hsqldb.persist.Logger;
import org.hsqldb.persist.PersistentStoreCollectionDatabase;
import org.hsqldb.result.Result;
import org.hsqldb.rowio.RowInputTextLog;
import org.hsqldb.types.Type;

public class ScriptReaderText
  extends ScriptReaderBase
{
  LineReader dataStreamIn;
  InputStream inputStream;
  InputStream bufferedStream;
  GZIPInputStream gzipStream;
  RowInputTextLog rowIn;
  boolean isInsert;
  
  ScriptReaderText(Database paramDatabase, String paramString)
  {
    super(paramDatabase, paramString);
  }
  
  public ScriptReaderText(Database paramDatabase, String paramString, boolean paramBoolean)
    throws IOException
  {
    super(paramDatabase, paramString);
    this.inputStream = this.database.logger.getFileAccess().openInputStreamElement(paramString);
    this.bufferedStream = new BufferedInputStream(this.inputStream);
    Object localObject;
    if (paramBoolean)
    {
      this.gzipStream = new GZIPInputStream(this.bufferedStream);
      localObject = this.gzipStream;
    }
    else
    {
      localObject = this.bufferedStream;
    }
    this.dataStreamIn = new LineReader((InputStream)localObject, "ISO-8859-1");
    this.rowIn = new RowInputTextLog(paramDatabase.databaseProperties.isVersion18());
  }
  
  protected void readDDL(Session paramSession)
  {
    while (readLoggedStatement(paramSession))
    {
      Statement localStatement = null;
      Result localResult = null;
      if (this.rowIn.getStatementType() == 3)
      {
        this.isInsert = true;
        break;
      }
      try
      {
        localStatement = paramSession.compileStatement(this.statement);
        localResult = paramSession.executeCompiledStatement(localStatement, ValuePool.emptyObjectArray, 0);
      }
      catch (HsqlException localHsqlException1)
      {
        localResult = Result.newErrorResult(localHsqlException1);
      }
      if ((!localResult.isError()) || (localStatement == null) || ((localStatement.getType() != 53) && ((localStatement.getType() != 69) || (localResult.getMainString().indexOf("org.hsqldb.Library") <= -1)))) {
        if (localResult.isError())
        {
          this.database.logger.logWarningEvent(localResult.getMainString(), localResult.getException());
          if ((localStatement == null) || (localStatement.getType() != 69))
          {
            HsqlException localHsqlException2 = Error.error(localResult.getException(), 461, 25, new Object[] { Long.toString(this.lineCount) + " " + this.database.getCanonicalPath(), localResult.getMainString() });
            handleException(localHsqlException2);
          }
        }
      }
    }
  }
  
  protected void readExistingData(Session paramSession)
  {
    try
    {
      Object localObject1 = null;
      this.database.setReferentialIntegrity(false);
      while ((this.isInsert) || (readLoggedStatement(paramSession)))
      {
        if (this.statementType == 6)
        {
          paramSession.setSchema(this.currentSchema);
          localObject1 = null;
        }
        else if (this.statementType == 3)
        {
          if (!this.rowIn.getTableName().equals(localObject1))
          {
            localObject1 = this.rowIn.getTableName();
            String str = paramSession.getSchemaName(this.currentSchema);
            this.currentTable = this.database.schemaManager.getUserTable((String)localObject1, str);
            this.currentStore = this.database.persistentStoreCollection.getStore(this.currentTable);
          }
          try
          {
            this.currentTable.insertFromScript(paramSession, this.currentStore, this.rowData);
          }
          catch (HsqlException localHsqlException)
          {
            handleException(localHsqlException);
          }
        }
        else
        {
          throw Error.error(461, this.statement);
        }
        this.isInsert = false;
      }
    }
    catch (Throwable localThrowable)
    {
      this.database.logger.logSevereEvent("readExistingData failed " + this.lineCount, localThrowable);
      throw Error.error(localThrowable, 461, 25, new Object[] { Long.valueOf(this.lineCount), localThrowable.toString() });
    }
    finally
    {
      this.database.setReferentialIntegrity(true);
    }
  }
  
  public boolean readLoggedStatement(Session paramSession)
  {
    if (!this.sessionChanged)
    {
      try
      {
        this.rawStatement = this.dataStreamIn.readLine();
      }
      catch (EOFException localEOFException)
      {
        return false;
      }
      catch (IOException localIOException)
      {
        throw Error.error(localIOException, 452, null);
      }
      this.lineCount += 1L;
      this.statement = StringConverter.unicodeStringToString(this.rawStatement);
      if (this.statement == null) {
        return false;
      }
    }
    processStatement(paramSession);
    return true;
  }
  
  void processStatement(Session paramSession)
  {
    if (this.statement.startsWith("/*C"))
    {
      int i = this.statement.indexOf('*', 4);
      this.sessionNumber = Integer.parseInt(this.statement.substring(3, i));
      this.statement = this.statement.substring(i + 2);
      this.sessionChanged = true;
      this.statementType = 5;
      return;
    }
    this.sessionChanged = false;
    this.rowIn.setSource(paramSession, this.statement);
    this.statementType = this.rowIn.getStatementType();
    if (this.statementType == 1)
    {
      this.rowData = null;
      this.currentTable = null;
      return;
    }
    if (this.statementType == 4)
    {
      this.rowData = null;
      this.currentTable = null;
      return;
    }
    if (this.statementType == 6)
    {
      this.rowData = null;
      this.currentTable = null;
      this.currentSchema = this.rowIn.getSchemaName();
      return;
    }
    String str1 = this.rowIn.getTableName();
    String str2 = paramSession.getCurrentSchemaHsqlName().name;
    this.currentTable = this.database.schemaManager.getUserTable(str1, str2);
    this.currentStore = this.database.persistentStoreCollection.getStore(this.currentTable);
    Type[] arrayOfType;
    if (this.statementType == 3) {
      arrayOfType = this.currentTable.getColumnTypes();
    } else if (this.currentTable.hasPrimaryKey()) {
      arrayOfType = this.currentTable.getPrimaryKeyTypes();
    } else {
      arrayOfType = this.currentTable.getColumnTypes();
    }
    this.rowData = this.rowIn.readData(arrayOfType);
  }
  
  public void close()
  {
    try
    {
      if (this.dataStreamIn != null) {
        this.dataStreamIn.close();
      }
    }
    catch (Exception localException1) {}
    try
    {
      if (this.gzipStream != null) {
        this.gzipStream.close();
      }
    }
    catch (Exception localException2) {}
    try
    {
      if (this.inputStream != null) {
        this.inputStream.close();
      }
    }
    catch (Exception localException3) {}
    try
    {
      if (this.scrwriter != null) {
        this.scrwriter.close();
      }
      this.database.recoveryMode = 0;
    }
    catch (Exception localException4) {}
  }
  
  private void handleException(HsqlException paramHsqlException)
  {
    if (this.database.recoveryMode == 0) {
      throw paramHsqlException;
    }
    if (this.scrwriter == null)
    {
      String str = this.database.getPath() + ".reject";
      this.scrwriter = new ScriptWriterText(this.database, str, true, true, true);
    }
    try
    {
      this.scrwriter.writeLogStatement(null, this.rawStatement);
    }
    catch (Throwable localThrowable) {}
  }
}


/* Location:              E:\java\java学习\hutubill\lib\all.jar!\org\hsqldb\scriptio\ScriptReaderText.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */
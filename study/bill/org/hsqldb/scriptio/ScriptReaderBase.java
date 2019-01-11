package org.hsqldb.scriptio;

import org.hsqldb.Database;
import org.hsqldb.NumberSequence;
import org.hsqldb.Session;
import org.hsqldb.Table;
import org.hsqldb.persist.PersistentStore;

public abstract class ScriptReaderBase
{
  public static final int ANY_STATEMENT = 1;
  public static final int DELETE_STATEMENT = 2;
  public static final int INSERT_STATEMENT = 3;
  public static final int COMMIT_STATEMENT = 4;
  public static final int SESSION_ID = 5;
  public static final int SET_SCHEMA_STATEMENT = 6;
  public static final int SET_FILES_CHECK_STATEMENT = 7;
  Database database;
  String fileNamePath;
  long lineCount;
  int statementType;
  int sessionNumber;
  boolean sessionChanged;
  Object[] rowData;
  long sequenceValue;
  String rawStatement;
  String statement;
  Table currentTable;
  PersistentStore currentStore;
  NumberSequence currentSequence;
  String currentSchema;
  ScriptWriterText scrwriter;
  
  ScriptReaderBase(Database paramDatabase, String paramString)
  {
    this.database = paramDatabase;
    this.fileNamePath = paramString;
  }
  
  public void readAll(Session paramSession)
  {
    readDDL(paramSession);
    readExistingData(paramSession);
  }
  
  protected abstract void readDDL(Session paramSession);
  
  protected abstract void readExistingData(Session paramSession);
  
  public abstract boolean readLoggedStatement(Session paramSession);
  
  public String getFileNamePath()
  {
    return this.fileNamePath;
  }
  
  public int getStatementType()
  {
    return this.statementType;
  }
  
  public int getSessionNumber()
  {
    return this.sessionNumber;
  }
  
  public Object[] getData()
  {
    return this.rowData;
  }
  
  public String getLoggedStatement()
  {
    return this.statement;
  }
  
  public Table getCurrentTable()
  {
    return this.currentTable;
  }
  
  public String getCurrentSchema()
  {
    return this.currentSchema;
  }
  
  public long getLineNumber()
  {
    return this.lineCount;
  }
  
  public abstract void close();
}


/* Location:              E:\java\java学习\hutubill\lib\all.jar!\org\hsqldb\scriptio\ScriptReaderBase.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */
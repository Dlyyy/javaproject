package org.hsqldb.persist;

import java.io.EOFException;
import org.hsqldb.ColumnSchema;
import org.hsqldb.Database;
import org.hsqldb.HsqlException;
import org.hsqldb.HsqlNameManager.HsqlName;
import org.hsqldb.Row;
import org.hsqldb.SchemaManager;
import org.hsqldb.Session;
import org.hsqldb.SessionManager;
import org.hsqldb.Statement;
import org.hsqldb.StatementDML;
import org.hsqldb.Table;
import org.hsqldb.error.Error;
import org.hsqldb.lib.IntKeyHashMap;
import org.hsqldb.map.ValuePool;
import org.hsqldb.result.Result;
import org.hsqldb.scriptio.ScriptReaderBase;
import org.hsqldb.scriptio.ScriptReaderDecode;
import org.hsqldb.scriptio.ScriptReaderText;
import org.hsqldb.types.Type;

public class ScriptRunner
{
  public static void runScript(Database paramDatabase, String paramString, boolean paramBoolean)
  {
    Crypto localCrypto = paramDatabase.logger.getCrypto();
    Object localObject;
    try
    {
      if (localCrypto == null) {
        localObject = new ScriptReaderText(paramDatabase, paramString, false);
      } else {
        localObject = new ScriptReaderDecode(paramDatabase, paramString, localCrypto, true);
      }
    }
    catch (Throwable localThrowable)
    {
      if (!(localThrowable instanceof EOFException)) {
        paramDatabase.logger.logSevereEvent("opening log file", localThrowable);
      }
      return;
    }
    runScript(paramDatabase, (ScriptReaderBase)localObject, paramBoolean);
  }
  
  private static void runScript(Database paramDatabase, ScriptReaderBase paramScriptReaderBase, boolean paramBoolean)
  {
    IntKeyHashMap localIntKeyHashMap = new IntKeyHashMap();
    Session localSession = null;
    int i = 0;
    StatementDML localStatementDML = new StatementDML(91, 2004, null);
    String str2 = paramDatabase.getCanonicalPath();
    String str3 = paramBoolean ? "open aborted" : "open continued";
    localStatementDML.setCompileTimestamp(Long.MAX_VALUE);
    paramDatabase.setReferentialIntegrity(false);
    try
    {
      while (paramScriptReaderBase.readLoggedStatement(localSession))
      {
        int k = paramScriptReaderBase.getSessionNumber();
        if ((localSession == null) || (i != k))
        {
          i = k;
          localSession = (Session)localIntKeyHashMap.get(i);
          if (localSession == null)
          {
            localSession = paramDatabase.getSessionManager().newSessionForLog(paramDatabase);
            localIntKeyHashMap.put(i, localSession);
          }
        }
        localObject1 = null;
        int j = paramScriptReaderBase.getStatementType();
        Object localObject4;
        Object localObject3;
        switch (j)
        {
        case 7: 
          localObject1 = null;
        case 1: 
          String str1 = paramScriptReaderBase.getLoggedStatement();
          try
          {
            localObject2 = localSession.compileStatement(str1);
            if ((paramDatabase.getProperties().isVersion18()) && (((Statement)localObject2).getType() == 87))
            {
              Table localTable = (Table)((org.hsqldb.StatementSchema)localObject2).getArguments()[0];
              for (int m = 0; m < localTable.getColumnCount(); m++)
              {
                localObject4 = localTable.getColumn(m);
                if (((ColumnSchema)localObject4).getDataType().isBitType()) {
                  ((ColumnSchema)localObject4).setType(Type.SQL_BOOLEAN);
                }
              }
            }
            localObject1 = localSession.executeCompiledStatement((Statement)localObject2, ValuePool.emptyObjectArray, 0);
          }
          catch (Throwable localThrowable2)
          {
            localObject1 = Result.newErrorResult(localThrowable2);
          }
          if ((localObject1 != null) && (((Result)localObject1).isError()))
          {
            if (((Result)localObject1).getException() != null) {
              throw ((Result)localObject1).getException();
            }
            throw Error.error((Result)localObject1);
          }
          break;
        case 4: 
          localSession.commit(false);
          break;
        case 3: 
          localSession.sessionContext.currentStatement = localStatementDML;
          localSession.beginAction(localStatementDML);
          localObject3 = paramScriptReaderBase.getData();
          paramScriptReaderBase.getCurrentTable().insertNoCheckFromLog(localSession, (Object[])localObject3);
          localSession.endAction(Result.updateOneResult);
          break;
        case 2: 
          localSession.sessionContext.currentStatement = localStatementDML;
          localSession.beginAction(localStatementDML);
          localObject3 = paramScriptReaderBase.getCurrentTable();
          PersistentStore localPersistentStore = ((Table)localObject3).getRowStore(localSession);
          localObject4 = paramScriptReaderBase.getData();
          Row localRow = ((Table)localObject3).getDeleteRowFromLog(localSession, (Object[])localObject4);
          if (localRow != null) {
            localSession.addDeleteAction((Table)localObject3, localPersistentStore, localRow, null);
          }
          localSession.endAction(Result.updateOneResult);
          break;
        case 6: 
          localObject3 = paramDatabase.schemaManager.findSchemaHsqlName(paramScriptReaderBase.getCurrentSchema());
          localSession.setCurrentSchemaHsqlName((HsqlNameManager.HsqlName)localObject3);
          break;
        case 5: 
          break;
        default: 
          throw Error.error(472);
        }
        if (localSession.isClosed())
        {
          localSession = null;
          localIntKeyHashMap.remove(i);
        }
      }
    }
    catch (HsqlException localHsqlException)
    {
      if (localHsqlException.getErrorCode() == 65064) {
        throw localHsqlException;
      }
      localObject1 = "statement error processing log - " + str3 + paramScriptReaderBase.getFileNamePath() + " line: " + paramScriptReaderBase.getLineNumber();
      paramDatabase.logger.logSevereEvent((String)localObject1, localHsqlException);
      if (paramBoolean) {
        throw Error.error(localHsqlException, 472, (String)localObject1);
      }
    }
    catch (OutOfMemoryError localOutOfMemoryError)
    {
      localObject1 = "out of memory processing log - " + str2 + " line: " + paramScriptReaderBase.getLineNumber();
      paramDatabase.logger.logSevereEvent((String)localObject1, localOutOfMemoryError);
      throw Error.error(460);
    }
    catch (Throwable localThrowable1)
    {
      Object localObject1 = Error.error(localThrowable1, 472, 25, new String[] { paramScriptReaderBase.getLineNumber() + " " + str2, localThrowable1.getMessage() });
      Object localObject2 = "statement error processing log - " + str3 + paramScriptReaderBase.getFileNamePath() + " line: " + paramScriptReaderBase.getLineNumber();
      paramDatabase.logger.logSevereEvent((String)localObject2, (Throwable)localObject1);
      if (paramBoolean) {
        throw ((Throwable)localObject1);
      }
    }
    finally
    {
      if (paramScriptReaderBase != null) {
        paramScriptReaderBase.close();
      }
      paramDatabase.getSessionManager().closeAllSessions();
      paramDatabase.setReferentialIntegrity(true);
    }
  }
}


/* Location:              E:\java\java学习\hutubill\lib\all.jar!\org\hsqldb\persist\ScriptRunner.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */
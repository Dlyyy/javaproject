package org.hsqldb;

import org.hsqldb.error.Error;
import org.hsqldb.lib.ArrayUtil;
import org.hsqldb.navigator.RowSetNavigatorData;
import org.hsqldb.navigator.RowSetNavigatorDataChange;
import org.hsqldb.persist.PersistentStore;
import org.hsqldb.result.Result;
import org.hsqldb.result.ResultMetaData;
import org.hsqldb.rights.Grantee;
import org.hsqldb.types.Type;

public class StatementResultUpdate
  extends StatementDML
{
  int actionType;
  Type[] types;
  Result result;
  
  StatementResultUpdate()
  {
    this.writeTableNames = new HsqlNameManager.HsqlName[1];
    setCompileTimestamp(Long.MAX_VALUE);
  }
  
  public String describe(Session paramSession)
  {
    return "";
  }
  
  public Result execute(Session paramSession)
  {
    Result localResult;
    try
    {
      localResult = getResult(paramSession);
      clearStructures(paramSession);
    }
    catch (Throwable localThrowable)
    {
      clearStructures(paramSession);
      localResult = Result.newErrorResult(localThrowable);
    }
    return localResult;
  }
  
  Result getResult(Session paramSession)
  {
    checkAccessRights(paramSession);
    Object[] arrayOfObject1 = paramSession.sessionContext.dynamicArguments;
    PersistentStore localPersistentStore = this.baseTable.getRowStore(paramSession);
    Row localRow;
    Object localObject;
    switch (this.actionType)
    {
    case 91: 
      localRow = getRow(paramSession, arrayOfObject1);
      if ((localRow == null) || (localRow.isDeleted(paramSession, localPersistentStore))) {
        throw Error.error(3621);
      }
      localObject = paramSession.sessionContext.getRowSetDataChange();
      Object[] arrayOfObject2 = (Object[])ArrayUtil.duplicateArray(localRow.getData());
      boolean[] arrayOfBoolean = this.baseTable.getNewColumnCheckList();
      for (int j = 0; j < this.baseColumnMap.length; j++) {
        if (this.types[j] != Type.SQL_ALL_TYPES)
        {
          arrayOfObject2[this.baseColumnMap[j]] = arrayOfObject1[j];
          arrayOfBoolean[this.baseColumnMap[j]] = true;
        }
      }
      int[] arrayOfInt = ArrayUtil.booleanArrayToIntIndexes(arrayOfBoolean);
      ((RowSetNavigatorDataChange)localObject).addRow(paramSession, localRow, arrayOfObject2, this.baseTable.getColumnTypes(), arrayOfInt);
      ((RowSetNavigatorDataChange)localObject).endMainDataSet();
      update(paramSession, this.baseTable, (RowSetNavigatorDataChange)localObject, null);
      break;
    case 18: 
      localRow = getRow(paramSession, arrayOfObject1);
      if ((localRow == null) || (localRow.isDeleted(paramSession, localPersistentStore))) {
        throw Error.error(3621);
      }
      localObject = paramSession.sessionContext.getRowSetDataChange();
      ((RowSetNavigatorDataChange)localObject).addRow(localRow);
      ((RowSetNavigatorDataChange)localObject).endMainDataSet();
      delete(paramSession, this.baseTable, (RowSetNavigatorDataChange)localObject);
      break;
    case 55: 
      localObject = this.baseTable.getNewRowData(paramSession);
      for (int i = 0; i < this.baseColumnMap.length; i++) {
        localObject[this.baseColumnMap[i]] = arrayOfObject1[i];
      }
      return insertSingleRow(paramSession, localPersistentStore, (Object[])localObject);
    }
    return Result.updateOneResult;
  }
  
  Row getRow(Session paramSession, Object[] paramArrayOfObject)
  {
    int i = this.result.metaData.getColumnCount();
    Long localLong = (Long)paramArrayOfObject[i];
    PersistentStore localPersistentStore = this.baseTable.getRowStore(paramSession);
    Row localRow = null;
    if (i + 2 == this.result.metaData.getExtendedColumnCount())
    {
      Object[] arrayOfObject = ((RowSetNavigatorData)this.result.getNavigator()).getData(localLong.longValue());
      if (arrayOfObject != null) {
        localRow = (Row)arrayOfObject[(i + 1)];
      }
    }
    else
    {
      int j = (int)localLong.longValue();
      localRow = (Row)localPersistentStore.get(j, false);
    }
    this.result = null;
    return localRow;
  }
  
  void setRowActionProperties(Result paramResult, int paramInt, StatementQuery paramStatementQuery, Type[] paramArrayOfType)
  {
    QueryExpression localQueryExpression = paramStatementQuery.queryExpression;
    this.result = paramResult;
    this.actionType = paramInt;
    this.baseTable = localQueryExpression.getBaseTable();
    this.types = paramArrayOfType;
    this.baseColumnMap = localQueryExpression.getBaseTableColumnMap();
    this.writeTableNames[0] = this.baseTable.getName();
    this.sql = paramStatementQuery.getSQL();
    this.parameterMetaData = localQueryExpression.getMetaData();
  }
  
  void checkAccessRights(Session paramSession)
  {
    switch (this.type)
    {
    case 10: 
      break;
    case 55: 
      paramSession.getGrantee().checkInsert(this.targetTable, this.insertCheckColumns);
      break;
    case 44: 
      break;
    case 19: 
      paramSession.getGrantee().checkDelete(this.targetTable);
      break;
    case 92: 
      paramSession.getGrantee().checkUpdate(this.targetTable, this.updateCheckColumns);
      break;
    case 56: 
      paramSession.getGrantee().checkInsert(this.targetTable, this.insertCheckColumns);
      paramSession.getGrantee().checkUpdate(this.targetTable, this.updateCheckColumns);
    }
  }
}


/* Location:              E:\java\java学习\hutubill\lib\all.jar!\org\hsqldb\StatementResultUpdate.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */
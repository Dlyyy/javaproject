package org.hsqldb;

import org.hsqldb.index.Index;
import org.hsqldb.navigator.RowIterator;
import org.hsqldb.navigator.RowSetNavigator;
import org.hsqldb.navigator.RowSetNavigatorClient;
import org.hsqldb.navigator.RowSetNavigatorDataChange;
import org.hsqldb.navigator.RowSetNavigatorDataChangeMemory;
import org.hsqldb.persist.PersistentStore;
import org.hsqldb.result.Result;
import org.hsqldb.result.ResultMetaData;
import org.hsqldb.types.Type;

public class StatementInsert
  extends StatementDML
{
  static final int isNone = 0;
  static final int isIgnore = 1;
  static final int isReplace = 2;
  static final int isUpdate = 3;
  int overrideUserValue = -1;
  int specialAction = 0;
  
  StatementInsert(Session paramSession, Table paramTable, int[] paramArrayOfInt1, Expression paramExpression, boolean[] paramArrayOfBoolean1, Expression[] paramArrayOfExpression1, boolean[] paramArrayOfBoolean2, int[] paramArrayOfInt2, Expression[] paramArrayOfExpression2, int paramInt, ParserDQL.CompileContext paramCompileContext)
  {
    super(55, 2004, paramSession.getCurrentSchemaHsqlName());
    this.targetTable = paramTable;
    this.baseTable = (paramTable.isTriggerInsertable() ? paramTable : paramTable.getBaseTable());
    this.insertColumnMap = paramArrayOfInt1;
    this.insertCheckColumns = paramArrayOfBoolean1;
    this.insertExpression = paramExpression;
    this.updateCheckColumns = paramArrayOfBoolean2;
    this.updateExpressions = paramArrayOfExpression1;
    this.updateColumnMap = paramArrayOfInt2;
    this.targets = paramArrayOfExpression2;
    this.specialAction = paramInt;
    setupChecks();
    setDatabaseObjects(paramSession, paramCompileContext);
    checkAccessRights(paramSession);
    this.isSimpleInsert = ((paramExpression != null) && (paramExpression.nodes.length == 1) && (this.updatableTableCheck == null) && (paramInt == 0));
  }
  
  StatementInsert(Session paramSession, Table paramTable, int[] paramArrayOfInt1, boolean[] paramArrayOfBoolean1, QueryExpression paramQueryExpression, Expression[] paramArrayOfExpression1, boolean[] paramArrayOfBoolean2, int[] paramArrayOfInt2, Expression[] paramArrayOfExpression2, int paramInt1, int paramInt2, ParserDQL.CompileContext paramCompileContext)
  {
    super(55, 2004, paramSession.getCurrentSchemaHsqlName());
    this.targetTable = paramTable;
    this.baseTable = (paramTable.isTriggerInsertable() ? paramTable : paramTable.getBaseTable());
    this.insertColumnMap = paramArrayOfInt1;
    this.insertCheckColumns = paramArrayOfBoolean1;
    this.queryExpression = paramQueryExpression;
    this.overrideUserValue = paramInt2;
    this.updateCheckColumns = paramArrayOfBoolean2;
    this.updateExpressions = paramArrayOfExpression1;
    this.updateColumnMap = paramArrayOfInt2;
    this.targets = paramArrayOfExpression2;
    this.specialAction = paramInt1;
    setupChecks();
    setDatabaseObjects(paramSession, paramCompileContext);
    checkAccessRights(paramSession);
  }
  
  Result getResult(Session paramSession)
  {
    Result localResult = null;
    RowSetNavigator localRowSetNavigator = null;
    PersistentStore localPersistentStore = this.baseTable.getRowStore(paramSession);
    int i = 0;
    if (this.generatedIndexes != null)
    {
      localResult = Result.newUpdateCountResult(this.generatedResultMetaData, 0);
      localRowSetNavigator = localResult.getChainedResult().getNavigator();
    }
    if (this.isSimpleInsert)
    {
      localObject1 = this.baseTable.getColumnTypes();
      localObject2 = getInsertData(paramSession, (Type[])localObject1, this.insertExpression.nodes[0].nodes);
      return insertSingleRow(paramSession, localPersistentStore, (Object[])localObject2);
    }
    Object localObject1 = this.queryExpression == null ? getInsertValuesNavigator(paramSession) : getInsertSelectNavigator(paramSession);
    Object localObject2 = null;
    Object localObject3;
    if (this.specialAction != 0) {
      while (((RowSetNavigator)localObject1).hasNext())
      {
        int j = 0;
        localObject3 = ((RowSetNavigator)localObject1).getNext();
        int k = 0;
        int m = this.baseTable.constraintList.length;
        while (k < m)
        {
          Constraint localConstraint = this.baseTable.constraintList[k];
          if (localConstraint.isUniqueOrPK())
          {
            RowIterator localRowIterator = localConstraint.findUniqueRows(paramSession, (Object[])localObject3);
            while (localRowIterator.hasNext())
            {
              j = 1;
              if (this.specialAction == 1) {
                break;
              }
              if (localObject2 == null) {
                localObject2 = new RowSetNavigatorDataChangeMemory(paramSession);
              }
              Row localRow = localRowIterator.getNextRow();
              if (localConstraint.core.mainIndex.compareRowNonUnique(paramSession, localRow.getData(), (Object[])localObject3, localConstraint.core.mainCols) != 0) {
                break;
              }
              ((RowSetNavigatorDataChange)localObject2).addRow(paramSession, localRow, (Object[])localObject3, this.baseTable.getColumnTypes(), this.baseTable.defaultColumnMap);
              i++;
            }
            localRowIterator.release();
          }
          k++;
        }
        if (j != 0) {
          ((RowSetNavigator)localObject1).removeCurrent();
        }
      }
    }
    if ((this.specialAction == 2) && (localObject2 != null))
    {
      i = update(paramSession, this.baseTable, (RowSetNavigatorDataChange)localObject2, null);
      ((RowSetNavigatorDataChange)localObject2).endMainDataSet();
    }
    else if ((this.specialAction == 3) && (localObject2 != null))
    {
      Type[] arrayOfType = this.baseTable.getColumnTypes();
      paramSession.sessionContext.setRangeIterator(((RowSetNavigatorDataChange)localObject2).getUpdateRowIterator());
      while (((RowSetNavigatorDataChange)localObject2).next())
      {
        paramSession.sessionData.startRowProcessing();
        localObject3 = ((RowSetNavigatorDataChange)localObject2).getCurrentRow();
        Object[] arrayOfObject1 = ((Row)localObject3).getData();
        Object[] arrayOfObject2 = getUpdatedData(paramSession, this.targets, this.baseTable, this.updateColumnMap, this.updateExpressions, arrayOfType, arrayOfObject1);
        ((RowSetNavigatorDataChange)localObject2).addUpdate((Row)localObject3, arrayOfObject2, this.updateColumnMap);
        paramSession.sessionContext.rownum += 1;
      }
      i = update(paramSession, this.baseTable, (RowSetNavigatorDataChange)localObject2, null);
      ((RowSetNavigatorDataChange)localObject2).endMainDataSet();
    }
    if (((RowSetNavigator)localObject1).getSize() != 0)
    {
      insertRowSet(paramSession, localRowSetNavigator, (RowSetNavigator)localObject1);
      i += ((RowSetNavigator)localObject1).getSize();
    }
    if (this.baseTable.triggerLists[0].length > 0) {
      this.baseTable.fireTriggers(paramSession, 0, (RowSetNavigator)localObject1);
    }
    if (localResult == null) {
      localResult = new Result(1, i);
    } else {
      localResult.setUpdateCount(i);
    }
    if (i == 0) {
      paramSession.addWarning(HsqlException.noDataCondition);
    }
    paramSession.sessionContext.diagnosticsVariables[2] = Integer.valueOf(i);
    return localResult;
  }
  
  RowSetNavigator getInsertSelectNavigator(Session paramSession)
  {
    Type[] arrayOfType1 = this.baseTable.getColumnTypes();
    int[] arrayOfInt = this.insertColumnMap;
    Result localResult = this.queryExpression.getResult(paramSession, 0);
    RowSetNavigator localRowSetNavigator = localResult.initialiseNavigator();
    Type[] arrayOfType2 = localResult.metaData.columnTypes;
    RowSetNavigatorClient localRowSetNavigatorClient = new RowSetNavigatorClient(localRowSetNavigator.getSize());
    while (localRowSetNavigator.hasNext())
    {
      Object[] arrayOfObject1 = this.baseTable.getNewRowData(paramSession);
      Object[] arrayOfObject2 = localRowSetNavigator.getNext();
      for (int i = 0; i < arrayOfInt.length; i++)
      {
        int j = arrayOfInt[i];
        if (j != this.overrideUserValue)
        {
          Type localType = arrayOfType2[i];
          arrayOfObject1[j] = arrayOfType1[j].convertToType(paramSession, arrayOfObject2[i], localType);
        }
      }
      localRowSetNavigatorClient.add(arrayOfObject1);
    }
    return localRowSetNavigatorClient;
  }
  
  RowSetNavigator getInsertValuesNavigator(Session paramSession)
  {
    Type[] arrayOfType = this.baseTable.getColumnTypes();
    Expression[] arrayOfExpression1 = this.insertExpression.nodes;
    RowSetNavigatorClient localRowSetNavigatorClient = new RowSetNavigatorClient(arrayOfExpression1.length);
    for (int i = 0; i < arrayOfExpression1.length; i++)
    {
      Expression[] arrayOfExpression2 = arrayOfExpression1[i].nodes;
      Object[] arrayOfObject = getInsertData(paramSession, arrayOfType, arrayOfExpression2);
      localRowSetNavigatorClient.add(arrayOfObject);
    }
    return localRowSetNavigatorClient;
  }
}


/* Location:              E:\java\java学习\hutubill\lib\all.jar!\org\hsqldb\StatementInsert.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */
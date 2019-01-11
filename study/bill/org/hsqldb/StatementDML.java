package org.hsqldb;

import org.hsqldb.error.Error;
import org.hsqldb.index.Index;
import org.hsqldb.lib.ArrayUtil;
import org.hsqldb.lib.HashSet;
import org.hsqldb.lib.OrderedHashSet;
import org.hsqldb.navigator.RangeIterator;
import org.hsqldb.navigator.RowIterator;
import org.hsqldb.navigator.RowSetNavigator;
import org.hsqldb.navigator.RowSetNavigatorClient;
import org.hsqldb.navigator.RowSetNavigatorDataChange;
import org.hsqldb.persist.PersistentStore;
import org.hsqldb.result.Result;
import org.hsqldb.result.ResultMetaData;
import org.hsqldb.types.DateTimeType;
import org.hsqldb.types.Type;

public class StatementDML
  extends StatementDMQL
{
  Expression[] targets;
  boolean isTruncate;
  boolean isMergeDeleteFirst;
  Expression mergeInsertCondition;
  Expression mergeUpdateCondition;
  Expression mergeDeleteCondition;
  boolean isSimpleInsert;
  int generatedType;
  ResultMetaData generatedInputMetaData;
  int limit = Integer.MAX_VALUE;
  int[] generatedIndexes;
  ResultMetaData generatedResultMetaData;
  
  public StatementDML(int paramInt1, int paramInt2, HsqlNameManager.HsqlName paramHsqlName)
  {
    super(paramInt1, paramInt2, paramHsqlName);
  }
  
  StatementDML(Session paramSession, Table paramTable, RangeVariable paramRangeVariable, RangeVariable[] paramArrayOfRangeVariable, ParserDQL.CompileContext paramCompileContext, boolean paramBoolean, int paramInt, SortAndSlice paramSortAndSlice)
  {
    super(19, 2004, paramSession.getCurrentSchemaHsqlName());
    this.targetTable = paramTable;
    this.baseTable = (paramTable.isTriggerDeletable() ? paramTable : paramTable.getBaseTable());
    this.targetRangeVariables = paramArrayOfRangeVariable;
    this.restartIdentity = paramBoolean;
    if (paramSortAndSlice != null)
    {
      int[] arrayOfInt = paramSortAndSlice.getLimits(paramSession, null, Integer.MAX_VALUE);
      this.limit = arrayOfInt[1];
    }
    setDatabaseObjects(paramSession, paramCompileContext);
    checkAccessRights(paramSession);
    if (paramInt == 1215) {
      this.isTruncate = true;
    }
    paramRangeVariable.addAllColumns();
  }
  
  StatementDML(Session paramSession, Expression[] paramArrayOfExpression1, Table paramTable, RangeVariable paramRangeVariable, RangeVariable[] paramArrayOfRangeVariable, int[] paramArrayOfInt, Expression[] paramArrayOfExpression2, boolean[] paramArrayOfBoolean, ParserDQL.CompileContext paramCompileContext, SortAndSlice paramSortAndSlice)
  {
    super(92, 2004, paramSession.getCurrentSchemaHsqlName());
    this.targets = paramArrayOfExpression1;
    this.targetTable = paramTable;
    this.baseTable = (paramTable.isTriggerUpdatable() ? paramTable : paramTable.getBaseTable());
    this.updateColumnMap = paramArrayOfInt;
    this.updateExpressions = paramArrayOfExpression2;
    this.updateCheckColumns = paramArrayOfBoolean;
    this.targetRangeVariables = paramArrayOfRangeVariable;
    if (paramSortAndSlice != null)
    {
      int[] arrayOfInt = paramSortAndSlice.getLimits(paramSession, null, Integer.MAX_VALUE);
      this.limit = arrayOfInt[1];
    }
    setupChecks();
    setDatabaseObjects(paramSession, paramCompileContext);
    checkAccessRights(paramSession);
    paramRangeVariable.addAllColumns();
  }
  
  StatementDML(Session paramSession, Expression[] paramArrayOfExpression1, RangeVariable paramRangeVariable1, RangeVariable paramRangeVariable2, RangeVariable[] paramArrayOfRangeVariable, int[] paramArrayOfInt1, int[] paramArrayOfInt2, boolean[] paramArrayOfBoolean, Expression paramExpression1, Expression paramExpression2, Expression[] paramArrayOfExpression2, boolean paramBoolean, Expression paramExpression3, Expression paramExpression4, Expression paramExpression5, ParserDQL.CompileContext paramCompileContext)
  {
    super(56, 2004, paramSession.getCurrentSchemaHsqlName());
    this.targets = paramArrayOfExpression1;
    this.sourceTable = paramRangeVariable1.rangeTable;
    this.targetTable = paramRangeVariable2.rangeTable;
    this.baseTable = (this.targetTable.isTriggerUpdatable() ? this.targetTable : this.targetTable.getBaseTable());
    this.insertCheckColumns = paramArrayOfBoolean;
    this.insertColumnMap = paramArrayOfInt1;
    this.updateColumnMap = paramArrayOfInt2;
    this.insertExpression = paramExpression2;
    this.updateExpressions = paramArrayOfExpression2;
    this.targetRangeVariables = paramArrayOfRangeVariable;
    this.condition = paramExpression1;
    this.mergeInsertCondition = paramExpression3;
    this.mergeUpdateCondition = paramExpression4;
    this.mergeDeleteCondition = paramExpression5;
    this.isMergeDeleteFirst = paramBoolean;
    setupChecks();
    setDatabaseObjects(paramSession, paramCompileContext);
    checkAccessRights(paramSession);
  }
  
  StatementDML()
  {
    super(91, 2004, null);
  }
  
  void setupChecks()
  {
    if (this.targetTable != this.baseTable)
    {
      QuerySpecification localQuerySpecification = ((TableDerived)this.targetTable).getQueryExpression().getMainSelect();
      this.updatableTableCheck = localQuerySpecification.checkQueryCondition;
      this.checkRangeVariable = localQuerySpecification.rangeVariables[(localQuerySpecification.rangeVariables.length - 1)];
    }
  }
  
  Result getResult(Session paramSession)
  {
    Result localResult = null;
    switch (this.type)
    {
    case 92: 
      localResult = executeUpdateStatement(paramSession);
      break;
    case 56: 
      localResult = executeMergeStatement(paramSession);
      break;
    case 19: 
      if (this.isTruncate) {
        localResult = executeDeleteTruncateStatement(paramSession);
      } else {
        localResult = executeDeleteStatement(paramSession);
      }
      break;
    default: 
      throw Error.runtimeError(201, "StatementDML");
    }
    paramSession.sessionContext.diagnosticsVariables[2] = Integer.valueOf(localResult.getUpdateCount());
    return localResult;
  }
  
  void collectTableNamesForRead(OrderedHashSet paramOrderedHashSet)
  {
    Object localObject;
    if (this.baseTable.isView())
    {
      getTriggerTableNames(paramOrderedHashSet, false);
    }
    else if (!this.baseTable.isTemp())
    {
      for (i = 0; i < this.baseTable.fkConstraints.length; i++)
      {
        localObject = this.baseTable.fkConstraints[i];
        switch (this.type)
        {
        case 92: 
          if (ArrayUtil.haveCommonElement(((Constraint)localObject).getRefColumns(), this.updateColumnMap)) {
            paramOrderedHashSet.add(this.baseTable.fkConstraints[i].getMain().getName());
          }
          break;
        case 55: 
          paramOrderedHashSet.add(this.baseTable.fkConstraints[i].getMain().getName());
          break;
        case 56: 
          if ((this.updateColumnMap != null) && (ArrayUtil.haveCommonElement(((Constraint)localObject).getRefColumns(), this.updateColumnMap))) {
            paramOrderedHashSet.add(this.baseTable.fkConstraints[i].getMain().getName());
          }
          if (this.insertExpression != null) {
            paramOrderedHashSet.add(this.baseTable.fkConstraints[i].getMain().getName());
          }
          break;
        }
      }
      if ((this.type == 92) || (this.type == 56)) {
        this.baseTable.collectFKReadLocks(this.updateColumnMap, paramOrderedHashSet);
      } else if (this.type == 19) {
        this.baseTable.collectFKReadLocks(null, paramOrderedHashSet);
      }
      getTriggerTableNames(paramOrderedHashSet, false);
    }
    for (int i = 0; i < this.rangeVariables.length; i++)
    {
      localObject = this.rangeVariables[i].rangeTable;
      HsqlNameManager.HsqlName localHsqlName = ((Table)localObject).getName();
      if ((!((Table)localObject).isDataReadOnly()) && (!((Table)localObject).isTemp()) && (localHsqlName.schema != SqlInvariants.SYSTEM_SCHEMA_HSQLNAME)) {
        paramOrderedHashSet.add(localHsqlName);
      }
    }
    for (i = 0; i < this.subqueries.length; i++) {
      if (this.subqueries[i].queryExpression != null) {
        this.subqueries[i].queryExpression.getBaseTableNames(paramOrderedHashSet);
      }
    }
    for (i = 0; i < this.routines.length; i++) {
      paramOrderedHashSet.addAll(this.routines[i].getTableNamesForRead());
    }
  }
  
  void collectTableNamesForWrite(OrderedHashSet paramOrderedHashSet)
  {
    if (this.baseTable.isView())
    {
      getTriggerTableNames(paramOrderedHashSet, true);
    }
    else if (!this.baseTable.isTemp())
    {
      paramOrderedHashSet.add(this.baseTable.getName());
      if ((this.type == 92) || (this.type == 56))
      {
        if (this.updateExpressions.length != 0) {
          this.baseTable.collectFKWriteLocks(this.updateColumnMap, paramOrderedHashSet);
        }
      }
      else if (this.type == 19) {
        this.baseTable.collectFKWriteLocks(null, paramOrderedHashSet);
      }
      getTriggerTableNames(paramOrderedHashSet, true);
    }
  }
  
  public void setGeneratedColumnInfo(int paramInt, ResultMetaData paramResultMetaData)
  {
    if (this.type != 55) {
      return;
    }
    int i = this.baseTable.getIdentityColumnIndex();
    this.generatedType = paramInt;
    this.generatedInputMetaData = paramResultMetaData;
    int j;
    int m;
    switch (paramInt)
    {
    case 2: 
      return;
    case 21: 
      this.generatedIndexes = paramResultMetaData.getGeneratedColumnIndexes();
      for (j = 0; j < this.generatedIndexes.length; j++) {
        if ((this.generatedIndexes[j] < 0) || (this.generatedIndexes[j] >= this.baseTable.getColumnCount())) {
          throw Error.error(5501);
        }
      }
      break;
    case 1: 
      if (this.baseTable.hasGeneratedColumn())
      {
        if (i >= 0)
        {
          j = ArrayUtil.countTrueElements(this.baseTable.colGenerated) + 1;
          this.generatedIndexes = new int[j];
          m = 0;
          int n = 0;
          while (m < this.baseTable.colGenerated.length)
          {
            if ((this.baseTable.colGenerated[m] != 0) || (m == i)) {
              this.generatedIndexes[(n++)] = m;
            }
            m++;
          }
        }
        else
        {
          this.generatedIndexes = ArrayUtil.booleanArrayToIntIndexes(this.baseTable.colGenerated);
        }
      }
      else if (i >= 0) {
        this.generatedIndexes = new int[] { i };
      } else {
        return;
      }
      break;
    case 11: 
      String[] arrayOfString = paramResultMetaData.getGeneratedColumnNames();
      this.generatedIndexes = this.baseTable.getColumnIndexes(arrayOfString);
      for (m = 0; m < this.generatedIndexes.length; m++) {
        if (this.generatedIndexes[m] < 0) {
          throw Error.error(5501, arrayOfString[0]);
        }
      }
    }
    this.generatedResultMetaData = ResultMetaData.newResultMetaData(this.generatedIndexes.length);
    for (int k = 0; k < this.generatedIndexes.length; k++)
    {
      ColumnSchema localColumnSchema = this.baseTable.getColumn(this.generatedIndexes[k]);
      this.generatedResultMetaData.columns[k] = localColumnSchema;
    }
    this.generatedResultMetaData.prepareData();
    this.isSimpleInsert = false;
  }
  
  Object[] getGeneratedColumns(Object[] paramArrayOfObject)
  {
    if (this.generatedIndexes == null) {
      return null;
    }
    Object[] arrayOfObject = new Object[this.generatedIndexes.length];
    for (int i = 0; i < this.generatedIndexes.length; i++) {
      arrayOfObject[i] = paramArrayOfObject[this.generatedIndexes[i]];
    }
    return arrayOfObject;
  }
  
  public boolean hasGeneratedColumns()
  {
    return this.generatedIndexes != null;
  }
  
  public ResultMetaData generatedResultMetaData()
  {
    return this.generatedResultMetaData;
  }
  
  void getTriggerTableNames(OrderedHashSet paramOrderedHashSet, boolean paramBoolean)
  {
    for (int i = 0; i < this.baseTable.triggerList.length; i++)
    {
      TriggerDef localTriggerDef = this.baseTable.triggerList[i];
      switch (this.type)
      {
      case 55: 
        if (localTriggerDef.getStatementType() != 55) {
          continue;
        }
        break;
      case 92: 
        if (localTriggerDef.getStatementType() != 92) {
          continue;
        }
        break;
      case 19: 
        if (localTriggerDef.getStatementType() != 19) {
          continue;
        }
      case 56: 
        if ((goto 143) && (localTriggerDef.getStatementType() != 55)) {
          if (localTriggerDef.getStatementType() != 92) {
            continue;
          }
        }
        break;
      default: 
        throw Error.runtimeError(201, "StatementDML");
      }
      if (localTriggerDef.routine != null) {
        if (paramBoolean) {
          paramOrderedHashSet.addAll(localTriggerDef.routine.getTableNamesForWrite());
        } else {
          paramOrderedHashSet.addAll(localTriggerDef.routine.getTableNamesForRead());
        }
      }
    }
  }
  
  Result executeUpdateStatement(Session paramSession)
  {
    int i = 0;
    RowSetNavigatorDataChange localRowSetNavigatorDataChange = paramSession.sessionContext.getRowSetDataChange();
    Type[] arrayOfType = this.baseTable.getColumnTypes();
    RangeIterator localRangeIterator = RangeVariable.getIterator(paramSession, this.targetRangeVariables);
    Result localResult = null;
    RowSetNavigator localRowSetNavigator = null;
    if (this.generatedIndexes != null)
    {
      localResult = Result.newUpdateCountResult(this.generatedResultMetaData, 0);
      localRowSetNavigator = localResult.getChainedResult().getNavigator();
    }
    paramSession.sessionContext.rownum = 1;
    int j = 0;
    while (localRangeIterator.next())
    {
      paramSession.sessionData.startRowProcessing();
      Row localRow = localRangeIterator.getCurrentRow();
      Object[] arrayOfObject1 = localRow.getData();
      Object[] arrayOfObject2 = getUpdatedData(paramSession, this.targets, this.baseTable, this.updateColumnMap, this.updateExpressions, arrayOfType, arrayOfObject1);
      if (this.updatableTableCheck != null)
      {
        localRangeIterator.setCurrent(arrayOfObject2);
        boolean bool = this.updatableTableCheck.testCondition(paramSession);
        if (!bool)
        {
          localRangeIterator.release();
          throw Error.error(5700);
        }
      }
      localRowSetNavigatorDataChange.addRow(paramSession, localRow, arrayOfObject2, arrayOfType, this.updateColumnMap);
      paramSession.sessionContext.rownum += 1;
      j++;
      if (j == this.limit) {
        break;
      }
    }
    localRowSetNavigatorDataChange.endMainDataSet();
    localRangeIterator.release();
    localRowSetNavigatorDataChange.beforeFirst();
    i = update(paramSession, this.baseTable, localRowSetNavigatorDataChange, localRowSetNavigator);
    if (localResult == null)
    {
      if (i == 1) {
        return Result.updateOneResult;
      }
      if (i == 0)
      {
        paramSession.addWarning(HsqlException.noDataCondition);
        return Result.updateZeroResult;
      }
      return new Result(1, i);
    }
    localResult.setUpdateCount(i);
    if (i == 0) {
      paramSession.addWarning(HsqlException.noDataCondition);
    }
    return localResult;
  }
  
  static Object[] getUpdatedData(Session paramSession, Expression[] paramArrayOfExpression1, Table paramTable, int[] paramArrayOfInt, Expression[] paramArrayOfExpression2, Type[] paramArrayOfType, Object[] paramArrayOfObject)
  {
    Object[] arrayOfObject1 = paramTable.getEmptyRowData();
    System.arraycopy(paramArrayOfObject, 0, arrayOfObject1, 0, arrayOfObject1.length);
    int i = 0;
    int j = 0;
    while (i < paramArrayOfInt.length)
    {
      Expression localExpression = paramArrayOfExpression2[(j++)];
      Object[] arrayOfObject2;
      int m;
      int n;
      Object localObject2;
      if (localExpression.getType() == 25)
      {
        arrayOfObject2 = localExpression.getRowValue(paramSession);
        m = 0;
        while (m < arrayOfObject2.length)
        {
          n = paramArrayOfInt[i];
          localObject2 = localExpression.nodes[m];
          if ((paramTable.identityColumn != n) || (((Expression)localObject2).getType() != 1) || (((Expression)localObject2).valueData != null)) {
            if (((Expression)localObject2).getType() == 4)
            {
              if (paramTable.identityColumn != n) {
                if (paramTable.colDefaults[n] == null) {
                  arrayOfObject1[n] = null;
                } else {
                  arrayOfObject1[n] = paramTable.colDefaults[n].getValue(paramSession);
                }
              }
            }
            else {
              arrayOfObject1[n] = paramArrayOfType[n].convertToType(paramSession, arrayOfObject2[m], ((Expression)localObject2).dataType);
            }
          }
          m++;
          i++;
        }
      }
      else if (localExpression.getType() == 22)
      {
        arrayOfObject2 = localExpression.getRowValue(paramSession);
        m = 0;
        while (m < arrayOfObject2.length)
        {
          n = paramArrayOfInt[i];
          localObject2 = localExpression.table.queryExpression.getMetaData().columnTypes[m];
          arrayOfObject1[n] = paramArrayOfType[n].convertToType(paramSession, arrayOfObject2[m], (Type)localObject2);
          m++;
          i++;
        }
      }
      else
      {
        int k = paramArrayOfInt[i];
        if (localExpression.getType() == 4)
        {
          if (paramTable.identityColumn == k)
          {
            i++;
          }
          else
          {
            if (paramTable.colDefaults[k] == null) {
              arrayOfObject1[k] = null;
            } else {
              arrayOfObject1[k] = paramTable.colDefaults[k].getValue(paramSession);
            }
            i++;
          }
        }
        else
        {
          Object localObject1 = localExpression.getValue(paramSession);
          if (paramArrayOfExpression1[i].getType() == 95) {
            arrayOfObject1[k] = ((ExpressionAccessor)paramArrayOfExpression1[i]).getUpdatedArray(paramSession, (Object[])(Object[])arrayOfObject1[k], localObject1, true);
          } else {
            arrayOfObject1[k] = paramArrayOfType[k].convertToType(paramSession, localObject1, localExpression.dataType);
          }
          i++;
        }
      }
    }
    return arrayOfObject1;
  }
  
  Result executeMergeStatement(Session paramSession)
  {
    Type[] arrayOfType = this.baseTable.getColumnTypes();
    Result localResult = null;
    RowSetNavigator localRowSetNavigator = null;
    int i = (this.mergeDeleteCondition != null) || (this.updateExpressions.length != 0) ? 1 : 0;
    if (this.generatedIndexes != null)
    {
      localResult = Result.newUpdateCountResult(this.generatedResultMetaData, 0);
      localRowSetNavigator = localResult.getChainedResult().getNavigator();
    }
    int j = 0;
    RowSetNavigatorClient localRowSetNavigatorClient = new RowSetNavigatorClient(8);
    RowSetNavigatorDataChange localRowSetNavigatorDataChange = paramSession.sessionContext.getRowSetDataChange();
    RangeVariable[] arrayOfRangeVariable = this.targetRangeVariables;
    RangeIterator[] arrayOfRangeIterator = new RangeIterator[arrayOfRangeVariable.length];
    for (int k = 0; k < arrayOfRangeVariable.length; k++) {
      arrayOfRangeIterator[k] = arrayOfRangeVariable[k].getIterator(paramSession);
    }
    k = 0;
    while (k >= 0)
    {
      RangeIterator localRangeIterator = arrayOfRangeIterator[k];
      boolean bool1 = localRangeIterator.isBeforeFirst();
      Object localObject;
      if (localRangeIterator.next())
      {
        if (k < arrayOfRangeVariable.length - 1) {
          k++;
        }
      }
      else
      {
        if ((k == 1) && (bool1) && (this.insertExpression != null))
        {
          localObject = getInsertData(paramSession, arrayOfType, this.insertExpression.nodes[0].nodes);
          if ((localObject != null) && (this.mergeInsertCondition.testCondition(paramSession))) {
            localRowSetNavigatorClient.add((Object[])localObject);
          }
        }
        localRangeIterator.reset();
        k--;
        continue;
      }
      if (i != 0)
      {
        localObject = localRangeIterator.getCurrentRow();
        paramSession.sessionData.startRowProcessing();
        try
        {
          boolean bool2 = false;
          if ((this.isMergeDeleteFirst) && (this.mergeDeleteCondition != null))
          {
            bool2 = this.mergeDeleteCondition.testCondition(paramSession);
            if (bool2) {
              localRowSetNavigatorDataChange.addRow((Row)localObject);
            }
          }
          if ((!bool2) && (this.mergeUpdateCondition != null))
          {
            bool2 = this.mergeUpdateCondition.testCondition(paramSession);
            if (bool2)
            {
              Object[] arrayOfObject = getUpdatedData(paramSession, this.targets, this.baseTable, this.updateColumnMap, this.updateExpressions, arrayOfType, ((Row)localObject).getData());
              localRowSetNavigatorDataChange.addRow(paramSession, (Row)localObject, arrayOfObject, arrayOfType, this.updateColumnMap);
            }
          }
          if ((!bool2) && (!this.isMergeDeleteFirst) && (this.mergeDeleteCondition != null))
          {
            bool2 = this.mergeDeleteCondition.testCondition(paramSession);
            if (bool2) {
              localRowSetNavigatorDataChange.addRow((Row)localObject);
            }
          }
        }
        catch (HsqlException localHsqlException)
        {
          for (int m = 0; m < arrayOfRangeVariable.length; m++) {
            arrayOfRangeIterator[m].reset();
          }
          throw Error.error(3201);
        }
      }
    }
    localRowSetNavigatorDataChange.endMainDataSet();
    for (k = 0; k < arrayOfRangeVariable.length; k++) {
      arrayOfRangeIterator[k].reset();
    }
    if (i != 0) {
      j = update(paramSession, this.baseTable, localRowSetNavigatorDataChange, localRowSetNavigator);
    }
    if (localRowSetNavigatorClient.getSize() > 0)
    {
      insertRowSet(paramSession, localRowSetNavigator, localRowSetNavigatorClient);
      j += localRowSetNavigatorClient.getSize();
    }
    if ((this.insertExpression != null) && (this.baseTable.triggerLists[0].length > 0)) {
      this.baseTable.fireTriggers(paramSession, 0, localRowSetNavigatorClient);
    }
    if (localResult == null)
    {
      if (j == 1) {
        return Result.updateOneResult;
      }
      if (j == 0)
      {
        paramSession.addWarning(HsqlException.noDataCondition);
        return Result.updateZeroResult;
      }
      return new Result(1, j);
    }
    localResult.setUpdateCount(j);
    if (j == 0) {
      paramSession.addWarning(HsqlException.noDataCondition);
    }
    return localResult;
  }
  
  void insertRowSet(Session paramSession, RowSetNavigator paramRowSetNavigator1, RowSetNavigator paramRowSetNavigator2)
  {
    PersistentStore localPersistentStore = this.baseTable.getRowStore(paramSession);
    RangeVariable.RangeIteratorMain localRangeIteratorMain = null;
    if (this.updatableTableCheck != null) {
      localRangeIteratorMain = this.checkRangeVariable.getIterator(paramSession);
    }
    paramRowSetNavigator2.beforeFirst();
    Object[] arrayOfObject1;
    if (this.baseTable.identityColumn != -1)
    {
      while (paramRowSetNavigator2.hasNext())
      {
        arrayOfObject1 = paramRowSetNavigator2.getNext();
        paramSession.sessionData.startRowProcessing();
        this.baseTable.setIdentityColumn(paramSession, arrayOfObject1);
      }
      paramRowSetNavigator2.beforeFirst();
    }
    if (this.baseTable.triggerLists[6].length > 0)
    {
      while (paramRowSetNavigator2.hasNext())
      {
        arrayOfObject1 = paramRowSetNavigator2.getNext();
        paramSession.sessionData.startRowProcessing();
        this.baseTable.fireTriggers(paramSession, 6, null, arrayOfObject1, null);
      }
      paramRowSetNavigator2.beforeFirst();
    }
    while (paramRowSetNavigator2.hasNext())
    {
      arrayOfObject1 = paramRowSetNavigator2.getNext();
      paramSession.sessionData.startRowProcessing();
      this.baseTable.insertSingleRow(paramSession, localPersistentStore, arrayOfObject1, null);
      if (localRangeIteratorMain != null)
      {
        localRangeIteratorMain.setCurrent(arrayOfObject1);
        boolean bool = this.updatableTableCheck.testCondition(paramSession);
        if (!bool) {
          throw Error.error(5700);
        }
      }
      if (paramRowSetNavigator1 != null)
      {
        Object[] arrayOfObject2 = getGeneratedColumns(arrayOfObject1);
        paramRowSetNavigator1.add(arrayOfObject2);
      }
    }
    paramRowSetNavigator2.beforeFirst();
    while (paramRowSetNavigator2.hasNext())
    {
      arrayOfObject1 = paramRowSetNavigator2.getNext();
      performIntegrityChecks(paramSession, this.baseTable, null, arrayOfObject1, null);
    }
    paramRowSetNavigator2.beforeFirst();
    if (this.baseTable.triggerLists[3].length > 0)
    {
      while (paramRowSetNavigator2.hasNext())
      {
        arrayOfObject1 = paramRowSetNavigator2.getNext();
        this.baseTable.fireTriggers(paramSession, 3, null, arrayOfObject1, null);
      }
      paramRowSetNavigator2.beforeFirst();
    }
  }
  
  Result insertSingleRow(Session paramSession, PersistentStore paramPersistentStore, Object[] paramArrayOfObject)
  {
    paramSession.sessionData.startRowProcessing();
    this.baseTable.setIdentityColumn(paramSession, paramArrayOfObject);
    if (this.baseTable.triggerLists[6].length > 0) {
      this.baseTable.fireTriggers(paramSession, 6, null, paramArrayOfObject, null);
    }
    this.baseTable.insertSingleRow(paramSession, paramPersistentStore, paramArrayOfObject, null);
    performIntegrityChecks(paramSession, this.baseTable, null, paramArrayOfObject, null);
    if (paramSession.database.isReferentialIntegrity())
    {
      int i = 0;
      int j = this.baseTable.fkConstraints.length;
      while (i < j)
      {
        this.baseTable.fkConstraints[i].checkInsert(paramSession, this.baseTable, paramArrayOfObject, true);
        i++;
      }
    }
    if (this.baseTable.triggerLists[3].length > 0) {
      this.baseTable.fireTriggers(paramSession, 3, null, paramArrayOfObject, null);
    }
    if (this.baseTable.triggerLists[0].length > 0) {
      this.baseTable.fireTriggers(paramSession, 0, (RowSetNavigator)null);
    }
    paramSession.sessionContext.diagnosticsVariables[2] = Integer.valueOf(1);
    return Result.updateOneResult;
  }
  
  Object[] getInsertData(Session paramSession, Type[] paramArrayOfType, Expression[] paramArrayOfExpression)
  {
    Object[] arrayOfObject = this.baseTable.getNewRowData(paramSession);
    paramSession.sessionData.startRowProcessing();
    for (int i = 0; i < paramArrayOfExpression.length; i++)
    {
      Expression localExpression = paramArrayOfExpression[i];
      int j = this.insertColumnMap[i];
      if (localExpression.opType == 4)
      {
        if ((this.baseTable.identityColumn != j) && (this.baseTable.colDefaults[j] != null)) {
          arrayOfObject[j] = this.baseTable.colDefaults[j].getValue(paramSession);
        }
      }
      else
      {
        Object localObject = localExpression.getValue(paramSession);
        Type localType = paramArrayOfType[j];
        if ((paramSession.database.sqlSyntaxMys) || (paramSession.database.sqlSyntaxPgs)) {
          try
          {
            localObject = localType.convertToType(paramSession, localObject, localExpression.dataType);
          }
          catch (HsqlException localHsqlException)
          {
            if (localType.typeCode == 91)
            {
              localObject = Type.SQL_TIMESTAMP.convertToType(paramSession, localObject, localExpression.dataType);
              localObject = localType.convertToType(paramSession, localObject, Type.SQL_TIMESTAMP);
            }
            else if (localType.typeCode == 93)
            {
              localObject = Type.SQL_DATE.convertToType(paramSession, localObject, localExpression.dataType);
              localObject = localType.convertToType(paramSession, localObject, Type.SQL_DATE);
            }
            else
            {
              throw localHsqlException;
            }
          }
        } else if ((localExpression.dataType == null) || (localType.typeDataGroup != localExpression.dataType.typeDataGroup) || (localType.isArrayType())) {
          localObject = localType.convertToType(paramSession, localObject, localExpression.dataType);
        }
        arrayOfObject[j] = localObject;
      }
    }
    return arrayOfObject;
  }
  
  int update(Session paramSession, Table paramTable, RowSetNavigatorDataChange paramRowSetNavigatorDataChange, RowSetNavigator paramRowSetNavigator)
  {
    int i = paramRowSetNavigatorDataChange.getSize();
    boolean bool = paramTable.hasUpdatedColumn(this.updateColumnMap);
    for (int j = 0; j < i; j++)
    {
      paramRowSetNavigatorDataChange.next();
      Object[] arrayOfObject1 = paramRowSetNavigatorDataChange.getCurrentChangedData();
      paramSession.sessionData.startRowProcessing();
      paramTable.setIdentityColumn(paramSession, arrayOfObject1);
      paramTable.setGeneratedColumns(paramSession, arrayOfObject1);
      if (bool) {
        paramTable.setUpdatedColumns(paramSession, arrayOfObject1);
      }
    }
    paramRowSetNavigatorDataChange.beforeFirst();
    Object localObject3;
    Object localObject4;
    if (paramTable.fkMainConstraints.length > 0)
    {
      localObject1 = paramSession.sessionContext.getConstraintPath();
      for (int k = 0; k < i; k++)
      {
        paramRowSetNavigatorDataChange.next();
        localObject3 = paramRowSetNavigatorDataChange.getCurrentRow();
        localObject4 = paramRowSetNavigatorDataChange.getCurrentChangedData();
        performReferentialActions(paramSession, paramRowSetNavigatorDataChange, (Row)localObject3, (Object[])localObject4, this.updateColumnMap, (HashSet)localObject1, false);
        ((HashSet)localObject1).clear();
      }
      paramRowSetNavigatorDataChange.beforeFirst();
    }
    Object localObject2;
    while (paramRowSetNavigatorDataChange.next())
    {
      localObject1 = paramRowSetNavigatorDataChange.getCurrentRow();
      localObject2 = paramRowSetNavigatorDataChange.getCurrentChangedData();
      localObject3 = paramRowSetNavigatorDataChange.getCurrentChangedColumns();
      localObject4 = (Table)((Row)localObject1).getTable();
      if ((localObject4 instanceof TableDerived)) {
        localObject4 = ((TableDerived)localObject4).view;
      }
      if (localObject4.triggerLists[8].length > 0)
      {
        paramSession.sessionData.startRowProcessing();
        ((Table)localObject4).fireTriggers(paramSession, 8, ((Row)localObject1).getData(), (Object[])localObject2, (int[])localObject3);
        ((Table)localObject4).enforceRowConstraints(paramSession, (Object[])localObject2);
      }
    }
    if (paramTable.isView) {
      return i;
    }
    paramRowSetNavigatorDataChange.beforeFirst();
    while (paramRowSetNavigatorDataChange.next())
    {
      localObject1 = paramRowSetNavigatorDataChange.getCurrentRow();
      localObject2 = (Table)((Row)localObject1).getTable();
      localObject3 = paramRowSetNavigatorDataChange.getCurrentChangedColumns();
      localObject4 = ((Table)localObject2).getRowStore(paramSession);
      paramSession.addDeleteAction((Table)localObject2, (PersistentStore)localObject4, (Row)localObject1, (int[])localObject3);
    }
    paramRowSetNavigatorDataChange.beforeFirst();
    Object localObject5;
    Object localObject6;
    while (paramRowSetNavigatorDataChange.next())
    {
      localObject1 = paramRowSetNavigatorDataChange.getCurrentRow();
      localObject2 = paramRowSetNavigatorDataChange.getCurrentChangedData();
      localObject3 = (Table)((Row)localObject1).getTable();
      localObject4 = paramRowSetNavigatorDataChange.getCurrentChangedColumns();
      localObject5 = ((Table)localObject3).getRowStore(paramSession);
      if (localObject2 != null)
      {
        localObject6 = ((Table)localObject3).insertSingleRow(paramSession, (PersistentStore)localObject5, (Object[])localObject2, (int[])localObject4);
        if (paramRowSetNavigator != null)
        {
          Object[] arrayOfObject2 = getGeneratedColumns((Object[])localObject2);
          paramRowSetNavigator.add(arrayOfObject2);
        }
      }
    }
    paramRowSetNavigatorDataChange.beforeFirst();
    Object localObject1 = null;
    int m = paramTable.triggerLists[5].length > 0 ? 1 : 0;
    while (paramRowSetNavigatorDataChange.next())
    {
      localObject3 = paramRowSetNavigatorDataChange.getCurrentRow();
      localObject4 = (Table)((Row)localObject3).getTable();
      localObject5 = paramRowSetNavigatorDataChange.getCurrentChangedData();
      localObject6 = paramRowSetNavigatorDataChange.getCurrentChangedColumns();
      performIntegrityChecks(paramSession, (Table)localObject4, ((Row)localObject3).getData(), (Object[])localObject5, (int[])localObject6);
      if (localObject4 != paramTable)
      {
        if (localObject1 == null) {
          localObject1 = new OrderedHashSet();
        }
        ((OrderedHashSet)localObject1).add(localObject4);
        if (localObject4.triggerLists[5].length > 0) {
          m = 1;
        }
      }
    }
    paramRowSetNavigatorDataChange.beforeFirst();
    if (m != 0)
    {
      while (paramRowSetNavigatorDataChange.next())
      {
        localObject3 = paramRowSetNavigatorDataChange.getCurrentRow();
        localObject4 = paramRowSetNavigatorDataChange.getCurrentChangedData();
        localObject5 = paramRowSetNavigatorDataChange.getCurrentChangedColumns();
        localObject6 = (Table)((Row)localObject3).getTable();
        ((Table)localObject6).fireTriggers(paramSession, 5, ((Row)localObject3).getData(), (Object[])localObject4, (int[])localObject5);
      }
      paramRowSetNavigatorDataChange.beforeFirst();
    }
    this.baseTable.fireTriggers(paramSession, 2, paramRowSetNavigatorDataChange);
    if (localObject1 != null) {
      for (int n = 0; n < ((OrderedHashSet)localObject1).size(); n++)
      {
        localObject4 = (Table)((OrderedHashSet)localObject1).get(n);
        ((Table)localObject4).fireTriggers(paramSession, 2, paramRowSetNavigatorDataChange);
      }
    }
    return i;
  }
  
  Result executeDeleteStatement(Session paramSession)
  {
    int i = 0;
    RangeIterator localRangeIterator = RangeVariable.getIterator(paramSession, this.targetRangeVariables);
    RowSetNavigatorDataChange localRowSetNavigatorDataChange = paramSession.sessionContext.getRowSetDataChange();
    paramSession.sessionContext.rownum = 1;
    int j = 0;
    while (localRangeIterator.next())
    {
      Row localRow = localRangeIterator.getCurrentRow();
      localRowSetNavigatorDataChange.addRow(localRow);
      paramSession.sessionContext.rownum += 1;
      j++;
      if (j == this.limit) {
        break;
      }
    }
    localRangeIterator.release();
    localRowSetNavigatorDataChange.endMainDataSet();
    if (localRowSetNavigatorDataChange.getSize() > 0)
    {
      i = delete(paramSession, this.baseTable, localRowSetNavigatorDataChange);
    }
    else
    {
      paramSession.addWarning(HsqlException.noDataCondition);
      return Result.updateZeroResult;
    }
    if (i == 1) {
      return Result.updateOneResult;
    }
    return new Result(1, i);
  }
  
  Result executeDeleteTruncateStatement(Session paramSession)
  {
    PersistentStore localPersistentStore = this.targetTable.getRowStore(paramSession);
    RowIterator localRowIterator = this.targetTable.getPrimaryIndex().firstRow(localPersistentStore);
    boolean bool = localRowIterator.hasNext();
    for (int i = 0; i < this.targetTable.fkMainConstraints.length; i++) {
      if (this.targetTable.fkMainConstraints[i].getRef() != this.targetTable)
      {
        HsqlNameManager.HsqlName localHsqlName = this.targetTable.fkMainConstraints[i].getRef().getName();
        Table localTable = paramSession.database.schemaManager.getUserTable(localHsqlName);
        if (!localTable.isEmpty(paramSession)) {
          throw Error.error(8, localTable.getName().name);
        }
      }
    }
    try
    {
      while (localRowIterator.hasNext())
      {
        Row localRow = localRowIterator.getNextRow();
        paramSession.addDeleteAction((Table)localRow.getTable(), localPersistentStore, localRow, null);
      }
      if ((this.restartIdentity) && (this.targetTable.identitySequence != null)) {
        this.targetTable.identitySequence.reset();
      }
    }
    finally
    {
      localRowIterator.release();
    }
    if (!bool) {
      paramSession.addWarning(HsqlException.noDataCondition);
    }
    return Result.updateOneResult;
  }
  
  int delete(Session paramSession, Table paramTable, RowSetNavigatorDataChange paramRowSetNavigatorDataChange)
  {
    int i = paramRowSetNavigatorDataChange.getSize();
    paramRowSetNavigatorDataChange.beforeFirst();
    Object localObject1;
    Object localObject4;
    if (paramTable.fkMainConstraints.length > 0)
    {
      localObject1 = paramSession.sessionContext.getConstraintPath();
      if (paramTable.cascadingDeletes > 0)
      {
        for (k = 0; k < i; k++)
        {
          paramRowSetNavigatorDataChange.next();
          Row localRow = paramRowSetNavigatorDataChange.getCurrentRow();
          performReferentialActions(paramSession, paramRowSetNavigatorDataChange, localRow, null, null, (HashSet)localObject1, true);
          ((HashSet)localObject1).clear();
        }
        paramRowSetNavigatorDataChange.beforeFirst();
      }
      int k = paramRowSetNavigatorDataChange.getSize();
      for (int m = 0; m < k; m++)
      {
        paramRowSetNavigatorDataChange.next();
        localObject4 = paramRowSetNavigatorDataChange.getCurrentRow();
        performReferentialActions(paramSession, paramRowSetNavigatorDataChange, (Row)localObject4, null, null, (HashSet)localObject1, false);
        ((HashSet)localObject1).clear();
      }
      paramRowSetNavigatorDataChange.beforeFirst();
    }
    while (paramRowSetNavigatorDataChange.next())
    {
      localObject1 = paramRowSetNavigatorDataChange.getCurrentRow();
      localObject2 = paramRowSetNavigatorDataChange.getCurrentChangedData();
      localObject3 = paramRowSetNavigatorDataChange.getCurrentChangedColumns();
      localObject4 = (Table)((Row)localObject1).getTable();
      if ((localObject4 instanceof TableDerived)) {
        localObject4 = ((TableDerived)localObject4).view;
      }
      if (localObject2 == null) {
        ((Table)localObject4).fireTriggers(paramSession, 7, ((Row)localObject1).getData(), null, null);
      } else {
        ((Table)localObject4).fireTriggers(paramSession, 8, ((Row)localObject1).getData(), (Object[])localObject2, (int[])localObject3);
      }
    }
    if (paramTable.isView) {
      return i;
    }
    paramRowSetNavigatorDataChange.beforeFirst();
    int j = 0;
    Object localObject5;
    while (paramRowSetNavigatorDataChange.next())
    {
      localObject2 = paramRowSetNavigatorDataChange.getCurrentRow();
      localObject3 = paramRowSetNavigatorDataChange.getCurrentChangedData();
      localObject4 = (Table)((Row)localObject2).getTable();
      localObject5 = ((Table)localObject4).getRowStore(paramSession);
      paramSession.addDeleteAction((Table)localObject4, (PersistentStore)localObject5, (Row)localObject2, null);
      if (localObject3 != null) {
        j = 1;
      }
    }
    paramRowSetNavigatorDataChange.beforeFirst();
    Object localObject6;
    Object localObject7;
    if (j != 0)
    {
      while (paramRowSetNavigatorDataChange.next())
      {
        localObject2 = paramRowSetNavigatorDataChange.getCurrentRow();
        localObject3 = paramRowSetNavigatorDataChange.getCurrentChangedData();
        localObject4 = (Table)((Row)localObject2).getTable();
        localObject5 = paramRowSetNavigatorDataChange.getCurrentChangedColumns();
        localObject6 = ((Table)localObject4).getRowStore(paramSession);
        if (localObject3 != null) {
          localObject7 = ((Table)localObject4).insertSingleRow(paramSession, (PersistentStore)localObject6, (Object[])localObject3, (int[])localObject5);
        }
      }
      paramRowSetNavigatorDataChange.beforeFirst();
    }
    Object localObject2 = null;
    Object localObject3 = null;
    int n = paramTable.triggerLists[4].length > 0 ? 1 : 0;
    if (i != paramRowSetNavigatorDataChange.getSize())
    {
      while (paramRowSetNavigatorDataChange.next())
      {
        localObject5 = paramRowSetNavigatorDataChange.getCurrentRow();
        localObject6 = paramRowSetNavigatorDataChange.getCurrentChangedData();
        localObject7 = paramRowSetNavigatorDataChange.getCurrentChangedColumns();
        Table localTable = (Table)((Row)localObject5).getTable();
        if (localObject6 != null) {
          performIntegrityChecks(paramSession, localTable, ((Row)localObject5).getData(), (Object[])localObject6, (int[])localObject7);
        }
        if (localTable != paramTable) {
          if (localObject6 == null)
          {
            if (localTable.triggerLists[4].length > 0) {
              n = 1;
            }
            if (localObject3 == null) {
              localObject3 = new OrderedHashSet();
            }
            ((OrderedHashSet)localObject3).add(localTable);
          }
          else
          {
            if (localTable.triggerLists[5].length > 0) {
              n = 1;
            }
            if (localObject2 == null) {
              localObject2 = new OrderedHashSet();
            }
            ((OrderedHashSet)localObject2).add(localTable);
          }
        }
      }
      paramRowSetNavigatorDataChange.beforeFirst();
    }
    if (n != 0)
    {
      while (paramRowSetNavigatorDataChange.next())
      {
        localObject5 = paramRowSetNavigatorDataChange.getCurrentRow();
        localObject6 = paramRowSetNavigatorDataChange.getCurrentChangedData();
        localObject7 = (Table)((Row)localObject5).getTable();
        if (localObject6 == null) {
          ((Table)localObject7).fireTriggers(paramSession, 4, ((Row)localObject5).getData(), null, null);
        } else {
          ((Table)localObject7).fireTriggers(paramSession, 5, ((Row)localObject5).getData(), (Object[])localObject6, null);
        }
      }
      paramRowSetNavigatorDataChange.beforeFirst();
    }
    paramTable.fireTriggers(paramSession, 1, paramRowSetNavigatorDataChange);
    int i1;
    if (localObject2 != null) {
      for (i1 = 0; i1 < ((OrderedHashSet)localObject2).size(); i1++)
      {
        localObject6 = (Table)((OrderedHashSet)localObject2).get(i1);
        ((Table)localObject6).fireTriggers(paramSession, 2, paramRowSetNavigatorDataChange);
      }
    }
    if (localObject3 != null) {
      for (i1 = 0; i1 < ((OrderedHashSet)localObject3).size(); i1++)
      {
        localObject6 = (Table)((OrderedHashSet)localObject3).get(i1);
        ((Table)localObject6).fireTriggers(paramSession, 1, paramRowSetNavigatorDataChange);
      }
    }
    return i;
  }
  
  static void performIntegrityChecks(Session paramSession, Table paramTable, Object[] paramArrayOfObject1, Object[] paramArrayOfObject2, int[] paramArrayOfInt)
  {
    if (paramArrayOfObject2 == null) {
      return;
    }
    int i = 0;
    int j = paramTable.checkConstraints.length;
    while (i < j)
    {
      paramTable.checkConstraints[i].checkInsert(paramSession, paramTable, paramArrayOfObject2, paramArrayOfObject1 == null);
      i++;
    }
    if (!paramSession.database.isReferentialIntegrity()) {
      return;
    }
    i = 0;
    j = paramTable.fkConstraints.length;
    while (i < j)
    {
      boolean bool = paramArrayOfObject1 == null;
      Constraint localConstraint = paramTable.fkConstraints[i];
      if (!bool) {
        bool = ArrayUtil.haveCommonElement(localConstraint.getRefColumns(), paramArrayOfInt);
      }
      if (bool) {
        localConstraint.checkInsert(paramSession, paramTable, paramArrayOfObject2, paramArrayOfObject1 == null);
      }
      i++;
    }
  }
  
  static void performReferentialActions(Session paramSession, RowSetNavigatorDataChange paramRowSetNavigatorDataChange, Row paramRow, Object[] paramArrayOfObject, int[] paramArrayOfInt, HashSet paramHashSet, boolean paramBoolean)
  {
    if (!paramSession.database.isReferentialIntegrity()) {
      return;
    }
    int i = paramArrayOfObject == null ? 1 : 0;
    Table localTable = (Table)paramRow.getTable();
    int j = 0;
    int k = localTable.fkMainConstraints.length;
    while (j < k)
    {
      Constraint localConstraint = localTable.fkMainConstraints[j];
      int m = i != 0 ? localConstraint.getDeleteAction() : localConstraint.getUpdateAction();
      if ((!(paramBoolean ^ ((i != 0) && (m == 0)))) && ((i != 0) || ((ArrayUtil.haveCommonElement(paramArrayOfInt, localConstraint.core.mainCols)) && (localConstraint.core.mainIndex.compareRowNonUnique(paramSession, paramRow.getData(), paramArrayOfObject, localConstraint.core.mainCols) != 0))))
      {
        RowIterator localRowIterator = localConstraint.findFkRef(paramSession, paramRow.getData());
        if (!localRowIterator.hasNext())
        {
          localRowIterator.release();
        }
        else
        {
          while (localRowIterator.hasNext())
          {
            Row localRow = localRowIterator.getNextRow();
            Object[] arrayOfObject = null;
            if (localConstraint.core.refIndex.compareRowNonUnique(paramSession, localRow.getData(), paramRow.getData(), localConstraint.core.mainCols) != 0) {
              break;
            }
            if ((i == 0) || (localRow.getId() != paramRow.getId()))
            {
              int i1;
              Object localObject;
              switch (m)
              {
              case 0: 
                int n;
                if (i != 0)
                {
                  try
                  {
                    n = paramRowSetNavigatorDataChange.addRow(localRow);
                  }
                  catch (HsqlException localHsqlException2)
                  {
                    String[] arrayOfString = getConstraintInfo(localConstraint);
                    localRowIterator.release();
                    throw Error.error(null, 3900, 2, arrayOfString);
                  }
                  if (n != 0) {
                    performReferentialActions(paramSession, paramRowSetNavigatorDataChange, localRow, null, null, paramHashSet, paramBoolean);
                  }
                }
                else
                {
                  arrayOfObject = localConstraint.core.refTable.getEmptyRowData();
                  System.arraycopy(localRow.getData(), 0, arrayOfObject, 0, arrayOfObject.length);
                  for (n = 0; n < localConstraint.core.refCols.length; n++) {
                    arrayOfObject[localConstraint.core.refCols[n]] = paramArrayOfObject[localConstraint.core.mainCols[n]];
                  }
                }
                break;
              case 2: 
                arrayOfObject = localConstraint.core.refTable.getEmptyRowData();
                System.arraycopy(localRow.getData(), 0, arrayOfObject, 0, arrayOfObject.length);
                for (i1 = 0; i1 < localConstraint.core.refCols.length; i1++) {
                  arrayOfObject[localConstraint.core.refCols[i1]] = null;
                }
                break;
              case 4: 
                arrayOfObject = localConstraint.core.refTable.getEmptyRowData();
                System.arraycopy(localRow.getData(), 0, arrayOfObject, 0, arrayOfObject.length);
                for (i1 = 0; i1 < localConstraint.core.refCols.length; i1++)
                {
                  localObject = localConstraint.core.refTable.getColumn(localConstraint.core.refCols[i1]);
                  arrayOfObject[localConstraint.core.refCols[i1]] = ((ColumnSchema)localObject).getDefaultValue(paramSession);
                }
                break;
              case 3: 
                if (i != 0 ? paramRowSetNavigatorDataChange.containsDeletedRow(localRow) : paramRowSetNavigatorDataChange.containsUpdatedRow(paramRow, localRow, localConstraint.core.mainCols)) {}
                break;
              case 1: 
                i1 = localConstraint.getDeleteAction() == 3 ? 8 : 3501;
                localObject = getConstraintInfo(localConstraint);
                localRowIterator.release();
                throw Error.error(null, i1, 2, (Object[])localObject);
              default: 
                continue;
                try
                {
                  arrayOfObject = paramRowSetNavigatorDataChange.addRow(paramSession, localRow, arrayOfObject, localConstraint.core.refTable.getColumnTypes(), localConstraint.core.refCols);
                }
                catch (HsqlException localHsqlException1)
                {
                  localObject = getConstraintInfo(localConstraint);
                  localRowIterator.release();
                  throw Error.error(null, 3900, 2, (Object[])localObject);
                }
                if ((arrayOfObject != null) && (paramHashSet.add(localConstraint)))
                {
                  performReferentialActions(paramSession, paramRowSetNavigatorDataChange, localRow, arrayOfObject, localConstraint.core.refCols, paramHashSet, paramBoolean);
                  paramHashSet.remove(localConstraint);
                }
                break;
              }
            }
          }
          localRowIterator.release();
        }
      }
      j++;
    }
  }
  
  static String[] getConstraintInfo(Constraint paramConstraint)
  {
    return new String[] { paramConstraint.core.refName.name, paramConstraint.core.refTable.getName().name };
  }
  
  public void clearStructures(Session paramSession)
  {
    paramSession.sessionContext.clearStructures(this);
  }
}


/* Location:              E:\java\java学习\hutubill\lib\all.jar!\org\hsqldb\StatementDML.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */
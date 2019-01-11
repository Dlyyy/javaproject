package org.hsqldb;

import org.hsqldb.error.Error;
import org.hsqldb.lib.ArrayUtil;
import org.hsqldb.lib.OrderedHashSet;
import org.hsqldb.map.ValuePool;
import org.hsqldb.result.Result;
import org.hsqldb.types.Type;

public class StatementSet
  extends StatementDMQL
{
  Expression expression;
  Expression[] targets;
  int[] variableIndexes;
  Type[] sourceTypes;
  final int operationType;
  public static final int TRIGGER_SET = 1;
  public static final int SELECT_INTO = 2;
  public static final int VARIABLE_SET = 3;
  
  StatementSet(Session paramSession, Expression[] paramArrayOfExpression1, Table paramTable, RangeVariable[] paramArrayOfRangeVariable, int[] paramArrayOfInt, Expression[] paramArrayOfExpression2, ParserDQL.CompileContext paramCompileContext)
  {
    super(97, 2004, paramSession.getCurrentSchemaHsqlName());
    this.operationType = 1;
    this.targets = paramArrayOfExpression1;
    this.targetTable = paramTable;
    this.baseTable = this.targetTable.getBaseTable();
    this.updateColumnMap = paramArrayOfInt;
    this.updateExpressions = paramArrayOfExpression2;
    this.updateCheckColumns = this.targetTable.getColumnCheckList(paramArrayOfInt);
    this.targetRangeVariables = paramArrayOfRangeVariable;
    this.isTransactionStatement = false;
    setDatabaseObjects(paramSession, paramCompileContext);
    checkAccessRights(paramSession);
  }
  
  StatementSet(Session paramSession, Expression[] paramArrayOfExpression, Expression paramExpression, int[] paramArrayOfInt, ParserDQL.CompileContext paramCompileContext)
  {
    super(97, 2007, null);
    this.operationType = 3;
    this.targets = paramArrayOfExpression;
    this.expression = paramExpression;
    this.variableIndexes = paramArrayOfInt;
    this.sourceTypes = this.expression.getNodeDataTypes();
    this.isTransactionStatement = false;
    setDatabaseObjects(paramSession, paramCompileContext);
    checkAccessRights(paramSession);
  }
  
  StatementSet(Session paramSession, Expression[] paramArrayOfExpression, QueryExpression paramQueryExpression, int[] paramArrayOfInt, ParserDQL.CompileContext paramCompileContext)
  {
    super(97, 2007, null);
    this.operationType = 2;
    this.queryExpression = paramQueryExpression;
    this.targets = paramArrayOfExpression;
    this.variableIndexes = paramArrayOfInt;
    this.sourceTypes = paramQueryExpression.getColumnTypes();
    this.isTransactionStatement = false;
    setDatabaseObjects(paramSession, paramCompileContext);
    checkAccessRights(paramSession);
  }
  
  TableDerived[] getSubqueries(Session paramSession)
  {
    OrderedHashSet localOrderedHashSet = null;
    if (this.expression != null) {
      localOrderedHashSet = this.expression.collectAllSubqueries(localOrderedHashSet);
    }
    if ((localOrderedHashSet == null) || (localOrderedHashSet.size() == 0)) {
      return TableDerived.emptyArray;
    }
    TableDerived[] arrayOfTableDerived = new TableDerived[localOrderedHashSet.size()];
    localOrderedHashSet.toArray(arrayOfTableDerived);
    for (int i = 0; i < this.subqueries.length; i++) {
      arrayOfTableDerived[i].prepareTable(paramSession);
    }
    return arrayOfTableDerived;
  }
  
  Result getResult(Session paramSession)
  {
    Result localResult = null;
    Object[] arrayOfObject;
    switch (this.operationType)
    {
    case 1: 
      localResult = executeTriggerSetStatement(paramSession);
      break;
    case 2: 
      arrayOfObject = this.queryExpression.getSingleRowValues(paramSession);
      if (arrayOfObject == null)
      {
        paramSession.addWarning(HsqlException.noDataCondition);
        localResult = Result.updateZeroResult;
      }
      else
      {
        localResult = performAssignment(paramSession, this.variableIndexes, this.targets, arrayOfObject, this.sourceTypes);
      }
      break;
    case 3: 
      arrayOfObject = getExpressionValues(paramSession);
      if (arrayOfObject == null) {
        localResult = Result.updateZeroResult;
      } else {
        localResult = performAssignment(paramSession, this.variableIndexes, this.targets, arrayOfObject, this.sourceTypes);
      }
      break;
    default: 
      throw Error.runtimeError(201, "StatementSet");
    }
    return localResult;
  }
  
  public void resolve(Session paramSession)
  {
    switch (this.operationType)
    {
    case 1: 
      for (int i = 0; i < this.updateExpressions.length; i++) {
        this.updateExpressions[i].collectObjectNames(this.references);
      }
      break;
    case 2: 
    case 3: 
      if (this.expression != null) {
        this.expression.collectObjectNames(this.references);
      }
      break;
    default: 
      throw Error.runtimeError(201, "StatementSet");
    }
  }
  
  public String getSQL()
  {
    StringBuffer localStringBuffer = new StringBuffer();
    switch (this.operationType)
    {
    case 1: 
      return this.sql;
    case 3: 
      localStringBuffer.append("SET").append(' ');
      localStringBuffer.append(this.targets[0].getColumn().getName().statementName);
      localStringBuffer.append(' ').append('=').append(' ').append(this.expression.getSQL());
    }
    return localStringBuffer.toString();
  }
  
  protected String describe(Session paramSession, int paramInt)
  {
    StringBuffer localStringBuffer = new StringBuffer();
    localStringBuffer.append('\n');
    for (int i = 0; i < paramInt; i++) {
      localStringBuffer.append(' ');
    }
    localStringBuffer.append("STATEMENT");
    return localStringBuffer.toString();
  }
  
  public Result execute(Session paramSession)
  {
    Result localResult;
    try
    {
      if (this.subqueries.length > 0) {
        materializeSubQueries(paramSession);
      }
      localResult = getResult(paramSession);
    }
    catch (Throwable localThrowable)
    {
      localResult = Result.newErrorResult(localThrowable);
    }
    if (localResult.isError()) {
      localResult.getException().setStatementType(this.group, this.type);
    }
    return localResult;
  }
  
  public String describe(Session paramSession)
  {
    return "";
  }
  
  Result executeTriggerSetStatement(Session paramSession)
  {
    Table localTable = this.targetTable;
    int[] arrayOfInt = this.updateColumnMap;
    Expression[] arrayOfExpression = this.updateExpressions;
    Type[] arrayOfType = localTable.getColumnTypes();
    int i = this.targetRangeVariables[1].rangePosition;
    Object[] arrayOfObject1 = paramSession.sessionContext.triggerArguments[i];
    Object[] arrayOfObject2 = StatementDML.getUpdatedData(paramSession, this.targets, localTable, arrayOfInt, arrayOfExpression, arrayOfType, arrayOfObject1);
    ArrayUtil.copyArray(arrayOfObject2, arrayOfObject1, arrayOfObject2.length);
    return Result.updateOneResult;
  }
  
  void collectTableNamesForRead(OrderedHashSet paramOrderedHashSet)
  {
    if (this.queryExpression != null) {
      this.queryExpression.getBaseTableNames(paramOrderedHashSet);
    }
    for (int i = 0; i < this.rangeVariables.length; i++)
    {
      Table localTable = this.rangeVariables[i].rangeTable;
      HsqlNameManager.HsqlName localHsqlName = localTable.getName();
      if ((!localTable.isDataReadOnly()) && (!localTable.isTemp()) && (!localTable.isView()) && (localHsqlName.schema != SqlInvariants.SYSTEM_SCHEMA_HSQLNAME)) {
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
  
  void collectTableNamesForWrite(OrderedHashSet paramOrderedHashSet) {}
  
  public void checkIsNotColumnTarget()
  {
    for (int i = 0; i < this.targets.length; i++)
    {
      ColumnSchema localColumnSchema = this.targets[i].getColumn();
      if (localColumnSchema.getType() == 9) {
        throw Error.error(2500, localColumnSchema.getName().statementName);
      }
    }
  }
  
  Object[] getExpressionValues(Session paramSession)
  {
    Object[] arrayOfObject;
    if (this.expression.getType() == 25)
    {
      arrayOfObject = this.expression.getRowValue(paramSession);
    }
    else if (this.expression.getType() == 22)
    {
      arrayOfObject = this.expression.table.queryExpression.getSingleRowValues(paramSession);
      if (arrayOfObject == null) {
        return null;
      }
    }
    else
    {
      arrayOfObject = new Object[1];
      arrayOfObject[0] = this.expression.getValue(paramSession);
    }
    return arrayOfObject;
  }
  
  static Result performAssignment(Session paramSession, int[] paramArrayOfInt, Expression[] paramArrayOfExpression, Object[] paramArrayOfObject, Type[] paramArrayOfType)
  {
    for (int i = 0; i < paramArrayOfObject.length; i++)
    {
      Object[] arrayOfObject = ValuePool.emptyObjectArray;
      switch (paramArrayOfExpression[i].getColumn().getType())
      {
      case 23: 
        arrayOfObject = paramSession.sessionContext.routineArguments;
        break;
      case 22: 
        arrayOfObject = paramSession.sessionContext.routineVariables;
        break;
      case 9: 
        arrayOfObject = paramSession.sessionContext.triggerArguments[1];
      }
      int j = paramArrayOfInt[i];
      Object localObject = paramArrayOfObject[i];
      Type localType;
      if (paramArrayOfExpression[i].getType() == 95)
      {
        localType = paramArrayOfExpression[i].getLeftNode().getColumn().getDataType().collectionBaseType();
        localObject = localType.convertToType(paramSession, localObject, paramArrayOfType[i]);
        arrayOfObject[j] = ((ExpressionAccessor)paramArrayOfExpression[i]).getUpdatedArray(paramSession, (Object[])(Object[])arrayOfObject[j], localObject, true);
      }
      else
      {
        localType = paramArrayOfExpression[i].getColumn().getDataType();
        localObject = localType.convertToType(paramSession, localObject, paramArrayOfType[i]);
        arrayOfObject[j] = localObject;
      }
    }
    return Result.updateZeroResult;
  }
}


/* Location:              E:\java\java学习\hutubill\lib\all.jar!\org\hsqldb\StatementSet.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */
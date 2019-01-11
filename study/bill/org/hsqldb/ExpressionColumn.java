package org.hsqldb;

import org.hsqldb.error.Error;
import org.hsqldb.lib.ArrayListIdentity;
import org.hsqldb.lib.HashMappedList;
import org.hsqldb.lib.HsqlList;
import org.hsqldb.lib.OrderedHashSet;
import org.hsqldb.lib.Set;
import org.hsqldb.map.ValuePool;
import org.hsqldb.navigator.RangeIterator;
import org.hsqldb.persist.PersistentStore;
import org.hsqldb.types.Type;

public class ExpressionColumn
  extends Expression
{
  public static final ExpressionColumn[] emptyArray = new ExpressionColumn[0];
  static final HsqlNameManager.SimpleName rownumName = HsqlNameManager.getSimpleName("ROWNUM", false);
  public static final HashMappedList diagnosticsList = new HashMappedList();
  static final String[] diagnosticsVariableTokens = { "NUMBER", "MORE", "ROW_COUNT" };
  public static final int idx_number = 0;
  public static final int idx_more = 1;
  public static final int idx_row_count = 2;
  ColumnSchema column;
  String schema;
  String tableName;
  String columnName;
  RangeVariable rangeVariable;
  NumberSequence sequence;
  boolean isWritable;
  boolean isParam;
  
  ExpressionColumn(String paramString1, String paramString2, String paramString3)
  {
    super(2);
    this.schema = paramString1;
    this.tableName = paramString2;
    this.columnName = paramString3;
  }
  
  ExpressionColumn(ColumnSchema paramColumnSchema)
  {
    super(2);
    this.column = paramColumnSchema;
    this.dataType = paramColumnSchema.getDataType();
    this.columnName = paramColumnSchema.getName().name;
  }
  
  ExpressionColumn(RangeVariable paramRangeVariable, int paramInt)
  {
    super(2);
    this.columnIndex = paramInt;
    setAutoAttributesAsColumn(paramRangeVariable, this.columnIndex);
  }
  
  ExpressionColumn(Expression paramExpression, int paramInt1, int paramInt2)
  {
    super(5);
    this.dataType = paramExpression.dataType;
    this.columnIndex = paramInt1;
    this.alias = paramExpression.alias;
    this.rangePosition = paramInt2;
  }
  
  ExpressionColumn()
  {
    super(11);
  }
  
  ExpressionColumn(int paramInt)
  {
    super(paramInt);
    if (paramInt == 8)
    {
      this.isParam = true;
    }
    else if (paramInt == 14)
    {
      this.columnName = rownumName.name;
      this.dataType = Type.SQL_INTEGER;
    }
  }
  
  ExpressionColumn(int paramInt1, int paramInt2)
  {
    super(paramInt1);
    this.column = ((ColumnSchema)diagnosticsList.get(paramInt2));
    this.columnIndex = paramInt2;
    this.dataType = this.column.dataType;
  }
  
  ExpressionColumn(Expression[] paramArrayOfExpression, String paramString)
  {
    super(3);
    this.nodes = paramArrayOfExpression;
    this.columnName = paramString;
  }
  
  ExpressionColumn(String paramString1, String paramString2)
  {
    super(93);
    this.schema = paramString1;
    this.tableName = paramString2;
  }
  
  ExpressionColumn(NumberSequence paramNumberSequence, int paramInt)
  {
    super(paramInt);
    this.sequence = paramNumberSequence;
    this.dataType = paramNumberSequence.getDataType();
  }
  
  void setAutoAttributesAsColumn(RangeVariable paramRangeVariable, int paramInt)
  {
    this.columnIndex = paramInt;
    this.column = paramRangeVariable.getColumn(paramInt);
    this.dataType = this.column.getDataType();
    this.columnName = paramRangeVariable.getColumnAlias(paramInt).name;
    this.tableName = paramRangeVariable.getTableAlias().name;
    this.rangeVariable = paramRangeVariable;
    this.rangeVariable.addColumn(this.columnIndex);
  }
  
  void setAttributesAsColumn(RangeVariable paramRangeVariable, int paramInt)
  {
    this.columnIndex = paramInt;
    this.column = paramRangeVariable.getColumn(paramInt);
    this.dataType = this.column.getDataType();
    this.rangeVariable = paramRangeVariable;
    this.rangeVariable.addColumn(this.columnIndex);
  }
  
  public byte getNullability()
  {
    switch (this.opType)
    {
    case 2: 
      if (this.nullability == 2) {
        return this.column.getNullability();
      }
      return this.nullability;
    case 3: 
    case 12: 
    case 14: 
      return 0;
    }
    return 2;
  }
  
  void setAttributesAsColumn(ColumnSchema paramColumnSchema, boolean paramBoolean)
  {
    this.column = paramColumnSchema;
    this.dataType = paramColumnSchema.getDataType();
    this.isWritable = paramBoolean;
  }
  
  HsqlNameManager.SimpleName getSimpleName()
  {
    if (this.alias != null) {
      return this.alias;
    }
    if ((this.rangeVariable != null) && (this.rangeVariable.hasColumnAlias())) {
      return this.rangeVariable.getColumnAlias(this.columnIndex);
    }
    if (this.column != null) {
      return this.column.getName();
    }
    if (this.opType == 3) {
      return this.nodes[0].getSimpleName();
    }
    if (this.opType == 14) {
      return rownumName;
    }
    return null;
  }
  
  String getAlias()
  {
    if (this.alias != null) {
      return this.alias.name;
    }
    switch (this.opType)
    {
    case 2: 
    case 3: 
    case 14: 
      return this.columnName;
    }
    return "";
  }
  
  void collectObjectNames(Set paramSet)
  {
    switch (this.opType)
    {
    case 12: 
      HsqlNameManager.HsqlName localHsqlName = this.sequence.getName();
      paramSet.add(localHsqlName);
      return;
    case 3: 
    case 5: 
    case 8: 
    case 11: 
    case 93: 
      break;
    case 6: 
    case 7: 
      break;
    case 2: 
      paramSet.add(this.column.getName());
      if (this.column.getName().parent != null) {
        paramSet.add(this.column.getName().parent);
      }
      return;
    }
  }
  
  String getColumnName()
  {
    switch (this.opType)
    {
    case 2: 
    case 6: 
    case 7: 
      if (this.column != null) {
        return this.column.getName().name;
      }
      if (this.columnName != null) {
        return this.columnName;
      }
      break;
    }
    return getAlias();
  }
  
  public ColumnSchema getColumn()
  {
    return this.column;
  }
  
  String getSchemaName()
  {
    return this.schema;
  }
  
  RangeVariable getRangeVariable()
  {
    return this.rangeVariable;
  }
  
  public HsqlList resolveColumnReferences(Session paramSession, RangeGroup paramRangeGroup, int paramInt, RangeGroup[] paramArrayOfRangeGroup, HsqlList paramHsqlList, boolean paramBoolean)
  {
    int i;
    switch (this.opType)
    {
    case 12: 
      if (!paramBoolean) {
        throw Error.error(5598);
      }
      break;
    case 93: 
      throw Error.error(5581, "*");
    case 5: 
    case 8: 
    case 10: 
    case 11: 
    case 14: 
      break;
    case 3: 
      for (i = 0; i < this.nodes.length; i++) {
        this.nodes[i].resolveColumnReferences(paramSession, paramRangeGroup, paramArrayOfRangeGroup, paramHsqlList);
      }
      break;
    case 2: 
    case 6: 
    case 7: 
      i = 0;
      RangeVariable[] arrayOfRangeVariable = paramRangeGroup.getRangeVariables();
      if (this.rangeVariable != null) {
        return paramHsqlList;
      }
      for (int j = 0; j < paramInt; j++)
      {
        RangeVariable localRangeVariable = arrayOfRangeVariable[j];
        if (localRangeVariable != null) {
          if (i != 0)
          {
            if ((paramSession.database.sqlEnforceRefs) && (resolvesDuplicateColumnReference(localRangeVariable)))
            {
              String str = getColumnName();
              if (this.alias != null)
              {
                StringBuffer localStringBuffer = new StringBuffer(str);
                localStringBuffer.append(' ').append("AS").append(' ').append(this.alias.getStatementName());
                str = localStringBuffer.toString();
              }
              throw Error.error(5580, str);
            }
          }
          else if (resolveColumnReference(localRangeVariable, false))
          {
            i = 1;
            if (!paramSession.database.sqlEnforceRefs) {
              break;
            }
          }
        }
      }
      if (i != 0) {
        return paramHsqlList;
      }
      if (((paramSession.database.sqlSyntaxOra) || (paramSession.database.sqlSyntaxDb2)) && (paramBoolean) && (this.tableName != null))
      {
        NumberSequence localNumberSequence;
        if (("CURRVAL".equals(this.columnName)) || ("PREVVAL".equals(this.columnName)))
        {
          localNumberSequence = paramSession.database.schemaManager.findSequence(paramSession, this.tableName, this.schema);
          if (localNumberSequence != null)
          {
            this.opType = 13;
            this.dataType = localNumberSequence.getDataType();
            this.sequence = localNumberSequence;
            this.schema = null;
            this.tableName = null;
            this.columnName = null;
            i = 1;
          }
        }
        else if ("NEXTVAL".equals(this.columnName))
        {
          localNumberSequence = paramSession.database.schemaManager.findSequence(paramSession, this.tableName, this.schema);
          if (localNumberSequence != null)
          {
            this.opType = 12;
            this.dataType = localNumberSequence.getDataType();
            this.sequence = localNumberSequence;
            this.schema = null;
            this.tableName = null;
            this.columnName = null;
            i = 1;
          }
        }
      }
      if (i != 0) {
        return paramHsqlList;
      }
      if (resolveCorrelated(paramRangeGroup, paramArrayOfRangeGroup)) {
        return paramHsqlList;
      }
      if (paramHsqlList == null) {
        paramHsqlList = new ArrayListIdentity();
      }
      paramHsqlList.add(this);
      break;
    }
    return paramHsqlList;
  }
  
  private boolean resolveCorrelated(RangeGroup paramRangeGroup, RangeGroup[] paramArrayOfRangeGroup)
  {
    for (int i = paramArrayOfRangeGroup.length - 1; i >= 0; i--)
    {
      RangeVariable[] arrayOfRangeVariable = paramArrayOfRangeGroup[i].getRangeVariables();
      for (int j = 0; j < arrayOfRangeVariable.length; j++)
      {
        RangeVariable localRangeVariable = arrayOfRangeVariable[j];
        if ((localRangeVariable != null) && (resolveColumnReference(localRangeVariable, true)))
        {
          if (this.opType == 2)
          {
            paramRangeGroup.setCorrelated();
            for (int k = paramArrayOfRangeGroup.length - 1; k > i; k--) {
              paramArrayOfRangeGroup[k].setCorrelated();
            }
          }
          return true;
        }
      }
    }
    return false;
  }
  
  private boolean resolveColumnReference(RangeVariable paramRangeVariable, boolean paramBoolean)
  {
    if (this.tableName == null)
    {
      ExpressionColumn localExpressionColumn = paramRangeVariable.getColumnExpression(this.columnName);
      if (localExpressionColumn != null)
      {
        this.opType = localExpressionColumn.opType;
        this.nodes = localExpressionColumn.nodes;
        this.dataType = localExpressionColumn.dataType;
        return true;
      }
    }
    int i = paramRangeVariable.findColumn(this.schema, this.tableName, this.columnName);
    if (i == -1) {
      return false;
    }
    switch (paramRangeVariable.rangeType)
    {
    case 3: 
    case 4: 
      if (this.tableName != null) {
        return false;
      }
      ColumnSchema localColumnSchema = paramRangeVariable.getColumn(i);
      if (localColumnSchema.getParameterMode() == 4) {
        return false;
      }
      this.opType = (paramRangeVariable.rangeType == 4 ? 6 : 7);
      break;
    case 2: 
      if (this.tableName == null) {
        return false;
      }
      if (this.schema != null) {
        return false;
      }
      this.opType = 9;
      break;
    }
    setAttributesAsColumn(paramRangeVariable, i);
    return true;
  }
  
  boolean resolvesDuplicateColumnReference(RangeVariable paramRangeVariable)
  {
    if (this.tableName == null)
    {
      ExpressionColumn localExpressionColumn = paramRangeVariable.getColumnExpression(this.columnName);
      if (localExpressionColumn != null) {
        return false;
      }
    }
    switch (paramRangeVariable.rangeType)
    {
    case 2: 
    case 3: 
    case 4: 
      return false;
    }
    int i = paramRangeVariable.findColumn(this.schema, this.tableName, this.columnName);
    return i != -1;
  }
  
  public void resolveTypes(Session paramSession, Expression paramExpression)
  {
    switch (this.opType)
    {
    case 4: 
      if ((paramExpression != null) && (paramExpression.opType != 25)) {
        throw Error.error(5544);
      }
      break;
    case 3: 
      Type localType = null;
      this.nullability = 0;
      for (int i = 0; i < this.nodes.length; i++) {
        localType = Type.getAggregateType(this.nodes[i].dataType, localType);
      }
      this.dataType = localType;
      break;
    }
  }
  
  public Object getValue(Session paramSession)
  {
    Object localObject1;
    switch (this.opType)
    {
    case 4: 
      return null;
    case 10: 
      return getDiagnosticsVariable(paramSession);
    case 6: 
      return paramSession.sessionContext.routineVariables[this.columnIndex];
    case 7: 
      return paramSession.sessionContext.routineArguments[this.columnIndex];
    case 9: 
      return paramSession.sessionContext.triggerArguments[this.rangeVariable.rangePosition][this.columnIndex];
    case 2: 
      localObject1 = paramSession.sessionContext.rangeIterators;
      Object localObject2 = localObject1[this.rangeVariable.rangePosition].getCurrent(this.columnIndex);
      if (this.dataType != this.column.dataType) {
        localObject2 = this.dataType.convertToType(paramSession, localObject2, this.column.dataType);
      }
      return localObject2;
    case 5: 
      localObject1 = paramSession.sessionContext.rangeIterators[this.rangePosition].getCurrent(this.columnIndex);
      return localObject1;
    case 3: 
      localObject1 = null;
      for (int i = 0; i < this.nodes.length; i++)
      {
        localObject1 = this.nodes[i].getValue(paramSession, this.dataType);
        if (localObject1 != null) {
          return localObject1;
        }
      }
      return localObject1;
    case 8: 
      return paramSession.sessionContext.dynamicArguments[this.parameterIndex];
    case 12: 
      return paramSession.sessionData.getSequenceValue(this.sequence);
    case 13: 
      return paramSession.sessionData.getSequenceCurrent(this.sequence);
    case 14: 
      return ValuePool.getInt(paramSession.sessionContext.rownum);
    }
    throw Error.runtimeError(201, "ExpressionColumn");
  }
  
  private Object getDiagnosticsVariable(Session paramSession)
  {
    return paramSession.sessionContext.diagnosticsVariables[this.columnIndex];
  }
  
  public String getSQL()
  {
    StringBuffer localStringBuffer;
    switch (this.opType)
    {
    case 4: 
      return "DEFAULT";
    case 8: 
      return "?";
    case 11: 
      return "*";
    case 3: 
      if (this.alias != null) {
        return this.alias.getStatementName();
      }
      return "COALESCE";
    case 6: 
    case 7: 
    case 10: 
      return this.column.getName().statementName;
    case 14: 
      localStringBuffer = new StringBuffer("ROWNUM");
      localStringBuffer.append('(').append(')');
      return localStringBuffer.toString();
    case 2: 
      if (this.column == null)
      {
        if (this.alias != null) {
          return this.alias.getStatementName();
        }
        if (this.tableName == null) {
          return this.columnName;
        }
        localStringBuffer = new StringBuffer();
        localStringBuffer.append(this.tableName);
        localStringBuffer.append('.');
        localStringBuffer.append(this.columnName);
        return localStringBuffer.toString();
      }
      if (this.rangeVariable.tableAlias == null) {
        return this.column.getName().getSchemaQualifiedStatementName();
      }
      localStringBuffer = new StringBuffer();
      localStringBuffer.append(this.rangeVariable.tableAlias.getStatementName());
      localStringBuffer.append('.');
      localStringBuffer.append(this.column.getName().statementName);
      return localStringBuffer.toString();
    case 93: 
      if (this.nodes.length == 0) {
        return "*";
      }
      localStringBuffer = new StringBuffer();
      for (int i = 0; i < this.nodes.length; i++)
      {
        Expression localExpression = this.nodes[i];
        if (i > 0) {
          localStringBuffer.append(',');
        }
        String str = localExpression.getSQL();
        localStringBuffer.append(str);
      }
      return localStringBuffer.toString();
    }
    throw Error.runtimeError(201, "ExpressionColumn");
  }
  
  protected String describe(Session paramSession, int paramInt)
  {
    StringBuffer localStringBuffer = new StringBuffer(64);
    for (int i = 0; i < paramInt; i++) {
      localStringBuffer.append(' ');
    }
    switch (this.opType)
    {
    case 4: 
      localStringBuffer.append("DEFAULT");
      break;
    case 11: 
      localStringBuffer.append("OpTypes.ASTERISK ");
      break;
    case 6: 
      localStringBuffer.append("VARIABLE: ");
      localStringBuffer.append(this.column.getName().name);
      break;
    case 7: 
      localStringBuffer.append("PARAMETER").append(": ");
      localStringBuffer.append(this.column.getName().name);
      break;
    case 3: 
      localStringBuffer.append("COLUMN").append(": ");
      localStringBuffer.append(this.columnName);
      if (this.alias != null) {
        localStringBuffer.append(" AS ").append(this.alias.name);
      }
      break;
    case 2: 
      localStringBuffer.append("COLUMN").append(": ");
      localStringBuffer.append(this.column.getName().getSchemaQualifiedStatementName());
      if (this.alias != null) {
        localStringBuffer.append(" AS ").append(this.alias.name);
      }
      break;
    case 8: 
      localStringBuffer.append("DYNAMIC PARAM: ");
      localStringBuffer.append(", TYPE = ").append(this.dataType.getNameString());
      break;
    case 12: 
      localStringBuffer.append("SEQUENCE").append(": ");
      localStringBuffer.append(this.sequence.getName().name);
      break;
    }
    localStringBuffer.append('\n');
    return localStringBuffer.toString();
  }
  
  String getTableName()
  {
    if (this.opType == 93) {
      return this.tableName;
    }
    if (this.opType == 2)
    {
      if (this.rangeVariable == null) {
        return this.tableName;
      }
      return this.rangeVariable.getTable().getName().name;
    }
    return "";
  }
  
  static void checkColumnsResolved(HsqlList paramHsqlList)
  {
    if ((paramHsqlList != null) && (!paramHsqlList.isEmpty()))
    {
      StringBuffer localStringBuffer = new StringBuffer();
      Expression localExpression = (Expression)paramHsqlList.get(0);
      if ((localExpression instanceof ExpressionColumn))
      {
        localObject = (ExpressionColumn)localExpression;
        if (((ExpressionColumn)localObject).schema != null) {
          localStringBuffer.append(((ExpressionColumn)localObject).schema + '.');
        }
        if (((ExpressionColumn)localObject).tableName != null) {
          localStringBuffer.append(((ExpressionColumn)localObject).tableName + '.');
        }
        localStringBuffer.append(((ExpressionColumn)localObject).getColumnName());
        throw Error.error(5501, localStringBuffer.toString());
      }
      Object localObject = new OrderedHashSet();
      localExpression.collectAllExpressions((OrderedHashSet)localObject, Expression.columnExpressionSet, Expression.emptyExpressionSet);
      checkColumnsResolved((HsqlList)localObject);
      throw Error.error(5501);
    }
  }
  
  public OrderedHashSet getUnkeyedColumns(OrderedHashSet paramOrderedHashSet)
  {
    for (int i = 0; i < this.nodes.length; i++) {
      if (this.nodes[i] != null) {
        paramOrderedHashSet = this.nodes[i].getUnkeyedColumns(paramOrderedHashSet);
      }
    }
    if ((this.opType == 2) && (!this.rangeVariable.hasKeyedColumnInGroupBy))
    {
      if (paramOrderedHashSet == null) {
        paramOrderedHashSet = new OrderedHashSet();
      }
      paramOrderedHashSet.add(this);
    }
    return paramOrderedHashSet;
  }
  
  OrderedHashSet collectRangeVariables(OrderedHashSet paramOrderedHashSet)
  {
    for (int i = 0; i < this.nodes.length; i++) {
      if (this.nodes[i] != null) {
        paramOrderedHashSet = this.nodes[i].collectRangeVariables(paramOrderedHashSet);
      }
    }
    if (this.rangeVariable != null)
    {
      if (paramOrderedHashSet == null) {
        paramOrderedHashSet = new OrderedHashSet();
      }
      paramOrderedHashSet.add(this.rangeVariable);
    }
    return paramOrderedHashSet;
  }
  
  OrderedHashSet collectRangeVariables(RangeVariable[] paramArrayOfRangeVariable, OrderedHashSet paramOrderedHashSet)
  {
    for (int i = 0; i < this.nodes.length; i++) {
      if (this.nodes[i] != null) {
        paramOrderedHashSet = this.nodes[i].collectRangeVariables(paramArrayOfRangeVariable, paramOrderedHashSet);
      }
    }
    if (this.rangeVariable != null) {
      for (i = 0; i < paramArrayOfRangeVariable.length; i++) {
        if (paramArrayOfRangeVariable[i] == this.rangeVariable)
        {
          if (paramOrderedHashSet == null) {
            paramOrderedHashSet = new OrderedHashSet();
          }
          paramOrderedHashSet.add(this.rangeVariable);
          break;
        }
      }
    }
    return paramOrderedHashSet;
  }
  
  Expression replaceAliasInOrderBy(Session paramSession, Expression[] paramArrayOfExpression, int paramInt)
  {
    for (int i = 0; i < this.nodes.length; i++) {
      if (this.nodes[i] != null) {
        this.nodes[i] = this.nodes[i].replaceAliasInOrderBy(paramSession, paramArrayOfExpression, paramInt);
      }
    }
    switch (this.opType)
    {
    case 2: 
    case 3: 
      i = -1;
      Object localObject;
      for (int j = 0; j < paramInt; j++)
      {
        localObject = paramArrayOfExpression[j].alias;
        String str1 = localObject == null ? null : ((HsqlNameManager.SimpleName)localObject).name;
        if ((this.schema == null) && (this.tableName == null) && (this.columnName.equals(str1))) {
          if (i < 0)
          {
            i = j;
          }
          else if (paramSession.database.sqlEnforceRefs)
          {
            String str2 = getColumnName();
            throw Error.error(5580, str2);
          }
        }
      }
      if (i >= 0) {
        return paramArrayOfExpression[i];
      }
      for (j = 0; j < paramInt; j++) {
        if ((paramArrayOfExpression[j] instanceof ExpressionColumn))
        {
          if (equals(paramArrayOfExpression[j])) {
            if (i < 0)
            {
              i = j;
            }
            else if (paramSession.database.sqlEnforceRefs)
            {
              localObject = getColumnName();
              throw Error.error(5580, (String)localObject);
            }
          }
          if ((this.tableName == null) && (this.schema == null) && (this.columnName.equals(((ExpressionColumn)paramArrayOfExpression[j]).columnName))) {
            if (i < 0)
            {
              i = j;
            }
            else if (paramSession.database.sqlEnforceRefs)
            {
              localObject = getColumnName();
              throw Error.error(5580, (String)localObject);
            }
          }
        }
      }
      if (i >= 0) {
        return paramArrayOfExpression[i];
      }
      break;
    }
    return this;
  }
  
  Expression replaceColumnReferences(RangeVariable paramRangeVariable, Expression[] paramArrayOfExpression)
  {
    if ((this.opType == 2) && (this.rangeVariable == paramRangeVariable)) {
      return paramArrayOfExpression[this.columnIndex];
    }
    for (int i = 0; i < this.nodes.length; i++) {
      if (this.nodes[i] != null) {
        this.nodes[i] = this.nodes[i].replaceColumnReferences(paramRangeVariable, paramArrayOfExpression);
      }
    }
    return this;
  }
  
  boolean hasReference(RangeVariable paramRangeVariable)
  {
    if (paramRangeVariable == this.rangeVariable) {
      return true;
    }
    for (int i = 0; i < this.nodes.length; i++) {
      if ((this.nodes[i] != null) && (this.nodes[i].hasReference(paramRangeVariable))) {
        return true;
      }
    }
    return false;
  }
  
  public boolean equals(Expression paramExpression)
  {
    if (paramExpression == this) {
      return true;
    }
    if (paramExpression == null) {
      return false;
    }
    if (this.opType != paramExpression.opType) {
      return false;
    }
    switch (this.opType)
    {
    case 5: 
      return this.columnIndex == paramExpression.columnIndex;
    case 3: 
      return this.nodes == paramExpression.nodes;
    case 2: 
    case 6: 
    case 7: 
    case 8: 
      return (this.column == paramExpression.getColumn()) && (this.rangeVariable == paramExpression.getRangeVariable());
    }
    return false;
  }
  
  void replaceRangeVariables(RangeVariable[] paramArrayOfRangeVariable1, RangeVariable[] paramArrayOfRangeVariable2)
  {
    for (int i = 0; i < this.nodes.length; i++) {
      this.nodes[i].replaceRangeVariables(paramArrayOfRangeVariable1, paramArrayOfRangeVariable2);
    }
    for (i = 0; i < paramArrayOfRangeVariable1.length; i++) {
      if (this.rangeVariable == paramArrayOfRangeVariable1[i])
      {
        this.rangeVariable = paramArrayOfRangeVariable2[i];
        break;
      }
    }
  }
  
  void resetColumnReferences()
  {
    this.rangeVariable = null;
    this.columnIndex = -1;
  }
  
  public boolean isIndexable(RangeVariable paramRangeVariable)
  {
    if (this.opType == 2) {
      return this.rangeVariable == paramRangeVariable;
    }
    return false;
  }
  
  public boolean isUnresolvedParam()
  {
    return (this.isParam) && (this.dataType == null);
  }
  
  boolean isDynamicParam()
  {
    return this.isParam;
  }
  
  void getJoinRangeVariables(RangeVariable[] paramArrayOfRangeVariable, HsqlList paramHsqlList)
  {
    if (this.opType == 2) {
      for (int i = 0; i < paramArrayOfRangeVariable.length; i++) {
        if (paramArrayOfRangeVariable[i] == this.rangeVariable)
        {
          paramHsqlList.add(this.rangeVariable);
          return;
        }
      }
    }
  }
  
  double costFactor(Session paramSession, RangeVariable paramRangeVariable, int paramInt)
  {
    if ((paramRangeVariable.rangeTable instanceof TableDerived)) {
      return 1024.0D;
    }
    PersistentStore localPersistentStore = paramRangeVariable.rangeTable.getRowStore(paramSession);
    int i = paramRangeVariable.rangeTable.indexTypeForColumn(paramSession, this.columnIndex);
    double d;
    switch (i)
    {
    case 2: 
      if (paramInt == 40) {
        d = 1.0D;
      } else {
        d = localPersistentStore.elementCount() / 2.0D;
      }
      break;
    case 1: 
      if (paramInt == 40)
      {
        d = localPersistentStore.elementCount() / 8.0D;
        if (d > 1024.0D) {
          d = 1024.0D;
        }
      }
      else
      {
        d = localPersistentStore.elementCount() / 2.0D;
      }
      break;
    case 0: 
    default: 
      d = localPersistentStore.elementCount();
    }
    return d < 16.0D ? 16.0D : d;
  }
  
  public Expression duplicate()
  {
    if (this.opType == 7) {
      return this;
    }
    return super.duplicate();
  }
  
  static
  {
    for (int i = 0; i < diagnosticsVariableTokens.length; i++)
    {
      HsqlNameManager.HsqlName localHsqlName = HsqlNameManager.newSystemObjectName(diagnosticsVariableTokens[i], 22);
      Object localObject = Type.SQL_INTEGER;
      if ("MORE".equals(diagnosticsVariableTokens[i])) {
        localObject = Type.SQL_CHAR;
      }
      ColumnSchema localColumnSchema = new ColumnSchema(localHsqlName, (Type)localObject, false, false, null);
      diagnosticsList.add(diagnosticsVariableTokens[i], localColumnSchema);
    }
  }
}


/* Location:              E:\java\java学习\hutubill\lib\all.jar!\org\hsqldb\ExpressionColumn.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */
package org.hsqldb;

import org.hsqldb.error.Error;
import org.hsqldb.index.Index;
import org.hsqldb.lib.ArrayListIdentity;
import org.hsqldb.lib.ArrayUtil;
import org.hsqldb.lib.HashMappedList;
import org.hsqldb.lib.HashSet;
import org.hsqldb.lib.HsqlArrayList;
import org.hsqldb.lib.HsqlList;
import org.hsqldb.lib.IntValueHashMap;
import org.hsqldb.lib.OrderedHashSet;
import org.hsqldb.lib.OrderedIntHashSet;
import org.hsqldb.lib.Set;
import org.hsqldb.map.ValuePool;
import org.hsqldb.navigator.RangeIterator;
import org.hsqldb.navigator.RowSetNavigator;
import org.hsqldb.navigator.RowSetNavigatorData;
import org.hsqldb.navigator.RowSetNavigatorDataTable;
import org.hsqldb.persist.PersistentStore;
import org.hsqldb.result.Result;
import org.hsqldb.result.ResultMetaData;
import org.hsqldb.types.Type;

public class QuerySpecification
  extends QueryExpression
{
  public int resultRangePosition;
  public boolean isDistinctSelect;
  public boolean isAggregated;
  public boolean isGrouped;
  public boolean isOrderSensitive;
  public boolean isSimpleDistinct;
  RangeVariable[] rangeVariables;
  private HsqlArrayList rangeVariableList;
  int startInnerRange = -1;
  int endInnerRange = -1;
  Expression queryCondition;
  Expression checkQueryCondition;
  private Expression havingCondition;
  Expression rowExpression;
  Expression[] exprColumns;
  HsqlArrayList exprColumnList;
  public int indexLimitVisible;
  private int indexLimitRowId;
  private int groupByColumnCount;
  private int havingColumnCount;
  private int indexStartHaving;
  public int indexStartOrderBy;
  public int indexStartAggregates;
  private int indexLimitExpressions;
  public int indexLimitData;
  private boolean hasRowID;
  private boolean isSimpleCount;
  private boolean isSingleMemoryTable;
  public boolean isUniqueResultRows;
  Type[] resultColumnTypes;
  private ArrayListIdentity aggregateSet;
  private ArrayListIdentity resolvedSubqueryExpressions = null;
  private boolean[] aggregateCheck;
  private OrderedHashSet tempSet = new OrderedHashSet();
  int[] columnMap;
  private Table baseTable;
  private OrderedHashSet conditionTables;
  public Index groupIndex;
  private RangeGroup[] outerRanges;
  
  QuerySpecification(Session paramSession, Table paramTable, ParserDQL.CompileContext paramCompileContext, boolean paramBoolean)
  {
    this(paramCompileContext);
    this.isValueList = paramBoolean;
    RangeVariable localRangeVariable = new RangeVariable(paramTable, null, null, null, paramCompileContext);
    localRangeVariable.addTableColumns(this.exprColumnList, 0, null);
    this.indexLimitVisible = this.exprColumnList.size();
    addRangeVariable(paramSession, localRangeVariable);
    this.sortAndSlice = SortAndSlice.noSort;
    this.isBaseMergeable = true;
    this.isMergeable = true;
    this.isTable = true;
  }
  
  QuerySpecification(ParserDQL.CompileContext paramCompileContext)
  {
    super(paramCompileContext);
    this.resultRangePosition = paramCompileContext.getNextRangeVarIndex();
    this.rangeVariableList = new HsqlArrayList();
    this.exprColumnList = new HsqlArrayList();
    this.sortAndSlice = SortAndSlice.noSort;
    this.isBaseMergeable = true;
    this.isMergeable = true;
  }
  
  void addRangeVariable(Session paramSession, RangeVariable paramRangeVariable)
  {
    this.rangeVariableList.add(paramRangeVariable);
  }
  
  public TableDerived getValueListTable()
  {
    if (this.isValueList)
    {
      RangeVariable localRangeVariable = null;
      if (this.rangeVariables == null)
      {
        if (this.rangeVariableList.size() == 1) {
          localRangeVariable = (RangeVariable)this.rangeVariableList.get(0);
        }
      }
      else if (this.rangeVariables.length == 1) {
        localRangeVariable = this.rangeVariables[0];
      }
      if (localRangeVariable != null) {
        return (TableDerived)localRangeVariable.getTable();
      }
    }
    return null;
  }
  
  public RangeVariable[] getRangeVariables()
  {
    return this.rangeVariables;
  }
  
  public int getCurrentRangeVariableCount()
  {
    return this.rangeVariableList.size();
  }
  
  private void resolveRangeVariables(Session paramSession, RangeGroup[] paramArrayOfRangeGroup)
  {
    if ((this.rangeVariables == null) || (this.rangeVariables.length < this.rangeVariableList.size()))
    {
      this.rangeVariables = new RangeVariable[this.rangeVariableList.size()];
      this.rangeVariableList.toArray(this.rangeVariables);
    }
    for (int i = 0; i < this.rangeVariables.length; i++)
    {
      Object localObject;
      if (this.rangeVariables[i].isLateral)
      {
        RangeVariable[] arrayOfRangeVariable = (RangeVariable[])ArrayUtil.resizeArray(this.rangeVariables, i);
        localObject = new RangeGroup.RangeGroupSimple(arrayOfRangeVariable, this);
      }
      else if (paramArrayOfRangeGroup == RangeGroup.emptyArray)
      {
        localObject = RangeGroup.emptyGroup;
      }
      else
      {
        localObject = new RangeGroup.RangeGroupSimple(RangeVariable.emptyArray, this);
      }
      this.rangeVariables[i].resolveRangeTable(paramSession, (RangeGroup)localObject, paramArrayOfRangeGroup);
    }
  }
  
  void addSelectColumnExpression(Expression paramExpression)
  {
    if (paramExpression.getType() == 25) {
      throw Error.error(5564);
    }
    if (this.indexLimitVisible > 0)
    {
      if ((paramExpression.opType == 93) && (((ExpressionColumn)paramExpression).getTableName() == null)) {
        throw Error.error(5578);
      }
      Expression localExpression = (Expression)this.exprColumnList.get(0);
      if ((localExpression.opType == 93) && (((ExpressionColumn)localExpression).getTableName() == null)) {
        throw Error.error(5578);
      }
    }
    this.exprColumnList.add(paramExpression);
    this.indexLimitVisible += 1;
  }
  
  void addQueryCondition(Expression paramExpression)
  {
    this.queryCondition = paramExpression;
  }
  
  void addGroupByColumnExpression(Expression paramExpression)
  {
    if (paramExpression.getType() == 25) {
      throw Error.error(5564);
    }
    this.exprColumnList.add(paramExpression);
    this.isGrouped = true;
    this.groupByColumnCount += 1;
  }
  
  void addHavingExpression(Expression paramExpression)
  {
    this.exprColumnList.add(paramExpression);
    this.havingCondition = paramExpression;
    this.havingColumnCount = 1;
  }
  
  void addSortAndSlice(SortAndSlice paramSortAndSlice)
  {
    this.sortAndSlice = paramSortAndSlice;
  }
  
  public void resolveReferences(Session paramSession, RangeGroup[] paramArrayOfRangeGroup)
  {
    if (this.isReferencesResolved) {
      return;
    }
    this.outerRanges = paramArrayOfRangeGroup;
    resolveRangeVariables(paramSession, paramArrayOfRangeGroup);
    resolveColumnReferencesForAsterisk();
    finaliseColumns();
    resolveColumnReferences(paramSession, paramArrayOfRangeGroup);
    setReferenceableColumns();
    Expression.resolveColumnSet(paramSession, RangeVariable.emptyArray, paramArrayOfRangeGroup, this.unresolvedExpressions);
    this.unionColumnTypes = new Type[this.indexLimitVisible];
    this.isReferencesResolved = true;
  }
  
  public boolean hasReference(RangeVariable paramRangeVariable)
  {
    if (this.unresolvedExpressions == null) {
      return false;
    }
    for (int i = 0; i < this.unresolvedExpressions.size(); i++) {
      if (((Expression)this.unresolvedExpressions.get(i)).hasReference(paramRangeVariable)) {
        return true;
      }
    }
    return false;
  }
  
  public boolean areColumnsResolved()
  {
    return super.areColumnsResolved();
  }
  
  public void resolveTypes(Session paramSession)
  {
    if (this.isResolved) {
      return;
    }
    resolveTypesPartOne(paramSession);
    resolveTypesPartTwo(paramSession);
    resolveTypesPartThree(paramSession);
    ArrayUtil.copyArray(this.resultTable.colTypes, this.unionColumnTypes, this.unionColumnTypes.length);
  }
  
  void resolveTypesPartOne(Session paramSession)
  {
    if (this.isPartOneResolved) {
      return;
    }
    resolveExpressionTypes(paramSession);
    resolveAggregates();
    for (int i = 0; i < this.unionColumnTypes.length; i++) {
      this.unionColumnTypes[i] = Type.getAggregateType(this.unionColumnTypes[i], this.exprColumns[i].getDataType());
    }
    this.isPartOneResolved = true;
  }
  
  void resolveTypesPartTwoRecursive(Session paramSession)
  {
    for (int i = 0; i < this.unionColumnTypes.length; i++)
    {
      Type localType = this.unionColumnTypes[i];
      this.exprColumns[i].setDataType(paramSession, localType);
    }
    setResultColumnTypes();
    createResultMetaData(paramSession);
    createTable(paramSession);
  }
  
  void resolveTypesPartTwo(Session paramSession)
  {
    if (this.isPartTwoResolved) {
      return;
    }
    resolveGroups();
    for (int i = 0; i < this.unionColumnTypes.length; i++)
    {
      Object localObject = this.unionColumnTypes[i];
      if (localObject == null)
      {
        if (paramSession.database.sqlEnforceTypes) {
          throw Error.error(5567);
        }
        localObject = Type.SQL_VARCHAR_DEFAULT;
        this.unionColumnTypes[i] = localObject;
      }
      this.exprColumns[i].setDataType(paramSession, (Type)localObject);
      if ((this.exprColumns[i].dataType.isArrayType()) && (this.exprColumns[i].dataType.collectionBaseType() == null)) {
        throw Error.error(5567);
      }
    }
    for (i = this.indexLimitVisible; i < this.indexStartHaving; i++) {
      if (this.exprColumns[i].dataType == null) {
        throw Error.error(5567);
      }
    }
    checkLobUsage();
    setMergeability();
    setUpdatability();
    setResultColumnTypes();
    createResultMetaData(paramSession);
    createTable(paramSession);
    mergeQuery();
    this.isPartTwoResolved = true;
  }
  
  void resolveTypesPartThree(Session paramSession)
  {
    if (this.isResolved) {
      return;
    }
    this.sortAndSlice.setSortIndex(this);
    setRangeVariableConditions(paramSession);
    setDistinctConditions(paramSession);
    setAggregateConditions(paramSession);
    this.sortAndSlice.setSortRange(this);
    for (int i = 0; i < this.rangeVariables.length; i++) {
      this.rangeVariables[i].resolveRangeTableTypes(paramSession, this.rangeVariables);
    }
    setResultNullability();
    this.rangeVariableList = null;
    this.tempSet = null;
    this.compileContext = null;
    this.outerRanges = null;
    this.isResolved = true;
  }
  
  public void addExtraConditions(Expression paramExpression)
  {
    if ((this.isAggregated) || (this.isGrouped)) {
      return;
    }
    this.queryCondition = ExpressionLogical.andExpressions(this.queryCondition, paramExpression);
  }
  
  private void resolveColumnReferences(Session paramSession, RangeGroup[] paramArrayOfRangeGroup)
  {
    if ((this.isDistinctSelect) || (this.isGrouped)) {
      this.acceptsSequences = false;
    }
    Object localObject;
    for (int i = 0; i < this.rangeVariables.length; i++)
    {
      localObject = this.rangeVariables[i].getJoinCondition();
      if (localObject != null) {
        resolveColumnReferencesAndAllocate(paramSession, (Expression)localObject, i + 1, paramArrayOfRangeGroup, false);
      }
    }
    resolveColumnReferencesAndAllocate(paramSession, this.queryCondition, this.rangeVariables.length, paramArrayOfRangeGroup, false);
    if (this.resolvedSubqueryExpressions != null) {
      this.resolvedSubqueryExpressions.setSize(0);
    }
    for (i = 0; i < this.indexLimitVisible; i++)
    {
      resolveColumnReferencesAndAllocate(paramSession, this.exprColumns[i], this.rangeVariables.length, paramArrayOfRangeGroup, this.acceptsSequences);
      if ((!this.isGrouped) && (!this.isDistinctSelect))
      {
        localObject = this.exprColumns[i].collectAllSubqueries(null);
        if (localObject != null) {
          this.isMergeable = false;
        }
        localObject = this.exprColumns[i].collectAllExpressions(null, Expression.sequenceExpressionSet, Expression.subqueryAggregateExpressionSet);
        if (localObject != null)
        {
          this.isOrderSensitive = true;
          this.isMergeable = false;
          this.isBaseMergeable = false;
        }
      }
    }
    for (i = this.indexLimitVisible; i < this.indexStartHaving; i++) {
      this.exprColumns[i] = resolveColumnReferencesInGroupBy(paramSession, this.exprColumns[i]);
    }
    for (i = this.indexStartHaving; i < this.indexStartOrderBy; i++) {
      resolveColumnReferencesAndAllocate(paramSession, this.exprColumns[i], this.rangeVariables.length, paramArrayOfRangeGroup, false);
    }
    resolveColumnReferencesInOrderBy(paramSession, paramArrayOfRangeGroup, this.sortAndSlice);
  }
  
  void resolveColumnReferencesInOrderBy(Session paramSession, RangeGroup[] paramArrayOfRangeGroup, SortAndSlice paramSortAndSlice)
  {
    int i = paramSortAndSlice.getOrderLength();
    for (int j = 0; j < i; j++)
    {
      ExpressionOrderBy localExpressionOrderBy = (ExpressionOrderBy)paramSortAndSlice.exprList.get(j);
      replaceColumnIndexInOrderBy(localExpressionOrderBy);
      if (localExpressionOrderBy.getLeftNode().queryTableColumnIndex == -1)
      {
        if ((paramSortAndSlice.sortUnion) && (localExpressionOrderBy.getLeftNode().getType() != 2)) {
          throw Error.error(5576);
        }
        localExpressionOrderBy.replaceAliasInOrderBy(paramSession, this.exprColumns, this.indexLimitVisible);
        resolveColumnReferencesAndAllocate(paramSession, localExpressionOrderBy, this.rangeVariables.length, RangeGroup.emptyArray, false);
        if ((this.isAggregated) || (this.isGrouped))
        {
          boolean bool = localExpressionOrderBy.getLeftNode().isComposedOf(this.exprColumns, 0, this.indexLimitVisible + this.groupByColumnCount, Expression.aggregateFunctionSet);
          if (!bool) {
            throw Error.error(5576);
          }
        }
      }
    }
    if (paramSortAndSlice.limitCondition != null) {
      this.unresolvedExpressions = paramSortAndSlice.limitCondition.resolveColumnReferences(paramSession, this, paramArrayOfRangeGroup, this.unresolvedExpressions);
    }
    paramSortAndSlice.prepare(this);
  }
  
  private boolean resolveColumnReferences(Session paramSession, Expression paramExpression, int paramInt, boolean paramBoolean)
  {
    if (paramExpression == null) {
      return true;
    }
    int i = this.unresolvedExpressions == null ? 0 : this.unresolvedExpressions.size();
    this.unresolvedExpressions = paramExpression.resolveColumnReferences(paramSession, this, paramInt, RangeGroup.emptyArray, this.unresolvedExpressions, paramBoolean);
    int j = this.unresolvedExpressions == null ? 0 : this.unresolvedExpressions.size();
    return i == j;
  }
  
  private void resolveColumnReferencesForAsterisk()
  {
    int i = 0;
    while (i < this.indexLimitVisible)
    {
      Expression localExpression = (Expression)this.exprColumnList.get(i);
      if (localExpression.getType() == 93)
      {
        this.exprColumnList.remove(i);
        String str = ((ExpressionColumn)localExpression).getTableName();
        if (str == null)
        {
          addAllJoinedColumns(localExpression);
        }
        else
        {
          j = 0;
          for (int k = 0; k < this.rangeVariables.length; k++)
          {
            RangeVariable localRangeVariable = this.rangeVariables[k].getRangeForTableName(str);
            if (localRangeVariable != null)
            {
              HashSet localHashSet = getAllNamedJoinColumns();
              this.rangeVariables[k].addTableColumns(localRangeVariable, localExpression, localHashSet);
              j = 1;
              break;
            }
          }
          if (j == 0) {
            throw Error.error(5501, str);
          }
        }
        for (int j = 0; j < localExpression.nodes.length; j++)
        {
          this.exprColumnList.add(i, localExpression.nodes[j]);
          i++;
        }
        this.indexLimitVisible += localExpression.nodes.length - 1;
      }
      else
      {
        i++;
      }
    }
  }
  
  private void resolveColumnReferencesAndAllocate(Session paramSession, Expression paramExpression, int paramInt, RangeGroup[] paramArrayOfRangeGroup, boolean paramBoolean)
  {
    if (paramExpression == null) {
      return;
    }
    HsqlList localHsqlList1 = paramExpression.resolveColumnReferences(paramSession, this, paramInt, paramArrayOfRangeGroup, null, paramBoolean);
    if (localHsqlList1 != null) {
      for (int i = 0; i < localHsqlList1.size(); i++)
      {
        Expression localExpression = (Expression)localHsqlList1.get(i);
        boolean bool = true;
        if (localExpression.isSelfAggregate()) {
          for (int j = 0; j < localExpression.nodes.length; j++)
          {
            HsqlList localHsqlList2 = localExpression.nodes[j].resolveColumnReferences(paramSession, this, paramInt, RangeGroup.emptyArray, null, false);
            for (int k = 0; k < paramArrayOfRangeGroup.length; k++) {
              if (paramArrayOfRangeGroup[k].isVariable()) {
                localHsqlList2 = Expression.resolveColumnSet(paramSession, paramArrayOfRangeGroup[k].getRangeVariables(), RangeGroup.emptyArray, localHsqlList2);
              }
            }
            bool &= localHsqlList2 == null;
          }
        } else {
          bool = resolveColumnReferences(paramSession, localExpression, paramInt, paramBoolean);
        }
        if (bool)
        {
          if (localExpression.isSelfAggregate())
          {
            if (this.aggregateSet == null) {
              this.aggregateSet = new ArrayListIdentity();
            }
            this.aggregateSet.add(localExpression);
            this.isAggregated = true;
            paramExpression.setAggregate();
            localExpression.setCorrelatedReferences(this);
          }
          if (this.resolvedSubqueryExpressions == null) {
            this.resolvedSubqueryExpressions = new ArrayListIdentity();
          }
          this.resolvedSubqueryExpressions.add(localExpression);
        }
        else
        {
          if (this.unresolvedExpressions == null) {
            this.unresolvedExpressions = new ArrayListIdentity();
          }
          this.unresolvedExpressions.add(localExpression);
        }
      }
    }
  }
  
  private Expression resolveColumnReferencesInGroupBy(Session paramSession, Expression paramExpression)
  {
    if (paramExpression == null) {
      return null;
    }
    HsqlList localHsqlList = paramExpression.resolveColumnReferences(paramSession, this, this.rangeVariables.length, RangeGroup.emptyArray, null, false);
    if (localHsqlList != null)
    {
      if (paramExpression.getType() == 2)
      {
        Expression localExpression = paramExpression.replaceAliasInOrderBy(paramSession, this.exprColumns, this.indexLimitVisible);
        if (localExpression != paramExpression) {
          return localExpression;
        }
      }
      resolveColumnReferencesAndAllocate(paramSession, paramExpression, this.rangeVariables.length, RangeGroup.emptyArray, false);
    }
    return paramExpression;
  }
  
  private HashSet getAllNamedJoinColumns()
  {
    HashSet localHashSet = null;
    for (int i = 0; i < this.rangeVariableList.size(); i++)
    {
      RangeVariable localRangeVariable = (RangeVariable)this.rangeVariableList.get(i);
      if (localRangeVariable.namedJoinColumns != null)
      {
        if (localHashSet == null) {
          localHashSet = new HashSet();
        }
        localHashSet.addAll(localRangeVariable.namedJoinColumns);
      }
    }
    return localHashSet;
  }
  
  public Expression getEquiJoinExpressions(OrderedHashSet paramOrderedHashSet, RangeVariable paramRangeVariable, boolean paramBoolean)
  {
    HashSet localHashSet = new HashSet();
    Expression localExpression = null;
    OrderedHashSet localOrderedHashSet = new OrderedHashSet();
    for (int i = this.rangeVariableList.size() - 1; i >= 0; i--)
    {
      RangeVariable localRangeVariable = (RangeVariable)this.rangeVariableList.get(i);
      HashMappedList localHashMappedList = localRangeVariable.rangeTable.columnList;
      for (int j = 0; j < localHashMappedList.size(); j++)
      {
        ColumnSchema localColumnSchema = (ColumnSchema)localHashMappedList.get(j);
        String str = localRangeVariable.getColumnAlias(j).name;
        boolean bool = paramOrderedHashSet.contains(str);
        int k = (localRangeVariable.namedJoinColumns != null) && (localRangeVariable.namedJoinColumns.contains(str)) ? 1 : 0;
        int m = (k == 0) && (!localHashSet.add(str)) ? 1 : 0;
        if ((m != 0) && ((!paramBoolean) || (bool))) {
          throw Error.error(5578, str);
        }
        if (bool)
        {
          localOrderedHashSet.add(str);
          int n = localRangeVariable.rangeTable.getColumnIndex(localColumnSchema.getNameString());
          int i1 = paramRangeVariable.rangeTable.getColumnIndex(str);
          ExpressionLogical localExpressionLogical = new ExpressionLogical(localRangeVariable, n, paramRangeVariable, i1);
          ExpressionColumn localExpressionColumn = localRangeVariable.getColumnExpression(str);
          if (localExpressionColumn == null)
          {
            localExpressionColumn = new ExpressionColumn(new Expression[] { localExpressionLogical.getLeftNode(), localExpressionLogical.getRightNode() }, str);
            localRangeVariable.addNamedJoinColumnExpression(str, localExpressionColumn, n);
            localExpression = ExpressionLogical.andExpressions(localExpression, localExpressionLogical);
            paramRangeVariable.addNamedJoinColumnExpression(str, localExpressionColumn, i1);
          }
          else if ((paramRangeVariable.getColumnExpression(str) == null) && ((!localRangeVariable.isLeftJoin) || (localRangeVariable.isRightJoin)))
          {
            if ((localRangeVariable.isLeftJoin) && (localRangeVariable.isRightJoin)) {
              localExpressionLogical = new ExpressionLogical(localExpressionColumn, localExpressionLogical.getRightNode());
            }
            localExpressionColumn.nodes = ((Expression[])ArrayUtil.resizeArray(localExpressionColumn.nodes, localExpressionColumn.nodes.length + 1));
            localExpressionColumn.nodes[(localExpressionColumn.nodes.length - 1)] = localExpressionLogical.getRightNode();
            localExpression = ExpressionLogical.andExpressions(localExpression, localExpressionLogical);
            paramRangeVariable.addNamedJoinColumnExpression(str, localExpressionColumn, i1);
          }
        }
      }
      if (!localRangeVariable.isJoin) {
        break;
      }
    }
    if ((paramBoolean) && (!localOrderedHashSet.containsAll(paramOrderedHashSet))) {
      throw Error.error(5501);
    }
    paramRangeVariable.addNamedJoinColumns(localOrderedHashSet);
    return localExpression;
  }
  
  private void addAllJoinedColumns(Expression paramExpression)
  {
    HsqlArrayList localHsqlArrayList = new HsqlArrayList();
    for (int i = 0; i < this.rangeVariables.length; i++) {
      this.rangeVariables[i].addTableColumns(localHsqlArrayList);
    }
    Expression[] arrayOfExpression = new Expression[localHsqlArrayList.size()];
    localHsqlArrayList.toArray(arrayOfExpression);
    paramExpression.nodes = arrayOfExpression;
  }
  
  private void finaliseColumns()
  {
    this.indexLimitRowId = this.indexLimitVisible;
    this.indexStartHaving = (this.indexLimitRowId + this.groupByColumnCount);
    this.indexStartOrderBy = (this.indexStartHaving + this.havingColumnCount);
    this.indexStartAggregates = (this.indexStartOrderBy + this.sortAndSlice.getOrderLength());
    this.indexLimitData = (this.indexLimitExpressions = this.indexStartAggregates);
    this.exprColumns = new Expression[this.indexLimitExpressions];
    this.exprColumnList.toArray(this.exprColumns);
    this.exprColumnList = null;
    for (int i = 0; i < this.indexLimitVisible; i++) {
      this.exprColumns[i].queryTableColumnIndex = i;
    }
    if (this.sortAndSlice.hasOrder()) {
      for (i = 0; i < this.sortAndSlice.getOrderLength(); i++) {
        this.exprColumns[(this.indexStartOrderBy + i)] = ((Expression)this.sortAndSlice.exprList.get(i));
      }
    }
    this.rowExpression = new Expression(25, this.exprColumns);
  }
  
  private void replaceColumnIndexInOrderBy(Expression paramExpression)
  {
    Expression localExpression = paramExpression.getLeftNode();
    if (localExpression.getType() != 1) {
      return;
    }
    Type localType = localExpression.getDataType();
    if ((localType != null) && (localType.typeCode == 4))
    {
      int i = ((Integer)localExpression.getValue(null)).intValue();
      if ((0 < i) && (i <= this.indexLimitVisible))
      {
        paramExpression.setLeftNode(this.exprColumns[(i - 1)]);
        return;
      }
    }
    throw Error.error(5576);
  }
  
  OrderedHashSet collectRangeVariables(RangeVariable[] paramArrayOfRangeVariable, OrderedHashSet paramOrderedHashSet)
  {
    for (int i = 0; i < this.indexStartAggregates; i++) {
      paramOrderedHashSet = this.exprColumns[i].collectRangeVariables(paramArrayOfRangeVariable, paramOrderedHashSet);
    }
    if (this.queryCondition != null) {
      paramOrderedHashSet = this.queryCondition.collectRangeVariables(paramArrayOfRangeVariable, paramOrderedHashSet);
    }
    if (this.havingCondition != null) {
      paramOrderedHashSet = this.havingCondition.collectRangeVariables(paramArrayOfRangeVariable, paramOrderedHashSet);
    }
    return paramOrderedHashSet;
  }
  
  OrderedHashSet collectRangeVariables(OrderedHashSet paramOrderedHashSet)
  {
    for (int i = 0; i < this.indexStartAggregates; i++) {
      paramOrderedHashSet = this.exprColumns[i].collectRangeVariables(paramOrderedHashSet);
    }
    if (this.queryCondition != null) {
      paramOrderedHashSet = this.queryCondition.collectRangeVariables(paramOrderedHashSet);
    }
    if (this.havingCondition != null) {
      paramOrderedHashSet = this.havingCondition.collectRangeVariables(paramOrderedHashSet);
    }
    return paramOrderedHashSet;
  }
  
  public void resolveExpressionTypes(Session paramSession)
  {
    Expression localExpression;
    for (int i = 0; i < this.indexStartAggregates; i++)
    {
      localExpression = this.exprColumns[i];
      localExpression.resolveTypes(paramSession, this.rowExpression);
      if (localExpression.getType() == 25) {
        throw Error.error(5565);
      }
      if ((localExpression.getType() == 22) && (localExpression.getDegree() > 1)) {
        throw Error.error(5565);
      }
      if ((localExpression.getDataType() != null) && (localExpression.getDataType().typeCode == 19)) {
        throw Error.error(5565);
      }
    }
    for (i = 0; i < this.rangeVariables.length; i++)
    {
      localExpression = this.rangeVariables[i].getJoinCondition();
      if (localExpression != null)
      {
        localExpression.resolveTypes(paramSession, null);
        if (localExpression.getDataType() != Type.SQL_BOOLEAN) {
          throw Error.error(5568);
        }
      }
    }
    if (this.queryCondition != null)
    {
      this.queryCondition.resolveTypes(paramSession, null);
      if (this.queryCondition.getDataType() != Type.SQL_BOOLEAN) {
        throw Error.error(5568);
      }
    }
    if (this.havingCondition != null)
    {
      this.havingCondition.resolveTypes(paramSession, null);
      if (this.havingCondition.getDataType() != Type.SQL_BOOLEAN) {
        throw Error.error(5568);
      }
    }
    if (this.sortAndSlice.limitCondition != null) {
      this.sortAndSlice.limitCondition.resolveTypes(paramSession, null);
    }
  }
  
  private void resolveAggregates()
  {
    this.tempSet.clear();
    if (this.isAggregated)
    {
      this.aggregateCheck = new boolean[this.indexStartAggregates];
      this.tempSet.addAll(this.aggregateSet);
      this.indexLimitData = (this.indexLimitExpressions = this.exprColumns.length + this.tempSet.size());
      this.exprColumns = ((Expression[])ArrayUtil.resizeArray(this.exprColumns, this.indexLimitExpressions));
      int i = this.indexStartAggregates;
      for (int j = 0; i < this.indexLimitExpressions; j++)
      {
        Expression localExpression = (Expression)this.tempSet.get(j);
        this.exprColumns[i] = localExpression.duplicate();
        this.exprColumns[i].nodes = localExpression.nodes;
        this.exprColumns[i].dataType = localExpression.dataType;
        i++;
      }
      this.tempSet.clear();
    }
  }
  
  private void setRangeVariableConditions(Session paramSession)
  {
    RangeVariableResolver localRangeVariableResolver = new RangeVariableResolver(paramSession, this);
    localRangeVariableResolver.processConditions();
    this.rangeVariables = localRangeVariableResolver.rangeVariables;
  }
  
  private void setDistinctConditions(Session paramSession)
  {
    if ((!this.isDistinctSelect) && (!this.isGrouped)) {
      return;
    }
    if (this.isAggregated) {
      return;
    }
    for (int i = 0; i < this.rangeVariables.length; i++) {
      if (this.rangeVariables[i].isRightJoin) {
        return;
      }
    }
    RangeVariable localRangeVariable = null;
    int[] arrayOfInt;
    if (this.isGrouped)
    {
      arrayOfInt = new int[this.groupByColumnCount];
      for (j = 0; j < this.groupByColumnCount; j++)
      {
        if (this.exprColumns[(this.indexLimitRowId + j)].getType() != 2) {
          return;
        }
        if (localRangeVariable == null) {
          localRangeVariable = this.exprColumns[(this.indexLimitRowId + j)].getRangeVariable();
        } else if (localRangeVariable != this.exprColumns[(this.indexLimitRowId + j)].getRangeVariable()) {
          return;
        }
        arrayOfInt[j] = this.exprColumns[j].columnIndex;
      }
    }
    else
    {
      arrayOfInt = new int[this.indexLimitVisible];
    }
    for (int j = 0; j < this.indexLimitVisible; j++)
    {
      if (this.exprColumns[j].getType() != 2) {
        return;
      }
      if (localRangeVariable == null) {
        localRangeVariable = this.exprColumns[j].getRangeVariable();
      } else if (localRangeVariable != this.exprColumns[j].getRangeVariable()) {
        return;
      }
      if (!this.isGrouped) {
        arrayOfInt[j] = this.exprColumns[j].columnIndex;
      }
    }
    if (localRangeVariable != this.rangeVariables[0]) {
      return;
    }
    boolean bool = ArrayUtil.areAllIntIndexesAsBooleanArray(arrayOfInt, localRangeVariable.usedColumns);
    if (!bool) {
      return;
    }
    if (!localRangeVariable.hasAnyIndexCondition())
    {
      Index localIndex = localRangeVariable.rangeTable.getIndexForColumns(arrayOfInt);
      if (localIndex != null) {
        localRangeVariable.setSortIndex(localIndex, false);
      }
    }
    this.isSimpleDistinct = localRangeVariable.setDistinctColumnsOnIndex(arrayOfInt);
  }
  
  private void setAggregateConditions(Session paramSession)
  {
    if (!this.isAggregated) {
      return;
    }
    if (this.isGrouped)
    {
      setGroupedAggregateConditions(paramSession);
    }
    else if ((!this.sortAndSlice.hasOrder()) && (!this.sortAndSlice.hasLimit()) && (this.aggregateSet.size() == 1) && (this.indexLimitVisible == 1))
    {
      Expression localExpression1 = this.exprColumns[this.indexStartAggregates];
      int i = localExpression1.getType();
      Expression localExpression2 = localExpression1.getLeftNode();
      Object localObject;
      switch (i)
      {
      case 73: 
      case 74: 
        if (!localExpression1.hasCondition())
        {
          localObject = new SortAndSlice();
          ((SortAndSlice)localObject).isGenerated = true;
          ((SortAndSlice)localObject).addLimitCondition(ExpressionOp.limitOneExpression);
          if (((SortAndSlice)localObject).prepareSpecial(paramSession, this)) {
            this.sortAndSlice = ((SortAndSlice)localObject);
          }
        }
        break;
      case 71: 
        if ((!localExpression1.hasCondition()) && (this.rangeVariables.length == 1) && (this.queryCondition == null)) {
          if (localExpression2.getType() == 11) {
            this.isSimpleCount = true;
          } else if (localExpression2.getNullability() == 0) {
            if (localExpression1.isDistinctAggregate)
            {
              if (localExpression2.opType == 2)
              {
                localObject = localExpression2.getRangeVariable().getTable();
                if ((((Table)localObject).getPrimaryKey().length == 1) && (((Table)localObject).getColumn(localObject.getPrimaryKey()[0]) == localExpression2.getColumn())) {
                  this.isSimpleCount = true;
                }
              }
            }
            else {
              this.isSimpleCount = true;
            }
          }
        }
        break;
      }
    }
  }
  
  private void setGroupedAggregateConditions(Session paramSession) {}
  
  void checkLobUsage() {}
  
  private void resolveGroups()
  {
    OrderedHashSet localOrderedHashSet1 = null;
    this.tempSet.clear();
    int i;
    int j;
    Expression localExpression1;
    if (this.isGrouped)
    {
      for (i = this.indexLimitVisible; i < this.indexLimitVisible + this.groupByColumnCount; i++)
      {
        this.exprColumns[i].collectAllExpressions(this.tempSet, Expression.aggregateFunctionSet, Expression.subqueryExpressionSet);
        if (!this.tempSet.isEmpty()) {
          throw Error.error(5572, ((Expression)this.tempSet.get(0)).getSQL());
        }
      }
      for (i = 0; i < this.indexLimitVisible; i++) {
        if (!this.exprColumns[i].isComposedOf(this.exprColumns, this.indexLimitVisible, this.indexLimitVisible + this.groupByColumnCount, Expression.aggregateFunctionSet)) {
          this.tempSet.add(this.exprColumns[i]);
        }
      }
      if (!this.tempSet.isEmpty())
      {
        if (!resolveForGroupBy(this.tempSet)) {
          throw Error.error(5574, ((Expression)this.tempSet.get(0)).getSQL());
        }
        localOrderedHashSet1 = new OrderedHashSet();
        localOrderedHashSet1.addAll(this.tempSet);
      }
    }
    else if (this.isAggregated)
    {
      for (i = 0; i < this.indexLimitVisible; i++)
      {
        this.exprColumns[i].collectAllExpressions(this.tempSet, Expression.columnExpressionSet, Expression.aggregateFunctionSet);
        for (j = 0; j < this.tempSet.size(); j++)
        {
          localExpression1 = (Expression)this.tempSet.get(j);
          for (int m = 0; m < this.rangeVariables.length; m++) {
            if (this.rangeVariables[m] == localExpression1.getRangeVariable()) {
              throw Error.error(5574, localExpression1.getSQL());
            }
          }
        }
        this.tempSet.clear();
      }
    }
    this.tempSet.clear();
    if (this.havingCondition != null)
    {
      if (this.unresolvedExpressions != null) {
        this.tempSet.addAll(this.unresolvedExpressions);
      }
      for (i = this.indexLimitVisible; i < this.indexLimitVisible + this.groupByColumnCount; i++) {
        this.tempSet.add(this.exprColumns[i]);
      }
      if (localOrderedHashSet1 != null) {
        this.tempSet.addAll(localOrderedHashSet1);
      }
      if (!this.havingCondition.isComposedOf(this.tempSet, this.outerRanges, Expression.subqueryAggregateExpressionSet)) {
        throw Error.error(5573);
      }
      this.tempSet.clear();
    }
    if (this.isDistinctSelect)
    {
      i = this.sortAndSlice.getOrderLength();
      for (j = 0; j < i; j++)
      {
        localExpression1 = (Expression)this.sortAndSlice.exprList.get(j);
        if ((localExpression1.queryTableColumnIndex == -1) && (!localExpression1.isComposedOf(this.exprColumns, 0, this.indexLimitVisible, Expression.emptyExpressionSet))) {
          throw Error.error(5576);
        }
      }
    }
    if (this.isGrouped)
    {
      i = this.sortAndSlice.getOrderLength();
      for (j = 0; j < i; j++)
      {
        localExpression1 = (Expression)this.sortAndSlice.exprList.get(j);
        if ((localExpression1.queryTableColumnIndex == -1) && (!localExpression1.isAggregate()) && (!localExpression1.isComposedOf(this.exprColumns, 0, this.indexLimitVisible + this.groupByColumnCount, Expression.emptyExpressionSet))) {
          throw Error.error(5576);
        }
      }
    }
    if (!this.isAggregated) {
      return;
    }
    OrderedHashSet localOrderedHashSet2 = new OrderedHashSet();
    OrderedHashSet localOrderedHashSet3 = new OrderedHashSet();
    Expression localExpression2;
    Object localObject;
    for (int k = this.indexStartAggregates; k < this.indexLimitExpressions; k++)
    {
      localExpression2 = this.exprColumns[k];
      localObject = new ExpressionColumn(localExpression2, k, this.resultRangePosition);
      localOrderedHashSet2.add(localExpression2);
      localOrderedHashSet3.add(localObject);
    }
    for (k = 0; k < this.indexStartHaving; k++) {
      if (!this.exprColumns[k].isAggregate())
      {
        localExpression2 = this.exprColumns[k];
        if (localOrderedHashSet2.add(localExpression2))
        {
          localObject = new ExpressionColumn(localExpression2, k, this.resultRangePosition);
          localOrderedHashSet3.add(localObject);
        }
      }
    }
    k = this.sortAndSlice.getOrderLength();
    for (int n = 0; n < k; n++)
    {
      localObject = (Expression)this.sortAndSlice.exprList.get(n);
      if (((Expression)localObject).getLeftNode().isAggregate()) {
        ((Expression)localObject).setAggregate();
      }
    }
    for (n = this.indexStartOrderBy; n < this.indexStartAggregates; n++) {
      if (this.exprColumns[n].getLeftNode().isAggregate()) {
        this.exprColumns[n].setAggregate();
      }
    }
    for (n = 0; n < this.indexStartAggregates; n++)
    {
      localObject = this.exprColumns[n];
      if (((Expression)localObject).isAggregate())
      {
        this.aggregateCheck[n] = true;
        if (((Expression)localObject).isAggregate()) {
          ((Expression)localObject).convertToSimpleColumn(localOrderedHashSet2, localOrderedHashSet3);
        }
      }
    }
    for (n = 0; n < this.aggregateSet.size(); n++)
    {
      localObject = (Expression)this.aggregateSet.get(n);
      ((Expression)localObject).convertToSimpleColumn(localOrderedHashSet2, localOrderedHashSet3);
    }
    if (this.resolvedSubqueryExpressions != null) {
      for (n = 0; n < this.resolvedSubqueryExpressions.size(); n++)
      {
        localObject = (Expression)this.resolvedSubqueryExpressions.get(n);
        ((Expression)localObject).convertToSimpleColumn(localOrderedHashSet2, localOrderedHashSet3);
      }
    }
  }
  
  boolean resolveForGroupBy(HsqlList paramHsqlList)
  {
    Object localObject1;
    Object localObject2;
    for (int i = this.indexLimitVisible; i < this.indexLimitVisible + this.groupByColumnCount; i++)
    {
      localObject1 = this.exprColumns[i];
      if (((Expression)localObject1).getType() == 2)
      {
        localObject2 = ((Expression)localObject1).getRangeVariable();
        int k = ((Expression)localObject1).getColumnIndex();
        ((RangeVariable)localObject2).columnsInGroupBy[k] = true;
      }
    }
    for (i = 0; i < this.rangeVariables.length; i++)
    {
      localObject1 = this.rangeVariables[i];
      ((RangeVariable)localObject1).hasKeyedColumnInGroupBy = (((RangeVariable)localObject1).rangeTable.getUniqueNotNullColumnGroup(((RangeVariable)localObject1).columnsInGroupBy) != null);
    }
    OrderedHashSet localOrderedHashSet = null;
    for (int j = 0; j < paramHsqlList.size(); j++)
    {
      localObject2 = (Expression)paramHsqlList.get(j);
      localOrderedHashSet = ((Expression)localObject2).getUnkeyedColumns(localOrderedHashSet);
    }
    return localOrderedHashSet == null;
  }
  
  Result getResult(Session paramSession, int paramInt)
  {
    Result localResult = getSingleResult(paramSession, paramInt);
    localResult.getNavigator().reset();
    return localResult;
  }
  
  private Result getSingleResult(Session paramSession, int paramInt)
  {
    int[] arrayOfInt = this.sortAndSlice.getLimits(paramSession, this, paramInt);
    Result localResult = buildResult(paramSession, arrayOfInt);
    RowSetNavigatorData localRowSetNavigatorData = (RowSetNavigatorData)localResult.getNavigator();
    if (this.isDistinctSelect) {
      localRowSetNavigatorData.removeDuplicates(paramSession);
    }
    if (this.sortAndSlice.hasOrder()) {
      localRowSetNavigatorData.sortOrder(paramSession);
    }
    if ((arrayOfInt != SortAndSlice.defaultLimits) && (!this.sortAndSlice.skipFullResult)) {
      localRowSetNavigatorData.trim(arrayOfInt[0], arrayOfInt[1]);
    }
    return localResult;
  }
  
  private Result buildResult(Session paramSession, int[] paramArrayOfInt)
  {
    Object localObject1 = new RowSetNavigatorData(paramSession, this);
    Result localResult = Result.newResult((RowSetNavigator)localObject1);
    int i = (this.isGrouped) && (!this.isSimpleDistinct) ? 1 : 0;
    localResult.metaData = this.resultMetaData;
    if (this.isUpdatable) {
      localResult.rsProperties = 8;
    }
    int j = 0;
    int k = paramArrayOfInt[2];
    if (this.sortAndSlice.skipFullResult)
    {
      j = paramArrayOfInt[0];
      k = paramArrayOfInt[1];
    }
    int i4;
    if (this.isSimpleCount)
    {
      Object[] arrayOfObject1 = new Object[this.indexLimitData];
      localObject2 = this.rangeVariables[0].getTable();
      ((Table)localObject2).materialise(paramSession);
      PersistentStore localPersistentStore = ((Table)localObject2).getRowStore(paramSession);
      long l = localPersistentStore.elementCount(paramSession);
      arrayOfObject1[this.indexStartAggregates] = ValuePool.getLong(l);
      ((RowSetNavigatorData)localObject1).add(arrayOfObject1);
      ((RowSetNavigatorData)localObject1).reset();
      paramSession.sessionContext.setRangeIterator((RangeIterator)localObject1);
      if (((RowSetNavigatorData)localObject1).next())
      {
        arrayOfObject1 = ((RowSetNavigatorData)localObject1).getCurrent();
        for (i4 = 0; i4 < this.indexStartAggregates; i4++) {
          arrayOfObject1[i4] = this.exprColumns[i4].getValue(paramSession);
        }
      }
      paramSession.sessionContext.unsetRangeIterator((RangeIterator)localObject1);
      return localResult;
    }
    int m = 0;
    Object localObject2 = new RangeIterator[this.rangeVariables.length];
    for (int n = 0; n < this.rangeVariables.length; n++) {
      localObject2[n] = this.rangeVariables[n].getIterator(paramSession);
    }
    paramSession.sessionContext.rownum = 1;
    n = 0;
    for (;;)
    {
      if (n < m)
      {
        int i1 = 1;
        for (int i3 = m + 1; i3 < this.rangeVariables.length; i3++) {
          if (this.rangeVariables[i3].isRightJoin)
          {
            m = i3;
            n = i3;
            i1 = 0;
            ((RangeVariable.RangeIteratorRight)localObject2[i3]).setOnOuterRows();
            break;
          }
        }
        if (i1 != 0) {
          break;
        }
      }
      else
      {
        Object localObject3 = localObject2[n];
        if (((RangeIterator)localObject3).next())
        {
          if (n < this.rangeVariables.length - 1) {
            n++;
          }
        }
        else
        {
          ((RangeIterator)localObject3).reset();
          n--;
          continue;
        }
        if (k != 0)
        {
          paramSession.sessionData.startRowProcessing();
          Object[] arrayOfObject3 = new Object[this.indexLimitData];
          for (i4 = 0; i4 < this.indexStartAggregates; i4++) {
            if ((!this.isAggregated) || (this.aggregateCheck[i4] == 0)) {
              arrayOfObject3[i4] = this.exprColumns[i4].getValue(paramSession);
            }
          }
          for (i4 = this.indexLimitVisible; i4 < this.indexLimitRowId; i4++) {
            if (i4 == this.indexLimitVisible) {
              arrayOfObject3[i4] = ((RangeIterator)localObject3).getRowidObject();
            } else {
              arrayOfObject3[i4] = ((RangeIterator)localObject3).getCurrentRow();
            }
          }
          paramSession.sessionContext.rownum += 1;
          if (j > 0)
          {
            j--;
            continue;
          }
          Object[] arrayOfObject4 = null;
          if ((this.isAggregated) || (i != 0))
          {
            arrayOfObject4 = ((RowSetNavigatorData)localObject1).getGroupData(arrayOfObject3);
            if (arrayOfObject4 != null) {
              arrayOfObject3 = arrayOfObject4;
            }
          }
          for (int i5 = this.indexStartAggregates; i5 < this.indexLimitExpressions; i5++) {
            arrayOfObject3[i5] = this.exprColumns[i5].updateAggregatingValue(paramSession, arrayOfObject3[i5]);
          }
          if (arrayOfObject4 == null)
          {
            ((RowSetNavigatorData)localObject1).add(arrayOfObject3);
            if (this.isSimpleDistinct)
            {
              for (i5 = 1; i5 < this.rangeVariables.length; i5++) {
                localObject2[i5].reset();
              }
              n = 0;
            }
          }
          else if (this.isAggregated)
          {
            ((RowSetNavigatorData)localObject1).update(arrayOfObject4, arrayOfObject3);
          }
          i5 = ((RowSetNavigatorData)localObject1).getSize();
          if ((i5 == paramSession.resultMaxMemoryRows) && (!this.isAggregated) && (!this.isSingleMemoryTable))
          {
            localObject1 = new RowSetNavigatorDataTable(paramSession, this, (RowSetNavigatorData)localObject1);
            localResult.setNavigator((RowSetNavigator)localObject1);
          }
          if (((this.isAggregated) || (i != 0)) && (!this.sortAndSlice.isGenerated)) {
            continue;
          }
          if (i5 >= k) {
            break;
          }
        }
      }
    }
    ((RowSetNavigatorData)localObject1).reset();
    for (n = 0; n < this.rangeVariables.length; n++) {
      localObject2[n].reset();
    }
    if ((i == 0) && (!this.isAggregated)) {
      return localResult;
    }
    Object[] arrayOfObject2;
    if (this.isAggregated)
    {
      int i2;
      if ((i == 0) && (((RowSetNavigatorData)localObject1).getSize() == 0))
      {
        arrayOfObject2 = new Object[this.exprColumns.length];
        for (i2 = 0; i2 < this.indexStartAggregates; i2++) {
          if (this.aggregateCheck[i2] == 0) {
            arrayOfObject2[i2] = this.exprColumns[i2].getValue(paramSession);
          }
        }
        ((RowSetNavigatorData)localObject1).add(arrayOfObject2);
      }
      ((RowSetNavigatorData)localObject1).reset();
      paramSession.sessionContext.setRangeIterator((RangeIterator)localObject1);
      while (((RowSetNavigatorData)localObject1).next())
      {
        arrayOfObject2 = ((RowSetNavigatorData)localObject1).getCurrent();
        for (i2 = this.indexStartAggregates; i2 < this.indexLimitExpressions; i2++) {
          arrayOfObject2[i2] = this.exprColumns[i2].getAggregatedValue(paramSession, arrayOfObject2[i2]);
        }
        for (i2 = 0; i2 < this.indexStartAggregates; i2++) {
          if (this.aggregateCheck[i2] != 0) {
            arrayOfObject2[i2] = this.exprColumns[i2].getValue(paramSession);
          }
        }
      }
      paramSession.sessionContext.unsetRangeIterator((RangeIterator)localObject1);
    }
    ((RowSetNavigatorData)localObject1).reset();
    if (this.havingCondition != null)
    {
      while (((RowSetNavigatorData)localObject1).hasNext())
      {
        arrayOfObject2 = ((RowSetNavigatorData)localObject1).getNext();
        if (!Boolean.TRUE.equals(arrayOfObject2[(this.indexLimitVisible + this.groupByColumnCount)])) {
          ((RowSetNavigatorData)localObject1).removeCurrent();
        }
      }
      ((RowSetNavigatorData)localObject1).reset();
    }
    return localResult;
  }
  
  void setReferenceableColumns()
  {
    this.accessibleColumns = new boolean[this.indexLimitVisible];
    IntValueHashMap localIntValueHashMap = new IntValueHashMap();
    for (int i = 0; i < this.indexLimitVisible; i++)
    {
      Expression localExpression = this.exprColumns[i];
      String str = localExpression.getAlias();
      if (str.length() == 0)
      {
        HsqlNameManager.HsqlName localHsqlName = HsqlNameManager.getAutoColumnName(i);
        localExpression.setAlias(localHsqlName);
      }
      else
      {
        int j = localIntValueHashMap.get(str, -1);
        if (j == -1)
        {
          localIntValueHashMap.put(str, i);
          this.accessibleColumns[i] = true;
        }
        else
        {
          this.accessibleColumns[j] = false;
        }
      }
    }
  }
  
  void setColumnAliases(HsqlNameManager.SimpleName[] paramArrayOfSimpleName)
  {
    if (paramArrayOfSimpleName.length != this.indexLimitVisible) {
      throw Error.error(5593);
    }
    for (int i = 0; i < this.indexLimitVisible; i++) {
      this.exprColumns[i].setAlias(paramArrayOfSimpleName[i]);
    }
  }
  
  private void createResultMetaData(Session paramSession)
  {
    this.resultMetaData = ResultMetaData.newResultMetaData(this.resultColumnTypes, this.columnMap, this.indexLimitVisible, this.indexLimitRowId);
    for (int i = 0; i < this.indexLimitVisible; i++)
    {
      Expression localExpression = this.exprColumns[i];
      ColumnSchema localColumnSchema = null;
      localColumnSchema = localExpression.getColumn();
      this.resultMetaData.columnTypes[i] = localExpression.getDataType();
      ColumnBase localColumnBase;
      if (localColumnSchema == null) {
        localColumnBase = new ColumnBase();
      } else {
        localColumnBase = new ColumnBase(paramSession.database.getCatalogName().name, localColumnSchema);
      }
      localColumnBase.setType(localExpression.getDataType());
      this.resultMetaData.columns[i] = localColumnBase;
      this.resultMetaData.columnLabels[i] = localExpression.getAlias();
    }
  }
  
  private void setResultNullability()
  {
    for (int i = 0; i < this.indexLimitVisible; i++)
    {
      Expression localExpression = this.exprColumns[i];
      byte b = localExpression.getNullability();
      if (localExpression.opType == 2)
      {
        RangeVariable localRangeVariable = localExpression.getRangeVariable();
        if ((localRangeVariable != null) && ((localRangeVariable.rangePositionInJoin < this.startInnerRange) || (localRangeVariable.rangePositionInJoin >= this.endInnerRange))) {
          b = 1;
        }
      }
      this.resultMetaData.columns[i].setNullability(b);
    }
  }
  
  void createTable(Session paramSession)
  {
    createResultTable(paramSession);
    this.mainIndex = this.resultTable.getPrimaryIndex();
    if ((this.sortAndSlice.hasOrder()) && (!this.sortAndSlice.skipSort)) {
      this.orderIndex = this.sortAndSlice.getNewIndex(paramSession, this.resultTable);
    }
    if ((this.isDistinctSelect) || (this.isFullOrder)) {
      createFullIndex(paramSession);
    }
    int[] arrayOfInt;
    if (this.isGrouped)
    {
      arrayOfInt = new int[this.groupByColumnCount];
      for (int i = 0; i < this.groupByColumnCount; i++) {
        arrayOfInt[i] = (this.indexLimitRowId + i);
      }
      this.groupIndex = this.resultTable.createAndAddIndexStructure(paramSession, null, arrayOfInt, null, null, false, false, false);
    }
    else if (this.isAggregated)
    {
      this.groupIndex = this.mainIndex;
    }
    if ((this.isUpdatable) && (this.view == null))
    {
      arrayOfInt = new int[] { this.indexLimitVisible };
      this.idIndex = this.resultTable.createAndAddIndexStructure(paramSession, null, arrayOfInt, null, null, false, false, false);
    }
  }
  
  private void createFullIndex(Session paramSession)
  {
    int[] arrayOfInt = new int[this.indexLimitVisible];
    ArrayUtil.fillSequence(arrayOfInt);
    this.fullIndex = this.resultTable.createAndAddIndexStructure(paramSession, null, arrayOfInt, null, null, false, false, false);
    this.resultTable.fullIndex = this.fullIndex;
  }
  
  private void setResultColumnTypes()
  {
    this.resultColumnTypes = new Type[this.indexLimitData];
    Expression localExpression;
    for (int i = 0; i < this.indexLimitVisible; i++)
    {
      localExpression = this.exprColumns[i];
      this.resultColumnTypes[i] = localExpression.getDataType();
    }
    for (i = this.indexLimitVisible; i < this.indexLimitRowId; i++) {
      if (i == this.indexLimitVisible) {
        this.resultColumnTypes[i] = Type.SQL_BIGINT;
      } else {
        this.resultColumnTypes[i] = Type.SQL_ALL_TYPES;
      }
    }
    for (i = this.indexLimitRowId; i < this.indexLimitData; i++)
    {
      localExpression = this.exprColumns[i];
      Type localType = localExpression.getDataType();
      if ((localType.getCollation() != localExpression.collation) && (localExpression.collation != null)) {
        localType = Type.getType(localType, localExpression.collation);
      }
      this.resultColumnTypes[i] = localType;
    }
  }
  
  void createResultTable(Session paramSession)
  {
    HsqlNameManager.HsqlName localHsqlName1 = paramSession.database.nameManager.getSubqueryTableName();
    int i = this.persistenceScope == 21 ? 2 : 9;
    HashMappedList localHashMappedList = new HashMappedList();
    for (int j = 0; j < this.indexLimitVisible; j++)
    {
      Expression localExpression = this.exprColumns[j];
      HsqlNameManager.SimpleName localSimpleName = localExpression.getSimpleName();
      String str = localSimpleName.name;
      HsqlNameManager.HsqlName localHsqlName2 = paramSession.database.nameManager.newColumnSchemaHsqlName(localHsqlName1, localSimpleName);
      if (this.accessibleColumns[j] == 0) {
        str = HsqlNameManager.getAutoNoNameColumnString(j);
      }
      ColumnSchema localColumnSchema = new ColumnSchema(localHsqlName2, localExpression.dataType, true, false, null);
      localHashMappedList.add(str, localColumnSchema);
    }
    this.resultTable = new TableDerived(paramSession.database, localHsqlName1, i, this.resultColumnTypes, localHashMappedList, ValuePool.emptyIntArray);
  }
  
  public String getSQL()
  {
    StringBuffer localStringBuffer = new StringBuffer();
    localStringBuffer.append("SELECT").append(' ');
    int i = this.indexLimitVisible;
    for (int j = 0; j < i; j++)
    {
      if (j > 0) {
        localStringBuffer.append(',');
      }
      localStringBuffer.append(this.exprColumns[j].getSQL());
    }
    localStringBuffer.append("FROM");
    i = this.rangeVariables.length;
    for (j = 0; j < i; j++)
    {
      RangeVariable localRangeVariable = this.rangeVariables[j];
      if (j > 0)
      {
        if ((localRangeVariable.isLeftJoin) && (localRangeVariable.isRightJoin)) {
          localStringBuffer.append("FULL").append(' ');
        } else if (localRangeVariable.isLeftJoin) {
          localStringBuffer.append("LEFT").append(' ');
        } else if (localRangeVariable.isRightJoin) {
          localStringBuffer.append("RIGHT").append(' ');
        }
        localStringBuffer.append("JOIN").append(' ');
      }
      localStringBuffer.append(localRangeVariable.getTable().getName().statementName);
    }
    if (this.isGrouped)
    {
      localStringBuffer.append(' ').append("GROUP").append(' ').append("BY");
      i = this.indexLimitVisible + this.groupByColumnCount;
      for (j = this.indexLimitVisible; j < i; j++)
      {
        localStringBuffer.append(this.exprColumns[j].getSQL());
        if (j < i - 1) {
          localStringBuffer.append(',');
        }
      }
    }
    if (this.havingCondition != null)
    {
      localStringBuffer.append(' ').append("HAVING").append(' ');
      localStringBuffer.append(this.havingCondition.getSQL());
    }
    if (this.sortAndSlice.hasOrder())
    {
      i = this.indexStartOrderBy + this.sortAndSlice.getOrderLength();
      localStringBuffer.append(' ').append("ORDER").append("BY").append(' ');
      for (j = this.indexStartOrderBy; j < i; j++)
      {
        localStringBuffer.append(this.exprColumns[j].getSQL());
        if (j < i - 1) {
          localStringBuffer.append(',');
        }
      }
    }
    if (this.sortAndSlice.hasLimit()) {
      localStringBuffer.append(this.sortAndSlice.limitCondition.getLeftNode().getSQL());
    }
    return localStringBuffer.toString();
  }
  
  public ResultMetaData getMetaData()
  {
    return this.resultMetaData;
  }
  
  public String describe(Session paramSession, int paramInt)
  {
    StringBuffer localStringBuffer2 = new StringBuffer(paramInt);
    for (int i = 0; i < paramInt; i++) {
      localStringBuffer2.append(' ');
    }
    StringBuffer localStringBuffer1 = new StringBuffer();
    localStringBuffer1.append(localStringBuffer2).append("isDistinctSelect=[").append(this.isDistinctSelect).append("]\n");
    localStringBuffer1.append(localStringBuffer2).append("isGrouped=[").append(this.isGrouped).append("]\n");
    localStringBuffer1.append(localStringBuffer2).append("isAggregated=[").append(this.isAggregated).append("]\n");
    localStringBuffer1.append(localStringBuffer2).append("columns=[");
    int j;
    for (i = 0; i < this.indexLimitVisible; i++)
    {
      j = i;
      if (this.exprColumns[i].getType() == 5) {
        j = this.exprColumns[i].columnIndex;
      }
      localStringBuffer1.append(localStringBuffer2);
      str = this.exprColumns[j].describe(paramSession, 2);
      localStringBuffer1.append(str.substring(0, str.length() - 1));
      if (this.resultMetaData.columns[i].getNullability() == 0) {
        localStringBuffer1.append(" not nullable\n");
      } else {
        localStringBuffer1.append(" nullable\n");
      }
    }
    localStringBuffer1.append("\n");
    localStringBuffer1.append(localStringBuffer2).append("]\n");
    for (i = 0; i < this.rangeVariables.length; i++)
    {
      localStringBuffer1.append(localStringBuffer2).append("[");
      localStringBuffer1.append("range variable ").append(i + 1).append("\n");
      localStringBuffer1.append(this.rangeVariables[i].describe(paramSession, paramInt + 2));
      localStringBuffer1.append(localStringBuffer2).append("]");
    }
    localStringBuffer1.append(localStringBuffer2).append("]\n");
    String str = this.queryCondition == null ? "null" : this.queryCondition.describe(paramSession, paramInt);
    if (this.isGrouped)
    {
      localStringBuffer1.append(localStringBuffer2).append("groupColumns=[");
      for (i = this.indexLimitRowId; i < this.indexLimitRowId + this.groupByColumnCount; i++)
      {
        j = i;
        if (this.exprColumns[i].getType() == 5) {
          j = this.exprColumns[i].columnIndex;
        }
        localStringBuffer1.append(this.exprColumns[j].describe(paramSession, paramInt));
      }
      localStringBuffer1.append(localStringBuffer2).append("]\n");
    }
    if (this.havingCondition != null)
    {
      str = this.havingCondition.describe(paramSession, paramInt);
      localStringBuffer1.append(localStringBuffer2).append("havingCondition=[").append(str).append("]\n");
    }
    if (this.sortAndSlice.hasOrder())
    {
      localStringBuffer1.append(localStringBuffer2).append("order by=[\n");
      for (i = 0; i < this.sortAndSlice.exprList.size(); i++) {
        localStringBuffer1.append(localStringBuffer2).append(((Expression)this.sortAndSlice.exprList.get(i)).describe(paramSession, paramInt));
      }
      if (this.sortAndSlice.primaryTableIndex != null) {
        localStringBuffer1.append(localStringBuffer2).append("uses index");
      }
      localStringBuffer1.append(localStringBuffer2).append("]\n");
    }
    if (this.sortAndSlice.hasLimit())
    {
      if (this.sortAndSlice.limitCondition.getLeftNode() != null) {
        localStringBuffer1.append(localStringBuffer2).append("offset=[").append(this.sortAndSlice.limitCondition.getLeftNode().describe(paramSession, localStringBuffer2.length())).append("]\n");
      }
      if (this.sortAndSlice.limitCondition.getRightNode() != null) {
        localStringBuffer1.append(localStringBuffer2).append("limit=[").append(this.sortAndSlice.limitCondition.getRightNode().describe(paramSession, localStringBuffer2.length())).append("]\n");
      }
    }
    return localStringBuffer1.toString();
  }
  
  void setMergeability()
  {
    this.isOrderSensitive |= ((this.sortAndSlice.hasLimit()) || (this.sortAndSlice.hasOrder()));
    if (this.isOrderSensitive) {
      this.isMergeable = false;
    }
    if (this.isAggregated) {
      this.isMergeable = false;
    }
    if ((this.isGrouped) || (this.isDistinctSelect)) {
      this.isMergeable = false;
    }
    if (this.rangeVariables.length != 1)
    {
      this.isBaseMergeable = false;
      this.isMergeable = false;
    }
  }
  
  void setUpdatability()
  {
    if (!this.isUpdatable) {
      return;
    }
    this.isUpdatable = false;
    if ((this.isGrouped) || (this.isDistinctSelect) || (this.isAggregated)) {
      return;
    }
    if (!this.isBaseMergeable) {
      return;
    }
    if (!this.isTopLevel) {
      return;
    }
    if ((this.sortAndSlice.hasLimit()) || (this.sortAndSlice.hasOrder())) {
      return;
    }
    RangeVariable localRangeVariable = this.rangeVariables[0];
    Table localTable1 = localRangeVariable.getTable();
    Table localTable2 = localTable1.getBaseTable();
    if (localTable2 == null) {
      return;
    }
    this.isInsertable = localTable1.isInsertable();
    this.isUpdatable = localTable1.isUpdatable();
    if ((!this.isInsertable) && (!this.isUpdatable)) {
      return;
    }
    IntValueHashMap localIntValueHashMap = new IntValueHashMap();
    int[] arrayOfInt1 = localTable1.getBaseTableColumnMap();
    int[] arrayOfInt2 = new int[this.indexLimitVisible];
    if (this.queryCondition != null)
    {
      this.tempSet.clear();
      collectSubQueriesAndReferences(this.tempSet, this.queryCondition);
      if ((this.tempSet.contains(localTable1.getName())) || (this.tempSet.contains(localTable2.getName())))
      {
        this.isUpdatable = false;
        this.isInsertable = false;
        return;
      }
    }
    Object localObject;
    String str;
    for (int i = 0; i < this.indexLimitVisible; i++)
    {
      localObject = this.exprColumns[i];
      if (((Expression)localObject).getType() == 2)
      {
        str = ((Expression)localObject).getColumn().getName().name;
        if (localIntValueHashMap.containsKey(str)) {
          localIntValueHashMap.put(str, 1);
        } else {
          localIntValueHashMap.put(str, 0);
        }
      }
      else
      {
        this.tempSet.clear();
        collectSubQueriesAndReferences(this.tempSet, (Expression)localObject);
        if (this.tempSet.contains(localTable1.getName()))
        {
          this.isUpdatable = false;
          this.isInsertable = false;
          return;
        }
      }
    }
    this.isUpdatable = false;
    for (i = 0; i < this.indexLimitVisible; i++)
    {
      if (this.accessibleColumns[i] != 0)
      {
        localObject = this.exprColumns[i];
        if (((Expression)localObject).getType() == 2)
        {
          str = ((Expression)localObject).getColumn().getName().name;
          if (localIntValueHashMap.get(str) == 0)
          {
            int j = localTable1.findColumn(str);
            arrayOfInt2[i] = arrayOfInt1[j];
            if (arrayOfInt2[i] == -1) {
              continue;
            }
            this.isUpdatable = true;
            continue;
          }
        }
      }
      arrayOfInt2[i] = -1;
      this.isInsertable = false;
    }
    if (this.isInsertable)
    {
      boolean[] arrayOfBoolean = localTable2.getColumnCheckList(arrayOfInt2);
      for (i = 0; i < arrayOfBoolean.length; i++) {
        if (arrayOfBoolean[i] == 0)
        {
          localObject = localTable2.getColumn(i);
          if ((!((ColumnSchema)localObject).isIdentity()) && (!((ColumnSchema)localObject).isGenerated()) && (!((ColumnSchema)localObject).hasDefault()) && (!((ColumnSchema)localObject).isNullable()))
          {
            this.isInsertable = false;
            break;
          }
        }
      }
    }
    if (!this.isUpdatable) {
      this.isInsertable = false;
    }
    if (this.isUpdatable)
    {
      this.columnMap = arrayOfInt2;
      this.baseTable = localTable2;
      if (this.view != null) {
        return;
      }
      this.indexLimitRowId += 1;
      this.hasRowID = true;
      if (!localTable2.isFileBased())
      {
        this.indexLimitRowId += 1;
        this.isSingleMemoryTable = true;
      }
      this.indexLimitData = this.indexLimitRowId;
    }
  }
  
  void mergeQuery()
  {
    RangeVariable localRangeVariable = this.rangeVariables[0];
    Table localTable = localRangeVariable.getTable();
    Expression localExpression1 = this.queryCondition;
    QueryExpression localQueryExpression = localTable.getQueryExpression();
    if ((this.isBaseMergeable) && (localQueryExpression != null) && (localQueryExpression.isMergeable))
    {
      QuerySpecification localQuerySpecification = localQueryExpression.getMainSelect();
      this.rangeVariables[0] = localQuerySpecification.rangeVariables[0];
      this.rangeVariables[0].resetConditions();
      for (int i = 0; i < this.indexLimitExpressions; i++)
      {
        Expression localExpression3 = this.exprColumns[i];
        this.exprColumns[i] = localExpression3.replaceColumnReferences(localRangeVariable, localQuerySpecification.exprColumns);
      }
      if (localExpression1 != null) {
        localExpression1 = localExpression1.replaceColumnReferences(localRangeVariable, localQuerySpecification.exprColumns);
      }
      Expression localExpression2 = localQuerySpecification.queryCondition;
      this.checkQueryCondition = localQuerySpecification.checkQueryCondition;
      this.queryCondition = ExpressionLogical.andExpressions(localExpression2, localExpression1);
    }
    if (this.view != null) {
      switch (this.view.getCheckOption())
      {
      case 1: 
        if (!this.isUpdatable) {
          throw Error.error(5537);
        }
        this.checkQueryCondition = localExpression1;
        break;
      case 2: 
        if (!this.isUpdatable) {
          throw Error.error(5537);
        }
        this.checkQueryCondition = this.queryCondition;
      }
    }
  }
  
  static void collectSubQueriesAndReferences(OrderedHashSet paramOrderedHashSet, Expression paramExpression)
  {
    paramExpression.collectAllExpressions(paramOrderedHashSet, Expression.subqueryExpressionSet, Expression.emptyExpressionSet);
    int i = paramOrderedHashSet.size();
    for (int j = 0; j < i; j++)
    {
      Expression localExpression = (Expression)paramOrderedHashSet.get(j);
      localExpression.collectObjectNames(paramOrderedHashSet);
    }
  }
  
  public OrderedHashSet getSubqueries()
  {
    OrderedHashSet localOrderedHashSet1 = null;
    for (int i = 0; i < this.indexLimitExpressions; i++) {
      localOrderedHashSet1 = this.exprColumns[i].collectAllSubqueries(localOrderedHashSet1);
    }
    if (this.queryCondition != null) {
      localOrderedHashSet1 = this.queryCondition.collectAllSubqueries(localOrderedHashSet1);
    }
    if (this.havingCondition != null) {
      localOrderedHashSet1 = this.havingCondition.collectAllSubqueries(localOrderedHashSet1);
    }
    for (i = 0; i < this.rangeVariables.length; i++)
    {
      OrderedHashSet localOrderedHashSet2 = this.rangeVariables[i].getSubqueries();
      localOrderedHashSet1 = OrderedHashSet.addAll(localOrderedHashSet1, localOrderedHashSet2);
    }
    return localOrderedHashSet1;
  }
  
  public Table getBaseTable()
  {
    return this.baseTable;
  }
  
  public OrderedHashSet collectAllSubqueries(OrderedHashSet paramOrderedHashSet)
  {
    return paramOrderedHashSet;
  }
  
  public OrderedHashSet collectOuterColumnExpressions(OrderedHashSet paramOrderedHashSet1, OrderedHashSet paramOrderedHashSet2)
  {
    paramOrderedHashSet1 = collectAllExpressions(paramOrderedHashSet1, Expression.columnExpressionSet, Expression.subqueryAggregateExpressionSet);
    if (paramOrderedHashSet1 == null) {
      return null;
    }
    for (int i = paramOrderedHashSet1.size() - 1; i >= 0; i--)
    {
      Expression localExpression = (Expression)paramOrderedHashSet1.get(i);
      if (ArrayUtil.find(this.rangeVariables, localExpression.getRangeVariable()) >= 0) {
        paramOrderedHashSet1.remove(i);
      }
      if (paramOrderedHashSet2.contains(localExpression)) {
        paramOrderedHashSet1.remove(i);
      }
    }
    if (paramOrderedHashSet1.isEmpty()) {
      paramOrderedHashSet1 = null;
    }
    return paramOrderedHashSet1;
  }
  
  public OrderedHashSet collectAllExpressions(OrderedHashSet paramOrderedHashSet, OrderedIntHashSet paramOrderedIntHashSet1, OrderedIntHashSet paramOrderedIntHashSet2)
  {
    for (int i = 0; i < this.indexStartAggregates; i++) {
      paramOrderedHashSet = this.exprColumns[i].collectAllExpressions(paramOrderedHashSet, paramOrderedIntHashSet1, paramOrderedIntHashSet2);
    }
    if (this.queryCondition != null) {
      paramOrderedHashSet = this.queryCondition.collectAllExpressions(paramOrderedHashSet, paramOrderedIntHashSet1, paramOrderedIntHashSet2);
    }
    if (this.havingCondition != null) {
      paramOrderedHashSet = this.havingCondition.collectAllExpressions(paramOrderedHashSet, paramOrderedIntHashSet1, paramOrderedIntHashSet2);
    }
    for (i = 0; i < this.rangeVariables.length; i++) {
      this.rangeVariables[i].collectAllExpressions(paramOrderedHashSet, paramOrderedIntHashSet1, paramOrderedIntHashSet2);
    }
    return paramOrderedHashSet;
  }
  
  public void collectObjectNames(Set paramSet)
  {
    for (int i = 0; i < this.indexStartAggregates; i++) {
      this.exprColumns[i].collectObjectNames(paramSet);
    }
    if (this.queryCondition != null) {
      this.queryCondition.collectObjectNames(paramSet);
    }
    if (this.havingCondition != null) {
      this.havingCondition.collectObjectNames(paramSet);
    }
    i = 0;
    int j = this.rangeVariables.length;
    while (i < j)
    {
      HsqlNameManager.HsqlName localHsqlName = this.rangeVariables[i].getTable().getName();
      paramSet.add(localHsqlName);
      i++;
    }
  }
  
  public void replaceColumnReferences(RangeVariable paramRangeVariable, Expression[] paramArrayOfExpression)
  {
    for (int i = 0; i < this.indexStartAggregates; i++) {
      this.exprColumns[i] = this.exprColumns[i].replaceColumnReferences(paramRangeVariable, paramArrayOfExpression);
    }
    if (this.queryCondition != null) {
      this.queryCondition = this.queryCondition.replaceColumnReferences(paramRangeVariable, paramArrayOfExpression);
    }
    if (this.havingCondition != null) {
      this.havingCondition = this.havingCondition.replaceColumnReferences(paramRangeVariable, paramArrayOfExpression);
    }
    i = 0;
    int j = this.rangeVariables.length;
    while (i < j)
    {
      this.rangeVariables[i].replaceColumnReferences(paramRangeVariable, paramArrayOfExpression);
      i++;
    }
  }
  
  public void replaceRangeVariables(RangeVariable[] paramArrayOfRangeVariable1, RangeVariable[] paramArrayOfRangeVariable2)
  {
    for (int i = 0; i < this.indexStartAggregates; i++) {
      this.exprColumns[i].replaceRangeVariables(paramArrayOfRangeVariable1, paramArrayOfRangeVariable2);
    }
    if (this.queryCondition != null) {
      this.queryCondition.replaceRangeVariables(paramArrayOfRangeVariable1, paramArrayOfRangeVariable2);
    }
    if (this.havingCondition != null) {
      this.havingCondition.replaceRangeVariables(paramArrayOfRangeVariable1, paramArrayOfRangeVariable2);
    }
    i = 0;
    int j = this.rangeVariables.length;
    while (i < j)
    {
      this.rangeVariables[i].getSubqueries();
      i++;
    }
  }
  
  public void setReturningResult()
  {
    setReturningResultSet();
    this.acceptsSequences = true;
    this.isTopLevel = true;
  }
  
  void setReturningResultSet()
  {
    this.persistenceScope = 23;
  }
  
  public boolean isSingleColumn()
  {
    return this.indexLimitVisible == 1;
  }
  
  public String[] getColumnNames()
  {
    String[] arrayOfString = new String[this.indexLimitVisible];
    for (int i = 0; i < this.indexLimitVisible; i++) {
      arrayOfString[i] = this.exprColumns[i].getAlias();
    }
    return arrayOfString;
  }
  
  public Type[] getColumnTypes()
  {
    if (this.resultColumnTypes.length == this.indexLimitVisible) {
      return this.resultColumnTypes;
    }
    Type[] arrayOfType = new Type[this.indexLimitVisible];
    ArrayUtil.copyArray(this.resultColumnTypes, arrayOfType, arrayOfType.length);
    return arrayOfType;
  }
  
  public int getColumnCount()
  {
    return this.indexLimitVisible;
  }
  
  public int[] getBaseTableColumnMap()
  {
    return this.columnMap;
  }
  
  public Expression getCheckCondition()
  {
    return this.queryCondition;
  }
  
  void getBaseTableNames(OrderedHashSet paramOrderedHashSet)
  {
    for (int i = 0; i < this.rangeVariables.length; i++)
    {
      Table localTable = this.rangeVariables[i].rangeTable;
      HsqlNameManager.HsqlName localHsqlName = localTable.getName();
      if ((!localTable.isView()) && (!localTable.isDataReadOnly()) && (!localTable.isTemp()) && (localHsqlName.schema != SqlInvariants.SYSTEM_SCHEMA_HSQLNAME)) {
        paramOrderedHashSet.add(localHsqlName);
      }
    }
  }
  
  boolean isEquivalent(QueryExpression paramQueryExpression)
  {
    if (!(paramQueryExpression instanceof QuerySpecification)) {
      return false;
    }
    QuerySpecification localQuerySpecification = (QuerySpecification)paramQueryExpression;
    if (!Expression.equals(this.exprColumns, localQuerySpecification.exprColumns)) {
      return false;
    }
    if (!Expression.equals(this.queryCondition, localQuerySpecification.queryCondition)) {
      return false;
    }
    if (this.rangeVariables.length != localQuerySpecification.rangeVariables.length) {
      return false;
    }
    for (int i = 0; i < this.rangeVariables.length; i++) {
      if (this.rangeVariables[i].getTable() != localQuerySpecification.rangeVariables[i].getTable()) {
        return false;
      }
    }
    return true;
  }
}


/* Location:              E:\java\java学习\hutubill\lib\all.jar!\org\hsqldb\QuerySpecification.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */
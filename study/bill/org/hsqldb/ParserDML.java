package org.hsqldb;

import org.hsqldb.error.Error;
import org.hsqldb.lib.ArrayUtil;
import org.hsqldb.lib.HashMappedList;
import org.hsqldb.lib.HsqlArrayList;
import org.hsqldb.lib.HsqlList;
import org.hsqldb.lib.LongDeque;
import org.hsqldb.lib.OrderedHashSet;
import org.hsqldb.map.ValuePool;
import org.hsqldb.types.Type;

public class ParserDML
  extends ParserDQL
{
  ParserDML(Session paramSession, Scanner paramScanner)
  {
    super(paramSession, paramScanner, null);
  }
  
  StatementDMQL compileInsertStatement(RangeGroup[] paramArrayOfRangeGroup)
  {
    boolean[] arrayOfBoolean2 = null;
    int[] arrayOfInt = ValuePool.emptyIntArray;
    int j = 0;
    int k = 0;
    int m = 0;
    int n = 0;
    int i1 = 0;
    Expression[] arrayOfExpression1 = Expression.emptyArray;
    Expression[] arrayOfExpression2 = null;
    if (this.database.sqlSyntaxMys)
    {
      if (readIfThis(761)) {
        i1 = 2;
      }
      if (i1 == 0)
      {
        readThis(145);
        if (readIfThis(435)) {
          i1 = 1;
        }
      }
      readIfThis(151);
    }
    else
    {
      readThis(145);
      readThis(151);
    }
    Token localToken = getRecordedToken();
    RangeVariable localRangeVariable = readRangeVariableForDataChange(55);
    localRangeVariable.resolveRangeTableTypes(this.session, RangeVariable.emptyArray);
    Table localTable1 = localRangeVariable.getTable();
    boolean[] arrayOfBoolean1 = null;
    Object localObject1 = localTable1.getColumnMap();
    int i = localTable1.getColumnCount();
    int i2 = getPosition();
    Table localTable2 = localTable1.isTriggerInsertable() ? localTable1 : localTable1.getBaseTable();
    Expression localExpression;
    Object localObject4;
    switch (this.token.tokenType)
    {
    case 83: 
      read();
      readThis(324);
      localExpression = new Expression(25, new Expression[0]);
      localExpression = new Expression(26, new Expression[] { localExpression });
      arrayOfBoolean1 = localTable1.getNewColumnCheckList();
      StatementInsert localStatementInsert = new StatementInsert(this.session, localTable1, (int[])localObject1, localExpression, arrayOfBoolean1, arrayOfExpression1, arrayOfBoolean2, arrayOfInt, null, i1, this.compileContext);
      return localStatementInsert;
    case 836: 
      int i3 = readOpenBrackets();
      if (i3 == 1)
      {
        int i5 = 0;
        switch (this.token.tokenType)
        {
        case 265: 
        case 294: 
        case 336: 
          rewind(i2);
          i5 = 1;
          break;
        }
        if (i5 == 0)
        {
          localObject4 = new OrderedHashSet();
          boolean bool = this.database.sqlSyntaxOra;
          readSimpleColumnNames((OrderedHashSet)localObject4, localRangeVariable, bool);
          readThis(822);
          i = ((OrderedHashSet)localObject4).size();
          localObject1 = localTable1.getColumnIndexes((OrderedHashSet)localObject4);
          n = 1;
        }
      }
      else
      {
        rewind(i2);
      }
      break;
    }
    if (this.token.tokenType == 483)
    {
      read();
      if (this.token.tokenType == 321)
      {
        read();
        j = 1;
      }
      else if (this.token.tokenType == 291)
      {
        read();
        k = 1;
      }
      else
      {
        throw unexpectedToken();
      }
      readThis(323);
    }
    Object localObject2;
    Object localObject3;
    Object localObject8;
    Object localObject9;
    switch (this.token.tokenType)
    {
    case 323: 
      if (!this.database.sqlSyntaxMys) {
        throw unexpectedToken();
      }
    case 324: 
      read();
      arrayOfBoolean1 = localTable1.getColumnCheckList((int[])localObject1);
      localExpression = XreadContextuallyTypedTable(i);
      localObject2 = localExpression.resolveColumnReferences(this.session, RangeGroup.emptyGroup, paramArrayOfRangeGroup, null);
      ExpressionColumn.checkColumnsResolved((HsqlList)localObject2);
      localExpression.resolveTypes(this.session, null);
      setParameterTypes(localExpression, localTable1, (int[])localObject1);
      if (localTable1 != localTable2)
      {
        localObject3 = localTable1.getBaseTableColumnMap();
        localObject4 = new int[localObject1.length];
        ArrayUtil.projectRow((int[])localObject3, (int[])localObject1, (int[])localObject4);
        localObject1 = localObject4;
      }
      localObject3 = localExpression.nodes;
      for (int i7 = 0; i7 < localObject3.length; i7++)
      {
        localObject6 = localObject3[i7].nodes;
        for (int i8 = 0; i8 < localObject6.length; i8++)
        {
          localObject8 = localObject6[i8];
          localObject9 = localTable2.getColumn(localObject1[i8]);
          if (((ColumnSchema)localObject9).isIdentity())
          {
            m = 1;
            if (((Expression)localObject8).getType() != 4)
            {
              if ((localTable2.identitySequence.isAlways()) && (j == 0) && (k == 0)) {
                throw Error.error(5543);
              }
              if (j != 0) {
                localObject6[i8] = new ExpressionColumn(4);
              }
            }
          }
          else if ((!((ColumnSchema)localObject9).hasDefault()) && (((ColumnSchema)localObject9).isGenerated()) && (((Expression)localObject8).getType() != 4))
          {
            throw Error.error(5541);
          }
          if (((Expression)localObject8).isUnresolvedParam()) {
            ((Expression)localObject8).setAttributesAsColumn((ColumnSchema)localObject9, true);
          }
        }
      }
      if ((m == 0) && ((j != 0) || (k != 0))) {
        throw unexpectedTokenRequire("OVERRIDING");
      }
      if (n == 0) {
        localToken.setWithColumnList();
      }
      if ((this.database.sqlSyntaxMys) && (i1 == 0) && (readIfThis(204)))
      {
        readThis(812);
        readThis(449);
        readThis(319);
        localObject5 = new OrderedHashSet();
        localObject6 = new LongDeque();
        localObject7 = new HsqlArrayList();
        localObject8 = new RangeVariable[] { localRangeVariable };
        localObject9 = new RangeGroup.RangeGroupSimple((RangeVariable[])localObject8, false);
        i1 = 3;
        readSetClauseList((RangeVariable[])localObject8, (OrderedHashSet)localObject5, (LongDeque)localObject6, (HsqlArrayList)localObject7);
        arrayOfInt = new int[((LongDeque)localObject6).size()];
        ((LongDeque)localObject6).toArray(arrayOfInt);
        arrayOfExpression2 = new Expression[((OrderedHashSet)localObject5).size()];
        ((OrderedHashSet)localObject5).toArray(arrayOfExpression2);
        for (int i9 = 0; i9 < arrayOfExpression2.length; i9++) {
          resolveReferencesAndTypes((RangeGroup)localObject9, paramArrayOfRangeGroup, arrayOfExpression2[i9]);
        }
        arrayOfBoolean2 = localTable1.getColumnCheckList(arrayOfInt);
        arrayOfExpression1 = new Expression[((HsqlArrayList)localObject7).size()];
        ((HsqlArrayList)localObject7).toArray(arrayOfExpression1);
        resolveUpdateExpressions(localTable1, (RangeGroup)localObject9, arrayOfInt, arrayOfExpression1, paramArrayOfRangeGroup);
      }
      localObject5 = new StatementInsert(this.session, localTable1, (int[])localObject1, localExpression, arrayOfBoolean1, arrayOfExpression1, arrayOfBoolean2, arrayOfInt, arrayOfExpression2, i1, this.compileContext);
      return (StatementDMQL)localObject5;
    case 265: 
    case 294: 
    case 336: 
    case 836: 
      break;
    }
    throw unexpectedToken();
    arrayOfBoolean1 = localTable1.getColumnCheckList((int[])localObject1);
    if (localTable1 != localTable2)
    {
      localObject2 = localTable1.getBaseTableColumnMap();
      localObject3 = new int[localObject1.length];
      ArrayUtil.projectRow((int[])localObject2, (int[])localObject1, (int[])localObject3);
      localObject1 = localObject3;
    }
    int i4 = localTable2.getIdentityColumnIndex();
    int i6 = -1;
    if ((i4 != -1) && (ArrayUtil.find((int[])localObject1, i4) > -1))
    {
      if ((localTable2.identitySequence.isAlways()) && (j == 0) && (k == 0)) {
        throw Error.error(5543);
      }
      if (j != 0) {
        i6 = i4;
      }
    }
    else if ((j != 0) || (k != 0))
    {
      throw unexpectedTokenRequire("OVERRIDING");
    }
    Object localObject5 = new Type[localObject1.length];
    ArrayUtil.projectRow(localTable2.getColumnTypes(), (int[])localObject1, (Object[])localObject5);
    this.compileContext.setOuterRanges(paramArrayOfRangeGroup);
    Object localObject6 = XreadQueryExpression();
    ((QueryExpression)localObject6).setReturningResult();
    ((QueryExpression)localObject6).resolve(this.session, paramArrayOfRangeGroup, (Type[])localObject5);
    if (i != ((QueryExpression)localObject6).getColumnCount()) {
      throw Error.error(5546);
    }
    if (n == 0) {
      localToken.setWithColumnList();
    }
    if ((this.database.sqlSyntaxMys) && (i1 == 0) && (readIfThis(204)))
    {
      readThis(812);
      readThis(449);
      readThis(319);
      localObject7 = new OrderedHashSet();
      localObject8 = new LongDeque();
      localObject9 = new HsqlArrayList();
      RangeVariable[] arrayOfRangeVariable = { localRangeVariable };
      RangeGroup.RangeGroupSimple localRangeGroupSimple = new RangeGroup.RangeGroupSimple(arrayOfRangeVariable, false);
      i1 = 3;
      readSetClauseList(arrayOfRangeVariable, (OrderedHashSet)localObject7, (LongDeque)localObject8, (HsqlArrayList)localObject9);
      arrayOfInt = new int[((LongDeque)localObject8).size()];
      ((LongDeque)localObject8).toArray(arrayOfInt);
      arrayOfExpression2 = new Expression[((OrderedHashSet)localObject7).size()];
      ((OrderedHashSet)localObject7).toArray(arrayOfExpression2);
      for (int i10 = 0; i10 < arrayOfExpression2.length; i10++) {
        resolveReferencesAndTypes(localRangeGroupSimple, paramArrayOfRangeGroup, arrayOfExpression2[i10]);
      }
      arrayOfBoolean2 = localTable1.getColumnCheckList(arrayOfInt);
      arrayOfExpression1 = new Expression[((HsqlArrayList)localObject9).size()];
      ((HsqlArrayList)localObject9).toArray(arrayOfExpression1);
      resolveUpdateExpressions(localTable1, localRangeGroupSimple, arrayOfInt, arrayOfExpression1, paramArrayOfRangeGroup);
    }
    Object localObject7 = new StatementInsert(this.session, localTable1, (int[])localObject1, arrayOfBoolean1, (QueryExpression)localObject6, arrayOfExpression1, arrayOfBoolean2, arrayOfInt, arrayOfExpression2, i1, i6, this.compileContext);
    return (StatementDMQL)localObject7;
  }
  
  private static void setParameterTypes(Expression paramExpression, Table paramTable, int[] paramArrayOfInt)
  {
    for (int i = 0; i < paramExpression.nodes.length; i++)
    {
      Expression[] arrayOfExpression = paramExpression.nodes[i].nodes;
      for (int j = 0; j < arrayOfExpression.length; j++) {
        if (arrayOfExpression[j].isUnresolvedParam()) {
          arrayOfExpression[j].setAttributesAsColumn(paramTable.getColumn(paramArrayOfInt[j]), true);
        }
      }
    }
  }
  
  Statement compileTruncateStatement()
  {
    int i = 0;
    int j = 0;
    boolean bool1 = false;
    boolean bool2 = false;
    HsqlNameManager.HsqlName localHsqlName = null;
    RangeVariable[] arrayOfRangeVariable = null;
    Table localTable = null;
    HsqlNameManager.HsqlName[] arrayOfHsqlName = null;
    RangeVariable localRangeVariable = null;
    readThis(311);
    if (this.token.tokenType == 294)
    {
      readThis(294);
      localRangeVariable = readRangeVariableForDataChange(1215);
      arrayOfRangeVariable = new RangeVariable[] { localRangeVariable };
      localTable = arrayOfRangeVariable[0].getTable();
      localHsqlName = localTable.getName();
      i = 1;
    }
    else
    {
      readThis(519);
      localHsqlName = readSchemaName();
    }
    switch (this.token.tokenType)
    {
    case 397: 
      read();
      readThis(138);
      break;
    case 506: 
      read();
      readThis(138);
      bool2 = true;
      break;
    }
    if (i == 0) {
      checkIsThis(5);
    }
    if (readIfThis(5))
    {
      readThis(47);
      j = 1;
      if (readIfThis(190))
      {
        readThis(40);
        bool1 = true;
      }
    }
    if (i != 0) {
      arrayOfHsqlName = new HsqlNameManager.HsqlName[] { localTable.getName() };
    } else {
      arrayOfHsqlName = this.session.database.schemaManager.getCatalogAndBaseTableNames();
    }
    if (j != 0)
    {
      localObject = new Object[] { localHsqlName, Boolean.valueOf(bool2), Boolean.valueOf(bool1) };
      return new StatementCommand(1215, (Object[])localObject, null, arrayOfHsqlName);
    }
    Object localObject = new StatementDML(this.session, localTable, localRangeVariable, arrayOfRangeVariable, this.compileContext, bool2, 1215, null);
    return (Statement)localObject;
  }
  
  Statement compileDeleteStatement(RangeGroup[] paramArrayOfRangeGroup)
  {
    Expression localExpression = null;
    boolean bool = false;
    readThis(84);
    readThis(124);
    RangeVariable localRangeVariable = readRangeVariableForDataChange(19);
    RangeVariable[] arrayOfRangeVariable = { localRangeVariable };
    RangeGroup.RangeGroupSimple localRangeGroupSimple = new RangeGroup.RangeGroupSimple(arrayOfRangeVariable, false);
    Table localTable1 = arrayOfRangeVariable[0].getTable();
    this.compileContext.setOuterRanges(paramArrayOfRangeGroup);
    if (this.token.tokenType == 333)
    {
      read();
      localExpression = XreadAndResolveBooleanValueExpression(paramArrayOfRangeGroup, localRangeGroupSimple);
    }
    SortAndSlice localSortAndSlice = null;
    if (this.token.tokenType == 625) {
      localSortAndSlice = XreadOrderByExpression();
    }
    Table localTable2 = localTable1.isTriggerDeletable() ? localTable1 : localTable1.getBaseTable();
    Object localObject;
    if (localTable1 != localTable2)
    {
      localObject = localTable1.getQueryExpression().getMainSelect();
      if (localExpression != null) {
        localExpression = localExpression.replaceColumnReferences(arrayOfRangeVariable[0], ((QuerySpecification)localObject).exprColumns);
      }
      localExpression = ExpressionLogical.andExpressions(((QuerySpecification)localObject).queryCondition, localExpression);
      arrayOfRangeVariable = ((QuerySpecification)localObject).rangeVariables;
      ArrayUtil.fillArray(arrayOfRangeVariable[0].usedColumns, true);
    }
    if (localExpression != null)
    {
      arrayOfRangeVariable[0].addJoinCondition(localExpression);
      localObject = new RangeVariableResolver(this.session, arrayOfRangeVariable, null, this.compileContext, false);
      ((RangeVariableResolver)localObject).processConditions();
      arrayOfRangeVariable = ((RangeVariableResolver)localObject).rangeVariables;
    }
    for (int i = 0; i < arrayOfRangeVariable.length; i++) {
      arrayOfRangeVariable[i].resolveRangeTableTypes(this.session, RangeVariable.emptyArray);
    }
    StatementDML localStatementDML = new StatementDML(this.session, localTable1, localRangeVariable, arrayOfRangeVariable, this.compileContext, bool, 19, localSortAndSlice);
    return localStatementDML;
  }
  
  StatementDMQL compileUpdateStatement(RangeGroup[] paramArrayOfRangeGroup)
  {
    read();
    OrderedHashSet localOrderedHashSet = new OrderedHashSet();
    LongDeque localLongDeque = new LongDeque();
    HsqlArrayList localHsqlArrayList = new HsqlArrayList();
    RangeVariable localRangeVariable = readRangeVariableForDataChange(92);
    RangeVariable[] arrayOfRangeVariable = { localRangeVariable };
    RangeGroup.RangeGroupSimple localRangeGroupSimple = new RangeGroup.RangeGroupSimple(arrayOfRangeVariable, false);
    Table localTable1 = arrayOfRangeVariable[0].rangeTable;
    Table localTable2 = localTable1.isTriggerUpdatable() ? localTable1 : localTable1.getBaseTable();
    readThis(268);
    readSetClauseList(arrayOfRangeVariable, localOrderedHashSet, localLongDeque, localHsqlArrayList);
    Object localObject1 = new int[localLongDeque.size()];
    localLongDeque.toArray((int[])localObject1);
    Expression[] arrayOfExpression2 = new Expression[localOrderedHashSet.size()];
    localOrderedHashSet.toArray(arrayOfExpression2);
    for (int i = 0; i < arrayOfExpression2.length; i++) {
      resolveReferencesAndTypes(localRangeGroupSimple, paramArrayOfRangeGroup, arrayOfExpression2[i]);
    }
    boolean[] arrayOfBoolean = localTable1.getColumnCheckList((int[])localObject1);
    Expression[] arrayOfExpression1 = new Expression[localHsqlArrayList.size()];
    localHsqlArrayList.toArray(arrayOfExpression1);
    Expression localExpression = null;
    if (this.token.tokenType == 333)
    {
      read();
      localExpression = XreadAndResolveBooleanValueExpression(paramArrayOfRangeGroup, localRangeGroupSimple);
    }
    SortAndSlice localSortAndSlice = null;
    if (this.token.tokenType == 625) {
      localSortAndSlice = XreadOrderByExpression();
    }
    resolveUpdateExpressions(localTable1, localRangeGroupSimple, (int[])localObject1, arrayOfExpression1, paramArrayOfRangeGroup);
    Object localObject2;
    if (localTable1 != localTable2)
    {
      localObject2 = ((TableDerived)localTable1).getQueryExpression().getMainSelect();
      if (localExpression != null) {
        localExpression = localExpression.replaceColumnReferences(arrayOfRangeVariable[0], ((QuerySpecification)localObject2).exprColumns);
      }
      for (int k = 0; k < arrayOfExpression1.length; k++) {
        arrayOfExpression1[k] = arrayOfExpression1[k].replaceColumnReferences(arrayOfRangeVariable[0], ((QuerySpecification)localObject2).exprColumns);
      }
      localExpression = ExpressionLogical.andExpressions(((QuerySpecification)localObject2).queryCondition, localExpression);
      arrayOfRangeVariable = ((QuerySpecification)localObject2).rangeVariables;
      ArrayUtil.fillArray(arrayOfRangeVariable[0].usedColumns, true);
    }
    if (localExpression != null)
    {
      arrayOfRangeVariable[0].addJoinCondition(localExpression);
      localObject2 = new RangeVariableResolver(this.session, arrayOfRangeVariable, null, this.compileContext, false);
      ((RangeVariableResolver)localObject2).processConditions();
      arrayOfRangeVariable = ((RangeVariableResolver)localObject2).rangeVariables;
    }
    for (int j = 0; j < arrayOfRangeVariable.length; j++) {
      arrayOfRangeVariable[j].resolveRangeTableTypes(this.session, RangeVariable.emptyArray);
    }
    if (localTable1 != localTable2)
    {
      localObject3 = localTable1.getBaseTableColumnMap();
      int[] arrayOfInt = new int[localObject1.length];
      ArrayUtil.projectRow((int[])localObject3, (int[])localObject1, arrayOfInt);
      localObject1 = arrayOfInt;
      for (int m = 0; m < localObject1.length; m++) {
        if (localTable2.colGenerated[localObject1[m]] != 0) {
          throw Error.error(5513);
        }
      }
    }
    Object localObject3 = new StatementDML(this.session, arrayOfExpression2, localTable1, localRangeVariable, arrayOfRangeVariable, (int[])localObject1, arrayOfExpression1, arrayOfBoolean, this.compileContext, localSortAndSlice);
    return (StatementDMQL)localObject3;
  }
  
  Expression XreadAndResolveBooleanValueExpression(RangeGroup[] paramArrayOfRangeGroup, RangeGroup paramRangeGroup)
  {
    Expression localExpression = XreadBooleanValueExpression();
    HsqlList localHsqlList = localExpression.resolveColumnReferences(this.session, paramRangeGroup, paramArrayOfRangeGroup, null);
    ExpressionColumn.checkColumnsResolved(localHsqlList);
    localExpression.resolveTypes(this.session, null);
    if (localExpression.isUnresolvedParam()) {
      localExpression.dataType = Type.SQL_BOOLEAN;
    }
    if (localExpression.getDataType() != Type.SQL_BOOLEAN) {
      throw Error.error(5568);
    }
    return localExpression;
  }
  
  void resolveUpdateExpressions(Table paramTable, RangeGroup paramRangeGroup, int[] paramArrayOfInt, Expression[] paramArrayOfExpression, RangeGroup[] paramArrayOfRangeGroup)
  {
    HsqlList localHsqlList = null;
    int i = -1;
    if ((paramTable.hasIdentityColumn()) && (paramTable.identitySequence.isAlways())) {
      i = paramTable.getIdentityColumnIndex();
    }
    int j = 0;
    for (int k = 0; j < paramArrayOfInt.length; k++)
    {
      Expression localExpression1 = paramArrayOfExpression[k];
      if (paramTable.colGenerated[paramArrayOfInt[j]] != 0) {
        throw Error.error(5513);
      }
      int n;
      Expression localExpression2;
      if (localExpression1.getType() == 25)
      {
        Expression[] arrayOfExpression = localExpression1.nodes;
        n = 0;
        while (n < arrayOfExpression.length)
        {
          localExpression2 = arrayOfExpression[n];
          if ((i == paramArrayOfInt[j]) && (localExpression2.getType() != 4)) {
            throw Error.error(5541);
          }
          if (localExpression2.isUnresolvedParam())
          {
            localExpression2.setAttributesAsColumn(paramTable.getColumn(paramArrayOfInt[j]), true);
          }
          else if (localExpression2.getType() != 4)
          {
            localHsqlList = localExpression1.resolveColumnReferences(this.session, paramRangeGroup, paramArrayOfRangeGroup, null);
            ExpressionColumn.checkColumnsResolved(localHsqlList);
            localHsqlList = null;
            localExpression2.resolveTypes(this.session, null);
          }
          n++;
          j++;
        }
      }
      else if (localExpression1.getType() == 22)
      {
        localHsqlList = localExpression1.resolveColumnReferences(this.session, paramRangeGroup, paramArrayOfRangeGroup, null);
        ExpressionColumn.checkColumnsResolved(localHsqlList);
        localExpression1.resolveTypes(this.session, null);
        int m = localExpression1.table.queryExpression.getColumnCount();
        n = 0;
        while (n < m)
        {
          if (i == paramArrayOfInt[j]) {
            throw Error.error(5541);
          }
          n++;
          j++;
        }
      }
      else
      {
        localExpression2 = localExpression1;
        if ((i == paramArrayOfInt[j]) && (localExpression2.getType() != 4)) {
          throw Error.error(5541);
        }
        if (localExpression2.isUnresolvedParam())
        {
          localExpression2.setAttributesAsColumn(paramTable.getColumn(paramArrayOfInt[j]), true);
        }
        else if (localExpression2.getType() != 4)
        {
          localHsqlList = localExpression1.resolveColumnReferences(this.session, paramRangeGroup, paramArrayOfRangeGroup, null);
          ExpressionColumn.checkColumnsResolved(localHsqlList);
          localExpression2.resolveTypes(this.session, null);
        }
        j++;
      }
    }
  }
  
  void readSetClauseList(RangeVariable[] paramArrayOfRangeVariable, OrderedHashSet paramOrderedHashSet, LongDeque paramLongDeque, HsqlArrayList paramHsqlArrayList)
  {
    for (;;)
    {
      int i;
      if (this.token.tokenType == 836)
      {
        read();
        int j = paramOrderedHashSet.size();
        readTargetSpecificationList(paramOrderedHashSet, paramArrayOfRangeVariable, paramLongDeque);
        i = paramOrderedHashSet.size() - j;
        readThis(822);
      }
      else
      {
        Expression localExpression1 = XreadTargetSpecification(paramArrayOfRangeVariable, paramLongDeque);
        if (!paramOrderedHashSet.add(localExpression1))
        {
          ColumnSchema localColumnSchema = localExpression1.getColumn();
          throw Error.error(5579, localColumnSchema.getName().name);
        }
        i = 1;
      }
      readThis(417);
      int k = getPosition();
      int m = readOpenBrackets();
      Object localObject;
      if (this.token.tokenType == 265)
      {
        rewind(k);
        TableDerived localTableDerived = XreadSubqueryTableBody(22);
        localObject = localTableDerived.getQueryExpression();
        ((QueryExpression)localObject).setReturningResult();
        if (i != ((QueryExpression)localObject).getColumnCount()) {
          throw Error.error(5546);
        }
        Expression localExpression2 = new Expression(22, localTableDerived);
        paramHsqlArrayList.add(localExpression2);
        if (this.token.tokenType != 824) {
          break;
        }
        read();
      }
      else
      {
        if (m > 0) {
          rewind(k);
        }
        boolean bool = false;
        if (this.database.sqlSyntaxMys) {
          bool = readIfThis(324);
        }
        if ((i > 1) || (bool))
        {
          readThis(836);
          localObject = readRow();
          readThis(822);
          int n = ((Expression)localObject).getType() == 25 ? ((Expression)localObject).nodes.length : 1;
          if (i != n) {
            throw Error.error(5546);
          }
          paramHsqlArrayList.add(localObject);
        }
        else
        {
          localObject = XreadValueExpressionWithContext();
          paramHsqlArrayList.add(localObject);
        }
        if (this.token.tokenType != 824) {
          break;
        }
        read();
      }
    }
  }
  
  void readGetClauseList(RangeVariable[] paramArrayOfRangeVariable, OrderedHashSet paramOrderedHashSet, LongDeque paramLongDeque, HsqlArrayList paramHsqlArrayList)
  {
    for (;;)
    {
      Expression localExpression = XreadTargetSpecification(paramArrayOfRangeVariable, paramLongDeque);
      if (!paramOrderedHashSet.add(localExpression))
      {
        ColumnSchema localColumnSchema = localExpression.getColumn();
        throw Error.error(5579, localColumnSchema.getName().name);
      }
      readThis(417);
      switch (this.token.tokenType)
      {
      case 465: 
      case 517: 
        int i = ExpressionColumn.diagnosticsList.getIndex(this.token.tokenString);
        ExpressionColumn localExpressionColumn = new ExpressionColumn(10, i);
        paramHsqlArrayList.add(localExpressionColumn);
        read();
        break;
      }
      if (this.token.tokenType != 824) {
        break;
      }
      read();
    }
  }
  
  StatementDMQL compileMergeStatement(RangeGroup[] paramArrayOfRangeGroup)
  {
    int[] arrayOfInt1 = null;
    int[] arrayOfInt2 = null;
    Expression[] arrayOfExpression1 = null;
    HsqlArrayList localHsqlArrayList1 = new HsqlArrayList();
    Expression[] arrayOfExpression2 = Expression.emptyArray;
    HsqlArrayList localHsqlArrayList2 = new HsqlArrayList();
    Expression localExpression2 = null;
    read();
    readThis(151);
    RangeVariable localRangeVariable1 = readRangeVariableForDataChange(56);
    Table localTable = localRangeVariable1.rangeTable;
    readThis(322);
    this.compileContext.setOuterRanges(paramArrayOfRangeGroup);
    RangeVariable localRangeVariable2 = readTableOrSubquery();
    RangeVariable[] arrayOfRangeVariable1 = { localRangeVariable1 };
    RangeGroup.RangeGroupSimple localRangeGroupSimple1 = new RangeGroup.RangeGroupSimple(arrayOfRangeVariable1, false);
    localRangeVariable2.resolveRangeTable(this.session, localRangeGroupSimple1, paramArrayOfRangeGroup);
    localRangeVariable2.resolveRangeTableTypes(this.session, arrayOfRangeVariable1);
    this.compileContext.setOuterRanges(RangeGroup.emptyArray);
    RangeVariable[] arrayOfRangeVariable2 = { localRangeVariable2, localRangeVariable1 };
    RangeVariable[] arrayOfRangeVariable3 = { localRangeVariable2 };
    RangeVariable[] arrayOfRangeVariable4 = { localRangeVariable1 };
    RangeGroup.RangeGroupSimple localRangeGroupSimple2 = new RangeGroup.RangeGroupSimple(arrayOfRangeVariable2, false);
    RangeGroup.RangeGroupSimple localRangeGroupSimple3 = new RangeGroup.RangeGroupSimple(arrayOfRangeVariable3, false);
    readThis(204);
    Expression localExpression1 = XreadAndResolveBooleanValueExpression(paramArrayOfRangeGroup, localRangeGroupSimple2);
    arrayOfInt1 = localTable.getColumnMap();
    boolean[] arrayOfBoolean = localTable.getNewColumnCheckList();
    OrderedHashSet localOrderedHashSet1 = new OrderedHashSet();
    OrderedHashSet localOrderedHashSet2 = new OrderedHashSet();
    LongDeque localLongDeque = new LongDeque();
    Expression[] arrayOfExpression3 = new Expression[3];
    boolean bool = false;
    readMergeWhen(paramArrayOfRangeGroup, localRangeGroupSimple2, localLongDeque, localOrderedHashSet2, localOrderedHashSet1, localHsqlArrayList2, localHsqlArrayList1, arrayOfRangeVariable4, localRangeVariable2, arrayOfExpression3);
    if (arrayOfExpression3[2] != null) {
      bool = true;
    }
    if (this.token.tokenType == 331) {
      readMergeWhen(paramArrayOfRangeGroup, localRangeGroupSimple2, localLongDeque, localOrderedHashSet2, localOrderedHashSet1, localHsqlArrayList2, localHsqlArrayList1, arrayOfRangeVariable4, localRangeVariable2, arrayOfExpression3);
    }
    if ((arrayOfExpression3[1] == null) && (arrayOfExpression3[2] != null)) {
      bool = true;
    }
    if (this.token.tokenType == 331) {
      readMergeWhen(paramArrayOfRangeGroup, localRangeGroupSimple2, localLongDeque, localOrderedHashSet2, localOrderedHashSet1, localHsqlArrayList2, localHsqlArrayList1, arrayOfRangeVariable4, localRangeVariable2, arrayOfExpression3);
    }
    if ((arrayOfExpression3[1] == null) && (arrayOfExpression3[2] != null)) {
      bool = true;
    }
    int i;
    if (localHsqlArrayList2.size() > 0)
    {
      i = localOrderedHashSet2.size();
      if (i != 0)
      {
        arrayOfInt1 = localTable.getColumnIndexes(localOrderedHashSet2);
        arrayOfBoolean = localTable.getColumnCheckList(arrayOfInt1);
      }
      localExpression2 = (Expression)localHsqlArrayList2.get(0);
      setParameterTypes(localExpression2, localTable, arrayOfInt1);
      if (arrayOfExpression3[0] == null) {
        arrayOfExpression3[0] = Expression.EXPR_TRUE;
      }
    }
    if (localHsqlArrayList1.size() > 0)
    {
      arrayOfExpression1 = new Expression[localOrderedHashSet1.size()];
      localOrderedHashSet1.toArray(arrayOfExpression1);
      for (i = 0; i < arrayOfExpression1.length; i++) {
        resolveReferencesAndTypes(localRangeGroupSimple1, paramArrayOfRangeGroup, arrayOfExpression1[i]);
      }
      arrayOfExpression2 = new Expression[localHsqlArrayList1.size()];
      localHsqlArrayList1.toArray(arrayOfExpression2);
      arrayOfInt2 = new int[localLongDeque.size()];
      localLongDeque.toArray(arrayOfInt2);
      if (arrayOfExpression3[1] == null) {
        arrayOfExpression3[1] = Expression.EXPR_TRUE;
      }
    }
    if (arrayOfExpression2.length != 0)
    {
      localObject = localTable.isTriggerUpdatable() ? localTable : localTable.getBaseTable();
      int[] arrayOfInt3 = arrayOfInt2;
      if (localTable != localObject)
      {
        arrayOfInt3 = new int[arrayOfInt2.length];
        ArrayUtil.projectRow(localTable.getBaseTableColumnMap(), arrayOfInt2, arrayOfInt3);
      }
      resolveUpdateExpressions(localTable, localRangeGroupSimple2, arrayOfInt2, arrayOfExpression2, paramArrayOfRangeGroup);
    }
    Object localObject = null;
    localObject = localExpression1.resolveColumnReferences(this.session, localRangeGroupSimple2, paramArrayOfRangeGroup, null);
    ExpressionColumn.checkColumnsResolved((HsqlList)localObject);
    localExpression1.resolveTypes(this.session, null);
    if (localExpression1.isUnresolvedParam()) {
      localExpression1.dataType = Type.SQL_BOOLEAN;
    }
    if (localExpression1.getDataType() != Type.SQL_BOOLEAN) {
      throw Error.error(5568);
    }
    arrayOfRangeVariable2[1].addJoinCondition(localExpression1);
    RangeVariableResolver localRangeVariableResolver = new RangeVariableResolver(this.session, arrayOfRangeVariable2, null, this.compileContext, false);
    localRangeVariableResolver.processConditions();
    arrayOfRangeVariable2 = localRangeVariableResolver.rangeVariables;
    for (int j = 0; j < arrayOfRangeVariable2.length; j++) {
      arrayOfRangeVariable2[j].resolveRangeTableTypes(this.session, RangeVariable.emptyArray);
    }
    if (localExpression2 != null)
    {
      localObject = localExpression2.resolveColumnReferences(this.session, localRangeGroupSimple3, RangeGroup.emptyArray, null);
      localObject = Expression.resolveColumnSet(this.session, RangeVariable.emptyArray, paramArrayOfRangeGroup, (HsqlList)localObject);
      ExpressionColumn.checkColumnsResolved((HsqlList)localObject);
      localExpression2.resolveTypes(this.session, null);
    }
    StatementDML localStatementDML = new StatementDML(this.session, arrayOfExpression1, localRangeVariable2, localRangeVariable1, arrayOfRangeVariable2, arrayOfInt1, arrayOfInt2, arrayOfBoolean, localExpression1, localExpression2, arrayOfExpression2, bool, arrayOfExpression3[0], arrayOfExpression3[1], arrayOfExpression3[2], this.compileContext);
    return localStatementDML;
  }
  
  private void readMergeWhen(RangeGroup[] paramArrayOfRangeGroup, RangeGroup paramRangeGroup, LongDeque paramLongDeque, OrderedHashSet paramOrderedHashSet1, OrderedHashSet paramOrderedHashSet2, HsqlArrayList paramHsqlArrayList1, HsqlArrayList paramHsqlArrayList2, RangeVariable[] paramArrayOfRangeVariable, RangeVariable paramRangeVariable, Expression[] paramArrayOfExpression)
  {
    Table localTable = paramArrayOfRangeVariable[0].rangeTable;
    int i = localTable.getColumnCount();
    Expression localExpression1 = null;
    readThis(331);
    if (this.token.tokenType == 459)
    {
      read();
      if (readIfThis(5)) {
        localExpression1 = XreadAndResolveBooleanValueExpression(paramArrayOfRangeGroup, paramRangeGroup);
      }
      readThis(296);
      if (readIfThis(319))
      {
        if (paramHsqlArrayList2.size() != 0) {
          throw Error.error(5547);
        }
        paramArrayOfExpression[1] = localExpression1;
        readThis(268);
        readSetClauseList(paramArrayOfRangeVariable, paramOrderedHashSet2, paramLongDeque, paramHsqlArrayList2);
      }
      else
      {
        if (paramArrayOfExpression[2] != null) {
          throw Error.error(5547);
        }
        if (localExpression1 == null) {
          localExpression1 = Expression.EXPR_TRUE;
        }
        paramArrayOfExpression[2] = localExpression1;
        readThis(84);
      }
    }
    else if (this.token.tokenType == 193)
    {
      if (paramHsqlArrayList1.size() != 0) {
        throw Error.error(5548);
      }
      read();
      readThis(459);
      if (readIfThis(5)) {
        localExpression1 = XreadAndResolveBooleanValueExpression(paramArrayOfRangeGroup, paramRangeGroup);
      }
      paramArrayOfExpression[0] = localExpression1;
      readThis(296);
      readThis(145);
      int j = readOpenBrackets();
      if (j == 1)
      {
        boolean bool = this.database.sqlSyntaxOra;
        readSimpleColumnNames(paramOrderedHashSet1, paramArrayOfRangeVariable[0], bool);
        i = paramOrderedHashSet1.size();
        readThis(822);
        j = 0;
      }
      readThis(324);
      Expression localExpression2 = XreadContextuallyTypedTable(i);
      if (localExpression2.nodes.length != 1) {
        throw Error.error(3201);
      }
      paramHsqlArrayList1.add(localExpression2);
    }
    else
    {
      throw unexpectedToken();
    }
  }
  
  StatementDMQL compileCallStatement(RangeGroup[] paramArrayOfRangeGroup, boolean paramBoolean)
  {
    read();
    if (isIdentifier())
    {
      localObject1 = (RoutineSchema)this.database.schemaManager.findSchemaObject(this.session, this.token.tokenString, this.token.namePrefix, this.token.namePrePrefix, 17);
      if ((localObject1 == null) && (this.token.namePrefix == null))
      {
        localObject2 = this.session.getSchemaName(null);
        localObject3 = this.database.schemaManager.findSynonym(this.token.tokenString, (String)localObject2, 18);
        if (localObject3 != null)
        {
          HsqlNameManager.HsqlName localHsqlName = ((ReferenceObject)localObject3).getTarget();
          localObject1 = (RoutineSchema)this.database.schemaManager.findSchemaObject(localHsqlName.name, localHsqlName.schema.name, localHsqlName.type);
        }
      }
      if (localObject1 != null)
      {
        read();
        return compileProcedureCall(paramArrayOfRangeGroup, (RoutineSchema)localObject1);
      }
    }
    if (paramBoolean) {
      throw Error.error(5501, this.token.tokenString);
    }
    Object localObject1 = XreadValueExpression();
    Object localObject2 = ((Expression)localObject1).resolveColumnReferences(this.session, RangeGroup.emptyGroup, paramArrayOfRangeGroup, null);
    ExpressionColumn.checkColumnsResolved((HsqlList)localObject2);
    ((Expression)localObject1).resolveTypes(this.session, null);
    Object localObject3 = new StatementProcedure(this.session, (Expression)localObject1, this.compileContext);
    return (StatementDMQL)localObject3;
  }
  
  StatementDMQL compileProcedureCall(RangeGroup[] paramArrayOfRangeGroup, RoutineSchema paramRoutineSchema)
  {
    HsqlArrayList localHsqlArrayList = new HsqlArrayList();
    boolean bool = true;
    if (this.database.sqlSyntaxOra) {
      bool = readIfThis(836);
    } else {
      readThis(836);
    }
    if (bool) {
      if (this.token.tokenType == 822) {
        read();
      } else {
        for (;;)
        {
          localObject1 = XreadValueExpression();
          localHsqlArrayList.add(localObject1);
          if (this.token.tokenType == 824)
          {
            read();
          }
          else
          {
            readThis(822);
            break;
          }
        }
      }
    }
    Object localObject1 = new Expression[localHsqlArrayList.size()];
    localHsqlArrayList.toArray(localObject1);
    Routine localRoutine = paramRoutineSchema.getSpecificRoutine(localObject1.length);
    this.compileContext.addProcedureCall(localRoutine);
    HsqlList localHsqlList = null;
    for (int i = 0; i < localObject1.length; i++)
    {
      Object localObject2 = localObject1[i];
      if (((Expression)localObject2).isUnresolvedParam())
      {
        ((Expression)localObject2).setAttributesAsColumn(localRoutine.getParameter(i), localRoutine.getParameter(i).isWriteable());
      }
      else
      {
        int j = localRoutine.getParameter(i).getParameterMode();
        localHsqlList = localObject1[i].resolveColumnReferences(this.session, RangeGroup.emptyGroup, paramArrayOfRangeGroup, localHsqlList);
        if ((j != 1) && (((Expression)localObject2).getType() != 6)) {
          throw Error.error(5603);
        }
      }
    }
    ExpressionColumn.checkColumnsResolved(localHsqlList);
    for (i = 0; i < localObject1.length; i++)
    {
      localObject1[i].resolveTypes(this.session, null);
      if (!localRoutine.getParameter(i).getDataType().canBeAssignedFrom(localObject1[i].getDataType())) {
        throw Error.error(5561);
      }
    }
    StatementProcedure localStatementProcedure = new StatementProcedure(this.session, localRoutine, (Expression[])localObject1, this.compileContext);
    return localStatementProcedure;
  }
  
  void resolveReferencesAndTypes(RangeGroup paramRangeGroup, RangeGroup[] paramArrayOfRangeGroup, Expression paramExpression)
  {
    HsqlList localHsqlList = paramExpression.resolveColumnReferences(this.session, paramRangeGroup, paramRangeGroup.getRangeVariables().length, paramArrayOfRangeGroup, null, false);
    ExpressionColumn.checkColumnsResolved(localHsqlList);
    paramExpression.resolveTypes(this.session, null);
  }
  
  void resolveOuterReferencesAndTypes(RangeGroup[] paramArrayOfRangeGroup, Expression paramExpression)
  {
    HsqlList localHsqlList = paramExpression.resolveColumnReferences(this.session, RangeGroup.emptyGroup, paramArrayOfRangeGroup, null);
    ExpressionColumn.checkColumnsResolved(localHsqlList);
    paramExpression.resolveTypes(this.session, null);
  }
}


/* Location:              E:\java\java学习\hutubill\lib\all.jar!\org\hsqldb\ParserDML.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */
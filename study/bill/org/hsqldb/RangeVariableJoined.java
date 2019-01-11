package org.hsqldb;

import org.hsqldb.index.Index;
import org.hsqldb.lib.HashMap;
import org.hsqldb.lib.HashSet;
import org.hsqldb.lib.HsqlArrayList;
import org.hsqldb.lib.OrderedHashSet;
import org.hsqldb.map.ValuePool;

public class RangeVariableJoined
  extends RangeVariable
{
  RangeVariable[] rangeArray;
  
  public RangeVariableJoined(Table paramTable, HsqlNameManager.SimpleName paramSimpleName, OrderedHashSet paramOrderedHashSet, HsqlNameManager.SimpleName[] paramArrayOfSimpleName, ParserDQL.CompileContext paramCompileContext)
  {
    super(paramTable, paramSimpleName, paramOrderedHashSet, paramArrayOfSimpleName, paramCompileContext);
    setParameters();
  }
  
  private void setParameters()
  {
    this.namedJoinColumnExpressions = new HashMap();
    QuerySpecification localQuerySpecification = (QuerySpecification)this.rangeTable.getQueryExpression();
    this.rangeArray = localQuerySpecification.rangeVariables;
    for (int i = 0; i < this.rangeArray.length; i++)
    {
      if (this.rangeArray[i].isLeftJoin) {
        this.hasLeftJoin = true;
      }
      if (this.rangeArray[i].isRightJoin) {
        this.hasRightJoin = true;
      }
      if (this.rangeArray[i].isLateral) {
        this.hasLateral = true;
      }
      if (this.rangeArray[i].namedJoinColumnExpressions != null) {
        this.namedJoinColumnExpressions.putAll(this.rangeArray[i].namedJoinColumnExpressions);
      }
    }
  }
  
  public RangeVariable[] getBaseRangeVariables()
  {
    return this.rangeArray;
  }
  
  public void setRangeTableVariables()
  {
    super.setRangeTableVariables();
  }
  
  public void setJoinType(boolean paramBoolean1, boolean paramBoolean2)
  {
    super.setJoinType(paramBoolean1, paramBoolean2);
  }
  
  public void addNamedJoinColumns(OrderedHashSet paramOrderedHashSet)
  {
    super.addNamedJoinColumns(paramOrderedHashSet);
  }
  
  public void addColumn(int paramInt)
  {
    super.addColumn(paramInt);
  }
  
  public void addAllColumns()
  {
    super.addAllColumns();
  }
  
  public void addNamedJoinColumnExpression(String paramString, Expression paramExpression, int paramInt)
  {
    super.addNamedJoinColumnExpression(paramString, paramExpression, paramInt);
  }
  
  public ExpressionColumn getColumnExpression(String paramString)
  {
    ExpressionColumn localExpressionColumn = super.getColumnExpression(paramString);
    if (localExpressionColumn == null) {
      localExpressionColumn = this.rangeArray[0].getColumnExpression(paramString);
    }
    return localExpressionColumn;
  }
  
  public Table getTable()
  {
    return super.getTable();
  }
  
  public boolean hasSingleIndexCondition()
  {
    return super.hasSingleIndexCondition();
  }
  
  public boolean setDistinctColumnsOnIndex(int[] paramArrayOfInt)
  {
    return super.setDistinctColumnsOnIndex(paramArrayOfInt);
  }
  
  public Index getSortIndex()
  {
    return super.getSortIndex();
  }
  
  public boolean setSortIndex(Index paramIndex, boolean paramBoolean)
  {
    return super.setSortIndex(paramIndex, paramBoolean);
  }
  
  public boolean reverseOrder()
  {
    return super.reverseOrder();
  }
  
  public OrderedHashSet getColumnNames()
  {
    return super.getColumnNames();
  }
  
  public OrderedHashSet getUniqueColumnNameSet()
  {
    return super.getUniqueColumnNameSet();
  }
  
  public int findColumn(String paramString1, String paramString2, String paramString3)
  {
    if (this.tableAlias != null) {
      return super.findColumn(paramString1, paramString2, paramString3);
    }
    int i = 0;
    int j = -1;
    if ((paramString2 == null) && (this.namedJoinColumnExpressions.containsKey(paramString3))) {
      return super.findColumn(null, null, paramString3);
    }
    for (int k = 0; k < this.rangeArray.length; k++)
    {
      RangeVariable localRangeVariable = this.rangeArray[k];
      j = localRangeVariable.findColumn(paramString1, paramString2, paramString3);
      if (localRangeVariable.namedJoinColumnCheck == null)
      {
        if (j > -1) {
          i += j;
        } else {
          i += localRangeVariable.rangeTable.getColumnCount();
        }
      }
      else
      {
        int m = j > -1 ? j : localRangeVariable.namedJoinColumnCheck.length;
        for (int n = 0; n < m; n++) {
          if (localRangeVariable.namedJoinColumnCheck[n] == 0) {
            i++;
          }
        }
      }
      if (j > -1) {
        break;
      }
    }
    if (j > -1)
    {
      if (this.namedJoinColumnExpressions != null) {
        i += this.namedJoinColumnExpressions.size();
      }
      return i;
    }
    return -1;
  }
  
  public HsqlNameManager.SimpleName getColumnAlias(int paramInt)
  {
    return super.getColumnAlias(paramInt);
  }
  
  public boolean hasColumnAlias()
  {
    return super.hasColumnAlias();
  }
  
  public HsqlNameManager.SimpleName getTableAlias()
  {
    return super.getTableAlias();
  }
  
  public RangeVariable getRangeForTableName(String paramString)
  {
    if (this.tableAlias != null) {
      return super.getRangeForTableName(paramString);
    }
    for (int i = 0; i < this.rangeArray.length; i++)
    {
      RangeVariable localRangeVariable = this.rangeArray[i].getRangeForTableName(paramString);
      if (localRangeVariable != null) {
        return localRangeVariable;
      }
    }
    return null;
  }
  
  public void addTableColumns(HsqlArrayList paramHsqlArrayList)
  {
    super.addTableColumns(paramHsqlArrayList);
  }
  
  public int addTableColumns(HsqlArrayList paramHsqlArrayList, int paramInt, HashSet paramHashSet)
  {
    return super.addTableColumns(paramHsqlArrayList, paramInt, paramHashSet);
  }
  
  public void addTableColumns(RangeVariable paramRangeVariable, Expression paramExpression, HashSet paramHashSet)
  {
    int i = getFirstColumnIndex(paramRangeVariable);
    addTableColumns(paramExpression, i, paramRangeVariable.rangeTable.getColumnCount(), paramHashSet);
  }
  
  protected int getFirstColumnIndex(RangeVariable paramRangeVariable)
  {
    if (paramRangeVariable == this) {
      return 0;
    }
    int i = 0;
    for (int j = 0; j < this.rangeArray.length; j++)
    {
      int k = this.rangeArray[j].getFirstColumnIndex(paramRangeVariable);
      if (k == -1) {
        i += this.rangeArray[j].rangeTable.getColumnCount();
      } else {
        return i + k;
      }
    }
    return -1;
  }
  
  public void setForCheckConstraint()
  {
    super.setForCheckConstraint();
  }
  
  public Expression getJoinCondition()
  {
    return super.getJoinCondition();
  }
  
  public void addJoinCondition(Expression paramExpression)
  {
    super.addJoinCondition(paramExpression);
  }
  
  public void resetConditions()
  {
    super.resetConditions();
  }
  
  public void replaceColumnReference(RangeVariable paramRangeVariable, Expression[] paramArrayOfExpression) {}
  
  public void replaceRangeVariables(RangeVariable[] paramArrayOfRangeVariable1, RangeVariable[] paramArrayOfRangeVariable2)
  {
    super.replaceRangeVariables(paramArrayOfRangeVariable1, paramArrayOfRangeVariable2);
  }
  
  public void resolveRangeTable(Session paramSession, RangeGroup paramRangeGroup, RangeGroup[] paramArrayOfRangeGroup)
  {
    super.resolveRangeTable(paramSession, paramRangeGroup, paramArrayOfRangeGroup);
  }
  
  public String describe(Session paramSession, int paramInt)
  {
    RangeVariable.RangeVariableConditions[] arrayOfRangeVariableConditions = this.joinConditions;
    String str1 = ValuePool.spaceString.substring(0, paramInt);
    StringBuffer localStringBuffer = new StringBuffer();
    String str2 = "INNER";
    if (this.isLeftJoin)
    {
      str2 = "LEFT OUTER";
      if (this.isRightJoin) {
        str2 = "FULL";
      }
    }
    else if (this.isRightJoin)
    {
      str2 = "RIGHT OUTER";
    }
    localStringBuffer.append(str1).append("join type=").append(str2).append("\n");
    localStringBuffer.append(str1).append("table=").append(this.rangeTable.getName().name).append("\n");
    if (this.tableAlias != null) {
      localStringBuffer.append(str1).append("alias=").append(this.tableAlias.name).append("\n");
    }
    int i = !arrayOfRangeVariableConditions[0].hasIndexCondition() ? 1 : 0;
    localStringBuffer.append(str1).append("access=").append(i != 0 ? "FULL SCAN" : "INDEX PRED").append("\n");
    for (int j = 0; j < arrayOfRangeVariableConditions.length; j++)
    {
      RangeVariable.RangeVariableConditions localRangeVariableConditions = this.joinConditions[j];
      if (j > 0) {
        localStringBuffer.append(str1).append("OR condition = [");
      } else {
        localStringBuffer.append(str1).append("condition = [");
      }
      localStringBuffer.append(localRangeVariableConditions.describe(paramSession, paramInt + 2));
      localStringBuffer.append(str1).append("]\n");
    }
    return localStringBuffer.toString();
  }
  
  public RangeVariable.RangeIteratorMain getIterator(Session paramSession)
  {
    return super.getIterator(paramSession);
  }
}


/* Location:              E:\java\java学习\hutubill\lib\all.jar!\org\hsqldb\RangeVariableJoined.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */
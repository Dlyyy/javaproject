package org.hsqldb;

public abstract interface RangeGroup
{
  public static final RangeGroup emptyGroup = new RangeGroupEmpty();
  public static final RangeGroup[] emptyArray = { emptyGroup };
  
  public abstract RangeVariable[] getRangeVariables();
  
  public abstract void setCorrelated();
  
  public abstract boolean isVariable();
  
  public static class RangeGroupEmpty
    implements RangeGroup
  {
    public RangeVariable[] getRangeVariables()
    {
      return RangeVariable.emptyArray;
    }
    
    public void setCorrelated() {}
    
    public boolean isVariable()
    {
      return false;
    }
  }
  
  public static class RangeGroupSimple
    implements RangeGroup
  {
    RangeVariable[] ranges;
    RangeGroup baseGroup;
    TableDerived table;
    boolean isVariable;
    
    public RangeGroupSimple(TableDerived paramTableDerived)
    {
      this.ranges = RangeVariable.emptyArray;
      this.table = paramTableDerived;
    }
    
    public RangeGroupSimple(RangeVariable[] paramArrayOfRangeVariable, RangeGroup paramRangeGroup)
    {
      this.ranges = paramArrayOfRangeVariable;
      this.baseGroup = paramRangeGroup;
    }
    
    public RangeGroupSimple(RangeVariable[] paramArrayOfRangeVariable, boolean paramBoolean)
    {
      this.ranges = paramArrayOfRangeVariable;
      this.isVariable = paramBoolean;
    }
    
    public RangeVariable[] getRangeVariables()
    {
      return this.ranges;
    }
    
    public void setCorrelated()
    {
      if (this.baseGroup != null) {
        this.baseGroup.setCorrelated();
      }
      if (this.table != null) {
        this.table.setCorrelated();
      }
    }
    
    public boolean isVariable()
    {
      return this.isVariable;
    }
  }
}


/* Location:              E:\java\java学习\hutubill\lib\all.jar!\org\hsqldb\RangeGroup.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */
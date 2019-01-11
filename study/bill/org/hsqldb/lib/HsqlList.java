package org.hsqldb.lib;

public abstract interface HsqlList
  extends Collection
{
  public abstract void add(int paramInt, Object paramObject);
  
  public abstract boolean add(Object paramObject);
  
  public abstract Object get(int paramInt);
  
  public abstract Object remove(int paramInt);
  
  public abstract Object set(int paramInt, Object paramObject);
  
  public abstract boolean isEmpty();
  
  public abstract int size();
  
  public abstract Iterator iterator();
}


/* Location:              E:\java\java学习\hutubill\lib\all.jar!\org\hsqldb\lib\HsqlList.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */
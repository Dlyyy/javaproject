package org.hsqldb.lib;

public abstract interface HsqlHeap
{
  public abstract void clear();
  
  public abstract boolean isEmpty();
  
  public abstract boolean isFull();
  
  public abstract void add(Object paramObject)
    throws IllegalArgumentException, RuntimeException;
  
  public abstract Object peek();
  
  public abstract Object remove();
  
  public abstract int size();
}


/* Location:              E:\java\java学习\hutubill\lib\all.jar!\org\hsqldb\lib\HsqlHeap.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */
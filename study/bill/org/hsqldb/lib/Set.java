package org.hsqldb.lib;

public abstract interface Set
  extends Collection
{
  public abstract int size();
  
  public abstract boolean isEmpty();
  
  public abstract boolean contains(Object paramObject);
  
  public abstract Iterator iterator();
  
  public abstract boolean add(Object paramObject);
  
  public abstract Object get(Object paramObject);
  
  public abstract boolean remove(Object paramObject);
  
  public abstract void clear();
  
  public abstract boolean equals(Object paramObject);
  
  public abstract int hashCode();
}


/* Location:              E:\java\java学习\hutubill\lib\all.jar!\org\hsqldb\lib\Set.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */
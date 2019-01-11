package org.hsqldb.lib;

public abstract interface Collection
{
  public abstract int size();
  
  public abstract boolean isEmpty();
  
  public abstract boolean contains(Object paramObject);
  
  public abstract Iterator iterator();
  
  public abstract boolean add(Object paramObject);
  
  public abstract boolean remove(Object paramObject);
  
  public abstract boolean addAll(Collection paramCollection);
  
  public abstract void clear();
}


/* Location:              E:\java\java学习\hutubill\lib\all.jar!\org\hsqldb\lib\Collection.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */
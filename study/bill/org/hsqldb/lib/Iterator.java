package org.hsqldb.lib;

import java.util.NoSuchElementException;

public abstract interface Iterator
{
  public abstract boolean hasNext();
  
  public abstract Object next()
    throws NoSuchElementException;
  
  public abstract int nextInt()
    throws NoSuchElementException;
  
  public abstract long nextLong()
    throws NoSuchElementException;
  
  public abstract void remove()
    throws NoSuchElementException;
  
  public abstract void setValue(Object paramObject);
}


/* Location:              E:\java\java学习\hutubill\lib\all.jar!\org\hsqldb\lib\Iterator.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */
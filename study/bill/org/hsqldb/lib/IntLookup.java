package org.hsqldb.lib;

import java.util.NoSuchElementException;

public abstract interface IntLookup
{
  public abstract int add(int paramInt1, int paramInt2);
  
  public abstract boolean addUnsorted(int paramInt1, int paramInt2);
  
  public abstract int lookup(int paramInt)
    throws NoSuchElementException;
  
  public abstract int lookup(int paramInt1, int paramInt2);
  
  public abstract int size();
  
  public abstract void clear();
}


/* Location:              E:\java\java学习\hutubill\lib\all.jar!\org\hsqldb\lib\IntLookup.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */
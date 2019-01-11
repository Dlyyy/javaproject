package org.hsqldb.lib;

import java.util.NoSuchElementException;

public abstract interface LongLookup
{
  public abstract int add(long paramLong1, long paramLong2);
  
  public abstract boolean addUnsorted(long paramLong1, long paramLong2);
  
  public abstract long lookup(long paramLong)
    throws NoSuchElementException;
  
  public abstract long lookup(long paramLong1, long paramLong2);
  
  public abstract int size();
  
  public abstract void clear();
}


/* Location:              E:\java\java学习\hutubill\lib\all.jar!\org\hsqldb\lib\LongLookup.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */
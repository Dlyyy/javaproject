package org.hsqldb.lib;

import java.util.Comparator;

public abstract interface ObjectComparator
  extends Comparator
{
  public abstract int hashCode(Object paramObject);
  
  public abstract long longKey(Object paramObject);
}


/* Location:              E:\java\java学习\hutubill\lib\all.jar!\org\hsqldb\lib\ObjectComparator.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */
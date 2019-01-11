package org.hsqldb.navigator;

import org.hsqldb.Row;

public abstract interface RangeIterator
  extends RowIterator
{
  public abstract boolean isBeforeFirst();
  
  public abstract boolean next();
  
  public abstract Row getCurrentRow();
  
  public abstract Object[] getCurrent();
  
  public abstract Object getCurrent(int paramInt);
  
  public abstract void setCurrent(Object[] paramArrayOfObject);
  
  public abstract Object getRowidObject();
  
  public abstract void reset();
  
  public abstract int getRangePosition();
}


/* Location:              E:\java\java学习\hutubill\lib\all.jar!\org\hsqldb\navigator\RangeIterator.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */
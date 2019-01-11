package org.hsqldb.navigator;

import org.hsqldb.Row;
import org.hsqldb.TableBase;

public abstract interface RowIterator
{
  public abstract Row getNextRow();
  
  public abstract Object[] getNext();
  
  public abstract boolean hasNext();
  
  public abstract void removeCurrent();
  
  public abstract void release();
  
  public abstract long getRowId();
  
  public abstract TableBase getCurrentTable();
}


/* Location:              E:\java\java学习\hutubill\lib\all.jar!\org\hsqldb\navigator\RowIterator.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */
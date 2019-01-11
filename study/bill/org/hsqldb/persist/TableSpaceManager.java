package org.hsqldb.persist;

import org.hsqldb.lib.DoubleIntIndex;

public abstract interface TableSpaceManager
{
  public abstract int getSpaceID();
  
  public abstract void release(long paramLong, int paramInt);
  
  public abstract long getFilePosition(int paramInt, boolean paramBoolean);
  
  public abstract boolean hasFileRoom(long paramLong);
  
  public abstract void addFileBlock(long paramLong1, long paramLong2);
  
  public abstract void initialiseFileBlock(DoubleIntIndex paramDoubleIntIndex, long paramLong1, long paramLong2);
  
  public abstract void reset();
  
  public abstract long getLostBlocksSize();
  
  public abstract boolean isDefaultSpace();
}


/* Location:              E:\java\java学习\hutubill\lib\all.jar!\org\hsqldb\persist\TableSpaceManager.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */
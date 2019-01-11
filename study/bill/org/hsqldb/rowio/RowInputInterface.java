package org.hsqldb.rowio;

import org.hsqldb.types.Type;

public abstract interface RowInputInterface
{
  public abstract long getPos();
  
  public abstract int getSize();
  
  public abstract int readType();
  
  public abstract String readString();
  
  public abstract byte readByte();
  
  public abstract char readChar();
  
  public abstract short readShort();
  
  public abstract int readInt();
  
  public abstract long readLong();
  
  public abstract Object readData(Type paramType);
  
  public abstract Object[] readData(Type[] paramArrayOfType);
  
  public abstract void resetRow(long paramLong, int paramInt);
  
  public abstract void resetBlock(long paramLong, int paramInt);
  
  public abstract byte[] getBuffer();
}


/* Location:              E:\java\java学习\hutubill\lib\all.jar!\org\hsqldb\rowio\RowInputInterface.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */
package org.hsqldb.persist;

public abstract interface LobStore
{
  public abstract byte[] getBlockBytes(int paramInt1, int paramInt2);
  
  public abstract void setBlockBytes(byte[] paramArrayOfByte, int paramInt1, int paramInt2);
  
  public abstract void setBlockBytes(byte[] paramArrayOfByte, long paramLong, int paramInt1, int paramInt2);
  
  public abstract int getBlockSize();
  
  public abstract void setLength(long paramLong);
  
  public abstract long getLength();
  
  public abstract void close();
  
  public abstract void synch();
}


/* Location:              E:\java\java学习\hutubill\lib\all.jar!\org\hsqldb\persist\LobStore.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */
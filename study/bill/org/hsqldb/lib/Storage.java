package org.hsqldb.lib;

import java.io.IOException;

public abstract interface Storage
{
  public abstract long length()
    throws IOException;
  
  public abstract void seek(long paramLong)
    throws IOException;
  
  public abstract long getFilePointer()
    throws IOException;
  
  public abstract int read()
    throws IOException;
  
  public abstract void read(byte[] paramArrayOfByte, int paramInt1, int paramInt2)
    throws IOException;
  
  public abstract void write(byte[] paramArrayOfByte, int paramInt1, int paramInt2)
    throws IOException;
  
  public abstract int readInt()
    throws IOException;
  
  public abstract void writeInt(int paramInt)
    throws IOException;
  
  public abstract long readLong()
    throws IOException;
  
  public abstract void writeLong(long paramLong)
    throws IOException;
  
  public abstract void close()
    throws IOException;
  
  public abstract boolean isReadOnly();
  
  public abstract boolean wasNio();
}


/* Location:              E:\java\java学习\hutubill\lib\all.jar!\org\hsqldb\lib\Storage.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */
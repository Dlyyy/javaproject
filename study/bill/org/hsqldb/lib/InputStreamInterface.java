package org.hsqldb.lib;

import java.io.IOException;

public abstract interface InputStreamInterface
{
  public abstract int read()
    throws IOException;
  
  public abstract int read(byte[] paramArrayOfByte)
    throws IOException;
  
  public abstract int read(byte[] paramArrayOfByte, int paramInt1, int paramInt2)
    throws IOException;
  
  public abstract long skip(long paramLong)
    throws IOException;
  
  public abstract int available()
    throws IOException;
  
  public abstract void close()
    throws IOException;
  
  public abstract void setSizeLimit(long paramLong);
  
  public abstract long getSizeLimit();
}


/* Location:              E:\java\java学习\hutubill\lib\all.jar!\org\hsqldb\lib\InputStreamInterface.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */
package org.hsqldb.persist;

import java.io.IOException;
import org.hsqldb.lib.Storage;

final class RAStorageWrapper
  implements RandomAccessInterface
{
  final Storage file;
  
  RAStorageWrapper(Storage paramStorage)
    throws IOException
  {
    this.file = paramStorage;
  }
  
  public long length()
    throws IOException
  {
    return this.file.length();
  }
  
  public void seek(long paramLong)
    throws IOException
  {
    this.file.seek(paramLong);
  }
  
  public long getFilePointer()
    throws IOException
  {
    return this.file.getFilePointer();
  }
  
  public int read()
    throws IOException
  {
    return this.file.read();
  }
  
  public long readLong()
    throws IOException
  {
    return this.file.readLong();
  }
  
  public int readInt()
    throws IOException
  {
    return this.file.readInt();
  }
  
  public void read(byte[] paramArrayOfByte, int paramInt1, int paramInt2)
    throws IOException
  {
    this.file.read(paramArrayOfByte, paramInt1, paramInt2);
  }
  
  public void write(byte[] paramArrayOfByte, int paramInt1, int paramInt2)
    throws IOException
  {
    this.file.write(paramArrayOfByte, paramInt1, paramInt2);
  }
  
  public void writeInt(int paramInt)
    throws IOException
  {
    this.file.writeInt(paramInt);
  }
  
  public void writeLong(long paramLong)
    throws IOException
  {
    this.file.writeLong(paramLong);
  }
  
  public void close()
    throws IOException
  {
    this.file.close();
  }
  
  public boolean isReadOnly()
  {
    return this.file.isReadOnly();
  }
  
  public boolean ensureLength(long paramLong)
  {
    return true;
  }
  
  public boolean setLength(long paramLong)
  {
    return false;
  }
  
  public void synch() {}
}


/* Location:              E:\java\java学习\hutubill\lib\all.jar!\org\hsqldb\persist\RAStorageWrapper.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */
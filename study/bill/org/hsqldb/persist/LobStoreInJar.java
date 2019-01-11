package org.hsqldb.persist;

import java.io.DataInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import org.hsqldb.Database;
import org.hsqldb.error.Error;

public class LobStoreInJar
  implements LobStore
{
  final int lobBlockSize;
  Database database;
  DataInputStream dataInput;
  final String fileName;
  long realPosition;
  
  public LobStoreInJar(Database paramDatabase, int paramInt)
  {
    this.lobBlockSize = paramInt;
    this.database = paramDatabase;
    try
    {
      this.fileName = (paramDatabase.getPath() + ".lobs");
    }
    catch (Throwable localThrowable)
    {
      throw Error.error(466, localThrowable);
    }
  }
  
  public byte[] getBlockBytes(int paramInt1, int paramInt2)
  {
    try
    {
      long l = paramInt1 * this.lobBlockSize;
      int i = paramInt2 * this.lobBlockSize;
      byte[] arrayOfByte = new byte[i];
      fileSeek(l);
      this.dataInput.readFully(arrayOfByte, 0, i);
      this.realPosition = (l + i);
      return arrayOfByte;
    }
    catch (Throwable localThrowable)
    {
      throw Error.error(466, localThrowable);
    }
  }
  
  public void setBlockBytes(byte[] paramArrayOfByte, int paramInt1, int paramInt2) {}
  
  public void setBlockBytes(byte[] paramArrayOfByte, long paramLong, int paramInt1, int paramInt2) {}
  
  public int getBlockSize()
  {
    return this.lobBlockSize;
  }
  
  public long getLength()
  {
    return 0L;
  }
  
  public void setLength(long paramLong) {}
  
  public void close()
  {
    try
    {
      if (this.dataInput != null) {
        this.dataInput.close();
      }
    }
    catch (Throwable localThrowable)
    {
      throw Error.error(466, localThrowable);
    }
  }
  
  public void synch() {}
  
  private void resetStream()
    throws IOException
  {
    if (this.dataInput != null) {
      this.dataInput.close();
    }
    InputStream localInputStream = null;
    try
    {
      localInputStream = getClass().getResourceAsStream(this.fileName);
      if (localInputStream == null)
      {
        ClassLoader localClassLoader = Thread.currentThread().getContextClassLoader();
        if (localClassLoader != null) {
          localInputStream = localClassLoader.getResourceAsStream(this.fileName);
        }
      }
    }
    catch (Throwable localThrowable) {}finally
    {
      if (localInputStream == null) {
        throw new FileNotFoundException(this.fileName);
      }
    }
    this.dataInput = new DataInputStream(localInputStream);
    this.realPosition = 0L;
  }
  
  private void fileSeek(long paramLong)
    throws IOException
  {
    if (this.dataInput == null) {
      resetStream();
    }
    long l = this.realPosition;
    if (paramLong < l) {
      resetStream();
    }
    for (l = 0L; paramLong > l; l += this.dataInput.skip(paramLong - l)) {}
    this.realPosition = paramLong;
  }
}


/* Location:              E:\java\java学习\hutubill\lib\all.jar!\org\hsqldb\persist\LobStoreInJar.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */
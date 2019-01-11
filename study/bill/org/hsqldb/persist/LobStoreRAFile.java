package org.hsqldb.persist;

import org.hsqldb.Database;
import org.hsqldb.error.Error;
import org.hsqldb.lib.FileAccess;

public class LobStoreRAFile
  implements LobStore
{
  final int lobBlockSize;
  String fileName;
  RandomAccessInterface file;
  Database database;
  
  public LobStoreRAFile(Database paramDatabase, int paramInt)
  {
    this.database = paramDatabase;
    this.lobBlockSize = paramInt;
    this.fileName = (paramDatabase.getPath() + ".lobs");
    try
    {
      boolean bool = paramDatabase.logger.getFileAccess().isStreamElement(this.fileName);
      if (bool) {
        openFile();
      }
    }
    catch (Throwable localThrowable)
    {
      throw Error.error(466, localThrowable);
    }
  }
  
  private void openFile()
  {
    try
    {
      boolean bool = this.database.isFilesReadOnly();
      if (this.database.logger.isStoredFileAccess()) {
        this.file = RAFile.newScaledRAFile(this.database, this.fileName, bool, 3);
      } else {
        this.file = new RAFileSimple(this.database.logger, this.fileName, bool ? "r" : "rws");
      }
    }
    catch (Throwable localThrowable)
    {
      throw Error.error(466, localThrowable);
    }
  }
  
  public byte[] getBlockBytes(int paramInt1, int paramInt2)
  {
    if (this.file == null) {
      throw Error.error(452);
    }
    try
    {
      long l = paramInt1 * this.lobBlockSize;
      int i = paramInt2 * this.lobBlockSize;
      byte[] arrayOfByte = new byte[i];
      this.file.seek(l);
      this.file.read(arrayOfByte, 0, i);
      return arrayOfByte;
    }
    catch (Throwable localThrowable)
    {
      throw Error.error(466, localThrowable);
    }
  }
  
  public void setBlockBytes(byte[] paramArrayOfByte, int paramInt1, int paramInt2)
  {
    if (this.file == null) {
      openFile();
    }
    try
    {
      long l = paramInt1 * this.lobBlockSize;
      int i = paramInt2 * this.lobBlockSize;
      this.file.seek(l);
      this.file.write(paramArrayOfByte, 0, i);
    }
    catch (Throwable localThrowable)
    {
      throw Error.error(466, localThrowable);
    }
  }
  
  public void setBlockBytes(byte[] paramArrayOfByte, long paramLong, int paramInt1, int paramInt2)
  {
    if (paramInt2 == 0) {
      return;
    }
    if (this.file == null) {
      openFile();
    }
    try
    {
      this.file.seek(paramLong);
      this.file.write(paramArrayOfByte, paramInt1, paramInt2);
    }
    catch (Throwable localThrowable)
    {
      throw Error.error(466, localThrowable);
    }
  }
  
  public int getBlockSize()
  {
    return this.lobBlockSize;
  }
  
  public long getLength()
  {
    if (this.file == null) {
      openFile();
    }
    try
    {
      return this.file.length();
    }
    catch (Throwable localThrowable)
    {
      throw Error.error(466, localThrowable);
    }
  }
  
  public void setLength(long paramLong)
  {
    try
    {
      if (this.file != null)
      {
        this.file.setLength(paramLong);
        this.file.synch();
      }
    }
    catch (Throwable localThrowable)
    {
      throw Error.error(466, localThrowable);
    }
  }
  
  public void close()
  {
    try
    {
      if (this.file != null)
      {
        this.file.synch();
        this.file.close();
      }
    }
    catch (Throwable localThrowable)
    {
      throw Error.error(466, localThrowable);
    }
  }
  
  public void synch()
  {
    if (this.file != null) {
      this.file.synch();
    }
  }
}


/* Location:              E:\java\java学习\hutubill\lib\all.jar!\org\hsqldb\persist\LobStoreRAFile.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */
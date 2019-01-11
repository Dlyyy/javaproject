package org.hsqldb.persist;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import org.hsqldb.Database;
import org.hsqldb.lib.HsqlByteArrayOutputStream;
import org.hsqldb.lib.InputStreamInterface;
import org.hsqldb.lib.java.JavaSystem;
import org.hsqldb.map.BitMap;

public class RAShadowFile
{
  private static final int headerSize = 12;
  final Database database;
  final String pathName;
  final RandomAccessInterface source;
  RandomAccessInterface dest;
  final int pageSize;
  final long maxSize;
  final BitMap bitMap;
  boolean zeroPageSet;
  long savedLength;
  long synchLength;
  byte[] buffer;
  HsqlByteArrayOutputStream byteArrayOutputStream;
  
  RAShadowFile(Database paramDatabase, RandomAccessInterface paramRandomAccessInterface, String paramString, long paramLong, int paramInt)
  {
    this.database = paramDatabase;
    this.pathName = paramString;
    this.source = paramRandomAccessInterface;
    this.pageSize = paramInt;
    this.maxSize = paramLong;
    int i = (int)(paramLong / paramInt);
    if (paramLong % paramInt != 0L) {
      i++;
    }
    this.bitMap = new BitMap(i, false);
    this.buffer = new byte[paramInt + 12];
    this.byteArrayOutputStream = new HsqlByteArrayOutputStream(this.buffer);
  }
  
  int copy(long paramLong, int paramInt)
    throws IOException
  {
    int i = 0;
    if (!this.zeroPageSet)
    {
      i += copy(0);
      this.bitMap.set(0);
      this.zeroPageSet = true;
    }
    if (paramLong >= this.maxSize) {
      return i;
    }
    long l = paramLong + paramInt;
    if (l > this.maxSize) {
      l = this.maxSize;
    }
    int j = (int)(paramLong / this.pageSize);
    int k = (int)(l / this.pageSize);
    if (l % this.pageSize == 0L) {
      k--;
    }
    while (j <= k)
    {
      i += copy(j);
      j++;
    }
    return i;
  }
  
  private int copy(int paramInt)
    throws IOException
  {
    if (this.bitMap.set(paramInt) == 1) {
      return 0;
    }
    long l1 = paramInt * this.pageSize;
    int i = this.pageSize;
    int j = this.buffer.length;
    if (this.maxSize - l1 < this.pageSize) {
      i = (int)(this.maxSize - l1);
    }
    if (this.dest == null) {
      open();
    }
    long l2 = this.dest.length();
    try
    {
      this.byteArrayOutputStream.reset();
      if (i < this.pageSize)
      {
        this.byteArrayOutputStream.fill(0, this.buffer.length);
        this.byteArrayOutputStream.reset();
      }
      this.byteArrayOutputStream.writeInt(this.pageSize);
      this.byteArrayOutputStream.writeLong(l1);
      this.source.seek(l1);
      this.source.read(this.buffer, 12, i);
      this.dest.seek(l2);
      this.dest.write(this.buffer, 0, j);
      this.savedLength = (l2 + j);
      return 1;
    }
    catch (Throwable localThrowable)
    {
      this.bitMap.unset(paramInt);
      this.dest.seek(0L);
      this.dest.setLength(l2);
      close();
      this.database.logger.logSevereEvent("shadow backup failure pos " + l1 + " " + i, localThrowable);
      throw JavaSystem.toIOException(localThrowable);
    }
  }
  
  private void open()
    throws IOException
  {
    if (this.database.logger.isStoredFileAccess()) {
      this.dest = RAFile.newScaledRAFile(this.database, this.pathName, false, 3);
    } else {
      this.dest = new RAFileSimple(this.database.logger, this.pathName, "rw");
    }
  }
  
  void close()
    throws IOException
  {
    if (this.dest != null)
    {
      this.dest.synch();
      this.dest.close();
      this.dest = null;
    }
  }
  
  public void synch()
  {
    if (this.dest != null)
    {
      this.dest.synch();
      this.synchLength = this.savedLength;
    }
  }
  
  public long getSavedLength()
  {
    return this.savedLength;
  }
  
  public InputStreamInterface getInputStream()
  {
    return new InputStreamShadow();
  }
  
  private static RandomAccessInterface getStorage(Database paramDatabase, String paramString1, String paramString2)
    throws IOException
  {
    if (paramDatabase.logger.isStoredFileAccess()) {
      return RAFile.newScaledRAFile(paramDatabase, paramString1, paramString2.equals("r"), 3);
    }
    return new RAFileSimple(paramDatabase.logger, paramString1, paramString2);
  }
  
  public static void restoreFile(Database paramDatabase, String paramString1, String paramString2)
    throws IOException
  {
    RandomAccessInterface localRandomAccessInterface1 = getStorage(paramDatabase, paramString1, "r");
    RandomAccessInterface localRandomAccessInterface2 = getStorage(paramDatabase, paramString2, "rw");
    while (localRandomAccessInterface1.getFilePointer() != localRandomAccessInterface1.length())
    {
      int i = localRandomAccessInterface1.readInt();
      long l = localRandomAccessInterface1.readLong();
      byte[] arrayOfByte = new byte[i];
      localRandomAccessInterface1.read(arrayOfByte, 0, arrayOfByte.length);
      localRandomAccessInterface2.seek(l);
      localRandomAccessInterface2.write(arrayOfByte, 0, arrayOfByte.length);
    }
    localRandomAccessInterface1.close();
    localRandomAccessInterface2.synch();
    localRandomAccessInterface2.close();
  }
  
  class InputStreamShadow
    implements InputStreamInterface
  {
    FileInputStream is;
    long limitSize = 0L;
    long fetchedSize = 0L;
    boolean initialised = false;
    
    InputStreamShadow() {}
    
    public int read()
      throws IOException
    {
      if (!this.initialised) {
        initialise();
      }
      if (this.fetchedSize == this.limitSize) {
        return -1;
      }
      int i = this.is.read();
      if (i < 0) {
        throw new IOException("backup file not complete " + this.fetchedSize + " " + this.limitSize);
      }
      this.fetchedSize += 1L;
      return i;
    }
    
    public int read(byte[] paramArrayOfByte)
      throws IOException
    {
      return read(paramArrayOfByte, 0, paramArrayOfByte.length);
    }
    
    public int read(byte[] paramArrayOfByte, int paramInt1, int paramInt2)
      throws IOException
    {
      if (!this.initialised) {
        initialise();
      }
      if (this.fetchedSize == this.limitSize) {
        return -1;
      }
      if ((this.limitSize >= 0L) && (this.limitSize - this.fetchedSize < paramInt2)) {
        paramInt2 = (int)(this.limitSize - this.fetchedSize);
      }
      int i = this.is.read(paramArrayOfByte, paramInt1, paramInt2);
      if (i < 0) {
        throw new IOException("backup file not complete " + this.fetchedSize + " " + this.limitSize);
      }
      this.fetchedSize += i;
      return i;
    }
    
    public long skip(long paramLong)
      throws IOException
    {
      return 0L;
    }
    
    public int available()
      throws IOException
    {
      return 0;
    }
    
    public void close()
      throws IOException
    {
      if (this.is != null) {
        this.is.close();
      }
    }
    
    public void setSizeLimit(long paramLong)
    {
      this.limitSize = paramLong;
    }
    
    public long getSizeLimit()
    {
      if (!this.initialised) {
        initialise();
      }
      return this.limitSize;
    }
    
    private void initialise()
    {
      this.limitSize = RAShadowFile.this.synchLength;
      RAShadowFile.this.database.logger.logDetailEvent("shadow file size for backup: " + this.limitSize);
      if (this.limitSize > 0L) {
        try
        {
          this.is = new FileInputStream(RAShadowFile.this.pathName);
        }
        catch (FileNotFoundException localFileNotFoundException) {}
      }
      this.initialised = true;
    }
  }
}


/* Location:              E:\java\java学习\hutubill\lib\all.jar!\org\hsqldb\persist\RAShadowFile.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */
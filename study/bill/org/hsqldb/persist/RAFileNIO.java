package org.hsqldb.persist;

import java.io.File;
import java.io.FileDescriptor;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.FileChannel.MapMode;
import org.hsqldb.Database;
import org.hsqldb.error.Error;
import org.hsqldb.lib.ArrayUtil;
import org.hsqldb.lib.java.JavaSystem;

final class RAFileNIO
  implements RandomAccessInterface
{
  private final EventLogInterface logger;
  private final boolean readOnly;
  private final long maxLength;
  private long fileLength;
  private RandomAccessFile file;
  private FileDescriptor fileDescriptor;
  private MappedByteBuffer buffer;
  private long bufferPosition;
  private int bufferLength;
  private long currentPosition;
  private FileChannel channel;
  private boolean buffersModified;
  private MappedByteBuffer[] buffers = new MappedByteBuffer[0];
  private static final String JVM_ERROR = "NIO access failed";
  static final int largeBufferScale = 24;
  static final int largeBufferSize = 16777216;
  static final long largeBufferMask = -16777216L;
  
  RAFileNIO(EventLogInterface paramEventLogInterface, String paramString, boolean paramBoolean, long paramLong1, long paramLong2)
    throws IOException
  {
    this.logger = paramEventLogInterface;
    this.maxLength = paramLong2;
    File localFile = new File(paramString);
    if (paramBoolean)
    {
      paramLong1 = localFile.length();
    }
    else
    {
      if (localFile.length() > paramLong1) {
        paramLong1 = localFile.length();
      }
      paramLong1 = ArrayUtil.getBinaryNormalisedCeiling(paramLong1, 24);
    }
    this.file = new RandomAccessFile(paramString, paramBoolean ? "r" : "rw");
    this.readOnly = paramBoolean;
    this.channel = this.file.getChannel();
    this.fileDescriptor = this.file.getFD();
    if (ensureLength(paramLong1))
    {
      this.buffer = this.buffers[0];
      this.bufferLength = this.buffer.limit();
      this.bufferPosition = 0L;
      this.currentPosition = 0L;
    }
    else
    {
      close();
      IOException localIOException = new IOException("NIO buffer allocation failed");
      throw localIOException;
    }
  }
  
  public long length()
    throws IOException
  {
    try
    {
      return this.file.length();
    }
    catch (IOException localIOException1)
    {
      this.logger.logWarningEvent("NIO access failed", localIOException1);
      throw localIOException1;
    }
    catch (Throwable localThrowable)
    {
      this.logger.logWarningEvent("NIO access failed", localThrowable);
      IOException localIOException2 = JavaSystem.toIOException(localThrowable);
      throw localIOException2;
    }
  }
  
  public void seek(long paramLong)
    throws IOException
  {
    try
    {
      positionBufferSeek(paramLong);
      this.buffer.position((int)(paramLong - this.bufferPosition));
    }
    catch (IllegalArgumentException localIllegalArgumentException)
    {
      this.logger.logWarningEvent("NIO access failed", localIllegalArgumentException);
      localIOException = JavaSystem.toIOException(localIllegalArgumentException);
      throw localIOException;
    }
    catch (Throwable localThrowable)
    {
      this.logger.logWarningEvent("NIO access failed", localThrowable);
      IOException localIOException = JavaSystem.toIOException(localThrowable);
      throw localIOException;
    }
  }
  
  public long getFilePointer()
    throws IOException
  {
    try
    {
      return this.currentPosition;
    }
    catch (Throwable localThrowable)
    {
      this.logger.logWarningEvent("NIO access failed", localThrowable);
      IOException localIOException = JavaSystem.toIOException(localThrowable);
      throw localIOException;
    }
  }
  
  public int read()
    throws IOException
  {
    try
    {
      int i = this.buffer.get();
      positionBufferMove(1);
      return i;
    }
    catch (Throwable localThrowable)
    {
      this.logger.logWarningEvent("NIO access failed", localThrowable);
      IOException localIOException = JavaSystem.toIOException(localThrowable);
      throw localIOException;
    }
  }
  
  public void read(byte[] paramArrayOfByte, int paramInt1, int paramInt2)
    throws IOException
  {
    try
    {
      for (;;)
      {
        checkBuffer();
        long l = this.bufferPosition + this.bufferLength - this.currentPosition;
        if (l > paramInt2) {
          l = paramInt2;
        }
        this.buffer.get(paramArrayOfByte, paramInt1, (int)l);
        positionBufferMove((int)l);
        paramInt2 = (int)(paramInt2 - l);
        paramInt1 = (int)(paramInt1 + l);
        if (paramInt2 == 0) {
          break;
        }
      }
    }
    catch (Throwable localThrowable)
    {
      this.logger.logWarningEvent("NIO access failed", localThrowable);
      IOException localIOException = JavaSystem.toIOException(localThrowable);
      throw localIOException;
    }
  }
  
  public int readInt()
    throws IOException
  {
    try
    {
      int i = this.buffer.getInt();
      positionBufferMove(4);
      return i;
    }
    catch (Throwable localThrowable)
    {
      this.logger.logWarningEvent("NIO access failed", localThrowable);
      IOException localIOException = JavaSystem.toIOException(localThrowable);
      throw localIOException;
    }
  }
  
  public long readLong()
    throws IOException
  {
    try
    {
      long l = this.buffer.getLong();
      positionBufferMove(8);
      return l;
    }
    catch (Throwable localThrowable)
    {
      this.logger.logWarningEvent("NIO access failed", localThrowable);
      IOException localIOException = JavaSystem.toIOException(localThrowable);
      throw localIOException;
    }
  }
  
  public void write(byte[] paramArrayOfByte, int paramInt1, int paramInt2)
    throws IOException
  {
    try
    {
      this.buffersModified = true;
      for (;;)
      {
        checkBuffer();
        long l = this.bufferPosition + this.bufferLength - this.currentPosition;
        if (l > paramInt2) {
          l = paramInt2;
        }
        this.buffer.put(paramArrayOfByte, paramInt1, (int)l);
        positionBufferMove((int)l);
        paramInt2 = (int)(paramInt2 - l);
        paramInt1 = (int)(paramInt1 + l);
        if (paramInt2 == 0) {
          break;
        }
      }
    }
    catch (Throwable localThrowable)
    {
      this.logger.logWarningEvent("NIO access failed", localThrowable);
      IOException localIOException = JavaSystem.toIOException(localThrowable);
      throw localIOException;
    }
  }
  
  public void writeInt(int paramInt)
    throws IOException
  {
    try
    {
      this.buffersModified = true;
      this.buffer.putInt(paramInt);
      positionBufferMove(4);
    }
    catch (Throwable localThrowable)
    {
      this.logger.logWarningEvent("NIO access failed", localThrowable);
      IOException localIOException = JavaSystem.toIOException(localThrowable);
      throw localIOException;
    }
  }
  
  public void writeLong(long paramLong)
    throws IOException
  {
    try
    {
      this.buffersModified = true;
      this.buffer.putLong(paramLong);
      positionBufferMove(8);
    }
    catch (Throwable localThrowable)
    {
      this.logger.logWarningEvent("NIO access failed", localThrowable);
      IOException localIOException = JavaSystem.toIOException(localThrowable);
      throw localIOException;
    }
  }
  
  public void close()
    throws IOException
  {
    try
    {
      this.logger.logDetailEvent("NIO file close, size: " + this.fileLength);
      this.buffer = null;
      this.channel = null;
      for (int i = 0; i < this.buffers.length; i++)
      {
        unmap(this.buffers[i]);
        this.buffers[i] = null;
      }
      this.file.close();
    }
    catch (Throwable localThrowable)
    {
      this.logger.logWarningEvent("NIO buffer close error", localThrowable);
      IOException localIOException = JavaSystem.toIOException(localThrowable);
      throw localIOException;
    }
  }
  
  public boolean isReadOnly()
  {
    return this.readOnly;
  }
  
  public boolean ensureLength(long paramLong)
  {
    if (paramLong > this.maxLength) {
      return false;
    }
    while (paramLong > this.fileLength) {
      if (!enlargeFile(paramLong)) {
        return false;
      }
    }
    return true;
  }
  
  private boolean enlargeFile(long paramLong)
  {
    try
    {
      long l = paramLong;
      if (!this.readOnly) {
        l = 16777216L;
      }
      FileChannel.MapMode localMapMode = this.readOnly ? FileChannel.MapMode.READ_ONLY : FileChannel.MapMode.READ_WRITE;
      if ((!this.readOnly) && (this.file.length() < this.fileLength + l))
      {
        this.file.seek(this.fileLength + l - 1L);
        this.file.writeByte(0);
      }
      MappedByteBuffer[] arrayOfMappedByteBuffer = new MappedByteBuffer[this.buffers.length + 1];
      MappedByteBuffer localMappedByteBuffer = this.channel.map(localMapMode, this.fileLength, l);
      System.arraycopy(this.buffers, 0, arrayOfMappedByteBuffer, 0, this.buffers.length);
      arrayOfMappedByteBuffer[this.buffers.length] = localMappedByteBuffer;
      this.buffers = arrayOfMappedByteBuffer;
      this.fileLength += l;
      this.logger.logDetailEvent("NIO buffer instance, file size " + this.fileLength);
    }
    catch (Throwable localThrowable)
    {
      this.logger.logDetailEvent("NOI buffer allocate failed, file size " + paramLong);
      return false;
    }
    return true;
  }
  
  public boolean setLength(long paramLong)
  {
    if (paramLong > this.fileLength) {
      return enlargeFile(paramLong);
    }
    try
    {
      seek(0L);
    }
    catch (Throwable localThrowable) {}
    return true;
  }
  
  public Database getDatabase()
  {
    return null;
  }
  
  public void synch()
  {
    int i = 0;
    int j = 0;
    for (int k = 0; k < this.buffers.length; k++) {
      try
      {
        this.buffers[k].force();
      }
      catch (Throwable localThrowable2)
      {
        this.logger.logWarningEvent("NIO buffer force error: pos " + k * 16777216 + " ", localThrowable2);
        if (i == 0) {
          j = k;
        }
        i = 1;
      }
    }
    if (i != 0) {
      for (k = j; k < this.buffers.length; k++) {
        try
        {
          this.buffers[k].force();
        }
        catch (Throwable localThrowable3)
        {
          this.logger.logWarningEvent("NIO buffer force error " + k * 16777216 + " ", localThrowable3);
        }
      }
    }
    try
    {
      this.fileDescriptor.sync();
      this.buffersModified = false;
    }
    catch (Throwable localThrowable1)
    {
      this.logger.logSevereEvent("NIO RA file sync error ", localThrowable1);
      throw Error.error(localThrowable1, 452, null);
    }
  }
  
  private void positionBufferSeek(long paramLong)
  {
    if ((paramLong < this.bufferPosition) || (paramLong >= this.bufferPosition + this.bufferLength)) {
      setCurrentBuffer(paramLong);
    }
    this.buffer.position((int)(paramLong - this.bufferPosition));
    this.currentPosition = paramLong;
  }
  
  private void positionBufferMove(int paramInt)
  {
    long l = this.currentPosition + paramInt;
    if (l >= this.bufferPosition + this.bufferLength) {
      setCurrentBuffer(l);
    }
    this.buffer.position((int)(l - this.bufferPosition));
    this.currentPosition = l;
  }
  
  private void setCurrentBuffer(long paramLong)
  {
    int i = (int)(paramLong >> 24);
    if (i == this.buffers.length)
    {
      i = this.buffers.length - 1;
      this.bufferPosition = (i * 16777216L);
      this.buffer = this.buffers[i];
      return;
    }
    this.buffer = this.buffers[i];
    this.bufferPosition = (paramLong & 0xFFFFFFFFFF000000);
  }
  
  private void checkBuffer()
  {
    int i = (int)(this.currentPosition >> 24);
    if (this.currentPosition != this.bufferPosition + this.buffer.position())
    {
      this.buffer = this.buffers[i];
      this.bufferPosition = (this.currentPosition & 0xFFFFFFFFFF000000);
      this.buffer.position((int)(this.currentPosition - this.bufferPosition));
    }
    else if (this.buffer != this.buffers[i])
    {
      this.buffer = this.buffers[i];
    }
  }
  
  private void unmap(MappedByteBuffer paramMappedByteBuffer)
    throws IOException
  {
    if (paramMappedByteBuffer == null) {
      return;
    }
    try
    {
      Method localMethod1 = paramMappedByteBuffer.getClass().getMethod("cleaner", new Class[0]);
      localMethod1.setAccessible(true);
      Object localObject = localMethod1.invoke(paramMappedByteBuffer, new Object[0]);
      Method localMethod2 = localObject.getClass().getMethod("clean", new Class[0]);
      localMethod2.invoke(localObject, new Object[0]);
    }
    catch (InvocationTargetException localInvocationTargetException) {}catch (NoSuchMethodException localNoSuchMethodException) {}catch (Throwable localThrowable) {}
  }
}


/* Location:              E:\java\java学习\hutubill\lib\all.jar!\org\hsqldb\persist\RAFileNIO.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */
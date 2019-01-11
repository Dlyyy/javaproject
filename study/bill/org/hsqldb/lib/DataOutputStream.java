package org.hsqldb.lib;

import java.io.BufferedOutputStream;
import java.io.DataOutput;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Reader;
import java.io.UTFDataFormatException;

public class DataOutputStream
  extends BufferedOutputStream
  implements DataOutput
{
  byte[] tempBuffer = new byte[8];
  
  public DataOutputStream(OutputStream paramOutputStream)
  {
    super(paramOutputStream, 8);
  }
  
  public final void writeByte(int paramInt)
    throws IOException
  {
    write(paramInt);
  }
  
  public final void writeInt(int paramInt)
    throws IOException
  {
    int i = 0;
    this.tempBuffer[(i++)] = ((byte)(paramInt >>> 24));
    this.tempBuffer[(i++)] = ((byte)(paramInt >>> 16));
    this.tempBuffer[(i++)] = ((byte)(paramInt >>> 8));
    this.tempBuffer[(i++)] = ((byte)paramInt);
    write(this.tempBuffer, 0, i);
  }
  
  public final void writeLong(long paramLong)
    throws IOException
  {
    writeInt((int)(paramLong >>> 32));
    writeInt((int)paramLong);
  }
  
  public void writeChar(int paramInt)
    throws IOException
  {
    int i = 0;
    this.tempBuffer[(i++)] = ((byte)(paramInt >>> 8));
    this.tempBuffer[(i++)] = ((byte)paramInt);
    write(this.tempBuffer, 0, i);
  }
  
  public void writeChars(String paramString)
    throws IOException
  {
    int i = paramString.length();
    for (int j = 0; j < i; j++)
    {
      int k = paramString.charAt(j);
      int m = 0;
      this.tempBuffer[(m++)] = ((byte)(k >>> 8));
      this.tempBuffer[(m++)] = ((byte)k);
      write(this.tempBuffer, 0, m);
    }
  }
  
  public void writeChars(char[] paramArrayOfChar)
    throws IOException
  {
    writeChars(paramArrayOfChar, paramArrayOfChar.length);
  }
  
  public void writeChars(char[] paramArrayOfChar, int paramInt)
    throws IOException
  {
    for (int i = 0; i < paramInt; i++)
    {
      int j = paramArrayOfChar[i];
      int k = 0;
      this.tempBuffer[(k++)] = ((byte)(j >>> 8));
      this.tempBuffer[(k++)] = ((byte)j);
      write(this.tempBuffer, 0, k);
    }
  }
  
  public long write(Reader paramReader, long paramLong)
    throws IOException
  {
    ReaderInputStream localReaderInputStream = new ReaderInputStream(paramReader);
    return write(localReaderInputStream, paramLong * 2L) / 2L;
  }
  
  public long write(InputStream paramInputStream, long paramLong)
    throws IOException
  {
    byte[] arrayOfByte = new byte['Ѐ'];
    long l2;
    for (long l1 = 0L;; l1 += l2)
    {
      l2 = paramLong - l1;
      if (l2 > arrayOfByte.length) {
        l2 = arrayOfByte.length;
      }
      l2 = paramInputStream.read(arrayOfByte, 0, (int)l2);
      if (l2 < 1L) {
        break;
      }
      write(arrayOfByte, 0, (int)l2);
    }
    return l1;
  }
  
  public void writeBoolean(boolean paramBoolean)
    throws IOException
  {
    int i = paramBoolean ? 1 : 0;
    write(i);
  }
  
  public void writeShort(int paramInt)
    throws IOException
  {
    int i = 0;
    this.tempBuffer[(i++)] = ((byte)(paramInt >> 8));
    this.tempBuffer[(i++)] = ((byte)paramInt);
    write(this.tempBuffer, 0, i);
  }
  
  public void writeFloat(float paramFloat)
    throws IOException
  {
    writeInt(Float.floatToIntBits(paramFloat));
  }
  
  public void writeDouble(double paramDouble)
    throws IOException
  {
    writeLong(Double.doubleToLongBits(paramDouble));
  }
  
  public void writeBytes(String paramString)
    throws IOException
  {
    int i = paramString.length();
    for (int j = 0; j < i; j++) {
      this.out.write((byte)paramString.charAt(j));
    }
  }
  
  public void writeUTF(String paramString)
    throws IOException
  {
    int i = paramString.length();
    if (i > 65535) {
      throw new UTFDataFormatException();
    }
    int j = StringConverter.getUTFSize(paramString);
    if (j > 65535) {
      throw new UTFDataFormatException();
    }
    writeChar(j);
    HsqlByteArrayOutputStream localHsqlByteArrayOutputStream = new HsqlByteArrayOutputStream(j);
    StringConverter.stringToUTFBytes(paramString, localHsqlByteArrayOutputStream);
    write(localHsqlByteArrayOutputStream.getBuffer(), 0, localHsqlByteArrayOutputStream.size());
  }
}


/* Location:              E:\java\java学习\hutubill\lib\all.jar!\org\hsqldb\lib\DataOutputStream.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */
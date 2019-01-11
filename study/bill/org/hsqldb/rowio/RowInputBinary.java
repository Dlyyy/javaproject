package org.hsqldb.rowio;

import java.io.EOFException;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import org.hsqldb.error.Error;
import org.hsqldb.lib.StringConverter;
import org.hsqldb.map.ValuePool;
import org.hsqldb.types.BinaryData;
import org.hsqldb.types.BlobData;
import org.hsqldb.types.BlobDataID;
import org.hsqldb.types.ClobData;
import org.hsqldb.types.ClobDataID;
import org.hsqldb.types.IntervalMonthData;
import org.hsqldb.types.IntervalSecondData;
import org.hsqldb.types.IntervalType;
import org.hsqldb.types.JavaObjectData;
import org.hsqldb.types.TimeData;
import org.hsqldb.types.TimestampData;
import org.hsqldb.types.Type;

public class RowInputBinary
  extends RowInputBase
  implements RowInputInterface
{
  public boolean ignoreDataErrors;
  private RowOutputBinary out;
  
  public RowInputBinary()
  {
    this(64);
  }
  
  public RowInputBinary(int paramInt)
  {
    super(paramInt);
  }
  
  public RowInputBinary(byte[] paramArrayOfByte)
  {
    super(paramArrayOfByte);
  }
  
  public RowInputBinary(RowOutputBinary paramRowOutputBinary)
  {
    super(paramRowOutputBinary.getBuffer());
    this.out = paramRowOutputBinary;
  }
  
  public void readFully(byte[] paramArrayOfByte)
  {
    try
    {
      super.readFully(paramArrayOfByte);
    }
    catch (IOException localIOException)
    {
      throw Error.error(localIOException, 467, "RowInputBinary");
    }
  }
  
  public long readLong()
  {
    try
    {
      return super.readLong();
    }
    catch (IOException localIOException)
    {
      throw Error.error(localIOException, 467, "RowInputBinary");
    }
  }
  
  public int readInt()
  {
    try
    {
      return super.readInt();
    }
    catch (IOException localIOException)
    {
      throw Error.error(localIOException, 467, "RowInputBinary");
    }
  }
  
  public short readShort()
  {
    try
    {
      return super.readShort();
    }
    catch (IOException localIOException)
    {
      throw Error.error(localIOException, 467, "RowInputBinary");
    }
  }
  
  public char readChar()
  {
    try
    {
      return super.readChar();
    }
    catch (IOException localIOException)
    {
      throw Error.error(localIOException, 467, "RowInputBinary");
    }
  }
  
  public byte readByte()
  {
    try
    {
      return super.readByte();
    }
    catch (IOException localIOException)
    {
      throw Error.error(localIOException, 467, "RowInputBinary");
    }
  }
  
  public boolean readBoolean()
  {
    try
    {
      return super.readBoolean();
    }
    catch (IOException localIOException)
    {
      throw Error.error(localIOException, 467, "RowInputBinary");
    }
  }
  
  public int readType()
  {
    return readShort();
  }
  
  public String readString()
  {
    try
    {
      int i = readInt();
      if (i < 0) {
        throw Error.error(467, "RowInputBinary - negative length");
      }
      String str = StringConverter.readUTF(this.buffer, this.pos, i);
      str = ValuePool.getString(str);
      this.pos += i;
      return str;
    }
    catch (IOException localIOException)
    {
      throw Error.error(localIOException, 467, "RowInputBinary");
    }
  }
  
  public boolean readNull()
  {
    int i = readByte();
    return i == 0;
  }
  
  protected String readChar(Type paramType)
  {
    return readString();
  }
  
  protected Integer readSmallint()
  {
    return ValuePool.getInt(readShort());
  }
  
  protected Integer readInteger()
  {
    return ValuePool.getInt(readInt());
  }
  
  protected Long readBigint()
  {
    return ValuePool.getLong(readLong());
  }
  
  protected Double readReal()
  {
    return ValuePool.getDouble(readLong());
  }
  
  protected BigDecimal readDecimal(Type paramType)
  {
    byte[] arrayOfByte = readByteArray();
    int i = readInt();
    BigInteger localBigInteger = new BigInteger(arrayOfByte);
    return ValuePool.getBigDecimal(new BigDecimal(localBigInteger, i));
  }
  
  protected Boolean readBoole()
  {
    return readBoolean() ? Boolean.TRUE : Boolean.FALSE;
  }
  
  protected TimeData readTime(Type paramType)
  {
    if (paramType.typeCode == 92) {
      return new TimeData(readInt(), readInt(), 0);
    }
    return new TimeData(readInt(), readInt(), readInt());
  }
  
  protected TimestampData readDate(Type paramType)
  {
    long l = readLong();
    return new TimestampData(l);
  }
  
  protected TimestampData readTimestamp(Type paramType)
  {
    if (paramType.typeCode == 93) {
      return new TimestampData(readLong(), readInt());
    }
    return new TimestampData(readLong(), readInt(), readInt());
  }
  
  protected IntervalMonthData readYearMonthInterval(Type paramType)
  {
    long l = readLong();
    return new IntervalMonthData(l, (IntervalType)paramType);
  }
  
  protected IntervalSecondData readDaySecondInterval(Type paramType)
  {
    long l = readLong();
    int i = readInt();
    return new IntervalSecondData(l, i, (IntervalType)paramType);
  }
  
  protected Object readOther()
  {
    return new JavaObjectData(readByteArray());
  }
  
  protected BinaryData readBit()
  {
    int i = readInt();
    byte[] arrayOfByte = new byte[(i + 7) / 8];
    readFully(arrayOfByte);
    return BinaryData.getBitData(arrayOfByte, i);
  }
  
  protected BinaryData readBinary()
  {
    return new BinaryData(readByteArray(), false);
  }
  
  protected ClobData readClob()
  {
    long l = readLong();
    return new ClobDataID(l);
  }
  
  protected BlobData readBlob()
  {
    long l = readLong();
    return new BlobDataID(l);
  }
  
  protected Object[] readArray(Type paramType)
  {
    paramType = paramType.collectionBaseType();
    int i = readInt();
    Object[] arrayOfObject = new Object[i];
    for (int j = 0; j < i; j++) {
      arrayOfObject[j] = readData(paramType);
    }
    return arrayOfObject;
  }
  
  public int[] readIntArray()
    throws IOException
  {
    int i = readInt();
    int[] arrayOfInt = new int[i];
    for (int j = 0; j < i; j++) {
      if (!readNull()) {
        arrayOfInt[j] = readInt();
      }
    }
    return arrayOfInt;
  }
  
  public Object[] readData(Type[] paramArrayOfType)
  {
    if (this.ignoreDataErrors) {
      return new Object[paramArrayOfType.length];
    }
    return super.readData(paramArrayOfType);
  }
  
  public byte[] readByteArray()
  {
    int i = readInt();
    byte[] arrayOfByte = new byte[i];
    readFully(arrayOfByte);
    return arrayOfByte;
  }
  
  public char[] readCharArray()
    throws IOException
  {
    int i = readInt();
    char[] arrayOfChar = new char[i];
    if (this.count - this.pos < arrayOfChar.length)
    {
      this.pos = this.count;
      throw new EOFException();
    }
    for (int j = 0; j < arrayOfChar.length; j++)
    {
      int k = this.buffer[(this.pos++)] & 0xFF;
      int m = this.buffer[(this.pos++)] & 0xFF;
      arrayOfChar[j] = ((char)((k << 8) + m));
    }
    return arrayOfChar;
  }
  
  public void resetRow(int paramInt)
  {
    if (this.out != null)
    {
      this.out.reset(paramInt);
      this.buffer = this.out.getBuffer();
    }
    super.reset();
  }
  
  public void resetRow(long paramLong, int paramInt)
  {
    if (this.out != null)
    {
      this.out.reset(paramInt);
      this.buffer = this.out.getBuffer();
    }
    super.resetRow(paramLong, paramInt);
  }
}


/* Location:              E:\java\java学习\hutubill\lib\all.jar!\org\hsqldb\rowio\RowInputBinary.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */
package org.hsqldb.rowio;

import java.math.BigDecimal;
import org.hsqldb.error.Error;
import org.hsqldb.lib.HsqlByteArrayInputStream;
import org.hsqldb.types.BinaryData;
import org.hsqldb.types.BlobData;
import org.hsqldb.types.ClobData;
import org.hsqldb.types.IntervalMonthData;
import org.hsqldb.types.IntervalSecondData;
import org.hsqldb.types.TimeData;
import org.hsqldb.types.TimestampData;
import org.hsqldb.types.Type;

abstract class RowInputBase
  extends HsqlByteArrayInputStream
{
  static final int NO_POS = -1;
  protected long filePos = -1L;
  protected int size;
  
  RowInputBase()
  {
    this(new byte[4]);
  }
  
  RowInputBase(int paramInt)
  {
    this(new byte[paramInt]);
  }
  
  RowInputBase(byte[] paramArrayOfByte)
  {
    super(paramArrayOfByte);
    this.size = paramArrayOfByte.length;
  }
  
  public long getPos()
  {
    if (this.filePos == -1L) {}
    return this.filePos;
  }
  
  public int getSize()
  {
    return this.size;
  }
  
  public abstract int readType();
  
  public abstract String readString();
  
  protected abstract boolean readNull();
  
  protected abstract String readChar(Type paramType);
  
  protected abstract Integer readSmallint();
  
  protected abstract Integer readInteger();
  
  protected abstract Long readBigint();
  
  protected abstract Double readReal();
  
  protected abstract BigDecimal readDecimal(Type paramType);
  
  protected abstract Boolean readBoole();
  
  protected abstract TimeData readTime(Type paramType);
  
  protected abstract TimestampData readDate(Type paramType);
  
  protected abstract TimestampData readTimestamp(Type paramType);
  
  protected abstract IntervalMonthData readYearMonthInterval(Type paramType);
  
  protected abstract IntervalSecondData readDaySecondInterval(Type paramType);
  
  protected abstract Object readOther();
  
  protected abstract BinaryData readBinary();
  
  protected abstract BinaryData readBit();
  
  protected abstract ClobData readClob();
  
  protected abstract BlobData readBlob();
  
  protected abstract Object[] readArray(Type paramType);
  
  public Object[] readData(Type[] paramArrayOfType)
  {
    int i = paramArrayOfType.length;
    Object[] arrayOfObject = new Object[i];
    for (int j = 0; j < i; j++)
    {
      Type localType = paramArrayOfType[j];
      arrayOfObject[j] = readData(localType);
    }
    return arrayOfObject;
  }
  
  public Object readData(Type paramType)
  {
    Object localObject = null;
    if (readNull()) {
      return null;
    }
    switch (paramType.typeCode)
    {
    case 0: 
      break;
    case 1: 
    case 12: 
      localObject = readChar(paramType);
      break;
    case -6: 
    case 5: 
      localObject = readSmallint();
      break;
    case 4: 
      localObject = readInteger();
      break;
    case 25: 
      localObject = readBigint();
      break;
    case 6: 
    case 7: 
    case 8: 
      localObject = readReal();
      break;
    case 2: 
    case 3: 
      localObject = readDecimal(paramType);
      break;
    case 91: 
      localObject = readDate(paramType);
      break;
    case 92: 
    case 94: 
      localObject = readTime(paramType);
      break;
    case 93: 
    case 95: 
      localObject = readTimestamp(paramType);
      break;
    case 101: 
    case 102: 
    case 107: 
      localObject = readYearMonthInterval(paramType);
      break;
    case 103: 
    case 104: 
    case 105: 
    case 106: 
    case 108: 
    case 109: 
    case 110: 
    case 111: 
    case 112: 
    case 113: 
      localObject = readDaySecondInterval(paramType);
      break;
    case 16: 
      localObject = readBoole();
      break;
    case 1111: 
      localObject = readOther();
      break;
    case 40: 
      localObject = readClob();
      break;
    case 30: 
      localObject = readBlob();
      break;
    case 50: 
      localObject = readArray(paramType);
      break;
    case -11: 
    case 60: 
    case 61: 
      localObject = readBinary();
      break;
    case 14: 
    case 15: 
      localObject = readBit();
      break;
    default: 
      throw Error.runtimeError(201, "RowInputBase - " + paramType.getNameString());
    }
    return localObject;
  }
  
  public void resetRow(long paramLong, int paramInt)
  {
    this.mark = 0;
    reset();
    if (this.buffer.length < paramInt) {
      this.buffer = new byte[paramInt];
    }
    this.filePos = paramLong;
    this.size = (this.count = paramInt);
    this.pos = 4;
    this.buffer[0] = ((byte)(paramInt >>> 24 & 0xFF));
    this.buffer[1] = ((byte)(paramInt >>> 16 & 0xFF));
    this.buffer[2] = ((byte)(paramInt >>> 8 & 0xFF));
    this.buffer[3] = ((byte)(paramInt >>> 0 & 0xFF));
  }
  
  public void resetBlock(long paramLong, int paramInt)
  {
    this.mark = 0;
    reset();
    if (this.buffer.length < paramInt) {
      this.buffer = new byte[paramInt];
    }
    this.filePos = paramLong;
    this.size = (this.count = paramInt);
  }
  
  public byte[] getBuffer()
  {
    return this.buffer;
  }
  
  public int skipBytes(int paramInt)
  {
    throw Error.runtimeError(201, "RowInputBase");
  }
  
  public String readLine()
  {
    throw Error.runtimeError(201, "RowInputBase");
  }
}


/* Location:              E:\java\java学习\hutubill\lib\all.jar!\org\hsqldb\rowio\RowInputBase.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */
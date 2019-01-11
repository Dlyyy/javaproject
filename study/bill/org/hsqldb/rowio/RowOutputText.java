package org.hsqldb.rowio;

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import org.hsqldb.Row;
import org.hsqldb.TableBase;
import org.hsqldb.error.Error;
import org.hsqldb.lib.StringConverter;
import org.hsqldb.persist.TextFileSettings;
import org.hsqldb.types.BinaryData;
import org.hsqldb.types.BlobData;
import org.hsqldb.types.ClobData;
import org.hsqldb.types.IntervalMonthData;
import org.hsqldb.types.IntervalSecondData;
import org.hsqldb.types.JavaObjectData;
import org.hsqldb.types.TimeData;
import org.hsqldb.types.TimestampData;
import org.hsqldb.types.Type;

public class RowOutputText
  extends RowOutputBase
{
  protected String fieldSep;
  protected String varSep;
  protected String longvarSep;
  private boolean fieldSepEnd;
  private boolean varSepEnd;
  private boolean longvarSepEnd;
  private String nextSep = "";
  private boolean nextSepEnd;
  protected TextFileSettings textFileSettings;
  
  public RowOutputText(TextFileSettings paramTextFileSettings)
  {
    initTextDatabaseRowOutput(paramTextFileSettings);
  }
  
  private void initTextDatabaseRowOutput(TextFileSettings paramTextFileSettings)
  {
    this.textFileSettings = paramTextFileSettings;
    this.fieldSep = paramTextFileSettings.fs;
    this.varSep = paramTextFileSettings.vs;
    this.longvarSep = paramTextFileSettings.lvs;
    if (this.fieldSep.endsWith("\n"))
    {
      this.fieldSepEnd = true;
      this.fieldSep = this.fieldSep.substring(0, this.fieldSep.length() - 1);
    }
    if (this.varSep.endsWith("\n"))
    {
      this.varSepEnd = true;
      this.varSep = this.varSep.substring(0, this.varSep.length() - 1);
    }
    if (this.longvarSep.endsWith("\n"))
    {
      this.longvarSepEnd = true;
      this.longvarSep = this.longvarSep.substring(0, this.longvarSep.length() - 1);
    }
  }
  
  public void setStorageSize(int paramInt) {}
  
  public void writeEnd()
  {
    if (this.nextSepEnd) {
      writeBytes(this.nextSep);
    }
    writeBytes(TextFileSettings.NL);
  }
  
  public void writeSize(int paramInt)
  {
    this.nextSep = "";
    this.nextSepEnd = false;
  }
  
  public void writeType(int paramInt) {}
  
  public void writeString(String paramString)
  {
    paramString = checkConvertString(paramString, this.fieldSep);
    if (paramString == null) {
      return;
    }
    byte[] arrayOfByte = getBytes(paramString);
    write(arrayOfByte, 0, arrayOfByte.length);
    this.nextSep = this.fieldSep;
    this.nextSepEnd = this.fieldSepEnd;
  }
  
  protected void writeVarString(String paramString)
  {
    paramString = checkConvertString(paramString, this.varSep);
    if (paramString == null) {
      return;
    }
    byte[] arrayOfByte = getBytes(paramString);
    write(arrayOfByte, 0, arrayOfByte.length);
    this.nextSep = this.varSep;
    this.nextSepEnd = this.varSepEnd;
  }
  
  protected void writeLongVarString(String paramString)
  {
    paramString = checkConvertString(paramString, this.longvarSep);
    if (paramString == null) {
      return;
    }
    byte[] arrayOfByte = getBytes(paramString);
    write(arrayOfByte, 0, arrayOfByte.length);
    this.nextSep = this.longvarSep;
    this.nextSepEnd = this.longvarSepEnd;
  }
  
  protected String checkConvertString(String paramString1, String paramString2)
  {
    if ((paramString1.indexOf('\n') != -1) || (paramString1.indexOf('\r') != -1)) {
      throw new IllegalArgumentException(Error.getMessage(485));
    }
    if (paramString1.indexOf(paramString2) != -1) {
      return null;
    }
    return paramString1;
  }
  
  private byte[] getBytes(String paramString)
  {
    byte[] arrayOfByte = null;
    try
    {
      arrayOfByte = paramString.getBytes(this.textFileSettings.charEncoding);
    }
    catch (UnsupportedEncodingException localUnsupportedEncodingException)
    {
      throw Error.error(484, localUnsupportedEncodingException);
    }
    return arrayOfByte;
  }
  
  protected void writeByteArray(byte[] paramArrayOfByte)
  {
    if (this.textFileSettings.isUTF16)
    {
      byte[] arrayOfByte = new byte[paramArrayOfByte.length * 2];
      StringConverter.writeHexBytes(arrayOfByte, 0, paramArrayOfByte);
      String str = new String(arrayOfByte);
      writeBytes(str);
    }
    else
    {
      ensureRoom(paramArrayOfByte.length * 2);
      StringConverter.writeHexBytes(getBuffer(), this.count, paramArrayOfByte);
      this.count += paramArrayOfByte.length * 2;
    }
  }
  
  public void writeShort(int paramInt)
  {
    writeInt(paramInt);
  }
  
  public void writeInt(int paramInt)
  {
    writeBytes(Integer.toString(paramInt));
    this.nextSep = this.fieldSep;
    this.nextSepEnd = this.fieldSepEnd;
  }
  
  public void writeLong(long paramLong)
  {
    throw Error.runtimeError(201, "RowOutputText");
  }
  
  public void writeBytes(String paramString)
  {
    if (this.textFileSettings.isUTF16) {
      try
      {
        if (paramString.length() > 0)
        {
          byte[] arrayOfByte = paramString.getBytes(this.textFileSettings.charEncoding);
          super.write(arrayOfByte);
        }
      }
      catch (UnsupportedEncodingException localUnsupportedEncodingException)
      {
        throw Error.error(484, localUnsupportedEncodingException);
      }
    } else {
      super.writeBytes(paramString);
    }
  }
  
  protected void writeFieldType(Type paramType)
  {
    writeBytes(this.nextSep);
    switch (paramType.typeCode)
    {
    case 12: 
      this.nextSep = this.varSep;
      this.nextSepEnd = this.varSepEnd;
      break;
    default: 
      this.nextSep = this.fieldSep;
      this.nextSepEnd = this.fieldSepEnd;
    }
  }
  
  protected void writeNull(Type paramType)
  {
    writeFieldType(paramType);
  }
  
  protected void writeChar(String paramString, Type paramType)
  {
    switch (paramType.typeCode)
    {
    case 1: 
      writeString(paramString);
      return;
    case 12: 
      writeVarString(paramString);
      return;
    }
    writeLongVarString(paramString);
  }
  
  protected void writeSmallint(Number paramNumber)
  {
    writeString(paramNumber.toString());
  }
  
  protected void writeInteger(Number paramNumber)
  {
    writeString(paramNumber.toString());
  }
  
  protected void writeBigint(Number paramNumber)
  {
    writeString(paramNumber.toString());
  }
  
  protected void writeReal(Double paramDouble)
  {
    writeString(paramDouble.toString());
  }
  
  protected void writeDecimal(BigDecimal paramBigDecimal, Type paramType)
  {
    writeString(paramType.convertToString(paramBigDecimal));
  }
  
  protected void writeBoolean(Boolean paramBoolean)
  {
    writeString(paramBoolean.toString());
  }
  
  protected void writeDate(TimestampData paramTimestampData, Type paramType)
  {
    writeString(paramType.convertToString(paramTimestampData));
  }
  
  protected void writeTime(TimeData paramTimeData, Type paramType)
  {
    writeString(paramType.convertToString(paramTimeData));
  }
  
  protected void writeTimestamp(TimestampData paramTimestampData, Type paramType)
  {
    writeString(paramType.convertToString(paramTimestampData));
  }
  
  protected void writeYearMonthInterval(IntervalMonthData paramIntervalMonthData, Type paramType)
  {
    writeBytes(paramType.convertToString(paramIntervalMonthData));
  }
  
  protected void writeDaySecondInterval(IntervalSecondData paramIntervalSecondData, Type paramType)
  {
    writeBytes(paramType.convertToString(paramIntervalSecondData));
  }
  
  protected void writeOther(JavaObjectData paramJavaObjectData)
  {
    byte[] arrayOfByte = paramJavaObjectData.getBytes();
    writeByteArray(arrayOfByte);
  }
  
  protected void writeBit(BinaryData paramBinaryData)
  {
    String str = StringConverter.byteArrayToBitString(paramBinaryData.getBytes(), (int)paramBinaryData.bitLength(null));
    writeString(str);
  }
  
  protected void writeBinary(BinaryData paramBinaryData)
  {
    writeByteArray(paramBinaryData.getBytes());
  }
  
  protected void writeClob(ClobData paramClobData, Type paramType)
  {
    writeString(Long.toString(paramClobData.getId()));
  }
  
  protected void writeBlob(BlobData paramBlobData, Type paramType)
  {
    writeString(Long.toString(paramBlobData.getId()));
  }
  
  protected void writeArray(Object[] paramArrayOfObject, Type paramType)
  {
    throw Error.runtimeError(201, "RowOutputText");
  }
  
  public int getSize(Row paramRow)
  {
    reset();
    try
    {
      writeSize(0);
      writeData(paramRow, paramRow.getTable().getColumnTypes());
      writeEnd();
    }
    catch (Exception localException)
    {
      reset();
    }
    int i = size();
    reset();
    return i;
  }
  
  public int getStorageSize(int paramInt)
  {
    return paramInt;
  }
  
  public RowOutputInterface duplicate()
  {
    throw Error.runtimeError(201, "RowOutputText");
  }
}


/* Location:              E:\java\java学习\hutubill\lib\all.jar!\org\hsqldb\rowio\RowOutputText.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */
package org.hsqldb.rowio;

import java.math.BigDecimal;
import org.hsqldb.Scanner;
import org.hsqldb.error.Error;
import org.hsqldb.map.ValuePool;
import org.hsqldb.persist.TextFileSettings;
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

public class RowInputText
  extends RowInputBase
  implements RowInputInterface
{
  protected TextFileSettings textFileSettings;
  private String fieldSep;
  private String varSep;
  private String longvarSep;
  private int fieldSepLen;
  private int varSepLen;
  private int longvarSepLen;
  private boolean fieldSepEnd;
  private boolean varSepEnd;
  private boolean longvarSepEnd;
  private int textLen;
  protected String text;
  protected long line;
  protected int field;
  protected int next = 0;
  protected Scanner scanner = new Scanner();
  private int maxPooledStringLength = ValuePool.getMaxStringLength();
  
  public RowInputText(TextFileSettings paramTextFileSettings)
  {
    super(new byte[0]);
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
    this.fieldSepLen = this.fieldSep.length();
    this.varSepLen = this.varSep.length();
    this.longvarSepLen = this.longvarSep.length();
  }
  
  public void setSource(String paramString, long paramLong, int paramInt)
  {
    this.size = paramInt;
    this.text = paramString;
    this.textLen = paramString.length();
    this.filePos = paramLong;
    this.next = 0;
    this.line += 1L;
    this.field = 0;
  }
  
  protected String getField(String paramString, int paramInt, boolean paramBoolean)
  {
    String str1 = null;
    try
    {
      int i = this.next;
      this.field += 1;
      if (paramBoolean)
      {
        if ((this.next >= this.textLen) && (paramInt > 0)) {
          throw Error.error(488);
        }
        if (this.text.endsWith(paramString)) {
          this.next = (this.textLen - paramInt);
        } else {
          throw Error.error(488);
        }
      }
      else
      {
        this.next = this.text.indexOf(paramString, i);
        if (this.next == -1) {
          this.next = this.textLen;
        }
      }
      if (i > this.next) {
        i = this.next;
      }
      str1 = this.text.substring(i, this.next);
      this.next += paramInt;
      int j = str1.trim().length();
      if (j == 0)
      {
        str1 = null;
      }
      else if (j < str1.length())
      {
        for (j = str1.length() - 1; str1.charAt(j) < ' '; j--) {}
        str1 = str1.substring(0, j + 1);
      }
    }
    catch (Exception localException)
    {
      String str2 = localException.toString();
      throw Error.error(localException, 41, str2);
    }
    return str1;
  }
  
  public String readString()
  {
    return getField(this.fieldSep, this.fieldSepLen, this.fieldSepEnd);
  }
  
  private String readVarString()
  {
    return getField(this.varSep, this.varSepLen, this.varSepEnd);
  }
  
  private String readLongVarString()
  {
    return getField(this.longvarSep, this.longvarSepLen, this.longvarSepEnd);
  }
  
  public char readChar()
  {
    throw Error.runtimeError(201, "RowInputText");
  }
  
  public byte readByte()
  {
    throw Error.runtimeError(201, "RowInputText");
  }
  
  public short readShort()
  {
    throw Error.runtimeError(201, "RowInputText");
  }
  
  public int readInt()
  {
    throw Error.runtimeError(201, "RowInputText");
  }
  
  public long readLong()
  {
    throw Error.runtimeError(201, "RowInputText");
  }
  
  public int readType()
  {
    return 0;
  }
  
  protected boolean readNull()
  {
    return false;
  }
  
  protected String readChar(Type paramType)
  {
    String str = null;
    switch (paramType.typeCode)
    {
    case 1: 
      str = readString();
      break;
    case 12: 
      str = readVarString();
      break;
    default: 
      str = readLongVarString();
    }
    if (str == null) {
      return null;
    }
    if (str.length() > this.maxPooledStringLength) {
      return str;
    }
    return ValuePool.getString(str);
  }
  
  protected Integer readSmallint()
  {
    String str = readString();
    if (str == null) {
      return null;
    }
    str = str.trim();
    if (str.length() == 0) {
      return null;
    }
    return ValuePool.getInt(Integer.parseInt(str));
  }
  
  protected Integer readInteger()
  {
    String str = readString();
    if (str == null) {
      return null;
    }
    str = str.trim();
    if (str.length() == 0) {
      return null;
    }
    return ValuePool.getInt(Integer.parseInt(str));
  }
  
  protected Long readBigint()
  {
    String str = readString();
    if (str == null) {
      return null;
    }
    str = str.trim();
    if (str.length() == 0) {
      return null;
    }
    return ValuePool.getLong(Long.parseLong(str));
  }
  
  protected Double readReal()
  {
    String str = readString();
    if (str == null) {
      return null;
    }
    str = str.trim();
    if (str.length() == 0) {
      return null;
    }
    return Double.valueOf(str);
  }
  
  protected BigDecimal readDecimal(Type paramType)
  {
    String str = readString();
    if (str == null) {
      return null;
    }
    str = str.trim();
    if (str.length() == 0) {
      return null;
    }
    return new BigDecimal(str);
  }
  
  protected TimeData readTime(Type paramType)
  {
    String str = readString();
    if (str == null) {
      return null;
    }
    str = str.trim();
    if (str.length() == 0) {
      return null;
    }
    return this.scanner.newTime(str);
  }
  
  protected TimestampData readDate(Type paramType)
  {
    String str = readString();
    if (str == null) {
      return null;
    }
    str = str.trim();
    if (str.length() == 0) {
      return null;
    }
    return this.scanner.newDate(str);
  }
  
  protected TimestampData readTimestamp(Type paramType)
  {
    String str = readString();
    if (str == null) {
      return null;
    }
    str = str.trim();
    if (str.length() == 0) {
      return null;
    }
    return this.scanner.newTimestamp(str);
  }
  
  protected IntervalMonthData readYearMonthInterval(Type paramType)
  {
    String str = readString();
    if (str == null) {
      return null;
    }
    str = str.trim();
    if (str.length() == 0) {
      return null;
    }
    return (IntervalMonthData)this.scanner.newInterval(str, (IntervalType)paramType);
  }
  
  protected IntervalSecondData readDaySecondInterval(Type paramType)
  {
    String str = readString();
    if (str == null) {
      return null;
    }
    str = str.trim();
    if (str.length() == 0) {
      return null;
    }
    return (IntervalSecondData)this.scanner.newInterval(str, (IntervalType)paramType);
  }
  
  protected Boolean readBoole()
  {
    String str = readString();
    if (str == null) {
      return null;
    }
    str = str.trim();
    if (str.length() == 0) {
      return null;
    }
    return str.equalsIgnoreCase("TRUE") ? Boolean.TRUE : Boolean.FALSE;
  }
  
  protected Object readOther()
  {
    String str = readString();
    if (str == null) {
      return null;
    }
    BinaryData localBinaryData = this.scanner.convertToBinary(str);
    if (localBinaryData.length(null) == 0L) {
      return null;
    }
    return new JavaObjectData(localBinaryData.getBytes());
  }
  
  protected BinaryData readBit()
  {
    String str = readString();
    if (str == null) {
      return null;
    }
    BinaryData localBinaryData = this.scanner.convertToBit(str);
    return localBinaryData;
  }
  
  protected BinaryData readBinary()
  {
    String str = readString();
    if (str == null) {
      return null;
    }
    BinaryData localBinaryData = this.scanner.convertToBinary(str);
    return localBinaryData;
  }
  
  protected ClobData readClob()
  {
    String str = readString();
    if (str == null) {
      return null;
    }
    str = str.trim();
    if (str.length() == 0) {
      return null;
    }
    long l = Long.parseLong(str);
    return new ClobDataID(l);
  }
  
  protected BlobData readBlob()
  {
    String str = readString();
    if (str == null) {
      return null;
    }
    str = str.trim();
    if (str.length() == 0) {
      return null;
    }
    long l = Long.parseLong(str);
    return new BlobDataID(l);
  }
  
  protected Object[] readArray(Type paramType)
  {
    throw Error.runtimeError(201, "RowInputText");
  }
  
  public long getLineNumber()
  {
    return this.line;
  }
  
  public void skippedLine()
  {
    this.line += 1L;
  }
  
  public void reset()
  {
    this.text = "";
    this.textLen = 0;
    this.filePos = 0L;
    this.next = 0;
    this.field = 0;
    this.line = 0L;
  }
}


/* Location:              E:\java\java学习\hutubill\lib\all.jar!\org\hsqldb\rowio\RowInputText.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */
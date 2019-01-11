package org.hsqldb.rowio;

import java.util.Calendar;
import java.util.GregorianCalendar;
import org.hsqldb.HsqlDateTime;
import org.hsqldb.types.TimeData;
import org.hsqldb.types.TimestampData;
import org.hsqldb.types.Type;

public class RowInputBinary180
  extends RowInputBinary
{
  Calendar tempCalDefault = new GregorianCalendar();
  
  public RowInputBinary180(byte[] paramArrayOfByte)
  {
    super(paramArrayOfByte);
  }
  
  protected TimeData readTime(Type paramType)
  {
    if (paramType.typeCode == 92)
    {
      long l = readLong();
      l = HsqlDateTime.convertMillisFromCalendar(this.tempCalDefault, l);
      l = HsqlDateTime.getNormalisedTime(l);
      return new TimeData((int)(l / 1000L), 0, 0);
    }
    return new TimeData(readInt(), readInt(), readInt());
  }
  
  protected TimestampData readDate(Type paramType)
  {
    long l = readLong();
    l = HsqlDateTime.convertMillisFromCalendar(this.tempCalDefault, l);
    l = HsqlDateTime.getNormalisedDate(l);
    return new TimestampData(l / 1000L);
  }
  
  protected TimestampData readTimestamp(Type paramType)
  {
    if (paramType.typeCode == 93)
    {
      long l = readLong();
      int i = readInt();
      l = HsqlDateTime.convertMillisFromCalendar(this.tempCalDefault, l);
      return new TimestampData(l / 1000L, i);
    }
    return new TimestampData(readLong(), readInt(), readInt());
  }
}


/* Location:              E:\java\java学习\hutubill\lib\all.jar!\org\hsqldb\rowio\RowInputBinary180.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */
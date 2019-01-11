package org.hsqldb.types;

import org.hsqldb.error.Error;
import org.hsqldb.lib.IntKeyIntValueHashMap;

public class IntervalSecondData
{
  public static final IntervalSecondData oneDay = newIntervalDay(1L, Type.SQL_INTERVAL_DAY);
  final long units;
  final int nanos;
  
  public static IntervalSecondData newInterval(double paramDouble, int paramInt)
  {
    int i = DTIType.intervalIndexMap.get(paramInt);
    paramDouble *= DTIType.yearToSecondFactors[i];
    return new IntervalSecondData(paramDouble, 0);
  }
  
  public static IntervalSecondData newIntervalDay(long paramLong, IntervalType paramIntervalType)
  {
    return new IntervalSecondData(paramLong * 24L * 60L * 60L, 0, paramIntervalType);
  }
  
  public static IntervalSecondData newIntervalHour(long paramLong, IntervalType paramIntervalType)
  {
    return new IntervalSecondData(paramLong * 60L * 60L, 0, paramIntervalType);
  }
  
  public static IntervalSecondData newIntervalMinute(long paramLong, IntervalType paramIntervalType)
  {
    return new IntervalSecondData(paramLong * 60L, 0, paramIntervalType);
  }
  
  public static IntervalSecondData newIntervalSeconds(long paramLong, IntervalType paramIntervalType)
  {
    return new IntervalSecondData(paramLong, 0, paramIntervalType);
  }
  
  public IntervalSecondData(long paramLong, int paramInt, IntervalType paramIntervalType)
  {
    if (paramLong >= paramIntervalType.getIntervalValueLimit()) {
      throw Error.error(3435);
    }
    this.units = paramLong;
    this.nanos = paramInt;
  }
  
  public IntervalSecondData(long paramLong, int paramInt)
  {
    this.units = paramLong;
    this.nanos = paramInt;
  }
  
  public IntervalSecondData(long paramLong1, long paramLong2, IntervalType paramIntervalType, boolean paramBoolean)
  {
    long l;
    if (paramLong2 >= 1000000000L)
    {
      l = paramLong2 / 1000000000L;
      paramLong2 %= 1000000000L;
      paramLong1 += l;
    }
    else if (paramLong2 <= -1000000000L)
    {
      l = -paramLong2 / 1000000000L;
      paramLong2 = -(-paramLong2 % 1000000000L);
      paramLong1 -= l;
    }
    int i = DTIType.nanoScaleFactors[paramIntervalType.scale];
    paramLong2 /= i;
    paramLong2 *= i;
    if ((paramLong1 > 0L) && (paramLong2 < 0L))
    {
      paramLong2 += 1000000000L;
      paramLong1 -= 1L;
    }
    else if ((paramLong1 < 0L) && (paramLong2 > 0L))
    {
      paramLong2 -= 1000000000L;
      paramLong1 += 1L;
    }
    i = DTIType.yearToSecondFactors[paramIntervalType.endPartIndex];
    paramLong1 /= i;
    paramLong1 *= i;
    if (paramLong1 >= paramIntervalType.getIntervalValueLimit()) {
      throw Error.error(3435);
    }
    this.units = paramLong1;
    this.nanos = ((int)paramLong2);
  }
  
  public boolean equals(Object paramObject)
  {
    if ((paramObject instanceof IntervalSecondData)) {
      return (this.units == ((IntervalSecondData)paramObject).units) && (this.nanos == ((IntervalSecondData)paramObject).nanos);
    }
    return false;
  }
  
  public int hashCode()
  {
    return (int)this.units ^ this.nanos;
  }
  
  public int compareTo(IntervalSecondData paramIntervalSecondData)
  {
    if (this.units > paramIntervalSecondData.units) {
      return 1;
    }
    if (this.units < paramIntervalSecondData.units) {
      return -1;
    }
    if (this.nanos > paramIntervalSecondData.nanos) {
      return 1;
    }
    if (this.nanos < paramIntervalSecondData.nanos) {
      return -1;
    }
    return 0;
  }
  
  public long getSeconds()
  {
    return this.units;
  }
  
  public int getNanos()
  {
    return this.nanos;
  }
  
  public String toString()
  {
    return Type.SQL_INTERVAL_SECOND_MAX_FRACTION_MAX_PRECISION.convertToString(this);
  }
}


/* Location:              E:\java\java学习\hutubill\lib\all.jar!\org\hsqldb\types\IntervalSecondData.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */
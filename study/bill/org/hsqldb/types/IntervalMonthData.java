package org.hsqldb.types;

import org.hsqldb.error.Error;
import org.hsqldb.lib.IntKeyIntValueHashMap;

public class IntervalMonthData
{
  public final int units;
  
  public static IntervalMonthData newInterval(double paramDouble, int paramInt)
  {
    int i = DTIType.intervalIndexMap.get(paramInt);
    paramDouble *= DTIType.yearToSecondFactors[i];
    return new IntervalMonthData(paramDouble);
  }
  
  public static IntervalMonthData newIntervalYear(long paramLong, IntervalType paramIntervalType)
  {
    return new IntervalMonthData(paramLong * 12L, paramIntervalType);
  }
  
  public static IntervalMonthData newIntervalMonth(long paramLong, IntervalType paramIntervalType)
  {
    return new IntervalMonthData(paramLong, paramIntervalType);
  }
  
  public IntervalMonthData(long paramLong, IntervalType paramIntervalType)
  {
    if (paramLong >= paramIntervalType.getIntervalValueLimit()) {
      throw Error.error(3406);
    }
    if (paramIntervalType.typeCode == 101) {
      paramLong -= paramLong % 12L;
    }
    this.units = ((int)paramLong);
  }
  
  public IntervalMonthData(long paramLong)
  {
    this.units = ((int)paramLong);
  }
  
  public boolean equals(Object paramObject)
  {
    if ((paramObject instanceof IntervalMonthData)) {
      return this.units == ((IntervalMonthData)paramObject).units;
    }
    return false;
  }
  
  public int hashCode()
  {
    return this.units;
  }
  
  public int compareTo(IntervalMonthData paramIntervalMonthData)
  {
    if (this.units > paramIntervalMonthData.units) {
      return 1;
    }
    if (this.units < paramIntervalMonthData.units) {
      return -1;
    }
    return 0;
  }
  
  public long getMonths()
  {
    return this.units;
  }
  
  public String toString()
  {
    return Type.SQL_INTERVAL_MONTH_MAX_PRECISION.convertToString(this);
  }
}


/* Location:              E:\java\java学习\hutubill\lib\all.jar!\org\hsqldb\types\IntervalMonthData.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */
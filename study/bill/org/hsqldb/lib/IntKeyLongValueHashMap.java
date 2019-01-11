package org.hsqldb.lib;

import java.util.NoSuchElementException;
import org.hsqldb.map.BaseHashMap;

public class IntKeyLongValueHashMap
  extends BaseHashMap
{
  public IntKeyLongValueHashMap()
  {
    this(8);
  }
  
  public IntKeyLongValueHashMap(int paramInt)
    throws IllegalArgumentException
  {
    super(paramInt, 1, 2, false);
  }
  
  public long get(int paramInt)
    throws NoSuchElementException
  {
    int i = getLookup(paramInt);
    if (i != -1) {
      return this.longValueTable[i];
    }
    throw new NoSuchElementException();
  }
  
  public long get(int paramInt, long paramLong)
  {
    int i = getLookup(paramInt);
    if (i != -1) {
      return this.longValueTable[i];
    }
    return paramLong;
  }
  
  public boolean get(int paramInt, long[] paramArrayOfLong)
  {
    int i = getLookup(paramInt);
    if (i != -1)
    {
      paramArrayOfLong[0] = this.longValueTable[i];
      return true;
    }
    return false;
  }
  
  public boolean put(int paramInt, long paramLong)
  {
    int i = size();
    super.addOrRemove(paramInt, paramLong, null, null, false);
    return i != size();
  }
  
  public boolean remove(int paramInt)
  {
    int i = size();
    super.addOrRemove(paramInt, 0L, null, null, true);
    return i != size();
  }
}


/* Location:              E:\java\java学习\hutubill\lib\all.jar!\org\hsqldb\lib\IntKeyLongValueHashMap.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */
package org.hsqldb.lib;

import java.util.NoSuchElementException;
import org.hsqldb.map.BaseHashMap;
import org.hsqldb.map.BaseHashMap.BaseHashIterator;

public class LongKeyLongValueHashMap
  extends BaseHashMap
{
  private Set keySet;
  private Collection values;
  
  public LongKeyLongValueHashMap()
  {
    this(8);
  }
  
  public LongKeyLongValueHashMap(boolean paramBoolean)
  {
    this(8);
    this.minimizeOnEmpty = paramBoolean;
  }
  
  public LongKeyLongValueHashMap(int paramInt)
    throws IllegalArgumentException
  {
    super(paramInt, 2, 2, false);
  }
  
  public long get(long paramLong)
    throws NoSuchElementException
  {
    int i = getLookup(paramLong);
    if (i != -1) {
      return this.longValueTable[i];
    }
    throw new NoSuchElementException();
  }
  
  public long get(long paramLong1, long paramLong2)
  {
    int i = getLookup(paramLong1);
    if (i != -1) {
      return this.longValueTable[i];
    }
    return paramLong2;
  }
  
  public boolean get(long paramLong, long[] paramArrayOfLong)
  {
    int i = getLookup(paramLong);
    if (i != -1)
    {
      paramArrayOfLong[0] = this.longValueTable[i];
      return true;
    }
    return false;
  }
  
  public boolean put(long paramLong1, long paramLong2)
  {
    int i = size();
    super.addOrRemove(paramLong1, paramLong2, null, null, false);
    return i != size();
  }
  
  public boolean remove(long paramLong)
  {
    int i = size();
    super.addOrRemove(paramLong, 0L, null, null, true);
    return i != size();
  }
  
  public Set keySet()
  {
    if (this.keySet == null) {
      this.keySet = new KeySet();
    }
    return this.keySet;
  }
  
  public Collection values()
  {
    if (this.values == null) {
      this.values = new Values();
    }
    return this.values;
  }
  
  class Values
    implements Collection
  {
    Values() {}
    
    public Iterator iterator()
    {
      LongKeyLongValueHashMap tmp8_5 = LongKeyLongValueHashMap.this;
      tmp8_5.getClass();
      return new BaseHashMap.BaseHashIterator(tmp8_5, false);
    }
    
    public int size()
    {
      return LongKeyLongValueHashMap.this.size();
    }
    
    public boolean contains(Object paramObject)
    {
      throw new UnsupportedOperationException();
    }
    
    public boolean add(Object paramObject)
    {
      throw new UnsupportedOperationException();
    }
    
    public boolean addAll(Collection paramCollection)
    {
      throw new UnsupportedOperationException();
    }
    
    public boolean remove(Object paramObject)
    {
      throw new UnsupportedOperationException();
    }
    
    public boolean isEmpty()
    {
      return size() == 0;
    }
    
    public void clear()
    {
      LongKeyLongValueHashMap.this.clear();
    }
  }
  
  class KeySet
    implements Set
  {
    KeySet() {}
    
    public Iterator iterator()
    {
      LongKeyLongValueHashMap tmp8_5 = LongKeyLongValueHashMap.this;
      tmp8_5.getClass();
      return new BaseHashMap.BaseHashIterator(tmp8_5, true);
    }
    
    public int size()
    {
      return LongKeyLongValueHashMap.this.size();
    }
    
    public boolean contains(Object paramObject)
    {
      throw new UnsupportedOperationException();
    }
    
    public Object get(Object paramObject)
    {
      throw new UnsupportedOperationException();
    }
    
    public boolean add(Object paramObject)
    {
      throw new UnsupportedOperationException();
    }
    
    public boolean addAll(Collection paramCollection)
    {
      throw new UnsupportedOperationException();
    }
    
    public boolean remove(Object paramObject)
    {
      throw new UnsupportedOperationException();
    }
    
    public boolean isEmpty()
    {
      return size() == 0;
    }
    
    public void clear()
    {
      LongKeyLongValueHashMap.this.clear();
    }
  }
}


/* Location:              E:\java\java学习\hutubill\lib\all.jar!\org\hsqldb\lib\LongKeyLongValueHashMap.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */
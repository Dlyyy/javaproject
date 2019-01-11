package org.hsqldb.lib;

import org.hsqldb.map.BaseHashMap;
import org.hsqldb.map.BaseHashMap.BaseHashIterator;

public class OrderedIntKeyHashMap
  extends BaseHashMap
{
  Set keySet;
  Collection values;
  
  public OrderedIntKeyHashMap()
  {
    this(8);
  }
  
  public OrderedIntKeyHashMap(int paramInt)
    throws IllegalArgumentException
  {
    super(paramInt, 1, 3, false);
    this.isList = true;
  }
  
  public int getKey(int paramInt1, int paramInt2)
  {
    if ((paramInt1 >= 0) && (paramInt1 < size())) {
      return this.intKeyTable[paramInt1];
    }
    return paramInt2;
  }
  
  public Object getValue(int paramInt)
  {
    if ((paramInt >= 0) && (paramInt < size())) {
      return this.objectValueTable[paramInt];
    }
    return null;
  }
  
  public Object get(int paramInt)
  {
    int i = getLookup(paramInt);
    if (i != -1) {
      return this.objectValueTable[i];
    }
    return null;
  }
  
  public Object put(int paramInt, Object paramObject)
  {
    return super.addOrRemove(paramInt, paramObject, null, false);
  }
  
  public boolean containsValue(Object paramObject)
  {
    return super.containsValue(paramObject);
  }
  
  public Object remove(int paramInt)
  {
    int i = getLookup(paramInt);
    if (i < 0) {
      return null;
    }
    Object localObject = super.addOrRemove(paramInt, null, null, true);
    removeRow(i);
    return localObject;
  }
  
  public Object removeKeyAndValue(int paramInt)
    throws IndexOutOfBoundsException
  {
    checkRange(paramInt);
    return remove(this.intKeyTable[paramInt]);
  }
  
  public boolean containsKey(int paramInt)
  {
    return super.containsKey(paramInt);
  }
  
  public void valuesToArray(Object[] paramArrayOfObject)
  {
    Iterator localIterator = values().iterator();
    for (int i = 0; localIterator.hasNext(); i++) {
      paramArrayOfObject[i] = localIterator.next();
    }
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
  
  private void checkRange(int paramInt)
  {
    if ((paramInt < 0) || (paramInt >= size())) {
      throw new IndexOutOfBoundsException();
    }
  }
  
  class Values
    implements Collection
  {
    Values() {}
    
    public Iterator iterator()
    {
      OrderedIntKeyHashMap tmp8_5 = OrderedIntKeyHashMap.this;
      tmp8_5.getClass();
      return new BaseHashMap.BaseHashIterator(tmp8_5, false);
    }
    
    public int size()
    {
      return OrderedIntKeyHashMap.this.size();
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
      OrderedIntKeyHashMap.this.clear();
    }
  }
  
  class KeySet
    implements Set
  {
    KeySet() {}
    
    public Iterator iterator()
    {
      OrderedIntKeyHashMap tmp8_5 = OrderedIntKeyHashMap.this;
      tmp8_5.getClass();
      return new BaseHashMap.BaseHashIterator(tmp8_5, true);
    }
    
    public int size()
    {
      return OrderedIntKeyHashMap.this.size();
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
      OrderedIntKeyHashMap.this.clear();
    }
  }
}


/* Location:              E:\java\java学习\hutubill\lib\all.jar!\org\hsqldb\lib\OrderedIntKeyHashMap.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */
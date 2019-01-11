package org.hsqldb.map;

import org.hsqldb.lib.HashMappedList;
import org.hsqldb.lib.HashSet;

public class ReusableObjectCache
{
  public ReusableObjectCache()
  {
    try
    {
      jbInit();
    }
    catch (Exception localException)
    {
      localException.printStackTrace();
    }
  }
  
  public static HashMappedList getHashMappedList()
  {
    return new HashMappedList();
  }
  
  public static void putHashMappedList(HashMappedList paramHashMappedList) {}
  
  public static HashSet getHashSet()
  {
    return new HashSet();
  }
  
  public static void putHashSet(HashSet paramHashSet) {}
  
  private void jbInit()
    throws Exception
  {}
}


/* Location:              E:\java\java学习\hutubill\lib\all.jar!\org\hsqldb\map\ReusableObjectCache.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */
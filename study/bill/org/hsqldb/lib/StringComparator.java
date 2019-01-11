package org.hsqldb.lib;

import java.io.Serializable;
import java.util.Comparator;

public class StringComparator
  implements Comparator, Serializable
{
  public int compare(Object paramObject1, Object paramObject2)
  {
    if (paramObject1 == paramObject2) {
      return 0;
    }
    if (paramObject1 == null) {
      return -1;
    }
    if (paramObject2 == null) {
      return 1;
    }
    return ((String)paramObject1).compareTo((String)paramObject2);
  }
}


/* Location:              E:\java\java学习\hutubill\lib\all.jar!\org\hsqldb\lib\StringComparator.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */
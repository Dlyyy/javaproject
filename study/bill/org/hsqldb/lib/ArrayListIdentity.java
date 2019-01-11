package org.hsqldb.lib;

public class ArrayListIdentity
  extends HsqlArrayList
  implements HsqlList
{
  public int indexOf(Object paramObject)
  {
    for (int i = 0; i < this.elementCount; i++) {
      if (this.elementData[i] == paramObject) {
        return i;
      }
    }
    return -1;
  }
}


/* Location:              E:\java\java学习\hutubill\lib\all.jar!\org\hsqldb\lib\ArrayListIdentity.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */
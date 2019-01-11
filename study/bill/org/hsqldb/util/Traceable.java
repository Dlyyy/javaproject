package org.hsqldb.util;

abstract interface Traceable
{
  public static final boolean TRACE = Boolean.getBoolean("hsqldb.util.trace");
  
  public abstract void trace(String paramString);
}


/* Location:              E:\java\java学习\hutubill\lib\all.jar!\org\hsqldb\util\Traceable.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */
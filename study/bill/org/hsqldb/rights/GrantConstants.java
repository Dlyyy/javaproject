package org.hsqldb.rights;

public abstract interface GrantConstants
{
  public static final int SELECT = 1;
  public static final int DELETE = 2;
  public static final int INSERT = 4;
  public static final int UPDATE = 8;
  public static final int USAGE = 16;
  public static final int EXECUTE = 32;
  public static final int REFERENCES = 64;
  public static final int TRIGGER = 128;
  public static final int ALL = 63;
}


/* Location:              E:\java\java学习\hutubill\lib\all.jar!\org\hsqldb\rights\GrantConstants.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */
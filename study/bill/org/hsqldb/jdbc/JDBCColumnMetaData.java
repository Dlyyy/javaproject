package org.hsqldb.jdbc;

import java.lang.reflect.Field;

public final class JDBCColumnMetaData
{
  public String catalogName;
  public String columnClassName;
  public int columnDisplaySize;
  public String columnLabel;
  public String columnName;
  public int columnType;
  public int precision;
  public int scale;
  public String schemaName;
  public String tableName;
  public boolean isAutoIncrement;
  public boolean isCaseSensitive;
  public boolean isCurrency;
  public boolean isDefinitelyWritable;
  public int isNullable;
  public boolean isReadOnly;
  public boolean isSearchable;
  public boolean isSigned;
  public boolean isWritable;
  
  public String toString()
  {
    try
    {
      return toStringImpl();
    }
    catch (Exception localException)
    {
      return super.toString() + "[" + localException + "]";
    }
  }
  
  private String toStringImpl()
    throws Exception
  {
    StringBuffer localStringBuffer = new StringBuffer();
    localStringBuffer.append('[');
    for (Field localField : getClass().getFields())
    {
      localStringBuffer.append(localField.getName());
      localStringBuffer.append('=');
      localStringBuffer.append(localField.get(this));
      if (??? + 1 < ???)
      {
        localStringBuffer.append(',');
        localStringBuffer.append(' ');
      }
    }
    localStringBuffer.append(']');
    return localStringBuffer.toString();
  }
}


/* Location:              E:\java\java学习\hutubill\lib\all.jar!\org\hsqldb\jdbc\JDBCColumnMetaData.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */
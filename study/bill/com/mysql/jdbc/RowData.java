package com.mysql.jdbc;

import java.sql.SQLException;

public abstract interface RowData
{
  public static final int RESULT_SET_SIZE_UNKNOWN = -1;
  
  public abstract void addRow(byte[][] paramArrayOfByte)
    throws SQLException;
  
  public abstract void afterLast()
    throws SQLException;
  
  public abstract void beforeFirst()
    throws SQLException;
  
  public abstract void beforeLast()
    throws SQLException;
  
  public abstract void close()
    throws SQLException;
  
  public abstract Object[] getAt(int paramInt)
    throws SQLException;
  
  public abstract int getCurrentRowNumber()
    throws SQLException;
  
  public abstract ResultSet getOwner();
  
  public abstract boolean hasNext()
    throws SQLException;
  
  public abstract boolean isAfterLast()
    throws SQLException;
  
  public abstract boolean isBeforeFirst()
    throws SQLException;
  
  public abstract boolean isDynamic()
    throws SQLException;
  
  public abstract boolean isEmpty()
    throws SQLException;
  
  public abstract boolean isFirst()
    throws SQLException;
  
  public abstract boolean isLast()
    throws SQLException;
  
  public abstract void moveRowRelative(int paramInt)
    throws SQLException;
  
  public abstract Object[] next()
    throws SQLException;
  
  public abstract void removeRow(int paramInt)
    throws SQLException;
  
  public abstract void setCurrentRow(int paramInt)
    throws SQLException;
  
  public abstract void setOwner(ResultSet paramResultSet);
  
  public abstract int size()
    throws SQLException;
  
  public abstract boolean wasEmpty();
}


/* Location:              E:\java\java学习\hutubill\lib\all.jar!\com\mysql\jdbc\RowData.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version:       0.7.1
 */
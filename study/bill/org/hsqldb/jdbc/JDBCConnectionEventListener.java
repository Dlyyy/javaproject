package org.hsqldb.jdbc;

import java.sql.SQLException;

public abstract interface JDBCConnectionEventListener
{
  public abstract void connectionClosed();
  
  public abstract void connectionErrorOccurred(SQLException paramSQLException);
}


/* Location:              E:\java\java学习\hutubill\lib\all.jar!\org\hsqldb\jdbc\JDBCConnectionEventListener.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */
package com.mysql.jdbc;

import java.sql.SQLException;

public abstract interface PingTarget
{
  public abstract void doPing()
    throws SQLException;
}


/* Location:              E:\java\java学习\hutubill\lib\all.jar!\com\mysql\jdbc\PingTarget.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version:       0.7.1
 */
package com.mysql.jdbc;

import java.sql.SQLException;
import java.util.Properties;

public abstract interface ConnectionPropertiesTransform
{
  public abstract Properties transformProperties(Properties paramProperties)
    throws SQLException;
}


/* Location:              E:\java\java学习\hutubill\lib\all.jar!\com\mysql\jdbc\ConnectionPropertiesTransform.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version:       0.7.1
 */
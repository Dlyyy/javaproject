package org.hsqldb.auth;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashSet;
import java.util.Set;
import org.hsqldb.lib.FrameworkLogger;

public class AuthUtils
{
  private static FrameworkLogger logger = FrameworkLogger.getLog(AuthUtils.class);
  
  static String getInitialSchema(Connection paramConnection)
    throws SQLException
  {
    Statement localStatement = paramConnection.createStatement();
    ResultSet localResultSet = null;
    try
    {
      localResultSet = localStatement.executeQuery("SELECT initial_schema FROM information_schema.system_users\nWHERE user_name = current_user");
      if (!localResultSet.next()) {
        throw new IllegalStateException("Failed to retrieve initial_schema for current user");
      }
      String str = localResultSet.getString(1);
      return str;
    }
    finally
    {
      if (localResultSet != null) {
        try
        {
          localResultSet.close();
        }
        catch (SQLException localSQLException3)
        {
          logger.error("Failed to close ResultSet for retrieving initial schema");
        }
      }
      localResultSet = null;
      try
      {
        localStatement.close();
      }
      catch (SQLException localSQLException4)
      {
        logger.error("Failed to close Statement for retrieving db name");
      }
      localStatement = null;
    }
  }
  
  static Set getEnabledRoles(Connection paramConnection)
    throws SQLException
  {
    HashSet localHashSet = new HashSet();
    Statement localStatement = paramConnection.createStatement();
    ResultSet localResultSet = null;
    try
    {
      localResultSet = localStatement.executeQuery("SELECT * FROM information_schema.enabled_roles");
      while (localResultSet.next()) {
        localHashSet.add(localResultSet.getString(1));
      }
    }
    finally
    {
      if (localResultSet != null) {
        try
        {
          localResultSet.close();
        }
        catch (SQLException localSQLException3)
        {
          logger.error("Failed to close ResultSet for retrieving db name");
        }
      }
      localResultSet = null;
      try
      {
        localStatement.close();
      }
      catch (SQLException localSQLException4)
      {
        logger.error("Failed to close Statement for retrieving db name");
      }
      localStatement = null;
    }
    return localHashSet;
  }
}


/* Location:              E:\java\java学习\hutubill\lib\all.jar!\org\hsqldb\auth\AuthUtils.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */
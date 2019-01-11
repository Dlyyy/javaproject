package org.hsqldb.jdbc;

import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.DriverPropertyInfo;
import java.sql.SQLException;
import java.sql.SQLFeatureNotSupportedException;
import java.util.Properties;
import java.util.logging.Logger;
import org.hsqldb.DatabaseURL;
import org.hsqldb.persist.HsqlProperties;

public class JDBCDriver
  implements Driver
{
  public static final JDBCDriver driverInstance = new JDBCDriver();
  public final ThreadLocal<JDBCConnection> threadConnection = new ThreadLocal();
  
  public Connection connect(String paramString, Properties paramProperties)
    throws SQLException
  {
    if (paramString.regionMatches(true, 0, "jdbc:default:connection", 0, "jdbc:default:connection".length()))
    {
      JDBCConnection localJDBCConnection = (JDBCConnection)this.threadConnection.get();
      if (localJDBCConnection == null) {
        return null;
      }
      return localJDBCConnection;
    }
    return getConnection(paramString, paramProperties);
  }
  
  public static Connection getConnection(String paramString, Properties paramProperties)
    throws SQLException
  {
    final HsqlProperties localHsqlProperties = DatabaseURL.parseURL(paramString, true, false);
    if (localHsqlProperties == null) {
      throw JDBCUtil.invalidArgument();
    }
    if (localHsqlProperties.isEmpty()) {
      return null;
    }
    long l = 0L;
    if (paramProperties != null) {
      l = HsqlProperties.getIntegerProperty(paramProperties, "loginTimeout", 0);
    }
    localHsqlProperties.addProperties(paramProperties);
    if (l == 0L) {
      l = DriverManager.getLoginTimeout();
    }
    if (l == 0L) {
      return new JDBCConnection(localHsqlProperties);
    }
    String str = localHsqlProperties.getProperty("connection_type");
    if (DatabaseURL.isInProcessDatabaseType(str)) {
      return new JDBCConnection(localHsqlProperties);
    }
    JDBCConnection[] arrayOfJDBCConnection = new JDBCConnection[1];
    final SQLException[] arrayOfSQLException = new SQLException[1];
    Thread local1 = new Thread()
    {
      public void run()
      {
        try
        {
          this.val$conn[0] = new JDBCConnection(localHsqlProperties);
        }
        catch (SQLException localSQLException)
        {
          arrayOfSQLException[0] = localSQLException;
        }
      }
    };
    local1.start();
    try
    {
      local1.join(1000L * l);
    }
    catch (InterruptedException localInterruptedException) {}
    try
    {
      local1.stop();
      try
      {
        local1.setContextClassLoader(null);
      }
      catch (Throwable localThrowable1) {}
      if (arrayOfSQLException[0] == null) {
        break label204;
      }
    }
    catch (Exception localException) {}finally
    {
      try
      {
        local1.setContextClassLoader(null);
      }
      catch (Throwable localThrowable3) {}
    }
    throw arrayOfSQLException[0];
    label204:
    if (arrayOfJDBCConnection[0] != null) {
      return arrayOfJDBCConnection[0];
    }
    throw JDBCUtil.sqlException(1351);
  }
  
  public boolean acceptsURL(String paramString)
  {
    if (paramString == null) {
      return false;
    }
    if (paramString.regionMatches(true, 0, "jdbc:hsqldb:", 0, "jdbc:hsqldb:".length())) {
      return true;
    }
    return paramString.regionMatches(true, 0, "jdbc:default:connection", 0, "jdbc:default:connection".length());
  }
  
  public DriverPropertyInfo[] getPropertyInfo(String paramString, Properties paramProperties)
  {
    if (!acceptsURL(paramString)) {
      return new DriverPropertyInfo[0];
    }
    String[] arrayOfString = { "true", "false" };
    DriverPropertyInfo[] arrayOfDriverPropertyInfo = new DriverPropertyInfo[6];
    if (paramProperties == null) {
      paramProperties = new Properties();
    }
    DriverPropertyInfo localDriverPropertyInfo = new DriverPropertyInfo("user", null);
    localDriverPropertyInfo.value = paramProperties.getProperty("user");
    localDriverPropertyInfo.required = true;
    arrayOfDriverPropertyInfo[0] = localDriverPropertyInfo;
    localDriverPropertyInfo = new DriverPropertyInfo("password", null);
    localDriverPropertyInfo.value = paramProperties.getProperty("password");
    localDriverPropertyInfo.required = true;
    arrayOfDriverPropertyInfo[1] = localDriverPropertyInfo;
    localDriverPropertyInfo = new DriverPropertyInfo("get_column_name", null);
    localDriverPropertyInfo.value = paramProperties.getProperty("get_column_name", "true");
    localDriverPropertyInfo.required = false;
    localDriverPropertyInfo.choices = arrayOfString;
    arrayOfDriverPropertyInfo[2] = localDriverPropertyInfo;
    localDriverPropertyInfo = new DriverPropertyInfo("ifexists", null);
    localDriverPropertyInfo.value = paramProperties.getProperty("ifexists", "false");
    localDriverPropertyInfo.required = false;
    localDriverPropertyInfo.choices = arrayOfString;
    arrayOfDriverPropertyInfo[3] = localDriverPropertyInfo;
    localDriverPropertyInfo = new DriverPropertyInfo("default_schema", null);
    localDriverPropertyInfo.value = paramProperties.getProperty("default_schema", "false");
    localDriverPropertyInfo.required = false;
    localDriverPropertyInfo.choices = arrayOfString;
    arrayOfDriverPropertyInfo[4] = localDriverPropertyInfo;
    localDriverPropertyInfo = new DriverPropertyInfo("shutdown", null);
    localDriverPropertyInfo.value = paramProperties.getProperty("shutdown", "false");
    localDriverPropertyInfo.required = false;
    localDriverPropertyInfo.choices = arrayOfString;
    arrayOfDriverPropertyInfo[5] = localDriverPropertyInfo;
    return arrayOfDriverPropertyInfo;
  }
  
  public int getMajorVersion()
  {
    return 2;
  }
  
  public int getMinorVersion()
  {
    return 3;
  }
  
  public boolean jdbcCompliant()
  {
    return true;
  }
  
  public Logger getParentLogger()
    throws SQLFeatureNotSupportedException
  {
    throw ((SQLFeatureNotSupportedException)JDBCUtil.notSupported());
  }
  
  static
  {
    try
    {
      DriverManager.registerDriver(driverInstance);
    }
    catch (Exception localException) {}
  }
}


/* Location:              E:\java\java学习\hutubill\lib\all.jar!\org\hsqldb\jdbc\JDBCDriver.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */
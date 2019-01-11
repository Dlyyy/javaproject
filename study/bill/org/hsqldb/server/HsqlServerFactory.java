package org.hsqldb.server;

import java.sql.SQLException;
import org.hsqldb.HsqlException;
import org.hsqldb.error.Error;
import org.hsqldb.jdbc.JDBCUtil;

public class HsqlServerFactory
{
  public static HsqlSocketRequestHandler createHsqlServer(String paramString, boolean paramBoolean1, boolean paramBoolean2)
    throws SQLException
  {
    ServerProperties localServerProperties = new ServerProperties(1);
    localServerProperties.setProperty("server.dbname.0", "");
    localServerProperties.setProperty("server.database.0", paramString);
    localServerProperties.setProperty("server.trace", paramBoolean1);
    localServerProperties.setProperty("server.silent", paramBoolean2);
    Server localServer = new Server();
    try
    {
      localServer.setProperties(localServerProperties);
    }
    catch (Exception localException)
    {
      throw new SQLException("Failed to set server properties: " + localException);
    }
    if (!localServer.openDatabases())
    {
      Throwable localThrowable = localServer.getServerError();
      if ((localThrowable instanceof HsqlException)) {
        throw JDBCUtil.sqlException((HsqlException)localThrowable);
      }
      throw JDBCUtil.sqlException(Error.error(458));
    }
    localServer.setState(1);
    return localServer;
  }
}


/* Location:              E:\java\java学习\hutubill\lib\all.jar!\org\hsqldb\server\HsqlServerFactory.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */
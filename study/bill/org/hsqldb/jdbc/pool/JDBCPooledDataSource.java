package org.hsqldb.jdbc.pool;

import java.io.Serializable;
import java.sql.SQLException;
import java.util.Properties;
import javax.naming.NamingException;
import javax.naming.Reference;
import javax.naming.Referenceable;
import javax.naming.StringRefAddr;
import javax.sql.CommonDataSource;
import javax.sql.ConnectionPoolDataSource;
import javax.sql.PooledConnection;
import org.hsqldb.jdbc.JDBCCommonDataSource;
import org.hsqldb.jdbc.JDBCConnection;
import org.hsqldb.jdbc.JDBCDriver;

public class JDBCPooledDataSource
  extends JDBCCommonDataSource
  implements ConnectionPoolDataSource, Serializable, Referenceable, CommonDataSource
{
  public PooledConnection getPooledConnection()
    throws SQLException
  {
    JDBCConnection localJDBCConnection = (JDBCConnection)JDBCDriver.getConnection(this.url, this.connectionProps);
    return new JDBCPooledConnection(localJDBCConnection);
  }
  
  public PooledConnection getPooledConnection(String paramString1, String paramString2)
    throws SQLException
  {
    Properties localProperties = new Properties();
    localProperties.setProperty("user", paramString1);
    localProperties.setProperty("password", paramString2);
    JDBCConnection localJDBCConnection = (JDBCConnection)JDBCDriver.getConnection(this.url, localProperties);
    return new JDBCPooledConnection(localJDBCConnection);
  }
  
  public Reference getReference()
    throws NamingException
  {
    String str = "org.hsqldb.jdbc.JDBCDataSourceFactory";
    Reference localReference = new Reference(getClass().getName(), str, null);
    localReference.add(new StringRefAddr("database", getDatabase()));
    localReference.add(new StringRefAddr("user", getUser()));
    localReference.add(new StringRefAddr("password", this.password));
    localReference.add(new StringRefAddr("loginTimeout", Integer.toString(this.loginTimeout)));
    return localReference;
  }
}


/* Location:              E:\java\java学习\hutubill\lib\all.jar!\org\hsqldb\jdbc\pool\JDBCPooledDataSource.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */
package org.hsqldb.jdbc.pool;

import java.sql.Connection;
import java.sql.SQLException;
import javax.sql.XAConnection;
import javax.transaction.xa.XAResource;
import org.hsqldb.jdbc.JDBCConnection;

public class JDBCXAConnection
  extends JDBCPooledConnection
  implements XAConnection
{
  JDBCXAResource xaResource;
  
  public JDBCXAConnection(JDBCXADataSource paramJDBCXADataSource, JDBCConnection paramJDBCConnection)
  {
    super(paramJDBCConnection);
    this.xaResource = new JDBCXAResource(paramJDBCXADataSource, paramJDBCConnection);
  }
  
  public XAResource getXAResource()
    throws SQLException
  {
    return this.xaResource;
  }
  
  public synchronized Connection getConnection()
    throws SQLException
  {
    if (this.isInUse) {
      throw new SQLException("Connection in use");
    }
    this.isInUse = true;
    if (this.connection == null) {
      throw new SQLException("Connection in closed");
    }
    return new JDBCXAConnectionWrapper(this.xaResource, this, this.connection);
  }
  
  public void close()
    throws SQLException
  {
    super.close();
  }
}


/* Location:              E:\java\java学习\hutubill\lib\all.jar!\org\hsqldb\jdbc\pool\JDBCXAConnection.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */
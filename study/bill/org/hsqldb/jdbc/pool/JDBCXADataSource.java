package org.hsqldb.jdbc.pool;

import java.io.Serializable;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock.ReadLock;
import java.util.concurrent.locks.ReentrantReadWriteLock.WriteLock;
import javax.naming.NamingException;
import javax.naming.Reference;
import javax.naming.Referenceable;
import javax.naming.StringRefAddr;
import javax.sql.CommonDataSource;
import javax.sql.XAConnection;
import javax.sql.XADataSource;
import javax.transaction.xa.Xid;
import org.hsqldb.error.Error;
import org.hsqldb.jdbc.JDBCCommonDataSource;
import org.hsqldb.jdbc.JDBCConnection;
import org.hsqldb.jdbc.JDBCDriver;
import org.hsqldb.jdbc.JDBCUtil;

public class JDBCXADataSource
  extends JDBCCommonDataSource
  implements XADataSource, Serializable, Referenceable, CommonDataSource
{
  private HashMap resources = new HashMap();
  private ReentrantReadWriteLock lock = new ReentrantReadWriteLock(true);
  
  public XAConnection getXAConnection()
    throws SQLException
  {
    JDBCConnection localJDBCConnection = (JDBCConnection)JDBCDriver.getConnection(this.url, this.connectionProps);
    JDBCXAConnection localJDBCXAConnection = new JDBCXAConnection(this, localJDBCConnection);
    return localJDBCXAConnection;
  }
  
  public XAConnection getXAConnection(String paramString1, String paramString2)
    throws SQLException
  {
    if ((paramString1 == null) || (paramString2 == null)) {
      throw JDBCUtil.nullArgument();
    }
    if ((paramString1.equals(this.user)) && (paramString2.equals(this.password))) {
      return getXAConnection();
    }
    throw JDBCUtil.sqlException(Error.error(4000));
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
  
  public void addResource(Xid paramXid, JDBCXAResource paramJDBCXAResource)
  {
    this.lock.writeLock().lock();
    try
    {
      this.resources.put(paramXid, paramJDBCXAResource);
    }
    finally
    {
      this.lock.writeLock().unlock();
    }
  }
  
  public JDBCXADataSource()
    throws SQLException
  {}
  
  public JDBCXAResource removeResource(Xid paramXid)
  {
    this.lock.writeLock().lock();
    try
    {
      JDBCXAResource localJDBCXAResource = (JDBCXAResource)this.resources.remove(paramXid);
      return localJDBCXAResource;
    }
    finally
    {
      this.lock.writeLock().unlock();
    }
  }
  
  Xid[] getPreparedXids()
  {
    this.lock.writeLock().lock();
    try
    {
      Iterator localIterator = this.resources.keySet().iterator();
      HashSet localHashSet = new HashSet();
      while (localIterator.hasNext())
      {
        Xid localXid = (Xid)localIterator.next();
        if (((JDBCXAResource)this.resources.get(localXid)).state == JDBCXAResource.XA_STATE_PREPARED) {
          localHashSet.add(localXid);
        }
      }
      Xid[] arrayOfXid1 = new Xid[localHashSet.size()];
      localHashSet.toArray(arrayOfXid1);
      Xid[] arrayOfXid2 = arrayOfXid1;
      return arrayOfXid2;
    }
    finally
    {
      this.lock.writeLock().unlock();
    }
  }
  
  JDBCXAResource getResource(Xid paramXid)
  {
    this.lock.readLock().lock();
    try
    {
      JDBCXAResource localJDBCXAResource = (JDBCXAResource)this.resources.get(paramXid);
      return localJDBCXAResource;
    }
    finally
    {
      this.lock.readLock().unlock();
    }
  }
}


/* Location:              E:\java\java学习\hutubill\lib\all.jar!\org\hsqldb\jdbc\pool\JDBCXADataSource.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */
package org.hsqldb.jdbc;

import java.io.InputStream;
import java.io.OutputStream;
import java.sql.Blob;
import java.sql.SQLException;
import org.hsqldb.HsqlException;
import org.hsqldb.SessionInterface;
import org.hsqldb.types.BlobData;
import org.hsqldb.types.BlobDataID;
import org.hsqldb.types.BlobInputStream;

public class JDBCBlobClient
  implements Blob
{
  BlobDataID originalBlob;
  BlobDataID blob;
  SessionInterface session;
  int colIndex;
  private boolean isClosed;
  private boolean isWritable;
  JDBCResultSet resultSet;
  
  public synchronized long length()
    throws SQLException
  {
    try
    {
      return this.blob.length(this.session);
    }
    catch (HsqlException localHsqlException)
    {
      throw JDBCUtil.sqlException(localHsqlException);
    }
  }
  
  public synchronized byte[] getBytes(long paramLong, int paramInt)
    throws SQLException
  {
    if (!isInLimits(Long.MAX_VALUE, paramLong - 1L, paramInt)) {
      throw JDBCUtil.outOfRangeArgument();
    }
    try
    {
      return this.blob.getBytes(this.session, paramLong - 1L, paramInt);
    }
    catch (HsqlException localHsqlException)
    {
      throw JDBCUtil.sqlException(localHsqlException);
    }
  }
  
  public synchronized InputStream getBinaryStream()
    throws SQLException
  {
    return new BlobInputStream(this.session, this.blob, 0L, length());
  }
  
  public synchronized long position(byte[] paramArrayOfByte, long paramLong)
    throws SQLException
  {
    if (!isInLimits(Long.MAX_VALUE, paramLong - 1L, 0L)) {
      throw JDBCUtil.outOfRangeArgument();
    }
    try
    {
      long l = this.blob.position(this.session, paramArrayOfByte, paramLong - 1L);
      if (l >= 0L) {
        l += 1L;
      }
      return l;
    }
    catch (HsqlException localHsqlException)
    {
      throw JDBCUtil.sqlException(localHsqlException);
    }
  }
  
  public synchronized long position(Blob paramBlob, long paramLong)
    throws SQLException
  {
    if (!isInLimits(Long.MAX_VALUE, paramLong - 1L, 0L)) {
      throw JDBCUtil.outOfRangeArgument();
    }
    if ((paramBlob instanceof JDBCBlobClient))
    {
      localObject = ((JDBCBlobClient)paramBlob).blob;
      try
      {
        long l = this.blob.position(this.session, (BlobData)localObject, paramLong - 1L);
        if (l >= 0L) {
          l += 1L;
        }
        return l;
      }
      catch (HsqlException localHsqlException)
      {
        throw JDBCUtil.sqlException(localHsqlException);
      }
    }
    if (!isInLimits(2147483647L, 0L, paramBlob.length())) {
      throw JDBCUtil.outOfRangeArgument();
    }
    Object localObject = paramBlob.getBytes(1L, (int)paramBlob.length());
    return position((byte[])localObject, paramLong);
  }
  
  public synchronized int setBytes(long paramLong, byte[] paramArrayOfByte)
    throws SQLException
  {
    return setBytes(paramLong, paramArrayOfByte, 0, paramArrayOfByte.length);
  }
  
  public synchronized int setBytes(long paramLong, byte[] paramArrayOfByte, int paramInt1, int paramInt2)
    throws SQLException
  {
    if (!isInLimits(paramArrayOfByte.length, paramInt1, paramInt2)) {
      throw JDBCUtil.outOfRangeArgument();
    }
    if (!isInLimits(Long.MAX_VALUE, paramLong - 1L, paramInt2)) {
      throw JDBCUtil.outOfRangeArgument();
    }
    if (!this.isWritable) {
      throw JDBCUtil.notUpdatableColumn();
    }
    startUpdate();
    try
    {
      this.blob.setBytes(this.session, paramLong - 1L, paramArrayOfByte, paramInt1, paramInt2);
      return paramInt2;
    }
    catch (HsqlException localHsqlException)
    {
      throw JDBCUtil.sqlException(localHsqlException);
    }
  }
  
  public synchronized OutputStream setBinaryStream(long paramLong)
    throws SQLException
  {
    throw JDBCUtil.notSupported();
  }
  
  public synchronized void truncate(long paramLong)
    throws SQLException
  {
    try
    {
      this.blob.truncate(this.session, paramLong);
    }
    catch (HsqlException localHsqlException)
    {
      throw JDBCUtil.sqlException(localHsqlException);
    }
  }
  
  public synchronized void free()
    throws SQLException
  {
    this.isClosed = true;
  }
  
  public synchronized InputStream getBinaryStream(long paramLong1, long paramLong2)
    throws SQLException
  {
    if (!isInLimits(Long.MAX_VALUE, paramLong1 - 1L, paramLong2)) {
      throw JDBCUtil.outOfRangeArgument();
    }
    return new BlobInputStream(this.session, this.blob, paramLong1 - 1L, paramLong2);
  }
  
  public JDBCBlobClient(SessionInterface paramSessionInterface, BlobDataID paramBlobDataID)
  {
    this.session = paramSessionInterface;
    this.blob = paramBlobDataID;
  }
  
  public boolean isClosed()
  {
    return this.isClosed;
  }
  
  public BlobDataID getBlob()
  {
    return this.blob;
  }
  
  public synchronized void setWritable(JDBCResultSet paramJDBCResultSet, int paramInt)
  {
    this.isWritable = true;
    this.resultSet = paramJDBCResultSet;
    this.colIndex = paramInt;
  }
  
  public synchronized void clearUpdates()
  {
    if (this.originalBlob != null)
    {
      this.blob = this.originalBlob;
      this.originalBlob = null;
    }
  }
  
  private void startUpdate()
    throws SQLException
  {
    if (this.originalBlob != null) {
      return;
    }
    this.originalBlob = this.blob;
    this.blob = ((BlobDataID)this.blob.duplicate(this.session));
    this.resultSet.startUpdate(this.colIndex + 1);
    this.resultSet.preparedStatement.parameterValues[this.colIndex] = this.blob;
    this.resultSet.preparedStatement.parameterSet[this.colIndex] = Boolean.TRUE;
  }
  
  private void checkClosed()
    throws SQLException
  {
    if (this.isClosed) {
      throw JDBCUtil.sqlException(1251);
    }
  }
  
  static boolean isInLimits(long paramLong1, long paramLong2, long paramLong3)
  {
    return (paramLong2 >= 0L) && (paramLong3 >= 0L) && (paramLong2 + paramLong3 <= paramLong1);
  }
}


/* Location:              E:\java\java学习\hutubill\lib\all.jar!\org\hsqldb\jdbc\JDBCBlobClient.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */
package org.hsqldb.types;

import java.io.InputStream;
import org.hsqldb.SessionInterface;

public abstract interface BlobData
  extends LobData
{
  public abstract byte[] getBytes();
  
  public abstract byte[] getBytes(SessionInterface paramSessionInterface, long paramLong, int paramInt);
  
  public abstract BlobData getBlob(SessionInterface paramSessionInterface, long paramLong1, long paramLong2);
  
  public abstract InputStream getBinaryStream(SessionInterface paramSessionInterface);
  
  public abstract InputStream getBinaryStream(SessionInterface paramSessionInterface, long paramLong1, long paramLong2);
  
  public abstract long length(SessionInterface paramSessionInterface);
  
  public abstract long bitLength(SessionInterface paramSessionInterface);
  
  public abstract boolean isBits();
  
  public abstract void setBytes(SessionInterface paramSessionInterface, long paramLong, byte[] paramArrayOfByte, int paramInt1, int paramInt2);
  
  public abstract void setBytes(SessionInterface paramSessionInterface, long paramLong, byte[] paramArrayOfByte);
  
  public abstract void setBytes(SessionInterface paramSessionInterface, long paramLong1, BlobData paramBlobData, long paramLong2, long paramLong3);
  
  public abstract void setBinaryStream(SessionInterface paramSessionInterface, long paramLong, InputStream paramInputStream);
  
  public abstract void truncate(SessionInterface paramSessionInterface, long paramLong);
  
  public abstract BlobData duplicate(SessionInterface paramSessionInterface);
  
  public abstract long position(SessionInterface paramSessionInterface, byte[] paramArrayOfByte, long paramLong);
  
  public abstract long position(SessionInterface paramSessionInterface, BlobData paramBlobData, long paramLong);
  
  public abstract long nonZeroLength(SessionInterface paramSessionInterface);
  
  public abstract long getId();
  
  public abstract void setId(long paramLong);
  
  public abstract void free();
  
  public abstract boolean isClosed();
  
  public abstract void setSession(SessionInterface paramSessionInterface);
  
  public abstract int getStreamBlockSize();
}


/* Location:              E:\java\java学习\hutubill\lib\all.jar!\org\hsqldb\types\BlobData.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */
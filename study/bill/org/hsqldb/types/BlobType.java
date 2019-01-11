package org.hsqldb.types;

import java.sql.Blob;
import org.hsqldb.Database;
import org.hsqldb.Scanner;
import org.hsqldb.Session;
import org.hsqldb.SessionInterface;
import org.hsqldb.error.Error;
import org.hsqldb.jdbc.JDBCBlobClient;
import org.hsqldb.persist.LobManager;

public final class BlobType
  extends BinaryType
{
  public static final long maxBlobPrecision = 1099511627776L;
  public static final int defaultBlobSize = 1073741824;
  public static final int defaultShortBlobSize = 16777216;
  
  public BlobType(long paramLong)
  {
    super(30, paramLong);
  }
  
  public int displaySize()
  {
    return this.precision > 2147483647L ? Integer.MAX_VALUE : (int)this.precision;
  }
  
  public int getJDBCTypeCode()
  {
    return 2004;
  }
  
  public Class getJDBCClass()
  {
    return Blob.class;
  }
  
  public String getJDBCClassName()
  {
    return "java.sql.Blob";
  }
  
  public String getNameString()
  {
    return "BLOB";
  }
  
  public String getFullNameString()
  {
    return "BINARY LARGE OBJECT";
  }
  
  public String getDefinition()
  {
    long l = this.precision;
    String str = null;
    if (this.precision % 1073741824L == 0L)
    {
      l = this.precision / 1073741824L;
      str = "G";
    }
    else if (this.precision % 1048576L == 0L)
    {
      l = this.precision / 1048576L;
      str = "M";
    }
    else if (this.precision % 1024L == 0L)
    {
      l = this.precision / 1024L;
      str = "K";
    }
    StringBuffer localStringBuffer = new StringBuffer(16);
    localStringBuffer.append(getNameString());
    localStringBuffer.append('(');
    localStringBuffer.append(l);
    if (str != null) {
      localStringBuffer.append(str);
    }
    localStringBuffer.append(')');
    return localStringBuffer.toString();
  }
  
  public boolean isBinaryType()
  {
    return true;
  }
  
  public boolean isLobType()
  {
    return true;
  }
  
  public boolean acceptsPrecision()
  {
    return true;
  }
  
  public long getMaxPrecision()
  {
    return 1099511627776L;
  }
  
  public boolean requiresPrecision()
  {
    return false;
  }
  
  public int compare(Session paramSession, Object paramObject1, Object paramObject2)
  {
    if (paramObject1 == paramObject2) {
      return 0;
    }
    if (paramObject1 == null) {
      return -1;
    }
    if (paramObject2 == null) {
      return 1;
    }
    if ((paramObject2 instanceof BinaryData)) {
      return paramSession.database.lobManager.compare((BlobData)paramObject1, ((BlobData)paramObject2).getBytes());
    }
    return paramSession.database.lobManager.compare((BlobData)paramObject1, (BlobData)paramObject2);
  }
  
  public Object convertToTypeLimits(SessionInterface paramSessionInterface, Object paramObject)
  {
    return paramObject;
  }
  
  public Object castToType(SessionInterface paramSessionInterface, Object paramObject, Type paramType)
  {
    if (paramObject == null) {
      return null;
    }
    BlobData localBlobData;
    long l;
    if (paramType.typeCode == 30)
    {
      localBlobData = (BlobData)paramObject;
      l = localBlobData.length(paramSessionInterface);
      if (l > this.precision)
      {
        l = this.precision;
        paramSessionInterface.addWarning(Error.error(1004));
        localBlobData = localBlobData.getBlob(paramSessionInterface, 0L, l);
        return localBlobData;
      }
      return paramObject;
    }
    if ((paramType.typeCode == 60) || (paramType.typeCode == 61))
    {
      localBlobData = (BlobData)paramObject;
      l = localBlobData.length(paramSessionInterface);
      if (l > this.precision)
      {
        l = this.precision;
        paramSessionInterface.addWarning(Error.error(1004));
      }
      BlobDataID localBlobDataID = paramSessionInterface.createBlob(localBlobData.length(paramSessionInterface));
      localBlobDataID.setBytes(paramSessionInterface, 0L, localBlobData.getBytes(), 0, (int)l);
      return localBlobDataID;
    }
    throw Error.error(5561);
  }
  
  public Object convertToType(SessionInterface paramSessionInterface, Object paramObject, Type paramType)
  {
    BlobData localBlobData = null;
    if (paramObject == null) {
      return null;
    }
    long l;
    if (paramType.typeCode == 30)
    {
      localBlobData = (BlobData)paramObject;
      l = localBlobData.length(paramSessionInterface);
      if (l > this.precision) {
        throw Error.error(3401);
      }
      return paramObject;
    }
    if (paramType.typeCode == 40)
    {
      paramObject = Type.SQL_VARCHAR.convertToType(paramSessionInterface, paramObject, paramType);
      paramType = Type.SQL_VARCHAR;
    }
    if ((paramType.typeCode == 12) || (paramType.typeCode == 1))
    {
      paramObject = paramSessionInterface.getScanner().convertToBinary((String)paramObject);
      paramType = Type.SQL_VARBINARY;
    }
    if ((paramType.typeCode == 60) || (paramType.typeCode == 61))
    {
      localBlobData = (BlobData)paramObject;
      l = localBlobData.length(paramSessionInterface);
      if (l > this.precision) {
        throw Error.error(3401);
      }
      BlobDataID localBlobDataID = paramSessionInterface.createBlob(localBlobData.length(paramSessionInterface));
      localBlobDataID.setBytes(paramSessionInterface, 0L, localBlobData.getBytes());
      return localBlobDataID;
    }
    throw Error.error(5561);
  }
  
  public Object convertJavaToSQL(SessionInterface paramSessionInterface, Object paramObject)
  {
    if (paramObject == null) {
      return null;
    }
    if ((paramObject instanceof JDBCBlobClient)) {
      return ((JDBCBlobClient)paramObject).getBlob();
    }
    throw Error.error(5561);
  }
  
  public Object convertSQLToJava(SessionInterface paramSessionInterface, Object paramObject)
  {
    if (paramObject == null) {
      return null;
    }
    if ((paramObject instanceof BlobDataID))
    {
      BlobDataID localBlobDataID = (BlobDataID)paramObject;
      return new JDBCBlobClient(paramSessionInterface, localBlobDataID);
    }
    throw Error.error(5561);
  }
  
  public Object convertToDefaultType(SessionInterface paramSessionInterface, Object paramObject)
  {
    if (paramObject == null) {
      return null;
    }
    if ((paramObject instanceof byte[])) {
      return new BinaryData((byte[])paramObject, false);
    }
    throw Error.error(5561);
  }
  
  public String convertToString(Object paramObject)
  {
    if (paramObject == null) {
      return null;
    }
    return Long.toString(((BlobData)paramObject).getId());
  }
  
  public String convertToSQLString(Object paramObject)
  {
    if (paramObject == null) {
      return "NULL";
    }
    return convertToString(paramObject);
  }
}


/* Location:              E:\java\java学习\hutubill\lib\all.jar!\org\hsqldb\types\BlobType.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */
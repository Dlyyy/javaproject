package org.hsqldb.types;

import java.sql.Clob;
import org.hsqldb.Database;
import org.hsqldb.Session;
import org.hsqldb.SessionInterface;
import org.hsqldb.error.Error;
import org.hsqldb.jdbc.JDBCClobClient;
import org.hsqldb.persist.LobManager;

public final class ClobType
  extends CharacterType
{
  public static final long maxClobPrecision = 1099511627776L;
  public static final int defaultClobSize = 1073741824;
  public static final int defaultShortClobSize = 16777216;
  
  public ClobType(long paramLong)
  {
    super(40, paramLong);
  }
  
  public int displaySize()
  {
    return this.precision > 2147483647L ? Integer.MAX_VALUE : (int)this.precision;
  }
  
  public int getJDBCTypeCode()
  {
    return 2005;
  }
  
  public Class getJDBCClass()
  {
    return Clob.class;
  }
  
  public String getJDBCClassName()
  {
    return "java.sql.Clob";
  }
  
  public int getSQLGenericTypeCode()
  {
    return this.typeCode;
  }
  
  public String getDefinition()
  {
    long l = this.precision;
    String str = null;
    if (this.precision % 1024L == 0L) {
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
      else
      {
        l = this.precision / 1024L;
        str = "K";
      }
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
  
  public long getMaxPrecision()
  {
    return 1099511627776L;
  }
  
  public boolean isLobType()
  {
    return true;
  }
  
  public int compare(Session paramSession, Object paramObject1, Object paramObject2)
  {
    return compare(paramSession, paramObject1, paramObject2, 40);
  }
  
  public int compare(Session paramSession, Object paramObject1, Object paramObject2, int paramInt)
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
    if ((paramObject2 instanceof String)) {
      return paramSession.database.lobManager.compare(this.collation, (ClobData)paramObject1, (String)paramObject2);
    }
    return paramSession.database.lobManager.compare(this.collation, (ClobData)paramObject1, (ClobData)paramObject2);
  }
  
  public Object convertToDefaultType(SessionInterface paramSessionInterface, Object paramObject)
  {
    if (paramObject == null) {
      return null;
    }
    if ((paramObject instanceof ClobData)) {
      return paramObject;
    }
    if ((paramObject instanceof String))
    {
      ClobDataID localClobDataID = paramSessionInterface.createClob(((String)paramObject).length());
      localClobDataID.setString(paramSessionInterface, 0L, (String)paramObject);
      return localClobDataID;
    }
    throw Error.error(5561);
  }
  
  public String convertToString(Object paramObject)
  {
    if (paramObject == null) {
      return null;
    }
    return Long.toString(((ClobData)paramObject).getId());
  }
  
  public String convertToSQLString(Object paramObject)
  {
    if (paramObject == null) {
      return "NULL";
    }
    return convertToString(paramObject);
  }
  
  public Object convertJavaToSQL(SessionInterface paramSessionInterface, Object paramObject)
  {
    if (paramObject == null) {
      return null;
    }
    if ((paramObject instanceof JDBCClobClient)) {
      return ((JDBCClobClient)paramObject).getClob();
    }
    throw Error.error(5561);
  }
  
  public Object convertSQLToJava(SessionInterface paramSessionInterface, Object paramObject)
  {
    if (paramObject == null) {
      return null;
    }
    if ((paramObject instanceof ClobDataID))
    {
      ClobDataID localClobDataID = (ClobDataID)paramObject;
      return new JDBCClobClient(paramSessionInterface, localClobDataID);
    }
    throw Error.error(5561);
  }
  
  public long position(SessionInterface paramSessionInterface, Object paramObject1, Object paramObject2, Type paramType, long paramLong)
  {
    if (paramType.typeCode == 40) {
      return ((ClobData)paramObject1).position(paramSessionInterface, (ClobData)paramObject2, paramLong);
    }
    if (paramType.isCharacterType()) {
      return ((ClobData)paramObject1).position(paramSessionInterface, (String)paramObject2, paramLong);
    }
    throw Error.runtimeError(201, "ClobType");
  }
}


/* Location:              E:\java\java学习\hutubill\lib\all.jar!\org\hsqldb\types\ClobType.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */
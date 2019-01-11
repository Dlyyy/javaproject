package org.hsqldb.types;

import java.util.UUID;
import org.hsqldb.Scanner;
import org.hsqldb.Session;
import org.hsqldb.SessionInterface;
import org.hsqldb.error.Error;
import org.hsqldb.lib.ArrayUtil;
import org.hsqldb.lib.StringConverter;

public class BinaryUUIDType
  extends BinaryType
{
  public static final int binaryUUIDPrecision = 16;
  
  protected BinaryUUIDType()
  {
    super(-11, 16L);
  }
  
  public int displaySize()
  {
    return 36;
  }
  
  public int getJDBCTypeCode()
  {
    return -2;
  }
  
  public Class getJDBCClass()
  {
    return byte[].class;
  }
  
  public String getJDBCClassName()
  {
    return "[B";
  }
  
  public String getNameString()
  {
    return "UUID";
  }
  
  public String getNameFullString()
  {
    return "UUID";
  }
  
  public String getDefinition()
  {
    return getNameString();
  }
  
  public boolean isBinaryType()
  {
    return true;
  }
  
  public boolean acceptsPrecision()
  {
    return false;
  }
  
  public long getMaxPrecision()
  {
    return 16L;
  }
  
  public boolean requiresPrecision()
  {
    return false;
  }
  
  public int precedenceDegree(Type paramType)
  {
    if (paramType.typeCode == this.typeCode) {
      return 0;
    }
    if (!paramType.isBinaryType()) {
      return Integer.MIN_VALUE;
    }
    return paramType.typeCode == 30 ? 4 : 2;
  }
  
  public Type getAggregateType(Type paramType)
  {
    if (paramType == null) {
      return this;
    }
    if (paramType == SQL_ALL_TYPES) {
      return this;
    }
    if (this.typeCode == paramType.typeCode) {
      return this;
    }
    if (paramType.isCharacterType()) {
      return this;
    }
    switch (paramType.typeCode)
    {
    case -11: 
    case 30: 
    case 60: 
    case 61: 
      return this;
    }
    throw Error.error(5562);
  }
  
  public Type getCombinedType(Session paramSession, Type paramType, int paramInt)
  {
    return Type.SQL_VARBINARY_DEFAULT;
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
    if (((paramObject1 instanceof BinaryData)) && ((paramObject2 instanceof BinaryData)))
    {
      byte[] arrayOfByte1 = ((BinaryData)paramObject1).getBytes();
      byte[] arrayOfByte2 = ((BinaryData)paramObject2).getBytes();
      int i = arrayOfByte1.length > arrayOfByte2.length ? arrayOfByte2.length : arrayOfByte1.length;
      for (int j = 0; j < i; j++) {
        if (arrayOfByte1[j] != arrayOfByte2[j]) {
          return (arrayOfByte1[j] & 0xFF) > (arrayOfByte2[j] & 0xFF) ? 1 : -1;
        }
      }
      if (arrayOfByte1.length == arrayOfByte2.length) {
        return 0;
      }
      return arrayOfByte1.length > arrayOfByte2.length ? 1 : -1;
    }
    throw Error.runtimeError(201, "BinaryUUIDType");
  }
  
  public Object convertToTypeLimits(SessionInterface paramSessionInterface, Object paramObject)
  {
    return castOrConvertToType(paramSessionInterface, paramObject, this, false);
  }
  
  public Object castToType(SessionInterface paramSessionInterface, Object paramObject, Type paramType)
  {
    return castOrConvertToType(paramSessionInterface, paramObject, paramType, true);
  }
  
  public Object convertToType(SessionInterface paramSessionInterface, Object paramObject, Type paramType)
  {
    return castOrConvertToType(paramSessionInterface, paramObject, paramType, false);
  }
  
  public Object convertJavaToSQL(SessionInterface paramSessionInterface, Object paramObject)
  {
    if (paramObject == null) {
      return null;
    }
    if ((paramObject instanceof byte[])) {
      return new BinaryData((byte[])paramObject, true);
    }
    throw Error.error(5561);
  }
  
  public Object convertSQLToJava(SessionInterface paramSessionInterface, Object paramObject)
  {
    if (paramObject == null) {
      return null;
    }
    return ((BlobData)paramObject).getBytes();
  }
  
  Object castOrConvertToType(SessionInterface paramSessionInterface, Object paramObject, Type paramType, boolean paramBoolean)
  {
    if (paramObject == null) {
      return null;
    }
    Object localObject;
    switch (paramType.typeCode)
    {
    case 40: 
      paramObject = Type.SQL_VARCHAR.convertToType(paramSessionInterface, paramObject, paramType);
    case 1: 
    case 12: 
      localObject = paramSessionInterface.getScanner().convertToBinary((String)paramObject, true);
      paramType = this;
      break;
    case -11: 
    case 30: 
    case 60: 
    case 61: 
      localObject = (BlobData)paramObject;
      break;
    default: 
      throw Error.error(3471);
    }
    if (paramType.typeCode == 30)
    {
      long l = ((BlobData)localObject).length(paramSessionInterface);
      if (l != this.precision) {
        throw Error.error(3401);
      }
      byte[] arrayOfByte = ((BlobData)localObject).getBytes(paramSessionInterface, 0L, (int)this.precision);
      localObject = new BinaryData(arrayOfByte, false);
      return localObject;
    }
    if (((BlobData)localObject).length(paramSessionInterface) != this.precision) {
      throw Error.error(3401);
    }
    return localObject;
  }
  
  public Object convertToDefaultType(SessionInterface paramSessionInterface, Object paramObject)
  {
    if (paramObject == null) {
      return paramObject;
    }
    if ((paramObject instanceof byte[]))
    {
      BinaryData localBinaryData = new BinaryData((byte[])paramObject, false);
      castOrConvertToType(paramSessionInterface, localBinaryData, Type.SQL_VARBINARY, false);
    }
    else
    {
      if ((paramObject instanceof BinaryData)) {
        return castOrConvertToType(paramSessionInterface, paramObject, Type.SQL_VARBINARY, false);
      }
      if ((paramObject instanceof String)) {
        return castOrConvertToType(paramSessionInterface, paramObject, Type.SQL_VARCHAR, false);
      }
    }
    throw Error.error(3471);
  }
  
  public String convertToString(Object paramObject)
  {
    if (paramObject == null) {
      return null;
    }
    return StringConverter.toStringUUID(((BlobData)paramObject).getBytes());
  }
  
  public String convertToSQLString(Object paramObject)
  {
    if (paramObject == null) {
      return "NULL";
    }
    return StringConverter.toStringUUID(((BlobData)paramObject).getBytes());
  }
  
  public boolean canConvertFrom(Type paramType)
  {
    return (paramType.typeCode == 0) || (paramType.isBinaryType()) || (paramType.isCharacterType());
  }
  
  public int canMoveFrom(Type paramType)
  {
    if (paramType == this) {
      return 0;
    }
    if (!paramType.isBinaryType()) {
      return -1;
    }
    switch (paramType.typeCode)
    {
    case 61: 
      return 1;
    case 14: 
    case 15: 
    case 30: 
      return -1;
    case -11: 
    case 60: 
      return this.precision == paramType.precision ? 0 : -1;
    }
    return -1;
  }
  
  public long position(SessionInterface paramSessionInterface, BlobData paramBlobData1, BlobData paramBlobData2, Type paramType, long paramLong)
  {
    if ((paramBlobData1 == null) || (paramBlobData2 == null)) {
      return -1L;
    }
    long l = paramBlobData1.length(paramSessionInterface);
    if (paramLong + l > paramBlobData1.length(paramSessionInterface)) {
      return -1L;
    }
    return paramBlobData1.position(paramSessionInterface, paramBlobData2, paramLong);
  }
  
  public BlobData substring(SessionInterface paramSessionInterface, BlobData paramBlobData, long paramLong1, long paramLong2, boolean paramBoolean)
  {
    long l2 = paramBlobData.length(paramSessionInterface);
    long l1;
    if (paramBoolean) {
      l1 = paramLong1 + paramLong2;
    } else {
      l1 = l2 > paramLong1 ? l2 : paramLong1;
    }
    if (paramLong1 > l1) {
      throw Error.error(3431);
    }
    if ((paramLong1 > l1) || (l1 < 0L))
    {
      paramLong1 = 0L;
      l1 = 0L;
    }
    if (paramLong1 < 0L) {
      paramLong1 = 0L;
    }
    if (l1 > l2) {
      l1 = l2;
    }
    paramLong2 = l1 - paramLong1;
    byte[] arrayOfByte = paramBlobData.getBytes(paramSessionInterface, paramLong1, (int)paramLong2);
    return new BinaryData(arrayOfByte, false);
  }
  
  int getRightTrimSize(BlobData paramBlobData)
  {
    byte[] arrayOfByte = paramBlobData.getBytes();
    int i = arrayOfByte.length;
    i--;
    while ((i >= 0) && (arrayOfByte[i] == 0)) {
      i--;
    }
    i++;
    return i;
  }
  
  public BlobData trim(Session paramSession, BlobData paramBlobData, int paramInt, boolean paramBoolean1, boolean paramBoolean2)
  {
    if (paramBlobData == null) {
      return null;
    }
    long l = paramBlobData.length(paramSession);
    if (l > 2147483647L) {
      throw Error.error(3460);
    }
    byte[] arrayOfByte1 = paramBlobData.getBytes(paramSession, 0L, (int)l);
    int i = arrayOfByte1.length;
    if (paramBoolean2)
    {
      i--;
      while ((i >= 0) && (arrayOfByte1[i] == paramInt)) {
        i--;
      }
      i++;
    }
    int j = 0;
    if (paramBoolean1) {
      while ((j < i) && (arrayOfByte1[j] == paramInt)) {
        j++;
      }
    }
    byte[] arrayOfByte2 = arrayOfByte1;
    if ((j != 0) || (i != arrayOfByte1.length))
    {
      arrayOfByte2 = new byte[i - j];
      System.arraycopy(arrayOfByte1, j, arrayOfByte2, 0, i - j);
    }
    return new BinaryData(arrayOfByte2, arrayOfByte2 == arrayOfByte1);
  }
  
  public BlobData overlay(Session paramSession, BlobData paramBlobData1, BlobData paramBlobData2, long paramLong1, long paramLong2, boolean paramBoolean)
  {
    if ((paramBlobData1 == null) || (paramBlobData2 == null)) {
      return null;
    }
    if (!paramBoolean) {
      paramLong2 = paramBlobData2.length(paramSession);
    }
    BinaryData localBinaryData = new BinaryData(paramSession, substring(paramSession, paramBlobData1, 0L, paramLong1, true), paramBlobData2);
    localBinaryData = new BinaryData(paramSession, localBinaryData, substring(paramSession, paramBlobData1, paramLong1 + paramLong2, 0L, false));
    return localBinaryData;
  }
  
  public Object concat(Session paramSession, Object paramObject1, Object paramObject2)
  {
    if ((paramObject1 == null) || (paramObject2 == null)) {
      return null;
    }
    long l = ((BlobData)paramObject1).length(paramSession) + ((BlobData)paramObject2).length(paramSession);
    if (l > this.precision) {
      throw Error.error(3401);
    }
    return new BinaryData(paramSession, (BlobData)paramObject1, (BlobData)paramObject2);
  }
  
  public long getLongColumnValue(Object paramObject, int paramInt)
  {
    if (paramObject == null) {
      return Long.MIN_VALUE;
    }
    BinaryData localBinaryData = (BinaryData)paramObject;
    byte[] arrayOfByte = localBinaryData.getBytes();
    long l = 0L;
    if (arrayOfByte.length == 0) {
      return Long.MIN_VALUE;
    }
    int i = arrayOfByte[0];
    i = (byte)(i - 128);
    l |= i & 0xFF;
    for (int j = 1; j < 8; j++)
    {
      l <<= 8;
      if (j < arrayOfByte.length) {
        l |= arrayOfByte[j] & 0xFF;
      }
    }
    return l;
  }
  
  public static BinaryData getBinary(long paramLong1, long paramLong2)
  {
    return new BinaryData(ArrayUtil.toByteArray(paramLong1, paramLong2), false);
  }
  
  public static BinaryData getBinary(UUID paramUUID)
  {
    return getBinary(paramUUID.getMostSignificantBits(), paramUUID.getLeastSignificantBits());
  }
}


/* Location:              E:\java\java学习\hutubill\lib\all.jar!\org\hsqldb\types\BinaryUUIDType.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */
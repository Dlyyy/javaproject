package org.hsqldb.rowio;

import org.hsqldb.Row;
import org.hsqldb.lib.HashMappedList;
import org.hsqldb.lib.HsqlByteArrayOutputStream;
import org.hsqldb.types.Type;

public abstract interface RowOutputInterface
  extends Cloneable
{
  public abstract void setStorageSize(int paramInt);
  
  public abstract void writeEnd();
  
  public abstract void writeSize(int paramInt);
  
  public abstract void writeType(int paramInt);
  
  public abstract void writeString(String paramString);
  
  public abstract void writeByte(int paramInt);
  
  public abstract void writeChar(int paramInt);
  
  public abstract void writeShort(int paramInt);
  
  public abstract void writeInt(int paramInt);
  
  public abstract void writeLong(long paramLong);
  
  public abstract void writeData(Object paramObject, Type paramType);
  
  public abstract void writeData(Row paramRow, Type[] paramArrayOfType);
  
  public abstract void writeData(int paramInt, Type[] paramArrayOfType, Object[] paramArrayOfObject, HashMappedList paramHashMappedList, int[] paramArrayOfInt);
  
  public abstract int getSize(Row paramRow);
  
  public abstract int getStorageSize(int paramInt);
  
  public abstract HsqlByteArrayOutputStream getOutputStream();
  
  public abstract byte[] getBuffer();
  
  public abstract void reset();
  
  public abstract void reset(int paramInt);
  
  public abstract void reset(byte[] paramArrayOfByte);
  
  public abstract int size();
  
  public abstract RowOutputInterface duplicate();
}


/* Location:              E:\java\java学习\hutubill\lib\all.jar!\org\hsqldb\rowio\RowOutputInterface.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */
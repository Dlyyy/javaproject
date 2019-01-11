package org.hsqldb.types;

import java.io.Serializable;
import org.hsqldb.error.Error;
import org.hsqldb.lib.InOutUtil;

public class JavaObjectData
{
  private byte[] data;
  
  JavaObjectData() {}
  
  public JavaObjectData(byte[] paramArrayOfByte)
  {
    this.data = paramArrayOfByte;
  }
  
  public JavaObjectData(Serializable paramSerializable)
  {
    try
    {
      this.data = InOutUtil.serialize(paramSerializable);
    }
    catch (Exception localException)
    {
      throw Error.error(3473, localException.toString());
    }
  }
  
  public byte[] getBytes()
  {
    return this.data;
  }
  
  public int getBytesLength()
  {
    return this.data.length;
  }
  
  public Object getObject()
  {
    try
    {
      return InOutUtil.deserialize(this.data);
    }
    catch (Exception localException)
    {
      throw Error.error(3473, localException.toString());
    }
  }
  
  public String toString()
  {
    return super.toString();
  }
  
  public boolean equals(Object paramObject)
  {
    return paramObject instanceof JavaObjectData;
  }
  
  public int hashCode()
  {
    return 1;
  }
}


/* Location:              E:\java\java学习\hutubill\lib\all.jar!\org\hsqldb\types\JavaObjectData.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */
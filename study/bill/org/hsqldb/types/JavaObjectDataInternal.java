package org.hsqldb.types;

import java.io.Serializable;
import org.hsqldb.error.Error;
import org.hsqldb.lib.InOutUtil;

public class JavaObjectDataInternal
  extends JavaObjectData
{
  Object object;
  
  public JavaObjectDataInternal(byte[] paramArrayOfByte)
  {
    try
    {
      this.object = InOutUtil.deserialize(paramArrayOfByte);
    }
    catch (Exception localException)
    {
      throw Error.error(3473, localException.toString());
    }
  }
  
  public JavaObjectDataInternal(Object paramObject)
  {
    this.object = paramObject;
  }
  
  public byte[] getBytes()
  {
    try
    {
      if ((this.object instanceof Serializable)) {
        return InOutUtil.serialize((Serializable)this.object);
      }
    }
    catch (Exception localException) {}
    return new byte[0];
  }
  
  public int getBytesLength()
  {
    try
    {
      if ((this.object instanceof Serializable))
      {
        byte[] arrayOfByte = InOutUtil.serialize((Serializable)this.object);
        return arrayOfByte.length;
      }
    }
    catch (Exception localException) {}
    return 0;
  }
  
  public Object getObject()
  {
    return this.object;
  }
  
  public String toString()
  {
    return super.toString();
  }
}


/* Location:              E:\java\java学习\hutubill\lib\all.jar!\org\hsqldb\types\JavaObjectDataInternal.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */
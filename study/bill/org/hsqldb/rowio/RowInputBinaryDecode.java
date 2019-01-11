package org.hsqldb.rowio;

import org.hsqldb.persist.Crypto;
import org.hsqldb.types.Type;

public class RowInputBinaryDecode
  extends RowInputBinary
{
  final Crypto crypto;
  
  public RowInputBinaryDecode(Crypto paramCrypto, byte[] paramArrayOfByte)
  {
    super(paramArrayOfByte);
    this.crypto = paramCrypto;
  }
  
  public Object[] readData(Type[] paramArrayOfType)
  {
    if (this.crypto != null)
    {
      int i = this.pos;
      int j = readInt();
      this.crypto.decode(this.buffer, this.pos, j, this.buffer, i);
      this.pos = i;
    }
    return super.readData(paramArrayOfType);
  }
}


/* Location:              E:\java\java学习\hutubill\lib\all.jar!\org\hsqldb\rowio\RowInputBinaryDecode.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */
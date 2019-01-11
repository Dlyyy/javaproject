package org.hsqldb.persist;

import org.hsqldb.lib.LongLookup;
import org.hsqldb.rowio.RowInputInterface;
import org.hsqldb.rowio.RowOutputInterface;

public class IntArrayCachedObject
  extends CachedObjectBase
{
  public static final int fileSizeFactor = 4;
  int[] values;
  
  public IntArrayCachedObject(int paramInt)
  {
    this.values = new int[paramInt];
    this.hasChanged = true;
  }
  
  public void read(RowInputInterface paramRowInputInterface)
  {
    this.position = paramRowInputInterface.getPos();
    int i = this.values.length;
    for (int j = 0; j < i; j++) {
      this.values[j] = paramRowInputInterface.readInt();
    }
    this.hasChanged = false;
  }
  
  public int getDefaultCapacity()
  {
    return this.values.length;
  }
  
  public int getRealSize(RowOutputInterface paramRowOutputInterface)
  {
    return this.values.length * 4;
  }
  
  public void write(RowOutputInterface paramRowOutputInterface)
  {
    write(paramRowOutputInterface, null);
  }
  
  public void write(RowOutputInterface paramRowOutputInterface, LongLookup paramLongLookup)
  {
    int i = this.values.length;
    paramRowOutputInterface.setStorageSize(this.storageSize);
    for (int j = 0; j < i; j++) {
      paramRowOutputInterface.writeInt(this.values[j]);
    }
    paramRowOutputInterface.writeEnd();
  }
  
  public int[] getIntArray()
  {
    return this.values;
  }
}


/* Location:              E:\java\java学习\hutubill\lib\all.jar!\org\hsqldb\persist\IntArrayCachedObject.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */
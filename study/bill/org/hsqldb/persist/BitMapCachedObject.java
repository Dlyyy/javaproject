package org.hsqldb.persist;

import org.hsqldb.lib.LongLookup;
import org.hsqldb.map.BitMap;
import org.hsqldb.rowio.RowInputInterface;
import org.hsqldb.rowio.RowOutputInterface;

public class BitMapCachedObject
  extends CachedObjectBase
{
  public static final int fileSizeFactor = 4;
  BitMap bitMap;
  
  public BitMapCachedObject(int paramInt)
  {
    this.bitMap = new BitMap(new int[paramInt]);
    this.hasChanged = true;
  }
  
  public void read(RowInputInterface paramRowInputInterface)
  {
    this.position = paramRowInputInterface.getPos();
    int[] arrayOfInt = this.bitMap.getIntArray();
    int i = arrayOfInt.length;
    for (int j = 0; j < i; j++) {
      arrayOfInt[j] = paramRowInputInterface.readInt();
    }
    this.hasChanged = false;
  }
  
  public int getDefaultCapacity()
  {
    return this.bitMap.getIntArray().length;
  }
  
  public int getRealSize(RowOutputInterface paramRowOutputInterface)
  {
    return this.bitMap.getIntArray().length * 4;
  }
  
  public void write(RowOutputInterface paramRowOutputInterface)
  {
    write(paramRowOutputInterface, null);
  }
  
  public void write(RowOutputInterface paramRowOutputInterface, LongLookup paramLongLookup)
  {
    int[] arrayOfInt = this.bitMap.getIntArray();
    int i = arrayOfInt.length;
    paramRowOutputInterface.setStorageSize(this.storageSize);
    for (int j = 0; j < i; j++) {
      paramRowOutputInterface.writeInt(arrayOfInt[j]);
    }
    paramRowOutputInterface.writeEnd();
  }
  
  public BitMap getBitMap()
  {
    return this.bitMap;
  }
}


/* Location:              E:\java\java学习\hutubill\lib\all.jar!\org\hsqldb\persist\BitMapCachedObject.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */
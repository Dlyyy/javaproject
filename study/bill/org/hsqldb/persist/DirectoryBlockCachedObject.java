package org.hsqldb.persist;

import org.hsqldb.lib.LongLookup;
import org.hsqldb.rowio.RowInputInterface;
import org.hsqldb.rowio.RowOutputInterface;

public class DirectoryBlockCachedObject
  extends CachedObjectBase
{
  public static final int fileSizeFactor = 12;
  int[] tableIds;
  int[] bitmapAddress;
  char[] freeSpace;
  char[] freeSpaceBlock;
  
  public DirectoryBlockCachedObject(int paramInt)
  {
    this.tableIds = new int[paramInt];
    this.bitmapAddress = new int[paramInt];
    this.freeSpace = new char[paramInt];
    this.freeSpaceBlock = new char[paramInt];
    this.hasChanged = true;
  }
  
  public void read(RowInputInterface paramRowInputInterface)
  {
    this.position = paramRowInputInterface.getPos();
    int i = this.tableIds.length;
    for (int j = 0; j < i; j++) {
      this.tableIds[j] = paramRowInputInterface.readInt();
    }
    for (j = 0; j < i; j++) {
      this.bitmapAddress[j] = paramRowInputInterface.readInt();
    }
    for (j = 0; j < i; j++) {
      this.freeSpace[j] = paramRowInputInterface.readChar();
    }
    for (j = 0; j < i; j++) {
      this.freeSpaceBlock[j] = paramRowInputInterface.readChar();
    }
    this.hasChanged = false;
  }
  
  public int getDefaultCapacity()
  {
    return this.tableIds.length;
  }
  
  public int getRealSize(RowOutputInterface paramRowOutputInterface)
  {
    return this.tableIds.length * 12;
  }
  
  public void write(RowOutputInterface paramRowOutputInterface)
  {
    write(paramRowOutputInterface, null);
  }
  
  public void write(RowOutputInterface paramRowOutputInterface, LongLookup paramLongLookup)
  {
    int i = this.tableIds.length;
    paramRowOutputInterface.setStorageSize(this.storageSize);
    for (int j = 0; j < i; j++) {
      paramRowOutputInterface.writeInt(this.tableIds[j]);
    }
    for (j = 0; j < i; j++) {
      paramRowOutputInterface.writeInt(this.bitmapAddress[j]);
    }
    for (j = 0; j < i; j++) {
      paramRowOutputInterface.writeChar(this.freeSpace[j]);
    }
    for (j = 0; j < i; j++) {
      paramRowOutputInterface.writeChar(this.freeSpaceBlock[j]);
    }
    paramRowOutputInterface.writeEnd();
  }
  
  public int[] getTableIdArray()
  {
    return this.tableIds;
  }
  
  public int[] getBitmapAddressArray()
  {
    return this.bitmapAddress;
  }
  
  public char[] getFreeSpaceArray()
  {
    return this.freeSpace;
  }
  
  public char[] getFreeBlockArray()
  {
    return this.freeSpaceBlock;
  }
}


/* Location:              E:\java\java学习\hutubill\lib\all.jar!\org\hsqldb\persist\DirectoryBlockCachedObject.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */
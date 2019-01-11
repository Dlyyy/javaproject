package org.hsqldb;

import java.io.IOException;
import org.hsqldb.index.NodeAVL;
import org.hsqldb.persist.PersistentStore;
import org.hsqldb.persist.RowStoreAVLDiskData;
import org.hsqldb.rowio.RowInputInterface;
import org.hsqldb.rowio.RowOutputInterface;

public class RowAVLDiskData
  extends RowAVL
{
  RowStoreAVLDiskData store;
  int accessCount;
  boolean hasDataChanged;
  int storageSize;
  
  public RowAVLDiskData(PersistentStore paramPersistentStore, TableBase paramTableBase, Object[] paramArrayOfObject)
  {
    super(paramTableBase, paramArrayOfObject);
    setNewNodes(paramPersistentStore);
    this.store = ((RowStoreAVLDiskData)paramPersistentStore);
    this.hasDataChanged = true;
  }
  
  public RowAVLDiskData(RowStoreAVLDiskData paramRowStoreAVLDiskData, TableBase paramTableBase, RowInputInterface paramRowInputInterface)
    throws IOException
  {
    super(paramTableBase, (Object[])null);
    setNewNodes(paramRowStoreAVLDiskData);
    this.position = paramRowInputInterface.getPos();
    this.storageSize = paramRowInputInterface.getSize();
    this.rowData = paramRowInputInterface.readData(this.table.getColumnTypes());
    this.hasDataChanged = false;
    this.store = paramRowStoreAVLDiskData;
  }
  
  public void setData(Object[] paramArrayOfObject)
  {
    this.rowData = paramArrayOfObject;
  }
  
  public Object[] getData()
  {
    Object[] arrayOfObject = this.rowData;
    if (arrayOfObject == null)
    {
      arrayOfObject = this.store.getData(this);
      arrayOfObject = this.rowData;
      keepInMemory(false);
    }
    else
    {
      this.accessCount += 1;
    }
    return arrayOfObject;
  }
  
  public void setNewNodes(PersistentStore paramPersistentStore)
  {
    int i = paramPersistentStore.getAccessorKeys().length;
    this.nPrimaryNode = new NodeAVL(this);
    NodeAVL localNodeAVL = this.nPrimaryNode;
    for (int j = 1; j < i; j++)
    {
      localNodeAVL.nNext = new NodeAVL(this);
      localNodeAVL = localNodeAVL.nNext;
    }
  }
  
  public NodeAVL insertNode(int paramInt)
  {
    NodeAVL localNodeAVL1 = getNode(paramInt - 1);
    NodeAVL localNodeAVL2 = new NodeAVL(this);
    localNodeAVL2.nNext = localNodeAVL1.nNext;
    localNodeAVL1.nNext = localNodeAVL2;
    return localNodeAVL2;
  }
  
  public int getRealSize(RowOutputInterface paramRowOutputInterface)
  {
    return paramRowOutputInterface.getSize(this);
  }
  
  public void write(RowOutputInterface paramRowOutputInterface)
  {
    paramRowOutputInterface.writeSize(this.storageSize);
    paramRowOutputInterface.writeData(this, this.table.colTypes);
    paramRowOutputInterface.writeEnd();
  }
  
  public boolean isNew()
  {
    return false;
  }
  
  public boolean hasChanged()
  {
    return this.hasDataChanged;
  }
  
  public synchronized void setChanged(boolean paramBoolean)
  {
    this.hasDataChanged = paramBoolean;
  }
  
  public void updateAccessCount(int paramInt)
  {
    this.accessCount = paramInt;
  }
  
  public int getAccessCount()
  {
    return this.accessCount;
  }
  
  public int getStorageSize()
  {
    return this.storageSize;
  }
  
  public void setStorageSize(int paramInt)
  {
    this.storageSize = paramInt;
  }
  
  public void setPos(long paramLong)
  {
    this.position = paramLong;
  }
  
  public boolean isMemory()
  {
    return true;
  }
  
  public boolean isInMemory()
  {
    return this.rowData != null;
  }
  
  public boolean isKeepInMemory()
  {
    return false;
  }
  
  public boolean keepInMemory(boolean paramBoolean)
  {
    return true;
  }
  
  public void setInMemory(boolean paramBoolean)
  {
    if (!paramBoolean) {
      this.rowData = null;
    }
  }
}


/* Location:              E:\java\java学习\hutubill\lib\all.jar!\org\hsqldb\RowAVLDiskData.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */
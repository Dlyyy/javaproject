package org.hsqldb.index;

import java.io.IOException;
import org.hsqldb.Row;
import org.hsqldb.RowAVL;
import org.hsqldb.RowAVLDisk;
import org.hsqldb.lib.LongLookup;
import org.hsqldb.persist.PersistentStore;
import org.hsqldb.rowio.RowInputInterface;
import org.hsqldb.rowio.RowOutputInterface;

public class NodeAVLDiskLarge
  extends NodeAVL
{
  private long iLeft = -1L;
  private long iRight = -1L;
  private long iParent = -1L;
  private int iId;
  public static final int SIZE_IN_BYTE = 16;
  
  public NodeAVLDiskLarge(RowAVLDisk paramRowAVLDisk, RowInputInterface paramRowInputInterface, int paramInt)
    throws IOException
  {
    super(paramRowAVLDisk);
    this.iId = paramInt;
    int i = paramRowInputInterface.readInt();
    this.iBalance = ((byte)i);
    this.iLeft = (paramRowInputInterface.readInt() & 0xFFFFFFFF);
    this.iRight = (paramRowInputInterface.readInt() & 0xFFFFFFFF);
    this.iParent = (paramRowInputInterface.readInt() & 0xFFFFFFFF);
    if (i > 255)
    {
      this.iParent |= i << 8 & 0xFF00000000;
      this.iLeft |= i << 16 & 0xFF00000000;
      this.iRight |= i << 24 & 0xFF00000000;
    }
    if (this.iLeft == 0L) {
      this.iLeft = -1L;
    }
    if (this.iRight == 0L) {
      this.iRight = -1L;
    }
    if (this.iParent == 0L) {
      this.iParent = -1L;
    }
  }
  
  public NodeAVLDiskLarge(RowAVLDisk paramRowAVLDisk, int paramInt)
  {
    super(paramRowAVLDisk);
    this.iId = paramInt;
  }
  
  public void delete()
  {
    this.iLeft = -1L;
    this.iRight = -1L;
    this.iParent = -1L;
    this.iBalance = 0;
    ((RowAVLDisk)this.row).setNodesChanged();
  }
  
  public boolean isInMemory()
  {
    return this.row.isInMemory();
  }
  
  public boolean isMemory()
  {
    return false;
  }
  
  public long getPos()
  {
    return this.row.getPos();
  }
  
  public RowAVL getRow(PersistentStore paramPersistentStore)
  {
    return (RowAVLDisk)paramPersistentStore.get(this.row, false);
  }
  
  public Object[] getData(PersistentStore paramPersistentStore)
  {
    return this.row.getData();
  }
  
  private NodeAVLDiskLarge findNode(PersistentStore paramPersistentStore, long paramLong)
  {
    NodeAVLDiskLarge localNodeAVLDiskLarge = null;
    RowAVLDisk localRowAVLDisk = (RowAVLDisk)paramPersistentStore.get(paramLong, false);
    if (localRowAVLDisk != null) {
      localNodeAVLDiskLarge = (NodeAVLDiskLarge)localRowAVLDisk.getNode(this.iId);
    }
    return localNodeAVLDiskLarge;
  }
  
  boolean isLeft(NodeAVL paramNodeAVL)
  {
    if (paramNodeAVL == null) {
      return this.iLeft == -1L;
    }
    return this.iLeft == paramNodeAVL.getPos();
  }
  
  boolean isRight(NodeAVL paramNodeAVL)
  {
    if (paramNodeAVL == null) {
      return this.iRight == -1L;
    }
    return this.iRight == paramNodeAVL.getPos();
  }
  
  NodeAVL getLeft(PersistentStore paramPersistentStore)
  {
    NodeAVLDiskLarge localNodeAVLDiskLarge1 = this;
    RowAVLDisk localRowAVLDisk = (RowAVLDisk)paramPersistentStore.get(this.row, false);
    localNodeAVLDiskLarge1 = (NodeAVLDiskLarge)localRowAVLDisk.getNode(this.iId);
    if (localNodeAVLDiskLarge1.iLeft == -1L) {
      return null;
    }
    NodeAVLDiskLarge localNodeAVLDiskLarge2 = findNode(paramPersistentStore, localNodeAVLDiskLarge1.iLeft);
    return localNodeAVLDiskLarge2;
  }
  
  NodeAVL getRight(PersistentStore paramPersistentStore)
  {
    NodeAVLDiskLarge localNodeAVLDiskLarge1 = this;
    RowAVLDisk localRowAVLDisk = (RowAVLDisk)paramPersistentStore.get(this.row, false);
    localNodeAVLDiskLarge1 = (NodeAVLDiskLarge)localRowAVLDisk.getNode(this.iId);
    if (localNodeAVLDiskLarge1.iRight == -1L) {
      return null;
    }
    NodeAVLDiskLarge localNodeAVLDiskLarge2 = findNode(paramPersistentStore, localNodeAVLDiskLarge1.iRight);
    return localNodeAVLDiskLarge2;
  }
  
  NodeAVL getParent(PersistentStore paramPersistentStore)
  {
    NodeAVLDiskLarge localNodeAVLDiskLarge1 = this;
    RowAVLDisk localRowAVLDisk = (RowAVLDisk)paramPersistentStore.get(this.row, false);
    localNodeAVLDiskLarge1 = (NodeAVLDiskLarge)localRowAVLDisk.getNode(this.iId);
    if (localNodeAVLDiskLarge1.iParent == -1L) {
      return null;
    }
    NodeAVLDiskLarge localNodeAVLDiskLarge2 = findNode(paramPersistentStore, this.iParent);
    return localNodeAVLDiskLarge2;
  }
  
  public int getBalance(PersistentStore paramPersistentStore)
  {
    NodeAVLDiskLarge localNodeAVLDiskLarge = this;
    RowAVLDisk localRowAVLDisk = (RowAVLDisk)paramPersistentStore.get(this.row, false);
    localNodeAVLDiskLarge = (NodeAVLDiskLarge)localRowAVLDisk.getNode(this.iId);
    return localNodeAVLDiskLarge.iBalance;
  }
  
  boolean isRoot(PersistentStore paramPersistentStore)
  {
    NodeAVLDiskLarge localNodeAVLDiskLarge = this;
    RowAVLDisk localRowAVLDisk = (RowAVLDisk)paramPersistentStore.get(this.row, false);
    localNodeAVLDiskLarge = (NodeAVLDiskLarge)localRowAVLDisk.getNode(this.iId);
    return localNodeAVLDiskLarge.iParent == -1L;
  }
  
  boolean isFromLeft(PersistentStore paramPersistentStore)
  {
    NodeAVLDiskLarge localNodeAVLDiskLarge1 = this;
    RowAVLDisk localRowAVLDisk = (RowAVLDisk)paramPersistentStore.get(this.row, false);
    localNodeAVLDiskLarge1 = (NodeAVLDiskLarge)localRowAVLDisk.getNode(this.iId);
    if (localNodeAVLDiskLarge1.iParent == -1L) {
      return true;
    }
    NodeAVLDiskLarge localNodeAVLDiskLarge2 = findNode(paramPersistentStore, this.iParent);
    return localRowAVLDisk.getPos() == localNodeAVLDiskLarge2.iLeft;
  }
  
  public NodeAVL child(PersistentStore paramPersistentStore, boolean paramBoolean)
  {
    return paramBoolean ? getLeft(paramPersistentStore) : getRight(paramPersistentStore);
  }
  
  NodeAVL setParent(PersistentStore paramPersistentStore, NodeAVL paramNodeAVL)
  {
    NodeAVLDiskLarge localNodeAVLDiskLarge = this;
    RowAVLDisk localRowAVLDisk = (RowAVLDisk)paramPersistentStore.get(this.row, true);
    localNodeAVLDiskLarge = (NodeAVLDiskLarge)localRowAVLDisk.getNode(this.iId);
    localRowAVLDisk.setNodesChanged();
    localNodeAVLDiskLarge.iParent = (paramNodeAVL == null ? -1L : paramNodeAVL.getPos());
    localRowAVLDisk.keepInMemory(false);
    return localNodeAVLDiskLarge;
  }
  
  public NodeAVL setBalance(PersistentStore paramPersistentStore, int paramInt)
  {
    NodeAVLDiskLarge localNodeAVLDiskLarge = this;
    RowAVLDisk localRowAVLDisk = (RowAVLDisk)paramPersistentStore.get(this.row, true);
    localNodeAVLDiskLarge = (NodeAVLDiskLarge)localRowAVLDisk.getNode(this.iId);
    localRowAVLDisk.setNodesChanged();
    localNodeAVLDiskLarge.iBalance = paramInt;
    localRowAVLDisk.keepInMemory(false);
    return localNodeAVLDiskLarge;
  }
  
  NodeAVL setLeft(PersistentStore paramPersistentStore, NodeAVL paramNodeAVL)
  {
    NodeAVLDiskLarge localNodeAVLDiskLarge = this;
    RowAVLDisk localRowAVLDisk = (RowAVLDisk)paramPersistentStore.get(this.row, true);
    localNodeAVLDiskLarge = (NodeAVLDiskLarge)localRowAVLDisk.getNode(this.iId);
    localRowAVLDisk.setNodesChanged();
    localNodeAVLDiskLarge.iLeft = (paramNodeAVL == null ? -1L : paramNodeAVL.getPos());
    localRowAVLDisk.keepInMemory(false);
    return localNodeAVLDiskLarge;
  }
  
  NodeAVL setRight(PersistentStore paramPersistentStore, NodeAVL paramNodeAVL)
  {
    NodeAVLDiskLarge localNodeAVLDiskLarge = this;
    RowAVLDisk localRowAVLDisk = (RowAVLDisk)paramPersistentStore.get(this.row, true);
    localNodeAVLDiskLarge = (NodeAVLDiskLarge)localRowAVLDisk.getNode(this.iId);
    localRowAVLDisk.setNodesChanged();
    localNodeAVLDiskLarge.iRight = (paramNodeAVL == null ? -1L : paramNodeAVL.getPos());
    localRowAVLDisk.keepInMemory(false);
    return localNodeAVLDiskLarge;
  }
  
  public NodeAVL set(PersistentStore paramPersistentStore, boolean paramBoolean, NodeAVL paramNodeAVL)
  {
    NodeAVL localNodeAVL;
    if (paramBoolean) {
      localNodeAVL = setLeft(paramPersistentStore, paramNodeAVL);
    } else {
      localNodeAVL = setRight(paramPersistentStore, paramNodeAVL);
    }
    if (paramNodeAVL != null) {
      paramNodeAVL.setParent(paramPersistentStore, this);
    }
    return localNodeAVL;
  }
  
  public void replace(PersistentStore paramPersistentStore, Index paramIndex, NodeAVL paramNodeAVL)
  {
    NodeAVLDiskLarge localNodeAVLDiskLarge = this;
    RowAVLDisk localRowAVLDisk = (RowAVLDisk)this.row;
    if (!localRowAVLDisk.keepInMemory(true))
    {
      localRowAVLDisk = (RowAVLDisk)paramPersistentStore.get(this.row, true);
      localNodeAVLDiskLarge = (NodeAVLDiskLarge)localRowAVLDisk.getNode(this.iId);
    }
    if (localNodeAVLDiskLarge.iParent == -1L)
    {
      if (paramNodeAVL != null) {
        paramNodeAVL = paramNodeAVL.setParent(paramPersistentStore, null);
      }
      paramPersistentStore.setAccessor(paramIndex, paramNodeAVL);
    }
    else
    {
      boolean bool = localNodeAVLDiskLarge.isFromLeft(paramPersistentStore);
      localNodeAVLDiskLarge.getParent(paramPersistentStore).set(paramPersistentStore, bool, paramNodeAVL);
    }
    localRowAVLDisk.keepInMemory(false);
  }
  
  boolean equals(NodeAVL paramNodeAVL)
  {
    if ((paramNodeAVL instanceof NodeAVLDiskLarge)) {
      return (this == paramNodeAVL) || (this.row.getPos() == paramNodeAVL.getPos());
    }
    return false;
  }
  
  public int getRealSize(RowOutputInterface paramRowOutputInterface)
  {
    return 16;
  }
  
  public void setInMemory(boolean paramBoolean) {}
  
  public void write(RowOutputInterface paramRowOutputInterface)
  {
    write(paramRowOutputInterface, null);
  }
  
  public void write(RowOutputInterface paramRowOutputInterface, LongLookup paramLongLookup)
  {
    long l1 = getTranslatePointer(this.iLeft, paramLongLookup);
    long l2 = getTranslatePointer(this.iRight, paramLongLookup);
    long l3 = getTranslatePointer(this.iParent, paramLongLookup);
    int i = 0;
    i |= (int)((l3 & 0xFF00000000) >> 8);
    i |= (int)((l1 & 0xFF00000000) >> 16);
    i |= (int)((l2 & 0xFF00000000) >> 24);
    if (i == 0) {
      i = this.iBalance;
    } else {
      i |= this.iBalance & 0xFF;
    }
    paramRowOutputInterface.writeInt(i);
    paramRowOutputInterface.writeInt((int)l1);
    paramRowOutputInterface.writeInt((int)l2);
    paramRowOutputInterface.writeInt((int)l3);
  }
  
  private static long getTranslatePointer(long paramLong, LongLookup paramLongLookup)
  {
    long l = 0L;
    if (paramLong != -1L) {
      if (paramLongLookup == null) {
        l = paramLong;
      } else {
        l = paramLongLookup.lookup(paramLong);
      }
    }
    return l;
  }
  
  public void restore() {}
  
  public void destroy() {}
  
  public void updateAccessCount(int paramInt) {}
  
  public int getAccessCount()
  {
    return 0;
  }
  
  public void setStorageSize(int paramInt) {}
  
  public int getStorageSize()
  {
    return 0;
  }
  
  public void setPos(long paramLong) {}
  
  public boolean hasChanged()
  {
    return false;
  }
  
  public void setChanged(boolean paramBoolean) {}
  
  public boolean isKeepInMemory()
  {
    return false;
  }
  
  public boolean keepInMemory(boolean paramBoolean)
  {
    return false;
  }
}


/* Location:              E:\java\java学习\hutubill\lib\all.jar!\org\hsqldb\index\NodeAVLDiskLarge.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */
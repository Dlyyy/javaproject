package org.hsqldb.index;

import org.hsqldb.Row;
import org.hsqldb.RowAVL;
import org.hsqldb.lib.LongLookup;
import org.hsqldb.persist.CachedObject;
import org.hsqldb.persist.PersistentStore;
import org.hsqldb.rowio.RowInputInterface;
import org.hsqldb.rowio.RowOutputInterface;

public class NodeAVL
  implements CachedObject
{
  static final int NO_POS = -1;
  public int iBalance;
  public NodeAVL nNext;
  protected NodeAVL nLeft;
  protected NodeAVL nRight;
  protected NodeAVL nParent;
  protected final Row row;
  
  NodeAVL()
  {
    this.row = null;
  }
  
  public NodeAVL(Row paramRow)
  {
    this.row = paramRow;
  }
  
  public void delete()
  {
    this.iBalance = 0;
    this.nLeft = (this.nRight = this.nParent = null);
  }
  
  NodeAVL getLeft(PersistentStore paramPersistentStore)
  {
    return this.nLeft;
  }
  
  NodeAVL setLeft(PersistentStore paramPersistentStore, NodeAVL paramNodeAVL)
  {
    this.nLeft = paramNodeAVL;
    return this;
  }
  
  public int getBalance(PersistentStore paramPersistentStore)
  {
    return this.iBalance;
  }
  
  boolean isLeft(NodeAVL paramNodeAVL)
  {
    return this.nLeft == paramNodeAVL;
  }
  
  boolean isRight(NodeAVL paramNodeAVL)
  {
    return this.nRight == paramNodeAVL;
  }
  
  NodeAVL getRight(PersistentStore paramPersistentStore)
  {
    return this.nRight;
  }
  
  NodeAVL setRight(PersistentStore paramPersistentStore, NodeAVL paramNodeAVL)
  {
    this.nRight = paramNodeAVL;
    return this;
  }
  
  NodeAVL getParent(PersistentStore paramPersistentStore)
  {
    return this.nParent;
  }
  
  boolean isRoot(PersistentStore paramPersistentStore)
  {
    return this.nParent == null;
  }
  
  NodeAVL setParent(PersistentStore paramPersistentStore, NodeAVL paramNodeAVL)
  {
    this.nParent = paramNodeAVL;
    return this;
  }
  
  public NodeAVL setBalance(PersistentStore paramPersistentStore, int paramInt)
  {
    this.iBalance = paramInt;
    return this;
  }
  
  boolean isFromLeft(PersistentStore paramPersistentStore)
  {
    if (this.nParent == null) {
      return true;
    }
    return this == this.nParent.nLeft;
  }
  
  public NodeAVL child(PersistentStore paramPersistentStore, boolean paramBoolean)
  {
    return paramBoolean ? getLeft(paramPersistentStore) : getRight(paramPersistentStore);
  }
  
  public NodeAVL set(PersistentStore paramPersistentStore, boolean paramBoolean, NodeAVL paramNodeAVL)
  {
    if (paramBoolean) {
      this.nLeft = paramNodeAVL;
    } else {
      this.nRight = paramNodeAVL;
    }
    if (paramNodeAVL != null) {
      paramNodeAVL.nParent = this;
    }
    return this;
  }
  
  public void replace(PersistentStore paramPersistentStore, Index paramIndex, NodeAVL paramNodeAVL)
  {
    if (this.nParent == null)
    {
      if (paramNodeAVL != null) {
        paramNodeAVL = paramNodeAVL.setParent(paramPersistentStore, null);
      }
      paramPersistentStore.setAccessor(paramIndex, paramNodeAVL);
    }
    else
    {
      this.nParent.set(paramPersistentStore, isFromLeft(paramPersistentStore), paramNodeAVL);
    }
  }
  
  boolean equals(NodeAVL paramNodeAVL)
  {
    return paramNodeAVL == this;
  }
  
  public void setInMemory(boolean paramBoolean) {}
  
  public int getDefaultCapacity()
  {
    return 0;
  }
  
  public void read(RowInputInterface paramRowInputInterface) {}
  
  public void write(RowOutputInterface paramRowOutputInterface) {}
  
  public void write(RowOutputInterface paramRowOutputInterface, LongLookup paramLongLookup) {}
  
  public long getPos()
  {
    return 0L;
  }
  
  public RowAVL getRow(PersistentStore paramPersistentStore)
  {
    return (RowAVL)this.row;
  }
  
  protected Object[] getData(PersistentStore paramPersistentStore)
  {
    return this.row.getData();
  }
  
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
  
  public final boolean isInvariable()
  {
    return false;
  }
  
  public final boolean isBlock()
  {
    return false;
  }
  
  public void setPos(long paramLong) {}
  
  public boolean isNew()
  {
    return false;
  }
  
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
    return true;
  }
  
  public boolean isInMemory()
  {
    return false;
  }
  
  public void restore() {}
  
  public void destroy() {}
  
  public int getRealSize(RowOutputInterface paramRowOutputInterface)
  {
    return 0;
  }
  
  public boolean isMemory()
  {
    return true;
  }
}


/* Location:              E:\java\java学习\hutubill\lib\all.jar!\org\hsqldb\index\NodeAVL.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */
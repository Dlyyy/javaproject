package org.hsqldb.persist;

import java.io.IOException;
import java.util.concurrent.atomic.AtomicLong;
import org.hsqldb.HsqlException;
import org.hsqldb.Row;
import org.hsqldb.RowAVL;
import org.hsqldb.RowAVLDisk;
import org.hsqldb.RowAction;
import org.hsqldb.Session;
import org.hsqldb.SessionData;
import org.hsqldb.TableBase;
import org.hsqldb.error.Error;
import org.hsqldb.index.Index;
import org.hsqldb.index.IndexAVL;
import org.hsqldb.index.NodeAVL;
import org.hsqldb.lib.ArrayUtil;
import org.hsqldb.navigator.RowIterator;
import org.hsqldb.rowio.RowInputInterface;
import org.hsqldb.rowio.RowOutputInterface;

public class RowStoreAVLHybrid
  extends RowStoreAVL
  implements PersistentStore
{
  DataFileCache cache;
  private int maxMemoryRowCount;
  private boolean useDisk;
  boolean isCached;
  int rowIdSequence = 0;
  
  public RowStoreAVLHybrid(Session paramSession, TableBase paramTableBase, boolean paramBoolean)
  {
    this.table = paramTableBase;
    this.maxMemoryRowCount = paramSession.getResultMemoryRowCount();
    this.useDisk = paramBoolean;
    if (this.maxMemoryRowCount == 0) {
      this.useDisk = false;
    }
    if (paramTableBase.getTableType() == 9) {
      setTimestamp(paramSession.getActionTimestamp());
    }
    resetAccessorKeys(paramSession, paramTableBase.getIndexList());
    this.nullsList = new boolean[paramTableBase.getColumnCount()];
  }
  
  public boolean isMemory()
  {
    return !this.isCached;
  }
  
  public void setMemory(boolean paramBoolean)
  {
    this.useDisk = (!paramBoolean);
  }
  
  public void set(CachedObject paramCachedObject) {}
  
  public CachedObject get(long paramLong)
  {
    try
    {
      if (this.isCached) {
        return this.cache.get(paramLong, this, false);
      }
      throw Error.runtimeError(201, "RowStoreAVLHybrid");
    }
    catch (HsqlException localHsqlException) {}
    return null;
  }
  
  public CachedObject get(long paramLong, boolean paramBoolean)
  {
    try
    {
      if (this.isCached) {
        return this.cache.get(paramLong, this, paramBoolean);
      }
      throw Error.runtimeError(201, "RowStoreAVLHybrid");
    }
    catch (HsqlException localHsqlException) {}
    return null;
  }
  
  public CachedObject get(CachedObject paramCachedObject, boolean paramBoolean)
  {
    try
    {
      if (this.isCached) {
        return this.cache.get(paramCachedObject, this, paramBoolean);
      }
      return paramCachedObject;
    }
    catch (HsqlException localHsqlException) {}
    return null;
  }
  
  public void add(Session paramSession, CachedObject paramCachedObject, boolean paramBoolean)
  {
    if (this.isCached)
    {
      int i = paramCachedObject.getRealSize(this.cache.rowOut);
      i += this.indexList.length * 16;
      i = this.cache.rowOut.getStorageSize(i);
      paramCachedObject.setStorageSize(i);
      long l = this.tableSpace.getFilePosition(i, false);
      paramCachedObject.setPos(l);
      this.cache.add(paramCachedObject, false);
    }
    Object[] arrayOfObject = ((Row)paramCachedObject).getData();
    for (int j = 0; j < this.nullsList.length; j++) {
      if (arrayOfObject[j] == null) {
        this.nullsList[j] = true;
      }
    }
  }
  
  public CachedObject get(RowInputInterface paramRowInputInterface)
  {
    try
    {
      if (this.isCached) {
        return new RowAVLDisk(this.table, paramRowInputInterface);
      }
    }
    catch (HsqlException localHsqlException)
    {
      return null;
    }
    catch (IOException localIOException)
    {
      return null;
    }
    return null;
  }
  
  public CachedObject getNewCachedObject(Session paramSession, Object paramObject, boolean paramBoolean)
  {
    if ((!this.isCached) && (this.useDisk) && (this.elementCount.get() >= this.maxMemoryRowCount)) {
      changeToDiskTable(paramSession);
    }
    Object localObject;
    if (this.isCached)
    {
      localObject = new RowAVLDisk(this.table, (Object[])paramObject, this);
    }
    else
    {
      int i = this.rowIdSequence++;
      localObject = new RowAVL(this.table, (Object[])paramObject, i, this);
    }
    add(paramSession, (CachedObject)localObject, paramBoolean);
    return (CachedObject)localObject;
  }
  
  public void indexRow(Session paramSession, Row paramRow)
  {
    try
    {
      paramRow = (Row)get(paramRow, true);
      super.indexRow(paramSession, paramRow);
    }
    catch (HsqlException localHsqlException)
    {
      throw localHsqlException;
    }
    finally
    {
      paramRow.keepInMemory(false);
    }
  }
  
  public void removeAll()
  {
    if (!this.isCached) {
      destroy();
    }
    this.elementCount.set(0L);
    ArrayUtil.fillArray(this.accessorList, null);
    for (int i = 0; i < this.nullsList.length; i++) {
      this.nullsList[i] = false;
    }
  }
  
  public void remove(CachedObject paramCachedObject)
  {
    if (this.isCached) {
      this.cache.remove(paramCachedObject);
    }
  }
  
  public void commitPersistence(CachedObject paramCachedObject) {}
  
  public void postCommitAction(Session paramSession, RowAction paramRowAction) {}
  
  public void commitRow(Session paramSession, Row paramRow, int paramInt1, int paramInt2)
  {
    switch (paramInt1)
    {
    case 2: 
      remove(paramRow);
      break;
    case 1: 
      break;
    case 4: 
      remove(paramRow);
      break;
    case 3: 
      throw Error.runtimeError(201, "RowStore");
    }
  }
  
  public void rollbackRow(Session paramSession, Row paramRow, int paramInt1, int paramInt2)
  {
    switch (paramInt1)
    {
    case 2: 
      paramRow = (Row)get(paramRow, true);
      ((RowAVL)paramRow).setNewNodes(this);
      paramRow.keepInMemory(false);
      indexRow(paramSession, paramRow);
      break;
    case 1: 
      delete(paramSession, paramRow);
      remove(paramRow);
      break;
    case 4: 
      remove(paramRow);
    }
  }
  
  public DataFileCache getCache()
  {
    return this.cache;
  }
  
  public void setCache(DataFileCache paramDataFileCache)
  {
    throw Error.runtimeError(201, "RowStoreAVLHybrid");
  }
  
  public void release()
  {
    if (!this.isCached) {
      destroy();
    }
    if (this.isCached)
    {
      this.cache.adjustStoreCount(-1);
      this.cache = null;
      this.isCached = false;
    }
    this.elementCount.set(0L);
    ArrayUtil.fillArray(this.accessorList, null);
  }
  
  public CachedObject getAccessor(Index paramIndex)
  {
    NodeAVL localNodeAVL = (NodeAVL)this.accessorList[paramIndex.getPosition()];
    if (localNodeAVL == null) {
      return null;
    }
    RowAVL localRowAVL = (RowAVL)get(localNodeAVL.getRow(this), false);
    localNodeAVL = localRowAVL.getNode(paramIndex.getPosition());
    this.accessorList[paramIndex.getPosition()] = localNodeAVL;
    return localNodeAVL;
  }
  
  public synchronized void resetAccessorKeys(Session paramSession, Index[] paramArrayOfIndex)
  {
    if ((this.indexList.length == 0) || (this.accessorList[0] == null))
    {
      this.indexList = paramArrayOfIndex;
      this.accessorList = new CachedObject[this.indexList.length];
      return;
    }
    if (this.isCached) {
      throw Error.runtimeError(201, "RowStoreAVLHybrid");
    }
    super.resetAccessorKeys(paramSession, paramArrayOfIndex);
  }
  
  public boolean hasNull(int paramInt)
  {
    return this.nullsList[paramInt];
  }
  
  public final void changeToDiskTable(Session paramSession)
  {
    this.cache = paramSession.sessionData.persistentStoreCollection.getSessionDataCache();
    this.maxMemoryRowCount = Integer.MAX_VALUE;
    if (this.cache == null) {
      return;
    }
    this.tableSpace = this.cache.spaceManager.getTableSpace(7);
    this.isCached = true;
    this.cache.adjustStoreCount(1);
    if (this.elementCount.get() == 0L) {
      return;
    }
    IndexAVL localIndexAVL = (IndexAVL)this.indexList[0];
    NodeAVL localNodeAVL = (NodeAVL)this.accessorList[0];
    RowIterator localRowIterator = this.table.rowIterator(this);
    ArrayUtil.fillArray(this.accessorList, null);
    ArrayUtil.fillArray(this.nullsList, false);
    this.elementCount.set(0L);
    while (localRowIterator.hasNext())
    {
      Row localRow1 = localRowIterator.getNextRow();
      Row localRow2 = (Row)getNewCachedObject(paramSession, localRow1.getData(), false);
      indexRow(paramSession, localRow2);
    }
    localIndexAVL.unlinkNodes(localNodeAVL);
  }
}


/* Location:              E:\java\java学习\hutubill\lib\all.jar!\org\hsqldb\persist\RowStoreAVLHybrid.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */
package org.hsqldb.persist;

import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import org.hsqldb.ColumnSchema;
import org.hsqldb.Database;
import org.hsqldb.HsqlException;
import org.hsqldb.Row;
import org.hsqldb.RowAVL;
import org.hsqldb.RowAction;
import org.hsqldb.Session;
import org.hsqldb.SessionData;
import org.hsqldb.Table;
import org.hsqldb.TableBase;
import org.hsqldb.TransactionManager;
import org.hsqldb.error.Error;
import org.hsqldb.index.Index;
import org.hsqldb.index.IndexAVL;
import org.hsqldb.index.NodeAVL;
import org.hsqldb.lib.ArrayUtil;
import org.hsqldb.navigator.RowIterator;
import org.hsqldb.rowio.RowInputInterface;
import org.hsqldb.types.Type;

public abstract class RowStoreAVL
  implements PersistentStore
{
  Database database;
  TableSpaceManager tableSpace;
  Index[] indexList = Index.emptyArray;
  CachedObject[] accessorList = CachedObject.emptyArray;
  TableBase table;
  long baseElementCount;
  AtomicLong elementCount = new AtomicLong();
  long storageSize;
  boolean[] nullsList;
  double[][] searchCost;
  boolean isSchemaStore;
  ReadWriteLock lock;
  Lock readLock;
  Lock writeLock;
  private long timestamp;
  PersistentStore[] subStores = PersistentStore.emptyArray;
  
  public boolean isRowStore()
  {
    return true;
  }
  
  public boolean isRowSet()
  {
    return false;
  }
  
  public TableBase getTable()
  {
    return this.table;
  }
  
  public long getTimestamp()
  {
    return this.timestamp;
  }
  
  public void setTimestamp(long paramLong)
  {
    this.timestamp = paramLong;
  }
  
  public abstract boolean isMemory();
  
  public void setMemory(boolean paramBoolean) {}
  
  public abstract void set(CachedObject paramCachedObject);
  
  public abstract CachedObject get(long paramLong, boolean paramBoolean);
  
  public abstract CachedObject get(CachedObject paramCachedObject, boolean paramBoolean);
  
  public CachedObject getRow(long paramLong, boolean[] paramArrayOfBoolean)
  {
    return get(paramLong, false);
  }
  
  public int compare(Session paramSession, long paramLong)
  {
    throw Error.runtimeError(201, "RowStoreAVL");
  }
  
  public abstract void add(Session paramSession, CachedObject paramCachedObject, boolean paramBoolean);
  
  public final void add(CachedObject paramCachedObject, boolean paramBoolean) {}
  
  public boolean canRead(Session paramSession, long paramLong, int paramInt, int[] paramArrayOfInt)
  {
    return true;
  }
  
  public boolean canRead(Session paramSession, CachedObject paramCachedObject, int paramInt, int[] paramArrayOfInt)
  {
    RowAction localRowAction = ((Row)paramCachedObject).rowAction;
    if (localRowAction == null) {
      return true;
    }
    return localRowAction.canRead(paramSession, paramInt);
  }
  
  public abstract CachedObject get(RowInputInterface paramRowInputInterface);
  
  public CachedObject get(CachedObject paramCachedObject, RowInputInterface paramRowInputInterface)
  {
    return paramCachedObject;
  }
  
  public CachedObject getNewInstance(int paramInt)
  {
    throw Error.runtimeError(201, "RowStoreAVL");
  }
  
  public int getDefaultObjectSize()
  {
    throw Error.runtimeError(201, "RowStoreAVL");
  }
  
  public abstract CachedObject getNewCachedObject(Session paramSession, Object paramObject, boolean paramBoolean);
  
  public abstract void removeAll();
  
  public abstract void remove(CachedObject paramCachedObject);
  
  public abstract void commitPersistence(CachedObject paramCachedObject);
  
  public abstract void postCommitAction(Session paramSession, RowAction paramRowAction);
  
  public abstract DataFileCache getCache();
  
  public TableSpaceManager getSpaceManager()
  {
    return this.tableSpace;
  }
  
  public void setSpaceManager(TableSpaceManager paramTableSpaceManager)
  {
    this.tableSpace = paramTableSpaceManager;
  }
  
  public abstract void setCache(DataFileCache paramDataFileCache);
  
  public abstract void release();
  
  public PersistentStore getAccessorStore(Index paramIndex)
  {
    return null;
  }
  
  public CachedObject getAccessor(Index paramIndex)
  {
    int i = paramIndex.getPosition();
    if (i >= this.accessorList.length) {
      throw Error.runtimeError(201, "RowStoreAVL");
    }
    return this.accessorList[i];
  }
  
  public void delete(Session paramSession, Row paramRow)
  {
    writeLock();
    try
    {
      for (int i = 0; i < this.indexList.length; i++) {
        this.indexList[i].delete(paramSession, this, paramRow);
      }
      for (i = 0; i < this.subStores.length; i++) {
        this.subStores[i].delete(paramSession, paramRow);
      }
      paramRow.delete(this);
      long l = this.elementCount.decrementAndGet();
      if ((l > 16384L) && (l < this.baseElementCount / 2L))
      {
        this.baseElementCount = l;
        this.searchCost = ((double[][])null);
      }
    }
    finally
    {
      writeUnlock();
    }
  }
  
  public void indexRow(Session paramSession, Row paramRow)
  {
    long l1 = 0;
    writeLock();
    try
    {
      while (l1 < this.indexList.length)
      {
        this.indexList[l1].insert(paramSession, this, paramRow);
        l1++;
      }
      int i = 0;
      try
      {
        for (i = 0; i < this.subStores.length; i++) {
          this.subStores[i].indexRow(paramSession, paramRow);
        }
      }
      catch (HsqlException localHsqlException2)
      {
        int j = i;
        for (i = 0; i < j; i++) {
          this.subStores[i].delete(paramSession, paramRow);
        }
        throw localHsqlException2;
      }
      l2 = this.elementCount.incrementAndGet();
      if ((l2 > 16384L) && (l2 > this.baseElementCount * 2L))
      {
        this.baseElementCount = l2;
        this.searchCost = ((double[][])null);
      }
    }
    catch (HsqlException localHsqlException1)
    {
      long l2 = l1;
      for (l1 = 0; l1 < l2; l1++) {
        this.indexList[l1].delete(paramSession, this, paramRow);
      }
      remove(paramRow);
      throw localHsqlException1;
    }
    catch (Throwable localThrowable)
    {
      long l3 = l1;
      for (l1 = 0; l1 < l3; l1++) {
        this.indexList[l1].delete(paramSession, this, paramRow);
      }
      throw Error.error(458, localThrowable);
    }
    finally
    {
      writeUnlock();
    }
  }
  
  public final void indexRows(Session paramSession)
  {
    writeLock();
    try
    {
      for (int i = 1; i < this.indexList.length; i++) {
        setAccessor(this.indexList[i], null);
      }
      RowIterator localRowIterator = rowIterator();
      while (localRowIterator.hasNext())
      {
        Row localRow = localRowIterator.getNextRow();
        ((RowAVL)localRow).clearNonPrimaryNodes();
        for (int j = 1; j < this.indexList.length; j++) {
          this.indexList[j].insert(paramSession, this, localRow);
        }
      }
    }
    finally
    {
      writeUnlock();
    }
  }
  
  public final RowIterator rowIterator()
  {
    Index localIndex = this.indexList[0];
    for (int i = 0; i < this.indexList.length; i++) {
      if (this.indexList[i].isClustered())
      {
        localIndex = this.indexList[i];
        break;
      }
    }
    return localIndex.firstRow(this);
  }
  
  public void setAccessor(Index paramIndex, CachedObject paramCachedObject)
  {
    this.accessorList[paramIndex.getPosition()] = paramCachedObject;
  }
  
  public void setAccessor(Index paramIndex, long paramLong) {}
  
  public void resetAccessorKeys(Session paramSession, Index[] paramArrayOfIndex)
  {
    Index[] arrayOfIndex1 = this.indexList;
    this.searchCost = ((double[][])null);
    if ((this.indexList.length == 0) || (this.accessorList[0] == null))
    {
      this.indexList = paramArrayOfIndex;
      this.accessorList = new CachedObject[this.indexList.length];
      return;
    }
    if (this.indexList == paramArrayOfIndex) {
      return;
    }
    CachedObject[] arrayOfCachedObject = this.accessorList;
    int i = this.indexList.length;
    int j = paramArrayOfIndex.length - this.indexList.length;
    int k = 0;
    if (j < -1) {
      throw Error.runtimeError(201, "RowStoreAVL");
    }
    if (j == -1)
    {
      i = paramArrayOfIndex.length;
    }
    else
    {
      if (j == 0) {
        return;
      }
      if (j != 1)
      {
        while ((k < i) && (this.indexList[k] == paramArrayOfIndex[k])) {
          k++;
        }
        Index[] arrayOfIndex2 = (Index[])ArrayUtil.toAdjustedArray(this.indexList, null, k, 1);
        arrayOfIndex2[k] = paramArrayOfIndex[k];
        resetAccessorKeys(paramSession, arrayOfIndex2);
        resetAccessorKeys(paramSession, paramArrayOfIndex);
        return;
      }
    }
    while ((k < i) && (this.indexList[k] == paramArrayOfIndex[k])) {
      k++;
    }
    this.accessorList = ((CachedObject[])ArrayUtil.toAdjustedArray(this.accessorList, null, k, j));
    this.indexList = paramArrayOfIndex;
    try
    {
      if (j > 0) {
        insertIndexNodes(paramSession, this.indexList[0], this.indexList[k]);
      } else {
        dropIndexFromRows(this.indexList[0], arrayOfIndex1[k]);
      }
    }
    catch (HsqlException localHsqlException)
    {
      this.accessorList = arrayOfCachedObject;
      this.indexList = arrayOfIndex1;
      throw localHsqlException;
    }
  }
  
  public Index[] getAccessorKeys()
  {
    return this.indexList;
  }
  
  public synchronized double searchCost(Session paramSession, Index paramIndex, int paramInt1, int paramInt2)
  {
    if (paramInt1 == 0) {
      return this.elementCount.get();
    }
    if (paramInt2 != 40) {
      return this.elementCount.get() / 2.0D;
    }
    if ((paramIndex.isUnique()) && (paramInt1 == paramIndex.getColumnCount())) {
      return 1.0D;
    }
    int i = paramIndex.getPosition();
    if ((this.searchCost == null) || (this.searchCost.length != this.indexList.length)) {
      this.searchCost = new double[this.indexList.length][];
    }
    if (this.searchCost[i] == null) {
      this.searchCost[i] = this.indexList[i].searchCost(paramSession, this);
    }
    return this.searchCost[paramIndex.getPosition()][(paramInt1 - 1)];
  }
  
  public long elementCount()
  {
    Index localIndex = this.indexList[0];
    if (this.elementCount.get() < 0L)
    {
      readLock();
      try
      {
        this.elementCount.set(localIndex.getNodeCount(null, this));
      }
      finally
      {
        readUnlock();
      }
    }
    return this.elementCount.get();
  }
  
  public long elementCount(Session paramSession)
  {
    if (paramSession != null)
    {
      Index localIndex = this.indexList[0];
      if (paramSession.database.txManager.isMVRows()) {
        switch (this.table.getTableType())
        {
        case 4: 
        case 5: 
        case 7: 
          readLock();
          try
          {
            long l = localIndex.getNodeCount(paramSession, this);
            return l;
          }
          finally
          {
            readUnlock();
          }
        }
      }
    }
    return elementCount();
  }
  
  public long elementCountUnique(Index paramIndex)
  {
    return 0L;
  }
  
  public void setElementCount(Index paramIndex, long paramLong1, long paramLong2)
  {
    this.elementCount.set(paramLong1);
  }
  
  public boolean hasNull(int paramInt)
  {
    return false;
  }
  
  public void moveDataToSpace(Session paramSession) {}
  
  public final void moveData(Session paramSession, PersistentStore paramPersistentStore, int paramInt1, int paramInt2)
  {
    Type localType1 = null;
    Type localType2 = null;
    Object localObject1 = null;
    Object localObject2;
    if ((paramInt2 >= 0) && (paramInt1 != -1))
    {
      localObject2 = ((Table)this.table).getColumn(paramInt1);
      localObject1 = ((ColumnSchema)localObject2).getDefaultValue(paramSession);
      localType2 = this.table.getColumnTypes()[paramInt1];
    }
    if ((paramInt2 <= 0) && (paramInt1 != -1)) {
      localType1 = paramPersistentStore.getTable().getColumnTypes()[paramInt1];
    }
    try
    {
      localObject2 = (Table)this.table;
      RowIterator localRowIterator = paramPersistentStore.rowIterator();
      Row localRow1;
      Object[] arrayOfObject;
      Object localObject3;
      while (localRowIterator.hasNext())
      {
        localRow1 = localRowIterator.getNextRow();
        arrayOfObject = localRow1.getData();
        localObject3 = ((Table)localObject2).getEmptyRowData();
        Object localObject4 = null;
        if ((paramInt2 == 0) && (paramInt1 != -1))
        {
          localObject4 = arrayOfObject[paramInt1];
          localObject1 = localType2.convertToType(paramSession, localObject4, localType1);
        }
        ArrayUtil.copyAdjustArray(arrayOfObject, localObject3, localObject1, paramInt1, paramInt2);
        ((Table)localObject2).systemSetIdentityColumn(paramSession, (Object[])localObject3);
        if (((Table)localObject2).hasGeneratedColumn()) {
          ((Table)localObject2).setGeneratedColumns(paramSession, (Object[])localObject3);
        }
        ((Table)localObject2).enforceTypeLimits(paramSession, (Object[])localObject3);
        ((Table)localObject2).enforceRowConstraints(paramSession, (Object[])localObject3);
        Row localRow2 = (Row)getNewCachedObject(paramSession, localObject3, false);
        indexRow(paramSession, localRow2);
      }
      if (((Table)localObject2).isTemp()) {
        return;
      }
      if ((localType1 != null) && (localType1.isLobType()))
      {
        localRowIterator = paramPersistentStore.rowIterator();
        while (localRowIterator.hasNext())
        {
          localRow1 = localRowIterator.getNextRow();
          arrayOfObject = localRow1.getData();
          localObject3 = arrayOfObject[paramInt1];
          if (localObject3 != null) {
            paramSession.sessionData.adjustLobUsageCount(localObject3, -1);
          }
        }
      }
      if ((localType2 != null) && (localType2.isLobType()))
      {
        localRowIterator = rowIterator();
        while (localRowIterator.hasNext())
        {
          localRow1 = localRowIterator.getNextRow();
          arrayOfObject = localRow1.getData();
          localObject3 = arrayOfObject[paramInt1];
          if (localObject3 != null) {
            paramSession.sessionData.adjustLobUsageCount(localObject3, 1);
          }
        }
      }
    }
    catch (OutOfMemoryError localOutOfMemoryError)
    {
      throw Error.error(460);
    }
  }
  
  public void reindex(Session paramSession, Index paramIndex)
  {
    writeLock();
    try
    {
      setAccessor(paramIndex, null);
      RowIterator localRowIterator = this.table.rowIterator(this);
      while (localRowIterator.hasNext())
      {
        RowAVL localRowAVL = (RowAVL)localRowIterator.getNextRow();
        localRowAVL.getNode(paramIndex.getPosition()).delete();
        paramIndex.insert(paramSession, this, localRowAVL);
      }
    }
    finally
    {
      writeUnlock();
    }
  }
  
  public void setReadOnly(boolean paramBoolean) {}
  
  public void readLock() {}
  
  public void readUnlock() {}
  
  public void writeLock() {}
  
  public void writeUnlock() {}
  
  void dropIndexFromRows(Index paramIndex1, Index paramIndex2)
  {
    RowIterator localRowIterator = paramIndex1.firstRow(this);
    int i = paramIndex2.getPosition() - 1;
    while (localRowIterator.hasNext())
    {
      Row localRow = localRowIterator.getNextRow();
      int j = i - 1;
      for (NodeAVL localNodeAVL = ((RowAVL)localRow).getNode(0); j-- > 0; localNodeAVL = localNodeAVL.nNext) {}
      localNodeAVL.nNext = localNodeAVL.nNext.nNext;
    }
    localRowIterator.release();
  }
  
  boolean insertIndexNodes(Session paramSession, Index paramIndex1, Index paramIndex2)
  {
    writeLock();
    try
    {
      int i = paramIndex2.getPosition();
      RowIterator localRowIterator = paramIndex1.firstRow(this);
      int j = 0;
      Object localObject1 = null;
      try
      {
        while (localRowIterator.hasNext())
        {
          Row localRow1 = localRowIterator.getNextRow();
          ((RowAVL)localRow1).insertNode(i);
          j++;
          paramIndex2.insert(paramSession, this, localRow1);
        }
        localRowIterator.release();
        boolean bool = true;
        return bool;
      }
      catch (OutOfMemoryError localOutOfMemoryError)
      {
        localObject1 = Error.error(460);
      }
      catch (HsqlException localHsqlException)
      {
        localObject1 = localHsqlException;
      }
      localRowIterator = paramIndex1.firstRow(this);
      for (int k = 0; k < j; k++)
      {
        Row localRow2 = localRowIterator.getNextRow();
        NodeAVL localNodeAVL = ((RowAVL)localRow2).getNode(0);
        int m = i;
        for (;;)
        {
          m--;
          if (m <= 0) {
            break;
          }
          localNodeAVL = localNodeAVL.nNext;
        }
        localNodeAVL.nNext = localNodeAVL.nNext.nNext;
      }
      localRowIterator.release();
      throw ((Throwable)localObject1);
    }
    finally
    {
      writeUnlock();
    }
  }
  
  void destroy()
  {
    if (this.indexList.length == 0) {
      return;
    }
    IndexAVL localIndexAVL = (IndexAVL)this.indexList[0];
    NodeAVL localNodeAVL = (NodeAVL)this.accessorList[0];
    localIndexAVL.unlinkNodes(localNodeAVL);
  }
}


/* Location:              E:\java\java学习\hutubill\lib\all.jar!\org\hsqldb\persist\RowStoreAVL.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */
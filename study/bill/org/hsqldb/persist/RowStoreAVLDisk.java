package org.hsqldb.persist;

import java.io.IOException;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import org.hsqldb.Database;
import org.hsqldb.HsqlException;
import org.hsqldb.HsqlNameManager.HsqlName;
import org.hsqldb.Row;
import org.hsqldb.RowAVL;
import org.hsqldb.RowAVLDisk;
import org.hsqldb.RowAVLDiskLarge;
import org.hsqldb.RowAction;
import org.hsqldb.Session;
import org.hsqldb.Table;
import org.hsqldb.TableBase;
import org.hsqldb.TransactionManager;
import org.hsqldb.error.Error;
import org.hsqldb.index.Index;
import org.hsqldb.index.NodeAVL;
import org.hsqldb.lib.ArrayUtil;
import org.hsqldb.lib.DoubleIntIndex;
import org.hsqldb.navigator.RowIterator;
import org.hsqldb.rowio.RowInputInterface;
import org.hsqldb.rowio.RowOutputInterface;

public class RowStoreAVLDisk
  extends RowStoreAVL
  implements PersistentStore
{
  DataFileCache cache;
  RowOutputInterface rowOut;
  boolean largeData;
  
  public RowStoreAVLDisk(DataFileCache paramDataFileCache, Table paramTable)
  {
    this(paramTable);
    this.cache = paramDataFileCache;
    this.rowOut = paramDataFileCache.rowOut.duplicate();
    paramDataFileCache.adjustStoreCount(1);
    this.largeData = this.database.logger.propLargeData;
    this.tableSpace = paramDataFileCache.spaceManager.getTableSpace(paramTable.getSpaceID());
    this.lock = new ReentrantReadWriteLock();
    this.readLock = this.lock.readLock();
    this.writeLock = this.lock.writeLock();
  }
  
  protected RowStoreAVLDisk(Table paramTable)
  {
    this.database = paramTable.database;
    this.table = paramTable;
    this.indexList = paramTable.getIndexList();
    this.accessorList = new CachedObject[this.indexList.length];
    this.largeData = (this.database.logger.getDataFileFactor() > 1);
  }
  
  public boolean isMemory()
  {
    return false;
  }
  
  public void set(CachedObject paramCachedObject)
  {
    this.database.txManager.setTransactionInfo(this, paramCachedObject);
  }
  
  public CachedObject get(long paramLong)
  {
    CachedObject localCachedObject = this.cache.get(paramLong, this, false);
    return localCachedObject;
  }
  
  public CachedObject get(long paramLong, boolean paramBoolean)
  {
    CachedObject localCachedObject = this.cache.get(paramLong, this, paramBoolean);
    return localCachedObject;
  }
  
  public CachedObject get(CachedObject paramCachedObject, boolean paramBoolean)
  {
    paramCachedObject = this.cache.get(paramCachedObject, this, paramBoolean);
    return paramCachedObject;
  }
  
  public void add(Session paramSession, CachedObject paramCachedObject, boolean paramBoolean)
  {
    int i = paramCachedObject.getRealSize(this.rowOut);
    i += this.indexList.length * 16;
    i = this.rowOut.getStorageSize(i);
    paramCachedObject.setStorageSize(i);
    long l = this.tableSpace.getFilePosition(i, false);
    paramCachedObject.setPos(l);
    if (paramBoolean)
    {
      RowAction.addInsertAction(paramSession, this.table, (Row)paramCachedObject);
      this.database.txManager.addTransactionInfo(paramCachedObject);
    }
    this.cache.add(paramCachedObject, false);
    this.storageSize += i;
  }
  
  public CachedObject get(RowInputInterface paramRowInputInterface)
  {
    try
    {
      if (this.largeData) {
        return new RowAVLDiskLarge(this.table, paramRowInputInterface);
      }
      return new RowAVLDisk(this.table, paramRowInputInterface);
    }
    catch (IOException localIOException)
    {
      throw Error.error(466, localIOException);
    }
  }
  
  public CachedObject getNewCachedObject(Session paramSession, Object paramObject, boolean paramBoolean)
  {
    Object localObject;
    if (this.largeData) {
      localObject = new RowAVLDiskLarge(this.table, (Object[])paramObject, this);
    } else {
      localObject = new RowAVLDisk(this.table, (Object[])paramObject, this);
    }
    add(paramSession, (CachedObject)localObject, paramBoolean);
    return (CachedObject)localObject;
  }
  
  public void delete(Session paramSession, Row paramRow)
  {
    writeLock();
    try
    {
      super.delete(paramSession, paramRow);
    }
    finally
    {
      writeUnlock();
    }
  }
  
  public void indexRow(Session paramSession, Row paramRow)
  {
    writeLock();
    try
    {
      paramRow = (Row)get(paramRow, true);
      super.indexRow(paramSession, paramRow);
    }
    catch (HsqlException localHsqlException)
    {
      this.database.txManager.removeTransactionInfo(paramRow);
      throw localHsqlException;
    }
    finally
    {
      paramRow.keepInMemory(false);
      writeUnlock();
    }
  }
  
  public void removeAll()
  {
    this.elementCount.set(0L);
    this.cache.spaceManager.freeTableSpace(this.tableSpace.getSpaceID());
    ArrayUtil.fillArray(this.accessorList, null);
  }
  
  public void remove(CachedObject paramCachedObject)
  {
    this.cache.remove(paramCachedObject);
    this.tableSpace.release(paramCachedObject.getPos(), paramCachedObject.getStorageSize());
    this.storageSize -= paramCachedObject.getStorageSize();
  }
  
  public void commitPersistence(CachedObject paramCachedObject) {}
  
  public void postCommitAction(Session paramSession, RowAction paramRowAction)
  {
    if (paramRowAction.getType() == 0)
    {
      this.database.txManager.removeTransactionInfo(paramRowAction.getPos());
    }
    else if ((paramRowAction.getType() == 3) && (!paramRowAction.isDeleteComplete()))
    {
      paramRowAction.setDeleteComplete();
      Row localRow = paramRowAction.getRow();
      if (localRow == null) {
        localRow = (Row)get(paramRowAction.getPos(), false);
      }
      delete(paramSession, localRow);
      this.database.txManager.removeTransactionInfo(localRow);
      remove(localRow);
    }
  }
  
  public void commitRow(Session paramSession, Row paramRow, int paramInt1, int paramInt2)
  {
    Object[] arrayOfObject = paramRow.getData();
    switch (paramInt1)
    {
    case 2: 
      this.database.logger.writeDeleteStatement(paramSession, (Table)this.table, arrayOfObject);
      if (paramInt2 == 0) {
        remove(paramRow);
      }
      break;
    case 1: 
      this.database.logger.writeInsertStatement(paramSession, paramRow, (Table)this.table);
      break;
    case 4: 
      if (paramInt2 == 0) {
        remove(paramRow);
      }
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
      if (paramInt2 == 0)
      {
        paramRow = (Row)get(paramRow, true);
        ((RowAVL)paramRow).setNewNodes(this);
        paramRow.keepInMemory(false);
        indexRow(paramSession, paramRow);
      }
      break;
    case 1: 
      delete(paramSession, paramRow);
      this.database.txManager.removeTransactionInfo(paramRow);
      remove(paramRow);
      break;
    case 4: 
      if (paramInt2 != 0)
      {
        delete(paramSession, paramRow);
        this.database.txManager.removeTransactionInfo(paramRow);
      }
      remove(paramRow);
    }
  }
  
  public DataFileCache getCache()
  {
    return this.cache;
  }
  
  public void setCache(DataFileCache paramDataFileCache)
  {
    throw Error.runtimeError(201, "RowStoreAVLDisk");
  }
  
  public void release()
  {
    this.cache.adjustStoreCount(-1);
    this.cache = null;
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
  
  public void setAccessor(Index paramIndex, long paramLong)
  {
    Object localObject = get(paramLong, false);
    if (localObject != null)
    {
      NodeAVL localNodeAVL = ((RowAVL)localObject).getNode(paramIndex.getPosition());
      localObject = localNodeAVL;
    }
    setAccessor(paramIndex, (CachedObject)localObject);
  }
  
  public void resetAccessorKeys(Session paramSession, Index[] paramArrayOfIndex)
  {
    if ((this.indexList.length == 0) || (this.accessorList[0] == null))
    {
      this.indexList = paramArrayOfIndex;
      this.accessorList = new CachedObject[this.indexList.length];
      return;
    }
    throw Error.runtimeError(201, "RowStoreAVLDisk");
  }
  
  public void setReadOnly(boolean paramBoolean) {}
  
  public void moveDataToSpace(Session paramSession)
  {
    Table localTable = (Table)this.table;
    long l1 = elementCount();
    if (l1 == 0L) {
      return;
    }
    if (l1 > 2147483647L) {
      return;
    }
    DoubleIntIndex localDoubleIntIndex = new DoubleIntIndex((int)l1, false);
    localDoubleIntIndex.setKeysSearchTarget();
    writeLock();
    try
    {
      moveDataToSpace(this.cache, localDoubleIntIndex);
      CachedObject[] arrayOfCachedObject = new CachedObject[this.accessorList.length];
      for (int i = 0; i < this.accessorList.length; i++)
      {
        long l2 = localDoubleIntIndex.lookup(this.accessorList[i].getPos());
        arrayOfCachedObject[i] = this.cache.get(l2, this, false);
      }
      RowIterator localRowIterator = rowIterator();
      while (localRowIterator.hasNext())
      {
        Row localRow = localRowIterator.getNextRow();
        this.cache.remove(localRow);
        this.tableSpace.release(localRow.getPos(), localRow.getStorageSize());
      }
      this.accessorList = arrayOfCachedObject;
    }
    finally
    {
      writeUnlock();
    }
    this.database.logger.logDetailEvent("table written " + localTable.getName().name);
  }
  
  public void moveDataToSpace(DataFileCache paramDataFileCache, DoubleIntIndex paramDoubleIntIndex)
  {
    int i = this.table.getSpaceID();
    TableSpaceManager localTableSpaceManager = paramDataFileCache.spaceManager.getTableSpace(i);
    paramDoubleIntIndex.setKeysSearchTarget();
    RowIterator localRowIterator = this.indexList[0].firstRow(this);
    while (localRowIterator.hasNext())
    {
      Row localRow1 = localRowIterator.getNextRow();
      paramDoubleIntIndex.addUnsorted(localRow1.getPos(), localRow1.getStorageSize());
    }
    paramDoubleIntIndex.sort();
    long l;
    for (int j = 0; j < paramDoubleIntIndex.size(); j++)
    {
      l = localTableSpaceManager.getFilePosition(paramDoubleIntIndex.getValue(j), false);
      paramDoubleIntIndex.setValue(j, (int)l);
    }
    localRowIterator = this.indexList[0].firstRow(this);
    while (localRowIterator.hasNext())
    {
      Row localRow2 = localRowIterator.getNextRow();
      l = paramDoubleIntIndex.lookup(localRow2.getPos());
      paramDataFileCache.rowOut.reset();
      localRow2.write(paramDataFileCache.rowOut, paramDoubleIntIndex);
      paramDataFileCache.saveRowOutput(l);
    }
  }
  
  long getStorageSizeEstimate()
  {
    if (this.elementCount.get() == 0L) {
      return 0L;
    }
    CachedObject localCachedObject1 = getAccessor(this.indexList[0]);
    CachedObject localCachedObject2 = get(localCachedObject1.getPos());
    return localCachedObject2.getStorageSize() * this.elementCount.get();
  }
  
  public void readLock()
  {
    this.readLock.lock();
  }
  
  public void readUnlock()
  {
    this.readLock.unlock();
  }
  
  public void writeLock()
  {
    this.writeLock.lock();
  }
  
  public void writeUnlock()
  {
    this.writeLock.unlock();
  }
}


/* Location:              E:\java\java学习\hutubill\lib\all.jar!\org\hsqldb\persist\RowStoreAVLDisk.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */
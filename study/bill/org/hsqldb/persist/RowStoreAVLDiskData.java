package org.hsqldb.persist;

import java.io.IOException;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import org.hsqldb.Database;
import org.hsqldb.HsqlException;
import org.hsqldb.Row;
import org.hsqldb.RowAVL;
import org.hsqldb.RowAVLDiskData;
import org.hsqldb.RowAction;
import org.hsqldb.Session;
import org.hsqldb.Table;
import org.hsqldb.TableBase;
import org.hsqldb.error.Error;
import org.hsqldb.index.Index;
import org.hsqldb.lib.ArrayUtil;
import org.hsqldb.rowio.RowInputInterface;
import org.hsqldb.rowio.RowOutputInterface;

public class RowStoreAVLDiskData
  extends RowStoreAVL
{
  DataFileCache cache;
  RowOutputInterface rowOut;
  
  public RowStoreAVLDiskData(Table paramTable)
  {
    this.database = paramTable.database;
    this.table = paramTable;
    this.indexList = paramTable.getIndexList();
    this.accessorList = new CachedObject[this.indexList.length];
    this.lock = new ReentrantReadWriteLock();
    this.readLock = this.lock.readLock();
    this.writeLock = this.lock.writeLock();
  }
  
  public Object[] getData(RowAVLDiskData paramRowAVLDiskData)
  {
    this.cache.writeLock.lock();
    try
    {
      this.cache.get(paramRowAVLDiskData, this, false);
      Object[] arrayOfObject = paramRowAVLDiskData.getData();
      return arrayOfObject;
    }
    finally
    {
      this.cache.writeLock.unlock();
    }
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
    this.cache.writeLock.lock();
    try
    {
      int i = paramCachedObject.getRealSize(this.cache.rowOut);
      paramCachedObject.setStorageSize(i);
      long l = this.tableSpace.getFilePosition(i, false);
      paramCachedObject.setPos(l);
      if (paramBoolean) {
        RowAction.addInsertAction(paramSession, this.table, (Row)paramCachedObject);
      }
      this.cache.add(paramCachedObject, false);
    }
    finally
    {
      this.cache.writeLock.unlock();
    }
  }
  
  public CachedObject get(RowInputInterface paramRowInputInterface)
  {
    try
    {
      RowAVLDiskData localRowAVLDiskData = new RowAVLDiskData(this, this.table, paramRowInputInterface);
      localRowAVLDiskData.setPos(paramRowInputInterface.getPos());
      localRowAVLDiskData.setStorageSize(paramRowInputInterface.getSize());
      localRowAVLDiskData.setChanged(false);
      ((TextCache)this.cache).addInit(localRowAVLDiskData);
      return localRowAVLDiskData;
    }
    catch (IOException localIOException)
    {
      throw Error.error(484, localIOException);
    }
  }
  
  public CachedObject get(CachedObject paramCachedObject, RowInputInterface paramRowInputInterface)
  {
    Object[] arrayOfObject = paramRowInputInterface.readData(this.table.getColumnTypes());
    ((RowAVLDiskData)paramCachedObject).setData(arrayOfObject);
    return paramCachedObject;
  }
  
  public CachedObject getNewCachedObject(Session paramSession, Object paramObject, boolean paramBoolean)
  {
    RowAVLDiskData localRowAVLDiskData = new RowAVLDiskData(this, this.table, (Object[])paramObject);
    add(paramSession, localRowAVLDiskData, paramBoolean);
    return localRowAVLDiskData;
  }
  
  public void indexRow(Session paramSession, Row paramRow)
  {
    super.indexRow(paramSession, paramRow);
  }
  
  public boolean isMemory()
  {
    return false;
  }
  
  public void set(CachedObject paramCachedObject) {}
  
  public CachedObject get(long paramLong)
  {
    CachedObject localCachedObject = this.cache.get(paramLong, this, false);
    return localCachedObject;
  }
  
  public void removeAll()
  {
    destroy();
    this.elementCount.set(0L);
    ArrayUtil.fillArray(this.accessorList, null);
  }
  
  public void remove(CachedObject paramCachedObject)
  {
    this.cache.remove(paramCachedObject);
  }
  
  public CachedObject getAccessor(Index paramIndex)
  {
    int i = paramIndex.getPosition();
    if (i >= this.accessorList.length) {
      throw Error.runtimeError(201, "RowStoreAVL");
    }
    return this.accessorList[i];
  }
  
  public void commitPersistence(CachedObject paramCachedObject)
  {
    try
    {
      this.cache.saveRow(paramCachedObject);
    }
    catch (HsqlException localHsqlException) {}
  }
  
  public void postCommitAction(Session paramSession, RowAction paramRowAction)
  {
    if ((paramRowAction.getType() == 3) && (!paramRowAction.isDeleteComplete()))
    {
      paramRowAction.setDeleteComplete();
      Row localRow = paramRowAction.getRow();
      if (localRow == null) {
        localRow = (Row)get(paramRowAction.getPos(), false);
      }
      delete(paramSession, localRow);
      remove(localRow);
    }
  }
  
  public void commitRow(Session paramSession, Row paramRow, int paramInt1, int paramInt2)
  {
    switch (paramInt1)
    {
    case 2: 
      this.cache.removePersistence(paramRow);
      break;
    case 1: 
      commitPersistence(paramRow);
      break;
    case 4: 
      if (paramInt2 == 0)
      {
        remove(paramRow);
      }
      else
      {
        delete(paramSession, paramRow);
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
        ((RowAVL)paramRow).setNewNodes(this);
        indexRow(paramSession, paramRow);
      }
      break;
    case 1: 
      if (paramInt2 == 0)
      {
        delete(paramSession, paramRow);
        remove(paramRow);
      }
      break;
    case 4: 
      if (paramInt2 == 0)
      {
        remove(paramRow);
      }
      else
      {
        delete(paramSession, paramRow);
        remove(paramRow);
      }
      break;
    }
  }
  
  public DataFileCache getCache()
  {
    return this.cache;
  }
  
  public void setCache(DataFileCache paramDataFileCache)
  {
    this.cache = paramDataFileCache;
    this.tableSpace = paramDataFileCache.spaceManager.getTableSpace(7);
  }
  
  public void release()
  {
    destroy();
    this.table.database.logger.textTableManager.closeTextCache((Table)this.table);
    this.cache = null;
    ArrayUtil.fillArray(this.accessorList, null);
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


/* Location:              E:\java\java学习\hutubill\lib\all.jar!\org\hsqldb\persist\RowStoreAVLDiskData.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */
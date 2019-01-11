package org.hsqldb.persist;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import org.hsqldb.Database;
import org.hsqldb.Row;
import org.hsqldb.RowAVL;
import org.hsqldb.RowAction;
import org.hsqldb.Session;
import org.hsqldb.Table;
import org.hsqldb.error.Error;
import org.hsqldb.lib.ArrayUtil;
import org.hsqldb.rowio.RowInputInterface;

public class RowStoreAVLMemory
  extends RowStoreAVL
  implements PersistentStore
{
  Database database;
  AtomicInteger rowIdSequence = new AtomicInteger();
  ReadWriteLock lock;
  Lock readLock;
  Lock writeLock;
  
  public RowStoreAVLMemory(Table paramTable)
  {
    this.database = paramTable.database;
    this.table = paramTable;
    this.indexList = paramTable.getIndexList();
    this.accessorList = new CachedObject[this.indexList.length];
    this.lock = new ReentrantReadWriteLock();
    this.readLock = this.lock.readLock();
    this.writeLock = this.lock.writeLock();
  }
  
  public boolean isMemory()
  {
    return true;
  }
  
  public int getAccessCount()
  {
    return 0;
  }
  
  public void set(CachedObject paramCachedObject) {}
  
  public CachedObject get(long paramLong)
  {
    throw Error.runtimeError(201, "RowStoreAVMemory");
  }
  
  public CachedObject get(long paramLong, boolean paramBoolean)
  {
    throw Error.runtimeError(201, "RowStoreAVLMemory");
  }
  
  public CachedObject get(CachedObject paramCachedObject, boolean paramBoolean)
  {
    return paramCachedObject;
  }
  
  public void add(Session paramSession, CachedObject paramCachedObject, boolean paramBoolean) {}
  
  public CachedObject get(RowInputInterface paramRowInputInterface)
  {
    throw Error.runtimeError(201, "RowStoreAVLMemory");
  }
  
  public CachedObject getNewCachedObject(Session paramSession, Object paramObject, boolean paramBoolean)
  {
    int i = this.rowIdSequence.getAndIncrement();
    RowAVL localRowAVL = new RowAVL(this.table, (Object[])paramObject, i, this);
    if (paramBoolean) {
      RowAction.addInsertAction(paramSession, this.table, localRowAVL);
    }
    return localRowAVL;
  }
  
  public void removeAll()
  {
    destroy();
    setTimestamp(0L);
    this.elementCount.set(0L);
    ArrayUtil.fillArray(this.accessorList, null);
  }
  
  public void remove(CachedObject paramCachedObject) {}
  
  public void release(long paramLong) {}
  
  public void commitPersistence(CachedObject paramCachedObject) {}
  
  public void postCommitAction(Session paramSession, RowAction paramRowAction)
  {
    if ((paramRowAction.getType() == 3) && (!paramRowAction.isDeleteComplete()))
    {
      paramRowAction.setDeleteComplete();
      Row localRow = paramRowAction.getRow();
      delete(paramSession, localRow);
    }
  }
  
  public void commitRow(Session paramSession, Row paramRow, int paramInt1, int paramInt2)
  {
    Object[] arrayOfObject = paramRow.getData();
    switch (paramInt1)
    {
    case 2: 
      this.database.logger.writeDeleteStatement(paramSession, (Table)this.table, arrayOfObject);
      break;
    case 1: 
      this.database.logger.writeInsertStatement(paramSession, paramRow, (Table)this.table);
      break;
    case 4: 
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
      delete(paramSession, paramRow);
      remove(paramRow);
      break;
    case 4: 
      remove(paramRow);
    }
  }
  
  public DataFileCache getCache()
  {
    return null;
  }
  
  public void setCache(DataFileCache paramDataFileCache) {}
  
  public void release()
  {
    destroy();
    setTimestamp(0L);
    this.elementCount.set(0L);
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


/* Location:              E:\java\java学习\hutubill\lib\all.jar!\org\hsqldb\persist\RowStoreAVLMemory.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */
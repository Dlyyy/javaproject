package org.hsqldb.persist;

import org.hsqldb.Row;
import org.hsqldb.RowAction;
import org.hsqldb.Session;
import org.hsqldb.TableBase;
import org.hsqldb.index.Index;
import org.hsqldb.navigator.RowIterator;
import org.hsqldb.rowio.RowInputInterface;

public abstract interface PersistentStore
{
  public static final int SHORT_STORE_SIZE = 2;
  public static final int INT_STORE_SIZE = 4;
  public static final int LONG_STORE_SIZE = 8;
  public static final PersistentStore[] emptyArray = new PersistentStore[0];
  
  public abstract boolean isRowStore();
  
  public abstract TableBase getTable();
  
  public abstract long getTimestamp();
  
  public abstract void setTimestamp(long paramLong);
  
  public abstract boolean isMemory();
  
  public abstract void setMemory(boolean paramBoolean);
  
  public abstract void set(CachedObject paramCachedObject);
  
  public abstract CachedObject get(long paramLong);
  
  public abstract CachedObject get(long paramLong, boolean paramBoolean);
  
  public abstract CachedObject get(CachedObject paramCachedObject, boolean paramBoolean);
  
  public abstract void add(CachedObject paramCachedObject, boolean paramBoolean);
  
  public abstract void add(Session paramSession, CachedObject paramCachedObject, boolean paramBoolean);
  
  public abstract boolean canRead(Session paramSession, long paramLong, int paramInt, int[] paramArrayOfInt);
  
  public abstract boolean canRead(Session paramSession, CachedObject paramCachedObject, int paramInt, int[] paramArrayOfInt);
  
  public abstract CachedObject get(RowInputInterface paramRowInputInterface);
  
  public abstract CachedObject get(CachedObject paramCachedObject, RowInputInterface paramRowInputInterface);
  
  public abstract CachedObject getNewInstance(int paramInt);
  
  public abstract int getDefaultObjectSize();
  
  public abstract CachedObject getNewCachedObject(Session paramSession, Object paramObject, boolean paramBoolean);
  
  public abstract void removeAll();
  
  public abstract void remove(CachedObject paramCachedObject);
  
  public abstract void commitPersistence(CachedObject paramCachedObject);
  
  public abstract void delete(Session paramSession, Row paramRow);
  
  public abstract void indexRow(Session paramSession, Row paramRow);
  
  public abstract void commitRow(Session paramSession, Row paramRow, int paramInt1, int paramInt2);
  
  public abstract void rollbackRow(Session paramSession, Row paramRow, int paramInt1, int paramInt2);
  
  public abstract void postCommitAction(Session paramSession, RowAction paramRowAction);
  
  public abstract void indexRows(Session paramSession);
  
  public abstract RowIterator rowIterator();
  
  public abstract DataFileCache getCache();
  
  public abstract void setCache(DataFileCache paramDataFileCache);
  
  public abstract TableSpaceManager getSpaceManager();
  
  public abstract void setSpaceManager(TableSpaceManager paramTableSpaceManager);
  
  public abstract void release();
  
  public abstract PersistentStore getAccessorStore(Index paramIndex);
  
  public abstract CachedObject getAccessor(Index paramIndex);
  
  public abstract void setAccessor(Index paramIndex, CachedObject paramCachedObject);
  
  public abstract void setAccessor(Index paramIndex, long paramLong);
  
  public abstract double searchCost(Session paramSession, Index paramIndex, int paramInt1, int paramInt2);
  
  public abstract long elementCount();
  
  public abstract long elementCount(Session paramSession);
  
  public abstract long elementCountUnique(Index paramIndex);
  
  public abstract void setElementCount(Index paramIndex, long paramLong1, long paramLong2);
  
  public abstract boolean hasNull(int paramInt);
  
  public abstract void resetAccessorKeys(Session paramSession, Index[] paramArrayOfIndex);
  
  public abstract Index[] getAccessorKeys();
  
  public abstract void moveDataToSpace(Session paramSession);
  
  public abstract void moveData(Session paramSession, PersistentStore paramPersistentStore, int paramInt1, int paramInt2);
  
  public abstract void reindex(Session paramSession, Index paramIndex);
  
  public abstract void setReadOnly(boolean paramBoolean);
  
  public abstract void readLock();
  
  public abstract void readUnlock();
  
  public abstract void writeLock();
  
  public abstract void writeUnlock();
}


/* Location:              E:\java\java学习\hutubill\lib\all.jar!\org\hsqldb\persist\PersistentStore.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */
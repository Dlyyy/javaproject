package org.hsqldb.persist;

import org.hsqldb.Database;
import org.hsqldb.HsqlException;
import org.hsqldb.Session;
import org.hsqldb.Table;
import org.hsqldb.TableBase;
import org.hsqldb.dbinfo.DatabaseInformation;
import org.hsqldb.error.Error;
import org.hsqldb.index.Index;
import org.hsqldb.lib.Collection;
import org.hsqldb.lib.HsqlDeque;
import org.hsqldb.lib.Iterator;
import org.hsqldb.lib.LongKeyHashMap;

public class PersistentStoreCollectionSession
  implements PersistentStoreCollection
{
  private final Session session;
  private final LongKeyHashMap rowStoreMapSession = new LongKeyHashMap();
  private LongKeyHashMap rowStoreMapTransaction = new LongKeyHashMap();
  private LongKeyHashMap rowStoreMapStatement = new LongKeyHashMap();
  private LongKeyHashMap rowStoreMapRoutine = new LongKeyHashMap();
  private HsqlDeque rowStoreListStack;
  DataFileCacheSession resultCache;
  
  public PersistentStoreCollectionSession(Session paramSession)
  {
    this.session = paramSession;
  }
  
  public synchronized void removeStore(TableBase paramTableBase)
  {
    switch (paramTableBase.persistenceScope)
    {
    case 20: 
      this.rowStoreMapRoutine.remove(paramTableBase.getPersistenceId());
      break;
    case 21: 
      this.rowStoreMapStatement.remove(paramTableBase.getPersistenceId());
      break;
    case 22: 
    case 24: 
      this.rowStoreMapTransaction.remove(paramTableBase.getPersistenceId());
      break;
    case 23: 
      this.rowStoreMapSession.remove(paramTableBase.getPersistenceId());
      break;
    default: 
      throw Error.runtimeError(201, "PersistentStoreCollectionSession");
    }
  }
  
  public synchronized PersistentStore getViewStore(long paramLong)
  {
    return (PersistentStore)this.rowStoreMapStatement.get(paramLong);
  }
  
  public synchronized PersistentStore getStore(TableBase paramTableBase)
  {
    PersistentStore localPersistentStore;
    switch (paramTableBase.persistenceScope)
    {
    case 20: 
      localPersistentStore = (PersistentStore)this.rowStoreMapRoutine.get(paramTableBase.getPersistenceId());
      if (localPersistentStore == null)
      {
        localPersistentStore = this.session.database.logger.newStore(this.session, this, paramTableBase);
        this.rowStoreMapRoutine.put(paramTableBase.getPersistenceId(), localPersistentStore);
      }
      return localPersistentStore;
    case 21: 
      localPersistentStore = (PersistentStore)this.rowStoreMapStatement.get(paramTableBase.getPersistenceId());
      if (localPersistentStore == null)
      {
        localPersistentStore = this.session.database.logger.newStore(this.session, this, paramTableBase);
        this.rowStoreMapStatement.put(paramTableBase.getPersistenceId(), localPersistentStore);
      }
      return localPersistentStore;
    case 22: 
    case 24: 
      localPersistentStore = (PersistentStore)this.rowStoreMapTransaction.get(paramTableBase.getPersistenceId());
      if (localPersistentStore == null)
      {
        localPersistentStore = this.session.database.logger.newStore(this.session, this, paramTableBase);
        this.rowStoreMapTransaction.put(paramTableBase.getPersistenceId(), localPersistentStore);
      }
      if (paramTableBase.getTableType() == 1) {
        this.session.database.dbInfo.setStore(this.session, (Table)paramTableBase, localPersistentStore);
      }
      return localPersistentStore;
    case 23: 
      localPersistentStore = (PersistentStore)this.rowStoreMapSession.get(paramTableBase.getPersistenceId());
      if (localPersistentStore == null)
      {
        localPersistentStore = this.session.database.logger.newStore(this.session, this, paramTableBase);
        this.rowStoreMapSession.put(paramTableBase.getPersistenceId(), localPersistentStore);
      }
      return localPersistentStore;
    }
    throw Error.runtimeError(201, "PersistentStoreCollectionSession");
  }
  
  public synchronized void clearAllTables()
  {
    clearSessionTables();
    clearTransactionTables();
    clearStatementTables();
    clearRoutineTables();
    closeSessionDataCache();
  }
  
  public synchronized void clearResultTables(long paramLong)
  {
    if (this.rowStoreMapSession.isEmpty()) {
      return;
    }
    Iterator localIterator = this.rowStoreMapSession.values().iterator();
    while (localIterator.hasNext())
    {
      PersistentStore localPersistentStore = (PersistentStore)localIterator.next();
      if (localPersistentStore.getTimestamp() == paramLong)
      {
        localPersistentStore.release();
        localIterator.remove();
      }
    }
  }
  
  public synchronized void clearSessionTables()
  {
    if (this.rowStoreMapSession.isEmpty()) {
      return;
    }
    Iterator localIterator = this.rowStoreMapSession.values().iterator();
    while (localIterator.hasNext())
    {
      PersistentStore localPersistentStore = (PersistentStore)localIterator.next();
      localPersistentStore.release();
    }
    this.rowStoreMapSession.clear();
  }
  
  public synchronized void clearTransactionTables()
  {
    if (this.rowStoreMapTransaction.isEmpty()) {
      return;
    }
    Iterator localIterator = this.rowStoreMapTransaction.values().iterator();
    while (localIterator.hasNext())
    {
      PersistentStore localPersistentStore = (PersistentStore)localIterator.next();
      localPersistentStore.release();
    }
    this.rowStoreMapTransaction.clear();
  }
  
  public synchronized void clearStatementTables()
  {
    if (this.rowStoreMapStatement.isEmpty()) {
      return;
    }
    Iterator localIterator = this.rowStoreMapStatement.values().iterator();
    while (localIterator.hasNext())
    {
      PersistentStore localPersistentStore = (PersistentStore)localIterator.next();
      localPersistentStore.release();
    }
    this.rowStoreMapStatement.clear();
  }
  
  public synchronized void clearRoutineTables()
  {
    if (this.rowStoreMapRoutine.isEmpty()) {
      return;
    }
    Iterator localIterator = this.rowStoreMapRoutine.values().iterator();
    while (localIterator.hasNext())
    {
      PersistentStore localPersistentStore = (PersistentStore)localIterator.next();
      localPersistentStore.release();
    }
    this.rowStoreMapRoutine.clear();
  }
  
  public synchronized PersistentStore findStore(TableBase paramTableBase)
  {
    PersistentStore localPersistentStore = null;
    switch (paramTableBase.persistenceScope)
    {
    case 20: 
      localPersistentStore = (PersistentStore)this.rowStoreMapRoutine.get(paramTableBase.getPersistenceId());
      break;
    case 21: 
      localPersistentStore = (PersistentStore)this.rowStoreMapStatement.get(paramTableBase.getPersistenceId());
      break;
    case 22: 
    case 24: 
      localPersistentStore = (PersistentStore)this.rowStoreMapTransaction.get(paramTableBase.getPersistenceId());
      break;
    case 23: 
      localPersistentStore = (PersistentStore)this.rowStoreMapSession.get(paramTableBase.getPersistenceId());
    }
    return localPersistentStore;
  }
  
  public synchronized void resetAccessorKeys(Session paramSession, Table paramTable, Index[] paramArrayOfIndex)
  {
    PersistentStore localPersistentStore = findStore(paramTable);
    if (localPersistentStore == null) {
      return;
    }
    localPersistentStore.resetAccessorKeys(paramSession, paramArrayOfIndex);
  }
  
  public synchronized void moveData(Table paramTable1, Table paramTable2, int paramInt1, int paramInt2)
  {
    PersistentStore localPersistentStore1 = findStore(paramTable1);
    if (localPersistentStore1 == null) {
      return;
    }
    PersistentStore localPersistentStore2 = getStore(paramTable2);
    try
    {
      localPersistentStore2.moveData(this.session, localPersistentStore1, paramInt1, paramInt2);
    }
    catch (HsqlException localHsqlException)
    {
      localPersistentStore2.release();
      removeStore(paramTable2);
      throw localHsqlException;
    }
    removeStore(paramTable1);
  }
  
  public synchronized void push(boolean paramBoolean)
  {
    if (this.rowStoreListStack == null) {
      this.rowStoreListStack = new HsqlDeque();
    }
    Object[] arrayOfObject = this.rowStoreMapStatement.toArray();
    this.rowStoreListStack.add(arrayOfObject);
    this.rowStoreMapStatement.clear();
    if (paramBoolean)
    {
      arrayOfObject = this.rowStoreMapRoutine.toArray();
      this.rowStoreListStack.add(arrayOfObject);
      this.rowStoreMapRoutine.clear();
    }
  }
  
  public synchronized void pop(boolean paramBoolean)
  {
    PersistentStore localPersistentStore;
    if (paramBoolean)
    {
      arrayOfObject = (Object[])this.rowStoreListStack.removeLast();
      clearRoutineTables();
      for (i = 0; i < arrayOfObject.length; i++)
      {
        localPersistentStore = (PersistentStore)arrayOfObject[i];
        this.rowStoreMapRoutine.put(localPersistentStore.getTable().getPersistenceId(), localPersistentStore);
      }
    }
    Object[] arrayOfObject = (Object[])this.rowStoreListStack.removeLast();
    clearStatementTables();
    for (int i = 0; i < arrayOfObject.length; i++)
    {
      localPersistentStore = (PersistentStore)arrayOfObject[i];
      this.rowStoreMapStatement.put(localPersistentStore.getTable().getPersistenceId(), localPersistentStore);
    }
  }
  
  public synchronized DataFileCacheSession getSessionDataCache()
  {
    if (this.resultCache == null)
    {
      String str = this.session.database.logger.getTempDirectoryPath();
      if (str == null) {
        return null;
      }
      try
      {
        this.resultCache = new DataFileCacheSession(this.session.database, str + "/session_" + Long.toString(this.session.getId()));
        this.resultCache.open(false);
      }
      catch (Throwable localThrowable)
      {
        return null;
      }
    }
    return this.resultCache;
  }
  
  private void closeSessionDataCache()
  {
    if (this.resultCache != null)
    {
      try
      {
        this.resultCache.release();
        this.resultCache.deleteFile();
      }
      catch (HsqlException localHsqlException) {}
      this.resultCache = null;
    }
  }
  
  public synchronized void release()
  {
    clearAllTables();
  }
}


/* Location:              E:\java\java学习\hutubill\lib\all.jar!\org\hsqldb\persist\PersistentStoreCollectionSession.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */
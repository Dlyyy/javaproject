package org.hsqldb.persist;

import org.hsqldb.Database;
import org.hsqldb.TableBase;
import org.hsqldb.lib.Collection;
import org.hsqldb.lib.Iterator;
import org.hsqldb.lib.LongKeyHashMap;

public class PersistentStoreCollectionDatabase
  implements PersistentStoreCollection
{
  private Database database;
  private long persistentStoreIdSequence;
  private final LongKeyHashMap rowStoreMap = new LongKeyHashMap();
  
  public PersistentStoreCollectionDatabase(Database paramDatabase)
  {
    this.database = paramDatabase;
  }
  
  public synchronized PersistentStore getStore(TableBase paramTableBase)
  {
    long l = paramTableBase.getPersistenceId();
    PersistentStore localPersistentStore = (PersistentStore)this.rowStoreMap.get(l);
    if (localPersistentStore == null)
    {
      localPersistentStore = this.database.logger.newStore(null, this, paramTableBase);
      this.rowStoreMap.put(l, localPersistentStore);
      paramTableBase.store = localPersistentStore;
    }
    return localPersistentStore;
  }
  
  public void release()
  {
    if (this.rowStoreMap.isEmpty()) {
      return;
    }
    Iterator localIterator = this.rowStoreMap.values().iterator();
    while (localIterator.hasNext())
    {
      PersistentStore localPersistentStore = (PersistentStore)localIterator.next();
      localPersistentStore.release();
    }
    this.rowStoreMap.clear();
  }
  
  public synchronized void removeStore(TableBase paramTableBase)
  {
    PersistentStore localPersistentStore = (PersistentStore)this.rowStoreMap.get(paramTableBase.getPersistenceId());
    if (localPersistentStore != null)
    {
      localPersistentStore.removeAll();
      localPersistentStore.release();
      this.rowStoreMap.remove(paramTableBase.getPersistenceId());
    }
  }
  
  public long getNextId()
  {
    return this.persistentStoreIdSequence++;
  }
  
  public void setNewTableSpaces()
  {
    DataFileCache localDataFileCache = this.database.logger.getCache();
    if (localDataFileCache == null) {
      return;
    }
    Iterator localIterator = this.rowStoreMap.values().iterator();
    while (localIterator.hasNext())
    {
      PersistentStore localPersistentStore = (PersistentStore)localIterator.next();
      if (localPersistentStore != null)
      {
        TableBase localTableBase = localPersistentStore.getTable();
        if (localTableBase.getTableType() == 5)
        {
          TableSpaceManager localTableSpaceManager = localDataFileCache.spaceManager.getTableSpace(localTableBase.getSpaceID());
          localPersistentStore.setSpaceManager(localTableSpaceManager);
        }
      }
    }
  }
}


/* Location:              E:\java\java学习\hutubill\lib\all.jar!\org\hsqldb\persist\PersistentStoreCollectionDatabase.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */
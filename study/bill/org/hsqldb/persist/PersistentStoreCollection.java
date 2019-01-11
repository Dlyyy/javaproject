package org.hsqldb.persist;

import org.hsqldb.TableBase;

public abstract interface PersistentStoreCollection
{
  public abstract PersistentStore getStore(TableBase paramTableBase);
  
  public abstract void removeStore(TableBase paramTableBase);
  
  public abstract void release();
}


/* Location:              E:\java\java学习\hutubill\lib\all.jar!\org\hsqldb\persist\PersistentStoreCollection.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */
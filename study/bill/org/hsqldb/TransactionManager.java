package org.hsqldb;

import org.hsqldb.persist.CachedObject;
import org.hsqldb.persist.PersistentStore;

public abstract interface TransactionManager
{
  public static final int LOCKS = 0;
  public static final int MVLOCKS = 1;
  public static final int MVCC = 2;
  public static final int ACTION_READ = 0;
  public static final int ACTION_DUP = 1;
  public static final int ACTION_REF = 2;
  public static final int resetSessionResults = 1;
  public static final int resetSessionTables = 2;
  public static final int resetSessionResetAll = 3;
  public static final int resetSessionRollback = 4;
  public static final int resetSessionAbort = 5;
  public static final int resetSessionClose = 6;
  
  public abstract long getGlobalChangeTimestamp();
  
  public abstract long getNextGlobalChangeTimestamp();
  
  public abstract RowAction addDeleteAction(Session paramSession, Table paramTable, PersistentStore paramPersistentStore, Row paramRow, int[] paramArrayOfInt);
  
  public abstract void addInsertAction(Session paramSession, Table paramTable, PersistentStore paramPersistentStore, Row paramRow, int[] paramArrayOfInt);
  
  public abstract void beginAction(Session paramSession, Statement paramStatement);
  
  public abstract void beginActionResume(Session paramSession);
  
  public abstract void beginTransaction(Session paramSession);
  
  public abstract boolean canRead(Session paramSession, PersistentStore paramPersistentStore, Row paramRow, int paramInt, int[] paramArrayOfInt);
  
  public abstract boolean canRead(Session paramSession, PersistentStore paramPersistentStore, long paramLong, int paramInt);
  
  public abstract boolean commitTransaction(Session paramSession);
  
  public abstract void completeActions(Session paramSession);
  
  public abstract int getTransactionControl();
  
  public abstract boolean isMVRows();
  
  public abstract boolean isMVCC();
  
  public abstract boolean is2PL();
  
  public abstract boolean prepareCommitActions(Session paramSession);
  
  public abstract void rollback(Session paramSession);
  
  public abstract void rollbackAction(Session paramSession);
  
  public abstract void rollbackSavepoint(Session paramSession, int paramInt);
  
  public abstract void rollbackPartial(Session paramSession, int paramInt, long paramLong);
  
  public abstract void setTransactionControl(Session paramSession, int paramInt);
  
  public abstract void addTransactionInfo(CachedObject paramCachedObject);
  
  public abstract void setTransactionInfo(PersistentStore paramPersistentStore, CachedObject paramCachedObject);
  
  public abstract void removeTransactionInfo(CachedObject paramCachedObject);
  
  public abstract void removeTransactionInfo(long paramLong);
  
  public abstract void resetSession(Session paramSession1, Session paramSession2, int paramInt);
}


/* Location:              E:\java\java学习\hutubill\lib\all.jar!\org\hsqldb\TransactionManager.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */
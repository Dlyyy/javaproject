package org.hsqldb;

import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantReadWriteLock.WriteLock;
import org.hsqldb.error.Error;
import org.hsqldb.lib.HashMappedList;
import org.hsqldb.lib.HsqlArrayList;
import org.hsqldb.lib.HsqlDeque;
import org.hsqldb.lib.LongDeque;
import org.hsqldb.lib.LongKeyHashMap;
import org.hsqldb.lib.OrderedHashSet;
import org.hsqldb.persist.CachedObject;
import org.hsqldb.persist.PersistentStore;

public class TransactionManagerMV2PL
  extends TransactionManagerCommon
  implements TransactionManager
{
  HsqlDeque committedTransactions = new HsqlDeque();
  LongDeque committedTransactionTimestamps = new LongDeque();
  
  public TransactionManagerMV2PL(Database paramDatabase)
  {
    this.database = paramDatabase;
    this.lobSession = this.database.sessionManager.getSysLobSession();
    this.rowActionMap = new LongKeyHashMap(8192);
    this.txModel = 1;
    this.catalogNameList = new HsqlNameManager.HsqlName[] { this.database.getCatalogName() };
  }
  
  public long getGlobalChangeTimestamp()
  {
    return this.globalChangeTimestamp.get();
  }
  
  public void setGlobalChangeTimestamp(long paramLong)
  {
    this.globalChangeTimestamp.set(paramLong);
  }
  
  public boolean isMVRows()
  {
    return true;
  }
  
  public boolean isMVCC()
  {
    return false;
  }
  
  public boolean is2PL()
  {
    return false;
  }
  
  public int getTransactionControl()
  {
    return 1;
  }
  
  public void setTransactionControl(Session paramSession, int paramInt)
  {
    super.setTransactionControl(paramSession, paramInt);
  }
  
  public void completeActions(Session paramSession)
  {
    endActionTPL(paramSession);
  }
  
  public boolean prepareCommitActions(Session paramSession)
  {
    this.writeLock.lock();
    try
    {
      int i = paramSession.rowActionList.size();
      paramSession.actionTimestamp = getNextGlobalChangeTimestamp();
      for (int j = 0; j < i; j++)
      {
        RowAction localRowAction = (RowAction)paramSession.rowActionList.get(j);
        localRowAction.prepareCommit(paramSession);
      }
      j = 1;
      return j;
    }
    finally
    {
      this.writeLock.unlock();
    }
  }
  
  public boolean commitTransaction(Session paramSession)
  {
    if (paramSession.abortTransaction) {
      return false;
    }
    this.writeLock.lock();
    try
    {
      int i = paramSession.rowActionList.size();
      paramSession.actionTimestamp = getNextGlobalChangeTimestamp();
      paramSession.transactionEndTimestamp = paramSession.actionTimestamp;
      endTransaction(paramSession);
      Object localObject1;
      for (int j = 0; j < i; j++)
      {
        localObject1 = (RowAction)paramSession.rowActionList.get(j);
        ((RowAction)localObject1).commit(paramSession);
      }
      adjustLobUsage(paramSession);
      persistCommit(paramSession);
      j = paramSession.rowActionList.size();
      if (j > i)
      {
        localObject1 = paramSession.rowActionList.getArray();
        mergeTransaction((Object[])localObject1, i, j, paramSession.actionTimestamp);
        finaliseRows(paramSession, (Object[])localObject1, i, j);
        paramSession.rowActionList.setSize(i);
      }
      if ((paramSession == this.lobSession) || (getFirstLiveTransactionTimestamp() > paramSession.actionTimestamp))
      {
        localObject1 = paramSession.rowActionList.getArray();
        mergeTransaction((Object[])localObject1, 0, i, paramSession.actionTimestamp);
        finaliseRows(paramSession, (Object[])localObject1, 0, i);
      }
      else
      {
        localObject1 = paramSession.rowActionList.toArray();
        addToCommittedQueue(paramSession, (Object[])localObject1);
      }
      paramSession.isTransaction = false;
      endTransactionTPL(paramSession);
    }
    finally
    {
      this.writeLock.unlock();
    }
    paramSession.actionSet.clear();
    return true;
  }
  
  public void rollback(Session paramSession)
  {
    this.writeLock.lock();
    try
    {
      paramSession.abortTransaction = false;
      paramSession.actionTimestamp = getNextGlobalChangeTimestamp();
      paramSession.transactionEndTimestamp = paramSession.actionTimestamp;
      rollbackPartial(paramSession, 0, paramSession.transactionTimestamp);
      endTransaction(paramSession);
      paramSession.logSequences();
      paramSession.isTransaction = false;
      endTransactionTPL(paramSession);
    }
    finally
    {
      this.writeLock.unlock();
    }
  }
  
  public void rollbackSavepoint(Session paramSession, int paramInt)
  {
    long l = paramSession.sessionContext.savepointTimestamps.get(paramInt);
    Integer localInteger = (Integer)paramSession.sessionContext.savepoints.get(paramInt);
    int i = localInteger.intValue();
    while (paramSession.sessionContext.savepoints.size() > paramInt + 1)
    {
      paramSession.sessionContext.savepoints.remove(paramSession.sessionContext.savepoints.size() - 1);
      paramSession.sessionContext.savepointTimestamps.removeLast();
    }
    rollbackPartial(paramSession, i, l);
  }
  
  public void rollbackAction(Session paramSession)
  {
    rollbackPartial(paramSession, paramSession.actionIndex, paramSession.actionStartTimestamp);
    endActionTPL(paramSession);
  }
  
  public void rollbackPartial(Session paramSession, int paramInt, long paramLong)
  {
    int i = paramSession.rowActionList.size();
    if (paramInt == i) {
      return;
    }
    for (int j = i - 1; j >= paramInt; j--)
    {
      RowAction localRowAction = (RowAction)paramSession.rowActionList.get(j);
      if ((localRowAction != null) && (localRowAction.type != 0) && (localRowAction.type != 3))
      {
        Row localRow = localRowAction.memoryRow;
        if (localRow == null) {
          localRow = (Row)localRowAction.store.get(localRowAction.getPos(), false);
        }
        if (localRow != null)
        {
          this.writeLock.lock();
          try
          {
            localRowAction.rollback(paramSession, paramLong);
            int k = localRowAction.mergeRollback(paramSession, paramLong, localRow);
            if (localRowAction.type == 3)
            {
              if (localRowAction.deleteComplete) {
                this.writeLock.unlock();
              } else {
                localRowAction.deleteComplete = true;
              }
            }
            else {
              localRowAction.store.rollbackRow(paramSession, localRow, k, this.txModel);
            }
          }
          finally
          {
            this.writeLock.unlock();
          }
        }
      }
    }
    paramSession.rowActionList.setSize(paramInt);
  }
  
  public RowAction addDeleteAction(Session paramSession, Table paramTable, PersistentStore paramPersistentStore, Row paramRow, int[] paramArrayOfInt)
  {
    RowAction localRowAction;
    synchronized (paramRow)
    {
      switch (paramTable.tableType)
      {
      case 5: 
        localRowAction = RowAction.addDeleteAction(paramSession, paramTable, paramRow, paramArrayOfInt);
        addTransactionInfo(paramRow);
        break;
      case 3: 
        localRowAction = RowAction.addDeleteAction(paramSession, paramTable, paramRow, paramArrayOfInt);
        paramPersistentStore.delete(paramSession, paramRow);
        paramRow.rowAction = null;
        break;
      case 4: 
      default: 
        localRowAction = RowAction.addDeleteAction(paramSession, paramTable, paramRow, paramArrayOfInt);
      }
    }
    paramSession.rowActionList.add(localRowAction);
    return localRowAction;
  }
  
  public void addInsertAction(Session paramSession, Table paramTable, PersistentStore paramPersistentStore, Row paramRow, int[] paramArrayOfInt)
  {
    RowAction localRowAction = paramRow.rowAction;
    if (localRowAction == null) {
      throw Error.runtimeError(458, "null insert action ");
    }
    paramPersistentStore.indexRow(paramSession, paramRow);
    if (paramTable.persistenceScope == 20)
    {
      paramRow.rowAction = null;
      return;
    }
    paramSession.rowActionList.add(localRowAction);
  }
  
  public boolean canRead(Session paramSession, PersistentStore paramPersistentStore, Row paramRow, int paramInt, int[] paramArrayOfInt)
  {
    RowAction localRowAction = paramRow.rowAction;
    if (localRowAction == null) {
      return true;
    }
    if (localRowAction.table.tableType == 3) {
      return true;
    }
    return localRowAction.canRead(paramSession, 0);
  }
  
  public boolean canRead(Session paramSession, PersistentStore paramPersistentStore, long paramLong, int paramInt)
  {
    if (paramPersistentStore.getTable().tableType == 3) {
      return true;
    }
    RowAction localRowAction = (RowAction)this.rowActionMap.get(paramLong);
    if (localRowAction == null) {
      return true;
    }
    return localRowAction.canRead(paramSession, paramInt);
  }
  
  public void addTransactionInfo(CachedObject paramCachedObject)
  {
    if (paramCachedObject.isMemory()) {
      return;
    }
    Row localRow = (Row)paramCachedObject;
    if (localRow.getTable().tableType == 5) {
      this.rowActionMap.put(paramCachedObject.getPos(), localRow.rowAction);
    }
  }
  
  public void setTransactionInfo(PersistentStore paramPersistentStore, CachedObject paramCachedObject)
  {
    if (paramCachedObject.isMemory()) {
      return;
    }
    Row localRow = (Row)paramCachedObject;
    if (localRow.getTable().tableType == 5)
    {
      RowAction localRowAction = (RowAction)this.rowActionMap.get(localRow.getPos());
      localRow.rowAction = localRowAction;
    }
  }
  
  public void removeTransactionInfo(CachedObject paramCachedObject)
  {
    if (paramCachedObject.isMemory()) {
      return;
    }
    this.rowActionMap.remove(paramCachedObject.getPos());
  }
  
  public void removeTransactionInfo(long paramLong)
  {
    this.rowActionMap.getWriteLock().lock();
    try
    {
      RowAction localRowAction = (RowAction)this.rowActionMap.get(paramLong);
      synchronized (localRowAction)
      {
        if (localRowAction.type == 0) {
          this.rowActionMap.remove(paramLong);
        }
      }
    }
    finally
    {
      this.rowActionMap.getWriteLock().unlock();
    }
  }
  
  void addToCommittedQueue(Session paramSession, Object[] paramArrayOfObject)
  {
    synchronized (this.committedTransactionTimestamps)
    {
      this.committedTransactions.addLast(paramArrayOfObject);
      this.committedTransactionTimestamps.addLast(paramSession.actionTimestamp);
    }
  }
  
  void mergeExpiredTransactions(Session paramSession)
  {
    long l1 = getFirstLiveTransactionTimestamp();
    for (;;)
    {
      long l2 = 0L;
      Object[] arrayOfObject = null;
      synchronized (this.committedTransactionTimestamps)
      {
        if (this.committedTransactionTimestamps.isEmpty()) {
          break;
        }
        l2 = this.committedTransactionTimestamps.getFirst();
        if (l2 < l1)
        {
          this.committedTransactionTimestamps.removeFirst();
          arrayOfObject = (Object[])this.committedTransactions.removeFirst();
        }
        else
        {
          break;
        }
      }
      mergeTransaction(arrayOfObject, 0, arrayOfObject.length, l2);
      finaliseRows(paramSession, arrayOfObject, 0, arrayOfObject.length);
    }
  }
  
  public void beginTransaction(Session paramSession)
  {
    this.writeLock.lock();
    try
    {
      if (!paramSession.isTransaction)
      {
        paramSession.actionTimestamp = getNextGlobalChangeTimestamp();
        paramSession.transactionTimestamp = paramSession.actionTimestamp;
        paramSession.isPreTransaction = false;
        paramSession.isTransaction = true;
        this.transactionCount += 1;
        this.liveTransactionTimestamps.addLast(paramSession.transactionTimestamp);
      }
    }
    finally
    {
      this.writeLock.unlock();
    }
  }
  
  public void beginAction(Session paramSession, Statement paramStatement)
  {
    this.writeLock.lock();
    try
    {
      if (paramStatement.getCompileTimestamp() < this.database.schemaManager.getSchemaChangeTimestamp())
      {
        paramStatement = paramSession.statementManager.getStatement(paramSession, paramStatement);
        paramSession.sessionContext.currentStatement = paramStatement;
        if (paramStatement == null) {
          return;
        }
      }
      boolean bool = setWaitedSessionsTPL(paramSession, paramStatement);
      if (bool)
      {
        if (paramSession.tempSet.isEmpty()) {
          lockTablesTPL(paramSession, paramStatement);
        } else {
          setWaitingSessionTPL(paramSession);
        }
      }
      else {
        paramSession.abortTransaction = true;
      }
    }
    finally
    {
      this.writeLock.unlock();
    }
  }
  
  public void beginActionResume(Session paramSession)
  {
    this.writeLock.lock();
    try
    {
      paramSession.actionTimestamp = getNextGlobalChangeTimestamp();
      paramSession.actionStartTimestamp = paramSession.actionTimestamp;
      if (!paramSession.isTransaction)
      {
        paramSession.transactionTimestamp = paramSession.actionTimestamp;
        paramSession.isPreTransaction = false;
        paramSession.isTransaction = true;
        this.liveTransactionTimestamps.addLast(paramSession.transactionTimestamp);
        this.transactionCount += 1;
      }
    }
    finally
    {
      this.writeLock.unlock();
    }
  }
  
  public void resetSession(Session paramSession1, Session paramSession2, int paramInt)
  {
    super.resetSession(paramSession1, paramSession2, paramInt);
  }
  
  private void endTransaction(Session paramSession)
  {
    long l = paramSession.transactionTimestamp;
    int i = this.liveTransactionTimestamps.indexOf(l);
    if (i >= 0)
    {
      this.transactionCount -= 1;
      this.liveTransactionTimestamps.remove(i);
      mergeExpiredTransactions(paramSession);
    }
  }
}


/* Location:              E:\java\java学习\hutubill\lib\all.jar!\org\hsqldb\TransactionManagerMV2PL.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */
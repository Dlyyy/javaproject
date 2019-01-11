package org.hsqldb;

import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantReadWriteLock.WriteLock;
import org.hsqldb.error.Error;
import org.hsqldb.lib.CountUpDownLatch;
import org.hsqldb.lib.HashMappedList;
import org.hsqldb.lib.HsqlArrayList;
import org.hsqldb.lib.HsqlDeque;
import org.hsqldb.lib.LongDeque;
import org.hsqldb.lib.LongKeyHashMap;
import org.hsqldb.lib.OrderedHashSet;
import org.hsqldb.persist.CachedObject;
import org.hsqldb.persist.Logger;
import org.hsqldb.persist.PersistentStore;

public class TransactionManagerMVCC
  extends TransactionManagerCommon
  implements TransactionManager
{
  HsqlDeque committedTransactions = new HsqlDeque();
  LongDeque committedTransactionTimestamps = new LongDeque();
  boolean isLockedMode;
  Session catalogWriteSession;
  long lockTxTs;
  long lockSessionId;
  long unlockTxTs;
  long unlockSessionId;
  int redoCount = 0;
  
  public TransactionManagerMVCC(Database paramDatabase)
  {
    this.database = paramDatabase;
    this.lobSession = this.database.sessionManager.getSysLobSession();
    this.rowActionMap = new LongKeyHashMap(8192);
    this.txModel = 2;
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
    return true;
  }
  
  public boolean is2PL()
  {
    return false;
  }
  
  public int getTransactionControl()
  {
    return 2;
  }
  
  public void setTransactionControl(Session paramSession, int paramInt)
  {
    super.setTransactionControl(paramSession, paramInt);
  }
  
  public void completeActions(Session paramSession) {}
  
  public boolean prepareCommitActions(Session paramSession)
  {
    if (paramSession.abortTransaction) {
      return false;
    }
    this.writeLock.lock();
    try
    {
      int i = paramSession.rowActionList.size();
      Object localObject1;
      for (int j = 0; j < i; j++)
      {
        localObject1 = (RowAction)paramSession.rowActionList.get(j);
        if (!((RowAction)localObject1).canCommit(paramSession, paramSession.actionSet))
        {
          boolean bool = false;
          return bool;
        }
      }
      paramSession.actionTimestamp = getNextGlobalChangeTimestamp();
      for (j = 0; j < i; j++)
      {
        localObject1 = (RowAction)paramSession.rowActionList.get(j);
        ((RowAction)localObject1).prepareCommit(paramSession);
      }
      for (j = 0; j < paramSession.actionSet.size(); j++)
      {
        localObject1 = ((RowActionBase)paramSession.actionSet.get(j)).session;
        ((Session)localObject1).abortTransaction = true;
      }
      j = 1;
      return j;
    }
    finally
    {
      this.writeLock.unlock();
      paramSession.actionSet.clear();
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
      Object localObject1;
      for (int j = 0; j < i; j++)
      {
        localObject1 = (RowAction)paramSession.rowActionList.get(j);
        if (!((RowAction)localObject1).canCommit(paramSession, paramSession.actionSet))
        {
          boolean bool = false;
          return bool;
        }
      }
      paramSession.actionTimestamp = getNextGlobalChangeTimestamp();
      paramSession.transactionEndTimestamp = paramSession.actionTimestamp;
      endTransaction(paramSession);
      for (j = 0; j < i; j++)
      {
        localObject1 = (RowAction)paramSession.rowActionList.get(j);
        ((RowAction)localObject1).commit(paramSession);
      }
      for (j = 0; j < paramSession.actionSet.size(); j++)
      {
        localObject1 = ((RowActionBase)paramSession.actionSet.get(j)).session;
        ((Session)localObject1).abortTransaction = true;
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
      else if (paramSession.rowActionList.size() > 0)
      {
        localObject1 = paramSession.rowActionList.toArray();
        addToCommittedQueue(paramSession, (Object[])localObject1);
      }
      endTransactionTPL(paramSession);
      paramSession.isTransaction = false;
      countDownLatches(paramSession);
    }
    finally
    {
      this.writeLock.unlock();
      paramSession.actionSet.clear();
    }
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
      endTransactionTPL(paramSession);
      paramSession.isTransaction = false;
      countDownLatches(paramSession);
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
    RowAction localRowAction = addDeleteActionToRow(paramSession, paramTable, paramPersistentStore, paramRow, paramArrayOfInt);
    Session localSession = null;
    boolean bool = true;
    if (localRowAction == null)
    {
      this.writeLock.lock();
      try
      {
        rollbackAction(paramSession);
        if ((paramSession.isolationLevel == 4) || (paramSession.isolationLevel == 8))
        {
          paramSession.actionSet.clear();
          paramSession.redoAction = false;
          paramSession.abortTransaction = paramSession.txConflictRollback;
          throw Error.error(4871);
        }
        if ((paramRow.rowAction != null) && (paramRow.rowAction.isDeleted()))
        {
          paramSession.actionSet.clear();
          paramSession.redoAction = true;
          this.redoCount += 1;
          throw Error.error(4871);
        }
        bool = !paramSession.actionSet.isEmpty();
        if (bool)
        {
          localSession = ((RowActionBase)paramSession.actionSet.get(0)).session;
          paramSession.actionSet.clear();
          if (localSession != null) {
            bool = checkDeadlock(paramSession, localSession);
          }
        }
        if (bool)
        {
          paramSession.redoAction = true;
          if (localSession != null)
          {
            localSession.waitingSessions.add(paramSession);
            paramSession.waitedSessions.add(localSession);
            paramSession.latch.setCount(paramSession.waitedSessions.size());
          }
          this.redoCount += 1;
        }
        else
        {
          paramSession.redoAction = false;
          paramSession.abortTransaction = paramSession.txConflictRollback;
        }
        throw Error.error(4871);
      }
      finally
      {
        this.writeLock.unlock();
      }
    }
    paramSession.rowActionList.add(localRowAction);
    return localRowAction;
  }
  
  public void addInsertAction(Session paramSession, Table paramTable, PersistentStore paramPersistentStore, Row paramRow, int[] paramArrayOfInt)
  {
    RowAction localRowAction = paramRow.rowAction;
    Session localSession = null;
    boolean bool = false;
    int i = 1;
    Object localObject1 = null;
    if (localRowAction == null) {
      throw Error.runtimeError(458, "TXManager - null insert action ");
    }
    try
    {
      paramPersistentStore.indexRow(paramSession, paramRow);
    }
    catch (HsqlException localHsqlException)
    {
      if (paramSession.actionSet.isEmpty()) {
        throw localHsqlException;
      }
      bool = true;
      localObject1 = localHsqlException;
    }
    if (!bool)
    {
      if (paramTable.persistenceScope == 20)
      {
        paramRow.rowAction = null;
        return;
      }
      paramSession.rowActionList.add(localRowAction);
      return;
    }
    this.writeLock.lock();
    try
    {
      rollbackAction(paramSession);
      RowActionBase localRowActionBase = (RowActionBase)paramSession.actionSet.get(0);
      localSession = localRowActionBase.session;
      paramSession.actionSet.clear();
      if (localRowActionBase.commitTimestamp != 0L) {
        i = 0;
      }
      switch (paramSession.isolationLevel)
      {
      case 4: 
      case 8: 
        bool = false;
        break;
      default: 
        bool = checkDeadlock(paramSession, localSession);
      }
      if (bool)
      {
        paramSession.redoAction = true;
        if (i != 0)
        {
          localSession.waitingSessions.add(paramSession);
          paramSession.waitedSessions.add(localSession);
          paramSession.latch.setCount(paramSession.waitedSessions.size());
        }
        this.redoCount += 1;
      }
      else
      {
        paramSession.abortTransaction = paramSession.txConflictRollback;
        paramSession.redoAction = false;
      }
      throw Error.error((Throwable)localObject1, 4871, null);
    }
    finally
    {
      this.writeLock.unlock();
    }
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
    if (paramInt == 0) {
      return localRowAction.canRead(paramSession, 0);
    }
    if (paramInt == 2) {
      return localRowAction.canRead(paramSession, 0);
    }
    return localRowAction.canRead(paramSession, paramInt);
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
    if (localRow.getTable().tableType == 5)
    {
      RowAction localRowAction = (RowAction)this.rowActionMap.get(paramCachedObject.getPos());
      if (localRowAction != null)
      {
        HsqlException localHsqlException = Error.error(4871, "TXManager - row exists");
        this.database.logger.logSevereEvent("TXManager MVROWS", localHsqlException);
        throw localHsqlException;
      }
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
      long l2;
      Object[] arrayOfObject;
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
        this.liveTransactionTimestamps.addLast(paramSession.transactionTimestamp);
        this.transactionCount += 1;
      }
    }
    finally
    {
      this.writeLock.unlock();
    }
  }
  
  public void beginAction(Session paramSession, Statement paramStatement)
  {
    if (paramSession.isTransaction) {
      return;
    }
    if (paramStatement == null) {
      return;
    }
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
      if (paramSession.abortTransaction) {
        return;
      }
      paramSession.isPreTransaction = true;
      if ((!this.isLockedMode) && (!paramStatement.isCatalogLock())) {
        return;
      }
      beginActionTPL(paramSession, paramStatement);
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
      if (paramSession.isTransaction) {
        return;
      }
      paramSession.transactionTimestamp = paramSession.actionTimestamp;
      paramSession.isPreTransaction = false;
      paramSession.isTransaction = true;
      this.liveTransactionTimestamps.addLast(paramSession.transactionTimestamp);
      this.transactionCount += 1;
    }
    finally
    {
      this.writeLock.unlock();
    }
  }
  
  RowAction addDeleteActionToRow(Session paramSession, Table paramTable, PersistentStore paramPersistentStore, Row paramRow, int[] paramArrayOfInt)
  {
    RowAction localRowAction = null;
    synchronized (paramRow)
    {
      switch (paramTable.tableType)
      {
      case 5: 
        this.rowActionMap.getWriteLock().lock();
        try
        {
          localRowAction = (RowAction)this.rowActionMap.get(paramRow.getPos());
          if (localRowAction == null)
          {
            localRowAction = RowAction.addDeleteAction(paramSession, paramTable, paramRow, paramArrayOfInt);
            if (localRowAction != null) {
              addTransactionInfo(paramRow);
            }
          }
          else
          {
            paramRow.rowAction = localRowAction;
            localRowAction = RowAction.addDeleteAction(paramSession, paramTable, paramRow, paramArrayOfInt);
          }
        }
        finally
        {
          this.rowActionMap.getWriteLock().unlock();
        }
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
    return localRowAction;
  }
  
  void endTransaction(Session paramSession)
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
  
  private void countDownLatches(Session paramSession)
  {
    for (int i = 0; i < paramSession.waitingSessions.size(); i++)
    {
      Session localSession = (Session)paramSession.waitingSessions.get(i);
      localSession.waitedSessions.remove(paramSession);
      localSession.latch.setCount(localSession.waitedSessions.size());
    }
    paramSession.waitedSessions.clear();
    paramSession.waitingSessions.clear();
  }
  
  void endTransactionTPL(Session paramSession)
  {
    if (this.catalogWriteSession != paramSession) {
      return;
    }
    Object localObject = null;
    Session localSession;
    for (int i = 0; i < paramSession.waitingSessions.size(); i++)
    {
      localSession = (Session)paramSession.waitingSessions.get(i);
      Statement localStatement = localSession.sessionContext.currentStatement;
      if ((localStatement != null) && (localStatement.isCatalogLock()))
      {
        localObject = localSession;
        break;
      }
    }
    if (localObject == null)
    {
      this.catalogWriteSession = null;
      this.isLockedMode = false;
    }
    else
    {
      for (i = 0; i < paramSession.waitingSessions.size(); i++)
      {
        localSession = (Session)paramSession.waitingSessions.get(i);
        if (localSession != localObject)
        {
          localSession.waitedSessions.add(localObject);
          ((Session)localObject).waitingSessions.add(localSession);
          localSession.latch.setCount(localSession.waitedSessions.size());
        }
      }
      this.catalogWriteSession = ((Session)localObject);
    }
    this.unlockTxTs = paramSession.actionTimestamp;
    this.unlockSessionId = paramSession.getId();
  }
  
  boolean beginActionTPL(Session paramSession, Statement paramStatement)
  {
    if (paramSession == this.catalogWriteSession) {
      return true;
    }
    paramSession.tempSet.clear();
    if ((paramStatement.isCatalogLock()) && (this.catalogWriteSession == null))
    {
      this.catalogWriteSession = paramSession;
      this.isLockedMode = true;
      this.lockTxTs = paramSession.actionTimestamp;
      this.lockSessionId = paramSession.getId();
      getTransactionAndPreSessions(paramSession);
      if (!paramSession.tempSet.isEmpty())
      {
        paramSession.waitedSessions.addAll(paramSession.tempSet);
        setWaitingSessionTPL(paramSession);
      }
      return true;
    }
    if (!this.isLockedMode) {
      return true;
    }
    if (paramStatement.getTableNamesForWrite().length > 0)
    {
      if (paramStatement.getTableNamesForWrite()[0].schema == SqlInvariants.LOBS_SCHEMA_HSQLNAME) {
        return true;
      }
    }
    else if ((paramStatement.getTableNamesForRead().length > 0) && (paramStatement.getTableNamesForRead()[0].schema == SqlInvariants.LOBS_SCHEMA_HSQLNAME)) {
      return true;
    }
    if (paramSession.waitingSessions.contains(this.catalogWriteSession)) {
      return true;
    }
    if (this.catalogWriteSession.waitingSessions.add(paramSession))
    {
      paramSession.waitedSessions.add(this.catalogWriteSession);
      paramSession.latch.setCount(paramSession.waitedSessions.size());
    }
    return true;
  }
  
  public void resetSession(Session paramSession1, Session paramSession2, int paramInt)
  {
    super.resetSession(paramSession1, paramSession2, paramInt);
  }
}


/* Location:              E:\java\java学习\hutubill\lib\all.jar!\org\hsqldb\TransactionManagerMVCC.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */
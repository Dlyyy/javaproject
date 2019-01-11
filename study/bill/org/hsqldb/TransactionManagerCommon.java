package org.hsqldb;

import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock.WriteLock;
import org.hsqldb.error.Error;
import org.hsqldb.lib.ArrayUtil;
import org.hsqldb.lib.Collection;
import org.hsqldb.lib.CountUpDownLatch;
import org.hsqldb.lib.HashMap;
import org.hsqldb.lib.HsqlArrayList;
import org.hsqldb.lib.Iterator;
import org.hsqldb.lib.LongDeque;
import org.hsqldb.lib.LongKeyHashMap;
import org.hsqldb.lib.MultiValueHashMap;
import org.hsqldb.lib.OrderedHashSet;
import org.hsqldb.persist.Logger;
import org.hsqldb.persist.PersistentStore;
import org.hsqldb.persist.PersistentStoreCollectionSession;

class TransactionManagerCommon
{
  Database database;
  Session lobSession;
  int txModel;
  HsqlNameManager.HsqlName[] catalogNameList;
  ReentrantReadWriteLock lock = new ReentrantReadWriteLock();
  ReentrantReadWriteLock.WriteLock writeLock = this.lock.writeLock();
  LongDeque liveTransactionTimestamps = new LongDeque();
  AtomicLong globalChangeTimestamp = new AtomicLong(1L);
  int transactionCount = 0;
  HashMap tableWriteLocks = new HashMap();
  MultiValueHashMap tableReadLocks = new MultiValueHashMap();
  public LongKeyHashMap rowActionMap;
  
  void setTransactionControl(Session paramSession, int paramInt)
  {
    Object localObject1 = null;
    if (paramInt == this.txModel) {
      return;
    }
    this.writeLock.lock();
    try
    {
      switch (this.txModel)
      {
      case 1: 
      case 2: 
        if (this.liveTransactionTimestamps.size() != 1) {
          throw Error.error(3701);
        }
        break;
      }
      switch (paramInt)
      {
      case 2: 
        localObject1 = new TransactionManagerMVCC(this.database);
        ((TransactionManagerCommon)localObject1).liveTransactionTimestamps.addLast(paramSession.transactionTimestamp);
        break;
      case 1: 
        localObject1 = new TransactionManagerMV2PL(this.database);
        ((TransactionManagerCommon)localObject1).liveTransactionTimestamps.addLast(paramSession.transactionTimestamp);
        break;
      case 0: 
        localObject1 = new TransactionManager2PL(this.database);
        break;
      default: 
        throw Error.runtimeError(201, "TransactionManagerCommon");
      }
      ((TransactionManagerCommon)localObject1).globalChangeTimestamp.set(this.globalChangeTimestamp.get());
      ((TransactionManagerCommon)localObject1).transactionCount = this.transactionCount;
      this.database.txManager = ((TransactionManager)localObject1);
    }
    finally
    {
      this.writeLock.unlock();
    }
  }
  
  void adjustLobUsage(Session paramSession)
  {
    RowAction localRowAction1 = paramSession.rowActionList.size();
    long l = paramSession.actionTimestamp;
    RowAction localRowAction3;
    for (RowAction localRowAction2 = 0; localRowAction2 < localRowAction1; localRowAction2++)
    {
      localRowAction3 = (RowAction)paramSession.rowActionList.get(localRowAction2);
      if ((localRowAction3.type != 0) && (localRowAction3.table.hasLobColumn))
      {
        int i = localRowAction3.getCommitTypeOn(l);
        Row localRow = localRowAction3.memoryRow;
        if (localRow == null) {
          localRow = (Row)localRowAction3.store.get(localRowAction3.getPos(), false);
        }
        switch (i)
        {
        case 1: 
          paramSession.sessionData.adjustLobUsageCount(localRowAction3.table, localRow.getData(), 1);
          break;
        case 2: 
          paramSession.sessionData.adjustLobUsageCount(localRowAction3.table, localRow.getData(), -1);
        }
      }
    }
    localRowAction2 = paramSession.rowActionList.size();
    if (localRowAction2 > localRowAction1) {
      for (localRowAction3 = localRowAction1; localRowAction3 < localRowAction2; localRowAction3++)
      {
        RowAction localRowAction4 = (RowAction)paramSession.rowActionList.get(localRowAction3);
        localRowAction4.commit(paramSession);
      }
    }
  }
  
  void persistCommit(Session paramSession)
  {
    int i = paramSession.rowActionList.size();
    int j = 0;
    for (int k = 0; k < i; k++)
    {
      RowAction localRowAction = (RowAction)paramSession.rowActionList.get(k);
      if (localRowAction.type != 0)
      {
        int m = localRowAction.getCommitTypeOn(paramSession.actionTimestamp);
        Row localRow = localRowAction.memoryRow;
        if (localRow == null) {
          localRow = (Row)localRowAction.store.get(localRowAction.getPos(), false);
        }
        if (localRowAction.table.tableType != 3) {
          j = 1;
        }
        try
        {
          localRowAction.store.commitRow(paramSession, localRow, m, this.txModel);
          if ((this.txModel == 0) || (localRowAction.table.tableType == 3))
          {
            localRowAction.setAsNoOp();
            localRow.rowAction = null;
          }
        }
        catch (HsqlException localHsqlException2)
        {
          this.database.logger.logWarningEvent("data commit failed", localHsqlException2);
        }
      }
    }
    try
    {
      paramSession.logSequences();
      if ((i > 0) && (j != 0)) {
        this.database.logger.writeCommitStatement(paramSession);
      }
    }
    catch (HsqlException localHsqlException1)
    {
      this.database.logger.logWarningEvent("data commit logging failed", localHsqlException1);
    }
  }
  
  void finaliseRows(Session paramSession, Object[] paramArrayOfObject, int paramInt1, int paramInt2)
  {
    for (int i = paramInt1; i < paramInt2; i++)
    {
      RowAction localRowAction = (RowAction)paramArrayOfObject[i];
      localRowAction.store.postCommitAction(paramSession, localRowAction);
    }
  }
  
  void mergeTransaction(Object[] paramArrayOfObject, int paramInt1, int paramInt2, long paramLong)
  {
    for (int i = paramInt1; i < paramInt2; i++)
    {
      RowAction localRowAction = (RowAction)paramArrayOfObject[i];
      localRowAction.mergeToTimestamp(paramLong);
    }
  }
  
  public long getNextGlobalChangeTimestamp()
  {
    return this.globalChangeTimestamp.incrementAndGet();
  }
  
  boolean checkDeadlock(Session paramSession, OrderedHashSet paramOrderedHashSet)
  {
    int i = paramSession.waitingSessions.size();
    for (int j = 0; j < i; j++)
    {
      Session localSession = (Session)paramSession.waitingSessions.get(j);
      if (paramOrderedHashSet.contains(localSession)) {
        return false;
      }
      if (!checkDeadlock(localSession, paramOrderedHashSet)) {
        return false;
      }
    }
    return true;
  }
  
  boolean checkDeadlock(Session paramSession1, Session paramSession2)
  {
    int i = paramSession1.waitingSessions.size();
    for (int j = 0; j < i; j++)
    {
      Session localSession = (Session)paramSession1.waitingSessions.get(j);
      if (localSession == paramSession2) {
        return false;
      }
      if (!checkDeadlock(localSession, paramSession2)) {
        return false;
      }
    }
    return true;
  }
  
  void getTransactionSessions(Session paramSession)
  {
    OrderedHashSet localOrderedHashSet = paramSession.tempSet;
    Session[] arrayOfSession = this.database.sessionManager.getAllSessions();
    for (int i = 0; i < arrayOfSession.length; i++)
    {
      long l = arrayOfSession[i].transactionTimestamp;
      if ((paramSession != arrayOfSession[i]) && (arrayOfSession[i].isTransaction)) {
        localOrderedHashSet.add(arrayOfSession[i]);
      }
    }
  }
  
  void getTransactionAndPreSessions(Session paramSession)
  {
    OrderedHashSet localOrderedHashSet = paramSession.tempSet;
    Session[] arrayOfSession = this.database.sessionManager.getAllSessions();
    for (int i = 0; i < arrayOfSession.length; i++)
    {
      long l = arrayOfSession[i].transactionTimestamp;
      if (paramSession != arrayOfSession[i]) {
        if (arrayOfSession[i].isPreTransaction) {
          localOrderedHashSet.add(arrayOfSession[i]);
        } else if (arrayOfSession[i].isTransaction) {
          localOrderedHashSet.add(arrayOfSession[i]);
        }
      }
    }
  }
  
  void endActionTPL(Session paramSession)
  {
    if ((paramSession.isolationLevel == 4) || (paramSession.isolationLevel == 8)) {
      return;
    }
    if (paramSession.sessionContext.currentStatement == null) {
      return;
    }
    if (paramSession.sessionContext.depth > 0) {
      return;
    }
    HsqlNameManager.HsqlName[] arrayOfHsqlName = paramSession.sessionContext.currentStatement.getTableNamesForRead();
    if (arrayOfHsqlName.length == 0) {
      return;
    }
    this.writeLock.lock();
    try
    {
      unlockReadTablesTPL(paramSession, arrayOfHsqlName);
      int i = paramSession.waitingSessions.size();
      if (i == 0) {
        return;
      }
      int j = 0;
      for (int k = 0; k < arrayOfHsqlName.length; k++) {
        if (this.tableWriteLocks.get(arrayOfHsqlName[k]) != paramSession)
        {
          j = 1;
          break;
        }
      }
      if (j == 0) {
        return;
      }
      j = 0;
      for (k = 0; k < i; k++)
      {
        Session localSession = (Session)paramSession.waitingSessions.get(k);
        if (localSession.abortTransaction)
        {
          j = 1;
          break;
        }
        Statement localStatement = localSession.sessionContext.currentStatement;
        if (localStatement == null)
        {
          j = 1;
          break;
        }
        if (ArrayUtil.containsAny(arrayOfHsqlName, localStatement.getTableNamesForWrite()))
        {
          j = 1;
          break;
        }
      }
      if (j == 0) {
        return;
      }
      resetLocks(paramSession);
      resetLatchesMidTransaction(paramSession);
    }
    finally
    {
      this.writeLock.unlock();
    }
  }
  
  void endTransactionTPL(Session paramSession)
  {
    unlockTablesTPL(paramSession);
    int i = paramSession.waitingSessions.size();
    if (i == 0) {
      return;
    }
    resetLocks(paramSession);
    resetLatches(paramSession);
  }
  
  void resetLocks(Session paramSession)
  {
    int i = paramSession.waitingSessions.size();
    Session localSession;
    for (int j = 0; j < i; j++)
    {
      localSession = (Session)paramSession.waitingSessions.get(j);
      localSession.tempUnlocked = false;
      long l = localSession.latch.getCount();
      if (l == 1L)
      {
        boolean bool = setWaitedSessionsTPL(localSession, localSession.sessionContext.currentStatement);
        if ((bool) && (localSession.tempSet.isEmpty()))
        {
          lockTablesTPL(localSession, localSession.sessionContext.currentStatement);
          localSession.tempUnlocked = true;
        }
      }
    }
    for (j = 0; j < i; j++)
    {
      localSession = (Session)paramSession.waitingSessions.get(j);
      if ((!localSession.tempUnlocked) && (!localSession.abortTransaction)) {
        setWaitedSessionsTPL(localSession, localSession.sessionContext.currentStatement);
      }
    }
  }
  
  void resetLatches(Session paramSession)
  {
    int i = paramSession.waitingSessions.size();
    for (int j = 0; j < i; j++)
    {
      Session localSession = (Session)paramSession.waitingSessions.get(j);
      setWaitingSessionTPL(localSession);
    }
    paramSession.waitingSessions.clear();
    paramSession.latch.setCount(0);
  }
  
  void resetLatchesMidTransaction(Session paramSession)
  {
    paramSession.tempSet.clear();
    paramSession.tempSet.addAll(paramSession.waitingSessions);
    paramSession.waitingSessions.clear();
    int i = paramSession.tempSet.size();
    for (int j = 0; j < i; j++)
    {
      Session localSession = (Session)paramSession.tempSet.get(j);
      if ((!localSession.abortTransaction) && (localSession.tempSet.isEmpty())) {}
      setWaitingSessionTPL(localSession);
    }
    paramSession.tempSet.clear();
  }
  
  boolean setWaitedSessionsTPL(Session paramSession, Statement paramStatement)
  {
    paramSession.tempSet.clear();
    if (paramStatement == null) {
      return true;
    }
    if (paramSession.abortTransaction) {
      return false;
    }
    if (paramStatement.isCatalogLock()) {
      getTransactionSessions(paramSession);
    }
    HsqlNameManager.HsqlName[] arrayOfHsqlName = paramStatement.getTableNamesForWrite();
    HsqlNameManager.HsqlName localHsqlName;
    Session localSession;
    for (int i = 0; i < arrayOfHsqlName.length; i++)
    {
      localHsqlName = arrayOfHsqlName[i];
      if (localHsqlName.schema != SqlInvariants.SYSTEM_SCHEMA_HSQLNAME)
      {
        localSession = (Session)this.tableWriteLocks.get(localHsqlName);
        if ((localSession != null) && (localSession != paramSession)) {
          paramSession.tempSet.add(localSession);
        }
        Iterator localIterator = this.tableReadLocks.get(localHsqlName);
        while (localIterator.hasNext())
        {
          localSession = (Session)localIterator.next();
          if (localSession != paramSession) {
            paramSession.tempSet.add(localSession);
          }
        }
      }
    }
    arrayOfHsqlName = paramStatement.getTableNamesForRead();
    if ((this.txModel == 1) && (paramSession.isReadOnly()) && (arrayOfHsqlName.length > 0)) {
      arrayOfHsqlName = this.catalogNameList;
    }
    for (i = 0; i < arrayOfHsqlName.length; i++)
    {
      localHsqlName = arrayOfHsqlName[i];
      if (localHsqlName.schema != SqlInvariants.SYSTEM_SCHEMA_HSQLNAME)
      {
        localSession = (Session)this.tableWriteLocks.get(localHsqlName);
        if ((localSession != null) && (localSession != paramSession)) {
          paramSession.tempSet.add(localSession);
        }
      }
    }
    if (paramSession.tempSet.isEmpty()) {
      return true;
    }
    if (checkDeadlock(paramSession, paramSession.tempSet)) {
      return true;
    }
    paramSession.tempSet.clear();
    paramSession.abortTransaction = true;
    return false;
  }
  
  void setWaitingSessionTPL(Session paramSession)
  {
    int i = paramSession.tempSet.size();
    assert (paramSession.latch.getCount() <= i + 1);
    for (int j = 0; j < i; j++)
    {
      Session localSession = (Session)paramSession.tempSet.get(j);
      localSession.waitingSessions.add(paramSession);
    }
    paramSession.tempSet.clear();
    paramSession.latch.setCount(i);
  }
  
  void lockTablesTPL(Session paramSession, Statement paramStatement)
  {
    if ((paramStatement == null) || (paramSession.abortTransaction)) {
      return;
    }
    HsqlNameManager.HsqlName[] arrayOfHsqlName = paramStatement.getTableNamesForWrite();
    HsqlNameManager.HsqlName localHsqlName;
    for (int i = 0; i < arrayOfHsqlName.length; i++)
    {
      localHsqlName = arrayOfHsqlName[i];
      if (localHsqlName.schema != SqlInvariants.SYSTEM_SCHEMA_HSQLNAME) {
        this.tableWriteLocks.put(localHsqlName, paramSession);
      }
    }
    arrayOfHsqlName = paramStatement.getTableNamesForRead();
    if ((this.txModel == 1) && (paramSession.isReadOnly()) && (arrayOfHsqlName.length > 0)) {
      arrayOfHsqlName = this.catalogNameList;
    }
    for (i = 0; i < arrayOfHsqlName.length; i++)
    {
      localHsqlName = arrayOfHsqlName[i];
      if (localHsqlName.schema != SqlInvariants.SYSTEM_SCHEMA_HSQLNAME) {
        this.tableReadLocks.put(localHsqlName, paramSession);
      }
    }
  }
  
  void unlockTablesTPL(Session paramSession)
  {
    Iterator localIterator = this.tableWriteLocks.values().iterator();
    Session localSession;
    while (localIterator.hasNext())
    {
      localSession = (Session)localIterator.next();
      if (localSession == paramSession) {
        localIterator.remove();
      }
    }
    localIterator = this.tableReadLocks.values().iterator();
    while (localIterator.hasNext())
    {
      localSession = (Session)localIterator.next();
      if (localSession == paramSession) {
        localIterator.remove();
      }
    }
  }
  
  void unlockReadTablesTPL(Session paramSession, HsqlNameManager.HsqlName[] paramArrayOfHsqlName)
  {
    for (int i = 0; i < paramArrayOfHsqlName.length; i++) {
      this.tableReadLocks.remove(paramArrayOfHsqlName[i], paramSession);
    }
  }
  
  boolean hasLocks(Session paramSession, Statement paramStatement)
  {
    if (paramStatement == null) {
      return true;
    }
    HsqlNameManager.HsqlName[] arrayOfHsqlName = paramStatement.getTableNamesForWrite();
    HsqlNameManager.HsqlName localHsqlName;
    Session localSession;
    for (int i = 0; i < arrayOfHsqlName.length; i++)
    {
      localHsqlName = arrayOfHsqlName[i];
      if (localHsqlName.schema != SqlInvariants.SYSTEM_SCHEMA_HSQLNAME)
      {
        localSession = (Session)this.tableWriteLocks.get(localHsqlName);
        if ((localSession != null) && (localSession != paramSession)) {
          return false;
        }
        Iterator localIterator = this.tableReadLocks.get(localHsqlName);
        while (localIterator.hasNext())
        {
          localSession = (Session)localIterator.next();
          if (localSession != paramSession) {
            return false;
          }
        }
      }
    }
    arrayOfHsqlName = paramStatement.getTableNamesForRead();
    for (i = 0; i < arrayOfHsqlName.length; i++)
    {
      localHsqlName = arrayOfHsqlName[i];
      if (localHsqlName.schema != SqlInvariants.SYSTEM_SCHEMA_HSQLNAME)
      {
        localSession = (Session)this.tableWriteLocks.get(localHsqlName);
        if ((localSession != null) && (localSession != paramSession)) {
          return false;
        }
      }
    }
    return true;
  }
  
  long getFirstLiveTransactionTimestamp()
  {
    if (this.liveTransactionTimestamps.isEmpty()) {
      return Long.MAX_VALUE;
    }
    return this.liveTransactionTimestamps.get(0);
  }
  
  RowAction[] getRowActionList()
  {
    this.writeLock.lock();
    try
    {
      Session[] arrayOfSession = this.database.sessionManager.getAllSessions();
      int[] arrayOfInt = new int[arrayOfSession.length];
      int i = 0;
      int j = 0;
      for (int k = 0; k < arrayOfSession.length; k++) {
        j += arrayOfSession[k].getTransactionSize();
      }
      RowAction[] arrayOfRowAction = new RowAction[j];
      for (;;)
      {
        j = 0;
        long l = Long.MAX_VALUE;
        int m = 0;
        for (int n = 0; n < arrayOfSession.length; n++)
        {
          int i1 = arrayOfSession[n].getTransactionSize();
          if (arrayOfInt[n] < i1)
          {
            RowAction localRowAction2 = (RowAction)arrayOfSession[n].rowActionList.get(arrayOfInt[n]);
            if (localRowAction2.actionTimestamp < l)
            {
              l = localRowAction2.actionTimestamp;
              m = n;
            }
            j = 1;
          }
        }
        if (j == 0) {
          break;
        }
        HsqlArrayList localHsqlArrayList = arrayOfSession[m].rowActionList;
        while (arrayOfInt[m] < localHsqlArrayList.size())
        {
          RowAction localRowAction1 = (RowAction)localHsqlArrayList.get(arrayOfInt[m]);
          if (localRowAction1.actionTimestamp == l + 1L) {
            l += 1L;
          }
          if (localRowAction1.actionTimestamp != l) {
            break;
          }
          arrayOfRowAction[(i++)] = localRowAction1;
          arrayOfInt[m] += 1;
        }
      }
      Object localObject1 = arrayOfRowAction;
      return (RowAction[])localObject1;
    }
    finally
    {
      this.writeLock.unlock();
    }
  }
  
  void resetSession(Session paramSession1, Session paramSession2, int paramInt)
  {
    this.writeLock.lock();
    try
    {
      switch (paramInt)
      {
      case 1: 
        if (!paramSession2.isInMidTransaction()) {
          paramSession2.sessionData.closeAllNavigators();
        }
        break;
      case 2: 
        if (!paramSession2.isInMidTransaction()) {
          paramSession2.sessionData.persistentStoreCollection.clearAllTables();
        }
        break;
      case 3: 
        if (!paramSession2.isInMidTransaction()) {
          paramSession2.resetSession();
        }
        break;
      case 4: 
        if (paramSession1 == paramSession2) {
          return;
        }
        if (paramSession2.isInMidTransaction())
        {
          prepareReset(paramSession2);
          if (paramSession2.latch.getCount() > 0L)
          {
            paramSession2.abortTransaction = true;
            paramSession2.latch.setCount(0);
          }
          else
          {
            paramSession2.abortTransaction = true;
          }
        }
        break;
      case 5: 
        if (paramSession1 == paramSession2) {
          return;
        }
        if (paramSession2.isInMidTransaction())
        {
          prepareReset(paramSession2);
          if (paramSession2.latch.getCount() > 0L)
          {
            paramSession2.abortAction = true;
            paramSession2.latch.setCount(0);
          }
          else
          {
            paramSession2.abortAction = true;
          }
        }
        break;
      case 6: 
        if (paramSession1 == paramSession2) {
          return;
        }
        if (!paramSession2.isInMidTransaction())
        {
          paramSession2.rollbackNoCheck(true);
          paramSession2.close();
        }
        break;
      }
    }
    finally
    {
      this.writeLock.unlock();
    }
  }
  
  void prepareReset(Session paramSession)
  {
    OrderedHashSet localOrderedHashSet = paramSession.waitedSessions;
    for (int i = 0; i < localOrderedHashSet.size(); i++)
    {
      Session localSession = (Session)localOrderedHashSet.get(i);
      localSession.waitingSessions.remove(paramSession);
    }
    localOrderedHashSet.clear();
  }
  
  public void abortAction(Session paramSession) {}
}


/* Location:              E:\java\java学习\hutubill\lib\all.jar!\org\hsqldb\TransactionManagerCommon.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */
package org.hsqldb.persist;

import org.hsqldb.Row;
import org.hsqldb.RowAVL;
import org.hsqldb.RowAction;
import org.hsqldb.Session;
import org.hsqldb.Table;
import org.hsqldb.TableBase;
import org.hsqldb.index.Index;
import org.hsqldb.index.NodeAVL;
import org.hsqldb.navigator.RowIterator;

public class RowStoreAVLHybridExtended
  extends RowStoreAVLHybrid
{
  Session session;
  
  public RowStoreAVLHybridExtended(Session paramSession, TableBase paramTableBase, boolean paramBoolean)
  {
    super(paramSession, paramTableBase, paramBoolean);
    this.session = paramSession;
  }
  
  public CachedObject getNewCachedObject(Session paramSession, Object paramObject, boolean paramBoolean)
  {
    return super.getNewCachedObject(paramSession, paramObject, paramBoolean);
  }
  
  public synchronized void add(Session paramSession, CachedObject paramCachedObject, boolean paramBoolean)
  {
    super.add(paramSession, paramCachedObject, paramBoolean);
    if (paramBoolean) {
      RowAction.addInsertAction(paramSession, (Table)this.table, (Row)paramCachedObject);
    }
  }
  
  public void indexRow(Session paramSession, Row paramRow)
  {
    NodeAVL localNodeAVL = ((RowAVL)paramRow).getNode(0);
    int i = 0;
    while (localNodeAVL != null)
    {
      i++;
      localNodeAVL = localNodeAVL.nNext;
    }
    if (((this.isCached ^ !paramRow.isMemory())) || (i != this.indexList.length)) {
      paramRow = (Row)getNewCachedObject(paramSession, paramRow.getData(), true);
    }
    super.indexRow(paramSession, paramRow);
  }
  
  public void delete(Session paramSession, Row paramRow)
  {
    NodeAVL localNodeAVL = ((RowAVL)paramRow).getNode(0);
    int i = 0;
    while (localNodeAVL != null)
    {
      i++;
      localNodeAVL = localNodeAVL.nNext;
    }
    if (((this.isCached ^ !paramRow.isMemory())) || (i != this.indexList.length)) {
      paramRow = ((Table)this.table).getDeleteRowFromLog(paramSession, paramRow.getData());
    }
    if (paramRow != null) {
      super.delete(paramSession, paramRow);
    }
  }
  
  public CachedObject getAccessor(Index paramIndex)
  {
    return super.getAccessor(paramIndex);
  }
  
  public synchronized void resetAccessorKeys(Session paramSession, Index[] paramArrayOfIndex)
  {
    if ((this.indexList.length == 0) || (this.accessorList[0] == null))
    {
      this.indexList = paramArrayOfIndex;
      this.accessorList = new CachedObject[this.indexList.length];
      return;
    }
    if (this.isCached)
    {
      resetAccessorKeysForCached(paramArrayOfIndex);
      return;
    }
    super.resetAccessorKeys(paramSession, paramArrayOfIndex);
  }
  
  private void resetAccessorKeysForCached(Index[] paramArrayOfIndex)
  {
    TableBase localTableBase = this.table.duplicate();
    localTableBase.persistenceId = this.table.persistenceId;
    localTableBase.setIndexes(paramArrayOfIndex);
    RowStoreAVLHybridExtended localRowStoreAVLHybridExtended = new RowStoreAVLHybridExtended(this.session, localTableBase, true);
    localRowStoreAVLHybridExtended.changeToDiskTable(this.session);
    RowIterator localRowIterator = rowIterator();
    while (localRowIterator.hasNext())
    {
      Row localRow1 = localRowIterator.getNextRow();
      Row localRow2 = (Row)localRowStoreAVLHybridExtended.getNewCachedObject(this.session, localRow1.getData(), false);
      localRowStoreAVLHybridExtended.indexRow(this.session, localRow2);
    }
    this.indexList = paramArrayOfIndex;
    this.accessorList = localRowStoreAVLHybridExtended.accessorList;
  }
}


/* Location:              E:\java\java学习\hutubill\lib\all.jar!\org\hsqldb\persist\RowStoreAVLHybridExtended.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */
package org.hsqldb.persist;

import java.io.IOException;
import org.hsqldb.HsqlException;
import org.hsqldb.RowDiskDataChange;
import org.hsqldb.Session;
import org.hsqldb.TableBase;
import org.hsqldb.rowio.RowInputInterface;

public class RowStoreDataChange
  extends RowStoreAVLHybrid
{
  Session session;
  
  public RowStoreDataChange(Session paramSession, PersistentStoreCollection paramPersistentStoreCollection, TableBase paramTableBase)
  {
    super(paramSession, paramTableBase, true);
    this.session = paramSession;
    super.changeToDiskTable(paramSession);
  }
  
  public CachedObject getNewCachedObject(Session paramSession, Object paramObject, boolean paramBoolean)
  {
    RowDiskDataChange localRowDiskDataChange = new RowDiskDataChange(this.table, (Object[])paramObject, this, null);
    add(paramSession, localRowDiskDataChange, paramBoolean);
    return localRowDiskDataChange;
  }
  
  public CachedObject get(RowInputInterface paramRowInputInterface)
  {
    try
    {
      return new RowDiskDataChange(this.session, this.table, paramRowInputInterface);
    }
    catch (HsqlException localHsqlException)
    {
      return null;
    }
    catch (IOException localIOException) {}
    return null;
  }
}


/* Location:              E:\java\java学习\hutubill\lib\all.jar!\org\hsqldb\persist\RowStoreDataChange.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */
package org.hsqldb.navigator;

import java.io.IOException;
import org.hsqldb.Database;
import org.hsqldb.Row;
import org.hsqldb.Session;
import org.hsqldb.TableBase;
import org.hsqldb.error.Error;
import org.hsqldb.lib.ArrayUtil;
import org.hsqldb.lib.OrderedLongKeyHashMap;
import org.hsqldb.result.ResultMetaData;
import org.hsqldb.rowio.RowInputInterface;
import org.hsqldb.rowio.RowOutputInterface;
import org.hsqldb.types.Type;

public class RowSetNavigatorDataChangeMemory
  implements RowSetNavigatorDataChange
{
  public static final RowSetNavigatorDataChangeMemory emptyRowSet = new RowSetNavigatorDataChangeMemory(null);
  int size;
  int currentPos = -1;
  OrderedLongKeyHashMap list;
  Session session;
  
  public RowSetNavigatorDataChangeMemory(Session paramSession)
  {
    this.session = paramSession;
    this.list = new OrderedLongKeyHashMap(64, true);
  }
  
  public void release()
  {
    beforeFirst();
    this.list.clear();
    this.size = 0;
  }
  
  public int getSize()
  {
    return this.size;
  }
  
  public int getRowPosition()
  {
    return this.currentPos;
  }
  
  public boolean next()
  {
    if (this.currentPos < this.size - 1)
    {
      this.currentPos += 1;
      return true;
    }
    this.currentPos = (this.size - 1);
    return false;
  }
  
  public boolean beforeFirst()
  {
    this.currentPos = -1;
    return true;
  }
  
  public Row getCurrentRow()
  {
    return (Row)this.list.getValueByIndex(this.currentPos);
  }
  
  public Object[] getCurrentChangedData()
  {
    return (Object[])this.list.getSecondValueByIndex(this.currentPos);
  }
  
  public int[] getCurrentChangedColumns()
  {
    return (int[])this.list.getThirdValueByIndex(this.currentPos);
  }
  
  public void write(RowOutputInterface paramRowOutputInterface, ResultMetaData paramResultMetaData)
    throws IOException
  {}
  
  public void read(RowInputInterface paramRowInputInterface, ResultMetaData paramResultMetaData)
    throws IOException
  {}
  
  public void endMainDataSet() {}
  
  public boolean addRow(Row paramRow)
  {
    int i = this.list.getLookup(paramRow.getId());
    if (i == -1)
    {
      this.list.put(paramRow.getId(), paramRow, null);
      this.size += 1;
      return true;
    }
    if (this.list.getSecondValueByIndex(i) != null)
    {
      if (this.session.database.sqlEnforceTDCD) {
        throw Error.error(3900);
      }
      this.list.setSecondValueByIndex(i, null);
      this.list.setThirdValueByIndex(i, null);
      return true;
    }
    return false;
  }
  
  public boolean addUpdate(Row paramRow, Object[] paramArrayOfObject, int[] paramArrayOfInt)
  {
    int i = this.list.getLookup(paramRow.getId());
    if (i == -1) {
      return false;
    }
    this.list.put(paramRow.getId(), paramRow, paramArrayOfObject);
    this.list.setThirdValueByIndex(i, paramArrayOfInt);
    return true;
  }
  
  public Object[] addRow(Session paramSession, Row paramRow, Object[] paramArrayOfObject, Type[] paramArrayOfType, int[] paramArrayOfInt)
  {
    long l = paramRow.getId();
    int i = this.list.getLookup(l);
    if (i == -1)
    {
      this.list.put(l, paramRow, paramArrayOfObject);
      this.list.setThirdValueByIndex(this.size, paramArrayOfInt);
      this.size += 1;
      return paramArrayOfObject;
    }
    Object[] arrayOfObject1 = ((Row)this.list.getFirstByLookup(i)).getData();
    Object[] arrayOfObject2 = (Object[])this.list.getSecondValueByIndex(i);
    if (arrayOfObject2 == null)
    {
      if (paramSession.database.sqlEnforceTDCD) {
        throw Error.error(3900);
      }
      return null;
    }
    for (int j = 0; j < paramArrayOfInt.length; j++)
    {
      int k = paramArrayOfInt[j];
      if (paramArrayOfType[k].compare(paramSession, paramArrayOfObject[k], arrayOfObject2[k]) != 0) {
        if (paramArrayOfType[k].compare(paramSession, arrayOfObject1[k], arrayOfObject2[k]) != 0)
        {
          if (paramSession.database.sqlEnforceTDCU) {
            throw Error.error(3900);
          }
        }
        else {
          arrayOfObject2[k] = paramArrayOfObject[k];
        }
      }
    }
    int[] arrayOfInt = (int[])this.list.getThirdValueByIndex(i);
    arrayOfInt = ArrayUtil.union(arrayOfInt, paramArrayOfInt);
    this.list.setThirdValueByIndex(i, arrayOfInt);
    return arrayOfObject2;
  }
  
  public boolean containsDeletedRow(Row paramRow)
  {
    int i = this.list.getLookup(paramRow.getId());
    if (i == -1) {
      return false;
    }
    Object[] arrayOfObject = (Object[])this.list.getSecondValueByIndex(i);
    return arrayOfObject == null;
  }
  
  public boolean containsUpdatedRow(Row paramRow1, Row paramRow2, int[] paramArrayOfInt)
  {
    int i = this.list.getLookup(paramRow2.getId());
    if (i > -1) {
      return true;
    }
    Object[] arrayOfObject1 = paramRow1.getData();
    label146:
    for (int j = 0; j < this.size; j++)
    {
      Row localRow = (Row)this.list.getValueByIndex(j);
      if (localRow.getTable() == paramRow1.getTable())
      {
        Type[] arrayOfType = paramRow1.getTable().getColumnTypes();
        Object[] arrayOfObject2 = (Object[])this.list.getSecondValueByIndex(j);
        for (int k = 0; k < paramArrayOfInt.length; k++)
        {
          int m = paramArrayOfInt[k];
          if (arrayOfType[m].compare(this.session, arrayOfObject1[m], arrayOfObject2[m]) != 0) {
            break label146;
          }
        }
        return true;
      }
    }
    return false;
  }
  
  public Row getNextRow()
  {
    if (next()) {
      return getCurrentRow();
    }
    return null;
  }
  
  public Object[] getNext()
  {
    if (next())
    {
      Row localRow = getCurrentRow();
      return localRow.getData();
    }
    return null;
  }
  
  public boolean hasNext()
  {
    return this.currentPos < this.size - 1;
  }
  
  public void removeCurrent() {}
  
  public long getRowId()
  {
    return getCurrentRow().getId();
  }
  
  public TableBase getCurrentTable()
  {
    return getCurrentRow().getTable();
  }
  
  public boolean isBeforeFirst()
  {
    return this.currentPos == -1;
  }
  
  public Object[] getCurrent()
  {
    return getCurrentRow().getData();
  }
  
  public Object getCurrent(int paramInt)
  {
    return getCurrentRow().getData()[paramInt];
  }
  
  public void setCurrent(Object[] paramArrayOfObject) {}
  
  public Object getRowidObject()
  {
    return Long.valueOf(getRowId());
  }
  
  public void reset()
  {
    beforeFirst();
  }
  
  public int getRangePosition()
  {
    return 1;
  }
  
  public RangeIterator getUpdateRowIterator()
  {
    return new UpdateRowIterator();
  }
  
  class UpdateRowIterator
    implements RangeIterator
  {
    UpdateRowIterator() {}
    
    public Row getNextRow()
    {
      return null;
    }
    
    public Object[] getNext()
    {
      if (RowSetNavigatorDataChangeMemory.this.next()) {
        return RowSetNavigatorDataChangeMemory.this.getCurrentChangedData();
      }
      return null;
    }
    
    public boolean hasNext()
    {
      return RowSetNavigatorDataChangeMemory.this.hasNext();
    }
    
    public void removeCurrent() {}
    
    public void release() {}
    
    public long getRowId()
    {
      return 0L;
    }
    
    public boolean beforeFirst()
    {
      return RowSetNavigatorDataChangeMemory.this.beforeFirst();
    }
    
    public Row getCurrentRow()
    {
      return null;
    }
    
    public boolean next()
    {
      return RowSetNavigatorDataChangeMemory.this.next();
    }
    
    public boolean isBeforeFirst()
    {
      return RowSetNavigatorDataChangeMemory.this.isBeforeFirst();
    }
    
    public Object[] getCurrent()
    {
      return RowSetNavigatorDataChangeMemory.this.getCurrentChangedData();
    }
    
    public Object getCurrent(int paramInt)
    {
      return RowSetNavigatorDataChangeMemory.this.getCurrentChangedData()[paramInt];
    }
    
    public TableBase getCurrentTable()
    {
      return null;
    }
    
    public void setCurrent(Object[] paramArrayOfObject) {}
    
    public Object getRowidObject()
    {
      return Long.valueOf(getRowId());
    }
    
    public void reset()
    {
      beforeFirst();
    }
    
    public int getRangePosition()
    {
      return 1;
    }
  }
}


/* Location:              E:\java\java学习\hutubill\lib\all.jar!\org\hsqldb\navigator\RowSetNavigatorDataChangeMemory.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */
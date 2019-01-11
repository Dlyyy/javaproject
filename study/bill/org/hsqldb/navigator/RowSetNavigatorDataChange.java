package org.hsqldb.navigator;

import java.io.IOException;
import org.hsqldb.Row;
import org.hsqldb.Session;
import org.hsqldb.result.ResultMetaData;
import org.hsqldb.rowio.RowInputInterface;
import org.hsqldb.rowio.RowOutputInterface;
import org.hsqldb.types.Type;

public abstract interface RowSetNavigatorDataChange
  extends RangeIterator
{
  public abstract int getSize();
  
  public abstract int getRowPosition();
  
  public abstract boolean beforeFirst();
  
  public abstract Object[] getCurrentChangedData();
  
  public abstract int[] getCurrentChangedColumns();
  
  public abstract void write(RowOutputInterface paramRowOutputInterface, ResultMetaData paramResultMetaData)
    throws IOException;
  
  public abstract void read(RowInputInterface paramRowInputInterface, ResultMetaData paramResultMetaData)
    throws IOException;
  
  public abstract void endMainDataSet();
  
  public abstract boolean addRow(Row paramRow);
  
  public abstract Object[] addRow(Session paramSession, Row paramRow, Object[] paramArrayOfObject, Type[] paramArrayOfType, int[] paramArrayOfInt);
  
  public abstract boolean addUpdate(Row paramRow, Object[] paramArrayOfObject, int[] paramArrayOfInt);
  
  public abstract boolean containsDeletedRow(Row paramRow);
  
  public abstract boolean containsUpdatedRow(Row paramRow1, Row paramRow2, int[] paramArrayOfInt);
  
  public abstract RangeIterator getUpdateRowIterator();
}


/* Location:              E:\java\java学习\hutubill\lib\all.jar!\org\hsqldb\navigator\RowSetNavigatorDataChange.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */
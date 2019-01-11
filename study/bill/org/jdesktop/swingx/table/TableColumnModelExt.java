package org.jdesktop.swingx.table;

import java.util.List;
import javax.swing.event.TableColumnModelListener;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;

public abstract interface TableColumnModelExt
  extends TableColumnModel
{
  public abstract int getColumnCount(boolean paramBoolean);
  
  public abstract List<TableColumn> getColumns(boolean paramBoolean);
  
  public abstract TableColumnExt getColumnExt(Object paramObject);
  
  public abstract TableColumnExt getColumnExt(int paramInt);
  
  public abstract void addColumnModelListener(TableColumnModelListener paramTableColumnModelListener);
}


/* Location:              E:\java\java学习\hutubill\lib\all.jar!\org\jdesktop\swingx\table\TableColumnModelExt.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */
package org.jdesktop.swingx.sort;

import java.util.Comparator;
import javax.swing.RowFilter;
import javax.swing.SortOrder;

public abstract interface SortController<M>
{
  public abstract void setSortable(boolean paramBoolean);
  
  public abstract boolean isSortable();
  
  public abstract void setSortable(int paramInt, boolean paramBoolean);
  
  public abstract boolean isSortable(int paramInt);
  
  public abstract void setComparator(int paramInt, Comparator<?> paramComparator);
  
  public abstract Comparator<?> getComparator(int paramInt);
  
  public abstract void setSortOrderCycle(SortOrder... paramVarArgs);
  
  public abstract SortOrder[] getSortOrderCycle();
  
  public abstract void setSortsOnUpdates(boolean paramBoolean);
  
  public abstract boolean getSortsOnUpdates();
  
  public abstract void setStringValueProvider(StringValueProvider paramStringValueProvider);
  
  public abstract StringValueProvider getStringValueProvider();
  
  public abstract void toggleSortOrder(int paramInt);
  
  public abstract void setSortOrder(int paramInt, SortOrder paramSortOrder);
  
  public abstract SortOrder getSortOrder(int paramInt);
  
  public abstract void resetSortOrders();
  
  public abstract void setRowFilter(RowFilter<? super M, ? super Integer> paramRowFilter);
  
  public abstract RowFilter<? super M, ? super Integer> getRowFilter();
}


/* Location:              E:\java\java学习\hutubill\lib\all.jar!\org\jdesktop\swingx\sort\SortController.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */
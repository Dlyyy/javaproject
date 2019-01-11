package org.jdesktop.swingx.treetable;

import javax.swing.tree.TreeModel;

public abstract interface TreeTableModel
  extends TreeModel
{
  public abstract Class<?> getColumnClass(int paramInt);
  
  public abstract int getColumnCount();
  
  public abstract String getColumnName(int paramInt);
  
  public abstract int getHierarchicalColumn();
  
  public abstract Object getValueAt(Object paramObject, int paramInt);
  
  public abstract boolean isCellEditable(Object paramObject, int paramInt);
  
  public abstract void setValueAt(Object paramObject1, Object paramObject2, int paramInt);
}


/* Location:              E:\java\java学习\hutubill\lib\all.jar!\org\jdesktop\swingx\treetable\TreeTableModel.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */
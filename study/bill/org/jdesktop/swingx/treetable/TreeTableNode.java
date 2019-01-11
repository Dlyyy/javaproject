package org.jdesktop.swingx.treetable;

import java.util.Enumeration;
import javax.swing.tree.TreeNode;

public abstract interface TreeTableNode
  extends TreeNode
{
  public abstract Enumeration<? extends TreeTableNode> children();
  
  public abstract Object getValueAt(int paramInt);
  
  public abstract TreeTableNode getChildAt(int paramInt);
  
  public abstract int getColumnCount();
  
  public abstract TreeTableNode getParent();
  
  public abstract boolean isEditable(int paramInt);
  
  public abstract void setValueAt(Object paramObject, int paramInt);
  
  public abstract Object getUserObject();
  
  public abstract void setUserObject(Object paramObject);
}


/* Location:              E:\java\java学习\hutubill\lib\all.jar!\org\jdesktop\swingx\treetable\TreeTableNode.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */
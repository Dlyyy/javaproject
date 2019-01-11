package org.jdesktop.swingx.treetable;

import java.util.Enumeration;

public abstract interface MutableTreeTableNode
  extends TreeTableNode
{
  public abstract Enumeration<? extends MutableTreeTableNode> children();
  
  public abstract void insert(MutableTreeTableNode paramMutableTreeTableNode, int paramInt);
  
  public abstract void remove(int paramInt);
  
  public abstract void remove(MutableTreeTableNode paramMutableTreeTableNode);
  
  public abstract void removeFromParent();
  
  public abstract void setParent(MutableTreeTableNode paramMutableTreeTableNode);
}


/* Location:              E:\java\java学习\hutubill\lib\all.jar!\org\jdesktop\swingx\treetable\MutableTreeTableNode.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */
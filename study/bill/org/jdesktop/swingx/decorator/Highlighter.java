package org.jdesktop.swingx.decorator;

import java.awt.Component;
import javax.swing.event.ChangeListener;

public abstract interface Highlighter
{
  public abstract Component highlight(Component paramComponent, ComponentAdapter paramComponentAdapter);
  
  public abstract void addChangeListener(ChangeListener paramChangeListener);
  
  public abstract void removeChangeListener(ChangeListener paramChangeListener);
  
  public abstract ChangeListener[] getChangeListeners();
}


/* Location:              E:\java\java学习\hutubill\lib\all.jar!\org\jdesktop\swingx\decorator\Highlighter.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */
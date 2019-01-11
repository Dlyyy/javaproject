package org.jdesktop.swingx.event;

import java.beans.PropertyChangeEvent;
import javax.swing.event.TableColumnModelListener;

public abstract interface TableColumnModelExtListener
  extends TableColumnModelListener
{
  public abstract void columnPropertyChange(PropertyChangeEvent paramPropertyChangeEvent);
}


/* Location:              E:\java\java学习\hutubill\lib\all.jar!\org\jdesktop\swingx\event\TableColumnModelExtListener.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */
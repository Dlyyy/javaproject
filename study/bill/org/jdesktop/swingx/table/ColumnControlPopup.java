package org.jdesktop.swingx.table;

import java.awt.ComponentOrientation;
import java.util.List;
import javax.swing.Action;
import javax.swing.JComponent;
import org.jdesktop.swingx.action.AbstractActionExt;
import org.jdesktop.swingx.plaf.UIDependent;

public abstract interface ColumnControlPopup
  extends UIDependent
{
  public abstract void toggleVisibility(JComponent paramJComponent);
  
  public abstract void applyComponentOrientation(ComponentOrientation paramComponentOrientation);
  
  public abstract void removeAll();
  
  public abstract void addVisibilityActionItems(List<? extends AbstractActionExt> paramList);
  
  public abstract void addAdditionalActionItems(List<? extends Action> paramList);
}


/* Location:              E:\java\java学习\hutubill\lib\all.jar!\org\jdesktop\swingx\table\ColumnControlPopup.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */
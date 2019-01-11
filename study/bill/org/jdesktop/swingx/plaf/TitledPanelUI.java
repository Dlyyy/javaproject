package org.jdesktop.swingx.plaf;

import java.awt.Container;
import javax.swing.JComponent;
import javax.swing.plaf.PanelUI;

public abstract class TitledPanelUI
  extends PanelUI
{
  public abstract void setRightDecoration(JComponent paramJComponent);
  
  public abstract JComponent getRightDecoration();
  
  public abstract void setLeftDecoration(JComponent paramJComponent);
  
  public abstract JComponent getLeftDecoration();
  
  public abstract Container getTitleBar();
}


/* Location:              E:\java\java学习\hutubill\lib\all.jar!\org\jdesktop\swingx\plaf\TitledPanelUI.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */
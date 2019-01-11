package org.jdesktop.swingx.plaf;

import java.awt.Component;
import java.awt.Dimension;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JInternalFrame;
import javax.swing.plaf.PanelUI;

public abstract class ErrorPaneUI
  extends PanelUI
{
  public abstract JFrame getErrorFrame(Component paramComponent);
  
  public abstract JDialog getErrorDialog(Component paramComponent);
  
  public abstract JInternalFrame getErrorInternalFrame(Component paramComponent);
  
  public abstract Dimension calculatePreferredSize();
}


/* Location:              E:\java\java学习\hutubill\lib\all.jar!\org\jdesktop\swingx\plaf\ErrorPaneUI.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */
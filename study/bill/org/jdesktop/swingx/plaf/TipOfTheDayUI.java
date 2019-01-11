package org.jdesktop.swingx.plaf;

import java.awt.Component;
import javax.swing.JDialog;
import javax.swing.plaf.PanelUI;
import org.jdesktop.swingx.JXTipOfTheDay.ShowOnStartupChoice;

public abstract class TipOfTheDayUI
  extends PanelUI
{
  public abstract JDialog createDialog(Component paramComponent, JXTipOfTheDay.ShowOnStartupChoice paramShowOnStartupChoice);
}


/* Location:              E:\java\java学习\hutubill\lib\all.jar!\org\jdesktop\swingx\plaf\TipOfTheDayUI.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */
package org.jdesktop.swingx.plaf;

import java.util.Date;
import javax.swing.plaf.ComponentUI;

public abstract class MonthViewUI
  extends ComponentUI
{
  public abstract String[] getDaysOfTheWeek();
  
  public abstract Date getDayAtLocation(int paramInt1, int paramInt2);
  
  public abstract Date getLastDisplayedDay();
}


/* Location:              E:\java\java学习\hutubill\lib\all.jar!\org\jdesktop\swingx\plaf\MonthViewUI.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */
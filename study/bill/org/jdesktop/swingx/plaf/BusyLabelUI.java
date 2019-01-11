package org.jdesktop.swingx.plaf;

import java.awt.Dimension;
import org.jdesktop.swingx.painter.BusyPainter;

public abstract interface BusyLabelUI
{
  public abstract BusyPainter getBusyPainter(Dimension paramDimension);
  
  public abstract int getDelay();
}


/* Location:              E:\java\java学习\hutubill\lib\all.jar!\org\jdesktop\swingx\plaf\BusyLabelUI.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */
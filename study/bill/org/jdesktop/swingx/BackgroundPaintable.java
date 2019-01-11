package org.jdesktop.swingx;

import org.jdesktop.swingx.painter.Painter;

abstract interface BackgroundPaintable
{
  public abstract Painter getBackgroundPainter();
  
  public abstract void setBackgroundPainter(Painter paramPainter);
  
  public abstract boolean isPaintBorderInsets();
  
  public abstract void setPaintBorderInsets(boolean paramBoolean);
}


/* Location:              E:\java\java学习\hutubill\lib\all.jar!\org\jdesktop\swingx\BackgroundPaintable.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */
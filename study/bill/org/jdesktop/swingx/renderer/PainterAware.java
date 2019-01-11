package org.jdesktop.swingx.renderer;

import org.jdesktop.swingx.painter.Painter;

public abstract interface PainterAware
{
  public abstract void setPainter(Painter<?> paramPainter);
  
  public abstract Painter<?> getPainter();
}


/* Location:              E:\java\java学习\hutubill\lib\all.jar!\org\jdesktop\swingx\renderer\PainterAware.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */
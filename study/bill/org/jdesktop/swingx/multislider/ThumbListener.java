package org.jdesktop.swingx.multislider;

import java.awt.event.MouseEvent;

public abstract interface ThumbListener
{
  public abstract void thumbMoved(int paramInt, float paramFloat);
  
  public abstract void thumbSelected(int paramInt);
  
  public abstract void mousePressed(MouseEvent paramMouseEvent);
}


/* Location:              E:\java\java学习\hutubill\lib\all.jar!\org\jdesktop\swingx\multislider\ThumbListener.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */
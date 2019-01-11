package org.jdesktop.swingx.multislider;

import java.util.List;

public abstract interface MultiThumbModel<E>
  extends Iterable<Thumb<E>>
{
  public abstract float getMinimumValue();
  
  public abstract void setMinimumValue(float paramFloat);
  
  public abstract float getMaximumValue();
  
  public abstract void setMaximumValue(float paramFloat);
  
  public abstract int addThumb(float paramFloat, E paramE);
  
  public abstract void insertThumb(float paramFloat, E paramE, int paramInt);
  
  public abstract void removeThumb(int paramInt);
  
  public abstract int getThumbCount();
  
  public abstract Thumb<E> getThumbAt(int paramInt);
  
  public abstract int getThumbIndex(Thumb<E> paramThumb);
  
  public abstract List<Thumb<E>> getSortedThumbs();
  
  public abstract void thumbPositionChanged(Thumb<E> paramThumb);
  
  public abstract void thumbValueChanged(Thumb<E> paramThumb);
  
  public abstract void addThumbDataListener(ThumbDataListener paramThumbDataListener);
  
  public abstract void removeThumbDataListener(ThumbDataListener paramThumbDataListener);
}


/* Location:              E:\java\java学习\hutubill\lib\all.jar!\org\jdesktop\swingx\multislider\MultiThumbModel.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */
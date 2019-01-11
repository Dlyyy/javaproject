package org.jdesktop.swingx.tips;

public abstract interface TipOfTheDayModel
{
  public abstract int getTipCount();
  
  public abstract Tip getTipAt(int paramInt);
  
  public static abstract interface Tip
  {
    public abstract String getTipName();
    
    public abstract Object getTip();
  }
}


/* Location:              E:\java\java学习\hutubill\lib\all.jar!\org\jdesktop\swingx\tips\TipOfTheDayModel.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */
package org.jdesktop.swingx.action;

public abstract interface Targetable
{
  public abstract boolean doCommand(Object paramObject1, Object paramObject2);
  
  public abstract boolean hasCommand(Object paramObject);
  
  public abstract Object[] getCommands();
}


/* Location:              E:\java\java学习\hutubill\lib\all.jar!\org\jdesktop\swingx\action\Targetable.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */
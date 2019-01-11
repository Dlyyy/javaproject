package org.hsqldb.persist;

public abstract interface EventLogInterface
{
  public abstract void logSevereEvent(String paramString, Throwable paramThrowable);
  
  public abstract void logWarningEvent(String paramString, Throwable paramThrowable);
  
  public abstract void logInfoEvent(String paramString);
  
  public abstract void logDetailEvent(String paramString);
}


/* Location:              E:\java\java学习\hutubill\lib\all.jar!\org\hsqldb\persist\EventLogInterface.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */
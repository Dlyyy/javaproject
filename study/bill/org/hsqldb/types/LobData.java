package org.hsqldb.types;

import org.hsqldb.SessionInterface;

public abstract interface LobData
{
  public abstract long length(SessionInterface paramSessionInterface);
  
  public abstract long getId();
  
  public abstract void setId(long paramLong);
  
  public abstract void setSession(SessionInterface paramSessionInterface);
  
  public abstract boolean isBinary();
}


/* Location:              E:\java\java学习\hutubill\lib\all.jar!\org\hsqldb\types\LobData.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */
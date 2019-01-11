package org.jdesktop.swingx.auth;

import org.jdesktop.beans.AbstractBean;

public abstract class UserNameStore
  extends AbstractBean
{
  public abstract String[] getUserNames();
  
  public abstract void setUserNames(String[] paramArrayOfString);
  
  public abstract void loadUserNames();
  
  public abstract void saveUserNames();
  
  public abstract boolean containsUserName(String paramString);
  
  public abstract void addUserName(String paramString);
  
  public abstract void removeUserName(String paramString);
}


/* Location:              E:\java\java学习\hutubill\lib\all.jar!\org\jdesktop\swingx\auth\UserNameStore.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */
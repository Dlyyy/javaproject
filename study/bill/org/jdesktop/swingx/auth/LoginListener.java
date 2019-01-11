package org.jdesktop.swingx.auth;

import java.util.EventListener;

public abstract interface LoginListener
  extends EventListener
{
  public abstract void loginFailed(LoginEvent paramLoginEvent);
  
  public abstract void loginStarted(LoginEvent paramLoginEvent);
  
  public abstract void loginCanceled(LoginEvent paramLoginEvent);
  
  public abstract void loginSucceeded(LoginEvent paramLoginEvent);
}


/* Location:              E:\java\java学习\hutubill\lib\all.jar!\org\jdesktop\swingx\auth\LoginListener.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */
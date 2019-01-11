package org.jdesktop.swingx.auth;

public abstract class LoginAdapter
  implements LoginListener
{
  public void loginSucceeded(LoginEvent source) {}
  
  public void loginStarted(LoginEvent source) {}
  
  public void loginFailed(LoginEvent source) {}
  
  public void loginCanceled(LoginEvent source) {}
}


/* Location:              E:\java\java学习\hutubill\lib\all.jar!\org\jdesktop\swingx\auth\LoginAdapter.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */
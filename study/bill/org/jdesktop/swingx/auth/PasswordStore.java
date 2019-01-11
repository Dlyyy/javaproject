package org.jdesktop.swingx.auth;

public abstract class PasswordStore
{
  public abstract boolean set(String paramString1, String paramString2, char[] paramArrayOfChar);
  
  public abstract char[] get(String paramString1, String paramString2);
  
  public abstract void removeUserPassword(String paramString);
}


/* Location:              E:\java\java学习\hutubill\lib\all.jar!\org\jdesktop\swingx\auth\PasswordStore.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */
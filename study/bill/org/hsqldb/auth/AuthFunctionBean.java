package org.hsqldb.auth;

public abstract interface AuthFunctionBean
{
  public abstract String[] authenticate(String paramString1, String paramString2)
    throws Exception;
}


/* Location:              E:\java\java学习\hutubill\lib\all.jar!\org\hsqldb\auth\AuthFunctionBean.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */
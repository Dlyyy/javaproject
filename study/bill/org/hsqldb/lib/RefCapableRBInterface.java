package org.hsqldb.lib;

public abstract interface RefCapableRBInterface
{
  public abstract String getString();
  
  public abstract String getString(String... paramVarArgs);
  
  public abstract String getExpandedString();
  
  public abstract String getExpandedString(String... paramVarArgs);
  
  public abstract String getString(int paramInt);
  
  public abstract String getString(int paramInt1, int paramInt2);
  
  public abstract String getString(int paramInt1, int paramInt2, int paramInt3);
  
  public abstract String getString(int paramInt, String paramString);
  
  public abstract String getString(String paramString, int paramInt);
  
  public abstract String getString(int paramInt1, int paramInt2, String paramString);
  
  public abstract String getString(int paramInt1, String paramString, int paramInt2);
  
  public abstract String getString(String paramString, int paramInt1, int paramInt2);
  
  public abstract String getString(int paramInt, String paramString1, String paramString2);
  
  public abstract String getString(String paramString1, String paramString2, int paramInt);
  
  public abstract String getString(String paramString1, int paramInt, String paramString2);
}


/* Location:              E:\java\java学习\hutubill\lib\all.jar!\org\hsqldb\lib\RefCapableRBInterface.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */
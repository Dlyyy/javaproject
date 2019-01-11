package org.jdesktop.swingx.search;

import java.util.regex.Pattern;

public abstract interface Searchable
{
  public abstract int search(String paramString);
  
  public abstract int search(String paramString, int paramInt);
  
  public abstract int search(String paramString, int paramInt, boolean paramBoolean);
  
  public abstract int search(Pattern paramPattern);
  
  public abstract int search(Pattern paramPattern, int paramInt);
  
  public abstract int search(Pattern paramPattern, int paramInt, boolean paramBoolean);
}


/* Location:              E:\java\java学习\hutubill\lib\all.jar!\org\jdesktop\swingx\search\Searchable.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */
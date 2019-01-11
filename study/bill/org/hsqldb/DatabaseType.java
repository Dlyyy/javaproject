package org.hsqldb;

import org.hsqldb.error.Error;

public enum DatabaseType
{
  DB_MEM("mem:"),  DB_FILE("file:"),  DB_RES("res:");
  
  private String value;
  
  private DatabaseType(String paramString)
  {
    this.value = paramString;
  }
  
  public String value()
  {
    return this.value;
  }
  
  public boolean isFileBased()
  {
    switch (this)
    {
    case DB_FILE: 
    case DB_RES: 
      return true;
    }
    return false;
  }
  
  public static DatabaseType get(String paramString)
  {
    if (DB_MEM.value.equals(paramString)) {
      return DB_MEM;
    }
    if (DB_FILE.value.equals(paramString)) {
      return DB_FILE;
    }
    if (DB_RES.value.equals(paramString)) {
      return DB_RES;
    }
    throw Error.runtimeError(201, "DatabaseType");
  }
}


/* Location:              E:\java\java学习\hutubill\lib\all.jar!\org\hsqldb\DatabaseType.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */
package org.hsqldb;

import org.hsqldb.error.Error;
import org.hsqldb.lib.OrderedHashSet;
import org.hsqldb.result.Result;

public class StatementSimple
  extends Statement
{
  HsqlNameManager.HsqlName label;
  
  StatementSimple(int paramInt, HsqlNameManager.HsqlName paramHsqlName)
  {
    super(paramInt, 2007);
    this.references = new OrderedHashSet();
    this.isTransactionStatement = false;
    this.label = paramHsqlName;
  }
  
  public String getSQL()
  {
    StringBuffer localStringBuffer = new StringBuffer();
    switch (this.type)
    {
    case 103: 
      localStringBuffer.append("ITERATE").append(' ').append(this.label);
      break;
    case 104: 
      localStringBuffer.append("LEAVE").append(' ').append(this.label);
    }
    return localStringBuffer.toString();
  }
  
  protected String describe(Session paramSession, int paramInt)
  {
    StringBuffer localStringBuffer = new StringBuffer();
    localStringBuffer.append('\n');
    for (int i = 0; i < paramInt; i++) {
      localStringBuffer.append(' ');
    }
    localStringBuffer.append("STATEMENT");
    return localStringBuffer.toString();
  }
  
  public Result execute(Session paramSession)
  {
    Result localResult;
    try
    {
      localResult = getResult(paramSession);
    }
    catch (Throwable localThrowable)
    {
      localResult = Result.newErrorResult(localThrowable);
    }
    if (localResult.isError()) {
      localResult.getException().setStatementType(this.group, this.type);
    }
    return localResult;
  }
  
  Result getResult(Session paramSession)
  {
    switch (this.type)
    {
    case 103: 
    case 104: 
      return Result.newPSMResult(this.type, this.label.name, null);
    }
    throw Error.runtimeError(201, "StatementSimple");
  }
  
  public void resolve(Session paramSession)
  {
    int i = 0;
    StatementCompound localStatementCompound;
    switch (this.type)
    {
    case 103: 
      localStatementCompound = this.parent;
    }
    while (localStatementCompound != null)
    {
      if (localStatementCompound.isLoop)
      {
        if (this.label == null)
        {
          i = 1;
          break;
        }
        if ((localStatementCompound.label != null) && (this.label.name.equals(localStatementCompound.label.name)))
        {
          i = 1;
          break;
        }
      }
      localStatementCompound = localStatementCompound.parent;
      continue;
      i = 1;
      break;
      throw Error.runtimeError(201, "StatementSimple");
    }
    if (i == 0) {
      throw Error.error(5602);
    }
  }
  
  public String describe(Session paramSession)
  {
    return "";
  }
  
  public boolean isCatalogLock()
  {
    return false;
  }
  
  public boolean isCatalogChange()
  {
    return false;
  }
}


/* Location:              E:\java\java学习\hutubill\lib\all.jar!\org\hsqldb\StatementSimple.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */
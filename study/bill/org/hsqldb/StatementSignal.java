package org.hsqldb;

import org.hsqldb.error.Error;
import org.hsqldb.lib.OrderedHashSet;
import org.hsqldb.result.Result;

public class StatementSignal
  extends Statement
{
  String sqlState;
  Expression messageExpression;
  
  StatementSignal(int paramInt, String paramString, Expression paramExpression)
  {
    super(paramInt, 2007);
    this.references = new OrderedHashSet();
    this.isTransactionStatement = false;
    this.sqlState = paramString;
    this.messageExpression = paramExpression;
  }
  
  public String getSQL()
  {
    StringBuffer localStringBuffer = new StringBuffer();
    switch (this.type)
    {
    case 108: 
      localStringBuffer.append("SIGNAL").append(' ');
      localStringBuffer.append("SQLSTATE");
      localStringBuffer.append(' ').append('\'').append(this.sqlState).append('\'');
      break;
    case 106: 
      localStringBuffer.append("RESIGNAL").append(' ');
      localStringBuffer.append("SQLSTATE");
      localStringBuffer.append(' ').append('\'').append(this.sqlState).append('\'');
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
    case 106: 
    case 108: 
      HsqlException localHsqlException = Error.error(getMessage(paramSession), this.sqlState);
      return Result.newErrorResult(localHsqlException);
    }
    throw Error.runtimeError(201, "StatementSignal");
  }
  
  String getMessage(Session paramSession)
  {
    if (this.messageExpression == null) {
      return null;
    }
    return (String)this.messageExpression.getValue(paramSession);
  }
  
  public void resolve(Session paramSession)
  {
    int i = 0;
    switch (this.type)
    {
    case 106: 
    case 108: 
      i = 1;
      break;
    default: 
      throw Error.runtimeError(201, "StatementSignal");
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


/* Location:              E:\java\java学习\hutubill\lib\all.jar!\org\hsqldb\StatementSignal.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */
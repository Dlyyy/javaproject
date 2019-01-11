package org.hsqldb;

import org.hsqldb.error.Error;
import org.hsqldb.lib.OrderedHashSet;
import org.hsqldb.result.Result;
import org.hsqldb.result.ResultMetaData;

public class StatementQuery
  extends StatementDMQL
{
  public static final StatementQuery[] emptyArray = new StatementQuery[0];
  
  StatementQuery(Session paramSession, QueryExpression paramQueryExpression, ParserDQL.CompileContext paramCompileContext)
  {
    super(44, 2003, paramSession.getCurrentSchemaHsqlName());
    this.statementReturnType = 2;
    this.queryExpression = paramQueryExpression;
    setDatabaseObjects(paramSession, paramCompileContext);
    checkAccessRights(paramSession);
  }
  
  Result getResult(Session paramSession)
  {
    Result localResult = this.queryExpression.getResult(paramSession, paramSession.getMaxRows());
    localResult.setStatement(this);
    return localResult;
  }
  
  public ResultMetaData getResultMetaData()
  {
    switch (this.type)
    {
    case 44: 
      return this.queryExpression.getMetaData();
    case 70: 
      return this.queryExpression.getMetaData();
    }
    throw Error.runtimeError(201, "StatementQuery.getResultMetaData()");
  }
  
  void collectTableNamesForRead(OrderedHashSet paramOrderedHashSet)
  {
    this.queryExpression.getBaseTableNames(paramOrderedHashSet);
    for (int i = 0; i < this.subqueries.length; i++) {
      if (this.subqueries[i].queryExpression != null) {
        this.subqueries[i].queryExpression.getBaseTableNames(paramOrderedHashSet);
      }
    }
    for (i = 0; i < this.routines.length; i++) {
      paramOrderedHashSet.addAll(this.routines[i].getTableNamesForRead());
    }
  }
  
  void collectTableNamesForWrite(OrderedHashSet paramOrderedHashSet)
  {
    if (this.queryExpression.isUpdatable) {
      this.queryExpression.getBaseTableNames(paramOrderedHashSet);
    }
  }
  
  public int getResultProperties()
  {
    return this.queryExpression.isUpdatable ? 8 : 0;
  }
  
  public void setCursorName(HsqlNameManager.HsqlName paramHsqlName)
  {
    this.cursorName = paramHsqlName;
  }
  
  public HsqlNameManager.HsqlName getCursorName()
  {
    return this.cursorName;
  }
}


/* Location:              E:\java\java学习\hutubill\lib\all.jar!\org\hsqldb\StatementQuery.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */
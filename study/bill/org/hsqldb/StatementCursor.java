package org.hsqldb;

import org.hsqldb.lib.OrderedHashSet;
import org.hsqldb.navigator.RowSetNavigator;
import org.hsqldb.result.Result;

public class StatementCursor
  extends StatementQuery
{
  public static final StatementCursor[] emptyArray = new StatementCursor[0];
  
  StatementCursor(Session paramSession, QueryExpression paramQueryExpression, ParserDQL.CompileContext paramCompileContext)
  {
    super(paramSession, paramQueryExpression, paramCompileContext);
  }
  
  Result getResult(Session paramSession)
  {
    Object[] arrayOfObject = paramSession.sessionContext.routineArguments;
    Result localResult1 = (Result)arrayOfObject[(arrayOfObject.length - 1)];
    Result localResult2 = localResult1;
    while (localResult1 != null)
    {
      if (getCursorName().name.equals(localResult1.getMainString()))
      {
        localResult1.navigator.release();
        if (localResult2 == localResult1) {
          localResult2 = localResult1.getChainedResult();
        }
      }
      if (localResult1.getChainedResult() == null) {
        break;
      }
      localResult1 = localResult1.getChainedResult();
    }
    arrayOfObject[(arrayOfObject.length - 1)] = localResult2;
    Result localResult3 = this.queryExpression.getResult(paramSession, 0);
    localResult3.setStatement(this);
    if (localResult3.isError()) {
      return localResult3;
    }
    localResult3.setMainString(getCursorName().name);
    if (localResult2 == null) {
      arrayOfObject[(arrayOfObject.length - 1)] = localResult3;
    } else {
      ((Result)arrayOfObject[(arrayOfObject.length - 1)]).addChainedResult(localResult3);
    }
    return Result.updateZeroResult;
  }
  
  void collectTableNamesForWrite(OrderedHashSet paramOrderedHashSet) {}
}


/* Location:              E:\java\java学习\hutubill\lib\all.jar!\org\hsqldb\StatementCursor.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */
package org.hsqldb;

import org.hsqldb.error.Error;
import org.hsqldb.lib.HsqlArrayList;
import org.hsqldb.navigator.RowSetNavigator;
import org.hsqldb.persist.Logger;
import org.hsqldb.result.Result;
import org.hsqldb.rights.Grantee;
import org.hsqldb.rights.GranteeManager;
import org.hsqldb.rights.User;
import org.hsqldb.rights.UserManager;
import org.hsqldb.types.CharacterType;
import org.hsqldb.types.IntervalSecondData;
import org.hsqldb.types.Type;

public class StatementSession
  extends Statement
{
  public static final StatementSession commitNoChainStatement = new StatementSession(14, new Object[] { Boolean.FALSE });
  public static final StatementSession rollbackNoChainStatement = new StatementSession(66, new Object[] { Boolean.FALSE });
  public static final StatementSession commitAndChainStatement = new StatementSession(14, new Object[] { Boolean.TRUE });
  public static final StatementSession rollbackAndChainStatement = new StatementSession(66, new Object[] { Boolean.TRUE });
  Expression[] expressions;
  Object[] parameters;
  
  StatementSession(int paramInt, Expression[] paramArrayOfExpression)
  {
    super(paramInt);
    this.expressions = paramArrayOfExpression;
    this.isTransactionStatement = false;
    this.isLogged = false;
    switch (paramInt)
    {
    case 72: 
    case 76: 
    case 77: 
    case 78: 
    case 79: 
    case 80: 
    case 81: 
    case 83: 
      this.group = 2008;
      break;
    case 73: 
    case 74: 
    case 75: 
    case 82: 
    default: 
      throw Error.runtimeError(201, "StatementSession");
    }
  }
  
  StatementSession(int paramInt, Object[] paramArrayOfObject)
  {
    super(paramInt);
    this.parameters = paramArrayOfObject;
    this.isTransactionStatement = false;
    this.isLogged = false;
    switch (paramInt)
    {
    case 80: 
      this.group = 2008;
      this.isLogged = true;
      break;
    case 1127: 
      this.group = 2011;
      this.isLogged = true;
      break;
    case 1: 
      this.group = 2003;
      break;
    case 2: 
    case 16: 
    case 17: 
      this.group = 2010;
      break;
    case 41: 
      this.group = 2004;
      break;
    case 40: 
    case 42: 
    case 43: 
      this.group = 2003;
      break;
    case 49: 
    case 50: 
    case 51: 
    case 52: 
    case 57: 
      this.group = 2003;
      break;
    case 58: 
    case 59: 
    case 60: 
      this.group = 2010;
      break;
    case 22: 
      this.group = 2006;
      break;
    case 73: 
    case 74: 
    case 75: 
    case 82: 
    case 83: 
    case 84: 
    case 1114: 
    case 1115: 
    case 1116: 
    case 1117: 
      this.group = 2011;
      break;
    case 1048: 
      this.isLogged = true;
      this.group = 2011;
      break;
    case 14: 
    case 61: 
    case 66: 
    case 67: 
    case 85: 
    case 86: 
    case 1118: 
      this.group = 2005;
      break;
    case 33: 
    case 1119: 
      this.group = 2008;
      break;
    default: 
      throw Error.runtimeError(201, "StatementSession");
    }
  }
  
  StatementSession(int paramInt, HsqlNameManager.HsqlName[] paramArrayOfHsqlName1, HsqlNameManager.HsqlName[] paramArrayOfHsqlName2)
  {
    super(paramInt);
    this.isTransactionStatement = true;
    this.isLogged = false;
    this.readTableNames = paramArrayOfHsqlName1;
    this.writeTableNames = paramArrayOfHsqlName2;
    switch (paramInt)
    {
    case 1111: 
    case 1112: 
    case 1113: 
      this.group = 2016;
      break;
    default: 
      throw Error.runtimeError(201, "StatementSession");
    }
  }
  
  public Result execute(Session paramSession)
  {
    Result localResult;
    try
    {
      localResult = getResult(paramSession);
    }
    catch (Throwable localThrowable1)
    {
      localResult = Result.newErrorResult(localThrowable1, null);
    }
    if (localResult.isError())
    {
      localResult.getException().setStatementType(this.group, this.type);
      return localResult;
    }
    try
    {
      if (this.isLogged) {
        paramSession.database.logger.writeOtherStatement(paramSession, this.sql);
      }
    }
    catch (Throwable localThrowable2)
    {
      return Result.newErrorResult(localThrowable2, this.sql);
    }
    return localResult;
  }
  
  Result getResult(Session paramSession)
  {
    int i = 0;
    if (this.isExplain) {
      return Result.newSingleColumnStringResult("OPERATION", describe(paramSession));
    }
    Object localObject1;
    Object localObject4;
    Object localObject6;
    Object localObject2;
    Object localObject5;
    int m;
    Object localObject3;
    switch (this.type)
    {
    case 1: 
    case 2: 
      return Result.updateZeroResult;
    case 14: 
      try
      {
        boolean bool1 = ((Boolean)this.parameters[0]).booleanValue();
        paramSession.commit(bool1);
        return Result.updateZeroResult;
      }
      catch (HsqlException localHsqlException1)
      {
        return Result.newErrorResult(localHsqlException1, this.sql);
      }
    case 16: 
    case 17: 
      return Result.updateZeroResult;
    case 22: 
      paramSession.close();
      return Result.updateZeroResult;
    case 40: 
    case 41: 
    case 42: 
    case 43: 
    case 49: 
    case 50: 
    case 51: 
    case 52: 
    case 57: 
    case 58: 
    case 59: 
    case 60: 
      return Result.updateZeroResult;
    case 1111: 
    case 1112: 
    case 1113: 
      return Result.updateZeroResult;
    case 61: 
      String str = (String)this.parameters[0];
      try
      {
        paramSession.releaseSavepoint(str);
        return Result.updateZeroResult;
      }
      catch (HsqlException localHsqlException6)
      {
        return Result.newErrorResult(localHsqlException6, this.sql);
      }
    case 66: 
      boolean bool2 = ((Boolean)this.parameters[0]).booleanValue();
      paramSession.rollback(bool2);
      return Result.updateZeroResult;
    case 1118: 
      localObject1 = (String)this.parameters[0];
      try
      {
        paramSession.rollbackToSavepoint((String)localObject1);
        return Result.updateZeroResult;
      }
      catch (HsqlException localHsqlException7)
      {
        return Result.newErrorResult(localHsqlException7, this.sql);
      }
    case 67: 
      localObject1 = (String)this.parameters[0];
      paramSession.savepoint((String)localObject1);
      return Result.updateZeroResult;
    case 72: 
      try
      {
        localObject1 = (String)this.expressions[0].getValue(paramSession);
        localObject1 = (String)Type.SQL_VARCHAR.trim(paramSession, localObject1, ' ', true, true);
        if (paramSession.database.getCatalogName().name.equals(localObject1)) {
          return Result.updateZeroResult;
        }
        return Result.newErrorResult(Error.error(4840), this.sql);
      }
      catch (HsqlException localHsqlException8)
      {
        return Result.newErrorResult(localHsqlException8, this.sql);
      }
    case 73: 
    case 74: 
    case 75: 
      return Result.updateZeroResult;
    case 76: 
      localObject1 = null;
      if ((this.expressions[0].getType() == 1) && (this.expressions[0].getConstantValueNoCheck(paramSession) == null))
      {
        paramSession.setZoneSeconds(paramSession.sessionTimeZoneSeconds);
        return Result.updateZeroResult;
      }
      try
      {
        localObject1 = this.expressions[0].getValue(paramSession);
      }
      catch (HsqlException localHsqlException9) {}
      if ((localObject1 instanceof Result))
      {
        Result localResult1 = (Result)localObject1;
        if (localResult1.isData())
        {
          Object[] arrayOfObject = localResult1.getNavigator().getNext();
          int i1 = !localResult1.getNavigator().next() ? 1 : 0;
          if ((i1 != 0) && (arrayOfObject != null) && (arrayOfObject[0] != null))
          {
            localObject1 = arrayOfObject[0];
            localResult1.getNavigator().release();
          }
          else
          {
            localResult1.getNavigator().release();
            return Result.newErrorResult(Error.error(3409), this.sql);
          }
        }
        else
        {
          return Result.newErrorResult(Error.error(3409), this.sql);
        }
      }
      else if (localObject1 == null)
      {
        return Result.newErrorResult(Error.error(3409), this.sql);
      }
      long l = ((IntervalSecondData)localObject1).getSeconds();
      if ((-50400L <= l) && (l <= 50400L))
      {
        paramSession.setZoneSeconds((int)l);
        return Result.updateZeroResult;
      }
      return Result.newErrorResult(Error.error(3409), this.sql);
    case 77: 
      return Result.updateZeroResult;
    case 78: 
      return Result.updateZeroResult;
    case 79: 
      localObject4 = null;
      try
      {
        localObject1 = (String)this.expressions[0].getValue(paramSession);
        if (localObject1 != null)
        {
          localObject1 = (String)Type.SQL_VARCHAR.trim(paramSession, localObject1, ' ', true, true);
          localObject4 = paramSession.database.granteeManager.getRole((String)localObject1);
        }
      }
      catch (HsqlException localHsqlException12)
      {
        return Result.newErrorResult(Error.error(2200), this.sql);
      }
      if (paramSession.isInMidTransaction()) {
        return Result.newErrorResult(Error.error(3701), this.sql);
      }
      if (localObject4 == null) {
        paramSession.setRole(null);
      }
      if (paramSession.getGrantee().hasRole((Grantee)localObject4))
      {
        paramSession.setRole((Grantee)localObject4);
        return Result.updateZeroResult;
      }
      return Result.newErrorResult(Error.error(2200), this.sql);
    case 80: 
      try
      {
        if (this.expressions == null) {
          localObject1 = ((HsqlNameManager.HsqlName)this.parameters[0]).name;
        } else {
          localObject1 = (String)this.expressions[0].getValue(paramSession);
        }
        localObject1 = (String)Type.SQL_VARCHAR.trim(paramSession, localObject1, ' ', true, true);
        localObject4 = paramSession.database.schemaManager.getSchemaHsqlName((String)localObject1);
        paramSession.setCurrentSchemaHsqlName((HsqlNameManager.HsqlName)localObject4);
        return Result.updateZeroResult;
      }
      catch (HsqlException localHsqlException13)
      {
        return Result.newErrorResult(localHsqlException13, this.sql);
      }
    case 81: 
      if (paramSession.isInMidTransaction()) {
        return Result.newErrorResult(Error.error(3701), this.sql);
      }
      try
      {
        localObject4 = null;
        localObject1 = (String)this.expressions[0].getValue(paramSession);
        localObject1 = (String)Type.SQL_VARCHAR.trim(paramSession, localObject1, ' ', true, true);
        if (this.expressions[1] != null) {
          localObject4 = (String)this.expressions[1].getValue(paramSession);
        }
        if (localObject4 == null) {
          localObject6 = paramSession.database.userManager.get((String)localObject1);
        } else {
          localObject6 = paramSession.database.getUserManager().getUser((String)localObject1, (String)localObject4);
        }
        if (localObject6 == null) {
          throw Error.error(4001);
        }
        this.sql = ((User)localObject6).getConnectUserSQL();
        if (localObject6 == paramSession.getGrantee()) {
          return Result.updateZeroResult;
        }
        if ((localObject4 == null) && (!paramSession.isProcessingLog()) && (((User)localObject6).isAdmin()) && (!paramSession.getGrantee().isAdmin())) {
          throw Error.error(4000);
        }
        if (paramSession.getGrantee().canChangeAuthorisation())
        {
          paramSession.setUser((User)localObject6);
          paramSession.setRole(null);
          paramSession.resetSchema();
          return Result.updateZeroResult;
        }
        throw Error.error(4000);
      }
      catch (HsqlException localHsqlException2)
      {
        return Result.newErrorResult(localHsqlException2, this.sql);
      }
    case 82: 
      try
      {
        if (this.parameters[0] != null)
        {
          boolean bool3 = ((Boolean)this.parameters[0]).booleanValue();
          paramSession.setReadOnlyDefault(bool3);
        }
        if (this.parameters[1] != null)
        {
          int j = ((Integer)this.parameters[1]).intValue();
          paramSession.setIsolationDefault(j);
        }
        return Result.updateZeroResult;
      }
      catch (HsqlException localHsqlException3)
      {
        return Result.newErrorResult(localHsqlException3, this.sql);
      }
    case 83: 
      return Result.updateZeroResult;
    case 84: 
      return Result.updateZeroResult;
    case 86: 
      i = 1;
    case 85: 
      try
      {
        if (this.parameters[0] != null)
        {
          boolean bool4 = ((Boolean)this.parameters[0]).booleanValue();
          paramSession.setReadOnly(bool4);
        }
        if (this.parameters[1] != null)
        {
          int k = ((Integer)this.parameters[1]).intValue();
          paramSession.setIsolation(k);
        }
        if (i != 0) {
          paramSession.startTransaction();
        }
        return Result.updateZeroResult;
      }
      catch (HsqlException localHsqlException4)
      {
        return Result.newErrorResult(localHsqlException4, this.sql);
      }
    case 1114: 
      boolean bool5 = ((Boolean)this.parameters[0]).booleanValue();
      try
      {
        paramSession.setAutoCommit(bool5);
        return Result.updateZeroResult;
      }
      catch (HsqlException localHsqlException10)
      {
        return Result.newErrorResult(localHsqlException10, this.sql);
      }
    case 1127: 
      localObject2 = (ColumnSchema[])this.parameters[0];
      try
      {
        for (int n = 0; n < localObject2.length; n++) {
          paramSession.sessionContext.addSessionVariable(localObject2[n]);
        }
        return Result.updateZeroResult;
      }
      catch (HsqlException localHsqlException11)
      {
        return Result.newErrorResult(localHsqlException11, this.sql);
      }
    case 1115: 
      localObject2 = (String)this.parameters[0];
      localObject5 = (Boolean)this.parameters[1];
      paramSession.setFeature((String)localObject2, ((Boolean)localObject5).booleanValue());
      return Result.updateZeroResult;
    case 1116: 
      m = ((Integer)this.parameters[0]).intValue();
      paramSession.setSQLMaxRows(m);
      return Result.updateZeroResult;
    case 1117: 
      m = ((Integer)this.parameters[0]).intValue();
      paramSession.setResultMemoryRowCount(m);
      return Result.updateZeroResult;
    case 1048: 
      try
      {
        boolean bool6 = ((Boolean)this.parameters[0]).booleanValue();
        paramSession.setIgnoreCase(bool6);
        return Result.updateZeroResult;
      }
      catch (HsqlException localHsqlException5)
      {
        return Result.newErrorResult(localHsqlException5, this.sql);
      }
    case 1119: 
      localObject3 = (Table)this.parameters[0];
      localObject5 = (HsqlArrayList)this.parameters[1];
      localObject6 = (StatementDMQL)this.parameters[3];
      Boolean localBoolean = (Boolean)this.parameters[4];
      try
      {
        if (((HsqlArrayList)localObject5).size() != 0) {
          localObject3 = ParserDDL.addTableConstraintDefinitions(paramSession, (Table)localObject3, (HsqlArrayList)localObject5, null, false);
        }
        ((Table)localObject3).compile(paramSession, null);
        try
        {
          paramSession.sessionContext.addSessionTable((Table)localObject3);
        }
        catch (HsqlException localHsqlException14)
        {
          if ((localBoolean != null) && (localBoolean.booleanValue())) {
            return Result.updateZeroResult;
          }
          return Result.newErrorResult(localHsqlException14, this.sql);
        }
        if (((Table)localObject3).hasLobColumn) {
          throw Error.error(5534);
        }
        if (localObject6 != null)
        {
          Result localResult2 = ((StatementDMQL)localObject6).execute(paramSession);
          ((Table)localObject3).insertIntoTable(paramSession, localResult2);
        }
        return Result.updateZeroResult;
      }
      catch (HsqlException localHsqlException15)
      {
        return Result.newErrorResult(localHsqlException15, this.sql);
      }
    case 33: 
      localObject3 = (HsqlNameManager.HsqlName)this.parameters[0];
      localObject5 = (Boolean)this.parameters[1];
      localObject6 = paramSession.sessionContext.findSessionTable(((HsqlNameManager.HsqlName)localObject3).name);
      if (localObject6 == null)
      {
        if (((Boolean)localObject5).booleanValue()) {
          return Result.updateZeroResult;
        }
        throw Error.error(5501, ((HsqlNameManager.HsqlName)localObject3).name);
      }
      paramSession.sessionContext.dropSessionTable(((HsqlNameManager.HsqlName)localObject3).name);
      return Result.updateZeroResult;
    }
    throw Error.runtimeError(201, "StatementSession");
  }
  
  public boolean isAutoCommitStatement()
  {
    return false;
  }
  
  public String describe(Session paramSession)
  {
    return this.sql;
  }
  
  public boolean isCatalogLock()
  {
    return false;
  }
  
  public boolean isCatalogChange()
  {
    return false;
  }
  
  static
  {
    commitNoChainStatement.sql = "COMMIT";
    commitAndChainStatement.sql = "COMMIT CHAIN";
    rollbackNoChainStatement.sql = "ROLLBACK";
    rollbackAndChainStatement.sql = "ROLLBACK CHAIN";
    commitNoChainStatement.compileTimestamp = Long.MAX_VALUE;
    commitAndChainStatement.compileTimestamp = Long.MAX_VALUE;
    rollbackNoChainStatement.compileTimestamp = Long.MAX_VALUE;
    rollbackAndChainStatement.compileTimestamp = Long.MAX_VALUE;
  }
}


/* Location:              E:\java\java学习\hutubill\lib\all.jar!\org\hsqldb\StatementSession.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */
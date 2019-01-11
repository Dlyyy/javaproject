package org.hsqldb;

import org.hsqldb.error.Error;
import org.hsqldb.lib.HsqlArrayList;
import org.hsqldb.persist.Logger;
import org.hsqldb.result.Result;

public class StatementSchemaDefinition
  extends StatementSchema
{
  StatementSchema[] statements;
  
  StatementSchemaDefinition(StatementSchema[] paramArrayOfStatementSchema)
  {
    super(68, 2001);
    this.statements = paramArrayOfStatementSchema;
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
    HsqlNameManager.HsqlName localHsqlName1 = this.statements[0].getSchemaName();
    if (this.isExplain) {
      return Result.newSingleColumnStringResult("OPERATION", describe(paramSession));
    }
    Result localResult = this.statements[0].execute(paramSession);
    HsqlArrayList localHsqlArrayList = new HsqlArrayList();
    StatementSchema localStatementSchema2 = new StatementSchema(null, 1161);
    if ((this.statements.length == 1) || (localResult.isError())) {
      return localResult;
    }
    HsqlNameManager.HsqlName localHsqlName2 = paramSession.getCurrentSchemaHsqlName();
    Table localTable;
    for (int i = 1; i < this.statements.length; i++)
    {
      try
      {
        paramSession.setSchema(localHsqlName1.name);
      }
      catch (HsqlException localHsqlException3) {}
      this.statements[i].setSchemaHsqlName(localHsqlName1);
      paramSession.parser.reset(paramSession, this.statements[i].getSQL());
      try
      {
        paramSession.parser.read();
        StatementSchema localStatementSchema1;
        switch (this.statements[i].getType())
        {
        case 53: 
        case 54: 
          localResult = this.statements[i].execute(paramSession);
          break;
        case 87: 
          localStatementSchema1 = paramSession.parser.compileCreate();
          localStatementSchema1.isSchemaDefinition = true;
          localStatementSchema1.setSchemaHsqlName(localHsqlName1);
          if (paramSession.parser.token.tokenType != 914) {
            throw paramSession.parser.unexpectedToken();
          }
          localStatementSchema1.isLogged = false;
          localResult = localStatementSchema1.execute(paramSession);
          HsqlNameManager.HsqlName localHsqlName3 = ((Table)localStatementSchema1.arguments[0]).getName();
          localTable = (Table)paramSession.database.schemaManager.getSchemaObject(localHsqlName3);
          localHsqlArrayList.addAll((HsqlArrayList)localStatementSchema1.arguments[1]);
          ((HsqlArrayList)localStatementSchema1.arguments[1]).clear();
          localStatementSchema2.sql = localTable.getSQL();
          localStatementSchema2.execute(paramSession);
          break;
        case 11: 
        case 13: 
        case 65: 
        case 71: 
        case 94: 
          localResult = this.statements[i].execute(paramSession);
          break;
        case 23: 
        case 69: 
        case 90: 
        case 96: 
        case 1125: 
          localStatementSchema1 = paramSession.parser.compileCreate();
          localStatementSchema1.isSchemaDefinition = true;
          localStatementSchema1.setSchemaHsqlName(localHsqlName1);
          if (paramSession.parser.token.tokenType != 914) {
            throw paramSession.parser.unexpectedToken();
          }
          localResult = localStatementSchema1.execute(paramSession);
          break;
        case 9: 
        case 88: 
        case 89: 
        case 93: 
        case 95: 
          throw paramSession.parser.unsupportedFeature();
        default: 
          throw Error.runtimeError(201, "");
        }
        if (localResult.isError()) {
          break;
        }
      }
      catch (HsqlException localHsqlException4)
      {
        localResult = Result.newErrorResult(localHsqlException4, this.statements[i].getSQL());
        break;
      }
    }
    if (!localResult.isError()) {
      try
      {
        for (i = 0; i < localHsqlArrayList.size(); i++)
        {
          Constraint localConstraint = (Constraint)localHsqlArrayList.get(i);
          localTable = paramSession.database.schemaManager.getUserTable(localConstraint.core.refTableName);
          ParserDDL.addForeignKey(paramSession, localTable, localConstraint, null);
          localStatementSchema2.sql = localConstraint.getSQL();
          localStatementSchema2.execute(paramSession);
        }
      }
      catch (HsqlException localHsqlException1)
      {
        localResult = Result.newErrorResult(localHsqlException1, this.sql);
      }
    }
    if (localResult.isError()) {
      try
      {
        paramSession.database.schemaManager.dropSchema(paramSession, localHsqlName1.name, true);
        paramSession.database.logger.writeOtherStatement(paramSession, getDropSchemaStatement(localHsqlName1));
      }
      catch (HsqlException localHsqlException2) {}
    }
    paramSession.setCurrentSchemaHsqlName(localHsqlName2);
    return localResult;
  }
  
  String getDropSchemaStatement(HsqlNameManager.HsqlName paramHsqlName)
  {
    return "DROP SCHEMA " + paramHsqlName.statementName + " " + "CASCADE";
  }
  
  public boolean isAutoCommitStatement()
  {
    return true;
  }
}


/* Location:              E:\java\java学习\hutubill\lib\all.jar!\org\hsqldb\StatementSchemaDefinition.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */
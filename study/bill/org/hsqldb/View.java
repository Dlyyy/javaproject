package org.hsqldb;

import org.hsqldb.error.Error;
import org.hsqldb.lib.OrderedHashSet;

public class View
  extends TableDerived
{
  private String statement;
  private HsqlNameManager.HsqlName[] columnNames;
  private OrderedHashSet schemaObjectNames;
  private int checkOption;
  private Table baseTable;
  boolean isTriggerInsertable;
  boolean isTriggerUpdatable;
  boolean isTriggerDeletable;
  
  View(Database paramDatabase, HsqlNameManager.HsqlName paramHsqlName, HsqlNameManager.HsqlName[] paramArrayOfHsqlName, int paramInt)
  {
    super(paramDatabase, paramHsqlName, 8);
    this.columnNames = paramArrayOfHsqlName;
    this.checkOption = paramInt;
  }
  
  public int getType()
  {
    return 4;
  }
  
  public OrderedHashSet getReferences()
  {
    return this.schemaObjectNames;
  }
  
  public OrderedHashSet getComponents()
  {
    return null;
  }
  
  public void compile(Session paramSession, SchemaObject paramSchemaObject)
  {
    ParserDQL localParserDQL = new ParserDQL(paramSession, new Scanner(paramSession, this.statement), null);
    localParserDQL.isViewDefinition = true;
    localParserDQL.read();
    TableDerived localTableDerived = localParserDQL.XreadViewSubqueryTable(this, true);
    this.queryExpression = localTableDerived.queryExpression;
    if (getColumnCount() == 0)
    {
      if (this.columnNames == null) {
        this.columnNames = localTableDerived.queryExpression.getResultColumnNames();
      }
      if (this.columnNames.length != localTableDerived.queryExpression.getColumnCount()) {
        throw Error.error(5593, getName().statementName);
      }
      TableUtil.setColumnsInSchemaTable(this, this.columnNames, this.queryExpression.getColumnTypes());
    }
    this.schemaObjectNames = localParserDQL.compileContext.getSchemaObjectNames();
    this.canRecompile = true;
    this.baseTable = this.queryExpression.getBaseTable();
    if (this.baseTable == null) {
      return;
    }
    switch (this.checkOption)
    {
    case 0: 
    case 1: 
    case 2: 
      break;
    default: 
      throw Error.runtimeError(201, "View");
    }
  }
  
  public String getSQL()
  {
    StringBuffer localStringBuffer = new StringBuffer(128);
    localStringBuffer.append("CREATE").append(' ').append("VIEW");
    localStringBuffer.append(' ');
    localStringBuffer.append(getName().getSchemaQualifiedStatementName()).append(' ');
    localStringBuffer.append('(');
    int i = getColumnCount();
    for (int j = 0; j < i; j++)
    {
      localStringBuffer.append(getColumn(j).getName().statementName);
      if (j < i - 1) {
        localStringBuffer.append(',');
      }
    }
    localStringBuffer.append(')').append(' ').append("AS").append(' ');
    localStringBuffer.append(getStatement());
    return localStringBuffer.toString();
  }
  
  public int[] getUpdatableColumns()
  {
    return this.queryExpression.getBaseTableColumnMap();
  }
  
  public boolean isTriggerInsertable()
  {
    return this.isTriggerInsertable;
  }
  
  public boolean isTriggerUpdatable()
  {
    return this.isTriggerUpdatable;
  }
  
  public boolean isTriggerDeletable()
  {
    return this.isTriggerDeletable;
  }
  
  public boolean isInsertable()
  {
    return this.isTriggerInsertable ? false : super.isInsertable();
  }
  
  public boolean isUpdatable()
  {
    return this.isTriggerUpdatable ? false : super.isUpdatable();
  }
  
  void addTrigger(TriggerDef paramTriggerDef, HsqlNameManager.HsqlName paramHsqlName)
  {
    switch (paramTriggerDef.operationType)
    {
    case 55: 
      if (this.isTriggerInsertable) {
        throw Error.error(5538);
      }
      this.isTriggerInsertable = true;
      break;
    case 19: 
      if (this.isTriggerDeletable) {
        throw Error.error(5538);
      }
      this.isTriggerDeletable = true;
      break;
    case 92: 
      if (this.isTriggerUpdatable) {
        throw Error.error(5538);
      }
      this.isTriggerUpdatable = true;
      break;
    default: 
      throw Error.runtimeError(201, "View");
    }
    super.addTrigger(paramTriggerDef, paramHsqlName);
  }
  
  void removeTrigger(TriggerDef paramTriggerDef)
  {
    switch (paramTriggerDef.operationType)
    {
    case 55: 
      this.isTriggerInsertable = false;
      break;
    case 19: 
      this.isTriggerDeletable = false;
      break;
    case 92: 
      this.isTriggerUpdatable = false;
      break;
    default: 
      throw Error.runtimeError(201, "View");
    }
    super.removeTrigger(paramTriggerDef);
  }
  
  public void setDataReadOnly(boolean paramBoolean)
  {
    throw Error.error(4000);
  }
  
  public int getCheckOption()
  {
    return this.checkOption;
  }
  
  public String getStatement()
  {
    return this.statement;
  }
  
  public void setStatement(String paramString)
  {
    this.statement = paramString;
  }
  
  public TableDerived newDerivedTable(Session paramSession)
  {
    ParserDQL localParserDQL = new ParserDQL(paramSession, new Scanner(), paramSession.parser.compileContext);
    localParserDQL.reset(paramSession, this.statement);
    localParserDQL.read();
    TableDerived localTableDerived = localParserDQL.XreadViewSubqueryTable(this, false);
    return localTableDerived;
  }
}


/* Location:              E:\java\java学习\hutubill\lib\all.jar!\org\hsqldb\View.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */
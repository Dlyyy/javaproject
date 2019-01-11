package org.hsqldb;

import org.hsqldb.types.Type;

public class TableUtil
{
  static Table newSingleColumnTable(Database paramDatabase, HsqlNameManager.HsqlName paramHsqlName1, int paramInt, HsqlNameManager.HsqlName paramHsqlName2, Type paramType)
  {
    TableDerived localTableDerived = new TableDerived(paramDatabase, paramHsqlName1, paramInt);
    ColumnSchema localColumnSchema = new ColumnSchema(paramHsqlName2, paramType, false, true, null);
    localTableDerived.addColumn(localColumnSchema);
    localTableDerived.createPrimaryKeyConstraint(localTableDerived.getName(), new int[] { 0 }, true);
    return localTableDerived;
  }
  
  public static void addAutoColumns(Table paramTable, Type[] paramArrayOfType)
  {
    for (int i = 0; i < paramArrayOfType.length; i++)
    {
      ColumnSchema localColumnSchema = new ColumnSchema(HsqlNameManager.getAutoColumnName(i), paramArrayOfType[i], true, false, null);
      paramTable.addColumnNoCheck(localColumnSchema);
    }
  }
  
  public static void setColumnsInSchemaTable(Table paramTable, HsqlNameManager.HsqlName[] paramArrayOfHsqlName, Type[] paramArrayOfType)
  {
    for (int i = 0; i < paramArrayOfHsqlName.length; i++)
    {
      HsqlNameManager.HsqlName localHsqlName = paramArrayOfHsqlName[i];
      localHsqlName = paramTable.database.nameManager.newColumnSchemaHsqlName(paramTable.getName(), localHsqlName);
      ColumnSchema localColumnSchema = new ColumnSchema(localHsqlName, paramArrayOfType[i], true, false, null);
      paramTable.addColumn(localColumnSchema);
    }
    paramTable.setColumnStructures();
  }
}


/* Location:              E:\java\java学习\hutubill\lib\all.jar!\org\hsqldb\TableUtil.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */
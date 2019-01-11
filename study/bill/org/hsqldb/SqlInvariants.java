package org.hsqldb;

import org.hsqldb.error.Error;

public final class SqlInvariants
{
  public static final String SYSTEM_AUTHORIZATION_NAME = "_SYSTEM";
  public static final String DBA_ADMIN_ROLE_NAME = "DBA";
  public static final String SCHEMA_CREATE_ROLE_NAME = "CREATE_SCHEMA";
  public static final String CHANGE_AUTH_ROLE_NAME = "CHANGE_AUTHORIZATION";
  public static final String SYSTEM_SUBQUERY = "SYSTEM_SUBQUERY";
  public static final String PUBLIC_ROLE_NAME = "PUBLIC";
  public static final String DEFAULT_CATALOG_NAME = "PUBLIC";
  public static final String SYSTEM_SCHEMA = "SYSTEM_SCHEMA";
  public static final String LOBS_SCHEMA = "SYSTEM_LOBS";
  public static final String DEFINITION_SCHEMA = "DEFINITION_SCHEMA";
  public static final String INFORMATION_SCHEMA = "INFORMATION_SCHEMA";
  public static final String SQLJ_SCHEMA = "SQLJ";
  public static final String PUBLIC_SCHEMA = "PUBLIC";
  public static final String CLASSPATH_NAME = "CLASSPATH";
  public static final String MODULE = "MODULE";
  public static final String DUAL = "DUAL";
  public static final String DUMMY = "DUMMY";
  public static final String IDX = "IDX";
  public static final HsqlNameManager.HsqlName INFORMATION_SCHEMA_HSQLNAME = HsqlNameManager.newSystemObjectName("INFORMATION_SCHEMA", 2);
  public static final HsqlNameManager.HsqlName SYSTEM_SCHEMA_HSQLNAME = HsqlNameManager.newSystemObjectName("SYSTEM_SCHEMA", 2);
  public static final HsqlNameManager.HsqlName LOBS_SCHEMA_HSQLNAME = HsqlNameManager.newSystemObjectName("SYSTEM_LOBS", 2);
  public static final HsqlNameManager.HsqlName SQLJ_SCHEMA_HSQLNAME = HsqlNameManager.newSystemObjectName("SQLJ", 2);
  public static final HsqlNameManager.HsqlName SYSTEM_SUBQUERY_HSQLNAME = HsqlNameManager.newSystemObjectName("SYSTEM_SUBQUERY", 3);
  public static final HsqlNameManager.HsqlName MODULE_HSQLNAME = HsqlNameManager.newSystemObjectName("MODULE", 2);
  public static final HsqlNameManager.HsqlName DUAL_TABLE_HSQLNAME = HsqlNameManager.newSystemObjectName("DUAL", 3);
  public static final HsqlNameManager.HsqlName DUAL_COLUMN_HSQLNAME;
  public static final HsqlNameManager.HsqlName SYSTEM_INDEX_HSQLNAME;
  
  public static void checkSchemaNameNotSystem(String paramString)
  {
    if (isSystemSchemaName(paramString)) {
      throw Error.error(5503, paramString);
    }
  }
  
  public static boolean isSystemSchemaName(String paramString)
  {
    return ("DEFINITION_SCHEMA".equals(paramString)) || ("INFORMATION_SCHEMA".equals(paramString)) || ("SYSTEM_SCHEMA".equals(paramString)) || ("SQLJ".equals(paramString));
  }
  
  public static boolean isLobsSchemaName(String paramString)
  {
    return "SYSTEM_LOBS".equals(paramString);
  }
  
  public static boolean isSchemaNameSystem(HsqlNameManager.HsqlName paramHsqlName)
  {
    if (paramHsqlName.schema != null) {
      paramHsqlName = paramHsqlName.schema;
    }
    return (INFORMATION_SCHEMA_HSQLNAME.equals(paramHsqlName)) || (SYSTEM_SCHEMA_HSQLNAME.equals(paramHsqlName)) || (SQLJ_SCHEMA_HSQLNAME.equals(paramHsqlName));
  }
  
  static
  {
    DUAL_TABLE_HSQLNAME.schema = SYSTEM_SCHEMA_HSQLNAME;
    DUAL_COLUMN_HSQLNAME = HsqlNameManager.newSystemObjectName("DUMMY", 9);
    DUAL_COLUMN_HSQLNAME.parent = DUAL_TABLE_HSQLNAME;
    SYSTEM_INDEX_HSQLNAME = HsqlNameManager.newSystemObjectName("IDX", 20);
    SYSTEM_SUBQUERY_HSQLNAME.setSchemaIfNull(SYSTEM_SCHEMA_HSQLNAME);
  }
}


/* Location:              E:\java\java学习\hutubill\lib\all.jar!\org\hsqldb\SqlInvariants.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */
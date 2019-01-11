package org.hsqldb.dbinfo;

import java.lang.reflect.Constructor;
import org.hsqldb.Database;
import org.hsqldb.Session;
import org.hsqldb.Table;
import org.hsqldb.lib.IntValueHashMap;
import org.hsqldb.persist.PersistentStore;

public class DatabaseInformation
{
  protected static final int SYSTEM_BESTROWIDENTIFIER = 0;
  protected static final int SYSTEM_COLUMNS = 1;
  protected static final int SYSTEM_CROSSREFERENCE = 2;
  protected static final int SYSTEM_INDEXINFO = 3;
  protected static final int SYSTEM_PRIMARYKEYS = 4;
  protected static final int SYSTEM_PROCEDURECOLUMNS = 5;
  protected static final int SYSTEM_PROCEDURES = 6;
  protected static final int SYSTEM_SCHEMAS = 7;
  protected static final int SYSTEM_TABLES = 8;
  protected static final int SYSTEM_TABLETYPES = 9;
  protected static final int SYSTEM_TYPEINFO = 10;
  protected static final int SYSTEM_UDTS = 11;
  protected static final int SYSTEM_USERS = 12;
  protected static final int SYSTEM_VERSIONCOLUMNS = 13;
  protected static final int SYSTEM_SEQUENCES = 14;
  protected static final int SYSTEM_CACHEINFO = 15;
  protected static final int SYSTEM_COLUMN_SEQUENCE_USAGE = 16;
  protected static final int SYSTEM_COMMENTS = 17;
  protected static final int SYSTEM_CONNECTION_PROPERTIES = 18;
  protected static final int SYSTEM_INDEXSTATS = 19;
  protected static final int SYSTEM_PROPERTIES = 20;
  protected static final int SYSTEM_SESSIONINFO = 21;
  protected static final int SYSTEM_SESSIONS = 22;
  protected static final int SYSTEM_TABLESTATS = 23;
  protected static final int SYSTEM_TEXTTABLES = 24;
  protected static final int SYSTEM_SYNONYMS = 25;
  protected static final int ADMINISTRABLE_ROLE_AUTHORIZATIONS = 26;
  protected static final int APPLICABLE_ROLES = 27;
  protected static final int ASSERTIONS = 28;
  protected static final int AUTHORIZATIONS = 29;
  protected static final int CHARACTER_SETS = 30;
  protected static final int CHECK_CONSTRAINT_ROUTINE_USAGE = 31;
  protected static final int CHECK_CONSTRAINTS = 32;
  protected static final int COLLATIONS = 33;
  protected static final int COLUMN_COLUMN_USAGE = 34;
  protected static final int COLUMN_DOMAIN_USAGE = 35;
  protected static final int COLUMN_PRIVILEGES = 36;
  protected static final int COLUMN_UDT_USAGE = 37;
  protected static final int COLUMNS = 38;
  protected static final int CONSTRAINT_COLUMN_USAGE = 39;
  protected static final int CONSTRAINT_TABLE_USAGE = 40;
  protected static final int DATA_TYPE_PRIVILEGES = 41;
  protected static final int DOMAIN_CONSTRAINTS = 42;
  protected static final int DOMAINS = 43;
  protected static final int ELEMENT_TYPES = 44;
  protected static final int ENABLED_ROLES = 45;
  protected static final int INFORMATION_SCHEMA_CATALOG_NAME = 46;
  protected static final int JAR_JAR_USAGE = 47;
  protected static final int JARS = 48;
  protected static final int KEY_COLUMN_USAGE = 49;
  protected static final int METHOD_SPECIFICATIONS = 50;
  protected static final int MODULE_COLUMN_USAGE = 51;
  protected static final int MODULE_PRIVILEGES = 52;
  protected static final int MODULE_TABLE_USAGE = 53;
  protected static final int MODULES = 54;
  protected static final int PARAMETERS = 55;
  protected static final int REFERENTIAL_CONSTRAINTS = 56;
  protected static final int ROLE_AUTHORIZATION_DESCRIPTORS = 57;
  protected static final int ROLE_COLUMN_GRANTS = 58;
  protected static final int ROLE_MODULE_GRANTS = 59;
  protected static final int ROLE_ROUTINE_GRANTS = 60;
  protected static final int ROLE_TABLE_GRANTS = 61;
  protected static final int ROLE_UDT_GRANTS = 62;
  protected static final int ROLE_USAGE_GRANTS = 63;
  protected static final int ROUTINE_COLUMN_USAGE = 64;
  protected static final int ROUTINE_JAR_USAGE = 65;
  protected static final int ROUTINE_PRIVILEGES = 66;
  protected static final int ROUTINE_ROUTINE_USAGE = 67;
  protected static final int ROUTINE_SEQUENCE_USAGE = 68;
  protected static final int ROUTINE_TABLE_USAGE = 69;
  protected static final int ROUTINES = 70;
  protected static final int SCHEMATA = 71;
  protected static final int SEQUENCES = 72;
  protected static final int SQL_FEATURES = 73;
  protected static final int SQL_IMPLEMENTATION_INFO = 74;
  protected static final int SQL_PACKAGES = 75;
  protected static final int SQL_PARTS = 76;
  protected static final int SQL_SIZING = 77;
  protected static final int SQL_SIZING_PROFILES = 78;
  protected static final int TABLE_CONSTRAINTS = 79;
  protected static final int TABLE_PRIVILEGES = 80;
  protected static final int TABLES = 81;
  protected static final int TRANSLATIONS = 82;
  protected static final int TRIGGER_COLUMN_USAGE = 83;
  protected static final int TRIGGER_ROUTINE_USAGE = 84;
  protected static final int TRIGGER_SEQUENCE_USAGE = 85;
  protected static final int TRIGGER_TABLE_USAGE = 86;
  protected static final int TRIGGERED_UPDATE_COLUMNS = 87;
  protected static final int TRIGGERS = 88;
  protected static final int TYPE_JAR_USAGE = 89;
  protected static final int UDT_PRIVILEGES = 90;
  protected static final int USAGE_PRIVILEGES = 91;
  protected static final int USER_DEFINED_TYPES = 92;
  protected static final int VIEW_COLUMN_USAGE = 93;
  protected static final int VIEW_ROUTINE_USAGE = 94;
  protected static final int VIEW_TABLE_USAGE = 95;
  protected static final int VIEWS = 96;
  protected static final String[] sysTableNames = { "SYSTEM_BESTROWIDENTIFIER", "SYSTEM_COLUMNS", "SYSTEM_CROSSREFERENCE", "SYSTEM_INDEXINFO", "SYSTEM_PRIMARYKEYS", "SYSTEM_PROCEDURECOLUMNS", "SYSTEM_PROCEDURES", "SYSTEM_SCHEMAS", "SYSTEM_TABLES", "SYSTEM_TABLETYPES", "SYSTEM_TYPEINFO", "SYSTEM_UDTS", "SYSTEM_USERS", "SYSTEM_VERSIONCOLUMNS", "SYSTEM_SEQUENCES", "SYSTEM_CACHEINFO", "SYSTEM_COLUMN_SEQUENCE_USAGE", "SYSTEM_COMMENTS", "SYSTEM_CONNECTION_PROPERTIES", "SYSTEM_INDEXSTATS", "SYSTEM_PROPERTIES", "SYSTEM_SESSIONINFO", "SYSTEM_SESSIONS", "SYSTEM_TABLESTATS", "SYSTEM_TEXTTABLES", "SYSTEM_SYNONYMS", "ADMINISTRABLE_ROLE_AUTHORIZATIONS", "APPLICABLE_ROLES", "ASSERTIONS", "AUTHORIZATIONS", "CHARACTER_SETS", "CHECK_CONSTRAINT_ROUTINE_USAGE", "CHECK_CONSTRAINTS", "COLLATIONS", "COLUMN_COLUMN_USAGE", "COLUMN_DOMAIN_USAGE", "COLUMN_PRIVILEGES", "COLUMN_UDT_USAGE", "COLUMNS", "CONSTRAINT_COLUMN_USAGE", "CONSTRAINT_TABLE_USAGE", "DATA_TYPE_PRIVILEGES", "DOMAIN_CONSTRAINTS", "DOMAINS", "ELEMENT_TYPES", "ENABLED_ROLES", "INFORMATION_SCHEMA_CATALOG_NAME", "JAR_JAR_USAGE", "JARS", "KEY_COLUMN_USAGE", "METHOD_SPECIFICATIONS", "MODULE_COLUMN_USAGE", "MODULE_PRIVILEGES", "MODULE_TABLE_USAGE", "MODULES", "PARAMETERS", "REFERENTIAL_CONSTRAINTS", "ROLE_AUTHORIZATION_DESCRIPTORS", "ROLE_COLUMN_GRANTS", "ROLE_MODULE_GRANTS", "ROLE_ROUTINE_GRANTS", "ROLE_TABLE_GRANTS", "ROLE_UDT_GRANTS", "ROLE_USAGE_GRANTS", "ROUTINE_COLUMN_USAGE", "ROUTINE_JAR_USAGE", "ROUTINE_PRIVILEGES", "ROUTINE_ROUTINE_USAGE", "ROUTINE_SEQUENCE_USAGE", "ROUTINE_TABLE_USAGE", "ROUTINES", "SCHEMATA", "SEQUENCES", "SQL_FEATURES", "SQL_IMPLEMENTATION_INFO", "SQL_PACKAGES", "SQL_PARTS", "SQL_SIZING", "SQL_SIZING_PROFILES", "TABLE_CONSTRAINTS", "TABLE_PRIVILEGES", "TABLES", "TRANSLATIONS", "TRIGGER_COLUMN_USAGE", "TRIGGER_ROUTINE_USAGE", "TRIGGER_SEQUENCE_USAGE", "TRIGGER_TABLE_USAGE", "TRIGGERED_UPDATE_COLUMNS", "TRIGGERS", "TYPE_JAR_USAGE", "UDT_PRIVILEGES", "USAGE_PRIVILEGES", "USER_DEFINED_TYPES", "VIEW_COLUMN_USAGE", "VIEW_ROUTINE_USAGE", "VIEW_TABLE_USAGE", "VIEWS" };
  protected static final IntValueHashMap sysTableNamesMap;
  protected final Database database;
  protected boolean withContent = false;
  
  static int getSysTableID(String paramString)
  {
    return sysTableNamesMap.get(paramString, -1);
  }
  
  public static DatabaseInformation newDatabaseInformation(Database paramDatabase)
  {
    Class localClass = null;
    try
    {
      localClass = Class.forName("org.hsqldb.dbinfo.DatabaseInformationFull");
    }
    catch (Exception localException1)
    {
      try
      {
        localClass = Class.forName("org.hsqldb.dbinfo.DatabaseInformationMain");
      }
      catch (Exception localException3)
      {
        localClass = DatabaseInformation.class;
      }
    }
    try
    {
      Class[] arrayOfClass = { Database.class };
      Object[] arrayOfObject = { paramDatabase };
      Constructor localConstructor = localClass.getDeclaredConstructor(arrayOfClass);
      return (DatabaseInformation)localConstructor.newInstance(arrayOfObject);
    }
    catch (Exception localException2)
    {
      localException2.printStackTrace();
    }
    return new DatabaseInformation(paramDatabase);
  }
  
  DatabaseInformation(Database paramDatabase)
  {
    this.database = paramDatabase;
  }
  
  final boolean isSystemTable(String paramString)
  {
    return sysTableNamesMap.containsKey(paramString);
  }
  
  public Table getSystemTable(Session paramSession, String paramString)
  {
    return null;
  }
  
  public void setStore(Session paramSession, Table paramTable, PersistentStore paramPersistentStore) {}
  
  public final void setWithContent(boolean paramBoolean)
  {
    this.withContent = paramBoolean;
  }
  
  static
  {
    synchronized (DatabaseInformation.class)
    {
      sysTableNamesMap = new IntValueHashMap(97);
      for (int i = 0; i < sysTableNames.length; i++) {
        sysTableNamesMap.put(sysTableNames[i], i);
      }
    }
  }
}


/* Location:              E:\java\java学习\hutubill\lib\all.jar!\org\hsqldb\dbinfo\DatabaseInformation.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */
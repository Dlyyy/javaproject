package org.hsqldb;

import java.util.Vector;
import org.hsqldb.error.Error;
import org.hsqldb.lib.Collection;
import org.hsqldb.lib.FileUtil;
import org.hsqldb.lib.HashMap;
import org.hsqldb.lib.HashSet;
import org.hsqldb.lib.HsqlTimer;
import org.hsqldb.lib.IntKeyHashMap;
import org.hsqldb.lib.Iterator;
import org.hsqldb.lib.Notified;
import org.hsqldb.lib.Set;
import org.hsqldb.map.ValuePool;
import org.hsqldb.persist.HsqlProperties;

public class DatabaseManager
{
  private static int dbIDCounter;
  static final HashMap memDatabaseMap = new HashMap();
  static final HashMap fileDatabaseMap = new HashMap();
  static final HashMap resDatabaseMap = new HashMap();
  static final IntKeyHashMap databaseIDMap = new IntKeyHashMap();
  static final HashMap serverMap = new HashMap();
  private static final HsqlTimer timer = new HsqlTimer();
  
  public static Vector getDatabaseURIs()
  {
    Vector localVector = new Vector();
    synchronized (databaseIDMap)
    {
      Iterator localIterator = databaseIDMap.values().iterator();
      while (localIterator.hasNext())
      {
        Database localDatabase = (Database)localIterator.next();
        localVector.addElement(localDatabase.getURI());
      }
    }
    return localVector;
  }
  
  public static void closeDatabases(int paramInt)
  {
    synchronized (databaseIDMap)
    {
      Iterator localIterator = databaseIDMap.values().iterator();
      while (localIterator.hasNext())
      {
        Database localDatabase = (Database)localIterator.next();
        try
        {
          localDatabase.close(paramInt);
        }
        catch (HsqlException localHsqlException) {}
      }
    }
  }
  
  public static Session newSession(int paramInt1, String paramString1, String paramString2, String paramString3, int paramInt2)
  {
    Database localDatabase = null;
    synchronized (databaseIDMap)
    {
      localDatabase = (Database)databaseIDMap.get(paramInt1);
    }
    if (localDatabase == null) {
      return null;
    }
    ??? = localDatabase.connect(paramString1, paramString2, paramString3, paramInt2);
    ((Session)???).isNetwork = true;
    return (Session)???;
  }
  
  public static Session newSession(String paramString1, String paramString2, String paramString3, String paramString4, HsqlProperties paramHsqlProperties, String paramString5, int paramInt)
  {
    Database localDatabase = getDatabase(paramString1, paramString2, paramHsqlProperties);
    return localDatabase.connect(paramString3, paramString4, paramString5, paramInt);
  }
  
  public static Session getSession(int paramInt, long paramLong)
  {
    Database localDatabase = null;
    synchronized (databaseIDMap)
    {
      localDatabase = (Database)databaseIDMap.get(paramInt);
    }
    return localDatabase == null ? null : localDatabase.sessionManager.getSession(paramLong);
  }
  
  public static int getDatabase(String paramString1, String paramString2, Notified paramNotified, HsqlProperties paramHsqlProperties)
  {
    Database localDatabase = getDatabase(paramString1, paramString2, paramHsqlProperties);
    registerServer(paramNotified, localDatabase);
    return localDatabase.databaseID;
  }
  
  public static Database getDatabase(int paramInt)
  {
    return (Database)databaseIDMap.get(paramInt);
  }
  
  public static void shutdownDatabases(Notified paramNotified, int paramInt)
  {
    Database[] arrayOfDatabase;
    synchronized (serverMap)
    {
      HashSet localHashSet = (HashSet)serverMap.get(paramNotified);
      if (localHashSet == null)
      {
        arrayOfDatabase = new Database[0];
      }
      else
      {
        arrayOfDatabase = new Database[localHashSet.size()];
        localHashSet.toArray(arrayOfDatabase);
      }
    }
    for (int i = 0; i < arrayOfDatabase.length; i++) {
      arrayOfDatabase[i].close(paramInt);
    }
  }
  
  public static Database getDatabase(String paramString1, String paramString2, HsqlProperties paramHsqlProperties)
  {
    DatabaseType localDatabaseType = DatabaseType.get(paramString1);
    Database localDatabase = getDatabaseObject(localDatabaseType, paramString2, paramHsqlProperties);
    synchronized (localDatabase)
    {
      switch (localDatabase.getState())
      {
      case 1: 
        break;
      case 4: 
        if (lookupDatabaseObject(localDatabaseType, paramString2) == null) {
          addDatabaseObject(localDatabaseType, paramString2, localDatabase);
        }
        localDatabase.open();
        break;
      case 2: 
      case 3: 
        throw Error.error(451, 23);
      }
    }
    return localDatabase;
  }
  
  private static synchronized Database getDatabaseObject(DatabaseType paramDatabaseType, String paramString, HsqlProperties paramHsqlProperties)
  {
    Object localObject1 = paramString;
    HashMap localHashMap;
    switch (paramDatabaseType)
    {
    case DB_FILE: 
      localHashMap = fileDatabaseMap;
      localObject1 = filePathToKey(paramString);
      localDatabase = (Database)localHashMap.get(localObject1);
      if ((localDatabase == null) && (localHashMap.size() > 0))
      {
        Iterator localIterator = localHashMap.keySet().iterator();
        while (localIterator.hasNext())
        {
          String str = (String)localIterator.next();
          if (((String)localObject1).equalsIgnoreCase(str))
          {
            localObject1 = str;
            break;
          }
        }
      }
      break;
    case DB_RES: 
      localHashMap = resDatabaseMap;
      break;
    case DB_MEM: 
      localHashMap = memDatabaseMap;
      break;
    default: 
      throw Error.runtimeError(201, "DatabaseManager");
    }
    Database localDatabase = (Database)localHashMap.get(localObject1);
    if (localDatabase == null)
    {
      localDatabase = new Database(paramDatabaseType, paramString, (String)localObject1, paramHsqlProperties);
      localDatabase.databaseID = dbIDCounter;
      synchronized (databaseIDMap)
      {
        databaseIDMap.put(dbIDCounter, localDatabase);
        dbIDCounter += 1;
      }
      localHashMap.put(localObject1, localDatabase);
    }
    return localDatabase;
  }
  
  public static synchronized Database lookupDatabaseObject(DatabaseType paramDatabaseType, String paramString)
  {
    String str = paramString;
    HashMap localHashMap;
    if (paramDatabaseType == DatabaseType.DB_FILE)
    {
      localHashMap = fileDatabaseMap;
      str = filePathToKey(paramString);
    }
    else if (paramDatabaseType == DatabaseType.DB_RES)
    {
      localHashMap = resDatabaseMap;
    }
    else if (paramDatabaseType == DatabaseType.DB_MEM)
    {
      localHashMap = memDatabaseMap;
    }
    else
    {
      throw Error.runtimeError(201, "DatabaseManager");
    }
    return (Database)localHashMap.get(str);
  }
  
  private static synchronized void addDatabaseObject(DatabaseType paramDatabaseType, String paramString, Database paramDatabase)
  {
    String str = paramString;
    HashMap localHashMap;
    if (paramDatabaseType == DatabaseType.DB_FILE)
    {
      localHashMap = fileDatabaseMap;
      str = filePathToKey(paramString);
    }
    else if (paramDatabaseType == DatabaseType.DB_RES)
    {
      localHashMap = resDatabaseMap;
    }
    else if (paramDatabaseType == DatabaseType.DB_MEM)
    {
      localHashMap = memDatabaseMap;
    }
    else
    {
      throw Error.runtimeError(201, "DatabaseManager");
    }
    synchronized (databaseIDMap)
    {
      databaseIDMap.put(paramDatabase.databaseID, paramDatabase);
    }
    localHashMap.put(str, paramDatabase);
  }
  
  static void removeDatabase(Database paramDatabase)
  {
    int i = paramDatabase.databaseID;
    DatabaseType localDatabaseType = paramDatabase.getType();
    String str1 = paramDatabase.getPath();
    String str2 = str1;
    notifyServers(paramDatabase);
    HashMap localHashMap;
    if (localDatabaseType == DatabaseType.DB_FILE)
    {
      localHashMap = fileDatabaseMap;
      str2 = filePathToKey(str1);
    }
    else if (localDatabaseType == DatabaseType.DB_RES)
    {
      localHashMap = resDatabaseMap;
    }
    else if (localDatabaseType == DatabaseType.DB_MEM)
    {
      localHashMap = memDatabaseMap;
    }
    else
    {
      throw Error.runtimeError(201, "DatabaseManager");
    }
    boolean bool = false;
    synchronized (databaseIDMap)
    {
      databaseIDMap.remove(i);
      bool = databaseIDMap.isEmpty();
    }
    synchronized (localHashMap)
    {
      localHashMap.remove(str2);
    }
    if (bool) {
      ValuePool.resetPool();
    }
  }
  
  public static void deRegisterServer(Notified paramNotified)
  {
    synchronized (serverMap)
    {
      serverMap.remove(paramNotified);
    }
  }
  
  private static void registerServer(Notified paramNotified, Database paramDatabase)
  {
    synchronized (serverMap)
    {
      if (!serverMap.containsKey(paramNotified)) {
        serverMap.put(paramNotified, new HashSet());
      }
      HashSet localHashSet = (HashSet)serverMap.get(paramNotified);
      localHashSet.add(paramDatabase);
    }
  }
  
  private static void notifyServers(Database paramDatabase)
  {
    Notified[] arrayOfNotified;
    synchronized (serverMap)
    {
      arrayOfNotified = new Notified[serverMap.size()];
      serverMap.keysToArray(arrayOfNotified);
    }
    for (int i = 0; i < arrayOfNotified.length; i++)
    {
      Notified localNotified = arrayOfNotified[i];
      boolean bool = false;
      HashSet localHashSet;
      synchronized (serverMap)
      {
        localHashSet = (HashSet)serverMap.get(localNotified);
      }
      if (localHashSet != null) {
        synchronized (localHashSet)
        {
          bool = localHashSet.remove(paramDatabase);
        }
      }
      if (bool) {
        localNotified.notify(paramDatabase.databaseID);
      }
    }
  }
  
  static boolean isServerDB(Database paramDatabase)
  {
    Iterator localIterator = serverMap.keySet().iterator();
    while (localIterator.hasNext())
    {
      Notified localNotified = (Notified)localIterator.next();
      HashSet localHashSet = (HashSet)serverMap.get(localNotified);
      if (localHashSet.contains(paramDatabase)) {
        return true;
      }
    }
    return false;
  }
  
  public static HsqlTimer getTimer()
  {
    return timer;
  }
  
  private static String filePathToKey(String paramString)
  {
    try
    {
      return FileUtil.getFileUtil().canonicalPath(paramString);
    }
    catch (Exception localException) {}
    return paramString;
  }
}


/* Location:              E:\java\java学习\hutubill\lib\all.jar!\org\hsqldb\DatabaseManager.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */
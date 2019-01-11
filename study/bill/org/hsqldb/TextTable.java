package org.hsqldb;

import org.hsqldb.error.Error;
import org.hsqldb.lib.StringConverter;
import org.hsqldb.persist.DataFileCache;
import org.hsqldb.persist.Logger;
import org.hsqldb.persist.PersistentStore;
import org.hsqldb.persist.PersistentStoreCollectionDatabase;
import org.hsqldb.persist.TextCache;
import org.hsqldb.persist.TextFileReader;
import org.hsqldb.persist.TextTableStorageManager;
import org.hsqldb.rights.Grantee;
import org.hsqldb.rowio.RowInputInterface;

public class TextTable
  extends Table
{
  String dataSource = "";
  boolean isReversed = false;
  boolean isConnected = false;
  
  TextTable(Database paramDatabase, HsqlNameManager.HsqlName paramHsqlName, int paramInt)
  {
    super(paramDatabase, paramHsqlName, paramInt);
    this.isWithDataSource = true;
  }
  
  public boolean isConnected()
  {
    return this.isConnected;
  }
  
  public void connect(Session paramSession)
  {
    connect(paramSession, this.isReadOnly);
  }
  
  private void connect(Session paramSession, boolean paramBoolean)
  {
    if ((this.dataSource.length() == 0) || (this.isConnected)) {
      return;
    }
    PersistentStore localPersistentStore = this.database.persistentStoreCollection.getStore(this);
    this.store = localPersistentStore;
    TextCache localTextCache = null;
    TextFileReader localTextFileReader = null;
    boolean bool = (this.isReadOnly) || (this.database.isReadOnly());
    String str = this.database.logger.getSecurePath(this.dataSource, false, true);
    if (str == null) {
      throw Error.error(457, this.dataSource);
    }
    try
    {
      localTextCache = (TextCache)this.database.logger.textTableManager.openTextFilePersistence(this, str, bool, this.isReversed);
      localPersistentStore.setCache(localTextCache);
      localTextFileReader = localTextCache.getTextFileReader();
      Row localRow = null;
      if (localTextCache.isIgnoreFirstLine())
      {
        localTextFileReader.readHeaderLine();
        localTextCache.setHeaderInitialise(localTextFileReader.getHeaderLine());
      }
      for (;;)
      {
        RowInputInterface localRowInputInterface = localTextFileReader.readObject();
        if (localRowInputInterface == null) {
          break;
        }
        localRow = (Row)localPersistentStore.get(localRowInputInterface);
        if (localRow == null) {
          break;
        }
        Object[] arrayOfObject = localRow.getData();
        systemUpdateIdentityValue(arrayOfObject);
        enforceRowConstraints(paramSession, arrayOfObject);
        localPersistentStore.indexRow(paramSession, localRow);
      }
    }
    catch (Throwable localThrowable)
    {
      long l = localTextFileReader == null ? 0L : localTextFileReader.getLineNumber();
      clearAllData(paramSession);
      if (localTextCache != null)
      {
        this.database.logger.textTableManager.closeTextCache(this);
        localPersistentStore.release();
      }
      throw Error.error(localThrowable, 483, 0, new Object[] { Long.valueOf(l), localThrowable.toString() });
    }
    this.isConnected = true;
    this.isReadOnly = paramBoolean;
  }
  
  public void disconnect()
  {
    this.store = null;
    PersistentStore localPersistentStore = this.database.persistentStoreCollection.getStore(this);
    localPersistentStore.release();
    this.isConnected = false;
  }
  
  private void openCache(Session paramSession, String paramString, boolean paramBoolean1, boolean paramBoolean2)
  {
    String str = this.dataSource;
    boolean bool1 = this.isReversed;
    boolean bool2 = this.isReadOnly;
    if (paramString == null) {
      paramString = "";
    }
    disconnect();
    this.dataSource = paramString;
    this.isReversed = ((paramBoolean1) && (this.dataSource.length() > 0));
    try
    {
      connect(paramSession, (paramBoolean2) || (paramBoolean1));
    }
    catch (HsqlException localHsqlException)
    {
      this.dataSource = str;
      this.isReversed = bool1;
      connect(paramSession, bool2);
      throw localHsqlException;
    }
  }
  
  void setDataSource(Session paramSession, String paramString, boolean paramBoolean1, boolean paramBoolean2)
  {
    if (getTableType() != 6) {
      paramSession.getGrantee().checkSchemaUpdateOrGrantRights(getSchemaName().name);
    }
    paramString = paramString.trim();
    if ((paramBoolean1) || (paramBoolean1 != this.isReversed) || (!this.dataSource.equals(paramString)) || (!this.isConnected)) {
      openCache(paramSession, paramString, paramBoolean1, this.isReadOnly);
    }
    if (this.isReversed) {
      this.isReadOnly = true;
    }
  }
  
  public String getDataSource()
  {
    return this.dataSource;
  }
  
  public boolean isDescDataSource()
  {
    return this.isReversed;
  }
  
  public void setHeader(String paramString)
  {
    PersistentStore localPersistentStore = this.database.persistentStoreCollection.getStore(this);
    TextCache localTextCache = (TextCache)localPersistentStore.getCache();
    if ((localTextCache != null) && (localTextCache.isIgnoreFirstLine()))
    {
      localTextCache.setHeader(paramString);
      return;
    }
    throw Error.error(486);
  }
  
  private String getHeader()
  {
    PersistentStore localPersistentStore = this.database.persistentStoreCollection.getStore(this);
    TextCache localTextCache = (TextCache)localPersistentStore.getCache();
    String str = localTextCache == null ? null : localTextCache.getHeader();
    return str == null ? null : StringConverter.toQuotedString(str, '\'', true);
  }
  
  public void checkDataReadOnly()
  {
    if (this.dataSource.length() == 0)
    {
      String str = getName().getSchemaQualifiedStatementName();
      throw Error.error(481, str);
    }
    if (isDataReadOnly()) {
      throw Error.error(456);
    }
  }
  
  public boolean isDataReadOnly()
  {
    return (!isConnected()) || (super.isDataReadOnly()) || (this.store.getCache().isDataReadOnly());
  }
  
  public void setDataReadOnly(boolean paramBoolean)
  {
    if (!paramBoolean)
    {
      if (this.isReversed) {
        throw Error.error(456);
      }
      if (this.database.isFilesReadOnly()) {
        throw Error.error(455);
      }
      if (isConnected())
      {
        this.store.getCache().close();
        this.store.getCache().open(paramBoolean);
      }
    }
    this.isReadOnly = paramBoolean;
  }
  
  public void insertData(Session paramSession, PersistentStore paramPersistentStore, Object[] paramArrayOfObject)
  {
    Row localRow = (Row)paramPersistentStore.getNewCachedObject(paramSession, paramArrayOfObject, false);
    paramPersistentStore.indexRow(paramSession, localRow);
    paramPersistentStore.commitPersistence(localRow);
  }
  
  String getDataSourceDDL()
  {
    String str = getDataSource();
    if (str == null) {
      return null;
    }
    StringBuffer localStringBuffer = new StringBuffer(128);
    localStringBuffer.append("SET").append(' ').append("TABLE").append(' ');
    localStringBuffer.append(getName().getSchemaQualifiedStatementName());
    localStringBuffer.append(' ').append("SOURCE").append(' ').append('\'');
    localStringBuffer.append(str);
    localStringBuffer.append('\'');
    return localStringBuffer.toString();
  }
  
  String getDataSourceHeader()
  {
    String str = getHeader();
    if (str == null) {
      return null;
    }
    StringBuffer localStringBuffer = new StringBuffer(128);
    localStringBuffer.append("SET").append(' ').append("TABLE").append(' ');
    localStringBuffer.append(getName().getSchemaQualifiedStatementName());
    localStringBuffer.append(' ').append("SOURCE").append(' ');
    localStringBuffer.append("HEADER").append(' ');
    localStringBuffer.append(str);
    return localStringBuffer.toString();
  }
}


/* Location:              E:\java\java学习\hutubill\lib\all.jar!\org\hsqldb\TextTable.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */
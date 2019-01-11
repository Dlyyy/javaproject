package org.hsqldb.persist;

import org.hsqldb.HsqlException;
import org.hsqldb.Table;
import org.hsqldb.lib.Collection;
import org.hsqldb.lib.HashMap;
import org.hsqldb.lib.Iterator;

public class TextTableStorageManager
{
  private HashMap textCacheList = new HashMap();
  
  public DataFileCache openTextFilePersistence(Table paramTable, String paramString, boolean paramBoolean1, boolean paramBoolean2)
  {
    closeTextCache(paramTable);
    TextCache localTextCache = new TextCache(paramTable, paramString);
    localTextCache.open((paramBoolean1) || (paramBoolean2));
    this.textCacheList.put(paramTable.getName(), localTextCache);
    return localTextCache;
  }
  
  public void closeTextCache(Table paramTable)
  {
    TextCache localTextCache = (TextCache)this.textCacheList.remove(paramTable.getName());
    if (localTextCache != null) {
      try
      {
        localTextCache.close();
      }
      catch (HsqlException localHsqlException) {}
    }
  }
  
  public void closeAllTextCaches(boolean paramBoolean)
  {
    Iterator localIterator = this.textCacheList.values().iterator();
    while (localIterator.hasNext())
    {
      TextCache localTextCache = (TextCache)localIterator.next();
      if ((paramBoolean) && (!localTextCache.table.isDataReadOnly())) {
        localTextCache.purge();
      } else {
        localTextCache.close();
      }
    }
  }
  
  public boolean isAnyTextCacheModified()
  {
    Iterator localIterator = this.textCacheList.values().iterator();
    while (localIterator.hasNext()) {
      if (((TextCache)localIterator.next()).isModified()) {
        return true;
      }
    }
    return false;
  }
}


/* Location:              E:\java\java学习\hutubill\lib\all.jar!\org\hsqldb\persist\TextTableStorageManager.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */
package org.hsqldb.persist;

import org.hsqldb.Database;
import org.hsqldb.HsqlNameManager.HsqlName;
import org.hsqldb.SchemaManager;
import org.hsqldb.Session;
import org.hsqldb.Table;
import org.hsqldb.error.Error;
import org.hsqldb.lib.DoubleIntIndex;
import org.hsqldb.lib.FileAccess;
import org.hsqldb.lib.HsqlArrayList;
import org.hsqldb.lib.StopWatch;
import org.hsqldb.lib.StringUtil;

final class DataFileDefrag
{
  DataFileCache dataFileOut;
  StopWatch stopw = new StopWatch();
  String dataFileName;
  long[][] rootsList;
  Database database;
  DataFileCache dataCache;
  int scale;
  DoubleIntIndex pointerLookup;
  
  DataFileDefrag(Database paramDatabase, DataFileCache paramDataFileCache)
  {
    this.database = paramDatabase;
    this.dataCache = paramDataFileCache;
    this.scale = paramDataFileCache.getDataFileScale();
    this.dataFileName = paramDataFileCache.getFileName();
  }
  
  void process(Session paramSession)
  {
    Object localObject1 = null;
    this.database.logger.logDetailEvent("Defrag process begins");
    HsqlArrayList localHsqlArrayList = this.database.schemaManager.getAllTables(true);
    this.rootsList = new long[localHsqlArrayList.size()][];
    long l1 = 0L;
    int i = 0;
    int j = localHsqlArrayList.size();
    Object localObject2;
    while (i < j)
    {
      Table localTable = (Table)localHsqlArrayList.get(i);
      if (localTable.getTableType() == 5)
      {
        localObject2 = this.database.persistentStoreCollection.getStore(localTable);
        long l2 = ((PersistentStore)localObject2).elementCount();
        if (l2 > l1) {
          l1 = l2;
        }
      }
      i++;
    }
    if (l1 > 2147483647L) {
      throw Error.error(3426);
    }
    try
    {
      String str = this.database.getCanonicalPath();
      this.pointerLookup = new DoubleIntIndex((int)l1, false);
      this.dataFileOut = new DataFileCache(this.database, str, true);
      this.pointerLookup.setKeysSearchTarget();
      j = 0;
      int k = localHsqlArrayList.size();
      while (j < k)
      {
        localObject2 = (Table)localHsqlArrayList.get(j);
        if (((Table)localObject2).getTableType() == 5)
        {
          long[] arrayOfLong = writeTableToDataFile((Table)localObject2);
          this.rootsList[j] = arrayOfLong;
        }
        else
        {
          this.rootsList[j] = null;
        }
        this.database.logger.logDetailEvent("table complete " + ((Table)localObject2).getName().name);
        j++;
      }
      this.dataFileOut.close();
      this.dataFileOut = null;
      j = 0;
      k = this.rootsList.length;
      while (j < k)
      {
        localObject2 = this.rootsList[j];
        if (localObject2 != null) {
          this.database.logger.logDetailEvent("roots: " + StringUtil.getList((long[])localObject2, ",", ""));
        }
        j++;
      }
    }
    catch (OutOfMemoryError localOutOfMemoryError)
    {
      localObject1 = localOutOfMemoryError;
      throw Error.error(460, localOutOfMemoryError);
    }
    catch (Throwable localThrowable2)
    {
      localObject1 = localThrowable2;
      throw Error.error(458, localThrowable2);
    }
    finally
    {
      try
      {
        if (this.dataFileOut != null) {
          this.dataFileOut.release();
        }
      }
      catch (Throwable localThrowable3) {}
      if ((localObject1 instanceof OutOfMemoryError)) {
        this.database.logger.logInfoEvent("defrag failed - out of memory - required: " + l1 * 8L);
      }
      if (localObject1 == null)
      {
        this.database.logger.logDetailEvent("Defrag transfer complete: " + this.stopw.elapsedTime());
      }
      else
      {
        this.database.logger.logSevereEvent("defrag failed ", (Throwable)localObject1);
        this.database.logger.getFileAccess().removeElement(this.dataFileName + ".new");
      }
    }
  }
  
  long[] writeTableToDataFile(Table paramTable)
  {
    RowStoreAVLDisk localRowStoreAVLDisk = (RowStoreAVLDisk)paramTable.database.persistentStoreCollection.getStore(paramTable);
    long[] arrayOfLong = paramTable.getIndexRootsArray();
    this.pointerLookup.clear();
    this.database.logger.logDetailEvent("lookup begins " + paramTable.getName().name + " " + this.stopw.elapsedTime());
    localRowStoreAVLDisk.moveDataToSpace(this.dataFileOut, this.pointerLookup);
    for (int i = 0; i < paramTable.getIndexCount(); i++) {
      if (arrayOfLong[i] != -1L)
      {
        long l2 = this.pointerLookup.lookup(arrayOfLong[i], -1L);
        if (l2 == -1L) {
          throw Error.error(466);
        }
        arrayOfLong[i] = l2;
      }
    }
    long l1 = localRowStoreAVLDisk.elementCount();
    if (l1 != this.pointerLookup.size()) {
      this.database.logger.logSevereEvent("discrepency in row count " + paramTable.getName().name + " " + l1 + " " + this.pointerLookup.size(), null);
    }
    this.database.logger.logDetailEvent("table written " + paramTable.getName().name);
    return arrayOfLong;
  }
  
  public long[][] getIndexRoots()
  {
    return this.rootsList;
  }
}


/* Location:              E:\java\java学习\hutubill\lib\all.jar!\org\hsqldb\persist\DataFileDefrag.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */
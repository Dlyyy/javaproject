package org.hsqldb.persist;

import java.lang.reflect.Constructor;
import org.hsqldb.Session;
import org.hsqldb.error.Error;
import org.hsqldb.rowio.RowInputInterface;
import org.hsqldb.rowio.RowOutputInterface;

public class BlockObjectStore
  extends SimpleStore
{
  Class objectClass;
  Constructor constructor;
  int storageSize;
  int blockSize;
  
  public BlockObjectStore(DataFileCache paramDataFileCache, TableSpaceManager paramTableSpaceManager, Class paramClass, int paramInt1, int paramInt2)
  {
    this.cache = paramDataFileCache;
    this.spaceManager = paramTableSpaceManager;
    this.objectClass = paramClass;
    this.blockSize = paramInt2;
    this.storageSize = paramInt1;
    try
    {
      this.constructor = paramClass.getConstructor(new Class[] { Integer.TYPE });
    }
    catch (Exception localException)
    {
      throw Error.runtimeError(201, "BlockObjectStore");
    }
  }
  
  public CachedObject get(long paramLong)
  {
    return this.cache.get(paramLong, this.storageSize, this, false);
  }
  
  public CachedObject get(CachedObject paramCachedObject, boolean paramBoolean)
  {
    return this.cache.get(paramCachedObject, this, paramBoolean);
  }
  
  public CachedObject get(long paramLong, boolean paramBoolean)
  {
    return this.cache.get(paramLong, this.storageSize, this, paramBoolean);
  }
  
  public void add(Session paramSession, CachedObject paramCachedObject, boolean paramBoolean)
  {
    throw Error.runtimeError(201, "BlockObjectStore");
  }
  
  public void add(CachedObject paramCachedObject, boolean paramBoolean)
  {
    int i = paramCachedObject.getRealSize(this.cache.rowOut);
    i = this.cache.rowOut.getStorageSize(i);
    if (i > this.storageSize) {
      throw Error.runtimeError(201, "BlockObjectStore");
    }
    paramCachedObject.setStorageSize(this.storageSize);
    long l = this.spaceManager.getFilePosition(i, true);
    paramCachedObject.setPos(l);
    this.cache.add(paramCachedObject, paramBoolean);
  }
  
  public CachedObject get(RowInputInterface paramRowInputInterface)
  {
    CachedObject localCachedObject = getNewInstance();
    localCachedObject.read(paramRowInputInterface);
    int i = localCachedObject.getRealSize(this.cache.rowOut);
    i = this.cache.rowOut.getStorageSize(i);
    if (i > this.storageSize) {
      throw Error.runtimeError(201, "BlockObjectStore");
    }
    localCachedObject.setStorageSize(this.storageSize);
    return localCachedObject;
  }
  
  public CachedObject getNewInstance(int paramInt)
  {
    throw Error.runtimeError(201, "BlockObjectStore");
  }
  
  private CachedObject getNewInstance()
  {
    try
    {
      CachedObject localCachedObject = (CachedObject)this.constructor.newInstance(new Object[] { Integer.valueOf(this.blockSize) });
      return localCachedObject;
    }
    catch (Exception localException) {}
    return null;
  }
}


/* Location:              E:\java\java学习\hutubill\lib\all.jar!\org\hsqldb\persist\BlockObjectStore.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */
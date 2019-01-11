package org.hsqldb.lib;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;

public class ReadWriteLockDummy
  implements ReadWriteLock
{
  public Lock readLock()
  {
    return new LockDummy();
  }
  
  public Lock writeLock()
  {
    return new LockDummy();
  }
  
  public static class LockDummy
    implements Lock
  {
    public void lock() {}
    
    public void lockInterruptibly()
      throws InterruptedException
    {}
    
    public boolean tryLock()
    {
      return false;
    }
    
    public boolean tryLock(long paramLong, TimeUnit paramTimeUnit)
      throws InterruptedException
    {
      return false;
    }
    
    public void unlock() {}
    
    public Condition newCondition()
    {
      return null;
    }
  }
}


/* Location:              E:\java\java学习\hutubill\lib\all.jar!\org\hsqldb\lib\ReadWriteLockDummy.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */
package org.hsqldb.lib;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.AbstractQueuedSynchronizer;

public class CountUpDownLatch
{
  private final Sync sync;
  
  public CountUpDownLatch()
  {
    this(0);
  }
  
  public CountUpDownLatch(int paramInt)
  {
    if (paramInt < 0) {
      throw new IllegalArgumentException("count < 0");
    }
    this.sync = new Sync(paramInt);
  }
  
  public void await()
    throws InterruptedException
  {
    this.sync.acquireSharedInterruptibly(1);
  }
  
  public boolean await(long paramLong, TimeUnit paramTimeUnit)
    throws InterruptedException
  {
    return this.sync.tryAcquireSharedNanos(1, paramTimeUnit.toNanos(paramLong));
  }
  
  public void countUp()
  {
    this.sync.releaseShared(this.sync.getCount() + 1);
  }
  
  public void countDown()
  {
    this.sync.releaseShared(this.sync.getCount() - 1);
  }
  
  public long getCount()
  {
    return this.sync.getCount();
  }
  
  public void setCount(int paramInt)
  {
    this.sync.releaseShared(paramInt);
  }
  
  public String toString()
  {
    return super.toString() + "[Count = " + this.sync.getCount() + "]";
  }
  
  private static final class Sync
    extends AbstractQueuedSynchronizer
  {
    private static final long serialVersionUID = 1L;
    
    Sync(int paramInt)
    {
      setState(paramInt);
    }
    
    int getCount()
    {
      return getState();
    }
    
    protected int tryAcquireShared(int paramInt)
    {
      return getState() == 0 ? 1 : -1;
    }
    
    protected boolean tryReleaseShared(int paramInt)
    {
      int i = Math.max(0, paramInt);
      boolean bool = i == 0;
      while (!compareAndSetState(getState(), i)) {}
      return bool;
    }
  }
}


/* Location:              E:\java\java学习\hutubill\lib\all.jar!\org\hsqldb\lib\CountUpDownLatch.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */
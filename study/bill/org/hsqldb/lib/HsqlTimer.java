package org.hsqldb.lib;

import java.util.Comparator;
import java.util.Date;

public final class HsqlTimer
  implements Comparator, ThreadFactory
{
  protected final TaskQueue taskQueue = new TaskQueue(16, this);
  protected final TaskRunner taskRunner = new TaskRunner();
  protected Thread taskRunnerThread;
  protected final ThreadFactory threadFactory = paramThreadFactory == null ? this : paramThreadFactory;
  protected volatile boolean isShutdown;
  static int nowCount = 0;
  
  public HsqlTimer()
  {
    this(null);
  }
  
  public HsqlTimer(ThreadFactory paramThreadFactory) {}
  
  public int compare(Object paramObject1, Object paramObject2)
  {
    long l1 = ((Task)paramObject1).getNextScheduled();
    long l2 = ((Task)paramObject2).getNextScheduled();
    return l1 == l2 ? 0 : l1 < l2 ? -1 : 1;
  }
  
  public Thread newThread(Runnable paramRunnable)
  {
    Thread localThread = new Thread(paramRunnable);
    localThread.setName("HSQLDB Timer @" + Integer.toHexString(hashCode()));
    localThread.setDaemon(true);
    return localThread;
  }
  
  public synchronized Thread getThread()
  {
    return this.taskRunnerThread;
  }
  
  public synchronized void restart()
    throws IllegalStateException
  {
    if (this.isShutdown) {
      throw new IllegalStateException("isShutdown==true");
    }
    if (this.taskRunnerThread == null)
    {
      this.taskRunnerThread = this.threadFactory.newThread(this.taskRunner);
      this.taskRunnerThread.start();
    }
    else
    {
      this.taskQueue.unpark();
    }
  }
  
  public Object scheduleAfter(long paramLong, Runnable paramRunnable)
    throws IllegalArgumentException
  {
    if (paramRunnable == null) {
      throw new IllegalArgumentException("runnable == null");
    }
    return addTask(now() + paramLong, paramRunnable, 0L, false);
  }
  
  public Object scheduleAt(Date paramDate, Runnable paramRunnable)
    throws IllegalArgumentException
  {
    if (paramDate == null) {
      throw new IllegalArgumentException("date == null");
    }
    if (paramRunnable == null) {
      throw new IllegalArgumentException("runnable == null");
    }
    return addTask(paramDate.getTime(), paramRunnable, 0L, false);
  }
  
  public Object schedulePeriodicallyAt(Date paramDate, long paramLong, Runnable paramRunnable, boolean paramBoolean)
    throws IllegalArgumentException
  {
    if (paramDate == null) {
      throw new IllegalArgumentException("date == null");
    }
    if (paramLong <= 0L) {
      throw new IllegalArgumentException("period <= 0");
    }
    if (paramRunnable == null) {
      throw new IllegalArgumentException("runnable == null");
    }
    return addTask(paramDate.getTime(), paramRunnable, paramLong, paramBoolean);
  }
  
  public Object schedulePeriodicallyAfter(long paramLong1, long paramLong2, Runnable paramRunnable, boolean paramBoolean)
    throws IllegalArgumentException
  {
    if (paramLong2 <= 0L) {
      throw new IllegalArgumentException("period <= 0");
    }
    if (paramRunnable == null) {
      throw new IllegalArgumentException("runnable == null");
    }
    return addTask(now() + paramLong1, paramRunnable, paramLong2, paramBoolean);
  }
  
  public synchronized void shutdown()
  {
    if (!this.isShutdown)
    {
      this.isShutdown = true;
      this.taskQueue.cancelAllTasks();
    }
  }
  
  public synchronized void shutDown()
  {
    shutdown();
  }
  
  public synchronized void shutdownImmediately()
  {
    if (!this.isShutdown)
    {
      Thread localThread = this.taskRunnerThread;
      this.isShutdown = true;
      if ((localThread != null) && (localThread.isAlive())) {
        localThread.interrupt();
      }
      this.taskQueue.cancelAllTasks();
    }
  }
  
  public static void cancel(Object paramObject)
  {
    if ((paramObject instanceof Task)) {
      ((Task)paramObject).cancel();
    }
  }
  
  public static boolean isCancelled(Object paramObject)
  {
    return (paramObject instanceof Task) ? ((Task)paramObject).isCancelled() : true;
  }
  
  public static boolean isFixedRate(Object paramObject)
  {
    if ((paramObject instanceof Task))
    {
      Task localTask = (Task)paramObject;
      return (localTask.relative) && (localTask.period > 0L);
    }
    return false;
  }
  
  public static boolean isFixedDelay(Object paramObject)
  {
    if ((paramObject instanceof Task))
    {
      Task localTask = (Task)paramObject;
      return (!localTask.relative) && (localTask.period > 0L);
    }
    return false;
  }
  
  public static boolean isPeriodic(Object paramObject)
  {
    return ((Task)paramObject).period > 0L;
  }
  
  public static Date getLastScheduled(Object paramObject)
  {
    if ((paramObject instanceof Task))
    {
      Task localTask = (Task)paramObject;
      long l = localTask.getLastScheduled();
      return l == 0L ? null : new Date(l);
    }
    return null;
  }
  
  public static Object setPeriod(Object paramObject, long paramLong)
  {
    return (paramObject instanceof Task) ? ((Task)paramObject).setPeriod(paramLong) : paramObject;
  }
  
  public static Date getNextScheduled(Object paramObject)
  {
    if ((paramObject instanceof Task))
    {
      Task localTask = (Task)paramObject;
      long l = localTask.isCancelled() ? 0L : localTask.getNextScheduled();
      return l == 0L ? null : new Date(l);
    }
    return null;
  }
  
  protected Task addTask(long paramLong1, Runnable paramRunnable, long paramLong2, boolean paramBoolean)
  {
    if (this.isShutdown) {
      throw new IllegalStateException("shutdown");
    }
    Task localTask = new Task(paramLong1, paramRunnable, paramLong2, paramBoolean);
    this.taskQueue.addTask(localTask);
    restart();
    return localTask;
  }
  
  protected synchronized void clearThread()
  {
    try
    {
      this.taskRunnerThread.setContextClassLoader(null);
    }
    catch (Throwable localThrowable) {}
    this.taskRunnerThread = null;
  }
  
  protected Task nextTask()
  {
    try
    {
      Task localTask;
      long l1;
      long l2;
      while ((!this.isShutdown) || (Thread.interrupted())) {
        synchronized (this.taskQueue)
        {
          localTask = this.taskQueue.peekTask();
          if (localTask == null) {
            break;
          }
          l1 = System.currentTimeMillis();
          l2 = localTask.next;
          long l3 = l2 - l1;
          if (l3 > 0L) {
            this.taskQueue.park(l3);
          } else {
            this.taskQueue.removeTask();
          }
        }
      }
      long l4 = localTask.period;
      if (l4 > 0L)
      {
        if (localTask.relative)
        {
          long l5 = l1 - l2;
          if (l5 > l4) {
            l4 = 0L;
          } else if (l5 > 0L) {
            l4 -= l5;
          }
        }
        localTask.updateSchedule(l1, l1 + l4);
        this.taskQueue.addTask(localTask);
      }
      return localTask;
    }
    catch (InterruptedException localInterruptedException) {}
    return null;
  }
  
  static long now()
  {
    nowCount += 1;
    return System.currentTimeMillis();
  }
  
  protected static class TaskQueue
    extends HsqlArrayHeap
  {
    TaskQueue(int paramInt, Comparator paramComparator)
    {
      super(paramComparator);
    }
    
    void addTask(HsqlTimer.Task paramTask)
    {
      super.add(paramTask);
    }
    
    void cancelAllTasks()
    {
      Object[] arrayOfObject;
      Object localObject1;
      synchronized (this)
      {
        arrayOfObject = this.heap;
        localObject1 = this.count;
        this.heap = new Object[1];
        this.count = 0;
      }
      for (??? = 0; ??? < localObject1; ???++) {
        ((HsqlTimer.Task)arrayOfObject[???]).cancelled = true;
      }
    }
    
    synchronized void park(long paramLong)
      throws InterruptedException
    {
      wait(paramLong);
    }
    
    synchronized HsqlTimer.Task peekTask()
    {
      while ((this.heap[0] != null) && (((HsqlTimer.Task)this.heap[0]).isCancelled())) {
        super.remove();
      }
      return (HsqlTimer.Task)this.heap[0];
    }
    
    synchronized void signalTaskCancelled(HsqlTimer.Task paramTask)
    {
      if (paramTask == this.heap[0])
      {
        super.remove();
        notify();
      }
    }
    
    HsqlTimer.Task removeTask()
    {
      return (HsqlTimer.Task)super.remove();
    }
    
    synchronized void unpark()
    {
      notify();
    }
  }
  
  protected class Task
  {
    Runnable runnable;
    long period;
    long last;
    long next;
    boolean cancelled = false;
    private final Object cancel_mutex = new Object();
    final boolean relative;
    
    Task(long paramLong1, Runnable paramRunnable, long paramLong2, boolean paramBoolean)
    {
      this.next = paramLong1;
      this.runnable = paramRunnable;
      this.period = paramLong2;
      this.relative = paramBoolean;
    }
    
    void cancel()
    {
      int i = 0;
      synchronized (this.cancel_mutex)
      {
        if (!this.cancelled) {
          this.cancelled = (i = 1);
        }
      }
      if (i != 0) {
        HsqlTimer.this.taskQueue.signalTaskCancelled(this);
      }
    }
    
    /* Error */
    boolean isCancelled()
    {
      // Byte code:
      //   0: aload_0
      //   1: getfield 5	org/hsqldb/lib/HsqlTimer$Task:cancel_mutex	Ljava/lang/Object;
      //   4: dup
      //   5: astore_1
      //   6: monitorenter
      //   7: aload_0
      //   8: getfield 3	org/hsqldb/lib/HsqlTimer$Task:cancelled	Z
      //   11: aload_1
      //   12: monitorexit
      //   13: ireturn
      //   14: astore_2
      //   15: aload_1
      //   16: monitorexit
      //   17: aload_2
      //   18: athrow
      // Local variable table:
      //   start	length	slot	name	signature
      //   0	19	0	this	Task
      //   5	11	1	Ljava/lang/Object;	Object
      //   14	4	2	localObject1	Object
      // Exception table:
      //   from	to	target	type
      //   7	13	14	finally
      //   14	17	14	finally
    }
    
    synchronized long getLastScheduled()
    {
      return this.last;
    }
    
    synchronized long getNextScheduled()
    {
      return this.next;
    }
    
    synchronized void updateSchedule(long paramLong1, long paramLong2)
    {
      this.last = paramLong1;
      this.next = paramLong2;
    }
    
    synchronized Object setPeriod(long paramLong)
    {
      if ((this.period == paramLong) || (isCancelled())) {
        return this;
      }
      if (paramLong > this.period)
      {
        this.period = paramLong;
        return this;
      }
      cancel();
      return HsqlTimer.this.addTask(HsqlTimer.now(), this.runnable, paramLong, this.relative);
    }
  }
  
  protected class TaskRunner
    implements Runnable
  {
    protected TaskRunner() {}
    
    public void run()
    {
      try
      {
        for (;;)
        {
          HsqlTimer.Task localTask = HsqlTimer.this.nextTask();
          if (localTask == null) {
            break;
          }
          localTask.runnable.run();
        }
      }
      finally
      {
        HsqlTimer.this.clearThread();
      }
    }
  }
}


/* Location:              E:\java\java学习\hutubill\lib\all.jar!\org\hsqldb\lib\HsqlTimer.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */
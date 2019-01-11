package org.hsqldb.lib;

class HsqlThreadFactory
  implements ThreadFactory
{
  protected ThreadFactory factory;
  
  public HsqlThreadFactory()
  {
    this(null);
  }
  
  public HsqlThreadFactory(ThreadFactory paramThreadFactory)
  {
    setImpl(paramThreadFactory);
  }
  
  public Thread newThread(Runnable paramRunnable)
  {
    return this.factory == this ? new Thread(paramRunnable) : this.factory.newThread(paramRunnable);
  }
  
  public synchronized ThreadFactory setImpl(ThreadFactory paramThreadFactory)
  {
    ThreadFactory localThreadFactory = this.factory;
    this.factory = (paramThreadFactory == null ? this : paramThreadFactory);
    return localThreadFactory;
  }
  
  public synchronized ThreadFactory getImpl()
  {
    return this.factory;
  }
}


/* Location:              E:\java\java学习\hutubill\lib\all.jar!\org\hsqldb\lib\HsqlThreadFactory.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */
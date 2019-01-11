package org.hsqldb.lib;

public class StopWatch
{
  private long startTime;
  private long lastStart;
  private long total;
  boolean running = false;
  
  public StopWatch()
  {
    this(true);
  }
  
  public StopWatch(boolean paramBoolean)
  {
    if (paramBoolean) {
      start();
    }
  }
  
  public long elapsedTime()
  {
    if (this.running) {
      return this.total + System.currentTimeMillis() - this.startTime;
    }
    return this.total;
  }
  
  public long currentElapsedTime()
  {
    if (this.running) {
      return System.currentTimeMillis() - this.startTime;
    }
    return 0L;
  }
  
  public void zero()
  {
    this.total = 0L;
    start();
  }
  
  public void start()
  {
    this.startTime = System.currentTimeMillis();
    this.running = true;
  }
  
  public void stop()
  {
    if (this.running)
    {
      this.total += System.currentTimeMillis() - this.startTime;
      this.running = false;
    }
  }
  
  public void mark()
  {
    stop();
    start();
  }
  
  public String elapsedTimeToMessage(String paramString)
  {
    return paramString + " in " + elapsedTime() + " ms.";
  }
  
  public String currentElapsedTimeToMessage(String paramString)
  {
    return paramString + " in " + currentElapsedTime() + " ms.";
  }
  
  public String toString()
  {
    return super.toString() + "[running=" + this.running + ", startTime=" + this.startTime + ", total=" + this.total + "]";
  }
}


/* Location:              E:\java\java学习\hutubill\lib\all.jar!\org\hsqldb\lib\StopWatch.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */
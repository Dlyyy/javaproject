package org.hsqldb.lib;

import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;
import org.hsqldb.HsqlDateTime;

public class SimpleLog
{
  public static final int LOG_NONE = 0;
  public static final int LOG_ERROR = 1;
  public static final int LOG_NORMAL = 2;
  public static final int LOG_DETAIL = 3;
  public static final int LOG_RESULT = 4;
  public static final String logTypeNameEngine = "ENGINE";
  static final String[] appLogTypeNames = { "", "ERROR ", "NORMAL", "DETAIL" };
  static final String[] sqlLogTypeNames = { "", "BASIC ", "NORMAL", "DETAIL", "RESULT" };
  private PrintWriter writer;
  private int level;
  private boolean isSystem;
  private boolean isSQL;
  String[] logTypeNames;
  private String filePath;
  private StringBuffer sb;
  
  public SimpleLog(String paramString, int paramInt, boolean paramBoolean)
  {
    this.isSystem = (paramString == null);
    this.filePath = paramString;
    this.isSQL = paramBoolean;
    this.logTypeNames = (paramBoolean ? sqlLogTypeNames : appLogTypeNames);
    this.sb = new StringBuffer(256);
    setLevel(paramInt);
  }
  
  private void setupWriter()
  {
    if (this.level == 0)
    {
      close();
      return;
    }
    if (this.writer == null) {
      if (this.isSystem)
      {
        this.writer = new PrintWriter(System.out);
      }
      else
      {
        File localFile = new File(this.filePath);
        setupLog(localFile);
      }
    }
  }
  
  private void setupLog(File paramFile)
  {
    try
    {
      FileUtil.getFileUtil().makeParentDirectories(paramFile);
      this.writer = new PrintWriter(new FileWriter(paramFile, true), true);
    }
    catch (Exception localException)
    {
      this.isSystem = true;
      this.writer = new PrintWriter(System.out);
    }
  }
  
  public int getLevel()
  {
    return this.level;
  }
  
  public void setLevel(int paramInt)
  {
    this.level = paramInt;
    setupWriter();
  }
  
  public PrintWriter getPrintWriter()
  {
    return this.writer;
  }
  
  public synchronized void logContext(int paramInt, String paramString)
  {
    if (this.level < paramInt) {
      return;
    }
    if (this.writer == null) {
      return;
    }
    this.sb.append(HsqlDateTime.getSystemTimeString()).append(' ');
    if (!this.isSQL) {
      this.sb.append(this.logTypeNames[paramInt]).append(' ');
    }
    this.sb.append(paramString);
    this.writer.println(this.sb.toString());
    this.sb.setLength(0);
    this.writer.flush();
  }
  
  public synchronized void logContext(int paramInt, String paramString1, String paramString2, String paramString3)
  {
    if (this.level < paramInt) {
      return;
    }
    if (this.writer == null) {
      return;
    }
    this.sb.append(HsqlDateTime.getSystemTimeString()).append(' ');
    if (!this.isSQL) {
      this.sb.append(this.logTypeNames[paramInt]).append(' ');
    }
    this.sb.append(paramString1).append(' ');
    this.sb.append(paramString2).append(' ').append(paramString3);
    this.writer.println(this.sb.toString());
    this.sb.setLength(0);
    this.writer.flush();
  }
  
  public synchronized void logContext(Throwable paramThrowable, String paramString, int paramInt)
  {
    if (this.level == 0) {
      return;
    }
    if (this.writer == null) {
      return;
    }
    this.sb.append(HsqlDateTime.getSystemTimeString()).append(' ');
    if (!this.isSQL) {
      this.sb.append(this.logTypeNames[paramInt]).append(' ');
    }
    this.sb.append(paramString);
    Throwable localThrowable = new Throwable();
    StackTraceElement[] arrayOfStackTraceElement = localThrowable.getStackTrace();
    if (arrayOfStackTraceElement.length > 1)
    {
      this.sb.append(' ');
      this.sb.append(arrayOfStackTraceElement[1].getClassName()).append('.');
      this.sb.append(arrayOfStackTraceElement[1].getMethodName());
    }
    arrayOfStackTraceElement = paramThrowable.getStackTrace();
    if (arrayOfStackTraceElement.length > 0)
    {
      this.sb.append(' ');
      this.sb.append(arrayOfStackTraceElement[0].getClassName()).append('.');
      this.sb.append(' ').append(arrayOfStackTraceElement[0].getMethodName());
    }
    this.sb.append(' ').append(paramThrowable.toString());
    this.writer.println(this.sb.toString());
    this.sb.setLength(0);
    this.writer.flush();
  }
  
  public void flush()
  {
    if (this.writer != null) {
      this.writer.flush();
    }
  }
  
  public void close()
  {
    if ((this.writer != null) && (!this.isSystem))
    {
      this.writer.flush();
      this.writer.close();
    }
    this.writer = null;
  }
}


/* Location:              E:\java\java学习\hutubill\lib\all.jar!\org\hsqldb\lib\SimpleLog.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */
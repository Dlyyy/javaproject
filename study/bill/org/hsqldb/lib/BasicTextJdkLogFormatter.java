package org.hsqldb.lib;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Formatter;
import java.util.logging.LogRecord;

public class BasicTextJdkLogFormatter
  extends Formatter
{
  public static final String LS = System.getProperty("line.separator");
  protected boolean withTime = true;
  protected SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ");
  
  public BasicTextJdkLogFormatter(boolean paramBoolean)
  {
    this.withTime = paramBoolean;
  }
  
  public BasicTextJdkLogFormatter() {}
  
  public String format(LogRecord paramLogRecord)
  {
    StringBuilder localStringBuilder = new StringBuilder();
    if (this.withTime) {
      localStringBuilder.append(this.sdf.format(new Date(paramLogRecord.getMillis())) + "  ");
    }
    localStringBuilder.append(paramLogRecord.getLevel() + "  " + formatMessage(paramLogRecord));
    if (paramLogRecord.getThrown() != null)
    {
      StringWriter localStringWriter = new StringWriter();
      paramLogRecord.getThrown().printStackTrace(new PrintWriter(localStringWriter));
      localStringBuilder.append(LS + localStringWriter);
    }
    return localStringBuilder.toString() + LS;
  }
}


/* Location:              E:\java\java学习\hutubill\lib\all.jar!\org\hsqldb\lib\BasicTextJdkLogFormatter.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */
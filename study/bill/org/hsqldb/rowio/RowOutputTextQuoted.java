package org.hsqldb.rowio;

import org.hsqldb.lib.StringConverter;
import org.hsqldb.persist.TextFileSettings;

public class RowOutputTextQuoted
  extends RowOutputText
{
  public RowOutputTextQuoted(TextFileSettings paramTextFileSettings)
  {
    super(paramTextFileSettings);
  }
  
  protected String checkConvertString(String paramString1, String paramString2)
  {
    if ((this.textFileSettings.isAllQuoted) || (paramString1.length() == 0) || (paramString1.indexOf(this.textFileSettings.quoteChar) != -1) || ((paramString2.length() > 0) && (paramString1.indexOf(paramString2) != -1)) || (hasUnprintable(paramString1))) {
      paramString1 = StringConverter.toQuotedString(paramString1, this.textFileSettings.quoteChar, true);
    }
    return paramString1;
  }
  
  private static boolean hasUnprintable(String paramString)
  {
    int i = 0;
    int j = paramString.length();
    while (i < j)
    {
      if (Character.isISOControl(paramString.charAt(i))) {
        return true;
      }
      i++;
    }
    return false;
  }
}


/* Location:              E:\java\java学习\hutubill\lib\all.jar!\org\hsqldb\rowio\RowOutputTextQuoted.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */
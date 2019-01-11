package org.hsqldb.rowio;

import org.hsqldb.error.Error;
import org.hsqldb.persist.TextFileSettings;

public class RowInputTextQuoted
  extends RowInputText
{
  private static final int NORMAL_FIELD = 0;
  private static final int NEED_END_QUOTE = 1;
  private static final int FOUND_QUOTE = 2;
  private final char quoteChar;
  int charLength = 0;
  
  public RowInputTextQuoted(TextFileSettings paramTextFileSettings)
  {
    super(paramTextFileSettings);
    this.quoteChar = paramTextFileSettings.quoteChar;
  }
  
  public void setSource(String paramString, long paramLong, int paramInt)
  {
    super.setSource(paramString, paramLong, paramInt);
    this.charLength = paramString.length();
    for (int i = this.charLength - 1; (i > -1) && ((paramString.charAt(i) == '\r') || (paramString.charAt(i) == '\n')); i--) {
      this.charLength -= 1;
    }
  }
  
  protected String getField(String paramString, int paramInt, boolean paramBoolean)
  {
    String str1 = null;
    if ((this.next >= this.charLength) || (this.text.charAt(this.next) != this.quoteChar)) {
      return super.getField(paramString, paramInt, paramBoolean);
    }
    try
    {
      this.field += 1;
      StringBuffer localStringBuffer = new StringBuffer();
      int i = 0;
      int j = 0;
      int k = -1;
      if (!paramBoolean) {
        k = this.text.indexOf(paramString, this.next);
      }
      while (this.next < this.charLength)
      {
        switch (j)
        {
        case 0: 
        default: 
          if (this.next == k)
          {
            this.next += paramInt;
            i = 1;
          }
          else if (this.text.charAt(this.next) == this.quoteChar)
          {
            j = 1;
          }
          else
          {
            localStringBuffer.append(this.text.charAt(this.next));
          }
          break;
        case 1: 
          if (this.text.charAt(this.next) == this.quoteChar) {
            j = 2;
          } else {
            localStringBuffer.append(this.text.charAt(this.next));
          }
          break;
        case 2: 
          if (this.text.charAt(this.next) == this.quoteChar)
          {
            localStringBuffer.append(this.text.charAt(this.next));
            j = 1;
          }
          else if (!paramBoolean)
          {
            k = this.text.indexOf(paramString, this.next);
            if (k < 0) {
              k = this.charLength;
            }
            localStringBuffer.append(this.text, this.next, k);
            this.next = (k + paramInt);
            i = 1;
          }
          else
          {
            this.next += paramInt - 1;
            j = 0;
          }
          break;
        }
        if (i != 0) {
          break;
        }
        this.next += 1;
      }
      str1 = localStringBuffer.toString();
    }
    catch (Exception localException)
    {
      String str2 = String.valueOf(this.field);
      throw Error.error(localException, 41, str2);
    }
    return str1;
  }
}


/* Location:              E:\java\java学习\hutubill\lib\all.jar!\org\hsqldb\rowio\RowInputTextQuoted.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */
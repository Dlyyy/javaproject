package org.hsqldb.persist;

import java.io.UnsupportedEncodingException;
import org.hsqldb.Database;
import org.hsqldb.error.Error;

public class TextFileSettings
{
  public static final String NL = System.getProperty("line.separator");
  public String fs;
  public String vs;
  public String lvs;
  public String qc;
  public char quoteChar;
  public String stringEncoding;
  public boolean isQuoted;
  public boolean isAllQuoted;
  public boolean ignoreFirst;
  public String charEncoding;
  public boolean isUTF8;
  public boolean isUTF16;
  public boolean hasUTF16BOM;
  public boolean isLittleEndian;
  private static final byte[] BYTES_NL = NL.getBytes();
  private static final byte[] SP = { 32 };
  Database database;
  String dataFileName;
  int maxCacheRows;
  int maxCacheBytes;
  char singleSeparator = '\000';
  byte[] bytesForLineEnd = BYTES_NL;
  byte[] bytesForSpace = SP;
  static final char DOUBLE_QUOTE_CHAR = '"';
  static final char BACKSLASH_CHAR = '\\';
  public static final char LF_CHAR = '\n';
  public static final char CR_CHAR = '\r';
  
  TextFileSettings(Database paramDatabase, String paramString)
  {
    this.database = paramDatabase;
    HsqlProperties localHsqlProperties = HsqlProperties.delimitedArgPairsToProps(paramString, "=", ";", "textdb");
    HsqlDatabaseProperties localHsqlDatabaseProperties = paramDatabase.getProperties();
    switch (localHsqlProperties.errorCodes.length)
    {
    case 0: 
      this.dataFileName = null;
      break;
    case 1: 
      this.dataFileName = localHsqlProperties.errorKeys[0].trim();
      break;
    default: 
      throw Error.error(302);
    }
    this.fs = localHsqlDatabaseProperties.getStringProperty("textdb.fs");
    this.fs = localHsqlProperties.getProperty("textdb.fs", this.fs);
    this.vs = localHsqlDatabaseProperties.getStringProperty("textdb.vs");
    this.vs = localHsqlProperties.getProperty("textdb.vs", this.vs);
    this.lvs = localHsqlDatabaseProperties.getStringProperty("textdb.lvs");
    this.lvs = localHsqlProperties.getProperty("textdb.lvs", this.lvs);
    this.qc = localHsqlDatabaseProperties.getStringProperty("textdb.qc");
    this.qc = localHsqlProperties.getProperty("textdb.qc", this.qc);
    if (this.vs == null) {
      this.vs = this.fs;
    }
    if (this.lvs == null) {
      this.lvs = this.fs;
    }
    this.fs = translateSep(this.fs);
    this.vs = translateSep(this.vs);
    this.lvs = translateSep(this.lvs);
    this.qc = translateSep(this.qc);
    if ((this.fs.length() == 0) || (this.vs.length() == 0) || (this.lvs.length() == 0)) {
      throw Error.error(303);
    }
    if (this.qc.length() != 1) {
      throw Error.error(304);
    }
    this.quoteChar = this.qc.charAt(0);
    if (this.quoteChar > '') {
      throw Error.error(304);
    }
    this.ignoreFirst = localHsqlDatabaseProperties.isPropertyTrue("textdb.ignore_first");
    this.ignoreFirst = localHsqlProperties.isPropertyTrue("textdb.ignore_first", this.ignoreFirst);
    this.isQuoted = localHsqlDatabaseProperties.isPropertyTrue("textdb.quoted");
    this.isQuoted = localHsqlProperties.isPropertyTrue("textdb.quoted", this.isQuoted);
    this.isAllQuoted = localHsqlDatabaseProperties.isPropertyTrue("textdb.all_quoted");
    this.isAllQuoted = localHsqlProperties.isPropertyTrue("textdb.all_quoted", this.isAllQuoted);
    this.stringEncoding = localHsqlDatabaseProperties.getStringProperty("textdb.encoding");
    this.stringEncoding = localHsqlProperties.getProperty("textdb.encoding", this.stringEncoding);
    this.charEncoding = this.stringEncoding;
    if ("UTF8".equals(this.stringEncoding))
    {
      this.isUTF8 = true;
    }
    else if ("UTF-8".equals(this.stringEncoding))
    {
      this.isUTF8 = true;
    }
    else if ("UTF-16".equals(this.stringEncoding))
    {
      this.charEncoding = "UTF-16BE";
      this.isUTF16 = true;
    }
    else if ("UTF-16BE".equals(this.stringEncoding))
    {
      this.isUTF16 = true;
    }
    else if ("UTF-16LE".equals(this.stringEncoding))
    {
      this.isUTF16 = true;
      this.isLittleEndian = true;
    }
    setSpaceAndLineEnd();
    if ((this.fs.length() == 1) || ((this.fs.length() == 2) && (this.fs.endsWith("\n")))) {
      this.singleSeparator = this.fs.charAt(0);
    }
    int i = localHsqlDatabaseProperties.getIntegerProperty("textdb.cache_scale");
    i = localHsqlProperties.getIntegerProperty("textdb.cache_scale", i);
    int j = localHsqlDatabaseProperties.getIntegerProperty("textdb.cache_size_scale");
    j = localHsqlProperties.getIntegerProperty("textdb.cache_size_scale", j);
    this.maxCacheRows = ((1 << i) * 3);
    this.maxCacheRows = localHsqlDatabaseProperties.getIntegerProperty("textdb.cache_rows", this.maxCacheRows);
    this.maxCacheRows = localHsqlProperties.getIntegerProperty("textdb.cache_rows", this.maxCacheRows);
    this.maxCacheBytes = ((1 << j) * this.maxCacheRows / 1024);
    if (this.maxCacheBytes < 4) {
      this.maxCacheBytes = 4;
    }
    this.maxCacheBytes = localHsqlDatabaseProperties.getIntegerProperty("textdb.cache_size", this.maxCacheBytes);
    this.maxCacheBytes = localHsqlProperties.getIntegerProperty("textdb.cache_size", this.maxCacheBytes);
    this.maxCacheBytes *= 1024;
  }
  
  String getFileName()
  {
    return this.dataFileName;
  }
  
  int getMaxCacheRows()
  {
    return this.maxCacheRows;
  }
  
  int getMaxCacheBytes()
  {
    return this.maxCacheBytes;
  }
  
  void setLittleEndianByteOrderMark()
  {
    if ("UTF-16".equals(this.stringEncoding))
    {
      this.charEncoding = "UTF-16LE";
      this.isLittleEndian = true;
      this.hasUTF16BOM = true;
    }
    else
    {
      throw Error.error(331);
    }
  }
  
  void setSpaceAndLineEnd()
  {
    try
    {
      if (this.isUTF16)
      {
        this.bytesForLineEnd = NL.getBytes(this.charEncoding);
        this.bytesForSpace = " ".getBytes(this.charEncoding);
      }
    }
    catch (UnsupportedEncodingException localUnsupportedEncodingException)
    {
      throw Error.error(331);
    }
  }
  
  private static String translateSep(String paramString)
  {
    return translateSep(paramString, false);
  }
  
  private static String translateSep(String paramString, boolean paramBoolean)
  {
    if (paramString == null) {
      return null;
    }
    int i = paramString.indexOf('\\');
    if (i != -1)
    {
      int j = 0;
      char[] arrayOfChar = paramString.toCharArray();
      int k = 0;
      int m = paramString.length();
      StringBuffer localStringBuffer = new StringBuffer(m);
      do
      {
        localStringBuffer.append(arrayOfChar, j, i - j);
        i++;
        j = i;
        if (i >= m)
        {
          localStringBuffer.append('\\');
          break;
        }
        if (!paramBoolean) {
          k = arrayOfChar[i];
        }
        if (k == 110)
        {
          localStringBuffer.append('\n');
          j++;
        }
        else if (k == 114)
        {
          localStringBuffer.append('\r');
          j++;
        }
        else if (k == 116)
        {
          localStringBuffer.append('\t');
          j++;
        }
        else if (k == 92)
        {
          localStringBuffer.append('\\');
          j++;
        }
        else if (k == 117)
        {
          j++;
          localStringBuffer.append((char)Integer.parseInt(paramString.substring(j, j + 4), 16));
          j += 4;
        }
        else if (paramString.startsWith("semi", i))
        {
          localStringBuffer.append(';');
          j += 4;
        }
        else if (paramString.startsWith("space", i))
        {
          localStringBuffer.append(' ');
          j += 5;
        }
        else if (paramString.startsWith("quote", i))
        {
          localStringBuffer.append('"');
          j += 5;
        }
        else if (paramString.startsWith("apos", i))
        {
          localStringBuffer.append('\'');
          j += 4;
        }
        else
        {
          localStringBuffer.append('\\');
          localStringBuffer.append(arrayOfChar[i]);
          j++;
        }
      } while ((i = paramString.indexOf('\\', j)) != -1);
      localStringBuffer.append(arrayOfChar, j, m - j);
      paramString = localStringBuffer.toString();
    }
    return paramString;
  }
}


/* Location:              E:\java\java学习\hutubill\lib\all.jar!\org\hsqldb\persist\TextFileSettings.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */
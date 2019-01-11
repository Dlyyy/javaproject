package org.hsqldb.types;

import org.hsqldb.HsqlNameManager;
import org.hsqldb.HsqlNameManager.HsqlName;
import org.hsqldb.SchemaObject;
import org.hsqldb.Session;
import org.hsqldb.lib.OrderedHashSet;
import org.hsqldb.rights.Grantee;

public class Charset
  implements SchemaObject
{
  public static final int[][] uppercaseLetters = { { 65, 90 } };
  public static final int[][] unquotedIdentifier = { { 48, 57 }, { 65, 90 }, { 95, 95 } };
  public static final int[][] basicIdentifier = { { 48, 57 }, { 65, 90 }, { 95, 95 }, { 97, 122 } };
  public static final Charset SQL_TEXT;
  public static final Charset SQL_IDENTIFIER_CHARSET;
  public static final Charset SQL_CHARACTER;
  public static final Charset ASCII_GRAPHIC;
  public static final Charset GRAPHIC_IRV;
  public static final Charset ASCII_FULL;
  public static final Charset ISO8BIT;
  public static final Charset LATIN1;
  public static final Charset UTF32;
  public static final Charset UTF16;
  public static final Charset UTF8;
  HsqlNameManager.HsqlName name;
  public HsqlNameManager.HsqlName base;
  int[][] ranges;
  
  public Charset(HsqlNameManager.HsqlName paramHsqlName)
  {
    this.name = paramHsqlName;
  }
  
  public int getType()
  {
    return 14;
  }
  
  public HsqlNameManager.HsqlName getName()
  {
    return this.name;
  }
  
  public HsqlNameManager.HsqlName getCatalogName()
  {
    return this.name.schema.schema;
  }
  
  public HsqlNameManager.HsqlName getSchemaName()
  {
    return this.name.schema;
  }
  
  public Grantee getOwner()
  {
    return this.name.schema.owner;
  }
  
  public OrderedHashSet getReferences()
  {
    OrderedHashSet localOrderedHashSet = new OrderedHashSet();
    localOrderedHashSet.add(this.base);
    return localOrderedHashSet;
  }
  
  public OrderedHashSet getComponents()
  {
    return null;
  }
  
  public void compile(Session paramSession, SchemaObject paramSchemaObject) {}
  
  public String getSQL()
  {
    StringBuffer localStringBuffer = new StringBuffer();
    localStringBuffer.append("CREATE").append(' ').append("CHARACTER").append(' ').append("SET").append(' ');
    if ("INFORMATION_SCHEMA".equals(this.name.schema.name)) {
      localStringBuffer.append(this.name.getStatementName());
    } else {
      localStringBuffer.append(this.name.getSchemaQualifiedStatementName());
    }
    if (this.base != null)
    {
      localStringBuffer.append(' ').append("AS").append(' ').append("GET");
      localStringBuffer.append(' ');
      if ("INFORMATION_SCHEMA".equals(this.base.schema.name)) {
        localStringBuffer.append(this.base.getStatementName());
      } else {
        localStringBuffer.append(this.base.getSchemaQualifiedStatementName());
      }
    }
    return localStringBuffer.toString();
  }
  
  public long getChangeTimestamp()
  {
    return 0L;
  }
  
  public static boolean isInSet(String paramString, int[][] paramArrayOfInt)
  {
    int i = paramString.length();
    label64:
    for (int j = 0; j < i; j++)
    {
      int k = paramString.charAt(j);
      for (int m = 0; m < paramArrayOfInt.length; m++) {
        if (k <= paramArrayOfInt[m][1])
        {
          if (k >= paramArrayOfInt[m][0]) {
            break label64;
          }
          return false;
        }
      }
      return false;
    }
    return true;
  }
  
  public static boolean startsWith(String paramString, int[][] paramArrayOfInt)
  {
    int i = paramString.charAt(0);
    for (int j = 0; j < paramArrayOfInt.length; j++) {
      if (i <= paramArrayOfInt[j][1]) {
        return i >= paramArrayOfInt[j][0];
      }
    }
    return false;
  }
  
  public static Charset getDefaultInstance()
  {
    return SQL_TEXT;
  }
  
  static
  {
    HsqlNameManager.HsqlName localHsqlName = HsqlNameManager.newInfoSchemaObjectName("SQL_TEXT", false, 14);
    SQL_TEXT = new Charset(localHsqlName);
    localHsqlName = HsqlNameManager.newInfoSchemaObjectName("SQL_IDENTIFIER", false, 14);
    SQL_IDENTIFIER_CHARSET = new Charset(localHsqlName);
    localHsqlName = HsqlNameManager.newInfoSchemaObjectName("SQL_CHARACTER", false, 14);
    SQL_CHARACTER = new Charset(localHsqlName);
    localHsqlName = HsqlNameManager.newInfoSchemaObjectName("LATIN1", false, 14);
    LATIN1 = new Charset(localHsqlName);
    localHsqlName = HsqlNameManager.newInfoSchemaObjectName("ASCII_GRAPHIC", false, 14);
    ASCII_GRAPHIC = new Charset(localHsqlName);
    localHsqlName = HsqlNameManager.newInfoSchemaObjectName("GRAPHIC_IRV", false, 14);
    GRAPHIC_IRV = new Charset(localHsqlName);
    localHsqlName = HsqlNameManager.newInfoSchemaObjectName("ASCII_FULL", false, 14);
    ASCII_FULL = new Charset(localHsqlName);
    localHsqlName = HsqlNameManager.newInfoSchemaObjectName("ISO8BIT", false, 14);
    ISO8BIT = new Charset(localHsqlName);
    localHsqlName = HsqlNameManager.newInfoSchemaObjectName("UTF32", false, 14);
    UTF32 = new Charset(localHsqlName);
    localHsqlName = HsqlNameManager.newInfoSchemaObjectName("UTF16", false, 14);
    UTF16 = new Charset(localHsqlName);
    localHsqlName = HsqlNameManager.newInfoSchemaObjectName("UTF8", false, 14);
    UTF8 = new Charset(localHsqlName);
  }
}


/* Location:              E:\java\java学习\hutubill\lib\all.jar!\org\hsqldb\types\Charset.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */
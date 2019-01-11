package org.hsqldb;

import org.hsqldb.types.Type;

public class Token
{
  String tokenString = "";
  int tokenType = -1;
  Type dataType;
  Object tokenValue;
  String namePrefix;
  String namePrePrefix;
  String namePrePrePrefix;
  String charsetSchema;
  String charsetName;
  String fullString;
  int lobMultiplierType = -1;
  boolean isDelimiter;
  boolean isDelimitedIdentifier;
  boolean isDelimitedPrefix;
  boolean isDelimitedPrePrefix;
  boolean isDelimitedPrePrePrefix;
  boolean isUndelimitedIdentifier;
  boolean hasIrregularChar;
  boolean isReservedIdentifier;
  boolean isCoreReservedIdentifier;
  boolean isHostParameter;
  boolean isMalformed;
  int position;
  Object expression;
  boolean hasColumnList;
  
  void reset()
  {
    this.tokenString = "";
    this.tokenType = -1;
    this.dataType = null;
    this.tokenValue = null;
    this.namePrefix = null;
    this.namePrePrefix = null;
    this.namePrePrePrefix = null;
    this.charsetSchema = null;
    this.charsetName = null;
    this.fullString = null;
    this.lobMultiplierType = -1;
    this.isDelimiter = false;
    this.isDelimitedIdentifier = false;
    this.isDelimitedPrefix = false;
    this.isDelimitedPrePrefix = false;
    this.isDelimitedPrePrePrefix = false;
    this.isUndelimitedIdentifier = false;
    this.hasIrregularChar = false;
    this.isReservedIdentifier = false;
    this.isCoreReservedIdentifier = false;
    this.isHostParameter = false;
    this.isMalformed = false;
    this.expression = null;
    this.hasColumnList = false;
  }
  
  Token duplicate()
  {
    Token localToken = new Token();
    localToken.tokenString = this.tokenString;
    localToken.tokenType = this.tokenType;
    localToken.dataType = this.dataType;
    localToken.tokenValue = this.tokenValue;
    localToken.namePrefix = this.namePrefix;
    localToken.namePrePrefix = this.namePrePrefix;
    localToken.namePrePrePrefix = this.namePrePrePrefix;
    localToken.charsetSchema = this.charsetSchema;
    localToken.charsetName = this.charsetName;
    localToken.fullString = this.fullString;
    localToken.lobMultiplierType = this.lobMultiplierType;
    localToken.isDelimiter = this.isDelimiter;
    localToken.isDelimitedIdentifier = this.isDelimitedIdentifier;
    localToken.isDelimitedPrefix = this.isDelimitedPrefix;
    localToken.isDelimitedPrePrefix = this.isDelimitedPrePrefix;
    localToken.isDelimitedPrePrePrefix = this.isDelimitedPrePrePrefix;
    localToken.isUndelimitedIdentifier = this.isUndelimitedIdentifier;
    localToken.hasIrregularChar = this.hasIrregularChar;
    localToken.isReservedIdentifier = this.isReservedIdentifier;
    localToken.isCoreReservedIdentifier = this.isCoreReservedIdentifier;
    localToken.isHostParameter = this.isHostParameter;
    localToken.isMalformed = this.isMalformed;
    return localToken;
  }
  
  public String getFullString()
  {
    return this.fullString;
  }
  
  public void setExpression(Object paramObject)
  {
    this.expression = paramObject;
  }
  
  public void setWithColumnList()
  {
    this.hasColumnList = true;
  }
  
  String getSQL()
  {
    Object localObject2;
    if ((this.expression instanceof ExpressionColumn))
    {
      if (this.tokenType == 821)
      {
        localObject1 = new StringBuffer();
        localObject2 = (Expression)this.expression;
        if ((((Expression)localObject2).opType == 93) && (((Expression)localObject2).nodes.length > 0))
        {
          ((StringBuffer)localObject1).append(' ');
          for (int i = 0; i < ((Expression)localObject2).nodes.length; i++)
          {
            Expression localExpression = localObject2.nodes[i];
            ColumnSchema localColumnSchema = localExpression.getColumn();
            if (localExpression.opType == 3)
            {
              if (i > 0) {
                ((StringBuffer)localObject1).append(',');
              }
              ((StringBuffer)localObject1).append(localExpression.getColumnName());
            }
            else
            {
              String str;
              if (localExpression.getRangeVariable().tableAlias == null)
              {
                str = localColumnSchema.getName().getSchemaQualifiedStatementName();
              }
              else
              {
                RangeVariable localRangeVariable = localExpression.getRangeVariable();
                str = localRangeVariable.tableAlias.getStatementName() + '.' + localColumnSchema.getName().statementName;
              }
              if (i > 0) {
                ((StringBuffer)localObject1).append(',');
              }
              ((StringBuffer)localObject1).append(str);
            }
          }
          ((StringBuffer)localObject1).append(' ');
        }
        else
        {
          return this.tokenString;
        }
        return ((StringBuffer)localObject1).toString();
      }
    }
    else
    {
      if ((this.expression instanceof Type))
      {
        this.isDelimiter = false;
        localObject1 = (Type)this.expression;
        if ((((Type)localObject1).isDistinctType()) || (((Type)localObject1).isDomainType())) {
          return ((Type)localObject1).getName().getSchemaQualifiedStatementName();
        }
        return ((Type)localObject1).getNameString();
      }
      if ((this.expression instanceof SchemaObject))
      {
        this.isDelimiter = false;
        localObject1 = ((SchemaObject)this.expression).getName().getSchemaQualifiedStatementName();
        if (this.hasColumnList)
        {
          localObject2 = (Table)this.expression;
          localObject1 = (String)localObject1 + ((Table)localObject2).getColumnListSQL(((Table)localObject2).defaultColumnMap, ((Table)localObject2).defaultColumnMap.length);
        }
        return (String)localObject1;
      }
    }
    if ((this.namePrefix == null) && (this.isUndelimitedIdentifier)) {
      return this.tokenString;
    }
    if (this.tokenType == 911) {
      return this.dataType.convertToSQLString(this.tokenValue);
    }
    Object localObject1 = new StringBuffer();
    if (this.namePrePrefix != null)
    {
      if (this.isDelimitedPrePrefix)
      {
        ((StringBuffer)localObject1).append('"');
        ((StringBuffer)localObject1).append(this.namePrePrefix);
        ((StringBuffer)localObject1).append('"');
      }
      else
      {
        ((StringBuffer)localObject1).append(this.namePrePrefix);
      }
      ((StringBuffer)localObject1).append('.');
    }
    if (this.namePrefix != null)
    {
      if (this.isDelimitedPrefix)
      {
        ((StringBuffer)localObject1).append('"');
        ((StringBuffer)localObject1).append(this.namePrefix);
        ((StringBuffer)localObject1).append('"');
      }
      else
      {
        ((StringBuffer)localObject1).append(this.namePrefix);
      }
      ((StringBuffer)localObject1).append('.');
    }
    if (this.isDelimitedIdentifier)
    {
      ((StringBuffer)localObject1).append('"');
      ((StringBuffer)localObject1).append(this.tokenString);
      ((StringBuffer)localObject1).append('"');
      this.isDelimiter = false;
    }
    else
    {
      ((StringBuffer)localObject1).append(this.tokenString);
    }
    return ((StringBuffer)localObject1).toString();
  }
  
  static String getSQL(Token[] paramArrayOfToken)
  {
    boolean bool = true;
    StringBuffer localStringBuffer = new StringBuffer();
    for (int i = 0; i < paramArrayOfToken.length; i++)
    {
      String str = paramArrayOfToken[i].getSQL();
      if ((!paramArrayOfToken[i].isDelimiter) && (!bool)) {
        localStringBuffer.append(' ');
      }
      localStringBuffer.append(str);
      bool = paramArrayOfToken[i].isDelimiter;
    }
    return localStringBuffer.toString();
  }
  
  static Object[] getSimplifiedTokens(Token[] paramArrayOfToken)
  {
    Object[] arrayOfObject = new Object[paramArrayOfToken.length];
    for (int i = 0; i < paramArrayOfToken.length; i++) {
      if (paramArrayOfToken[i].expression == null) {
        arrayOfObject[i] = paramArrayOfToken[i].getSQL();
      } else {
        arrayOfObject[i] = paramArrayOfToken[i].expression;
      }
    }
    return arrayOfObject;
  }
}


/* Location:              E:\java\java学习\hutubill\lib\all.jar!\org\hsqldb\Token.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */
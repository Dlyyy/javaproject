package org.hsqldb;

import java.math.BigDecimal;
import org.hsqldb.error.Error;
import org.hsqldb.lib.ArrayUtil;
import org.hsqldb.lib.HsqlArrayList;
import org.hsqldb.lib.IntKeyIntValueHashMap;
import org.hsqldb.map.ValuePool;
import org.hsqldb.types.IntervalType;
import org.hsqldb.types.NumberType;
import org.hsqldb.types.Type;

public class ParserBase
{
  protected Scanner scanner;
  protected Token token;
  private final Token dummyToken = new Token();
  protected int parsePosition;
  protected HsqlException lastError;
  protected HsqlNameManager.HsqlName lastSynonym;
  protected boolean isCheckOrTriggerCondition;
  protected boolean isSchemaDefinition;
  protected boolean isViewDefinition;
  protected boolean isRecording;
  protected HsqlArrayList recordedStatement;
  static final BigDecimal LONG_MAX_VALUE_INCREMENT = BigDecimal.valueOf(Long.MAX_VALUE).add(BigDecimal.valueOf(1L));
  private static final IntKeyIntValueHashMap expressionTypeMap = new IntKeyIntValueHashMap(37);
  
  ParserBase(Scanner paramScanner)
  {
    this.scanner = paramScanner;
    this.token = paramScanner.token;
  }
  
  public Scanner getScanner()
  {
    return this.scanner;
  }
  
  public int getParsePosition()
  {
    return this.parsePosition;
  }
  
  public void setParsePosition(int paramInt)
  {
    this.parsePosition = paramInt;
  }
  
  void reset(Session paramSession, String paramString)
  {
    this.scanner.reset(paramSession, paramString);
    this.parsePosition = 0;
    this.lastError = null;
    this.lastSynonym = null;
    this.isCheckOrTriggerCondition = false;
    this.isSchemaDefinition = false;
    this.isViewDefinition = false;
    this.isRecording = false;
    this.recordedStatement = null;
  }
  
  int getPosition()
  {
    return this.scanner.getTokenPosition();
  }
  
  void rewind(int paramInt)
  {
    if (paramInt == this.scanner.getTokenPosition()) {
      return;
    }
    this.scanner.position(paramInt);
    if (this.isRecording)
    {
      for (int i = this.recordedStatement.size() - 1; i >= 0; i--)
      {
        Token localToken = (Token)this.recordedStatement.get(i);
        if (localToken.position < paramInt) {
          break;
        }
      }
      this.recordedStatement.setSize(i + 1);
    }
    read();
  }
  
  String getLastPart()
  {
    return this.scanner.getPart(this.parsePosition, this.scanner.getTokenPosition());
  }
  
  String getLastPart(int paramInt)
  {
    return this.scanner.getPart(paramInt, this.scanner.getTokenPosition());
  }
  
  String getLastPartAndCurrent(int paramInt)
  {
    return this.scanner.getPart(paramInt, this.scanner.getPosition());
  }
  
  String getStatement(int paramInt, short[] paramArrayOfShort)
  {
    while ((this.token.tokenType != 841) && (this.token.tokenType != 914) && (ArrayUtil.find(paramArrayOfShort, this.token.tokenType) == -1)) {
      read();
    }
    String str = this.scanner.getPart(paramInt, this.scanner.getTokenPosition());
    return str;
  }
  
  String getStatementForRoutine(int paramInt, short[] paramArrayOfShort)
  {
    int i = 0;
    int j = -1;
    int k = -1;
    for (;;)
    {
      if (this.token.tokenType == 841)
      {
        k = this.scanner.getTokenPosition();
        j = i;
      }
      else if (this.token.tokenType == 914)
      {
        if ((j > 0) && (j == i - 1)) {
          rewind(k);
        }
      }
      else
      {
        if (ArrayUtil.find(paramArrayOfShort, this.token.tokenType) != -1) {
          break;
        }
      }
      read();
      i++;
    }
    String str = this.scanner.getPart(paramInt, this.scanner.getTokenPosition());
    return str;
  }
  
  void startRecording()
  {
    this.recordedStatement = new HsqlArrayList();
    this.recordedStatement.add(this.token.duplicate());
    this.isRecording = true;
  }
  
  Token getRecordedToken()
  {
    if (this.isRecording) {
      return (Token)this.recordedStatement.get(this.recordedStatement.size() - 1);
    }
    return this.token.duplicate();
  }
  
  Token[] getRecordedStatement()
  {
    this.isRecording = false;
    this.recordedStatement.remove(this.recordedStatement.size() - 1);
    Token[] arrayOfToken = new Token[this.recordedStatement.size()];
    this.recordedStatement.toArray(arrayOfToken);
    this.recordedStatement = null;
    return arrayOfToken;
  }
  
  void read()
  {
    this.scanner.scanNext();
    if (this.token.isMalformed)
    {
      int i = -1;
      switch (this.token.tokenType)
      {
      case 922: 
        i = 5587;
        break;
      case 921: 
        i = 5588;
        break;
      case 923: 
        i = 5586;
        break;
      case 919: 
        i = 5584;
        break;
      case -1: 
        i = 5582;
        break;
      case 920: 
        i = 5585;
        break;
      case 924: 
        i = 5589;
        break;
      case 925: 
        i = 5583;
        break;
      }
      throw Error.error(i, this.token.getFullString());
    }
    if (this.isRecording)
    {
      Token localToken = this.token.duplicate();
      localToken.position = this.scanner.getTokenPosition();
      this.recordedStatement.add(localToken);
    }
  }
  
  boolean isReservedKey()
  {
    return this.token.isReservedIdentifier;
  }
  
  boolean isCoreReservedKey()
  {
    return this.token.isCoreReservedIdentifier;
  }
  
  boolean isNonReservedIdentifier()
  {
    return (!this.token.isReservedIdentifier) && ((this.token.isUndelimitedIdentifier) || (this.token.isDelimitedIdentifier));
  }
  
  void checkIsNonReservedIdentifier()
  {
    if (!isNonReservedIdentifier()) {
      throw unexpectedToken();
    }
  }
  
  boolean isNonCoreReservedIdentifier()
  {
    return (!this.token.isCoreReservedIdentifier) && ((this.token.isUndelimitedIdentifier) || (this.token.isDelimitedIdentifier));
  }
  
  void checkIsNonCoreReservedIdentifier()
  {
    if (!isNonCoreReservedIdentifier()) {
      throw unexpectedToken();
    }
  }
  
  void checkIsIrregularCharInIdentifier()
  {
    if (this.token.hasIrregularChar) {
      throw unexpectedToken();
    }
  }
  
  boolean isIdentifier()
  {
    return (this.token.isUndelimitedIdentifier) || (this.token.isDelimitedIdentifier);
  }
  
  void checkIsIdentifier()
  {
    if (!isIdentifier()) {
      throw unexpectedToken();
    }
  }
  
  boolean isDelimitedIdentifier()
  {
    return this.token.isDelimitedIdentifier;
  }
  
  void checkIsDelimitedIdentifier()
  {
    if (!this.token.isDelimitedIdentifier) {
      throw Error.error(5569);
    }
  }
  
  void checkIsUndelimitedIdentifier()
  {
    if (!this.token.isUndelimitedIdentifier) {
      throw unexpectedToken();
    }
  }
  
  void checkIsValue()
  {
    if (this.token.tokenType != 911) {
      throw unexpectedToken();
    }
  }
  
  void checkIsQuotedString()
  {
    if ((this.token.tokenType != 911) || (!this.token.dataType.isCharacterType())) {
      throw unexpectedToken();
    }
  }
  
  void checkIsThis(int paramInt)
  {
    if (this.token.tokenType != paramInt)
    {
      String str = Tokens.getKeyword(paramInt);
      throw unexpectedTokenRequire(str);
    }
  }
  
  boolean isUndelimitedSimpleName()
  {
    return (this.token.isUndelimitedIdentifier) && (this.token.namePrefix == null);
  }
  
  boolean isDelimitedSimpleName()
  {
    return (this.token.isDelimitedIdentifier) && (this.token.namePrefix == null);
  }
  
  boolean isSimpleName()
  {
    return (isNonCoreReservedIdentifier()) && (this.token.namePrefix == null);
  }
  
  void checkIsSimpleName()
  {
    if (!isSimpleName()) {
      throw unexpectedToken();
    }
  }
  
  void readUnquotedIdentifier(String paramString)
  {
    checkIsSimpleName();
    if (!this.token.tokenString.equals(paramString)) {
      throw unexpectedToken();
    }
    read();
  }
  
  String readQuotedString()
  {
    checkIsValue();
    if (!this.token.dataType.isCharacterType()) {
      throw Error.error(5563);
    }
    String str = this.token.tokenString;
    read();
    return str;
  }
  
  void readThis(int paramInt)
  {
    if (this.token.tokenType != paramInt)
    {
      String str = Tokens.getKeyword(paramInt);
      throw unexpectedTokenRequire(str);
    }
    read();
  }
  
  boolean readIfThis(int paramInt)
  {
    if (this.token.tokenType == paramInt)
    {
      read();
      return true;
    }
    return false;
  }
  
  void readThis(String paramString)
  {
    if (!paramString.equals(this.token.tokenString))
    {
      String str = paramString;
      throw unexpectedTokenRequire(str);
    }
    read();
  }
  
  boolean readIfThis(String paramString)
  {
    if (paramString.equals(this.token.tokenString))
    {
      read();
      return true;
    }
    return false;
  }
  
  Integer readIntegerObject()
  {
    int i = readInteger();
    return ValuePool.getInt(i);
  }
  
  int readInteger()
  {
    int i = 0;
    if (this.token.tokenType == 834)
    {
      i = 1;
      read();
    }
    checkIsValue();
    if ((i != 0) && (this.token.dataType.typeCode == 25) && (((Number)this.token.tokenValue).longValue() == 2147483648L))
    {
      read();
      return Integer.MIN_VALUE;
    }
    if (this.token.dataType.typeCode != 4) {
      throw Error.error(5563);
    }
    int j = ((Number)this.token.tokenValue).intValue();
    if (i != 0) {
      j = -j;
    }
    read();
    return j;
  }
  
  long readBigint()
  {
    int i = 0;
    if (this.token.tokenType == 834)
    {
      i = 1;
      read();
    }
    checkIsValue();
    if ((i != 0) && (this.token.dataType.typeCode == 2) && (LONG_MAX_VALUE_INCREMENT.equals(this.token.tokenValue)))
    {
      read();
      return Long.MIN_VALUE;
    }
    if ((this.token.dataType.typeCode != 4) && (this.token.dataType.typeCode != 25)) {
      throw Error.error(5563);
    }
    long l = ((Number)this.token.tokenValue).longValue();
    if (i != 0) {
      l = -l;
    }
    read();
    return l;
  }
  
  Expression readDateTimeIntervalLiteral(Session paramSession)
  {
    int i = getPosition();
    String str;
    Object localObject1;
    Object localObject2;
    switch (this.token.tokenType)
    {
    case 77: 
      read();
      if ((this.token.tokenType == 911) && (this.token.dataType.isCharacterType()))
      {
        str = this.token.tokenString;
        read();
        localObject1 = this.scanner.newDate(str);
        return new ExpressionValue(localObject1, Type.SQL_DATE);
      }
      break;
    case 297: 
      read();
      if ((this.token.tokenType == 911) && (this.token.dataType.isCharacterType()))
      {
        str = this.token.tokenString;
        read();
        localObject1 = this.scanner.newTime(str);
        localObject2 = this.scanner.dateTimeType;
        return new ExpressionValue(localObject1, (Type)localObject2);
      }
      break;
    case 298: 
      read();
      if ((this.token.tokenType == 911) && (this.token.dataType.isCharacterType()))
      {
        str = this.token.tokenString;
        read();
        localObject1 = this.scanner.newTimestamp(str);
        localObject2 = this.scanner.dateTimeType;
        return new ExpressionValue(localObject1, (Type)localObject2);
      }
      break;
    case 150: 
      int j = 0;
      read();
      if (this.token.tokenType == 834)
      {
        read();
        j = 1;
      }
      else if (this.token.tokenType == 837)
      {
        read();
      }
      if (this.token.tokenType == 911)
      {
        localObject1 = this.token.tokenString;
        if ((this.token.dataType.isIntegralType()) || (this.token.dataType.isCharacterType()))
        {
          read();
          localObject2 = readIntervalType(false);
          Object localObject3 = this.scanner.newInterval((String)localObject1, (IntervalType)localObject2);
          localObject2 = (IntervalType)this.scanner.dateTimeType;
          if (j != 0) {
            localObject3 = ((IntervalType)localObject2).negate(localObject3);
          }
          return new ExpressionValue(localObject3, (Type)localObject2);
        }
      }
      break;
    default: 
      throw Error.runtimeError(201, "ParserBase");
    }
    rewind(i);
    return null;
  }
  
  IntervalType readIntervalType(boolean paramBoolean)
  {
    int i = -1;
    int j = -1;
    int m;
    int k = m = this.token.tokenType;
    read();
    if (this.token.tokenType == 836)
    {
      read();
      i = readInteger();
      if (i <= 0) {
        throw Error.error(5592);
      }
      if (this.token.tokenType == 824)
      {
        if (k != 264) {
          throw unexpectedToken();
        }
        read();
        j = readInteger();
        if (j < 0) {
          throw Error.error(5592);
        }
      }
      readThis(822);
    }
    if (this.token.tokenType == 301)
    {
      read();
      m = this.token.tokenType;
      read();
    }
    if (this.token.tokenType == 836)
    {
      if ((m != 264) || (m == k)) {
        throw unexpectedToken();
      }
      read();
      j = readInteger();
      if (j < 0) {
        throw Error.error(5592);
      }
      readThis(822);
    }
    int n = ArrayUtil.find(Tokens.SQL_INTERVAL_FIELD_CODES, k);
    int i1 = ArrayUtil.find(Tokens.SQL_INTERVAL_FIELD_CODES, m);
    if ((i == -1) && (paramBoolean)) {
      if (n == 5) {
        i = 12;
      } else {
        i = 9;
      }
    }
    return IntervalType.getIntervalType(n, i1, i, j);
  }
  
  static int getExpressionType(int paramInt)
  {
    int i = expressionTypeMap.get(paramInt, -1);
    if (i == -1) {
      throw Error.runtimeError(201, "ParserBase");
    }
    return i;
  }
  
  HsqlException unexpectedToken(String paramString)
  {
    return Error.parseError(5581, paramString, this.scanner.getLineNumber());
  }
  
  HsqlException unexpectedTokenRequire(String paramString)
  {
    if (this.token.tokenType == 914) {
      return Error.parseError(5590, 1, this.scanner.getLineNumber(), new Object[] { "", paramString });
    }
    String str;
    if (this.token.charsetSchema != null) {
      str = this.token.charsetSchema;
    } else if (this.token.charsetName != null) {
      str = this.token.charsetName;
    } else if (this.token.namePrePrefix != null) {
      str = this.token.namePrePrefix;
    } else if (this.token.namePrefix != null) {
      str = this.token.namePrefix;
    } else {
      str = this.token.tokenString;
    }
    return Error.parseError(5581, 1, this.scanner.getLineNumber(), new Object[] { str, paramString });
  }
  
  HsqlException unexpectedToken()
  {
    if (this.token.tokenType == 914) {
      return Error.parseError(5590, null, this.scanner.getLineNumber());
    }
    String str;
    if (this.token.charsetSchema != null) {
      str = this.token.charsetSchema;
    } else if (this.token.charsetName != null) {
      str = this.token.charsetName;
    } else if (this.token.namePrePrefix != null) {
      str = this.token.namePrePrefix;
    } else if (this.token.namePrefix != null) {
      str = this.token.namePrefix;
    } else {
      str = this.token.tokenString;
    }
    return Error.parseError(5581, str, this.scanner.getLineNumber());
  }
  
  HsqlException tooManyIdentifiers()
  {
    String str;
    if (this.token.namePrePrePrefix != null) {
      str = this.token.namePrePrePrefix;
    } else if (this.token.namePrePrefix != null) {
      str = this.token.namePrePrefix;
    } else if (this.token.namePrefix != null) {
      str = this.token.namePrefix;
    } else {
      str = this.token.tokenString;
    }
    return Error.parseError(5551, str, this.scanner.getLineNumber());
  }
  
  HsqlException unsupportedFeature()
  {
    return Error.error(1551, this.token.tokenString);
  }
  
  HsqlException unsupportedFeature(String paramString)
  {
    return Error.error(1551, paramString);
  }
  
  public Number convertToNumber(String paramString, NumberType paramNumberType)
  {
    return this.scanner.convertToNumber(paramString, paramNumberType);
  }
  
  static
  {
    expressionTypeMap.put(417, 40);
    expressionTypeMap.put(829, 43);
    expressionTypeMap.put(832, 44);
    expressionTypeMap.put(830, 41);
    expressionTypeMap.put(833, 45);
    expressionTypeMap.put(835, 46);
    expressionTypeMap.put(56, 71);
    expressionTypeMap.put(173, 74);
    expressionTypeMap.put(178, 73);
    expressionTypeMap.put(289, 72);
    expressionTypeMap.put(17, 75);
    expressionTypeMap.put(105, 76);
    expressionTypeMap.put(6, 77);
    expressionTypeMap.put(272, 77);
    expressionTypeMap.put(283, 78);
    expressionTypeMap.put(284, 79);
    expressionTypeMap.put(325, 80);
    expressionTypeMap.put(326, 81);
    expressionTypeMap.put(9, 82);
    expressionTypeMap.put(719, 83);
    expressionTypeMap.put(634, 85);
  }
}


/* Location:              E:\java\java学习\hutubill\lib\all.jar!\org\hsqldb\ParserBase.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */
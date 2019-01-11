package org.hsqldb;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.hsqldb.error.Error;
import org.hsqldb.lib.ArrayUtil;
import org.hsqldb.lib.HsqlArrayList;
import org.hsqldb.lib.IntKeyIntValueHashMap;
import org.hsqldb.lib.OrderedIntHashSet;
import org.hsqldb.lib.StringConverter;
import org.hsqldb.lib.StringUtil;
import org.hsqldb.map.BitMap;
import org.hsqldb.map.ValuePool;
import org.hsqldb.persist.Crypto;
import org.hsqldb.types.ArrayType;
import org.hsqldb.types.BinaryData;
import org.hsqldb.types.BinaryType;
import org.hsqldb.types.BlobData;
import org.hsqldb.types.CharacterType;
import org.hsqldb.types.ClobData;
import org.hsqldb.types.DateTimeType;
import org.hsqldb.types.IntervalMonthData;
import org.hsqldb.types.IntervalSecondData;
import org.hsqldb.types.IntervalType;
import org.hsqldb.types.LobData;
import org.hsqldb.types.NumberType;
import org.hsqldb.types.TimeData;
import org.hsqldb.types.TimestampData;
import org.hsqldb.types.Type;

public class FunctionCustom
  extends FunctionSQL
{
  public static final String[] openGroupNumericFunctions = { "ABS", "ACOS", "ASIN", "ATAN", "ATAN2", "BITAND", "BITOR", "BITXOR", "CEILING", "COS", "COT", "DEGREES", "EXP", "FLOOR", "LOG", "LOG10", "MOD", "PI", "POWER", "RADIANS", "RAND", "ROUND", "ROUNDMAGIC", "SIGN", "SIN", "SQRT", "TAN", "TRUNCATE" };
  public static final String[] openGroupStringFunctions = { "ASCII", "CHAR", "CONCAT", "DIFFERENCE", "HEXTORAW", "INSERT", "LCASE", "LEFT", "LENGTH", "LOCATE", "LTRIM", "RAWTOHEX", "REPEAT", "REPLACE", "RIGHT", "RTRIM", "SOUNDEX", "SPACE", "SUBSTR", "UCASE" };
  public static final String[] openGroupDateTimeFunctions = { "CURDATE", "CURTIME", "DATEDIFF", "DAYNAME", "DAYOFMONTH", "DAYOFWEEK", "DAYOFYEAR", "HOUR", "MINUTE", "MONTH", "MONTHNAME", "NOW", "QUARTER", "SECOND", "SECONDS_SINCE_MIDNIGHT", "TIMESTAMPADD", "TIMESTAMPDIFF", "TO_CHAR", "WEEK", "YEAR" };
  public static final String[] openGroupSystemFunctions = { "DATABASE", "IFNULL", "USER" };
  private static final int FUNC_ACOS = 71;
  private static final int FUNC_ACTION_ID = 72;
  private static final int FUNC_ADD_MONTHS = 73;
  private static final int FUNC_ASCII = 74;
  private static final int FUNC_ASIN = 75;
  private static final int FUNC_ATAN = 76;
  private static final int FUNC_ATAN2 = 77;
  private static final int FUNC_BITAND = 78;
  private static final int FUNC_BITANDNOT = 79;
  private static final int FUNC_BITNOT = 80;
  private static final int FUNC_BITOR = 81;
  private static final int FUNC_BITXOR = 82;
  private static final int FUNC_CHAR = 83;
  private static final int FUNC_CONCAT = 84;
  private static final int FUNC_COS = 85;
  private static final int FUNC_COT = 86;
  private static final int FUNC_CRYPT_KEY = 87;
  private static final int FUNC_DATABASE = 88;
  private static final int FUNC_DATABASE_ISOLATION_LEVEL = 89;
  private static final int FUNC_DATABASE_NAME = 90;
  private static final int FUNC_DATABASE_TIMEZONE = 91;
  private static final int FUNC_DATABASE_VERSION = 92;
  private static final int FUNC_DATE_ADD = 93;
  private static final int FUNC_DATE_SUB = 94;
  private static final int FUNC_DATEADD = 95;
  private static final int FUNC_DATEDIFF = 96;
  private static final int FUNC_DAYS = 97;
  private static final int FUNC_DBTIMEZONE = 98;
  private static final int FUNC_DEGREES = 99;
  private static final int FUNC_DIAGNOSTICS = 100;
  private static final int FUNC_DIFFERENCE = 101;
  private static final int FUNC_FROM_TZ = 102;
  private static final int FUNC_HEXTORAW = 103;
  private static final int FUNC_IDENTITY = 104;
  private static final int FUNC_INSTR = 105;
  private static final int FUNC_ISAUTOCOMMIT = 106;
  private static final int FUNC_ISOLATION_LEVEL = 107;
  private static final int FUNC_ISREADONLYDATABASE = 108;
  private static final int FUNC_ISREADONLYDATABASEFILES = 109;
  private static final int FUNC_ISREADONLYSESSION = 110;
  private static final int FUNC_LAST_DAY = 111;
  private static final int FUNC_LEFT = 112;
  private static final int FUNC_LOAD_FILE = 113;
  private static final int FUNC_LOB_ID = 114;
  private static final int FUNC_LOCATE = 115;
  private static final int FUNC_LOG10 = 116;
  private static final int FUNC_LPAD = 117;
  private static final int FUNC_LTRIM = 118;
  private static final int FUNC_MONTHS_BETWEEN = 119;
  private static final int FUNC_NEW_TIME = 120;
  private static final int FUNC_NEXT_DAY = 121;
  private static final int FUNC_NUMTODSINTERVAL = 122;
  private static final int FUNC_NUMTOYMINTERVAL = 123;
  private static final int FUNC_PI = 124;
  private static final int FUNC_POSITION_ARRAY = 125;
  private static final int FUNC_RADIANS = 126;
  private static final int FUNC_RAND = 127;
  private static final int FUNC_RAWTOHEX = 128;
  private static final int FUNC_REGEXP_MATCHES = 129;
  private static final int FUNC_REGEXP_REPLACE = 130;
  private static final int FUNC_REGEXP_SUBSTRING = 131;
  private static final int FUNC_REGEXP_SUBSTRING_ARRAY = 132;
  private static final int FUNC_REPEAT = 133;
  private static final int FUNC_REPLACE = 134;
  private static final int FUNC_REVERSE = 135;
  private static final int FUNC_RIGHT = 136;
  private static final int FUNC_ROUND = 137;
  private static final int FUNC_ROUNDMAGIC = 138;
  private static final int FUNC_RPAD = 139;
  private static final int FUNC_RTRIM = 140;
  private static final int FUNC_SECONDS_MIDNIGHT = 141;
  private static final int FUNC_SEQUENCE_ARRAY = 142;
  private static final int FUNC_SESSION_ID = 143;
  private static final int FUNC_SESSION_ISOLATION_LEVEL = 144;
  private static final int FUNC_SESSION_TIMEZONE = 145;
  private static final int FUNC_SESSIONTIMEZONE = 146;
  private static final int FUNC_SIGN = 147;
  private static final int FUNC_SIN = 148;
  private static final int FUNC_SOUNDEX = 149;
  private static final int FUNC_SORT_ARRAY = 150;
  private static final int FUNC_SPACE = 151;
  private static final int FUNC_SUBSTR = 152;
  private static final int FUNC_SYS_EXTRACT_UTC = 153;
  private static final int FUNC_SYSDATE = 154;
  private static final int FUNC_SYSTIMESTAMP = 155;
  private static final int FUNC_TAN = 156;
  private static final int FUNC_TIMESTAMP = 157;
  private static final int FUNC_TIMESTAMP_WITH_ZONE = 158;
  private static final int FUNC_TIMESTAMPADD = 159;
  private static final int FUNC_TIMESTAMPDIFF = 160;
  private static final int FUNC_TIMEZONE = 161;
  private static final int FUNC_TO_CHAR = 162;
  private static final int FUNC_TO_DATE = 163;
  private static final int FUNC_TO_DSINTERVAL = 164;
  private static final int FUNC_TO_YMINTERVAL = 165;
  private static final int FUNC_TO_NUMBER = 166;
  private static final int FUNC_TO_TIMESTAMP = 167;
  private static final int FUNC_TO_TIMESTAMP_TZ = 168;
  private static final int FUNC_TRANSACTION_CONTROL = 169;
  private static final int FUNC_TRANSACTION_ID = 170;
  private static final int FUNC_TRANSACTION_SIZE = 171;
  private static final int FUNC_TRANSLATE = 172;
  private static final int FUNC_TRUNC = 173;
  private static final int FUNC_TRUNCATE = 174;
  private static final int FUNC_UUID = 175;
  private static final int FUNC_UNIX_TIMESTAMP = 176;
  private static final int FUNC_UNIX_MILLIS = 177;
  private static final int FUNC_SQLCODE = 182;
  private static final int FUNC_SQLERRM = 183;
  static final IntKeyIntValueHashMap customRegularFuncMap = new IntKeyIntValueHashMap();
  static final IntKeyIntValueHashMap customValueFuncMap;
  private int extractSpec;
  private Pattern pattern;
  private IntKeyIntValueHashMap charLookup;
  
  public static FunctionSQL newCustomFunction(String paramString, int paramInt)
  {
    int i = customRegularFuncMap.get(paramInt, -1);
    if (i == -1) {
      i = customValueFuncMap.get(paramInt, -1);
    }
    if (i == -1) {
      return null;
    }
    switch (paramInt)
    {
    case 453: 
    case 682: 
    case 729: 
    case 734: 
    case 750: 
    case 800: 
      return new FunctionSQL(i);
    case 692: 
    case 693: 
    case 747: 
    case 794: 
      localObject = new FunctionSQL(i);
      ((FunctionSQL)localObject).parseList = optionalNoParamList;
      return (FunctionSQL)localObject;
    case 778: 
      localObject = new FunctionSQL(i);
      ((FunctionSQL)localObject).parseList = tripleParamList;
      return (FunctionSQL)localObject;
    }
    Object localObject = new FunctionCustom(i);
    if (i == 31) {
      switch (paramInt)
      {
      case 737: 
        ((FunctionCustom)localObject).extractSpec = 161;
        break;
      case 765: 
        ((FunctionCustom)localObject).extractSpec = 302;
        break;
      }
    }
    if (i == 5) {
      switch (paramInt)
      {
      case 706: 
        ((FunctionCustom)localObject).extractSpec = 702;
        break;
      case 740: 
        ((FunctionCustom)localObject).extractSpec = 739;
        break;
      case 707: 
        ((FunctionCustom)localObject).extractSpec = 703;
        break;
      case 708: 
        ((FunctionCustom)localObject).extractSpec = 704;
        break;
      case 709: 
        ((FunctionCustom)localObject).extractSpec = 705;
        break;
      case 804: 
        ((FunctionCustom)localObject).extractSpec = 805;
        break;
      default: 
        ((FunctionCustom)localObject).extractSpec = paramInt;
      }
    }
    if (((FunctionCustom)localObject).name == null) {
      ((FunctionCustom)localObject).name = paramString;
    }
    return (FunctionSQL)localObject;
  }
  
  public static boolean isRegularFunction(int paramInt)
  {
    return customRegularFuncMap.get(paramInt, -1) != -1;
  }
  
  public static boolean isValueFunction(int paramInt)
  {
    return customValueFuncMap.get(paramInt, -1) != -1;
  }
  
  private FunctionCustom(int paramInt)
  {
    this.funcType = paramInt;
    this.isDeterministic = (!nonDeterministicFuncSet.contains(paramInt));
    switch (paramInt)
    {
    case 182: 
    case 183: 
      this.parseList = optionalNoParamList;
      break;
    case 154: 
    case 155: 
      this.parseList = optionalNoParamList;
      break;
    case 72: 
    case 88: 
    case 89: 
    case 90: 
    case 91: 
    case 92: 
    case 98: 
    case 106: 
    case 107: 
    case 108: 
    case 109: 
    case 110: 
    case 124: 
    case 143: 
    case 144: 
    case 145: 
    case 146: 
    case 161: 
    case 169: 
    case 170: 
    case 171: 
      this.parseList = emptyParamList;
      break;
    case 71: 
    case 74: 
    case 75: 
    case 76: 
    case 80: 
    case 83: 
    case 85: 
    case 86: 
    case 97: 
    case 99: 
    case 103: 
    case 111: 
    case 114: 
    case 116: 
    case 126: 
    case 128: 
    case 135: 
    case 138: 
    case 147: 
    case 148: 
    case 149: 
    case 151: 
    case 153: 
    case 156: 
    case 158: 
    case 164: 
    case 165: 
    case 166: 
      this.parseList = singleParamList;
      break;
    case 73: 
    case 77: 
    case 78: 
    case 79: 
    case 81: 
    case 82: 
    case 84: 
    case 87: 
    case 101: 
    case 102: 
    case 112: 
    case 119: 
    case 121: 
    case 122: 
    case 123: 
    case 129: 
    case 131: 
    case 132: 
    case 133: 
    case 136: 
    case 162: 
      this.parseList = doubleParamList;
      break;
    case 113: 
    case 137: 
    case 157: 
    case 163: 
    case 167: 
    case 168: 
    case 173: 
    case 174: 
      this.parseList = optionalDoubleParamList;
      break;
    case 96: 
      this.parseList = new short[] { 836, 838, 824, 838, 902, 2, 824, 838, 822 };
      break;
    case 93: 
    case 94: 
      this.parseList = doubleParamList;
      break;
    case 95: 
    case 120: 
    case 142: 
    case 172: 
      this.parseList = tripleParamList;
      break;
    case 1: 
    case 117: 
    case 130: 
    case 134: 
    case 139: 
      this.parseList = new short[] { 836, 838, 824, 838, 902, 2, 824, 838, 822 };
      break;
    case 175: 
    case 176: 
    case 177: 
      this.parseList = optionalSingleParamList;
      break;
    case 5: 
      this.name = "EXTRACT";
      this.parseList = singleParamList;
      break;
    case 31: 
      this.name = "TRIM";
      this.parseList = singleParamList;
      break;
    case 32: 
      this.name = "OVERLAY";
      this.parseList = quadParamList;
      break;
    case 104: 
      this.name = "IDENTITY";
      this.parseList = emptyParamList;
      break;
    case 100: 
      this.parseList = new short[] { 836, 517, 822 };
      break;
    case 125: 
      this.parseList = new short[] { 836, 838, 140, 838, 902, 2, 124, 838, 822 };
      break;
    case 150: 
      this.parseList = new short[] { 836, 838, 902, 4, 901, 2, 359, 410, 902, 5, 473, 901, 2, 423, 452, 822 };
      break;
    case 159: 
      this.name = "TIMESTAMPADD";
      this.parseList = new short[] { 836, 901, 10, 881, 882, 883, 884, 885, 886, 887, 888, 889, 890, 824, 838, 824, 838, 822 };
      break;
    case 160: 
      this.name = "TIMESTAMPDIFF";
      this.parseList = new short[] { 836, 901, 10, 881, 882, 883, 884, 885, 886, 887, 888, 889, 890, 824, 838, 824, 838, 822 };
      break;
    case 127: 
      this.parseList = optionalSingleParamList;
      break;
    case 2: 
    case 3: 
    case 4: 
    case 6: 
    case 7: 
    case 8: 
    case 9: 
    case 10: 
    case 11: 
    case 12: 
    case 13: 
    case 14: 
    case 15: 
    case 16: 
    case 17: 
    case 18: 
    case 19: 
    case 20: 
    case 21: 
    case 22: 
    case 23: 
    case 24: 
    case 25: 
    case 26: 
    case 27: 
    case 28: 
    case 29: 
    case 30: 
    case 33: 
    case 34: 
    case 35: 
    case 36: 
    case 37: 
    case 38: 
    case 39: 
    case 40: 
    case 41: 
    case 42: 
    case 43: 
    case 44: 
    case 45: 
    case 46: 
    case 47: 
    case 48: 
    case 49: 
    case 50: 
    case 51: 
    case 52: 
    case 53: 
    case 54: 
    case 55: 
    case 56: 
    case 57: 
    case 58: 
    case 59: 
    case 60: 
    case 61: 
    case 62: 
    case 63: 
    case 64: 
    case 65: 
    case 66: 
    case 67: 
    case 68: 
    case 69: 
    case 70: 
    case 105: 
    case 115: 
    case 118: 
    case 140: 
    case 141: 
    case 152: 
    case 178: 
    case 179: 
    case 180: 
    case 181: 
    default: 
      throw Error.runtimeError(201, "FunctionCustom");
    }
  }
  
  public void setArguments(Expression[] paramArrayOfExpression)
  {
    Object localObject;
    switch (this.funcType)
    {
    case 1: 
      localObject = new Expression[4];
      if ("LOCATE".equals(this.name))
      {
        localObject[0] = paramArrayOfExpression[0];
        localObject[1] = paramArrayOfExpression[1];
        localObject[3] = paramArrayOfExpression[2];
        paramArrayOfExpression = (Expression[])localObject;
      }
      else if ("INSTR".equals(this.name))
      {
        localObject[0] = paramArrayOfExpression[1];
        localObject[1] = paramArrayOfExpression[0];
        localObject[3] = paramArrayOfExpression[2];
        paramArrayOfExpression = (Expression[])localObject;
      }
      break;
    case 32: 
      localObject = paramArrayOfExpression[1];
      Expression localExpression = paramArrayOfExpression[2];
      paramArrayOfExpression[1] = paramArrayOfExpression[3];
      paramArrayOfExpression[2] = localObject;
      paramArrayOfExpression[3] = localExpression;
      break;
    case 5: 
      localObject = new Expression[2];
      localObject[0] = new ExpressionValue(ValuePool.getInt(this.extractSpec), Type.SQL_INTEGER);
      localObject[1] = paramArrayOfExpression[0];
      paramArrayOfExpression = (Expression[])localObject;
      break;
    case 31: 
      localObject = new Expression[3];
      localObject[0] = new ExpressionValue(ValuePool.getInt(this.extractSpec), Type.SQL_INTEGER);
      localObject[1] = new ExpressionValue(" ", Type.SQL_CHAR);
      localObject[2] = paramArrayOfExpression[0];
      paramArrayOfExpression = (Expression[])localObject;
    }
    super.setArguments(paramArrayOfExpression);
  }
  
  public Expression getFunctionExpression()
  {
    switch (this.funcType)
    {
    case 84: 
      return new ExpressionArithmetic(36, this.nodes[0], this.nodes[1]);
    }
    return super.getFunctionExpression();
  }
  
  Object getValue(Session paramSession, Object[] paramArrayOfObject)
  {
    Object localObject1;
    Object localObject7;
    Object localObject16;
    Object localObject14;
    int k;
    Object localObject11;
    long l8;
    Object localObject4;
    int m;
    SimpleDateFormat localSimpleDateFormat;
    long l3;
    long l6;
    long l5;
    TimestampData localTimestampData1;
    double d1;
    double d3;
    Object localObject8;
    Object localObject5;
    int i20;
    Object localObject12;
    Object localObject9;
    Object localObject15;
    int i8;
    Object localObject6;
    Object localObject13;
    Object localObject10;
    IntervalSecondData localIntervalSecondData2;
    int i13;
    int i17;
    double d4;
    switch (this.funcType)
    {
    case 182: 
      return Integer.valueOf(0);
    case 183: 
      return "Error";
    case 1: 
    case 5: 
    case 31: 
    case 32: 
      return super.getValue(paramSession, paramArrayOfObject);
    case 88: 
      return paramSession.getDatabase().getPath();
    case 90: 
      return paramSession.getDatabase().getNameString();
    case 106: 
      return paramSession.isAutoCommit() ? Boolean.TRUE : Boolean.FALSE;
    case 110: 
      return paramSession.isReadOnlyDefault() ? Boolean.TRUE : Boolean.FALSE;
    case 108: 
      return paramSession.getDatabase().databaseReadOnly ? Boolean.TRUE : Boolean.FALSE;
    case 109: 
      return paramSession.getDatabase().isFilesReadOnly() ? Boolean.TRUE : Boolean.FALSE;
    case 107: 
      return Session.getIsolationString(paramSession.isolationLevel);
    case 144: 
      return Session.getIsolationString(paramSession.isolationLevelDefault);
    case 89: 
      return Session.getIsolationString(paramSession.database.defaultIsolationLevel);
    case 169: 
      switch (paramSession.database.txManager.getTransactionControl())
      {
      case 2: 
        return "MVCC";
      case 1: 
        return "MVLOCKS";
      }
      return "LOCKS";
    case 161: 
      return new IntervalSecondData(paramSession.getZoneSeconds(), 0);
    case 145: 
      return new IntervalSecondData(paramSession.sessionTimeZoneSeconds, 0);
    case 91: 
      int i = HsqlDateTime.getZoneSeconds(HsqlDateTime.tempCalDefault);
      return new IntervalSecondData(i, 0);
    case 92: 
      return "2.3.4";
    case 143: 
      return Long.valueOf(paramSession.getId());
    case 72: 
      return Long.valueOf(paramSession.actionTimestamp);
    case 170: 
      return Long.valueOf(paramSession.transactionTimestamp);
    case 171: 
      return Long.valueOf(paramSession.actionIndex);
    case 114: 
      localObject1 = (LobData)paramArrayOfObject[0];
      if (localObject1 == null) {
        return null;
      }
      return Long.valueOf(((LobData)localObject1).getId());
    case 104: 
      localObject1 = paramSession.getLastIdentity();
      if ((localObject1 instanceof Long)) {
        return localObject1;
      }
      return ValuePool.getLong(((Number)localObject1).longValue());
    case 100: 
      return paramSession.sessionContext.diagnosticsVariables[this.exprSubType];
    case 142: 
      for (int j = 0; j < paramArrayOfObject.length; j++) {
        if (paramArrayOfObject[j] == null) {
          return null;
        }
      }
      HsqlArrayList localHsqlArrayList = new HsqlArrayList();
      Object localObject3 = paramArrayOfObject[0];
      localObject7 = this.nodes[0].getDataType();
      int i14 = ((Type)localObject7).compare(paramSession, paramArrayOfObject[1], paramArrayOfObject[0]) >= 0 ? 1 : 0;
      for (;;)
      {
        int i18 = ((Type)localObject7).compare(paramSession, localObject3, paramArrayOfObject[1]);
        if (i14 != 0 ? i18 > 0 : i18 < 0) {
          break;
        }
        localHsqlArrayList.add(localObject3);
        localObject16 = ((Type)localObject7).add(paramSession, localObject3, paramArrayOfObject[2], this.nodes[2].getDataType());
        i18 = ((Type)localObject7).compare(paramSession, localObject3, localObject16);
        if (i14 != 0 ? i18 >= 0 : i18 <= 0) {
          break;
        }
        localObject3 = localObject16;
      }
      localObject14 = localHsqlArrayList.toArray();
      return localObject14;
    case 159: 
      if ((paramArrayOfObject[1] == null) || (paramArrayOfObject[2] == null)) {
        return null;
      }
      paramArrayOfObject[1] = Type.SQL_BIGINT.convertToType(paramSession, paramArrayOfObject[1], this.nodes[1].getDataType());
      k = ((Number)this.nodes[0].valueData).intValue();
      long l2 = ((Number)paramArrayOfObject[1]).longValue();
      localObject11 = (TimestampData)paramArrayOfObject[2];
      int i22;
      switch (k)
      {
      case 881: 
        l8 = l2 / 1000000000L;
        i22 = (int)(l2 % 1000000000L);
        localObject14 = Type.SQL_INTERVAL_SECOND_MAX_FRACTION;
        localObject16 = new IntervalSecondData(l8, i22, (IntervalType)localObject14);
        return this.dataType.add(paramSession, localObject11, localObject16, (Type)localObject14);
      case 882: 
        l8 = l2 / 1000L;
        i22 = (int)(l2 % 1000L) * 1000000;
        localObject14 = Type.SQL_INTERVAL_SECOND_MAX_FRACTION;
        localObject16 = new IntervalSecondData(l8, i22, (IntervalType)localObject14);
        return this.dataType.add(paramSession, localObject11, localObject16, (Type)localObject14);
      case 883: 
        localObject14 = Type.SQL_INTERVAL_SECOND_MAX_PRECISION;
        localObject16 = IntervalSecondData.newIntervalSeconds(l2, (IntervalType)localObject14);
        return this.dataType.add(paramSession, localObject11, localObject16, (Type)localObject14);
      case 884: 
        localObject14 = Type.SQL_INTERVAL_MINUTE_MAX_PRECISION;
        localObject16 = IntervalSecondData.newIntervalMinute(l2, (IntervalType)localObject14);
        return this.dataType.add(paramSession, localObject11, localObject16, (Type)localObject14);
      case 885: 
        localObject14 = Type.SQL_INTERVAL_HOUR_MAX_PRECISION;
        localObject16 = IntervalSecondData.newIntervalHour(l2, (IntervalType)localObject14);
        return this.dataType.add(paramSession, localObject11, localObject16, (Type)localObject14);
      case 886: 
        localObject14 = Type.SQL_INTERVAL_DAY_MAX_PRECISION;
        localObject16 = IntervalSecondData.newIntervalDay(l2, (IntervalType)localObject14);
        return this.dataType.add(paramSession, localObject11, localObject16, (Type)localObject14);
      case 887: 
        localObject14 = Type.SQL_INTERVAL_DAY_MAX_PRECISION;
        localObject16 = IntervalSecondData.newIntervalDay(l2 * 7L, (IntervalType)localObject14);
        return this.dataType.add(paramSession, localObject11, localObject16, (Type)localObject14);
      case 888: 
        localObject14 = Type.SQL_INTERVAL_MONTH_MAX_PRECISION;
        localObject16 = IntervalMonthData.newIntervalMonth(l2, (IntervalType)localObject14);
        return this.dataType.add(paramSession, localObject11, localObject16, (Type)localObject14);
      case 889: 
        localObject14 = Type.SQL_INTERVAL_MONTH_MAX_PRECISION;
        localObject16 = IntervalMonthData.newIntervalMonth(l2 * 3L, (IntervalType)localObject14);
        return this.dataType.add(paramSession, localObject11, localObject16, (Type)localObject14);
      case 890: 
        localObject14 = Type.SQL_INTERVAL_YEAR_MAX_PRECISION;
        localObject16 = IntervalMonthData.newIntervalMonth(l2 * 12L, (IntervalType)localObject14);
        return this.dataType.add(paramSession, localObject11, localObject16, (Type)localObject14);
      }
      throw Error.runtimeError(201, "FunctionCustom");
    case 160: 
      if ((paramArrayOfObject[1] == null) || (paramArrayOfObject[2] == null)) {
        return null;
      }
      k = ((Number)this.nodes[0].valueData).intValue();
      localObject4 = (TimestampData)paramArrayOfObject[2];
      localObject7 = (TimestampData)paramArrayOfObject[1];
      if (this.nodes[2].dataType.isDateTimeTypeWithZone()) {
        localObject4 = (TimestampData)Type.SQL_TIMESTAMP.convertToType(paramSession, localObject4, Type.SQL_TIMESTAMP_WITH_TIME_ZONE);
      }
      if (this.nodes[1].dataType.isDateTimeTypeWithZone()) {
        localObject7 = (TimestampData)Type.SQL_TIMESTAMP.convertToType(paramSession, localObject7, Type.SQL_TIMESTAMP_WITH_TIME_ZONE);
      }
      switch (k)
      {
      case 881: 
        localObject11 = Type.SQL_INTERVAL_SECOND_MAX_PRECISION;
        localObject14 = (IntervalSecondData)((IntervalType)localObject11).subtract(paramSession, localObject4, localObject7, null);
        return new Long(1000000000L * ((IntervalSecondData)localObject14).getSeconds() + ((IntervalSecondData)localObject14).getNanos());
      case 882: 
        localObject11 = Type.SQL_INTERVAL_SECOND_MAX_PRECISION;
        localObject14 = (IntervalSecondData)((IntervalType)localObject11).subtract(paramSession, localObject4, localObject7, null);
        return new Long(1000L * ((IntervalSecondData)localObject14).getSeconds() + ((IntervalSecondData)localObject14).getNanos() / 1000000);
      case 883: 
        localObject11 = Type.SQL_INTERVAL_SECOND_MAX_PRECISION;
        return new Long(((IntervalType)localObject11).convertToLongEndUnits(((IntervalType)localObject11).subtract(paramSession, localObject4, localObject7, null)));
      case 884: 
        localObject11 = Type.SQL_INTERVAL_MINUTE_MAX_PRECISION;
        return new Long(((IntervalType)localObject11).convertToLongEndUnits(((IntervalType)localObject11).subtract(paramSession, localObject4, localObject7, null)));
      case 885: 
        localObject11 = Type.SQL_INTERVAL_HOUR_MAX_PRECISION;
        return new Long(((IntervalType)localObject11).convertToLongEndUnits(((IntervalType)localObject11).subtract(paramSession, localObject4, localObject7, null)));
      case 886: 
        localObject11 = Type.SQL_INTERVAL_DAY_MAX_PRECISION;
        return new Long(((IntervalType)localObject11).convertToLongEndUnits(((IntervalType)localObject11).subtract(paramSession, localObject4, localObject7, null)));
      case 887: 
        localObject11 = Type.SQL_INTERVAL_DAY_MAX_PRECISION;
        return new Long(((IntervalType)localObject11).convertToLongEndUnits(((IntervalType)localObject11).subtract(paramSession, localObject4, localObject7, null)) / 7L);
      case 888: 
        localObject11 = Type.SQL_INTERVAL_MONTH_MAX_PRECISION;
        return new Long(((IntervalType)localObject11).convertToLongEndUnits(((IntervalType)localObject11).subtract(paramSession, localObject4, localObject7, null)));
      case 889: 
        localObject11 = Type.SQL_INTERVAL_MONTH_MAX_PRECISION;
        return new Long(((IntervalType)localObject11).convertToLongEndUnits(((IntervalType)localObject11).subtract(paramSession, localObject4, localObject7, null)) / 3L);
      case 890: 
        localObject11 = Type.SQL_INTERVAL_YEAR_MAX_PRECISION;
        return new Long(((IntervalType)localObject11).convertToLongEndUnits(((IntervalType)localObject11).subtract(paramSession, localObject4, localObject7, null)));
      }
      throw Error.runtimeError(201, "FunctionCustom");
    case 93: 
      if ((paramArrayOfObject[0] == null) || (paramArrayOfObject[1] == null)) {
        return null;
      }
      return this.dataType.add(paramSession, paramArrayOfObject[0], paramArrayOfObject[1], this.nodes[1].dataType);
    case 94: 
      if ((paramArrayOfObject[0] == null) || (paramArrayOfObject[1] == null)) {
        return null;
      }
      return this.dataType.subtract(paramSession, paramArrayOfObject[0], paramArrayOfObject[1], this.nodes[1].dataType);
    case 97: 
      if (paramArrayOfObject[0] == null) {
        return null;
      }
      IntervalSecondData localIntervalSecondData1 = (IntervalSecondData)Type.SQL_INTERVAL_DAY_MAX_PRECISION.subtract(paramSession, paramArrayOfObject[0], DateTimeType.epochTimestamp, Type.SQL_DATE);
      return ValuePool.getInt((int)(localIntervalSecondData1.getSeconds() / 86400L + 1L));
    case 137: 
    case 173: 
      m = 103;
      if (paramArrayOfObject[0] == null) {
        return null;
      }
      if (this.dataType.isDateTimeType())
      {
        localObject4 = (DateTimeType)this.dataType;
        if ((this.nodes.length > 1) && (this.nodes[1] != null))
        {
          if (paramArrayOfObject[1] == null) {
            return null;
          }
          m = HsqlDateTime.toStandardIntervalPart((String)paramArrayOfObject[1]);
        }
        if (m < 0) {
          throw Error.error(5566, (String)paramArrayOfObject[1]);
        }
        return this.funcType == 137 ? ((DateTimeType)localObject4).round(paramArrayOfObject[0], m) : ((DateTimeType)localObject4).truncate(paramArrayOfObject[0], m);
      }
    case 174: 
      m = 0;
      if (paramArrayOfObject[0] == null) {
        return null;
      }
      if (this.nodes.length > 1)
      {
        if (paramArrayOfObject[1] == null) {
          return null;
        }
        paramArrayOfObject[1] = Type.SQL_INTEGER.convertToType(paramSession, paramArrayOfObject[1], this.nodes[1].getDataType());
        m = ((Number)paramArrayOfObject[1]).intValue();
      }
      return this.funcType == 137 ? ((NumberType)this.dataType).round(paramArrayOfObject[0], m) : ((NumberType)this.dataType).truncate(paramArrayOfObject[0], m);
    case 162: 
      if ((paramArrayOfObject[0] == null) || (paramArrayOfObject[1] == null)) {
        return null;
      }
      localSimpleDateFormat = paramSession.getSimpleDateFormatGMT();
      localObject4 = (Date)((DateTimeType)this.nodes[0].dataType).convertSQLToJavaGMT(paramSession, paramArrayOfObject[0]);
      return HsqlDateTime.toFormattedDate((Date)localObject4, (String)paramArrayOfObject[1], localSimpleDateFormat);
    case 166: 
      if (paramArrayOfObject[0] == null) {
        return null;
      }
      return this.dataType.convertToType(paramSession, paramArrayOfObject[0], this.nodes[0].dataType);
    case 163: 
    case 167: 
      if ((paramArrayOfObject[0] == null) || (paramArrayOfObject[1] == null)) {
        return null;
      }
      localSimpleDateFormat = paramSession.getSimpleDateFormatGMT();
      localObject4 = HsqlDateTime.toDate((String)paramArrayOfObject[0], (String)paramArrayOfObject[1], localSimpleDateFormat);
      if (this.funcType == 163) {
        ((TimestampData)localObject4).clearNanos();
      }
      return localObject4;
    case 157: 
      int n = this.nodes[1] == null ? 1 : 0;
      if (paramArrayOfObject[0] == null) {
        return null;
      }
      if (n != 0)
      {
        if (this.nodes[0].dataType.isNumberType()) {
          return new TimestampData(((Number)paramArrayOfObject[0]).longValue());
        }
        try
        {
          return Type.SQL_TIMESTAMP.convertToType(paramSession, paramArrayOfObject[0], this.nodes[0].dataType);
        }
        catch (HsqlException localHsqlException)
        {
          return Type.SQL_DATE.convertToType(paramSession, paramArrayOfObject[0], this.nodes[0].dataType);
        }
      }
      if (paramArrayOfObject[1] == null) {
        return null;
      }
      TimestampData localTimestampData2 = (TimestampData)Type.SQL_DATE.convertToType(paramSession, paramArrayOfObject[0], this.nodes[0].dataType);
      localObject7 = (TimeData)Type.SQL_TIME.convertToType(paramSession, paramArrayOfObject[1], this.nodes[1].dataType);
      return new TimestampData(localTimestampData2.getSeconds() + ((TimeData)localObject7).getSeconds(), ((TimeData)localObject7).getNanos());
    case 158: 
      Calendar localCalendar = paramSession.getCalendar();
      int i15 = 0;
      if (paramArrayOfObject[0] == null) {
        return null;
      }
      if (this.nodes[0].dataType.isNumberType())
      {
        l3 = ((Number)paramArrayOfObject[0]).longValue();
      }
      else if (this.nodes[0].dataType.typeCode == 93)
      {
        l3 = ((TimestampData)paramArrayOfObject[0]).getSeconds();
        l3 = HsqlDateTime.convertMillisToCalendar(localCalendar, l3 * 1000L) / 1000L;
      }
      else if (this.nodes[0].dataType.typeCode == 95)
      {
        l3 = ((TimestampData)paramArrayOfObject[0]).getSeconds();
      }
      else
      {
        throw Error.error(5566, (String)paramArrayOfObject[1]);
      }
      synchronized (localCalendar)
      {
        localCalendar.setTimeInMillis(l3 * 1000L);
        l6 = HsqlDateTime.getZoneSeconds(localCalendar);
      }
      return new TimestampData(l3, i15, l6);
    case 124: 
      return new Double(3.141592653589793D);
    case 127: 
      if (this.nodes[0] == null) {
        return new Double(paramSession.random());
      }
      paramArrayOfObject[0] = Type.SQL_BIGINT.convertToType(paramSession, paramArrayOfObject[0], this.nodes[0].getDataType());
      long l1 = ((Number)paramArrayOfObject[0]).longValue();
      return new Double(paramSession.random(l1));
    case 175: 
      Object localObject2;
      if (this.nodes[0] == null)
      {
        localObject2 = UUID.randomUUID();
        l3 = ((UUID)localObject2).getMostSignificantBits();
        l5 = ((UUID)localObject2).getLeastSignificantBits();
        return new BinaryData(ArrayUtil.toByteArray(l3, l5), false);
      }
      if (paramArrayOfObject[0] == null) {
        return null;
      }
      try
      {
        if (this.dataType.isBinaryType())
        {
          localObject2 = StringConverter.toBinaryUUID((String)paramArrayOfObject[0]);
          return new BinaryData((byte[])localObject2, false);
        }
        return StringConverter.toStringUUID(((BinaryData)paramArrayOfObject[0]).getBytes());
      }
      catch (NumberFormatException localNumberFormatException)
      {
        throw Error.error(3459);
      }
    case 177: 
      if (this.nodes[0] == null)
      {
        localTimestampData1 = paramSession.getCurrentTimestamp(true);
      }
      else
      {
        if (paramArrayOfObject[0] == null) {
          return null;
        }
        localTimestampData1 = (TimestampData)paramArrayOfObject[0];
      }
      l3 = localTimestampData1.getSeconds() * 1000L + localTimestampData1.getNanos() / 1000000;
      return Long.valueOf(l3);
    case 176: 
      if (this.nodes[0] == null)
      {
        localTimestampData1 = paramSession.getCurrentTimestamp(true);
      }
      else
      {
        if (paramArrayOfObject[0] == null) {
          return null;
        }
        localTimestampData1 = (TimestampData)paramArrayOfObject[0];
      }
      return Long.valueOf(localTimestampData1.getSeconds());
    case 71: 
      if (paramArrayOfObject[0] == null) {
        return null;
      }
      d1 = NumberType.toDouble(paramArrayOfObject[0]);
      return new Double(Math.acos(d1));
    case 75: 
      if (paramArrayOfObject[0] == null) {
        return null;
      }
      d1 = NumberType.toDouble(paramArrayOfObject[0]);
      return new Double(Math.asin(d1));
    case 76: 
      if (paramArrayOfObject[0] == null) {
        return null;
      }
      d1 = NumberType.toDouble(paramArrayOfObject[0]);
      return new Double(Math.atan(d1));
    case 85: 
      if (paramArrayOfObject[0] == null) {
        return null;
      }
      d1 = NumberType.toDouble(paramArrayOfObject[0]);
      return new Double(Math.cos(d1));
    case 86: 
      if (paramArrayOfObject[0] == null) {
        return null;
      }
      d1 = NumberType.toDouble(paramArrayOfObject[0]);
      d3 = 1.0D / Math.tan(d1);
      return new Double(d3);
    case 99: 
      if (paramArrayOfObject[0] == null) {
        return null;
      }
      d1 = NumberType.toDouble(paramArrayOfObject[0]);
      return new Double(Math.toDegrees(d1));
    case 148: 
      if (paramArrayOfObject[0] == null) {
        return null;
      }
      d1 = NumberType.toDouble(paramArrayOfObject[0]);
      return new Double(Math.sin(d1));
    case 156: 
      if (paramArrayOfObject[0] == null) {
        return null;
      }
      d1 = NumberType.toDouble(paramArrayOfObject[0]);
      return new Double(Math.tan(d1));
    case 116: 
      if (paramArrayOfObject[0] == null) {
        return null;
      }
      d1 = NumberType.toDouble(paramArrayOfObject[0]);
      return new Double(Math.log10(d1));
    case 126: 
      if (paramArrayOfObject[0] == null) {
        return null;
      }
      d1 = NumberType.toDouble(paramArrayOfObject[0]);
      return new Double(Math.toRadians(d1));
    case 147: 
      if (paramArrayOfObject[0] == null) {
        return null;
      }
      int i1 = ((NumberType)this.nodes[0].dataType).compareToZero(paramArrayOfObject[0]);
      return ValuePool.getInt(i1);
    case 77: 
      if (paramArrayOfObject[0] == null) {
        return null;
      }
      double d2 = NumberType.toDouble(paramArrayOfObject[0]);
      d3 = NumberType.toDouble(paramArrayOfObject[1]);
      return new Double(Math.atan2(d2, d3));
    case 74: 
      if (paramArrayOfObject[0] == null) {
        return null;
      }
      String str1;
      if (this.nodes[0].dataType.isLobType()) {
        str1 = ((ClobData)paramArrayOfObject[0]).getSubString(paramSession, 0L, 1);
      } else {
        str1 = (String)paramArrayOfObject[0];
      }
      if (str1.length() == 0) {
        return null;
      }
      return ValuePool.getInt(str1.charAt(0));
    case 83: 
      if (paramArrayOfObject[0] == null) {
        return null;
      }
      paramArrayOfObject[0] = Type.SQL_INTEGER.convertToType(paramSession, paramArrayOfObject[0], this.nodes[0].getDataType());
      int i2 = ((Number)paramArrayOfObject[0]).intValue();
      if ((Character.isValidCodePoint(i2)) && (Character.isValidCodePoint((char)i2))) {
        return String.valueOf((char)i2);
      }
      throw Error.error(3472);
    case 138: 
      int i3 = 0;
      if (paramArrayOfObject[0] == null) {
        return null;
      }
      if (this.nodes.length > 1)
      {
        if (paramArrayOfObject[1] == null) {
          return null;
        }
        i3 = ((Number)paramArrayOfObject[1]).intValue();
      }
      return ((NumberType)this.dataType).round(paramArrayOfObject[0], i3);
    case 149: 
      if (paramArrayOfObject[0] == null) {
        return null;
      }
      String str2 = (String)paramArrayOfObject[0];
      return new String(soundex(str2), 0, 4);
    case 78: 
    case 79: 
    case 80: 
    case 81: 
    case 82: 
      for (int i4 = 0; i4 < paramArrayOfObject.length; i4++) {
        if (paramArrayOfObject[i4] == null) {
          return null;
        }
      }
      if (this.dataType.isNumberType())
      {
        long l4 = 0L;
        long l7 = 0L;
        paramArrayOfObject[0] = Type.SQL_BIGINT.convertToType(paramSession, paramArrayOfObject[0], this.nodes[0].getDataType());
        l5 = ((Number)paramArrayOfObject[0]).longValue();
        if (this.funcType != 80)
        {
          paramArrayOfObject[1] = Type.SQL_BIGINT.convertToType(paramSession, paramArrayOfObject[1], this.nodes[1].getDataType());
          l7 = ((Number)paramArrayOfObject[1]).longValue();
        }
        switch (this.funcType)
        {
        case 78: 
          l4 = l5 & l7;
          break;
        case 79: 
          l4 = l5 & (l7 ^ 0xFFFFFFFFFFFFFFFF);
          break;
        case 80: 
          l4 = l5 ^ 0xFFFFFFFFFFFFFFFF;
          break;
        case 81: 
          l4 = l5 | l7;
          break;
        case 82: 
          l4 = l5 ^ l7;
          break;
        }
        switch (this.dataType.typeCode)
        {
        case 2: 
        case 3: 
          return BigDecimal.valueOf(l4);
        case 25: 
          return ValuePool.getLong(l4);
        case -6: 
        case 4: 
        case 5: 
          return ValuePool.getInt((int)l4);
        }
        throw Error.error(5561);
      }
      byte[] arrayOfByte1 = ((BinaryData)paramArrayOfObject[0]).getBytes();
      localObject8 = null;
      if (this.funcType != 80) {
        localObject8 = ((BinaryData)paramArrayOfObject[1]).getBytes();
      }
      byte[] arrayOfByte2;
      switch (this.funcType)
      {
      case 78: 
        arrayOfByte2 = BitMap.and(arrayOfByte1, (byte[])localObject8);
        break;
      case 79: 
        localObject8 = BitMap.not((byte[])localObject8);
        arrayOfByte2 = BitMap.and(arrayOfByte1, (byte[])localObject8);
        break;
      case 80: 
        arrayOfByte2 = BitMap.not(arrayOfByte1);
        break;
      case 81: 
        arrayOfByte2 = BitMap.or(arrayOfByte1, (byte[])localObject8);
        break;
      case 82: 
        arrayOfByte2 = BitMap.xor(arrayOfByte1, (byte[])localObject8);
        break;
      default: 
        throw Error.error(5561);
      }
      return new BinaryData(arrayOfByte2, this.dataType.precision);
    case 101: 
      for (int i5 = 0; i5 < paramArrayOfObject.length; i5++) {
        if (paramArrayOfObject[i5] == null) {
          return null;
        }
      }
      localObject5 = soundex((String)paramArrayOfObject[0]);
      localObject8 = soundex((String)paramArrayOfObject[1]);
      int i16 = 0;
      if (localObject5[0] == localObject8[0]) {
        i16++;
      }
      l6 = 1;
      for (i20 = 1; i20 < 4; i20++) {
        for (l8 = l6; l8 < 4; l8++) {
          if (localObject5[l8] == localObject8[i20])
          {
            i16++;
            l6 = l8 + 1;
            break;
          }
        }
      }
      return ValuePool.getInt(i16);
    case 103: 
      if (paramArrayOfObject[0] == null) {
        return null;
      }
      return this.dataType.convertToType(paramSession, paramArrayOfObject[0], this.nodes[0].dataType);
    case 128: 
      if (paramArrayOfObject[0] == null) {
        return null;
      }
      localObject5 = (BlobData)paramArrayOfObject[0];
      localObject8 = ((BlobData)localObject5).getBytes(paramSession, 0L, (int)((BlobData)localObject5).length(paramSession));
      return StringConverter.byteArrayToHexString((byte[])localObject8);
    case 133: 
      for (int i6 = 0; i6 < paramArrayOfObject.length; i6++) {
        if (paramArrayOfObject[i6] == null) {
          return null;
        }
      }
      paramArrayOfObject[1] = Type.SQL_INTEGER.convertToType(paramSession, paramArrayOfObject[1], this.nodes[1].getDataType());
      String str3 = (String)paramArrayOfObject[0];
      int i10 = ((Number)paramArrayOfObject[1]).intValue();
      localObject12 = new StringBuffer(str3.length() * i10);
      while (i10-- > 0) {
        ((StringBuffer)localObject12).append(str3);
      }
      return ((StringBuffer)localObject12).toString();
    case 134: 
      for (int i7 = 0; i7 < paramArrayOfObject.length; i7++) {
        if (paramArrayOfObject[i7] == null) {
          return null;
        }
      }
      String str4 = (String)paramArrayOfObject[0];
      localObject9 = (String)paramArrayOfObject[1];
      localObject12 = (String)paramArrayOfObject[2];
      localObject15 = new StringBuffer();
      i20 = 0;
      if (((String)localObject9).length() == 0) {
        return str4;
      }
      for (;;)
      {
        int i21 = str4.indexOf((String)localObject9, i20);
        if (i21 == -1)
        {
          ((StringBuffer)localObject15).append(str4.substring(i20));
          break;
        }
        ((StringBuffer)localObject15).append(str4.substring(i20, i21));
        ((StringBuffer)localObject15).append((String)localObject12);
        i20 = i21 + ((String)localObject9).length();
      }
      return ((StringBuffer)localObject15).toString();
    case 112: 
    case 136: 
      for (i8 = 0; i8 < paramArrayOfObject.length; i8++) {
        if (paramArrayOfObject[i8] == null) {
          return null;
        }
      }
      i8 = ((Number)paramArrayOfObject[1]).intValue();
      return ((CharacterType)this.dataType).substring(paramSession, paramArrayOfObject[0], 0L, i8, true, this.funcType == 136);
    case 151: 
      if (paramArrayOfObject[0] == null) {
        return null;
      }
      paramArrayOfObject[0] = Type.SQL_INTEGER.convertToType(paramSession, paramArrayOfObject[0], this.nodes[0].getDataType());
      i8 = ((Number)paramArrayOfObject[0]).intValue();
      localObject9 = new char[i8];
      ArrayUtil.fillArray((char[])localObject9, 0, ' ');
      return String.valueOf((char[])localObject9);
    case 135: 
      if (paramArrayOfObject[0] == null) {
        return null;
      }
      StringBuffer localStringBuffer = new StringBuffer((String)paramArrayOfObject[0]);
      localStringBuffer = localStringBuffer.reverse();
      return localStringBuffer.toString();
    case 129: 
    case 130: 
    case 131: 
    case 132: 
      for (int i9 = 0; i9 < paramArrayOfObject.length; i9++) {
        if (paramArrayOfObject[i9] == null) {
          return null;
        }
      }
      localObject6 = this.pattern;
      if (localObject6 == null)
      {
        localObject9 = (String)paramArrayOfObject[1];
        localObject6 = Pattern.compile((String)localObject9);
      }
      localObject9 = ((Pattern)localObject6).matcher((String)paramArrayOfObject[0]);
      switch (this.funcType)
      {
      case 129: 
        boolean bool1 = ((Matcher)localObject9).matches();
        return Boolean.valueOf(bool1);
      case 130: 
        String str5 = (String)paramArrayOfObject[2];
        localObject15 = ((Matcher)localObject9).replaceAll(str5);
        return localObject15;
      case 131: 
        boolean bool2 = ((Matcher)localObject9).find();
        if (bool2) {
          return ((Matcher)localObject9).group();
        }
        return null;
      case 132: 
        localObject13 = new HsqlArrayList();
        while (((Matcher)localObject9).find()) {
          ((HsqlArrayList)localObject13).add(((Matcher)localObject9).group());
        }
        return ((HsqlArrayList)localObject13).toArray();
      }
    case 87: 
      localObject6 = Crypto.getNewKey((String)paramArrayOfObject[0], (String)paramArrayOfObject[1]);
      return StringConverter.byteArrayToHexString((byte[])localObject6);
    case 113: 
      localObject6 = (String)paramArrayOfObject[0];
      if (localObject6 == null) {
        return null;
      }
      switch (this.dataType.typeCode)
      {
      case 40: 
        return paramSession.sessionData.createClobFromFile((String)localObject6, (String)paramArrayOfObject[1]);
      }
      return paramSession.sessionData.createBlobFromFile((String)localObject6);
    case 117: 
    case 139: 
      if ((paramArrayOfObject[0] == null) || (paramArrayOfObject[1] == null)) {
        return null;
      }
      if (this.nodes[0].dataType.typeCode == 40) {
        localObject6 = (String)Type.SQL_VARCHAR.convertToType(paramSession, paramArrayOfObject[0], this.nodes[0].dataType);
      } else if (this.nodes[0].dataType.isCharacterType()) {
        localObject6 = (String)paramArrayOfObject[0];
      } else {
        localObject6 = this.nodes[0].dataType.convertToString(paramArrayOfObject[0]);
      }
      int i11 = ((Integer)Type.SQL_INTEGER.convertToType(paramSession, paramArrayOfObject[1], this.nodes[1].dataType)).intValue();
      localObject13 = " ";
      if (this.nodes[2] != null)
      {
        localObject13 = this.nodes[2].dataType.convertToString(paramArrayOfObject[2]);
        if (((String)localObject13).length() == 0) {
          localObject13 = " ";
        }
      }
      localObject6 = (String)Type.SQL_VARCHAR.trim(paramSession, localObject6, ' ', true, true);
      localObject6 = StringUtil.toPaddedString((String)localObject6, i11, (String)localObject13, this.funcType == 139);
      if (this.dataType.isLobType()) {
        return this.dataType.convertToType(paramSession, localObject6, Type.SQL_VARCHAR);
      }
      return localObject6;
    case 125: 
      if (paramArrayOfObject[1] == null) {
        return null;
      }
      if (paramArrayOfObject[2] == null) {
        return null;
      }
      localObject6 = (Object[])paramArrayOfObject[1];
      localObject10 = (ArrayType)this.nodes[1].dataType;
      localObject13 = ((ArrayType)localObject10).collectionBaseType();
      int i19 = ((Number)Type.SQL_INTEGER.convertToType(paramSession, paramArrayOfObject[2], this.nodes[2].dataType)).intValue();
      if (i19 <= 0) {
        throw Error.error(3403);
      }
      i19--;
      for (i20 = i19; i20 < localObject6.length; i20++) {
        if (((Type)localObject13).compare(paramSession, paramArrayOfObject[0], localObject6[i20]) == 0) {
          return ValuePool.getInt(i20 + 1);
        }
      }
      return ValuePool.INTEGER_0;
    case 150: 
      if (paramArrayOfObject[0] == null) {
        return null;
      }
      localObject6 = (ArrayType)this.dataType;
      localObject10 = new SortAndSlice();
      ((SortAndSlice)localObject10).prepareSingleColumn(1);
      ((SortAndSlice)localObject10).sortDescending[0] = (((Number)paramArrayOfObject[1]).intValue() == 410 ? 1 : false);
      ((SortAndSlice)localObject10).sortNullsLast[0] = (((Number)paramArrayOfObject[2]).intValue() == 452 ? 1 : false);
      localObject13 = ArrayUtil.duplicateArray(paramArrayOfObject[0]);
      ((ArrayType)localObject6).sort(paramSession, localObject13, (SortAndSlice)localObject10);
      return localObject13;
    case 73: 
      if (paramArrayOfObject[0] == null) {
        return null;
      }
      if (paramArrayOfObject[1] == null) {
        return null;
      }
      localObject6 = (TimestampData)paramArrayOfObject[0];
      int i12 = ((Number)paramArrayOfObject[1]).intValue();
      return Type.SQL_TIMESTAMP_NO_FRACTION.addMonthsSpecial(paramSession, localObject6, i12);
    case 98: 
      localObject6 = paramSession.getSystemTimestamp(true);
      localIntervalSecondData2 = new IntervalSecondData(((TimestampData)localObject6).getZone(), 0);
      return Type.SQL_INTERVAL_HOUR_TO_MINUTE.convertToString(localIntervalSecondData2);
    case 102: 
      if ((paramArrayOfObject[0] == null) || (paramArrayOfObject[1] == null)) {
        return null;
      }
      localObject6 = (TimestampData)paramArrayOfObject[0];
      localIntervalSecondData2 = (IntervalSecondData)Type.SQL_INTERVAL_HOUR_TO_MINUTE.convertToDefaultType(paramSession, paramArrayOfObject[1]);
      return new TimestampData(((TimestampData)localObject6).getSeconds() - localIntervalSecondData2.getSeconds(), ((TimestampData)localObject6).getNanos(), (int)localIntervalSecondData2.getSeconds());
    case 111: 
      if (paramArrayOfObject[0] == null) {
        return null;
      }
      return Type.SQL_TIMESTAMP_NO_FRACTION.getLastDayOfMonth(paramSession, paramArrayOfObject[0]);
    case 119: 
      if ((paramArrayOfObject[0] == null) || (paramArrayOfObject[1] == null)) {
        return null;
      }
      return DateTimeType.subtractMonthsSpecial(paramSession, (TimestampData)paramArrayOfObject[0], (TimestampData)paramArrayOfObject[1]);
    case 120: 
      if ((paramArrayOfObject[0] == null) || (paramArrayOfObject[1] == null) || (paramArrayOfObject[2] == null)) {
        return null;
      }
      localObject6 = (IntervalSecondData)Type.SQL_INTERVAL_HOUR_TO_MINUTE.convertToDefaultType(paramSession, paramArrayOfObject[1]);
      localIntervalSecondData2 = (IntervalSecondData)Type.SQL_INTERVAL_HOUR_TO_MINUTE.convertToDefaultType(paramSession, paramArrayOfObject[1]);
      localObject13 = Type.SQL_TIMESTAMP_WITH_TIME_ZONE.changeZone(paramArrayOfObject[0], Type.SQL_TIMESTAMP, (int)localIntervalSecondData2.getSeconds(), (int)((IntervalSecondData)localObject6).getSeconds());
      return Type.SQL_TIMESTAMP.convertToType(paramSession, localObject13, Type.SQL_TIMESTAMP_WITH_TIME_ZONE);
    case 121: 
      if ((paramArrayOfObject[0] == null) || (paramArrayOfObject[1] == null)) {
        return null;
      }
    case 122: 
      if ((paramArrayOfObject[0] == null) || (paramArrayOfObject[1] == null)) {
        return null;
      }
      localObject6 = Type.SQL_VARCHAR.trim(paramSession, paramArrayOfObject[1], ' ', true, true);
      localObject6 = Type.SQL_VARCHAR.upper(paramSession, localObject6);
      localObject6 = Type.SQL_VARCHAR.convertToDefaultType(paramSession, localObject6);
      i13 = Tokens.get((String)localObject6);
      i17 = IntervalType.getFieldNameTypeForToken(i13);
      switch (i17)
      {
      case 103: 
      case 104: 
      case 105: 
      case 106: 
        break;
      default: 
        throw Error.error(5566);
      }
      d4 = ((Number)paramArrayOfObject[0]).doubleValue();
      return IntervalSecondData.newInterval(d4, i17);
    case 123: 
      if ((paramArrayOfObject[0] == null) || (paramArrayOfObject[1] == null)) {
        return null;
      }
      localObject6 = Type.SQL_VARCHAR.trim(paramSession, paramArrayOfObject[1], ' ', true, true);
      localObject6 = Type.SQL_VARCHAR.upper(paramSession, localObject6);
      localObject6 = Type.SQL_VARCHAR.convertToDefaultType(paramSession, localObject6);
      i13 = Tokens.get((String)localObject6);
      i17 = IntervalType.getFieldNameTypeForToken(i13);
      switch (i17)
      {
      case 101: 
      case 102: 
        break;
      default: 
        throw Error.error(5566);
      }
      d4 = ((Number)paramArrayOfObject[0]).doubleValue();
      return IntervalMonthData.newInterval(d4, i17);
    case 146: 
      localObject6 = new IntervalSecondData(paramSession.sessionTimeZoneSeconds, 0);
      return Type.SQL_INTERVAL_HOUR_TO_MINUTE.convertToString(localObject6);
    case 153: 
      if (paramArrayOfObject[0] == null) {
        return null;
      }
      return Type.SQL_TIMESTAMP_WITH_TIME_ZONE.changeZone(paramArrayOfObject[0], Type.SQL_TIMESTAMP_WITH_TIME_ZONE, 0, 0);
    case 154: 
      localObject6 = paramSession.getSystemTimestamp(false);
      return Type.SQL_TIMESTAMP_NO_FRACTION.convertToType(paramSession, localObject6, Type.SQL_TIMESTAMP);
    case 155: 
      return paramSession.getSystemTimestamp(true);
    case 164: 
      if (paramArrayOfObject[0] == null) {
        return null;
      }
      return Type.SQL_INTERVAL_DAY_TO_SECOND.convertToType(paramSession, paramArrayOfObject[0], Type.SQL_VARCHAR);
    case 165: 
      if (paramArrayOfObject[0] == null) {
        return null;
      }
      return Type.SQL_INTERVAL_YEAR_TO_MONTH_MAX_PRECISION.convertToType(paramSession, paramArrayOfObject[0], Type.SQL_VARCHAR);
    case 168: 
      if ((paramArrayOfObject[0] == null) || (paramArrayOfObject[1] == null)) {
        return null;
      }
    case 172: 
      if ((paramArrayOfObject[0] == null) || (paramArrayOfObject[1] == null) || (paramArrayOfObject[2] == null)) {
        return null;
      }
      localObject6 = this.charLookup;
      if (localObject6 == null) {
        localObject6 = getTranslationMap((String)paramArrayOfObject[1], (String)paramArrayOfObject[2]);
      }
      return translateWithMap((String)paramArrayOfObject[0], (IntKeyIntValueHashMap)localObject6);
    }
    throw Error.runtimeError(201, "FunctionCustom");
  }
  
  public void resolveTypes(Session paramSession, Expression paramExpression)
  {
    for (int i = 0; i < this.nodes.length; i++) {
      if (this.nodes[i] != null) {
        this.nodes[i].resolveTypes(paramSession, this);
      }
    }
    int i1;
    Object localObject1;
    int k;
    Object localObject2;
    switch (this.funcType)
    {
    case 182: 
      this.dataType = Type.SQL_INTEGER;
      return;
    case 183: 
      this.dataType = Type.SQL_VARCHAR_DEFAULT;
      return;
    case 1: 
    case 5: 
    case 31: 
    case 32: 
      super.resolveTypes(paramSession, paramExpression);
      return;
    case 88: 
      this.dataType = Type.SQL_VARCHAR_DEFAULT;
      return;
    case 90: 
      this.dataType = Type.SQL_VARCHAR_DEFAULT;
      return;
    case 106: 
    case 108: 
    case 109: 
    case 110: 
      this.dataType = Type.SQL_BOOLEAN;
      return;
    case 89: 
    case 92: 
    case 107: 
    case 144: 
    case 169: 
      this.dataType = Type.SQL_VARCHAR_DEFAULT;
      return;
    case 91: 
    case 145: 
    case 161: 
      this.dataType = Type.SQL_INTERVAL_HOUR_TO_MINUTE;
      return;
    case 72: 
    case 104: 
    case 114: 
    case 143: 
    case 170: 
    case 171: 
      this.dataType = Type.SQL_BIGINT;
      return;
    case 100: 
      this.exprSubType = 2;
      this.dataType = Type.SQL_INTEGER;
      return;
    case 142: 
      if (this.nodes[0].dataType == null) {
        this.nodes[0].dataType = this.nodes[1].dataType;
      }
      if (this.nodes[1].dataType == null) {
        this.nodes[1].dataType = this.nodes[0].dataType;
      }
      if (this.nodes[0].dataType == null)
      {
        this.nodes[0].dataType = Type.SQL_INTEGER;
        this.nodes[1].dataType = Type.SQL_INTEGER;
      }
      if (this.nodes[0].dataType.isNumberType())
      {
        if (this.nodes[2].dataType == null) {
          this.nodes[2].dataType = this.nodes[0].dataType;
        }
      }
      else if (this.nodes[0].dataType.isDateTimeType())
      {
        if (this.nodes[2].dataType == null) {
          throw Error.error(5561);
        }
        if (!this.nodes[2].dataType.isIntervalType()) {
          throw Error.error(5561);
        }
      }
      this.dataType = new ArrayType(this.nodes[0].getDataType(), Integer.MAX_VALUE);
      return;
    case 95: 
      if (this.nodes[0].dataType == null) {
        throw Error.error(5575);
      }
      if (!this.nodes[0].dataType.isCharacterType()) {
        throw Error.error(5561);
      }
      i = getTSIToken((String)this.nodes[0].valueData);
      this.nodes[0].valueData = ValuePool.getInt(i);
      this.nodes[0].dataType = Type.SQL_INTEGER;
      this.funcType = 159;
    case 159: 
      if (this.nodes[1].dataType == null) {
        this.nodes[1].dataType = Type.SQL_BIGINT;
      }
      if (this.nodes[2].dataType == null) {
        this.nodes[2].dataType = Type.SQL_TIMESTAMP;
      }
      if (!this.nodes[1].dataType.isNumberType()) {
        throw Error.error(5561);
      }
      if ((this.nodes[2].dataType.typeCode != 91) && (this.nodes[2].dataType.typeCode != 93) && (this.nodes[2].dataType.typeCode != 95)) {
        throw Error.error(5561);
      }
      this.dataType = this.nodes[2].dataType;
      return;
    case 96: 
      if (this.nodes[2] == null)
      {
        this.nodes[2] = this.nodes[0];
        this.nodes[0] = new ExpressionValue(ValuePool.getInt(886), Type.SQL_INTEGER);
      }
      else
      {
        if (!this.nodes[0].dataType.isCharacterType()) {
          throw Error.error(5563);
        }
        i = getTSIToken((String)this.nodes[0].valueData);
        this.nodes[0].valueData = ValuePool.getInt(i);
        this.nodes[0].dataType = Type.SQL_INTEGER;
      }
      this.funcType = 160;
    case 160: 
      if (this.nodes[1].dataType == null) {
        this.nodes[1].dataType = this.nodes[2].dataType;
      }
      if (this.nodes[2].dataType == null) {
        this.nodes[2].dataType = this.nodes[1].dataType;
      }
      if (this.nodes[1].dataType == null)
      {
        this.nodes[1].dataType = Type.SQL_TIMESTAMP;
        this.nodes[2].dataType = Type.SQL_TIMESTAMP;
      }
      switch (this.nodes[1].dataType.typeCode)
      {
      case 91: 
        if ((this.nodes[2].dataType.typeCode == 92) || (this.nodes[2].dataType.typeCode == 94)) {
          throw Error.error(5563);
        }
        switch (((Integer)this.nodes[0].valueData).intValue())
        {
        case 886: 
        case 887: 
        case 888: 
        case 889: 
        case 890: 
          break;
        default: 
          throw Error.error(5563);
        }
        break;
      case 93: 
      case 95: 
        if ((this.nodes[2].dataType.typeCode == 92) || (this.nodes[2].dataType.typeCode == 94)) {
          throw Error.error(5563);
        }
        break;
      case 92: 
      case 94: 
      default: 
        throw Error.error(5563);
      }
      this.dataType = Type.SQL_BIGINT;
      return;
    case 93: 
    case 94: 
      if (this.nodes[0].dataType == null) {
        this.nodes[0].dataType = Type.SQL_DATE;
      }
      if (this.nodes[1].dataType == null) {
        this.nodes[1].dataType = Type.SQL_INTEGER;
      }
      if (this.nodes[0].dataType.isCharacterType()) {
        this.nodes[0] = new ExpressionOp(this.nodes[0], Type.SQL_TIMESTAMP);
      }
      if (this.nodes[1].dataType.isIntegralType()) {
        this.nodes[1] = new ExpressionOp(this.nodes[1], Type.SQL_INTERVAL_DAY);
      }
      this.nodes[0].resolveTypes(paramSession, this);
      this.nodes[1].resolveTypes(paramSession, this);
      this.dataType = this.nodes[0].dataType;
      return;
    case 97: 
      if (this.nodes[0].dataType == null) {
        this.nodes[0].dataType = Type.SQL_DATE;
      }
      switch (this.nodes[0].dataType.typeCode)
      {
      case 91: 
      case 93: 
      case 95: 
        break;
      case 92: 
      case 94: 
      default: 
        throw Error.error(5563);
      }
      this.dataType = Type.SQL_INTEGER;
      return;
    case 137: 
    case 173: 
      i = (this.nodes.length == 1) || (this.nodes[1] == null) ? 1 : 0;
      if (this.nodes[0].dataType == null) {
        if (i != 0)
        {
          if (((paramExpression instanceof ExpressionLogical)) || ((paramExpression instanceof ExpressionArithmetic))) {
            for (i1 = 0; i1 < paramExpression.nodes.length; i1++) {
              if (paramExpression.nodes[i1].dataType != null)
              {
                this.nodes[0].dataType = paramExpression.nodes[i1].dataType;
                break;
              }
            }
          }
          if (this.nodes[0].dataType == null) {
            this.nodes[0].dataType = Type.SQL_DECIMAL;
          }
          if (this.nodes[0].dataType.isNumberType()) {
            this.nodes[0].dataType = Type.SQL_DECIMAL;
          }
        }
        else
        {
          if (this.nodes[1].dataType == null) {
            this.nodes[1].dataType = Type.SQL_INTEGER;
          }
          if (this.nodes[1].dataType.isNumberType()) {
            this.nodes[0].dataType = Type.SQL_DECIMAL;
          } else {
            this.nodes[0].dataType = Type.SQL_TIMESTAMP;
          }
        }
      }
      if (this.nodes[0].dataType.isDateTimeType())
      {
        if ((i == 0) && (!this.nodes[1].dataType.isCharacterType())) {
          throw Error.error(5566);
        }
        this.dataType = this.nodes[0].dataType;
        return;
      }
      if (!this.nodes[0].dataType.isNumberType()) {
        throw Error.error(5561);
      }
    case 174: 
      localObject1 = null;
      if (this.nodes[0].dataType == null) {
        throw Error.error(5567);
      }
      if (!this.nodes[0].dataType.isNumberType()) {
        throw Error.error(5563);
      }
      if (this.nodes[1] == null)
      {
        this.nodes[1] = new ExpressionValue(ValuePool.INTEGER_0, Type.SQL_INTEGER);
        localObject1 = ValuePool.INTEGER_0;
      }
      else
      {
        if (this.nodes[1].dataType == null) {
          this.nodes[1].dataType = Type.SQL_INTEGER;
        } else if (!this.nodes[1].dataType.isIntegralType()) {
          throw Error.error(5563);
        }
        if (this.nodes[1].opType == 1) {
          localObject1 = (Number)this.nodes[1].getValue(paramSession);
        }
      }
      this.dataType = this.nodes[0].dataType;
      if (localObject1 != null)
      {
        i1 = ((Number)localObject1).intValue();
        if (i1 < 0) {
          i1 = 0;
        } else if (i1 > this.dataType.scale) {
          i1 = this.dataType.scale;
        }
        if (((this.dataType.typeCode == 3) || (this.dataType.typeCode == 2)) && (i1 != this.dataType.scale)) {
          this.dataType = new NumberType(this.dataType.typeCode, this.dataType.precision - this.dataType.scale + i1, i1);
        }
      }
      return;
    case 162: 
      if (this.nodes[0].dataType == null) {
        this.nodes[0].dataType = Type.SQL_TIMESTAMP;
      }
      if (this.nodes[1].dataType == null) {
        this.nodes[1].dataType = Type.SQL_VARCHAR_DEFAULT;
      }
      if (!this.nodes[1].dataType.isCharacterType()) {
        throw Error.error(5563);
      }
      if (!this.nodes[0].dataType.isDateTimeType()) {
        throw Error.error(5563);
      }
      this.dataType = CharacterType.getCharacterType(12, 64L);
      return;
    case 166: 
      if (this.nodes[0].dataType == null) {
        this.nodes[0].dataType = Type.SQL_VARCHAR_DEFAULT;
      }
      if (!this.nodes[0].dataType.isCharacterType()) {
        throw Error.error(5563);
      }
      this.dataType = Type.SQL_DECIMAL_DEFAULT;
      return;
    case 163: 
    case 167: 
      if (this.nodes[0].dataType == null) {
        this.nodes[0].dataType = Type.SQL_VARCHAR_DEFAULT;
      }
      if (this.nodes[1] == null)
      {
        localObject1 = "DD-MON-YYYY HH24:MI:SS";
        if (this.funcType == 167) {
          localObject1 = "DD-MON-YYYY HH24:MI:SS.FF";
        }
        this.nodes[1] = new ExpressionValue(localObject1, Type.SQL_VARCHAR);
      }
      if (this.nodes[1].dataType == null) {
        this.nodes[1].dataType = Type.SQL_VARCHAR_DEFAULT;
      }
      if ((!this.nodes[0].dataType.isCharacterType()) || (!this.nodes[1].dataType.isCharacterType())) {
        throw Error.error(5563);
      }
      this.dataType = (this.funcType == 163 ? Type.SQL_TIMESTAMP_NO_FRACTION : Type.SQL_TIMESTAMP);
      return;
    case 157: 
      localObject1 = this.nodes[0].dataType;
      if (this.nodes[1] == null)
      {
        if (localObject1 == null) {
          localObject1 = this.nodes[0].dataType = Type.SQL_VARCHAR_DEFAULT;
        }
        if ((!((Type)localObject1).isCharacterType()) && (((Type)localObject1).typeCode != 93) && (((Type)localObject1).typeCode != 95) && (!((Type)localObject1).isNumberType())) {
          throw Error.error(5561);
        }
      }
      else
      {
        if (localObject1 == null) {
          if (this.nodes[1].dataType == null) {
            localObject1 = this.nodes[0].dataType = this.nodes[1].dataType = Type.SQL_VARCHAR_DEFAULT;
          } else if (this.nodes[1].dataType.isCharacterType()) {
            localObject1 = this.nodes[0].dataType = Type.SQL_VARCHAR_DEFAULT;
          } else {
            localObject1 = this.nodes[0].dataType = Type.SQL_DATE;
          }
        }
        if (this.nodes[1].dataType == null) {
          if (((Type)localObject1).isCharacterType()) {
            this.nodes[1].dataType = Type.SQL_VARCHAR_DEFAULT;
          } else if (((Type)localObject1).typeCode == 91) {
            this.nodes[1].dataType = Type.SQL_TIME;
          }
        }
        if (((((Type)localObject1).typeCode != 91) || (this.nodes[1].dataType.typeCode != 92)) && ((!((Type)localObject1).isCharacterType()) || (!this.nodes[1].dataType.isCharacterType()))) {
          throw Error.error(5561);
        }
      }
      this.dataType = Type.SQL_TIMESTAMP;
      return;
    case 158: 
      localObject1 = this.nodes[0].dataType;
      if (localObject1 == null) {
        localObject1 = this.nodes[0].dataType = Type.SQL_BIGINT;
      }
      if ((((Type)localObject1).typeCode != 93) && (((Type)localObject1).typeCode != 95) && (!((Type)localObject1).isNumberType())) {
        throw Error.error(5561);
      }
      this.dataType = Type.SQL_TIMESTAMP_WITH_TIME_ZONE;
      return;
    case 124: 
      this.dataType = Type.SQL_DOUBLE;
      break;
    case 175: 
      if (this.nodes[0] == null)
      {
        this.dataType = Type.SQL_BINARY_16;
        return;
      }
      if (this.nodes[0].dataType == null)
      {
        this.nodes[0].dataType = Type.SQL_VARCHAR;
        this.dataType = Type.SQL_BINARY_16;
        return;
      }
      if (this.nodes[0].dataType.isCharacterType())
      {
        this.dataType = Type.SQL_BINARY_16;
        return;
      }
      if ((this.nodes[0].dataType.isBinaryType()) && (!this.nodes[0].dataType.isLobType()))
      {
        this.dataType = Type.SQL_CHAR_16;
        return;
      }
      throw Error.error(5563);
    case 176: 
    case 177: 
      if (this.nodes[0] != null) {
        if (this.nodes[0].dataType == null) {
          this.nodes[0].dataType = Type.SQL_TIMESTAMP;
        } else if ((!this.nodes[0].dataType.isDateTimeType()) || (this.nodes[0].dataType.typeCode == 92) || (this.nodes[0].dataType.typeCode == 94)) {
          throw Error.error(5563);
        }
      }
      this.dataType = Type.SQL_BIGINT;
      break;
    case 127: 
      if (this.nodes[0] != null) {
        if (this.nodes[0].dataType == null) {
          this.nodes[0].dataType = Type.SQL_BIGINT;
        } else if (!this.nodes[0].dataType.isExactNumberType()) {
          throw Error.error(5563);
        }
      }
      this.dataType = Type.SQL_DOUBLE;
      break;
    case 71: 
    case 75: 
    case 76: 
    case 85: 
    case 86: 
    case 99: 
    case 116: 
    case 126: 
    case 138: 
    case 148: 
    case 156: 
      if (this.nodes[0].dataType == null) {
        this.nodes[0].dataType = Type.SQL_DOUBLE;
      }
      if (!this.nodes[0].dataType.isNumberType()) {
        throw Error.error(5561);
      }
      this.dataType = Type.SQL_DOUBLE;
      break;
    case 147: 
      if (this.nodes[0].dataType == null) {
        this.nodes[0].dataType = Type.SQL_DOUBLE;
      }
      if (!this.nodes[0].dataType.isNumberType()) {
        throw Error.error(5561);
      }
      this.dataType = Type.SQL_INTEGER;
      break;
    case 77: 
      if (this.nodes[0].dataType == null) {
        this.nodes[0].dataType = Type.SQL_DOUBLE;
      }
      if (this.nodes[1].dataType == null) {
        this.nodes[1].dataType = Type.SQL_DOUBLE;
      }
      if ((!this.nodes[0].dataType.isNumberType()) || (!this.nodes[1].dataType.isNumberType())) {
        throw Error.error(5561);
      }
      this.dataType = Type.SQL_DOUBLE;
      break;
    case 149: 
      if (this.nodes[0].dataType == null) {
        this.nodes[0].dataType = Type.SQL_VARCHAR;
      }
      if (!this.nodes[0].dataType.isCharacterType()) {
        throw Error.error(5561);
      }
      this.dataType = CharacterType.getCharacterType(12, 4L);
      break;
    case 78: 
    case 79: 
    case 80: 
    case 81: 
    case 82: 
      if (this.nodes[0].dataType == null) {
        this.nodes[0].dataType = this.nodes[1].dataType;
      }
      if (this.funcType == 80)
      {
        if (this.nodes[0].dataType == null) {
          this.nodes[0].dataType = Type.SQL_INTEGER;
        }
        this.dataType = this.nodes[0].dataType;
      }
      else
      {
        this.dataType = this.nodes[0].dataType;
        if (this.nodes[1].dataType == null) {
          this.nodes[1].dataType = this.nodes[0].dataType;
        }
        for (int j = 0; j < this.nodes.length; j++) {
          if (this.nodes[j].dataType == null) {
            this.nodes[j].dataType = Type.SQL_INTEGER;
          }
        }
        this.dataType = this.nodes[0].dataType.getAggregateType(this.nodes[1].dataType);
      }
      switch (this.dataType.typeCode)
      {
      case -6: 
      case 2: 
      case 3: 
      case 4: 
      case 5: 
      case 8: 
      case 25: 
        break;
      case 14: 
      case 15: 
        break;
      case -5: 
      case -4: 
      case -3: 
      case -2: 
      case -1: 
      case 0: 
      case 1: 
      case 6: 
      case 7: 
      case 9: 
      case 10: 
      case 11: 
      case 12: 
      case 13: 
      case 16: 
      case 17: 
      case 18: 
      case 19: 
      case 20: 
      case 21: 
      case 22: 
      case 23: 
      case 24: 
      default: 
        throw Error.error(5561);
      }
      break;
    case 74: 
      if (this.nodes[0].dataType == null) {
        this.nodes[0].dataType = Type.SQL_VARCHAR;
      }
      if (!this.nodes[0].dataType.isCharacterType()) {
        throw Error.error(5561);
      }
      this.dataType = Type.SQL_INTEGER;
      break;
    case 83: 
      if (this.nodes[0].dataType == null) {
        this.nodes[0].dataType = Type.SQL_INTEGER;
      }
      if (!this.nodes[0].dataType.isExactNumberType()) {
        throw Error.error(5561);
      }
      this.dataType = CharacterType.getCharacterType(12, 1L);
      break;
    case 101: 
      if (this.nodes[0].dataType == null) {
        this.nodes[0].dataType = Type.SQL_VARCHAR;
      }
      if (this.nodes[1].dataType == null) {
        this.nodes[1].dataType = Type.SQL_VARCHAR;
      }
      this.dataType = Type.SQL_INTEGER;
      break;
    case 103: 
      if (this.nodes[0].dataType == null) {
        this.nodes[0].dataType = Type.SQL_VARCHAR;
      }
      if (!this.nodes[0].dataType.isCharacterType()) {
        throw Error.error(5561);
      }
      this.dataType = (this.nodes[0].dataType.precision == 0L ? Type.SQL_VARBINARY_DEFAULT : BinaryType.getBinaryType(61, this.nodes[0].dataType.precision / 2L));
      break;
    case 128: 
      if (this.nodes[0].dataType == null) {
        this.nodes[0].dataType = Type.SQL_VARBINARY;
      }
      if (!this.nodes[0].dataType.isBinaryType()) {
        throw Error.error(5561);
      }
      this.dataType = (this.nodes[0].dataType.precision == 0L ? Type.SQL_VARCHAR_DEFAULT : CharacterType.getCharacterType(12, this.nodes[0].dataType.precision * 2L));
      break;
    case 133: 
      if (this.nodes[0].dataType == null) {
        this.nodes[0].dataType = Type.SQL_VARCHAR;
      }
      k = this.nodes[0].dataType.isCharacterType();
      if ((k == 0) && (!this.nodes[0].dataType.isBinaryType())) {
        throw Error.error(5561);
      }
      if (!this.nodes[1].dataType.isExactNumberType()) {
        throw Error.error(5561);
      }
      this.dataType = (k != 0 ? Type.SQL_VARCHAR_DEFAULT : Type.SQL_VARBINARY_DEFAULT);
      break;
    case 134: 
      if (this.nodes[2] == null) {
        this.nodes[2] = new ExpressionValue("", Type.SQL_VARCHAR);
      }
      for (k = 0; k < this.nodes.length; k++) {
        if (this.nodes[k].dataType == null) {
          this.nodes[k].dataType = Type.SQL_VARCHAR;
        } else if (!this.nodes[k].dataType.isCharacterType()) {
          throw Error.error(5561);
        }
      }
      this.dataType = Type.SQL_VARCHAR_DEFAULT;
      break;
    case 112: 
    case 136: 
      if (this.nodes[0].dataType == null) {
        this.nodes[0].dataType = Type.SQL_VARCHAR;
      }
      if (!this.nodes[0].dataType.isCharacterType()) {
        throw Error.error(5561);
      }
      if (this.nodes[1].dataType == null) {
        this.nodes[1].dataType = Type.SQL_INTEGER;
      }
      if (!this.nodes[1].dataType.isExactNumberType()) {
        throw Error.error(5561);
      }
      this.dataType = (this.nodes[0].dataType.precision == 0L ? Type.SQL_VARCHAR_DEFAULT : ((CharacterType)this.nodes[0].dataType).getCharacterType(this.nodes[0].dataType.precision));
      break;
    case 151: 
      if (this.nodes[0].dataType == null) {
        this.nodes[0].dataType = Type.SQL_INTEGER;
      }
      if (!this.nodes[0].dataType.isIntegralType()) {
        throw Error.error(5561);
      }
      this.dataType = Type.SQL_VARCHAR_DEFAULT;
      break;
    case 135: 
      if (this.nodes[0].dataType == null) {
        this.nodes[0].dataType = Type.SQL_VARCHAR_DEFAULT;
      }
      this.dataType = this.nodes[0].dataType;
      if ((this.dataType.isCharacterType()) && (!this.dataType.isLobType())) {
        return;
      }
      throw Error.error(5561);
    case 130: 
      if (this.nodes[2] == null) {
        this.nodes[2] = new ExpressionValue("", Type.SQL_VARCHAR);
      }
    case 129: 
    case 131: 
    case 132: 
      if (this.nodes[0].dataType == null) {
        this.nodes[0].dataType = Type.SQL_VARCHAR_DEFAULT;
      }
      if (this.nodes[1].dataType == null) {
        this.nodes[1].dataType = Type.SQL_VARCHAR_DEFAULT;
      }
      if ((!this.nodes[0].dataType.isCharacterType()) || (!this.nodes[1].dataType.isCharacterType()) || (this.nodes[1].dataType.isLobType())) {
        throw Error.error(5561);
      }
      if (this.nodes[1].exprSubType == 1)
      {
        String str = (String)this.nodes[1].getValue(paramSession);
        this.pattern = Pattern.compile(str);
      }
      switch (this.funcType)
      {
      case 129: 
        this.dataType = Type.SQL_BOOLEAN;
        break;
      case 130: 
        this.dataType = Type.SQL_VARCHAR_DEFAULT;
        break;
      case 131: 
        this.dataType = Type.SQL_VARCHAR_DEFAULT;
        break;
      case 132: 
        this.dataType = Type.getDefaultArrayType(12);
        break;
      }
      break;
    case 87: 
      for (int m = 0; m < this.nodes.length; m++) {
        if (this.nodes[m].dataType == null) {
          this.nodes[m].dataType = Type.SQL_VARCHAR;
        } else if (!this.nodes[m].dataType.isCharacterType()) {
          throw Error.error(5561);
        }
      }
      this.dataType = Type.SQL_VARCHAR_DEFAULT;
      break;
    case 113: 
      if (this.nodes[0].dataType == null) {
        this.nodes[0].dataType = Type.SQL_VARCHAR_DEFAULT;
      }
      if (!this.nodes[0].dataType.isCharacterType()) {
        throw Error.error(5561);
      }
      if (this.nodes[1] == null)
      {
        this.dataType = Type.SQL_BLOB;
        return;
      }
      this.dataType = Type.SQL_CLOB;
      if ((this.nodes[1].dataType != null) && (this.nodes[1].dataType.isCharacterType())) {
        return;
      }
      throw Error.error(5561);
    case 117: 
    case 139: 
      if (this.nodes[0].dataType == null) {
        this.nodes[0].dataType = Type.SQL_VARCHAR_DEFAULT;
      }
      if (this.nodes[1].dataType == null) {
        this.nodes[1].dataType = Type.SQL_INTEGER;
      }
      if (!this.nodes[1].dataType.isIntegralType()) {
        throw Error.error(5561);
      }
      if (this.nodes[2] != null)
      {
        if (this.nodes[2].dataType == null) {
          this.nodes[2].dataType = Type.SQL_VARCHAR_DEFAULT;
        }
        if (!this.nodes[2].dataType.isCharacterType()) {
          throw Error.error(5561);
        }
      }
      this.dataType = this.nodes[0].dataType;
      if (this.dataType.typeCode != 40) {
        this.dataType = Type.SQL_VARCHAR_DEFAULT;
      }
      if (this.nodes[1].opType != 1) {
        return;
      }
      localObject2 = (Number)this.nodes[1].getValue(paramSession);
      if (localObject2 != null) {
        this.dataType = ((CharacterType)this.dataType).getCharacterType(((Number)localObject2).longValue());
      }
      break;
    case 125: 
      if (this.nodes[1].dataType == null) {
        throw Error.error(5567);
      }
      if (!this.nodes[1].dataType.isArrayType()) {
        throw Error.error(5563);
      }
      if (this.nodes[0].dataType == null) {
        this.nodes[0].dataType = this.nodes[1].dataType.collectionBaseType();
      }
      if (!this.nodes[1].dataType.collectionBaseType().canCompareDirect(this.nodes[0].dataType)) {
        throw Error.error(5563);
      }
      if (this.nodes[2] == null) {
        this.nodes[2] = new ExpressionValue(ValuePool.INTEGER_1, Type.SQL_INTEGER);
      }
      if (this.nodes[2].dataType == null) {
        this.nodes[2].dataType = Type.SQL_INTEGER;
      }
      if (!this.nodes[2].dataType.isIntegralType()) {
        throw Error.error(5563);
      }
      this.dataType = Type.SQL_INTEGER;
      break;
    case 150: 
      if (this.nodes[0].dataType == null) {
        throw Error.error(5567);
      }
      if (!this.nodes[0].dataType.isArrayType()) {
        throw Error.error(5563);
      }
      if (this.nodes[1] == null) {
        this.nodes[1] = new ExpressionValue(ValuePool.getInt(359), Type.SQL_INTEGER);
      }
      if (this.nodes[2] == null) {
        this.nodes[2] = new ExpressionValue(ValuePool.getInt(423), Type.SQL_INTEGER);
      }
      this.dataType = this.nodes[0].dataType;
      break;
    case 73: 
      if (this.nodes[0].dataType == null) {
        this.nodes[0].dataType = Type.SQL_TIMESTAMP_NO_FRACTION;
      }
      if (!this.nodes[0].dataType.isDateOrTimestampType()) {
        throw Error.error(5563);
      }
      this.dataType = Type.SQL_TIMESTAMP_NO_FRACTION;
      break;
    case 98: 
      this.dataType = CharacterType.getCharacterType(12, 6L);
      break;
    case 102: 
      if (this.nodes[0].dataType == null) {
        this.nodes[0].dataType = Type.SQL_TIMESTAMP;
      }
      if (this.nodes[1].dataType == null) {
        this.nodes[1].dataType = Type.SQL_VARCHAR;
      }
      this.dataType = Type.SQL_TIMESTAMP_WITH_TIME_ZONE;
      break;
    case 111: 
      if (this.nodes[0].dataType == null) {
        this.nodes[0].dataType = Type.SQL_TIMESTAMP_NO_FRACTION;
      }
      if (!this.nodes[0].dataType.isDateOrTimestampType()) {
        throw Error.error(5563);
      }
      this.dataType = Type.SQL_TIMESTAMP_NO_FRACTION;
      break;
    case 119: 
      if (this.nodes[0].dataType == null) {
        this.nodes[0].dataType = Type.SQL_TIMESTAMP_NO_FRACTION;
      }
      if (this.nodes[1].dataType == null) {
        this.nodes[1].dataType = Type.SQL_TIMESTAMP_NO_FRACTION;
      }
      if (!this.nodes[0].dataType.isDateOrTimestampType()) {
        throw Error.error(5563);
      }
      if (!this.nodes[1].dataType.isDateOrTimestampType()) {
        throw Error.error(5563);
      }
      this.dataType = Type.SQL_DECIMAL_DEFAULT;
      break;
    case 120: 
      if (this.nodes[0].dataType == null) {
        this.nodes[0].dataType = Type.SQL_TIMESTAMP_NO_FRACTION;
      }
      if (this.nodes[1].dataType == null) {
        this.nodes[1].dataType = Type.SQL_VARCHAR;
      }
      if (this.nodes[2].dataType == null) {
        this.nodes[2].dataType = Type.SQL_VARCHAR;
      }
      this.dataType = Type.SQL_TIMESTAMP_NO_FRACTION;
      break;
    case 121: 
      if (this.nodes[0].dataType == null) {
        this.nodes[0].dataType = Type.SQL_TIMESTAMP_NO_FRACTION;
      }
      if (this.nodes[1].dataType == null) {
        this.nodes[1].dataType = Type.SQL_VARCHAR;
      }
      this.dataType = Type.SQL_TIMESTAMP_NO_FRACTION;
      break;
    case 122: 
      if (this.nodes[0].dataType == null) {
        this.nodes[0].dataType = Type.SQL_DOUBLE;
      }
      if (this.nodes[1].dataType == null) {
        this.nodes[1].dataType = Type.SQL_VARCHAR;
      }
      if (!this.nodes[0].dataType.isNumberType()) {
        throw Error.error(5563);
      }
      if (!this.nodes[1].dataType.isCharacterType()) {
        throw Error.error(5563);
      }
      this.dataType = Type.SQL_INTERVAL_DAY_TO_SECOND_MAX_PRECISION;
      break;
    case 123: 
      if (this.nodes[0].dataType == null) {
        this.nodes[0].dataType = Type.SQL_DOUBLE;
      }
      if (this.nodes[1].dataType == null) {
        this.nodes[1].dataType = Type.SQL_VARCHAR;
      }
      if (!this.nodes[0].dataType.isNumberType()) {
        throw Error.error(5563);
      }
      if (!this.nodes[1].dataType.isCharacterType()) {
        throw Error.error(5563);
      }
      this.dataType = Type.SQL_INTERVAL_YEAR_TO_MONTH_MAX_PRECISION;
      break;
    case 146: 
      this.dataType = CharacterType.getCharacterType(12, 6L);
      break;
    case 153: 
      if (this.nodes[0].dataType == null) {
        this.nodes[0].dataType = Type.SQL_TIMESTAMP_WITH_TIME_ZONE;
      }
      this.dataType = Type.SQL_TIMESTAMP;
      break;
    case 154: 
      this.dataType = Type.SQL_TIMESTAMP_NO_FRACTION;
      break;
    case 155: 
      this.dataType = Type.SQL_TIMESTAMP_WITH_TIME_ZONE;
      break;
    case 164: 
      if (this.nodes[0].dataType == null) {
        this.nodes[0].dataType = Type.SQL_VARCHAR;
      }
      this.dataType = Type.SQL_INTERVAL_DAY_TO_SECOND_MAX_PRECISION;
      break;
    case 165: 
      if (this.nodes[0].dataType == null) {
        this.nodes[0].dataType = Type.SQL_VARCHAR;
      }
      this.dataType = Type.SQL_INTERVAL_YEAR_TO_MONTH_MAX_PRECISION;
      break;
    case 168: 
      if (this.nodes[0].dataType == null) {
        this.nodes[0].dataType = Type.SQL_VARCHAR_DEFAULT;
      }
      if (this.nodes[1] == null)
      {
        localObject2 = "DD-MON-YYYY HH24:MI:SS:FF TZH:TZM";
        this.nodes[1] = new ExpressionValue(localObject2, Type.SQL_VARCHAR);
      }
      if (this.nodes[1].dataType == null) {
        this.nodes[1].dataType = Type.SQL_VARCHAR;
      }
      if ((!this.nodes[0].dataType.isCharacterType()) || (!this.nodes[1].dataType.isCharacterType())) {
        throw Error.error(5567);
      }
      this.dataType = Type.SQL_TIMESTAMP_WITH_TIME_ZONE;
      break;
    case 172: 
      for (int n = 0; n < this.nodes.length; n++)
      {
        if (this.nodes[n].dataType == null) {
          this.nodes[n].dataType = Type.SQL_VARCHAR_DEFAULT;
        }
        if ((!this.nodes[n].dataType.isCharacterType()) || (this.nodes[n].dataType.isLobType())) {
          throw Error.error(5563);
        }
      }
      if ((this.nodes[1].valueData != null) && (this.nodes[2].valueData != null)) {
        this.charLookup = getTranslationMap((String)this.nodes[1].valueData, (String)this.nodes[2].valueData);
      }
      this.dataType = this.nodes[0].dataType;
      break;
    }
    throw Error.runtimeError(201, "FunctionCustom");
  }
  
  public String getSQL()
  {
    Object localObject;
    switch (this.funcType)
    {
    case 1: 
      localObject = new StringBuffer("LOCATE").append("(").append(this.nodes[0].getSQL()).append(",").append(this.nodes[1].getSQL());
      if ((this.nodes.length > 3) && (this.nodes[3] != null)) {
        ((StringBuffer)localObject).append(",").append(this.nodes[3].getSQL());
      }
      ((StringBuffer)localObject).append(")").toString();
      return ((StringBuffer)localObject).toString();
    case 117: 
    case 139: 
      localObject = new StringBuffer(this.name);
      ((StringBuffer)localObject).append("(").append(this.nodes[0].getSQL());
      ((StringBuffer)localObject).append(",").append(this.nodes[1].getSQL());
      if (this.nodes[2] != null) {
        ((StringBuffer)localObject).append(",").append(this.nodes[2].getSQL());
      }
      ((StringBuffer)localObject).append(")").toString();
      return ((StringBuffer)localObject).toString();
    case 5: 
    case 31: 
    case 32: 
      return super.getSQL();
    case 125: 
      localObject = new StringBuffer(this.name).append('(');
      ((StringBuffer)localObject).append(this.nodes[0].getSQL()).append(' ').append("IN");
      ((StringBuffer)localObject).append(' ').append(this.nodes[1].getSQL());
      if (((Number)this.nodes[1].valueData).intValue() == 410)
      {
        ((StringBuffer)localObject).append(' ').append("FROM");
        ((StringBuffer)localObject).append(' ').append(this.nodes[2].getSQL());
      }
      ((StringBuffer)localObject).append(')');
      return ((StringBuffer)localObject).toString();
    case 150: 
      localObject = new StringBuffer(this.name).append('(');
      ((StringBuffer)localObject).append(this.nodes[0].getSQL());
      if (((Number)this.nodes[1].valueData).intValue() == 410) {
        ((StringBuffer)localObject).append(' ').append("DESC");
      }
      if (((Number)this.nodes[2].valueData).intValue() == 452)
      {
        ((StringBuffer)localObject).append(' ').append("NULLS").append(' ');
        ((StringBuffer)localObject).append("LAST");
      }
      ((StringBuffer)localObject).append(')');
      return ((StringBuffer)localObject).toString();
    case 154: 
    case 155: 
      return this.name;
    case 72: 
    case 88: 
    case 89: 
    case 90: 
    case 91: 
    case 92: 
    case 104: 
    case 106: 
    case 107: 
    case 108: 
    case 109: 
    case 110: 
    case 124: 
    case 143: 
    case 144: 
    case 145: 
    case 161: 
    case 169: 
    case 170: 
    case 171: 
      return this.name + "(" + ")";
    case 159: 
      localObject = Tokens.getSQLTSIString(((Number)this.nodes[0].getValue(null)).intValue());
      return "TIMESTAMPADD" + "(" + (String)localObject + "," + this.nodes[1].getSQL() + "," + this.nodes[2].getSQL() + ")";
    case 160: 
      localObject = Tokens.getSQLTSIString(((Number)this.nodes[0].getValue(null)).intValue());
      return "TIMESTAMPDIFF" + "(" + (String)localObject + "," + this.nodes[1].getSQL() + "," + this.nodes[2].getSQL() + ")";
    case 93: 
      return this.nodes[0].getSQL() + ' ' + '+' + this.nodes[1].getSQL();
    case 94: 
      return this.nodes[0].getSQL() + ' ' + '-' + this.nodes[1].getSQL();
    case 127: 
    case 176: 
    case 177: 
      localObject = new StringBuffer(this.name).append('(');
      if (this.nodes[0] != null) {
        ((StringBuffer)localObject).append(this.nodes[0].getSQL());
      }
      ((StringBuffer)localObject).append(')');
      return ((StringBuffer)localObject).toString();
    case 113: 
    case 137: 
    case 157: 
    case 163: 
    case 166: 
    case 167: 
    case 168: 
    case 173: 
    case 174: 
      localObject = new StringBuffer(this.name).append('(');
      ((StringBuffer)localObject).append(this.nodes[0].getSQL());
      if ((this.nodes.length > 1) && (this.nodes[1] != null)) {
        ((StringBuffer)localObject).append(',').append(this.nodes[1].getSQL());
      }
      ((StringBuffer)localObject).append(')');
      return ((StringBuffer)localObject).toString();
    case 71: 
    case 74: 
    case 75: 
    case 76: 
    case 83: 
    case 85: 
    case 86: 
    case 97: 
    case 99: 
    case 103: 
    case 114: 
    case 116: 
    case 126: 
    case 128: 
    case 135: 
    case 138: 
    case 147: 
    case 148: 
    case 149: 
    case 151: 
    case 156: 
      return this.name + '(' + this.nodes[0].getSQL() + ')';
    case 77: 
    case 78: 
    case 79: 
    case 80: 
    case 81: 
    case 82: 
    case 87: 
    case 101: 
    case 112: 
    case 129: 
    case 131: 
    case 132: 
    case 133: 
    case 136: 
    case 162: 
      return this.name + '(' + this.nodes[0].getSQL() + "," + this.nodes[1].getSQL() + ')';
    case 100: 
      localObject = new StringBuffer(this.name).append('(');
      ((StringBuffer)localObject).append("ROW_COUNT");
      ((StringBuffer)localObject).append(')');
      return ((StringBuffer)localObject).toString();
    case 130: 
    case 134: 
    case 142: 
      return this.name + '(' + this.nodes[0].getSQL() + "," + this.nodes[1].getSQL() + "," + this.nodes[2].getSQL() + ')';
    case 73: 
    case 98: 
    case 102: 
    case 111: 
    case 119: 
    case 120: 
    case 121: 
    case 122: 
    case 123: 
    case 146: 
    case 153: 
    case 164: 
    case 165: 
    case 172: 
      return getSQLSimple();
    }
    return super.getSQL();
  }
  
  private String getSQLSimple()
  {
    StringBuffer localStringBuffer = new StringBuffer(this.name).append('(');
    for (int i = 0; i < this.nodes.length; i++)
    {
      if (i > 0) {
        localStringBuffer.append(',');
      }
      localStringBuffer.append(this.nodes[i].getSQL());
    }
    localStringBuffer.append(')');
    return localStringBuffer.toString();
  }
  
  public static char[] soundex(String paramString)
  {
    if (paramString == null) {
      return null;
    }
    paramString = paramString.toUpperCase(Locale.ENGLISH);
    int i = paramString.length();
    char[] arrayOfChar = { '0', '0', '0', '0' };
    int j = 48;
    int k = 0;
    int m = 0;
    while ((k < i) && (m < 4))
    {
      int n = paramString.charAt(k);
      int i1;
      if ("AEIOUY".indexOf(n) != -1)
      {
        i1 = 55;
      }
      else if ((n == 72) || (n == 87))
      {
        i1 = 56;
      }
      else if ("BFPV".indexOf(n) != -1)
      {
        i1 = 49;
      }
      else if ("CGJKQSXZ".indexOf(n) != -1)
      {
        i1 = 50;
      }
      else if ((n == 68) || (n == 84))
      {
        i1 = 51;
      }
      else if (n == 76)
      {
        i1 = 52;
      }
      else if ((n == 77) || (n == 78))
      {
        i1 = 53;
      }
      else
      {
        if (n != 82) {
          break label275;
        }
        i1 = 54;
      }
      if (m == 0)
      {
        arrayOfChar[(m++)] = n;
        j = i1;
      }
      else if (i1 <= 54)
      {
        if (i1 != j)
        {
          arrayOfChar[(m++)] = i1;
          j = i1;
        }
      }
      else if (i1 == 55)
      {
        j = i1;
      }
      label275:
      k++;
    }
    return arrayOfChar;
  }
  
  int getTSIToken(String paramString)
  {
    int i;
    if (("yy".equalsIgnoreCase(paramString)) || ("year".equalsIgnoreCase(paramString))) {
      i = 890;
    } else if (("mm".equalsIgnoreCase(paramString)) || ("month".equalsIgnoreCase(paramString))) {
      i = 888;
    } else if (("dd".equalsIgnoreCase(paramString)) || ("day".equalsIgnoreCase(paramString))) {
      i = 886;
    } else if (("hh".equalsIgnoreCase(paramString)) || ("hour".equalsIgnoreCase(paramString))) {
      i = 885;
    } else if (("mi".equalsIgnoreCase(paramString)) || ("minute".equalsIgnoreCase(paramString))) {
      i = 884;
    } else if (("ss".equalsIgnoreCase(paramString)) || ("second".equalsIgnoreCase(paramString))) {
      i = 883;
    } else if (("ms".equalsIgnoreCase(paramString)) || ("millisecond".equalsIgnoreCase(paramString))) {
      i = 882;
    } else {
      throw Error.error(5566, paramString);
    }
    return i;
  }
  
  IntKeyIntValueHashMap getTranslationMap(String paramString1, String paramString2)
  {
    IntKeyIntValueHashMap localIntKeyIntValueHashMap = new IntKeyIntValueHashMap();
    for (int i = 0; i < paramString1.length(); i++)
    {
      int j = paramString1.charAt(i);
      if (i >= paramString2.length())
      {
        localIntKeyIntValueHashMap.put(j, -1);
      }
      else
      {
        int k = paramString2.charAt(i);
        localIntKeyIntValueHashMap.put(j, k);
      }
    }
    return localIntKeyIntValueHashMap;
  }
  
  String translateWithMap(String paramString, IntKeyIntValueHashMap paramIntKeyIntValueHashMap)
  {
    StringBuffer localStringBuffer = new StringBuffer(paramString.length());
    for (int i = 0; i < paramString.length(); i++)
    {
      int j = paramString.charAt(i);
      int k = paramIntKeyIntValueHashMap.get(j, -2);
      if (k == -2) {
        localStringBuffer.append((char)j);
      } else if (k != -1) {
        localStringBuffer.append((char)k);
      }
    }
    return localStringBuffer.toString();
  }
  
  static
  {
    nonDeterministicFuncSet.add(72);
    nonDeterministicFuncSet.add(87);
    nonDeterministicFuncSet.add(88);
    nonDeterministicFuncSet.add(89);
    nonDeterministicFuncSet.add(91);
    nonDeterministicFuncSet.add(104);
    nonDeterministicFuncSet.add(106);
    nonDeterministicFuncSet.add(110);
    nonDeterministicFuncSet.add(108);
    nonDeterministicFuncSet.add(109);
    nonDeterministicFuncSet.add(107);
    nonDeterministicFuncSet.add(143);
    nonDeterministicFuncSet.add(144);
    nonDeterministicFuncSet.add(145);
    nonDeterministicFuncSet.add(146);
    nonDeterministicFuncSet.add(154);
    nonDeterministicFuncSet.add(155);
    nonDeterministicFuncSet.add(157);
    nonDeterministicFuncSet.add(161);
    nonDeterministicFuncSet.add(169);
    nonDeterministicFuncSet.add(170);
    nonDeterministicFuncSet.add(171);
    nonDeterministicFuncSet.add(175);
    nonDeterministicFuncSet.add(176);
    nonDeterministicFuncSet.add(177);
    customRegularFuncMap.put(671, 71);
    customRegularFuncMap.put(672, 72);
    customRegularFuncMap.put(675, 73);
    customRegularFuncMap.put(673, 150);
    customRegularFuncMap.put(674, 74);
    customRegularFuncMap.put(676, 75);
    customRegularFuncMap.put(677, 76);
    customRegularFuncMap.put(678, 77);
    customRegularFuncMap.put(680, 78);
    customRegularFuncMap.put(681, 79);
    customRegularFuncMap.put(682, 6);
    customRegularFuncMap.put(683, 80);
    customRegularFuncMap.put(684, 81);
    customRegularFuncMap.put(685, 82);
    customRegularFuncMap.put(36, 83);
    customRegularFuncMap.put(686, 83);
    customRegularFuncMap.put(687, 84);
    customRegularFuncMap.put(689, 85);
    customRegularFuncMap.put(690, 86);
    customRegularFuncMap.put(691, 87);
    customRegularFuncMap.put(692, 43);
    customRegularFuncMap.put(693, 51);
    customRegularFuncMap.put(605, 88);
    customRegularFuncMap.put(695, 90);
    customRegularFuncMap.put(694, 89);
    customRegularFuncMap.put(696, 91);
    customRegularFuncMap.put(697, 92);
    customRegularFuncMap.put(698, 93);
    customRegularFuncMap.put(699, 94);
    customRegularFuncMap.put(700, 95);
    customRegularFuncMap.put(701, 96);
    customRegularFuncMap.put(78, 5);
    customRegularFuncMap.put(706, 5);
    customRegularFuncMap.put(707, 5);
    customRegularFuncMap.put(708, 5);
    customRegularFuncMap.put(709, 5);
    customRegularFuncMap.put(710, 97);
    customRegularFuncMap.put(711, 98);
    customRegularFuncMap.put(713, 99);
    customRegularFuncMap.put(412, 100);
    customRegularFuncMap.put(714, 101);
    customRegularFuncMap.put(716, 102);
    customRegularFuncMap.put(717, 103);
    customRegularFuncMap.put(137, 5);
    customRegularFuncMap.put(138, 104);
    customRegularFuncMap.put(145, 32);
    customRegularFuncMap.put(721, 1);
    customRegularFuncMap.put(722, 106);
    customRegularFuncMap.put(723, 108);
    customRegularFuncMap.put(724, 109);
    customRegularFuncMap.put(725, 110);
    customRegularFuncMap.put(726, 107);
    customRegularFuncMap.put(728, 111);
    customRegularFuncMap.put(729, 26);
    customRegularFuncMap.put(163, 112);
    customRegularFuncMap.put(453, 7);
    customRegularFuncMap.put(731, 113);
    customRegularFuncMap.put(738, 114);
    customRegularFuncMap.put(732, 1);
    customRegularFuncMap.put(734, 14);
    customRegularFuncMap.put(735, 116);
    customRegularFuncMap.put(736, 117);
    customRegularFuncMap.put(737, 31);
    customRegularFuncMap.put(179, 5);
    customRegularFuncMap.put(183, 5);
    customRegularFuncMap.put(740, 5);
    customRegularFuncMap.put(741, 119);
    customRegularFuncMap.put(744, 120);
    customRegularFuncMap.put(748, 122);
    customRegularFuncMap.put(749, 123);
    customRegularFuncMap.put(750, 8);
    customRegularFuncMap.put(751, 124);
    customRegularFuncMap.put(752, 125);
    customRegularFuncMap.put(753, 5);
    customRegularFuncMap.put(754, 126);
    customRegularFuncMap.put(755, 127);
    customRegularFuncMap.put(756, 128);
    customRegularFuncMap.put(757, 129);
    customRegularFuncMap.put(758, 130);
    customRegularFuncMap.put(759, 131);
    customRegularFuncMap.put(760, 132);
    customRegularFuncMap.put(248, 133);
    customRegularFuncMap.put(761, 134);
    customRegularFuncMap.put(762, 135);
    customRegularFuncMap.put(254, 136);
    customRegularFuncMap.put(763, 137);
    customRegularFuncMap.put(764, 138);
    customRegularFuncMap.put(766, 139);
    customRegularFuncMap.put(765, 31);
    customRegularFuncMap.put(264, 5);
    customRegularFuncMap.put(767, 5);
    customRegularFuncMap.put(768, 142);
    customRegularFuncMap.put(769, 143);
    customRegularFuncMap.put(770, 144);
    customRegularFuncMap.put(771, 145);
    customRegularFuncMap.put(772, 146);
    customRegularFuncMap.put(773, 147);
    customRegularFuncMap.put(774, 148);
    customRegularFuncMap.put(775, 150);
    customRegularFuncMap.put(776, 149);
    customRegularFuncMap.put(536, 151);
    customRegularFuncMap.put(778, 23);
    customRegularFuncMap.put(779, 153);
    customRegularFuncMap.put(780, 154);
    customRegularFuncMap.put(781, 155);
    customRegularFuncMap.put(782, 156);
    customRegularFuncMap.put(298, 157);
    customRegularFuncMap.put(783, 158);
    customRegularFuncMap.put(784, 159);
    customRegularFuncMap.put(785, 160);
    customRegularFuncMap.put(786, 161);
    customRegularFuncMap.put(787, 162);
    customRegularFuncMap.put(788, 163);
    customRegularFuncMap.put(789, 164);
    customRegularFuncMap.put(790, 165);
    customRegularFuncMap.put(791, 166);
    customRegularFuncMap.put(792, 167);
    customRegularFuncMap.put(795, 169);
    customRegularFuncMap.put(796, 170);
    customRegularFuncMap.put(797, 171);
    customRegularFuncMap.put(303, 172);
    customRegularFuncMap.put(798, 173);
    customRegularFuncMap.put(311, 174);
    customRegularFuncMap.put(800, 27);
    customRegularFuncMap.put(801, 177);
    customRegularFuncMap.put(802, 176);
    customRegularFuncMap.put(803, 175);
    customRegularFuncMap.put(804, 5);
    customRegularFuncMap.put(340, 5);
    customValueFuncMap = new IntKeyIntValueHashMap();
    customValueFuncMap.put(794, 43);
    customValueFuncMap.put(747, 52);
  }
}


/* Location:              E:\java\java学习\hutubill\lib\all.jar!\org\hsqldb\FunctionCustom.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */
package org.hsqldb;

import org.hsqldb.error.Error;
import org.hsqldb.index.Index;
import org.hsqldb.lib.HashMappedList;
import org.hsqldb.lib.Iterator;
import org.hsqldb.lib.OrderedHashSet;
import org.hsqldb.persist.DataFileCache;
import org.hsqldb.persist.DataSpaceManager;
import org.hsqldb.persist.HsqlDatabaseProperties;
import org.hsqldb.persist.HsqlProperties;
import org.hsqldb.persist.LobManager;
import org.hsqldb.persist.Logger;
import org.hsqldb.persist.PersistentStore;
import org.hsqldb.persist.TableSpaceManager;
import org.hsqldb.result.Result;
import org.hsqldb.result.ResultMetaData;
import org.hsqldb.rights.Grantee;
import org.hsqldb.rights.GranteeManager;
import org.hsqldb.rights.User;
import org.hsqldb.rights.UserManager;
import org.hsqldb.scriptio.ScriptWriterText;
import org.hsqldb.types.Collation;

public class StatementCommand
  extends Statement
{
  Object[] parameters;
  
  StatementCommand(int paramInt, Object[] paramArrayOfObject)
  {
    this(paramInt, paramArrayOfObject, null, null);
  }
  
  StatementCommand(int paramInt, Object[] paramArrayOfObject, HsqlNameManager.HsqlName[] paramArrayOfHsqlName1, HsqlNameManager.HsqlName[] paramArrayOfHsqlName2)
  {
    super(paramInt);
    this.isTransactionStatement = true;
    this.parameters = paramArrayOfObject;
    if (paramArrayOfHsqlName1 != null) {
      this.readTableNames = paramArrayOfHsqlName1;
    }
    if (paramArrayOfHsqlName2 != null) {
      this.writeTableNames = paramArrayOfHsqlName2;
    }
    switch (paramInt)
    {
    case 1215: 
      this.group = 2014;
      break;
    case 1151: 
      this.group = 2009;
      this.statementReturnType = 2;
      this.isTransactionStatement = false;
      this.isLogged = false;
      break;
    case 1002: 
      this.group = 2014;
      this.isLogged = false;
      break;
    case 1004: 
      String str = (String)this.parameters[0];
      if (str == null) {
        this.statementReturnType = 2;
      }
      this.group = 2014;
      this.isLogged = false;
      break;
    case 1001: 
      this.group = 2014;
      if (paramArrayOfHsqlName2.length == 0) {
        this.group = 2015;
      }
      this.isLogged = false;
      break;
    case 1016: 
    case 1032: 
    case 1033: 
    case 1055: 
      this.isTransactionStatement = false;
      this.group = 2013;
      break;
    case 1011: 
    case 1012: 
    case 1013: 
    case 1015: 
    case 1017: 
    case 1018: 
    case 1020: 
    case 1021: 
    case 1022: 
    case 1025: 
    case 1026: 
    case 1031: 
    case 1034: 
    case 1035: 
    case 1036: 
    case 1037: 
    case 1039: 
    case 1040: 
    case 1046: 
    case 1047: 
    case 1049: 
    case 1050: 
    case 1051: 
    case 1052: 
    case 1053: 
    case 1054: 
    case 1056: 
      this.group = 2013;
      break;
    case 1014: 
      this.group = 2013;
      this.isLogged = false;
      break;
    case 1158: 
    case 1159: 
    case 1160: 
      this.group = 2012;
      break;
    case 1156: 
      this.group = 2012;
      this.isLogged = false;
      break;
    case 1155: 
      this.group = 2012;
      break;
    case 1154: 
      this.group = 2012;
      break;
    case 1003: 
      this.group = 2014;
      this.isTransactionStatement = false;
      this.isLogged = false;
      break;
    case 1157: 
      this.group = 2012;
      break;
    case 1153: 
      this.group = 2013;
      this.isTransactionStatement = false;
      this.isLogged = false;
      break;
    case 1091: 
    case 1092: 
    case 1093: 
      this.group = 2013;
      this.isTransactionStatement = false;
      break;
    case 1005: 
      this.group = 2011;
      this.isTransactionStatement = false;
      this.isLogged = false;
      break;
    case 1006: 
    case 1007: 
    case 1008: 
    case 1009: 
    case 1010: 
    case 1019: 
    case 1023: 
    case 1024: 
    case 1027: 
    case 1028: 
    case 1029: 
    case 1030: 
    case 1038: 
    case 1041: 
    case 1042: 
    case 1043: 
    case 1044: 
    case 1045: 
    case 1048: 
    case 1057: 
    case 1058: 
    case 1059: 
    case 1060: 
    case 1061: 
    case 1062: 
    case 1063: 
    case 1064: 
    case 1065: 
    case 1066: 
    case 1067: 
    case 1068: 
    case 1069: 
    case 1070: 
    case 1071: 
    case 1072: 
    case 1073: 
    case 1074: 
    case 1075: 
    case 1076: 
    case 1077: 
    case 1078: 
    case 1079: 
    case 1080: 
    case 1081: 
    case 1082: 
    case 1083: 
    case 1084: 
    case 1085: 
    case 1086: 
    case 1087: 
    case 1088: 
    case 1089: 
    case 1090: 
    case 1094: 
    case 1095: 
    case 1096: 
    case 1097: 
    case 1098: 
    case 1099: 
    case 1100: 
    case 1101: 
    case 1102: 
    case 1103: 
    case 1104: 
    case 1105: 
    case 1106: 
    case 1107: 
    case 1108: 
    case 1109: 
    case 1110: 
    case 1111: 
    case 1112: 
    case 1113: 
    case 1114: 
    case 1115: 
    case 1116: 
    case 1117: 
    case 1118: 
    case 1119: 
    case 1120: 
    case 1121: 
    case 1122: 
    case 1123: 
    case 1124: 
    case 1125: 
    case 1126: 
    case 1127: 
    case 1128: 
    case 1129: 
    case 1130: 
    case 1131: 
    case 1132: 
    case 1133: 
    case 1134: 
    case 1135: 
    case 1136: 
    case 1137: 
    case 1138: 
    case 1139: 
    case 1140: 
    case 1141: 
    case 1142: 
    case 1143: 
    case 1144: 
    case 1145: 
    case 1146: 
    case 1147: 
    case 1148: 
    case 1149: 
    case 1150: 
    case 1152: 
    case 1161: 
    case 1162: 
    case 1163: 
    case 1164: 
    case 1165: 
    case 1166: 
    case 1167: 
    case 1168: 
    case 1169: 
    case 1170: 
    case 1171: 
    case 1172: 
    case 1173: 
    case 1174: 
    case 1175: 
    case 1176: 
    case 1177: 
    case 1178: 
    case 1179: 
    case 1180: 
    case 1181: 
    case 1182: 
    case 1183: 
    case 1184: 
    case 1185: 
    case 1186: 
    case 1187: 
    case 1188: 
    case 1189: 
    case 1190: 
    case 1191: 
    case 1192: 
    case 1193: 
    case 1194: 
    case 1195: 
    case 1196: 
    case 1197: 
    case 1198: 
    case 1199: 
    case 1200: 
    case 1201: 
    case 1202: 
    case 1203: 
    case 1204: 
    case 1205: 
    case 1206: 
    case 1207: 
    case 1208: 
    case 1209: 
    case 1210: 
    case 1211: 
    case 1212: 
    case 1213: 
    case 1214: 
    default: 
      throw Error.runtimeError(201, "StatementCommand");
    }
  }
  
  public Result execute(Session paramSession)
  {
    Result localResult;
    try
    {
      localResult = getResult(paramSession);
    }
    catch (Throwable localThrowable1)
    {
      localResult = Result.newErrorResult(localThrowable1, getSQL());
    }
    if (localResult.isError())
    {
      localResult.getException().setStatementType(this.group, this.type);
      return localResult;
    }
    try
    {
      if (this.isLogged) {
        paramSession.database.logger.writeOtherStatement(paramSession, this.sql);
      }
    }
    catch (Throwable localThrowable2)
    {
      return Result.newErrorResult(localThrowable2, getSQL());
    }
    return localResult;
  }
  
  Result getResult(Session paramSession)
  {
    if (this.isExplain) {
      return Result.newSingleColumnStringResult("OPERATION", describe(paramSession));
    }
    Object localObject1;
    boolean bool1;
    boolean bool8;
    boolean bool9;
    Object localObject3;
    Object localObject5;
    Object localObject8;
    Object localObject10;
    TableSpaceManager localTableSpaceManager;
    Object localObject12;
    Object localObject6;
    Object localObject9;
    boolean bool18;
    Object localObject4;
    Object localObject11;
    User localUser1;
    Object localObject7;
    switch (this.type)
    {
    case 1215: 
      return getTruncateResult(paramSession);
    case 1151: 
      localObject1 = (Statement)this.parameters[0];
      return Result.newSingleColumnStringResult("OPERATION", ((Statement)localObject1).describe(paramSession));
    case 1001: 
      localObject1 = (String)this.parameters[0];
      boolean bool7 = ((Boolean)this.parameters[1]).booleanValue();
      boolean bool11 = ((Boolean)this.parameters[2]).booleanValue();
      boolean bool15 = ((Boolean)this.parameters[3]).booleanValue();
      boolean bool17 = ((Boolean)this.parameters[4]).booleanValue();
      try
      {
        paramSession.checkAdmin();
        if (paramSession.database.getType() != DatabaseType.DB_FILE) {
          throw Error.error(459);
        }
        if (paramSession.database.isFilesReadOnly()) {
          throw Error.error(455);
        }
        if (paramSession.database.logger.isStoredFileAccess()) {
          throw Error.error(457);
        }
        paramSession.database.logger.backup((String)localObject1, bool11, bool7, bool15, bool17);
        return Result.updateZeroResult;
      }
      catch (HsqlException localHsqlException42)
      {
        return Result.newErrorResult(localHsqlException42, this.sql);
      }
    case 1002: 
      bool1 = ((Boolean)this.parameters[0]).booleanValue();
      try
      {
        paramSession.checkAdmin();
        paramSession.checkDDLWrite();
        paramSession.database.logger.checkpoint(paramSession, bool1, true);
        return Result.updateZeroResult;
      }
      catch (HsqlException localHsqlException39)
      {
        return Result.newErrorResult(localHsqlException39, this.sql);
      }
    case 1011: 
      try
      {
        bool1 = ((Boolean)this.parameters[0]).booleanValue();
        paramSession.checkAdmin();
        paramSession.checkDDLWrite();
        paramSession.database.logger.setIncrementBackup(bool1);
        return Result.updateZeroResult;
      }
      catch (HsqlException localHsqlException1)
      {
        return Result.newErrorResult(localHsqlException1, this.sql);
      }
    case 1012: 
      try
      {
        int i = ((Integer)this.parameters[0]).intValue();
        bool8 = this.parameters[1] == null;
        paramSession.checkAdmin();
        paramSession.checkDDLWrite();
        if (bool8) {
          i = paramSession.database.getProperties().getPropertyWithinRange("hsqldb.cache_rows", i);
        }
        paramSession.database.logger.setCacheMaxRows(i);
        return Result.updateZeroResult;
      }
      catch (HsqlException localHsqlException2)
      {
        return Result.newErrorResult(localHsqlException2, this.sql);
      }
    case 1013: 
      try
      {
        int j = ((Integer)this.parameters[0]).intValue();
        bool8 = this.parameters[1] == null;
        paramSession.checkAdmin();
        paramSession.checkDDLWrite();
        if (bool8) {
          j = paramSession.database.getProperties().getPropertyWithinRange("hsqldb.cache_size", j);
        }
        paramSession.database.logger.setCacheSize(j);
        return Result.updateZeroResult;
      }
      catch (HsqlException localHsqlException3)
      {
        return Result.newErrorResult(localHsqlException3, this.sql);
      }
    case 1014: 
      try
      {
        long l1 = ((Long)this.parameters[0]).longValue();
        long l3 = ((Long)this.parameters[1]).longValue();
        paramSession.checkAdmin();
        paramSession.checkDDLWrite();
        if (paramSession.isProcessingScript()) {
          paramSession.database.logger.setFilesTimestamp(l1);
        }
        return Result.updateZeroResult;
      }
      catch (HsqlException localHsqlException4)
      {
        return Result.newErrorResult(localHsqlException4, this.sql);
      }
    case 1017: 
      try
      {
        int k = ((Integer)this.parameters[0]).intValue();
        paramSession.checkAdmin();
        paramSession.checkDDLWrite();
        if (paramSession.isProcessingScript()) {
          paramSession.database.logger.setLobFileScaleNoCheck(k);
        } else {
          paramSession.database.logger.setLobFileScale(k);
        }
        return Result.updateZeroResult;
      }
      catch (HsqlException localHsqlException5)
      {
        return Result.newErrorResult(localHsqlException5, this.sql);
      }
    case 1018: 
      try
      {
        boolean bool2 = ((Boolean)this.parameters[0]).booleanValue();
        paramSession.checkAdmin();
        paramSession.checkDDLWrite();
        if (paramSession.isProcessingScript()) {
          paramSession.database.logger.setLobFileCompressedNoCheck(bool2);
        } else {
          paramSession.database.logger.setLobFileCompressed(bool2);
        }
        return Result.updateZeroResult;
      }
      catch (HsqlException localHsqlException6)
      {
        return Result.newErrorResult(localHsqlException6, this.sql);
      }
    case 1025: 
      try
      {
        int m = ((Integer)this.parameters[0]).intValue();
        paramSession.checkAdmin();
        paramSession.checkDDLWrite();
        if (paramSession.isProcessingScript()) {
          paramSession.database.logger.setDataFileScaleNoCheck(m);
        } else {
          paramSession.database.logger.setDataFileScale(m);
        }
        return Result.updateZeroResult;
      }
      catch (HsqlException localHsqlException7)
      {
        return Result.newErrorResult(localHsqlException7, this.sql);
      }
    case 1031: 
      try
      {
        paramSession.checkAdmin();
        paramSession.checkDDLWrite();
        if (paramSession.database.getType() == DatabaseType.DB_RES) {
          return Result.updateZeroResult;
        }
        if (paramSession.database.isFilesReadOnly()) {
          return Result.updateZeroResult;
        }
        if ((this.parameters[0] instanceof Boolean))
        {
          boolean bool3 = ((Boolean)this.parameters[0]).booleanValue();
          paramSession.database.logger.setDataFileSpaces(bool3);
        }
        else
        {
          int n = ((Integer)this.parameters[0]).intValue();
          paramSession.database.logger.setDataFileSpaces(n);
        }
        return Result.updateZeroResult;
      }
      catch (HsqlException localHsqlException8)
      {
        return Result.newErrorResult(localHsqlException8, this.sql);
      }
    case 1015: 
      try
      {
        int i1 = ((Integer)this.parameters[0]).intValue();
        paramSession.checkAdmin();
        paramSession.checkDDLWrite();
        if (!paramSession.database.getProperties().validateProperty("hsqldb.defrag_limit", i1)) {
          throw Error.error(5556);
        }
        paramSession.database.logger.setDefagLimit(i1);
        return Result.updateZeroResult;
      }
      catch (HsqlException localHsqlException9)
      {
        return Result.newErrorResult(localHsqlException9, this.sql);
      }
    case 1016: 
      try
      {
        int i2 = ((Integer)this.parameters[0]).intValue();
        bool8 = ((Boolean)this.parameters[1]).booleanValue();
        paramSession.checkAdmin();
        paramSession.checkDDLWrite();
        paramSession.database.logger.setEventLogLevel(i2, bool8);
        return Result.updateZeroResult;
      }
      catch (HsqlException localHsqlException10)
      {
        return Result.newErrorResult(localHsqlException10, this.sql);
      }
    case 1022: 
      try
      {
        paramSession.checkAdmin();
        paramSession.checkDDLWrite();
        Object localObject2 = this.parameters[0];
        if ((localObject2 instanceof Boolean))
        {
          bool8 = ((Boolean)this.parameters[0]).booleanValue();
          paramSession.database.logger.setNioDataFile(bool8);
        }
        else
        {
          int i11 = ((Integer)this.parameters[0]).intValue();
          paramSession.database.logger.setNioMaxSize(i11);
        }
        return Result.updateZeroResult;
      }
      catch (HsqlException localHsqlException11)
      {
        return Result.newErrorResult(localHsqlException11, this.sql);
      }
    case 1020: 
      try
      {
        boolean bool4 = ((Boolean)this.parameters[0]).booleanValue();
        paramSession.checkAdmin();
        paramSession.checkDDLWrite();
        paramSession.database.logger.setLogData(bool4);
        return Result.updateZeroResult;
      }
      catch (HsqlException localHsqlException12)
      {
        return Result.newErrorResult(localHsqlException12, this.sql);
      }
    case 1021: 
      try
      {
        int i3 = ((Integer)this.parameters[0]).intValue();
        paramSession.checkAdmin();
        paramSession.checkDDLWrite();
        paramSession.database.logger.setLogSize(i3);
        return Result.updateZeroResult;
      }
      catch (HsqlException localHsqlException13)
      {
        return Result.newErrorResult(localHsqlException13, this.sql);
      }
    case 1032: 
      try
      {
        String str1 = (String)this.parameters[0];
        paramSession.checkAdmin();
        paramSession.checkDDLWrite();
        return Result.updateZeroResult;
      }
      catch (HsqlException localHsqlException14)
      {
        return Result.newErrorResult(localHsqlException14, this.sql);
      }
    case 1026: 
      try
      {
        int i4 = ((Integer)this.parameters[0]).intValue();
        paramSession.checkAdmin();
        paramSession.checkDDLWrite();
        paramSession.database.logger.setScriptType(i4);
        return Result.updateZeroResult;
      }
      catch (HsqlException localHsqlException15)
      {
        return Result.newErrorResult(localHsqlException15, this.sql);
      }
    case 1033: 
      try
      {
        int i5 = ((Integer)this.parameters[0]).intValue();
        paramSession.checkAdmin();
        paramSession.checkDDLWrite();
        paramSession.database.logger.setWriteDelay(i5);
        return Result.updateZeroResult;
      }
      catch (HsqlException localHsqlException16)
      {
        return Result.newErrorResult(localHsqlException16, this.sql);
      }
    case 1036: 
      try
      {
        Routine localRoutine1 = (Routine)this.parameters[0];
        paramSession.checkAdmin();
        paramSession.checkDDLWrite();
        paramSession.database.userManager.setExtAuthenticationFunction(localRoutine1);
        return Result.updateZeroResult;
      }
      catch (HsqlException localHsqlException17)
      {
        return Result.newErrorResult(localHsqlException17, this.sql);
      }
    case 1040: 
      try
      {
        Routine localRoutine2 = (Routine)this.parameters[0];
        paramSession.checkAdmin();
        paramSession.checkDDLWrite();
        paramSession.database.userManager.setPasswordCheckFunction(localRoutine2);
        return Result.updateZeroResult;
      }
      catch (HsqlException localHsqlException18)
      {
        return Result.newErrorResult(localHsqlException18, this.sql);
      }
    case 1056: 
      try
      {
        String str2 = (String)this.parameters[0];
        paramSession.checkAdmin();
        paramSession.checkDDLWrite();
        if (!paramSession.isProcessingScript()) {
          return Result.updateZeroResult;
        }
        paramSession.database.granteeManager.setDigestAlgo(str2);
        return Result.updateZeroResult;
      }
      catch (HsqlException localHsqlException19)
      {
        return Result.newErrorResult(localHsqlException19, this.sql);
      }
    case 1047: 
      try
      {
        String str3 = (String)this.parameters[0];
        bool9 = ((Boolean)this.parameters[1]).booleanValue();
        paramSession.checkAdmin();
        paramSession.checkDDLWrite();
        paramSession.database.collation.setCollation(str3, bool9);
        paramSession.database.schemaManager.setSchemaChangeTimestamp();
        return Result.updateZeroResult;
      }
      catch (HsqlException localHsqlException20)
      {
        return Result.newErrorResult(localHsqlException20, this.sql);
      }
    case 1049: 
      boolean bool5 = ((Boolean)this.parameters[0]).booleanValue();
      paramSession.checkAdmin();
      paramSession.checkDDLWrite();
      paramSession.database.setReferentialIntegrity(bool5);
      return Result.updateZeroResult;
    case 1050: 
      localObject3 = (String)this.parameters[0];
      bool9 = ((Boolean)this.parameters[1]).booleanValue();
      int i14 = ((Number)this.parameters[2]).intValue();
      paramSession.checkAdmin();
      paramSession.checkDDLWrite();
      if ("sql.live_object".equals(localObject3))
      {
        paramSession.database.setLiveObject(bool9);
      }
      else if ("sql.enforce_names".equals(localObject3))
      {
        paramSession.database.setStrictNames(bool9);
      }
      else if ("sql.regular_names".equals(localObject3))
      {
        paramSession.database.setRegularNames(bool9);
      }
      else if ("sql.enforce_size".equals(localObject3))
      {
        paramSession.database.setStrictColumnSize(bool9);
      }
      else if ("sql.enforce_types".equals(localObject3))
      {
        paramSession.database.setStrictTypes(bool9);
      }
      else if ("sql.enforce_refs".equals(localObject3))
      {
        paramSession.database.setStrictReferences(bool9);
      }
      else if ("sql.enforce_tdc_delete".equals(localObject3))
      {
        paramSession.database.setStrictTDCD(bool9);
      }
      else if ("sql.enforce_tdc_update".equals(localObject3))
      {
        paramSession.database.setStrictTDCU(bool9);
      }
      else if ("jdbc.translate_tti_types".equals(localObject3))
      {
        paramSession.database.setTranslateTTI(bool9);
      }
      else if ("sql.char_literal".equals(localObject3))
      {
        paramSession.database.setCharacterLiteral(bool9);
      }
      else if ("sql.concat_nulls".equals(localObject3))
      {
        paramSession.database.setConcatNulls(bool9);
      }
      else if ("sql.nulls_first".equals(localObject3))
      {
        paramSession.database.setNullsFirst(bool9);
      }
      else if ("sql.nulls_order".equals(localObject3))
      {
        paramSession.database.setNullsOrder(bool9);
      }
      else if ("sql.unique_nulls".equals(localObject3))
      {
        paramSession.database.setUniqueNulls(bool9);
      }
      else if ("sql.convert_trunc".equals(localObject3))
      {
        paramSession.database.setConvertTrunc(bool9);
      }
      else if ("sql.avg_scale".equals(localObject3))
      {
        paramSession.database.setAvgScale(i14);
      }
      else if ("sql.double_nan".equals(localObject3))
      {
        paramSession.database.setDoubleNaN(bool9);
      }
      else if ("sql.longvar_is_lob".equals(localObject3))
      {
        paramSession.database.setLongVarIsLob(bool9);
      }
      else if ("sql.ignore_case".equals(localObject3))
      {
        paramSession.database.setIgnoreCase(bool9);
        paramSession.setIgnoreCase(bool9);
      }
      else if ("sql.syntax_db2".equals(localObject3))
      {
        paramSession.database.setSyntaxDb2(bool9);
      }
      else if ("sql.syntax_mss".equals(localObject3))
      {
        paramSession.database.setSyntaxMss(bool9);
      }
      else if ("sql.syntax_mys".equals(localObject3))
      {
        paramSession.database.setSyntaxMys(bool9);
      }
      else if ("sql.syntax_ora".equals(localObject3))
      {
        paramSession.database.setSyntaxOra(bool9);
      }
      else if ("sql.syntax_pgs".equals(localObject3))
      {
        paramSession.database.setSyntaxPgs(bool9);
      }
      return Result.updateZeroResult;
    case 1034: 
      localObject3 = (HsqlNameManager.HsqlName)this.parameters[0];
      paramSession.checkAdmin();
      paramSession.checkDDLWrite();
      paramSession.database.schemaManager.setDefaultSchemaHsqlName((HsqlNameManager.HsqlName)localObject3);
      paramSession.database.schemaManager.setSchemaChangeTimestamp();
      return Result.updateZeroResult;
    case 1035: 
      localObject3 = (Integer)this.parameters[0];
      paramSession.checkAdmin();
      paramSession.checkDDLWrite();
      paramSession.database.schemaManager.setDefaultTableType(((Integer)localObject3).intValue());
      return Result.updateZeroResult;
    case 1052: 
      try
      {
        int i6 = ((Integer)this.parameters[0]).intValue();
        paramSession.checkAdmin();
        paramSession.database.txManager.setTransactionControl(paramSession, i6);
        return Result.updateZeroResult;
      }
      catch (HsqlException localHsqlException21)
      {
        return Result.newErrorResult(localHsqlException21, this.sql);
      }
    case 1053: 
      try
      {
        int i7 = ((Integer)this.parameters[0]).intValue();
        paramSession.checkAdmin();
        paramSession.database.defaultIsolationLevel = i7;
        return Result.updateZeroResult;
      }
      catch (HsqlException localHsqlException22)
      {
        return Result.newErrorResult(localHsqlException22, this.sql);
      }
    case 1054: 
      try
      {
        boolean bool6 = ((Boolean)this.parameters[0]).booleanValue();
        paramSession.checkAdmin();
        paramSession.database.txConflictRollback = bool6;
        return Result.updateZeroResult;
      }
      catch (HsqlException localHsqlException23)
      {
        return Result.newErrorResult(localHsqlException23, this.sql);
      }
    case 1037: 
      try
      {
        int i8 = ((Integer)this.parameters[0]).intValue();
        paramSession.checkAdmin();
        org.hsqldb.lib.java.JavaSystem.gcFrequency = i8;
        return Result.updateZeroResult;
      }
      catch (HsqlException localHsqlException24)
      {
        return Result.newErrorResult(localHsqlException24, this.sql);
      }
    case 1039: 
      try
      {
        String str4 = (String)this.parameters[0];
        localObject5 = this.parameters[1];
        paramSession.checkAdmin();
        paramSession.checkDDLWrite();
        return Result.updateZeroResult;
      }
      catch (HsqlException localHsqlException25)
      {
        return Result.newErrorResult(localHsqlException25, this.sql);
      }
    case 1046: 
      int i9 = ((Integer)this.parameters[0]).intValue();
      paramSession.checkAdmin();
      paramSession.database.setResultMaxMemoryRows(i9);
      return Result.updateZeroResult;
    case 1051: 
      try
      {
        String str5 = (String)this.parameters[0];
        localObject5 = null;
        paramSession.checkAdmin();
        if (str5.length() > 0)
        {
          localObject5 = HsqlProperties.delimitedArgPairsToProps(str5, "=", ";", null);
          if (((HsqlProperties)localObject5).getErrorKeys().length > 0) {
            throw Error.error(482, localObject5.getErrorKeys()[0]);
          }
          paramSession.database.logger.setDefaultTextTableProperties(str5, (HsqlProperties)localObject5);
        }
        return Result.updateZeroResult;
      }
      catch (HsqlException localHsqlException26)
      {
        return Result.newErrorResult(localHsqlException26, this.sql);
      }
    case 1055: 
      try
      {
        String str6 = (String)this.parameters[0];
        paramSession.checkAdmin();
        paramSession.database.setDatabaseName(str6);
        return Result.updateZeroResult;
      }
      catch (HsqlException localHsqlException27)
      {
        return Result.newErrorResult(localHsqlException27, this.sql);
      }
    case 1004: 
      ScriptWriterText localScriptWriterText = null;
      localObject5 = (String)this.parameters[0];
      try
      {
        paramSession.checkAdmin();
        if (localObject5 == null) {
          return paramSession.database.getScript(false);
        }
        localScriptWriterText = new ScriptWriterText(paramSession.database, (String)localObject5, true, true, true);
        localScriptWriterText.writeAll();
        localScriptWriterText.close();
        return Result.updateZeroResult;
      }
      catch (HsqlException localHsqlException40)
      {
        return Result.newErrorResult(localHsqlException40, this.sql);
      }
    case 1003: 
      try
      {
        int i10 = ((Integer)this.parameters[0]).intValue();
        paramSession.checkAdmin();
        paramSession.database.close(i10);
        return Result.updateZeroResult;
      }
      catch (HsqlException localHsqlException28)
      {
        return Result.newErrorResult(localHsqlException28, this.sql);
      }
    case 1006: 
      try
      {
        HsqlNameManager.HsqlName localHsqlName1 = (HsqlNameManager.HsqlName)this.parameters[0];
        localObject5 = paramSession.database.schemaManager.getUserTable(localHsqlName1.name, localHsqlName1.schema.name);
        return Result.updateZeroResult;
      }
      catch (HsqlException localHsqlException29)
      {
        return Result.newErrorResult(localHsqlException29, this.sql);
      }
    case 1159: 
      try
      {
        HsqlNameManager.HsqlName localHsqlName2 = (HsqlNameManager.HsqlName)this.parameters[0];
        localObject5 = paramSession.database.schemaManager.getUserTable(localHsqlName2.name, localHsqlName2.schema.name);
        paramSession.checkAdmin();
        paramSession.checkDDLWrite();
        if (!paramSession.database.logger.isFileDatabase()) {
          return Result.updateZeroResult;
        }
        if (paramSession.database.logger.getDataFileSpaces() == 0) {
          throw Error.error(457);
        }
        if (((Table)localObject5).getSpaceID() != 7) {
          return Result.updateZeroResult;
        }
        localObject8 = paramSession.database.logger.getCache();
        if (localObject8 == null) {
          return Result.updateZeroResult;
        }
        localObject10 = ((DataFileCache)localObject8).spaceManager;
        int i16 = ((DataSpaceManager)localObject10).getNewTableSpaceID();
        ((Table)localObject5).setSpaceID(i16);
        if (!((Table)localObject5).isCached()) {
          return Result.updateZeroResult;
        }
        localTableSpaceManager = ((DataSpaceManager)localObject10).getTableSpace(i16);
        PersistentStore localPersistentStore = ((Table)localObject5).getRowStore(paramSession);
        localPersistentStore.setSpaceManager(localTableSpaceManager);
        return Result.updateZeroResult;
      }
      catch (HsqlException localHsqlException30)
      {
        return Result.newErrorResult(localHsqlException30, this.sql);
      }
    case 1160: 
      try
      {
        HsqlNameManager.HsqlName localHsqlName3 = (HsqlNameManager.HsqlName)this.parameters[0];
        int i12 = ((Integer)this.parameters[1]).intValue();
        localObject8 = paramSession.database.schemaManager.getUserTable(localHsqlName3.name, localHsqlName3.schema.name);
        if (!paramSession.isProcessingScript()) {
          return Result.updateZeroResult;
        }
        if (((Table)localObject8).getTableType() != 5) {
          return Result.updateZeroResult;
        }
        if (((Table)localObject8).getSpaceID() != 7) {
          return Result.updateZeroResult;
        }
        ((Table)localObject8).setSpaceID(i12);
        if (((Table)localObject8).store == null) {
          return Result.updateZeroResult;
        }
        localObject10 = paramSession.database.logger.getCache();
        if (localObject10 == null) {
          return Result.updateZeroResult;
        }
        localObject12 = ((DataFileCache)localObject10).spaceManager;
        localTableSpaceManager = ((DataSpaceManager)localObject12).getTableSpace(((Table)localObject8).getSpaceID());
        ((Table)localObject8).store.setSpaceManager(localTableSpaceManager);
        return Result.updateZeroResult;
      }
      catch (HsqlException localHsqlException31)
      {
        return Result.newErrorResult(localHsqlException31, this.sql);
      }
    case 1158: 
      try
      {
        HsqlNameManager.HsqlName localHsqlName4 = (HsqlNameManager.HsqlName)this.parameters[0];
        localObject6 = (int[])this.parameters[1];
        localObject8 = paramSession.database.schemaManager.getUserTable(localHsqlName4.name, localHsqlName4.schema.name);
        StatementSchema.checkSchemaUpdateAuthorisation(paramSession, ((Table)localObject8).getSchemaName());
        if ((!((Table)localObject8).isCached()) && (!((Table)localObject8).isText())) {
          throw Error.error(457);
        }
        localObject10 = ((Table)localObject8).getIndexForColumns(paramSession, (int[])localObject6);
        if (localObject10 != null)
        {
          localObject12 = ((Table)localObject8).getIndexList();
          for (int i17 = 0; i17 < localObject12.length; i17++) {
            localObject12[i17].setClustered(false);
          }
          ((Index)localObject10).setClustered(true);
        }
        return Result.updateZeroResult;
      }
      catch (HsqlException localHsqlException32)
      {
        return Result.newErrorResult(localHsqlException32, this.sql);
      }
    case 1153: 
      try
      {
        HsqlNameManager.HsqlName localHsqlName5 = (HsqlNameManager.HsqlName)this.parameters[0];
        localObject6 = (String)this.parameters[1];
        localObject8 = (Integer)this.parameters[2];
        localObject10 = paramSession.database.schemaManager.getUserTable(localHsqlName5.name, localHsqlName5.schema.name);
        if (paramSession.isProcessingScript()) {
          ((Table)localObject10).setIndexRoots(paramSession, (String)localObject6);
        }
        return Result.updateZeroResult;
      }
      catch (HsqlException localHsqlException33)
      {
        return Result.newErrorResult(localHsqlException33, this.sql);
      }
    case 1154: 
      try
      {
        HsqlNameManager.HsqlName localHsqlName6 = (HsqlNameManager.HsqlName)this.parameters[0];
        localObject6 = paramSession.database.schemaManager.getUserTable(localHsqlName6.name, localHsqlName6.schema.name);
        boolean bool12 = ((Boolean)this.parameters[1]).booleanValue();
        StatementSchema.checkSchemaUpdateAuthorisation(paramSession, ((Table)localObject6).getSchemaName());
        ((Table)localObject6).setDataReadOnly(bool12);
        paramSession.database.schemaManager.setSchemaChangeTimestamp();
        return Result.updateZeroResult;
      }
      catch (HsqlException localHsqlException34)
      {
        return Result.newErrorResult(localHsqlException34, this.sql);
      }
    case 1155: 
    case 1156: 
      try
      {
        HsqlNameManager.HsqlName localHsqlName7 = (HsqlNameManager.HsqlName)this.parameters[0];
        localObject6 = paramSession.database.schemaManager.getUserTable(localHsqlName7.name, localHsqlName7.schema.name);
        StatementSchema.checkSchemaUpdateAuthorisation(paramSession, ((Table)localObject6).getSchemaName());
        if (!((Table)localObject6).isText())
        {
          HsqlException localHsqlException41 = Error.error(321);
          return Result.newErrorResult(localHsqlException41, this.sql);
        }
        if (this.parameters[1] != null)
        {
          boolean bool13 = ((Boolean)this.parameters[1]).booleanValue();
          if (bool13) {
            ((TextTable)localObject6).connect(paramSession);
          } else {
            ((TextTable)localObject6).disconnect();
          }
          paramSession.database.schemaManager.setSchemaChangeTimestamp();
          return Result.updateZeroResult;
        }
        localObject9 = (String)this.parameters[2];
        boolean bool16 = ((Boolean)this.parameters[3]).booleanValue();
        bool18 = ((Boolean)this.parameters[4]).booleanValue();
        if (bool18) {
          ((TextTable)localObject6).setHeader((String)localObject9);
        } else {
          ((TextTable)localObject6).setDataSource(paramSession, (String)localObject9, bool16, false);
        }
        return Result.updateZeroResult;
      }
      catch (Throwable localThrowable)
      {
        if (!(localThrowable instanceof HsqlException)) {
          localObject4 = Error.error(467, localThrowable.toString());
        }
        if ((paramSession.isProcessingLog()) || (paramSession.isProcessingScript()))
        {
          paramSession.addWarning((HsqlException)localObject4);
          paramSession.database.logger.logWarningEvent("Problem processing SET TABLE SOURCE", (Throwable)localObject4);
          return Result.updateZeroResult;
        }
        return Result.newErrorResult((Throwable)localObject4, this.sql);
      }
    case 1157: 
      try
      {
        localObject4 = (HsqlNameManager.HsqlName)this.parameters[0];
        int i13 = ((Integer)this.parameters[1]).intValue();
        localObject9 = paramSession.database.schemaManager.getUserTable(((HsqlNameManager.HsqlName)localObject4).name, ((HsqlNameManager.HsqlName)localObject4).schema.name);
        if (((Table)localObject9).getTableType() == i13) {
          return Result.updateZeroResult;
        }
        StatementSchema.checkSchemaUpdateAuthorisation(paramSession, ((Table)localObject9).getSchemaName());
        localObject11 = new TableWorks(paramSession, (Table)localObject9);
        bool18 = ((TableWorks)localObject11).setTableType(paramSession, i13);
        if (!bool18) {
          throw Error.error(467);
        }
        paramSession.database.schemaManager.setSchemaChangeTimestamp();
        if (((HsqlNameManager.HsqlName)localObject4).schema == SqlInvariants.LOBS_SCHEMA_HSQLNAME) {
          paramSession.database.lobManager.compileStatements();
        }
        return Result.updateZeroResult;
      }
      catch (HsqlException localHsqlException35)
      {
        return Result.newErrorResult(localHsqlException35, this.sql);
      }
    case 1091: 
      localUser1 = (User)this.parameters[0];
      boolean bool10 = ((Boolean)this.parameters[1]).booleanValue();
      paramSession.checkAdmin();
      paramSession.checkDDLWrite();
      localUser1.isLocalOnly = bool10;
      paramSession.database.schemaManager.setSchemaChangeTimestamp();
      return Result.updateZeroResult;
    case 1092: 
      try
      {
        localUser1 = (User)this.parameters[0];
        localObject7 = (HsqlNameManager.HsqlName)this.parameters[1];
        paramSession.checkDDLWrite();
        if (localUser1 == null)
        {
          localUser1 = paramSession.getUser();
        }
        else
        {
          paramSession.checkAdmin();
          paramSession.checkDDLWrite();
          localUser1 = paramSession.database.userManager.get(localUser1.getName().getNameString());
        }
        if (localObject7 != null) {
          localObject7 = paramSession.database.schemaManager.getSchemaHsqlName(((HsqlNameManager.HsqlName)localObject7).name);
        }
        localUser1.setInitialSchema((HsqlNameManager.HsqlName)localObject7);
        paramSession.database.schemaManager.setSchemaChangeTimestamp();
        return Result.updateZeroResult;
      }
      catch (HsqlException localHsqlException36)
      {
        return Result.newErrorResult(localHsqlException36, this.sql);
      }
    case 1093: 
      try
      {
        User localUser2 = this.parameters[0] == null ? paramSession.getUser() : (User)this.parameters[0];
        localObject7 = (String)this.parameters[1];
        boolean bool14 = ((Boolean)this.parameters[2]).booleanValue();
        paramSession.checkDDLWrite();
        paramSession.database.userManager.setPassword(paramSession, localUser2, (String)localObject7, bool14);
        return Result.updateZeroResult;
      }
      catch (HsqlException localHsqlException37)
      {
        return Result.newErrorResult(localHsqlException37, this.sql);
      }
    case 1005: 
      try
      {
        long l2 = ((Number)this.parameters[0]).longValue();
        int i15 = ((Number)this.parameters[1]).intValue();
        localObject11 = paramSession.database.sessionManager.getSession(l2);
        if (localObject11 == null) {
          throw Error.error(4500);
        }
        switch (i15)
        {
        case 2: 
          paramSession.database.txManager.resetSession(paramSession, (Session)localObject11, 3);
          break;
        case 294: 
          paramSession.database.txManager.resetSession(paramSession, (Session)localObject11, 2);
          break;
        case 250: 
          paramSession.database.txManager.resetSession(paramSession, (Session)localObject11, 1);
          break;
        case 42: 
          paramSession.database.txManager.resetSession(paramSession, (Session)localObject11, 6);
          break;
        case 247: 
          paramSession.database.txManager.resetSession(paramSession, (Session)localObject11, 4);
          break;
        case 99: 
          paramSession.database.txManager.resetSession(paramSession, (Session)localObject11, 5);
        }
      }
      catch (HsqlException localHsqlException38)
      {
        return Result.newErrorResult(localHsqlException38, this.sql);
      }
      return Result.updateZeroResult;
    }
    throw Error.runtimeError(201, "StatementCommand");
  }
  
  Result getTruncateResult(Session paramSession)
  {
    try
    {
      HsqlNameManager.HsqlName localHsqlName1 = (HsqlNameManager.HsqlName)this.parameters[0];
      boolean bool1 = ((Boolean)this.parameters[1]).booleanValue();
      boolean bool2 = ((Boolean)this.parameters[2]).booleanValue();
      Object localObject1;
      Table[] arrayOfTable;
      Object localObject4;
      Object localObject2;
      Object localObject3;
      if (localHsqlName1.type == 3)
      {
        localObject1 = paramSession.database.schemaManager.getUserTable(localHsqlName1);
        arrayOfTable = new Table[] { localObject1 };
        paramSession.getGrantee().checkDelete((SchemaObject)localObject1);
        if (!bool2) {
          for (int j = 0; j < ((Table)localObject1).fkMainConstraints.length; j++) {
            if (localObject1.fkMainConstraints[j].getRef() != localObject1)
            {
              HsqlNameManager.HsqlName localHsqlName2 = localObject1.fkMainConstraints[j].getRef().getName();
              localObject4 = paramSession.database.schemaManager.getUserTable(localHsqlName2);
              if (!((Table)localObject4).isEmpty(paramSession)) {
                throw Error.error(8, ((Table)localObject4).getName().name);
              }
            }
          }
        }
      }
      else
      {
        paramSession.database.schemaManager.getSchemaHsqlName(localHsqlName1.name);
        localObject1 = paramSession.database.schemaManager.getTables(localHsqlName1.name);
        arrayOfTable = new Table[((HashMappedList)localObject1).size()];
        ((HashMappedList)localObject1).toValuesArray(arrayOfTable);
        StatementSchema.checkSchemaUpdateAuthorisation(paramSession, localHsqlName1);
        if (!bool2)
        {
          localObject2 = new OrderedHashSet();
          paramSession.database.schemaManager.getCascadingReferencesToSchema(localHsqlName1, (OrderedHashSet)localObject2);
          for (int k = 0; k < ((OrderedHashSet)localObject2).size(); k++)
          {
            localObject4 = (HsqlNameManager.HsqlName)((OrderedHashSet)localObject2).get(k);
            if ((((HsqlNameManager.HsqlName)localObject4).type == 5) && (((HsqlNameManager.HsqlName)localObject4).parent.type == 3))
            {
              Table localTable = paramSession.database.schemaManager.getUserTable(((HsqlNameManager.HsqlName)localObject4).parent);
              if (!localTable.isEmpty(paramSession)) {
                throw Error.error(8, localTable.getName().name);
              }
            }
          }
        }
        if (bool1)
        {
          localObject2 = paramSession.database.schemaManager.databaseObjectIterator(localHsqlName1.name, 7);
          while (((Iterator)localObject2).hasNext())
          {
            localObject3 = (NumberSequence)((Iterator)localObject2).next();
            ((NumberSequence)localObject3).reset();
          }
        }
      }
      for (int i = 0; i < arrayOfTable.length; i++)
      {
        localObject2 = arrayOfTable[i];
        localObject3 = ((Table)localObject2).getRowStore(paramSession);
        ((PersistentStore)localObject3).removeAll();
        if ((bool1) && (((Table)localObject2).identitySequence != null)) {
          ((Table)localObject2).identitySequence.reset();
        }
      }
      return Result.updateZeroResult;
    }
    catch (HsqlException localHsqlException)
    {
      return Result.newErrorResult(localHsqlException, this.sql);
    }
  }
  
  public ResultMetaData getResultMetaData()
  {
    switch (this.type)
    {
    case 1151: 
      return ResultMetaData.newSingleColumnMetaData("OPERATION");
    case 1004: 
      if (this.statementReturnType == 2) {
        return ResultMetaData.newSingleColumnMetaData("COMMANDS");
      }
      break;
    }
    return super.getResultMetaData();
  }
  
  public boolean isAutoCommitStatement()
  {
    return this.isTransactionStatement;
  }
  
  public String describe(Session paramSession)
  {
    return this.sql;
  }
}


/* Location:              E:\java\java学习\hutubill\lib\all.jar!\org\hsqldb\StatementCommand.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */
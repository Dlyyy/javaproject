package org.hsqldb;

import org.hsqldb.error.Error;
import org.hsqldb.lib.HsqlArrayList;
import org.hsqldb.lib.HsqlList;
import org.hsqldb.lib.OrderedHashSet;
import org.hsqldb.map.ValuePool;
import org.hsqldb.persist.HsqlDatabaseProperties;
import org.hsqldb.result.Result;
import org.hsqldb.result.ResultMetaData;
import org.hsqldb.rights.User;
import org.hsqldb.types.Charset;
import org.hsqldb.types.Type;

public class ParserCommand
  extends ParserDDL
{
  ParserCommand(Session paramSession, Scanner paramScanner)
  {
    super(paramSession, paramScanner);
  }
  
  Statement compileStatement(int paramInt)
  {
    Statement localStatement = compilePart(paramInt);
    if (this.token.tokenType == 914)
    {
      if (localStatement.getSchemaName() == null) {
        localStatement.setSchemaHsqlName(this.session.getCurrentSchemaHsqlName());
      }
      return localStatement;
    }
    throw unexpectedToken();
  }
  
  HsqlArrayList compileStatements(String paramString, Result paramResult)
  {
    HsqlArrayList localHsqlArrayList = new HsqlArrayList();
    Statement localStatement = null;
    reset(this.session, paramString);
    while (this.token.tokenType != 914)
    {
      try
      {
        this.lastError = null;
        localStatement = compilePart(paramResult.getExecuteProperties());
      }
      catch (HsqlException localHsqlException)
      {
        if ((this.lastError != null) && (this.lastError.getLevel() > localHsqlException.getLevel())) {
          throw this.lastError;
        }
        throw localHsqlException;
      }
      if ((!localStatement.isExplain) && (localStatement.getParametersMetaData().getColumnCount() > 0)) {
        throw Error.error(5575);
      }
      localStatement.setCompileTimestamp(this.database.txManager.getGlobalChangeTimestamp());
      localHsqlArrayList.add(localStatement);
    }
    int i = paramResult.getStatementType();
    if (i != 0)
    {
      int j = localStatement.getGroup();
      if (j == 2003)
      {
        if (i == 1) {
          throw Error.error(1253);
        }
      }
      else if (i != 2) {}
    }
    return localHsqlArrayList;
  }
  
  private Statement compilePart(int paramInt)
  {
    this.compileContext.reset();
    setParsePosition(getPosition());
    if (this.token.tokenType == 915) {
      read();
    }
    Object localObject;
    switch (this.token.tokenType)
    {
    case 265: 
    case 294: 
    case 336: 
    case 836: 
      localObject = compileCursorSpecification(RangeGroup.emptyArray, paramInt, false);
      break;
    case 324: 
      RangeGroup[] arrayOfRangeGroup = this.session.sessionContext.sessionVariableRangeGroups;
      this.compileContext.setOuterRanges(arrayOfRangeGroup);
      localObject = compileShortCursorSpecification(paramInt);
      break;
    case 145: 
      localObject = compileInsertStatement(RangeGroup.emptyArray);
      break;
    case 319: 
      localObject = compileUpdateStatement(RangeGroup.emptyArray);
      break;
    case 176: 
      localObject = compileMergeStatement(RangeGroup.emptyArray);
      break;
    case 84: 
      localObject = compileDeleteStatement(RangeGroup.emptyArray);
      break;
    case 311: 
      localObject = compileTruncateStatement();
      break;
    case 761: 
      localObject = compileInsertStatement(RangeGroup.emptyArray);
      break;
    case 28: 
      localObject = compileCallStatement(this.session.sessionContext.sessionVariableRangeGroups, false);
      break;
    case 268: 
      localObject = compileSet();
      break;
    case 128: 
      localObject = compileGetStatement(this.session.sessionContext.sessionVariableRangeGroups);
      break;
    case 281: 
      localObject = compileStartTransaction();
      break;
    case 47: 
      localObject = compileCommit();
      break;
    case 255: 
      localObject = compileRollback();
      break;
    case 260: 
      localObject = compileSavepoint();
      break;
    case 247: 
      localObject = compileReleaseSavepoint();
      break;
    case 59: 
      localObject = compileCreate();
      break;
    case 4: 
      localObject = compileAlter();
      break;
    case 93: 
      localObject = compileDrop();
      break;
    case 130: 
    case 253: 
      localObject = compileGrantOrRevoke();
      break;
    case 599: 
      localObject = compileComment();
      break;
    case 629: 
      localObject = compileLock();
      break;
    case 50: 
      localObject = compileConnect();
      break;
    case 89: 
      localObject = compileDisconnect();
      break;
    case 655: 
      localObject = compileScript();
      break;
    case 657: 
      localObject = compileShutdown();
      break;
    case 585: 
      localObject = compileBackup();
      break;
    case 595: 
      localObject = compileCheckpoint();
      break;
    case 611: 
      int i = getPosition();
      localObject = compileExplainPlan();
      ((Statement)localObject).setSQL(getLastPart(i));
      break;
    case 82: 
      localObject = compileDeclare();
      break;
    case 644: 
      localObject = compilePerform();
      break;
    default: 
      throw unexpectedToken();
    }
    switch (((Statement)localObject).type)
    {
    case 14: 
    case 66: 
    case 1093: 
    case 1151: 
      break;
    default: 
      ((Statement)localObject).setSQL(getLastPart());
    }
    if (this.token.tokenType == 841) {
      read();
    } else if (this.token.tokenType != 914) {}
    return (Statement)localObject;
  }
  
  private Statement compileDeclare()
  {
    Object localObject = compileDeclareLocalTableOrNull();
    if (localObject != null) {
      return (Statement)localObject;
    }
    ColumnSchema[] arrayOfColumnSchema = readLocalVariableDeclarationOrNull();
    if (arrayOfColumnSchema != null)
    {
      Object[] arrayOfObject = { arrayOfColumnSchema };
      localObject = new StatementSession(1127, arrayOfObject);
      return (Statement)localObject;
    }
    localObject = compileDeclareCursorOrNull(RangeGroup.emptyArray, false);
    if (localObject == null) {
      throw (this.lastError == null ? unexpectedToken() : this.lastError);
    }
    return (Statement)localObject;
  }
  
  private Statement compileScript()
  {
    String str = null;
    read();
    if (this.token.tokenType == 911) {
      str = readQuotedString();
    }
    HsqlNameManager.HsqlName[] arrayOfHsqlName = this.database.schemaManager.getCatalogAndBaseTableNames();
    Object[] arrayOfObject = { str };
    return new StatementCommand(1004, arrayOfObject, null, arrayOfHsqlName);
  }
  
  private Statement compileConnect()
  {
    String str2 = null;
    read();
    readThis(321);
    checkIsSimpleName();
    String str1 = this.token.tokenString;
    read();
    if (!this.session.isProcessingLog())
    {
      readThis(643);
      str2 = readPassword();
    }
    Expression[] arrayOfExpression = { new ExpressionValue(str1, Type.SQL_VARCHAR), new ExpressionValue(str2, Type.SQL_VARCHAR) };
    StatementSession localStatementSession = new StatementSession(81, arrayOfExpression);
    return localStatementSession;
  }
  
  private StatementCommand compileSetDefault()
  {
    read();
    Object localObject;
    Object[] arrayOfObject;
    int i;
    switch (this.token.tokenType)
    {
    case 623: 
      read();
      readThis(519);
      localObject = this.database.schemaManager.getSchemaHsqlName(this.token.tokenString);
      read();
      arrayOfObject = new Object[] { localObject };
      return new StatementCommand(1034, arrayOfObject);
    case 250: 
      read();
      readThis(635);
      readThis(259);
      localObject = readIntegerObject();
      arrayOfObject = new Object[] { localObject };
      return new StatementCommand(1046, arrayOfObject);
    case 294: 
      read();
      readThis(557);
      switch (this.token.tokenType)
      {
      case 635: 
        i = 4;
        break;
      case 593: 
        i = 5;
        break;
      default: 
        throw unexpectedToken();
      }
      read();
      arrayOfObject = new Object[] { ValuePool.getInt(i) };
      return new StatementCommand(1035, arrayOfObject);
    case 446: 
      read();
      readThis(454);
      switch (this.token.tokenType)
      {
      case 502: 
        read();
        readThis(386);
        i = 2;
        break;
      case 528: 
        read();
        i = 8;
        break;
      default: 
        throw unexpectedToken();
      }
      arrayOfObject = new Object[] { ValuePool.getInt(i) };
      return new StatementCommand(1053, arrayOfObject);
    }
    throw unexpectedToken();
  }
  
  private StatementCommand compileSetProperty()
  {
    read();
    checkIsSimpleName();
    checkIsDelimitedIdentifier();
    String str = this.token.tokenString;
    read();
    Object localObject;
    if (this.token.tokenType == 310)
    {
      localObject = Boolean.TRUE;
    }
    else if (this.token.tokenType == 114)
    {
      localObject = Boolean.FALSE;
    }
    else
    {
      checkIsValue();
      localObject = this.token.tokenValue;
    }
    read();
    Object[] arrayOfObject = { str, localObject };
    return new StatementCommand(1039, arrayOfObject);
  }
  
  private Statement compileSet()
  {
    read();
    Object localObject1;
    Object localObject3;
    Object localObject4;
    int i;
    Object localObject2;
    switch (this.token.tokenType)
    {
    case 369: 
      read();
      localObject1 = XreadValueSpecificationOrNull();
      if (localObject1 == null)
      {
        localObject3 = readSchemaName();
        localObject4 = new Object[] { localObject3 };
        return new StatementSession(72, (Object[])localObject4);
      }
      if (!((Expression)localObject1).getDataType().isCharacterType()) {
        throw Error.error(2200);
      }
      if ((((Expression)localObject1).getType() != 1) && ((((Expression)localObject1).getType() != 28) || (!((FunctionSQL)localObject1).isValueFunction()))) {
        throw Error.error(2200);
      }
      localObject3 = new Expression[] { localObject1 };
      return new StatementSession(72, (Expression[])localObject3);
    case 519: 
      read();
      localObject1 = XreadValueSpecificationOrNull();
      if (localObject1 == null)
      {
        localObject3 = readSchemaName();
        localObject4 = new Object[] { localObject3 };
        return new StatementSession(80, (Object[])localObject4);
      }
      if (!((Expression)localObject1).getDataType().isCharacterType()) {
        throw Error.error(2200);
      }
      if ((((Expression)localObject1).getType() != 1) && ((((Expression)localObject1).getType() != 28) || (!((FunctionSQL)localObject1).isValueFunction()))) {
        throw Error.error(2200);
      }
      localObject3 = new Expression[] { localObject1 };
      return new StatementSession(80, (Expression[])localObject3);
    case 190: 
      read();
      readThis(379);
      localObject1 = null;
      if (readIfThis(120))
      {
        localObject1 = new HsqlArrayList();
        for (;;)
        {
          localObject3 = readSchemaObjectName(14);
          ((HsqlArrayList)localObject1).add(localObject3);
          if (this.token.tokenType != 824) {
            break;
          }
          read();
        }
      }
      localObject3 = new Object[] { null, Boolean.FALSE, localObject1 };
      return new StatementSession(83, (Object[])localObject3);
    case 379: 
      read();
      localObject1 = XreadValueSpecificationOrNull();
      if ((localObject1 == null) || (!((Expression)localObject1).getDataType().isCharacterType())) {
        throw Error.error(4650);
      }
      localObject3 = null;
      if (readIfThis(120))
      {
        localObject3 = new HsqlArrayList();
        for (;;)
        {
          localObject4 = readSchemaObjectName(14);
          ((HsqlArrayList)localObject3).add(localObject4);
          if (this.token.tokenType != 824) {
            break;
          }
          read();
        }
      }
      localObject4 = new Object[] { localObject1, Boolean.TRUE, localObject3 };
      return new StatementSession(83, (Object[])localObject4);
    case 297: 
      read();
      return compileSetTimeZone();
    case 512: 
      read();
      return compileSetRole();
    case 530: 
      read();
      return compileSessionSettings();
    case 548: 
      read();
      localObject1 = processTransactionCharacteristics();
      if ((localObject1[0] == null) && (localObject1[1] == null)) {
        throw unexpectedToken();
      }
      return new StatementSession(85, (Object[])localObject1);
    case 583: 
      read();
      localObject1 = processTrueOrFalseObject();
      localObject3 = new Object[] { localObject1 };
      return new StatementSession(1114, (Object[])localObject3);
    case 649: 
      read();
      localObject1 = processTrueOrFalseObject();
      localObject3 = new Object[] { localObject1 };
      return new StatementSession(82, (Object[])localObject3);
    case 618: 
      read();
      localObject1 = processTrueOrFalseObject();
      localObject3 = new Object[] { localObject1 };
      return new StatementSession(1048, (Object[])localObject3);
    case 633: 
      read();
      localObject1 = readIntegerObject();
      localObject3 = new Object[] { localObject1 };
      return new StatementSession(1116, (Object[])localObject3);
    case 83: 
      read();
      readThis(294);
      readThis(557);
      switch (this.token.tokenType)
      {
      case 635: 
        i = 4;
        break;
      case 593: 
        i = 5;
        break;
      default: 
        throw unexpectedToken();
      }
      read();
      localObject3 = new Object[] { ValuePool.getInt(i) };
      return new StatementCommand(1035, (Object[])localObject3);
    case 294: 
      return compileSetTable();
    case 668: 
      read();
      i = 0;
      if (this.token.tokenType == 310)
      {
        i = this.database.getProperties().getDefaultWriteDelay();
        read();
      }
      else if (this.token.tokenType == 114)
      {
        i = 0;
        read();
      }
      else
      {
        i = readInteger();
        if (i < 0) {
          i = 0;
        }
        if (this.token.tokenType == 636) {
          read();
        } else {
          i *= 1000;
        }
      }
      localObject3 = new Object[] { Integer.valueOf(i) };
      return new StatementCommand(1033, (Object[])localObject3, null, null);
    case 643: 
      localObject3 = Boolean.FALSE;
      read();
      if (readIfThis(609)) {
        localObject3 = Boolean.TRUE;
      }
      localObject2 = readPassword();
      localObject4 = new Object[] { null, localObject2, localObject3 };
      StatementCommand localStatementCommand = new StatementCommand(1093, (Object[])localObject4);
      String str = User.getSetCurrentPasswordDigestSQL(this.database.granteeManager, (String)localObject2, ((Boolean)localObject3).booleanValue());
      localStatementCommand.setSQL(str);
      return localStatementCommand;
    case 623: 
      read();
      readThis(519);
      if (this.token.tokenType == 83) {
        localObject2 = null;
      } else {
        localObject2 = this.database.schemaManager.getSchemaHsqlName(this.token.tokenString);
      }
      read();
      localObject3 = new Object[] { null, localObject2 };
      return new StatementCommand(1092, (Object[])localObject3);
    case 614: 
      return compileSetFilesProperty();
    case 605: 
      return compileSetDatabaseProperty();
    case 647: 
      return compileSetProperty();
    }
    return compileSetStatement(this.session.sessionContext.sessionVariableRangeGroups, this.session.sessionContext.sessionVariablesRange);
  }
  
  StatementCommand compileSetTable()
  {
    read();
    Table localTable = readTableName();
    Object[] arrayOfObject = { localTable.getName(), null, null };
    Object localObject1;
    switch (this.token.tokenType)
    {
    default: 
      throw unexpectedToken();
    case 535: 
      read();
      return compileTableSource(localTable);
    case 502: 
      read();
      boolean bool = false;
      if (this.token.tokenType == 569)
      {
        read();
      }
      else
      {
        readThis(205);
        bool = true;
      }
      arrayOfObject[1] = Boolean.valueOf(bool);
      return new StatementCommand(1154, arrayOfObject, null, new HsqlNameManager.HsqlName[] { localTable.getName() });
    case 649: 
      read();
      localObject1 = processTrueOrFalseObject();
      arrayOfObject[1] = localObject1;
      return new StatementCommand(1154, arrayOfObject, null, new HsqlNameManager.HsqlName[] { localTable.getName() });
    case 621: 
      read();
      checkIsValue();
      localObject1 = this.token.tokenString;
      read();
      arrayOfObject[1] = localObject1;
      arrayOfObject[2] = Integer.valueOf(5);
      return new StatementCommand(1153, arrayOfObject, null, new HsqlNameManager.HsqlName[] { localTable.getName() });
    case 557: 
      read();
      int i;
      switch (this.token.tokenType)
      {
      case 635: 
        i = 4;
        break;
      case 593: 
        i = 5;
        break;
      default: 
        throw unexpectedToken();
      }
      switch (localTable.getTableType())
      {
      case 4: 
      case 5: 
      case 7: 
        break;
      case 6: 
      default: 
        throw unexpectedToken();
      }
      read();
      arrayOfObject[1] = Integer.valueOf(i);
      return new StatementCommand(1157, arrayOfObject, null, new HsqlNameManager.HsqlName[] { localTable.getName() });
    case 598: 
      read();
      readThis(204);
      localObject2 = new OrderedHashSet();
      readThis(836);
      readSimpleColumnNames((OrderedHashSet)localObject2, localTable, false);
      readThis(822);
      localObject3 = localTable.getColumnIndexes((OrderedHashSet)localObject2);
      arrayOfObject[1] = localObject3;
      return new StatementCommand(1158, arrayOfObject, null, new HsqlNameManager.HsqlName[] { localTable.getName() });
    case 189: 
      read();
      readThis(536);
      arrayOfObject = new Object[] { localTable.getName() };
      localObject2 = this.database.schemaManager.getCatalogAndBaseTableNames(localTable.getName());
      return new StatementCommand(1159, arrayOfObject, null, (HsqlNameManager.HsqlName[])localObject2);
    }
    read();
    Object localObject2 = readIntegerObject();
    arrayOfObject = new Object[] { localTable.getName(), localObject2 };
    Object localObject3 = this.database.schemaManager.getCatalogAndBaseTableNames(localTable.getName());
    return new StatementCommand(1160, arrayOfObject, null, (HsqlNameManager.HsqlName[])localObject3);
  }
  
  StatementCommand compileSetDatabaseProperty()
  {
    read();
    checkDatabaseUpdateAuthorisation();
    Object localObject1;
    Object localObject4;
    String str;
    Object localObject5;
    Object localObject2;
    Object localObject6;
    Object localObject3;
    switch (this.token.tokenType)
    {
    case 584: 
      read();
      readThis(126);
      localObject1 = readCreateDatabaseAuthenticationFunction();
      localObject4 = new Object[] { localObject1 };
      return new StatementCommand(1036, (Object[])localObject4, null, null);
    case 379: 
      localObject1 = null;
      read();
      checkIsSimpleName();
      str = this.token.tokenString;
      read();
      if (readIfThis(190))
      {
        readThis(485);
        localObject1 = Boolean.FALSE;
      }
      else if (readIfThis(485))
      {
        readThis(536);
        localObject1 = Boolean.TRUE;
      }
      if (localObject1 == null) {
        if ((this.session.isProcessingScript()) && (this.database.getProperties().isVersion18())) {
          localObject1 = Boolean.FALSE;
        } else {
          localObject1 = Boolean.TRUE;
        }
      }
      localObject4 = new Object[] { str, localObject1 };
      return new StatementCommand(1047, (Object[])localObject4, null, null);
    case 83: 
      return compileSetDefault();
    case 610: 
      read();
      readThis(734);
      boolean bool1 = readIfThis(275);
      readThis(454);
      localObject4 = readIntegerObject();
      localObject5 = new Object[] { localObject4, Boolean.valueOf(bool1) };
      return new StatementCommand(1016, (Object[])localObject5, null, null);
    case 616: 
      read();
      localObject2 = readIntegerObject();
      localObject4 = new Object[] { localObject2 };
      return new StatementCommand(1037, (Object[])localObject4, null, null);
    case 643: 
      read();
      switch (this.token.tokenType)
      {
      case 40: 
        read();
        readThis(126);
        localObject2 = readCreatePasswordCheckFunction();
        localObject4 = new Object[] { localObject2 };
        return new StatementCommand(1040, (Object[])localObject4, null, null);
      case 609: 
        read();
        str = readQuotedString();
        localObject2 = new Object[] { str };
        return new StatementCommand(1056, (Object[])localObject2, null, null);
      }
      throw unexpectedToken();
    case 650: 
      read();
      readThis(620);
      boolean bool2 = processTrueOrFalse();
      localObject4 = new Object[] { Boolean.valueOf(bool2) };
      return new StatementCommand(1049, (Object[])localObject4, null, null);
    case 275: 
      read();
      int i = 1050;
      localObject4 = Boolean.TRUE;
      localObject5 = Integer.valueOf(0);
      localObject6 = null;
      switch (this.token.tokenType)
      {
      case 627: 
        read();
        readThis(475);
        localObject6 = "sql.live_object";
        localObject4 = processTrueOrFalseObject();
        arrayOfObject2 = new Object[] { localObject6, localObject4, localObject5 };
        break;
      case 468: 
        read();
        localObject6 = "sql.enforce_names";
        localObject4 = processTrueOrFalseObject();
        break;
      case 651: 
        read();
        readThis(468);
        localObject6 = "sql.regular_names";
        localObject4 = processTrueOrFalseObject();
        break;
      case 236: 
        read();
        localObject4 = processTrueOrFalseObject();
        localObject6 = "sql.enforce_refs";
        break;
      case 534: 
        read();
        localObject4 = processTrueOrFalseObject();
        localObject6 = "sql.enforce_size";
        break;
      case 665: 
        read();
        localObject4 = processTrueOrFalseObject();
        localObject6 = "sql.enforce_types";
        break;
      case 660: 
        read();
        if (readIfThis(84))
        {
          localObject6 = "sql.enforce_tdc_delete";
        }
        else
        {
          readThis(319);
          localObject6 = "sql.enforce_tdc_update";
        }
        localObject4 = processTrueOrFalseObject();
        break;
      case 303: 
        read();
        readThis(664);
        readThis(665);
        localObject4 = processTrueOrFalseObject();
        localObject6 = "jdbc.translate_tti_types";
        break;
      case 38: 
        read();
        readThis(626);
        localObject4 = processTrueOrFalseObject();
        localObject6 = "sql.char_literal";
        break;
      case 687: 
        read();
        readThis(473);
        localObject4 = processTrueOrFalseObject();
        localObject6 = "sql.concat_nulls";
        break;
      case 473: 
        read();
        if (readIfThis(423))
        {
          localObject6 = "sql.nulls_first";
        }
        else
        {
          readThis(208);
          localObject6 = "sql.nulls_order";
        }
        localObject4 = processTrueOrFalseObject();
        break;
      case 315: 
        read();
        readThis(473);
        localObject4 = processTrueOrFalseObject();
        localObject6 = "sql.unique_nulls";
        break;
      case 53: 
        read();
        readThis(311);
        localObject4 = processTrueOrFalseObject();
        localObject6 = "sql.convert_trunc";
        break;
      case 17: 
        read();
        readThis(518);
        localObject5 = readIntegerObject();
        localObject6 = "sql.avg_scale";
        break;
      case 92: 
        read();
        readThis(638);
        localObject4 = processTrueOrFalseObject();
        localObject6 = "sql.double_nan";
        break;
      case 632: 
        read();
        readThis(152);
        readThis(733);
        localObject4 = processTrueOrFalseObject();
        localObject6 = "sql.longvar_is_lob";
        break;
      case 618: 
        read();
        localObject4 = processTrueOrFalseObject();
        localObject6 = "sql.ignore_case";
        break;
      case 659: 
        read();
        if (this.token.tokenString.equals("DB2"))
        {
          read();
          localObject6 = "sql.syntax_db2";
        }
        else if (this.token.tokenString.equals("MSS"))
        {
          read();
          localObject6 = "sql.syntax_mss";
        }
        else if (this.token.tokenString.equals("MYS"))
        {
          read();
          localObject6 = "sql.syntax_mys";
        }
        else if (this.token.tokenString.equals("ORA"))
        {
          read();
          localObject6 = "sql.syntax_ora";
        }
        else if (this.token.tokenString.equals("PGS"))
        {
          read();
          localObject6 = "sql.syntax_pgs";
        }
        else
        {
          throw unexpectedToken();
        }
        localObject4 = processTrueOrFalseObject();
        break;
      default: 
        throw unexpectedToken();
      }
      Object[] arrayOfObject2 = { localObject6, localObject4, localObject5 };
      return new StatementCommand(i, arrayOfObject2, null, null);
    case 662: 
      read();
      readThis(294);
      readThis(402);
      localObject3 = readQuotedString();
      localObject4 = new Object[] { localObject3 };
      return new StatementCommand(1051, (Object[])localObject4, null, null);
    case 548: 
      read();
      if (readIfThis(255))
      {
        readThis(204);
        if (!readIfThis(606)) {
          readThis(602);
        }
        localObject3 = processTrueOrFalseObject();
        localObject4 = new StatementCommand(1054, new Object[] { localObject3 }, null, null);
        return (StatementCommand)localObject4;
      }
      readThis(603);
      int j = 0;
      switch (this.token.tokenType)
      {
      case 742: 
        read();
        j = 2;
        break;
      case 743: 
        read();
        j = 1;
        break;
      case 630: 
        read();
        j = 0;
        break;
      }
      localObject4 = this.database.schemaManager.getCatalogAndBaseTableNames();
      localObject5 = new Object[] { ValuePool.getInt(j) };
      localObject6 = new StatementCommand(1052, (Object[])localObject5, null, (HsqlNameManager.HsqlName[])localObject4);
      return (StatementCommand)localObject6;
    case 315: 
      read();
      readThis(467);
      if (!isUndelimitedSimpleName()) {
        throw unexpectedToken();
      }
      str = this.token.tokenString;
      read();
      if (str.length() != 16) {
        throw Error.error(5555);
      }
      if ((!Charset.isInSet(str, Charset.unquotedIdentifier)) || (!Charset.startsWith(str, Charset.uppercaseLetters))) {
        throw Error.error(5501);
      }
      Object[] arrayOfObject1 = { str };
      return new StatementCommand(1055, arrayOfObject1, null, null);
    }
    throw unexpectedToken();
  }
  
  StatementCommand compileSetFilesProperty()
  {
    read();
    int i = 0;
    Boolean localBoolean1 = null;
    Integer localInteger = null;
    Boolean localBoolean2 = null;
    HsqlNameManager.HsqlName[] arrayOfHsqlName = this.database.schemaManager.getCatalogNameArray();
    checkDatabaseUpdateAuthorisation();
    switch (this.token.tokenType)
    {
    case 40: 
      read();
      long l1 = readBigint();
      long l2 = -1L;
      i = 1014;
      arrayOfHsqlName = this.database.schemaManager.getCatalogNameArray();
      if (readIfThis(824)) {
        l2 = readBigint();
      }
      Object[] arrayOfObject2 = new Object[2];
      arrayOfObject2[0] = Long.valueOf(l1);
      arrayOfObject2[1] = Long.valueOf(l2);
      return new StatementCommand(i, arrayOfObject2, null, arrayOfHsqlName);
    case 592: 
      read();
      if (readIfThis(534))
      {
        localInteger = readIntegerObject();
        i = 1013;
      }
      else
      {
        readThis(259);
        localInteger = readIntegerObject();
        i = 1012;
      }
      if (readIfThis(190))
      {
        readThis(40);
        localBoolean2 = Boolean.TRUE;
      }
      break;
    case 518: 
      read();
      localInteger = readIntegerObject();
      i = 1025;
      arrayOfHsqlName = this.database.schemaManager.getCatalogAndBaseTableNames();
      break;
    case 536: 
      read();
      if (this.token.tokenType == 310)
      {
        localBoolean1 = Boolean.TRUE;
        read();
      }
      else if (this.token.tokenType == 114)
      {
        localBoolean1 = Boolean.FALSE;
        read();
      }
      else
      {
        localInteger = readIntegerObject();
      }
      i = 1031;
      arrayOfHsqlName = this.database.schemaManager.getCatalogAndBaseTableNames();
      break;
    case 733: 
      read();
      if (readIfThis(518))
      {
        localInteger = readIntegerObject();
        i = 1017;
      }
      else
      {
        readThis(601);
        i = 1018;
        localBoolean1 = processTrueOrFalseObject();
      }
      arrayOfHsqlName = this.database.schemaManager.getCatalogAndBaseTableNames();
      break;
    case 607: 
      read();
      i = 1015;
      localInteger = readIntegerObject();
      break;
    case 746: 
      read();
      if (readIfThis(534)) {
        localInteger = readIntegerObject();
      } else {
        localBoolean1 = processTrueOrFalseObject();
      }
      i = 1022;
      break;
    case 585: 
      read();
      i = 1011;
      readThis(439);
      localBoolean1 = processTrueOrFalseObject();
      break;
    case 734: 
      read();
      if (readIfThis(534))
      {
        i = 1021;
        localInteger = readIntegerObject();
      }
      else
      {
        i = 1020;
        localBoolean1 = processTrueOrFalseObject();
      }
      break;
    case 661: 
      read();
      readThis(494);
      i = 1032;
      localInteger = readIntegerObject();
      break;
    case 569: 
      read();
      readThis(608);
      i = 1033;
      int j = 0;
      if (this.token.tokenType == 310)
      {
        j = this.database.getProperties().getDefaultWriteDelay();
        read();
      }
      else if (this.token.tokenType == 114)
      {
        j = 0;
        read();
      }
      else
      {
        j = readInteger();
        if (j < 0) {
          j = 0;
        }
        if (this.token.tokenType == 636) {
          read();
        } else {
          j *= 1000;
        }
      }
      localInteger = Integer.valueOf(j);
      break;
    case 655: 
      read();
      readThis(615);
      if (this.token.tokenType == 662)
      {
        read();
        localInteger = Integer.valueOf(0);
      }
      else
      {
        readThis(601);
        localInteger = Integer.valueOf(3);
      }
      i = 1026;
      break;
    default: 
      throw unexpectedToken();
    }
    Object[] arrayOfObject1 = new Object[2];
    arrayOfObject1[0] = (localBoolean1 == null ? localInteger : localBoolean1);
    arrayOfObject1[1] = localBoolean2;
    return new StatementCommand(i, arrayOfObject1, null, arrayOfHsqlName);
  }
  
  Object[] processTransactionCharacteristics()
  {
    int i = 0;
    boolean bool = false;
    Object[] arrayOfObject = new Object[2];
    for (;;)
    {
      switch (this.token.tokenType)
      {
      case 502: 
        if (arrayOfObject[0] != null) {
          throw unexpectedToken();
        }
        read();
        if (this.token.tokenType == 205)
        {
          read();
          bool = true;
        }
        else
        {
          readThis(569);
          bool = false;
        }
        arrayOfObject[0] = Boolean.valueOf(bool);
        break;
      case 446: 
        if (arrayOfObject[1] != null) {
          throw unexpectedToken();
        }
        read();
        readThis(454);
        switch (this.token.tokenType)
        {
        case 528: 
          read();
          i = 8;
          break;
        case 502: 
          read();
          if (this.token.tokenType == 386)
          {
            read();
            i = 2;
          }
          else if (this.token.tokenType == 559)
          {
            read();
            i = 1;
          }
          else
          {
            throw unexpectedToken();
          }
          break;
        case 504: 
          read();
          readThis(502);
          i = 4;
          break;
        default: 
          throw unexpectedToken();
        }
        arrayOfObject[1] = Integer.valueOf(i);
        break;
      case 824: 
        if ((arrayOfObject[0] == null) && (arrayOfObject[1] == null)) {
          throw unexpectedToken();
        }
        read();
      }
    }
    if ((!bool) && (i == 1)) {
      throw unexpectedToken("WRITE");
    }
    return arrayOfObject;
  }
  
  private Statement compileCommit()
  {
    boolean bool = false;
    read();
    readIfThis(568);
    if (this.token.tokenType == 5)
    {
      read();
      if (this.token.tokenType == 190) {
        read();
      } else {
        bool = true;
      }
      readThis(371);
    }
    String str = bool ? StatementSession.commitAndChainStatement.sql : StatementSession.commitNoChainStatement.sql;
    StatementSession localStatementSession = new StatementSession(14, new Object[] { Boolean.valueOf(bool) });
    localStatementSession.setSQL(str);
    return localStatementSession;
  }
  
  private Statement compileStartTransaction()
  {
    read();
    readThis(548);
    Object[] arrayOfObject = processTransactionCharacteristics();
    StatementSession localStatementSession = new StatementSession(86, arrayOfObject);
    return localStatementSession;
  }
  
  private Statement compileLock()
  {
    read();
    if (readIfThis(369)) {
      return compileLockCatalog();
    }
    readThis(294);
    return compileLockTable();
  }
  
  private Statement compileLockCatalog()
  {
    boolean bool = processTrueOrFalse();
    int i = bool ? 1111 : 1112;
    HsqlNameManager.HsqlName[] arrayOfHsqlName = bool ? this.database.schemaManager.getCatalogAndBaseTableNames() : null;
    StatementSession localStatementSession = new StatementSession(i, null, arrayOfHsqlName);
    return localStatementSession;
  }
  
  private Statement compileLockTable()
  {
    OrderedHashSet localOrderedHashSet1 = new OrderedHashSet();
    OrderedHashSet localOrderedHashSet2 = new OrderedHashSet();
    for (;;)
    {
      localObject = readTableName(true);
      switch (this.token.tokenType)
      {
      case 502: 
        read();
        localOrderedHashSet1.add(((Table)localObject).getName());
        break;
      case 569: 
        read();
        localOrderedHashSet2.add(((Table)localObject).getName());
        break;
      default: 
        throw unexpectedToken();
      }
      if (this.token.tokenType != 824) {
        break;
      }
      read();
    }
    Object localObject = new HsqlNameManager.HsqlName[localOrderedHashSet2.size()];
    localOrderedHashSet2.toArray((Object[])localObject);
    localOrderedHashSet1.removeAll((Object[])localObject);
    HsqlNameManager.HsqlName[] arrayOfHsqlName = new HsqlNameManager.HsqlName[localOrderedHashSet1.size()];
    localOrderedHashSet1.toArray(arrayOfHsqlName);
    StatementSession localStatementSession = new StatementSession(1113, arrayOfHsqlName, (HsqlNameManager.HsqlName[])localObject);
    return localStatementSession;
  }
  
  private Statement compileRollback()
  {
    boolean bool = false;
    String str = null;
    read();
    if (this.token.tokenType == 568) {
      read();
    }
    if (this.token.tokenType == 301)
    {
      read();
      readThis(260);
      checkIsSimpleName();
      str = this.token.tokenString;
      read();
      localObject = new Object[] { str };
      localStatementSession = new StatementSession(1118, (Object[])localObject);
      return localStatementSession;
    }
    if (this.token.tokenType == 5)
    {
      read();
      if (this.token.tokenType == 190) {
        read();
      } else {
        bool = true;
      }
      readThis(371);
    }
    Object localObject = bool ? StatementSession.rollbackAndChainStatement.sql : StatementSession.rollbackNoChainStatement.sql;
    StatementSession localStatementSession = new StatementSession(66, new Object[] { Boolean.valueOf(bool) });
    localStatementSession.setSQL((String)localObject);
    return localStatementSession;
  }
  
  private Statement compileSavepoint()
  {
    read();
    checkIsSimpleName();
    String str = this.token.tokenString;
    read();
    Object[] arrayOfObject = { str };
    return new StatementSession(67, arrayOfObject);
  }
  
  private Statement compileReleaseSavepoint()
  {
    read();
    readThis(260);
    String str = this.token.tokenString;
    read();
    Object[] arrayOfObject = { str };
    return new StatementSession(61, arrayOfObject);
  }
  
  private Statement compileSessionSettings()
  {
    Object localObject1;
    Object localObject2;
    switch (this.token.tokenType)
    {
    case 375: 
      read();
      readThis(11);
      readThis(548);
      localObject1 = processTransactionCharacteristics();
      return new StatementSession(82, (Object[])localObject1);
    case 16: 
      read();
      localObject1 = XreadValueSpecificationOrNull();
      if (localObject1 == null) {
        throw Error.error(5584);
      }
      ((Expression)localObject1).resolveTypes(this.session, null);
      if (((Expression)localObject1).isUnresolvedParam()) {
        ((Expression)localObject1).dataType = Type.SQL_VARCHAR;
      }
      if ((((Expression)localObject1).dataType == null) || (!((Expression)localObject1).dataType.isCharacterType())) {
        throw Error.error(5563);
      }
      localObject2 = new Expression[] { localObject1, null };
      return new StatementSession(81, (Expression[])localObject2);
    case 250: 
      read();
      readThis(635);
      readThis(259);
      localObject1 = readIntegerObject();
      localObject2 = new Object[] { localObject1 };
      return new StatementSession(1117, (Object[])localObject2);
    case 612: 
      read();
      localObject1 = parseSQLFeatureValue();
      localObject2 = processTrueOrFalseObject();
      Object[] arrayOfObject = { localObject1, localObject2 };
      return new StatementSession(1115, arrayOfObject);
    }
    throw unexpectedToken();
  }
  
  private Statement compileSetRole()
  {
    Object localObject;
    if (this.token.tokenType == 191)
    {
      read();
      localObject = new ExpressionValue(null, Type.SQL_VARCHAR);
    }
    else
    {
      localObject = XreadValueSpecificationOrNull();
      if (localObject == null) {
        throw Error.error(4100);
      }
      if (!((Expression)localObject).getDataType().isCharacterType()) {
        throw Error.error(2200);
      }
      if ((((Expression)localObject).getType() != 1) && ((((Expression)localObject).getType() != 28) || (!((FunctionSQL)localObject).isValueFunction()))) {
        throw Error.error(2200);
      }
    }
    return new StatementSession(79, new Expression[] { localObject });
  }
  
  private Statement compileSetTimeZone()
  {
    readThis(571);
    Object localObject;
    if (this.token.tokenType == 167)
    {
      read();
      localObject = new ExpressionValue(null, Type.SQL_INTERVAL_HOUR_TO_MINUTE);
    }
    else
    {
      localObject = XreadIntervalValueExpression();
      HsqlList localHsqlList = ((Expression)localObject).resolveColumnReferences(this.session, RangeGroup.emptyGroup, RangeGroup.emptyArray, null);
      ExpressionColumn.checkColumnsResolved(localHsqlList);
      ((Expression)localObject).resolveTypes(this.session, null);
      if (((Expression)localObject).dataType == null) {
        throw Error.error(5563);
      }
      if (((Expression)localObject).dataType.typeCode != 111) {
        throw Error.error(5563);
      }
    }
    return new StatementSession(76, new Expression[] { localObject });
  }
  
  private Statement compileShutdown()
  {
    this.session.checkAdmin();
    int i = 2;
    read();
    switch (this.token.tokenType)
    {
    case 619: 
      i = 1;
      read();
      break;
    case 600: 
      i = 3;
      read();
      break;
    case 655: 
      i = 4;
      read();
      break;
    }
    if (this.token.tokenType == 841) {
      read();
    }
    if (this.token.tokenType != 914) {
      throw unexpectedToken();
    }
    Object[] arrayOfObject = { Integer.valueOf(i) };
    StatementCommand localStatementCommand = new StatementCommand(1003, arrayOfObject, null, null);
    return localStatementCommand;
  }
  
  private Statement compileBackup()
  {
    Boolean localBoolean1 = null;
    Boolean localBoolean2 = null;
    Boolean localBoolean3 = null;
    Boolean localBoolean4 = null;
    read();
    readThis(605);
    readThis(301);
    String str = readQuotedString();
    str = str.trim();
    if (str.length() == 0) {
      throw unexpectedToken(str);
    }
    for (;;)
    {
      switch (this.token.tokenType)
      {
      case 589: 
        if (localBoolean1 != null) {
          throw unexpectedToken();
        }
        localBoolean1 = Boolean.TRUE;
        read();
        break;
      case 655: 
        if (localBoolean2 != null) {
          throw unexpectedToken();
        }
        localBoolean2 = Boolean.TRUE;
        read();
        break;
      case 601: 
        if (localBoolean3 != null) {
          throw unexpectedToken();
        }
        localBoolean3 = Boolean.TRUE;
        read();
        break;
      case 193: 
        read();
        if (this.token.tokenType == 601)
        {
          if (localBoolean3 != null) {
            throw unexpectedToken();
          }
          localBoolean3 = Boolean.FALSE;
          read();
        }
        else if (this.token.tokenType == 589)
        {
          if (localBoolean1 != null) {
            throw unexpectedToken();
          }
          localBoolean1 = Boolean.FALSE;
          read();
        }
        else
        {
          throw unexpectedToken();
        }
        break;
      case 11: 
        if (localBoolean4 != null) {
          throw unexpectedToken();
        }
        read();
        readThis(614);
        localBoolean4 = Boolean.TRUE;
      }
    }
    if (localBoolean2 == null) {
      localBoolean2 = Boolean.FALSE;
    }
    if (localBoolean1 == null) {
      localBoolean1 = Boolean.TRUE;
    }
    if (localBoolean3 == null) {
      localBoolean3 = Boolean.TRUE;
    }
    if (localBoolean4 == null) {
      localBoolean4 = Boolean.FALSE;
    }
    if ((localBoolean2.booleanValue()) && (!localBoolean1.booleanValue())) {
      throw unexpectedToken("NOT");
    }
    HsqlNameManager.HsqlName[] arrayOfHsqlName = localBoolean1.booleanValue() ? this.database.schemaManager.getCatalogAndBaseTableNames() : HsqlNameManager.HsqlName.emptyArray;
    Object[] arrayOfObject = { str, localBoolean1, localBoolean2, localBoolean3, localBoolean4 };
    StatementCommand localStatementCommand = new StatementCommand(1001, arrayOfObject, null, arrayOfHsqlName);
    return localStatementCommand;
  }
  
  private Statement compilePerform()
  {
    read();
    switch (this.token.tokenType)
    {
    case 40: 
      read();
      readThis(294);
      Table localTable = readTableName();
      readThis(621);
      Object[] arrayOfObject = { localTable.getName(), Integer.valueOf(1), null };
      HsqlNameManager.HsqlName[] arrayOfHsqlName = this.database.schemaManager.getCatalogAndBaseTableNames(localTable.getName());
      return new StatementCommand(1006, arrayOfObject, null, arrayOfHsqlName);
    }
    throw unexpectedToken();
  }
  
  private Statement compileCheckpoint()
  {
    boolean bool = false;
    read();
    if (this.token.tokenType == 607)
    {
      bool = true;
      read();
    }
    else if (this.token.tokenType == 841)
    {
      read();
    }
    if (this.token.tokenType != 914) {
      throw unexpectedToken();
    }
    HsqlNameManager.HsqlName[] arrayOfHsqlName = this.database.schemaManager.getCatalogAndBaseTableNames();
    Object[] arrayOfObject = { Boolean.valueOf(bool) };
    StatementCommand localStatementCommand = new StatementCommand(1002, arrayOfObject, null, arrayOfHsqlName);
    return localStatementCommand;
  }
  
  public static Statement getAutoCheckpointStatement(Database paramDatabase)
  {
    HsqlNameManager.HsqlName[] arrayOfHsqlName = paramDatabase.schemaManager.getCatalogAndBaseTableNames();
    Object[] arrayOfObject = { Boolean.FALSE };
    StatementCommand localStatementCommand = new StatementCommand(1002, arrayOfObject, null, arrayOfHsqlName);
    localStatementCommand.setCompileTimestamp(paramDatabase.txManager.getGlobalChangeTimestamp());
    localStatementCommand.setSQL("CHECKPOINT");
    return localStatementCommand;
  }
  
  private Statement compileDisconnect()
  {
    read();
    String str = "DISCONNECT";
    StatementSession localStatementSession = new StatementSession(22, (Object[])null);
    return localStatementSession;
  }
  
  private Statement compileExplainPlan()
  {
    read();
    readThis(645);
    readThis(120);
    Statement localStatement = compilePart(0);
    localStatement.setDescribe();
    return new StatementCommand(1151, new Object[] { localStatement });
  }
  
  private StatementCommand compileTableSource(Table paramTable)
  {
    boolean bool1 = false;
    boolean bool2 = false;
    Object[] arrayOfObject = new Object[5];
    arrayOfObject[0] = paramTable.getName();
    if (!paramTable.isText()) {
      localObject = Error.error(321);
    }
    if (this.token.tokenType == 204)
    {
      read();
      localObject = getLastPart();
      arrayOfObject[1] = Boolean.TRUE;
      return new StatementCommand(1155, arrayOfObject, null, new HsqlNameManager.HsqlName[] { paramTable.getName() });
    }
    if (this.token.tokenType == 642)
    {
      read();
      localObject = getLastPart();
      arrayOfObject[1] = Boolean.FALSE;
      return new StatementCommand(1155, arrayOfObject, null, new HsqlNameManager.HsqlName[] { paramTable.getName() });
    }
    if (this.token.tokenType == 617)
    {
      read();
      bool1 = true;
    }
    String str;
    if (this.token.tokenType == 913)
    {
      str = this.token.tokenString;
      read();
    }
    else
    {
      str = readQuotedString();
    }
    if ((!bool1) && (this.token.tokenType == 410))
    {
      bool2 = true;
      read();
    }
    Object localObject = getLastPart();
    arrayOfObject[2] = str;
    arrayOfObject[3] = Boolean.valueOf(bool2);
    arrayOfObject[4] = Boolean.valueOf(bool1);
    int i = bool1 ? 1156 : 1155;
    return new StatementCommand(i, arrayOfObject, null, new HsqlNameManager.HsqlName[] { paramTable.getName() });
  }
}


/* Location:              E:\java\java学习\hutubill\lib\all.jar!\org\hsqldb\ParserCommand.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */
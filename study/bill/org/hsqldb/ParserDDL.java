package org.hsqldb;

import java.lang.reflect.Method;
import org.hsqldb.error.Error;
import org.hsqldb.index.Index;
import org.hsqldb.lib.ArrayUtil;
import org.hsqldb.lib.HsqlArrayList;
import org.hsqldb.lib.HsqlList;
import org.hsqldb.lib.OrderedHashSet;
import org.hsqldb.lib.OrderedIntHashSet;
import org.hsqldb.map.ValuePool;
import org.hsqldb.persist.HsqlDatabaseProperties;
import org.hsqldb.rights.Grantee;
import org.hsqldb.rights.GranteeManager;
import org.hsqldb.rights.Right;
import org.hsqldb.rights.User;
import org.hsqldb.rights.UserManager;
import org.hsqldb.types.Charset;
import org.hsqldb.types.Collation;
import org.hsqldb.types.Type;
import org.hsqldb.types.UserTypeModifier;

public class ParserDDL
  extends ParserRoutine
{
  static final int[] schemaCommands = { 59, 130 };
  static final short[] startStatementTokens = { 59, 130, 4, 93 };
  static final short[] startStatementTokensSchema = { 59, 130 };
  
  ParserDDL(Session paramSession, Scanner paramScanner)
  {
    super(paramSession, paramScanner);
  }
  
  void reset(Session paramSession, String paramString)
  {
    super.reset(paramSession, paramString);
  }
  
  StatementSchema compileCreate()
  {
    int j = 0;
    boolean bool = false;
    read();
    int i;
    switch (this.token.tokenType)
    {
    case 129: 
      read();
      readThis(545);
      readIfThis(635);
      readThis(294);
      j = 1;
      i = 3;
      break;
    case 661: 
      read();
      readThis(294);
      j = 1;
      i = 3;
      break;
    case 545: 
      read();
      readThis(294);
      j = 1;
      i = 3;
      break;
    case 635: 
      read();
      readThis(294);
      j = 1;
      i = 4;
      break;
    case 593: 
      read();
      readThis(294);
      j = 1;
      i = 5;
      break;
    case 662: 
      read();
      readThis(294);
      j = 1;
      i = 7;
      break;
    case 294: 
      read();
      j = 1;
      i = this.database.schemaManager.getDefaultTableType();
      break;
    default: 
      i = 4;
    }
    if (j != 0) {
      return compileCreateTable(i);
    }
    if ((this.database.sqlSyntaxOra) && (this.token.tokenType == 207))
    {
      read();
      readThis(761);
      switch (this.token.tokenType)
      {
      case 126: 
      case 229: 
      case 307: 
      case 557: 
      case 567: 
      case 658: 
        break;
      default: 
        throw unexpectedToken("OR");
      }
      bool = true;
    }
    switch (this.token.tokenType)
    {
    case 581: 
      return compileCreateAlias();
    case 527: 
      return compileCreateSequence();
    case 519: 
      return compileCreateSchema();
    case 307: 
      return compileCreateTrigger(bool);
    case 321: 
      return compileCreateUser();
    case 512: 
      return compileCreateRole();
    case 567: 
      return compileCreateView(false, bool);
    case 414: 
      return compileCreateDomain();
    case 557: 
      return compileCreateType(bool);
    case 38: 
      return compileCreateCharacterSet();
    case 379: 
      return compileCreateCollation();
    case 315: 
      read();
      checkIsThis(621);
      return compileCreateIndex(true);
    case 621: 
      return compileCreateIndex(false);
    case 582: 
      return compileCreateProcedureOrFunction(bool);
    case 126: 
    case 229: 
      return compileCreateProcedureOrFunction(bool);
    case 658: 
      return compileCreateSynonym(bool);
    }
    throw unexpectedToken();
  }
  
  Statement compileAlter()
  {
    read();
    Object localObject;
    switch (this.token.tokenType)
    {
    case 621: 
      read();
      localObject = readNewSchemaObjectName(20, true);
      ((HsqlNameManager.HsqlName)localObject).setSchemaIfNull(this.session.getCurrentSchemaHsqlName());
      if (this.token.tokenType == 652)
      {
        read();
        readThis(301);
        return compileRenameObject((HsqlNameManager.HsqlName)localObject, 20);
      }
      readThis(11);
      Index localIndex = (Index)this.database.schemaManager.getSchemaObject((HsqlNameManager.HsqlName)localObject);
      if (localIndex == null) {
        throw Error.error(5501);
      }
      Table localTable = (Table)this.database.schemaManager.getSchemaObject(localIndex.getName().parent);
      int[] arrayOfInt = readColumnList(localTable, true);
      String str = getLastPart();
      Object[] arrayOfObject = { localTable, arrayOfInt, localIndex.getName() };
      HsqlNameManager.HsqlName[] arrayOfHsqlName = { this.database.getCatalogName(), localTable.getName() };
      return new StatementSchema(str, 1121, arrayOfObject, null, arrayOfHsqlName);
    case 519: 
      read();
      localObject = readSchemaName();
      readThis(652);
      readThis(301);
      return compileRenameObject((HsqlNameManager.HsqlName)localObject, 2);
    case 369: 
      read();
      checkIsSimpleName();
      localObject = this.token.tokenString;
      checkValidCatalogName((String)localObject);
      read();
      readThis(652);
      readThis(301);
      return compileRenameObject(this.database.getCatalogName(), 1);
    case 527: 
      return compileAlterSequence();
    case 294: 
      return compileAlterTable();
    case 321: 
      return compileAlterUser();
    case 414: 
      return compileAlterDomain();
    case 567: 
      return compileCreateView(true, false);
    case 530: 
      return compileAlterSession();
    case 273: 
      return compileAlterSpecificRoutine();
    case 513: 
      return compileAlterRoutine();
    case 51: 
      read();
      localObject = (Constraint)readSchemaObjectName(5);
      readThis(652);
      readThis(301);
      return compileRenameObject(((Constraint)localObject).getName(), 5);
    }
    throw unexpectedToken();
  }
  
  Statement compileAlterRoutine()
  {
    readThis(513);
    RoutineSchema localRoutineSchema = (RoutineSchema)readSchemaObjectName(18);
    readThis(652);
    readThis(301);
    return compileRenameObject(localRoutineSchema.getName(), localRoutineSchema.getName().type);
  }
  
  Statement compileDrop()
  {
    int m = 0;
    boolean bool1 = false;
    int n = 0;
    boolean bool2 = false;
    read();
    int i = this.token.tokenType;
    int k;
    int j;
    switch (i)
    {
    case 621: 
      read();
      k = 1129;
      j = 20;
      n = 1;
      break;
    case 360: 
      read();
      k = 24;
      j = 6;
      m = 1;
      break;
    case 273: 
      read();
      switch (this.token.tokenType)
      {
      case 126: 
      case 229: 
      case 513: 
        read();
        break;
      default: 
        throw unexpectedToken();
      }
      k = 30;
      j = 24;
      m = 1;
      n = 1;
      break;
    case 229: 
      read();
      k = 30;
      j = 17;
      m = 1;
      n = 1;
      break;
    case 126: 
      read();
      k = 30;
      j = 16;
      m = 1;
      n = 1;
      break;
    case 519: 
      read();
      k = 31;
      j = 2;
      m = 1;
      n = 1;
      break;
    case 527: 
      read();
      k = 32;
      j = 7;
      m = 1;
      n = 1;
      break;
    case 307: 
      read();
      k = 36;
      j = 8;
      m = 0;
      n = 1;
      break;
    case 321: 
      read();
      k = 1131;
      j = 11;
      m = 1;
      break;
    case 512: 
      read();
      k = 29;
      j = 11;
      m = 1;
      break;
    case 414: 
      read();
      k = 28;
      j = 13;
      m = 1;
      n = 1;
      break;
    case 557: 
      read();
      k = 27;
      j = 12;
      m = 1;
      n = 1;
      break;
    case 38: 
      read();
      readThis(268);
      k = 25;
      j = 14;
      m = 0;
      n = 1;
      break;
    case 379: 
      read();
      k = 26;
      j = 15;
      m = 0;
      n = 1;
      break;
    case 567: 
      read();
      k = 39;
      j = 4;
      m = 1;
      n = 1;
      break;
    case 294: 
      read();
      k = 33;
      j = 3;
      m = 1;
      n = 1;
      break;
    case 658: 
      read();
      k = 1147;
      j = 29;
      m = 0;
      n = 1;
      break;
    default: 
      throw unexpectedToken();
    }
    if ((n != 0) && (this.token.tokenType == 434))
    {
      int i1 = getPosition();
      read();
      if (this.token.tokenType == 109)
      {
        read();
        bool2 = true;
      }
      else
      {
        rewind(i1);
      }
    }
    checkIsIdentifier();
    HsqlNameManager.HsqlName localHsqlName = null;
    switch (i)
    {
    case 321: 
      checkIsSimpleName();
      checkDatabaseUpdateAuthorisation();
      localObject = this.database.getUserManager().get(this.token.tokenString);
      read();
      break;
    case 512: 
      checkIsSimpleName();
      checkDatabaseUpdateAuthorisation();
      localObject = this.database.getGranteeManager().getRole(this.token.tokenString);
      read();
      break;
    case 519: 
      localHsqlName = readNewSchemaName();
      localObject = this.database.schemaManager.findSchema(localHsqlName.name);
      break;
    case 294: 
      int i2 = (this.token.namePrePrefix == null) && (("MODULE".equals(this.token.namePrefix)) || ("SESSION".equals(this.token.namePrefix))) ? 1 : 0;
      if (i2 != 0)
      {
        localHsqlName = readNewSchemaObjectName(j, false);
        if ((!bool2) && (this.token.tokenType == 434))
        {
          read();
          readThis(109);
          bool2 = true;
        }
        arrayOfObject = new Object[] { localHsqlName, Boolean.valueOf(bool2) };
        return new StatementSession(33, arrayOfObject);
      }
      break;
    }
    localHsqlName = readNewSchemaObjectName(j, false);
    localHsqlName.setSchemaIfNull(this.session.getCurrentSchemaHsqlName());
    Object localObject = this.database.schemaManager.findSchemaObject(localHsqlName.name, localHsqlName.schema.name, localHsqlName.type);
    if ((!bool2) && (n != 0) && (this.token.tokenType == 434))
    {
      read();
      readThis(109);
      bool2 = true;
    }
    if (m != 0) {
      if (this.token.tokenType == 368)
      {
        bool1 = true;
        read();
        if (this.database.sqlSyntaxOra) {
          readIfThis(394);
        }
      }
      else if (this.token.tokenType == 507)
      {
        read();
      }
    }
    HsqlNameManager.HsqlName[] arrayOfHsqlName;
    if (localObject == null)
    {
      arrayOfHsqlName = null;
    }
    else
    {
      localHsqlName = ((SchemaObject)localObject).getName();
      arrayOfHsqlName = this.database.schemaManager.getCatalogAndBaseTableNames(localHsqlName);
    }
    String str = getLastPart();
    Object[] arrayOfObject = { localHsqlName, Integer.valueOf(j), Boolean.valueOf(bool1), Boolean.valueOf(bool2) };
    StatementSchema localStatementSchema = new StatementSchema(str, k, arrayOfObject, null, arrayOfHsqlName);
    return localStatementSchema;
  }
  
  Statement compileAlterTable()
  {
    read();
    String str = this.token.tokenString;
    HsqlNameManager.HsqlName localHsqlName = this.session.getSchemaHsqlName(this.token.namePrefix);
    checkSchemaUpdateAuthorisation(localHsqlName);
    Table localTable = this.database.schemaManager.getUserTable(str, localHsqlName.name);
    read();
    Object localObject2;
    Object localObject1;
    switch (this.token.tokenType)
    {
    case 652: 
      read();
      if ((this.database.sqlSyntaxPgs) && (this.token.tokenType == 46))
      {
        read();
        checkIsIdentifier();
        int i = localTable.getColumnIndex(this.token.tokenString);
        localObject2 = localTable.getColumn(i);
        read();
        readThis(301);
        return compileAlterColumnRename(localTable, (ColumnSchema)localObject2);
      }
      readThis(301);
      return compileRenameObject(localTable.getName(), 3);
    case 355: 
      read();
      localObject1 = null;
      localObject2 = Boolean.FALSE;
      if (this.token.tokenType == 51)
      {
        read();
        localObject2 = readIfNotExists();
        localObject1 = readNewDependentSchemaObjectName(localTable.getName(), 5);
      }
      switch (this.token.tokenType)
      {
      case 121: 
        read();
        readThis(449);
        return compileAlterTableAddForeignKeyConstraint(localTable, (HsqlNameManager.HsqlName)localObject1, (Boolean)localObject2);
      case 315: 
        read();
        if ((this.database.sqlSyntaxMys) && (!readIfThis(621))) {
          readIfThis(449);
        }
        return compileAlterTableAddUniqueConstraint(localTable, (HsqlNameManager.HsqlName)localObject1, (Boolean)localObject2);
      case 40: 
        read();
        return compileAlterTableAddCheckConstraint(localTable, (HsqlNameManager.HsqlName)localObject1, (Boolean)localObject2);
      case 228: 
        read();
        readThis(449);
        return compileAlterTableAddPrimaryKey(localTable, (HsqlNameManager.HsqlName)localObject1, (Boolean)localObject2);
      case 46: 
        if (localObject1 != null) {
          throw unexpectedToken();
        }
        read();
        checkIsSimpleName();
        return compileAlterTableAddColumn(localTable);
      }
      if (localObject1 != null) {
        throw unexpectedToken();
      }
      checkIsSimpleName();
      return compileAlterTableAddColumn(localTable);
    case 93: 
      read();
      switch (this.token.tokenType)
      {
      case 228: 
        read();
        readThis(449);
        return compileAlterTableDropPrimaryKey(localTable);
      case 51: 
        read();
        return compileAlterTableDropConstraint(localTable);
      case 46: 
        read();
      }
      checkIsSimpleName();
      localObject1 = this.token.tokenString;
      boolean bool = false;
      read();
      if (this.token.tokenType == 507)
      {
        read();
      }
      else if (this.token.tokenType == 368)
      {
        read();
        bool = true;
      }
      return compileAlterTableDropColumn(localTable, (String)localObject1, bool);
    case 4: 
      read();
      if (this.token.tokenType == 46) {
        read();
      }
      int j = localTable.getColumnIndex(this.token.tokenString);
      ColumnSchema localColumnSchema = localTable.getColumn(j);
      read();
      return compileAlterColumn(localTable, localColumnSchema, j);
    }
    throw unexpectedToken();
  }
  
  private Statement compileAlterTableDropConstraint(Table paramTable)
  {
    boolean bool = false;
    SchemaObject localSchemaObject = readSchemaObjectName(paramTable.getSchemaName(), 5);
    if (this.token.tokenType == 507)
    {
      read();
    }
    else if (this.token.tokenType == 368)
    {
      read();
      bool = true;
    }
    String str = getLastPart();
    Object[] arrayOfObject = { localSchemaObject.getName(), ValuePool.getInt(5), Boolean.valueOf(bool), Boolean.FALSE };
    HsqlNameManager.HsqlName[] arrayOfHsqlName = this.database.schemaManager.getCatalogAndBaseTableNames(paramTable.getName());
    HsqlNameManager.HsqlName localHsqlName = ((Constraint)localSchemaObject).getMainTableName();
    if ((localHsqlName != null) && (localHsqlName != paramTable.getName())) {
      arrayOfHsqlName = (HsqlNameManager.HsqlName[])ArrayUtil.toAdjustedArray(arrayOfHsqlName, localHsqlName, arrayOfHsqlName.length, 1);
    }
    StatementSchema localStatementSchema = new StatementSchema(str, 1130, arrayOfObject, null, arrayOfHsqlName);
    return localStatementSchema;
  }
  
  private Statement compileAlterTableDropPrimaryKey(Table paramTable)
  {
    boolean bool = false;
    if (this.token.tokenType == 507)
    {
      read();
    }
    else if (this.token.tokenType == 368)
    {
      read();
      bool = true;
    }
    if (!paramTable.hasPrimaryKey()) {
      throw Error.error(5501);
    }
    String str = getLastPart();
    Constraint localConstraint = paramTable.getPrimaryConstraint();
    Object[] arrayOfObject = { localConstraint.getName(), ValuePool.getInt(5), Boolean.valueOf(bool), Boolean.FALSE };
    HsqlNameManager.HsqlName[] arrayOfHsqlName = this.database.schemaManager.getCatalogAndBaseTableNames(paramTable.getName());
    StatementSchema localStatementSchema = new StatementSchema(str, 1130, arrayOfObject, null, arrayOfHsqlName);
    return localStatementSchema;
  }
  
  StatementSession compileDeclareLocalTableOrNull()
  {
    int i = getPosition();
    try
    {
      readThis(82);
      readThis(167);
      readThis(545);
      readThis(294);
    }
    catch (HsqlException localHsqlException)
    {
      this.lastError = localHsqlException;
      rewind(i);
      return null;
    }
    if ((this.token.namePrePrefix != null) || ((this.token.namePrefix != null) && (!"MODULE".equals(this.token.namePrefix)) && (!"SESSION".equals(this.token.namePrefix)))) {
      throw unexpectedToken();
    }
    boolean bool = readIfNotExists().booleanValue();
    HsqlNameManager.HsqlName localHsqlName = readNewSchemaObjectName(3, false);
    localHsqlName.schema = SqlInvariants.MODULE_HSQLNAME;
    Table localTable = new Table(this.database, localHsqlName, 3);
    StatementSchema localStatementSchema;
    if (this.token.tokenType == 11)
    {
      localStatementSchema = compileCreateTableAsSubqueryDefinition(localTable);
    }
    else
    {
      localStatementSchema = compileCreateTableBody(localTable, bool);
      localObject = (HsqlArrayList)localStatementSchema.arguments[1];
      for (int j = 0; j < ((HsqlArrayList)localObject).size(); j++)
      {
        Constraint localConstraint = (Constraint)((HsqlArrayList)localObject).get(j);
        if (localConstraint.getConstraintType() == 0) {
          throw unexpectedToken("FOREIGN");
        }
      }
    }
    Object localObject = new StatementSession(1119, localStatementSchema.arguments);
    return (StatementSession)localObject;
  }
  
  StatementSchema compileCreateView(boolean paramBoolean1, boolean paramBoolean2)
  {
    read();
    Boolean localBoolean = Boolean.FALSE;
    if (!paramBoolean1) {
      localBoolean = readIfNotExists();
    }
    HsqlNameManager.HsqlName localHsqlName = readNewSchemaObjectName(4, true);
    localHsqlName.setSchemaIfNull(this.session.getCurrentSchemaHsqlName());
    checkSchemaUpdateAuthorisation(localHsqlName.schema);
    HsqlNameManager.HsqlName[] arrayOfHsqlName1 = null;
    if (this.token.tokenType == 836) {
      try
      {
        arrayOfHsqlName1 = readColumnNames(localHsqlName);
      }
      catch (HsqlException localHsqlException1)
      {
        if ((this.session.isProcessingScript()) && (this.database.getProperties().isVersion18())) {}
        while (this.token.tokenType != 11)
        {
          read();
          continue;
          throw localHsqlException1;
        }
      }
    }
    readThis(11);
    startRecording();
    Object localObject1;
    try
    {
      this.isViewDefinition = true;
      localObject1 = XreadQueryExpression();
    }
    catch (HsqlException localHsqlException2)
    {
      localObject1 = XreadJoinedTableAsView();
    }
    finally
    {
      this.isViewDefinition = false;
    }
    Token[] arrayOfToken = getRecordedStatement();
    int i = 0;
    if (this.token.tokenType == 336)
    {
      read();
      i = 2;
      if (readIfThis(167)) {
        i = 1;
      } else {
        readIfThis(31);
      }
      readThis(40);
      readThis(477);
    }
    View localView = new View(this.database, localHsqlName, arrayOfHsqlName1, i);
    ((QueryExpression)localObject1).setView(localView);
    ((QueryExpression)localObject1).resolve(this.session);
    localView.setStatement(Token.getSQL(arrayOfToken));
    StatementQuery localStatementQuery = new StatementQuery(this.session, (QueryExpression)localObject1, this.compileContext);
    String str = getLastPart();
    Object[] arrayOfObject = { localView, localBoolean };
    int j = paramBoolean1 ? 1122 : 96;
    HsqlNameManager.HsqlName[] arrayOfHsqlName2 = this.database.schemaManager.catalogNameArray;
    return new StatementSchema(str, j, arrayOfObject, localStatementQuery.readTableNames, arrayOfHsqlName2);
  }
  
  StatementSchema compileCreateSequence()
  {
    read();
    Boolean localBoolean = readIfNotExists();
    HsqlNameManager.HsqlName localHsqlName = readNewSchemaObjectName(7, false);
    NumberSequence localNumberSequence = new NumberSequence(localHsqlName, Type.SQL_INTEGER);
    readSequenceOptions(localNumberSequence, true, false, false);
    String str = getLastPart();
    Object[] arrayOfObject = { localNumberSequence, localBoolean };
    HsqlNameManager.HsqlName[] arrayOfHsqlName = this.database.schemaManager.catalogNameArray;
    return new StatementSchema(str, 71, arrayOfObject, null, arrayOfHsqlName);
  }
  
  StatementSchema compileCreateDomain()
  {
    UserTypeModifier localUserTypeModifier = null;
    read();
    HsqlNameManager.HsqlName localHsqlName = readNewSchemaObjectName(13, false);
    readIfThis(11);
    Type localType = readTypeDefinition(true, false).duplicate();
    Expression localExpression = null;
    if (readIfThis(83)) {
      localExpression = readDefaultClause(localType);
    }
    localUserTypeModifier = new UserTypeModifier(localHsqlName, 13, localType);
    localUserTypeModifier.setDefaultClause(localExpression);
    localType.userTypeModifier = localUserTypeModifier;
    HsqlArrayList localHsqlArrayList = new HsqlArrayList();
    this.compileContext.currentDomain = localType;
    for (;;)
    {
      i = 0;
      switch (this.token.tokenType)
      {
      case 40: 
      case 51: 
        readConstraint(localType, localHsqlArrayList);
        break;
      default: 
        i = 1;
      }
      if (i != 0) {
        break;
      }
    }
    this.compileContext.currentDomain = null;
    for (int i = 0; i < localHsqlArrayList.size(); i++)
    {
      localObject = (Constraint)localHsqlArrayList.get(i);
      ((Constraint)localObject).prepareCheckConstraint(this.session, null);
      localUserTypeModifier.addConstraint((Constraint)localObject);
    }
    String str = getLastPart();
    Object localObject = { localType };
    HsqlNameManager.HsqlName[] arrayOfHsqlName = this.database.schemaManager.catalogNameArray;
    return new StatementSchema(str, 23, (Object[])localObject, null, arrayOfHsqlName);
  }
  
  StatementSchema compileCreateType(boolean paramBoolean)
  {
    read();
    HsqlNameManager.HsqlName localHsqlName = readNewSchemaObjectName(12, false);
    readThis(11);
    Type localType = readTypeDefinition(true, false).duplicate();
    readIfThis(422);
    UserTypeModifier localUserTypeModifier = new UserTypeModifier(localHsqlName, 12, localType);
    localType.userTypeModifier = localUserTypeModifier;
    String str = getLastPart();
    Object[] arrayOfObject = { localType };
    HsqlNameManager.HsqlName[] arrayOfHsqlName = this.database.schemaManager.catalogNameArray;
    return new StatementSchema(str, 94, arrayOfObject, null, arrayOfHsqlName);
  }
  
  StatementSchema compileCreateCharacterSet()
  {
    read();
    readThis(268);
    HsqlNameManager.HsqlName localHsqlName = readNewSchemaObjectName(14, false);
    readIfThis(11);
    readThis(128);
    String str1 = this.token.namePrefix;
    Charset localCharset1 = (Charset)this.database.schemaManager.getCharacterSet(this.session, this.token.tokenString, str1);
    read();
    if (this.token.tokenType == 379)
    {
      read();
      readThis(124);
      readThis(83);
    }
    Charset localCharset2 = new Charset(localHsqlName);
    localCharset2.base = localCharset1.getName();
    String str2 = getLastPart();
    Object[] arrayOfObject = { localCharset2 };
    HsqlNameManager.HsqlName[] arrayOfHsqlName = this.database.schemaManager.catalogNameArray;
    return new StatementSchema(str2, 11, arrayOfObject, null, arrayOfHsqlName);
  }
  
  StatementSchema compileCreateCollation()
  {
    read();
    HsqlNameManager.HsqlName localHsqlName1 = readNewSchemaObjectName(15, false);
    localHsqlName1.setSchemaIfNull(this.session.getCurrentSchemaHsqlName());
    readThis(120);
    HsqlNameManager.HsqlName localHsqlName2 = readNewSchemaObjectName(14, false);
    readThis(124);
    HsqlNameManager.HsqlName localHsqlName3 = readNewSchemaObjectName(15, false);
    Boolean localBoolean = null;
    if (readIfThis(190))
    {
      readThis(485);
      localBoolean = Boolean.FALSE;
    }
    else if (readIfThis(485))
    {
      readThis(536);
      localBoolean = Boolean.TRUE;
    }
    String str1 = localHsqlName2.schema == null ? null : localHsqlName2.schema.name;
    Charset localCharset = (Charset)this.database.schemaManager.getCharacterSet(this.session, localHsqlName2.name, str1);
    if (localCharset == null) {
      throw Error.error(5501, localHsqlName2.getSchemaQualifiedStatementName());
    }
    str1 = localHsqlName3.schema == null ? null : localHsqlName3.schema.name;
    Collation localCollation1 = this.database.schemaManager.getCollation(this.session, localHsqlName3.name, str1);
    Collation localCollation2 = new Collation(localHsqlName1, localCollation1, localCharset, localBoolean);
    String str2 = getLastPart();
    Object[] arrayOfObject = { localCollation2 };
    HsqlNameManager.HsqlName[] arrayOfHsqlName = this.database.schemaManager.catalogNameArray;
    return new StatementSchema(str2, 13, arrayOfObject, null, arrayOfHsqlName);
  }
  
  StatementSchema compileCreateAlias()
  {
    HsqlNameManager.HsqlName localHsqlName = null;
    Routine[] arrayOfRoutine = null;
    String str2 = null;
    if (!this.session.isProcessingScript()) {
      throw unsupportedFeature();
    }
    read();
    String str1;
    try
    {
      str1 = this.token.tokenString;
      read();
      readThis(120);
      str2 = this.token.tokenString;
      read();
    }
    catch (HsqlException localHsqlException)
    {
      str1 = null;
    }
    if (str1 != null)
    {
      localObject1 = this.database.schemaManager.getDefaultSchemaHsqlName();
      localHsqlName = this.database.nameManager.newHsqlName((HsqlNameManager.HsqlName)localObject1, str1, 16);
      localObject2 = Routine.getMethods(str2);
      arrayOfRoutine = Routine.newRoutines(this.session, (Method[])localObject2);
    }
    Object localObject1 = getLastPart();
    Object localObject2 = { localHsqlName, arrayOfRoutine };
    HsqlNameManager.HsqlName[] arrayOfHsqlName = this.database.schemaManager.catalogNameArray;
    return new StatementSchema((String)localObject1, 1124, (Object[])localObject2, null, arrayOfHsqlName);
  }
  
  StatementSchema compileCreateIndex(boolean paramBoolean)
  {
    read();
    Boolean localBoolean = readIfNotExists();
    HsqlNameManager.HsqlName localHsqlName1 = readNewSchemaObjectName(20, true);
    if ((this.database.sqlSyntaxMys) && (readIfThis(322)) && (!readIfThis("HASH"))) {
      readThis("BTREE");
    }
    readThis(204);
    Table localTable = readTableName();
    HsqlNameManager.HsqlName localHsqlName2 = localTable.getSchemaName();
    localHsqlName1.setSchemaIfNull(localHsqlName2);
    localHsqlName1.parent = localTable.getName();
    if (localHsqlName1.schema != localHsqlName2) {
      throw Error.error(5505);
    }
    localHsqlName1.schema = localTable.getSchemaName();
    int[] arrayOfInt = readColumnList(localTable, true);
    if (this.database.sqlSyntaxMys)
    {
      if ((readIfThis(322)) && (!readIfThis("HASH"))) {
        readThis("BTREE");
      }
      if (readIfThis(599))
      {
        str = readQuotedString();
        localHsqlName1.comment = str;
      }
    }
    String str = getLastPart();
    Object[] arrayOfObject = { localTable, arrayOfInt, localHsqlName1, Boolean.valueOf(paramBoolean), null, localBoolean };
    return new StatementSchema(str, 1125, arrayOfObject, null, new HsqlNameManager.HsqlName[] { this.database.getCatalogName(), localTable.getName() });
  }
  
  StatementSchema compileCreateSchema()
  {
    HsqlNameManager.HsqlName localHsqlName1 = null;
    String str1 = null;
    HsqlNameManager.HsqlName localHsqlName2 = null;
    read();
    if (this.token.tokenType != 16) {
      localHsqlName1 = readNewSchemaName();
    }
    if (this.token.tokenType == 16)
    {
      read();
      checkIsSimpleName();
      str1 = this.token.tokenString;
      read();
      if (localHsqlName1 == null)
      {
        localGrantee = this.database.getGranteeManager().get(str1);
        if (localGrantee == null) {
          throw Error.error(4001, str1);
        }
        localHsqlName1 = this.database.nameManager.newHsqlName(localGrantee.getName().name, isDelimitedIdentifier(), 2);
        SqlInvariants.checkSchemaNameNotSystem(this.token.tokenString);
      }
    }
    if ("PUBLIC".equals(str1)) {
      throw Error.error(4002, str1);
    }
    Grantee localGrantee = str1 == null ? this.session.getGrantee() : this.database.getGranteeManager().get(str1);
    if (localGrantee == null) {
      throw Error.error(4001, str1);
    }
    if (!this.session.getGrantee().isSchemaCreator()) {
      throw Error.error(2051, this.session.getGrantee().getName().getNameString());
    }
    if (((localGrantee instanceof User)) && (((User)localGrantee).isExternalOnly)) {
      throw Error.error(2000, this.session.getGrantee().getName().getNameString());
    }
    if ((this.database.schemaManager.schemaExists(localHsqlName1.name)) && ((!this.session.isProcessingScript()) || (!"PUBLIC".equals(localHsqlName1.name)))) {
      throw Error.error(5504, localHsqlName1.name);
    }
    if (localHsqlName1.name.equals("SYSTEM_LOBS"))
    {
      localHsqlName1 = SqlInvariants.LOBS_SCHEMA_HSQLNAME;
      localGrantee = localHsqlName1.owner;
    }
    if (readIfThis(83))
    {
      readThis(38);
      readThis(268);
      localHsqlName2 = readNewSchemaObjectName(14, false);
    }
    String str2 = getLastPart();
    Object[] arrayOfObject = { localHsqlName1, localGrantee };
    HsqlNameManager.HsqlName[] arrayOfHsqlName = this.database.schemaManager.catalogNameArray;
    StatementSchema localStatementSchema1 = new StatementSchema(str2, 68, arrayOfObject, null, arrayOfHsqlName);
    localStatementSchema1.setSchemaHsqlName(localHsqlName1);
    HsqlArrayList localHsqlArrayList = new HsqlArrayList();
    localHsqlArrayList.add(localStatementSchema1);
    getCompiledStatementBody(localHsqlArrayList);
    StatementSchema[] arrayOfStatementSchema = new StatementSchema[localHsqlArrayList.size()];
    localHsqlArrayList.toArray(arrayOfStatementSchema);
    int i;
    do
    {
      i = 0;
      for (int j = 0; j < arrayOfStatementSchema.length - 1; j++) {
        if (arrayOfStatementSchema[j].order > arrayOfStatementSchema[(j + 1)].order)
        {
          StatementSchema localStatementSchema2 = arrayOfStatementSchema[(j + 1)];
          arrayOfStatementSchema[(j + 1)] = arrayOfStatementSchema[j];
          arrayOfStatementSchema[j] = localStatementSchema2;
          i = 1;
        }
      }
    } while (i != 0);
    return new StatementSchemaDefinition(arrayOfStatementSchema);
  }
  
  void getCompiledStatementBody(HsqlList paramHsqlList)
  {
    int k = 0;
    while (k == 0)
    {
      StatementSchema localStatementSchema = null;
      int i = getPosition();
      switch (this.token.tokenType)
      {
      case 59: 
        read();
        int j;
        String str;
        switch (this.token.tokenType)
        {
        case 315: 
        case 321: 
        case 519: 
          throw unexpectedToken();
        case 621: 
          j = 1125;
          str = getStatement(i, startStatementTokensSchema);
          localStatementSchema = new StatementSchema(str, j);
          break;
        case 527: 
          localStatementSchema = compileCreateSequence();
          localStatementSchema.sql = getLastPart(i);
          break;
        case 512: 
          localStatementSchema = compileCreateRole();
          localStatementSchema.sql = getLastPart(i);
          break;
        case 414: 
          j = 23;
          str = getStatement(i, startStatementTokensSchema);
          localStatementSchema = new StatementSchema(str, j);
          break;
        case 557: 
          localStatementSchema = compileCreateType(false);
          localStatementSchema.sql = getLastPart(i);
          break;
        case 38: 
          localStatementSchema = compileCreateCharacterSet();
          localStatementSchema.sql = getLastPart(i);
          break;
        case 360: 
          throw unexpectedToken();
        case 129: 
        case 294: 
        case 545: 
        case 593: 
        case 635: 
        case 661: 
        case 662: 
          j = 87;
          str = getStatement(i, startStatementTokensSchema);
          localStatementSchema = new StatementSchema(str, j);
          break;
        case 307: 
          j = 90;
          str = getStatement(i, startStatementTokensSchema);
          localStatementSchema = new StatementSchema(str, j);
          break;
        case 567: 
          j = 96;
          str = getStatement(i, startStatementTokensSchema);
          localStatementSchema = new StatementSchema(str, j);
          break;
        case 126: 
          j = 69;
          str = getStatementForRoutine(i, startStatementTokensSchema);
          localStatementSchema = new StatementSchema(str, j);
          break;
        case 229: 
          j = 69;
          str = getStatementForRoutine(i, startStatementTokensSchema);
          localStatementSchema = new StatementSchema(str, j);
          break;
        default: 
          throw unexpectedToken();
        }
        break;
      case 130: 
        localStatementSchema = compileGrantOrRevoke();
        localStatementSchema.sql = getLastPart(i);
        break;
      case 841: 
        read();
        k = 1;
        break;
      case 914: 
        k = 1;
        break;
      default: 
        throw unexpectedToken();
      }
      if (localStatementSchema != null)
      {
        localStatementSchema.isSchemaDefinition = true;
        paramHsqlList.add(localStatementSchema);
      }
    }
  }
  
  StatementSchema compileCreateRole()
  {
    read();
    HsqlNameManager.HsqlName localHsqlName = readNewUserIdentifier();
    String str = getLastPart();
    Object[] arrayOfObject = { localHsqlName };
    HsqlNameManager.HsqlName[] arrayOfHsqlName = this.database.schemaManager.catalogNameArray;
    return new StatementSchema(str, 65, arrayOfObject, null, arrayOfHsqlName);
  }
  
  StatementSchema compileCreateUser()
  {
    Boolean localBoolean1 = Boolean.FALSE;
    Boolean localBoolean2 = Boolean.FALSE;
    Grantee localGrantee = this.session.getGrantee();
    read();
    HsqlNameManager.HsqlName localHsqlName = readNewUserIdentifier();
    readThis(643);
    if (readIfThis(609)) {
      localBoolean2 = Boolean.TRUE;
    }
    String str1 = readPassword();
    if (this.token.tokenType == 356)
    {
      read();
      localBoolean1 = Boolean.TRUE;
    }
    checkDatabaseUpdateAuthorisation();
    String str2 = getLastPart();
    Object[] arrayOfObject = { localHsqlName, str1, localGrantee, localBoolean1, localBoolean2 };
    HsqlNameManager.HsqlName[] arrayOfHsqlName = this.database.schemaManager.catalogNameArray;
    return new StatementSchema(str2, 1126, arrayOfObject, null, arrayOfHsqlName);
  }
  
  HsqlNameManager.HsqlName readNewUserIdentifier()
  {
    checkIsSimpleName();
    String str = this.token.tokenString;
    boolean bool = isDelimitedIdentifier();
    if (str.equalsIgnoreCase("SA"))
    {
      str = "SA";
      bool = false;
    }
    HsqlNameManager.HsqlName localHsqlName = this.database.nameManager.newHsqlName(str, bool, 11);
    read();
    return localHsqlName;
  }
  
  String readPassword()
  {
    String str = this.token.tokenString;
    if ((isUndelimitedSimpleName()) || (isDelimitedSimpleName())) {
      read();
    } else {
      readQuotedString();
    }
    return str;
  }
  
  StatementSchema compileCreateSynonym(boolean paramBoolean)
  {
    read();
    HsqlNameManager.HsqlName localHsqlName1 = readNewSchemaObjectName(29, true);
    readThis(120);
    HsqlNameManager.HsqlName localHsqlName2 = readNewSchemaObjectName(29, true);
    String str = getLastPart();
    Object[] arrayOfObject = { localHsqlName1, localHsqlName2 };
    return new StatementSchema(str, 1146, arrayOfObject, null, new HsqlNameManager.HsqlName[] { this.database.getCatalogName() });
  }
  
  Statement compileRenameObject(HsqlNameManager.HsqlName paramHsqlName, int paramInt)
  {
    HsqlNameManager.HsqlName localHsqlName = readNewSchemaObjectName(paramInt, true);
    String str = getLastPart();
    switch (paramInt)
    {
    case 1: 
      break;
    case 2: 
      checkSchemaUpdateAuthorisation(this.session, paramHsqlName);
      break;
    default: 
      paramHsqlName.setSchemaIfNull(this.session.getCurrentSchemaHsqlName());
      checkSchemaUpdateAuthorisation(this.session, paramHsqlName.schema);
    }
    Object[] arrayOfObject = { paramHsqlName, localHsqlName };
    HsqlNameManager.HsqlName[] arrayOfHsqlName = this.database.schemaManager.getCatalogNameArray();
    return new StatementSchema(str, 1152, arrayOfObject, null, arrayOfHsqlName);
  }
  
  Statement compileAlterTableAddUniqueConstraint(Table paramTable, HsqlNameManager.HsqlName paramHsqlName, Boolean paramBoolean)
  {
    if (paramHsqlName == null) {
      paramHsqlName = this.database.nameManager.newAutoName("CT", paramTable.getSchemaName(), paramTable.getName(), 5);
    }
    int[] arrayOfInt = readColumnList(paramTable, false);
    HsqlNameManager.HsqlName localHsqlName = this.database.nameManager.newAutoName("IDX", paramHsqlName.name, paramTable.getSchemaName(), paramTable.getName(), 20);
    Index localIndex = paramTable.createIndexStructure(localHsqlName, arrayOfInt, null, null, true, true, false);
    Constraint localConstraint = new Constraint(paramHsqlName, paramTable, localIndex, 2);
    String str = getLastPart();
    Object[] arrayOfObject = { Integer.valueOf(1134), paramTable, localConstraint, paramBoolean };
    HsqlNameManager.HsqlName[] arrayOfHsqlName = this.database.schemaManager.getCatalogAndBaseTableNames(paramTable.getName());
    return new StatementSchema(str, 7, arrayOfObject, null, arrayOfHsqlName);
  }
  
  Statement compileAlterTableAddForeignKeyConstraint(Table paramTable, HsqlNameManager.HsqlName paramHsqlName, Boolean paramBoolean)
  {
    if (paramHsqlName == null) {
      paramHsqlName = this.database.nameManager.newAutoName("FK", paramTable.getSchemaName(), paramTable.getName(), 5);
    }
    OrderedHashSet localOrderedHashSet = readColumnNames(false);
    Constraint localConstraint = readFKReferences(paramTable, paramHsqlName, localOrderedHashSet);
    HsqlNameManager.HsqlName localHsqlName = localConstraint.getMainTableName();
    localConstraint.core.mainTable = this.database.schemaManager.getUserTable(localHsqlName.name, localHsqlName.schema.name);
    localConstraint.setColumnsIndexes(paramTable);
    if (localConstraint.core.mainCols.length != localConstraint.core.refCols.length) {
      throw Error.error(5593);
    }
    String str = getLastPart();
    Object[] arrayOfObject = { Integer.valueOf(1134), paramTable, localConstraint, paramBoolean };
    HsqlNameManager.HsqlName[] arrayOfHsqlName = this.database.schemaManager.getCatalogAndBaseTableNames(paramTable.getName());
    if (localHsqlName != paramTable.getName()) {
      arrayOfHsqlName = (HsqlNameManager.HsqlName[])ArrayUtil.toAdjustedArray(arrayOfHsqlName, localHsqlName, arrayOfHsqlName.length, 1);
    }
    return new StatementSchema(str, 7, arrayOfObject, null, arrayOfHsqlName);
  }
  
  Statement compileAlterTableAddCheckConstraint(Table paramTable, HsqlNameManager.HsqlName paramHsqlName, Boolean paramBoolean)
  {
    if (paramHsqlName == null) {
      paramHsqlName = this.database.nameManager.newAutoName("CT", paramTable.getSchemaName(), paramTable.getName(), 5);
    }
    Constraint localConstraint = new Constraint(paramHsqlName, null, 3);
    readCheckConstraintCondition(localConstraint);
    String str = getLastPart();
    Object[] arrayOfObject = { Integer.valueOf(1134), paramTable, localConstraint, paramBoolean };
    HsqlNameManager.HsqlName[] arrayOfHsqlName = { this.database.getCatalogName(), paramTable.getName() };
    return new StatementSchema(str, 7, arrayOfObject, null, arrayOfHsqlName);
  }
  
  Statement compileAlterTableAddColumn(Table paramTable)
  {
    int i = paramTable.getColumnCount();
    HsqlArrayList localHsqlArrayList = new HsqlArrayList();
    Constraint localConstraint = new Constraint(null, null, 5);
    localHsqlArrayList.add(localConstraint);
    checkIsSchemaObjectName();
    HsqlNameManager.HsqlName localHsqlName = this.database.nameManager.newColumnHsqlName(paramTable.getName(), this.token.tokenString, isDelimitedIdentifier());
    read();
    ColumnSchema localColumnSchema = readColumnDefinitionOrNull(paramTable, localHsqlName, localHsqlArrayList);
    if (localColumnSchema == null) {
      throw Error.error(5000);
    }
    if (this.token.tokenType == 364)
    {
      read();
      i = paramTable.getColumnIndex(this.token.tokenString);
      read();
    }
    String str = getLastPart();
    Object[] arrayOfObject = { Integer.valueOf(1133), paramTable, localColumnSchema, Integer.valueOf(i), localHsqlArrayList };
    HsqlNameManager.HsqlName[] arrayOfHsqlName = this.database.schemaManager.getCatalogAndBaseTableNames(paramTable.getName());
    return new StatementSchema(str, 7, arrayOfObject, null, arrayOfHsqlName);
  }
  
  Statement compileAlterTableAddPrimaryKey(Table paramTable, HsqlNameManager.HsqlName paramHsqlName, Boolean paramBoolean)
  {
    if (paramHsqlName == null) {
      paramHsqlName = this.session.database.nameManager.newAutoName("PK", paramTable.getSchemaName(), paramTable.getName(), 5);
    }
    OrderedHashSet localOrderedHashSet = readColumnNames(false);
    Constraint localConstraint = new Constraint(paramHsqlName, localOrderedHashSet, 4);
    localConstraint.setColumnsIndexes(paramTable);
    String str = getLastPart();
    Object[] arrayOfObject = { Integer.valueOf(1134), paramTable, localConstraint, paramBoolean };
    HsqlNameManager.HsqlName[] arrayOfHsqlName = this.database.schemaManager.getCatalogAndBaseTableNames(paramTable.getName());
    return new StatementSchema(str, 7, arrayOfObject, null, arrayOfHsqlName);
  }
  
  Statement compileAlterTableDropColumn(Table paramTable, String paramString, boolean paramBoolean)
  {
    int i = paramTable.getColumnIndex(paramString);
    if (paramTable.getColumnCount() == 1) {
      throw Error.error(5591);
    }
    String str = getLastPart();
    Object[] arrayOfObject = { paramTable.getColumn(i).getName(), ValuePool.getInt(9), Boolean.valueOf(paramBoolean), Boolean.FALSE };
    HsqlNameManager.HsqlName[] arrayOfHsqlName = this.database.schemaManager.getCatalogAndBaseTableNames(paramTable.getName());
    return new StatementSchema(str, 1128, arrayOfObject, null, arrayOfHsqlName);
  }
  
  Statement compileAlterColumn(Table paramTable, ColumnSchema paramColumnSchema, int paramInt)
  {
    HsqlNameManager.HsqlName[] arrayOfHsqlName = this.database.schemaManager.getCatalogAndBaseTableNames(paramTable.getName());
    int i = getPosition();
    Object localObject1;
    Object localObject2;
    switch (this.token.tokenType)
    {
    case 652: 
      read();
      readThis(301);
      return compileAlterColumnRename(paramTable, paramColumnSchema);
    case 93: 
      read();
      if (this.token.tokenType == 83)
      {
        read();
        localObject1 = getLastPart();
        localObject2 = new Object[] { Integer.valueOf(1141), paramTable, paramColumnSchema, Integer.valueOf(paramInt) };
        return new StatementSchema((String)localObject1, 7, (Object[])localObject2, null, arrayOfHsqlName);
      }
      if (this.token.tokenType == 421)
      {
        read();
        localObject1 = getLastPart();
        localObject2 = new Object[] { Integer.valueOf(1142), paramTable, paramColumnSchema, Integer.valueOf(paramInt) };
        return new StatementSchema((String)localObject1, 7, (Object[])localObject2, null, arrayOfHsqlName);
      }
      if ((this.token.tokenType == 429) || (this.token.tokenType == 138))
      {
        read();
        localObject1 = getLastPart();
        localObject2 = new Object[] { Integer.valueOf(1143), paramTable, paramColumnSchema, Integer.valueOf(paramInt) };
        return new StatementSchema((String)localObject1, 7, (Object[])localObject2, null, arrayOfHsqlName);
      }
      if (this.token.tokenType == 193)
      {
        read();
        readThis(196);
        localObject1 = getLastPart();
        localObject2 = new Object[] { Integer.valueOf(1139), paramTable, paramColumnSchema, Boolean.TRUE };
        return new StatementSchema((String)localObject1, 7, (Object[])localObject2, null, arrayOfHsqlName);
      }
      throw unexpectedToken();
    case 268: 
      read();
      switch (this.token.tokenType)
      {
      case 399: 
        read();
        readThis(557);
        return compileAlterColumnDataType(paramTable, paramColumnSchema);
      case 83: 
        read();
        localObject1 = paramColumnSchema.getDataType();
        localObject2 = readDefaultClause((Type)localObject1);
        String str = getLastPart();
        Object[] arrayOfObject = { Integer.valueOf(1140), paramTable, paramColumnSchema, Integer.valueOf(paramInt), localObject2 };
        return new StatementSchema(str, 7, arrayOfObject, null, arrayOfHsqlName);
      case 193: 
        read();
        readThis(196);
        return compileAlterColumnSetNullability(paramTable, paramColumnSchema, false);
      case 196: 
        read();
        return compileAlterColumnSetNullability(paramTable, paramColumnSchema, true);
      case 429: 
        return compileAlterColumnAddSequence(paramTable, paramColumnSchema, paramInt);
      }
      rewind(i);
      read();
      break;
    case 429: 
      return compileAlterColumnAddSequence(paramTable, paramColumnSchema, paramInt);
    }
    if ((this.token.tokenType == 268) || (this.token.tokenType == 506))
    {
      if (!paramColumnSchema.isIdentity()) {
        throw Error.error(5535);
      }
      return compileAlterColumnSequenceOptions(paramTable, paramColumnSchema, paramInt);
    }
    return compileAlterColumnDataTypeIdentity(paramTable, paramColumnSchema);
  }
  
  private Statement compileAlterColumnDataTypeIdentity(Table paramTable, ColumnSchema paramColumnSchema)
  {
    if (paramColumnSchema.isGenerated()) {
      throw Error.error(5561);
    }
    NumberSequence localNumberSequence = paramColumnSchema.getIdentitySequence();
    Type localType = paramColumnSchema.getDataType();
    if (this.token.tokenType == 138)
    {
      read();
      if (!localType.isIntegralType()) {
        throw Error.error(5561);
      }
      if (localNumberSequence == null) {
        localNumberSequence = new NumberSequence(null, localType);
      }
    }
    else
    {
      localType = readTypeDefinition(true, true);
      switch (this.token.tokenType)
      {
      case 138: 
        if (!localType.isIntegralType()) {
          throw Error.error(5561);
        }
        read();
        if (localNumberSequence == null) {
          localNumberSequence = new NumberSequence(null, localType);
        }
        break;
      case 429: 
        localNumberSequence = readSequence(paramColumnSchema);
        break;
      default: 
        localNumberSequence = null;
      }
    }
    String str = getLastPart();
    Object[] arrayOfObject = { Integer.valueOf(1144), paramTable, paramColumnSchema, localType, localNumberSequence };
    HsqlNameManager.HsqlName[] arrayOfHsqlName = this.database.schemaManager.getCatalogAndBaseTableNames(paramTable.getName());
    return new StatementSchema(str, 7, arrayOfObject, null, arrayOfHsqlName);
  }
  
  private Statement compileAlterColumnDataType(Table paramTable, ColumnSchema paramColumnSchema)
  {
    if (paramColumnSchema.isGenerated()) {
      throw Error.error(5561);
    }
    Type localType = readTypeDefinition(true, true);
    if ((paramColumnSchema.isIdentity()) && (!localType.isIntegralType())) {
      throw Error.error(5561);
    }
    String str = getLastPart();
    Object[] arrayOfObject = { Integer.valueOf(1136), paramTable, paramColumnSchema, localType };
    HsqlNameManager.HsqlName[] arrayOfHsqlName = this.database.schemaManager.getCatalogAndBaseTableNames(paramTable.getName());
    return new StatementSchema(str, 7, arrayOfObject, null, arrayOfHsqlName);
  }
  
  private Statement compileAlterColumnSetNullability(Table paramTable, ColumnSchema paramColumnSchema, boolean paramBoolean)
  {
    String str = getLastPart();
    Object[] arrayOfObject = { Integer.valueOf(1139), paramTable, paramColumnSchema, Boolean.valueOf(paramBoolean) };
    HsqlNameManager.HsqlName[] arrayOfHsqlName = this.database.schemaManager.getCatalogAndBaseTableNames(paramTable.getName());
    return new StatementSchema(str, 7, arrayOfObject, null, arrayOfHsqlName);
  }
  
  Statement compileAlterSequence()
  {
    read();
    HsqlNameManager.HsqlName localHsqlName = this.session.getSchemaHsqlName(this.token.namePrefix);
    NumberSequence localNumberSequence1 = this.database.schemaManager.getSequence(this.token.tokenString, localHsqlName.name, true);
    read();
    if (this.token.tokenType == 652)
    {
      read();
      readThis(301);
      return compileRenameObject(localNumberSequence1.getName(), 7);
    }
    checkSchemaUpdateAuthorisation(this.session, localNumberSequence1.getName().schema);
    NumberSequence localNumberSequence2 = localNumberSequence1.duplicate();
    readSequenceOptions(localNumberSequence2, false, true, false);
    String str = getLastPart();
    Object[] arrayOfObject = { localNumberSequence1, localNumberSequence2 };
    HsqlNameManager.HsqlName[] arrayOfHsqlName = this.database.schemaManager.getCatalogNameArray();
    return new StatementSchema(str, 5, arrayOfObject, null, arrayOfHsqlName);
  }
  
  StatementSchema compileAlterColumnAddSequence(Table paramTable, ColumnSchema paramColumnSchema, int paramInt)
  {
    if (!paramColumnSchema.getDataType().isIntegralType()) {
      throw Error.error(5525);
    }
    if (paramColumnSchema.isIdentity()) {
      throw Error.error(5525);
    }
    NumberSequence localNumberSequence = readSequence(paramColumnSchema);
    String str = getLastPart();
    Object[] arrayOfObject = { Integer.valueOf(1137), paramTable, paramColumnSchema, Integer.valueOf(paramInt), localNumberSequence };
    HsqlNameManager.HsqlName[] arrayOfHsqlName = this.database.schemaManager.getCatalogAndBaseTableNames(paramTable.getName());
    return new StatementSchema(str, 7, arrayOfObject, null, arrayOfHsqlName);
  }
  
  NumberSequence readSequence(ColumnSchema paramColumnSchema)
  {
    readThis(429);
    NumberSequence localNumberSequence = new NumberSequence(null, paramColumnSchema.getDataType());
    boolean bool = false;
    if (this.token.tokenType == 27)
    {
      read();
      readThis(83);
    }
    else
    {
      readThis(358);
      bool = true;
    }
    readThis(11);
    readThis(138);
    localNumberSequence.setAlways(bool);
    if (this.token.tokenType == 836)
    {
      read();
      readSequenceOptions(localNumberSequence, false, false, false);
      readThis(822);
    }
    localNumberSequence.checkValues();
    return localNumberSequence;
  }
  
  StatementSchema compileAlterColumnSequenceOptions(Table paramTable, ColumnSchema paramColumnSchema, int paramInt)
  {
    OrderedIntHashSet localOrderedIntHashSet = new OrderedIntHashSet();
    NumberSequence localNumberSequence = paramColumnSchema.getIdentitySequence().duplicate();
    for (;;)
    {
      int i = 0;
      long l;
      switch (this.token.tokenType)
      {
      case 506: 
        if (!localOrderedIntHashSet.add(this.token.tokenType)) {
          throw unexpectedToken();
        }
        read();
        if (readIfThis(336))
        {
          l = readBigint();
          localNumberSequence.setCurrentValueNoCheck(l);
        }
        else
        {
          localNumberSequence.reset();
        }
        break;
      case 268: 
        read();
        switch (this.token.tokenType)
        {
        case 439: 
          if (!localOrderedIntHashSet.add(this.token.tokenType)) {
            throw unexpectedToken();
          }
          read();
          readThis(27);
          l = readBigint();
          localNumberSequence.setIncrement(l);
          break;
        case 190: 
          read();
          if (this.token.tokenType == 460) {
            localNumberSequence.setDefaultMaxValue();
          } else if (this.token.tokenType == 464) {
            localNumberSequence.setDefaultMinValue();
          } else if (this.token.tokenType == 76) {
            localNumberSequence.setCycle(false);
          } else {
            throw unexpectedToken();
          }
          if (!localOrderedIntHashSet.add(this.token.tokenType)) {
            throw unexpectedToken();
          }
          read();
          break;
        case 460: 
          if (!localOrderedIntHashSet.add(this.token.tokenType)) {
            throw unexpectedToken();
          }
          read();
          l = readBigint();
          localNumberSequence.setMaxValueNoCheck(l);
          break;
        case 464: 
          if (!localOrderedIntHashSet.add(this.token.tokenType)) {
            throw unexpectedToken();
          }
          read();
          l = readBigint();
          localNumberSequence.setMinValueNoCheck(l);
          break;
        case 76: 
          if (!localOrderedIntHashSet.add(this.token.tokenType)) {
            throw unexpectedToken();
          }
          read();
          localNumberSequence.setCycle(true);
          break;
        default: 
          throw unexpectedToken();
        }
        break;
      default: 
        i = 1;
      }
      if (i != 0) {
        break;
      }
    }
    localNumberSequence.checkValues();
    String str = getLastPart();
    Object[] arrayOfObject = { Integer.valueOf(1137), paramTable, paramColumnSchema, Integer.valueOf(paramInt), localNumberSequence };
    HsqlNameManager.HsqlName[] arrayOfHsqlName = { this.database.getCatalogName(), paramTable.getName() };
    return new StatementSchema(str, 7, arrayOfObject, null, arrayOfHsqlName);
  }
  
  private Statement compileAlterColumnRename(Table paramTable, ColumnSchema paramColumnSchema)
  {
    checkIsSimpleName();
    HsqlNameManager.HsqlName localHsqlName = readNewSchemaObjectName(9, true);
    if (paramTable.findColumn(localHsqlName.name) > -1) {
      throw Error.error(5504, localHsqlName.name);
    }
    this.database.schemaManager.checkColumnIsReferenced(paramTable.getName(), paramColumnSchema.getName());
    String str = getLastPart();
    Object[] arrayOfObject = { paramColumnSchema.getName(), localHsqlName };
    HsqlNameManager.HsqlName[] arrayOfHsqlName = { this.database.getCatalogName(), paramTable.getName() };
    return new StatementSchema(str, 1152, arrayOfObject, null, arrayOfHsqlName);
  }
  
  Statement compileAlterSchemaRename()
  {
    HsqlNameManager.HsqlName localHsqlName1 = readSchemaName();
    checkSchemaUpdateAuthorisation(localHsqlName1);
    readThis(652);
    readThis(301);
    HsqlNameManager.HsqlName localHsqlName2 = readNewSchemaName();
    String str = getLastPart();
    Object[] arrayOfObject = { localHsqlName1, localHsqlName2 };
    HsqlNameManager.HsqlName[] arrayOfHsqlName = this.database.schemaManager.getCatalogNameArray();
    return new StatementSchema(str, 1152, arrayOfObject, null, arrayOfHsqlName);
  }
  
  Statement compileAlterUser()
  {
    read();
    HsqlNameManager.HsqlName localHsqlName1 = readNewUserIdentifier();
    User localUser = this.database.getUserManager().get(localHsqlName1.name);
    if (localHsqlName1.name.equals("PUBLIC")) {
      throw Error.error(5503);
    }
    if (localHsqlName1.name.equals("_SYSTEM")) {
      throw Error.error(5503);
    }
    readThis(268);
    Object[] arrayOfObject;
    switch (this.token.tokenType)
    {
    case 167: 
      read();
      Boolean localBoolean = processTrueOrFalseObject();
      arrayOfObject = new Object[] { localUser, localBoolean };
      return new StatementCommand(1091, arrayOfObject);
    case 643: 
      read();
      boolean bool = false;
      if (readIfThis(609)) {
        bool = Boolean.TRUE.booleanValue();
      }
      String str1 = readPassword();
      arrayOfObject = new Object[] { localUser, str1, Boolean.valueOf(bool) };
      StatementCommand localStatementCommand = new StatementCommand(1093, arrayOfObject);
      String str2 = localUser.getSetUserPasswordDigestSQL(str1, bool);
      localStatementCommand.setSQL(str2);
      return localStatementCommand;
    case 623: 
      read();
      readThis(519);
      HsqlNameManager.HsqlName localHsqlName2;
      if (this.token.tokenType == 83) {
        localHsqlName2 = null;
      } else {
        localHsqlName2 = this.database.schemaManager.getSchemaHsqlName(this.token.tokenString);
      }
      read();
      arrayOfObject = new Object[] { localUser, localHsqlName2 };
      return new StatementCommand(1092, arrayOfObject);
    }
    throw unexpectedToken();
  }
  
  Statement compileAlterDomain()
  {
    read();
    HsqlNameManager.HsqlName localHsqlName = this.session.getSchemaHsqlName(this.token.namePrefix);
    checkSchemaUpdateAuthorisation(localHsqlName);
    Type localType = this.database.schemaManager.getDomain(this.token.tokenString, localHsqlName.name, true);
    read();
    Object localObject1;
    Object localObject2;
    Object localObject3;
    Object localObject4;
    switch (this.token.tokenType)
    {
    case 652: 
      read();
      readThis(301);
      return compileRenameObject(localType.getName(), 13);
    case 93: 
      read();
      if (this.token.tokenType == 83)
      {
        read();
        localObject1 = getLastPart();
        localObject2 = new Object[] { Integer.valueOf(1132), localType };
        localObject3 = this.database.schemaManager.getCatalogAndBaseTableNames(localType.getName());
        return new StatementSchema((String)localObject1, 3, (Object[])localObject2, null, (HsqlNameManager.HsqlName[])localObject3);
      }
      if (this.token.tokenType == 51)
      {
        read();
        checkIsSchemaObjectName();
        localObject1 = this.database.schemaManager.getSchemaObjectName(localType.getSchemaName(), this.token.tokenString, 5, true);
        read();
        localObject2 = getLastPart();
        localObject3 = new Object[] { Integer.valueOf(1130), localType, localObject1 };
        localObject4 = this.database.schemaManager.getCatalogAndBaseTableNames(localType.getName());
        return new StatementSchema((String)localObject2, 3, (Object[])localObject3, null, (HsqlNameManager.HsqlName[])localObject4);
      }
      throw unexpectedToken();
    case 268: 
      read();
      readThis(83);
      localObject1 = readDefaultClause(localType);
      localObject2 = getLastPart();
      localObject3 = new Object[] { Integer.valueOf(1135), localType, localObject1 };
      localObject4 = this.database.schemaManager.getCatalogAndBaseTableNames(localType.getName());
      return new StatementSchema((String)localObject2, 3, (Object[])localObject3, null, (HsqlNameManager.HsqlName[])localObject4);
    case 355: 
      read();
      if ((this.token.tokenType == 51) || (this.token.tokenType == 40))
      {
        localObject1 = new HsqlArrayList();
        this.compileContext.currentDomain = localType;
        readConstraint(localType, (HsqlArrayList)localObject1);
        this.compileContext.currentDomain = null;
        localObject2 = (Constraint)((HsqlArrayList)localObject1).get(0);
        localObject3 = getLastPart();
        localObject4 = new Object[] { Integer.valueOf(1134), localType, localObject2 };
        HsqlNameManager.HsqlName[] arrayOfHsqlName = this.database.schemaManager.getCatalogAndBaseTableNames(localType.getName());
        return new StatementSchema((String)localObject3, 3, (Object[])localObject4, null, arrayOfHsqlName);
      }
      break;
    }
    throw unexpectedToken();
  }
  
  private boolean isGrantToken()
  {
    switch (this.token.tokenType)
    {
    case 2: 
    case 84: 
    case 108: 
    case 145: 
    case 236: 
    case 265: 
    case 319: 
    case 562: 
      return true;
    }
    return false;
  }
  
  StatementSchema compileGrantOrRevoke()
  {
    boolean bool = this.token.tokenType == 130;
    read();
    if ((isGrantToken()) || ((!bool) && ((this.token.tokenType == 130) || (this.token.tokenType == 433)))) {
      return compileRightGrantOrRevoke(bool);
    }
    return compileRoleGrantOrRevoke(bool);
  }
  
  private StatementSchema compileRightGrantOrRevoke(boolean paramBoolean)
  {
    OrderedHashSet localOrderedHashSet = new OrderedHashSet();
    Grantee localGrantee = null;
    Right localRight = null;
    HsqlNameManager.HsqlName localHsqlName = null;
    int i = 0;
    int j = 0;
    int k = 0;
    int m = 0;
    boolean bool1 = false;
    boolean bool2 = false;
    if (!paramBoolean) {
      if (this.token.tokenType == 130)
      {
        read();
        readThis(477);
        readThis(120);
        bool1 = true;
      }
      else if (this.token.tokenType == 433)
      {
        throw unsupportedFeature();
      }
    }
    if (this.token.tokenType == 2)
    {
      read();
      if (this.token.tokenType == 500) {
        read();
      }
      localRight = Right.fullRights;
      m = 1;
    }
    else
    {
      localRight = new Right();
      n = 1;
      while (n != 0)
      {
        checkIsUndelimitedIdentifier();
        int i1 = GranteeManager.getCheckSingleRight(this.token.tokenString);
        i2 = this.token.tokenType;
        localObject = null;
        read();
        switch (i2)
        {
        case 145: 
        case 236: 
        case 265: 
        case 319: 
          if (this.token.tokenType == 836) {
            localObject = readColumnNames(false);
          }
        case 307: 
          if (localRight == null) {
            localRight = new Right();
          }
          localRight.set(i1, (OrderedHashSet)localObject);
          i = 1;
          break;
        case 84: 
          if (localRight == null) {
            localRight = new Right();
          }
          localRight.set(i1, null);
          i = 1;
          break;
        case 562: 
          if (i != 0) {
            throw unexpectedToken();
          }
          localRight = Right.fullRights;
          j = 1;
          n = 0;
          break;
        case 108: 
          if (i != 0) {
            throw unexpectedToken();
          }
          localRight = Right.fullRights;
          k = 1;
          n = 0;
          break;
        default: 
          if (this.token.tokenType != 824) {
            break label405;
          }
          read();
        }
      }
    }
    label405:
    readThis(204);
    int n = 0;
    switch (this.token.tokenType)
    {
    case 597: 
      if ((k == 0) && (m == 0)) {
        throw unexpectedToken();
      }
      read();
      if ((!isSimpleName()) || (!isDelimitedIdentifier())) {
        throw Error.error(5569);
      }
      n = 16;
      break;
    case 273: 
      if ((k == 0) && (m == 0)) {
        throw unexpectedToken();
      }
      read();
      switch (this.token.tokenType)
      {
      case 126: 
      case 229: 
      case 513: 
        read();
        break;
      default: 
        throw unexpectedToken();
      }
      n = 24;
      break;
    case 126: 
      if ((k == 0) && (m == 0)) {
        throw unexpectedToken();
      }
      read();
      n = 16;
      break;
    case 229: 
      if ((k == 0) && (m == 0)) {
        throw unexpectedToken();
      }
      read();
      n = 17;
      break;
    case 513: 
      if ((k == 0) && (m == 0)) {
        throw unexpectedToken();
      }
      read();
      n = 18;
      break;
    case 557: 
      if ((j == 0) && (m == 0)) {
        throw unexpectedToken();
      }
      read();
      n = 12;
      break;
    case 414: 
      if ((j == 0) && (m == 0)) {
        throw unexpectedToken();
      }
      read();
      n = 13;
      break;
    case 527: 
      if ((j == 0) && (m == 0)) {
        throw unexpectedToken();
      }
      read();
      n = 7;
      break;
    case 38: 
      if ((j == 0) && (m == 0)) {
        throw unexpectedToken();
      }
      read();
      readThis(268);
      n = 14;
      break;
    case 294: 
    default: 
      if ((i == 0) && (m == 0)) {
        throw unexpectedToken();
      }
      readIfThis(294);
      n = 3;
    }
    localHsqlName = readNewSchemaObjectName(n, false);
    if (paramBoolean) {
      readThis(301);
    } else {
      readThis(124);
    }
    for (;;)
    {
      checkIsSimpleName();
      localOrderedHashSet.add(this.token.tokenString);
      read();
      if (this.token.tokenType != 824) {
        break;
      }
      read();
    }
    if (paramBoolean)
    {
      if (this.token.tokenType == 336)
      {
        read();
        readThis(130);
        readThis(477);
        bool1 = true;
      }
      if (this.token.tokenType == 432)
      {
        read();
        readThis(27);
        if (this.token.tokenType == 74)
        {
          read();
        }
        else
        {
          readThis(68);
          if (this.session.getRole() == null) {
            throw Error.error(2200);
          }
          localGrantee = this.session.getRole();
        }
      }
    }
    else if (this.token.tokenType == 368)
    {
      bool2 = true;
      read();
    }
    else
    {
      readThis(507);
    }
    String str = getLastPart();
    int i2 = paramBoolean ? 53 : 63;
    Object localObject = { localOrderedHashSet, localHsqlName, localRight, localGrantee, Boolean.valueOf(bool2), Boolean.valueOf(bool1) };
    HsqlNameManager.HsqlName[] arrayOfHsqlName = this.database.schemaManager.getCatalogNameArray();
    StatementSchema localStatementSchema = new StatementSchema(str, i2, (Object[])localObject, null, arrayOfHsqlName);
    return localStatementSchema;
  }
  
  private StatementSchema compileRoleGrantOrRevoke(boolean paramBoolean)
  {
    Grantee localGrantee = this.session.getGrantee();
    OrderedHashSet localOrderedHashSet1 = new OrderedHashSet();
    OrderedHashSet localOrderedHashSet2 = new OrderedHashSet();
    boolean bool = false;
    if ((!paramBoolean) && (this.token.tokenType == 356)) {
      throw unsupportedFeature();
    }
    for (;;)
    {
      checkIsSimpleName();
      localOrderedHashSet1.add(this.token.tokenString);
      read();
      if (this.token.tokenType != 824) {
        break;
      }
      read();
    }
    if (paramBoolean) {
      readThis(301);
    } else {
      readThis(124);
    }
    for (;;)
    {
      checkIsSimpleName();
      localOrderedHashSet2.add(this.token.tokenString);
      read();
      if (this.token.tokenType != 824) {
        break;
      }
      read();
    }
    if ((paramBoolean) && (this.token.tokenType == 336)) {
      throw unsupportedFeature();
    }
    if (this.token.tokenType == 432)
    {
      read();
      readThis(27);
      if (this.token.tokenType == 74)
      {
        read();
      }
      else
      {
        readThis(68);
        if (this.session.getRole() == null) {
          throw Error.error(2200);
        }
        localGrantee = this.session.getRole();
      }
    }
    if (!paramBoolean) {
      if (this.token.tokenType == 368)
      {
        bool = true;
        read();
      }
      else
      {
        readThis(507);
      }
    }
    String str = getLastPart();
    int i = paramBoolean ? 54 : 64;
    Object[] arrayOfObject = { localOrderedHashSet2, localOrderedHashSet1, localGrantee, Boolean.valueOf(bool) };
    HsqlNameManager.HsqlName[] arrayOfHsqlName = this.database.schemaManager.getCatalogNameArray();
    StatementSchema localStatementSchema = new StatementSchema(str, i, arrayOfObject, null, arrayOfHsqlName);
    return localStatementSchema;
  }
  
  void checkDatabaseUpdateAuthorisation()
  {
    this.session.checkAdmin();
    this.session.checkDDLWrite();
  }
  
  void checkSchemaUpdateAuthorisation(Session paramSession, HsqlNameManager.HsqlName paramHsqlName)
  {
    if (paramSession.isProcessingLog()) {
      return;
    }
    if (SqlInvariants.isSystemSchemaName(paramHsqlName.name)) {
      throw Error.error(5503);
    }
    if (paramSession.parser.isSchemaDefinition)
    {
      if (paramHsqlName == paramSession.getCurrentSchemaHsqlName()) {
        return;
      }
      throw Error.error(5505, paramHsqlName.name);
    }
    paramSession.getGrantee().checkSchemaUpdateOrGrantRights(paramHsqlName.name);
    paramSession.checkDDLWrite();
  }
  
  StatementSchema compileComment()
  {
    readThis(599);
    readThis(204);
    HsqlNameManager.HsqlName localHsqlName;
    switch (this.token.tokenType)
    {
    case 294: 
    case 513: 
      int i = this.token.tokenType == 513 ? 18 : 3;
      read();
      checkIsSchemaObjectName();
      localHsqlName = this.database.nameManager.newHsqlName(this.token.tokenString, this.token.isDelimitedIdentifier, i);
      if (this.token.namePrefix == null) {
        localHsqlName.schema = this.session.getCurrentSchemaHsqlName();
      } else {
        localHsqlName.schema = this.database.nameManager.newHsqlName(this.token.namePrefix, this.token.isDelimitedPrefix, 2);
      }
      read();
      break;
    case 46: 
      read();
      checkIsSchemaObjectName();
      localHsqlName = this.database.nameManager.newHsqlName(this.token.tokenString, this.token.isDelimitedIdentifier, 9);
      if (this.token.namePrefix == null) {
        throw Error.error(5501);
      }
      localHsqlName.parent = this.database.nameManager.newHsqlName(this.token.namePrefix, this.token.isDelimitedPrefix, 3);
      if (this.token.namePrePrefix == null) {
        localHsqlName.parent.schema = this.session.getCurrentSchemaHsqlName();
      } else {
        localHsqlName.parent.schema = this.database.nameManager.newHsqlName(this.token.namePrePrefix, this.token.isDelimitedPrePrefix, 3);
      }
      read();
      break;
    default: 
      throw unexpectedToken();
    }
    readThis(152);
    String str = readQuotedString();
    Object[] arrayOfObject = { localHsqlName, str };
    return new StatementSchema(null, 1123, arrayOfObject, null, null);
  }
  
  Statement compileAlterSession()
  {
    read();
    this.session.checkAdmin();
    if (this.token.tokenType == 653)
    {
      read();
      int i = this.token.tokenType;
      switch (this.token.tokenType)
      {
      case 2: 
        read();
        break;
      case 250: 
        read();
        readThis(532);
        break;
      case 294: 
        read();
        readThis(399);
        break;
      default: 
        throw unexpectedTokenRequire("ALL,RESULT,TABLE");
      }
      Object[] arrayOfObject1 = { Long.valueOf(this.session.getId()), Integer.valueOf(i) };
      return new StatementCommand(1005, arrayOfObject1);
    }
    long l = readBigint();
    Session localSession = this.database.sessionManager.getSession(l);
    if (localSession == null) {
      throw Error.error(4500);
    }
    int j = this.token.tokenType;
    switch (this.token.tokenType)
    {
    case 42: 
      read();
      break;
    case 247: 
      read();
      break;
    case 99: 
      read();
      readThis(539);
      break;
    default: 
      throw unexpectedToken();
    }
    Object[] arrayOfObject2 = { Long.valueOf(l), Integer.valueOf(j) };
    return new StatementCommand(1005, arrayOfObject2);
  }
  
  boolean processTrueOrFalse()
  {
    if (this.token.namePrefix != null) {
      throw unexpectedToken();
    }
    if (this.token.tokenType == 310)
    {
      read();
      return true;
    }
    if (this.token.tokenType == 114)
    {
      read();
      return false;
    }
    throw unexpectedToken();
  }
  
  Boolean processTrueOrFalseObject()
  {
    return Boolean.valueOf(processTrueOrFalse());
  }
}


/* Location:              E:\java\java学习\hutubill\lib\all.jar!\org\hsqldb\ParserDDL.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */
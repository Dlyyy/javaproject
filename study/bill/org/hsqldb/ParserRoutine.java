package org.hsqldb;

import org.hsqldb.error.Error;
import org.hsqldb.lib.ArrayUtil;
import org.hsqldb.lib.HsqlArrayList;
import org.hsqldb.lib.HsqlList;
import org.hsqldb.lib.LongDeque;
import org.hsqldb.lib.OrderedHashSet;
import org.hsqldb.lib.OrderedIntHashSet;
import org.hsqldb.rights.Grantee;
import org.hsqldb.types.ArrayType;
import org.hsqldb.types.RowType;
import org.hsqldb.types.Type;

public class ParserRoutine
  extends ParserTable
{
  static String[] featureStrings = { "H901_03" };
  
  ParserRoutine(Session paramSession, Scanner paramScanner)
  {
    super(paramSession, paramScanner);
  }
  
  Statement compileOpenCursorStatement(StatementCompound paramStatementCompound)
  {
    readThis(206);
    checkIsSimpleName();
    String str = this.token.tokenString;
    read();
    for (int i = 0; i < paramStatementCompound.cursors.length; i++) {
      if (paramStatementCompound.cursors[i].getCursorName().name.equals(str)) {
        return paramStatementCompound.cursors[i];
      }
    }
    throw Error.parseError(4680, null, this.scanner.getLineNumber());
  }
  
  Statement compileSelectSingleRowStatement(RangeGroup[] paramArrayOfRangeGroup)
  {
    OrderedHashSet localOrderedHashSet = new OrderedHashSet();
    LongDeque localLongDeque = new LongDeque();
    this.compileContext.setOuterRanges(paramArrayOfRangeGroup);
    QuerySpecification localQuerySpecification = XreadSelect();
    readThis(151);
    RangeVariable[] arrayOfRangeVariable = paramArrayOfRangeGroup[0].getRangeVariables();
    readTargetSpecificationList(localOrderedHashSet, arrayOfRangeVariable, localLongDeque);
    XreadTableExpression(localQuerySpecification);
    localQuerySpecification.setReturningResult();
    int[] arrayOfInt = new int[localLongDeque.size()];
    localLongDeque.toArray(arrayOfInt);
    Expression[] arrayOfExpression = new Expression[localOrderedHashSet.size()];
    localOrderedHashSet.toArray(arrayOfExpression);
    Type[] arrayOfType = new Type[arrayOfExpression.length];
    for (int i = 0; i < arrayOfExpression.length; i++)
    {
      if (arrayOfExpression[i].getColumn().getParameterMode() == 1) {
        throw Error.parseError(2500, null, this.scanner.getLineNumber());
      }
      arrayOfType[i] = arrayOfExpression[i].getDataType();
    }
    localQuerySpecification.setReturningResult();
    localQuerySpecification.resolve(this.session, paramArrayOfRangeGroup, arrayOfType);
    if (localQuerySpecification.getColumnCount() != arrayOfExpression.length) {
      throw Error.error(5564, "INTO");
    }
    StatementSet localStatementSet = new StatementSet(this.session, arrayOfExpression, localQuerySpecification, arrayOfInt, this.compileContext);
    return localStatementSet;
  }
  
  Statement compileGetStatement(RangeGroup[] paramArrayOfRangeGroup)
  {
    read();
    readThis(412);
    OrderedHashSet localOrderedHashSet = new OrderedHashSet();
    HsqlArrayList localHsqlArrayList = new HsqlArrayList();
    LongDeque localLongDeque = new LongDeque();
    RangeVariable[] arrayOfRangeVariable = paramArrayOfRangeGroup[0].getRangeVariables();
    readGetClauseList(arrayOfRangeVariable, localOrderedHashSet, localLongDeque, localHsqlArrayList);
    if (localHsqlArrayList.size() > 1) {
      throw Error.parseError(5602, null, this.scanner.getLineNumber());
    }
    Expression localExpression = (Expression)localHsqlArrayList.get(0);
    if (localExpression.getDegree() != localOrderedHashSet.size()) {
      throw Error.error(5546, "SET");
    }
    int[] arrayOfInt = new int[localLongDeque.size()];
    localLongDeque.toArray(arrayOfInt);
    Expression[] arrayOfExpression = new Expression[localOrderedHashSet.size()];
    localOrderedHashSet.toArray(arrayOfExpression);
    for (int i = 0; i < arrayOfExpression.length; i++) {
      resolveOuterReferencesAndTypes(paramArrayOfRangeGroup, arrayOfExpression[i]);
    }
    resolveOuterReferencesAndTypes(paramArrayOfRangeGroup, localExpression);
    for (i = 0; i < arrayOfExpression.length; i++)
    {
      if (arrayOfExpression[i].getColumn().getParameterMode() == 1) {
        throw Error.parseError(2500, null, this.scanner.getLineNumber());
      }
      if (!arrayOfExpression[i].getDataType().canBeAssignedFrom(localExpression.getNodeDataType(i))) {
        throw Error.parseError(5561, null, this.scanner.getLineNumber());
      }
    }
    StatementSet localStatementSet = new StatementSet(this.session, arrayOfExpression, localExpression, arrayOfInt, this.compileContext);
    return localStatementSet;
  }
  
  StatementSet compileSetStatement(RangeGroup[] paramArrayOfRangeGroup, RangeVariable[] paramArrayOfRangeVariable)
  {
    OrderedHashSet localOrderedHashSet = new OrderedHashSet();
    HsqlArrayList localHsqlArrayList = new HsqlArrayList();
    LongDeque localLongDeque = new LongDeque();
    readSetClauseList(paramArrayOfRangeVariable, localOrderedHashSet, localLongDeque, localHsqlArrayList);
    if (localHsqlArrayList.size() > 1) {
      throw Error.parseError(5602, null, this.scanner.getLineNumber());
    }
    Expression localExpression = (Expression)localHsqlArrayList.get(0);
    if (localExpression.getDegree() != localOrderedHashSet.size()) {
      throw Error.error(5546, "SET");
    }
    int[] arrayOfInt = new int[localLongDeque.size()];
    localLongDeque.toArray(arrayOfInt);
    Expression[] arrayOfExpression = new Expression[localOrderedHashSet.size()];
    localOrderedHashSet.toArray(arrayOfExpression);
    for (int i = 0; i < arrayOfExpression.length; i++) {
      resolveOuterReferencesAndTypes(paramArrayOfRangeGroup, arrayOfExpression[i]);
    }
    resolveOuterReferencesAndTypes(paramArrayOfRangeGroup, localExpression);
    for (i = 0; i < arrayOfExpression.length; i++)
    {
      ColumnSchema localColumnSchema = arrayOfExpression[i].getColumn();
      if (localColumnSchema.getParameterMode() == 1) {
        throw Error.error(2500, localColumnSchema.getName().statementName);
      }
      if (!arrayOfExpression[i].getDataType().canBeAssignedFrom(localExpression.getNodeDataType(i))) {
        throw Error.parseError(5561, null, this.scanner.getLineNumber());
      }
    }
    StatementSet localStatementSet = new StatementSet(this.session, arrayOfExpression, localExpression, arrayOfInt, this.compileContext);
    return localStatementSet;
  }
  
  StatementDMQL compileTriggerSetStatement(Table paramTable, RangeGroup[] paramArrayOfRangeGroup)
  {
    OrderedHashSet localOrderedHashSet = new OrderedHashSet();
    HsqlArrayList localHsqlArrayList = new HsqlArrayList();
    RangeVariable[] arrayOfRangeVariable = { paramArrayOfRangeGroup[0].getRangeVariables()[1] };
    LongDeque localLongDeque = new LongDeque();
    readSetClauseList(arrayOfRangeVariable, localOrderedHashSet, localLongDeque, localHsqlArrayList);
    int[] arrayOfInt = new int[localLongDeque.size()];
    localLongDeque.toArray(arrayOfInt);
    Expression[] arrayOfExpression2 = new Expression[localOrderedHashSet.size()];
    localOrderedHashSet.toArray(arrayOfExpression2);
    for (int i = 0; i < arrayOfExpression2.length; i++) {
      resolveOuterReferencesAndTypes(RangeGroup.emptyArray, arrayOfExpression2[i]);
    }
    Expression[] arrayOfExpression1 = new Expression[localHsqlArrayList.size()];
    localHsqlArrayList.toArray(arrayOfExpression1);
    resolveUpdateExpressions(paramTable, RangeGroup.emptyGroup, arrayOfInt, arrayOfExpression1, paramArrayOfRangeGroup);
    StatementSet localStatementSet = new StatementSet(this.session, arrayOfExpression2, paramTable, paramArrayOfRangeGroup[0].getRangeVariables(), arrayOfInt, arrayOfExpression1, this.compileContext);
    return localStatementSet;
  }
  
  StatementSchema compileAlterSpecificRoutine()
  {
    boolean bool = false;
    readThis(273);
    readThis(513);
    Routine localRoutine = (Routine)readSchemaObjectName(24);
    localRoutine = localRoutine.duplicate();
    readRoutineCharacteristics(localRoutine);
    bool = readIfThis(507);
    if (bool)
    {
      localObject = this.database.schemaManager.getReferencesTo(localRoutine.getSpecificName());
      if (!((OrderedHashSet)localObject).isEmpty()) {
        throw Error.parseError(5502, null, this.scanner.getLineNumber());
      }
    }
    if (this.token.tokenType == 590) {
      read();
    } else if (this.token.tokenType == 467) {
      read();
    }
    readRoutineBody(localRoutine);
    localRoutine.resetAlteredRoutineSettings();
    localRoutine.resolve(this.session);
    Object localObject = { localRoutine };
    String str = getLastPart();
    StatementSchema localStatementSchema = new StatementSchema(str, 4, (Object[])localObject, null, this.database.schemaManager.getCatalogNameArray());
    return localStatementSchema;
  }
  
  StatementSchema compileCreateProcedureOrFunction(boolean paramBoolean)
  {
    Routine localRoutine = readCreateProcedureOrFunction();
    Object[] arrayOfObject = { localRoutine };
    String str = getLastPart();
    StatementSchema localStatementSchema = new StatementSchema(str, 69, arrayOfObject, null, this.database.schemaManager.getCatalogNameArray());
    return localStatementSchema;
  }
  
  Routine readCreateProcedureOrFunction()
  {
    Routine localRoutine = readProcedureOrFunctionDeclaration();
    readRoutineBody(localRoutine);
    return localRoutine;
  }
  
  Routine readProcedureOrFunctionDeclaration()
  {
    boolean bool = false;
    if (this.token.tokenType == 582)
    {
      bool = true;
      read();
      if (this.token.tokenType == 229) {
        throw unexpectedToken();
      }
    }
    int i = this.token.tokenType == 229 ? 17 : 16;
    read();
    HsqlNameManager.HsqlName localHsqlName = readNewSchemaObjectName(i, true);
    localHsqlName.setSchemaIfNull(this.session.getCurrentSchemaHsqlName());
    Routine localRoutine = new Routine(i);
    localRoutine.setName(localHsqlName);
    localRoutine.setAggregate(bool);
    readRoutineArguments(localRoutine);
    if (i != 17)
    {
      readThis(252);
      Object localObject;
      if (this.token.tokenType == 294)
      {
        read();
        localObject = new TableDerived(this.database, SqlInvariants.MODULE_HSQLNAME, 11);
        readTableDefinition(localRoutine, (Table)localObject);
        localRoutine.setReturnTable((TableDerived)localObject);
      }
      else
      {
        localObject = readTypeDefinition(false, true);
        localRoutine.setReturnType((Type)localObject);
      }
    }
    readRoutineCharacteristics(localRoutine);
    return localRoutine;
  }
  
  void readRoutineArguments(Routine paramRoutine)
  {
    readThis(836);
    if (this.token.tokenType == 822) {
      read();
    } else {
      for (;;)
      {
        ColumnSchema localColumnSchema = readRoutineParameter(paramRoutine, true);
        paramRoutine.addParameter(localColumnSchema);
        if (this.token.tokenType == 824)
        {
          read();
        }
        else
        {
          readThis(822);
          break;
        }
      }
    }
  }
  
  Routine readCreatePasswordCheckFunction()
  {
    Routine localRoutine = new Routine(16);
    if (this.token.tokenType == 191)
    {
      read();
      return null;
    }
    if (this.token.tokenType == 112)
    {
      localRoutine.setLanguage(1);
      localRoutine.setDataImpact(1);
    }
    else
    {
      localRoutine.setLanguage(2);
      localRoutine.setDataImpact(2);
    }
    HsqlNameManager.HsqlName localHsqlName = this.database.nameManager.newHsqlName("PASSWORD", false, 16);
    localHsqlName.setSchemaIfNull(SqlInvariants.SYSTEM_SCHEMA_HSQLNAME);
    localRoutine.setName(localHsqlName);
    localHsqlName = this.database.nameManager.newHsqlName("PASSWORD", false, 23);
    ColumnSchema localColumnSchema = new ColumnSchema(localHsqlName, Type.SQL_VARCHAR, false, false, null);
    localRoutine.addParameter(localColumnSchema);
    localRoutine.setReturnType(Type.SQL_BOOLEAN);
    readRoutineBody(localRoutine);
    localRoutine.resolve(this.session);
    return localRoutine;
  }
  
  Routine readCreateDatabaseAuthenticationFunction()
  {
    Routine localRoutine = new Routine(16);
    if (this.token.tokenType == 191)
    {
      read();
      return null;
    }
    checkIsThis(112);
    localRoutine.setLanguage(1);
    localRoutine.setDataImpact(1);
    localRoutine.setName(this.database.nameManager.newHsqlName("AUTHENTICATION", false, 16));
    for (int i = 0; i < 3; i++)
    {
      ColumnSchema localColumnSchema = new ColumnSchema(null, Type.SQL_VARCHAR, false, false, null);
      localRoutine.addParameter(localColumnSchema);
    }
    localRoutine.setReturnType(new ArrayType(Type.SQL_VARCHAR_DEFAULT, 1024));
    readRoutineBody(localRoutine);
    localRoutine.resolve(this.session);
    return localRoutine;
  }
  
  private void readTableDefinition(Routine paramRoutine, Table paramTable)
  {
    readThis(836);
    for (int i = 0;; i++)
    {
      ColumnSchema localColumnSchema = readRoutineParameter(paramRoutine, false);
      if (localColumnSchema.getName() == null) {
        throw unexpectedToken();
      }
      paramTable.addColumn(localColumnSchema);
      if (this.token.tokenType == 824)
      {
        read();
      }
      else
      {
        readThis(822);
        break;
      }
    }
    paramTable.createPrimaryKey();
  }
  
  private void readRoutineCharacteristics(Routine paramRoutine)
  {
    OrderedIntHashSet localOrderedIntHashSet = new OrderedIntHashSet();
    int i = 0;
    while (i == 0) {
      switch (this.token.tokenType)
      {
      case 156: 
        if (!localOrderedIntHashSet.add(156)) {
          throw unexpectedToken();
        }
        read();
        if (this.token.tokenType == 447)
        {
          read();
          paramRoutine.setLanguage(1);
        }
        else if (this.token.tokenType == 275)
        {
          read();
          paramRoutine.setLanguage(2);
        }
        else
        {
          throw unexpectedToken();
        }
        break;
      case 214: 
        if (!localOrderedIntHashSet.add(214)) {
          throw unexpectedToken();
        }
        read();
        readThis(541);
        if (this.token.tokenType == 447)
        {
          read();
          paramRoutine.setParameterStyle(1);
        }
        else
        {
          readThis(275);
          paramRoutine.setParameterStyle(2);
        }
        break;
      case 273: 
        if (!localOrderedIntHashSet.add(273)) {
          throw unexpectedToken();
        }
        read();
        HsqlNameManager.HsqlName localHsqlName = readNewSchemaObjectName(24, false);
        paramRoutine.setSpecificName(localHsqlName);
        break;
      case 88: 
        if (!localOrderedIntHashSet.add(88)) {
          throw unexpectedToken();
        }
        read();
        paramRoutine.setDeterministic(true);
        break;
      case 193: 
        if (!localOrderedIntHashSet.add(88)) {
          throw unexpectedToken();
        }
        read();
        readThis(88);
        paramRoutine.setDeterministic(false);
        break;
      case 181: 
        if (!localOrderedIntHashSet.add(275)) {
          throw unexpectedToken();
        }
        if (paramRoutine.getType() == 16) {
          throw unexpectedToken();
        }
        read();
        readThis(275);
        readThis(399);
        paramRoutine.setDataImpact(4);
        break;
      case 190: 
        if (!localOrderedIntHashSet.add(275)) {
          throw unexpectedToken();
        }
        read();
        readThis(275);
        paramRoutine.setDataImpact(1);
        break;
      case 232: 
        if (!localOrderedIntHashSet.add(275)) {
          throw unexpectedToken();
        }
        read();
        readThis(275);
        readThis(399);
        paramRoutine.setDataImpact(3);
        break;
      case 52: 
        if (!localOrderedIntHashSet.add(275)) {
          throw unexpectedToken();
        }
        read();
        readThis(275);
        paramRoutine.setDataImpact(2);
        break;
      case 252: 
        if ((!localOrderedIntHashSet.add(196)) || (paramRoutine.isProcedure())) {
          throw unexpectedToken();
        }
        if (paramRoutine.isAggregate()) {
          throw Error.error(5604, this.token.tokenString);
        }
        read();
        readThis(196);
        readThis(204);
        readThis(196);
        readThis(441);
        paramRoutine.setNullInputOutput(true);
        break;
      case 29: 
        if ((!localOrderedIntHashSet.add(196)) || (paramRoutine.isProcedure())) {
          throw unexpectedToken();
        }
        read();
        readThis(204);
        readThis(196);
        readThis(441);
        paramRoutine.setNullInputOutput(false);
        break;
      case 94: 
        if ((!localOrderedIntHashSet.add(250)) || (paramRoutine.isFunction())) {
          throw unexpectedToken();
        }
        read();
        readThis(250);
        readThis(532);
        int j = readInteger();
        if ((j < 0) || (j > 16)) {
          throw Error.error(5604, String.valueOf(j));
        }
        paramRoutine.setMaxDynamicResults(j);
        break;
      case 189: 
        if ((paramRoutine.getType() == 16) || (!localOrderedIntHashSet.add(260))) {
          throw unexpectedToken();
        }
        read();
        readThis(260);
        readThis(454);
        paramRoutine.setNewSavepointLevel(true);
        break;
      case 203: 
        if ((paramRoutine.getType() == 16) || (!localOrderedIntHashSet.add(260))) {
          throw unexpectedToken();
        }
        read();
        readThis(260);
        readThis(454);
        paramRoutine.setNewSavepointLevel(false);
        throw unsupportedFeature("OLD");
      default: 
        i = 1;
      }
    }
  }
  
  void readRoutineBody(Routine paramRoutine)
  {
    if (this.token.tokenType == 112) {
      readRoutineJavaBody(paramRoutine);
    } else {
      readRoutineSQLBody(paramRoutine);
    }
  }
  
  void readRoutineSQLBody(Routine paramRoutine)
  {
    startRecording();
    this.session.sessionContext.pushRoutineTables();
    try
    {
      Statement localStatement = compileSQLProcedureStatementOrNull(paramRoutine, null);
      if (localStatement == null) {
        throw unexpectedToken();
      }
      Token[] arrayOfToken = getRecordedStatement();
      String str = Token.getSQL(arrayOfToken);
      localStatement.setSQL(str);
      paramRoutine.setProcedure(localStatement);
    }
    finally
    {
      this.session.sessionContext.popRoutineTables();
    }
  }
  
  void readRoutineJavaBody(Routine paramRoutine)
  {
    if (paramRoutine.getLanguage() != 1) {
      throw unexpectedToken();
    }
    read();
    readThis(467);
    checkIsQuotedString();
    paramRoutine.setMethodURL((String)this.token.tokenValue);
    read();
    if (this.token.tokenType == 214)
    {
      read();
      readThis(541);
      readThis(447);
    }
  }
  
  Object[] readLocalDeclarationList(Routine paramRoutine, StatementCompound paramStatementCompound)
  {
    HsqlArrayList localHsqlArrayList = new HsqlArrayList();
    int i = 0;
    RangeGroup[] arrayOfRangeGroup = new RangeGroup[1];
    arrayOfRangeGroup[0] = (paramStatementCompound == null ? paramRoutine : paramStatementCompound);
    this.compileContext.setOuterRanges(arrayOfRangeGroup);
    while (this.token.tokenType == 82)
    {
      localObject = null;
      if (i == 0)
      {
        localObject = readLocalTableVariableDeclarationOrNull(paramRoutine);
        if (localObject == null)
        {
          i = 1;
        }
        else
        {
          localHsqlArrayList.add(localObject);
          readThis(841);
        }
      }
      else if (i == 1)
      {
        localObject = readLocalVariableDeclarationOrNull();
        if (localObject == null) {
          i = 2;
        } else {
          localHsqlArrayList.addAll((Object[])localObject);
        }
      }
      else if (i == 2)
      {
        localObject = compileDeclareCursorOrNull(arrayOfRangeGroup, true);
        if (localObject == null)
        {
          i = 3;
        }
        else
        {
          localHsqlArrayList.add(localObject);
          readThis(841);
        }
      }
      else if (i == 3)
      {
        localObject = compileLocalHandlerDeclaration(paramRoutine, paramStatementCompound);
        localHsqlArrayList.add(localObject);
      }
    }
    Object localObject = new Object[localHsqlArrayList.size()];
    localHsqlArrayList.toArray(localObject);
    return (Object[])localObject;
  }
  
  Table readLocalTableVariableDeclarationOrNull(Routine paramRoutine)
  {
    int i = getPosition();
    readThis(82);
    if (this.token.tokenType == 294)
    {
      read();
      HsqlNameManager.HsqlName localHsqlName = readNewSchemaObjectName(3, false);
      localHsqlName.schema = SqlInvariants.MODULE_HSQLNAME;
      Table localTable = new Table(this.database, localHsqlName, 3);
      localTable.persistenceScope = 20;
      readTableDefinition(paramRoutine, localTable);
      this.session.sessionContext.addSessionTable(localTable);
      return localTable;
    }
    rewind(i);
    return null;
  }
  
  ColumnSchema[] readLocalVariableDeclarationOrNull()
  {
    int i = getPosition();
    HsqlArrayList localHsqlArrayList = new HsqlArrayList();
    Type localType;
    try
    {
      readThis(82);
      if (isReservedKey())
      {
        rewind(i);
        return null;
      }
      for (;;)
      {
        HsqlNameManager.HsqlName localHsqlName = readNewSchemaObjectName(22, false);
        if (this.token.tokenType == 49)
        {
          rewind(i);
          return null;
        }
        localHsqlArrayList.add(localHsqlName);
        if (this.token.tokenType != 824) {
          break;
        }
        read();
      }
      localType = readTypeDefinition(false, true);
    }
    catch (HsqlException localHsqlException)
    {
      rewind(i);
      return null;
    }
    Expression localExpression = null;
    if (this.token.tokenType == 83)
    {
      read();
      localExpression = readDefaultClause(localType);
    }
    ColumnSchema[] arrayOfColumnSchema = new ColumnSchema[localHsqlArrayList.size()];
    for (int j = 0; j < localHsqlArrayList.size(); j++)
    {
      arrayOfColumnSchema[j] = new ColumnSchema((HsqlNameManager.HsqlName)localHsqlArrayList.get(j), localType, true, false, localExpression);
      arrayOfColumnSchema[j].setParameterMode((byte)2);
    }
    readThis(841);
    return arrayOfColumnSchema;
  }
  
  private StatementHandler compileLocalHandlerDeclaration(Routine paramRoutine, StatementCompound paramStatementCompound)
  {
    readThis(82);
    int i;
    switch (this.token.tokenType)
    {
    case 397: 
      read();
      i = 5;
      break;
    case 110: 
      read();
      i = 6;
      break;
    case 313: 
      read();
      i = 7;
      break;
    default: 
      throw unexpectedToken();
    }
    readThis(134);
    readThis(120);
    StatementHandler localStatementHandler = new StatementHandler(i);
    int j = 0;
    int k = 1;
    label301:
    while (j == 0)
    {
      int m = 0;
      switch (this.token.tokenType)
      {
      case 824: 
        if (k != 0) {
          throw unexpectedToken();
        }
        read();
        k = 1;
        break;
      case 277: 
        m = 4;
      case 276: 
        if (m == 0) {
          m = 1;
        }
      case 278: 
        if (m == 0) {
          m = 2;
        }
      case 193: 
        if (m == 0) {
          m = 3;
        }
        if (k == 0) {
          throw unexpectedToken();
        }
        k = 0;
        read();
        if (m == 3)
        {
          readThis(426);
        }
        else if (m == 4)
        {
          String str = parseSQLStateValue();
          localStatementHandler.addConditionState(str);
          break label301;
        }
        localStatementHandler.addConditionType(m);
        break;
      }
      if (k != 0) {
        throw unexpectedToken();
      }
      j = 1;
    }
    if (this.token.tokenType == 841)
    {
      read();
    }
    else
    {
      Statement localStatement = compileSQLProcedureStatementOrNull(paramRoutine, paramStatementCompound);
      if (localStatement == null) {
        throw unexpectedToken();
      }
      readThis(841);
      localStatementHandler.addStatement(localStatement);
    }
    return localStatementHandler;
  }
  
  String parseSQLStateValue()
  {
    readIfThis(323);
    checkIsQuotedString();
    String str = this.token.tokenString;
    if (str.length() != 5) {
      throw Error.parseError(5607, null, this.scanner.getLineNumber());
    }
    read();
    return str;
  }
  
  String parseSQLFeatureValue()
  {
    if (!isUndelimitedSimpleName()) {
      throw Error.parseError(5555, this.token.tokenString, this.scanner.getLineNumber());
    }
    String str = this.token.tokenString;
    int i = ArrayUtil.find(featureStrings, str);
    if (i < 0) {
      throw Error.parseError(5555, this.token.tokenString, this.scanner.getLineNumber());
    }
    read();
    return str;
  }
  
  Statement compileCompoundStatement(Routine paramRoutine, StatementCompound paramStatementCompound, HsqlNameManager.HsqlName paramHsqlName)
  {
    readThis(18);
    readThis(15);
    paramHsqlName = createLabelIfNull(paramStatementCompound, paramHsqlName);
    StatementCompound localStatementCompound = new StatementCompound(99, paramHsqlName, paramStatementCompound);
    localStatementCompound.setAtomic(true);
    localStatementCompound.setRoot(paramRoutine);
    Object[] arrayOfObject = readLocalDeclarationList(paramRoutine, paramStatementCompound);
    localStatementCompound.setLocalDeclarations(arrayOfObject);
    Statement[] arrayOfStatement = compileSQLProcedureStatementList(paramRoutine, localStatementCompound);
    localStatementCompound.setStatements(arrayOfStatement);
    readThis(99);
    if ((isSimpleName()) && (!isReservedKey()))
    {
      if (paramHsqlName == null) {
        throw unexpectedToken();
      }
      if (!paramHsqlName.name.equals(this.token.tokenString)) {
        throw Error.error(5508, this.token.tokenString);
      }
      read();
    }
    return localStatementCompound;
  }
  
  HsqlNameManager.HsqlName createLabelIfNull(StatementCompound paramStatementCompound, HsqlNameManager.HsqlName paramHsqlName)
  {
    if (paramHsqlName != null) {
      return paramHsqlName;
    }
    StatementCompound localStatementCompound = paramStatementCompound;
    int i = 0;
    while (localStatementCompound != null)
    {
      i++;
      localStatementCompound = localStatementCompound.parent;
    }
    String str = "_" + i;
    paramHsqlName = this.session.database.nameManager.newHsqlName(str, false, 21);
    return paramHsqlName;
  }
  
  Statement[] compileSQLProcedureStatementList(Routine paramRoutine, StatementCompound paramStatementCompound)
  {
    HsqlArrayList localHsqlArrayList = new HsqlArrayList();
    for (;;)
    {
      Statement localStatement = compileSQLProcedureStatementOrNull(paramRoutine, paramStatementCompound);
      if (localStatement == null) {
        break;
      }
      readThis(841);
      localHsqlArrayList.add(localStatement);
    }
    if (localHsqlArrayList.size() == 0) {
      throw unexpectedToken();
    }
    Statement[] arrayOfStatement = new Statement[localHsqlArrayList.size()];
    localHsqlArrayList.toArray(arrayOfStatement);
    return arrayOfStatement;
  }
  
  Statement compileSQLProcedureStatementOrNull(Routine paramRoutine, StatementCompound paramStatementCompound)
  {
    Object localObject1 = null;
    HsqlNameManager.HsqlName localHsqlName1 = null;
    StatementCompound localStatementCompound = paramStatementCompound == null ? paramRoutine : paramStatementCompound;
    RangeGroup[] arrayOfRangeGroup = { localStatementCompound };
    if ((!paramRoutine.isTrigger()) && (isSimpleName()) && (!isReservedKey())) {
      localHsqlName1 = readLabel();
    }
    this.compileContext.reset();
    HsqlNameManager.HsqlName localHsqlName2 = this.session.getCurrentSchemaHsqlName();
    this.session.setCurrentSchemaHsqlName(paramRoutine.getSchemaName());
    try
    {
      switch (this.token.tokenType)
      {
      case 206: 
        if (paramRoutine.dataImpact == 2) {
          throw Error.error(5602, paramRoutine.getDataImpactString());
        }
        if (localHsqlName1 != null) {
          throw unexpectedToken();
        }
        localObject1 = compileOpenCursorStatement(paramStatementCompound);
        break;
      case 265: 
        if (localHsqlName1 != null) {
          throw unexpectedToken();
        }
        localObject1 = compileSelectSingleRowStatement(arrayOfRangeGroup);
        break;
      case 145: 
        if (localHsqlName1 != null) {
          throw unexpectedToken();
        }
        localObject1 = compileInsertStatement(arrayOfRangeGroup);
        break;
      case 319: 
        if (localHsqlName1 != null) {
          throw unexpectedToken();
        }
        localObject1 = compileUpdateStatement(arrayOfRangeGroup);
        break;
      case 84: 
        if (localHsqlName1 != null) {
          throw unexpectedToken();
        }
        localObject1 = compileDeleteStatement(arrayOfRangeGroup);
        break;
      case 311: 
        if (localHsqlName1 != null) {
          throw unexpectedToken();
        }
        localObject1 = compileTruncateStatement();
        break;
      case 176: 
        if (localHsqlName1 != null) {
          throw unexpectedToken();
        }
        localObject1 = compileMergeStatement(arrayOfRangeGroup);
        break;
      case 268: 
        if (localHsqlName1 != null) {
          throw unexpectedToken();
        }
        read();
        if (paramRoutine.isTrigger())
        {
          if ((paramRoutine.triggerType == 4) && (paramRoutine.triggerOperation != 19))
          {
            int i = getPosition();
            try
            {
              localObject1 = compileTriggerSetStatement(paramRoutine.triggerTable, arrayOfRangeGroup);
            }
            catch (HsqlException localHsqlException)
            {
              rewind(i);
              localObject1 = compileSetStatement(arrayOfRangeGroup, localStatementCompound.getRangeVariables());
              break label529;
            }
          }
          else
          {
            localObject1 = compileSetStatement(arrayOfRangeGroup, localStatementCompound.getRangeVariables());
          }
          ((StatementSet)localObject1).checkIsNotColumnTarget();
        }
        else
        {
          localObject1 = compileSetStatement(arrayOfRangeGroup, localStatementCompound.getRangeVariables());
        }
        break;
      case 128: 
        if (localHsqlName1 != null) {
          throw unexpectedToken();
        }
        localObject1 = compileGetStatement(arrayOfRangeGroup);
        break;
      case 28: 
        if (localHsqlName1 != null) {
          throw unexpectedToken();
        }
        localObject1 = compileCallStatement(arrayOfRangeGroup, true);
        localObject2 = ((StatementProcedure)localObject1).procedure;
        if (localObject2 != null) {
          switch (paramRoutine.dataImpact)
          {
          case 2: 
            if ((((Routine)localObject2).dataImpact == 3) || (((Routine)localObject2).dataImpact == 4)) {
              throw Error.error(5602, paramRoutine.getDataImpactString());
            }
            break;
          case 3: 
            if (((Routine)localObject2).dataImpact == 4) {
              throw Error.error(5602, paramRoutine.getDataImpactString());
            }
            break;
          }
        }
        break;
      case 251: 
        if ((paramRoutine.isTrigger()) || (localHsqlName1 != null)) {
          throw unexpectedToken();
        }
        read();
        localObject1 = compileReturnValue(paramRoutine, paramStatementCompound);
        break;
      case 18: 
        localObject1 = compileCompoundStatement(paramRoutine, paramStatementCompound, localHsqlName1);
        break;
      case 339: 
        if (paramRoutine.isTrigger()) {
          throw unexpectedToken();
        }
        localObject1 = compileWhile(paramRoutine, paramStatementCompound, localHsqlName1);
        break;
      case 248: 
        localObject1 = compileRepeat(paramRoutine, paramStatementCompound, localHsqlName1);
        break;
      case 170: 
        localObject1 = compileLoop(paramRoutine, paramStatementCompound, localHsqlName1);
        break;
      case 120: 
        localObject1 = compileFor(paramRoutine, paramStatementCompound, localHsqlName1);
        break;
      case 153: 
        if (localHsqlName1 != null) {
          throw unexpectedToken();
        }
        localObject1 = compileIterate();
        break;
      case 162: 
        if (localHsqlName1 != null) {
          throw unexpectedToken();
        }
        localObject1 = compileLeave(paramRoutine, paramStatementCompound);
        break;
      case 434: 
        localObject1 = compileIf(paramRoutine, paramStatementCompound);
        break;
      case 32: 
        localObject1 = compileCase(paramRoutine, paramStatementCompound);
        break;
      case 269: 
        localObject1 = compileSignal(paramRoutine, paramStatementCompound, localHsqlName1);
        break;
      case 249: 
        localObject1 = compileResignal(paramRoutine, paramStatementCompound, localHsqlName1);
        break;
      default: 
        label529:
        localObject2 = null;
        return (Statement)localObject2;
      }
      ((Statement)localObject1).setRoot(paramRoutine);
      ((Statement)localObject1).setParent(paramStatementCompound);
      Object localObject2 = localObject1;
      return (Statement)localObject2;
    }
    finally
    {
      this.session.setCurrentSchemaHsqlName(localHsqlName2);
    }
  }
  
  HsqlNameManager.HsqlName readLabel()
  {
    HsqlNameManager.HsqlName localHsqlName = readNewSchemaObjectName(21, false);
    if (this.token.tokenType != 823) {
      throw unexpectedToken(localHsqlName.getNameString());
    }
    readThis(823);
    return localHsqlName;
  }
  
  Statement compileReturnValue(Routine paramRoutine, StatementCompound paramStatementCompound)
  {
    RangeGroup[] arrayOfRangeGroup = new RangeGroup[1];
    arrayOfRangeGroup[0] = (paramStatementCompound == null ? paramRoutine : paramStatementCompound);
    this.compileContext.setOuterRanges(arrayOfRangeGroup);
    Expression localExpression = XreadValueExpressionOrNull();
    if (localExpression == null) {
      throw unexpectedToken();
    }
    resolveOuterReferencesAndTypes(paramRoutine, paramStatementCompound, localExpression);
    if (paramRoutine.isProcedure()) {
      throw Error.parseError(5602, null, this.scanner.getLineNumber());
    }
    if ((paramRoutine.returnsTable()) && (localExpression.getType() != 23)) {
      throw Error.parseError(5611, null, this.scanner.getLineNumber());
    }
    RowType localRowType = new RowType(localExpression.getNodeDataTypes());
    Object localObject = paramRoutine.getReturnType();
    if (!((Type)localObject).isRowType()) {
      localObject = new RowType(new Type[] { paramRoutine.getReturnType() });
    }
    if (((Type)localObject).getDegree() != localRowType.getDegree()) {
      throw Error.parseError(5564, null, this.scanner.getLineNumber());
    }
    if (!((Type)localObject).canBeAssignedFrom(localRowType)) {
      throw Error.parseError(5611, null, this.scanner.getLineNumber());
    }
    return new StatementExpression(this.session, this.compileContext, 62, localExpression);
  }
  
  Statement compileIterate()
  {
    readThis(153);
    HsqlNameManager.HsqlName localHsqlName = readNewSchemaObjectName(21, false);
    return new StatementSimple(103, localHsqlName);
  }
  
  Statement compileLeave(Routine paramRoutine, StatementCompound paramStatementCompound)
  {
    readThis(162);
    HsqlNameManager.HsqlName localHsqlName = readNewSchemaObjectName(21, false);
    return new StatementSimple(104, localHsqlName);
  }
  
  Statement compileWhile(Routine paramRoutine, StatementCompound paramStatementCompound, HsqlNameManager.HsqlName paramHsqlName)
  {
    readThis(339);
    Expression localExpression = XreadBooleanValueExpression();
    resolveOuterReferencesAndTypes(paramRoutine, paramStatementCompound, localExpression);
    StatementExpression localStatementExpression = new StatementExpression(this.session, this.compileContext, 1211, localExpression);
    readThis(91);
    Statement[] arrayOfStatement = compileSQLProcedureStatementList(paramRoutine, paramStatementCompound);
    readThis(99);
    readThis(339);
    if ((isSimpleName()) && (!isReservedKey()))
    {
      if (paramHsqlName == null) {
        throw unexpectedToken();
      }
      if (!paramHsqlName.name.equals(this.token.tokenString)) {
        throw Error.error(5508, this.token.tokenString);
      }
      read();
    }
    StatementCompound localStatementCompound = new StatementCompound(110, paramHsqlName, paramStatementCompound);
    localStatementCompound.setStatements(arrayOfStatement);
    localStatementCompound.setCondition(localStatementExpression);
    return localStatementCompound;
  }
  
  Statement compileRepeat(Routine paramRoutine, StatementCompound paramStatementCompound, HsqlNameManager.HsqlName paramHsqlName)
  {
    readThis(248);
    Statement[] arrayOfStatement = compileSQLProcedureStatementList(paramRoutine, paramStatementCompound);
    readThis(318);
    Expression localExpression = XreadBooleanValueExpression();
    resolveOuterReferencesAndTypes(paramRoutine, paramStatementCompound, localExpression);
    StatementExpression localStatementExpression = new StatementExpression(this.session, this.compileContext, 1211, localExpression);
    readThis(99);
    readThis(248);
    if ((isSimpleName()) && (!isReservedKey()))
    {
      if (paramHsqlName == null) {
        throw unexpectedToken();
      }
      if (!paramHsqlName.name.equals(this.token.tokenString)) {
        throw Error.error(5508, this.token.tokenString);
      }
      read();
    }
    StatementCompound localStatementCompound = new StatementCompound(107, paramHsqlName, paramStatementCompound);
    localStatementCompound.setStatements(arrayOfStatement);
    localStatementCompound.setCondition(localStatementExpression);
    return localStatementCompound;
  }
  
  Statement compileLoop(Routine paramRoutine, StatementCompound paramStatementCompound, HsqlNameManager.HsqlName paramHsqlName)
  {
    readThis(170);
    Statement[] arrayOfStatement = compileSQLProcedureStatementList(paramRoutine, paramStatementCompound);
    readThis(99);
    readThis(170);
    if ((isSimpleName()) && (!isReservedKey()))
    {
      if (paramHsqlName == null) {
        throw unexpectedToken();
      }
      if (!paramHsqlName.name.equals(this.token.tokenString)) {
        throw Error.error(5508, this.token.tokenString);
      }
      read();
    }
    StatementCompound localStatementCompound = new StatementCompound(105, paramHsqlName, paramStatementCompound);
    localStatementCompound.setStatements(arrayOfStatement);
    return localStatementCompound;
  }
  
  Statement compileFor(Routine paramRoutine, StatementCompound paramStatementCompound, HsqlNameManager.HsqlName paramHsqlName)
  {
    RangeGroup[] arrayOfRangeGroup = new RangeGroup[1];
    arrayOfRangeGroup[0] = (paramStatementCompound == null ? paramRoutine : paramStatementCompound);
    this.compileContext.setOuterRanges(arrayOfRangeGroup);
    readThis(120);
    StatementQuery localStatementQuery = compileCursorSpecification(arrayOfRangeGroup, 0, false);
    readThis(91);
    StatementCompound localStatementCompound = new StatementCompound(101, paramHsqlName, paramStatementCompound);
    localStatementCompound.setAtomic(true);
    localStatementCompound.setRoot(paramRoutine);
    localStatementCompound.setLoopStatement(null, localStatementQuery);
    Statement[] arrayOfStatement = compileSQLProcedureStatementList(paramRoutine, localStatementCompound);
    readThis(99);
    readThis(120);
    if ((isSimpleName()) && (!isReservedKey()))
    {
      if (paramHsqlName == null) {
        throw unexpectedToken();
      }
      if (!paramHsqlName.name.equals(this.token.tokenString)) {
        throw Error.error(5508, this.token.tokenString);
      }
      read();
    }
    localStatementCompound.setStatements(arrayOfStatement);
    return localStatementCompound;
  }
  
  Statement compileIf(Routine paramRoutine, StatementCompound paramStatementCompound)
  {
    HsqlArrayList localHsqlArrayList = new HsqlArrayList();
    readThis(434);
    Expression localExpression = XreadBooleanValueExpression();
    resolveOuterReferencesAndTypes(paramRoutine, paramStatementCompound, localExpression);
    StatementExpression localStatementExpression = new StatementExpression(this.session, this.compileContext, 1211, localExpression);
    localHsqlArrayList.add(localStatementExpression);
    readThis(296);
    Statement[] arrayOfStatement = compileSQLProcedureStatementList(paramRoutine, paramStatementCompound);
    for (int i = 0; i < arrayOfStatement.length; i++) {
      localHsqlArrayList.add(arrayOfStatement[i]);
    }
    while (this.token.tokenType == 98)
    {
      read();
      localExpression = XreadBooleanValueExpression();
      resolveOuterReferencesAndTypes(paramRoutine, paramStatementCompound, localExpression);
      localStatementExpression = new StatementExpression(this.session, this.compileContext, 1211, localExpression);
      localHsqlArrayList.add(localStatementExpression);
      readThis(296);
      arrayOfStatement = compileSQLProcedureStatementList(paramRoutine, paramStatementCompound);
      for (i = 0; i < arrayOfStatement.length; i++) {
        localHsqlArrayList.add(arrayOfStatement[i]);
      }
    }
    if (this.token.tokenType == 97)
    {
      read();
      localExpression = Expression.EXPR_TRUE;
      localStatementExpression = new StatementExpression(this.session, this.compileContext, 1211, localExpression);
      localHsqlArrayList.add(localStatementExpression);
      arrayOfStatement = compileSQLProcedureStatementList(paramRoutine, paramStatementCompound);
      for (i = 0; i < arrayOfStatement.length; i++) {
        localHsqlArrayList.add(arrayOfStatement[i]);
      }
    }
    readThis(99);
    readThis(434);
    arrayOfStatement = new Statement[localHsqlArrayList.size()];
    localHsqlArrayList.toArray(arrayOfStatement);
    StatementCompound localStatementCompound = new StatementCompound(102, null, paramStatementCompound);
    localStatementCompound.setStatements(arrayOfStatement);
    return localStatementCompound;
  }
  
  Statement compileCase(Routine paramRoutine, StatementCompound paramStatementCompound)
  {
    HsqlArrayList localHsqlArrayList = new HsqlArrayList();
    Expression localExpression = null;
    readThis(32);
    if (this.token.tokenType == 331) {
      localHsqlArrayList = readCaseWhen(paramRoutine, paramStatementCompound);
    } else {
      localHsqlArrayList = readSimpleCaseWhen(paramRoutine, paramStatementCompound);
    }
    if (this.token.tokenType == 97)
    {
      read();
      localExpression = Expression.EXPR_TRUE;
      StatementExpression localStatementExpression = new StatementExpression(this.session, this.compileContext, 1211, localExpression);
      localHsqlArrayList.add(localStatementExpression);
      arrayOfStatement = compileSQLProcedureStatementList(paramRoutine, paramStatementCompound);
      for (int i = 0; i < arrayOfStatement.length; i++) {
        localHsqlArrayList.add(arrayOfStatement[i]);
      }
    }
    readThis(99);
    readThis(32);
    Statement[] arrayOfStatement = new Statement[localHsqlArrayList.size()];
    localHsqlArrayList.toArray(arrayOfStatement);
    StatementCompound localStatementCompound = new StatementCompound(102, null, paramStatementCompound);
    localStatementCompound.setStatements(arrayOfStatement);
    return localStatementCompound;
  }
  
  HsqlArrayList readSimpleCaseWhen(Routine paramRoutine, StatementCompound paramStatementCompound)
  {
    HsqlArrayList localHsqlArrayList = new HsqlArrayList();
    Object localObject1 = null;
    Expression localExpression = XreadRowValuePredicand();
    for (;;)
    {
      readThis(331);
      for (;;)
      {
        Object localObject2 = XreadPredicateRightPart(localExpression);
        if (localExpression == localObject2) {
          localObject2 = new ExpressionLogical(localExpression, XreadRowValuePredicand());
        }
        resolveOuterReferencesAndTypes(paramRoutine, paramStatementCompound, (Expression)localObject2);
        if (localObject1 == null) {
          localObject1 = localObject2;
        } else {
          localObject1 = new ExpressionLogical(50, (Expression)localObject1, (Expression)localObject2);
        }
        if (this.token.tokenType != 824) {
          break;
        }
        read();
      }
      StatementExpression localStatementExpression = new StatementExpression(this.session, this.compileContext, 1211, (Expression)localObject1);
      localHsqlArrayList.add(localStatementExpression);
      readThis(296);
      Statement[] arrayOfStatement = compileSQLProcedureStatementList(paramRoutine, paramStatementCompound);
      for (int i = 0; i < arrayOfStatement.length; i++) {
        localHsqlArrayList.add(arrayOfStatement[i]);
      }
      if (this.token.tokenType != 331) {
        break;
      }
    }
    return localHsqlArrayList;
  }
  
  HsqlArrayList readCaseWhen(Routine paramRoutine, StatementCompound paramStatementCompound)
  {
    HsqlArrayList localHsqlArrayList = new HsqlArrayList();
    Expression localExpression = null;
    for (;;)
    {
      readThis(331);
      localExpression = XreadBooleanValueExpression();
      resolveOuterReferencesAndTypes(paramRoutine, paramStatementCompound, localExpression);
      StatementExpression localStatementExpression = new StatementExpression(this.session, this.compileContext, 1211, localExpression);
      localHsqlArrayList.add(localStatementExpression);
      readThis(296);
      Statement[] arrayOfStatement = compileSQLProcedureStatementList(paramRoutine, paramStatementCompound);
      for (int i = 0; i < arrayOfStatement.length; i++) {
        localHsqlArrayList.add(arrayOfStatement[i]);
      }
      if (this.token.tokenType != 331) {
        break;
      }
    }
    return localHsqlArrayList;
  }
  
  Statement compileSignal(Routine paramRoutine, StatementCompound paramStatementCompound, HsqlNameManager.HsqlName paramHsqlName)
  {
    Expression localExpression = null;
    readThis(269);
    readThis(277);
    String str = parseSQLStateValue();
    if (readIfThis(268))
    {
      readThis(463);
      readThis(417);
      localExpression = XreadSimpleValueSpecificationOrNull();
      if (localExpression == null) {
        throw unexpectedToken();
      }
      resolveOuterReferencesAndTypes(paramRoutine, paramStatementCompound, localExpression);
    }
    StatementSignal localStatementSignal = new StatementSignal(108, str, localExpression);
    return localStatementSignal;
  }
  
  private Statement compileResignal(Routine paramRoutine, StatementCompound paramStatementCompound, HsqlNameManager.HsqlName paramHsqlName)
  {
    String str = null;
    Expression localExpression = null;
    readThis(249);
    if (readIfThis(277))
    {
      str = parseSQLStateValue();
      if (readIfThis(268))
      {
        readThis(463);
        readThis(417);
        localExpression = XreadSimpleValueSpecificationOrNull();
        if (localExpression == null) {
          throw unexpectedToken();
        }
        resolveOuterReferencesAndTypes(paramRoutine, paramStatementCompound, localExpression);
      }
    }
    StatementSignal localStatementSignal = new StatementSignal(106, str, localExpression);
    return localStatementSignal;
  }
  
  ColumnSchema readRoutineParameter(Routine paramRoutine, boolean paramBoolean)
  {
    HsqlNameManager.HsqlName localHsqlName = null;
    int i = readRoutineParameterMode(paramRoutine.routineType, paramRoutine.isAggregate);
    if (!isReservedKey()) {
      localHsqlName = readNewDependentSchemaObjectName(paramRoutine.getName(), 23);
    }
    Type localType = readTypeDefinition(false, true);
    ColumnSchema localColumnSchema = new ColumnSchema(localHsqlName, localType, true, false, null);
    if (paramBoolean) {
      localColumnSchema.setParameterMode((byte)i);
    }
    return localColumnSchema;
  }
  
  int readRoutineParameterMode(int paramInt, boolean paramBoolean)
  {
    int i = 1;
    switch (this.token.tokenType)
    {
    case 140: 
      read();
      break;
    case 209: 
      if (paramInt != 17) {
        throw unexpectedToken();
      }
      read();
      i = 4;
      break;
    case 143: 
      if ((paramInt != 17) && (!paramBoolean)) {
        throw unexpectedToken();
      }
      read();
      i = 2;
      break;
    }
    return i;
  }
  
  void resolveOuterReferencesAndTypes(Routine paramRoutine, StatementCompound paramStatementCompound, Expression paramExpression)
  {
    StatementCompound localStatementCompound = paramStatementCompound == null ? paramRoutine : paramStatementCompound;
    resolveOuterReferencesAndTypes(new RangeGroup[] { localStatementCompound }, paramExpression);
  }
  
  StatementSchema compileCreateTrigger(boolean paramBoolean)
  {
    Object localObject1 = null;
    boolean bool = false;
    int i = 0;
    int j = 0;
    HsqlNameManager.HsqlName localHsqlName2 = null;
    OrderedHashSet localOrderedHashSet = null;
    int[] arrayOfInt = null;
    read();
    HsqlNameManager.HsqlName localHsqlName1 = readNewSchemaObjectName(8, true);
    int k;
    switch (this.token.tokenType)
    {
    case 444: 
      k = TriggerDef.getTiming(444);
      read();
      readThis(201);
      break;
    case 357: 
    case 364: 
      k = TriggerDef.getTiming(this.token.tokenType);
      read();
      break;
    default: 
      throw unexpectedToken();
    }
    int m;
    switch (this.token.tokenType)
    {
    case 84: 
    case 145: 
      m = TriggerDef.getOperationType(this.token.tokenType);
      read();
      break;
    case 319: 
      m = TriggerDef.getOperationType(this.token.tokenType);
      read();
      if ((this.token.tokenType == 201) && (k != 6))
      {
        read();
        localOrderedHashSet = new OrderedHashSet();
        readColumnNameList(localOrderedHashSet, null, false);
      }
      break;
    default: 
      throw unexpectedToken();
    }
    readThis(204);
    Table localTable = readTableName();
    if (this.token.tokenType == 364)
    {
      read();
      checkIsSimpleName();
      localHsqlName2 = readNewSchemaObjectName(8, true);
    }
    localHsqlName1.setSchemaIfNull(localTable.getSchemaName());
    checkSchemaUpdateAuthorisation(localHsqlName1.schema);
    if (k == 6)
    {
      if ((!localTable.isView()) || (((View)localTable).getCheckOption() == 2)) {
        throw Error.error(5538, localHsqlName1.schema.name);
      }
    }
    else if (localTable.isView()) {
      throw Error.error(5538, localHsqlName1.schema.name);
    }
    if (localHsqlName1.schema != localTable.getSchemaName()) {
      throw Error.error(5505, localHsqlName1.schema.name);
    }
    localHsqlName1.parent = localTable.getName();
    this.database.schemaManager.checkSchemaObjectNotExists(localHsqlName1);
    if (localOrderedHashSet != null)
    {
      arrayOfInt = localTable.getColumnIndexes(localOrderedHashSet);
      for (int n = 0; n < arrayOfInt.length; n++) {
        if (arrayOfInt[n] == -1) {
          throw Error.error(5544, (String)localOrderedHashSet.get(n));
        }
      }
    }
    Expression localExpression = null;
    HsqlNameManager.SimpleName localSimpleName = null;
    Object localObject3 = null;
    Object localObject4 = null;
    Object localObject5 = null;
    Table[] arrayOfTable = new Table[4];
    RangeVariable[] arrayOfRangeVariable = new RangeVariable[4];
    String str2 = null;
    RangeGroup[] arrayOfRangeGroup = { new RangeGroup.RangeGroupSimple(arrayOfRangeVariable, false) };
    Object localObject7;
    Object localObject9;
    if (this.token.tokenType == 237)
    {
      read();
      if ((this.token.tokenType != 203) && (this.token.tokenType != 189)) {
        throw unexpectedToken();
      }
      for (;;)
      {
        Object localObject6;
        if (this.token.tokenType == 203)
        {
          if (m == 55) {
            throw unexpectedToken();
          }
          read();
          if (this.token.tokenType == 294)
          {
            if ((Boolean.TRUE.equals(localObject1)) || (localSimpleName != null) || (k == 4)) {
              throw unexpectedToken();
            }
            read();
            readIfThis(11);
            checkIsSimpleName();
            read();
            localSimpleName = HsqlNameManager.getSimpleName(this.token.tokenString, this.token.isDelimitedIdentifier);
            localObject6 = localSimpleName;
            if ((((HsqlNameManager.SimpleName)localObject6).equals(localObject3)) || (((HsqlNameManager.SimpleName)localObject6).equals(localObject4)) || (((HsqlNameManager.SimpleName)localObject6).equals(localObject5))) {
              throw unexpectedToken();
            }
            localObject1 = Boolean.FALSE;
            localObject7 = this.database.nameManager.newHsqlName(localTable.getSchemaName(), ((HsqlNameManager.SimpleName)localObject6).name, isDelimitedIdentifier(), 10);
            localObject8 = new Table(localTable, (HsqlNameManager.HsqlName)localObject7);
            localObject9 = new RangeVariable((Table)localObject8, null, null, null, this.compileContext);
            arrayOfTable[2] = localObject8;
            arrayOfRangeVariable[2] = localObject9;
          }
          else
          {
            if ((Boolean.FALSE.equals(localObject1)) || (localObject4 != null)) {
              throw unexpectedToken();
            }
            readIfThis(257);
            readIfThis(11);
            checkIsSimpleName();
            localObject4 = HsqlNameManager.getSimpleName(this.token.tokenString, this.token.isDelimitedIdentifier);
            read();
            localObject6 = localObject4;
            if ((((HsqlNameManager.SimpleName)localObject6).equals(localObject3)) || (((HsqlNameManager.SimpleName)localObject6).equals(localSimpleName)) || (((HsqlNameManager.SimpleName)localObject6).equals(localObject5))) {
              throw unexpectedToken();
            }
            localObject1 = Boolean.TRUE;
            localObject7 = new RangeVariable(localTable.columnList, (HsqlNameManager.SimpleName)localObject4, false, 2);
            ((RangeVariable)localObject7).rangePosition = 0;
            arrayOfTable[0] = null;
            arrayOfRangeVariable[0] = localObject7;
          }
        }
        else
        {
          if (this.token.tokenType != 189) {
            break;
          }
          if (m == 19) {
            throw unexpectedToken();
          }
          read();
          if (this.token.tokenType == 294)
          {
            if ((Boolean.TRUE.equals(localObject1)) || (localObject3 != null) || (k == 4)) {
              throw unexpectedToken();
            }
            read();
            readIfThis(11);
            checkIsSimpleName();
            localObject3 = HsqlNameManager.getSimpleName(this.token.tokenString, this.token.isDelimitedIdentifier);
            read();
            localObject1 = Boolean.FALSE;
            localObject6 = localObject3;
            if ((((HsqlNameManager.SimpleName)localObject6).equals(localSimpleName)) || (((HsqlNameManager.SimpleName)localObject6).equals(localObject4)) || (((HsqlNameManager.SimpleName)localObject6).equals(localObject5))) {
              throw unexpectedToken();
            }
            localObject7 = this.database.nameManager.newHsqlName(localTable.getSchemaName(), ((HsqlNameManager.SimpleName)localObject6).name, isDelimitedIdentifier(), 10);
            localObject8 = new Table(localTable, (HsqlNameManager.HsqlName)localObject7);
            localObject9 = new RangeVariable((Table)localObject8, null, null, null, this.compileContext);
            arrayOfTable[3] = localObject8;
            arrayOfRangeVariable[3] = localObject9;
          }
          else
          {
            if ((Boolean.FALSE.equals(localObject1)) || (localObject5 != null)) {
              throw unexpectedToken();
            }
            readIfThis(257);
            readIfThis(11);
            checkIsSimpleName();
            localObject5 = HsqlNameManager.getSimpleName(this.token.tokenString, this.token.isDelimitedIdentifier);
            read();
            localObject6 = localObject5;
            if ((((HsqlNameManager.SimpleName)localObject6).equals(localSimpleName)) || (((HsqlNameManager.SimpleName)localObject6).equals(localObject3)) || (((HsqlNameManager.SimpleName)localObject6).equals(localObject4))) {
              throw unexpectedToken();
            }
            localObject1 = Boolean.TRUE;
            localObject7 = new RangeVariable(localTable.columnList, (HsqlNameManager.SimpleName)localObject5, false, 2);
            ((RangeVariable)localObject7).rangePosition = 1;
            arrayOfTable[1] = null;
            arrayOfRangeVariable[1] = localObject7;
          }
        }
      }
    }
    if ((Boolean.TRUE.equals(localObject1)) && (this.token.tokenType != 120)) {
      throw unexpectedTokenRequire("FOR");
    }
    if (this.token.tokenType == 120)
    {
      read();
      readThis(95);
      if (this.token.tokenType == 257)
      {
        if (Boolean.FALSE.equals(localObject1)) {
          throw unexpectedToken();
        }
        localObject1 = Boolean.TRUE;
      }
      else if (this.token.tokenType == 539)
      {
        if ((Boolean.TRUE.equals(localObject1)) || (k == 4)) {
          throw unexpectedToken();
        }
        localObject1 = Boolean.FALSE;
      }
      else
      {
        throw unexpectedToken();
      }
      read();
    }
    if ((arrayOfRangeVariable[2] == null) || ((arrayOfRangeVariable[3] == null) || ("QUEUE".equals(this.token.tokenString))))
    {
      read();
      j = readInteger();
      i = 1;
    }
    if ("NOWAIT".equals(this.token.tokenString))
    {
      read();
      bool = true;
    }
    int i1;
    if ((this.token.tokenType == 331) && (k != 6))
    {
      read();
      readThis(836);
      i1 = getPosition();
      this.isCheckOrTriggerCondition = true;
      localExpression = XreadBooleanValueExpression();
      str2 = getLastPart(i1);
      this.isCheckOrTriggerCondition = false;
      readThis(822);
      localObject7 = localExpression.resolveColumnReferences(this.session, arrayOfRangeGroup[0], arrayOfRangeGroup, null);
      ExpressionColumn.checkColumnsResolved((HsqlList)localObject7);
      localExpression.resolveTypes(this.session, null);
      if (localExpression.getDataType() != Type.SQL_BOOLEAN) {
        throw Error.error(5568);
      }
    }
    if (localObject1 == null) {
      localObject1 = Boolean.FALSE;
    }
    if (this.token.tokenType == 28)
    {
      i1 = getPosition();
      try
      {
        read();
        checkIsSimpleName();
        checkIsDelimitedIdentifier();
        String str1 = this.token.tokenString;
        read();
        if (this.token.tokenType == 836) {
          throw unexpectedToken();
        }
        localObject2 = new TriggerDef(localHsqlName1, k, m, ((Boolean)localObject1).booleanValue(), localTable, arrayOfTable, arrayOfRangeVariable, localExpression, str2, arrayOfInt, str1, bool, j);
        localObject7 = getLastPart();
        localObject8 = new Object[] { localObject2, localHsqlName2 };
        localObject9 = new HsqlNameManager.HsqlName[] { this.database.getCatalogName(), localTable.getName() };
        return new StatementSchema((String)localObject7, 90, (Object[])localObject8, null, (HsqlNameManager.HsqlName[])localObject9);
      }
      catch (HsqlException localHsqlException)
      {
        rewind(i1);
      }
    }
    if (i != 0) {
      throw unexpectedToken("QUEUE");
    }
    if (bool) {
      throw unexpectedToken("NOWAIT");
    }
    Routine localRoutine = compileTriggerRoutine(localTable, arrayOfRangeVariable, k, m);
    Object localObject2 = new TriggerDefSQL(localHsqlName1, k, m, ((Boolean)localObject1).booleanValue(), localTable, arrayOfTable, arrayOfRangeVariable, localExpression, str2, arrayOfInt, localRoutine);
    String str3 = getLastPart();
    Object localObject8 = { localObject2, localHsqlName2 };
    return new StatementSchema(str3, 90, (Object[])localObject8, null, new HsqlNameManager.HsqlName[] { this.database.getCatalogName(), localTable.getName() });
  }
  
  Routine compileTriggerRoutine(Table paramTable, RangeVariable[] paramArrayOfRangeVariable, int paramInt1, int paramInt2)
  {
    int i = paramInt1 == 4 ? 3 : 4;
    Routine localRoutine = new Routine(paramTable, paramArrayOfRangeVariable, i, paramInt1, paramInt2);
    this.session.sessionContext.pushRoutineTables();
    try
    {
      startRecording();
      StatementCompound localStatementCompound = new StatementCompound(99, null, null);
      localStatementCompound.rangeVariables = paramArrayOfRangeVariable;
      Statement localStatement = compileSQLProcedureStatementOrNull(localRoutine, null);
      if (localStatement == null) {
        throw unexpectedToken();
      }
      Token[] arrayOfToken = getRecordedStatement();
      String str = Token.getSQL(arrayOfToken);
      localStatement.setSQL(str);
      localRoutine.setProcedure(localStatement);
      localRoutine.resolve(this.session);
    }
    finally
    {
      this.session.sessionContext.popRoutineTables();
    }
    return localRoutine;
  }
  
  void checkSchemaUpdateAuthorisation(HsqlNameManager.HsqlName paramHsqlName)
  {
    if (this.session.isProcessingLog()) {
      return;
    }
    SqlInvariants.checkSchemaNameNotSystem(paramHsqlName.name);
    if (this.isSchemaDefinition)
    {
      if (paramHsqlName != this.session.getCurrentSchemaHsqlName()) {
        throw Error.error(5505);
      }
    }
    else {
      this.session.getGrantee().checkSchemaUpdateOrGrantRights(paramHsqlName.name);
    }
    this.session.checkDDLWrite();
  }
}


/* Location:              E:\java\java学习\hutubill\lib\all.jar!\org\hsqldb\ParserRoutine.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */
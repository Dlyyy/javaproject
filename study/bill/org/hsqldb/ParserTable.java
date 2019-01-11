package org.hsqldb;

import org.hsqldb.error.Error;
import org.hsqldb.index.Index;
import org.hsqldb.lib.HsqlArrayList;
import org.hsqldb.lib.OrderedHashSet;
import org.hsqldb.lib.OrderedIntHashSet;
import org.hsqldb.types.BinaryData;
import org.hsqldb.types.Type;

public class ParserTable
  extends ParserDML
{
  ParserTable(Session paramSession, Scanner paramScanner)
  {
    super(paramSession, paramScanner);
  }
  
  StatementSchema compileCreateTable(int paramInt)
  {
    boolean bool = readIfNotExists().booleanValue();
    HsqlNameManager.HsqlName localHsqlName = readNewSchemaObjectName(3, false);
    localHsqlName.setSchemaIfNull(this.session.getCurrentSchemaHsqlName());
    Object localObject;
    switch (paramInt)
    {
    case 6: 
    case 7: 
      localObject = new TextTable(this.database, localHsqlName, paramInt);
      break;
    default: 
      localObject = new Table(this.database, localHsqlName, paramInt);
    }
    if (this.token.tokenType == 11) {
      return compileCreateTableAsSubqueryDefinition((Table)localObject);
    }
    return compileCreateTableBody((Table)localObject, bool);
  }
  
  StatementSchema compileCreateTableBody(Table paramTable, boolean paramBoolean)
  {
    HsqlArrayList localHsqlArrayList1 = new HsqlArrayList();
    HsqlArrayList localHsqlArrayList2 = new HsqlArrayList();
    boolean bool = readTableContentsSource(paramTable, localHsqlArrayList1, localHsqlArrayList2);
    if (!bool) {
      return compileCreateTableAsSubqueryDefinition(paramTable);
    }
    readTableOnCommitClause(paramTable);
    if ((this.database.sqlSyntaxMys) && (readIfThis(599)))
    {
      readIfThis(417);
      localObject1 = readQuotedString();
      paramTable.getName().comment = ((String)localObject1);
    }
    Object localObject1 = new OrderedHashSet();
    ((OrderedHashSet)localObject1).add(this.database.getCatalogName());
    for (int i = 0; i < localHsqlArrayList1.size(); i++)
    {
      localObject2 = (Constraint)localHsqlArrayList1.get(i);
      localObject3 = ((Constraint)localObject2).getMainTableName();
      if (localObject3 != null)
      {
        Table localTable = this.database.schemaManager.findUserTable(((HsqlNameManager.HsqlName)localObject3).name, ((HsqlNameManager.HsqlName)localObject3).schema.name);
        if ((localTable != null) && (!localTable.isTemp())) {
          ((OrderedHashSet)localObject1).add(paramTable.getName());
        }
      }
    }
    String str = getLastPart();
    Object localObject2 = { paramTable, localHsqlArrayList1, localHsqlArrayList2, null, Boolean.valueOf(paramBoolean) };
    Object localObject3 = new HsqlNameManager.HsqlName[((OrderedHashSet)localObject1).size()];
    ((OrderedHashSet)localObject1).toArray((Object[])localObject3);
    return new StatementSchema(str, 87, (Object[])localObject2, null, (HsqlNameManager.HsqlName[])localObject3);
  }
  
  boolean readTableContentsSource(Table paramTable, HsqlArrayList paramHsqlArrayList1, HsqlArrayList paramHsqlArrayList2)
  {
    int i = getPosition();
    readThis(836);
    Constraint localConstraint = new Constraint(null, null, 5);
    paramHsqlArrayList1.add(localConstraint);
    int j = 1;
    int k = 1;
    int m = 0;
    while (m == 0)
    {
      switch (this.token.tokenType)
      {
      case 164: 
        localObject = readLikeTable(paramTable);
        for (int n = 0; n < localObject.length; n++) {
          paramTable.addColumn(localObject[n]);
        }
        j = 0;
        k = 0;
        break;
      case 40: 
      case 51: 
      case 121: 
      case 228: 
      case 315: 
        if (k == 0) {
          throw unexpectedToken();
        }
        readConstraint(paramTable, paramHsqlArrayList1);
        j = 0;
        k = 0;
        break;
      case 824: 
        if (k != 0) {
          throw unexpectedToken();
        }
        read();
        k = 1;
        break;
      case 822: 
        read();
        m = 1;
        break;
      case 449: 
      case 621: 
        if (this.database.sqlSyntaxMys)
        {
          readIndex(paramTable, paramHsqlArrayList2);
          j = 0;
          k = 0;
        }
        break;
      }
      if (k == 0) {
        throw unexpectedToken();
      }
      checkIsSchemaObjectName();
      Object localObject = this.database.nameManager.newColumnHsqlName(paramTable.getName(), this.token.tokenString, isDelimitedIdentifier());
      read();
      ColumnSchema localColumnSchema = readColumnDefinitionOrNull(paramTable, (HsqlNameManager.HsqlName)localObject, paramHsqlArrayList1);
      if (localColumnSchema == null)
      {
        if (j != 0)
        {
          rewind(i);
          return false;
        }
        throw Error.error(5000);
      }
      paramTable.addColumn(localColumnSchema);
      j = 0;
      k = 0;
    }
    if (paramTable.getColumnCount() == 0) {
      throw Error.error(5591);
    }
    return true;
  }
  
  void readTableOnCommitClause(Table paramTable)
  {
    if (this.token.tokenType == 204)
    {
      if (!paramTable.isTemp()) {
        throw unexpectedToken();
      }
      read();
      readThis(47);
      if ((this.token.tokenType != 84) && (this.token.tokenType == 498)) {
        paramTable.persistenceScope = 23;
      }
      read();
      readThis(259);
    }
  }
  
  private ColumnSchema[] readLikeTable(Table paramTable)
  {
    read();
    int i = 0;
    int j = 0;
    int k = 0;
    Table localTable = readTableName();
    OrderedIntHashSet localOrderedIntHashSet = new OrderedIntHashSet();
    for (;;)
    {
      int m = this.token.tokenType == 438 ? 1 : 0;
      if ((m == 0) && (this.token.tokenType != 420)) {
        break;
      }
      read();
      switch (this.token.tokenType)
      {
      case 429: 
        if (!localOrderedIntHashSet.add(this.token.tokenType)) {
          throw unexpectedToken();
        }
        i = m;
        break;
      case 138: 
        if (!localOrderedIntHashSet.add(this.token.tokenType)) {
          throw unexpectedToken();
        }
        j = m;
        break;
      case 402: 
        if (!localOrderedIntHashSet.add(this.token.tokenType)) {
          throw unexpectedToken();
        }
        k = m;
        break;
      default: 
        throw unexpectedToken();
      }
      read();
    }
    ColumnSchema[] arrayOfColumnSchema = new ColumnSchema[localTable.getColumnCount()];
    for (int n = 0; n < arrayOfColumnSchema.length; n++)
    {
      ColumnSchema localColumnSchema = localTable.getColumn(n).duplicate();
      HsqlNameManager.HsqlName localHsqlName = this.database.nameManager.newColumnSchemaHsqlName(paramTable.getName(), localColumnSchema.getName());
      localColumnSchema.setName(localHsqlName);
      localColumnSchema.setPrimaryKey(false);
      if (j != 0)
      {
        if (localColumnSchema.isIdentity()) {
          localColumnSchema.setIdentity(localColumnSchema.getIdentitySequence().duplicate());
        }
      }
      else {
        localColumnSchema.setIdentity(null);
      }
      if (k == 0) {
        localColumnSchema.setDefaultExpression(null);
      }
      if (i == 0) {
        localColumnSchema.setGeneratingExpression(null);
      }
      arrayOfColumnSchema[n] = localColumnSchema;
    }
    return arrayOfColumnSchema;
  }
  
  StatementSchema compileCreateTableAsSubqueryDefinition(Table paramTable)
  {
    HsqlNameManager.HsqlName[] arrayOfHsqlName1 = null;
    int i = 1;
    HsqlNameManager.HsqlName[] arrayOfHsqlName2 = null;
    StatementQuery localStatementQuery = null;
    if (this.token.tokenType == 836) {
      arrayOfHsqlName2 = readColumnNames(paramTable.getName());
    }
    readThis(11);
    readThis(836);
    QueryExpression localQueryExpression = XreadQueryExpression();
    localQueryExpression.setReturningResult();
    localQueryExpression.resolve(this.session);
    readThis(822);
    readThis(336);
    if (this.token.tokenType == 190)
    {
      read();
      i = 0;
    }
    else if (paramTable.getTableType() == 7)
    {
      throw unexpectedTokenRequire("NO");
    }
    readThis(399);
    if (this.token.tokenType == 204)
    {
      if (!paramTable.isTemp()) {
        throw unexpectedToken();
      }
      read();
      readThis(47);
      if ((this.token.tokenType != 84) && (this.token.tokenType == 498)) {
        paramTable.persistenceScope = 23;
      }
      read();
      readThis(259);
    }
    if (arrayOfHsqlName2 == null) {
      arrayOfHsqlName2 = localQueryExpression.getResultColumnNames();
    } else if (arrayOfHsqlName2.length != localQueryExpression.getColumnCount()) {
      throw Error.error(5593);
    }
    TableUtil.setColumnsInSchemaTable(paramTable, arrayOfHsqlName2, localQueryExpression.getColumnTypes());
    paramTable.createPrimaryKey();
    if ((paramTable.isTemp()) && (paramTable.hasLobColumn())) {
      throw Error.error(5534);
    }
    if (i != 0)
    {
      localStatementQuery = new StatementQuery(this.session, localQueryExpression, this.compileContext);
      arrayOfHsqlName1 = localStatementQuery.getTableNamesForRead();
    }
    Object[] arrayOfObject = { paramTable, new HsqlArrayList(), null, localStatementQuery, Boolean.FALSE };
    String str = getLastPart();
    HsqlNameManager.HsqlName[] arrayOfHsqlName3 = this.database.schemaManager.catalogNameArray;
    StatementSchema localStatementSchema = new StatementSchema(str, 87, arrayOfObject, arrayOfHsqlName1, arrayOfHsqlName3);
    return localStatementSchema;
  }
  
  static Table addTableConstraintDefinitions(Session paramSession, Table paramTable, HsqlArrayList paramHsqlArrayList1, HsqlArrayList paramHsqlArrayList2, boolean paramBoolean)
  {
    Constraint localConstraint1 = (Constraint)paramHsqlArrayList1.get(0);
    String str = localConstraint1.getName() == null ? null : localConstraint1.getName().name;
    HsqlNameManager.HsqlName localHsqlName = paramSession.database.nameManager.newAutoName("IDX", str, paramTable.getSchemaName(), paramTable.getName(), 20);
    localConstraint1.setColumnsIndexes(paramTable);
    paramTable.createPrimaryKey(localHsqlName, localConstraint1.core.mainCols, true);
    if (localConstraint1.core.mainCols != null)
    {
      Constraint localConstraint2 = new Constraint(localConstraint1.getName(), paramTable, paramTable.getPrimaryIndex(), 4);
      paramTable.addConstraint(localConstraint2);
      if (paramBoolean) {
        paramSession.database.schemaManager.addSchemaObject(localConstraint2);
      }
    }
    for (int i = 1; i < paramHsqlArrayList1.size(); i++)
    {
      localConstraint1 = (Constraint)paramHsqlArrayList1.get(i);
      switch (localConstraint1.getConstraintType())
      {
      case 2: 
        localConstraint1.setColumnsIndexes(paramTable);
        if (paramTable.getUniqueConstraintForColumns(localConstraint1.core.mainCols) != null) {
          throw Error.error(5522);
        }
        localHsqlName = paramSession.database.nameManager.newAutoName("IDX", localConstraint1.getName().name, paramTable.getSchemaName(), paramTable.getName(), 20);
        Index localIndex = paramTable.createAndAddIndexStructure(paramSession, localHsqlName, localConstraint1.core.mainCols, null, null, true, true, false);
        Constraint localConstraint3 = new Constraint(localConstraint1.getName(), paramTable, localIndex, 2);
        paramTable.addConstraint(localConstraint3);
        if (paramBoolean) {
          paramSession.database.schemaManager.addSchemaObject(localConstraint3);
        }
        break;
      case 0: 
        addForeignKey(paramSession, paramTable, localConstraint1, paramHsqlArrayList2);
        break;
      case 3: 
        try
        {
          localConstraint1.prepareCheckConstraint(paramSession, paramTable);
        }
        catch (HsqlException localHsqlException)
        {
          if (!paramSession.isProcessingScript()) {
            break label351;
          }
        }
        continue;
        label351:
        throw localHsqlException;
        paramTable.addConstraint(localConstraint1);
        if (localConstraint1.isNotNull())
        {
          ColumnSchema localColumnSchema = paramTable.getColumn(localConstraint1.notNullColumnIndex);
          localColumnSchema.setNullable(false);
          paramTable.setColumnTypeVars(localConstraint1.notNullColumnIndex);
        }
        if (paramBoolean) {
          paramSession.database.schemaManager.addSchemaObject(localConstraint1);
        }
        break;
      }
    }
    return paramTable;
  }
  
  static void addForeignKey(Session paramSession, Table paramTable, Constraint paramConstraint, HsqlArrayList paramHsqlArrayList)
  {
    HsqlNameManager.HsqlName localHsqlName1 = paramConstraint.getMainTableName();
    if (localHsqlName1 == paramTable.getName())
    {
      paramConstraint.core.mainTable = paramTable;
    }
    else
    {
      localObject = paramSession.database.schemaManager.findUserTable(localHsqlName1.name, localHsqlName1.schema.name);
      if (localObject == null)
      {
        if (paramHsqlArrayList == null) {
          throw Error.error(5501, localHsqlName1.name);
        }
        paramHsqlArrayList.add(paramConstraint);
        return;
      }
      paramConstraint.core.mainTable = ((Table)localObject);
    }
    paramConstraint.setColumnsIndexes(paramTable);
    Object localObject = new TableWorks(paramSession, paramTable);
    ((TableWorks)localObject).checkCreateForeignKey(paramConstraint);
    Constraint localConstraint = paramConstraint.core.mainTable.getUniqueConstraintForColumns(paramConstraint.core.mainCols);
    if (localConstraint == null) {
      throw Error.error(5523);
    }
    Index localIndex1 = localConstraint.getMainIndex();
    boolean bool = paramConstraint.core.mainTable.getSchemaName() != paramTable.getSchemaName();
    int i = paramSession.database.schemaManager.getTableIndex(paramTable);
    if ((i != -1) && (i < paramSession.database.schemaManager.getTableIndex(paramConstraint.core.mainTable))) {
      bool = true;
    }
    HsqlNameManager.HsqlName localHsqlName2 = paramSession.database.nameManager.newAutoName("IDX", paramTable.getSchemaName(), paramTable.getName(), 20);
    Index localIndex2 = paramTable.createAndAddIndexStructure(paramSession, localHsqlName2, paramConstraint.core.refCols, null, null, false, true, bool);
    HsqlNameManager.HsqlName localHsqlName3 = paramSession.database.nameManager.newAutoName("REF", paramConstraint.getName().name, paramTable.getSchemaName(), paramTable.getName(), 20);
    paramConstraint.core.uniqueName = localConstraint.getName();
    paramConstraint.core.mainName = localHsqlName3;
    paramConstraint.core.mainIndex = localIndex1;
    paramConstraint.core.refTable = paramTable;
    paramConstraint.core.refName = paramConstraint.getName();
    paramConstraint.core.refIndex = localIndex2;
    paramConstraint.isForward = bool;
    paramTable.addConstraint(paramConstraint);
    paramConstraint.core.mainTable.addConstraint(new Constraint(localHsqlName3, paramConstraint));
    paramSession.database.schemaManager.addSchemaObject(paramConstraint);
  }
  
  Constraint readFKReferences(Table paramTable, HsqlNameManager.HsqlName paramHsqlName, OrderedHashSet paramOrderedHashSet)
  {
    OrderedHashSet localOrderedHashSet = null;
    readThis(236);
    HsqlNameManager.HsqlName localHsqlName2;
    if (this.token.namePrefix == null) {
      localHsqlName2 = paramTable.getSchemaName();
    } else {
      localHsqlName2 = this.database.schemaManager.getSchemaHsqlName(this.token.namePrefix);
    }
    HsqlNameManager.HsqlName localHsqlName1;
    if ((paramTable.getSchemaName() == localHsqlName2) && (paramTable.getName().name.equals(this.token.tokenString)))
    {
      localHsqlName1 = paramTable.getName();
      read();
    }
    else
    {
      localHsqlName1 = readFKTableName(localHsqlName2);
    }
    if (this.token.tokenType == 836) {
      localOrderedHashSet = readColumnNames(false);
    }
    int i = 65;
    if (this.token.tokenType == 172)
    {
      read();
      switch (this.token.tokenType)
      {
      case 533: 
        read();
        break;
      case 492: 
        throw unsupportedFeature();
      case 125: 
        read();
        i = 67;
        break;
      default: 
        throw unexpectedToken();
      }
    }
    int j = 3;
    int k = 3;
    OrderedIntHashSet localOrderedIntHashSet = new OrderedIntHashSet();
    while (this.token.tokenType == 204)
    {
      read();
      if (!localOrderedIntHashSet.add(this.token.tokenType)) {
        throw unexpectedToken();
      }
      if (this.token.tokenType == 84)
      {
        read();
        if (this.token.tokenType == 268)
        {
          read();
          switch (this.token.tokenType)
          {
          case 83: 
            read();
            j = 4;
            break;
          case 196: 
            read();
            j = 2;
            break;
          default: 
            throw unexpectedToken();
          }
        }
        else if (this.token.tokenType == 368)
        {
          read();
          j = 0;
        }
        else if (this.token.tokenType == 507)
        {
          read();
        }
        else
        {
          readThis(190);
          readThis(353);
        }
      }
      else if (this.token.tokenType == 319)
      {
        read();
        if (this.token.tokenType == 268)
        {
          read();
          switch (this.token.tokenType)
          {
          case 83: 
            read();
            k = 4;
            break;
          case 196: 
            read();
            k = 2;
            break;
          default: 
            throw unexpectedToken();
          }
        }
        else if (this.token.tokenType == 368)
        {
          read();
          k = 0;
        }
        else if (this.token.tokenType == 507)
        {
          read();
        }
        else
        {
          readThis(190);
          readThis(353);
        }
      }
      else
      {
        throw unexpectedToken();
      }
    }
    if (paramHsqlName == null) {
      paramHsqlName = this.database.nameManager.newAutoName("FK", paramTable.getSchemaName(), paramTable.getName(), 5);
    }
    return new Constraint(paramHsqlName, paramTable.getName(), paramOrderedHashSet, localHsqlName1, localOrderedHashSet, 0, j, k, i);
  }
  
  HsqlNameManager.HsqlName readFKTableName(HsqlNameManager.HsqlName paramHsqlName)
  {
    checkIsSchemaObjectName();
    Table localTable = this.database.schemaManager.findUserTable(this.token.tokenString, paramHsqlName.name);
    HsqlNameManager.HsqlName localHsqlName;
    if (localTable == null) {
      localHsqlName = this.database.nameManager.newHsqlName(paramHsqlName, this.token.tokenString, isDelimitedIdentifier(), 3);
    } else {
      localHsqlName = localTable.getName();
    }
    read();
    return localHsqlName;
  }
  
  ColumnSchema readColumnDefinitionOrNull(Table paramTable, HsqlNameManager.HsqlName paramHsqlName, HsqlArrayList paramHsqlArrayList)
  {
    int i = 0;
    int j = 0;
    int k = 0;
    boolean bool1 = false;
    Expression localExpression1 = null;
    boolean bool2 = true;
    Expression localExpression2 = null;
    Object localObject = null;
    NumberSequence localNumberSequence = null;
    switch (this.token.tokenType)
    {
    case 429: 
      read();
      readThis(358);
      i = 1;
      bool1 = true;
      throw unexpectedToken("GENERATED");
    case 138: 
      read();
      j = 1;
      k = 1;
      localObject = Type.SQL_INTEGER;
      localNumberSequence = new NumberSequence(null, 0L, 1L, (Type)localObject);
      break;
    case 824: 
      return null;
    case 822: 
      return null;
    default: 
      if (this.token.isUndelimitedIdentifier) {
        if ("SERIAL".equals(this.token.tokenString))
        {
          if (this.database.sqlSyntaxMys)
          {
            read();
            j = 1;
            k = 1;
            localObject = Type.SQL_BIGINT;
            localNumberSequence = new NumberSequence(null, 1L, 1L, (Type)localObject);
            break;
          }
          if (this.database.sqlSyntaxPgs)
          {
            read();
            j = 1;
            localObject = Type.SQL_INTEGER;
            localNumberSequence = new NumberSequence(null, 1L, 1L, (Type)localObject);
            break;
          }
        }
        else if (("BIGSERIAL".equals(this.token.tokenString)) && (this.database.sqlSyntaxPgs))
        {
          read();
          j = 1;
          k = 1;
          localObject = Type.SQL_BIGINT;
          localNumberSequence = new NumberSequence(null, 1L, 1L, (Type)localObject);
          break;
        }
      }
      localObject = readTypeDefinition(true, true);
    }
    if ((i == 0) && (j == 0))
    {
      if (this.database.sqlSyntaxMys) {
        switch (this.token.tokenType)
        {
        case 196: 
          read();
          break;
        case 193: 
          read();
          readThis(196);
          bool2 = false;
          break;
        }
      }
      switch (this.token.tokenType)
      {
      case 336: 
        if (this.database.sqlSyntaxDb2) {
          read();
        } else {
          throw unexpectedToken();
        }
      case 83: 
        read();
        localExpression2 = readDefaultClause((Type)localObject);
        if ((localExpression2.opType == 12) && (this.database.sqlSyntaxPgs))
        {
          localNumberSequence = ((ExpressionColumn)localExpression2).sequence;
          localExpression2 = null;
          j = 1;
        }
        break;
      case 429: 
        read();
        if (this.token.tokenType == 27)
        {
          read();
          readThis(83);
        }
        else
        {
          readThis(358);
          bool1 = true;
        }
        readThis(11);
        if (this.token.tokenType == 138)
        {
          read();
          localNumberSequence = new NumberSequence(null, (Type)localObject);
          localNumberSequence.setAlways(bool1);
          if (this.token.tokenType == 836)
          {
            read();
            readSequenceOptions(localNumberSequence, false, false, true);
            readThis(822);
          }
          j = 1;
        }
        else if (this.token.tokenType == 836)
        {
          if (!bool1) {
            throw unexpectedTokenRequire("IDENTITY");
          }
          i = 1;
        }
        else if (this.token.tokenType == 527)
        {
          if (bool1) {
            throw unexpectedToken();
          }
          read();
          if ((this.token.namePrefix != null) && (!this.token.namePrefix.equals(paramTable.getSchemaName().name))) {
            throw unexpectedToken(this.token.namePrefix);
          }
          localNumberSequence = this.database.schemaManager.getSequence(this.token.tokenString, paramTable.getSchemaName().name, true);
          j = 1;
          read();
        }
        break;
      case 138: 
        read();
        j = 1;
        k = 1;
        localNumberSequence = new NumberSequence(null, 0L, 1L, (Type)localObject);
      }
    }
    if (i != 0)
    {
      readThis(836);
      localExpression1 = XreadValueExpression();
      readThis(822);
    }
    if ((i == 0) && (j == 0) && (this.database.sqlSyntaxMys) && (this.token.isUndelimitedIdentifier) && ("AUTO_INCREMENT".equals(this.token.tokenString)))
    {
      read();
      j = 1;
      localNumberSequence = new NumberSequence(null, 1L, 1L, (Type)localObject);
    }
    ColumnSchema localColumnSchema = new ColumnSchema(paramHsqlName, (Type)localObject, bool2, false, localExpression2);
    localColumnSchema.setGeneratingExpression(localExpression1);
    readColumnConstraints(paramTable, localColumnSchema, paramHsqlArrayList);
    if ((this.token.tokenType == 138) && (j == 0))
    {
      read();
      j = 1;
      k = 1;
      localNumberSequence = new NumberSequence(null, 0L, 1L, (Type)localObject);
    }
    if ((this.token.tokenType == 429) && (j == 0) && (i == 0))
    {
      read();
      if (this.token.tokenType == 27)
      {
        read();
        readThis(83);
      }
      else
      {
        readThis(358);
        bool1 = true;
      }
      readThis(11);
      readThis(138);
      localNumberSequence = new NumberSequence(null, (Type)localObject);
      localNumberSequence.setAlways(bool1);
      if (this.token.tokenType == 836)
      {
        read();
        readSequenceOptions(localNumberSequence, false, false, true);
        readThis(822);
      }
      j = 1;
    }
    if (j != 0) {
      localColumnSchema.setIdentity(localNumberSequence);
    }
    if ((k != 0) && (!localColumnSchema.isPrimaryKey()))
    {
      OrderedHashSet localOrderedHashSet = new OrderedHashSet();
      localOrderedHashSet.add(localColumnSchema.getName().name);
      HsqlNameManager.HsqlName localHsqlName = this.database.nameManager.newAutoName("PK", paramTable.getSchemaName(), paramTable.getName(), 5);
      Constraint localConstraint = new Constraint(localHsqlName, localOrderedHashSet, 4);
      localConstraint.setSimpleIdentityPK();
      paramHsqlArrayList.set(0, localConstraint);
      localColumnSchema.setPrimaryKey(true);
    }
    if ((this.database.sqlSyntaxPgs) && (this.token.tokenType == 83) && (localColumnSchema.getDefaultExpression() == null) && (localColumnSchema.getIdentitySequence() == null))
    {
      read();
      localExpression2 = readDefaultClause((Type)localObject);
      if (localExpression2.opType == 12)
      {
        localNumberSequence = ((ExpressionColumn)localExpression2).sequence;
        localExpression2 = null;
      }
      localColumnSchema.setDefaultExpression(localExpression2);
      localColumnSchema.setIdentity(localNumberSequence);
    }
    return localColumnSchema;
  }
  
  void readConstraint(SchemaObject paramSchemaObject, HsqlArrayList paramHsqlArrayList)
  {
    HsqlNameManager.HsqlName localHsqlName = null;
    if (this.token.tokenType == 51)
    {
      read();
      localHsqlName = readNewDependentSchemaObjectName(paramSchemaObject.getName(), 5);
    }
    Object localObject1;
    Object localObject2;
    switch (this.token.tokenType)
    {
    case 228: 
      if (paramSchemaObject.getName().type != 3) {
        throw unexpectedTokenRequire("CHECK");
      }
      read();
      readThis(449);
      localObject1 = (Constraint)paramHsqlArrayList.get(0);
      if ((((Constraint)localObject1).getConstraintType() == 4) && (!((Constraint)localObject1).isSimpleIdentityPK)) {
        throw Error.error(5532);
      }
      if (localHsqlName == null) {
        localHsqlName = this.database.nameManager.newAutoName("PK", paramSchemaObject.getSchemaName(), paramSchemaObject.getName(), 5);
      }
      localObject2 = readColumnNames(false);
      Constraint localConstraint = new Constraint(localHsqlName, (OrderedHashSet)localObject2, 4);
      paramHsqlArrayList.set(0, localConstraint);
      break;
    case 315: 
      if (paramSchemaObject.getName().type != 3) {
        throw unexpectedTokenRequire("CHECK");
      }
      read();
      if ((this.database.sqlSyntaxMys) && (!readIfThis(621))) {
        readIfThis(449);
      }
      localObject1 = readColumnNames(false);
      if (localHsqlName == null) {
        localHsqlName = this.database.nameManager.newAutoName("CT", paramSchemaObject.getSchemaName(), paramSchemaObject.getName(), 5);
      }
      localObject2 = new Constraint(localHsqlName, (OrderedHashSet)localObject1, 2);
      paramHsqlArrayList.add(localObject2);
      break;
    case 121: 
      if (paramSchemaObject.getName().type != 3) {
        throw unexpectedTokenRequire("CHECK");
      }
      read();
      readThis(449);
      localObject1 = readColumnNames(false);
      localObject2 = readFKReferences((Table)paramSchemaObject, localHsqlName, (OrderedHashSet)localObject1);
      paramHsqlArrayList.add(localObject2);
      break;
    case 40: 
      read();
      if (localHsqlName == null) {
        localHsqlName = this.database.nameManager.newAutoName("CT", paramSchemaObject.getSchemaName(), paramSchemaObject.getName(), 5);
      }
      localObject1 = new Constraint(localHsqlName, null, 3);
      readCheckConstraintCondition((Constraint)localObject1);
      paramHsqlArrayList.add(localObject1);
      break;
    default: 
      if (localHsqlName != null) {
        throw unexpectedToken();
      }
      break;
    }
  }
  
  void readColumnConstraints(Table paramTable, ColumnSchema paramColumnSchema, HsqlArrayList paramHsqlArrayList)
  {
    int i = 0;
    int j = 0;
    int k = 0;
    int m = 0;
    if ((paramColumnSchema.getDataType().typeCode == 93) && (this.token.tokenType == 204))
    {
      int n = getPosition();
      try
      {
        read();
        readThis(319);
        readThis(72);
        FunctionSQL localFunctionSQL = FunctionSQL.newSQLFunction("CURRENT_TIMESTAMP", this.compileContext);
        localFunctionSQL.resolveTypes(this.session, null);
        paramColumnSchema.setUpdateExpression(localFunctionSQL);
      }
      catch (Exception localException)
      {
        rewind(n);
      }
    }
    for (;;)
    {
      HsqlNameManager.HsqlName localHsqlName = null;
      if (this.token.tokenType == 51)
      {
        read();
        localHsqlName = readNewDependentSchemaObjectName(paramTable.getName(), 5);
      }
      Object localObject1;
      Object localObject2;
      switch (this.token.tokenType)
      {
      case 228: 
        if ((k != 0) || (m != 0)) {
          throw unexpectedToken();
        }
        read();
        readThis(449);
        localObject1 = (Constraint)paramHsqlArrayList.get(0);
        if (((Constraint)localObject1).getConstraintType() == 4) {
          throw Error.error(5532);
        }
        localObject2 = new OrderedHashSet();
        ((OrderedHashSet)localObject2).add(paramColumnSchema.getName().name);
        if (localHsqlName == null) {
          localHsqlName = this.database.nameManager.newAutoName("PK", paramTable.getSchemaName(), paramTable.getName(), 5);
        }
        Constraint localConstraint = new Constraint(localHsqlName, (OrderedHashSet)localObject2, 4);
        paramHsqlArrayList.set(0, localConstraint);
        paramColumnSchema.setPrimaryKey(true);
        m = 1;
        break;
      case 315: 
        read();
        localObject1 = new OrderedHashSet();
        ((OrderedHashSet)localObject1).add(paramColumnSchema.getName().name);
        if (localHsqlName == null) {
          localHsqlName = this.database.nameManager.newAutoName("CT", paramTable.getSchemaName(), paramTable.getName(), 5);
        }
        localObject2 = new Constraint(localHsqlName, (OrderedHashSet)localObject1, 2);
        paramHsqlArrayList.add(localObject2);
        break;
      case 121: 
        read();
        readThis(449);
      case 236: 
        localObject1 = new OrderedHashSet();
        ((OrderedHashSet)localObject1).add(paramColumnSchema.getName().name);
        localObject2 = readFKReferences(paramTable, localHsqlName, (OrderedHashSet)localObject1);
        paramHsqlArrayList.add(localObject2);
        break;
      case 40: 
        read();
        if (localHsqlName == null) {
          localHsqlName = this.database.nameManager.newAutoName("CT", paramTable.getSchemaName(), paramTable.getName(), 5);
        }
        localObject1 = new Constraint(localHsqlName, null, 3);
        readCheckConstraintCondition((Constraint)localObject1);
        localObject2 = ((Constraint)localObject1).getCheckColumnExpressions();
        for (int i1 = 0; i1 < ((OrderedHashSet)localObject2).size(); i1++)
        {
          ExpressionColumn localExpressionColumn = (ExpressionColumn)((OrderedHashSet)localObject2).get(i1);
          if (paramColumnSchema.getName().name.equals(localExpressionColumn.getColumnName()))
          {
            if ((localExpressionColumn.getSchemaName() != null) && (!localExpressionColumn.getSchemaName().equals(paramTable.getSchemaName().name))) {
              throw Error.error(5505);
            }
          }
          else {
            throw Error.error(5501);
          }
        }
        paramHsqlArrayList.add(localObject1);
        break;
      case 193: 
        if ((j != 0) || (k != 0)) {
          throw unexpectedToken();
        }
        read();
        readThis(196);
        if (localHsqlName == null) {
          localHsqlName = this.database.nameManager.newAutoName("CT", paramTable.getSchemaName(), paramTable.getName(), 5);
        }
        localObject1 = new Constraint(localHsqlName, null, 3);
        ((Constraint)localObject1).check = new ExpressionLogical(paramColumnSchema);
        paramHsqlArrayList.add(localObject1);
        j = 1;
        break;
      case 196: 
        if ((j != 0) || (k != 0) || (m != 0)) {
          throw unexpectedToken();
        }
        if (localHsqlName != null) {
          throw unexpectedToken();
        }
        read();
        k = 1;
        break;
      default: 
        i = 1;
      }
      if (i != 0) {
        break;
      }
    }
  }
  
  void readCheckConstraintCondition(Constraint paramConstraint)
  {
    readThis(836);
    startRecording();
    this.isCheckOrTriggerCondition = true;
    Expression localExpression = XreadBooleanValueExpression();
    this.isCheckOrTriggerCondition = false;
    Token[] arrayOfToken = getRecordedStatement();
    readThis(822);
    paramConstraint.check = localExpression;
  }
  
  Expression readDefaultClause(Type paramType)
  {
    Expression localExpression = null;
    int i = 0;
    if (this.token.tokenType == 196)
    {
      read();
      return new ExpressionValue(null, paramType);
    }
    if ((paramType.isDateTimeType()) || (paramType.isIntervalType())) {}
    Object localObject1;
    switch (this.token.tokenType)
    {
    case 77: 
    case 150: 
    case 297: 
    case 298: 
      localExpression = readDateTimeIntervalLiteral(this.session);
      if (localExpression.dataType.typeCode != paramType.typeCode) {
        throw unexpectedToken();
      }
      localObject1 = localExpression.getValue(this.session, paramType);
      return new ExpressionValue(localObject1, paramType);
    case 911: 
      break;
    default: 
      localExpression = XreadDateTimeValueFunctionOrNull();
      if (localExpression != null)
      {
        localExpression = XreadModifier(localExpression);
        break;
        if (paramType.isNumberType())
        {
          if ((this.database.sqlSyntaxPgs) && (this.token.tokenType == 639)) {
            return readNextvalFunction();
          }
          if ((this.database.sqlDoubleNaN) && (paramType.typeCode == 8))
          {
            localExpression = XreadNumericValueExpression();
          }
          else if (this.token.tokenType == 834)
          {
            read();
            i = 1;
          }
        }
        else if (paramType.isCharacterType())
        {
          switch (this.token.tokenType)
          {
          case 64: 
          case 67: 
          case 68: 
          case 70: 
          case 74: 
          case 267: 
          case 293: 
          case 321: 
            localObject1 = FunctionSQL.newSQLFunction(this.token.tokenString, this.compileContext);
            localExpression = readSQLFunction((FunctionSQL)localObject1);
            break;
          }
        }
        else if (paramType.isBooleanType())
        {
          switch (this.token.tokenType)
          {
          case 310: 
            read();
            return Expression.EXPR_TRUE;
          case 114: 
            read();
            return Expression.EXPR_FALSE;
          }
        }
        else if (paramType.isBitType())
        {
          switch (this.token.tokenType)
          {
          case 310: 
            read();
            return new ExpressionValue(BinaryData.singleBitOne, paramType);
          case 114: 
            read();
            return new ExpressionValue(BinaryData.singleBitZero, paramType);
          }
        }
        else if (paramType.isArrayType())
        {
          localExpression = readCollection(19);
          if (localExpression.nodes.length > 0) {
            throw Error.parseError(5562, null, this.scanner.getLineNumber());
          }
          localExpression.dataType = paramType;
          return localExpression;
        }
      }
      break;
    }
    if (localExpression != null)
    {
      localExpression.resolveTypes(this.session, null);
      if (!paramType.canBeAssignedFrom(localExpression.getDataType())) {
        throw Error.parseError(5562, null, this.scanner.getLineNumber());
      }
      return localExpression;
    }
    int j = 0;
    if (((this.database.sqlSyntaxMss) || (this.database.sqlSyntaxPgs)) && (this.token.tokenType == 836))
    {
      read();
      j = 1;
    }
    Object localObject2;
    Object localObject3;
    Object localObject4;
    if (this.token.tokenType == 911)
    {
      localObject2 = this.token.tokenValue;
      localObject3 = this.token.dataType;
      localObject4 = paramType;
      if (paramType.typeCode == 40) {
        localObject4 = Type.getType(12, null, this.database.collation, paramType.precision, 0);
      } else if (paramType.typeCode == 30) {
        localObject4 = Type.getType(61, null, null, paramType.precision, 0);
      }
      if (i != 0) {
        localObject2 = ((Type)localObject3).negate(localObject2);
      }
      localObject2 = ((Type)localObject4).convertToType(this.session, localObject2, (Type)localObject3);
      read();
      if (j != 0) {
        readThis(822);
      }
      return new ExpressionValue(localObject2, (Type)localObject4);
    }
    if ((this.database.sqlSyntaxOra) || (this.database.sqlSyntaxPgs))
    {
      localExpression = XreadSimpleValueExpressionPrimary();
      if (localExpression != null)
      {
        if (localExpression.getType() == 22)
        {
          localObject2 = (TableDerived)localExpression.getTable();
          localObject3 = (QuerySpecification)((TableDerived)localObject2).getQueryExpression();
          ((QuerySpecification)localObject3).setReturningResult();
        }
        localExpression.resolveColumnReferences(this.session, RangeGroup.emptyGroup, 0, RangeGroup.emptyArray, null, true);
        localExpression.resolveTypes(this.session, null);
        if (localExpression.getType() == 22)
        {
          localObject2 = (TableDerived)localExpression.getTable();
          localObject3 = (QuerySpecification)((TableDerived)localObject2).getQueryExpression();
          localObject4 = localObject3.getRangeVariables()[0].getTable();
          if ((localObject4 != this.session.database.schemaManager.dualTable) || (((QuerySpecification)localObject3).exprColumns.length != 1)) {
            throw Error.error(5565);
          }
          localExpression = localObject3.exprColumns[0];
        }
        if (j != 0) {
          readThis(822);
        }
        return localExpression;
      }
    }
    if (this.database.sqlSyntaxDb2)
    {
      localObject2 = null;
      switch (paramType.typeComparisonGroup)
      {
      case 12: 
        localObject2 = "";
        break;
      case 61: 
        localObject2 = BinaryData.zeroLengthBinary;
        break;
      case 2: 
        localObject2 = Integer.valueOf(0);
        break;
      case 16: 
        localObject2 = Boolean.FALSE;
        break;
      case 40: 
        localObject2 = "";
        return new ExpressionValue(localObject2, Type.SQL_VARCHAR_DEFAULT);
      case 30: 
        localObject2 = BinaryData.zeroLengthBinary;
        return new ExpressionValue(localObject2, Type.SQL_VARBINARY_DEFAULT);
      case 92: 
        localObject3 = FunctionSQL.newSQLFunction("CURRENT_TIME", this.compileContext);
        ((FunctionSQL)localObject3).resolveTypes(this.session, null);
        return (Expression)localObject3;
      case 91: 
        localObject3 = FunctionSQL.newSQLFunction("CURRENT_DATE", this.compileContext);
        ((FunctionSQL)localObject3).resolveTypes(this.session, null);
        return (Expression)localObject3;
      case 93: 
        localObject3 = FunctionSQL.newSQLFunction("CURRENT_TIMESTAMP", this.compileContext);
        ((FunctionSQL)localObject3).resolveTypes(this.session, null);
        return (Expression)localObject3;
      }
      localObject2 = paramType.convertToDefaultType(this.session, localObject2);
      return new ExpressionValue(localObject2, paramType);
    }
    if (j != 0) {
      readThis(822);
    }
    throw unexpectedToken();
  }
  
  void readSequenceOptions(NumberSequence paramNumberSequence, boolean paramBoolean1, boolean paramBoolean2, boolean paramBoolean3)
  {
    OrderedIntHashSet localOrderedIntHashSet = new OrderedIntHashSet();
    for (;;)
    {
      int i = 0;
      if (localOrderedIntHashSet.contains(this.token.tokenType)) {
        throw unexpectedToken();
      }
      long l;
      switch (this.token.tokenType)
      {
      case 11: 
        if (paramBoolean1)
        {
          localOrderedIntHashSet.add(this.token.tokenType);
          read();
          Type localType = readTypeDefinition(false, true);
          paramNumberSequence.setDefaults(paramNumberSequence.getName(), localType);
        }
        else
        {
          throw unexpectedToken();
        }
        break;
      case 281: 
        localOrderedIntHashSet.add(this.token.tokenType);
        read();
        readThis(336);
        l = readBigint();
        paramNumberSequence.setStartValueNoCheck(l);
        if (paramBoolean3) {
          readIfThis(824);
        }
        break;
      case 506: 
        if (!paramBoolean2)
        {
          i = 1;
        }
        else
        {
          localOrderedIntHashSet.add(this.token.tokenType);
          read();
          if (readIfThis(336))
          {
            l = readBigint();
            paramNumberSequence.setCurrentValueNoCheck(l);
          }
          else
          {
            paramNumberSequence.setStartValueDefault();
          }
        }
        break;
      case 439: 
        localOrderedIntHashSet.add(this.token.tokenType);
        read();
        readThis(27);
        l = readBigint();
        paramNumberSequence.setIncrement(l);
        break;
      case 190: 
        read();
        if (localOrderedIntHashSet.contains(this.token.tokenType)) {
          throw unexpectedToken();
        }
        if (this.token.tokenType == 460) {
          paramNumberSequence.setDefaultMaxValue();
        } else if (this.token.tokenType == 464) {
          paramNumberSequence.setDefaultMinValue();
        } else if (this.token.tokenType == 76) {
          paramNumberSequence.setCycle(false);
        } else {
          throw unexpectedToken();
        }
        localOrderedIntHashSet.add(this.token.tokenType);
        read();
        break;
      case 460: 
        localOrderedIntHashSet.add(this.token.tokenType);
        read();
        l = readBigint();
        paramNumberSequence.setMaxValueNoCheck(l);
        break;
      case 464: 
        localOrderedIntHashSet.add(this.token.tokenType);
        read();
        l = readBigint();
        paramNumberSequence.setMinValueNoCheck(l);
        break;
      case 76: 
        localOrderedIntHashSet.add(this.token.tokenType);
        read();
        paramNumberSequence.setCycle(true);
        break;
      default: 
        if (((this.database.sqlSyntaxOra) || (this.database.sqlSyntaxDb2)) && (isSimpleName()))
        {
          if ((this.token.tokenString.equals("NOCACHE")) || (this.token.tokenString.equals("NOCYCLE")) || (this.token.tokenString.equals("NOMAXVALUE")) || (this.token.tokenString.equals("NOMINVALUE")) || (this.token.tokenString.equals("NOORDER")) || (this.token.tokenString.equals("ORDER")))
          {
            read();
            break;
          }
          if (this.token.tokenString.equals("CACHE"))
          {
            read();
            readBigint();
            break;
          }
        }
        i = 1;
      }
      if (i != 0) {
        break;
      }
    }
    paramNumberSequence.checkValues();
  }
  
  private void readIndex(Table paramTable, HsqlArrayList paramHsqlArrayList)
  {
    read();
    HsqlNameManager.HsqlName localHsqlName1 = readNewSchemaObjectName(20, true);
    HsqlNameManager.HsqlName localHsqlName2 = paramTable.getSchemaName();
    localHsqlName1.schema = localHsqlName2;
    localHsqlName1.parent = paramTable.getName();
    localHsqlName1.schema = paramTable.getSchemaName();
    if ((readIfThis(322)) && (("BTREE".equals(this.token.tokenString)) || ("HASH".equals(this.token.tokenString)))) {
      read();
    }
    readThis(204);
    int[] arrayOfInt = readColumnList(paramTable, true);
    Constraint localConstraint = new Constraint(localHsqlName1, paramTable, arrayOfInt, 20);
    paramHsqlArrayList.add(localConstraint);
  }
  
  Boolean readIfNotExists()
  {
    Boolean localBoolean = Boolean.FALSE;
    if (this.token.tokenType == 434)
    {
      int i = getPosition();
      read();
      if (this.token.tokenType == 193)
      {
        read();
        readThis(109);
        localBoolean = Boolean.TRUE;
      }
      else
      {
        rewind(i);
        localBoolean = Boolean.FALSE;
      }
    }
    return localBoolean;
  }
}


/* Location:              E:\java\java学习\hutubill\lib\all.jar!\org\hsqldb\ParserTable.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */
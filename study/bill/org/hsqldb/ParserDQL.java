package org.hsqldb;

import org.hsqldb.error.Error;
import org.hsqldb.lib.ArrayUtil;
import org.hsqldb.lib.HashMappedList;
import org.hsqldb.lib.HsqlArrayList;
import org.hsqldb.lib.HsqlList;
import org.hsqldb.lib.IntValueHashMap;
import org.hsqldb.lib.Iterator;
import org.hsqldb.lib.LongDeque;
import org.hsqldb.lib.OrderedHashSet;
import org.hsqldb.lib.OrderedIntKeyHashMap;
import org.hsqldb.lib.Set;
import org.hsqldb.map.BitMap;
import org.hsqldb.map.ValuePool;
import org.hsqldb.result.ResultProperties;
import org.hsqldb.types.ArrayType;
import org.hsqldb.types.Charset;
import org.hsqldb.types.Collation;
import org.hsqldb.types.IntervalType;
import org.hsqldb.types.Type;
import org.hsqldb.types.Types;

public class ParserDQL
  extends ParserBase
{
  protected Database database;
  protected Session session;
  protected final CompileContext compileContext;
  
  ParserDQL(Session paramSession, Scanner paramScanner, CompileContext paramCompileContext)
  {
    super(paramScanner);
    this.session = paramSession;
    this.database = paramSession.getDatabase();
    this.compileContext = new CompileContext(paramSession, this, paramCompileContext);
  }
  
  void reset(Session paramSession, String paramString)
  {
    super.reset(paramSession, paramString);
    this.compileContext.reset();
    this.lastError = null;
  }
  
  void checkIsSchemaObjectName()
  {
    if (this.database.sqlEnforceNames) {
      checkIsNonReservedIdentifier();
    } else {
      checkIsNonCoreReservedIdentifier();
    }
    if (this.database.sqlRegularNames) {
      checkIsIrregularCharInIdentifier();
    }
  }
  
  Type readTypeDefinition(boolean paramBoolean1, boolean paramBoolean2)
  {
    int i = Integer.MIN_VALUE;
    int j = 0;
    int k = 0;
    int m = 0;
    int n = 0;
    int i1 = 0;
    boolean bool1 = this.database.sqlEnforceSize;
    checkIsIdentifier();
    if (this.token.namePrefix == null) {
      i = Type.getTypeNr(this.token.tokenString);
    }
    if ((this.database.sqlSyntaxOra) && (!this.session.isProcessingScript()) && (i == 91))
    {
      read();
      return Type.SQL_TIMESTAMP_NO_FRACTION;
    }
    if (i == Integer.MIN_VALUE)
    {
      if (paramBoolean2)
      {
        checkIsSchemaObjectName();
        Type localType = this.database.schemaManager.findDomainOrUDT(this.session, this.token.tokenString, this.token.namePrefix, this.token.namePrePrefix, this.token.namePrePrePrefix);
        if (localType != null)
        {
          getRecordedToken().setExpression(localType);
          this.compileContext.addSchemaObject(localType);
          read();
          return localType;
        }
      }
      if (this.token.namePrefix != null) {
        throw Error.error(5509, this.token.tokenString);
      }
      if (this.database.sqlSyntaxOra) {
        switch (this.token.tokenType)
        {
        case 586: 
        case 587: 
          read();
          return Type.SQL_DOUBLE;
        case 631: 
          read();
          if (this.token.tokenType == 648)
          {
            read();
            return Type.getType(61, null, null, 1073741824L, 0);
          }
          return Type.getType(12, null, this.database.collation, 1073741824L, 0);
        case 474: 
          read();
          if (this.token.tokenType == 836)
          {
            read();
            int i2 = readInteger();
            int i3 = 0;
            if (this.token.tokenType == 824)
            {
              read();
              i3 = readInteger();
            }
            readThis(822);
            return Type.getType(3, null, null, i2, i3);
          }
          return Type.SQL_DECIMAL_DEFAULT;
        case 648: 
          i = 61;
          break;
        case 667: 
          i1 = 1;
          bool1 = false;
          i = 12;
          break;
        case 640: 
          i = 12;
          break;
        case 187: 
          i = 1;
          break;
        }
      }
      if (this.database.sqlSyntaxPgs) {
        switch (this.token.tokenType)
        {
        case 662: 
          i = -1;
          i1 = 1;
          break;
        case 596: 
          i = 100;
        }
      }
      if (this.database.sqlSyntaxMys) {
        switch (this.token.tokenType)
        {
        case 811: 
          i = 12;
          i1 = 1;
          break;
        case 662: 
          i = -1;
          i1 = 1;
          break;
        case 807: 
        case 809: 
          i = -1;
          i1 = 1;
          break;
        case 810: 
          i = -3;
          break;
        case 806: 
        case 808: 
          i = -4;
        }
      }
      if (i == Integer.MIN_VALUE) {
        throw Error.error(5509, this.token.tokenString);
      }
    }
    read();
    switch (i)
    {
    case 1: 
      if (this.token.tokenType == 329)
      {
        read();
        i = 12;
      }
      else if (this.token.tokenType == 157)
      {
        read();
        readThis(475);
        i = 40;
      }
      else if (this.database.sqlSyntaxOra)
      {
        i1 = 1;
      }
      break;
    case 8: 
      if (this.token.tokenType == 226) {
        read();
      }
      break;
    case 60: 
      if (this.token.tokenType == 329)
      {
        read();
        i = 61;
      }
      else if (this.token.tokenType == 157)
      {
        read();
        readThis(475);
        i = 30;
      }
      break;
    case 14: 
      if (this.token.tokenType == 329)
      {
        read();
        i = 15;
      }
      break;
    case 10: 
      return readIntervalType(false);
    }
    long l = i == 93 ? 6L : 0L;
    int i4 = 0;
    if ((Types.requiresPrecision(i)) && (this.token.tokenType != 836) && (bool1) && (!this.session.isProcessingScript())) {
      throw Error.error(5599, Type.getDefaultType(i).getNameString());
    }
    boolean bool2 = Types.acceptsPrecision(i);
    if (this.database.sqlSyntaxMys) {
      switch (i)
      {
      case -6: 
      case 4: 
      case 5: 
      case 25: 
        bool2 = true;
      }
    }
    if (bool2)
    {
      if (this.token.tokenType == 836)
      {
        int i5 = 1;
        read();
        switch (this.token.tokenType)
        {
        case 911: 
          if ((this.token.dataType.typeCode != 4) && (this.token.dataType.typeCode != 25)) {
            throw unexpectedToken();
          }
          break;
        case 918: 
          if ((i == 30) || (i == 40) || (i == 61) || (i == 12)) {}
          switch (this.token.lobMultiplierType)
          {
          case 448: 
            i5 = 1024;
            break;
          case 457: 
            i5 = 1048576;
            break;
          case 427: 
            i5 = 1073741824;
            break;
          case 484: 
          case 543: 
          default: 
            throw unexpectedToken();
            throw unexpectedToken(this.token.getFullString());
          }
          break;
        default: 
          throw unexpectedToken();
        }
        j = 1;
        l = ((Number)this.token.tokenValue).longValue();
        if ((l < 0L) || ((l == 0L) && (!Types.acceptsZeroPrecision(i)))) {
          throw Error.error(5592);
        }
        l *= i5;
        read();
        if ((i == 1) || (i == 12) || (i == 40)) {
          if (this.token.tokenType == 376) {
            read();
          } else if (this.token.tokenType == 476) {
            read();
          }
        }
        if ((Types.acceptsScaleCreateParam(i)) && (this.token.tokenType == 824))
        {
          read();
          i4 = readInteger();
          if (i4 < 0) {
            throw Error.error(5592);
          }
          k = 1;
        }
        if ((i1 != 0) && (!readIfThis(36))) {
          readIfThis(591);
        }
        readThis(822);
      }
      else if (i == 14)
      {
        l = 1L;
      }
      else if ((i == 30) || (i == 40))
      {
        l = 1073741824L;
      }
      else if ((bool1) && ((i == 1) || (i == 60)))
      {
        l = 1L;
      }
      if ((i == 93) || (i == 92))
      {
        if (l > 9L) {
          throw Error.error(5592);
        }
        i4 = (int)l;
        l = 0L;
        if (this.token.tokenType == 336)
        {
          read();
          readThis(297);
          readThis(571);
          if (i == 93) {
            i = 95;
          } else {
            i = 94;
          }
        }
        else if (this.token.tokenType == 338)
        {
          read();
          readThis(297);
          readThis(571);
        }
      }
    }
    switch (i)
    {
    case -1: 
      if (this.database.sqlLongvarIsLob)
      {
        i = 40;
        l = 1073741824L;
      }
      else
      {
        i = 12;
        if (j == 0) {
          l = 16777216L;
        }
      }
      break;
    case -4: 
      if (this.database.sqlLongvarIsLob)
      {
        i = 30;
        l = 1073741824L;
      }
      else
      {
        i = 61;
        if (j == 0) {
          l = 16777216L;
        }
      }
      break;
    case 1: 
      if ((this.database.sqlSyntaxDb2) && (readIfThis(120)))
      {
        readThis(588);
        readThis(399);
        i = 60;
      }
      break;
    case 40: 
      m = 1;
      break;
    case 100: 
      i = 12;
      n = 1;
    case 12: 
      if ((this.database.sqlSyntaxDb2) && (readIfThis(120)))
      {
        readThis(588);
        readThis(399);
        i = 61;
        if (j == 0) {
          l = 32768L;
        }
      }
      else
      {
        m = 1;
        if (j == 0) {
          l = 32768L;
        }
        if ((this.session.isIgnorecase()) && (!this.session.isProcessingScript())) {
          n = 1;
        }
        if (l > 2147483647L) {
          throw Error.error(5592);
        }
      }
      break;
    case 60: 
      break;
    case 61: 
      if (j == 0) {
        l = 32768L;
      }
      if (l > 2147483647L) {
        throw Error.error(5592);
      }
      break;
    case 2: 
    case 3: 
      if ((j == 0) && (k == 0) && (!bool1))
      {
        l = 128L;
        i4 = 32;
      }
      break;
    }
    Collation localCollation = this.database.collation;
    Charset localCharset = null;
    if ((m != 0) && (paramBoolean1))
    {
      if (this.token.tokenType == 38)
      {
        read();
        readThis(268);
        checkIsSchemaObjectName();
        localCharset = (Charset)this.database.schemaManager.getCharacterSet(this.session, this.token.tokenString, this.token.namePrefix);
        read();
      }
      if (this.token.tokenType == 44)
      {
        read();
        checkIsSimpleName();
        localCollation = this.database.schemaManager.getCollation(this.session, this.token.tokenString, this.token.namePrefix);
        read();
      }
      else if (n != 0)
      {
        localCollation = Collation.getUpperCaseCompareCollation(localCollation);
      }
    }
    Object localObject = Type.getType(i, localCharset, localCollation, l, i4);
    if (this.token.tokenType == 8)
    {
      if (((Type)localObject).isLobType()) {
        throw unexpectedToken();
      }
      read();
      int i6 = 1024;
      if (this.token.tokenType == 831)
      {
        read();
        i6 = readInteger();
        if (i4 < 0) {
          throw Error.error(5592);
        }
        readThis(840);
      }
      localObject = new ArrayType((Type)localObject, i6);
    }
    return (Type)localObject;
  }
  
  void readSimpleColumnNames(OrderedHashSet paramOrderedHashSet, RangeVariable paramRangeVariable, boolean paramBoolean)
  {
    do
    {
      ColumnSchema localColumnSchema = readSimpleColumnName(paramRangeVariable, paramBoolean);
      if (!paramOrderedHashSet.add(localColumnSchema.getName().name)) {
        throw Error.error(5579, localColumnSchema.getName().name);
      }
    } while (readIfThis(824));
    if (this.token.tokenType != 822) {
      throw unexpectedToken();
    }
  }
  
  void readTargetSpecificationList(OrderedHashSet paramOrderedHashSet, RangeVariable[] paramArrayOfRangeVariable, LongDeque paramLongDeque)
  {
    do
    {
      Expression localExpression = XreadTargetSpecification(paramArrayOfRangeVariable, paramLongDeque);
      if (!paramOrderedHashSet.add(localExpression))
      {
        ColumnSchema localColumnSchema = localExpression.getColumn();
        throw Error.error(5579, localColumnSchema.getName().name);
      }
    } while (readIfThis(824));
    if ((this.token.tokenType != 822) && (this.token.tokenType != 124)) {
      throw unexpectedToken();
    }
  }
  
  int[] readColumnList(Table paramTable, boolean paramBoolean)
  {
    OrderedHashSet localOrderedHashSet = readColumnNames(paramBoolean);
    return paramTable.getColumnIndexes(localOrderedHashSet);
  }
  
  void readSimpleColumnNames(OrderedHashSet paramOrderedHashSet, Table paramTable, boolean paramBoolean)
  {
    do
    {
      ColumnSchema localColumnSchema = readSimpleColumnName(paramTable, paramBoolean);
      if (!paramOrderedHashSet.add(localColumnSchema.getName().name)) {
        throw Error.error(5577, localColumnSchema.getName().name);
      }
    } while (readIfThis(824));
    if (this.token.tokenType != 822) {
      throw unexpectedToken();
    }
  }
  
  HsqlNameManager.HsqlName[] readColumnNames(HsqlNameManager.HsqlName paramHsqlName)
  {
    BitMap localBitMap = new BitMap(0, true);
    OrderedHashSet localOrderedHashSet = readColumnNames(localBitMap, false);
    HsqlNameManager.HsqlName[] arrayOfHsqlName = new HsqlNameManager.HsqlName[localOrderedHashSet.size()];
    for (int i = 0; i < arrayOfHsqlName.length; i++)
    {
      String str = (String)localOrderedHashSet.get(i);
      boolean bool = localBitMap.isSet(i);
      arrayOfHsqlName[i] = this.database.nameManager.newHsqlName(paramHsqlName.schema, str, bool, 9, paramHsqlName);
    }
    return arrayOfHsqlName;
  }
  
  OrderedHashSet readColumnNames(boolean paramBoolean)
  {
    return readColumnNames(null, paramBoolean);
  }
  
  OrderedHashSet readColumnNames(BitMap paramBitMap, boolean paramBoolean)
  {
    readThis(836);
    OrderedHashSet localOrderedHashSet = new OrderedHashSet();
    readColumnNameList(localOrderedHashSet, paramBitMap, paramBoolean);
    readThis(822);
    return localOrderedHashSet;
  }
  
  void readColumnNameList(OrderedHashSet paramOrderedHashSet, BitMap paramBitMap, boolean paramBoolean)
  {
    int i = 0;
    do
    {
      if (this.session.isProcessingScript())
      {
        if (!isSimpleName()) {
          this.token.isDelimitedIdentifier = true;
        }
      }
      else {
        checkIsSimpleName();
      }
      if (!paramOrderedHashSet.add(this.token.tokenString)) {
        throw Error.error(5577, this.token.tokenString);
      }
      if (paramBitMap != null) {
        paramBitMap.setValue(i, isDelimitedIdentifier());
      }
      read();
      i++;
      if ((paramBoolean) && ((this.token.tokenType == 359) || (this.token.tokenType == 410))) {
        read();
      }
    } while (readIfThis(824));
  }
  
  HsqlNameManager.SimpleName[] readColumnNameList(OrderedHashSet paramOrderedHashSet)
  {
    BitMap localBitMap = new BitMap(0, true);
    readThis(836);
    readColumnNameList(paramOrderedHashSet, localBitMap, false);
    readThis(822);
    HsqlNameManager.SimpleName[] arrayOfSimpleName = new HsqlNameManager.SimpleName[paramOrderedHashSet.size()];
    for (int i = 0; i < paramOrderedHashSet.size(); i++)
    {
      HsqlNameManager.SimpleName localSimpleName = HsqlNameManager.getSimpleName((String)paramOrderedHashSet.get(i), localBitMap.isSet(i));
      arrayOfSimpleName[i] = localSimpleName;
    }
    return arrayOfSimpleName;
  }
  
  int XreadUnionType()
  {
    int i = 0;
    switch (this.token.tokenType)
    {
    case 314: 
      read();
      i = 1;
      if (this.token.tokenType == 2)
      {
        i = 2;
        read();
      }
      else if (this.token.tokenType == 90)
      {
        read();
      }
      break;
    case 148: 
      read();
      i = 3;
      if (this.token.tokenType == 2)
      {
        i = 4;
        read();
      }
      else if (this.token.tokenType == 90)
      {
        read();
      }
      break;
    case 106: 
    case 637: 
      read();
      i = 6;
      if (this.token.tokenType == 2)
      {
        i = 5;
        read();
      }
      else if (this.token.tokenType == 90)
      {
        read();
      }
      break;
    }
    return i;
  }
  
  void XreadUnionCorrespondingClause(QueryExpression paramQueryExpression)
  {
    if (this.token.tokenType == 55)
    {
      read();
      paramQueryExpression.setUnionCorresoponding();
      if (this.token.tokenType == 27)
      {
        read();
        OrderedHashSet localOrderedHashSet = readColumnNames(false);
        paramQueryExpression.setUnionCorrespondingColumns(localOrderedHashSet);
      }
    }
  }
  
  QueryExpression XreadQueryExpression()
  {
    Object localObject2;
    if (this.token.tokenType == 336)
    {
      read();
      boolean bool = readIfThis(234);
      this.compileContext.initSubqueryNames();
      for (;;)
      {
        checkIsSimpleName();
        localObject1 = null;
        HsqlNameManager.HsqlName localHsqlName1 = this.database.nameManager.newHsqlName(this.token.tokenString, isDelimitedIdentifier(), 27);
        localHsqlName1.schema = SqlInvariants.SYSTEM_SCHEMA_HSQLNAME;
        read();
        this.compileContext.registerSubquery(localHsqlName1.name);
        if (this.token.tokenType == 836) {
          localObject1 = readColumnNames(localHsqlName1);
        } else if (bool) {
          throw unexpectedTokenRequire("(");
        }
        readThis(11);
        readThis(836);
        localObject2 = XreadTableNamedSubqueryBody(localHsqlName1, (HsqlNameManager.HsqlName[])localObject1, bool ? 24 : 23);
        readThis(822);
        if (this.token.tokenType == 76) {
          throw unsupportedFeature();
        }
        if ((bool) && (this.token.tokenType == 76))
        {
          Object localObject3 = localObject2;
          int[] arrayOfInt = readColumnList((Table)localObject3, false);
          readThis(268);
          checkIsSimpleName();
          HsqlNameManager.HsqlName localHsqlName2 = this.database.nameManager.newColumnHsqlName(((Table)localObject3).getName(), this.token.tokenString, this.token.isDelimitedIdentifier);
          ColumnSchema localColumnSchema1 = new ColumnSchema(localHsqlName2, null, true, false, null);
          if (((Table)localObject3).getColumnIndex(localHsqlName2.name) != -1) {
            throw Error.error(5578, this.token.tokenString);
          }
          read();
          readThis(301);
          String str1 = readQuotedString();
          if (str1.length() != 1) {
            throw unexpectedToken(str1);
          }
          readThis(83);
          String str2 = readQuotedString();
          if (str2.length() != 1) {
            throw unexpectedToken(str2);
          }
          if (str1.equals(str2)) {
            throw unexpectedToken(str1);
          }
          readThis(322);
          checkIsSimpleName();
          checkIsSimpleName();
          localHsqlName2 = this.database.nameManager.newColumnHsqlName(((Table)localObject3).getName(), this.token.tokenString, this.token.isDelimitedIdentifier);
          if (((Table)localObject3).getColumnIndex(localHsqlName2.name) != -1) {
            throw Error.error(5578, this.token.tokenString);
          }
          read();
          ColumnSchema localColumnSchema2 = new ColumnSchema(localHsqlName2, null, true, false, null);
        }
        this.compileContext.registerSubquery(localHsqlName1.name, (TableDerived)localObject2);
        if (this.token.tokenType != 824) {
          break;
        }
        read();
      }
    }
    QueryExpression localQueryExpression = XreadQueryExpressionBody();
    Object localObject1 = XreadOrderByExpression();
    if (localQueryExpression.sortAndSlice == null)
    {
      localQueryExpression.addSortAndSlice((SortAndSlice)localObject1);
    }
    else if (localQueryExpression.sortAndSlice.hasLimit())
    {
      if (((SortAndSlice)localObject1).hasLimit()) {
        throw Error.error(5549);
      }
      for (int i = 0; i < ((SortAndSlice)localObject1).exprList.size(); i++)
      {
        localObject2 = (Expression)((SortAndSlice)localObject1).exprList.get(i);
        localQueryExpression.sortAndSlice.addOrderExpression((Expression)localObject2);
      }
    }
    else
    {
      localQueryExpression.addSortAndSlice((SortAndSlice)localObject1);
    }
    this.compileContext.unregisterSubqueries();
    return localQueryExpression;
  }
  
  QueryExpression XreadQueryExpressionBody()
  {
    for (QueryExpression localQueryExpression = XreadQueryTerm();; localQueryExpression = XreadSetOperation(localQueryExpression)) {
      switch (this.token.tokenType)
      {
      }
    }
    return localQueryExpression;
  }
  
  QueryExpression XreadQueryTerm()
  {
    for (QueryExpression localQueryExpression = XreadQueryPrimary(); this.token.tokenType == 148; localQueryExpression = XreadSetOperation(localQueryExpression)) {}
    return localQueryExpression;
  }
  
  private QueryExpression XreadSetOperation(QueryExpression paramQueryExpression)
  {
    paramQueryExpression = new QueryExpression(this.compileContext, paramQueryExpression);
    int i = XreadUnionType();
    XreadUnionCorrespondingClause(paramQueryExpression);
    QueryExpression localQueryExpression = XreadQueryTerm();
    paramQueryExpression.addUnion(localQueryExpression, i);
    return paramQueryExpression;
  }
  
  QueryExpression XreadQueryPrimary()
  {
    Object localObject;
    switch (this.token.tokenType)
    {
    case 265: 
    case 294: 
    case 324: 
      localObject = XreadSimpleTable();
      return (QueryExpression)localObject;
    case 836: 
      read();
      localObject = XreadQueryExpressionBody();
      SortAndSlice localSortAndSlice = XreadOrderByExpression();
      readThis(822);
      if (((QueryExpression)localObject).sortAndSlice == null)
      {
        ((QueryExpression)localObject).addSortAndSlice(localSortAndSlice);
      }
      else if (((QueryExpression)localObject).sortAndSlice.hasLimit())
      {
        if (localSortAndSlice.hasLimit()) {
          throw Error.error(5549);
        }
        for (int i = 0; i < localSortAndSlice.exprList.size(); i++)
        {
          Expression localExpression = (Expression)localSortAndSlice.exprList.get(i);
          ((QueryExpression)localObject).sortAndSlice.addOrderExpression(localExpression);
        }
      }
      else
      {
        ((QueryExpression)localObject).addSortAndSlice(localSortAndSlice);
      }
      return (QueryExpression)localObject;
    }
    throw unexpectedToken();
  }
  
  QuerySpecification XreadSimpleTable()
  {
    Object localObject;
    QuerySpecification localQuerySpecification;
    switch (this.token.tokenType)
    {
    case 294: 
      read();
      localObject = readTableName(true);
      if (((Table)localObject).isView()) {
        localObject = ((View)localObject).newDerivedTable(this.session);
      }
      localQuerySpecification = new QuerySpecification(this.session, (Table)localObject, this.compileContext, false);
      break;
    case 324: 
      read();
      localObject = XreadRowValueExpressionList();
      localQuerySpecification = new QuerySpecification(this.session, (Table)localObject, this.compileContext, true);
      break;
    case 265: 
      localQuerySpecification = XreadQuerySpecification();
      break;
    default: 
      throw unexpectedToken();
    }
    return localQuerySpecification;
  }
  
  QuerySpecification XreadQuerySpecification()
  {
    QuerySpecification localQuerySpecification = XreadSelect();
    if ((!localQuerySpecification.isValueList) && (localQuerySpecification.getCurrentRangeVariableCount() == 0)) {
      XreadTableExpression(localQuerySpecification);
    }
    return localQuerySpecification;
  }
  
  void XreadTableExpression(QuerySpecification paramQuerySpecification)
  {
    XreadFromClause(paramQuerySpecification);
    readWhereGroupHaving(paramQuerySpecification);
  }
  
  QuerySpecification XreadSelect()
  {
    QuerySpecification localQuerySpecification = new QuerySpecification(this.compileContext);
    readThis(265);
    Object localObject;
    if ((this.token.tokenType == 663) || (this.token.tokenType == 625))
    {
      localObject = XreadTopOrLimit();
      if (localObject != null) {
        localQuerySpecification.addSortAndSlice((SortAndSlice)localObject);
      }
    }
    if (this.token.tokenType == 90)
    {
      localQuerySpecification.isDistinctSelect = true;
      read();
    }
    else if (this.token.tokenType == 2)
    {
      read();
    }
    do
    {
      localObject = XreadValueExpression();
      if (this.token.tokenType == 11)
      {
        read();
        checkIsNonCoreReservedIdentifier();
      }
      if (isNonCoreReservedIdentifier())
      {
        ((Expression)localObject).setAlias(HsqlNameManager.getSimpleName(this.token.tokenString, isDelimitedIdentifier()));
        read();
      }
      localQuerySpecification.addSelectColumnExpression((Expression)localObject);
      if ((this.token.tokenType == 124) || (this.token.tokenType == 151)) {
        break;
      }
    } while (readIfThis(824));
    if (((this.token.tokenType == 822) || (this.token.tokenType == 914) || (this.token.tokenType == 841)) && ((this.database.sqlSyntaxMss) || (this.database.sqlSyntaxMys) || (this.database.sqlSyntaxPgs)))
    {
      RangeVariable localRangeVariable = new RangeVariable(this.database.schemaManager.dualTable, null, null, null, this.compileContext);
      localQuerySpecification.addRangeVariable(this.session, localRangeVariable);
      return localQuerySpecification;
    }
    throw unexpectedToken();
    return localQuerySpecification;
  }
  
  void XreadFromClause(QuerySpecification paramQuerySpecification)
  {
    readThis(124);
    do
    {
      XreadTableReference(paramQuerySpecification);
    } while (readIfThis(824));
  }
  
  void XreadTableReference(QuerySpecification paramQuerySpecification)
  {
    int i = 0;
    RangeVariable localRangeVariable1 = readTableOrSubquery();
    paramQuerySpecification.addRangeVariable(this.session, localRangeVariable1);
    for (;;)
    {
      boolean bool1 = false;
      boolean bool2 = false;
      int k = 0;
      int j = this.token.tokenType;
      switch (this.token.tokenType)
      {
      case 186: 
        if (i != 0) {
          throw unexpectedToken();
        }
        read();
        i = 1;
        break;
      case 142: 
        read();
        readThis(154);
        break;
      case 60: 
        if (i != 0) {
          throw unexpectedToken();
        }
        read();
        readThis(154);
        break;
      case 314: 
        if (i != 0) {
          throw unexpectedToken();
        }
        int m = getPosition();
        read();
        if (this.token.tokenType == 154)
        {
          read();
          bool1 = true;
          bool2 = true;
        }
        else
        {
          rewind(m);
          k = 1;
        }
        break;
      case 163: 
        read();
        readIfThis(210);
        readThis(154);
        bool1 = true;
        break;
      case 254: 
        read();
        readIfThis(210);
        readThis(154);
        bool2 = true;
        break;
      case 125: 
        read();
        readIfThis(210);
        readThis(154);
        bool1 = true;
        bool2 = true;
        break;
      case 154: 
        read();
        j = 142;
        break;
      case 824: 
      default: 
        if (i != 0) {
          throw unexpectedToken();
        }
        k = 1;
        if (k != 0) {
          return;
        }
        RangeVariable localRangeVariable2 = readTableOrSubquery();
        Expression localExpression = null;
        localRangeVariable2.setJoinType(bool1, bool2);
        switch (j)
        {
        case 60: 
          paramQuerySpecification.addRangeVariable(this.session, localRangeVariable2);
          break;
        case 314: 
          localExpression = Expression.EXPR_FALSE;
          localRangeVariable2.addJoinCondition(localExpression);
          paramQuerySpecification.addRangeVariable(this.session, localRangeVariable2);
          break;
        case 125: 
        case 142: 
        case 163: 
        case 254: 
          int n = this.token.tokenType == 322 ? 1 : 0;
          if ((i != 0) || (n != 0))
          {
            localRangeVariable1.resolveRangeTable(this.session, RangeGroup.emptyGroup, this.compileContext.getOuterRanges());
            localRangeVariable2.resolveRangeTable(this.session, RangeGroup.emptyGroup, this.compileContext.getOuterRanges());
          }
          OrderedHashSet localOrderedHashSet;
          if (i != 0)
          {
            localOrderedHashSet = localRangeVariable2.getUniqueColumnNameSet();
            localExpression = paramQuerySpecification.getEquiJoinExpressions(localOrderedHashSet, localRangeVariable2, false);
            localRangeVariable2.addJoinCondition(localExpression);
            paramQuerySpecification.addRangeVariable(this.session, localRangeVariable2);
          }
          else if (n != 0)
          {
            read();
            localOrderedHashSet = new OrderedHashSet();
            readThis(836);
            readSimpleColumnNames(localOrderedHashSet, localRangeVariable2, false);
            readThis(822);
            localExpression = paramQuerySpecification.getEquiJoinExpressions(localOrderedHashSet, localRangeVariable2, true);
            localRangeVariable2.addJoinCondition(localExpression);
            paramQuerySpecification.addRangeVariable(this.session, localRangeVariable2);
          }
          else if (this.token.tokenType == 204)
          {
            read();
            localExpression = XreadBooleanValueExpression();
            localRangeVariable2.addJoinCondition(localExpression);
            paramQuerySpecification.addRangeVariable(this.session, localRangeVariable2);
          }
          else
          {
            throw unexpectedToken();
          }
          break;
        }
        i = 0;
      }
    }
  }
  
  Expression getRowExpression(OrderedHashSet paramOrderedHashSet)
  {
    Expression[] arrayOfExpression = new Expression[paramOrderedHashSet.size()];
    for (int i = 0; i < arrayOfExpression.length; i++)
    {
      String str = (String)paramOrderedHashSet.get(i);
      arrayOfExpression[i] = new ExpressionColumn(null, null, str);
    }
    return new Expression(25, arrayOfExpression);
  }
  
  void readWhereGroupHaving(QuerySpecification paramQuerySpecification)
  {
    Expression localExpression;
    if (this.token.tokenType == 333)
    {
      read();
      localExpression = XreadBooleanValueExpression();
      paramQuerySpecification.addQueryCondition(localExpression);
    }
    if (this.token.tokenType == 131)
    {
      read();
      readThis(27);
      for (;;)
      {
        localExpression = XreadValueExpression();
        paramQuerySpecification.addGroupByColumnExpression(localExpression);
        if (this.token.tokenType != 824) {
          break;
        }
        read();
      }
    }
    if (this.token.tokenType == 135)
    {
      read();
      localExpression = XreadBooleanValueExpression();
      paramQuerySpecification.addHavingExpression(localExpression);
    }
  }
  
  SortAndSlice XreadOrderByExpression()
  {
    SortAndSlice localSortAndSlice = null;
    if (this.token.tokenType == 208)
    {
      read();
      readThis(27);
      localSortAndSlice = XreadOrderBy();
    }
    if ((this.token.tokenType == 625) || (this.token.tokenType == 115) || (this.token.tokenType == 202))
    {
      if (localSortAndSlice == null) {
        localSortAndSlice = new SortAndSlice();
      }
      XreadLimit(localSortAndSlice);
    }
    return localSortAndSlice == null ? SortAndSlice.noSort : localSortAndSlice;
  }
  
  private SortAndSlice XreadTopOrLimit()
  {
    Object localObject = null;
    Expression localExpression = null;
    if (this.token.tokenType == 625)
    {
      i = getPosition();
      read();
      localObject = XreadSimpleValueSpecificationOrNull();
      if (localObject == null)
      {
        rewind(i);
        return null;
      }
      readIfThis(824);
      localExpression = XreadSimpleValueSpecificationOrNull();
      if (localExpression == null) {
        throw Error.error(5563, 81);
      }
    }
    else if (this.token.tokenType == 663)
    {
      i = getPosition();
      read();
      localExpression = XreadSimpleValueSpecificationOrNull();
      if (localExpression == null)
      {
        rewind(i);
        return null;
      }
      localObject = new ExpressionValue(ValuePool.INTEGER_0, Type.SQL_INTEGER);
    }
    else
    {
      throw unexpectedToken();
    }
    int i = 1;
    if (((Expression)localObject).isUnresolvedParam()) {
      ((Expression)localObject).setDataType(this.session, Type.SQL_INTEGER);
    } else if (((Expression)localObject).opType == 1) {
      i = (((Expression)localObject).getDataType().typeCode == 4) && (((Integer)((Expression)localObject).getValue(null)).intValue() >= 0) ? 1 : 0;
    } else {
      throw Error.error(5563, 81);
    }
    if (localExpression.isUnresolvedParam()) {
      localExpression.setDataType(this.session, Type.SQL_INTEGER);
    } else if (localExpression.opType == 1) {
      i &= ((localExpression.getDataType().typeCode == 4) && (((Integer)localExpression.getValue(null)).intValue() >= 0) ? 1 : 0);
    } else {
      throw Error.error(5563, 81);
    }
    if (i != 0)
    {
      SortAndSlice localSortAndSlice = new SortAndSlice();
      localSortAndSlice.addLimitCondition(new ExpressionOp(91, (Expression)localObject, localExpression));
      return localSortAndSlice;
    }
    throw Error.error(5563, 81);
  }
  
  private void XreadLimit(SortAndSlice paramSortAndSlice)
  {
    Object localObject1 = null;
    Object localObject2 = null;
    if (this.token.tokenType == 202)
    {
      read();
      localObject1 = XreadSimpleValueSpecificationOrNull();
      if (localObject1 == null) {
        throw Error.error(5563, 81);
      }
      if ((this.token.tokenType == 257) || (this.token.tokenType == 259)) {
        read();
      }
    }
    if (this.token.tokenType == 625)
    {
      read();
      localObject2 = XreadSimpleValueSpecificationOrNull();
      if (localObject2 == null) {
        throw Error.error(5563, 81);
      }
      if (localObject1 == null) {
        if (this.token.tokenType == 824)
        {
          read();
          localObject1 = localObject2;
          localObject2 = XreadSimpleValueSpecificationOrNull();
        }
        else if (this.token.tokenType == 202)
        {
          read();
          localObject1 = XreadSimpleValueSpecificationOrNull();
        }
      }
      if ((this.database.sqlSyntaxPgs) || (this.database.sqlSyntaxMys)) {
        paramSortAndSlice.setZeroLimit();
      }
    }
    else if (this.token.tokenType == 115)
    {
      read();
      if ((this.token.tokenType == 423) || (this.token.tokenType == 470)) {
        read();
      }
      localObject2 = XreadSimpleValueSpecificationOrNull();
      if (localObject2 == null) {
        localObject2 = new ExpressionValue(ValuePool.INTEGER_1, Type.SQL_INTEGER);
      }
      if ((this.token.tokenType == 257) || (this.token.tokenType == 259)) {
        read();
      }
      readThis(205);
      paramSortAndSlice.setStrictLimit();
    }
    if ((paramSortAndSlice.hasOrder()) && (this.token.tokenType == 322))
    {
      read();
      readThis(621);
      paramSortAndSlice.setUsingIndex();
    }
    if (localObject1 == null) {
      localObject1 = new ExpressionValue(ValuePool.INTEGER_0, Type.SQL_INTEGER);
    }
    int i = 1;
    if (((Expression)localObject1).isUnresolvedParam()) {
      ((Expression)localObject1).setDataType(this.session, Type.SQL_INTEGER);
    }
    if ((localObject2 != null) && (((Expression)localObject2).isUnresolvedParam())) {
      ((Expression)localObject2).setDataType(this.session, Type.SQL_INTEGER);
    }
    if (i != 0)
    {
      paramSortAndSlice.addLimitCondition(new ExpressionOp(91, (Expression)localObject1, (Expression)localObject2));
      return;
    }
    throw Error.error(5563, 81);
  }
  
  private SortAndSlice XreadOrderBy()
  {
    SortAndSlice localSortAndSlice = new SortAndSlice();
    for (;;)
    {
      int i = 0;
      boolean bool = false;
      Expression localExpression = XreadValueExpression();
      ExpressionOrderBy localExpressionOrderBy = new ExpressionOrderBy(localExpression);
      if (this.token.tokenType == 410)
      {
        localExpressionOrderBy.setDescending();
        i = 1;
        read();
      }
      else if (this.token.tokenType == 359)
      {
        read();
      }
      if (this.database.sqlNullsOrder) {
        bool = !this.database.sqlNullsFirst;
      } else {
        bool = this.database.sqlNullsFirst == i;
      }
      localExpressionOrderBy.setNullsLast(bool);
      if (this.token.tokenType == 473)
      {
        read();
        if (this.token.tokenType == 423)
        {
          read();
          localExpressionOrderBy.setNullsLast(false);
        }
        else if (this.token.tokenType == 452)
        {
          read();
          localExpressionOrderBy.setNullsLast(true);
        }
        else
        {
          throw unexpectedToken();
        }
      }
      localSortAndSlice.addOrderExpression(localExpressionOrderBy);
      if (this.token.tokenType != 824) {
        break;
      }
      read();
    }
    return localSortAndSlice;
  }
  
  protected RangeVariable readRangeVariableForDataChange(int paramInt)
  {
    Object localObject = readTableName(true);
    HsqlNameManager.SimpleName localSimpleName = null;
    if (paramInt != 1215)
    {
      if (this.token.tokenType == 11)
      {
        read();
        checkIsNonCoreReservedIdentifier();
      }
      if ((isNonCoreReservedIdentifier()) && ((!this.database.sqlSyntaxMys) || (paramInt != 55)))
      {
        localSimpleName = HsqlNameManager.getSimpleName(this.token.tokenString, isDelimitedIdentifier());
        read();
      }
      if ((localSimpleName == null) && (this.lastSynonym != null)) {
        localSimpleName = HsqlNameManager.getSimpleName(this.lastSynonym.name, this.lastSynonym.isNameQuoted);
      }
    }
    if (((Table)localObject).isView)
    {
      switch (paramInt)
      {
      case 56: 
        if (((!((Table)localObject).isTriggerUpdatable()) || (!((Table)localObject).isTriggerInsertable())) && ((((Table)localObject).isTriggerUpdatable()) || (((Table)localObject).isTriggerInsertable()) || (!((Table)localObject).isUpdatable()) || (!((Table)localObject).isInsertable()))) {
          throw Error.error(5545);
        }
        break;
      case 92: 
        if ((!((Table)localObject).isTriggerUpdatable()) && (!((Table)localObject).isUpdatable())) {
          throw Error.error(5545);
        }
        break;
      case 19: 
        if ((!((Table)localObject).isTriggerDeletable()) && (!((Table)localObject).isUpdatable())) {
          throw Error.error(5545);
        }
        break;
      case 55: 
        if ((!((Table)localObject).isTriggerInsertable()) && (!((Table)localObject).isInsertable()) && (!this.session.isProcessingScript())) {
          throw Error.error(5545);
        }
        break;
      case 1215: 
        throw Error.error(5545);
      }
      localObject = ((View)localObject).newDerivedTable(this.session);
    }
    RangeVariable localRangeVariable = new RangeVariable((Table)localObject, localSimpleName, null, null, this.compileContext);
    return localRangeVariable;
  }
  
  protected Table readNamedSubqueryOrNull()
  {
    if (!isSimpleName()) {
      return null;
    }
    TableDerived localTableDerived = this.compileContext.getNamedSubQuery(this.token.tokenString);
    if (localTableDerived == null) {
      return null;
    }
    read();
    if (localTableDerived.isRecompiled()) {
      localTableDerived = localTableDerived.newDerivedTable(this.session);
    } else {
      localTableDerived.canRecompile = true;
    }
    return localTableDerived;
  }
  
  protected RangeVariable readTableOrSubquery()
  {
    Object localObject1 = null;
    HsqlNameManager.SimpleName localSimpleName = null;
    HsqlNameManager.SimpleName[] arrayOfSimpleName = null;
    OrderedHashSet localOrderedHashSet = null;
    int i = 0;
    int j = 0;
    int k = 0;
    Expression localExpression;
    switch (this.token.tokenType)
    {
    case 836: 
      localObject1 = XreadTableSubqueryOrNull(false);
      if (localObject1 == null)
      {
        localObject1 = XreadJoinedTableAsSubqueryOrNull();
        if (localObject1 == null)
        {
          localObject1 = XreadTableSubqueryOrNull(true);
          if (localObject1 == null) {
            throw unexpectedToken();
          }
        }
        else
        {
          i = 1;
        }
      }
      break;
    case 317: 
      localExpression = XreadCollectionDerivedTable(23);
      localObject1 = localExpression.getTable();
      j = 1;
      break;
    case 159: 
      localExpression = XreadLateralDerivedTable();
      localObject1 = localExpression.getTable();
      j = 1;
      break;
    case 294: 
      localExpression = XreadTableFunctionDerivedTable();
      localObject1 = localExpression.getTable();
      j = 1;
      break;
    default: 
      localObject1 = readNamedSubqueryOrNull();
      if (localObject1 == null)
      {
        localObject1 = readTableName(true);
        k = 1;
      }
      if (((Table)localObject1).isView()) {
        localObject1 = ((View)localObject1).newDerivedTable(this.session);
      }
      break;
    }
    int m = 0;
    if (this.token.tokenType == 11)
    {
      read();
      checkIsNonCoreReservedIdentifier();
      m = 1;
    }
    if (isNonCoreReservedIdentifier())
    {
      int n = (this.token.tokenType == 625) || (this.token.tokenType == 202) || (this.token.tokenType == 115) ? 1 : 0;
      int i1 = this.token.tokenType == 637 ? 1 : 0;
      int i2 = getPosition();
      localSimpleName = HsqlNameManager.getSimpleName(this.token.tokenString, isDelimitedIdentifier());
      read();
      if (this.token.tokenType == 836)
      {
        localOrderedHashSet = new OrderedHashSet();
        arrayOfSimpleName = readColumnNameList(localOrderedHashSet);
      }
      else if ((m == 0) && (n != 0))
      {
        if ((this.token.tokenType == 823) || (this.token.tokenType == 838) || (this.token.tokenType == 911))
        {
          localSimpleName = null;
          rewind(i2);
        }
      }
      else if ((m == 0) && (i1 != 0))
      {
        rewind(i2);
      }
    }
    if ((k != 0) && (localSimpleName == null) && (this.lastSynonym != null)) {
      localSimpleName = HsqlNameManager.getSimpleName(this.lastSynonym.name, this.lastSynonym.isNameQuoted);
    }
    if ((this.database.sqlSyntaxMss) && (readIfThis(336))) {
      readNestedParenthesisedTokens();
    }
    Object localObject2;
    if (i != 0) {
      localObject2 = new RangeVariableJoined((Table)localObject1, localSimpleName, localOrderedHashSet, arrayOfSimpleName, this.compileContext);
    } else {
      localObject2 = new RangeVariable((Table)localObject1, localSimpleName, localOrderedHashSet, arrayOfSimpleName, this.compileContext);
    }
    if (j != 0) {
      ((RangeVariable)localObject2).isLateral = true;
    }
    return (RangeVariable)localObject2;
  }
  
  private Expression readAggregate()
  {
    int i = this.token.tokenType;
    read();
    readThis(836);
    Expression localExpression = readAggregateExpression(i);
    readThis(822);
    readFilterClause(localExpression);
    return localExpression;
  }
  
  private void readFilterClause(Expression paramExpression)
  {
    int i = getPosition();
    if (this.token.tokenType == 116)
    {
      read();
      if (this.token.tokenType != 836)
      {
        rewind(i);
        return;
      }
      readThis(836);
      readThis(333);
      Expression localExpression = XreadBooleanValueExpression();
      paramExpression.setCondition(localExpression);
      readThis(822);
    }
  }
  
  private Expression readAggregateExpression(int paramInt)
  {
    int i = getExpressionType(paramInt);
    boolean bool = false;
    int j = 0;
    SortAndSlice localSortAndSlice = null;
    String str = null;
    if (this.token.tokenType == 90)
    {
      bool = true;
      read();
    }
    else if (this.token.tokenType == 2)
    {
      j = 1;
      read();
    }
    int k = getPosition();
    Expression localExpression = XreadValueExpression();
    switch (i)
    {
    case 71: 
      if (localExpression.getType() == 93)
      {
        if (((ExpressionColumn)localExpression).tableName != null) {
          throw unexpectedToken();
        }
        if ((j != 0) || (bool)) {
          throw unexpectedToken();
        }
        localExpression.opType = 11;
      }
      else if (this.token.tokenType == 824)
      {
        rewind(k);
        localExpression = XreadRowElementList(false);
      }
      break;
    case 78: 
    case 79: 
    case 80: 
    case 81: 
      if ((j != 0) || (bool)) {
        throw unexpectedToken(j != 0 ? "ALL" : "DISTINCT");
      }
      break;
    case 82: 
    case 83: 
      if (this.token.tokenType == 208)
      {
        read();
        readThis(27);
        localSortAndSlice = XreadOrderBy();
      }
      if ((i == 83) && (this.token.tokenType == 656))
      {
        read();
        checkIsQuotedString();
        str = (String)this.token.tokenValue;
        read();
      }
      return new ExpressionArrayAggregate(i, bool, localExpression, localSortAndSlice, str);
    case 85: 
      return new ExpressionArrayAggregate(i, bool, localExpression, localSortAndSlice, str);
    case 72: 
    case 73: 
    case 74: 
    case 75: 
    case 76: 
    case 77: 
    case 84: 
    default: 
      if ((localExpression.getType() == 93) || (localExpression.getType() == 11)) {
        throw unexpectedToken("*");
      }
      break;
    }
    ExpressionAggregate localExpressionAggregate = new ExpressionAggregate(i, bool, localExpression);
    return localExpressionAggregate;
  }
  
  Expression XreadValueSpecificationOrNull()
  {
    Object localObject = null;
    int i = 0;
    switch (this.token.tokenType)
    {
    case 837: 
      read();
      break;
    case 834: 
      read();
      i = 1;
      break;
    }
    localObject = XreadUnsignedValueSpecificationOrNull();
    if (localObject == null) {
      return null;
    }
    if (i != 0) {
      localObject = new ExpressionArithmetic(31, (Expression)localObject);
    }
    return (Expression)localObject;
  }
  
  Expression XreadUnsignedValueSpecificationOrNull()
  {
    Object localObject;
    switch (this.token.tokenType)
    {
    case 310: 
      read();
      return Expression.EXPR_TRUE;
    case 114: 
      read();
      return Expression.EXPR_FALSE;
    case 83: 
      if (this.compileContext.contextuallyTypedExpression)
      {
        read();
        localObject = new ExpressionColumn(4);
        return (Expression)localObject;
      }
      break;
    case 196: 
      localObject = new ExpressionValue(null, null);
      read();
      return (Expression)localObject;
    case 911: 
      localObject = new ExpressionValue(this.token.tokenValue, this.token.dataType);
      read();
      return (Expression)localObject;
    case 912: 
    case 913: 
      if (!this.token.isHostParameter) {
        return null;
      }
      return null;
    case 823: 
      read();
      if ((this.token.tokenType != 913) && (this.token.tokenType != 912)) {
        throw unexpectedToken(":");
      }
    case 838: 
      ExpressionColumn localExpressionColumn = new ExpressionColumn(8);
      this.compileContext.addParameter(localExpressionColumn, getPosition());
      read();
      return localExpressionColumn;
    case 379: 
      return XreadCurrentCollationSpec();
    case 64: 
    case 66: 
    case 67: 
    case 68: 
    case 70: 
    case 73: 
    case 74: 
    case 267: 
    case 293: 
    case 321: 
    case 323: 
      FunctionSQL localFunctionSQL = FunctionSQL.newSQLFunction(this.token.tokenString, this.compileContext);
      if (localFunctionSQL == null) {
        return null;
      }
      return readSQLFunction(localFunctionSQL);
    }
    return null;
  }
  
  Expression XreadSimpleValueSpecificationOrNull()
  {
    Object localObject;
    switch (this.token.tokenType)
    {
    case 911: 
      localObject = new ExpressionValue(this.token.tokenValue, this.token.dataType);
      read();
      return (Expression)localObject;
    case 823: 
      read();
      if ((this.token.tokenType != 913) && (this.token.tokenType != 912)) {
        throw unexpectedToken(":");
      }
    case 838: 
      ExpressionColumn localExpressionColumn = new ExpressionColumn(8);
      this.compileContext.addParameter(localExpressionColumn, getPosition());
      read();
      return localExpressionColumn;
    case 912: 
    case 913: 
      checkValidCatalogName(this.token.namePrePrePrefix);
      localObject = new ExpressionColumn(this.token.namePrePrefix, this.token.namePrefix, this.token.tokenString);
      read();
      return (Expression)localObject;
    }
    return null;
  }
  
  Expression XreadAllTypesValueExpressionPrimary(boolean paramBoolean)
  {
    Expression localExpression = null;
    int i = 0;
    int j = 0;
    switch (this.token.tokenType)
    {
    case 109: 
    case 315: 
      if (paramBoolean) {
        return XreadPredicate();
      }
      break;
    case 220: 
      j = 1;
    case 257: 
      if (!paramBoolean)
      {
        read();
        readThis(836);
        localExpression = XreadRowElementList(true);
        readThis(822);
        if (j != 0) {
          localExpression.setSubType(57);
        }
      }
      break;
    default: 
      localExpression = XreadSimpleValueExpressionPrimary();
      if (localExpression != null) {
        localExpression = XreadArrayElementReference(localExpression);
      }
      break;
    }
    if (localExpression == null)
    {
      if (this.token.tokenType == 257)
      {
        read();
        checkIsThis(836);
        i = 1;
      }
      else if (this.token.tokenType == 220)
      {
        read();
        checkIsThis(836);
        j = 1;
      }
      if (this.token.tokenType == 836)
      {
        read();
        localExpression = XreadRowElementList(true);
        readThis(822);
        if (j != 0) {
          localExpression.setSubType(57);
        }
      }
    }
    if ((paramBoolean) && (localExpression != null)) {
      localExpression = XreadPredicateRightPart(localExpression);
    }
    return localExpression;
  }
  
  Expression XreadValueExpressionPrimary()
  {
    Expression localExpression = XreadSimpleValueExpressionPrimary();
    if (localExpression != null)
    {
      localExpression = XreadArrayElementReference(localExpression);
      return localExpression;
    }
    if (this.token.tokenType == 836)
    {
      read();
      localExpression = XreadValueExpression();
      readThis(822);
    }
    else
    {
      return null;
    }
    return localExpression;
  }
  
  Expression XreadSimpleValueExpressionPrimary()
  {
    Object localObject1 = XreadUnsignedValueSpecificationOrNull();
    if (localObject1 != null) {
      return (Expression)localObject1;
    }
    int i = getPosition();
    Object localObject2;
    switch (this.token.tokenType)
    {
    case 836: 
      read();
      int j = getPosition();
      readOpenBrackets();
      switch (this.token.tokenType)
      {
      case 265: 
      case 294: 
      case 324: 
        localObject2 = null;
        rewind(j);
        try
        {
          localObject2 = XreadSubqueryTableBody(21);
          readThis(822);
        }
        catch (HsqlException localHsqlException)
        {
          localHsqlException.setLevel(this.compileContext.subqueryDepth);
          if ((this.lastError == null) || (this.lastError.getLevel() < localHsqlException.getLevel())) {
            this.lastError = localHsqlException;
          }
          rewind(i);
          return null;
        }
        if (((TableDerived)localObject2).queryExpression != null) {
          if (((TableDerived)localObject2).queryExpression.isSingleColumn()) {
            localObject1 = new Expression(21, (TableDerived)localObject2);
          } else {
            localObject1 = new Expression(22, (TableDerived)localObject2);
          }
        }
        if (localObject1 != null) {
          return (Expression)localObject1;
        }
        break;
      }
      rewind(i);
      return null;
    case 821: 
      localObject1 = new ExpressionColumn(this.token.namePrePrefix, this.token.namePrefix);
      getRecordedToken().setExpression(localObject1);
      read();
      return (Expression)localObject1;
    case 730: 
      localObject1 = readLeastExpressionOrNull();
      if (localObject1 != null) {
        return (Expression)localObject1;
      }
      break;
    case 718: 
      localObject1 = readGreatestExpressionOrNull();
      if (localObject1 != null) {
        return (Expression)localObject1;
      }
      break;
    case 712: 
      localObject1 = readDecodeExpressionOrNull();
      if (localObject1 != null) {
        return (Expression)localObject1;
      }
      break;
    case 687: 
      localObject1 = readConcatExpressionOrNull();
      if (localObject1 != null) {
        return (Expression)localObject1;
      }
      break;
    case 688: 
      localObject1 = readConcatSeparatorExpressionOrNull();
      if (localObject1 != null) {
        return (Expression)localObject1;
      }
      break;
    case 594: 
      localObject1 = readCaseWhenExpressionOrNull();
      if (localObject1 != null) {
        return (Expression)localObject1;
      }
      break;
    case 32: 
      return readCaseExpression();
    case 197: 
      return readNullIfExpression();
    case 43: 
      return readCoalesceExpression();
    case 720: 
    case 727: 
      localObject1 = readIfNullExpressionOrNull();
      if (localObject1 != null) {
        return (Expression)localObject1;
      }
      break;
    case 641: 
      localObject1 = readIfNull2ExpressionOrNull();
      if (localObject1 != null) {
        return (Expression)localObject1;
      }
      break;
    case 33: 
    case 53: 
      localObject1 = readCastExpressionOrNull();
      if (localObject1 != null) {
        return (Expression)localObject1;
      }
      break;
    case 77: 
    case 150: 
    case 297: 
    case 298: 
      localObject1 = readDateTimeIntervalLiteral(this.session);
      if (localObject1 != null) {
        return (Expression)localObject1;
      }
      break;
    case 8: 
      return readCollection(19);
    case 6: 
    case 9: 
    case 17: 
    case 56: 
    case 105: 
    case 173: 
    case 178: 
    case 272: 
    case 283: 
    case 284: 
    case 289: 
    case 325: 
    case 326: 
    case 634: 
    case 719: 
      return readAggregate();
    case 470: 
      localObject1 = readSequenceExpressionOrNull(12);
      if (localObject1 != null) {
        return (Expression)localObject1;
      }
      break;
    case 63: 
    case 646: 
      localObject1 = readSequenceExpressionOrNull(13);
      if (localObject1 != null) {
        return (Expression)localObject1;
      }
      break;
    case 604: 
      if (this.database.sqlSyntaxPgs)
      {
        read();
        readThis(836);
        localObject2 = readQuotedString();
        Scanner localScanner = this.session.getScanner();
        localScanner.reset(this.session, (String)localObject2);
        localScanner.scanNext();
        String str = this.session.getSchemaName(localScanner.token.namePrefix);
        NumberSequence localNumberSequence = this.database.schemaManager.getSequence(localScanner.token.tokenString, str, true);
        localObject1 = new ExpressionColumn(localNumberSequence, 13);
        readThis(822);
        return (Expression)localObject1;
      }
      break;
    case 624: 
      if (this.database.sqlSyntaxPgs)
      {
        read();
        readThis(836);
        readThis(822);
        return FunctionCustom.newCustomFunction("IDENTITY", 138);
      }
      break;
    case 639: 
      if (this.database.sqlSyntaxPgs) {
        return readNextvalFunction();
      }
      if (this.database.sqlSyntaxDb2)
      {
        localObject1 = readSequenceExpressionOrNull(12);
        if (localObject1 != null) {
          return (Expression)localObject1;
        }
      }
      break;
    case 258: 
      read();
      if (this.token.tokenType == 836)
      {
        read();
        readThis(822);
        readThis(211);
        readThis(836);
        readThis(822);
      }
      else
      {
        rewind(i);
        break;
      }
      return new ExpressionColumn(14);
    case 654: 
      read();
      if (this.token.tokenType == 836)
      {
        read();
        if (this.token.tokenType == 822)
        {
          read();
        }
        else
        {
          rewind(i);
          break;
        }
      }
      else if ((!this.database.sqlSyntaxOra) && (!this.database.sqlSyntaxDb2))
      {
        rewind(i);
        break;
      }
      return new ExpressionColumn(14);
    case 163: 
    case 254: 
      break;
    case 294: 
      read();
      readThis(836);
      localObject2 = XreadSubqueryTableBody(23);
      readThis(822);
      return new Expression(23, (TableDerived)localObject2);
    default: 
      if (isCoreReservedKey()) {
        throw unexpectedToken();
      }
      break;
    }
    localObject1 = readColumnOrFunctionExpression();
    if (((Expression)localObject1).isAggregate()) {
      readFilterClause((Expression)localObject1);
    }
    return (Expression)localObject1;
  }
  
  Expression readNextvalFunction()
  {
    read();
    readThis(836);
    String str1 = readQuotedString();
    Scanner localScanner = this.session.getScanner();
    localScanner.reset(this.session, str1);
    localScanner.scanNext();
    String str2 = this.session.getSchemaName(localScanner.token.namePrefix);
    NumberSequence localNumberSequence = this.database.schemaManager.getSequence(localScanner.token.tokenString, str2, true);
    ExpressionColumn localExpressionColumn = new ExpressionColumn(localNumberSequence, 12);
    readThis(822);
    return localExpressionColumn;
  }
  
  Expression XreadAllTypesPrimary(boolean paramBoolean)
  {
    Expression localExpression = null;
    switch (this.token.tokenType)
    {
    case 1: 
    case 30: 
    case 34: 
    case 35: 
    case 37: 
    case 39: 
    case 111: 
    case 113: 
    case 119: 
    case 166: 
    case 171: 
    case 180: 
    case 192: 
    case 199: 
    case 200: 
    case 213: 
    case 222: 
    case 223: 
    case 224: 
    case 279: 
    case 286: 
    case 287: 
    case 304: 
    case 308: 
    case 320: 
    case 334: 
      FunctionSQL localFunctionSQL = FunctionSQL.newSQLFunction(this.token.tokenString, this.compileContext);
      if (localFunctionSQL == null) {
        throw unsupportedFeature();
      }
      localExpression = readSQLFunction(localFunctionSQL);
      if (localExpression != null) {
        break;
      }
    default: 
      localExpression = XreadAllTypesValueExpressionPrimary(paramBoolean);
    }
    localExpression = XreadModifier(localExpression);
    return localExpression;
  }
  
  Expression XreadModifier(Expression paramExpression)
  {
    Object localObject;
    switch (this.token.tokenType)
    {
    case 14: 
      read();
      localObject = null;
      if (this.token.tokenType == 167)
      {
        read();
      }
      else
      {
        readThis(297);
        readThis(571);
        localObject = XreadValueExpressionPrimary();
        switch (this.token.tokenType)
        {
        case 78: 
        case 137: 
        case 179: 
        case 183: 
        case 264: 
        case 340: 
          IntervalType localIntervalType = readIntervalType(false);
          if (((Expression)localObject).getType() == 33) {
            ((Expression)localObject).dataType = localIntervalType;
          } else {
            localObject = new ExpressionOp((Expression)localObject, localIntervalType);
          }
          break;
        }
      }
      paramExpression = new ExpressionOp(88, paramExpression, (Expression)localObject);
      break;
    case 78: 
    case 137: 
    case 179: 
    case 183: 
    case 264: 
    case 340: 
      localObject = readIntervalType(true);
      if (paramExpression.getType() == 33) {
        paramExpression.dataType = ((Type)localObject);
      } else {
        paramExpression = new ExpressionOp(paramExpression, (Type)localObject);
      }
      break;
    case 44: 
      read();
      localObject = this.database.schemaManager.getCollation(this.session, this.token.tokenString, this.token.namePrefix);
      paramExpression.setCollation((Collation)localObject);
      read();
    }
    return paramExpression;
  }
  
  Expression XreadValueExpressionWithContext()
  {
    this.compileContext.contextuallyTypedExpression = true;
    Expression localExpression = XreadValueExpressionOrNull();
    this.compileContext.contextuallyTypedExpression = false;
    return localExpression;
  }
  
  Expression XreadValueExpressionOrNull()
  {
    Expression localExpression = XreadAllTypesCommonValueExpression(true);
    if (localExpression == null) {
      return null;
    }
    return localExpression;
  }
  
  Expression XreadValueExpression()
  {
    Object localObject = XreadAllTypesCommonValueExpression(true);
    if (this.token.tokenType == 831)
    {
      read();
      Expression localExpression = XreadNumericValueExpression();
      readThis(840);
      localObject = new ExpressionAccessor((Expression)localObject, localExpression);
    }
    return (Expression)localObject;
  }
  
  Expression XreadRowOrCommonValueExpression()
  {
    return XreadAllTypesCommonValueExpression(false);
  }
  
  Expression XreadAllTypesCommonValueExpression(boolean paramBoolean)
  {
    Object localObject1 = XreadAllTypesTerm(paramBoolean);
    int i = 0;
    int j = 0;
    for (;;)
    {
      switch (this.token.tokenType)
      {
      case 837: 
        i = 32;
        paramBoolean = false;
        break;
      case 834: 
        i = 33;
        paramBoolean = false;
        break;
      case 825: 
        i = 36;
        paramBoolean = false;
        break;
      case 207: 
        if (paramBoolean) {
          i = 50;
        }
        break;
      }
      j = 1;
      if (j != 0) {
        break;
      }
      read();
      Object localObject2 = localObject1;
      localObject1 = XreadAllTypesTerm(paramBoolean);
      localObject1 = paramBoolean ? new ExpressionLogical(i, (Expression)localObject2, (Expression)localObject1) : new ExpressionArithmetic(i, (Expression)localObject2, (Expression)localObject1);
    }
    return (Expression)localObject1;
  }
  
  Expression XreadAllTypesTerm(boolean paramBoolean)
  {
    Object localObject1 = XreadAllTypesFactor(paramBoolean);
    int i = 0;
    int j = 0;
    for (;;)
    {
      switch (this.token.tokenType)
      {
      case 821: 
        i = 34;
        paramBoolean = false;
        break;
      case 826: 
        i = 35;
        paramBoolean = false;
        break;
      case 5: 
        if (paramBoolean) {
          i = 49;
        }
        break;
      }
      j = 1;
      if (j != 0) {
        break;
      }
      read();
      Object localObject2 = localObject1;
      localObject1 = XreadAllTypesFactor(paramBoolean);
      if (localObject1 == null) {
        throw unexpectedToken();
      }
      localObject1 = paramBoolean ? new ExpressionLogical(i, (Expression)localObject2, (Expression)localObject1) : new ExpressionArithmetic(i, (Expression)localObject2, (Expression)localObject1);
    }
    return (Expression)localObject1;
  }
  
  Expression XreadAllTypesFactor(boolean paramBoolean)
  {
    int i = 0;
    int j = 0;
    int k = 0;
    switch (this.token.tokenType)
    {
    case 837: 
      read();
      paramBoolean = false;
      break;
    case 834: 
      read();
      paramBoolean = false;
      i = 1;
      break;
    case 193: 
      if (paramBoolean)
      {
        read();
        j = 1;
      }
      break;
    }
    Object localObject = XreadAllTypesPrimary(paramBoolean);
    if ((paramBoolean) && (this.token.tokenType == 152))
    {
      read();
      if (this.token.tokenType == 193)
      {
        read();
        j = j == 0 ? 1 : 0;
      }
      if (this.token.tokenType == 310)
      {
        read();
      }
      else if (this.token.tokenType == 114)
      {
        read();
        j = j == 0 ? 1 : 0;
      }
      else if (this.token.tokenType == 316)
      {
        read();
        k = 1;
      }
      else
      {
        throw unexpectedToken();
      }
    }
    if (k != 0) {
      localObject = new ExpressionLogical(47, (Expression)localObject);
    } else if (i != 0) {
      localObject = new ExpressionArithmetic(31, (Expression)localObject);
    } else if (j != 0) {
      localObject = new ExpressionLogical(48, (Expression)localObject);
    }
    return (Expression)localObject;
  }
  
  Expression XreadStringValueExpression()
  {
    return XreadCharacterValueExpression();
  }
  
  Expression XreadCharacterValueExpression()
  {
    Object localObject1 = XreadCharacterPrimary();
    Collation localCollation = readCollateClauseOrNull();
    while (this.token.tokenType == 825)
    {
      read();
      Object localObject2 = localObject1;
      localObject1 = XreadCharacterPrimary();
      localCollation = readCollateClauseOrNull();
      localObject1 = new ExpressionArithmetic(36, (Expression)localObject2, (Expression)localObject1);
    }
    return (Expression)localObject1;
  }
  
  Expression XreadCharacterPrimary()
  {
    switch (this.token.tokenType)
    {
    case 171: 
    case 213: 
    case 286: 
    case 308: 
    case 320: 
      FunctionSQL localFunctionSQL = FunctionSQL.newSQLFunction(this.token.tokenString, this.compileContext);
      Expression localExpression = readSQLFunction(localFunctionSQL);
      if (localExpression != null) {
        return localExpression;
      }
      break;
    }
    return XreadValueExpressionPrimary();
  }
  
  Expression XreadNumericPrimary()
  {
    switch (this.token.tokenType)
    {
    case 1: 
    case 30: 
    case 34: 
    case 35: 
    case 37: 
    case 39: 
    case 111: 
    case 113: 
    case 119: 
    case 166: 
    case 180: 
    case 200: 
    case 222: 
    case 224: 
    case 279: 
    case 334: 
      FunctionSQL localFunctionSQL = FunctionSQL.newSQLFunction(this.token.tokenString, this.compileContext);
      if (localFunctionSQL == null) {
        throw unexpectedToken();
      }
      Expression localExpression = readSQLFunction(localFunctionSQL);
      if (localExpression != null) {
        return localExpression;
      }
      break;
    }
    return XreadValueExpressionPrimary();
  }
  
  Expression XreadNumericValueExpression()
  {
    int i;
    Object localObject2;
    for (Object localObject1 = XreadTerm();; localObject1 = new ExpressionArithmetic(i, (Expression)localObject2, (Expression)localObject1))
    {
      if (this.token.tokenType == 837)
      {
        i = 32;
      }
      else
      {
        if (this.token.tokenType != 834) {
          break;
        }
        i = 33;
      }
      read();
      localObject2 = localObject1;
      localObject1 = XreadTerm();
    }
    return (Expression)localObject1;
  }
  
  Expression XreadTerm()
  {
    int i;
    Object localObject2;
    for (Object localObject1 = XreadFactor();; localObject1 = new ExpressionArithmetic(i, (Expression)localObject2, (Expression)localObject1))
    {
      if (this.token.tokenType == 821)
      {
        i = 34;
      }
      else
      {
        if (this.token.tokenType != 826) {
          break;
        }
        i = 35;
      }
      read();
      localObject2 = localObject1;
      localObject1 = XreadFactor();
      if (localObject1 == null) {
        throw unexpectedToken();
      }
    }
    return (Expression)localObject1;
  }
  
  Expression XreadFactor()
  {
    int i = 0;
    if (this.token.tokenType == 837)
    {
      read();
    }
    else if (this.token.tokenType == 834)
    {
      read();
      i = 1;
    }
    Object localObject = XreadNumericPrimary();
    if (localObject == null) {
      return null;
    }
    if (i != 0) {
      localObject = new ExpressionArithmetic(31, (Expression)localObject);
    }
    return (Expression)localObject;
  }
  
  Expression XreadDatetimeValueExpression()
  {
    int i;
    Object localObject2;
    for (Object localObject1 = XreadDateTimeIntervalTerm();; localObject1 = new ExpressionArithmetic(i, (Expression)localObject2, (Expression)localObject1))
    {
      if (this.token.tokenType == 837)
      {
        i = 32;
      }
      else
      {
        if (this.token.tokenType != 834) {
          break;
        }
        i = 33;
      }
      read();
      localObject2 = localObject1;
      localObject1 = XreadDateTimeIntervalTerm();
    }
    return (Expression)localObject1;
  }
  
  Expression XreadIntervalValueExpression()
  {
    int i;
    Object localObject2;
    for (Object localObject1 = XreadDateTimeIntervalTerm();; localObject1 = new ExpressionArithmetic(i, (Expression)localObject2, (Expression)localObject1))
    {
      if (this.token.tokenType == 837)
      {
        i = 32;
      }
      else
      {
        if (this.token.tokenType != 834) {
          break;
        }
        i = 33;
      }
      read();
      localObject2 = localObject1;
      localObject1 = XreadDateTimeIntervalTerm();
    }
    return (Expression)localObject1;
  }
  
  Expression XreadDateTimeIntervalTerm()
  {
    switch (this.token.tokenType)
    {
    case 1: 
    case 65: 
    case 71: 
    case 72: 
    case 168: 
    case 169: 
      FunctionSQL localFunctionSQL = FunctionSQL.newSQLFunction(this.token.tokenString, this.compileContext);
      if (localFunctionSQL == null) {
        throw unexpectedToken();
      }
      return readSQLFunction(localFunctionSQL);
    }
    return XreadValueExpressionPrimary();
  }
  
  Expression XreadDateTimeValueFunctionOrNull()
  {
    FunctionSQL localFunctionSQL = null;
    switch (this.token.tokenType)
    {
    case 65: 
    case 71: 
    case 72: 
    case 168: 
    case 169: 
      localFunctionSQL = FunctionSQL.newSQLFunction(this.token.tokenString, this.compileContext);
      break;
    case 781: 
      if (!this.database.sqlSyntaxOra) {
        return null;
      }
    case 747: 
    case 780: 
    case 794: 
      localFunctionSQL = FunctionCustom.newCustomFunction(this.token.tokenString, this.token.tokenType);
      break;
    }
    return null;
    if (localFunctionSQL == null) {
      throw unexpectedToken();
    }
    return readSQLFunction(localFunctionSQL);
  }
  
  Expression XreadBooleanValueExpression()
  {
    try
    {
      Object localObject1 = XreadBooleanTermOrNull();
      if (localObject1 == null) {
        throw Error.error(5568);
      }
      while (this.token.tokenType == 207)
      {
        int i = 50;
        read();
        Object localObject2 = localObject1;
        localObject1 = XreadBooleanTermOrNull();
        if (localObject1 == null) {
          throw Error.error(5568);
        }
        localObject1 = new ExpressionLogical(i, (Expression)localObject2, (Expression)localObject1);
      }
      if (localObject1 == null) {
        throw Error.error(5568);
      }
      return (Expression)localObject1;
    }
    catch (HsqlException localHsqlException1)
    {
      localHsqlException1.setLevel(this.compileContext.subqueryDepth);
      HsqlException localHsqlException2;
      if ((this.lastError != null) && (this.lastError.getLevel() >= localHsqlException1.getLevel()))
      {
        localHsqlException2 = this.lastError;
        this.lastError = null;
      }
      throw localHsqlException2;
    }
  }
  
  Expression XreadBooleanTermOrNull()
  {
    Object localObject1 = XreadBooleanFactorOrNull();
    if (localObject1 == null) {
      return null;
    }
    while (this.token.tokenType == 5)
    {
      int i = 49;
      read();
      Object localObject2 = localObject1;
      localObject1 = XreadBooleanFactorOrNull();
      if (localObject1 == null) {
        throw unexpectedToken();
      }
      localObject1 = new ExpressionLogical(i, (Expression)localObject2, (Expression)localObject1);
    }
    return (Expression)localObject1;
  }
  
  Expression XreadBooleanFactorOrNull()
  {
    int i = 0;
    if (this.token.tokenType == 193)
    {
      read();
      i = 1;
    }
    Object localObject = XreadBooleanTestOrNull();
    if (localObject == null) {
      return null;
    }
    if (i != 0) {
      localObject = new ExpressionLogical(48, (Expression)localObject);
    }
    return (Expression)localObject;
  }
  
  Expression XreadBooleanTestOrNull()
  {
    int i = 0;
    int j = 0;
    Object localObject = XreadBooleanPrimaryOrNull();
    if (localObject == null) {
      return (Expression)localObject;
    }
    if (this.token.tokenType == 152)
    {
      read();
      if (this.token.tokenType == 193)
      {
        read();
        j = 1;
      }
      if (this.token.tokenType == 310)
      {
        read();
      }
      else if (this.token.tokenType == 114)
      {
        read();
        j = j == 0 ? 1 : 0;
      }
      else if (this.token.tokenType == 316)
      {
        read();
        i = 1;
      }
      else
      {
        throw unexpectedToken();
      }
    }
    if (i != 0) {
      localObject = new ExpressionLogical(47, (Expression)localObject);
    }
    if (j != 0) {
      localObject = new ExpressionLogical(48, (Expression)localObject);
    }
    return (Expression)localObject;
  }
  
  Expression XreadBooleanPrimaryOrNull()
  {
    Expression localExpression = null;
    int i;
    switch (this.token.tokenType)
    {
    case 109: 
    case 315: 
      return XreadPredicate();
    case 257: 
      read();
      readThis(836);
      localExpression = XreadRowElementList(true);
      readThis(822);
      break;
    default: 
      i = getPosition();
      try
      {
        localExpression = XreadAllTypesCommonValueExpression(false);
      }
      catch (HsqlException localHsqlException1)
      {
        localHsqlException1.setLevel(this.compileContext.subqueryDepth);
        if ((this.lastError == null) || (this.lastError.getLevel() < localHsqlException1.getLevel())) {
          this.lastError = localHsqlException1;
        }
        rewind(i);
      }
    }
    if ((localExpression == null) && (this.token.tokenType == 836))
    {
      read();
      i = getPosition();
      try
      {
        localExpression = XreadRowElementList(true);
        readThis(822);
      }
      catch (HsqlException localHsqlException2)
      {
        localHsqlException2.setLevel(this.compileContext.subqueryDepth);
        if ((this.lastError == null) || (this.lastError.getLevel() < localHsqlException2.getLevel())) {
          this.lastError = localHsqlException2;
        }
        rewind(i);
        localExpression = XreadBooleanValueExpression();
        readThis(822);
      }
    }
    if (localExpression != null) {
      localExpression = XreadPredicateRightPart(localExpression);
    }
    return localExpression;
  }
  
  Expression XreadBooleanPredicand()
  {
    if (this.token.tokenType == 836)
    {
      read();
      localExpression = XreadBooleanValueExpression();
      readThis(822);
      return localExpression;
    }
    Expression localExpression = XreadSimpleValueExpressionPrimary();
    if (localExpression != null) {
      localExpression = XreadArrayElementReference(localExpression);
    }
    return localExpression;
  }
  
  Expression XreadPredicate()
  {
    switch (this.token.tokenType)
    {
    case 109: 
      read();
      localExpression = XreadTableSubquery(55);
      return new ExpressionLogical(55, localExpression);
    case 315: 
      read();
      localExpression = XreadTableSubquery(63);
      return new ExpressionLogical(63, localExpression);
    }
    Expression localExpression = XreadRowValuePredicand();
    return XreadPredicateRightPart(localExpression);
  }
  
  Expression XreadPredicateRightPart(Expression paramExpression)
  {
    int i = 0;
    int j = 0;
    ExpressionLogical localExpressionLogical = null;
    int k = getPosition();
    if (this.token.tokenType == 193)
    {
      read();
      i = 1;
    }
    if (this.token.tokenType == 619)
    {
      read();
      if ((this.token.tokenType != 225) && (this.token.tokenType != 288)) {
        throw unexpectedToken();
      }
      j = 1;
    }
    switch (this.token.tokenType)
    {
    case 152: 
      if (i != 0) {
        throw unexpectedToken();
      }
      read();
      if (this.token.tokenType == 193)
      {
        i = 1;
        read();
      }
      if (this.token.tokenType == 196)
      {
        read();
        if (i != 0)
        {
          localExpressionLogical = new ExpressionLogical(39, paramExpression);
          i = 0;
        }
        else
        {
          localExpressionLogical = new ExpressionLogical(47, paramExpression);
        }
      }
      else if (this.token.tokenType == 90)
      {
        read();
        readThis(124);
        Expression localExpression1 = XreadRowValuePredicand();
        localExpressionLogical = new ExpressionLogical(64, paramExpression, localExpression1);
        i = i == 0 ? 1 : 0;
      }
      else
      {
        rewind(k);
        return paramExpression;
      }
      break;
    case 164: 
      localExpressionLogical = XreadLikePredicateRightPart(paramExpression);
      localExpressionLogical.noOptimisation = this.isCheckOrTriggerCondition;
      break;
    case 21: 
      localExpressionLogical = XreadBetweenPredicateRightPart(paramExpression);
      break;
    case 140: 
      localExpressionLogical = XreadInPredicateRightPart(paramExpression);
      localExpressionLogical.noOptimisation = this.isCheckOrTriggerCondition;
      break;
    case 52: 
      if (i != 0) {
        throw unexpectedToken();
      }
      localExpressionLogical = XreadPeriodPredicateRightPart(56, paramExpression);
      break;
    case 103: 
      if (i != 0) {
        throw unexpectedToken();
      }
      localExpressionLogical = XreadPeriodPredicateRightPart(57, paramExpression);
      break;
    case 212: 
      if (i != 0) {
        throw unexpectedToken();
      }
      localExpressionLogical = XreadPeriodPredicateRightPart(58, paramExpression);
      break;
    case 225: 
      if (i != 0) {
        throw unexpectedToken();
      }
      if (j != 0) {
        localExpressionLogical = XreadPeriodPredicateRightPart(61, paramExpression);
      } else {
        localExpressionLogical = XreadPeriodPredicateRightPart(59, paramExpression);
      }
      break;
    case 288: 
      if (i != 0) {
        throw unexpectedToken();
      }
      if (j != 0) {
        localExpressionLogical = XreadPeriodPredicateRightPart(62, paramExpression);
      } else {
        localExpressionLogical = XreadPeriodPredicateRightPart(60, paramExpression);
      }
      break;
    case 417: 
    case 829: 
    case 830: 
    case 832: 
    case 833: 
    case 835: 
      if (i != 0) {
        throw unexpectedToken();
      }
      int m = getExpressionType(this.token.tokenType);
      read();
      switch (this.token.tokenType)
      {
      case 2: 
      case 6: 
      case 272: 
        localExpressionLogical = XreadQuantifiedComparisonRightPart(m, paramExpression);
        break;
      default: 
        Expression localExpression2 = XreadRowValuePredicand();
        localExpressionLogical = new ExpressionLogical(m, paramExpression, localExpression2);
      }
      break;
    case 172: 
      localExpressionLogical = XreadMatchPredicateRightPart(paramExpression);
      break;
    default: 
      if (i != 0) {
        throw unexpectedToken();
      }
      return paramExpression;
    }
    if (i != 0) {
      localExpressionLogical = new ExpressionLogical(48, localExpressionLogical);
    }
    return localExpressionLogical;
  }
  
  private ExpressionLogical XreadBetweenPredicateRightPart(Expression paramExpression)
  {
    int i = 0;
    read();
    if (this.token.tokenType == 13)
    {
      read();
    }
    else if (this.token.tokenType == 290)
    {
      i = 1;
      read();
    }
    Expression localExpression1 = XreadRowValuePredicand();
    readThis(5);
    Expression localExpression2 = XreadRowValuePredicand();
    ExpressionLogical localExpressionLogical1 = new ExpressionLogical(41, paramExpression, localExpression1);
    ExpressionLogical localExpressionLogical2 = new ExpressionLogical(45, paramExpression, localExpression2);
    ExpressionLogical localExpressionLogical3 = new ExpressionLogical(49, localExpressionLogical1, localExpressionLogical2);
    if (i != 0)
    {
      localExpressionLogical1 = new ExpressionLogical(45, paramExpression, localExpression1);
      localExpressionLogical2 = new ExpressionLogical(41, paramExpression, localExpression2);
      ExpressionLogical localExpressionLogical4 = new ExpressionLogical(49, localExpressionLogical1, localExpressionLogical2);
      return new ExpressionLogical(50, localExpressionLogical3, localExpressionLogical4);
    }
    return localExpressionLogical3;
  }
  
  private ExpressionLogical XreadQuantifiedComparisonRightPart(int paramInt, Expression paramExpression)
  {
    int i = this.token.tokenType;
    int j = 0;
    switch (this.token.tokenType)
    {
    case 6: 
    case 272: 
      j = 52;
      break;
    case 2: 
      j = 51;
      break;
    default: 
      throw Error.runtimeError(201, "ParserDQL");
    }
    read();
    readThis(836);
    int k = getPosition();
    readOpenBrackets();
    Expression localExpression;
    switch (this.token.tokenType)
    {
    case 265: 
    case 294: 
    case 324: 
    case 336: 
      rewind(k);
      localObject = XreadSubqueryTableBody(54);
      localExpression = new Expression(23, (TableDerived)localObject);
      readThis(822);
      break;
    default: 
      rewind(k);
      localExpression = readAggregateExpression(i);
      readThis(822);
      readFilterClause(localExpression);
    }
    Object localObject = new ExpressionLogical(paramInt, paramExpression, localExpression);
    ((ExpressionLogical)localObject).setSubType(j);
    return (ExpressionLogical)localObject;
  }
  
  private ExpressionLogical XreadInPredicateRightPart(Expression paramExpression)
  {
    int i = paramExpression.getDegree();
    Expression localExpression = null;
    read();
    readThis(836);
    int j = getPosition();
    int k = readOpenBrackets();
    Object localObject;
    switch (this.token.tokenType)
    {
    case 317: 
      localExpression = XreadCollectionDerivedTable(54);
      readThis(822);
      readCloseBrackets(k);
      break;
    case 265: 
    case 294: 
    case 324: 
    case 336: 
      rewind(j);
      localObject = XreadSubqueryTableBody(54);
      localExpression = new Expression(23, (TableDerived)localObject);
      readThis(822);
      break;
    default: 
      rewind(j);
      localExpression = XreadInValueListConstructor(i);
      readThis(822);
    }
    if (this.isCheckOrTriggerCondition)
    {
      localObject = new ExpressionLogical(54, paramExpression, localExpression);
    }
    else
    {
      localObject = new ExpressionLogical(40, paramExpression, localExpression);
      ((ExpressionLogical)localObject).setSubType(52);
    }
    return (ExpressionLogical)localObject;
  }
  
  Expression XreadInValueList(int paramInt)
  {
    HsqlArrayList localHsqlArrayList = new HsqlArrayList();
    for (;;)
    {
      localObject = XreadValueExpression();
      if (((Expression)localObject).getType() != 25) {
        localObject = new Expression(25, new Expression[] { localObject });
      }
      localHsqlArrayList.add(localObject);
      if (this.token.tokenType != 824) {
        break;
      }
      read();
    }
    Object localObject = new Expression[localHsqlArrayList.size()];
    localHsqlArrayList.toArray(localObject);
    Expression localExpression = new Expression(26, (Expression[])localObject);
    for (int i = 0; i < localObject.length; i++)
    {
      if (localObject[i].getType() != 25) {
        localObject[i] = new Expression(25, new Expression[] { localObject[i] });
      }
      Expression[] arrayOfExpression = localObject[i].nodes;
      if (arrayOfExpression.length != paramInt) {
        throw unexpectedToken();
      }
      for (int j = 0; j < paramInt; j++) {
        if (arrayOfExpression[j].getType() == 25) {
          throw unexpectedToken();
        }
      }
    }
    return localExpression;
  }
  
  private ExpressionLogical XreadLikePredicateRightPart(Expression paramExpression)
  {
    read();
    Expression localExpression1 = XreadStringValueExpression();
    Expression localExpression2 = null;
    if (this.token.tokenString.equals("ESCAPE"))
    {
      read();
      localExpression2 = XreadStringValueExpression();
    }
    return new ExpressionLike(paramExpression, localExpression1, localExpression2, this.isCheckOrTriggerCondition);
  }
  
  private ExpressionLogical XreadMatchPredicateRightPart(Expression paramExpression)
  {
    int i = 0;
    int j = 65;
    read();
    if (this.token.tokenType == 315)
    {
      read();
      i = 1;
    }
    if (this.token.tokenType == 533)
    {
      read();
      j = i != 0 ? 68 : 65;
    }
    else if (this.token.tokenType == 492)
    {
      read();
      j = i != 0 ? 69 : 66;
    }
    else if (this.token.tokenType == 125)
    {
      read();
      j = i != 0 ? 70 : 67;
    }
    int k = i != 0 ? 23 : 54;
    Expression localExpression = XreadTableSubquery(k);
    return new ExpressionLogical(j, paramExpression, localExpression);
  }
  
  private ExpressionLogical XreadPeriodPredicateRightPart(int paramInt, Expression paramExpression)
  {
    if (paramExpression.getType() != 25) {
      throw Error.error(5564);
    }
    if (paramExpression.nodes.length != 2) {
      throw Error.error(5564);
    }
    if ((paramInt != 58) && (paramExpression.getSubType() != 57)) {
      throw unexpectedTokenRequire("PERIOD");
    }
    read();
    int i = 0;
    if (this.token.tokenType == 220)
    {
      read();
      i = 1;
      if (this.token.tokenType != 836) {
        throw unexpectedTokenRequire("(");
      }
    }
    Expression localExpression = XreadRowValuePredicand();
    if (i != 0) {
      if (localExpression.nodes.length == 2) {
        localExpression.setSubType(57);
      } else {
        throw Error.error(5564);
      }
    }
    if (localExpression.nodes.length == 2)
    {
      if (paramInt == 58)
      {
        if ((i != 0) && (paramExpression.getSubType() != 57)) {
          throw unexpectedTokenRequire("PERIOD");
        }
      }
      else if (i == 0) {
        throw unexpectedTokenRequire("PERIOD");
      }
    }
    else if (localExpression.nodes.length < 2)
    {
      if (paramInt != 56) {
        throw Error.error(5564);
      }
    }
    else {
      throw Error.error(5564);
    }
    return new ExpressionLogical(paramInt, paramExpression, localExpression);
  }
  
  Expression XreadRowValueExpression()
  {
    Expression localExpression = XreadExplicitRowValueConstructorOrNull();
    if (localExpression != null) {
      return localExpression;
    }
    return XreadRowValueSpecialCase();
  }
  
  Expression XreadTableRowValueConstructor()
  {
    Expression localExpression = XreadExplicitRowValueConstructorOrNull();
    if (localExpression != null) {
      return localExpression;
    }
    return XreadRowValueSpecialCase();
  }
  
  Expression XreadRowValuePredicand()
  {
    return XreadRowOrCommonValueExpression();
  }
  
  Expression XreadRowValueSpecialCase()
  {
    Expression localExpression = XreadSimpleValueExpressionPrimary();
    if (localExpression != null) {
      localExpression = XreadArrayElementReference(localExpression);
    }
    return localExpression;
  }
  
  Expression XreadRowValueConstructor()
  {
    Expression localExpression = XreadExplicitRowValueConstructorOrNull();
    if (localExpression != null) {
      return localExpression;
    }
    localExpression = XreadRowOrCommonValueExpression();
    if (localExpression != null) {
      return localExpression;
    }
    return XreadBooleanValueExpression();
  }
  
  Expression XreadExplicitRowValueConstructorOrNull()
  {
    Expression localExpression;
    switch (this.token.tokenType)
    {
    case 836: 
      read();
      int i = getPosition();
      readOpenBrackets();
      switch (this.token.tokenType)
      {
      case 265: 
      case 294: 
      case 324: 
        rewind(i);
        TableDerived localTableDerived = XreadSubqueryTableBody(22);
        readThis(822);
        return new Expression(22, localTableDerived);
      }
      rewind(i);
      localExpression = XreadRowElementList(true);
      readThis(822);
      return localExpression;
    case 257: 
      read();
      readThis(836);
      localExpression = XreadRowElementList(false);
      readThis(822);
      return localExpression;
    }
    return null;
  }
  
  Expression XreadRowElementList(boolean paramBoolean)
  {
    HsqlArrayList localHsqlArrayList = new HsqlArrayList();
    Expression localExpression;
    for (;;)
    {
      localExpression = XreadValueExpression();
      localHsqlArrayList.add(localExpression);
      if (this.token.tokenType != 824) {
        break;
      }
      read();
    }
    if ((paramBoolean) && (localHsqlArrayList.size() == 1)) {
      return localExpression;
    }
    Expression[] arrayOfExpression = new Expression[localHsqlArrayList.size()];
    localHsqlArrayList.toArray(arrayOfExpression);
    return new Expression(25, arrayOfExpression);
  }
  
  Expression XreadCurrentCollationSpec()
  {
    throw Error.error(1500);
  }
  
  Expression XreadTableSubquery(int paramInt)
  {
    readThis(836);
    TableDerived localTableDerived = XreadSubqueryTableBody(paramInt);
    readThis(822);
    return new Expression(23, localTableDerived);
  }
  
  Table XreadTableSubqueryOrNull(boolean paramBoolean)
  {
    int i = 0;
    int j = getPosition();
    readThis(836);
    switch (this.token.tokenType)
    {
    case 265: 
    case 294: 
    case 324: 
    case 336: 
      break;
    case 836: 
      if (paramBoolean) {
        break;
      }
    default: 
      i = 1;
    }
    if (i != 0)
    {
      rewind(j);
      return null;
    }
    TableDerived localTableDerived = XreadSubqueryTableBody(23);
    readThis(822);
    return localTableDerived;
  }
  
  TableDerived XreadJoinedTableAsSubqueryOrNull()
  {
    int i = getPosition();
    readThis(836);
    this.compileContext.incrementDepth();
    QuerySpecification localQuerySpecification = XreadJoinedTableAsView();
    localQuerySpecification.resolveReferences(this.session, this.compileContext.getOuterRanges());
    if (localQuerySpecification.rangeVariables.length < 2)
    {
      this.compileContext.decrementDepth();
      rewind(i);
      return null;
    }
    localQuerySpecification.resolveTypesPartOne(this.session);
    localQuerySpecification.resolveTypesPartTwo(this.session);
    TableDerived localTableDerived = newSubQueryTable(localQuerySpecification, 23);
    readThis(822);
    localTableDerived.setSQL(getLastPart(i));
    localTableDerived.prepareTable(this.session);
    this.compileContext.decrementDepth();
    return localTableDerived;
  }
  
  QuerySpecification XreadJoinedTableAsView()
  {
    QuerySpecification localQuerySpecification = new QuerySpecification(this.compileContext);
    ExpressionColumn localExpressionColumn = new ExpressionColumn(93);
    localQuerySpecification.addSelectColumnExpression(localExpressionColumn);
    XreadTableReference(localQuerySpecification);
    return localQuerySpecification;
  }
  
  TableDerived XreadTableNamedSubqueryBody(HsqlNameManager.HsqlName paramHsqlName, HsqlNameManager.HsqlName[] paramArrayOfHsqlName, int paramInt)
  {
    TableDerived localTableDerived;
    switch (paramInt)
    {
    case 24: 
      localTableDerived = XreadRecursiveSubqueryBody(paramHsqlName, paramArrayOfHsqlName);
      return localTableDerived;
    case 23: 
      localTableDerived = XreadSubqueryTableBody(paramHsqlName, paramInt);
      if (localTableDerived.queryExpression != null) {
        localTableDerived.queryExpression.resolve(this.session);
      }
      localTableDerived.prepareTable(this.session, paramArrayOfHsqlName);
      return localTableDerived;
    }
    throw unexpectedToken();
  }
  
  TableDerived XreadRecursiveSubqueryBody(HsqlNameManager.HsqlName paramHsqlName, HsqlNameManager.HsqlName[] paramArrayOfHsqlName)
  {
    int i = getPosition();
    this.compileContext.incrementDepth();
    this.compileContext.incrementDepth();
    QuerySpecification localQuerySpecification1 = XreadSimpleTable();
    localQuerySpecification1.resolveReferences(this.session, this.compileContext.getOuterRanges());
    localQuerySpecification1.resolve(this.session);
    TableDerived localTableDerived1 = newSubQueryTable(paramHsqlName, localQuerySpecification1, 23);
    this.compileContext.decrementDepth();
    localTableDerived1.prepareTable(this.session, paramArrayOfHsqlName);
    this.compileContext.initSubqueryNames();
    this.compileContext.registerSubquery(paramHsqlName.name);
    this.compileContext.registerSubquery(paramHsqlName.name, localTableDerived1);
    checkIsThis(314);
    int j = XreadUnionType();
    if ((this.database.sqlSyntaxDb2) && (j == 2)) {
      j = 1;
    }
    QuerySpecification localQuerySpecification2 = XreadSimpleTable();
    QueryExpression localQueryExpression = new QueryExpression(this.compileContext, localQuerySpecification1);
    localQuerySpecification2.isBaseMergeable = false;
    localQuerySpecification2.resolveReferences(this.session, this.compileContext.getOuterRanges());
    localQueryExpression.addUnion(localQuerySpecification2, j);
    localQueryExpression.isRecursive = true;
    localQueryExpression.recursiveTable = localTableDerived1;
    localQueryExpression.resolve(this.session);
    TableDerived localTableDerived2 = newSubQueryTable(paramHsqlName, localQueryExpression, 23);
    localTableDerived2.prepareTable(this.session, paramArrayOfHsqlName);
    localTableDerived2.setSQL(getLastPart(i));
    this.compileContext.decrementDepth();
    return localTableDerived2;
  }
  
  TableDerived newSubQueryTable(Expression paramExpression, int paramInt)
  {
    HsqlNameManager.HsqlName localHsqlName = this.database.nameManager.getSubqueryTableName();
    TableDerived localTableDerived = new TableDerived(this.database, localHsqlName, 2, null, paramExpression, paramInt, this.compileContext.getDepth());
    return localTableDerived;
  }
  
  TableDerived newSubQueryTable(QueryExpression paramQueryExpression, int paramInt)
  {
    return newSubQueryTable(null, paramQueryExpression, paramInt);
  }
  
  TableDerived newSubQueryTable(HsqlNameManager.HsqlName paramHsqlName, QueryExpression paramQueryExpression, int paramInt)
  {
    if (paramHsqlName == null) {
      paramHsqlName = this.database.nameManager.getSubqueryTableName();
    }
    TableDerived localTableDerived = new TableDerived(this.database, paramHsqlName, 2, paramQueryExpression, null, paramInt, this.compileContext.getDepth());
    return localTableDerived;
  }
  
  TableDerived XreadSubqueryTableBody(int paramInt)
  {
    return XreadSubqueryTableBody(null, paramInt);
  }
  
  TableDerived XreadSubqueryTableBody(HsqlNameManager.HsqlName paramHsqlName, int paramInt)
  {
    int i = getPosition();
    this.compileContext.incrementDepth();
    QueryExpression localQueryExpression = XreadQueryExpression();
    TableDerived localTableDerived = null;
    if (localQueryExpression.isValueList) {
      localTableDerived = ((QuerySpecification)localQueryExpression).getValueListTable();
    }
    if (localTableDerived == null) {
      localTableDerived = newSubQueryTable(paramHsqlName, localQueryExpression, paramInt);
    }
    localTableDerived.setSQL(getLastPart(i));
    this.compileContext.decrementDepth();
    return localTableDerived;
  }
  
  TableDerived XreadViewSubqueryTable(View paramView, boolean paramBoolean)
  {
    this.compileContext.incrementDepth();
    Object localObject;
    try
    {
      localObject = XreadQueryExpression();
    }
    catch (HsqlException localHsqlException)
    {
      localObject = XreadJoinedTableAsView();
    }
    ((QueryExpression)localObject).setView(paramView);
    ((QueryExpression)localObject).resolveReferences(this.session, RangeGroup.emptyArray);
    ((QueryExpression)localObject).resolveTypesPartOne(this.session);
    ((QueryExpression)localObject).resolveTypesPartTwo(this.session);
    if (paramBoolean) {
      ((QueryExpression)localObject).resolveTypesPartThree(this.session);
    }
    TableDerived localTableDerived = new TableDerived(this.database, paramView.getName(), 8, (QueryExpression)localObject, null, 0, this.compileContext.getDepth());
    localTableDerived.view = paramView;
    localTableDerived.columnList = paramView.columnList;
    localTableDerived.columnCount = paramView.columnList.size();
    localTableDerived.createPrimaryKey();
    localTableDerived.triggerList = paramView.triggerList;
    localTableDerived.triggerLists = paramView.triggerLists;
    this.compileContext.decrementDepth();
    return localTableDerived;
  }
  
  Expression XreadContextuallyTypedTable(int paramInt)
  {
    Expression localExpression = readRow();
    Expression[] arrayOfExpression1 = localExpression.nodes;
    int i = 0;
    if (paramInt == 1)
    {
      if (localExpression.getType() == 25)
      {
        localExpression.opType = 26;
        for (j = 0; j < arrayOfExpression1.length; j++) {
          if (arrayOfExpression1[j].getType() != 25) {
            arrayOfExpression1[j] = new Expression(25, new Expression[] { arrayOfExpression1[j] });
          } else if (arrayOfExpression1[j].nodes.length != paramInt) {
            throw Error.error(5564);
          }
        }
        return localExpression;
      }
      localExpression = new Expression(25, new Expression[] { localExpression });
      localExpression = new Expression(26, new Expression[] { localExpression });
      return localExpression;
    }
    if (localExpression.getType() != 25) {
      throw Error.error(5564);
    }
    for (int j = 0; j < arrayOfExpression1.length; j++) {
      if (arrayOfExpression1[j].getType() == 25)
      {
        i = 1;
        break;
      }
    }
    if (i != 0)
    {
      localExpression.opType = 26;
      for (j = 0; j < arrayOfExpression1.length; j++)
      {
        if (arrayOfExpression1[j].getType() != 25) {
          throw Error.error(5564);
        }
        Expression[] arrayOfExpression2 = arrayOfExpression1[j].nodes;
        if (arrayOfExpression2.length != paramInt) {
          throw Error.error(5564);
        }
        for (int k = 0; k < paramInt; k++) {
          if (arrayOfExpression2[k].getType() == 25) {
            throw Error.error(5564);
          }
        }
      }
    }
    else
    {
      if (arrayOfExpression1.length != paramInt) {
        throw Error.error(5564);
      }
      localExpression = new Expression(26, new Expression[] { localExpression });
    }
    return localExpression;
  }
  
  private Expression XreadInValueListConstructor(int paramInt)
  {
    int i = getPosition();
    this.compileContext.incrementDepth();
    Expression localExpression = XreadInValueList(paramInt);
    TableDerived localTableDerived = newSubQueryTable(localExpression, 54);
    localTableDerived.setSQL(getLastPart(i));
    localExpression.table = localTableDerived;
    this.compileContext.decrementDepth();
    return localExpression;
  }
  
  private TableDerived XreadRowValueExpressionList()
  {
    this.compileContext.incrementDepth();
    Expression localExpression = XreadRowValueExpressionListBody();
    TableDerived localTableDerived = prepareSubqueryTable(localExpression, null);
    this.compileContext.decrementDepth();
    return localTableDerived;
  }
  
  private TableDerived prepareSubqueryTable(Expression paramExpression, HsqlNameManager.HsqlName[] paramArrayOfHsqlName)
  {
    HsqlList localHsqlList = paramExpression.resolveColumnReferences(this.session, RangeGroup.emptyGroup, this.compileContext.getOuterRanges(), null);
    ExpressionColumn.checkColumnsResolved(localHsqlList);
    paramExpression.resolveTypes(this.session, null);
    paramExpression.prepareTable(this.session, null, paramExpression.nodes[0].nodes.length);
    TableDerived localTableDerived = newSubQueryTable(paramExpression, 26);
    localTableDerived.prepareTable(this.session, paramArrayOfHsqlName);
    return localTableDerived;
  }
  
  Expression XreadRowValueExpressionListBody()
  {
    Expression localExpression1 = null;
    for (;;)
    {
      int i = readOpenBrackets();
      Expression localExpression2 = readRow();
      readCloseBrackets(i);
      if (localExpression1 == null)
      {
        localExpression1 = new Expression(25, new Expression[] { localExpression2 });
      }
      else
      {
        localExpression1.nodes = ((Expression[])ArrayUtil.resizeArray(localExpression1.nodes, localExpression1.nodes.length + 1));
        localExpression1.nodes[(localExpression1.nodes.length - 1)] = localExpression2;
      }
      if (this.token.tokenType != 824) {
        break;
      }
      read();
    }
    Expression[] arrayOfExpression = localExpression1.nodes;
    int j = 1;
    if (arrayOfExpression[0].getType() == 25) {
      j = arrayOfExpression[0].nodes.length;
    }
    localExpression1.opType = 26;
    for (int k = 0; k < arrayOfExpression.length; k++) {
      if (arrayOfExpression[k].getType() == 25)
      {
        if (arrayOfExpression[k].nodes.length != j) {
          throw Error.error(5564);
        }
      }
      else
      {
        if (j != 1) {
          throw Error.error(5564);
        }
        arrayOfExpression[k] = new Expression(25, new Expression[] { arrayOfExpression[k] });
      }
    }
    return localExpression1;
  }
  
  Expression XreadTargetSpecification(RangeVariable[] paramArrayOfRangeVariable, LongDeque paramLongDeque)
  {
    ColumnSchema localColumnSchema = null;
    int i = -1;
    checkIsIdentifier();
    if (this.token.namePrePrePrefix != null) {
      checkValidCatalogName(this.token.namePrePrePrefix);
    }
    for (int j = 0; j < paramArrayOfRangeVariable.length; j++) {
      if (paramArrayOfRangeVariable[j] != null)
      {
        i = paramArrayOfRangeVariable[j].findColumn(this.token.namePrePrefix, this.token.namePrefix, this.token.tokenString);
        if (i > -1)
        {
          localColumnSchema = paramArrayOfRangeVariable[j].getColumn(i);
          read();
          break;
        }
      }
    }
    if (localColumnSchema == null) {
      throw Error.error(5501, this.token.tokenString);
    }
    paramLongDeque.add(i);
    if (this.token.tokenType == 831)
    {
      if (!localColumnSchema.getDataType().isArrayType()) {
        throw unexpectedToken();
      }
      read();
      Object localObject = XreadNumericValueExpression();
      if (localObject == null) {
        throw Error.error(5501, this.token.tokenString);
      }
      localObject = new ExpressionAccessor(localColumnSchema.getAccessor(), (Expression)localObject);
      readThis(840);
      return (Expression)localObject;
    }
    return localColumnSchema.getAccessor();
  }
  
  Expression XreadCollectionDerivedTable(int paramInt)
  {
    boolean bool = false;
    int i = getPosition();
    readThis(317);
    readThis(836);
    this.compileContext.incrementDepth();
    HsqlArrayList localHsqlArrayList = new HsqlArrayList();
    for (;;)
    {
      localObject = XreadValueExpression();
      localHsqlArrayList.add(localObject);
      if (this.token.tokenType != 824) {
        break;
      }
      read();
    }
    Object localObject = new Expression[localHsqlArrayList.size()];
    localHsqlArrayList.toArray(localObject);
    readThis(822);
    if (this.token.tokenType == 336)
    {
      read();
      readThis(480);
      bool = true;
    }
    ExpressionTable localExpressionTable = new ExpressionTable((Expression[])localObject, bool);
    TableDerived localTableDerived = newSubQueryTable(localExpressionTable, paramInt);
    localTableDerived.setSQL(getLastPart(i));
    this.compileContext.decrementDepth();
    return localExpressionTable;
  }
  
  Expression XreadTableFunctionDerivedTable()
  {
    int i = getPosition();
    readThis(294);
    readThis(836);
    this.compileContext.incrementDepth();
    Object localObject = XreadValueExpression();
    if ((((Expression)localObject).getType() != 27) && (((Expression)localObject).getType() != 28))
    {
      this.compileContext.decrementDepth();
      throw unexpectedToken("TABLE");
    }
    readThis(822);
    localObject = new ExpressionTable(new Expression[] { localObject }, false);
    TableDerived localTableDerived = newSubQueryTable((Expression)localObject, 23);
    localTableDerived.setSQL(getLastPart(i));
    this.compileContext.decrementDepth();
    return (Expression)localObject;
  }
  
  Expression XreadLateralDerivedTable()
  {
    readThis(159);
    readThis(836);
    TableDerived localTableDerived = XreadSubqueryTableBody(23);
    readThis(822);
    return new Expression(23, localTableDerived);
  }
  
  Expression XreadArrayConstructor()
  {
    readThis(836);
    TableDerived localTableDerived = XreadSubqueryTableBody(23);
    readThis(822);
    return new Expression(96, localTableDerived);
  }
  
  Collation readCollateClauseOrNull()
  {
    if (this.token.tokenType == 44)
    {
      read();
      Collation localCollation = this.database.schemaManager.getCollation(this.session, this.token.tokenString, this.token.namePrefix);
      return localCollation;
    }
    return null;
  }
  
  Expression XreadArrayElementReference(Expression paramExpression)
  {
    if (this.token.tokenType == 831)
    {
      read();
      Expression localExpression = XreadNumericValueExpression();
      readThis(840);
      paramExpression = new ExpressionAccessor(paramExpression, localExpression);
    }
    return paramExpression;
  }
  
  Expression readRow()
  {
    Object localObject = null;
    for (;;)
    {
      Expression localExpression = XreadValueExpressionWithContext();
      if (localObject == null) {
        localObject = localExpression;
      } else if (((Expression)localObject).getType() == 25)
      {
        if ((localExpression.getType() == 25) && (localObject.nodes[0].getType() != 25))
        {
          localObject = new Expression(25, new Expression[] { localObject, localExpression });
        }
        else
        {
          ((Expression)localObject).nodes = ((Expression[])ArrayUtil.resizeArray(((Expression)localObject).nodes, ((Expression)localObject).nodes.length + 1));
          ((Expression)localObject).nodes[(localObject.nodes.length - 1)] = localExpression;
        }
      }
      else {
        localObject = new Expression(25, new Expression[] { localObject, localExpression });
      }
      if (this.token.tokenType != 824) {
        break;
      }
      read();
    }
    return (Expression)localObject;
  }
  
  Expression readCaseExpression()
  {
    Expression localExpression = null;
    read();
    if (this.token.tokenType != 331) {
      localExpression = XreadRowValuePredicand();
    }
    return readCaseWhen(localExpression);
  }
  
  private Expression readCaseWhen(Expression paramExpression)
  {
    readThis(331);
    Object localObject1 = null;
    if (paramExpression == null) {
      localObject1 = XreadBooleanValueExpression();
    } else {
      for (;;)
      {
        localObject2 = XreadPredicateRightPart(paramExpression);
        if (paramExpression == localObject2) {
          localObject2 = new ExpressionLogical(paramExpression, XreadRowValuePredicand());
        }
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
    }
    readThis(296);
    Object localObject2 = XreadValueExpression();
    Object localObject3 = null;
    if (this.token.tokenType == 331)
    {
      localObject3 = readCaseWhen(paramExpression);
    }
    else if (this.token.tokenType == 97)
    {
      read();
      localObject3 = XreadValueExpression();
      readThis(99);
      readIfThis(32);
    }
    else
    {
      localObject3 = new ExpressionValue((Object)null, (Type)null);
      readThis(99);
      readIfThis(32);
    }
    ExpressionOp localExpressionOp1 = new ExpressionOp(92, (Expression)localObject2, (Expression)localObject3);
    ExpressionOp localExpressionOp2 = new ExpressionOp(89, (Expression)localObject1, localExpressionOp1);
    return localExpressionOp2;
  }
  
  private Expression readCaseWhenExpressionOrNull()
  {
    Object localObject = null;
    int i = getPosition();
    read();
    if (!readIfThis(836))
    {
      rewind(i);
      return null;
    }
    localObject = XreadBooleanValueExpression();
    readThis(824);
    Expression localExpression = XreadValueExpression();
    readThis(824);
    ExpressionOp localExpressionOp = new ExpressionOp(92, localExpression, XreadValueExpression());
    localObject = new ExpressionOp(89, (Expression)localObject, localExpressionOp);
    readThis(822);
    return (Expression)localObject;
  }
  
  private Expression readCastExpressionOrNull()
  {
    int i = this.token.tokenType == 53 ? 1 : 0;
    int j = getPosition();
    read();
    Type localType;
    Object localObject;
    if (i != 0)
    {
      if (!readIfThis(836))
      {
        rewind(j);
        return null;
      }
      if (this.database.sqlSyntaxMss)
      {
        localType = readTypeDefinition(false, true);
        readThis(824);
        localObject = XreadValueExpressionOrNull();
      }
      else
      {
        localObject = XreadValueExpressionOrNull();
        readThis(824);
        localType = Type.getTypeForJDBCConvertToken(this.token.tokenType);
        if (localType == null) {
          localType = readTypeDefinition(false, true);
        } else {
          read();
        }
      }
    }
    else
    {
      readThis(836);
      localObject = XreadValueExpressionOrNull();
      readThis(11);
      localType = readTypeDefinition(false, true);
    }
    if (((Expression)localObject).isUnresolvedParam()) {
      ((Expression)localObject).setDataType(this.session, localType);
    } else {
      localObject = new ExpressionOp((Expression)localObject, localType);
    }
    readThis(822);
    return (Expression)localObject;
  }
  
  private Expression readColumnOrFunctionExpression()
  {
    String str = this.token.tokenString;
    boolean bool = isDelimitedSimpleName();
    Token localToken = getRecordedToken();
    checkIsIdentifier();
    if (isUndelimitedSimpleName())
    {
      int i = this.token.tokenType;
      localObject2 = FunctionCustom.newCustomFunction(this.token.tokenString, this.token.tokenType);
      if ((localObject2 != null) && (i == 781) && (!this.database.sqlSyntaxOra)) {
        localObject2 = null;
      }
      if (localObject2 != null)
      {
        int j = getPosition();
        try
        {
          Expression localExpression = readSQLFunction((FunctionSQL)localObject2);
          if (localExpression != null) {
            return localExpression;
          }
        }
        catch (HsqlException localHsqlException)
        {
          localHsqlException.setLevel(this.compileContext.subqueryDepth);
          if ((this.lastError == null) || (this.lastError.getLevel() < localHsqlException.getLevel())) {
            this.lastError = localHsqlException;
          }
          rewind(j);
        }
      }
      else if (isReservedKey())
      {
        localObject2 = FunctionSQL.newSQLFunction(str, this.compileContext);
        if (localObject2 != null)
        {
          localObject3 = readSQLFunction((FunctionSQL)localObject2);
          if (localObject3 != null) {
            return (Expression)localObject3;
          }
        }
      }
    }
    read();
    if (this.token.tokenType != 836)
    {
      checkValidCatalogName(localToken.namePrePrePrefix);
      localObject1 = new ExpressionColumn(localToken.namePrePrefix, localToken.namePrefix, str);
      return (Expression)localObject1;
    }
    Object localObject1 = (RoutineSchema)this.database.schemaManager.findSchemaObject(this.session, str, localToken.namePrefix, localToken.namePrePrefix, 16);
    if ((localObject1 == null) && (localToken.namePrefix == null) && (!this.isViewDefinition))
    {
      localObject2 = this.session.getSchemaName(null);
      localObject3 = this.database.schemaManager.findSynonym(localToken.tokenString, (String)localObject2, 18);
      if (localObject3 != null)
      {
        localObject4 = ((ReferenceObject)localObject3).getTarget();
        localObject1 = (RoutineSchema)this.database.schemaManager.findSchemaObject(((HsqlNameManager.HsqlName)localObject4).name, ((HsqlNameManager.HsqlName)localObject4).schema.name, 18);
      }
    }
    if ((localObject1 == null) && (bool))
    {
      localObject2 = this.database.schemaManager.getDefaultSchemaHsqlName();
      localObject1 = (RoutineSchema)this.database.schemaManager.findSchemaObject(str, ((HsqlNameManager.HsqlName)localObject2).name, 16);
      if (localObject1 == null)
      {
        Routine.createRoutines(this.session, (HsqlNameManager.HsqlName)localObject2, str);
        localObject1 = (RoutineSchema)this.database.schemaManager.findSchemaObject(str, ((HsqlNameManager.HsqlName)localObject2).name, 16);
      }
    }
    if (localObject1 == null)
    {
      if (this.lastError != null) {
        throw this.lastError;
      }
      throw Error.error(5501, str);
    }
    Object localObject2 = new HsqlArrayList();
    readThis(836);
    if (this.token.tokenType == 822) {
      read();
    } else {
      for (;;)
      {
        localObject3 = XreadValueExpression();
        ((HsqlArrayList)localObject2).add(localObject3);
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
    Object localObject3 = new FunctionSQLInvoked((RoutineSchema)localObject1);
    Object localObject4 = new Expression[((HsqlArrayList)localObject2).size()];
    ((HsqlArrayList)localObject2).toArray(localObject4);
    ((FunctionSQLInvoked)localObject3).setArguments((Expression[])localObject4);
    this.compileContext.addFunctionCall((FunctionSQLInvoked)localObject3);
    localToken.setExpression(localObject1);
    return (Expression)localObject3;
  }
  
  Expression readCollection(int paramInt)
  {
    read();
    if (this.token.tokenType == 836) {
      return XreadArrayConstructor();
    }
    readThis(831);
    HsqlArrayList localHsqlArrayList = new HsqlArrayList();
    for (int i = 0;; i++)
    {
      if (this.token.tokenType == 840)
      {
        read();
        break;
      }
      if (i > 0) {
        readThis(824);
      }
      Expression localExpression = XreadValueExpressionOrNull();
      localHsqlArrayList.add(localExpression);
    }
    Expression[] arrayOfExpression = new Expression[localHsqlArrayList.size()];
    localHsqlArrayList.toArray(arrayOfExpression);
    return new Expression(19, arrayOfExpression);
  }
  
  private Expression readDecodeExpressionOrNull()
  {
    int i = getPosition();
    read();
    if (!readIfThis(836))
    {
      rewind(i);
      return null;
    }
    Object localObject1 = null;
    Object localObject2 = null;
    Expression localExpression1 = XreadValueExpression();
    readThis(824);
    for (;;)
    {
      Expression localExpression2 = XreadValueExpression();
      if (this.token.tokenType == 824)
      {
        readThis(824);
      }
      else
      {
        if (localObject2 == null) {
          throw unexpectedToken();
        }
        ((Expression)localObject2).setRightNode(localExpression2);
        break;
      }
      ExpressionLogical localExpressionLogical = new ExpressionLogical(64, localExpression1, localExpression2);
      Expression localExpression3 = XreadValueExpression();
      ExpressionOp localExpressionOp1 = new ExpressionOp(92, localExpression3, null);
      ExpressionOp localExpressionOp2 = new ExpressionOp(89, localExpressionLogical, localExpressionOp1);
      if (localObject1 == null) {
        localObject1 = localExpressionOp2;
      } else {
        ((Expression)localObject2).setRightNode(localExpressionOp2);
      }
      localObject2 = localExpressionOp1;
      if (this.token.tokenType == 824)
      {
        readThis(824);
      }
      else
      {
        ((Expression)localObject2).setRightNode(new ExpressionValue(null, null));
        break;
      }
    }
    readThis(822);
    return (Expression)localObject1;
  }
  
  private Expression readConcatExpressionOrNull()
  {
    int i = getPosition();
    read();
    if (!readIfThis(836))
    {
      rewind(i);
      return null;
    }
    Object localObject = XreadValueExpression();
    readThis(824);
    do
    {
      for (;;)
      {
        Expression localExpression = XreadValueExpression();
        localObject = new ExpressionArithmetic(36, (Expression)localObject, localExpression);
        if (this.token.tokenType != 824) {
          break;
        }
        readThis(824);
      }
    } while (this.token.tokenType != 822);
    readThis(822);
    return (Expression)localObject;
  }
  
  private Expression readConcatSeparatorExpressionOrNull()
  {
    HsqlArrayList localHsqlArrayList = new HsqlArrayList();
    int i = getPosition();
    read();
    if (!readIfThis(836))
    {
      rewind(i);
      return null;
    }
    Expression localExpression = XreadValueExpression();
    localHsqlArrayList.add(localExpression);
    readThis(824);
    localExpression = XreadValueExpression();
    localHsqlArrayList.add(localExpression);
    readThis(824);
    do
    {
      for (;;)
      {
        localExpression = XreadValueExpression();
        localHsqlArrayList.add(localExpression);
        if (this.token.tokenType != 824) {
          break;
        }
        readThis(824);
      }
    } while (this.token.tokenType != 822);
    readThis(822);
    Expression[] arrayOfExpression = new Expression[localHsqlArrayList.size()];
    localHsqlArrayList.toArray(arrayOfExpression);
    return new ExpressionOp(86, arrayOfExpression);
  }
  
  private Expression readLeastExpressionOrNull()
  {
    int i = getPosition();
    read();
    if (!readIfThis(836))
    {
      rewind(i);
      return null;
    }
    Expression localExpression = null;
    for (;;)
    {
      localExpression = readValue(localExpression, 44);
      if (this.token.tokenType != 824) {
        break;
      }
      readThis(824);
    }
    readThis(822);
    return localExpression;
  }
  
  private Expression readGreatestExpressionOrNull()
  {
    int i = getPosition();
    read();
    if (!readIfThis(836))
    {
      rewind(i);
      return null;
    }
    Expression localExpression = null;
    for (;;)
    {
      localExpression = readValue(localExpression, 43);
      if (this.token.tokenType != 824) {
        break;
      }
      readThis(824);
    }
    readThis(822);
    return localExpression;
  }
  
  private Expression readValue(Expression paramExpression, int paramInt)
  {
    Expression localExpression = XreadValueExpression();
    if (paramExpression == null) {
      return localExpression;
    }
    ExpressionLogical localExpressionLogical = new ExpressionLogical(paramInt, paramExpression, localExpression);
    ExpressionOp localExpressionOp = new ExpressionOp(92, paramExpression, localExpression);
    return new ExpressionOp(89, localExpressionLogical, localExpressionOp);
  }
  
  private Expression readNullIfExpression()
  {
    read();
    readThis(836);
    Object localObject = XreadValueExpression();
    readThis(824);
    ExpressionOp localExpressionOp = new ExpressionOp(92, new ExpressionValue((Object)null, (Type)null), (Expression)localObject);
    localObject = new ExpressionLogical((Expression)localObject, XreadValueExpression());
    localObject = new ExpressionOp(89, (Expression)localObject, localExpressionOp);
    readThis(822);
    return (Expression)localObject;
  }
  
  private Expression readIfNullExpressionOrNull()
  {
    int i = getPosition();
    read();
    if (!readIfThis(836))
    {
      rewind(i);
      return null;
    }
    Object localObject = XreadValueExpression();
    readThis(824);
    Expression localExpression = XreadValueExpression();
    ExpressionLogical localExpressionLogical = new ExpressionLogical(47, (Expression)localObject);
    ExpressionOp localExpressionOp = new ExpressionOp(92, localExpression, (Expression)localObject);
    localObject = new ExpressionOp(89, localExpressionLogical, localExpressionOp);
    ((Expression)localObject).setSubType(87);
    localExpressionOp.setSubType(87);
    readThis(822);
    return (Expression)localObject;
  }
  
  private Expression readIfNull2ExpressionOrNull()
  {
    int i = getPosition();
    read();
    if (!readIfThis(836))
    {
      rewind(i);
      return null;
    }
    Object localObject = XreadValueExpression();
    readThis(824);
    Expression localExpression1 = XreadValueExpression();
    readThis(824);
    Expression localExpression2 = XreadValueExpression();
    ExpressionLogical localExpressionLogical = new ExpressionLogical(47, (Expression)localObject);
    ExpressionOp localExpressionOp = new ExpressionOp(92, localExpression2, localExpression1);
    localObject = new ExpressionOp(89, localExpressionLogical, localExpressionOp);
    ((Expression)localObject).setSubType(87);
    localExpressionOp.setSubType(87);
    readThis(822);
    return (Expression)localObject;
  }
  
  private Expression readCoalesceExpression()
  {
    Object localObject1 = null;
    read();
    readThis(836);
    Object localObject2 = null;
    for (;;)
    {
      Expression localExpression = XreadValueExpression();
      if ((localObject2 != null) && (this.token.tokenType == 822))
      {
        readThis(822);
        ((Expression)localObject2).setLeftNode(localExpression);
        break;
      }
      ExpressionValue localExpressionValue = new ExpressionValue((Object)null, (Type)null);
      ExpressionLogical localExpressionLogical = new ExpressionLogical(47, localExpression);
      ExpressionOp localExpressionOp1 = new ExpressionOp(92, localExpressionValue, localExpression);
      ExpressionOp localExpressionOp2 = new ExpressionOp(89, localExpressionLogical, localExpressionOp1);
      if (this.session.database.sqlSyntaxMys)
      {
        localExpressionOp1.setSubType(87);
        localExpressionOp2.setSubType(87);
      }
      if (localObject1 == null) {
        localObject1 = localExpressionOp2;
      } else {
        ((Expression)localObject2).setLeftNode(localExpressionOp2);
      }
      localObject2 = localExpressionOp1;
      readThis(824);
    }
    return (Expression)localObject1;
  }
  
  Expression readSQLFunction(FunctionSQL paramFunctionSQL)
  {
    int i = getPosition();
    read();
    short[] arrayOfShort = paramFunctionSQL.parseList;
    if (arrayOfShort.length == 0) {
      return paramFunctionSQL;
    }
    HsqlArrayList localHsqlArrayList = new HsqlArrayList();
    int j = this.token.tokenType == 836 ? 1 : 0;
    if (j == 0)
    {
      if (arrayOfShort[0] == 902) {
        return paramFunctionSQL;
      }
      rewind(i);
      return null;
    }
    try
    {
      readExpression(localHsqlArrayList, arrayOfShort, 0, arrayOfShort.length, false);
      this.lastError = null;
    }
    catch (HsqlException localHsqlException)
    {
      if (paramFunctionSQL.parseListAlt == null) {
        throw localHsqlException;
      }
      rewind(i);
      read();
      arrayOfShort = paramFunctionSQL.parseListAlt;
      localHsqlArrayList = new HsqlArrayList();
      readExpression(localHsqlArrayList, arrayOfShort, 0, arrayOfShort.length, false);
      this.lastError = null;
    }
    Expression[] arrayOfExpression = new Expression[localHsqlArrayList.size()];
    localHsqlArrayList.toArray(arrayOfExpression);
    paramFunctionSQL.setArguments(arrayOfExpression);
    return paramFunctionSQL.getFunctionExpression();
  }
  
  void readExpression(HsqlArrayList paramHsqlArrayList, short[] paramArrayOfShort, int paramInt1, int paramInt2, boolean paramBoolean)
  {
    for (int i = paramInt1; i < paramInt1 + paramInt2; i++)
    {
      int j = paramArrayOfShort[i];
      Object localObject;
      int k;
      int m;
      int n;
      switch (j)
      {
      case 838: 
        localObject = null;
        localObject = XreadAllTypesCommonValueExpression(false);
        paramHsqlArrayList.add(localObject);
        break;
      case 904: 
        localObject = null;
        Integer localInteger = readIntegerObject();
        if (localInteger.intValue() < 0) {
          throw Error.error(5592);
        }
        localObject = new ExpressionValue(localInteger, Type.SQL_INTEGER);
        paramHsqlArrayList.add(localObject);
        break;
      case 902: 
        i++;
        k = paramHsqlArrayList.size();
        m = getPosition();
        n = paramArrayOfShort[(i++)];
        int i1 = paramHsqlArrayList.size();
        try
        {
          readExpression(paramHsqlArrayList, paramArrayOfShort, i, n, true);
        }
        catch (HsqlException localHsqlException)
        {
          localHsqlException.setLevel(this.compileContext.subqueryDepth);
          if ((this.lastError == null) || (this.lastError.getLevel() < localHsqlException.getLevel())) {
            this.lastError = localHsqlException;
          }
          rewind(m);
          paramHsqlArrayList.setSize(k);
          for (int i2 = i; i2 < i + n; i2++) {
            if ((paramArrayOfShort[i2] == 838) || (paramArrayOfShort[i2] == 901) || (paramArrayOfShort[i2] == 904)) {
              paramHsqlArrayList.add(null);
            }
          }
          i += n - 1;
          continue;
        }
        if ((i1 == paramHsqlArrayList.size()) && (paramArrayOfShort[i] != 836)) {
          paramHsqlArrayList.add(null);
        }
        i += n - 1;
        break;
      case 903: 
        i++;
        k = paramArrayOfShort[(i++)];
        m = i;
        for (;;)
        {
          n = paramHsqlArrayList.size();
          readExpression(paramHsqlArrayList, paramArrayOfShort, m, k, true);
          if (paramHsqlArrayList.size() == n) {
            break;
          }
        }
        i += k - 1;
        break;
      case 901: 
        k = paramArrayOfShort[(++i)];
        ExpressionValue localExpressionValue = null;
        if (ArrayUtil.find(paramArrayOfShort, this.token.tokenType, i + 1, k) == -1)
        {
          if (!paramBoolean) {
            throw unexpectedToken();
          }
        }
        else
        {
          localExpressionValue = new ExpressionValue(ValuePool.getInt(this.token.tokenType), Type.SQL_INTEGER);
          read();
        }
        paramHsqlArrayList.add(localExpressionValue);
        i += k;
        break;
      case 822: 
      case 824: 
      case 836: 
      default: 
        if (this.token.tokenType != j) {
          throw unexpectedToken();
        }
        read();
      }
    }
  }
  
  private Expression readSequenceExpressionOrNull(int paramInt)
  {
    int i = getPosition();
    switch (paramInt)
    {
    case 12: 
      if (this.token.tokenType == 470)
      {
        read();
        if (this.token.tokenType != 323)
        {
          rewind(i);
          return null;
        }
        readThis(323);
      }
      else if ((this.database.sqlSyntaxDb2) && (this.token.tokenType == 639))
      {
        read();
      }
      else if ((this.database.sqlSyntaxDb2) && (this.token.tokenType == 646))
      {
        read();
      }
      else
      {
        rewind(i);
        return null;
      }
      break;
    case 13: 
      read();
      readThis(323);
      break;
    }
    readThis(120);
    checkIsSchemaObjectName();
    NumberSequence localNumberSequence = this.database.schemaManager.findSequence(this.session, this.token.tokenString, this.token.namePrefix);
    if (localNumberSequence == null) {
      throw Error.error(5501, this.token.tokenString);
    }
    Token localToken = getRecordedToken();
    read();
    ExpressionColumn localExpressionColumn = new ExpressionColumn(localNumberSequence, paramInt);
    localToken.setExpression(localNumberSequence);
    this.compileContext.addSequence(localNumberSequence);
    return localExpressionColumn;
  }
  
  HsqlNameManager.SimpleName readSimpleName()
  {
    checkIsSimpleName();
    HsqlNameManager.SimpleName localSimpleName = HsqlNameManager.getSimpleName(this.token.tokenString, isDelimitedIdentifier());
    read();
    return localSimpleName;
  }
  
  HsqlNameManager.HsqlName readNewSchemaName()
  {
    HsqlNameManager.HsqlName localHsqlName = readNewSchemaObjectName(2, false);
    SqlInvariants.checkSchemaNameNotSystem(localHsqlName.name);
    return localHsqlName;
  }
  
  HsqlNameManager.HsqlName readNewSchemaObjectName(int paramInt, boolean paramBoolean)
  {
    checkIsSchemaObjectName();
    HsqlNameManager.HsqlName localHsqlName1 = this.database.nameManager.newHsqlName(this.token.tokenString, isDelimitedIdentifier(), paramInt);
    if (this.token.namePrefix != null) {
      switch (paramInt)
      {
      case 1: 
      case 11: 
      case 21: 
      case 22: 
        throw unexpectedToken();
      case 19: 
        if ((this.token.namePrePrefix != null) || (this.token.isDelimitedPrefix) || (!"MODULE".equals(this.token.namePrefix))) {
          throw unexpectedTokenRequire("MODULE");
        }
        break;
      case 2: 
        checkValidCatalogName(this.token.namePrefix);
        if (this.token.namePrePrefix != null) {
          throw tooManyIdentifiers();
        }
        break;
      case 25: 
      case 26: 
        checkValidCatalogName(this.token.namePrefix);
        if (this.token.namePrePrefix != null) {
          throw tooManyIdentifiers();
        }
        break;
      case 9: 
        throw tooManyIdentifiers();
      case 3: 
      case 4: 
      case 5: 
      case 6: 
      case 7: 
      case 8: 
      case 10: 
      case 12: 
      case 13: 
      case 14: 
      case 15: 
      case 16: 
      case 17: 
      case 18: 
      case 20: 
      case 23: 
      case 24: 
      default: 
        checkValidCatalogName(this.token.namePrePrefix);
        HsqlNameManager.HsqlName localHsqlName2;
        if (paramBoolean)
        {
          localHsqlName2 = this.session.getSchemaHsqlName(this.token.namePrefix);
        }
        else
        {
          localHsqlName2 = this.session.database.schemaManager.findSchemaHsqlName(this.token.namePrefix);
          if (localHsqlName2 == null) {
            localHsqlName2 = this.database.nameManager.newHsqlName(this.token.namePrefix, isDelimitedIdentifier(), 2);
          }
        }
        localHsqlName1.setSchemaIfNull(localHsqlName2);
        break;
      }
    }
    read();
    return localHsqlName1;
  }
  
  HsqlNameManager.HsqlName readNewDependentSchemaObjectName(HsqlNameManager.HsqlName paramHsqlName, int paramInt)
  {
    HsqlNameManager.HsqlName localHsqlName = readNewSchemaObjectName(paramInt, true);
    localHsqlName.parent = paramHsqlName;
    localHsqlName.setSchemaIfNull(paramHsqlName.schema);
    if ((localHsqlName.schema != null) && (paramHsqlName.schema != null) && (localHsqlName.schema != paramHsqlName.schema)) {
      throw Error.error(5505, this.token.namePrefix);
    }
    return localHsqlName;
  }
  
  HsqlNameManager.HsqlName readSchemaName()
  {
    checkIsSchemaObjectName();
    checkValidCatalogName(this.token.namePrefix);
    HsqlNameManager.HsqlName localHsqlName = this.session.getSchemaHsqlName(this.token.tokenString);
    read();
    return localHsqlName;
  }
  
  SchemaObject readSchemaObjectName(int paramInt)
  {
    checkIsSchemaObjectName();
    checkValidCatalogName(this.token.namePrePrefix);
    String str = this.session.getSchemaName(this.token.namePrefix);
    SchemaObject localSchemaObject = this.database.schemaManager.getSchemaObject(this.token.tokenString, str, paramInt);
    read();
    return localSchemaObject;
  }
  
  SchemaObject readSchemaObjectName(HsqlNameManager.HsqlName paramHsqlName, int paramInt)
  {
    checkIsSchemaObjectName();
    SchemaObject localSchemaObject = this.database.schemaManager.getSchemaObject(this.token.tokenString, paramHsqlName.name, paramInt);
    if (this.token.namePrefix != null)
    {
      if (!this.token.namePrefix.equals(paramHsqlName.name)) {
        throw Error.error(5505, this.token.namePrefix);
      }
      if ((this.token.namePrePrefix != null) && (!this.token.namePrePrefix.equals(this.database.getCatalogName().name))) {
        throw Error.error(5505, this.token.namePrefix);
      }
    }
    read();
    return localSchemaObject;
  }
  
  Table readTableName()
  {
    return readTableName(false);
  }
  
  Table readTableName(boolean paramBoolean)
  {
    checkIsIdentifier();
    this.lastSynonym = null;
    Table localTable = this.database.schemaManager.findTable(this.session, this.token.tokenString, this.token.namePrefix, this.token.namePrePrefix);
    if (localTable == null)
    {
      int i = (paramBoolean) && (this.token.namePrefix == null) && (!this.isViewDefinition) ? 1 : 0;
      if (i != 0)
      {
        ReferenceObject localReferenceObject = this.database.schemaManager.findSynonym(this.token.tokenString, this.session.getCurrentSchemaHsqlName().name, 3);
        if (localReferenceObject != null)
        {
          localTable = (Table)this.database.schemaManager.getSchemaObject(localReferenceObject.getTarget());
          this.lastSynonym = localReferenceObject.getName();
        }
      }
      if (localTable == null) {
        throw Error.error(5501, this.token.tokenString);
      }
    }
    getRecordedToken().setExpression(localTable);
    read();
    return localTable;
  }
  
  ColumnSchema readSimpleColumnName(RangeVariable paramRangeVariable, boolean paramBoolean)
  {
    ColumnSchema localColumnSchema = null;
    checkIsIdentifier();
    if ((!paramBoolean) && (this.token.namePrefix != null)) {
      throw tooManyIdentifiers();
    }
    int i = paramRangeVariable.findColumn(this.token.namePrePrefix, this.token.namePrefix, this.token.tokenString);
    if (i == -1) {
      throw Error.error(5501, this.token.tokenString);
    }
    localColumnSchema = paramRangeVariable.getTable().getColumn(i);
    read();
    return localColumnSchema;
  }
  
  ColumnSchema readSimpleColumnName(Table paramTable, boolean paramBoolean)
  {
    checkIsIdentifier();
    if (paramBoolean)
    {
      if ((this.token.namePrefix != null) && (!paramTable.getName().name.equals(this.token.namePrefix))) {
        throw Error.error(5501, this.token.namePrefix);
      }
    }
    else if (this.token.namePrefix != null) {
      throw tooManyIdentifiers();
    }
    int i = paramTable.findColumn(this.token.tokenString);
    if (i == -1) {
      throw Error.error(5501, this.token.tokenString);
    }
    ColumnSchema localColumnSchema = paramTable.getColumn(i);
    read();
    return localColumnSchema;
  }
  
  StatementQuery compileDeclareCursorOrNull(RangeGroup[] paramArrayOfRangeGroup, boolean paramBoolean)
  {
    int i = 0;
    int j = 0;
    int k = 0;
    int m = 0;
    int n = getPosition();
    readThis(82);
    HsqlNameManager.HsqlName localHsqlName = readNewSchemaObjectName(19, false);
    switch (this.token.tokenType)
    {
    case 266: 
      read();
      i = 2;
      break;
    case 144: 
      read();
      i = 1;
      break;
    case 12: 
      read();
      break;
    }
    if (this.token.tokenType == 190)
    {
      readThis(262);
    }
    else if (this.token.tokenType == 262)
    {
      read();
      j = 1;
    }
    if (this.token.tokenType != 75)
    {
      rewind(n);
      return null;
    }
    readThis(75);
    for (int i1 = 0; i1 < 2; i1++) {
      if (this.token.tokenType == 336)
      {
        read();
        if ((i1 == 0) && (this.token.tokenType == 136))
        {
          read();
          k = 1;
        }
        else
        {
          readThis(251);
          i1++;
          m = 1;
        }
      }
      else if (this.token.tokenType == 338)
      {
        read();
        if ((i1 == 0) && (this.token.tokenType == 136))
        {
          read();
        }
        else
        {
          readThis(251);
          i1++;
        }
      }
    }
    readThis(120);
    i1 = ResultProperties.getProperties(i, 1, j, k, m);
    StatementQuery localStatementQuery = compileCursorSpecification(paramArrayOfRangeGroup, i1, paramBoolean);
    localStatementQuery.setCursorName(localHsqlName);
    return localStatementQuery;
  }
  
  StatementQuery compileCursorSpecification(RangeGroup[] paramArrayOfRangeGroup, int paramInt, boolean paramBoolean)
  {
    OrderedHashSet localOrderedHashSet = null;
    QueryExpression localQueryExpression = XreadQueryExpression();
    if (this.token.tokenType == 120)
    {
      read();
      if ((this.token.tokenType == 502) || (this.token.tokenType == 115))
      {
        read();
        readThis(205);
        paramInt = ResultProperties.addUpdatable(paramInt, false);
      }
      else
      {
        readThis(319);
        paramInt = ResultProperties.addUpdatable(paramInt, true);
        if (this.token.tokenType == 201)
        {
          readThis(201);
          localOrderedHashSet = new OrderedHashSet();
          readColumnNameList(localOrderedHashSet, null, false);
        }
      }
    }
    if (this.database.sqlSyntaxDb2)
    {
      if ((readIfThis(336)) && (!readIfThis("CS")) && (!readIfThis("RR")) && (!readIfThis("RS"))) {
        readThis("UR");
      }
      if (readIfThis(666))
      {
        readThis(5);
        readThis("KEEP");
        if ((!readIfThis("EXCLUSIVE")) && (!readIfThis("SHARE"))) {
          readThis(319);
        }
        readThis(630);
      }
    }
    if (ResultProperties.isUpdatable(paramInt)) {
      localQueryExpression.isUpdatable = true;
    }
    localQueryExpression.setReturningResult();
    localQueryExpression.resolve(this.session, paramArrayOfRangeGroup, null);
    StatementQuery localStatementQuery = paramBoolean ? new StatementCursor(this.session, localQueryExpression, this.compileContext) : new StatementQuery(this.session, localQueryExpression, this.compileContext);
    return localStatementQuery;
  }
  
  StatementDMQL compileShortCursorSpecification(int paramInt)
  {
    QuerySpecification localQuerySpecification = XreadSimpleTable();
    if (ResultProperties.isUpdatable(paramInt)) {
      localQuerySpecification.isUpdatable = true;
    }
    localQuerySpecification.setReturningResult();
    localQuerySpecification.resolve(this.session);
    StatementQuery localStatementQuery = new StatementQuery(this.session, localQuerySpecification, this.compileContext);
    return localStatementQuery;
  }
  
  int readCloseBrackets(int paramInt)
  {
    for (int i = 0; (i < paramInt) && (this.token.tokenType == 822); i++) {
      read();
    }
    return i;
  }
  
  int readOpenBrackets()
  {
    int i = 0;
    while (this.token.tokenType == 836)
    {
      i++;
      read();
    }
    return i;
  }
  
  void readNestedParenthesisedTokens()
  {
    readThis(836);
    do
    {
      read();
      if (this.token.tokenType == 836) {
        readNestedParenthesisedTokens();
      }
      if (this.token.tokenType == 914) {
        throw unexpectedToken();
      }
    } while (this.token.tokenType != 822);
    read();
  }
  
  void checkValidCatalogName(String paramString)
  {
    if ((paramString != null) && (!paramString.equals(this.database.getCatalogName().name))) {
      throw Error.error(5501, paramString);
    }
  }
  
  void rewind(int paramInt)
  {
    super.rewind(paramInt);
    this.compileContext.rewind(paramInt);
  }
  
  public static final class CompileContext
  {
    final Session session;
    final ParserBase parser;
    final CompileContext baseContext;
    private int subqueryDepth;
    private HsqlArrayList namedSubqueries;
    private OrderedIntKeyHashMap parameters = new OrderedIntKeyHashMap();
    private IntValueHashMap parameterIndexes = new IntValueHashMap();
    private HsqlArrayList usedSequences = new HsqlArrayList(8, true);
    private HsqlArrayList usedRoutines = new HsqlArrayList(8, true);
    private OrderedIntKeyHashMap rangeVariables = new OrderedIntKeyHashMap();
    private HsqlArrayList usedObjects = new HsqlArrayList(8, true);
    Type currentDomain;
    boolean contextuallyTypedExpression;
    Routine callProcedure;
    private RangeGroup[] outerRangeGroups = RangeGroup.emptyArray;
    private int rangeVarIndex = 1;
    
    public CompileContext(Session paramSession)
    {
      this(paramSession, null, null);
    }
    
    public CompileContext(Session paramSession, ParserBase paramParserBase, CompileContext paramCompileContext)
    {
      this.session = paramSession;
      this.parser = paramParserBase;
      this.baseContext = paramCompileContext;
      if (paramCompileContext != null)
      {
        this.rangeVarIndex = paramCompileContext.getRangeVarCount();
        this.subqueryDepth = paramCompileContext.getDepth();
      }
    }
    
    public void reset()
    {
      this.rangeVarIndex = 1;
      this.subqueryDepth = 0;
      this.rangeVariables.clear();
      this.parameters.clear();
      this.parameterIndexes.clear();
      this.usedSequences.clear();
      this.usedRoutines.clear();
      this.callProcedure = null;
      this.usedObjects.clear();
      this.outerRangeGroups = RangeGroup.emptyArray;
      this.currentDomain = null;
      this.contextuallyTypedExpression = false;
    }
    
    public int getDepth()
    {
      return this.subqueryDepth;
    }
    
    public void incrementDepth()
    {
      this.subqueryDepth += 1;
      if (this.baseContext != null) {
        this.baseContext.subqueryDepth += 1;
      }
    }
    
    public void decrementDepth()
    {
      this.subqueryDepth -= 1;
      if (this.baseContext != null) {
        this.baseContext.subqueryDepth -= 1;
      }
    }
    
    public void rewind(int paramInt)
    {
      if (this.baseContext != null)
      {
        this.baseContext.rewindRangeVariables(paramInt);
        rewindParameters(paramInt);
        return;
      }
      rewindRangeVariables(paramInt);
      rewindParameters(paramInt);
    }
    
    private void rewindRangeVariables(int paramInt)
    {
      for (int i = this.rangeVariables.size() - 1; i >= 0; i--) {
        if (this.rangeVariables.getKey(i, -1) > paramInt) {
          this.rangeVariables.removeKeyAndValue(i);
        }
      }
    }
    
    private void rewindParameters(int paramInt)
    {
      Iterator localIterator = this.parameters.keySet().iterator();
      while (localIterator.hasNext())
      {
        int i = localIterator.nextInt();
        if (i >= paramInt) {
          localIterator.remove();
        }
      }
    }
    
    public void setCurrentSubquery(String paramString)
    {
      int i = this.baseContext.parameterIndexes.get(paramString, 0);
      for (int j = 0; j < i; j++) {
        this.parameters.put(this.baseContext.parameters.getKey(j, -1), this.baseContext.parameters.getValue(j));
      }
    }
    
    public void registerRangeVariable(RangeVariable paramRangeVariable)
    {
      int i = this.parser == null ? 0 : this.parser.getPosition();
      paramRangeVariable.rangePosition = getNextRangeVarIndex();
      paramRangeVariable.level = this.subqueryDepth;
      this.rangeVariables.put(i, paramRangeVariable);
    }
    
    public void setNextRangeVarIndex(int paramInt)
    {
      if (this.baseContext != null)
      {
        this.baseContext.setNextRangeVarIndex(paramInt);
        return;
      }
      this.rangeVarIndex = paramInt;
    }
    
    public int getNextRangeVarIndex()
    {
      if (this.baseContext != null) {
        return this.baseContext.getNextRangeVarIndex();
      }
      return this.rangeVarIndex++;
    }
    
    public int getRangeVarCount()
    {
      if (this.baseContext != null) {
        return this.baseContext.getRangeVarCount();
      }
      return this.rangeVarIndex;
    }
    
    public RangeVariable[] getAllRangeVariables()
    {
      RangeVariable[] arrayOfRangeVariable = new RangeVariable[this.rangeVariables.size()];
      this.rangeVariables.valuesToArray(arrayOfRangeVariable);
      return arrayOfRangeVariable;
    }
    
    public RangeGroup[] getOuterRanges()
    {
      return this.outerRangeGroups;
    }
    
    public void setOuterRanges(RangeGroup[] paramArrayOfRangeGroup)
    {
      this.outerRangeGroups = paramArrayOfRangeGroup;
    }
    
    public NumberSequence[] getSequences()
    {
      if (this.usedSequences.size() == 0) {
        return NumberSequence.emptyArray;
      }
      NumberSequence[] arrayOfNumberSequence = new NumberSequence[this.usedSequences.size()];
      this.usedSequences.toArray(arrayOfNumberSequence);
      return arrayOfNumberSequence;
    }
    
    public Routine[] getRoutines()
    {
      if ((this.callProcedure == null) && (this.usedRoutines.size() == 0)) {
        return Routine.emptyArray;
      }
      OrderedHashSet localOrderedHashSet = new OrderedHashSet();
      for (int i = 0; i < this.usedRoutines.size(); i++)
      {
        FunctionSQLInvoked localFunctionSQLInvoked = (FunctionSQLInvoked)this.usedRoutines.get(i);
        localOrderedHashSet.add(localFunctionSQLInvoked.routine);
      }
      if (this.callProcedure != null) {
        localOrderedHashSet.add(this.callProcedure);
      }
      Routine[] arrayOfRoutine = new Routine[localOrderedHashSet.size()];
      localOrderedHashSet.toArray(arrayOfRoutine);
      return arrayOfRoutine;
    }
    
    private void initSubqueryNames()
    {
      if (this.namedSubqueries == null) {
        this.namedSubqueries = new HsqlArrayList();
      }
      if (this.namedSubqueries.size() <= this.subqueryDepth) {
        this.namedSubqueries.setSize(this.subqueryDepth + 1);
      }
      HashMappedList localHashMappedList = (HashMappedList)this.namedSubqueries.get(this.subqueryDepth);
      if (localHashMappedList == null)
      {
        localHashMappedList = new HashMappedList();
        this.namedSubqueries.set(this.subqueryDepth, localHashMappedList);
      }
      else
      {
        localHashMappedList.clear();
      }
    }
    
    private void registerSubquery(String paramString)
    {
      HashMappedList localHashMappedList = (HashMappedList)this.namedSubqueries.get(this.subqueryDepth);
      boolean bool = localHashMappedList.add(paramString, null);
      if (!bool) {
        throw Error.error(5504);
      }
      this.parameterIndexes.put(paramString, this.parameters.size());
    }
    
    private void registerSubquery(String paramString, TableDerived paramTableDerived)
    {
      HashMappedList localHashMappedList = (HashMappedList)this.namedSubqueries.get(this.subqueryDepth);
      localHashMappedList.put(paramString, paramTableDerived);
    }
    
    private void unregisterSubqueries()
    {
      if (this.namedSubqueries == null) {
        return;
      }
      for (int i = this.subqueryDepth; i < this.namedSubqueries.size(); i++) {
        this.namedSubqueries.set(i, null);
      }
    }
    
    private TableDerived getNamedSubQuery(String paramString)
    {
      if (this.baseContext != null)
      {
        TableDerived localTableDerived1 = this.baseContext.getNamedSubQuery(paramString);
        if (localTableDerived1 != null) {
          return localTableDerived1;
        }
      }
      if (this.namedSubqueries == null) {
        return null;
      }
      for (int i = this.subqueryDepth; i >= 0; i--) {
        if (this.namedSubqueries.size() > i)
        {
          HashMappedList localHashMappedList = (HashMappedList)this.namedSubqueries.get(i);
          if (localHashMappedList != null)
          {
            TableDerived localTableDerived2 = (TableDerived)localHashMappedList.get(paramString);
            if (localTableDerived2 != null) {
              return localTableDerived2;
            }
          }
        }
      }
      return null;
    }
    
    private void addParameter(ExpressionColumn paramExpressionColumn, int paramInt)
    {
      paramExpressionColumn.parameterIndex = this.parameters.size();
      this.parameters.put(paramInt, paramExpressionColumn);
    }
    
    private void addSchemaObject(SchemaObject paramSchemaObject)
    {
      this.usedObjects.add(paramSchemaObject);
    }
    
    private void addSequence(SchemaObject paramSchemaObject)
    {
      this.usedSequences.add(paramSchemaObject);
    }
    
    void addFunctionCall(FunctionSQLInvoked paramFunctionSQLInvoked)
    {
      this.usedRoutines.add(paramFunctionSQLInvoked);
    }
    
    void addProcedureCall(Routine paramRoutine)
    {
      this.callProcedure = paramRoutine;
    }
    
    ExpressionColumn[] getParameters()
    {
      if (this.parameters.size() == 0) {
        return ExpressionColumn.emptyArray;
      }
      ExpressionColumn[] arrayOfExpressionColumn = new ExpressionColumn[this.parameters.size()];
      this.parameters.valuesToArray(arrayOfExpressionColumn);
      this.parameters.clear();
      return arrayOfExpressionColumn;
    }
    
    public OrderedHashSet getSchemaObjectNames()
    {
      OrderedHashSet localOrderedHashSet = new OrderedHashSet();
      Object localObject;
      for (int i = 0; i < this.usedSequences.size(); i++)
      {
        localObject = (SchemaObject)this.usedSequences.get(i);
        localOrderedHashSet.add(((SchemaObject)localObject).getName());
      }
      for (i = 0; i < this.usedObjects.size(); i++)
      {
        localObject = (SchemaObject)this.usedObjects.get(i);
        localOrderedHashSet.add(((SchemaObject)localObject).getName());
      }
      for (i = 0; i < this.rangeVariables.size(); i++)
      {
        localObject = (RangeVariable)this.rangeVariables.getValue(i);
        HsqlNameManager.HsqlName localHsqlName = ((RangeVariable)localObject).rangeTable.getName();
        if (localHsqlName.schema != SqlInvariants.SYSTEM_SCHEMA_HSQLNAME)
        {
          localOrderedHashSet.add(((RangeVariable)localObject).rangeTable.getName());
          localOrderedHashSet.addAll(((RangeVariable)localObject).getColumnNames());
        }
        else if (localHsqlName.type == 10)
        {
          localOrderedHashSet.addAll(((RangeVariable)localObject).getColumnNames());
        }
      }
      Routine[] arrayOfRoutine = getRoutines();
      for (int j = 0; j < arrayOfRoutine.length; j++) {
        localOrderedHashSet.add(arrayOfRoutine[j].getSpecificName());
      }
      return localOrderedHashSet;
    }
  }
}


/* Location:              E:\java\java学习\hutubill\lib\all.jar!\org\hsqldb\ParserDQL.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */
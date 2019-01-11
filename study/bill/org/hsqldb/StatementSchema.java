package org.hsqldb;

import org.hsqldb.error.Error;
import org.hsqldb.index.Index;
import org.hsqldb.lib.HsqlArrayList;
import org.hsqldb.lib.OrderedHashSet;
import org.hsqldb.map.ValuePool;
import org.hsqldb.navigator.RowIterator;
import org.hsqldb.persist.HsqlDatabaseProperties;
import org.hsqldb.persist.Logger;
import org.hsqldb.result.Result;
import org.hsqldb.rights.Grantee;
import org.hsqldb.rights.GranteeManager;
import org.hsqldb.rights.Right;
import org.hsqldb.rights.User;
import org.hsqldb.rights.UserManager;
import org.hsqldb.types.Charset;
import org.hsqldb.types.Collation;
import org.hsqldb.types.Type;
import org.hsqldb.types.UserTypeModifier;

public class StatementSchema
  extends Statement
{
  int order;
  Object[] arguments = ValuePool.emptyObjectArray;
  boolean isSchemaDefinition;
  Token[] statementTokens;
  
  StatementSchema(int paramInt1, int paramInt2)
  {
    super(paramInt1, paramInt2);
    this.isTransactionStatement = true;
  }
  
  StatementSchema(String paramString, int paramInt)
  {
    this(paramString, paramInt, null, (HsqlNameManager.HsqlName[])null, null);
  }
  
  StatementSchema(String paramString, int paramInt, Object[] paramArrayOfObject, HsqlNameManager.HsqlName[] paramArrayOfHsqlName1, HsqlNameManager.HsqlName[] paramArrayOfHsqlName2)
  {
    super(paramInt);
    this.isTransactionStatement = true;
    this.sql = paramString;
    if (paramArrayOfObject != null) {
      this.arguments = paramArrayOfObject;
    }
    if (paramArrayOfHsqlName1 != null) {
      this.readTableNames = paramArrayOfHsqlName1;
    }
    if (paramArrayOfHsqlName2 != null) {
      this.writeTableNames = paramArrayOfHsqlName2;
    }
    switch (paramInt)
    {
    case 1152: 
      this.group = 2002;
      break;
    case 3: 
    case 4: 
    case 5: 
    case 6: 
    case 7: 
    case 8: 
    case 1121: 
    case 1122: 
      this.group = 2002;
      break;
    case 24: 
    case 25: 
    case 26: 
    case 27: 
    case 28: 
    case 29: 
    case 30: 
    case 31: 
    case 32: 
    case 33: 
    case 34: 
    case 35: 
    case 36: 
    case 37: 
    case 38: 
    case 39: 
    case 1128: 
    case 1129: 
    case 1130: 
    case 1131: 
    case 1147: 
      this.group = 2002;
      break;
    case 53: 
      this.group = 2002;
      this.order = 10;
      break;
    case 54: 
      this.group = 2002;
      this.order = 10;
      break;
    case 63: 
    case 64: 
      this.group = 2002;
      break;
    case 68: 
      this.group = 2001;
      break;
    case 65: 
      this.group = 2001;
      this.order = 1;
      break;
    case 69: 
      this.group = 2001;
      this.order = 7;
      break;
    case 71: 
      this.group = 2001;
      this.order = 1;
      break;
    case 87: 
      this.group = 2001;
      this.order = 2;
      break;
    case 88: 
      this.group = 2001;
      this.order = 1;
      break;
    case 89: 
      this.group = 2001;
      this.order = 1;
      break;
    case 90: 
      this.group = 2001;
      this.order = 7;
      break;
    case 93: 
      this.group = 2001;
      this.order = 2;
      break;
    case 94: 
      this.group = 2001;
      this.order = 1;
      break;
    case 95: 
      this.group = 2001;
      this.order = 1;
      break;
    case 96: 
      this.group = 2001;
      this.order = 5;
      break;
    case 1126: 
      this.group = 2001;
      this.order = 1;
      break;
    case 9: 
      this.group = 2001;
      this.order = 9;
      break;
    case 11: 
      this.group = 2001;
      this.order = 1;
      break;
    case 13: 
      this.group = 2001;
      this.order = 1;
      break;
    case 23: 
      this.group = 2001;
      this.order = 1;
      break;
    case 1124: 
      this.group = 2001;
      this.order = 8;
      break;
    case 1125: 
      this.group = 2002;
      this.order = 4;
      break;
    case 1146: 
      this.group = 2002;
      this.order = 12;
      break;
    case 1123: 
      this.group = 2002;
      this.order = 11;
      break;
    case 1214: 
      this.group = 2002;
      this.statementTokens = ((Token[])paramArrayOfObject[0]);
      break;
    case 1161: 
      this.group = 2002;
      break;
    default: 
      throw Error.runtimeError(201, "StatementSchema");
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
    paramSession.database.schemaManager.setSchemaChangeTimestamp();
    try
    {
      if (this.isLogged) {
        paramSession.database.logger.writeOtherStatement(paramSession, this.sql);
      }
    }
    catch (Throwable localThrowable2)
    {
      return Result.newErrorResult(localThrowable2, this.sql);
    }
    return localResult;
  }
  
  Result getResult(Session paramSession)
  {
    SchemaManager localSchemaManager = paramSession.database.schemaManager;
    if (this.isExplain) {
      return Result.newSingleColumnStringResult("OPERATION", describe(paramSession));
    }
    Object localObject1;
    Object localObject5;
    Object localObject9;
    Object localObject16;
    Object localObject19;
    Object localObject13;
    Object localObject17;
    TableWorks localTableWorks1;
    int i4;
    Object localObject2;
    int m;
    boolean bool2;
    boolean bool4;
    OrderedHashSet localOrderedHashSet;
    Object localObject10;
    Object localObject14;
    Object localObject20;
    int i9;
    Object localObject24;
    boolean bool7;
    Object localObject3;
    Object localObject4;
    Object localObject6;
    Object localObject7;
    Object localObject15;
    Object localObject18;
    Object localObject21;
    Object localObject8;
    Object localObject11;
    switch (this.type)
    {
    case 1152: 
      localObject1 = (HsqlNameManager.HsqlName)this.arguments[0];
      localObject5 = (HsqlNameManager.HsqlName)this.arguments[1];
      if (((HsqlNameManager.HsqlName)localObject1).type == 1)
      {
        try
        {
          paramSession.checkAdmin();
          paramSession.checkDDLWrite();
          ((HsqlNameManager.HsqlName)localObject1).rename((HsqlNameManager.HsqlName)localObject5);
        }
        catch (HsqlException localHsqlException22)
        {
          return Result.newErrorResult(localHsqlException22, this.sql);
        }
      }
      else if (((HsqlNameManager.HsqlName)localObject1).type == 2)
      {
        checkSchemaUpdateAuthorisation(paramSession, (HsqlNameManager.HsqlName)localObject1);
        localSchemaManager.checkSchemaNameCanChange((HsqlNameManager.HsqlName)localObject1);
        localSchemaManager.renameSchema((HsqlNameManager.HsqlName)localObject1, (HsqlNameManager.HsqlName)localObject5);
      }
      else
      {
        try
        {
          ((HsqlNameManager.HsqlName)localObject1).setSchemaIfNull(paramSession.getCurrentSchemaHsqlName());
          Object localObject12;
          if (((HsqlNameManager.HsqlName)localObject1).type == 9)
          {
            localObject12 = localSchemaManager.getUserTable(((HsqlNameManager.HsqlName)localObject1).parent);
            int i6 = ((Table)localObject12).getColumnIndex(((HsqlNameManager.HsqlName)localObject1).name);
            localObject9 = ((Table)localObject12).getColumn(i6);
          }
          else
          {
            localObject9 = localSchemaManager.getSchemaObject((HsqlNameManager.HsqlName)localObject1);
            if (localObject9 == null) {
              throw Error.error(5501, ((HsqlNameManager.HsqlName)localObject1).name);
            }
            localObject1 = ((SchemaObject)localObject9).getName();
          }
          checkSchemaUpdateAuthorisation(paramSession, ((HsqlNameManager.HsqlName)localObject1).schema);
          ((HsqlNameManager.HsqlName)localObject5).setSchemaIfNull(((HsqlNameManager.HsqlName)localObject1).schema);
          if (((HsqlNameManager.HsqlName)localObject1).schema != ((HsqlNameManager.HsqlName)localObject5).schema)
          {
            localObject12 = Error.error(5505);
            return Result.newErrorResult((Throwable)localObject12, this.sql);
          }
          ((HsqlNameManager.HsqlName)localObject5).parent = ((HsqlNameManager.HsqlName)localObject1).parent;
          switch (((SchemaObject)localObject9).getType())
          {
          case 9: 
            localObject12 = ((SchemaObject)localObject9).getName().parent;
            localSchemaManager.checkObjectIsReferenced((HsqlNameManager.HsqlName)localObject12);
            localObject16 = localSchemaManager.getUserTable((HsqlNameManager.HsqlName)localObject12);
            localObject19 = ((Table)localObject16).getTriggers();
            for (int i8 = 0; i8 < localObject19.length; i8++) {
              if ((localObject19[i8] instanceof TriggerDefSQL)) {
                throw Error.error(5502, localObject19[i8].getName().getSchemaQualifiedStatementName());
              }
            }
            ((Table)localObject16).renameColumn((ColumnSchema)localObject9, (HsqlNameManager.HsqlName)localObject5);
            break;
          default: 
            localSchemaManager.renameSchemaObject((HsqlNameManager.HsqlName)localObject1, (HsqlNameManager.HsqlName)localObject5);
          }
        }
        catch (HsqlException localHsqlException23)
        {
          return Result.newErrorResult(localHsqlException23, this.sql);
        }
      }
      break;
    case 1121: 
      localObject1 = (Table)this.arguments[0];
      localObject5 = (int[])this.arguments[1];
      localObject9 = (HsqlNameManager.HsqlName)this.arguments[2];
      try
      {
        localObject13 = (Index)paramSession.database.schemaManager.getSchemaObject((HsqlNameManager.HsqlName)localObject9);
        localObject16 = new TableWorks(paramSession, (Table)localObject1);
        ((TableWorks)localObject16).alterIndex((Index)localObject13, (int[])localObject5);
      }
      catch (HsqlException localHsqlException24)
      {
        return Result.newErrorResult(localHsqlException24, this.sql);
      }
    case 5: 
      try
      {
        localObject1 = (NumberSequence)this.arguments[0];
        localObject5 = (NumberSequence)this.arguments[1];
        checkSchemaUpdateAuthorisation(paramSession, ((NumberSequence)localObject1).getSchemaName());
        ((NumberSequence)localObject1).reset((NumberSequence)localObject5);
      }
      catch (HsqlException localHsqlException1)
      {
        return Result.newErrorResult(localHsqlException1, this.sql);
      }
    case 3: 
      try
      {
        int i = ((Integer)this.arguments[0]).intValue();
        localObject5 = (Type)this.arguments[1];
        switch (i)
        {
        case 1134: 
          localObject9 = (Constraint)this.arguments[2];
          paramSession.database.schemaManager.checkSchemaObjectNotExists(((Constraint)localObject9).getName());
          ((Type)localObject5).userTypeModifier.addConstraint((Constraint)localObject9);
          paramSession.database.schemaManager.addSchemaObject((SchemaObject)localObject9);
          break;
        case 1135: 
          localObject9 = (Expression)this.arguments[2];
          ((Type)localObject5).userTypeModifier.setDefaultClause((Expression)localObject9);
          break;
        case 1130: 
          localObject9 = (HsqlNameManager.HsqlName)this.arguments[2];
          paramSession.database.schemaManager.removeSchemaObject((HsqlNameManager.HsqlName)localObject9);
          break;
        case 1132: 
          ((Type)localObject5).userTypeModifier.removeDefaultClause();
        }
      }
      catch (HsqlException localHsqlException2)
      {
        return Result.newErrorResult(localHsqlException2, this.sql);
      }
    case 7: 
      try
      {
        int j = ((Integer)this.arguments[0]).intValue();
        localObject5 = (Table)this.arguments[1];
        Type localType;
        switch (j)
        {
        case 1134: 
          localObject9 = (Constraint)this.arguments[2];
          localObject13 = (Boolean)this.arguments[3];
          if (((Boolean)localObject13).booleanValue())
          {
            localObject17 = paramSession.database.schemaManager.findSchemaObject(((Constraint)localObject9).getName().name, ((Constraint)localObject9).getName().schema.name, ((Constraint)localObject9).getName().type);
            if (localObject17 != null) {
              return Result.updateZeroResult;
            }
          }
          switch (((Constraint)localObject9).getConstraintType())
          {
          case 4: 
            localObject17 = new TableWorks(paramSession, (Table)localObject5);
            ((TableWorks)localObject17).addPrimaryKey((Constraint)localObject9);
            break;
          case 2: 
            localObject17 = new TableWorks(paramSession, (Table)localObject5);
            ((TableWorks)localObject17).addUniqueConstraint((Constraint)localObject9);
            break;
          case 0: 
            localObject17 = new TableWorks(paramSession, (Table)localObject5);
            ((TableWorks)localObject17).addForeignKey((Constraint)localObject9);
            break;
          case 3: 
            localObject17 = new TableWorks(paramSession, (Table)localObject5);
            ((TableWorks)localObject17).addCheckConstraint((Constraint)localObject9);
          }
          break;
        case 1133: 
          localObject9 = (ColumnSchema)this.arguments[2];
          int i2 = ((Integer)this.arguments[3]).intValue();
          localObject17 = (HsqlArrayList)this.arguments[4];
          localObject19 = new TableWorks(paramSession, (Table)localObject5);
          ((TableWorks)localObject19).addColumn((ColumnSchema)localObject9, i2, (HsqlArrayList)localObject17);
          break;
        case 1136: 
          localObject9 = (ColumnSchema)this.arguments[2];
          localType = (Type)this.arguments[3];
          localObject17 = ((ColumnSchema)localObject9).duplicate();
          ((ColumnSchema)localObject17).setType(localType);
          localObject19 = new TableWorks(paramSession, (Table)localObject5);
          ((TableWorks)localObject19).retypeColumn((ColumnSchema)localObject9, (ColumnSchema)localObject17);
          break;
        case 1144: 
          localObject9 = (ColumnSchema)this.arguments[2];
          localType = (Type)this.arguments[3];
          localObject17 = (NumberSequence)this.arguments[4];
          localObject19 = ((ColumnSchema)localObject9).duplicate();
          ((ColumnSchema)localObject19).setType(localType);
          ((ColumnSchema)localObject19).setIdentity((NumberSequence)localObject17);
          localTableWorks1 = new TableWorks(paramSession, (Table)localObject5);
          localTableWorks1.retypeColumn((ColumnSchema)localObject9, (ColumnSchema)localObject19);
          break;
        case 1137: 
          localObject9 = (ColumnSchema)this.arguments[2];
          int i3 = ((Integer)this.arguments[3]).intValue();
          localObject17 = (NumberSequence)this.arguments[4];
          if (((ColumnSchema)localObject9).isIdentity())
          {
            ((ColumnSchema)localObject9).getIdentitySequence().reset((NumberSequence)localObject17);
          }
          else
          {
            ((ColumnSchema)localObject9).setIdentity((NumberSequence)localObject17);
            ((Table)localObject5).setColumnTypeVars(i3);
          }
          break;
        case 1139: 
          localObject9 = (ColumnSchema)this.arguments[2];
          boolean bool3 = ((Boolean)this.arguments[3]).booleanValue();
          localObject17 = new TableWorks(paramSession, (Table)localObject5);
          ((TableWorks)localObject17).setColNullability((ColumnSchema)localObject9, bool3);
          break;
        case 1140: 
          localObject9 = (ColumnSchema)this.arguments[2];
          i4 = ((Integer)this.arguments[3]).intValue();
          localObject17 = (Expression)this.arguments[4];
          localObject19 = new TableWorks(paramSession, (Table)localObject5);
          ((TableWorks)localObject19).setColDefaultExpression(i4, (Expression)localObject17);
          break;
        case 1141: 
          localObject9 = (ColumnSchema)this.arguments[2];
          i4 = ((Integer)this.arguments[3]).intValue();
          localObject17 = new TableWorks(paramSession, (Table)localObject5);
          ((TableWorks)localObject17).setColDefaultExpression(i4, null);
          ((Table)localObject5).setColumnTypeVars(i4);
          break;
        case 1142: 
          localObject9 = (ColumnSchema)this.arguments[2];
          i4 = ((Integer)this.arguments[3]).intValue();
          ((ColumnSchema)localObject9).setGeneratingExpression(null);
          ((Table)localObject5).setColumnTypeVars(i4);
          break;
        case 1143: 
          localObject9 = (ColumnSchema)this.arguments[2];
          i4 = ((Integer)this.arguments[3]).intValue();
          ((ColumnSchema)localObject9).setIdentity(null);
          ((Table)localObject5).setColumnTypeVars(i4);
        }
      }
      catch (HsqlException localHsqlException3)
      {
        return Result.newErrorResult(localHsqlException3, this.sql);
      }
    case 4: 
      localObject2 = (Routine)this.arguments[0];
      try
      {
        ((Routine)localObject2).resolveReferences(paramSession);
        localObject5 = (Routine)localSchemaManager.getSchemaObject(((Routine)localObject2).getSpecificName());
        localSchemaManager.replaceReferences((SchemaObject)localObject5, (SchemaObject)localObject2);
        ((Routine)localObject5).setAsAlteredRoutine((Routine)localObject2);
      }
      catch (HsqlException localHsqlException9)
      {
        return Result.newErrorResult(localHsqlException9, this.sql);
      }
    case 6: 
    case 8: 
      throw Error.runtimeError(201, "StatementSchema");
    case 1122: 
      localObject2 = (View)this.arguments[0];
      try
      {
        checkSchemaUpdateAuthorisation(paramSession, ((View)localObject2).getSchemaName());
        View localView = (View)localSchemaManager.getSchemaObject(((View)localObject2).getName());
        if (localView == null) {
          throw Error.error(5501, ((View)localObject2).getName().name);
        }
        ((View)localObject2).setName(localView.getName());
        ((View)localObject2).compile(paramSession, null);
        localObject9 = localSchemaManager.getReferencesTo(localView.getName());
        if (((OrderedHashSet)localObject9).getCommonElementCount(((View)localObject2).getReferences()) > 0) {
          throw Error.error(5502);
        }
        i4 = localSchemaManager.getTableIndex(localView);
        localSchemaManager.setTable(i4, (Table)localObject2);
        localObject17 = new OrderedHashSet();
        ((OrderedHashSet)localObject17).add(localObject2);
        try
        {
          localSchemaManager.recompileDependentObjects((OrderedHashSet)localObject17);
          localSchemaManager.replaceReferences(localView, (SchemaObject)localObject2);
        }
        catch (HsqlException localHsqlException25)
        {
          localSchemaManager.setTable(i4, localView);
          localSchemaManager.recompileDependentObjects((OrderedHashSet)localObject17);
        }
      }
      catch (HsqlException localHsqlException10)
      {
        return Result.newErrorResult(localHsqlException10, this.sql);
      }
    case 1128: 
      try
      {
        localObject2 = (HsqlNameManager.HsqlName)this.arguments[0];
        m = ((Integer)this.arguments[1]).intValue();
        bool2 = ((Boolean)this.arguments[2]).booleanValue();
        bool4 = ((Boolean)this.arguments[3]).booleanValue();
        localObject17 = localSchemaManager.getUserTable(((HsqlNameManager.HsqlName)localObject2).parent);
        int i7 = ((Table)localObject17).getColumnIndex(((HsqlNameManager.HsqlName)localObject2).name);
        if (((Table)localObject17).getColumnCount() == 1) {
          throw Error.error(5591);
        }
        checkSchemaUpdateAuthorisation(paramSession, ((Table)localObject17).getSchemaName());
        localTableWorks1 = new TableWorks(paramSession, (Table)localObject17);
        localTableWorks1.dropColumn(i7, bool2);
      }
      catch (HsqlException localHsqlException4)
      {
        return Result.newErrorResult(localHsqlException4, this.sql);
      }
    case 24: 
    case 25: 
    case 26: 
    case 27: 
    case 28: 
    case 29: 
    case 30: 
    case 31: 
    case 32: 
    case 33: 
    case 34: 
    case 35: 
    case 36: 
    case 37: 
    case 38: 
    case 39: 
    case 1129: 
    case 1130: 
    case 1131: 
    case 1147: 
      try
      {
        HsqlNameManager.HsqlName localHsqlName = (HsqlNameManager.HsqlName)this.arguments[0];
        m = ((Integer)this.arguments[1]).intValue();
        bool2 = ((Boolean)this.arguments[2]).booleanValue();
        bool4 = ((Boolean)this.arguments[3]).booleanValue();
        switch (this.type)
        {
        case 29: 
        case 1131: 
          paramSession.checkAdmin();
          paramSession.checkDDLWrite();
          break;
        case 31: 
          checkSchemaUpdateAuthorisation(paramSession, localHsqlName);
          if ((!localSchemaManager.schemaExists(localHsqlName.name)) && (bool4)) {
            return Result.updateZeroResult;
          }
          break;
        default: 
          if (localHsqlName.schema == null) {
            localHsqlName.schema = paramSession.getCurrentSchemaHsqlName();
          } else if ((!localSchemaManager.schemaExists(localHsqlName.schema.name)) && (bool4)) {
            return Result.updateZeroResult;
          }
          localHsqlName.schema = localSchemaManager.getUserSchemaHsqlName(localHsqlName.schema.name);
          checkSchemaUpdateAuthorisation(paramSession, localHsqlName.schema);
          localObject17 = localSchemaManager.getSchemaObject(localHsqlName);
          if (localObject17 == null)
          {
            if (bool4) {
              return Result.updateZeroResult;
            }
            throw Error.error(5501, localHsqlName.name);
          }
          if (localHsqlName.type == 24) {
            localHsqlName = ((Routine)localObject17).getSpecificName();
          } else {
            localHsqlName = ((SchemaObject)localObject17).getName();
          }
          break;
        }
        if (!bool2) {
          localSchemaManager.checkObjectIsReferenced(localHsqlName);
        }
        switch (this.type)
        {
        case 29: 
          dropRole(paramSession, localHsqlName, bool2);
          break;
        case 1131: 
          dropUser(paramSession, localHsqlName, bool2);
          break;
        case 31: 
          dropSchema(paramSession, localHsqlName, bool2);
          break;
        case 24: 
          break;
        case 25: 
        case 26: 
        case 32: 
        case 36: 
        case 1147: 
          dropObject(paramSession, localHsqlName, bool2);
          break;
        case 27: 
          dropType(paramSession, localHsqlName, bool2);
          break;
        case 28: 
          dropDomain(paramSession, localHsqlName, bool2);
          break;
        case 30: 
          dropRoutine(paramSession, localHsqlName, bool2);
          break;
        case 33: 
        case 39: 
          dropTable(paramSession, localHsqlName, bool2);
          break;
        case 34: 
        case 35: 
        case 37: 
        case 38: 
          break;
        case 1129: 
          checkSchemaUpdateAuthorisation(paramSession, localHsqlName.schema);
          localSchemaManager.dropIndex(paramSession, localHsqlName);
          break;
        case 1130: 
          checkSchemaUpdateAuthorisation(paramSession, localHsqlName.schema);
          localSchemaManager.dropConstraint(paramSession, localHsqlName, bool2);
        }
      }
      catch (HsqlException localHsqlException5)
      {
        return Result.newErrorResult(localHsqlException5, this.sql);
      }
    case 53: 
    case 63: 
      try
      {
        int k = this.type == 53 ? 1 : 0;
        localOrderedHashSet = (OrderedHashSet)this.arguments[0];
        localObject10 = (HsqlNameManager.HsqlName)this.arguments[1];
        setSchemaName(paramSession, null, (HsqlNameManager.HsqlName)localObject10);
        localObject10 = localSchemaManager.getSchemaObjectName(((HsqlNameManager.HsqlName)localObject10).schema, ((HsqlNameManager.HsqlName)localObject10).name, ((HsqlNameManager.HsqlName)localObject10).type, true);
        localObject14 = localSchemaManager.getSchemaObject((HsqlNameManager.HsqlName)localObject10);
        localObject17 = (Right)this.arguments[2];
        localObject20 = (Grantee)this.arguments[3];
        i9 = ((Boolean)this.arguments[4]).booleanValue();
        boolean bool8 = ((Boolean)this.arguments[5]).booleanValue();
        if (localObject20 == null) {
          localObject20 = this.isSchemaDefinition ? this.schemaName.owner : paramSession.getGrantee();
        }
        GranteeManager localGranteeManager = paramSession.database.granteeManager;
        switch (((SchemaObject)localObject14).getType())
        {
        case 14: 
          break;
        case 3: 
        case 4: 
          localObject24 = (Table)localObject14;
          ((Right)localObject17).setColumns((Table)localObject24);
          if ((((Table)localObject24).getTableType() == 3) && (!((Right)localObject17).isFull())) {
            return Result.newErrorResult(Error.error(5595), this.sql);
          }
          break;
        }
        if (k != 0) {
          localGranteeManager.grant(paramSession, localOrderedHashSet, (SchemaObject)localObject14, (Right)localObject17, (Grantee)localObject20, bool8);
        } else {
          localGranteeManager.revoke(localOrderedHashSet, (SchemaObject)localObject14, (Right)localObject17, (Grantee)localObject20, bool8, i9);
        }
      }
      catch (HsqlException localHsqlException6)
      {
        return Result.newErrorResult(localHsqlException6, this.sql);
      }
    case 54: 
    case 64: 
      try
      {
        boolean bool1 = this.type == 54;
        localOrderedHashSet = (OrderedHashSet)this.arguments[0];
        localObject10 = (OrderedHashSet)this.arguments[1];
        localObject14 = (Grantee)this.arguments[2];
        bool7 = ((Boolean)this.arguments[3]).booleanValue();
        localObject20 = paramSession.database.granteeManager;
        ((GranteeManager)localObject20).checkGranteeList(localOrderedHashSet);
        String str;
        for (i9 = 0; i9 < localOrderedHashSet.size(); i9++)
        {
          str = (String)localOrderedHashSet.get(i9);
          ((GranteeManager)localObject20).checkRoleList(str, (OrderedHashSet)localObject10, (Grantee)localObject14, bool1);
        }
        int i10;
        int i12;
        if (bool1) {
          for (i10 = 0; i10 < localOrderedHashSet.size(); i10++)
          {
            str = (String)localOrderedHashSet.get(i10);
            for (i12 = 0; i12 < ((OrderedHashSet)localObject10).size(); i12++)
            {
              localObject24 = (String)((OrderedHashSet)localObject10).get(i12);
              ((GranteeManager)localObject20).grant(str, (String)localObject24, (Grantee)localObject14);
            }
          }
        } else {
          for (i10 = 0; i10 < localOrderedHashSet.size(); i10++)
          {
            str = (String)localOrderedHashSet.get(i10);
            for (i12 = 0; i12 < ((OrderedHashSet)localObject10).size(); i12++) {
              ((GranteeManager)localObject20).revoke(str, (String)((OrderedHashSet)localObject10).get(i12), (Grantee)localObject14);
            }
          }
        }
      }
      catch (HsqlException localHsqlException7)
      {
        return Result.newErrorResult(localHsqlException7, this.sql);
      }
    case 9: 
      return Result.updateZeroResult;
    case 11: 
      localObject3 = (Charset)this.arguments[0];
      try
      {
        setOrCheckObjectName(paramSession, null, ((Charset)localObject3).getName(), true);
        localSchemaManager.addSchemaObject((SchemaObject)localObject3);
      }
      catch (HsqlException localHsqlException11)
      {
        return Result.newErrorResult(localHsqlException11, this.sql);
      }
    case 13: 
      localObject3 = (Collation)this.arguments[0];
      try
      {
        setOrCheckObjectName(paramSession, null, ((Collation)localObject3).getName(), true);
        localSchemaManager.addSchemaObject((SchemaObject)localObject3);
      }
      catch (HsqlException localHsqlException12)
      {
        return Result.newErrorResult(localHsqlException12, this.sql);
      }
    case 65: 
      try
      {
        paramSession.checkAdmin();
        paramSession.checkDDLWrite();
        localObject3 = (HsqlNameManager.HsqlName)this.arguments[0];
        paramSession.database.getGranteeManager().addRole((HsqlNameManager.HsqlName)localObject3);
      }
      catch (HsqlException localHsqlException8)
      {
        return Result.newErrorResult(localHsqlException8, this.sql);
      }
    case 1126: 
      localObject4 = (HsqlNameManager.HsqlName)this.arguments[0];
      localObject6 = (String)this.arguments[1];
      localObject10 = (Grantee)this.arguments[2];
      boolean bool5 = ((Boolean)this.arguments[3]).booleanValue();
      bool7 = ((Boolean)this.arguments[4]).booleanValue();
      try
      {
        paramSession.checkAdmin();
        paramSession.checkDDLWrite();
        paramSession.database.getUserManager().createUser(paramSession, (HsqlNameManager.HsqlName)localObject4, (String)localObject6, bool7);
        if (bool5) {
          paramSession.database.getGranteeManager().grant(((HsqlNameManager.HsqlName)localObject4).name, "DBA", (Grantee)localObject10);
        }
      }
      catch (HsqlException localHsqlException26)
      {
        return Result.newErrorResult(localHsqlException26, this.sql);
      }
    case 68: 
      localObject4 = (HsqlNameManager.HsqlName)this.arguments[0];
      localObject6 = (Grantee)this.arguments[1];
      try
      {
        paramSession.checkDDLWrite();
        if (localSchemaManager.schemaExists(((HsqlNameManager.HsqlName)localObject4).name))
        {
          if ((!paramSession.isProcessingScript()) || (!"PUBLIC".equals(((HsqlNameManager.HsqlName)localObject4).name))) {
            throw Error.error(5504, ((HsqlNameManager.HsqlName)localObject4).name);
          }
        }
        else
        {
          localSchemaManager.createSchema((HsqlNameManager.HsqlName)localObject4, (Grantee)localObject6);
          localObject10 = localSchemaManager.findSchema(((HsqlNameManager.HsqlName)localObject4).name);
          this.sql = ((Schema)localObject10).getSQL();
          if ((paramSession.isProcessingScript()) && (paramSession.database.getProperties().isVersion18())) {
            paramSession.setCurrentSchemaHsqlName(((Schema)localObject10).getName());
          }
        }
      }
      catch (HsqlException localHsqlException15)
      {
        return Result.newErrorResult(localHsqlException15, this.sql);
      }
    case 69: 
      localObject4 = (Routine)this.arguments[0];
      try
      {
        ((Routine)localObject4).resolve(paramSession);
        setOrCheckObjectName(paramSession, null, ((Routine)localObject4).getName(), false);
        localSchemaManager.addSchemaObject((SchemaObject)localObject4);
      }
      catch (HsqlException localHsqlException13)
      {
        return Result.newErrorResult(localHsqlException13, this.sql);
      }
    case 1124: 
      localObject4 = (HsqlNameManager.HsqlName)this.arguments[0];
      localObject7 = (Routine[])this.arguments[1];
      try
      {
        paramSession.checkAdmin();
        paramSession.checkDDLWrite();
        if (localObject4 != null) {
          for (int n = 0; n < localObject7.length; n++)
          {
            localObject7[n].setName((HsqlNameManager.HsqlName)localObject4);
            localSchemaManager.addSchemaObject(localObject7[n]);
          }
        }
      }
      catch (HsqlException localHsqlException16)
      {
        return Result.newErrorResult(localHsqlException16, this.sql);
      }
    case 71: 
      localObject4 = (NumberSequence)this.arguments[0];
      localObject7 = (Boolean)this.arguments[1];
      try
      {
        setOrCheckObjectName(paramSession, null, ((NumberSequence)localObject4).getName(), true);
        localSchemaManager.addSchemaObject((SchemaObject)localObject4);
      }
      catch (HsqlException localHsqlException17)
      {
        if ((localObject7 != null) && (((Boolean)localObject7).booleanValue())) {
          return Result.updateZeroResult;
        }
        return Result.newErrorResult(localHsqlException17, this.sql);
      }
    case 23: 
      localObject4 = (Type)this.arguments[0];
      localObject7 = ((Type)localObject4).userTypeModifier.getConstraints();
      try
      {
        setOrCheckObjectName(paramSession, null, ((Type)localObject4).getName(), true);
        for (int i1 = 0; i1 < localObject7.length; i1++)
        {
          localObject15 = localObject7[i1];
          setOrCheckObjectName(paramSession, ((Type)localObject4).getName(), ((Constraint)localObject15).getName(), true);
          localSchemaManager.addSchemaObject((SchemaObject)localObject15);
        }
        localSchemaManager.addSchemaObject((SchemaObject)localObject4);
      }
      catch (HsqlException localHsqlException18)
      {
        return Result.newErrorResult(localHsqlException18, this.sql);
      }
    case 87: 
      localObject4 = (Table)this.arguments[0];
      localObject7 = (HsqlArrayList)this.arguments[1];
      HsqlArrayList localHsqlArrayList = (HsqlArrayList)this.arguments[2];
      localObject15 = (StatementDMQL)this.arguments[3];
      localObject18 = (Boolean)this.arguments[4];
      localObject21 = null;
      try
      {
        setOrCheckObjectName(paramSession, null, ((Table)localObject4).getName(), true);
      }
      catch (HsqlException localHsqlException27)
      {
        if ((localObject18 != null) && (((Boolean)localObject18).booleanValue())) {
          return Result.updateZeroResult;
        }
        return Result.newErrorResult(localHsqlException27, this.sql);
      }
      try
      {
        if (this.isSchemaDefinition) {
          localObject21 = new HsqlArrayList();
        }
        if (((HsqlArrayList)localObject7).size() != 0)
        {
          localObject4 = ParserDDL.addTableConstraintDefinitions(paramSession, (Table)localObject4, (HsqlArrayList)localObject7, (HsqlArrayList)localObject21, true);
          this.arguments[1] = localObject21;
        }
        ((Table)localObject4).compile(paramSession, null);
        localSchemaManager.addSchemaObject((SchemaObject)localObject4);
        Object localObject22;
        Object localObject23;
        if (localHsqlArrayList != null)
        {
          localObject22 = new TableWorks(paramSession, (Table)localObject4);
          for (int i11 = 0; i11 < localHsqlArrayList.size(); i11++)
          {
            localObject23 = (Constraint)localHsqlArrayList.get(i11);
            ((TableWorks)localObject22).addIndex(((Constraint)localObject23).getMainColumns(), ((Constraint)localObject23).getName(), false);
          }
          localObject4 = ((TableWorks)localObject22).getTable();
        }
        if (localObject15 != null)
        {
          localObject22 = ((StatementDMQL)localObject15).execute(paramSession);
          if (((Result)localObject22).isError()) {
            return (Result)localObject22;
          }
          ((Table)localObject4).insertIntoTable(paramSession, (Result)localObject22);
        }
        if (((Table)localObject4).hasLobColumn)
        {
          localObject22 = ((Table)localObject4).rowIterator(paramSession);
          while (((RowIterator)localObject22).hasNext())
          {
            Row localRow = ((RowIterator)localObject22).getNextRow();
            localObject23 = localRow.getData();
            paramSession.sessionData.adjustLobUsageCount((TableBase)localObject4, (Object[])localObject23, 1);
          }
        }
        return Result.updateZeroResult;
      }
      catch (HsqlException localHsqlException28)
      {
        localSchemaManager.removeExportedKeys((Table)localObject4);
        localSchemaManager.removeDependentObjects(((Table)localObject4).getName());
        return Result.newErrorResult(localHsqlException28, this.sql);
      }
    case 88: 
      return Result.updateZeroResult;
    case 89: 
      return Result.updateZeroResult;
    case 90: 
      localObject4 = (TriggerDef)this.arguments[0];
      localObject7 = (HsqlNameManager.HsqlName)this.arguments[1];
      try
      {
        checkSchemaUpdateAuthorisation(paramSession, ((TriggerDef)localObject4).getSchemaName());
        localSchemaManager.checkSchemaObjectNotExists(((TriggerDef)localObject4).getName());
        if ((localObject7 != null) && (localSchemaManager.getSchemaObject((HsqlNameManager.HsqlName)localObject7) == null)) {
          throw Error.error(5501, ((HsqlNameManager.HsqlName)localObject7).name);
        }
        ((TriggerDef)localObject4).table.addTrigger((TriggerDef)localObject4, (HsqlNameManager.HsqlName)localObject7);
        localSchemaManager.addSchemaObject((SchemaObject)localObject4);
        ((TriggerDef)localObject4).start();
      }
      catch (HsqlException localHsqlException19)
      {
        return Result.newErrorResult(localHsqlException19, this.sql);
      }
    case 93: 
      return Result.updateZeroResult;
    case 94: 
      localObject4 = (Type)this.arguments[0];
      try
      {
        setOrCheckObjectName(paramSession, null, ((Type)localObject4).getName(), true);
        localSchemaManager.addSchemaObject((SchemaObject)localObject4);
      }
      catch (HsqlException localHsqlException14)
      {
        return Result.newErrorResult(localHsqlException14, this.sql);
      }
    case 95: 
      return Result.updateZeroResult;
    case 96: 
      localObject4 = (View)this.arguments[0];
      localObject8 = (Boolean)this.arguments[1];
      try
      {
        setOrCheckObjectName(paramSession, null, ((View)localObject4).getName(), true);
      }
      catch (HsqlException localHsqlException20)
      {
        if ((localObject8 != null) && (((Boolean)localObject8).booleanValue())) {
          return Result.updateZeroResult;
        }
        return Result.newErrorResult(localHsqlException20, this.sql);
      }
      try
      {
        ((View)localObject4).compile(paramSession, null);
        localSchemaManager.addSchemaObject((SchemaObject)localObject4);
      }
      catch (HsqlException localHsqlException21)
      {
        return Result.newErrorResult(localHsqlException21, this.sql);
      }
    case 1125: 
      localObject4 = (Table)this.arguments[0];
      localObject11 = (int[])this.arguments[1];
      localObject8 = (HsqlNameManager.HsqlName)this.arguments[2];
      boolean bool6 = ((Boolean)this.arguments[3]).booleanValue();
      localObject18 = (RoutineSchema)this.arguments[4];
      localObject21 = (Boolean)this.arguments[5];
      try
      {
        setOrCheckObjectName(paramSession, ((Table)localObject4).getName(), (HsqlNameManager.HsqlName)localObject8, true);
      }
      catch (HsqlException localHsqlException29)
      {
        if ((localObject21 != null) && (((Boolean)localObject21).booleanValue())) {
          return Result.updateZeroResult;
        }
        return Result.newErrorResult(localHsqlException29, this.sql);
      }
      try
      {
        TableWorks localTableWorks2 = new TableWorks(paramSession, (Table)localObject4);
        localTableWorks2.addIndex((int[])localObject11, (HsqlNameManager.HsqlName)localObject8, bool6);
      }
      catch (HsqlException localHsqlException30)
      {
        return Result.newErrorResult(localHsqlException30, this.sql);
      }
    case 1146: 
      localObject4 = (HsqlNameManager.HsqlName)this.arguments[0];
      localObject8 = (HsqlNameManager.HsqlName)this.arguments[1];
      setSchemaName(paramSession, null, (HsqlNameManager.HsqlName)localObject4);
      setSchemaName(paramSession, null, (HsqlNameManager.HsqlName)localObject8);
      paramSession.database.schemaManager.checkSchemaObjectNotExists((HsqlNameManager.HsqlName)localObject4);
      localObject11 = paramSession.database.schemaManager.findAnySchemaObject(((HsqlNameManager.HsqlName)localObject8).name, ((HsqlNameManager.HsqlName)localObject8).schema.name);
      if (localObject11 == null) {
        throw Error.error(5501);
      }
      if (!paramSession.getGrantee().isFullyAccessibleByRole(((SchemaObject)localObject11).getName())) {
        throw Error.error(5501);
      }
      localObject8 = ((SchemaObject)localObject11).getName();
      ReferenceObject localReferenceObject = new ReferenceObject((HsqlNameManager.HsqlName)localObject4, (HsqlNameManager.HsqlName)localObject8);
      localSchemaManager.addSchemaObject(localReferenceObject);
      break;
    case 1123: 
      localObject4 = (HsqlNameManager.HsqlName)this.arguments[0];
      localObject8 = (String)this.arguments[1];
      switch (((HsqlNameManager.HsqlName)localObject4).type)
      {
      case 9: 
        localObject11 = (Table)localSchemaManager.getSchemaObject(((HsqlNameManager.HsqlName)localObject4).parent.name, ((HsqlNameManager.HsqlName)localObject4).parent.schema.name, 3);
        if (!paramSession.getGrantee().isFullyAccessibleByRole(((Table)localObject11).getName())) {
          throw Error.error(5501);
        }
        int i5 = ((Table)localObject11).getColumnIndex(((HsqlNameManager.HsqlName)localObject4).name);
        if (i5 < 0) {
          throw Error.error(5501);
        }
        localObject18 = ((Table)localObject11).getColumn(i5);
        ((ColumnSchema)localObject18).getName().comment = ((String)localObject8);
        break;
      case 18: 
        localObject11 = (RoutineSchema)localSchemaManager.getSchemaObject(((HsqlNameManager.HsqlName)localObject4).name, ((HsqlNameManager.HsqlName)localObject4).schema.name, 18);
        if (!paramSession.getGrantee().isFullyAccessibleByRole(((RoutineSchema)localObject11).getName())) {
          throw Error.error(5501);
        }
        ((RoutineSchema)localObject11).getName().comment = ((String)localObject8);
        break;
      case 3: 
        localObject11 = (Table)localSchemaManager.getSchemaObject(((HsqlNameManager.HsqlName)localObject4).name, ((HsqlNameManager.HsqlName)localObject4).schema.name, 3);
        if (!paramSession.getGrantee().isFullyAccessibleByRole(((Table)localObject11).getName())) {
          throw Error.error(5501);
        }
        ((Table)localObject11).getName().comment = ((String)localObject8);
      }
      break;
    case 1161: 
      break;
    default: 
      throw Error.runtimeError(201, "StatementSchema");
    }
    return Result.updateZeroResult;
  }
  
  private void dropType(Session paramSession, HsqlNameManager.HsqlName paramHsqlName, boolean paramBoolean)
  {
    checkSchemaUpdateAuthorisation(paramSession, paramHsqlName.schema);
    Type localType = (Type)paramSession.database.schemaManager.getSchemaObject(paramHsqlName);
    paramSession.database.schemaManager.removeSchemaObject(paramHsqlName, paramBoolean);
    localType.userTypeModifier = null;
  }
  
  private static void dropDomain(Session paramSession, HsqlNameManager.HsqlName paramHsqlName, boolean paramBoolean)
  {
    Type localType = (Type)paramSession.database.schemaManager.getSchemaObject(paramHsqlName);
    OrderedHashSet localOrderedHashSet = paramSession.database.schemaManager.getReferencesTo(localType.getName());
    if ((!paramBoolean) && (localOrderedHashSet.size() > 0))
    {
      localObject = (HsqlNameManager.HsqlName)localOrderedHashSet.get(0);
      throw Error.error(5502, ((HsqlNameManager.HsqlName)localObject).getSchemaQualifiedStatementName());
    }
    Object localObject = localType.userTypeModifier.getConstraints();
    localOrderedHashSet = new OrderedHashSet();
    for (int i = 0; i < localObject.length; i++) {
      localOrderedHashSet.add(localObject[i].getName());
    }
    paramSession.database.schemaManager.removeSchemaObjects(localOrderedHashSet);
    paramSession.database.schemaManager.removeSchemaObject(localType.getName(), paramBoolean);
    localType.userTypeModifier = null;
  }
  
  private static void dropRole(Session paramSession, HsqlNameManager.HsqlName paramHsqlName, boolean paramBoolean)
  {
    Grantee localGrantee = paramSession.database.getGranteeManager().getRole(paramHsqlName.name);
    if ((!paramBoolean) && (paramSession.database.schemaManager.hasSchemas(localGrantee)))
    {
      HsqlArrayList localHsqlArrayList = paramSession.database.schemaManager.getSchemas(localGrantee);
      Schema localSchema = (Schema)localHsqlArrayList.get(0);
      throw Error.error(5502, localSchema.getName().statementName);
    }
    paramSession.database.schemaManager.dropSchemas(paramSession, localGrantee, paramBoolean);
    paramSession.database.getGranteeManager().dropRole(paramHsqlName.name);
  }
  
  private static void dropUser(Session paramSession, HsqlNameManager.HsqlName paramHsqlName, boolean paramBoolean)
  {
    User localUser = paramSession.database.getUserManager().get(paramHsqlName.name);
    if (paramSession.database.getSessionManager().isUserActive(paramHsqlName.name)) {
      throw Error.error(5539);
    }
    if ((!paramBoolean) && (paramSession.database.schemaManager.hasSchemas(localUser)))
    {
      HsqlArrayList localHsqlArrayList = paramSession.database.schemaManager.getSchemas(localUser);
      Schema localSchema = (Schema)localHsqlArrayList.get(0);
      throw Error.error(5502, localSchema.getName().statementName);
    }
    paramSession.database.schemaManager.dropSchemas(paramSession, localUser, paramBoolean);
    paramSession.database.getUserManager().dropUser(paramHsqlName.name);
  }
  
  private void dropSchema(Session paramSession, HsqlNameManager.HsqlName paramHsqlName, boolean paramBoolean)
  {
    HsqlNameManager.HsqlName localHsqlName = paramSession.database.schemaManager.getUserSchemaHsqlName(paramHsqlName.name);
    checkSchemaUpdateAuthorisation(paramSession, localHsqlName);
    paramSession.database.schemaManager.dropSchema(paramSession, paramHsqlName.name, paramBoolean);
  }
  
  private void dropRoutine(Session paramSession, HsqlNameManager.HsqlName paramHsqlName, boolean paramBoolean)
  {
    checkSchemaUpdateAuthorisation(paramSession, paramHsqlName.schema);
    paramSession.database.schemaManager.removeSchemaObject(paramHsqlName, paramBoolean);
  }
  
  private void dropObject(Session paramSession, HsqlNameManager.HsqlName paramHsqlName, boolean paramBoolean)
  {
    paramHsqlName = paramSession.database.schemaManager.getSchemaObjectName(paramHsqlName.schema, paramHsqlName.name, paramHsqlName.type, true);
    paramSession.database.schemaManager.removeSchemaObject(paramHsqlName, paramBoolean);
  }
  
  private void dropTable(Session paramSession, HsqlNameManager.HsqlName paramHsqlName, boolean paramBoolean)
  {
    Table localTable = paramSession.database.schemaManager.findUserTable(paramHsqlName.name, paramHsqlName.schema.name);
    paramSession.database.schemaManager.dropTableOrView(paramSession, localTable, paramBoolean);
  }
  
  static void checkSchemaUpdateAuthorisation(Session paramSession, HsqlNameManager.HsqlName paramHsqlName)
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
  
  void setOrCheckObjectName(Session paramSession, HsqlNameManager.HsqlName paramHsqlName1, HsqlNameManager.HsqlName paramHsqlName2, boolean paramBoolean)
  {
    if (paramHsqlName2.schema == null)
    {
      paramHsqlName2.schema = (this.schemaName == null ? paramSession.getCurrentSchemaHsqlName() : this.schemaName);
    }
    else
    {
      paramHsqlName2.schema = paramSession.getSchemaHsqlName(paramHsqlName2.schema.name);
      if (paramHsqlName2.schema == null) {
        throw Error.error(5505);
      }
      if ((this.isSchemaDefinition) && (this.schemaName != paramHsqlName2.schema)) {
        throw Error.error(5505);
      }
    }
    paramHsqlName2.parent = paramHsqlName1;
    if (!this.isSchemaDefinition) {
      checkSchemaUpdateAuthorisation(paramSession, paramHsqlName2.schema);
    }
    if (paramBoolean) {
      paramSession.database.schemaManager.checkSchemaObjectNotExists(paramHsqlName2);
    }
  }
  
  void setSchemaName(Session paramSession, HsqlNameManager.HsqlName paramHsqlName1, HsqlNameManager.HsqlName paramHsqlName2)
  {
    if (paramHsqlName2.schema == null)
    {
      paramHsqlName2.schema = (this.schemaName == null ? paramSession.getCurrentSchemaHsqlName() : this.schemaName);
    }
    else
    {
      paramHsqlName2.schema = paramSession.getSchemaHsqlName(paramHsqlName2.schema.name);
      if (paramHsqlName2.schema == null) {
        throw Error.error(5505);
      }
      if ((this.isSchemaDefinition) && (this.schemaName != paramHsqlName2.schema)) {
        throw Error.error(5505);
      }
    }
  }
  
  public boolean isAutoCommitStatement()
  {
    return true;
  }
  
  public String describe(Session paramSession)
  {
    return this.sql;
  }
  
  public Object[] getArguments()
  {
    return this.arguments;
  }
}


/* Location:              E:\java\java学习\hutubill\lib\all.jar!\org\hsqldb\StatementSchema.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */
package org.hsqldb;

import org.hsqldb.error.Error;
import org.hsqldb.lib.Collection;
import org.hsqldb.lib.HashMap;
import org.hsqldb.lib.HashMappedList;
import org.hsqldb.lib.HsqlArrayList;
import org.hsqldb.lib.Iterator;
import org.hsqldb.lib.OrderedHashSet;
import org.hsqldb.lib.WrapperIterator;
import org.hsqldb.rights.Grantee;

public final class Schema
  implements SchemaObject
{
  private HsqlNameManager.HsqlName name;
  SchemaObjectSet triggerLookup;
  SchemaObjectSet constraintLookup;
  SchemaObjectSet indexLookup;
  SchemaObjectSet tableLookup;
  SchemaObjectSet sequenceLookup;
  SchemaObjectSet typeLookup;
  SchemaObjectSet charsetLookup;
  SchemaObjectSet collationLookup;
  SchemaObjectSet procedureLookup;
  SchemaObjectSet functionLookup;
  SchemaObjectSet specificRoutineLookup;
  SchemaObjectSet assertionLookup;
  SchemaObjectSet referenceLookup;
  HashMappedList tableList;
  HashMappedList sequenceList;
  HashMappedList referenceList;
  long changeTimestamp;
  
  public Schema(HsqlNameManager.HsqlName paramHsqlName, Grantee paramGrantee)
  {
    this.name = paramHsqlName;
    this.triggerLookup = new SchemaObjectSet(8);
    this.indexLookup = new SchemaObjectSet(20);
    this.constraintLookup = new SchemaObjectSet(5);
    this.tableLookup = new SchemaObjectSet(3);
    this.sequenceLookup = new SchemaObjectSet(7);
    this.typeLookup = new SchemaObjectSet(12);
    this.charsetLookup = new SchemaObjectSet(14);
    this.collationLookup = new SchemaObjectSet(15);
    this.procedureLookup = new SchemaObjectSet(17);
    this.functionLookup = new SchemaObjectSet(16);
    this.specificRoutineLookup = new SchemaObjectSet(24);
    this.assertionLookup = new SchemaObjectSet(6);
    this.referenceLookup = new SchemaObjectSet(29);
    this.tableList = ((HashMappedList)this.tableLookup.map);
    this.sequenceList = ((HashMappedList)this.sequenceLookup.map);
    this.referenceList = ((HashMappedList)this.referenceLookup.map);
    paramHsqlName.owner = paramGrantee;
  }
  
  public int getType()
  {
    return 2;
  }
  
  public HsqlNameManager.HsqlName getName()
  {
    return this.name;
  }
  
  public HsqlNameManager.HsqlName getSchemaName()
  {
    return null;
  }
  
  public HsqlNameManager.HsqlName getCatalogName()
  {
    return this.name.schema;
  }
  
  public Grantee getOwner()
  {
    return this.name.owner;
  }
  
  public OrderedHashSet getReferences()
  {
    return new OrderedHashSet();
  }
  
  public OrderedHashSet getComponents()
  {
    return null;
  }
  
  public void compile(Session paramSession, SchemaObject paramSchemaObject) {}
  
  public long getChangeTimestamp()
  {
    return this.changeTimestamp;
  }
  
  public String getSQL()
  {
    StringBuffer localStringBuffer = new StringBuffer(128);
    localStringBuffer.append("CREATE").append(' ');
    localStringBuffer.append("SCHEMA").append(' ');
    localStringBuffer.append(getName().statementName).append(' ');
    localStringBuffer.append("AUTHORIZATION").append(' ');
    localStringBuffer.append(getOwner().getName().getStatementName());
    return localStringBuffer.toString();
  }
  
  static String getSetSchemaSQL(HsqlNameManager.HsqlName paramHsqlName)
  {
    StringBuffer localStringBuffer = new StringBuffer();
    localStringBuffer.append("SET").append(' ');
    localStringBuffer.append("SCHEMA").append(' ');
    localStringBuffer.append(paramHsqlName.statementName);
    return localStringBuffer.toString();
  }
  
  public HsqlArrayList getSQLArray(OrderedHashSet paramOrderedHashSet1, OrderedHashSet paramOrderedHashSet2)
  {
    HsqlArrayList localHsqlArrayList = new HsqlArrayList();
    String str = getSetSchemaSQL(this.name);
    localHsqlArrayList.add(str);
    this.sequenceLookup.getSQL(localHsqlArrayList, paramOrderedHashSet1, paramOrderedHashSet2);
    this.tableLookup.getSQL(localHsqlArrayList, paramOrderedHashSet1, paramOrderedHashSet2);
    this.functionLookup.getSQL(localHsqlArrayList, paramOrderedHashSet1, paramOrderedHashSet2);
    this.procedureLookup.getSQL(localHsqlArrayList, paramOrderedHashSet1, paramOrderedHashSet2);
    this.referenceLookup.getSQL(localHsqlArrayList, paramOrderedHashSet1, paramOrderedHashSet2);
    if (localHsqlArrayList.size() == 1) {
      localHsqlArrayList.clear();
    }
    return localHsqlArrayList;
  }
  
  public HsqlArrayList getSequenceRestartSQL()
  {
    HsqlArrayList localHsqlArrayList = new HsqlArrayList();
    Iterator localIterator = this.sequenceLookup.map.values().iterator();
    while (localIterator.hasNext())
    {
      NumberSequence localNumberSequence = (NumberSequence)localIterator.next();
      String str = localNumberSequence.getRestartSQL();
      localHsqlArrayList.add(str);
    }
    return localHsqlArrayList;
  }
  
  public HsqlArrayList getTriggerSQL()
  {
    HsqlArrayList localHsqlArrayList = new HsqlArrayList();
    Iterator localIterator = this.tableLookup.map.values().iterator();
    while (localIterator.hasNext())
    {
      Table localTable = (Table)localIterator.next();
      String[] arrayOfString = localTable.getTriggerSQL();
      localHsqlArrayList.addAll(arrayOfString);
    }
    return localHsqlArrayList;
  }
  
  public void addSimpleObjects(OrderedHashSet paramOrderedHashSet)
  {
    Iterator localIterator = this.specificRoutineLookup.map.values().iterator();
    while (localIterator.hasNext())
    {
      Routine localRoutine = (Routine)localIterator.next();
      if ((localRoutine.dataImpact == 1) || (localRoutine.dataImpact == 2)) {
        paramOrderedHashSet.add(localRoutine);
      }
    }
    paramOrderedHashSet.addAll(this.typeLookup.map.values());
    paramOrderedHashSet.addAll(this.charsetLookup.map.values());
    paramOrderedHashSet.addAll(this.collationLookup.map.values());
  }
  
  boolean isEmpty()
  {
    return (this.sequenceLookup.isEmpty()) && (this.tableLookup.isEmpty()) && (this.typeLookup.isEmpty()) && (this.charsetLookup.isEmpty()) && (this.collationLookup.isEmpty()) && (this.specificRoutineLookup.isEmpty());
  }
  
  public SchemaObjectSet getObjectSet(int paramInt)
  {
    switch (paramInt)
    {
    case 7: 
      return this.sequenceLookup;
    case 3: 
    case 4: 
      return this.tableLookup;
    case 14: 
      return this.charsetLookup;
    case 15: 
      return this.collationLookup;
    case 17: 
      return this.procedureLookup;
    case 16: 
      return this.functionLookup;
    case 18: 
      return this.functionLookup;
    case 24: 
      return this.specificRoutineLookup;
    case 12: 
    case 13: 
      return this.typeLookup;
    case 6: 
      return this.assertionLookup;
    case 8: 
      return this.triggerLookup;
    case 29: 
      return this.referenceLookup;
    case 20: 
      return this.indexLookup;
    case 5: 
      return this.constraintLookup;
    }
    throw Error.runtimeError(201, "Schema");
  }
  
  Iterator schemaObjectIterator(int paramInt)
  {
    switch (paramInt)
    {
    case 7: 
      return this.sequenceLookup.map.values().iterator();
    case 3: 
    case 4: 
      return this.tableLookup.map.values().iterator();
    case 14: 
      return this.charsetLookup.map.values().iterator();
    case 15: 
      return this.collationLookup.map.values().iterator();
    case 17: 
      return this.procedureLookup.map.values().iterator();
    case 16: 
      return this.functionLookup.map.values().iterator();
    case 18: 
      Iterator localIterator = this.functionLookup.map.values().iterator();
      return new WrapperIterator(localIterator, this.procedureLookup.map.values().iterator());
    case 24: 
      return this.specificRoutineLookup.map.values().iterator();
    case 12: 
    case 13: 
      return this.typeLookup.map.values().iterator();
    case 6: 
      return this.assertionLookup.map.values().iterator();
    case 8: 
      return this.triggerLookup.map.values().iterator();
    case 29: 
      return this.referenceLookup.map.values().iterator();
    case 20: 
      return this.indexLookup.map.values().iterator();
    case 5: 
      return this.constraintLookup.map.values().iterator();
    }
    throw Error.runtimeError(201, "Schema");
  }
  
  SchemaObject findAnySchemaObject(String paramString)
  {
    int[] arrayOfInt1 = { 7, 3, 18 };
    for (int k : arrayOfInt1)
    {
      SchemaObject localSchemaObject = findSchemaObject(paramString, k);
      if (localSchemaObject != null) {
        return localSchemaObject;
      }
    }
    return null;
  }
  
  ReferenceObject findReference(String paramString, int paramInt)
  {
    ReferenceObject localReferenceObject = (ReferenceObject)this.referenceList.get(paramString);
    if (localReferenceObject == null) {
      return null;
    }
    if (localReferenceObject.getTarget().type == paramInt) {
      return localReferenceObject;
    }
    switch (paramInt)
    {
    case 3: 
      if (localReferenceObject.getTarget().type == 4) {
        return localReferenceObject;
      }
    case 18: 
      if ((localReferenceObject.getTarget().type == 16) || (localReferenceObject.getTarget().type == 17)) {
        return localReferenceObject;
      }
      break;
    }
    return null;
  }
  
  SchemaObject findSchemaObject(String paramString, int paramInt)
  {
    SchemaObjectSet localSchemaObjectSet = null;
    HsqlNameManager.HsqlName localHsqlName;
    Table localTable;
    switch (paramInt)
    {
    case 7: 
      return this.sequenceLookup.getObject(paramString);
    case 3: 
    case 4: 
      return this.tableLookup.getObject(paramString);
    case 14: 
      return this.charsetLookup.getObject(paramString);
    case 15: 
      return this.collationLookup.getObject(paramString);
    case 17: 
      return this.procedureLookup.getObject(paramString);
    case 16: 
      return this.functionLookup.getObject(paramString);
    case 18: 
      SchemaObject localSchemaObject = this.procedureLookup.getObject(paramString);
      if (localSchemaObject == null) {
        localSchemaObject = this.functionLookup.getObject(paramString);
      }
      return localSchemaObject;
    case 24: 
      return this.specificRoutineLookup.getObject(paramString);
    case 12: 
    case 13: 
      return this.typeLookup.getObject(paramString);
    case 20: 
      localSchemaObjectSet = this.indexLookup;
      localHsqlName = localSchemaObjectSet.getName(paramString);
      if (localHsqlName == null) {
        return null;
      }
      localTable = (Table)this.tableList.get(localHsqlName.parent.name);
      return localTable.getIndex(paramString);
    case 5: 
      localSchemaObjectSet = this.constraintLookup;
      localHsqlName = localSchemaObjectSet.getName(paramString);
      if (localHsqlName == null) {
        return null;
      }
      localTable = (Table)this.tableList.get(localHsqlName.parent.name);
      if (localTable == null) {
        return null;
      }
      return localTable.getConstraint(paramString);
    case 8: 
      localSchemaObjectSet = this.triggerLookup;
      localHsqlName = localSchemaObjectSet.getName(paramString);
      if (localHsqlName == null) {
        return null;
      }
      localTable = (Table)this.tableList.get(localHsqlName.parent.name);
      return localTable.getTrigger(paramString);
    case 29: 
      return this.referenceLookup.getObject(paramString);
    }
    throw Error.runtimeError(201, "SchemaManager");
  }
  
  public void addSchemaObject(HsqlNameManager paramHsqlNameManager, SchemaObject paramSchemaObject, boolean paramBoolean)
  {
    HsqlNameManager.HsqlName localHsqlName1 = paramSchemaObject.getName();
    SchemaObjectSet localSchemaObjectSet1 = getObjectSet(localHsqlName1.type);
    switch (localHsqlName1.type)
    {
    case 16: 
    case 17: 
      RoutineSchema localRoutineSchema = (RoutineSchema)localSchemaObjectSet1.getObject(localHsqlName1.name);
      SchemaObjectSet localSchemaObjectSet2;
      if (localRoutineSchema == null)
      {
        localRoutineSchema = new RoutineSchema(localHsqlName1.type, localHsqlName1);
        localRoutineSchema.addSpecificRoutine(paramHsqlNameManager, (Routine)paramSchemaObject, paramBoolean);
        localSchemaObjectSet1.checkAdd(localHsqlName1);
        localSchemaObjectSet2 = getObjectSet(24);
        localSchemaObjectSet2.checkAdd(((Routine)paramSchemaObject).getSpecificName());
        localSchemaObjectSet1.add(localRoutineSchema, paramBoolean);
        localSchemaObjectSet2.add(paramSchemaObject, paramBoolean);
      }
      else
      {
        localSchemaObjectSet2 = getObjectSet(24);
        HsqlNameManager.HsqlName localHsqlName2 = ((Routine)paramSchemaObject).getSpecificName();
        if (localHsqlName2 != null) {
          localSchemaObjectSet2.checkAdd(localHsqlName2);
        }
        localRoutineSchema.addSpecificRoutine(paramHsqlNameManager, (Routine)paramSchemaObject, paramBoolean);
        localSchemaObjectSet2.add(paramSchemaObject, paramBoolean);
      }
      return;
    }
    localSchemaObjectSet1.add(paramSchemaObject, paramBoolean);
  }
  
  void release()
  {
    for (int i = 0; i < this.tableList.size(); i++)
    {
      Table localTable = (Table)this.tableList.get(i);
      localTable.terminateTriggers();
    }
    this.triggerLookup = null;
    this.indexLookup = null;
    this.constraintLookup = null;
    this.charsetLookup = null;
    this.collationLookup = null;
    this.procedureLookup = null;
    this.functionLookup = null;
    this.specificRoutineLookup = null;
    this.sequenceLookup = null;
    this.tableLookup = null;
    this.typeLookup = null;
    this.tableList.clear();
    this.sequenceList.clear();
    this.referenceList.clear();
  }
}


/* Location:              E:\java\java学习\hutubill\lib\all.jar!\org\hsqldb\Schema.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */
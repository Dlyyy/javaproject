package org.hsqldb;

import org.hsqldb.error.Error;
import org.hsqldb.lib.ArrayUtil;
import org.hsqldb.lib.HashMappedList;
import org.hsqldb.lib.HashSet;
import org.hsqldb.lib.HsqlArrayList;
import org.hsqldb.lib.LongDeque;
import org.hsqldb.map.ValuePool;
import org.hsqldb.navigator.RangeIterator;
import org.hsqldb.navigator.RowSetNavigatorDataChange;
import org.hsqldb.navigator.RowSetNavigatorDataChangeMemory;
import org.hsqldb.persist.PersistentStoreCollectionSession;
import org.hsqldb.result.Result;

public class SessionContext
{
  Session session;
  public Boolean isAutoCommit;
  Boolean isReadOnly;
  Boolean noSQL;
  int currentMaxRows;
  HashMappedList sessionVariables;
  RangeVariable[] sessionVariablesRange;
  RangeGroup[] sessionVariableRangeGroups;
  private HsqlArrayList stack;
  Object[] diagnosticsVariables = ValuePool.emptyObjectArray;
  Object[] routineArguments = ValuePool.emptyObjectArray;
  Object[] routineVariables = ValuePool.emptyObjectArray;
  Result[] routineCursors = Result.emptyArray;
  Object[] dynamicArguments = ValuePool.emptyObjectArray;
  Object[][] triggerArguments = (Object[][])null;
  public int depth;
  Boolean isInRoutine;
  Number lastIdentity = ValuePool.INTEGER_0;
  HashMappedList savepoints;
  LongDeque savepointTimestamps;
  RangeIterator[] rangeIterators;
  HashMappedList sessionTables;
  HashMappedList popSessionTables;
  public Statement currentStatement;
  public int rownum;
  HashSet constraintPath;
  StatementResultUpdate rowUpdateStatement = new StatementResultUpdate();
  
  SessionContext(Session paramSession)
  {
    this.session = paramSession;
    this.diagnosticsVariables = new Object[ExpressionColumn.diagnosticsVariableTokens.length];
    this.rangeIterators = new RangeIterator[8];
    this.savepoints = new HashMappedList(4);
    this.savepointTimestamps = new LongDeque();
    this.sessionVariables = new HashMappedList();
    this.sessionVariablesRange = new RangeVariable[1];
    this.sessionVariablesRange[0] = new RangeVariable(this.sessionVariables, null, true, 4);
    this.sessionVariableRangeGroups = new RangeGroup[] { new RangeGroup.RangeGroupSimple(this.sessionVariablesRange, true) };
    this.isAutoCommit = Boolean.FALSE;
    this.isReadOnly = Boolean.FALSE;
    this.noSQL = Boolean.FALSE;
    this.isInRoutine = Boolean.FALSE;
  }
  
  void resetStack()
  {
    while (this.depth > 0) {
      pop(this.isInRoutine.booleanValue());
    }
  }
  
  public void push()
  {
    push(false);
  }
  
  private void push(boolean paramBoolean)
  {
    if (this.depth > 256) {
      throw Error.error(458);
    }
    this.session.sessionData.persistentStoreCollection.push(paramBoolean);
    if (this.stack == null) {
      this.stack = new HsqlArrayList(32, true);
    }
    this.stack.add(this.diagnosticsVariables);
    this.stack.add(this.dynamicArguments);
    this.stack.add(this.routineArguments);
    this.stack.add(this.triggerArguments);
    this.stack.add(this.routineVariables);
    this.stack.add(this.routineCursors);
    this.stack.add(this.rangeIterators);
    this.stack.add(this.savepoints);
    this.stack.add(this.savepointTimestamps);
    this.stack.add(this.lastIdentity);
    this.stack.add(this.isAutoCommit);
    this.stack.add(this.isReadOnly);
    this.stack.add(this.noSQL);
    this.stack.add(this.isInRoutine);
    this.stack.add(ValuePool.getInt(this.currentMaxRows));
    this.stack.add(ValuePool.getInt(this.rownum));
    this.diagnosticsVariables = new Object[ExpressionColumn.diagnosticsVariableTokens.length];
    this.rangeIterators = new RangeIterator[8];
    this.savepoints = new HashMappedList(4);
    this.savepointTimestamps = new LongDeque();
    this.isAutoCommit = Boolean.FALSE;
    this.currentMaxRows = 0;
    this.isInRoutine = Boolean.valueOf(paramBoolean);
    this.depth += 1;
  }
  
  public void pop()
  {
    pop(false);
  }
  
  private void pop(boolean paramBoolean)
  {
    this.session.sessionData.persistentStoreCollection.pop(paramBoolean);
    this.rownum = ((Integer)this.stack.remove(this.stack.size() - 1)).intValue();
    this.currentMaxRows = ((Integer)this.stack.remove(this.stack.size() - 1)).intValue();
    this.isInRoutine = ((Boolean)this.stack.remove(this.stack.size() - 1));
    this.noSQL = ((Boolean)this.stack.remove(this.stack.size() - 1));
    this.isReadOnly = ((Boolean)this.stack.remove(this.stack.size() - 1));
    this.isAutoCommit = ((Boolean)this.stack.remove(this.stack.size() - 1));
    this.lastIdentity = ((Number)this.stack.remove(this.stack.size() - 1));
    this.savepointTimestamps = ((LongDeque)this.stack.remove(this.stack.size() - 1));
    this.savepoints = ((HashMappedList)this.stack.remove(this.stack.size() - 1));
    this.rangeIterators = ((RangeIterator[])this.stack.remove(this.stack.size() - 1));
    this.routineCursors = ((Result[])this.stack.remove(this.stack.size() - 1));
    this.routineVariables = ((Object[])this.stack.remove(this.stack.size() - 1));
    this.triggerArguments = ((Object[][])this.stack.remove(this.stack.size() - 1));
    this.routineArguments = ((Object[])this.stack.remove(this.stack.size() - 1));
    this.dynamicArguments = ((Object[])this.stack.remove(this.stack.size() - 1));
    this.diagnosticsVariables = ((Object[])this.stack.remove(this.stack.size() - 1));
    this.depth -= 1;
  }
  
  public void pushRoutineInvocation()
  {
    push(true);
  }
  
  public void popRoutineInvocation()
  {
    pop(true);
  }
  
  public void pushDynamicArguments(Object[] paramArrayOfObject)
  {
    push();
    this.dynamicArguments = paramArrayOfObject;
  }
  
  public void pushStatementState()
  {
    if (this.stack == null) {
      this.stack = new HsqlArrayList(32, true);
    }
    this.stack.add(ValuePool.getInt(this.rownum));
  }
  
  public void popStatementState()
  {
    this.rownum = ((Integer)this.stack.remove(this.stack.size() - 1)).intValue();
  }
  
  public void setDynamicArguments(Object[] paramArrayOfObject)
  {
    this.dynamicArguments = paramArrayOfObject;
  }
  
  RowSetNavigatorDataChange getRowSetDataChange()
  {
    return new RowSetNavigatorDataChangeMemory(this.session);
  }
  
  void clearStructures(StatementDMQL paramStatementDMQL)
  {
    int i = paramStatementDMQL.rangeIteratorCount;
    if (i > this.rangeIterators.length) {
      i = this.rangeIterators.length;
    }
    for (int j = 0; j < i; j++) {
      if (this.rangeIterators[j] != null)
      {
        this.rangeIterators[j].release();
        this.rangeIterators[j] = null;
      }
    }
  }
  
  public RangeVariable.RangeIteratorBase getCheckIterator(RangeVariable paramRangeVariable)
  {
    Object localObject = this.rangeIterators[0];
    if (localObject == null)
    {
      localObject = paramRangeVariable.getIterator(this.session);
      this.rangeIterators[0] = localObject;
    }
    return (RangeVariable.RangeIteratorBase)localObject;
  }
  
  public void setRangeIterator(RangeIterator paramRangeIterator)
  {
    int i = paramRangeIterator.getRangePosition();
    if (i >= this.rangeIterators.length) {
      this.rangeIterators = ((RangeIterator[])ArrayUtil.resizeArray(this.rangeIterators, i + 4));
    }
    this.rangeIterators[i] = paramRangeIterator;
  }
  
  public RangeIterator getRangeIterator(int paramInt)
  {
    RangeIterator[] arrayOfRangeIterator = this.rangeIterators;
    if (this.stack != null) {
      for (int i = 0; i < this.stack.size(); i++)
      {
        Object localObject = this.stack.get(i);
        if ((localObject instanceof RangeIterator[]))
        {
          arrayOfRangeIterator = (RangeIterator[])localObject;
          break;
        }
      }
    }
    return arrayOfRangeIterator[paramInt];
  }
  
  public void unsetRangeIterator(RangeIterator paramRangeIterator)
  {
    int i = paramRangeIterator.getRangePosition();
    this.rangeIterators[i] = null;
  }
  
  public HashSet getConstraintPath()
  {
    if (this.constraintPath == null) {
      this.constraintPath = new HashSet();
    } else {
      this.constraintPath.clear();
    }
    return this.constraintPath;
  }
  
  public void addSessionVariable(ColumnSchema paramColumnSchema)
  {
    int i = this.sessionVariables.size();
    if (!this.sessionVariables.add(paramColumnSchema.getName().name, paramColumnSchema)) {
      throw Error.error(5504);
    }
    Object[] arrayOfObject = new Object[this.sessionVariables.size()];
    ArrayUtil.copyArray(this.routineVariables, arrayOfObject, this.routineVariables.length);
    this.routineVariables = arrayOfObject;
    this.routineVariables[i] = paramColumnSchema.getDefaultValue(this.session);
  }
  
  public void pushRoutineTables()
  {
    this.popSessionTables = this.sessionTables;
    this.sessionTables = new HashMappedList();
  }
  
  public void popRoutineTables()
  {
    this.sessionTables.clear();
    this.sessionTables = this.popSessionTables;
  }
  
  public void addSessionTable(Table paramTable)
  {
    if (this.sessionTables == null) {
      this.sessionTables = new HashMappedList();
    }
    if (this.sessionTables.containsKey(paramTable.getName().name)) {
      throw Error.error(5504);
    }
    this.sessionTables.add(paramTable.getName().name, paramTable);
  }
  
  public void setSessionTables(Table[] paramArrayOfTable) {}
  
  public Table findSessionTable(String paramString)
  {
    if (this.sessionTables == null) {
      return null;
    }
    return (Table)this.sessionTables.get(paramString);
  }
  
  public void dropSessionTable(String paramString)
  {
    this.sessionTables.remove(paramString);
  }
}


/* Location:              E:\java\java学习\hutubill\lib\all.jar!\org\hsqldb\SessionContext.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */
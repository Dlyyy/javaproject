package org.hsqldb.index;

import java.io.PrintStream;
import org.hsqldb.Constraint;
import org.hsqldb.Database;
import org.hsqldb.HsqlNameManager.HsqlName;
import org.hsqldb.Row;
import org.hsqldb.RowAVL;
import org.hsqldb.SchemaObject;
import org.hsqldb.Session;
import org.hsqldb.Table;
import org.hsqldb.TableBase;
import org.hsqldb.TransactionManager;
import org.hsqldb.error.Error;
import org.hsqldb.lib.ArrayUtil;
import org.hsqldb.lib.OrderedHashSet;
import org.hsqldb.navigator.RowIterator;
import org.hsqldb.persist.CachedObject;
import org.hsqldb.persist.PersistentStore;
import org.hsqldb.rights.Grantee;
import org.hsqldb.types.Type;

public class IndexAVL
  implements Index
{
  private static final IndexRowIterator emptyIterator = new IndexRowIterator(null, (PersistentStore)null, null, null, 0, false, false);
  private final long persistenceId;
  protected final HsqlNameManager.HsqlName name;
  private final boolean[] colCheck;
  final int[] colIndex;
  private final int[] defaultColMap;
  final Type[] colTypes;
  private final boolean[] colDesc;
  private final boolean[] nullsLast;
  final boolean isSimpleOrder;
  final boolean isSimple;
  protected final boolean isPK;
  protected final boolean isUnique;
  protected final boolean isConstraint;
  private final boolean isForward;
  private boolean isClustered;
  protected TableBase table;
  int position;
  private Index.IndexUse[] asArray;
  Object[] nullData;
  
  public IndexAVL(HsqlNameManager.HsqlName paramHsqlName, long paramLong, TableBase paramTableBase, int[] paramArrayOfInt, boolean[] paramArrayOfBoolean1, boolean[] paramArrayOfBoolean2, Type[] paramArrayOfType, boolean paramBoolean1, boolean paramBoolean2, boolean paramBoolean3, boolean paramBoolean4)
  {
    this.persistenceId = paramLong;
    this.name = paramHsqlName;
    this.colIndex = paramArrayOfInt;
    this.colTypes = paramArrayOfType;
    this.colDesc = (paramArrayOfBoolean1 == null ? new boolean[paramArrayOfInt.length] : paramArrayOfBoolean1);
    this.nullsLast = (paramArrayOfBoolean2 == null ? new boolean[paramArrayOfInt.length] : paramArrayOfBoolean2);
    this.isPK = paramBoolean1;
    this.isUnique = paramBoolean2;
    this.isConstraint = paramBoolean3;
    this.isForward = paramBoolean4;
    this.table = paramTableBase;
    this.colCheck = paramTableBase.getNewColumnCheckList();
    this.asArray = new Index.IndexUse[] { new Index.IndexUse(this, this.colIndex.length) };
    ArrayUtil.intIndexesToBooleanArray(this.colIndex, this.colCheck);
    this.defaultColMap = new int[paramArrayOfInt.length];
    ArrayUtil.fillSequence(this.defaultColMap);
    boolean bool = this.colIndex.length > 0;
    for (int i = 0; i < this.colDesc.length; i++) {
      if ((this.colDesc[i] != 0) || (this.nullsLast[i] != 0)) {
        bool = false;
      }
    }
    this.isSimpleOrder = bool;
    this.isSimple = ((this.isSimpleOrder) && (this.colIndex.length == 1));
    this.nullData = new Object[this.colIndex.length];
  }
  
  public int getType()
  {
    return 20;
  }
  
  public HsqlNameManager.HsqlName getName()
  {
    return this.name;
  }
  
  public HsqlNameManager.HsqlName getCatalogName()
  {
    return this.name.schema.schema;
  }
  
  public HsqlNameManager.HsqlName getSchemaName()
  {
    return this.name.schema;
  }
  
  public Grantee getOwner()
  {
    return this.name.schema.owner;
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
  
  public String getSQL()
  {
    StringBuffer localStringBuffer = new StringBuffer(128);
    localStringBuffer.append("CREATE").append(' ');
    if (isUnique()) {
      localStringBuffer.append("UNIQUE").append(' ');
    }
    localStringBuffer.append("INDEX").append(' ');
    localStringBuffer.append(getName().statementName);
    localStringBuffer.append(' ').append("ON").append(' ');
    localStringBuffer.append(((Table)this.table).getName().getSchemaQualifiedStatementName());
    localStringBuffer.append(((Table)this.table).getColumnListSQL(this.colIndex, this.colIndex.length));
    return localStringBuffer.toString();
  }
  
  public long getChangeTimestamp()
  {
    return 0L;
  }
  
  public Index.IndexUse[] asArray()
  {
    return this.asArray;
  }
  
  public RowIterator emptyIterator()
  {
    return emptyIterator;
  }
  
  public int getPosition()
  {
    return this.position;
  }
  
  public void setPosition(int paramInt)
  {
    this.position = paramInt;
  }
  
  public long getPersistenceId()
  {
    return this.persistenceId;
  }
  
  public int getColumnCount()
  {
    return this.colIndex.length;
  }
  
  public boolean isUnique()
  {
    return this.isUnique;
  }
  
  public boolean isConstraint()
  {
    return this.isConstraint;
  }
  
  public int[] getColumns()
  {
    return this.colIndex;
  }
  
  public Type[] getColumnTypes()
  {
    return this.colTypes;
  }
  
  public boolean[] getColumnDesc()
  {
    return this.colDesc;
  }
  
  public int[] getDefaultColumnMap()
  {
    return this.defaultColMap;
  }
  
  public int getIndexOrderValue()
  {
    if (this.isPK) {
      return 0;
    }
    if (this.isConstraint) {
      return this.isUnique ? 0 : this.isForward ? 4 : 1;
    }
    return 2;
  }
  
  public boolean isForward()
  {
    return this.isForward;
  }
  
  public void setTable(TableBase paramTableBase)
  {
    this.table = paramTableBase;
  }
  
  public TableBase getTable()
  {
    return this.table;
  }
  
  public void setClustered(boolean paramBoolean)
  {
    this.isClustered = paramBoolean;
  }
  
  public boolean isClustered()
  {
    return this.isClustered;
  }
  
  public long size(Session paramSession, PersistentStore paramPersistentStore)
  {
    return paramPersistentStore.elementCount(paramSession);
  }
  
  public long sizeUnique(PersistentStore paramPersistentStore)
  {
    return paramPersistentStore.elementCountUnique(this);
  }
  
  public double[] searchCost(Session paramSession, PersistentStore paramPersistentStore)
  {
    int i = 0;
    int j = 1;
    double[] arrayOfDouble1 = new double[this.colIndex.length];
    int k = 0;
    int[] arrayOfInt = new int[1];
    paramPersistentStore.readLock();
    try
    {
      Object localObject1 = getAccessor(paramPersistentStore);
      Object localObject2 = localObject1;
      double[] arrayOfDouble2;
      if (localObject1 == null)
      {
        arrayOfDouble2 = arrayOfDouble1;
        return arrayOfDouble2;
      }
      for (;;)
      {
        localObject1 = localObject2;
        localObject2 = ((NodeAVL)localObject1).getLeft(paramPersistentStore);
        if (localObject2 == null) {
          break;
        }
        if (k == 4)
        {
          i = 1;
          break;
        }
        k++;
      }
      for (;;)
      {
        localObject2 = next(paramPersistentStore, (NodeAVL)localObject1, k, 4, arrayOfInt);
        k = arrayOfInt[0];
        if (localObject2 == null) {
          break;
        }
        compareRowForChange(paramSession, ((NodeAVL)localObject1).getData(paramPersistentStore), ((NodeAVL)localObject2).getData(paramPersistentStore), arrayOfDouble1);
        localObject1 = localObject2;
        j++;
      }
      if (i != 0)
      {
        arrayOfDouble2 = new double[this.colIndex.length];
        int m = probeFactor(paramSession, paramPersistentStore, arrayOfDouble2, true) + probeFactor(paramSession, paramPersistentStore, arrayOfDouble2, false);
        for (n = 0; n < this.colIndex.length; n++)
        {
          arrayOfDouble2[n] /= 2.0D;
          for (int i1 = 0; i1 < arrayOfDouble2[n]; i1++) {
            arrayOfDouble1[n] *= 2.0D;
          }
        }
      }
      long l = paramPersistentStore.elementCount();
      for (int n = 0; n < this.colIndex.length; n++)
      {
        if (arrayOfDouble1[n] == 0.0D) {
          arrayOfDouble1[n] = 1.0D;
        }
        arrayOfDouble1[n] = (l / arrayOfDouble1[n]);
        if (arrayOfDouble1[n] < 2.0D) {
          arrayOfDouble1[n] = 2.0D;
        }
      }
      double[] arrayOfDouble3 = arrayOfDouble1;
      return arrayOfDouble3;
    }
    finally
    {
      paramPersistentStore.readUnlock();
    }
  }
  
  int probeFactor(Session paramSession, PersistentStore paramPersistentStore, double[] paramArrayOfDouble, boolean paramBoolean)
  {
    int i = 0;
    Object localObject1 = getAccessor(paramPersistentStore);
    Object localObject2 = localObject1;
    if (localObject1 == null) {
      return 0;
    }
    while (localObject2 != null)
    {
      localObject1 = localObject2;
      localObject2 = paramBoolean ? ((NodeAVL)localObject1).getLeft(paramPersistentStore) : ((NodeAVL)localObject1).getRight(paramPersistentStore);
      i++;
      if ((i > 4) && (localObject2 != null)) {
        compareRowForChange(paramSession, ((NodeAVL)localObject1).getData(paramPersistentStore), ((NodeAVL)localObject2).getData(paramPersistentStore), paramArrayOfDouble);
      }
    }
    return i - 4;
  }
  
  public long getNodeCount(Session paramSession, PersistentStore paramPersistentStore)
  {
    long l = 0L;
    RowIterator localRowIterator = firstRow(paramSession, paramPersistentStore, 0, null);
    while (localRowIterator.hasNext())
    {
      localRowIterator.getNextRow();
      l += 1L;
    }
    return l;
  }
  
  public boolean isEmpty(PersistentStore paramPersistentStore)
  {
    paramPersistentStore.readLock();
    try
    {
      boolean bool = getAccessor(paramPersistentStore) == null;
      return bool;
    }
    finally
    {
      paramPersistentStore.readUnlock();
    }
  }
  
  public void unlinkNodes(NodeAVL paramNodeAVL)
  {
    Object localObject1 = paramNodeAVL;
    for (Object localObject2 = localObject1; localObject2 != null; localObject2 = ((NodeAVL)localObject1).getLeft(null)) {
      localObject1 = localObject2;
    }
    while (localObject1 != null)
    {
      NodeAVL localNodeAVL = nextUnlink((NodeAVL)localObject1);
      localObject1 = localNodeAVL;
    }
  }
  
  private NodeAVL nextUnlink(NodeAVL paramNodeAVL)
  {
    NodeAVL localNodeAVL = paramNodeAVL.getRight(null);
    if (localNodeAVL != null)
    {
      paramNodeAVL = localNodeAVL;
      for (localNodeAVL = paramNodeAVL.getLeft(null); localNodeAVL != null; localNodeAVL = paramNodeAVL.getLeft(null)) {
        paramNodeAVL = localNodeAVL;
      }
      return paramNodeAVL;
    }
    localNodeAVL = paramNodeAVL;
    for (paramNodeAVL = paramNodeAVL.getParent(null); (paramNodeAVL != null) && (paramNodeAVL.isRight(localNodeAVL)); paramNodeAVL = paramNodeAVL.getParent(null))
    {
      paramNodeAVL.nRight = null;
      localNodeAVL.getRow(null).destroy();
      localNodeAVL.delete();
      localNodeAVL = paramNodeAVL;
    }
    if (paramNodeAVL != null) {
      paramNodeAVL.nLeft = null;
    }
    localNodeAVL.getRow(null).destroy();
    localNodeAVL.delete();
    return paramNodeAVL;
  }
  
  public void checkIndex(PersistentStore paramPersistentStore)
  {
    paramPersistentStore.readLock();
    try
    {
      Object localObject1 = getAccessor(paramPersistentStore);
      Object localObject2 = null;
      while (localObject1 != null)
      {
        localObject2 = localObject1;
        checkNodes(paramPersistentStore, (NodeAVL)localObject1);
        localObject1 = ((NodeAVL)localObject1).getLeft(paramPersistentStore);
      }
      localObject1 = localObject2;
      while (localObject2 != null)
      {
        checkNodes(paramPersistentStore, (NodeAVL)localObject2);
        localObject2 = next(paramPersistentStore, (NodeAVL)localObject2);
      }
    }
    finally
    {
      paramPersistentStore.readUnlock();
    }
  }
  
  void checkNodes(PersistentStore paramPersistentStore, NodeAVL paramNodeAVL)
  {
    NodeAVL localNodeAVL1 = paramNodeAVL.getLeft(paramPersistentStore);
    NodeAVL localNodeAVL2 = paramNodeAVL.getRight(paramPersistentStore);
    if ((localNodeAVL1 != null) && (localNodeAVL1.getBalance(paramPersistentStore) == -2)) {
      System.out.print("broken index - deleted");
    }
    if ((localNodeAVL2 != null) && (localNodeAVL2.getBalance(paramPersistentStore) == -2)) {
      System.out.print("broken index -deleted");
    }
    if ((localNodeAVL1 != null) && (!paramNodeAVL.equals(localNodeAVL1.getParent(paramPersistentStore)))) {
      System.out.print("broken index - no parent");
    }
    if ((localNodeAVL2 != null) && (!paramNodeAVL.equals(localNodeAVL2.getParent(paramPersistentStore)))) {
      System.out.print("broken index - no parent");
    }
  }
  
  public int compareRowNonUnique(Session paramSession, Object[] paramArrayOfObject1, Object[] paramArrayOfObject2, int[] paramArrayOfInt)
  {
    int i = paramArrayOfInt.length;
    for (int j = 0; j < i; j++)
    {
      int k = this.colTypes[j].compare(paramSession, paramArrayOfObject1[this.colIndex[j]], paramArrayOfObject2[paramArrayOfInt[j]]);
      if (k != 0) {
        return k;
      }
    }
    return 0;
  }
  
  public int compareRowNonUnique(Session paramSession, Object[] paramArrayOfObject1, Object[] paramArrayOfObject2, int[] paramArrayOfInt, int paramInt)
  {
    for (int i = 0; i < paramInt; i++)
    {
      int j = this.colTypes[i].compare(paramSession, paramArrayOfObject1[this.colIndex[i]], paramArrayOfObject2[paramArrayOfInt[i]]);
      if (j != 0) {
        return j;
      }
    }
    return 0;
  }
  
  public int compareRowNonUnique(Session paramSession, Object[] paramArrayOfObject1, Object[] paramArrayOfObject2, int paramInt)
  {
    for (int i = 0; i < paramInt; i++)
    {
      int j = this.colTypes[i].compare(paramSession, paramArrayOfObject1[this.colIndex[i]], paramArrayOfObject2[this.colIndex[i]]);
      if (j != 0) {
        return j;
      }
    }
    return 0;
  }
  
  public void compareRowForChange(Session paramSession, Object[] paramArrayOfObject1, Object[] paramArrayOfObject2, double[] paramArrayOfDouble)
  {
    for (int i = 0; i < this.colIndex.length; i++)
    {
      int j = this.colTypes[i].compare(paramSession, paramArrayOfObject1[this.colIndex[i]], paramArrayOfObject2[this.colIndex[i]]);
      if (j != 0) {
        while (i < this.colIndex.length)
        {
          paramArrayOfDouble[i] += 1.0D;
          i++;
        }
      }
    }
  }
  
  public int compareRow(Session paramSession, Object[] paramArrayOfObject1, Object[] paramArrayOfObject2)
  {
    for (int i = 0; i < this.colIndex.length; i++)
    {
      int j = this.colTypes[i].compare(paramSession, paramArrayOfObject1[this.colIndex[i]], paramArrayOfObject2[this.colIndex[i]]);
      if (j != 0)
      {
        if (this.isSimpleOrder) {
          return j;
        }
        int k = (paramArrayOfObject1[this.colIndex[i]] == null) || (paramArrayOfObject2[this.colIndex[i]] == null) ? 1 : 0;
        if ((this.colDesc[i] != 0) && (k == 0)) {
          j = -j;
        }
        if ((this.nullsLast[i] != 0) && (k != 0)) {
          j = -j;
        }
        return j;
      }
    }
    return 0;
  }
  
  int compareRowForInsertOrDelete(Session paramSession, Row paramRow1, Row paramRow2, boolean paramBoolean, int paramInt)
  {
    Object[] arrayOfObject1 = paramRow1.getData();
    Object[] arrayOfObject2 = paramRow2.getData();
    for (int i = paramInt; i < this.colIndex.length; i++)
    {
      int j = this.colTypes[i].compare(paramSession, arrayOfObject1[this.colIndex[i]], arrayOfObject2[this.colIndex[i]]);
      if (j != 0)
      {
        if (this.isSimpleOrder) {
          return j;
        }
        int k = (arrayOfObject1[this.colIndex[i]] == null) || (arrayOfObject2[this.colIndex[i]] == null) ? 1 : 0;
        if ((this.colDesc[i] != 0) && (k == 0)) {
          j = -j;
        }
        if ((this.nullsLast[i] != 0) && (k != 0)) {
          j = -j;
        }
        return j;
      }
    }
    if (paramBoolean)
    {
      long l = paramRow1.getPos() - paramRow2.getPos();
      return l > 0L ? 1 : l == 0L ? 0 : -1;
    }
    return 0;
  }
  
  int compareObject(Session paramSession, Object[] paramArrayOfObject1, Object[] paramArrayOfObject2, int[] paramArrayOfInt, int paramInt1, int paramInt2)
  {
    return this.colTypes[paramInt1].compare(paramSession, paramArrayOfObject1[this.colIndex[paramInt1]], paramArrayOfObject2[paramArrayOfInt[paramInt1]], paramInt2);
  }
  
  boolean hasNulls(Session paramSession, Object[] paramArrayOfObject)
  {
    int i = (paramSession == null) || (paramSession.database.sqlUniqueNulls) ? 1 : 0;
    boolean bool = false;
    for (int j = 0; j < this.colIndex.length; j++) {
      if (paramArrayOfObject[this.colIndex[j]] == null)
      {
        bool = true;
        if (i != 0) {
          break;
        }
      }
      else if (i == 0)
      {
        bool = false;
        break;
      }
    }
    return bool;
  }
  
  public void insert(Session paramSession, PersistentStore paramPersistentStore, Row paramRow)
  {
    boolean bool1 = true;
    int i = -1;
    boolean bool2 = (!this.isUnique) || (hasNulls(paramSession, paramRow.getData()));
    NodeAVL localNodeAVL1 = getAccessor(paramPersistentStore);
    NodeAVL localNodeAVL2 = localNodeAVL1;
    if (localNodeAVL1 == null)
    {
      paramPersistentStore.setAccessor(this, ((RowAVL)paramRow).getNode(this.position));
      return;
    }
    for (;;)
    {
      RowAVL localRowAVL = localNodeAVL1.getRow(paramPersistentStore);
      i = compareRowForInsertOrDelete(paramSession, paramRow, localRowAVL, bool2, 0);
      if ((i == 0) && (paramSession != null) && (!bool2) && (paramSession.database.txManager.isMVRows()) && (!isEqualReadable(paramSession, paramPersistentStore, localNodeAVL1)))
      {
        bool2 = true;
        i = compareRowForInsertOrDelete(paramSession, paramRow, localRowAVL, bool2, this.colIndex.length);
      }
      if (i == 0)
      {
        Constraint localConstraint = null;
        if (this.isConstraint) {
          localConstraint = ((Table)this.table).getUniqueConstraintForIndex(this);
        }
        if (localConstraint == null) {
          throw Error.error(104, this.name.statementName);
        }
        throw localConstraint.getException(paramRow.getData());
      }
      bool1 = i < 0;
      localNodeAVL2 = localNodeAVL1;
      localNodeAVL1 = localNodeAVL2.child(paramPersistentStore, bool1);
      if (localNodeAVL1 == null) {
        break;
      }
    }
    localNodeAVL2 = localNodeAVL2.set(paramPersistentStore, bool1, ((RowAVL)paramRow).getNode(this.position));
    balance(paramPersistentStore, localNodeAVL2, bool1);
  }
  
  public void delete(Session paramSession, PersistentStore paramPersistentStore, Row paramRow)
  {
    paramRow = (Row)paramPersistentStore.get(paramRow, false);
    Object localObject1 = ((RowAVL)paramRow).getNode(this.position);
    if (localObject1 == null) {
      return;
    }
    int i;
    NodeAVL localNodeAVL3;
    NodeAVL localNodeAVL5;
    if (((NodeAVL)localObject1).getLeft(paramPersistentStore) == null)
    {
      localNodeAVL1 = ((NodeAVL)localObject1).getRight(paramPersistentStore);
    }
    else if (((NodeAVL)localObject1).getRight(paramPersistentStore) == null)
    {
      localNodeAVL1 = ((NodeAVL)localObject1).getLeft(paramPersistentStore);
    }
    else
    {
      Object localObject2 = localObject1;
      NodeAVL localNodeAVL2;
      for (localObject1 = ((NodeAVL)localObject1).getLeft(paramPersistentStore);; localObject1 = localNodeAVL2)
      {
        localNodeAVL2 = ((NodeAVL)localObject1).getRight(paramPersistentStore);
        if (localNodeAVL2 == null) {
          break;
        }
      }
      localNodeAVL1 = ((NodeAVL)localObject1).getLeft(paramPersistentStore);
      i = ((NodeAVL)localObject1).getBalance(paramPersistentStore);
      localObject1 = ((NodeAVL)localObject1).setBalance(paramPersistentStore, ((NodeAVL)localObject2).getBalance(paramPersistentStore));
      localObject2 = ((NodeAVL)localObject2).setBalance(paramPersistentStore, i);
      localNodeAVL3 = ((NodeAVL)localObject1).getParent(paramPersistentStore);
      NodeAVL localNodeAVL4 = ((NodeAVL)localObject2).getParent(paramPersistentStore);
      if (((NodeAVL)localObject2).isRoot(paramPersistentStore)) {
        paramPersistentStore.setAccessor(this, (CachedObject)localObject1);
      }
      localObject1 = ((NodeAVL)localObject1).setParent(paramPersistentStore, localNodeAVL4);
      if (localNodeAVL4 != null) {
        if (localNodeAVL4.isRight((NodeAVL)localObject2)) {
          localNodeAVL4 = localNodeAVL4.setRight(paramPersistentStore, (NodeAVL)localObject1);
        } else {
          localNodeAVL4 = localNodeAVL4.setLeft(paramPersistentStore, (NodeAVL)localObject1);
        }
      }
      if (((NodeAVL)localObject2).equals(localNodeAVL3))
      {
        localObject2 = ((NodeAVL)localObject2).setParent(paramPersistentStore, (NodeAVL)localObject1);
        if (((NodeAVL)localObject2).isLeft((NodeAVL)localObject1))
        {
          localObject1 = ((NodeAVL)localObject1).setLeft(paramPersistentStore, (NodeAVL)localObject2);
          localNodeAVL5 = ((NodeAVL)localObject2).getRight(paramPersistentStore);
          localObject1 = ((NodeAVL)localObject1).setRight(paramPersistentStore, localNodeAVL5);
        }
        else
        {
          localObject1 = ((NodeAVL)localObject1).setRight(paramPersistentStore, (NodeAVL)localObject2);
          localNodeAVL5 = ((NodeAVL)localObject2).getLeft(paramPersistentStore);
          localObject1 = ((NodeAVL)localObject1).setLeft(paramPersistentStore, localNodeAVL5);
        }
      }
      else
      {
        localObject2 = ((NodeAVL)localObject2).setParent(paramPersistentStore, localNodeAVL3);
        localNodeAVL3 = localNodeAVL3.setRight(paramPersistentStore, (NodeAVL)localObject2);
        localNodeAVL5 = ((NodeAVL)localObject2).getLeft(paramPersistentStore);
        NodeAVL localNodeAVL6 = ((NodeAVL)localObject2).getRight(paramPersistentStore);
        localObject1 = ((NodeAVL)localObject1).setLeft(paramPersistentStore, localNodeAVL5);
        localObject1 = ((NodeAVL)localObject1).setRight(paramPersistentStore, localNodeAVL6);
      }
      ((NodeAVL)localObject1).getRight(paramPersistentStore).setParent(paramPersistentStore, (NodeAVL)localObject1);
      ((NodeAVL)localObject1).getLeft(paramPersistentStore).setParent(paramPersistentStore, (NodeAVL)localObject1);
      localObject2 = ((NodeAVL)localObject2).setLeft(paramPersistentStore, localNodeAVL1);
      if (localNodeAVL1 != null) {
        localNodeAVL1 = localNodeAVL1.setParent(paramPersistentStore, (NodeAVL)localObject2);
      }
      localObject2 = ((NodeAVL)localObject2).setRight(paramPersistentStore, null);
      localObject1 = localObject2;
    }
    boolean bool = ((NodeAVL)localObject1).isFromLeft(paramPersistentStore);
    ((NodeAVL)localObject1).replace(paramPersistentStore, this, localNodeAVL1);
    NodeAVL localNodeAVL1 = ((NodeAVL)localObject1).getParent(paramPersistentStore);
    ((NodeAVL)localObject1).delete();
    while (localNodeAVL1 != null)
    {
      localObject1 = localNodeAVL1;
      i = bool ? 1 : -1;
      switch (((NodeAVL)localObject1).getBalance(paramPersistentStore) * i)
      {
      case -1: 
        localObject1 = ((NodeAVL)localObject1).setBalance(paramPersistentStore, 0);
        break;
      case 0: 
        localObject1 = ((NodeAVL)localObject1).setBalance(paramPersistentStore, i);
        return;
      case 1: 
        localNodeAVL3 = ((NodeAVL)localObject1).child(paramPersistentStore, !bool);
        int j = localNodeAVL3.getBalance(paramPersistentStore);
        if (j * i >= 0)
        {
          ((NodeAVL)localObject1).replace(paramPersistentStore, this, localNodeAVL3);
          localNodeAVL5 = localNodeAVL3.child(paramPersistentStore, bool);
          localObject1 = ((NodeAVL)localObject1).set(paramPersistentStore, !bool, localNodeAVL5);
          localNodeAVL3 = localNodeAVL3.set(paramPersistentStore, bool, (NodeAVL)localObject1);
          if (j == 0)
          {
            localObject1 = ((NodeAVL)localObject1).setBalance(paramPersistentStore, i);
            localNodeAVL3 = localNodeAVL3.setBalance(paramPersistentStore, -i);
            return;
          }
          localObject1 = ((NodeAVL)localObject1).setBalance(paramPersistentStore, 0);
          localNodeAVL3 = localNodeAVL3.setBalance(paramPersistentStore, 0);
          localObject1 = localNodeAVL3;
        }
        else
        {
          localNodeAVL5 = localNodeAVL3.child(paramPersistentStore, bool);
          ((NodeAVL)localObject1).replace(paramPersistentStore, this, localNodeAVL5);
          j = localNodeAVL5.getBalance(paramPersistentStore);
          localNodeAVL3 = localNodeAVL3.set(paramPersistentStore, bool, localNodeAVL5.child(paramPersistentStore, !bool));
          localNodeAVL5 = localNodeAVL5.set(paramPersistentStore, !bool, localNodeAVL3);
          localObject1 = ((NodeAVL)localObject1).set(paramPersistentStore, !bool, localNodeAVL5.child(paramPersistentStore, bool));
          localNodeAVL5 = localNodeAVL5.set(paramPersistentStore, bool, (NodeAVL)localObject1);
          localObject1 = ((NodeAVL)localObject1).setBalance(paramPersistentStore, j == i ? -i : 0);
          localNodeAVL3 = localNodeAVL3.setBalance(paramPersistentStore, j == -i ? i : 0);
          localNodeAVL5 = localNodeAVL5.setBalance(paramPersistentStore, 0);
          localObject1 = localNodeAVL5;
        }
        break;
      }
      bool = ((NodeAVL)localObject1).isFromLeft(paramPersistentStore);
      localNodeAVL1 = ((NodeAVL)localObject1).getParent(paramPersistentStore);
    }
  }
  
  public boolean existsParent(Session paramSession, PersistentStore paramPersistentStore, Object[] paramArrayOfObject, int[] paramArrayOfInt)
  {
    NodeAVL localNodeAVL = findNode(paramSession, paramPersistentStore, paramArrayOfObject, paramArrayOfInt, paramArrayOfInt.length, 40, 2, false);
    return localNodeAVL != null;
  }
  
  public RowIterator findFirstRow(Session paramSession, PersistentStore paramPersistentStore, Object[] paramArrayOfObject, int paramInt1, int paramInt2, int paramInt3, boolean paramBoolean, boolean[] paramArrayOfBoolean)
  {
    NodeAVL localNodeAVL = findNode(paramSession, paramPersistentStore, paramArrayOfObject, this.defaultColMap, paramInt1, paramInt3, 0, paramBoolean);
    if (localNodeAVL == null) {
      return emptyIterator;
    }
    return new IndexRowIterator(paramSession, paramPersistentStore, this, localNodeAVL, paramInt2, false, paramBoolean);
  }
  
  public RowIterator findFirstRow(Session paramSession, PersistentStore paramPersistentStore, Object[] paramArrayOfObject)
  {
    NodeAVL localNodeAVL = findNode(paramSession, paramPersistentStore, paramArrayOfObject, this.colIndex, this.colIndex.length, 40, 0, false);
    if (localNodeAVL == null) {
      return emptyIterator;
    }
    return new IndexRowIterator(paramSession, paramPersistentStore, this, localNodeAVL, 0, false, false);
  }
  
  public RowIterator findFirstRow(Session paramSession, PersistentStore paramPersistentStore, Object[] paramArrayOfObject, int[] paramArrayOfInt)
  {
    NodeAVL localNodeAVL = findNode(paramSession, paramPersistentStore, paramArrayOfObject, paramArrayOfInt, paramArrayOfInt.length, 40, 0, false);
    if (localNodeAVL == null) {
      return emptyIterator;
    }
    return new IndexRowIterator(paramSession, paramPersistentStore, this, localNodeAVL, 0, false, false);
  }
  
  public RowIterator findFirstRowNotNull(Session paramSession, PersistentStore paramPersistentStore)
  {
    NodeAVL localNodeAVL = findNode(paramSession, paramPersistentStore, this.nullData, this.defaultColMap, 1, 48, 0, false);
    if (localNodeAVL == null) {
      return emptyIterator;
    }
    return new IndexRowIterator(paramSession, paramPersistentStore, this, localNodeAVL, 0, false, false);
  }
  
  public RowIterator firstRow(Session paramSession, PersistentStore paramPersistentStore, int paramInt, boolean[] paramArrayOfBoolean)
  {
    paramPersistentStore.readLock();
    try
    {
      Object localObject1 = getAccessor(paramPersistentStore);
      for (Object localObject2 = localObject1; localObject2 != null; localObject2 = ((NodeAVL)localObject1).getLeft(paramPersistentStore)) {
        localObject1 = localObject2;
      }
      while ((paramSession != null) && (localObject1 != null))
      {
        localObject3 = ((NodeAVL)localObject1).getRow(paramPersistentStore);
        if (paramSession.database.txManager.canRead(paramSession, paramPersistentStore, (Row)localObject3, 0, null)) {
          break;
        }
        localObject1 = next(paramPersistentStore, (NodeAVL)localObject1);
      }
      if (localObject1 == null)
      {
        localObject3 = emptyIterator;
        return (RowIterator)localObject3;
      }
      Object localObject3 = new IndexRowIterator(paramSession, paramPersistentStore, this, (NodeAVL)localObject1, paramInt, false, false);
      return (RowIterator)localObject3;
    }
    finally
    {
      paramPersistentStore.readUnlock();
    }
  }
  
  public RowIterator firstRow(PersistentStore paramPersistentStore)
  {
    paramPersistentStore.readLock();
    try
    {
      Object localObject1 = getAccessor(paramPersistentStore);
      for (Object localObject2 = localObject1; localObject2 != null; localObject2 = ((NodeAVL)localObject1).getLeft(paramPersistentStore)) {
        localObject1 = localObject2;
      }
      if (localObject1 == null)
      {
        localIndexRowIterator = emptyIterator;
        return localIndexRowIterator;
      }
      IndexRowIterator localIndexRowIterator = new IndexRowIterator(null, paramPersistentStore, this, (NodeAVL)localObject1, 0, false, false);
      return localIndexRowIterator;
    }
    finally
    {
      paramPersistentStore.readUnlock();
    }
  }
  
  public RowIterator lastRow(Session paramSession, PersistentStore paramPersistentStore, int paramInt, boolean[] paramArrayOfBoolean)
  {
    paramPersistentStore.readLock();
    try
    {
      Object localObject1 = getAccessor(paramPersistentStore);
      for (Object localObject2 = localObject1; localObject2 != null; localObject2 = ((NodeAVL)localObject1).getRight(paramPersistentStore)) {
        localObject1 = localObject2;
      }
      while ((paramSession != null) && (localObject1 != null))
      {
        localObject3 = ((NodeAVL)localObject1).getRow(paramPersistentStore);
        if (paramSession.database.txManager.canRead(paramSession, paramPersistentStore, (Row)localObject3, 0, null)) {
          break;
        }
        localObject1 = last(paramPersistentStore, (NodeAVL)localObject1);
      }
      if (localObject1 == null)
      {
        localObject3 = emptyIterator;
        return (RowIterator)localObject3;
      }
      Object localObject3 = new IndexRowIterator(paramSession, paramPersistentStore, this, (NodeAVL)localObject1, paramInt, false, true);
      return (RowIterator)localObject3;
    }
    finally
    {
      paramPersistentStore.readUnlock();
    }
  }
  
  NodeAVL next(Session paramSession, PersistentStore paramPersistentStore, NodeAVL paramNodeAVL, int paramInt)
  {
    if (paramNodeAVL == null) {
      return null;
    }
    if (paramInt != 0) {
      return findDistinctNode(paramSession, paramPersistentStore, paramNodeAVL, paramInt, false);
    }
    for (;;)
    {
      paramNodeAVL = next(paramPersistentStore, paramNodeAVL);
      if (paramNodeAVL == null) {
        return paramNodeAVL;
      }
      if (paramSession == null) {
        return paramNodeAVL;
      }
      RowAVL localRowAVL = paramNodeAVL.getRow(paramPersistentStore);
      if (paramSession.database.txManager.canRead(paramSession, paramPersistentStore, localRowAVL, 0, null)) {
        return paramNodeAVL;
      }
    }
  }
  
  NodeAVL last(Session paramSession, PersistentStore paramPersistentStore, NodeAVL paramNodeAVL, int paramInt)
  {
    if (paramNodeAVL == null) {
      return null;
    }
    if (paramInt != 0) {
      return findDistinctNode(paramSession, paramPersistentStore, paramNodeAVL, paramInt, true);
    }
    for (;;)
    {
      paramNodeAVL = last(paramPersistentStore, paramNodeAVL);
      if (paramNodeAVL == null) {
        return paramNodeAVL;
      }
      if (paramSession == null) {
        return paramNodeAVL;
      }
      RowAVL localRowAVL = paramNodeAVL.getRow(paramPersistentStore);
      if (paramSession.database.txManager.canRead(paramSession, paramPersistentStore, localRowAVL, 0, null)) {
        return paramNodeAVL;
      }
    }
  }
  
  NodeAVL next(PersistentStore paramPersistentStore, NodeAVL paramNodeAVL)
  {
    if (paramNodeAVL == null) {
      return null;
    }
    RowAVL localRowAVL = paramNodeAVL.getRow(paramPersistentStore);
    paramNodeAVL = localRowAVL.getNode(this.position);
    NodeAVL localNodeAVL = paramNodeAVL.getRight(paramPersistentStore);
    if (localNodeAVL != null)
    {
      paramNodeAVL = localNodeAVL;
      for (localNodeAVL = paramNodeAVL.getLeft(paramPersistentStore); localNodeAVL != null; localNodeAVL = paramNodeAVL.getLeft(paramPersistentStore)) {
        paramNodeAVL = localNodeAVL;
      }
      return paramNodeAVL;
    }
    localNodeAVL = paramNodeAVL;
    for (paramNodeAVL = paramNodeAVL.getParent(paramPersistentStore); (paramNodeAVL != null) && (paramNodeAVL.isRight(localNodeAVL)); paramNodeAVL = paramNodeAVL.getParent(paramPersistentStore)) {
      localNodeAVL = paramNodeAVL;
    }
    return paramNodeAVL;
  }
  
  NodeAVL next(PersistentStore paramPersistentStore, NodeAVL paramNodeAVL, int paramInt1, int paramInt2, int[] paramArrayOfInt)
  {
    NodeAVL localNodeAVL = paramInt1 == paramInt2 ? null : paramNodeAVL.getRight(paramPersistentStore);
    if (localNodeAVL != null)
    {
      paramInt1++;
      paramNodeAVL = localNodeAVL;
      localNodeAVL = paramInt1 == paramInt2 ? null : paramNodeAVL.getLeft(paramPersistentStore);
      while (localNodeAVL != null)
      {
        paramInt1++;
        paramNodeAVL = localNodeAVL;
        if (paramInt1 == paramInt2) {
          localNodeAVL = null;
        } else {
          localNodeAVL = paramNodeAVL.getLeft(paramPersistentStore);
        }
      }
      paramArrayOfInt[0] = paramInt1;
      return paramNodeAVL;
    }
    localNodeAVL = paramNodeAVL;
    paramNodeAVL = paramNodeAVL.getParent(paramPersistentStore);
    paramInt1--;
    while ((paramNodeAVL != null) && (paramNodeAVL.isRight(localNodeAVL)))
    {
      localNodeAVL = paramNodeAVL;
      paramNodeAVL = paramNodeAVL.getParent(paramPersistentStore);
      paramInt1--;
    }
    paramArrayOfInt[0] = paramInt1;
    return paramNodeAVL;
  }
  
  NodeAVL last(PersistentStore paramPersistentStore, NodeAVL paramNodeAVL)
  {
    if (paramNodeAVL == null) {
      return null;
    }
    RowAVL localRowAVL = paramNodeAVL.getRow(paramPersistentStore);
    paramNodeAVL = localRowAVL.getNode(this.position);
    NodeAVL localNodeAVL = paramNodeAVL.getLeft(paramPersistentStore);
    if (localNodeAVL != null)
    {
      paramNodeAVL = localNodeAVL;
      for (localNodeAVL = paramNodeAVL.getRight(paramPersistentStore); localNodeAVL != null; localNodeAVL = paramNodeAVL.getRight(paramPersistentStore)) {
        paramNodeAVL = localNodeAVL;
      }
      return paramNodeAVL;
    }
    localNodeAVL = paramNodeAVL;
    for (paramNodeAVL = paramNodeAVL.getParent(paramPersistentStore); (paramNodeAVL != null) && (paramNodeAVL.isLeft(localNodeAVL)); paramNodeAVL = paramNodeAVL.getParent(paramPersistentStore)) {
      localNodeAVL = paramNodeAVL;
    }
    return paramNodeAVL;
  }
  
  boolean isEqualReadable(Session paramSession, PersistentStore paramPersistentStore, NodeAVL paramNodeAVL)
  {
    NodeAVL localNodeAVL = paramNodeAVL;
    RowAVL localRowAVL = paramNodeAVL.getRow(paramPersistentStore);
    paramSession.database.txManager.setTransactionInfo(paramPersistentStore, localRowAVL);
    if (paramSession.database.txManager.canRead(paramSession, paramPersistentStore, localRowAVL, 1, null)) {
      return true;
    }
    Object[] arrayOfObject1 = paramNodeAVL.getData(paramPersistentStore);
    Object[] arrayOfObject2;
    do
    {
      localNodeAVL = last(paramPersistentStore, localNodeAVL);
      if (localNodeAVL == null) {
        break;
      }
      arrayOfObject2 = localNodeAVL.getData(paramPersistentStore);
      if (compareRow(paramSession, arrayOfObject1, arrayOfObject2) != 0) {
        break;
      }
      localRowAVL = localNodeAVL.getRow(paramPersistentStore);
      paramSession.database.txManager.setTransactionInfo(paramPersistentStore, localRowAVL);
    } while (!paramSession.database.txManager.canRead(paramSession, paramPersistentStore, localRowAVL, 1, null));
    return true;
    do
    {
      localNodeAVL = next(paramSession, paramPersistentStore, paramNodeAVL, 0);
      if (localNodeAVL == null) {
        break;
      }
      arrayOfObject2 = localNodeAVL.getData(paramPersistentStore);
      if (compareRow(paramSession, arrayOfObject1, arrayOfObject2) != 0) {
        break;
      }
      localRowAVL = localNodeAVL.getRow(paramPersistentStore);
      paramSession.database.txManager.setTransactionInfo(paramPersistentStore, localRowAVL);
    } while (!paramSession.database.txManager.canRead(paramSession, paramPersistentStore, localRowAVL, 1, null));
    return true;
    return false;
  }
  
  NodeAVL findNode(Session paramSession, PersistentStore paramPersistentStore, Object[] paramArrayOfObject, int[] paramArrayOfInt, int paramInt1, int paramInt2, int paramInt3, boolean paramBoolean)
  {
    paramPersistentStore.readLock();
    try
    {
      Object localObject1 = getAccessor(paramPersistentStore);
      NodeAVL localNodeAVL = null;
      Object localObject2 = null;
      RowAVL localRowAVL = null;
      if ((paramInt2 != 40) && (paramInt2 != 47))
      {
        paramInt1--;
        if ((paramInt2 == 44) || (paramInt2 == 45) || (paramInt2 == 74)) {
          paramBoolean = true;
        }
      }
      while (localObject1 != null)
      {
        localRowAVL = ((NodeAVL)localObject1).getRow(paramPersistentStore);
        int i = 0;
        if (paramInt1 > 0) {
          i = compareRowNonUnique(paramSession, localRowAVL.getData(), paramArrayOfObject, paramArrayOfInt, paramInt1);
        }
        if (i == 0) {
          switch (paramInt2)
          {
          case 40: 
          case 47: 
          case 74: 
            localObject2 = localObject1;
            if (paramBoolean) {
              localNodeAVL = ((NodeAVL)localObject1).getRight(paramPersistentStore);
            } else {
              localNodeAVL = ((NodeAVL)localObject1).getLeft(paramPersistentStore);
            }
            break;
          case 43: 
          case 48: 
            i = compareObject(paramSession, localRowAVL.getData(), paramArrayOfObject, paramArrayOfInt, paramInt1, paramInt2);
            if (i <= 0)
            {
              localNodeAVL = ((NodeAVL)localObject1).getRight(paramPersistentStore);
            }
            else
            {
              localObject2 = localObject1;
              localNodeAVL = ((NodeAVL)localObject1).getLeft(paramPersistentStore);
            }
            break;
          case 41: 
          case 42: 
            i = compareObject(paramSession, localRowAVL.getData(), paramArrayOfObject, paramArrayOfInt, paramInt1, paramInt2);
            if (i < 0)
            {
              localNodeAVL = ((NodeAVL)localObject1).getRight(paramPersistentStore);
            }
            else
            {
              localObject2 = localObject1;
              localNodeAVL = ((NodeAVL)localObject1).getLeft(paramPersistentStore);
            }
            break;
          case 44: 
            i = compareObject(paramSession, localRowAVL.getData(), paramArrayOfObject, paramArrayOfInt, paramInt1, paramInt2);
            if (i < 0)
            {
              localObject2 = localObject1;
              localNodeAVL = ((NodeAVL)localObject1).getRight(paramPersistentStore);
            }
            else
            {
              localNodeAVL = ((NodeAVL)localObject1).getLeft(paramPersistentStore);
            }
            break;
          case 45: 
            i = compareObject(paramSession, localRowAVL.getData(), paramArrayOfObject, paramArrayOfInt, paramInt1, paramInt2);
            if (i <= 0)
            {
              localObject2 = localObject1;
              localNodeAVL = ((NodeAVL)localObject1).getRight(paramPersistentStore);
            }
            else
            {
              localNodeAVL = ((NodeAVL)localObject1).getLeft(paramPersistentStore);
            }
            break;
          case 46: 
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
          case 71: 
          case 72: 
          case 73: 
          default: 
            throw Error.runtimeError(201, "Index");
          }
        } else if (i < 0) {
          localNodeAVL = ((NodeAVL)localObject1).getRight(paramPersistentStore);
        } else if (i > 0) {
          localNodeAVL = ((NodeAVL)localObject1).getLeft(paramPersistentStore);
        }
        if (localNodeAVL == null) {
          break;
        }
        localObject1 = localNodeAVL;
      }
      if (paramSession == null)
      {
        localObject3 = localObject2;
        return (NodeAVL)localObject3;
      }
      while (localObject2 != null)
      {
        localRowAVL = ((NodeAVL)localObject2).getRow(paramPersistentStore);
        if (!paramSession.database.txManager.canRead(paramSession, paramPersistentStore, localRowAVL, paramInt3, this.colIndex))
        {
          localObject2 = paramBoolean ? last(paramPersistentStore, (NodeAVL)localObject2) : next(paramPersistentStore, (NodeAVL)localObject2);
          if (localObject2 != null)
          {
            localRowAVL = ((NodeAVL)localObject2).getRow(paramPersistentStore);
            if ((paramInt1 > 0) && (compareRowNonUnique(paramSession, localRowAVL.getData(), paramArrayOfObject, paramArrayOfInt, paramInt1) != 0)) {
              localObject2 = null;
            }
          }
        }
      }
      Object localObject3 = localObject2;
      return (NodeAVL)localObject3;
    }
    finally
    {
      paramPersistentStore.readUnlock();
    }
  }
  
  NodeAVL findDistinctNode(Session paramSession, PersistentStore paramPersistentStore, NodeAVL paramNodeAVL, int paramInt, boolean paramBoolean)
  {
    paramPersistentStore.readLock();
    try
    {
      Object localObject1 = getAccessor(paramPersistentStore);
      NodeAVL localNodeAVL = null;
      Object localObject2 = null;
      RowAVL localRowAVL = null;
      Object[] arrayOfObject = paramNodeAVL.getData(paramPersistentStore);
      while (localObject1 != null)
      {
        localRowAVL = ((NodeAVL)localObject1).getRow(paramPersistentStore);
        int i = 0;
        i = compareRowNonUnique(paramSession, localRowAVL.getData(), arrayOfObject, this.colIndex, paramInt);
        if (paramBoolean)
        {
          if (i < 0)
          {
            localObject2 = localObject1;
            localNodeAVL = ((NodeAVL)localObject1).getRight(paramPersistentStore);
          }
          else
          {
            localNodeAVL = ((NodeAVL)localObject1).getLeft(paramPersistentStore);
          }
        }
        else if (i <= 0)
        {
          localNodeAVL = ((NodeAVL)localObject1).getRight(paramPersistentStore);
        }
        else
        {
          localObject2 = localObject1;
          localNodeAVL = ((NodeAVL)localObject1).getLeft(paramPersistentStore);
        }
        if (localNodeAVL == null) {
          break;
        }
        localObject1 = localNodeAVL;
      }
      if (paramSession == null)
      {
        localObject3 = localObject2;
        return (NodeAVL)localObject3;
      }
      while (localObject2 != null)
      {
        localRowAVL = ((NodeAVL)localObject2).getRow(paramPersistentStore);
        if (paramSession.database.txManager.canRead(paramSession, paramPersistentStore, localRowAVL, 0, this.colIndex)) {
          break;
        }
        localObject2 = paramBoolean ? last(paramPersistentStore, (NodeAVL)localObject2) : next(paramPersistentStore, (NodeAVL)localObject2);
      }
      Object localObject3 = localObject2;
      return (NodeAVL)localObject3;
    }
    finally
    {
      paramPersistentStore.readUnlock();
    }
  }
  
  void balance(PersistentStore paramPersistentStore, NodeAVL paramNodeAVL, boolean paramBoolean)
  {
    for (;;)
    {
      int i = paramBoolean ? 1 : -1;
      switch (paramNodeAVL.getBalance(paramPersistentStore) * i)
      {
      case 1: 
        paramNodeAVL = paramNodeAVL.setBalance(paramPersistentStore, 0);
        return;
      case 0: 
        paramNodeAVL = paramNodeAVL.setBalance(paramPersistentStore, -i);
        break;
      case -1: 
        NodeAVL localNodeAVL1 = paramNodeAVL.child(paramPersistentStore, paramBoolean);
        if (localNodeAVL1.getBalance(paramPersistentStore) == -i)
        {
          paramNodeAVL.replace(paramPersistentStore, this, localNodeAVL1);
          paramNodeAVL = paramNodeAVL.set(paramPersistentStore, paramBoolean, localNodeAVL1.child(paramPersistentStore, !paramBoolean));
          localNodeAVL1 = localNodeAVL1.set(paramPersistentStore, !paramBoolean, paramNodeAVL);
          paramNodeAVL = paramNodeAVL.setBalance(paramPersistentStore, 0);
          localNodeAVL1 = localNodeAVL1.setBalance(paramPersistentStore, 0);
        }
        else
        {
          NodeAVL localNodeAVL2 = localNodeAVL1.child(paramPersistentStore, !paramBoolean);
          paramNodeAVL.replace(paramPersistentStore, this, localNodeAVL2);
          localNodeAVL1 = localNodeAVL1.set(paramPersistentStore, !paramBoolean, localNodeAVL2.child(paramPersistentStore, paramBoolean));
          localNodeAVL2 = localNodeAVL2.set(paramPersistentStore, paramBoolean, localNodeAVL1);
          paramNodeAVL = paramNodeAVL.set(paramPersistentStore, paramBoolean, localNodeAVL2.child(paramPersistentStore, !paramBoolean));
          localNodeAVL2 = localNodeAVL2.set(paramPersistentStore, !paramBoolean, paramNodeAVL);
          int j = localNodeAVL2.getBalance(paramPersistentStore);
          paramNodeAVL = paramNodeAVL.setBalance(paramPersistentStore, j == -i ? i : 0);
          localNodeAVL1 = localNodeAVL1.setBalance(paramPersistentStore, j == i ? -i : 0);
          localNodeAVL2 = localNodeAVL2.setBalance(paramPersistentStore, 0);
        }
        return;
      }
      if (paramNodeAVL.isRoot(paramPersistentStore)) {
        return;
      }
      paramBoolean = paramNodeAVL.isFromLeft(paramPersistentStore);
      paramNodeAVL = paramNodeAVL.getParent(paramPersistentStore);
    }
  }
  
  NodeAVL getAccessor(PersistentStore paramPersistentStore)
  {
    NodeAVL localNodeAVL = (NodeAVL)paramPersistentStore.getAccessor(this);
    return localNodeAVL;
  }
  
  IndexRowIterator getIterator(Session paramSession, PersistentStore paramPersistentStore, NodeAVL paramNodeAVL, boolean paramBoolean1, boolean paramBoolean2)
  {
    if (paramNodeAVL == null) {
      return emptyIterator;
    }
    IndexRowIterator localIndexRowIterator = new IndexRowIterator(paramSession, paramPersistentStore, this, paramNodeAVL, 0, paramBoolean1, paramBoolean2);
    return localIndexRowIterator;
  }
  
  public static final class IndexRowIterator
    implements RowIterator
  {
    final Session session;
    final PersistentStore store;
    final IndexAVL index;
    NodeAVL nextnode;
    Row lastrow;
    int distinctCount;
    boolean single;
    boolean reversed;
    
    public IndexRowIterator(Session paramSession, PersistentStore paramPersistentStore, IndexAVL paramIndexAVL, NodeAVL paramNodeAVL, int paramInt, boolean paramBoolean1, boolean paramBoolean2)
    {
      this.session = paramSession;
      this.store = paramPersistentStore;
      this.index = paramIndexAVL;
      this.distinctCount = paramInt;
      this.single = paramBoolean1;
      this.reversed = paramBoolean2;
      if (paramIndexAVL == null) {
        return;
      }
      this.nextnode = paramNodeAVL;
    }
    
    public boolean hasNext()
    {
      return this.nextnode != null;
    }
    
    public Row getNextRow()
    {
      if (this.nextnode == null)
      {
        release();
        return null;
      }
      NodeAVL localNodeAVL = this.nextnode;
      if (this.single)
      {
        this.nextnode = null;
      }
      else
      {
        this.store.readLock();
        try
        {
          for (;;)
          {
            if (this.reversed) {
              this.nextnode = this.index.last(this.session, this.store, this.nextnode, this.distinctCount);
            } else {
              this.nextnode = this.index.next(this.session, this.store, this.nextnode, this.distinctCount);
            }
            if (this.nextnode == null) {
              break;
            }
            RowAVL localRowAVL = this.nextnode.getRow(this.store);
            if ((this.session == null) || (this.store.canRead(this.session, localRowAVL, 0, null))) {
              break;
            }
          }
        }
        finally
        {
          this.store.readUnlock();
        }
      }
      this.lastrow = localNodeAVL.getRow(this.store);
      return this.lastrow;
    }
    
    public Object[] getNext()
    {
      Row localRow = getNextRow();
      return localRow == null ? null : localRow.getData();
    }
    
    public void removeCurrent()
    {
      this.store.delete(this.session, this.lastrow);
      this.store.remove(this.lastrow);
    }
    
    public void release() {}
    
    public long getRowId()
    {
      return this.nextnode.getPos();
    }
    
    public TableBase getCurrentTable()
    {
      return this.index.table;
    }
  }
}


/* Location:              E:\java\java学习\hutubill\lib\all.jar!\org\hsqldb\index\IndexAVL.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */
package org.hsqldb.navigator;

import java.util.Comparator;
import java.util.TreeMap;
import org.hsqldb.QueryExpression;
import org.hsqldb.QuerySpecification;
import org.hsqldb.Row;
import org.hsqldb.Session;
import org.hsqldb.SortAndSlice;
import org.hsqldb.TableBase;
import org.hsqldb.error.Error;
import org.hsqldb.index.Index;
import org.hsqldb.lib.ArraySort;
import org.hsqldb.lib.ArrayUtil;
import org.hsqldb.lib.LongKeyHashMap;
import org.hsqldb.result.ResultMetaData;
import org.hsqldb.rowio.RowInputInterface;
import org.hsqldb.rowio.RowOutputInterface;

public class RowSetNavigatorData
  extends RowSetNavigator
  implements Comparator
{
  public static final Object[][] emptyTable = new Object[0][];
  int currentOffset;
  int baseBlockSize;
  Object[][] table = emptyTable;
  int visibleColumnCount;
  boolean isAggregate;
  boolean isSimpleAggregate;
  Object[] simpleAggregateData;
  boolean reindexTable;
  Index mainIndex;
  Index fullIndex;
  Index orderIndex;
  Index groupIndex;
  Index idIndex;
  TreeMap rowMap;
  LongKeyHashMap idMap;
  
  RowSetNavigatorData(Session paramSession)
  {
    this.session = paramSession;
  }
  
  public RowSetNavigatorData(Session paramSession, QuerySpecification paramQuerySpecification)
  {
    this.session = paramSession;
    this.rangePosition = paramQuerySpecification.resultRangePosition;
    this.visibleColumnCount = paramQuerySpecification.getColumnCount();
    this.isSimpleAggregate = ((paramQuerySpecification.isAggregated) && (!paramQuerySpecification.isGrouped));
    this.mainIndex = paramQuerySpecification.mainIndex;
    this.fullIndex = paramQuerySpecification.fullIndex;
    this.orderIndex = paramQuerySpecification.orderIndex;
    if (paramQuerySpecification.isGrouped)
    {
      this.mainIndex = paramQuerySpecification.groupIndex;
      this.rowMap = new TreeMap(this);
    }
    if (paramQuerySpecification.idIndex != null) {
      this.idMap = new LongKeyHashMap();
    }
  }
  
  public RowSetNavigatorData(Session paramSession, QueryExpression paramQueryExpression)
  {
    this.session = paramSession;
    this.mainIndex = paramQueryExpression.mainIndex;
    this.fullIndex = paramQueryExpression.fullIndex;
    this.orderIndex = paramQueryExpression.orderIndex;
    this.visibleColumnCount = paramQueryExpression.getColumnCount();
  }
  
  public RowSetNavigatorData(Session paramSession, RowSetNavigator paramRowSetNavigator)
  {
    this.session = paramSession;
    setCapacity(paramRowSetNavigator.size);
    while (paramRowSetNavigator.hasNext()) {
      add(paramRowSetNavigator.getNext());
    }
  }
  
  public void sortFull(Session paramSession)
  {
    this.mainIndex = this.fullIndex;
    ArraySort.sort(this.table, 0, this.size, this);
    reset();
  }
  
  public void sortOrder(Session paramSession)
  {
    if (this.orderIndex != null)
    {
      this.mainIndex = this.orderIndex;
      ArraySort.sort(this.table, 0, this.size, this);
    }
    reset();
  }
  
  public void sortOrderUnion(Session paramSession, SortAndSlice paramSortAndSlice)
  {
    if (paramSortAndSlice.index != null)
    {
      this.mainIndex = paramSortAndSlice.index;
      ArraySort.sort(this.table, 0, this.size, this);
      reset();
    }
  }
  
  public void add(Object[] paramArrayOfObject)
  {
    ensureCapacity();
    this.table[this.size] = paramArrayOfObject;
    this.size += 1;
    if (this.rowMap != null) {
      this.rowMap.put(paramArrayOfObject, paramArrayOfObject);
    }
    if (this.idMap != null)
    {
      Long localLong = (Long)paramArrayOfObject[this.visibleColumnCount];
      this.idMap.put(localLong.longValue(), paramArrayOfObject);
    }
  }
  
  public boolean addRow(Row paramRow)
  {
    throw Error.runtimeError(201, "RowSetNavigatorData");
  }
  
  public void update(Object[] paramArrayOfObject1, Object[] paramArrayOfObject2) {}
  
  void addAdjusted(Object[] paramArrayOfObject, int[] paramArrayOfInt)
  {
    paramArrayOfObject = projectData(paramArrayOfObject, paramArrayOfInt);
    add(paramArrayOfObject);
  }
  
  void insertAdjusted(Object[] paramArrayOfObject, int[] paramArrayOfInt)
  {
    projectData(paramArrayOfObject, paramArrayOfInt);
    insert(paramArrayOfObject);
  }
  
  Object[] projectData(Object[] paramArrayOfObject, int[] paramArrayOfInt)
  {
    if (paramArrayOfInt == null)
    {
      paramArrayOfObject = (Object[])ArrayUtil.resizeArrayIfDifferent(paramArrayOfObject, this.visibleColumnCount);
    }
    else
    {
      Object[] arrayOfObject = new Object[this.visibleColumnCount];
      ArrayUtil.projectRow(paramArrayOfObject, paramArrayOfInt, arrayOfObject);
      paramArrayOfObject = arrayOfObject;
    }
    return paramArrayOfObject;
  }
  
  void insert(Object[] paramArrayOfObject)
  {
    ensureCapacity();
    System.arraycopy(this.table, this.currentPos, this.table, this.currentPos + 1, this.size - this.currentPos);
    this.table[this.currentPos] = paramArrayOfObject;
    this.size += 1;
  }
  
  public void release()
  {
    this.table = emptyTable;
    this.size = 0;
    reset();
    this.isClosed = true;
  }
  
  public void clear()
  {
    this.table = emptyTable;
    this.size = 0;
    reset();
  }
  
  public boolean absolute(int paramInt)
  {
    return super.absolute(paramInt);
  }
  
  public Object[] getCurrent()
  {
    if ((this.currentPos < 0) || (this.currentPos >= this.size)) {
      return null;
    }
    if (this.currentPos == this.currentOffset + this.table.length) {
      getBlock(this.currentOffset + this.table.length);
    }
    return this.table[(this.currentPos - this.currentOffset)];
  }
  
  public Row getCurrentRow()
  {
    throw Error.runtimeError(201, "RowSetNavigatorData");
  }
  
  public Object[] getNextRowData()
  {
    return next() ? getCurrent() : null;
  }
  
  public boolean next()
  {
    return super.next();
  }
  
  public void removeCurrent()
  {
    System.arraycopy(this.table, this.currentPos + 1, this.table, this.currentPos, this.size - this.currentPos - 1);
    this.table[(this.size - 1)] = null;
    this.currentPos -= 1;
    this.size -= 1;
  }
  
  public void reset()
  {
    super.reset();
  }
  
  public boolean isMemory()
  {
    return true;
  }
  
  public void read(RowInputInterface paramRowInputInterface, ResultMetaData paramResultMetaData) {}
  
  public void write(RowOutputInterface paramRowOutputInterface, ResultMetaData paramResultMetaData)
  {
    reset();
    paramRowOutputInterface.writeLong(this.id);
    paramRowOutputInterface.writeInt(this.size);
    paramRowOutputInterface.writeInt(0);
    paramRowOutputInterface.writeInt(this.size);
    while (hasNext())
    {
      Object[] arrayOfObject = getNext();
      paramRowOutputInterface.writeData(paramResultMetaData.getExtendedColumnCount(), paramResultMetaData.columnTypes, arrayOfObject, null, null);
    }
    reset();
  }
  
  public Object[] getData(long paramLong)
  {
    return (Object[])this.idMap.get(paramLong);
  }
  
  public void copy(RowIterator paramRowIterator, int[] paramArrayOfInt)
  {
    while (paramRowIterator.hasNext())
    {
      Object[] arrayOfObject = paramRowIterator.getNext();
      addAdjusted(arrayOfObject, paramArrayOfInt);
    }
  }
  
  public void union(Session paramSession, RowSetNavigatorData paramRowSetNavigatorData)
  {
    removeDuplicates(paramSession);
    paramRowSetNavigatorData.removeDuplicates(paramSession);
    this.mainIndex = this.fullIndex;
    while (paramRowSetNavigatorData.hasNext())
    {
      Object[] arrayOfObject = paramRowSetNavigatorData.getNext();
      int i = ArraySort.searchFirst(this.table, 0, this.size, arrayOfObject, this);
      if (i < 0)
      {
        i = -i - 1;
        this.currentPos = i;
        insert(arrayOfObject);
      }
    }
    reset();
  }
  
  public void unionAll(Session paramSession, RowSetNavigatorData paramRowSetNavigatorData)
  {
    this.mainIndex = this.fullIndex;
    paramRowSetNavigatorData.reset();
    while (paramRowSetNavigatorData.hasNext())
    {
      Object[] arrayOfObject = paramRowSetNavigatorData.getNext();
      add(arrayOfObject);
    }
    reset();
  }
  
  public void intersect(Session paramSession, RowSetNavigatorData paramRowSetNavigatorData)
  {
    removeDuplicates(paramSession);
    paramRowSetNavigatorData.sortFull(paramSession);
    while (hasNext())
    {
      Object[] arrayOfObject = getNext();
      boolean bool = paramRowSetNavigatorData.containsRow(arrayOfObject);
      if (!bool) {
        removeCurrent();
      }
    }
    reset();
  }
  
  public void intersectAll(Session paramSession, RowSetNavigatorData paramRowSetNavigatorData)
  {
    Object localObject = null;
    Object[] arrayOfObject1 = null;
    sortFull(paramSession);
    paramRowSetNavigatorData.sortFull(paramSession);
    RowIterator localRowIterator = this.fullIndex.emptyIterator();
    while (hasNext())
    {
      Object[] arrayOfObject2 = getNext();
      int i = (localObject == null) || (this.fullIndex.compareRowNonUnique(paramSession, arrayOfObject2, (Object[])localObject, this.visibleColumnCount) != 0) ? 1 : 0;
      if (i != 0)
      {
        localObject = arrayOfObject2;
        localRowIterator = paramRowSetNavigatorData.findFirstRow(arrayOfObject2);
      }
      arrayOfObject1 = localRowIterator.getNext();
      if ((arrayOfObject1 == null) || (this.fullIndex.compareRowNonUnique(paramSession, arrayOfObject2, arrayOfObject1, this.visibleColumnCount) != 0)) {
        removeCurrent();
      }
    }
    reset();
  }
  
  public void except(Session paramSession, RowSetNavigatorData paramRowSetNavigatorData)
  {
    removeDuplicates(paramSession);
    paramRowSetNavigatorData.sortFull(paramSession);
    while (hasNext())
    {
      Object[] arrayOfObject = getNext();
      boolean bool = paramRowSetNavigatorData.containsRow(arrayOfObject);
      if (bool) {
        removeCurrent();
      }
    }
    reset();
  }
  
  public void exceptAll(Session paramSession, RowSetNavigatorData paramRowSetNavigatorData)
  {
    Object localObject = null;
    Object[] arrayOfObject1 = null;
    sortFull(paramSession);
    paramRowSetNavigatorData.sortFull(paramSession);
    RowIterator localRowIterator = this.fullIndex.emptyIterator();
    while (hasNext())
    {
      Object[] arrayOfObject2 = getNext();
      int i = (localObject == null) || (this.fullIndex.compareRowNonUnique(paramSession, arrayOfObject2, (Object[])localObject, this.fullIndex.getColumnCount()) != 0) ? 1 : 0;
      if (i != 0)
      {
        localObject = arrayOfObject2;
        localRowIterator = paramRowSetNavigatorData.findFirstRow(arrayOfObject2);
      }
      arrayOfObject1 = localRowIterator.getNext();
      if ((arrayOfObject1 != null) && (this.fullIndex.compareRowNonUnique(paramSession, arrayOfObject2, arrayOfObject1, this.fullIndex.getColumnCount()) == 0)) {
        removeCurrent();
      }
    }
    reset();
  }
  
  public boolean hasUniqueNotNullRows(Session paramSession)
  {
    sortFull(paramSession);
    reset();
    Object localObject = null;
    while (hasNext())
    {
      Object[] arrayOfObject = getNext();
      if (!hasNull(arrayOfObject))
      {
        if ((localObject != null) && (this.fullIndex.compareRow(paramSession, (Object[])localObject, arrayOfObject) == 0)) {
          return false;
        }
        localObject = arrayOfObject;
      }
    }
    return true;
  }
  
  public void removeDuplicates(Session paramSession)
  {
    sortFull(paramSession);
    reset();
    int i = -1;
    Object localObject = null;
    while (hasNext())
    {
      Object[] arrayOfObject = getNext();
      if (localObject == null)
      {
        i = this.currentPos;
        localObject = arrayOfObject;
      }
      else if (this.fullIndex.compareRow(paramSession, (Object[])localObject, arrayOfObject) != 0)
      {
        i++;
        localObject = arrayOfObject;
        this.table[i] = arrayOfObject;
      }
    }
    for (int j = i + 1; j < this.size; j++) {
      this.table[j] = null;
    }
    this.size = (i + 1);
    reset();
  }
  
  public void trim(int paramInt1, int paramInt2)
  {
    if (this.size == 0) {
      return;
    }
    if (paramInt1 >= this.size)
    {
      clear();
      return;
    }
    if (paramInt1 != 0)
    {
      reset();
      for (i = 0; i < paramInt1; i++)
      {
        next();
        removeCurrent();
      }
    }
    if (paramInt2 >= this.size) {
      return;
    }
    reset();
    for (int i = 0; i < paramInt2; i++) {
      next();
    }
    while (hasNext())
    {
      next();
      removeCurrent();
    }
    reset();
  }
  
  boolean hasNull(Object[] paramArrayOfObject)
  {
    for (int i = 0; i < this.visibleColumnCount; i++) {
      if (paramArrayOfObject[i] == null) {
        return true;
      }
    }
    return false;
  }
  
  public Object[] getGroupData(Object[] paramArrayOfObject)
  {
    if (this.isSimpleAggregate)
    {
      if (this.simpleAggregateData == null)
      {
        this.simpleAggregateData = paramArrayOfObject;
        return null;
      }
      return this.simpleAggregateData;
    }
    return (Object[])this.rowMap.get(paramArrayOfObject);
  }
  
  boolean containsRow(Object[] paramArrayOfObject)
  {
    int i = ArraySort.searchFirst(this.table, 0, this.size, paramArrayOfObject, this);
    return i >= 0;
  }
  
  RowIterator findFirstRow(Object[] paramArrayOfObject)
  {
    int i = ArraySort.searchFirst(this.table, 0, this.size, paramArrayOfObject, this);
    if (i < 0) {
      i = this.size;
    } else {
      i--;
    }
    return new DataIterator(i);
  }
  
  void getBlock(int paramInt) {}
  
  private void setCapacity(int paramInt)
  {
    if (this.size > this.table.length) {
      this.table = new Object[paramInt][];
    }
  }
  
  private void ensureCapacity()
  {
    if (this.size == this.table.length)
    {
      int i = this.size == 0 ? 4 : this.size * 2;
      Object[][] arrayOfObject = new Object[i][];
      System.arraycopy(this.table, 0, arrayOfObject, 0, this.size);
      this.table = arrayOfObject;
    }
  }
  
  void implement()
  {
    throw Error.error(201, "RSND");
  }
  
  public int compare(Object paramObject1, Object paramObject2)
  {
    return this.mainIndex.compareRow((Session)this.session, (Object[])paramObject1, (Object[])paramObject2);
  }
  
  class DataIterator
    implements RowIterator
  {
    int pos;
    
    DataIterator(int paramInt)
    {
      this.pos = paramInt;
    }
    
    public Row getNextRow()
    {
      return null;
    }
    
    public Object[] getNext()
    {
      if (hasNext())
      {
        this.pos += 1;
        return RowSetNavigatorData.this.table[this.pos];
      }
      return null;
    }
    
    public boolean hasNext()
    {
      return this.pos < RowSetNavigatorData.this.size - 1;
    }
    
    public void removeCurrent() {}
    
    public void release() {}
    
    public long getRowId()
    {
      return 0L;
    }
    
    public TableBase getCurrentTable()
    {
      return null;
    }
  }
}


/* Location:              E:\java\java学习\hutubill\lib\all.jar!\org\hsqldb\navigator\RowSetNavigatorData.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */
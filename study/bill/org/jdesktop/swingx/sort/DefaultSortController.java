/*     */ package org.jdesktop.swingx.sort;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import java.util.Arrays;
/*     */ import java.util.Comparator;
/*     */ import java.util.List;
/*     */ import javax.swing.DefaultRowSorter;
/*     */ import javax.swing.DefaultRowSorter.ModelWrapper;
/*     */ import javax.swing.RowSorter.SortKey;
/*     */ import javax.swing.SortOrder;
/*     */ import org.jdesktop.swingx.renderer.StringValue;
/*     */ import org.jdesktop.swingx.renderer.StringValues;
/*     */ import org.jdesktop.swingx.util.Contract;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public abstract class DefaultSortController<M>
/*     */   extends DefaultRowSorter<M, Integer>
/*     */   implements SortController<M>
/*     */ {
/*  57 */   public static final Comparator COMPARABLE_COMPARATOR = new ComparableComparator(null);
/*     */   
/*     */ 
/*  60 */   private static final SortOrder[] DEFAULT_CYCLE = { SortOrder.ASCENDING, SortOrder.DESCENDING };
/*     */   
/*     */   private List<SortOrder> sortCycle;
/*     */   
/*     */   private boolean sortable;
/*     */   
/*     */   private StringValueProvider stringValueProvider;
/*     */   
/*     */   protected int cachedModelRowCount;
/*     */   
/*     */   public DefaultSortController()
/*     */   {
/*  72 */     setSortable(true);
/*  73 */     setSortOrderCycle(DEFAULT_CYCLE);
/*  74 */     setSortsOnUpdates(true);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setSortable(boolean sortable)
/*     */   {
/*  82 */     this.sortable = sortable;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean isSortable()
/*     */   {
/*  91 */     return this.sortable;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setSortable(int column, boolean sortable)
/*     */   {
/* 100 */     super.setSortable(column, sortable);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean isSortable(int column)
/*     */   {
/* 109 */     if (!isSortable()) return false;
/* 110 */     return super.isSortable(column);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void toggleSortOrder(int column)
/*     */   {
/* 122 */     checkColumn(column);
/* 123 */     if (!isSortable(column))
/* 124 */       return;
/* 125 */     SortOrder firstInCycle = getFirstInCycle();
/*     */     
/* 127 */     if (firstInCycle == null)
/* 128 */       return;
/* 129 */     List<RowSorter.SortKey> keys = new ArrayList(getSortKeys());
/* 130 */     RowSorter.SortKey sortKey = SortUtils.getFirstSortKeyForColumn(keys, column);
/* 131 */     if (keys.indexOf(sortKey) == 0)
/*     */     {
/* 133 */       keys.set(0, new RowSorter.SortKey(column, getNextInCycle(sortKey.getSortOrder())));
/*     */     }
/*     */     else {
/* 136 */       keys.remove(sortKey);
/* 137 */       keys.add(0, new RowSorter.SortKey(column, getFirstInCycle()));
/*     */     }
/* 139 */     if (keys.size() > getMaxSortKeys()) {
/* 140 */       keys = keys.subList(0, getMaxSortKeys());
/*     */     }
/* 142 */     setSortKeys(keys);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private SortOrder getNextInCycle(SortOrder current)
/*     */   {
/* 154 */     int pos = this.sortCycle.indexOf(current);
/* 155 */     if (pos < 0)
/*     */     {
/* 157 */       return getFirstInCycle();
/*     */     }
/* 159 */     pos++;
/* 160 */     if (pos >= this.sortCycle.size()) {
/* 161 */       pos = 0;
/*     */     }
/* 163 */     return (SortOrder)this.sortCycle.get(pos);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private SortOrder getFirstInCycle()
/*     */   {
/* 172 */     return this.sortCycle.size() > 0 ? (SortOrder)this.sortCycle.get(0) : null;
/*     */   }
/*     */   
/*     */   private void checkColumn(int column) {
/* 176 */     if ((column < 0) || (column >= getModelWrapper().getColumnCount())) {
/* 177 */       throw new IndexOutOfBoundsException("column beyond range of TableModel");
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setSortOrder(int column, SortOrder sortOrder)
/*     */   {
/* 191 */     if (!isSortable(column)) return;
/* 192 */     RowSorter.SortKey replace = new RowSorter.SortKey(column, sortOrder);
/* 193 */     List<RowSorter.SortKey> keys = new ArrayList(getSortKeys());
/* 194 */     SortUtils.removeFirstSortKeyForColumn(keys, column);
/* 195 */     keys.add(0, replace);
/*     */     
/* 197 */     setSortKeys(keys);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public SortOrder getSortOrder(int column)
/*     */   {
/* 206 */     RowSorter.SortKey key = SortUtils.getFirstSortKeyForColumn(getSortKeys(), column);
/* 207 */     return key != null ? key.getSortOrder() : SortOrder.UNSORTED;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void resetSortOrders()
/*     */   {
/* 216 */     if (!isSortable()) return;
/* 217 */     List<RowSorter.SortKey> keys = new ArrayList(getSortKeys());
/* 218 */     for (int i = keys.size() - 1; i >= 0; i--) {
/* 219 */       RowSorter.SortKey sortKey = (RowSorter.SortKey)keys.get(i);
/* 220 */       if (isSortable(sortKey.getColumn())) {
/* 221 */         keys.remove(sortKey);
/*     */       }
/*     */     }
/*     */     
/* 225 */     setSortKeys(keys);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public SortOrder[] getSortOrderCycle()
/*     */   {
/* 235 */     return (SortOrder[])this.sortCycle.toArray(new SortOrder[0]);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setSortOrderCycle(SortOrder... cycle)
/*     */   {
/* 243 */     Contract.asNotNull(cycle, "Elements of SortOrderCycle must not be null");
/*     */     
/* 245 */     this.sortCycle = Arrays.asList(cycle);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setStringValueProvider(StringValueProvider registry)
/*     */   {
/* 255 */     this.stringValueProvider = registry;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public StringValueProvider getStringValueProvider()
/*     */   {
/* 266 */     if (this.stringValueProvider == null) {
/* 267 */       this.stringValueProvider = DEFAULT_PROVIDER;
/*     */     }
/* 269 */     return this.stringValueProvider;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static SortOrder[] getDefaultSortOrderCycle()
/*     */   {
/* 278 */     return (SortOrder[])Arrays.copyOf(DEFAULT_CYCLE, DEFAULT_CYCLE.length);
/*     */   }
/*     */   
/* 281 */   private static final StringValueProvider DEFAULT_PROVIDER = new StringValueProvider()
/*     */   {
/*     */     public StringValue getStringValue(int row, int column)
/*     */     {
/* 285 */       return StringValues.TO_STRING;
/*     */     }
/*     */   };
/*     */   
/*     */   private static class ComparableComparator
/*     */     implements Comparator
/*     */   {
/*     */     public int compare(Object o1, Object o2)
/*     */     {
/* 294 */       return ((Comparable)o1).compareTo(o2);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public int convertRowIndexToModel(int viewIndex)
/*     */   {
/* 309 */     if ((viewIndex < 0) || (viewIndex >= getViewRowCount())) {
/* 310 */       throw new IndexOutOfBoundsException("valid viewIndex: 0 <= index < " + getViewRowCount() + " but was: " + viewIndex);
/*     */     }
/*     */     try
/*     */     {
/* 314 */       return super.convertRowIndexToModel(viewIndex);
/*     */     }
/*     */     catch (Exception e) {}
/*     */     
/*     */ 
/*     */ 
/* 320 */     return viewIndex;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public int convertRowIndexToView(int modelIndex)
/*     */   {
/* 333 */     if ((modelIndex < 0) || (modelIndex >= getModelRowCount())) {
/* 334 */       throw new IndexOutOfBoundsException("valid modelIndex: 0 <= index < " + getModelRowCount() + " but was: " + modelIndex);
/*     */     }
/*     */     try
/*     */     {
/* 338 */       return super.convertRowIndexToView(modelIndex);
/*     */     }
/*     */     catch (Exception e) {}
/*     */     
/*     */ 
/*     */ 
/* 344 */     return modelIndex;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public int getModelRowCount()
/*     */   {
/* 358 */     return this.cachedModelRowCount;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public int getViewRowCount()
/*     */   {
/* 372 */     if (hasRowFilter())
/* 373 */       return super.getViewRowCount();
/* 374 */     return getModelRowCount();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   private boolean hasRowFilter()
/*     */   {
/* 381 */     return getRowFilter() != null;
/*     */   }
/*     */   
/*     */ 
/*     */   public void allRowsChanged()
/*     */   {
/* 387 */     this.cachedModelRowCount = getModelWrapper().getRowCount();
/* 388 */     super.allRowsChanged();
/*     */   }
/*     */   
/*     */   public void modelStructureChanged() {
/* 392 */     super.modelStructureChanged();
/* 393 */     this.cachedModelRowCount = getModelWrapper().getRowCount();
/*     */   }
/*     */   
/*     */   public void rowsDeleted(int firstRow, int endRow) {
/* 397 */     this.cachedModelRowCount = getModelWrapper().getRowCount();
/* 398 */     super.rowsDeleted(firstRow, endRow);
/*     */   }
/*     */   
/*     */   public void rowsInserted(int firstRow, int endRow) {
/* 402 */     this.cachedModelRowCount = getModelWrapper().getRowCount();
/* 403 */     super.rowsInserted(firstRow, endRow);
/*     */   }
/*     */ }


/* Location:              E:\java\java学习\hutubill\lib\all.jar!\org\jdesktop\swingx\sort\DefaultSortController.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */
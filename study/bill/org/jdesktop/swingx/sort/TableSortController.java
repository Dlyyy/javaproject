/*     */ package org.jdesktop.swingx.sort;
/*     */ 
/*     */ import java.text.Collator;
/*     */ import java.util.Comparator;
/*     */ import javax.swing.DefaultRowSorter.ModelWrapper;
/*     */ import javax.swing.table.TableModel;
/*     */ import org.jdesktop.swingx.renderer.StringValue;
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
/*     */ public class TableSortController<M extends TableModel>
/*     */   extends DefaultSortController<M>
/*     */ {
/*     */   private M tableModel;
/*     */   
/*     */   public TableSortController()
/*     */   {
/*  44 */     this(null);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public TableSortController(M model)
/*     */   {
/*  52 */     setModel(model);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setModel(M model)
/*     */   {
/*  63 */     this.tableModel = model;
/*  64 */     if (model != null)
/*  65 */       this.cachedModelRowCount = model.getRowCount();
/*  66 */     setModelWrapper(new TableRowSorterModelWrapper(null));
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public Comparator<?> getComparator(int column)
/*     */   {
/*  89 */     Comparator<?> comparator = super.getComparator(column);
/*  90 */     if (comparator != null) {
/*  91 */       return comparator;
/*     */     }
/*  93 */     Class<?> columnClass = ((TableModel)getModel()).getColumnClass(column);
/*  94 */     if (columnClass == String.class) {
/*  95 */       return Collator.getInstance();
/*     */     }
/*  97 */     if (Comparable.class.isAssignableFrom(columnClass)) {
/*  98 */       return COMPARABLE_COMPARATOR;
/*     */     }
/* 100 */     return Collator.getInstance();
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
/*     */ 
/*     */   protected boolean useToString(int column)
/*     */   {
/* 115 */     Comparator<?> comparator = super.getComparator(column);
/* 116 */     if (comparator != null) {
/* 117 */       return false;
/*     */     }
/* 119 */     Class<?> columnClass = ((TableModel)getModel()).getColumnClass(column);
/* 120 */     if (columnClass == String.class) {
/* 121 */       return false;
/*     */     }
/* 123 */     if (Comparable.class.isAssignableFrom(columnClass)) {
/* 124 */       return false;
/*     */     }
/* 126 */     return true;
/*     */   }
/*     */   
/*     */ 
/*     */   private class TableRowSorterModelWrapper
/*     */     extends DefaultRowSorter.ModelWrapper<M, Integer>
/*     */   {
/*     */     private TableRowSorterModelWrapper() {}
/*     */     
/*     */     public M getModel()
/*     */     {
/* 137 */       return TableSortController.this.tableModel;
/*     */     }
/*     */     
/*     */     public int getColumnCount()
/*     */     {
/* 142 */       return TableSortController.this.tableModel == null ? 0 : TableSortController.this.tableModel.getColumnCount();
/*     */     }
/*     */     
/*     */     public int getRowCount()
/*     */     {
/* 147 */       return TableSortController.this.tableModel == null ? 0 : TableSortController.this.tableModel.getRowCount();
/*     */     }
/*     */     
/*     */     public Object getValueAt(int row, int column)
/*     */     {
/* 152 */       return TableSortController.this.tableModel.getValueAt(row, column);
/*     */     }
/*     */     
/*     */     public String getStringValueAt(int row, int column)
/*     */     {
/* 157 */       return TableSortController.this.getStringValueProvider().getStringValue(row, column).getString(getValueAt(row, column));
/*     */     }
/*     */     
/*     */ 
/*     */     public Integer getIdentifier(int index)
/*     */     {
/* 163 */       return Integer.valueOf(index);
/*     */     }
/*     */   }
/*     */ }


/* Location:              E:\java\java学习\hutubill\lib\all.jar!\org\jdesktop\swingx\sort\TableSortController.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */
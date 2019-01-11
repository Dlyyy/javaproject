/*    */ package org.jdesktop.swingx.sort;
/*    */ 
/*    */ import java.util.List;
/*    */ import javax.swing.RowSorter.SortKey;
/*    */ import javax.swing.SortOrder;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class SortUtils
/*    */ {
/*    */   public static RowSorter.SortKey getFirstSortingKey(List<? extends RowSorter.SortKey> keys)
/*    */   {
/* 28 */     for (RowSorter.SortKey key : keys) {
/* 29 */       if (isSorted(key.getSortOrder())) {
/* 30 */         return key;
/*    */       }
/*    */     }
/* 33 */     return null;
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   public static RowSorter.SortKey getFirstSortKeyForColumn(List<? extends RowSorter.SortKey> keys, int modelColumn)
/*    */   {
/* 46 */     for (RowSorter.SortKey key : keys) {
/* 47 */       if (key.getColumn() == modelColumn) {
/* 48 */         return key;
/*    */       }
/*    */     }
/* 51 */     return null;
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   public static RowSorter.SortKey removeFirstSortKeyForColumn(List<? extends RowSorter.SortKey> keys, int modelColumn)
/*    */   {
/* 64 */     for (RowSorter.SortKey key : keys) {
/* 65 */       if (key.getColumn() == modelColumn) {
/* 66 */         keys.remove(key);
/* 67 */         return key;
/*    */       }
/*    */     }
/* 70 */     return null;
/*    */   }
/*    */   
/* 73 */   public static boolean isSorted(SortOrder sortOrder) { return (sortOrder != null) && (SortOrder.UNSORTED != sortOrder); }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   public static boolean isAscending(SortOrder sortOrder)
/*    */   {
/* 83 */     return sortOrder == SortOrder.ASCENDING;
/*    */   }
/*    */   
/*    */   public static boolean isSorted(SortOrder sortOrder, boolean ascending) {
/* 87 */     return (isSorted(sortOrder)) && (ascending == isAscending(sortOrder));
/*    */   }
/*    */ }


/* Location:              E:\java\java学习\hutubill\lib\all.jar!\org\jdesktop\swingx\sort\SortUtils.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */
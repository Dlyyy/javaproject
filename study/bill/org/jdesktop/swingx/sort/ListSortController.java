/*    */ package org.jdesktop.swingx.sort;
/*    */ 
/*    */ import javax.swing.DefaultRowSorter.ModelWrapper;
/*    */ import javax.swing.ListModel;
/*    */ import org.jdesktop.swingx.renderer.StringValue;
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
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class ListSortController<M extends ListModel>
/*    */   extends DefaultSortController<M>
/*    */ {
/*    */   private M listModel;
/*    */   
/*    */   public ListSortController(M model)
/*    */   {
/* 39 */     setModel(model);
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   public void setModel(M model)
/*    */   {
/* 50 */     this.listModel = model;
/* 51 */     if (model != null)
/* 52 */       this.cachedModelRowCount = model.getSize();
/* 53 */     setModelWrapper(new ListRowSorterModelWrapper(null));
/*    */   }
/*    */   
/*    */   private class ListRowSorterModelWrapper
/*    */     extends DefaultRowSorter.ModelWrapper<M, Integer>
/*    */   {
/*    */     private ListRowSorterModelWrapper() {}
/*    */     
/*    */     public M getModel()
/*    */     {
/* 63 */       return ListSortController.this.listModel;
/*    */     }
/*    */     
/*    */     public int getColumnCount()
/*    */     {
/* 68 */       return ListSortController.this.listModel == null ? 0 : 1;
/*    */     }
/*    */     
/*    */     public int getRowCount()
/*    */     {
/* 73 */       return ListSortController.this.listModel == null ? 0 : ListSortController.this.listModel.getSize();
/*    */     }
/*    */     
/*    */     public Object getValueAt(int row, int column)
/*    */     {
/* 78 */       return ListSortController.this.listModel.getElementAt(row);
/*    */     }
/*    */     
/*    */     public String getStringValueAt(int row, int column)
/*    */     {
/* 83 */       return ListSortController.this.getStringValueProvider().getStringValue(row, column).getString(getValueAt(row, column));
/*    */     }
/*    */     
/*    */ 
/*    */     public Integer getIdentifier(int index)
/*    */     {
/* 89 */       return Integer.valueOf(index);
/*    */     }
/*    */   }
/*    */ }


/* Location:              E:\java\java学习\hutubill\lib\all.jar!\org\jdesktop\swingx\sort\ListSortController.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */
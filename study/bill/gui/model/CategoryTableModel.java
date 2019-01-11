/*    */ package gui.model;
/*    */ 
/*    */ import entity.Category;
/*    */ import java.util.List;
/*    */ import javax.swing.table.AbstractTableModel;
/*    */ import service.CategoryService;
/*    */ 
/*    */ 
/*    */ public class CategoryTableModel
/*    */   extends AbstractTableModel
/*    */ {
/* 12 */   String[] columnNames = { "分类名称", "消费次数" };
/*    */   
/*    */ 
/*    */ 
/* 16 */   public List<Category> cs = new CategoryService().list();
/*    */   
/*    */   public int getRowCount()
/*    */   {
/* 20 */     return this.cs.size();
/*    */   }
/*    */   
/*    */   public int getColumnCount()
/*    */   {
/* 25 */     return this.columnNames.length;
/*    */   }
/*    */   
/*    */   public String getColumnName(int columnIndex)
/*    */   {
/* 30 */     return this.columnNames[columnIndex];
/*    */   }
/*    */   
/*    */   public boolean isCellEditable(int rowIndex, int columnIndex) {
/* 34 */     return false;
/*    */   }
/*    */   
/*    */ 
/*    */   public Object getValueAt(int rowIndex, int columnIndex)
/*    */   {
/* 40 */     Category h = (Category)this.cs.get(rowIndex);
/* 41 */     if (columnIndex == 0)
/* 42 */       return h.name;
/* 43 */     if (1 == columnIndex) {
/* 44 */       return Integer.valueOf(h.recordNumber);
/*    */     }
/* 46 */     return null;
/*    */   }
/*    */ }


/* Location:              E:\java\java学习\hutubill\lib\all.jar!\gui\model\CategoryTableModel.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */
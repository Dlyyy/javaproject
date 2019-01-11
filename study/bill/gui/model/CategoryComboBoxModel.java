/*    */ package gui.model;
/*    */ 
/*    */ import entity.Category;
/*    */ import java.util.List;
/*    */ import javax.swing.ComboBoxModel;
/*    */ import javax.swing.event.ListDataListener;
/*    */ import service.CategoryService;
/*    */ 
/*    */ 
/*    */ public class CategoryComboBoxModel
/*    */   implements ComboBoxModel<Category>
/*    */ {
/* 13 */   public List<Category> cs = new CategoryService().list();
/*    */   public Category c;
/*    */   
/*    */   public CategoryComboBoxModel()
/*    */   {
/* 18 */     if (!this.cs.isEmpty()) {
/* 19 */       this.c = ((Category)this.cs.get(0));
/*    */     }
/*    */   }
/*    */   
/*    */ 
/*    */   public int getSize()
/*    */   {
/* 26 */     return this.cs.size();
/*    */   }
/*    */   
/*    */ 
/*    */   public Category getElementAt(int index)
/*    */   {
/* 32 */     return (Category)this.cs.get(index);
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */   public void addListDataListener(ListDataListener l) {}
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */   public void removeListDataListener(ListDataListener l) {}
/*    */   
/*    */ 
/*    */ 
/*    */   public void setSelectedItem(Object anItem)
/*    */   {
/* 49 */     this.c = ((Category)anItem);
/*    */   }
/*    */   
/*    */   public Object getSelectedItem()
/*    */   {
/* 54 */     if (!this.cs.isEmpty()) {
/* 55 */       return this.c;
/*    */     }
/* 57 */     return null;
/*    */   }
/*    */ }


/* Location:              E:\java\java学习\hutubill\lib\all.jar!\gui\model\CategoryComboBoxModel.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */
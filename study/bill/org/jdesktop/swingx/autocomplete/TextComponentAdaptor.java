/*    */ package org.jdesktop.swingx.autocomplete;
/*    */ 
/*    */ import java.util.List;
/*    */ import javax.swing.text.JTextComponent;
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
/*    */ public class TextComponentAdaptor
/*    */   extends AbstractAutoCompleteAdaptor
/*    */ {
/*    */   List<?> items;
/*    */   JTextComponent textComponent;
/*    */   Object selectedItem;
/*    */   
/*    */   public TextComponentAdaptor(JTextComponent textComponent, List<?> items)
/*    */   {
/* 53 */     this.items = items;
/* 54 */     this.textComponent = textComponent;
/*    */   }
/*    */   
/*    */   public Object getSelectedItem()
/*    */   {
/* 59 */     return this.selectedItem;
/*    */   }
/*    */   
/*    */   public int getItemCount()
/*    */   {
/* 64 */     return this.items.size();
/*    */   }
/*    */   
/*    */   public Object getItem(int index)
/*    */   {
/* 69 */     return this.items.get(index);
/*    */   }
/*    */   
/*    */   public void setSelectedItem(Object item)
/*    */   {
/* 74 */     this.selectedItem = item;
/*    */   }
/*    */   
/*    */   public JTextComponent getTextComponent()
/*    */   {
/* 79 */     return this.textComponent;
/*    */   }
/*    */ }


/* Location:              E:\java\java学习\hutubill\lib\all.jar!\org\jdesktop\swingx\autocomplete\TextComponentAdaptor.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */
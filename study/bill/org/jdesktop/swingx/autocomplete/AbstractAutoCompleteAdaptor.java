/*     */ package org.jdesktop.swingx.autocomplete;
/*     */ 
/*     */ import javax.swing.text.JTextComponent;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public abstract class AbstractAutoCompleteAdaptor
/*     */ {
/*     */   private String selectedItemAsString;
/*     */   
/*     */   public abstract Object getSelectedItem();
/*     */   
/*     */   public abstract void setSelectedItem(Object paramObject);
/*     */   
/*     */   public String getSelectedItemAsString()
/*     */   {
/*  60 */     return this.selectedItemAsString;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setSelectedItemAsString(String itemAsString)
/*     */   {
/*  68 */     this.selectedItemAsString = itemAsString;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public abstract int getItemCount();
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public abstract Object getItem(int paramInt);
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean listContainsSelectedItem()
/*     */   {
/*  89 */     Object selectedItem = getSelectedItem();
/*  90 */     int i = 0; for (int n = getItemCount(); i < n; i++) {
/*  91 */       if (getItem(i) == selectedItem) return true;
/*     */     }
/*  93 */     return false;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public abstract JTextComponent getTextComponent();
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void markEntireText()
/*     */   {
/* 106 */     markText(0);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void markText(int start)
/*     */   {
/* 115 */     getTextComponent().setCaretPosition(getTextComponent().getText().length());
/* 116 */     getTextComponent().moveCaretPosition(start);
/*     */   }
/*     */ }


/* Location:              E:\java\java学习\hutubill\lib\all.jar!\org\jdesktop\swingx\autocomplete\AbstractAutoCompleteAdaptor.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */
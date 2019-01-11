/*     */ package org.jdesktop.swingx.autocomplete;
/*     */ 
/*     */ import java.awt.Component;
/*     */ import java.awt.event.ActionListener;
/*     */ import javax.swing.ComboBoxEditor;
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
/*     */ 
/*     */ public class AutoCompleteComboBoxEditor
/*     */   implements ComboBoxEditor
/*     */ {
/*     */   final ComboBoxEditor wrapped;
/*     */   final ObjectToStringConverter stringConverter;
/*     */   private Object oldItem;
/*     */   
/*     */   public AutoCompleteComboBoxEditor(ComboBoxEditor wrapped, ObjectToStringConverter stringConverter)
/*     */   {
/*  62 */     this.wrapped = wrapped;
/*  63 */     this.stringConverter = stringConverter;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public Component getEditorComponent()
/*     */   {
/*  70 */     return this.wrapped.getEditorComponent();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public void setItem(Object anObject)
/*     */   {
/*  77 */     this.oldItem = anObject;
/*  78 */     this.wrapped.setItem(this.stringConverter.getPreferredStringForItem(anObject));
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public Object getItem()
/*     */   {
/*  85 */     Object wrappedItem = this.wrapped.getItem();
/*     */     
/*  87 */     String[] oldAsStrings = this.stringConverter.getPossibleStringsForItem(this.oldItem);
/*  88 */     int i = 0; for (int n = oldAsStrings.length; i < n; i++) {
/*  89 */       String oldAsString = oldAsStrings[i];
/*  90 */       if ((oldAsString != null) && (oldAsString.equals(wrappedItem))) {
/*  91 */         return this.oldItem;
/*     */       }
/*     */     }
/*  94 */     return null;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public void selectAll()
/*     */   {
/* 101 */     this.wrapped.selectAll();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public void addActionListener(ActionListener l)
/*     */   {
/* 108 */     this.wrapped.addActionListener(l);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public void removeActionListener(ActionListener l)
/*     */   {
/* 115 */     this.wrapped.removeActionListener(l);
/*     */   }
/*     */ }


/* Location:              E:\java\java学习\hutubill\lib\all.jar!\org\jdesktop\swingx\autocomplete\AutoCompleteComboBoxEditor.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */
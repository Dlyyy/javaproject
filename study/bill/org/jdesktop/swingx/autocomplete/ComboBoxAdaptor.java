/*     */ package org.jdesktop.swingx.autocomplete;
/*     */ 
/*     */ import java.awt.Rectangle;
/*     */ import java.awt.event.ActionEvent;
/*     */ import java.awt.event.ActionListener;
/*     */ import javax.accessibility.Accessible;
/*     */ import javax.swing.ComboBoxEditor;
/*     */ import javax.swing.ComboBoxModel;
/*     */ import javax.swing.JComboBox;
/*     */ import javax.swing.JList;
/*     */ import javax.swing.ListModel;
/*     */ import javax.swing.plaf.ComboBoxUI;
/*     */ import javax.swing.plaf.basic.ComboPopup;
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
/*     */ public class ComboBoxAdaptor
/*     */   extends AbstractAutoCompleteAdaptor
/*     */   implements ActionListener
/*     */ {
/*     */   private JComboBox comboBox;
/*     */   
/*     */   public ComboBoxAdaptor(JComboBox comboBox)
/*     */   {
/*  49 */     this.comboBox = comboBox;
/*     */     
/*  51 */     comboBox.addActionListener(this);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void actionPerformed(ActionEvent actionEvent)
/*     */   {
/*  60 */     markEntireText();
/*     */   }
/*     */   
/*     */   public int getItemCount()
/*     */   {
/*  65 */     return this.comboBox.getItemCount();
/*     */   }
/*     */   
/*     */   public Object getItem(int index)
/*     */   {
/*  70 */     return this.comboBox.getItemAt(index);
/*     */   }
/*     */   
/*     */ 
/*     */   public void setSelectedItem(Object item)
/*     */   {
/*  76 */     if (item == getSelectedItem()) {
/*  77 */       return;
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*  87 */     Accessible a = this.comboBox.getUI().getAccessibleChild(this.comboBox, 0);
/*     */     
/*  89 */     if ((getItemCount() > 0) && ((a instanceof ComboPopup))) {
/*  90 */       JList list = ((ComboPopup)a).getList();
/*  91 */       int lastIndex = list.getModel().getSize() - 1;
/*     */       
/*  93 */       Rectangle rect = list.getCellBounds(lastIndex, lastIndex);
/*     */       
/*  95 */       if (rect == null) {
/*  96 */         throw new IllegalStateException("attempting to access index " + lastIndex + " for " + this.comboBox);
/*     */       }
/*     */       
/*     */ 
/* 100 */       list.scrollRectToVisible(rect);
/*     */     }
/*     */     
/*     */ 
/* 104 */     this.comboBox.setSelectedItem(item);
/*     */   }
/*     */   
/*     */   public Object getSelectedItem()
/*     */   {
/* 109 */     return this.comboBox.getModel().getSelectedItem();
/*     */   }
/*     */   
/*     */ 
/*     */   public JTextComponent getTextComponent()
/*     */   {
/* 115 */     return (JTextComponent)this.comboBox.getEditor().getEditorComponent();
/*     */   }
/*     */ }


/* Location:              E:\java\java学习\hutubill\lib\all.jar!\org\jdesktop\swingx\autocomplete\ComboBoxAdaptor.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */
/*     */ package org.jdesktop.swingx.combobox;
/*     */ 
/*     */ import java.awt.event.ActionEvent;
/*     */ import java.awt.event.ActionListener;
/*     */ import java.util.List;
/*     */ import javax.swing.AbstractListModel;
/*     */ import javax.swing.ComboBoxModel;
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
/*     */ 
/*     */ public class ListComboBoxModel<E>
/*     */   extends AbstractListModel
/*     */   implements ComboBoxModel, ActionListener
/*     */ {
/*     */   public static final String UPDATE = "update";
/*     */   protected final List<E> data;
/*     */   protected E selected;
/*     */   
/*     */   public ListComboBoxModel(List<E> list)
/*     */   {
/*  66 */     this.data = list;
/*     */     
/*  68 */     if (list.size() > 0) {
/*  69 */       this.selected = list.get(0);
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
/*     */ 
/*     */   public void setSelectedItem(Object item)
/*     */   {
/*  85 */     if (((this.selected != null) && (!this.selected.equals(item))) || ((this.selected == null) && (item != null)))
/*     */     {
/*  87 */       this.selected = item;
/*  88 */       fireContentsChanged(this, -1, -1);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public E getSelectedItem()
/*     */   {
/*  96 */     return (E)this.selected;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public E getElementAt(int index)
/*     */   {
/* 103 */     return (E)this.data.get(index);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public int getSize()
/*     */   {
/* 110 */     return this.data.size();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public void actionPerformed(ActionEvent evt)
/*     */   {
/* 117 */     if (evt.getActionCommand().equals("update")) {
/* 118 */       fireContentsChanged(this, 0, getSize() - 1);
/*     */     }
/*     */   }
/*     */ }


/* Location:              E:\java\java学习\hutubill\lib\all.jar!\org\jdesktop\swingx\combobox\ListComboBoxModel.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */
/*     */ package org.jdesktop.swingx.autocomplete;
/*     */ 
/*     */ import javax.swing.JList;
/*     */ import javax.swing.ListModel;
/*     */ import javax.swing.event.ListSelectionEvent;
/*     */ import javax.swing.event.ListSelectionListener;
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
/*     */ public class ListAdaptor
/*     */   extends AbstractAutoCompleteAdaptor
/*     */   implements ListSelectionListener
/*     */ {
/*     */   JList list;
/*     */   JTextComponent textComponent;
/*     */   ObjectToStringConverter stringConverter;
/*     */   
/*     */   public ListAdaptor(JList list, JTextComponent textComponent)
/*     */   {
/*  50 */     this(list, textComponent, ObjectToStringConverter.DEFAULT_IMPLEMENTATION);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public ListAdaptor(JList list, JTextComponent textComponent, ObjectToStringConverter stringConverter)
/*     */   {
/*  62 */     this.list = list;
/*  63 */     this.textComponent = textComponent;
/*  64 */     this.stringConverter = stringConverter;
/*     */     
/*  66 */     list.addListSelectionListener(this);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void valueChanged(ListSelectionEvent listSelectionEvent)
/*     */   {
/*  76 */     getTextComponent().setText(this.stringConverter.getPreferredStringForItem(this.list.getSelectedValue()));
/*     */     
/*  78 */     markEntireText();
/*     */   }
/*     */   
/*     */   public Object getSelectedItem()
/*     */   {
/*  83 */     return this.list.getSelectedValue();
/*     */   }
/*     */   
/*     */   public int getItemCount()
/*     */   {
/*  88 */     return this.list.getModel().getSize();
/*     */   }
/*     */   
/*     */   public Object getItem(int index)
/*     */   {
/*  93 */     return this.list.getModel().getElementAt(index);
/*     */   }
/*     */   
/*     */   public void setSelectedItem(Object item)
/*     */   {
/*  98 */     this.list.setSelectedValue(item, true);
/*     */   }
/*     */   
/*     */   public JTextComponent getTextComponent()
/*     */   {
/* 103 */     return this.textComponent;
/*     */   }
/*     */ }


/* Location:              E:\java\java学习\hutubill\lib\all.jar!\org\jdesktop\swingx\autocomplete\ListAdaptor.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */
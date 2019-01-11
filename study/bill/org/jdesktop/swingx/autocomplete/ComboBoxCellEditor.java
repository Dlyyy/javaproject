/*     */ package org.jdesktop.swingx.autocomplete;
/*     */ 
/*     */ import java.awt.event.ActionEvent;
/*     */ import java.awt.event.MouseEvent;
/*     */ import java.util.EventObject;
/*     */ import javax.swing.ComboBoxEditor;
/*     */ import javax.swing.DefaultCellEditor;
/*     */ import javax.swing.DefaultCellEditor.EditorDelegate;
/*     */ import javax.swing.JComboBox;
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
/*     */ public class ComboBoxCellEditor
/*     */   extends DefaultCellEditor
/*     */ {
/*     */   public ComboBoxCellEditor(final JComboBox comboBox)
/*     */   {
/*  59 */     super(comboBox);
/*     */     
/*  61 */     comboBox.removeActionListener(this.delegate);
/*     */     
/*  63 */     this.delegate = new DefaultCellEditor.EditorDelegate(comboBox)
/*     */     {
/*     */       public void setValue(Object value) {
/*  66 */         comboBox.setSelectedItem(value);
/*     */       }
/*     */       
/*     */       public Object getCellEditorValue() {
/*  70 */         return comboBox.getSelectedItem();
/*     */       }
/*     */       
/*     */       public boolean shouldSelectCell(EventObject anEvent) {
/*  74 */         if ((anEvent instanceof MouseEvent)) {
/*  75 */           MouseEvent e = (MouseEvent)anEvent;
/*  76 */           return e.getID() != 506;
/*     */         }
/*  78 */         return true;
/*     */       }
/*     */       
/*     */       public boolean stopCellEditing() {
/*  82 */         if (comboBox.isEditable())
/*     */         {
/*  84 */           comboBox.actionPerformed(new ActionEvent(ComboBoxCellEditor.this, 0, ""));
/*     */         }
/*     */         
/*  87 */         return super.stopCellEditing();
/*     */       }
/*     */       
/*     */ 
/*     */       public void actionPerformed(ActionEvent e)
/*     */       {
/*  93 */         JTextComponent editorComponent = (JTextComponent)comboBox.getEditor().getEditorComponent();
/*     */         
/*  95 */         if ((editorComponent.getDocument() instanceof AutoCompleteDocument)) {
/*  96 */           AutoCompleteDocument document = (AutoCompleteDocument)editorComponent.getDocument();
/*     */           
/*  98 */           if (!document.selecting) {
/*  99 */             ComboBoxCellEditor.this.stopCellEditing();
/*     */           }
/*     */         }
/*     */         else {
/* 103 */           ComboBoxCellEditor.this.stopCellEditing();
/*     */         }
/*     */       }
/* 106 */     };
/* 107 */     comboBox.addActionListener(this.delegate);
/*     */   }
/*     */ }


/* Location:              E:\java\java学习\hutubill\lib\all.jar!\org\jdesktop\swingx\autocomplete\ComboBoxCellEditor.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */
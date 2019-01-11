/*    */ package org.jdesktop.swingx.plaf;
/*    */ 
/*    */ import java.awt.Insets;
/*    */ import javax.swing.BorderFactory;
/*    */ import javax.swing.JTextField;
/*    */ import javax.swing.border.Border;
/*    */ import javax.swing.plaf.TextUI;
/*    */ import javax.swing.text.JTextComponent;
/*    */ import org.jdesktop.swingx.search.NativeSearchFieldSupport;
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
/*    */ public class PromptTextFieldUI
/*    */   extends PromptTextUI
/*    */ {
/*    */   public PromptTextFieldUI(TextUI delegate)
/*    */   {
/* 28 */     super(delegate);
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   public JTextComponent getPromptComponent(JTextComponent txt)
/*    */   {
/* 37 */     LabelField lbl = (LabelField)super.getPromptComponent(txt);
/* 38 */     JTextField txtField = (JTextField)txt;
/*    */     
/* 40 */     lbl.setHorizontalAlignment(txtField.getHorizontalAlignment());
/* 41 */     lbl.setColumns(txtField.getColumns());
/*    */     
/*    */ 
/* 44 */     lbl.hasFocus = ((txtField.hasFocus()) && (NativeSearchFieldSupport.isNativeSearchField(txtField)));
/*    */     
/*    */ 
/*    */ 
/*    */ 
/* 49 */     NativeSearchFieldSupport.setSearchField(lbl, NativeSearchFieldSupport.isSearchField(txtField));
/*    */     
/* 51 */     NativeSearchFieldSupport.setFindPopupMenu(lbl, NativeSearchFieldSupport.getFindPopupMenu(txtField));
/*    */     
/*    */ 
/*    */ 
/*    */ 
/* 56 */     Border b = txt.getBorder();
/*    */     
/* 58 */     if (b == null) {
/* 59 */       lbl.setBorder(txt.getBorder());
/*    */     } else {
/* 61 */       Insets insets = b.getBorderInsets(txt);
/* 62 */       lbl.setBorder(BorderFactory.createEmptyBorder(insets.top, insets.left, insets.bottom, insets.right));
/*    */     }
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
/* 74 */     return lbl;
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */   protected JTextComponent createPromptComponent()
/*    */   {
/* 82 */     return new LabelField(null);
/*    */   }
/*    */   
/*    */   private static final class LabelField extends JTextField
/*    */   {
/*    */     boolean hasFocus;
/*    */     
/*    */     public boolean hasFocus() {
/* 90 */       return this.hasFocus;
/*    */     }
/*    */   }
/*    */ }


/* Location:              E:\java\java学习\hutubill\lib\all.jar!\org\jdesktop\swingx\plaf\PromptTextFieldUI.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */
/*    */ package org.jdesktop.swingx.plaf;
/*    */ 
/*    */ import javax.swing.JTextArea;
/*    */ import javax.swing.plaf.TextUI;
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
/*    */ public class PromptTextAreaUI
/*    */   extends PromptTextUI
/*    */ {
/* 18 */   private static final JTextArea txt = new JTextArea();
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   public PromptTextAreaUI(TextUI delegate)
/*    */   {
/* 26 */     super(delegate);
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   public JTextComponent getPromptComponent(JTextComponent txt)
/*    */   {
/* 35 */     JTextArea lbl = (JTextArea)super.getPromptComponent(txt);
/* 36 */     JTextArea txtArea = (JTextArea)txt;
/*    */     
/* 38 */     lbl.setColumns(txtArea.getColumns());
/* 39 */     lbl.setRows(txtArea.getRows());
/*    */     
/* 41 */     return lbl;
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */   protected JTextComponent createPromptComponent()
/*    */   {
/* 49 */     txt.updateUI();
/* 50 */     return txt;
/*    */   }
/*    */ }


/* Location:              E:\java\java学习\hutubill\lib\all.jar!\org\jdesktop\swingx\plaf\PromptTextAreaUI.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */
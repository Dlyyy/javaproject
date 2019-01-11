/*    */ package com.birosoft.liquid;
/*    */ 
/*    */ import com.birosoft.liquid.borders.LiquidTextFieldBorder;
/*    */ import java.awt.Graphics;
/*    */ import java.awt.Insets;
/*    */ import javax.swing.JComponent;
/*    */ import javax.swing.UIDefaults;
/*    */ import javax.swing.UIManager;
/*    */ import javax.swing.border.Border;
/*    */ import javax.swing.plaf.ComponentUI;
/*    */ import javax.swing.plaf.basic.BasicTextFieldUI;
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
/*    */ public class LiquidTextFieldUI
/*    */   extends BasicTextFieldUI
/*    */ {
/*    */   static JTextComponent _editor;
/*    */   
/*    */   public static ComponentUI createUI(JComponent c)
/*    */   {
/* 34 */     return new LiquidTextFieldUI();
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */   public void installUI(JComponent c)
/*    */   {
/* 41 */     super.installUI(c);
/*    */   }
/*    */   
/*    */   protected void paintBackground(Graphics g) {
/* 45 */     JTextComponent editor = getComponent();
/*    */     
/* 47 */     if ((_editor == null) || (!_editor.equals(editor))) {
/* 48 */       _editor = editor;
/*    */       
/* 50 */       Insets margin = editor.getMargin();
/* 51 */       Border border = editor.getBorder();
/*    */       
/* 53 */       if ((margin.top > 0) && (margin.left > 0) && (margin.bottom > 0) && (margin.right > 0) && ((border instanceof LiquidTextFieldBorder)))
/*    */       {
/* 55 */         ((LiquidTextFieldBorder)border).setInsets(margin);
/*    */       }
/*    */     }
/*    */     
/* 59 */     if (editor.isEnabled()) {
/* 60 */       g.setColor(editor.getBackground());
/*    */     } else {
/* 62 */       g.setColor(UIManager.getDefaults().getColor("TextField.disabledBackground"));
/*    */     }
/*    */     
/* 65 */     g.fillRect(0, 0, editor.getWidth(), editor.getHeight());
/*    */   }
/*    */ }


/* Location:              E:\java\java学习\hutubill\lib\all.jar!\com\birosoft\liquid\LiquidTextFieldUI.class
 * Java compiler version: 4 (48.0)
 * JD-Core Version:       0.7.1
 */
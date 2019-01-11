/*    */ package org.jdesktop.swingx.plaf.linux;
/*    */ 
/*    */ import java.awt.Component;
/*    */ import java.awt.Graphics;
/*    */ import java.awt.Insets;
/*    */ import javax.swing.JLabel;
/*    */ import javax.swing.border.Border;
/*    */ import org.jdesktop.swingx.util.Contract;
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
/*    */ class SafeBorder
/*    */   implements Border
/*    */ {
/*    */   private Border delegate;
/*    */   
/*    */   public SafeBorder(Border delegate)
/*    */   {
/* 40 */     this.delegate = ((Border)Contract.asNotNull(delegate, "delegate cannot be null"));
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */   public Insets getBorderInsets(Component c)
/*    */   {
/* 48 */     JLabel label = (c instanceof JLabel) ? (JLabel)c : new JLabel();
/*    */     
/*    */ 
/* 51 */     return this.delegate.getBorderInsets(label);
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */   public boolean isBorderOpaque()
/*    */   {
/* 59 */     return this.delegate.isBorderOpaque();
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */   public void paintBorder(Component c, Graphics g, int x, int y, int width, int height)
/*    */   {
/* 67 */     this.delegate.paintBorder(c, g, x, y, width, height);
/*    */   }
/*    */   
/*    */   Border getDelegate() {
/* 71 */     return this.delegate;
/*    */   }
/*    */ }


/* Location:              E:\java\java学习\hutubill\lib\all.jar!\org\jdesktop\swingx\plaf\linux\SafeBorder.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */
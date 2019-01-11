/*    */ package org.jdesktop.swingx.plaf.windows;
/*    */ 
/*    */ import java.awt.Color;
/*    */ import java.awt.Component;
/*    */ import java.awt.Graphics2D;
/*    */ import java.awt.Insets;
/*    */ import javax.swing.BorderFactory;
/*    */ import javax.swing.JComponent;
/*    */ import javax.swing.border.Border;
/*    */ import javax.swing.plaf.BorderUIResource;
/*    */ import javax.swing.plaf.ComponentUI;
/*    */ import org.jdesktop.swingx.JXStatusBar;
/*    */ import org.jdesktop.swingx.plaf.basic.BasicStatusBarUI;
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
/*    */ public class WindowsClassicStatusBarUI
/*    */   extends BasicStatusBarUI
/*    */ {
/*    */   public static ComponentUI createUI(JComponent c)
/*    */   {
/* 59 */     return new WindowsClassicStatusBarUI();
/*    */   }
/*    */   
/*    */   protected void paintBackground(Graphics2D g, JXStatusBar bar) {
/* 63 */     g.setColor(bar.getBackground());
/* 64 */     g.fillRect(0, 0, bar.getWidth(), bar.getHeight());
/*    */     
/*    */ 
/*    */ 
/* 68 */     Border b = BorderFactory.createBevelBorder(1, Color.WHITE, bar.getBackground(), bar.getBackground(), Color.GRAY);
/*    */     
/* 70 */     Insets insets = new Insets(0, 0, 0, 0);
/* 71 */     for (Component c : bar.getComponents()) {
/* 72 */       getSeparatorInsets(insets);
/* 73 */       int x = c.getX() - insets.right;
/* 74 */       int y = c.getY() - 2;
/* 75 */       int w = c.getWidth() + insets.left + insets.right;
/* 76 */       int h = c.getHeight() + 4;
/* 77 */       b.paintBorder(c, g, x, y, w, h);
/*    */     }
/*    */   }
/*    */   
/*    */ 
/*    */   protected void paintSeparator(Graphics2D g, JXStatusBar bar, int x, int y, int w, int h) {}
/*    */   
/*    */   protected int getSeparatorWidth()
/*    */   {
/* 86 */     return 11;
/*    */   }
/*    */   
/*    */   protected BorderUIResource createBorder() {
/* 90 */     return new BorderUIResource(BorderFactory.createEmptyBorder(4, 5, 3, 22));
/*    */   }
/*    */ }


/* Location:              E:\java\java学习\hutubill\lib\all.jar!\org\jdesktop\swingx\plaf\windows\WindowsClassicStatusBarUI.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */
/*    */ package org.jdesktop.swingx.plaf.metal;
/*    */ 
/*    */ import java.awt.Color;
/*    */ import java.awt.GradientPaint;
/*    */ import java.awt.Graphics2D;
/*    */ import java.util.List;
/*    */ import javax.swing.JComponent;
/*    */ import javax.swing.UIManager;
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
/*    */ public class MetalStatusBarUI
/*    */   extends BasicStatusBarUI
/*    */ {
/*    */   public static ComponentUI createUI(JComponent c)
/*    */   {
/* 57 */     return new MetalStatusBarUI();
/*    */   }
/*    */   
/*    */   protected void paintBackground(Graphics2D g, JXStatusBar bar)
/*    */   {
/* 62 */     int w = bar.getWidth();
/* 63 */     int h = bar.getHeight();
/*    */     
/*    */ 
/*    */ 
/* 67 */     List<?> gradient = (List)UIManager.get("MenuBar.gradient");
/*    */     
/* 69 */     if ((gradient != null) && (w > 0) && (0 < h)) {
/* 70 */       float ratio1 = ((Number)gradient.get(0)).floatValue();
/* 71 */       float ratio2 = ((Number)gradient.get(1)).floatValue();
/* 72 */       Color c1 = (Color)gradient.get(2);
/* 73 */       Color c2 = (Color)gradient.get(3);
/* 74 */       Color c3 = (Color)gradient.get(4);
/* 75 */       int mid = (int)(ratio1 * h);
/* 76 */       int mid2 = (int)(ratio2 * h);
/* 77 */       if (mid > 0) {
/* 78 */         g.setPaint(new GradientPaint(0.0F, 0.0F, c1, 0.0F, mid, c2));
/*    */         
/* 80 */         g.fillRect(0, 0, w, mid);
/*    */       }
/* 82 */       if (mid2 > 0) {
/* 83 */         g.setColor(c2);
/* 84 */         g.fillRect(0, mid, w, mid2);
/*    */       }
/* 86 */       if (mid > 0) {
/* 87 */         g.setPaint(new GradientPaint(0.0F, mid + mid2, c2, 0.0F, mid * 2.0F + mid2, c1));
/*    */         
/* 89 */         g.fillRect(0, mid + mid2, w, mid);
/*    */       }
/* 91 */       if (h - mid * 2 - mid2 > 0) {
/* 92 */         g.setPaint(new GradientPaint(0.0F, mid * 2.0F + mid2, c1, 0.0F, h, c3));
/*    */         
/* 94 */         g.fillRect(0, mid * 2 + mid2, w, h - mid * 2 - mid2);
/*    */       }
/*    */     }
/*    */   }
/*    */ }


/* Location:              E:\java\java学习\hutubill\lib\all.jar!\org\jdesktop\swingx\plaf\metal\MetalStatusBarUI.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */
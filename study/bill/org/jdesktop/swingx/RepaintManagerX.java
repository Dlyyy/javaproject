/*    */ package org.jdesktop.swingx;
/*    */ 
/*    */ import java.awt.Container;
/*    */ import java.awt.Rectangle;
/*    */ import javax.swing.JComponent;
/*    */ import javax.swing.RepaintManager;
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
/*    */ @TranslucentRepaintManager
/*    */ public class RepaintManagerX
/*    */   extends ForwardingRepaintManager
/*    */ {
/*    */   public RepaintManagerX(RepaintManager delegate)
/*    */   {
/* 46 */     super(delegate);
/*    */   }
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
/*    */   public void addDirtyRegion(JComponent c, int x, int y, int w, int h)
/*    */   {
/* 63 */     Rectangle dirtyRegion = getDirtyRegion(c);
/* 64 */     if ((dirtyRegion.width == 0) && (dirtyRegion.height == 0)) {
/* 65 */       int lastDeltaX = c.getX();
/* 66 */       int lastDeltaY = c.getY();
/* 67 */       Container parent = c.getParent();
/* 68 */       while ((parent instanceof JComponent)) {
/* 69 */         if ((!parent.isVisible()) || (!parent.isDisplayable())) {
/* 70 */           return;
/*    */         }
/* 72 */         if (((parent instanceof JXPanel)) && ((((JXPanel)parent).getAlpha() < 1.0F) || (!parent.isOpaque())))
/*    */         {
/* 74 */           x += lastDeltaX;
/* 75 */           y += lastDeltaY;
/* 76 */           lastDeltaX = lastDeltaY = 0;
/* 77 */           c = (JComponent)parent;
/*    */         }
/* 79 */         lastDeltaX += parent.getX();
/* 80 */         lastDeltaY += parent.getY();
/* 81 */         parent = parent.getParent();
/*    */       }
/*    */     }
/* 84 */     super.addDirtyRegion(c, x, y, w, h);
/*    */   }
/*    */ }


/* Location:              E:\java\java学习\hutubill\lib\all.jar!\org\jdesktop\swingx\RepaintManagerX.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */
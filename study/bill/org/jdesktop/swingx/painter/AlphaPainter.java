/*    */ package org.jdesktop.swingx.painter;
/*    */ 
/*    */ import java.awt.AlphaComposite;
/*    */ import java.awt.Graphics2D;
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
/*    */ public class AlphaPainter<T>
/*    */   extends CompoundPainter<T>
/*    */ {
/* 34 */   private float alpha = 1.0F;
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */   protected void doPaint(Graphics2D g, T component, int width, int height)
/*    */   {
/* 41 */     Graphics2D g2 = (Graphics2D)g.create();
/*    */     try
/*    */     {
/* 44 */       if (getTransform() != null) {
/* 45 */         g2.setTransform(getTransform());
/*    */       }
/* 47 */       if (this.alpha < 1.0F) {
/* 48 */         g2.setComposite(AlphaComposite.getInstance(3, this.alpha));
/*    */       }
/*    */       
/*    */ 
/* 52 */       super.doPaint(g2, component, width, height);
/*    */     } finally {
/* 54 */       g2.dispose();
/*    */     }
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
/*    */   public float getAlpha()
/*    */   {
/* 84 */     return this.alpha;
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   public void setAlpha(float alpha)
/*    */   {
/* 95 */     float old = getAlpha();
/* 96 */     this.alpha = alpha;
/* 97 */     firePropertyChange("alpha", Float.valueOf(old), Float.valueOf(getAlpha()));
/*    */   }
/*    */ }


/* Location:              E:\java\java学习\hutubill\lib\all.jar!\org\jdesktop\swingx\painter\AlphaPainter.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */
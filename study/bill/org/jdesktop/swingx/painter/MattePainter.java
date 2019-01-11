/*     */ package org.jdesktop.swingx.painter;
/*     */ 
/*     */ import java.awt.Graphics2D;
/*     */ import java.awt.Insets;
/*     */ import java.awt.Paint;
/*     */ import java.awt.Rectangle;
/*     */ import java.awt.Shape;
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
/*     */ public class MattePainter
/*     */   extends AbstractAreaPainter<Object>
/*     */ {
/*     */   public MattePainter() {}
/*     */   
/*     */   public MattePainter(Paint paint)
/*     */   {
/*  68 */     super(paint);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public MattePainter(Paint paint, boolean paintStretched)
/*     */   {
/*  81 */     super(paint);
/*  82 */     setPaintStretched(paintStretched);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   protected void doPaint(Graphics2D g, Object component, int width, int height)
/*     */   {
/*  90 */     Paint p = getFillPaint();
/*     */     
/*  92 */     if (p != null) {
/*  93 */       Insets insets = getInsets();
/*  94 */       int w = width - insets.left - insets.right;
/*  95 */       int h = height - insets.top - insets.bottom;
/*     */       
/*  97 */       if (isPaintStretched()) {
/*  98 */         p = calculateSnappedPaint(p, w, h);
/*     */       }
/*     */       
/* 101 */       g.translate(insets.left, insets.top);
/* 102 */       g.setPaint(p);
/* 103 */       g.fill(provideShape(g, component, w, h));
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   protected Shape provideShape(Graphics2D g, Object comp, int width, int height)
/*     */   {
/* 112 */     return new Rectangle(0, 0, width, height);
/*     */   }
/*     */ }


/* Location:              E:\java\java学习\hutubill\lib\all.jar!\org\jdesktop\swingx\painter\MattePainter.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */
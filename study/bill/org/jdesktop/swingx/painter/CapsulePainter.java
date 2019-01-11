/*     */ package org.jdesktop.swingx.painter;
/*     */ 
/*     */ import java.awt.Graphics2D;
/*     */ import java.awt.Shape;
/*     */ import java.awt.geom.RoundRectangle2D.Double;
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
/*     */ public class CapsulePainter
/*     */   extends AbstractAreaPainter<Object>
/*     */ {
/*     */   private Portion portion;
/*     */   
/*     */   public static enum Portion
/*     */   {
/*  35 */     Top,  Full,  Bottom,  Left,  Right;
/*     */     
/*     */ 
/*     */     private Portion() {}
/*     */   }
/*     */   
/*     */   public CapsulePainter()
/*     */   {
/*  43 */     setPortion(Portion.Full);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public CapsulePainter(Portion portion)
/*     */   {
/*  50 */     setPortion(portion);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public Portion getPortion()
/*     */   {
/*  61 */     return this.portion;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setPortion(Portion portion)
/*     */   {
/*  70 */     Portion old = this.portion;
/*  71 */     this.portion = portion;
/*  72 */     setDirty(true);
/*  73 */     firePropertyChange("portion", old, getPortion());
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected void doPaint(Graphics2D g, Object component, int width, int height)
/*     */   {
/*  82 */     Shape rect = provideShape(g, component, width, height);
/*  83 */     if ((getStyle() == AbstractAreaPainter.Style.BOTH) || (getStyle() == AbstractAreaPainter.Style.FILLED)) {
/*  84 */       g.setPaint(getFillPaint());
/*  85 */       g.fill(rect);
/*     */     }
/*  87 */     if ((getStyle() == AbstractAreaPainter.Style.BOTH) || (getStyle() == AbstractAreaPainter.Style.OUTLINE)) {
/*  88 */       g.setPaint(getBorderPaint());
/*  89 */       g.draw(rect);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   protected Shape provideShape(Graphics2D g, Object comp, int width, int height)
/*     */   {
/*  98 */     int round = 10;
/*  99 */     int rheight = height;
/* 100 */     int ry = 0;
/* 101 */     if (getPortion() == Portion.Top) {
/* 102 */       round = height * 2;
/* 103 */       rheight = height * 2;
/*     */     }
/* 105 */     if (getPortion() == Portion.Bottom) {
/* 106 */       round = height * 2;
/* 107 */       rheight = height * 2;
/* 108 */       ry = -height;
/*     */     }
/*     */     
/*     */ 
/* 112 */     return new RoundRectangle2D.Double(0.0D, ry, width, rheight, round, round);
/*     */   }
/*     */ }


/* Location:              E:\java\java学习\hutubill\lib\all.jar!\org\jdesktop\swingx\painter\CapsulePainter.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */
/*     */ package org.jdesktop.swingx.painter;
/*     */ 
/*     */ import java.awt.Color;
/*     */ import java.awt.Graphics2D;
/*     */ import java.awt.Paint;
/*     */ import java.awt.Rectangle;
/*     */ import java.awt.geom.Area;
/*     */ import java.awt.geom.Ellipse2D;
/*     */ import java.awt.geom.Ellipse2D.Double;
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
/*     */ public class GlossPainter
/*     */   extends AbstractPainter<Object>
/*     */ {
/*     */   private Paint paint;
/*     */   private GlossPosition position;
/*     */   
/*     */   public static enum GlossPosition
/*     */   {
/*  54 */     TOP,  BOTTOM;
/*     */     
/*     */ 
/*     */ 
/*     */     private GlossPosition() {}
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public GlossPainter()
/*     */   {
/*  65 */     this(new Color(1.0F, 1.0F, 1.0F, 0.2F), GlossPosition.TOP);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public GlossPainter(Paint paint)
/*     */   {
/*  75 */     this(paint, GlossPosition.TOP);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public GlossPainter(GlossPosition position)
/*     */   {
/*  85 */     this(new Color(1.0F, 1.0F, 1.0F, 0.2F), position);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public GlossPainter(Paint paint, GlossPosition position)
/*     */   {
/*  96 */     setPaint(paint);
/*  97 */     setPosition(position);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   protected void doPaint(Graphics2D g, Object component, int width, int height)
/*     */   {
/* 105 */     if (getPaint() != null) {
/* 106 */       Ellipse2D ellipse = new Ellipse2D.Double(-width / 2.0D, height / 2.7D, width * 2.0D, height * 2.0D);
/*     */       
/*     */ 
/*     */ 
/* 110 */       Area gloss = new Area(ellipse);
/* 111 */       if (getPosition() == GlossPosition.TOP) {
/* 112 */         Area area = new Area(new Rectangle(0, 0, width, height));
/*     */         
/* 114 */         area.subtract(new Area(ellipse));
/* 115 */         gloss = area;
/*     */       }
/*     */       
/*     */ 
/*     */ 
/*     */ 
/* 121 */       g.setPaint(getPaint());
/* 122 */       g.fill(gloss);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public Paint getPaint()
/*     */   {
/* 132 */     return this.paint;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setPaint(Paint paint)
/*     */   {
/* 143 */     Paint old = this.paint;
/* 144 */     this.paint = paint;
/* 145 */     setDirty(true);
/* 146 */     firePropertyChange("paint", old, getPaint());
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public GlossPosition getPosition()
/*     */   {
/* 155 */     return this.position;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setPosition(GlossPosition position)
/*     */   {
/* 165 */     GlossPosition old = this.position;
/* 166 */     this.position = position;
/* 167 */     setDirty(true);
/* 168 */     firePropertyChange("position", old, getPosition());
/*     */   }
/*     */ }


/* Location:              E:\java\java学习\hutubill\lib\all.jar!\org\jdesktop\swingx\painter\GlossPainter.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */
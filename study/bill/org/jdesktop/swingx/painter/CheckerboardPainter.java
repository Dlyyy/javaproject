/*     */ package org.jdesktop.swingx.painter;
/*     */ 
/*     */ import java.awt.Color;
/*     */ import java.awt.Graphics2D;
/*     */ import java.awt.Paint;
/*     */ import java.awt.Rectangle;
/*     */ import java.awt.TexturePaint;
/*     */ import java.awt.image.BufferedImage;
/*     */ import javax.swing.JComponent;
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
/*     */ public class CheckerboardPainter
/*     */   extends AbstractPainter<Object>
/*     */ {
/*     */   private transient Paint checkerPaint;
/*  63 */   private Paint darkPaint = new Color(204, 204, 204);
/*  64 */   private Paint lightPaint = Color.WHITE;
/*  65 */   private double squareSize = 8.0D;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public CheckerboardPainter() {}
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public CheckerboardPainter(Paint darkPaint, Paint lightPaint)
/*     */   {
/*  82 */     this(darkPaint, lightPaint, 8.0D);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public CheckerboardPainter(Paint darkPaint, Paint lightPaint, double squareSize)
/*     */   {
/*  94 */     this.darkPaint = darkPaint;
/*  95 */     this.lightPaint = lightPaint;
/*  96 */     this.squareSize = squareSize;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setSquareSize(double squareSize)
/*     */   {
/* 107 */     if (squareSize <= 0.0D) {
/* 108 */       throw new IllegalArgumentException("Length must be > 0");
/*     */     }
/*     */     
/* 111 */     double old = getSquareSize();
/* 112 */     this.squareSize = squareSize;
/* 113 */     this.checkerPaint = null;
/* 114 */     setDirty(true);
/* 115 */     firePropertyChange("squareSize", Double.valueOf(old), Double.valueOf(getSquareSize()));
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public double getSquareSize()
/*     */   {
/* 124 */     return this.squareSize;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setDarkPaint(Paint color)
/*     */   {
/* 135 */     Paint old = getDarkPaint();
/* 136 */     this.darkPaint = color;
/* 137 */     this.checkerPaint = null;
/* 138 */     setDirty(true);
/* 139 */     firePropertyChange("darkPaint", old, getDarkPaint());
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public Paint getDarkPaint()
/*     */   {
/* 148 */     return this.darkPaint;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setLightPaint(Paint color)
/*     */   {
/* 159 */     Paint old = getLightPaint();
/* 160 */     this.lightPaint = color;
/* 161 */     this.checkerPaint = null;
/* 162 */     setDirty(true);
/* 163 */     firePropertyChange("lightPaint", old, getLightPaint());
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public Paint getLightPaint()
/*     */   {
/* 172 */     return this.lightPaint;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private Paint getCheckerPaint(Object c)
/*     */   {
/* 181 */     if (this.checkerPaint == null) {
/* 182 */       double sqlength = getSquareSize();
/* 183 */       int length = (int)(sqlength * 2.0D);
/* 184 */       BufferedImage image = new BufferedImage(length, length, 2);
/* 185 */       Graphics2D gfx = image.createGraphics();
/*     */       try
/*     */       {
/* 188 */         Paint p = getLightPaint();
/* 189 */         if ((p == null) && ((c instanceof JComponent))) {
/* 190 */           p = ((JComponent)c).getForeground();
/*     */         }
/* 192 */         gfx.setPaint(p);
/* 193 */         gfx.fillRect(0, 0, length, length);
/* 194 */         p = getDarkPaint();
/* 195 */         if ((p == null) && 
/* 196 */           ((c instanceof JComponent))) {
/* 197 */           p = ((JComponent)c).getBackground();
/*     */         }
/*     */         
/* 200 */         gfx.setPaint(p);
/* 201 */         gfx.fillRect(0, 0, (int)(sqlength - 1.0D), (int)(sqlength - 1.0D));
/* 202 */         gfx.fillRect((int)sqlength, (int)sqlength, (int)sqlength - 1, (int)sqlength - 1);
/*     */       }
/*     */       finally {
/* 205 */         gfx.dispose();
/*     */       }
/*     */       
/* 208 */       this.checkerPaint = new TexturePaint(image, new Rectangle(0, 0, image.getWidth(), image.getHeight()));
/*     */     }
/* 210 */     return this.checkerPaint;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   protected void doPaint(Graphics2D g, Object t, int width, int height)
/*     */   {
/* 218 */     g.setPaint(getCheckerPaint(t));
/* 219 */     g.fillRect(0, 0, width, height);
/*     */   }
/*     */ }


/* Location:              E:\java\java学习\hutubill\lib\all.jar!\org\jdesktop\swingx\painter\CheckerboardPainter.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */
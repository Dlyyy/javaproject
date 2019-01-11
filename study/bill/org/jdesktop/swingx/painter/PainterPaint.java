/*     */ package org.jdesktop.swingx.painter;
/*     */ 
/*     */ import java.awt.Graphics2D;
/*     */ import java.awt.Paint;
/*     */ import java.awt.PaintContext;
/*     */ import java.awt.Rectangle;
/*     */ import java.awt.RenderingHints;
/*     */ import java.awt.geom.AffineTransform;
/*     */ import java.awt.geom.Rectangle2D;
/*     */ import java.awt.image.BufferedImage;
/*     */ import java.awt.image.ColorModel;
/*     */ import java.awt.image.Raster;
/*     */ import org.jdesktop.swingx.graphics.GraphicsUtilities;
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
/*     */ public class PainterPaint<T>
/*     */   implements Paint
/*     */ {
/*     */   private final PainterPaintContext<T> context;
/*     */   
/*     */   protected static class PainterPaintContext<T>
/*     */     implements PaintContext
/*     */   {
/*     */     private Painter<T> painter;
/*     */     private T object;
/*     */     private BufferedImage saved;
/*     */     
/*     */     public PainterPaintContext(Painter<T> painter, T object)
/*     */     {
/*  48 */       painter.getClass();
/*  49 */       this.painter = painter;
/*  50 */       this.object = object;
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     public void dispose() {}
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     public ColorModel getColorModel()
/*     */     {
/*  66 */       if (this.saved == null) {
/*  67 */         return GraphicsUtilities.createCompatibleImage(1, 1).getColorModel();
/*     */       }
/*     */       
/*  70 */       return this.saved.getColorModel();
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */     public Raster getRaster(int x, int y, int w, int h)
/*     */     {
/*  78 */       if ((this.saved == null) || (this.saved.getWidth() != w) || (this.saved.getHeight() != h)) {
/*  79 */         this.saved = GraphicsUtilities.createCompatibleImage(w, h);
/*  80 */         Graphics2D g2d = this.saved.createGraphics();
/*     */         try
/*     */         {
/*  83 */           if ((this.painter instanceof AbstractPainter)) {
/*  84 */             ((AbstractPainter)this.painter).setInPaintContext(true);
/*     */           }
/*  86 */           this.painter.paint(g2d, this.object, w, h);
/*     */         } finally {
/*  88 */           g2d.dispose();
/*  89 */           if ((this.painter instanceof AbstractPainter)) {
/*  90 */             ((AbstractPainter)this.painter).setInPaintContext(false);
/*     */           }
/*     */         }
/*     */       }
/*     */       
/*  95 */       return this.saved.getData();
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */   public PainterPaint(Painter<T> painter, T object)
/*     */   {
/* 102 */     this.context = new PainterPaintContext(painter, object);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public PaintContext createContext(ColorModel cm, Rectangle deviceBounds, Rectangle2D userBounds, AffineTransform xform, RenderingHints hints)
/*     */   {
/* 111 */     return this.context;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public int getTransparency()
/*     */   {
/* 119 */     return 2;
/*     */   }
/*     */ }


/* Location:              E:\java\java学习\hutubill\lib\all.jar!\org\jdesktop\swingx\painter\PainterPaint.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */
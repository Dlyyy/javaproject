/*    */ package org.jdesktop.swingx.image;
/*    */ 
/*    */ import java.awt.Rectangle;
/*    */ import java.awt.RenderingHints;
/*    */ import java.awt.geom.Point2D;
/*    */ import java.awt.geom.Rectangle2D;
/*    */ import java.awt.image.BufferedImage;
/*    */ import java.awt.image.BufferedImageOp;
/*    */ import java.awt.image.ColorModel;
/*    */ import org.jdesktop.beans.AbstractBean;
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
/*    */ 
/*    */ 
/*    */ 
/*    */ public abstract class AbstractFilter
/*    */   extends AbstractBean
/*    */   implements BufferedImageOp
/*    */ {
/*    */   public abstract BufferedImage filter(BufferedImage paramBufferedImage1, BufferedImage paramBufferedImage2);
/*    */   
/*    */   public Rectangle2D getBounds2D(BufferedImage src)
/*    */   {
/* 62 */     return new Rectangle(0, 0, src.getWidth(), src.getHeight());
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */   public BufferedImage createCompatibleDestImage(BufferedImage src, ColorModel destCM)
/*    */   {
/* 70 */     if (destCM == null) {
/* 71 */       destCM = src.getColorModel();
/*    */     }
/*    */     
/* 74 */     return new BufferedImage(destCM, destCM.createCompatibleWritableRaster(src.getWidth(), src.getHeight()), destCM.isAlphaPremultiplied(), null);
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   public Point2D getPoint2D(Point2D srcPt, Point2D dstPt)
/*    */   {
/* 84 */     return (Point2D)srcPt.clone();
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */   public RenderingHints getRenderingHints()
/*    */   {
/* 91 */     return null;
/*    */   }
/*    */ }


/* Location:              E:\java\java学习\hutubill\lib\all.jar!\org\jdesktop\swingx\image\AbstractFilter.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */
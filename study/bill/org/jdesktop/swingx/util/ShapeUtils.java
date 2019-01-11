/*    */ package org.jdesktop.swingx.util;
/*    */ 
/*    */ import java.awt.Font;
/*    */ import java.awt.Graphics2D;
/*    */ import java.awt.Polygon;
/*    */ import java.awt.Shape;
/*    */ import java.awt.font.GlyphVector;
/*    */ import java.awt.geom.AffineTransform;
/*    */ import java.awt.geom.Ellipse2D.Float;
/*    */ import java.awt.geom.GeneralPath;
/*    */ import java.awt.geom.Point2D;
/*    */ import java.awt.geom.Point2D.Float;
/*    */ import java.awt.geom.Rectangle2D;
/*    */ import java.awt.image.BufferedImage;
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
/*    */ public final class ShapeUtils
/*    */ {
/*    */   public static Shape generatePolygon(int sides, int outsideRadius, boolean normalize)
/*    */   {
/* 45 */     return generatePolygon(sides, outsideRadius, 0, normalize);
/*    */   }
/*    */   
/*    */   public static Shape generatePolygon(int sides, int outsideRadius, int insideRadius, boolean normalize) {
/* 49 */     Shape shape = generatePolygon(sides, outsideRadius, insideRadius);
/* 50 */     if (normalize) {
/* 51 */       Rectangle2D bounds = shape.getBounds2D();
/* 52 */       GeneralPath path = new GeneralPath(shape);
/* 53 */       shape = path.createTransformedShape(AffineTransform.getTranslateInstance(-bounds.getX(), -bounds.getY()));
/*    */     }
/*    */     
/* 56 */     return shape;
/*    */   }
/*    */   
/*    */ 
/*    */   public static Shape generatePolygon(int sides, int outsideRadius, int insideRadius)
/*    */   {
/* 62 */     if (sides < 3) {
/* 63 */       return new Ellipse2D.Float(0.0F, 0.0F, 10.0F, 10.0F);
/*    */     }
/*    */     
/* 66 */     AffineTransform trans = new AffineTransform();
/* 67 */     Polygon poly = new Polygon();
/* 68 */     for (int i = 0; i < sides; i++) {
/* 69 */       trans.rotate(6.283185307179586D / sides / 2.0D);
/* 70 */       Point2D out = trans.transform(new Point2D.Float(0.0F, outsideRadius), null);
/* 71 */       poly.addPoint((int)out.getX(), (int)out.getY());
/* 72 */       trans.rotate(6.283185307179586D / sides / 2.0D);
/* 73 */       if (insideRadius > 0) {
/* 74 */         Point2D in = trans.transform(new Point2D.Float(0.0F, insideRadius), null);
/* 75 */         poly.addPoint((int)in.getX(), (int)in.getY());
/*    */       }
/*    */     }
/*    */     
/* 79 */     return poly;
/*    */   }
/*    */   
/*    */   public static Shape generateShapeFromText(Font font, char ch) {
/* 83 */     return generateShapeFromText(font, String.valueOf(ch));
/*    */   }
/*    */   
/*    */   public static Shape generateShapeFromText(Font font, String string) {
/* 87 */     BufferedImage img = new BufferedImage(100, 100, 2);
/* 88 */     Graphics2D g2 = img.createGraphics();
/*    */     try
/*    */     {
/* 91 */       GlyphVector vect = font.createGlyphVector(g2.getFontRenderContext(), string);
/* 92 */       Shape shape = vect.getOutline(0.0F, (float)-vect.getVisualBounds().getY());
/*    */       
/* 94 */       return shape;
/*    */     } finally {
/* 96 */       g2.dispose();
/*    */     }
/*    */   }
/*    */ }


/* Location:              E:\java\java学习\hutubill\lib\all.jar!\org\jdesktop\swingx\util\ShapeUtils.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */
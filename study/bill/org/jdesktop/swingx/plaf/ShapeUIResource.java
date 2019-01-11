/*    */ package org.jdesktop.swingx.plaf;
/*    */ 
/*    */ import java.awt.Rectangle;
/*    */ import java.awt.Shape;
/*    */ import java.awt.geom.AffineTransform;
/*    */ import java.awt.geom.PathIterator;
/*    */ import java.awt.geom.Point2D;
/*    */ import java.awt.geom.Rectangle2D;
/*    */ import javax.swing.plaf.UIResource;
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
/*    */ public class ShapeUIResource
/*    */   implements Shape, UIResource
/*    */ {
/*    */   private Shape s;
/*    */   
/*    */   public ShapeUIResource(Shape p)
/*    */   {
/* 44 */     this.s = p;
/*    */   }
/*    */   
/*    */   public boolean contains(Point2D p) {
/* 48 */     return this.s.contains(p);
/*    */   }
/*    */   
/*    */   public boolean contains(Rectangle2D r) {
/* 52 */     return this.s.contains(r);
/*    */   }
/*    */   
/*    */   public boolean contains(double x, double y) {
/* 56 */     return this.s.contains(x, y);
/*    */   }
/*    */   
/*    */   public boolean contains(double x, double y, double w, double h) {
/* 60 */     return this.s.contains(x, y, w, h);
/*    */   }
/*    */   
/*    */   public Rectangle getBounds() {
/* 64 */     return this.s.getBounds();
/*    */   }
/*    */   
/*    */   public Rectangle2D getBounds2D() {
/* 68 */     return this.s.getBounds2D();
/*    */   }
/*    */   
/*    */   public PathIterator getPathIterator(AffineTransform at) {
/* 72 */     return this.s.getPathIterator(at);
/*    */   }
/*    */   
/*    */   public PathIterator getPathIterator(AffineTransform at, double flatness) {
/* 76 */     return this.s.getPathIterator(at, flatness);
/*    */   }
/*    */   
/*    */   public boolean intersects(Rectangle2D r) {
/* 80 */     return this.s.intersects(r);
/*    */   }
/*    */   
/*    */   public boolean intersects(double x, double y, double w, double h) {
/* 84 */     return this.s.intersects(x, y, w, h);
/*    */   }
/*    */ }


/* Location:              E:\java\java学习\hutubill\lib\all.jar!\org\jdesktop\swingx\plaf\ShapeUIResource.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */
/*     */ package org.jdesktop.swingx.geom;
/*     */ 
/*     */ import java.awt.Rectangle;
/*     */ import java.awt.Shape;
/*     */ import java.awt.geom.AffineTransform;
/*     */ import java.awt.geom.GeneralPath;
/*     */ import java.awt.geom.PathIterator;
/*     */ import java.awt.geom.Point2D;
/*     */ import java.awt.geom.Rectangle2D;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class Star2D
/*     */   implements Shape
/*     */ {
/*     */   private Shape starShape;
/*     */   private double x;
/*     */   private double y;
/*     */   private double innerRadius;
/*     */   private double outerRadius;
/*     */   private int branchesCount;
/*     */   
/*     */   public Star2D(double x, double y, double innerRadius, double outerRadius, int branchesCount)
/*     */   {
/*  81 */     if (branchesCount < 3) {
/*  82 */       throw new IllegalArgumentException("The number of branches must be >= 3.");
/*     */     }
/*  84 */     if (innerRadius >= outerRadius) {
/*  85 */       throw new IllegalArgumentException("The inner radius must be < outer radius.");
/*     */     }
/*     */     
/*     */ 
/*  89 */     this.x = x;
/*  90 */     this.y = y;
/*  91 */     this.innerRadius = innerRadius;
/*  92 */     this.outerRadius = outerRadius;
/*  93 */     this.branchesCount = branchesCount;
/*     */     
/*  95 */     this.starShape = generateStar(x, y, innerRadius, outerRadius, branchesCount);
/*     */   }
/*     */   
/*     */ 
/*     */   private static Shape generateStar(double x, double y, double innerRadius, double outerRadius, int branchesCount)
/*     */   {
/* 101 */     GeneralPath path = new GeneralPath();
/*     */     
/* 103 */     double outerAngleIncrement = 6.283185307179586D / branchesCount;
/*     */     
/* 105 */     double outerAngle = branchesCount % 2 == 0 ? 0.0D : -1.5707963267948966D;
/* 106 */     double innerAngle = outerAngleIncrement / 2.0D + outerAngle;
/*     */     
/* 108 */     float x1 = (float)(Math.cos(outerAngle) * outerRadius + x);
/* 109 */     float y1 = (float)(Math.sin(outerAngle) * outerRadius + y);
/*     */     
/* 111 */     float x2 = (float)(Math.cos(innerAngle) * innerRadius + x);
/* 112 */     float y2 = (float)(Math.sin(innerAngle) * innerRadius + y);
/*     */     
/* 114 */     path.moveTo(x1, y1);
/* 115 */     path.lineTo(x2, y2);
/*     */     
/* 117 */     outerAngle += outerAngleIncrement;
/* 118 */     innerAngle += outerAngleIncrement;
/*     */     
/* 120 */     for (int i = 1; i < branchesCount; i++) {
/* 121 */       x1 = (float)(Math.cos(outerAngle) * outerRadius + x);
/* 122 */       y1 = (float)(Math.sin(outerAngle) * outerRadius + y);
/*     */       
/* 124 */       path.lineTo(x1, y1);
/*     */       
/* 126 */       x2 = (float)(Math.cos(innerAngle) * innerRadius + x);
/* 127 */       y2 = (float)(Math.sin(innerAngle) * innerRadius + y);
/*     */       
/* 129 */       path.lineTo(x2, y2);
/*     */       
/* 131 */       outerAngle += outerAngleIncrement;
/* 132 */       innerAngle += outerAngleIncrement;
/*     */     }
/*     */     
/* 135 */     path.closePath();
/* 136 */     return path;
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
/*     */   public void setInnerRadius(double innerRadius)
/*     */   {
/* 149 */     if (innerRadius >= this.outerRadius) {
/* 150 */       throw new IllegalArgumentException("The inner radius must be < outer radius.");
/*     */     }
/*     */     
/*     */ 
/* 154 */     this.innerRadius = innerRadius;
/* 155 */     this.starShape = generateStar(getX(), getY(), innerRadius, getOuterRadius(), getBranchesCount());
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setX(double x)
/*     */   {
/* 165 */     this.x = x;
/* 166 */     this.starShape = generateStar(x, getY(), getInnerRadius(), getOuterRadius(), getBranchesCount());
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setY(double y)
/*     */   {
/* 176 */     this.y = y;
/* 177 */     this.starShape = generateStar(getX(), y, getInnerRadius(), getOuterRadius(), getBranchesCount());
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
/*     */ 
/*     */   public void setOuterRadius(double outerRadius)
/*     */   {
/* 191 */     if (this.innerRadius >= outerRadius) {
/* 192 */       throw new IllegalArgumentException("The outer radius must be > inner radius.");
/*     */     }
/*     */     
/*     */ 
/* 196 */     this.outerRadius = outerRadius;
/* 197 */     this.starShape = generateStar(getX(), getY(), getInnerRadius(), outerRadius, getBranchesCount());
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setBranchesCount(int branchesCount)
/*     */   {
/* 209 */     if (branchesCount <= 2) {
/* 210 */       throw new IllegalArgumentException("The number of branches must be >= 3.");
/*     */     }
/*     */     
/*     */ 
/* 214 */     this.branchesCount = branchesCount;
/* 215 */     this.starShape = generateStar(getX(), getY(), getInnerRadius(), getOuterRadius(), branchesCount);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public double getX()
/*     */   {
/* 225 */     return this.x;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public double getY()
/*     */   {
/* 234 */     return this.y;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public double getInnerRadius()
/*     */   {
/* 244 */     return this.innerRadius;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public double getOuterRadius()
/*     */   {
/* 254 */     return this.outerRadius;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public int getBranchesCount()
/*     */   {
/* 263 */     return this.branchesCount;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public Rectangle getBounds()
/*     */   {
/* 270 */     return this.starShape.getBounds();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public Rectangle2D getBounds2D()
/*     */   {
/* 277 */     return this.starShape.getBounds2D();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public boolean contains(double x, double y)
/*     */   {
/* 284 */     return this.starShape.contains(x, y);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public boolean contains(Point2D p)
/*     */   {
/* 291 */     return this.starShape.contains(p);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public boolean intersects(double x, double y, double w, double h)
/*     */   {
/* 298 */     return this.starShape.intersects(x, y, w, h);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public boolean intersects(Rectangle2D r)
/*     */   {
/* 305 */     return this.starShape.intersects(r);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public boolean contains(double x, double y, double w, double h)
/*     */   {
/* 312 */     return this.starShape.contains(x, y, w, h);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public boolean contains(Rectangle2D r)
/*     */   {
/* 319 */     return this.starShape.contains(r);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public PathIterator getPathIterator(AffineTransform at)
/*     */   {
/* 326 */     return this.starShape.getPathIterator(at);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public PathIterator getPathIterator(AffineTransform at, double flatness)
/*     */   {
/* 333 */     return this.starShape.getPathIterator(at, flatness);
/*     */   }
/*     */ }


/* Location:              E:\java\java学习\hutubill\lib\all.jar!\org\jdesktop\swingx\geom\Star2D.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */
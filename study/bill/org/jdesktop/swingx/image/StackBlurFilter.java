/*     */ package org.jdesktop.swingx.image;
/*     */ 
/*     */ import java.awt.image.BufferedImage;
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
/*     */ public class StackBlurFilter
/*     */   extends AbstractFilter
/*     */ {
/*     */   private final int radius;
/*     */   private final int iterations;
/*     */   
/*     */   public StackBlurFilter()
/*     */   {
/*  63 */     this(3, 3);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public StackBlurFilter(int radius)
/*     */   {
/*  73 */     this(radius, 3);
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
/*     */   public StackBlurFilter(int radius, int iterations)
/*     */   {
/*  87 */     if (radius < 1) {
/*  88 */       radius = 1;
/*     */     }
/*  90 */     if (iterations < 1) {
/*  91 */       iterations = 1;
/*     */     }
/*     */     
/*  94 */     this.radius = radius;
/*  95 */     this.iterations = iterations;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public int getEffectiveRadius()
/*     */   {
/* 105 */     return getIterations() * getRadius();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public int getRadius()
/*     */   {
/* 114 */     return this.radius;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public int getIterations()
/*     */   {
/* 124 */     return this.iterations;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public BufferedImage filter(BufferedImage src, BufferedImage dst)
/*     */   {
/* 132 */     int width = src.getWidth();
/* 133 */     int height = src.getHeight();
/*     */     
/* 135 */     if (dst == null) {
/* 136 */       dst = createCompatibleDestImage(src, null);
/*     */     }
/*     */     
/* 139 */     int[] srcPixels = new int[width * height];
/* 140 */     int[] dstPixels = new int[width * height];
/*     */     
/* 142 */     GraphicsUtilities.getPixels(src, 0, 0, width, height, srcPixels);
/* 143 */     for (int i = 0; i < this.iterations; i++)
/*     */     {
/* 145 */       FastBlurFilter.blur(srcPixels, dstPixels, width, height, this.radius);
/*     */       
/* 147 */       FastBlurFilter.blur(dstPixels, srcPixels, height, width, this.radius);
/*     */     }
/*     */     
/* 150 */     GraphicsUtilities.setPixels(dst, 0, 0, width, height, srcPixels);
/*     */     
/* 152 */     return dst;
/*     */   }
/*     */ }


/* Location:              E:\java\java学习\hutubill\lib\all.jar!\org\jdesktop\swingx\image\StackBlurFilter.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */
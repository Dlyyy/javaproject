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
/*     */ public class GaussianBlurFilter
/*     */   extends AbstractFilter
/*     */ {
/*     */   private final int radius;
/*     */   
/*     */   public GaussianBlurFilter()
/*     */   {
/*  48 */     this(3);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public GaussianBlurFilter(int radius)
/*     */   {
/*  58 */     if (radius < 1) {
/*  59 */       radius = 1;
/*     */     }
/*     */     
/*  62 */     this.radius = radius;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public int getRadius()
/*     */   {
/*  71 */     return this.radius;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public BufferedImage filter(BufferedImage src, BufferedImage dst)
/*     */   {
/*  80 */     int width = src.getWidth();
/*  81 */     int height = src.getHeight();
/*     */     
/*  83 */     if (dst == null) {
/*  84 */       dst = createCompatibleDestImage(src, null);
/*     */     }
/*     */     
/*  87 */     int[] srcPixels = new int[width * height];
/*  88 */     int[] dstPixels = new int[width * height];
/*     */     
/*  90 */     float[] kernel = createGaussianKernel(this.radius);
/*     */     
/*  92 */     GraphicsUtilities.getPixels(src, 0, 0, width, height, srcPixels);
/*     */     
/*  94 */     blur(srcPixels, dstPixels, width, height, kernel, this.radius);
/*     */     
/*  96 */     blur(dstPixels, srcPixels, height, width, kernel, this.radius);
/*     */     
/*  98 */     GraphicsUtilities.setPixels(dst, 0, 0, width, height, srcPixels);
/*     */     
/* 100 */     return dst;
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
/*     */   static void blur(int[] srcPixels, int[] dstPixels, int width, int height, float[] kernel, int radius)
/*     */   {
/* 131 */     for (int y = 0; y < height; y++) {
/* 132 */       int index = y;
/* 133 */       int offset = y * width;
/*     */       
/* 135 */       for (int x = 0; x < width; x++) { float b;
/* 136 */         float g; float r; float a = r = g = b = 0.0F;
/*     */         
/* 138 */         for (int i = -radius; i <= radius; i++) {
/* 139 */           int subOffset = x + i;
/* 140 */           if ((subOffset < 0) || (subOffset >= width)) {
/* 141 */             subOffset = (x + width) % width;
/*     */           }
/*     */           
/* 144 */           int pixel = srcPixels[(offset + subOffset)];
/* 145 */           float blurFactor = kernel[(radius + i)];
/*     */           
/* 147 */           a += blurFactor * (pixel >> 24 & 0xFF);
/* 148 */           r += blurFactor * (pixel >> 16 & 0xFF);
/* 149 */           g += blurFactor * (pixel >> 8 & 0xFF);
/* 150 */           b += blurFactor * (pixel & 0xFF);
/*     */         }
/*     */         
/* 153 */         int ca = (int)(a + 0.5F);
/* 154 */         int cr = (int)(r + 0.5F);
/* 155 */         int cg = (int)(g + 0.5F);
/* 156 */         int cb = (int)(b + 0.5F);
/*     */         
/* 158 */         dstPixels[index] = ((ca > 255 ? 'ÿ' : ca) << 24 | (cr > 255 ? 'ÿ' : cr) << 16 | (cg > 255 ? 'ÿ' : cg) << 8 | (cb > 255 ? 'ÿ' : cb));
/*     */         
/*     */ 
/*     */ 
/* 162 */         index += height;
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   static float[] createGaussianKernel(int radius) {
/* 168 */     if (radius < 1) {
/* 169 */       throw new IllegalArgumentException("Radius must be >= 1");
/*     */     }
/*     */     
/* 172 */     float[] data = new float[radius * 2 + 1];
/*     */     
/* 174 */     float sigma = radius / 3.0F;
/* 175 */     float twoSigmaSquare = 2.0F * sigma * sigma;
/* 176 */     float sigmaRoot = (float)Math.sqrt(twoSigmaSquare * 3.141592653589793D);
/* 177 */     float total = 0.0F;
/*     */     
/* 179 */     for (int i = -radius; i <= radius; i++) {
/* 180 */       float distance = i * i;
/* 181 */       int index = i + radius;
/* 182 */       data[index] = ((float)Math.exp(-distance / twoSigmaSquare) / sigmaRoot);
/* 183 */       total += data[index];
/*     */     }
/*     */     
/* 186 */     for (int i = 0; i < data.length; i++) {
/* 187 */       data[i] /= total;
/*     */     }
/*     */     
/* 190 */     return data;
/*     */   }
/*     */ }


/* Location:              E:\java\java学习\hutubill\lib\all.jar!\org\jdesktop\swingx\image\GaussianBlurFilter.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */
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
/*     */ public class FastBlurFilter
/*     */   extends AbstractFilter
/*     */ {
/*     */   private final int radius;
/*     */   
/*     */   public FastBlurFilter()
/*     */   {
/*  62 */     this(3);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public FastBlurFilter(int radius)
/*     */   {
/*  72 */     if (radius < 1) {
/*  73 */       radius = 1;
/*     */     }
/*     */     
/*  76 */     this.radius = radius;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public int getRadius()
/*     */   {
/*  85 */     return this.radius;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public BufferedImage filter(BufferedImage src, BufferedImage dst)
/*     */   {
/*  93 */     int width = src.getWidth();
/*  94 */     int height = src.getHeight();
/*     */     
/*  96 */     if (dst == null) {
/*  97 */       dst = createCompatibleDestImage(src, null);
/*     */     }
/*     */     
/* 100 */     int[] srcPixels = new int[width * height];
/* 101 */     int[] dstPixels = new int[width * height];
/*     */     
/* 103 */     GraphicsUtilities.getPixels(src, 0, 0, width, height, srcPixels);
/*     */     
/* 105 */     blur(srcPixels, dstPixels, width, height, this.radius);
/*     */     
/*     */ 
/* 108 */     blur(dstPixels, srcPixels, height, width, this.radius);
/*     */     
/* 110 */     GraphicsUtilities.setPixels(dst, 0, 0, width, height, srcPixels);
/*     */     
/* 112 */     return dst;
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
/*     */   static void blur(int[] srcPixels, int[] dstPixels, int width, int height, int radius)
/*     */   {
/* 131 */     int windowSize = radius * 2 + 1;
/* 132 */     int radiusPlusOne = radius + 1;
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 139 */     int srcIndex = 0;
/*     */     
/*     */ 
/*     */ 
/* 143 */     int[] sumLookupTable = new int[256 * windowSize];
/* 144 */     for (int i = 0; i < sumLookupTable.length; i++) {
/* 145 */       sumLookupTable[i] = (i / windowSize);
/*     */     }
/*     */     
/* 148 */     int[] indexLookupTable = new int[radiusPlusOne];
/* 149 */     if (radius < width) {
/* 150 */       for (int i = 0; i < indexLookupTable.length; i++) {
/* 151 */         indexLookupTable[i] = i;
/*     */       }
/*     */     } else {
/* 154 */       for (int i = 0; i < width; i++) {
/* 155 */         indexLookupTable[i] = i;
/*     */       }
/* 157 */       for (int i = width; i < indexLookupTable.length; i++) {
/* 158 */         indexLookupTable[i] = (width - 1);
/*     */       }
/*     */     }
/*     */     
/* 162 */     for (int y = 0; y < height; y++) { int sumBlue;
/* 163 */       int sumGreen; int sumRed; int sumAlpha = sumRed = sumGreen = sumBlue = 0;
/* 164 */       int dstIndex = y;
/*     */       
/* 166 */       int pixel = srcPixels[srcIndex];
/* 167 */       sumAlpha += radiusPlusOne * (pixel >> 24 & 0xFF);
/* 168 */       sumRed += radiusPlusOne * (pixel >> 16 & 0xFF);
/* 169 */       sumGreen += radiusPlusOne * (pixel >> 8 & 0xFF);
/* 170 */       sumBlue += radiusPlusOne * (pixel & 0xFF);
/*     */       
/* 172 */       for (int i = 1; i <= radius; i++) {
/* 173 */         pixel = srcPixels[(srcIndex + indexLookupTable[i])];
/* 174 */         sumAlpha += (pixel >> 24 & 0xFF);
/* 175 */         sumRed += (pixel >> 16 & 0xFF);
/* 176 */         sumGreen += (pixel >> 8 & 0xFF);
/* 177 */         sumBlue += (pixel & 0xFF);
/*     */       }
/*     */       
/* 180 */       for (int x = 0; x < width; x++) {
/* 181 */         dstPixels[dstIndex] = (sumLookupTable[sumAlpha] << 24 | sumLookupTable[sumRed] << 16 | sumLookupTable[sumGreen] << 8 | sumLookupTable[sumBlue]);
/*     */         
/*     */ 
/*     */ 
/* 185 */         dstIndex += height;
/*     */         
/* 187 */         int nextPixelIndex = x + radiusPlusOne;
/* 188 */         if (nextPixelIndex >= width) {
/* 189 */           nextPixelIndex = width - 1;
/*     */         }
/*     */         
/* 192 */         int previousPixelIndex = x - radius;
/* 193 */         if (previousPixelIndex < 0) {
/* 194 */           previousPixelIndex = 0;
/*     */         }
/*     */         
/* 197 */         int nextPixel = srcPixels[(srcIndex + nextPixelIndex)];
/* 198 */         int previousPixel = srcPixels[(srcIndex + previousPixelIndex)];
/*     */         
/* 200 */         sumAlpha += (nextPixel >> 24 & 0xFF);
/* 201 */         sumAlpha -= (previousPixel >> 24 & 0xFF);
/*     */         
/* 203 */         sumRed += (nextPixel >> 16 & 0xFF);
/* 204 */         sumRed -= (previousPixel >> 16 & 0xFF);
/*     */         
/* 206 */         sumGreen += (nextPixel >> 8 & 0xFF);
/* 207 */         sumGreen -= (previousPixel >> 8 & 0xFF);
/*     */         
/* 209 */         sumBlue += (nextPixel & 0xFF);
/* 210 */         sumBlue -= (previousPixel & 0xFF);
/*     */       }
/*     */       
/* 213 */       srcIndex += width;
/*     */     }
/*     */   }
/*     */ }


/* Location:              E:\java\java学习\hutubill\lib\all.jar!\org\jdesktop\swingx\image\FastBlurFilter.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */
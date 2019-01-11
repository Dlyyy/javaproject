/*     */ package org.jdesktop.swingx.image;
/*     */ 
/*     */ import java.awt.Color;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class ColorTintFilter
/*     */   extends AbstractFilter
/*     */ {
/*     */   private final Color mixColor;
/*     */   private final float mixValue;
/*     */   private int[] preMultipliedRed;
/*     */   private int[] preMultipliedGreen;
/*     */   private int[] preMultipliedBlue;
/*     */   
/*     */   public ColorTintFilter(Color mixColor, float mixValue)
/*     */   {
/*  82 */     if (mixColor == null) {
/*  83 */       throw new IllegalArgumentException("mixColor cannot be null");
/*     */     }
/*     */     
/*  86 */     this.mixColor = mixColor;
/*  87 */     if (mixValue < 0.0F) {
/*  88 */       mixValue = 0.0F;
/*  89 */     } else if (mixValue > 1.0F) {
/*  90 */       mixValue = 1.0F;
/*     */     }
/*  92 */     this.mixValue = mixValue;
/*     */     
/*  94 */     int mix_r = (int)(mixColor.getRed() * mixValue);
/*  95 */     int mix_g = (int)(mixColor.getGreen() * mixValue);
/*  96 */     int mix_b = (int)(mixColor.getBlue() * mixValue);
/*     */     
/*     */ 
/*     */ 
/* 100 */     float factor = 1.0F - mixValue;
/* 101 */     this.preMultipliedRed = new int['Ā'];
/* 102 */     this.preMultipliedGreen = new int['Ā'];
/* 103 */     this.preMultipliedBlue = new int['Ā'];
/*     */     
/* 105 */     for (int i = 0; i < 256; i++) {
/* 106 */       int value = (int)(i * factor);
/* 107 */       this.preMultipliedRed[i] = (value + mix_r);
/* 108 */       this.preMultipliedGreen[i] = (value + mix_g);
/* 109 */       this.preMultipliedBlue[i] = (value + mix_b);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public float getMixValue()
/*     */   {
/* 119 */     return this.mixValue;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public Color getMixColor()
/*     */   {
/* 128 */     return this.mixColor;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public BufferedImage filter(BufferedImage src, BufferedImage dst)
/*     */   {
/* 136 */     if (dst == null) {
/* 137 */       dst = createCompatibleDestImage(src, null);
/*     */     }
/*     */     
/* 140 */     int width = src.getWidth();
/* 141 */     int height = src.getHeight();
/*     */     
/* 143 */     int[] pixels = new int[width * height];
/* 144 */     GraphicsUtilities.getPixels(src, 0, 0, width, height, pixels);
/* 145 */     mixColor(pixels);
/* 146 */     GraphicsUtilities.setPixels(dst, 0, 0, width, height, pixels);
/*     */     
/* 148 */     return dst;
/*     */   }
/*     */   
/*     */   private void mixColor(int[] pixels) {
/* 152 */     for (int i = 0; i < pixels.length; i++) {
/* 153 */       int argb = pixels[i];
/* 154 */       pixels[i] = (argb & 0xFF000000 | this.preMultipliedRed[(argb >> 16 & 0xFF)] << 16 | this.preMultipliedGreen[(argb >> 8 & 0xFF)] << 8 | this.preMultipliedBlue[(argb & 0xFF)]);
/*     */     }
/*     */   }
/*     */ }


/* Location:              E:\java\java学习\hutubill\lib\all.jar!\org\jdesktop\swingx\image\ColorTintFilter.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */
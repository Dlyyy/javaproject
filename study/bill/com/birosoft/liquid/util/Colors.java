/*     */ package com.birosoft.liquid.util;
/*     */ 
/*     */ import com.birosoft.liquid.skin.SkinImageCache;
/*     */ import java.awt.Color;
/*     */ import java.awt.Graphics;
/*     */ import java.awt.Image;
/*     */ import java.awt.image.BufferedImage;
/*     */ import java.awt.image.PixelGrabber;
/*     */ import java.io.PrintStream;
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
/*     */ public class Colors
/*     */ {
/*     */   static Image image;
/*     */   static Image newImage;
/*     */   static BufferedImage clearFill;
/*  27 */   static Color buttonBg = new Color(215, 231, 249);
/*  28 */   static Color bg = new Color(246, 245, 244);
/*     */   
/*     */ 
/*     */ 
/*     */   public static Image getImage()
/*     */   {
/*  34 */     return newImage;
/*     */   }
/*     */   
/*     */   public static BufferedImage getClearFill() {
/*  38 */     return clearFill;
/*     */   }
/*     */   
/*     */   public static void drawStipples(Graphics g, JComponent c, Color bg) {
/*  42 */     g.setColor(dark(bg, 103));
/*     */     
/*  44 */     int i = 0;
/*  45 */     int height = c.getHeight();
/*     */     
/*  47 */     while (i < height) {
/*  48 */       g.drawLine(0, i, c.getWidth() - 1, i);
/*  49 */       i++;
/*  50 */       g.drawLine(0, i, c.getWidth() - 1, i);
/*  51 */       i += 3;
/*     */     }
/*     */   }
/*     */   
/*     */   static Color handlesinglepixel(int x, int y, int pixel, Color c) {
/*  56 */     int alpha = pixel >> 24 & 0xFF;
/*  57 */     int red = pixel >> 16 & 0xFF;
/*  58 */     int green = pixel >> 8 & 0xFF;
/*  59 */     int blue = pixel & 0xFF;
/*     */     
/*  61 */     return liquidAlpha(c, new Color(red, green, blue, alpha));
/*     */   }
/*     */   
/*     */   public static void getPixels() {
/*  65 */     SkinImageCache sic = SkinImageCache.getInstance();
/*  66 */     image = sic.getAutomaticImage("button.png");
/*  67 */     clearFill = sic.getBufferedImage("clear_fill.png");
/*     */     
/*  69 */     int x = 0;
/*  70 */     int y = 0;
/*  71 */     int w = image.getWidth(null);
/*  72 */     int h = image.getHeight(null);
/*  73 */     newImage = new BufferedImage(w, h, 6);
/*     */     
/*  75 */     int[] pixels = new int[w * h];
/*  76 */     PixelGrabber pg = new PixelGrabber(image, x, y, w, h, pixels, 0, w);
/*     */     try
/*     */     {
/*  79 */       pg.grabPixels();
/*     */     } catch (InterruptedException e) {
/*  81 */       System.err.println("interrupted waiting for pixels!");
/*     */       
/*  83 */       return;
/*     */     }
/*     */     
/*  86 */     if ((pg.getStatus() & 0x80) != 0) {
/*  87 */       System.err.println("image fetch aborted or errored");
/*     */       
/*  89 */       return;
/*     */     }
/*     */     
/*  92 */     Graphics g = newImage.getGraphics();
/*  93 */     Color colourWith = null;
/*     */     
/*  95 */     for (int j = 0; j < h; j++) {
/*  96 */       for (int i = 0; i < w; i++) {
/*  97 */         if ((i == 0) || (i == 117)) {
/*  98 */           colourWith = bg;
/*  99 */         } else if ((i == 39) || (i == 156)) {
/* 100 */           colourWith = buttonBg;
/* 101 */         } else if (i == 78) {
/* 102 */           colourWith = dark(buttonBg, 115);
/*     */         }
/*     */         
/* 105 */         g.setColor(handlesinglepixel(x + i, y + j, pixels[(j * w + i)], colourWith));
/*     */         
/* 107 */         g.drawLine(x + i, y + j, x + i, y + j);
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   public static Color dark(Color c, int factor) {
/* 113 */     float[] hsv = Color.RGBtoHSB(c.getRed(), c.getGreen(), c.getBlue(), null);
/*     */     
/* 115 */     if ((factor <= 0) || (c.getAlpha() < 255))
/* 116 */       return c;
/* 117 */     if (factor < 100) {
/* 118 */       return light(c, 10000 / factor);
/*     */     }
/*     */     
/* 121 */     int vi = (int)(hsv[2] * 255.0F);
/*     */     
/* 123 */     vi = 100 * vi / factor;
/*     */     
/* 125 */     float v = vi / 255.0F;
/*     */     
/* 127 */     return Color.getHSBColor(hsv[0], hsv[1], v);
/*     */   }
/*     */   
/*     */   public static Color light(Color c, int factor) {
/* 131 */     if (factor <= 0)
/* 132 */       return c;
/* 133 */     if (factor < 100) {
/* 134 */       return dark(c, 10000 / factor);
/*     */     }
/*     */     
/* 137 */     float[] hsv = Color.RGBtoHSB(c.getRed(), c.getGreen(), c.getBlue(), null);
/*     */     
/* 139 */     float s = hsv[1];
/* 140 */     float v = hsv[2];
/*     */     
/* 142 */     System.out.print("LIGHT V : " + v);
/*     */     
/* 144 */     v = factor * v / 100.0F;
/*     */     
/* 146 */     if (v > 1.0F) {
/* 147 */       v = 1.0F;
/*     */     }
/*     */     
/* 150 */     if (v > 255.0F) {
/* 151 */       s -= v - 255.0F;
/*     */       
/* 153 */       if (s < 0.0F) {
/* 154 */         s = 0.0F;
/*     */       }
/*     */       
/* 157 */       v = 255.0F;
/*     */     }
/*     */     
/* 160 */     return Color.getHSBColor(hsv[0], hsv[1], v);
/*     */   }
/*     */   
/*     */   public static Color liquidAlpha(Color c, Color bg) {
/* 164 */     boolean blend = false;
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 172 */     int srcR = c.getRed();
/* 173 */     int srcG = c.getGreen();
/* 174 */     int srcB = c.getBlue();
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 184 */     srcR += 20;
/* 185 */     srcG += 20;
/* 186 */     srcB += 20;
/* 187 */     int alpha = bg.getAlpha();
/* 188 */     int delta = 255 - bg.getRed();
/* 189 */     int destR = srcR - delta;
/* 190 */     int destG = srcG - delta;
/* 191 */     int destB = srcB - delta;
/*     */     
/* 193 */     if (destR < 0) {
/* 194 */       destR = 0;
/*     */     }
/*     */     
/* 197 */     if (destG < 0) {
/* 198 */       destG = 0;
/*     */     }
/*     */     
/* 201 */     if (destB < 0) {
/* 202 */       destB = 0;
/*     */     }
/*     */     
/* 205 */     if (destR > 255) {
/* 206 */       destR = 255;
/*     */     }
/*     */     
/* 209 */     if (destG > 255) {
/* 210 */       destG = 255;
/*     */     }
/*     */     
/* 213 */     if (destB > 255) {
/* 214 */       destB = 255;
/*     */     }
/*     */     
/* 217 */     if ((blend) && (alpha != 255) && (alpha != 0)) {
/* 218 */       float srcPercent = alpha / 255.0F;
/* 219 */       float destPercent = 1.0F - srcPercent;
/* 220 */       destR = (int)(srcPercent * destR + destPercent * bg.getRed());
/* 221 */       destG = (int)(srcPercent * destG + destPercent * bg.getGreen());
/*     */       
/* 223 */       destB = (int)(srcPercent * destB + destPercent * bg.getBlue());
/* 224 */       alpha = 255;
/*     */     }
/*     */     
/* 227 */     return new Color(destR, destG, destB, alpha);
/*     */   }
/*     */ }


/* Location:              E:\java\java学习\hutubill\lib\all.jar!\com\birosoft\liquid\util\Colors.class
 * Java compiler version: 4 (48.0)
 * JD-Core Version:       0.7.1
 */
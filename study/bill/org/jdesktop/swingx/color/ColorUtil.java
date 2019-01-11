/*     */ package org.jdesktop.swingx.color;
/*     */ 
/*     */ import java.awt.Color;
/*     */ import java.awt.Graphics;
/*     */ import java.awt.Insets;
/*     */ import java.awt.Paint;
/*     */ import java.awt.Rectangle;
/*     */ import java.awt.TexturePaint;
/*     */ import java.awt.image.BufferedImage;
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
/*     */ public final class ColorUtil
/*     */ {
/*     */   public static Color removeAlpha(Color color)
/*     */   {
/*  59 */     return setAlpha(color, 255);
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
/*     */   public static Color setAlpha(Color color, int alpha)
/*     */   {
/*  78 */     if ((alpha < 0) || (alpha > 255)) {
/*  79 */       throw new IllegalArgumentException("invalid alpha value");
/*     */     }
/*     */     
/*  82 */     return new Color(color.getRed(), color.getGreen(), color.getBlue(), alpha);
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
/*     */   public static Color setSaturation(Color color, float saturation)
/*     */   {
/* 107 */     if ((saturation < 0.0F) || (saturation > 1.0F)) {
/* 108 */       throw new IllegalArgumentException("invalid saturation value");
/*     */     }
/*     */     
/* 111 */     int alpha = color.getAlpha();
/*     */     
/* 113 */     float[] hsb = Color.RGBtoHSB(color.getRed(), color.getGreen(), color.getBlue(), null);
/*     */     
/* 115 */     Color c = Color.getHSBColor(hsb[0], saturation, hsb[2]);
/*     */     
/* 117 */     return setAlpha(c, alpha);
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
/*     */   public static Color setBrightness(Color color, float brightness)
/*     */   {
/* 141 */     if ((brightness < 0.0F) || (brightness > 1.0F)) {
/* 142 */       throw new IllegalArgumentException("invalid brightness value");
/*     */     }
/*     */     
/* 145 */     int alpha = color.getAlpha();
/*     */     
/* 147 */     float[] hsb = Color.RGBtoHSB(color.getRed(), color.getGreen(), color.getBlue(), null);
/*     */     
/* 149 */     Color c = Color.getHSBColor(hsb[0], hsb[1], brightness);
/*     */     
/* 151 */     return setAlpha(c, alpha);
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
/*     */   public static String toHexString(Color color)
/*     */   {
/* 165 */     return "#" + Integer.toHexString(color.getRGB() | 0xFF000000).substring(2);
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
/*     */   public static Color computeForeground(Color bg)
/*     */   {
/* 179 */     float[] rgb = bg.getRGBColorComponents(null);
/* 180 */     float y = 0.3F * rgb[0] + 0.59F * rgb[1] + 0.11F * rgb[2];
/*     */     
/* 182 */     return y > 0.5F ? Color.BLACK : Color.WHITE;
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
/*     */   public static Color blend(Color origin, Color over)
/*     */   {
/* 198 */     if (over == null) {
/* 199 */       return origin;
/*     */     }
/*     */     
/* 202 */     if (origin == null) {
/* 203 */       return over;
/*     */     }
/*     */     
/* 206 */     int a = over.getAlpha();
/*     */     
/* 208 */     int rb = (over.getRGB() & 0xFF00FF) * (a + 1) + (origin.getRGB() & 0xFF00FF) * (255 - a) & 0xFF00FF00;
/*     */     
/* 210 */     int g = (over.getRGB() & 0xFF00) * (a + 1) + (origin.getRGB() & 0xFF00) * (255 - a) & 0xFF0000;
/*     */     
/*     */ 
/* 213 */     return new Color(over.getRGB() & 0xFF000000 | (rb | g) >> 8);
/*     */   }
/*     */   
/*     */   public static Color interpolate(Color b, Color a, float t) {
/* 217 */     float[] acomp = a.getRGBComponents(null);
/* 218 */     float[] bcomp = b.getRGBComponents(null);
/* 219 */     float[] ccomp = new float[4];
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 228 */     for (int i = 0; i < 4; i++) {
/* 229 */       acomp[i] += (bcomp[i] - acomp[i]) * t;
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/* 235 */     return new Color(ccomp[0], ccomp[1], ccomp[2], ccomp[3]);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static Paint getCheckerPaint()
/*     */   {
/* 245 */     return getCheckerPaint(Color.WHITE, Color.GRAY, 20);
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
/*     */   public static Paint getCheckerPaint(Color c1, Color c2, int size)
/*     */   {
/* 265 */     BufferedImage img = new BufferedImage(size, size, 2);
/* 266 */     Graphics g = img.getGraphics();
/*     */     try
/*     */     {
/* 269 */       g.setColor(c1);
/* 270 */       g.fillRect(0, 0, size, size);
/* 271 */       g.setColor(c2);
/* 272 */       g.fillRect(0, 0, size / 2, size / 2);
/* 273 */       g.fillRect(size / 2, size / 2, size / 2, size / 2);
/*     */     } finally {
/* 275 */       g.dispose();
/*     */     }
/*     */     
/* 278 */     return new TexturePaint(img, new Rectangle(0, 0, size, size));
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
/*     */   public static void tileStretchPaint(Graphics g, JComponent comp, BufferedImage img, Insets ins)
/*     */   {
/* 292 */     int left = ins.left;
/* 293 */     int right = ins.right;
/* 294 */     int top = ins.top;
/* 295 */     int bottom = ins.bottom;
/*     */     
/*     */ 
/* 298 */     g.drawImage(img, 0, 0, left, top, 0, 0, left, top, null);
/*     */     
/*     */ 
/*     */ 
/* 302 */     g.drawImage(img, left, 0, comp.getWidth() - right, top, left, 0, img.getWidth() - right, top, null);
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 308 */     g.drawImage(img, comp.getWidth() - right, 0, comp.getWidth(), top, img.getWidth() - right, 0, img.getWidth(), top, null);
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 316 */     g.drawImage(img, 0, top, left, comp.getHeight() - bottom, 0, top, left, img.getHeight() - bottom, null);
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 323 */     g.drawImage(img, left, top, comp.getWidth() - right, comp.getHeight() - bottom, left, top, img.getWidth() - right, img.getHeight() - bottom, null);
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 330 */     g.drawImage(img, comp.getWidth() - right, top, comp.getWidth(), comp.getHeight() - bottom, img.getWidth() - right, top, img.getWidth(), img.getHeight() - bottom, null);
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 338 */     g.drawImage(img, 0, comp.getHeight() - bottom, left, comp.getHeight(), 0, img.getHeight() - bottom, left, img.getHeight(), null);
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 344 */     g.drawImage(img, left, comp.getHeight() - bottom, comp.getWidth() - right, comp.getHeight(), left, img.getHeight() - bottom, img.getWidth() - right, img.getHeight(), null);
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 350 */     g.drawImage(img, comp.getWidth() - right, comp.getHeight() - bottom, comp.getWidth(), comp.getHeight(), img.getWidth() - right, img.getHeight() - bottom, img.getWidth(), img.getHeight(), null);
/*     */   }
/*     */ }


/* Location:              E:\java\java学习\hutubill\lib\all.jar!\org\jdesktop\swingx\color\ColorUtil.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */
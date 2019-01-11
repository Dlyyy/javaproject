/*     */ package com.birosoft.liquid.skin;
/*     */ 
/*     */ import com.birosoft.liquid.LiquidLookAndFeel;
/*     */ import java.awt.Color;
/*     */ import java.awt.Dimension;
/*     */ import java.awt.Graphics;
/*     */ import java.awt.Graphics2D;
/*     */ import java.awt.Image;
/*     */ import java.awt.image.BufferedImage;
/*     */ import java.awt.image.PixelGrabber;
/*     */ import java.io.PrintStream;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class Skin
/*     */   extends SkinElement
/*     */ {
/*     */   private int nrImages;
/*     */   private int hsize;
/*     */   private int vsize;
/*     */   private int ulX;
/*     */   private int ulY;
/*     */   private int lrX;
/*     */   private int lrY;
/*  75 */   private boolean noBorder = false;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public Skin(String fileName, int nrImages, int ulX, int ulY, int lrX, int lrY)
/*     */   {
/*  90 */     super(fileName, true);
/*  91 */     this.nrImages = nrImages;
/*  92 */     this.ulX = ulX;
/*  93 */     this.ulY = ulY;
/*  94 */     this.lrX = lrX;
/*  95 */     this.lrY = lrY;
/*  96 */     calculateSizes();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public Skin(String fileName, int nrImages, int roundedSize)
/*     */   {
/* 108 */     this(fileName, nrImages, roundedSize, roundedSize, roundedSize, roundedSize);
/*     */     
/*     */ 
/* 111 */     if (roundedSize == 0) {
/* 112 */       this.noBorder = true;
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void draw(Graphics g, int index, int sizeX, int sizeY)
/*     */   {
/* 125 */     Graphics2D g2 = (Graphics2D)g;
/* 126 */     int offset = index * getHsize();
/*     */     
/* 128 */     if (!this.noBorder)
/*     */     {
/* 130 */       g2.drawImage(getImage(), 0, 0, this.ulX, this.ulY, offset + 0, 0, offset + this.ulX, this.ulY, null);
/*     */       
/*     */ 
/*     */ 
/* 134 */       g2.drawImage(getImage(), this.ulX, 0, sizeX - this.lrX, this.ulY, offset + this.ulX, 0, offset + this.hsize - this.lrX, this.ulY, null);
/*     */       
/*     */ 
/*     */ 
/* 138 */       g2.drawImage(getImage(), sizeX - this.lrX, 0, sizeX, this.ulY, offset + this.hsize - this.lrX, 0, offset + this.hsize, this.ulY, null);
/*     */       
/*     */ 
/*     */ 
/* 142 */       g2.drawImage(getImage(), 0, this.ulY, this.ulX, sizeY - this.lrY, offset + 0, this.ulY, offset + this.ulX, this.vsize - this.lrY, null);
/*     */       
/*     */ 
/*     */ 
/* 146 */       g2.drawImage(getImage(), sizeX - this.lrX, this.ulY, sizeX, sizeY - this.lrY, offset + this.hsize - this.lrX, this.ulY, offset + this.hsize, this.vsize - this.lrY, null);
/*     */       
/*     */ 
/*     */ 
/* 150 */       g2.drawImage(getImage(), 0, sizeY - this.lrY, this.ulX, sizeY, offset + 0, this.vsize - this.lrY, offset + this.ulX, this.vsize, null);
/*     */       
/*     */ 
/*     */ 
/* 154 */       g2.drawImage(getImage(), this.ulX, sizeY - this.lrY, sizeX - this.lrX, sizeY, offset + this.ulX, this.vsize - this.lrY, offset + this.hsize - this.lrX, this.vsize, null);
/*     */       
/*     */ 
/*     */ 
/* 158 */       g2.drawImage(getImage(), sizeX - this.lrX, sizeY - this.lrY, sizeX, sizeY, offset + this.hsize - this.lrX, this.vsize - this.lrY, offset + this.hsize, this.vsize, null);
/*     */       
/*     */ 
/* 161 */       g2.drawImage(getImage(), this.ulX, this.ulY, sizeX - this.lrX, sizeY - this.lrY, offset + this.ulX, this.ulY, offset + this.hsize - this.lrX, this.vsize - this.lrY, null);
/*     */     }
/*     */     else {
/* 164 */       g.drawImage(getImage(), 0, 0, sizeX, sizeY, offset, 0, offset + this.hsize, this.vsize, null);
/*     */     }
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
/*     */   public void draw(Graphics g, int index, int x, int y, int sizeX, int sizeY)
/*     */   {
/* 182 */     int offset = index * getHsize();
/*     */     
/* 184 */     if (!this.noBorder)
/*     */     {
/* 186 */       g.drawImage(getImage(), x + 0, y + 0, x + this.ulX, y + this.ulY, offset + 0, 0, offset + this.ulX, this.ulY, null);
/*     */       
/*     */ 
/*     */ 
/* 190 */       g.drawImage(getImage(), x + this.ulX, y + 0, x + sizeX - this.lrX, y + this.ulY, offset + this.ulX, 0, offset + this.hsize - this.lrX, this.ulY, null);
/*     */       
/*     */ 
/*     */ 
/* 194 */       g.drawImage(getImage(), x + sizeX - this.lrX, y + 0, x + sizeX, y + this.ulY, offset + this.hsize - this.lrX, 0, offset + this.hsize, this.ulY, null);
/*     */       
/*     */ 
/*     */ 
/* 198 */       g.drawImage(getImage(), x + 0, y + this.ulY, x + this.ulX, y + sizeY - this.lrY, offset + 0, this.ulY, offset + this.ulX, this.vsize - this.lrY, null);
/*     */       
/*     */ 
/*     */ 
/* 202 */       g.drawImage(getImage(), x + sizeX - this.lrX, y + this.ulY, x + sizeX, y + sizeY - this.lrY, offset + this.hsize - this.lrX, this.ulY, offset + this.hsize, this.vsize - this.lrY, null);
/*     */       
/*     */ 
/*     */ 
/*     */ 
/* 207 */       g.drawImage(getImage(), x + 0, y + sizeY - this.lrY, x + this.ulX, y + sizeY, offset + 0, this.vsize - this.lrY, offset + this.ulX, this.vsize, null);
/*     */       
/*     */ 
/*     */ 
/* 211 */       g.drawImage(getImage(), x + this.ulX, y + sizeY - this.lrY, x + sizeX - this.lrX, y + sizeY, offset + this.ulX, this.vsize - this.lrY, offset + this.hsize - this.lrX, this.vsize, null);
/*     */       
/*     */ 
/*     */ 
/*     */ 
/* 216 */       g.drawImage(getImage(), x + sizeX - this.lrX, y + sizeY - this.lrY, x + sizeX, y + sizeY, offset + this.hsize - this.lrX, this.vsize - this.lrY, offset + this.hsize, this.vsize, null);
/*     */       
/*     */ 
/* 219 */       g.drawImage(getImage(), x + this.ulX, y + this.ulY, x + sizeX - this.lrX, y + sizeY - this.lrY, offset + this.ulX, this.ulY, offset + this.hsize - this.lrX, this.vsize - this.lrY, null);
/*     */     }
/*     */     else
/*     */     {
/* 223 */       g.drawImage(getImage(), x, y, x + sizeX, y + sizeY, offset, 0, offset + this.hsize, this.vsize, null);
/*     */     }
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
/*     */   public void drawCentered(Graphics g, int index, int sizeX, int sizeY)
/*     */   {
/* 237 */     int offset = index * getHsize();
/*     */     
/* 239 */     int w = getHsize();
/* 240 */     int h = getVsize();
/*     */     
/* 242 */     int sx = (sizeX - w) / 2;
/* 243 */     int sy = (sizeY - h) / 2;
/*     */     
/* 245 */     g.drawImage(getImage(), sx, sy, sx + w, sy + h, offset, 0, offset + w, h, null);
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
/*     */   public void drawCentered(Graphics g, int index, int x, int y, int sizeX, int sizeY)
/*     */   {
/* 261 */     int offset = index * getHsize();
/*     */     
/* 263 */     int w = getHsize();
/* 264 */     int h = getVsize();
/*     */     
/* 266 */     int sx = (sizeX - w) / 2;
/* 267 */     int sy = (sizeY - h) / 2;
/*     */     
/* 269 */     g.drawImage(getImage(), x + sx, y + sy, x + sx + w, y + sy + h, offset, 0, offset + w, h, null);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public int getHsize()
/*     */   {
/* 278 */     return this.hsize;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public int getVsize()
/*     */   {
/* 286 */     return this.vsize;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public Dimension getSize()
/*     */   {
/* 294 */     return new Dimension(this.hsize, this.vsize);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   protected void calculateSizes()
/*     */   {
/* 301 */     this.hsize = (getImage().getWidth(null) / this.nrImages);
/* 302 */     this.vsize = getImage().getHeight(null);
/*     */   }
/*     */   
/*     */   Color dark(Color c, int factor) {
/* 306 */     float[] hsv = Color.RGBtoHSB(c.getRed(), c.getGreen(), c.getBlue(), null);
/*     */     
/* 308 */     if ((factor <= 0) || (c.getAlpha() < 255))
/* 309 */       return c;
/* 310 */     if (factor < 100) {
/* 311 */       return light(c, 10000 / factor);
/*     */     }
/*     */     
/* 314 */     int vi = (int)(hsv[2] * 255.0F);
/*     */     
/* 316 */     vi = 100 * vi / factor;
/*     */     
/* 318 */     float v = vi / 255.0F;
/*     */     
/* 320 */     return Color.getHSBColor(hsv[0], hsv[1], v);
/*     */   }
/*     */   
/*     */   Color light(Color c, int factor) {
/* 324 */     if (factor <= 0)
/* 325 */       return c;
/* 326 */     if (factor < 100) {
/* 327 */       return dark(c, 10000 / factor);
/*     */     }
/*     */     
/* 330 */     float[] hsv = Color.RGBtoHSB(c.getRed(), c.getGreen(), c.getBlue(), null);
/*     */     
/* 332 */     float s = hsv[1];
/* 333 */     float v = hsv[2];
/*     */     
/* 335 */     System.out.print("LIGHT V : " + v);
/*     */     
/* 337 */     v = factor * v / 100.0F;
/*     */     
/* 339 */     if (v > 1.0F) {
/* 340 */       v = 1.0F;
/*     */     }
/*     */     
/* 343 */     if (v > 255.0F) {
/* 344 */       s -= v - 255.0F;
/*     */       
/* 346 */       if (s < 0.0F) {
/* 347 */         s = 0.0F;
/*     */       }
/*     */       
/* 350 */       v = 255.0F;
/*     */     }
/*     */     
/* 353 */     return Color.getHSBColor(hsv[0], hsv[1], v);
/*     */   }
/*     */   
/*     */   Color colour(Color src, Color bg) {
/* 357 */     boolean blend = false;
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 365 */     int srcR = src.getRed();
/* 366 */     int srcG = src.getGreen();
/* 367 */     int srcB = src.getBlue();
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 377 */     srcR += 20;
/* 378 */     srcG += 20;
/* 379 */     srcB += 20;
/* 380 */     int alpha = bg.getAlpha();
/* 381 */     int delta = 255 - bg.getRed();
/* 382 */     int destR = srcR - delta;
/* 383 */     int destG = srcG - delta;
/* 384 */     int destB = srcB - delta;
/*     */     
/* 386 */     if (destR < 0) {
/* 387 */       destR = 0;
/*     */     }
/*     */     
/* 390 */     if (destG < 0) {
/* 391 */       destG = 0;
/*     */     }
/*     */     
/* 394 */     if (destB < 0) {
/* 395 */       destB = 0;
/*     */     }
/*     */     
/* 398 */     if (destR > 255) {
/* 399 */       destR = 255;
/*     */     }
/*     */     
/* 402 */     if (destG > 255) {
/* 403 */       destG = 255;
/*     */     }
/*     */     
/* 406 */     if (destB > 255) {
/* 407 */       destB = 255;
/*     */     }
/*     */     
/* 410 */     if ((blend) && (alpha != 255) && (alpha != 0)) {
/* 411 */       float srcPercent = alpha / 255.0F;
/* 412 */       float destPercent = 1.0F - srcPercent;
/* 413 */       destR = (int)(srcPercent * destR + destPercent * bg.getRed());
/* 414 */       destG = (int)(srcPercent * destG + destPercent * bg.getGreen());
/*     */       
/* 416 */       destB = (int)(srcPercent * destB + destPercent * bg.getBlue());
/* 417 */       alpha = 255;
/*     */     }
/*     */     
/* 420 */     return new Color(destR, destG, destB, alpha);
/*     */   }
/*     */   
/*     */   private Color colourSinglePixel(int x, int y, int pixel, Color c) {
/* 424 */     int alpha = pixel >> 24 & 0xFF;
/* 425 */     int red = pixel >> 16 & 0xFF;
/* 426 */     int green = pixel >> 8 & 0xFF;
/* 427 */     int blue = pixel & 0xFF;
/*     */     
/* 429 */     return colour(c, new Color(red, green, blue, alpha));
/*     */   }
/*     */   
/*     */   public void colourImage() {
/* 433 */     int x = 0;
/* 434 */     int y = 0;
/* 435 */     int w = this.hsize * this.nrImages;
/* 436 */     int h = this.vsize;
/* 437 */     BufferedImage newImage = new BufferedImage(w, h, 6);
/*     */     
/*     */ 
/* 440 */     int[] pixels = new int[w * h];
/* 441 */     PixelGrabber pg = new PixelGrabber(getImage(), x, y, w, h, pixels, 0, w);
/*     */     try
/*     */     {
/* 444 */       pg.grabPixels();
/*     */     } catch (InterruptedException e) {
/* 446 */       System.err.println("interrupted waiting for pixels!");
/*     */       
/* 448 */       return;
/*     */     }
/*     */     
/* 451 */     if ((pg.getStatus() & 0x80) != 0) {
/* 452 */       System.err.println("image fetch aborted or errored");
/*     */       
/* 454 */       return;
/*     */     }
/*     */     
/* 457 */     Graphics g = newImage.getGraphics();
/* 458 */     Color colourWith = null;
/*     */     
/* 460 */     for (int j = 0; j < h; j++) {
/* 461 */       for (int i = 0; i < w; i++) {
/* 462 */         if ((i == 0) || (i == 117)) {
/* 463 */           colourWith = LiquidLookAndFeel.getBackgroundColor();
/* 464 */         } else if ((i == 39) || (i == 156)) {
/* 465 */           colourWith = LiquidLookAndFeel.getButtonBackground();
/* 466 */         } else if (i == 78) {
/* 467 */           colourWith = dark(LiquidLookAndFeel.getButtonBackground(), 115);
/*     */         }
/*     */         
/*     */ 
/* 471 */         g.setColor(colourSinglePixel(x + i, y + j, pixels[(j * w + i)], colourWith));
/*     */         
/* 473 */         g.drawLine(x + i, y + j, x + i, y + j);
/*     */       }
/*     */     }
/*     */     
/*     */ 
/* 478 */     setImage(newImage);
/*     */   }
/*     */ }


/* Location:              E:\java\java学习\hutubill\lib\all.jar!\com\birosoft\liquid\skin\Skin.class
 * Java compiler version: 4 (48.0)
 * JD-Core Version:       0.7.1
 */
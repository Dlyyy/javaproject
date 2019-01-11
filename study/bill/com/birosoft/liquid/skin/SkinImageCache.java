/*     */ package com.birosoft.liquid.skin;
/*     */ 
/*     */ import java.awt.Graphics;
/*     */ import java.awt.GraphicsConfiguration;
/*     */ import java.awt.GraphicsDevice;
/*     */ import java.awt.GraphicsEnvironment;
/*     */ import java.awt.Image;
/*     */ import java.awt.image.BufferedImage;
/*     */ import java.util.HashMap;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class SkinImageCache
/*     */ {
/*  36 */   private static SkinImageCache instance = new SkinImageCache();
/*     */   static GraphicsConfiguration conf;
/*     */   private HashMap map;
/*     */   
/*  40 */   static { GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
/*  41 */     conf = ge.getDefaultScreenDevice().getDefaultConfiguration();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   protected SkinImageCache()
/*     */   {
/*  49 */     this.map = new HashMap();
/*  50 */     this.iconMap = new HashMap();
/*  51 */     this.bufferedMap = new HashMap();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   private HashMap iconMap;
/*     */   
/*     */   private HashMap bufferedMap;
/*     */   
/*     */   public Image getAutomaticImage(String fileName)
/*     */   {
/*  62 */     Image ret = (Image)this.map.get(fileName);
/*     */     
/*  64 */     if (ret == null) {
/*  65 */       Image img = SecretLoader.loadImage(fileName);
/*  66 */       this.map.put(fileName, img);
/*     */       
/*  68 */       return img;
/*     */     }
/*     */     
/*  71 */     return ret;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public Image getImage(String fileName)
/*     */   {
/*  79 */     return getAutomaticImage(fileName);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public BufferedImage getBufferedImage(String fileName)
/*     */   {
/*  89 */     BufferedImage b = (BufferedImage)this.bufferedMap.get(fileName);
/*     */     
/*  91 */     if (b != null) {
/*  92 */       return b;
/*     */     }
/*     */     
/*  95 */     Image img = getImage(fileName);
/*     */     
/*  97 */     if ((img instanceof BufferedImage)) {
/*  98 */       return (BufferedImage)img;
/*     */     }
/*     */     
/* 101 */     int w = img.getWidth(null);
/* 102 */     int h = img.getHeight(null);
/*     */     
/* 104 */     BufferedImage img2 = conf.createCompatibleImage(w, h);
/* 105 */     Graphics g = img2.getGraphics();
/* 106 */     g.drawImage(img, 0, 0, w, h, 0, 0, w, h, null);
/* 107 */     this.bufferedMap.put(fileName, img2);
/*     */     
/* 109 */     return img2;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public static SkinImageCache getInstance()
/*     */   {
/* 117 */     return instance;
/*     */   }
/*     */ }


/* Location:              E:\java\java学习\hutubill\lib\all.jar!\com\birosoft\liquid\skin\SkinImageCache.class
 * Java compiler version: 4 (48.0)
 * JD-Core Version:       0.7.1
 */
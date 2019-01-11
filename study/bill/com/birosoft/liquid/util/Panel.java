/*     */ package com.birosoft.liquid.util;
/*     */ 
/*     */ import java.awt.Color;
/*     */ import java.awt.Graphics;
/*     */ import java.awt.Graphics2D;
/*     */ import java.awt.Image;
/*     */ import java.awt.Paint;
/*     */ import java.awt.TexturePaint;
/*     */ import java.awt.geom.Rectangle2D.Float;
/*     */ import java.awt.image.BufferedImage;
/*     */ import javax.swing.JPanel;
/*     */ 
/*     */ 
/*     */ 
/*     */ public class Panel
/*     */   extends JPanel
/*     */ {
/*     */   Image image;
/*  19 */   static Color buttonBg = new Color(215, 231, 249);
/*  20 */   static Color bg = new Color(246, 245, 244);
/*     */   
/*     */ 
/*     */   public Panel(Image i)
/*     */   {
/*  25 */     this.image = i;
/*     */   }
/*     */   
/*     */   public void paint(Graphics g) {
/*  29 */     super.paint(g);
/*  30 */     g.drawImage(this.image, 10, 100, null);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   private void drawIt(Graphics g, int x, int y, int w, int h, Color c, Color bg)
/*     */   {
/*  37 */     int x2 = x + w - 1;
/*  38 */     int y2 = y + h - 1;
/*     */     
/*  40 */     g.setColor(Colors.dark(c, 115));
/*  41 */     g.drawLine(x + 2, y, x2 - 2, y);
/*  42 */     g.drawLine(x, y + 2, x, y2 - 2);
/*  43 */     g.drawLine(x + 1, y + 1, x + 1, y + 1);
/*  44 */     g.setColor(Colors.dark(c, 150));
/*  45 */     g.drawLine(x + 2, y2, x2 - 2, y2);
/*  46 */     g.drawLine(x2, y + 2, x2, y2 - 2);
/*  47 */     g.drawLine(x2 - 1, y2 - 1, x2 - 1, y2 - 1);
/*  48 */     g.setColor(Colors.dark(c, 132));
/*  49 */     g.drawLine(x2 - 1, y + 1, x2 - 1, y + 1);
/*  50 */     g.drawLine(x + 1, y2 - 1, x + 1, y2 - 1);
/*     */     
/*     */ 
/*  53 */     g.setColor(Colors.light(c, 105));
/*  54 */     g.drawLine(x + 2, y + 1, x2 - 2, y + 1);
/*  55 */     g.drawLine(x + 1, y + 2, x2 - 1, y + 2);
/*  56 */     g.drawLine(x + 1, y + 3, x + 2, y + 3);
/*  57 */     g.drawLine(x2 - 2, y + 3, x2 - 1, y + 3);
/*  58 */     g.drawLine(x + 1, y + 4, x + 1, y + 4);
/*  59 */     g.drawLine(x2 - 1, y + 4, x2 - 1, y + 4);
/*     */     
/*     */ 
/*  62 */     g.setColor(Colors.light(c, 110));
/*  63 */     g.drawLine(x + 2, y2 - 1, x2 - 2, y2 - 1);
/*  64 */     g.drawLine(x + 1, y2 - 2, x2 - 1, y2 - 2);
/*  65 */     g.drawLine(x + 1, y2 - 3, x + 2, y2 - 3);
/*  66 */     g.drawLine(x2 - 2, y2 - 3, x2 - 1, y2 - 3);
/*  67 */     g.drawLine(x + 1, y2 - 4, x + 1, y2 - 4);
/*  68 */     g.drawLine(x2 - 1, y2 - 4, x2 - 1, y2 - 4);
/*     */     
/*     */ 
/*  71 */     g.setColor(c);
/*  72 */     g.drawLine(x + 1, y + 5, x + 1, y2 - 5);
/*  73 */     g.drawLine(x + 2, y + 4, x + 2, y2 - 4);
/*     */     
/*     */ 
/*  76 */     g.drawLine(x2 - 1, y + 5, x2 - 1, y2 - 5);
/*  77 */     g.drawLine(x2 - 2, y + 4, x2 - 2, y2 - 4);
/*     */     
/*  79 */     Graphics2D g2 = (Graphics2D)g;
/*  80 */     BufferedImage img = Colors.getClearFill();
/*  81 */     TexturePaint tp = new TexturePaint(img, new Rectangle2D.Float(0.0F, 0.0F, img.getWidth(), img.getHeight()));
/*  82 */     Paint p = g2.getPaint();
/*  83 */     g2.setPaint(tp);
/*  84 */     g2.fillRect(x + 2, y + 2, w - 4, h - 4);
/*  85 */     g2.setPaint(p);
/*     */     
/*  87 */     Color btnColor = Colors.dark(c, 130);
/*  88 */     int red = (btnColor.getRed() >> 1) + (bg.getRed() >> 1);
/*  89 */     int green = (btnColor.getGreen() >> 1) + (bg.getGreen() >> 1);
/*  90 */     int blue = (btnColor.getBlue() >> 1) + (bg.getBlue() >> 1);
/*  91 */     btnColor = new Color(red, green, blue);
/*     */     
/*  93 */     g.setColor(btnColor);
/*  94 */     g.drawLine(x + 1, y, x + 1, y);
/*  95 */     g.drawLine(x, y + 1, x, y + 1);
/*  96 */     g.drawLine(x + 1, y2, x + 1, y2);
/*  97 */     g.drawLine(x, y2 - 1, x, y2 - 1);
/*     */     
/*  99 */     g.drawLine(x2 - 1, y, x2 - 1, y);
/* 100 */     g.drawLine(x2, y + 1, x2, y + 1);
/* 101 */     g.drawLine(x2 - 1, y2, x2 - 1, y2);
/* 102 */     g.drawLine(x2, y2 - 1, x2, y2 - 1);
/*     */   }
/*     */ }


/* Location:              E:\java\java学习\hutubill\lib\all.jar!\com\birosoft\liquid\util\Panel.class
 * Java compiler version: 4 (48.0)
 * JD-Core Version:       0.7.1
 */
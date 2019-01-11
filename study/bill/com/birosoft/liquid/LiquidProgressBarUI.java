/*     */ package com.birosoft.liquid;
/*     */ 
/*     */ import com.birosoft.liquid.skin.Skin;
/*     */ import java.awt.Color;
/*     */ import java.awt.Dimension;
/*     */ import java.awt.Graphics;
/*     */ import java.awt.Graphics2D;
/*     */ import java.awt.Insets;
/*     */ import java.awt.Paint;
/*     */ import java.awt.Rectangle;
/*     */ import javax.swing.JComponent;
/*     */ import javax.swing.JProgressBar;
/*     */ import javax.swing.UIManager;
/*     */ import javax.swing.plaf.ComponentUI;
/*     */ import javax.swing.plaf.basic.BasicProgressBarUI;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class LiquidProgressBarUI
/*     */   extends BasicProgressBarUI
/*     */ {
/*     */   static Skin skinHorizontal;
/*     */   static Skin skinVertical;
/*     */   private static Skin skinTrackVert;
/*     */   private static Skin skinTrackHoriz;
/*     */   private Skin skinTrack;
/*     */   private static Skin skinThumbVert;
/*     */   private static Skin skinThumbHoriz;
/*     */   private static Skin disabledSkinThumbVert;
/*     */   private static Skin disabledSkinThumbHoriz;
/*     */   private Skin skinThumb;
/*  78 */   int offset = 3;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static ComponentUI createUI(JComponent c)
/*     */   {
/*  87 */     return new LiquidProgressBarUI();
/*     */   }
/*     */   
/*     */   protected void paintDeterminate(Graphics g, JComponent c) {
/*  91 */     if (!(g instanceof Graphics2D)) {
/*  92 */       return;
/*     */     }
/*  94 */     int index = 0;
/*     */     
/*  96 */     Insets b = this.progressBar.getInsets();
/*  97 */     int barRectWidth = this.progressBar.getWidth() - (b.right + b.left);
/*  98 */     int barRectHeight = this.progressBar.getHeight() - (b.top + b.bottom);
/*     */     
/* 100 */     Graphics2D g2 = (Graphics2D)g;
/*     */     
/* 102 */     if (this.progressBar.getOrientation() == 0) {
/* 103 */       int amountFull = getAmountFull(b, barRectWidth, barRectHeight);
/*     */       
/* 105 */       getSkinTrack().draw(g, 0, 0, 0, barRectWidth, barRectHeight);
/*     */       
/* 107 */       Paint p = g2.getPaint();
/*     */       
/* 109 */       if (amountFull > 10) {
/* 110 */         getSkinThumb().draw(g, index, 0, 0, amountFull, barRectHeight);
/*     */       }
/*     */       
/*     */ 
/* 114 */       if (this.progressBar.isStringPainted()) {
/* 115 */         g.setColor(Color.black);
/* 116 */         paintString(g, b.left, b.top, barRectWidth, barRectHeight, amountFull, b);
/*     */       }
/*     */       
/* 119 */       g2.setPaint(p);
/*     */     }
/*     */     else {
/* 122 */       int amountFull = getAmountFull(b, barRectWidth, barRectHeight);
/*     */       
/* 124 */       getSkinTrack().draw(g, 0, 0, 0, barRectWidth, barRectHeight);
/*     */       
/* 126 */       Paint p = g2.getPaint();
/*     */       
/* 128 */       if (amountFull > 10) {
/* 129 */         getSkinThumb().draw(g, index, 0, barRectHeight - amountFull, barRectWidth, amountFull);
/*     */       }
/*     */       
/*     */ 
/* 133 */       if (this.progressBar.isStringPainted()) {
/* 134 */         g.setColor(Color.black);
/* 135 */         paintString(g, b.left, b.top, barRectWidth, barRectHeight, amountFull, b);
/*     */       }
/*     */       
/* 138 */       g2.setPaint(p);
/*     */     }
/*     */   }
/*     */   
/*     */   protected void paintIndeterminate(Graphics g, JComponent c) {
/* 143 */     if (!(g instanceof Graphics2D)) {
/* 144 */       return;
/*     */     }
/* 146 */     Graphics2D g2 = (Graphics2D)g;
/*     */     
/* 148 */     int index = 0;
/*     */     
/* 150 */     Insets b = this.progressBar.getInsets();
/* 151 */     int barRectWidth = this.progressBar.getWidth() - (b.right + b.left);
/* 152 */     int barRectHeight = this.progressBar.getHeight() - (b.top + b.bottom);
/*     */     
/* 154 */     Rectangle boxRect = getBox(null);
/* 155 */     if (this.progressBar.getOrientation() == 0) {
/* 156 */       getSkinTrack().draw(g, 0, 0, 0, barRectWidth, barRectHeight);
/*     */       
/* 158 */       g.translate(boxRect.x, boxRect.y);
/*     */       
/* 160 */       getSkinThumb().draw(g, index, 0, 0, boxRect.width, barRectHeight);
/*     */       
/* 162 */       g.translate(-boxRect.x, -boxRect.y);
/*     */     }
/*     */     else {
/* 165 */       getSkinTrack().draw(g, 0, 0, 0, barRectWidth, barRectHeight);
/*     */       
/* 167 */       g.translate(boxRect.x, boxRect.y);
/*     */       
/* 169 */       getSkinThumb().draw(g, index, barRectWidth, boxRect.height);
/*     */       
/* 171 */       g.translate(-boxRect.x, -boxRect.y);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void update(Graphics g, JComponent c)
/*     */   {
/* 180 */     paint(g, c);
/*     */   }
/*     */   
/*     */   protected void installDefaults() {
/* 184 */     LiquidLookAndFeel.installColorsAndFont(this.progressBar, "ProgressBar.background", "ProgressBar.foreground", "ProgressBar.font");
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   protected Dimension getPreferredInnerHorizontal()
/*     */   {
/* 192 */     Dimension horizDim = (Dimension)UIManager.get("ProgressBar.horizontalSize");
/* 193 */     if (horizDim == null) {
/* 194 */       horizDim = new Dimension(146, 14);
/*     */     }
/* 196 */     return horizDim;
/*     */   }
/*     */   
/*     */   protected Dimension getPreferredInnerVertical() {
/* 200 */     Dimension vertDim = (Dimension)UIManager.get("ProgressBar.vertictalSize");
/* 201 */     if (vertDim == null) {
/* 202 */       vertDim = new Dimension(14, 146);
/*     */     }
/* 204 */     return vertDim;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static Skin getSkinThumbHoriz()
/*     */   {
/* 214 */     if (skinThumbHoriz == null)
/*     */     {
/* 216 */       skinThumbHoriz = new Skin("scrollbarthumbhoriz.png", 4, 8, 6, 8, 8);
/*     */     }
/* 218 */     return skinThumbHoriz;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static Skin getSkinThumbVert()
/*     */   {
/* 227 */     if (skinThumbVert == null)
/*     */     {
/* 229 */       skinThumbVert = new Skin("scrollbarthumbvert.png", 4, 6, 8, 8, 7);
/*     */     }
/* 231 */     return skinThumbVert;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static Skin getDisabledSkinThumbHoriz()
/*     */   {
/* 240 */     if (disabledSkinThumbHoriz == null)
/*     */     {
/* 242 */       disabledSkinThumbHoriz = new Skin("scrollbarthumbhoriz.png", 4, 8, 6, 8, 8);
/* 243 */       disabledSkinThumbHoriz.colourImage();
/*     */     }
/* 245 */     return disabledSkinThumbHoriz;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static Skin getDisabledSkinThumbVert()
/*     */   {
/* 254 */     if (disabledSkinThumbVert == null)
/*     */     {
/* 256 */       disabledSkinThumbVert = new Skin("scrollbarthumbvert.png", 4, 6, 8, 8, 7);
/* 257 */       disabledSkinThumbVert.colourImage();
/*     */     }
/* 259 */     return disabledSkinThumbVert;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static Skin getSkinTrackHoriz()
/*     */   {
/* 268 */     if (skinTrackHoriz == null)
/*     */     {
/* 270 */       skinTrackHoriz = new Skin("progressbartrackhoriz.png", 1, 7);
/*     */     }
/* 272 */     return skinTrackHoriz;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static Skin getSkinTrackVert()
/*     */   {
/* 281 */     if (skinTrackVert == null)
/*     */     {
/* 283 */       skinTrackVert = new Skin("progressbartrackvert.png", 1, 7);
/*     */     }
/* 285 */     return skinTrackVert;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public Skin getSkinTrack()
/*     */   {
/* 294 */     if (this.skinTrack == null)
/*     */     {
/* 296 */       this.skinTrack = (this.progressBar.getOrientation() == 1 ? getSkinTrackVert() : getSkinTrackHoriz());
/*     */     }
/* 298 */     return this.skinTrack;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public Skin getSkinThumb()
/*     */   {
/* 307 */     if (this.skinThumb == null)
/*     */     {
/* 309 */       if (this.progressBar.isEnabled()) {
/* 310 */         this.skinThumb = (this.progressBar.getOrientation() == 1 ? getSkinThumbVert() : getSkinThumbHoriz());
/*     */       } else {
/* 312 */         this.skinThumb = (this.progressBar.getOrientation() == 1 ? getDisabledSkinThumbVert() : getDisabledSkinThumbHoriz());
/*     */       }
/*     */     }
/* 315 */     return this.skinThumb;
/*     */   }
/*     */ }


/* Location:              E:\java\java学习\hutubill\lib\all.jar!\com\birosoft\liquid\LiquidProgressBarUI.class
 * Java compiler version: 4 (48.0)
 * JD-Core Version:       0.7.1
 */
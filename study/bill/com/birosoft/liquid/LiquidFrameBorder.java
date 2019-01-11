/*     */ package com.birosoft.liquid;
/*     */ 
/*     */ import java.awt.Color;
/*     */ import java.awt.Component;
/*     */ import java.awt.Graphics;
/*     */ import java.awt.Insets;
/*     */ import java.awt.Window;
/*     */ import javax.swing.JDialog;
/*     */ import javax.swing.SwingUtilities;
/*     */ import javax.swing.UIManager;
/*     */ import javax.swing.border.AbstractBorder;
/*     */ import javax.swing.plaf.UIResource;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class LiquidFrameBorder
/*     */   extends AbstractBorder
/*     */   implements UIResource
/*     */ {
/*     */   private static LiquidFrameBorder onlyInstance;
/*  29 */   private static final Insets insets = new Insets(0, 4, 4, 4);
/*  30 */   private boolean prevState = false;
/*     */   
/*     */   private Window window;
/*     */   
/*     */   private int titleHeight;
/*  35 */   private boolean isActive = true;
/*     */   
/*     */   public static LiquidFrameBorder getInstance() {
/*  38 */     if (onlyInstance == null) {
/*  39 */       onlyInstance = new LiquidFrameBorder();
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*  54 */     return onlyInstance;
/*     */   }
/*     */   
/*     */   private void isWindowRealActive(Window window) {
/*  58 */     this.isActive = window.isActive();
/*     */     
/*  60 */     if (this.isActive) {
/*  61 */       this.prevState = true;
/*     */     }
/*     */     
/*  64 */     if ((!this.prevState) && (!this.isActive)) {
/*  65 */       this.isActive = true;
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void paintBorder(Component c, Graphics g, int x, int y, int w, int h)
/*     */   {
/*  74 */     this.window = SwingUtilities.getWindowAncestor(c);
/*     */     
/*  76 */     if ((this.window instanceof JDialog)) {
/*  77 */       JDialog d = (JDialog)this.window;
/*     */       
/*  79 */       if (d.isModal()) {
/*  80 */         this.isActive = true;
/*     */       } else {
/*  82 */         isWindowRealActive(this.window);
/*     */       }
/*     */     } else {
/*  85 */       isWindowRealActive(this.window);
/*     */     }
/*     */     
/*  88 */     int frameTitleHeight = UIManager.getInt("InternalFrame.frameTitleHeight");
/*     */     
/*     */ 
/*  91 */     int index = this.isActive ? 0 : 1;
/*     */     
/*  93 */     drawLeftTop(g, this.isActive, 4, frameTitleHeight);
/*     */     
/*  95 */     g.translate(0, frameTitleHeight);
/*  96 */     drawLeft(g, this.isActive, 4, h - frameTitleHeight - 4);
/*  97 */     g.translate(0, -frameTitleHeight);
/*     */     
/*  99 */     g.translate(0, h - 4);
/*     */     
/*     */ 
/* 102 */     drawBottom(g, this.isActive, w, 4);
/* 103 */     g.translate(0, -(h - 4));
/*     */     
/* 105 */     g.translate(w - 4, 0);
/*     */     
/* 107 */     drawRightTop(g, this.isActive, 4, frameTitleHeight);
/*     */     
/* 109 */     g.translate(0, frameTitleHeight);
/* 110 */     drawRight(g, this.isActive, 4, h - frameTitleHeight - 4);
/* 111 */     g.translate(0, -frameTitleHeight);
/*     */     
/* 113 */     g.translate(-(w - 4), 0);
/*     */   }
/*     */   
/*     */   private void drawLeftTop(Graphics g, boolean isSelected, int w, int h) {
/* 117 */     if (LiquidLookAndFeel.winDecoPanther) {
/* 118 */       Color c = new Color(198, 198, 198);
/* 119 */       g.setColor(c);
/* 120 */       g.fillRect(0, 0, w, h);
/* 121 */       return;
/*     */     }
/*     */     
/* 124 */     Color c = isSelected ? new Color(62, 145, 235) : new Color(175, 214, 255);
/* 125 */     g.setColor(c);
/* 126 */     g.fillRect(0, 0, w, h);
/* 127 */     c = isSelected ? new Color(94, 172, 255) : new Color(226, 240, 255);
/* 128 */     g.setColor(c);
/* 129 */     g.drawLine(0, 0, w, 0);
/* 130 */     g.drawLine(0, 0, 0, h);
/* 131 */     c = isSelected ? new Color(60, 141, 228) : new Color(170, 207, 247);
/* 132 */     g.setColor(c);
/* 133 */     g.drawLine(1, 1, w, 1);
/*     */     
/* 135 */     for (int i = 4; i < h; i += 4) {
/* 136 */       c = isSelected ? new Color(59, 138, 223) : new Color(166, 203, 242);
/* 137 */       g.setColor(c);
/* 138 */       g.drawLine(1, i, w, i);
/* 139 */       c = isSelected ? new Color(60, 141, 228) : new Color(170, 207, 247);
/* 140 */       g.setColor(c);
/* 141 */       g.drawLine(1, i + 1, w, i + 1);
/*     */     }
/*     */     
/* 144 */     c = isSelected ? new Color(47, 111, 180) : new Color(135, 164, 196);
/* 145 */     g.setColor(c);
/* 146 */     g.drawLine(w - 1, h - 1, w - 1, h - 1);
/*     */   }
/*     */   
/*     */   private void drawLeft(Graphics g, boolean isSelected, int w, int h) {
/* 150 */     if (LiquidLookAndFeel.winDecoPanther) {
/* 151 */       Color c = new Color(198, 198, 198);
/* 152 */       g.setColor(c);
/* 153 */       g.fillRect(0, 0, w, h);
/* 154 */       return;
/*     */     }
/*     */     
/* 157 */     Color c = isSelected ? new Color(62, 145, 235) : new Color(175, 214, 255);
/* 158 */     g.setColor(c);
/* 159 */     g.fillRect(0, 0, w, h);
/* 160 */     c = isSelected ? new Color(94, 172, 255) : new Color(226, 240, 255);
/* 161 */     g.setColor(c);
/* 162 */     g.drawLine(0, 0, 0, h);
/* 163 */     c = isSelected ? new Color(59, 138, 223) : new Color(166, 203, 242);
/* 164 */     g.setColor(c);
/* 165 */     g.drawLine(1, 0, w, 0);
/*     */     
/* 167 */     for (int i = 3; i < h; i += 4) {
/* 168 */       c = isSelected ? new Color(59, 138, 223) : new Color(166, 203, 242);
/* 169 */       g.setColor(c);
/* 170 */       g.drawLine(1, i, w, i);
/* 171 */       c = isSelected ? new Color(60, 141, 228) : new Color(170, 207, 247);
/* 172 */       g.setColor(c);
/* 173 */       g.drawLine(1, i + 1, w, i + 1);
/*     */     }
/*     */     
/* 176 */     c = isSelected ? new Color(47, 111, 180) : new Color(135, 164, 196);
/* 177 */     g.setColor(c);
/* 178 */     g.drawLine(w - 1, 0, w - 1, h);
/*     */   }
/*     */   
/*     */   private void drawRightTop(Graphics g, boolean isSelected, int w, int h) {
/* 182 */     if (LiquidLookAndFeel.winDecoPanther) {
/* 183 */       Color c = new Color(198, 198, 198);
/* 184 */       g.setColor(c);
/* 185 */       g.fillRect(0, 0, w, h);
/* 186 */       return;
/*     */     }
/*     */     
/* 189 */     Color c = isSelected ? new Color(62, 145, 235) : new Color(175, 214, 255);
/* 190 */     g.setColor(c);
/* 191 */     g.fillRect(0, 0, w, h);
/* 192 */     c = isSelected ? new Color(94, 172, 255) : new Color(226, 240, 255);
/* 193 */     g.setColor(c);
/* 194 */     g.drawLine(0, 0, w - 2, 0);
/* 195 */     c = isSelected ? new Color(60, 141, 228) : new Color(170, 207, 247);
/* 196 */     g.setColor(c);
/* 197 */     g.drawLine(0, 1, w - 2, 1);
/*     */     
/* 199 */     for (int i = 4; i < h; i += 4) {
/* 200 */       c = isSelected ? new Color(59, 138, 223) : new Color(166, 203, 242);
/* 201 */       g.setColor(c);
/* 202 */       g.drawLine(0, i, w - 2, i);
/* 203 */       c = isSelected ? new Color(60, 141, 228) : new Color(170, 207, 247);
/* 204 */       g.setColor(c);
/* 205 */       g.drawLine(0, i + 1, w - 2, i + 1);
/*     */     }
/*     */     
/* 208 */     c = isSelected ? new Color(94, 172, 255) : new Color(226, 240, 255);
/* 209 */     g.setColor(c);
/* 210 */     g.drawLine(0, h - 1, 0, h - 1);
/* 211 */     c = isSelected ? new Color(47, 111, 180) : new Color(135, 164, 196);
/* 212 */     g.setColor(c);
/* 213 */     g.drawLine(w - 1, 0, w - 1, h);
/*     */   }
/*     */   
/*     */   private void drawRight(Graphics g, boolean isSelected, int w, int h) {
/* 217 */     if (LiquidLookAndFeel.winDecoPanther) {
/* 218 */       Color c = new Color(198, 198, 198);
/* 219 */       g.setColor(c);
/* 220 */       g.fillRect(0, 0, w, h);
/* 221 */       return;
/*     */     }
/*     */     
/* 224 */     Color c = isSelected ? new Color(62, 145, 235) : new Color(175, 214, 255);
/* 225 */     g.setColor(c);
/* 226 */     g.fillRect(0, 0, w, h);
/* 227 */     c = isSelected ? new Color(94, 172, 255) : new Color(226, 240, 255);
/* 228 */     g.setColor(c);
/* 229 */     g.drawLine(0, 0, 0, h);
/* 230 */     c = isSelected ? new Color(59, 138, 223) : new Color(166, 203, 242);
/* 231 */     g.setColor(c);
/* 232 */     g.drawLine(1, 0, w, 0);
/*     */     
/* 234 */     for (int i = 3; i < h; i += 4) {
/* 235 */       c = isSelected ? new Color(59, 138, 223) : new Color(166, 203, 242);
/* 236 */       g.setColor(c);
/* 237 */       g.drawLine(1, i, w, i);
/* 238 */       c = isSelected ? new Color(60, 141, 228) : new Color(170, 207, 247);
/* 239 */       g.setColor(c);
/* 240 */       g.drawLine(1, i + 1, w, i + 1);
/*     */     }
/*     */     
/* 243 */     c = isSelected ? new Color(47, 111, 180) : new Color(135, 164, 196);
/* 244 */     g.setColor(c);
/* 245 */     g.drawLine(w - 1, 0, w - 1, h);
/*     */   }
/*     */   
/*     */   private void drawBottom(Graphics g, boolean isSelected, int w, int h) {
/* 249 */     if (LiquidLookAndFeel.winDecoPanther) {
/* 250 */       Color c = new Color(198, 198, 198);
/* 251 */       g.setColor(c);
/* 252 */       g.fillRect(0, 0, w, h);
/* 253 */       return;
/*     */     }
/*     */     
/* 256 */     Color c = isSelected ? new Color(62, 145, 235) : new Color(175, 214, 255);
/* 257 */     g.setColor(c);
/* 258 */     g.fillRect(1, 0, w - 1, h - 1);
/* 259 */     c = isSelected ? new Color(94, 172, 255) : new Color(226, 240, 255);
/* 260 */     g.setColor(c);
/* 261 */     g.drawLine(3, 0, w - 4, 0);
/* 262 */     g.drawLine(0, 0, 0, h - 2);
/* 263 */     c = isSelected ? new Color(47, 111, 180) : new Color(135, 164, 196);
/* 264 */     g.setColor(c);
/* 265 */     g.drawLine(0, h - 1, w, h - 1);
/* 266 */     g.drawLine(w - 1, 0, w - 1, h - 1);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public Insets getBorderInsets(Component c)
/*     */   {
/* 274 */     if (LiquidLookAndFeel.winDecoPanther) {
/* 275 */       return new Insets(0, 1, 1, 1);
/*     */     }
/*     */     
/* 278 */     return new Insets(0, 4, 4, 4);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setActive(boolean isActive)
/*     */   {
/* 286 */     this.isActive = isActive;
/*     */   }
/*     */ }


/* Location:              E:\java\java学习\hutubill\lib\all.jar!\com\birosoft\liquid\LiquidFrameBorder.class
 * Java compiler version: 4 (48.0)
 * JD-Core Version:       0.7.1
 */
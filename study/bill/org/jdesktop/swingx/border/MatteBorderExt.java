/*     */ package org.jdesktop.swingx.border;
/*     */ 
/*     */ import java.awt.Color;
/*     */ import java.awt.Component;
/*     */ import java.awt.Graphics;
/*     */ import java.awt.Insets;
/*     */ import javax.swing.Icon;
/*     */ import javax.swing.border.MatteBorder;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class MatteBorderExt
/*     */   extends MatteBorder
/*     */ {
/*  39 */   protected Icon[] tileIcons = null;
/*  40 */   private Icon defaultIcon = null;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public MatteBorderExt(int top, int left, int bottom, int right, Icon[] tileIcons)
/*     */   {
/*  62 */     super(top, left, bottom, right, (tileIcons == null) || (tileIcons.length == 0) ? null : tileIcons[0]);
/*     */     
/*     */ 
/*  65 */     this.tileIcons = tileIcons;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public MatteBorderExt(int top, int left, int bottom, int right, Color matteColor)
/*     */   {
/*  73 */     super(top, left, bottom, right, matteColor);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public MatteBorderExt(Insets borderInsets, Color matteColor)
/*     */   {
/*  80 */     super(borderInsets, matteColor);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public MatteBorderExt(int top, int left, int bottom, int right, Icon tileIcon)
/*     */   {
/*  88 */     super(top, left, bottom, right, tileIcon);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public MatteBorderExt(Insets borderInsets, Icon tileIcon)
/*     */   {
/*  95 */     super(borderInsets, tileIcon);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public MatteBorderExt(Icon tileIcon)
/*     */   {
/* 102 */     super(tileIcon);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public Icon[] getTileIcons()
/*     */   {
/* 111 */     return this.tileIcons;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void paintBorder(Component c, Graphics g, int x, int y, int width, int height)
/*     */   {
/* 120 */     if ((this.tileIcons == null) || (this.tileIcons.length < 2)) {
/* 121 */       super.paintBorder(c, g, x, y, width, height);
/* 122 */       return;
/*     */     }
/*     */     
/* 125 */     Insets insets = getBorderInsets(c);
/*     */     
/*     */ 
/* 128 */     int clipWidth = Math.min(width, insets.left);
/* 129 */     int clipHeight = Math.min(height, insets.top);
/*     */     
/* 131 */     if ((clipWidth <= 0) || (clipHeight <= 0)) {
/* 132 */       return;
/*     */     }
/*     */     
/*     */ 
/*     */ 
/* 137 */     Color oldColor = g.getColor();
/* 138 */     g.translate(x, y);
/*     */     
/* 140 */     for (int i = 0; i < this.tileIcons.length; i++)
/*     */     {
/* 142 */       if (this.tileIcons[i] == null) {
/* 143 */         this.tileIcons[i] = getDefaultIcon();
/*     */       }
/*     */     }
/*     */     
/* 147 */     paintTopLeft(c, g, 0, 0, insets.left, insets.top);
/* 148 */     paintTop(c, g, insets.left, 0, width - insets.left - insets.right, insets.top);
/* 149 */     paintTopRight(c, g, width - insets.right, 0, insets.right, insets.top);
/* 150 */     paintRight(c, g, width - insets.right, insets.top, insets.right, height - insets.top - insets.bottom);
/* 151 */     paintBottomRight(c, g, width - insets.right, height - insets.bottom, insets.right, insets.bottom);
/* 152 */     paintBottom(c, g, insets.left, height - insets.bottom, width - insets.left - insets.right, insets.bottom);
/* 153 */     paintBottomLeft(c, g, 0, height - insets.bottom, insets.left, insets.bottom);
/* 154 */     paintLeft(c, g, 0, insets.top, insets.left, height - insets.top - insets.bottom);
/*     */     
/* 156 */     g.translate(-x, -y);
/* 157 */     g.setColor(oldColor);
/*     */   }
/*     */   
/*     */ 
/*     */   protected void paint(Icon icon, Component c, Graphics g, int x, int y, int width, int height)
/*     */   {
/* 163 */     Graphics cg = g.create();
/*     */     try
/*     */     {
/* 166 */       cg.setClip(x, y, width, height);
/* 167 */       int tileW = icon.getIconWidth();
/* 168 */       int tileH = icon.getIconHeight();
/*     */       
/* 170 */       for (int ypos = 0; height - ypos > 0; ypos += tileH) {
/* 171 */         for (int xpos = 0; width - xpos > 0; xpos += tileW) {
/* 172 */           icon.paintIcon(c, cg, x + xpos, y + ypos);
/*     */         }
/*     */       }
/*     */     } finally {
/* 176 */       cg.dispose();
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   protected void paintTopLeft(Component c, Graphics g, int x, int y, int width, int height)
/*     */   {
/* 184 */     Graphics cg = g.create();
/*     */     try
/*     */     {
/* 187 */       cg.setClip(x, y, width, height);
/* 188 */       this.tileIcons[0].paintIcon(c, cg, x, y);
/*     */     } finally {
/* 190 */       cg.dispose();
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   protected void paintTop(Component c, Graphics g, int x, int y, int width, int height)
/*     */   {
/* 198 */     paint(this.tileIcons[1], c, g, x, y, width, height);
/*     */   }
/*     */   
/*     */ 
/*     */   protected void paintTopRight(Component c, Graphics g, int x, int y, int width, int height)
/*     */   {
/*     */     Icon icon;
/* 205 */     if (this.tileIcons.length == 8) {
/* 206 */       paint(this.tileIcons[2], c, g, x, y, width, height);
/*     */     }
/*     */     else {
/* 209 */       icon = this.tileIcons[0];
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */   protected void paintRight(Component c, Graphics g, int x, int y, int width, int height)
/*     */   {
/*     */     Icon icon;
/*     */     
/* 218 */     if (this.tileIcons.length == 8) {
/* 219 */       paint(this.tileIcons[3], c, g, x, y, width, height);
/*     */     }
/*     */     else {
/* 222 */       icon = this.tileIcons[1];
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */   protected void paintBottomRight(Component c, Graphics g, int x, int y, int width, int height)
/*     */   {
/*     */     Icon icon;
/*     */     
/* 231 */     if (this.tileIcons.length == 8) {
/* 232 */       paint(this.tileIcons[4], c, g, x, y, width, height);
/*     */     }
/*     */     else {
/* 235 */       icon = this.tileIcons[0];
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */   protected void paintBottom(Component c, Graphics g, int x, int y, int width, int height)
/*     */   {
/*     */     Icon icon;
/*     */     
/* 244 */     if (this.tileIcons.length == 8) {
/* 245 */       paint(this.tileIcons[5], c, g, x, y, width, height);
/*     */     }
/*     */     else {
/* 248 */       icon = this.tileIcons[1];
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */   protected void paintBottomLeft(Component c, Graphics g, int x, int y, int width, int height)
/*     */   {
/*     */     Icon icon;
/*     */     
/* 257 */     if (this.tileIcons.length == 8) {
/* 258 */       paint(this.tileIcons[6], c, g, x, y, width, height);
/*     */     }
/*     */     else {
/* 261 */       icon = this.tileIcons[0];
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */   protected void paintLeft(Component c, Graphics g, int x, int y, int width, int height)
/*     */   {
/*     */     Icon icon;
/*     */     
/* 270 */     if (this.tileIcons.length == 8) {
/* 271 */       paint(this.tileIcons[7], c, g, x, y, width, height);
/*     */     }
/*     */     else {
/* 274 */       icon = this.tileIcons[1];
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   protected Icon getDefaultIcon()
/*     */   {
/* 283 */     if (this.defaultIcon == null) {
/* 284 */       this.defaultIcon = new Icon() {
/* 285 */         private int width = 3;
/* 286 */         private int height = 3;
/*     */         
/*     */         public int getIconWidth() {
/* 289 */           return this.width;
/*     */         }
/*     */         
/*     */         public int getIconHeight() {
/* 293 */           return this.height;
/*     */         }
/*     */         
/*     */         public void paintIcon(Component c, Graphics g, int x, int y) {
/* 297 */           g.setColor(c.getBackground().darker().darker());
/*     */           
/* 299 */           g.fillRect(x, y, this.width, this.height);
/*     */         }
/*     */       };
/*     */     }
/* 303 */     return this.defaultIcon;
/*     */   }
/*     */ }


/* Location:              E:\java\java学习\hutubill\lib\all.jar!\org\jdesktop\swingx\border\MatteBorderExt.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */
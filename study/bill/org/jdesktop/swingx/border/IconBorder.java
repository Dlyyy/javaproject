/*     */ package org.jdesktop.swingx.border;
/*     */ 
/*     */ import java.awt.Component;
/*     */ import java.awt.ComponentOrientation;
/*     */ import java.awt.Graphics;
/*     */ import java.awt.Insets;
/*     */ import java.awt.Rectangle;
/*     */ import java.io.Serializable;
/*     */ import javax.swing.Icon;
/*     */ import javax.swing.border.Border;
/*     */ import org.jdesktop.swingx.icon.EmptyIcon;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class IconBorder
/*     */   implements Border, Serializable
/*     */ {
/*  57 */   public static final Icon EMPTY_ICON = new EmptyIcon();
/*     */   private int padding;
/*     */   private Icon icon;
/*     */   private int iconPosition;
/*  61 */   private Rectangle iconBounds = new Rectangle();
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public IconBorder()
/*     */   {
/*  70 */     this(null);
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
/*     */   public IconBorder(Icon validIcon)
/*     */   {
/*  83 */     this(validIcon, 11);
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
/*     */   public IconBorder(Icon validIcon, int iconPosition)
/*     */   {
/* 108 */     this(validIcon, iconPosition, 4);
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public IconBorder(Icon validIcon, int iconPosition, int padding)
/*     */   {
/* 137 */     setIcon(validIcon);
/* 138 */     setPadding(padding);
/* 139 */     setIconPosition(iconPosition);
/*     */   }
/*     */   
/*     */   private boolean isValidPosition(int position) {
/* 143 */     boolean result = false;
/*     */     
/* 145 */     switch (position) {
/*     */     case 3: 
/*     */     case 7: 
/*     */     case 10: 
/*     */     case 11: 
/* 150 */       result = true;
/* 151 */       break;
/*     */     case 4: case 5: case 6: case 8: case 9: default: 
/* 153 */       result = false;
/*     */     }
/*     */     
/* 156 */     return result;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public Insets getBorderInsets(Component c)
/*     */   {
/* 163 */     int horizontalInset = this.icon.getIconWidth() + 2 * this.padding;
/* 164 */     int iconPosition = bidiDecodeLeadingTrailing(c.getComponentOrientation(), this.iconPosition);
/* 165 */     if (iconPosition == 3) {
/* 166 */       return new Insets(0, 0, 0, horizontalInset);
/*     */     }
/* 168 */     return new Insets(0, horizontalInset, 0, 0);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setIcon(Icon validIcon)
/*     */   {
/* 180 */     this.icon = (validIcon == null ? EMPTY_ICON : validIcon);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean isBorderOpaque()
/*     */   {
/* 189 */     return false;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void paintBorder(Component c, Graphics g, int x, int y, int width, int height)
/*     */   {
/* 197 */     int iconPosition = bidiDecodeLeadingTrailing(c.getComponentOrientation(), this.iconPosition);
/* 198 */     if (iconPosition == 2) {
/* 199 */       this.iconBounds.y = (y + this.padding);
/* 200 */       this.iconBounds.x = (x + width - this.padding - this.icon.getIconWidth());
/* 201 */     } else if (iconPosition == 3) {
/* 202 */       this.iconBounds.y = (y + (height - this.icon.getIconHeight()) / 2);
/*     */       
/* 204 */       this.iconBounds.x = (x + width - this.padding - this.icon.getIconWidth());
/* 205 */     } else if (iconPosition == 7) {
/* 206 */       this.iconBounds.y = (y + (height - this.icon.getIconHeight()) / 2);
/*     */       
/* 208 */       this.iconBounds.x = (x + this.padding);
/*     */     }
/* 210 */     this.iconBounds.width = this.icon.getIconWidth();
/* 211 */     this.iconBounds.height = this.icon.getIconHeight();
/* 212 */     this.icon.paintIcon(c, g, this.iconBounds.x, this.iconBounds.y);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private int bidiDecodeLeadingTrailing(ComponentOrientation c, int position)
/*     */   {
/* 221 */     if (position == 11) {
/* 222 */       if (!c.isLeftToRight()) {
/* 223 */         return 7;
/*     */       }
/* 225 */       return 3;
/*     */     }
/* 227 */     if (position == 10) {
/* 228 */       if (c.isLeftToRight()) {
/* 229 */         return 7;
/*     */       }
/* 231 */       return 3;
/*     */     }
/* 233 */     return position;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public int getPadding()
/*     */   {
/* 243 */     return this.padding;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setPadding(int padding)
/*     */   {
/* 254 */     this.padding = (padding < 0 ? 0 : padding);
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
/*     */   public int getIconPosition()
/*     */   {
/* 269 */     return this.iconPosition;
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
/*     */   public void setIconPosition(int iconPosition)
/*     */   {
/* 286 */     if (!isValidPosition(iconPosition)) {
/* 287 */       throw new IllegalArgumentException("Invalid icon position");
/*     */     }
/* 289 */     this.iconPosition = iconPosition;
/*     */   }
/*     */ }


/* Location:              E:\java\java学习\hutubill\lib\all.jar!\org\jdesktop\swingx\border\IconBorder.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */
/*     */ package com.birosoft.liquid;
/*     */ 
/*     */ import com.birosoft.liquid.skin.Skin;
/*     */ import com.birosoft.liquid.skin.SkinSimpleButtonIndexModel;
/*     */ import java.awt.Color;
/*     */ import java.awt.ComponentOrientation;
/*     */ import java.awt.Dimension;
/*     */ import java.awt.Graphics;
/*     */ import java.awt.Insets;
/*     */ import java.awt.Rectangle;
/*     */ import java.awt.event.MouseEvent;
/*     */ import javax.swing.JButton;
/*     */ import javax.swing.JComponent;
/*     */ import javax.swing.JScrollBar;
/*     */ import javax.swing.plaf.ComponentUI;
/*     */ import javax.swing.plaf.basic.BasicScrollBarUI;
/*     */ import javax.swing.plaf.basic.BasicScrollBarUI.TrackListener;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class LiquidScrollBarUI
/*     */   extends BasicScrollBarUI
/*     */ {
/*  35 */   private int orientation = -1;
/*     */   private int minExtent;
/*  37 */   private int minValue; private final int MIN_THUMB_SIZE = 14;
/*     */   
/*     */ 
/*     */ 
/*     */   public static final String FREE_STANDING_PROP = "JScrollBar.isFreeStanding";
/*     */   
/*     */ 
/*     */ 
/*     */   private static Color highlightColor;
/*     */   
/*     */ 
/*     */ 
/*     */   private static Color darkShadowColor;
/*     */   
/*     */ 
/*     */ 
/*     */   private static Color thumbShadow;
/*     */   
/*     */ 
/*     */ 
/*     */   private static Color thumbHighlightColor;
/*     */   
/*     */ 
/*     */ 
/*  61 */   protected boolean isRollover = false;
/*     */   
/*  63 */   protected boolean wasRollover = false;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*  68 */   private boolean freeStanding = false;
/*     */   
/*     */ 
/*     */   int scrollBarWidth;
/*     */   
/*     */ 
/*     */   private static Skin skinTrackVert;
/*     */   
/*     */ 
/*     */   private static Skin skinTrackHoriz;
/*     */   
/*     */ 
/*     */   private Skin skinTrack;
/*     */   
/*     */   private static Skin skinThumbVert;
/*     */   
/*     */   private static Skin skinThumbHoriz;
/*     */   
/*     */   private Skin skinThumb;
/*     */   
/*  88 */   private SkinSimpleButtonIndexModel skinThumbIndexModel = new SkinSimpleButtonIndexModel();
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   JButton decreaseButton;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   JButton increaseButton;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected void installDefaults()
/*     */   {
/* 109 */     this.scrollBarWidth = LiquidScrollButton.getSkinUp().getHsize();
/* 110 */     super.installDefaults();
/* 111 */     this.scrollbar.setBorder(null);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static ComponentUI createUI(JComponent c)
/*     */   {
/* 123 */     return new LiquidScrollBarUI();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected JButton createDecreaseButton(int orientation)
/*     */   {
/* 135 */     this.decreaseButton = new LiquidScrollButton(orientation, this.scrollBarWidth, this.freeStanding);
/* 136 */     return this.decreaseButton;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected JButton createIncreaseButton(int orientation)
/*     */   {
/* 147 */     this.increaseButton = new LiquidScrollButton(orientation, this.scrollBarWidth, this.freeStanding);
/* 148 */     return this.increaseButton;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public Dimension getPreferredSize(JComponent c)
/*     */   {
/* 155 */     if (this.scrollbar.getOrientation() == 1)
/*     */     {
/* 157 */       return new Dimension(this.scrollBarWidth, this.scrollBarWidth * 3 + 10);
/*     */     }
/*     */     
/* 160 */     return new Dimension(this.scrollBarWidth * 3 + 10, this.scrollBarWidth);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   protected void layoutVScrollbar(JScrollBar sb)
/*     */   {
/* 167 */     Dimension sbSize = sb.getSize();
/* 168 */     Insets sbInsets = sb.getInsets();
/*     */     
/*     */ 
/*     */ 
/*     */ 
/* 173 */     int itemW = sbSize.width - (sbInsets.left + sbInsets.right);
/* 174 */     int itemX = sbInsets.left;
/*     */     
/*     */ 
/*     */ 
/*     */ 
/* 179 */     int decrButtonH = this.decrButton.getPreferredSize().height;
/* 180 */     int decrButtonY = sbInsets.top;
/*     */     
/* 182 */     int incrButtonH = this.incrButton.getPreferredSize().height;
/* 183 */     int incrButtonY = sbSize.height - (sbInsets.bottom + incrButtonH);
/*     */     
/*     */ 
/*     */ 
/*     */ 
/* 188 */     int sbInsetsH = sbInsets.top + sbInsets.bottom;
/* 189 */     int sbButtonsH = decrButtonH + incrButtonH;
/* 190 */     float trackH = sbSize.height - (sbInsetsH + sbButtonsH);
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 198 */     float min = sb.getMinimum();
/* 199 */     float extent = sb.getVisibleAmount();
/* 200 */     float range = sb.getMaximum() - min;
/* 201 */     float value = sb.getValue();
/*     */     
/* 203 */     int thumbH = range <= 0.0F ? getMaximumThumbSize().height : (int)(trackH * (extent / range));
/*     */     
/* 205 */     thumbH = Math.max(thumbH, getMinimumThumbSize().height);
/* 206 */     thumbH = Math.min(thumbH, getMaximumThumbSize().height);
/*     */     
/* 208 */     if (thumbH < 14) {
/* 209 */       thumbH = 14;
/*     */     }
/* 211 */     int thumbY = incrButtonY - thumbH;
/* 212 */     if (sb.getValue() < sb.getMaximum() - sb.getVisibleAmount())
/*     */     {
/* 214 */       float thumbRange = trackH - thumbH;
/* 215 */       thumbY = (int)(0.5F + thumbRange * ((value - min) / (range - extent)));
/* 216 */       thumbY += decrButtonY + decrButtonH;
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/* 222 */     int sbAvailButtonH = sbSize.height - sbInsetsH;
/* 223 */     if (sbAvailButtonH < sbButtonsH)
/*     */     {
/* 225 */       incrButtonH = decrButtonH = sbAvailButtonH / 2;
/* 226 */       incrButtonY = sbSize.height - (sbInsets.bottom + incrButtonH);
/*     */     }
/* 228 */     this.decrButton.setBounds(itemX, decrButtonY, itemW, decrButtonH);
/* 229 */     this.incrButton.setBounds(itemX, incrButtonY, itemW, incrButtonH);
/*     */     
/*     */ 
/*     */ 
/* 233 */     int itrackY = decrButtonY + decrButtonH;
/* 234 */     int itrackH = incrButtonY - itrackY;
/* 235 */     this.trackRect.setBounds(itemX, itrackY, itemW, itrackH);
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 241 */     if (thumbH >= (int)trackH)
/*     */     {
/* 243 */       setThumbBounds(0, 0, 0, 0);
/*     */     }
/*     */     else
/*     */     {
/* 247 */       if (thumbY + thumbH > incrButtonY)
/*     */       {
/* 249 */         thumbY = incrButtonY - thumbH;
/*     */       }
/* 251 */       if (thumbY < decrButtonY + decrButtonH)
/*     */       {
/* 253 */         thumbY = decrButtonY + decrButtonH + 1;
/*     */       }
/* 255 */       setThumbBounds(itemX, thumbY, itemW, thumbH);
/*     */     }
/*     */   }
/*     */   
/*     */   protected void layoutHScrollbar(JScrollBar sb)
/*     */   {
/* 261 */     Dimension sbSize = sb.getSize();
/* 262 */     Insets sbInsets = sb.getInsets();
/*     */     
/*     */ 
/*     */ 
/* 266 */     int itemH = sbSize.height - (sbInsets.top + sbInsets.bottom);
/* 267 */     int itemY = sbInsets.top;
/*     */     
/* 269 */     boolean ltr = sb.getComponentOrientation().isLeftToRight();
/*     */     
/*     */ 
/*     */ 
/*     */ 
/* 274 */     int leftButtonW = (ltr ? this.decrButton : this.incrButton).getPreferredSize().width;
/* 275 */     int rightButtonW = (ltr ? this.incrButton : this.decrButton).getPreferredSize().width;
/* 276 */     int leftButtonX = sbInsets.left;
/* 277 */     int rightButtonX = sbSize.width - (sbInsets.right + rightButtonW);
/*     */     
/*     */ 
/*     */ 
/*     */ 
/* 282 */     int sbInsetsW = sbInsets.left + sbInsets.right;
/* 283 */     int sbButtonsW = leftButtonW + rightButtonW;
/* 284 */     float trackW = sbSize.width - (sbInsetsW + sbButtonsW);
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 292 */     float min = sb.getMinimum();
/* 293 */     float max = sb.getMaximum();
/* 294 */     float extent = sb.getVisibleAmount();
/* 295 */     float range = max - min;
/* 296 */     float value = sb.getValue();
/*     */     
/* 298 */     int thumbW = range <= 0.0F ? getMaximumThumbSize().width : (int)(trackW * (extent / range));
/*     */     
/* 300 */     thumbW = Math.max(thumbW, getMinimumThumbSize().width);
/* 301 */     thumbW = Math.min(thumbW, getMaximumThumbSize().width);
/*     */     
/* 303 */     if (thumbW < 14) {
/* 304 */       thumbW = 14;
/*     */     }
/* 306 */     int thumbX = ltr ? rightButtonX - thumbW : leftButtonX + leftButtonW;
/* 307 */     if (sb.getValue() < max - sb.getVisibleAmount()) {
/* 308 */       float thumbRange = trackW - thumbW;
/* 309 */       if (ltr) {
/* 310 */         thumbX = (int)(0.5F + thumbRange * ((value - min) / (range - extent)));
/*     */       } else {
/* 312 */         thumbX = (int)(0.5F + thumbRange * ((max - extent - value) / (range - extent)));
/*     */       }
/* 314 */       thumbX += leftButtonX + leftButtonW;
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/* 320 */     int sbAvailButtonW = sbSize.width - sbInsetsW;
/* 321 */     if (sbAvailButtonW < sbButtonsW) {
/* 322 */       rightButtonW = leftButtonW = sbAvailButtonW / 2;
/* 323 */       rightButtonX = sbSize.width - (sbInsets.right + rightButtonW);
/*     */     }
/*     */     
/* 326 */     (ltr ? this.decrButton : this.incrButton).setBounds(leftButtonX, itemY, leftButtonW, itemH);
/* 327 */     (ltr ? this.incrButton : this.decrButton).setBounds(rightButtonX, itemY, rightButtonW, itemH);
/*     */     
/*     */ 
/*     */ 
/* 331 */     int itrackX = leftButtonX + leftButtonW;
/* 332 */     int itrackW = rightButtonX - itrackX;
/* 333 */     this.trackRect.setBounds(itrackX, itemY, itrackW, itemH);
/*     */     
/*     */ 
/*     */ 
/*     */ 
/* 338 */     if (thumbW >= (int)trackW) {
/* 339 */       setThumbBounds(0, 0, 0, 0);
/*     */     }
/*     */     else {
/* 342 */       if (thumbX + thumbW > rightButtonX) {
/* 343 */         thumbX = rightButtonX - thumbW;
/*     */       }
/* 345 */       if (thumbX < leftButtonX + leftButtonW) {
/* 346 */         thumbX = leftButtonX + leftButtonW + 1;
/*     */       }
/* 348 */       setThumbBounds(thumbX, itemY, thumbW, itemH);
/*     */     }
/*     */   }
/*     */   
/*     */   public void paint(Graphics g, JComponent c)
/*     */   {
/* 354 */     if (this.orientation == -1) this.orientation = this.scrollbar.getOrientation();
/* 355 */     Rectangle trackBounds = getTrackBounds();
/* 356 */     getSkinTrack().draw(g, 0, trackBounds.x, trackBounds.y, trackBounds.width, trackBounds.height);
/*     */     
/* 358 */     Rectangle thumbBounds = getThumbBounds();
/* 359 */     int index = this.skinThumbIndexModel.getIndexForState(c.isEnabled(), this.isRollover, this.isDragging);
/*     */     
/* 361 */     int x = this.orientation == 1 ? thumbBounds.x + 1 : thumbBounds.x;
/* 362 */     int y = this.orientation == 1 ? thumbBounds.y : thumbBounds.y + 1;
/* 363 */     int width = this.orientation == 1 ? thumbBounds.width - 2 : thumbBounds.width;
/* 364 */     int height = this.orientation == 1 ? thumbBounds.height : thumbBounds.height - 2;
/* 365 */     getSkinThumb().draw(g, index, x, y, width, height);
/*     */   }
/*     */   
/*     */   public boolean isThumbVisible()
/*     */   {
/* 370 */     if (this.scrollbar.getOrientation() == 1)
/*     */     {
/* 372 */       if (getThumbBounds().height == 0) {
/* 373 */         return false;
/*     */       }
/* 375 */       return true;
/*     */     }
/*     */     
/* 378 */     if (getThumbBounds().width == 0) {
/* 379 */       return false;
/*     */     }
/* 381 */     return true;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   protected BasicScrollBarUI.TrackListener createTrackListener()
/*     */   {
/* 388 */     return new MyTrackListener();
/*     */   }
/*     */   
/*     */   protected class MyTrackListener extends BasicScrollBarUI.TrackListener
/*     */   {
/*     */     protected MyTrackListener()
/*     */     {
/* 395 */       super();
/*     */     }
/*     */     
/*     */     public void mouseReleased(MouseEvent e) {
/* 399 */       super.mouseReleased(e);
/* 400 */       LiquidScrollBarUI.this.scrollbar.repaint();
/*     */     }
/*     */     
/*     */     public void mousePressed(MouseEvent e)
/*     */     {
/* 405 */       super.mousePressed(e);
/* 406 */       LiquidScrollBarUI.this.scrollbar.repaint();
/*     */     }
/*     */     
/*     */     public void mouseEntered(MouseEvent e) {
/* 410 */       LiquidScrollBarUI.this.isRollover = false;
/* 411 */       LiquidScrollBarUI.this.wasRollover = false;
/* 412 */       if (LiquidScrollBarUI.this.getThumbBounds().contains(e.getX(), e.getY()))
/*     */       {
/* 414 */         LiquidScrollBarUI.this.isRollover = true;
/*     */       }
/*     */     }
/*     */     
/*     */     public void mouseExited(MouseEvent e) {
/* 419 */       LiquidScrollBarUI.this.isRollover = false;
/* 420 */       if (LiquidScrollBarUI.this.isRollover != LiquidScrollBarUI.this.wasRollover)
/*     */       {
/* 422 */         LiquidScrollBarUI.this.scrollbar.repaint();
/* 423 */         LiquidScrollBarUI.this.wasRollover = LiquidScrollBarUI.this.isRollover;
/*     */       }
/*     */     }
/*     */     
/*     */     public void mouseDragged(MouseEvent e) {
/* 428 */       if (LiquidScrollBarUI.this.getThumbBounds().contains(e.getX(), e.getY()))
/*     */       {
/* 430 */         LiquidScrollBarUI.this.isRollover = true;
/*     */       }
/* 432 */       super.mouseDragged(e);
/*     */     }
/*     */     
/*     */     public void mouseMoved(MouseEvent e) {
/* 436 */       if (LiquidScrollBarUI.this.getThumbBounds().contains(e.getX(), e.getY()))
/*     */       {
/* 438 */         LiquidScrollBarUI.this.isRollover = true;
/* 439 */         if (LiquidScrollBarUI.this.isRollover != LiquidScrollBarUI.this.wasRollover)
/*     */         {
/* 441 */           LiquidScrollBarUI.this.scrollbar.repaint();
/* 442 */           LiquidScrollBarUI.this.wasRollover = LiquidScrollBarUI.this.isRollover;
/*     */         }
/*     */       }
/*     */       else {
/* 446 */         LiquidScrollBarUI.this.isRollover = false;
/* 447 */         if (LiquidScrollBarUI.this.isRollover != LiquidScrollBarUI.this.wasRollover)
/*     */         {
/* 449 */           LiquidScrollBarUI.this.scrollbar.repaint();
/* 450 */           LiquidScrollBarUI.this.wasRollover = LiquidScrollBarUI.this.isRollover;
/*     */         }
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static Skin getSkinThumbHoriz()
/*     */   {
/* 462 */     if (skinThumbHoriz == null)
/*     */     {
/* 464 */       skinThumbHoriz = new Skin("scrollbarthumbhoriz.png", 4, 8, 6, 8, 8);
/*     */     }
/* 466 */     return skinThumbHoriz;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static Skin getSkinThumbVert()
/*     */   {
/* 475 */     if (skinThumbVert == null)
/*     */     {
/* 477 */       skinThumbVert = new Skin("scrollbarthumbvert.png", 4, 6, 8, 8, 7);
/*     */     }
/* 479 */     return skinThumbVert;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static Skin getSkinTrackHoriz()
/*     */   {
/* 488 */     if (skinTrackHoriz == null)
/*     */     {
/* 490 */       skinTrackHoriz = new Skin("scrollbartrackhoriz.png", 1, 7);
/*     */     }
/* 492 */     return skinTrackHoriz;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static Skin getSkinTrackVert()
/*     */   {
/* 501 */     if (skinTrackVert == null)
/*     */     {
/* 503 */       skinTrackVert = new Skin("scrollbartrackvert.png", 1, 7);
/*     */     }
/* 505 */     return skinTrackVert;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public Skin getSkinTrack()
/*     */   {
/* 514 */     if (this.skinTrack == null)
/*     */     {
/* 516 */       this.skinTrack = (this.scrollbar.getOrientation() == 1 ? getSkinTrackVert() : getSkinTrackHoriz());
/*     */     }
/* 518 */     return this.skinTrack;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public Skin getSkinThumb()
/*     */   {
/* 527 */     if (this.skinThumb == null)
/*     */     {
/* 529 */       this.skinThumb = (this.scrollbar.getOrientation() == 1 ? getSkinThumbVert() : getSkinThumbHoriz());
/*     */     }
/* 531 */     return this.skinThumb;
/*     */   }
/*     */ }


/* Location:              E:\java\java学习\hutubill\lib\all.jar!\com\birosoft\liquid\LiquidScrollBarUI.class
 * Java compiler version: 4 (48.0)
 * JD-Core Version:       0.7.1
 */
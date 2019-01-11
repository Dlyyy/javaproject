/*     */ package com.birosoft.liquid;
/*     */ 
/*     */ import com.birosoft.liquid.skin.Skin;
/*     */ import com.birosoft.liquid.skin.SkinSimpleButtonIndexModel;
/*     */ import com.birosoft.liquid.util.Colors;
/*     */ import java.awt.BasicStroke;
/*     */ import java.awt.Color;
/*     */ import java.awt.Font;
/*     */ import java.awt.FontMetrics;
/*     */ import java.awt.Graphics;
/*     */ import java.awt.Insets;
/*     */ import java.awt.Rectangle;
/*     */ import java.awt.event.MouseEvent;
/*     */ import java.awt.event.MouseListener;
/*     */ import java.awt.event.MouseMotionListener;
/*     */ import javax.swing.JButton;
/*     */ import javax.swing.JComponent;
/*     */ import javax.swing.JTabbedPane;
/*     */ import javax.swing.plaf.ComponentUI;
/*     */ import javax.swing.plaf.UIResource;
/*     */ import javax.swing.plaf.basic.BasicGraphicsUtils;
/*     */ import javax.swing.plaf.basic.BasicTabbedPaneUI;
/*     */ import javax.swing.plaf.basic.BasicTabbedPaneUI.TabbedPaneLayout;
/*     */ import javax.swing.text.View;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class LiquidTabbedPaneUI
/*     */   extends BasicTabbedPaneUI
/*     */ {
/*     */   static Skin skinTop;
/*     */   static Skin skinLeft;
/*     */   static Skin skinRight;
/*     */   static Skin skinBottom;
/*     */   static Skin skinBorder;
/*     */   static Skin skinBorderBottom;
/*     */   static Skin skinBorderVertical;
/*     */   SkinSimpleButtonIndexModel indexModel;
/*  53 */   static BasicStroke focusStroke = new BasicStroke(1.0F, 0, 2, 1.0F, new float[] { 1.0F }, 1.0F);
/*     */   int rollover;
/*     */   MouseMotionListener mouseMotionHandler;
/*     */   
/*     */   public LiquidTabbedPaneUI()
/*     */   {
/*  52 */     this.indexModel = new SkinSimpleButtonIndexModel();
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*  78 */     this.rollover = -1;
/*     */     
/*  80 */     this.mouseMotionHandler = null;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static ComponentUI createUI(JComponent c)
/*     */   {
/*  89 */     return new LiquidTabbedPaneUI();
/*     */   }
/*     */   
/*     */   protected void installListeners() {
/*  93 */     super.installListeners();
/*  94 */     this.mouseMotionHandler = new MyMouseHandler();
/*  95 */     this.tabPane.addMouseMotionListener(this.mouseMotionHandler);
/*     */   }
/*     */   
/*     */   protected void uninstallListeners() {
/*  99 */     super.uninstallListeners();
/* 100 */     this.tabPane.removeMouseMotionListener(this.mouseMotionHandler);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected boolean scrollableTabLayoutEnabled()
/*     */   {
/* 111 */     return this.tabPane.getTabLayoutPolicy() == 1;
/*     */   }
/*     */   
/*     */   private void ensureCurrentLayout() {
/* 115 */     if (!this.tabPane.isValid()) {
/* 116 */       this.tabPane.validate();
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 123 */     if (!this.tabPane.isValid()) {
/* 124 */       BasicTabbedPaneUI.TabbedPaneLayout layout = (BasicTabbedPaneUI.TabbedPaneLayout)this.tabPane.getLayout();
/* 125 */       layout.calculateLayoutInfo();
/*     */     }
/*     */   }
/*     */   
/*     */   private int getTabAtLocation(int x, int y) {
/* 130 */     ensureCurrentLayout();
/*     */     
/*     */ 
/*     */ 
/*     */ 
/* 135 */     if (LiquidLookAndFeel.isPreTiger()) {
/* 136 */       int tabCount = this.tabPane.getTabCount();
/*     */       
/* 138 */       for (int i = 0; i < tabCount; i++) {
/* 139 */         if (this.rects[i].contains(x, y)) {
/* 140 */           return i;
/*     */         }
/*     */       }
/*     */       
/* 144 */       return -1;
/*     */     }
/* 146 */     return tabForCoordinate(this.tabPane, x, y);
/*     */   }
/*     */   
/*     */   protected JButton createScrollButton(int direction) {
/* 150 */     if ((direction != 5) && (direction != 1) && (direction != 3) && (direction != 7))
/*     */     {
/* 152 */       throw new IllegalArgumentException("Direction must be one of: SOUTH, NORTH, EAST or WEST");
/*     */     }
/*     */     
/* 155 */     return new LiquidScrollableTabButton(direction);
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected void paintTabBackground(Graphics g, int tabPlacement, int tabIndex, int x, int y, int w, int h, boolean isSelected) {}
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected void paintContentBorder(Graphics g, int tabPlacement, int selectedIndex)
/*     */   {
/* 218 */     int width = this.tabPane.getWidth();
/* 219 */     int height = this.tabPane.getHeight();
/* 220 */     Insets insets = this.tabPane.getInsets();
/* 221 */     Insets contentInsets = getContentBorderInsets(tabPlacement);
/*     */     
/* 223 */     int x = insets.left;
/* 224 */     int y = insets.top;
/* 225 */     int w = width - insets.right - insets.left;
/* 226 */     int h = height - insets.top - insets.bottom;
/*     */     
/* 228 */     switch (tabPlacement) {
/*     */     case 2: 
/* 230 */       x += calculateTabAreaWidth(tabPlacement, this.runCount, this.maxTabWidth);
/* 231 */       w -= x - insets.left;
/* 232 */       break;
/*     */     
/*     */     case 4: 
/* 235 */       w -= calculateTabAreaWidth(tabPlacement, this.runCount, this.maxTabWidth);
/* 236 */       break;
/*     */     
/*     */     case 3: 
/* 239 */       h -= y - insets.top + calculateTabAreaHeight(tabPlacement, this.runCount, this.maxTabHeight);
/* 240 */       break;
/*     */     case 1: 
/*     */     default: 
/* 243 */       y += calculateTabAreaHeight(tabPlacement, this.runCount, this.maxTabHeight);
/* 244 */       h -= y - insets.top;
/*     */     }
/*     */     
/* 247 */     Color oldColor = g.getColor();
/*     */     
/* 249 */     Color inBorderClr = new Color(198, 214, 252);
/* 250 */     Color grayBorderClr = new Color(145, 155, 156);
/* 251 */     Color darkShadowClr = new Color(208, 206, 191);
/* 252 */     Color lightShadowClr = new Color(227, 224, 208);
/*     */     
/*     */ 
/*     */ 
/* 256 */     if (tabPlacement == 3) {
/* 257 */       getSkinBorder().draw(g, 0, x, h - contentInsets.bottom, w, contentInsets.bottom);
/*     */       
/*     */ 
/* 260 */       g.setColor(inBorderClr);
/* 261 */       g.drawLine(w - contentInsets.right, y + contentInsets.top - 2, w - contentInsets.right, y + h - contentInsets.bottom - 1);
/* 262 */       g.drawLine(w - contentInsets.right + 1, y + contentInsets.top - 2, w - contentInsets.right + 1, y + h - contentInsets.bottom - 1);
/* 263 */       g.setColor(grayBorderClr);
/* 264 */       g.drawLine(w - contentInsets.right + 2, y + contentInsets.top - 2, w - contentInsets.right + 2, y + h - contentInsets.bottom - 1);
/*     */       
/*     */ 
/* 267 */       g.setColor(grayBorderClr);
/* 268 */       g.drawLine(x + 1, y + contentInsets.top - 2, x + 1, y + h - contentInsets.bottom - 1);
/* 269 */       g.setColor(inBorderClr);
/* 270 */       g.drawLine(x + 2, y + contentInsets.top - 2, x + 2, y + h - contentInsets.bottom - 1);
/* 271 */       g.drawLine(x + 3, y + contentInsets.top - 2, x + 3, y + h - contentInsets.bottom - 1);
/*     */       
/*     */ 
/* 274 */       g.setColor(grayBorderClr);
/* 275 */       g.drawLine(x + 1, y + 1, w - contentInsets.right + 2, y + 1);
/* 276 */       g.setColor(inBorderClr);
/* 277 */       g.drawLine(x + 2, y + 2, w - contentInsets.right, y + 2);
/* 278 */       g.drawLine(x + 2, y + 3, w - contentInsets.right, y + 3);
/*     */     }
/*     */     
/* 281 */     if (tabPlacement == 1) {
/* 282 */       getSkinBorder().draw(g, 0, x, y, w, 5);
/*     */       
/*     */ 
/* 285 */       g.setColor(inBorderClr);
/* 286 */       g.drawLine(w - contentInsets.right, y + contentInsets.top, w - contentInsets.right, y + h - contentInsets.bottom + 1);
/* 287 */       g.drawLine(w - contentInsets.right + 1, y + contentInsets.top, w - contentInsets.right + 1, y + h - contentInsets.bottom + 1);
/* 288 */       g.setColor(grayBorderClr);
/* 289 */       g.drawLine(w - contentInsets.right + 2, y + contentInsets.top, w - contentInsets.right + 2, y + h - contentInsets.bottom + 1);
/*     */       
/*     */ 
/* 292 */       g.setColor(grayBorderClr);
/* 293 */       g.drawLine(x + 1, y + contentInsets.top, x + 1, y + h - contentInsets.bottom + 1);
/* 294 */       g.setColor(inBorderClr);
/* 295 */       g.drawLine(x + 2, y + contentInsets.top, x + 2, y + h - contentInsets.bottom + 1);
/* 296 */       g.drawLine(x + 3, y + contentInsets.top, x + 3, y + h - contentInsets.bottom + 1);
/*     */       
/*     */ 
/* 299 */       g.setColor(inBorderClr);
/* 300 */       g.drawLine(x + contentInsets.left, height - contentInsets.bottom, w - contentInsets.right - 1, height - contentInsets.bottom);
/* 301 */       g.drawLine(x + contentInsets.left, height - contentInsets.bottom + 1, w - contentInsets.right - 1, height - contentInsets.bottom + 1);
/*     */       
/* 303 */       g.setColor(grayBorderClr);
/* 304 */       g.drawLine(x + 1, height - contentInsets.bottom + 2, w - contentInsets.right + 2, height - contentInsets.bottom + 2);
/*     */       
/* 306 */       g.setColor(darkShadowClr);
/* 307 */       g.drawLine(x + 1, height - contentInsets.bottom + 3, w - contentInsets.right + 2, height - contentInsets.bottom + 3);
/* 308 */       g.drawLine(x + 1, height - contentInsets.bottom + 4, w - contentInsets.right + 2, height - contentInsets.bottom + 4);
/*     */       
/* 310 */       g.setColor(lightShadowClr);
/* 311 */       g.drawLine(x + 1, height - contentInsets.bottom + 5, w - contentInsets.right + 2, height - contentInsets.bottom + 5);
/*     */     }
/*     */     
/* 314 */     if (tabPlacement == 4) {
/* 315 */       getSkinBorderVertical().draw(g, 0, w - 5, y, 5, h);
/*     */       
/*     */ 
/* 318 */       g.setColor(grayBorderClr);
/* 319 */       g.drawLine(x + 1, y + contentInsets.top - 2, x + 1, y + h - contentInsets.bottom + 1);
/* 320 */       g.setColor(inBorderClr);
/* 321 */       g.drawLine(x + 2, y + contentInsets.top, x + 2, y + h - contentInsets.bottom);
/* 322 */       g.drawLine(x + 3, y + contentInsets.top, x + 3, y + h - contentInsets.bottom);
/*     */       
/*     */ 
/* 325 */       g.setColor(grayBorderClr);
/* 326 */       g.drawLine(x + 1, y + 1, w - contentInsets.right - 1, y + 1);
/* 327 */       g.setColor(inBorderClr);
/* 328 */       g.drawLine(x + 2, y + 2, w - contentInsets.right - 1, y + 2);
/* 329 */       g.drawLine(x + 2, y + 3, w - contentInsets.right - 1, y + 3);
/*     */       
/*     */ 
/* 332 */       g.setColor(inBorderClr);
/* 333 */       g.drawLine(x + 2, height - contentInsets.bottom, w - contentInsets.right - 1, height - contentInsets.bottom);
/* 334 */       g.drawLine(x + 2, height - contentInsets.bottom + 1, w - contentInsets.right - 1, height - contentInsets.bottom + 1);
/*     */       
/* 336 */       g.setColor(grayBorderClr);
/* 337 */       g.drawLine(x + 1, height - contentInsets.bottom + 2, w - contentInsets.right - 1, height - contentInsets.bottom + 2);
/*     */       
/* 339 */       g.setColor(darkShadowClr);
/* 340 */       g.drawLine(x + 1, height - contentInsets.bottom + 3, w - contentInsets.right - 1, height - contentInsets.bottom + 3);
/* 341 */       g.drawLine(x + 1, height - contentInsets.bottom + 4, w - contentInsets.right - 1, height - contentInsets.bottom + 4);
/*     */       
/* 343 */       g.setColor(lightShadowClr);
/* 344 */       g.drawLine(x + 1, height - contentInsets.bottom + 5, w - contentInsets.right - 1, height - contentInsets.bottom + 5);
/*     */     }
/*     */     
/* 347 */     if (tabPlacement == 2) {
/* 348 */       getSkinBorderVertical().draw(g, 0, x, y, 5, h);
/*     */       
/*     */ 
/* 351 */       g.setColor(inBorderClr);
/* 352 */       g.drawLine(width - contentInsets.right, y + contentInsets.top - 1, width - contentInsets.right, y + h - contentInsets.bottom);
/* 353 */       g.drawLine(width - contentInsets.right + 1, y + contentInsets.top - 2, width - contentInsets.right + 1, y + h - contentInsets.bottom + 1);
/* 354 */       g.setColor(grayBorderClr);
/* 355 */       g.drawLine(width - contentInsets.right + 2, y + contentInsets.top - 3, width - contentInsets.right + 2, y + h - contentInsets.bottom + 2);
/*     */       
/*     */ 
/* 358 */       g.setColor(grayBorderClr);
/* 359 */       g.drawLine(x + contentInsets.left, y + 1, width - contentInsets.right + 1, y + 1);
/* 360 */       g.setColor(inBorderClr);
/* 361 */       g.drawLine(x + contentInsets.left, y + 2, width - contentInsets.right, y + 2);
/* 362 */       g.drawLine(x + contentInsets.left, y + 3, width - contentInsets.right, y + 3);
/*     */       
/*     */ 
/* 365 */       g.setColor(inBorderClr);
/* 366 */       g.drawLine(x + contentInsets.left, height - contentInsets.bottom, width - contentInsets.right, height - contentInsets.bottom);
/* 367 */       g.drawLine(x + contentInsets.left, height - contentInsets.bottom + 1, width - contentInsets.right, height - contentInsets.bottom + 1);
/*     */       
/* 369 */       g.setColor(grayBorderClr);
/* 370 */       g.drawLine(x + contentInsets.left, height - contentInsets.bottom + 2, width - contentInsets.right + 2, height - contentInsets.bottom + 2);
/*     */       
/* 372 */       g.setColor(darkShadowClr);
/* 373 */       g.drawLine(x + contentInsets.left, height - contentInsets.bottom + 3, width - contentInsets.right + 1, height - contentInsets.bottom + 3);
/* 374 */       g.drawLine(x + contentInsets.left, height - contentInsets.bottom + 4, width - contentInsets.right + 1, height - contentInsets.bottom + 4);
/*     */       
/* 376 */       g.setColor(lightShadowClr);
/* 377 */       g.drawLine(x + contentInsets.left, height - contentInsets.bottom + 5, width - contentInsets.right + 1, height - contentInsets.bottom + 5);
/*     */     }
/*     */     
/*     */ 
/*     */ 
/* 382 */     g.setColor(oldColor);
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected void paintTabBorder(Graphics g, int tabPlacement, int tabIndex, int x, int y, int w, int h, boolean isSelected)
/*     */   {
/* 475 */     int index = this.indexModel.getIndexForState(this.tabPane.isEnabledAt(tabIndex), this.rollover == tabIndex, isSelected);
/*     */     
/*     */ 
/* 478 */     switch (tabPlacement) {
/*     */     case 2: 
/* 480 */       getSkinLeft().draw(g, index, x, y, w, h - 1);
/*     */       
/* 482 */       break;
/*     */     
/*     */     case 4: 
/* 485 */       getSkinRight().draw(g, index, x, y, w, h - 1);
/*     */       
/* 487 */       break;
/*     */     
/*     */     case 3: 
/* 490 */       getSkinBottom().draw(g, index, x, y, w, h);
/*     */       
/* 492 */       break;
/*     */     case 1: 
/*     */     default: 
/* 495 */       getSkinTop().draw(g, index, x, y, w, h);
/*     */     }
/*     */     
/*     */   }
/*     */   
/*     */   protected void paintText(Graphics g, int tabPlacement, Font font, FontMetrics metrics, int tabIndex, String title, Rectangle textRect, boolean isSelected)
/*     */   {
/* 502 */     int yOffset = 0;
/*     */     
/* 504 */     if ((tabPlacement == 1) && (isSelected)) {
/* 505 */       yOffset = 1;
/*     */     }
/*     */     
/* 508 */     if (tabPlacement == 3) {
/* 509 */       yOffset = isSelected ? -2 : -1;
/*     */     }
/*     */     
/* 512 */     g.setFont(font);
/*     */     
/* 514 */     View v = getTextViewForTab(tabIndex);
/*     */     
/* 516 */     if (v != null)
/*     */     {
/* 518 */       textRect.y += yOffset;
/* 519 */       v.paint(g, textRect);
/*     */     }
/*     */     else {
/* 522 */       int mnemIndex = this.tabPane.getDisplayedMnemonicIndexAt(tabIndex);
/*     */       
/* 524 */       if ((this.tabPane.isEnabled()) && (this.tabPane.isEnabledAt(tabIndex))) {
/* 525 */         g.setColor(this.tabPane.getForegroundAt(tabIndex));
/* 526 */         BasicGraphicsUtils.drawStringUnderlineCharAt(g, title, mnemIndex, textRect.x, textRect.y + metrics.getAscent() + yOffset);
/*     */       }
/*     */       else
/*     */       {
/* 530 */         g.setColor(this.tabPane.getBackgroundAt(tabIndex).brighter());
/* 531 */         BasicGraphicsUtils.drawStringUnderlineCharAt(g, title, mnemIndex, textRect.x, textRect.y + metrics.getAscent());
/*     */         
/* 533 */         g.setColor(this.tabPane.getBackgroundAt(tabIndex).darker());
/* 534 */         BasicGraphicsUtils.drawStringUnderlineCharAt(g, title, mnemIndex, textRect.x - 1, textRect.y + metrics.getAscent() - 1);
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */   public void paint(Graphics g, JComponent c)
/*     */   {
/* 542 */     int width = this.tabPane.getWidth();
/* 543 */     int height = this.tabPane.getHeight();
/* 544 */     Insets insets = this.tabPane.getInsets();
/*     */     
/* 546 */     int x = insets.left;
/* 547 */     int y = insets.top;
/* 548 */     int w = width - insets.right - insets.left;
/* 549 */     int h = height - insets.top - insets.bottom;
/*     */     
/* 551 */     int tabPlacement = this.tabPane.getTabPlacement();
/* 552 */     Insets contentInsets = getContentBorderInsets(tabPlacement);
/*     */     
/* 554 */     if (tabPlacement == 3) {
/* 555 */       Color oldColor = g.getColor();
/* 556 */       Color bg = LiquidLookAndFeel.getBackgroundColor();
/*     */       
/* 558 */       if (c.isOpaque()) {
/* 559 */         g.setColor(bg);
/* 560 */         g.fillRect(0, 0, c.getWidth(), c.getHeight());
/*     */       }
/*     */       
/* 563 */       if (LiquidLookAndFeel.areStipplesUsed()) {
/* 564 */         Colors.drawStipples(g, c, bg);
/*     */       }
/* 566 */       g.setColor(oldColor);
/*     */     }
/*     */     
/* 569 */     if ((tabPlacement == 1) && 
/* 570 */       (LiquidLookAndFeel.areStipplesUsed())) {
/* 571 */       c.setOpaque(false);
/*     */     }
/*     */     
/* 574 */     super.paint(g, c);
/*     */   }
/*     */   
/*     */   public void update(Graphics g, JComponent c) {
/* 578 */     paint(g, c);
/*     */   }
/*     */   
/*     */   protected int getTabLabelShiftX(int tabPlacement, int tabIndex, boolean isSelected)
/*     */   {
/* 583 */     Rectangle tabRect = this.rects[tabIndex];
/* 584 */     int nudge = 0;
/*     */     
/* 586 */     switch (tabPlacement) {
/*     */     case 2: 
/* 588 */       nudge = isSelected ? -1 : 1;
/*     */       
/* 590 */       break;
/*     */     
/*     */     case 4: 
/* 593 */       nudge = isSelected ? 1 : -1;
/*     */       
/* 595 */       break;
/*     */     case 1: 
/*     */     case 3: 
/*     */     default: 
/* 599 */       nudge = 0;
/*     */     }
/*     */     
/* 602 */     return nudge;
/*     */   }
/*     */   
/*     */   protected int getTabLabelShiftY(int tabPlacement, int tabIndex, boolean isSelected)
/*     */   {
/* 607 */     Rectangle tabRect = this.rects[tabIndex];
/* 608 */     int nudge = 0;
/*     */     
/* 610 */     switch (tabPlacement) {
/*     */     case 3: 
/* 612 */       nudge = isSelected ? 1 : -1;
/*     */       
/* 614 */       break;
/*     */     
/*     */     case 2: 
/*     */     case 4: 
/* 618 */       nudge = tabRect.height % 2;
/*     */       
/* 620 */       break;
/*     */     case 1: 
/*     */     default: 
/* 623 */       nudge = isSelected ? -1 : 1;
/*     */     }
/*     */     
/* 626 */     return nudge;
/*     */   }
/*     */   
/*     */   protected Insets getContentBorderInsets(int tabPlacement) {
/* 630 */     Insets contentBorderInsets = new Insets(4, 4, 4, 4);
/*     */     
/* 632 */     switch (tabPlacement)
/*     */     {
/*     */     case 1: 
/* 635 */       contentBorderInsets.top = 5;
/* 636 */       contentBorderInsets.bottom = 6;
/* 637 */       break;
/*     */     case 3: 
/* 639 */       contentBorderInsets.bottom = 6;
/* 640 */       break;
/*     */     case 2: 
/* 642 */       contentBorderInsets.left = 5;
/* 643 */       contentBorderInsets.bottom = 6;
/* 644 */       break;
/*     */     case 4: 
/* 646 */       contentBorderInsets.right = 5;
/* 647 */       contentBorderInsets.bottom = 6;
/*     */     }
/*     */     
/*     */     
/* 651 */     return contentBorderInsets;
/*     */   }
/*     */   
/*     */   public Skin getSkinTop() {
/* 655 */     if (skinTop == null) {
/* 656 */       skinTop = new Skin("tabtop.png", 4, 7, 6, 7, 2);
/*     */     }
/*     */     
/* 659 */     return skinTop;
/*     */   }
/*     */   
/*     */   public Skin getSkinLeft() {
/* 663 */     if (skinLeft == null) {
/* 664 */       skinLeft = new Skin("tableft.png", 4, 6, 7, 2, 7);
/*     */     }
/*     */     
/* 667 */     return skinLeft;
/*     */   }
/*     */   
/*     */   public Skin getSkinRight() {
/* 671 */     if (skinRight == null) {
/* 672 */       skinRight = new Skin("tabright.png", 4, 2, 7, 6, 7);
/*     */     }
/*     */     
/* 675 */     return skinRight;
/*     */   }
/*     */   
/*     */   public Skin getSkinBottom() {
/* 679 */     if (skinBottom == null)
/*     */     {
/* 681 */       skinBottom = new Skin("tabbottom.png", 4, 6, 7, 6, 2);
/*     */     }
/*     */     
/* 684 */     return skinBottom;
/*     */   }
/*     */   
/*     */   public Skin getSkinBorder() {
/* 688 */     if (skinBorder == null) {
/* 689 */       skinBorder = new Skin("tabborderh.png", 1, 5, 2, 5, 2);
/*     */     }
/*     */     
/* 692 */     return skinBorder;
/*     */   }
/*     */   
/*     */   public Skin getSkinBorderVertical() {
/* 696 */     if (skinBorderVertical == null) {
/* 697 */       skinBorderVertical = new Skin("tabborderv.png", 1, 2, 5, 2, 5);
/*     */     }
/*     */     
/* 700 */     return skinBorderVertical;
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
/*     */   public Skin getSkinBorderBottom()
/*     */   {
/* 728 */     if (skinBorderBottom == null) {
/* 729 */       skinBorderBottom = new Skin("tabborderbottom.png", 1, 5, 0, 5, 0);
/*     */     }
/*     */     
/* 732 */     return skinBorderBottom;
/*     */   }
/*     */   
/*     */   private class LiquidScrollableTabButton extends LiquidScrollButton implements UIResource {
/*     */     public LiquidScrollableTabButton(int direction) {
/* 737 */       super(10, true);
/* 738 */       setEnabled(false);
/*     */     }
/*     */   }
/*     */   
/*     */   public class MyMouseHandler implements MouseListener, MouseMotionListener { public MyMouseHandler() {}
/*     */     
/* 744 */     public void mousePressed(MouseEvent e) { if (!LiquidTabbedPaneUI.this.tabPane.isEnabled()) {
/* 745 */         return;
/*     */       }
/*     */       
/* 748 */       int tabIndex = LiquidTabbedPaneUI.this.getTabAtLocation(e.getX(), e.getY());
/*     */       
/* 750 */       if ((tabIndex >= 0) && (LiquidTabbedPaneUI.this.tabPane.isEnabledAt(tabIndex))) {
/* 751 */         if (tabIndex == LiquidTabbedPaneUI.this.tabPane.getSelectedIndex()) {
/* 752 */           if (LiquidTabbedPaneUI.this.tabPane.isRequestFocusEnabled()) {
/* 753 */             LiquidTabbedPaneUI.this.tabPane.requestFocus();
/* 754 */             LiquidTabbedPaneUI.this.tabPane.repaint(LiquidTabbedPaneUI.this.getTabBounds(LiquidTabbedPaneUI.this.tabPane, tabIndex));
/*     */           }
/*     */         } else {
/* 757 */           LiquidTabbedPaneUI.this.tabPane.setSelectedIndex(tabIndex);
/*     */         }
/*     */       }
/*     */     }
/*     */     
/*     */     public void mouseEntered(MouseEvent e) {}
/*     */     
/*     */     public void mouseExited(MouseEvent e)
/*     */     {
/* 766 */       if ((LiquidTabbedPaneUI.this.rollover != -1) && (LiquidTabbedPaneUI.this.rollover < LiquidTabbedPaneUI.this.tabPane.getTabCount())) {
/* 767 */         int oldRollover = LiquidTabbedPaneUI.this.rollover;
/* 768 */         LiquidTabbedPaneUI.this.rollover = -1;
/* 769 */         LiquidTabbedPaneUI.this.tabPane.repaint(LiquidTabbedPaneUI.this.getTabBounds(LiquidTabbedPaneUI.this.tabPane, oldRollover));
/*     */       }
/*     */     }
/*     */     
/*     */ 
/*     */     public void mouseClicked(MouseEvent e) {}
/*     */     
/*     */ 
/*     */     public void mouseReleased(MouseEvent e) {}
/*     */     
/*     */     public void mouseDragged(MouseEvent e) {}
/*     */     
/*     */     public void mouseMoved(MouseEvent e)
/*     */     {
/* 783 */       if (LiquidTabbedPaneUI.this.tabPane == null) {
/* 784 */         return;
/*     */       }
/*     */       
/* 787 */       if (!LiquidTabbedPaneUI.this.tabPane.isEnabled()) {
/* 788 */         return;
/*     */       }
/*     */       
/* 791 */       int tabIndex = LiquidTabbedPaneUI.this.getTabAtLocation(e.getX(), e.getY());
/* 792 */       if ((tabIndex >= 0) && (tabIndex != LiquidTabbedPaneUI.this.rollover) && (LiquidTabbedPaneUI.this.rollover != -1))
/*     */       {
/* 794 */         if ((LiquidTabbedPaneUI.this.rollover >= 0) && (LiquidTabbedPaneUI.this.rollover < LiquidTabbedPaneUI.this.tabPane.getTabCount())) {
/* 795 */           LiquidTabbedPaneUI.this.tabPane.repaint(LiquidTabbedPaneUI.this.getTabBounds(LiquidTabbedPaneUI.this.tabPane, LiquidTabbedPaneUI.this.rollover));
/*     */         }
/*     */       }
/*     */       
/* 799 */       if ((tabIndex >= 0) && (LiquidTabbedPaneUI.this.tabPane.isEnabledAt(tabIndex)) && (tabIndex < LiquidTabbedPaneUI.this.tabPane.getTabCount()))
/*     */       {
/* 801 */         if (tabIndex != LiquidTabbedPaneUI.this.rollover)
/*     */         {
/* 803 */           LiquidTabbedPaneUI.this.rollover = tabIndex;
/* 804 */           LiquidTabbedPaneUI.this.tabPane.repaint(LiquidTabbedPaneUI.this.getTabBounds(LiquidTabbedPaneUI.this.tabPane, tabIndex));
/*     */         }
/* 806 */       } else if ((tabIndex == -1) && (LiquidTabbedPaneUI.this.rollover != -1) && (LiquidTabbedPaneUI.this.rollover < LiquidTabbedPaneUI.this.tabPane.getTabCount())) {
/* 807 */         int oldRollover = LiquidTabbedPaneUI.this.rollover;
/* 808 */         LiquidTabbedPaneUI.this.rollover = -1;
/* 809 */         LiquidTabbedPaneUI.this.tabPane.repaint(LiquidTabbedPaneUI.this.getTabBounds(LiquidTabbedPaneUI.this.tabPane, oldRollover));
/*     */       }
/*     */     }
/*     */   }
/*     */ }


/* Location:              E:\java\java学习\hutubill\lib\all.jar!\com\birosoft\liquid\LiquidTabbedPaneUI.class
 * Java compiler version: 4 (48.0)
 * JD-Core Version:       0.7.1
 */
/*     */ package com.birosoft.liquid;
/*     */ 
/*     */ import com.birosoft.liquid.util.LiquidUtilities;
/*     */ import java.awt.Color;
/*     */ import java.awt.Component;
/*     */ import java.awt.ComponentOrientation;
/*     */ import java.awt.Container;
/*     */ import java.awt.Dimension;
/*     */ import java.awt.Font;
/*     */ import java.awt.FontMetrics;
/*     */ import java.awt.GradientPaint;
/*     */ import java.awt.Graphics;
/*     */ import java.awt.Graphics2D;
/*     */ import java.awt.Insets;
/*     */ import java.awt.LayoutManager;
/*     */ import java.awt.Rectangle;
/*     */ import java.awt.RenderingHints;
/*     */ import java.awt.event.ActionEvent;
/*     */ import java.awt.event.MouseEvent;
/*     */ import java.awt.event.MouseListener;
/*     */ import java.awt.font.FontRenderContext;
/*     */ import java.awt.font.LineMetrics;
/*     */ import javax.accessibility.AccessibleContext;
/*     */ import javax.swing.Action;
/*     */ import javax.swing.ButtonModel;
/*     */ import javax.swing.Icon;
/*     */ import javax.swing.JButton;
/*     */ import javax.swing.JInternalFrame;
/*     */ import javax.swing.JMenuBar;
/*     */ import javax.swing.UIManager;
/*     */ import javax.swing.plaf.basic.BasicInternalFrameTitlePane;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class LiquidInternalFrameTitlePane
/*     */   extends BasicInternalFrameTitlePane
/*     */   implements LayoutManager
/*     */ {
/*     */   static LiquidWindowButtonUI iconButtonUI;
/*     */   static LiquidWindowButtonUI maxButtonUI;
/*     */   static LiquidWindowButtonUI closeButtonUI;
/*  53 */   protected boolean isPalette = false;
/*     */   
/*     */ 
/*     */   protected Icon paletteCloseIcon;
/*     */   
/*     */   protected int paletteTitleHeight;
/*     */   
/*  60 */   Color normalTitleColor = Color.white;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*  65 */   Color shadowColor = new Color(10, 24, 131);
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*  70 */   Color disabledTitleColor = new Color(64, 63, 63);
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected int frameTitleHeight;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private int buttonsWidth;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public LiquidInternalFrameTitlePane(JInternalFrame frame)
/*     */   {
/*  90 */     super(frame);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected void installDefaults()
/*     */   {
/* 101 */     super.installDefaults();
/* 102 */     this.frameTitleHeight = UIManager.getInt("InternalFrame.frameTitleHeight");
/* 103 */     Font font = LiquidLookAndFeel.winDecoPanther ? UIManager.getFont("InternalFrame.pantherTitleFont") : this.isPalette ? UIManager.getFont("InternalFrame.paletteTitleFont") : UIManager.getFont("InternalFrame.titleFont");
/*     */     
/*     */ 
/*     */ 
/* 107 */     setFont(font);
/* 108 */     this.paletteTitleHeight = UIManager.getInt("InternalFrame.paletteTitleHeight");
/*     */     
/* 110 */     this.paletteCloseIcon = UIManager.getIcon("InternalFrame.paletteCloseIcon");
/*     */   }
/*     */   
/*     */ 
/*     */   protected void uninstallDefaults()
/*     */   {
/* 116 */     super.uninstallDefaults();
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
/*     */   protected void paintTitleBackground(Graphics g) {}
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void paintComponent(Graphics g)
/*     */   {
/* 138 */     boolean leftToRight = this.frame.getComponentOrientation().isLeftToRight();
/* 139 */     boolean isSelected = this.frame.isSelected();
/*     */     
/* 141 */     Insets insets = this.frame.getInsets();
/* 142 */     int width = getWidth();
/* 143 */     int height = getHeight();
/*     */     
/* 145 */     Color foreground = LiquidLookAndFeel.getWindowTitleInactiveForeground();
/*     */     
/* 147 */     Graphics2D g2 = (Graphics2D)g;
/* 148 */     Object oldAntiAliasingValue = g2.getRenderingHint(RenderingHints.KEY_ANTIALIASING);
/* 149 */     g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
/*     */     
/* 151 */     if (LiquidLookAndFeel.winDecoPanther) {
/* 152 */       drawPantherCaption(g, isSelected, width, height);
/*     */     } else {
/* 154 */       drawLiquidCaption(g, isSelected, width, height);
/*     */     }
/*     */     
/* 157 */     int xOffset = (leftToRight) && (!LiquidLookAndFeel.winDecoPanther) ? 2 : width - 2;
/* 158 */     int xOvalOffset = 8;
/*     */     
/*     */ 
/* 161 */     Icon icon = null;
/* 162 */     if (LiquidLookAndFeel.winDecoPanther) {
/* 163 */       icon = UIManager.getIcon("InternalFrame.pantherFrameIcon");
/*     */     } else {
/* 165 */       icon = this.frame.getFrameIcon();
/*     */     }
/*     */     
/* 168 */     if (icon != null) {
/* 169 */       if ((!leftToRight) || (LiquidLookAndFeel.winDecoPanther)) {
/* 170 */         xOffset -= icon.getIconWidth();
/*     */       }
/*     */       
/* 173 */       int iconY = height / 2 - icon.getIconHeight() / 2;
/* 174 */       icon.paintIcon(this.frame, g, xOffset, iconY);
/* 175 */       xOffset += ((leftToRight) && (!LiquidLookAndFeel.winDecoPanther) ? icon.getIconWidth() + 2 : -2);
/*     */     }
/*     */     
/*     */ 
/* 179 */     xOffset += ((leftToRight) && (!LiquidLookAndFeel.winDecoPanther) ? xOvalOffset : 0);
/*     */     
/* 181 */     String frameTitle = this.frame.getTitle();
/* 182 */     int titleLength = 0;
/*     */     
/* 184 */     if (frameTitle != null) {
/* 185 */       Font f = getFont();
/* 186 */       g.setFont(f);
/* 187 */       int osFontOffset = f.getFamily().equals(LiquidLookAndFeel.fontName) ? 2 : 0;
/*     */       
/* 189 */       FontMetrics fm = g.getFontMetrics();
/*     */       
/* 191 */       int titleW = 0;
/*     */       
/* 193 */       Rectangle r = new Rectangle(0, 0, 0, 0);
/* 194 */       if ((leftToRight) && (!LiquidLookAndFeel.winDecoPanther)) {
/* 195 */         if (this.frame.isIconifiable()) {
/* 196 */           r = this.iconButton.getBounds();
/* 197 */         } else if (this.frame.isMaximizable()) {
/* 198 */           r = this.maxButton.getBounds();
/* 199 */         } else if (this.frame.isClosable()) {
/* 200 */           r = this.closeButton.getBounds();
/*     */         }
/*     */         
/* 203 */         if (r.x == 0) {
/* 204 */           r.x = (this.frame.getWidth() - this.frame.getInsets().right);
/*     */         }
/*     */         
/* 207 */         xOffset += (this.menuBar != null ? this.menuBar.getX() + this.menuBar.getWidth() + 2 : 2);
/* 208 */         titleW = r.x - xOffset - (!this.isPalette ? xOvalOffset : 0);
/*     */       } else {
/* 210 */         if (this.frame.isMaximizable()) {
/* 211 */           r = this.maxButton.getBounds();
/* 212 */         } else if (this.frame.isIconifiable()) {
/* 213 */           r = this.iconButton.getBounds();
/* 214 */         } else if (this.frame.isClosable()) {
/* 215 */           r = this.closeButton.getBounds();
/*     */         }
/*     */         
/* 218 */         Rectangle menu = new Rectangle(0, 0, 0, 0);
/* 219 */         menu.x = (this.frame.getWidth() - insets.right);
/*     */         
/* 221 */         xOffset = !this.isPalette ? r.x + r.width + 2 : 10;
/* 222 */         titleW = menu.x - xOffset;
/*     */       }
/*     */       
/*     */ 
/* 226 */       int startTitleLength = fm.stringWidth(frameTitle);
/* 227 */       frameTitle = LiquidUtilities.clipStringIfNecessary(fm, frameTitle, titleW);
/* 228 */       titleLength = fm.stringWidth(frameTitle);
/* 229 */       if ((titleLength == startTitleLength) && (!this.isPalette)) {
/* 230 */         xOffset += (titleW - titleLength) / 2;
/*     */       }
/*     */       
/* 233 */       FontRenderContext frc = g2.getFontRenderContext();
/* 234 */       LineMetrics lm = f.getLineMetrics(frameTitle, frc);
/*     */       
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 250 */       int yOffset = (height - Math.round(lm.getHeight())) / 2 + Math.round(lm.getAscent()) + osFontOffset;
/* 251 */       int endOffset = 19;
/*     */       
/*     */ 
/*     */ 
/*     */ 
/* 256 */       if (this.isPalette) {
/* 257 */         if (isSelected) {
/* 258 */           g.setColor(Color.white);
/*     */         }
/* 260 */         else if (!LiquidLookAndFeel.winDecoPanther) {
/* 261 */           g.setColor(Color.black);
/*     */         } else {
/* 263 */           g.setColor(Color.gray);
/*     */         }
/*     */         
/*     */ 
/* 267 */         if (!leftToRight) {
/* 268 */           xOffset -= titleLength;
/*     */         }
/*     */         
/*     */ 
/* 272 */         xOffset -= xOvalOffset;
/*     */         
/* 274 */         g2.drawString(frameTitle, xOffset, yOffset);
/* 275 */         xOffset += (leftToRight ? titleLength + 2 : -2);
/*     */ 
/*     */       }
/*     */       else
/*     */       {
/* 280 */         if (!leftToRight) {
/* 281 */           xOffset -= titleLength;
/*     */         }
/*     */         
/*     */ 
/*     */ 
/* 286 */         if (isSelected)
/*     */         {
/*     */ 
/* 289 */           if (!LiquidLookAndFeel.winDecoPanther) {
/* 290 */             GradientPaint grad = new GradientPaint(xOffset + titleLength / 2, yOffset - 15, new Color(60, 144, 233), xOffset + titleLength / 2, endOffset, new Color(102, 186, 255));
/*     */             
/*     */ 
/*     */ 
/*     */ 
/* 295 */             g2.setPaint(grad);
/* 296 */             g2.fillRoundRect(xOffset - 8, yOffset - 15, titleLength + 15, endOffset, 18, 18);
/*     */             
/* 298 */             g.setColor(new Color(0, 78, 167));
/* 299 */             g2.drawRoundRect(xOffset - 8, yOffset - 15, titleLength + 15, endOffset, 18, 18);
/*     */           }
/*     */           
/*     */ 
/* 303 */           if (!LiquidLookAndFeel.winDecoPanther) {
/* 304 */             g.setColor(this.shadowColor);
/* 305 */             g.drawString(frameTitle, xOffset + 1, yOffset);
/* 306 */             g.setColor(this.normalTitleColor);
/*     */           } else {
/* 308 */             g.setColor(Color.black);
/*     */           }
/*     */           
/* 311 */           g.drawString(frameTitle, xOffset, yOffset - 1);
/*     */           
/* 313 */           xOffset += (leftToRight ? titleLength + 2 : -2);
/*     */ 
/*     */         }
/*     */         else
/*     */         {
/* 318 */           if (!LiquidLookAndFeel.winDecoPanther) {
/* 319 */             GradientPaint grad = new GradientPaint(xOffset + titleLength / 2, yOffset - 15, new Color(191, 211, 233), xOffset + titleLength / 2, endOffset, new Color(233, 253, 255));
/*     */             
/*     */ 
/*     */ 
/*     */ 
/* 324 */             g2.setPaint(grad);
/* 325 */             g2.fillRoundRect(xOffset - 8, yOffset - 15, titleLength + 15, endOffset, 18, 18);
/*     */             
/* 327 */             g.setColor(new Color(125, 145, 167));
/* 328 */             g2.drawRoundRect(xOffset - 8, yOffset - 15, titleLength + 15, endOffset, 18, 18);
/*     */           }
/*     */           
/*     */ 
/*     */ 
/* 333 */           if (!LiquidLookAndFeel.winDecoPanther) {
/* 334 */             g.setColor(Color.black);
/*     */           } else {
/* 336 */             g.setColor(Color.GRAY);
/*     */           }
/*     */           
/* 339 */           g.drawString(frameTitle, xOffset, yOffset - 1);
/*     */           
/* 341 */           xOffset += (leftToRight ? titleLength + 2 : -2);
/*     */         }
/*     */       }
/*     */     }
/*     */     
/* 346 */     g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, oldAntiAliasingValue);
/*     */   }
/*     */   
/*     */   private void drawLiquidCaption(Graphics g, boolean isSelected, int w, int h) {
/* 350 */     Color c = isSelected ? new Color(62, 145, 235) : new Color(175, 214, 255);
/* 351 */     g.setColor(c);
/* 352 */     g.fillRect(0, 0, w, h - 1);
/* 353 */     c = isSelected ? new Color(94, 172, 255) : new Color(226, 240, 255);
/* 354 */     g.setColor(c);
/* 355 */     g.drawLine(0, 0, w, 0);
/* 356 */     c = isSelected ? new Color(60, 141, 228) : new Color(170, 207, 247);
/* 357 */     g.setColor(c);
/* 358 */     g.drawLine(0, 1, w, 1);
/*     */     
/* 360 */     for (int i = 4; i < h - 1; i += 4) {
/* 361 */       c = isSelected ? new Color(59, 138, 223) : new Color(166, 203, 242);
/* 362 */       g.setColor(c);
/* 363 */       g.drawLine(0, i, w, i);
/* 364 */       c = isSelected ? new Color(60, 141, 228) : new Color(170, 207, 247);
/* 365 */       g.setColor(c);
/* 366 */       g.drawLine(0, i + 1, w, i + 1);
/*     */     }
/*     */     
/* 369 */     c = isSelected ? new Color(47, 111, 180) : new Color(135, 164, 196);
/* 370 */     g.setColor(c);
/* 371 */     g.drawLine(0, h - 1, w, h - 1);
/*     */   }
/*     */   
/*     */   private void drawPantherCaption(Graphics g, boolean isSelected, int w, int h) {
/* 375 */     Graphics2D g2 = (Graphics2D)g;
/* 376 */     GradientPaint grad = isSelected ? new GradientPaint(0.0F, 0.0F, new Color(238, 238, 238), 0.0F, h - 1, new Color(192, 192, 192)) : new GradientPaint(0.0F, 0.0F, new Color(230, 230, 230), 0.0F, h - 1, new Color(202, 202, 202));
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 382 */     g2.setPaint(grad);
/* 383 */     g2.fillRect(0, 0, w, h - 1);
/*     */     
/* 385 */     g2.setColor(new Color(198, 198, 198));
/* 386 */     g2.drawLine(0, 0, w - 1, 0);
/* 387 */     g2.setColor(Color.WHITE);
/* 388 */     g2.drawLine(0, 1, w - 1, 1);
/* 389 */     g2.setColor(new Color(147, 147, 147));
/* 390 */     g2.drawLine(0, h - 1, w, h - 1);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected LayoutManager createLayout()
/*     */   {
/* 400 */     return this;
/*     */   }
/*     */   
/*     */   protected void addSubComponents()
/*     */   {
/* 405 */     add(this.iconButton);
/* 406 */     add(this.maxButton);
/* 407 */     add(this.closeButton);
/*     */     
/* 409 */     if (this.menuBar != null) {}
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected void setButtonIcons() {}
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   protected void createButtons()
/*     */   {
/* 423 */     if (iconButtonUI == null) {
/* 424 */       iconButtonUI = LiquidWindowButtonUI.createButtonUIForType(2);
/* 425 */       maxButtonUI = LiquidWindowButtonUI.createButtonUIForType(1);
/* 426 */       closeButtonUI = LiquidWindowButtonUI.createButtonUIForType(0);
/*     */     }
/*     */     
/* 429 */     this.iconButton = new SpecialUIButton(iconButtonUI, this.frame);
/* 430 */     this.iconButton.addActionListener(this.iconifyAction);
/* 431 */     this.iconButton.setRolloverEnabled(true);
/* 432 */     this.iconButton.addMouseListener(new RolloverListener(this.iconButton, this.iconifyAction));
/*     */     
/*     */ 
/* 435 */     this.maxButton = new SpecialUIButton(maxButtonUI, this.frame);
/* 436 */     this.maxButton.addActionListener(this.maximizeAction);
/* 437 */     this.maxButton.setRolloverEnabled(true);
/* 438 */     this.maxButton.addMouseListener(new RolloverListener(this.maxButton, this.maximizeAction));
/*     */     
/*     */ 
/* 441 */     this.closeButton = new SpecialUIButton(closeButtonUI, this.frame);
/* 442 */     this.closeButton.addActionListener(this.closeAction);
/* 443 */     this.closeButton.setRolloverEnabled(true);
/* 444 */     this.closeButton.addMouseListener(new RolloverListener(this.closeButton, this.closeAction));
/*     */     
/*     */ 
/* 447 */     this.iconButton.getAccessibleContext().setAccessibleName(UIManager.getString("InternalFrameTitlePane.iconifyButtonAccessibleName"));
/*     */     
/*     */ 
/* 450 */     this.maxButton.getAccessibleContext().setAccessibleName(UIManager.getString("InternalFrameTitlePane.maximizeButtonAccessibleName"));
/*     */     
/*     */ 
/* 453 */     this.closeButton.getAccessibleContext().setAccessibleName(UIManager.getString("InternalFrameTitlePane.closeButtonAccessibleName"));
/*     */     
/*     */ 
/* 456 */     if (this.frame.isSelected()) {
/* 457 */       activate();
/*     */     } else {
/* 459 */       deactivate();
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void paintPalette(Graphics g) {}
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setPalette(boolean b)
/*     */   {
/* 474 */     this.isPalette = b;
/*     */     
/* 476 */     if (this.isPalette) {
/* 477 */       this.closeButton.setIcon(this.paletteCloseIcon);
/*     */       
/* 479 */       if (this.frame.isMaximizable()) {
/* 480 */         remove(this.maxButton);
/*     */       }
/*     */       
/* 483 */       if (this.frame.isIconifiable()) {
/* 484 */         remove(this.iconButton);
/*     */       }
/*     */     } else {
/* 487 */       this.closeButton.setIcon(this.closeIcon);
/*     */       
/* 489 */       if (this.frame.isMaximizable()) {
/* 490 */         add(this.maxButton);
/*     */       }
/*     */       
/* 493 */       if (this.frame.isIconifiable()) {
/* 494 */         add(this.iconButton);
/*     */       }
/*     */     }
/*     */     
/* 498 */     revalidate();
/* 499 */     repaint();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void addLayoutComponent(String name, Component c) {}
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void removeLayoutComponent(Component c) {}
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public Dimension preferredLayoutSize(Container c)
/*     */   {
/* 526 */     return getPreferredSize(c);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public Dimension getPreferredSize(Container c)
/*     */   {
/* 536 */     return new Dimension(c.getSize().width, this.isPalette ? this.paletteTitleHeight : this.frameTitleHeight);
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
/*     */   public Dimension getMinimumSize()
/*     */   {
/* 551 */     return new Dimension(70, 25);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public Dimension minimumLayoutSize(Container c)
/*     */   {
/* 562 */     return preferredLayoutSize(c);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void layoutContainer(Container c)
/*     */   {
/* 571 */     boolean leftToRight = this.frame.getComponentOrientation().isLeftToRight();
/*     */     
/* 573 */     if (LiquidLookAndFeel.winDecoPanther) {
/* 574 */       leftToRight = !leftToRight;
/*     */     }
/*     */     
/* 577 */     int buttonHeight = this.closeButton.getPreferredSize().height;
/*     */     
/* 579 */     int w = getWidth();
/* 580 */     int x = leftToRight ? w : 0;
/* 581 */     int y = (getHeight() - buttonHeight) / 2 + 1;
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 587 */     int buttonWidth = this.closeButton.getPreferredSize().width;
/*     */     
/* 589 */     if (this.frame.isClosable()) {
/* 590 */       if (this.isPalette) {
/* 591 */         int spacing = LiquidLookAndFeel.winDecoPanther ? 7 : 0;
/* 592 */         x += (leftToRight ? -spacing - buttonWidth : spacing);
/* 593 */         this.closeButton.setBounds(x, y, buttonWidth, buttonHeight);
/*     */         
/* 595 */         if (!leftToRight) {
/* 596 */           x += buttonWidth;
/*     */         }
/*     */       } else {
/* 599 */         int spacing = LiquidLookAndFeel.winDecoPanther ? 7 : 0;
/* 600 */         x += (leftToRight ? -spacing - buttonWidth : spacing);
/* 601 */         this.closeButton.setBounds(x, y, buttonWidth, buttonHeight);
/*     */         
/* 603 */         if (!leftToRight) {
/* 604 */           x += buttonWidth;
/*     */         }
/*     */       }
/*     */     }
/*     */     
/* 609 */     if ((LiquidLookAndFeel.winDecoPanther) && 
/* 610 */       (this.frame.isIconifiable()) && (!this.isPalette))
/*     */     {
/* 612 */       int spacing = 7;
/*     */       
/* 614 */       x += (leftToRight ? -spacing - buttonWidth : spacing);
/* 615 */       this.iconButton.setBounds(x, y, buttonWidth, buttonHeight);
/*     */       
/* 617 */       if (!leftToRight) {
/* 618 */         x += buttonWidth;
/*     */       }
/*     */     }
/*     */     
/*     */ 
/* 623 */     if ((this.frame.isMaximizable()) && (!this.isPalette) && (
/* 624 */       (!LiquidLookAndFeel.winDecoPanther) || (!this.frame.isIcon()))) {
/* 625 */       int spacing = LiquidLookAndFeel.winDecoPanther ? 7 : 0;
/*     */       
/* 627 */       x += (leftToRight ? -spacing - buttonWidth : spacing);
/* 628 */       this.maxButton.setBounds(x, y, buttonWidth, buttonHeight);
/*     */       
/* 630 */       if (!leftToRight) {
/* 631 */         x += buttonWidth;
/*     */       }
/*     */     }
/*     */     
/*     */ 
/* 636 */     if ((!LiquidLookAndFeel.winDecoPanther) && 
/* 637 */       (this.frame.isIconifiable()) && (!this.isPalette))
/*     */     {
/* 639 */       int spacing = 0;
/*     */       
/* 641 */       x += (leftToRight ? -spacing - buttonWidth : spacing);
/* 642 */       this.iconButton.setBounds(x, y, buttonWidth, buttonHeight);
/*     */       
/* 644 */       if (!leftToRight) {
/* 645 */         x += buttonWidth;
/*     */       }
/*     */     }
/*     */     
/*     */ 
/* 650 */     this.buttonsWidth = (leftToRight ? w - x : x);
/*     */   }
/*     */   
/*     */   public void activate() {
/* 654 */     this.closeButton.setEnabled(true);
/* 655 */     this.iconButton.setEnabled(true);
/* 656 */     this.maxButton.setEnabled(true);
/*     */   }
/*     */   
/*     */   public void deactivate() {
/* 660 */     this.closeButton.setEnabled(false);
/* 661 */     this.iconButton.setEnabled(false);
/* 662 */     this.maxButton.setEnabled(false);
/*     */   }
/*     */   
/*     */ 
/*     */   class RolloverListener
/*     */     implements MouseListener
/*     */   {
/*     */     JButton button;
/*     */     
/*     */     Action action;
/*     */     
/*     */ 
/*     */     public RolloverListener(JButton b, Action a)
/*     */     {
/* 676 */       this.button = b;
/* 677 */       this.action = a;
/*     */     }
/*     */     
/*     */     public void mouseClicked(MouseEvent e) {
/* 681 */       this.action.actionPerformed(new ActionEvent(this, 1001, this.button.getText()));
/*     */     }
/*     */     
/*     */ 
/*     */     public void mousePressed(MouseEvent e) {}
/*     */     
/*     */ 
/*     */     public void mouseReleased(MouseEvent e) {}
/*     */     
/*     */     public void mouseEntered(MouseEvent e)
/*     */     {
/* 692 */       this.button.getModel().setRollover(true);
/*     */       
/* 694 */       if (!this.button.isEnabled()) {
/* 695 */         this.button.setEnabled(true);
/*     */       }
/*     */       
/* 698 */       this.button.repaint();
/*     */     }
/*     */     
/*     */     public void mouseExited(MouseEvent e) {
/* 702 */       this.button.getModel().setRollover(false);
/*     */       
/* 704 */       if (!LiquidInternalFrameTitlePane.this.frame.isSelected()) {
/* 705 */         this.button.setEnabled(false);
/*     */       }
/*     */       
/* 708 */       this.button.repaint();
/*     */     }
/*     */   }
/*     */ }


/* Location:              E:\java\java学习\hutubill\lib\all.jar!\com\birosoft\liquid\LiquidInternalFrameTitlePane.class
 * Java compiler version: 4 (48.0)
 * JD-Core Version:       0.7.1
 */
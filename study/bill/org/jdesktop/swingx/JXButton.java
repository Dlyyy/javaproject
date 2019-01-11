/*     */ package org.jdesktop.swingx;
/*     */ 
/*     */ import java.awt.Color;
/*     */ import java.awt.Dimension;
/*     */ import java.awt.FontMetrics;
/*     */ import java.awt.Graphics;
/*     */ import java.awt.Graphics2D;
/*     */ import java.awt.Insets;
/*     */ import java.awt.Rectangle;
/*     */ import java.awt.image.BufferedImage;
/*     */ import java.awt.image.BufferedImageOp;
/*     */ import javax.swing.Action;
/*     */ import javax.swing.ButtonModel;
/*     */ import javax.swing.CellRendererPane;
/*     */ import javax.swing.Icon;
/*     */ import javax.swing.JButton;
/*     */ import javax.swing.SwingUtilities;
/*     */ import javax.swing.plaf.basic.BasicGraphicsUtils;
/*     */ import org.jdesktop.swingx.color.ColorUtil;
/*     */ import org.jdesktop.swingx.graphics.GraphicsUtilities;
/*     */ import org.jdesktop.swingx.painter.AbstractPainter;
/*     */ import org.jdesktop.swingx.painter.Painter;
/*     */ import org.jdesktop.swingx.painter.PainterPaint;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class JXButton
/*     */   extends JButton
/*     */   implements BackgroundPaintable
/*     */ {
/*     */   public static final String uiClassID = "XButtonUI";
/*     */   private ForegroundButton fgStamp;
/*     */   private Painter fgPainter;
/*     */   private PainterPaint fgPaint;
/*     */   private BackgroundButton bgStamp;
/*     */   private Painter bgPainter;
/*     */   
/*     */   private class BackgroundButton
/*     */     extends JButton
/*     */   {
/*     */     private BackgroundButton() {}
/*     */     
/*     */     public boolean isDefaultButton()
/*     */     {
/*  77 */       return JXButton.this.isDefaultButton();
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */     public Icon getDisabledIcon()
/*     */     {
/*  85 */       return null;
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */     public Icon getDisabledSelectedIcon()
/*     */     {
/*  93 */       return null;
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */     public int getDisplayedMnemonicIndex()
/*     */     {
/* 101 */       return -1;
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */     public int getHorizontalAlignment()
/*     */     {
/* 109 */       return JXButton.this.getHorizontalAlignment();
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */     public int getHorizontalTextPosition()
/*     */     {
/* 117 */       return JXButton.this.getHorizontalTextPosition();
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */     public Icon getIcon()
/*     */     {
/* 125 */       return null;
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */     public int getIconTextGap()
/*     */     {
/* 133 */       return JXButton.this.getIconTextGap();
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */     public Insets getMargin()
/*     */     {
/* 141 */       return JXButton.this.getMargin();
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */     public int getMnemonic()
/*     */     {
/* 149 */       return -1;
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */     public ButtonModel getModel()
/*     */     {
/* 157 */       return JXButton.this.getModel();
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */     public Icon getPressedIcon()
/*     */     {
/* 165 */       return null;
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */     public Icon getRolloverIcon()
/*     */     {
/* 173 */       return null;
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */     public Icon getRolloverSelectedIcon()
/*     */     {
/* 181 */       return null;
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */     public Icon getSelectedIcon()
/*     */     {
/* 189 */       return null;
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */     public String getText()
/*     */     {
/* 197 */       return "";
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */     public int getVerticalAlignment()
/*     */     {
/* 205 */       return JXButton.this.getVerticalAlignment();
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */     public int getVerticalTextPosition()
/*     */     {
/* 213 */       return JXButton.this.getVerticalTextPosition();
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */     public boolean isBorderPainted()
/*     */     {
/* 221 */       return JXButton.this.isBorderPainted();
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */     public boolean isContentAreaFilled()
/*     */     {
/* 229 */       return JXButton.this.isContentAreaFilled();
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */     public boolean isFocusPainted()
/*     */     {
/* 237 */       return false;
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */     public boolean isRolloverEnabled()
/*     */     {
/* 245 */       return JXButton.this.isRolloverEnabled();
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */     public boolean isSelected()
/*     */     {
/* 253 */       return JXButton.this.isSelected();
/*     */     }
/*     */   }
/*     */   
/*     */   private class ForegroundButton
/*     */     extends JButton
/*     */   {
/*     */     private ForegroundButton() {}
/*     */     
/*     */     public Color getForeground()
/*     */     {
/* 264 */       if (JXButton.this.fgPainter == null) {
/* 265 */         return JXButton.this.getForeground();
/*     */       }
/*     */       
/* 268 */       return ColorUtil.setAlpha(JXButton.this.getForeground(), 0);
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */     public boolean isDefaultButton()
/*     */     {
/* 275 */       return JXButton.this.isDefaultButton();
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */     public Icon getDisabledIcon()
/*     */     {
/* 283 */       return JXButton.this.getDisabledIcon();
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */     public Icon getDisabledSelectedIcon()
/*     */     {
/* 291 */       return JXButton.this.getDisabledSelectedIcon();
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */     public int getDisplayedMnemonicIndex()
/*     */     {
/* 299 */       return JXButton.this.getDisplayedMnemonicIndex();
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */     public int getHorizontalAlignment()
/*     */     {
/* 307 */       return JXButton.this.getHorizontalAlignment();
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */     public int getHorizontalTextPosition()
/*     */     {
/* 315 */       return JXButton.this.getHorizontalTextPosition();
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */     public Icon getIcon()
/*     */     {
/* 323 */       return JXButton.this.getIcon();
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */     public int getIconTextGap()
/*     */     {
/* 331 */       return JXButton.this.getIconTextGap();
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */     public Insets getMargin()
/*     */     {
/* 339 */       return JXButton.this.getMargin();
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */     public int getMnemonic()
/*     */     {
/* 347 */       return JXButton.this.getMnemonic();
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */     public ButtonModel getModel()
/*     */     {
/* 355 */       return JXButton.this.getModel();
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */     public Icon getPressedIcon()
/*     */     {
/* 363 */       return JXButton.this.getPressedIcon();
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */     public Icon getRolloverIcon()
/*     */     {
/* 371 */       return JXButton.this.getRolloverIcon();
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */     public Icon getRolloverSelectedIcon()
/*     */     {
/* 379 */       return JXButton.this.getRolloverSelectedIcon();
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */     public Icon getSelectedIcon()
/*     */     {
/* 387 */       return JXButton.this.getSelectedIcon();
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */     public String getText()
/*     */     {
/* 395 */       return JXButton.this.getText();
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */     public int getVerticalAlignment()
/*     */     {
/* 403 */       return JXButton.this.getVerticalAlignment();
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */     public int getVerticalTextPosition()
/*     */     {
/* 411 */       return JXButton.this.getVerticalTextPosition();
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */     public boolean isBorderPainted()
/*     */     {
/* 419 */       return JXButton.this.isBorderPainted();
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */     public boolean isContentAreaFilled()
/*     */     {
/* 427 */       return false;
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */     public boolean hasFocus()
/*     */     {
/* 435 */       return JXButton.this.hasFocus();
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */     public boolean isFocusPainted()
/*     */     {
/* 443 */       return JXButton.this.isFocusPainted();
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */     public boolean isRolloverEnabled()
/*     */     {
/* 451 */       return JXButton.this.isRolloverEnabled();
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */     public boolean isSelected()
/*     */     {
/* 459 */       return JXButton.this.isSelected();
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
/* 475 */   private boolean paintBorderInsets = true;
/*     */   
/* 477 */   private Rectangle viewRect = new Rectangle();
/* 478 */   private Rectangle textRect = new Rectangle();
/* 479 */   private Rectangle iconRect = new Rectangle();
/*     */   
/*     */ 
/*     */ 
/*     */   public JXButton()
/*     */   {
/* 485 */     init();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public JXButton(String text)
/*     */   {
/* 495 */     super(text);
/* 496 */     init();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public JXButton(Action a)
/*     */   {
/* 506 */     super(a);
/* 507 */     init();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public JXButton(Icon icon)
/*     */   {
/* 517 */     super(icon);
/* 518 */     init();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public JXButton(String text, Icon icon)
/*     */   {
/* 530 */     super(text, icon);
/* 531 */     init();
/*     */   }
/*     */   
/*     */   private void init() {
/* 535 */     this.fgStamp = new ForegroundButton(null);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public Painter getBackgroundPainter()
/*     */   {
/* 544 */     return this.bgPainter;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setBackgroundPainter(Painter p)
/*     */   {
/* 553 */     Painter old = getBackgroundPainter();
/* 554 */     this.bgPainter = p;
/* 555 */     firePropertyChange("backgroundPainter", old, getBackgroundPainter());
/* 556 */     repaint();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public Painter getForegroundPainter()
/*     */   {
/* 564 */     return this.fgPainter;
/*     */   }
/*     */   
/*     */   public void setForegroundPainter(Painter p)
/*     */   {
/* 569 */     Painter old = getForegroundPainter();
/* 570 */     this.fgPainter = p;
/*     */     
/* 572 */     if (this.fgPainter == null) {
/* 573 */       this.fgPaint = null;
/*     */     } else {
/* 575 */       this.fgPaint = new PainterPaint(this.fgPainter, this);
/*     */       
/* 577 */       if (this.bgStamp == null) {
/* 578 */         this.bgStamp = new BackgroundButton(null);
/*     */       }
/*     */     }
/*     */     
/* 582 */     firePropertyChange("foregroundPainter", old, getForegroundPainter());
/* 583 */     repaint();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean isPaintBorderInsets()
/*     */   {
/* 593 */     return this.paintBorderInsets;
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
/*     */   public void setPaintBorderInsets(boolean paintBorderInsets)
/*     */   {
/* 606 */     boolean old = isPaintBorderInsets();
/* 607 */     this.paintBorderInsets = paintBorderInsets;
/* 608 */     firePropertyChange("paintBorderInsets", old, isPaintBorderInsets());
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public Dimension getPreferredSize()
/*     */   {
/* 616 */     if ((getComponentCount() == 1) && ((getComponent(0) instanceof CellRendererPane))) {
/* 617 */       return BasicGraphicsUtils.getPreferredButtonSize(this.fgStamp, getIconTextGap());
/*     */     }
/*     */     
/* 620 */     return super.getPreferredSize();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   protected void paintComponent(Graphics g)
/*     */   {
/* 628 */     if ((this.fgPainter == null) && (this.bgPainter == null)) {
/* 629 */       super.paintComponent(g);
/*     */     }
/* 631 */     else if (this.fgPainter == null) {
/* 632 */       Graphics2D g2d = (Graphics2D)g.create();
/*     */       try
/*     */       {
/* 635 */         paintWithoutForegroundPainter(g2d);
/*     */       } finally {
/* 637 */         g2d.dispose();
/*     */       }
/* 639 */     } else if (((this.fgPainter instanceof AbstractPainter)) && (((AbstractPainter)this.fgPainter).getFilters().length > 0)) {
/* 640 */       paintWithForegroundPainterWithFilters(g);
/*     */     } else {
/* 642 */       Graphics2D g2d = (Graphics2D)g.create();
/*     */       try
/*     */       {
/* 645 */         paintWithForegroundPainterWithoutFilters(g2d);
/*     */       } finally {
/* 647 */         g2d.dispose();
/*     */       }
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private void paintWithoutForegroundPainter(Graphics2D g2d)
/*     */   {
/* 695 */     if (this.bgPainter == null) {
/* 696 */       SwingUtilities.paintComponent(g2d, this.bgStamp, this, 0, 0, getWidth(), getHeight());
/*     */     } else {
/* 698 */       SwingXUtilities.paintBackground(this, g2d);
/*     */     }
/*     */     
/* 701 */     SwingUtilities.paintComponent(g2d, this.fgStamp, this, 0, 0, getWidth(), getHeight());
/*     */   }
/*     */   
/*     */   private void paintWithForegroundPainterWithoutFilters(Graphics2D g2d) {
/* 705 */     paintWithoutForegroundPainter(g2d);
/*     */     
/* 707 */     if ((getText() != null) && (!getText().isEmpty())) {
/* 708 */       Insets i = getInsets();
/* 709 */       this.viewRect.x = i.left;
/* 710 */       this.viewRect.y = i.top;
/* 711 */       this.viewRect.width = (getWidth() - (i.right + this.viewRect.x));
/* 712 */       this.viewRect.height = (getHeight() - (i.bottom + this.viewRect.y));
/*     */       
/* 714 */       this.textRect.x = (this.textRect.y = this.textRect.width = this.textRect.height = 0);
/* 715 */       this.iconRect.x = (this.iconRect.y = this.iconRect.width = this.iconRect.height = 0);
/*     */       
/*     */ 
/* 718 */       String text = SwingUtilities.layoutCompoundLabel(this, g2d.getFontMetrics(), getText(), getIcon(), getVerticalAlignment(), getHorizontalAlignment(), getVerticalTextPosition(), getHorizontalTextPosition(), this.viewRect, this.iconRect, this.textRect, getText() == null ? 0 : getIconTextGap());
/*     */       
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 725 */       if (!isPaintBorderInsets()) {
/* 726 */         g2d.translate(i.left, i.top);
/*     */       }
/*     */       
/* 729 */       g2d.setPaint(this.fgPaint);
/* 730 */       BasicGraphicsUtils.drawStringUnderlineCharAt(g2d, text, getDisplayedMnemonicIndex(), this.textRect.x, this.textRect.y + g2d.getFontMetrics().getAscent());
/*     */     }
/*     */   }
/*     */   
/*     */   private void paintWithForegroundPainterWithFilters(Graphics g)
/*     */   {
/* 736 */     BufferedImage im = GraphicsUtilities.createCompatibleImage(getWidth(), getHeight());
/* 737 */     Graphics2D g2d = im.createGraphics();
/* 738 */     paintWithForegroundPainterWithoutFilters(g2d);
/*     */     
/* 740 */     for (BufferedImageOp filter : ((AbstractPainter)this.fgPainter).getFilters()) {
/* 741 */       im = filter.filter(im, null);
/*     */     }
/*     */     
/* 744 */     g.drawImage(im, 0, 0, this);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void updateUI()
/*     */   {
/* 755 */     super.updateUI();
/*     */     
/* 757 */     if (this.bgStamp != null) {
/* 758 */       this.bgStamp.updateUI();
/*     */     }
/*     */     
/* 761 */     if (this.fgStamp != null) {
/* 762 */       this.fgStamp.updateUI();
/*     */     }
/*     */   }
/*     */ }


/* Location:              E:\java\java学习\hutubill\lib\all.jar!\org\jdesktop\swingx\JXButton.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */
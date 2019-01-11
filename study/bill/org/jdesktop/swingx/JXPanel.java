/*     */ package org.jdesktop.swingx;
/*     */ 
/*     */ import java.awt.AlphaComposite;
/*     */ import java.awt.Color;
/*     */ import java.awt.Component;
/*     */ import java.awt.Composite;
/*     */ import java.awt.Dimension;
/*     */ import java.awt.Graphics;
/*     */ import java.awt.Graphics2D;
/*     */ import java.awt.LayoutManager;
/*     */ import java.awt.Rectangle;
/*     */ import java.beans.PropertyChangeEvent;
/*     */ import java.beans.PropertyChangeListener;
/*     */ import javax.swing.JPanel;
/*     */ import javax.swing.RepaintManager;
/*     */ import javax.swing.Scrollable;
/*     */ import org.jdesktop.swingx.painter.AbstractPainter;
/*     */ import org.jdesktop.swingx.painter.Painter;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class JXPanel
/*     */   extends JPanel
/*     */   implements BackgroundPaintable, Scrollable
/*     */ {
/*  84 */   private ScrollableSizeHint scrollableWidthHint = ScrollableSizeHint.FIT;
/*  85 */   private ScrollableSizeHint scrollableHeightHint = ScrollableSizeHint.FIT;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*  90 */   private float alpha = 1.0F;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private boolean oldOpaque;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 102 */   private boolean inheritAlpha = true;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private Painter backgroundPainter;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private PropertyChangeListener painterChangeListener;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public JXPanel() {}
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public JXPanel(boolean isDoubleBuffered)
/*     */   {
/* 134 */     super(isDoubleBuffered);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public JXPanel(LayoutManager layout)
/*     */   {
/* 143 */     super(layout);
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
/*     */   public JXPanel(LayoutManager layout, boolean isDoubleBuffered)
/*     */   {
/* 156 */     super(layout, isDoubleBuffered);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setAlpha(float alpha)
/*     */   {
/* 168 */     if (this.alpha != alpha) {
/* 169 */       assert ((alpha >= 0.0F) && (alpha <= 1.0D));
/* 170 */       float oldAlpha = this.alpha;
/* 171 */       this.alpha = alpha;
/* 172 */       if ((alpha > 0.0F) && (alpha < 1.0F)) {
/* 173 */         if (oldAlpha == 1.0F)
/*     */         {
/* 175 */           this.oldOpaque = isOpaque();
/* 176 */           setOpaque(false);
/*     */         }
/*     */         
/* 179 */         RepaintManager manager = RepaintManager.currentManager(this);
/* 180 */         RepaintManager trm = SwingXUtilities.getTranslucentRepaintManager(manager);
/* 181 */         RepaintManager.setCurrentManager(trm);
/* 182 */       } else if (alpha == 1.0F)
/*     */       {
/* 184 */         if (this.oldOpaque) {
/* 185 */           setOpaque(true);
/*     */         }
/*     */       }
/* 188 */       firePropertyChange("alpha", oldAlpha, alpha);
/* 189 */       repaint();
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public float getAlpha()
/*     */   {
/* 198 */     return this.alpha;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public float getEffectiveAlpha()
/*     */   {
/* 208 */     if (this.inheritAlpha) {
/* 209 */       float a = this.alpha;
/* 210 */       Component c = this;
/* 211 */       while ((c = c.getParent()) != null) {
/* 212 */         if ((c instanceof JXPanel)) {
/* 213 */           a = Math.min(((JXPanel)c).getAlpha(), a);
/*     */         }
/*     */       }
/* 216 */       return a;
/*     */     }
/* 218 */     return this.alpha;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean isInheritAlpha()
/*     */   {
/* 230 */     return this.inheritAlpha;
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
/*     */   public void setInheritAlpha(boolean val)
/*     */   {
/* 244 */     if (this.inheritAlpha != val) {
/* 245 */       this.inheritAlpha = val;
/* 246 */       firePropertyChange("inheritAlpha", !this.inheritAlpha, this.inheritAlpha);
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
/*     */   public final void setScrollableWidthHint(ScrollableSizeHint hint)
/*     */   {
/* 264 */     if (!hint.isHorizontalCompatible()) { throw new IllegalArgumentException("track must be horizontal, but was " + hint);
/*     */     }
/* 266 */     ScrollableSizeHint oldValue = getScrollableWidthHint();
/* 267 */     if (oldValue == hint) return;
/* 268 */     this.scrollableWidthHint = hint;
/* 269 */     revalidate();
/* 270 */     firePropertyChange("scrollableWidthHint", oldValue, getScrollableWidthHint());
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
/*     */   public final void setScrollableHeightHint(ScrollableSizeHint hint)
/*     */   {
/* 288 */     if (!hint.isVerticalCompatible()) { throw new IllegalArgumentException("track must be vertical, but was " + hint);
/*     */     }
/* 290 */     ScrollableSizeHint oldValue = getScrollableHeightHint();
/* 291 */     if (oldValue == hint) return;
/* 292 */     this.scrollableHeightHint = hint;
/* 293 */     revalidate();
/* 294 */     firePropertyChange("scrollableHeightHint", oldValue, getScrollableHeightHint());
/*     */   }
/*     */   
/*     */   protected ScrollableSizeHint getScrollableWidthHint() {
/* 298 */     return this.scrollableWidthHint;
/*     */   }
/*     */   
/*     */   protected ScrollableSizeHint getScrollableHeightHint() {
/* 302 */     return this.scrollableHeightHint;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean getScrollableTracksViewportHeight()
/*     */   {
/* 311 */     return this.scrollableHeightHint.getTracksParentSize(this);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public boolean getScrollableTracksViewportWidth()
/*     */   {
/* 318 */     return this.scrollableWidthHint.getTracksParentSize(this);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public Dimension getPreferredScrollableViewportSize()
/*     */   {
/* 325 */     return getPreferredSize();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public int getScrollableBlockIncrement(Rectangle visibleRect, int orientation, int direction)
/*     */   {
/* 332 */     if (orientation == 1)
/* 333 */       return visibleRect.height;
/* 334 */     if (orientation == 0) {
/* 335 */       return visibleRect.width;
/*     */     }
/* 337 */     throw new IllegalArgumentException("invalid orientation");
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public int getScrollableUnitIncrement(Rectangle visibleRect, int orientation, int direction)
/*     */   {
/* 345 */     return getScrollableBlockIncrement(visibleRect, orientation, direction) / 10;
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
/*     */   public void setScrollableTracksViewportHeight(boolean scrollableTracksViewportHeight)
/*     */   {
/* 361 */     setScrollableHeightHint(scrollableTracksViewportHeight ? ScrollableSizeHint.FIT : ScrollableSizeHint.NONE);
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
/*     */   public void setScrollableTracksViewportWidth(boolean scrollableTracksViewportWidth)
/*     */   {
/* 377 */     setScrollableWidthHint(scrollableTracksViewportWidth ? ScrollableSizeHint.FIT : ScrollableSizeHint.NONE);
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
/*     */   public void setBackground(Color bg)
/*     */   {
/* 397 */     super.setBackground(bg);
/*     */     
/* 399 */     SwingXUtilities.installBackground(this, bg);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setBackgroundPainter(Painter p)
/*     */   {
/* 409 */     Painter old = getBackgroundPainter();
/* 410 */     if ((old instanceof AbstractPainter)) {
/* 411 */       ((AbstractPainter)old).removePropertyChangeListener(this.painterChangeListener);
/*     */     }
/* 413 */     this.backgroundPainter = p;
/* 414 */     if ((this.backgroundPainter instanceof AbstractPainter)) {
/* 415 */       ((AbstractPainter)this.backgroundPainter).addPropertyChangeListener(getPainterChangeListener());
/*     */     }
/* 417 */     firePropertyChange("backgroundPainter", old, getBackgroundPainter());
/* 418 */     repaint();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   protected PropertyChangeListener getPainterChangeListener()
/*     */   {
/* 425 */     if (this.painterChangeListener == null) {
/* 426 */       this.painterChangeListener = new PropertyChangeListener()
/*     */       {
/*     */         public void propertyChange(PropertyChangeEvent evt)
/*     */         {
/* 430 */           JXPanel.this.repaint();
/*     */         }
/*     */       };
/*     */     }
/* 434 */     return this.painterChangeListener;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public Painter getBackgroundPainter()
/*     */   {
/* 445 */     return this.backgroundPainter;
/*     */   }
/*     */   
/*     */ 
/* 449 */   private boolean paintBorderInsets = true;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean isPaintBorderInsets()
/*     */   {
/* 458 */     return this.paintBorderInsets;
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
/* 471 */     boolean old = isPaintBorderInsets();
/* 472 */     this.paintBorderInsets = paintBorderInsets;
/* 473 */     firePropertyChange("paintBorderInsets", old, isPaintBorderInsets());
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void paint(Graphics g)
/*     */   {
/* 482 */     Graphics2D g2d = (Graphics2D)g;
/* 483 */     Composite oldComp = g2d.getComposite();
/*     */     try
/*     */     {
/* 486 */       float alpha = getEffectiveAlpha();
/* 487 */       Composite alphaComp = AlphaComposite.getInstance(3, alpha);
/* 488 */       g2d.setComposite(alphaComp);
/* 489 */       super.paint(g2d);
/*     */     } finally {
/* 491 */       g2d.setComposite(oldComp);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected void paintComponent(Graphics g)
/*     */   {
/* 501 */     if (this.backgroundPainter != null) {
/* 502 */       if (isOpaque()) {
/* 503 */         super.paintComponent(g);
/*     */       }
/*     */       
/* 506 */       Graphics2D g2 = (Graphics2D)g.create();
/*     */       try
/*     */       {
/* 509 */         SwingXUtilities.paintBackground(this, g2);
/*     */       } finally {
/* 511 */         g2.dispose();
/*     */       }
/*     */     } else {
/* 514 */       super.paintComponent(g);
/*     */     }
/*     */   }
/*     */ }


/* Location:              E:\java\java学习\hutubill\lib\all.jar!\org\jdesktop\swingx\JXPanel.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */
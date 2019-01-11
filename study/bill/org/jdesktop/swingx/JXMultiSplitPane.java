/*     */ package org.jdesktop.swingx;
/*     */ 
/*     */ import java.awt.Color;
/*     */ import java.awt.Cursor;
/*     */ import java.awt.Dimension;
/*     */ import java.awt.Graphics;
/*     */ import java.awt.Graphics2D;
/*     */ import java.awt.Insets;
/*     */ import java.awt.Rectangle;
/*     */ import java.awt.event.KeyEvent;
/*     */ import java.awt.event.KeyListener;
/*     */ import java.awt.event.MouseEvent;
/*     */ import javax.accessibility.AccessibleContext;
/*     */ import javax.accessibility.AccessibleRole;
/*     */ import javax.swing.JPanel;
/*     */ import javax.swing.JPanel.AccessibleJPanel;
/*     */ import javax.swing.event.MouseInputAdapter;
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
/*     */ public class JXMultiSplitPane
/*     */   extends JPanel
/*     */ {
/*  55 */   private AccessibleContext accessibleContext = null;
/*  56 */   private boolean continuousLayout = true;
/*  57 */   private DividerPainter dividerPainter = new DefaultDividerPainter(null);
/*     */   
/*     */ 
/*     */   private Painter backgroundPainter;
/*     */   
/*     */ 
/*     */   public JXMultiSplitPane()
/*     */   {
/*  65 */     this(new MultiSplitLayout());
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public JXMultiSplitPane(MultiSplitLayout layout)
/*     */   {
/*  73 */     super(layout);
/*  74 */     InputHandler inputHandler = new InputHandler(null);
/*  75 */     addMouseListener(inputHandler);
/*  76 */     addMouseMotionListener(inputHandler);
/*  77 */     addKeyListener(inputHandler);
/*  78 */     setFocusable(true);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public final MultiSplitLayout getMultiSplitLayout()
/*     */   {
/*  90 */     return (MultiSplitLayout)getLayout();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public final void setModel(MultiSplitLayout.Node model)
/*     */   {
/* 102 */     getMultiSplitLayout().setModel(model);
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
/*     */   public final void setDividerSize(int dividerSize)
/*     */   {
/* 115 */     getMultiSplitLayout().setDividerSize(dividerSize);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public final int getDividerSize()
/*     */   {
/* 127 */     return getMultiSplitLayout().getDividerSize();
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
/*     */   public void setContinuousLayout(boolean continuousLayout)
/*     */   {
/* 140 */     boolean oldContinuousLayout = isContinuousLayout();
/* 141 */     this.continuousLayout = continuousLayout;
/* 142 */     firePropertyChange("continuousLayout", oldContinuousLayout, isContinuousLayout());
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean isContinuousLayout()
/*     */   {
/* 154 */     return this.continuousLayout;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public MultiSplitLayout.Divider activeDivider()
/*     */   {
/* 164 */     return this.dragDivider;
/*     */   }
/*     */   
/*     */ 
/*     */   public static abstract class DividerPainter
/*     */     extends AbstractPainter<MultiSplitLayout.Divider>
/*     */   {}
/*     */   
/*     */ 
/*     */   private class DefaultDividerPainter
/*     */     extends JXMultiSplitPane.DividerPainter
/*     */   {
/*     */     private DefaultDividerPainter() {}
/*     */     
/*     */     protected void doPaint(Graphics2D g, MultiSplitLayout.Divider divider, int width, int height)
/*     */     {
/* 180 */       if ((divider == JXMultiSplitPane.this.activeDivider()) && (!JXMultiSplitPane.this.isContinuousLayout())) {
/* 181 */         g.setColor(Color.black);
/* 182 */         g.fillRect(0, 0, width, height);
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
/*     */   public DividerPainter getDividerPainter()
/*     */   {
/* 195 */     return this.dividerPainter;
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
/*     */   public void setDividerPainter(DividerPainter dividerPainter)
/*     */   {
/* 212 */     DividerPainter old = getDividerPainter();
/* 213 */     this.dividerPainter = dividerPainter;
/* 214 */     firePropertyChange("dividerPainter", old, getDividerPainter());
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
/*     */   protected void paintComponent(Graphics g)
/*     */   {
/* 249 */     if (this.backgroundPainter != null) {
/* 250 */       Graphics2D g2 = (Graphics2D)g.create();
/*     */       try
/*     */       {
/* 253 */         Insets ins = getInsets();
/* 254 */         g2.translate(ins.left, ins.top);
/* 255 */         this.backgroundPainter.paint(g2, this, getWidth() - ins.left - ins.right, getHeight() - ins.top - ins.bottom);
/*     */       }
/*     */       finally {
/* 258 */         g2.dispose();
/*     */       }
/*     */     } else {
/* 261 */       super.paintComponent(g);
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
/*     */   public void setBackgroundPainter(Painter p)
/*     */   {
/* 274 */     Painter old = getBackgroundPainter();
/* 275 */     this.backgroundPainter = p;
/*     */     
/* 277 */     if (p != null) {
/* 278 */       setOpaque(false);
/*     */     }
/*     */     
/* 281 */     firePropertyChange("backgroundPainter", old, getBackgroundPainter());
/* 282 */     repaint();
/*     */   }
/*     */   
/*     */   public Painter getBackgroundPainter() {
/* 286 */     return this.backgroundPainter;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected void paintChildren(Graphics g)
/*     */   {
/* 298 */     super.paintChildren(g);
/* 299 */     DividerPainter dp = getDividerPainter();
/* 300 */     Rectangle clipR = g.getClipBounds();
/* 301 */     if ((dp != null) && (clipR != null)) {
/* 302 */       MultiSplitLayout msl = getMultiSplitLayout();
/* 303 */       if (msl.hasModel()) {
/* 304 */         for (MultiSplitLayout.Divider divider : msl.dividersThatOverlap(clipR)) {
/* 305 */           Rectangle bounds = divider.getBounds();
/* 306 */           Graphics cg = g.create(bounds.x, bounds.y, bounds.width, bounds.height);
/*     */           try {
/* 308 */             dp.paint((Graphics2D)cg, divider, bounds.width, bounds.height);
/*     */           } finally {
/* 310 */             cg.dispose();
/*     */           }
/*     */         }
/*     */       }
/*     */     }
/*     */   }
/*     */   
/* 317 */   private boolean dragUnderway = false;
/* 318 */   private MultiSplitLayout.Divider dragDivider = null;
/* 319 */   private Rectangle initialDividerBounds = null;
/* 320 */   private boolean oldFloatingDividers = true;
/* 321 */   private int dragOffsetX = 0;
/* 322 */   private int dragOffsetY = 0;
/* 323 */   private int dragMin = -1;
/* 324 */   private int dragMax = -1;
/*     */   
/*     */   private void startDrag(int mx, int my) {
/* 327 */     requestFocusInWindow();
/* 328 */     MultiSplitLayout msl = getMultiSplitLayout();
/* 329 */     MultiSplitLayout.Divider divider = msl.dividerAt(mx, my);
/* 330 */     if (divider != null) {
/* 331 */       MultiSplitLayout.Node prevNode = divider.previousSibling();
/* 332 */       MultiSplitLayout.Node nextNode = divider.nextSibling();
/* 333 */       if ((prevNode == null) || (nextNode == null)) {
/* 334 */         this.dragUnderway = false;
/*     */       }
/*     */       else {
/* 337 */         this.initialDividerBounds = divider.getBounds();
/* 338 */         this.dragOffsetX = (mx - this.initialDividerBounds.x);
/* 339 */         this.dragOffsetY = (my - this.initialDividerBounds.y);
/* 340 */         this.dragDivider = divider;
/*     */         
/* 342 */         Rectangle prevNodeBounds = prevNode.getBounds();
/* 343 */         Rectangle nextNodeBounds = nextNode.getBounds();
/* 344 */         if (this.dragDivider.isVertical()) {
/* 345 */           this.dragMin = prevNodeBounds.x;
/* 346 */           this.dragMax = (nextNodeBounds.x + nextNodeBounds.width);
/* 347 */           this.dragMax -= this.dragDivider.getBounds().width;
/* 348 */           if (msl.getLayoutMode() == 2) {
/* 349 */             this.dragMax -= msl.getUserMinSize();
/*     */           }
/*     */         } else {
/* 352 */           this.dragMin = prevNodeBounds.y;
/* 353 */           this.dragMax = (nextNodeBounds.y + nextNodeBounds.height);
/* 354 */           this.dragMax -= this.dragDivider.getBounds().height;
/* 355 */           if (msl.getLayoutMode() == 2) {
/* 356 */             this.dragMax -= msl.getUserMinSize();
/*     */           }
/*     */         }
/* 359 */         if (msl.getLayoutMode() == 2) {
/* 360 */           this.dragMin += msl.getUserMinSize();
/*     */ 
/*     */         }
/* 363 */         else if (this.dragDivider.isVertical()) {
/* 364 */           this.dragMin = Math.max(this.dragMin, this.dragMin + getMinNodeSize(msl, prevNode).width);
/* 365 */           this.dragMax = Math.min(this.dragMax, this.dragMax - getMinNodeSize(msl, nextNode).width);
/*     */           
/* 367 */           Dimension maxDim = getMaxNodeSize(msl, prevNode);
/* 368 */           if (maxDim != null) {
/* 369 */             this.dragMax = Math.min(this.dragMax, prevNodeBounds.x + maxDim.width);
/*     */           }
/*     */         } else {
/* 372 */           this.dragMin = Math.max(this.dragMin, this.dragMin + getMinNodeSize(msl, prevNode).height);
/* 373 */           this.dragMax = Math.min(this.dragMax, this.dragMax - getMinNodeSize(msl, nextNode).height);
/*     */           
/* 375 */           Dimension maxDim = getMaxNodeSize(msl, prevNode);
/* 376 */           if (maxDim != null) {
/* 377 */             this.dragMax = Math.min(this.dragMax, prevNodeBounds.y + maxDim.height);
/*     */           }
/*     */         }
/*     */         
/* 381 */         this.oldFloatingDividers = getMultiSplitLayout().getFloatingDividers();
/* 382 */         getMultiSplitLayout().setFloatingDividers(false);
/* 383 */         this.dragUnderway = true;
/*     */       }
/*     */     }
/*     */     else {
/* 387 */       this.dragUnderway = false;
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
/*     */   protected Dimension getMaxNodeSize(MultiSplitLayout msl, MultiSplitLayout.Node n)
/*     */   {
/* 412 */     return null;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected Dimension getMinNodeSize(MultiSplitLayout msl, MultiSplitLayout.Node n)
/*     */   {
/* 423 */     return msl.minimumNodeSize(n);
/*     */   }
/*     */   
/*     */   private void repaintDragLimits() {
/* 427 */     Rectangle damageR = this.dragDivider.getBounds();
/* 428 */     if (this.dragDivider.isVertical()) {
/* 429 */       damageR.x = this.dragMin;
/* 430 */       damageR.width = (this.dragMax - this.dragMin);
/*     */     }
/*     */     else {
/* 433 */       damageR.y = this.dragMin;
/* 434 */       damageR.height = (this.dragMax - this.dragMin);
/*     */     }
/* 436 */     repaint(damageR);
/*     */   }
/*     */   
/*     */   private void updateDrag(int mx, int my) {
/* 440 */     if (!this.dragUnderway) {
/* 441 */       return;
/*     */     }
/* 443 */     Rectangle oldBounds = this.dragDivider.getBounds();
/* 444 */     Rectangle bounds = new Rectangle(oldBounds);
/* 445 */     if (this.dragDivider.isVertical()) {
/* 446 */       bounds.x = (mx - this.dragOffsetX);
/* 447 */       bounds.x = Math.max(bounds.x, this.dragMin);
/* 448 */       bounds.x = Math.min(bounds.x, this.dragMax);
/*     */     }
/*     */     else {
/* 451 */       bounds.y = (my - this.dragOffsetY);
/* 452 */       bounds.y = Math.max(bounds.y, this.dragMin);
/* 453 */       bounds.y = Math.min(bounds.y, this.dragMax);
/*     */     }
/* 455 */     this.dragDivider.setBounds(bounds);
/* 456 */     if (isContinuousLayout()) {
/* 457 */       revalidate();
/* 458 */       repaintDragLimits();
/*     */     }
/*     */     else {
/* 461 */       repaint(oldBounds.union(bounds));
/*     */     }
/*     */   }
/*     */   
/*     */   private void clearDragState() {
/* 466 */     this.dragDivider = null;
/* 467 */     this.initialDividerBounds = null;
/* 468 */     this.oldFloatingDividers = true;
/* 469 */     this.dragOffsetX = (this.dragOffsetY = 0);
/* 470 */     this.dragMin = (this.dragMax = -1);
/* 471 */     this.dragUnderway = false;
/*     */   }
/*     */   
/*     */   private void finishDrag(int x, int y) {
/* 475 */     if (this.dragUnderway) {
/* 476 */       clearDragState();
/* 477 */       if (!isContinuousLayout()) {
/* 478 */         revalidate();
/* 479 */         repaint();
/*     */       }
/*     */     }
/* 482 */     setCursor(Cursor.getPredefinedCursor(0));
/*     */   }
/*     */   
/*     */   private void cancelDrag() {
/* 486 */     if (this.dragUnderway) {
/* 487 */       this.dragDivider.setBounds(this.initialDividerBounds);
/* 488 */       getMultiSplitLayout().setFloatingDividers(this.oldFloatingDividers);
/* 489 */       setCursor(Cursor.getPredefinedCursor(0));
/* 490 */       repaint();
/* 491 */       revalidate();
/* 492 */       clearDragState();
/*     */     }
/*     */   }
/*     */   
/*     */   private void updateCursor(int x, int y, boolean show) {
/* 497 */     if (this.dragUnderway) {
/* 498 */       return;
/*     */     }
/* 500 */     int cursorID = 0;
/* 501 */     if (show) {
/* 502 */       MultiSplitLayout.Divider divider = getMultiSplitLayout().dividerAt(x, y);
/* 503 */       if (divider != null) {
/* 504 */         cursorID = divider.isVertical() ? 11 : 8;
/*     */       }
/*     */     }
/*     */     
/*     */ 
/* 509 */     setCursor(Cursor.getPredefinedCursor(cursorID));
/*     */   }
/*     */   
/*     */   private class InputHandler extends MouseInputAdapter implements KeyListener
/*     */   {
/*     */     private InputHandler() {}
/*     */     
/*     */     public void mouseEntered(MouseEvent e) {
/* 517 */       JXMultiSplitPane.this.updateCursor(e.getX(), e.getY(), true);
/*     */     }
/*     */     
/*     */     public void mouseMoved(MouseEvent e)
/*     */     {
/* 522 */       JXMultiSplitPane.this.updateCursor(e.getX(), e.getY(), true);
/*     */     }
/*     */     
/*     */     public void mouseExited(MouseEvent e)
/*     */     {
/* 527 */       JXMultiSplitPane.this.updateCursor(e.getX(), e.getY(), false);
/*     */     }
/*     */     
/*     */     public void mousePressed(MouseEvent e)
/*     */     {
/* 532 */       JXMultiSplitPane.this.startDrag(e.getX(), e.getY());
/*     */     }
/*     */     
/*     */     public void mouseReleased(MouseEvent e) {
/* 536 */       JXMultiSplitPane.this.finishDrag(e.getX(), e.getY());
/*     */     }
/*     */     
/*     */ 
/* 540 */     public void mouseDragged(MouseEvent e) { JXMultiSplitPane.this.updateDrag(e.getX(), e.getY()); }
/*     */     
/*     */     public void keyPressed(KeyEvent e) {
/* 543 */       if (e.getKeyCode() == 27) {
/* 544 */         JXMultiSplitPane.this.cancelDrag();
/*     */       }
/*     */     }
/*     */     
/*     */     public void keyReleased(KeyEvent e) {}
/*     */     
/*     */     public void keyTyped(KeyEvent e) {}
/*     */   }
/*     */   
/*     */   public AccessibleContext getAccessibleContext() {
/* 554 */     if (this.accessibleContext == null) {
/* 555 */       this.accessibleContext = new AccessibleMultiSplitPane();
/*     */     }
/* 557 */     return this.accessibleContext;
/*     */   }
/*     */   
/* 560 */   protected class AccessibleMultiSplitPane extends JPanel.AccessibleJPanel { protected AccessibleMultiSplitPane() { super(); }
/*     */     
/*     */     public AccessibleRole getAccessibleRole() {
/* 563 */       return AccessibleRole.SPLIT_PANE;
/*     */     }
/*     */   }
/*     */ }


/* Location:              E:\java\java学习\hutubill\lib\all.jar!\org\jdesktop\swingx\JXMultiSplitPane.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */
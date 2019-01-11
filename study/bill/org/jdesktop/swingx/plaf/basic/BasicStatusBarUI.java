/*     */ package org.jdesktop.swingx.plaf.basic;
/*     */ 
/*     */ import java.awt.Color;
/*     */ import java.awt.Component;
/*     */ import java.awt.ComponentOrientation;
/*     */ import java.awt.Container;
/*     */ import java.awt.Cursor;
/*     */ import java.awt.Dimension;
/*     */ import java.awt.Graphics;
/*     */ import java.awt.Graphics2D;
/*     */ import java.awt.Insets;
/*     */ import java.awt.LayoutManager;
/*     */ import java.awt.LayoutManager2;
/*     */ import java.awt.Point;
/*     */ import java.awt.Rectangle;
/*     */ import java.awt.Window;
/*     */ import java.awt.event.MouseEvent;
/*     */ import java.awt.event.MouseListener;
/*     */ import java.awt.event.MouseMotionListener;
/*     */ import java.beans.PropertyChangeEvent;
/*     */ import java.beans.PropertyChangeListener;
/*     */ import java.util.HashMap;
/*     */ import java.util.Map;
/*     */ import javax.swing.BorderFactory;
/*     */ import javax.swing.JComponent;
/*     */ import javax.swing.JRootPane;
/*     */ import javax.swing.LookAndFeel;
/*     */ import javax.swing.SwingUtilities;
/*     */ import javax.swing.border.Border;
/*     */ import javax.swing.plaf.BorderUIResource;
/*     */ import javax.swing.plaf.ComponentUI;
/*     */ import javax.swing.plaf.UIResource;
/*     */ import org.jdesktop.swingx.JXStatusBar;
/*     */ import org.jdesktop.swingx.JXStatusBar.Constraint;
/*     */ import org.jdesktop.swingx.JXStatusBar.Constraint.ResizeBehavior;
/*     */ import org.jdesktop.swingx.plaf.StatusBarUI;
/*     */ import org.jdesktop.swingx.plaf.UIManagerExt;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class BasicStatusBarUI
/*     */   extends StatusBarUI
/*     */ {
/*     */   private class Handler
/*     */     implements MouseListener, MouseMotionListener, PropertyChangeListener
/*     */   {
/*  66 */     private Window window = SwingUtilities.getWindowAncestor(BasicStatusBarUI.this.statusBar);
/*  67 */     private int handleBoundary = getHandleBoundary();
/*  68 */     private boolean validPress = false;
/*     */     
/*     */     private Handler() {}
/*     */     
/*  72 */     private int getHandleBoundary() { Border border = BasicStatusBarUI.this.statusBar.getBorder();
/*     */       
/*  74 */       if ((border == null) || (!BasicStatusBarUI.this.statusBar.isResizeHandleEnabled())) {
/*  75 */         return 0;
/*     */       }
/*     */       
/*  78 */       if (BasicStatusBarUI.this.statusBar.getComponentOrientation().isLeftToRight()) {
/*  79 */         return border.getBorderInsets(BasicStatusBarUI.this.statusBar).right;
/*     */       }
/*  81 */       return border.getBorderInsets(BasicStatusBarUI.this.statusBar).left;
/*     */     }
/*     */     
/*     */     private boolean isHandleAreaPoint(Point point)
/*     */     {
/*  86 */       if ((this.window == null) || (this.window.isMaximumSizeSet())) {
/*  87 */         return false;
/*     */       }
/*     */       
/*  90 */       if (BasicStatusBarUI.this.statusBar.getComponentOrientation().isLeftToRight()) {
/*  91 */         return point.x >= BasicStatusBarUI.this.statusBar.getWidth() - this.handleBoundary;
/*     */       }
/*  93 */       return point.x <= this.handleBoundary;
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */     private Point startingPoint;
/*     */     
/*     */ 
/*     */     public void mouseClicked(MouseEvent e) {}
/*     */     
/*     */ 
/*     */ 
/*     */     public void mouseEntered(MouseEvent e)
/*     */     {
/* 108 */       if (isHandleAreaPoint(e.getPoint())) {
/* 109 */         if (BasicStatusBarUI.this.statusBar.getComponentOrientation().isLeftToRight()) {
/* 110 */           this.window.setCursor(Cursor.getPredefinedCursor(5));
/*     */         }
/*     */         else {
/* 113 */           this.window.setCursor(Cursor.getPredefinedCursor(4));
/*     */         }
/*     */       }
/*     */       else {
/* 117 */         this.window.setCursor(null);
/*     */       }
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */     public void mouseExited(MouseEvent e)
/*     */     {
/* 125 */       if (!this.validPress) {
/* 126 */         this.window.setCursor(null);
/*     */       }
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */     public void mousePressed(MouseEvent e)
/*     */     {
/* 134 */       this.validPress = ((SwingUtilities.isLeftMouseButton(e)) && (isHandleAreaPoint(e.getPoint())));
/* 135 */       this.startingPoint = e.getPoint();
/* 136 */       SwingUtilities.convertPointToScreen(this.startingPoint, BasicStatusBarUI.this.statusBar);
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */     public void mouseReleased(MouseEvent e)
/*     */     {
/* 143 */       this.validPress = (!SwingUtilities.isLeftMouseButton(e));
/* 144 */       this.window.validate();
/* 145 */       this.window.setCursor(null);
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */     public void mouseDragged(MouseEvent e)
/*     */     {
/* 152 */       if (this.validPress) {
/* 153 */         Rectangle wb = this.window.getBounds();
/* 154 */         Point p = e.getPoint();
/* 155 */         SwingUtilities.convertPointToScreen(p, BasicStatusBarUI.this.statusBar);
/*     */         
/* 157 */         wb.height += p.y - this.startingPoint.y;
/* 158 */         if (BasicStatusBarUI.this.statusBar.getComponentOrientation().isLeftToRight()) {
/* 159 */           wb.width += p.x - this.startingPoint.x;
/*     */         } else {
/* 161 */           wb.x += p.x - this.startingPoint.x;
/* 162 */           wb.width += this.startingPoint.x - p.x;
/*     */         }
/*     */         
/* 165 */         this.window.setBounds(wb);
/* 166 */         this.window.validate();
/* 167 */         this.startingPoint = p;
/*     */       }
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */     public void mouseMoved(MouseEvent e)
/*     */     {
/* 175 */       if (isHandleAreaPoint(e.getPoint())) {
/* 176 */         if (BasicStatusBarUI.this.statusBar.getComponentOrientation().isLeftToRight()) {
/* 177 */           this.window.setCursor(Cursor.getPredefinedCursor(5));
/*     */         }
/*     */         else {
/* 180 */           this.window.setCursor(Cursor.getPredefinedCursor(4));
/*     */         }
/*     */       }
/*     */       else {
/* 184 */         this.window.setCursor(null);
/*     */       }
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */     public void propertyChange(PropertyChangeEvent evt)
/*     */     {
/* 192 */       if ("ancestor".equals(evt.getPropertyName())) {
/* 193 */         this.window = SwingUtilities.getWindowAncestor(BasicStatusBarUI.this.statusBar);
/*     */         
/* 195 */         boolean useResizeHandle = (BasicStatusBarUI.this.statusBar.getParent() != null) && (BasicStatusBarUI.this.statusBar.getRootPane() != null) && ((BasicStatusBarUI.this.statusBar.getParent() == BasicStatusBarUI.this.statusBar.getRootPane()) || (BasicStatusBarUI.this.statusBar.getParent() == BasicStatusBarUI.this.statusBar.getRootPane().getContentPane()));
/*     */         
/*     */ 
/*     */ 
/* 199 */         BasicStatusBarUI.this.statusBar.setResizeHandleEnabled(useResizeHandle);
/* 200 */       } else if ("border".equals(evt.getPropertyName())) {
/* 201 */         this.handleBoundary = getHandleBoundary();
/* 202 */       } else if ("componentOrientation".equals(evt.getPropertyName())) {
/* 203 */         this.handleBoundary = getHandleBoundary();
/* 204 */       } else if ("resizeHandleEnabled".equals(evt.getPropertyName()))
/*     */       {
/* 206 */         this.handleBoundary = getHandleBoundary();
/*     */       }
/*     */     }
/*     */   }
/*     */   
/* 211 */   public static final String AUTO_ADD_SEPARATOR = "auto-add-separator";
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   private static Insets TEMP_INSETS;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   protected JXStatusBar statusBar;
/*     */   
/*     */ 
/*     */ 
/*     */   protected MouseListener mouseListener;
/*     */   
/*     */ 
/*     */ 
/*     */   protected MouseMotionListener mouseMotionListener;
/*     */   
/*     */ 
/*     */ 
/*     */   protected PropertyChangeListener propertyChangeListener;
/*     */   
/*     */ 
/*     */ 
/*     */   private Handler handler;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public static ComponentUI createUI(JComponent c)
/*     */   {
/* 244 */     return new BasicStatusBarUI();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void installUI(JComponent c)
/*     */   {
/* 252 */     assert ((c instanceof JXStatusBar));
/* 253 */     this.statusBar = ((JXStatusBar)c);
/*     */     
/* 255 */     installDefaults(this.statusBar);
/* 256 */     installListeners(this.statusBar);
/*     */     
/*     */ 
/*     */ 
/* 260 */     LayoutManager m = this.statusBar.getLayout();
/* 261 */     if ((m == null) || ((m instanceof UIResource))) {
/* 262 */       this.statusBar.setLayout(createLayout());
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   protected void installDefaults(JXStatusBar sb)
/*     */   {
/* 271 */     Border b = this.statusBar.getBorder();
/* 272 */     if ((b == null) || ((b instanceof UIResource))) {
/* 273 */       this.statusBar.setBorder(createBorder());
/*     */     }
/*     */     
/* 276 */     LookAndFeel.installProperty(sb, "opaque", Boolean.TRUE);
/*     */   }
/*     */   
/*     */   private Handler getHandler() {
/* 280 */     if (this.handler == null) {
/* 281 */       this.handler = new Handler(null);
/*     */     }
/*     */     
/* 284 */     return this.handler;
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
/*     */   protected MouseListener createMouseListener()
/*     */   {
/* 298 */     return getHandler();
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
/*     */   protected MouseMotionListener createMouseMotionListener()
/*     */   {
/* 312 */     return getHandler();
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
/*     */   protected PropertyChangeListener createPropertyChangeListener()
/*     */   {
/* 326 */     return getHandler();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   protected void installListeners(JXStatusBar sb)
/*     */   {
/* 334 */     if ((this.mouseListener = createMouseListener()) != null) {
/* 335 */       this.statusBar.addMouseListener(this.mouseListener);
/*     */     }
/*     */     
/* 338 */     if ((this.mouseMotionListener = createMouseMotionListener()) != null) {
/* 339 */       this.statusBar.addMouseMotionListener(this.mouseMotionListener);
/*     */     }
/*     */     
/* 342 */     if ((this.propertyChangeListener = createPropertyChangeListener()) != null) {
/* 343 */       this.statusBar.addPropertyChangeListener(this.propertyChangeListener);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void uninstallUI(JComponent c)
/*     */   {
/* 352 */     assert ((c instanceof JXStatusBar));
/*     */     
/* 354 */     uninstallDefaults(this.statusBar);
/* 355 */     uninstallListeners(this.statusBar);
/*     */     
/* 357 */     if ((this.statusBar.getLayout() instanceof UIResource)) {
/* 358 */       this.statusBar.setLayout(null);
/*     */     }
/*     */   }
/*     */   
/*     */   protected void uninstallDefaults(JXStatusBar sb) {
/* 363 */     if ((sb.getBorder() instanceof UIResource)) {
/* 364 */       sb.setBorder(null);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected void uninstallListeners(JXStatusBar sb)
/*     */   {
/* 374 */     if (this.mouseListener != null) {
/* 375 */       this.statusBar.removeMouseListener(this.mouseListener);
/*     */     }
/*     */     
/* 378 */     if (this.mouseMotionListener != null) {
/* 379 */       this.statusBar.removeMouseMotionListener(this.mouseMotionListener);
/*     */     }
/*     */     
/* 382 */     if (this.propertyChangeListener != null) {
/* 383 */       this.statusBar.removePropertyChangeListener(this.propertyChangeListener);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */   public void paint(Graphics g, JComponent c)
/*     */   {
/* 390 */     if (this.statusBar.isOpaque()) {
/* 391 */       Graphics2D g2 = (Graphics2D)g;
/* 392 */       paintBackground(g2, this.statusBar);
/*     */     }
/*     */     
/* 395 */     if (includeSeparators())
/*     */     {
/* 397 */       TEMP_INSETS = getSeparatorInsets(TEMP_INSETS);
/* 398 */       for (int i = 0; i < this.statusBar.getComponentCount() - 1; i++) {
/* 399 */         Component comp = this.statusBar.getComponent(i);
/* 400 */         int x = comp.getX() + comp.getWidth() + TEMP_INSETS.left;
/* 401 */         int y = TEMP_INSETS.top;
/* 402 */         int w = getSeparatorWidth() - TEMP_INSETS.left - TEMP_INSETS.right;
/* 403 */         int h = c.getHeight() - TEMP_INSETS.top - TEMP_INSETS.bottom;
/*     */         
/* 405 */         paintSeparator((Graphics2D)g, this.statusBar, x, y, w, h);
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   protected void paintBackground(Graphics2D g, JXStatusBar bar)
/*     */   {
/* 412 */     if (bar.isOpaque()) {
/* 413 */       g.setColor(bar.getBackground());
/* 414 */       g.fillRect(0, 0, bar.getWidth(), bar.getHeight());
/*     */     }
/*     */   }
/*     */   
/*     */   protected void paintSeparator(Graphics2D g, JXStatusBar bar, int x, int y, int w, int h) {
/* 419 */     Color fg = UIManagerExt.getSafeColor("Separator.foreground", Color.BLACK);
/* 420 */     Color bg = UIManagerExt.getSafeColor("Separator.background", Color.WHITE);
/*     */     
/* 422 */     x += w / 2;
/* 423 */     g.setColor(fg);
/* 424 */     g.drawLine(x, y, x, h);
/*     */     
/* 426 */     g.setColor(bg);
/* 427 */     g.drawLine(x + 1, y, x + 1, h);
/*     */   }
/*     */   
/*     */   protected Insets getSeparatorInsets(Insets insets) {
/* 431 */     if (insets == null) {
/* 432 */       insets = new Insets(0, 0, 0, 0);
/*     */     }
/*     */     
/* 435 */     insets.top = 4;
/* 436 */     insets.left = 4;
/* 437 */     insets.bottom = 2;
/* 438 */     insets.right = 4;
/*     */     
/* 440 */     return insets;
/*     */   }
/*     */   
/*     */   protected int getSeparatorWidth() {
/* 444 */     return 10;
/*     */   }
/*     */   
/*     */   protected boolean includeSeparators() {
/* 448 */     Boolean b = (Boolean)this.statusBar.getClientProperty(AUTO_ADD_SEPARATOR);
/* 449 */     return (b == null) || (b.booleanValue());
/*     */   }
/*     */   
/*     */   protected BorderUIResource createBorder() {
/* 453 */     return new BorderUIResource(BorderFactory.createEmptyBorder(4, 5, 4, 22));
/*     */   }
/*     */   
/*     */ 
/*     */   protected LayoutManager createLayout()
/*     */   {
/* 459 */     new LayoutManager2() {
/* 460 */       private Map<Component, JXStatusBar.Constraint> constraints = new HashMap();
/*     */       
/* 462 */       public void addLayoutComponent(String name, Component comp) { addLayoutComponent(comp, null); }
/* 463 */       public void removeLayoutComponent(Component comp) { this.constraints.remove(comp); }
/* 464 */       public Dimension minimumLayoutSize(Container parent) { return preferredLayoutSize(parent); }
/* 465 */       public Dimension maximumLayoutSize(Container target) { return new Dimension(Integer.MAX_VALUE, Integer.MAX_VALUE); }
/* 466 */       public float getLayoutAlignmentX(Container target) { return 0.5F; }
/* 467 */       public float getLayoutAlignmentY(Container target) { return 0.5F; }
/*     */       
/*     */       public void invalidateLayout(Container target) {}
/*     */       
/*     */       public void addLayoutComponent(Component comp, Object constraint) {
/* 472 */         if ((constraint instanceof Insets)) {
/* 473 */           constraint = new JXStatusBar.Constraint((Insets)constraint);
/* 474 */         } else if ((constraint instanceof JXStatusBar.Constraint.ResizeBehavior)) {
/* 475 */           constraint = new JXStatusBar.Constraint((JXStatusBar.Constraint.ResizeBehavior)constraint);
/*     */         }
/*     */         
/* 478 */         this.constraints.put(comp, (JXStatusBar.Constraint)constraint);
/*     */       }
/*     */       
/*     */       public Dimension preferredLayoutSize(Container parent) {
/* 482 */         Dimension prefSize = new Dimension();
/* 483 */         int count = 0;
/* 484 */         for (Component comp : this.constraints.keySet()) {
/* 485 */           JXStatusBar.Constraint c = (JXStatusBar.Constraint)this.constraints.get(comp);
/* 486 */           Dimension d = comp.getPreferredSize();
/* 487 */           int prefWidth = 0;
/* 488 */           if (c != null) {
/* 489 */             Insets i = c.getInsets();
/* 490 */             d.width += i.left + i.right;
/* 491 */             d.height += i.top + i.bottom;
/* 492 */             prefWidth = c.getFixedWidth();
/*     */           }
/* 494 */           prefSize.height = Math.max(prefSize.height, d.height);
/* 495 */           prefSize.width += Math.max(d.width, prefWidth);
/*     */           
/*     */ 
/*     */ 
/* 499 */           count++;
/* 500 */           if ((BasicStatusBarUI.this.includeSeparators()) && (this.constraints.size() < count)) {
/* 501 */             prefSize.width += BasicStatusBarUI.this.getSeparatorWidth();
/*     */           }
/*     */         }
/*     */         
/* 505 */         Insets insets = parent.getInsets();
/* 506 */         prefSize.height += insets.top + insets.bottom;
/* 507 */         prefSize.width += insets.left + insets.right;
/* 508 */         return prefSize;
/*     */       }
/*     */       
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       public void layoutContainer(Container parent)
/*     */       {
/* 527 */         Insets parentInsets = parent.getInsets();
/*     */         
/* 529 */         int availableWidth = parent.getWidth() - parentInsets.left - parentInsets.right;
/* 530 */         if (BasicStatusBarUI.this.includeSeparators())
/*     */         {
/* 532 */           availableWidth -= (parent.getComponentCount() - 1) * BasicStatusBarUI.this.getSeparatorWidth();
/*     */         }
/*     */         
/*     */ 
/*     */ 
/*     */ 
/* 538 */         int[] preferredWidths = new int[parent.getComponentCount()];
/* 539 */         int sumPreferredWidths = 0;
/* 540 */         for (int i = 0; i < preferredWidths.length; i++) {
/* 541 */           preferredWidths[i] = getPreferredWidth(parent.getComponent(i));
/* 542 */           sumPreferredWidths += preferredWidths[i];
/*     */         }
/*     */         
/*     */ 
/*     */ 
/*     */ 
/* 548 */         if (availableWidth > sumPreferredWidths)
/*     */         {
/* 550 */           int numFilledComponents = 0;
/* 551 */           for (Component comp : parent.getComponents()) {
/* 552 */             JXStatusBar.Constraint c = (JXStatusBar.Constraint)this.constraints.get(comp);
/* 553 */             if ((c != null) && (c.getResizeBehavior() == JXStatusBar.Constraint.ResizeBehavior.FILL)) {
/* 554 */               numFilledComponents++;
/*     */             }
/*     */           }
/*     */           
/* 558 */           if (numFilledComponents > 0)
/*     */           {
/* 560 */             availableWidth -= sumPreferredWidths;
/* 561 */             double weight = 1.0D / numFilledComponents;
/* 562 */             int share = (int)(availableWidth * weight);
/* 563 */             int remaining = numFilledComponents;
/* 564 */             for (int i = 0; i < parent.getComponentCount(); i++) {
/* 565 */               Component comp = parent.getComponent(i);
/* 566 */               JXStatusBar.Constraint c = (JXStatusBar.Constraint)this.constraints.get(comp);
/* 567 */               if ((c != null) && (c.getResizeBehavior() == JXStatusBar.Constraint.ResizeBehavior.FILL)) {
/* 568 */                 if (remaining > 1) {
/* 569 */                   preferredWidths[i] += share;
/* 570 */                   availableWidth -= share;
/*     */                 } else {
/* 572 */                   preferredWidths[i] += availableWidth;
/*     */                 }
/* 574 */                 remaining--;
/*     */               }
/*     */             }
/*     */           }
/*     */         }
/*     */         
/*     */ 
/* 581 */         int nextX = parentInsets.left;
/* 582 */         int height = parent.getHeight() - parentInsets.top - parentInsets.bottom;
/* 583 */         for (int i = 0; i < parent.getComponentCount(); i++) {
/* 584 */           Component comp = parent.getComponent(i);
/* 585 */           JXStatusBar.Constraint c = (JXStatusBar.Constraint)this.constraints.get(comp);
/* 586 */           Insets insets = c == null ? new Insets(0, 0, 0, 0) : c.getInsets();
/* 587 */           int width = preferredWidths[i] - (insets.left + insets.right);
/* 588 */           int x = nextX + insets.left;
/* 589 */           int y = parentInsets.top + insets.top;
/* 590 */           comp.setSize(width, height);
/* 591 */           comp.setLocation(x, y);
/* 592 */           nextX = x + width + insets.right;
/*     */           
/*     */ 
/* 595 */           if ((BasicStatusBarUI.this.includeSeparators()) && (i < parent.getComponentCount() - 1)) {
/* 596 */             nextX += BasicStatusBarUI.this.getSeparatorWidth();
/*     */           }
/*     */         }
/*     */       }
/*     */       
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       private int getPreferredWidth(Component comp)
/*     */       {
/* 607 */         JXStatusBar.Constraint c = (JXStatusBar.Constraint)this.constraints.get(comp);
/* 608 */         if (c == null) {
/* 609 */           return comp.getPreferredSize().width;
/*     */         }
/* 611 */         Insets insets = c.getInsets();
/* 612 */         assert (insets != null);
/* 613 */         if (c.getFixedWidth() <= 0) {
/* 614 */           return comp.getPreferredSize().width + insets.left + insets.right;
/*     */         }
/* 616 */         return c.getFixedWidth() + insets.left + insets.right;
/*     */       }
/*     */     };
/*     */   }
/*     */ }


/* Location:              E:\java\java学习\hutubill\lib\all.jar!\org\jdesktop\swingx\plaf\basic\BasicStatusBarUI.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */
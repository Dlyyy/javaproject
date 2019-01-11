/*      */ package com.birosoft.liquid;
/*      */ 
/*      */ import java.awt.Component;
/*      */ import java.awt.Container;
/*      */ import java.awt.Cursor;
/*      */ import java.awt.Dialog;
/*      */ import java.awt.Dimension;
/*      */ import java.awt.Frame;
/*      */ import java.awt.Insets;
/*      */ import java.awt.LayoutManager;
/*      */ import java.awt.LayoutManager2;
/*      */ import java.awt.Point;
/*      */ import java.awt.Rectangle;
/*      */ import java.awt.Toolkit;
/*      */ import java.awt.Window;
/*      */ import java.awt.event.MouseEvent;
/*      */ import java.beans.PropertyChangeEvent;
/*      */ import javax.swing.JComponent;
/*      */ import javax.swing.JLayeredPane;
/*      */ import javax.swing.JMenuBar;
/*      */ import javax.swing.JRootPane;
/*      */ import javax.swing.LookAndFeel;
/*      */ import javax.swing.SwingUtilities;
/*      */ import javax.swing.event.MouseInputListener;
/*      */ import javax.swing.plaf.ComponentUI;
/*      */ import javax.swing.plaf.basic.BasicRootPaneUI;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ public class LiquidRootPaneUI
/*      */   extends BasicRootPaneUI
/*      */ {
/*   61 */   private static final String[] borderKeys = { null, "RootPane.frameBorder", "RootPane.plainDialogBorder", "RootPane.informationDialogBorder", "RootPane.errorDialogBorder", "RootPane.colorChooserDialogBorder", "RootPane.fileChooserDialogBorder", "RootPane.questionDialogBorder", "RootPane.warningDialogBorder" };
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   private static final int CORNER_DRAG_WIDTH = 16;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   private static final int BORDER_DRAG_THICKNESS = 5;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*   83 */   private static final int[] cursorMapping = { 6, 6, 8, 7, 7, 6, 0, 0, 0, 7, 10, 0, 0, 0, 11, 4, 0, 0, 0, 5, 4, 4, 9, 5, 5 };
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   private Window window;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   private JComponent titlePane;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   private MouseInputListener mouseInputListener;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   private LayoutManager layoutManager;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   private LayoutManager savedOldLayout;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   private JRootPane root;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   private Cursor lastCursor;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public LiquidRootPaneUI()
/*      */   {
/*  132 */     this.lastCursor = Cursor.getPredefinedCursor(0);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public static ComponentUI createUI(JComponent c)
/*      */   {
/*  141 */     return new LiquidRootPaneUI();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void installUI(JComponent c)
/*      */   {
/*  159 */     super.installUI(c);
/*  160 */     this.root = ((JRootPane)c);
/*      */     
/*  162 */     int style = this.root.getWindowDecorationStyle();
/*      */     
/*  164 */     if (style != 0) {
/*  165 */       installClientDecorations(this.root);
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void uninstallUI(JComponent c)
/*      */   {
/*  181 */     super.uninstallUI(c);
/*  182 */     uninstallClientDecorations(this.root);
/*      */     
/*  184 */     this.layoutManager = null;
/*  185 */     this.mouseInputListener = null;
/*  186 */     this.root = null;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   void installBorder(JRootPane root)
/*      */   {
/*  194 */     int style = root.getWindowDecorationStyle();
/*      */     
/*  196 */     if (style == 0) {
/*  197 */       LookAndFeel.uninstallBorder(root);
/*      */     }
/*      */     else {
/*  200 */       LookAndFeel.installBorder(root, borderKeys[style]);
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   private void uninstallBorder(JRootPane root)
/*      */   {
/*  208 */     LookAndFeel.uninstallBorder(root);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   private void installWindowListeners(JRootPane root, Component parent)
/*      */   {
/*  222 */     if ((parent instanceof Window)) {
/*  223 */       this.window = ((Window)parent);
/*      */     } else {
/*  225 */       this.window = SwingUtilities.getWindowAncestor(parent);
/*      */     }
/*      */     
/*  228 */     if (this.window != null) {
/*  229 */       if (this.mouseInputListener == null) {
/*  230 */         this.mouseInputListener = createWindowMouseInputListener(root);
/*      */       }
/*      */       
/*  233 */       this.window.addMouseListener(this.mouseInputListener);
/*  234 */       this.window.addMouseMotionListener(this.mouseInputListener);
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   private void uninstallWindowListeners(JRootPane root)
/*      */   {
/*  243 */     if (this.window != null) {
/*  244 */       this.window.removeMouseListener(this.mouseInputListener);
/*  245 */       this.window.removeMouseMotionListener(this.mouseInputListener);
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   private void installLayout(JRootPane root)
/*      */   {
/*  254 */     if (this.layoutManager == null) {
/*  255 */       this.layoutManager = createLayoutManager();
/*      */     }
/*      */     
/*  258 */     this.savedOldLayout = root.getLayout();
/*  259 */     root.setLayout(this.layoutManager);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   private void uninstallLayout(JRootPane root)
/*      */   {
/*  266 */     if (this.savedOldLayout != null) {
/*  267 */       root.setLayout(this.savedOldLayout);
/*  268 */       this.savedOldLayout = null;
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   private void installClientDecorations(JRootPane root)
/*      */   {
/*  278 */     installBorder(root);
/*      */     
/*  280 */     JComponent titlePane = createTitlePane(root);
/*      */     
/*  282 */     setTitlePane(root, titlePane);
/*  283 */     installWindowListeners(root, root.getParent());
/*  284 */     installLayout(root);
/*      */     
/*  286 */     if (this.window != null) {
/*  287 */       root.revalidate();
/*  288 */       root.repaint();
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   private void uninstallClientDecorations(JRootPane root)
/*      */   {
/*  300 */     uninstallBorder(root);
/*  301 */     uninstallWindowListeners(root);
/*  302 */     setTitlePane(root, null);
/*  303 */     uninstallLayout(root);
/*  304 */     root.repaint();
/*  305 */     root.revalidate();
/*      */     
/*      */ 
/*  308 */     if (this.window != null) {
/*  309 */       this.window.setCursor(Cursor.getPredefinedCursor(0));
/*      */     }
/*      */     
/*  312 */     this.window = null;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   private JComponent createTitlePane(JRootPane root)
/*      */   {
/*  320 */     return new LiquidTitlePane(root, this);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   private MouseInputListener createWindowMouseInputListener(JRootPane root)
/*      */   {
/*  328 */     return new MouseInputHandler(null);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   private LayoutManager createLayoutManager()
/*      */   {
/*  336 */     return new MetalRootLayout(null);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   private void setTitlePane(JRootPane root, JComponent titlePane)
/*      */   {
/*  349 */     JLayeredPane layeredPane = root.getLayeredPane();
/*  350 */     JComponent oldTitlePane = getTitlePane();
/*      */     
/*  352 */     if (oldTitlePane != null) {
/*  353 */       oldTitlePane.setVisible(false);
/*  354 */       layeredPane.remove(oldTitlePane);
/*      */     }
/*      */     
/*  357 */     if (titlePane != null) {
/*  358 */       layeredPane.add(titlePane, JLayeredPane.FRAME_CONTENT_LAYER);
/*  359 */       titlePane.setVisible(true);
/*      */     }
/*      */     
/*  362 */     this.titlePane = titlePane;
/*  363 */     root.validate();
/*  364 */     root.repaint();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   private JComponent getTitlePane()
/*      */   {
/*  375 */     return this.titlePane;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   private JRootPane getRootPane()
/*      */   {
/*  383 */     return this.root;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void propertyChange(PropertyChangeEvent e)
/*      */   {
/*  405 */     super.propertyChange(e);
/*      */     
/*  407 */     String propertyName = e.getPropertyName();
/*      */     
/*  409 */     if (propertyName == null) {
/*  410 */       return;
/*      */     }
/*      */     
/*  413 */     if (propertyName.equals("windowDecorationStyle")) {
/*  414 */       JRootPane root = (JRootPane)e.getSource();
/*  415 */       int style = root.getWindowDecorationStyle();
/*      */       
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  421 */       uninstallClientDecorations(root);
/*      */       
/*  423 */       if (style != 0) {
/*  424 */         installClientDecorations(root);
/*      */       }
/*  426 */     } else if (propertyName.equals("ancestor")) {
/*  427 */       uninstallWindowListeners(this.root);
/*      */       
/*  429 */       if (((JRootPane)e.getSource()).getWindowDecorationStyle() != 0) {
/*  430 */         installWindowListeners(this.root, this.root.getParent());
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   private static class MetalRootLayout
/*      */     implements LayoutManager2
/*      */   {
/*      */     MetalRootLayout(LiquidRootPaneUI.1 x0)
/*      */     {
/*  445 */       this();
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     public Dimension preferredLayoutSize(Container parent)
/*      */     {
/*  456 */       int cpWidth = 0;
/*  457 */       int cpHeight = 0;
/*  458 */       int mbWidth = 0;
/*  459 */       int mbHeight = 0;
/*  460 */       int tpWidth = 0;
/*  461 */       int tpHeight = 0;
/*  462 */       Insets i = parent.getInsets();
/*  463 */       JRootPane root = (JRootPane)parent;
/*      */       Dimension cpd;
/*  465 */       Dimension cpd; if (root.getContentPane() != null) {
/*  466 */         cpd = root.getContentPane().getPreferredSize();
/*      */       } else {
/*  468 */         cpd = root.getSize();
/*      */       }
/*      */       
/*  471 */       if (cpd != null) {
/*  472 */         cpWidth = cpd.width;
/*  473 */         cpHeight = cpd.height;
/*      */       }
/*      */       
/*  476 */       if (root.getJMenuBar() != null) {
/*  477 */         Dimension mbd = root.getJMenuBar().getPreferredSize();
/*      */         
/*  479 */         if (mbd != null) {
/*  480 */           mbWidth = mbd.width;
/*  481 */           mbHeight = mbd.height;
/*      */         }
/*      */       }
/*      */       
/*  485 */       if ((root.getWindowDecorationStyle() != 0) && ((root.getUI() instanceof LiquidRootPaneUI)))
/*      */       {
/*  487 */         JComponent titlePane = ((LiquidRootPaneUI)root.getUI()).getTitlePane();
/*      */         
/*  489 */         if (titlePane != null) {
/*  490 */           Dimension tpd = titlePane.getPreferredSize();
/*      */           
/*  492 */           if (tpd != null) {
/*  493 */             tpWidth = tpd.width;
/*  494 */             tpHeight = tpd.height;
/*      */           }
/*      */         }
/*      */       }
/*      */       
/*  499 */       return new Dimension(Math.max(Math.max(cpWidth, mbWidth), tpWidth) + i.left + i.right, cpHeight + mbHeight + tpWidth + i.top + i.bottom);
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     public Dimension minimumLayoutSize(Container parent)
/*      */     {
/*  514 */       int cpWidth = 0;
/*  515 */       int cpHeight = 0;
/*  516 */       int mbWidth = 0;
/*  517 */       int mbHeight = 0;
/*  518 */       int tpWidth = 0;
/*  519 */       int tpHeight = 0;
/*  520 */       Insets i = parent.getInsets();
/*  521 */       JRootPane root = (JRootPane)parent;
/*      */       Dimension cpd;
/*  523 */       Dimension cpd; if (root.getContentPane() != null) {
/*  524 */         cpd = root.getContentPane().getMinimumSize();
/*      */       } else {
/*  526 */         cpd = root.getSize();
/*      */       }
/*      */       
/*  529 */       if (cpd != null) {
/*  530 */         cpWidth = cpd.width;
/*  531 */         cpHeight = cpd.height;
/*      */       }
/*      */       
/*  534 */       if (root.getJMenuBar() != null) {
/*  535 */         Dimension mbd = root.getJMenuBar().getMinimumSize();
/*      */         
/*  537 */         if (mbd != null) {
/*  538 */           mbWidth = mbd.width;
/*  539 */           mbHeight = mbd.height;
/*      */         }
/*      */       }
/*      */       
/*  543 */       if ((root.getWindowDecorationStyle() != 0) && ((root.getUI() instanceof LiquidRootPaneUI)))
/*      */       {
/*  545 */         JComponent titlePane = ((LiquidRootPaneUI)root.getUI()).getTitlePane();
/*      */         
/*  547 */         if (titlePane != null) {
/*  548 */           Dimension tpd = titlePane.getMinimumSize();
/*      */           
/*  550 */           if (tpd != null) {
/*  551 */             tpWidth = tpd.width;
/*  552 */             tpHeight = tpd.height;
/*      */           }
/*      */         }
/*      */       }
/*      */       
/*  557 */       return new Dimension(Math.max(Math.max(cpWidth, mbWidth), tpWidth) + i.left + i.right, cpHeight + mbHeight + tpWidth + i.top + i.bottom);
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     public Dimension maximumLayoutSize(Container target)
/*      */     {
/*  572 */       int cpWidth = Integer.MAX_VALUE;
/*  573 */       int cpHeight = Integer.MAX_VALUE;
/*  574 */       int mbWidth = Integer.MAX_VALUE;
/*  575 */       int mbHeight = Integer.MAX_VALUE;
/*  576 */       int tpWidth = Integer.MAX_VALUE;
/*  577 */       int tpHeight = Integer.MAX_VALUE;
/*  578 */       Insets i = target.getInsets();
/*  579 */       JRootPane root = (JRootPane)target;
/*      */       
/*  581 */       if (root.getContentPane() != null) {
/*  582 */         Dimension cpd = root.getContentPane().getMaximumSize();
/*      */         
/*  584 */         if (cpd != null) {
/*  585 */           cpWidth = cpd.width;
/*  586 */           cpHeight = cpd.height;
/*      */         }
/*      */       }
/*      */       
/*  590 */       if (root.getJMenuBar() != null) {
/*  591 */         Dimension mbd = root.getJMenuBar().getMaximumSize();
/*      */         
/*  593 */         if (mbd != null) {
/*  594 */           mbWidth = mbd.width;
/*  595 */           mbHeight = mbd.height;
/*      */         }
/*      */       }
/*      */       
/*  599 */       if ((root.getWindowDecorationStyle() != 0) && ((root.getUI() instanceof LiquidRootPaneUI)))
/*      */       {
/*  601 */         JComponent titlePane = ((LiquidRootPaneUI)root.getUI()).getTitlePane();
/*      */         
/*  603 */         if (titlePane != null) {
/*  604 */           Dimension tpd = titlePane.getMaximumSize();
/*      */           
/*  606 */           if (tpd != null) {
/*  607 */             tpWidth = tpd.width;
/*  608 */             tpHeight = tpd.height;
/*      */           }
/*      */         }
/*      */       }
/*      */       
/*  613 */       int maxHeight = Math.max(Math.max(cpHeight, mbHeight), tpHeight);
/*      */       
/*      */ 
/*      */ 
/*  617 */       if (maxHeight != Integer.MAX_VALUE) {
/*  618 */         maxHeight = cpHeight + mbHeight + tpHeight + i.top + i.bottom;
/*      */       }
/*      */       
/*  621 */       int maxWidth = Math.max(Math.max(cpWidth, mbWidth), tpWidth);
/*      */       
/*      */ 
/*  624 */       if (maxWidth != Integer.MAX_VALUE) {
/*  625 */         maxWidth += i.left + i.right;
/*      */       }
/*      */       
/*  628 */       return new Dimension(maxWidth, maxHeight);
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     public void layoutContainer(Container parent)
/*      */     {
/*  638 */       JRootPane root = (JRootPane)parent;
/*  639 */       Rectangle b = root.getBounds();
/*  640 */       Insets i = root.getInsets();
/*  641 */       int nextY = 0;
/*  642 */       int w = b.width - i.right - i.left;
/*  643 */       int h = b.height - i.top - i.bottom;
/*      */       
/*  645 */       if (root.getLayeredPane() != null) {
/*  646 */         root.getLayeredPane().setBounds(i.left, i.top, w, h);
/*      */       }
/*      */       
/*  649 */       if (root.getGlassPane() != null) {
/*  650 */         root.getGlassPane().setBounds(i.left, i.top, w, h);
/*      */       }
/*      */       
/*      */ 
/*      */ 
/*  655 */       if ((root.getWindowDecorationStyle() != 0) && ((root.getUI() instanceof LiquidRootPaneUI)))
/*      */       {
/*  657 */         JComponent titlePane = ((LiquidRootPaneUI)root.getUI()).getTitlePane();
/*      */         
/*  659 */         if (titlePane != null) {
/*  660 */           Dimension tpd = titlePane.getPreferredSize();
/*      */           
/*  662 */           if (tpd != null) {
/*  663 */             int tpHeight = tpd.height;
/*  664 */             titlePane.setBounds(0, 0, w, tpHeight);
/*  665 */             nextY += tpHeight;
/*      */           }
/*      */         }
/*      */       }
/*      */       
/*  670 */       if (root.getJMenuBar() != null) {
/*  671 */         Dimension mbd = root.getJMenuBar().getPreferredSize();
/*  672 */         root.getJMenuBar().setBounds(0, nextY, w, mbd.height);
/*  673 */         nextY += mbd.height;
/*      */       }
/*      */       
/*  676 */       if (root.getContentPane() != null) {
/*  677 */         Dimension cpd = root.getContentPane().getPreferredSize();
/*  678 */         root.getContentPane().setBounds(0, nextY, w, h < nextY ? 0 : h - nextY);
/*      */       }
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     public float getLayoutAlignmentX(Container target)
/*      */     {
/*  693 */       return 0.0F;
/*      */     }
/*      */     
/*      */ 
/*  697 */     public float getLayoutAlignmentY(Container target) { return 0.0F; }
/*      */     
/*      */     private MetalRootLayout() {}
/*      */     
/*      */     public void addLayoutComponent(String name, Component comp) {}
/*      */     
/*      */     public void removeLayoutComponent(Component comp) {}
/*      */     
/*      */     public void addLayoutComponent(Component comp, Object constraints) {}
/*      */     
/*      */     public void invalidateLayout(Container target) {} }
/*      */   
/*  709 */   private class MouseInputHandler implements MouseInputListener { MouseInputHandler(LiquidRootPaneUI.1 x1) { this(); }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     private boolean isMovingWindow;
/*      */     
/*      */ 
/*      */ 
/*      */     private int dragCursor;
/*      */     
/*      */ 
/*      */ 
/*      */     private int dragOffsetX;
/*      */     
/*      */ 
/*      */ 
/*      */     private int dragOffsetY;
/*      */     
/*      */ 
/*      */ 
/*      */     private int dragWidth;
/*      */     
/*      */ 
/*      */ 
/*      */     private int dragHeight;
/*      */     
/*      */ 
/*      */ 
/*      */     public void mousePressed(MouseEvent ev)
/*      */     {
/*  741 */       JRootPane rootPane = LiquidRootPaneUI.this.getRootPane();
/*      */       
/*  743 */       if (rootPane.getWindowDecorationStyle() == 0) {
/*  744 */         return;
/*      */       }
/*      */       
/*  747 */       Point dragWindowOffset = ev.getPoint();
/*  748 */       Window w = (Window)ev.getSource();
/*  749 */       Point convertedDragWindowOffset = SwingUtilities.convertPoint(w, dragWindowOffset, LiquidRootPaneUI.this.getTitlePane());
/*      */       
/*      */ 
/*  752 */       Frame f = null;
/*  753 */       Dialog d = null;
/*      */       
/*  755 */       if ((w instanceof Frame)) {
/*  756 */         f = (Frame)w;
/*  757 */       } else if ((w instanceof Dialog)) {
/*  758 */         d = (Dialog)w;
/*      */       }
/*      */       
/*  761 */       int frameState = f != null ? f.getExtendedState() : 0;
/*      */       
/*  763 */       if ((LiquidRootPaneUI.this.getTitlePane() != null) && (LiquidRootPaneUI.this.getTitlePane().contains(convertedDragWindowOffset)))
/*      */       {
/*  765 */         if ((ev.getClickCount() == 2) && 
/*  766 */           (f != null) && (f.isResizable())) {
/*  767 */           if (((frameState & 0x2) == 2) || ((frameState & 0x4) == 4))
/*      */           {
/*  769 */             f.setExtendedState(frameState & 0xFFFFFFF9);
/*      */           }
/*      */           else {
/*  772 */             f.setExtendedState(frameState | 0x6);
/*      */           }
/*      */           
/*      */ 
/*  776 */           return;
/*      */         }
/*      */         
/*      */ 
/*  780 */         if (((f != null) && ((frameState & 0x2) != 2) && ((frameState & 0x4) != 4)) || ((d != null) && (dragWindowOffset.y >= 5) && (dragWindowOffset.x >= 5) && (dragWindowOffset.x < w.getWidth() - 5)))
/*      */         {
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  788 */           this.isMovingWindow = true;
/*  789 */           this.dragOffsetX = dragWindowOffset.x;
/*  790 */           this.dragOffsetY = dragWindowOffset.y;
/*      */         }
/*  792 */       } else if (((f != null) && (f.isResizable()) && ((frameState & 0x2) != 2) && ((frameState & 0x4) != 4)) || ((d != null) && (d.isResizable())))
/*      */       {
/*      */ 
/*      */ 
/*  796 */         this.dragOffsetX = dragWindowOffset.x;
/*  797 */         this.dragOffsetY = dragWindowOffset.y;
/*  798 */         this.dragWidth = w.getWidth();
/*  799 */         this.dragHeight = w.getHeight();
/*  800 */         this.dragCursor = getCursor(calculateCorner(w, dragWindowOffset.x, dragWindowOffset.y));
/*      */       }
/*      */     }
/*      */     
/*      */     public void mouseReleased(MouseEvent ev)
/*      */     {
/*  806 */       if ((this.dragCursor != 0) && (LiquidRootPaneUI.this.window != null) && (!LiquidRootPaneUI.this.window.isValid()))
/*      */       {
/*      */ 
/*  809 */         LiquidRootPaneUI.this.window.validate();
/*  810 */         LiquidRootPaneUI.this.getRootPane().repaint();
/*      */       }
/*      */       
/*  813 */       this.isMovingWindow = false;
/*  814 */       this.dragCursor = 0;
/*      */     }
/*      */     
/*      */     public void mouseMoved(MouseEvent ev) {
/*  818 */       JRootPane root = LiquidRootPaneUI.this.getRootPane();
/*      */       
/*  820 */       if (root.getWindowDecorationStyle() == 0) {
/*  821 */         return;
/*      */       }
/*      */       
/*  824 */       Window w = (Window)ev.getSource();
/*      */       
/*  826 */       Frame f = null;
/*  827 */       Dialog d = null;
/*      */       
/*  829 */       if ((w instanceof Frame)) {
/*  830 */         f = (Frame)w;
/*  831 */       } else if ((w instanceof Dialog)) {
/*  832 */         d = (Dialog)w;
/*      */       }
/*      */       
/*      */ 
/*  836 */       int cursor = getCursor(calculateCorner(w, ev.getX(), ev.getY()));
/*      */       
/*  838 */       if ((cursor != 0) && (((f != null) && (f.isResizable()) && ((f.getExtendedState() & 0x4) != 4) && ((f.getExtendedState() & 0x2) != 2)) || ((d != null) && (d.isResizable()))))
/*      */       {
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  844 */         w.setCursor(Cursor.getPredefinedCursor(cursor));
/*      */       } else {
/*  846 */         w.setCursor(LiquidRootPaneUI.this.lastCursor);
/*      */       }
/*      */     }
/*      */     
/*      */     private void adjust(Rectangle bounds, Dimension min, int deltaX, int deltaY, int deltaWidth, int deltaHeight)
/*      */     {
/*  852 */       bounds.x += deltaX;
/*  853 */       bounds.y += deltaY;
/*  854 */       bounds.width += deltaWidth;
/*  855 */       bounds.height += deltaHeight;
/*      */       
/*  857 */       if (min != null) {
/*  858 */         if (bounds.width < min.width) {
/*  859 */           int correction = min.width - bounds.width;
/*      */           
/*  861 */           if (deltaX != 0) {
/*  862 */             bounds.x -= correction;
/*      */           }
/*      */           
/*  865 */           bounds.width = min.width;
/*      */         }
/*      */         
/*  868 */         if (bounds.height < min.height) {
/*  869 */           int correction = min.height - bounds.height;
/*      */           
/*  871 */           if (deltaY != 0) {
/*  872 */             bounds.y -= correction;
/*      */           }
/*      */           
/*  875 */           bounds.height = min.height;
/*      */         }
/*      */       }
/*      */     }
/*      */     
/*      */     public void mouseDragged(MouseEvent ev) {
/*  881 */       Window w = (Window)ev.getSource();
/*  882 */       Point pt = ev.getPoint();
/*      */       
/*  884 */       if (this.isMovingWindow) {
/*  885 */         Point windowPt = w.getLocationOnScreen();
/*      */         
/*  887 */         windowPt.x += pt.x - this.dragOffsetX;
/*  888 */         windowPt.y += pt.y - this.dragOffsetY;
/*  889 */         w.setLocation(windowPt);
/*  890 */       } else if (this.dragCursor != 0) {
/*  891 */         Rectangle r = w.getBounds();
/*  892 */         Rectangle startBounds = new Rectangle(r);
/*  893 */         Dimension min = w.getMinimumSize();
/*      */         
/*  895 */         switch (this.dragCursor) {
/*      */         case 11: 
/*  897 */           adjust(r, min, 0, 0, pt.x + (this.dragWidth - this.dragOffsetX) - r.width, 0);
/*      */           
/*      */ 
/*  900 */           break;
/*      */         
/*      */         case 9: 
/*  903 */           adjust(r, min, 0, 0, 0, pt.y + (this.dragHeight - this.dragOffsetY) - r.height);
/*      */           
/*      */ 
/*  906 */           break;
/*      */         
/*      */         case 8: 
/*  909 */           adjust(r, min, 0, pt.y - this.dragOffsetY, 0, -(pt.y - this.dragOffsetY));
/*      */           
/*      */ 
/*  912 */           break;
/*      */         
/*      */         case 10: 
/*  915 */           adjust(r, min, pt.x - this.dragOffsetX, 0, -(pt.x - this.dragOffsetX), 0);
/*      */           
/*      */ 
/*  918 */           break;
/*      */         
/*      */         case 7: 
/*  921 */           adjust(r, min, 0, pt.y - this.dragOffsetY, pt.x + (this.dragWidth - this.dragOffsetX) - r.width, -(pt.y - this.dragOffsetY));
/*      */           
/*      */ 
/*      */ 
/*  925 */           break;
/*      */         
/*      */         case 5: 
/*  928 */           adjust(r, min, 0, 0, pt.x + (this.dragWidth - this.dragOffsetX) - r.width, pt.y + (this.dragHeight - this.dragOffsetY) - r.height);
/*      */           
/*      */ 
/*      */ 
/*  932 */           break;
/*      */         
/*      */         case 6: 
/*  935 */           adjust(r, min, pt.x - this.dragOffsetX, pt.y - this.dragOffsetY, -(pt.x - this.dragOffsetX), -(pt.y - this.dragOffsetY));
/*      */           
/*      */ 
/*  938 */           break;
/*      */         
/*      */         case 4: 
/*  941 */           adjust(r, min, pt.x - this.dragOffsetX, 0, -(pt.x - this.dragOffsetX), pt.y + (this.dragHeight - this.dragOffsetY) - r.height);
/*      */           
/*      */ 
/*      */ 
/*  945 */           break;
/*      */         }
/*      */         
/*      */         
/*      */ 
/*      */ 
/*  951 */         if (!r.equals(startBounds)) {
/*  952 */           w.setBounds(r);
/*      */           
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  958 */           if (Toolkit.getDefaultToolkit().isDynamicLayoutActive()) {
/*  959 */             w.validate();
/*  960 */             LiquidRootPaneUI.this.getRootPane().repaint();
/*      */           }
/*      */         }
/*      */       }
/*      */     }
/*      */     
/*      */     public void mouseEntered(MouseEvent ev) {
/*  967 */       Window w = (Window)ev.getSource();
/*  968 */       LiquidRootPaneUI.this.lastCursor = w.getCursor();
/*  969 */       mouseMoved(ev);
/*      */     }
/*      */     
/*      */     public void mouseExited(MouseEvent ev) {
/*  973 */       Window w = (Window)ev.getSource();
/*  974 */       w.setCursor(LiquidRootPaneUI.this.lastCursor);
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     private int calculateCorner(Component c, int x, int y)
/*      */     {
/*  985 */       int xPosition = calculatePosition(x, c.getWidth());
/*  986 */       int yPosition = calculatePosition(y, c.getHeight());
/*      */       
/*  988 */       if ((xPosition == -1) || (yPosition == -1)) {
/*  989 */         return -1;
/*      */       }
/*      */       
/*  992 */       return yPosition * 5 + xPosition;
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */     private int getCursor(int corner)
/*      */     {
/* 1000 */       if (corner == -1) {
/* 1001 */         return 0;
/*      */       }
/*      */       
/* 1004 */       return LiquidRootPaneUI.cursorMapping[corner];
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     private int calculatePosition(int spot, int width)
/*      */     {
/* 1018 */       if (spot < 5) {
/* 1019 */         return 0;
/*      */       }
/*      */       
/* 1022 */       if (spot < 16) {
/* 1023 */         return 1;
/*      */       }
/*      */       
/* 1026 */       if (spot >= width - 5) {
/* 1027 */         return 4;
/*      */       }
/*      */       
/* 1030 */       if (spot >= width - 16) {
/* 1031 */         return 3;
/*      */       }
/*      */       
/* 1034 */       return 2;
/*      */     }
/*      */     
/*      */     private MouseInputHandler() {}
/*      */     
/*      */     public void mouseClicked(MouseEvent ev) {}
/*      */   }
/*      */ }


/* Location:              E:\java\java学习\hutubill\lib\all.jar!\com\birosoft\liquid\LiquidRootPaneUI.class
 * Java compiler version: 4 (48.0)
 * JD-Core Version:       0.7.1
 */
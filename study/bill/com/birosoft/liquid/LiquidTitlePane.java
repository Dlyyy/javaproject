/*      */ package com.birosoft.liquid;
/*      */ 
/*      */ import java.awt.Color;
/*      */ import java.awt.Component;
/*      */ import java.awt.ComponentOrientation;
/*      */ import java.awt.Container;
/*      */ import java.awt.Dialog;
/*      */ import java.awt.Dimension;
/*      */ import java.awt.Font;
/*      */ import java.awt.FontMetrics;
/*      */ import java.awt.Frame;
/*      */ import java.awt.GradientPaint;
/*      */ import java.awt.Graphics;
/*      */ import java.awt.Graphics2D;
/*      */ import java.awt.Image;
/*      */ import java.awt.Insets;
/*      */ import java.awt.LayoutManager;
/*      */ import java.awt.Rectangle;
/*      */ import java.awt.RenderingHints;
/*      */ import java.awt.Toolkit;
/*      */ import java.awt.Window;
/*      */ import java.awt.event.ActionEvent;
/*      */ import java.awt.event.ComponentAdapter;
/*      */ import java.awt.event.ComponentEvent;
/*      */ import java.awt.event.MouseEvent;
/*      */ import java.awt.event.WindowAdapter;
/*      */ import java.awt.event.WindowEvent;
/*      */ import java.awt.event.WindowListener;
/*      */ import java.awt.font.FontRenderContext;
/*      */ import java.awt.font.LineMetrics;
/*      */ import java.beans.PropertyChangeEvent;
/*      */ import java.beans.PropertyChangeListener;
/*      */ import java.util.Locale;
/*      */ import javax.accessibility.AccessibleContext;
/*      */ import javax.swing.AbstractAction;
/*      */ import javax.swing.Action;
/*      */ import javax.swing.Icon;
/*      */ import javax.swing.JButton;
/*      */ import javax.swing.JComponent;
/*      */ import javax.swing.JFrame;
/*      */ import javax.swing.JMenuBar;
/*      */ import javax.swing.JMenuItem;
/*      */ import javax.swing.JPopupMenu;
/*      */ import javax.swing.JRootPane;
/*      */ import javax.swing.SwingUtilities;
/*      */ import javax.swing.UIManager;
/*      */ import javax.swing.border.Border;
/*      */ import javax.swing.plaf.UIResource;
/*      */ 
/*      */ class LiquidTitlePane extends JComponent
/*      */ {
/*   52 */   private static final Border handyEmptyBorder = new javax.swing.border.EmptyBorder(0, 0, 0, 0);
/*      */   private static final int IMAGE_HEIGHT = 16;
/*      */   private static final int IMAGE_WIDTH = 16;
/*      */   private static LiquidWindowButtonUI iconButtonUI;
/*      */   private static LiquidWindowButtonUI maxButtonUI;
/*      */   private static LiquidWindowButtonUI closeButtonUI;
/*      */   private static LiquidWindowButtonUI menuButtonUI;
/*   59 */   private boolean prevState = false;
/*   60 */   private boolean isMenuShowed = false;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*   65 */   Color normalTitleColor = Color.white;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*   70 */   Color shadowColor = new Color(10, 24, 131);
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*   75 */   Color disabledTitleColor = new Color(216, 228, 244);
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   private PropertyChangeListener propertyChangeListener;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   private JMenuBar menuBar;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   private Action closeAction;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   private Action iconifyAction;
/*      */   
/*      */ 
/*      */ 
/*      */   private Action restoreAction;
/*      */   
/*      */ 
/*      */ 
/*      */   private Action maximizeAction;
/*      */   
/*      */ 
/*      */ 
/*      */   private Action menuAction;
/*      */   
/*      */ 
/*      */ 
/*      */   private JButton toggleButton;
/*      */   
/*      */ 
/*      */ 
/*      */   private JButton iconifyButton;
/*      */   
/*      */ 
/*      */ 
/*      */   private JButton closeButton;
/*      */   
/*      */ 
/*      */ 
/*      */   private JButton menuButton;
/*      */   
/*      */ 
/*      */ 
/*      */   private WindowListener windowListener;
/*      */   
/*      */ 
/*      */ 
/*      */   private java.awt.event.ComponentListener windowMoveListener;
/*      */   
/*      */ 
/*      */ 
/*      */   private Window window;
/*      */   
/*      */ 
/*      */ 
/*      */   private JRootPane rootPane;
/*      */   
/*      */ 
/*      */ 
/*      */   private int buttonsWidth;
/*      */   
/*      */ 
/*      */ 
/*      */   private int state;
/*      */   
/*      */ 
/*      */ 
/*      */   private LiquidRootPaneUI rootPaneUI;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public LiquidTitlePane(JRootPane root, LiquidRootPaneUI ui)
/*      */   {
/*  158 */     this.rootPane = root;
/*  159 */     this.rootPaneUI = ui;
/*      */     
/*  161 */     this.state = -1;
/*      */     
/*  163 */     installSubcomponents();
/*  164 */     installDefaults();
/*      */     
/*  166 */     setLayout(createLayout());
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   private void uninstall()
/*      */   {
/*  173 */     uninstallListeners();
/*  174 */     this.window = null;
/*  175 */     removeAll();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   private void installListeners()
/*      */   {
/*  182 */     if (this.window != null) {
/*  183 */       this.windowListener = createWindowListener();
/*  184 */       this.window.addWindowListener(this.windowListener);
/*  185 */       this.propertyChangeListener = createWindowPropertyChangeListener();
/*  186 */       this.window.addPropertyChangeListener(this.propertyChangeListener);
/*  187 */       this.windowMoveListener = new WindowMoveListener();
/*  188 */       this.window.addComponentListener(this.windowMoveListener);
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   private void uninstallListeners()
/*      */   {
/*  196 */     if (this.window != null) {
/*  197 */       this.window.removeWindowListener(this.windowListener);
/*  198 */       this.window.removePropertyChangeListener(this.propertyChangeListener);
/*  199 */       this.window.removeComponentListener(this.windowMoveListener);
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   private WindowListener createWindowListener()
/*      */   {
/*  208 */     return new WindowHandler(null);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   private PropertyChangeListener createWindowPropertyChangeListener()
/*      */   {
/*  216 */     return new PropertyChangeHandler(null);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public JRootPane getRootPane()
/*      */   {
/*  223 */     return this.rootPane;
/*      */   }
/*      */   
/*      */   public static void resetCachedSkins() {
/*  227 */     iconButtonUI = null;
/*  228 */     maxButtonUI = null;
/*  229 */     closeButtonUI = null;
/*  230 */     menuButtonUI = null;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   private int getWindowDecorationStyle()
/*      */   {
/*  239 */     return getRootPane().getWindowDecorationStyle();
/*      */   }
/*      */   
/*      */   public void addNotify() {
/*  243 */     super.addNotify();
/*      */     
/*  245 */     uninstallListeners();
/*      */     
/*  247 */     this.window = SwingUtilities.getWindowAncestor(this);
/*      */     
/*  249 */     if (this.window != null) {
/*  250 */       if ((this.window instanceof Frame)) {
/*  251 */         setState(((Frame)this.window).getExtendedState());
/*      */       } else {
/*  253 */         setState(0);
/*      */       }
/*      */       
/*  256 */       setActive(this.window.isActive());
/*  257 */       installListeners();
/*      */     }
/*      */   }
/*      */   
/*      */   public void removeNotify() {
/*  262 */     super.removeNotify();
/*      */     
/*  264 */     uninstallListeners();
/*  265 */     this.window = null;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   private void installSubcomponents()
/*      */   {
/*  272 */     if (getWindowDecorationStyle() == 1) {
/*  273 */       createActions();
/*      */       
/*  275 */       if (!LiquidLookAndFeel.winDecoPanther) {
/*  276 */         this.menuBar = createMenuBar();
/*  277 */         add(this.menuBar);
/*      */       }
/*      */       
/*  280 */       createButtons();
/*  281 */       add(this.iconifyButton);
/*  282 */       add(this.toggleButton);
/*  283 */       add(this.closeButton);
/*      */       
/*  285 */       if (LiquidLookAndFeel.winDecoPanther) {
/*  286 */         add(this.menuButton);
/*  287 */         this.menuButton.putClientProperty("externalFrameButton", Boolean.TRUE);
/*      */       }
/*      */       
/*  290 */       this.iconifyButton.putClientProperty("externalFrameButton", Boolean.TRUE);
/*  291 */       this.toggleButton.putClientProperty("externalFrameButton", Boolean.TRUE);
/*  292 */       this.closeButton.putClientProperty("externalFrameButton", Boolean.TRUE);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     }
/*  298 */     else if (getWindowDecorationStyle() != 0) {
/*  299 */       createActions();
/*  300 */       createButtons();
/*  301 */       add(this.closeButton);
/*  302 */       this.closeButton.putClientProperty("externalFrameButton", Boolean.FALSE);
/*      */       
/*  304 */       if (LiquidLookAndFeel.winDecoPanther) {
/*  305 */         add(this.menuButton);
/*  306 */         this.menuButton.putClientProperty("externalFrameButton", Boolean.FALSE);
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   private void installDefaults()
/*      */   {
/*  317 */     Font font = LiquidLookAndFeel.winDecoPanther ? UIManager.getFont("InternalFrame.pantherTitleFont") : UIManager.getFont("InternalFrame.titleFont");
/*      */     
/*  319 */     setFont(font);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   private void uninstallDefaults() {}
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   protected JMenuBar createMenuBar()
/*      */   {
/*  333 */     this.menuBar = new SystemMenuBar(createMenu());
/*  334 */     this.menuBar.setFocusable(false);
/*  335 */     this.menuBar.setBorderPainted(true);
/*      */     
/*      */ 
/*  338 */     return this.menuBar;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   private void close()
/*      */   {
/*  345 */     this.isMenuShowed = false;
/*      */     
/*  347 */     Window window = getWindow();
/*      */     
/*  349 */     if (window != null) {
/*  350 */       window.dispatchEvent(new WindowEvent(window, 201));
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   private void iconify()
/*      */   {
/*  359 */     this.isMenuShowed = false;
/*      */     
/*  361 */     Frame frame = getFrame();
/*      */     
/*  363 */     if (frame != null) {
/*  364 */       frame.setExtendedState(this.state | 0x1);
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   private void maximize()
/*      */   {
/*  372 */     this.isMenuShowed = false;
/*      */     
/*  374 */     Frame frame = getFrame();
/*      */     
/*  376 */     if (frame != null) {
/*  377 */       setMaximizeBounds(frame);
/*  378 */       frame.setExtendedState(this.state | 0x6);
/*      */     }
/*      */   }
/*      */   
/*      */   private void setMaximizeBounds(Frame frame) {
/*  383 */     if (frame.getMaximizedBounds() != null) {
/*  384 */       return;
/*      */     }
/*      */     
/*  387 */     Insets screenInsets = Toolkit.getDefaultToolkit().getScreenInsets(frame.getGraphicsConfiguration());
/*  388 */     Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
/*      */     
/*      */ 
/*  391 */     int x = screenInsets.left;
/*  392 */     int y = screenInsets.top;
/*  393 */     int w = screenSize.width - x - screenInsets.right;
/*  394 */     int h = screenSize.height - y - screenInsets.bottom;
/*  395 */     Rectangle maxBounds = new Rectangle(x, y, w, h);
/*  396 */     frame.setMaximizedBounds(maxBounds);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   private void restore()
/*      */   {
/*  403 */     this.isMenuShowed = false;
/*      */     
/*  405 */     Frame frame = getFrame();
/*      */     
/*  407 */     if (frame == null) {
/*  408 */       return;
/*      */     }
/*      */     
/*  411 */     if ((this.state & 0x1) != 0) {
/*  412 */       frame.setExtendedState(this.state & 0xFFFFFFFE);
/*      */     } else {
/*  414 */       frame.setExtendedState(this.state & 0xFFFFFFF9);
/*      */     }
/*      */   }
/*      */   
/*      */   private void showMenu(JPopupMenu systemMenu) {
/*  419 */     if (!this.isMenuShowed) {
/*  420 */       systemMenu.show(this, LiquidLookAndFeel.winDecoPanther ? getWidth() - systemMenu.getPreferredSize().width : 0, 21);
/*  421 */       this.isMenuShowed = true;
/*      */     } else {
/*  423 */       this.isMenuShowed = false;
/*  424 */       systemMenu.setVisible(this.isMenuShowed);
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   private void createActions()
/*      */   {
/*  433 */     this.closeAction = new CloseAction();
/*  434 */     this.iconifyAction = new IconifyAction();
/*  435 */     this.restoreAction = new RestoreAction();
/*  436 */     this.maximizeAction = new MaximizeAction();
/*  437 */     this.menuAction = new MenuAction();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   private JPopupMenu createMenu()
/*      */   {
/*  445 */     JPopupMenu menu = new JPopupMenu();
/*      */     
/*  447 */     if ((getWindowDecorationStyle() == 1) || (getWindowDecorationStyle() == 2))
/*      */     {
/*  449 */       addMenuItems(menu);
/*      */       
/*      */ 
/*  452 */       menu.putClientProperty("isSystemMenu", Boolean.TRUE);
/*      */     }
/*      */     
/*  455 */     return menu;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   private void addMenuItems(JPopupMenu menu)
/*      */   {
/*  462 */     Locale locale = getRootPane().getLocale();
/*  463 */     JMenuItem mi = menu.add(this.restoreAction);
/*  464 */     mi.setMnemonic('r');
/*      */     
/*  466 */     mi = menu.add(this.iconifyAction);
/*  467 */     mi.setMnemonic('e');
/*      */     
/*  469 */     if (Toolkit.getDefaultToolkit().isFrameStateSupported(6)) {
/*  470 */       mi = menu.add(this.maximizeAction);
/*  471 */       mi.setMnemonic('x');
/*      */     }
/*      */     
/*  474 */     menu.addSeparator();
/*      */     
/*  476 */     mi = menu.add(this.closeAction);
/*  477 */     mi.setMnemonic('c');
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   protected void createButtons()
/*      */   {
/*  484 */     if (iconButtonUI == null) {
/*  485 */       iconButtonUI = LiquidWindowButtonUI.createButtonUIForType(2);
/*  486 */       maxButtonUI = LiquidWindowButtonUI.createButtonUIForType(1);
/*  487 */       closeButtonUI = LiquidWindowButtonUI.createButtonUIForType(0);
/*      */       
/*  489 */       if (LiquidLookAndFeel.winDecoPanther) {
/*  490 */         menuButtonUI = LiquidWindowButtonUI.createButtonUIForType(4);
/*      */       }
/*      */     }
/*      */     
/*  494 */     this.iconifyButton = new SpecialUIButton(iconButtonUI, (Window)getRootPane().getParent());
/*      */     
/*  496 */     this.iconifyButton.setAction(this.iconifyAction);
/*  497 */     this.iconifyButton.setRolloverEnabled(true);
/*      */     
/*  499 */     this.toggleButton = new SpecialUIButton(maxButtonUI, (Window)getRootPane().getParent());
/*      */     
/*  501 */     this.toggleButton.setAction(this.maximizeAction);
/*  502 */     this.toggleButton.setRolloverEnabled(true);
/*      */     
/*  504 */     this.closeButton = new SpecialUIButton(closeButtonUI, (Window)getRootPane().getParent());
/*      */     
/*  506 */     this.closeButton.setAction(this.closeAction);
/*  507 */     this.closeButton.setRolloverEnabled(true);
/*      */     
/*  509 */     if (LiquidLookAndFeel.winDecoPanther) {
/*  510 */       this.menuButton = new SpecialUIButton(menuButtonUI, (Window)getRootPane().getParent());
/*      */       
/*  512 */       this.menuButton.setAction(this.menuAction);
/*  513 */       this.menuButton.setRolloverEnabled(true);
/*  514 */       this.menuButton.getAccessibleContext().setAccessibleName("Menu");
/*      */     }
/*      */     
/*  517 */     this.closeButton.getAccessibleContext().setAccessibleName("Close");
/*  518 */     this.iconifyButton.getAccessibleContext().setAccessibleName("Iconify");
/*  519 */     this.toggleButton.getAccessibleContext().setAccessibleName("Maximize");
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   private LayoutManager createLayout()
/*      */   {
/*  527 */     return new TitlePaneLayout(null);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   private void setActive(boolean isActive)
/*      */   {
/*  534 */     if (getWindowDecorationStyle() == 1) {
/*  535 */       Boolean activeB = isActive ? Boolean.TRUE : Boolean.FALSE;
/*      */       
/*  537 */       this.iconifyButton.putClientProperty("paintActive", activeB);
/*  538 */       this.closeButton.putClientProperty("paintActive", activeB);
/*  539 */       this.toggleButton.putClientProperty("paintActive", activeB);
/*      */       
/*  541 */       this.iconifyButton.setEnabled(isActive);
/*  542 */       this.closeButton.setEnabled(isActive);
/*  543 */       this.toggleButton.setEnabled(isActive);
/*      */       
/*  545 */       if (LiquidLookAndFeel.winDecoPanther) {
/*  546 */         this.menuButton.putClientProperty("paintActive", activeB);
/*  547 */         this.menuButton.setEnabled(isActive);
/*      */       }
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*  553 */     getRootPane().repaint();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   private void setState(int state)
/*      */   {
/*  560 */     setState(state, false);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   private void setState(int state, boolean updateRegardless)
/*      */   {
/*  568 */     Window w = getWindow();
/*      */     
/*  570 */     if ((w != null) && ((getWindowDecorationStyle() == 1) || (getWindowDecorationStyle() == 2)))
/*      */     {
/*      */ 
/*  573 */       if ((this.state == state) && (!updateRegardless)) {
/*  574 */         return;
/*      */       }
/*      */       
/*  577 */       Frame frame = getFrame();
/*      */       
/*  579 */       if (frame != null) {
/*  580 */         JRootPane rootPane = getRootPane();
/*      */         
/*  582 */         if ((((state & 0x6) == 6) && ((rootPane.getBorder() == null) || ((rootPane.getBorder() instanceof UIResource))) && (frame.isShowing())) || 
/*      */         
/*      */ 
/*      */ 
/*      */ 
/*  587 */           ((state & 0x6) == 6) || 
/*      */           
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  593 */           (frame.isResizable())) {
/*  594 */           if (((state & 0x4) == 4) || ((state & 0x2) == 2))
/*      */           {
/*  596 */             updateToggleButton(this.restoreAction);
/*  597 */             this.maximizeAction.setEnabled(false);
/*  598 */             this.restoreAction.setEnabled(true);
/*      */           } else {
/*  600 */             updateToggleButton(this.maximizeAction);
/*  601 */             this.maximizeAction.setEnabled(true);
/*  602 */             this.restoreAction.setEnabled(false);
/*      */           }
/*      */           
/*  605 */           if ((this.toggleButton.getParent() == null) || (this.iconifyButton.getParent() == null))
/*      */           {
/*  607 */             add(this.toggleButton);
/*  608 */             add(this.iconifyButton);
/*  609 */             revalidate();
/*  610 */             repaint();
/*      */           }
/*      */           
/*  613 */           this.toggleButton.setText(null);
/*      */         } else {
/*  615 */           this.maximizeAction.setEnabled(false);
/*  616 */           this.restoreAction.setEnabled(false);
/*      */           
/*  618 */           if (this.toggleButton.getParent() != null) {
/*  619 */             remove(this.toggleButton);
/*  620 */             revalidate();
/*  621 */             repaint();
/*      */           }
/*      */         }
/*      */       }
/*      */       else {
/*  626 */         this.maximizeAction.setEnabled(false);
/*  627 */         this.restoreAction.setEnabled(false);
/*  628 */         this.iconifyAction.setEnabled(false);
/*  629 */         remove(this.toggleButton);
/*  630 */         remove(this.iconifyButton);
/*  631 */         revalidate();
/*  632 */         repaint();
/*      */       }
/*      */       
/*  635 */       this.closeAction.setEnabled(true);
/*  636 */       this.state = state;
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   private void updateToggleButton(Action action)
/*      */   {
/*  645 */     this.toggleButton.setAction(action);
/*  646 */     this.toggleButton.setText(null);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   private Frame getFrame()
/*      */   {
/*  654 */     Window window = getWindow();
/*      */     
/*  656 */     if ((window instanceof Frame)) {
/*  657 */       return (Frame)window;
/*      */     }
/*      */     
/*  660 */     return null;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   private Window getWindow()
/*      */   {
/*  669 */     return this.window;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   private String getTitle()
/*      */   {
/*  676 */     Window w = getWindow();
/*      */     
/*  678 */     if ((w instanceof Frame))
/*  679 */       return ((Frame)w).getTitle();
/*  680 */     if ((w instanceof Dialog)) {
/*  681 */       return ((Dialog)w).getTitle();
/*      */     }
/*      */     
/*  684 */     return null;
/*      */   }
/*      */   
/*      */   public boolean isSelected() {
/*  688 */     Window window = getWindow();
/*      */     
/*  690 */     return window == null ? true : window.isActive();
/*      */   }
/*      */   
/*      */   public boolean isFrameMaximized() {
/*  694 */     Frame frame = getFrame();
/*      */     
/*  696 */     if (frame != null) {
/*  697 */       return (this.state & 0x6) == 6;
/*      */     }
/*      */     
/*  700 */     return false;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public void paintComponent(Graphics g)
/*      */   {
/*  707 */     if (getFrame() != null) {
/*  708 */       setState(getFrame().getExtendedState());
/*      */     }
/*      */     
/*  711 */     Window frame = getWindow();
/*      */     
/*  713 */     boolean leftToRight = frame.getComponentOrientation().isLeftToRight();
/*  714 */     boolean isSelected = isSelected();
/*      */     
/*  716 */     if (isSelected) {
/*  717 */       this.prevState = true;
/*      */     }
/*      */     
/*  720 */     if ((!this.prevState) && (!isSelected)) {
/*  721 */       isSelected = true;
/*      */     }
/*      */     
/*  724 */     int width = getWidth();
/*  725 */     int height = getHeight();
/*      */     
/*  727 */     Color foreground = LiquidLookAndFeel.getWindowTitleInactiveForeground();
/*  728 */     Graphics2D g2 = (Graphics2D)g;
/*      */     
/*  730 */     Object oldAntiAliasingValue = g2.getRenderingHint(RenderingHints.KEY_ANTIALIASING);
/*  731 */     g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
/*      */     
/*      */ 
/*      */ 
/*  735 */     if (LiquidLookAndFeel.winDecoPanther) {
/*  736 */       drawPantherCaption(g, isSelected, width, height);
/*      */     } else {
/*  738 */       drawLiquidCaption(g, isSelected, width, height);
/*      */     }
/*      */     
/*  741 */     int titleLength = 0;
/*  742 */     int xOffset = leftToRight ? 2 : width - 2;
/*  743 */     String frameTitle = getTitle();
/*  744 */     int xOvalOffset = 8;
/*      */     
/*      */ 
/*  747 */     xOffset += ((leftToRight) && (!LiquidLookAndFeel.winDecoPanther) ? xOvalOffset : 0);
/*      */     
/*  749 */     if (frameTitle != null) {
/*  750 */       Font f = getFont();
/*  751 */       g.setFont(f);
/*  752 */       int osFontOffset = f.getFamily().equals(LiquidLookAndFeel.fontName) ? 2 : 0;
/*      */       
/*      */ 
/*  755 */       FontMetrics fm = g.getFontMetrics();
/*  756 */       titleLength = fm.stringWidth(frameTitle);
/*      */       
/*  758 */       int titleW = 0;
/*      */       
/*  760 */       Rectangle r = new Rectangle(0, 0, 0, 0);
/*  761 */       if ((leftToRight) && (!LiquidLookAndFeel.winDecoPanther)) {
/*  762 */         if (this.iconifyAction.isEnabled()) {
/*  763 */           r = this.iconifyButton.getBounds();
/*  764 */         } else if (this.maximizeAction.isEnabled()) {
/*  765 */           r = this.toggleButton.getBounds();
/*  766 */         } else if (this.closeAction.isEnabled()) {
/*  767 */           r = this.closeButton.getBounds();
/*      */         }
/*      */         
/*  770 */         if (r.x == 0) {
/*  771 */           r.x = (frame.getWidth() - frame.getInsets().right);
/*      */         }
/*      */         
/*  774 */         xOffset += (this.menuBar != null ? this.menuBar.getX() + this.menuBar.getWidth() + 2 : 2);
/*  775 */         titleW = r.x - xOffset - xOvalOffset - 2;
/*      */       } else {
/*  777 */         if (this.maximizeAction.isEnabled()) {
/*  778 */           r = this.toggleButton.getBounds();
/*  779 */         } else if (this.iconifyAction.isEnabled()) {
/*  780 */           r = this.iconifyButton.getBounds();
/*  781 */         } else if (this.closeAction.isEnabled()) {
/*  782 */           r = this.closeButton.getBounds();
/*      */         }
/*      */         
/*  785 */         Rectangle menu = new Rectangle(0, 0, 0, 0);
/*  786 */         if (this.menuButton != null) {
/*  787 */           menu = this.menuButton.getBounds();
/*      */         }
/*      */         
/*  790 */         if (menu.x == 0) {
/*  791 */           menu.x = (frame.getWidth() - frame.getInsets().right);
/*      */         }
/*  793 */         xOffset = r.x + r.width + 2;
/*  794 */         titleW = menu.x - xOffset;
/*      */       }
/*      */       
/*  797 */       int startTitleLength = fm.stringWidth(frameTitle);
/*  798 */       frameTitle = com.birosoft.liquid.util.LiquidUtilities.clipStringIfNecessary(fm, frameTitle, titleW);
/*  799 */       titleLength = fm.stringWidth(frameTitle);
/*  800 */       if (titleLength == startTitleLength) {
/*  801 */         xOffset += (titleW - titleLength) / 2;
/*      */       }
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
/*  813 */       FontRenderContext frc = g2.getFontRenderContext();
/*  814 */       LineMetrics lm = f.getLineMetrics(frameTitle, frc);
/*      */       
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  822 */       int yOffset = (height - Math.round(lm.getHeight())) / 2 + Math.round(lm.getAscent()) + osFontOffset;
/*  823 */       int endOffset = 19;
/*      */       
/*      */ 
/*      */ 
/*      */ 
/*  828 */       if (!leftToRight) {
/*  829 */         xOffset -= titleLength;
/*      */       }
/*      */       
/*  832 */       if (isSelected)
/*      */       {
/*  834 */         if ((!LiquidLookAndFeel.winDecoPanther) && (titleLength > 0)) {
/*  835 */           GradientPaint grad = new GradientPaint(xOffset + titleLength / 2, yOffset - 15, new Color(60, 144, 233), xOffset + titleLength / 2, endOffset, new Color(102, 186, 255));
/*      */           
/*      */ 
/*      */ 
/*      */ 
/*  840 */           g2.setPaint(grad);
/*      */           
/*  842 */           g2.fillRoundRect(xOffset - 8, yOffset - 15, titleLength + 15, endOffset, 18, 18);
/*      */           
/*  844 */           g.setColor(new Color(0, 78, 167));
/*      */           
/*  846 */           g2.drawRoundRect(xOffset - 8, yOffset - 15, titleLength + 15, endOffset, 18, 18);
/*      */         }
/*      */         
/*      */ 
/*  850 */         if (!LiquidLookAndFeel.winDecoPanther) {
/*  851 */           g.setColor(this.shadowColor);
/*  852 */           g.drawString(frameTitle, xOffset + 1, yOffset);
/*  853 */           g.setColor(this.normalTitleColor);
/*      */         } else {
/*  855 */           g.setColor(Color.black);
/*      */           
/*  857 */           Frame _frame = null;
/*  858 */           Dialog _dialog = null;
/*  859 */           Image image = null;
/*  860 */           Window w = getWindow();
/*      */           
/*  862 */           if ((w instanceof Frame)) {
/*  863 */             _frame = (Frame)w;
/*  864 */             image = _frame.getIconImage();
/*      */           } else {
/*  866 */             _dialog = (Dialog)w;
/*      */             
/*  868 */             if ((_dialog.getParent() != null) && 
/*  869 */               ((_dialog.getParent() instanceof Frame))) {
/*  870 */               image = ((Frame)_dialog.getParent()).getIconImage();
/*      */             }
/*      */           }
/*      */           
/*      */ 
/*  875 */           if (image != null) {
/*  876 */             xOffset += 10;
/*  877 */             g.drawImage(image, xOffset - 20, 3, 16, 16, null);
/*      */           }
/*      */           else {
/*  880 */             Icon icon = UIManager.getIcon("InternalFrame.pantherIcon");
/*      */             
/*      */ 
/*  883 */             if (icon != null) {
/*  884 */               xOffset += 10;
/*  885 */               icon.paintIcon(this, g, xOffset - 20, 3);
/*      */             }
/*      */           }
/*      */         }
/*      */         
/*  890 */         g.drawString(frameTitle, xOffset, yOffset - 1);
/*  891 */         xOffset += (leftToRight ? titleLength + 2 : -2);
/*      */       }
/*      */       else
/*      */       {
/*  895 */         if ((!LiquidLookAndFeel.winDecoPanther) && (titleLength > 0)) {
/*  896 */           GradientPaint grad = new GradientPaint(xOffset + titleLength / 2, yOffset - 15, new Color(191, 211, 233), xOffset + titleLength / 2, endOffset, new Color(233, 253, 255));
/*      */           
/*      */ 
/*      */ 
/*      */ 
/*  901 */           g2.setPaint(grad);
/*      */           
/*      */ 
/*  904 */           g2.fillRoundRect(xOffset - 8, yOffset - 15, titleLength + 15, endOffset, 18, 18);
/*      */           
/*  906 */           g.setColor(new Color(125, 145, 167));
/*      */           
/*      */ 
/*  909 */           g2.drawRoundRect(xOffset - 8, yOffset - 15, titleLength + 15, endOffset, 18, 18);
/*      */         }
/*      */         
/*      */ 
/*  913 */         if (LiquidLookAndFeel.winDecoPanther) {
/*  914 */           Frame _frame = null;
/*  915 */           Dialog _dialog = null;
/*  916 */           Image image = null;
/*  917 */           Window w = getWindow();
/*      */           
/*  919 */           if ((w instanceof Frame)) {
/*  920 */             _frame = (Frame)w;
/*  921 */             image = _frame.getIconImage();
/*      */           } else {
/*  923 */             _dialog = (Dialog)w;
/*      */             
/*  925 */             if ((_dialog.getParent() != null) && 
/*  926 */               ((_dialog.getParent() instanceof Frame))) {
/*  927 */               image = ((Frame)_dialog.getParent()).getIconImage();
/*      */             }
/*      */           }
/*      */           
/*      */ 
/*  932 */           if (image != null) {
/*  933 */             xOffset += 10;
/*  934 */             g.drawImage(image, xOffset - 20, 3, 16, 16, null);
/*      */           }
/*      */           else {
/*  937 */             Icon icon = UIManager.getIcon("InternalFrame.pantherIconInactive");
/*      */             
/*      */ 
/*  940 */             if (icon != null) {
/*  941 */               xOffset += 10;
/*  942 */               icon.paintIcon(this, g, xOffset - 20, 3);
/*      */             }
/*      */           }
/*      */         }
/*      */         
/*  947 */         g.setColor(LiquidLookAndFeel.winDecoPanther ? new Color(115, 115, 115) : Color.black);
/*      */         
/*  949 */         g.drawString(frameTitle, xOffset, yOffset - 1);
/*      */         
/*  951 */         xOffset += (leftToRight ? titleLength + 2 : -2);
/*      */       }
/*      */     }
/*      */     
/*  955 */     g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, oldAntiAliasingValue);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   private void drawLiquidCaption(Graphics g, boolean isSelected, int w, int h)
/*      */   {
/*  963 */     Color c = isSelected ? new Color(62, 145, 235) : new Color(175, 214, 255);
/*  964 */     g.setColor(c);
/*  965 */     g.fillRect(0, 0, w, h - 1);
/*  966 */     c = isSelected ? new Color(94, 172, 255) : new Color(226, 240, 255);
/*  967 */     g.setColor(c);
/*  968 */     g.drawLine(0, 0, w, 0);
/*  969 */     c = isSelected ? new Color(60, 141, 228) : new Color(170, 207, 247);
/*  970 */     g.setColor(c);
/*  971 */     g.drawLine(0, 1, w, 1);
/*      */     
/*  973 */     for (int i = 4; i < h - 1; i += 4) {
/*  974 */       c = isSelected ? new Color(59, 138, 223) : new Color(166, 203, 242);
/*  975 */       g.setColor(c);
/*  976 */       g.drawLine(0, i, w, i);
/*  977 */       c = isSelected ? new Color(60, 141, 228) : new Color(170, 207, 247);
/*  978 */       g.setColor(c);
/*  979 */       g.drawLine(0, i + 1, w, i + 1);
/*      */     }
/*      */     
/*  982 */     c = isSelected ? new Color(47, 111, 180) : new Color(135, 164, 196);
/*  983 */     g.setColor(c);
/*  984 */     g.drawLine(0, h - 1, w, h - 1);
/*      */   }
/*      */   
/*      */   private void drawPantherCaption(Graphics g, boolean isSelected, int w, int h) {
/*  988 */     Graphics2D g2 = (Graphics2D)g;
/*  989 */     GradientPaint grad = isSelected ? new GradientPaint(0.0F, 0.0F, new Color(238, 238, 238), 0.0F, h - 1, new Color(192, 192, 192)) : new GradientPaint(0.0F, 0.0F, new Color(230, 230, 230), 0.0F, h - 1, new Color(202, 202, 202));
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  995 */     g2.setPaint(grad);
/*  996 */     g2.fillRect(0, 0, w, h - 1);
/*      */     
/*  998 */     g2.setColor(new Color(198, 198, 198));
/*  999 */     g2.drawLine(0, 0, w - 1, 0);
/* 1000 */     g2.setColor(Color.WHITE);
/* 1001 */     g2.drawLine(0, 1, w - 1, 1);
/* 1002 */     g2.setColor(new Color(147, 147, 147));
/* 1003 */     g2.drawLine(0, h - 1, w, h - 1);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   private String clippedText(String text, FontMetrics fm, int availTextWidth)
/*      */   {
/* 1011 */     if ((text == null) || (text.equals(""))) {
/* 1012 */       return "";
/*      */     }
/*      */     
/* 1015 */     int textWidth = SwingUtilities.computeStringWidth(fm, text);
/* 1016 */     String clipString = "...";
/*      */     
/* 1018 */     if (textWidth > availTextWidth) {
/* 1019 */       int totalWidth = SwingUtilities.computeStringWidth(fm, clipString);
/*      */       
/*      */ 
/* 1022 */       for (int nChars = 0; nChars < text.length(); nChars++) {
/* 1023 */         totalWidth += fm.charWidth(text.charAt(nChars));
/*      */         
/* 1025 */         if (totalWidth > availTextWidth) {
/*      */           break;
/*      */         }
/*      */       }
/*      */       
/* 1030 */       text = text.substring(0, nChars) + clipString;
/*      */     }
/*      */     
/* 1033 */     return text;
/*      */   }
/*      */   
/*      */   private int getInt(Object key, int defaultValue) {
/* 1037 */     Object value = UIManager.get(key);
/*      */     
/* 1039 */     if ((value instanceof Integer)) {
/* 1040 */       return ((Integer)value).intValue();
/*      */     }
/*      */     
/* 1043 */     if ((value instanceof String)) {
/*      */       try {
/* 1045 */         return Integer.parseInt((String)value);
/*      */       }
/*      */       catch (NumberFormatException nfe) {}
/*      */     }
/*      */     
/* 1050 */     return defaultValue;
/*      */   }
/*      */   
/*      */   private class CloseAction
/*      */     extends AbstractAction
/*      */   {
/*      */     public CloseAction()
/*      */     {
/* 1058 */       super();
/*      */     }
/*      */     
/*      */     public void actionPerformed(ActionEvent e) {
/* 1062 */       LiquidTitlePane.this.close();
/*      */     }
/*      */   }
/*      */   
/*      */   private class IconifyAction
/*      */     extends AbstractAction
/*      */   {
/*      */     public IconifyAction()
/*      */     {
/* 1071 */       super();
/*      */     }
/*      */     
/*      */     public void actionPerformed(ActionEvent e) {
/* 1075 */       LiquidTitlePane.this.iconify();
/*      */     }
/*      */   }
/*      */   
/*      */   private class RestoreAction
/*      */     extends AbstractAction
/*      */   {
/*      */     public RestoreAction()
/*      */     {
/* 1084 */       super();
/*      */     }
/*      */     
/*      */     public void actionPerformed(ActionEvent e) {
/* 1088 */       LiquidTitlePane.this.restore();
/*      */     }
/*      */   }
/*      */   
/*      */   private class MaximizeAction
/*      */     extends AbstractAction
/*      */   {
/*      */     public MaximizeAction()
/*      */     {
/* 1097 */       super();
/*      */     }
/*      */     
/*      */     public void actionPerformed(ActionEvent e) {
/* 1101 */       LiquidTitlePane.this.maximize();
/*      */     }
/*      */   }
/*      */   
/*      */   private class MenuAction extends AbstractAction {
/*      */     public MenuAction() {
/* 1107 */       super();
/*      */     }
/*      */     
/*      */     public void actionPerformed(ActionEvent e) {
/* 1111 */       LiquidTitlePane.this.showMenu(LiquidTitlePane.access$700(LiquidTitlePane.this));
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */   private class SystemMenuBar
/*      */     extends JMenuBar
/*      */     implements java.awt.event.MouseListener
/*      */   {
/*      */     private JPopupMenu systemMenu;
/*      */     
/* 1122 */     private boolean isShowed = false;
/*      */     
/*      */     public SystemMenuBar(JPopupMenu menu)
/*      */     {
/* 1126 */       this.systemMenu = menu;
/* 1127 */       addMouseListener(this);
/*      */     }
/*      */     
/*      */     protected void setSystemMenuVisible(boolean b) {
/* 1131 */       this.isShowed = b;
/*      */     }
/*      */     
/*      */     public void paint(Graphics g) {
/* 1135 */       if (LiquidLookAndFeel.winDecoPanther) {
/* 1136 */         return;
/*      */       }
/*      */       
/* 1139 */       Frame frame = LiquidTitlePane.this.getFrame();
/* 1140 */       Image image = frame != null ? frame.getIconImage() : null;
/*      */       
/* 1142 */       if (image != null) {
/* 1143 */         g.drawImage(image, 0, 0, 16, 16, null);
/*      */       } else {
/* 1145 */         Icon icon = UIManager.getIcon("InternalFrame.icon");
/*      */         
/* 1147 */         if (icon != null) {
/* 1148 */           icon.paintIcon(this, g, 0, 0);
/*      */         }
/*      */       }
/*      */     }
/*      */     
/*      */     public Dimension getMinimumSize() {
/* 1154 */       return getPreferredSize();
/*      */     }
/*      */     
/*      */     public Dimension getPreferredSize() {
/* 1158 */       Icon icon = UIManager.getIcon("InternalFrame.icon");
/*      */       
/* 1160 */       if (icon != null) {
/* 1161 */         return new Dimension(icon.getIconWidth(), icon.getIconHeight());
/*      */       }
/*      */       
/* 1164 */       Dimension size = super.getPreferredSize();
/*      */       
/* 1166 */       return new Dimension(Math.max(16, size.width), Math.max(size.height, 16));
/*      */     }
/*      */     
/*      */     public void mouseClicked(MouseEvent e)
/*      */     {
/* 1171 */       if (!this.isShowed) {
/* 1172 */         this.systemMenu.show(this, 0, 18);
/* 1173 */         this.isShowed = true;
/*      */       } else {
/* 1175 */         this.isShowed = false;
/* 1176 */         this.systemMenu.setVisible(this.isShowed);
/*      */       }
/*      */     }
/*      */     
/*      */     public void mouseEntered(MouseEvent e) {
/* 1181 */       if (!this.systemMenu.isVisible()) {
/* 1182 */         this.isShowed = false;
/*      */       }
/*      */     }
/*      */     
/*      */ 
/*      */     public void mouseExited(MouseEvent e) {}
/*      */     
/*      */ 
/*      */     public void mousePressed(MouseEvent e) {}
/*      */     
/*      */ 
/*      */     public void mouseReleased(MouseEvent e) {}
/*      */   }
/*      */   
/*      */   private class TitlePaneLayout
/*      */     implements LayoutManager
/*      */   {
/*      */     TitlePaneLayout(LiquidTitlePane.1 x1)
/*      */     {
/* 1201 */       this();
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */     public Dimension preferredLayoutSize(Container c)
/*      */     {
/* 1209 */       int height = computeHeight();
/*      */       
/* 1211 */       return new Dimension(height, height);
/*      */     }
/*      */     
/*      */     public Dimension minimumLayoutSize(Container c) {
/* 1215 */       return preferredLayoutSize(c);
/*      */     }
/*      */     
/*      */     private int computeHeight() {
/* 1219 */       if ((LiquidTitlePane.this.getFrame() instanceof JFrame)) {
/* 1220 */         LiquidTitlePane.this.setMaximizeBounds(LiquidTitlePane.access$900(LiquidTitlePane.this));
/*      */         
/* 1222 */         if (LiquidLookAndFeel.winDecoPanther) {
/* 1223 */           return 22;
/*      */         }
/*      */         
/* 1226 */         return 24;
/*      */       }
/* 1228 */       if (LiquidLookAndFeel.winDecoPanther) {
/* 1229 */         return 22;
/*      */       }
/*      */       
/* 1232 */       return 24;
/*      */     }
/*      */     
/*      */     public void layoutContainer(Container c)
/*      */     {
/* 1237 */       if (LiquidTitlePane.this.getWindowDecorationStyle() == 0) {
/* 1238 */         LiquidTitlePane.this.buttonsWidth = 0;
/*      */         
/* 1240 */         return;
/*      */       }
/*      */       
/* 1243 */       boolean leftToRight = LiquidTitlePane.this.window == null ? LiquidTitlePane.this.getRootPane().getComponentOrientation().isLeftToRight() : LiquidTitlePane.this.window.getComponentOrientation().isLeftToRight();
/*      */       
/*      */ 
/*      */ 
/* 1247 */       if (LiquidLookAndFeel.winDecoPanther) {
/* 1248 */         leftToRight = !leftToRight;
/*      */       }
/*      */       
/* 1251 */       int w = LiquidTitlePane.this.getWidth();
/*      */       
/*      */       int buttonWidth;
/*      */       
/*      */       int buttonHeight;
/*      */       int buttonWidth;
/* 1257 */       if (LiquidTitlePane.this.closeButton != null) {
/* 1258 */         int buttonHeight = LiquidTitlePane.this.closeButton.getPreferredSize().height;
/* 1259 */         buttonWidth = LiquidTitlePane.this.closeButton.getPreferredSize().width;
/*      */       } else {
/* 1261 */         buttonHeight = 16;
/* 1262 */         buttonWidth = 16;
/*      */       }
/*      */       
/* 1265 */       int y = (LiquidTitlePane.this.getHeight() - buttonHeight) / 2 + 1;
/*      */       
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 1274 */       int spacing = LiquidLookAndFeel.winDecoPanther ? 7 : 0;
/* 1275 */       int x = leftToRight ? spacing : w - buttonWidth - spacing;
/*      */       
/* 1277 */       if (LiquidTitlePane.this.menuBar != null)
/*      */       {
/* 1279 */         LiquidTitlePane.this.menuBar.setBounds(x, y, buttonWidth, buttonHeight);
/*      */       }
/*      */       
/*      */ 
/* 1283 */       x = leftToRight ? w : 0;
/* 1284 */       x += (leftToRight ? -spacing - buttonWidth : LiquidLookAndFeel.winDecoPanther ? spacing : leftToRight ? -spacing - 3 - buttonWidth : spacing);
/*      */       
/*      */ 
/*      */ 
/* 1288 */       int yOffset = LiquidLookAndFeel.winDecoPanther ? 1 : 0;
/*      */       
/* 1290 */       if (LiquidTitlePane.this.closeButton != null) {
/* 1291 */         LiquidTitlePane.this.closeButton.setBounds(x, y - yOffset, buttonWidth, buttonHeight);
/*      */       }
/*      */       
/* 1294 */       if (LiquidTitlePane.this.menuButton != null) {
/* 1295 */         LiquidTitlePane.this.menuButton.setBounds(leftToRight ? 3 : w - 21 - spacing, y - yOffset + 1, 21, 12);
/*      */       }
/*      */       
/*      */ 
/* 1299 */       if (!leftToRight) {
/* 1300 */         x += buttonWidth;
/*      */       }
/*      */       
/* 1303 */       if ((LiquidLookAndFeel.winDecoPanther) && 
/* 1304 */         (LiquidTitlePane.this.iconifyButton != null) && (LiquidTitlePane.this.iconifyButton.getParent() != null)) {
/* 1305 */         x += (leftToRight ? -spacing - buttonWidth : spacing);
/* 1306 */         LiquidTitlePane.this.iconifyButton.setBounds(x, y - yOffset, buttonWidth, buttonHeight);
/*      */         
/*      */ 
/* 1309 */         if (!leftToRight) {
/* 1310 */           x += buttonWidth;
/*      */         }
/*      */       }
/*      */       
/*      */ 
/* 1315 */       if ((Toolkit.getDefaultToolkit().isFrameStateSupported(6)) && 
/* 1316 */         (LiquidTitlePane.this.toggleButton.getParent() != null)) {
/* 1317 */         x += (leftToRight ? -spacing - buttonWidth : spacing);
/* 1318 */         LiquidTitlePane.this.toggleButton.setBounds(x, y - yOffset, buttonWidth, buttonHeight);
/*      */         
/*      */ 
/* 1321 */         if (!leftToRight) {
/* 1322 */           x += buttonWidth;
/*      */         }
/*      */       }
/*      */       
/*      */ 
/* 1327 */       if ((!LiquidLookAndFeel.winDecoPanther) && 
/* 1328 */         (LiquidTitlePane.this.iconifyButton != null) && (LiquidTitlePane.this.iconifyButton.getParent() != null)) {
/* 1329 */         x += (leftToRight ? -spacing - buttonWidth : spacing);
/* 1330 */         LiquidTitlePane.this.iconifyButton.setBounds(x, y - yOffset, buttonWidth, buttonHeight);
/*      */         
/*      */ 
/* 1333 */         if (!leftToRight) {
/* 1334 */           x += buttonWidth;
/*      */         }
/*      */       }
/*      */       
/*      */ 
/* 1339 */       LiquidTitlePane.this.buttonsWidth = (leftToRight ? w - x : x); }
/*      */     
/*      */     private TitlePaneLayout() {}
/*      */     
/*      */     public void addLayoutComponent(String name, Component c) {}
/*      */     
/*      */     public void removeLayoutComponent(Component c) {} }
/*      */   
/* 1347 */   private class PropertyChangeHandler implements PropertyChangeListener { PropertyChangeHandler(LiquidTitlePane.1 x1) { this(); }
/*      */     
/* 1349 */     public void propertyChange(PropertyChangeEvent pce) { String name = pce.getPropertyName();
/*      */       
/*      */ 
/* 1352 */       if (("resizable".equals(name)) || ("state".equals(name))) {
/* 1353 */         Frame frame = LiquidTitlePane.this.getFrame();
/*      */         
/* 1355 */         if (frame != null) {
/* 1356 */           LiquidTitlePane.this.setState(frame.getExtendedState(), true);
/*      */         }
/*      */         
/* 1359 */         if ("resizable".equals(name)) {
/* 1360 */           LiquidTitlePane.this.getRootPane().repaint();
/*      */         }
/* 1362 */       } else if ("title".equals(name)) {
/* 1363 */         LiquidTitlePane.this.repaint();
/* 1364 */       } else if ("componentOrientation".equals(name)) {
/* 1365 */         LiquidTitlePane.this.revalidate();
/* 1366 */         LiquidTitlePane.this.repaint();
/*      */       }
/*      */     }
/*      */     
/*      */     private PropertyChangeHandler() {}
/*      */   }
/*      */   
/*      */   private class WindowHandler extends WindowAdapter {
/* 1374 */     WindowHandler(LiquidTitlePane.1 x1) { this(); }
/*      */     
/* 1376 */     public void windowActivated(WindowEvent ev) { LiquidTitlePane.this.setActive(true); }
/*      */     
/*      */ 
/*      */ 
/* 1380 */     public void windowDeactivated(WindowEvent ev) { LiquidTitlePane.this.setActive(false); }
/*      */     
/*      */     private WindowHandler() {}
/*      */   }
/*      */   
/*      */   class WindowMoveListener extends ComponentAdapter {
/*      */     WindowMoveListener() {}
/*      */     
/*      */     public void componentMoved(ComponentEvent e) {
/* 1389 */       if (LiquidTitlePane.this.getWindowDecorationStyle() == 0) {
/* 1390 */         return;
/*      */       }
/*      */       
/* 1393 */       Window w = LiquidTitlePane.this.getWindow();
/* 1394 */       w.repaint(0, 0, w.getWidth(), 5);
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */     public void componentResized(ComponentEvent e)
/*      */     {
/* 1401 */       if (LiquidTitlePane.this.getWindowDecorationStyle() == 0) {
/* 1402 */         return;
/*      */       }
/*      */       
/* 1405 */       Window w = LiquidTitlePane.this.getWindow();
/* 1406 */       w.repaint(0, 0, w.getWidth(), 5);
/*      */     }
/*      */   }
/*      */ }


/* Location:              E:\java\java学习\hutubill\lib\all.jar!\com\birosoft\liquid\LiquidTitlePane.class
 * Java compiler version: 4 (48.0)
 * JD-Core Version:       0.7.1
 */
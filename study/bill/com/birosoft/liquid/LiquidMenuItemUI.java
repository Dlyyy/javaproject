/*      */ package com.birosoft.liquid;
/*      */ 
/*      */ import com.birosoft.liquid.skin.Skin;
/*      */ import com.birosoft.liquid.skin.SkinMenuItem;
/*      */ import com.birosoft.liquid.util.Colors;
/*      */ import java.awt.Color;
/*      */ import java.awt.Component;
/*      */ import java.awt.Container;
/*      */ import java.awt.Dimension;
/*      */ import java.awt.Font;
/*      */ import java.awt.FontMetrics;
/*      */ import java.awt.Graphics;
/*      */ import java.awt.Insets;
/*      */ import java.awt.Point;
/*      */ import java.awt.Rectangle;
/*      */ import java.awt.event.ActionEvent;
/*      */ import java.awt.event.KeyEvent;
/*      */ import java.awt.event.MouseEvent;
/*      */ import java.beans.PropertyChangeEvent;
/*      */ import java.beans.PropertyChangeListener;
/*      */ import javax.swing.AbstractAction;
/*      */ import javax.swing.ActionMap;
/*      */ import javax.swing.ButtonModel;
/*      */ import javax.swing.Icon;
/*      */ import javax.swing.InputMap;
/*      */ import javax.swing.JCheckBoxMenuItem;
/*      */ import javax.swing.JComponent;
/*      */ import javax.swing.JMenu;
/*      */ import javax.swing.JMenuItem;
/*      */ import javax.swing.JRadioButtonMenuItem;
/*      */ import javax.swing.KeyStroke;
/*      */ import javax.swing.LookAndFeel;
/*      */ import javax.swing.MenuElement;
/*      */ import javax.swing.MenuSelectionManager;
/*      */ import javax.swing.SwingUtilities;
/*      */ import javax.swing.UIDefaults;
/*      */ import javax.swing.UIManager;
/*      */ import javax.swing.event.MenuDragMouseEvent;
/*      */ import javax.swing.event.MenuDragMouseListener;
/*      */ import javax.swing.event.MenuKeyEvent;
/*      */ import javax.swing.event.MenuKeyListener;
/*      */ import javax.swing.event.MouseInputListener;
/*      */ import javax.swing.plaf.ActionMapUIResource;
/*      */ import javax.swing.plaf.ComponentInputMapUIResource;
/*      */ import javax.swing.plaf.ComponentUI;
/*      */ import javax.swing.plaf.MenuItemUI;
/*      */ import javax.swing.plaf.UIResource;
/*      */ import javax.swing.plaf.basic.BasicGraphicsUtils;
/*      */ import javax.swing.plaf.basic.BasicHTML;
/*      */ import javax.swing.text.View;
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
/*      */ public class LiquidMenuItemUI
/*      */   extends MenuItemUI
/*      */ {
/*      */   protected static int defaultTextIconGap;
/*      */   protected static int defaultIconGap;
/*      */   private static final boolean TRACE = false;
/*      */   private static final boolean VERBOSE = false;
/*      */   private static final boolean DEBUG = false;
/*      */   static final String MAX_TEXT_WIDTH = "maxTextWidth";
/*      */   static final String MAX_ACC_WIDTH = "maxAccWidth";
/*      */   static final String MAX_ICON_WIDTH = "maxIconWidth";
/*   88 */   static SkinMenuItem skin = new SkinMenuItem("menu.png", 0, 1, 2, 3, 4);
/*   89 */   static Skin topSkin = new Skin("menu_top.png", 3, 4, 10, 4, 11);
/*      */   
/*      */ 
/*      */ 
/*   93 */   static Rectangle zeroRect = new Rectangle(0, 0, 0, 0);
/*   94 */   static Rectangle iconRect = new Rectangle();
/*   95 */   static Rectangle textRect = new Rectangle();
/*   96 */   static Rectangle acceleratorRect = new Rectangle();
/*   97 */   static Rectangle checkIconRect = new Rectangle();
/*   98 */   static Rectangle arrowIconRect = new Rectangle();
/*   99 */   static Rectangle viewRect = new Rectangle(32767, 32767);
/*  100 */   static Rectangle r = new Rectangle();
/*  101 */   public LiquidMenuItemUI() { this.menuItem = null;
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
/*  113 */     this.arrowIcon = null;
/*  114 */     this.selArrowIcon = null;
/*  115 */     this.checkIcon = null;
/*      */   }
/*      */   
/*      */   protected JMenuItem menuItem;
/*      */   protected Color selectionBackground;
/*      */   public void installUI(JComponent c)
/*      */   {
/*  122 */     this.menuItem = ((JMenuItem)c);
/*      */     
/*  124 */     installDefaults();
/*  125 */     installComponents(this.menuItem);
/*  126 */     installListeners();
/*  127 */     installKeyboardActions();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   protected void installComponents(JMenuItem menuItem)
/*      */   {
/*  134 */     BasicHTML.updateRenderer(menuItem, menuItem.getText());
/*      */   }
/*      */   
/*      */   protected String getPropertyPrefix() {
/*  138 */     return "MenuItem";
/*      */   }
/*      */   
/*      */   protected void installListeners() {
/*  142 */     if ((this.mouseInputListener = createMouseInputListener(this.menuItem)) != null) {
/*  143 */       this.menuItem.addMouseListener(this.mouseInputListener);
/*  144 */       this.menuItem.addMouseMotionListener(this.mouseInputListener);
/*      */     }
/*      */     
/*  147 */     if ((this.menuDragMouseListener = createMenuDragMouseListener(this.menuItem)) != null) {
/*  148 */       this.menuItem.addMenuDragMouseListener(this.menuDragMouseListener);
/*      */     }
/*      */     
/*  151 */     if ((this.menuKeyListener = createMenuKeyListener(this.menuItem)) != null) {
/*  152 */       this.menuItem.addMenuKeyListener(this.menuKeyListener);
/*      */     }
/*      */     
/*  155 */     if ((this.propertyChangeListener = createPropertyChangeListener(this.menuItem)) != null) {
/*  156 */       this.menuItem.addPropertyChangeListener(this.propertyChangeListener);
/*      */     }
/*      */   }
/*      */   
/*      */   protected void installKeyboardActions() {
/*  161 */     ActionMap actionMap = getActionMap();
/*      */     
/*  163 */     SwingUtilities.replaceUIActionMap(this.menuItem, actionMap);
/*  164 */     updateAcceleratorBinding();
/*      */   }
/*      */   
/*      */   public void uninstallUI(JComponent c) {
/*  168 */     this.menuItem = ((JMenuItem)c);
/*  169 */     uninstallDefaults();
/*  170 */     uninstallComponents(this.menuItem);
/*  171 */     uninstallListeners();
/*  172 */     uninstallKeyboardActions();
/*      */     
/*      */ 
/*  175 */     Container parent = this.menuItem.getParent();
/*      */     
/*  177 */     if ((parent != null) && ((parent instanceof JComponent)) && ((!(this.menuItem instanceof JMenu)) || (!((JMenu)this.menuItem).isTopLevelMenu())))
/*      */     {
/*      */ 
/*  180 */       JComponent p = (JComponent)parent;
/*  181 */       p.putClientProperty("maxAccWidth", null);
/*  182 */       p.putClientProperty("maxTextWidth", null);
/*      */     }
/*      */     
/*  185 */     this.menuItem = null;
/*      */   }
/*      */   
/*      */   protected void uninstallDefaults() {
/*  189 */     LookAndFeel.uninstallBorder(this.menuItem);
/*  190 */     this.menuItem.setBorderPainted(this.oldBorderPainted);
/*      */     
/*  192 */     if ((this.menuItem.getMargin() instanceof UIResource)) {
/*  193 */       this.menuItem.setMargin(null);
/*      */     }
/*      */     
/*  196 */     if ((this.arrowIcon instanceof UIResource)) {
/*  197 */       this.arrowIcon = null;
/*      */     }
/*      */     
/*  200 */     if ((this.selArrowIcon instanceof UIResource)) {
/*  201 */       this.selArrowIcon = null;
/*      */     }
/*      */     
/*  204 */     if ((this.checkIcon instanceof UIResource)) {
/*  205 */       this.checkIcon = null;
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   protected void uninstallComponents(JMenuItem menuItem)
/*      */   {
/*  213 */     BasicHTML.updateRenderer(menuItem, "");
/*      */   }
/*      */   
/*      */   protected void uninstallListeners() {
/*  217 */     if (this.mouseInputListener != null) {
/*  218 */       this.menuItem.removeMouseListener(this.mouseInputListener);
/*  219 */       this.menuItem.removeMouseMotionListener(this.mouseInputListener);
/*      */     }
/*      */     
/*  222 */     if (this.menuDragMouseListener != null) {
/*  223 */       this.menuItem.removeMenuDragMouseListener(this.menuDragMouseListener);
/*      */     }
/*      */     
/*  226 */     if (this.menuKeyListener != null) {
/*  227 */       this.menuItem.removeMenuKeyListener(this.menuKeyListener);
/*      */     }
/*      */     
/*  230 */     if (this.propertyChangeListener != null) {
/*  231 */       this.menuItem.removePropertyChangeListener(this.propertyChangeListener);
/*      */     }
/*      */     
/*  234 */     this.mouseInputListener = null;
/*  235 */     this.menuDragMouseListener = null;
/*  236 */     this.menuKeyListener = null;
/*  237 */     this.propertyChangeListener = null;
/*      */   }
/*      */   
/*      */   protected void uninstallKeyboardActions() {
/*  241 */     SwingUtilities.replaceUIActionMap(this.menuItem, null);
/*      */     
/*  243 */     if (this.windowInputMap != null) {
/*  244 */       SwingUtilities.replaceUIInputMap(this.menuItem, 2, null);
/*      */       
/*  246 */       this.windowInputMap = null;
/*      */     }
/*      */   }
/*      */   
/*      */   protected MouseInputListener createMouseInputListener(JComponent c) {
/*  251 */     return new MouseInputHandler();
/*      */   }
/*      */   
/*      */   protected MenuDragMouseListener createMenuDragMouseListener(JComponent c) {
/*  255 */     return new MenuDragMouseHandler(null);
/*      */   }
/*      */   
/*      */   protected MenuKeyListener createMenuKeyListener(JComponent c) {
/*  259 */     return new MenuKeyHandler(null);
/*      */   }
/*      */   
/*      */   private PropertyChangeListener createPropertyChangeListener(JComponent c) {
/*  263 */     return new PropertyChangeHandler(null);
/*      */   }
/*      */   
/*      */   ActionMap getActionMap() {
/*  267 */     String propertyPrefix = getPropertyPrefix();
/*  268 */     String uiKey = propertyPrefix + ".actionMap";
/*  269 */     ActionMap am = (ActionMap)UIManager.get(uiKey);
/*      */     
/*  271 */     if (am == null) {
/*  272 */       am = createActionMap();
/*  273 */       UIManager.getLookAndFeelDefaults().put(uiKey, am);
/*      */     }
/*      */     
/*  276 */     return am;
/*      */   }
/*      */   
/*      */   ActionMap createActionMap() {
/*  280 */     ActionMap map = new ActionMapUIResource();
/*  281 */     map.put("doClick", new ClickAction(null));
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  287 */     return map;
/*      */   }
/*      */   
/*      */   InputMap createInputMap(int condition) {
/*  291 */     if (condition == 2) {
/*  292 */       return new ComponentInputMapUIResource(this.menuItem);
/*      */     }
/*      */     
/*  295 */     return null;
/*      */   }
/*      */   
/*      */   void updateAcceleratorBinding() {
/*  299 */     KeyStroke accelerator = this.menuItem.getAccelerator();
/*      */     
/*  301 */     if (this.windowInputMap != null) {
/*  302 */       this.windowInputMap.clear();
/*      */     }
/*      */     
/*  305 */     if (accelerator != null) {
/*  306 */       if (this.windowInputMap == null) {
/*  307 */         this.windowInputMap = createInputMap(2);
/*  308 */         SwingUtilities.replaceUIInputMap(this.menuItem, 2, this.windowInputMap);
/*      */       }
/*      */       
/*      */ 
/*  312 */       this.windowInputMap.put(accelerator, "doClick");
/*      */     }
/*      */   }
/*      */   
/*      */   public Dimension getMinimumSize(JComponent c) {
/*  317 */     Dimension d = null;
/*  318 */     View v = (View)c.getClientProperty("html");
/*      */     
/*  320 */     if (v != null) {
/*  321 */       d = getPreferredSize(c); Dimension 
/*  322 */         tmp23_22 = d;tmp23_22.width = ((int)(tmp23_22.width - (v.getPreferredSpan(0) - v.getMinimumSpan(0))));
/*      */     }
/*      */     
/*      */ 
/*  326 */     return d;
/*      */   }
/*      */   
/*      */   public Dimension getPreferredSize(JComponent c) {
/*  330 */     return getPreferredMenuItemSize(c, this.checkIcon, this.arrowIcon, defaultTextIconGap);
/*      */   }
/*      */   
/*      */ 
/*      */   protected Color selectionForeground;
/*      */   
/*      */   protected Color disabledForeground;
/*      */   
/*      */   protected Color acceleratorForeground;
/*      */   
/*      */   protected Color acceleratorSelectionForeground;
/*      */   private String acceleratorDelimiter;
/*      */   protected Font acceleratorFont;
/*      */   protected void paintText(Graphics g, JMenuItem menuItem, Rectangle textRect, String text)
/*      */   {
/*  345 */     ButtonModel model = menuItem.getModel();
/*  346 */     FontMetrics fm = g.getFontMetrics();
/*  347 */     int mnemIndex = menuItem.getDisplayedMnemonicIndex();
/*      */     
/*  349 */     if (!model.isEnabled())
/*      */     {
/*  351 */       if ((UIManager.get("MenuItem.disabledForeground") instanceof Color)) {
/*  352 */         g.setColor(UIManager.getColor("MenuItem.disabledForeground"));
/*  353 */         BasicGraphicsUtils.drawStringUnderlineCharAt(g, text, mnemIndex, textRect.x, textRect.y + fm.getAscent() - 1);
/*      */       }
/*      */       else {
/*  356 */         g.setColor(menuItem.getBackground().brighter());
/*  357 */         BasicGraphicsUtils.drawStringUnderlineCharAt(g, text, mnemIndex, textRect.x, textRect.y + fm.getAscent() - 1);
/*      */         
/*  359 */         g.setColor(menuItem.getBackground().darker());
/*  360 */         BasicGraphicsUtils.drawStringUnderlineCharAt(g, text, mnemIndex, textRect.x - 1, textRect.y + fm.getAscent() - 2);
/*      */       }
/*      */       
/*      */     }
/*      */     else
/*      */     {
/*  366 */       if ((model.isArmed()) || (((menuItem instanceof JMenu)) && (model.isSelected())))
/*      */       {
/*  368 */         g.setColor(this.selectionForeground);
/*      */       }
/*      */       
/*  371 */       BasicGraphicsUtils.drawStringUnderlineCharAt(g, text, mnemIndex, textRect.x, textRect.y + fm.getAscent() - 1);
/*      */     }
/*      */   }
/*      */   
/*      */   public Dimension getMaximumSize(JComponent c)
/*      */   {
/*  377 */     Dimension d = null;
/*  378 */     View v = (View)c.getClientProperty("html");
/*      */     
/*  380 */     if (v != null) {
/*  381 */       d = getPreferredSize(c); Dimension 
/*  382 */         tmp23_22 = d;tmp23_22.width = ((int)(tmp23_22.width + (v.getMaximumSpan(0) - v.getPreferredSpan(0))));
/*      */     }
/*      */     
/*      */ 
/*  386 */     return d;
/*      */   }
/*      */   
/*      */   private void resetRects() {
/*  390 */     iconRect.setBounds(zeroRect);
/*  391 */     textRect.setBounds(zeroRect);
/*  392 */     acceleratorRect.setBounds(zeroRect);
/*  393 */     checkIconRect.setBounds(zeroRect);
/*  394 */     arrowIconRect.setBounds(zeroRect);
/*  395 */     viewRect.setBounds(0, 0, 32767, 32767);
/*  396 */     r.setBounds(zeroRect);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void update(Graphics g, JComponent c)
/*      */   {
/*  406 */     paint(g, c);
/*      */   }
/*      */   
/*      */   public void paint(Graphics g, JComponent c) {
/*  410 */     paintMenuItem(g, c, this.checkIcon, this.arrowIcon, this.selectionBackground, this.selectionForeground, defaultTextIconGap);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   private boolean useCheckAndArrow()
/*      */   {
/*  419 */     boolean b = true;
/*      */     
/*  421 */     if (((this.menuItem instanceof JMenu)) && (((JMenu)this.menuItem).isTopLevelMenu()))
/*      */     {
/*  423 */       b = false;
/*      */     }
/*      */     
/*  426 */     return b;
/*      */   }
/*      */   
/*      */   public MenuElement[] getPath() {
/*  430 */     MenuSelectionManager m = MenuSelectionManager.defaultManager();
/*  431 */     MenuElement[] oldPath = m.getSelectedPath();
/*      */     
/*  433 */     int i = oldPath.length;
/*      */     
/*  435 */     if (i == 0) {
/*  436 */       return new MenuElement[0];
/*      */     }
/*      */     
/*  439 */     Component parent = this.menuItem.getParent();
/*      */     MenuElement[] newPath;
/*  441 */     if (oldPath[(i - 1)].getComponent() == parent)
/*      */     {
/*  443 */       MenuElement[] newPath = new MenuElement[i + 1];
/*  444 */       System.arraycopy(oldPath, 0, newPath, 0, i);
/*  445 */       newPath[i] = this.menuItem;
/*      */ 
/*      */ 
/*      */ 
/*      */     }
/*      */     else
/*      */     {
/*      */ 
/*      */ 
/*      */ 
/*  455 */       for (int j = oldPath.length - 1; j >= 0; j--) {
/*  456 */         if (oldPath[j].getComponent() == parent) {
/*      */           break;
/*      */         }
/*      */       }
/*      */       
/*  461 */       newPath = new MenuElement[j + 2];
/*  462 */       System.arraycopy(oldPath, 0, newPath, 0, j + 1);
/*  463 */       newPath[(j + 1)] = this.menuItem;
/*      */     }
/*      */     
/*  466 */     return newPath;
/*      */   }
/*      */   
/*      */ 
/*      */   protected MouseInputListener mouseInputListener;
/*      */   
/*      */   protected MenuDragMouseListener menuDragMouseListener;
/*      */   
/*      */   protected MenuKeyListener menuKeyListener;
/*      */   
/*      */   private PropertyChangeListener propertyChangeListener;
/*      */   
/*      */   protected Icon arrowIcon;
/*      */   
/*      */   protected Icon selArrowIcon;
/*      */   
/*      */   protected Icon checkIcon;
/*      */   protected boolean oldBorderPainted;
/*      */   InputMap windowInputMap;
/*      */   protected void doClick(MenuSelectionManager msm)
/*      */   {
/*  487 */     if (msm == null) {
/*  488 */       msm = MenuSelectionManager.defaultManager();
/*      */     }
/*      */     
/*  491 */     msm.clearSelectedPath();
/*  492 */     this.menuItem.doClick(0);
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
/*      */   private boolean isInternalFrameSystemMenu()
/*      */   {
/*  506 */     String actionCommand = this.menuItem.getActionCommand();
/*      */     
/*  508 */     if ((actionCommand == "Close") || (actionCommand == "Minimize") || (actionCommand == "Restore") || (actionCommand == "Maximize"))
/*      */     {
/*  510 */       return true;
/*      */     }
/*  512 */     return false;
/*      */   }
/*      */   
/*      */   public static ComponentUI createUI(JComponent c)
/*      */   {
/*  517 */     return new LiquidMenuItemUI();
/*      */   }
/*      */   
/*      */   protected void installDefaults() {
/*  521 */     String prefix = getPropertyPrefix();
/*      */     
/*  523 */     this.acceleratorFont = UIManager.getFont("MenuItem.acceleratorFont");
/*      */     
/*  525 */     this.menuItem.setOpaque(true);
/*      */     
/*  527 */     if ((this.menuItem.getMargin() == null) || ((this.menuItem.getMargin() instanceof UIResource)))
/*      */     {
/*  529 */       this.menuItem.setMargin(UIManager.getInsets(prefix + ".margin"));
/*      */     }
/*      */     
/*  532 */     defaultTextIconGap = 4;
/*      */     
/*  534 */     LookAndFeel.installBorder(this.menuItem, prefix + ".border");
/*  535 */     this.oldBorderPainted = this.menuItem.isBorderPainted();
/*  536 */     this.menuItem.setBorderPainted(((Boolean)UIManager.get(prefix + ".borderPainted")).booleanValue());
/*      */     
/*  538 */     LookAndFeel.installColorsAndFont(this.menuItem, prefix + ".background", prefix + ".foreground", prefix + ".font");
/*      */     
/*      */ 
/*      */ 
/*  542 */     if ((this.selectionBackground == null) || ((this.selectionBackground instanceof UIResource)))
/*      */     {
/*  544 */       this.selectionBackground = UIManager.getColor(prefix + ".selectionBackground");
/*      */     }
/*      */     
/*      */ 
/*  548 */     if ((this.selectionForeground == null) || ((this.selectionForeground instanceof UIResource)))
/*      */     {
/*  550 */       this.selectionForeground = UIManager.getColor(prefix + ".selectionForeground");
/*      */     }
/*      */     
/*      */ 
/*  554 */     if ((this.disabledForeground == null) || ((this.disabledForeground instanceof UIResource)))
/*      */     {
/*  556 */       this.disabledForeground = UIManager.getColor(prefix + ".disabledForeground");
/*      */     }
/*      */     
/*      */ 
/*  560 */     if ((this.acceleratorForeground == null) || ((this.acceleratorForeground instanceof UIResource)))
/*      */     {
/*  562 */       this.acceleratorForeground = UIManager.getColor(prefix + ".acceleratorForeground");
/*      */     }
/*      */     
/*      */ 
/*  566 */     if ((this.acceleratorSelectionForeground == null) || ((this.acceleratorSelectionForeground instanceof UIResource)))
/*      */     {
/*  568 */       this.acceleratorSelectionForeground = UIManager.getColor(prefix + ".acceleratorSelectionForeground");
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*  573 */     this.acceleratorDelimiter = UIManager.getString("MenuItem.acceleratorDelimiter");
/*      */     
/*      */ 
/*  576 */     if (this.acceleratorDelimiter == null) {
/*  577 */       this.acceleratorDelimiter = "+";
/*      */     }
/*      */     
/*      */ 
/*  581 */     if (useCheckAndArrow()) {
/*  582 */       if ((this.arrowIcon == null) || ((this.arrowIcon instanceof UIResource))) {
/*  583 */         this.arrowIcon = UIManager.getIcon(prefix + ".arrowIcon");
/*      */       }
/*      */       
/*  586 */       if ((this.checkIcon == null) || ((this.checkIcon instanceof UIResource))) {
/*  587 */         this.checkIcon = UIManager.getIcon(prefix + ".checkIcon");
/*      */       }
/*      */     }
/*      */     
/*      */ 
/*  592 */     defaultTextIconGap = 8;
/*  593 */     defaultIconGap = 4;
/*      */   }
/*      */   
/*      */   protected Dimension getPreferredMenuItemSize(JComponent c, Icon checkIcon, Icon arrowIcon, int defaultTextIconGap)
/*      */   {
/*  598 */     JMenuItem b = (JMenuItem)c;
/*  599 */     Icon icon = b.getIcon();
/*  600 */     String text = b.getText();
/*  601 */     KeyStroke accelerator = b.getAccelerator();
/*  602 */     String acceleratorText = "";
/*      */     
/*  604 */     if (accelerator != null) {
/*  605 */       int modifiers = accelerator.getModifiers();
/*      */       
/*  607 */       if (modifiers > 0) {
/*  608 */         acceleratorText = KeyEvent.getKeyModifiersText(modifiers);
/*      */         
/*      */ 
/*  611 */         acceleratorText = acceleratorText + this.acceleratorDelimiter;
/*      */       }
/*      */       
/*  614 */       int keyCode = accelerator.getKeyCode();
/*      */       
/*  616 */       if (keyCode != 0) {
/*  617 */         acceleratorText = acceleratorText + KeyEvent.getKeyText(keyCode);
/*      */       } else {
/*  619 */         acceleratorText = acceleratorText + accelerator.getKeyChar();
/*      */       }
/*      */     }
/*      */     
/*  623 */     Font font = b.getFont();
/*  624 */     FontMetrics fm = b.getFontMetrics(font);
/*  625 */     FontMetrics fmAccel = b.getFontMetrics(this.acceleratorFont);
/*      */     
/*  627 */     resetRects();
/*      */     
/*  629 */     layoutMenuItem(fm, text, fmAccel, acceleratorText, icon, checkIcon, arrowIcon, b.getVerticalAlignment(), b.getHorizontalAlignment(), b.getVerticalTextPosition(), b.getHorizontalTextPosition(), viewRect, iconRect, textRect, acceleratorRect, checkIconRect, arrowIconRect, text == null ? 0 : defaultTextIconGap, defaultIconGap);
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  637 */     r.setBounds(textRect);
/*  638 */     r = SwingUtilities.computeUnion(iconRect.x, iconRect.y, iconRect.width, iconRect.height, r);
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  645 */     Container parent = this.menuItem.getParent();
/*      */     
/*      */ 
/*  648 */     if ((parent != null) && ((parent instanceof JComponent)) && ((!(this.menuItem instanceof JMenu)) || (!((JMenu)this.menuItem).isTopLevelMenu())))
/*      */     {
/*      */ 
/*  651 */       JComponent p = (JComponent)parent;
/*      */       
/*      */ 
/*  654 */       Integer maxTextWidth = (Integer)p.getClientProperty("maxTextWidth");
/*  655 */       Integer maxAccWidth = (Integer)p.getClientProperty("maxAccWidth");
/*  656 */       Integer maxIconWidth = (Integer)p.getClientProperty("maxIconWidth");
/*      */       
/*  658 */       int maxTextValue = maxTextWidth != null ? maxTextWidth.intValue() : 0;
/*      */       
/*  660 */       int maxAccValue = maxAccWidth != null ? maxAccWidth.intValue() : 0;
/*  661 */       int maxIconValue = maxIconWidth != null ? maxIconWidth.intValue() : 0;
/*      */       
/*      */ 
/*      */ 
/*  665 */       if (r.width < maxTextValue) {
/*  666 */         r.width = maxTextValue;
/*      */       } else {
/*  668 */         p.putClientProperty("maxTextWidth", new Integer(r.width));
/*      */       }
/*      */       
/*      */ 
/*      */ 
/*  673 */       if (acceleratorRect.width > maxAccValue) {
/*  674 */         maxAccValue = acceleratorRect.width;
/*  675 */         p.putClientProperty("maxAccWidth", new Integer(acceleratorRect.width));
/*      */       }
/*      */       
/*      */ 
/*      */ 
/*  680 */       if ((icon != null) && (icon.getIconWidth() > maxIconValue)) {
/*  681 */         maxIconValue = icon.getIconWidth();
/*  682 */         p.putClientProperty("maxIconWidth", new Integer(maxIconValue));
/*      */       }
/*      */       
/*      */ 
/*      */ 
/*  687 */       r.width += maxAccValue;
/*  688 */       r.width += defaultTextIconGap;
/*      */     }
/*      */     
/*  691 */     if (useCheckAndArrow())
/*      */     {
/*  693 */       r.width += checkIconRect.width;
/*  694 */       r.width += defaultTextIconGap;
/*      */       
/*      */ 
/*  697 */       r.width += defaultTextIconGap;
/*  698 */       r.width += arrowIconRect.width;
/*      */     }
/*      */     
/*  701 */     r.width += defaultTextIconGap;
/*      */     
/*  703 */     Insets insets = b.getInsets();
/*      */     
/*  705 */     if (insets != null) {
/*  706 */       r.width += insets.left + insets.right;
/*  707 */       r.height += insets.top + insets.bottom;
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*  712 */     if (r.width % 2 == 0) {
/*  713 */       r.width += 1;
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*  718 */     if (r.height % 2 != 0) {
/*  719 */       r.height -= 1;
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*  724 */     if (r.height < 24) {
/*  725 */       r.height = 20;
/*      */     }
/*      */     
/*  728 */     return r.getSize();
/*      */   }
/*      */   
/*      */ 
/*      */   protected void paintMenuItem(Graphics g, JComponent c, Icon checkIcon, Icon arrowIcon, Color background, Color foreground, int defaultTextIconGap)
/*      */   {
/*  734 */     JMenuItem b = (JMenuItem)c;
/*  735 */     ButtonModel model = b.getModel();
/*      */     
/*  737 */     Icon bicon = b.getIcon();
/*      */     
/*  739 */     JComponent p = (JComponent)b.getParent();
/*  740 */     Integer maxValueInt = (Integer)p.getClientProperty("maxIconWidth");
/*  741 */     int maxValue = maxValueInt == null ? 16 : maxValueInt.intValue();
/*      */     
/*  743 */     int menuWidth = b.getWidth();
/*  744 */     int menuHeight = b.getHeight();
/*  745 */     Insets i = c.getInsets();
/*      */     
/*  747 */     resetRects();
/*      */     
/*  749 */     viewRect.setBounds(0, 0, menuWidth, menuHeight);
/*      */     
/*  751 */     viewRect.x += 0;
/*  752 */     viewRect.y += i.top;
/*  753 */     viewRect.width -= i.right + viewRect.x;
/*  754 */     viewRect.height -= i.bottom + viewRect.y;
/*      */     
/*  756 */     Font holdf = g.getFont();
/*  757 */     Font f = c.getFont();
/*  758 */     g.setFont(f);
/*      */     
/*  760 */     FontMetrics fm = g.getFontMetrics(f);
/*  761 */     FontMetrics fmAccel = g.getFontMetrics(this.acceleratorFont);
/*      */     
/*      */ 
/*  764 */     KeyStroke accelerator = b.getAccelerator();
/*  765 */     String acceleratorText = "";
/*      */     
/*  767 */     if (accelerator != null) {
/*  768 */       int modifiers = accelerator.getModifiers();
/*      */       
/*  770 */       if (modifiers > 0) {
/*  771 */         acceleratorText = KeyEvent.getKeyModifiersText(modifiers);
/*      */         
/*      */ 
/*  774 */         acceleratorText = acceleratorText + this.acceleratorDelimiter;
/*      */       }
/*      */       
/*  777 */       int keyCode = accelerator.getKeyCode();
/*      */       
/*  779 */       if (keyCode != 0) {
/*  780 */         acceleratorText = acceleratorText + KeyEvent.getKeyText(keyCode);
/*      */       } else {
/*  782 */         acceleratorText = acceleratorText + accelerator.getKeyChar();
/*      */       }
/*      */     }
/*      */     
/*  786 */     int offset = 0;
/*      */     
/*      */ 
/*  789 */     Icon ic = b.getIcon();
/*  790 */     Icon iCheck = checkIcon;
/*  791 */     Icon paintIcon = ic;
/*      */     
/*  793 */     if (useCheckAndArrow()) {
/*  794 */       if (((c instanceof JCheckBoxMenuItem)) || ((c instanceof JRadioButtonMenuItem)))
/*      */       {
/*  796 */         ic = checkIcon;
/*      */         
/*  798 */         if (checkIcon.getIconWidth() < maxValue) {
/*  799 */           ic = new EmptyIcon(maxValue, checkIcon.getIconHeight());
/*  800 */           offset = (maxValue - checkIcon.getIconWidth()) / 2;
/*      */         }
/*      */         
/*  803 */         paintIcon = null;
/*  804 */       } else if (((c instanceof JMenuItem)) && (
/*  805 */         (ic == null) || (ic.getIconWidth() < maxValue))) {
/*  806 */         int height = ic == null ? 2 : b.getIcon().getIconHeight();
/*  807 */         int width = ic == null ? 2 : b.getIcon().getIconWidth();
/*  808 */         offset = (maxValue - width) / 2;
/*  809 */         ic = new EmptyIcon(maxValue, height);
/*      */       }
/*      */     }
/*      */     
/*      */ 
/*  814 */     String text = layoutMenuItem(fm, b.getText(), fmAccel, acceleratorText, ic, null, arrowIcon, b.getVerticalAlignment(), b.getHorizontalAlignment(), b.getVerticalTextPosition(), b.getHorizontalTextPosition(), viewRect, iconRect, textRect, acceleratorRect, checkIconRect, arrowIconRect, b.getText() == null ? 0 : defaultTextIconGap, defaultIconGap);
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  822 */     paintBackground(g, b, background);
/*      */     
/*  824 */     Color holdc = g.getColor();
/*      */     
/*      */ 
/*  827 */     if (checkIcon != null) {
/*  828 */       if ((model.isArmed()) || (((c instanceof JMenu)) && (model.isSelected()))) {
/*  829 */         g.setColor(foreground);
/*      */       } else {
/*  831 */         g.setColor(holdc);
/*      */       }
/*      */       
/*  834 */       if (useCheckAndArrow()) {
/*  835 */         if ((c instanceof JCheckBoxMenuItem)) {
/*  836 */           if (model.isSelected()) {
/*  837 */             UIManager.getIcon("CheckBoxMenuItem.checkedIcon").paintIcon(c, g, iconRect.x + offset - 2, iconRect.y);
/*      */           }
/*      */           else
/*      */           {
/*  841 */             checkIcon.paintIcon(c, g, iconRect.x + offset - 2, iconRect.y);
/*      */           }
/*      */         }
/*      */         
/*      */ 
/*  846 */         if ((c instanceof JRadioButtonMenuItem)) {
/*  847 */           if (model.isSelected()) {
/*  848 */             UIManager.getIcon("RadioButtonMenuItem.checkedIcon").paintIcon(c, g, iconRect.x + offset, iconRect.y);
/*      */           }
/*      */           else
/*      */           {
/*  852 */             checkIcon.paintIcon(c, g, iconRect.x + offset, iconRect.y);
/*      */           }
/*      */         }
/*      */       }
/*      */       
/*      */ 
/*  858 */       g.setColor(holdc);
/*      */     }
/*      */     
/*      */ 
/*  862 */     if (paintIcon != null)
/*      */     {
/*      */ 
/*  865 */       if (!model.isEnabled()) {
/*  866 */         Icon icon = b.getDisabledIcon();
/*      */         
/*  868 */         if (icon != null) {
/*  869 */           icon.paintIcon(c, g, iconRect.x + offset, iconRect.y);
/*      */         }
/*  871 */       } else if ((model.isPressed()) && (model.isArmed())) {
/*  872 */         Icon icon = b.getPressedIcon();
/*      */         
/*  874 */         if (icon == null)
/*      */         {
/*  876 */           icon = b.getIcon();
/*      */         }
/*      */         
/*  879 */         if (icon != null) {
/*  880 */           icon.paintIcon(c, g, iconRect.x + offset, iconRect.y);
/*      */         }
/*  882 */       } else if ((model.isArmed()) || (model.isSelected())) {
/*  883 */         Icon icon = b.getIcon();
/*      */         
/*  885 */         if (icon != null) {
/*  886 */           Icon disabled = b.getDisabledIcon();
/*      */           
/*  888 */           if (disabled != null) {
/*  889 */             disabled.paintIcon(c, g, iconRect.x + offset + 1, iconRect.y + 1);
/*      */           }
/*      */           
/*      */ 
/*  893 */           icon.paintIcon(c, g, iconRect.x + offset - 1, iconRect.y - 1);
/*      */         }
/*      */       }
/*      */       else {
/*  897 */         Icon icon = b.getIcon();
/*      */         
/*  899 */         if (icon != null) {
/*  900 */           icon.paintIcon(c, g, iconRect.x + offset, iconRect.y);
/*      */         }
/*      */       }
/*      */     }
/*      */     
/*      */ 
/*  906 */     if (text != null) {
/*  907 */       View v = (View)c.getClientProperty("html");
/*      */       
/*  909 */       if (v != null) {
/*  910 */         v.paint(g, textRect);
/*      */       } else {
/*  912 */         paintText(g, b, textRect, text);
/*      */       }
/*      */     }
/*      */     
/*      */ 
/*  917 */     if ((acceleratorText != null) && (!acceleratorText.equals("")))
/*      */     {
/*  919 */       int accOffset = 0;
/*  920 */       Container parent = this.menuItem.getParent();
/*      */       
/*  922 */       if ((parent != null) && ((parent instanceof JComponent))) {
/*  923 */         Integer amaxValueInt = (Integer)p.getClientProperty("maxAccWidth");
/*  924 */         int amaxValue = amaxValueInt != null ? amaxValueInt.intValue() : acceleratorRect.width;
/*      */         
/*      */ 
/*      */ 
/*  928 */         accOffset = amaxValue - acceleratorRect.width;
/*      */       }
/*      */       
/*  931 */       g.setFont(this.acceleratorFont);
/*      */       
/*  933 */       if (!model.isEnabled())
/*      */       {
/*  935 */         if (this.disabledForeground != null) {
/*  936 */           g.setColor(this.disabledForeground);
/*  937 */           BasicGraphicsUtils.drawString(g, acceleratorText, 0, acceleratorRect.x - accOffset, acceleratorRect.y + fmAccel.getAscent());
/*      */         }
/*      */         else
/*      */         {
/*  941 */           g.setColor(b.getBackground().brighter());
/*  942 */           BasicGraphicsUtils.drawString(g, acceleratorText, 0, acceleratorRect.x - accOffset, acceleratorRect.y + fmAccel.getAscent());
/*      */           
/*      */ 
/*  945 */           g.setColor(b.getBackground().darker());
/*  946 */           BasicGraphicsUtils.drawString(g, acceleratorText, 0, acceleratorRect.x - accOffset - 1, acceleratorRect.y + fmAccel.getAscent() - 1);
/*      */         }
/*      */         
/*      */       }
/*      */       else
/*      */       {
/*  952 */         if ((model.isArmed()) || (((c instanceof JMenu)) && (model.isSelected())))
/*      */         {
/*  954 */           g.setColor(this.acceleratorSelectionForeground);
/*      */         } else {
/*  956 */           g.setColor(this.acceleratorForeground);
/*      */         }
/*      */         
/*  959 */         BasicGraphicsUtils.drawString(g, acceleratorText, 0, acceleratorRect.x - accOffset, acceleratorRect.y + fmAccel.getAscent());
/*      */       }
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*  966 */     if ((arrowIcon != null) && ((c instanceof JMenu))) {
/*  967 */       JMenu menu = null;
/*      */       
/*  969 */       if ((c instanceof JMenu)) {
/*  970 */         menu = (JMenu)c;
/*      */       }
/*      */       
/*  973 */       if ((model.isArmed()) || (((c instanceof JMenu)) && (model.isSelected()))) {
/*  974 */         g.setColor(foreground);
/*      */       }
/*      */       
/*  977 */       if (useCheckAndArrow())
/*      */       {
/*  979 */         if (model.isSelected()) {
/*  980 */           UIManager.getIcon("MenuItem.selArrowIcon").paintIcon(c, g, arrowIconRect.x, arrowIconRect.y);
/*      */         }
/*  982 */         else if (menu.isEnabled()) {
/*  983 */           UIManager.getIcon("MenuItem.arrowIcon").paintIcon(c, g, arrowIconRect.x, arrowIconRect.y);
/*      */         }
/*      */       }
/*      */     }
/*      */     
/*      */ 
/*  989 */     g.setColor(holdc);
/*  990 */     g.setFont(holdf);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected void paintBackground(Graphics g, JMenuItem menuItem, Color bgColor)
/*      */   {
/* 1002 */     ButtonModel model = menuItem.getModel();
/* 1003 */     Color oldColor = g.getColor();
/* 1004 */     int menuWidth = menuItem.getWidth();
/* 1005 */     int menuHeight = menuItem.getHeight();
/*      */     
/* 1007 */     boolean mouseOver = (model.isArmed()) || (((menuItem instanceof JMenu)) && (model.isSelected()));
/*      */     
/*      */ 
/* 1010 */     int maxValue = menuWidth;
/*      */     
/* 1012 */     g.setColor(LiquidLookAndFeel.getBackgroundColor());
/* 1013 */     g.fillRect(0, 0, menuWidth, menuHeight);
/* 1014 */     g.setColor(oldColor);
/*      */     
/* 1016 */     if (useCheckAndArrow()) {
/* 1017 */       JComponent p = (JComponent)menuItem.getParent();
/* 1018 */       Integer maxValueInt = (Integer)p.getClientProperty("maxIconWidth");
/* 1019 */       maxValue = maxValueInt == null ? 16 : maxValueInt.intValue();
/* 1020 */       maxValue += defaultTextIconGap;
/* 1021 */       skin.draw(g, model.isEnabled(), mouseOver, false, false, menuWidth, maxValue, menuHeight);
/*      */       
/*      */ 
/* 1024 */       if (!mouseOver) {
/* 1025 */         Color old = g.getColor();
/*      */         
/* 1027 */         if (LiquidLookAndFeel.areStipplesUsed()) {
/* 1028 */           Colors.drawStipples(g, menuItem, menuItem.getBackground());
/*      */         }
/*      */         
/* 1031 */         g.setColor(old);
/*      */       }
/*      */     } else {
/* 1034 */       boolean isRollover = menuItem.getClientProperty("rollover") == Boolean.TRUE;
/*      */       
/*      */ 
/* 1037 */       int index = 2;
/*      */       
/* 1039 */       if (!model.isEnabled()) {
/* 1040 */         index = 2;
/* 1041 */       } else if (model.isSelected()) {
/* 1042 */         index = 1;
/* 1043 */       } else if (isRollover) {
/* 1044 */         index = 0;
/*      */       }
/*      */       
/* 1047 */       topSkin.draw(g, index, 0, 1, menuWidth, menuHeight - 1);
/*      */       
/* 1049 */       if (index == 2) {
/* 1050 */         Color old = g.getColor();
/*      */         
/* 1052 */         if (LiquidLookAndFeel.areStipplesUsed()) {
/* 1053 */           Colors.drawStipples(g, menuItem, menuItem.getBackground());
/*      */         }
/*      */         
/* 1056 */         g.setColor(old);
/*      */       }
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
/*      */ 
/*      */   private String layoutMenuItem(FontMetrics fm, String text, FontMetrics fmAccel, String acceleratorText, Icon icon, Icon checkIcon, Icon arrowIcon, int verticalAlignment, int horizontalAlignment, int verticalTextPosition, int horizontalTextPosition, Rectangle viewRect, Rectangle iconRect, Rectangle textRect, Rectangle acceleratorRect, Rectangle checkIconRect, Rectangle arrowIconRect, int textIconGap, int menuItemGap)
/*      */   {
/* 1074 */     SwingUtilities.layoutCompoundLabel(this.menuItem, fm, text, icon, verticalAlignment, horizontalAlignment, verticalTextPosition, horizontalTextPosition, viewRect, iconRect, textRect, textIconGap);
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 1082 */     if ((acceleratorText == null) || (acceleratorText.equals(""))) {
/* 1083 */       acceleratorRect.width = (acceleratorRect.height = 0);
/* 1084 */       acceleratorText = "";
/*      */     } else {
/* 1086 */       acceleratorRect.width = SwingUtilities.computeStringWidth(fmAccel, acceleratorText);
/*      */       
/* 1088 */       acceleratorRect.height = fmAccel.getHeight();
/*      */     }
/*      */     
/*      */ 
/*      */ 
/* 1093 */     if (useCheckAndArrow()) {
/* 1094 */       if (checkIcon != null) {
/* 1095 */         checkIconRect.height = checkIcon.getIconHeight();
/* 1096 */         checkIconRect.width = checkIcon.getIconWidth();
/*      */       } else {
/* 1098 */         checkIconRect.width = (checkIconRect.height = 0);
/*      */       }
/*      */       
/*      */ 
/*      */ 
/* 1103 */       if (arrowIcon != null) {
/* 1104 */         arrowIconRect.width = arrowIcon.getIconWidth();
/* 1105 */         arrowIconRect.height = arrowIcon.getIconHeight();
/*      */       } else {
/* 1107 */         arrowIconRect.width = (arrowIconRect.height = 0);
/*      */       }
/*      */     } else {
/* 1110 */       checkIconRect.width = (checkIconRect.height = 0);
/* 1111 */       arrowIconRect.width = (arrowIconRect.height = 0);
/*      */     }
/*      */     
/* 1114 */     Rectangle labelRect = iconRect.union(textRect);
/*      */     
/*      */ 
/*      */ 
/* 1118 */     if (checkIcon != null) {
/* 1119 */       checkIconRect.x += menuItemGap;
/*      */     } else {
/* 1121 */       textRect.x += menuItemGap;
/* 1122 */       iconRect.x += menuItemGap;
/*      */     }
/*      */     
/*      */ 
/* 1126 */     acceleratorRect.x = (viewRect.x + viewRect.width - arrowIconRect.width - menuItemGap - acceleratorRect.width);
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 1132 */     acceleratorRect.y = (labelRect.y + labelRect.height / 2 - acceleratorRect.height / 2);
/*      */     
/*      */ 
/* 1135 */     if (useCheckAndArrow()) {
/* 1136 */       arrowIconRect.y = (labelRect.y + labelRect.height / 2 - arrowIconRect.height / 2);
/*      */       
/* 1138 */       checkIconRect.y = (labelRect.y + labelRect.height / 2 - checkIconRect.height / 2);
/*      */       
/* 1140 */       arrowIconRect.x = (viewRect.x + viewRect.width - menuItemGap - arrowIconRect.width);
/*      */     }
/*      */     
/*      */ 
/*      */ 
/* 1145 */     return text;
/*      */   }
/*      */   
/*      */   protected class MouseInputHandler implements MouseInputListener {
/*      */     protected MouseInputHandler() {}
/*      */     
/*      */     public void mouseClicked(MouseEvent e) {}
/*      */     
/*      */     public void mousePressed(MouseEvent e) {}
/*      */     
/*      */     public void mouseReleased(MouseEvent e) {
/* 1156 */       MenuSelectionManager manager = MenuSelectionManager.defaultManager();
/* 1157 */       Point p = e.getPoint();
/*      */       
/* 1159 */       if ((p.x >= 0) && (p.x < LiquidMenuItemUI.this.menuItem.getWidth()) && (p.y >= 0) && (p.y < LiquidMenuItemUI.this.menuItem.getHeight()))
/*      */       {
/* 1161 */         LiquidMenuItemUI.this.doClick(manager);
/*      */       } else {
/* 1163 */         manager.processMouseEvent(e);
/*      */       }
/*      */     }
/*      */     
/*      */     public void mouseEntered(MouseEvent e) {
/* 1168 */       MenuSelectionManager manager = MenuSelectionManager.defaultManager();
/* 1169 */       int modifiers = e.getModifiers();
/*      */       
/*      */ 
/* 1172 */       if ((modifiers & 0x1C) != 0)
/*      */       {
/*      */ 
/* 1175 */         MenuSelectionManager.defaultManager().processMouseEvent(e);
/*      */       } else {
/* 1177 */         manager.setSelectedPath(LiquidMenuItemUI.this.getPath());
/*      */       }
/*      */     }
/*      */     
/*      */     public void mouseExited(MouseEvent e) {
/* 1182 */       MenuSelectionManager manager = MenuSelectionManager.defaultManager();
/*      */       
/* 1184 */       int modifiers = e.getModifiers();
/*      */       
/*      */ 
/* 1187 */       if ((modifiers & 0x1C) != 0)
/*      */       {
/*      */ 
/* 1190 */         MenuSelectionManager.defaultManager().processMouseEvent(e);
/*      */       } else {
/* 1192 */         MenuElement[] path = manager.getSelectedPath();
/*      */         
/* 1194 */         if (path.length > 1) {
/* 1195 */           MenuElement[] newPath = new MenuElement[path.length - 1];
/*      */           
/*      */ 
/*      */ 
/* 1199 */           int i = 0; for (int c = path.length - 1; i < c; i++) {
/* 1200 */             newPath[i] = path[i];
/*      */           }
/* 1202 */           manager.setSelectedPath(newPath);
/*      */         }
/*      */       }
/*      */     }
/*      */     
/*      */     public void mouseDragged(MouseEvent e) {
/* 1208 */       MenuSelectionManager.defaultManager().processMouseEvent(e);
/*      */     }
/*      */     
/*      */     public void mouseMoved(MouseEvent e) {}
/*      */   }
/*      */   
/*      */   private class MenuDragMouseHandler implements MenuDragMouseListener {
/* 1215 */     MenuDragMouseHandler(LiquidMenuItemUI.1 x1) { this(); }
/*      */     
/*      */ 
/*      */     public void menuDragMouseDragged(MenuDragMouseEvent e)
/*      */     {
/* 1220 */       MenuSelectionManager manager = e.getMenuSelectionManager();
/* 1221 */       MenuElement[] path = e.getPath();
/* 1222 */       manager.setSelectedPath(path);
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */     public void menuDragMouseReleased(MenuDragMouseEvent e)
/*      */     {
/* 1229 */       MenuSelectionManager manager = e.getMenuSelectionManager();
/* 1230 */       MenuElement[] path = e.getPath();
/* 1231 */       Point p = e.getPoint();
/*      */       
/* 1233 */       if ((p.x >= 0) && (p.x < LiquidMenuItemUI.this.menuItem.getWidth()) && (p.y >= 0) && (p.y < LiquidMenuItemUI.this.menuItem.getHeight()))
/*      */       {
/* 1235 */         LiquidMenuItemUI.this.doClick(manager);
/*      */       } else
/* 1237 */         manager.clearSelectedPath(); }
/*      */     
/*      */     private MenuDragMouseHandler() {}
/*      */     public void menuDragMouseEntered(MenuDragMouseEvent e) {}
/*      */     public void menuDragMouseExited(MenuDragMouseEvent e) {} }
/* 1242 */   private class MenuKeyHandler implements MenuKeyListener { MenuKeyHandler(LiquidMenuItemUI.1 x1) { this(); }
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
/*      */     public void menuKeyTyped(MenuKeyEvent e)
/*      */     {
/* 1258 */       int key = LiquidMenuItemUI.this.menuItem.getMnemonic();
/*      */       
/* 1260 */       if ((key == 0) || (e.getPath().length != 2))
/*      */       {
/* 1262 */         return;
/*      */       }
/*      */       
/* 1265 */       if (lower((char)key) == lower(e.getKeyChar())) {
/* 1266 */         MenuSelectionManager manager = e.getMenuSelectionManager();
/* 1267 */         LiquidMenuItemUI.this.doClick(manager);
/* 1268 */         e.consume();
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
/*      */ 
/*      */ 
/* 1283 */     private char lower(char keyChar) { return Character.toLowerCase(keyChar); }
/*      */     private MenuKeyHandler() {}
/*      */     public void menuKeyPressed(MenuKeyEvent e) {}
/*      */     public void menuKeyReleased(MenuKeyEvent e) {} }
/* 1287 */   private class PropertyChangeHandler implements PropertyChangeListener { PropertyChangeHandler(LiquidMenuItemUI.1 x1) { this(); }
/*      */     
/* 1289 */     public void propertyChange(PropertyChangeEvent e) { String name = e.getPropertyName();
/*      */       
/* 1291 */       if ((name.equals("labelFor")) || (name.equals("displayedMnemonic")) || (name.equals("accelerator")))
/*      */       {
/* 1293 */         LiquidMenuItemUI.this.updateAcceleratorBinding();
/* 1294 */       } else if ((name.equals("text")) || ("font".equals(name)) || ("foreground".equals(name)))
/*      */       {
/*      */ 
/*      */ 
/*      */ 
/* 1299 */         JMenuItem lbl = (JMenuItem)e.getSource();
/* 1300 */         String text = lbl.getText();
/* 1301 */         BasicHTML.updateRenderer(lbl, text);
/*      */       } }
/*      */     
/*      */     private PropertyChangeHandler() {} }
/*      */   
/* 1306 */   private static class ClickAction extends AbstractAction { ClickAction(LiquidMenuItemUI.1 x0) { this(); }
/*      */     
/* 1308 */     public void actionPerformed(ActionEvent e) { JMenuItem mi = (JMenuItem)e.getSource();
/* 1309 */       MenuSelectionManager.defaultManager().clearSelectedPath();
/* 1310 */       mi.doClick();
/*      */     }
/*      */     
/*      */     private ClickAction() {}
/*      */   }
/*      */   
/*      */   public static class EmptyIcon implements Icon {
/*      */     int width;
/*      */     int height;
/*      */     
/* 1320 */     public EmptyIcon(int width, int height) { this.height = height;
/* 1321 */       this.width = width;
/*      */     }
/*      */     
/*      */ 
/*      */     public void paintIcon(Component c, Graphics g, int x, int y) {}
/*      */     
/*      */     public int getIconWidth()
/*      */     {
/* 1329 */       return this.width;
/*      */     }
/*      */     
/*      */     public int getIconHeight() {
/* 1333 */       return this.height;
/*      */     }
/*      */   }
/*      */ }


/* Location:              E:\java\java学习\hutubill\lib\all.jar!\com\birosoft\liquid\LiquidMenuItemUI.class
 * Java compiler version: 4 (48.0)
 * JD-Core Version:       0.7.1
 */
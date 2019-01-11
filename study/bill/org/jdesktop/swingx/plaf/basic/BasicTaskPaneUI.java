/*     */ package org.jdesktop.swingx.plaf.basic;
/*     */ 
/*     */ import java.awt.Color;
/*     */ import java.awt.Component;
/*     */ import java.awt.ComponentOrientation;
/*     */ import java.awt.Cursor;
/*     */ import java.awt.Dimension;
/*     */ import java.awt.Font;
/*     */ import java.awt.FontMetrics;
/*     */ import java.awt.Graphics;
/*     */ import java.awt.Insets;
/*     */ import java.awt.Rectangle;
/*     */ import java.awt.event.ActionEvent;
/*     */ import java.awt.event.FocusEvent;
/*     */ import java.awt.event.FocusListener;
/*     */ import java.awt.event.MouseEvent;
/*     */ import java.beans.PropertyChangeEvent;
/*     */ import java.beans.PropertyChangeListener;
/*     */ import javax.swing.AbstractAction;
/*     */ import javax.swing.Action;
/*     */ import javax.swing.ActionMap;
/*     */ import javax.swing.BorderFactory;
/*     */ import javax.swing.Icon;
/*     */ import javax.swing.InputMap;
/*     */ import javax.swing.JComponent;
/*     */ import javax.swing.JLabel;
/*     */ import javax.swing.LookAndFeel;
/*     */ import javax.swing.SwingUtilities;
/*     */ import javax.swing.UIManager;
/*     */ import javax.swing.border.Border;
/*     */ import javax.swing.border.CompoundBorder;
/*     */ import javax.swing.event.MouseInputAdapter;
/*     */ import javax.swing.event.MouseInputListener;
/*     */ import javax.swing.plaf.ActionMapUIResource;
/*     */ import javax.swing.plaf.ColorUIResource;
/*     */ import javax.swing.plaf.ComponentUI;
/*     */ import javax.swing.plaf.FontUIResource;
/*     */ import javax.swing.plaf.UIResource;
/*     */ import javax.swing.plaf.basic.BasicGraphicsUtils;
/*     */ import org.jdesktop.swingx.JXCollapsiblePane;
/*     */ import org.jdesktop.swingx.JXHyperlink;
/*     */ import org.jdesktop.swingx.JXTaskPane;
/*     */ import org.jdesktop.swingx.SwingXUtilities;
/*     */ import org.jdesktop.swingx.icon.EmptyIcon;
/*     */ import org.jdesktop.swingx.plaf.TaskPaneUI;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class BasicTaskPaneUI
/*     */   extends TaskPaneUI
/*     */ {
/*  74 */   private static FocusListener focusListener = new RepaintOnFocus();
/*     */   protected int titleHeight;
/*     */   
/*  77 */   public static ComponentUI createUI(JComponent c) { return new BasicTaskPaneUI(); }
/*     */   
/*     */   public BasicTaskPaneUI() {
/*  80 */     this.titleHeight = 25;
/*  81 */     this.roundHeight = 5;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   protected int roundHeight;
/*     */   
/*     */ 
/*     */   protected JXTaskPane group;
/*     */   
/*     */ 
/*     */   public void installUI(JComponent c)
/*     */   {
/*  95 */     super.installUI(c);
/*  96 */     this.group = ((JXTaskPane)c);
/*     */     
/*  98 */     installDefaults();
/*  99 */     installListeners();
/* 100 */     installKeyboardActions();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   protected boolean mouseOver;
/*     */   
/*     */ 
/*     */ 
/*     */   protected MouseInputListener mouseListener;
/*     */   
/*     */ 
/*     */ 
/*     */   protected PropertyChangeListener propertyListener;
/*     */   
/*     */ 
/*     */ 
/*     */   protected void installDefaults()
/*     */   {
/* 120 */     LookAndFeel.installProperty(this.group, "opaque", Boolean.valueOf(true));
/* 121 */     this.group.setBorder(createPaneBorder());
/* 122 */     ((JComponent)this.group.getContentPane()).setBorder(createContentPaneBorder());
/*     */     
/*     */ 
/* 125 */     LookAndFeel.installColorsAndFont(this.group, "TaskPane.background", "TaskPane.foreground", "TaskPane.font");
/*     */     
/*     */ 
/* 128 */     LookAndFeel.installColorsAndFont((JComponent)this.group.getContentPane(), "TaskPane.background", "TaskPane.foreground", "TaskPane.font");
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   protected void installListeners()
/*     */   {
/* 136 */     this.mouseListener = createMouseInputListener();
/* 137 */     this.group.addMouseMotionListener(this.mouseListener);
/* 138 */     this.group.addMouseListener(this.mouseListener);
/*     */     
/* 140 */     this.group.addFocusListener(focusListener);
/* 141 */     this.propertyListener = createPropertyListener();
/* 142 */     this.group.addPropertyChangeListener(this.propertyListener);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   protected void installKeyboardActions()
/*     */   {
/* 149 */     InputMap inputMap = (InputMap)UIManager.get("TaskPane.focusInputMap");
/* 150 */     if (inputMap != null) {
/* 151 */       SwingUtilities.replaceUIInputMap(this.group, 0, inputMap);
/*     */     }
/*     */     
/*     */ 
/* 155 */     ActionMap map = getActionMap();
/* 156 */     if (map != null) {
/* 157 */       SwingUtilities.replaceUIActionMap(this.group, map);
/*     */     }
/*     */   }
/*     */   
/*     */   ActionMap getActionMap() {
/* 162 */     ActionMap map = new ActionMapUIResource();
/* 163 */     map.put("toggleCollapsed", new ToggleCollapsedAction());
/* 164 */     return map;
/*     */   }
/*     */   
/*     */   public void uninstallUI(JComponent c)
/*     */   {
/* 169 */     uninstallListeners();
/* 170 */     super.uninstallUI(c);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   protected void uninstallListeners()
/*     */   {
/* 177 */     this.group.removeMouseListener(this.mouseListener);
/* 178 */     this.group.removeMouseMotionListener(this.mouseListener);
/* 179 */     this.group.removeFocusListener(focusListener);
/* 180 */     this.group.removePropertyChangeListener(this.propertyListener);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   protected MouseInputListener createMouseInputListener()
/*     */   {
/* 188 */     return new ToggleListener();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   protected PropertyChangeListener createPropertyListener()
/*     */   {
/* 196 */     return new ChangeListener();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected boolean isInBorder(MouseEvent event)
/*     */   {
/* 205 */     return event.getY() < getTitleHeight(event.getComponent());
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected int getTitleHeight(Component c)
/*     */   {
/* 215 */     if ((c instanceof JXTaskPane)) {
/* 216 */       JXTaskPane taskPane = (JXTaskPane)c;
/* 217 */       Font font = taskPane.getFont();
/* 218 */       int height = this.titleHeight;
/*     */       
/* 220 */       if ((font != null) && (!(font instanceof FontUIResource))) {
/* 221 */         height = Math.max(height, taskPane.getFontMetrics(font).getHeight());
/*     */       }
/*     */       
/* 224 */       Icon icon = taskPane.getIcon();
/*     */       
/* 226 */       if (icon != null) {
/* 227 */         height = Math.max(height, icon.getIconHeight() + 4);
/*     */       }
/*     */       
/* 230 */       return height;
/*     */     }
/*     */     
/* 233 */     return this.titleHeight;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   protected Border createPaneBorder()
/*     */   {
/* 241 */     return new PaneBorder();
/*     */   }
/*     */   
/*     */   public Dimension getPreferredSize(JComponent c)
/*     */   {
/* 246 */     Component component = this.group.getComponent(0);
/* 247 */     if (!(component instanceof JXCollapsiblePane))
/*     */     {
/* 249 */       return super.getPreferredSize(c);
/*     */     }
/*     */     
/* 252 */     JXCollapsiblePane collapsible = (JXCollapsiblePane)component;
/* 253 */     Dimension dim = collapsible.getPreferredSize();
/*     */     
/* 255 */     Border groupBorder = this.group.getBorder();
/* 256 */     if ((groupBorder instanceof PaneBorder)) {
/* 257 */       ((PaneBorder)groupBorder).label.setDisplayedMnemonic(this.group.getMnemonic());
/*     */       
/* 259 */       Dimension border = ((PaneBorder)groupBorder).getPreferredSize(this.group);
/*     */       
/* 261 */       dim.width = Math.max(dim.width, border.width);
/* 262 */       dim.height += border.height;
/*     */     } else {
/* 264 */       dim.height += getTitleHeight(c);
/*     */     }
/*     */     
/* 267 */     return dim;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected Border createContentPaneBorder()
/*     */   {
/* 276 */     Color borderColor = UIManager.getColor("TaskPane.borderColor");
/* 277 */     return new CompoundBorder(new ContentPaneBorder(borderColor), BorderFactory.createEmptyBorder(10, 10, 10, 10));
/*     */   }
/*     */   
/*     */ 
/*     */   public Component createAction(Action action)
/*     */   {
/* 283 */     JXHyperlink link = new JXHyperlink(action)
/*     */     {
/*     */       public void updateUI() {
/* 286 */         super.updateUI();
/*     */         
/* 288 */         BasicTaskPaneUI.this.configure(this);
/*     */       }
/* 290 */     };
/* 291 */     configure(link);
/* 292 */     return link;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected void configure(JXHyperlink link)
/*     */   {
/* 301 */     link.setOpaque(false);
/* 302 */     link.setBorderPainted(false);
/* 303 */     link.setFocusPainted(true);
/* 304 */     link.setForeground(UIManager.getColor("TaskPane.titleForeground"));
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   protected void ensureVisible()
/*     */   {
/* 311 */     SwingUtilities.invokeLater(new Runnable() {
/*     */       public void run() {
/* 313 */         BasicTaskPaneUI.this.group.scrollRectToVisible(new Rectangle(BasicTaskPaneUI.this.group.getWidth(), BasicTaskPaneUI.this.group.getHeight()));
/*     */       }
/*     */     });
/*     */   }
/*     */   
/*     */ 
/*     */   static class RepaintOnFocus
/*     */     implements FocusListener
/*     */   {
/*     */     public void focusGained(FocusEvent e)
/*     */     {
/* 324 */       e.getComponent().repaint();
/*     */     }
/*     */     
/*     */     public void focusLost(FocusEvent e) {
/* 328 */       e.getComponent().repaint();
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */   class ChangeListener
/*     */     implements PropertyChangeListener
/*     */   {
/*     */     ChangeListener() {}
/*     */     
/*     */     public void propertyChange(PropertyChangeEvent evt)
/*     */     {
/* 340 */       if ((("collapsed".equals(evt.getPropertyName())) && (Boolean.TRUE.equals(evt.getNewValue())) && (!BasicTaskPaneUI.this.group.isAnimated())) || (("animationState".equals(evt.getPropertyName())) && ("expanded".equals(evt.getNewValue()))))
/*     */       {
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 346 */         if (BasicTaskPaneUI.this.group.isScrollOnExpand()) {
/* 347 */           BasicTaskPaneUI.this.ensureVisible();
/*     */         }
/* 349 */       } else if (("icon".equals(evt.getPropertyName())) || ("title".equals(evt.getPropertyName())) || ("special".equals(evt.getPropertyName())))
/*     */       {
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 356 */         BasicTaskPaneUI.this.group.repaint();
/* 357 */       } else if ("mnemonic".equals(evt.getPropertyName())) {
/* 358 */         SwingXUtilities.updateMnemonicBinding(BasicTaskPaneUI.this.group, "toggleCollapsed");
/*     */         
/* 360 */         Border b = BasicTaskPaneUI.this.group.getBorder();
/*     */         
/* 362 */         if ((b instanceof BasicTaskPaneUI.PaneBorder)) {
/* 363 */           int key = ((Integer)evt.getNewValue()).intValue();
/* 364 */           ((BasicTaskPaneUI.PaneBorder)b).label.setDisplayedMnemonic(key);
/*     */         }
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   class ToggleListener extends MouseInputAdapter
/*     */   {
/*     */     ToggleListener() {}
/*     */     
/*     */     public void mouseEntered(MouseEvent e)
/*     */     {
/* 376 */       if (BasicTaskPaneUI.this.isInBorder(e)) {
/* 377 */         e.getComponent().setCursor(Cursor.getPredefinedCursor(12));
/*     */       }
/*     */       else {
/* 380 */         BasicTaskPaneUI.this.mouseOver = false;
/* 381 */         BasicTaskPaneUI.this.group.repaint(0, 0, BasicTaskPaneUI.this.group.getWidth(), BasicTaskPaneUI.this.getTitleHeight(BasicTaskPaneUI.this.group));
/*     */       }
/*     */     }
/*     */     
/*     */     public void mouseExited(MouseEvent e)
/*     */     {
/* 387 */       e.getComponent().setCursor(null);
/* 388 */       BasicTaskPaneUI.this.mouseOver = false;
/* 389 */       BasicTaskPaneUI.this.group.repaint(0, 0, BasicTaskPaneUI.this.group.getWidth(), BasicTaskPaneUI.this.getTitleHeight(BasicTaskPaneUI.this.group));
/*     */     }
/*     */     
/*     */     public void mouseMoved(MouseEvent e)
/*     */     {
/* 394 */       if (BasicTaskPaneUI.this.isInBorder(e)) {
/* 395 */         e.getComponent().setCursor(Cursor.getPredefinedCursor(12));
/*     */         
/* 397 */         BasicTaskPaneUI.this.mouseOver = true;
/*     */       } else {
/* 399 */         e.getComponent().setCursor(null);
/* 400 */         BasicTaskPaneUI.this.mouseOver = false;
/*     */       }
/*     */       
/* 403 */       BasicTaskPaneUI.this.group.repaint(0, 0, BasicTaskPaneUI.this.group.getWidth(), BasicTaskPaneUI.this.getTitleHeight(BasicTaskPaneUI.this.group));
/*     */     }
/*     */     
/*     */     public void mouseReleased(MouseEvent e)
/*     */     {
/* 408 */       if ((SwingUtilities.isLeftMouseButton(e)) && (BasicTaskPaneUI.this.isInBorder(e))) {
/* 409 */         BasicTaskPaneUI.this.group.setCollapsed(!BasicTaskPaneUI.this.group.isCollapsed());
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   class ToggleCollapsedAction
/*     */     extends AbstractAction
/*     */   {
/*     */     private static final long serialVersionUID = 5676859881615358815L;
/*     */     
/*     */ 
/*     */     public ToggleCollapsedAction()
/*     */     {
/* 424 */       super();
/*     */     }
/*     */     
/*     */     public void actionPerformed(ActionEvent e) {
/* 428 */       BasicTaskPaneUI.this.group.setCollapsed(!BasicTaskPaneUI.this.group.isCollapsed());
/*     */     }
/*     */     
/*     */     public boolean isEnabled()
/*     */     {
/* 433 */       return BasicTaskPaneUI.this.group.isVisible();
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */   protected static class ChevronIcon
/*     */     implements Icon
/*     */   {
/* 441 */     boolean up = true;
/*     */     
/*     */     public ChevronIcon(boolean up) {
/* 444 */       this.up = up;
/*     */     }
/*     */     
/*     */     public int getIconHeight() {
/* 448 */       return 3;
/*     */     }
/*     */     
/*     */     public int getIconWidth() {
/* 452 */       return 6;
/*     */     }
/*     */     
/*     */     public void paintIcon(Component c, Graphics g, int x, int y) {
/* 456 */       if (this.up) {
/* 457 */         g.drawLine(x + 3, y, x, y + 3);
/* 458 */         g.drawLine(x + 3, y, x + 6, y + 3);
/*     */       } else {
/* 460 */         g.drawLine(x, y, x + 3, y + 3);
/* 461 */         g.drawLine(x + 3, y + 3, x + 6, y);
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   protected static class ContentPaneBorder
/*     */     implements Border, UIResource
/*     */   {
/*     */     Color color;
/*     */     
/*     */     public ContentPaneBorder(Color color)
/*     */     {
/* 473 */       this.color = color;
/*     */     }
/*     */     
/*     */     public Insets getBorderInsets(Component c) {
/* 477 */       return new Insets(0, 1, 1, 1);
/*     */     }
/*     */     
/*     */     public boolean isBorderOpaque() {
/* 481 */       return true;
/*     */     }
/*     */     
/*     */     public void paintBorder(Component c, Graphics g, int x, int y, int width, int height)
/*     */     {
/* 486 */       g.setColor(this.color);
/* 487 */       g.drawLine(x, y, x, y + height - 1);
/* 488 */       g.drawLine(x, y + height - 1, x + width - 1, y + height - 1);
/* 489 */       g.drawLine(x + width - 1, y, x + width - 1, y + height - 1);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */   protected class PaneBorder
/*     */     implements Border, UIResource
/*     */   {
/*     */     protected Color borderColor;
/*     */     
/*     */     protected Color titleForeground;
/*     */     
/*     */     protected Color specialTitleBackground;
/*     */     
/*     */     protected Color specialTitleForeground;
/*     */     
/*     */     protected Color titleBackgroundGradientStart;
/*     */     
/*     */     protected Color titleBackgroundGradientEnd;
/*     */     
/*     */     protected Color titleOver;
/*     */     
/*     */     protected Color specialTitleOver;
/*     */     protected JLabel label;
/*     */     
/*     */     public PaneBorder()
/*     */     {
/* 516 */       this.borderColor = UIManager.getColor("TaskPane.borderColor");
/*     */       
/* 518 */       this.titleForeground = UIManager.getColor("TaskPane.titleForeground");
/*     */       
/* 520 */       this.specialTitleBackground = UIManager.getColor("TaskPane.specialTitleBackground");
/*     */       
/* 522 */       this.specialTitleForeground = UIManager.getColor("TaskPane.specialTitleForeground");
/*     */       
/*     */ 
/* 525 */       this.titleBackgroundGradientStart = UIManager.getColor("TaskPane.titleBackgroundGradientStart");
/*     */       
/* 527 */       this.titleBackgroundGradientEnd = UIManager.getColor("TaskPane.titleBackgroundGradientEnd");
/*     */       
/*     */ 
/* 530 */       this.titleOver = UIManager.getColor("TaskPane.titleOver");
/* 531 */       if (this.titleOver == null) {
/* 532 */         this.titleOver = this.specialTitleBackground.brighter();
/*     */       }
/* 534 */       this.specialTitleOver = UIManager.getColor("TaskPane.specialTitleOver");
/* 535 */       if (this.specialTitleOver == null) {
/* 536 */         this.specialTitleOver = this.specialTitleBackground.brighter();
/*     */       }
/*     */       
/* 539 */       this.label = new JLabel();
/* 540 */       this.label.setOpaque(false);
/* 541 */       this.label.setIconTextGap(8);
/*     */     }
/*     */     
/*     */     public Insets getBorderInsets(Component c) {
/* 545 */       return new Insets(BasicTaskPaneUI.this.getTitleHeight(c), 0, 0, 0);
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     public boolean isBorderOpaque()
/*     */     {
/* 556 */       return true;
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     public Dimension getPreferredSize(JXTaskPane group)
/*     */     {
/* 569 */       configureLabel(group);
/* 570 */       Dimension dim = this.label.getPreferredSize();
/*     */       
/* 572 */       dim.width += 3;
/*     */       
/* 574 */       dim.width += BasicTaskPaneUI.this.getTitleHeight(group);
/*     */       
/* 576 */       dim.width += 3;
/*     */       
/* 578 */       dim.height = BasicTaskPaneUI.this.getTitleHeight(group);
/* 579 */       return dim;
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     protected void paintTitleBackground(JXTaskPane group, Graphics g)
/*     */     {
/* 592 */       if (group.isSpecial()) {
/* 593 */         g.setColor(this.specialTitleBackground);
/*     */       } else {
/* 595 */         g.setColor(this.titleBackgroundGradientStart);
/*     */       }
/* 597 */       g.fillRect(0, 0, group.getWidth(), BasicTaskPaneUI.this.getTitleHeight(group) - 1);
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     protected void paintTitle(JXTaskPane group, Graphics g, Color textColor, int x, int y, int width, int height)
/*     */     {
/* 620 */       configureLabel(group);
/* 621 */       this.label.setForeground(textColor);
/* 622 */       if ((group.getFont() != null) && (!(group.getFont() instanceof FontUIResource))) {
/* 623 */         this.label.setFont(group.getFont());
/*     */       }
/* 625 */       g.translate(x, y);
/* 626 */       this.label.setBounds(0, 0, width, height);
/* 627 */       this.label.paint(g);
/* 628 */       g.translate(-x, -y);
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     protected void configureLabel(JXTaskPane group)
/*     */     {
/* 639 */       this.label.applyComponentOrientation(group.getComponentOrientation());
/* 640 */       this.label.setFont(group.getFont());
/* 641 */       this.label.setText(group.getTitle());
/* 642 */       this.label.setIcon(group.getIcon() == null ? new EmptyIcon() : group.getIcon());
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     protected void paintExpandedControls(JXTaskPane group, Graphics g, int x, int y, int width, int height) {}
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     protected Color getPaintColor(JXTaskPane group)
/*     */     {
/*     */       Color paintColor;
/*     */       
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       Color paintColor;
/*     */       
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 675 */       if (isMouseOverBorder()) { Color paintColor;
/* 676 */         if (BasicTaskPaneUI.this.mouseOver) { Color paintColor;
/* 677 */           if (group.isSpecial()) {
/* 678 */             paintColor = this.specialTitleOver;
/*     */           } else
/* 680 */             paintColor = this.titleOver;
/*     */         } else {
/*     */           Color paintColor;
/* 683 */           if (group.isSpecial()) {
/* 684 */             paintColor = this.specialTitleForeground;
/*     */           } else
/* 686 */             paintColor = (group.getForeground() == null) || ((group.getForeground() instanceof ColorUIResource)) ? this.titleForeground : group.getForeground();
/*     */         }
/*     */       } else {
/*     */         Color paintColor;
/* 690 */         if (group.isSpecial()) {
/* 691 */           paintColor = this.specialTitleForeground;
/*     */         } else {
/* 693 */           paintColor = (group.getForeground() == null) || ((group.getForeground() instanceof ColorUIResource)) ? this.titleForeground : group.getForeground();
/*     */         }
/*     */       }
/* 696 */       return paintColor;
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     public void paintBorder(Component c, Graphics g, int x, int y, int width, int height)
/*     */     {
/* 706 */       JXTaskPane group = (JXTaskPane)c;
/*     */       
/*     */ 
/* 709 */       int controlWidth = BasicTaskPaneUI.this.getTitleHeight(group) - 2 * BasicTaskPaneUI.this.getRoundHeight();
/* 710 */       int controlX = group.getWidth() - BasicTaskPaneUI.this.getTitleHeight(group);
/* 711 */       int controlY = BasicTaskPaneUI.this.getRoundHeight() - 1;
/* 712 */       int titleX = 3;
/* 713 */       int titleY = 0;
/* 714 */       int titleWidth = group.getWidth() - BasicTaskPaneUI.this.getTitleHeight(group) - 3;
/* 715 */       int titleHeight = BasicTaskPaneUI.this.getTitleHeight(group);
/*     */       
/* 717 */       if (!group.getComponentOrientation().isLeftToRight()) {
/* 718 */         controlX = group.getWidth() - controlX - controlWidth;
/* 719 */         titleX = group.getWidth() - titleX - titleWidth;
/*     */       }
/*     */       
/*     */ 
/* 723 */       paintTitleBackground(group, g);
/*     */       
/*     */ 
/* 726 */       paintExpandedControls(group, g, controlX, controlY, controlWidth, controlWidth);
/*     */       
/*     */ 
/*     */ 
/* 730 */       Color paintColor = getPaintColor(group);
/*     */       
/*     */ 
/* 733 */       if (group.hasFocus()) {
/* 734 */         paintFocus(g, paintColor, 3, 3, width - 6, BasicTaskPaneUI.this.getTitleHeight(group) - 6);
/*     */       }
/*     */       
/* 737 */       paintTitle(group, g, paintColor, titleX, titleY, titleWidth, titleHeight);
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     protected void paintRectAroundControls(JXTaskPane group, Graphics g, int x, int y, int width, int height, Color highColor, Color lowColor)
/*     */     {
/* 760 */       if (BasicTaskPaneUI.this.mouseOver) {
/* 761 */         int x2 = x + width;
/* 762 */         int y2 = y + height;
/* 763 */         g.setColor(highColor);
/* 764 */         g.drawLine(x, y, x2, y);
/* 765 */         g.drawLine(x, y, x, y2);
/* 766 */         g.setColor(lowColor);
/* 767 */         g.drawLine(x2, y, x2, y2);
/* 768 */         g.drawLine(x, y2, x2, y2);
/*     */       }
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     protected void paintOvalAroundControls(JXTaskPane group, Graphics g, int x, int y, int width, int height)
/*     */     {
/* 790 */       if (group.isSpecial()) {
/* 791 */         g.setColor(this.specialTitleBackground.brighter());
/* 792 */         g.drawOval(x, y, width, height);
/*     */       } else {
/* 794 */         g.setColor(this.titleBackgroundGradientStart);
/* 795 */         g.fillOval(x, y, width, height);
/*     */         
/* 797 */         g.setColor(this.titleBackgroundGradientEnd.darker());
/* 798 */         g.drawOval(x, y, width, width);
/*     */       }
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     protected void paintChevronControls(JXTaskPane group, Graphics g, int x, int y, int width, int height)
/*     */     {
/*     */       BasicTaskPaneUI.ChevronIcon chevron;
/*     */       
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       BasicTaskPaneUI.ChevronIcon chevron;
/*     */       
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 821 */       if (group.isCollapsed()) {
/* 822 */         chevron = new BasicTaskPaneUI.ChevronIcon(false);
/*     */       } else {
/* 824 */         chevron = new BasicTaskPaneUI.ChevronIcon(true);
/*     */       }
/* 826 */       int chevronX = x + width / 2 - chevron.getIconWidth() / 2;
/* 827 */       int chevronY = y + (height / 2 - chevron.getIconHeight());
/* 828 */       chevron.paintIcon(group, g, chevronX, chevronY);
/* 829 */       chevron.paintIcon(group, g, chevronX, chevronY + chevron.getIconHeight() + 1);
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     protected void paintFocus(Graphics g, Color paintColor, int x, int y, int width, int height)
/*     */     {
/* 851 */       g.setColor(paintColor);
/* 852 */       BasicGraphicsUtils.drawDashedRect(g, x, y, width, height);
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     protected boolean isMouseOverBorder()
/*     */     {
/* 862 */       return false;
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected int getRoundHeight()
/*     */   {
/* 872 */     return this.roundHeight;
/*     */   }
/*     */ }


/* Location:              E:\java\java学习\hutubill\lib\all.jar!\org\jdesktop\swingx\plaf\basic\BasicTaskPaneUI.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */
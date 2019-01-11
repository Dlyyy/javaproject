/*     */ package org.jdesktop.swingx;
/*     */ 
/*     */ import java.awt.Color;
/*     */ import java.awt.Component;
/*     */ import java.awt.ComponentOrientation;
/*     */ import java.awt.Container;
/*     */ import java.awt.Dimension;
/*     */ import java.awt.Font;
/*     */ import java.awt.Graphics2D;
/*     */ import java.awt.Insets;
/*     */ import java.awt.Point;
/*     */ import java.awt.Rectangle;
/*     */ import java.awt.Window;
/*     */ import java.awt.event.MouseEvent;
/*     */ import java.io.IOException;
/*     */ import java.io.StringReader;
/*     */ import java.lang.reflect.Method;
/*     */ import java.util.Locale;
/*     */ import javax.swing.InputMap;
/*     */ import javax.swing.JComponent;
/*     */ import javax.swing.JList;
/*     */ import javax.swing.JPopupMenu;
/*     */ import javax.swing.KeyStroke;
/*     */ import javax.swing.ListCellRenderer;
/*     */ import javax.swing.ListModel;
/*     */ import javax.swing.ListSelectionModel;
/*     */ import javax.swing.MenuElement;
/*     */ import javax.swing.RepaintManager;
/*     */ import javax.swing.SwingUtilities;
/*     */ import javax.swing.plaf.ComponentInputMapUIResource;
/*     */ import javax.swing.plaf.UIResource;
/*     */ import javax.swing.text.html.HTMLDocument;
/*     */ import javax.swing.text.html.StyleSheet;
/*     */ import org.jdesktop.swingx.painter.Painter;
/*     */ import org.jdesktop.swingx.plaf.PainterUIResource;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public final class SwingXUtilities
/*     */ {
/*     */   public static void updateMnemonicBinding(JComponent c, String pressed)
/*     */   {
/*  95 */     updateMnemonicBinding(c, pressed, null);
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
/*     */   public static void updateMnemonicBinding(JComponent c, String pressed, String released)
/*     */   {
/* 122 */     Class<?> clazz = c.getClass();
/* 123 */     int m = -1;
/*     */     try
/*     */     {
/* 126 */       Method mtd = clazz.getMethod("getMnemonic", new Class[0]);
/* 127 */       m = ((Integer)mtd.invoke(c, new Object[0])).intValue();
/*     */     } catch (RuntimeException e) {
/* 129 */       throw e;
/*     */     } catch (Exception e) {
/* 131 */       throw new IllegalArgumentException("unable to access mnemonic", e);
/*     */     }
/*     */     
/* 134 */     InputMap map = SwingUtilities.getUIInputMap(c, 2);
/*     */     
/*     */ 
/* 137 */     if (m != 0) {
/* 138 */       if (map == null) {
/* 139 */         map = new ComponentInputMapUIResource(c);
/* 140 */         SwingUtilities.replaceUIInputMap(c, 2, map);
/*     */       }
/*     */       
/*     */ 
/* 144 */       map.clear();
/*     */       
/*     */ 
/* 147 */       map.put(KeyStroke.getKeyStroke(m, 8, false), pressed);
/*     */       
/* 149 */       map.put(KeyStroke.getKeyStroke(m, 8, true), released);
/*     */       
/* 151 */       map.put(KeyStroke.getKeyStroke(m, 0, true), released);
/*     */     }
/* 153 */     else if (map != null) {
/* 154 */       map.clear();
/*     */     }
/*     */   }
/*     */   
/*     */   static <C extends JComponent,  extends BackgroundPaintable> void installBackground(C comp, Color color)
/*     */   {
/* 160 */     if (isUIInstallable(color))
/*     */     {
/* 162 */       if ((((BackgroundPaintable)comp).getBackgroundPainter() instanceof UIResource)) {
/* 163 */         ((BackgroundPaintable)comp).setBackgroundPainter(new PainterUIResource(new BackgroundPainter(color)));
/*     */       }
/*     */     }
/*     */     else {
/* 167 */       ((BackgroundPaintable)comp).setBackgroundPainter(new BackgroundPainter(color));
/*     */     }
/*     */   }
/*     */   
/*     */   static <C extends JComponent,  extends BackgroundPaintable> void paintBackground(C comp, Graphics2D g)
/*     */   {
/* 173 */     Painter painter = ((BackgroundPaintable)comp).getBackgroundPainter();
/*     */     
/* 175 */     if ((painter instanceof BackgroundPainter))
/*     */     {
/* 177 */       painter.paint(g, comp, comp.getWidth(), comp.getHeight());
/* 178 */     } else if (painter != null) {
/* 179 */       if (((BackgroundPaintable)comp).isPaintBorderInsets()) {
/* 180 */         painter.paint(g, comp, comp.getWidth(), comp.getHeight());
/*     */       } else {
/* 182 */         Insets insets = comp.getInsets();
/* 183 */         g.translate(insets.left, insets.top);
/* 184 */         painter.paint(g, comp, comp.getWidth() - insets.left - insets.right, comp.getHeight() - insets.top - insets.bottom);
/*     */         
/* 186 */         g.translate(-insets.left, -insets.top);
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   private static Component[] getChildren(Component c) {
/* 192 */     Component[] children = null;
/*     */     
/* 194 */     if ((c instanceof MenuElement)) {
/* 195 */       MenuElement[] elements = ((MenuElement)c).getSubElements();
/* 196 */       children = new Component[elements.length];
/*     */       
/* 198 */       for (int i = 0; i < elements.length; i++) {
/* 199 */         children[i] = elements[i].getComponent();
/*     */       }
/* 201 */     } else if ((c instanceof Container)) {
/* 202 */       children = ((Container)c).getComponents();
/*     */     }
/*     */     
/* 205 */     return children;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static void setComponentTreeEnabled(Component c, boolean enabled)
/*     */   {
/* 217 */     c.setEnabled(enabled);
/*     */     
/* 219 */     Component[] children = getChildren(c);
/*     */     
/* 221 */     if (children != null) {
/* 222 */       for (int i = 0; i < children.length; i++) {
/* 223 */         setComponentTreeEnabled(children[i], enabled);
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
/*     */   public static void setComponentTreeLocale(Component c, Locale locale)
/*     */   {
/* 238 */     c.setLocale(locale);
/*     */     
/* 240 */     Component[] children = getChildren(c);
/*     */     
/* 242 */     if (children != null) {
/* 243 */       for (int i = 0; i < children.length; i++) {
/* 244 */         setComponentTreeLocale(children[i], locale);
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
/*     */   public static void setComponentTreeBackground(Component c, Color color)
/*     */   {
/* 259 */     c.setBackground(color);
/*     */     
/* 261 */     Component[] children = getChildren(c);
/*     */     
/* 263 */     if (children != null) {
/* 264 */       for (int i = 0; i < children.length; i++) {
/* 265 */         setComponentTreeBackground(children[i], color);
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
/*     */   public static void setComponentTreeForeground(Component c, Color color)
/*     */   {
/* 280 */     c.setForeground(color);
/*     */     
/* 282 */     Component[] children = getChildren(c);
/*     */     
/* 284 */     if (children != null) {
/* 285 */       for (int i = 0; i < children.length; i++) {
/* 286 */         setComponentTreeForeground(children[i], color);
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
/*     */   public static void setComponentTreeFont(Component c, Font font)
/*     */   {
/* 300 */     c.setFont(font);
/*     */     
/* 302 */     Component[] children = getChildren(c);
/*     */     
/* 304 */     if (children != null) {
/* 305 */       for (int i = 0; i < children.length; i++) {
/* 306 */         setComponentTreeFont(children[i], font);
/*     */       }
/*     */     }
/*     */   }
/*     */   
/* 311 */   private static String STYLESHEET = "body { margin-top: 0; margin-bottom: 0; margin-left: 0; margin-right: 0; font-family: %s; font-size: %dpt;  }a, p, li { margin-top: 0; margin-bottom: 0; margin-left: 0; margin-right: 0; font-family: %s; font-size: %dpt;  }";
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static void setHtmlFont(HTMLDocument doc, Font font)
/*     */   {
/* 332 */     String stylesheet = String.format(STYLESHEET, new Object[] { font.getName(), Integer.valueOf(font.getSize()), font.getName(), Integer.valueOf(font.getSize()) });
/*     */     
/*     */     try
/*     */     {
/* 336 */       doc.getStyleSheet().loadRules(new StringReader(stylesheet), null);
/*     */     }
/*     */     catch (IOException e) {
/* 339 */       throw new IllegalStateException(e);
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
/*     */   public static void updateAllComponentTreeUIs()
/*     */   {
/* 353 */     for (Window window : ) {
/* 354 */       SwingUtilities.updateComponentTreeUI(window);
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
/*     */   public static void updateAllComponentTreeUIs(Window window)
/*     */   {
/* 368 */     SwingUtilities.updateComponentTreeUI(window);
/* 369 */     for (Window owned : window.getOwnedWindows()) {
/* 370 */       updateAllComponentTreeUIs(owned);
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
/*     */   public static <T> T getAncestor(Class<T> clazz, Component c)
/*     */   {
/* 392 */     if ((clazz == null) || (c == null)) {
/* 393 */       return null;
/*     */     }
/*     */     
/* 396 */     Component parent = c.getParent();
/*     */     
/* 398 */     while ((parent != null) && (!clazz.isInstance(parent))) {
/* 399 */       parent = (parent instanceof JPopupMenu) ? ((JPopupMenu)parent).getInvoker() : parent.getParent();
/*     */     }
/*     */     
/*     */ 
/* 403 */     return parent;
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
/*     */   public static boolean isDescendingFrom(Component focusOwner, Component parent)
/*     */   {
/* 417 */     while (focusOwner != null) {
/* 418 */       if ((focusOwner instanceof JPopupMenu)) {
/* 419 */         focusOwner = ((JPopupMenu)focusOwner).getInvoker();
/* 420 */         if (focusOwner == null) {
/* 421 */           return false;
/*     */         }
/*     */       }
/* 424 */       if (focusOwner == parent) {
/* 425 */         return true;
/*     */       }
/* 427 */       focusOwner = focusOwner.getParent();
/*     */     }
/* 429 */     return false;
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
/*     */   static RepaintManager getTranslucentRepaintManager(RepaintManager delegate)
/*     */   {
/* 445 */     RepaintManager manager = delegate;
/*     */     
/* 447 */     while ((manager != null) && (!manager.getClass().isAnnotationPresent(TranslucentRepaintManager.class))) {
/* 448 */       if ((manager instanceof ForwardingRepaintManager)) {
/* 449 */         manager = ((ForwardingRepaintManager)manager).getDelegateManager();
/*     */       } else {
/* 451 */         manager = null;
/*     */       }
/*     */     }
/*     */     
/* 455 */     return manager == null ? new RepaintManagerX(delegate) : delegate;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static boolean isUIInstallable(Object property)
/*     */   {
/* 467 */     return (property == null) || ((property instanceof UIResource));
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
/*     */   public static void setLeadAnchorWithoutSelection(ListSelectionModel selectionModel, int lead, int anchor)
/*     */   {
/* 484 */     if (anchor == -1) {
/* 485 */       anchor = lead;
/*     */     }
/* 487 */     if (lead == -1) {
/* 488 */       selectionModel.setAnchorSelectionIndex(-1);
/* 489 */       selectionModel.setLeadSelectionIndex(-1);
/*     */     } else {
/* 491 */       if (selectionModel.isSelectedIndex(lead)) {
/* 492 */         selectionModel.addSelectionInterval(lead, lead);
/*     */       } else {
/* 494 */         selectionModel.removeSelectionInterval(lead, lead);
/*     */       }
/* 496 */       selectionModel.setAnchorSelectionIndex(anchor);
/*     */     }
/*     */   }
/*     */   
/*     */   public static boolean shouldIgnore(MouseEvent mouseEvent, JComponent component)
/*     */   {
/* 502 */     return (component == null) || (!component.isEnabled()) || (!SwingUtilities.isLeftMouseButton(mouseEvent)) || (mouseEvent.isConsumed());
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public static int loc2IndexFileList(JList list, Point point)
/*     */   {
/* 509 */     int i = list.locationToIndex(point);
/* 510 */     if (i != -1) {
/* 511 */       Object localObject = list.getClientProperty("List.isFileList");
/*     */       
/* 513 */       if (((localObject instanceof Boolean)) && (((Boolean)localObject).booleanValue()) && (!pointIsInActualBounds(list, i, point)))
/*     */       {
/*     */ 
/*     */ 
/* 517 */         i = -1;
/*     */       }
/*     */     }
/* 520 */     return i;
/*     */   }
/*     */   
/*     */ 
/*     */   private static boolean pointIsInActualBounds(JList list, int index, Point point)
/*     */   {
/* 526 */     ListCellRenderer renderer = list.getCellRenderer();
/* 527 */     ListModel model = list.getModel();
/* 528 */     Object element = model.getElementAt(index);
/* 529 */     Component comp = renderer.getListCellRendererComponent(list, element, index, false, false);
/*     */     
/*     */ 
/* 532 */     Dimension prefSize = comp.getPreferredSize();
/* 533 */     Rectangle cellBounds = list.getCellBounds(index, index);
/* 534 */     if (!comp.getComponentOrientation().isLeftToRight()) {
/* 535 */       cellBounds.x += cellBounds.width - prefSize.width;
/*     */     }
/* 537 */     cellBounds.width = prefSize.width;
/*     */     
/* 539 */     return cellBounds.contains(point);
/*     */   }
/*     */   
/*     */   public static void adjustFocus(JComponent component) {
/* 543 */     if ((!component.hasFocus()) && (component.isRequestFocusEnabled())) {
/* 544 */       component.requestFocus();
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */   public static int convertModifiersToDropAction(int modifiers, int sourcActions)
/*     */   {
/* 551 */     int i = 0;
/*     */     
/* 553 */     switch (modifiers & 0xC0) {
/*     */     case 192: 
/* 555 */       i = 1073741824;
/* 556 */       break;
/*     */     case 128: 
/* 558 */       i = 1;
/* 559 */       break;
/*     */     case 64: 
/* 561 */       i = 2;
/* 562 */       break;
/*     */     default: 
/* 564 */       if ((sourcActions & 0x2) != 0) {
/* 565 */         i = 2;
/*     */ 
/*     */       }
/* 568 */       else if ((sourcActions & 0x1) != 0) {
/* 569 */         i = 1;
/*     */ 
/*     */       }
/* 572 */       else if ((sourcActions & 0x40000000) != 0)
/*     */       {
/* 574 */         i = 1073741824;
/*     */       }
/*     */       break;
/*     */     }
/* 578 */     return i & sourcActions;
/*     */   }
/*     */ }


/* Location:              E:\java\java学习\hutubill\lib\all.jar!\org\jdesktop\swingx\SwingXUtilities.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */
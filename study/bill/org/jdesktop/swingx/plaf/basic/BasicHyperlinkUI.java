/*     */ package org.jdesktop.swingx.plaf.basic;
/*     */ 
/*     */ import java.awt.Color;
/*     */ import java.awt.Container;
/*     */ import java.awt.Cursor;
/*     */ import java.awt.Font;
/*     */ import java.awt.FontMetrics;
/*     */ import java.awt.Graphics;
/*     */ import java.awt.Insets;
/*     */ import java.awt.Rectangle;
/*     */ import java.awt.Shape;
/*     */ import java.beans.PropertyChangeEvent;
/*     */ import java.beans.PropertyChangeListener;
/*     */ import java.io.Reader;
/*     */ import java.io.StringReader;
/*     */ import java.lang.reflect.Method;
/*     */ import java.net.URL;
/*     */ import java.util.logging.Logger;
/*     */ import javax.swing.AbstractButton;
/*     */ import javax.swing.BorderFactory;
/*     */ import javax.swing.ButtonModel;
/*     */ import javax.swing.Icon;
/*     */ import javax.swing.JComponent;
/*     */ import javax.swing.JToolBar;
/*     */ import javax.swing.LookAndFeel;
/*     */ import javax.swing.SwingUtilities;
/*     */ import javax.swing.UIManager;
/*     */ import javax.swing.event.ChangeEvent;
/*     */ import javax.swing.plaf.BorderUIResource;
/*     */ import javax.swing.plaf.ComponentUI;
/*     */ import javax.swing.plaf.basic.BasicButtonListener;
/*     */ import javax.swing.plaf.basic.BasicButtonUI;
/*     */ import javax.swing.plaf.basic.BasicGraphicsUtils;
/*     */ import javax.swing.plaf.basic.BasicHTML;
/*     */ import javax.swing.text.AttributeSet;
/*     */ import javax.swing.text.BadLocationException;
/*     */ import javax.swing.text.Document;
/*     */ import javax.swing.text.Element;
/*     */ import javax.swing.text.Position.Bias;
/*     */ import javax.swing.text.View;
/*     */ import javax.swing.text.ViewFactory;
/*     */ import javax.swing.text.html.HTMLDocument;
/*     */ import javax.swing.text.html.HTMLEditorKit;
/*     */ import javax.swing.text.html.HTMLEditorKit.HTMLFactory;
/*     */ import javax.swing.text.html.ImageView;
/*     */ import javax.swing.text.html.StyleSheet;
/*     */ import org.jdesktop.swingx.JXHyperlink;
/*     */ import org.jdesktop.swingx.SwingXUtilities;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class BasicHyperlinkUI
/*     */   extends BasicButtonUI
/*     */ {
/*  79 */   private static final Logger LOG = Logger.getLogger(BasicHyperlinkUI.class.getName());
/*     */   
/*     */   public static ComponentUI createUI(JComponent c)
/*     */   {
/*  83 */     return new BasicHyperlinkUI();
/*     */   }
/*     */   
/*  86 */   private static Rectangle viewRect = new Rectangle();
/*     */   
/*  88 */   private static Rectangle textRect = new Rectangle();
/*     */   
/*  90 */   private static Rectangle iconRect = new Rectangle();
/*     */   
/*     */ 
/*     */   protected int dashedRectGapX;
/*     */   
/*     */ 
/*     */   protected int dashedRectGapY;
/*     */   
/*     */   protected int dashedRectGapWidth;
/*     */   
/*     */   protected int dashedRectGapHeight;
/*     */   
/*     */   private Color focusColor;
/*     */   private View ulv;
/*     */   private PropertyChangeListener pcListener;
/*     */   
/* 106 */   public BasicHyperlinkUI() { this.pcListener = new PropertyChangeListener()
/*     */     {
/*     */ 
/*     */       public void propertyChange(PropertyChangeEvent evt)
/*     */       {
/*     */ 
/* 112 */         BasicHyperlinkUI.this.ulv = null;
/*     */       }
/*     */     }; }
/*     */   
/*     */   protected void installDefaults(AbstractButton b) {
/* 117 */     super.installDefaults(b);
/*     */     
/* 119 */     JXHyperlink link = (JXHyperlink)b;
/*     */     
/* 121 */     LookAndFeel.installProperty(b, "opaque", Boolean.valueOf(false));
/*     */     
/* 123 */     if (SwingXUtilities.isUIInstallable(link.getUnclickedColor())) {
/* 124 */       link.setUnclickedColor(UIManager.getColor("Hyperlink.linkColor"));
/*     */     }
/*     */     
/* 127 */     if (SwingXUtilities.isUIInstallable(link.getClickedColor())) {
/* 128 */       link.setClickedColor(UIManager.getColor("Hyperlink.visitedColor"));
/*     */     }
/*     */     
/* 131 */     b.setBorderPainted(false);
/* 132 */     b.setRolloverEnabled(true);
/*     */     
/* 134 */     if (SwingXUtilities.isUIInstallable(b.getBorder())) {
/* 135 */       b.setBorder(new BorderUIResource(BorderFactory.createEmptyBorder(0, 1, 0, 0)));
/*     */     }
/*     */     
/* 138 */     this.dashedRectGapX = UIManager.getInt("ButtonUI.dashedRectGapX");
/* 139 */     this.dashedRectGapY = UIManager.getInt("ButtonUI.dashedRectGapY");
/* 140 */     this.dashedRectGapWidth = UIManager.getInt("ButtonUI.dashedRectGapWidth");
/* 141 */     this.dashedRectGapHeight = UIManager.getInt("ButtonUI.dashedRectGapHeight");
/* 142 */     this.focusColor = UIManager.getColor("ButtonUI.focus");
/*     */     
/* 144 */     b.setHorizontalAlignment(10);
/*     */   }
/*     */   
/*     */   protected void installListeners(AbstractButton b)
/*     */   {
/* 149 */     super.installListeners(b);
/*     */     
/* 151 */     b.addPropertyChangeListener(this.pcListener);
/*     */   }
/*     */   
/*     */   protected void uninstallListeners(AbstractButton b)
/*     */   {
/* 156 */     super.uninstallListeners(b);
/*     */     
/* 158 */     b.removePropertyChangeListener(this.pcListener);
/*     */   }
/*     */   
/*     */   protected Color getFocusColor() {
/* 162 */     return this.focusColor;
/*     */   }
/*     */   
/*     */   public void paint(Graphics g, JComponent c)
/*     */   {
/* 167 */     AbstractButton b = (AbstractButton)c;
/* 168 */     ButtonModel model = b.getModel();
/*     */     
/* 170 */     FontMetrics fm = g.getFontMetrics();
/*     */     
/* 172 */     Insets i = c.getInsets();
/*     */     
/* 174 */     viewRect.x = i.left;
/* 175 */     viewRect.y = i.top;
/* 176 */     viewRect.width = (b.getWidth() - (i.right + viewRect.x));
/* 177 */     viewRect.height = (b.getHeight() - (i.bottom + viewRect.y));
/*     */     
/* 179 */     textRect.x = (textRect.y = textRect.width = textRect.height = 0);
/* 180 */     iconRect.x = (iconRect.y = iconRect.width = iconRect.height = 0);
/*     */     
/* 182 */     Font f = c.getFont();
/* 183 */     g.setFont(f);
/*     */     
/*     */ 
/* 186 */     String text = SwingUtilities.layoutCompoundLabel(c, fm, b.getText(), b.getIcon(), b.getVerticalAlignment(), b.getHorizontalAlignment(), b.getVerticalTextPosition(), b.getHorizontalTextPosition(), viewRect, iconRect, textRect, b.getText() == null ? 0 : b.getIconTextGap());
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 192 */     clearTextShiftOffset();
/*     */     
/*     */ 
/* 195 */     if ((model.isArmed()) && (model.isPressed())) {
/* 196 */       paintButtonPressed(g, b);
/*     */     }
/*     */     
/*     */ 
/* 200 */     if (b.getIcon() != null) {
/* 201 */       paintIcon(g, c, iconRect);
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 211 */     if ((text != null) && (!text.equals(""))) {
/* 212 */       View v = (View)c.getClientProperty("html");
/* 213 */       if (v != null) {
/* 214 */         paintHTMLText(g, b, textRect, text, v);
/*     */       } else {
/* 216 */         paintText(g, b, textRect, text);
/*     */       }
/*     */     }
/*     */     
/* 220 */     if ((b.isFocusPainted()) && (b.hasFocus()))
/*     */     {
/* 222 */       paintFocus(g, b, viewRect, textRect, iconRect);
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
/*     */   protected void paintHTMLText(Graphics g, AbstractButton b, Rectangle textRect, String text, View v)
/*     */   {
/* 239 */     textRect.x += getTextShiftOffset();
/* 240 */     textRect.y += getTextShiftOffset();
/*     */     
/* 242 */     if (b.getModel().isRollover())
/*     */     {
/* 244 */       if (this.ulv == null) {
/* 245 */         this.ulv = ULHtml.createHTMLView(b, text);
/*     */       }
/* 247 */       this.ulv.paint(g, textRect);
/*     */     } else {
/* 249 */       v.paint(g, textRect);
/*     */     }
/* 251 */     textRect.x -= getTextShiftOffset();
/* 252 */     textRect.y -= getTextShiftOffset();
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
/*     */   protected void paintText(Graphics g, AbstractButton b, Rectangle textRect, String text)
/*     */   {
/* 266 */     if (!b.getModel().isEnabled()) {
/* 267 */       textRect.x += 1;
/*     */     }
/*     */     
/* 270 */     super.paintText(g, b, textRect, text);
/* 271 */     if (b.getModel().isRollover()) {
/* 272 */       paintUnderline(g, b, textRect, text);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */   private void paintUnderline(Graphics g, AbstractButton b, Rectangle rect, String text)
/*     */   {
/* 279 */     FontMetrics fm = g.getFontMetrics();
/* 280 */     int descent = fm.getDescent();
/*     */     
/*     */ 
/*     */ 
/* 284 */     g.drawLine(rect.x + getTextShiftOffset(), rect.y + rect.height - descent + 1 + getTextShiftOffset(), rect.x + rect.width + getTextShiftOffset(), rect.y + rect.height - descent + 1 + getTextShiftOffset());
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected void paintFocus(Graphics g, AbstractButton b, Rectangle viewRect, Rectangle textRect, Rectangle iconRect)
/*     */   {
/* 293 */     if ((b.getParent() instanceof JToolBar))
/*     */     {
/* 295 */       return;
/*     */     }
/*     */     
/*     */ 
/* 299 */     g.setColor(getFocusColor());
/*     */     
/*     */ 
/* 302 */     Rectangle iconTextRect = getIconTextRect(b);
/*     */     
/*     */ 
/* 305 */     BasicGraphicsUtils.drawDashedRect(g, iconTextRect.x, iconTextRect.y, iconTextRect.width, iconTextRect.height);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected void paintButtonPressed(Graphics g, AbstractButton b)
/*     */   {
/* 316 */     setTextShiftOffset();
/*     */   }
/*     */   
/*     */ 
/*     */   protected BasicButtonListener createButtonListener(AbstractButton b)
/*     */   {
/* 322 */     return new BasicHyperlinkListener(b);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean contains(JComponent c, int x, int y)
/*     */   {
/* 333 */     AbstractButton button = (AbstractButton)c;
/* 334 */     return isInside(getIconTextRect(button), x, y);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private boolean isInside(Rectangle iconTextRect, int x, int y)
/*     */   {
/* 343 */     if (iconTextRect == null) return false;
/* 344 */     return iconTextRect.contains(x, y);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected Rectangle getIconTextRect(AbstractButton b)
/*     */   {
/* 355 */     if (b.getComponentCount() > 0) {
/* 356 */       return null;
/*     */     }
/*     */     
/* 359 */     Icon icon = b.getIcon();
/* 360 */     String text = b.getText();
/*     */     
/* 362 */     Font font = b.getFont();
/* 363 */     FontMetrics fm = b.getFontMetrics(font);
/*     */     
/* 365 */     Rectangle iconR = new Rectangle();
/* 366 */     Rectangle textR = new Rectangle();
/* 367 */     Rectangle viewR = new Rectangle(b.getSize());
/*     */     
/* 369 */     SwingUtilities.layoutCompoundLabel(b, fm, text, icon, b.getVerticalAlignment(), b.getHorizontalAlignment(), b.getVerticalTextPosition(), b.getHorizontalTextPosition(), viewR, iconR, textR, text == null ? 0 : b.getIconTextGap());
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 380 */     Rectangle r = iconR.union(textR);
/*     */     
/* 382 */     Insets insets = b.getInsets();
/* 383 */     r.width += insets.left + insets.right;
/* 384 */     r.height += insets.top + insets.bottom;
/*     */     
/*     */ 
/* 387 */     r.y -= insets.top;
/* 388 */     return r;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static class BasicHyperlinkListener
/*     */     extends BasicButtonListener
/*     */   {
/*     */     public BasicHyperlinkListener(AbstractButton b)
/*     */     {
/* 402 */       super();
/*     */     }
/*     */     
/*     */ 
/*     */     public void stateChanged(ChangeEvent e)
/*     */     {
/* 408 */       AbstractButton button = (AbstractButton)e.getSource();
/* 409 */       if (button.isRolloverEnabled()) {
/* 410 */         button.setCursor(button.getModel().isRollover() ? Cursor.getPredefinedCursor(12) : null);
/*     */       }
/*     */       
/*     */ 
/* 414 */       super.stateChanged(e);
/*     */     }
/*     */   }
/*     */   
/*     */   static class ULHtml extends BasicHTML {
/*     */     private static BasicEditorKit basicHTMLFactory;
/*     */     private static ViewFactory basicHTMLViewFactory;
/*     */     private static final String styleChanges = "p { margin-top: 0; margin-bottom: 0; margin-left: 0; margin-right: 0; text-decoration: underline }body { margin-top: 0; margin-bottom: 0; margin-left: 0; margin-right: 0; text-decoration: underline }font {text-decoration: underline}";
/*     */     
/*     */     public static View createHTMLView(JComponent c, String html) {
/* 424 */       BasicEditorKit kit = getFactory();
/* 425 */       Document doc = kit.createDefaultDocument(c.getFont(), c.getForeground());
/*     */       
/* 427 */       Object base = c.getClientProperty("html.base");
/* 428 */       if ((base instanceof URL)) {
/* 429 */         ((HTMLDocument)doc).setBase((URL)base);
/*     */       }
/* 431 */       Reader r = new StringReader(html);
/*     */       try {
/* 433 */         kit.read(r, doc, 0);
/*     */       }
/*     */       catch (Throwable e) {}
/* 436 */       ViewFactory f = kit.getViewFactory();
/* 437 */       View hview = f.create(doc.getDefaultRootElement());
/* 438 */       View v = new Renderer(c, f, hview);
/* 439 */       return v;
/*     */     }
/*     */     
/*     */     static BasicEditorKit getFactory() {
/* 443 */       if (basicHTMLFactory == null) {
/* 444 */         basicHTMLViewFactory = new BasicHTMLViewFactory();
/* 445 */         basicHTMLFactory = new BasicEditorKit();
/*     */       }
/* 447 */       return basicHTMLFactory;
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
/*     */     static class BasicEditorKit
/*     */       extends HTMLEditorKit
/*     */     {
/*     */       private static StyleSheet defaultStyles;
/*     */       
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       public StyleSheet getStyleSheet()
/*     */       {
/* 478 */         if (defaultStyles == null) {
/* 479 */           defaultStyles = new StyleSheet();
/* 480 */           StringReader r = new StringReader("p { margin-top: 0; margin-bottom: 0; margin-left: 0; margin-right: 0; text-decoration: underline }body { margin-top: 0; margin-bottom: 0; margin-left: 0; margin-right: 0; text-decoration: underline }font {text-decoration: underline}");
/*     */           try {
/* 482 */             defaultStyles.loadRules(r, null);
/*     */           }
/*     */           catch (Throwable e) {}
/*     */           
/*     */ 
/* 487 */           r.close();
/* 488 */           defaultStyles.addStyleSheet(super.getStyleSheet());
/*     */         }
/* 490 */         return defaultStyles;
/*     */       }
/*     */       
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       public Document createDefaultDocument(Font defaultFont, Color foreground)
/*     */       {
/* 499 */         StyleSheet styles = getStyleSheet();
/* 500 */         StyleSheet ss = new StyleSheet();
/* 501 */         ss.addStyleSheet(styles);
/* 502 */         BasicHyperlinkUI.ULHtml.BasicDocument doc = new BasicHyperlinkUI.ULHtml.BasicDocument(ss, defaultFont, foreground);
/* 503 */         doc.setAsynchronousLoadPriority(Integer.MAX_VALUE);
/* 504 */         doc.setPreservesUnknownTags(false);
/* 505 */         return doc;
/*     */       }
/*     */       
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       public ViewFactory getViewFactory()
/*     */       {
/* 514 */         return BasicHyperlinkUI.ULHtml.basicHTMLViewFactory;
/*     */       }
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */     static class BasicHTMLViewFactory
/*     */       extends HTMLEditorKit.HTMLFactory
/*     */     {
/*     */       public View create(Element elem)
/*     */       {
/* 526 */         View view = super.create(elem);
/*     */         
/* 528 */         if ((view instanceof ImageView)) {
/* 529 */           ((ImageView)view).setLoadsSynchronously(true);
/*     */         }
/* 531 */         return view;
/*     */       }
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */     static class BasicDocument
/*     */       extends HTMLDocument
/*     */     {
/*     */       private static Class<?> clz;
/*     */       
/*     */ 
/*     */       private static Method displayPropertiesToCSS;
/*     */       
/*     */ 
/*     */ 
/*     */       static
/*     */       {
/* 549 */         String j5 = "com.sun.java.swing.SwingUtilities2";
/* 550 */         String j6 = "sun.swing.SwingUtilities2";
/*     */         try
/*     */         {
/* 553 */           clz = Class.forName(j6);
/*     */         }
/*     */         catch (ClassNotFoundException e) {
/*     */           try {
/* 557 */             clz = Class.forName(j5);
/*     */           } catch (ClassNotFoundException e1) {
/* 559 */             throw new RuntimeException("Failed to find SwingUtilities2. Check the classpath.");
/*     */           }
/*     */         }
/*     */         try {
/* 563 */           displayPropertiesToCSS = clz.getMethod("displayPropertiesToCSS", new Class[] { Font.class, Color.class });
/*     */         } catch (Exception e) {
/* 565 */           throw new RuntimeException("Failed to use SwingUtilities2. Check the permissions and class version.");
/*     */         }
/*     */       }
/*     */       
/*     */       private static String displayPropertiesToCSS(Font f, Color c) {
/*     */         try {
/* 571 */           return (String)displayPropertiesToCSS.invoke(null, new Object[] { f, c });
/*     */         } catch (Exception e) {
/* 573 */           throw new RuntimeException(e);
/*     */         }
/*     */       }
/*     */       
/*     */ 
/*     */       BasicDocument(StyleSheet s, Font defaultFont, Color foreground)
/*     */       {
/* 580 */         super();
/* 581 */         setPreservesUnknownTags(false);
/* 582 */         setFontAndColor(defaultFont, foreground);
/*     */       }
/*     */       
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       private void setFontAndColor(Font font, Color fg)
/*     */       {
/* 592 */         getStyleSheet().addRule(displayPropertiesToCSS(font, fg));
/*     */       }
/*     */     }
/*     */     
/*     */     static class Renderer extends View {
/*     */       private int width;
/*     */       private View view;
/*     */       private ViewFactory factory;
/*     */       private JComponent host;
/*     */       
/*     */       Renderer(JComponent c, ViewFactory f, View v) {
/* 603 */         super();
/* 604 */         this.host = c;
/* 605 */         this.factory = f;
/* 606 */         this.view = v;
/* 607 */         this.view.setParent(this);
/*     */         
/* 609 */         setSize(this.view.getPreferredSpan(0), this.view.getPreferredSpan(1));
/*     */       }
/*     */       
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       public AttributeSet getAttributes()
/*     */       {
/* 619 */         return null;
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
/*     */       public float getPreferredSpan(int axis)
/*     */       {
/* 633 */         if (axis == 0)
/*     */         {
/* 635 */           return this.width;
/*     */         }
/* 637 */         return this.view.getPreferredSpan(axis);
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
/*     */       public float getMinimumSpan(int axis)
/*     */       {
/* 651 */         return this.view.getMinimumSpan(axis);
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
/*     */       public float getMaximumSpan(int axis)
/*     */       {
/* 665 */         return 2.14748365E9F;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       public void preferenceChanged(View child, boolean width, boolean height)
/*     */       {
/* 688 */         this.host.revalidate();
/* 689 */         this.host.repaint();
/*     */       }
/*     */       
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       public float getAlignment(int axis)
/*     */       {
/* 701 */         return this.view.getAlignment(axis);
/*     */       }
/*     */       
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       public void paint(Graphics g, Shape allocation)
/*     */       {
/* 712 */         Rectangle alloc = allocation.getBounds();
/* 713 */         this.view.setSize(alloc.width, alloc.height);
/* 714 */         this.view.paint(g, allocation);
/*     */       }
/*     */       
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       public void setParent(View parent)
/*     */       {
/* 724 */         throw new Error("Can't set parent on root view");
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
/*     */       public int getViewCount()
/*     */       {
/* 737 */         return 1;
/*     */       }
/*     */       
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       public View getView(int n)
/*     */       {
/* 748 */         return this.view;
/*     */       }
/*     */       
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       public Shape modelToView(int pos, Shape a, Position.Bias b)
/*     */         throws BadLocationException
/*     */       {
/* 761 */         return this.view.modelToView(pos, a, b);
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       public Shape modelToView(int p0, Position.Bias b0, int p1, Position.Bias b1, Shape a)
/*     */         throws BadLocationException
/*     */       {
/* 786 */         return this.view.modelToView(p0, b0, p1, b1, a);
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
/*     */       public int viewToModel(float x, float y, Shape a, Position.Bias[] bias)
/*     */       {
/* 801 */         return this.view.viewToModel(x, y, a, bias);
/*     */       }
/*     */       
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       public Document getDocument()
/*     */       {
/* 811 */         return this.view.getDocument();
/*     */       }
/*     */       
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       public int getStartOffset()
/*     */       {
/* 821 */         return this.view.getStartOffset();
/*     */       }
/*     */       
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       public int getEndOffset()
/*     */       {
/* 831 */         return this.view.getEndOffset();
/*     */       }
/*     */       
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       public Element getElement()
/*     */       {
/* 841 */         return this.view.getElement();
/*     */       }
/*     */       
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       public void setSize(float width, float height)
/*     */       {
/* 852 */         this.width = ((int)width);
/* 853 */         this.view.setSize(width, height);
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
/*     */       public Container getContainer()
/*     */       {
/* 866 */         return this.host;
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
/*     */       public ViewFactory getViewFactory()
/*     */       {
/* 881 */         return this.factory;
/*     */       }
/*     */     }
/*     */   }
/*     */ }


/* Location:              E:\java\java学习\hutubill\lib\all.jar!\org\jdesktop\swingx\plaf\basic\BasicHyperlinkUI.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */
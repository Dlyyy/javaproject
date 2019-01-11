/*      */ package org.jdesktop.swingx;
/*      */ 
/*      */ import java.applet.Applet;
/*      */ import java.awt.Color;
/*      */ import java.awt.Component;
/*      */ import java.awt.Container;
/*      */ import java.awt.Dimension;
/*      */ import java.awt.Font;
/*      */ import java.awt.FontMetrics;
/*      */ import java.awt.Graphics;
/*      */ import java.awt.Graphics2D;
/*      */ import java.awt.Insets;
/*      */ import java.awt.Rectangle;
/*      */ import java.awt.Shape;
/*      */ import java.awt.Window;
/*      */ import java.awt.event.HierarchyBoundsAdapter;
/*      */ import java.awt.event.HierarchyEvent;
/*      */ import java.awt.font.TextAttribute;
/*      */ import java.awt.geom.Point2D;
/*      */ import java.awt.geom.Point2D.Double;
/*      */ import java.beans.PropertyChangeEvent;
/*      */ import java.beans.PropertyChangeListener;
/*      */ import java.io.Reader;
/*      */ import java.io.StringReader;
/*      */ import java.util.Map;
/*      */ import javax.swing.Icon;
/*      */ import javax.swing.JLabel;
/*      */ import javax.swing.JPanel;
/*      */ import javax.swing.JViewport;
/*      */ import javax.swing.border.Border;
/*      */ import javax.swing.event.DocumentEvent;
/*      */ import javax.swing.event.DocumentEvent.ElementChange;
/*      */ import javax.swing.text.AttributeSet;
/*      */ import javax.swing.text.BoxView;
/*      */ import javax.swing.text.ComponentView;
/*      */ import javax.swing.text.DefaultStyledDocument;
/*      */ import javax.swing.text.Document;
/*      */ import javax.swing.text.Element;
/*      */ import javax.swing.text.IconView;
/*      */ import javax.swing.text.LabelView;
/*      */ import javax.swing.text.MutableAttributeSet;
/*      */ import javax.swing.text.ParagraphView;
/*      */ import javax.swing.text.SimpleAttributeSet;
/*      */ import javax.swing.text.Style;
/*      */ import javax.swing.text.StyleConstants;
/*      */ import javax.swing.text.StyledEditorKit;
/*      */ import javax.swing.text.View;
/*      */ import javax.swing.text.ViewFactory;
/*      */ import javax.swing.text.WrappedPlainView;
/*      */ import org.jdesktop.swingx.painter.AbstractPainter;
/*      */ import org.jdesktop.swingx.painter.Painter;
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
/*      */ public class JXLabel
/*      */   extends JLabel
/*      */   implements BackgroundPaintable
/*      */ {
/*      */   public static final double NORMAL = 0.0D;
/*      */   public static final double INVERTED = 3.141592653589793D;
/*      */   public static final double VERTICAL_LEFT = 4.71238898038469D;
/*      */   public static final double VERTICAL_RIGHT = 1.5707963267948966D;
/*      */   
/*      */   public static enum TextAlignment
/*      */     implements JXLabel.IValue
/*      */   {
/*  120 */     LEFT(0),  CENTER(1),  RIGHT(2),  JUSTIFY(3);
/*      */     
/*      */     private int value;
/*      */     
/*  124 */     private TextAlignment(int val) { this.value = val; }
/*      */     
/*      */     public int getValue()
/*      */     {
/*  128 */       return this.value;
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
/*      */ 
/*      */ 
/*      */ 
/*  146 */   private double textRotation = 0.0D;
/*      */   
/*  148 */   private boolean painting = false;
/*      */   
/*      */ 
/*      */   private Painter foregroundPainter;
/*      */   
/*      */   private Painter backgroundPainter;
/*      */   
/*      */   private boolean multiLine;
/*      */   
/*      */   private int pWidth;
/*      */   
/*      */   private int pHeight;
/*      */   
/*  161 */   private boolean dontIgnoreRepaint = false;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   private int occupiedWidth;
/*      */   
/*      */ 
/*      */ 
/*      */   private static final String oldRendererKey = "washtml";
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public JXLabel()
/*      */   {
/*  177 */     initPainterSupport();
/*  178 */     initLineWrapSupport();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public JXLabel(Icon image)
/*      */   {
/*  186 */     super(image);
/*  187 */     initPainterSupport();
/*  188 */     initLineWrapSupport();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public JXLabel(Icon image, int horizontalAlignment)
/*      */   {
/*  197 */     super(image, horizontalAlignment);
/*  198 */     initPainterSupport();
/*  199 */     initLineWrapSupport();
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
/*      */   public JXLabel(String text)
/*      */   {
/*  213 */     super(text);
/*  214 */     initPainterSupport();
/*  215 */     initLineWrapSupport();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public JXLabel(String text, Icon image, int horizontalAlignment)
/*      */   {
/*  225 */     super(text, image, horizontalAlignment);
/*  226 */     initPainterSupport();
/*  227 */     initLineWrapSupport();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public JXLabel(String text, int horizontalAlignment)
/*      */   {
/*  236 */     super(text, horizontalAlignment);
/*  237 */     initPainterSupport();
/*  238 */     initLineWrapSupport();
/*      */   }
/*      */   
/*      */   private void initPainterSupport() {
/*  242 */     this.foregroundPainter = new AbstractPainter()
/*      */     {
/*      */       protected void doPaint(Graphics2D g, JXLabel label, int width, int height) {
/*  245 */         Insets i = JXLabel.this.getInsets();
/*  246 */         g = (Graphics2D)g.create(-i.left, -i.top, width, height);
/*      */         try
/*      */         {
/*  249 */           label.paint(g);
/*      */         } finally {
/*  251 */           g.dispose();
/*      */         }
/*      */       }
/*      */       
/*      */ 
/*      */ 
/*      */       protected boolean shouldUseCache()
/*      */       {
/*  259 */         return false;
/*      */       }
/*      */       
/*      */       public boolean equals(Object obj)
/*      */       {
/*  264 */         return (obj != null) && (getClass().equals(obj.getClass()));
/*      */       }
/*      */       
/*  267 */     };
/*  268 */     ((AbstractPainter)this.foregroundPainter).setAntialiasing(false);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   private void initLineWrapSupport()
/*      */   {
/*  275 */     addPropertyChangeListener(new MultiLineSupport());
/*      */     
/*      */ 
/*      */ 
/*  279 */     addHierarchyBoundsListener(new HierarchyBoundsAdapter()
/*      */     {
/*      */ 
/*      */       public void ancestorResized(HierarchyEvent e)
/*      */       {
/*  284 */         if ((e.getChanged() instanceof JViewport)) {
/*  285 */           Rectangle viewportBounds = e.getChanged().getBounds();
/*  286 */           if (viewportBounds.getWidth() < JXLabel.this.getWidth()) {
/*  287 */             View view = JXLabel.this.getWrappingView();
/*  288 */             if (view != null) {
/*  289 */               view.setSize(viewportBounds.width, viewportBounds.height);
/*      */             }
/*      */           }
/*      */         }
/*      */       }
/*      */     });
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public final Painter getForegroundPainter()
/*      */   {
/*  303 */     return this.foregroundPainter;
/*      */   }
/*      */   
/*      */   public void reshape(int x, int y, int w, int h)
/*      */   {
/*  308 */     int oldH = getHeight();
/*  309 */     super.reshape(x, y, w, h);
/*  310 */     if (!isLineWrap()) {
/*  311 */       return;
/*      */     }
/*  313 */     if (oldH == 0) {
/*  314 */       return;
/*      */     }
/*  316 */     if (w > getVisibleRect().width) {
/*  317 */       w = getVisibleRect().width;
/*      */     }
/*  319 */     View view = (View)getClientProperty("html");
/*  320 */     if ((view != null) && ((view instanceof Renderer))) {
/*  321 */       view.setSize(w - this.occupiedWidth, h);
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void setBackground(Color bg)
/*      */   {
/*  331 */     super.setBackground(bg);
/*      */     
/*  333 */     SwingXUtilities.installBackground(this, bg);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void setForegroundPainter(Painter painter)
/*      */   {
/*  343 */     Painter old = getForegroundPainter();
/*  344 */     if (painter == null)
/*      */     {
/*  346 */       initPainterSupport();
/*      */     } else {
/*  348 */       this.foregroundPainter = painter;
/*      */     }
/*  350 */     firePropertyChange("foregroundPainter", old, getForegroundPainter());
/*  351 */     repaint();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void setBackgroundPainter(Painter p)
/*      */   {
/*  363 */     Painter old = getBackgroundPainter();
/*  364 */     this.backgroundPainter = p;
/*  365 */     firePropertyChange("backgroundPainter", old, getBackgroundPainter());
/*  366 */     repaint();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public final Painter getBackgroundPainter()
/*      */   {
/*  377 */     return this.backgroundPainter;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public double getTextRotation()
/*      */   {
/*  387 */     return this.textRotation;
/*      */   }
/*      */   
/*      */   public Dimension getPreferredSize()
/*      */   {
/*  392 */     Dimension size = super.getPreferredSize();
/*      */     
/*  394 */     if (isPreferredSizeSet())
/*      */     {
/*  396 */       return size; }
/*  397 */     if (this.textRotation != 0.0D)
/*      */     {
/*  399 */       double theta = getTextRotation();
/*  400 */       size.setSize(rotateWidth(size, theta), rotateHeight(size, theta));
/*      */     }
/*      */     else
/*      */     {
/*  404 */       View view = getWrappingView();
/*  405 */       if (view == null) {
/*  406 */         if ((isLineWrap()) && (!MultiLineSupport.isHTML(getText())))
/*      */         {
/*  408 */           getMultiLineSupport();putClientProperty("html", MultiLineSupport.createView(this));
/*      */           
/*  410 */           view = (View)getClientProperty("html");
/*      */         } else {
/*  412 */           return size;
/*      */         }
/*      */       }
/*  415 */       Insets insets = getInsets();
/*  416 */       int dx = insets.left + insets.right;
/*  417 */       int dy = insets.top + insets.bottom;
/*      */       
/*      */ 
/*  420 */       Rectangle textR = new Rectangle();
/*  421 */       Rectangle viewR = new Rectangle();
/*  422 */       textR.x = (textR.y = textR.width = textR.height = 0);
/*  423 */       viewR.x = dx;
/*  424 */       viewR.y = dy;
/*  425 */       viewR.width = (viewR.height = '翿');
/*      */       
/*      */ 
/*  428 */       Rectangle iconR = calculateIconRect();
/*      */       
/*  430 */       boolean textIsEmpty = (getText() == null) || (getText().equals(""));
/*  431 */       int lsb = 0;
/*      */       
/*      */       int gap;
/*      */       
/*      */       int gap;
/*  436 */       if (textIsEmpty) {
/*  437 */         textR.width = (textR.height = 0);
/*  438 */         gap = 0;
/*      */       }
/*      */       else
/*      */       {
/*  442 */         gap = iconR.width == 0 ? 0 : getIconTextGap();
/*      */         
/*  444 */         this.occupiedWidth = (dx + iconR.width + gap);
/*  445 */         Object parent = getParent();
/*  446 */         if ((parent != null) && ((parent instanceof JPanel))) {
/*  447 */           JPanel panel = (JPanel)parent;
/*  448 */           Border b = panel.getBorder();
/*  449 */           if (b != null) {
/*  450 */             Insets in = b.getBorderInsets(panel);
/*  451 */             this.occupiedWidth += in.left + in.right; } }
/*      */         int availTextWidth;
/*      */         int availTextWidth;
/*  454 */         if (getHorizontalTextPosition() == 0) {
/*  455 */           availTextWidth = viewR.width;
/*      */         }
/*      */         else {
/*  458 */           availTextWidth = viewR.width - (iconR.width + gap);
/*      */         }
/*  460 */         float xPrefSpan = view.getPreferredSpan(0);
/*      */         
/*  462 */         textR.width = Math.min(availTextWidth, (int)xPrefSpan);
/*  463 */         if (this.maxLineSpan > 0) {
/*  464 */           textR.width = Math.min(textR.width, this.maxLineSpan);
/*  465 */           if (xPrefSpan > this.maxLineSpan) {
/*  466 */             view.setSize(this.maxLineSpan, textR.height);
/*      */           }
/*      */         }
/*  469 */         textR.height = ((int)view.getPreferredSpan(1));
/*  470 */         if (textR.height == 0) {
/*  471 */           textR.height = getFont().getSize();
/*      */         }
/*      */       }
/*      */       
/*      */ 
/*      */ 
/*  477 */       if (getVerticalTextPosition() == 1) {
/*  478 */         if (getHorizontalTextPosition() != 0) {
/*  479 */           textR.y = 0;
/*      */         }
/*      */         else {
/*  482 */           textR.y = (-(textR.height + gap));
/*      */         }
/*      */       }
/*  485 */       else if (getVerticalTextPosition() == 0) {
/*  486 */         textR.y = (iconR.height / 2 - textR.height / 2);
/*      */ 
/*      */       }
/*  489 */       else if (getVerticalTextPosition() != 0) {
/*  490 */         textR.y = (iconR.height - textR.height);
/*      */       }
/*      */       else {
/*  493 */         textR.y = (iconR.height + gap);
/*      */       }
/*      */       
/*      */ 
/*  497 */       if (getHorizontalTextPosition() == 2) {
/*  498 */         textR.x = (-(textR.width + gap));
/*      */       }
/*  500 */       else if (getHorizontalTextPosition() == 0) {
/*  501 */         textR.x = (iconR.width / 2 - textR.width / 2);
/*      */       }
/*      */       else {
/*  504 */         textR.x = (iconR.width + gap);
/*      */       }
/*      */       
/*      */ 
/*  508 */       int labelR_x = Math.min(iconR.x, textR.x);
/*  509 */       int labelR_width = Math.max(iconR.x + iconR.width, textR.x + textR.width) - labelR_x;
/*      */       
/*  511 */       int labelR_y = Math.min(iconR.y, textR.y);
/*  512 */       int labelR_height = Math.max(iconR.y + iconR.height, textR.y + textR.height) - labelR_y;
/*      */       
/*      */       int day;
/*      */       
/*      */       int day;
/*  517 */       if (getVerticalAlignment() == 1) {
/*  518 */         day = viewR.y - labelR_y;
/*      */       } else { int day;
/*  520 */         if (getVerticalAlignment() == 0) {
/*  521 */           day = viewR.y + viewR.height / 2 - (labelR_y + labelR_height / 2);
/*      */         }
/*      */         else
/*  524 */           day = viewR.y + viewR.height - (labelR_y + labelR_height); }
/*      */       int dax;
/*      */       int dax;
/*  527 */       if (getHorizontalAlignment() == 2) {
/*  528 */         dax = viewR.x - labelR_x;
/*      */       } else { int dax;
/*  530 */         if (getHorizontalAlignment() == 4) {
/*  531 */           dax = viewR.x + viewR.width - (labelR_x + labelR_width);
/*      */         }
/*      */         else {
/*  534 */           dax = viewR.x + viewR.width / 2 - (labelR_x + labelR_width / 2);
/*      */         }
/*      */       }
/*      */       
/*  538 */       textR.x += dax;
/*  539 */       textR.y += day;
/*      */       
/*  541 */       iconR.x += dax;
/*  542 */       iconR.y += day;
/*      */       
/*  544 */       if (lsb < 0)
/*      */       {
/*      */ 
/*  547 */         textR.x -= lsb;
/*      */       }
/*      */       
/*      */ 
/*  551 */       int x1 = Math.min(iconR.x, textR.x);
/*  552 */       int x2 = Math.max(iconR.x + iconR.width, textR.x + textR.width);
/*  553 */       int y1 = Math.min(iconR.y, textR.y);
/*  554 */       int y2 = Math.max(iconR.y + iconR.height, textR.y + textR.height);
/*  555 */       Dimension rv = new Dimension(x2 - x1, y2 - y1);
/*      */       
/*  557 */       rv.width += dx;
/*  558 */       rv.height += dy;
/*      */       
/*  560 */       return rv;
/*      */     }
/*      */     
/*  563 */     return size;
/*      */   }
/*      */   
/*      */   private View getWrappingView() {
/*  567 */     if (super.getTopLevelAncestor() == null) {
/*  568 */       return null;
/*      */     }
/*  570 */     View view = (View)getClientProperty("html");
/*  571 */     if (!(view instanceof Renderer)) {
/*  572 */       return null;
/*      */     }
/*  574 */     return view;
/*      */   }
/*      */   
/*      */   private Container getViewport() {
/*  578 */     for (Container p = this; p != null; p = p.getParent()) {
/*  579 */       if (((p instanceof Window)) || ((p instanceof Applet)) || ((p instanceof JViewport))) {
/*  580 */         return p;
/*      */       }
/*      */     }
/*  583 */     return null;
/*      */   }
/*      */   
/*      */   private Rectangle calculateIconRect() {
/*  587 */     Rectangle iconR = new Rectangle();
/*  588 */     Icon icon = isEnabled() ? getIcon() : getDisabledIcon();
/*  589 */     iconR.x = (iconR.y = iconR.width = iconR.height = 0);
/*  590 */     if (icon != null) {
/*  591 */       iconR.width = icon.getIconWidth();
/*  592 */       iconR.height = icon.getIconHeight();
/*      */     }
/*      */     else {
/*  595 */       iconR.width = (iconR.height = 0);
/*      */     }
/*  597 */     return iconR;
/*      */   }
/*      */   
/*      */   public int getMaxLineSpan() {
/*  601 */     return this.maxLineSpan;
/*      */   }
/*      */   
/*      */   public void setMaxLineSpan(int maxLineSpan) {
/*  605 */     int old = getMaxLineSpan();
/*  606 */     this.maxLineSpan = maxLineSpan;
/*  607 */     firePropertyChange("maxLineSpan", old, getMaxLineSpan());
/*      */   }
/*      */   
/*      */   private static int rotateWidth(Dimension size, double theta) {
/*  611 */     return (int)Math.round(size.width * Math.abs(Math.cos(theta)) + size.height * Math.abs(Math.sin(theta)));
/*      */   }
/*      */   
/*      */   private static int rotateHeight(Dimension size, double theta)
/*      */   {
/*  616 */     return (int)Math.round(size.width * Math.abs(Math.sin(theta)) + size.height * Math.abs(Math.cos(theta)));
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
/*      */   public void setTextRotation(double textOrientation)
/*      */   {
/*  630 */     double old = getTextRotation();
/*  631 */     this.textRotation = textOrientation;
/*  632 */     if (old != getTextRotation()) {
/*  633 */       firePropertyChange("textRotation", old, getTextRotation());
/*      */     }
/*  635 */     repaint();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void setLineWrap(boolean b)
/*      */   {
/*  645 */     boolean old = isLineWrap();
/*  646 */     this.multiLine = b;
/*  647 */     if (isLineWrap() != old) {
/*  648 */       firePropertyChange("lineWrap", old, isLineWrap());
/*  649 */       if (getForegroundPainter() != null)
/*      */       {
/*  651 */         ((AbstractPainter)getForegroundPainter()).setCacheable(!b);
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
/*      */   public boolean isLineWrap()
/*      */   {
/*  664 */     return this.multiLine;
/*      */   }
/*      */   
/*  667 */   private boolean paintBorderInsets = true;
/*      */   
/*  669 */   private int maxLineSpan = -1;
/*      */   
/*      */   public boolean painted;
/*      */   
/*  673 */   private TextAlignment textAlignment = TextAlignment.LEFT;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public TextAlignment getTextAlignment()
/*      */   {
/*  680 */     return this.textAlignment;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void setTextAlignment(TextAlignment alignment)
/*      */   {
/*  689 */     TextAlignment old = getTextAlignment();
/*  690 */     this.textAlignment = alignment;
/*  691 */     firePropertyChange("textAlignment", old, getTextAlignment());
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public boolean isPaintBorderInsets()
/*      */   {
/*  702 */     return this.paintBorderInsets;
/*      */   }
/*      */   
/*      */   public boolean isOpaque()
/*      */   {
/*  707 */     return this.painting ? false : super.isOpaque();
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
/*      */   public void setPaintBorderInsets(boolean paintBorderInsets)
/*      */   {
/*  721 */     boolean old = isPaintBorderInsets();
/*  722 */     this.paintBorderInsets = paintBorderInsets;
/*  723 */     firePropertyChange("paintBorderInsets", old, isPaintBorderInsets());
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
/*      */   protected void paintComponent(Graphics g)
/*      */   {
/*  738 */     this.painted = true;
/*  739 */     if ((this.painting) || ((this.backgroundPainter == null) && (this.foregroundPainter == null))) {
/*  740 */       super.paintComponent(g);
/*      */     } else {
/*  742 */       this.pWidth = getWidth();
/*  743 */       this.pHeight = getHeight();
/*  744 */       if (this.backgroundPainter != null) {
/*  745 */         Graphics2D tmp = (Graphics2D)g.create();
/*      */         try
/*      */         {
/*  748 */           SwingXUtilities.paintBackground(this, tmp);
/*      */         } finally {
/*  750 */           tmp.dispose();
/*      */         }
/*      */       }
/*  753 */       if (this.foregroundPainter != null) {
/*  754 */         Insets i = getInsets();
/*  755 */         this.pWidth = (getWidth() - i.left - i.right);
/*  756 */         this.pHeight = (getHeight() - i.top - i.bottom);
/*      */         
/*  758 */         Point2D tPoint = calculateT();
/*  759 */         double wx = Math.sin(this.textRotation) * tPoint.getY() + Math.cos(this.textRotation) * tPoint.getX();
/*  760 */         double wy = Math.sin(this.textRotation) * tPoint.getX() + Math.cos(this.textRotation) * tPoint.getY();
/*  761 */         double x = (getWidth() - wx) / 2.0D + Math.sin(this.textRotation) * tPoint.getY();
/*  762 */         double y = (getHeight() - wy) / 2.0D;
/*  763 */         Graphics2D tmp = (Graphics2D)g.create();
/*  764 */         if (i != null) {
/*  765 */           tmp.translate(i.left + x, i.top + y);
/*      */         } else {
/*  767 */           tmp.translate(x, y);
/*      */         }
/*  769 */         tmp.rotate(this.textRotation);
/*      */         
/*  771 */         this.painting = true;
/*      */         
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  778 */         this.foregroundPainter.paint(tmp, this, this.pWidth, this.pHeight);
/*  779 */         tmp.dispose();
/*  780 */         this.painting = false;
/*  781 */         this.pWidth = 0;
/*  782 */         this.pHeight = 0;
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */   private Point2D calculateT() {
/*  788 */     double tx = getWidth();
/*  789 */     double ty = getHeight();
/*      */     
/*      */ 
/*  792 */     if (((this.textRotation > 4.697D) && (this.textRotation < 4.727D)) || ((this.textRotation > 1.555D) && (this.textRotation < 1.585D)))
/*      */     {
/*  794 */       int tmp = this.pHeight;
/*  795 */       this.pHeight = this.pWidth;
/*  796 */       this.pWidth = tmp;
/*  797 */       tx = this.pWidth;
/*  798 */       ty = this.pHeight;
/*  799 */     } else if (((this.textRotation > -0.015D) && (this.textRotation < 0.015D)) || ((this.textRotation > 3.14D) && (this.textRotation < 3.143D)))
/*      */     {
/*      */ 
/*  802 */       this.pHeight = getHeight();
/*  803 */       this.pWidth = getWidth();
/*      */ 
/*      */ 
/*      */     }
/*      */     else
/*      */     {
/*      */ 
/*  810 */       this.dontIgnoreRepaint = false;
/*  811 */       double square = Math.min(getHeight(), getWidth()) * Math.cos(0.7853981633974483D);
/*      */       
/*  813 */       View v = (View)getClientProperty("html");
/*  814 */       if (v == null)
/*      */       {
/*      */ 
/*  817 */         ty = getFontMetrics(getFont()).getHeight();
/*  818 */         double cw = (getWidth() - Math.abs(ty * Math.sin(this.textRotation))) / Math.abs(Math.cos(this.textRotation));
/*      */         
/*  820 */         double ch = (getHeight() - Math.abs(ty * Math.cos(this.textRotation))) / Math.abs(Math.sin(this.textRotation));
/*      */         
/*      */ 
/*  823 */         tx = ch > 0.0D ? Math.min(cw, ch) : cw < 0.0D ? ch : cw;
/*      */       } else {
/*  825 */         float w = v.getPreferredSpan(0);
/*  826 */         float h = v.getPreferredSpan(1);
/*  827 */         double c = w;
/*  828 */         double alpha = this.textRotation;
/*  829 */         boolean ready = false;
/*  830 */         while (!ready)
/*      */         {
/*  832 */           while (h == v.getPreferredSpan(1)) {
/*  833 */             w -= 10.0F;
/*  834 */             v.setSize(w, h);
/*      */           }
/*  836 */           if ((w < square) || (h > square))
/*      */           {
/*      */ 
/*      */ 
/*  840 */             w = h = (float)square;
/*      */             
/*  842 */             v.setSize(w, 100000.0F);
/*  843 */             break;
/*      */           }
/*      */           
/*  846 */           h = v.getPreferredSpan(1);
/*  847 */           double cw = (getWidth() - Math.abs(h * Math.sin(alpha))) / Math.abs(Math.cos(alpha));
/*  848 */           double ch = (getHeight() - Math.abs(h * Math.cos(alpha))) / Math.abs(Math.sin(alpha));
/*      */           
/*  850 */           c = ch > 0.0D ? Math.min(cw, ch) : cw < 0.0D ? ch : cw;
/*      */           
/*  852 */           c -= 1.0D;
/*  853 */           if (c > w) {
/*  854 */             v.setSize((float)c, 10.0F * h);
/*  855 */             ready = true;
/*      */           } else {
/*  857 */             v.setSize((float)c, 10.0F * h);
/*  858 */             if (v.getPreferredSpan(1) > h)
/*      */             {
/*  860 */               v.setSize(w, 10.0F * h);
/*      */             } else {
/*  862 */               w = (float)c;
/*  863 */               ready = true;
/*      */             }
/*      */           }
/*      */         }
/*      */         
/*  868 */         tx = Math.floor(w);
/*  869 */         ty = h;
/*      */       }
/*  871 */       this.pWidth = ((int)tx);
/*  872 */       this.pHeight = ((int)ty);
/*  873 */       this.dontIgnoreRepaint = true;
/*      */     }
/*  875 */     return new Point2D.Double(tx, ty);
/*      */   }
/*      */   
/*      */   public void repaint()
/*      */   {
/*  880 */     if (!this.dontIgnoreRepaint) {
/*  881 */       return;
/*      */     }
/*  883 */     super.repaint();
/*      */   }
/*      */   
/*      */   public void repaint(int x, int y, int width, int height)
/*      */   {
/*  888 */     if (!this.dontIgnoreRepaint) {
/*  889 */       return;
/*      */     }
/*  891 */     super.repaint(x, y, width, height);
/*      */   }
/*      */   
/*      */   public void repaint(long tm)
/*      */   {
/*  896 */     if (!this.dontIgnoreRepaint) {
/*  897 */       return;
/*      */     }
/*  899 */     super.repaint(tm);
/*      */   }
/*      */   
/*      */   public void repaint(long tm, int x, int y, int width, int height)
/*      */   {
/*  904 */     if (!this.dontIgnoreRepaint) {
/*  905 */       return;
/*      */     }
/*  907 */     super.repaint(tm, x, y, width, height);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public int getHeight()
/*      */   {
/*  914 */     int retValue = super.getHeight();
/*  915 */     if (this.painting) {
/*  916 */       retValue = this.pHeight;
/*      */     }
/*  918 */     return retValue;
/*      */   }
/*      */   
/*      */   public int getWidth()
/*      */   {
/*  923 */     int retValue = super.getWidth();
/*  924 */     if (this.painting) {
/*  925 */       retValue = this.pWidth;
/*      */     }
/*  927 */     return retValue;
/*      */   }
/*      */   
/*      */   protected MultiLineSupport getMultiLineSupport() {
/*  931 */     return new MultiLineSupport();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   protected static abstract interface IValue
/*      */   {
/*      */     public abstract int getValue();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public static class MultiLineSupport
/*      */     implements PropertyChangeListener
/*      */   {
/*      */     private static final String HTML = "<html>";
/*      */     
/*      */ 
/*      */     private static ViewFactory basicViewFactory;
/*      */     
/*      */ 
/*      */     private static BasicEditorKit basicFactory;
/*      */     
/*      */ 
/*      */     public void propertyChange(PropertyChangeEvent evt)
/*      */     {
/*  957 */       String name = evt.getPropertyName();
/*  958 */       JXLabel src = (JXLabel)evt.getSource();
/*  959 */       if ("ancestor".equals(name)) {
/*  960 */         src.dontIgnoreRepaint = true;
/*      */       }
/*  962 */       if (src.isLineWrap()) {
/*  963 */         if (("font".equals(name)) || ("foreground".equals(name)) || ("maxLineSpan".equals(name)) || ("textAlignment".equals(name)) || ("icon".equals(name)) || ("iconTextGap".equals(name))) {
/*  964 */           if ((evt.getOldValue() != null) && (!isHTML(src.getText()))) {
/*  965 */             updateRenderer(src);
/*      */           }
/*  967 */         } else if ("text".equals(name)) {
/*  968 */           if ((isHTML((String)evt.getOldValue())) && (evt.getNewValue() != null) && (!isHTML((String)evt.getNewValue())))
/*      */           {
/*      */ 
/*  971 */             if ((src.getClientProperty("washtml") == null) && (src.getClientProperty("html") != null))
/*      */             {
/*  973 */               src.putClientProperty("washtml", src.getClientProperty("html"));
/*      */             }
/*  975 */             src.putClientProperty("html", createView(src));
/*  976 */           } else if ((!isHTML((String)evt.getOldValue())) && (evt.getNewValue() != null) && (!isHTML((String)evt.getNewValue())))
/*      */           {
/*      */ 
/*  979 */             updateRenderer(src);
/*      */           }
/*      */           else {
/*  982 */             restoreHtmlRenderer(src);
/*      */           }
/*  984 */         } else if (("lineWrap".equals(name)) && (!isHTML(src.getText()))) {
/*  985 */           src.putClientProperty("html", createView(src));
/*      */         }
/*  987 */       } else if (("lineWrap".equals(name)) && (!((Boolean)evt.getNewValue()).booleanValue())) {
/*  988 */         restoreHtmlRenderer(src);
/*      */       }
/*      */     }
/*      */     
/*      */     private static void restoreHtmlRenderer(JXLabel src) {
/*  993 */       Object current = src.getClientProperty("html");
/*  994 */       if ((current == null) || ((current instanceof JXLabel.Renderer))) {
/*  995 */         src.putClientProperty("html", src.getClientProperty("washtml"));
/*      */       }
/*      */     }
/*      */     
/*      */     private static boolean isHTML(String s) {
/* 1000 */       return (s != null) && (s.toLowerCase().startsWith("<html>"));
/*      */     }
/*      */     
/*      */     public static View createView(JXLabel c) {
/* 1004 */       BasicEditorKit kit = getFactory();
/* 1005 */       float rightIndent = 0.0F;
/* 1006 */       if ((c.getIcon() != null) && (c.getHorizontalTextPosition() != 0)) {
/* 1007 */         rightIndent = c.getIcon().getIconWidth() + c.getIconTextGap();
/*      */       }
/* 1009 */       Document doc = kit.createDefaultDocument(c.getFont(), c.getForeground(), c.getTextAlignment(), rightIndent);
/* 1010 */       Reader r = new StringReader(c.getText() == null ? "" : c.getText());
/*      */       try {
/* 1012 */         kit.read(r, doc, 0);
/*      */       }
/*      */       catch (Throwable e) {}
/* 1015 */       ViewFactory f = kit.getViewFactory();
/* 1016 */       View hview = f.create(doc.getDefaultRootElement());
/* 1017 */       View v = new JXLabel.Renderer(c, f, hview, true);
/* 1018 */       return v;
/*      */     }
/*      */     
/*      */     public static void updateRenderer(JXLabel c) {
/* 1022 */       View value = null;
/* 1023 */       View oldValue = (View)c.getClientProperty("html");
/* 1024 */       if ((oldValue == null) || ((oldValue instanceof JXLabel.Renderer))) {
/* 1025 */         value = createView(c);
/*      */       }
/* 1027 */       if ((value != oldValue) && (oldValue != null)) {
/* 1028 */         for (int i = 0; i < oldValue.getViewCount(); i++) {
/* 1029 */           oldValue.getView(i).setParent(null);
/*      */         }
/*      */       }
/* 1032 */       c.putClientProperty("html", value);
/*      */     }
/*      */     
/*      */     private static BasicEditorKit getFactory() {
/* 1036 */       if (basicFactory == null) {
/* 1037 */         basicViewFactory = new JXLabel.BasicViewFactory(null);
/* 1038 */         basicFactory = new BasicEditorKit(null);
/*      */       }
/* 1040 */       return basicFactory;
/*      */     }
/*      */     
/*      */     private static class BasicEditorKit extends StyledEditorKit {
/*      */       public Document createDefaultDocument(Font defaultFont, Color foreground, JXLabel.TextAlignment textAlignment, float rightIndent) {
/* 1045 */         JXLabel.BasicDocument doc = new JXLabel.BasicDocument(defaultFont, foreground, textAlignment, rightIndent);
/* 1046 */         doc.setAsynchronousLoadPriority(Integer.MAX_VALUE);
/* 1047 */         return doc;
/*      */       }
/*      */       
/*      */       public ViewFactory getViewFactory()
/*      */       {
/* 1052 */         return JXLabel.MultiLineSupport.basicViewFactory;
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */   private static class BasicViewFactory implements ViewFactory
/*      */   {
/*      */     public View create(Element elem) {
/* 1060 */       String kind = elem.getName();
/* 1061 */       View view = null;
/* 1062 */       if (kind == null)
/*      */       {
/* 1064 */         view = new LabelView(elem);
/* 1065 */       } else if (kind.equals("content")) {
/* 1066 */         view = new LabelView(elem);
/* 1067 */       } else if (kind.equals("paragraph")) {
/* 1068 */         view = new ParagraphView(elem);
/* 1069 */       } else if (kind.equals("section")) {
/* 1070 */         view = new BoxView(elem, 1);
/* 1071 */       } else if (kind.equals("component")) {
/* 1072 */         view = new ComponentView(elem);
/* 1073 */       } else if (kind.equals("icon")) {
/* 1074 */         view = new IconView(elem);
/*      */       }
/* 1076 */       return view;
/*      */     }
/*      */   }
/*      */   
/*      */   static class BasicDocument extends DefaultStyledDocument {
/*      */     BasicDocument(Font defaultFont, Color foreground, JXLabel.TextAlignment textAlignment, float rightIndent) {
/* 1082 */       setFontAndColor(defaultFont, foreground);
/*      */       
/* 1084 */       MutableAttributeSet attr = new SimpleAttributeSet();
/* 1085 */       StyleConstants.setAlignment(attr, textAlignment.getValue());
/* 1086 */       getStyle("default").addAttributes(attr);
/*      */       
/* 1088 */       attr = new SimpleAttributeSet();
/* 1089 */       StyleConstants.setRightIndent(attr, rightIndent);
/* 1090 */       getStyle("default").addAttributes(attr);
/*      */     }
/*      */     
/*      */     private void setFontAndColor(Font font, Color fg) {
/* 1094 */       if (fg != null)
/*      */       {
/* 1096 */         MutableAttributeSet attr = new SimpleAttributeSet();
/* 1097 */         StyleConstants.setForeground(attr, fg);
/* 1098 */         getStyle("default").addAttributes(attr);
/*      */       }
/*      */       
/* 1101 */       if (font != null) {
/* 1102 */         MutableAttributeSet attr = new SimpleAttributeSet();
/* 1103 */         StyleConstants.setFontFamily(attr, font.getFamily());
/* 1104 */         getStyle("default").addAttributes(attr);
/*      */         
/* 1106 */         attr = new SimpleAttributeSet();
/* 1107 */         StyleConstants.setFontSize(attr, font.getSize());
/* 1108 */         getStyle("default").addAttributes(attr);
/*      */         
/* 1110 */         attr = new SimpleAttributeSet();
/* 1111 */         StyleConstants.setBold(attr, font.isBold());
/* 1112 */         getStyle("default").addAttributes(attr);
/*      */         
/* 1114 */         attr = new SimpleAttributeSet();
/* 1115 */         StyleConstants.setItalic(attr, font.isItalic());
/* 1116 */         getStyle("default").addAttributes(attr);
/*      */         
/* 1118 */         attr = new SimpleAttributeSet();
/* 1119 */         Object underline = font.getAttributes().get(TextAttribute.UNDERLINE);
/* 1120 */         boolean canUnderline = ((underline instanceof Integer)) && (((Integer)underline).intValue() != -1);
/* 1121 */         StyleConstants.setUnderline(attr, canUnderline);
/* 1122 */         getStyle("default").addAttributes(attr);
/*      */       }
/*      */       
/* 1125 */       MutableAttributeSet attr = new SimpleAttributeSet();
/* 1126 */       StyleConstants.setSpaceAbove(attr, 0.0F);
/* 1127 */       getStyle("default").addAttributes(attr);
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   static class Renderer
/*      */     extends WrappedPlainView
/*      */   {
/*      */     JXLabel host;
/*      */     
/*      */ 
/* 1139 */     boolean invalidated = false;
/*      */     private float width;
/*      */     private float height;
/*      */     private View view;
/*      */     private ViewFactory factory;
/*      */     
/*      */     Renderer(JXLabel c, ViewFactory f, View v, boolean wordWrap) {
/* 1146 */       super(wordWrap);
/* 1147 */       this.factory = f;
/* 1148 */       this.view = v;
/* 1149 */       this.view.setParent(this);
/* 1150 */       this.host = c;
/*      */       
/*      */ 
/* 1153 */       if (this.host.getVisibleRect().width == 0) {
/* 1154 */         this.invalidated = true;
/* 1155 */         return;
/*      */       }
/* 1157 */       int w = this.host.getVisibleRect().width;
/*      */       
/*      */ 
/*      */ 
/*      */ 
/* 1162 */       setSize(c.getMaxLineSpan() > -1 ? c.getMaxLineSpan() : w, this.host.getVisibleRect().height);
/*      */     }
/*      */     
/*      */     protected void updateLayout(DocumentEvent.ElementChange ec, DocumentEvent e, Shape a)
/*      */     {
/* 1167 */       if (a != null)
/*      */       {
/* 1169 */         preferenceChanged(null, true, true);
/* 1170 */         Container host = getContainer();
/* 1171 */         if (host != null) {
/* 1172 */           host.repaint();
/*      */         }
/*      */       }
/*      */     }
/*      */     
/*      */     public void preferenceChanged(View child, boolean width, boolean height)
/*      */     {
/* 1179 */       if ((this.host != null) && (this.host.painted)) {
/* 1180 */         this.host.revalidate();
/* 1181 */         this.host.repaint();
/*      */       }
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     public AttributeSet getAttributes()
/*      */     {
/* 1192 */       return null;
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     public void paint(Graphics g, Shape allocation)
/*      */     {
/* 1203 */       Rectangle alloc = allocation.getBounds();
/*      */       
/*      */ 
/*      */ 
/*      */ 
/* 1208 */       if (g.getClipBounds() == null) {
/* 1209 */         g.setClip(alloc);
/* 1210 */         this.view.paint(g, allocation);
/* 1211 */         g.setClip(null);
/*      */       }
/*      */       else {
/* 1214 */         this.view.paint(g, allocation);
/*      */       }
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     public void setParent(View parent)
/*      */     {
/* 1226 */       throw new Error("Can't set parent on root view");
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     public int getViewCount()
/*      */     {
/* 1238 */       return 1;
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     public View getView(int n)
/*      */     {
/* 1249 */       return this.view;
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     public Document getDocument()
/*      */     {
/* 1259 */       return this.view == null ? null : this.view.getDocument();
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     public void setSize(float width, float height)
/*      */     {
/* 1270 */       if (this.host.maxLineSpan > 0) {
/* 1271 */         width = Math.min(width, this.host.maxLineSpan);
/*      */       }
/* 1273 */       if ((width == this.width) && (height == this.height)) {
/* 1274 */         return;
/*      */       }
/* 1276 */       this.width = ((int)width);
/* 1277 */       this.height = ((int)height);
/* 1278 */       this.view.setSize(width, height == 0.0F ? 32767.0F : height);
/* 1279 */       if (this.height == 0.0F) {
/* 1280 */         this.height = this.view.getPreferredSpan(1);
/*      */       }
/*      */     }
/*      */     
/*      */     public float getPreferredSpan(int axis)
/*      */     {
/* 1286 */       if (axis == 0)
/*      */       {
/*      */ 
/* 1289 */         if (this.invalidated) {
/* 1290 */           int w = this.host.getVisibleRect().width;
/* 1291 */           if (w != 0)
/*      */           {
/* 1293 */             this.invalidated = false;
/*      */             
/* 1295 */             setSize(w - this.host.getOccupiedWidth(), this.host.getVisibleRect().height);
/*      */           }
/*      */         }
/*      */         
/*      */ 
/* 1300 */         return this.width > 0.0F ? this.width : this.view.getPreferredSpan(axis);
/*      */       }
/* 1302 */       return this.view.getPreferredSpan(axis);
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     public Container getContainer()
/*      */     {
/* 1314 */       return this.host;
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     public ViewFactory getViewFactory()
/*      */     {
/* 1326 */       return this.factory;
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     public int getWidth()
/*      */     {
/* 1335 */       return (int)this.width;
/*      */     }
/*      */     
/*      */     public int getHeight()
/*      */     {
/* 1340 */       return (int)this.height;
/*      */     }
/*      */   }
/*      */   
/*      */   protected int getOccupiedWidth()
/*      */   {
/* 1346 */     return this.occupiedWidth;
/*      */   }
/*      */ }


/* Location:              E:\java\java学习\hutubill\lib\all.jar!\org\jdesktop\swingx\JXLabel.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */
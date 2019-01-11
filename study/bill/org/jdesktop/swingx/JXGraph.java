/*      */ package org.jdesktop.swingx;
/*      */ 
/*      */ import java.awt.BasicStroke;
/*      */ import java.awt.Color;
/*      */ import java.awt.Cursor;
/*      */ import java.awt.Dimension;
/*      */ import java.awt.FontMetrics;
/*      */ import java.awt.Graphics;
/*      */ import java.awt.Graphics2D;
/*      */ import java.awt.Point;
/*      */ import java.awt.Rectangle;
/*      */ import java.awt.RenderingHints;
/*      */ import java.awt.Stroke;
/*      */ import java.awt.event.MouseAdapter;
/*      */ import java.awt.event.MouseEvent;
/*      */ import java.awt.event.MouseMotionAdapter;
/*      */ import java.awt.event.MouseWheelEvent;
/*      */ import java.awt.event.MouseWheelListener;
/*      */ import java.awt.geom.GeneralPath;
/*      */ import java.awt.geom.Point2D;
/*      */ import java.awt.geom.Point2D.Double;
/*      */ import java.awt.geom.Rectangle2D;
/*      */ import java.awt.geom.Rectangle2D.Double;
/*      */ import java.beans.PropertyChangeEvent;
/*      */ import java.beans.PropertyChangeListener;
/*      */ import java.text.DecimalFormat;
/*      */ import java.text.NumberFormat;
/*      */ import java.util.LinkedList;
/*      */ import java.util.List;
/*      */ import org.jdesktop.beans.AbstractBean;
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
/*      */ public class JXGraph
/*      */   extends JXPanel
/*      */ {
/*      */   private static final float STROKE_AXIS = 1.2F;
/*      */   private static final float STROKE_GRID = 1.0F;
/*      */   private static final float ZOOM_MULTIPLIER = 1.1F;
/*      */   private PropertyChangeListener plotChangeListener;
/*  308 */   private Color majorGridColor = Color.GRAY.brighter();
/*  309 */   private Color minorGridColor = new Color(220, 220, 220);
/*  310 */   private Color axisColor = Color.BLACK;
/*      */   
/*      */   private List<DrawablePlot> plots;
/*      */   
/*      */   private double minX;
/*      */   
/*      */   private double maxX;
/*      */   
/*      */   private double minY;
/*      */   
/*      */   private double maxY;
/*      */   
/*      */   private Rectangle2D defaultView;
/*      */   
/*      */   private double originX;
/*      */   
/*      */   private double originY;
/*      */   
/*      */   private double majorX;
/*      */   
/*      */   private double defaultMajorX;
/*      */   
/*      */   private int minorCountX;
/*      */   
/*      */   private double majorY;
/*      */   
/*      */   private double defaultMajorY;
/*      */   
/*      */   private int minorCountY;
/*  339 */   private boolean textPainted = true;
/*  340 */   private boolean gridPainted = true;
/*  341 */   private boolean axisPainted = true;
/*  342 */   private boolean backPainted = true;
/*      */   
/*      */ 
/*      */   private Point dragStart;
/*      */   
/*      */ 
/*      */   private NumberFormat mainFormatter;
/*      */   
/*      */ 
/*      */   private NumberFormat secondFormatter;
/*      */   
/*  353 */   private boolean inputEnabled = true;
/*      */   
/*      */ 
/*      */   private ZoomHandler zoomHandler;
/*      */   
/*      */ 
/*      */   private PanMotionHandler panMotionHandler;
/*      */   
/*      */ 
/*      */   private PanHandler panHandler;
/*      */   
/*      */ 
/*      */   private ResetHandler resetHandler;
/*      */   
/*      */ 
/*      */   public JXGraph()
/*      */   {
/*  370 */     this(0.0D, 0.0D, -1.0D, 1.0D, -1.0D, 1.0D, 0.2D, 4, 0.2D, 4);
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
/*      */   public JXGraph(Rectangle2D view)
/*      */   {
/*  385 */     this(new Point2D.Double(view.getCenterX(), view.getCenterY()), view, 0.2D, 4, 0.2D, 4);
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
/*      */   public JXGraph(Rectangle2D view, double majorX, int minorCountX, double majorY, int minorCountY)
/*      */   {
/*  407 */     this(new Point2D.Double(view.getCenterX(), view.getCenterY()), view, majorX, minorCountX, majorY, minorCountY);
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
/*      */   public JXGraph(Point2D origin, Rectangle2D view)
/*      */   {
/*  423 */     this(origin, view, 0.2D, 4, 0.2D, 4);
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
/*      */   public JXGraph(Point2D origin, Rectangle2D view, double majorX, int minorCountX, double majorY, int minorCountY)
/*      */   {
/*  445 */     this(origin.getX(), origin.getY(), view.getMinX(), view.getMaxX(), view.getMinY(), view.getMaxY(), majorX, minorCountX, majorY, minorCountY);
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
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public JXGraph(double originX, double originY, double minX, double maxX, double minY, double maxY, double majorX, int minorCountX, double majorY, int minorCountY)
/*      */   {
/*  475 */     if (minX >= maxX) {
/*  476 */       throw new IllegalArgumentException("minX must be < to maxX");
/*      */     }
/*      */     
/*  479 */     if (minY >= maxY) {
/*  480 */       throw new IllegalArgumentException("minY must be < to maxY");
/*      */     }
/*      */     
/*  483 */     if (minorCountX < 0) {
/*  484 */       throw new IllegalArgumentException("minorCountX must be >= 0");
/*      */     }
/*      */     
/*  487 */     if (minorCountY < 0) {
/*  488 */       throw new IllegalArgumentException("minorCountY must be >= 0");
/*      */     }
/*      */     
/*  491 */     if (majorX <= 0.0D) {
/*  492 */       throw new IllegalArgumentException("majorX must be > 0.0");
/*      */     }
/*      */     
/*  495 */     if (majorY <= 0.0D) {
/*  496 */       throw new IllegalArgumentException("majorY must be > 0.0");
/*      */     }
/*      */     
/*  499 */     this.originX = originX;
/*  500 */     this.originY = originY;
/*      */     
/*  502 */     this.minX = minX;
/*  503 */     this.maxX = maxX;
/*  504 */     this.minY = minY;
/*  505 */     this.maxY = maxY;
/*      */     
/*  507 */     this.defaultView = new Rectangle2D.Double(minX, minY, maxX - minX, maxY - minY);
/*      */     
/*      */ 
/*  510 */     setMajorX(this.defaultMajorX = majorX);
/*  511 */     setMinorCountX(minorCountX);
/*  512 */     setMajorY(this.defaultMajorY = majorY);
/*  513 */     setMinorCountY(minorCountY);
/*      */     
/*  515 */     this.plots = new LinkedList();
/*      */     
/*  517 */     this.mainFormatter = NumberFormat.getInstance();
/*  518 */     this.mainFormatter.setMaximumFractionDigits(2);
/*      */     
/*  520 */     this.secondFormatter = new DecimalFormat("0.##E0");
/*      */     
/*  522 */     this.resetHandler = new ResetHandler(null);
/*  523 */     addMouseListener(this.resetHandler);
/*  524 */     this.panHandler = new PanHandler(null);
/*  525 */     addMouseListener(this.panHandler);
/*  526 */     this.panMotionHandler = new PanMotionHandler(null);
/*  527 */     addMouseMotionListener(this.panMotionHandler);
/*  528 */     this.zoomHandler = new ZoomHandler(null);
/*  529 */     addMouseWheelListener(this.zoomHandler);
/*      */     
/*  531 */     setBackground(Color.WHITE);
/*  532 */     setForeground(Color.BLACK);
/*      */     
/*  534 */     this.plotChangeListener = new PropertyChangeListener() {
/*      */       public void propertyChange(PropertyChangeEvent evt) {
/*  536 */         JXGraph.this.repaint();
/*      */       }
/*      */     };
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public boolean isOpaque()
/*      */   {
/*  546 */     if (!isBackgroundPainted()) {
/*  547 */       return false;
/*      */     }
/*  549 */     return super.isOpaque();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void setEnabled(boolean enabled)
/*      */   {
/*  558 */     super.setEnabled(enabled);
/*  559 */     setInputEnabled(enabled);
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
/*      */   public void setInputEnabled(boolean enabled)
/*      */   {
/*  574 */     if (this.inputEnabled != enabled) {
/*  575 */       boolean old = isInputEnabled();
/*  576 */       this.inputEnabled = enabled;
/*      */       
/*  578 */       if (enabled) {
/*  579 */         addMouseListener(this.resetHandler);
/*  580 */         addMouseListener(this.panHandler);
/*  581 */         addMouseMotionListener(this.panMotionHandler);
/*  582 */         addMouseWheelListener(this.zoomHandler);
/*      */       } else {
/*  584 */         removeMouseListener(this.resetHandler);
/*  585 */         removeMouseListener(this.panHandler);
/*  586 */         removeMouseMotionListener(this.panMotionHandler);
/*  587 */         removeMouseWheelListener(this.zoomHandler);
/*      */       }
/*      */       
/*  590 */       firePropertyChange("inputEnabled", old, isInputEnabled());
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public boolean isInputEnabled()
/*      */   {
/*  602 */     return this.inputEnabled;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public boolean isTextPainted()
/*      */   {
/*  614 */     return this.textPainted;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void setTextPainted(boolean textPainted)
/*      */   {
/*  626 */     boolean old = isTextPainted();
/*  627 */     this.textPainted = textPainted;
/*  628 */     firePropertyChange("textPainted", old, this.textPainted);
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
/*      */   public boolean isGridPainted()
/*      */   {
/*  641 */     return this.gridPainted;
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
/*      */   public void setGridPainted(boolean gridPainted)
/*      */   {
/*  654 */     boolean old = isGridPainted();
/*  655 */     this.gridPainted = gridPainted;
/*  656 */     firePropertyChange("gridPainted", old, isGridPainted());
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
/*      */   public boolean isAxisPainted()
/*      */   {
/*  669 */     return this.axisPainted;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void setAxisPainted(boolean axisPainted)
/*      */   {
/*  681 */     boolean old = isAxisPainted();
/*  682 */     this.axisPainted = axisPainted;
/*  683 */     firePropertyChange("axisPainted", old, isAxisPainted());
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
/*      */   public boolean isBackgroundPainted()
/*      */   {
/*  697 */     return this.backPainted;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void setBackgroundPainted(boolean backPainted)
/*      */   {
/*  709 */     boolean old = isBackgroundPainted();
/*  710 */     this.backPainted = backPainted;
/*  711 */     firePropertyChange("backgroundPainted", old, isBackgroundPainted());
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public Color getMajorGridColor()
/*      */   {
/*  722 */     return this.majorGridColor;
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
/*      */   public void setMajorGridColor(Color majorGridColor)
/*      */   {
/*  736 */     if (majorGridColor == null) {
/*  737 */       throw new IllegalArgumentException("Color cannot be null.");
/*      */     }
/*      */     
/*  740 */     Color old = getMajorGridColor();
/*  741 */     this.majorGridColor = majorGridColor;
/*  742 */     firePropertyChange("majorGridColor", old, getMajorGridColor());
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public Color getMinorGridColor()
/*      */   {
/*  753 */     return this.minorGridColor;
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
/*      */   public void setMinorGridColor(Color minorGridColor)
/*      */   {
/*  767 */     if (minorGridColor == null) {
/*  768 */       throw new IllegalArgumentException("Color cannot be null.");
/*      */     }
/*      */     
/*  771 */     Color old = getMinorGridColor();
/*  772 */     this.minorGridColor = minorGridColor;
/*  773 */     firePropertyChange("minorGridColor", old, getMinorGridColor());
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public Color getAxisColor()
/*      */   {
/*  784 */     return this.axisColor;
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
/*      */   public void setAxisColor(Color axisColor)
/*      */   {
/*  797 */     if (axisColor == null) {
/*  798 */       throw new IllegalArgumentException("Color cannot be null.");
/*      */     }
/*      */     
/*  801 */     Color old = getAxisColor();
/*  802 */     this.axisColor = axisColor;
/*  803 */     firePropertyChange("axisColor", old, getAxisColor());
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
/*      */   public double getMajorX()
/*      */   {
/*  818 */     return this.majorX;
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
/*      */   public void setMajorX(double majorX)
/*      */   {
/*  835 */     if (majorX <= 0.0D) {
/*  836 */       throw new IllegalArgumentException("majorX must be > 0.0");
/*      */     }
/*      */     
/*  839 */     double old = getMajorX();
/*  840 */     this.majorX = majorX;
/*  841 */     this.defaultMajorX = majorX;
/*  842 */     repaint();
/*  843 */     firePropertyChange("majorX", old, getMajorX());
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
/*      */   public int getMinorCountX()
/*      */   {
/*  858 */     return this.minorCountX;
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
/*      */   public void setMinorCountX(int minorCountX)
/*      */   {
/*  875 */     if (minorCountX < 0) {
/*  876 */       throw new IllegalArgumentException("minorCountX must be >= 0");
/*      */     }
/*      */     
/*  879 */     int old = getMinorCountX();
/*  880 */     this.minorCountX = minorCountX;
/*  881 */     repaint();
/*  882 */     firePropertyChange("minorCountX", old, getMinorCountX());
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
/*      */   public double getMajorY()
/*      */   {
/*  897 */     return this.majorY;
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
/*      */   public void setMajorY(double majorY)
/*      */   {
/*  914 */     if (majorY <= 0.0D) {
/*  915 */       throw new IllegalArgumentException("majorY must be > 0.0");
/*      */     }
/*      */     
/*  918 */     double old = getMajorY();
/*  919 */     this.majorY = majorY;
/*  920 */     this.defaultMajorY = majorY;
/*  921 */     repaint();
/*  922 */     firePropertyChange("majorY", old, getMajorY());
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
/*      */   public int getMinorCountY()
/*      */   {
/*  937 */     return this.minorCountY;
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
/*      */   public void setMinorCountY(int minorCountY)
/*      */   {
/*  954 */     if (minorCountY < 0) {
/*  955 */       throw new IllegalArgumentException("minorCountY must be >= 0");
/*      */     }
/*      */     
/*  958 */     int old = getMinorCountY();
/*  959 */     this.minorCountY = minorCountY;
/*  960 */     repaint();
/*  961 */     firePropertyChange("minorCountY", old, getMinorCountY());
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
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void setViewAndOrigin(Rectangle2D bounds)
/*      */   {
/*  987 */     setView(bounds);
/*  988 */     setOrigin(new Point2D.Double(bounds.getCenterX(), bounds.getCenterY()));
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
/*      */ 
/*      */ 
/*      */ 
/*      */   public void setView(Rectangle2D bounds)
/*      */   {
/* 1013 */     if (bounds == null) {
/* 1014 */       return;
/*      */     }
/* 1016 */     Rectangle2D old = getView();
/* 1017 */     this.defaultView = new Rectangle2D.Double(bounds.getX(), bounds.getY(), bounds.getWidth(), bounds.getHeight());
/*      */     
/*      */ 
/* 1020 */     this.minX = this.defaultView.getMinX();
/* 1021 */     this.maxX = this.defaultView.getMaxX();
/* 1022 */     this.minY = this.defaultView.getMinY();
/* 1023 */     this.maxY = this.defaultView.getMaxY();
/*      */     
/* 1025 */     this.majorX = this.defaultMajorX;
/* 1026 */     this.majorY = this.defaultMajorY;
/* 1027 */     firePropertyChange("view", old, getView());
/* 1028 */     repaint();
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
/*      */   public Rectangle2D getView()
/*      */   {
/* 1047 */     return new Rectangle2D.Double(this.minX, this.minY, this.maxX - this.minX, this.maxY - this.minY);
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
/*      */   public void resetView()
/*      */   {
/* 1061 */     setView(this.defaultView);
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
/*      */   public void setOrigin(Point2D origin)
/*      */   {
/* 1077 */     if (origin == null) {
/* 1078 */       return;
/*      */     }
/*      */     
/* 1081 */     Point2D old = getOrigin();
/* 1082 */     this.originX = origin.getX();
/* 1083 */     this.originY = origin.getY();
/* 1084 */     firePropertyChange("origin", old, getOrigin());
/* 1085 */     repaint();
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
/*      */   public Point2D getOrigin()
/*      */   {
/* 1098 */     return new Point2D.Double(this.originX, this.originY);
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
/*      */   public void addPlots(Color color, Plot... plotList)
/*      */   {
/* 1117 */     if (color == null) {
/* 1118 */       throw new IllegalArgumentException("Plots color cannot be null.");
/*      */     }
/*      */     
/* 1121 */     if (plotList == null) {
/* 1122 */       return;
/*      */     }
/*      */     
/* 1125 */     for (Plot plot : plotList) {
/* 1126 */       DrawablePlot drawablePlot = new DrawablePlot(plot, color, null);
/*      */       
/* 1128 */       if ((plot != null) && (!this.plots.contains(drawablePlot))) {
/* 1129 */         plot.addPropertyChangeListener(this.plotChangeListener);
/* 1130 */         this.plots.add(drawablePlot);
/*      */       }
/*      */     }
/* 1133 */     repaint();
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
/*      */   public void removePlots(Plot... plotList)
/*      */   {
/* 1152 */     if (plotList == null) {
/* 1153 */       return;
/*      */     }
/*      */     
/* 1156 */     for (Plot plot : plotList) {
/* 1157 */       if (plot != null) {
/* 1158 */         DrawablePlot toRemove = null;
/* 1159 */         for (DrawablePlot drawable : this.plots) {
/* 1160 */           if (drawable.getEquation() == plot) {
/* 1161 */             toRemove = drawable;
/* 1162 */             break;
/*      */           }
/*      */         }
/*      */         
/* 1166 */         if (toRemove != null) {
/* 1167 */           plot.removePropertyChangeListener(this.plotChangeListener);
/* 1168 */           this.plots.remove(toRemove);
/*      */         }
/*      */       }
/*      */     }
/* 1172 */     repaint();
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
/*      */   public void removeAllPlots()
/*      */   {
/* 1185 */     this.plots.clear();
/* 1186 */     repaint();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public Dimension getPreferredSize()
/*      */   {
/* 1194 */     return new Dimension(400, 400);
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
/*      */   protected double yPositionToPixel(double position)
/*      */   {
/* 1210 */     double height = getHeight();
/* 1211 */     return height - (position - this.minY) * height / (this.maxY - this.minY);
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
/*      */   protected double xPositionToPixel(double position)
/*      */   {
/* 1227 */     return (position - this.minX) * getWidth() / (this.maxX - this.minX);
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
/*      */   protected double xPixelToPosition(double pixel)
/*      */   {
/* 1245 */     return this.minX + pixel * (this.maxX - this.minX) / getWidth();
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
/*      */   protected double yPixelToPosition(double pixel)
/*      */   {
/* 1263 */     return this.minY + (getHeight() - pixel) * (this.maxY - this.minY) / getHeight();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   protected void paintComponent(Graphics g)
/*      */   {
/* 1271 */     if (!isVisible()) {
/* 1272 */       return;
/*      */     }
/*      */     
/* 1275 */     Graphics2D g2 = (Graphics2D)g;
/* 1276 */     setupGraphics(g2);
/*      */     
/* 1278 */     paintBackground(g2);
/* 1279 */     drawGrid(g2);
/* 1280 */     drawAxis(g2);
/* 1281 */     drawPlots(g2);
/* 1282 */     drawLabels(g2);
/*      */     
/* 1284 */     paintExtra(g2);
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
/*      */   protected void paintExtra(Graphics2D g2) {}
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   private void drawPlots(Graphics2D g2)
/*      */   {
/* 1307 */     for (DrawablePlot drawable : this.plots) {
/* 1308 */       g2.setColor(drawable.getColor());
/* 1309 */       drawPlot(g2, drawable.getEquation());
/*      */     }
/*      */   }
/*      */   
/*      */   private void drawPlot(Graphics2D g2, Plot equation)
/*      */   {
/* 1315 */     float x = 0.0F;
/* 1316 */     float y = (float)yPositionToPixel(equation.compute(xPixelToPosition(0.0D)));
/*      */     
/* 1318 */     GeneralPath path = new GeneralPath();
/* 1319 */     path.moveTo(x, y);
/*      */     
/* 1321 */     float width = getWidth();
/* 1322 */     for (x = 0.0F; x < width; x += 1.0F) {
/* 1323 */       double position = xPixelToPosition(x);
/* 1324 */       y = (float)yPositionToPixel(equation.compute(position));
/* 1325 */       path.lineTo(x, y);
/*      */     }
/*      */     
/* 1328 */     g2.draw(path);
/*      */   }
/*      */   
/*      */   private void drawGrid(Graphics2D g2)
/*      */   {
/* 1333 */     Stroke stroke = g2.getStroke();
/*      */     
/* 1335 */     if (isGridPainted()) {
/* 1336 */       drawVerticalGrid(g2);
/* 1337 */       drawHorizontalGrid(g2);
/*      */     }
/*      */     
/* 1340 */     g2.setStroke(stroke);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   private void drawLabels(Graphics2D g2)
/*      */   {
/* 1347 */     if (isTextPainted()) {
/* 1348 */       double axisH = yPositionToPixel(this.originY);
/* 1349 */       double axisV = xPositionToPixel(this.originX);
/*      */       
/* 1351 */       if (isAxisPainted()) {
/* 1352 */         Stroke stroke = g2.getStroke();
/* 1353 */         g2.setStroke(new BasicStroke(1.2F));
/* 1354 */         g2.setColor(getAxisColor());
/* 1355 */         g2.drawLine((int)axisV - 3, (int)axisH, (int)axisV + 3, (int)axisH);
/*      */         
/* 1357 */         g2.drawLine((int)axisV, (int)axisH - 3, (int)axisV, (int)axisH + 3);
/*      */         
/* 1359 */         g2.setStroke(stroke);
/*      */       }
/*      */       
/* 1362 */       g2.setColor(getForeground());
/* 1363 */       FontMetrics metrics = g2.getFontMetrics();
/* 1364 */       g2.drawString(format(this.originX) + "; " + format(this.originY), (int)axisV + 5, (int)axisH + metrics.getHeight());
/*      */       
/*      */ 
/*      */ 
/* 1368 */       drawHorizontalAxisLabels(g2);
/* 1369 */       drawVerticalAxisLabels(g2);
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */   private void drawVerticalAxisLabels(Graphics2D g2)
/*      */   {
/* 1376 */     double axisV = xPositionToPixel(this.originX);
/*      */     
/*      */ 
/* 1379 */     double startY = Math.floor(this.minY / this.majorY) * this.majorY;
/* 1380 */     for (double y = startY; y < this.maxY + this.majorY; y += this.majorY) {
/* 1381 */       if ((y - this.majorY / 2.0D >= this.originY) || (y + this.majorY / 2.0D <= this.originY))
/*      */       {
/*      */ 
/*      */ 
/*      */ 
/* 1386 */         int position = (int)yPositionToPixel(y);
/* 1387 */         g2.drawString(format(y), (int)axisV + 5, position);
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */   private void drawHorizontalGrid(Graphics2D g2)
/*      */   {
/* 1394 */     double minorSpacing = this.majorY / getMinorCountY();
/* 1395 */     double axisV = xPositionToPixel(this.originX);
/*      */     
/* 1397 */     Stroke gridStroke = new BasicStroke(1.0F);
/* 1398 */     Stroke axisStroke = new BasicStroke(1.2F);
/*      */     
/* 1400 */     Rectangle clip = g2.getClipBounds();
/*      */     
/*      */ 
/*      */ 
/* 1404 */     if (!isAxisPainted()) {
/* 1405 */       int position = (int)xPositionToPixel(this.originX);
/* 1406 */       if ((position >= clip.x) && (position <= clip.x + clip.width)) {
/* 1407 */         g2.setColor(getMajorGridColor());
/* 1408 */         g2.drawLine(position, clip.y, position, clip.y + clip.height);
/*      */       }
/*      */     }
/*      */     
/*      */ 
/* 1413 */     double startY = Math.floor(this.minY / this.majorY) * this.majorY;
/* 1414 */     for (double y = startY; y < this.maxY + this.majorY; y += this.majorY) {
/* 1415 */       g2.setStroke(gridStroke);
/* 1416 */       g2.setColor(getMinorGridColor());
/* 1417 */       for (int i = 0; i < getMinorCountY(); i++) {
/* 1418 */         int position = (int)yPositionToPixel(y - i * minorSpacing);
/* 1419 */         if ((position >= clip.y) && (position <= clip.y + clip.height)) {
/* 1420 */           g2.drawLine(clip.x, position, clip.x + clip.width, position);
/*      */         }
/*      */       }
/*      */       
/* 1424 */       int position = (int)yPositionToPixel(y);
/* 1425 */       if ((position >= clip.y) && (position <= clip.y + clip.height)) {
/* 1426 */         g2.setColor(getMajorGridColor());
/* 1427 */         g2.drawLine(clip.x, position, clip.x + clip.width, position);
/*      */         
/* 1429 */         if (isAxisPainted()) {
/* 1430 */           g2.setStroke(axisStroke);
/* 1431 */           g2.setColor(getAxisColor());
/* 1432 */           g2.drawLine((int)axisV - 3, position, (int)axisV + 3, position);
/*      */         }
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */   private void drawHorizontalAxisLabels(Graphics2D g2)
/*      */   {
/* 1441 */     double axisH = yPositionToPixel(this.originY);
/* 1442 */     FontMetrics metrics = g2.getFontMetrics();
/*      */     
/*      */ 
/* 1445 */     double startX = Math.floor(this.minX / this.majorX) * this.majorX;
/* 1446 */     for (double x = startX; x < this.maxX + this.majorX; x += this.majorX) {
/* 1447 */       if ((x - this.majorX / 2.0D >= this.originX) || (x + this.majorX / 2.0D <= this.originX))
/*      */       {
/*      */ 
/*      */ 
/*      */ 
/* 1452 */         int position = (int)xPositionToPixel(x);
/* 1453 */         g2.drawString(format(x), position, (int)axisH + metrics.getHeight());
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */   private void drawVerticalGrid(Graphics2D g2)
/*      */   {
/* 1461 */     double minorSpacing = this.majorX / getMinorCountX();
/* 1462 */     double axisH = yPositionToPixel(this.originY);
/*      */     
/* 1464 */     Stroke gridStroke = new BasicStroke(1.0F);
/* 1465 */     Stroke axisStroke = new BasicStroke(1.2F);
/*      */     
/* 1467 */     Rectangle clip = g2.getClipBounds();
/*      */     
/*      */ 
/* 1470 */     if (!isAxisPainted()) {
/* 1471 */       int position = (int)yPositionToPixel(this.originY);
/* 1472 */       if ((position >= clip.y) && (position <= clip.y + clip.height)) {
/* 1473 */         g2.setColor(getMajorGridColor());
/* 1474 */         g2.drawLine(clip.x, position, clip.x + clip.width, position);
/*      */       }
/*      */     }
/*      */     
/*      */ 
/* 1479 */     double startX = Math.floor(this.minX / this.majorX) * this.majorX;
/* 1480 */     for (double x = startX; x < this.maxX + this.majorX; x += this.majorX) {
/* 1481 */       g2.setStroke(gridStroke);
/* 1482 */       g2.setColor(getMinorGridColor());
/* 1483 */       for (int i = 0; i < getMinorCountX(); i++) {
/* 1484 */         int position = (int)xPositionToPixel(x - i * minorSpacing);
/* 1485 */         if ((position >= clip.x) && (position <= clip.x + clip.width)) {
/* 1486 */           g2.drawLine(position, clip.y, position, clip.y + clip.height);
/*      */         }
/*      */       }
/*      */       
/* 1490 */       int position = (int)xPositionToPixel(x);
/* 1491 */       if ((position >= clip.x) && (position <= clip.x + clip.width)) {
/* 1492 */         g2.setColor(getMajorGridColor());
/* 1493 */         g2.drawLine(position, clip.y, position, clip.y + clip.height);
/*      */         
/* 1495 */         if (isAxisPainted()) {
/* 1496 */           g2.setStroke(axisStroke);
/* 1497 */           g2.setColor(getAxisColor());
/* 1498 */           g2.drawLine(position, (int)axisH - 3, position, (int)axisH + 3);
/*      */         }
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */   private void drawAxis(Graphics2D g2)
/*      */   {
/* 1506 */     if (!isAxisPainted()) {
/* 1507 */       return;
/*      */     }
/*      */     
/* 1510 */     double axisH = yPositionToPixel(this.originY);
/* 1511 */     double axisV = xPositionToPixel(this.originX);
/*      */     
/* 1513 */     Rectangle clip = g2.getClipBounds();
/*      */     
/* 1515 */     g2.setColor(getAxisColor());
/* 1516 */     Stroke stroke = g2.getStroke();
/* 1517 */     g2.setStroke(new BasicStroke(1.2F));
/*      */     
/* 1519 */     if ((axisH >= clip.y) && (axisH <= clip.y + clip.height)) {
/* 1520 */       g2.drawLine(clip.x, (int)axisH, clip.x + clip.width, (int)axisH);
/*      */     }
/* 1522 */     if ((axisV >= clip.x) && (axisV <= clip.x + clip.width)) {
/* 1523 */       g2.drawLine((int)axisV, clip.y, (int)axisV, clip.y + clip.height);
/*      */     }
/*      */     
/* 1526 */     g2.setStroke(stroke);
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
/*      */   protected void setupGraphics(Graphics2D g2)
/*      */   {
/* 1541 */     g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
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
/*      */   protected void paintBackground(Graphics2D g2)
/*      */   {
/* 1560 */     if (isBackgroundPainted()) {
/* 1561 */       Painter p = getBackgroundPainter();
/* 1562 */       if (p != null) {
/* 1563 */         p.paint(g2, this, getWidth(), getHeight());
/*      */       } else {
/* 1565 */         g2.setColor(getBackground());
/* 1566 */         g2.fill(g2.getClipBounds());
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   private String format(double number)
/*      */   {
/* 1575 */     boolean farAway = ((number != 0.0D) && (Math.abs(number) < 0.01D)) || (Math.abs(number) > 99.0D);
/*      */     
/* 1577 */     return (farAway ? this.secondFormatter : this.mainFormatter).format(number);
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
/*      */   public static abstract class Plot
/*      */     extends AbstractBean
/*      */   {
/*      */     public abstract double compute(double paramDouble);
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
/*      */   private static class DrawablePlot
/*      */   {
/*      */     private final JXGraph.Plot equation;
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
/*      */     private final Color color;
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
/*      */     private DrawablePlot(JXGraph.Plot equation, Color color)
/*      */     {
/* 1633 */       this.equation = equation;
/* 1634 */       this.color = color;
/*      */     }
/*      */     
/*      */     private JXGraph.Plot getEquation() {
/* 1638 */       return this.equation;
/*      */     }
/*      */     
/*      */     private Color getColor() {
/* 1642 */       return this.color;
/*      */     }
/*      */     
/*      */     public boolean equals(Object o)
/*      */     {
/* 1647 */       if (this == o) {
/* 1648 */         return true;
/*      */       }
/* 1650 */       if ((o == null) || (getClass() != o.getClass())) {
/* 1651 */         return false;
/*      */       }
/* 1653 */       DrawablePlot that = (DrawablePlot)o;
/* 1654 */       if (!this.color.equals(that.color)) {
/* 1655 */         return false;
/*      */       }
/* 1657 */       return this.equation.equals(that.equation);
/*      */     }
/*      */     
/*      */ 
/*      */     public int hashCode()
/*      */     {
/* 1663 */       int result = this.equation.hashCode();
/* 1664 */       result = 29 * result + this.color.hashCode();
/* 1665 */       return result;
/*      */     }
/*      */   }
/*      */   
/*      */   private class ZoomHandler implements MouseWheelListener {
/*      */     private ZoomHandler() {}
/*      */     
/*      */     public void mouseWheelMoved(MouseWheelEvent e) {
/* 1673 */       double distanceX = JXGraph.this.maxX - JXGraph.this.minX;
/* 1674 */       double distanceY = JXGraph.this.maxY - JXGraph.this.minY;
/*      */       
/* 1676 */       double cursorX = JXGraph.this.minX + distanceX / 2.0D;
/* 1677 */       double cursorY = JXGraph.this.minY + distanceY / 2.0D;
/*      */       
/* 1679 */       int rotation = e.getWheelRotation();
/* 1680 */       if (rotation < 0) {
/* 1681 */         distanceX /= 1.100000023841858D;
/* 1682 */         distanceY /= 1.100000023841858D;
/*      */         
/* 1684 */         JXGraph.access$1142(JXGraph.this, 1.100000023841858D);
/* 1685 */         JXGraph.access$1242(JXGraph.this, 1.100000023841858D);
/*      */       } else {
/* 1687 */         distanceX *= 1.100000023841858D;
/* 1688 */         distanceY *= 1.100000023841858D;
/*      */         
/* 1690 */         JXGraph.access$1134(JXGraph.this, 1.100000023841858D);
/* 1691 */         JXGraph.access$1234(JXGraph.this, 1.100000023841858D);
/*      */       }
/*      */       
/* 1694 */       JXGraph.this.minX = (cursorX - distanceX / 2.0D);
/* 1695 */       JXGraph.this.maxX = (cursorX + distanceX / 2.0D);
/* 1696 */       JXGraph.this.minY = (cursorY - distanceY / 2.0D);
/* 1697 */       JXGraph.this.maxY = (cursorY + distanceY / 2.0D);
/*      */       
/* 1699 */       JXGraph.this.repaint();
/*      */     }
/*      */   }
/*      */   
/*      */   private class ResetHandler extends MouseAdapter {
/*      */     private ResetHandler() {}
/*      */     
/*      */     public void mousePressed(MouseEvent e) {
/* 1707 */       if (e.getButton() != 2) {
/* 1708 */         return;
/*      */       }
/*      */       
/* 1711 */       JXGraph.this.resetView();
/*      */     }
/*      */   }
/*      */   
/*      */   private class PanHandler extends MouseAdapter {
/*      */     private PanHandler() {}
/*      */     
/*      */     public void mousePressed(MouseEvent e) {
/* 1719 */       if (e.getButton() != 1) {
/* 1720 */         return;
/*      */       }
/*      */       
/* 1723 */       JXGraph.this.dragStart = e.getPoint();
/* 1724 */       JXGraph.this.setCursor(Cursor.getPredefinedCursor(13));
/*      */     }
/*      */     
/*      */     public void mouseReleased(MouseEvent e)
/*      */     {
/* 1729 */       if (e.getButton() != 1) {
/* 1730 */         return;
/*      */       }
/*      */       
/* 1733 */       JXGraph.this.setCursor(Cursor.getDefaultCursor());
/*      */     }
/*      */   }
/*      */   
/*      */   private class PanMotionHandler extends MouseMotionAdapter
/*      */   {
/*      */     private PanMotionHandler() {}
/*      */     
/*      */     public void mouseDragged(MouseEvent e) {
/* 1742 */       Point dragEnd = e.getPoint();
/*      */       
/* 1744 */       double distance = JXGraph.this.xPixelToPosition(dragEnd.getX()) - JXGraph.this.xPixelToPosition(JXGraph.this.dragStart.getX());
/*      */       
/* 1746 */       JXGraph.this.minX = (JXGraph.this.minX - distance);
/* 1747 */       JXGraph.this.maxX = (JXGraph.this.maxX - distance);
/*      */       
/* 1749 */       distance = JXGraph.this.yPixelToPosition(dragEnd.getY()) - JXGraph.this.yPixelToPosition(JXGraph.this.dragStart.getY());
/*      */       
/* 1751 */       JXGraph.this.minY = (JXGraph.this.minY - distance);
/* 1752 */       JXGraph.this.maxY = (JXGraph.this.maxY - distance);
/*      */       
/* 1754 */       JXGraph.this.repaint();
/* 1755 */       JXGraph.this.dragStart = dragEnd;
/*      */     }
/*      */   }
/*      */ }


/* Location:              E:\java\java学习\hutubill\lib\all.jar!\org\jdesktop\swingx\JXGraph.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */
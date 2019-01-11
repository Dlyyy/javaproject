/*      */ package com.objectplanet.chart;
/*      */ 
/*      */ import java.awt.AWTEvent;
/*      */ import java.awt.Color;
/*      */ import java.awt.Component;
/*      */ import java.awt.Cursor;
/*      */ import java.awt.Dimension;
/*      */ import java.awt.Font;
/*      */ import java.awt.FontMetrics;
/*      */ import java.awt.Frame;
/*      */ import java.awt.Graphics;
/*      */ import java.awt.Image;
/*      */ import java.awt.Insets;
/*      */ import java.awt.ItemSelectable;
/*      */ import java.awt.MediaTracker;
/*      */ import java.awt.Point;
/*      */ import java.awt.Polygon;
/*      */ import java.awt.Rectangle;
/*      */ import java.awt.Toolkit;
/*      */ import java.awt.event.ItemEvent;
/*      */ import java.awt.event.ItemListener;
/*      */ import java.awt.event.MouseEvent;
/*      */ import java.awt.image.MemoryImageSource;
/*      */ import java.awt.image.PixelGrabber;
/*      */ import java.io.PrintStream;
/*      */ import java.lang.reflect.Constructor;
/*      */ import java.lang.reflect.Field;
/*      */ import java.lang.reflect.InvocationTargetException;
/*      */ import java.lang.reflect.Method;
/*      */ import java.text.DecimalFormat;
/*      */ import java.text.NumberFormat;
/*      */ import java.util.Enumeration;
/*      */ import java.util.Hashtable;
/*      */ import java.util.Locale;
/*      */ import java.util.Vector;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ public abstract class Chart
/*      */   extends Component
/*      */   implements ItemSelectable, Runnable
/*      */ {
/*  184 */   static final double[] STEPS = { 1.0D, 2.0D, 2.5D, 5.0D };
/*  185 */   static final double[] STEPS_TIME = { 1.0D, 2.0D, 5.0D, 10.0D, 30.0D, 60.0D, 120.0D, 300.0D, 600.0D, 1200.0D, 1800.0D, 3600.0D, 7200.0D, 14400.0D, 21600.0D, 43200.0D, 86400.0D, 173200.0D, 864000.0D };
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  304 */   private static Color[] DEFAULT_SAMPLE_COLORS = {
/*  305 */     new Color(132, 130, 255), 
/*  306 */     new Color(132, 32, 99), 
/*  307 */     new Color(255, 255, 198), 
/*  308 */     new Color(165, 227, 231), 
/*  309 */     new Color(99, 0, 132), 
/*  310 */     new Color(255, 130, 132), 
/*  311 */     new Color(0, 130, 198), 
/*  312 */     new Color(198, 195, 255), 
/*  313 */     new Color(0, 0, 132), 
/*  314 */     new Color(255, 0, 255), 
/*  315 */     new Color(255, 255, 0), 
/*  316 */     new Color(0, 255, 255), 
/*  317 */     new Color(132, 0, 132), 
/*  318 */     new Color(132, 0, 0), 
/*  319 */     new Color(0, 130, 132), 
/*  320 */     new Color(0, 0, 255), 
/*  321 */     new Color(0, 207, 255), 
/*  322 */     new Color(107, 255, 255), 
/*  323 */     new Color(206, 255, 206), 
/*  324 */     new Color(255, 255, 156) };
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  353 */   private static Object[] fifo_queue = new Object['΄'];
/*  354 */   private static Hashtable angledLabelCache = new Hashtable();
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public Chart(int count)
/*      */   {
/*  363 */     this(1, count);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public Chart(int seriesCount, int sampleCount)
/*      */   {
/*  374 */     this.chartData = new ChartData(seriesCount, sampleCount);
/*  375 */     this.sampleLabelColors = new Color[sampleCount];
/*  376 */     this.seriesLabelColors = new Color[seriesCount];
/*  377 */     this.valueLabelColors = new Color[seriesCount];
/*  378 */     this.listeners = new Vector();
/*  379 */     this.labels = new Hashtable();
/*  380 */     this.labelFonts = new Hashtable();
/*  381 */     this.labelAngles = new Hashtable();
/*  382 */     this.graphInsets = new Insets(-1, -1, -1, -1);
/*  383 */     this.preferredSize = new Dimension(300, 200);
/*  384 */     this.targetsLabel = new Hashtable();
/*  385 */     this.targetsValue = new Hashtable();
/*  386 */     this.targetsColor = new Hashtable();
/*  387 */     this.targetsStyle = new Hashtable();
/*  388 */     this.targetLabelsPosition = -1;
/*  389 */     this.labelIDs = new String[0];
/*  390 */     this.labelTexts = new Hashtable();
/*  391 */     this.labelXs = new Hashtable();
/*  392 */     this.labelYs = new Hashtable();
/*  393 */     this.labelURLs = new Hashtable();
/*  394 */     this.labelSeries = new Hashtable();
/*  395 */     this.labelSamples = new Hashtable();
/*  396 */     this.chartDataBounds = new Rectangle();
/*  397 */     this.depth3dPoint = new Point();
/*  398 */     this.depth3d = -1;
/*  399 */     this.rangeAdjusterBounds = new Rectangle[2];
/*  400 */     this.rangeAdjusterBounds[0] = new Rectangle();
/*  401 */     this.rangeAdjusterBounds[1] = new Rectangle();
/*  402 */     this.sampleScrollerBounds = new Rectangle();
/*  403 */     RESIZE_CURSOR = new Cursor(8);
/*  404 */     RESIZE_HOR_CURSOR = new Cursor(11);
/*  405 */     HAND_CURSOR = new Cursor(12);
/*  406 */     DEFAULT_CURSOR = new Cursor(0);
/*  407 */     GRID_CURSOR_VERT = new Cursor(8);
/*  408 */     GRID_CURSOR_HORZ = new Cursor(11);
/*  409 */     this.old_cursor = null;
/*  410 */     this.new_cursor = DEFAULT_CURSOR;
/*  411 */     setCursor(this.new_cursor);
/*  412 */     this.legend = new Rectangle();
/*  413 */     this.visibleLegend = new Rectangle();
/*  414 */     this.labelSizeCache = new Hashtable();
/*  415 */     this.valueLabelsOn = new boolean[seriesCount];
/*  416 */     this.rangeOn = new boolean[1];
/*  417 */     this.rangePosition = new int[this.rangeOn.length];
/*  418 */     this.upperRange = new double[this.rangeOn.length];
/*  419 */     this.lowerRange = new double[this.rangeOn.length];
/*  420 */     this.currentUpperRange = new double[this.rangeOn.length];
/*  421 */     this.currentLowerRange = new double[this.rangeOn.length];
/*  422 */     this.rangeDecimalCount = new int[this.rangeOn.length];
/*  423 */     this.rangeColor = new Color[this.rangeOn.length];
/*  424 */     this.rangeBounds = new Rectangle[this.rangeOn.length];
/*  425 */     this.rangeLabelsOn = new boolean[this.rangeOn.length];
/*  426 */     this.zeroLine = new int[this.rangeOn.length];
/*  427 */     this.seriesRange = new int[seriesCount];
/*  428 */     this.rangeAdjusterOn = new boolean[this.rangeOn.length];
/*  429 */     this.rangeAdjusted = new int[this.rangeOn.length];
/*  430 */     this.overlayCharts = new Vector();
/*  431 */     this.legendSelection = new boolean[seriesCount > 1 ? seriesCount : sampleCount];
/*  432 */     this.verticalLegendScroller = new Rectangle();
/*  433 */     this.horizontalLegendScroller = new Rectangle();
/*  434 */     this.visibleSamples = new int[2];
/*  435 */     this.visibleSamples[1] = 2147483646;
/*  436 */     this.mousePosition = new Point();
/*  437 */     this.gridAdjustment = new boolean[4];
/*  438 */     this.numberFormatter = NumberFormat.getInstance();
/*  439 */     setSampleCount(sampleCount);
/*  440 */     setSeriesCount(seriesCount);
/*      */     
/*      */ 
/*  443 */     enableEvents(49L);
/*      */     
/*      */     try
/*      */     {
/*  447 */       this.c2 = new Chart2();
/*  448 */       this.c2.version = getVersion();
/*      */ 
/*      */     }
/*      */     catch (Throwable e)
/*      */     {
/*  453 */       throw new IllegalAccessError("No valid license");
/*      */     }
/*      */     
/*      */ 
/*  457 */     this.automaticRepaintOn = true;
/*  458 */     this.chartTitle = null;
/*  459 */     this.chartTitleOn = false;
/*  460 */     this.sampleDecimalCount = new int[getSeriesCount()];
/*  461 */     this.legendOn = false;
/*  462 */     this.legendPosition = 1;
/*  463 */     this.display3dOn = false;
/*  464 */     this.chartBackground = Color.white;
/*  465 */     this.chartForeground = Color.black;
/*  466 */     setForeground(Color.black);
/*  467 */     setBackground(new Color(231, 221, 231));
/*  468 */     this.labelFonts.clear();
/*  469 */     this.labels.clear();
/*  470 */     this.legendLabels = null;
/*  471 */     this.graphInsets.top = (this.graphInsets.bottom = this.graphInsets.left = this.graphInsets.right = -1);
/*  472 */     this.preferredSize.setSize(300, 200);
/*  473 */     this.lastSelectedSample = -1;
/*  474 */     this.lastSelectedSeries = -1;
/*  475 */     this.mouseOverSampleIndex = -1;
/*  476 */     this.mouseOverSeriesIndex = -1;
/*  477 */     setFont(new Font("Arial", 0, 11));
/*  478 */     setFont("titleFont", new Font("Arial", 1, 14));
/*  479 */     setSampleColors(null);
/*  480 */     this.seriesRange = new int[getSeriesCount()];
/*  481 */     this.valueLabelsOn = new boolean[getSeriesCount()];
/*  482 */     this.overlayChartOn = true;
/*  483 */     this.servletModeOn = false;
/*  484 */     this.externalGraphicsOn = false;
/*  485 */     this.floatingOnLegendOn = true;
/*  486 */     this.barLabels = null;
/*  487 */     this.needRender = true;
/*  488 */     this.needGraphBounds = true;
/*  489 */     this.needChartCalculation = true;
/*  490 */     this.mousePressX = -1;
/*  491 */     this.mousePressY = -1;
/*  492 */     this.shouldSelect = true;
/*  493 */     this.labelIDs = new String[0];
/*  494 */     this.labelTexts.clear();
/*  495 */     this.labelXs.clear();
/*  496 */     this.labelYs.clear();
/*  497 */     this.labelSeries.clear();
/*  498 */     this.labelSamples.clear();
/*  499 */     this.labelURLs.clear();
/*  500 */     this.gridAlignment = 1;
/*  501 */     setSize(300, 200);
/*  502 */     this.images = new Hashtable();
/*  503 */     this.imageTracker = new MediaTracker(this);
/*      */     
/*      */ 
/*  506 */     this.maxValueLineCount = Integer.MAX_VALUE;
/*  507 */     this.maxGridLineCount = 1000;
/*  508 */     this.leftSampleAxisRange = 0.0D;
/*  509 */     this.rightSampleAxisRange = 100.0D;
/*  510 */     this.valueLinesOn = false;
/*  511 */     this.valueLinesColor = Color.lightGray;
/*  512 */     this.rangeOn[0] = true;
/*  513 */     this.rangeDecimalCount[0] = 0;
/*  514 */     this.rangeLabelsOn[0] = true;
/*  515 */     this.sampleLabelsOn = false;
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*  520 */     this.rangeAdjusterOn[0] = false;
/*  521 */     this.sampleScrollerOn = false;
/*  522 */     this.upperRange[0] = 100.0D;
/*  523 */     this.lowerRange[0] = 0.0D;
/*  524 */     this.currentUpperRange[0] = 100.0D;
/*  525 */     this.currentLowerRange[0] = 0.0D;
/*  526 */     this.rangeAdjusterPosition = 1;
/*  527 */     this.adjusterIndex = -1;
/*  528 */     this.gridEdgeToAdjust = -1;
/*  529 */     this.rangePosition[0] = 0;
/*  530 */     this.rangeAdjusted[0] = 0;
/*  531 */     this.gridLines = null;
/*  532 */     this.defaultGridLines = null;
/*  533 */     this.defaultGridLinesColor = Color.lightGray;
/*  534 */     this.gridLineColors = null;
/*  535 */     this.leftSampleAxisRange = 0.0D;
/*  536 */     this.rightSampleAxisRange = 100.0D;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void reset()
/*      */   {
/*  545 */     this.chartTitle = null;
/*  546 */     this.chartTitleOn = false;
/*  547 */     this.sampleDecimalCount = new int[getSeriesCount()];
/*  548 */     this.legendOn = false;
/*  549 */     this.legendPosition = 1;
/*  550 */     this.legendReverseOn = false;
/*  551 */     this.display3dOn = false;
/*  552 */     this.chartBackground = Color.white;
/*  553 */     this.chartForeground = Color.black;
/*  554 */     setForeground(Color.black);
/*  555 */     setBackground(new Color(231, 221, 231));
/*  556 */     this.labelFonts.clear();
/*  557 */     this.labels.clear();
/*  558 */     this.labelAngles.clear();
/*  559 */     this.legendLabels = null;
/*  560 */     this.graphInsets.top = (this.graphInsets.bottom = this.graphInsets.left = this.graphInsets.right = -1);
/*  561 */     this.preferredSize.setSize(300, 200);
/*  562 */     this.lastSelectedSample = -1;
/*  563 */     this.lastSelectedSeries = -1;
/*  564 */     this.mouseOverSampleIndex = -1;
/*  565 */     this.mouseOverSeriesIndex = -1;
/*  566 */     setFont(new Font("Arial", 0, 11));
/*  567 */     setFont("titleFont", new Font("Arial", 1, 14));
/*  568 */     setSampleColors(null);
/*  569 */     this.seriesRange = new int[getSeriesCount()];
/*  570 */     this.valueLabelsOn = new boolean[getSeriesCount()];
/*  571 */     this.overlayChartOn = true;
/*  572 */     this.servletModeOn = false;
/*  573 */     this.externalGraphicsOn = false;
/*  574 */     this.floatingOnLegendOn = true;
/*  575 */     setSampleLabels(null);
/*  576 */     setSeriesLabels(null);
/*  577 */     this.barLabels = null;
/*  578 */     setSeriesCount(1);
/*  579 */     setSampleCount(1);
/*  580 */     setSampleValues(0, null);
/*  581 */     this.needRender = true;
/*  582 */     this.needGraphBounds = true;
/*  583 */     this.needChartCalculation = true;
/*  584 */     this.mousePressX = -1;
/*  585 */     this.mousePressY = -1;
/*  586 */     this.shouldSelect = true;
/*  587 */     this.labelIDs = new String[0];
/*  588 */     this.labelTexts.clear();
/*  589 */     this.labelXs.clear();
/*  590 */     this.labelYs.clear();
/*  591 */     this.labelSeries.clear();
/*  592 */     this.labelSamples.clear();
/*  593 */     this.labelURLs.clear();
/*  594 */     this.gridAlignment = 1;
/*  595 */     setSize(300, 200);
/*  596 */     this.images = new Hashtable();
/*  597 */     this.imageTracker = new MediaTracker(this);
/*  598 */     this.displayVersionOn = false;
/*  599 */     this.currentLocale = null;
/*      */     
/*      */ 
/*  602 */     this.maxValueLineCount = Integer.MAX_VALUE;
/*  603 */     this.maxGridLineCount = 1000;
/*  604 */     this.leftSampleAxisRange = 0.0D;
/*  605 */     this.rightSampleAxisRange = 100.0D;
/*  606 */     this.valueLinesOn = false;
/*  607 */     this.valueLinesColor = Color.lightGray;
/*  608 */     this.rangeOn[0] = true;
/*  609 */     this.rangeDecimalCount[0] = 0;
/*  610 */     this.rangeLabelsOn[0] = true;
/*  611 */     this.sampleLabelsOn = false;
/*  612 */     this.targetsLabel.clear();
/*  613 */     this.targetsValue.clear();
/*  614 */     this.targetsColor.clear();
/*  615 */     this.targetsStyle.clear();
/*  616 */     this.targetLabelsPosition = -1;
/*  617 */     this.rangeAdjusterOn[0] = false;
/*  618 */     this.sampleScrollerOn = false;
/*  619 */     this.upperRange[0] = 100.0D;
/*  620 */     this.lowerRange[0] = 0.0D;
/*  621 */     this.currentUpperRange[0] = 100.0D;
/*  622 */     this.currentLowerRange[0] = 0.0D;
/*  623 */     this.rangeAdjusterPosition = 1;
/*  624 */     this.adjusterIndex = -1;
/*  625 */     this.gridEdgeToAdjust = -1;
/*  626 */     this.rangePosition[0] = 0;
/*  627 */     this.rangeAdjusted[0] = 0;
/*  628 */     this.gridLines = null;
/*  629 */     this.defaultGridLines = null;
/*  630 */     this.defaultGridLinesColor = Color.lightGray;
/*  631 */     this.gridLineColors = null;
/*  632 */     this.leftSampleAxisRange = 0.0D;
/*  633 */     this.rightSampleAxisRange = 100.0D;
/*      */     
/*      */ 
/*  636 */     this.overlayCharts.removeAllElements();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void setChartData(ChartData chartData)
/*      */   {
/*  648 */     this.chartData = chartData;
/*  649 */     checkDataIntegrity();
/*  650 */     autoRepaint();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public ChartData getChartData()
/*      */   {
/*  658 */     return this.chartData;
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
/*      */   public void setSamples(int serie, ChartSample[] samples)
/*      */   {
/*  672 */     this.chartData.setSamples(serie, samples);
/*  673 */     this.needRender = true;
/*  674 */     this.needChartCalculation = true;
/*  675 */     if ((isValueLabelsOn(serie)) && (getLabelAngle("valueLabelAngle") != 0)) {
/*  676 */       angledLabelCache.clear();
/*      */     }
/*  678 */     this.labelSizeCache.clear();
/*  679 */     autoRepaint();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public ChartSample[] getSamples(int serie)
/*      */   {
/*  690 */     return this.chartData.getSamples(serie);
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
/*      */   public void setSample(int serie, int index, ChartSample sample)
/*      */   {
/*  703 */     this.chartData.setSample(serie, index, sample);
/*  704 */     this.needRender = true;
/*  705 */     this.needChartCalculation = true;
/*  706 */     if ((isValueLabelsOn(serie)) && (getLabelAngle("valueLabelAngle") != 0)) {
/*  707 */       angledLabelCache.clear();
/*      */     }
/*  709 */     autoRepaint();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public ChartSample getSample(int serie, int index)
/*      */   {
/*  721 */     return this.chartData.getSample(serie, index);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public ChartSample getSample(Object key)
/*      */   {
/*  731 */     return this.chartData.getSample(key);
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
/*      */   public void setSampleValues(int serie, double[] values)
/*      */   {
/*  748 */     this.chartData.setSampleValues(serie, values);
/*  749 */     this.needRender = true;
/*  750 */     this.needChartCalculation = true;
/*  751 */     if ((isValueLabelsOn(serie)) && (getLabelAngle("valueLabelAngle") != 0)) {
/*  752 */       angledLabelCache.clear();
/*      */     }
/*  754 */     this.labelSizeCache.clear();
/*  755 */     autoRepaint();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public double[] getSampleValues(int serie)
/*      */   {
/*  766 */     return this.chartData.getSampleValues(serie);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void setSampleValue(int serie, int index, double value)
/*      */   {
/*  778 */     this.chartData.setSampleValue(serie, index, value);
/*  779 */     this.needRender = true;
/*  780 */     this.needChartCalculation = true;
/*  781 */     if ((isValueLabelsOn(serie)) && (getLabelAngle("valueLabelAngle") != 0)) {
/*  782 */       angledLabelCache.clear();
/*      */     }
/*  784 */     autoRepaint();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public double getSampleValue(int serie, int index)
/*      */   {
/*  796 */     return this.chartData.getSampleValue(serie, index);
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
/*      */   public int appendSample(int serie, ChartSample sample, boolean makeSpace)
/*      */   {
/*  825 */     int index = this.chartData.appendSample(serie, sample, makeSpace);
/*  826 */     checkDataIntegrity();
/*  827 */     this.needRender = true;
/*  828 */     this.needChartCalculation = true;
/*  829 */     if ((isValueLabelsOn(serie)) && (getLabelAngle("valueLabelAngle") != 0)) {
/*  830 */       angledLabelCache.clear();
/*      */     }
/*  832 */     autoRepaint();
/*  833 */     return index;
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
/*      */   public int appendSampleValue(int serie, double value, boolean makeSpace)
/*      */   {
/*  859 */     int sample_count = getSampleCount();
/*  860 */     int index = this.chartData.appendSampleValue(serie, value, makeSpace);
/*      */     
/*      */ 
/*  863 */     if (index >= this.visibleSamples[0] + this.visibleSamples[1]) {
/*  864 */       this.visibleSamples[1] = (index - this.visibleSamples[0] + 1);
/*      */     }
/*      */     
/*      */ 
/*  868 */     if ((makeSpace) && (index >= sample_count)) {
/*  869 */       checkDataIntegrity();
/*      */     }
/*      */     
/*      */ 
/*  873 */     this.needRender = true;
/*  874 */     this.needChartCalculation = true;
/*  875 */     if ((isValueLabelsOn(serie)) && (getLabelAngle("valueLabelAngle") != 0)) {
/*  876 */       angledLabelCache.clear();
/*      */     }
/*  878 */     autoRepaint();
/*  879 */     return index;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void setSampleLabels(String[] labels)
/*      */   {
/*  891 */     this.chartData.setSampleLabels(labels);
/*  892 */     this.needRender = true;
/*  893 */     this.needGraphBounds = true;
/*  894 */     this.needChartCalculation = true;
/*  895 */     if ((this.sampleLabelsOn) && ((getLabelAngle("sampleLabelAngle") != 0) || (getLabelAngle("barLabelAngle") != 0))) {
/*  896 */       angledLabelCache.clear();
/*      */     }
/*  898 */     this.labelSizeCache.clear();
/*  899 */     autoRepaint();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public String[] getSampleLabels()
/*      */   {
/*  909 */     return this.chartData.getSampleLabels();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void setSampleLabel(int index, String label)
/*      */   {
/*  920 */     String oldLabel = getSampleLabel(index);
/*  921 */     this.chartData.setSampleLabel(index, label);
/*  922 */     this.needRender = true;
/*  923 */     this.needGraphBounds = true;
/*  924 */     this.needChartCalculation = true;
/*      */     
/*  926 */     if ((this.sampleLabelsOn) && ((getLabelAngle("sampleLabelAngle") != 0) || (getLabelAngle("barLabelAngle") != 0))) {
/*  927 */       angledLabelCache.clear();
/*      */     }
/*      */     
/*  930 */     if (oldLabel != null) {
/*  931 */       Long key = new Long(oldLabel.hashCode() + getFont("sampleLabelFont").hashCode());
/*  932 */       this.labelSizeCache.remove(key);
/*  933 */       key = new Long(oldLabel.hashCode() + getFont("barLabelFont").hashCode());
/*  934 */       this.labelSizeCache.remove(key);
/*      */     }
/*  936 */     autoRepaint();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public String getSampleLabel(int index)
/*      */   {
/*  947 */     return this.chartData.getSampleLabel(index);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void setSampleLabelColor(int index, Color color)
/*      */   {
/*  958 */     if (index >= 0) {
/*  959 */       if (index >= this.sampleLabelColors.length) {
/*  960 */         Color[] newSampleLabelColors = new Color[index + 1];
/*  961 */         System.arraycopy(this.sampleLabelColors, 0, newSampleLabelColors, 0, this.sampleLabelColors.length);
/*  962 */         this.sampleLabelColors = newSampleLabelColors;
/*      */       }
/*  964 */       this.sampleLabelColors[index] = color;
/*  965 */       autoRepaint();
/*      */     } else {
/*  967 */       throw new IllegalArgumentException("Invalid sample index: " + index);
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public Color getSampleLabelColor(int index)
/*      */   {
/*  978 */     if ((this.sampleLabelColors != null) && (index >= 0) && (index < this.sampleLabelColors.length)) {
/*  979 */       return this.sampleLabelColors[index];
/*      */     }
/*  981 */     return null;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void setSampleLabelSelectionColor(Color color)
/*      */   {
/*  991 */     if (color != this.sampleLabelSelectionColor) {
/*  992 */       this.sampleLabelSelectionColor = color;
/*  993 */       this.needRender = true;
/*  994 */       autoRepaint();
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public Color getSampleLabelSelectionColor()
/*      */   {
/* 1004 */     return this.sampleLabelSelectionColor;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void setSampleLabelsOn(boolean on)
/*      */   {
/* 1015 */     if (this.sampleLabelsOn != on) {
/* 1016 */       this.sampleLabelsOn = on;
/* 1017 */       this.needRender = true;
/* 1018 */       this.needGraphBounds = true;
/* 1019 */       this.needChartCalculation = true;
/* 1020 */       autoRepaint();
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public boolean isSampleLabelsOn()
/*      */   {
/* 1030 */     return this.sampleLabelsOn;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void setSampleLabelStyle(int style)
/*      */   {
/* 1042 */     if ((style == 5) && (this.chartType != "pie")) {
/* 1043 */       style = 1;
/*      */     }
/* 1045 */     if (this.sampleLabelStyle != style) {
/* 1046 */       this.sampleLabelStyle = style;
/* 1047 */       this.needRender = true;
/* 1048 */       this.needGraphBounds = true;
/* 1049 */       this.needChartCalculation = true;
/* 1050 */       autoRepaint();
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public int getSampleLabelStyle()
/*      */   {
/* 1059 */     return this.sampleLabelStyle;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void setSeriesLabels(String[] labels)
/*      */   {
/* 1068 */     this.chartData.setSeriesLabels(labels);
/* 1069 */     this.needRender = true;
/* 1070 */     this.needGraphBounds = true;
/* 1071 */     this.needChartCalculation = true;
/* 1072 */     autoRepaint();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public String[] getSeriesLabels()
/*      */   {
/* 1082 */     return this.chartData.getSeriesLabels();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void setSeriesLabel(int serie, String label)
/*      */   {
/* 1093 */     this.chartData.setSeriesLabel(serie, label);
/* 1094 */     this.needRender = true;
/* 1095 */     this.needGraphBounds = true;
/* 1096 */     this.needChartCalculation = true;
/* 1097 */     autoRepaint();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public String getSeriesLabel(int serie)
/*      */   {
/* 1108 */     return this.chartData.getSeriesLabel(serie);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void setSeriesLabelColor(int serie, Color color)
/*      */   {
/*      */     try
/*      */     {
/* 1120 */       if (this.seriesLabelColors[serie] != color) {
/* 1121 */         this.seriesLabelColors[serie] = color;
/* 1122 */         this.needRender = true;
/* 1123 */         autoRepaint();
/*      */       }
/*      */     } catch (IndexOutOfBoundsException e) {
/* 1126 */       throw new IllegalArgumentException("Invalid series index: " + serie);
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public Color getSeriesLabelColor(int serie)
/*      */   {
/*      */     try
/*      */     {
/* 1139 */       return this.seriesLabelColors[serie];
/*      */     } catch (IndexOutOfBoundsException e) {
/* 1141 */       throw new IllegalArgumentException("Invalid series index: " + serie);
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void setSeriesLabelsOn(boolean on)
/*      */   {
/* 1151 */     if (this.seriesLabelsOn != on) {
/* 1152 */       this.seriesLabelsOn = on;
/* 1153 */       this.needRender = true;
/* 1154 */       this.needGraphBounds = true;
/* 1155 */       this.needChartCalculation = true;
/* 1156 */       autoRepaint();
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public boolean isSeriesLabelsOn()
/*      */   {
/* 1166 */     return this.seriesLabelsOn;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void setSeriesLabelStyle(int style)
/*      */   {
/* 1177 */     if ((style == 5) && (this.chartType != "pie")) {
/* 1178 */       style = 1;
/*      */     }
/* 1180 */     if (this.seriesLabelStyle != style) {
/* 1181 */       this.seriesLabelStyle = style;
/* 1182 */       this.needRender = true;
/* 1183 */       this.needGraphBounds = true;
/* 1184 */       this.needChartCalculation = true;
/* 1185 */       autoRepaint();
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public int getSeriesLabelStyle()
/*      */   {
/* 1194 */     return this.seriesLabelStyle;
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
/*      */   public void setLegendLabels(String[] labels)
/*      */   {
/* 1207 */     this.legendLabels = labels;
/* 1208 */     checkDataIntegrity();
/* 1209 */     this.labelSizeCache.clear();
/* 1210 */     this.needRender = true;
/* 1211 */     this.needGraphBounds = true;
/* 1212 */     this.needChartCalculation = true;
/* 1213 */     autoRepaint();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public String[] getLegendLabels()
/*      */   {
/* 1224 */     if (this.legendLabels != null)
/* 1225 */       return this.legendLabels;
/* 1226 */     if (getSeriesCount() == 1) {
/* 1227 */       return getSampleLabels();
/*      */     }
/* 1229 */     return getSeriesLabels();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void setLabel(String name, String label)
/*      */   {
/* 1240 */     if ((name != null) && (name.length() > 0)) {
/* 1241 */       name = name.toLowerCase().trim();
/* 1242 */       if ((label != null) && (label.length() > 0)) {
/* 1243 */         this.labels.put(name, label);
/*      */       } else {
/* 1245 */         this.labels.remove(name);
/*      */       }
/* 1247 */       this.needRender = true;
/* 1248 */       this.needGraphBounds = true;
/* 1249 */       this.needChartCalculation = true;
/* 1250 */       if (getLabelAngle(name + "Angle") != 0) {
/* 1251 */         angledLabelCache.clear();
/*      */       }
/* 1253 */       autoRepaint();
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public String getLabel(String name)
/*      */   {
/* 1265 */     if (name != null) {
/* 1266 */       name = name.toLowerCase().trim();
/* 1267 */       return (String)this.labels.get(name);
/*      */     }
/* 1269 */     return null;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void setLabelAngle(String name, int angle)
/*      */   {
/* 1279 */     if ((name != null) && (name.length() > 0)) {
/* 1280 */       angle %= 360;
/* 1281 */       angle = angle < 0 ? angle + 360 : angle;
/* 1282 */       name = name.toLowerCase().trim();
/* 1283 */       this.labelAngles.put(name, new Integer(angle));
/* 1284 */       angledLabelCache.clear();
/* 1285 */       this.needRender = true;
/* 1286 */       this.needGraphBounds = true;
/* 1287 */       this.needChartCalculation = true;
/* 1288 */       autoRepaint();
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public int getLabelAngle(String name)
/*      */   {
/* 1300 */     if (name != null) {
/* 1301 */       name = name.toLowerCase().trim();
/* 1302 */       if (this.labelAngles.get(name) != null) {
/* 1303 */         return ((Integer)this.labelAngles.get(name)).intValue();
/*      */       }
/*      */     }
/* 1306 */     return 0;
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
/*      */   public static void setLabelAngleCacheSize(int count)
/*      */   {
/* 1327 */     if (fifo_queue != null) synchronized (fifo_queue) {
/* 1328 */         if (count > 0) {
/* 1329 */           fifo_queue = new Object[count];
/* 1330 */           fifo_count = 0;
/* 1331 */           fifo_head = 0;
/* 1332 */           fifo_tail = 0;
/* 1333 */           angledLabelCache.clear();
/*      */         } else {
/* 1335 */           fifo_queue = null;
/* 1336 */           angledLabelCache.clear();
/*      */         }
/*      */       }
/* 1339 */     if (count > 0) {
/* 1340 */       fifo_queue = new Object[count];
/* 1341 */       fifo_count = 0;
/* 1342 */       fifo_head = 0;
/* 1343 */       fifo_tail = 0;
/* 1344 */       angledLabelCache.clear();
/*      */     } else {
/* 1346 */       angledLabelCache.clear();
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
/*      */   public void setRange(int index, double range)
/*      */   {
/* 1365 */     index = Math.max(0, index);
/* 1366 */     checkRangeIntegrity(this.rangeOn.length, index + 1);
/* 1367 */     this.upperRange[index] = range;
/* 1368 */     this.currentUpperRange[index] = Math.min(this.currentUpperRange[index], range);
/* 1369 */     if (this.rangeAdjusterOn[index] == 0) {
/* 1370 */       this.currentUpperRange[index] = range;
/*      */     }
/*      */     
/* 1373 */     for (int i = 0; i < this.overlayCharts.size(); i++) {
/* 1374 */       ((Chart)this.overlayCharts.elementAt(i)).setRange(index, range);
/*      */     }
/* 1376 */     this.needRender = true;
/* 1377 */     this.needChartCalculation = true;
/* 1378 */     autoRepaint();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public double getRange(int index)
/*      */   {
/* 1387 */     return this.upperRange[Math.min(this.upperRange.length - 1, Math.max(0, index))];
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
/*      */   public void setRelativeRange(double range)
/*      */   {
/* 1401 */     setRelativeRangeIndex(0, range);
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
/*      */   void setRelativeRangeIndex(int index, double range)
/*      */   {
/* 1416 */     index = Math.max(0, index);
/* 1417 */     checkRangeIntegrity(this.upperRange.length, index + 1);
/*      */     
/*      */ 
/* 1420 */     double max = getHighestValue(index);
/* 1421 */     double min = getLowestValue(index);
/* 1422 */     if (index == 0) {
/* 1423 */       for (Enumeration e = this.targetsValue.elements(); e.hasMoreElements();) {
/* 1424 */         max = Math.max(((Double)e.nextElement()).doubleValue(), max);
/*      */       }
/*      */     }
/* 1427 */     if (max > 0.0D) {
/* 1428 */       setRange(index, max * Math.abs(range));
/* 1429 */     } else if ((max == 0.0D) && (min == 0.0D)) {
/* 1430 */       setRange(index, 100.0D);
/*      */     } else {
/* 1432 */       setRange(index, 0.0D);
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
/*      */   public void setRelativeRange(double range, double step)
/*      */   {
/* 1449 */     setRelativeRange(0, range, step);
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
/*      */   public void setRelativeRange(int index, double range, double step)
/*      */   {
/* 1466 */     index = Math.max(0, index);
/* 1467 */     checkRangeIntegrity(this.upperRange.length, index + 1);
/*      */     
/* 1469 */     double max = getHighestValue(index);
/* 1470 */     double min = getLowestValue(index);
/*      */     
/* 1472 */     if (index == 0) {
/* 1473 */       for (Enumeration e = this.targetsValue.elements(); e.hasMoreElements();) {
/* 1474 */         max = Math.max(((Double)e.nextElement()).doubleValue(), max);
/*      */       }
/*      */     }
/* 1477 */     if (max > 0.0D)
/*      */     {
/* 1479 */       step = Math.abs(step);
/* 1480 */       double new_range = max * Math.abs(range);
/* 1481 */       if (step % 1.0D == 0.0D) {
/* 1482 */         if (new_range % step == 0.0D) {
/* 1483 */           setRange(index, new_range);
/*      */         } else {
/* 1485 */           setRange(index, new_range - new_range % step + step);
/*      */         }
/*      */       } else {
/* 1488 */         double value = 0.0D;
/* 1489 */         while (value < new_range) {
/* 1490 */           value += step;
/*      */         }
/* 1492 */         setRange(index, value);
/*      */       }
/* 1494 */     } else if ((max == 0.0D) && (min == 0.0D)) {
/* 1495 */       setRange(index, 100.0D);
/*      */     } else {
/* 1497 */       setRange(index, 0.0D);
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
/*      */   public void setLowerRange(int index, double range)
/*      */   {
/* 1510 */     index = Math.max(0, index);
/* 1511 */     checkRangeIntegrity(this.lowerRange.length, index + 1);
/* 1512 */     this.lowerRange[index] = range;
/* 1513 */     this.currentLowerRange[index] = Math.max(this.currentLowerRange[index], range);
/* 1514 */     if (this.rangeAdjusterOn[index] == 0) {
/* 1515 */       this.currentLowerRange[index] = range;
/*      */     }
/*      */     
/* 1518 */     for (int i = 0; i < this.overlayCharts.size(); i++) {
/* 1519 */       ((Chart)this.overlayCharts.elementAt(i)).setLowerRange(index, range);
/*      */     }
/* 1521 */     this.needRender = true;
/* 1522 */     this.needChartCalculation = true;
/* 1523 */     autoRepaint();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public double getLowerRange(int index)
/*      */   {
/* 1532 */     return this.lowerRange[Math.min(this.lowerRange.length - 1, Math.max(0, index))];
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
/*      */   public void setLowerRelativeRange(double range)
/*      */   {
/* 1545 */     setLowerRelativeRangeIndex(0, range);
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
/*      */   void setLowerRelativeRangeIndex(int index, double range)
/*      */   {
/* 1559 */     index = Math.max(0, index);
/* 1560 */     checkRangeIntegrity(this.lowerRange.length, index + 1);
/* 1561 */     double min = getLowestValue(index);
/* 1562 */     if (index == 0) {
/* 1563 */       for (Enumeration e = this.targetsValue.elements(); e.hasMoreElements();) {
/* 1564 */         min = Math.min(((Double)e.nextElement()).doubleValue(), min);
/*      */       }
/*      */     }
/* 1567 */     if (min < 0.0D) {
/* 1568 */       setLowerRange(index, min * Math.abs(range));
/*      */     } else {
/* 1570 */       setLowerRange(index, 0.0D);
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
/*      */   public void setLowerRelativeRange(double range, double step)
/*      */   {
/* 1587 */     setLowerRelativeRange(0, range, step);
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
/*      */   public void setLowerRelativeRange(int index, double range, double step)
/*      */   {
/* 1604 */     index = Math.max(0, index);
/* 1605 */     checkRangeIntegrity(this.lowerRange.length, index + 1);
/* 1606 */     double min = getLowestValue(index);
/* 1607 */     if (index == 0) {
/* 1608 */       for (Enumeration e = this.targetsValue.elements(); e.hasMoreElements();) {
/* 1609 */         min = Math.min(((Double)e.nextElement()).doubleValue(), min);
/*      */       }
/*      */     }
/* 1612 */     if (min < 0.0D)
/*      */     {
/* 1614 */       step = Math.abs(step);
/* 1615 */       double new_range = min * Math.abs(range);
/* 1616 */       if (step % 1.0D == 0.0D) {
/* 1617 */         if (new_range % step == 0.0D) {
/* 1618 */           setLowerRange(index, new_range);
/*      */         } else {
/* 1620 */           setLowerRange(index, new_range - new_range % step - step);
/*      */         }
/*      */       } else {
/* 1623 */         double value = 0.0D;
/* 1624 */         while (value > new_range) {
/* 1625 */           value -= step;
/*      */         }
/* 1627 */         setLowerRange(index, value);
/*      */       }
/*      */     } else {
/* 1630 */       setLowerRange(index, 0.0D);
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void setCurrentRange(int range, double upper)
/*      */   {
/* 1642 */     checkRangeIntegrity(this.currentUpperRange.length, range + 1);
/* 1643 */     if (this.upperRange[range] > this.lowerRange[range]) {
/* 1644 */       upper = Math.min(this.upperRange[range], upper);
/* 1645 */       upper = Math.max(this.currentLowerRange[range], upper);
/*      */     } else {
/* 1647 */       upper = Math.max(this.upperRange[range], upper);
/* 1648 */       upper = Math.min(this.currentLowerRange[range], upper);
/*      */     }
/* 1650 */     this.currentUpperRange[range] = upper;
/* 1651 */     this.needRender = true;
/* 1652 */     this.needChartCalculation = true;
/* 1653 */     autoRepaint();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public double getCurrentRange(int range)
/*      */   {
/* 1663 */     range = Math.min(this.currentUpperRange.length - 1, Math.max(0, range));
/* 1664 */     return this.currentUpperRange[range];
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void setCurrentLowerRange(int range, double lower)
/*      */   {
/* 1675 */     range = Math.max(0, range);
/* 1676 */     checkRangeIntegrity(this.currentLowerRange.length, range + 1);
/* 1677 */     if (this.upperRange[range] > this.lowerRange[range]) {
/* 1678 */       lower = Math.max(this.lowerRange[range], lower);
/* 1679 */       lower = Math.min(this.currentUpperRange[range], lower);
/*      */     } else {
/* 1681 */       lower = Math.min(this.lowerRange[range], lower);
/* 1682 */       lower = Math.max(this.currentUpperRange[range], lower);
/*      */     }
/* 1684 */     this.currentLowerRange[range] = lower;
/* 1685 */     this.needRender = true;
/* 1686 */     this.needChartCalculation = true;
/* 1687 */     autoRepaint();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public double getCurrentLowerRange(int range)
/*      */   {
/* 1697 */     range = Math.min(this.currentLowerRange.length - 1, Math.max(0, range));
/* 1698 */     return this.currentLowerRange[range];
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void setRangeOn(int index, boolean on)
/*      */   {
/* 1708 */     index = Math.max(0, index);
/* 1709 */     checkRangeIntegrity(this.rangeOn.length, index + 1);
/* 1710 */     this.rangeOn[index] = on;
/* 1711 */     this.needRender = true;
/* 1712 */     this.needGraphBounds = true;
/* 1713 */     this.needChartCalculation = true;
/* 1714 */     autoRepaint();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public boolean isRangeOn(int index)
/*      */   {
/* 1724 */     if (index < this.rangeOn.length) {
/* 1725 */       return this.rangeOn[Math.max(0, index)];
/*      */     }
/* 1727 */     return false;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void setRangePosition(int index, int position)
/*      */   {
/* 1738 */     index = Math.max(0, index);
/* 1739 */     checkRangeIntegrity(this.rangePosition.length, index + 1);
/* 1740 */     this.rangePosition[index] = Math.min(1, Math.max(0, position));
/* 1741 */     this.needRender = true;
/* 1742 */     this.needGraphBounds = true;
/* 1743 */     this.needChartCalculation = true;
/* 1744 */     autoRepaint();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public int getRangePosition(int index)
/*      */   {
/* 1754 */     return this.rangePosition[Math.min(this.rangePosition.length - 1, Math.max(0, index))];
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void setRangeColor(int index, Color color)
/*      */   {
/* 1764 */     index = Math.max(0, index);
/* 1765 */     checkRangeIntegrity(this.rangeColor.length, index + 1);
/* 1766 */     this.rangeColor[index] = (color != null ? color : this.chartForeground);
/* 1767 */     this.needRender = true;
/* 1768 */     autoRepaint();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public Color getRangeColor(int index)
/*      */   {
/* 1777 */     index = Math.min(this.rangeColor.length - 1, Math.max(0, index));
/* 1778 */     return this.rangeColor[index] != null ? this.rangeColor[index] : this.chartForeground;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void setRangeDecimalCount(int index, int count)
/*      */   {
/* 1788 */     index = Math.max(0, index);
/* 1789 */     checkRangeIntegrity(this.rangeDecimalCount.length, index + 1);
/* 1790 */     this.rangeDecimalCount[index] = count;
/* 1791 */     this.needRender = true;
/* 1792 */     this.needGraphBounds = true;
/* 1793 */     this.needChartCalculation = true;
/* 1794 */     autoRepaint();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public int getRangeDecimalCount(int index)
/*      */   {
/* 1803 */     index = Math.max(0, Math.min(this.rangeDecimalCount.length - 1, index));
/* 1804 */     return this.rangeDecimalCount[index];
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void setSeriesRange(int series, int range)
/*      */   {
/* 1814 */     if ((series >= 0) && (series < getSeriesCount())) {
/* 1815 */       this.seriesRange[series] = Math.max(0, range);
/*      */     }
/* 1817 */     this.needRender = true;
/* 1818 */     this.needChartCalculation = true;
/* 1819 */     autoRepaint();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public int getSeriesRange(int series)
/*      */   {
/* 1829 */     if ((series >= 0) && (series < getSeriesCount()) && (series < this.seriesRange.length)) {
/* 1830 */       return this.seriesRange[series] < this.rangeOn.length ? this.seriesRange[series] : this.rangeOn.length - 1;
/*      */     }
/* 1832 */     return 0;
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
/*      */   public void setValueLinesOn(boolean on)
/*      */   {
/* 1846 */     if (on != this.valueLinesOn) {
/* 1847 */       this.valueLinesOn = on;
/* 1848 */       this.needRender = true;
/* 1849 */       autoRepaint();
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public boolean isValueLinesOn()
/*      */   {
/* 1859 */     return this.valueLinesOn;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void setMaxValueLineCount(int count)
/*      */   {
/* 1869 */     if (count != this.maxValueLineCount) {
/* 1870 */       if (count > -1) {
/* 1871 */         this.maxValueLineCount = count;
/*      */       } else {
/* 1873 */         this.maxValueLineCount = Integer.MAX_VALUE;
/*      */       }
/* 1875 */       this.needRender = true;
/* 1876 */       autoRepaint();
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public int getMaxValueLineCount()
/*      */   {
/* 1887 */     if (this.maxValueLineCount != Integer.MAX_VALUE) {
/* 1888 */       return this.maxValueLineCount;
/*      */     }
/* 1890 */     return -1;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void setThousandsDelimiter(String delimiter)
/*      */   {
/* 1902 */     this.thousandsDelimiter = delimiter;
/* 1903 */     if ((this.thousandsDelimiter != null) && (this.thousandsDelimiter.length() > 1) && (!this.thousandsDelimiter.trim().equalsIgnoreCase("off"))) {
/* 1904 */       this.thousandsDelimiter = this.thousandsDelimiter.substring(0, 1);
/* 1905 */     } else if ((this.thousandsDelimiter != null) && (!this.thousandsDelimiter.equals(" "))) {
/* 1906 */       this.thousandsDelimiter = this.thousandsDelimiter.trim().toLowerCase();
/*      */     }
/* 1908 */     this.needRender = true;
/* 1909 */     autoRepaint();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public String getThousandsDelimeter()
/*      */   {
/* 1918 */     return this.thousandsDelimiter;
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
/*      */   public void setDefaultGridLinesOn(boolean on)
/*      */   {
/* 1931 */     setDefaultGridLinesOn(on, -1.0D, -1.0D);
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
/*      */   public void setDefaultGridLinesOn(boolean on, double start, double step)
/*      */   {
/* 1947 */     this.defaultGridLines = null;
/* 1948 */     if (on) {
/* 1949 */       if ((start <= 0.0D) && (step > 0.0D)) {
/* 1950 */         start = step;
/*      */       }
/*      */       
/*      */ 
/*      */ 
/* 1955 */       if (step <= 0.0D) {
/* 1956 */         int lines_number = 0;
/* 1957 */         if (this.chartType.equals("bar")) {
/* 1958 */           lines_number = getSampleCount() - 1;
/* 1959 */         } else if (this.chartType.equals("line")) {
/* 1960 */           lines_number = getSampleCount() - 2;
/*      */         }
/* 1962 */         if (lines_number >= 0) {
/* 1963 */           double grid_step = (this.rightSampleAxisRange - this.leftSampleAxisRange) / (lines_number + 1);
/* 1964 */           this.defaultGridLines = new double[lines_number];
/* 1965 */           for (int i = 0; i < this.defaultGridLines.length; i++) {
/* 1966 */             this.defaultGridLines[i] = (this.leftSampleAxisRange + grid_step * (i + 1));
/*      */           }
/*      */         }
/*      */       }
/*      */       else {
/* 1971 */         int lines_number = (int)Math.ceil((this.rightSampleAxisRange - start) / step);
/* 1972 */         this.defaultGridLines = new double[lines_number];
/* 1973 */         for (int i = 0; i < this.defaultGridLines.length; i++) {
/* 1974 */           this.defaultGridLines[i] = (start + step * i);
/*      */         }
/*      */       }
/*      */     }
/* 1978 */     this.needRender = true;
/* 1979 */     autoRepaint();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public boolean isDefaultGridLinesOn()
/*      */   {
/* 1988 */     return this.defaultGridLines != null;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void setGridLines(double[] grid)
/*      */   {
/* 1999 */     this.gridLines = grid;
/* 2000 */     this.needRender = true;
/* 2001 */     autoRepaint();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public double[] getGridLines()
/*      */   {
/* 2010 */     return this.gridLines;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void setGridLine(int index, double value)
/*      */   {
/* 2021 */     if ((index >= this.maxGridLineCount) || (index < 0)) {
/* 2022 */       throw new IllegalArgumentException("Illegal vertical grid line index: " + index);
/*      */     }
/*      */     
/* 2025 */     if (this.gridLines == null) {
/* 2026 */       this.gridLines = new double[index + 1];
/* 2027 */       for (int i = 0; i < this.gridLines.length; i++) {
/* 2028 */         this.gridLines[i] = -2.147483648E9D;
/*      */       }
/*      */     }
/*      */     
/* 2032 */     if (index >= this.gridLines.length) {
/* 2033 */       double[] tmp_arr = new double[index + 1];
/* 2034 */       System.arraycopy(this.gridLines, 0, tmp_arr, 0, this.gridLines.length);
/* 2035 */       for (int i = this.gridLines.length; i < tmp_arr.length - 1; i++) {
/* 2036 */         tmp_arr[i] = -2.147483648E9D;
/*      */       }
/* 2038 */       this.gridLines = tmp_arr;
/*      */     }
/*      */     
/* 2041 */     this.gridLines[index] = value;
/* 2042 */     this.needRender = true;
/* 2043 */     autoRepaint();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public double getGridLine(int index)
/*      */   {
/* 2054 */     if ((this.gridLines == null) || (index >= this.gridLines.length)) {
/* 2055 */       return -2.147483648E9D;
/*      */     }
/* 2057 */     return this.gridLines[index];
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
/*      */   public void setSampleAxisRange(double min, double max)
/*      */   {
/* 2073 */     if (min >= max) {
/* 2074 */       throw new IllegalArgumentException("Min is more or equal than max: " + min + ", " + max);
/*      */     }
/* 2076 */     this.leftSampleAxisRange = min;
/* 2077 */     this.rightSampleAxisRange = max;
/* 2078 */     this.needRender = true;
/* 2079 */     autoRepaint();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void setDefaultGridLinesColor(Color color)
/*      */   {
/* 2088 */     this.defaultGridLinesColor = color;
/* 2089 */     if (this.defaultGridLinesColor == null) {
/* 2090 */       this.defaultGridLinesColor = Color.lightGray;
/*      */     }
/* 2092 */     this.needRender = true;
/* 2093 */     autoRepaint();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void setGridLinesColor(Color color)
/*      */   {
/* 2103 */     if (this.gridLines != null) {
/* 2104 */       Color[] colors = new Color[this.gridLines.length];
/* 2105 */       for (int i = 0; i < this.gridLines.length; i++) {
/* 2106 */         colors[i] = color;
/*      */       }
/* 2108 */       setGridLineColors(colors);
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void setGridLineColors(Color[] colors)
/*      */   {
/* 2118 */     this.gridLineColors = colors;
/* 2119 */     this.needRender = true;
/* 2120 */     autoRepaint();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public Color[] getGridLineColors()
/*      */   {
/* 2130 */     return this.gridLineColors;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void setGridLineColor(int index, Color color)
/*      */   {
/* 2142 */     if ((index >= this.maxGridLineCount) || (index < 0)) {
/* 2143 */       throw new IllegalArgumentException("Illegal vertical grid line index: " + index);
/*      */     }
/*      */     
/* 2146 */     if (this.gridLineColors == null) {
/* 2147 */       this.gridLineColors = new Color[index + 1];
/*      */     }
/*      */     
/* 2150 */     if (index >= this.gridLineColors.length) {
/* 2151 */       Color[] tmp_arr = new Color[index + 1];
/* 2152 */       System.arraycopy(this.gridLineColors, 0, tmp_arr, 0, this.gridLineColors.length);
/* 2153 */       this.gridLineColors = tmp_arr;
/*      */     }
/*      */     
/* 2156 */     this.gridLineColors[index] = color;
/* 2157 */     this.needRender = true;
/* 2158 */     autoRepaint();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public Color getGridLineColor(int index)
/*      */   {
/* 2169 */     if ((this.gridLineColors == null) || (index >= this.gridLineColors.length)) {
/* 2170 */       return getValueLinesColor();
/*      */     }
/* 2172 */     return this.gridLineColors[index];
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void setValueLinesColor(Color color)
/*      */   {
/* 2182 */     this.valueLinesColor = color;
/* 2183 */     if (this.valueLinesColor == null) {
/* 2184 */       this.valueLinesColor = Color.lightGray;
/*      */     }
/* 2186 */     this.needRender = true;
/* 2187 */     autoRepaint();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public Color getValueLinesColor()
/*      */   {
/* 2195 */     return this.valueLinesColor;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void setRangeLabelsOn(int index, boolean on)
/*      */   {
/* 2207 */     index = Math.max(-1, index);
/* 2208 */     if (index == -1) {
/* 2209 */       for (int i = 0; i < this.rangeLabelsOn.length; i++) {
/* 2210 */         this.rangeLabelsOn[index] = on;
/*      */       }
/*      */     } else {
/* 2213 */       checkRangeIntegrity(this.rangeLabelsOn.length, index + 1);
/* 2214 */       this.rangeLabelsOn[index] = on;
/*      */     }
/*      */     
/* 2217 */     this.needRender = true;
/* 2218 */     this.needGraphBounds = true;
/* 2219 */     this.needChartCalculation = true;
/* 2220 */     autoRepaint();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public boolean isRangeLabelsOn(int index)
/*      */   {
/* 2230 */     index = Math.min(this.rangeLabelsOn.length - 1, Math.max(0, index));
/* 2231 */     return this.rangeLabelsOn[index];
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
/*      */   public void setTargetValueLine(String id, double value, Color color, int style)
/*      */   {
/* 2246 */     if (color == null) {
/* 2247 */       this.targetsLabel.remove(id);
/* 2248 */       this.targetsValue.remove(id);
/* 2249 */       this.targetsColor.remove(id);
/* 2250 */       this.targetsStyle.remove(id);
/*      */ 
/*      */     }
/*      */     else
/*      */     {
/* 2255 */       this.targetsLabel.put(id, id);
/* 2256 */       this.targetsValue.put(id, new Double(value));
/* 2257 */       this.targetsColor.put(id, color);
/* 2258 */       this.targetsStyle.put(id, new Integer(style));
/*      */     }
/*      */     
/*      */ 
/* 2262 */     this.needRender = true;
/* 2263 */     this.needGraphBounds = true;
/* 2264 */     this.needChartCalculation = true;
/* 2265 */     autoRepaint();
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
/*      */   public void setLabel(String id, String text, int x, int y)
/*      */   {
/* 2279 */     if (text == null) {
/* 2280 */       String[] labelIDs_new = new String[Math.max(this.labelIDs.length - 1, 0)];
/* 2281 */       for (int i = 0; i < this.labelIDs.length; i++) {
/* 2282 */         if (id.equals(this.labelIDs[i])) {
/* 2283 */           System.arraycopy(this.labelIDs, 0, labelIDs_new, 0, i);
/* 2284 */           System.arraycopy(this.labelIDs, i + 1, labelIDs_new, i, this.labelIDs.length - i - 1);
/* 2285 */           break;
/*      */         }
/*      */       }
/* 2288 */       this.labelIDs = labelIDs_new;
/* 2289 */       this.labelTexts.remove(id);
/* 2290 */       this.labelXs.remove(id);
/* 2291 */       this.labelYs.remove(id);
/*      */     }
/*      */     else
/*      */     {
/* 2295 */       if (!this.labelTexts.containsKey(id)) {
/* 2296 */         String[] new_labelIDs = new String[this.labelIDs.length + 1];
/* 2297 */         System.arraycopy(this.labelIDs, 0, new_labelIDs, 0, this.labelIDs.length);
/* 2298 */         this.labelIDs = new_labelIDs;
/* 2299 */         this.labelIDs[(this.labelIDs.length - 1)] = id;
/*      */       }
/* 2301 */       this.labelTexts.put(id, text);
/* 2302 */       this.labelXs.put(id, new Double(x));
/* 2303 */       this.labelYs.put(id, new Double(y));
/*      */     }
/* 2305 */     this.labelSamples.remove(id);
/* 2306 */     this.labelSeries.remove(id);
/*      */     
/*      */ 
/* 2309 */     autoRepaint();
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
/*      */   public void setLabel(String id, String text, double x, double y)
/*      */   {
/* 2323 */     if (text == null) {
/* 2324 */       String[] labelIDs_new = new String[Math.max(this.labelIDs.length - 1, 0)];
/* 2325 */       for (int i = 0; i < this.labelIDs.length; i++) {
/* 2326 */         if (id.equals(this.labelIDs[i])) {
/* 2327 */           System.arraycopy(this.labelIDs, 0, labelIDs_new, 0, i);
/* 2328 */           System.arraycopy(this.labelIDs, i + 1, labelIDs_new, i, this.labelIDs.length - i - 1);
/* 2329 */           break;
/*      */         }
/*      */       }
/* 2332 */       this.labelIDs = labelIDs_new;
/* 2333 */       this.labelTexts.remove(id);
/* 2334 */       this.labelXs.remove(id);
/* 2335 */       this.labelYs.remove(id);
/*      */     }
/*      */     else
/*      */     {
/* 2339 */       if (!this.labelTexts.containsKey(id)) {
/* 2340 */         String[] new_labelIDs = new String[this.labelIDs.length + 1];
/* 2341 */         System.arraycopy(this.labelIDs, 0, new_labelIDs, 0, this.labelIDs.length);
/* 2342 */         this.labelIDs = new_labelIDs;
/* 2343 */         this.labelIDs[(this.labelIDs.length - 1)] = id;
/*      */       }
/* 2345 */       this.labelTexts.put(id, text);
/* 2346 */       this.labelXs.put(id, new Double(x));
/* 2347 */       this.labelYs.put(id, new Double(y));
/*      */     }
/* 2349 */     this.labelSamples.remove(id);
/* 2350 */     this.labelSeries.remove(id);
/*      */     
/*      */ 
/* 2353 */     autoRepaint();
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
/*      */   public void setLabel(String id, String text, int x, int y, int serie, int sample)
/*      */   {
/* 2369 */     if ((sample >= getSampleCount()) || (serie >= getSeriesCount())) {
/* 2370 */       return;
/*      */     }
/*      */     
/*      */ 
/* 2374 */     if (text == null) {
/* 2375 */       String[] labelIDs_new = new String[Math.max(this.labelIDs.length - 1, 0)];
/* 2376 */       for (int i = 0; i < this.labelIDs.length; i++) {
/* 2377 */         if (id.equals(this.labelIDs[i])) {
/* 2378 */           System.arraycopy(this.labelIDs, 0, labelIDs_new, 0, i);
/* 2379 */           System.arraycopy(this.labelIDs, i + 1, labelIDs_new, i, this.labelIDs.length - i - 1);
/* 2380 */           break;
/*      */         }
/*      */       }
/* 2383 */       this.labelIDs = labelIDs_new;
/* 2384 */       this.labelTexts.remove(id);
/* 2385 */       this.labelXs.remove(id);
/* 2386 */       this.labelYs.remove(id);
/* 2387 */       this.labelSamples.remove(id);
/* 2388 */       this.labelSeries.remove(id);
/*      */     }
/*      */     else
/*      */     {
/* 2392 */       if (!this.labelTexts.containsKey(id)) {
/* 2393 */         String[] new_labelIDs = new String[this.labelIDs.length + 1];
/* 2394 */         System.arraycopy(this.labelIDs, 0, new_labelIDs, 0, this.labelIDs.length);
/* 2395 */         this.labelIDs = new_labelIDs;
/* 2396 */         this.labelIDs[(this.labelIDs.length - 1)] = id;
/*      */       }
/* 2398 */       this.labelTexts.put(id, text);
/* 2399 */       this.labelXs.put(id, new Double(x));
/* 2400 */       this.labelYs.put(id, new Double(y));
/* 2401 */       this.labelSamples.put(id, new Integer(sample));
/* 2402 */       this.labelSeries.put(id, new Integer(serie));
/*      */     }
/*      */     
/*      */ 
/* 2406 */     autoRepaint();
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
/*      */   public void setLabel(String id, String text, double x, double y, int serie, int sample)
/*      */   {
/* 2422 */     if ((sample >= getSampleCount()) || (serie >= getSeriesCount())) {
/* 2423 */       return;
/*      */     }
/*      */     
/*      */ 
/* 2427 */     if (text == null) {
/* 2428 */       String[] labelIDs_new = new String[Math.max(this.labelIDs.length - 1, 0)];
/* 2429 */       for (int i = 0; i < this.labelIDs.length; i++) {
/* 2430 */         if (id.equals(this.labelIDs[i])) {
/* 2431 */           System.arraycopy(this.labelIDs, 0, labelIDs_new, 0, i);
/* 2432 */           System.arraycopy(this.labelIDs, i + 1, labelIDs_new, i, this.labelIDs.length - i - 1);
/* 2433 */           break;
/*      */         }
/*      */       }
/* 2436 */       this.labelIDs = labelIDs_new;
/* 2437 */       this.labelTexts.remove(id);
/* 2438 */       this.labelXs.remove(id);
/* 2439 */       this.labelYs.remove(id);
/* 2440 */       this.labelSamples.remove(id);
/* 2441 */       this.labelSeries.remove(id);
/*      */     }
/*      */     else
/*      */     {
/* 2445 */       if (!this.labelTexts.containsKey(id)) {
/* 2446 */         String[] new_labelIDs = new String[this.labelIDs.length + 1];
/* 2447 */         System.arraycopy(this.labelIDs, 0, new_labelIDs, 0, this.labelIDs.length);
/* 2448 */         this.labelIDs = new_labelIDs;
/* 2449 */         this.labelIDs[(this.labelIDs.length - 1)] = id;
/*      */       }
/* 2451 */       this.labelTexts.put(id, text);
/* 2452 */       this.labelXs.put(id, new Double(x));
/* 2453 */       this.labelYs.put(id, new Double(y));
/* 2454 */       this.labelSamples.put(id, new Integer(sample));
/* 2455 */       this.labelSeries.put(id, new Integer(serie));
/*      */     }
/*      */     
/*      */ 
/* 2459 */     autoRepaint();
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
/*      */   public void setLabelPosition(String id, int x, int y)
/*      */   {
/* 2472 */     String label_id = null;
/* 2473 */     for (int i = 0; i < this.labelIDs.length; i++) {
/* 2474 */       if (id.equals(this.labelIDs[i])) {
/* 2475 */         label_id = id;
/*      */       }
/*      */     }
/*      */     
/* 2479 */     if (label_id != null) {
/* 2480 */       this.labelXs.put(id, new Double(x));
/* 2481 */       this.labelYs.put(id, new Double(y));
/*      */     }
/*      */     
/*      */ 
/* 2485 */     autoRepaint();
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
/*      */   public void setLabelPosition(String id, double x, double y)
/*      */   {
/* 2498 */     String label_id = null;
/* 2499 */     for (int i = 0; i < this.labelIDs.length; i++) {
/* 2500 */       if (id.equals(this.labelIDs[i])) {
/* 2501 */         label_id = id;
/*      */       }
/*      */     }
/*      */     
/* 2505 */     if (label_id != null) {
/* 2506 */       this.labelXs.put(id, new Double(x));
/* 2507 */       this.labelYs.put(id, new Double(y));
/*      */     }
/*      */     
/*      */ 
/* 2511 */     autoRepaint();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void setLabelURL(String id, String adress)
/*      */   {
/* 2522 */     if (adress == null) {
/* 2523 */       this.labelURLs.remove(id);
/*      */     }
/*      */     else
/*      */     {
/* 2527 */       this.labelURLs.put(id, adress);
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   String getTargetLabel(String id)
/*      */   {
/* 2537 */     String label = "";
/* 2538 */     int style = ((Integer)this.targetsStyle.get(id)).intValue();
/* 2539 */     double value = ((Double)this.targetsValue.get(id)).doubleValue();
/* 2540 */     String number = formatNumber(value, this.rangeDecimalCount[0]);
/* 2541 */     if (getLabel("rangeLabelPrefix") != null) {
/* 2542 */       number = getLabel("rangeLabelPrefix") + number;
/*      */     }
/* 2544 */     if (getLabel("rangeLabelPostfix") != null) {
/* 2545 */       number = number + getLabel("rangeLabelPostfix");
/*      */     }
/* 2547 */     switch (style) {
/*      */     case 0: 
/* 2549 */       label = "";
/* 2550 */       break;
/*      */     case 1: 
/* 2552 */       label = id;
/* 2553 */       break;
/*      */     case 2: 
/* 2555 */       label = number;
/* 2556 */       break;
/*      */     case 3: 
/* 2558 */       if (this.rangePosition[0] == 0) {
/* 2559 */         label = id + " " + number;
/*      */       } else {
/* 2561 */         label = number + " " + id;
/*      */       }
/*      */       break;
/*      */     }
/* 2565 */     return label;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public double getTargetValueLine(String id)
/*      */   {
/* 2575 */     if (this.targetsValue.get(id) != null) {
/* 2576 */       return ((Double)this.targetsValue.get(id)).doubleValue();
/*      */     }
/* 2578 */     return 0.0D;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void setTargetLabelsPosition(int position)
/*      */   {
/* 2588 */     this.targetLabelsPosition = Math.min(1, Math.max(-1, position));
/* 2589 */     this.needRender = true;
/* 2590 */     this.needGraphBounds = true;
/* 2591 */     this.needChartCalculation = true;
/* 2592 */     autoRepaint();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public int getTargetLabelsPosition()
/*      */   {
/* 2602 */     return this.targetLabelsPosition;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void setRangeAdjusterOn(int adjuster, boolean on)
/*      */   {
/* 2614 */     adjuster = Math.min(this.rangeAdjusterOn.length - 1, Math.max(0, adjuster));
/* 2615 */     boolean old_state = this.rangeAdjusterOn[adjuster];
/* 2616 */     this.rangeAdjusterOn[adjuster] = on;
/*      */     
/* 2618 */     if (!on) {
/* 2619 */       int rangeIndex = this.rangeAdjusted[adjuster];
/* 2620 */       if (rangeIndex == -1) {
/* 2621 */         for (int i = 0; i < this.rangeOn.length; i++) {
/* 2622 */           setCurrentRange(i, getRange(i));
/* 2623 */           setCurrentLowerRange(i, getLowerRange(i));
/*      */         }
/*      */       } else {
/* 2626 */         setCurrentRange(rangeIndex, getRange(rangeIndex));
/* 2627 */         setCurrentLowerRange(rangeIndex, getLowerRange(rangeIndex));
/*      */       }
/*      */     }
/* 2630 */     if (old_state != on) {
/* 2631 */       this.needRender = true;
/* 2632 */       this.needGraphBounds = true;
/* 2633 */       this.needChartCalculation = true;
/*      */     }
/* 2635 */     autoRepaint();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public boolean isRangeAdjusterOn(int adjuster)
/*      */   {
/* 2645 */     adjuster = Math.min(this.rangeAdjusterOn.length - 1, Math.max(0, adjuster));
/* 2646 */     return this.rangeAdjusterOn[adjuster];
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void setRangeAdjusterPosition(int position)
/*      */   {
/* 2657 */     this.rangeAdjusterPosition = Math.min(1, Math.max(0, position));
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   /**
/*      */    * @deprecated
/*      */    */
/*      */   public void setRangeAdjusterPosition(int adjuster, int position)
/*      */   {
/* 2668 */     if (adjuster == 0) {
/* 2669 */       this.rangeAdjusterPosition = Math.min(1, Math.max(0, position));
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public int getRangeAdjusterPosition()
/*      */   {
/* 2679 */     return this.rangeAdjusterPosition;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   /**
/*      */    * @deprecated
/*      */    */
/*      */   public int getRangeAdjusterPosition(int adjuster)
/*      */   {
/* 2689 */     return this.rangeAdjusterPosition;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   int getAdjusterPosition(int adjuster)
/*      */   {
/* 2699 */     if ((this.rangeOn.length <= 2) && (adjuster == 0)) {
/* 2700 */       return this.rangeAdjusterPosition;
/*      */     }
/* 2702 */     return this.rangePosition[adjuster];
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void setRangeAdjusted(int adjuster, int range)
/*      */   {
/* 2713 */     adjuster = Math.min(this.rangeAdjusterOn.length - 1, Math.max(-1, adjuster));
/* 2714 */     range = Math.min(this.rangeAdjusterOn.length - 1, Math.max(-1, range));
/* 2715 */     this.rangeAdjusted[adjuster] = range;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public int getRangeAdjusted(int adjuster)
/*      */   {
/* 2725 */     adjuster = Math.min(this.rangeAdjusterOn.length - 1, Math.max(-1, adjuster));
/* 2726 */     return this.rangeAdjusted[adjuster];
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void setSampleScrollerOn(boolean on)
/*      */   {
/* 2737 */     if (on != this.sampleScrollerOn) {
/* 2738 */       this.sampleScrollerOn = on;
/* 2739 */       this.needRender = true;
/* 2740 */       this.needGraphBounds = true;
/* 2741 */       this.needChartCalculation = true;
/* 2742 */       autoRepaint();
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public boolean isSampleScrollerOn()
/*      */   {
/* 2752 */     return this.sampleScrollerOn;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void setZoomOn(boolean on)
/*      */   {
/* 2763 */     this.zoomOn = on;
/* 2764 */     this.needRender = true;
/* 2765 */     autoRepaint();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public boolean isZoomOn()
/*      */   {
/* 2774 */     return this.zoomOn;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void setSeriesCount(int count)
/*      */   {
/* 2786 */     if (count != getSeriesCount()) {
/* 2787 */       this.chartData.setSeriesCount(count);
/* 2788 */       checkDataIntegrity();
/* 2789 */       this.needRender = true;
/* 2790 */       this.needChartCalculation = true;
/* 2791 */       autoRepaint();
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public int getSeriesCount()
/*      */   {
/* 2800 */     return this.chartData.getSeriesCount();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void setSampleCount(int count)
/*      */   {
/* 2809 */     if (count != getSampleCount()) {
/* 2810 */       this.chartData.setSampleCount(count);
/* 2811 */       checkDataIntegrity();
/* 2812 */       this.needRender = true;
/* 2813 */       this.needChartCalculation = true;
/* 2814 */       autoRepaint();
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public int getSampleCount()
/*      */   {
/* 2823 */     return this.chartData.getSampleCount();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public double getMaxValue(int serie)
/*      */   {
/* 2834 */     return this.chartData.getMaxValue(serie);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   double getHighestValue(int rangeIndex)
/*      */   {
/* 2845 */     rangeIndex = Math.min(this.rangeOn.length - 1, Math.max(0, rangeIndex));
/* 2846 */     int seriesCount = getSeriesCount();
/* 2847 */     double max = 0.0D;
/* 2848 */     for (int i = 0; i < seriesCount; i++) {
/* 2849 */       if (getSeriesRange(i) == rangeIndex) {
/* 2850 */         max = Math.max(getMaxValue(i), max);
/*      */       }
/*      */     }
/*      */     
/* 2854 */     for (int i = 0; i < this.overlayCharts.size(); i++) {
/* 2855 */       Chart chart = (Chart)this.overlayCharts.elementAt(i);
/* 2856 */       if ((chart != null) && (!chart.chartType.equals("pie"))) {
/* 2857 */         for (int j = 0; j < chart.getSeriesCount(); j++) {
/* 2858 */           if (chart.getSeriesRange(j) == rangeIndex) {
/* 2859 */             max = Math.max(chart.getMaxValue(j), max);
/*      */           }
/*      */         }
/*      */       }
/*      */     }
/* 2864 */     return max;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public double getMinValue(int serie)
/*      */   {
/* 2875 */     return this.chartData.getMinValue(serie);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   double getLowestValue(int rangeIndex)
/*      */   {
/* 2885 */     rangeIndex = Math.min(this.rangeOn.length - 1, Math.max(0, rangeIndex));
/* 2886 */     int seriesCount = getSeriesCount();
/* 2887 */     double min = 0.0D;
/* 2888 */     for (int i = 0; i < seriesCount; i++) {
/* 2889 */       if (getSeriesRange(i) == rangeIndex) {
/* 2890 */         min = Math.min(getMinValue(i), min);
/*      */       }
/*      */     }
/*      */     
/* 2894 */     for (int i = 0; i < this.overlayCharts.size(); i++) {
/* 2895 */       Chart chart = (Chart)this.overlayCharts.elementAt(i);
/* 2896 */       if ((chart != null) && (!chart.chartType.equals("pie"))) {
/* 2897 */         for (int j = 0; j < chart.getSeriesCount(); j++) {
/* 2898 */           if (chart.getSeriesRange(j) == rangeIndex) {
/* 2899 */             min = Math.min(chart.getMinValue(j), min);
/*      */           }
/*      */         }
/*      */       }
/*      */     }
/* 2904 */     return min;
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
/*      */   public void setVisibleSamples(int start, int count)
/*      */   {
/* 2920 */     int sampleCount = getSampleCount();
/* 2921 */     if (count < 0) {
/* 2922 */       count = sampleCount;
/*      */     }
/* 2924 */     start = Math.max(0, start);
/* 2925 */     start = Math.min(sampleCount - 1, start);
/* 2926 */     count = Math.max(1, count);
/* 2927 */     count = Math.min(sampleCount - start, count);
/* 2928 */     start = Math.min(sampleCount - count, start);
/* 2929 */     this.visibleSamples[0] = start;
/* 2930 */     this.visibleSamples[1] = count;
/*      */     
/*      */ 
/* 2933 */     if (this.rightToLeftScrollingOn)
/*      */     {
/* 2935 */       int last_index = 0;
/* 2936 */       int seriesCount = getSeriesCount();
/* 2937 */       for (int serie = 0; serie < seriesCount; serie++) {
/* 2938 */         ChartSample[] samples = getSamples(serie);
/* 2939 */         if (samples != null) {
/* 2940 */           for (int sample = samples.length - 1; sample >= 0; sample--) {
/* 2941 */             if ((samples[sample] != null) && (samples[sample].hasValue())) {
/* 2942 */               last_index = Math.max(last_index, sample);
/* 2943 */               break;
/*      */             }
/*      */           }
/*      */         }
/*      */       }
/* 2948 */       this.leftScrollerFactor = ((sampleCount - count) / sampleCount);
/* 2949 */       this.rightScrollerFactor = 0.0D;
/*      */     }
/*      */     else
/*      */     {
/* 2953 */       this.leftScrollerFactor = (start / sampleCount);
/* 2954 */       this.rightScrollerFactor = ((sampleCount - count - start) / sampleCount);
/*      */     }
/* 2956 */     this.needRender = true;
/* 2957 */     this.needChartCalculation = true;
/* 2958 */     autoRepaint();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   private void checkRangeIntegrity(int length, int newLength)
/*      */   {
/* 2970 */     if (newLength > length)
/*      */     {
/*      */ 
/* 2973 */       boolean[] newRangeOn = new boolean[newLength];
/* 2974 */       System.arraycopy(this.rangeOn, 0, newRangeOn, 0, this.rangeOn.length);
/* 2975 */       this.rangeOn = newRangeOn;
/*      */       
/*      */ 
/* 2978 */       int[] newRangePosition = new int[newLength];
/* 2979 */       System.arraycopy(this.rangePosition, 0, newRangePosition, 0, this.rangePosition.length);
/* 2980 */       this.rangePosition = newRangePosition;
/*      */       
/* 2982 */       if (length == 1) {
/* 2983 */         this.rangePosition[1] = 1;
/*      */       }
/*      */       
/*      */ 
/* 2987 */       Rectangle[] newRangeBounds = new Rectangle[newLength];
/* 2988 */       System.arraycopy(this.rangeBounds, 0, newRangeBounds, 0, this.rangeBounds.length);
/* 2989 */       this.rangeBounds = newRangeBounds;
/*      */       
/*      */ 
/* 2992 */       double[] newUpperRange = new double[newLength];
/* 2993 */       System.arraycopy(this.upperRange, 0, newUpperRange, 0, this.upperRange.length);
/* 2994 */       for (int i = this.upperRange.length; i < newUpperRange.length; i++) {
/* 2995 */         newUpperRange[i] = 100.0D;
/*      */       }
/* 2997 */       this.upperRange = newUpperRange;
/*      */       
/*      */ 
/* 3000 */       double[] newLowerRange = new double[newLength];
/* 3001 */       System.arraycopy(this.lowerRange, 0, newLowerRange, 0, this.lowerRange.length);
/* 3002 */       this.lowerRange = newLowerRange;
/*      */       
/*      */ 
/* 3005 */       double[] newCurrentUpperRange = new double[newLength];
/* 3006 */       System.arraycopy(this.currentUpperRange, 0, newCurrentUpperRange, 0, this.currentUpperRange.length);
/* 3007 */       for (int i = this.currentUpperRange.length; i < newCurrentUpperRange.length; i++) {
/* 3008 */         newCurrentUpperRange[i] = 100.0D;
/*      */       }
/* 3010 */       this.currentUpperRange = newCurrentUpperRange;
/*      */       
/*      */ 
/* 3013 */       double[] newCurrentLowerRange = new double[newLength];
/* 3014 */       System.arraycopy(this.currentLowerRange, 0, newCurrentLowerRange, 0, this.currentLowerRange.length);
/* 3015 */       this.currentLowerRange = newCurrentLowerRange;
/*      */       
/*      */ 
/* 3018 */       int[] newRangeDecimalCount = new int[newLength];
/* 3019 */       System.arraycopy(this.rangeDecimalCount, 0, newRangeDecimalCount, 0, this.rangeDecimalCount.length);
/* 3020 */       this.rangeDecimalCount = newRangeDecimalCount;
/*      */       
/*      */ 
/* 3023 */       boolean[] newRangeAdjusterOn = new boolean[newLength];
/* 3024 */       System.arraycopy(this.rangeAdjusterOn, 0, newRangeAdjusterOn, 0, this.rangeAdjusterOn.length);
/* 3025 */       this.rangeAdjusterOn = newRangeAdjusterOn;
/*      */       
/*      */ 
/* 3028 */       int[] newRangeAdjusted = new int[newLength];
/* 3029 */       System.arraycopy(this.rangeAdjusted, 0, newRangeAdjusted, 0, this.rangeAdjusted.length);
/* 3030 */       for (int i = this.rangeAdjusted.length; i < newRangeAdjusted.length; i++) {
/* 3031 */         newRangeAdjusted[i] = i;
/*      */       }
/* 3033 */       this.rangeAdjusted = newRangeAdjusted;
/*      */       
/*      */ 
/* 3036 */       Rectangle[] newRangeAdjusterBounds = new Rectangle[newLength];
/* 3037 */       System.arraycopy(this.rangeAdjusterBounds, 0, newRangeAdjusterBounds, 0, this.rangeAdjusterBounds.length);
/* 3038 */       for (int i = this.rangeAdjusterBounds.length; i < newRangeAdjusterBounds.length; i++) {
/* 3039 */         newRangeAdjusterBounds[i] = new Rectangle();
/*      */       }
/* 3041 */       this.rangeAdjusterBounds = newRangeAdjusterBounds;
/*      */       
/*      */ 
/* 3044 */       Color[] newRangeColor = new Color[newLength];
/* 3045 */       System.arraycopy(this.rangeColor, 0, newRangeColor, 0, this.rangeColor.length);
/* 3046 */       this.rangeColor = newRangeColor;
/*      */       
/*      */ 
/* 3049 */       boolean[] newRangeLabelsOn = new boolean[newLength];
/* 3050 */       System.arraycopy(this.rangeLabelsOn, 0, newRangeLabelsOn, 0, this.rangeLabelsOn.length);
/* 3051 */       for (int i = this.rangeLabelsOn.length; i < newRangeLabelsOn.length; i++) {
/* 3052 */         newRangeLabelsOn[i] = true;
/*      */       }
/* 3054 */       this.rangeLabelsOn = newRangeLabelsOn;
/*      */       
/*      */ 
/* 3057 */       int[] newZeroLine = new int[newLength];
/* 3058 */       System.arraycopy(this.zeroLine, 0, newZeroLine, 0, this.zeroLine.length);
/* 3059 */       this.zeroLine = newZeroLine;
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
/*      */   protected void checkDataIntegrity()
/*      */   {
/* 3072 */     int seriesCount = this.chartData.getSeriesCount();
/* 3073 */     if (seriesCount != this.seriesLabelColors.length) {
/* 3074 */       Color[] newSeriesLabelColors = new Color[seriesCount];
/* 3075 */       int count = Math.min(seriesCount, this.seriesLabelColors.length);
/* 3076 */       System.arraycopy(this.seriesLabelColors, 0, newSeriesLabelColors, 0, count);
/* 3077 */       this.seriesLabelColors = newSeriesLabelColors;
/*      */       
/* 3079 */       int[] newSeriesRange = new int[seriesCount];
/* 3080 */       System.arraycopy(this.seriesRange, 0, newSeriesRange, 0, Math.min(this.seriesRange.length, newSeriesRange.length));
/* 3081 */       this.seriesRange = newSeriesRange;
/*      */       
/* 3083 */       int[] newSampleDecimalCount = new int[seriesCount];
/* 3084 */       System.arraycopy(this.sampleDecimalCount, 0, newSampleDecimalCount, 0, Math.min(this.sampleDecimalCount.length, newSampleDecimalCount.length));
/* 3085 */       this.sampleDecimalCount = newSampleDecimalCount;
/*      */       
/* 3087 */       boolean[] newValueLabelsOn = new boolean[seriesCount];
/* 3088 */       System.arraycopy(this.valueLabelsOn, 0, newValueLabelsOn, 0, Math.min(this.valueLabelsOn.length, newValueLabelsOn.length));
/* 3089 */       this.valueLabelsOn = newValueLabelsOn;
/*      */       
/* 3091 */       Color[] newValueLabelColors = new Color[seriesCount];
/* 3092 */       count = Math.min(seriesCount, this.valueLabelColors.length);
/* 3093 */       System.arraycopy(this.valueLabelColors, 0, newValueLabelColors, 0, count);
/* 3094 */       this.valueLabelColors = newValueLabelColors;
/*      */     }
/*      */     
/*      */ 
/* 3098 */     String[] legendLabels = getLegendLabels();
/* 3099 */     if (this.legendSelection.length != legendLabels.length) {
/* 3100 */       boolean[] newLegendSelection = new boolean[legendLabels.length];
/* 3101 */       System.arraycopy(this.legendSelection, 0, newLegendSelection, 0, Math.min(this.legendSelection.length, newLegendSelection.length));
/* 3102 */       this.legendSelection = newLegendSelection;
/*      */     }
/*      */     
/*      */ 
/* 3106 */     int sampleCount = this.chartData.getSampleCount();
/* 3107 */     if (sampleCount != this.sampleLabelColors.length) {
/* 3108 */       Color[] newSampleLabelColors = new Color[sampleCount];
/* 3109 */       int count = Math.min(sampleCount, this.sampleLabelColors.length);
/* 3110 */       System.arraycopy(this.sampleLabelColors, 0, newSampleLabelColors, 0, count);
/* 3111 */       this.sampleLabelColors = newSampleLabelColors;
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
/*      */   public void setSampleDecimalCount(int serie, int count)
/*      */   {
/* 3126 */     if (serie == -1) {
/* 3127 */       for (int i = 0; i < this.sampleDecimalCount.length; i++) {
/* 3128 */         this.sampleDecimalCount[i] = count;
/*      */       }
/* 3130 */     } else if ((serie >= 0) && (serie < this.sampleDecimalCount.length)) {
/* 3131 */       this.sampleDecimalCount[serie] = count;
/*      */     }
/* 3133 */     if ((isValueLabelsOn(serie)) && (getLabelAngle("valueLabelAngle") != 0)) {
/* 3134 */       angledLabelCache.clear();
/*      */     }
/* 3136 */     this.needRender = true;
/* 3137 */     this.needGraphBounds = true;
/* 3138 */     this.needChartCalculation = true;
/* 3139 */     autoRepaint();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public int getSampleDecimalCount(int serie)
/*      */   {
/* 3148 */     if ((serie >= 0) && (serie < this.sampleDecimalCount.length)) {
/* 3149 */       return this.sampleDecimalCount[serie];
/*      */     }
/* 3151 */     return 0;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void setTitle(String title)
/*      */   {
/* 3163 */     if (this.chartTitle != null) {
/* 3164 */       Long key = new Long(this.chartTitle.hashCode() + getFont("titleFont").hashCode());
/* 3165 */       this.labelSizeCache.remove(key);
/*      */     }
/* 3167 */     this.chartTitle = title;
/* 3168 */     this.needRender = true;
/* 3169 */     this.needGraphBounds = true;
/* 3170 */     this.needChartCalculation = true;
/* 3171 */     autoRepaint();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public String getTitle()
/*      */   {
/* 3180 */     return this.chartTitle;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void setTitleOn(boolean on)
/*      */   {
/* 3191 */     if (on != this.chartTitleOn) {
/* 3192 */       this.chartTitleOn = on;
/* 3193 */       this.needRender = true;
/* 3194 */       this.needGraphBounds = true;
/* 3195 */       this.needChartCalculation = true;
/* 3196 */       autoRepaint();
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public boolean isTitleOn()
/*      */   {
/* 3206 */     return this.chartTitleOn;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void setFont(String label, Font font)
/*      */   {
/* 3218 */     if ((label == null) || (label.trim().length() < 1)) {
/* 3219 */       return;
/*      */     }
/*      */     
/* 3222 */     label = label.trim().toLowerCase();
/* 3223 */     if (font != null) {
/* 3224 */       this.labelFonts.put(label, font);
/*      */     } else {
/* 3226 */       this.labelFonts.remove(label);
/*      */     }
/*      */     
/* 3229 */     this.labelSizeCache.clear();
/* 3230 */     this.needRender = true;
/* 3231 */     this.needGraphBounds = true;
/* 3232 */     this.needChartCalculation = true;
/* 3233 */     autoRepaint();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public Font getFont(String label)
/*      */   {
/* 3244 */     if (label == null) {
/* 3245 */       getFont();
/*      */     }
/* 3247 */     Font font = (Font)this.labelFonts.get(label.trim().toLowerCase());
/* 3248 */     if (font != null) {
/* 3249 */       return font;
/*      */     }
/* 3251 */     return getFont();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void setLegendPosition(int position)
/*      */   {
/* 3262 */     if (position != this.legendPosition) {
/* 3263 */       if ((position == 2) || (position == 3) || (position == 0) || (position == 1)) {
/* 3264 */         this.legendPosition = position;
/* 3265 */         this.needRender = true;
/* 3266 */         this.needGraphBounds = true;
/* 3267 */         this.needChartCalculation = true;
/* 3268 */         autoRepaint();
/*      */       } else {
/* 3270 */         throw new IllegalArgumentException("Should be TOP, BOTTOM, LEFT, or RIGHT");
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public int getLegendPosition()
/*      */   {
/* 3281 */     return this.legendPosition;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void setLegendColumns(int columns)
/*      */   {
/* 3291 */     this.legendColumns = Math.max(0, columns);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public int getLegendColumns()
/*      */   {
/* 3300 */     return this.legendColumns;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void setLegendReverseOn(boolean on)
/*      */   {
/* 3310 */     if (on != this.legendReverseOn) {
/* 3311 */       this.legendReverseOn = on;
/* 3312 */       this.needRender = true;
/* 3313 */       this.needGraphBounds = true;
/* 3314 */       this.needChartCalculation = true;
/* 3315 */       autoRepaint();
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public boolean isLegendReverseOn()
/*      */   {
/* 3325 */     return this.legendReverseOn;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void setLegendOn(boolean on)
/*      */   {
/* 3336 */     if (on != this.legendOn) {
/* 3337 */       this.legendOn = on;
/* 3338 */       this.legend.width = 0;
/* 3339 */       this.needRender = true;
/* 3340 */       this.needGraphBounds = true;
/* 3341 */       this.needChartCalculation = true;
/* 3342 */       autoRepaint();
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public boolean isLegendOn()
/*      */   {
/* 3352 */     return this.legendOn;
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
/*      */   public void setLegendColors(Color[] colors)
/*      */   {
/* 3365 */     this.legendColors = colors;
/* 3366 */     this.needRender = true;
/* 3367 */     autoRepaint();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public Color[] getLegendColors()
/*      */   {
/* 3376 */     return this.legendColors;
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
/*      */   public void setLegendColor(int index, Color color)
/*      */   {
/* 3389 */     if (this.legendColors == null) {
/* 3390 */       this.legendColors = new Color[index + 1];
/*      */     }
/*      */     
/* 3393 */     if (index >= this.legendColors.length) {
/* 3394 */       Color[] tmp_arr = new Color[index + 1];
/* 3395 */       System.arraycopy(this.legendColors, 0, tmp_arr, 0, this.legendColors.length);
/* 3396 */       this.legendColors = tmp_arr;
/*      */     }
/*      */     
/* 3399 */     this.legendColors[index] = color;
/* 3400 */     this.needRender = true;
/* 3401 */     autoRepaint();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public Color getLegendColor(int index)
/*      */   {
/* 3411 */     if ((this.legendColors != null) && (index >= 0) && (index < this.legendColors.length) && (this.legendColors[index] != null)) {
/* 3412 */       return this.legendColors[index];
/*      */     }
/* 3414 */     return getSampleColor(index);
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
/*      */   public void setLegendImage(int index, String name)
/*      */   {
/* 3427 */     if ((index >= 0) && (index < 1000))
/*      */     {
/* 3429 */       if (this.legendImages == null) {
/* 3430 */         this.legendImages = new String[index + 1];
/*      */       }
/* 3432 */       if (index >= this.legendImages.length) {
/* 3433 */         String[] tmp_arr = new String[index + 1];
/* 3434 */         System.arraycopy(this.legendImages, 0, tmp_arr, 0, this.legendImages.length);
/* 3435 */         this.legendImages = tmp_arr;
/*      */       }
/*      */       
/* 3438 */       this.legendImages[index] = name;
/* 3439 */       this.needRender = true;
/* 3440 */       this.needGraphBounds = true;
/* 3441 */       this.needChartCalculation = true;
/* 3442 */       autoRepaint();
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public String getLegendImage(int index)
/*      */   {
/* 3453 */     if ((this.legendImages != null) && (index >= 0) && (index < this.legendImages.length)) {
/* 3454 */       return this.legendImages[index];
/*      */     }
/* 3456 */     return null;
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
/*      */   public void setValueLabelsOn(boolean on)
/*      */   {
/* 3469 */     setValueLabelsOn(-1, on);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void setValueLabelsOn(int serie, boolean on)
/*      */   {
/* 3481 */     if ((serie >= 0) && (serie < this.valueLabelsOn.length)) {
/* 3482 */       this.valueLabelsOn[serie] = on;
/* 3483 */     } else if (serie == -1) {
/* 3484 */       for (int i = 0; i < this.valueLabelsOn.length; i++) {
/* 3485 */         this.valueLabelsOn[i] = on;
/*      */       }
/*      */     }
/* 3488 */     this.needRender = true;
/* 3489 */     this.needGraphBounds = true;
/* 3490 */     this.needChartCalculation = true;
/* 3491 */     autoRepaint();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public boolean isValueLabelsOn()
/*      */   {
/* 3500 */     return isValueLabelsOn(-1);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public boolean isValueLabelsOn(int serie)
/*      */   {
/* 3511 */     if ((serie >= 0) && (serie < this.valueLabelsOn.length))
/* 3512 */       return this.valueLabelsOn[serie];
/* 3513 */     if (serie == -1) {
/* 3514 */       for (int i = 0; i < this.valueLabelsOn.length; i++) {
/* 3515 */         if (this.valueLabelsOn[i] != 0) {
/* 3516 */           return true;
/*      */         }
/*      */       }
/*      */     }
/*      */     
/* 3521 */     return false;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void setValueLabelColor(int serie, Color color)
/*      */   {
/*      */     try
/*      */     {
/* 3533 */       if (this.valueLabelColors[serie] != color) {
/* 3534 */         this.valueLabelColors[serie] = color;
/* 3535 */         this.needRender = true;
/* 3536 */         autoRepaint();
/*      */       }
/*      */     } catch (IndexOutOfBoundsException e) {
/* 3539 */       throw new IllegalArgumentException("Invalid series index: " + serie);
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public Color getValueLabelColor(int serie)
/*      */   {
/*      */     try
/*      */     {
/* 3552 */       return this.valueLabelColors[serie];
/*      */     } catch (IndexOutOfBoundsException e) {
/* 3554 */       throw new IllegalArgumentException("Invalid series index: " + serie);
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
/*      */   public void setValueLabelStyle(int style)
/*      */   {
/* 3567 */     if ((style == 5) && (this.chartType != "pie")) {
/* 3568 */       style = 1;
/*      */     }
/* 3570 */     switch (style) {
/*      */     case 0: 
/*      */     case 1: 
/*      */     case 3: 
/*      */     case 4: 
/*      */     case 5: 
/* 3576 */       if (style != this.valueLabelStyle) {
/* 3577 */         this.valueLabelStyle = style;
/* 3578 */         this.needRender = true;
/* 3579 */         this.needGraphBounds = true;
/* 3580 */         this.needChartCalculation = true;
/* 3581 */         autoRepaint();
/*      */       }
/* 3583 */       break;
/*      */     case 2: default: 
/* 3585 */       throw new IllegalArgumentException("Invalid valueLabelStyle: " + style);
/*      */     }
/*      */     
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public int getValueLabelStyle()
/*      */   {
/* 3595 */     return this.valueLabelStyle;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void setFloatingOnLegendOn(boolean on)
/*      */   {
/* 3606 */     this.floatingOnLegendOn = on;
/* 3607 */     this.needRender = true;
/* 3608 */     autoRepaint();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public boolean isFloatingOnLegendOn()
/*      */   {
/* 3620 */     return this.floatingOnLegendOn;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void displayFloatingLabel(int serie, int sample)
/*      */   {
/* 3630 */     this.mouseOverSampleIndex = sample;
/* 3631 */     this.mouseOverSeriesIndex = serie;
/* 3632 */     repaint();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void set3DModeOn(boolean on)
/*      */   {
/* 3642 */     if (this.display3dOn != on) {
/* 3643 */       this.display3dOn = on;
/* 3644 */       this.needRender = true;
/* 3645 */       this.needGraphBounds = true;
/* 3646 */       this.needChartCalculation = true;
/* 3647 */       autoRepaint();
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public boolean is3DModeOn()
/*      */   {
/* 3657 */     return this.display3dOn;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void set3DDepth(int depth)
/*      */   {
/* 3668 */     if ((depth >= -1) && (depth < 32767)) {
/* 3669 */       this.depth3d = depth;
/* 3670 */       if (depth >= 0) {
/* 3671 */         this.depth3dPoint.x = this.depth3d;
/* 3672 */         this.depth3dPoint.y = (-this.depth3d);
/*      */       }
/* 3674 */       this.needRender = true;
/* 3675 */       this.needGraphBounds = true;
/* 3676 */       this.needChartCalculation = true;
/* 3677 */       autoRepaint();
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public int get3DDepth()
/*      */   {
/* 3688 */     return this.depth3d;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void setBackground(Color color)
/*      */   {
/* 3699 */     super.setBackground(color);
/* 3700 */     this.needRender = true;
/* 3701 */     autoRepaint();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void setForeground(Color color)
/*      */   {
/* 3711 */     super.setForeground(color);
/* 3712 */     this.needRender = true;
/* 3713 */     autoRepaint();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void setChartBackground(Color color)
/*      */   {
/* 3723 */     this.chartBackground = color;
/* 3724 */     this.needRender = true;
/* 3725 */     autoRepaint();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public Color getChartBackground()
/*      */   {
/* 3733 */     return this.chartBackground;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void setGridImage(String name)
/*      */   {
/* 3744 */     this.gridImage = name;
/* 3745 */     this.needRender = true;
/* 3746 */     this.needGraphBounds = true;
/* 3747 */     this.needChartCalculation = true;
/* 3748 */     autoRepaint();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public String getGridImage(int index)
/*      */   {
/* 3757 */     return this.gridImage;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void setChartForeground(Color color)
/*      */   {
/* 3767 */     this.chartForeground = color;
/* 3768 */     this.needRender = true;
/* 3769 */     autoRepaint();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public Color getChartForeground()
/*      */   {
/* 3777 */     return this.chartForeground;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void setSampleColors(Color[] colors)
/*      */   {
/* 3788 */     if (colors != null) {
/* 3789 */       this.sampleColors = colors;
/*      */     } else {
/* 3791 */       this.sampleColors = new Color[DEFAULT_SAMPLE_COLORS.length];
/* 3792 */       System.arraycopy(DEFAULT_SAMPLE_COLORS, 0, this.sampleColors, 0, this.sampleColors.length);
/*      */     }
/* 3794 */     this.needRender = true;
/* 3795 */     autoRepaint();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public Color[] getSampleColors()
/*      */   {
/* 3805 */     return this.sampleColors;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void setSampleColor(int index, Color color)
/*      */   {
/* 3817 */     if (index < 0) throw new IllegalArgumentException("Negativ index");
/* 3818 */     this.sampleColors[(index % this.sampleColors.length)] = color;
/* 3819 */     this.needRender = true;
/* 3820 */     autoRepaint();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public Color getSampleColor(int index)
/*      */   {
/* 3830 */     Color c = DEFAULT_SAMPLE_COLORS[(index % DEFAULT_SAMPLE_COLORS.length)];
/* 3831 */     if ((this.sampleColors != null) && (this.sampleColors.length > 0)) {
/* 3832 */       c = this.sampleColors[(index % this.sampleColors.length)];
/* 3833 */       if (c == null) {
/* 3834 */         int min = Math.min(this.sampleColors.length, DEFAULT_SAMPLE_COLORS.length);
/* 3835 */         c = DEFAULT_SAMPLE_COLORS[(index % min)];
/*      */       }
/*      */     }
/* 3838 */     return c;
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
/*      */   public void setAutomaticRepaintOn(boolean state)
/*      */   {
/* 3853 */     this.automaticRepaintOn = state;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public boolean isAutomaticRepaintOn()
/*      */   {
/* 3863 */     return this.automaticRepaintOn;
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
/*      */   public void setSelection(int serie, int sample, boolean selected)
/*      */   {
/* 3881 */     setSelection(serie, sample, selected, false, true);
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
/*      */   public void setSelection(int serie, int sample, boolean selected, boolean notify)
/*      */   {
/* 3895 */     setSelection(serie, sample, selected, false, notify);
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
/*      */   private void setSelection(int serie, int sample, boolean selected, boolean clear, boolean notify)
/*      */   {
/* 3913 */     this.chartData.setSelection(serie, sample, selected, clear);
/* 3914 */     if (this.overlayCharts != null) {
/* 3915 */       int count = this.overlayCharts.size();
/* 3916 */       for (int i = 0; i < count; i++) {
/* 3917 */         Chart overlay = (Chart)this.overlayCharts.elementAt(i);
/* 3918 */         if (overlay != null) {
/* 3919 */           overlay.chartData.setSelection(serie, sample, selected, clear);
/*      */         }
/*      */       }
/*      */     }
/* 3923 */     this.lastSelectedSample = -1;
/* 3924 */     this.lastSelectedSeries = -1;
/*      */     
/*      */ 
/* 3927 */     for (int i = 0; i < this.legendSelection.length; i++) {
/* 3928 */       this.legendSelection[i] = false;
/*      */     }
/*      */     
/*      */ 
/* 3932 */     if ((sample == -1) && (serie == -1) && (this.selectedSample != null)) {
/* 3933 */       if (notify) {
/* 3934 */         notifyListeners(this, 2, this.selectedSample);
/*      */       }
/*      */       
/* 3937 */       this.selectedSample = null;
/*      */ 
/*      */ 
/*      */     }
/* 3941 */     else if ((sample == -1) && (serie >= 0)) {
/* 3942 */       ChartSample s = new ChartSample(sample);
/* 3943 */       s.setSeries(serie);
/* 3944 */       if ((this.legendLabels != null) && (serie < this.legendLabels.length) && (this.legendLabels[serie] != null)) {
/* 3945 */         s.setLabel(this.legendLabels[serie]);
/* 3946 */       } else if ((serie >= 0) && (serie < getSeriesCount())) {
/* 3947 */         s.setLabel(getSeriesLabel(serie));
/*      */       }
/* 3949 */       if (selected) {
/* 3950 */         this.lastSelectedSeries = serie;
/*      */         
/*      */ 
/* 3953 */         if (((this.selectedSample == null) || (s.getSeries() != this.selectedSample.getSeries())) && 
/* 3954 */           (notify) && (this.selectedSample != null)) {
/* 3955 */           notifyListeners(this, 2, this.selectedSample);
/*      */         }
/*      */         
/*      */ 
/* 3959 */         if (notify) {
/* 3960 */           notifyListeners(this, 1, s);
/*      */         }
/*      */         
/* 3963 */         this.selectedSample = s;
/*      */       } else {
/* 3965 */         if (notify) {
/* 3966 */           notifyListeners(this, 2, s);
/*      */         }
/* 3968 */         if (s == this.selectedSample) {
/* 3969 */           this.selectedSample = null;
/*      */         }
/*      */         
/*      */       }
/*      */       
/*      */     }
/* 3975 */     else if ((sample >= 0) && (serie >= 0))
/*      */     {
/*      */ 
/* 3978 */       ChartSample s = null;
/* 3979 */       Chart source = null;
/* 3980 */       if ((serie < getSeriesCount()) && (sample < getSampleCount())) {
/* 3981 */         s = getSample(serie, sample);
/* 3982 */         source = this;
/*      */       }
/*      */       
/*      */ 
/* 3986 */       if ((s == null) && (this.overlayCharts != null)) {
/* 3987 */         int count = this.overlayCharts.size();
/* 3988 */         for (int i = count - 1; (s == null) && (i >= 0); i--) {
/* 3989 */           Chart overlay = (Chart)this.overlayCharts.elementAt(i);
/* 3990 */           if (overlay != null) {
/*      */             try {
/* 3992 */               s = overlay.getSample(serie, sample);
/* 3993 */               if (s != null) {
/* 3994 */                 overlay.setSelection(serie, sample, selected, clear, false);
/* 3995 */                 source = overlay;
/*      */               }
/*      */             }
/*      */             catch (IllegalArgumentException localIllegalArgumentException) {}
/*      */           }
/*      */         }
/*      */       }
/*      */       
/*      */ 
/*      */ 
/* 4005 */       if (selected) {
/* 4006 */         this.lastSelectedSample = sample;
/* 4007 */         this.lastSelectedSeries = serie;
/*      */         
/* 4009 */         if ((notify) && (this.selectedSample != null) && (s != this.selectedSample)) {
/* 4010 */           notifyListeners(this, 2, this.selectedSample);
/*      */         }
/*      */         
/* 4013 */         if (notify) {
/* 4014 */           notifyListeners(source, 1, s);
/*      */         }
/* 4016 */         this.selectedSample = s;
/*      */       }
/*      */       else
/*      */       {
/* 4020 */         if (notify) {
/* 4021 */           notifyListeners(this, 2, s);
/*      */         }
/* 4023 */         if (s == this.selectedSample) {
/* 4024 */           this.selectedSample = null;
/*      */         }
/*      */       }
/*      */     }
/*      */     
/*      */ 
/* 4030 */     if ((getSeriesCount() > 1) || (this.multiSeriesOn)) {
/* 4031 */       if ((serie >= 0) && (serie < this.legendSelection.length)) {
/* 4032 */         this.legendSelection[serie] = selected;
/*      */       }
/*      */     }
/* 4035 */     else if ((sample >= 0) && (sample < this.legendSelection.length)) {
/* 4036 */       this.legendSelection[sample] = selected;
/*      */     }
/*      */     
/*      */ 
/*      */ 
/* 4041 */     if (sample == -1) {
/* 4042 */       for (int i = 0; i < this.overlayCharts.size(); i++) {
/* 4043 */         Chart chart = (Chart)this.overlayCharts.elementAt(i);
/* 4044 */         if (chart != null) {
/* 4045 */           chart.setSelection(serie, sample, selected, clear, notify);
/*      */         }
/*      */       }
/*      */     }
/*      */     
/*      */ 
/* 4051 */     this.needRender = true;
/* 4052 */     autoRepaint();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public boolean isSelected(int serie, int sample)
/*      */   {
/* 4063 */     return this.chartData.isSelected(serie, sample);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public Object[] getSelectedObjects()
/*      */   {
/* 4073 */     int seriesCount = getSeriesCount();
/* 4074 */     int sampleCount = getSampleCount();
/* 4075 */     int selectedCount = 0;
/* 4076 */     for (int serie = 0; serie < seriesCount; serie++) {
/* 4077 */       for (int sample = 0; sample < sampleCount; sample++) {
/* 4078 */         if (isSelected(serie, sample)) {
/* 4079 */           selectedCount++;
/*      */         }
/*      */       }
/*      */     }
/*      */     
/* 4084 */     if (selectedCount > 0) {
/* 4085 */       Object[] selected = new Object[selectedCount];
/* 4086 */       int index = 0;
/* 4087 */       for (int serie = 0; serie < seriesCount; serie++) {
/* 4088 */         for (int sample = 0; sample < sampleCount; sample++) {
/* 4089 */           if ((isSelected(serie, sample)) && 
/* 4090 */             (index < selected.length)) {
/* 4091 */             selected[(index++)] = getSample(serie, sample);
/*      */           }
/*      */         }
/*      */       }
/*      */       
/* 4096 */       return selected;
/*      */     }
/* 4098 */     return null;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public int getLastSelectedSample()
/*      */   {
/* 4107 */     return this.lastSelectedSample;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public int getLastSelectedSeries()
/*      */   {
/* 4116 */     return this.lastSelectedSeries;
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
/*      */   public String getLabelAtPoint(int x, int y)
/*      */   {
/* 4129 */     for (int i = this.labelIDs.length - 1; i >= 0; i--)
/*      */     {
/* 4131 */       String id = this.labelIDs[i];
/* 4132 */       Integer serie = (Integer)this.labelSeries.get(id);
/* 4133 */       Integer sample = (Integer)this.labelSamples.get(id);
/*      */       
/*      */ 
/* 4136 */       boolean do_check = (id != null) && (this.labelURLs.get(id) != null);
/* 4137 */       do_check |= ((sample != null) && (serie != null));
/* 4138 */       if (do_check)
/*      */       {
/*      */ 
/* 4141 */         Double X = (Double)this.labelXs.get(id);
/* 4142 */         Double Y = (Double)this.labelYs.get(id);
/* 4143 */         if ((id != null) && (X != null) && (Y != null)) {
/* 4144 */           String text = (String)this.labelTexts.get(id);
/* 4145 */           FontMetrics fm = getFontMetrics(getFont("font"));
/* 4146 */           Dimension labelSize = getLabelSize(text, fm);
/* 4147 */           double double_x = X.doubleValue();
/* 4148 */           double double_y = Y.doubleValue();
/* 4149 */           int lab_x = (int)double_x - 3;
/* 4150 */           int lab_y = (int)double_y - fm.getAscent() + 1;
/* 4151 */           int range = 0;
/* 4152 */           if (serie != null) {
/* 4153 */             range = getSeriesRange(serie.intValue());
/*      */           }
/*      */           
/*      */ 
/* 4157 */           int box_width = labelSize.width + 4;
/* 4158 */           int box_height = labelSize.height + 2;
/* 4159 */           if ((double_x > 0.0D) && (double_x < 1.0D) && (double_y > 0.0D) && (double_y < 1.0D)) {
/* 4160 */             Rectangle bounds = new Rectangle(this.chartDataBounds);
/* 4161 */             if (this.chartType.equals("pie")) {
/* 4162 */               if (this.graphBounds == null) {
/* 4163 */                 return null;
/*      */               }
/* 4165 */               bounds = new Rectangle(this.graphBounds);
/*      */             } else {
/* 4167 */               bounds.height = ((int)(bounds.height / (this.currentUpperRange[range] - this.currentLowerRange[range]) * (this.upperRange[range] - this.lowerRange[range])));
/* 4168 */               bounds.y -= (int)((this.currentUpperRange[range] - this.upperRange[range]) / (this.lowerRange[range] - this.upperRange[range]) * bounds.height);
/* 4169 */               if (is3DModeOn()) {
/* 4170 */                 bounds.x -= this.depth3dPoint.x;
/* 4171 */                 bounds.width += this.depth3dPoint.x;
/* 4172 */                 bounds.height -= this.depth3dPoint.y;
/*      */               }
/*      */             }
/* 4175 */             lab_x = bounds.x + (int)Math.round((bounds.width - box_width) * double_x);
/* 4176 */             lab_y = bounds.y + (int)Math.round((bounds.height - box_height) * double_y);
/*      */           }
/*      */           
/*      */ 
/* 4180 */           if ((x >= lab_x) && (x <= lab_x + box_width) && (y >= lab_y) && (y <= lab_y + box_height)) {
/* 4181 */             if ((this.chartType.equals("pie")) || (sample == null) || (serie == null)) {
/* 4182 */               return id;
/*      */             }
/* 4184 */             Point center = getSampleCenter(sample.intValue(), serie.intValue());
/* 4185 */             Rectangle grid = new Rectangle(this.graphBounds);
/* 4186 */             grid.grow(1, 1);
/* 4187 */             if (is3DModeOn()) {
/* 4188 */               grid.x -= this.depth3dPoint.x;
/* 4189 */               grid.width += this.depth3dPoint.x;
/* 4190 */               grid.height -= this.depth3dPoint.y;
/*      */             }
/* 4192 */             if ((center != null) && (grid.contains(center))) {
/* 4193 */               return id;
/*      */             }
/*      */           }
/*      */         }
/*      */       }
/*      */     }
/*      */     
/* 4200 */     return null;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public String toString()
/*      */   {
/* 4208 */     if (this.chartTitle != null) {
/* 4209 */       return getTitle();
/*      */     }
/* 4211 */     return super.toString();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void setLocale(Locale locale)
/*      */   {
/* 4221 */     this.currentLocale = locale;
/* 4222 */     if (locale != null) {
/* 4223 */       this.numberFormatter = NumberFormat.getInstance(locale);
/*      */     } else {
/* 4225 */       this.numberFormatter = NumberFormat.getInstance();
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public Locale getLocale()
/*      */   {
/* 4234 */     if (this.currentLocale != null) {
/* 4235 */       return this.currentLocale;
/*      */     }
/* 4237 */     return Locale.getDefault();
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
/*      */   public String formatNumber(double value, int decimals)
/*      */   {
/* 4251 */     if (this.currentLocale != null) {
/* 4252 */       this.numberFormatter.setMaximumFractionDigits(decimals);
/* 4253 */       this.numberFormatter.setMinimumFractionDigits(decimals);
/* 4254 */       return this.numberFormatter.format(value);
/*      */     }
/*      */     
/*      */ 
/*      */ 
/* 4259 */     long rounded = 0L;
/* 4260 */     if (value >= 0.0D) {
/* 4261 */       rounded = (value + 0.5D);
/*      */     } else {
/* 4263 */       rounded = (value - 0.5D);
/*      */     }
/*      */     
/* 4266 */     if (this.showValue_As_Time) {
/* 4267 */       if (rounded == 0L) {
/* 4268 */         return "-";
/*      */       }
/*      */       
/*      */ 
/* 4272 */       DecimalFormat myformat = new DecimalFormat("00");
/* 4273 */       String str = "";
/* 4274 */       long hour = rounded / 3600L;
/* 4275 */       long minute = rounded / 60L % 60L;
/* 4276 */       long second = rounded % 60L;
/*      */       
/* 4278 */       str = hour + ":";
/* 4279 */       str = str + myformat.format(minute) + ":";
/* 4280 */       str = str + myformat.format(second);
/*      */       
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 4291 */       return str;
/*      */     }
/*      */     
/* 4294 */     if ((decimals == 0) && (rounded < 1000L) && (rounded > -1000L)) {
/* 4295 */       return rounded;
/*      */     }
/*      */     
/*      */ 
/* 4299 */     long pow = Math.pow(10.0D, decimals);
/* 4300 */     if (pow == 0L) {
/* 4301 */       return rounded;
/*      */     }
/* 4303 */     long integer = Math.round(value * pow);
/* 4304 */     long fraction = Math.abs(integer) % pow + pow;
/* 4305 */     if ((value >= 1000000.0D) || (value <= -1000000.0D)) {
/* 4306 */       fraction = pow;
/*      */     }
/* 4308 */     String formatted = "";
/* 4309 */     if (decimals > 0) {
/* 4310 */       formatted = "." + new StringBuilder(String.valueOf(fraction)).toString().substring(1);
/*      */     }
/* 4312 */     integer /= pow;
/*      */     
/*      */ 
/* 4315 */     if (this.thousandsDelimiter == null) {
/* 4316 */       this.numberFormatter.setGroupingUsed(true);
/* 4317 */       formatted = this.numberFormatter.format(integer) + formatted;
/* 4318 */     } else if (this.thousandsDelimiter.trim().equalsIgnoreCase("off")) {
/* 4319 */       this.numberFormatter.setGroupingUsed(false);
/* 4320 */       formatted = this.numberFormatter.format(integer) + formatted;
/*      */ 
/*      */     }
/*      */     else
/*      */     {
/* 4325 */       long integer_part = integer;
/* 4326 */       if (integer_part > 0L) {
/* 4327 */         while (integer_part >= 1L) {
/* 4328 */           long n = integer_part % 1000L + 1000L;
/* 4329 */           if ((n >= 1000L) && (integer_part >= 1000L)) {
/* 4330 */             formatted = this.thousandsDelimiter + new StringBuilder(String.valueOf(n)).toString().substring(1) + formatted;
/*      */           } else {
/* 4332 */             formatted = integer_part % 1000L + formatted;
/*      */           }
/* 4334 */           integer_part /= 1000L;
/*      */         }
/* 4336 */       } else if (integer_part < 0L) {
/* 4337 */         while (integer_part <= -1L) {
/* 4338 */           long n = integer_part % 1000L - 1000L;
/* 4339 */           if ((n <= -1000L) && (integer_part <= -1000L)) {
/* 4340 */             formatted = this.thousandsDelimiter + new StringBuilder(String.valueOf(n)).toString().substring(2) + formatted;
/*      */           } else {
/* 4342 */             formatted = integer_part % 1000L + formatted;
/*      */           }
/* 4344 */           integer_part /= 1000L;
/*      */         }
/* 4346 */       } else if (integer_part == 0L) {
/* 4347 */         formatted = "0" + formatted;
/*      */       }
/*      */     }
/*      */     
/*      */ 
/* 4352 */     if ((value < 0.0D) && (integer == 0L)) {
/* 4353 */       formatted = "-" + formatted;
/*      */     }
/*      */     
/*      */ 
/* 4357 */     return formatted;
/*      */   }
/*      */   
/*      */ 
/*      */   public String formatNumber(double value, int decimals, boolean numberAsTime)
/*      */   {
/* 4363 */     if (this.currentLocale != null) {
/* 4364 */       this.numberFormatter.setMaximumFractionDigits(decimals);
/* 4365 */       this.numberFormatter.setMinimumFractionDigits(decimals);
/* 4366 */       return this.numberFormatter.format(value);
/*      */     }
/*      */     
/*      */ 
/*      */ 
/* 4371 */     long rounded = 0L;
/* 4372 */     if (value >= 0.0D) {
/* 4373 */       rounded = (value + 0.5D);
/*      */     } else {
/* 4375 */       rounded = (value - 0.5D);
/*      */     }
/*      */     
/* 4378 */     if (numberAsTime) {
/* 4379 */       if (rounded == 0L) {
/* 4380 */         return "-";
/*      */       }
/*      */       
/*      */ 
/* 4384 */       DecimalFormat myformat = new DecimalFormat("00");
/* 4385 */       String str = "";
/* 4386 */       long hour = rounded / 3600L;
/* 4387 */       long minute = rounded / 60L % 60L;
/* 4388 */       long second = rounded % 60L;
/*      */       
/* 4390 */       str = hour + ":";
/* 4391 */       str = str + myformat.format(minute) + ":";
/* 4392 */       str = str + myformat.format(second);
/*      */       
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 4403 */       return str;
/*      */     }
/*      */     
/* 4406 */     if ((decimals == 0) && (rounded < 1000L) && (rounded > -1000L)) {
/* 4407 */       return rounded;
/*      */     }
/*      */     
/*      */ 
/* 4411 */     long pow = Math.pow(10.0D, decimals);
/* 4412 */     if (pow == 0L) {
/* 4413 */       return rounded;
/*      */     }
/* 4415 */     long integer = Math.round(value * pow);
/* 4416 */     long fraction = Math.abs(integer) % pow + pow;
/* 4417 */     if ((value >= 1000000.0D) || (value <= -1000000.0D)) {
/* 4418 */       fraction = pow;
/*      */     }
/* 4420 */     String formatted = "";
/* 4421 */     if (decimals > 0) {
/* 4422 */       formatted = "." + new StringBuilder(String.valueOf(fraction)).toString().substring(1);
/*      */     }
/* 4424 */     integer /= pow;
/*      */     
/*      */ 
/* 4427 */     if (this.thousandsDelimiter == null) {
/* 4428 */       this.numberFormatter.setGroupingUsed(true);
/* 4429 */       formatted = this.numberFormatter.format(integer) + formatted;
/* 4430 */     } else if (this.thousandsDelimiter.trim().equalsIgnoreCase("off")) {
/* 4431 */       this.numberFormatter.setGroupingUsed(false);
/* 4432 */       formatted = this.numberFormatter.format(integer) + formatted;
/*      */ 
/*      */     }
/*      */     else
/*      */     {
/* 4437 */       long integer_part = integer;
/* 4438 */       if (integer_part > 0L) {
/* 4439 */         while (integer_part >= 1L) {
/* 4440 */           long n = integer_part % 1000L + 1000L;
/* 4441 */           if ((n >= 1000L) && (integer_part >= 1000L)) {
/* 4442 */             formatted = this.thousandsDelimiter + new StringBuilder(String.valueOf(n)).toString().substring(1) + formatted;
/*      */           } else {
/* 4444 */             formatted = integer_part % 1000L + formatted;
/*      */           }
/* 4446 */           integer_part /= 1000L;
/*      */         }
/* 4448 */       } else if (integer_part < 0L) {
/* 4449 */         while (integer_part <= -1L) {
/* 4450 */           long n = integer_part % 1000L - 1000L;
/* 4451 */           if ((n <= -1000L) && (integer_part <= -1000L)) {
/* 4452 */             formatted = this.thousandsDelimiter + new StringBuilder(String.valueOf(n)).toString().substring(2) + formatted;
/*      */           } else {
/* 4454 */             formatted = integer_part % 1000L + formatted;
/*      */           }
/* 4456 */           integer_part /= 1000L;
/*      */         }
/* 4458 */       } else if (integer_part == 0L) {
/* 4459 */         formatted = "0" + formatted;
/*      */       }
/*      */     }
/*      */     
/*      */ 
/* 4464 */     if ((value < 0.0D) && (integer == 0L)) {
/* 4465 */       formatted = "-" + formatted;
/*      */     }
/*      */     
/*      */ 
/* 4469 */     return formatted;
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
/*      */   public void setGraphInsets(int top, int left, int bottom, int right)
/*      */   {
/* 4482 */     this.graphInsets.top = top;
/* 4483 */     this.graphInsets.left = left;
/* 4484 */     this.graphInsets.bottom = bottom;
/* 4485 */     this.graphInsets.right = right;
/* 4486 */     this.needRender = true;
/* 4487 */     this.needGraphBounds = true;
/* 4488 */     this.needChartCalculation = true;
/* 4489 */     autoRepaint();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public Insets getGraphInsets()
/*      */   {
/* 4498 */     if (this.graphInsets != null) {
/* 4499 */       return this.graphInsets;
/*      */     }
/* 4501 */     return new Insets(-1, -1, -1, -1);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void setGridAdjustmentOn(int edge, boolean on)
/*      */   {
/* 4512 */     if ((edge >= 0) && (edge < this.gridAdjustment.length)) {
/* 4513 */       this.gridAdjustment[edge] = on;
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public boolean isGridAdjustmentOn(int edge)
/*      */   {
/* 4524 */     if ((edge >= 0) && (edge < this.gridAdjustment.length)) {
/* 4525 */       return this.gridAdjustment[edge];
/*      */     }
/* 4527 */     return false;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void addImage(String name, Image image)
/*      */   {
/* 4538 */     if (name != null) {
/*      */       try
/*      */       {
/* 4541 */         if (image != null) {
/* 4542 */           this.images.put(name, image);
/* 4543 */           this.imageTracker.addImage(image, 0);
/* 4544 */           this.imageTracker.waitForAll();
/*      */         }
/*      */         else
/*      */         {
/* 4548 */           this.images.remove(name);
/*      */         }
/*      */       } catch (InterruptedException e) {
/* 4551 */         e.printStackTrace();
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
/*      */   public Image loadImage(String name)
/*      */   {
/* 4564 */     if (name == null) {
/* 4565 */       return null;
/*      */     }
/*      */     
/*      */ 
/* 4569 */     Toolkit toolkit = Toolkit.getDefaultToolkit();
/* 4570 */     Image image = toolkit.getImage(name);
/* 4571 */     MediaTracker tracker = new MediaTracker(this);
/* 4572 */     int id = (int)(Math.random() * 2.147483647E9D);
/* 4573 */     tracker.addImage(image, id);
/*      */     try {
/* 4575 */       tracker.waitForID(id);
/*      */     } catch (InterruptedException e) {
/* 4577 */       System.out.println("Could not load image: " + name);
/*      */     }
/* 4579 */     return image;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public Image getImage(String name)
/*      */   {
/* 4589 */     return (Image)this.images.get(name);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void setServletModeOn(boolean on)
/*      */   {
/* 4600 */     this.servletModeOn = on;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public boolean isServletModeOn()
/*      */   {
/* 4609 */     return this.servletModeOn;
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
/*      */   public void addItemListener(ItemListener l)
/*      */   {
/* 4626 */     if (l != null) {
/* 4627 */       this.listeners.addElement(l);
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void removeItemListener(ItemListener l)
/*      */   {
/* 4638 */     if ((l != null) && (this.listeners != null)) {
/* 4639 */       this.listeners.removeElement(l);
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
/*      */   public int addOverlayChart(Chart chart)
/*      */   {
/* 4657 */     if (chart != null) {
/* 4658 */       chart.overlayChartOn = true;
/* 4659 */       chart.parentChart = this;
/* 4660 */       this.overlayCharts.addElement(chart);
/* 4661 */       chart.setRange(0, getRange(0));
/* 4662 */       chart.setRange(1, getRange(1));
/* 4663 */       chart.setLowerRange(0, getLowerRange(0));
/* 4664 */       chart.setLowerRange(1, getLowerRange(1));
/* 4665 */       this.needRender = true;
/* 4666 */       autoRepaint();
/* 4667 */       return this.overlayCharts.lastIndexOf(chart);
/*      */     }
/* 4669 */     return 0;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void removeOverlayChart(int index)
/*      */   {
/* 4681 */     if ((index >= 0) && (index < this.overlayCharts.size())) {
/* 4682 */       this.overlayCharts.removeElementAt(index);
/* 4683 */       this.needRender = true;
/* 4684 */       autoRepaint();
/*      */     } else {
/* 4686 */       throw new IllegalArgumentException("invalid overlay index: " + index);
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
/*      */   public void setOverlayChart(int index, Chart chart)
/*      */   {
/* 4699 */     if (chart != null) {
/* 4700 */       int count = this.overlayCharts.size();
/* 4701 */       if ((index >= 0) && (index <= count)) {
/* 4702 */         if (index == count) {
/* 4703 */           this.overlayCharts.addElement(chart);
/*      */         } else {
/* 4705 */           this.overlayCharts.setElementAt(chart, index);
/*      */         }
/* 4707 */         for (int i = 0; i < this.rangeOn.length; i++) {
/* 4708 */           chart.setRange(i, getRange(i));
/* 4709 */           chart.setLowerRange(i, getLowerRange(i));
/*      */         }
/* 4711 */         this.needRender = true;
/* 4712 */         autoRepaint();
/*      */       } else {
/* 4714 */         throw new IllegalArgumentException("Invalid index for overlay chart: " + index);
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public Chart getOverlayChart(int index)
/*      */   {
/* 4726 */     if ((index >= 0) && (index < this.overlayCharts.size())) {
/* 4727 */       return (Chart)this.overlayCharts.elementAt(index);
/*      */     }
/* 4729 */     return null;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void setOverlayChartOn(int index, boolean on)
/*      */   {
/* 4739 */     Chart chart = getOverlayChart(index);
/* 4740 */     if (chart != null) {
/* 4741 */       chart.overlayChartOn = on;
/*      */     }
/* 4743 */     this.needRender = true;
/* 4744 */     autoRepaint();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public boolean isOverlayChartOn(int index)
/*      */   {
/* 4754 */     Chart chart = getOverlayChart(index);
/* 4755 */     if (chart != null) {
/* 4756 */       return chart.overlayChartOn;
/*      */     }
/* 4758 */     return false;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public Dimension getSize()
/*      */   {
/* 4769 */     if (this.parentChart != null) {
/* 4770 */       return this.parentChart.getSize();
/*      */     }
/* 4772 */     return super.getSize();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void setPreferredSize(int width, int height)
/*      */   {
/* 4784 */     this.preferredSize.width = width;
/* 4785 */     this.preferredSize.height = height;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public Dimension getPreferredSize()
/*      */   {
/* 4794 */     return new Dimension(this.preferredSize);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public Dimension getMinimumSize()
/*      */   {
/* 4803 */     return new Dimension(100, 70);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public Image getImage(int width, int height)
/*      */   {
/* 4814 */     width = Math.max(1, width);
/* 4815 */     height = Math.max(1, height);
/* 4816 */     Dimension oldSize = getSize();
/* 4817 */     Image image = createImage(width, height);
/* 4818 */     if (image != null) {
/* 4819 */       Graphics g = image.getGraphics();
/* 4820 */       if (g != null)
/*      */       {
/*      */ 
/*      */ 
/* 4824 */         setSize(width, height);
/* 4825 */         boolean servlet_on = isServletModeOn();
/* 4826 */         setServletModeOn(true);
/* 4827 */         paint(g);
/* 4828 */         setServletModeOn(servlet_on);
/*      */       }
/*      */     }
/* 4831 */     setSize(oldSize.width, oldSize.height);
/* 4832 */     return image;
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
/*      */   public Image createImage(int width, int height)
/*      */   {
/* 4855 */     boolean is_java2 = !System.getProperty("java.version").startsWith("1.1");
/* 4856 */     if (is_java2) {
/* 4857 */       if (!this.bufferedImageClass_tried_loading) {
/*      */         try {
/* 4859 */           this.bufferedImageClass = Class.forName("java.awt.image.BufferedImage");
/* 4860 */           Class[] parameters = { Integer.TYPE, Integer.TYPE, Integer.TYPE };
/* 4861 */           this.bufferedImageConstructor = this.bufferedImageClass.getConstructor(parameters);
/* 4862 */           Field field = this.bufferedImageClass.getField("TYPE_INT_RGB");
/* 4863 */           this.TYPE_INT_RGB = field.getInt(null);
/*      */ 
/*      */         }
/*      */         catch (ClassNotFoundException localClassNotFoundException) {}catch (NoSuchMethodException e)
/*      */         {
/* 4868 */           this.bufferedImageClass = null;
/*      */         }
/*      */         catch (NoSuchFieldException localNoSuchFieldException) {}catch (IllegalAccessException localIllegalAccessException) {}
/*      */         
/* 4872 */         this.bufferedImageClass_tried_loading = true;
/*      */       }
/*      */     } else {
/* 4875 */       this.bufferedImageClass_tried_loading = true;
/*      */     }
/*      */     
/*      */ 
/* 4879 */     width = Math.max(1, width);
/* 4880 */     height = Math.max(1, height);
/* 4881 */     Image image = null;
/*      */     
/*      */ 
/* 4884 */     if (this.bufferedImageClass != null) {
/* 4885 */       try { Object[] parameters = { new Integer(width), new Integer(height), new Integer(this.TYPE_INT_RGB) };
/* 4886 */         image = (Image)this.bufferedImageConstructor.newInstance(parameters);
/*      */       }
/*      */       catch (InstantiationException localInstantiationException) {}catch (IllegalAccessException localIllegalAccessException1) {}catch (InvocationTargetException localInvocationTargetException) {}
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/* 4894 */     if (image == null) {
/* 4895 */       image = super.createImage(width, height);
/* 4896 */       if (image == null) {
/* 4897 */         if (this.servletFrame == null) {
/* 4898 */           this.servletFrame = new Frame();
/* 4899 */           this.servletFrame.addNotify();
/*      */         }
/* 4901 */         image = this.servletFrame.createImage(width, height);
/* 4902 */         this.servletFrame.removeNotify();
/*      */       }
/*      */     }
/* 4905 */     return image;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public Image createImage_oldstyle(int width, int height)
/*      */   {
/* 4916 */     width = Math.max(1, width);
/* 4917 */     height = Math.max(1, height);
/* 4918 */     Image image = super.createImage(width, height);
/* 4919 */     if (image == null) {
/* 4920 */       if (this.servletFrame == null) {
/* 4921 */         this.servletFrame = new Frame();
/* 4922 */         this.servletFrame.addNotify();
/*      */       }
/* 4924 */       image = this.servletFrame.createImage(width, height);
/* 4925 */       this.servletFrame.removeNotify();
/*      */     }
/* 4927 */     return image;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void setExternalGraphics(Graphics g, Image image)
/*      */   {
/* 4939 */     this.externalGraphicsOn = true;
/* 4940 */     this.offscreen = image;
/* 4941 */     this.external_gc = g;
/* 4942 */     this.needRender = true;
/* 4943 */     autoRepaint();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void removeExternalGraphics()
/*      */   {
/* 4952 */     this.externalGraphicsOn = false;
/* 4953 */     this.needRender = true;
/* 4954 */     autoRepaint();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public void autoRepaint()
/*      */   {
/* 4962 */     if (this.parentChart != null) {
/* 4963 */       this.parentChart.autoRepaint();
/*      */     }
/* 4965 */     else if (this.automaticRepaintOn) {
/* 4966 */       repaint();
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void forceRepaint()
/*      */   {
/* 4976 */     this.needRender = true;
/* 4977 */     this.needGraphBounds = true;
/* 4978 */     this.needChartCalculation = true;
/* 4979 */     repaint();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public final void update(Graphics g)
/*      */   {
/* 4987 */     paint(g);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public final void paint(Graphics g)
/*      */   {
/* 4999 */     if (g == null) {
/* 5000 */       return;
/*      */     }
/*      */     
/*      */ 
/* 5004 */     Dimension size = getSize();
/*      */     
/*      */ 
/* 5007 */     if (this.c2 != null) {
/* 5008 */       if (this.chartData.hasChangedSince(this.lastRenderTime)) {
/* 5009 */         this.needRender = true;
/* 5010 */         this.needChartCalculation = true;
/* 5011 */         checkDataIntegrity();
/*      */       }
/*      */       
/* 5014 */       for (int i = 0; i < this.overlayCharts.size(); i++) {
/* 5015 */         Chart overlay = (Chart)this.overlayCharts.elementAt(i);
/* 5016 */         if ((overlay.chartData != null) && (overlay.chartData.hasChangedSince(this.lastRenderTime))) {
/* 5017 */           this.needRender = (overlay.needRender = 1);
/* 5018 */           this.needChartCalculation = (overlay.needChartCalculation = 1);
/*      */         }
/* 5020 */         if (overlay.needRender) {
/* 5021 */           this.needRender = overlay.needRender;
/*      */         }
/* 5023 */         if (overlay.needChartCalculation) {
/* 5024 */           this.needChartCalculation = true;
/*      */         }
/*      */       }
/* 5027 */       render(g);
/* 5028 */       this.lastRenderTime = System.currentTimeMillis();
/* 5029 */       this.c2.paint(g, size);
/*      */     }
/*      */     
/*      */ 
/* 5033 */     if (this.displayVersionOn) {
/* 5034 */       g.setColor(Color.white);
/* 5035 */       g.fillRect(0, 0, size.width, 20);
/* 5036 */       g.setColor(Color.black);
/* 5037 */       g.setFont(new Font("Arial", 0, 12));
/* 5038 */       g.drawString("EasyCharts " + getVersion() + ", Copyright 1998-2004, ObjectPlanet, Inc.", 0, 12);
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void setPrintAsBitmap(boolean on)
/*      */   {
/* 5050 */     this.printAsBitmap = on;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public boolean isPrintAsBitmap()
/*      */   {
/* 5059 */     return this.printAsBitmap;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public final void print(Graphics g)
/*      */   {
/* 5070 */     Dimension size = getSize();
/* 5071 */     g.setColor(getBackground());
/* 5072 */     g.fillRect(0, 0, size.width, size.height);
/* 5073 */     g.setColor(getForeground());
/*      */     
/*      */ 
/* 5076 */     this.needRender = true;
/* 5077 */     this.needGraphBounds = true;
/* 5078 */     this.needChartCalculation = true;
/* 5079 */     render(g, false);
/* 5080 */     if (this.printAsBitmap) {
/* 5081 */       render(g, true);
/*      */     }
/*      */     
/*      */ 
/* 5085 */     this.c2.paint(g, size);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected abstract void render(Graphics paramGraphics);
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   void render(Graphics g, boolean offscreenOn) {}
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected abstract void calculateChartData(Rectangle paramRectangle1, Rectangle paramRectangle2);
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected abstract void renderData(Graphics paramGraphics, Rectangle paramRectangle1, Rectangle paramRectangle2);
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected abstract Point getSampleCenter(int paramInt1, int paramInt2);
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public abstract Rectangle getGraphBounds();
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public Rectangle getDataBounds(Rectangle grid)
/*      */   {
/* 5145 */     if (grid == null) {
/* 5146 */       throw new IllegalArgumentException("Grid is NULL");
/*      */     }
/*      */     
/*      */ 
/* 5150 */     this.chartDataBounds.setBounds(grid);
/* 5151 */     double visible = 1.0D - this.leftScrollerFactor - this.rightScrollerFactor;
/* 5152 */     if (this.gridAlignment == 1) {
/* 5153 */       this.chartDataBounds.width = ((int)Math.round(grid.width / visible)); Rectangle 
/* 5154 */         tmp65_62 = this.chartDataBounds;tmp65_62.x = ((int)(tmp65_62.x - Math.round(this.leftScrollerFactor * this.chartDataBounds.width)));
/*      */     } else {
/* 5156 */       this.chartDataBounds.height = ((int)Math.round(grid.height / visible)); Rectangle 
/* 5157 */         tmp116_113 = this.chartDataBounds;tmp116_113.y = ((int)(tmp116_113.y - Math.round(this.leftScrollerFactor * this.chartDataBounds.height)));
/*      */     }
/* 5159 */     return this.chartDataBounds;
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
/*      */   Rectangle getGraphBounds(String[] myLabels)
/*      */   {
/* 5174 */     if (this.labels == null) {
/* 5175 */       throw new IllegalArgumentException("Labels is NULL");
/*      */     }
/*      */     
/* 5178 */     String[] labels = (String[])myLabels.clone();
/* 5179 */     if ((getSeriesCount() == 1) && (this.chartType.equalsIgnoreCase("pie"))) {
/* 5180 */       for (int i = 0; i < labels.length; i++) {
/* 5181 */         int tmp54_53 = i; String[] tmp54_52 = labels;tmp54_52[tmp54_53] = (tmp54_52[tmp54_53] + " (" + formatNumber(getSampleValue(0, i), getSampleDecimalCount(0)) + ")");
/*      */       }
/*      */     }
/*      */     
/* 5185 */     Dimension size = getSize();
/* 5186 */     Rectangle bounds = new Rectangle(10, 10, size.width - 20, size.height - 20);
/* 5187 */     FontMetrics fm = getFontMetrics(getFont("legendFont"));
/*      */     
/*      */ 
/* 5190 */     Dimension titleSize = null;
/* 5191 */     if (this.chartTitleOn) {
/* 5192 */       titleSize = getLabelSize(this.chartTitle, getFontMetrics(getFont("titleFont")));
/* 5193 */       bounds.y += titleSize.height;
/* 5194 */       bounds.height -= titleSize.height;
/*      */     }
/*      */     
/*      */ 
/* 5198 */     if (this.legendOn)
/*      */     {
/*      */ 
/* 5201 */       int columns = getLegendColumns();
/* 5202 */       int rest = 0;
/* 5203 */       int rows; int rows; if (columns == 0) { int rows;
/* 5204 */         if ((this.legendPosition == 1) || (this.legendPosition == 0)) {
/* 5205 */           columns = 1;
/* 5206 */           rows = labels.length;
/*      */         } else {
/* 5208 */           columns = labels.length;
/* 5209 */           rows = 1;
/*      */         }
/*      */       } else {
/* 5212 */         rows = labels.length / columns;
/* 5213 */         rest = labels.length % columns;
/* 5214 */         if (rest > 0) {
/* 5215 */           rows++;
/*      */         }
/*      */       }
/*      */       
/*      */ 
/* 5220 */       Font legendFont = getFont("legendFont");
/* 5221 */       fm = getFontMetrics(legendFont);
/* 5222 */       this.legend.width = 0;
/* 5223 */       this.legend.height = 0;
/*      */       
/*      */ 
/* 5226 */       int index = 0;
/* 5227 */       if (this.legendReverseOn) {
/* 5228 */         index = labels.length - 1;
/*      */       }
/* 5230 */       this.legendRowHeight = new int[rows];
/* 5231 */       this.legendColumnWidth = new int[columns];
/* 5232 */       for (int i = 0; i < columns; i++) {
/* 5233 */         for (int j = 0; j < rows; j++)
/*      */         {
/* 5235 */           if ((rest <= 0) || (i < rest) || (j != rows - 1))
/*      */           {
/*      */ 
/*      */ 
/* 5239 */             Dimension labelSize = getLabelSize(labels[index], fm);
/* 5240 */             Dimension imageSize = getImageSize(getLegendImage(index));
/* 5241 */             imageSize.height = (imageSize.height > 0 ? imageSize.height : 6);
/* 5242 */             imageSize.width = (imageSize.width > 0 ? imageSize.width : 6);
/* 5243 */             int width = labelSize.width + imageSize.width + 12;
/* 5244 */             int height = Math.max(Math.max(imageSize.height + 6, labelSize.height + 2), 13);
/*      */             
/* 5246 */             this.legendColumnWidth[i] = Math.max(this.legendColumnWidth[i], width);
/* 5247 */             this.legendRowHeight[j] = Math.max(this.legendRowHeight[j], height);
/* 5248 */             if (!this.legendReverseOn) {
/* 5249 */               index++;
/*      */             } else {
/* 5251 */               index--;
/*      */             }
/*      */           }
/*      */         }
/* 5255 */         this.legend.width += this.legendColumnWidth[i];
/*      */       }
/*      */       
/* 5258 */       for (int j = 0; j < rows; j++) {
/* 5259 */         this.legend.height += this.legendRowHeight[j];
/*      */       }
/*      */       
/*      */ 
/*      */ 
/* 5264 */       switch (this.legendPosition) {
/*      */       case 1: 
/*      */       default: 
/* 5267 */         this.legend.x = (size.width - this.legend.width - 1);
/* 5268 */         this.legend.y = (size.height / 2 - this.legend.height / 2);
/* 5269 */         bounds.width -= this.legend.width + 5;
/* 5270 */         if (this.legend.height > size.height - this.legend.y - 12) {
/* 5271 */           this.legend.x -= 9;
/* 5272 */           bounds.width -= 9;
/*      */         }
/* 5274 */         this.legend.y = Math.max(this.legend.y, 10);
/* 5275 */         break;
/*      */       case 0: 
/* 5277 */         this.legend.x = 6;
/* 5278 */         this.legend.y = (size.height / 2 - this.legend.height / 2);
/* 5279 */         bounds.x += this.legend.width + 10;
/* 5280 */         bounds.width -= this.legend.width + 10;
/* 5281 */         if (this.legend.height > size.height - this.legend.y - 12) {
/* 5282 */           bounds.x += 9;
/* 5283 */           bounds.width -= 9;
/*      */         }
/* 5285 */         this.legend.y = Math.max(this.legend.y, 10);
/* 5286 */         break;
/*      */       case 2: 
/* 5288 */         this.legend.x = (size.width / 2 - this.legend.width / 2);
/* 5289 */         this.legend.y = (titleSize != null ? titleSize.height + 7 : 7);
/* 5290 */         bounds.y += this.legend.height + fm.getDescent();
/* 5291 */         bounds.height -= this.legend.height + fm.getDescent();
/* 5292 */         if (this.legend.width > size.width - this.legend.x - 10) {
/* 5293 */           bounds.y += 12;
/* 5294 */           bounds.height -= 12;
/*      */         }
/* 5296 */         this.legend.x = Math.max(this.legend.x, 10);
/* 5297 */         break;
/*      */       case 3: 
/* 5299 */         this.legend.x = (size.width / 2 - this.legend.width / 2);
/* 5300 */         this.legend.y = (size.height - this.legend.height - 5);
/* 5301 */         bounds.height -= this.legend.height + 2;
/* 5302 */         if (this.legend.width > size.width - this.legend.x - 10) {
/* 5303 */           bounds.height -= 12;
/* 5304 */           this.legend.y -= 12;
/*      */         }
/* 5306 */         this.legend.x = Math.max(this.legend.x, 10);
/*      */       }
/*      */       
/*      */     }
/* 5310 */     return bounds;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   void calculateRangeBounds(Rectangle grid)
/*      */   {
/* 5320 */     for (int i = 0; i < this.rangeBounds.length; i++) {
/* 5321 */       this.rangeBounds[i] = new Rectangle();
/*      */     }
/*      */     
/*      */ 
/* 5325 */     boolean leftmost = true;
/* 5326 */     boolean rightmost = true;
/* 5327 */     boolean highest = true;
/* 5328 */     boolean lowest = true;
/* 5329 */     for (int index = this.rangeOn.length - 1; index >= 0; index--) {
/* 5330 */       if (this.rangeOn[index] != 0)
/*      */       {
/* 5332 */         int widest = getRangeWidth(index, true, true);
/*      */         
/* 5334 */         String rangeAxisLabel = null;
/* 5335 */         int angle = 0;
/* 5336 */         if (index == 0) {
/* 5337 */           rangeAxisLabel = getLabel("rangeAxisLabel");
/* 5338 */           angle = getLabelAngle("rangeAxisLabelAngle");
/* 5339 */         } else if (index > 0) {
/* 5340 */           rangeAxisLabel = getLabel("rangeAxisLabel_" + (index + 1));
/* 5341 */           angle = getLabelAngle("rangeAxisLabelAngle_" + (index + 1));
/*      */         }
/* 5343 */         angle = this.gridAlignment == 1 ? angle : 0;
/* 5344 */         int labelWidth = 0;
/* 5345 */         int labelHeight = 0;
/* 5346 */         if (rangeAxisLabel != null) {
/* 5347 */           Font rangeAxisLabelFont = getFont("rangeAxisLabelFont");
/* 5348 */           Dimension labelSize = getLabelSize(rangeAxisLabel, getFontMetrics(rangeAxisLabelFont));
/* 5349 */           Dimension angledSize = getAngledLabelSize(labelSize, angle);
/* 5350 */           labelWidth = angledSize.width + 5;
/* 5351 */           labelHeight = angledSize.height;
/*      */         }
/*      */         
/*      */ 
/*      */ 
/* 5356 */         if (this.rangePosition[index] == 0) {
/* 5357 */           if (this.gridAlignment == 1) {
/* 5358 */             this.rangeBounds[index].width = (widest + 2 + labelWidth);
/* 5359 */             if ((this.rangeAdjusterOn[index] != 0) && (getAdjusterPosition(index) == this.rangePosition[index])) {
/* 5360 */               this.rangeBounds[index].width += 6;
/*      */             }
/* 5362 */             this.rangeBounds[index].width += (leftmost ? 0 : 15);
/* 5363 */             leftmost = false;
/*      */           } else {
/* 5365 */             this.rangeBounds[index].height = (widest + 2 + labelHeight);
/* 5366 */             if ((this.rangeAdjusterOn[index] != 0) && (getAdjusterPosition(index) == this.rangePosition[index])) {
/* 5367 */               this.rangeBounds[index].y -= 6;
/* 5368 */               this.rangeBounds[index].height += 6;
/*      */             }
/* 5370 */             this.rangeBounds[index].height += (highest ? 0 : 10);
/* 5371 */             highest = false;
/*      */           }
/*      */           
/*      */         }
/* 5375 */         else if (this.gridAlignment == 1) {
/* 5376 */           this.rangeBounds[index].width = ((widest > 0 ? widest + 2 : 0) + labelWidth);
/* 5377 */           if ((this.rangeAdjusterOn[index] != 0) && (getAdjusterPosition(index) == this.rangePosition[index])) {
/* 5378 */             this.rangeBounds[index].width += 6;
/*      */           }
/* 5380 */           this.rangeBounds[index].width += (rightmost ? 0 : 15);
/* 5381 */           rightmost = false;
/*      */         } else {
/* 5383 */           this.rangeBounds[index].height = (widest + 2 + labelHeight);
/* 5384 */           if ((this.rangeAdjusterOn[index] != 0) && (getAdjusterPosition(index) == this.rangePosition[index])) {
/* 5385 */             this.rangeBounds[index].height += 6;
/*      */           }
/* 5387 */           this.rangeBounds[index].height += (lowest ? 0 : 10);
/* 5388 */           lowest = false;
/*      */         }
/*      */       }
/*      */     }
/*      */     
/*      */ 
/*      */ 
/* 5395 */     int left_offset = grid.x;
/* 5396 */     int right_offset = grid.x + grid.width;
/* 5397 */     if (this.gridAlignment == 0) {
/* 5398 */       left_offset = grid.y + grid.height;
/* 5399 */       right_offset = grid.y;
/*      */     }
/* 5401 */     for (int index = this.rangeBounds.length - 1; index >= 0; index--) {
/* 5402 */       if (this.gridAlignment == 1) {
/* 5403 */         if (this.rangePosition[index] == 0) {
/* 5404 */           this.rangeBounds[index].x = left_offset;
/* 5405 */           left_offset += this.rangeBounds[index].width;
/*      */         } else {
/* 5407 */           this.rangeBounds[index].x = (right_offset - this.rangeBounds[index].width);
/* 5408 */           right_offset -= this.rangeBounds[index].width;
/*      */         }
/*      */       }
/* 5411 */       else if (this.rangePosition[index] == 0) {
/* 5412 */         this.rangeBounds[index].y = (left_offset - this.rangeBounds[index].height);
/* 5413 */         left_offset -= this.rangeBounds[index].height;
/*      */       } else {
/* 5415 */         this.rangeBounds[index].y = right_offset;
/* 5416 */         right_offset += this.rangeBounds[index].height;
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
/*      */   void calculateZeroLines(Rectangle grid)
/*      */   {
/* 5429 */     for (int rangeIndex = 0; rangeIndex < this.zeroLine.length; rangeIndex++) {
/* 5430 */       if (this.currentUpperRange[rangeIndex] - this.currentLowerRange[rangeIndex] != 0.0D) {
/* 5431 */         if (this.gridAlignment == 1) {
/* 5432 */           int positiveSpace = (int)Math.round(grid.height * (this.currentUpperRange[rangeIndex] / (this.currentUpperRange[rangeIndex] - this.currentLowerRange[rangeIndex])));
/* 5433 */           this.zeroLine[rangeIndex] = (grid.y + positiveSpace);
/*      */         } else {
/* 5435 */           int positiveSpace = (int)Math.round(grid.width * (this.currentUpperRange[rangeIndex] / (this.currentUpperRange[rangeIndex] - this.currentLowerRange[rangeIndex])));
/* 5436 */           this.zeroLine[rangeIndex] = (grid.x + grid.width - positiveSpace);
/*      */         }
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
/*      */ 
/*      */ 
/*      */ 
/*      */   protected ChartSample checkSelection(Point point)
/*      */   {
/* 5458 */     if ((this.legendBounds == null) || (!this.legendOn)) {
/* 5459 */       return null;
/*      */     }
/*      */     
/*      */ 
/*      */ 
/* 5464 */     this.mouseOverSampleIndex = -1;
/* 5465 */     this.mouseOverSeriesIndex = -1;
/*      */     
/*      */ 
/*      */ 
/* 5469 */     ChartSample sample = null;
/* 5470 */     if (this.overlayCharts != null) {
/* 5471 */       int overlay_count = this.overlayCharts.size();
/* 5472 */       for (int i = overlay_count - 1; i >= 0; i--) {
/* 5473 */         Chart overlay = (Chart)this.overlayCharts.elementAt(i);
/* 5474 */         if ((overlay != null) && (sample == null)) {
/* 5475 */           sample = overlay.checkSelection(point);
/*      */         }
/*      */       }
/*      */     }
/*      */     
/*      */ 
/* 5481 */     if (sample != null) {
/* 5482 */       return sample;
/*      */     }
/*      */     
/*      */ 
/* 5486 */     int seriesCount = getSeriesCount();
/* 5487 */     int sampleCount = getSampleCount();
/* 5488 */     if ((sampleCount > 0) && (seriesCount > 0) && (this.visibleLegend.contains(point))) {
/* 5489 */       for (int i = 0; (i < this.legendBounds.length) && ((i < sampleCount) || ((this.chartType.equals("pie")) && (i < seriesCount))); i++) {
/* 5490 */         if ((this.legendBounds[i] != null) && (this.legendBounds[i].contains(point))) {
/* 5491 */           if ((seriesCount > 1) || (this.chartType.equals("line")) || (this.multiSeriesOn)) {
/* 5492 */             sample = new ChartSample(-1);
/* 5493 */             sample.setSeries(i);
/* 5494 */             break; }
/* 5495 */           sample = getSample(0, i);
/*      */           
/* 5497 */           break;
/*      */         }
/*      */       }
/*      */     }
/* 5501 */     return sample;
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
/*      */   void paintGrid(Graphics g, Rectangle grid)
/*      */   {
/* 5515 */     if ((g == null) || (grid == null)) {
/* 5516 */       return;
/*      */     }
/*      */     
/*      */ 
/* 5520 */     if (this.depth3d > -1) {
/* 5521 */       this.depth3dPoint.x = this.depth3d;
/* 5522 */       this.depth3dPoint.y = (-this.depth3d);
/*      */     }
/*      */     
/*      */ 
/* 5526 */     Polygon left = new Polygon();
/* 5527 */     left.addPoint(grid.x, grid.y);
/* 5528 */     left.addPoint(grid.x - this.depth3dPoint.x, grid.y - this.depth3dPoint.y);
/* 5529 */     left.addPoint(left.xpoints[1], left.ypoints[1] + grid.height);
/* 5530 */     left.addPoint(grid.x, grid.y + grid.height);
/* 5531 */     left.addPoint(grid.x, grid.y);
/*      */     
/*      */ 
/* 5534 */     Polygon bottom = new Polygon();
/* 5535 */     bottom.addPoint(grid.x, grid.y + grid.height);
/* 5536 */     bottom.addPoint(left.xpoints[2], left.ypoints[2]);
/* 5537 */     bottom.addPoint(bottom.xpoints[1] + grid.width, bottom.ypoints[1]);
/* 5538 */     bottom.addPoint(grid.x + grid.width, grid.y + grid.height);
/* 5539 */     bottom.addPoint(bottom.xpoints[0], bottom.ypoints[0]);
/*      */     
/*      */ 
/* 5542 */     Color chartBackground = getChartBackground();
/* 5543 */     g.setColor(chartBackground);
/* 5544 */     g.fillRect(grid.x, grid.y, grid.width, grid.height);
/* 5545 */     if (this.display3dOn) {
/* 5546 */       g.setColor(this.gridAlignment == 1 ? chartBackground : chartBackground.darker());
/* 5547 */       g.fillPolygon(left);
/* 5548 */       g.setColor(this.gridAlignment == 0 ? chartBackground : chartBackground.darker());
/* 5549 */       g.fillPolygon(bottom);
/*      */     }
/*      */     
/*      */ 
/* 5553 */     Dimension image_size = getImageSize(this.gridImage);
/* 5554 */     if ((this.gridImage != null) && (image_size.height > 0) && (image_size.width > 0)) {
/* 5555 */       Image image = (Image)this.images.get(this.gridImage);
/* 5556 */       if (image != null) {
/* 5557 */         g.drawImage(image, grid.x, grid.y, grid.width, grid.height, this);
/*      */       }
/*      */     }
/*      */     
/*      */ 
/* 5562 */     g.setColor(this.chartForeground);
/* 5563 */     if (this.display3dOn) {
/* 5564 */       g.drawPolygon(left);
/* 5565 */       g.drawPolygon(bottom);
/*      */     }
/* 5567 */     g.drawRect(grid.x, grid.y, grid.width, grid.height);
/*      */     
/*      */ 
/*      */ 
/* 5571 */     for (int i = this.rangeOn.length - 1; i >= 0; i--) {
/* 5572 */       paintRange(g, grid, i);
/*      */     }
/*      */     
/*      */ 
/* 5576 */     g.setColor(this.chartForeground);
/* 5577 */     if (getLabel("sampleAxisLabel") != null)
/*      */     {
/* 5579 */       Dimension sampleLabel = new Dimension();
/* 5580 */       boolean adjust_for_sample = this.sampleLabelsOn;
/* 5581 */       adjust_for_sample &= ((this.sampleLabelStyle == 2) || (this.sampleLabelStyle == 4));
/* 5582 */       if (((this.chartType.equals("bar")) && (this.barLabelsOn)) || (adjust_for_sample))
/*      */       {
/* 5584 */         Font font = getFont();
/* 5585 */         int angle = 0;
/* 5586 */         if (this.chartType.equals("bar")) {
/* 5587 */           font = getFont("barLabelFont");
/* 5588 */           angle = getLabelAngle("barLabelAngle");
/* 5589 */         } else if (this.chartType.equals("line")) {
/* 5590 */           font = getFont("sampleLabelFont");
/* 5591 */           angle = getLabelAngle("sampleLabelAngle");
/*      */         }
/*      */         
/* 5594 */         String[] labels = this.barLabels != null ? this.barLabels : getSampleLabels();
/* 5595 */         FontMetrics sample_label_fm = getFontMetrics(font);
/* 5596 */         for (int i = 0; i < labels.length; i++) {
/* 5597 */           Dimension labelSize = getLabelSize(labels[i], sample_label_fm);
/* 5598 */           Dimension angledSize = getAngledLabelSize(labelSize, angle);
/* 5599 */           sampleLabel.width = Math.max(sampleLabel.width, angledSize.width);
/* 5600 */           sampleLabel.height = Math.max(sampleLabel.height, angledSize.height);
/*      */         }
/* 5602 */         if (angle % 180 != 0) {
/* 5603 */           sampleLabel.height += 3;
/*      */         }
/*      */       }
/*      */       
/*      */ 
/* 5608 */       String label = getLabel("sampleAxisLabel");
/* 5609 */       Font sampleAxisLabelFont = getFont("sampleAxisLabelFont");
/* 5610 */       FontMetrics fm = getFontMetrics(sampleAxisLabelFont);
/* 5611 */       g.setFont(sampleAxisLabelFont);
/* 5612 */       int x = 0;
/* 5613 */       int y = 0;
/* 5614 */       int angle = getLabelAngle("sampleAxisLabelAngle");
/* 5615 */       angle = this.gridAlignment == 0 ? angle : 0;
/* 5616 */       Dimension labelSize = getLabelSize(label, fm);
/* 5617 */       Dimension angledSize = getAngledLabelSize(labelSize, angle);
/*      */       
/*      */ 
/* 5620 */       if (this.gridAlignment == 1)
/*      */       {
/*      */ 
/* 5623 */         x = grid.x + grid.width / 2 - angledSize.width / 2;
/* 5624 */         y = grid.y + grid.height + 7;
/* 5625 */         if (angle % 180 == 0) {
/* 5626 */           y += fm.getMaxAscent() - 2;
/*      */         }
/*      */         
/* 5629 */         if (this.display3dOn) {
/* 5630 */           x -= this.depth3dPoint.x;
/* 5631 */           y -= this.depth3dPoint.y;
/*      */         }
/*      */         
/* 5634 */         y += sampleLabel.height + 1;
/*      */         
/* 5636 */         if (this.sampleScrollerOn) {
/* 5637 */           y += 10;
/*      */         }
/*      */       }
/*      */       else
/*      */       {
/* 5642 */         x = grid.x - angledSize.width - 5;
/* 5643 */         y = grid.y + grid.height / 2 - angledSize.height / 2;
/* 5644 */         if (angle % 180 == 0) {
/* 5645 */           y += fm.getAscent();
/*      */         }
/*      */         
/* 5648 */         if (this.sampleLabelsOn) {
/* 5649 */           x -= sampleLabel.width + 4;
/*      */         }
/*      */         
/* 5652 */         if (this.display3dOn) {
/* 5653 */           y -= this.depth3dPoint.y;
/* 5654 */           x -= this.depth3dPoint.x;
/*      */         }
/*      */         
/* 5657 */         x -= sampleLabel.width + 1;
/*      */         
/* 5659 */         if (this.sampleScrollerOn) {
/* 5660 */           x -= 10;
/*      */         }
/*      */       }
/*      */       
/*      */ 
/* 5665 */       paintLabel(g, label, x, y, labelSize, 0, angle, false);
/*      */     }
/*      */     
/*      */ 
/* 5669 */     for (int i = 0; i < this.rangeAdjusterOn.length; i++) {
/* 5670 */       paintRangeAdjuster(g, grid, i);
/*      */     }
/* 5672 */     if (this.sampleScrollerOn) {
/* 5673 */       paintSampleScroller(g, grid);
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   void paintLabels(Graphics g)
/*      */   {
/* 5685 */     Color foreground = getForeground();
/*      */     
/*      */ 
/* 5688 */     Font font = getFont("font");
/* 5689 */     FontMetrics fm = getFontMetrics(font);
/* 5690 */     g.setFont(font);
/*      */     
/*      */ 
/* 5693 */     Rectangle grid = new Rectangle(this.graphBounds);
/* 5694 */     if (is3DModeOn()) {
/* 5695 */       grid.x -= this.depth3dPoint.x;
/* 5696 */       grid.width += this.depth3dPoint.x;
/* 5697 */       grid.height -= this.depth3dPoint.y;
/*      */     }
/*      */     
/* 5700 */     for (int i = 0; i < this.labelIDs.length; i++)
/*      */     {
/* 5702 */       String id = this.labelIDs[i];
/* 5703 */       String label = (String)this.labelTexts.get(id);
/* 5704 */       Double x = (Double)this.labelXs.get(id);
/* 5705 */       Double y = (Double)this.labelYs.get(id);
/* 5706 */       Integer sample = (Integer)this.labelSamples.get(id);
/* 5707 */       Integer serie = (Integer)this.labelSeries.get(id);
/* 5708 */       int range = 0;
/* 5709 */       if (serie != null) {
/* 5710 */         range = getSeriesRange(serie.intValue());
/*      */       }
/*      */       
/*      */ 
/* 5714 */       if ((id != null) && (x != null) && (y != null))
/*      */       {
/* 5716 */         Dimension labelSize = getLabelSize(label, fm);
/* 5717 */         double x_pos = x.doubleValue();
/* 5718 */         double y_pos = y.doubleValue();
/* 5719 */         int abs_x = -100;
/* 5720 */         int abs_y = -100;
/*      */         
/*      */ 
/* 5723 */         Rectangle bounds = new Rectangle(this.chartDataBounds);
/* 5724 */         if (this.chartType.equals("pie")) {
/* 5725 */           grid = new Rectangle(this.graphBounds);
/* 5726 */           bounds = new Rectangle(this.graphBounds);
/*      */         } else {
/* 5728 */           bounds.height = ((int)(bounds.height / (this.currentUpperRange[range] - this.currentLowerRange[range]) * (this.upperRange[range] - this.lowerRange[range])));
/* 5729 */           bounds.y -= (int)((this.currentUpperRange[range] - this.upperRange[range]) / (this.lowerRange[range] - this.upperRange[range]) * bounds.height);
/* 5730 */           if (is3DModeOn()) {
/* 5731 */             bounds.x -= this.depth3dPoint.x;
/* 5732 */             bounds.width += this.depth3dPoint.x;
/* 5733 */             bounds.height -= this.depth3dPoint.y;
/*      */           }
/*      */         }
/* 5736 */         if ((x_pos > 0.0D) && (x_pos < 1.0D) && (y_pos > 0.0D) && (y_pos < 1.0D)) {
/* 5737 */           abs_x = bounds.x + (int)Math.round((bounds.width - (labelSize.width + 4)) * x_pos);
/* 5738 */           abs_y = bounds.y + (int)Math.round((bounds.height - (labelSize.height + 2)) * y_pos);
/*      */         }
/*      */         else
/*      */         {
/* 5742 */           abs_x = (int)x_pos - 3;
/* 5743 */           abs_y = (int)y_pos - fm.getAscent();
/*      */         }
/*      */         
/*      */ 
/* 5747 */         if ((sample != null) && (serie != null)) {
/* 5748 */           Rectangle box = new Rectangle(abs_x, abs_y, labelSize.width + 4, labelSize.height + 2);
/* 5749 */           Point center = getSampleCenter(sample.intValue(), serie.intValue());
/* 5750 */           if (center != null)
/*      */           {
/*      */ 
/* 5753 */             if ((x_pos > 0.0D) && (x_pos < 1.0D) && (y_pos > 0.0D) && (y_pos < 1.0D))
/*      */             {
/* 5755 */               box.x = Math.min(box.x, grid.x + grid.width - box.width);
/* 5756 */               box.x = Math.max(box.x, grid.x);
/* 5757 */               if (box.x != abs_x) {
/* 5758 */                 x_pos = (box.x - bounds.x) / (bounds.width - box.width);
/* 5759 */                 this.labelXs.put(id, new Double(x_pos));
/*      */               }
/*      */               
/* 5762 */               box.y = Math.min(box.y, grid.y + grid.height - box.height);
/* 5763 */               box.y = Math.max(box.y, grid.y);
/* 5764 */               if (box.y != abs_y) {
/* 5765 */                 y_pos = (box.y - bounds.y) / (bounds.height - box.height);
/* 5766 */                 this.labelYs.put(id, new Double(y_pos));
/*      */               }
/*      */             }
/*      */             
/*      */ 
/* 5771 */             Polygon poly = new Polygon();
/*      */             
/* 5773 */             if (!box.contains(center))
/*      */             {
/*      */ 
/*      */ 
/* 5777 */               if ((box.y + box.height < center.y) && 
/* 5778 */                 (box.x - center.x < center.y - (box.y + box.height)) && 
/* 5779 */                 (center.x - (box.x + box.width) < center.y - (box.y + box.height))) {
/* 5780 */                 poly.addPoint(box.x + (int)(box.width * 0.25D), box.y + box.height);
/* 5781 */                 poly.addPoint(box.x + (int)(box.width * 0.75D), box.y + box.height);
/* 5782 */                 poly.addPoint(center.x, center.y);
/*      */ 
/*      */               }
/* 5785 */               else if ((box.y > center.y) && 
/* 5786 */                 (box.x - center.x < box.y - center.y) && 
/* 5787 */                 (center.x - (box.x + box.width) < box.y - center.y)) {
/* 5788 */                 poly.addPoint(box.x + (int)(box.width * 0.25D), box.y);
/* 5789 */                 poly.addPoint(box.x + (int)(box.width * 0.75D), box.y);
/* 5790 */                 poly.addPoint(center.x, center.y);
/*      */ 
/*      */               }
/* 5793 */               else if (box.x > center.x) {
/* 5794 */                 poly.addPoint(box.x, box.y + (int)(box.height * 0.2D));
/* 5795 */                 poly.addPoint(box.x, box.y + (int)(box.height * 0.8D));
/* 5796 */                 poly.addPoint(center.x, center.y);
/*      */ 
/*      */               }
/* 5799 */               else if (box.x + box.width < center.x) {
/* 5800 */                 poly.addPoint(box.x + box.width, box.y + (int)(box.height * 0.2D));
/* 5801 */                 poly.addPoint(box.x + box.width, box.y + (int)(box.height * 0.8D));
/* 5802 */                 poly.addPoint(center.x, center.y);
/*      */               }
/*      */             }
/*      */             
/* 5806 */             g.setColor(new Color(255, 255, 231));
/* 5807 */             g.fillRect(box.x, box.y, box.width, box.height);
/* 5808 */             g.setColor(foreground);
/* 5809 */             g.drawRect(box.x, box.y, box.width, box.height);
/*      */             
/*      */ 
/* 5812 */             if (poly.npoints == 3) {
/* 5813 */               g.setColor(new Color(255, 255, 231));
/* 5814 */               g.fillPolygon(poly);
/* 5815 */               g.drawLine(poly.xpoints[0], poly.ypoints[0], poly.xpoints[1], poly.ypoints[1]);
/* 5816 */               g.setColor(foreground);
/* 5817 */               g.drawLine(poly.xpoints[0], poly.ypoints[0], poly.xpoints[2], poly.ypoints[2]);
/* 5818 */               g.drawLine(poly.xpoints[1], poly.ypoints[1], poly.xpoints[2], poly.ypoints[2]);
/*      */             }
/*      */             
/*      */ 
/* 5822 */             String adress = (String)this.labelURLs.get(id);
/* 5823 */             if ((adress != null) && (adress.length() > 0)) {
/* 5824 */               g.setColor(Color.blue);
/*      */             } else {
/* 5826 */               g.setColor(foreground);
/*      */             }
/* 5828 */             paintLabel(g, label, box.x + 3, box.y + fm.getAscent(), labelSize, 0, 0, (adress != null) && (adress.length() > 0));
/*      */           }
/*      */           
/*      */ 
/*      */         }
/*      */         else
/*      */         {
/* 5835 */           String adress = (String)this.labelURLs.get(id);
/* 5836 */           if ((adress != null) && (adress.length() > 0)) {
/* 5837 */             g.setColor(Color.blue);
/*      */           } else {
/* 5839 */             g.setColor(foreground);
/*      */           }
/* 5841 */           paintLabel(g, label, abs_x + 3, abs_y + fm.getAscent(), labelSize, 0, 0, (adress != null) && (adress.length() > 0));
/*      */         }
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
/*      */   private void paintRange(Graphics g, Rectangle grid, int index)
/*      */   {
/* 5856 */     index = Math.max(0, index);
/* 5857 */     if ((this.rangeOn[index] == 0) || (this.rangeBounds[index] == null)) {
/* 5858 */       return;
/*      */     }
/*      */     
/*      */ 
/* 5862 */     String prefix = null;
/* 5863 */     String postfix = null;
/* 5864 */     if (index == 0) {
/* 5865 */       prefix = getLabel("rangeLabelPrefix");
/* 5866 */       postfix = getLabel("rangeLabelPostfix");
/*      */     } else {
/* 5868 */       prefix = getLabel("rangeLabelPrefix_" + (index + 1));
/* 5869 */       postfix = getLabel("rangeLabelPostfix_" + (index + 1));
/*      */     }
/*      */     
/*      */ 
/* 5873 */     Font rangeLabelFont = getFont("rangeLabelFont");
/* 5874 */     FontMetrics fm = getFontMetrics(rangeLabelFont);
/* 5875 */     g.setFont(rangeLabelFont);
/*      */     
/*      */ 
/* 5878 */     Color rangeLabelColor = getRangeColor(index);
/* 5879 */     g.setColor(rangeLabelColor);
/* 5880 */     Rectangle b = this.rangeBounds[index];
/*      */     
/* 5882 */     if (this.gridAlignment == 1) {
/* 5883 */       if ((this.rangePosition[index] == 0) && (b.x + b.width != grid.x)) {
/* 5884 */         int x = b.x + b.width - (this.display3dOn ? this.depth3dPoint.x : 0);
/* 5885 */         int y = grid.y - (this.display3dOn ? this.depth3dPoint.y : 0);
/* 5886 */         int height = grid.height;
/* 5887 */         g.drawLine(x, y, x, y + height);
/* 5888 */       } else if ((this.rangePosition[index] == 1) && (b.x != grid.x + grid.width)) {
/* 5889 */         g.drawLine(b.x, grid.y, b.x, grid.y + grid.height);
/*      */       }
/*      */     }
/*      */     else {
/* 5893 */       if (this.rangePosition[index] == 0) if (b.y != grid.y + grid.height - (this.display3dOn ? this.depth3dPoint.y : 0)) {
/* 5894 */           int x = grid.x - (this.display3dOn ? this.depth3dPoint.x : 0);
/* 5895 */           int y = b.y - (this.display3dOn ? this.depth3dPoint.y : 0);
/* 5896 */           g.drawLine(x, y, x + grid.width, y);
/* 5897 */           break label522; } if ((this.rangePosition[index] == 1) && (b.y + b.height != grid.y)) {
/* 5898 */         int x = grid.x + (this.display3dOn ? this.depth3dPoint.x : 0);
/* 5899 */         int y = b.y + b.height;
/* 5900 */         g.drawLine(x, y, x + grid.width, y);
/*      */       }
/*      */     }
/*      */     
/*      */     label522:
/* 5905 */     long valueLineCount = 0L;
/* 5906 */     if (this.gridAlignment == 1) {
/* 5907 */       valueLineCount = (int)Math.round(grid.height / fm.getHeight());
/*      */     } else {
/* 5909 */       String upper = formatNumber(this.upperRange[index], this.rangeDecimalCount[index]);
/* 5910 */       String lower = formatNumber(this.lowerRange[index], this.rangeDecimalCount[index]);
/* 5911 */       if ((prefix != null) && (prefix.length() > 0)) {
/* 5912 */         upper = prefix + upper;
/* 5913 */         lower = prefix + lower;
/*      */       }
/* 5915 */       if ((postfix != null) && (postfix.length() > 0)) {
/* 5916 */         upper = upper + postfix;
/* 5917 */         lower = lower + postfix;
/*      */       }
/* 5919 */       int range_label_width = Math.max(fm.stringWidth(upper), fm.stringWidth(lower)) + 4;
/* 5920 */       valueLineCount = (int)Math.round(grid.width / range_label_width);
/*      */     }
/*      */     
/*      */ 
/*      */ 
/* 5925 */     valueLineCount = Math.min(this.maxValueLineCount, valueLineCount);
/* 5926 */     long count = Math.round(Math.abs(this.currentUpperRange[index] - this.currentLowerRange[index]) * Math.pow(10.0D, this.rangeDecimalCount[index]));
/* 5927 */     valueLineCount = Math.min(valueLineCount, count + 1L);
/* 5928 */     valueLineCount = Math.max(1L, valueLineCount);
/*      */     
/*      */ 
/* 5931 */     double raw_step = (this.currentUpperRange[index] - this.currentLowerRange[index]) / (valueLineCount - 1L);
/* 5932 */     raw_step = Math.abs(raw_step);
/* 5933 */     double step = raw_step;
/* 5934 */     boolean stepFound = false;
/*      */     
/*      */ 
/* 5937 */     if (!this.showValue_As_Time) {
/* 5938 */       double multiplier = 10.0D;
/*      */       do {
/* 5940 */         for (int i = 0; i < STEPS.length; i++)
/*      */         {
/* 5942 */           if (STEPS[i] * multiplier * 1.0E-12D >= raw_step) {
/* 5943 */             step = STEPS[i] * multiplier * 1.0E-12D;
/* 5944 */             stepFound = true;
/* 5945 */             break;
/*      */           }
/*      */         }
/* 5948 */         multiplier *= 10.0D;
/* 5939 */         if (stepFound) break; } while (multiplier * 1.0E-12D < 1.0E14D);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     }
/*      */     else
/*      */     {
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 5952 */       for (int i = 0; i < STEPS_TIME.length; i++)
/*      */       {
/* 5954 */         if (STEPS_TIME[i] >= raw_step) {
/* 5955 */           step = STEPS_TIME[i];
/* 5956 */           stepFound = true;
/* 5957 */           break;
/*      */         }
/*      */       }
/*      */     }
/*      */     
/*      */ 
/* 5963 */     if ((this.currentUpperRange[index] > this.currentLowerRange[index]) && (this.currentUpperRange[index] > 0.0D))
/*      */     {
/* 5965 */       double value = Math.max(0.0D, this.currentLowerRange[index] - this.currentLowerRange[index] % step);
/* 5966 */       while (value < this.currentUpperRange[index]) {
/* 5967 */         if (value != 0.0D)
/*      */         {
/* 5969 */           String label = this.rangeLabelsOn[index] != 0 ? formatNumber(value, this.rangeDecimalCount[index]) : null;
/*      */           
/* 5971 */           if ((label != null) && (prefix != null)) {
/* 5972 */             label = prefix + label;
/*      */           }
/* 5974 */           if ((label != null) && (postfix != null)) {
/* 5975 */             label = label + postfix;
/*      */           }
/* 5977 */           paintGridLine(g, grid, value, this.valueLinesColor, label, rangeLabelColor, (index == 0) && (this.valueLinesOn), false, index, 0);
/*      */         }
/* 5979 */         value += step;
/*      */       }
/* 5981 */     } else if ((this.currentUpperRange[index] < this.currentLowerRange[index]) && (this.currentUpperRange[index] < 0.0D))
/*      */     {
/* 5983 */       double value = Math.min(0.0D, this.currentLowerRange[index] - this.currentLowerRange[index] % step);
/* 5984 */       while (value > this.currentUpperRange[index]) {
/* 5985 */         if (value != 0.0D)
/*      */         {
/* 5987 */           String label = this.rangeLabelsOn[index] != 0 ? formatNumber(value, this.rangeDecimalCount[index]) : null;
/*      */           
/* 5989 */           if ((label != null) && (prefix != null)) {
/* 5990 */             label = prefix + label;
/*      */           }
/* 5992 */           if ((label != null) && (postfix != null)) {
/* 5993 */             label = label + postfix;
/*      */           }
/* 5995 */           paintGridLine(g, grid, value, this.valueLinesColor, label, rangeLabelColor, (index == 0) && (this.valueLinesOn), false, index, 0);
/*      */         }
/* 5997 */         value -= step;
/*      */       }
/*      */     }
/*      */     
/*      */ 
/*      */ 
/* 6003 */     if ((this.currentUpperRange[index] > this.currentLowerRange[index]) && (this.currentLowerRange[index] < 0.0D))
/*      */     {
/* 6005 */       double value = Math.min(0.0D, this.currentUpperRange[index] - this.currentUpperRange[index] % step);
/* 6006 */       while (value > this.currentLowerRange[index]) {
/* 6007 */         if (value != 0.0D)
/*      */         {
/* 6009 */           String label = this.rangeLabelsOn[index] != 0 ? formatNumber(value, this.rangeDecimalCount[index]) : null;
/*      */           
/* 6011 */           if ((label != null) && (prefix != null)) {
/* 6012 */             label = prefix + label;
/*      */           }
/* 6014 */           if ((label != null) && (postfix != null)) {
/* 6015 */             label = label + postfix;
/*      */           }
/*      */           
/*      */ 
/* 6019 */           paintGridLine(g, grid, value, this.valueLinesColor, label, rangeLabelColor, (index == 0) && (this.valueLinesOn), false, index, 0);
/*      */         }
/* 6021 */         value -= step;
/*      */       }
/* 6023 */     } else if ((this.currentUpperRange[index] < this.currentLowerRange[index]) && (this.currentLowerRange[index] > 0.0D))
/*      */     {
/* 6025 */       double value = Math.max(0.0D, this.currentUpperRange[index] - this.currentUpperRange[index] % step);
/* 6026 */       while (value < this.currentLowerRange[index]) {
/* 6027 */         if (value != 0.0D)
/*      */         {
/* 6029 */           String label = this.rangeLabelsOn[index] != 0 ? formatNumber(value, this.rangeDecimalCount[index]) : null;
/*      */           
/* 6031 */           if ((label != null) && (prefix != null)) {
/* 6032 */             label = prefix + label;
/*      */           }
/* 6034 */           if ((label != null) && (postfix != null)) {
/* 6035 */             label = label + postfix;
/*      */           }
/*      */           
/*      */ 
/* 6039 */           paintGridLine(g, grid, value, this.valueLinesColor, label, rangeLabelColor, (index == 0) && (this.valueLinesOn), false, index, 0);
/*      */         }
/* 6041 */         value += step;
/*      */       }
/*      */     }
/*      */     
/*      */ 
/* 6046 */     if (this.rangeLabelsOn[index] != 0) {
/* 6047 */       String label = formatNumber(this.currentUpperRange[index], this.currentUpperRange[index] != 0.0D ? this.rangeDecimalCount[index] : 0);
/* 6048 */       label = prefix != null ? prefix + label : label;
/* 6049 */       label = postfix != null ? label + postfix : label;
/* 6050 */       paintGridLine(g, grid, this.currentUpperRange[index], this.chartForeground, label, rangeLabelColor, index == 0, false, index, 0);
/* 6051 */       label = this.rangeLabelsOn[index] != 0 ? formatNumber(this.currentLowerRange[index], this.currentLowerRange[index] != 0.0D ? this.rangeDecimalCount[index] : 0) : null;
/* 6052 */       label = prefix != null ? prefix + label : label;
/* 6053 */       label = postfix != null ? label + postfix : label;
/* 6054 */       paintGridLine(g, grid, this.currentLowerRange[index], this.chartForeground, label, rangeLabelColor, index == 0, false, index, 0);
/*      */     }
/*      */     
/*      */ 
/* 6058 */     if (this.rangeLabelsOn[index] != 0) {
/* 6059 */       String label = "0";
/* 6060 */       label = prefix != null ? prefix + label : label;
/* 6061 */       label = postfix != null ? label + postfix : label;
/* 6062 */       paintGridLine(g, grid, 0.0D, index == 0 ? this.chartForeground : rangeLabelColor, label, rangeLabelColor, true, false, index, 0);
/*      */     }
/*      */     
/*      */ 
/* 6066 */     if (index == 0) {
/* 6067 */       for (Enumeration e = this.targetsLabel.keys(); e.hasMoreElements();)
/*      */       {
/* 6069 */         String id = (String)e.nextElement();
/* 6070 */         String target = getTargetLabel(id);
/* 6071 */         Double value = (Double)this.targetsValue.get(id);
/* 6072 */         Color color = (Color)this.targetsColor.get(id);
/*      */         
/* 6074 */         if (value != null) {
/* 6075 */           paintGridLine(g, grid, value.doubleValue(), color, target, color, true, true, 0, 0);
/*      */         }
/*      */       }
/*      */     }
/*      */     
/*      */ 
/* 6081 */     if (this.defaultGridLines != null) {
/* 6082 */       for (int i = 0; i < this.defaultGridLines.length; i++) {
/* 6083 */         paintGridLine(g, grid, this.defaultGridLines[i], this.defaultGridLinesColor, null, null, true, false, 0, 1);
/*      */       }
/*      */     }
/*      */     
/*      */ 
/* 6088 */     if (this.gridLines != null) {
/* 6089 */       for (int i = 0; i < this.gridLines.length; i++) {
/* 6090 */         Color color = this.defaultGridLinesColor;
/* 6091 */         if ((this.gridLineColors != null) && (this.gridLineColors.length > i) && (this.gridLineColors[i] != null)) {
/* 6092 */           color = this.gridLineColors[i];
/*      */         }
/* 6094 */         paintGridLine(g, grid, this.gridLines[i], color, null, null, true, true, 0, 1);
/*      */       }
/*      */     }
/*      */     
/*      */ 
/* 6099 */     String rangeAxisLabel = null;
/* 6100 */     if (index == 0) {
/* 6101 */       rangeAxisLabel = getLabel("rangeAxisLabel");
/* 6102 */     } else if (index > 0) {
/* 6103 */       rangeAxisLabel = getLabel("rangeAxisLabel_" + (index + 1));
/*      */     }
/*      */     
/* 6106 */     if (rangeAxisLabel != null)
/*      */     {
/* 6108 */       Font range_axis_label_font = getFont("rangeAxisLabelFont");
/* 6109 */       fm = getFontMetrics(range_axis_label_font);
/* 6110 */       g.setFont(range_axis_label_font);
/* 6111 */       g.setColor(rangeLabelColor);
/*      */       
/*      */ 
/* 6114 */       int label_x = 0;
/* 6115 */       int label_y = 0;
/* 6116 */       int angle = index == 0 ? getLabelAngle("rangeAxisLabelAngle") : getLabelAngle("rangeAxisLabelAngle_" + (index + 1));
/* 6117 */       angle = this.gridAlignment == 1 ? angle : 0;
/* 6118 */       Dimension labelSize = getLabelSize(rangeAxisLabel, fm);
/* 6119 */       Dimension angledSize = getAngledLabelSize(labelSize, angle);
/*      */       
/* 6121 */       if (this.gridAlignment == 1)
/*      */       {
/* 6123 */         int widest = 0;
/*      */         
/* 6125 */         if (this.rangePosition[index] == 0) {
/* 6126 */           widest = getRangeWidth(index, true, true);
/* 6127 */           label_x = this.rangeBounds[index].x + this.rangeBounds[index].width - angledSize.width - widest - 9;
/* 6128 */           if ((this.rangeAdjusterOn[index] != 0) && (getAdjusterPosition(index) == this.rangePosition[index])) {
/* 6129 */             label_x -= 6;
/*      */           }
/* 6131 */           if (this.display3dOn) {
/* 6132 */             label_x -= this.depth3dPoint.x;
/*      */           }
/*      */         }
/*      */         else {
/* 6136 */           widest = getRangeWidth(index, true, true);
/* 6137 */           label_x = this.rangeBounds[index].x + widest + 9;
/* 6138 */           if ((this.rangeAdjusterOn[index] != 0) && (getAdjusterPosition(index) == this.rangePosition[index])) {
/* 6139 */             label_x += 6;
/*      */           }
/*      */         }
/*      */         
/* 6143 */         label_y = grid.y + grid.height / 2 - angledSize.height / 2;
/* 6144 */         if (angle % 180 == 0) {
/* 6145 */           label_y += fm.getAscent();
/*      */         }
/* 6147 */         if ((this.display3dOn) && (this.rangePosition[index] == 0)) {
/* 6148 */           label_y -= this.depth3dPoint.y;
/*      */         }
/*      */         
/*      */       }
/*      */       else
/*      */       {
/* 6154 */         label_x = grid.x + grid.width / 2 - angledSize.width / 2;
/*      */         
/* 6156 */         if (this.rangePosition[index] == 0) {
/* 6157 */           label_y = this.rangeBounds[index].y + 5 + getRangeWidth(index, true, true) + (angle % 180 == 0 ? fm.getMaxAscent() - 3 : 0);
/* 6158 */           if ((this.rangeAdjusterOn[index] != 0) && (getAdjusterPosition(index) == this.rangePosition[index])) {
/* 6159 */             label_y += 6;
/*      */           }
/*      */         }
/*      */         else {
/* 6163 */           label_y = this.rangeBounds[index].y + this.rangeBounds[index].height - 7 - getRangeWidth(index, true, true);
/* 6164 */           if ((this.rangeAdjusterOn[index] != 0) && (getAdjusterPosition(index) == this.rangePosition[index])) {
/* 6165 */             label_y -= 6;
/*      */           }
/*      */         }
/*      */       }
/*      */       
/* 6170 */       paintLabel(g, rangeAxisLabel, label_x, label_y, labelSize, 0, angle, false);
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
/*      */   int getValuePosition(int index, double value, Rectangle grid)
/*      */   {
/* 6183 */     index = Math.min(this.upperRange.length - 1, Math.max(0, index));
/*      */     
/*      */ 
/* 6186 */     int positive_space = 0;
/* 6187 */     int zero_line = 0;
/* 6188 */     if (this.gridAlignment == 1) {
/* 6189 */       if (this.currentUpperRange[index] - this.currentLowerRange[index] != 0.0D) {
/* 6190 */         positive_space = (int)(grid.height * (this.currentUpperRange[index] / (this.currentUpperRange[index] - this.currentLowerRange[index])));
/*      */       }
/* 6192 */       zero_line = grid.y + positive_space;
/*      */     } else {
/* 6194 */       if (this.currentUpperRange[index] - this.currentLowerRange[index] != 0.0D) {
/* 6195 */         positive_space = (int)(grid.width * (this.currentUpperRange[index] / (this.currentUpperRange[index] - this.currentLowerRange[index])));
/*      */       }
/* 6197 */       zero_line = grid.x + grid.width - positive_space;
/*      */     }
/*      */     
/*      */ 
/* 6201 */     int pos = 0;
/* 6202 */     if (this.currentUpperRange[index] - this.currentLowerRange[index] != 0.0D) {
/* 6203 */       if (this.gridAlignment == 1) {
/* 6204 */         pos = (int)(zero_line - grid.height * (value / (this.currentUpperRange[index] - this.currentLowerRange[index])));
/*      */       } else {
/* 6206 */         pos = (int)(zero_line + grid.width * (value / (this.currentUpperRange[index] - this.currentLowerRange[index])));
/*      */       }
/*      */     }
/* 6209 */     return pos;
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
/*      */   int getRangeWidth(int range, boolean rangeLabels, boolean targetLabels)
/*      */   {
/* 6225 */     FontMetrics fm = getFontMetrics(getFont("rangeLabelFont"));
/* 6226 */     String prefix = null;
/* 6227 */     String postfix = null;
/* 6228 */     if (range == 0) {
/* 6229 */       prefix = getLabel("rangeLabelPrefix");
/* 6230 */       postfix = getLabel("rangeLabelPostfix");
/*      */     } else {
/* 6232 */       prefix = getLabel("rangeLabelPrefix_" + (range + 1));
/* 6233 */       postfix = getLabel("rangeLabelPostfix_" + (range + 1));
/*      */     }
/*      */     
/*      */ 
/* 6237 */     int widest = 0;
/* 6238 */     if ((range >= 0) && (range < this.rangeOn.length) && (rangeLabels) && (this.rangeLabelsOn[range] != 0)) {
/* 6239 */       int decimals = getRangeDecimalCount(range);
/* 6240 */       String upper = formatNumber(this.upperRange[range], decimals);
/* 6241 */       String lower = formatNumber(this.lowerRange[range], decimals);
/* 6242 */       upper = prefix != null ? prefix + upper : upper;
/* 6243 */       lower = prefix != null ? prefix + lower : lower;
/* 6244 */       upper = postfix != null ? upper + postfix : upper;
/* 6245 */       lower = postfix != null ? lower + postfix : lower;
/* 6246 */       if (this.gridAlignment == 1) {
/* 6247 */         widest = Math.max(fm.stringWidth(upper), fm.stringWidth(lower));
/*      */       } else {
/* 6249 */         widest = fm.getHeight();
/*      */       }
/*      */     }
/*      */     
/* 6253 */     if ((range < this.rangeOn.length) && (targetLabels))
/*      */     {
/* 6255 */       int inner_range = -1;
/* 6256 */       if (this.targetLabelsPosition == -1) {
/* 6257 */         inner_range = 0;
/*      */       } else {
/* 6259 */         for (int i = this.rangeBounds.length - 1; i >= 0; i--) {
/* 6260 */           if (this.rangePosition[i] == this.targetLabelsPosition) {
/* 6261 */             inner_range = i;
/*      */           }
/*      */         }
/*      */       }
/*      */       
/* 6266 */       int widestTarget = 0;
/* 6267 */       for (Enumeration e = this.targetsLabel.keys(); e.hasMoreElements();) {
/* 6268 */         String id = (String)e.nextElement();
/* 6269 */         String label = getTargetLabel(id);
/*      */         
/* 6271 */         if ((label != null) && (label.length() > 0)) {
/* 6272 */           if (this.gridAlignment == 1) {
/* 6273 */             widestTarget = Math.max(widestTarget, fm.stringWidth(label));
/*      */           } else {
/* 6275 */             widestTarget = fm.getHeight();
/*      */           }
/*      */         }
/*      */       }
/*      */       
/*      */ 
/* 6281 */       if (range == -1) {
/* 6282 */         widest = widestTarget;
/* 6283 */       } else if (range == inner_range) {
/* 6284 */         if (this.gridAlignment == 1) {
/* 6285 */           widest = Math.max(widestTarget, widest);
/*      */         } else {
/* 6287 */           widest += widestTarget;
/*      */         }
/*      */       }
/*      */     }
/* 6291 */     return widest;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   private void paintRangeAdjuster(Graphics g, Rectangle grid, int adjuster)
/*      */   {
/* 6303 */     adjuster = Math.max(0, adjuster);
/* 6304 */     if ((this.rangeOn[adjuster] == 0) || (this.rangeAdjusterOn[adjuster] == 0) || (this.rangeBounds[adjuster] == null)) {
/* 6305 */       return;
/*      */     }
/*      */     
/*      */ 
/*      */ 
/* 6310 */     int rangeIndex = this.rangeAdjusted[adjuster];
/* 6311 */     if (rangeIndex == -1) {
/* 6312 */       rangeIndex = adjuster;
/*      */     }
/*      */     
/*      */ 
/* 6316 */     if (this.gridAlignment == 1)
/*      */     {
/* 6318 */       int xpos = 0;
/* 6319 */       int ypos = grid.y;
/*      */       
/*      */ 
/* 6322 */       if (this.rangePosition[adjuster] == 0) {
/* 6323 */         xpos = this.rangeBounds[adjuster].x + this.rangeBounds[adjuster].width - 5 - this.depth3dPoint.x;
/* 6324 */         ypos -= (this.display3dOn ? this.depth3dPoint.y : 0);
/*      */       }
/*      */       else {
/* 6327 */         xpos = this.rangeBounds[adjuster].x + 5;
/*      */       }
/*      */       
/* 6330 */       if (this.rangeAdjusterOn.length > 2) {
/* 6331 */         this.rangeAdjusterPosition = this.rangePosition[0];
/*      */       }
/* 6333 */       if (adjuster == 0) {
/* 6334 */         if (this.rangeAdjusterPosition == 0) {
/* 6335 */           xpos = grid.x - 5 - (this.display3dOn ? this.depth3dPoint.x : 0);
/* 6336 */           ypos = grid.y - (this.display3dOn ? this.depth3dPoint.y : 0);
/*      */         } else {
/* 6338 */           xpos = grid.x + grid.width + 5;
/* 6339 */           ypos = grid.y;
/*      */         }
/*      */       }
/* 6342 */       g.setColor(getBackground().darker());
/* 6343 */       g.drawLine(xpos, ypos, xpos, ypos + grid.height);
/* 6344 */       g.setColor(getBackground().brighter());
/* 6345 */       g.drawLine(xpos + 1, ypos, xpos + 1, ypos + grid.height);
/*      */       
/*      */ 
/* 6348 */       double factor_u = 0.0D;
/* 6349 */       double factor_l = 0.0D;
/* 6350 */       if (this.upperRange[rangeIndex] - this.lowerRange[rangeIndex] != 0.0D) {
/* 6351 */         factor_u = (this.currentUpperRange[rangeIndex] - this.lowerRange[rangeIndex]) / (this.upperRange[rangeIndex] - this.lowerRange[rangeIndex]);
/* 6352 */         factor_l = (this.currentLowerRange[rangeIndex] - this.lowerRange[rangeIndex]) / (this.upperRange[rangeIndex] - this.lowerRange[rangeIndex]);
/*      */       }
/* 6354 */       int upper_y = (int)Math.round(ypos + grid.height - factor_u * grid.height);
/* 6355 */       int lower_y = (int)Math.round(ypos + grid.height - factor_l * grid.height);
/* 6356 */       this.rangeAdjusterBounds[adjuster].setBounds(xpos - 3, upper_y, 7, lower_y - upper_y);
/*      */       
/*      */ 
/* 6359 */       paintBox(g, Color.lightGray, xpos - 3, upper_y, 6, lower_y - upper_y, false);
/* 6360 */       g.setColor(Color.gray);
/* 6361 */       g.drawLine(xpos - 2, upper_y + 2, xpos + 2, upper_y + 2);
/* 6362 */       g.setColor(Color.white);
/* 6363 */       g.drawLine(xpos - 2, lower_y - 2, xpos + 2, lower_y - 2);
/*      */ 
/*      */     }
/*      */     else
/*      */     {
/*      */ 
/* 6369 */       int ypos = 0;
/* 6370 */       int xpos = grid.x;
/*      */       
/* 6372 */       if (this.rangePosition[adjuster] == 0) {
/* 6373 */         ypos = this.rangeBounds[adjuster].y + 5;
/* 6374 */         xpos -= (this.display3dOn ? this.depth3dPoint.x : 0);
/*      */       }
/*      */       else {
/* 6377 */         ypos = this.rangeBounds[adjuster].y + this.rangeBounds[adjuster].height - 5;
/*      */       }
/*      */       
/* 6380 */       if (this.rangeAdjusterOn.length > 2) {
/* 6381 */         this.rangeAdjusterPosition = this.rangePosition[0];
/*      */       }
/* 6383 */       if (adjuster == 0) {
/* 6384 */         if (this.rangeAdjusterPosition == 0) {
/* 6385 */           xpos = grid.x - (this.display3dOn ? this.depth3dPoint.x : 0);
/* 6386 */           ypos = grid.y + grid.height + 5 - (this.display3dOn ? this.depth3dPoint.y : 0);
/*      */         } else {
/* 6388 */           xpos = grid.x;
/* 6389 */           ypos = grid.y - 5;
/*      */         }
/*      */       }
/* 6392 */       g.setColor(getBackground().darker());
/* 6393 */       g.drawLine(xpos, ypos, xpos + grid.width, ypos);
/* 6394 */       g.setColor(getBackground().brighter());
/* 6395 */       g.drawLine(xpos, ypos + 1, xpos + grid.width, ypos + 1);
/*      */       
/*      */ 
/* 6398 */       double factor_u = 0.0D;
/* 6399 */       double factor_l = 0.0D;
/* 6400 */       if (this.upperRange[rangeIndex] - this.lowerRange[rangeIndex] != 0.0D) {
/* 6401 */         factor_u = (this.currentUpperRange[rangeIndex] - this.lowerRange[rangeIndex]) / (this.upperRange[rangeIndex] - this.lowerRange[rangeIndex]);
/* 6402 */         factor_l = (this.currentLowerRange[rangeIndex] - this.lowerRange[rangeIndex]) / (this.upperRange[rangeIndex] - this.lowerRange[rangeIndex]);
/*      */       }
/* 6404 */       int lower_x = (int)Math.round(xpos + factor_l * grid.width);
/* 6405 */       int upper_x = (int)Math.round(xpos + factor_u * grid.width);
/* 6406 */       this.rangeAdjusterBounds[adjuster].setBounds(lower_x, ypos - 3, upper_x - lower_x, 7);
/*      */       
/*      */ 
/* 6409 */       paintBox(g, Color.lightGray, lower_x, ypos - 3, upper_x - lower_x, 6, false);
/* 6410 */       g.setColor(Color.gray);
/* 6411 */       g.drawLine(lower_x + 2, ypos - 2, lower_x + 2, ypos + 2);
/* 6412 */       g.setColor(Color.white);
/* 6413 */       g.drawLine(upper_x - 2, ypos - 2, upper_x - 2, ypos + 2);
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
/*      */   private void paintSampleScroller(Graphics g, Rectangle bounds)
/*      */   {
/* 6426 */     if (this.gridAlignment == 1)
/*      */     {
/* 6428 */       int xpos = bounds.x;
/* 6429 */       int ypos = bounds.y + bounds.height + 1;
/* 6430 */       if (this.display3dOn) {
/* 6431 */         xpos -= this.depth3dPoint.x;
/* 6432 */         ypos -= this.depth3dPoint.y;
/*      */       }
/* 6434 */       this.sampleScrollerBounds.x = xpos;
/* 6435 */       this.sampleScrollerBounds.y = ypos;
/* 6436 */       this.sampleScrollerBounds.height = 8;
/* 6437 */       this.sampleScrollerBounds.width = bounds.width;
/*      */       
/*      */ 
/* 6440 */       g.setColor(getBackground().darker());
/* 6441 */       g.drawLine(xpos, ypos + 4, xpos + this.sampleScrollerBounds.width, ypos + 4);
/* 6442 */       g.setColor(getBackground().brighter());
/* 6443 */       g.drawLine(xpos, ypos + 5, xpos + this.sampleScrollerBounds.width, ypos + 5);
/*      */       
/*      */ 
/* 6446 */       paintBox(g, Color.lightGray, xpos, ypos, 8, 8, this.leftPushed);
/* 6447 */       xpos += (this.leftPushed ? 1 : 0);
/* 6448 */       g.setColor(Color.black);
/* 6449 */       g.drawLine(xpos + 3, ypos + 4, xpos + 3, ypos + 4);
/* 6450 */       g.drawLine(xpos + 4, ypos + 3, xpos + 4, ypos + 5);
/* 6451 */       g.drawLine(xpos + 5, ypos + 2, xpos + 5, ypos + 6);
/*      */       
/*      */ 
/* 6454 */       xpos = this.sampleScrollerBounds.x + this.sampleScrollerBounds.width - 8;
/* 6455 */       paintBox(g, Color.lightGray, xpos, ypos, 8, 8, this.rightPushed);
/* 6456 */       xpos += (this.rightPushed ? 1 : 0);
/* 6457 */       g.setColor(Color.black);
/* 6458 */       g.drawLine(xpos + 3, ypos + 2, xpos + 3, ypos + 6);
/* 6459 */       g.drawLine(xpos + 4, ypos + 3, xpos + 4, ypos + 5);
/* 6460 */       g.drawLine(xpos + 5, ypos + 4, xpos + 5, ypos + 4);
/*      */       
/*      */ 
/* 6463 */       this.sampleScrollerSpace = (this.sampleScrollerBounds.width - 18 - 8);
/* 6464 */       int leftSpace = (int)Math.round(this.leftScrollerFactor * this.sampleScrollerSpace);
/* 6465 */       xpos = bounds.x + 9 + leftSpace;
/* 6466 */       if (this.display3dOn) {
/* 6467 */         xpos -= this.depth3dPoint.x;
/*      */       }
/* 6469 */       int rightSpace = (int)Math.round(this.rightScrollerFactor * this.sampleScrollerSpace);
/* 6470 */       int width = this.sampleScrollerSpace + 8 - rightSpace - leftSpace;
/* 6471 */       this.leftAdjusterPos = xpos;
/* 6472 */       this.rightAdjusterPos = (this.leftAdjusterPos + width);
/* 6473 */       paintBox(g, Color.lightGray, xpos, ypos, width, 8, false);
/* 6474 */       g.setColor(Color.gray);
/* 6475 */       g.drawLine(xpos + 2, ypos + 1, xpos + 2, ypos + 7);
/* 6476 */       g.setColor(Color.white);
/* 6477 */       g.drawLine(xpos + width - 2, ypos + 1, xpos + width - 2, ypos + 7);
/*      */ 
/*      */     }
/*      */     else
/*      */     {
/*      */ 
/* 6483 */       int xpos = bounds.x - 10;
/* 6484 */       int ypos = bounds.y;
/* 6485 */       if (this.display3dOn) {
/* 6486 */         xpos -= this.depth3dPoint.x;
/* 6487 */         ypos -= this.depth3dPoint.y;
/*      */       }
/* 6489 */       this.sampleScrollerBounds.x = xpos;
/* 6490 */       this.sampleScrollerBounds.y = ypos;
/* 6491 */       this.sampleScrollerBounds.height = bounds.height;
/* 6492 */       this.sampleScrollerBounds.width = 7;
/*      */       
/*      */ 
/* 6495 */       g.setColor(getBackground().darker());
/* 6496 */       g.drawLine(xpos + 4, ypos, xpos + 4, ypos + this.sampleScrollerBounds.height);
/* 6497 */       g.setColor(getBackground().brighter());
/* 6498 */       g.drawLine(xpos + 5, ypos, xpos + 5, ypos + this.sampleScrollerBounds.height);
/*      */       
/*      */ 
/* 6501 */       paintBox(g, Color.lightGray, xpos, ypos, 8, 8, this.leftPushed);
/* 6502 */       ypos += (this.leftPushed ? 1 : 0);
/* 6503 */       g.setColor(Color.black);
/* 6504 */       g.drawLine(xpos + 4, ypos + 3, xpos + 4, ypos + 3);
/* 6505 */       g.drawLine(xpos + 3, ypos + 4, xpos + 5, ypos + 4);
/* 6506 */       g.drawLine(xpos + 2, ypos + 5, xpos + 6, ypos + 5);
/*      */       
/*      */ 
/* 6509 */       ypos = this.sampleScrollerBounds.y + this.sampleScrollerBounds.height - 8;
/* 6510 */       paintBox(g, Color.lightGray, xpos, ypos, 8, 8, this.rightPushed);
/* 6511 */       ypos += (this.rightPushed ? 1 : 0);
/* 6512 */       g.setColor(Color.black);
/* 6513 */       g.drawLine(xpos + 4, ypos + 5, xpos + 4, ypos + 5);
/* 6514 */       g.drawLine(xpos + 3, ypos + 4, xpos + 5, ypos + 4);
/* 6515 */       g.drawLine(xpos + 2, ypos + 3, xpos + 6, ypos + 3);
/*      */       
/*      */ 
/* 6518 */       this.sampleScrollerSpace = (this.sampleScrollerBounds.height - 18 - 8);
/* 6519 */       int topSpace = (int)Math.round(this.leftScrollerFactor * this.sampleScrollerSpace);
/* 6520 */       ypos = bounds.y + 9 + topSpace;
/* 6521 */       if (this.display3dOn) {
/* 6522 */         ypos -= this.depth3dPoint.y;
/*      */       }
/* 6524 */       int bottomSpace = (int)Math.round(this.rightScrollerFactor * this.sampleScrollerSpace);
/* 6525 */       int height = this.sampleScrollerSpace + 8 - bottomSpace - topSpace;
/* 6526 */       this.leftAdjusterPos = ypos;
/* 6527 */       this.rightAdjusterPos = (this.leftAdjusterPos + height);
/* 6528 */       paintBox(g, Color.lightGray, xpos, ypos, 8, height, false);
/* 6529 */       g.setColor(Color.gray);
/* 6530 */       g.drawLine(xpos + 1, ypos + 2, xpos + 7, ypos + 2);
/* 6531 */       g.setColor(Color.white);
/* 6532 */       g.drawLine(xpos + 1, ypos + height - 2, xpos + 7, ypos + height - 2);
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   void paintZoomOutButton(Graphics g, Rectangle bounds)
/*      */   {
/* 6544 */     boolean paintZoomOut = false;
/* 6545 */     for (int range = 0; range < this.rangeOn.length; range++)
/*      */     {
/* 6547 */       paintZoomOut |= getRange(range) != getCurrentRange(range);
/* 6548 */       paintZoomOut |= getLowerRange(range) != getCurrentLowerRange(range);
/*      */       
/* 6550 */       for (int chart = 0; chart < this.overlayCharts.size(); chart++) {
/* 6551 */         Chart overlay = (Chart)this.overlayCharts.elementAt(chart);
/* 6552 */         paintZoomOut |= overlay.getRange(range) != overlay.getCurrentRange(range);
/* 6553 */         paintZoomOut |= overlay.getLowerRange(range) != overlay.getCurrentLowerRange(range);
/*      */       }
/*      */     }
/*      */     
/* 6557 */     if (this.gridAlignment == 1) {
/* 6558 */       paintZoomOut |= this.chartDataBounds.width != bounds.width;
/*      */     } else {
/* 6560 */       paintZoomOut |= this.chartDataBounds.height != bounds.height;
/*      */     }
/*      */     
/*      */ 
/*      */ 
/* 6565 */     if (paintZoomOut) {
/* 6566 */       int xpos = bounds.x + 8 - this.depth3dPoint.x;
/* 6567 */       int ypos = bounds.y + 8 - this.depth3dPoint.y;
/* 6568 */       if (this.gridAlignment == 0) {
/* 6569 */         xpos = bounds.x + bounds.width - 16;
/* 6570 */         ypos = bounds.y + 8;
/*      */       }
/* 6572 */       paintBox(g, Color.lightGray, xpos, ypos, 8, 8, this.zoomOutPushed);
/* 6573 */       g.setColor(Color.black);
/* 6574 */       g.drawLine(xpos + 3, ypos + 4, xpos + 5, ypos + 4);
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
/*      */   private void paintBox(Graphics g, Color c, int x, int y, int w, int h, boolean pushed)
/*      */   {
/* 6590 */     g.setColor(c);
/* 6591 */     g.fillRect(x, y, w, h);
/* 6592 */     g.setColor(pushed ? c.darker() : Color.white);
/* 6593 */     g.drawLine(x, y, x + w, y);
/* 6594 */     g.drawLine(x, y + 1, x, y + h);
/* 6595 */     g.setColor(pushed ? Color.white : c.darker());
/* 6596 */     g.drawLine(x + 1, y + h, x + w, y + h);
/* 6597 */     g.drawLine(x + w, y + 1, x + w, y + h);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   void paintMouseBox(Graphics g)
/*      */   {
/* 6607 */     g.setColor(getChartForeground());
/*      */     
/*      */ 
/* 6610 */     if (this.mouseBox != null) {
/* 6611 */       boolean dot = true;
/*      */       
/* 6613 */       for (int x = this.mouseBox.x; x < this.mouseBox.x + this.mouseBox.width; x++) {
/* 6614 */         if (dot) {
/* 6615 */           g.drawLine(x, this.mouseBox.y, x, this.mouseBox.y);
/* 6616 */           g.drawLine(x, this.mouseBox.y + this.mouseBox.height, x, this.mouseBox.y + this.mouseBox.height);
/*      */         }
/* 6618 */         dot = !dot;
/*      */       }
/*      */       
/* 6621 */       dot = true;
/* 6622 */       for (int y = this.mouseBox.y; y < this.mouseBox.y + this.mouseBox.height; y++) {
/* 6623 */         if (dot) {
/* 6624 */           g.drawLine(this.mouseBox.x, y, this.mouseBox.x, y);
/* 6625 */           g.drawLine(this.mouseBox.x + this.mouseBox.width, y, this.mouseBox.x + this.mouseBox.width, y);
/*      */         }
/* 6627 */         dot = !dot;
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
/*      */ 
/*      */ 
/*      */ 
/*      */   void paintGridLine(Graphics g, Rectangle grid, double value, Color color, String label, Color labelColor, boolean lineOn, boolean target, int index, int align)
/*      */   {
/* 6648 */     double factor = 0.0D;
/*      */     
/*      */ 
/* 6651 */     if (align == 0) {
/* 6652 */       index = Math.max(0, Math.min(this.rangeOn.length - 1, index));
/* 6653 */       if (this.currentUpperRange[index] - this.currentLowerRange[index] != 0.0D) {
/* 6654 */         factor = (value - this.currentLowerRange[index]) / (this.currentUpperRange[index] - this.currentLowerRange[index]);
/*      */       }
/*      */       
/*      */     }
/* 6658 */     else if (align == 1) {
/* 6659 */       Rectangle bounds = getDataBounds(grid);
/*      */       double currentMaxRange;
/*      */       double currentMinRange;
/* 6662 */       double currentMaxRange; if (this.gridAlignment == 1) {
/* 6663 */         double currentMinRange = (grid.x - bounds.x) / bounds.width * (this.rightSampleAxisRange - this.leftSampleAxisRange) + this.leftSampleAxisRange;
/* 6664 */         currentMaxRange = (grid.width + grid.x - bounds.x) / bounds.width * (this.rightSampleAxisRange - this.leftSampleAxisRange) + this.leftSampleAxisRange;
/*      */       } else {
/* 6666 */         currentMinRange = (grid.y - bounds.y) / bounds.height * (this.rightSampleAxisRange - this.leftSampleAxisRange) + this.leftSampleAxisRange;
/* 6667 */         currentMaxRange = (grid.height + grid.y - bounds.y) / bounds.height * (this.rightSampleAxisRange - this.leftSampleAxisRange) + this.leftSampleAxisRange;
/*      */       }
/* 6669 */       factor = (value - currentMinRange) / (currentMaxRange - currentMinRange);
/*      */     }
/*      */     
/*      */ 
/* 6673 */     FontMetrics fm = g.getFontMetrics();
/* 6674 */     int fheight = fm.getHeight();
/* 6675 */     int descent = fm.getDescent();
/* 6676 */     int label_width = label != null ? fm.stringWidth(label) : 0;
/* 6677 */     int range_pos = this.rangePosition[index];
/* 6678 */     if ((target) && (this.targetLabelsPosition == -1)) {
/* 6679 */       range_pos = this.rangePosition[0];
/* 6680 */     } else if (target) {
/* 6681 */       range_pos = this.targetLabelsPosition;
/*      */     }
/* 6683 */     if (((this.gridAlignment == 1) && (align == 0)) || (
/* 6684 */       (this.gridAlignment == 0) && (align == 1)))
/*      */     {
/* 6686 */       int position = (int)Math.round(grid.y + grid.height - factor * grid.height);
/* 6687 */       if (align == 1) {
/* 6688 */         position = (int)Math.round(grid.y + factor * grid.height);
/*      */       }
/* 6690 */       boolean inside = (position > grid.y) && (position < grid.y + grid.height);
/* 6691 */       boolean room = position - fheight / 2 > grid.y + descent / 2;
/* 6692 */       room &= position + fheight / 2 < grid.y + grid.height - descent / 2;
/* 6693 */       room |= ((align == 1) && (position > grid.y) && (position < grid.y + grid.height));
/* 6694 */       room |= ((align == 0) && (value == 0.0D));
/* 6695 */       if (((inside) && ((room) || (target))) || ((align == 0) && ((value == this.currentLowerRange[index]) || (value == this.currentUpperRange[index]))))
/*      */       {
/* 6697 */         if (lineOn) {
/* 6698 */           g.setColor(color);
/* 6699 */           g.drawLine(grid.x + 1, position, grid.x + grid.width - 1, position);
/* 6700 */           if (this.display3dOn) {
/* 6701 */             g.drawLine(grid.x, position, grid.x - this.depth3dPoint.x, position - this.depth3dPoint.y);
/*      */           }
/*      */         }
/*      */         
/* 6705 */         if (label != null)
/*      */         {
/* 6707 */           position -= ((this.display3dOn) && (range_pos == 0) ? this.depth3dPoint.y : 0);
/*      */           
/*      */ 
/* 6710 */           int xpos = 0;
/* 6711 */           int label_x = 0;
/* 6712 */           g.setColor(labelColor);
/* 6713 */           if (range_pos == 0) {
/* 6714 */             xpos = this.rangeBounds[index].x + this.rangeBounds[index].width - (this.display3dOn ? this.depth3dPoint.x : 0);
/* 6715 */             if (target) {
/* 6716 */               index = 0;
/*      */               
/* 6718 */               for (int i = 0; i < this.rangeOn.length; i++) {
/* 6719 */                 if (this.rangePosition[i] == range_pos) {
/* 6720 */                   index = i;
/* 6721 */                   break;
/*      */                 }
/*      */               }
/* 6724 */               xpos = grid.x - (this.display3dOn ? this.depth3dPoint.x : 0);
/*      */             }
/* 6726 */             g.drawLine(xpos, position, xpos - 3, position);
/* 6727 */             label_x = xpos - label_width - 4;
/*      */             
/* 6729 */             if ((this.rangeAdjusterOn[index] != 0) && (getAdjusterPosition(index) == range_pos)) {
/* 6730 */               label_x -= 6;
/*      */             }
/*      */           } else {
/* 6733 */             xpos = this.rangeBounds[index].x;
/* 6734 */             if (target)
/*      */             {
/* 6736 */               index = 0;
/* 6737 */               for (int i = 0; i < this.rangeOn.length; i++) {
/* 6738 */                 if (this.rangePosition[i] == range_pos) {
/* 6739 */                   index = i;
/* 6740 */                   break;
/*      */                 }
/*      */               }
/* 6743 */               xpos = grid.x + grid.width;
/*      */             }
/* 6745 */             g.drawLine(xpos, position, xpos + 2, position);
/* 6746 */             label_x = xpos + 5;
/*      */             
/* 6748 */             if (((index == 1) && (this.rangeAdjusterPosition == 1)) || (
/* 6749 */               (this.rangeAdjusterOn[index] != 0) && (getAdjusterPosition(index) == range_pos))) {
/* 6750 */               label_x += 7;
/*      */             }
/*      */           }
/*      */           
/* 6754 */           int label_y = position + fheight - fm.getAscent() + 1;
/*      */           
/*      */ 
/* 6757 */           g.drawString(label, label_x, label_y);
/*      */         }
/*      */         
/*      */       }
/*      */       
/*      */ 
/*      */     }
/*      */     else
/*      */     {
/* 6766 */       String prefix = null;
/* 6767 */       String postfix = null;
/* 6768 */       if (index == 0) {
/* 6769 */         prefix = getLabel("rangeLabelPrefix");
/* 6770 */         postfix = getLabel("rangeLabelPostfix");
/*      */       } else {
/* 6772 */         prefix = getLabel("rangeLabelPrefix_" + (index + 1));
/* 6773 */         postfix = getLabel("rangeLabelPostfix_" + (index + 1));
/*      */       }
/* 6775 */       String upper = formatNumber(this.currentUpperRange[index], this.rangeDecimalCount[index]);
/* 6776 */       String lower = formatNumber(this.currentLowerRange[index], this.rangeDecimalCount[index]);
/* 6777 */       if ((prefix != null) && (prefix.length() > 0)) {
/* 6778 */         upper = prefix + upper;
/* 6779 */         lower = prefix + lower;
/*      */       }
/* 6781 */       if ((postfix != null) && (postfix.length() > 0)) {
/* 6782 */         upper = upper + postfix;
/* 6783 */         lower = lower + postfix;
/*      */       }
/* 6785 */       int position = (int)((value != 0.0D) || (align == 1) ? Math.round(grid.x + factor * grid.width) : this.zeroLine[index]);
/* 6786 */       boolean inside = (position > grid.x) && (position < grid.x + grid.width);
/* 6787 */       boolean room = position - label_width / 2 > grid.x + fm.stringWidth(lower) / 2;
/* 6788 */       room &= position + label_width / 2 < grid.x + grid.width - fm.stringWidth(upper) / 2;
/* 6789 */       room |= ((align == 1) && (position > grid.x) && (position < grid.x + grid.width));
/* 6790 */       room |= ((align == 0) && (value == 0.0D));
/*      */       
/* 6792 */       if (((inside) && ((room) || (target))) || ((align == 0) && ((value == this.currentLowerRange[index]) || (value == this.currentUpperRange[index]))))
/*      */       {
/* 6794 */         if (lineOn) {
/* 6795 */           g.setColor(color);
/* 6796 */           g.drawLine(position, grid.y + 1, position, grid.y + grid.height - 1);
/*      */           
/* 6798 */           if (this.display3dOn) {
/* 6799 */             g.drawLine(position, grid.y + grid.height, position - this.depth3dPoint.x, grid.y + grid.height - this.depth3dPoint.y);
/*      */           }
/*      */         }
/*      */         
/* 6803 */         if (label != null) {
/* 6804 */           g.setColor(labelColor);
/*      */           
/* 6806 */           position -= ((this.display3dOn) && (range_pos == 0) ? this.depth3dPoint.x : 0);
/*      */           
/*      */ 
/* 6809 */           int ypos = 0;
/* 6810 */           int label_y = 0;
/*      */           
/* 6812 */           if (range_pos == 0) {
/* 6813 */             ypos = this.rangeBounds[index].y - (this.display3dOn ? this.depth3dPoint.y : 0);
/* 6814 */             if (target) {
/* 6815 */               index = 0;
/*      */               
/* 6817 */               for (int i = 0; i < this.rangeOn.length; i++) {
/* 6818 */                 if (this.rangePosition[i] == range_pos) {
/* 6819 */                   index = i;
/* 6820 */                   break;
/*      */                 }
/*      */               }
/* 6823 */               ypos = grid.y + grid.height - (this.display3dOn ? this.depth3dPoint.y : 0);
/*      */             }
/* 6825 */             g.drawLine(position, ypos, position, ypos + 3);
/* 6826 */             label_y = ypos + fm.getHeight();
/* 6827 */             if ((target) && (this.rangeLabelsOn[index] != 0) && (range_pos == this.rangePosition[index])) {
/* 6828 */               label_y += fm.getHeight();
/*      */             }
/*      */             
/* 6831 */             if ((this.rangeAdjusterOn[index] != 0) && (getAdjusterPosition(index) == range_pos)) {
/* 6832 */               label_y += 6;
/*      */             }
/*      */           } else {
/* 6835 */             ypos = this.rangeBounds[index].y + this.rangeBounds[index].height;
/* 6836 */             if (target) {
/* 6837 */               index = 0;
/*      */               
/* 6839 */               for (int i = 0; i < this.rangeOn.length; i++) {
/* 6840 */                 if (this.rangePosition[i] == range_pos) {
/* 6841 */                   index = i;
/* 6842 */                   break;
/*      */                 }
/*      */               }
/* 6845 */               ypos = grid.y;
/*      */             }
/* 6847 */             g.drawLine(position, ypos, position, ypos - 3);
/* 6848 */             label_y = ypos - 5;
/* 6849 */             if ((target) && (this.rangeLabelsOn[index] != 0) && (range_pos == this.rangePosition[index])) {
/* 6850 */               label_y -= fm.getHeight();
/*      */             }
/*      */             
/* 6853 */             if (((index == 1) && (this.rangeAdjusterPosition == 1)) || (
/* 6854 */               (this.rangeAdjusterOn[index] != 0) && (getAdjusterPosition(index) == range_pos))) {
/* 6855 */               label_y -= 6;
/*      */             }
/*      */           }
/*      */           
/* 6859 */           int label_x = position - label_width / 2;
/*      */           
/* 6861 */           g.drawString(label, label_x, label_y);
/*      */         }
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
/*      */   Dimension getImageSize(String name)
/*      */   {
/* 6878 */     Dimension imageSize = new Dimension(0, 0);
/*      */     
/* 6880 */     if (name != null) {
/* 6881 */       Image img = (Image)this.images.get(name);
/*      */       
/* 6883 */       if (img != null) {
/* 6884 */         imageSize.width = img.getWidth(this);
/* 6885 */         imageSize.height = img.getHeight(this);
/*      */       }
/*      */     }
/* 6888 */     return imageSize;
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
/*      */   protected Dimension getLabelSize(String label, FontMetrics fm)
/*      */   {
/* 6901 */     if ((label == null) || (fm == null)) {
/* 6902 */       return new Dimension(0, 0);
/*      */     }
/*      */     
/*      */ 
/*      */ 
/* 6907 */     Long key = new Long(label.hashCode() + fm.getFont().hashCode());
/* 6908 */     Dimension size = (Dimension)this.labelSizeCache.get(key);
/*      */     
/*      */ 
/* 6911 */     if (size == null) {
/* 6912 */       size = new Dimension(0, 0);
/*      */       
/* 6914 */       if (label.indexOf("\n") == -1) {
/* 6915 */         size.width = (fm.stringWidth(label) + 2);
/* 6916 */         size.height = (fm.getMaxAscent() + 2);
/*      */ 
/*      */       }
/*      */       else
/*      */       {
/* 6921 */         String[] lines = getStringValues(label, "\n");
/* 6922 */         for (int i = 0; (lines != null) && (i < lines.length); i++) {
/* 6923 */           size.height += fm.getMaxAscent() + 1;
/* 6924 */           if (lines[i] != null) {
/* 6925 */             size.width = Math.max(size.width, fm.stringWidth(lines[i]) + 2);
/*      */           }
/*      */         }
/* 6928 */         size.height += 1;
/*      */       }
/* 6930 */       size.height = Math.max(size.height, fm.getMaxAscent());
/*      */       
/* 6932 */       if (!this.servletModeOn) {
/* 6933 */         this.labelSizeCache.put(key, size);
/*      */       }
/*      */     }
/*      */     
/* 6937 */     if (size != null) {
/* 6938 */       return size;
/*      */     }
/* 6940 */     return new Dimension(0, 0);
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
/*      */   Dimension getAngledLabelSize(Dimension size, int angle)
/*      */   {
/* 6953 */     if ((angle == 90) || (angle == 270)) {
/* 6954 */       return new Dimension(size.height, size.width + 20);
/*      */     }
/*      */     
/* 6957 */     if ((angle > 0) && (angle < 360))
/*      */     {
/* 6959 */       double sin_angle = Math.sin(angle * 0.017453292519943295D);
/* 6960 */       double cos_angle = Math.cos(angle * 0.017453292519943295D);
/* 6961 */       int width = (int)(Math.abs(size.width * cos_angle) + Math.abs(size.height * sin_angle) + 0.5D);
/* 6962 */       int height = (int)(Math.abs(size.width * sin_angle) + Math.abs(size.height * cos_angle) + 0.5D);
/* 6963 */       return new Dimension(width, height);
/*      */     }
/*      */     
/*      */ 
/* 6967 */     return size;
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
/*      */   void paintLabel(Graphics g, String label, int label_x, int label_y, Dimension size, int style, int angle, boolean underlined)
/*      */   {
/* 6985 */     FontMetrics fm = g.getFontMetrics();
/* 6986 */     if ((label != null) && (angle == 0)) {
/* 6987 */       String[] lines = getStringValues(label, "\n");
/* 6988 */       int i = 0;
/* 6989 */       do { if ((lines[i] != null) && (!lines[i].equals("\n"))) {
/* 6990 */           int x = label_x;
/* 6991 */           int y = label_y;
/* 6992 */           if (style != -1) {
/* 6993 */             if (style == 1) {
/* 6994 */               x = label_x + size.width - fm.stringWidth(lines[i]);
/*      */             } else
/* 6996 */               x = label_x + size.width / 2 - fm.stringWidth(lines[i]) / 2;
/*      */           }
/* 6998 */           g.drawString(lines[i], x, y);
/* 6999 */           if (underlined) {
/* 7000 */             g.drawLine(x, y + 1, x + fm.stringWidth(lines[i]) - 2, y + 1);
/*      */           }
/*      */         }
/* 7003 */         label_y += fm.getMaxAscent() + 1;i++;
/* 6988 */         if (lines == null) break; } while (i < lines.length);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     }
/* 7007 */     else if ((label != null) && (size.width != 0) && (size.height != 0)) {
/* 7008 */       paintAngledLabel(g, label, label_x, label_y, size, style, angle, underlined);
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
/*      */   void paintAngledLabel(Graphics g, String label, int label_x, int label_y, Dimension size, int style, int angle, boolean underlined)
/*      */   {
/* 7028 */     if (fifo_count != angledLabelCache.size()) {
/* 7029 */       fifo_clear();
/*      */     }
/* 7031 */     FontMetrics fm = g.getFontMetrics();
/*      */     
/*      */ 
/* 7034 */     Image angled_label = null;
/* 7035 */     Long key = null;
/* 7036 */     if ((fifo_queue != null) && (!this.servletModeOn)) {
/* 7037 */       key = new Long(hashCode(label) + angle + g.getFont().hashCode() + g.getColor().hashCode());
/* 7038 */       angled_label = (Image)angledLabelCache.get(key);
/*      */     }
/*      */     
/*      */ 
/*      */ 
/* 7043 */     if (angled_label == null) {
/* 7044 */       boolean is_java2 = !System.getProperty("java.version").startsWith("1.1");
/* 7045 */       if (!is_java2)
/*      */       {
/* 7047 */         if ((this.rotateImage == null) || (size.width > this.rotateImage.getWidth(this)) || (size.height > this.rotateImage.getHeight(this)))
/*      */         {
/* 7049 */           if (this.rotateImage != null) {
/* 7050 */             this.rotateImage.flush();
/* 7051 */             this.rotateImage = null;
/* 7052 */             System.gc();
/*      */           }
/*      */           try
/*      */           {
/* 7056 */             this.rotateImage = createImage(size.width, size.height);
/*      */           }
/*      */           catch (Throwable e) {
/* 7059 */             fifo_clear();
/*      */             try {
/* 7061 */               this.rotateImage = createImage(size.width, size.height);
/*      */             }
/*      */             catch (Throwable ex) {
/*      */               try {
/* 7065 */                 this.rotateImage = createImage_oldstyle(size.width, size.height);
/*      */               }
/*      */               catch (Throwable localThrowable1) {}
/*      */             }
/*      */           }
/*      */         }
/*      */         
/*      */ 
/*      */ 
/* 7074 */         if (this.rotateImage != null) {
/* 7075 */           Color[] colors = new Color[2];
/* 7076 */           colors[1] = g.getColor();
/* 7077 */           int[] color_pixels = new int[2];
/* 7078 */           getColorPixels(colors, color_pixels);
/*      */           
/*      */ 
/* 7081 */           Graphics g_label = this.rotateImage.getGraphics();
/* 7082 */           g_label.setColor(colors[0]);
/* 7083 */           g_label.fillRect(0, 0, size.width, size.height);
/* 7084 */           g_label.setColor(colors[1]);
/* 7085 */           g_label.setFont(g.getFont());
/* 7086 */           paintLabel(g_label, label, 0, fm.getMaxAscent() - fm.getDescent() + 2, size, 0, 0, false);
/* 7087 */           g_label.dispose();
/* 7088 */           g_label = null;
/*      */           
/*      */ 
/* 7091 */           int[] pixels = new int[size.width * size.height];
/* 7092 */           PixelGrabber grabber = new PixelGrabber(this.rotateImage, 0, 0, size.width, size.height, pixels, 0, size.width);
/* 7093 */           grabber.startGrabbing();
/* 7094 */           grabber.getPixels();
/*      */           
/*      */ 
/*      */ 
/* 7098 */           int foreground = colors[1].getRGB();
/* 7099 */           for (int i = 0; i < pixels.length; i++) {
/* 7100 */             if (pixels[i] == color_pixels[0]) {
/* 7101 */               pixels[i] = (color_pixels[0] & 0xFFFFFF);
/*      */             } else {
/* 7103 */               pixels[i] = foreground;
/*      */             }
/*      */           }
/*      */           
/*      */ 
/*      */ 
/*      */ 
/* 7110 */           Dimension newSize = new Dimension(size.width, size.height);
/* 7111 */           int[] new_pixels = rotateImage(pixels, angle, newSize, color_pixels[0]);
/*      */           
/*      */           try
/*      */           {
/* 7115 */             angled_label = createImage(new MemoryImageSource(newSize.width, newSize.height, new_pixels, 0, newSize.width));
/*      */           }
/*      */           catch (Throwable localThrowable2) {}
/*      */         }
/*      */       }
/*      */       else
/*      */       {
/* 7122 */         Dimension angled_size = getAngledLabelSize(size, angle);
/* 7123 */         Float radian = new Float(angle / 180.0D * 3.141592653589793D);
/* 7124 */         Float f_width = new Float(angled_size.width / 2.0D);
/* 7125 */         Float f_height = new Float(angled_size.height / 2.0D);
/* 7126 */         int delta_y = (angled_size.height - size.height) / 2;
/* 7127 */         int delta_x = (angled_size.width - size.width) / 2;
/*      */         
/*      */         try
/*      */         {
/* 7131 */           Class bufferedImageClass = Class.forName("java.awt.image.BufferedImage");
/* 7132 */           Class[] parameters = { Integer.TYPE, Integer.TYPE, Integer.TYPE };
/* 7133 */           this.bufferedImageConstructor = bufferedImageClass.getConstructor(parameters);
/* 7134 */           Field field = bufferedImageClass.getField("TYPE_INT_ARGB");
/* 7135 */           int TYPE_INT_ARGB = field.getInt(null);
/* 7136 */           Object[] params = { new Integer(angled_size.width), new Integer(angled_size.height), new Integer(TYPE_INT_ARGB) };
/* 7137 */           angled_label = (Image)this.bufferedImageConstructor.newInstance(params);
/*      */           
/*      */ 
/* 7140 */           Class affineTransformClass = Class.forName("java.awt.geom.AffineTransform");
/* 7141 */           Object affineTransform = affineTransformClass.newInstance();
/*      */           
/*      */ 
/* 7144 */           parameters = new Class[] { Double.TYPE, Double.TYPE, Double.TYPE };
/* 7145 */           Method method = affineTransformClass.getMethod("setToRotation", parameters);
/* 7146 */           params = new Object[] { radian, f_width, f_height };
/* 7147 */           method.invoke(affineTransform, params);
/*      */           
/*      */ 
/* 7150 */           Graphics g2D = angled_label.getGraphics();
/* 7151 */           parameters = new Class[] { affineTransformClass };
/* 7152 */           Class g2DClass = Class.forName("java.awt.Graphics2D");
/* 7153 */           Method method1 = g2DClass.getMethod("setTransform", parameters);
/* 7154 */           params = new Object[] { affineTransform };
/* 7155 */           method1.invoke(g2D, params);
/*      */           
/*      */ 
/* 7158 */           Class renderingHintsClass = Class.forName("java.awt.RenderingHints");
/* 7159 */           Class renderingHintsKeyClass = Class.forName("java.awt.RenderingHints$Key");
/* 7160 */           Class objectClass = Class.forName("java.lang.Object");
/* 7161 */           parameters = new Class[] { renderingHintsKeyClass, objectClass };
/* 7162 */           Method method2 = g2DClass.getMethod("setRenderingHint", parameters);
/* 7163 */           Field field1 = renderingHintsClass.getField("KEY_ANTIALIASING");
/* 7164 */           Field field2 = renderingHintsClass.getField("VALUE_ANTIALIAS_ON");
/* 7165 */           params = new Object[] { field1.get(null), field2.get(null) };
/* 7166 */           method2.invoke(g2D, params);
/*      */           
/*      */ 
/* 7169 */           g2D.setColor(g.getColor());
/* 7170 */           paintLabel(g2D, label, delta_x, delta_y + fm.getAscent() - fm.getDescent() + 2, size, 0, 0, false);
/*      */         }
/*      */         catch (ClassNotFoundException localClassNotFoundException) {}catch (NoSuchMethodException localNoSuchMethodException) {}catch (NoSuchFieldException localNoSuchFieldException) {}catch (InstantiationException localInstantiationException) {}catch (IllegalAccessException localIllegalAccessException) {}catch (InvocationTargetException localInvocationTargetException) {}
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
/* 7185 */     if ((fifo_queue != null) && (!this.servletModeOn) && (key != null) && (angled_label != null)) {
/* 7186 */       synchronized (fifo_queue) {
/* 7187 */         if (fifo_count >= fifo_queue.length)
/*      */         {
/* 7189 */           Object[] image_and_key = fifo_remove();
/* 7190 */           if (image_and_key != null)
/*      */           {
/* 7192 */             angledLabelCache.remove(image_and_key[0]);
/* 7193 */             ((Image)image_and_key[1]).flush();
/*      */           }
/*      */         }
/*      */         
/* 7197 */         angledLabelCache.put(key, angled_label);
/* 7198 */         fifo_add(new Object[] { key, angled_label });
/*      */       }
/*      */     }
/*      */     
/*      */ 
/*      */ 
/* 7204 */     if (angle == 180) {
/* 7205 */       label_y -= fm.getAscent() - 2;
/*      */     }
/*      */     
/*      */ 
/* 7209 */     if (angled_label != null) {
/* 7210 */       g.drawImage(angled_label, label_x, label_y, null);
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
/*      */   public static String[] getStringValues(String string, String delimiter)
/*      */   {
/* 7224 */     if (delimiter == null) {
/* 7225 */       delimiter = ",";
/*      */     }
/*      */     
/*      */ 
/* 7229 */     String[] labels = null;
/* 7230 */     if (string == null) {
/* 7231 */       labels = new String[0];
/*      */ 
/*      */ 
/*      */     }
/* 7235 */     else if (string.indexOf(delimiter) == -1) {
/* 7236 */       labels = new String[] { string };
/*      */ 
/*      */     }
/*      */     else
/*      */     {
/*      */ 
/* 7242 */       int length = string.length();
/* 7243 */       int delimiter_length = delimiter.length();
/* 7244 */       int count = 1;
/* 7245 */       int index = 0;
/* 7246 */       while (index >= 0) {
/* 7247 */         index = string.indexOf(delimiter, index);
/* 7248 */         if ((index != -1) && (index != length - delimiter_length)) {
/* 7249 */           index += delimiter_length;
/* 7250 */           count++;
/* 7251 */         } else if (index == length - delimiter_length) {
/* 7252 */           index = -1;
/* 7253 */           count++;
/*      */         }
/*      */       }
/*      */       
/*      */ 
/* 7258 */       labels = new String[count];
/* 7259 */       int start = 0;
/* 7260 */       for (int i = 0; i < count; i++)
/*      */       {
/* 7262 */         boolean is_delimiter = false;
/* 7263 */         if ((start < length) && (string.charAt(start) == delimiter.charAt(0))) {
/* 7264 */           is_delimiter = true;
/* 7265 */           for (int c = 1; c < delimiter_length; c++) {
/* 7266 */             if (string.charAt(start + c) != delimiter.charAt(c)) {
/* 7267 */               is_delimiter = false;
/*      */             }
/*      */           }
/*      */         }
/*      */         
/*      */ 
/* 7273 */         if (is_delimiter) {
/* 7274 */           labels[i] = "";
/* 7275 */           start += delimiter_length;
/*      */         }
/*      */         else
/*      */         {
/* 7279 */           int end = string.indexOf(delimiter, start + 1);
/* 7280 */           String sub = null;
/* 7281 */           if (end > 0) {
/* 7282 */             sub = string.substring(start, end);
/*      */           } else {
/* 7284 */             sub = string.substring(start);
/*      */           }
/* 7286 */           sub = trim(sub);
/* 7287 */           labels[i] = sub;
/* 7288 */           start = end + delimiter_length;
/*      */         }
/*      */       }
/*      */     }
/* 7292 */     return labels;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   static String trim(String string)
/*      */   {
/* 7302 */     if (string == null)
/* 7303 */       return null;
/* 7304 */     if (string.indexOf("\n") == -1) {
/* 7305 */       return string.trim();
/*      */     }
/* 7307 */     int len = string.length();
/* 7308 */     int st = 0;
/*      */     do {
/* 7310 */       st++;
/* 7309 */       if ((st >= len) || (string.charAt(st) > ' ')) break; } while (string.charAt(st) != '\n');
/*      */     
/*      */ 
/* 7312 */     while ((st < len) && (string.charAt(len - 1) <= ' ') && (string.charAt(len - 1) != '\n')) {
/* 7313 */       len--;
/*      */     }
/* 7315 */     return (st > 0) || (len < string.length()) ? string.substring(st, len) : string;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   private static void fifo_add(Object[] image_and_key)
/*      */   {
/* 7326 */     if ((image_and_key == null) || (image_and_key.length != 2) || ((fifo_head == fifo_tail) && (fifo_queue[fifo_tail] != null))) {
/* 7327 */       return;
/*      */     }
/*      */     
/*      */ 
/* 7331 */     fifo_tail += 1;
/* 7332 */     if (fifo_tail >= fifo_queue.length) {
/* 7333 */       fifo_tail = 0;
/*      */     }
/* 7335 */     fifo_queue[fifo_tail] = image_and_key;
/* 7336 */     fifo_count += 1;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   private static Object[] fifo_remove()
/*      */   {
/* 7346 */     if ((fifo_head == fifo_tail) && (fifo_queue[fifo_tail] == null)) {
/* 7347 */       return null;
/*      */     }
/*      */     
/*      */ 
/* 7351 */     fifo_head += 1;
/* 7352 */     if (fifo_head >= fifo_queue.length) {
/* 7353 */       fifo_head = 0;
/*      */     }
/*      */     
/*      */ 
/* 7357 */     Object[] image_and_key = (Object[])fifo_queue[fifo_head];
/* 7358 */     fifo_queue[fifo_head] = null;
/*      */     
/*      */ 
/* 7361 */     fifo_count -= 1;
/* 7362 */     return image_and_key;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   static void fifo_clear()
/*      */   {
/* 7370 */     synchronized (fifo_queue) {
/* 7371 */       for (int i = 0; (fifo_queue != null) && (i < fifo_queue.length); i++) {
/* 7372 */         Object[] image_and_key = (Object[])fifo_queue[i];
/* 7373 */         if ((image_and_key != null) && (image_and_key[1] != null)) {
/* 7374 */           ((Image)image_and_key[1]).flush();
/*      */         }
/* 7376 */         fifo_queue[i] = null;
/*      */       }
/* 7378 */       fifo_count = 0;
/* 7379 */       fifo_head = 0;
/* 7380 */       fifo_tail = 0;
/* 7381 */       angledLabelCache.clear();
/* 7382 */       System.gc();
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   int hashCode(String s)
/*      */   {
/* 7393 */     int h = 0;
/* 7394 */     int off = 0;
/* 7395 */     int len = s.length();
/* 7396 */     char[] val = new char[len];
/* 7397 */     s.getChars(0, len, val, 0);
/* 7398 */     for (int i = len; i > 0; i--) {
/* 7399 */       h = h * 37 + val[(off++)];
/*      */     }
/* 7401 */     return h;
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
/*      */   private void getColorPixels(Color[] colors, int[] color_pixels)
/*      */   {
/* 7414 */     colors[0] = Color.white;
/* 7415 */     if (colors[0].equals(colors[1])) {
/* 7416 */       colors[0] = Color.black;
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 7423 */     if (this.pixel_grabber_image == null) {
/*      */       try {
/* 7425 */         this.pixel_grabber_image = createImage(2, 1);
/*      */ 
/*      */ 
/*      */       }
/*      */       catch (Throwable e)
/*      */       {
/*      */ 
/* 7432 */         this.pixel_grabber_image = createImage_oldstyle(2, 1);
/*      */       }
/*      */     }
/* 7435 */     if (this.pixel_grabber_image != null) {
/* 7436 */       Graphics g = this.pixel_grabber_image.getGraphics();
/* 7437 */       g.setColor(colors[0]);
/* 7438 */       g.drawLine(0, 0, 0, 0);
/* 7439 */       g.setColor(colors[1]);
/* 7440 */       g.drawLine(1, 0, 1, 0);
/* 7441 */       PixelGrabber grabber = new PixelGrabber(this.pixel_grabber_image, 0, 0, 2, 1, color_pixels, 0, 2);
/* 7442 */       grabber.startGrabbing();
/* 7443 */       grabber.getPixels();
/* 7444 */       if (color_pixels[0] == color_pixels[1]) {
/* 7445 */         colors[0] = new Color(8388607);
/*      */       }
/* 7447 */       g.dispose();
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
/*      */   private int[] rotateImage(int[] pixels, int angle, Dimension size, int background)
/*      */   {
/* 7462 */     if (angle % 360 == 0) {
/* 7463 */       return pixels;
/*      */     }
/*      */     
/*      */ 
/* 7467 */     if (angle % 360 == 90)
/*      */     {
/* 7469 */       int[] new_pixels = new int[pixels.length];
/* 7470 */       for (int col = 0; col < size.height; col++) {
/* 7471 */         for (int row = 0; row < size.width; row++) {
/* 7472 */           new_pixels[(row * size.height + col)] = pixels[(size.width * size.height - size.width - col * size.width + row)];
/*      */         }
/*      */       }
/*      */       
/* 7476 */       int width = size.width;
/* 7477 */       size.width = size.height;
/* 7478 */       size.height = width;
/* 7479 */       return new_pixels;
/*      */     }
/*      */     
/*      */ 
/* 7483 */     if (angle % 360 == 180)
/*      */     {
/* 7485 */       int[] new_pixels = new int[pixels.length];
/* 7486 */       for (int col = 0; col < size.width; col++) {
/* 7487 */         for (int row = 0; row < size.height; row++) {
/* 7488 */           new_pixels[(row * size.width + col)] = pixels[(size.width * size.height - 1 - row * size.width - col)];
/*      */         }
/*      */       }
/* 7491 */       return new_pixels;
/*      */     }
/*      */     
/*      */ 
/* 7495 */     if (angle % 360 == 270)
/*      */     {
/* 7497 */       int[] new_pixels = new int[pixels.length];
/* 7498 */       for (int col = 0; col < size.height; col++) {
/* 7499 */         for (int row = 0; row < size.width; row++) {
/* 7500 */           new_pixels[(row * size.height + col)] = pixels[(col * size.width + size.width - row - 1)];
/*      */         }
/*      */       }
/*      */       
/* 7504 */       int width = size.width;
/* 7505 */       size.width = size.height;
/* 7506 */       size.height = width;
/* 7507 */       return new_pixels;
/*      */     }
/*      */     
/*      */ 
/*      */ 
/* 7512 */     angle = -angle;
/* 7513 */     double sin = Math.sin(angle * 0.017453292519943295D);
/* 7514 */     double cos = Math.cos(angle * 0.017453292519943295D);
/* 7515 */     double[] coord = new double[2];
/* 7516 */     Rectangle bounds = new Rectangle(size);
/* 7517 */     getAngledBounds(cos, sin, bounds, coord);
/* 7518 */     int xoffset = -bounds.x;
/* 7519 */     int yoffset = -bounds.y;
/*      */     
/* 7521 */     int[] new_pixels = new int[bounds.width * bounds.height];
/* 7522 */     int[] line_pixels = new int[bounds.width];
/*      */     
/*      */ 
/* 7525 */     for (int dy = 0; dy < bounds.height; dy++) {
/* 7526 */       itransform(cos, sin, 0 - xoffset, dy - yoffset, coord);
/* 7527 */       double x1 = coord[0];
/* 7528 */       double y1 = coord[1];
/*      */       
/* 7530 */       itransform(cos, sin, bounds.width - xoffset, dy - yoffset, coord);
/* 7531 */       double x2 = coord[0];
/* 7532 */       double y2 = coord[1];
/* 7533 */       double xinc = (x2 - x1) / bounds.width;
/* 7534 */       double yinc = (y2 - y1) / bounds.width;
/* 7535 */       for (int dx = 0; dx < bounds.width; dx++) {
/* 7536 */         int sx = (int)Math.round(x1);
/* 7537 */         int sy = (int)Math.round(y1);
/* 7538 */         if ((sx < 0) || (sy < 0) || (sx >= size.width) || (sy >= size.height)) {
/* 7539 */           line_pixels[dx] = (background & 0xFFFFFF);
/*      */         } else {
/* 7541 */           line_pixels[dx] = pixels[(sy * size.width + sx)];
/*      */         }
/* 7543 */         x1 += xinc;
/* 7544 */         y1 += yinc;
/*      */       }
/* 7546 */       System.arraycopy(line_pixels, 0, new_pixels, dy * bounds.width, line_pixels.length);
/*      */     }
/* 7548 */     size.width = bounds.width;
/* 7549 */     size.height = bounds.height;
/* 7550 */     return new_pixels;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   private void transform(double cos, double sin, double x, double y, double[] coord)
/*      */   {
/* 7558 */     coord[0] = (cos * x + sin * y);
/* 7559 */     coord[1] = (cos * y - sin * x);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   private void itransform(double cos, double sin, double x, double y, double[] coord)
/*      */   {
/* 7567 */     coord[0] = (cos * x - sin * y);
/* 7568 */     coord[1] = (cos * y + sin * x);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   private void getAngledBounds(double cos, double sin, Rectangle rect, double[] coord)
/*      */   {
/* 7576 */     double minx = Double.POSITIVE_INFINITY;
/* 7577 */     double miny = Double.POSITIVE_INFINITY;
/* 7578 */     double maxx = Double.NEGATIVE_INFINITY;
/* 7579 */     double maxy = Double.NEGATIVE_INFINITY;
/* 7580 */     for (int y = 0; y <= 1; y++) {
/* 7581 */       for (int x = 0; x <= 1; x++) {
/* 7582 */         transform(cos, sin, rect.x + x * rect.width, rect.y + y * rect.height, coord);
/* 7583 */         minx = Math.min(minx, coord[0]);
/* 7584 */         miny = Math.min(miny, coord[1]);
/* 7585 */         maxx = Math.max(maxx, coord[0]);
/* 7586 */         maxy = Math.max(maxy, coord[1]);
/*      */       }
/*      */     }
/* 7589 */     rect.x = ((int)Math.floor(minx));
/* 7590 */     rect.y = ((int)Math.floor(miny));
/* 7591 */     rect.width = ((int)Math.ceil(maxx) - rect.x + 1);
/* 7592 */     rect.height = ((int)Math.ceil(maxy) - rect.y + 1);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   void paintTitle(Graphics g, Dimension size)
/*      */   {
/* 7602 */     if ((this.chartTitleOn) && (this.chartTitle != null))
/*      */     {
/* 7604 */       Font font = getFont("titleFont");
/* 7605 */       FontMetrics fm = getFontMetrics(font);
/* 7606 */       Dimension labelSize = getLabelSize(this.chartTitle, fm);
/* 7607 */       int label_x = size.width / 2 - labelSize.width / 2;
/* 7608 */       int label_y = fm.getMaxAscent() + 2;
/*      */       
/* 7610 */       g.setColor(getForeground());
/* 7611 */       g.setFont(font);
/* 7612 */       paintLabel(g, this.chartTitle, label_x, label_y, labelSize, 2, 0, false);
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
/*      */   void paintLegend(Graphics g, Rectangle bounds, String[] labels)
/*      */   {
/* 7625 */     if ((g == null) || (bounds == null) || (labels == null)) {
/* 7626 */       return;
/*      */     }
/* 7628 */     Font legendFont = getFont("legendFont");
/* 7629 */     g.setClip(this.visibleLegend.x, this.visibleLegend.y, this.visibleLegend.width - 2, this.visibleLegend.height + 1);
/*      */     
/*      */ 
/* 7632 */     if ((this.legendBounds == null) || (this.legendBounds.length != labels.length)) {
/* 7633 */       this.legendBounds = new Rectangle[labels.length];
/* 7634 */       for (int i = 0; i < this.legendBounds.length; i++) {
/* 7635 */         this.legendBounds[i] = new Rectangle();
/*      */       }
/*      */     }
/*      */     
/*      */ 
/* 7640 */     int rows = 0;
/* 7641 */     int columns = getLegendColumns();
/* 7642 */     int rest = 0;
/*      */     
/* 7644 */     if (columns == 0) {
/* 7645 */       if ((this.legendPosition == 1) || (this.legendPosition == 0)) {
/* 7646 */         columns = 1;
/* 7647 */         rows = labels.length;
/*      */       } else {
/* 7649 */         columns = labels.length;
/* 7650 */         rows = 1;
/*      */       }
/*      */     } else {
/* 7653 */       rows = labels.length / columns;
/* 7654 */       rest = labels.length % columns;
/* 7655 */       if (rest > 0) {
/* 7656 */         rows++;
/*      */       }
/*      */     }
/*      */     
/*      */ 
/* 7661 */     int boxSize = 6;
/* 7662 */     int index = 0;
/* 7663 */     FontMetrics fm = getFontMetrics(legendFont);
/* 7664 */     g.setFont(legendFont);
/* 7665 */     int sampleCount = getSampleCount();
/* 7666 */     int seriesCount = getSeriesCount();
/*      */     
/*      */ 
/* 7669 */     int xpos = this.visibleLegend.x;
/* 7670 */     if (this.legend.width > this.visibleLegend.width) {
/* 7671 */       double offset = this.horizontalLegendFactor * (this.legend.width - this.visibleLegend.width);
/* 7672 */       xpos -= (int)offset;
/*      */     }
/*      */     
/* 7675 */     int ypos = 0;
/* 7676 */     index = 0;
/* 7677 */     if (this.legendReverseOn) {
/* 7678 */       index = labels.length - 1;
/*      */     }
/* 7680 */     for (int i = 0; i < columns; i++) {
/* 7681 */       for (int j = 0; j < rows; j++)
/*      */       {
/* 7683 */         if ((rest <= 0) || (i < rest) || (j != rows - 1))
/*      */         {
/*      */ 
/* 7686 */           if (j == 0)
/*      */           {
/* 7688 */             ypos = this.legend.y + 1;
/* 7689 */             if (this.legend.height > this.visibleLegend.height) {
/* 7690 */               double offset = this.verticalLegendFactor * (this.legend.height - this.visibleLegend.height);
/* 7691 */               ypos -= (int)offset;
/*      */             }
/*      */           }
/*      */           
/*      */ 
/* 7696 */           String imageName = getLegendImage(index);
/* 7697 */           Dimension imageSize = getImageSize(imageName);
/* 7698 */           imageSize.height = (imageSize.height > 0 ? imageSize.height : boxSize);
/* 7699 */           imageSize.width = (imageSize.width > 0 ? imageSize.width : boxSize);
/* 7700 */           int image_ypos = ypos + 3;
/* 7701 */           int image_xpos = xpos;
/*      */           
/*      */ 
/* 7704 */           Dimension iSize = getImageSize(imageName);
/* 7705 */           if ((imageName != null) && (iSize.height > 0) && (iSize.width > 0)) {
/* 7706 */             Image image = (Image)this.images.get(imageName);
/* 7707 */             if (image != null) {
/* 7708 */               g.drawImage(image, image_xpos, image_ypos, this);
/*      */             }
/*      */             
/*      */ 
/*      */           }
/*      */           else
/*      */           {
/* 7715 */             Color color = getLegendColor(index);
/* 7716 */             if (color != null) {
/* 7717 */               g.setColor(color);
/*      */             }
/* 7719 */             g.fillRect(image_xpos, image_ypos, boxSize, boxSize);
/* 7720 */             g.setColor(Color.black);
/* 7721 */             g.drawRect(image_xpos, image_ypos, boxSize, boxSize);
/*      */           }
/*      */           
/*      */ 
/* 7725 */           g.setColor(getForeground());
/* 7726 */           if ((seriesCount == 1) && (index < sampleCount)) {
/* 7727 */             Color color = getSampleLabelColor(index);
/* 7728 */             if (color != null) {
/* 7729 */               g.setColor(color);
/*      */             }
/* 7731 */           } else if ((seriesCount > 1) && (index < seriesCount)) {
/* 7732 */             Color color = getSeriesLabelColor(index);
/* 7733 */             if (color != null) {
/* 7734 */               g.setColor(color);
/*      */             }
/*      */           }
/*      */           
/*      */           String lableWithValue;
/*      */           
/*      */           String lableWithValue;
/*      */           
/* 7742 */           if ((seriesCount == 1) && (this.chartType.equalsIgnoreCase("pie"))) {
/* 7743 */             lableWithValue = labels[index] + " (" + formatNumber(getSampleValue(0, index), getSampleDecimalCount(0)) + ")";
/*      */           }
/*      */           else {
/* 7746 */             lableWithValue = labels[index];
/*      */           }
/* 7748 */           Dimension labelSize = getLabelSize(lableWithValue, fm);
/* 7749 */           int imageHeight = imageSize.height > 0 ? imageSize.height : 6;
/* 7750 */           int label_y = ypos + fm.getMaxAscent() + 3;
/* 7751 */           label_y -= (imageHeight < fm.getMaxAscent() - fm.getDescent() ? fm.getDescent() + 1 : 0);
/* 7752 */           int label_x = xpos + imageSize.width + 6;
/* 7753 */           paintLabel(g, lableWithValue, label_x, label_y, labelSize, -1, 0, false);
/*      */           
/*      */ 
/* 7756 */           Rectangle b = this.legendBounds[index];
/* 7757 */           b.x = (xpos - 3);
/* 7758 */           b.y = (imageHeight < fm.getMaxAscent() - fm.getDescent() ? image_ypos - 4 : image_ypos - 3);
/* 7759 */           b.width = (labelSize.width + label_x - xpos + 6);
/* 7760 */           b.height = Math.max(imageSize.height + 5, labelSize.height + 2);
/* 7761 */           b.height = Math.max(b.height, boxSize + 6);
/* 7762 */           if ((index >= 0) && (index < this.legendSelection.length) && 
/* 7763 */             (this.legendSelection[index] != 0)) {
/* 7764 */             g.setClip(this.visibleLegend.x - 3, this.visibleLegend.y, this.visibleLegend.width + 1, this.visibleLegend.height + 1);
/* 7765 */             g.setColor(getForeground());
/* 7766 */             g.drawRect(b.x, b.y, b.width, b.height);
/* 7767 */             g.setClip(this.visibleLegend.x, this.visibleLegend.y, this.visibleLegend.width - 2, this.visibleLegend.height + 1);
/*      */           }
/*      */           
/*      */ 
/*      */ 
/* 7772 */           if (this.legendRowHeight != null) {
/* 7773 */             ypos += this.legendRowHeight[j];
/*      */           }
/* 7775 */           if (!this.legendReverseOn) {
/* 7776 */             index++;
/*      */           } else {
/* 7778 */             index--;
/*      */           }
/*      */         }
/*      */       }
/* 7782 */       if (this.legendColumnWidth != null) {
/* 7783 */         xpos += this.legendColumnWidth[i];
/*      */       }
/*      */     }
/* 7786 */     g.setClip(0, 0, 10000, 10000);
/* 7787 */     paintLegendScrollers(g);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   private void paintLegendScrollers(Graphics g)
/*      */   {
/* 7798 */     if ((this.verticalLegendScrollerOn) && (this.visibleLegend.height > 25))
/*      */     {
/* 7800 */       int ypos = this.visibleLegend.y;
/* 7801 */       int xpos = this.visibleLegend.x + this.visibleLegend.width - 2;
/* 7802 */       if (this.legendPosition == 0) {
/* 7803 */         xpos = this.visibleLegend.x - 13;
/*      */       }
/*      */       
/*      */ 
/* 7807 */       g.setColor(getBackground().darker());
/* 7808 */       g.drawLine(xpos + 4, ypos, xpos + 4, ypos + this.visibleLegend.height);
/* 7809 */       g.setColor(getBackground().brighter());
/* 7810 */       g.drawLine(xpos + 5, ypos, xpos + 5, ypos + this.visibleLegend.height);
/*      */       
/*      */ 
/* 7813 */       paintBox(g, Color.lightGray, xpos, ypos, 8, 8, this.topLegendPushed);
/* 7814 */       ypos += (this.topLegendPushed ? 1 : 0);
/* 7815 */       g.setColor(Color.black);
/* 7816 */       g.drawLine(xpos + 4, ypos + 3, xpos + 4, ypos + 3);
/* 7817 */       g.drawLine(xpos + 3, ypos + 4, xpos + 5, ypos + 4);
/* 7818 */       g.drawLine(xpos + 2, ypos + 5, xpos + 6, ypos + 5);
/*      */       
/*      */ 
/* 7821 */       ypos = this.visibleLegend.y + this.visibleLegend.height - 8;
/* 7822 */       paintBox(g, Color.lightGray, xpos, ypos, 8, 8, this.bottomLegendPushed);
/* 7823 */       ypos += (this.bottomLegendPushed ? 1 : 0);
/* 7824 */       g.setColor(Color.black);
/* 7825 */       g.drawLine(xpos + 4, ypos + 5, xpos + 4, ypos + 5);
/* 7826 */       g.drawLine(xpos + 3, ypos + 4, xpos + 5, ypos + 4);
/* 7827 */       g.drawLine(xpos + 2, ypos + 3, xpos + 6, ypos + 3);
/*      */       
/*      */ 
/* 7830 */       int scrollerSpace = this.visibleLegend.height - 18;
/* 7831 */       int slider = Math.max(this.visibleLegend.height * scrollerSpace / this.legend.height, 8);
/* 7832 */       ypos = this.visibleLegend.y + 9 + (int)(this.verticalLegendFactor * (scrollerSpace - slider));
/* 7833 */       paintBox(g, Color.lightGray, xpos, ypos, 8, slider, false);
/*      */       
/*      */ 
/* 7836 */       this.verticalLegendScroller.x = xpos;
/* 7837 */       this.verticalLegendScroller.y = ypos;
/* 7838 */       this.verticalLegendScroller.height = slider;
/* 7839 */       this.verticalLegendScroller.width = 8;
/*      */     }
/*      */     
/*      */ 
/* 7843 */     if ((this.horizontalLegendScrollerOn) && (this.visibleLegend.width > 26))
/*      */     {
/* 7845 */       int xpos = this.visibleLegend.x - 1;
/* 7846 */       int ypos = this.visibleLegend.y + this.visibleLegend.height + 1;
/*      */       
/*      */ 
/* 7849 */       g.setColor(getBackground().darker());
/* 7850 */       g.drawLine(xpos, ypos + 4, xpos + this.visibleLegend.width - 2, ypos + 4);
/* 7851 */       g.setColor(getBackground().brighter());
/* 7852 */       g.drawLine(xpos, ypos + 5, xpos + this.visibleLegend.width - 2, ypos + 5);
/*      */       
/*      */ 
/* 7855 */       paintBox(g, Color.lightGray, xpos, ypos, 8, 8, this.leftLegendPushed);
/* 7856 */       xpos += (this.leftLegendPushed ? 1 : 0);
/* 7857 */       g.setColor(Color.black);
/* 7858 */       g.drawLine(xpos + 3, ypos + 4, xpos + 3, ypos + 4);
/* 7859 */       g.drawLine(xpos + 4, ypos + 3, xpos + 4, ypos + 5);
/* 7860 */       g.drawLine(xpos + 5, ypos + 2, xpos + 5, ypos + 6);
/*      */       
/*      */ 
/* 7863 */       xpos = this.visibleLegend.x + this.visibleLegend.width - 10;
/* 7864 */       paintBox(g, Color.lightGray, xpos, ypos, 8, 8, this.rightLegendPushed);
/* 7865 */       xpos += (this.rightLegendPushed ? 1 : 0);
/* 7866 */       g.setColor(Color.black);
/* 7867 */       g.drawLine(xpos + 5, ypos + 4, xpos + 5, ypos + 4);
/* 7868 */       g.drawLine(xpos + 4, ypos + 3, xpos + 4, ypos + 5);
/* 7869 */       g.drawLine(xpos + 3, ypos + 2, xpos + 3, ypos + 6);
/*      */       
/*      */ 
/* 7872 */       int scrollerSpace = this.visibleLegend.width - 19;
/* 7873 */       int slider = Math.max(this.visibleLegend.width * scrollerSpace / this.legend.width, 8);
/* 7874 */       xpos = this.visibleLegend.x + 8 + (int)(this.horizontalLegendFactor * (scrollerSpace - slider));
/* 7875 */       paintBox(g, Color.lightGray, xpos, ypos, slider, 8, false);
/*      */       
/*      */ 
/* 7878 */       this.horizontalLegendScroller.x = xpos;
/* 7879 */       this.horizontalLegendScroller.y = ypos;
/* 7880 */       this.horizontalLegendScroller.height = 8;
/* 7881 */       this.horizontalLegendScroller.width = slider;
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
/*      */   boolean paintFloatingLabels(Graphics g, Rectangle bounds)
/*      */   {
/* 7896 */     boolean overlay_painted = false;
/* 7897 */     int size = this.overlayCharts.size();
/* 7898 */     Rectangle overlay_bounds = new Rectangle();
/* 7899 */     bounds.x -= this.depth3dPoint.x;
/* 7900 */     bounds.width += this.depth3dPoint.x;
/* 7901 */     overlay_bounds.y = bounds.y;
/* 7902 */     bounds.height -= this.depth3dPoint.y;
/* 7903 */     for (int i = size - 1; i >= 0; i--) {
/* 7904 */       Chart overlay = (Chart)this.overlayCharts.elementAt(i);
/* 7905 */       if ((overlay != null) && (isOverlayChartOn(i)) && 
/* 7906 */         (overlay.paintFloatingLabels(g, overlay_bounds))) {
/* 7907 */         overlay_painted = true;
/* 7908 */         break;
/*      */       }
/*      */     }
/*      */     
/* 7912 */     if (overlay_painted) {
/* 7913 */       return true;
/*      */     }
/*      */     
/* 7916 */     if (((this.mouseOverSeriesIndex > -1) || (this.mouseOverSampleIndex > -1)) && (isAnyFloatingLabelsOn(this.mouseOverSeriesIndex))) {
/* 7917 */       Font font = getFont("floatingLabelFont");
/* 7918 */       FontMetrics fm = getFontMetrics(font);
/* 7919 */       g.setFont(font);
/*      */       
/*      */ 
/* 7922 */       if ((this.mouseOverSeriesIndex >= 0) && (this.mouseOverSampleIndex >= 0)) {
/* 7923 */         String label = constructLabel(this.mouseOverSeriesIndex, this.mouseOverSampleIndex, 3, true, null);
/* 7924 */         paintFloatingLabel(g, label, bounds, this.mouseOverSampleIndex, this.mouseOverSeriesIndex, font, fm);
/* 7925 */         return true;
/*      */       }
/*      */       
/* 7928 */       if ((isFloatingOnLegendOn()) && (this.mouseOverSampleIndex == -1)) {
/* 7929 */         int sampleCount = getSampleCount();
/* 7930 */         for (int i = 0; i < sampleCount; i++) {
/* 7931 */           if ((getSample(this.mouseOverSeriesIndex, i) != null) && (getSample(this.mouseOverSeriesIndex, i).hasValue())) {
/* 7932 */             String label = constructLabel(this.mouseOverSeriesIndex, i, 3, false, null);
/* 7933 */             paintFloatingLabel(g, label, bounds, i, this.mouseOverSeriesIndex, font, fm);
/*      */           }
/*      */         }
/*      */       }
/* 7937 */       else if ((isFloatingOnLegendOn()) && (this.mouseOverSeriesIndex == -1)) {
/* 7938 */         String label = constructLabel(0, this.mouseOverSampleIndex, 3, true, null);
/* 7939 */         paintFloatingLabel(g, label, bounds, this.mouseOverSampleIndex, 0, font, fm);
/*      */       }
/*      */     }
/* 7942 */     return false;
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
/*      */   abstract void paintFloatingLabel(Graphics paramGraphics, String paramString, Rectangle paramRectangle, int paramInt1, int paramInt2, Font paramFont, FontMetrics paramFontMetrics);
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   void paintFloatingLabel(Graphics g, String label, int x, int y, Dimension labelSize, Color frameColor, FontMetrics fm)
/*      */   {
/* 7971 */     Dimension size = getSize();
/* 7972 */     int label_x = Math.max(x, 3);
/* 7973 */     label_x = Math.min(label_x, size.width - labelSize.width - 3);
/* 7974 */     int label_y = Math.max(y, fm.getAscent());
/* 7975 */     label_y = Math.min(label_y, size.height - labelSize.height + fm.getAscent() - 1);
/*      */     
/*      */ 
/*      */ 
/* 7979 */     int fheight = fm.getHeight();
/* 7980 */     int descent = fm.getDescent();
/* 7981 */     g.setColor(new Color(255, 255, 231));
/* 7982 */     g.fillRect(label_x - 2, label_y - fheight + descent * 2 - 2, labelSize.width + 5, labelSize.height);
/* 7983 */     g.setColor(frameColor);
/* 7984 */     g.drawRect(label_x - 3, label_y - fheight + descent * 2 - 3, labelSize.width + 5, labelSize.height + 1);
/* 7985 */     g.setColor(Color.black);
/* 7986 */     paintLabel(g, label, label_x, label_y, labelSize, 0, 0, false);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   /**
/*      */    * @deprecated
/*      */    */
/*      */   protected String constructLabel(int series, int sample, boolean paintSeriesOn, int type)
/*      */   {
/* 8000 */     return construct_label(series, sample, type, paintSeriesOn, null);
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
/*      */   protected String constructLabel(int series, int sample, int style, boolean paintSeriesOn, String percentLabel)
/*      */   {
/* 8014 */     if (percentLabel != null) {
/* 8015 */       return construct_label(series, sample, style, paintSeriesOn, percentLabel);
/*      */     }
/* 8017 */     return constructLabel(series, sample, paintSeriesOn, style);
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
/*      */   private String construct_label(int series, int sample, int style, boolean paintSeriesOn, String percentLabel)
/*      */   {
/* 8033 */     if ((series < 0) || (series >= getSeriesCount()) || (sample < 0) || (sample >= getSampleCount())) {
/* 8034 */       return "";
/*      */     }
/*      */     
/*      */ 
/* 8038 */     boolean seriesOn = (paintSeriesOn) && (this.seriesLabelsOn) && (this.seriesLabelStyle == style);
/* 8039 */     int sample_style = this.sampleLabelStyle;
/* 8040 */     if (this.sampleLabelStyle == 4) {
/* 8041 */       sample_style = 3;
/*      */     }
/* 8043 */     boolean sample_on = ((this.chartType.equals("bar")) && (this.barLabelsOn) && (style == 3)) || ((this.sampleLabelsOn) && (sample_style == style));
/* 8044 */     boolean valueOn = (isValueLabelsOn(series)) && (this.valueLabelStyle == style);
/* 8045 */     String separator = style == 3 ? " : " : ":";
/*      */     
/*      */ 
/*      */ 
/* 8049 */     String label = "";
/*      */     
/*      */ 
/* 8052 */     if (seriesOn) {
/* 8053 */       String seriesLabel = getSeriesLabel(series);
/* 8054 */       if (seriesLabel != null) {
/* 8055 */         label = label + seriesLabel;
/* 8056 */         if ((!seriesLabel.endsWith("\n")) && (sample_on) && (getSampleLabel(sample) != null)) {
/* 8057 */           label = label + separator;
/*      */         }
/*      */       }
/*      */     }
/*      */     
/*      */ 
/* 8063 */     if ((sample_on) && 
/* 8064 */       (getSampleLabel(sample) != null)) {
/* 8065 */       label = label + getSampleLabel(sample);
/*      */     }
/*      */     
/*      */ 
/*      */ 
/* 8070 */     if (valueOn) {
/* 8071 */       String prefix = getLabel("valueLabelPrefix_" + series);
/* 8072 */       if (prefix == null) {
/* 8073 */         prefix = getLabel("valueLabelPrefix");
/*      */       }
/* 8075 */       String postfix = getLabel("valueLabelPostfix_" + series);
/* 8076 */       if (postfix == null) {
/* 8077 */         postfix = getLabel("valueLabelPostfix");
/*      */       }
/* 8079 */       if (label.length() > 0) {
/* 8080 */         String number = formatNumber(getSampleValue(series, sample), getSampleDecimalCount(series));
/* 8081 */         number = prefix != null ? prefix + number : number;
/* 8082 */         number = postfix != null ? number + postfix : number;
/*      */         
/* 8084 */         if ((label.endsWith("\n")) || (number.startsWith("\n"))) {
/* 8085 */           label = label + number;
/*      */         } else {
/* 8087 */           label = label + separator + number;
/*      */         }
/*      */       }
/*      */       else {
/* 8091 */         label = formatNumber(getSampleValue(series, sample), getSampleDecimalCount(series));
/* 8092 */         label = prefix != null ? prefix + label : label;
/* 8093 */         label = postfix != null ? label + postfix : label;
/*      */       }
/*      */     }
/*      */     
/*      */ 
/* 8098 */     if (percentLabel != null) {
/* 8099 */       if ((!label.equals("")) && (!label.endsWith("\n"))) {
/* 8100 */         label = label + " : ";
/*      */       }
/* 8102 */       label = label + percentLabel;
/*      */     }
/* 8104 */     return label;
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
/*      */   boolean isAnyFloatingLabelsOn(int serie)
/*      */   {
/* 8118 */     return false;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected void processEvent(AWTEvent event)
/*      */   {
/* 8128 */     if (event == null) {
/* 8129 */       return;
/*      */     }
/*      */     
/*      */ 
/* 8133 */     if (this.depth3d > -1) {
/* 8134 */       this.depth3dPoint.x = this.depth3d;
/* 8135 */       this.depth3dPoint.y = (-this.depth3d);
/*      */     }
/*      */     
/*      */ 
/* 8139 */     int xpos = 0;
/* 8140 */     int ypos = 0;
/* 8141 */     if ((event instanceof MouseEvent)) {
/* 8142 */       this.mousePosition.x = (xpos = ((MouseEvent)event).getX());
/* 8143 */       this.mousePosition.y = (ypos = ((MouseEvent)event).getY());
/*      */     }
/*      */     
/*      */ 
/* 8147 */     Dimension size = getSize();
/* 8148 */     int top = 0;
/* 8149 */     int left = 0;
/* 8150 */     int right = 0;
/* 8151 */     int bottom = 0;
/* 8152 */     if (this.currentBounds != null) {
/* 8153 */       top = this.currentBounds.y;
/* 8154 */       left = this.currentBounds.x - (this.display3dOn ? this.depth3dPoint.x : 0);
/* 8155 */       right = this.currentBounds.x + this.currentBounds.width;
/* 8156 */       bottom = this.currentBounds.y + this.currentBounds.height - (this.display3dOn ? this.depth3dPoint.y : 0);
/*      */     }
/*      */     
/* 8159 */     if ((event instanceof MouseEvent)) {
/* 8160 */       this.old_cursor = this.new_cursor;
/* 8161 */       this.new_cursor = DEFAULT_CURSOR;
/*      */       
/* 8163 */       boolean adjuster_on = false;
/* 8164 */       for (int i = 0; i < this.rangeAdjusterOn.length; i++) {
/* 8165 */         if (this.rangeAdjusterOn[i] != 0) {
/* 8166 */           adjuster_on = true;
/*      */         }
/*      */       }
/* 8169 */       if ((adjuster_on) || (this.sampleScrollerOn)) {
/* 8170 */         handleAdjusters(event);
/*      */       }
/*      */       
/* 8173 */       if ((this.verticalLegendScrollerOn) || (this.horizontalLegendScrollerOn)) {
/* 8174 */         handleLegendScrollers(event);
/*      */       }
/*      */       
/* 8177 */       String id = getLabelAtPoint(xpos, ypos);
/* 8178 */       if ((id != null) && (this.labelURLs.get(id) != null) && (this.mouseBox == null)) {
/* 8179 */         this.new_cursor = HAND_CURSOR;
/*      */ 
/*      */       }
/* 8182 */       else if ((this.zoomOn) && (this.dragged_label == null) && (this.gridEdgeToAdjust == -1)) {
/* 8183 */         handleZoom(event);
/*      */       }
/*      */     }
/*      */     
/*      */ 
/* 8188 */     ChartSample selection = checkSelection(this.mousePosition);
/* 8189 */     switch (event.getID()) {
/*      */     case 503: 
/*      */     case 506: 
/* 8192 */       if (selection != this.lastSelection)
/*      */       {
/*      */ 
/*      */ 
/* 8196 */         if ((selection == null) || (this.lastSelection == null) || (selection.getIndex() != -1)) {
/* 8197 */           repaint();
/* 8198 */         } else if (selection.getSeries() != this.lastSelection.getSeries()) {
/* 8199 */           repaint();
/*      */         }
/* 8201 */         this.lastSelection = selection;
/*      */       }
/*      */       
/*      */       break;
/*      */     }
/*      */     
/* 8207 */     long now = System.currentTimeMillis();
/* 8208 */     switch (event.getID())
/*      */     {
/*      */ 
/*      */ 
/*      */     case 501: 
/* 8213 */       this.mousePressX = xpos;
/* 8214 */       this.mousePressY = ypos;
/*      */       
/*      */ 
/* 8217 */       if (this.c2.copyright) {
/* 8218 */         this.c2.copyright = false;
/*      */       }
/* 8220 */       else if ((xpos > 0) && (xpos < 11) && (ypos > size.height - 11) && (ypos < size.height)) {
/* 8221 */         this.c2.copyright = true;
/*      */ 
/*      */ 
/*      */       }
/* 8225 */       else if ((this.gridEdgeToAdjust >= 0) && 
/* 8226 */         (now - this.lastClickTime < 350L)) {
/* 8227 */         switch (this.gridEdgeToAdjust) {
/*      */         case 0: 
/* 8229 */           this.graphInsets.left = -1;
/* 8230 */           break;
/*      */         case 1: 
/* 8232 */           this.graphInsets.right = -1;
/* 8233 */           break;
/*      */         case 2: 
/* 8235 */           this.graphInsets.top = -1;
/* 8236 */           break;
/*      */         case 3: 
/* 8238 */           this.graphInsets.bottom = -1;
/*      */         }
/*      */         
/* 8241 */         this.needRender = true;
/* 8242 */         this.needGraphBounds = true;
/* 8243 */         this.needChartCalculation = true;
/*      */       }
/*      */       
/*      */ 
/*      */ 
/* 8248 */       this.dragged_label = getLabelAtPoint(xpos, ypos);
/* 8249 */       if ((this.dragged_label != null) && (this.labelSamples.get(this.dragged_label) != null) && (this.labelSeries.get(this.dragged_label) != null)) {
/* 8250 */         this.dragged_start_x = ((Double)this.labelXs.get(this.dragged_label)).doubleValue();
/* 8251 */         this.dragged_start_y = ((Double)this.labelYs.get(this.dragged_label)).doubleValue();
/*      */         
/*      */ 
/* 8254 */         for (int i = 0; i < this.labelIDs.length; i++) {
/* 8255 */           if (this.dragged_label.equals(this.labelIDs[i])) {
/* 8256 */             System.arraycopy(this.labelIDs, i + 1, this.labelIDs, i, this.labelIDs.length - i - 1);
/* 8257 */             break;
/*      */           }
/*      */         }
/* 8260 */         this.labelIDs[(this.labelIDs.length - 1)] = this.dragged_label;
/* 8261 */         this.shouldSelect = false;
/*      */       }
/*      */       
/* 8264 */       this.lastClickTime = now;
/* 8265 */       repaint();
/* 8266 */       break;
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */     case 502: 
/* 8272 */       this.mousePressX = -1;
/* 8273 */       this.mousePressY = -1;
/*      */       
/* 8275 */       if (this.currentBounds != null)
/*      */       {
/* 8277 */         ChartSample sample = checkSelection(new Point(xpos, ypos));
/*      */         
/*      */ 
/* 8280 */         if (this.shouldSelect) {
/* 8281 */           if (sample != null) {
/* 8282 */             int series = sample.getSeries();
/* 8283 */             if (series >= -1) {
/*      */               try {
/* 8285 */                 setSelection(Math.max(series, 0), sample.getIndex(), true, true, true);
/* 8286 */                 repaint();
/*      */ 
/*      */               }
/*      */               catch (IllegalArgumentException localIllegalArgumentException) {}
/*      */             }
/*      */             
/*      */ 
/*      */           }
/* 8294 */           else if (this.selectedSample != null) {
/* 8295 */             setSelection(-1, -1, false, true, true);
/* 8296 */             repaint();
/*      */           }
/*      */         }
/*      */       }
/*      */       
/* 8301 */       this.shouldSelect = true;
/* 8302 */       this.dragged_label = null;
/* 8303 */       break;
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     case 503: 
/* 8311 */       if ((this.gridAdjustment[2] != 0) && (xpos >= left) && (xpos <= right) && (ypos >= top - 1) && (ypos <= top + 3)) {
/* 8312 */         this.new_cursor = GRID_CURSOR_VERT;
/* 8313 */         this.gridEdgeToAdjust = 2;
/*      */ 
/*      */       }
/* 8316 */       else if ((this.gridAdjustment[0] != 0) && (xpos >= left - 1) && (xpos <= left + 3) && (ypos >= top) && (ypos <= bottom)) {
/* 8317 */         this.new_cursor = GRID_CURSOR_HORZ;
/* 8318 */         this.gridEdgeToAdjust = 0;
/*      */ 
/*      */       }
/* 8321 */       else if ((this.gridAdjustment[3] != 0) && (xpos >= left) && (xpos <= right) && (ypos >= bottom - 3) && (ypos <= bottom + 1)) {
/* 8322 */         this.new_cursor = GRID_CURSOR_VERT;
/* 8323 */         this.gridEdgeToAdjust = 3;
/*      */ 
/*      */       }
/* 8326 */       else if ((this.gridAdjustment[1] != 0) && (xpos >= right - 3) && (xpos <= right + 1) && (ypos >= top) && (ypos <= bottom)) {
/* 8327 */         this.new_cursor = GRID_CURSOR_HORZ;
/* 8328 */         this.gridEdgeToAdjust = 1;
/*      */       }
/*      */       else
/*      */       {
/* 8332 */         this.gridEdgeToAdjust = -1;
/*      */       }
/* 8334 */       break;
/*      */     
/*      */ 
/*      */ 
/*      */     case 506: 
/* 8339 */       int xoff = 0;
/* 8340 */       int yoff = 0;
/* 8341 */       if (this.display3dOn) {
/* 8342 */         xoff = this.depth3dPoint.x;
/* 8343 */         yoff = this.depth3dPoint.y;
/*      */       }
/* 8345 */       if (this.gridEdgeToAdjust >= 0) {
/* 8346 */         switch (this.gridEdgeToAdjust) {
/*      */         case 0: 
/* 8348 */           this.graphInsets.left = (Math.max(0, xpos) + xoff);
/* 8349 */           if (this.currentBounds != null) {
/* 8350 */             this.graphInsets.left = Math.min(this.graphInsets.left, this.currentBounds.x + this.currentBounds.width - 5);
/*      */           }
/* 8352 */           this.new_cursor = GRID_CURSOR_HORZ;
/* 8353 */           break;
/*      */         case 1: 
/* 8355 */           this.graphInsets.right = Math.max(size.width - xpos, 0);
/* 8356 */           if (this.currentBounds != null) {
/* 8357 */             this.graphInsets.right = Math.min(this.graphInsets.right, size.width - this.currentBounds.x - 5);
/*      */           }
/* 8359 */           this.new_cursor = GRID_CURSOR_HORZ;
/* 8360 */           break;
/*      */         case 2: 
/* 8362 */           this.graphInsets.top = Math.max(ypos, 0);
/* 8363 */           if (this.currentBounds != null) {
/* 8364 */             this.graphInsets.top = Math.min(this.graphInsets.top, this.currentBounds.y + this.currentBounds.height - 5);
/*      */           }
/* 8366 */           this.new_cursor = GRID_CURSOR_VERT;
/* 8367 */           break;
/*      */         case 3: 
/* 8369 */           this.graphInsets.bottom = (Math.max(size.height - ypos, 0) - yoff);
/* 8370 */           if (this.currentBounds != null) {
/* 8371 */             this.graphInsets.bottom = Math.min(this.graphInsets.bottom, size.height - this.currentBounds.y - 5);
/*      */           }
/* 8373 */           this.new_cursor = GRID_CURSOR_VERT;
/*      */         }
/*      */         
/*      */         
/* 8377 */         this.needRender = true;
/* 8378 */         this.needGraphBounds = true;
/* 8379 */         this.needChartCalculation = true;
/* 8380 */         for (int i = 0; i < this.overlayCharts.size(); i++) {
/* 8381 */           Chart overlay = (Chart)this.overlayCharts.elementAt(i);
/* 8382 */           if ((overlay != null) && (isOverlayChartOn(i))) {
/* 8383 */             overlay.needChartCalculation = true;
/*      */           }
/*      */         }
/* 8386 */         repaint();
/*      */       }
/*      */       
/*      */ 
/* 8390 */       if ((this.dragged_label != null) && (this.labelSamples.get(this.dragged_label) != null) && (this.labelSeries.get(this.dragged_label) != null)) {
/* 8391 */         String text = (String)this.labelTexts.get(this.dragged_label);
/* 8392 */         Integer serie = (Integer)this.labelSeries.get(this.dragged_label);
/* 8393 */         Font font = getFont("font");
/* 8394 */         FontMetrics fm = getFontMetrics(font);
/* 8395 */         Dimension labelSize = getLabelSize(text, fm);
/* 8396 */         int delta_x = xpos - this.mousePressX;
/* 8397 */         int delta_y = ypos - this.mousePressY;
/*      */         double new_x;
/*      */         double new_y;
/* 8400 */         if ((this.dragged_start_x > 0.0D) && (this.dragged_start_x < 1.0D) && (this.dragged_start_y > 0.0D) && (this.dragged_start_y < 1.0D)) {
/* 8401 */           Rectangle bounds = new Rectangle(this.chartDataBounds);
/* 8402 */           if (this.chartType.equals("pie")) {
/* 8403 */             bounds = new Rectangle(this.graphBounds);
/*      */           } else {
/* 8405 */             int range = 0;
/* 8406 */             if (serie != null) {
/* 8407 */               range = getSeriesRange(serie.intValue());
/*      */             }
/* 8409 */             bounds.height = ((int)(bounds.height / (this.currentUpperRange[range] - this.currentLowerRange[range]) * (this.upperRange[range] - this.lowerRange[range])));
/* 8410 */             bounds.y -= (int)((this.currentUpperRange[range] - this.upperRange[range]) / (this.lowerRange[range] - this.upperRange[range]) * bounds.height);
/* 8411 */             if (is3DModeOn()) {
/* 8412 */               bounds.x -= this.depth3dPoint.x;
/* 8413 */               bounds.width += this.depth3dPoint.x;
/* 8414 */               bounds.height -= this.depth3dPoint.y;
/*      */             }
/*      */           }
/* 8417 */           double new_x = this.dragged_start_x + delta_x / (bounds.width - (labelSize.width + 4));
/* 8418 */           double new_y = this.dragged_start_y + delta_y / (bounds.height - (labelSize.height + 2));
/* 8419 */           new_x = Math.max(new_x, 1.0E-6D);
/* 8420 */           new_y = Math.max(new_y, 1.0E-6D);
/* 8421 */           new_x = Math.min(new_x, 0.999999D);
/* 8422 */           new_y = Math.min(new_y, 0.999999D);
/*      */         } else {
/* 8424 */           new_x = this.dragged_start_x + delta_x;
/* 8425 */           new_y = this.dragged_start_y + delta_y;
/* 8426 */           new_x = Math.max(new_x, -labelSize.width + 3);
/* 8427 */           new_x = Math.min(new_x, size.width - 3);
/* 8428 */           new_y = Math.max(new_y, 0.0D);
/* 8429 */           new_y = Math.min(new_y, size.height + labelSize.height - 5);
/*      */         }
/* 8431 */         this.labelXs.put(this.dragged_label, new Double(new_x));
/* 8432 */         this.labelYs.put(this.dragged_label, new Double(new_y));
/* 8433 */         this.needRender = true;
/* 8434 */         repaint();
/*      */       }
/* 8436 */       break;
/*      */     
/*      */ 
/*      */     case 505: 
/* 8440 */       this.mouseOverSampleIndex = -1;
/* 8441 */       this.mouseOverSeriesIndex = -1;
/* 8442 */       repaint();
/* 8443 */       break;
/*      */     
/*      */ 
/*      */ 
/*      */     case 101: 
/* 8448 */       for (int i = 0; i < this.overlayCharts.size(); i++) {
/* 8449 */         Chart overlay = (Chart)this.overlayCharts.elementAt(i);
/* 8450 */         if ((overlay != null) && (isOverlayChartOn(i))) {
/* 8451 */           overlay.needChartCalculation = true;
/*      */         }
/*      */       }
/* 8454 */       this.needRender = true;
/* 8455 */       this.needGraphBounds = true;
/* 8456 */       this.needChartCalculation = true;
/*      */       
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 8463 */       if (event.getSource() == this)
/*      */       {
/* 8465 */         Graphics g = getGraphics();
/* 8466 */         if ((!this.componentResized) && (g != null) && (size.width > 0) && (size.height > 0)) {
/* 8467 */           paint(g);
/* 8468 */           this.componentResized = true;
/* 8469 */         } else if ((g != null) && (size.width > 0) && (size.height > 0)) {
/* 8470 */           repaint();
/*      */         }
/*      */       }
/*      */       break;
/*      */     }
/*      */     
/* 8476 */     if (this.new_cursor != this.old_cursor) {
/* 8477 */       setCursor(this.new_cursor);
/*      */     }
/* 8479 */     super.processEvent(event);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   private void handleZoom(AWTEvent event)
/*      */   {
/* 8488 */     if ((this.adjustingUpper) || (this.adjustingLower) || (this.slidingAdjuster) || (this.adjustingLeft) || 
/* 8489 */       (this.adjustingRight) || (this.slidingScroller) || (this.leftPushed) || (this.rightPushed)) {
/* 8490 */       return;
/*      */     }
/*      */     
/*      */ 
/* 8494 */     int xpos = ((MouseEvent)event).getX();
/* 8495 */     int ypos = ((MouseEvent)event).getY();
/* 8496 */     long now = System.currentTimeMillis();
/* 8497 */     switch (event.getID())
/*      */     {
/*      */     case 501: 
/* 8500 */       ChartSample sample = checkSelection(new Point(xpos, ypos));
/*      */       
/*      */ 
/* 8503 */       boolean inside_zoom_out = false;
/* 8504 */       if (this.gridAlignment == 1) {
/* 8505 */         int x = xpos + this.depth3dPoint.x;
/* 8506 */         int y = ypos + this.depth3dPoint.y;
/* 8507 */         inside_zoom_out = (x > this.graphBounds.x + 8) && (x < this.graphBounds.x + 17) && (y > this.graphBounds.y + 8) && (y < this.graphBounds.y + 17);
/*      */       } else {
/* 8509 */         inside_zoom_out = (xpos < this.graphBounds.x + this.graphBounds.width - 7) && (xpos > this.graphBounds.x + this.graphBounds.width - 16) && (ypos > this.graphBounds.y + 8) && (ypos < this.graphBounds.y + 17);
/*      */       }
/* 8511 */       if (inside_zoom_out)
/*      */       {
/* 8513 */         for (int adjuster = 0; adjuster < this.rangeOn.length; adjuster++)
/*      */         {
/* 8515 */           double upperDiff = Math.abs(this.upperRange[adjuster] - this.currentUpperRange[adjuster]);
/* 8516 */           double lowerDiff = Math.abs(this.lowerRange[adjuster] - this.currentLowerRange[adjuster]);
/* 8517 */           if ((upperDiff > 0.0D) || (lowerDiff > 0.0D))
/*      */           {
/* 8519 */             double factor = 0.1D * (this.upperRange[adjuster] - this.lowerRange[adjuster]);
/* 8520 */             double delta = factor * upperDiff / (upperDiff + lowerDiff);
/* 8521 */             setCurrentRange(adjuster, getCurrentRange(adjuster) + delta);
/* 8522 */             setCurrentLowerRange(adjuster, getCurrentLowerRange(adjuster) - (factor - delta));
/*      */             
/* 8524 */             for (int chart = 0; chart < this.overlayCharts.size(); chart++) {
/* 8525 */               Chart overlay = (Chart)this.overlayCharts.elementAt(chart);
/* 8526 */               overlay.setCurrentRange(adjuster, overlay.getCurrentRange(adjuster) + delta);
/* 8527 */               overlay.setCurrentLowerRange(adjuster, overlay.getCurrentLowerRange(adjuster) - (factor - delta));
/*      */             }
/*      */           }
/*      */         }
/*      */         
/* 8532 */         if ((this.leftScrollerFactor > 0.0D) || (this.rightScrollerFactor > 0.0D)) {
/* 8533 */           double delta = 0.1D * this.leftScrollerFactor / (this.rightScrollerFactor + this.leftScrollerFactor);
/* 8534 */           this.leftScrollerFactor = Math.max(0.0D, this.leftScrollerFactor - delta);
/* 8535 */           this.rightScrollerFactor = Math.max(0.0D, this.rightScrollerFactor - (0.1D - delta));
/*      */         }
/* 8537 */         this.zoomOutPushed = true;
/* 8538 */         this.shouldSelect = false;
/*      */ 
/*      */ 
/*      */       }
/* 8542 */       else if ((now - this.lastClickTime < 350L) && (sample == null))
/*      */       {
/* 8544 */         this.leftScrollerFactor = 0.0D;
/* 8545 */         this.rightScrollerFactor = 0.0D;
/*      */         
/* 8547 */         for (int adjuster = 0; adjuster < this.rangeOn.length; adjuster++) {
/* 8548 */           setCurrentRange(adjuster, getRange(adjuster));
/* 8549 */           setCurrentLowerRange(adjuster, getLowerRange(adjuster));
/* 8550 */           for (int chart = 0; chart < this.overlayCharts.size(); chart++) {
/* 8551 */             Chart overlay = (Chart)this.overlayCharts.elementAt(chart);
/* 8552 */             overlay.setCurrentRange(adjuster, overlay.getRange(adjuster));
/* 8553 */             overlay.setCurrentLowerRange(adjuster, overlay.getLowerRange(adjuster));
/*      */           }
/*      */         }
/* 8556 */         this.shouldSelect = false;
/* 8557 */         this.visibleSamples[0] = 0;
/* 8558 */         this.visibleSamples[1] = (getSampleCount() - 1);
/*      */       }
/*      */       
/* 8561 */       this.needRender = true;
/* 8562 */       this.needChartCalculation = true;
/* 8563 */       repaint();
/* 8564 */       break;
/*      */     
/*      */ 
/*      */     case 506: 
/* 8568 */       if ((!this.zoomOutPushed) && (this.mousePressX >= 0) && (this.mousePressY >= 0)) {
/* 8569 */         int box_start_x = Math.max(this.graphBounds.x - this.depth3dPoint.x + 1, this.mousePressX);
/* 8570 */         int box_start_y = Math.max(this.graphBounds.y - this.depth3dPoint.y + 1, this.mousePressY);
/* 8571 */         box_start_x = Math.min(this.graphBounds.x + this.graphBounds.width - this.depth3dPoint.x - 1, box_start_x);
/* 8572 */         box_start_y = Math.min(this.graphBounds.y + this.graphBounds.height - this.depth3dPoint.y - 1, box_start_y);
/*      */         
/* 8574 */         int box_end_x = Math.max(this.graphBounds.x - this.depth3dPoint.x + 1, xpos);
/* 8575 */         int box_end_y = Math.max(this.graphBounds.y - this.depth3dPoint.y + 1, ypos);
/* 8576 */         box_end_x = Math.min(this.graphBounds.x + this.graphBounds.width - this.depth3dPoint.x - 1, box_end_x);
/* 8577 */         box_end_y = Math.min(this.graphBounds.y + this.graphBounds.height - this.depth3dPoint.y - 1, box_end_y);
/*      */         
/* 8579 */         this.mouseBox = new Rectangle();
/* 8580 */         this.mouseBox.x = Math.min(box_start_x, box_end_x);
/* 8581 */         this.mouseBox.y = Math.min(box_start_y, box_end_y);
/* 8582 */         this.mouseBox.width = Math.abs(box_start_x - box_end_x);
/* 8583 */         this.mouseBox.height = Math.abs(box_start_y - box_end_y);
/*      */         
/*      */ 
/* 8586 */         if ((this.mouseBox.width < 3) || (this.mouseBox.height < 3)) {
/* 8587 */           this.mouseBox = null;
/*      */         }
/* 8589 */         repaint();
/*      */       }
/* 8591 */       break;
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */     case 502: 
/* 8597 */       if ((now - this.lastClickTime > 100L) && (this.mouseBox != null))
/*      */       {
/* 8599 */         Rectangle rect = new Rectangle(this.mouseBox.x - 1, this.mouseBox.y, this.mouseBox.width + 2, this.mouseBox.height);
/* 8600 */         if (this.gridAlignment == 0) {
/* 8601 */           rect = new Rectangle(this.mouseBox.x, this.mouseBox.y - 1, this.mouseBox.width, this.mouseBox.height + 2);
/*      */         }
/*      */         
/*      */ 
/* 8605 */         for (int range = 0; range < this.rangeOn.length; range++) {
/* 8606 */           double upperFactor = (rect.y - this.chartDataBounds.y) / this.chartDataBounds.height;
/* 8607 */           if (this.gridAlignment == 0) {
/* 8608 */             upperFactor = (this.chartDataBounds.x + this.chartDataBounds.width - rect.x - rect.width) / this.chartDataBounds.width;
/*      */           }
/* 8610 */           double lowerFactor = (this.chartDataBounds.y + this.chartDataBounds.height - rect.y - rect.height) / this.chartDataBounds.height;
/* 8611 */           if (this.gridAlignment == 0) {
/* 8612 */             lowerFactor = (rect.x - this.chartDataBounds.x) / this.chartDataBounds.width;
/*      */           }
/*      */           
/*      */ 
/* 8616 */           int upperRangeDelta = (int)(upperFactor * (this.currentUpperRange[range] - this.currentLowerRange[range]));
/* 8617 */           int lowerRangeDelta = (int)(lowerFactor * (this.currentUpperRange[range] - this.currentLowerRange[range]));
/*      */           
/* 8619 */           setCurrentRange(range, getCurrentRange(range) - upperRangeDelta);
/* 8620 */           setCurrentLowerRange(range, getCurrentLowerRange(range) + lowerRangeDelta);
/* 8621 */           for (int chart = 0; chart < this.overlayCharts.size(); chart++) {
/* 8622 */             Chart overlay = (Chart)this.overlayCharts.elementAt(chart);
/* 8623 */             overlay.setCurrentRange(range, overlay.getCurrentRange(range) - upperRangeDelta);
/* 8624 */             overlay.setCurrentLowerRange(range, overlay.getCurrentLowerRange(range) + lowerRangeDelta);
/*      */           }
/*      */         }
/*      */         
/*      */ 
/* 8629 */         if (this.gridAlignment == 1) {
/* 8630 */           this.leftScrollerFactor = ((rect.x - this.chartDataBounds.x) / this.chartDataBounds.width);
/* 8631 */           this.rightScrollerFactor = ((this.chartDataBounds.x + this.chartDataBounds.width - rect.x - rect.width) / this.chartDataBounds.width);
/*      */         } else {
/* 8633 */           this.leftScrollerFactor = ((rect.y - this.chartDataBounds.y) / this.chartDataBounds.height);
/* 8634 */           this.rightScrollerFactor = ((this.chartDataBounds.y + this.chartDataBounds.height - rect.y - rect.height) / this.chartDataBounds.height);
/*      */         }
/* 8636 */         this.shouldSelect = false;
/*      */       }
/* 8638 */       this.mouseBox = null;
/* 8639 */       this.zoomOutPushed = false;
/*      */       
/* 8641 */       this.needRender = true;
/* 8642 */       this.needChartCalculation = true;
/* 8643 */       repaint();
/*      */     }
/*      */     
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   private void handleLegendScrollers(AWTEvent event)
/*      */   {
/* 8653 */     boolean inside_vertical = false;
/* 8654 */     boolean insideTopButton = false;
/* 8655 */     boolean insideBottomButton = false;
/* 8656 */     boolean inside_horizontal = false;
/* 8657 */     boolean insideLeftButton = false;
/* 8658 */     boolean insideRightButton = false;
/*      */     
/*      */ 
/* 8661 */     int xpos = ((MouseEvent)event).getX();
/* 8662 */     int ypos = ((MouseEvent)event).getY();
/*      */     
/*      */ 
/* 8665 */     Rectangle b = this.verticalLegendScroller;
/* 8666 */     inside_vertical = (xpos >= b.x) && (xpos <= b.x + b.width) && (ypos >= b.y) && (ypos <= b.y + b.height);
/* 8667 */     insideTopButton = (xpos >= b.x) && (xpos <= b.x + b.width);
/* 8668 */     insideTopButton &= ((ypos >= this.visibleLegend.y) && (ypos <= this.visibleLegend.y + 8));
/* 8669 */     insideBottomButton = (xpos >= b.x) && (xpos <= b.x + b.width);
/* 8670 */     insideBottomButton &= ((ypos <= this.visibleLegend.y + this.visibleLegend.height) && (ypos >= this.visibleLegend.y + this.visibleLegend.height - 8));
/*      */     
/*      */ 
/* 8673 */     b = this.horizontalLegendScroller;
/* 8674 */     inside_horizontal = (ypos >= b.y) && (ypos <= b.y + b.height) && (xpos >= b.x) && (xpos <= b.x + b.width);
/* 8675 */     insideLeftButton = (ypos >= b.y) && (ypos <= b.y + b.height);
/* 8676 */     insideLeftButton &= ((xpos >= this.visibleLegend.x) && (xpos <= this.visibleLegend.x + 8));
/* 8677 */     insideRightButton = (ypos >= b.y) && (ypos <= b.y + b.height);
/* 8678 */     insideRightButton &= ((xpos <= this.visibleLegend.x + this.visibleLegend.width) && (xpos >= this.visibleLegend.x + this.visibleLegend.width - 8));
/*      */     
/* 8680 */     if ((inside_vertical) || (inside_horizontal)) {
/* 8681 */       this.new_cursor = HAND_CURSOR;
/*      */     }
/* 8683 */     switch (event.getID())
/*      */     {
/*      */     case 503: 
/*      */       break;
/*      */     case 501: 
/* 8688 */       this.topLegendPushed = insideTopButton;
/* 8689 */       this.bottomLegendPushed = insideBottomButton;
/* 8690 */       this.leftLegendPushed = insideLeftButton;
/* 8691 */       this.rightLegendPushed = insideRightButton;
/* 8692 */       if ((inside_vertical) || (inside_horizontal) || (insideTopButton) || (insideBottomButton) || 
/* 8693 */         (insideLeftButton) || (insideRightButton)) {
/* 8694 */         this.shouldSelect = false;
/*      */       }
/*      */       
/* 8697 */       if (inside_vertical) {
/* 8698 */         this.slidingVerticalLegend = true;
/* 8699 */         this.lastScrollerClick = ypos;
/*      */       }
/* 8701 */       else if (inside_horizontal) {
/* 8702 */         this.slidingHorizontalLegend = true;
/* 8703 */         this.lastScrollerClick = xpos;
/*      */       }
/* 8705 */       else if (insideTopButton) {
/* 8706 */         long delta = Math.round((1.0D - this.verticalLegendFactor) * (this.legend.height - this.visibleLegend.height));
/* 8707 */         int offset = 0;
/* 8708 */         for (int i = this.legendRowHeight.length - 1; i >= 0; i--) {
/* 8709 */           offset += this.legendRowHeight[i];
/* 8710 */           if (offset > delta) {
/* 8711 */             delta = offset;
/* 8712 */             break;
/*      */           }
/*      */         }
/* 8715 */         this.verticalLegendFactor = (1.0D - delta / (this.legend.height - this.visibleLegend.height));
/* 8716 */         this.verticalLegendFactor = Math.max(this.verticalLegendFactor, 0.0D);
/*      */       }
/* 8718 */       else if (insideBottomButton) {
/* 8719 */         long delta = Math.round(this.verticalLegendFactor * (this.legend.height - this.visibleLegend.height));
/* 8720 */         int offset = 0;
/* 8721 */         for (int i = 0; i < this.legendRowHeight.length; i++) {
/* 8722 */           offset += this.legendRowHeight[i];
/* 8723 */           if (offset > delta) {
/* 8724 */             delta = offset;
/* 8725 */             break;
/*      */           }
/*      */         }
/* 8728 */         this.verticalLegendFactor = (delta / (this.legend.height - this.visibleLegend.height));
/* 8729 */         this.verticalLegendFactor = Math.min(this.verticalLegendFactor, 1.0D);
/*      */       }
/* 8731 */       else if (insideLeftButton) {
/* 8732 */         long delta = Math.round((1.0D - this.horizontalLegendFactor) * (this.legend.width - this.visibleLegend.width));
/* 8733 */         int offset = 0;
/* 8734 */         for (int i = this.legendColumnWidth.length - 1; i >= 0; i--) {
/* 8735 */           offset += this.legendColumnWidth[i];
/* 8736 */           if (offset > delta) {
/* 8737 */             delta = offset;
/* 8738 */             break;
/*      */           }
/*      */         }
/* 8741 */         this.horizontalLegendFactor = (1.0D - delta / (this.legend.width - this.visibleLegend.width));
/* 8742 */         this.horizontalLegendFactor = Math.max(this.horizontalLegendFactor, 0.0D);
/*      */       }
/* 8744 */       else if (insideRightButton) {
/* 8745 */         long delta = Math.round(this.horizontalLegendFactor * (this.legend.width - this.visibleLegend.width));
/* 8746 */         int offset = 0;
/* 8747 */         for (int i = 0; i < this.legendColumnWidth.length; i++) {
/* 8748 */           offset += this.legendColumnWidth[i];
/* 8749 */           if (offset > delta) {
/* 8750 */             delta = offset;
/* 8751 */             break;
/*      */           }
/*      */         }
/* 8754 */         this.horizontalLegendFactor = (delta / (this.legend.width - this.visibleLegend.width));
/* 8755 */         this.horizontalLegendFactor = Math.min(this.horizontalLegendFactor, 1.0D);
/*      */       }
/* 8757 */       this.needRender = true;
/* 8758 */       repaint();
/* 8759 */       break;
/*      */     
/*      */ 
/*      */     case 506: 
/* 8763 */       if (this.slidingVerticalLegend) {
/* 8764 */         this.new_cursor = HAND_CURSOR;
/* 8765 */         int scrollerSpace = this.visibleLegend.height - 18;
/* 8766 */         int slider = Math.max(this.visibleLegend.height * scrollerSpace / this.legend.height, 8);
/* 8767 */         double factor = (ypos - this.lastScrollerClick) / (scrollerSpace - slider);
/* 8768 */         this.verticalLegendFactor = Math.min(Math.max(this.verticalLegendFactor + factor, 0.0D), 1.0D);
/* 8769 */         this.lastScrollerClick = ypos;
/*      */ 
/*      */       }
/* 8772 */       else if (this.slidingHorizontalLegend) {
/* 8773 */         this.new_cursor = HAND_CURSOR;
/* 8774 */         int scrollerSpace = this.visibleLegend.width - 18;
/* 8775 */         int slider = Math.max(this.visibleLegend.width * scrollerSpace / this.legend.width, 8);
/* 8776 */         double factor = (xpos - this.lastScrollerClick) / (scrollerSpace - slider);
/* 8777 */         this.horizontalLegendFactor = Math.min(Math.max(this.horizontalLegendFactor + factor, 0.0D), 1.0D);
/* 8778 */         this.lastScrollerClick = xpos;
/*      */       }
/*      */       
/* 8781 */       this.needRender = true;
/* 8782 */       repaint();
/* 8783 */       break;
/*      */     
/*      */     case 502: 
/* 8786 */       this.topLegendPushed = false;
/* 8787 */       this.bottomLegendPushed = false;
/* 8788 */       this.leftLegendPushed = false;
/* 8789 */       this.rightLegendPushed = false;
/* 8790 */       this.slidingVerticalLegend = false;
/* 8791 */       this.slidingHorizontalLegend = false;
/* 8792 */       this.needRender = true;
/* 8793 */       repaint();
/*      */     }
/*      */     
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   private void handleAdjusters(AWTEvent event)
/*      */   {
/* 8803 */     boolean inside_range = false;
/* 8804 */     boolean inside_upper = false;
/* 8805 */     boolean inside_lower = false;
/* 8806 */     boolean inside_scroller = false;
/* 8807 */     boolean inside_left_adjuster = false;
/* 8808 */     boolean inside_right_adjuster = false;
/* 8809 */     this.insideLeftButton = false;
/* 8810 */     this.insideRightButton = false;
/* 8811 */     int adjuster = -1;
/* 8812 */     int sampleCount = getSampleCount();
/*      */     
/*      */ 
/* 8815 */     int xpos = ((MouseEvent)event).getX();
/* 8816 */     int ypos = ((MouseEvent)event).getY();
/*      */     
/* 8818 */     if (this.gridAlignment == 1) {
/* 8819 */       adjuster = -1;
/* 8820 */       for (int index = 0; index < this.rangeAdjusterBounds.length; index++) {
/* 8821 */         inside_range = (xpos >= this.rangeAdjusterBounds[index].x) && (xpos <= this.rangeAdjusterBounds[index].x + this.rangeAdjusterBounds[index].width);
/* 8822 */         inside_range = (inside_range) && (ypos >= this.rangeAdjusterBounds[index].y - 3) && (ypos <= this.rangeAdjusterBounds[index].y + this.rangeAdjusterBounds[index].height + 1);
/* 8823 */         inside_upper = (ypos >= this.rangeAdjusterBounds[index].y - 1) && (ypos <= this.rangeAdjusterBounds[index].y + 3);
/* 8824 */         inside_upper = (inside_upper) && (xpos >= this.rangeAdjusterBounds[index].x) && (xpos <= this.rangeAdjusterBounds[index].x + this.rangeAdjusterBounds[index].width);
/* 8825 */         inside_lower = (ypos >= this.rangeAdjusterBounds[index].y + this.rangeAdjusterBounds[index].height - 3) && (ypos <= this.rangeAdjusterBounds[index].y + this.rangeAdjusterBounds[index].height + 1);
/* 8826 */         inside_lower = (inside_lower) && (xpos >= this.rangeAdjusterBounds[index].x) && (xpos <= this.rangeAdjusterBounds[index].x + this.rangeAdjusterBounds[index].width);
/* 8827 */         if ((inside_range) || (inside_upper) || (inside_lower)) {
/* 8828 */           adjuster = index;
/* 8829 */           break;
/*      */         }
/*      */       }
/*      */     } else {
/* 8833 */       adjuster = -1;
/* 8834 */       for (int index = 0; index < this.rangeAdjusterBounds.length; index++) {
/* 8835 */         inside_range = (ypos >= this.rangeAdjusterBounds[index].y) && (ypos <= this.rangeAdjusterBounds[index].y + this.rangeAdjusterBounds[index].height);
/* 8836 */         inside_range = (inside_range) && (xpos >= this.rangeAdjusterBounds[index].x - 1) && (xpos <= this.rangeAdjusterBounds[index].x + this.rangeAdjusterBounds[index].width + 1);
/* 8837 */         inside_lower = (xpos >= this.rangeAdjusterBounds[index].x - 1) && (xpos <= this.rangeAdjusterBounds[index].x + 2);
/* 8838 */         inside_lower = (inside_lower) && (ypos >= this.rangeAdjusterBounds[index].y) && (ypos <= this.rangeAdjusterBounds[index].y + this.rangeAdjusterBounds[index].height);
/* 8839 */         inside_upper = (xpos >= this.rangeAdjusterBounds[index].x + this.rangeAdjusterBounds[index].width - 3) && (xpos <= this.rangeAdjusterBounds[index].x + this.rangeAdjusterBounds[index].width + 1);
/* 8840 */         inside_upper = (inside_upper) && (ypos >= this.rangeAdjusterBounds[index].y) && (ypos <= this.rangeAdjusterBounds[index].y + this.rangeAdjusterBounds[index].height);
/* 8841 */         if ((inside_range) || (inside_upper) || (inside_lower)) {
/* 8842 */           adjuster = index;
/* 8843 */           break;
/*      */         }
/*      */       }
/*      */     }
/*      */     
/*      */ 
/* 8849 */     if (!inside_range) {
/* 8850 */       if (this.gridAlignment == 1) {
/* 8851 */         int left = this.sampleScrollerBounds.x;
/* 8852 */         int right = this.sampleScrollerBounds.x + this.sampleScrollerBounds.width;
/* 8853 */         inside_scroller = (ypos >= this.sampleScrollerBounds.y) && (ypos <= this.sampleScrollerBounds.y + this.sampleScrollerBounds.height);
/* 8854 */         inside_scroller = (inside_scroller) && (xpos >= left - 1) && (xpos <= right + 1);
/* 8855 */         this.insideLeftButton = ((inside_scroller) && (xpos >= left) && (xpos <= left + 8));
/* 8856 */         this.insideRightButton = ((inside_scroller) && (xpos >= right - 8) && (xpos <= right));
/* 8857 */         inside_left_adjuster = (inside_scroller) && (xpos >= this.leftAdjusterPos) && (xpos <= this.leftAdjusterPos + 2);
/* 8858 */         inside_right_adjuster = (inside_scroller) && (xpos >= this.rightAdjusterPos - 2) && (xpos <= this.rightAdjusterPos);
/*      */         
/* 8860 */         inside_scroller = (inside_scroller) && (xpos > this.leftAdjusterPos + 2) && (xpos < this.rightAdjusterPos - 2);
/*      */       } else {
/* 8862 */         int top = this.sampleScrollerBounds.y;
/* 8863 */         int bottom = this.sampleScrollerBounds.y + this.sampleScrollerBounds.height;
/* 8864 */         inside_scroller = (xpos >= this.sampleScrollerBounds.x) && (xpos <= this.sampleScrollerBounds.x + this.sampleScrollerBounds.width);
/* 8865 */         inside_scroller = (inside_scroller) && (ypos >= top - 1) && (ypos <= bottom + 1);
/* 8866 */         this.insideLeftButton = ((inside_scroller) && (ypos >= top) && (ypos <= top + 8));
/* 8867 */         this.insideRightButton = ((inside_scroller) && (ypos >= bottom - 8) && (ypos <= bottom));
/* 8868 */         inside_left_adjuster = (inside_scroller) && (ypos >= this.leftAdjusterPos) && (ypos <= this.leftAdjusterPos + 2);
/* 8869 */         inside_right_adjuster = (inside_scroller) && (ypos >= this.rightAdjusterPos - 2) && (ypos <= this.rightAdjusterPos);
/*      */         
/* 8871 */         inside_scroller = (inside_scroller) && (ypos > this.leftAdjusterPos + 2) && (ypos < this.rightAdjusterPos - 2);
/*      */       }
/*      */     }
/*      */     
/* 8875 */     if (((inside_upper) || (inside_lower)) && (inside_range)) {
/* 8876 */       this.new_cursor = (this.gridAlignment == 1 ? RESIZE_CURSOR : RESIZE_HOR_CURSOR);
/* 8877 */     } else if (inside_range) {
/* 8878 */       this.new_cursor = HAND_CURSOR;
/* 8879 */     } else if ((inside_left_adjuster) || (inside_right_adjuster)) {
/* 8880 */       this.new_cursor = (this.gridAlignment == 1 ? RESIZE_HOR_CURSOR : RESIZE_CURSOR);
/* 8881 */     } else if (inside_scroller) {
/* 8882 */       this.new_cursor = HAND_CURSOR;
/*      */     }
/* 8884 */     switch (event.getID())
/*      */     {
/*      */     case 503: 
/*      */       break;
/*      */     
/*      */ 
/*      */ 
/*      */     case 501: 
/* 8892 */       this.adjustingUpper = ((inside_upper) && (inside_range));
/* 8893 */       this.adjustingLower = ((inside_lower) && (inside_range));
/* 8894 */       this.slidingAdjuster = ((inside_range) && (!inside_upper) && (!inside_lower));
/* 8895 */       this.adjusterIndex = adjuster;
/* 8896 */       this.leftPushed = this.insideLeftButton;
/* 8897 */       this.rightPushed = this.insideRightButton;
/* 8898 */       this.adjustingLeft = inside_left_adjuster;
/* 8899 */       this.adjustingRight = inside_right_adjuster;
/* 8900 */       this.slidingScroller = ((inside_scroller) && (!inside_left_adjuster) && (!inside_right_adjuster));
/* 8901 */       this.lastAdjusterClick = (this.gridAlignment == 1 ? ypos : xpos);
/* 8902 */       this.lastScrollerClick = (this.gridAlignment == 1 ? xpos : ypos);
/* 8903 */       long now = System.currentTimeMillis();
/*      */       
/*      */ 
/* 8906 */       if ((this.insideLeftButton) || (this.insideRightButton) || (inside_scroller) || (inside_range) || 
/* 8907 */         (inside_upper) || (inside_lower) || (inside_right_adjuster) || (inside_left_adjuster)) {
/* 8908 */         this.shouldSelect = false;
/*      */       }
/*      */       
/*      */ 
/* 8912 */       if (this.insideLeftButton) {
/* 8913 */         double delta = this.chartDataBounds.width / sampleCount / this.chartDataBounds.width;
/* 8914 */         if (this.gridAlignment == 0) {
/* 8915 */           delta = this.chartDataBounds.height / sampleCount / this.chartDataBounds.height;
/*      */         }
/* 8917 */         delta = Math.min(delta, this.leftScrollerFactor);
/* 8918 */         this.leftScrollerFactor -= delta;
/* 8919 */         this.rightScrollerFactor += delta;
/* 8920 */         if (this.scrollerThread == null) {
/* 8921 */           this.scrollerThread = new Thread(this);
/* 8922 */           this.scrollerThread.start();
/*      */         }
/*      */         
/*      */ 
/*      */       }
/* 8927 */       else if (this.insideRightButton) {
/* 8928 */         double delta = this.chartDataBounds.width / sampleCount / this.chartDataBounds.width;
/* 8929 */         if (this.gridAlignment == 0) {
/* 8930 */           delta = this.chartDataBounds.height / sampleCount / this.chartDataBounds.height;
/*      */         }
/* 8932 */         delta = Math.min(delta, this.rightScrollerFactor);
/* 8933 */         this.leftScrollerFactor += delta;
/* 8934 */         this.rightScrollerFactor -= delta;
/* 8935 */         if (this.scrollerThread == null) {
/* 8936 */           this.scrollerThread = new Thread(this);
/* 8937 */           this.scrollerThread.start();
/*      */         }
/*      */         
/*      */ 
/*      */       }
/* 8942 */       else if ((inside_scroller) && (!this.insideLeftButton) && (!this.insideRightButton)) {
/* 8943 */         if (now - this.lastClickTime < 350L) {
/* 8944 */           this.leftScrollerFactor = 0.0D;
/* 8945 */           this.rightScrollerFactor = 0.0D;
/*      */         }
/*      */         
/*      */ 
/*      */       }
/* 8950 */       else if ((inside_range) && (!inside_upper) && (!inside_lower) && 
/* 8951 */         (now - this.lastClickTime < 350L)) {
/* 8952 */         for (int i = 0; i < this.rangeAdjusterOn.length; i++) {
/* 8953 */           int rangeIndex = this.rangeAdjusted[this.adjusterIndex] == -1 ? i : this.rangeAdjusted[this.adjusterIndex];
/* 8954 */           this.currentUpperRange[rangeIndex] = this.upperRange[rangeIndex];
/* 8955 */           this.currentLowerRange[rangeIndex] = this.lowerRange[rangeIndex];
/* 8956 */           for (int overlay = 0; overlay < this.overlayCharts.size(); overlay++) {
/* 8957 */             Chart chart = (Chart)this.overlayCharts.elementAt(overlay);
/* 8958 */             if (chart != null) {
/* 8959 */               chart.currentUpperRange[rangeIndex] = chart.upperRange[rangeIndex];
/* 8960 */               chart.currentLowerRange[rangeIndex] = chart.lowerRange[rangeIndex];
/*      */             }
/*      */           }
/*      */         }
/*      */       }
/*      */       
/*      */ 
/*      */ 
/* 8968 */       if (!this.rightToLeftScrollingOn) {
/* 8969 */         this.visibleSamples[0] = ((int)Math.round(sampleCount * this.leftScrollerFactor));
/* 8970 */         this.visibleSamples[1] = ((int)Math.round(sampleCount - this.visibleSamples[0] - sampleCount * this.rightScrollerFactor + 1.0D));
/*      */       }
/*      */       
/*      */ 
/* 8974 */       this.needRender = true;
/* 8975 */       this.needChartCalculation = true;
/* 8976 */       for (int i = 0; i < this.overlayCharts.size(); i++) {
/* 8977 */         Chart overlay = (Chart)this.overlayCharts.elementAt(i);
/* 8978 */         overlay.needChartCalculation = true;
/*      */       }
/* 8980 */       repaint();
/* 8981 */       break;
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */     case 506: 
/* 8987 */       if ((this.adjustingUpper) || (this.adjustingLower) || (this.slidingAdjuster) || (this.slidingScroller) || 
/* 8988 */         (this.leftPushed) || (this.rightPushed) || (this.adjustingLeft) || (this.adjustingRight))
/*      */       {
/*      */ 
/*      */ 
/* 8992 */         int pos = ((MouseEvent)event).getY();
/* 8993 */         if (this.gridAlignment == 0) {
/* 8994 */           pos = ((MouseEvent)event).getX();
/*      */         }
/* 8996 */         Rectangle grid = this.currentBounds;
/* 8997 */         double factor = 0.0D;
/* 8998 */         int adjusterPos = getRangeAdjusterPosition();
/*      */         
/*      */ 
/* 9001 */         if (this.adjustingUpper) {
/* 9002 */           if (this.gridAlignment == 1) {
/* 9003 */             this.new_cursor = RESIZE_CURSOR;
/* 9004 */             pos -= grid.y;
/* 9005 */             if (adjusterPos == 0) {
/* 9006 */               pos += this.depth3dPoint.y;
/*      */             }
/* 9008 */             factor = 1.0D - pos / grid.height;
/*      */           } else {
/* 9010 */             this.new_cursor = RESIZE_HOR_CURSOR;
/* 9011 */             pos -= grid.x;
/* 9012 */             if (adjusterPos == 0) {
/* 9013 */               pos += this.depth3dPoint.x;
/*      */             }
/* 9015 */             factor = pos / grid.width;
/*      */           }
/*      */           
/* 9018 */           if (this.rangeAdjusted[this.adjusterIndex] == -1) {
/* 9019 */             for (int i = 0; i < this.rangeOn.length; i++) {
/* 9020 */               adjustRange(this, i, factor, 0.0D);
/* 9021 */               for (int chart = 0; chart < this.overlayCharts.size(); chart++) {
/* 9022 */                 adjustRange((Chart)this.overlayCharts.elementAt(chart), i, factor, 0.0D);
/*      */               }
/*      */             }
/*      */           }
/*      */           else
/*      */           {
/* 9028 */             adjustRange(this, this.rangeAdjusted[this.adjusterIndex], factor, 0.0D);
/* 9029 */             for (int i = 0; i < this.overlayCharts.size(); i++) {
/* 9030 */               adjustRange((Chart)this.overlayCharts.elementAt(i), this.rangeAdjusted[this.adjusterIndex], factor, 0.0D);
/*      */             }
/*      */             
/*      */           }
/*      */           
/*      */         }
/* 9036 */         else if (this.adjustingLower) {
/* 9037 */           if (this.gridAlignment == 1) {
/* 9038 */             this.new_cursor = RESIZE_CURSOR;
/* 9039 */             pos -= grid.y;
/* 9040 */             if (adjusterPos == 0) {
/* 9041 */               pos += this.depth3dPoint.y;
/*      */             }
/* 9043 */             factor = 1.0D - pos / grid.height;
/*      */           } else {
/* 9045 */             this.new_cursor = RESIZE_HOR_CURSOR;
/* 9046 */             pos -= grid.x;
/* 9047 */             if (adjusterPos == 0) {
/* 9048 */               pos += this.depth3dPoint.x;
/*      */             }
/* 9050 */             factor = pos / grid.width;
/*      */           }
/*      */           
/* 9053 */           if (this.rangeAdjusted[this.adjusterIndex] == -1) {
/* 9054 */             for (int i = 0; i < this.rangeOn.length; i++) {
/* 9055 */               adjustRange(this, i, 0.0D, factor);
/* 9056 */               for (int chart = 0; chart < this.overlayCharts.size(); chart++) {
/* 9057 */                 adjustRange((Chart)this.overlayCharts.elementAt(chart), i, 0.0D, factor);
/*      */               }
/*      */             }
/*      */           }
/*      */           else
/*      */           {
/* 9063 */             adjustRange(this, this.rangeAdjusted[this.adjusterIndex], 0.0D, factor);
/* 9064 */             for (int i = 0; i < this.overlayCharts.size(); i++) {
/* 9065 */               adjustRange((Chart)this.overlayCharts.elementAt(i), this.rangeAdjusted[this.adjusterIndex], 0.0D, factor);
/*      */             }
/*      */             
/*      */           }
/*      */           
/*      */         }
/* 9071 */         else if (this.slidingAdjuster) {
/* 9072 */           this.new_cursor = HAND_CURSOR;
/*      */           
/* 9074 */           int change_pos = this.lastAdjusterClick - pos;
/* 9075 */           if (this.gridAlignment == 1) {
/* 9076 */             factor = change_pos / grid.height;
/*      */           } else {
/* 9078 */             factor = change_pos / grid.width;
/*      */           }
/*      */           
/* 9081 */           if (this.rangeAdjusted[this.adjusterIndex] == -1) {
/* 9082 */             for (int i = 0; i < this.rangeOn.length; i++) {
/* 9083 */               adjustRangeSlide(this, i, factor);
/* 9084 */               for (int chart = 0; chart < this.overlayCharts.size(); chart++) {
/* 9085 */                 adjustRangeSlide((Chart)this.overlayCharts.elementAt(chart), i, factor);
/*      */               }
/*      */             }
/*      */           }
/*      */           else
/*      */           {
/* 9091 */             adjustRangeSlide(this, this.rangeAdjusted[this.adjusterIndex], factor);
/* 9092 */             for (int i = 0; i < this.overlayCharts.size(); i++) {
/* 9093 */               adjustRangeSlide((Chart)this.overlayCharts.elementAt(i), this.rangeAdjusted[this.adjusterIndex], factor);
/*      */             }
/*      */           }
/* 9096 */           this.lastAdjusterClick = pos;
/*      */ 
/*      */ 
/*      */         }
/* 9100 */         else if (this.adjustingLeft)
/*      */         {
/* 9102 */           if (this.gridAlignment == 1) {
/* 9103 */             this.new_cursor = RESIZE_HOR_CURSOR;
/* 9104 */             int left = this.sampleScrollerBounds.x + 8;
/* 9105 */             pos = Math.max(((MouseEvent)event).getX(), left);
/* 9106 */             pos = Math.min(pos, this.rightAdjusterPos - 10);
/* 9107 */             this.leftScrollerFactor = ((pos - left) / this.sampleScrollerSpace);
/*      */           } else {
/* 9109 */             this.new_cursor = RESIZE_CURSOR;
/* 9110 */             int top = this.sampleScrollerBounds.y + 8;
/* 9111 */             pos = Math.max(((MouseEvent)event).getY(), top);
/* 9112 */             pos = Math.min(pos, this.rightAdjusterPos - 10);
/* 9113 */             this.leftScrollerFactor = ((pos - top) / this.sampleScrollerSpace);
/*      */           }
/*      */           
/*      */ 
/*      */         }
/* 9118 */         else if (this.adjustingRight) {
/* 9119 */           if (this.gridAlignment == 1) {
/* 9120 */             this.new_cursor = RESIZE_HOR_CURSOR;
/* 9121 */             int right = this.sampleScrollerBounds.x + this.sampleScrollerBounds.width - 8;
/* 9122 */             pos = Math.min(((MouseEvent)event).getX(), right);
/* 9123 */             pos = Math.max(pos, this.leftAdjusterPos + 10);
/* 9124 */             this.rightScrollerFactor = ((right - pos) / this.sampleScrollerSpace);
/*      */           } else {
/* 9126 */             this.new_cursor = RESIZE_CURSOR;
/* 9127 */             int bottom = this.sampleScrollerBounds.y + this.sampleScrollerBounds.height - 8;
/* 9128 */             pos = Math.min(((MouseEvent)event).getY(), bottom);
/* 9129 */             pos = Math.max(pos, this.leftAdjusterPos + 10);
/* 9130 */             this.rightScrollerFactor = ((bottom - pos) / this.sampleScrollerSpace);
/*      */           }
/*      */           
/*      */ 
/*      */         }
/* 9135 */         else if (this.slidingScroller) {
/* 9136 */           this.new_cursor = HAND_CURSOR;
/* 9137 */           int delta = this.gridAlignment == 1 ? xpos - this.lastScrollerClick : ypos - this.lastScrollerClick;
/* 9138 */           factor = delta / this.sampleScrollerSpace;
/* 9139 */           if (delta > 0) {
/* 9140 */             factor = Math.min(factor, this.rightScrollerFactor);
/*      */           } else {
/* 9142 */             factor = -Math.min(-factor, this.leftScrollerFactor);
/*      */           }
/* 9144 */           this.leftScrollerFactor += factor;
/* 9145 */           this.rightScrollerFactor -= factor;
/* 9146 */           this.lastScrollerClick = (this.gridAlignment == 1 ? xpos : ypos);
/*      */         }
/*      */         
/*      */ 
/* 9150 */         if (!this.rightToLeftScrollingOn) {
/* 9151 */           this.visibleSamples[0] = ((int)Math.round(sampleCount * this.leftScrollerFactor));
/* 9152 */           this.visibleSamples[1] = ((int)Math.round(sampleCount - this.visibleSamples[0] - sampleCount * this.rightScrollerFactor + 1.0D));
/*      */         }
/*      */         
/*      */ 
/* 9156 */         this.needRender = true;
/* 9157 */         this.needChartCalculation = true;
/* 9158 */         for (int i = 0; i < this.overlayCharts.size(); i++) {
/* 9159 */           Chart overlay = (Chart)this.overlayCharts.elementAt(i);
/* 9160 */           overlay.needChartCalculation = true;
/*      */         }
/* 9162 */         repaint(); }
/* 9163 */       break;
/*      */     
/*      */ 
/*      */     case 502: 
/* 9167 */       this.adjustingUpper = (this.adjustingLower = this.slidingAdjuster = 0);
/* 9168 */       this.adjusterIndex = -1;
/* 9169 */       this.leftPushed = (this.rightPushed = 0);
/* 9170 */       this.adjustingLeft = (this.adjustingRight = this.slidingScroller = 0);
/*      */       
/* 9172 */       if (this.scrollerThread != null) {
/* 9173 */         this.scrollerThread = null;
/*      */       }
/* 9175 */       this.needRender = true;
/* 9176 */       this.needChartCalculation = true;
/* 9177 */       for (int i = 0; i < this.overlayCharts.size(); i++) {
/* 9178 */         Chart overlay = (Chart)this.overlayCharts.elementAt(i);
/* 9179 */         overlay.needChartCalculation = true;
/*      */       }
/* 9181 */       repaint();
/*      */     }
/*      */     
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
/*      */   private void adjustRange(Chart chart, int rangeIndex, double upperFactor, double lowerFactor)
/*      */   {
/* 9196 */     if (upperFactor != 0.0D) {
/* 9197 */       double upperValue = chart.lowerRange[rangeIndex] + upperFactor * (chart.upperRange[rangeIndex] - chart.lowerRange[rangeIndex]);
/* 9198 */       if (this.upperRange[rangeIndex] >= this.lowerRange[rangeIndex]) {
/* 9199 */         if (upperValue > chart.currentLowerRange[rangeIndex]) {
/* 9200 */           chart.currentUpperRange[rangeIndex] = upperValue;
/*      */         }
/* 9202 */         chart.currentUpperRange[rangeIndex] = Math.min(chart.currentUpperRange[rangeIndex], chart.upperRange[rangeIndex]);
/* 9203 */       } else if (this.upperRange[rangeIndex] < this.lowerRange[rangeIndex]) {
/* 9204 */         if (upperValue < chart.currentLowerRange[rangeIndex]) {
/* 9205 */           chart.currentUpperRange[rangeIndex] = upperValue;
/*      */         }
/* 9207 */         chart.currentUpperRange[rangeIndex] = Math.max(chart.currentUpperRange[rangeIndex], chart.upperRange[rangeIndex]);
/*      */       }
/*      */     }
/*      */     
/* 9211 */     if (lowerFactor != 0.0D) {
/* 9212 */       double lowerValue = chart.lowerRange[rangeIndex] + lowerFactor * (chart.upperRange[rangeIndex] - chart.lowerRange[rangeIndex]);
/* 9213 */       if (this.upperRange[rangeIndex] >= this.lowerRange[rangeIndex]) {
/* 9214 */         if (lowerValue < chart.currentUpperRange[rangeIndex]) {
/* 9215 */           chart.currentLowerRange[rangeIndex] = lowerValue;
/*      */         }
/* 9217 */         chart.currentLowerRange[rangeIndex] = Math.max(chart.currentLowerRange[rangeIndex], chart.lowerRange[rangeIndex]);
/* 9218 */       } else if (this.upperRange[rangeIndex] < this.lowerRange[rangeIndex]) {
/* 9219 */         if (lowerValue > chart.currentUpperRange[rangeIndex]) {
/* 9220 */           chart.currentLowerRange[rangeIndex] = lowerValue;
/*      */         }
/* 9222 */         chart.currentLowerRange[rangeIndex] = Math.min(chart.currentLowerRange[rangeIndex], chart.lowerRange[rangeIndex]);
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
/*      */   private void adjustRangeSlide(Chart chart, int rangeIndex, double factor)
/*      */   {
/* 9235 */     double change = factor * (chart.upperRange[rangeIndex] - chart.lowerRange[rangeIndex]);
/* 9236 */     change = this.gridAlignment == 1 ? change : -change;
/*      */     
/* 9238 */     if (chart.upperRange[rangeIndex] >= chart.lowerRange[rangeIndex]) {
/* 9239 */       if (change >= 0.0D) {
/* 9240 */         change = Math.min(chart.upperRange[rangeIndex] - chart.currentUpperRange[rangeIndex], change);
/* 9241 */       } else if (chart.currentLowerRange[rangeIndex] + change < chart.lowerRange[rangeIndex]) {
/* 9242 */         change = chart.lowerRange[rangeIndex] - chart.currentLowerRange[rangeIndex];
/*      */       }
/* 9244 */     } else if (chart.upperRange[rangeIndex] < chart.lowerRange[rangeIndex]) {
/* 9245 */       if (change <= 0.0D) {
/* 9246 */         change = Math.max(chart.upperRange[rangeIndex] - chart.currentUpperRange[rangeIndex], change);
/* 9247 */       } else if (chart.currentLowerRange[rangeIndex] + change > chart.lowerRange[rangeIndex]) {
/* 9248 */         change = chart.lowerRange[rangeIndex] - chart.currentLowerRange[rangeIndex];
/*      */       }
/*      */     }
/*      */     
/* 9252 */     chart.currentUpperRange[rangeIndex] += change;
/* 9253 */     chart.currentLowerRange[rangeIndex] += change;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void run()
/*      */   {
/* 9262 */     Thread thisThread = Thread.currentThread();
/*      */     try {
/* 9264 */       Thread.sleep(250L);
/*      */     }
/*      */     catch (InterruptedException localInterruptedException) {}
/*      */     
/* 9268 */     int sampleCount = getSampleCount();
/* 9269 */     double delta = this.chartDataBounds.width / getSampleCount() / this.chartDataBounds.width;
/* 9270 */     while (this.scrollerThread == thisThread) {
/* 9271 */       if (this.insideLeftButton)
/*      */       {
/* 9273 */         delta = Math.min(delta, this.leftScrollerFactor);
/* 9274 */         this.leftScrollerFactor -= delta;
/* 9275 */         this.rightScrollerFactor += delta;
/*      */         
/* 9277 */         if (!this.rightToLeftScrollingOn) {
/* 9278 */           this.visibleSamples[0] = ((int)Math.round(sampleCount * this.leftScrollerFactor));
/* 9279 */           this.visibleSamples[1] = ((int)Math.round(sampleCount - this.visibleSamples[0] - sampleCount * this.rightScrollerFactor));
/*      */         }
/*      */         
/* 9282 */         this.needRender = true;
/* 9283 */         this.needChartCalculation = true;
/* 9284 */         repaint();
/*      */       }
/* 9286 */       if (this.insideRightButton)
/*      */       {
/* 9288 */         delta = Math.min(delta, this.rightScrollerFactor);
/* 9289 */         this.leftScrollerFactor += delta;
/* 9290 */         this.rightScrollerFactor -= delta;
/*      */         
/* 9292 */         if (!this.rightToLeftScrollingOn) {
/* 9293 */           this.visibleSamples[0] = ((int)Math.round(sampleCount * this.leftScrollerFactor));
/* 9294 */           this.visibleSamples[1] = ((int)Math.round(sampleCount - this.visibleSamples[0] - sampleCount * this.rightScrollerFactor));
/*      */         }
/*      */         
/* 9297 */         this.needRender = true;
/* 9298 */         this.needChartCalculation = true;
/* 9299 */         repaint();
/*      */       }
/*      */       try
/*      */       {
/* 9303 */         Thread.sleep(35L);
/*      */       }
/*      */       catch (InterruptedException localInterruptedException1) {}
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   void notifyListeners(Chart chart, int selected, Object item)
/*      */   {
/* 9317 */     if (this.listeners != null)
/*      */     {
/* 9319 */       ItemEvent event = new ItemEvent(chart, 701, item, selected);
/*      */       
/* 9321 */       for (Enumeration e = this.listeners.elements(); e.hasMoreElements();) {
/* 9322 */         ((ItemListener)e.nextElement()).itemStateChanged(event);
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
/*      */   public static String getVersion()
/*      */   {
/* 9335 */     return "3.0pre2";
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public static void main(String[] argv)
/*      */   {
/* 9343 */     System.out.println("EasyCharts " + getVersion());
/* 9344 */     System.out.println("Copyright 1998-2004, ObjectPlanet, Inc.");
/*      */   }
/*      */   
/* 9347 */   private boolean showValue_As_Time = false;
/*      */   
/* 9349 */   public void showValueAsTime() { this.showValue_As_Time = true; }
/*      */   
/*      */   public static final int HORIZONTAL = 0;
/*      */   public static final int VERTICAL = 1;
/*      */   public static final int LEFT = 0;
/*      */   public static final int RIGHT = 1;
/*      */   public static final int TOP = 2;
/*      */   public static final int BOTTOM = 3;
/*      */   public static final int TARGET_LINE_NO_LABEL = 0;
/*      */   public static final int TARGET_LINE_ID_LABEL = 1;
/*      */   public static final int TARGET_LINE_VALUE_LABEL = 2;
/*      */   public static final int TARGET_LINE_ID_AND_VALUE_LABEL = 3;
/*      */   public static final int INSIDE = 0;
/*      */   public static final int OUTSIDE = 1;
/*      */   public static final int BELOW = 2;
/*      */   public static final int FLOATING = 3;
/*      */   public static final int BELOW_AND_FLOATING = 4;
/*      */   public static final int POINTING = 5;
/*      */   private static final int X_AXIS = 0;
/*      */   private static final int Y_AXIS = 1;
/*      */   protected ChartData chartData;
/*      */   protected Vector overlayCharts;
/*      */   protected int[] visibleSamples;
/*      */   private Color[] sampleColors;
/*      */   private Color[] sampleLabelColors;
/*      */   private Color[] seriesLabelColors;
/*      */   private Color[] valueLabelColors;
/*      */   private Color sampleLabelSelectionColor;
/*      */   int sampleLabelStyle;
/*      */   int seriesLabelStyle;
/*      */   int valueLabelStyle;
/*      */   boolean sampleLabelsOn;
/*      */   boolean seriesLabelsOn;
/*      */   private boolean[] valueLabelsOn;
/*      */   boolean barLabelsOn;
/*      */   private String chartTitle;
/*      */   private boolean chartTitleOn;
/*      */   private int[] sampleDecimalCount;
/*      */   private boolean legendOn;
/*      */   private int legendColumns;
/*      */   private int legendPosition;
/*      */   private boolean legendReverseOn;
/*      */   private boolean display3dOn;
/*      */   private Color chartBackground;
/*      */   private Color chartForeground;
/*      */   String gridImage;
/*      */   private Hashtable labels;
/*      */   private Hashtable labelFonts;
/*      */   private Hashtable labelAngles;
/*      */   String[] legendLabels;
/*      */   private Color[] legendColors;
/*      */   private String[] legendImages;
/*      */   private boolean automaticRepaintOn;
/*      */   private Insets graphInsets;
/*      */   private Dimension preferredSize;
/*      */   int lastSelectedSample;
/*      */   int lastSelectedSeries;
/*      */   boolean multiSeriesOn;
/*      */   private boolean printAsBitmap;
/*      */   Hashtable images;
/*      */   private boolean overlayChartOn;
/*      */   private boolean servletModeOn;
/*      */   private boolean floatingOnLegendOn;
/*      */   Point mousePosition;
/*      */   private String thousandsDelimiter;
/*      */   String[] labelIDs;
/*      */   Hashtable labelTexts;
/*      */   Hashtable labelXs;
/*      */   Hashtable labelYs;
/*      */   Hashtable labelURLs;
/*      */   Hashtable labelSeries;
/*      */   Hashtable labelSamples;
/*      */   boolean displayVersionOn;
/*      */   boolean[] rangeOn;
/*      */   double[] upperRange;
/*      */   double[] lowerRange;
/*      */   double[] currentUpperRange;
/*      */   double[] currentLowerRange;
/*      */   Rectangle[] rangeBounds;
/*      */   double leftSampleAxisRange;
/*      */   double rightSampleAxisRange;
/*      */   int[] rangePosition;
/*      */   Color[] rangeColor;
/*      */   int[] seriesRange;
/*      */   boolean valueLinesOn;
/*      */   int maxValueLineCount;
/*      */   Color valueLinesColor;
/*      */   int maxGridLineCount;
/*      */   double[] gridLines;
/*      */   double[] defaultGridLines;
/*      */   Color defaultGridLinesColor;
/*      */   Color[] gridLineColors;
/*      */   boolean[] rangeLabelsOn;
/*      */   int[] rangeDecimalCount;
/*      */   Hashtable targetsLabel;
/*      */   Hashtable targetsValue;
/*      */   Hashtable targetsColor;
/*      */   Hashtable targetsStyle;
/*      */   int targetLabelsPosition;
/*      */   int[] zeroLine;
/*      */   protected Point depth3dPoint;
/*      */   int depth3d;
/*      */   int gridAlignment;
/*      */   static final float ANGLE = 1.25F;
/*      */   static final float DEPTH = 0.75F;
/*      */   Rectangle currentBounds;
/*      */   String[] barLabels;
/*      */   boolean rightToLeftScrollingOn;
/*      */   private boolean[] gridAdjustment;
/*      */   private int gridEdgeToAdjust;
/*      */   boolean[] rangeAdjusterOn;
/*      */   int rangeAdjusterPosition;
/*      */   int[] rangeAdjusted;
/*      */   boolean adjustingUpper;
/*      */   boolean adjustingLower;
/*      */   boolean slidingAdjuster;
/*      */   int adjusterIndex;
/*      */   int lastAdjusterClick;
/*      */   Rectangle[] rangeAdjusterBounds;
/*      */   static Cursor RESIZE_CURSOR;
/*      */   static Cursor RESIZE_HOR_CURSOR;
/*      */   static Cursor GRID_CURSOR_HORZ;
/*      */   static Cursor GRID_CURSOR_VERT;
/*      */   static Cursor HAND_CURSOR;
/*      */   static Cursor DEFAULT_CURSOR;
/*      */   Cursor old_cursor;
/*      */   Cursor new_cursor;
/*      */   boolean sampleScrollerOn;
/*      */   boolean leftPushed;
/*      */   boolean rightPushed;
/*      */   boolean adjustingLeft;
/*      */   boolean adjustingRight;
/*      */   boolean slidingScroller;
/*      */   boolean insideLeftButton;
/*      */   boolean insideRightButton;
/*      */   int lastScrollerClick;
/*      */   double leftScrollerFactor;
/*      */   double rightScrollerFactor;
/*      */   int leftAdjusterPos;
/*      */   int rightAdjusterPos;
/*      */   int sampleScrollerSpace;
/*      */   Thread scrollerThread;
/*      */   Rectangle sampleScrollerBounds;
/*      */   Rectangle chartDataBounds;
/*      */   Rectangle visibleLegend;
/*      */   private Rectangle verticalLegendScroller;
/*      */   private Rectangle horizontalLegendScroller;
/*      */   private double verticalLegendFactor;
/*      */   private double horizontalLegendFactor;
/*      */   private boolean slidingVerticalLegend;
/*      */   private boolean slidingHorizontalLegend;
/*      */   boolean verticalLegendScrollerOn;
/*      */   boolean horizontalLegendScrollerOn;
/*      */   private boolean topLegendPushed;
/*      */   private boolean bottomLegendPushed;
/*      */   private boolean leftLegendPushed;
/*      */   private boolean rightLegendPushed;
/*      */   private int[] legendRowHeight;
/*      */   private int[] legendColumnWidth;
/*      */   boolean shouldSelect;
/*      */   private boolean zoomOn;
/*      */   private Rectangle mouseBox;
/*      */   private boolean zoomOutPushed;
/*      */   private int mousePressX;
/*      */   private int mousePressY;
/*      */   String chartType;
/*      */   Chart parentChart;
/*      */   Rectangle graphBounds;
/*      */   private ChartSample selectedSample;
/*      */   Rectangle[] legendBounds;
/*      */   boolean[] legendSelection;
/*      */   int mouseOverSampleIndex;
/*      */   int mouseOverSeriesIndex;
/*      */   private Vector listeners;
/*      */   private NumberFormat numberFormatter;
/*      */   private Locale currentLocale;
/*      */   boolean needRender;
/*      */   boolean needGraphBounds;
/*      */   boolean needChartCalculation;
/*      */   Rectangle legend;
/*      */   long lastClickTime;
/*      */   private Hashtable labelSizeCache;
/*      */   Image offscreen;
/*      */   Graphics external_gc;
/*      */   private Frame servletFrame;
/*      */   private long lastRenderTime;
/*      */   protected long lastCalculationTime;
/*      */   private Chart2 c2;
/*      */   private static final int DOUBLE_CLICK_TIME = 350;
/*      */   MediaTracker imageTracker;
/*      */   boolean externalGraphicsOn;
/*      */   private ChartSample lastSelection;
/*      */   String dragged_label;
/*      */   double dragged_start_x;
/*      */   double dragged_start_y;
/*      */   private boolean componentResized;
/*      */   private Image rotateImage;
/*      */   private Image pixel_grabber_image;
/*      */   private static int fifo_count;
/*      */   private static int fifo_head;
/*      */   private static int fifo_tail;
/*      */   private Class bufferedImageClass;
/*      */   private Constructor bufferedImageConstructor;
/*      */   private boolean bufferedImageClass_tried_loading;
/*      */   private int TYPE_INT_RGB;
/*      */ }


/* Location:              E:\java\java学习\hutubill\lib\all.jar!\com\objectplanet\chart\Chart.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */
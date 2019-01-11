/*      */ package com.objectplanet.chart;
/*      */ 
/*      */ import java.awt.Color;
/*      */ import java.awt.Dimension;
/*      */ import java.awt.Font;
/*      */ import java.awt.FontMetrics;
/*      */ import java.awt.Graphics;
/*      */ import java.awt.Image;
/*      */ import java.awt.Insets;
/*      */ import java.awt.Point;
/*      */ import java.awt.Polygon;
/*      */ import java.awt.Rectangle;
/*      */ import java.awt.Shape;
/*      */ import java.io.PrintStream;
/*      */ import java.util.Hashtable;
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
/*      */ public class LineChart
/*      */   extends Chart
/*      */ {
/*      */   public static final int SAMPLE_HIGHLIGHT_CIRCLE = 0;
/*      */   public static final int SAMPLE_HIGHLIGHT_CIRCLE_OPAQUE = 1;
/*      */   public static final int SAMPLE_HIGHLIGHT_CIRCLE_FILLED = 2;
/*      */   public static final int SAMPLE_HIGHLIGHT_SQUARE = 3;
/*      */   public static final int SAMPLE_HIGHLIGHT_SQUARE_OPAQUE = 4;
/*      */   public static final int SAMPLE_HIGHLIGHT_SQUARE_FILLED = 5;
/*      */   public static final int SAMPLE_HIGHLIGHT_DIAMOND = 6;
/*      */   public static final int SAMPLE_HIGHLIGHT_DIAMOND_OPAQUE = 7;
/*      */   public static final int SAMPLE_HIGHLIGHT_DIAMOND_FILLED = 8;
/*      */   private boolean autoLabelSpacingOn;
/*      */   private int[] lineWidth;
/*      */   private int[][] lineStroke;
/*      */   private boolean stackedOn;
/*      */   private boolean[][] sampleHighlightOn;
/*      */   private boolean[] sampleHighlightOn_all;
/*      */   private int[] sampleHighlightStyle;
/*      */   private int[] sampleHighlightSize;
/*      */   private boolean[] seriesLinesOn;
/*      */   private boolean[] connectedLinesOn;
/*      */   private String highlightImage;
/*      */   private String[] highlightImages_all;
/*      */   private String[][] highlightImages;
/*      */   protected int[][][] samplePoints;
/*      */   private int[] sampleLabelPos;
/*      */   protected int lastSelectedLine;
/*      */   
/*      */   public LineChart()
/*      */   {
/*  104 */     this(1, 5, 100.0D);
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
/*      */   public LineChart(int seriesCount, int sampleCount, double range)
/*      */   {
/*  117 */     this(seriesCount, sampleCount, range, 0.0D);
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
/*      */   public LineChart(int seriesCount, int sampleCount, double range, double lowerRange)
/*      */   {
/*  131 */     super(sampleCount);
/*  132 */     setAutomaticRepaintOn(false);
/*  133 */     this.chartType = "line";
/*      */     
/*      */ 
/*      */ 
/*  137 */     this.sampleHighlightOn = new boolean[seriesCount][sampleCount];
/*  138 */     this.sampleHighlightOn_all = new boolean[seriesCount];
/*  139 */     this.sampleHighlightStyle = new int[seriesCount];
/*  140 */     this.sampleHighlightSize = new int[seriesCount];
/*  141 */     this.seriesLinesOn = new boolean[seriesCount];
/*  142 */     this.lineWidth = new int[seriesCount];
/*  143 */     this.lineStroke = new int[seriesCount][];
/*  144 */     for (int i = 0; i < this.sampleHighlightSize.length; i++) {
/*  145 */       this.sampleHighlightSize[i] = 6;
/*  146 */       this.seriesLinesOn[i] = true;
/*  147 */       this.lineWidth[i] = 2;
/*      */     }
/*  149 */     this.highlightImages = new String[seriesCount][sampleCount];
/*  150 */     this.highlightImages_all = new String[seriesCount];
/*  151 */     this.connectedLinesOn = new boolean[seriesCount];
/*  152 */     this.sampleLabelPos = new int[sampleCount];
/*  153 */     this.mousePosition = new Point();
/*      */     
/*      */ 
/*  156 */     setSeriesCount(seriesCount);
/*  157 */     setRange(0, range);
/*  158 */     setLowerRange(0, lowerRange);
/*  159 */     setSampleColors(null);
/*  160 */     this.lastSelectedLine = -1;
/*  161 */     this.valueLabelStyle = 1;
/*  162 */     this.sampleLabelStyle = 2;
/*  163 */     this.seriesLabelStyle = 3;
/*  164 */     this.autoLabelSpacingOn = false;
/*  165 */     this.multiSeriesOn = true;
/*  166 */     setAutomaticRepaintOn(true);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void reset()
/*      */   {
/*  177 */     super.reset();
/*  178 */     this.valueLabelStyle = 1;
/*  179 */     this.sampleLabelStyle = 2;
/*  180 */     this.seriesLabelStyle = 3;
/*  181 */     this.autoLabelSpacingOn = false;
/*  182 */     this.multiSeriesOn = true;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public String[] getLegendLabels()
/*      */   {
/*  190 */     if (this.legendLabels != null) {
/*  191 */       return this.legendLabels;
/*      */     }
/*  193 */     return getSeriesLabels();
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
/*      */   double getHighestValue(int range)
/*      */   {
/*  207 */     if (this.stackedOn) {
/*  208 */       return Math.max(super.getHighestValue(range), getMaxValue(-2));
/*      */     }
/*  210 */     return super.getHighestValue(range);
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
/*      */   public double getMaxValue(int serie)
/*      */   {
/*  224 */     if ((serie >= -1) || (!this.stackedOn)) {
/*  225 */       return super.getMaxValue(serie);
/*      */     }
/*      */     
/*  228 */     double max = 0.0D;
/*  229 */     int sampleCount = getSampleCount();
/*  230 */     int seriesCount = getSeriesCount();
/*  231 */     for (int sample = 0; sample < sampleCount; sample++)
/*      */     {
/*  233 */       double sum = 0.0D;
/*  234 */       for (int i = 0; i < seriesCount; i++) {
/*  235 */         ChartSample s = getSample(i, sample);
/*  236 */         if ((s != null) && (s.value != null) && (!s.value.isNaN()) && (s.value.doubleValue() > 0.0D)) {
/*  237 */           sum += s.value.doubleValue();
/*      */         }
/*      */       }
/*  240 */       max = Math.max(max, sum);
/*      */     }
/*  242 */     return max;
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
/*  253 */     super.setSampleColors(colors);
/*  254 */     if (colors == null)
/*      */     {
/*      */ 
/*  257 */       setSampleColor(2, new Color(132, 255, 198));
/*      */     }
/*  259 */     autoRepaint();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public int getValuePosition(double value)
/*      */   {
/*  269 */     return getValuePosition(0, value, getGraphBounds());
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public Point getSamplePoint(int serie, int sample)
/*      */   {
/*  280 */     Point point = new Point(this.samplePoints[serie][sample][0] - this.depth3dPoint.x, this.samplePoints[serie][sample][1]);
/*  281 */     return point;
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
/*      */   public void setAutoLabelSpacingOn(boolean on)
/*      */   {
/*  295 */     if (this.autoLabelSpacingOn != on) {
/*  296 */       this.autoLabelSpacingOn = on;
/*  297 */       this.needRender = true;
/*  298 */       autoRepaint();
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public boolean isAutoLabelSpacingOn()
/*      */   {
/*  309 */     return this.autoLabelSpacingOn;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void setStackedOn(boolean on)
/*      */   {
/*  319 */     if (this.stackedOn != on) {
/*  320 */       this.stackedOn = on;
/*  321 */       this.needRender = true;
/*  322 */       this.needChartCalculation = true;
/*  323 */       autoRepaint();
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public boolean isStackedOn()
/*      */   {
/*  334 */     return this.stackedOn;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   /**
/*      */    * @deprecated
/*      */    */
/*      */   public void setLineWidth(int width)
/*      */   {
/*  344 */     setLineWidth(-1, width);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void setLineWidth(int series, int width)
/*      */   {
/*  354 */     if ((series >= 0) && (series < this.lineWidth.length)) {
/*  355 */       this.lineWidth[series] = width;
/*  356 */     } else if (series < 0) {
/*  357 */       for (int i = 0; i < this.lineWidth.length; i++) {
/*  358 */         this.lineWidth[i] = width;
/*      */       }
/*      */     }
/*  361 */     this.needRender = true;
/*  362 */     autoRepaint();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   /**
/*      */    * @deprecated
/*      */    */
/*      */   public int getLineWidth()
/*      */   {
/*  372 */     return getLineWidth(0);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public int getLineWidth(int series)
/*      */   {
/*  382 */     if ((series >= 0) && (series < this.lineWidth.length)) {
/*  383 */       return this.lineWidth[series];
/*      */     }
/*  385 */     return this.lineWidth[0];
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
/*      */   public void setLineStroke(int series, int[] dash)
/*      */   {
/*  402 */     int count = dash.length % 2 == 0 ? dash.length : dash.length + 1;
/*  403 */     this.lineStroke[series] = new int[count];
/*      */     
/*      */ 
/*  406 */     if ((series >= 0) && (series < this.lineStroke.length)) {
/*  407 */       for (int i = 0; i < dash.length; i++) {
/*  408 */         this.lineStroke[series][i] = Math.abs(dash[i]);
/*      */       }
/*  410 */       if (this.lineStroke[series][(count - 1)] == 0) {
/*  411 */         this.lineStroke[series][(count - 1)] = this.lineStroke[series][(count - 2)];
/*      */       }
/*      */       
/*      */ 
/*      */     }
/*  416 */     else if (series < 0) {
/*  417 */       for (int j = 0; j < this.lineStroke.length; j++) {
/*  418 */         for (int i = 0; i < dash.length; i++) {
/*  419 */           this.lineStroke[j][i] = Math.abs(dash[i]);
/*      */         }
/*  421 */         if (this.lineStroke[j][(count - 1)] == 0) {
/*  422 */           this.lineStroke[j][(count - 1)] = this.lineStroke[j][(count - 2)];
/*      */         }
/*      */       }
/*      */     }
/*  426 */     this.needRender = true;
/*  427 */     autoRepaint();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public int[] getLineStroke(int series)
/*      */   {
/*  437 */     if ((series >= 0) && (series < this.lineStroke.length)) {
/*  438 */       return this.lineStroke[series];
/*      */     }
/*  440 */     return this.lineStroke[0];
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void setSampleHighlightOn(boolean on)
/*      */   {
/*  449 */     for (int series = 0; series < this.sampleHighlightOn.length; series++) {
/*  450 */       this.sampleHighlightOn_all[series] = on;
/*  451 */       for (int sample = 0; sample < this.sampleHighlightOn[series].length; sample++) {
/*  452 */         this.sampleHighlightOn[series][sample] = on;
/*      */       }
/*      */     }
/*  455 */     this.needRender = true;
/*  456 */     autoRepaint();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void setSampleHighlightOn(int series, boolean on)
/*      */   {
/*      */     try
/*      */     {
/*  468 */       this.sampleHighlightOn_all[series] = on;
/*  469 */       for (int sample = 0; sample < this.sampleHighlightOn[series].length; sample++) {
/*  470 */         this.sampleHighlightOn[series][sample] = on;
/*      */       }
/*      */     } catch (IndexOutOfBoundsException e) {
/*  473 */       throw new IllegalArgumentException("Invalid series: " + series);
/*      */     }
/*  475 */     this.needRender = true;
/*  476 */     autoRepaint();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void setSampleHighlightOn(int series, int sample, boolean on)
/*      */   {
/*      */     try
/*      */     {
/*  489 */       this.sampleHighlightOn[series][sample] = on;
/*      */     } catch (IndexOutOfBoundsException e) {
/*  491 */       throw new IllegalArgumentException("Invalid series or index: " + series + ", " + sample);
/*      */     }
/*  493 */     this.needRender = true;
/*  494 */     autoRepaint();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public boolean isSampleHighlightOn(int series)
/*      */   {
/*      */     try
/*      */     {
/*  506 */       return this.sampleHighlightOn_all[series];
/*      */     } catch (IndexOutOfBoundsException e) {
/*  508 */       throw new IllegalArgumentException("Invalid serie: , " + series);
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public boolean isSampleHighlightOn(int series, int sample)
/*      */   {
/*      */     try
/*      */     {
/*  522 */       return this.sampleHighlightOn[series][sample];
/*      */     } catch (IndexOutOfBoundsException e) {
/*  524 */       throw new IllegalArgumentException("Invalid series or index: " + series + ", " + sample);
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
/*      */   public void setSampleHighlightStyle(int style, int size)
/*      */   {
/*  541 */     for (int i = 0; i < this.sampleHighlightStyle.length; i++) {
/*  542 */       setSampleHighlightStyle(i, style, size);
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
/*      */   public void setSampleHighlightStyle(int serie, int style, int size)
/*      */   {
/*  559 */     this.sampleHighlightSize[serie] = Math.max(0, size);
/*  560 */     this.sampleHighlightStyle[serie] = style;
/*  561 */     this.needRender = true;
/*  562 */     autoRepaint();
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
/*      */   public int getSampleHighlightStyle(int series)
/*      */   {
/*      */     try
/*      */     {
/*  578 */       return this.sampleHighlightStyle[series];
/*      */     } catch (IndexOutOfBoundsException e) {
/*  580 */       throw new IllegalArgumentException("Invalid series: " + series);
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public int getSampleHighlightSize(int series)
/*      */   {
/*      */     try
/*      */     {
/*  593 */       return this.sampleHighlightSize[series];
/*      */     } catch (IndexOutOfBoundsException e) {
/*  595 */       throw new IllegalArgumentException("Invalid series: " + series);
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
/*      */   public void setSampleHighlightImage(int series, int sample, String name)
/*      */   {
/*  610 */     if ((series >= -1) && (series < 1000) && (sample >= -1) && (series <= 1000))
/*      */     {
/*  612 */       if ((series == -1) && (sample == -1)) {
/*  613 */         this.highlightImage = name;
/*      */ 
/*      */       }
/*  616 */       else if (sample == -1) {
/*  617 */         int series_count = Math.max(this.highlightImages.length, series + 1);
/*      */         
/*  619 */         if (series >= this.highlightImages_all.length) {
/*  620 */           String[] newHighlightImages_all = new String[series_count];
/*  621 */           for (int i = 0; i < this.highlightImages_all.length; i++) {
/*  622 */             newHighlightImages_all[i] = this.highlightImages_all[i];
/*      */           }
/*  624 */           this.highlightImages_all = newHighlightImages_all;
/*      */         }
/*      */         
/*  627 */         this.highlightImages_all[series] = name;
/*      */       }
/*      */       else
/*      */       {
/*  631 */         int series_count = Math.max(this.highlightImages.length, series + 1);
/*  632 */         int sample_count = Math.max(this.highlightImages[0].length, sample + 1);
/*      */         
/*  634 */         if ((series >= this.highlightImages.length) || (sample >= this.highlightImages[0].length)) {
/*  635 */           String[][] newHighlightImages = new String[series_count][sample_count];
/*  636 */           for (int i = 0; i < this.highlightImages.length; i++) {
/*  637 */             for (int j = 0; j < this.highlightImages[0].length; j++) {
/*  638 */               newHighlightImages[i][j] = this.highlightImages[i][j];
/*      */             }
/*      */           }
/*  641 */           this.highlightImages = newHighlightImages;
/*      */         }
/*      */         
/*  644 */         this.highlightImages[series][sample] = name;
/*      */       }
/*  646 */       this.needRender = true;
/*  647 */       autoRepaint();
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
/*      */   public String getSampleHighlightImage(int series, int sample)
/*      */   {
/*  661 */     if ((series == -1) && (sample == -1))
/*  662 */       return this.highlightImage;
/*  663 */     if ((series >= 0) && (series < this.highlightImages_all.length) && (sample == -1))
/*  664 */       return this.highlightImages_all[series];
/*  665 */     if ((series >= 0) && (series < this.highlightImages.length) && (sample >= 0) && (sample < this.highlightImages[0].length)) {
/*  666 */       return this.highlightImages[series][sample];
/*      */     }
/*  668 */     return null;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void setSeriesLineOn(boolean state)
/*      */   {
/*  680 */     for (int i = 0; i < this.seriesLinesOn.length; i++) {
/*  681 */       this.seriesLinesOn[i] = state;
/*      */     }
/*  683 */     this.needRender = true;
/*  684 */     autoRepaint();
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
/*      */   public void setSeriesLineOn(int serie, boolean state)
/*      */   {
/*  697 */     if (this.seriesLinesOn[serie] != state) {
/*  698 */       this.seriesLinesOn[serie] = state;
/*  699 */       this.needRender = true;
/*  700 */       autoRepaint();
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
/*      */   public boolean isSeriesLineOn(int serie)
/*      */   {
/*  713 */     return this.seriesLinesOn[serie];
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void setConnectedLinesOn(int serie, boolean on)
/*      */   {
/*  725 */     if ((serie >= 0) && (serie < this.connectedLinesOn.length)) {
/*  726 */       this.connectedLinesOn[serie] = on;
/*  727 */     } else if (serie == -1) {
/*  728 */       for (int i = 0; i < this.connectedLinesOn.length; i++) {
/*  729 */         this.connectedLinesOn[i] = on;
/*      */       }
/*      */     }
/*  732 */     this.needRender = true;
/*  733 */     autoRepaint();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public boolean isConnectedLinesOn(int serie)
/*      */   {
/*  744 */     if ((serie >= 0) && (serie < this.connectedLinesOn.length))
/*  745 */       return this.connectedLinesOn[serie];
/*  746 */     if (serie == -1) {
/*  747 */       boolean on = false;
/*  748 */       for (int i = 0; i < this.connectedLinesOn.length; i++) {
/*  749 */         if (this.connectedLinesOn[i] != 0) {
/*  750 */           on = true;
/*      */         }
/*      */       }
/*  753 */       return on;
/*      */     }
/*  755 */     return false;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void setRightToLeftScrollingOn(boolean state)
/*      */   {
/*  767 */     this.rightToLeftScrollingOn = state;
/*  768 */     setVisibleSamples(this.visibleSamples[0], this.visibleSamples[1]);
/*  769 */     this.needChartCalculation = true;
/*  770 */     this.needRender = true;
/*  771 */     autoRepaint();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public boolean isRightToLeftScrollingOn()
/*      */   {
/*  781 */     return this.rightToLeftScrollingOn;
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
/*      */   public ChartSample checkSelection(Point point)
/*      */   {
/*  797 */     if (point == null) {
/*  798 */       return null;
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*  803 */     ChartSample foundSample = null;
/*  804 */     if (this.overlayCharts != null) {
/*  805 */       int overlay_count = this.overlayCharts.size();
/*  806 */       for (int i = overlay_count - 1; (i >= 0) && (foundSample == null); i--) {
/*  807 */         Chart overlay = (Chart)this.overlayCharts.elementAt(i);
/*  808 */         if (overlay != null) {
/*  809 */           foundSample = overlay.checkSelection(point);
/*      */         }
/*      */       }
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*  816 */     if (foundSample != null) {
/*  817 */       this.mouseOverSampleIndex = -1;
/*  818 */       this.mouseOverSeriesIndex = -1;
/*  819 */       return foundSample;
/*      */     }
/*      */     
/*      */ 
/*  823 */     if ((isLegendOn()) && (this.legendBounds != null) && (this.visibleLegend.contains(point))) {
/*  824 */       for (int i = 0; i < this.legendBounds.length; i++) {
/*  825 */         if ((this.legendBounds[i] != null) && (this.legendBounds[i].contains(point))) {
/*  826 */           foundSample = new ChartSample(-1);
/*  827 */           foundSample.setSeries(i);
/*  828 */           break;
/*      */         }
/*      */       }
/*      */       
/*      */ 
/*      */ 
/*  834 */       if ((foundSample != null) && (this.overlayCharts != null)) {
/*  835 */         int overlay_count = this.overlayCharts.size();
/*  836 */         for (int i = overlay_count - 1; i >= 0; i--) {
/*  837 */           Chart overlay = (Chart)this.overlayCharts.elementAt(i);
/*  838 */           if (overlay != null) {
/*  839 */             overlay.mouseOverSampleIndex = foundSample.getIndex();
/*  840 */             overlay.mouseOverSeriesIndex = foundSample.getSeries();
/*  841 */             if (this.gridAlignment == 1) {
/*  842 */               overlay.mousePosition.x = this.mousePosition.x;
/*  843 */               overlay.mousePosition.y = -1;
/*      */             } else {
/*  845 */               overlay.mousePosition.x = -1;
/*  846 */               overlay.mousePosition.y = this.mousePosition.y;
/*      */             }
/*      */           }
/*      */         }
/*      */       }
/*      */     }
/*      */     
/*      */ 
/*  854 */     if ((foundSample == null) && (this.samplePoints != null)) {
/*  855 */       int off = 3;
/*  856 */       for (int serie = 0; (foundSample == null) && (serie < this.samplePoints.length); serie++) {
/*  857 */         for (int sample = 0; (foundSample == null) && (sample < this.samplePoints[serie].length); sample++) {
/*  858 */           Point pos = new Point(this.samplePoints[serie][sample][0], this.samplePoints[serie][sample][1]);
/*      */           
/*  860 */           if (is3DModeOn()) {
/*  861 */             pos.x -= this.depth3dPoint.x;
/*  862 */             if (!this.stackedOn) {
/*  863 */               double depth_x = this.depth3dPoint.x / getSeriesCount();
/*  864 */               int change = (int)Math.round(depth_x * serie);
/*  865 */               pos.x += change;
/*      */             }
/*      */           }
/*  868 */           boolean inside = (point.x >= pos.x - off) && (point.x <= pos.x + off);
/*  869 */           inside &= ((point.y >= pos.y - off) && (point.y <= pos.y + off));
/*  870 */           if (inside) {
/*  871 */             foundSample = getSample(serie, sample);
/*      */           }
/*      */         }
/*      */       }
/*      */     }
/*      */     
/*  877 */     if (foundSample != null) {
/*  878 */       this.mouseOverSampleIndex = foundSample.getIndex();
/*  879 */       this.mouseOverSeriesIndex = foundSample.getSeries();
/*  880 */       this.mousePosition = point;
/*      */     } else {
/*  882 */       this.mouseOverSampleIndex = -1;
/*  883 */       this.mouseOverSeriesIndex = -1;
/*      */     }
/*      */     
/*  886 */     return foundSample;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void render(Graphics g)
/*      */   {
/*  896 */     render(g, !isServletModeOn());
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   void render(Graphics g, boolean offscreenOn)
/*      */   {
/*  907 */     Dimension size = getSize();
/*  908 */     this.graphBounds = getGraphBounds();
/*  909 */     Rectangle grid = this.graphBounds;
/*  910 */     Rectangle dataBounds = getDataBounds(grid);
/*  911 */     calculateChartData(grid, dataBounds);
/*  912 */     this.lastCalculationTime = System.currentTimeMillis();
/*  913 */     for (int i = 0; i < this.overlayCharts.size(); i++) {
/*  914 */       Chart overlay = (Chart)this.overlayCharts.elementAt(i);
/*  915 */       if (overlay != null)
/*      */       {
/*      */ 
/*  918 */         Rectangle overlayGrid = new Rectangle(grid);
/*  919 */         Rectangle overlayDataBounds = new Rectangle(dataBounds);
/*  920 */         if (is3DModeOn()) {
/*  921 */           overlayGrid.x -= this.depth3dPoint.x;
/*  922 */           overlayGrid.y -= this.depth3dPoint.y;
/*  923 */           overlayDataBounds.x -= this.depth3dPoint.x;
/*  924 */           overlayDataBounds.y -= this.depth3dPoint.y;
/*      */         }
/*  926 */         overlay.calculateChartData(overlayGrid, overlayDataBounds);
/*      */       }
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*  932 */     if ((offscreenOn) && ((this.offscreen == null) || (this.offscreen.getWidth(this) < size.width) || (this.offscreen.getHeight(this) < size.height))) {
/*      */       try {
/*  934 */         this.offscreen = createImage(Math.max(1, size.width), Math.max(1, size.height));
/*      */       } catch (Throwable e) {
/*  936 */         Chart.fifo_clear();
/*      */       }
/*      */       
/*      */ 
/*  940 */       this.needRender = true;
/*      */     }
/*      */     
/*      */ 
/*  944 */     if ((!offscreenOn) || (this.needRender) || (this.offscreen == null))
/*      */     {
/*  946 */       Graphics gc = g;
/*  947 */       if ((this.offscreen != null) && (offscreenOn) && (!this.externalGraphicsOn)) {
/*  948 */         gc = this.offscreen.getGraphics();
/*  949 */       } else if ((this.externalGraphicsOn) && (this.external_gc != null) && (this.offscreen != null)) {
/*  950 */         gc = this.external_gc;
/*      */       }
/*  952 */       gc.setColor(getBackground());
/*  953 */       gc.fillRect(0, 0, size.width, size.height);
/*      */       
/*      */ 
/*  956 */       paintGrid(gc, grid);
/*  957 */       paintBelowSampleLabels(gc, grid, dataBounds);
/*  958 */       paintTitle(gc, size);
/*  959 */       if (isLegendOn()) {
/*  960 */         paintLegend(gc, grid, getLegendLabels());
/*      */       }
/*      */       
/*  963 */       renderData(gc, grid, dataBounds);
/*      */       
/*      */ 
/*  966 */       for (int i = 0; i < this.overlayCharts.size(); i++) {
/*  967 */         Chart overlay = (Chart)this.overlayCharts.elementAt(i);
/*  968 */         if ((overlay != null) && (isOverlayChartOn(i))) {
/*  969 */           Rectangle overlayGrid = new Rectangle(grid);
/*  970 */           Rectangle overlayDataBounds = new Rectangle(dataBounds);
/*  971 */           overlayGrid.x -= this.depth3dPoint.x;
/*  972 */           overlayGrid.y -= this.depth3dPoint.y;
/*  973 */           overlayDataBounds.x -= this.depth3dPoint.x;
/*  974 */           overlayDataBounds.y -= this.depth3dPoint.y;
/*  975 */           if (((overlay instanceof LineChart)) || ((overlay instanceof PieChart))) {
/*  976 */             overlay.renderData(gc, overlayGrid, overlayDataBounds);
/*      */           } else {
/*  978 */             overlay.renderData(gc, grid, dataBounds);
/*      */           }
/*      */         }
/*      */       }
/*  982 */       if (is3DModeOn()) {
/*  983 */         for (int i = this.rangeOn.length - 1; i >= 0; i--) {
/*  984 */           paint3DZeroDivider(gc, grid, i);
/*      */         }
/*      */       }
/*      */       
/*  988 */       if ((!this.externalGraphicsOn) && (gc != g)) {
/*  989 */         gc.dispose();
/*      */       }
/*      */       
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  999 */       if (!this.chartData.hasChangedSince(this.lastCalculationTime)) {
/* 1000 */         this.needRender = false;
/*      */       }
/*      */     }
/*      */     
/*      */ 
/* 1005 */     if (((offscreenOn) || (this.externalGraphicsOn)) && (this.offscreen != null)) {
/* 1006 */       g.drawImage(this.offscreen, 0, 0, this);
/*      */     }
/*      */     
/*      */ 
/* 1010 */     if (isZoomOn()) {
/* 1011 */       paintZoomOutButton(g, grid);
/* 1012 */       paintMouseBox(g);
/*      */     }
/*      */     
/*      */ 
/*      */ 
/* 1017 */     paintFloatingLabels(g, grid);
/*      */     
/*      */ 
/* 1020 */     paintLabels(g);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected void calculateChartData(Rectangle grid, Rectangle dataBounds)
/*      */   {
/* 1031 */     if ((!this.needChartCalculation) && (!isServletModeOn())) {
/* 1032 */       return;
/*      */     }
/*      */     
/*      */ 
/* 1036 */     int sampleCount = getSampleCount();
/* 1037 */     int seriesCount = getSeriesCount();
/*      */     
/* 1039 */     calculateZeroLines(grid);
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 1045 */     boolean display3dOn = is3DModeOn();
/* 1046 */     if (display3dOn)
/*      */     {
/* 1048 */       if (this.depth3d > -1) {
/* 1049 */         this.depth3dPoint.x = this.depth3d;
/* 1050 */         this.depth3dPoint.y = (-this.depth3d);
/*      */       }
/*      */       else
/*      */       {
/* 1054 */         int sample_count = Math.max(sampleCount, 20);
/* 1055 */         if ((seriesCount > 1) && (!this.stackedOn)) {
/* 1056 */           sample_count /= seriesCount;
/* 1057 */           sample_count = Math.max(sample_count, 4);
/*      */         }
/* 1059 */         double sampleWidth = grid.width / sample_count;
/* 1060 */         this.depth3dPoint.x = ((int)Math.round(sampleWidth * 1.25D));
/* 1061 */         this.depth3dPoint.y = ((int)Math.round(-sampleWidth / 1.25D));
/*      */       }
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 1069 */     if ((this.samplePoints == null) || (this.samplePoints.length != seriesCount) || (this.samplePoints.length == 0) || (this.samplePoints[0] == null) || (this.samplePoints[0].length != sampleCount)) {
/* 1070 */       this.samplePoints = new int[seriesCount][sampleCount][2];
/*      */     }
/*      */     
/*      */ 
/*      */ 
/* 1075 */     int[] last_index = new int[seriesCount];
/* 1076 */     if (this.rightToLeftScrollingOn)
/*      */     {
/* 1078 */       for (int serie = 0; serie < last_index.length; serie++) {
/* 1079 */         ChartSample[] samples = null;
/*      */         try {
/* 1081 */           samples = getSamples(serie);
/*      */         }
/*      */         catch (IllegalArgumentException localIllegalArgumentException) {}
/*      */         
/* 1085 */         if (samples != null) {
/* 1086 */           for (int sample = samples.length - 1; sample >= 0; sample--) {
/* 1087 */             if ((samples[sample] != null) && (samples[sample].hasValue())) {
/* 1088 */               last_index[serie] = Math.max(last_index[serie], sample);
/* 1089 */               break;
/*      */             }
/*      */           }
/*      */         }
/*      */       }
/*      */     }
/*      */     
/*      */ 
/* 1097 */     double delta_x = dataBounds.width;
/* 1098 */     double depth_x = this.depth3dPoint.x;
/* 1099 */     if (sampleCount > 2) {
/* 1100 */       delta_x = dataBounds.width / (sampleCount - 1);
/*      */     }
/*      */     
/*      */ 
/* 1104 */     if ((this.sampleLabelPos == null) || (this.sampleLabelPos.length < sampleCount)) {
/* 1105 */       int[] newSampleLabelPos = new int[sampleCount];
/* 1106 */       if (this.sampleLabelPos != null) {
/* 1107 */         System.arraycopy(this.sampleLabelPos, 0, newSampleLabelPos, 0, Math.min(this.sampleLabelPos.length, newSampleLabelPos.length));
/*      */       }
/* 1109 */       this.sampleLabelPos = newSampleLabelPos;
/*      */     }
/*      */     
/* 1112 */     double sample_xpos = dataBounds.x;
/* 1113 */     if (this.rightToLeftScrollingOn)
/*      */     {
/* 1115 */       int last_label_index = 0;
/* 1116 */       String[] labels = getSampleLabels();
/* 1117 */       if (labels != null) {
/* 1118 */         for (int i = labels.length - 1; i >= 0; i--) {
/* 1119 */           if (labels[i] != null) {
/* 1120 */             last_label_index = Math.max(last_label_index, i);
/* 1121 */             break;
/*      */           }
/*      */         }
/*      */       }
/* 1125 */       sample_xpos = sample_xpos + dataBounds.width - delta_x * last_label_index;
/*      */     }
/*      */     
/* 1128 */     for (int i = 0; (i < sampleCount) && (i < this.sampleLabelPos.length); i++)
/*      */     {
/* 1130 */       int xpos = 0;
/* 1131 */       if (sample_xpos >= 0.0D) {
/* 1132 */         xpos = (int)(sample_xpos + 0.5D);
/*      */       } else {
/* 1134 */         xpos = (int)(sample_xpos - 0.5D);
/*      */       }
/* 1136 */       this.sampleLabelPos[i] = xpos;
/* 1137 */       sample_xpos += delta_x;
/*      */     }
/*      */     
/*      */ 
/* 1141 */     for (int serie = 0; (serie < seriesCount) && (this.samplePoints != null) && (serie < this.samplePoints.length); serie++)
/*      */     {
/*      */ 
/* 1144 */       sample_xpos = dataBounds.x;
/* 1145 */       if (this.rightToLeftScrollingOn) {
/* 1146 */         sample_xpos = sample_xpos + dataBounds.width - delta_x * last_index[serie];
/*      */       }
/*      */       
/*      */ 
/* 1150 */       double last_value = NaN.0D;
/* 1151 */       for (int sample = 0; (sample < sampleCount) && (this.samplePoints[serie] != null) && (sample < this.samplePoints[serie].length); sample++)
/*      */       {
/* 1153 */         int xpos = 0;
/* 1154 */         if (sample_xpos >= 0.0D) {
/* 1155 */           xpos = (int)(sample_xpos + 0.5D);
/*      */         } else {
/* 1157 */           xpos = (int)(sample_xpos - 0.5D);
/*      */         }
/*      */         
/*      */         try
/*      */         {
/* 1162 */           this.samplePoints[serie][sample][0] = xpos;
/*      */         }
/*      */         catch (Exception localException1) {}
/*      */         
/*      */ 
/*      */ 
/* 1168 */         if ((display3dOn) && 
/* 1169 */           (!this.stackedOn)) {
/* 1170 */           depth_x = this.depth3dPoint.x / seriesCount * serie;
/*      */           try {
/* 1172 */             int tmp708_707 = 0; int[] tmp708_706 = this.samplePoints[serie][sample];tmp708_706[tmp708_707] = ((int)(tmp708_706[tmp708_707] + depth_x));
/*      */           }
/*      */           catch (Exception localException2) {}
/*      */         }
/*      */         
/*      */ 
/*      */ 
/*      */ 
/* 1180 */         double value = NaN.0D;
/*      */         try
/*      */         {
/* 1183 */           value = getSampleValue(serie, sample);
/* 1184 */           if (!new Double(value).isNaN()) {
/* 1185 */             last_value = value;
/*      */ 
/*      */ 
/*      */ 
/*      */           }
/* 1190 */           else if (this.stackedOn)
/*      */           {
/*      */ 
/* 1193 */             for (int cur = sample + 1; cur < sampleCount; cur++) {
/* 1194 */               ChartSample cur_s = getSample(serie, cur);
/* 1195 */               if ((cur_s != null) && (cur_s.hasValue()))
/*      */               {
/* 1197 */                 if (new Double(last_value).isNaN()) {
/* 1198 */                   value = cur_s.getValue();
/* 1199 */                   break;
/*      */                 }
/*      */                 
/* 1202 */                 double delta = (cur_s.getValue() - last_value) / (cur - sample);
/* 1203 */                 value = last_value + delta;
/* 1204 */                 last_value = value;
/* 1205 */                 if (this.connectedLinesOn[serie] == 0) break;
/* 1206 */                 setSampleValue(serie, sample, value);
/*      */                 
/*      */ 
/* 1209 */                 break;
/*      */               }
/*      */             }
/*      */             
/*      */ 
/* 1214 */             if (new Double(value).isNaN()) {
/* 1215 */               value = last_value;
/*      */             }
/*      */           }
/*      */         }
/*      */         catch (IllegalArgumentException localIllegalArgumentException1) {}
/*      */         
/*      */ 
/*      */ 
/* 1223 */         int rangeIndex = getSeriesRange(serie);
/* 1224 */         double factor = 0.0D;
/*      */         try {
/* 1226 */           factor = (value - this.currentLowerRange[rangeIndex]) / (this.currentUpperRange[rangeIndex] - this.currentLowerRange[rangeIndex]);
/*      */         }
/*      */         catch (Exception e) {
/* 1229 */           factor = NaN.0D;
/*      */         }
/*      */         
/*      */ 
/*      */ 
/* 1234 */         double y_value = grid.y + grid.height - factor * grid.height;
/* 1235 */         if ((serie > 0) && (this.stackedOn)) {
/*      */           try {
/* 1237 */             factor = value / (this.currentUpperRange[rangeIndex] - this.currentLowerRange[rangeIndex]);
/* 1238 */             y_value = this.samplePoints[(serie - 1)][sample][1] - grid.height * factor;
/*      */           }
/*      */           catch (Exception localException3) {}
/*      */         }
/*      */         
/*      */         try
/*      */         {
/* 1245 */           if (y_value >= 0.0D) {
/* 1246 */             this.samplePoints[serie][sample][1] = ((int)(y_value + 0.5D));
/*      */           } else {
/* 1248 */             this.samplePoints[serie][sample][1] = ((int)(y_value - 0.5D));
/*      */           }
/*      */         }
/*      */         catch (Exception localException4) {}
/*      */         
/*      */ 
/*      */         try
/*      */         {
/* 1256 */           if ((display3dOn) && (this.stackedOn)) {
/* 1257 */             if (serie == 0) {
/* 1258 */               this.samplePoints[serie][sample][1] -= this.depth3dPoint.y;
/*      */             }
/* 1260 */           } else if (display3dOn) {
/* 1261 */             this.samplePoints[serie][sample][1] -= this.depth3dPoint.y;
/* 1262 */             double depth_y = this.depth3dPoint.y / getSeriesCount() * serie;
/* 1263 */             this.samplePoints[serie][sample][1] += (int)Math.round(depth_y);
/*      */           }
/*      */         }
/*      */         catch (Exception localException5) {}
/*      */         
/* 1268 */         sample_xpos += delta_x;
/*      */       }
/*      */     }
/*      */     
/* 1272 */     this.needChartCalculation = false;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected void renderData(Graphics g, Rectangle grid, Rectangle dataBounds)
/*      */   {
/* 1283 */     paintLines(g, grid, dataBounds);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected void checkDataIntegrity()
/*      */   {
/* 1292 */     super.checkDataIntegrity();
/*      */     
/*      */ 
/* 1295 */     int seriesCount = getSeriesCount();
/* 1296 */     int sampleCount = getSampleCount();
/* 1297 */     int oldSeriesCount = this.sampleHighlightOn != null ? this.sampleHighlightOn.length : 1;
/* 1298 */     int oldSampleCount = (oldSeriesCount > 0) && (this.sampleHighlightOn != null) ? this.sampleHighlightOn[0].length : 0;
/*      */     
/*      */ 
/* 1301 */     if ((this.samplePoints == null) || (this.samplePoints.length != seriesCount) || (this.samplePoints.length == 0) || (this.samplePoints[0].length != sampleCount)) {
/* 1302 */       this.samplePoints = new int[seriesCount][sampleCount][2];
/* 1303 */       Rectangle grid = getGraphBounds();
/* 1304 */       Rectangle dataBounds = getDataBounds(grid);
/* 1305 */       this.needChartCalculation = true;
/* 1306 */       calculateChartData(grid, dataBounds);
/*      */     }
/*      */     
/*      */ 
/* 1310 */     if ((seriesCount != oldSeriesCount) || (sampleCount != oldSampleCount))
/*      */     {
/* 1312 */       boolean[][] newSampleHighlightOn = new boolean[seriesCount][sampleCount];
/* 1313 */       boolean[] newSampleHighlightOn_all = new boolean[seriesCount];
/* 1314 */       for (int serie = 0; serie < seriesCount; serie++) {
/* 1315 */         if ((serie < oldSeriesCount) && (this.sampleHighlightOn_all != null)) {
/*      */           try {
/* 1317 */             newSampleHighlightOn_all[serie] = this.sampleHighlightOn_all[serie];
/*      */           }
/*      */           catch (Exception localException) {}
/*      */           
/* 1321 */           for (int sample = 0; sample < sampleCount; sample++) {
/*      */             try {
/* 1323 */               if (sample < oldSampleCount) {
/* 1324 */                 newSampleHighlightOn[serie][sample] = this.sampleHighlightOn[serie][sample];
/*      */               } else {
/* 1326 */                 newSampleHighlightOn[serie][sample] = this.sampleHighlightOn_all[serie];
/*      */               }
/*      */             }
/*      */             catch (Exception localException1) {}
/*      */           }
/*      */         }
/*      */       }
/*      */       
/* 1334 */       this.sampleHighlightOn = newSampleHighlightOn;
/* 1335 */       this.sampleHighlightOn_all = newSampleHighlightOn_all;
/*      */       
/*      */ 
/* 1338 */       int[] newSampleHighlightStyle = new int[seriesCount];
/* 1339 */       int[] newSampleHighlightSize = new int[seriesCount];
/* 1340 */       boolean[] newSeriesLinesOn = new boolean[seriesCount];
/* 1341 */       boolean[] newConnectedLinesOn = new boolean[seriesCount];
/* 1342 */       int[] newLineWidth = new int[seriesCount];
/* 1343 */       int[][] newLineStroke = new int[seriesCount][];
/* 1344 */       for (int serie = 0; serie < seriesCount; serie++) {
/* 1345 */         if ((serie < oldSeriesCount) && (this.sampleHighlightStyle != null) && 
/* 1346 */           (this.sampleHighlightSize != null) && (this.seriesLinesOn != null) && 
/* 1347 */           (this.connectedLinesOn != null) && (this.lineWidth != null) && (this.lineStroke != null)) {
/* 1348 */           newSampleHighlightStyle[serie] = this.sampleHighlightStyle[serie];
/* 1349 */           newSampleHighlightSize[serie] = this.sampleHighlightSize[serie];
/* 1350 */           newSeriesLinesOn[serie] = this.seriesLinesOn[serie];
/* 1351 */           newConnectedLinesOn[serie] = this.connectedLinesOn[serie];
/* 1352 */           newLineWidth[serie] = this.lineWidth[serie];
/* 1353 */           newLineStroke[serie] = this.lineStroke[serie];
/*      */         } else {
/* 1355 */           newSampleHighlightSize[serie] = 6;
/* 1356 */           newSeriesLinesOn[serie] = true;
/* 1357 */           newLineWidth[serie] = 2;
/*      */         }
/*      */       }
/* 1360 */       this.sampleHighlightStyle = newSampleHighlightStyle;
/* 1361 */       this.sampleHighlightSize = newSampleHighlightSize;
/* 1362 */       this.seriesLinesOn = newSeriesLinesOn;
/* 1363 */       this.connectedLinesOn = newConnectedLinesOn;
/* 1364 */       this.lineWidth = newLineWidth;
/* 1365 */       this.lineStroke = newLineStroke;
/* 1366 */       this.needChartCalculation = true;
/*      */     }
/*      */     
/*      */ 
/* 1370 */     if (this.lastSelectedLine >= seriesCount) {
/* 1371 */       this.lastSelectedLine = -1;
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
/*      */   protected Point getSampleCenter(int sample, int serie)
/*      */   {
/* 1384 */     Point centerPoint = getSamplePoint(serie, sample);
/*      */     
/*      */ 
/* 1387 */     Rectangle bounds = new Rectangle(this.graphBounds);
/* 1388 */     bounds.grow(1, 1);
/* 1389 */     if (is3DModeOn()) {
/* 1390 */       bounds.x -= this.depth3dPoint.x;
/* 1391 */       bounds.width += this.depth3dPoint.x;
/* 1392 */       bounds.height -= this.depth3dPoint.y;
/*      */     }
/*      */     
/*      */ 
/* 1396 */     if (bounds.contains(centerPoint)) {
/* 1397 */       return centerPoint;
/*      */     }
/* 1399 */     return null;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public Rectangle getGraphBounds()
/*      */   {
/* 1409 */     if ((!this.needGraphBounds) && (this.currentBounds != null)) {
/* 1410 */       return this.currentBounds;
/*      */     }
/*      */     
/*      */ 
/* 1414 */     Rectangle bounds = super.getGraphBounds(getLegendLabels());
/*      */     
/*      */ 
/* 1417 */     Dimension size = getSize();
/* 1418 */     int left = bounds.x;
/* 1419 */     int right = size.width - bounds.width - bounds.x;
/* 1420 */     int top = bounds.y;
/* 1421 */     int bottom = size.height - bounds.height - bounds.y;
/*      */     
/*      */ 
/* 1424 */     int bottom_offset = 0;
/* 1425 */     if ((this.sampleLabelsOn) && (getSampleCount() > 0) && ((this.sampleLabelStyle == 2) || (this.sampleLabelStyle == 4)))
/*      */     {
/* 1427 */       Font sampleLabelFont = getFont("sampleLabelFont");
/* 1428 */       FontMetrics fm = getFontMetrics(sampleLabelFont);
/*      */       
/*      */ 
/* 1431 */       int height = 0;
/* 1432 */       int angle = getLabelAngle("sampleLabelAngle");
/* 1433 */       String[] labels = getSampleLabels();
/* 1434 */       for (int i = 0; i < labels.length; i++) {
/* 1435 */         Dimension labelSize = getLabelSize(labels[i], fm);
/* 1436 */         Dimension angledSize = getAngledLabelSize(labelSize, angle);
/* 1437 */         height = Math.max(angledSize.height, height);
/*      */       }
/* 1439 */       bottom_offset = height + 6;
/* 1440 */       if (angle % 180 == 0) {
/* 1441 */         bottom_offset -= fm.getMaxDescent();
/*      */       }
/* 1443 */       bottom += bottom_offset;
/*      */       
/*      */ 
/* 1446 */       String label = null;
/*      */       try {
/* 1448 */         label = getSampleLabel(getSampleCount() - 1);
/*      */       }
/*      */       catch (IllegalArgumentException localIllegalArgumentException) {}
/*      */       
/* 1452 */       int label_width = 0;
/* 1453 */       if (label != null) {
/* 1454 */         Dimension labelSize = getLabelSize(label, fm);
/* 1455 */         Dimension angledSize = getAngledLabelSize(labelSize, angle);
/* 1456 */         label_width = angledSize.width / 2 - 5;
/*      */       }
/*      */       
/*      */ 
/*      */ 
/* 1461 */       int right_width = 0;
/* 1462 */       if ((isLegendOn()) && (getLegendPosition() == 1)) {
/* 1463 */         right_width = this.legend.width;
/*      */       }
/* 1465 */       if (label_width > right_width) {
/* 1466 */         right += label_width - right_width;
/*      */       }
/*      */     }
/*      */     
/*      */ 
/* 1471 */     if (this.sampleScrollerOn) {
/* 1472 */       bottom_offset += 10;
/* 1473 */       bottom += 10;
/*      */     }
/*      */     
/*      */ 
/* 1477 */     String sampleAxisLabel = getLabel("sampleAxisLabel");
/* 1478 */     if (sampleAxisLabel != null) {
/* 1479 */       Font sampleAxisLabelFont = getFont("sampleAxisLabelFont");
/* 1480 */       FontMetrics fm = getFontMetrics(sampleAxisLabelFont);
/* 1481 */       int angle = getLabelAngle("sampleAxisLabelAngle");
/* 1482 */       Dimension labelSize = getLabelSize(sampleAxisLabel, fm);
/* 1483 */       Dimension angledSize = getAngledLabelSize(labelSize, angle);
/* 1484 */       bottom += angledSize.height + 3;
/* 1485 */       if (angle % 180 == 0) {
/* 1486 */         bottom -= fm.getDescent();
/*      */       }
/*      */     }
/*      */     
/*      */ 
/* 1491 */     if (is3DModeOn()) {
/* 1492 */       if (this.depth3d > -1) {
/* 1493 */         left += this.depth3dPoint.x;
/* 1494 */         bottom -= this.depth3dPoint.y;
/*      */       } else {
/* 1496 */         int sampleCount = Math.max(20, getSampleCount());
/* 1497 */         int seriesCount = getSeriesCount();
/* 1498 */         if ((seriesCount > 1) && (!this.stackedOn)) {
/* 1499 */           sampleCount /= seriesCount;
/* 1500 */           sampleCount = Math.max(sampleCount, 4);
/*      */         }
/* 1502 */         int effect3d = (int)(bounds.width * 1.25F / (sampleCount + 1.25F));
/* 1503 */         left += effect3d;
/* 1504 */         bounds.width = (size.width - left - right);
/* 1505 */         double d = bounds.width / sampleCount;
/* 1506 */         bottom += (int)Math.round(d / 1.25D);
/*      */       }
/*      */     }
/*      */     
/*      */ 
/*      */ 
/* 1512 */     Insets insets = getGraphInsets();
/* 1513 */     boolean range_on = false;
/* 1514 */     int left_offset = 0;
/* 1515 */     int right_offset = 0;
/* 1516 */     for (int i = 0; i < this.rangeOn.length; i++) {
/* 1517 */       range_on = this.rangeOn[i] != 0 ? true : range_on;
/*      */     }
/* 1519 */     if ((range_on) || (this.targetsLabel.size() > 0))
/*      */     {
/* 1521 */       Rectangle currentGrid = new Rectangle();
/* 1522 */       int cur_top = insets.top == -1 ? top : insets.top;
/* 1523 */       int cur_left = insets.left == -1 ? left : insets.left;
/* 1524 */       int cur_bottom = insets.bottom == -1 ? bottom : insets.bottom;
/* 1525 */       int cur_right = insets.right == -1 ? right : insets.right;
/* 1526 */       currentGrid.x = cur_left;
/* 1527 */       currentGrid.width = (size.width - cur_left - cur_right - 1);
/* 1528 */       currentGrid.y = cur_top;
/* 1529 */       currentGrid.height = (size.height - cur_top - cur_bottom - 1);
/* 1530 */       calculateRangeBounds(currentGrid);
/*      */       
/*      */ 
/*      */ 
/* 1534 */       int left_inner_range = -1;
/* 1535 */       int right_inner_range = -1;
/* 1536 */       for (int i = this.rangeBounds.length - 1; i >= 0; i--) {
/* 1537 */         if (this.rangePosition[i] == 0) {
/* 1538 */           left_inner_range = i;
/* 1539 */           left_offset += this.rangeBounds[i].width;
/* 1540 */         } else if (this.rangePosition[i] == 1) {
/* 1541 */           right_inner_range = i;
/* 1542 */           right_offset += this.rangeBounds[i].width;
/*      */         }
/*      */       }
/*      */       
/*      */ 
/* 1547 */       if ((this.targetLabelsPosition == 0) && (left_inner_range == -1)) {
/* 1548 */         left_offset += getRangeWidth(-1, false, true);
/* 1549 */       } else if ((this.targetLabelsPosition == 1) && (right_inner_range == -1)) {
/* 1550 */         right_offset += getRangeWidth(-1, false, true);
/*      */       }
/*      */       
/*      */ 
/* 1554 */       if ((this.rangeOn.length == 1) && (this.rangeOn[0] != 0) && (this.rangeAdjusterOn[0] != 0)) {
/* 1555 */         if ((this.rangePosition[0] == 0) && (this.rangeAdjusterPosition == 1)) {
/* 1556 */           right_offset += 6;
/* 1557 */         } else if ((this.rangePosition[0] == 1) && (this.rangeAdjusterPosition == 0)) {
/* 1558 */           left_offset += 6;
/*      */         }
/*      */       }
/*      */     }
/*      */     
/*      */ 
/* 1564 */     if (insets.left != -1) {
/* 1565 */       for (int index = this.rangeBounds.length - 1; index >= 0; index--) {
/* 1566 */         this.rangeBounds[index].x -= (this.rangePosition[index] == 0 ? left_offset : 0);
/*      */       }
/*      */     }
/* 1569 */     if (insets.right != -1) {
/* 1570 */       for (int index = this.rangeBounds.length - 1; index >= 0; index--) {
/* 1571 */         this.rangeBounds[index].x += (this.rangePosition[index] == 1 ? right_offset : 0);
/*      */       }
/*      */     }
/*      */     
/* 1575 */     if (insets != null) {
/* 1576 */       left = insets.left == -1 ? left + left_offset : insets.left;
/* 1577 */       right = insets.right == -1 ? right + right_offset : insets.right;
/* 1578 */       top = insets.top == -1 ? top : insets.top;
/* 1579 */       bottom = insets.bottom == -1 ? bottom : insets.bottom;
/*      */     }
/*      */     
/*      */ 
/* 1583 */     bounds.x = left;
/* 1584 */     bounds.width = (size.width - left - right - 1);
/* 1585 */     bounds.y = top;
/* 1586 */     bounds.height = (size.height - top - bottom - 1);
/*      */     
/*      */ 
/* 1589 */     switch (getLegendPosition())
/*      */     {
/*      */     case 1: 
/*      */     default: 
/* 1593 */       this.visibleLegend.x = (bounds.x + bounds.width + right_offset + 15);
/* 1594 */       this.visibleLegend.x = Math.max(this.legend.x, this.visibleLegend.x);
/* 1595 */       this.visibleLegend.y = this.legend.y;
/* 1596 */       this.visibleLegend.width = (size.width - this.visibleLegend.x - 1);
/* 1597 */       this.visibleLegend.height = (size.height - this.legend.y - 12);
/*      */       
/* 1599 */       if (this.legend.width > this.visibleLegend.width) {
/* 1600 */         this.visibleLegend.height = Math.min(this.legend.height, this.visibleLegend.height - 9);
/*      */       }
/* 1602 */       if (this.legend.height > this.visibleLegend.height) {
/* 1603 */         this.visibleLegend.width = Math.min(this.legend.width, this.visibleLegend.width - 9);
/*      */       }
/* 1605 */       break;
/*      */     
/*      */ 
/*      */     case 0: 
/* 1609 */       this.visibleLegend.x = this.legend.x;
/* 1610 */       this.visibleLegend.y = this.legend.y;
/* 1611 */       this.visibleLegend.width = (bounds.x - left_offset - 20);
/* 1612 */       this.visibleLegend.height = (size.height - this.legend.y - 12);
/*      */       
/* 1614 */       if (this.legend.width > this.visibleLegend.width) {
/* 1615 */         this.visibleLegend.height = Math.min(this.legend.height, this.visibleLegend.height - 9);
/*      */       }
/* 1617 */       if (this.legend.height > this.visibleLegend.height) {
/* 1618 */         this.visibleLegend.width = Math.min(this.legend.width, this.visibleLegend.width - 9);
/* 1619 */         this.visibleLegend.x += 9;
/*      */       }
/* 1621 */       break;
/*      */     
/*      */ 
/*      */     case 2: 
/* 1625 */       this.visibleLegend.x = this.legend.x;
/* 1626 */       this.visibleLegend.y = this.legend.y;
/* 1627 */       this.visibleLegend.width = (size.width - this.legend.x - 9);
/* 1628 */       this.visibleLegend.height = (bounds.y - 13);
/* 1629 */       if ((insets.top != -1) && (isTitleOn())) {
/* 1630 */         this.visibleLegend.height -= getLabelSize(getTitle(), getFontMetrics(getFont("titleFont"))).height;
/*      */       }
/*      */       
/* 1633 */       if (this.legend.height > this.visibleLegend.height) {
/* 1634 */         this.visibleLegend.width = Math.min(this.legend.width, this.visibleLegend.width - 9);
/*      */       }
/* 1636 */       if (this.legend.width > this.visibleLegend.width) {
/* 1637 */         this.visibleLegend.height = Math.min(this.legend.height, this.visibleLegend.height - 9);
/*      */       }
/* 1639 */       break;
/*      */     
/*      */     case 3: 
/* 1642 */       this.legend.y = Math.max(this.legend.y, bounds.y + bounds.height + bottom_offset + 6);
/* 1643 */       this.visibleLegend.y = this.legend.y;
/* 1644 */       this.visibleLegend.x = this.legend.x;
/* 1645 */       this.visibleLegend.width = (size.width - this.legend.x - 9);
/* 1646 */       this.visibleLegend.height = (size.height - this.visibleLegend.y - 4);
/*      */       
/* 1648 */       if (this.legend.height > this.visibleLegend.height) {
/* 1649 */         this.visibleLegend.width = Math.min(this.legend.width, this.visibleLegend.width - 9);
/*      */       }
/* 1651 */       if (this.legend.width > this.visibleLegend.width) {
/* 1652 */         this.visibleLegend.height = Math.min(this.legend.height, this.visibleLegend.height - 9);
/*      */       }
/*      */       break;
/*      */     }
/* 1656 */     this.horizontalLegendScrollerOn = (this.legend.width > this.visibleLegend.width);
/* 1657 */     this.verticalLegendScrollerOn = (this.legend.height > this.visibleLegend.height);
/*      */     
/*      */ 
/* 1660 */     this.needGraphBounds = false;
/* 1661 */     this.currentBounds = bounds;
/* 1662 */     return bounds;
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
/*      */   private void paintLines(Graphics g, Rectangle grid, Rectangle dataBounds)
/*      */   {
/* 1676 */     int seriesCount = getSeriesCount();
/* 1677 */     if (seriesCount > 0)
/*      */     {
/* 1679 */       for (int i = seriesCount - 1; i >= 0; i--) {
/* 1680 */         int rangeIndex = getSeriesRange(i);
/* 1681 */         if (this.currentUpperRange[rangeIndex] != this.currentLowerRange[rangeIndex]) {
/* 1682 */           paintLine(g, i, grid, dataBounds, getSampleColor(i), isSelected(i, -1));
/*      */         }
/*      */       }
/*      */       
/* 1686 */       if ((!is3DModeOn()) && (this.lastSelectedLine >= 0)) {
/* 1687 */         int i = this.lastSelectedLine;
/* 1688 */         paintLine(g, i, grid, dataBounds, getSampleColor(i), isSelected(i, -1));
/*      */       }
/*      */       
/* 1691 */       if ((isValueLabelsOn(-1)) || (this.sampleLabelsOn) || (this.seriesLabelsOn)) {
/* 1692 */         paintStaticLabels(g, grid, dataBounds);
/*      */       }
/*      */     }
/*      */     
/* 1696 */     if (is3DModeOn()) {
/* 1697 */       g.setColor(getBackground());
/*      */       
/* 1699 */       Polygon edge = new Polygon();
/* 1700 */       edge.addPoint(grid.x + grid.width, grid.y + grid.height);
/* 1701 */       edge.addPoint(grid.x + grid.width, grid.y + grid.height - this.depth3dPoint.y + 1);
/* 1702 */       edge.addPoint(grid.x + grid.width - this.depth3dPoint.x - 1, grid.y + grid.height - this.depth3dPoint.y + 1);
/* 1703 */       g.fillPolygon(edge);
/* 1704 */       g.setColor(getChartForeground());
/* 1705 */       g.drawLine(grid.x + grid.width, grid.y + grid.height, grid.x + grid.width - this.depth3dPoint.x, grid.y + grid.height - this.depth3dPoint.y);
/*      */       
/* 1707 */       edge.xpoints[0] = grid.x;
/* 1708 */       edge.ypoints[0] = grid.y;
/* 1709 */       edge.xpoints[1] = (grid.x - this.depth3dPoint.x);
/* 1710 */       edge.ypoints[1] = grid.y;
/* 1711 */       edge.xpoints[2] = (grid.x - this.depth3dPoint.x);
/* 1712 */       edge.ypoints[2] = (grid.y - this.depth3dPoint.y);
/* 1713 */       g.setColor(getBackground());
/* 1714 */       g.fillPolygon(edge);
/* 1715 */       g.setColor(getChartForeground());
/* 1716 */       g.drawLine(grid.x, grid.y, grid.x - this.depth3dPoint.x, grid.y - this.depth3dPoint.y);
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
/*      */   protected void paintLine(Graphics g, int serie, Rectangle grid, Rectangle dataBounds, Color color, boolean selected)
/*      */   {
/* 1733 */     int seriesCount = getSeriesCount();
/* 1734 */     if ((serie < 0) || (serie >= seriesCount)) {
/* 1735 */       throw new IllegalArgumentException("Invalid series: " + serie);
/*      */     }
/*      */     
/*      */ 
/* 1739 */     if (selected) {
/* 1740 */       this.lastSelectedLine = serie;
/*      */     }
/*      */     
/*      */ 
/* 1744 */     ChartSample[] samples = getSamples(serie);
/* 1745 */     if ((samples == null) || (samples.length == 0)) {
/* 1746 */       return;
/*      */     }
/*      */     
/*      */ 
/* 1750 */     Color outline = (this.stackedOn) || (selected) ? color.darker() : color;
/*      */     
/*      */ 
/* 1753 */     boolean display3dOn = is3DModeOn();
/* 1754 */     Graphics cg = g;
/* 1755 */     if (display3dOn) {
/* 1756 */       cg.setClip(grid.x - this.depth3dPoint.x + 1, grid.y + 1, grid.width + this.depth3dPoint.x - 1, grid.height - this.depth3dPoint.y - 1);
/*      */     } else {
/* 1758 */       cg.setClip(grid.x + 1, grid.y, grid.width - 1, grid.height + 1);
/*      */     }
/*      */     
/*      */ 
/* 1762 */     int[] pos_x = new int[4];
/* 1763 */     int[] pos_y = new int[4];
/* 1764 */     boolean last_valid = false;
/* 1765 */     int last_valid_x = 0;
/* 1766 */     int last_valid_y = 0;
/*      */     try {
/* 1768 */       last_valid_x = this.samplePoints[serie][0][0];
/* 1769 */       last_valid_y = this.samplePoints[serie][0][1];
/*      */     }
/*      */     catch (IndexOutOfBoundsException localIndexOutOfBoundsException) {}catch (NullPointerException localNullPointerException) {}
/*      */     
/*      */ 
/*      */ 
/*      */ 
/* 1776 */     int cur_dash = 0;
/* 1777 */     int rest = 0;
/*      */     
/*      */ 
/* 1780 */     for (int sample = 0; sample < samples.length - 1; sample++)
/*      */     {
/*      */ 
/* 1783 */       boolean this_sample_valid = (samples[sample] != null) && (samples[sample].value != null) && (!samples[sample].value.isNaN());
/* 1784 */       boolean next_sample_valid = (samples[(sample + 1)] != null) && (samples[(sample + 1)].value != null) && (!samples[(sample + 1)].value.isNaN());
/*      */       
/*      */ 
/*      */ 
/* 1788 */       boolean paint_line = (this_sample_valid) && (next_sample_valid);
/* 1789 */       paint_line |= ((next_sample_valid) && (this.connectedLinesOn[serie] != 0));
/* 1790 */       paint_line &= getLineWidth(serie) > 0;
/*      */       
/*      */ 
/*      */       try
/*      */       {
/* 1795 */         if ((paint_line) && (sample > 0) && (sample < this.samplePoints[serie].length - 1)) {
/* 1796 */           paint_line = this.samplePoints[serie][sample][0] != last_valid_x;
/* 1797 */           paint_line |= this.samplePoints[serie][(sample + 1)][1] != last_valid_y;
/*      */         }
/*      */       }
/*      */       catch (IndexOutOfBoundsException localIndexOutOfBoundsException1) {}catch (NullPointerException localNullPointerException1) {}
/*      */       
/*      */ 
/*      */ 
/*      */ 
/* 1805 */       if (this_sample_valid) {
/*      */         try {
/* 1807 */           last_valid_x = this.samplePoints[serie][sample][0];
/* 1808 */           last_valid_y = this.samplePoints[serie][sample][1];
/* 1809 */           last_valid = true;
/*      */         }
/*      */         catch (IndexOutOfBoundsException localIndexOutOfBoundsException2) {}catch (NullPointerException localNullPointerException2) {}
/*      */       }
/*      */       
/*      */ 
/* 1815 */       paint_line &= last_valid;
/*      */       
/*      */ 
/*      */ 
/*      */ 
/* 1820 */       if (paint_line) {
/* 1821 */         if ((this.connectedLinesOn != null) && (serie < this.connectedLinesOn.length) && (this.connectedLinesOn[serie] != 0)) {
/* 1822 */           pos_x[0] = last_valid_x;
/* 1823 */           pos_y[0] = last_valid_y;
/*      */         } else {
/*      */           try {
/* 1826 */             pos_x[0] = this.samplePoints[serie][sample][0];
/* 1827 */             pos_y[0] = this.samplePoints[serie][sample][1];
/*      */           }
/*      */           catch (Exception localException) {}
/*      */         }
/*      */         
/* 1832 */         if ((this.samplePoints != null) && (serie < this.samplePoints.length) && (sample + 1 < this.samplePoints[serie].length)) {
/* 1833 */           pos_x[1] = this.samplePoints[serie][(sample + 1)][0];
/* 1834 */           pos_y[1] = this.samplePoints[serie][(sample + 1)][1];
/*      */         }
/*      */         
/*      */ 
/* 1838 */         if (display3dOn) {
/* 1839 */           pos_x[0] -= this.depth3dPoint.x;
/* 1840 */           pos_x[1] -= this.depth3dPoint.x;
/* 1841 */           pos_x[2] -= this.depth3dPoint.x;
/* 1842 */           pos_x[3] -= this.depth3dPoint.x;
/*      */         }
/*      */         
/*      */ 
/* 1846 */         if (this.stackedOn) {
/* 1847 */           pos_x[2] = pos_x[1];
/* 1848 */           if (serie > 0) {
/*      */             try {
/* 1850 */               pos_y[2] = this.samplePoints[(serie - 1)][(sample + 1)][1];
/*      */ 
/*      */             }
/*      */             catch (Exception localException1) {}
/*      */           } else {
/* 1855 */             pos_y[2] = (grid.y + grid.height);
/*      */           }
/* 1857 */           if (display3dOn) {
/* 1858 */             pos_y[2] -= this.depth3dPoint.y;
/*      */           }
/* 1860 */           pos_x[3] = pos_x[0];
/* 1861 */           if (serie > 0) {
/*      */             try {
/* 1863 */               pos_y[3] = this.samplePoints[(serie - 1)][sample][1];
/*      */ 
/*      */             }
/*      */             catch (Exception localException2) {}
/*      */           } else {
/* 1868 */             pos_y[3] = (grid.y + grid.height);
/*      */           }
/* 1870 */           if (display3dOn) {
/* 1871 */             pos_y[3] -= this.depth3dPoint.y;
/*      */           }
/*      */         }
/*      */         
/*      */ 
/*      */ 
/* 1877 */         boolean inside_grid = false;
/* 1878 */         int left = grid.x - (display3dOn ? this.depth3dPoint.x * 2 : 0);
/* 1879 */         int right = grid.x + grid.width;
/*      */         
/* 1881 */         inside_grid |= ((pos_x[0] >= left) && (pos_x[0] <= right));
/*      */         
/* 1883 */         inside_grid |= ((pos_x[1] >= left) && (pos_x[1] <= right));
/*      */         
/* 1885 */         inside_grid |= ((pos_x[0] <= left) && (pos_x[1] >= right));
/*      */         
/*      */ 
/* 1888 */         if (inside_grid)
/*      */         {
/*      */ 
/* 1891 */           pos_x[0] = Math.max(pos_x[0], 55536);
/* 1892 */           pos_x[1] = Math.max(pos_x[1], 55536);
/* 1893 */           pos_x[0] = Math.min(pos_x[0], 11000);
/* 1894 */           pos_x[1] = Math.min(pos_x[1], 11000);
/* 1895 */           pos_y[0] = Math.max(pos_y[0], 55536);
/* 1896 */           pos_y[1] = Math.max(pos_y[1], 55536);
/* 1897 */           pos_y[0] = Math.min(pos_y[0], 11000);
/* 1898 */           pos_y[1] = Math.min(pos_y[1], 11000);
/*      */           
/*      */ 
/* 1901 */           if ((this.seriesLinesOn != null) && (serie < this.seriesLinesOn.length) && (this.seriesLinesOn[serie] != 0)) {
/* 1902 */             if ((display3dOn) && (this.stackedOn))
/*      */             {
/* 1904 */               boolean topOn = serie == seriesCount - 1;
/*      */               
/*      */ 
/* 1907 */               boolean rightOn = sample == samples.length - 2;
/* 1908 */               if (sample < samples.length - 2) {
/* 1909 */                 rightOn |= ((samples[(sample + 2)] == null) || (samples[(sample + 2)].value == null) || (samples[(sample + 2)].value.isNaN()));
/* 1910 */                 rightOn &= this.connectedLinesOn[serie] == 0;
/*      */               }
/* 1912 */               paint3DLine(cg, pos_x[0], pos_y[0], pos_x[1], pos_y[1], pos_y[2], outline, topOn, rightOn, true);
/*      */ 
/*      */             }
/* 1915 */             else if (display3dOn) {
/* 1916 */               paint3DLine(cg, pos_x[0], pos_y[0], pos_x[1], pos_y[1], pos_y[2], outline, true, false, true);
/*      */             }
/*      */           }
/*      */           
/*      */ 
/* 1921 */           if ((this.stackedOn) && (this.seriesLinesOn[serie] != 0)) {
/* 1922 */             cg.setColor(selected ? color.darker() : color);
/* 1923 */             cg.fillPolygon(pos_x, pos_y, 4);
/* 1924 */             cg.drawLine(pos_x[1], pos_y[1], pos_x[2], pos_y[2]);
/*      */           }
/*      */           
/*      */ 
/* 1928 */           cg.setColor(color);
/* 1929 */           if ((display3dOn) && (this.stackedOn)) {
/* 1930 */             cg.setColor(color.darker().darker());
/* 1931 */           } else if ((display3dOn) || (this.stackedOn)) {
/* 1932 */             cg.setColor(color.darker());
/*      */           }
/*      */           
/*      */ 
/* 1936 */           boolean paint_whole = (this.stackedOn) || (is3DModeOn());
/* 1937 */           paint_whole |= ((this.lineStroke[serie] == null) || (this.lineStroke[serie].length == 0) || (this.lineStroke[serie][0] == 0));
/* 1938 */           if (((this.seriesLinesOn != null) && (serie < this.seriesLinesOn.length) && (this.seriesLinesOn[serie] != 0)) || (selected))
/*      */           {
/*      */ 
/* 1941 */             if (paint_whole) {
/* 1942 */               cg.drawLine(pos_x[0], pos_y[0], pos_x[1], pos_y[1]);
/*      */               
/* 1944 */               if ((!display3dOn) && (!this.stackedOn) && (this.lineWidth[serie] > 1) && ((this.seriesLinesOn[serie] != 0) || (selected))) {
/* 1945 */                 double slope = 0.0D;
/* 1946 */                 if (pos_x[1] - pos_x[0] != 0) {
/* 1947 */                   slope = Math.abs((pos_y[1] - pos_y[0]) / (pos_x[1] - pos_x[0]));
/*      */                 }
/* 1949 */                 int side = 1;
/* 1950 */                 for (int width = 2; width <= this.lineWidth[serie]; width++) {
/* 1951 */                   int offset = width / 2 * side;
/* 1952 */                   if (slope <= 1.0D) {
/* 1953 */                     cg.drawLine(pos_x[0], pos_y[0] + offset, pos_x[1], pos_y[1] + offset);
/*      */                   } else {
/* 1955 */                     cg.drawLine(pos_x[0] - offset, pos_y[0], pos_x[1] - offset, pos_y[1]);
/*      */                   }
/* 1957 */                   side = -side;
/*      */                 }
/*      */                 
/*      */               }
/*      */             }
/*      */             else
/*      */             {
/* 1964 */               int cur_pos = 0;
/*      */               
/* 1966 */               double tg = (pos_y[1] - pos_y[0]) / (pos_x[1] - pos_x[0]);
/* 1967 */               int distance; int distance; if ((tg >= 1.0D) || (tg <= -1.0D)) {
/* 1968 */                 distance = Math.abs(pos_y[1] - pos_y[0]);
/*      */               } else {
/* 1970 */                 distance = Math.abs(pos_x[1] - pos_x[0]);
/*      */               }
/*      */               
/* 1973 */               while (cur_pos < distance) {
/*      */                 int length;
/* 1975 */                 if (rest != 0)
/*      */                 {
/*      */ 
/* 1978 */                   int length = rest;
/* 1979 */                   rest = 0;
/*      */                 } else {
/* 1981 */                   length = this.lineStroke[serie][cur_dash];
/*      */                 }
/*      */                 
/* 1984 */                 if (cur_dash % 2 == 0) {
/* 1985 */                   paintDash(cg, serie, pos_x, pos_y, length - 1, cur_pos, tg);
/* 1986 */                   if (selected) {
/* 1987 */                     cg.setColor(color.darker().darker());
/* 1988 */                     paintDash(cg, serie, pos_x, pos_y, length - 1, cur_pos, tg);
/* 1989 */                     cg.setColor(color);
/*      */                   }
/*      */                 }
/*      */                 
/*      */ 
/* 1994 */                 cur_pos += length;
/* 1995 */                 if (cur_pos - distance <= 0) {
/* 1996 */                   if (cur_dash == this.lineStroke[serie].length - 1) {
/* 1997 */                     cur_dash = 0;
/*      */                   } else {
/* 1999 */                     cur_dash++;
/*      */                   }
/*      */                 }
/*      */               }
/* 2003 */               rest = cur_pos - distance;
/*      */             }
/*      */           }
/*      */           
/*      */ 
/* 2008 */           if ((selected) && (paint_whole) && (this.seriesLinesOn != null) && (serie < this.seriesLinesOn.length) && (this.seriesLinesOn[serie] != 0) && (!display3dOn) && (!this.stackedOn)) {
/* 2009 */             double slope = 0.0D;
/* 2010 */             if (pos_x[1] - pos_x[0] != 0) {
/* 2011 */               slope = Math.abs((pos_y[1] - pos_y[0]) / (pos_x[1] - pos_x[0]));
/*      */             }
/* 2013 */             int offset = this.lineWidth[serie] / 2 + 1;
/* 2014 */             cg.setColor(color.darker().darker());
/* 2015 */             if (slope <= 1.0D) {
/* 2016 */               cg.drawLine(pos_x[0], pos_y[0] + offset, pos_x[1], pos_y[1] + offset);
/*      */             } else {
/* 2018 */               cg.drawLine(pos_x[0] - offset, pos_y[0], pos_x[1] - offset, pos_y[1]);
/*      */             }
/*      */           }
/*      */         }
/*      */       }
/*      */     }
/*      */     
/*      */ 
/* 2026 */     if (!display3dOn) {
/* 2027 */       for (int sample = 0; (samples != null) && (sample < samples.length) && (this.sampleHighlightOn != null) && (serie < this.sampleHighlightOn.length) && (sample < this.sampleHighlightOn[serie].length); sample++) {
/* 2028 */         ChartSample s = samples[sample];
/* 2029 */         if ((this.sampleHighlightOn[serie][sample] != 0) && (s != null) && (s.hasValue())) {
/*      */           try {
/* 2031 */             int xpos = this.samplePoints[serie][sample][0];
/* 2032 */             int ypos = this.samplePoints[serie][sample][1];
/*      */             
/* 2034 */             if ((ypos >= grid.y) && (ypos <= grid.y + grid.height) && 
/* 2035 */               (xpos >= grid.x) && (xpos <= grid.x + grid.width + 1))
/*      */             {
/* 2037 */               paintSampleHighlight(g, serie, sample, Math.round(xpos), ypos, color);
/*      */             }
/*      */           }
/*      */           catch (Exception localException3) {}
/*      */         }
/*      */       }
/*      */     }
/*      */     
/*      */ 
/* 2046 */     g.setClip(0, 0, 32767, 32767);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   private void paintDash(Graphics g, int serie, int[] pos_x, int[] pos_y, int length, int cur_pos, double tg)
/*      */   {
/*      */     int end_y;
/*      */     
/*      */ 
/*      */     int start_x;
/*      */     
/*      */ 
/*      */     int start_y;
/*      */     
/*      */ 
/*      */     int end_x;
/*      */     
/*      */ 
/*      */     int end_y;
/*      */     
/*      */ 
/* 2069 */     if (tg <= -1.0D) {
/* 2070 */       int start_x = pos_x[0] + Math.abs((int)(cur_pos / tg));
/* 2071 */       int start_y = pos_y[0] - cur_pos;
/* 2072 */       int length_x = -(int)(length / tg);
/* 2073 */       int length_y = -length;
/* 2074 */       int end_x = Math.min(start_x + length_x, pos_x[1]);
/* 2075 */       end_y = Math.max(start_y + length_y, pos_y[1]);
/*      */     } else {
/*      */       int end_y;
/* 2078 */       if ((tg > -1.0D) && (tg < 0.0D)) {
/* 2079 */         int start_x = pos_x[0] + cur_pos;
/* 2080 */         int start_y = pos_y[0] + (int)(cur_pos * tg);
/* 2081 */         int length_x = length;
/* 2082 */         int length_y = (int)(length * tg);
/* 2083 */         int end_x = Math.min(start_x + length_x, pos_x[1]);
/* 2084 */         end_y = Math.max(start_y + length_y, pos_y[1]);
/*      */       } else {
/*      */         int end_y;
/* 2087 */         if ((tg < 1.0D) && (tg > 0.0D)) {
/* 2088 */           int start_x = pos_x[0] + cur_pos;
/* 2089 */           int start_y = pos_y[0] + (int)(cur_pos * tg);
/* 2090 */           int length_x = length;
/* 2091 */           int length_y = (int)(length * tg);
/* 2092 */           int end_x = Math.min(start_x + length_x, pos_x[1]);
/* 2093 */           end_y = Math.min(start_y + length_y, pos_y[1]);
/*      */         } else {
/*      */           int end_y;
/* 2096 */           if (tg >= 1.0D) {
/* 2097 */             int start_x = pos_x[0] + Math.abs((int)(cur_pos / tg));
/* 2098 */             int start_y = pos_y[0] + cur_pos;
/* 2099 */             int length_x = (int)(length / tg);
/* 2100 */             int length_y = length;
/* 2101 */             int end_x = Math.min(start_x + length_x, pos_x[1]);
/* 2102 */             end_y = Math.min(start_y + length_y, pos_y[1]);
/*      */           }
/*      */           else
/*      */           {
/* 2106 */             start_x = pos_x[0] + cur_pos;
/* 2107 */             start_y = pos_y[0];
/* 2108 */             int length_x = length;
/* 2109 */             int length_y = 0;
/* 2110 */             end_x = Math.min(start_x + length_x, pos_x[1]);
/* 2111 */             end_y = start_y;
/*      */           }
/*      */         }
/*      */       } }
/* 2115 */     g.drawLine(start_x, start_y, end_x, end_y);
/* 2116 */     int side = 1;
/* 2117 */     for (int width = 2; width <= this.lineWidth[serie]; width++) {
/* 2118 */       int offset = width / 2 * side;
/* 2119 */       if ((tg >= 1.0D) || (tg <= -1.0D)) {
/* 2120 */         g.drawLine(start_x - offset, start_y, end_x - offset, end_y);
/*      */       } else {
/* 2122 */         g.drawLine(start_x, start_y - offset, end_x, end_y - offset);
/*      */       }
/* 2124 */       side = -side;
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
/*      */   protected void paint3DLine(Graphics g, int x1, int y1, int x2, int y2, int y3, Color color, boolean top, boolean right, boolean outlined)
/*      */   {
/* 2144 */     int[] pos_x = new int[4];
/* 2145 */     int[] pos_y = new int[4];
/* 2146 */     int seriesCount = this.stackedOn ? 1 : Math.max(1, getSeriesCount());
/* 2147 */     int depth_x = (int)Math.round(this.depth3dPoint.x / seriesCount);
/* 2148 */     int depth_y = (int)Math.round(this.depth3dPoint.y / seriesCount);
/* 2149 */     pos_x[0] = x1;
/* 2150 */     pos_y[0] = y1;
/* 2151 */     pos_x[1] = (x1 + depth_x);
/* 2152 */     pos_y[1] = (y1 + depth_y);
/* 2153 */     pos_x[2] = (pos_x[1] + (x2 - x1));
/* 2154 */     pos_y[2] = (y2 + depth_y);
/* 2155 */     pos_x[3] = x2;
/* 2156 */     pos_y[3] = y2;
/*      */     
/*      */ 
/*      */ 
/* 2160 */     Color darker = color.darker();
/* 2161 */     if (top) {
/* 2162 */       double angle = 0.0D;
/* 2163 */       if (pos_x[1] - pos_x[0] != 0) {
/* 2164 */         angle = (y1 - y2) / (x2 - x1);
/*      */       }
/* 2166 */       if (angle > 0.69D) {
/* 2167 */         g.setColor(darker);
/*      */       } else {
/* 2169 */         g.setColor(color);
/*      */       }
/* 2171 */       g.fillPolygon(pos_x, pos_y, 4);
/*      */       
/*      */ 
/* 2174 */       if (outlined) {
/* 2175 */         g.setColor(darker);
/* 2176 */         g.drawLine(pos_x[0], pos_y[0], pos_x[1], pos_y[1]);
/* 2177 */         g.drawLine(pos_x[1], pos_y[1], pos_x[2], pos_y[2]);
/* 2178 */         g.drawLine(pos_x[2], pos_y[2], pos_x[3], pos_y[3]);
/*      */       }
/*      */     }
/*      */     
/*      */ 
/* 2183 */     if (right) {
/* 2184 */       pos_x[0] = pos_x[3];
/* 2185 */       pos_y[0] = pos_y[3];
/* 2186 */       pos_x[1] = pos_x[2];
/* 2187 */       pos_y[1] = pos_y[2];
/* 2188 */       pos_x[2] = pos_x[2];
/* 2189 */       pos_y[2] = (y3 + depth_y);
/* 2190 */       pos_x[3] = pos_x[0];
/* 2191 */       pos_y[3] = y3;
/* 2192 */       g.setColor(color);
/* 2193 */       g.fillPolygon(pos_x, pos_y, 4);
/*      */       
/*      */ 
/* 2196 */       if (outlined) {
/* 2197 */         g.setColor(darker);
/* 2198 */         g.drawLine(pos_x[0], pos_y[0], pos_x[1], pos_y[1]);
/* 2199 */         g.drawLine(pos_x[1], pos_y[1], pos_x[2], pos_y[2]);
/* 2200 */         g.drawLine(pos_x[2], pos_y[2], pos_x[3], pos_y[3]);
/* 2201 */         g.drawLine(pos_x[3], pos_y[3], pos_x[0], pos_y[0]);
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
/*      */   private boolean isLabelInside(double sample_xpos, double sample_ypos, double label_bottom, Rectangle gridBounds)
/*      */   {
/* 2217 */     boolean inside = true;
/* 2218 */     if (is3DModeOn()) {
/* 2219 */       if (sample_ypos <= label_bottom) {
/* 2220 */         inside = 
/* 2221 */           (sample_ypos >= gridBounds.y - 2) && 
/* 2222 */           (sample_ypos <= gridBounds.y + gridBounds.height + 2 - this.depth3dPoint.y) && 
/* 2223 */           (sample_xpos >= gridBounds.x - 2) && (
/* 2224 */           sample_xpos <= gridBounds.x + gridBounds.width + this.depth3dPoint.x + 2);
/*      */       } else {
/* 2226 */         double x_change = this.depth3dPoint.x / getSeriesCount();
/* 2227 */         double y_change = this.depth3dPoint.y / getSeriesCount();
/* 2228 */         inside = 
/* 2229 */           (sample_ypos >= gridBounds.y - y_change - 2.0D) && 
/* 2230 */           (sample_ypos <= gridBounds.y + gridBounds.height - this.depth3dPoint.y - y_change) && 
/* 2231 */           (sample_xpos >= gridBounds.x - x_change - 2.0D) && (
/* 2232 */           sample_xpos <= gridBounds.x + gridBounds.width - x_change + this.depth3dPoint.x + 2.0D);
/*      */       }
/*      */     } else {
/* 2235 */       inside = 
/* 2236 */         (sample_ypos >= gridBounds.y - 2) && 
/* 2237 */         (sample_ypos <= gridBounds.y + gridBounds.height + 2) && 
/* 2238 */         (sample_xpos >= gridBounds.x - 4) && (
/* 2239 */         sample_xpos <= gridBounds.x + gridBounds.width + 2);
/*      */     }
/* 2241 */     return inside;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   private void paintStaticLabels(Graphics g, Rectangle gridBounds, Rectangle dataBounds)
/*      */   {
/* 2253 */     int sampleCount = getSampleCount();
/* 2254 */     int seriesCount = getSeriesCount();
/* 2255 */     boolean display3dOn = is3DModeOn();
/*      */     
/* 2257 */     double delta_x = dataBounds.width;
/* 2258 */     if (sampleCount > 2) {
/* 2259 */       delta_x = dataBounds.width / (sampleCount - 1);
/*      */     }
/*      */     
/*      */ 
/* 2263 */     Font value_label_font = getFont("valueLabelFont");
/* 2264 */     FontMetrics value_fm = getFontMetrics(value_label_font);
/* 2265 */     int valueLabelAngle = getLabelAngle("valueLabelAngle");
/*      */     
/*      */ 
/* 2268 */     Font sample_label_font = getFont("sampleLabelFont");
/* 2269 */     FontMetrics sample_fm = getFontMetrics(sample_label_font);
/* 2270 */     int sampleLabelAngle = getLabelAngle("sampleLabelAngle");
/*      */     
/*      */ 
/*      */ 
/* 2274 */     for (int serie = 0; serie < seriesCount; serie++)
/*      */     {
/* 2276 */       if (this.stackedOn) {
/* 2277 */         if (serie != 0) break;
/* 2278 */         serie = seriesCount - 1;
/*      */       }
/*      */       
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 2287 */       String prefix = getLabel("valueLabelPrefix_" + serie);
/* 2288 */       if (prefix == null) {
/* 2289 */         prefix = getLabel("valueLabelPrefix");
/*      */       }
/* 2291 */       String postfix = getLabel("valueLabelPostfix_" + serie);
/* 2292 */       if (postfix == null) {
/* 2293 */         postfix = getLabel("valueLabelPostfix");
/*      */       }
/*      */       
/* 2296 */       int decimals = getSampleDecimalCount(serie);
/*      */       
/*      */ 
/* 2299 */       ChartSample[] values = getSamples(serie);
/* 2300 */       int startIndex = Math.max(this.visibleSamples[0] - 1, 0);
/* 2301 */       int stopIndex = Math.min(startIndex + this.visibleSamples[1] + 1, sampleCount);
/* 2302 */       if (this.rightToLeftScrollingOn) {
/* 2303 */         startIndex = 0;
/* 2304 */         stopIndex = getSampleCount();
/*      */       }
/* 2306 */       for (int sample = startIndex; sample < stopIndex; sample++)
/*      */       {
/* 2308 */         if ((this.stackedOn) || (
/* 2309 */           (values[sample] != null) && (values[sample].value != null) && (!values[sample].value.isNaN())))
/*      */         {
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 2316 */           double value = 0.0D;
/* 2317 */           if (this.stackedOn) {
/* 2318 */             boolean validValue = false;
/* 2319 */             for (int i = 0; i < seriesCount; i++) {
/* 2320 */               ChartSample s = getSample(i, sample);
/* 2321 */               if ((s != null) && (s.value != null) && (!s.value.isNaN())) {
/* 2322 */                 value += s.value.doubleValue();
/* 2323 */                 validValue = true;
/*      */               }
/*      */             }
/*      */             
/* 2327 */             if (!validValue) {
/*      */               continue;
/*      */             }
/*      */           }
/*      */           else
/*      */           {
/* 2333 */             ChartSample s = getSample(serie, sample);
/* 2334 */             if ((s == null) || (s.value == null) || (s.value.isNaN())) continue;
/* 2335 */             value = values[sample].getFloatValue();
/*      */           }
/*      */           
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 2342 */           double previous = value;
/* 2343 */           if ((sample >= 1) && (values[(sample - 1)] != null)) {
/* 2344 */             previous = values[(sample - 1)].getFloatValue();
/*      */           }
/* 2346 */           double next = value;
/* 2347 */           if ((sample < values.length - 1) && (values[(sample + 1)] != null)) {
/* 2348 */             next = values[(sample + 1)].getFloatValue();
/*      */           }
/* 2350 */           double prev_slope = (value - previous) / delta_x;
/* 2351 */           double next_slope = (next - value) / delta_x;
/*      */           
/*      */ 
/* 2354 */           Dimension valueAngledSize = new Dimension();
/* 2355 */           int sample_xpos = Integer.MIN_VALUE;
/* 2356 */           int sample_ypos = Integer.MIN_VALUE;
/* 2357 */           if (sample < this.samplePoints[serie].length) {
/* 2358 */             sample_xpos = this.samplePoints[serie][sample][0];
/* 2359 */             sample_ypos = this.samplePoints[serie][sample][1] - 1;
/*      */           }
/* 2361 */           if ((this.valueLabelStyle == 0) && (isValueLabelsOn(serie)))
/*      */           {
/* 2363 */             String valueLabel = formatNumber(value, decimals);
/* 2364 */             valueLabel = prefix != null ? prefix + valueLabel : valueLabel;
/* 2365 */             valueLabel = postfix != null ? valueLabel + postfix : valueLabel;
/* 2366 */             Dimension valueLabelSize = getLabelSize(valueLabel, value_fm);
/* 2367 */             valueAngledSize = getAngledLabelSize(valueLabelSize, valueLabelAngle);
/* 2368 */             Color fontColor = getValueLabelColor(serie);
/* 2369 */             if (fontColor == null) {
/* 2370 */               fontColor = getChartForeground();
/*      */             }
/*      */             
/*      */ 
/* 2374 */             int label_xpos = Math.round(sample_xpos - valueAngledSize.width / 2) + 1;
/* 2375 */             int label_ypos = sample_ypos;
/* 2376 */             if (valueLabelAngle % 180 == 0) {
/* 2377 */               label_ypos += value_fm.getAscent() / 2;
/*      */             } else {
/* 2379 */               label_ypos = sample_ypos - valueAngledSize.height / 2;
/*      */             }
/*      */             
/* 2382 */             if ((display3dOn) && (!this.stackedOn)) {
/* 2383 */               label_xpos -= this.depth3dPoint.x;
/* 2384 */               if (prev_slope >= next_slope) {
/* 2385 */                 label_ypos = (int)(label_ypos + this.depth3dPoint.y / seriesCount);
/* 2386 */                 label_xpos = (int)(label_xpos + this.depth3dPoint.x / seriesCount);
/*      */               }
/* 2388 */             } else if ((display3dOn) && (this.stackedOn)) {
/* 2389 */               label_xpos -= this.depth3dPoint.x;
/*      */             }
/*      */             
/*      */ 
/* 2393 */             if (isLabelInside(sample_xpos, sample_ypos, label_ypos + valueAngledSize.height, gridBounds)) {
/* 2394 */               g.setColor(fontColor);
/* 2395 */               g.setFont(value_label_font);
/* 2396 */               paintLabel(g, valueLabel, label_xpos, label_ypos, valueLabelSize, 0, valueLabelAngle, false);
/*      */             }
/*      */           }
/*      */           
/*      */ 
/* 2401 */           if (((this.valueLabelStyle == 1) && (isValueLabelsOn(serie))) || 
/* 2402 */             ((this.sampleLabelStyle == 1) && (this.sampleLabelsOn)) || (
/* 2403 */             (this.seriesLabelStyle == 1) && (this.seriesLabelsOn)))
/*      */           {
/* 2405 */             int angle = 0;
/* 2406 */             int vertical_offset = sample_fm.getAscent();
/* 2407 */             Color fontColor = null;
/*      */             
/* 2409 */             if ((this.valueLabelStyle == 1) && (isValueLabelsOn(serie))) {
/* 2410 */               fontColor = getValueLabelColor(serie);
/* 2411 */               angle = valueLabelAngle;
/* 2412 */               vertical_offset = value_fm.getAscent();
/*      */             }
/*      */             
/* 2415 */             if ((this.sampleLabelStyle == 1) && (this.sampleLabelsOn)) {
/* 2416 */               if (getSampleLabelColor(sample) != null) {
/* 2417 */                 fontColor = getSampleLabelColor(sample);
/*      */               }
/* 2419 */               angle = sampleLabelAngle;
/* 2420 */               vertical_offset = sample_fm.getAscent();
/*      */             }
/*      */             
/* 2423 */             if ((this.seriesLabelStyle == 1) && (this.seriesLabelsOn) && 
/* 2424 */               (fontColor == null)) {
/* 2425 */               fontColor = getSeriesLabelColor(serie);
/*      */             }
/*      */             
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 2434 */             if (fontColor == null) {
/* 2435 */               fontColor = getChartForeground();
/*      */             }
/*      */             
/* 2438 */             String label = constructLabel(serie, sample, 1, this.seriesLabelsOn, null);
/* 2439 */             Dimension labelSize = getLabelSize(label, value_fm);
/* 2440 */             Dimension angledSize = getAngledLabelSize(labelSize, angle);
/*      */             
/* 2442 */             int label_xpos = Math.round(sample_xpos - angledSize.width / 2) + 1;
/* 2443 */             if (display3dOn) {
/* 2444 */               label_xpos -= this.depth3dPoint.x;
/*      */             }
/*      */             
/*      */ 
/* 2448 */             int label_ypos = sample_ypos;
/* 2449 */             if ((display3dOn) && (prev_slope >= next_slope)) {
/* 2450 */               label_ypos = (int)(label_ypos + this.depth3dPoint.y / seriesCount);
/*      */             }
/*      */             
/*      */ 
/* 2454 */             int under_ypos = label_ypos;
/* 2455 */             if (angle % 180 == 0) {
/* 2456 */               under_ypos += vertical_offset;
/*      */             } else {
/* 2458 */               under_ypos += 6;
/*      */             }
/*      */             
/* 2461 */             int over_ypos = label_ypos;
/* 2462 */             if (angle % 180 != 0) {
/* 2463 */               over_ypos -= angledSize.height;
/*      */             }
/*      */             
/*      */ 
/*      */ 
/* 2468 */             boolean highlight = false;
/* 2469 */             int hsize = 0;
/*      */             try {
/* 2471 */               highlight = this.sampleHighlightOn[serie][sample];
/* 2472 */               hsize = this.sampleHighlightSize[serie];
/*      */             } catch (IndexOutOfBoundsException e) {
/* 2474 */               System.out.println("Internal error with value labels");
/* 2475 */               e.printStackTrace();
/*      */             }
/* 2477 */             if ((highlight) || ((this.valueLabelStyle == 0) && (isValueLabelsOn(serie))))
/*      */             {
/* 2479 */               if (hsize >= valueAngledSize.height) {
/* 2480 */                 under_ypos += hsize / 2;
/* 2481 */                 over_ypos -= hsize / 2;
/*      */               }
/*      */               else
/*      */               {
/* 2485 */                 under_ypos += valueAngledSize.height / 2 - 3;
/* 2486 */                 over_ypos -= valueAngledSize.height / 2 - 1;
/*      */               }
/*      */             }
/*      */             
/*      */ 
/*      */ 
/* 2492 */             if (((prev_slope < next_slope) && (this.upperRange[0] >= this.lowerRange[0])) || ((prev_slope >= next_slope) && (this.lowerRange[0] >= this.upperRange[0]))) {
/* 2493 */               label_ypos = under_ypos;
/*      */             } else {
/* 2495 */               label_ypos = over_ypos;
/*      */             }
/*      */             
/*      */ 
/* 2499 */             if ((label_ypos != under_ypos) && 
/* 2500 */               (display3dOn) && (!this.stackedOn))
/*      */             {
/* 2502 */               double change = this.depth3dPoint.x / seriesCount;
/* 2503 */               label_xpos = (int)(label_xpos + change);
/*      */             }
/*      */             
/*      */ 
/*      */ 
/* 2508 */             if (display3dOn) {
/* 2509 */               label_xpos = Math.max(gridBounds.x - this.depth3dPoint.x + 2, label_xpos);
/*      */             } else {
/* 2511 */               label_xpos = Math.max(gridBounds.x + 2, label_xpos);
/*      */             }
/* 2513 */             label_xpos = Math.min(label_xpos, gridBounds.x + gridBounds.width - angledSize.width - 1);
/*      */             
/*      */ 
/* 2516 */             if (isLabelInside(sample_xpos, sample_ypos, label_ypos + angledSize.height, gridBounds)) {
/* 2517 */               g.setColor(fontColor);
/* 2518 */               g.setFont(value_label_font);
/* 2519 */               paintLabel(g, label, label_xpos, label_ypos, labelSize, 0, angle, false);
/*      */             }
/*      */           }
/* 2522 */           sample_xpos = (int)(sample_xpos + delta_x);
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
/*      */   private void paintBelowSampleLabels(Graphics g, Rectangle gridBounds, Rectangle dataBounds)
/*      */   {
/* 2536 */     Font sampleLabelFont = getFont("sampleLabelFont");
/* 2537 */     FontMetrics fm = getFontMetrics(sampleLabelFont);
/* 2538 */     int sampleAngle = getLabelAngle("sampleLabelAngle");
/*      */     
/*      */ 
/* 2541 */     String[] sampleLabels = getSampleLabels();
/* 2542 */     if (sampleLabels == null) {
/* 2543 */       return;
/*      */     }
/*      */     
/*      */ 
/* 2547 */     int startIndex = Math.max(this.visibleSamples[0] - 1, 0);
/* 2548 */     int stopIndex = Math.min(startIndex + this.visibleSamples[1] + 1, getSampleCount());
/* 2549 */     if (this.rightToLeftScrollingOn) {
/* 2550 */       startIndex = 0;
/* 2551 */       stopIndex = getSampleCount();
/*      */     }
/* 2553 */     double prev_x = 0.0D;
/* 2554 */     for (int sample = startIndex; (sample < stopIndex) && (sample < sampleLabels.length); sample++)
/*      */     {
/* 2556 */       String sampleLabel = sampleLabels[sample];
/* 2557 */       Dimension sampleLabelSize = getLabelSize(sampleLabel, fm);
/* 2558 */       Dimension sampleAngledSize = getAngledLabelSize(sampleLabelSize, sampleAngle);
/*      */       
/*      */ 
/* 2561 */       if ((this.sampleLabelsOn) && ((this.sampleLabelStyle == 2) || (this.sampleLabelStyle == 4)))
/*      */       {
/* 2563 */         int ypos = gridBounds.y + gridBounds.height;
/* 2564 */         int left_edge = gridBounds.x;
/* 2565 */         int right_edge = gridBounds.x + gridBounds.width;
/* 2566 */         if (is3DModeOn()) {
/* 2567 */           ypos -= this.depth3dPoint.y;
/* 2568 */           right_edge -= this.depth3dPoint.x;
/* 2569 */           left_edge -= this.depth3dPoint.x;
/*      */         }
/*      */         
/* 2572 */         if (this.sampleScrollerOn) {
/* 2573 */           ypos += 10;
/*      */         }
/*      */         
/*      */ 
/* 2577 */         int sample_xpos = 0;
/* 2578 */         if ((this.sampleLabelPos != null) && (sample < this.sampleLabelPos.length)) {
/* 2579 */           sample_xpos = this.sampleLabelPos[sample];
/*      */         }
/* 2581 */         sample_xpos -= (is3DModeOn() ? this.depth3dPoint.x : 0);
/* 2582 */         if ((sampleLabel != null) && (sampleLabel.length() > 0) && (sample_xpos >= left_edge) && (sample_xpos <= right_edge + 1))
/*      */         {
/* 2584 */           int label_ypos = ypos + 8;
/* 2585 */           if (sampleAngle % 180 == 0) {
/* 2586 */             label_ypos += fm.getMaxAscent() - 4;
/*      */           }
/*      */           
/*      */ 
/* 2590 */           int label_xpos = 0;
/* 2591 */           if (sampleAngle % 180 == 0) {
/* 2592 */             label_xpos = sample_xpos - sampleAngledSize.width / 2;
/* 2593 */           } else if (((sampleAngle % 360 > 90) && (sampleAngle % 360 < 180)) || (sampleAngle % 360 > 270)) {
/* 2594 */             label_xpos = sample_xpos - sampleAngledSize.width + fm.getAscent() / 2;
/*      */           } else {
/* 2596 */             label_xpos = sample_xpos - fm.getAscent() / 2;
/*      */           }
/*      */           
/*      */ 
/* 2600 */           g.setFont(sampleLabelFont);
/* 2601 */           Color chartForeground = getForeground();
/* 2602 */           Color fontColor = getSampleLabelColor(sample);
/* 2603 */           if (fontColor == null) {
/* 2604 */             fontColor = chartForeground;
/*      */           }
/* 2606 */           g.setColor(chartForeground);
/*      */           
/* 2608 */           if (this.autoLabelSpacingOn) {
/* 2609 */             if ((sampleLabel != null) && (sampleLabel.trim().length() > 0) && (
/* 2610 */               (label_xpos > prev_x) || (sample == 0))) {
/* 2611 */               g.drawLine(Math.round(sample_xpos), ypos, Math.round(sample_xpos), ypos + 3);
/* 2612 */               g.setColor(fontColor);
/* 2613 */               paintLabel(g, sampleLabel, label_xpos, label_ypos, sampleLabelSize, 0, sampleAngle, false);
/* 2614 */               prev_x = label_xpos + sampleAngledSize.width;
/*      */             }
/*      */             
/*      */ 
/*      */           }
/* 2619 */           else if ((sampleLabel != null) && (sampleLabel.trim().length() > 0)) {
/* 2620 */             g.drawLine(Math.round(sample_xpos), ypos, Math.round(sample_xpos), ypos + 3);
/* 2621 */             g.setColor(fontColor);
/* 2622 */             paintLabel(g, sampleLabel, label_xpos, label_ypos, sampleLabelSize, 0, sampleAngle, false);
/*      */           }
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
/*      */   void paintFloatingLabel(Graphics g, String label, Rectangle grid, int sample, int series, Font font, FontMetrics fm)
/*      */   {
/* 2643 */     int offset = 0;
/* 2644 */     if (is3DModeOn()) {
/* 2645 */       if (this.stackedOn) {
/* 2646 */         offset = -this.depth3d;
/*      */       } else {
/* 2648 */         double depth_x = this.depth3dPoint.x / getSeriesCount();
/* 2649 */         offset = (int)Math.round(depth_x * series) - this.depth3dPoint.x;
/*      */       }
/*      */     }
/*      */     
/*      */ 
/*      */ 
/* 2655 */     int xpos = this.samplePoints[series][sample][0];
/* 2656 */     int ypos = this.samplePoints[series][sample][1];
/* 2657 */     if ((xpos < grid.x) || (xpos > grid.x + grid.width - offset)) {
/* 2658 */       return;
/*      */     }
/* 2660 */     if ((ypos < grid.y) || (ypos > grid.y + grid.height - this.depth3dPoint.y)) {
/* 2661 */       return;
/*      */     }
/*      */     
/* 2664 */     Dimension labelSize = getLabelSize(label, fm);
/* 2665 */     int label_x = xpos - labelSize.width / 2 + offset;
/* 2666 */     int label_y = ypos - labelSize.height + fm.getAscent() - 3;
/* 2667 */     if (this.mouseOverSampleIndex > -1) {
/* 2668 */       label_x = this.mousePosition.x - labelSize.width / 2 + offset;
/* 2669 */       label_y = this.mousePosition.y - labelSize.height + fm.getAscent() - 3;
/*      */     }
/*      */     
/* 2672 */     paintFloatingLabel(g, label, label_x, label_y, labelSize, getSampleColor(series).darker(), fm);
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
/*      */   protected void paintSampleHighlight(Graphics g, int serie, int sample, int x, int y, Color color)
/*      */   {
/* 2686 */     Shape clip = g.getClip();
/* 2687 */     g.setClip(0, 0, 32767, 32767);
/*      */     
/*      */ 
/* 2690 */     String imageName = getSampleHighlightImage(serie, sample);
/* 2691 */     if (imageName == null) {
/* 2692 */       imageName = getSampleHighlightImage(serie, -1);
/*      */     }
/* 2694 */     if (imageName == null) {
/* 2695 */       imageName = getSampleHighlightImage(-1, -1);
/*      */     }
/* 2697 */     if (imageName == null) {
/* 2698 */       imageName = getLegendImage(serie);
/*      */     }
/* 2700 */     Dimension imageSize = getImageSize(imageName);
/* 2701 */     if ((imageName != null) && (imageSize.width > 0) && (imageSize.height > 0)) {
/* 2702 */       Image image = (Image)this.images.get(imageName);
/* 2703 */       if (image != null) {
/* 2704 */         g.drawImage(image, x - imageSize.width / 2, y - imageSize.height / 2, this);
/*      */       }
/*      */     }
/*      */     else
/*      */     {
/* 2709 */       int size = 6;
/* 2710 */       if ((serie >= 0) && (serie < this.sampleHighlightSize.length)) {
/* 2711 */         size = this.sampleHighlightSize[serie];
/*      */       }
/* 2713 */       int style = 0;
/* 2714 */       if ((serie >= 0) && (serie < this.sampleHighlightStyle.length)) {
/* 2715 */         style = this.sampleHighlightStyle[serie];
/*      */       }
/*      */       
/* 2718 */       Color chartBackground = getChartBackground();
/* 2719 */       g.setColor(color);
/* 2720 */       switch (style) {
/*      */       case 0: 
/* 2722 */         g.drawOval(x - size / 2, y - size / 2, size, size);
/* 2723 */         break;
/*      */       
/*      */       case 2: 
/* 2726 */         g.fillOval(x - size / 2, y - size / 2, size + 1, size + 1);
/* 2727 */         break;
/*      */       
/*      */       case 1: 
/* 2730 */         g.setColor(chartBackground);
/* 2731 */         g.fillOval(x - size / 2, y - size / 2, size + 1, size + 1);
/* 2732 */         g.setColor(color);
/* 2733 */         g.drawOval(x - size / 2, y - size / 2, size, size);
/* 2734 */         break;
/*      */       
/*      */       case 3: 
/* 2737 */         g.drawRect(x - size / 2, y - size / 2, size, size);
/* 2738 */         break;
/*      */       
/*      */       case 5: 
/* 2741 */         g.fillRect(x - size / 2, y - size / 2, size + 1, size + 1);
/* 2742 */         break;
/*      */       
/*      */       case 4: 
/* 2745 */         g.setColor(chartBackground);
/* 2746 */         g.fillRect(x - size / 2, y - size / 2, size + 1, size + 1);
/* 2747 */         g.setColor(color);
/* 2748 */         g.drawRect(x - size / 2, y - size / 2, size, size);
/* 2749 */         break;
/*      */       
/*      */       case 6: 
/*      */       case 7: 
/*      */       case 8: 
/* 2754 */         int[] xpoints = new int[4];
/* 2755 */         xpoints[0] = (xpoints[2] = x);
/* 2756 */         xpoints[1] = (x + size / 2);
/* 2757 */         xpoints[3] = (x - size / 2);
/* 2758 */         int[] ypoints = new int[4];
/* 2759 */         ypoints[0] = (y - size / 2);
/* 2760 */         ypoints[2] = (y + size / 2);
/* 2761 */         ypoints[1] = (ypoints[3] = y);
/* 2762 */         if (style == 7) {
/* 2763 */           g.setColor(chartBackground);
/* 2764 */           g.fillPolygon(xpoints, ypoints, 4);
/* 2765 */         } else if (style == 8) {
/* 2766 */           g.setColor(color);
/* 2767 */           g.fillPolygon(xpoints, ypoints, 4);
/*      */         }
/* 2769 */         g.setColor(color);
/* 2770 */         g.drawPolygon(xpoints, ypoints, 4);
/*      */       }
/*      */       
/*      */     }
/* 2774 */     g.setClip(clip);
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
/*      */   private void paint3DZeroDivider(Graphics g, Rectangle grid, int index)
/*      */   {
/* 2787 */     index = Math.min(this.rangeOn.length - 1, Math.max(0, index));
/* 2788 */     if (this.rangeOn[index] != 0)
/*      */     {
/* 2790 */       int zero_line = Math.max(this.zeroLine[index], grid.y);
/* 2791 */       zero_line = Math.min(zero_line, grid.y + grid.height);
/* 2792 */       int x = grid.x - this.depth3dPoint.x;
/* 2793 */       int y = zero_line - this.depth3dPoint.y;
/*      */       
/* 2795 */       g.setColor(index == 0 ? getChartForeground() : getRangeColor(index));
/* 2796 */       g.drawLine(x, y, x + grid.width, y);
/* 2797 */       g.drawLine(x + grid.width, y, grid.x + grid.width, zero_line);
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
/*      */   boolean isAnyFloatingLabelsOn(int serie)
/*      */   {
/* 2812 */     boolean labels_on = (isValueLabelsOn(serie)) && (this.valueLabelStyle == 3);
/* 2813 */     labels_on |= ((this.sampleLabelsOn) && ((this.sampleLabelStyle == 3) || (this.sampleLabelStyle == 4)));
/* 2814 */     labels_on |= ((this.seriesLabelsOn) && (this.seriesLabelStyle == 3));
/* 2815 */     return labels_on;
/*      */   }
/*      */ }


/* Location:              E:\java\java学习\hutubill\lib\all.jar!\com\objectplanet\chart\LineChart.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */
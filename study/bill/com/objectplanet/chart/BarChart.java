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
/*      */ public class BarChart
/*      */   extends Chart
/*      */ {
/*      */   public static final int SIDE_BY_SIDE_BARS = 0;
/*      */   public static final int STACKED_BARS = 1;
/*      */   private Color[] barLabelColors;
/*      */   private boolean multiColorOn;
/*      */   private boolean barOutlineOn;
/*      */   private Color barOutlineColor;
/*      */   private boolean autoLabelSpacingOn;
/*      */   private double barWidthFraction;
/*      */   private int barType;
/*      */   private Rectangle[][] barBounds;
/*      */   
/*      */   public BarChart()
/*      */   {
/*   81 */     this(1);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public BarChart(int count)
/*      */   {
/*   91 */     this(count, 100.0D);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public BarChart(int count, double range)
/*      */   {
/*  101 */     this(count, range, 0.0D);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public BarChart(int sampleCount, double range, double lowerRange)
/*      */   {
/*  112 */     this(1, sampleCount, range, lowerRange);
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
/*      */   public BarChart(int seriesCount, int sampleCount, double range, double lowerRange)
/*      */   {
/*  125 */     super(sampleCount);
/*  126 */     this.chartType = "bar";
/*  127 */     this.barLabelColors = new Color[sampleCount];
/*  128 */     setAutomaticRepaintOn(false);
/*  129 */     setSeriesCount(seriesCount);
/*  130 */     this.upperRange[0] = (this.currentUpperRange[0] = range);
/*  131 */     this.lowerRange[0] = (this.currentLowerRange[0] = lowerRange);
/*  132 */     this.multiColorOn = false;
/*  133 */     this.valueLabelStyle = 1;
/*  134 */     this.seriesLabelStyle = 1;
/*  135 */     this.sampleLabelStyle = 1;
/*  136 */     this.barOutlineOn = true;
/*  137 */     this.autoLabelSpacingOn = false;
/*  138 */     this.barWidthFraction = 0.4D;
/*  139 */     this.barType = 0;
/*  140 */     this.multiSeriesOn = false;
/*  141 */     this.barWidthFraction = 0.4D;
/*  142 */     this.barOutlineColor = null;
/*  143 */     this.seriesLabelsOn = false;
/*  144 */     setAutomaticRepaintOn(true);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public void reset()
/*      */   {
/*  152 */     super.reset();
/*  153 */     this.multiColorOn = false;
/*  154 */     this.sampleLabelStyle = 1;
/*  155 */     this.seriesLabelStyle = 1;
/*  156 */     this.valueLabelStyle = 1;
/*  157 */     this.barOutlineOn = true;
/*  158 */     this.autoLabelSpacingOn = false;
/*  159 */     this.barWidthFraction = 0.4D;
/*  160 */     this.barType = 0;
/*  161 */     this.multiSeriesOn = false;
/*  162 */     this.barOutlineColor = null;
/*  163 */     this.seriesLabelsOn = false;
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
/*  177 */     if ((this.barType == 0) || (getSeriesRange(0) != range)) {
/*  178 */       return super.getHighestValue(range);
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*  183 */     double max = super.getHighestValue(range);
/*  184 */     int sampleCount = getSampleCount();
/*  185 */     int seriesCount = getSeriesCount();
/*  186 */     for (int sample = 0; sample < sampleCount; sample++)
/*      */     {
/*  188 */       double sum = 0.0D;
/*  189 */       for (int serie = 0; serie < seriesCount; serie++) {
/*  190 */         ChartSample s = getSample(serie, sample);
/*  191 */         if ((s != null) && (s.value != null) && (!s.value.isNaN()) && (s.value.doubleValue() > 0.0D)) {
/*  192 */           sum += s.value.doubleValue();
/*      */         }
/*      */       }
/*  195 */       max = Math.max(max, sum);
/*      */     }
/*  197 */     return max;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   double getLowestValue(int range)
/*      */   {
/*  208 */     if (this.barType == 0) {
/*  209 */       return super.getLowestValue(range);
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*  214 */     double min = super.getLowestValue(range);
/*  215 */     int sampleCount = getSampleCount();
/*  216 */     int seriesCount = getSeriesCount();
/*  217 */     for (int sample = 0; sample < sampleCount; sample++)
/*      */     {
/*  219 */       double sum = 0.0D;
/*  220 */       for (int serie = 0; serie < seriesCount; serie++) {
/*  221 */         ChartSample s = getSample(serie, sample);
/*  222 */         if ((s != null) && (s.value != null) && (!s.value.isNaN()) && (s.value.doubleValue() < 0.0D)) {
/*  223 */           sum += s.value.doubleValue();
/*      */         }
/*      */       }
/*  226 */       min = Math.min(min, sum);
/*      */     }
/*  228 */     return min;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   private Rectangle adjustForGridLabels(Rectangle grid)
/*      */   {
/*  238 */     boolean adjust = (isValueLabelsOn(0)) && (this.valueLabelStyle == 1);
/*  239 */     adjust |= ((this.sampleLabelsOn) && (this.sampleLabelStyle == 1));
/*  240 */     adjust |= ((this.seriesLabelsOn) && (this.seriesLabelStyle == 1));
/*  241 */     if ((!adjust) || ((this.barType == 1) && (!isValueLabelsOn()))) {
/*  242 */       return grid;
/*      */     }
/*      */     
/*  245 */     Rectangle newGrid = new Rectangle(grid);
/*      */     
/*  247 */     int angle = 0;
/*  248 */     Font font = getFont("seriesLabelFont");
/*  249 */     if ((this.valueLabelStyle == 1) && (isValueLabelsOn(0))) {
/*  250 */       angle = getLabelAngle("valueLabelAngle");
/*  251 */       font = getFont("seriesLabelFont");
/*      */     }
/*  253 */     if ((this.sampleLabelStyle == 1) && (this.sampleLabelsOn)) {
/*  254 */       angle = getLabelAngle("sampleLabelAngle");
/*  255 */       font = getFont("sampleLabelFont");
/*      */     }
/*  257 */     FontMetrics fm = getFontMetrics(font);
/*      */     
/*  259 */     int seriesCount = getSeriesCount();
/*  260 */     int sampleCount = getSampleCount();
/*  261 */     int topAdjust = 0;
/*  262 */     int bottomAdjust = 0;
/*  263 */     double highest = 0.0D;
/*  264 */     double lowest = 0.0D;
/*  265 */     for (int series = 0; series < seriesCount; series++) {
/*  266 */       for (int sample = 0; sample < sampleCount; sample++) {
/*  267 */         String label = constructLabel(series, sample, 1, this.seriesLabelsOn, null);
/*  268 */         if (this.barType == 1) {
/*  269 */           double value = getSampleValue(series, sample);
/*      */           
/*  271 */           String valueLabel = formatNumber(value, getSampleDecimalCount(0));
/*  272 */           String prefix = getLabel("valueLabelPrefix");
/*  273 */           String postfix = getLabel("valueLabelPostfix");
/*  274 */           valueLabel = prefix != null ? prefix + valueLabel : valueLabel;
/*  275 */           valueLabel = postfix != null ? valueLabel + postfix : valueLabel;
/*      */           
/*  277 */           String sampleLabel = "";
/*  278 */           if ((this.sampleLabelsOn) && (this.sampleLabelStyle == 1)) {
/*  279 */             sampleLabel = getSampleLabel(sample);
/*  280 */             if ((sampleLabel != null) && 
/*  281 */               (!sampleLabel.endsWith("\n")) && (valueLabel != null) && (this.valueLabelStyle == 1)) {
/*  282 */               sampleLabel = sampleLabel + " : ";
/*      */             }
/*      */           }
/*      */           
/*  286 */           label = this.valueLabelStyle == 1 ? sampleLabel + valueLabel : sampleLabel;
/*      */         }
/*  288 */         Dimension labelSize = getLabelSize(label, fm);
/*  289 */         Dimension angledSize = getAngledLabelSize(labelSize, angle);
/*  290 */         double next = getSampleValue(series, sample);
/*  291 */         if (next == highest) {
/*  292 */           if (this.gridAlignment == 1) {
/*  293 */             topAdjust = Math.max(topAdjust, angledSize.height);
/*      */           } else {
/*  295 */             topAdjust = Math.max(topAdjust, fm.stringWidth(label));
/*      */           }
/*      */         }
/*  298 */         if (next > highest) {
/*  299 */           highest = next;
/*  300 */           if (this.gridAlignment == 1) {
/*  301 */             topAdjust = angledSize.height;
/*      */           } else {
/*  303 */             topAdjust = fm.stringWidth(label);
/*      */           }
/*      */         }
/*  306 */         if (getSampleValue(series, sample) < lowest) {
/*  307 */           lowest = getSampleValue(series, sample);
/*  308 */           if (this.gridAlignment == 1) {
/*  309 */             bottomAdjust = angledSize.height;
/*      */           } else {
/*  311 */             bottomAdjust = fm.stringWidth(label);
/*      */           }
/*      */         }
/*      */       }
/*      */     }
/*      */     
/*  317 */     if ((angle % 180 == 0) && (this.gridAlignment == 1)) {
/*  318 */       bottomAdjust -= fm.getDescent();
/*      */     }
/*  320 */     if (this.gridAlignment == 1) {
/*  321 */       newGrid.y += topAdjust;
/*  322 */       newGrid.height -= topAdjust + bottomAdjust;
/*      */     } else {
/*  324 */       newGrid.x += bottomAdjust;
/*  325 */       newGrid.width -= topAdjust + bottomAdjust;
/*      */     }
/*  327 */     return newGrid;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public int getValuePosition(double value)
/*      */   {
/*  339 */     return getValuePosition(0, value, getGraphBounds());
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void setMultiColorOn(boolean on)
/*      */   {
/*  348 */     if (on != this.multiColorOn) {
/*  349 */       this.multiColorOn = on;
/*  350 */       this.needRender = true;
/*  351 */       autoRepaint();
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public boolean isMultiColorOn()
/*      */   {
/*  361 */     return this.multiColorOn;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void setBarLabels(String[] labels)
/*      */   {
/*  372 */     this.barLabels = labels;
/*  373 */     this.needRender = true;
/*  374 */     this.needGraphBounds = true;
/*  375 */     this.needChartCalculation = true;
/*  376 */     autoRepaint();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public String[] getBarLabels()
/*      */   {
/*  386 */     if (this.barLabels != null) {
/*  387 */       return this.barLabels;
/*      */     }
/*  389 */     return getSampleLabels();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void setBarLabelsOn(boolean on)
/*      */   {
/*  400 */     this.barLabelsOn = on;
/*  401 */     this.needRender = true;
/*  402 */     this.needGraphBounds = true;
/*  403 */     this.needChartCalculation = true;
/*  404 */     autoRepaint();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public boolean isBarLabelsOn()
/*      */   {
/*  413 */     return this.barLabelsOn;
/*      */   }
/*      */   
/*      */   /**
/*      */    * @deprecated
/*      */    */
/*      */   public void setBarLabelStyle(int style)
/*      */   {
/*  421 */     super.setSampleLabelStyle(style);
/*      */   }
/*      */   
/*      */   /**
/*      */    * @deprecated
/*      */    */
/*      */   public int getBarLabelStyle()
/*      */   {
/*  429 */     return super.getSampleLabelStyle();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public synchronized void setBarLabelColor(int index, Color color)
/*      */   {
/*      */     try
/*      */     {
/*  442 */       this.barLabelColors[index] = color;
/*  443 */       autoRepaint();
/*      */     } catch (IndexOutOfBoundsException e) {
/*  445 */       throw new IllegalArgumentException("Invalid sample index: " + index);
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public Color getBarLabelColor(int index)
/*      */   {
/*  457 */     if (this.barLabels != null) {
/*      */       try {
/*  459 */         return this.barLabelColors[index];
/*      */       } catch (IndexOutOfBoundsException e) {
/*  461 */         throw new IllegalArgumentException("Invalid sample index: " + index);
/*      */       }
/*      */     }
/*  464 */     return null;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void setBarOutlineOn(boolean on)
/*      */   {
/*  475 */     if (this.barOutlineOn != on) {
/*  476 */       this.barOutlineOn = on;
/*  477 */       this.needRender = true;
/*  478 */       autoRepaint();
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public boolean isBarOutlineOn()
/*      */   {
/*  489 */     return this.barOutlineOn;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void setBarOutlineColor(Color color)
/*      */   {
/*  499 */     if (color != this.barOutlineColor) {
/*  500 */       this.barOutlineColor = color;
/*  501 */       this.needRender = true;
/*  502 */       autoRepaint();
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void setBarAlignment(int alignment)
/*      */   {
/*  513 */     if (alignment != this.gridAlignment) {
/*  514 */       if ((alignment == 0) || (alignment == 1)) {
/*  515 */         this.gridAlignment = alignment;
/*  516 */         this.needRender = true;
/*  517 */         this.needGraphBounds = true;
/*  518 */         this.needChartCalculation = true;
/*  519 */         autoRepaint();
/*      */       } else {
/*  521 */         throw new IllegalArgumentException("Alignment must be HORIZONTAL or VERTICAL");
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public int getgridAlignment()
/*      */   {
/*  533 */     return this.gridAlignment;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void setBarType(int type)
/*      */   {
/*  543 */     if (type != this.barType) {
/*  544 */       if ((type == 0) || (type == 1)) {
/*  545 */         this.barType = type;
/*  546 */         this.needRender = true;
/*  547 */         this.needChartCalculation = true;
/*  548 */         autoRepaint();
/*      */       } else {
/*  550 */         throw new IllegalArgumentException("Invalid bar type: " + type);
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public int getBarType()
/*      */   {
/*  561 */     return this.barType;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void setAutoLabelSpacingOn(boolean on)
/*      */   {
/*  572 */     if (on != this.autoLabelSpacingOn) {
/*  573 */       this.autoLabelSpacingOn = on;
/*  574 */       this.needRender = true;
/*  575 */       autoRepaint();
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
/*  586 */     return this.autoLabelSpacingOn;
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
/*      */   public void setBarWidth(double width)
/*      */   {
/*  599 */     if (width != this.barWidthFraction) {
/*  600 */       width = Math.max(0.0D, width);
/*  601 */       width = Math.min(1.0D, width);
/*  602 */       this.barWidthFraction = width;
/*  603 */       this.needRender = true;
/*  604 */       this.needChartCalculation = true;
/*  605 */       autoRepaint();
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public double getBarWidth()
/*      */   {
/*  616 */     return this.barWidthFraction;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public Rectangle getBarBounds(int serie, int sample)
/*      */   {
/*  627 */     if ((this.barBounds != null) && (serie >= 0) && (sample >= 0) && (serie < getSeriesCount()) && (sample < getSampleCount())) {
/*  628 */       return this.barBounds[serie][sample];
/*      */     }
/*  630 */     return null;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void setMultiSeriesOn(boolean state)
/*      */   {
/*  642 */     if (state != this.multiSeriesOn) {
/*  643 */       this.multiSeriesOn = state;
/*  644 */       this.needRender = true;
/*  645 */       this.needGraphBounds = true;
/*  646 */       this.needChartCalculation = true;
/*  647 */       autoRepaint();
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public boolean isMultiSeriesOn()
/*      */   {
/*  658 */     return this.multiSeriesOn;
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
/*      */   public ChartSample checkSelection(Point point)
/*      */   {
/*  673 */     if (point == null) {
/*  674 */       return null;
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*  679 */     ChartSample foundSample = null;
/*  680 */     if (this.overlayCharts != null) {
/*  681 */       int overlay_count = this.overlayCharts.size();
/*  682 */       for (int i = overlay_count - 1; (i >= 0) && (foundSample == null); i--) {
/*  683 */         Chart overlay = (Chart)this.overlayCharts.elementAt(i);
/*  684 */         if (overlay != null) {
/*  685 */           foundSample = overlay.checkSelection(point);
/*      */         }
/*      */       }
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*  692 */     if (foundSample != null) {
/*  693 */       this.mouseOverSampleIndex = -1;
/*  694 */       this.mouseOverSeriesIndex = -1;
/*  695 */       return foundSample;
/*      */     }
/*      */     
/*      */ 
/*  699 */     int seriesCount = getSeriesCount();
/*  700 */     int sampleCount = getSampleCount();
/*  701 */     boolean from_legend = false;
/*  702 */     if ((isLegendOn()) && (this.legendBounds != null) && (this.visibleLegend.contains(point))) {
/*  703 */       for (int i = 0; i < this.legendBounds.length; i++) {
/*  704 */         if ((this.legendBounds[i] != null) && (this.legendBounds[i].contains(point))) {
/*  705 */           if ((seriesCount > 1) || (this.barType == 1) || (this.multiSeriesOn)) {
/*  706 */             foundSample = new ChartSample(-1);
/*  707 */             foundSample.setSeries(i);
/*  708 */           } else if (i < sampleCount) {
/*  709 */             foundSample = getSample(0, i);
/*  710 */             if (foundSample != null) {
/*  711 */               foundSample.setSeries(-1);
/*      */             }
/*      */           }
/*  714 */           from_legend = true;
/*  715 */           break;
/*      */         }
/*      */       }
/*      */       
/*      */ 
/*      */ 
/*  721 */       if ((foundSample != null) && (this.overlayCharts != null)) {
/*  722 */         int overlay_count = this.overlayCharts.size();
/*  723 */         for (int i = overlay_count - 1; i >= 0; i--) {
/*  724 */           Chart overlay = (Chart)this.overlayCharts.elementAt(i);
/*  725 */           if (overlay != null) {
/*  726 */             overlay.mouseOverSampleIndex = foundSample.getIndex();
/*  727 */             overlay.mouseOverSeriesIndex = foundSample.getSeries();
/*  728 */             if (this.gridAlignment == 1) {
/*  729 */               overlay.mousePosition.x = this.mousePosition.x;
/*  730 */               overlay.mousePosition.y = -1;
/*      */             } else {
/*  732 */               overlay.mousePosition.x = -1;
/*  733 */               overlay.mousePosition.y = this.mousePosition.y;
/*      */             }
/*      */           }
/*      */         }
/*      */       }
/*      */     }
/*      */     
/*      */ 
/*  741 */     if (this.barBounds != null)
/*      */     {
/*      */ 
/*      */ 
/*  745 */       int samplesToCheck = sampleCount;
/*  746 */       int seriesToCheck; for (; (foundSample == null) && (samplesToCheck != 0); 
/*      */           
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  754 */           (foundSample == null) && (seriesToCheck != 0))
/*      */       {
/*  747 */         int sample = this.gridAlignment == 0 ? getSampleCount() - samplesToCheck : samplesToCheck - 1;
/*  748 */         samplesToCheck--;
/*      */         
/*      */ 
/*      */ 
/*      */ 
/*  753 */         seriesToCheck = seriesCount;
/*  754 */         continue;
/*      */         
/*  756 */         int serie = 0;
/*  757 */         if (this.barType == 1) {
/*  758 */           serie = seriesToCheck - 1;
/*      */         }
/*  760 */         else if (this.gridAlignment == 0) {
/*  761 */           serie = getSeriesCount() - seriesToCheck;
/*      */         } else {
/*  763 */           serie = seriesToCheck - 1;
/*      */         }
/*      */         
/*  766 */         seriesToCheck--;
/*      */         
/*  768 */         Rectangle bar = null;
/*      */         try {
/*  770 */           bar = this.barBounds[serie][sample];
/*      */         }
/*      */         catch (NullPointerException localNullPointerException) {}catch (IndexOutOfBoundsException localIndexOutOfBoundsException) {}
/*      */         
/*      */ 
/*      */ 
/*      */ 
/*  777 */         if (bar != null)
/*      */         {
/*      */ 
/*      */ 
/*  781 */           Rectangle tip_area = new Rectangle(bar);
/*  782 */           if ((this.barType != 1) || (seriesCount <= 1)) {
/*  783 */             if ((this.gridAlignment == 1) && (tip_area.height < 3)) {
/*  784 */               tip_area.y -= 3 - tip_area.height;
/*  785 */               tip_area.height = 6;
/*  786 */             } else if ((this.gridAlignment == 0) && (tip_area.width < 3)) {
/*  787 */               tip_area.x -= 3 - tip_area.width;
/*  788 */               tip_area.width = 6;
/*      */             }
/*      */           }
/*  791 */           if (tip_area.contains(point)) {
/*  792 */             foundSample = getSample(serie, sample);
/*      */           }
/*      */           
/*      */ 
/*  796 */           if ((foundSample == null) && (is3DModeOn()))
/*      */           {
/*      */ 
/*  799 */             boolean check_top = (this.gridAlignment != 1) || (this.barType != 1) || (serie == 0);
/*  800 */             check_top |= ((this.upperRange[0] > this.lowerRange[0]) && (getSampleValues(serie)[sample] > 0.0D));
/*  801 */             check_top |= ((this.upperRange[0] < this.lowerRange[0]) && (getSampleValues(serie)[sample] < 0.0D));
/*  802 */             if (check_top) {
/*  803 */               Polygon top = new Polygon();
/*  804 */               top.addPoint(bar.x, bar.y);
/*  805 */               top.addPoint(top.xpoints[0] + this.depth3dPoint.x, top.ypoints[0] + this.depth3dPoint.y);
/*  806 */               top.addPoint(top.xpoints[1] + bar.width, top.ypoints[1]);
/*  807 */               top.addPoint(bar.x + bar.width, bar.y);
/*  808 */               if (top.contains(point)) {
/*  809 */                 foundSample = getSample(serie, sample);
/*      */               }
/*      */             }
/*      */             
/*      */ 
/*      */ 
/*  815 */             boolean check_right = (this.gridAlignment != 0) || (this.barType != 1) || (serie == 0);
/*  816 */             check_top |= ((this.upperRange[0] > this.lowerRange[0]) && (getSampleValues(serie)[sample] > 0.0D));
/*  817 */             check_top |= ((this.upperRange[0] < this.lowerRange[0]) && (getSampleValues(serie)[sample] < 0.0D));
/*  818 */             if (check_right) {
/*  819 */               Polygon right = new Polygon();
/*  820 */               right.addPoint(bar.x + bar.width - 1, bar.y);
/*  821 */               right.addPoint(bar.x + this.depth3dPoint.x + bar.width, bar.y + this.depth3dPoint.y);
/*  822 */               right.addPoint(bar.x + this.depth3dPoint.x + bar.width, bar.y + this.depth3dPoint.y + bar.height);
/*  823 */               right.addPoint(bar.x + bar.width - 1, bar.y + bar.height);
/*  824 */               if (right.contains(point)) {
/*  825 */                 foundSample = getSample(serie, sample);
/*      */               }
/*      */             }
/*      */           }
/*      */         }
/*      */       }
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*  835 */     if (foundSample != null) {
/*  836 */       this.mouseOverSampleIndex = foundSample.getIndex();
/*  837 */       this.mouseOverSeriesIndex = foundSample.getSeries();
/*  838 */       this.mousePosition = point;
/*  839 */       if (from_legend) {
/*  840 */         if (this.gridAlignment == 1) {
/*  841 */           this.mousePosition.y = -1;
/*      */         } else {
/*  843 */           this.mousePosition.x = -1;
/*      */         }
/*      */       }
/*      */     } else {
/*  847 */       this.mouseOverSampleIndex = -1;
/*  848 */       this.mouseOverSeriesIndex = -1;
/*      */     }
/*  850 */     return foundSample;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public String[] getLegendLabels()
/*      */   {
/*  860 */     if (this.legendLabels != null)
/*  861 */       return this.legendLabels;
/*  862 */     if ((getSeriesCount() > 1) || (this.barType == 1) || (this.multiSeriesOn)) {
/*  863 */       return getSeriesLabels();
/*      */     }
/*  865 */     return getSampleLabels();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   private Polygon get3dBarTop(Rectangle bar)
/*      */   {
/*  876 */     Polygon top = new Polygon();
/*  877 */     top.addPoint(bar.x, bar.y);
/*  878 */     top.addPoint(top.xpoints[0] + this.depth3dPoint.x, top.ypoints[0] + this.depth3dPoint.y);
/*  879 */     top.addPoint(top.xpoints[1] + bar.width, top.ypoints[1]);
/*  880 */     top.addPoint(bar.x + bar.width, bar.y);
/*  881 */     return top;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   private Polygon get3dBarRight(Rectangle bar)
/*      */   {
/*  891 */     Polygon right = new Polygon();
/*  892 */     right.addPoint(bar.x + bar.width, bar.y);
/*  893 */     right.addPoint(bar.x + this.depth3dPoint.x + bar.width, bar.y + this.depth3dPoint.y);
/*  894 */     right.addPoint(bar.x + this.depth3dPoint.x + bar.width, bar.y + this.depth3dPoint.y + bar.height);
/*  895 */     right.addPoint(bar.x + bar.width, bar.y + bar.height);
/*  896 */     return right;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void render(Graphics g)
/*      */   {
/*  905 */     render(g, !isServletModeOn());
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
/*  916 */     this.graphBounds = getGraphBounds();
/*  917 */     Rectangle grid = this.graphBounds;
/*  918 */     Rectangle dataBounds = getDataBounds(grid);
/*  919 */     calculateChartData(grid, dataBounds);
/*  920 */     this.lastCalculationTime = System.currentTimeMillis();
/*  921 */     for (int i = 0; i < this.overlayCharts.size(); i++) {
/*  922 */       Chart overlay = (Chart)this.overlayCharts.elementAt(i);
/*  923 */       if ((overlay != null) && (isOverlayChartOn(i)))
/*      */       {
/*      */ 
/*  926 */         Rectangle overlayGrid = new Rectangle(grid);
/*  927 */         Rectangle overlayDataBounds = new Rectangle(dataBounds);
/*  928 */         if (is3DModeOn()) {
/*  929 */           overlayGrid.x -= this.depth3dPoint.x;
/*  930 */           overlayGrid.y -= this.depth3dPoint.y;
/*  931 */           overlayDataBounds.x -= this.depth3dPoint.x;
/*  932 */           overlayDataBounds.y -= this.depth3dPoint.y;
/*      */         }
/*  934 */         if ((overlay instanceof LineChart)) {
/*  935 */           overlay.calculateChartData(overlayGrid, getLineChartBounds(overlayDataBounds));
/*      */         } else {
/*  937 */           overlay.calculateChartData(overlayGrid, overlayDataBounds);
/*      */         }
/*      */       }
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*  944 */     Dimension size = getSize();
/*  945 */     if ((offscreenOn) && ((this.offscreen == null) || (this.offscreen.getWidth(this) < size.width) || (this.offscreen.getHeight(this) < size.height))) {
/*      */       try {
/*  947 */         this.offscreen = createImage(Math.max(1, size.width), Math.max(1, size.height));
/*      */       } catch (Throwable e) {
/*  949 */         Chart.fifo_clear();
/*      */       }
/*      */       
/*      */ 
/*  953 */       this.needRender = true;
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*  958 */     if ((!offscreenOn) || (this.needRender) || (this.offscreen == null))
/*      */     {
/*  960 */       Graphics gc = g;
/*  961 */       if ((this.offscreen != null) && (offscreenOn) && (!this.externalGraphicsOn)) {
/*  962 */         gc = this.offscreen.getGraphics();
/*  963 */       } else if ((this.externalGraphicsOn) && (this.external_gc != null) && (this.offscreen != null)) {
/*  964 */         gc = this.external_gc;
/*      */       }
/*  966 */       gc.setColor(getBackground());
/*  967 */       gc.fillRect(0, 0, size.width, size.height);
/*      */       
/*      */ 
/*  970 */       paintTitle(gc, size);
/*  971 */       if (isLegendOn()) {
/*  972 */         paintLegend(gc, grid, getLegendLabels());
/*      */       }
/*  974 */       paintGrid(gc, grid);
/*  975 */       paintBarLabels(gc, grid);
/*  976 */       if (this.upperRange != this.lowerRange) {
/*  977 */         renderData(gc, grid, dataBounds);
/*      */         
/*  979 */         for (int i = 0; i < this.overlayCharts.size(); i++) {
/*  980 */           Chart overlay = (Chart)this.overlayCharts.elementAt(i);
/*  981 */           if ((overlay != null) && (isOverlayChartOn(i)))
/*      */           {
/*      */ 
/*  984 */             Rectangle overlayGrid = new Rectangle(grid);
/*  985 */             Rectangle overlayDataBounds = new Rectangle(dataBounds);
/*  986 */             overlayGrid.x -= this.depth3dPoint.x;
/*  987 */             overlayGrid.y -= this.depth3dPoint.y;
/*  988 */             overlayDataBounds.x -= this.depth3dPoint.x;
/*  989 */             overlayDataBounds.y -= this.depth3dPoint.y;
/*  990 */             if ((overlay instanceof LineChart)) {
/*  991 */               overlay.renderData(gc, overlayGrid, getLineChartBounds(overlayDataBounds));
/*  992 */             } else if ((overlay instanceof PieChart)) {
/*  993 */               overlay.renderData(gc, overlayGrid, overlayDataBounds);
/*      */             } else {
/*  995 */               overlay.renderData(gc, grid, dataBounds);
/*      */             }
/*      */           }
/*      */         }
/*      */       }
/*      */       
/* 1001 */       if ((!this.externalGraphicsOn) && (gc != g)) {
/* 1002 */         gc.dispose();
/*      */       }
/*      */       
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 1012 */       if (!this.chartData.hasChangedSince(this.lastCalculationTime)) {
/* 1013 */         this.needRender = false;
/*      */       }
/*      */     }
/*      */     
/*      */ 
/* 1018 */     if (((offscreenOn) || (this.externalGraphicsOn)) && (this.offscreen != null)) {
/* 1019 */       g.drawImage(this.offscreen, 0, 0, this);
/*      */     }
/*      */     
/*      */ 
/* 1023 */     if (isZoomOn()) {
/* 1024 */       paintZoomOutButton(g, grid);
/* 1025 */       paintMouseBox(g);
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/* 1031 */     paintFloatingLabels(g, grid);
/*      */     
/*      */ 
/* 1034 */     paintLabels(g);
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
/* 1045 */     paintBars(g, grid);
/*      */     
/*      */ 
/* 1048 */     if (is3DModeOn()) {
/* 1049 */       for (int i = this.rangeOn.length - 1; i >= 0; i--) {
/* 1050 */         paint3DZeroDivider(g, grid, i);
/*      */       }
/*      */     }
/* 1053 */     paintStaticLabels(g, grid);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected void checkDataIntegrity()
/*      */   {
/* 1062 */     super.checkDataIntegrity();
/*      */     
/* 1064 */     int sampleCount = this.chartData.getSampleCount();
/* 1065 */     if (sampleCount != this.barLabelColors.length) {
/* 1066 */       this.needRender = true;
/* 1067 */       Color[] newBarLabelColors = new Color[sampleCount];
/* 1068 */       int count = Math.min(sampleCount, this.barLabelColors.length);
/* 1069 */       System.arraycopy(this.barLabelColors, 0, newBarLabelColors, 0, count);
/* 1070 */       this.barLabelColors = newBarLabelColors;
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   private Rectangle getLineChartBounds(Rectangle dataBounds)
/*      */   {
/* 1081 */     Rectangle lineBounds = new Rectangle(dataBounds);
/* 1082 */     int seriesCount = 1;
/* 1083 */     if (this.barType != 1) {
/* 1084 */       seriesCount = getSeriesCount();
/*      */     }
/* 1086 */     if ((getSampleCount() > 1) && (this.barBounds != null) && (this.barBounds.length > 0) && (this.barBounds[0] != null) && (this.barBounds[0].length > 0)) {
/* 1087 */       int change = this.barBounds[0][0].x + this.barBounds[0][0].width * seriesCount / 2 - dataBounds.x;
/* 1088 */       lineBounds.x += change;
/* 1089 */       lineBounds.width -= change * 2;
/*      */     }
/* 1091 */     return lineBounds;
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
/* 1103 */     Rectangle bounds = new Rectangle();
/* 1104 */     if (this.graphBounds != null) {
/* 1105 */       bounds = new Rectangle(this.graphBounds);
/*      */     }
/* 1107 */     if (is3DModeOn()) {
/* 1108 */       bounds.x -= this.depth3dPoint.x;
/* 1109 */       bounds.y -= this.depth3dPoint.y;
/*      */     }
/*      */     
/*      */ 
/* 1113 */     Rectangle b = getBarBounds(serie, sample);
/*      */     
/*      */ 
/* 1116 */     if (b.y < bounds.y) {
/* 1117 */       b.height -= bounds.y - b.y + 1;
/* 1118 */       b.y = bounds.y;
/*      */     }
/* 1120 */     if (b.y + b.height > bounds.y + bounds.height) {
/* 1121 */       b.height -= b.y + b.height - (bounds.y + bounds.height);
/*      */     }
/*      */     
/* 1124 */     if (b.x + b.width > bounds.x + bounds.width) {
/* 1125 */       b.width = (bounds.x + bounds.width - b.x);
/*      */     }
/*      */     
/* 1128 */     if (b.x < bounds.x) {
/* 1129 */       b.width -= bounds.x - b.x + 2;
/* 1130 */       b.x = bounds.x;
/*      */     }
/*      */     
/*      */ 
/* 1134 */     Point centerPoint = new Point(b.x + b.width / 2, b.y + b.height / 2);
/*      */     
/*      */ 
/* 1137 */     if (bounds.contains(centerPoint)) {
/* 1138 */       return centerPoint;
/*      */     }
/* 1140 */     return null;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public Rectangle getGraphBounds()
/*      */   {
/* 1150 */     if ((!this.needGraphBounds) && (this.currentBounds != null)) {
/* 1151 */       return this.currentBounds;
/*      */     }
/*      */     
/*      */ 
/* 1155 */     Rectangle bounds = super.getGraphBounds(getLegendLabels());
/*      */     
/*      */ 
/* 1158 */     Dimension size = getSize();
/* 1159 */     int left = bounds.x;
/* 1160 */     int right = size.width - bounds.width - bounds.x;
/* 1161 */     int top = bounds.y;
/* 1162 */     int bottom = size.height - bounds.height - bounds.y;
/*      */     
/*      */ 
/* 1165 */     boolean adjust_for_sample = this.sampleLabelsOn;
/* 1166 */     adjust_for_sample &= ((this.sampleLabelStyle == 2) || (this.sampleLabelStyle == 4));
/* 1167 */     int bottom_offset = 0;
/* 1168 */     if ((this.barLabelsOn) || (adjust_for_sample))
/*      */     {
/* 1170 */       String[] labels = getSampleLabels();
/* 1171 */       if (this.barLabelsOn) {
/* 1172 */         labels = getBarLabels();
/*      */       }
/*      */       
/*      */ 
/* 1176 */       Font barLabelFont = getFont("sampleLabelFont");
/* 1177 */       int angle = getLabelAngle("sampleLabelAngle");
/* 1178 */       if (this.barLabelsOn) {
/* 1179 */         barLabelFont = getFont("barLabelFont");
/* 1180 */         angle = getLabelAngle("barLabelAngle");
/*      */       }
/* 1182 */       FontMetrics fm = getFontMetrics(barLabelFont);
/*      */       
/*      */ 
/* 1185 */       if (this.gridAlignment == 1)
/*      */       {
/* 1187 */         int height = 0;
/*      */         
/*      */ 
/* 1190 */         for (int i = 0; i < labels.length; i++) {
/* 1191 */           Dimension labelSize = getLabelSize(labels[i], fm);
/* 1192 */           Dimension angledSize = getAngledLabelSize(labelSize, angle);
/* 1193 */           height = Math.max(angledSize.height, height);
/*      */         }
/* 1195 */         bottom_offset = height;
/* 1196 */         if (angle % 180 == 0) {
/* 1197 */           bottom_offset -= fm.getMaxDescent();
/*      */         }
/* 1199 */         bottom += bottom_offset;
/*      */       }
/*      */       else
/*      */       {
/* 1203 */         int widest = 0;
/*      */         
/*      */ 
/*      */ 
/* 1207 */         for (int i = 0; i < labels.length; i++) {
/* 1208 */           Dimension labelSize = getLabelSize(labels[i], fm);
/* 1209 */           Dimension angledSize = getAngledLabelSize(labelSize, angle);
/* 1210 */           widest = Math.max(widest, angledSize.width);
/*      */         }
/* 1212 */         bottom_offset = widest;
/* 1213 */         left += widest;
/*      */       }
/*      */     }
/*      */     
/*      */ 
/* 1218 */     if (this.sampleScrollerOn) {
/* 1219 */       bottom_offset += 10;
/* 1220 */       if (this.gridAlignment == 1) {
/* 1221 */         bottom += 10;
/*      */       } else {
/* 1223 */         left += 10;
/*      */       }
/*      */     }
/*      */     
/*      */ 
/* 1228 */     Rectangle newGrid = new Rectangle(left, top, size.width - left - right, size.height - top - bottom);
/* 1229 */     newGrid = adjustForGridLabels(newGrid);
/* 1230 */     left = newGrid.x;
/* 1231 */     right = size.width - newGrid.width - newGrid.x;
/* 1232 */     top = newGrid.y;
/* 1233 */     bottom = size.height - newGrid.height - newGrid.y;
/*      */     
/*      */ 
/* 1236 */     String sampleAxisLabel = getLabel("sampleAxisLabel");
/* 1237 */     if (sampleAxisLabel != null) {
/* 1238 */       int angle = getLabelAngle("sampleAxisLabelAngle");
/* 1239 */       angle = this.gridAlignment == 0 ? angle : 0;
/* 1240 */       Font sampleAxisLabelFont = getFont("sampleAxisLabelFont");
/* 1241 */       FontMetrics fm = getFontMetrics(sampleAxisLabelFont);
/* 1242 */       Dimension labelSize = getLabelSize(sampleAxisLabel, fm);
/* 1243 */       Dimension angledSize = getAngledLabelSize(labelSize, angle);
/* 1244 */       if (this.gridAlignment == 1) {
/* 1245 */         bottom += angledSize.height + 3;
/*      */       } else {
/* 1247 */         left += angledSize.width + 5;
/*      */       }
/*      */     }
/*      */     
/*      */ 
/* 1252 */     if (this.gridAlignment == 0) {
/* 1253 */       int upper_width = 0;
/* 1254 */       int lower_width = 0;
/* 1255 */       for (int range = 0; range < this.rangeOn.length; range++)
/* 1256 */         if ((this.rangeOn[range] != 0) && (this.rangeLabelsOn[range] != 0))
/*      */         {
/*      */ 
/*      */ 
/*      */ 
/* 1261 */           FontMetrics fm = getFontMetrics(getFont("rangeLabelFont"));
/* 1262 */           String prefix = null;
/* 1263 */           String postfix = null;
/* 1264 */           if (range == 0) {
/* 1265 */             prefix = getLabel("rangeLabelPrefix");
/* 1266 */             postfix = getLabel("rangeLabelPostfix");
/*      */           } else {
/* 1268 */             prefix = getLabel("rangeLabelPrefix_" + (range + 1));
/* 1269 */             postfix = getLabel("rangeLabelPostfix_" + (range + 1));
/*      */           }
/*      */           
/*      */ 
/* 1273 */           int decimals = getRangeDecimalCount(range);
/* 1274 */           String upper = formatNumber(this.upperRange[range], decimals);
/* 1275 */           String lower = formatNumber(this.lowerRange[range], decimals);
/* 1276 */           if (this.upperRange[range] < this.lowerRange[range]) {
/* 1277 */             upper = formatNumber(this.lowerRange[range], decimals);
/* 1278 */             lower = formatNumber(this.upperRange[range], decimals);
/*      */           }
/* 1280 */           upper = prefix != null ? prefix + upper : upper;
/* 1281 */           lower = prefix != null ? prefix + lower : lower;
/* 1282 */           upper = postfix != null ? upper + postfix : upper;
/* 1283 */           lower = postfix != null ? lower + postfix : lower;
/* 1284 */           upper_width = Math.max(fm.stringWidth(upper), upper_width);
/* 1285 */           lower_width = Math.max(fm.stringWidth(lower), lower_width);
/*      */         }
/* 1287 */       left = Math.max(lower_width / 2 + 5, left);
/* 1288 */       right = Math.max(upper_width / 2 + 5, right);
/*      */     }
/*      */     
/*      */ 
/* 1292 */     if (is3DModeOn())
/*      */     {
/* 1294 */       if (this.depth3d >= 0) {
/* 1295 */         bottom += this.depth3d;
/* 1296 */         left += this.depth3d;
/*      */ 
/*      */       }
/*      */       else
/*      */       {
/* 1301 */         int barCount = Math.max(1, getSampleCount());
/* 1302 */         if (this.barType == 0) {
/* 1303 */           barCount *= Math.max(1, getSeriesCount());
/*      */         }
/*      */         
/* 1306 */         if (this.gridAlignment == 1)
/*      */         {
/* 1308 */           bounds.width = (size.width - left - right);
/* 1309 */           double effect3d = 0.0D;
/* 1310 */           if (barCount + this.barWidthFraction * 1.25D > 0.0D) {
/* 1311 */             effect3d = bounds.width * this.barWidthFraction * 1.25D / (
/* 1312 */               barCount + this.barWidthFraction * 1.25D);
/*      */           }
/*      */           
/* 1315 */           left = (int)(left + Math.round(effect3d));
/* 1316 */           bounds.width = (size.width - left - right);
/* 1317 */           double d = 0.0D;
/* 1318 */           if (barCount > 0) {
/* 1319 */             d = bounds.width / barCount * this.barWidthFraction;
/*      */           }
/* 1321 */           bottom += (int)Math.round(d / 1.25D);
/*      */         }
/*      */         else {
/* 1324 */           int effect3d = 0;
/* 1325 */           if (1.25F * barCount + this.barWidthFraction * 0.75D > 0.0D) {
/* 1326 */             effect3d = (int)(bounds.height * this.barWidthFraction * 0.75D / (
/* 1327 */               1.25F * barCount + this.barWidthFraction * 0.75D));
/*      */           }
/*      */           
/* 1330 */           bottom += effect3d;
/* 1331 */           bounds.height = (size.height - top - bottom);
/* 1332 */           double d = bounds.height / barCount * this.barWidthFraction;
/* 1333 */           left += (int)Math.round(d * 1.25D * 0.75D);
/*      */         }
/*      */       }
/*      */     }
/*      */     
/*      */ 
/*      */ 
/* 1340 */     Insets insets = getGraphInsets();
/* 1341 */     boolean range_on = false;
/* 1342 */     int left_offset = 0;
/* 1343 */     int right_offset = 0;
/* 1344 */     for (int i = 0; i < this.rangeOn.length; i++) {
/* 1345 */       range_on = this.rangeOn[i] != 0 ? true : range_on;
/*      */     }
/* 1347 */     if ((range_on) || (this.targetsLabel.size() > 0))
/*      */     {
/* 1349 */       Rectangle currentGrid = new Rectangle();
/* 1350 */       int cur_top = insets.top == -1 ? top : insets.top;
/* 1351 */       int cur_left = insets.left == -1 ? left : insets.left;
/* 1352 */       int cur_bottom = insets.bottom == -1 ? bottom : insets.bottom;
/* 1353 */       int cur_right = insets.right == -1 ? right : insets.right;
/* 1354 */       currentGrid.x = cur_left;
/* 1355 */       currentGrid.width = (size.width - cur_left - cur_right - 1);
/* 1356 */       currentGrid.y = cur_top;
/* 1357 */       currentGrid.height = (size.height - cur_top - cur_bottom - 1);
/* 1358 */       calculateRangeBounds(currentGrid);
/*      */       
/*      */ 
/*      */ 
/* 1362 */       int left_inner_range = -1;
/* 1363 */       int right_inner_range = -1;
/* 1364 */       for (int i = this.rangeBounds.length - 1; i >= 0; i--) {
/* 1365 */         if (this.rangePosition[i] == 0) {
/* 1366 */           left_inner_range = i;
/* 1367 */           left_offset += (this.gridAlignment == 1 ? this.rangeBounds[i].width : this.rangeBounds[i].height);
/* 1368 */         } else if (this.rangePosition[i] == 1) {
/* 1369 */           right_inner_range = i;
/* 1370 */           right_offset += (this.gridAlignment == 1 ? this.rangeBounds[i].width : this.rangeBounds[i].height);
/*      */         }
/*      */       }
/*      */       
/*      */ 
/* 1375 */       if ((this.targetLabelsPosition == 0) && (left_inner_range == -1)) {
/* 1376 */         left_offset += getRangeWidth(-1, false, true);
/* 1377 */       } else if ((this.targetLabelsPosition == 1) && (right_inner_range == -1)) {
/* 1378 */         right_offset += getRangeWidth(-1, false, true);
/*      */       }
/*      */       
/*      */ 
/* 1382 */       if ((this.rangeOn.length == 1) && (this.rangeOn[0] != 0) && (this.rangeAdjusterOn[0] != 0)) {
/* 1383 */         if ((this.rangePosition[0] == 0) && (this.rangeAdjusterPosition == 1)) {
/* 1384 */           right_offset += 6;
/* 1385 */         } else if ((this.rangePosition[0] == 1) && (this.rangeAdjusterPosition == 0)) {
/* 1386 */           left_offset += 6;
/*      */         }
/*      */       }
/*      */     }
/*      */     
/*      */ 
/* 1392 */     if (this.gridAlignment == 1) {
/* 1393 */       if (insets.left != -1) {
/* 1394 */         for (int index = this.rangeBounds.length - 1; index >= 0; index--) {
/* 1395 */           this.rangeBounds[index].x -= (this.rangePosition[index] == 0 ? left_offset : 0);
/*      */         }
/*      */       }
/* 1398 */       if (insets.right != -1) {
/* 1399 */         for (int index = this.rangeBounds.length - 1; index >= 0; index--) {
/* 1400 */           this.rangeBounds[index].x += (this.rangePosition[index] == 1 ? right_offset : 0);
/*      */         }
/*      */       }
/*      */     } else {
/* 1404 */       if (insets.top != -1) {
/* 1405 */         for (int index = this.rangeBounds.length - 1; index >= 0; index--) {
/* 1406 */           this.rangeBounds[index].y -= (this.rangePosition[index] == 1 ? right_offset : 0);
/*      */         }
/*      */       }
/* 1409 */       if (insets.bottom != -1) {
/* 1410 */         for (int index = this.rangeBounds.length - 1; index >= 0; index--) {
/* 1411 */           this.rangeBounds[index].y += (this.rangePosition[index] == 0 ? left_offset : 0);
/*      */         }
/*      */       }
/*      */     }
/*      */     
/*      */ 
/* 1417 */     if (insets != null) {
/* 1418 */       if (this.gridAlignment == 1) {
/* 1419 */         left = insets.left == -1 ? left + left_offset : insets.left;
/* 1420 */         right = insets.right == -1 ? right + right_offset : insets.right;
/* 1421 */         top = insets.top == -1 ? top : insets.top;
/* 1422 */         bottom = insets.bottom == -1 ? bottom : insets.bottom;
/*      */       } else {
/* 1424 */         left = insets.left == -1 ? left : insets.left;
/* 1425 */         right = insets.right == -1 ? right : insets.right;
/* 1426 */         top = insets.top == -1 ? top + right_offset : insets.top;
/* 1427 */         bottom = insets.bottom == -1 ? bottom + left_offset : insets.bottom;
/*      */       }
/*      */     }
/*      */     
/*      */ 
/* 1432 */     bounds.x = left;
/* 1433 */     bounds.width = (size.width - left - right - 1);
/* 1434 */     bounds.y = top;
/* 1435 */     bounds.height = (size.height - top - bottom - 1);
/*      */     
/*      */ 
/* 1438 */     switch (getLegendPosition())
/*      */     {
/*      */     case 1: 
/*      */     default: 
/* 1442 */       this.visibleLegend.x = (bounds.x + bounds.width + (this.gridAlignment == 1 ? right_offset : 0) + 15);
/* 1443 */       this.visibleLegend.x = Math.max(this.legend.x, this.visibleLegend.x);
/* 1444 */       this.visibleLegend.y = this.legend.y;
/* 1445 */       this.visibleLegend.width = (size.width - this.visibleLegend.x - 1);
/* 1446 */       this.visibleLegend.height = (size.height - this.legend.y - 12);
/*      */       
/* 1448 */       if (this.legend.width > this.visibleLegend.width) {
/* 1449 */         this.visibleLegend.height = Math.min(this.legend.height, this.visibleLegend.height - 9);
/*      */       }
/* 1451 */       if (this.legend.height > this.visibleLegend.height) {
/* 1452 */         this.visibleLegend.width = Math.min(this.legend.width, this.visibleLegend.width - 9);
/*      */       }
/* 1454 */       break;
/*      */     
/*      */ 
/*      */     case 0: 
/* 1458 */       this.visibleLegend.x = this.legend.x;
/* 1459 */       this.visibleLegend.y = this.legend.y;
/* 1460 */       this.visibleLegend.width = (bounds.x - (this.gridAlignment == 1 ? left_offset : bottom_offset) - 20);
/* 1461 */       this.visibleLegend.height = (size.height - this.legend.y - 12);
/*      */       
/* 1463 */       if (this.legend.width > this.visibleLegend.width) {
/* 1464 */         this.visibleLegend.height = Math.min(this.legend.height, this.visibleLegend.height - 9);
/*      */       }
/* 1466 */       if (this.legend.height > this.visibleLegend.height) {
/* 1467 */         this.visibleLegend.width = Math.min(this.legend.width, this.visibleLegend.width - 9);
/* 1468 */         this.visibleLegend.x += 9;
/*      */       }
/* 1470 */       break;
/*      */     
/*      */ 
/*      */     case 2: 
/* 1474 */       this.visibleLegend.x = this.legend.x;
/* 1475 */       this.visibleLegend.y = this.legend.y;
/* 1476 */       this.visibleLegend.width = (size.width - this.legend.x - 9);
/* 1477 */       this.visibleLegend.height = (bounds.y - (this.gridAlignment == 1 ? 0 : right_offset) - 13);
/* 1478 */       if ((insets.top != -1) && (isTitleOn())) {
/* 1479 */         this.visibleLegend.height -= getLabelSize(getTitle(), getFontMetrics(getFont("titleFont"))).height;
/*      */       }
/*      */       
/* 1482 */       if (this.legend.height > this.visibleLegend.height) {
/* 1483 */         this.visibleLegend.width = Math.min(this.legend.width, this.visibleLegend.width - 9);
/*      */       }
/* 1485 */       if (this.legend.width > this.visibleLegend.width) {
/* 1486 */         this.visibleLegend.height = Math.min(this.legend.height, this.visibleLegend.height - 9);
/*      */       }
/* 1488 */       break;
/*      */     
/*      */     case 3: 
/* 1491 */       this.legend.y = Math.max(this.legend.y, bounds.y + bounds.height + (this.gridAlignment == 1 ? bottom_offset : left_offset) + 6);
/* 1492 */       this.visibleLegend.y = this.legend.y;
/* 1493 */       this.visibleLegend.x = this.legend.x;
/* 1494 */       this.visibleLegend.width = (size.width - this.legend.x - 9);
/* 1495 */       this.visibleLegend.height = (size.height - this.visibleLegend.y - 4);
/*      */       
/* 1497 */       if (this.legend.height > this.visibleLegend.height) {
/* 1498 */         this.visibleLegend.width = Math.min(this.legend.width, this.visibleLegend.width - 9);
/*      */       }
/* 1500 */       if (this.legend.width > this.visibleLegend.width) {
/* 1501 */         this.visibleLegend.height = Math.min(this.legend.height, this.visibleLegend.height - 9);
/*      */       }
/*      */       break;
/*      */     }
/* 1505 */     this.horizontalLegendScrollerOn = (this.legend.width > this.visibleLegend.width);
/* 1506 */     this.verticalLegendScrollerOn = (this.legend.height > this.visibleLegend.height);
/*      */     
/*      */ 
/*      */ 
/* 1510 */     this.needGraphBounds = false;
/* 1511 */     this.currentBounds = bounds;
/* 1512 */     return bounds;
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
/* 1523 */     if ((!this.needChartCalculation) && (!isServletModeOn())) {
/* 1524 */       return;
/*      */     }
/* 1526 */     calculateZeroLines(grid);
/*      */     
/* 1528 */     int sampleCount = getSampleCount();
/* 1529 */     int seriesCount = getSeriesCount();
/* 1530 */     if ((is3DModeOn()) && (sampleCount > 0) && (seriesCount > 0))
/*      */     {
/* 1532 */       if (this.depth3d >= 0) {
/* 1533 */         this.depth3dPoint.x = this.depth3d;
/* 1534 */         this.depth3dPoint.y = (-this.depth3d);
/*      */ 
/*      */ 
/*      */       }
/* 1538 */       else if (this.gridAlignment == 1) {
/* 1539 */         double sampleWidth = grid.width / sampleCount;
/* 1540 */         double barWidth = sampleWidth * this.barWidthFraction;
/* 1541 */         if (this.barType == 0) {
/* 1542 */           barWidth /= seriesCount;
/*      */         }
/* 1544 */         this.depth3dPoint.x = ((int)Math.round(barWidth * 1.25D));
/* 1545 */         this.depth3dPoint.y = ((int)Math.round(-barWidth / 1.25D));
/*      */       } else {
/* 1547 */         double sampleWidth = grid.height / sampleCount;
/* 1548 */         double barWidth = sampleWidth * this.barWidthFraction;
/* 1549 */         if (this.barType == 0) {
/* 1550 */           barWidth /= seriesCount;
/*      */         }
/* 1552 */         this.depth3dPoint.x = ((int)Math.round(barWidth * 1.25D * 0.75D));
/* 1553 */         this.depth3dPoint.y = ((int)Math.round(-(barWidth / 1.25D * 0.75D)));
/*      */       }
/*      */     }
/*      */     
/*      */ 
/*      */ 
/* 1559 */     if ((this.barBounds == null) || (this.barBounds.length != seriesCount) || (this.barBounds[0].length != sampleCount)) {
/* 1560 */       Rectangle[][] newBarBounds = new Rectangle[seriesCount][sampleCount];
/* 1561 */       for (int serie = 0; serie < seriesCount; serie++) {
/* 1562 */         for (int sample = 0; sample < sampleCount; sample++) {
/* 1563 */           if ((this.barBounds != null) && (serie < this.barBounds.length) && (sample < this.barBounds[0].length) && (this.barBounds[serie][sample] != null)) {
/* 1564 */             newBarBounds[serie][sample] = this.barBounds[serie][sample];
/*      */           }
/*      */         }
/*      */       }
/* 1568 */       this.barBounds = newBarBounds;
/*      */     }
/*      */     
/*      */ 
/* 1572 */     for (int sample = 0; sample < sampleCount; sample++) {
/* 1573 */       for (int serie = 0; serie < seriesCount; serie++) {
/* 1574 */         if (this.barBounds[serie][sample] == null) {
/* 1575 */           this.barBounds[serie][sample] = new Rectangle();
/*      */         }
/*      */       }
/*      */     }
/*      */     
/*      */ 
/* 1581 */     double sampleWidth = 0.0D;
/* 1582 */     double barWidth = dataBounds.width;
/* 1583 */     if ((sampleCount > 0) && (seriesCount > 0)) {
/* 1584 */       if (this.gridAlignment == 1) {
/* 1585 */         sampleWidth = dataBounds.width / sampleCount;
/*      */       } else {
/* 1587 */         sampleWidth = dataBounds.height / sampleCount;
/*      */       }
/* 1589 */       barWidth = sampleWidth * this.barWidthFraction;
/* 1590 */       if (this.barType == 0) {
/* 1591 */         barWidth /= seriesCount;
/*      */       }
/*      */     }
/*      */     
/*      */ 
/* 1596 */     double barSpace = sampleWidth * (1.0D - this.barWidthFraction);
/* 1597 */     double pos = 0.0D;
/* 1598 */     boolean display3dOn = is3DModeOn();
/* 1599 */     if (this.gridAlignment == 1) {
/* 1600 */       pos = dataBounds.x + barSpace / 2.0D;
/* 1601 */       if (display3dOn) {
/* 1602 */         pos -= this.depth3dPoint.x;
/*      */       }
/*      */     } else {
/* 1605 */       pos = dataBounds.y + barSpace / 2.0D;
/* 1606 */       if (display3dOn) {
/* 1607 */         pos -= this.depth3dPoint.y;
/*      */       }
/*      */     }
/*      */     
/*      */ 
/* 1612 */     if (this.gridAlignment == 1) {
/* 1613 */       for (int sample = 0; sample < sampleCount; sample++) {
/* 1614 */         for (int serie = 0; serie < seriesCount; serie++)
/*      */         {
/* 1616 */           Rectangle bar = new Rectangle();
/*      */           
/*      */ 
/*      */ 
/* 1620 */           if (pos >= 0.0D) {
/* 1621 */             bar.x = ((int)(pos + 0.5D));
/* 1622 */             bar.width = ((int)(pos + barWidth + 0.5D) - bar.x);
/*      */           } else {
/* 1624 */             bar.x = ((int)(pos - 0.5D));
/* 1625 */             bar.width = ((int)(pos + barWidth - 0.5D) - bar.x);
/*      */           }
/*      */           
/* 1628 */           boolean stacked = this.barType == 1;
/* 1629 */           int rangeIndex = getSeriesRange(serie);
/*      */           
/*      */ 
/* 1632 */           double value = getSampleValue(serie, sample);
/* 1633 */           if (value >= 0.0D)
/*      */           {
/* 1635 */             double height = 0.0D;
/* 1636 */             if (this.upperRange[rangeIndex] > this.lowerRange[rangeIndex]) {
/* 1637 */               height = value / this.currentUpperRange[rangeIndex] * (this.zeroLine[rangeIndex] - dataBounds.y);
/*      */             } else {
/* 1639 */               height = value / this.currentLowerRange[rangeIndex] * (dataBounds.y + dataBounds.height - this.zeroLine[rangeIndex]);
/*      */             }
/*      */             
/*      */ 
/* 1643 */             bar.height = ((int)(height + 0.5D));
/*      */             
/*      */ 
/* 1646 */             bar.y = this.zeroLine[rangeIndex];
/* 1647 */             if (this.upperRange[rangeIndex] > this.lowerRange[rangeIndex]) {
/* 1648 */               bar.y -= bar.height;
/*      */             }
/*      */             
/*      */ 
/* 1652 */             if ((stacked) && (serie > 0))
/*      */             {
/* 1654 */               double sum = 0.0D;
/* 1655 */               for (int i = 0; i < serie; i++) {
/* 1656 */                 double sample_value = getSampleValue(i, sample);
/* 1657 */                 if (sample_value >= 0.0D) {
/* 1658 */                   sum += sample_value;
/*      */                 }
/*      */               }
/*      */               
/*      */ 
/* 1663 */               double total_height = 0.0D;
/* 1664 */               if (this.upperRange[0] > this.lowerRange[0]) {
/* 1665 */                 total_height = sum / this.currentUpperRange[0] * (this.zeroLine[0] - dataBounds.y);
/*      */               } else {
/* 1667 */                 total_height = -(sum / this.currentLowerRange[0]) * (dataBounds.y + dataBounds.height - this.zeroLine[0]);
/*      */               }
/*      */               
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 1675 */               double ypos = this.zeroLine[0] - total_height;
/* 1676 */               if (this.upperRange[0] > this.lowerRange[0]) {
/* 1677 */                 ypos -= height;
/*      */               }
/* 1679 */               if (ypos >= 0.0D) {
/* 1680 */                 bar.y = ((int)(ypos + 0.5D));
/*      */               } else {
/* 1682 */                 bar.y = ((int)(ypos - 0.5D));
/*      */               }
/* 1684 */               double bar_height = height + ypos - bar.y;
/* 1685 */               if (bar_height >= 0.0D) {
/* 1686 */                 bar.height = ((int)(bar_height + 0.5D));
/*      */               } else {
/* 1688 */                 bar.height = ((int)(bar_height - 0.5D));
/*      */               }
/*      */             }
/*      */             
/*      */ 
/* 1693 */             int bar_bottom = bar.y + bar.height;
/* 1694 */             int chart_bottom = dataBounds.y + dataBounds.height;
/* 1695 */             if (bar_bottom > chart_bottom) {
/* 1696 */               bar.height -= bar_bottom - chart_bottom;
/*      */             }
/*      */             
/*      */ 
/*      */           }
/*      */           else
/*      */           {
/* 1703 */             double height = 0.0D;
/* 1704 */             if (this.upperRange[rangeIndex] > this.lowerRange[rangeIndex]) {
/* 1705 */               height = value / this.currentLowerRange[rangeIndex] * (dataBounds.y + dataBounds.height - this.zeroLine[rangeIndex]);
/*      */             } else {
/* 1707 */               height = value / this.currentUpperRange[rangeIndex] * (this.zeroLine[rangeIndex] - dataBounds.y);
/*      */             }
/*      */             
/*      */ 
/* 1711 */             bar.height = ((int)(height + 0.5D));
/*      */             
/*      */ 
/* 1714 */             bar.y = this.zeroLine[rangeIndex];
/* 1715 */             if (this.upperRange[rangeIndex] < this.lowerRange[rangeIndex]) {
/* 1716 */               bar.y -= bar.height;
/*      */             }
/*      */             
/*      */ 
/* 1720 */             if ((stacked) && (serie > 0))
/*      */             {
/* 1722 */               double sum = 0.0D;
/* 1723 */               for (int i = 0; i < serie; i++) {
/* 1724 */                 if (getSampleValue(i, sample) < 0.0D) {
/* 1725 */                   sum += getSampleValue(i, sample);
/*      */                 }
/*      */               }
/*      */               
/*      */ 
/* 1730 */               double total_height = 0.0D;
/* 1731 */               if (this.upperRange[0] > this.lowerRange[0]) {
/* 1732 */                 total_height = sum / this.currentLowerRange[0] * (dataBounds.y + dataBounds.height - this.zeroLine[0]);
/*      */               } else {
/* 1734 */                 total_height = -(sum / this.currentUpperRange[0]) * (this.zeroLine[0] - dataBounds.y);
/*      */               }
/*      */               
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 1742 */               double ypos = this.zeroLine[0] + total_height;
/* 1743 */               if (this.upperRange[0] < this.lowerRange[0]) {
/* 1744 */                 ypos -= height;
/*      */               }
/* 1746 */               if (ypos >= 0.0D) {
/* 1747 */                 bar.y = ((int)(ypos + 0.5D));
/*      */               } else {
/* 1749 */                 bar.y = ((int)(ypos - 0.5D));
/*      */               }
/* 1751 */               double bar_height = height + ypos - bar.y;
/* 1752 */               if (bar_height >= 0.0D) {
/* 1753 */                 bar.height = ((int)(bar_height + 0.5D));
/*      */               } else {
/* 1755 */                 bar.height = ((int)(bar_height - 0.5D));
/*      */               }
/*      */             }
/*      */             
/*      */ 
/* 1760 */             if (bar.y < dataBounds.y) {
/* 1761 */               bar.height -= dataBounds.y - bar.y;
/* 1762 */               bar.y = dataBounds.y;
/*      */             }
/*      */           }
/*      */           
/*      */ 
/* 1767 */           if (display3dOn) {
/* 1768 */             bar.y -= this.depth3dPoint.y;
/*      */           }
/*      */           
/*      */ 
/* 1772 */           if ((this.barType == 0) || (serie == seriesCount - 1)) {
/* 1773 */             pos += barWidth;
/*      */           }
/*      */           
/*      */ 
/* 1777 */           this.barBounds[serie][sample].x = bar.x;
/* 1778 */           this.barBounds[serie][sample].y = bar.y;
/* 1779 */           this.barBounds[serie][sample].width = bar.width;
/* 1780 */           this.barBounds[serie][sample].height = bar.height;
/*      */         }
/*      */         
/*      */ 
/* 1784 */         pos += barSpace;
/*      */       }
/*      */       
/*      */     }
/*      */     else
/*      */     {
/* 1790 */       for (int sample = 0; sample < sampleCount; sample++) {
/* 1791 */         for (int serie = 0; serie < seriesCount; serie++)
/*      */         {
/* 1793 */           Rectangle bar = new Rectangle();
/*      */           
/*      */ 
/*      */ 
/* 1797 */           if (pos >= 0.0D) {
/* 1798 */             bar.y = ((int)(pos + 0.5D));
/* 1799 */             bar.height = ((int)(pos + barWidth + 0.5D) - bar.y);
/*      */           } else {
/* 1801 */             bar.y = ((int)(pos - 0.5D));
/* 1802 */             bar.height = ((int)(pos + barWidth - 0.5D) - bar.y);
/*      */           }
/*      */           
/*      */ 
/* 1806 */           boolean stacked = this.barType == 1;
/* 1807 */           int rangeIndex = getSeriesRange(serie);
/*      */           
/*      */ 
/* 1810 */           double value = getSampleValue(serie, sample);
/* 1811 */           if (value >= 0.0D) {
/* 1812 */             double height = 0.0D;
/* 1813 */             if (this.upperRange[rangeIndex] > this.lowerRange[rangeIndex]) {
/* 1814 */               height = value / this.currentUpperRange[rangeIndex] * (dataBounds.x + dataBounds.width - this.zeroLine[rangeIndex]);
/*      */             } else {
/* 1816 */               height = value / this.currentLowerRange[rangeIndex] * (this.zeroLine[rangeIndex] - dataBounds.x);
/*      */             }
/*      */             
/*      */ 
/* 1820 */             bar.width = ((int)(height + 0.5D));
/* 1821 */             bar.x = this.zeroLine[rangeIndex];
/* 1822 */             if (this.upperRange[rangeIndex] < this.lowerRange[rangeIndex]) {
/* 1823 */               bar.x -= bar.width;
/*      */             }
/*      */             
/*      */ 
/* 1827 */             if ((this.barType == 1) && (serie > 0))
/*      */             {
/* 1829 */               double sum = 0.0D;
/* 1830 */               for (int i = 0; i < serie; i++) {
/* 1831 */                 double sample_value = getSampleValue(i, sample);
/* 1832 */                 if (sample_value >= 0.0D) {
/* 1833 */                   sum += sample_value;
/*      */                 }
/*      */               }
/*      */               
/*      */ 
/* 1838 */               double total_height = 0.0D;
/* 1839 */               if (this.upperRange[0] > this.lowerRange[0]) {
/* 1840 */                 total_height = sum / this.currentUpperRange[0] * (dataBounds.x + dataBounds.width - this.zeroLine[0]);
/*      */               } else {
/* 1842 */                 total_height = -(sum / this.currentLowerRange[0]) * (this.zeroLine[0] - dataBounds.x);
/*      */               }
/*      */               
/*      */ 
/* 1846 */               double xpos = this.zeroLine[0] + total_height;
/* 1847 */               if (this.upperRange[0] < this.lowerRange[0]) {
/* 1848 */                 xpos -= height;
/*      */               }
/*      */               
/*      */ 
/*      */ 
/* 1853 */               if (xpos >= 0.0D) {
/* 1854 */                 bar.x = ((int)(xpos + 0.5D));
/*      */               } else {
/* 1856 */                 bar.x = ((int)(xpos - 0.5D));
/*      */               }
/* 1858 */               double bar_width = height + xpos - bar.x;
/* 1859 */               if (bar_width >= 0.0D) {
/* 1860 */                 bar.width = ((int)(bar_width + 0.5D));
/*      */               } else {
/* 1862 */                 bar.width = ((int)(bar_width - 0.5D));
/*      */               }
/*      */             }
/*      */             
/*      */ 
/*      */ 
/* 1868 */             if (bar.x < dataBounds.x) {
/* 1869 */               bar.width -= dataBounds.x - bar.x;
/* 1870 */               bar.x = dataBounds.x;
/*      */             }
/*      */             
/*      */           }
/*      */           else
/*      */           {
/* 1876 */             double height = 0.0D;
/* 1877 */             if (this.upperRange[rangeIndex] > this.lowerRange[rangeIndex]) {
/* 1878 */               height = value / this.currentLowerRange[rangeIndex] * (this.zeroLine[rangeIndex] - dataBounds.x);
/*      */             } else {
/* 1880 */               height = value / this.currentUpperRange[rangeIndex] * (dataBounds.x + dataBounds.width - this.zeroLine[rangeIndex]);
/*      */             }
/*      */             
/*      */ 
/* 1884 */             bar.width = ((int)(height + 0.5D));
/* 1885 */             bar.x = this.zeroLine[rangeIndex];
/* 1886 */             if (this.upperRange[rangeIndex] > this.lowerRange[rangeIndex]) {
/* 1887 */               bar.x -= bar.width;
/*      */             }
/*      */             
/*      */ 
/* 1891 */             if ((this.barType == 1) && (serie > 0))
/*      */             {
/* 1893 */               double sum = 0.0D;
/* 1894 */               for (int i = 0; i < serie; i++) {
/* 1895 */                 if (getSampleValue(i, sample) < 0.0D) {
/* 1896 */                   sum += getSampleValue(i, sample);
/*      */                 }
/*      */               }
/*      */               
/*      */ 
/* 1901 */               double total_height = 0.0D;
/* 1902 */               if (this.upperRange[0] > this.lowerRange[0]) {
/* 1903 */                 total_height = sum / this.currentLowerRange[0] * (this.zeroLine[0] - dataBounds.x);
/*      */               } else {
/* 1905 */                 total_height = -(sum / this.currentUpperRange[0]) * (dataBounds.x + dataBounds.width - this.zeroLine[0]);
/*      */               }
/*      */               
/*      */ 
/* 1909 */               double xpos = this.zeroLine[0] - total_height;
/* 1910 */               if (this.upperRange[0] > this.lowerRange[0]) {
/* 1911 */                 xpos -= height;
/*      */               }
/*      */               
/*      */ 
/*      */ 
/* 1916 */               if (xpos >= 0.0D) {
/* 1917 */                 bar.x = ((int)(xpos + 0.5D));
/*      */               } else {
/* 1919 */                 bar.x = ((int)(xpos - 0.5D));
/*      */               }
/* 1921 */               double bar_width = height + xpos - bar.x;
/* 1922 */               if (bar_width >= 0.0D) {
/* 1923 */                 bar.width = ((int)(bar_width + 0.5D));
/*      */               } else {
/* 1925 */                 bar.width = ((int)(bar_width - 0.5D));
/*      */               }
/*      */             }
/*      */             
/*      */ 
/*      */ 
/* 1931 */             int bar_bottom = bar.x + bar.width;
/* 1932 */             int chart_edge = dataBounds.x + dataBounds.width;
/* 1933 */             if (bar_bottom > chart_edge) {
/* 1934 */               bar.width -= bar_bottom - chart_edge;
/*      */             }
/*      */           }
/*      */           
/*      */ 
/* 1939 */           if (display3dOn) {
/* 1940 */             bar.x -= this.depth3dPoint.x;
/*      */           }
/*      */           
/*      */ 
/* 1944 */           this.barBounds[serie][sample].x = bar.x;
/* 1945 */           this.barBounds[serie][sample].y = bar.y;
/* 1946 */           this.barBounds[serie][sample].width = bar.width;
/* 1947 */           this.barBounds[serie][sample].height = bar.height;
/*      */           
/*      */ 
/* 1950 */           if ((this.barType == 0) || (serie == seriesCount - 1)) {
/* 1951 */             pos += barWidth;
/*      */           }
/*      */         }
/*      */         
/*      */ 
/* 1956 */         pos += barSpace;
/*      */       }
/*      */     }
/* 1959 */     this.needChartCalculation = false;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   private void paintBars(Graphics g, Rectangle grid)
/*      */   {
/* 1969 */     int seriesCount = getSeriesCount();
/* 1970 */     int sampleCount = getSampleCount();
/*      */     
/*      */ 
/* 1973 */     if ((grid.width < 0) || (grid.height < 0)) {
/* 1974 */       return;
/*      */     }
/*      */     
/*      */ 
/* 1978 */     boolean display3dOn = is3DModeOn();
/* 1979 */     if (display3dOn) {
/* 1980 */       g.setClip(grid.x - this.depth3dPoint.x + 1, grid.y + 1, grid.width + this.depth3dPoint.x - 1, grid.height - this.depth3dPoint.y - 1);
/*      */     }
/* 1982 */     else if ((this.parentChart != null) && (this.parentChart.is3DModeOn())) {
/* 1983 */       g.setClip(grid.x + 1 - this.parentChart.depth3dPoint.x, grid.y + 1 - this.parentChart.depth3dPoint.y, grid.width - 1, grid.height - 1);
/*      */     } else {
/* 1985 */       g.setClip(grid.x + 1, grid.y + 1, grid.width - 1, grid.height - 1);
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/* 1991 */     int startIndex = Math.max(this.visibleSamples[0] - 1, 0);
/* 1992 */     int stopIndex = Math.min(startIndex + this.visibleSamples[1] + 1, sampleCount);
/*      */     
/* 1994 */     if (this.gridAlignment == 1)
/*      */     {
/* 1996 */       if (this.barType == 0) {
/* 1997 */         for (int sample = startIndex; sample < stopIndex; sample++) {
/* 1998 */           for (int serie = 0; serie < seriesCount; serie++) {
/* 1999 */             paintBar(g, grid, serie, sample);
/*      */           }
/*      */           
/*      */         }
/*      */         
/*      */ 
/*      */       }
/* 2006 */       else if (this.upperRange[0] > this.lowerRange[0])
/*      */       {
/* 2008 */         for (int sample = startIndex; sample < stopIndex; sample++) {
/* 2009 */           for (int serie = seriesCount - 1; serie >= 0; serie--) {
/*      */             try {
/* 2011 */               if (getSampleValue(serie, sample) < 0.0D) {
/* 2012 */                 paintBar(g, grid, serie, sample);
/*      */               }
/*      */             }
/*      */             catch (IllegalArgumentException localIllegalArgumentException) {}
/*      */           }
/*      */         }
/*      */         
/*      */ 
/* 2020 */         for (int sample = startIndex; sample < stopIndex; sample++) {
/* 2021 */           for (int serie = 0; serie < seriesCount; serie++) {
/*      */             try {
/* 2023 */               if (getSampleValue(serie, sample) >= 0.0D) {
/* 2024 */                 paintBar(g, grid, serie, sample);
/*      */               }
/*      */               
/*      */ 
/*      */             }
/*      */             catch (IllegalArgumentException localIllegalArgumentException1) {}
/*      */           }
/*      */         }
/*      */       }
/*      */       else
/*      */       {
/* 2035 */         for (int sample = startIndex; sample < stopIndex; sample++) {
/* 2036 */           for (int serie = seriesCount - 1; serie >= 0; serie--) {
/*      */             try {
/* 2038 */               if (getSampleValue(serie, sample) < 0.0D) {
/* 2039 */                 paintBar(g, grid, serie, sample);
/*      */               }
/*      */             }
/*      */             catch (IllegalArgumentException localIllegalArgumentException2) {}
/*      */           }
/*      */         }
/*      */         
/*      */ 
/* 2047 */         for (int sample = startIndex; sample < stopIndex; sample++) {
/* 2048 */           for (int serie = 0; serie < seriesCount; serie++) {
/*      */             try {
/* 2050 */               if (getSampleValue(serie, sample) >= 0.0D) {
/* 2051 */                 paintBar(g, grid, serie, sample);
/*      */               }
/*      */               
/*      */ 
/*      */             }
/*      */             catch (IllegalArgumentException localIllegalArgumentException3) {}
/*      */           }
/*      */           
/*      */         }
/*      */         
/*      */       }
/*      */       
/*      */ 
/*      */     }
/* 2065 */     else if (this.barType == 0) {
/* 2066 */       for (int sample = stopIndex - 1; sample >= startIndex; sample--) {
/* 2067 */         for (int serie = seriesCount - 1; serie >= 0; serie--) {
/* 2068 */           paintBar(g, grid, serie, sample);
/*      */         }
/*      */         
/*      */       }
/*      */       
/*      */ 
/*      */     }
/* 2075 */     else if (this.upperRange[0] > this.lowerRange[0])
/*      */     {
/* 2077 */       for (int sample = stopIndex - 1; sample >= startIndex; sample--) {
/* 2078 */         for (int serie = seriesCount - 1; serie >= 0; serie--) {
/* 2079 */           if (getSampleValue(serie, sample) < 0.0D) {
/* 2080 */             paintBar(g, grid, serie, sample);
/*      */           }
/*      */         }
/*      */       }
/*      */       
/* 2085 */       for (int sample = stopIndex - 1; sample >= startIndex; sample--) {
/* 2086 */         for (int serie = 0; serie < seriesCount; serie++) {
/* 2087 */           if (getSampleValue(serie, sample) >= 0.0D) {
/* 2088 */             paintBar(g, grid, serie, sample);
/*      */           }
/*      */           
/*      */         }
/*      */       }
/*      */     }
/*      */     else
/*      */     {
/* 2096 */       for (int sample = stopIndex - 1; sample >= startIndex; sample--) {
/* 2097 */         for (int serie = seriesCount - 1; serie >= 0; serie--) {
/* 2098 */           if (getSampleValue(serie, sample) >= 0.0D) {
/* 2099 */             paintBar(g, grid, serie, sample);
/*      */           }
/*      */         }
/*      */       }
/*      */       
/* 2104 */       for (int sample = stopIndex - 1; sample >= startIndex; sample--) {
/* 2105 */         for (int serie = 0; serie < seriesCount; serie++) {
/* 2106 */           if (getSampleValue(serie, sample) < 0.0D) {
/* 2107 */             paintBar(g, grid, serie, sample);
/*      */           }
/*      */         }
/*      */       }
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/* 2116 */     if (display3dOn) {
/* 2117 */       g.setColor(getBackground());
/*      */       
/* 2119 */       Polygon edge = new Polygon();
/* 2120 */       edge.addPoint(grid.x + grid.width, grid.y + grid.height);
/* 2121 */       edge.addPoint(grid.x + grid.width, grid.y + grid.height - this.depth3dPoint.y + 1);
/* 2122 */       edge.addPoint(grid.x + grid.width - this.depth3dPoint.x - 1, grid.y + grid.height - this.depth3dPoint.y + 1);
/* 2123 */       g.fillPolygon(edge);
/* 2124 */       g.setColor(getChartForeground());
/* 2125 */       g.drawLine(grid.x + grid.width, grid.y + grid.height, grid.x + grid.width - this.depth3dPoint.x, grid.y + grid.height - this.depth3dPoint.y);
/*      */       
/* 2127 */       edge.xpoints[0] = grid.x;
/* 2128 */       edge.ypoints[0] = grid.y;
/* 2129 */       edge.xpoints[1] = (grid.x - this.depth3dPoint.x);
/* 2130 */       edge.ypoints[1] = grid.y;
/* 2131 */       edge.xpoints[2] = (grid.x - this.depth3dPoint.x);
/* 2132 */       edge.ypoints[2] = (grid.y - this.depth3dPoint.y);
/* 2133 */       g.setColor(getBackground());
/* 2134 */       g.fillPolygon(edge);
/* 2135 */       g.setColor(getChartForeground());
/* 2136 */       g.drawLine(grid.x, grid.y, grid.x - this.depth3dPoint.x, grid.y - this.depth3dPoint.y);
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
/*      */   private void paintBar(Graphics g, Rectangle grid, int serie, int sample)
/*      */   {
/* 2150 */     ChartSample s = getSample(serie, sample);
/* 2151 */     int seriesCount = getSeriesCount();
/* 2152 */     if ((s == null) || (s.value == null) || (s.value.isNaN()) || ((s.getFloatValue() == 0.0D) && (this.barType == 1) && (seriesCount > 1))) {
/* 2153 */       return;
/*      */     }
/*      */     
/*      */ 
/* 2157 */     int rangeIndex = getSeriesRange(serie);
/* 2158 */     double value = s.getFloatValue();
/* 2159 */     double upper = this.currentUpperRange[rangeIndex];
/* 2160 */     double lower = this.currentLowerRange[rangeIndex];
/* 2161 */     boolean outside_grid = (upper > lower) && (((value < lower) && (lower > 0.0D)) || ((value > upper) && (upper < 0.0D)));
/* 2162 */     outside_grid &= ((upper < lower) && (((value < lower) && (lower < 0.0D)) || ((value > upper) && (upper > 0.0D))));
/* 2163 */     if ((this.barType != 1) && (outside_grid)) {
/* 2164 */       return;
/*      */     }
/*      */     
/*      */ 
/* 2168 */     if (this.multiColorOn) {
/* 2169 */       if ((getSeriesCount() > 1) || (this.barType == 1) || (this.multiSeriesOn)) {
/* 2170 */         g.setColor(getSampleColor(serie));
/*      */       } else {
/* 2172 */         g.setColor(getSampleColor(sample));
/*      */       }
/*      */     } else {
/* 2175 */       g.setColor(getSampleColor(0));
/*      */     }
/*      */     
/*      */ 
/* 2179 */     Rectangle bar = null;
/* 2180 */     if ((this.barBounds != null) && (serie < this.barBounds.length) && (this.barBounds[serie] != null) && (sample < this.barBounds[serie].length)) {
/* 2181 */       bar = new Rectangle(this.barBounds[serie][sample]);
/*      */       
/* 2183 */       int ver_cut_off = grid.y - this.depth3dPoint.y - bar.y;
/* 2184 */       ver_cut_off = Math.max(0, ver_cut_off);
/* 2185 */       bar.y += ver_cut_off;
/* 2186 */       bar.height -= ver_cut_off;
/* 2187 */       int hor_cut_off = bar.x + bar.width - (grid.x + grid.width - this.depth3dPoint.x);
/* 2188 */       hor_cut_off = Math.max(0, hor_cut_off);
/* 2189 */       bar.width -= hor_cut_off;
/*      */     }
/*      */     
/*      */ 
/* 2193 */     if (bar != null) {
/* 2194 */       boolean selected = (s != null) && (s.isSelected());
/* 2195 */       Color chartForeground = getChartForeground();
/*      */       
/* 2197 */       if (this.barOutlineOn) {
/* 2198 */         g.fillRect(bar.x, bar.y, bar.width + 1, bar.height);
/* 2199 */         Color c = g.getColor();
/* 2200 */         if (this.barOutlineColor != null) {
/* 2201 */           g.setColor(this.barOutlineColor);
/*      */         } else {
/* 2203 */           g.setColor(chartForeground);
/*      */         }
/* 2205 */         g.drawRect(bar.x, bar.y, bar.width, bar.height);
/* 2206 */         if (selected) {
/* 2207 */           g.drawRect(bar.x + 1, bar.y + 1, Math.max(bar.width - 2, 1), bar.height == 2 ? 1 : bar.height - 2);
/*      */         }
/* 2209 */         g.setColor(c);
/*      */ 
/*      */       }
/*      */       else
/*      */       {
/* 2214 */         g.fillRect(bar.x, bar.y, bar.width, bar.height);
/*      */         
/* 2216 */         if (selected) {
/* 2217 */           Color c = g.getColor();
/* 2218 */           if (this.barOutlineColor != null) {
/* 2219 */             g.setColor(this.barOutlineColor);
/*      */           } else {
/* 2221 */             g.setColor(chartForeground);
/*      */           }
/* 2223 */           if (this.gridAlignment == 1) {
/* 2224 */             g.drawRect(bar.x, bar.y, bar.width - 1, bar.height);
/*      */           } else {
/* 2226 */             g.drawRect(bar.x, bar.y, bar.width - 1, bar.height - 1);
/*      */           }
/* 2228 */           g.setColor(c);
/*      */         }
/*      */       }
/*      */       
/*      */ 
/* 2233 */       if ((is3DModeOn()) && (bar.height > 0) && (bar.width > 0)) {
/* 2234 */         paint3DBar(g, bar, selected, value);
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
/*      */   private void paint3DBar(Graphics g, Rectangle bar, boolean selected, double value)
/*      */   {
/* 2249 */     Polygon top = get3dBarTop(bar);
/* 2250 */     Polygon right = get3dBarRight(bar);
/*      */     
/*      */ 
/* 2253 */     g.setColor(g.getColor().darker());
/* 2254 */     g.fillPolygon(top);
/* 2255 */     g.setColor(g.getColor().darker());
/* 2256 */     g.fillPolygon(right);
/* 2257 */     if (this.barOutlineOn) {
/* 2258 */       if (this.barOutlineColor != null) {
/* 2259 */         g.setColor(this.barOutlineColor);
/*      */       } else {
/* 2261 */         g.setColor(getChartForeground());
/*      */       }
/* 2263 */       g.drawPolygon(top);
/* 2264 */       g.drawPolygon(right);
/* 2265 */       if (selected) {
/* 2266 */         g.drawLine(right.xpoints[1] - 1, right.ypoints[1] + 1, right.xpoints[2] - 1, right.ypoints[2]);
/* 2267 */         g.drawLine(top.xpoints[1], top.ypoints[1] + 1, top.xpoints[2] - 1, top.ypoints[2] + 1);
/* 2268 */         if (this.gridAlignment == 1) {
/* 2269 */           g.drawLine(right.xpoints[2] - 1, right.ypoints[2], right.xpoints[3] - 1, right.ypoints[3]);
/* 2270 */           g.drawLine(top.xpoints[0] + 1, top.ypoints[0], top.xpoints[1] + 1, top.ypoints[1]);
/* 2271 */           if (bar.height != 0) {
/* 2272 */             g.drawLine(right.xpoints[0], right.ypoints[0] + 1, right.xpoints[1], right.ypoints[1] + 1);
/*      */           } else {
/* 2274 */             g.drawLine(top.xpoints[3] - 1, top.ypoints[3] - 1, top.xpoints[0] + 1, top.ypoints[0] - 1);
/*      */           }
/*      */         } else {
/* 2277 */           g.drawLine(right.xpoints[2], right.ypoints[2] - 1, right.xpoints[3], right.ypoints[3] - 1);
/* 2278 */           g.drawLine(top.xpoints[0], top.ypoints[0] + 1, top.xpoints[1], top.ypoints[1] + 1);
/* 2279 */           if (bar.width != 0) {
/* 2280 */             g.drawLine(top.xpoints[2] - 1, top.ypoints[2], top.xpoints[3] - 1, top.ypoints[3]);
/*      */           } else {
/* 2282 */             g.drawLine(right.xpoints[3] + 1, right.ypoints[3] - 1, right.xpoints[0] + 1, right.ypoints[0] + 1);
/*      */           }
/*      */         }
/*      */       }
/* 2286 */     } else if (selected) {
/* 2287 */       if (this.barOutlineColor != null) {
/* 2288 */         g.setColor(this.barOutlineColor);
/*      */       } else {
/* 2290 */         g.setColor(getChartForeground());
/*      */       }
/* 2292 */       g.drawPolygon(top);
/* 2293 */       g.drawLine(right.xpoints[1], right.ypoints[1], right.xpoints[2], right.ypoints[2]);
/* 2294 */       if (this.gridAlignment == 1) {
/* 2295 */         g.drawLine(right.xpoints[2], right.ypoints[2], right.xpoints[3], right.ypoints[3]);
/*      */       } else {
/* 2297 */         g.drawLine(right.xpoints[2], right.ypoints[2] - 1, right.xpoints[3], right.ypoints[3] - 1);
/* 2298 */         g.drawLine(right.xpoints[3] - 1, right.ypoints[3] - 1, right.xpoints[0] - 1, right.ypoints[0]);
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
/*      */   private void paint3DZeroDivider(Graphics g, Rectangle grid, int index)
/*      */   {
/* 2313 */     index = Math.min(this.rangeOn.length - 1, Math.max(0, index));
/* 2314 */     if (this.rangeOn[index] == 0) {
/* 2315 */       return;
/*      */     }
/*      */     
/*      */ 
/*      */ 
/* 2320 */     g.setColor(index == 0 ? getChartForeground() : getRangeColor(index));
/* 2321 */     if (this.gridAlignment == 1)
/*      */     {
/* 2323 */       int zero_line = Math.max(this.zeroLine[index], grid.y);
/* 2324 */       zero_line = Math.min(zero_line, grid.y + grid.height);
/* 2325 */       int x = grid.x - this.depth3dPoint.x;
/* 2326 */       int y = zero_line - this.depth3dPoint.y;
/*      */       
/* 2328 */       g.drawLine(x, y, x + grid.width, y);
/* 2329 */       g.drawLine(x + grid.width, y, grid.x + grid.width, zero_line);
/*      */     }
/*      */     else {
/* 2332 */       int zero_line = Math.max(this.zeroLine[index], grid.x);
/* 2333 */       zero_line = Math.min(zero_line, grid.x + grid.width);
/* 2334 */       int y = grid.y - this.depth3dPoint.y;
/* 2335 */       int x = zero_line - this.depth3dPoint.x;
/*      */       
/* 2337 */       g.drawLine(x, y, x, y + grid.height);
/* 2338 */       g.drawLine(x, y, zero_line, grid.y);
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   private void paintStaticLabels(Graphics g, Rectangle grid)
/*      */   {
/* 2350 */     g.setClip(0, 0, 32767, 32767);
/*      */     
/*      */ 
/* 2353 */     int sampleCount = getSampleCount();
/* 2354 */     int seriesCount = getSeriesCount();
/* 2355 */     if ((sampleCount == 0) || (seriesCount == 0)) {
/* 2356 */       return;
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 2365 */     boolean outside = (isValueLabelsOn()) && (this.valueLabelStyle == 1);
/* 2366 */     outside |= ((this.sampleLabelsOn) && (this.sampleLabelStyle == 1));
/* 2367 */     outside |= ((this.seriesLabelsOn) && (this.seriesLabelStyle == 1));
/* 2368 */     boolean stacked_and_outside = (outside) && (this.barType == 1) && (seriesCount > 1);
/*      */     
/*      */ 
/* 2371 */     boolean display3dOn = is3DModeOn();
/* 2372 */     int grid_top = grid.y;
/* 2373 */     int grid_bottom = grid_top + grid.height - (display3dOn ? this.depth3dPoint.y : 0);
/* 2374 */     int grid_left = grid.x - (display3dOn ? this.depth3dPoint.x : 0);
/* 2375 */     int grid_right = grid.x + grid.width;
/*      */     
/*      */ 
/* 2378 */     Font valueLabelFont = getFont("valueLabelFont");
/* 2379 */     FontMetrics fm = getFontMetrics(valueLabelFont);
/* 2380 */     int valueLabelAngle = getLabelAngle("valueLabelAngle");
/* 2381 */     int fheight = fm.getHeight();
/* 2382 */     int descent = fm.getDescent();
/* 2383 */     int valueAscent = fm.getAscent();
/* 2384 */     int decimals = getSampleDecimalCount(0);
/*      */     
/*      */ 
/* 2387 */     Font sampleLabelFont = getFont("sampleLabelFont");
/* 2388 */     int sampleAngle = getLabelAngle("sampleLabelAngle");
/*      */     
/*      */ 
/* 2391 */     Font seriesLabelFont = new Font("Dialog", 0, 12);
/*      */     
/* 2393 */     int start = Math.max(this.visibleSamples[0] - 1, 0);
/* 2394 */     int stop = Math.min(start + this.visibleSamples[1] + 1, sampleCount);
/* 2395 */     for (int sample = start; sample < stop; sample++) {
/* 2396 */       int label_x = 0;
/* 2397 */       int label_y = 0;
/* 2398 */       boolean paint_label = true;
/*      */       
/* 2400 */       for (int series = 0; series < seriesCount; series++) {
/* 2401 */         if ((isValueLabelsOn(0)) || (this.sampleLabelsOn) || (this.seriesLabelsOn))
/*      */         {
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 2408 */           String prefix = getLabel("valueLabelPrefix_" + series);
/* 2409 */           if ((prefix == null) || (stacked_and_outside)) {
/* 2410 */             prefix = getLabel("valueLabelPrefix");
/*      */           }
/* 2412 */           String postfix = getLabel("valueLabelPostfix_" + series);
/* 2413 */           if ((postfix == null) || (stacked_and_outside)) {
/* 2414 */             postfix = getLabel("valueLabelPostfix");
/*      */           }
/*      */           
/*      */ 
/* 2418 */           double value = 0.0D;
/* 2419 */           double negative_value = 0.0D;
/* 2420 */           Rectangle bar = null;
/* 2421 */           Rectangle negative_bar = null;
/* 2422 */           if (this.barBounds != null) {
/* 2423 */             if ((series < this.barBounds.length) && (this.barBounds[series] != null) && (sample < this.barBounds[series].length)) {
/* 2424 */               bar = this.barBounds[series][sample];
/*      */             }
/* 2426 */             if ((this.barBounds.length > 0) && (this.barBounds[0] != null) && (sample < this.barBounds[0].length)) {
/* 2427 */               negative_bar = this.barBounds[series][sample];
/*      */             }
/*      */           }
/* 2430 */           if ((stacked_and_outside) && (bar != null) && (negative_bar != null))
/*      */           {
/* 2432 */             for (int i = 0; i < this.barBounds.length; i++) {
/* 2433 */               Rectangle b = this.barBounds[i][sample];
/* 2434 */               if (b != null)
/*      */               {
/* 2436 */                 if (this.gridAlignment == 1) {
/* 2437 */                   if (b.y + b.height > negative_bar.y + negative_bar.height) {
/* 2438 */                     negative_bar = b;
/*      */                   }
/* 2440 */                   if (b.y < bar.y) {
/* 2441 */                     bar = b;
/*      */                   }
/*      */                 }
/*      */                 else
/*      */                 {
/* 2446 */                   if (b.x < negative_bar.x) {
/* 2447 */                     negative_bar = b;
/*      */                   }
/* 2449 */                   if (b.x + b.width > bar.x + bar.width) {
/* 2450 */                     bar = b;
/*      */                   }
/*      */                 }
/*      */               }
/*      */             }
/*      */             
/* 2456 */             boolean validValue = false;
/* 2457 */             for (int i = 0; i < seriesCount; i++) {
/* 2458 */               ChartSample s = getSample(i, sample);
/* 2459 */               if ((s != null) && (s.value != null) && (!s.value.isNaN())) {
/* 2460 */                 validValue = true;
/* 2461 */                 double val = s.getFloatValue();
/* 2462 */                 if (val >= 0.0D) {
/* 2463 */                   value += val;
/*      */                 } else {
/* 2465 */                   negative_value += val;
/*      */                 }
/*      */               }
/*      */             }
/*      */             
/* 2470 */             if (!validValue) {
/*      */               continue;
/*      */             }
/*      */           }
/*      */           else
/*      */           {
/* 2476 */             ChartSample s = getSample(series, sample);
/* 2477 */             if ((s == null) || (s.value == null) || (s.value.isNaN())) continue;
/* 2478 */             value = s.getFloatValue();
/*      */           }
/*      */           
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 2485 */           if ((bar != null) && ((this.seriesLabelStyle == 0) || (this.sampleLabelStyle == 0) || (this.valueLabelStyle == 0)))
/*      */           {
/* 2487 */             int angle = 0;
/* 2488 */             Color fontColor = null;
/* 2489 */             Font font = seriesLabelFont;
/*      */             
/* 2491 */             if ((this.valueLabelStyle == 0) && (isValueLabelsOn(series))) {
/* 2492 */               angle = valueLabelAngle;
/* 2493 */               fontColor = getValueLabelColor(series);
/* 2494 */               font = valueLabelFont;
/*      */             }
/*      */             
/* 2497 */             if ((this.sampleLabelStyle == 0) && (this.sampleLabelsOn)) {
/* 2498 */               angle = sampleAngle;
/* 2499 */               if (getSampleLabelColor(sample) != null) {
/* 2500 */                 fontColor = getSampleLabelColor(sample);
/*      */               }
/* 2502 */               font = sampleLabelFont;
/*      */             }
/*      */             
/* 2505 */             if ((this.seriesLabelStyle == 0) && (this.seriesLabelsOn) && 
/* 2506 */               (fontColor == null)) {
/* 2507 */               fontColor = getSeriesLabelColor(series);
/*      */             }
/*      */             
/* 2510 */             fm = getFontMetrics(font);
/* 2511 */             g.setFont(font);
/*      */             
/* 2513 */             if (fontColor == null)
/*      */             {
/*      */ 
/* 2516 */               Color c = getSampleColor(0);
/* 2517 */               if (this.multiColorOn) {
/* 2518 */                 if ((seriesCount > 1) || (this.barType == 1) || (this.multiSeriesOn)) {
/* 2519 */                   c = getSampleColor(series);
/*      */                 } else {
/* 2521 */                   c = getSampleColor(sample);
/*      */                 }
/*      */               }
/* 2524 */               if ((c.getRed() + c.getGreen() + c.getBlue()) / 3 > 110) {
/* 2525 */                 g.setColor(Color.black);
/*      */               } else {
/* 2527 */                 g.setColor(Color.white);
/*      */               }
/*      */             }
/*      */             
/*      */ 
/* 2532 */             String label = constructLabel(series, sample, 0, this.seriesLabelsOn, null);
/* 2533 */             Dimension labelSize = getLabelSize(label, fm);
/* 2534 */             Dimension angledSize = getAngledLabelSize(labelSize, angle);
/*      */             
/*      */ 
/* 2537 */             if (this.gridAlignment == 1)
/*      */             {
/* 2539 */               label_x = bar.x + bar.width / 2 - angledSize.width / 2 + 1;
/*      */               
/*      */ 
/* 2542 */               if (((value >= 0.0D) && (this.upperRange[0] >= 0.0D)) || ((value < 0.0D) && (this.lowerRange[0] >= 0.0D))) {
/* 2543 */                 if (angle % 180 == 0) {
/* 2544 */                   label_y = bar.y + fm.getAscent();
/* 2545 */                   label_y = Math.max(label_y, grid.y + fm.getAscent());
/* 2546 */                   label_y = Math.min(label_y, grid.y + grid.height - 2);
/*      */                 } else {
/* 2548 */                   label_y = bar.y + 3;
/* 2549 */                   label_y = Math.min(label_y, bar.y + bar.height - angledSize.height - 2);
/* 2550 */                   label_y = Math.max(label_y, grid.y + 3);
/*      */                 }
/*      */                 
/*      */ 
/*      */               }
/* 2555 */               else if (angle % 180 == 0) {
/* 2556 */                 label_y = bar.y + bar.height - 2;
/* 2557 */                 label_y = Math.min(label_y, grid.y + grid.height - 2 - (display3dOn ? this.depth3dPoint.y : 0));
/* 2558 */                 label_y = Math.max(label_y, grid.y + angledSize.height - 1);
/*      */               } else {
/* 2560 */                 label_y = bar.y + bar.height - angledSize.height - 2;
/* 2561 */                 label_y = Math.min(label_y, grid.y + grid.height - angledSize.height - 2 - (display3dOn ? this.depth3dPoint.y : 0));
/* 2562 */                 label_y = Math.max(label_y, grid.y + 3 - (display3dOn ? this.depth3dPoint.y : 0));
/*      */               }
/*      */               
/*      */ 
/* 2566 */               if ((this.barType == 1) && (seriesCount > 1)) {
/* 2567 */                 paint_label = angledSize.height < bar.height;
/*      */               }
/*      */               
/*      */             }
/*      */             else
/*      */             {
/* 2573 */               if (angle % 180 == 0) {
/* 2574 */                 label_y = bar.y + bar.height / 2 + angledSize.height / 2 - 1;
/*      */               } else {
/* 2576 */                 label_y = bar.y + bar.height / 2 - angledSize.height / 2 + 1;
/*      */               }
/*      */               
/* 2579 */               if (value >= 0.0D) {
/* 2580 */                 label_x = bar.x + bar.width - angledSize.width - 1;
/* 2581 */                 label_x = Math.max(label_x, bar.x + 2);
/* 2582 */                 label_x = Math.min(label_x, grid.x + grid.width - angledSize.width - 1);
/*      */               } else {
/* 2584 */                 label_x = bar.x + 2;
/* 2585 */                 label_x = Math.max(label_x, grid.x + 2 - (display3dOn ? this.depth3dPoint.x : 0));
/* 2586 */                 label_x = Math.min(label_x, grid.x + grid.width - angledSize.width - 1 - (display3dOn ? this.depth3dPoint.x : 0));
/*      */               }
/*      */               
/* 2589 */               if ((this.barType == 1) && (seriesCount > 1)) {
/* 2590 */                 paint_label = angledSize.width < bar.width;
/*      */               }
/*      */             }
/*      */             
/*      */ 
/* 2595 */             if (this.gridAlignment == 1) {
/* 2596 */               if ((label_x + angledSize.width / 2 < grid_left - 3) || (label_x + angledSize.width / 2 > grid_right + 3)) {
/* 2597 */                 paint_label = false;
/*      */               }
/* 2599 */             } else if (angle % 180 == 0) {
/* 2600 */               if ((label_y - fheight / 2 < grid_top - 3) || (label_y - fheight / 2 > grid_bottom)) {
/* 2601 */                 paint_label = false;
/*      */               }
/* 2603 */             } else if ((label_y + angledSize.height / 2 < grid_top - 3) || (label_y + angledSize.height / 2 > grid_bottom)) {
/* 2604 */               paint_label = false;
/*      */             }
/*      */             
/* 2607 */             if (paint_label) {
/* 2608 */               g.setColor(fontColor);
/* 2609 */               paintLabel(g, label, label_x, label_y, labelSize, 0, angle, false);
/*      */             }
/*      */           }
/*      */           
/*      */ 
/* 2614 */           if ((bar != null) && ((this.seriesLabelStyle == 1) || (this.sampleLabelStyle == 1) || (this.valueLabelStyle == 1)) && (
/* 2615 */             (!stacked_and_outside) || (series >= seriesCount - 1)))
/*      */           {
/*      */ 
/*      */ 
/* 2619 */             int angle = 0;
/* 2620 */             Color fontColor = null;
/* 2621 */             Font font = seriesLabelFont;
/*      */             
/* 2623 */             if ((this.valueLabelStyle == 1) && (isValueLabelsOn(series))) {
/* 2624 */               angle = valueLabelAngle;
/* 2625 */               fontColor = getValueLabelColor(series);
/* 2626 */               font = valueLabelFont;
/*      */             }
/*      */             
/* 2629 */             if ((this.sampleLabelStyle == 1) && (this.sampleLabelsOn)) {
/* 2630 */               angle = sampleAngle;
/* 2631 */               if (getSampleLabelColor(sample) != null) {
/* 2632 */                 fontColor = getSampleLabelColor(sample);
/*      */               }
/* 2634 */               font = sampleLabelFont;
/*      */             }
/*      */             
/* 2637 */             if ((this.seriesLabelStyle == 1) && (this.seriesLabelsOn) && 
/* 2638 */               (fontColor == null)) {
/* 2639 */               fontColor = getSeriesLabelColor(series);
/*      */             }
/*      */             
/* 2642 */             fm = getFontMetrics(font);
/* 2643 */             g.setFont(font);
/*      */             
/* 2645 */             if (fontColor == null) {
/* 2646 */               fontColor = getChartForeground();
/*      */             }
/*      */             
/*      */ 
/* 2650 */             String label = constructLabel(series, sample, 1, this.seriesLabelsOn, null);
/* 2651 */             String negative_label = null;
/* 2652 */             Dimension negativeLabelSize = null;
/* 2653 */             Dimension negativeLabelAngledSize = null;
/* 2654 */             if (stacked_and_outside)
/*      */             {
/* 2656 */               String valueLabel = formatNumber(value, decimals);
/* 2657 */               valueLabel = prefix != null ? prefix + valueLabel : valueLabel;
/* 2658 */               valueLabel = postfix != null ? valueLabel + postfix : valueLabel;
/*      */               
/* 2660 */               String sampleLabel = "";
/* 2661 */               if ((this.sampleLabelsOn) && (this.sampleLabelStyle == 1)) {
/* 2662 */                 sampleLabel = getSampleLabel(sample);
/* 2663 */                 if ((sampleLabel != null) && 
/* 2664 */                   (!sampleLabel.endsWith("\n")) && (valueLabel != null) && (isValueLabelsOn()) && (this.valueLabelStyle == 1)) {
/* 2665 */                   sampleLabel = sampleLabel + " : ";
/*      */                 }
/*      */               }
/*      */               
/* 2669 */               label = (isValueLabelsOn()) && (this.valueLabelStyle == 1) ? sampleLabel + valueLabel : sampleLabel;
/* 2670 */               if (negative_value < 0.0D) {
/* 2671 */                 negative_label = formatNumber(negative_value, decimals);
/* 2672 */                 negative_label = prefix != null ? prefix + negative_label : negative_label;
/* 2673 */                 negative_label = postfix != null ? negative_label + postfix : negative_label;
/* 2674 */                 negativeLabelSize = getLabelSize(negative_label, getFontMetrics(valueLabelFont));
/* 2675 */                 negativeLabelAngledSize = getAngledLabelSize(negativeLabelSize, angle);
/*      */               }
/* 2677 */               angle = valueLabelAngle;
/* 2678 */               g.setFont(valueLabelFont);
/*      */             }
/*      */             
/* 2681 */             Dimension labelSize = getLabelSize(label, fm);
/* 2682 */             Dimension angledSize = getAngledLabelSize(labelSize, angle);
/*      */             
/*      */ 
/* 2685 */             if (((value >= 0.0D) && (this.upperRange[0] >= 0.0D)) || ((value < 0.0D) && (this.lowerRange[0] >= 0.0D))) {
/* 2686 */               if (this.gridAlignment == 1)
/*      */               {
/* 2688 */                 label_x = bar.x + bar.width / 2 - angledSize.width / 2;
/*      */                 
/*      */ 
/* 2691 */                 if (angle % 180 == 0) {
/* 2692 */                   label_y = bar.y - 2 - angledSize.height + fheight - descent;
/* 2693 */                   label_y = Math.max(grid_top - 2 - angledSize.height + fheight - descent, label_y);
/* 2694 */                   label_y = Math.min(grid_bottom - 2 - angledSize.height + fheight - descent, label_y);
/*      */                 } else {
/* 2696 */                   label_y = bar.y - 2 - angledSize.height;
/* 2697 */                   label_y = Math.max(label_y, grid_top - angledSize.height - 2);
/* 2698 */                   label_y = Math.min(label_y, grid_bottom - angledSize.height - 2);
/*      */                 }
/*      */                 
/*      */ 
/* 2702 */                 if ((angle % 180 > 0) && (angle % 180 < 90)) {
/* 2703 */                   label_x = bar.x + bar.width - angledSize.width + 1;
/* 2704 */                 } else if (angle % 180 > 90) {
/* 2705 */                   label_x = bar.x - 1;
/*      */                 }
/*      */                 
/* 2708 */                 if ((display3dOn) && ((seriesCount == 1) || (stacked_and_outside))) {
/* 2709 */                   label_y += this.depth3dPoint.y;
/* 2710 */                   label_x += this.depth3dPoint.x;
/* 2711 */                   if (angle % 180 == 0) {
/* 2712 */                     label_y = Math.max(grid_top - 2, label_y);
/*      */                   } else {
/* 2714 */                     label_y = Math.max(grid_top - angledSize.height - 2, label_y);
/*      */                   }
/*      */                 }
/* 2717 */                 if ((label_x + angledSize.width / 2 < grid_left - 3) || (label_x + angledSize.width / 2 > grid_right + 3)) {
/* 2718 */                   paint_label = false;
/*      */                 }
/*      */               }
/*      */               else
/*      */               {
/* 2723 */                 if (angle % 180 == 0) {
/* 2724 */                   label_y = bar.y + bar.height / 2 + angledSize.height / 2 - 1;
/*      */                 } else {
/* 2726 */                   label_y = bar.y + bar.height / 2 - angledSize.height / 2 + 1;
/*      */                 }
/* 2728 */                 label_x = bar.x + bar.width + 3;
/* 2729 */                 label_x = Math.min(grid.x + grid.width + 3, label_x);
/* 2730 */                 label_x = Math.max(grid_left + 3, label_x);
/*      */                 
/* 2732 */                 if ((display3dOn) && (seriesCount == 1)) {
/* 2733 */                   label_y += this.depth3dPoint.y;
/* 2734 */                   label_x += this.depth3dPoint.x;
/* 2735 */                   label_x = Math.min(grid.x + grid.width + 3, label_x);
/*      */                 }
/*      */                 
/* 2738 */                 if (angle % 180 == 0) {
/* 2739 */                   if ((label_y - fheight / 2 < grid_top - 3) || (label_y - fheight / 2 > grid_bottom)) {
/* 2740 */                     paint_label = false;
/*      */                   }
/* 2742 */                 } else if ((label_y + angledSize.height / 2 < grid_top - 3) || (label_y + angledSize.height / 2 > grid_bottom)) {
/* 2743 */                   paint_label = false;
/*      */                 }
/*      */               }
/*      */               
/* 2747 */               if ((paint_label) && ((value != 0.0D) || (negative_value >= 0.0D))) {
/* 2748 */                 g.setColor(fontColor);
/* 2749 */                 paintLabel(g, label, label_x, label_y, labelSize, 0, angle, false);
/*      */               }
/*      */             }
/*      */             
/*      */ 
/*      */ 
/* 2755 */             if ((stacked_and_outside) && (negative_value < 0.0D)) {
/* 2756 */               value = negative_value;
/*      */               
/* 2758 */               String valueLabel = formatNumber(value, decimals);
/* 2759 */               valueLabel = prefix != null ? prefix + valueLabel : valueLabel;
/* 2760 */               valueLabel = postfix != null ? valueLabel + postfix : valueLabel;
/*      */               
/* 2762 */               String sampleLabel = "";
/* 2763 */               if ((this.sampleLabelsOn) && (this.sampleLabelStyle == 1)) {
/* 2764 */                 sampleLabel = getSampleLabel(sample);
/* 2765 */                 if ((sampleLabel != null) && 
/* 2766 */                   (!sampleLabel.endsWith("\n")) && (valueLabel != null) && (isValueLabelsOn()) && (this.valueLabelStyle == 1)) {
/* 2767 */                   sampleLabel = sampleLabel + " : ";
/*      */                 }
/*      */               }
/*      */               
/* 2771 */               label = (isValueLabelsOn()) && (this.valueLabelStyle == 1) ? sampleLabel + valueLabel : sampleLabel;
/* 2772 */               labelSize = negativeLabelSize;
/* 2773 */               angledSize = negativeLabelAngledSize;
/*      */             }
/* 2775 */             if (((value < 0.0D) && (this.lowerRange[0] < 0.0D)) || ((value >= 0.0D) && (this.upperRange[0] < 0.0D))) {
/* 2776 */               if (this.gridAlignment == 1)
/*      */               {
/* 2778 */                 label_x = negative_bar.x + negative_bar.width / 2 - angledSize.width / 2;
/*      */                 
/* 2780 */                 if (angle % 180 == 0) {
/* 2781 */                   label_y = negative_bar.y + negative_bar.height + valueAscent;
/* 2782 */                   label_y = Math.min(grid_bottom + valueAscent, label_y);
/* 2783 */                   label_y = Math.max(label_y, display3dOn ? grid_top - this.depth3dPoint.y + valueAscent : grid_top + valueAscent);
/*      */                 }
/*      */                 else
/*      */                 {
/* 2787 */                   label_y = negative_bar.y + negative_bar.height + 3;
/* 2788 */                   label_y = Math.min(grid_bottom + 3, label_y);
/* 2789 */                   label_y = Math.max(label_y, display3dOn ? grid_top - this.depth3dPoint.y + 2 : grid_top + 2);
/*      */                 }
/*      */                 
/*      */ 
/* 2793 */                 if ((angle % 180 > 0) && (angle % 180 < 90)) {
/* 2794 */                   label_x = bar.x + bar.width - angledSize.width + 1;
/* 2795 */                 } else if (angle % 180 > 90) {
/* 2796 */                   label_x = bar.x - 1;
/*      */                 }
/* 2798 */                 if ((label_x + angledSize.width / 2 < grid_left - 3) || (label_x + angledSize.width / 2 > grid_right + 3)) {
/* 2799 */                   paint_label = false;
/*      */                 }
/*      */               }
/*      */               else {
/* 2803 */                 if (angle % 180 == 0) {
/* 2804 */                   label_y = negative_bar.y + bar.height / 2 + angledSize.height / 2 - 1;
/*      */                 } else {
/* 2806 */                   label_y = negative_bar.y + bar.height / 2 - angledSize.height / 2 + 1;
/*      */                 }
/* 2808 */                 label_x = negative_bar.x - angledSize.width - 3;
/* 2809 */                 label_x = Math.max(grid_left - labelSize.width - 3, label_x);
/* 2810 */                 label_x = Math.min(grid_left + grid.width - labelSize.width - 3, label_x);
/*      */                 
/* 2812 */                 if (angle % 180 == 0) {
/* 2813 */                   if ((label_y - fheight / 2 < grid_top - 3) || (label_y - fheight / 2 > grid_bottom)) {
/* 2814 */                     paint_label = false;
/*      */                   }
/* 2816 */                 } else if ((label_y + angledSize.height / 2 < grid_top - 3) || (label_y + angledSize.height / 2 > grid_bottom)) {
/* 2817 */                   paint_label = false;
/*      */                 }
/*      */               }
/* 2820 */               if (paint_label) {
/* 2821 */                 g.setColor(fontColor);
/* 2822 */                 paintLabel(g, label, label_x, label_y, labelSize, 0, angle, false);
/*      */               }
/*      */             }
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
/*      */   private void paintBarLabels(Graphics g, Rectangle gridBounds)
/*      */   {
/* 2838 */     boolean paint = (this.sampleLabelsOn) && (this.sampleLabelStyle == 2);
/* 2839 */     paint |= ((this.sampleLabelsOn) && (this.sampleLabelStyle == 4));
/* 2840 */     paint |= this.barLabelsOn;
/* 2841 */     if (!paint) {
/* 2842 */       return;
/*      */     }
/*      */     
/*      */ 
/* 2846 */     String[] labels = getSampleLabels();
/* 2847 */     if (this.barLabelsOn) {
/* 2848 */       labels = getBarLabels();
/*      */     }
/* 2850 */     if (labels == null) {
/* 2851 */       return;
/*      */     }
/*      */     
/*      */ 
/* 2855 */     Font barLabelFont = getFont("sampleLabelFont");
/* 2856 */     int angle = getLabelAngle("sampleLabelAngle");
/* 2857 */     if (this.barLabelsOn) {
/* 2858 */       barLabelFont = getFont("barLabelFont");
/* 2859 */       angle = getLabelAngle("barLabelAngle");
/*      */     }
/* 2861 */     FontMetrics fm = getFontMetrics(barLabelFont);
/* 2862 */     g.setFont(barLabelFont);
/*      */     
/*      */ 
/* 2865 */     if (this.gridAlignment == 1) {
/* 2866 */       int last_label_x = 0;
/* 2867 */       int start = Math.max(this.visibleSamples[0] - 1, 0);
/* 2868 */       int stop = Math.min(start + this.visibleSamples[1] + 1, labels.length);
/* 2869 */       for (int index = start; index < stop; index++)
/*      */       {
/* 2871 */         if (labels[index] != null)
/*      */         {
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 2877 */           boolean selected = false;
/* 2878 */           int series_count = getSeriesCount();
/* 2879 */           if (index < getSampleCount()) {
/* 2880 */             for (int i = 0; i < series_count; i++) {
/* 2881 */               if (isSelected(i, index)) {
/* 2882 */                 selected = true;
/* 2883 */                 break;
/*      */               }
/*      */             }
/*      */           }
/*      */           
/* 2888 */           Color fontColor = null;
/* 2889 */           if (isBarLabelsOn()) {
/* 2890 */             fontColor = getBarLabelColor(index);
/*      */           }
/* 2892 */           if (fontColor == null) {
/* 2893 */             fontColor = getSampleLabelColor(index);
/*      */           }
/* 2895 */           if (fontColor == null) {
/* 2896 */             fontColor = getForeground();
/*      */           }
/* 2898 */           if ((selected) && (getSampleLabelSelectionColor() != null)) {
/* 2899 */             g.setColor(getSampleLabelSelectionColor());
/* 2900 */           } else if (fontColor != null) {
/* 2901 */             g.setColor(fontColor);
/*      */           }
/*      */           
/*      */ 
/* 2905 */           Dimension labelSize = getLabelSize(labels[index], fm);
/* 2906 */           Dimension angledSize = getAngledLabelSize(labelSize, angle);
/* 2907 */           int label_x = Integer.MIN_VALUE;
/* 2908 */           int w = 0;
/* 2909 */           if ((this.barBounds != null) && (this.barBounds.length > 0) && (index < this.barBounds[0].length)) {
/* 2910 */             label_x = this.barBounds[0][index].x;
/* 2911 */             w = this.barBounds[(this.barBounds.length - 1)][index].x + this.barBounds[(this.barBounds.length - 1)][index].width - label_x;
/*      */           }
/* 2913 */           int center = label_x + w / 2;
/*      */           
/*      */ 
/*      */ 
/* 2917 */           if (angle % 90 == 0) {
/* 2918 */             label_x = center - angledSize.width / 2;
/*      */ 
/*      */ 
/*      */           }
/* 2922 */           else if (angle % 180 > 90) {
/* 2923 */             label_x = center + w / 2 - angledSize.width;
/*      */           } else {
/* 2925 */             label_x = center - w / 2;
/*      */           }
/*      */           
/*      */ 
/* 2929 */           int left = gridBounds.x - 2 - (is3DModeOn() ? this.depth3dPoint.x : 0);
/* 2930 */           if ((center >= left) && (center <= gridBounds.x + gridBounds.width + 2))
/*      */           {
/*      */ 
/*      */ 
/*      */ 
/* 2935 */             int label_y = gridBounds.y + gridBounds.height + 6;
/* 2936 */             if (angle % 180 == 0) {
/* 2937 */               label_y += fm.getMaxAscent() - 4;
/*      */             }
/*      */             
/*      */ 
/* 2941 */             if (is3DModeOn()) {
/* 2942 */               label_y -= this.depth3dPoint.y;
/*      */             }
/*      */             
/*      */ 
/* 2946 */             if (this.sampleScrollerOn) {
/* 2947 */               label_y += 10;
/*      */             }
/*      */             
/*      */ 
/* 2951 */             if ((isValueLabelsOn()) && (this.valueLabelStyle == 1) && (this.lowerRange[0] < 0.0D))
/*      */             {
/* 2953 */               int bottom = gridBounds.y;
/* 2954 */               int zero = this.zeroLine[0] - (is3DModeOn() ? this.depth3dPoint.y : 0);
/* 2955 */               for (int serie = 0; serie < this.barBounds.length; serie++)
/*      */               {
/* 2957 */                 int y = this.barBounds[serie][index].y + this.barBounds[serie][index].height;
/* 2958 */                 if ((y > bottom) && (y > zero)) {
/* 2959 */                   bottom = y;
/*      */                 }
/*      */               }
/* 2962 */               label_y = Math.max(label_y, bottom + fm.getMaxAscent() + 10);
/* 2963 */               bottom = gridBounds.y + gridBounds.height - (is3DModeOn() ? this.depth3dPoint.y : 0);
/* 2964 */               label_y = Math.min(label_y, bottom + fm.getMaxAscent() + 10);
/*      */             }
/*      */             
/*      */ 
/* 2968 */             if (this.autoLabelSpacingOn) {
/* 2969 */               if ((labels[index] != null) && (labels[index].trim().length() > 0) && (
/* 2970 */                 (label_x > last_label_x) || (index == 0))) {
/* 2971 */                 paintLabel(g, labels[index], label_x, label_y, labelSize, 0, angle, false);
/* 2972 */                 last_label_x = label_x + angledSize.width;
/*      */               }
/*      */             }
/*      */             else {
/* 2976 */               paintLabel(g, labels[index], label_x, label_y, labelSize, 0, angle, false);
/*      */             }
/*      */           }
/*      */         }
/*      */       }
/*      */     }
/*      */     else
/*      */     {
/* 2984 */       int last_label_y = 0;
/* 2985 */       int count = Math.min(labels.length, getSampleCount());
/* 2986 */       int start = Math.max(this.visibleSamples[0] - 1, 0);
/* 2987 */       int stop = Math.min(start + this.visibleSamples[1] + 1, count);
/* 2988 */       for (int index = start; index < stop; index++)
/*      */       {
/* 2990 */         if (labels[index] != null)
/*      */         {
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 2996 */           boolean selected = false;
/* 2997 */           int series_count = getSeriesCount();
/* 2998 */           for (int i = 0; i < series_count; i++) {
/* 2999 */             if (isSelected(i, index)) {
/* 3000 */               selected = true;
/* 3001 */               break;
/*      */             }
/*      */           }
/*      */           
/*      */ 
/* 3006 */           Color fontColor = null;
/* 3007 */           if (isBarLabelsOn()) {
/* 3008 */             fontColor = getBarLabelColor(index);
/*      */           }
/* 3010 */           if (fontColor == null) {
/* 3011 */             fontColor = getSampleLabelColor(index);
/*      */           }
/* 3013 */           if (fontColor == null) {
/* 3014 */             fontColor = getForeground();
/*      */           }
/* 3016 */           if ((selected) && (getSampleLabelSelectionColor() != null)) {
/* 3017 */             g.setColor(getSampleLabelSelectionColor());
/* 3018 */           } else if (fontColor != null) {
/* 3019 */             g.setColor(fontColor);
/*      */           }
/*      */           
/*      */ 
/* 3023 */           Dimension labelSize = getLabelSize(labels[index], fm);
/* 3024 */           Dimension angledSize = getAngledLabelSize(labelSize, angle);
/* 3025 */           int height = angledSize.height;
/* 3026 */           if ((this.barBounds.length > 0) && 
/* 3027 */             (index < this.barBounds[(this.barBounds.length - 1)].length) && (index < this.barBounds[0].length)) {
/* 3028 */             height = this.barBounds[(this.barBounds.length - 1)][index].y + this.barBounds[(this.barBounds.length - 1)][index].height - this.barBounds[0][index].y;
/*      */           }
/*      */           
/* 3031 */           int center = 0;
/* 3032 */           if (index < this.barBounds[0].length) {
/* 3033 */             center = this.barBounds[0][index].y + height / 2;
/*      */           }
/*      */           
/*      */ 
/* 3037 */           int label_y = center - angledSize.height / 2;
/* 3038 */           if (angle % 180 == 0) {
/* 3039 */             label_y += fm.getAscent() - 1;
/*      */ 
/*      */           }
/* 3042 */           else if (angle % 180 > 90) {
/* 3043 */             label_y = center - height / 2;
/*      */ 
/*      */           }
/* 3046 */           else if (angle % 180 != 90) {
/* 3047 */             label_y = center + height / 2 - angledSize.height;
/*      */           }
/*      */           
/*      */ 
/* 3051 */           if (center >= gridBounds.y) { if (center <= gridBounds.y + gridBounds.height - (is3DModeOn() ? this.depth3dPoint.y : 0))
/*      */             {
/*      */ 
/*      */ 
/*      */ 
/* 3056 */               int label_x = gridBounds.x - angledSize.width - 3;
/* 3057 */               if (is3DModeOn()) {
/* 3058 */                 label_x -= this.depth3dPoint.x;
/*      */               }
/*      */               
/*      */ 
/* 3062 */               if (this.sampleScrollerOn) {
/* 3063 */                 label_x -= 10;
/*      */               }
/*      */               
/*      */ 
/*      */ 
/*      */ 
/* 3069 */               if ((isValueLabelsOn()) && (this.valueLabelStyle == 1) && (this.lowerRange[0] < 0.0D))
/*      */               {
/* 3071 */                 int left = gridBounds.x + gridBounds.width;
/* 3072 */                 int value_label_width = 0;
/* 3073 */                 double value = 0.0D;
/* 3074 */                 for (int serie = 0; serie < this.barBounds.length; serie++)
/*      */                 {
/* 3076 */                   if (getSampleValue(serie, index) < 0.0D)
/*      */                   {
/*      */ 
/*      */ 
/* 3080 */                     if (this.barType == 1) {
/* 3081 */                       value += getSampleValue(serie, index);
/*      */                     } else {
/* 3083 */                       value = getSampleValue(serie, index);
/*      */                     }
/*      */                     
/* 3086 */                     if (this.barBounds[serie][index].x < left) {
/* 3087 */                       String prefix = getLabel("valueLabelPrefix");
/* 3088 */                       String postfix = getLabel("valueLabelPostfix");
/* 3089 */                       left = this.barBounds[serie][index].x;
/* 3090 */                       String label = formatNumber(value, getSampleDecimalCount(0));
/* 3091 */                       label = prefix != null ? prefix + label : label;
/* 3092 */                       label = postfix != null ? label + postfix : label;
/* 3093 */                       value_label_width = fm.stringWidth(label);
/*      */                     }
/*      */                   } }
/* 3096 */                 label_x = Math.min(label_x, left - value_label_width - angledSize.width - 3);
/* 3097 */                 left = gridBounds.x - (is3DModeOn() ? this.depth3dPoint.x : 0);
/* 3098 */                 label_x = Math.max(label_x, left - value_label_width - angledSize.width - 3);
/* 3099 */                 left -= (this.sampleScrollerOn ? 10 : 0);
/* 3100 */                 label_x = Math.min(label_x, left - angledSize.width - 3);
/*      */               }
/*      */               
/*      */ 
/*      */ 
/* 3105 */               if (this.autoLabelSpacingOn) {
/* 3106 */                 if ((labels[index] != null) && (labels[index].trim().length() > 0) && (
/* 3107 */                   (label_y > last_label_y) || (index == 0))) {
/* 3108 */                   paintLabel(g, labels[index], label_x, label_y, labelSize, 0, angle, false);
/* 3109 */                   last_label_y = label_y + angledSize.height;
/*      */                 }
/*      */               }
/*      */               else {
/* 3113 */                 paintLabel(g, labels[index], label_x, label_y, labelSize, 1, angle, false);
/*      */               }
/*      */             }
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
/*      */   void paintFloatingLabel(Graphics g, String label, Rectangle grid, int sample, int series, Font font, FontMetrics fm)
/*      */   {
/* 3132 */     if ((series < 0) || (series >= getSeriesCount()) || (sample < 0) || (sample >= getSampleCount()) || (fm == null)) {
/* 3133 */       return;
/*      */     }
/*      */     
/*      */ 
/* 3137 */     Rectangle bar = this.barBounds[series][sample];
/* 3138 */     if (bar == null) {
/* 3139 */       return;
/*      */     }
/*      */     
/*      */ 
/*      */ 
/* 3144 */     if (this.gridAlignment == 1)
/*      */     {
/* 3146 */       if ((bar.x + bar.width < grid.x - this.depth3dPoint.x) || (bar.x > grid.x + grid.width)) {
/* 3147 */         return;
/*      */       }
/*      */       
/* 3150 */       if ((bar.y + bar.height >= grid.y) && (bar.y <= grid.y + grid.height - this.depth3dPoint.y)) {}
/*      */ 
/*      */     }
/*      */     else
/*      */     {
/* 3155 */       if ((bar.y + bar.height < grid.y) || (bar.y > grid.y + grid.height - this.depth3dPoint.y)) {
/* 3156 */         return;
/*      */       }
/*      */       
/* 3159 */       if ((bar.x + bar.width < grid.x - this.depth3dPoint.x) || (bar.x > grid.x + grid.width)) {
/* 3160 */         return;
/*      */       }
/*      */     }
/*      */     
/*      */ 
/* 3165 */     Dimension labelSize = getLabelSize(label, getFontMetrics(font));
/* 3166 */     int label_x = this.mousePosition.x + 3;
/* 3167 */     if (this.gridAlignment == 1) {
/* 3168 */       label_x = bar.x + bar.width / 2 - labelSize.width / 2 + 1;
/*      */     }
/* 3170 */     else if (this.mousePosition.x < 0) {
/* 3171 */       label_x = bar.x + bar.width / 2 - labelSize.width / 2;
/*      */     } else {
/* 3173 */       label_x = this.mousePosition.x - labelSize.width - 3;
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 3180 */     int label_y = this.mousePosition.y - labelSize.height + fm.getAscent();
/* 3181 */     if ((this.gridAlignment == 0) || (this.mousePosition.y < 0)) {
/* 3182 */       label_y = bar.y + bar.height / 2 - labelSize.height / 2 + fm.getAscent();
/*      */     }
/*      */     
/*      */ 
/* 3186 */     if ((this.barType == 1) && (label_y - labelSize.height / 2 < bar.y)) {
/* 3187 */       label_y = bar.y + labelSize.height / 2 + 1;
/*      */ 
/*      */     }
/* 3190 */     else if ((is3DModeOn()) && (this.depth3dPoint.y != 0) && (this.gridAlignment == 1) && 
/* 3191 */       (this.mousePosition.y < bar.y) && (this.mousePosition.y >= 0)) {
/* 3192 */       label_x += -this.depth3dPoint.x * (bar.y - this.mousePosition.y) / this.depth3dPoint.y;
/*      */       
/*      */ 
/* 3195 */       Rectangle nextBarBounds = null;
/* 3196 */       if (series < getSeriesCount() - 1) {
/* 3197 */         nextBarBounds = this.barBounds[(series + 1)][sample];
/* 3198 */       } else if (sample < getSampleCount() - 1) {
/* 3199 */         nextBarBounds = this.barBounds[0][(sample + 1)];
/*      */       }
/*      */       
/* 3202 */       if (nextBarBounds != null) {
/* 3203 */         int offset = -this.depth3dPoint.x * (nextBarBounds.y - this.mousePosition.y) / this.depth3dPoint.y;
/* 3204 */         if ((label_y >= nextBarBounds.y) && (label_x > nextBarBounds.x - 10)) {
/* 3205 */           label_x = nextBarBounds.x - 10;
/* 3206 */         } else if ((label_y < nextBarBounds.y) && (label_x > nextBarBounds.x + offset - 10)) {
/* 3207 */           label_x = nextBarBounds.x + offset - 10;
/*      */         }
/*      */       }
/*      */     }
/*      */     
/* 3212 */     Color frameColor = getChartForeground();
/* 3213 */     if ((getSeriesCount() == 1) && (this.multiColorOn)) {
/* 3214 */       frameColor = getSampleColor(sample).darker();
/* 3215 */     } else if (this.multiColorOn) {
/* 3216 */       frameColor = getSampleColor(series).darker();
/*      */     }
/* 3218 */     paintFloatingLabel(g, label, label_x, label_y, labelSize, frameColor, fm);
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
/* 3232 */     boolean labels_on = (isValueLabelsOn(serie)) && (this.valueLabelStyle == 3);
/* 3233 */     labels_on |= ((this.seriesLabelsOn) && (this.seriesLabelStyle == 3));
/* 3234 */     labels_on |= ((this.sampleLabelsOn) && ((this.sampleLabelStyle == 3) || (this.sampleLabelStyle == 4)));
/* 3235 */     return labels_on;
/*      */   }
/*      */ }


/* Location:              E:\java\java学习\hutubill\lib\all.jar!\com\objectplanet\chart\BarChart.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */
/*      */ package com.objectplanet.chart;
/*      */ 
/*      */ import java.awt.AWTEvent;
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
/*      */ import java.awt.event.MouseEvent;
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
/*      */ public class PieChart
/*      */   extends Chart
/*      */ {
/*      */   public static final int SELECTION_STYLE_TRIANGLE = 0;
/*      */   public static final int SELECTION_STYLE_CIRCLE = 1;
/*      */   public static final int SELECTION_STYLE_DETACHED = 2;
/*      */   private int pieAngle;
/*      */   private double pieDepth;
/*      */   private boolean percentLabelsOn;
/*      */   private boolean pieLabelsOn;
/*      */   private int percentDecimalCount;
/*      */   private int selectionStyle;
/*      */   private boolean sliceSeperatorOn;
/*      */   private Color sliceSeperatorColor;
/*      */   private double detachedDistance;
/*      */   private Hashtable detachedSlices;
/*      */   private int percentLabelStyle;
/*      */   private Color insideLabelColor;
/*      */   private Color[] insideLabelColors;
/*      */   private Color outsideLabelColor;
/*      */   private Color[] outsideLabelColors;
/*      */   private Color pointingLabelColor;
/*      */   private Color[] pointingLabelColors;
/*      */   private double[][] angle_start;
/*      */   private double[][] angle_end;
/*      */   private Rectangle[] pieBounds;
/*      */   private Rectangle[] pieOuterBounds;
/*      */   private Point[] pieCenter;
/*      */   private int[] pieWidth;
/*      */   private int pointingDistance;
/*      */   private double size_factor;
/*      */   private static final int PAINT_BOTTOM = 0;
/*      */   private static final int PAINT_SIDES = 1;
/*      */   private static final int PAINT_TOP = 2;
/*      */   private boolean pieRotationOn;
/*      */   private double oldAngle;
/*      */   private double[] rotateAngles;
/*      */   private int rotatedPie;
/*      */   
/*      */   public PieChart()
/*      */   {
/*  108 */     this(1);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public PieChart(int sampleCount)
/*      */   {
/*  117 */     this(1, sampleCount);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public PieChart(int seriesCount, int sampleCount)
/*      */   {
/*  127 */     super(seriesCount, sampleCount);
/*  128 */     this.chartType = "pie";
/*  129 */     this.insideLabelColors = new Color[sampleCount];
/*  130 */     this.outsideLabelColors = new Color[sampleCount];
/*  131 */     this.pointingLabelColors = new Color[sampleCount];
/*  132 */     this.angle_start = new double[seriesCount][sampleCount];
/*  133 */     this.angle_end = new double[seriesCount][sampleCount];
/*  134 */     this.detachedSlices = new Hashtable();
/*  135 */     this.pieBounds = new Rectangle[seriesCount == 1 ? 1 : sampleCount];
/*  136 */     this.pieOuterBounds = new Rectangle[seriesCount == 1 ? 1 : sampleCount];
/*  137 */     this.pieCenter = new Point[this.pieBounds.length];
/*  138 */     this.pieWidth = new int[this.pieBounds.length];
/*  139 */     for (int i = 0; i < this.pieBounds.length; i++) {
/*  140 */       this.pieBounds[i] = new Rectangle();
/*  141 */       this.pieOuterBounds[i] = new Rectangle();
/*  142 */       this.pieCenter[i] = new Point();
/*      */     }
/*  144 */     this.pieAngle = 20;
/*  145 */     this.pieDepth = 0.4000000059604645D;
/*  146 */     this.sampleLabelsOn = false;
/*  147 */     this.percentLabelsOn = false;
/*  148 */     this.pieLabelsOn = false;
/*  149 */     this.percentDecimalCount = 0;
/*  150 */     this.selectionStyle = 0;
/*  151 */     this.sliceSeperatorOn = true;
/*  152 */     this.sliceSeperatorColor = null;
/*  153 */     this.detachedDistance = 0.1D;
/*  154 */     this.percentLabelStyle = 3;
/*  155 */     this.valueLabelStyle = 3;
/*  156 */     this.sampleLabelStyle = 3;
/*  157 */     this.seriesLabelStyle = 3;
/*  158 */     this.rotateAngles = new double[1];
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public void reset()
/*      */   {
/*  166 */     super.reset();
/*  167 */     this.pieAngle = 20;
/*  168 */     this.pieDepth = 0.4000000059604645D;
/*  169 */     this.percentLabelsOn = false;
/*  170 */     this.pieLabelsOn = false;
/*  171 */     this.percentDecimalCount = 0;
/*  172 */     this.selectionStyle = 0;
/*  173 */     this.sliceSeperatorOn = true;
/*  174 */     this.sliceSeperatorColor = null;
/*  175 */     this.detachedDistance = 0.1D;
/*  176 */     if (this.detachedSlices != null) {
/*  177 */       this.detachedSlices.clear();
/*      */     }
/*  179 */     this.pieRotationOn = false;
/*  180 */     this.percentLabelStyle = 3;
/*  181 */     this.valueLabelStyle = 3;
/*  182 */     this.sampleLabelStyle = 3;
/*  183 */     this.seriesLabelStyle = 3;
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
/*      */   public double getPercentValue(int serie, int index)
/*      */   {
/*  200 */     if ((serie < 0) || (serie >= getSeriesCount())) {
/*  201 */       throw new IllegalArgumentException("Invalid series index: " + serie);
/*      */     }
/*  203 */     if ((index < 0) || (index >= getSampleCount())) {
/*  204 */       throw new IllegalArgumentException("Invalid sample index: " + index);
/*      */     }
/*      */     
/*      */ 
/*  208 */     int seriesCount = getSeriesCount();
/*  209 */     if (seriesCount == 1)
/*      */     {
/*  211 */       int sampleCount = getSampleCount();
/*  212 */       double sum = 0.0D;
/*  213 */       for (int i = 0; i < sampleCount; i++) {
/*  214 */         double value = getSampleValue(0, i);
/*  215 */         if (value > 0.0D) {
/*  216 */           sum += value;
/*      */         }
/*      */       }
/*      */       
/*  220 */       double value = getSampleValue(0, index);
/*  221 */       if (value >= 0.0D) {
/*  222 */         return value / sum * 100.0D;
/*      */       }
/*      */       
/*      */ 
/*      */     }
/*      */     else
/*      */     {
/*  229 */       double sum = 0.0D;
/*  230 */       for (int i = 0; i < seriesCount; i++) {
/*  231 */         double value = getSampleValue(i, index);
/*  232 */         if (value > 0.0D) {
/*  233 */           sum += value;
/*      */         }
/*      */       }
/*      */       
/*  237 */       double value = getSampleValue(serie, index);
/*  238 */       if (value > 0.0D) {
/*  239 */         return value / sum * 100.0D;
/*      */       }
/*      */     }
/*  242 */     return 0.0D;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void setAngle(int angle)
/*      */   {
/*  252 */     if (this.pieAngle != angle) {
/*  253 */       this.pieAngle = angle;
/*  254 */       this.needRender = true;
/*  255 */       this.needChartCalculation = true;
/*  256 */       autoRepaint();
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public int getAngle()
/*      */   {
/*  266 */     return this.pieAngle;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void setDepth(double depth)
/*      */   {
/*  278 */     if (this.pieDepth != depth) {
/*  279 */       this.pieDepth = depth;
/*  280 */       this.needRender = true;
/*  281 */       this.needChartCalculation = true;
/*  282 */       autoRepaint();
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public float getDepth()
/*      */   {
/*  292 */     return (float)this.pieDepth;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void setPercentDecimalCount(int count)
/*      */   {
/*  303 */     if (this.percentDecimalCount != count) {
/*  304 */       this.percentDecimalCount = count;
/*  305 */       this.needRender = true;
/*  306 */       autoRepaint();
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public int getPercentDecimalCount()
/*      */   {
/*  315 */     return this.percentDecimalCount;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void setPercentLabelsOn(boolean on)
/*      */   {
/*  327 */     if (this.percentLabelsOn != on) {
/*  328 */       this.percentLabelsOn = on;
/*  329 */       this.needRender = true;
/*  330 */       autoRepaint();
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public boolean isPercentLabelsOn()
/*      */   {
/*  340 */     return this.percentLabelsOn;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void setPercentLabelStyle(int style)
/*      */   {
/*  351 */     if (this.percentLabelStyle != style) {
/*  352 */       this.percentLabelStyle = 3;
/*  353 */       if ((style == 0) || (style == 1) || (style == 5)) {
/*  354 */         this.percentLabelStyle = style;
/*      */       }
/*  356 */       this.needRender = true;
/*  357 */       autoRepaint();
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public int getPercentLabelStyle()
/*      */   {
/*  368 */     return this.percentLabelStyle;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void setPieLabelsOn(boolean on)
/*      */   {
/*  378 */     if (this.pieLabelsOn != on) {
/*  379 */       this.pieLabelsOn = on;
/*  380 */       this.needRender = true;
/*  381 */       this.needChartCalculation = true;
/*  382 */       autoRepaint();
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public boolean isPieLabelsOn()
/*      */   {
/*  393 */     return this.pieLabelsOn;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void setInsideLabelColor(int index, Color color)
/*      */   {
/*  405 */     if (index >= 0) {
/*  406 */       if (index >= this.insideLabelColors.length) {
/*  407 */         Color[] newinsideLabelColors = new Color[index + 1];
/*  408 */         System.arraycopy(this.insideLabelColors, 0, newinsideLabelColors, 0, this.insideLabelColors.length);
/*  409 */         this.insideLabelColors = newinsideLabelColors;
/*      */       }
/*  411 */       this.insideLabelColors[index] = color;
/*  412 */       autoRepaint();
/*      */     } else {
/*  414 */       this.insideLabelColor = color;
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
/*      */   public Color getInsideLabelColor(int index)
/*      */   {
/*  427 */     if (index == -1) {
/*  428 */       return this.insideLabelColor;
/*      */     }
/*      */     try {
/*  431 */       return this.insideLabelColors[index];
/*      */     } catch (IndexOutOfBoundsException e) {
/*  433 */       throw new IllegalArgumentException("Invalid pie index: " + index);
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
/*      */   public void setOutsideLabelColor(int index, Color color)
/*      */   {
/*  446 */     if (index >= 0) {
/*  447 */       if (index >= this.outsideLabelColors.length) {
/*  448 */         Color[] newOutsideLabelColors = new Color[index + 1];
/*  449 */         System.arraycopy(this.outsideLabelColors, 0, newOutsideLabelColors, 0, this.outsideLabelColors.length);
/*  450 */         this.outsideLabelColors = newOutsideLabelColors;
/*      */       }
/*  452 */       this.outsideLabelColors[index] = color;
/*  453 */       autoRepaint();
/*      */     } else {
/*  455 */       this.outsideLabelColor = color;
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
/*      */   public Color getOutsideLabelColor(int index)
/*      */   {
/*  468 */     if (index == -1) {
/*  469 */       return this.outsideLabelColor;
/*      */     }
/*      */     try {
/*  472 */       return this.outsideLabelColors[index];
/*      */     } catch (IndexOutOfBoundsException e) {
/*  474 */       throw new IllegalArgumentException("Invalid pie index: " + index);
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
/*      */   public void setPointingLabelColor(int index, Color color)
/*      */   {
/*  487 */     if (index >= 0) {
/*  488 */       if (index >= this.pointingLabelColors.length) {
/*  489 */         Color[] newPointingLabelColors = new Color[index + 1];
/*  490 */         System.arraycopy(this.pointingLabelColors, 0, newPointingLabelColors, 0, this.pointingLabelColors.length);
/*  491 */         this.pointingLabelColors = newPointingLabelColors;
/*      */       }
/*  493 */       this.pointingLabelColors[index] = color;
/*  494 */       autoRepaint();
/*      */     } else {
/*  496 */       this.pointingLabelColor = color;
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
/*      */   public Color getPointingLabelColor(int index)
/*      */   {
/*  509 */     if (index == -1) {
/*  510 */       return this.pointingLabelColor;
/*      */     }
/*      */     try {
/*  513 */       return this.pointingLabelColors[index];
/*      */     } catch (IndexOutOfBoundsException e) {
/*  515 */       throw new IllegalArgumentException("Invalid pie index: " + index);
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
/*      */   public void setFont(String label, Font font)
/*      */   {
/*  528 */     super.setFont(label, font);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void setSelectionStyle(int style)
/*      */   {
/*  540 */     if (this.selectionStyle != style) {
/*  541 */       this.selectionStyle = style;
/*  542 */       this.needRender = true;
/*  543 */       this.needChartCalculation = true;
/*  544 */       autoRepaint();
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public int getSelectionStyle()
/*      */   {
/*  555 */     return this.selectionStyle;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void setDetachedDistance(double distance)
/*      */   {
/*  567 */     if (this.detachedDistance != distance) {
/*  568 */       this.detachedDistance = distance;
/*  569 */       this.needRender = true;
/*  570 */       this.needChartCalculation = true;
/*  571 */       autoRepaint();
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public double getDetachedDistance()
/*      */   {
/*  581 */     return this.detachedDistance;
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
/*      */   public void setDetachedSlice(int serie, int index, double distance)
/*      */   {
/*  594 */     Double key = new Double(serie * 1000000 + index);
/*  595 */     if (distance != 0.0D) {
/*  596 */       this.detachedSlices.put(key, new Double(distance));
/*      */     } else {
/*  598 */       this.detachedSlices.remove(key);
/*      */     }
/*  600 */     this.needRender = true;
/*  601 */     this.needChartCalculation = true;
/*  602 */     autoRepaint();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public double getDetachedSlice(int serie, int index)
/*      */   {
/*  613 */     Double key = new Double(serie * 1000000 + index);
/*  614 */     Double value = (Double)this.detachedSlices.get(key);
/*  615 */     if (value != null) {
/*  616 */       return value.doubleValue();
/*      */     }
/*  618 */     return 0.0D;
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
/*      */   public void setSliceSeperatorOn(boolean state)
/*      */   {
/*  632 */     if (this.sliceSeperatorOn != state) {
/*  633 */       this.sliceSeperatorOn = state;
/*  634 */       this.needRender = true;
/*  635 */       autoRepaint();
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public boolean isSliceSeperatorOn()
/*      */   {
/*  646 */     return this.sliceSeperatorOn;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void setSliceSeperatorColor(Color color)
/*      */   {
/*  657 */     if (this.sliceSeperatorColor != color) {
/*  658 */       this.sliceSeperatorColor = color;
/*  659 */       this.needRender = true;
/*  660 */       autoRepaint();
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public Color getSliceSeperatorColor()
/*      */   {
/*  670 */     return this.sliceSeperatorColor;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void setPieRotationOn(boolean on)
/*      */   {
/*  680 */     this.pieRotationOn = on;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public boolean isPieRotationOn()
/*      */   {
/*  689 */     return this.pieRotationOn;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public Polygon getSlicePolygon(int serie, int sample)
/*      */   {
/*  700 */     Polygon poly = new Polygon();
/*      */     
/*      */ 
/*      */ 
/*  704 */     int pieIndex = 0;
/*  705 */     double start = this.angle_start[0][sample];
/*  706 */     double stop = this.angle_end[0][sample];
/*  707 */     double detach = 0.0D;
/*  708 */     if (getSeriesCount() == 1) {
/*  709 */       detach = getDetachedSlice(0, sample);
/*      */     } else {
/*  711 */       detach = getDetachedSlice(sample, serie);
/*  712 */       pieIndex = sample;
/*  713 */       start = this.angle_start[serie][pieIndex];
/*  714 */       stop = this.angle_end[serie][pieIndex];
/*      */     }
/*  716 */     start = -start;
/*  717 */     stop = -stop;
/*      */     
/*      */ 
/*  720 */     int width = this.pieWidth[pieIndex];
/*  721 */     Point center = new Point();
/*  722 */     center.x = this.pieCenter[pieIndex].x;
/*  723 */     center.y = this.pieCenter[pieIndex].y;
/*      */     
/*      */ 
/*  726 */     double factor = 1.0D;
/*  727 */     if (is3DModeOn()) {
/*  728 */       int change = (int)Math.round(width * (this.pieAngle / 90.0D));
/*  729 */       factor = (width - change) / width;
/*      */     }
/*      */     
/*      */ 
/*  733 */     double rad = (start + stop) * 0.017453292519943295D / 2.0D;
/*  734 */     int angle_x = center.x + (int)Math.round(Math.cos(rad) * width * 0.5D * detach);
/*  735 */     int angle_y = center.y + (int)Math.round(Math.sin(rad) * width * 0.5D * detach * factor);
/*  736 */     poly.addPoint(angle_x, angle_y);
/*      */     
/*      */ 
/*      */ 
/*  740 */     rad = start * 0.017453292519943295D;
/*  741 */     angle_x = center.x + (int)Math.round(Math.cos(rad) * width * 0.5D * (1.0D + detach));
/*  742 */     angle_y = center.y + (int)Math.round(Math.sin(rad) * width * 0.5D * (1.0D + detach) * factor);
/*  743 */     poly.addPoint(angle_x, angle_y);
/*      */     
/*      */ 
/*      */ 
/*  747 */     double cur_angle = start + 5.0D;
/*  748 */     while (cur_angle < stop) {
/*  749 */       rad = cur_angle * 0.017453292519943295D;
/*  750 */       angle_x = center.x + (int)Math.round(Math.cos(rad) * width * 0.5D * (1.0D + detach));
/*  751 */       angle_y = center.y + (int)Math.round(Math.sin(rad) * width * 0.5D * (1.0D + detach) * factor);
/*  752 */       poly.addPoint(angle_x, angle_y);
/*  753 */       cur_angle += 5.0D;
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*  758 */     rad = stop * 0.017453292519943295D;
/*  759 */     angle_x = center.x + (int)Math.round(Math.cos(rad) * width * 0.5D * (1.0D + detach));
/*  760 */     angle_y = center.y + (int)Math.round(Math.sin(rad) * width * 0.5D * (1.0D + detach) * factor);
/*  761 */     poly.addPoint(angle_x, angle_y);
/*  762 */     return poly;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected void checkDataIntegrity()
/*      */   {
/*  771 */     super.checkDataIntegrity();
/*  772 */     int seriesCount = getSeriesCount();
/*  773 */     int sampleCount = getSampleCount();
/*  774 */     this.angle_start = new double[seriesCount][sampleCount];
/*  775 */     this.angle_end = new double[seriesCount][sampleCount];
/*  776 */     this.pieBounds = new Rectangle[seriesCount == 1 ? 1 : sampleCount];
/*  777 */     this.pieOuterBounds = new Rectangle[seriesCount == 1 ? 1 : sampleCount];
/*  778 */     this.pieCenter = new Point[this.pieBounds.length];
/*  779 */     this.pieWidth = new int[this.pieBounds.length];
/*  780 */     for (int i = 0; i < this.pieBounds.length; i++) {
/*  781 */       this.pieBounds[i] = new Rectangle();
/*  782 */       this.pieOuterBounds[i] = new Rectangle();
/*  783 */       this.pieCenter[i] = new Point();
/*      */     }
/*      */     
/*      */ 
/*  787 */     if ((this.insideLabelColors != null) && (sampleCount != this.insideLabelColors.length)) {
/*  788 */       Color[] newinsideLabelColors = new Color[sampleCount];
/*  789 */       int count = Math.min(sampleCount, this.insideLabelColors.length);
/*  790 */       System.arraycopy(this.insideLabelColors, 0, newinsideLabelColors, 0, count);
/*  791 */       this.insideLabelColors = newinsideLabelColors;
/*      */     }
/*      */     
/*      */ 
/*  795 */     if ((this.outsideLabelColors != null) && (sampleCount != this.outsideLabelColors.length)) {
/*  796 */       Color[] newoutsideLabelColors = new Color[sampleCount];
/*  797 */       int count = Math.min(sampleCount, this.outsideLabelColors.length);
/*  798 */       System.arraycopy(this.outsideLabelColors, 0, newoutsideLabelColors, 0, count);
/*  799 */       this.outsideLabelColors = newoutsideLabelColors;
/*      */     }
/*      */     
/*      */ 
/*  803 */     if ((this.pointingLabelColors != null) && (sampleCount != this.pointingLabelColors.length)) {
/*  804 */       Color[] newpointingLabelColors = new Color[sampleCount];
/*  805 */       int count = Math.min(sampleCount, this.pointingLabelColors.length);
/*  806 */       System.arraycopy(this.pointingLabelColors, 0, newpointingLabelColors, 0, count);
/*  807 */       this.pointingLabelColors = newpointingLabelColors;
/*      */     }
/*      */     
/*      */ 
/*  811 */     if ((this.rotateAngles != null) && (sampleCount != this.rotateAngles.length)) {
/*  812 */       double[] newRotateAngles = new double[sampleCount];
/*  813 */       int count = Math.min(sampleCount, this.rotateAngles.length);
/*  814 */       System.arraycopy(this.rotateAngles, 0, newRotateAngles, 0, count);
/*  815 */       this.rotateAngles = newRotateAngles;
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
/*      */   public ChartSample checkSelection(Point point)
/*      */   {
/*  830 */     if (point == null) {
/*  831 */       return null;
/*      */     }
/*  833 */     this.mouseOverSeriesIndex = -1;
/*  834 */     this.mouseOverSampleIndex = -1;
/*      */     
/*      */ 
/*  837 */     ChartSample sample = super.checkSelection(point);
/*      */     
/*      */ 
/*  840 */     if (sample == null)
/*      */     {
/*  842 */       int pieIndex = -1;
/*  843 */       Rectangle bounds = null;
/*  844 */       for (int i = 0; (this.pieBounds != null) && (i < this.pieBounds.length); i++) {
/*  845 */         bounds = this.pieBounds[i];
/*  846 */         if ((bounds != null) && (bounds.contains(point))) {
/*  847 */           pieIndex = i;
/*  848 */           break;
/*      */         }
/*      */       }
/*      */       
/*      */ 
/*      */ 
/*  854 */       if (pieIndex >= 0)
/*      */       {
/*  856 */         point.x -= this.pieCenter[pieIndex].x;
/*  857 */         point.y -= this.pieCenter[pieIndex].y;
/*      */         
/*      */ 
/*      */ 
/*  861 */         if (is3DModeOn()) {
/*  862 */           int width = this.pieWidth[pieIndex];
/*  863 */           int change = (int)Math.round(width * (this.pieAngle / 90.0D));
/*  864 */           double factor = width / (width - change);
/*  865 */           point.y = ((int)Math.round(point.y * factor));
/*      */         }
/*      */         
/*      */ 
/*  869 */         double rad = Math.atan2(point.y, point.x);
/*  870 */         double angle = -(rad * 57.29577951308232D);
/*  871 */         if ((angle > 90.0D) && (angle <= 180.0D)) {
/*  872 */           angle += -360.0D;
/*      */         }
/*      */         
/*      */ 
/*      */ 
/*  877 */         int seriesCount = getSeriesCount();
/*  878 */         double segment_angle = 0.0D;
/*  879 */         double stop = 0.0D;
/*  880 */         double start = 0.0D;
/*  881 */         if (seriesCount == 1) {
/*  882 */           int sampleCount = getSampleCount();
/*  883 */           for (int i = 0; i < sampleCount; i++)
/*      */           {
/*  885 */             start = this.angle_start[0][i];
/*  886 */             stop = this.angle_end[0][i];
/*      */             
/*  888 */             if (((angle <= start) && (angle > stop)) || 
/*  889 */               ((angle - 360.0D <= start) && (angle - 360.0D > stop)) || (
/*  890 */               (angle + 360.0D <= start) && (angle + 360.0D > stop))) {
/*  891 */               sample = getSample(0, i);
/*  892 */               segment_angle = (start + stop) / 2.0D;
/*  893 */               break;
/*      */             }
/*      */           }
/*      */         }
/*      */         else
/*      */         {
/*  899 */           for (int i = 0; i < seriesCount; i++)
/*      */           {
/*  901 */             start = this.angle_start[i][pieIndex];
/*  902 */             stop = this.angle_end[i][pieIndex];
/*      */             
/*  904 */             if (((angle <= start) && (angle > stop)) || 
/*  905 */               ((angle - 360.0D <= start) && (angle - 360.0D > stop)) || (
/*  906 */               (angle + 360.0D <= start) && (angle + 360.0D > stop))) {
/*  907 */               sample = getSample(i, pieIndex);
/*  908 */               segment_angle = (start + stop) / 2.0D;
/*  909 */               break;
/*      */             }
/*      */           }
/*      */         }
/*      */         
/*      */ 
/*  915 */         if ((sample != null) && (sample.isSelected()) && (this.selectionStyle == 2))
/*      */         {
/*      */ 
/*  918 */           double segment_rad = segment_angle * 0.017453292519943295D; Point 
/*  919 */             tmp544_543 = point;tmp544_543.x = ((int)(tmp544_543.x - Math.cos(segment_rad) * this.pieWidth[pieIndex] * 0.5D * this.detachedDistance)); Point 
/*  920 */             tmp577_576 = point;tmp577_576.y = ((int)(tmp577_576.y + Math.sin(segment_rad) * this.pieWidth[pieIndex] * 0.5D * this.detachedDistance));
/*      */           
/*      */ 
/*  923 */           rad = Math.atan2(point.y, point.x);
/*  924 */           angle = -(rad * 57.29577951308232D);
/*  925 */           if ((angle > 90.0D) && (angle <= 180.0D)) {
/*  926 */             angle += -360.0D;
/*      */           }
/*      */           
/*  929 */           if (((angle > start) || (angle <= stop)) && (
/*  930 */             (stop <= start) || ((angle > start) && (angle < stop))))
/*      */           {
/*  932 */             return null;
/*      */           }
/*      */         }
/*      */         
/*      */ 
/*  937 */         double r = Math.sqrt(point.x * point.x + point.y * point.y);
/*  938 */         if (r > this.pieWidth[pieIndex] / 2) {
/*  939 */           return null;
/*      */         }
/*      */       }
/*      */     }
/*      */     
/*      */ 
/*  945 */     if (sample != null) {
/*  946 */       this.mouseOverSampleIndex = sample.getIndex();
/*  947 */       this.mouseOverSeriesIndex = sample.getSeries();
/*  948 */       this.mousePosition = point;
/*      */     } else {
/*  950 */       this.mouseOverSampleIndex = -1;
/*  951 */       this.mouseOverSeriesIndex = -1;
/*      */     }
/*  953 */     return sample;
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
/*  965 */     Point centerPoint = new Point();
/*      */     
/*      */ 
/*  968 */     int seriesCount = getSeriesCount();
/*  969 */     Point center = this.pieCenter[0];
/*  970 */     if (center == null) {
/*  971 */       return null;
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*  976 */     double angle = 0.0D;
/*  977 */     double start = this.angle_start[serie][sample];
/*  978 */     double end = this.angle_end[serie][sample];
/*  979 */     angle = -((start + end) / 2.0D);
/*      */     
/*      */ 
/*  982 */     double factor = 1.0D;
/*  983 */     int width = this.pieWidth[sample];
/*      */     
/*      */ 
/*  986 */     double detach = 0.0D;
/*  987 */     if ((isSelected(serie, sample)) && (start - end < 360.0D) && (this.selectionStyle == 2)) {
/*  988 */       detach = width * this.detachedDistance / 2.0D;
/*      */     }
/*  990 */     if (is3DModeOn()) {
/*  991 */       int change = (int)Math.round(width * (this.pieAngle / 90.0D));
/*  992 */       factor = (width - change) / width;
/*      */     }
/*      */     
/*      */ 
/*  996 */     double rad = angle * 0.017453292519943295D;
/*  997 */     center.x += (int)Math.round(Math.cos(rad) * width * 0.3D + Math.cos(rad) * detach);
/*  998 */     center.y += (int)Math.round((Math.sin(rad) * width * 0.3D + Math.sin(rad) * detach) * factor);
/*      */     
/*      */ 
/*      */ 
/* 1002 */     if (this.graphBounds.contains(centerPoint)) {
/* 1003 */       return centerPoint;
/*      */     }
/* 1005 */     return null;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public Rectangle getGraphBounds()
/*      */   {
/* 1015 */     if ((!this.needGraphBounds) && (this.currentBounds != null)) {
/* 1016 */       return this.currentBounds;
/*      */     }
/*      */     
/*      */ 
/* 1020 */     Rectangle bounds = super.getGraphBounds(getLegendLabels());
/*      */     
/*      */ 
/* 1023 */     Dimension size = getSize();
/* 1024 */     Insets insets = getGraphInsets();
/* 1025 */     if (insets != null) {
/* 1026 */       if (insets.top != -1) {
/* 1027 */         int bottom = bounds.y + bounds.height;
/* 1028 */         bounds.y = insets.top;
/* 1029 */         bounds.height = (bottom - bounds.y);
/*      */       }
/* 1031 */       if (insets.left != -1) {
/* 1032 */         int right = bounds.x + bounds.width;
/* 1033 */         bounds.x = insets.left;
/* 1034 */         bounds.width = (right - bounds.x);
/*      */       }
/* 1036 */       if (insets.bottom != -1) {
/* 1037 */         bounds.height = (size.height - insets.bottom - bounds.y);
/*      */       }
/* 1039 */       if (insets.right != -1) {
/* 1040 */         bounds.width = (size.width - insets.right - bounds.x);
/*      */       }
/*      */     }
/*      */     
/*      */ 
/* 1045 */     switch (getLegendPosition())
/*      */     {
/*      */     case 1: 
/*      */     default: 
/* 1049 */       this.visibleLegend.x = (bounds.x + bounds.width + 14);
/* 1050 */       this.visibleLegend.x = Math.max(this.legend.x, this.visibleLegend.x);
/* 1051 */       this.visibleLegend.y = this.legend.y;
/* 1052 */       this.visibleLegend.width = (size.width - this.visibleLegend.x - 1);
/* 1053 */       this.visibleLegend.height = (size.height - this.legend.y - 12);
/*      */       
/* 1055 */       if (this.legend.width > this.visibleLegend.width) {
/* 1056 */         this.visibleLegend.height = Math.min(this.legend.height, this.visibleLegend.height - 9);
/*      */       }
/* 1058 */       if (this.legend.height > this.visibleLegend.height) {
/* 1059 */         this.visibleLegend.width = Math.min(this.legend.width, this.visibleLegend.width - 9);
/*      */       }
/* 1061 */       break;
/*      */     
/*      */ 
/*      */     case 0: 
/* 1065 */       this.visibleLegend.x = this.legend.x;
/* 1066 */       this.visibleLegend.y = this.legend.y;
/* 1067 */       this.visibleLegend.width = (bounds.x - 20);
/* 1068 */       this.visibleLegend.height = (size.height - this.legend.y - 12);
/*      */       
/* 1070 */       if (this.legend.width > this.visibleLegend.width) {
/* 1071 */         this.visibleLegend.height = Math.min(this.legend.height, this.visibleLegend.height - 9);
/*      */       }
/* 1073 */       if (this.legend.height > this.visibleLegend.height) {
/* 1074 */         this.visibleLegend.width = Math.min(this.legend.width, this.visibleLegend.width - 9);
/* 1075 */         this.visibleLegend.x += 9;
/*      */       }
/* 1077 */       break;
/*      */     
/*      */ 
/*      */     case 2: 
/* 1081 */       this.visibleLegend.x = this.legend.x;
/* 1082 */       this.visibleLegend.y = this.legend.y;
/* 1083 */       this.visibleLegend.width = (size.width - this.legend.x - 9);
/* 1084 */       this.visibleLegend.height = (bounds.y - 13);
/* 1085 */       if ((insets.top != -1) && (isTitleOn())) {
/* 1086 */         this.visibleLegend.height -= getLabelSize(getTitle(), getFontMetrics(getFont("titleFont"))).height;
/*      */       }
/*      */       
/* 1089 */       if (this.legend.height > this.visibleLegend.height) {
/* 1090 */         this.visibleLegend.width = Math.min(this.legend.width, this.visibleLegend.width - 9);
/*      */       }
/* 1092 */       if (this.legend.width > this.visibleLegend.width) {
/* 1093 */         this.visibleLegend.height = Math.min(this.legend.height, this.visibleLegend.height - 9);
/*      */       }
/* 1095 */       break;
/*      */     
/*      */     case 3: 
/* 1098 */       this.legend.y = Math.max(this.legend.y, bounds.y + bounds.height + 6);
/* 1099 */       this.visibleLegend.y = this.legend.y;
/* 1100 */       this.visibleLegend.x = this.legend.x;
/* 1101 */       this.visibleLegend.width = (size.width - this.legend.x - 9);
/* 1102 */       this.visibleLegend.height = (size.height - this.visibleLegend.y - 4);
/*      */       
/* 1104 */       if (this.legend.height > this.visibleLegend.height) {
/* 1105 */         this.visibleLegend.width = Math.min(this.legend.width, this.visibleLegend.width - 9);
/*      */       }
/* 1107 */       if (this.legend.width > this.visibleLegend.width) {
/* 1108 */         this.visibleLegend.height = Math.min(this.legend.height, this.visibleLegend.height - 9);
/*      */       }
/*      */       break;
/*      */     }
/* 1112 */     this.horizontalLegendScrollerOn = (this.legend.width > this.visibleLegend.width);
/* 1113 */     this.verticalLegendScrollerOn = (this.legend.height > this.visibleLegend.height);
/*      */     
/*      */ 
/*      */ 
/* 1117 */     this.needGraphBounds = false;
/* 1118 */     this.currentBounds = bounds;
/* 1119 */     return bounds;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void render(Graphics g)
/*      */   {
/* 1129 */     render(g, !isServletModeOn());
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   void render(Graphics g, boolean offscreenOn)
/*      */   {
/* 1141 */     this.graphBounds = getGraphBounds();
/* 1142 */     Rectangle grid = this.graphBounds;
/* 1143 */     for (int i = 0; i < this.overlayCharts.size(); i++) {
/* 1144 */       Chart overlay = (Chart)this.overlayCharts.elementAt(i);
/* 1145 */       if (overlay != null) {
/* 1146 */         overlay.calculateChartData(grid, grid);
/*      */       }
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/* 1153 */     Dimension size = getSize();
/* 1154 */     if ((offscreenOn) && ((this.offscreen == null) || (this.offscreen.getWidth(this) < size.width) || (this.offscreen.getHeight(this) < size.height))) {
/*      */       try {
/* 1156 */         this.offscreen = createImage(Math.max(1, size.width), Math.max(1, size.height));
/*      */       } catch (Throwable e) {
/* 1158 */         Chart.fifo_clear();
/*      */       }
/*      */       
/*      */ 
/* 1162 */       this.needRender = true;
/*      */     }
/*      */     
/*      */ 
/*      */ 
/* 1167 */     if ((!offscreenOn) || (this.needRender) || (this.offscreen == null))
/*      */     {
/* 1169 */       Graphics gc = g;
/* 1170 */       if ((this.offscreen != null) && (offscreenOn) && (!this.externalGraphicsOn)) {
/* 1171 */         gc = this.offscreen.getGraphics();
/* 1172 */       } else if ((this.externalGraphicsOn) && (this.external_gc != null) && (this.offscreen != null)) {
/* 1173 */         gc = this.external_gc;
/*      */       }
/* 1175 */       gc.setColor(getBackground());
/* 1176 */       gc.fillRect(0, 0, size.width, size.height);
/*      */       
/*      */ 
/* 1179 */       paintTitle(gc, size);
/* 1180 */       renderData(gc, grid, grid);
/*      */       
/* 1182 */       for (int i = 0; i < this.overlayCharts.size(); i++) {
/* 1183 */         Chart overlay = (Chart)this.overlayCharts.elementAt(i);
/* 1184 */         if (overlay != null) {
/* 1185 */           overlay.renderData(gc, grid, grid);
/*      */         }
/*      */       }
/* 1188 */       if (isLegendOn()) {
/* 1189 */         paintLegend(gc, grid, getLegendLabels());
/*      */       }
/*      */       
/* 1192 */       if ((!this.externalGraphicsOn) && (gc != g)) {
/* 1193 */         gc.dispose();
/*      */       }
/* 1195 */       this.needRender = true;
/*      */     }
/*      */     
/*      */ 
/* 1199 */     if (((offscreenOn) || (this.externalGraphicsOn)) && (this.offscreen != null)) {
/* 1200 */       g.drawImage(this.offscreen, 0, 0, this);
/*      */     }
/*      */     
/*      */ 
/*      */ 
/* 1205 */     paintFloatingLabels(g, grid);
/*      */     
/*      */ 
/* 1208 */     paintLabels(g);
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
/* 1219 */     paintPies(g, grid);
/* 1220 */     paintStaticLabels(g);
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
/*      */   protected void calculateChartData(Rectangle grid, Rectangle dataBounds) {}
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   private void paintPies(Graphics g, Rectangle bounds)
/*      */   {
/* 1243 */     int seriesCount = getSeriesCount();
/* 1244 */     if (seriesCount == 1)
/*      */     {
/* 1246 */       double[] values = getSampleValues(0);
/* 1247 */       calculatePieBounds(bounds, 1);
/* 1248 */       paintPie(g, this.pieBounds[0], values, 0);
/*      */     }
/*      */     else {
/* 1251 */       int sampleCount = getSampleCount();
/* 1252 */       calculatePieBounds(bounds, sampleCount);
/* 1253 */       for (int sample = 0; sample < sampleCount; sample++)
/*      */       {
/* 1255 */         double[] values = new double[seriesCount];
/* 1256 */         for (int serie = 0; serie < seriesCount; serie++) {
/* 1257 */           values[serie] = getSampleValue(serie, sample);
/*      */         }
/*      */         
/* 1260 */         paintPie(g, this.pieBounds[sample], values, sample);
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
/*      */   private void calculatePieBounds(Rectangle bounds, int count)
/*      */   {
/* 1274 */     int max = 0;
/* 1275 */     int optimal_x = count;
/* 1276 */     int optimal_y = 1;
/* 1277 */     for (int x = 1; x <= count; x++) {
/* 1278 */       for (int y = 1; y <= count; y++) {
/* 1279 */         if (x * y >= count) {
/* 1280 */           int size = Math.min(bounds.width / x, bounds.height / y);
/* 1281 */           if (size > max) {
/* 1282 */             max = size;
/* 1283 */             optimal_x = x;
/* 1284 */             optimal_y = y;
/*      */           }
/*      */         }
/*      */       }
/*      */     }
/*      */     
/*      */ 
/* 1291 */     int currentPie = 0;
/* 1292 */     for (int y = 0; y < optimal_y; y++) {
/* 1293 */       for (int x = 0; x < optimal_x; x++) {
/* 1294 */         if (currentPie < count) {
/* 1295 */           Rectangle r = this.pieOuterBounds[currentPie];
/* 1296 */           bounds.width /= optimal_x;
/* 1297 */           bounds.height /= optimal_y;
/* 1298 */           bounds.x += r.width * x;
/* 1299 */           bounds.y += r.height * y;
/* 1300 */           if (count > 1) {
/* 1301 */             r.width -= 10;
/* 1302 */             r.height -= 10;
/* 1303 */             r.x += 5;
/* 1304 */             r.y += 5;
/*      */           }
/* 1306 */           this.pieBounds[currentPie].setBounds(this.pieOuterBounds[currentPie]);
/* 1307 */           currentPie++;
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
/*      */   private void paintPie(Graphics g, Rectangle bounds, double[] values, int pieIndex)
/*      */   {
/* 1323 */     if ((bounds == null) || (bounds.width <= 0) || (bounds.height <= 0)) {
/* 1324 */       return;
/*      */     }
/*      */     
/*      */ 
/* 1328 */     boolean outsideOn = ((this.percentLabelsOn) && (this.percentLabelStyle == 1)) || 
/* 1329 */       ((this.sampleLabelsOn) && (this.sampleLabelStyle == 1)) || 
/* 1330 */       ((this.seriesLabelsOn) && (this.seriesLabelStyle == 1)) || (
/* 1331 */       (isValueLabelsOn()) && (this.valueLabelStyle == 1));
/*      */     
/* 1333 */     boolean pointingOn = ((this.percentLabelsOn) && (this.percentLabelStyle == 5)) || 
/* 1334 */       ((this.sampleLabelsOn) && (this.sampleLabelStyle == 5)) || 
/* 1335 */       ((this.seriesLabelsOn) && (this.seriesLabelStyle == 5)) || (
/* 1336 */       (isValueLabelsOn()) && (this.valueLabelStyle == 5));
/*      */     
/*      */ 
/* 1339 */     int seriesCount = getSeriesCount();
/* 1340 */     int sampleCount = getSampleCount();
/* 1341 */     int max_label_width = 0;
/* 1342 */     int max_label_height = 0;
/* 1343 */     if ((outsideOn) || (pointingOn)) {
/* 1344 */       Font font = getFont("outsideLabelFont");
/* 1345 */       if (pointingOn) {
/* 1346 */         font = getFont("pointingLabelFont");
/*      */       }
/* 1348 */       FontMetrics fm = getFontMetrics(font);
/* 1349 */       for (int serie = 0; serie < seriesCount; serie++) {
/* 1350 */         for (int sample = 0; sample < sampleCount; sample++) {
/* 1351 */           ChartSample s = null;
/* 1352 */           if (seriesCount == 1) {
/* 1353 */             s = getSample(0, sample);
/*      */           } else {
/* 1355 */             s = getSample(serie, sample);
/*      */           }
/* 1357 */           if ((s != null) && (s.value != null) && (!s.value.isNaN())) {
/* 1358 */             String label = constructLabel(serie, sample, 1, true, null);
/* 1359 */             if (pointingOn) {
/* 1360 */               label = constructLabel(serie, sample, 5, true, null);
/*      */             }
/* 1362 */             Dimension labelSize = getLabelSize(label, fm);
/* 1363 */             max_label_width = Math.max(max_label_width, labelSize.width);
/* 1364 */             max_label_height = Math.max(max_label_height, labelSize.height);
/*      */           }
/*      */         }
/*      */       }
/* 1368 */       bounds.x += max_label_width;
/* 1369 */       bounds.width -= max_label_width * 2;
/* 1370 */       if ((outsideOn) && (!pointingOn)) {
/* 1371 */         bounds.y += max_label_height;
/* 1372 */         bounds.height -= max_label_height * 2;
/*      */       }
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/* 1379 */     int width = Math.min(bounds.width, bounds.height);
/* 1380 */     int height = width;
/* 1381 */     int height3d = height;
/* 1382 */     int center_x = bounds.x + bounds.width / 2;
/* 1383 */     int center_y = bounds.y + bounds.height / 2;
/*      */     
/*      */ 
/* 1386 */     Font pieLabelFont = getFont("pieLabelFont");
/* 1387 */     if (pieLabelFont == null) {
/* 1388 */       pieLabelFont = getFont();
/*      */     }
/* 1390 */     FontMetrics fm = getFontMetrics(pieLabelFont);
/* 1391 */     g.setFont(pieLabelFont);
/*      */     
/*      */ 
/*      */ 
/* 1395 */     boolean display3dOn = is3DModeOn();
/* 1396 */     if (display3dOn)
/*      */     {
/*      */ 
/*      */ 
/* 1400 */       int change = (int)Math.round(width * (this.pieAngle / 90.0D));
/* 1401 */       height -= change;
/* 1402 */       int effect3d = (int)Math.round(width * this.pieDepth * (this.pieAngle / 90.0D));
/* 1403 */       height3d = height + effect3d;
/* 1404 */       center_y -= effect3d / 2;
/*      */       
/*      */ 
/*      */ 
/* 1408 */       double factor_width = bounds.width / width;
/* 1409 */       int new_width = (int)Math.round(width * factor_width);
/* 1410 */       int new_height = (int)Math.round(height * factor_width);
/* 1411 */       effect3d = (int)Math.round(new_width * this.pieDepth * (this.pieAngle / 90.0D));
/* 1412 */       height3d = new_height + effect3d;
/* 1413 */       center_y = bounds.y + bounds.height / 2 - effect3d / 2;
/*      */       
/*      */ 
/*      */ 
/*      */ 
/* 1418 */       if (center_y - new_height / 2 < bounds.y) {
/* 1419 */         double factor_height = new_height / height3d;
/*      */         
/* 1421 */         height3d = bounds.height;
/* 1422 */         new_height = (int)Math.round(height3d * factor_height);
/* 1423 */         effect3d = bounds.height - new_height;
/* 1424 */         factor_width = width / height;
/* 1425 */         new_width = (int)Math.round(new_height * factor_width);
/* 1426 */         center_y = bounds.y + bounds.height / 2 - effect3d / 2;
/*      */       }
/*      */       
/*      */ 
/* 1430 */       width = new_width;
/* 1431 */       height = new_height;
/*      */     }
/*      */     
/*      */ 
/* 1435 */     this.size_factor = (height / width);
/* 1436 */     if ((outsideOn) || (pointingOn)) {
/* 1437 */       if (display3dOn) {
/* 1438 */         if (!pointingOn) {
/* 1439 */           int effect3d = (int)Math.round(width * this.pieDepth * (this.pieAngle / 90.0D));
/* 1440 */           int old_height = height;
/* 1441 */           width -= (int)(width / 11.0D);
/* 1442 */           height = (int)(width * this.size_factor);
/* 1443 */           effect3d = (int)Math.round(width * this.pieDepth * (this.pieAngle / 90.0D));
/* 1444 */           if (height + effect3d + width / 10 > bounds.height) {
/* 1445 */             height = bounds.height - effect3d - width / 11;
/* 1446 */             width = (int)(height / this.size_factor);
/* 1447 */             effect3d = (int)Math.round(width * this.pieDepth * (this.pieAngle / 90.0D));
/*      */           }
/* 1449 */           center_y = this.pieBounds[pieIndex].y + this.pieBounds[pieIndex].height / 2 - effect3d / 2;
/*      */         } else {
/* 1451 */           int cur_width = (int)(width * 1.1D) + max_label_width * 2;
/* 1452 */           if (this.pieOuterBounds[pieIndex].width < cur_width) {
/* 1453 */             width -= Math.min(width / 11, cur_width - this.pieOuterBounds[pieIndex].width);
/* 1454 */             height = (int)(width * this.size_factor);
/*      */           }
/*      */         }
/*      */       } else {
/* 1458 */         if (!pointingOn) {
/* 1459 */           width -= width / 11;
/*      */         } else {
/* 1461 */           int cur_width = Math.min(width, height) + max_label_width * 2 + 70;
/* 1462 */           if (this.pieOuterBounds[pieIndex].width < cur_width) {
/* 1463 */             width -= Math.min(70, cur_width - this.pieOuterBounds[pieIndex].width);
/*      */           }
/*      */         }
/* 1466 */         height = width;
/*      */       }
/* 1468 */       this.pointingDistance = ((this.pieOuterBounds[pieIndex].width - width) / 2 - max_label_width);
/*      */     }
/*      */     
/*      */ 
/* 1472 */     if (this.pieLabelsOn) {
/* 1473 */       Font font = getFont("pieLabelFont");
/* 1474 */       String label = getSampleLabel(pieIndex);
/* 1475 */       Dimension labelSize = getLabelSize(label, getFontMetrics(font));
/* 1476 */       if (display3dOn) {
/* 1477 */         int effect3d = (int)Math.round(width * this.pieDepth * (this.pieAngle / 90.0D));
/* 1478 */         int ypos = center_y + height / 2 + labelSize.height - fm.getDescent() + 2;
/* 1479 */         ypos += effect3d;
/* 1480 */         if (ypos > bounds.y + bounds.height)
/*      */         {
/* 1482 */           int needed = ypos - (bounds.y + bounds.height);
/* 1483 */           height -= needed;
/* 1484 */           center_y -= needed / 2;
/*      */         }
/*      */       }
/*      */       else
/*      */       {
/* 1489 */         height -= labelSize.height;
/* 1490 */         width = height;
/* 1491 */         center_y -= labelSize.height / 2;
/*      */       }
/*      */     }
/*      */     
/*      */ 
/* 1496 */     this.pieCenter[pieIndex].x = center_x;
/* 1497 */     this.pieCenter[pieIndex].y = center_y;
/* 1498 */     this.pieWidth[pieIndex] = width;
/*      */     
/*      */ 
/* 1501 */     double sum = 0.0D;
/* 1502 */     for (int i = 0; i < values.length; i++) {
/* 1503 */       if (values[i] > 0.0D) {
/* 1504 */         sum += values[i];
/*      */       }
/*      */     }
/*      */     
/*      */ 
/* 1509 */     double startAngle = 90.0D;
/* 1510 */     seriesCount = getSeriesCount();
/* 1511 */     for (int i = 0; i < values.length; i++)
/*      */     {
/* 1513 */       if (seriesCount == 1) {
/* 1514 */         this.angle_start[0][i] = (startAngle + this.rotateAngles[0]);
/*      */       } else {
/* 1516 */         this.angle_start[i][pieIndex] = (startAngle + this.rotateAngles[pieIndex]);
/*      */       }
/*      */       
/* 1519 */       double angle = values[i] / sum * 360.0D;
/* 1520 */       if (!Double.isNaN(angle)) {
/* 1521 */         startAngle -= angle;
/*      */       }
/* 1523 */       if (seriesCount == 1) {
/* 1524 */         this.angle_end[0][i] = (startAngle + this.rotateAngles[0]);
/*      */       } else {
/* 1526 */         this.angle_end[i][pieIndex] = (startAngle + this.rotateAngles[pieIndex]);
/*      */       }
/*      */     }
/*      */     
/*      */ 
/* 1531 */     if (display3dOn)
/*      */     {
/*      */ 
/*      */ 
/* 1535 */       int slice = 0;
/* 1536 */       double start = -270.0D;
/* 1537 */       double end = start;
/* 1538 */       if (seriesCount == 1) {
/* 1539 */         for (int i = values.length - 1; i >= 0; i--) {
/* 1540 */           slice = (this.angle_start[0][i] - 90.0D) % 360.0D < (this.angle_start[0][slice] - 90.0D) % 360.0D ? i : slice;
/*      */         }
/* 1542 */         start = this.angle_start[0][slice];
/* 1543 */         end = this.angle_end[0][slice];
/*      */       } else {
/* 1545 */         for (int i = values.length - 1; i >= 0; i--) {
/* 1546 */           slice = (this.angle_start[i][pieIndex] - 90.0D) % 360.0D < (this.angle_start[slice][pieIndex] - 90.0D) % 360.0D ? i : slice;
/*      */         }
/* 1548 */         start = this.angle_start[slice][pieIndex];
/* 1549 */         end = this.angle_end[slice][pieIndex];
/*      */       }
/* 1551 */       int bottom_slice = -1;
/* 1552 */       double bottom_start = 0.0D;
/* 1553 */       double bottom_end = 0.0D;
/* 1554 */       while ((start - 90.0D) % 360.0D <= -180.0D) {
/* 1555 */         ChartSample s = null;
/* 1556 */         if (seriesCount == 1) {
/* 1557 */           s = getSample(0, slice);
/*      */         } else {
/* 1559 */           s = getSample(slice, pieIndex);
/*      */         }
/*      */         
/* 1562 */         boolean selected = false;
/* 1563 */         if (seriesCount == 1) {
/* 1564 */           selected = isSelected(0, slice);
/*      */         } else {
/* 1566 */           selected = isSelected(slice, pieIndex);
/*      */         }
/*      */         
/*      */ 
/* 1570 */         double detached = 0.0D;
/* 1571 */         if ((selected) && (this.selectionStyle == 2)) {
/* 1572 */           detached = this.detachedDistance;
/* 1573 */         } else if (seriesCount == 1) {
/* 1574 */           detached = getDetachedSlice(0, slice);
/*      */         } else {
/* 1576 */           detached = getDetachedSlice(slice, pieIndex);
/*      */         }
/*      */         
/*      */ 
/* 1580 */         if (s != null) {
/* 1581 */           paintPieSegment(g, getSampleColor(slice), start, start - end, width, height, center_x, center_y, selected, detached, 1);
/*      */         }
/*      */         
/* 1584 */         if (seriesCount == 1) {
/* 1585 */           slice = slice != 0 ? slice - 1 : this.angle_start[0].length - 1;
/* 1586 */           start = this.angle_start[0][slice];
/* 1587 */           end = this.angle_end[0][slice];
/*      */         } else {
/* 1589 */           slice = slice != 0 ? slice - 1 : this.angle_start.length - 1;
/* 1590 */           start = this.angle_start[slice][pieIndex];
/* 1591 */           end = this.angle_end[slice][pieIndex];
/*      */         }
/*      */         
/*      */ 
/*      */ 
/* 1596 */         if ((start < -90.0D) && (start > -270.0D) && (end < -450.0D)) {
/* 1597 */           bottom_start = start;
/* 1598 */           bottom_end = end;
/* 1599 */           bottom_slice = slice;
/* 1600 */           break;
/*      */         }
/*      */       }
/*      */       
/*      */ 
/*      */ 
/* 1606 */       if (seriesCount == 1) {
/* 1607 */         for (int i = 0; i < values.length; i++) {
/* 1608 */           slice = (this.angle_start[0][i] - 90.0D) % 360.0D > (this.angle_start[0][slice] - 90.0D) % 360.0D ? i : slice;
/*      */         }
/* 1610 */         start = this.angle_start[0][slice];
/* 1611 */         end = this.angle_end[0][slice];
/*      */       } else {
/* 1613 */         for (int i = 0; i < values.length; i++) {
/* 1614 */           slice = (this.angle_start[i][pieIndex] - 90.0D) % 360.0D > (this.angle_start[slice][pieIndex] - 90.0D) % 360.0D ? i : slice;
/*      */         }
/* 1616 */         start = this.angle_start[slice][pieIndex];
/* 1617 */         end = this.angle_end[slice][pieIndex];
/*      */       }
/* 1619 */       int slices_done = 0;
/* 1620 */       while ((start - 90.0D) % 360.0D > -180.0D)
/*      */       {
/* 1622 */         if (((seriesCount == 1) && (slices_done >= this.angle_start[0].length)) || ((seriesCount > 1) && (slices_done >= this.angle_start.length))) {
/*      */           break;
/*      */         }
/* 1625 */         ChartSample s = null;
/* 1626 */         if (seriesCount == 1) {
/* 1627 */           s = getSample(0, slice);
/*      */         } else {
/* 1629 */           s = getSample(slice, pieIndex);
/*      */         }
/*      */         
/*      */ 
/*      */ 
/* 1634 */         boolean selected = false;
/* 1635 */         if (seriesCount == 1) {
/* 1636 */           selected = isSelected(0, slice);
/*      */         } else {
/* 1638 */           selected = isSelected(slice, pieIndex);
/*      */         }
/*      */         
/*      */ 
/* 1642 */         double detached = 0.0D;
/* 1643 */         if ((selected) && (this.selectionStyle == 2)) {
/* 1644 */           detached = this.detachedDistance;
/* 1645 */         } else if (seriesCount == 1) {
/* 1646 */           detached = getDetachedSlice(0, slice);
/*      */         } else {
/* 1648 */           detached = getDetachedSlice(slice, pieIndex);
/*      */         }
/*      */         
/*      */ 
/* 1652 */         if (s != null) {
/* 1653 */           paintPieSegment(g, getSampleColor(slice), start, start - end, width, height, center_x, center_y, selected, detached, 1);
/*      */         }
/* 1655 */         if (((start > -90.0D) && (end <= -90.0D)) || ((start < -450.0D) && (end >= -450.0D))) {
/*      */           break;
/*      */         }
/*      */         
/* 1659 */         if (seriesCount == 1) {
/* 1660 */           slice = slice < this.angle_start[0].length - 1 ? slice + 1 : 0;
/* 1661 */           start = this.angle_start[0][slice];
/* 1662 */           end = this.angle_end[0][slice];
/*      */         } else {
/* 1664 */           slice = slice < this.angle_start.length - 1 ? slice + 1 : 0;
/* 1665 */           start = this.angle_start[slice][pieIndex];
/* 1666 */           end = this.angle_end[slice][pieIndex];
/*      */         }
/* 1668 */         slices_done++;
/*      */       }
/*      */       
/*      */ 
/* 1672 */       if (bottom_slice != -1) {
/* 1673 */         ChartSample s = null;
/* 1674 */         if (seriesCount == 1) {
/* 1675 */           s = getSample(0, bottom_slice);
/*      */         } else {
/* 1677 */           s = getSample(bottom_slice, pieIndex);
/*      */         }
/*      */         
/* 1680 */         boolean selected = false;
/* 1681 */         if (seriesCount == 1) {
/* 1682 */           selected = isSelected(0, bottom_slice);
/*      */         } else {
/* 1684 */           selected = isSelected(bottom_slice, pieIndex);
/*      */         }
/*      */         
/* 1687 */         double detached = 0.0D;
/* 1688 */         if ((selected) && (this.selectionStyle == 2)) {
/* 1689 */           detached = this.detachedDistance;
/* 1690 */         } else if (seriesCount == 1) {
/* 1691 */           detached = getDetachedSlice(0, bottom_slice);
/*      */         } else {
/* 1693 */           detached = getDetachedSlice(bottom_slice, pieIndex);
/*      */         }
/* 1695 */         if (s != null) {
/* 1696 */           paintPieSegment(g, getSampleColor(bottom_slice), bottom_start, bottom_start - bottom_end, width, height, center_x, center_y, selected, detached, 1);
/*      */         }
/*      */       }
/*      */     }
/*      */     
/*      */ 
/* 1702 */     for (int slice = 0; slice < values.length; slice++)
/*      */     {
/* 1704 */       double start = 90.0D;
/* 1705 */       double end = -270.0D;
/* 1706 */       if (seriesCount == 1) {
/* 1707 */         start = this.angle_start[0][slice];
/* 1708 */         end = this.angle_end[0][slice];
/*      */       } else {
/* 1710 */         start = this.angle_start[slice][pieIndex];
/* 1711 */         end = this.angle_end[slice][pieIndex];
/*      */       }
/*      */       
/* 1714 */       boolean selected = false;
/* 1715 */       if (seriesCount == 1) {
/* 1716 */         selected = isSelected(0, slice);
/*      */       } else {
/* 1718 */         selected = isSelected(slice, pieIndex);
/*      */       }
/*      */       
/*      */ 
/* 1722 */       double detached = 0.0D;
/* 1723 */       if ((selected) && (this.selectionStyle == 2)) {
/* 1724 */         detached = this.detachedDistance;
/* 1725 */       } else if (seriesCount == 1) {
/* 1726 */         detached = getDetachedSlice(0, slice);
/*      */       } else {
/* 1728 */         detached = getDetachedSlice(slice, pieIndex);
/*      */       }
/*      */       
/*      */ 
/* 1732 */       ChartSample s = null;
/* 1733 */       if (seriesCount == 1) {
/* 1734 */         s = getSample(0, slice);
/*      */       } else {
/* 1736 */         s = getSample(slice, pieIndex);
/*      */       }
/* 1738 */       if (s != null) {
/* 1739 */         paintPieSegment(g, getSampleColor(slice), start, start - end, width, height, center_x, center_y, selected, detached, 2);
/*      */       }
/*      */     }
/*      */     
/*      */ 
/* 1744 */     if (this.sliceSeperatorOn) {
/* 1745 */       for (int slice = 0; slice < values.length; slice++)
/*      */       {
/*      */ 
/* 1748 */         double start = 0.0D;
/* 1749 */         double end = -90.0D;
/* 1750 */         ChartSample s = null;
/* 1751 */         boolean selected = false;
/* 1752 */         if (seriesCount == 1) {
/* 1753 */           start = this.angle_start[0][slice];
/* 1754 */           end = this.angle_end[0][slice];
/* 1755 */           selected = isSelected(0, slice);
/* 1756 */           s = getSample(0, slice);
/*      */         } else {
/* 1758 */           start = this.angle_start[slice][pieIndex];
/* 1759 */           end = this.angle_end[slice][pieIndex];
/* 1760 */           selected = isSelected(slice, pieIndex);
/* 1761 */           s = getSample(slice, pieIndex);
/*      */         }
/*      */         
/*      */ 
/* 1765 */         double detached = 0.0D;
/* 1766 */         if ((selected) && (this.selectionStyle == 2)) {
/* 1767 */           detached = this.detachedDistance;
/* 1768 */         } else if (seriesCount == 1) {
/* 1769 */           detached = getDetachedSlice(0, slice);
/*      */         } else {
/* 1771 */           detached = getDetachedSlice(slice, pieIndex);
/*      */         }
/*      */         
/* 1774 */         if ((s != null) && (s.value != null) && (!s.value.isNaN()))
/*      */         {
/* 1776 */           if (detached == 0.0D) {
/* 1777 */             double start_rad = -Math.round(start) * 0.017453292519943295D;
/* 1778 */             int start_x = center_x + (int)Math.round(Math.cos(start_rad) * (width * 0.5D));
/* 1779 */             int start_y = center_y + (int)Math.round(Math.sin(start_rad) * (height * 0.5D));
/* 1780 */             double stop_rad = -Math.round(end) * 0.017453292519943295D;
/* 1781 */             int stop_x = center_x + (int)Math.round(Math.cos(stop_rad) * (width * 0.5D));
/* 1782 */             int stop_y = center_y + (int)Math.round(Math.sin(stop_rad) * (height * 0.5D));
/*      */             
/*      */ 
/* 1785 */             if ((start > -90.0D) && (start < 90.0D)) {
/* 1786 */               g.setColor(getSampleColor(slice));
/* 1787 */               g.drawLine(center_x, center_y + 1, start_x, start_y + 1);
/*      */             }
/* 1789 */             else if ((-start > 90.0D) && (-start < 270.0D)) {
/* 1790 */               g.setColor(getSampleColor(slice));
/* 1791 */               g.drawLine(center_x, center_y - 1, start_x, start_y - 1);
/*      */             }
/* 1793 */             if ((-end > 90.0D) && (-end < 270.0D)) {
/* 1794 */               g.setColor(getSampleColor(slice));
/* 1795 */               g.drawLine(center_x, center_y + 1, stop_x, stop_y + 1);
/* 1796 */             } else if ((end > -90.0D) && (end < 90.0D)) {
/* 1797 */               g.setColor(getSampleColor(slice));
/* 1798 */               g.drawLine(center_x, center_y - 1, stop_x, stop_y - 1);
/* 1799 */               if (stop_y < center_y) {
/* 1800 */                 g.drawLine(center_x, center_y - 1, stop_x - 1, stop_y - 1);
/*      */               }
/*      */             }
/*      */           }
/*      */         }
/*      */       }
/*      */       
/* 1807 */       boolean prev_selected = false;
/* 1808 */       for (int slice = 0; slice < values.length; slice++)
/*      */       {
/*      */ 
/*      */ 
/* 1812 */         double start = 90.0D;
/* 1813 */         double end = -270.0D;
/* 1814 */         ChartSample s = null;
/* 1815 */         boolean selected = false;
/* 1816 */         if (seriesCount == 1) {
/* 1817 */           start = this.angle_start[0][slice];
/* 1818 */           end = this.angle_end[0][slice];
/* 1819 */           selected = isSelected(0, slice);
/* 1820 */           s = getSample(0, slice);
/*      */         } else {
/* 1822 */           start = this.angle_start[slice][pieIndex];
/* 1823 */           end = this.angle_end[slice][pieIndex];
/* 1824 */           selected = isSelected(slice, pieIndex);
/* 1825 */           s = getSample(slice, pieIndex);
/*      */         }
/*      */         
/*      */ 
/* 1829 */         double detached = 0.0D;
/* 1830 */         if ((selected) && (this.selectionStyle == 2)) {
/* 1831 */           detached = this.detachedDistance;
/* 1832 */         } else if (seriesCount == 1) {
/* 1833 */           detached = getDetachedSlice(0, slice);
/*      */         } else {
/* 1835 */           detached = getDetachedSlice(slice, pieIndex);
/*      */         }
/*      */         
/*      */ 
/* 1839 */         if (detached == 0.0D) {
/* 1840 */           if ((s != null) && (s.value != null) && (!s.value.isNaN())) {
/* 1841 */             if (this.sliceSeperatorColor != null) {
/* 1842 */               g.setColor(this.sliceSeperatorColor);
/*      */             } else {
/* 1844 */               g.setColor(getSampleColor(slice).darker());
/*      */             }
/*      */             
/* 1847 */             double start_rad = -Math.round(start) * 0.017453292519943295D;
/* 1848 */             int start_x = center_x + (int)Math.round(Math.cos(start_rad) * (width * 0.5D));
/* 1849 */             int start_y = center_y + (int)Math.round(Math.sin(start_rad) * (height * 0.5D));
/* 1850 */             double stop_rad = -Math.round(end) * 0.017453292519943295D;
/* 1851 */             int stop_x = center_x + (int)Math.round(Math.cos(stop_rad) * (width * 0.5D));
/* 1852 */             int stop_y = center_y + (int)Math.round(Math.sin(stop_rad) * (height * 0.5D));
/* 1853 */             if (prev_selected) {
/* 1854 */               g.drawLine(center_x, center_y, start_x, start_y);
/*      */             }
/* 1856 */             g.drawLine(center_x, center_y, stop_x, stop_y);
/*      */           }
/* 1858 */           prev_selected = selected;
/*      */         }
/*      */       }
/*      */     }
/*      */     
/*      */ 
/* 1864 */     if (this.pieLabelsOn)
/*      */     {
/* 1866 */       String label = seriesCount == 1 ? getSeriesLabel(pieIndex) : getSampleLabel(pieIndex);
/* 1867 */       Font font = getFont("pieLabelFont");
/*      */       
/* 1869 */       if ((label != null) && (label.trim().length() > 0)) {
/* 1870 */         Dimension labelSize = getLabelSize(label, getFontMetrics(font));
/* 1871 */         int xpos = center_x - labelSize.width / 2;
/* 1872 */         int ypos = center_y + height / 2 + fm.getHeight() - fm.getDescent() + 3;
/* 1873 */         if (display3dOn) {
/* 1874 */           ypos += (int)Math.round(width * this.pieDepth * (this.pieAngle / 90.0D));
/*      */         }
/*      */         
/* 1877 */         Color c = getSampleLabelColor(pieIndex);
/* 1878 */         g.setColor(c != null ? c : getForeground());
/* 1879 */         paintLabel(g, label, xpos, ypos, labelSize, 0, 0, false);
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
/*      */ 
/*      */   private void paintPieSegment(Graphics g, Color color, double startAngle, double angle, int width, int height, int center_x, int center_y, boolean selected, double distance, int part)
/*      */   {
/* 1901 */     if ((distance > 0.0D) && (angle < 360.0D))
/*      */     {
/* 1903 */       double mid = -startAngle + angle / 2.0D;
/* 1904 */       double rad = mid * 0.017453292519943295D;
/* 1905 */       center_x += (int)Math.round(Math.cos(rad) * (width * (distance / 2.0D)));
/* 1906 */       center_y += (int)Math.round(Math.sin(rad) * (height * (distance / 2.0D)));
/*      */     }
/*      */     
/* 1909 */     double startrad = -Math.round(startAngle) * 0.017453292519943295D;
/* 1910 */     double stoprad = -Math.round(startAngle - angle) * 0.017453292519943295D;
/*      */     
/*      */ 
/* 1913 */     double pie_fraction = Math.abs(startrad - stoprad) / 6.283185307179586D;
/* 1914 */     double circum = 3.141592653589793D * Math.max(width, height);
/* 1915 */     double line_count = pie_fraction * circum;
/* 1916 */     double delta = Math.abs(startrad - stoprad) / line_count;
/*      */     
/*      */ 
/* 1919 */     if (part == 1)
/*      */     {
/* 1921 */       int d = (int)Math.round(width * this.pieDepth * (this.pieAngle / 90.0D));
/* 1922 */       int w = width / 2 + 1;
/* 1923 */       int h = height / 2 + 1;
/*      */       
/*      */ 
/* 1926 */       if (delta > 0.0D) {
/* 1927 */         double rad = startrad;
/* 1928 */         g.setColor(color.darker().darker());
/* 1929 */         while (rad < stoprad) {
/* 1930 */           int x = center_x + (int)Math.round(Math.cos(rad) * w);
/* 1931 */           int y = center_y + (int)Math.round(Math.sin(rad) * h);
/* 1932 */           if (y >= center_y) {
/* 1933 */             g.drawLine(x, y, x, y + d - 1);
/*      */           }
/* 1935 */           rad += delta;
/*      */         }
/*      */       }
/*      */       
/* 1939 */       int start_x = center_x + (int)Math.round(Math.cos(startrad) * w);
/* 1940 */       int start_y = center_y + (int)Math.round(Math.sin(startrad) * h);
/* 1941 */       int stop_x = center_x + (int)Math.round(Math.cos(stoprad) * w);
/* 1942 */       int stop_y = center_y + (int)Math.round(Math.sin(stoprad) * h);
/* 1943 */       if (start_x < center_x) {
/* 1944 */         if (start_y > center_y) {
/* 1945 */           g.fillPolygon(new int[] { center_x, center_x, start_x, start_x }, 
/* 1946 */             new int[] { center_y + d, center_y - 2, start_y - 2, start_y + d }, 4);
/*      */         }
/* 1948 */         g.fillPolygon(new int[] { center_x, center_x, start_x, start_x }, 
/* 1949 */           new int[] { center_y + d, center_y - 1, start_y - 1, start_y + d }, 4);
/*      */       }
/* 1951 */       if (stop_x > center_x) {
/* 1952 */         g.fillPolygon(new int[] { center_x, center_x, stop_x + 1, stop_x + 1 }, 
/* 1953 */           new int[] { center_y + d, center_y - 1, stop_y - 1, stop_y + d }, 4);
/* 1954 */         g.fillPolygon(new int[] { center_x, center_x, stop_x, stop_x }, 
/* 1955 */           new int[] { center_y + d, center_y - 1, stop_y - 1, stop_y + d }, 4);
/* 1956 */         g.fillPolygon(new int[] { center_x, center_x, stop_x - 1, stop_x - 1 }, 
/* 1957 */           new int[] { center_y + d, center_y - 1, stop_y - 1, stop_y + d }, 4);
/*      */       }
/*      */       
/* 1960 */       int xpos = center_x - width / 2;
/* 1961 */       int ypos = center_y - height / 2;
/* 1962 */       int start = (int)Math.round(startAngle);
/* 1963 */       int stop = (int)Math.round(startAngle - angle) - (int)Math.round(startAngle);
/* 1964 */       g.fillArc(xpos - 1, ypos - 1 + d, width + 2, height + 3, start, stop);
/*      */     }
/*      */     
/*      */ 
/*      */ 
/* 1969 */     if (part == 2) {
/* 1970 */       int xpos = center_x - width / 2;
/* 1971 */       int ypos = center_y - height / 2;
/* 1972 */       int start = (int)Math.round(startAngle);
/* 1973 */       int stop = (int)Math.round(startAngle - angle) - (int)Math.round(startAngle);
/* 1974 */       if (!is3DModeOn()) {
/* 1975 */         g.setColor(color);
/* 1976 */         g.fillArc(xpos - 1, ypos - 1, width + 3, height + 3, start, stop);
/* 1977 */         g.setColor(color.darker());
/* 1978 */         g.drawArc(xpos - 1, ypos - 1, width + 2, height + 2, start, stop);
/*      */       } else {
/* 1980 */         g.setColor(color);
/* 1981 */         g.fillArc(xpos - 1, ypos - 1, width + 2, height + 2, start, stop);
/* 1982 */         g.drawArc(xpos, ypos - 1, width, height + 2, start, stop);
/*      */         
/* 1984 */         g.setColor(color.darker().darker());
/* 1985 */         g.drawArc(xpos - 1, ypos - 1, width + 2, height + 3, start, stop);
/*      */         
/* 1987 */         g.setColor(color);
/* 1988 */         g.drawArc(xpos - 1, ypos, width + 2, height, start, stop);
/*      */         
/* 1990 */         g.setColor(color.darker());
/* 1991 */         g.drawArc(xpos - 1, ypos - 1, width + 2, height + 2, start, stop);
/*      */       }
/*      */     }
/*      */     
/*      */ 
/* 1996 */     if (selected) {
/* 1997 */       paintSelectionMarker(g, color, (int)Math.round(-startAngle), (int)Math.round(-angle), width, height, center_x, center_y, this.selectionStyle);
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
/*      */   private void paintSelectionMarker(Graphics g, Color color, double startAngle, double angle, int width, int height, int center_x, int center_y, int style)
/*      */   {
/* 2016 */     if (style == 1)
/*      */     {
/*      */ 
/* 2019 */       double mid = startAngle - angle / 2.0D;
/* 2020 */       double rad = mid * 0.017453292519943295D;
/* 2021 */       int x = center_x + (int)Math.round(Math.cos(rad) * (width * 0.4D));
/* 2022 */       int y = center_y + (int)Math.round(Math.sin(rad) * (height * 0.4D));
/* 2023 */       int m_width = Math.max(width / 16, 3);
/* 2024 */       int m_height = Math.max(height / 16, 3);
/* 2025 */       int off_x = m_width / 2;
/* 2026 */       int off_y = m_height / 2;
/*      */       
/* 2028 */       g.setColor(color.darker());
/* 2029 */       g.fillOval(x - off_x, y - off_y, m_width, m_height);
/*      */       
/* 2031 */       g.setColor(color.darker());
/* 2032 */       g.drawArc(x - off_x, y - off_y, m_width, m_height, 45, 180);
/* 2033 */       g.setColor(color.darker().darker());
/* 2034 */       g.drawArc(x - off_x + 1, y - off_y + 1, m_width - 2, m_height - 2, 45, 180);
/*      */       
/* 2036 */       g.setColor(color);
/* 2037 */       g.drawArc(x - off_x + 1, y - off_y + 1, m_width - 2, m_height - 2, 45, 65356);
/* 2038 */       g.setColor(color.brighter());
/* 2039 */       g.drawArc(x - off_x, y - off_y, m_width, m_height, 45, 65356);
/*      */ 
/*      */ 
/*      */     }
/* 2043 */     else if (style == 0)
/*      */     {
/* 2045 */       double mid = startAngle - angle / 2.0D;
/* 2046 */       double rad = mid * 0.017453292519943295D;
/* 2047 */       int w = (int)Math.round(width * 0.7D);
/* 2048 */       int h = (int)Math.round(height * 0.7D);
/* 2049 */       int xpos = center_x + (int)Math.round(Math.cos(rad) * (width * 0.1D));
/* 2050 */       int ypos = center_y + (int)Math.round(Math.sin(rad) * (height * 0.1D));
/* 2051 */       xpos -= (int)Math.round(w / 2.0D);
/* 2052 */       ypos -= (int)Math.round(h / 2.0D);
/*      */       
/* 2054 */       g.setColor(color.darker());
/* 2055 */       g.fillArc(xpos, ypos, w + 1, h + 1, (int)-Math.round(startAngle), (int)Math.round(angle));
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
/*      */   void paintFloatingLabel(Graphics g, String label, Rectangle grid, int sample, int series, Font font, FontMetrics fm)
/*      */   {
/* 2072 */     int seriesCount = getSeriesCount();
/* 2073 */     Point center = this.pieCenter[0];
/*      */     
/* 2075 */     if ((series < 0) || (series >= getSeriesCount()) || (sample < 0) || (sample >= getSampleCount()) || (fm == null) || (center == null)) {
/* 2076 */       return;
/*      */     }
/*      */     
/*      */ 
/*      */ 
/* 2081 */     double start = this.angle_start[series][sample];
/* 2082 */     double end = this.angle_end[series][sample];
/* 2083 */     double angle = -((start + end) / 2.0D);
/*      */     
/*      */ 
/*      */ 
/* 2087 */     double factor = 1.0D;
/* 2088 */     int width = this.pieWidth[sample];
/* 2089 */     if (is3DModeOn()) {
/* 2090 */       int change = (int)Math.round(width * (this.pieAngle / 90.0D));
/* 2091 */       factor = (width - change) / width;
/*      */     }
/*      */     
/*      */ 
/* 2095 */     double rad = angle * 0.017453292519943295D;
/* 2096 */     int label_x = center.x + (int)Math.round(Math.cos(rad) * width * 0.3D);
/* 2097 */     int label_y = center.y + (int)Math.round(Math.sin(rad) * width * 0.3D * factor);
/*      */     
/*      */ 
/* 2100 */     Dimension labelSize = getLabelSize(label, fm);
/* 2101 */     label_x -= labelSize.width / 2;
/* 2102 */     label_y -= labelSize.height / 2 - fm.getAscent();
/*      */     
/*      */ 
/* 2105 */     Color frameColor = getSampleColor(getSeriesCount() == 1 ? sample : series).darker();
/* 2106 */     paintFloatingLabel(g, label, label_x, label_y, labelSize, frameColor, fm);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   private void paintStaticLabels(Graphics g)
/*      */   {
/* 2118 */     if (g == null) {
/* 2119 */       return;
/*      */     }
/*      */     
/*      */ 
/* 2123 */     int seriesCount = getSeriesCount();
/* 2124 */     boolean do_paint = (this.percentLabelsOn) && (this.percentLabelStyle != 3);
/* 2125 */     do_paint |= ((isValueLabelsOn()) && (this.valueLabelStyle != 3));
/* 2126 */     do_paint |= ((this.sampleLabelsOn) && (this.sampleLabelStyle != 3));
/* 2127 */     do_paint |= ((this.seriesLabelsOn) && (seriesCount > 1) && (this.seriesLabelStyle != 3));
/* 2128 */     if (!do_paint) {
/* 2129 */       return;
/*      */     }
/*      */     
/*      */ 
/* 2133 */     Font font = getFont("insideLabelFont");
/* 2134 */     FontMetrics fm = getFontMetrics(font);
/* 2135 */     g.setFont(font);
/* 2136 */     int sampleCount = getSampleCount();
/* 2137 */     for (int serie = 0; serie < seriesCount; serie++) {
/* 2138 */       for (int sample = 0; sample < sampleCount; sample++) {
/* 2139 */         ChartSample s = getSample(serie, sample);
/* 2140 */         if ((s != null) && (s.value != null) && (!s.value.isNaN())) {
/* 2141 */           String label = constructLabel(serie, sample, 0, true, null);
/* 2142 */           paintInsideLabel(g, label, serie, sample, font, fm);
/*      */         }
/*      */       }
/*      */     }
/*      */     
/* 2147 */     boolean outsideOn = ((this.percentLabelsOn) && (this.percentLabelStyle == 1)) || 
/* 2148 */       ((this.sampleLabelsOn) && (this.sampleLabelStyle == 1)) || 
/* 2149 */       ((this.seriesLabelsOn) && (this.seriesLabelStyle == 1)) || (
/* 2150 */       (isValueLabelsOn()) && (this.valueLabelStyle == 1));
/*      */     
/* 2152 */     boolean pointingOn = ((this.percentLabelsOn) && (this.percentLabelStyle == 5)) || 
/* 2153 */       ((this.sampleLabelsOn) && (this.sampleLabelStyle == 5)) || 
/* 2154 */       ((this.seriesLabelsOn) && (this.seriesLabelStyle == 5)) || (
/* 2155 */       (isValueLabelsOn()) && (this.valueLabelStyle == 5));
/*      */     
/* 2157 */     if ((outsideOn) && (!pointingOn)) {
/* 2158 */       paintOutsideLabels(g);
/*      */     }
/*      */     
/* 2161 */     if (pointingOn) {
/* 2162 */       paintPointingLabels(g);
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
/*      */   private void paintInsideLabel(Graphics g, String label, int serie, int index, Font font, FontMetrics fm)
/*      */   {
/* 2178 */     int seriesCount = getSeriesCount();
/* 2179 */     Point center = this.pieCenter[0];
/* 2180 */     if (center == null) {
/* 2181 */       return;
/*      */     }
/*      */     
/*      */ 
/*      */ 
/* 2186 */     double angle = 0.0D;
/* 2187 */     double start = this.angle_start[serie][index];
/* 2188 */     double end = this.angle_end[serie][index];
/* 2189 */     angle = -((start + end) / 2.0D);
/*      */     
/*      */ 
/*      */ 
/* 2193 */     double factor = 1.0D;
/* 2194 */     int width = this.pieWidth[index];
/*      */     
/*      */ 
/* 2197 */     double detach = 0.0D;
/* 2198 */     if ((isSelected(serie, index)) && (start - end < 360.0D) && (this.selectionStyle == 2)) {
/* 2199 */       detach = width * this.detachedDistance / 2.0D;
/*      */     }
/*      */     
/* 2202 */     if (is3DModeOn()) {
/* 2203 */       int change = (int)Math.round(width * (this.pieAngle / 90.0D));
/* 2204 */       factor = (width - change) / width;
/*      */     }
/*      */     
/*      */ 
/* 2208 */     double rad = angle * 0.017453292519943295D;
/* 2209 */     int label_x = center.x + (int)Math.round(Math.cos(rad) * width * 0.35D + Math.cos(rad) * detach);
/* 2210 */     int label_y = center.y + (int)Math.round((Math.sin(rad) * width * 0.35D + Math.sin(rad) * detach) * factor);
/*      */     
/*      */ 
/* 2213 */     Dimension labelSize = getLabelSize(label, fm);
/* 2214 */     label_x -= labelSize.width / 2;
/* 2215 */     label_y -= labelSize.height / 2 - fm.getAscent();
/*      */     
/*      */ 
/* 2218 */     int pieIndex = 0;
/* 2219 */     if (seriesCount > 1) {
/* 2220 */       pieIndex = index;
/*      */     }
/* 2222 */     Color color = getInsideLabelColor(pieIndex);
/*      */     
/*      */ 
/* 2225 */     if (color == null) {
/* 2226 */       color = getInsideLabelColor(-1);
/*      */     }
/*      */     
/*      */ 
/*      */ 
/* 2231 */     if (color == null) {
/* 2232 */       Color c = getSampleColor(seriesCount == 1 ? index : serie);
/* 2233 */       if ((c.getRed() + c.getGreen() + c.getBlue()) / 3 > 110) {
/* 2234 */         color = Color.black;
/*      */       } else {
/* 2236 */         color = Color.white;
/*      */       }
/*      */     }
/* 2239 */     g.setColor(color);
/*      */     
/*      */ 
/* 2242 */     paintLabel(g, label, label_x, label_y, labelSize, 0, 0, false);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   private void paintOutsideLabels(Graphics g)
/*      */   {
/* 2251 */     Font font = getFont("outsideLabelFont");
/* 2252 */     FontMetrics fm = getFontMetrics(font);
/* 2253 */     g.setFont(font);
/* 2254 */     int sampleCount = getSampleCount();
/* 2255 */     int seriesCount = getSeriesCount();
/* 2256 */     Rectangle[] first = new Rectangle[sampleCount];
/* 2257 */     Rectangle[] prev = new Rectangle[sampleCount];
/* 2258 */     for (int i = 0; i < sampleCount; i++) {
/* 2259 */       prev[i] = new Rectangle(-10, -10, -10, -10);
/*      */     }
/* 2261 */     for (int serie = 0; serie < seriesCount; serie++) {
/* 2262 */       for (int sample = 0; sample < sampleCount; sample++)
/*      */       {
/* 2264 */         ChartSample s = getSample(serie, sample);
/* 2265 */         if ((s == null) || (s.value == null) || (s.value.isNaN())) {
/* 2266 */           return;
/*      */         }
/*      */         
/* 2269 */         Point center = this.pieCenter[0];
/* 2270 */         if (center == null) {
/* 2271 */           return;
/*      */         }
/*      */         
/*      */ 
/* 2275 */         String label = constructLabel(serie, sample, 1, true, null);
/*      */         
/*      */ 
/*      */ 
/* 2279 */         double start = this.angle_start[serie][sample];
/* 2280 */         double end = this.angle_end[serie][sample];
/* 2281 */         double angle = -((start + end) / 2.0D);
/*      */         
/*      */ 
/*      */ 
/* 2285 */         double factor = 1.0D;
/* 2286 */         int width = this.pieWidth[sample];
/* 2287 */         if (is3DModeOn()) {
/* 2288 */           int change = (int)Math.round(width * (this.pieAngle / 90.0D));
/* 2289 */           factor = (width - change) / width;
/*      */         }
/*      */         
/*      */ 
/* 2293 */         double detach = 0.0D;
/* 2294 */         if ((isSelected(serie, sample)) && (start - end < 360.0D) && (this.selectionStyle == 2)) {
/* 2295 */           detach = width * this.detachedDistance / 2.0D;
/*      */         }
/*      */         
/*      */ 
/* 2299 */         angle = (angle + 360.0D) % 360.0D;
/* 2300 */         double rad = angle * 0.017453292519943295D;
/* 2301 */         Dimension labelSize = getLabelSize(label, fm);
/*      */         
/*      */ 
/* 2304 */         int label_x = center.x + (int)Math.round(Math.cos(rad) * width * 0.5D + Math.cos(rad) * detach);
/* 2305 */         int label_y = center.y + (int)Math.round((Math.sin(rad) * width * 0.5D + Math.sin(rad) * detach) * factor);
/* 2306 */         label_y -= labelSize.height / 2;
/* 2307 */         double label_space = Math.max(width * 0.05D, 10.0D);
/* 2308 */         if ((angle > 292.5D) && (angle <= 337.5D)) {
/* 2309 */           label_x = (int)(label_x + label_space * Math.cos(rad));
/* 2310 */           label_y = (int)(label_y + (label_space * Math.sin(rad) - labelSize.height / 2));
/* 2311 */         } else if ((angle > 247.5D) && (angle <= 292.5D)) {
/* 2312 */           label_x = (int)(label_x + (label_space * Math.cos(rad) - labelSize.width / 2));
/* 2313 */           label_y = (int)(label_y + (label_space * Math.sin(rad) - labelSize.height / 2));
/* 2314 */         } else if ((angle > 202.5D) && (angle <= 247.5D)) {
/* 2315 */           label_x = (int)(label_x + (label_space * Math.cos(rad) - labelSize.width));
/* 2316 */           label_y = (int)(label_y + (label_space * Math.sin(rad) - labelSize.height / 2));
/* 2317 */         } else if ((angle > 157.5D) && (angle <= 202.5D)) {
/* 2318 */           label_x = (int)(label_x + (label_space * Math.cos(rad) - labelSize.width));
/* 2319 */           label_y = (int)(label_y + label_space * Math.sin(rad));
/* 2320 */         } else if ((angle > 112.5D) && (angle <= 157.5D)) {
/* 2321 */           label_x = (int)(label_x + (label_space * Math.cos(rad) - labelSize.width));
/* 2322 */           label_y = (int)(label_y + (label_space * Math.sin(rad) + labelSize.height / 2));
/* 2323 */         } else if ((angle > 67.5D) && (angle <= 112.5D)) {
/* 2324 */           label_x = (int)(label_x + (label_space * Math.cos(rad) - labelSize.width / 2));
/* 2325 */           label_y = (int)(label_y + (label_space * Math.sin(rad) + labelSize.height / 2));
/* 2326 */         } else if ((angle > 22.5D) && (angle <= 67.5D)) {
/* 2327 */           label_x = (int)(label_x + label_space * Math.cos(rad));
/* 2328 */           label_y = (int)(label_y + (label_space * Math.sin(rad) + labelSize.height / 2));
/*      */         } else {
/* 2330 */           label_x = (int)(label_x + label_space * Math.cos(rad));
/* 2331 */           label_y = (int)(label_y + label_space * Math.sin(rad));
/*      */         }
/*      */         
/*      */ 
/* 2335 */         if ((is3DModeOn()) && (angle > 7.0D) && (angle < 173.0D)) {
/* 2336 */           label_y = (int)(label_y + Math.round(width * this.pieDepth * (this.pieAngle / 90.0D)));
/*      */         }
/*      */         
/*      */ 
/* 2340 */         int pieIndex = 0;
/* 2341 */         if (seriesCount > 1) {
/* 2342 */           pieIndex = sample;
/*      */         }
/* 2344 */         Color color = getOutsideLabelColor(pieIndex);
/*      */         
/*      */ 
/* 2347 */         if (color == null) {
/* 2348 */           color = getOutsideLabelColor(-1);
/*      */         }
/*      */         
/* 2351 */         if (color == null) {
/* 2352 */           color = getForeground();
/*      */         }
/* 2354 */         g.setColor(color);
/*      */         
/*      */ 
/* 2357 */         Rectangle cur = new Rectangle(label_x, label_y, labelSize.width, labelSize.height);
/* 2358 */         Rectangle inter = seriesCount == 1 ? cur.intersection(prev[0]) : cur.intersection(prev[sample]);
/* 2359 */         boolean should_paint = (inter.width <= 0) || (inter.height <= 0);
/* 2360 */         if (((seriesCount != 0) || (sample != 0)) && ((seriesCount <= 0) || (serie != 0))) {
/* 2361 */           inter = seriesCount == 1 ? cur.intersection(first[0]) : cur.intersection(first[sample]);
/* 2362 */           should_paint &= ((inter.width <= 0) || (inter.height <= 0));
/*      */         }
/* 2364 */         if (should_paint) {
/* 2365 */           paintLabel(g, label, label_x, label_y + fm.getAscent(), labelSize, 0, 0, false);
/*      */           
/* 2367 */           if (seriesCount == 1) {
/* 2368 */             prev[0] = cur;
/*      */           } else {
/* 2370 */             prev[sample] = cur;
/*      */           }
/*      */         }
/*      */         
/* 2374 */         if ((seriesCount == 1) && (sample == 0)) {
/* 2375 */           first[0] = cur;
/* 2376 */         } else if ((seriesCount > 1) && (serie == 0)) {
/* 2377 */           first[sample] = cur;
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
/*      */   private void paintPointingLabels(Graphics g)
/*      */   {
/* 2390 */     boolean pointingOn = ((this.percentLabelsOn) && (this.percentLabelStyle == 5)) || 
/* 2391 */       ((this.sampleLabelsOn) && (this.sampleLabelStyle == 5)) || 
/* 2392 */       ((this.seriesLabelsOn) && (this.seriesLabelStyle == 5)) || (
/* 2393 */       (isValueLabelsOn()) && (this.valueLabelStyle == 5));
/* 2394 */     if (!pointingOn) {
/* 2395 */       return;
/*      */     }
/*      */     
/*      */ 
/* 2399 */     Font font = getFont("pointingLabelFont");
/* 2400 */     FontMetrics fm = getFontMetrics(font);
/* 2401 */     g.setFont(font);
/* 2402 */     int seriesCount = getSeriesCount();
/* 2403 */     int sampleCount = getSampleCount();
/* 2404 */     int pieCount = seriesCount > 1 ? sampleCount : 1;
/* 2405 */     int sliceCount = seriesCount > 1 ? seriesCount : sampleCount;
/* 2406 */     for (int pieIndex = 0; pieIndex < pieCount; pieIndex++) {
/* 2407 */       Point center = this.pieCenter[pieIndex];
/* 2408 */       if (center != null)
/*      */       {
/*      */ 
/*      */ 
/* 2412 */         int length = 0;
/* 2413 */         for (int i = 0; i < sliceCount; i++) {
/* 2414 */           ChartSample s = null;
/* 2415 */           if (pieCount == 1) {
/* 2416 */             s = getSample(0, i);
/*      */           } else {
/* 2418 */             s = getSample(i, pieIndex);
/*      */           }
/* 2420 */           if ((s != null) && (s.value != null) && (!s.value.isNaN())) {
/* 2421 */             length++;
/*      */           }
/*      */         }
/* 2424 */         String[] pointingLabels = new String[length];
/* 2425 */         Dimension[] labelSize = new Dimension[length];
/* 2426 */         Point[] labelPos = new Point[length];
/* 2427 */         Point[] lineStart = new Point[length];
/* 2428 */         double[] sampleAngle = new double[length];
/* 2429 */         for (int slice = 0; slice < sliceCount; slice++) {
/* 2430 */           ChartSample s = null;
/* 2431 */           if (pieCount == 1) {
/* 2432 */             s = getSample(0, slice);
/*      */           } else {
/* 2434 */             s = getSample(slice, pieIndex);
/*      */           }
/* 2436 */           if ((s != null) && (s.value != null) && (!s.value.isNaN()))
/*      */           {
/*      */ 
/*      */ 
/* 2440 */             if (pieCount == 1) {
/* 2441 */               pointingLabels[slice] = constructLabel(0, slice, 5, true, null);
/*      */             } else {
/* 2443 */               pointingLabels[slice] = constructLabel(slice, pieIndex, 5, true, null);
/*      */             }
/*      */             
/*      */ 
/*      */ 
/* 2448 */             double angle = 0.0D;
/* 2449 */             double start = 0.0D;
/* 2450 */             double end = 0.0D;
/* 2451 */             if (getSeriesCount() == 1) {
/* 2452 */               start = this.angle_start[0][slice];
/* 2453 */               end = this.angle_end[0][slice];
/*      */             } else {
/* 2455 */               start = this.angle_start[slice][pieIndex];
/* 2456 */               end = this.angle_end[slice][pieIndex];
/*      */             }
/* 2458 */             angle = -((start + end) / 2.0D);
/* 2459 */             sampleAngle[slice] = angle;
/*      */             
/*      */ 
/*      */ 
/* 2463 */             double factor = 1.0D;
/* 2464 */             int width = this.pieWidth[pieIndex];
/* 2465 */             if (is3DModeOn()) {
/* 2466 */               int change = (int)Math.round(width * (this.pieAngle / 90.0D));
/* 2467 */               factor = (width - change) / width;
/*      */             }
/*      */             
/*      */ 
/* 2471 */             boolean selected = false;
/* 2472 */             if (seriesCount == 1) {
/* 2473 */               selected = isSelected(0, slice);
/*      */             } else {
/* 2475 */               selected = isSelected(slice, pieIndex);
/*      */             }
/*      */             
/*      */ 
/* 2479 */             double detach = 0.0D;
/* 2480 */             if ((selected) && (start - end < 360.0D) && (this.selectionStyle == 2)) {
/* 2481 */               detach = width * this.detachedDistance / 2.0D;
/*      */             }
/*      */             
/*      */ 
/* 2485 */             angle += (angle < 0.0D ? 360 : 0);
/* 2486 */             double rad = angle * 0.017453292519943295D;
/* 2487 */             labelSize[slice] = getLabelSize(pointingLabels[slice], fm);
/*      */             
/*      */ 
/* 2490 */             double line_x = center.x + (width / 2.0D + detach) * Math.cos(rad);
/* 2491 */             double line_y = center.y + ((width + 2) / 2.0D + detach) * Math.sin(rad) * factor;
/* 2492 */             int label_x = 0;
/* 2493 */             if (((sampleAngle[slice] >= 90.0D) && (sampleAngle[slice] < 270.0D)) || (
/* 2494 */               (sampleAngle[slice] >= 450.0D) && (sampleAngle[slice] < 630.0D))) {
/* 2495 */               label_x = center.x - width / 2 - this.pointingDistance - labelSize[slice].width;
/*      */             } else {
/* 2497 */               label_x = center.x + width / 2 + this.pointingDistance;
/*      */             }
/* 2499 */             int label_y = (int)(line_y - labelSize[slice].height / 2.0D + fm.getAscent());
/* 2500 */             lineStart[slice] = new Point((int)line_x, (int)line_y);
/* 2501 */             labelPos[slice] = new Point(label_x, label_y);
/*      */           }
/*      */         }
/*      */         
/* 2505 */         int first_right = 0;
/* 2506 */         for (int i = length - 1; i >= 0; i--) {
/* 2507 */           if ((sampleAngle[i] + 90.0D) % 360.0D < (sampleAngle[first_right] + 90.0D) % 360.0D) {
/* 2508 */             first_right = i;
/*      */           }
/*      */         }
/* 2511 */         int first_left = 0;
/* 2512 */         for (int i = 0; i < length; i++) {
/* 2513 */           int next = i == length - 1 ? 0 : i + 1;
/* 2514 */           if ((sampleAngle[i] + 90.0D) % 360.0D > (sampleAngle[first_left] + 90.0D) % 360.0D) {
/* 2515 */             first_left = i;
/*      */           }
/*      */         }
/*      */         
/*      */ 
/* 2520 */         String[] rPointingLab = new String[length];
/* 2521 */         String[] lPointingLab = new String[length];
/* 2522 */         Dimension[] rLabelSize = new Dimension[length];
/* 2523 */         Dimension[] lLabelSize = new Dimension[length];
/* 2524 */         Point[] rLabelPos = new Point[length];
/* 2525 */         Point[] lLabelPos = new Point[length];
/* 2526 */         Point[] rLineStart = new Point[length];
/* 2527 */         Point[] lLineStart = new Point[length];
/* 2528 */         double[] rSampleAngle = new double[length];
/* 2529 */         double[] lSampleAngle = new double[length];
/* 2530 */         int rcount = 0;
/* 2531 */         int lcount = 0;
/* 2532 */         for (int i = 0; i < length; i++) {
/* 2533 */           if (((sampleAngle[i] >= -90.0D) && (sampleAngle[i] < 90.0D)) || (
/* 2534 */             (sampleAngle[i] >= 270.0D) && (sampleAngle[i] < 450.0D))) {
/* 2535 */             rPointingLab[rcount] = pointingLabels[first_right];
/* 2536 */             rLabelPos[rcount] = labelPos[first_right];
/* 2537 */             rLabelSize[rcount] = labelSize[first_right];
/* 2538 */             rLineStart[rcount] = lineStart[first_right];
/* 2539 */             rSampleAngle[rcount] = sampleAngle[first_right];
/* 2540 */             first_right = first_right == length - 1 ? 0 : first_right + 1;
/* 2541 */             rcount++;
/*      */           }
/*      */         }
/* 2544 */         for (int i = 0; i < length; i++) {
/* 2545 */           if (((sampleAngle[i] >= 90.0D) && (sampleAngle[i] < 270.0D)) || (
/* 2546 */             (sampleAngle[i] >= 450.0D) && (sampleAngle[i] < 630.0D))) {
/* 2547 */             lPointingLab[lcount] = pointingLabels[first_left];
/* 2548 */             lLabelPos[lcount] = labelPos[first_left];
/* 2549 */             lLabelSize[lcount] = labelSize[first_left];
/* 2550 */             lLineStart[lcount] = lineStart[first_left];
/* 2551 */             lSampleAngle[lcount] = sampleAngle[first_left];
/* 2552 */             first_left = first_left == 0 ? length - 1 : first_left - 1;
/* 2553 */             lcount++;
/*      */           }
/*      */         }
/*      */         
/*      */ 
/* 2558 */         alignPointingLabels(pieIndex, rLabelPos, rLabelSize, rLineStart, center, rcount, fm);
/* 2559 */         alignPointingLabels(pieIndex, lLabelPos, lLabelSize, lLineStart, center, lcount, fm);
/*      */         
/*      */ 
/* 2562 */         Color color = getPointingLabelColor(pieIndex);
/*      */         
/* 2564 */         if (color == null) {
/* 2565 */           color = getPointingLabelColor(-1);
/*      */         }
/*      */         
/* 2568 */         if (color == null) {
/* 2569 */           color = getForeground();
/*      */         }
/* 2571 */         g.setColor(color);
/*      */         
/*      */ 
/* 2574 */         for (int i = 0; i < rcount; i++) {
/* 2575 */           if (rLabelPos[i] != null) {
/* 2576 */             int start_x = rLineStart[i].x + 2;
/* 2577 */             int start_y = rLineStart[i].y + 1;
/* 2578 */             int stop_x = rLabelPos[i].x - 2;
/* 2579 */             int stop_y = rLabelPos[i].y - fm.getAscent() + rLabelSize[i].height / 2 + 1;
/* 2580 */             double rad = rSampleAngle[i] * 0.017453292519943295D;
/* 2581 */             if (((start_y > center.y) && (stop_y > start_y)) || ((start_y <= center.y) && (stop_y < start_y))) {
/* 2582 */               int delta_x = (int)((stop_y - start_y) / Math.tan(rad));
/* 2583 */               delta_x = Math.min(delta_x, stop_x - start_x - this.pointingDistance / 2);
/* 2584 */               g.drawLine(start_x, start_y, start_x + delta_x, stop_y);
/* 2585 */               g.drawLine(start_x + delta_x, stop_y, stop_x, stop_y);
/*      */             } else {
/* 2587 */               int delta_x = (this.pieWidth[pieIndex] / 2 - (start_x - center.x)) / 3;
/* 2588 */               g.drawLine(stop_x - this.pointingDistance / 2, stop_y, stop_x, stop_y);
/* 2589 */               g.drawLine(start_x + delta_x, start_y, stop_x - this.pointingDistance / 2, stop_y);
/* 2590 */               g.drawLine(start_x, start_y, start_x + delta_x, start_y);
/*      */             }
/* 2592 */             paintLabel(g, rPointingLab[i], rLabelPos[i].x, rLabelPos[i].y, rLabelSize[i], 0, 0, false);
/*      */           }
/*      */         }
/*      */         
/* 2596 */         for (int i = 0; i < lcount; i++) {
/* 2597 */           if (lLabelPos[i] != null) {
/* 2598 */             int start_x = lLineStart[i].x;
/* 2599 */             int start_y = lLineStart[i].y + 1;
/* 2600 */             int stop_x = lLabelPos[i].x + lLabelSize[i].width;
/* 2601 */             int stop_y = lLabelPos[i].y - fm.getAscent() + lLabelSize[i].height / 2 + 1;
/* 2602 */             double rad = lSampleAngle[i] * 0.017453292519943295D;
/* 2603 */             if (((start_y > center.y) && (stop_y > start_y)) || ((start_y <= center.y) && (stop_y < start_y))) {
/* 2604 */               int delta_x = (int)((stop_y - start_y) / Math.tan(rad));
/* 2605 */               delta_x = Math.max(delta_x, stop_x - start_x + this.pointingDistance / 2);
/* 2606 */               g.drawLine(start_x, start_y, start_x + delta_x, stop_y);
/* 2607 */               g.drawLine(start_x + delta_x, stop_y, stop_x, stop_y);
/*      */             } else {
/* 2609 */               int delta_x = (this.pieWidth[pieIndex] / 2 - (center.x - start_x)) / 3;
/* 2610 */               g.drawLine(stop_x + this.pointingDistance / 2, stop_y, stop_x, stop_y);
/* 2611 */               g.drawLine(start_x - delta_x, start_y, stop_x + this.pointingDistance / 2, stop_y);
/* 2612 */               g.drawLine(start_x, start_y, start_x - delta_x, start_y);
/*      */             }
/* 2614 */             paintLabel(g, lPointingLab[i], lLabelPos[i].x, lLabelPos[i].y, lLabelSize[i], 0, 0, false);
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
/*      */   private void alignPointingLabels(int pieIndex, Point[] labelPos, Dimension[] labelSize, Point[] lineStart, Point center, int length, FontMetrics fm)
/*      */   {
/* 2633 */     if ((labelPos == null) || (labelSize == null) || (lineStart == null) || (length < 1)) {
/* 2634 */       return;
/*      */     }
/*      */     
/*      */ 
/* 2638 */     labelPos[0].y = Math.max(labelPos[0].y, this.pieOuterBounds[pieIndex].y + fm.getAscent());
/* 2639 */     labelPos[(length - 1)].y = Math.min(labelPos[(length - 1)].y, this.pieOuterBounds[pieIndex].y + this.pieOuterBounds[pieIndex].height - labelSize[(length - 1)].height + fm.getAscent());
/*      */     
/*      */ 
/* 2642 */     int space_needed = labelSize[0].height;
/* 2643 */     for (int i = 1; i < length; i++) {
/* 2644 */       space_needed += labelSize[i].height + 10;
/*      */     }
/*      */     
/*      */ 
/* 2648 */     int pieHeight = this.pieWidth[pieIndex];
/* 2649 */     if (is3DModeOn()) {
/* 2650 */       int effect3d = (int)Math.round(this.pieWidth[pieIndex] * this.pieDepth * (this.pieAngle / 90.0D));
/* 2651 */       pieHeight = (int)Math.round(this.pieWidth[pieIndex] * this.size_factor + effect3d / 2);
/*      */     }
/* 2653 */     int top = center.y - pieHeight / 2;
/* 2654 */     if (space_needed <= pieHeight)
/*      */     {
/* 2656 */       int free_space = labelPos[0].y - (top + fm.getAscent());
/* 2657 */       for (int i = 1; i < length; i++) {
/* 2658 */         int gap = labelPos[i].y - (labelPos[(i - 1)].y + labelSize[(i - 1)].height) - 10;
/* 2659 */         if (gap > 0) {
/* 2660 */           free_space += gap;
/*      */ 
/*      */         }
/* 2663 */         else if (gap < 0) {
/* 2664 */           if (-gap < free_space) {
/* 2665 */             free_space += gap;
/* 2666 */             for (int j = i; j > 0; j--) {
/* 2667 */               if (j == 1) {
/* 2668 */                 gap += labelPos[(j - 1)].y - (top + fm.getAscent());
/*      */               } else {
/* 2670 */                 gap += labelPos[(j - 1)].y - (labelPos[(j - 2)].y + labelSize[(j - 2)].height) - 10;
/*      */               }
/* 2672 */               labelPos[(j - 1)].y = (labelPos[j].y - labelSize[(j - 1)].height - 10);
/* 2673 */               if (gap >= 0) {
/*      */                 break;
/*      */               }
/*      */             }
/*      */           } else {
/* 2678 */             for (int j = 1; j <= i; j++) {
/* 2679 */               labelPos[j].y = (labelPos[(j - 1)].y + labelSize[(j - 1)].height + 10);
/*      */             }
/* 2681 */             free_space = 0;
/*      */           }
/*      */           
/*      */         }
/*      */       }
/*      */     }
/*      */     else
/*      */     {
/* 2689 */       int visible_labels = length;
/* 2690 */       while (space_needed > this.pieOuterBounds[pieIndex].height)
/*      */       {
/* 2692 */         int mid = visible_labels / 2;
/* 2693 */         if (visible_labels % 2 == 1) {
/* 2694 */           mid = length - mid;
/*      */         }
/* 2696 */         labelPos[(length - mid)] = null;
/* 2697 */         space_needed -= labelSize[(length - mid)].height + 10;
/* 2698 */         visible_labels--;
/*      */       }
/*      */       
/*      */ 
/* 2702 */       int offset = (this.pieOuterBounds[pieIndex].height - space_needed) / 2;
/* 2703 */       int cur_pos = this.pieOuterBounds[pieIndex].y + fm.getAscent() + offset;
/* 2704 */       for (int i = 0; i < length; i++) {
/* 2705 */         if (labelPos[i] != null) {
/* 2706 */           labelPos[i].y = cur_pos;
/* 2707 */           cur_pos += labelSize[i].height + 10;
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
/*      */   protected String constructLabel(int series, int sample, int style, boolean paintSeriesOn, String percentLabel)
/*      */   {
/* 2724 */     boolean percentOn = (this.percentLabelsOn) && (this.percentLabelStyle == style);
/* 2725 */     if ((percentOn) && (series >= 0)) {
/* 2726 */       percentLabel = formatNumber(getPercentValue(series, sample), getPercentDecimalCount(), false) + "%";
/*      */     }
/* 2728 */     return super.constructLabel(series, sample, style, paintSeriesOn, percentLabel);
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
/* 2742 */     boolean labels_on = (this.sampleLabelsOn) && (this.sampleLabelStyle == 3);
/* 2743 */     labels_on |= ((this.seriesLabelsOn) && (this.seriesLabelStyle == 3));
/* 2744 */     labels_on |= ((isValueLabelsOn(serie)) && (this.valueLabelStyle == 3));
/* 2745 */     labels_on |= ((this.percentLabelsOn) && (this.percentLabelStyle == 3));
/* 2746 */     return labels_on;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   protected void processEvent(AWTEvent e)
/*      */   {
/* 2754 */     int seriesCount = getSeriesCount();
/* 2755 */     int xpos = 0;
/* 2756 */     int ypos = 0;
/* 2757 */     double rad = 0.0D;
/* 2758 */     switch (e.getID())
/*      */     {
/*      */ 
/*      */     case 501: 
/* 2762 */       ChartSample sample = checkSelection(((MouseEvent)e).getPoint());
/* 2763 */       this.rotatedPie = 0;
/* 2764 */       if ((seriesCount != 1) && (sample != null)) {
/* 2765 */         this.rotatedPie = sample.getIndex();
/*      */       }
/*      */       
/* 2768 */       if (this.rotatedPie != -1) {
/* 2769 */         xpos = ((MouseEvent)e).getX();
/* 2770 */         ypos = ((MouseEvent)e).getY();
/* 2771 */         xpos -= this.pieCenter[this.rotatedPie].x;
/* 2772 */         ypos -= this.pieCenter[this.rotatedPie].y;
/* 2773 */         rad = Math.atan2(ypos, xpos);
/* 2774 */         this.oldAngle = (-(rad * 57.29577951308232D));
/* 2775 */         if (sample == null) {
/* 2776 */           this.oldAngle = NaN.0D;
/*      */         }
/*      */       }
/* 2779 */       break;
/*      */     case 506: 
/* 2781 */       xpos = ((MouseEvent)e).getX();
/* 2782 */       ypos = ((MouseEvent)e).getY();
/*      */       
/*      */ 
/* 2785 */       if ((this.pieRotationOn) && (!new Double(this.oldAngle).isNaN()) && (this.dragged_label == null))
/*      */       {
/*      */ 
/*      */ 
/* 2789 */         xpos -= this.pieCenter[this.rotatedPie].x;
/* 2790 */         ypos -= this.pieCenter[this.rotatedPie].y;
/* 2791 */         rad = Math.atan2(ypos, xpos);
/* 2792 */         double angle = -(rad * 57.29577951308232D);
/* 2793 */         double delta_angle = angle - this.oldAngle;
/* 2794 */         if (Math.abs(delta_angle) >= 2.0D) {
/* 2795 */           this.rotateAngles[this.rotatedPie] += delta_angle - delta_angle % 2.0D;
/* 2796 */           this.rotateAngles[this.rotatedPie] -= 360.0D;
/* 2797 */           this.rotateAngles[this.rotatedPie] %= 360.0D;
/* 2798 */           this.oldAngle = angle;
/*      */         }
/*      */         
/*      */ 
/*      */ 
/*      */ 
/* 2804 */         if (System.currentTimeMillis() - this.lastClickTime > 150L) {
/* 2805 */           this.shouldSelect = false;
/*      */         }
/* 2807 */         this.needRender = true;
/* 2808 */         repaint();
/*      */       }
/*      */       break; }
/* 2811 */     super.processEvent(e);
/*      */   }
/*      */ }


/* Location:              E:\java\java学习\hutubill\lib\all.jar!\com\objectplanet\chart\PieChart.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */
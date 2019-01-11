/*     */ package com.objectplanet.chart;
/*     */ 
/*     */ import java.awt.Color;
/*     */ import java.io.Serializable;
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
/*     */ public class ChartSample
/*     */   implements Serializable
/*     */ {
/*     */   protected Double value;
/*     */   protected String label;
/*     */   Color labelColor;
/*  31 */   private static final Color DEFAULT_COLOR = Color.black;
/*     */   
/*     */ 
/*     */ 
/*     */   private int series;
/*     */   
/*     */ 
/*     */ 
/*     */   private int index;
/*     */   
/*     */ 
/*     */ 
/*     */   Object key;
/*     */   
/*     */ 
/*     */ 
/*     */   private boolean selected;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public ChartSample(int index)
/*     */   {
/*  54 */     this.index = index;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public ChartSample(int index, double value)
/*     */   {
/*  64 */     this.index = index;
/*  65 */     this.value = new Double(value);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public ChartSample(int index, double value, String label, Object key)
/*     */   {
/*  77 */     this.index = index;
/*  78 */     this.value = new Double(value);
/*  79 */     this.label = label;
/*  80 */     this.key = key;
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
/*     */   public void set(double value, String label, Object key)
/*     */   {
/*  95 */     this.value = new Double(value);
/*  96 */     this.label = label;
/*  97 */     this.key = key;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setValue(double value)
/*     */   {
/* 106 */     this.value = new Double(value);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public long getValue()
/*     */   {
/* 114 */     if (this.value != null) {
/* 115 */       return this.value.longValue();
/*     */     }
/* 117 */     return 0L;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public double getFloatValue()
/*     */   {
/* 127 */     if (this.value != null) {
/* 128 */       return this.value.doubleValue();
/*     */     }
/* 130 */     return NaN.0D;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean hasValue()
/*     */   {
/* 139 */     return (this.value != null) && (!this.value.isNaN());
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void clearValue()
/*     */   {
/* 148 */     this.value = null;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setLabel(String label)
/*     */   {
/* 157 */     this.label = label;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public String getLabel()
/*     */   {
/* 165 */     return this.label;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setLabelColor(Color color)
/*     */   {
/* 175 */     this.labelColor = color;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   void setIndex(int index)
/*     */   {
/* 185 */     this.index = index;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public int getIndex()
/*     */   {
/* 194 */     return this.index;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   void setSeries(int series)
/*     */   {
/* 203 */     this.series = series;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public int getSeries()
/*     */   {
/* 211 */     return this.series;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public Object getKey()
/*     */   {
/* 221 */     return this.key;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean setSelection(boolean state)
/*     */   {
/* 231 */     this.selected = state;
/* 232 */     return this.selected;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean isSelected()
/*     */   {
/* 241 */     return this.selected;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean toggleSelection()
/*     */   {
/* 250 */     this.selected = (!this.selected);
/* 251 */     return this.selected;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public String toString()
/*     */   {
/* 259 */     if ((this.label != null) && (this.value != null)) {
/* 260 */       return this.series + ":" + this.index + " " + this.label + " " + this.value.doubleValue();
/*     */     }
/* 262 */     if (this.value != null) {
/* 263 */       return this.series + ":" + this.index + " " + "null " + this.value.doubleValue();
/*     */     }
/* 265 */     if (this.label != null) {
/* 266 */       return this.series + ":" + this.index + " " + this.label;
/*     */     }
/* 268 */     return this.series + ":" + this.index + " " + "null 0";
/*     */   }
/*     */ }


/* Location:              E:\java\java学习\hutubill\lib\all.jar!\com\objectplanet\chart\ChartSample.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */
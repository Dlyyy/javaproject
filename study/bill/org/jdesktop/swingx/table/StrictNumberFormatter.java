/*     */ package org.jdesktop.swingx.table;
/*     */ 
/*     */ import java.math.BigDecimal;
/*     */ import java.text.Format;
/*     */ import java.text.NumberFormat;
/*     */ import java.text.ParseException;
/*     */ import javax.swing.text.NumberFormatter;
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
/*     */ @Deprecated
/*     */ class StrictNumberFormatter
/*     */   extends NumberFormatter
/*     */ {
/*     */   private BigDecimal maxAsBig;
/*     */   private BigDecimal minAsBig;
/*     */   
/*     */   public StrictNumberFormatter(NumberFormat format)
/*     */   {
/*  51 */     super(format);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setValueClass(Class<?> valueClass)
/*     */   {
/*  62 */     super.setValueClass(valueClass);
/*  63 */     updateMinMax();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private void updateMinMax()
/*     */   {
/*  72 */     Comparable min = null;
/*  73 */     Comparable max = null;
/*  74 */     if (getValueClass() == Integer.class) {
/*  75 */       max = Integer.valueOf(Integer.MAX_VALUE);
/*  76 */       min = Integer.valueOf(Integer.MIN_VALUE);
/*  77 */     } else if (getValueClass() == Long.class) {
/*  78 */       max = Long.valueOf(Long.MAX_VALUE);
/*  79 */       min = Long.valueOf(Long.MIN_VALUE);
/*  80 */     } else if (getValueClass() == Short.class) {
/*  81 */       max = Short.valueOf((short)Short.MAX_VALUE);
/*  82 */       min = Short.valueOf((short)Short.MIN_VALUE);
/*  83 */     } else if (getValueClass() == Byte.class) {
/*  84 */       max = Byte.valueOf((byte)Byte.MAX_VALUE);
/*  85 */       min = Byte.valueOf((byte)Byte.MIN_VALUE);
/*  86 */     } else if (getValueClass() == Float.class) {
/*  87 */       max = Float.valueOf(Float.MAX_VALUE);
/*  88 */       min = Float.valueOf(Float.MIN_VALUE);
/*  89 */     } else if (getValueClass() != Double.class) {}
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*  94 */     setMaximum(max);
/*  95 */     setMinimum(min);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public void setMaximum(Comparable max)
/*     */   {
/* 102 */     super.setMaximum(max);
/* 103 */     this.maxAsBig = (max != null ? new BigDecimal(max.toString()) : null);
/*     */   }
/*     */   
/*     */ 
/*     */   public void setMinimum(Comparable minimum)
/*     */   {
/* 109 */     super.setMinimum(minimum);
/* 110 */     this.minAsBig = (minimum != null ? new BigDecimal(minimum.toString()) : null);
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
/*     */   public Object stringToValue(String text)
/*     */     throws ParseException
/*     */   {
/* 124 */     Object value = getParsedValue(text, getFormat());
/*     */     try {
/* 126 */       if (!isValueInRange(value, true)) {
/* 127 */         throw new ParseException("Value not within min/max range", 0);
/*     */       }
/*     */     } catch (ClassCastException cce) {
/* 130 */       throw new ParseException("Class cast exception comparing values: " + cce, 0);
/*     */     }
/*     */     
/* 133 */     return convertValueToValueClass(value, getValueClass());
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private Object convertValueToValueClass(Object value, Class<?> valueClass)
/*     */   {
/* 144 */     if ((valueClass != null) && ((value instanceof Number))) {
/* 145 */       if (valueClass == Integer.class) {
/* 146 */         return new Integer(((Number)value).intValue());
/*     */       }
/* 148 */       if (valueClass == Long.class) {
/* 149 */         return new Long(((Number)value).longValue());
/*     */       }
/* 151 */       if (valueClass == Float.class) {
/* 152 */         return new Float(((Number)value).floatValue());
/*     */       }
/* 154 */       if (valueClass == Double.class) {
/* 155 */         return new Double(((Number)value).doubleValue());
/*     */       }
/* 157 */       if (valueClass == Byte.class) {
/* 158 */         return new Byte(((Number)value).byteValue());
/*     */       }
/* 160 */       if (valueClass == Short.class) {
/* 161 */         return new Short(((Number)value).shortValue());
/*     */       }
/*     */     }
/* 164 */     return value;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   private Object getParsedValue(String text, Format f)
/*     */     throws ParseException
/*     */   {
/* 172 */     if (f == null) {
/* 173 */       return text;
/*     */     }
/* 175 */     return f.parseObject(text);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private boolean isValueInRange(Object orgValue, boolean wantsCCE)
/*     */   {
/* 187 */     if (orgValue == null) return true;
/* 188 */     if ((getMinimum() == null) && (getMaximum() == null)) { return true;
/*     */     }
/* 190 */     BigDecimal value = new BigDecimal(orgValue.toString());
/* 191 */     Comparable<BigDecimal> min = getMinimumAsBig();
/*     */     try
/*     */     {
/* 194 */       if ((min != null) && (min.compareTo(value) > 0)) {
/* 195 */         return false;
/*     */       }
/*     */     } catch (ClassCastException cce) {
/* 198 */       if (wantsCCE) {
/* 199 */         throw cce;
/*     */       }
/* 201 */       return false;
/*     */     }
/*     */     
/* 204 */     Comparable<BigDecimal> max = getMaximumAsBig();
/*     */     try {
/* 206 */       if ((max != null) && (max.compareTo(value) < 0)) {
/* 207 */         return false;
/*     */       }
/*     */     } catch (ClassCastException cce) {
/* 210 */       if (wantsCCE) {
/* 211 */         throw cce;
/*     */       }
/* 213 */       return false;
/*     */     }
/* 215 */     return true;
/*     */   }
/*     */   
/*     */   private Comparable<BigDecimal> getMinimumAsBig()
/*     */   {
/* 220 */     return this.minAsBig;
/*     */   }
/*     */   
/*     */   private Comparable<BigDecimal> getMaximumAsBig() {
/* 224 */     return this.maxAsBig;
/*     */   }
/*     */ }


/* Location:              E:\java\java学习\hutubill\lib\all.jar!\org\jdesktop\swingx\table\StrictNumberFormatter.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */
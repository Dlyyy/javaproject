/*     */ package org.jdesktop.swingx.text;
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
/*     */ public class StrictNumberFormatter
/*     */   extends NumberFormatter
/*     */ {
/*     */   private BigDecimal maxAsBig;
/*     */   private BigDecimal minAsBig;
/*     */   
/*     */   public StrictNumberFormatter(NumberFormat format)
/*     */   {
/*  48 */     super(format);
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
/*  59 */     super.setValueClass(valueClass);
/*  60 */     updateMinMax();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private void updateMinMax()
/*     */   {
/*  69 */     Comparable min = null;
/*  70 */     Comparable max = null;
/*  71 */     if (getValueClass() == Integer.class) {
/*  72 */       max = Integer.valueOf(Integer.MAX_VALUE);
/*  73 */       min = Integer.valueOf(Integer.MIN_VALUE);
/*  74 */     } else if (getValueClass() == Long.class) {
/*  75 */       max = Long.valueOf(Long.MAX_VALUE);
/*  76 */       min = Long.valueOf(Long.MIN_VALUE);
/*  77 */     } else if (getValueClass() == Short.class) {
/*  78 */       max = Short.valueOf((short)Short.MAX_VALUE);
/*  79 */       min = Short.valueOf((short)Short.MIN_VALUE);
/*  80 */     } else if (getValueClass() == Byte.class) {
/*  81 */       max = Byte.valueOf((byte)Byte.MAX_VALUE);
/*  82 */       min = Byte.valueOf((byte)Byte.MIN_VALUE);
/*  83 */     } else if (getValueClass() == Float.class) {
/*  84 */       max = Float.valueOf(Float.MAX_VALUE);
/*  85 */       min = Float.valueOf(Float.MIN_VALUE);
/*  86 */     } else if (getValueClass() != Double.class) {}
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*  91 */     setMaximum(max);
/*  92 */     setMinimum(min);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public void setMaximum(Comparable max)
/*     */   {
/*  99 */     super.setMaximum(max);
/* 100 */     this.maxAsBig = (max != null ? new BigDecimal(max.toString()) : null);
/*     */   }
/*     */   
/*     */ 
/*     */   public void setMinimum(Comparable minimum)
/*     */   {
/* 106 */     super.setMinimum(minimum);
/* 107 */     this.minAsBig = (minimum != null ? new BigDecimal(minimum.toString()) : null);
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
/* 121 */     Object value = getParsedValue(text, getFormat());
/*     */     try {
/* 123 */       if (!isValueInRange(value, true)) {
/* 124 */         throw new ParseException("Value not within min/max range", 0);
/*     */       }
/*     */     } catch (ClassCastException cce) {
/* 127 */       throw new ParseException("Class cast exception comparing values: " + cce, 0);
/*     */     }
/*     */     
/* 130 */     return convertValueToValueClass(value, getValueClass());
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
/* 141 */     if ((valueClass != null) && ((value instanceof Number))) {
/* 142 */       if (valueClass == Integer.class) {
/* 143 */         return new Integer(((Number)value).intValue());
/*     */       }
/* 145 */       if (valueClass == Long.class) {
/* 146 */         return new Long(((Number)value).longValue());
/*     */       }
/* 148 */       if (valueClass == Float.class) {
/* 149 */         return new Float(((Number)value).floatValue());
/*     */       }
/* 151 */       if (valueClass == Double.class) {
/* 152 */         return new Double(((Number)value).doubleValue());
/*     */       }
/* 154 */       if (valueClass == Byte.class) {
/* 155 */         return new Byte(((Number)value).byteValue());
/*     */       }
/* 157 */       if (valueClass == Short.class) {
/* 158 */         return new Short(((Number)value).shortValue());
/*     */       }
/*     */     }
/* 161 */     return value;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   private Object getParsedValue(String text, Format f)
/*     */     throws ParseException
/*     */   {
/* 169 */     if (f == null) {
/* 170 */       return text;
/*     */     }
/* 172 */     return f.parseObject(text);
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
/* 184 */     if (orgValue == null) return true;
/* 185 */     if ((getMinimum() == null) && (getMaximum() == null)) { return true;
/*     */     }
/* 187 */     BigDecimal value = new BigDecimal(orgValue.toString());
/* 188 */     Comparable<BigDecimal> min = getMinimumAsBig();
/*     */     try
/*     */     {
/* 191 */       if ((min != null) && (min.compareTo(value) > 0)) {
/* 192 */         return false;
/*     */       }
/*     */     } catch (ClassCastException cce) {
/* 195 */       if (wantsCCE) {
/* 196 */         throw cce;
/*     */       }
/* 198 */       return false;
/*     */     }
/*     */     
/* 201 */     Comparable<BigDecimal> max = getMaximumAsBig();
/*     */     try {
/* 203 */       if ((max != null) && (max.compareTo(value) < 0)) {
/* 204 */         return false;
/*     */       }
/*     */     } catch (ClassCastException cce) {
/* 207 */       if (wantsCCE) {
/* 208 */         throw cce;
/*     */       }
/* 210 */       return false;
/*     */     }
/* 212 */     return true;
/*     */   }
/*     */   
/*     */   private Comparable<BigDecimal> getMinimumAsBig()
/*     */   {
/* 217 */     return this.minAsBig;
/*     */   }
/*     */   
/*     */   private Comparable<BigDecimal> getMaximumAsBig() {
/* 221 */     return this.maxAsBig;
/*     */   }
/*     */ }


/* Location:              E:\java\java学习\hutubill\lib\all.jar!\org\jdesktop\swingx\text\StrictNumberFormatter.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */
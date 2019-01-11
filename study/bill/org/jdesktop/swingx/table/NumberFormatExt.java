/*     */ package org.jdesktop.swingx.table;
/*     */ 
/*     */ import java.text.AttributedCharacterIterator;
/*     */ import java.text.AttributedString;
/*     */ import java.text.FieldPosition;
/*     */ import java.text.NumberFormat;
/*     */ import java.text.ParsePosition;
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
/*     */ class NumberFormatExt
/*     */   extends NumberFormat
/*     */ {
/*     */   private NumberFormat childFormat;
/*     */   
/*     */   public NumberFormatExt()
/*     */   {
/*  45 */     this(null);
/*     */   }
/*     */   
/*     */   public NumberFormatExt(NumberFormat childFormat) {
/*  49 */     if (childFormat == null) {
/*  50 */       childFormat = NumberFormat.getInstance();
/*     */     }
/*  52 */     this.childFormat = childFormat;
/*     */   }
/*     */   
/*     */   public AttributedCharacterIterator formatToCharacterIterator(Object obj)
/*     */   {
/*  57 */     if (obj == null)
/*  58 */       return new AttributedString("").getIterator();
/*  59 */     return this.childFormat.formatToCharacterIterator(obj);
/*     */   }
/*     */   
/*     */ 
/*     */   public StringBuffer format(Object obj, StringBuffer toAppendTo, FieldPosition pos)
/*     */   {
/*  65 */     if (obj == null)
/*  66 */       return new StringBuffer("");
/*  67 */     return this.childFormat.format(obj, toAppendTo, pos);
/*     */   }
/*     */   
/*     */   public Number parse(String source, ParsePosition pos)
/*     */   {
/*  72 */     if (source == null) {
/*  73 */       pos.setIndex(1);
/*  74 */       return null;
/*     */     }
/*  76 */     if (source.trim().equals("")) {
/*  77 */       pos.setIndex(1);
/*  78 */       return null;
/*     */     }
/*  80 */     Number val = this.childFormat.parse(source, pos);
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*  87 */     if (pos.getIndex() != source.length()) {
/*  88 */       pos.setErrorIndex(pos.getIndex());
/*  89 */       pos.setIndex(0);
/*     */     }
/*  91 */     return val;
/*     */   }
/*     */   
/*     */ 
/*     */   public StringBuffer format(double number, StringBuffer toAppendTo, FieldPosition pos)
/*     */   {
/*  97 */     return this.childFormat.format(number, toAppendTo, pos);
/*     */   }
/*     */   
/*     */ 
/*     */   public StringBuffer format(long number, StringBuffer toAppendTo, FieldPosition pos)
/*     */   {
/* 103 */     return this.childFormat.format(number, toAppendTo, pos);
/*     */   }
/*     */ }


/* Location:              E:\java\java学习\hutubill\lib\all.jar!\org\jdesktop\swingx\table\NumberFormatExt.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */
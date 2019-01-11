/*     */ package org.jdesktop.swingx.text;
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
/*     */ public class NumberFormatExt
/*     */   extends NumberFormat
/*     */ {
/*     */   private NumberFormat childFormat;
/*     */   
/*     */   public NumberFormatExt()
/*     */   {
/*  42 */     this(null);
/*     */   }
/*     */   
/*     */   public NumberFormatExt(NumberFormat childFormat) {
/*  46 */     if (childFormat == null) {
/*  47 */       childFormat = NumberFormat.getInstance();
/*     */     }
/*  49 */     this.childFormat = childFormat;
/*     */   }
/*     */   
/*     */   public AttributedCharacterIterator formatToCharacterIterator(Object obj)
/*     */   {
/*  54 */     if (obj == null)
/*  55 */       return new AttributedString("").getIterator();
/*  56 */     return this.childFormat.formatToCharacterIterator(obj);
/*     */   }
/*     */   
/*     */ 
/*     */   public StringBuffer format(Object obj, StringBuffer toAppendTo, FieldPosition pos)
/*     */   {
/*  62 */     if (obj == null)
/*  63 */       return new StringBuffer("");
/*  64 */     return this.childFormat.format(obj, toAppendTo, pos);
/*     */   }
/*     */   
/*     */   public Number parse(String source, ParsePosition pos)
/*     */   {
/*  69 */     if (source == null) {
/*  70 */       pos.setIndex(1);
/*  71 */       return null;
/*     */     }
/*  73 */     if (source.trim().equals("")) {
/*  74 */       pos.setIndex(1);
/*  75 */       return null;
/*     */     }
/*  77 */     Number val = this.childFormat.parse(source, pos);
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*  84 */     if (pos.getIndex() != source.length()) {
/*  85 */       pos.setErrorIndex(pos.getIndex());
/*  86 */       pos.setIndex(0);
/*     */     }
/*  88 */     return val;
/*     */   }
/*     */   
/*     */ 
/*     */   public StringBuffer format(double number, StringBuffer toAppendTo, FieldPosition pos)
/*     */   {
/*  94 */     return this.childFormat.format(number, toAppendTo, pos);
/*     */   }
/*     */   
/*     */ 
/*     */   public StringBuffer format(long number, StringBuffer toAppendTo, FieldPosition pos)
/*     */   {
/* 100 */     return this.childFormat.format(number, toAppendTo, pos);
/*     */   }
/*     */ }


/* Location:              E:\java\java学习\hutubill\lib\all.jar!\org\jdesktop\swingx\text\NumberFormatExt.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */
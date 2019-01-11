/*    */ package org.jdesktop.swingx.table;
/*    */ 
/*    */ import java.text.AttributedCharacterIterator;
/*    */ import java.text.AttributedString;
/*    */ import java.text.FieldPosition;
/*    */ import java.text.Format;
/*    */ import java.text.NumberFormat;
/*    */ import java.text.ParsePosition;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ @Deprecated
/*    */ class NumberEditorNumberFormat
/*    */   extends Format
/*    */ {
/*    */   private final NumberFormat childFormat;
/*    */   
/*    */   public NumberEditorNumberFormat(NumberFormat childFormat)
/*    */   {
/* 43 */     if (childFormat == null) {
/* 44 */       childFormat = NumberFormat.getInstance();
/*    */     }
/* 46 */     this.childFormat = childFormat;
/*    */   }
/*    */   
/*    */   public AttributedCharacterIterator formatToCharacterIterator(Object obj)
/*    */   {
/* 51 */     if (obj == null)
/* 52 */       return new AttributedString("").getIterator();
/* 53 */     return this.childFormat.formatToCharacterIterator(obj);
/*    */   }
/*    */   
/*    */ 
/*    */   public StringBuffer format(Object obj, StringBuffer toAppendTo, FieldPosition pos)
/*    */   {
/* 59 */     if (obj == null)
/* 60 */       return new StringBuffer("");
/* 61 */     return this.childFormat.format(obj, toAppendTo, pos);
/*    */   }
/*    */   
/*    */   public Object parseObject(String source, ParsePosition pos)
/*    */   {
/* 66 */     if (source == null) {
/* 67 */       pos.setIndex(1);
/* 68 */       return null;
/*    */     }
/* 70 */     if (source.trim().equals("")) {
/* 71 */       pos.setIndex(1);
/* 72 */       return null;
/*    */     }
/* 74 */     Object val = this.childFormat.parseObject(source, pos);
/*    */     
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/* 81 */     if (pos.getIndex() != source.length()) {
/* 82 */       pos.setErrorIndex(pos.getIndex());
/* 83 */       pos.setIndex(0);
/*    */     }
/* 85 */     return val;
/*    */   }
/*    */ }


/* Location:              E:\java\java学习\hutubill\lib\all.jar!\org\jdesktop\swingx\table\NumberEditorNumberFormat.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */
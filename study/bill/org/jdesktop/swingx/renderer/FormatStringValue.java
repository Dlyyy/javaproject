/*    */ package org.jdesktop.swingx.renderer;
/*    */ 
/*    */ import java.text.Format;
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
/*    */ public class FormatStringValue
/*    */   implements StringValue
/*    */ {
/*    */   protected Format format;
/*    */   
/*    */   public FormatStringValue()
/*    */   {
/* 57 */     this(null);
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   public FormatStringValue(Format format)
/*    */   {
/* 66 */     this.format = format;
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */   public Format getFormat()
/*    */   {
/* 74 */     return this.format;
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */   public String getString(Object value)
/*    */   {
/* 81 */     if (value == null) return "";
/* 82 */     if (this.format != null) {
/*    */       try {
/* 84 */         return this.format.format(value);
/*    */       }
/*    */       catch (IllegalArgumentException e) {}
/*    */     }
/*    */     
/* 89 */     return value.toString();
/*    */   }
/*    */ }


/* Location:              E:\java\java学习\hutubill\lib\all.jar!\org\jdesktop\swingx\renderer\FormatStringValue.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */
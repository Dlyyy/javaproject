/*    */ package org.jdesktop.swingx.renderer;
/*    */ 
/*    */ import javax.swing.Icon;
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
/*    */ public final class MappedValues
/*    */ {
/* 35 */   public static final MappedValue STRING_OR_ICON_ONLY = new MappedValue(new StringValue()
/*    */   {
/*    */     public String getString(Object value) {
/* 38 */       if ((value instanceof Icon)) {
/* 39 */         return StringValues.EMPTY.getString(value);
/*    */       }
/*    */       
/* 42 */       return StringValues.TO_STRING.getString(value);
/*    */     }
/* 35 */   }, IconValues.ICON);
/*    */ }


/* Location:              E:\java\java学习\hutubill\lib\all.jar!\org\jdesktop\swingx\renderer\MappedValues.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */
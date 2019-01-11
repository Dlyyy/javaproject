/*    */ package org.jdesktop.swingx.util;
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
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class Contract
/*    */ {
/*    */   public static <T> T asNotNull(T input, String message)
/*    */   {
/* 56 */     if (input == null) {
/* 57 */       throw new NullPointerException(message);
/*    */     }
/* 59 */     if ((input.getClass().isArray()) && 
/* 60 */       (!input.getClass().getComponentType().isPrimitive())) {
/* 61 */       T[] array = (Object[])input;
/* 62 */       for (int i = 0; i < array.length; i++) {
/* 63 */         asNotNull(array[i], message);
/*    */       }
/*    */     }
/*    */     
/*    */ 
/* 68 */     return input;
/*    */   }
/*    */ }


/* Location:              E:\java\java学习\hutubill\lib\all.jar!\org\jdesktop\swingx\util\Contract.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */
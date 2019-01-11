/*    */ package org.jdesktop.swingx.error;
/*    */ 
/*    */ import java.util.logging.Level;
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
/*    */ public class ErrorLevel
/*    */   extends Level
/*    */ {
/* 45 */   public static final ErrorLevel FATAL = new ErrorLevel("FATAL", 1100);
/*    */   
/*    */   protected ErrorLevel(String name, int value)
/*    */   {
/* 49 */     super(name, value);
/*    */   }
/*    */ }


/* Location:              E:\java\java学习\hutubill\lib\all.jar!\org\jdesktop\swingx\error\ErrorLevel.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */
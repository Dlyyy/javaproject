/*    */ package org.jdesktop.swingx.error;
/*    */ 
/*    */ import java.util.EventObject;
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
/*    */ public class ErrorEvent
/*    */   extends EventObject
/*    */ {
/*    */   private Throwable throwable;
/*    */   
/*    */   public ErrorEvent(Throwable throwable, Object source)
/*    */   {
/* 43 */     super(source);
/* 44 */     this.throwable = throwable;
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */   public Throwable getThrowable()
/*    */   {
/* 52 */     return this.throwable;
/*    */   }
/*    */ }


/* Location:              E:\java\java学习\hutubill\lib\all.jar!\org\jdesktop\swingx\error\ErrorEvent.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */
/*    */ package org.jdesktop.swingx.auth;
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
/*    */ public class LoginEvent
/*    */   extends EventObject
/*    */ {
/*    */   private Throwable cause;
/*    */   
/*    */   public LoginEvent(Object source)
/*    */   {
/* 33 */     this(source, null);
/*    */   }
/*    */   
/*    */   public LoginEvent(Object source, Throwable cause)
/*    */   {
/* 38 */     super(source);
/* 39 */     this.cause = cause;
/*    */   }
/*    */   
/*    */   public Throwable getCause() {
/* 43 */     return this.cause;
/*    */   }
/*    */ }


/* Location:              E:\java\java学习\hutubill\lib\all.jar!\org\jdesktop\swingx\auth\LoginEvent.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */
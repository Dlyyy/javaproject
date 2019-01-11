/*    */ package com.mysql.jdbc.jdbc2.optional;
/*    */ 
/*    */ import javax.transaction.xa.XAException;
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
/*    */ class MysqlXAException
/*    */   extends XAException
/*    */ {
/*    */   private static final long serialVersionUID = -9075817535836563004L;
/*    */   private String message;
/*    */   private String xidAsString;
/*    */   
/*    */   public MysqlXAException(int errorCode, String message, String xidAsString)
/*    */   {
/* 39 */     super(errorCode);
/* 40 */     this.message = message;
/* 41 */     this.xidAsString = xidAsString;
/*    */   }
/*    */   
/*    */ 
/*    */   public MysqlXAException(String message, String xidAsString)
/*    */   {
/* 47 */     this.message = message;
/* 48 */     this.xidAsString = xidAsString;
/*    */   }
/*    */   
/*    */   public String getMessage() {
/* 52 */     String superMessage = super.getMessage();
/* 53 */     StringBuffer returnedMessage = new StringBuffer();
/*    */     
/* 55 */     if (superMessage != null) {
/* 56 */       returnedMessage.append(superMessage);
/* 57 */       returnedMessage.append(":");
/*    */     }
/*    */     
/* 60 */     if (this.message != null) {
/* 61 */       returnedMessage.append(this.message);
/*    */     }
/*    */     
/* 64 */     return returnedMessage.toString();
/*    */   }
/*    */ }


/* Location:              E:\java\java学习\hutubill\lib\all.jar!\com\mysql\jdbc\jdbc2\optional\MysqlXAException.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version:       0.7.1
 */
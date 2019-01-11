/*    */ package com.mysql.jdbc.exceptions;
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
/*    */ public class MySQLIntegrityConstraintViolationException
/*    */   extends MySQLNonTransientException
/*    */ {
/*    */   public MySQLIntegrityConstraintViolationException() {}
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
/*    */   public MySQLIntegrityConstraintViolationException(String reason, String SQLState, int vendorCode)
/*    */   {
/* 34 */     super(reason, SQLState, vendorCode);
/*    */   }
/*    */   
/*    */   public MySQLIntegrityConstraintViolationException(String reason, String SQLState) {
/* 38 */     super(reason, SQLState);
/*    */   }
/*    */   
/*    */   public MySQLIntegrityConstraintViolationException(String reason) {
/* 42 */     super(reason);
/*    */   }
/*    */ }


/* Location:              E:\java\java学习\hutubill\lib\all.jar!\com\mysql\jdbc\exceptions\MySQLIntegrityConstraintViolationException.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version:       0.7.1
 */
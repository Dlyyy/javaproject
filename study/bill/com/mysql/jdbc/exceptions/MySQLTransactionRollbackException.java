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
/*    */ public class MySQLTransactionRollbackException
/*    */   extends MySQLTransientException
/*    */ {
/*    */   public MySQLTransactionRollbackException(String reason, String SQLState, int vendorCode)
/*    */   {
/* 30 */     super(reason, SQLState, vendorCode);
/*    */   }
/*    */   
/*    */   public MySQLTransactionRollbackException(String reason, String SQLState) {
/* 34 */     super(reason, SQLState);
/*    */   }
/*    */   
/*    */   public MySQLTransactionRollbackException(String reason) {
/* 38 */     super(reason);
/*    */   }
/*    */   
/*    */   public MySQLTransactionRollbackException() {}
/*    */ }


/* Location:              E:\java\java学习\hutubill\lib\all.jar!\com\mysql\jdbc\exceptions\MySQLTransactionRollbackException.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version:       0.7.1
 */
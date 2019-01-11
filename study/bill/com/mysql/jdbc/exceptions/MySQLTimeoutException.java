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
/*    */ public class MySQLTimeoutException
/*    */   extends MySQLTransientException
/*    */ {
/*    */   public MySQLTimeoutException(String reason, String SQLState, int vendorCode)
/*    */   {
/* 29 */     super(reason, SQLState, vendorCode);
/*    */   }
/*    */   
/*    */   public MySQLTimeoutException(String reason, String SQLState) {
/* 33 */     super(reason, SQLState);
/*    */   }
/*    */   
/*    */   public MySQLTimeoutException(String reason) {
/* 37 */     super(reason);
/*    */   }
/*    */   
/*    */   public MySQLTimeoutException() {
/* 41 */     super("Statement cancelled due to timeout or client request");
/*    */   }
/*    */   
/*    */   public int getErrorCode()
/*    */   {
/* 46 */     return super.getErrorCode();
/*    */   }
/*    */ }


/* Location:              E:\java\java学习\hutubill\lib\all.jar!\com\mysql\jdbc\exceptions\MySQLTimeoutException.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version:       0.7.1
 */
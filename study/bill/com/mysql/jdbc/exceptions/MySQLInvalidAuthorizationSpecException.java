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
/*    */ public class MySQLInvalidAuthorizationSpecException
/*    */   extends MySQLNonTransientException
/*    */ {
/*    */   public MySQLInvalidAuthorizationSpecException() {}
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
/*    */   public MySQLInvalidAuthorizationSpecException(String reason, String SQLState, int vendorCode)
/*    */   {
/* 34 */     super(reason, SQLState, vendorCode);
/*    */   }
/*    */   
/*    */   public MySQLInvalidAuthorizationSpecException(String reason, String SQLState) {
/* 38 */     super(reason, SQLState);
/*    */   }
/*    */   
/*    */   public MySQLInvalidAuthorizationSpecException(String reason) {
/* 42 */     super(reason);
/*    */   }
/*    */ }


/* Location:              E:\java\java学习\hutubill\lib\all.jar!\com\mysql\jdbc\exceptions\MySQLInvalidAuthorizationSpecException.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version:       0.7.1
 */
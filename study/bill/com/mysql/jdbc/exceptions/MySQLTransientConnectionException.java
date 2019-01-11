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
/*    */ public class MySQLTransientConnectionException
/*    */   extends MySQLTransientException
/*    */ {
/*    */   public MySQLTransientConnectionException(String reason, String SQLState, int vendorCode)
/*    */   {
/* 30 */     super(reason, SQLState, vendorCode);
/*    */   }
/*    */   
/*    */   public MySQLTransientConnectionException(String reason, String SQLState) {
/* 34 */     super(reason, SQLState);
/*    */   }
/*    */   
/*    */   public MySQLTransientConnectionException(String reason) {
/* 38 */     super(reason);
/*    */   }
/*    */   
/*    */   public MySQLTransientConnectionException() {}
/*    */ }


/* Location:              E:\java\java学习\hutubill\lib\all.jar!\com\mysql\jdbc\exceptions\MySQLTransientConnectionException.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version:       0.7.1
 */
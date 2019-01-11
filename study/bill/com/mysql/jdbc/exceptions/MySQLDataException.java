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
/*    */ public class MySQLDataException
/*    */   extends MySQLNonTransientException
/*    */ {
/*    */   public MySQLDataException() {}
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
/*    */   public MySQLDataException(String reason, String SQLState, int vendorCode)
/*    */   {
/* 33 */     super(reason, SQLState, vendorCode);
/*    */   }
/*    */   
/*    */   public MySQLDataException(String reason, String SQLState) {
/* 37 */     super(reason, SQLState);
/*    */   }
/*    */   
/*    */   public MySQLDataException(String reason) {
/* 41 */     super(reason);
/*    */   }
/*    */ }


/* Location:              E:\java\java学习\hutubill\lib\all.jar!\com\mysql\jdbc\exceptions\MySQLDataException.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version:       0.7.1
 */
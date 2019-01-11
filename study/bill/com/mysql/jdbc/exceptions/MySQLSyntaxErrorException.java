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
/*    */ public class MySQLSyntaxErrorException
/*    */   extends MySQLNonTransientException
/*    */ {
/*    */   public MySQLSyntaxErrorException() {}
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
/*    */   public MySQLSyntaxErrorException(String reason, String SQLState, int vendorCode)
/*    */   {
/* 33 */     super(reason, SQLState, vendorCode);
/*    */   }
/*    */   
/*    */   public MySQLSyntaxErrorException(String reason, String SQLState) {
/* 37 */     super(reason, SQLState);
/*    */   }
/*    */   
/*    */   public MySQLSyntaxErrorException(String reason) {
/* 41 */     super(reason);
/*    */   }
/*    */ }


/* Location:              E:\java\java学习\hutubill\lib\all.jar!\com\mysql\jdbc\exceptions\MySQLSyntaxErrorException.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version:       0.7.1
 */
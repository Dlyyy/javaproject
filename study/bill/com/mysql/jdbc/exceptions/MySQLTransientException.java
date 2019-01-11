/*    */ package com.mysql.jdbc.exceptions;
/*    */ 
/*    */ import java.sql.SQLException;
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
/*    */ public class MySQLTransientException
/*    */   extends SQLException
/*    */ {
/*    */   public MySQLTransientException(String reason, String SQLState, int vendorCode)
/*    */   {
/* 31 */     super(reason, SQLState, vendorCode);
/*    */   }
/*    */   
/*    */   public MySQLTransientException(String reason, String SQLState) {
/* 35 */     super(reason, SQLState);
/*    */   }
/*    */   
/*    */   public MySQLTransientException(String reason) {
/* 39 */     super(reason);
/*    */   }
/*    */   
/*    */   public MySQLTransientException() {}
/*    */ }


/* Location:              E:\java\java学习\hutubill\lib\all.jar!\com\mysql\jdbc\exceptions\MySQLTransientException.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version:       0.7.1
 */
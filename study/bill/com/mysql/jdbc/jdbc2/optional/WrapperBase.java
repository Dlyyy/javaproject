/*    */ package com.mysql.jdbc.jdbc2.optional;
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
/*    */ abstract class WrapperBase
/*    */ {
/*    */   protected MysqlPooledConnection pooledConnection;
/*    */   
/*    */   protected void checkAndFireConnectionError(SQLException sqlEx)
/*    */     throws SQLException
/*    */   {
/* 51 */     if ((this.pooledConnection != null) && 
/* 52 */       ("08S01".equals(sqlEx.getSQLState())))
/*    */     {
/* 54 */       this.pooledConnection.callListener(1, sqlEx);
/*    */     }
/*    */     
/*    */ 
/*    */ 
/* 59 */     throw sqlEx;
/*    */   }
/*    */ }


/* Location:              E:\java\java学习\hutubill\lib\all.jar!\com\mysql\jdbc\jdbc2\optional\WrapperBase.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version:       0.7.1
 */
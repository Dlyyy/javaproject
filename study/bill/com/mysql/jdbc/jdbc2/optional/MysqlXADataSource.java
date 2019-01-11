/*    */ package com.mysql.jdbc.jdbc2.optional;
/*    */ 
/*    */ import java.sql.SQLException;
/*    */ import javax.sql.XAConnection;
/*    */ import javax.sql.XADataSource;
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
/*    */ public class MysqlXADataSource
/*    */   extends MysqlDataSource
/*    */   implements XADataSource
/*    */ {
/*    */   public XAConnection getXAConnection()
/*    */     throws SQLException
/*    */   {
/* 47 */     java.sql.Connection conn = getConnection();
/*    */     
/* 49 */     return wrapConnection(conn);
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */   public XAConnection getXAConnection(String user, String password)
/*    */     throws SQLException
/*    */   {
/* 58 */     java.sql.Connection conn = getConnection(user, password);
/*    */     
/* 60 */     return wrapConnection(conn);
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */   private XAConnection wrapConnection(java.sql.Connection conn)
/*    */     throws SQLException
/*    */   {
/* 68 */     if ((getPinGlobalTxToPhysicalConnection()) || (((com.mysql.jdbc.Connection)conn).getPinGlobalTxToPhysicalConnection()))
/*    */     {
/* 70 */       return new SuspendableXAConnection((com.mysql.jdbc.Connection)conn);
/*    */     }
/*    */     
/* 73 */     return new MysqlXAConnection((com.mysql.jdbc.Connection)conn, getLogXaCommands());
/*    */   }
/*    */ }


/* Location:              E:\java\java学习\hutubill\lib\all.jar!\com\mysql\jdbc\jdbc2\optional\MysqlXADataSource.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version:       0.7.1
 */
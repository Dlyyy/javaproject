/*    */ package com.mysql.jdbc.jdbc2.optional;
/*    */ 
/*    */ import java.sql.SQLException;
/*    */ import javax.sql.ConnectionPoolDataSource;
/*    */ import javax.sql.PooledConnection;
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
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class MysqlConnectionPoolDataSource
/*    */   extends MysqlDataSource
/*    */   implements ConnectionPoolDataSource
/*    */ {
/*    */   public synchronized PooledConnection getPooledConnection()
/*    */     throws SQLException
/*    */   {
/* 58 */     java.sql.Connection connection = getConnection();
/* 59 */     MysqlPooledConnection mysqlPooledConnection = new MysqlPooledConnection((com.mysql.jdbc.Connection)connection);
/*    */     
/*    */ 
/* 62 */     return mysqlPooledConnection;
/*    */   }
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
/*    */   public synchronized PooledConnection getPooledConnection(String s, String s1)
/*    */     throws SQLException
/*    */   {
/* 79 */     java.sql.Connection connection = getConnection(s, s1);
/* 80 */     MysqlPooledConnection mysqlPooledConnection = new MysqlPooledConnection((com.mysql.jdbc.Connection)connection);
/*    */     
/*    */ 
/* 83 */     return mysqlPooledConnection;
/*    */   }
/*    */ }


/* Location:              E:\java\java学习\hutubill\lib\all.jar!\com\mysql\jdbc\jdbc2\optional\MysqlConnectionPoolDataSource.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version:       0.7.1
 */
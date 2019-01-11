/*    */ package com.mysql.jdbc;
/*    */ 
/*    */ import java.sql.Driver;
/*    */ import java.sql.DriverManager;
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
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class ReplicationDriver
/*    */   extends NonRegisteringReplicationDriver
/*    */   implements Driver
/*    */ {
/*    */   public ReplicationDriver()
/*    */     throws SQLException
/*    */   {}
/*    */   
/*    */   static
/*    */   {
/*    */     try
/*    */     {
/* 64 */       DriverManager.registerDriver(new NonRegisteringReplicationDriver());
/*    */     }
/*    */     catch (SQLException E) {
/* 67 */       throw new RuntimeException("Can't register driver!");
/*    */     }
/*    */   }
/*    */ }


/* Location:              E:\java\java学习\hutubill\lib\all.jar!\com\mysql\jdbc\ReplicationDriver.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version:       0.7.1
 */
/*    */ package com.mysql.jdbc;
/*    */ 
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
/*    */ public class Driver
/*    */   extends NonRegisteringDriver
/*    */   implements java.sql.Driver
/*    */ {
/*    */   public Driver()
/*    */     throws SQLException
/*    */   {}
/*    */   
/*    */   static
/*    */   {
/*    */     try
/*    */     {
/* 62 */       DriverManager.registerDriver(new Driver());
/*    */     } catch (SQLException E) {
/* 64 */       throw new RuntimeException("Can't register driver!");
/*    */     }
/*    */   }
/*    */ }


/* Location:              E:\java\java学习\hutubill\lib\all.jar!\com\mysql\jdbc\Driver.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version:       0.7.1
 */
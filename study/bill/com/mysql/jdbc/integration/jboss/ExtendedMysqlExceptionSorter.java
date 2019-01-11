/*    */ package com.mysql.jdbc.integration.jboss;
/*    */ 
/*    */ import java.sql.SQLException;
/*    */ import org.jboss.resource.adapter.jdbc.vendor.MySQLExceptionSorter;
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
/*    */ public final class ExtendedMysqlExceptionSorter
/*    */   extends MySQLExceptionSorter
/*    */ {
/*    */   public boolean isExceptionFatal(SQLException ex)
/*    */   {
/* 45 */     String sqlState = ex.getSQLState();
/*    */     
/* 47 */     if ((sqlState != null) && (sqlState.startsWith("08"))) {
/* 48 */       return true;
/*    */     }
/*    */     
/* 51 */     return super.isExceptionFatal(ex);
/*    */   }
/*    */ }


/* Location:              E:\java\java学习\hutubill\lib\all.jar!\com\mysql\jdbc\integration\jboss\ExtendedMysqlExceptionSorter.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version:       0.7.1
 */
/*     */ package com.mysql.jdbc.integration.c3p0;
/*     */ 
/*     */ import com.mchange.v2.c3p0.C3P0ProxyConnection;
/*     */ import com.mchange.v2.c3p0.QueryConnectionTester;
/*     */ import com.mysql.jdbc.CommunicationsException;
/*     */ import java.lang.reflect.Method;
/*     */ import java.sql.ResultSet;
/*     */ import java.sql.SQLException;
/*     */ import java.sql.Statement;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public final class MysqlConnectionTester
/*     */   implements QueryConnectionTester
/*     */ {
/*     */   private static final long serialVersionUID = 3256444690067896368L;
/*  47 */   private static final Object[] NO_ARGS_ARRAY = new Object[0];
/*     */   private Method pingMethod;
/*     */   
/*     */   public MysqlConnectionTester()
/*     */   {
/*     */     try {
/*  53 */       this.pingMethod = com.mysql.jdbc.Connection.class.getMethod("ping", null);
/*     */     }
/*     */     catch (Exception ex) {}
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public int activeCheckConnection(java.sql.Connection con)
/*     */   {
/*     */     try
/*     */     {
/*  69 */       if (this.pingMethod != null) {
/*  70 */         if ((con instanceof com.mysql.jdbc.Connection))
/*     */         {
/*     */ 
/*  73 */           ((com.mysql.jdbc.Connection)con).ping();
/*     */         }
/*     */         else {
/*  76 */           C3P0ProxyConnection castCon = (C3P0ProxyConnection)con;
/*  77 */           castCon.rawConnectionOperation(this.pingMethod, C3P0ProxyConnection.RAW_CONNECTION, NO_ARGS_ARRAY);
/*     */         }
/*     */       }
/*     */       else {
/*  81 */         Statement pingStatement = null;
/*     */         try
/*     */         {
/*  84 */           pingStatement = con.createStatement();
/*  85 */           pingStatement.executeQuery("SELECT 1").close();
/*     */         } finally {
/*  87 */           if (pingStatement != null) {
/*  88 */             pingStatement.close();
/*     */           }
/*     */         }
/*     */       }
/*     */       
/*  93 */       return 0;
/*     */     } catch (Exception ex) {}
/*  95 */     return -1;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public int statusOnException(java.sql.Connection arg0, Throwable throwable)
/*     */   {
/* 106 */     if ((throwable instanceof CommunicationsException)) {
/* 107 */       return -1;
/*     */     }
/*     */     
/* 110 */     if ((throwable instanceof SQLException)) {
/* 111 */       String sqlState = ((SQLException)throwable).getSQLState();
/*     */       
/* 113 */       if ((sqlState != null) && (sqlState.startsWith("08"))) {
/* 114 */         return -1;
/*     */       }
/*     */       
/* 117 */       return 0;
/*     */     }
/*     */     
/*     */ 
/*     */ 
/* 122 */     return -1;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public int activeCheckConnection(java.sql.Connection arg0, String arg1)
/*     */   {
/* 132 */     return 0;
/*     */   }
/*     */ }


/* Location:              E:\java\java学习\hutubill\lib\all.jar!\com\mysql\jdbc\integration\c3p0\MysqlConnectionTester.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version:       0.7.1
 */
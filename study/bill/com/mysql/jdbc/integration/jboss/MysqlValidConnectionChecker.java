/*     */ package com.mysql.jdbc.integration.jboss;
/*     */ 
/*     */ import com.mysql.jdbc.SQLError;
/*     */ import com.mysql.jdbc.jdbc2.optional.ConnectionWrapper;
/*     */ import java.io.Serializable;
/*     */ import java.lang.reflect.Method;
/*     */ import java.sql.ResultSet;
/*     */ import java.sql.SQLException;
/*     */ import java.sql.Statement;
/*     */ import org.jboss.resource.adapter.jdbc.ValidConnectionChecker;
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
/*     */ public final class MysqlValidConnectionChecker
/*     */   implements ValidConnectionChecker, Serializable
/*     */ {
/*     */   private static final long serialVersionUID = 3258689922776119348L;
/*     */   private Method pingMethod;
/*     */   private Method pingMethodWrapped;
/*  50 */   private static final Object[] NO_ARGS_OBJECT_ARRAY = new Object[0];
/*     */   
/*     */   public MysqlValidConnectionChecker()
/*     */   {
/*     */     try {
/*  55 */       Class mysqlConnection = Thread.currentThread().getContextClassLoader().loadClass("com.mysql.jdbc.Connection");
/*     */       
/*     */ 
/*     */ 
/*  59 */       this.pingMethod = mysqlConnection.getMethod("ping", null);
/*     */       
/*  61 */       Class mysqlConnectionWrapper = Thread.currentThread().getContextClassLoader().loadClass("com.mysql.jdbc.jdbc2.optional.ConnectionWrapper");
/*     */       
/*     */ 
/*     */ 
/*  65 */       this.pingMethodWrapped = mysqlConnectionWrapper.getMethod("ping", null);
/*     */     }
/*     */     catch (Exception ex) {}
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public SQLException isValidConnection(java.sql.Connection conn)
/*     */   {
/*  77 */     if ((conn instanceof com.mysql.jdbc.Connection)) {
/*  78 */       if (this.pingMethod == null)
/*     */         break label130;
/*  80 */       try { this.pingMethod.invoke(conn, null);
/*     */         
/*  82 */         return null;
/*     */       } catch (Exception ex) {
/*  84 */         if ((ex instanceof SQLException)) {
/*  85 */           return (SQLException)ex;
/*     */         }
/*     */         
/*  88 */         return SQLError.createSQLException("Ping failed: " + ex.toString());
/*     */       }
/*     */     }
/*  91 */     if (((conn instanceof ConnectionWrapper)) && 
/*  92 */       (this.pingMethodWrapped != null)) {
/*     */       try {
/*  94 */         this.pingMethodWrapped.invoke(conn, null);
/*     */         
/*  96 */         return null;
/*     */       } catch (Exception ex) {
/*  98 */         if ((ex instanceof SQLException)) {
/*  99 */           return (SQLException)ex;
/*     */         }
/*     */         
/* 102 */         return SQLError.createSQLException("Ping failed: " + ex.toString());
/*     */       }
/*     */     }
/*     */     
/*     */ 
/*     */     label130:
/*     */     
/* 109 */     Statement pingStatement = null;
/*     */     try
/*     */     {
/* 112 */       pingStatement = conn.createStatement();
/*     */       
/* 114 */       pingStatement.executeQuery("SELECT 1").close();
/*     */       
/* 116 */       return null;
/*     */     } catch (SQLException sqlEx) {
/* 118 */       return sqlEx;
/*     */     } finally {
/* 120 */       if (pingStatement != null) {
/*     */         try {
/* 122 */           pingStatement.close();
/*     */         }
/*     */         catch (SQLException sqlEx) {}
/*     */       }
/*     */     }
/*     */   }
/*     */ }


/* Location:              E:\java\java学习\hutubill\lib\all.jar!\com\mysql\jdbc\integration\jboss\MysqlValidConnectionChecker.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version:       0.7.1
 */
/*     */ package com.mysql.jdbc;
/*     */ 
/*     */ import java.sql.ParameterMetaData;
/*     */ import java.sql.SQLException;
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
/*     */ public class MysqlParameterMetadata
/*     */   implements ParameterMetaData
/*     */ {
/*  31 */   boolean returnSimpleMetadata = false;
/*  32 */   ResultSetMetaData metadata = null;
/*  33 */   int parameterCount = 0;
/*     */   
/*     */   MysqlParameterMetadata(Field[] fieldInfo, int parameterCount)
/*     */   {
/*  37 */     this.metadata = new ResultSetMetaData(fieldInfo, false);
/*     */     
/*  39 */     this.parameterCount = parameterCount;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   MysqlParameterMetadata(int count)
/*     */   {
/*  49 */     this.parameterCount = count;
/*  50 */     this.returnSimpleMetadata = true;
/*     */   }
/*     */   
/*     */   public int getParameterCount() throws SQLException {
/*  54 */     return this.parameterCount;
/*     */   }
/*     */   
/*     */   public int isNullable(int arg0) throws SQLException {
/*  58 */     checkAvailable();
/*     */     
/*  60 */     return this.metadata.isNullable(arg0);
/*     */   }
/*     */   
/*     */   private void checkAvailable() throws SQLException {
/*  64 */     if ((this.metadata == null) || (this.metadata.fields == null)) {
/*  65 */       throw SQLError.createSQLException("Parameter metadata not available for the given statement", "S1C00");
/*     */     }
/*     */   }
/*     */   
/*     */   public boolean isSigned(int arg0)
/*     */     throws SQLException
/*     */   {
/*  72 */     if (this.returnSimpleMetadata) {
/*  73 */       checkBounds(arg0);
/*     */       
/*  75 */       return false;
/*     */     }
/*     */     
/*  78 */     checkAvailable();
/*     */     
/*  80 */     return this.metadata.isSigned(arg0);
/*     */   }
/*     */   
/*     */   public int getPrecision(int arg0) throws SQLException {
/*  84 */     if (this.returnSimpleMetadata) {
/*  85 */       checkBounds(arg0);
/*     */       
/*  87 */       return 0;
/*     */     }
/*     */     
/*  90 */     checkAvailable();
/*     */     
/*  92 */     return this.metadata.getPrecision(arg0);
/*     */   }
/*     */   
/*     */   public int getScale(int arg0) throws SQLException {
/*  96 */     if (this.returnSimpleMetadata) {
/*  97 */       checkBounds(arg0);
/*     */       
/*  99 */       return 0;
/*     */     }
/*     */     
/* 102 */     checkAvailable();
/*     */     
/* 104 */     return this.metadata.getScale(arg0);
/*     */   }
/*     */   
/*     */   public int getParameterType(int arg0) throws SQLException {
/* 108 */     if (this.returnSimpleMetadata) {
/* 109 */       checkBounds(arg0);
/*     */       
/* 111 */       return 12;
/*     */     }
/*     */     
/* 114 */     checkAvailable();
/*     */     
/* 116 */     return this.metadata.getColumnType(arg0);
/*     */   }
/*     */   
/*     */   public String getParameterTypeName(int arg0) throws SQLException {
/* 120 */     if (this.returnSimpleMetadata) {
/* 121 */       checkBounds(arg0);
/*     */       
/* 123 */       return "VARCHAR";
/*     */     }
/*     */     
/* 126 */     checkAvailable();
/*     */     
/* 128 */     return this.metadata.getColumnTypeName(arg0);
/*     */   }
/*     */   
/*     */   public String getParameterClassName(int arg0) throws SQLException {
/* 132 */     if (this.returnSimpleMetadata) {
/* 133 */       checkBounds(arg0);
/*     */       
/* 135 */       return "java.lang.String";
/*     */     }
/*     */     
/* 138 */     checkAvailable();
/*     */     
/* 140 */     return this.metadata.getColumnClassName(arg0);
/*     */   }
/*     */   
/*     */   public int getParameterMode(int arg0) throws SQLException {
/* 144 */     return 1;
/*     */   }
/*     */   
/*     */   private void checkBounds(int paramNumber) throws SQLException {
/* 148 */     if (paramNumber < 1) {
/* 149 */       throw SQLError.createSQLException("Parameter index of '" + paramNumber + "' is invalid.", "S1009");
/*     */     }
/*     */     
/*     */ 
/*     */ 
/* 154 */     if (paramNumber > this.parameterCount) {
/* 155 */       throw SQLError.createSQLException("Parameter index of '" + paramNumber + "' is greater than number of parameters, which is '" + this.parameterCount + "'.", "S1009");
/*     */     }
/*     */   }
/*     */ }


/* Location:              E:\java\java学习\hutubill\lib\all.jar!\com\mysql\jdbc\MysqlParameterMetadata.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version:       0.7.1
 */
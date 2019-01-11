/*     */ package com.mysql.jdbc.jdbc2.optional;
/*     */ 
/*     */ import java.util.Hashtable;
/*     */ import javax.naming.Context;
/*     */ import javax.naming.Name;
/*     */ import javax.naming.RefAddr;
/*     */ import javax.naming.Reference;
/*     */ import javax.naming.spi.ObjectFactory;
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
/*     */ public class MysqlDataSourceFactory
/*     */   implements ObjectFactory
/*     */ {
/*     */   protected static final String DATA_SOURCE_CLASS_NAME = "com.mysql.jdbc.jdbc2.optional.MysqlDataSource";
/*     */   protected static final String POOL_DATA_SOURCE_CLASS_NAME = "com.mysql.jdbc.jdbc2.optional.MysqlConnectionPoolDataSource";
/*     */   protected static final String XA_DATA_SOURCE_CLASS_NAME = "com.mysql.jdbc.jdbc2.optional.MysqlXADataSource";
/*     */   
/*     */   public Object getObjectInstance(Object refObj, Name nm, Context ctx, Hashtable env)
/*     */     throws Exception
/*     */   {
/*  76 */     Reference ref = (Reference)refObj;
/*  77 */     String className = ref.getClassName();
/*     */     
/*  79 */     if ((className != null) && ((className.equals("com.mysql.jdbc.jdbc2.optional.MysqlDataSource")) || (className.equals("com.mysql.jdbc.jdbc2.optional.MysqlConnectionPoolDataSource")) || (className.equals("com.mysql.jdbc.jdbc2.optional.MysqlXADataSource"))))
/*     */     {
/*     */ 
/*     */ 
/*  83 */       MysqlDataSource dataSource = null;
/*     */       try
/*     */       {
/*  86 */         dataSource = (MysqlDataSource)Class.forName(className).newInstance();
/*     */       }
/*     */       catch (Exception ex) {
/*  89 */         throw new RuntimeException("Unable to create DataSource of class '" + className + "', reason: " + ex.toString());
/*     */       }
/*     */       
/*     */ 
/*  93 */       int portNumber = 3306;
/*     */       
/*  95 */       String portNumberAsString = nullSafeRefAddrStringGet("port", ref);
/*     */       
/*  97 */       if (portNumberAsString != null) {
/*  98 */         portNumber = Integer.parseInt(portNumberAsString);
/*     */       }
/*     */       
/* 101 */       dataSource.setPort(portNumber);
/*     */       
/* 103 */       String user = nullSafeRefAddrStringGet("user", ref);
/*     */       
/* 105 */       if (user != null) {
/* 106 */         dataSource.setUser(user);
/*     */       }
/*     */       
/* 109 */       String password = nullSafeRefAddrStringGet("password", ref);
/*     */       
/* 111 */       if (password != null) {
/* 112 */         dataSource.setPassword(password);
/*     */       }
/*     */       
/* 115 */       String serverName = nullSafeRefAddrStringGet("serverName", ref);
/*     */       
/* 117 */       if (serverName != null) {
/* 118 */         dataSource.setServerName(serverName);
/*     */       }
/*     */       
/* 121 */       String databaseName = nullSafeRefAddrStringGet("databaseName", ref);
/*     */       
/* 123 */       if (databaseName != null) {
/* 124 */         dataSource.setDatabaseName(databaseName);
/*     */       }
/*     */       
/* 127 */       String explicitUrlAsString = nullSafeRefAddrStringGet("explicitUrl", ref);
/*     */       
/* 129 */       if ((explicitUrlAsString != null) && 
/* 130 */         (Boolean.valueOf(explicitUrlAsString).booleanValue())) {
/* 131 */         dataSource.setUrl(nullSafeRefAddrStringGet("url", ref));
/*     */       }
/*     */       
/*     */ 
/* 135 */       dataSource.setPropertiesViaRef(ref);
/*     */       
/* 137 */       return dataSource;
/*     */     }
/*     */     
/*     */ 
/* 141 */     return null;
/*     */   }
/*     */   
/*     */   private String nullSafeRefAddrStringGet(String referenceName, Reference ref) {
/* 145 */     RefAddr refAddr = ref.get(referenceName);
/*     */     
/* 147 */     String asString = refAddr != null ? (String)refAddr.getContent() : null;
/*     */     
/* 149 */     return asString;
/*     */   }
/*     */ }


/* Location:              E:\java\java学习\hutubill\lib\all.jar!\com\mysql\jdbc\jdbc2\optional\MysqlDataSourceFactory.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version:       0.7.1
 */
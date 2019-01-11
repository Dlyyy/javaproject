/*     */ package com.mysql.jdbc;
/*     */ 
/*     */ import java.sql.Connection;
/*     */ import java.sql.SQLException;
/*     */ import java.util.Properties;
/*     */ import java.util.StringTokenizer;
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
/*     */ public class NonRegisteringReplicationDriver
/*     */   extends NonRegisteringDriver
/*     */ {
/*     */   public NonRegisteringReplicationDriver()
/*     */     throws SQLException
/*     */   {}
/*     */   
/*     */   public Connection connect(String url, Properties info)
/*     */     throws SQLException
/*     */   {
/*  51 */     Properties parsedProps = parseURL(url, info);
/*     */     
/*  53 */     if (parsedProps == null) {
/*  54 */       return null;
/*     */     }
/*     */     
/*  57 */     Properties masterProps = (Properties)parsedProps.clone();
/*  58 */     Properties slavesProps = (Properties)parsedProps.clone();
/*     */     
/*     */ 
/*     */ 
/*  62 */     slavesProps.setProperty("com.mysql.jdbc.ReplicationConnection.isSlave", "true");
/*     */     
/*  64 */     String hostValues = parsedProps.getProperty("HOST");
/*     */     
/*  66 */     if (hostValues != null) {
/*  67 */       StringTokenizer st = new StringTokenizer(hostValues, ",");
/*     */       
/*  69 */       StringBuffer masterHost = new StringBuffer();
/*  70 */       StringBuffer slaveHosts = new StringBuffer();
/*     */       
/*  72 */       if (st.hasMoreTokens()) {
/*  73 */         String[] hostPortPair = parseHostPortPair(st.nextToken());
/*     */         
/*  75 */         if (hostPortPair[0] != null) {
/*  76 */           masterHost.append(hostPortPair[0]);
/*     */         }
/*     */         
/*  79 */         if (hostPortPair[1] != null) {
/*  80 */           masterHost.append(":");
/*  81 */           masterHost.append(hostPortPair[1]);
/*     */         }
/*     */       }
/*     */       
/*  85 */       boolean firstSlaveHost = true;
/*     */       
/*  87 */       while (st.hasMoreTokens()) {
/*  88 */         String[] hostPortPair = parseHostPortPair(st.nextToken());
/*     */         
/*  90 */         if (!firstSlaveHost) {
/*  91 */           slaveHosts.append(",");
/*     */         } else {
/*  93 */           firstSlaveHost = false;
/*     */         }
/*     */         
/*  96 */         if (hostPortPair[0] != null) {
/*  97 */           slaveHosts.append(hostPortPair[0]);
/*     */         }
/*     */         
/* 100 */         if (hostPortPair[1] != null) {
/* 101 */           slaveHosts.append(":");
/* 102 */           slaveHosts.append(hostPortPair[1]);
/*     */         }
/*     */       }
/*     */       
/* 106 */       if (slaveHosts.length() == 0) {
/* 107 */         throw SQLError.createSQLException("Must specify at least one slave host to connect to for master/slave replication load-balancing functionality", "01S00");
/*     */       }
/*     */       
/*     */ 
/*     */ 
/* 112 */       masterProps.setProperty("HOST", masterHost.toString());
/* 113 */       slavesProps.setProperty("HOST", slaveHosts.toString());
/*     */     }
/*     */     
/* 116 */     return new ReplicationConnection(masterProps, slavesProps);
/*     */   }
/*     */ }


/* Location:              E:\java\java学习\hutubill\lib\all.jar!\com\mysql\jdbc\NonRegisteringReplicationDriver.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version:       0.7.1
 */
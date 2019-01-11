/*     */ package com.mysql.jdbc;
/*     */ 
/*     */ import java.sql.CallableStatement;
/*     */ import java.sql.DatabaseMetaData;
/*     */ import java.sql.PreparedStatement;
/*     */ import java.sql.SQLException;
/*     */ import java.sql.SQLWarning;
/*     */ import java.sql.Savepoint;
/*     */ import java.util.Map;
/*     */ import java.util.Properties;
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
/*     */ public class ReplicationConnection
/*     */   implements java.sql.Connection, PingTarget
/*     */ {
/*     */   private Connection currentConnection;
/*     */   private Connection masterConnection;
/*     */   private Connection slavesConnection;
/*     */   
/*     */   public ReplicationConnection(Properties masterProperties, Properties slaveProperties)
/*     */     throws SQLException
/*     */   {
/*  51 */     Driver driver = new Driver();
/*     */     
/*  53 */     StringBuffer masterUrl = new StringBuffer("jdbc:mysql://");
/*  54 */     StringBuffer slaveUrl = new StringBuffer("jdbc:mysql://");
/*     */     
/*  56 */     String masterHost = masterProperties.getProperty("HOST");
/*     */     
/*     */ 
/*  59 */     if (masterHost != null) {
/*  60 */       masterUrl.append(masterHost);
/*     */     }
/*     */     
/*  63 */     String slaveHost = slaveProperties.getProperty("HOST");
/*     */     
/*     */ 
/*  66 */     if (slaveHost != null) {
/*  67 */       slaveUrl.append(slaveHost);
/*     */     }
/*     */     
/*  70 */     String masterDb = masterProperties.getProperty("DBNAME");
/*     */     
/*     */ 
/*  73 */     masterUrl.append("/");
/*     */     
/*  75 */     if (masterDb != null) {
/*  76 */       masterUrl.append(masterDb);
/*     */     }
/*     */     
/*  79 */     String slaveDb = slaveProperties.getProperty("DBNAME");
/*     */     
/*     */ 
/*  82 */     slaveUrl.append("/");
/*     */     
/*  84 */     if (slaveDb != null) {
/*  85 */       slaveUrl.append(slaveDb);
/*     */     }
/*     */     
/*  88 */     this.masterConnection = ((Connection)driver.connect(masterUrl.toString(), masterProperties));
/*     */     
/*  90 */     this.slavesConnection = ((Connection)driver.connect(slaveUrl.toString(), slaveProperties));
/*     */     
/*     */ 
/*  93 */     this.currentConnection = this.masterConnection;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public synchronized void clearWarnings()
/*     */     throws SQLException
/*     */   {
/* 102 */     this.currentConnection.clearWarnings();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public synchronized void close()
/*     */     throws SQLException
/*     */   {
/* 111 */     this.masterConnection.close();
/* 112 */     this.slavesConnection.close();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public synchronized void commit()
/*     */     throws SQLException
/*     */   {
/* 121 */     this.currentConnection.commit();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public java.sql.Statement createStatement()
/*     */     throws SQLException
/*     */   {
/* 130 */     java.sql.Statement stmt = this.currentConnection.createStatement();
/* 131 */     ((Statement)stmt).setPingTarget(this);
/*     */     
/* 133 */     return stmt;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public synchronized java.sql.Statement createStatement(int resultSetType, int resultSetConcurrency)
/*     */     throws SQLException
/*     */   {
/* 143 */     java.sql.Statement stmt = this.currentConnection.createStatement(resultSetType, resultSetConcurrency);
/*     */     
/*     */ 
/* 146 */     ((Statement)stmt).setPingTarget(this);
/*     */     
/* 148 */     return stmt;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public synchronized java.sql.Statement createStatement(int resultSetType, int resultSetConcurrency, int resultSetHoldability)
/*     */     throws SQLException
/*     */   {
/* 159 */     java.sql.Statement stmt = this.currentConnection.createStatement(resultSetType, resultSetConcurrency, resultSetHoldability);
/*     */     
/*     */ 
/* 162 */     ((Statement)stmt).setPingTarget(this);
/*     */     
/* 164 */     return stmt;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public synchronized boolean getAutoCommit()
/*     */     throws SQLException
/*     */   {
/* 173 */     return this.currentConnection.getAutoCommit();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public synchronized String getCatalog()
/*     */     throws SQLException
/*     */   {
/* 182 */     return this.currentConnection.getCatalog();
/*     */   }
/*     */   
/*     */   public synchronized Connection getCurrentConnection() {
/* 186 */     return this.currentConnection;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public synchronized int getHoldability()
/*     */     throws SQLException
/*     */   {
/* 195 */     return this.currentConnection.getHoldability();
/*     */   }
/*     */   
/*     */   public synchronized Connection getMasterConnection() {
/* 199 */     return this.masterConnection;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public synchronized DatabaseMetaData getMetaData()
/*     */     throws SQLException
/*     */   {
/* 208 */     return this.currentConnection.getMetaData();
/*     */   }
/*     */   
/*     */   public synchronized Connection getSlavesConnection() {
/* 212 */     return this.slavesConnection;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public synchronized int getTransactionIsolation()
/*     */     throws SQLException
/*     */   {
/* 221 */     return this.currentConnection.getTransactionIsolation();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public synchronized Map getTypeMap()
/*     */     throws SQLException
/*     */   {
/* 230 */     return this.currentConnection.getTypeMap();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public synchronized SQLWarning getWarnings()
/*     */     throws SQLException
/*     */   {
/* 239 */     return this.currentConnection.getWarnings();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public synchronized boolean isClosed()
/*     */     throws SQLException
/*     */   {
/* 248 */     return this.currentConnection.isClosed();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public synchronized boolean isReadOnly()
/*     */     throws SQLException
/*     */   {
/* 257 */     return this.currentConnection == this.slavesConnection;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public synchronized String nativeSQL(String sql)
/*     */     throws SQLException
/*     */   {
/* 266 */     return this.currentConnection.nativeSQL(sql);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public CallableStatement prepareCall(String sql)
/*     */     throws SQLException
/*     */   {
/* 275 */     return this.currentConnection.prepareCall(sql);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public synchronized CallableStatement prepareCall(String sql, int resultSetType, int resultSetConcurrency)
/*     */     throws SQLException
/*     */   {
/* 285 */     return this.currentConnection.prepareCall(sql, resultSetType, resultSetConcurrency);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public synchronized CallableStatement prepareCall(String sql, int resultSetType, int resultSetConcurrency, int resultSetHoldability)
/*     */     throws SQLException
/*     */   {
/* 297 */     return this.currentConnection.prepareCall(sql, resultSetType, resultSetConcurrency, resultSetHoldability);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public PreparedStatement prepareStatement(String sql)
/*     */     throws SQLException
/*     */   {
/* 307 */     PreparedStatement pstmt = this.currentConnection.prepareStatement(sql);
/*     */     
/* 309 */     ((Statement)pstmt).setPingTarget(this);
/*     */     
/* 311 */     return pstmt;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public synchronized PreparedStatement prepareStatement(String sql, int autoGeneratedKeys)
/*     */     throws SQLException
/*     */   {
/* 321 */     PreparedStatement pstmt = this.currentConnection.prepareStatement(sql, autoGeneratedKeys);
/*     */     
/* 323 */     ((Statement)pstmt).setPingTarget(this);
/*     */     
/* 325 */     return pstmt;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public synchronized PreparedStatement prepareStatement(String sql, int resultSetType, int resultSetConcurrency)
/*     */     throws SQLException
/*     */   {
/* 335 */     PreparedStatement pstmt = this.currentConnection.prepareStatement(sql, resultSetType, resultSetConcurrency);
/*     */     
/*     */ 
/* 338 */     ((Statement)pstmt).setPingTarget(this);
/*     */     
/* 340 */     return pstmt;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public synchronized PreparedStatement prepareStatement(String sql, int resultSetType, int resultSetConcurrency, int resultSetHoldability)
/*     */     throws SQLException
/*     */   {
/* 352 */     PreparedStatement pstmt = this.currentConnection.prepareStatement(sql, resultSetType, resultSetConcurrency, resultSetHoldability);
/*     */     
/*     */ 
/* 355 */     ((Statement)pstmt).setPingTarget(this);
/*     */     
/* 357 */     return pstmt;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public synchronized PreparedStatement prepareStatement(String sql, int[] columnIndexes)
/*     */     throws SQLException
/*     */   {
/* 367 */     PreparedStatement pstmt = this.currentConnection.prepareStatement(sql, columnIndexes);
/*     */     
/* 369 */     ((Statement)pstmt).setPingTarget(this);
/*     */     
/* 371 */     return pstmt;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public synchronized PreparedStatement prepareStatement(String sql, String[] columnNames)
/*     */     throws SQLException
/*     */   {
/* 382 */     PreparedStatement pstmt = this.currentConnection.prepareStatement(sql, columnNames);
/*     */     
/* 384 */     ((Statement)pstmt).setPingTarget(this);
/*     */     
/* 386 */     return pstmt;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public synchronized void releaseSavepoint(Savepoint savepoint)
/*     */     throws SQLException
/*     */   {
/* 396 */     this.currentConnection.releaseSavepoint(savepoint);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public synchronized void rollback()
/*     */     throws SQLException
/*     */   {
/* 405 */     this.currentConnection.rollback();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public synchronized void rollback(Savepoint savepoint)
/*     */     throws SQLException
/*     */   {
/* 414 */     this.currentConnection.rollback(savepoint);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public synchronized void setAutoCommit(boolean autoCommit)
/*     */     throws SQLException
/*     */   {
/* 424 */     this.currentConnection.setAutoCommit(autoCommit);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public synchronized void setCatalog(String catalog)
/*     */     throws SQLException
/*     */   {
/* 433 */     this.currentConnection.setCatalog(catalog);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public synchronized void setHoldability(int holdability)
/*     */     throws SQLException
/*     */   {
/* 443 */     this.currentConnection.setHoldability(holdability);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public synchronized void setReadOnly(boolean readOnly)
/*     */     throws SQLException
/*     */   {
/* 452 */     if (readOnly) {
/* 453 */       if (this.currentConnection != this.slavesConnection) {
/* 454 */         switchToSlavesConnection();
/*     */       }
/*     */     }
/* 457 */     else if (this.currentConnection != this.masterConnection) {
/* 458 */       switchToMasterConnection();
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public synchronized Savepoint setSavepoint()
/*     */     throws SQLException
/*     */   {
/* 469 */     return this.currentConnection.setSavepoint();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public synchronized Savepoint setSavepoint(String name)
/*     */     throws SQLException
/*     */   {
/* 478 */     return this.currentConnection.setSavepoint(name);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public synchronized void setTransactionIsolation(int level)
/*     */     throws SQLException
/*     */   {
/* 488 */     this.currentConnection.setTransactionIsolation(level);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public synchronized void setTypeMap(Map arg0)
/*     */     throws SQLException
/*     */   {
/* 499 */     this.currentConnection.setTypeMap(arg0);
/*     */   }
/*     */   
/*     */   private synchronized void switchToMasterConnection() throws SQLException {
/* 503 */     swapConnections(this.masterConnection, this.slavesConnection);
/*     */   }
/*     */   
/*     */   private synchronized void switchToSlavesConnection() throws SQLException {
/* 507 */     swapConnections(this.slavesConnection, this.masterConnection);
/*     */   }
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
/*     */   private synchronized void swapConnections(Connection switchToConnection, Connection switchFromConnection)
/*     */     throws SQLException
/*     */   {
/* 522 */     String switchFromCatalog = switchFromConnection.getCatalog();
/* 523 */     String switchToCatalog = switchToConnection.getCatalog();
/*     */     
/* 525 */     if ((switchToCatalog != null) && (!switchToCatalog.equals(switchFromCatalog))) {
/* 526 */       switchToConnection.setCatalog(switchFromCatalog);
/* 527 */     } else if (switchFromCatalog != null) {
/* 528 */       switchToConnection.setCatalog(switchFromCatalog);
/*     */     }
/*     */     
/* 531 */     boolean switchToAutoCommit = switchToConnection.getAutoCommit();
/* 532 */     boolean switchFromConnectionAutoCommit = switchFromConnection.getAutoCommit();
/*     */     
/* 534 */     if (switchFromConnectionAutoCommit != switchToAutoCommit) {
/* 535 */       switchToConnection.setAutoCommit(switchFromConnectionAutoCommit);
/*     */     }
/*     */     
/* 538 */     int switchToIsolation = switchToConnection.getTransactionIsolation();
/*     */     
/*     */ 
/* 541 */     int switchFromIsolation = switchFromConnection.getTransactionIsolation();
/*     */     
/* 543 */     if (switchFromIsolation != switchToIsolation) {
/* 544 */       switchToConnection.setTransactionIsolation(switchFromIsolation);
/*     */     }
/*     */     
/*     */ 
/* 548 */     this.currentConnection = switchToConnection;
/*     */   }
/*     */   
/*     */   public synchronized void doPing() throws SQLException {
/* 552 */     if (this.masterConnection != null) {
/* 553 */       this.masterConnection.ping();
/*     */     }
/*     */     
/* 556 */     if (this.slavesConnection != null) {
/* 557 */       this.slavesConnection.ping();
/*     */     }
/*     */   }
/*     */ }


/* Location:              E:\java\java学习\hutubill\lib\all.jar!\com\mysql\jdbc\ReplicationConnection.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version:       0.7.1
 */
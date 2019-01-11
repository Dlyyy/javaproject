/*     */ package com.mysql.jdbc.jdbc2.optional;
/*     */ 
/*     */ import com.mysql.jdbc.SQLError;
/*     */ import java.sql.CallableStatement;
/*     */ import java.sql.DatabaseMetaData;
/*     */ import java.sql.PreparedStatement;
/*     */ import java.sql.SQLException;
/*     */ import java.sql.SQLWarning;
/*     */ import java.sql.Savepoint;
/*     */ import java.sql.Statement;
/*     */ import java.util.Map;
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
/*     */ public class ConnectionWrapper
/*     */   extends WrapperBase
/*     */   implements java.sql.Connection
/*     */ {
/*  58 */   private com.mysql.jdbc.Connection mc = null;
/*     */   
/*  60 */   private MysqlPooledConnection mpc = null;
/*     */   
/*  62 */   private String invalidHandleStr = "Logical handle no longer valid";
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   private boolean closed;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   private boolean isForXa;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public ConnectionWrapper(MysqlPooledConnection mysqlPooledConnection, com.mysql.jdbc.Connection mysqlConnection, boolean forXa)
/*     */     throws SQLException
/*     */   {
/*  81 */     this.mpc = mysqlPooledConnection;
/*  82 */     this.mc = mysqlConnection;
/*  83 */     this.closed = false;
/*  84 */     this.pooledConnection = this.mpc;
/*  85 */     this.isForXa = forXa;
/*     */     
/*  87 */     if (this.isForXa) {
/*  88 */       setInGlobalTx(false);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setAutoCommit(boolean autoCommit)
/*     */     throws SQLException
/*     */   {
/*  99 */     checkClosed();
/*     */     
/* 101 */     if ((autoCommit) && (isInGlobalTx())) {
/* 102 */       throw SQLError.createSQLException("Can't set autocommit to 'true' on an XAConnection", "2D000", 1401);
/*     */     }
/*     */     
/*     */ 
/*     */     try
/*     */     {
/* 108 */       this.mc.setAutoCommit(autoCommit);
/*     */     } catch (SQLException sqlException) {
/* 110 */       checkAndFireConnectionError(sqlException);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean getAutoCommit()
/*     */     throws SQLException
/*     */   {
/* 121 */     checkClosed();
/*     */     try
/*     */     {
/* 124 */       return this.mc.getAutoCommit();
/*     */     } catch (SQLException sqlException) {
/* 126 */       checkAndFireConnectionError(sqlException);
/*     */     }
/*     */     
/* 129 */     return false;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setCatalog(String catalog)
/*     */     throws SQLException
/*     */   {
/* 139 */     checkClosed();
/*     */     try
/*     */     {
/* 142 */       this.mc.setCatalog(catalog);
/*     */     } catch (SQLException sqlException) {
/* 144 */       checkAndFireConnectionError(sqlException);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public String getCatalog()
/*     */     throws SQLException
/*     */   {
/* 158 */     checkClosed();
/*     */     try
/*     */     {
/* 161 */       return this.mc.getCatalog();
/*     */     } catch (SQLException sqlException) {
/* 163 */       checkAndFireConnectionError(sqlException);
/*     */     }
/*     */     
/* 166 */     return null;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean isClosed()
/*     */     throws SQLException
/*     */   {
/* 176 */     return (this.closed) || (this.mc.isClosed());
/*     */   }
/*     */   
/*     */   public boolean isMasterConnection() throws SQLException {
/* 180 */     return this.mc.isMasterConnection();
/*     */   }
/*     */   
/*     */ 
/*     */   public void setHoldability(int arg0)
/*     */     throws SQLException
/*     */   {
/* 187 */     checkClosed();
/*     */     try
/*     */     {
/* 190 */       this.mc.setHoldability(arg0);
/*     */     } catch (SQLException sqlException) {
/* 192 */       checkAndFireConnectionError(sqlException);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */   public int getHoldability()
/*     */     throws SQLException
/*     */   {
/* 200 */     checkClosed();
/*     */     try
/*     */     {
/* 203 */       return this.mc.getHoldability();
/*     */     } catch (SQLException sqlException) {
/* 205 */       checkAndFireConnectionError(sqlException);
/*     */     }
/*     */     
/* 208 */     return 1;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public long getIdleFor()
/*     */   {
/* 218 */     return this.mc.getIdleFor();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public DatabaseMetaData getMetaData()
/*     */     throws SQLException
/*     */   {
/* 231 */     checkClosed();
/*     */     try
/*     */     {
/* 234 */       return this.mc.getMetaData();
/*     */     } catch (SQLException sqlException) {
/* 236 */       checkAndFireConnectionError(sqlException);
/*     */     }
/*     */     
/* 239 */     return null;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setReadOnly(boolean readOnly)
/*     */     throws SQLException
/*     */   {
/* 249 */     checkClosed();
/*     */     try
/*     */     {
/* 252 */       this.mc.setReadOnly(readOnly);
/*     */     } catch (SQLException sqlException) {
/* 254 */       checkAndFireConnectionError(sqlException);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean isReadOnly()
/*     */     throws SQLException
/*     */   {
/* 265 */     checkClosed();
/*     */     try
/*     */     {
/* 268 */       return this.mc.isReadOnly();
/*     */     } catch (SQLException sqlException) {
/* 270 */       checkAndFireConnectionError(sqlException);
/*     */     }
/*     */     
/* 273 */     return false;
/*     */   }
/*     */   
/*     */ 
/*     */   public Savepoint setSavepoint()
/*     */     throws SQLException
/*     */   {
/* 280 */     checkClosed();
/*     */     
/* 282 */     if (isInGlobalTx()) {
/* 283 */       throw SQLError.createSQLException("Can't set autocommit to 'true' on an XAConnection", "2D000", 1401);
/*     */     }
/*     */     
/*     */ 
/*     */     try
/*     */     {
/* 289 */       return this.mc.setSavepoint();
/*     */     } catch (SQLException sqlException) {
/* 291 */       checkAndFireConnectionError(sqlException);
/*     */     }
/*     */     
/* 294 */     return null;
/*     */   }
/*     */   
/*     */ 
/*     */   public Savepoint setSavepoint(String arg0)
/*     */     throws SQLException
/*     */   {
/* 301 */     checkClosed();
/*     */     
/* 303 */     if (isInGlobalTx()) {
/* 304 */       throw SQLError.createSQLException("Can't set autocommit to 'true' on an XAConnection", "2D000", 1401);
/*     */     }
/*     */     
/*     */ 
/*     */     try
/*     */     {
/* 310 */       return this.mc.setSavepoint(arg0);
/*     */     } catch (SQLException sqlException) {
/* 312 */       checkAndFireConnectionError(sqlException);
/*     */     }
/*     */     
/* 315 */     return null;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setTransactionIsolation(int level)
/*     */     throws SQLException
/*     */   {
/* 325 */     checkClosed();
/*     */     try
/*     */     {
/* 328 */       this.mc.setTransactionIsolation(level);
/*     */     } catch (SQLException sqlException) {
/* 330 */       checkAndFireConnectionError(sqlException);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public int getTransactionIsolation()
/*     */     throws SQLException
/*     */   {
/* 341 */     checkClosed();
/*     */     try
/*     */     {
/* 344 */       return this.mc.getTransactionIsolation();
/*     */     } catch (SQLException sqlException) {
/* 346 */       checkAndFireConnectionError(sqlException);
/*     */     }
/*     */     
/* 349 */     return 4;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setTypeMap(Map map)
/*     */     throws SQLException
/*     */   {
/* 360 */     checkClosed();
/*     */     try
/*     */     {
/* 363 */       this.mc.setTypeMap(map);
/*     */     } catch (SQLException sqlException) {
/* 365 */       checkAndFireConnectionError(sqlException);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public Map getTypeMap()
/*     */     throws SQLException
/*     */   {
/* 376 */     checkClosed();
/*     */     try
/*     */     {
/* 379 */       return this.mc.getTypeMap();
/*     */     } catch (SQLException sqlException) {
/* 381 */       checkAndFireConnectionError(sqlException);
/*     */     }
/*     */     
/* 384 */     return null;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public SQLWarning getWarnings()
/*     */     throws SQLException
/*     */   {
/* 394 */     checkClosed();
/*     */     try
/*     */     {
/* 397 */       return this.mc.getWarnings();
/*     */     } catch (SQLException sqlException) {
/* 399 */       checkAndFireConnectionError(sqlException);
/*     */     }
/*     */     
/* 402 */     return null;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void clearWarnings()
/*     */     throws SQLException
/*     */   {
/* 413 */     checkClosed();
/*     */     try
/*     */     {
/* 416 */       this.mc.clearWarnings();
/*     */     } catch (SQLException sqlException) {
/* 418 */       checkAndFireConnectionError(sqlException);
/*     */     }
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
/*     */   public void close()
/*     */     throws SQLException
/*     */   {
/* 433 */     close(true);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void commit()
/*     */     throws SQLException
/*     */   {
/* 444 */     checkClosed();
/*     */     
/* 446 */     if (isInGlobalTx()) {
/* 447 */       throw SQLError.createSQLException("Can't call commit() on an XAConnection associated with a global transaction", "2D000", 1401);
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */     try
/*     */     {
/* 454 */       this.mc.commit();
/*     */     } catch (SQLException sqlException) {
/* 456 */       checkAndFireConnectionError(sqlException);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public Statement createStatement()
/*     */     throws SQLException
/*     */   {
/* 467 */     checkClosed();
/*     */     try
/*     */     {
/* 470 */       return new StatementWrapper(this, this.mpc, this.mc.createStatement());
/*     */     }
/*     */     catch (SQLException sqlException) {
/* 473 */       checkAndFireConnectionError(sqlException);
/*     */     }
/*     */     
/* 476 */     return null;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public Statement createStatement(int resultSetType, int resultSetConcurrency)
/*     */     throws SQLException
/*     */   {
/* 487 */     checkClosed();
/*     */     try
/*     */     {
/* 490 */       return new StatementWrapper(this, this.mpc, this.mc.createStatement(resultSetType, resultSetConcurrency));
/*     */     }
/*     */     catch (SQLException sqlException) {
/* 493 */       checkAndFireConnectionError(sqlException);
/*     */     }
/*     */     
/* 496 */     return null;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public Statement createStatement(int arg0, int arg1, int arg2)
/*     */     throws SQLException
/*     */   {
/* 504 */     checkClosed();
/*     */     try
/*     */     {
/* 507 */       return new StatementWrapper(this, this.mpc, this.mc.createStatement(arg0, arg1, arg2));
/*     */     }
/*     */     catch (SQLException sqlException) {
/* 510 */       checkAndFireConnectionError(sqlException);
/*     */     }
/*     */     
/* 513 */     return null;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public String nativeSQL(String sql)
/*     */     throws SQLException
/*     */   {
/* 523 */     checkClosed();
/*     */     try
/*     */     {
/* 526 */       return this.mc.nativeSQL(sql);
/*     */     } catch (SQLException sqlException) {
/* 528 */       checkAndFireConnectionError(sqlException);
/*     */     }
/*     */     
/* 531 */     return null;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public CallableStatement prepareCall(String sql)
/*     */     throws SQLException
/*     */   {
/* 542 */     checkClosed();
/*     */     try
/*     */     {
/* 545 */       return new CallableStatementWrapper(this, this.mpc, this.mc.prepareCall(sql));
/*     */     }
/*     */     catch (SQLException sqlException) {
/* 548 */       checkAndFireConnectionError(sqlException);
/*     */     }
/*     */     
/* 551 */     return null;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public CallableStatement prepareCall(String sql, int resultSetType, int resultSetConcurrency)
/*     */     throws SQLException
/*     */   {
/* 562 */     checkClosed();
/*     */     try
/*     */     {
/* 565 */       return new CallableStatementWrapper(this, this.mpc, this.mc.prepareCall(sql, resultSetType, resultSetConcurrency));
/*     */     }
/*     */     catch (SQLException sqlException) {
/* 568 */       checkAndFireConnectionError(sqlException);
/*     */     }
/*     */     
/* 571 */     return null;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public CallableStatement prepareCall(String arg0, int arg1, int arg2, int arg3)
/*     */     throws SQLException
/*     */   {
/* 579 */     checkClosed();
/*     */     try
/*     */     {
/* 582 */       return new CallableStatementWrapper(this, this.mpc, this.mc.prepareCall(arg0, arg1, arg2, arg3));
/*     */     }
/*     */     catch (SQLException sqlException) {
/* 585 */       checkAndFireConnectionError(sqlException);
/*     */     }
/*     */     
/* 588 */     return null;
/*     */   }
/*     */   
/*     */   public PreparedStatement clientPrepare(String sql) throws SQLException
/*     */   {
/* 593 */     checkClosed();
/*     */     try
/*     */     {
/* 596 */       return new PreparedStatementWrapper(this, this.mpc, this.mc.clientPrepareStatement(sql));
/*     */     }
/*     */     catch (SQLException sqlException) {
/* 599 */       checkAndFireConnectionError(sqlException);
/*     */     }
/*     */     
/* 602 */     return null;
/*     */   }
/*     */   
/*     */   public PreparedStatement clientPrepare(String sql, int resultSetType, int resultSetConcurrency)
/*     */     throws SQLException
/*     */   {
/* 608 */     checkClosed();
/*     */     try
/*     */     {
/* 611 */       return new PreparedStatementWrapper(this, this.mpc, this.mc.clientPrepareStatement(sql, resultSetType, resultSetConcurrency));
/*     */     }
/*     */     catch (SQLException sqlException)
/*     */     {
/* 615 */       checkAndFireConnectionError(sqlException);
/*     */     }
/*     */     
/* 618 */     return null;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public PreparedStatement prepareStatement(String sql)
/*     */     throws SQLException
/*     */   {
/* 629 */     checkClosed();
/*     */     try
/*     */     {
/* 632 */       return new PreparedStatementWrapper(this, this.mpc, this.mc.prepareStatement(sql));
/*     */     }
/*     */     catch (SQLException sqlException) {
/* 635 */       checkAndFireConnectionError(sqlException);
/*     */     }
/*     */     
/* 638 */     return null;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public PreparedStatement prepareStatement(String sql, int resultSetType, int resultSetConcurrency)
/*     */     throws SQLException
/*     */   {
/* 649 */     checkClosed();
/*     */     try
/*     */     {
/* 652 */       return new PreparedStatementWrapper(this, this.mpc, this.mc.prepareStatement(sql, resultSetType, resultSetConcurrency));
/*     */     }
/*     */     catch (SQLException sqlException) {
/* 655 */       checkAndFireConnectionError(sqlException);
/*     */     }
/*     */     
/* 658 */     return null;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public PreparedStatement prepareStatement(String arg0, int arg1, int arg2, int arg3)
/*     */     throws SQLException
/*     */   {
/* 666 */     checkClosed();
/*     */     try
/*     */     {
/* 669 */       return new PreparedStatementWrapper(this, this.mpc, this.mc.prepareStatement(arg0, arg1, arg2, arg3));
/*     */     }
/*     */     catch (SQLException sqlException) {
/* 672 */       checkAndFireConnectionError(sqlException);
/*     */     }
/*     */     
/* 675 */     return null;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public PreparedStatement prepareStatement(String arg0, int arg1)
/*     */     throws SQLException
/*     */   {
/* 683 */     checkClosed();
/*     */     try
/*     */     {
/* 686 */       return new PreparedStatementWrapper(this, this.mpc, this.mc.prepareStatement(arg0, arg1));
/*     */     }
/*     */     catch (SQLException sqlException) {
/* 689 */       checkAndFireConnectionError(sqlException);
/*     */     }
/*     */     
/* 692 */     return null;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public PreparedStatement prepareStatement(String arg0, int[] arg1)
/*     */     throws SQLException
/*     */   {
/* 700 */     checkClosed();
/*     */     try
/*     */     {
/* 703 */       return new PreparedStatementWrapper(this, this.mpc, this.mc.prepareStatement(arg0, arg1));
/*     */     }
/*     */     catch (SQLException sqlException) {
/* 706 */       checkAndFireConnectionError(sqlException);
/*     */     }
/*     */     
/* 709 */     return null;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public PreparedStatement prepareStatement(String arg0, String[] arg1)
/*     */     throws SQLException
/*     */   {
/* 717 */     checkClosed();
/*     */     try
/*     */     {
/* 720 */       return new PreparedStatementWrapper(this, this.mpc, this.mc.prepareStatement(arg0, arg1));
/*     */     }
/*     */     catch (SQLException sqlException) {
/* 723 */       checkAndFireConnectionError(sqlException);
/*     */     }
/*     */     
/* 726 */     return null;
/*     */   }
/*     */   
/*     */ 
/*     */   public void releaseSavepoint(Savepoint arg0)
/*     */     throws SQLException
/*     */   {
/* 733 */     checkClosed();
/*     */     try
/*     */     {
/* 736 */       this.mc.releaseSavepoint(arg0);
/*     */     } catch (SQLException sqlException) {
/* 738 */       checkAndFireConnectionError(sqlException);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void rollback()
/*     */     throws SQLException
/*     */   {
/* 749 */     checkClosed();
/*     */     
/*     */ 
/* 752 */     if (isInGlobalTx()) {
/* 753 */       throw SQLError.createSQLException("Can't call rollback() on an XAConnection associated with a global transaction", "2D000", 1401);
/*     */     }
/*     */     
/*     */ 
/*     */     try
/*     */     {
/* 759 */       this.mc.rollback();
/*     */     } catch (SQLException sqlException) {
/* 761 */       checkAndFireConnectionError(sqlException);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */   public void rollback(Savepoint arg0)
/*     */     throws SQLException
/*     */   {
/* 769 */     checkClosed();
/*     */     
/* 771 */     if (isInGlobalTx()) {
/* 772 */       throw SQLError.createSQLException("Can't call rollback() on an XAConnection associated with a global transaction", "2D000", 1401);
/*     */     }
/*     */     
/*     */ 
/*     */     try
/*     */     {
/* 778 */       this.mc.rollback(arg0);
/*     */     } catch (SQLException sqlException) {
/* 780 */       checkAndFireConnectionError(sqlException);
/*     */     }
/*     */   }
/*     */   
/*     */   public boolean isSameResource(java.sql.Connection c) {
/* 785 */     if ((c instanceof ConnectionWrapper))
/* 786 */       return this.mc.isSameResource(((ConnectionWrapper)c).mc);
/* 787 */     if ((c instanceof com.mysql.jdbc.Connection)) {
/* 788 */       return this.mc.isSameResource((com.mysql.jdbc.Connection)c);
/*     */     }
/*     */     
/* 791 */     return false;
/*     */   }
/*     */   
/*     */   protected void close(boolean fireClosedEvent) throws SQLException {
/* 795 */     synchronized (this.mpc) {
/* 796 */       if (this.closed) {
/* 797 */         return;
/*     */       }
/*     */       
/* 800 */       if ((!isInGlobalTx()) && (this.mc.getRollbackOnPooledClose()) && (!getAutoCommit()))
/*     */       {
/*     */ 
/* 803 */         rollback();
/*     */       }
/*     */       
/* 806 */       if (fireClosedEvent) {
/* 807 */         this.mpc.callListener(2, null);
/*     */       }
/*     */       
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 816 */       this.closed = true;
/*     */     }
/*     */   }
/*     */   
/*     */   private void checkClosed() throws SQLException {
/* 821 */     if (this.closed) {
/* 822 */       throw SQLError.createSQLException(this.invalidHandleStr);
/*     */     }
/*     */   }
/*     */   
/*     */   protected boolean isInGlobalTx() {
/* 827 */     return this.mc.isInGlobalTx();
/*     */   }
/*     */   
/*     */   protected void setInGlobalTx(boolean flag) {
/* 831 */     this.mc.setInGlobalTx(flag);
/*     */   }
/*     */   
/*     */   public void ping() throws SQLException {
/* 835 */     if (this.mc != null) {
/* 836 */       this.mc.ping();
/*     */     }
/*     */   }
/*     */ }


/* Location:              E:\java\java学习\hutubill\lib\all.jar!\com\mysql\jdbc\jdbc2\optional\ConnectionWrapper.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version:       0.7.1
 */
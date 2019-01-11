/*     */ package com.mysql.jdbc.jdbc2.optional;
/*     */ 
/*     */ import com.mysql.jdbc.SQLError;
/*     */ import java.sql.Connection;
/*     */ import java.sql.SQLException;
/*     */ import java.sql.SQLWarning;
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
/*     */ public class StatementWrapper
/*     */   extends WrapperBase
/*     */   implements java.sql.Statement
/*     */ {
/*     */   protected java.sql.Statement wrappedStmt;
/*     */   protected ConnectionWrapper wrappedConn;
/*     */   
/*     */   protected StatementWrapper(ConnectionWrapper c, MysqlPooledConnection conn, java.sql.Statement toWrap)
/*     */   {
/*  51 */     this.pooledConnection = conn;
/*  52 */     this.wrappedStmt = toWrap;
/*  53 */     this.wrappedConn = c;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public Connection getConnection()
/*     */     throws SQLException
/*     */   {
/*     */     try
/*     */     {
/*  63 */       if (this.wrappedStmt != null) {
/*  64 */         return this.wrappedConn;
/*     */       }
/*     */       
/*  67 */       throw SQLError.createSQLException("Statement already closed", "S1009");
/*     */     }
/*     */     catch (SQLException sqlEx) {
/*  70 */       checkAndFireConnectionError(sqlEx);
/*     */     }
/*     */     
/*  73 */     return null;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setCursorName(String name)
/*     */     throws SQLException
/*     */   {
/*     */     try
/*     */     {
/*  86 */       if (this.wrappedStmt != null) {
/*  87 */         this.wrappedStmt.setCursorName(name);
/*     */       } else {
/*  89 */         throw SQLError.createSQLException("Statement already closed", "S1009");
/*     */       }
/*     */     }
/*     */     catch (SQLException sqlEx) {
/*  93 */       checkAndFireConnectionError(sqlEx);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public void setEscapeProcessing(boolean enable)
/*     */     throws SQLException
/*     */   {
/*     */     try
/*     */     {
/* 104 */       if (this.wrappedStmt != null) {
/* 105 */         this.wrappedStmt.setEscapeProcessing(enable);
/*     */       } else {
/* 107 */         throw SQLError.createSQLException("Statement already closed", "S1009");
/*     */       }
/*     */     }
/*     */     catch (SQLException sqlEx) {
/* 111 */       checkAndFireConnectionError(sqlEx);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public void setFetchDirection(int direction)
/*     */     throws SQLException
/*     */   {
/*     */     try
/*     */     {
/* 122 */       if (this.wrappedStmt != null) {
/* 123 */         this.wrappedStmt.setFetchDirection(direction);
/*     */       } else {
/* 125 */         throw SQLError.createSQLException("Statement already closed", "S1009");
/*     */       }
/*     */     }
/*     */     catch (SQLException sqlEx) {
/* 129 */       checkAndFireConnectionError(sqlEx);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public int getFetchDirection()
/*     */     throws SQLException
/*     */   {
/*     */     try
/*     */     {
/* 140 */       if (this.wrappedStmt != null) {
/* 141 */         return this.wrappedStmt.getFetchDirection();
/*     */       }
/*     */       
/* 144 */       throw SQLError.createSQLException("Statement already closed", "S1009");
/*     */     }
/*     */     catch (SQLException sqlEx) {
/* 147 */       checkAndFireConnectionError(sqlEx);
/*     */     }
/*     */     
/* 150 */     return 1000;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setFetchSize(int rows)
/*     */     throws SQLException
/*     */   {
/*     */     try
/*     */     {
/* 163 */       if (this.wrappedStmt != null) {
/* 164 */         this.wrappedStmt.setFetchSize(rows);
/*     */       } else {
/* 166 */         throw SQLError.createSQLException("Statement already closed", "S1009");
/*     */       }
/*     */     }
/*     */     catch (SQLException sqlEx) {
/* 170 */       checkAndFireConnectionError(sqlEx);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public int getFetchSize()
/*     */     throws SQLException
/*     */   {
/*     */     try
/*     */     {
/* 181 */       if (this.wrappedStmt != null) {
/* 182 */         return this.wrappedStmt.getFetchSize();
/*     */       }
/*     */       
/* 185 */       throw SQLError.createSQLException("Statement already closed", "S1009");
/*     */     }
/*     */     catch (SQLException sqlEx) {
/* 188 */       checkAndFireConnectionError(sqlEx);
/*     */     }
/*     */     
/* 191 */     return 0;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public java.sql.ResultSet getGeneratedKeys()
/*     */     throws SQLException
/*     */   {
/*     */     try
/*     */     {
/* 203 */       if (this.wrappedStmt != null) {
/* 204 */         return this.wrappedStmt.getGeneratedKeys();
/*     */       }
/*     */       
/* 207 */       throw SQLError.createSQLException("Statement already closed", "S1009");
/*     */     }
/*     */     catch (SQLException sqlEx) {
/* 210 */       checkAndFireConnectionError(sqlEx);
/*     */     }
/*     */     
/* 213 */     return null;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setMaxFieldSize(int max)
/*     */     throws SQLException
/*     */   {
/*     */     try
/*     */     {
/* 226 */       if (this.wrappedStmt != null) {
/* 227 */         this.wrappedStmt.setMaxFieldSize(max);
/*     */       } else {
/* 229 */         throw SQLError.createSQLException("Statement already closed", "S1009");
/*     */       }
/*     */     }
/*     */     catch (SQLException sqlEx) {
/* 233 */       checkAndFireConnectionError(sqlEx);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public int getMaxFieldSize()
/*     */     throws SQLException
/*     */   {
/*     */     try
/*     */     {
/* 244 */       if (this.wrappedStmt != null) {
/* 245 */         return this.wrappedStmt.getMaxFieldSize();
/*     */       }
/*     */       
/* 248 */       throw SQLError.createSQLException("Statement already closed", "S1009");
/*     */     }
/*     */     catch (SQLException sqlEx) {
/* 251 */       checkAndFireConnectionError(sqlEx);
/*     */     }
/*     */     
/* 254 */     return 0;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setMaxRows(int max)
/*     */     throws SQLException
/*     */   {
/*     */     try
/*     */     {
/* 266 */       if (this.wrappedStmt != null) {
/* 267 */         this.wrappedStmt.setMaxRows(max);
/*     */       } else {
/* 269 */         throw SQLError.createSQLException("Statement already closed", "S1009");
/*     */       }
/*     */     }
/*     */     catch (SQLException sqlEx) {
/* 273 */       checkAndFireConnectionError(sqlEx);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public int getMaxRows()
/*     */     throws SQLException
/*     */   {
/*     */     try
/*     */     {
/* 284 */       if (this.wrappedStmt != null) {
/* 285 */         return this.wrappedStmt.getMaxRows();
/*     */       }
/*     */       
/* 288 */       throw SQLError.createSQLException("Statement already closed", "S1009");
/*     */     }
/*     */     catch (SQLException sqlEx) {
/* 291 */       checkAndFireConnectionError(sqlEx);
/*     */     }
/*     */     
/* 294 */     return 0;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean getMoreResults()
/*     */     throws SQLException
/*     */   {
/*     */     try
/*     */     {
/* 306 */       if (this.wrappedStmt != null) {
/* 307 */         return this.wrappedStmt.getMoreResults();
/*     */       }
/*     */       
/* 310 */       throw SQLError.createSQLException("Statement already closed", "S1009");
/*     */     }
/*     */     catch (SQLException sqlEx) {
/* 313 */       checkAndFireConnectionError(sqlEx);
/*     */     }
/*     */     
/* 316 */     return false;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public boolean getMoreResults(int current)
/*     */     throws SQLException
/*     */   {
/*     */     try
/*     */     {
/* 326 */       if (this.wrappedStmt != null) {
/* 327 */         return this.wrappedStmt.getMoreResults(current);
/*     */       }
/*     */       
/* 330 */       throw SQLError.createSQLException("Statement already closed", "S1009");
/*     */     }
/*     */     catch (SQLException sqlEx) {
/* 333 */       checkAndFireConnectionError(sqlEx);
/*     */     }
/*     */     
/* 336 */     return false;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public void setQueryTimeout(int seconds)
/*     */     throws SQLException
/*     */   {
/*     */     try
/*     */     {
/* 346 */       if (this.wrappedStmt != null) {
/* 347 */         this.wrappedStmt.setQueryTimeout(seconds);
/*     */       } else {
/* 349 */         throw SQLError.createSQLException("Statement already closed", "S1009");
/*     */       }
/*     */     }
/*     */     catch (SQLException sqlEx) {
/* 353 */       checkAndFireConnectionError(sqlEx);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public int getQueryTimeout()
/*     */     throws SQLException
/*     */   {
/*     */     try
/*     */     {
/* 364 */       if (this.wrappedStmt != null) {
/* 365 */         return this.wrappedStmt.getQueryTimeout();
/*     */       }
/*     */       
/* 368 */       throw SQLError.createSQLException("Statement already closed", "S1009");
/*     */     }
/*     */     catch (SQLException sqlEx) {
/* 371 */       checkAndFireConnectionError(sqlEx);
/*     */     }
/*     */     
/* 374 */     return 0;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public java.sql.ResultSet getResultSet()
/*     */     throws SQLException
/*     */   {
/*     */     try
/*     */     {
/* 384 */       if (this.wrappedStmt != null) {
/* 385 */         java.sql.ResultSet rs = this.wrappedStmt.getResultSet();
/*     */         
/* 387 */         ((com.mysql.jdbc.ResultSet)rs).setWrapperStatement(this);
/*     */         
/* 389 */         return rs;
/*     */       }
/*     */       
/* 392 */       throw SQLError.createSQLException("Statement already closed", "S1009");
/*     */     }
/*     */     catch (SQLException sqlEx) {
/* 395 */       checkAndFireConnectionError(sqlEx);
/*     */     }
/*     */     
/* 398 */     return null;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public int getResultSetConcurrency()
/*     */     throws SQLException
/*     */   {
/*     */     try
/*     */     {
/* 408 */       if (this.wrappedStmt != null) {
/* 409 */         return this.wrappedStmt.getResultSetConcurrency();
/*     */       }
/*     */       
/* 412 */       throw SQLError.createSQLException("Statement already closed", "S1009");
/*     */     }
/*     */     catch (SQLException sqlEx) {
/* 415 */       checkAndFireConnectionError(sqlEx);
/*     */     }
/*     */     
/* 418 */     return 0;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public int getResultSetHoldability()
/*     */     throws SQLException
/*     */   {
/*     */     try
/*     */     {
/* 428 */       if (this.wrappedStmt != null) {
/* 429 */         return this.wrappedStmt.getResultSetHoldability();
/*     */       }
/*     */       
/* 432 */       throw SQLError.createSQLException("Statement already closed", "S1009");
/*     */     }
/*     */     catch (SQLException sqlEx) {
/* 435 */       checkAndFireConnectionError(sqlEx);
/*     */     }
/*     */     
/* 438 */     return 1;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public int getResultSetType()
/*     */     throws SQLException
/*     */   {
/*     */     try
/*     */     {
/* 448 */       if (this.wrappedStmt != null) {
/* 449 */         return this.wrappedStmt.getResultSetType();
/*     */       }
/*     */       
/* 452 */       throw SQLError.createSQLException("Statement already closed", "S1009");
/*     */     }
/*     */     catch (SQLException sqlEx) {
/* 455 */       checkAndFireConnectionError(sqlEx);
/*     */     }
/*     */     
/* 458 */     return 1003;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public int getUpdateCount()
/*     */     throws SQLException
/*     */   {
/*     */     try
/*     */     {
/* 468 */       if (this.wrappedStmt != null) {
/* 469 */         return this.wrappedStmt.getUpdateCount();
/*     */       }
/*     */       
/* 472 */       throw SQLError.createSQLException("Statement already closed", "S1009");
/*     */     }
/*     */     catch (SQLException sqlEx) {
/* 475 */       checkAndFireConnectionError(sqlEx);
/*     */     }
/*     */     
/* 478 */     return -1;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public SQLWarning getWarnings()
/*     */     throws SQLException
/*     */   {
/*     */     try
/*     */     {
/* 488 */       if (this.wrappedStmt != null) {
/* 489 */         return this.wrappedStmt.getWarnings();
/*     */       }
/*     */       
/* 492 */       throw SQLError.createSQLException("Statement already closed", "S1009");
/*     */     }
/*     */     catch (SQLException sqlEx) {
/* 495 */       checkAndFireConnectionError(sqlEx);
/*     */     }
/*     */     
/* 498 */     return null;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public void addBatch(String sql)
/*     */     throws SQLException
/*     */   {
/*     */     try
/*     */     {
/* 508 */       if (this.wrappedStmt != null) {
/* 509 */         this.wrappedStmt.addBatch(sql);
/*     */       }
/*     */     } catch (SQLException sqlEx) {
/* 512 */       checkAndFireConnectionError(sqlEx);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public void cancel()
/*     */     throws SQLException
/*     */   {
/*     */     try
/*     */     {
/* 523 */       if (this.wrappedStmt != null) {
/* 524 */         this.wrappedStmt.cancel();
/*     */       }
/*     */     } catch (SQLException sqlEx) {
/* 527 */       checkAndFireConnectionError(sqlEx);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public void clearBatch()
/*     */     throws SQLException
/*     */   {
/*     */     try
/*     */     {
/* 538 */       if (this.wrappedStmt != null) {
/* 539 */         this.wrappedStmt.clearBatch();
/*     */       }
/*     */     } catch (SQLException sqlEx) {
/* 542 */       checkAndFireConnectionError(sqlEx);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public void clearWarnings()
/*     */     throws SQLException
/*     */   {
/*     */     try
/*     */     {
/* 553 */       if (this.wrappedStmt != null) {
/* 554 */         this.wrappedStmt.clearWarnings();
/*     */       }
/*     */     } catch (SQLException sqlEx) {
/* 557 */       checkAndFireConnectionError(sqlEx);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public void close()
/*     */     throws SQLException
/*     */   {
/*     */     try
/*     */     {
/* 568 */       if (this.wrappedStmt != null) {
/* 569 */         this.wrappedStmt.close();
/*     */       }
/*     */     } catch (SQLException sqlEx) {
/* 572 */       checkAndFireConnectionError(sqlEx);
/*     */     } finally {
/* 574 */       this.wrappedStmt = null;
/* 575 */       this.pooledConnection = null;
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean execute(String sql, int autoGeneratedKeys)
/*     */     throws SQLException
/*     */   {
/*     */     try
/*     */     {
/* 587 */       if (this.wrappedStmt != null) {
/* 588 */         return this.wrappedStmt.execute(sql, autoGeneratedKeys);
/*     */       }
/*     */       
/* 591 */       throw SQLError.createSQLException("Statement already closed", "S1009");
/*     */     }
/*     */     catch (SQLException sqlEx) {
/* 594 */       checkAndFireConnectionError(sqlEx);
/*     */     }
/*     */     
/* 597 */     return false;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean execute(String sql, int[] columnIndexes)
/*     */     throws SQLException
/*     */   {
/*     */     try
/*     */     {
/* 610 */       if (this.wrappedStmt != null) {
/* 611 */         return this.wrappedStmt.execute(sql, columnIndexes);
/*     */       }
/*     */       
/* 614 */       throw SQLError.createSQLException("Statement already closed", "S1009");
/*     */     }
/*     */     catch (SQLException sqlEx) {
/* 617 */       checkAndFireConnectionError(sqlEx);
/*     */     }
/*     */     
/* 620 */     return false;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean execute(String sql, String[] columnNames)
/*     */     throws SQLException
/*     */   {
/*     */     try
/*     */     {
/* 634 */       if (this.wrappedStmt != null) {
/* 635 */         return this.wrappedStmt.execute(sql, columnNames);
/*     */       }
/*     */       
/* 638 */       throw SQLError.createSQLException("Statement already closed", "S1009");
/*     */     }
/*     */     catch (SQLException sqlEx) {
/* 641 */       checkAndFireConnectionError(sqlEx);
/*     */     }
/*     */     
/* 644 */     return false;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean execute(String sql)
/*     */     throws SQLException
/*     */   {
/*     */     try
/*     */     {
/* 657 */       if (this.wrappedStmt != null) {
/* 658 */         return this.wrappedStmt.execute(sql);
/*     */       }
/*     */       
/* 661 */       throw SQLError.createSQLException("Statement already closed", "S1009");
/*     */     }
/*     */     catch (SQLException sqlEx) {
/* 664 */       checkAndFireConnectionError(sqlEx);
/*     */     }
/*     */     
/* 667 */     return false;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public int[] executeBatch()
/*     */     throws SQLException
/*     */   {
/*     */     try
/*     */     {
/* 680 */       if (this.wrappedStmt != null) {
/* 681 */         return this.wrappedStmt.executeBatch();
/*     */       }
/*     */       
/* 684 */       throw SQLError.createSQLException("Statement already closed", "S1009");
/*     */     }
/*     */     catch (SQLException sqlEx) {
/* 687 */       checkAndFireConnectionError(sqlEx);
/*     */     }
/*     */     
/* 690 */     return null;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public java.sql.ResultSet executeQuery(String sql)
/*     */     throws SQLException
/*     */   {
/*     */     try
/*     */     {
/* 703 */       if (this.wrappedStmt != null)
/*     */       {
/* 705 */         java.sql.ResultSet rs = this.wrappedStmt.executeQuery(sql);
/* 706 */         ((com.mysql.jdbc.ResultSet)rs).setWrapperStatement(this);
/*     */         
/* 708 */         return rs;
/*     */       }
/*     */       
/* 711 */       throw SQLError.createSQLException("Statement already closed", "S1009");
/*     */     }
/*     */     catch (SQLException sqlEx) {
/* 714 */       checkAndFireConnectionError(sqlEx);
/*     */     }
/*     */     
/* 717 */     return null;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public int executeUpdate(String sql, int autoGeneratedKeys)
/*     */     throws SQLException
/*     */   {
/*     */     try
/*     */     {
/* 731 */       if (this.wrappedStmt != null) {
/* 732 */         return this.wrappedStmt.executeUpdate(sql, autoGeneratedKeys);
/*     */       }
/*     */       
/* 735 */       throw SQLError.createSQLException("Statement already closed", "S1009");
/*     */     }
/*     */     catch (SQLException sqlEx) {
/* 738 */       checkAndFireConnectionError(sqlEx);
/*     */     }
/*     */     
/* 741 */     return -1;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public int executeUpdate(String sql, int[] columnIndexes)
/*     */     throws SQLException
/*     */   {
/*     */     try
/*     */     {
/* 754 */       if (this.wrappedStmt != null) {
/* 755 */         return this.wrappedStmt.executeUpdate(sql, columnIndexes);
/*     */       }
/*     */       
/* 758 */       throw SQLError.createSQLException("Statement already closed", "S1009");
/*     */     }
/*     */     catch (SQLException sqlEx) {
/* 761 */       checkAndFireConnectionError(sqlEx);
/*     */     }
/*     */     
/* 764 */     return -1;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public int executeUpdate(String sql, String[] columnNames)
/*     */     throws SQLException
/*     */   {
/*     */     try
/*     */     {
/* 778 */       if (this.wrappedStmt != null) {
/* 779 */         return this.wrappedStmt.executeUpdate(sql, columnNames);
/*     */       }
/*     */       
/* 782 */       throw SQLError.createSQLException("Statement already closed", "S1009");
/*     */     }
/*     */     catch (SQLException sqlEx) {
/* 785 */       checkAndFireConnectionError(sqlEx);
/*     */     }
/*     */     
/* 788 */     return -1;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public int executeUpdate(String sql)
/*     */     throws SQLException
/*     */   {
/*     */     try
/*     */     {
/* 800 */       if (this.wrappedStmt != null) {
/* 801 */         return this.wrappedStmt.executeUpdate(sql);
/*     */       }
/*     */       
/* 804 */       throw SQLError.createSQLException("Statement already closed", "S1009");
/*     */     }
/*     */     catch (SQLException sqlEx) {
/* 807 */       checkAndFireConnectionError(sqlEx);
/*     */     }
/*     */     
/* 810 */     return -1;
/*     */   }
/*     */   
/*     */   public void enableStreamingResults() throws SQLException
/*     */   {
/*     */     try
/*     */     {
/* 817 */       if (this.wrappedStmt != null) {
/* 818 */         ((com.mysql.jdbc.Statement)this.wrappedStmt).enableStreamingResults();
/*     */       }
/*     */       else {
/* 821 */         throw SQLError.createSQLException("No operations allowed after statement closed", "S1000");
/*     */       }
/*     */     }
/*     */     catch (SQLException sqlEx)
/*     */     {
/* 826 */       checkAndFireConnectionError(sqlEx);
/*     */     }
/*     */   }
/*     */ }


/* Location:              E:\java\java学习\hutubill\lib\all.jar!\com\mysql\jdbc\jdbc2\optional\StatementWrapper.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version:       0.7.1
 */
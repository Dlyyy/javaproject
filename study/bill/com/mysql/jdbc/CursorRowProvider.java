/*     */ package com.mysql.jdbc;
/*     */ 
/*     */ import java.sql.SQLException;
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
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
/*     */ public class CursorRowProvider
/*     */   implements RowData
/*     */ {
/*     */   private static final int BEFORE_START_OF_ROWS = -1;
/*     */   private List fetchedRows;
/*  49 */   private int currentPositionInEntireResult = -1;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*  55 */   private int currentPositionInFetchedRows = -1;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   private ResultSet owner;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*  65 */   private boolean lastRowFetched = false;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private Field[] fields;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private MysqlIO mysql;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private long statementIdOnServer;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   private ServerPreparedStatement prepStmt;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   private static final int SERVER_STATUS_LAST_ROW_SENT = 128;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*  98 */   private boolean firstFetchCompleted = false;
/*     */   
/* 100 */   private boolean wasEmpty = false;
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
/*     */   public CursorRowProvider(MysqlIO ioChannel, ServerPreparedStatement creatingStatement, Field[] metadata)
/*     */   {
/* 114 */     this.currentPositionInEntireResult = -1;
/* 115 */     this.fields = metadata;
/* 116 */     this.mysql = ioChannel;
/* 117 */     this.statementIdOnServer = creatingStatement.getServerStatementId();
/* 118 */     this.prepStmt = creatingStatement;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean isAfterLast()
/*     */   {
/* 127 */     return (this.lastRowFetched) && (this.currentPositionInFetchedRows > this.fetchedRows.size());
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
/*     */   public Object[] getAt(int ind)
/*     */     throws SQLException
/*     */   {
/* 141 */     notSupported();
/*     */     
/* 143 */     return null;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean isBeforeFirst()
/*     */     throws SQLException
/*     */   {
/* 154 */     return this.currentPositionInEntireResult < 0;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setCurrentRow(int rowNumber)
/*     */     throws SQLException
/*     */   {
/* 166 */     notSupported();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public int getCurrentRowNumber()
/*     */     throws SQLException
/*     */   {
/* 177 */     return this.currentPositionInEntireResult + 1;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean isDynamic()
/*     */   {
/* 189 */     return true;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean isEmpty()
/*     */     throws SQLException
/*     */   {
/* 200 */     return (isBeforeFirst()) && (isAfterLast());
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean isFirst()
/*     */     throws SQLException
/*     */   {
/* 211 */     return this.currentPositionInEntireResult == 0;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean isLast()
/*     */     throws SQLException
/*     */   {
/* 222 */     return (this.lastRowFetched) && (this.currentPositionInFetchedRows == this.fetchedRows.size() - 1);
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
/*     */   public void addRow(byte[][] row)
/*     */     throws SQLException
/*     */   {
/* 236 */     notSupported();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void afterLast()
/*     */     throws SQLException
/*     */   {
/* 246 */     notSupported();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void beforeFirst()
/*     */     throws SQLException
/*     */   {
/* 256 */     notSupported();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void beforeLast()
/*     */     throws SQLException
/*     */   {
/* 266 */     notSupported();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void close()
/*     */     throws SQLException
/*     */   {
/* 277 */     this.fields = null;
/* 278 */     this.owner = null;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean hasNext()
/*     */     throws SQLException
/*     */   {
/* 290 */     if ((this.fetchedRows != null) && (this.fetchedRows.size() == 0)) {
/* 291 */       return false;
/*     */     }
/*     */     
/* 294 */     if ((this.owner != null) && (this.owner.owningStatement != null)) {
/* 295 */       int maxRows = this.owner.owningStatement.maxRows;
/*     */       
/* 297 */       if ((maxRows != -1) && (this.currentPositionInEntireResult + 1 > maxRows)) {
/* 298 */         return false;
/*     */       }
/*     */     }
/*     */     
/* 302 */     if (this.currentPositionInEntireResult != -1)
/*     */     {
/*     */ 
/* 305 */       if (this.currentPositionInFetchedRows < this.fetchedRows.size() - 1)
/* 306 */         return true;
/* 307 */       if ((this.currentPositionInFetchedRows == this.fetchedRows.size()) && (this.lastRowFetched))
/*     */       {
/*     */ 
/* 310 */         return false;
/*     */       }
/*     */       
/* 313 */       fetchMoreRows();
/*     */       
/* 315 */       return this.fetchedRows.size() > 0;
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/* 321 */     fetchMoreRows();
/*     */     
/* 323 */     return this.fetchedRows.size() > 0;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void moveRowRelative(int rows)
/*     */     throws SQLException
/*     */   {
/* 335 */     notSupported();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public Object[] next()
/*     */     throws SQLException
/*     */   {
/* 347 */     this.currentPositionInEntireResult += 1;
/* 348 */     this.currentPositionInFetchedRows += 1;
/*     */     
/*     */ 
/*     */ 
/* 352 */     if ((this.fetchedRows != null) && (this.fetchedRows.size() == 0)) {
/* 353 */       return null;
/*     */     }
/*     */     
/* 356 */     if (this.currentPositionInFetchedRows > this.fetchedRows.size() - 1) {
/* 357 */       fetchMoreRows();
/* 358 */       this.currentPositionInFetchedRows = 0;
/*     */     }
/*     */     
/* 361 */     Object[] row = (Object[])this.fetchedRows.get(this.currentPositionInFetchedRows);
/*     */     
/*     */ 
/* 364 */     return row;
/*     */   }
/*     */   
/*     */ 
/*     */   private void fetchMoreRows()
/*     */     throws SQLException
/*     */   {
/* 371 */     if (this.lastRowFetched) {
/* 372 */       this.fetchedRows = new ArrayList(0);
/* 373 */       return;
/*     */     }
/*     */     
/* 376 */     synchronized (this.owner.connection.getMutex()) {
/* 377 */       boolean oldFirstFetchCompleted = this.firstFetchCompleted;
/*     */       
/* 379 */       if (!this.firstFetchCompleted) {
/* 380 */         this.firstFetchCompleted = true;
/*     */       }
/*     */       
/* 383 */       int numRowsToFetch = this.owner.getFetchSize();
/*     */       
/* 385 */       if (numRowsToFetch == 0) {
/* 386 */         numRowsToFetch = this.prepStmt.getFetchSize();
/*     */       }
/*     */       
/* 389 */       if (numRowsToFetch == Integer.MIN_VALUE)
/*     */       {
/*     */ 
/*     */ 
/* 393 */         numRowsToFetch = 1;
/*     */       }
/*     */       
/* 396 */       this.fetchedRows = this.mysql.fetchRowsViaCursor(this.fetchedRows, this.statementIdOnServer, this.fields, numRowsToFetch);
/*     */       
/* 398 */       this.currentPositionInFetchedRows = -1;
/*     */       
/* 400 */       if ((this.mysql.getServerStatus() & 0x80) != 0) {
/* 401 */         this.lastRowFetched = true;
/*     */         
/* 403 */         if ((!oldFirstFetchCompleted) && (this.fetchedRows.size() == 0)) {
/* 404 */           this.wasEmpty = true;
/*     */         }
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void removeRow(int ind)
/*     */     throws SQLException
/*     */   {
/* 419 */     notSupported();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public int size()
/*     */   {
/* 428 */     return -1;
/*     */   }
/*     */   
/*     */   private void nextRecord() throws SQLException
/*     */   {}
/*     */   
/*     */   private void notSupported() throws SQLException
/*     */   {
/* 436 */     throw new OperationNotSupportedException();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setOwner(ResultSet rs)
/*     */   {
/* 445 */     this.owner = rs;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public ResultSet getOwner()
/*     */   {
/* 454 */     return this.owner;
/*     */   }
/*     */   
/*     */   public boolean wasEmpty() {
/* 458 */     return this.wasEmpty;
/*     */   }
/*     */ }


/* Location:              E:\java\java学习\hutubill\lib\all.jar!\com\mysql\jdbc\CursorRowProvider.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version:       0.7.1
 */
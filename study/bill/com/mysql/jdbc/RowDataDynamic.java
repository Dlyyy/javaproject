/*     */ package com.mysql.jdbc;
/*     */ 
/*     */ import com.mysql.jdbc.profiler.ProfileEventSink;
/*     */ import com.mysql.jdbc.profiler.ProfilerEvent;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class RowDataDynamic
/*     */   implements RowData
/*     */ {
/*     */   private int columnCount;
/*     */   private Field[] fields;
/*     */   
/*     */   class OperationNotSupportedException
/*     */     extends SQLException
/*     */   {
/*     */     OperationNotSupportedException()
/*     */     {
/*  44 */       super("S1009");
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*  53 */   private int index = -1;
/*     */   
/*     */   private MysqlIO io;
/*     */   
/*  57 */   private boolean isAfterEnd = false;
/*     */   
/*  59 */   private boolean isAtEnd = false;
/*     */   
/*  61 */   private boolean isBinaryEncoded = false;
/*     */   
/*     */   private Object[] nextRow;
/*     */   
/*     */   private ResultSet owner;
/*     */   
/*  67 */   private boolean streamerClosed = false;
/*     */   
/*  69 */   private boolean wasEmpty = false;
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
/*     */   public RowDataDynamic(MysqlIO io, int colCount, Field[] fields, boolean isBinaryEncoded)
/*     */     throws SQLException
/*     */   {
/*  90 */     this.io = io;
/*  91 */     this.columnCount = colCount;
/*  92 */     this.isBinaryEncoded = isBinaryEncoded;
/*  93 */     this.fields = fields;
/*  94 */     nextRecord();
/*     */   }
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
/* 106 */     notSupported();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void afterLast()
/*     */     throws SQLException
/*     */   {
/* 116 */     notSupported();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void beforeFirst()
/*     */     throws SQLException
/*     */   {
/* 126 */     notSupported();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void beforeLast()
/*     */     throws SQLException
/*     */   {
/* 136 */     notSupported();
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
/* 147 */     boolean hadMore = false;
/* 148 */     int howMuchMore = 0;
/*     */     
/*     */ 
/* 151 */     while (hasNext()) {
/* 152 */       next();
/* 153 */       hadMore = true;
/* 154 */       howMuchMore++;
/*     */       
/* 156 */       if (howMuchMore % 100 == 0) {
/* 157 */         Thread.yield();
/*     */       }
/*     */     }
/*     */     
/* 161 */     if (this.owner != null) {
/* 162 */       Connection conn = this.owner.connection;
/*     */       
/* 164 */       if ((conn != null) && (conn.getUseUsageAdvisor()) && 
/* 165 */         (hadMore))
/*     */       {
/* 167 */         ProfileEventSink eventSink = ProfileEventSink.getInstance(conn);
/*     */         
/*     */ 
/* 170 */         eventSink.consumeEvent(new ProfilerEvent((byte)0, "", this.owner.owningStatement == null ? "N/A" : this.owner.owningStatement.currentCatalog, this.owner.connectionId, this.owner.owningStatement == null ? -1 : this.owner.owningStatement.getId(), -1, System.currentTimeMillis(), 0L, Constants.MILLIS_I18N, null, null, Messages.getString("RowDataDynamic.2") + howMuchMore + Messages.getString("RowDataDynamic.3") + Messages.getString("RowDataDynamic.4") + Messages.getString("RowDataDynamic.5") + Messages.getString("RowDataDynamic.6") + this.owner.pointOfOrigin));
/*     */       }
/*     */     }
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
/* 200 */     this.fields = null;
/* 201 */     this.owner = null;
/*     */   }
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
/* 214 */     notSupported();
/*     */     
/* 216 */     return null;
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
/* 227 */     notSupported();
/*     */     
/* 229 */     return -1;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public ResultSet getOwner()
/*     */   {
/* 236 */     return this.owner;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean hasNext()
/*     */     throws SQLException
/*     */   {
/* 247 */     boolean hasNext = this.nextRow != null;
/*     */     
/* 249 */     if ((!hasNext) && (!this.streamerClosed)) {
/* 250 */       this.io.closeStreamer(this);
/* 251 */       this.streamerClosed = true;
/*     */     }
/*     */     
/* 254 */     return hasNext;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean isAfterLast()
/*     */     throws SQLException
/*     */   {
/* 265 */     return this.isAfterEnd;
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
/* 276 */     return this.index < 0;
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
/* 288 */     return true;
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
/* 299 */     notSupported();
/*     */     
/* 301 */     return false;
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
/* 312 */     notSupported();
/*     */     
/* 314 */     return false;
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
/* 325 */     notSupported();
/*     */     
/* 327 */     return false;
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
/* 339 */     notSupported();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public Object[] next()
/*     */     throws SQLException
/*     */   {
/* 350 */     if (this.index != Integer.MAX_VALUE) {
/* 351 */       this.index += 1;
/*     */     }
/*     */     
/* 354 */     Object[] ret = this.nextRow;
/* 355 */     nextRecord();
/*     */     
/* 357 */     return ret;
/*     */   }
/*     */   
/*     */   private void nextRecord() throws SQLException
/*     */   {
/*     */     try
/*     */     {
/* 364 */       if (!this.isAtEnd)
/*     */       {
/* 366 */         this.nextRow = this.io.nextRow(this.fields, this.columnCount, this.isBinaryEncoded, 1007);
/*     */         
/*     */ 
/*     */ 
/* 370 */         if (this.nextRow == null) {
/* 371 */           this.isAtEnd = true;
/*     */           
/* 373 */           if (this.index == -1) {
/* 374 */             this.wasEmpty = true;
/*     */           }
/*     */         }
/*     */       } else {
/* 378 */         this.isAfterEnd = true;
/*     */       }
/*     */     }
/*     */     catch (CommunicationsException comEx) {
/* 382 */       comEx.setWasStreamingResults();
/*     */       
/* 384 */       throw comEx;
/*     */     }
/*     */     catch (SQLException sqlEx) {
/* 387 */       throw sqlEx;
/*     */     } catch (Exception ex) {
/* 389 */       String exceptionType = ex.getClass().getName();
/* 390 */       String exceptionMessage = ex.getMessage();
/*     */       
/* 392 */       exceptionMessage = exceptionMessage + Messages.getString("RowDataDynamic.7");
/* 393 */       exceptionMessage = exceptionMessage + Util.stackTraceToString(ex);
/*     */       
/* 395 */       throw new SQLException(Messages.getString("RowDataDynamic.8") + exceptionType + Messages.getString("RowDataDynamic.9") + exceptionMessage, "S1000");
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */   private void notSupported()
/*     */     throws SQLException
/*     */   {
/* 403 */     throw new OperationNotSupportedException();
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
/* 415 */     notSupported();
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
/*     */   public void setCurrentRow(int rowNumber)
/*     */     throws SQLException
/*     */   {
/* 430 */     notSupported();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public void setOwner(ResultSet rs)
/*     */   {
/* 437 */     this.owner = rs;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public int size()
/*     */   {
/* 446 */     return -1;
/*     */   }
/*     */   
/*     */   public boolean wasEmpty() {
/* 450 */     return this.wasEmpty;
/*     */   }
/*     */ }


/* Location:              E:\java\java学习\hutubill\lib\all.jar!\com\mysql\jdbc\RowDataDynamic.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version:       0.7.1
 */
/*     */ package com.mysql.jdbc.jdbc2.optional;
/*     */ 
/*     */ import com.mysql.jdbc.SQLError;
/*     */ import java.io.InputStream;
/*     */ import java.io.Reader;
/*     */ import java.math.BigDecimal;
/*     */ import java.net.URL;
/*     */ import java.sql.Array;
/*     */ import java.sql.Blob;
/*     */ import java.sql.Clob;
/*     */ import java.sql.Date;
/*     */ import java.sql.ParameterMetaData;
/*     */ import java.sql.PreparedStatement;
/*     */ import java.sql.Ref;
/*     */ import java.sql.ResultSetMetaData;
/*     */ import java.sql.SQLException;
/*     */ import java.sql.Time;
/*     */ import java.sql.Timestamp;
/*     */ import java.util.Calendar;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class PreparedStatementWrapper
/*     */   extends StatementWrapper
/*     */   implements PreparedStatement
/*     */ {
/*     */   PreparedStatementWrapper(ConnectionWrapper c, MysqlPooledConnection conn, PreparedStatement toWrap)
/*     */   {
/*  64 */     super(c, conn, toWrap);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public void setArray(int parameterIndex, Array x)
/*     */     throws SQLException
/*     */   {
/*     */     try
/*     */     {
/*  74 */       if (this.wrappedStmt != null) {
/*  75 */         ((PreparedStatement)this.wrappedStmt).setArray(parameterIndex, x);
/*     */       }
/*     */       else {
/*  78 */         throw SQLError.createSQLException("No operations allowed after statement closed", "S1000");
/*     */       }
/*     */     }
/*     */     catch (SQLException sqlEx)
/*     */     {
/*  83 */       checkAndFireConnectionError(sqlEx);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setAsciiStream(int parameterIndex, InputStream x, int length)
/*     */     throws SQLException
/*     */   {
/*     */     try
/*     */     {
/*  96 */       if (this.wrappedStmt != null) {
/*  97 */         ((PreparedStatement)this.wrappedStmt).setAsciiStream(parameterIndex, x, length);
/*     */       }
/*     */       else {
/* 100 */         throw SQLError.createSQLException("No operations allowed after statement closed", "S1000");
/*     */       }
/*     */     }
/*     */     catch (SQLException sqlEx)
/*     */     {
/* 105 */       checkAndFireConnectionError(sqlEx);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setBigDecimal(int parameterIndex, BigDecimal x)
/*     */     throws SQLException
/*     */   {
/*     */     try
/*     */     {
/* 117 */       if (this.wrappedStmt != null) {
/* 118 */         ((PreparedStatement)this.wrappedStmt).setBigDecimal(parameterIndex, x);
/*     */       }
/*     */       else {
/* 121 */         throw SQLError.createSQLException("No operations allowed after statement closed", "S1000");
/*     */       }
/*     */     }
/*     */     catch (SQLException sqlEx)
/*     */     {
/* 126 */       checkAndFireConnectionError(sqlEx);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setBinaryStream(int parameterIndex, InputStream x, int length)
/*     */     throws SQLException
/*     */   {
/*     */     try
/*     */     {
/* 139 */       if (this.wrappedStmt != null) {
/* 140 */         ((PreparedStatement)this.wrappedStmt).setBinaryStream(parameterIndex, x, length);
/*     */       }
/*     */       else {
/* 143 */         throw SQLError.createSQLException("No operations allowed after statement closed", "S1000");
/*     */       }
/*     */     }
/*     */     catch (SQLException sqlEx)
/*     */     {
/* 148 */       checkAndFireConnectionError(sqlEx);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public void setBlob(int parameterIndex, Blob x)
/*     */     throws SQLException
/*     */   {
/*     */     try
/*     */     {
/* 159 */       if (this.wrappedStmt != null) {
/* 160 */         ((PreparedStatement)this.wrappedStmt).setBlob(parameterIndex, x);
/*     */       }
/*     */       else {
/* 163 */         throw SQLError.createSQLException("No operations allowed after statement closed", "S1000");
/*     */       }
/*     */     }
/*     */     catch (SQLException sqlEx)
/*     */     {
/* 168 */       checkAndFireConnectionError(sqlEx);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public void setBoolean(int parameterIndex, boolean x)
/*     */     throws SQLException
/*     */   {
/*     */     try
/*     */     {
/* 179 */       if (this.wrappedStmt != null) {
/* 180 */         ((PreparedStatement)this.wrappedStmt).setBoolean(parameterIndex, x);
/*     */       }
/*     */       else {
/* 183 */         throw SQLError.createSQLException("No operations allowed after statement closed", "S1000");
/*     */       }
/*     */     }
/*     */     catch (SQLException sqlEx)
/*     */     {
/* 188 */       checkAndFireConnectionError(sqlEx);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public void setByte(int parameterIndex, byte x)
/*     */     throws SQLException
/*     */   {
/*     */     try
/*     */     {
/* 199 */       if (this.wrappedStmt != null) {
/* 200 */         ((PreparedStatement)this.wrappedStmt).setByte(parameterIndex, x);
/*     */       }
/*     */       else {
/* 203 */         throw SQLError.createSQLException("No operations allowed after statement closed", "S1000");
/*     */       }
/*     */     }
/*     */     catch (SQLException sqlEx)
/*     */     {
/* 208 */       checkAndFireConnectionError(sqlEx);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public void setBytes(int parameterIndex, byte[] x)
/*     */     throws SQLException
/*     */   {
/*     */     try
/*     */     {
/* 219 */       if (this.wrappedStmt != null) {
/* 220 */         ((PreparedStatement)this.wrappedStmt).setBytes(parameterIndex, x);
/*     */       }
/*     */       else {
/* 223 */         throw SQLError.createSQLException("No operations allowed after statement closed", "S1000");
/*     */       }
/*     */     }
/*     */     catch (SQLException sqlEx)
/*     */     {
/* 228 */       checkAndFireConnectionError(sqlEx);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setCharacterStream(int parameterIndex, Reader reader, int length)
/*     */     throws SQLException
/*     */   {
/*     */     try
/*     */     {
/* 241 */       if (this.wrappedStmt != null) {
/* 242 */         ((PreparedStatement)this.wrappedStmt).setCharacterStream(parameterIndex, reader, length);
/*     */       }
/*     */       else {
/* 245 */         throw SQLError.createSQLException("No operations allowed after statement closed", "S1000");
/*     */       }
/*     */     }
/*     */     catch (SQLException sqlEx)
/*     */     {
/* 250 */       checkAndFireConnectionError(sqlEx);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public void setClob(int parameterIndex, Clob x)
/*     */     throws SQLException
/*     */   {
/*     */     try
/*     */     {
/* 261 */       if (this.wrappedStmt != null) {
/* 262 */         ((PreparedStatement)this.wrappedStmt).setClob(parameterIndex, x);
/*     */       }
/*     */       else {
/* 265 */         throw SQLError.createSQLException("No operations allowed after statement closed", "S1000");
/*     */       }
/*     */     }
/*     */     catch (SQLException sqlEx)
/*     */     {
/* 270 */       checkAndFireConnectionError(sqlEx);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public void setDate(int parameterIndex, Date x)
/*     */     throws SQLException
/*     */   {
/*     */     try
/*     */     {
/* 281 */       if (this.wrappedStmt != null) {
/* 282 */         ((PreparedStatement)this.wrappedStmt).setDate(parameterIndex, x);
/*     */       }
/*     */       else {
/* 285 */         throw SQLError.createSQLException("No operations allowed after statement closed", "S1000");
/*     */       }
/*     */     }
/*     */     catch (SQLException sqlEx)
/*     */     {
/* 290 */       checkAndFireConnectionError(sqlEx);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setDate(int parameterIndex, Date x, Calendar cal)
/*     */     throws SQLException
/*     */   {
/*     */     try
/*     */     {
/* 303 */       if (this.wrappedStmt != null) {
/* 304 */         ((PreparedStatement)this.wrappedStmt).setDate(parameterIndex, x, cal);
/*     */       }
/*     */       else {
/* 307 */         throw SQLError.createSQLException("No operations allowed after statement closed", "S1000");
/*     */       }
/*     */     }
/*     */     catch (SQLException sqlEx)
/*     */     {
/* 312 */       checkAndFireConnectionError(sqlEx);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public void setDouble(int parameterIndex, double x)
/*     */     throws SQLException
/*     */   {
/*     */     try
/*     */     {
/* 323 */       if (this.wrappedStmt != null) {
/* 324 */         ((PreparedStatement)this.wrappedStmt).setDouble(parameterIndex, x);
/*     */       }
/*     */       else {
/* 327 */         throw SQLError.createSQLException("No operations allowed after statement closed", "S1000");
/*     */       }
/*     */     }
/*     */     catch (SQLException sqlEx)
/*     */     {
/* 332 */       checkAndFireConnectionError(sqlEx);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public void setFloat(int parameterIndex, float x)
/*     */     throws SQLException
/*     */   {
/*     */     try
/*     */     {
/* 343 */       if (this.wrappedStmt != null) {
/* 344 */         ((PreparedStatement)this.wrappedStmt).setFloat(parameterIndex, x);
/*     */       }
/*     */       else {
/* 347 */         throw SQLError.createSQLException("No operations allowed after statement closed", "S1000");
/*     */       }
/*     */     }
/*     */     catch (SQLException sqlEx)
/*     */     {
/* 352 */       checkAndFireConnectionError(sqlEx);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public void setInt(int parameterIndex, int x)
/*     */     throws SQLException
/*     */   {
/*     */     try
/*     */     {
/* 363 */       if (this.wrappedStmt != null) {
/* 364 */         ((PreparedStatement)this.wrappedStmt).setInt(parameterIndex, x);
/*     */       }
/*     */       else {
/* 367 */         throw SQLError.createSQLException("No operations allowed after statement closed", "S1000");
/*     */       }
/*     */     }
/*     */     catch (SQLException sqlEx)
/*     */     {
/* 372 */       checkAndFireConnectionError(sqlEx);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public void setLong(int parameterIndex, long x)
/*     */     throws SQLException
/*     */   {
/*     */     try
/*     */     {
/* 383 */       if (this.wrappedStmt != null) {
/* 384 */         ((PreparedStatement)this.wrappedStmt).setLong(parameterIndex, x);
/*     */       }
/*     */       else {
/* 387 */         throw SQLError.createSQLException("No operations allowed after statement closed", "S1000");
/*     */       }
/*     */     }
/*     */     catch (SQLException sqlEx)
/*     */     {
/* 392 */       checkAndFireConnectionError(sqlEx);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public ResultSetMetaData getMetaData()
/*     */     throws SQLException
/*     */   {
/*     */     try
/*     */     {
/* 403 */       if (this.wrappedStmt != null) {
/* 404 */         return ((PreparedStatement)this.wrappedStmt).getMetaData();
/*     */       }
/*     */       
/* 407 */       throw SQLError.createSQLException("No operations allowed after statement closed", "S1000");
/*     */     }
/*     */     catch (SQLException sqlEx)
/*     */     {
/* 411 */       checkAndFireConnectionError(sqlEx);
/*     */     }
/*     */     
/* 414 */     return null;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public void setNull(int parameterIndex, int sqlType)
/*     */     throws SQLException
/*     */   {
/*     */     try
/*     */     {
/* 424 */       if (this.wrappedStmt != null) {
/* 425 */         ((PreparedStatement)this.wrappedStmt).setNull(parameterIndex, sqlType);
/*     */       }
/*     */       else {
/* 428 */         throw SQLError.createSQLException("No operations allowed after statement closed", "S1000");
/*     */       }
/*     */     }
/*     */     catch (SQLException sqlEx)
/*     */     {
/* 433 */       checkAndFireConnectionError(sqlEx);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setNull(int parameterIndex, int sqlType, String typeName)
/*     */     throws SQLException
/*     */   {
/*     */     try
/*     */     {
/* 445 */       if (this.wrappedStmt != null) {
/* 446 */         ((PreparedStatement)this.wrappedStmt).setNull(parameterIndex, sqlType, typeName);
/*     */       }
/*     */       else {
/* 449 */         throw SQLError.createSQLException("No operations allowed after statement closed", "S1000");
/*     */       }
/*     */     }
/*     */     catch (SQLException sqlEx)
/*     */     {
/* 454 */       checkAndFireConnectionError(sqlEx);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public void setObject(int parameterIndex, Object x)
/*     */     throws SQLException
/*     */   {
/*     */     try
/*     */     {
/* 465 */       if (this.wrappedStmt != null) {
/* 466 */         ((PreparedStatement)this.wrappedStmt).setObject(parameterIndex, x);
/*     */       }
/*     */       else {
/* 469 */         throw SQLError.createSQLException("No operations allowed after statement closed", "S1000");
/*     */       }
/*     */     }
/*     */     catch (SQLException sqlEx)
/*     */     {
/* 474 */       checkAndFireConnectionError(sqlEx);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setObject(int parameterIndex, Object x, int targetSqlType)
/*     */     throws SQLException
/*     */   {
/*     */     try
/*     */     {
/* 486 */       if (this.wrappedStmt != null) {
/* 487 */         ((PreparedStatement)this.wrappedStmt).setObject(parameterIndex, x, targetSqlType);
/*     */       }
/*     */       else {
/* 490 */         throw SQLError.createSQLException("No operations allowed after statement closed", "S1000");
/*     */       }
/*     */     }
/*     */     catch (SQLException sqlEx)
/*     */     {
/* 495 */       checkAndFireConnectionError(sqlEx);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setObject(int parameterIndex, Object x, int targetSqlType, int scale)
/*     */     throws SQLException
/*     */   {
/*     */     try
/*     */     {
/* 508 */       if (this.wrappedStmt != null) {
/* 509 */         ((PreparedStatement)this.wrappedStmt).setObject(parameterIndex, x, targetSqlType, scale);
/*     */       }
/*     */       else {
/* 512 */         throw SQLError.createSQLException("No operations allowed after statement closed", "S1000");
/*     */       }
/*     */     }
/*     */     catch (SQLException sqlEx)
/*     */     {
/* 517 */       checkAndFireConnectionError(sqlEx);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public ParameterMetaData getParameterMetaData()
/*     */     throws SQLException
/*     */   {
/*     */     try
/*     */     {
/* 528 */       if (this.wrappedStmt != null) {
/* 529 */         return ((PreparedStatement)this.wrappedStmt).getParameterMetaData();
/*     */       }
/*     */       
/*     */ 
/* 533 */       throw SQLError.createSQLException("No operations allowed after statement closed", "S1000");
/*     */     }
/*     */     catch (SQLException sqlEx)
/*     */     {
/* 537 */       checkAndFireConnectionError(sqlEx);
/*     */     }
/*     */     
/* 540 */     return null;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public void setRef(int parameterIndex, Ref x)
/*     */     throws SQLException
/*     */   {
/*     */     try
/*     */     {
/* 550 */       if (this.wrappedStmt != null) {
/* 551 */         ((PreparedStatement)this.wrappedStmt).setRef(parameterIndex, x);
/*     */       }
/*     */       else {
/* 554 */         throw SQLError.createSQLException("No operations allowed after statement closed", "S1000");
/*     */       }
/*     */     }
/*     */     catch (SQLException sqlEx)
/*     */     {
/* 559 */       checkAndFireConnectionError(sqlEx);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public void setShort(int parameterIndex, short x)
/*     */     throws SQLException
/*     */   {
/*     */     try
/*     */     {
/* 570 */       if (this.wrappedStmt != null) {
/* 571 */         ((PreparedStatement)this.wrappedStmt).setShort(parameterIndex, x);
/*     */       }
/*     */       else {
/* 574 */         throw SQLError.createSQLException("No operations allowed after statement closed", "S1000");
/*     */       }
/*     */     }
/*     */     catch (SQLException sqlEx)
/*     */     {
/* 579 */       checkAndFireConnectionError(sqlEx);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public void setString(int parameterIndex, String x)
/*     */     throws SQLException
/*     */   {
/*     */     try
/*     */     {
/* 590 */       if (this.wrappedStmt != null) {
/* 591 */         ((PreparedStatement)this.wrappedStmt).setString(parameterIndex, x);
/*     */       }
/*     */       else {
/* 594 */         throw SQLError.createSQLException("No operations allowed after statement closed", "S1000");
/*     */       }
/*     */     }
/*     */     catch (SQLException sqlEx)
/*     */     {
/* 599 */       checkAndFireConnectionError(sqlEx);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public void setTime(int parameterIndex, Time x)
/*     */     throws SQLException
/*     */   {
/*     */     try
/*     */     {
/* 610 */       if (this.wrappedStmt != null) {
/* 611 */         ((PreparedStatement)this.wrappedStmt).setTime(parameterIndex, x);
/*     */       }
/*     */       else {
/* 614 */         throw SQLError.createSQLException("No operations allowed after statement closed", "S1000");
/*     */       }
/*     */     }
/*     */     catch (SQLException sqlEx)
/*     */     {
/* 619 */       checkAndFireConnectionError(sqlEx);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setTime(int parameterIndex, Time x, Calendar cal)
/*     */     throws SQLException
/*     */   {
/*     */     try
/*     */     {
/* 632 */       if (this.wrappedStmt != null) {
/* 633 */         ((PreparedStatement)this.wrappedStmt).setTime(parameterIndex, x, cal);
/*     */       }
/*     */       else {
/* 636 */         throw SQLError.createSQLException("No operations allowed after statement closed", "S1000");
/*     */       }
/*     */     }
/*     */     catch (SQLException sqlEx)
/*     */     {
/* 641 */       checkAndFireConnectionError(sqlEx);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setTimestamp(int parameterIndex, Timestamp x)
/*     */     throws SQLException
/*     */   {
/*     */     try
/*     */     {
/* 653 */       if (this.wrappedStmt != null) {
/* 654 */         ((PreparedStatement)this.wrappedStmt).setTimestamp(parameterIndex, x);
/*     */       }
/*     */       else {
/* 657 */         throw SQLError.createSQLException("No operations allowed after statement closed", "S1000");
/*     */       }
/*     */     }
/*     */     catch (SQLException sqlEx)
/*     */     {
/* 662 */       checkAndFireConnectionError(sqlEx);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setTimestamp(int parameterIndex, Timestamp x, Calendar cal)
/*     */     throws SQLException
/*     */   {
/*     */     try
/*     */     {
/* 675 */       if (this.wrappedStmt != null) {
/* 676 */         ((PreparedStatement)this.wrappedStmt).setTimestamp(parameterIndex, x, cal);
/*     */       }
/*     */       else {
/* 679 */         throw SQLError.createSQLException("No operations allowed after statement closed", "S1000");
/*     */       }
/*     */     }
/*     */     catch (SQLException sqlEx)
/*     */     {
/* 684 */       checkAndFireConnectionError(sqlEx);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public void setURL(int parameterIndex, URL x)
/*     */     throws SQLException
/*     */   {
/*     */     try
/*     */     {
/* 695 */       if (this.wrappedStmt != null) {
/* 696 */         ((PreparedStatement)this.wrappedStmt).setURL(parameterIndex, x);
/*     */       }
/*     */       else {
/* 699 */         throw SQLError.createSQLException("No operations allowed after statement closed", "S1000");
/*     */       }
/*     */     }
/*     */     catch (SQLException sqlEx)
/*     */     {
/* 704 */       checkAndFireConnectionError(sqlEx);
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   /**
/*     */    * @deprecated
/*     */    */
/*     */   public void setUnicodeStream(int parameterIndex, InputStream x, int length)
/*     */     throws SQLException
/*     */   {
/*     */     try
/*     */     {
/* 728 */       if (this.wrappedStmt != null) {
/* 729 */         ((PreparedStatement)this.wrappedStmt).setUnicodeStream(parameterIndex, x, length);
/*     */       }
/*     */       else {
/* 732 */         throw SQLError.createSQLException("No operations allowed after statement closed", "S1000");
/*     */       }
/*     */     }
/*     */     catch (SQLException sqlEx)
/*     */     {
/* 737 */       checkAndFireConnectionError(sqlEx);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public void addBatch()
/*     */     throws SQLException
/*     */   {
/*     */     try
/*     */     {
/* 748 */       if (this.wrappedStmt != null) {
/* 749 */         ((PreparedStatement)this.wrappedStmt).addBatch();
/*     */       } else {
/* 751 */         throw SQLError.createSQLException("No operations allowed after statement closed", "S1000");
/*     */       }
/*     */     }
/*     */     catch (SQLException sqlEx)
/*     */     {
/* 756 */       checkAndFireConnectionError(sqlEx);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public void clearParameters()
/*     */     throws SQLException
/*     */   {
/*     */     try
/*     */     {
/* 767 */       if (this.wrappedStmt != null) {
/* 768 */         ((PreparedStatement)this.wrappedStmt).clearParameters();
/*     */       } else {
/* 770 */         throw SQLError.createSQLException("No operations allowed after statement closed", "S1000");
/*     */       }
/*     */     }
/*     */     catch (SQLException sqlEx)
/*     */     {
/* 775 */       checkAndFireConnectionError(sqlEx);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public boolean execute()
/*     */     throws SQLException
/*     */   {
/*     */     try
/*     */     {
/* 786 */       if (this.wrappedStmt != null) {
/* 787 */         return ((PreparedStatement)this.wrappedStmt).execute();
/*     */       }
/*     */       
/* 790 */       throw SQLError.createSQLException("No operations allowed after statement closed", "S1000");
/*     */     }
/*     */     catch (SQLException sqlEx)
/*     */     {
/* 794 */       checkAndFireConnectionError(sqlEx);
/*     */     }
/*     */     
/* 797 */     return false;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public java.sql.ResultSet executeQuery()
/*     */     throws SQLException
/*     */   {
/*     */     try
/*     */     {
/* 810 */       if (this.wrappedStmt != null) {
/* 811 */         java.sql.ResultSet rs = ((PreparedStatement)this.wrappedStmt).executeQuery();
/*     */         
/*     */ 
/* 814 */         ((com.mysql.jdbc.ResultSet)rs).setWrapperStatement(this);
/*     */         
/* 816 */         return rs;
/*     */       }
/*     */       
/* 819 */       throw SQLError.createSQLException("No operations allowed after statement closed", "S1000");
/*     */     }
/*     */     catch (SQLException sqlEx)
/*     */     {
/* 823 */       checkAndFireConnectionError(sqlEx);
/*     */     }
/*     */     
/* 826 */     return null;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public int executeUpdate()
/*     */     throws SQLException
/*     */   {
/*     */     try
/*     */     {
/* 839 */       if (this.wrappedStmt != null) {
/* 840 */         return ((PreparedStatement)this.wrappedStmt).executeUpdate();
/*     */       }
/*     */       
/* 843 */       throw SQLError.createSQLException("No operations allowed after statement closed", "S1000");
/*     */     }
/*     */     catch (SQLException sqlEx)
/*     */     {
/* 847 */       checkAndFireConnectionError(sqlEx);
/*     */     }
/*     */     
/* 850 */     return -1;
/*     */   }
/*     */ }


/* Location:              E:\java\java学习\hutubill\lib\all.jar!\com\mysql\jdbc\jdbc2\optional\PreparedStatementWrapper.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version:       0.7.1
 */
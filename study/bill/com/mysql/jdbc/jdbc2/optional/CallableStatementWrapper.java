/*      */ package com.mysql.jdbc.jdbc2.optional;
/*      */ 
/*      */ import com.mysql.jdbc.SQLError;
/*      */ import java.io.InputStream;
/*      */ import java.io.Reader;
/*      */ import java.math.BigDecimal;
/*      */ import java.net.URL;
/*      */ import java.sql.Array;
/*      */ import java.sql.Blob;
/*      */ import java.sql.CallableStatement;
/*      */ import java.sql.Clob;
/*      */ import java.sql.Date;
/*      */ import java.sql.Ref;
/*      */ import java.sql.SQLException;
/*      */ import java.sql.Time;
/*      */ import java.sql.Timestamp;
/*      */ import java.util.Calendar;
/*      */ import java.util.Map;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ public class CallableStatementWrapper
/*      */   extends PreparedStatementWrapper
/*      */   implements CallableStatement
/*      */ {
/*      */   public CallableStatementWrapper(ConnectionWrapper c, MysqlPooledConnection conn, CallableStatement toWrap)
/*      */   {
/*   62 */     super(c, conn, toWrap);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public void registerOutParameter(int parameterIndex, int sqlType)
/*      */     throws SQLException
/*      */   {
/*      */     try
/*      */     {
/*   73 */       if (this.wrappedStmt != null) {
/*   74 */         ((CallableStatement)this.wrappedStmt).registerOutParameter(parameterIndex, sqlType);
/*      */       }
/*      */       else {
/*   77 */         throw SQLError.createSQLException("No operations allowed after statement closed", "S1000");
/*      */       }
/*      */     }
/*      */     catch (SQLException sqlEx)
/*      */     {
/*   82 */       checkAndFireConnectionError(sqlEx);
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public void registerOutParameter(int parameterIndex, int sqlType, int scale)
/*      */     throws SQLException
/*      */   {
/*      */     try
/*      */     {
/*   94 */       if (this.wrappedStmt != null) {
/*   95 */         ((CallableStatement)this.wrappedStmt).registerOutParameter(parameterIndex, sqlType, scale);
/*      */       }
/*      */       else {
/*   98 */         throw SQLError.createSQLException("No operations allowed after statement closed", "S1000");
/*      */       }
/*      */     }
/*      */     catch (SQLException sqlEx)
/*      */     {
/*  103 */       checkAndFireConnectionError(sqlEx);
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public boolean wasNull()
/*      */     throws SQLException
/*      */   {
/*      */     try
/*      */     {
/*  114 */       if (this.wrappedStmt != null) {
/*  115 */         return ((CallableStatement)this.wrappedStmt).wasNull();
/*      */       }
/*  117 */       throw SQLError.createSQLException("No operations allowed after statement closed", "S1000");
/*      */ 
/*      */     }
/*      */     catch (SQLException sqlEx)
/*      */     {
/*  122 */       checkAndFireConnectionError(sqlEx);
/*      */     }
/*      */     
/*  125 */     return false;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public String getString(int parameterIndex)
/*      */     throws SQLException
/*      */   {
/*      */     try
/*      */     {
/*  135 */       if (this.wrappedStmt != null) {
/*  136 */         return ((CallableStatement)this.wrappedStmt).getString(parameterIndex);
/*      */       }
/*      */       
/*  139 */       throw SQLError.createSQLException("No operations allowed after statement closed", "S1000");
/*      */ 
/*      */     }
/*      */     catch (SQLException sqlEx)
/*      */     {
/*  144 */       checkAndFireConnectionError(sqlEx);
/*      */     }
/*  146 */     return null;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public boolean getBoolean(int parameterIndex)
/*      */     throws SQLException
/*      */   {
/*      */     try
/*      */     {
/*  156 */       if (this.wrappedStmt != null) {
/*  157 */         return ((CallableStatement)this.wrappedStmt).getBoolean(parameterIndex);
/*      */       }
/*      */       
/*  160 */       throw SQLError.createSQLException("No operations allowed after statement closed", "S1000");
/*      */ 
/*      */     }
/*      */     catch (SQLException sqlEx)
/*      */     {
/*  165 */       checkAndFireConnectionError(sqlEx);
/*      */     }
/*      */     
/*  168 */     return false;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public byte getByte(int parameterIndex)
/*      */     throws SQLException
/*      */   {
/*      */     try
/*      */     {
/*  178 */       if (this.wrappedStmt != null) {
/*  179 */         return ((CallableStatement)this.wrappedStmt).getByte(parameterIndex);
/*      */       }
/*      */       
/*  182 */       throw SQLError.createSQLException("No operations allowed after statement closed", "S1000");
/*      */ 
/*      */     }
/*      */     catch (SQLException sqlEx)
/*      */     {
/*  187 */       checkAndFireConnectionError(sqlEx);
/*      */     }
/*      */     
/*  190 */     return 0;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public short getShort(int parameterIndex)
/*      */     throws SQLException
/*      */   {
/*      */     try
/*      */     {
/*  200 */       if (this.wrappedStmt != null) {
/*  201 */         return ((CallableStatement)this.wrappedStmt).getShort(parameterIndex);
/*      */       }
/*      */       
/*  204 */       throw SQLError.createSQLException("No operations allowed after statement closed", "S1000");
/*      */ 
/*      */     }
/*      */     catch (SQLException sqlEx)
/*      */     {
/*  209 */       checkAndFireConnectionError(sqlEx);
/*      */     }
/*      */     
/*  212 */     return 0;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public int getInt(int parameterIndex)
/*      */     throws SQLException
/*      */   {
/*      */     try
/*      */     {
/*  222 */       if (this.wrappedStmt != null) {
/*  223 */         return ((CallableStatement)this.wrappedStmt).getInt(parameterIndex);
/*      */       }
/*      */       
/*  226 */       throw SQLError.createSQLException("No operations allowed after statement closed", "S1000");
/*      */ 
/*      */     }
/*      */     catch (SQLException sqlEx)
/*      */     {
/*  231 */       checkAndFireConnectionError(sqlEx);
/*      */     }
/*      */     
/*  234 */     return 0;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public long getLong(int parameterIndex)
/*      */     throws SQLException
/*      */   {
/*      */     try
/*      */     {
/*  244 */       if (this.wrappedStmt != null) {
/*  245 */         return ((CallableStatement)this.wrappedStmt).getLong(parameterIndex);
/*      */       }
/*      */       
/*  248 */       throw SQLError.createSQLException("No operations allowed after statement closed", "S1000");
/*      */ 
/*      */     }
/*      */     catch (SQLException sqlEx)
/*      */     {
/*  253 */       checkAndFireConnectionError(sqlEx);
/*      */     }
/*      */     
/*  256 */     return 0L;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public float getFloat(int parameterIndex)
/*      */     throws SQLException
/*      */   {
/*      */     try
/*      */     {
/*  266 */       if (this.wrappedStmt != null) {
/*  267 */         return ((CallableStatement)this.wrappedStmt).getFloat(parameterIndex);
/*      */       }
/*      */       
/*  270 */       throw SQLError.createSQLException("No operations allowed after statement closed", "S1000");
/*      */ 
/*      */     }
/*      */     catch (SQLException sqlEx)
/*      */     {
/*  275 */       checkAndFireConnectionError(sqlEx);
/*      */     }
/*      */     
/*  278 */     return 0.0F;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public double getDouble(int parameterIndex)
/*      */     throws SQLException
/*      */   {
/*      */     try
/*      */     {
/*  288 */       if (this.wrappedStmt != null) {
/*  289 */         return ((CallableStatement)this.wrappedStmt).getDouble(parameterIndex);
/*      */       }
/*      */       
/*  292 */       throw SQLError.createSQLException("No operations allowed after statement closed", "S1000");
/*      */ 
/*      */     }
/*      */     catch (SQLException sqlEx)
/*      */     {
/*  297 */       checkAndFireConnectionError(sqlEx);
/*      */     }
/*      */     
/*  300 */     return 0.0D;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public BigDecimal getBigDecimal(int parameterIndex, int scale)
/*      */     throws SQLException
/*      */   {
/*      */     try
/*      */     {
/*  311 */       if (this.wrappedStmt != null) {
/*  312 */         return ((CallableStatement)this.wrappedStmt).getBigDecimal(parameterIndex, scale);
/*      */       }
/*      */       
/*  315 */       throw SQLError.createSQLException("No operations allowed after statement closed", "S1000");
/*      */ 
/*      */     }
/*      */     catch (SQLException sqlEx)
/*      */     {
/*  320 */       checkAndFireConnectionError(sqlEx);
/*      */     }
/*      */     
/*  323 */     return null;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public byte[] getBytes(int parameterIndex)
/*      */     throws SQLException
/*      */   {
/*      */     try
/*      */     {
/*  333 */       if (this.wrappedStmt != null) {
/*  334 */         return ((CallableStatement)this.wrappedStmt).getBytes(parameterIndex);
/*      */       }
/*      */       
/*  337 */       throw SQLError.createSQLException("No operations allowed after statement closed", "S1000");
/*      */ 
/*      */     }
/*      */     catch (SQLException sqlEx)
/*      */     {
/*  342 */       checkAndFireConnectionError(sqlEx);
/*      */     }
/*      */     
/*  345 */     return null;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public Date getDate(int parameterIndex)
/*      */     throws SQLException
/*      */   {
/*      */     try
/*      */     {
/*  355 */       if (this.wrappedStmt != null) {
/*  356 */         return ((CallableStatement)this.wrappedStmt).getDate(parameterIndex);
/*      */       }
/*      */       
/*  359 */       throw SQLError.createSQLException("No operations allowed after statement closed", "S1000");
/*      */ 
/*      */     }
/*      */     catch (SQLException sqlEx)
/*      */     {
/*  364 */       checkAndFireConnectionError(sqlEx);
/*      */     }
/*      */     
/*  367 */     return null;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public Time getTime(int parameterIndex)
/*      */     throws SQLException
/*      */   {
/*      */     try
/*      */     {
/*  377 */       if (this.wrappedStmt != null) {
/*  378 */         return ((CallableStatement)this.wrappedStmt).getTime(parameterIndex);
/*      */       }
/*      */       
/*  381 */       throw SQLError.createSQLException("No operations allowed after statement closed", "S1000");
/*      */ 
/*      */     }
/*      */     catch (SQLException sqlEx)
/*      */     {
/*  386 */       checkAndFireConnectionError(sqlEx);
/*      */     }
/*      */     
/*  389 */     return null;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public Timestamp getTimestamp(int parameterIndex)
/*      */     throws SQLException
/*      */   {
/*      */     try
/*      */     {
/*  399 */       if (this.wrappedStmt != null) {
/*  400 */         return ((CallableStatement)this.wrappedStmt).getTimestamp(parameterIndex);
/*      */       }
/*      */       
/*  403 */       throw SQLError.createSQLException("No operations allowed after statement closed", "S1000");
/*      */ 
/*      */     }
/*      */     catch (SQLException sqlEx)
/*      */     {
/*  408 */       checkAndFireConnectionError(sqlEx);
/*      */     }
/*      */     
/*  411 */     return null;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public Object getObject(int parameterIndex)
/*      */     throws SQLException
/*      */   {
/*      */     try
/*      */     {
/*  421 */       if (this.wrappedStmt != null) {
/*  422 */         return ((CallableStatement)this.wrappedStmt).getObject(parameterIndex);
/*      */       }
/*      */       
/*  425 */       throw SQLError.createSQLException("No operations allowed after statement closed", "S1000");
/*      */ 
/*      */     }
/*      */     catch (SQLException sqlEx)
/*      */     {
/*  430 */       checkAndFireConnectionError(sqlEx);
/*      */     }
/*      */     
/*  433 */     return null;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public BigDecimal getBigDecimal(int parameterIndex)
/*      */     throws SQLException
/*      */   {
/*      */     try
/*      */     {
/*  443 */       if (this.wrappedStmt != null) {
/*  444 */         return ((CallableStatement)this.wrappedStmt).getBigDecimal(parameterIndex);
/*      */       }
/*      */       
/*  447 */       throw SQLError.createSQLException("No operations allowed after statement closed", "S1000");
/*      */ 
/*      */     }
/*      */     catch (SQLException sqlEx)
/*      */     {
/*  452 */       checkAndFireConnectionError(sqlEx);
/*      */     }
/*      */     
/*  455 */     return null;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public Object getObject(int parameterIndex, Map typeMap)
/*      */     throws SQLException
/*      */   {
/*      */     try
/*      */     {
/*  466 */       if (this.wrappedStmt != null) {
/*  467 */         return ((CallableStatement)this.wrappedStmt).getObject(parameterIndex, typeMap);
/*      */       }
/*      */       
/*  470 */       throw SQLError.createSQLException("No operations allowed after statement closed", "S1000");
/*      */ 
/*      */     }
/*      */     catch (SQLException sqlEx)
/*      */     {
/*  475 */       checkAndFireConnectionError(sqlEx);
/*      */     }
/*  477 */     return null;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public Ref getRef(int parameterIndex)
/*      */     throws SQLException
/*      */   {
/*      */     try
/*      */     {
/*  487 */       if (this.wrappedStmt != null) {
/*  488 */         return ((CallableStatement)this.wrappedStmt).getRef(parameterIndex);
/*      */       }
/*      */       
/*  491 */       throw SQLError.createSQLException("No operations allowed after statement closed", "S1000");
/*      */ 
/*      */     }
/*      */     catch (SQLException sqlEx)
/*      */     {
/*  496 */       checkAndFireConnectionError(sqlEx);
/*      */     }
/*      */     
/*  499 */     return null;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public Blob getBlob(int parameterIndex)
/*      */     throws SQLException
/*      */   {
/*      */     try
/*      */     {
/*  509 */       if (this.wrappedStmt != null) {
/*  510 */         return ((CallableStatement)this.wrappedStmt).getBlob(parameterIndex);
/*      */       }
/*      */       
/*  513 */       throw SQLError.createSQLException("No operations allowed after statement closed", "S1000");
/*      */ 
/*      */     }
/*      */     catch (SQLException sqlEx)
/*      */     {
/*  518 */       checkAndFireConnectionError(sqlEx);
/*      */     }
/*      */     
/*  521 */     return null;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public Clob getClob(int parameterIndex)
/*      */     throws SQLException
/*      */   {
/*      */     try
/*      */     {
/*  531 */       if (this.wrappedStmt != null) {
/*  532 */         return ((CallableStatement)this.wrappedStmt).getClob(parameterIndex);
/*      */       }
/*      */       
/*  535 */       throw SQLError.createSQLException("No operations allowed after statement closed", "S1000");
/*      */ 
/*      */     }
/*      */     catch (SQLException sqlEx)
/*      */     {
/*  540 */       checkAndFireConnectionError(sqlEx);
/*      */     }
/*  542 */     return null;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public Array getArray(int parameterIndex)
/*      */     throws SQLException
/*      */   {
/*      */     try
/*      */     {
/*  552 */       if (this.wrappedStmt != null) {
/*  553 */         return ((CallableStatement)this.wrappedStmt).getArray(parameterIndex);
/*      */       }
/*      */       
/*  556 */       throw SQLError.createSQLException("No operations allowed after statement closed", "S1000");
/*      */ 
/*      */     }
/*      */     catch (SQLException sqlEx)
/*      */     {
/*  561 */       checkAndFireConnectionError(sqlEx);
/*      */     }
/*  563 */     return null;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public Date getDate(int parameterIndex, Calendar cal)
/*      */     throws SQLException
/*      */   {
/*      */     try
/*      */     {
/*  573 */       if (this.wrappedStmt != null) {
/*  574 */         return ((CallableStatement)this.wrappedStmt).getDate(parameterIndex, cal);
/*      */       }
/*      */       
/*  577 */       throw SQLError.createSQLException("No operations allowed after statement closed", "S1000");
/*      */ 
/*      */     }
/*      */     catch (SQLException sqlEx)
/*      */     {
/*  582 */       checkAndFireConnectionError(sqlEx);
/*      */     }
/*  584 */     return null;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public Time getTime(int parameterIndex, Calendar cal)
/*      */     throws SQLException
/*      */   {
/*      */     try
/*      */     {
/*  594 */       if (this.wrappedStmt != null) {
/*  595 */         return ((CallableStatement)this.wrappedStmt).getTime(parameterIndex, cal);
/*      */       }
/*      */       
/*  598 */       throw SQLError.createSQLException("No operations allowed after statement closed", "S1000");
/*      */ 
/*      */     }
/*      */     catch (SQLException sqlEx)
/*      */     {
/*  603 */       checkAndFireConnectionError(sqlEx);
/*      */     }
/*  605 */     return null;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public Timestamp getTimestamp(int parameterIndex, Calendar cal)
/*      */     throws SQLException
/*      */   {
/*      */     try
/*      */     {
/*  616 */       if (this.wrappedStmt != null) {
/*  617 */         return ((CallableStatement)this.wrappedStmt).getTimestamp(parameterIndex, cal);
/*      */       }
/*      */       
/*  620 */       throw SQLError.createSQLException("No operations allowed after statement closed", "S1000");
/*      */ 
/*      */     }
/*      */     catch (SQLException sqlEx)
/*      */     {
/*  625 */       checkAndFireConnectionError(sqlEx);
/*      */     }
/*  627 */     return null;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void registerOutParameter(int paramIndex, int sqlType, String typeName)
/*      */     throws SQLException
/*      */   {
/*      */     try
/*      */     {
/*  639 */       if (this.wrappedStmt != null) {
/*  640 */         ((CallableStatement)this.wrappedStmt).registerOutParameter(paramIndex, sqlType, typeName);
/*      */       }
/*      */       else {
/*  643 */         throw SQLError.createSQLException("No operations allowed after statement closed", "S1000");
/*      */       }
/*      */     }
/*      */     catch (SQLException sqlEx)
/*      */     {
/*  648 */       checkAndFireConnectionError(sqlEx);
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void registerOutParameter(String parameterName, int sqlType)
/*      */     throws SQLException
/*      */   {
/*      */     try
/*      */     {
/*  661 */       if (this.wrappedStmt != null) {
/*  662 */         ((CallableStatement)this.wrappedStmt).registerOutParameter(parameterName, sqlType);
/*      */       }
/*      */       else {
/*  665 */         throw SQLError.createSQLException("No operations allowed after statement closed", "S1000");
/*      */       }
/*      */     }
/*      */     catch (SQLException sqlEx)
/*      */     {
/*  670 */       checkAndFireConnectionError(sqlEx);
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void registerOutParameter(String parameterName, int sqlType, int scale)
/*      */     throws SQLException
/*      */   {
/*      */     try
/*      */     {
/*  683 */       if (this.wrappedStmt != null) {
/*  684 */         ((CallableStatement)this.wrappedStmt).registerOutParameter(parameterName, sqlType, scale);
/*      */       }
/*      */       else {
/*  687 */         throw SQLError.createSQLException("No operations allowed after statement closed", "S1000");
/*      */       }
/*      */     }
/*      */     catch (SQLException sqlEx)
/*      */     {
/*  692 */       checkAndFireConnectionError(sqlEx);
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void registerOutParameter(String parameterName, int sqlType, String typeName)
/*      */     throws SQLException
/*      */   {
/*      */     try
/*      */     {
/*  705 */       if (this.wrappedStmt != null) {
/*  706 */         ((CallableStatement)this.wrappedStmt).registerOutParameter(parameterName, sqlType, typeName);
/*      */       }
/*      */       else {
/*  709 */         throw SQLError.createSQLException("No operations allowed after statement closed", "S1000");
/*      */       }
/*      */     }
/*      */     catch (SQLException sqlEx)
/*      */     {
/*  714 */       checkAndFireConnectionError(sqlEx);
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public URL getURL(int parameterIndex)
/*      */     throws SQLException
/*      */   {
/*      */     try
/*      */     {
/*  725 */       if (this.wrappedStmt != null) {
/*  726 */         return ((CallableStatement)this.wrappedStmt).getURL(parameterIndex);
/*      */       }
/*      */       
/*  729 */       throw SQLError.createSQLException("No operations allowed after statement closed", "S1000");
/*      */ 
/*      */     }
/*      */     catch (SQLException sqlEx)
/*      */     {
/*  734 */       checkAndFireConnectionError(sqlEx);
/*      */     }
/*      */     
/*  737 */     return null;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public void setURL(String parameterName, URL val)
/*      */     throws SQLException
/*      */   {
/*      */     try
/*      */     {
/*  747 */       if (this.wrappedStmt != null) {
/*  748 */         ((CallableStatement)this.wrappedStmt).setURL(parameterName, val);
/*      */       }
/*      */       else {
/*  751 */         throw SQLError.createSQLException("No operations allowed after statement closed", "S1000");
/*      */       }
/*      */     }
/*      */     catch (SQLException sqlEx)
/*      */     {
/*  756 */       checkAndFireConnectionError(sqlEx);
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public void setNull(String parameterName, int sqlType)
/*      */     throws SQLException
/*      */   {
/*      */     try
/*      */     {
/*  767 */       if (this.wrappedStmt != null) {
/*  768 */         ((CallableStatement)this.wrappedStmt).setNull(parameterName, sqlType);
/*      */       }
/*      */       else {
/*  771 */         throw SQLError.createSQLException("No operations allowed after statement closed", "S1000");
/*      */       }
/*      */     }
/*      */     catch (SQLException sqlEx)
/*      */     {
/*  776 */       checkAndFireConnectionError(sqlEx);
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public void setBoolean(String parameterName, boolean x)
/*      */     throws SQLException
/*      */   {
/*      */     try
/*      */     {
/*  787 */       if (this.wrappedStmt != null) {
/*  788 */         ((CallableStatement)this.wrappedStmt).setBoolean(parameterName, x);
/*      */       }
/*      */       else {
/*  791 */         throw SQLError.createSQLException("No operations allowed after statement closed", "S1000");
/*      */       }
/*      */     }
/*      */     catch (SQLException sqlEx)
/*      */     {
/*  796 */       checkAndFireConnectionError(sqlEx);
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public void setByte(String parameterName, byte x)
/*      */     throws SQLException
/*      */   {
/*      */     try
/*      */     {
/*  807 */       if (this.wrappedStmt != null) {
/*  808 */         ((CallableStatement)this.wrappedStmt).setByte(parameterName, x);
/*      */       }
/*      */       else {
/*  811 */         throw SQLError.createSQLException("No operations allowed after statement closed", "S1000");
/*      */       }
/*      */     }
/*      */     catch (SQLException sqlEx)
/*      */     {
/*  816 */       checkAndFireConnectionError(sqlEx);
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public void setShort(String parameterName, short x)
/*      */     throws SQLException
/*      */   {
/*      */     try
/*      */     {
/*  827 */       if (this.wrappedStmt != null) {
/*  828 */         ((CallableStatement)this.wrappedStmt).setShort(parameterName, x);
/*      */       }
/*      */       else {
/*  831 */         throw SQLError.createSQLException("No operations allowed after statement closed", "S1000");
/*      */       }
/*      */     }
/*      */     catch (SQLException sqlEx)
/*      */     {
/*  836 */       checkAndFireConnectionError(sqlEx);
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public void setInt(String parameterName, int x)
/*      */     throws SQLException
/*      */   {
/*      */     try
/*      */     {
/*  847 */       if (this.wrappedStmt != null) {
/*  848 */         ((CallableStatement)this.wrappedStmt).setInt(parameterName, x);
/*      */       } else {
/*  850 */         throw SQLError.createSQLException("No operations allowed after statement closed", "S1000");
/*      */       }
/*      */     }
/*      */     catch (SQLException sqlEx)
/*      */     {
/*  855 */       checkAndFireConnectionError(sqlEx);
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public void setLong(String parameterName, long x)
/*      */     throws SQLException
/*      */   {
/*      */     try
/*      */     {
/*  866 */       if (this.wrappedStmt != null) {
/*  867 */         ((CallableStatement)this.wrappedStmt).setLong(parameterName, x);
/*      */       }
/*      */       else {
/*  870 */         throw SQLError.createSQLException("No operations allowed after statement closed", "S1000");
/*      */       }
/*      */     }
/*      */     catch (SQLException sqlEx)
/*      */     {
/*  875 */       checkAndFireConnectionError(sqlEx);
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public void setFloat(String parameterName, float x)
/*      */     throws SQLException
/*      */   {
/*      */     try
/*      */     {
/*  886 */       if (this.wrappedStmt != null) {
/*  887 */         ((CallableStatement)this.wrappedStmt).setFloat(parameterName, x);
/*      */       }
/*      */       else {
/*  890 */         throw SQLError.createSQLException("No operations allowed after statement closed", "S1000");
/*      */       }
/*      */     }
/*      */     catch (SQLException sqlEx)
/*      */     {
/*  895 */       checkAndFireConnectionError(sqlEx);
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public void setDouble(String parameterName, double x)
/*      */     throws SQLException
/*      */   {
/*      */     try
/*      */     {
/*  906 */       if (this.wrappedStmt != null) {
/*  907 */         ((CallableStatement)this.wrappedStmt).setDouble(parameterName, x);
/*      */       }
/*      */       else {
/*  910 */         throw SQLError.createSQLException("No operations allowed after statement closed", "S1000");
/*      */       }
/*      */     }
/*      */     catch (SQLException sqlEx)
/*      */     {
/*  915 */       checkAndFireConnectionError(sqlEx);
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void setBigDecimal(String parameterName, BigDecimal x)
/*      */     throws SQLException
/*      */   {
/*      */     try
/*      */     {
/*  928 */       if (this.wrappedStmt != null) {
/*  929 */         ((CallableStatement)this.wrappedStmt).setBigDecimal(parameterName, x);
/*      */       }
/*      */       else {
/*  932 */         throw SQLError.createSQLException("No operations allowed after statement closed", "S1000");
/*      */       }
/*      */     }
/*      */     catch (SQLException sqlEx)
/*      */     {
/*  937 */       checkAndFireConnectionError(sqlEx);
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public void setString(String parameterName, String x)
/*      */     throws SQLException
/*      */   {
/*      */     try
/*      */     {
/*  949 */       if (this.wrappedStmt != null) {
/*  950 */         ((CallableStatement)this.wrappedStmt).setString(parameterName, x);
/*      */       }
/*      */       else {
/*  953 */         throw SQLError.createSQLException("No operations allowed after statement closed", "S1000");
/*      */       }
/*      */     }
/*      */     catch (SQLException sqlEx)
/*      */     {
/*  958 */       checkAndFireConnectionError(sqlEx);
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public void setBytes(String parameterName, byte[] x)
/*      */     throws SQLException
/*      */   {
/*      */     try
/*      */     {
/*  969 */       if (this.wrappedStmt != null) {
/*  970 */         ((CallableStatement)this.wrappedStmt).setBytes(parameterName, x);
/*      */       }
/*      */       else {
/*  973 */         throw SQLError.createSQLException("No operations allowed after statement closed", "S1000");
/*      */       }
/*      */     }
/*      */     catch (SQLException sqlEx)
/*      */     {
/*  978 */       checkAndFireConnectionError(sqlEx);
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public void setDate(String parameterName, Date x)
/*      */     throws SQLException
/*      */   {
/*      */     try
/*      */     {
/*  989 */       if (this.wrappedStmt != null) {
/*  990 */         ((CallableStatement)this.wrappedStmt).setDate(parameterName, x);
/*      */       }
/*      */       else {
/*  993 */         throw SQLError.createSQLException("No operations allowed after statement closed", "S1000");
/*      */       }
/*      */     }
/*      */     catch (SQLException sqlEx)
/*      */     {
/*  998 */       checkAndFireConnectionError(sqlEx);
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public void setTime(String parameterName, Time x)
/*      */     throws SQLException
/*      */   {
/*      */     try
/*      */     {
/* 1009 */       if (this.wrappedStmt != null) {
/* 1010 */         ((CallableStatement)this.wrappedStmt).setTime(parameterName, x);
/*      */       }
/*      */       else {
/* 1013 */         throw SQLError.createSQLException("No operations allowed after statement closed", "S1000");
/*      */       }
/*      */     }
/*      */     catch (SQLException sqlEx)
/*      */     {
/* 1018 */       checkAndFireConnectionError(sqlEx);
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void setTimestamp(String parameterName, Timestamp x)
/*      */     throws SQLException
/*      */   {
/*      */     try
/*      */     {
/* 1031 */       if (this.wrappedStmt != null) {
/* 1032 */         ((CallableStatement)this.wrappedStmt).setTimestamp(parameterName, x);
/*      */       }
/*      */       else {
/* 1035 */         throw SQLError.createSQLException("No operations allowed after statement closed", "S1000");
/*      */       }
/*      */     }
/*      */     catch (SQLException sqlEx)
/*      */     {
/* 1040 */       checkAndFireConnectionError(sqlEx);
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void setAsciiStream(String parameterName, InputStream x, int length)
/*      */     throws SQLException
/*      */   {
/*      */     try
/*      */     {
/* 1053 */       if (this.wrappedStmt != null) {
/* 1054 */         ((CallableStatement)this.wrappedStmt).setAsciiStream(parameterName, x, length);
/*      */       }
/*      */       else {
/* 1057 */         throw SQLError.createSQLException("No operations allowed after statement closed", "S1000");
/*      */       }
/*      */     }
/*      */     catch (SQLException sqlEx)
/*      */     {
/* 1062 */       checkAndFireConnectionError(sqlEx);
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void setBinaryStream(String parameterName, InputStream x, int length)
/*      */     throws SQLException
/*      */   {
/*      */     try
/*      */     {
/* 1076 */       if (this.wrappedStmt != null) {
/* 1077 */         ((CallableStatement)this.wrappedStmt).setBinaryStream(parameterName, x, length);
/*      */       }
/*      */       else {
/* 1080 */         throw SQLError.createSQLException("No operations allowed after statement closed", "S1000");
/*      */       }
/*      */     }
/*      */     catch (SQLException sqlEx)
/*      */     {
/* 1085 */       checkAndFireConnectionError(sqlEx);
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void setObject(String parameterName, Object x, int targetSqlType, int scale)
/*      */     throws SQLException
/*      */   {
/*      */     try
/*      */     {
/* 1098 */       if (this.wrappedStmt != null) {
/* 1099 */         ((CallableStatement)this.wrappedStmt).setObject(parameterName, x, targetSqlType, scale);
/*      */       }
/*      */       else {
/* 1102 */         throw SQLError.createSQLException("No operations allowed after statement closed", "S1000");
/*      */       }
/*      */     }
/*      */     catch (SQLException sqlEx)
/*      */     {
/* 1107 */       checkAndFireConnectionError(sqlEx);
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void setObject(String parameterName, Object x, int targetSqlType)
/*      */     throws SQLException
/*      */   {
/*      */     try
/*      */     {
/* 1120 */       if (this.wrappedStmt != null) {
/* 1121 */         ((CallableStatement)this.wrappedStmt).setObject(parameterName, x, targetSqlType);
/*      */       }
/*      */       else {
/* 1124 */         throw SQLError.createSQLException("No operations allowed after statement closed", "S1000");
/*      */       }
/*      */     }
/*      */     catch (SQLException sqlEx)
/*      */     {
/* 1129 */       checkAndFireConnectionError(sqlEx);
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public void setObject(String parameterName, Object x)
/*      */     throws SQLException
/*      */   {
/*      */     try
/*      */     {
/* 1141 */       if (this.wrappedStmt != null) {
/* 1142 */         ((CallableStatement)this.wrappedStmt).setObject(parameterName, x);
/*      */       }
/*      */       else {
/* 1145 */         throw SQLError.createSQLException("No operations allowed after statement closed", "S1000");
/*      */       }
/*      */     }
/*      */     catch (SQLException sqlEx)
/*      */     {
/* 1150 */       checkAndFireConnectionError(sqlEx);
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void setCharacterStream(String parameterName, Reader reader, int length)
/*      */     throws SQLException
/*      */   {
/*      */     try
/*      */     {
/* 1163 */       if (this.wrappedStmt != null) {
/* 1164 */         ((CallableStatement)this.wrappedStmt).setCharacterStream(parameterName, reader, length);
/*      */       }
/*      */       else {
/* 1167 */         throw SQLError.createSQLException("No operations allowed after statement closed", "S1000");
/*      */       }
/*      */     }
/*      */     catch (SQLException sqlEx)
/*      */     {
/* 1172 */       checkAndFireConnectionError(sqlEx);
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void setDate(String parameterName, Date x, Calendar cal)
/*      */     throws SQLException
/*      */   {
/*      */     try
/*      */     {
/* 1185 */       if (this.wrappedStmt != null) {
/* 1186 */         ((CallableStatement)this.wrappedStmt).setDate(parameterName, x, cal);
/*      */       }
/*      */       else {
/* 1189 */         throw SQLError.createSQLException("No operations allowed after statement closed", "S1000");
/*      */       }
/*      */     }
/*      */     catch (SQLException sqlEx)
/*      */     {
/* 1194 */       checkAndFireConnectionError(sqlEx);
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void setTime(String parameterName, Time x, Calendar cal)
/*      */     throws SQLException
/*      */   {
/*      */     try
/*      */     {
/* 1207 */       if (this.wrappedStmt != null) {
/* 1208 */         ((CallableStatement)this.wrappedStmt).setTime(parameterName, x, cal);
/*      */       }
/*      */       else {
/* 1211 */         throw SQLError.createSQLException("No operations allowed after statement closed", "S1000");
/*      */       }
/*      */     }
/*      */     catch (SQLException sqlEx)
/*      */     {
/* 1216 */       checkAndFireConnectionError(sqlEx);
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void setTimestamp(String parameterName, Timestamp x, Calendar cal)
/*      */     throws SQLException
/*      */   {
/*      */     try
/*      */     {
/* 1229 */       if (this.wrappedStmt != null) {
/* 1230 */         ((CallableStatement)this.wrappedStmt).setTimestamp(parameterName, x, cal);
/*      */       }
/*      */       else {
/* 1233 */         throw SQLError.createSQLException("No operations allowed after statement closed", "S1000");
/*      */       }
/*      */     }
/*      */     catch (SQLException sqlEx)
/*      */     {
/* 1238 */       checkAndFireConnectionError(sqlEx);
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void setNull(String parameterName, int sqlType, String typeName)
/*      */     throws SQLException
/*      */   {
/*      */     try
/*      */     {
/* 1251 */       if (this.wrappedStmt != null) {
/* 1252 */         ((CallableStatement)this.wrappedStmt).setNull(parameterName, sqlType, typeName);
/*      */       }
/*      */       else {
/* 1255 */         throw SQLError.createSQLException("No operations allowed after statement closed", "S1000");
/*      */       }
/*      */     }
/*      */     catch (SQLException sqlEx)
/*      */     {
/* 1260 */       checkAndFireConnectionError(sqlEx);
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public String getString(String parameterName)
/*      */     throws SQLException
/*      */   {
/*      */     try
/*      */     {
/* 1271 */       if (this.wrappedStmt != null) {
/* 1272 */         return ((CallableStatement)this.wrappedStmt).getString(parameterName);
/*      */       }
/*      */       
/* 1275 */       throw SQLError.createSQLException("No operations allowed after statement closed", "S1000");
/*      */ 
/*      */     }
/*      */     catch (SQLException sqlEx)
/*      */     {
/* 1280 */       checkAndFireConnectionError(sqlEx);
/*      */     }
/* 1282 */     return null;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public boolean getBoolean(String parameterName)
/*      */     throws SQLException
/*      */   {
/*      */     try
/*      */     {
/* 1292 */       if (this.wrappedStmt != null) {
/* 1293 */         return ((CallableStatement)this.wrappedStmt).getBoolean(parameterName);
/*      */       }
/*      */       
/* 1296 */       throw SQLError.createSQLException("No operations allowed after statement closed", "S1000");
/*      */ 
/*      */     }
/*      */     catch (SQLException sqlEx)
/*      */     {
/* 1301 */       checkAndFireConnectionError(sqlEx);
/*      */     }
/*      */     
/* 1304 */     return false;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public byte getByte(String parameterName)
/*      */     throws SQLException
/*      */   {
/*      */     try
/*      */     {
/* 1314 */       if (this.wrappedStmt != null) {
/* 1315 */         return ((CallableStatement)this.wrappedStmt).getByte(parameterName);
/*      */       }
/*      */       
/* 1318 */       throw SQLError.createSQLException("No operations allowed after statement closed", "S1000");
/*      */ 
/*      */     }
/*      */     catch (SQLException sqlEx)
/*      */     {
/* 1323 */       checkAndFireConnectionError(sqlEx);
/*      */     }
/*      */     
/* 1326 */     return 0;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public short getShort(String parameterName)
/*      */     throws SQLException
/*      */   {
/*      */     try
/*      */     {
/* 1336 */       if (this.wrappedStmt != null) {
/* 1337 */         return ((CallableStatement)this.wrappedStmt).getShort(parameterName);
/*      */       }
/*      */       
/* 1340 */       throw SQLError.createSQLException("No operations allowed after statement closed", "S1000");
/*      */ 
/*      */     }
/*      */     catch (SQLException sqlEx)
/*      */     {
/* 1345 */       checkAndFireConnectionError(sqlEx);
/*      */     }
/*      */     
/* 1348 */     return 0;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public int getInt(String parameterName)
/*      */     throws SQLException
/*      */   {
/*      */     try
/*      */     {
/* 1358 */       if (this.wrappedStmt != null) {
/* 1359 */         return ((CallableStatement)this.wrappedStmt).getInt(parameterName);
/*      */       }
/*      */       
/* 1362 */       throw SQLError.createSQLException("No operations allowed after statement closed", "S1000");
/*      */ 
/*      */     }
/*      */     catch (SQLException sqlEx)
/*      */     {
/* 1367 */       checkAndFireConnectionError(sqlEx);
/*      */     }
/*      */     
/* 1370 */     return 0;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public long getLong(String parameterName)
/*      */     throws SQLException
/*      */   {
/*      */     try
/*      */     {
/* 1380 */       if (this.wrappedStmt != null) {
/* 1381 */         return ((CallableStatement)this.wrappedStmt).getLong(parameterName);
/*      */       }
/*      */       
/* 1384 */       throw SQLError.createSQLException("No operations allowed after statement closed", "S1000");
/*      */ 
/*      */     }
/*      */     catch (SQLException sqlEx)
/*      */     {
/* 1389 */       checkAndFireConnectionError(sqlEx);
/*      */     }
/*      */     
/* 1392 */     return 0L;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public float getFloat(String parameterName)
/*      */     throws SQLException
/*      */   {
/*      */     try
/*      */     {
/* 1402 */       if (this.wrappedStmt != null) {
/* 1403 */         return ((CallableStatement)this.wrappedStmt).getFloat(parameterName);
/*      */       }
/*      */       
/* 1406 */       throw SQLError.createSQLException("No operations allowed after statement closed", "S1000");
/*      */ 
/*      */     }
/*      */     catch (SQLException sqlEx)
/*      */     {
/* 1411 */       checkAndFireConnectionError(sqlEx);
/*      */     }
/*      */     
/* 1414 */     return 0.0F;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public double getDouble(String parameterName)
/*      */     throws SQLException
/*      */   {
/*      */     try
/*      */     {
/* 1424 */       if (this.wrappedStmt != null) {
/* 1425 */         return ((CallableStatement)this.wrappedStmt).getDouble(parameterName);
/*      */       }
/*      */       
/* 1428 */       throw SQLError.createSQLException("No operations allowed after statement closed", "S1000");
/*      */ 
/*      */     }
/*      */     catch (SQLException sqlEx)
/*      */     {
/* 1433 */       checkAndFireConnectionError(sqlEx);
/*      */     }
/*      */     
/* 1436 */     return 0.0D;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public byte[] getBytes(String parameterName)
/*      */     throws SQLException
/*      */   {
/*      */     try
/*      */     {
/* 1446 */       if (this.wrappedStmt != null) {
/* 1447 */         return ((CallableStatement)this.wrappedStmt).getBytes(parameterName);
/*      */       }
/*      */       
/* 1450 */       throw SQLError.createSQLException("No operations allowed after statement closed", "S1000");
/*      */ 
/*      */     }
/*      */     catch (SQLException sqlEx)
/*      */     {
/* 1455 */       checkAndFireConnectionError(sqlEx);
/*      */     }
/*      */     
/* 1458 */     return null;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public Date getDate(String parameterName)
/*      */     throws SQLException
/*      */   {
/*      */     try
/*      */     {
/* 1468 */       if (this.wrappedStmt != null) {
/* 1469 */         return ((CallableStatement)this.wrappedStmt).getDate(parameterName);
/*      */       }
/*      */       
/* 1472 */       throw SQLError.createSQLException("No operations allowed after statement closed", "S1000");
/*      */ 
/*      */     }
/*      */     catch (SQLException sqlEx)
/*      */     {
/* 1477 */       checkAndFireConnectionError(sqlEx);
/*      */     }
/*      */     
/* 1480 */     return null;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public Time getTime(String parameterName)
/*      */     throws SQLException
/*      */   {
/*      */     try
/*      */     {
/* 1490 */       if (this.wrappedStmt != null) {
/* 1491 */         return ((CallableStatement)this.wrappedStmt).getTime(parameterName);
/*      */       }
/*      */       
/* 1494 */       throw SQLError.createSQLException("No operations allowed after statement closed", "S1000");
/*      */ 
/*      */     }
/*      */     catch (SQLException sqlEx)
/*      */     {
/* 1499 */       checkAndFireConnectionError(sqlEx);
/*      */     }
/*      */     
/* 1502 */     return null;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public Timestamp getTimestamp(String parameterName)
/*      */     throws SQLException
/*      */   {
/*      */     try
/*      */     {
/* 1512 */       if (this.wrappedStmt != null) {
/* 1513 */         return ((CallableStatement)this.wrappedStmt).getTimestamp(parameterName);
/*      */       }
/*      */       
/* 1516 */       throw SQLError.createSQLException("No operations allowed after statement closed", "S1000");
/*      */ 
/*      */     }
/*      */     catch (SQLException sqlEx)
/*      */     {
/* 1521 */       checkAndFireConnectionError(sqlEx);
/*      */     }
/*      */     
/* 1524 */     return null;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public Object getObject(String parameterName)
/*      */     throws SQLException
/*      */   {
/*      */     try
/*      */     {
/* 1534 */       if (this.wrappedStmt != null) {
/* 1535 */         return ((CallableStatement)this.wrappedStmt).getObject(parameterName);
/*      */       }
/*      */       
/* 1538 */       throw SQLError.createSQLException("No operations allowed after statement closed", "S1000");
/*      */ 
/*      */     }
/*      */     catch (SQLException sqlEx)
/*      */     {
/* 1543 */       checkAndFireConnectionError(sqlEx);
/*      */     }
/*      */     
/* 1546 */     return null;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public BigDecimal getBigDecimal(String parameterName)
/*      */     throws SQLException
/*      */   {
/*      */     try
/*      */     {
/* 1556 */       if (this.wrappedStmt != null) {
/* 1557 */         return ((CallableStatement)this.wrappedStmt).getBigDecimal(parameterName);
/*      */       }
/*      */       
/* 1560 */       throw SQLError.createSQLException("No operations allowed after statement closed", "S1000");
/*      */ 
/*      */     }
/*      */     catch (SQLException sqlEx)
/*      */     {
/* 1565 */       checkAndFireConnectionError(sqlEx);
/*      */     }
/*      */     
/* 1568 */     return null;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public Object getObject(String parameterName, Map typeMap)
/*      */     throws SQLException
/*      */   {
/*      */     try
/*      */     {
/* 1579 */       if (this.wrappedStmt != null) {
/* 1580 */         return ((CallableStatement)this.wrappedStmt).getObject(parameterName, typeMap);
/*      */       }
/*      */       
/* 1583 */       throw SQLError.createSQLException("No operations allowed after statement closed", "S1000");
/*      */ 
/*      */     }
/*      */     catch (SQLException sqlEx)
/*      */     {
/* 1588 */       checkAndFireConnectionError(sqlEx);
/*      */     }
/* 1590 */     return null;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public Ref getRef(String parameterName)
/*      */     throws SQLException
/*      */   {
/*      */     try
/*      */     {
/* 1600 */       if (this.wrappedStmt != null) {
/* 1601 */         return ((CallableStatement)this.wrappedStmt).getRef(parameterName);
/*      */       }
/*      */       
/* 1604 */       throw SQLError.createSQLException("No operations allowed after statement closed", "S1000");
/*      */ 
/*      */     }
/*      */     catch (SQLException sqlEx)
/*      */     {
/* 1609 */       checkAndFireConnectionError(sqlEx);
/*      */     }
/*      */     
/* 1612 */     return null;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public Blob getBlob(String parameterName)
/*      */     throws SQLException
/*      */   {
/*      */     try
/*      */     {
/* 1622 */       if (this.wrappedStmt != null) {
/* 1623 */         return ((CallableStatement)this.wrappedStmt).getBlob(parameterName);
/*      */       }
/*      */       
/* 1626 */       throw SQLError.createSQLException("No operations allowed after statement closed", "S1000");
/*      */ 
/*      */     }
/*      */     catch (SQLException sqlEx)
/*      */     {
/* 1631 */       checkAndFireConnectionError(sqlEx);
/*      */     }
/*      */     
/* 1634 */     return null;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public Clob getClob(String parameterName)
/*      */     throws SQLException
/*      */   {
/*      */     try
/*      */     {
/* 1644 */       if (this.wrappedStmt != null) {
/* 1645 */         return ((CallableStatement)this.wrappedStmt).getClob(parameterName);
/*      */       }
/*      */       
/* 1648 */       throw SQLError.createSQLException("No operations allowed after statement closed", "S1000");
/*      */ 
/*      */     }
/*      */     catch (SQLException sqlEx)
/*      */     {
/* 1653 */       checkAndFireConnectionError(sqlEx);
/*      */     }
/* 1655 */     return null;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public Array getArray(String parameterName)
/*      */     throws SQLException
/*      */   {
/*      */     try
/*      */     {
/* 1665 */       if (this.wrappedStmt != null) {
/* 1666 */         return ((CallableStatement)this.wrappedStmt).getArray(parameterName);
/*      */       }
/*      */       
/* 1669 */       throw SQLError.createSQLException("No operations allowed after statement closed", "S1000");
/*      */ 
/*      */     }
/*      */     catch (SQLException sqlEx)
/*      */     {
/* 1674 */       checkAndFireConnectionError(sqlEx);
/*      */     }
/* 1676 */     return null;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public Date getDate(String parameterName, Calendar cal)
/*      */     throws SQLException
/*      */   {
/*      */     try
/*      */     {
/* 1686 */       if (this.wrappedStmt != null) {
/* 1687 */         return ((CallableStatement)this.wrappedStmt).getDate(parameterName, cal);
/*      */       }
/*      */       
/* 1690 */       throw SQLError.createSQLException("No operations allowed after statement closed", "S1000");
/*      */ 
/*      */     }
/*      */     catch (SQLException sqlEx)
/*      */     {
/* 1695 */       checkAndFireConnectionError(sqlEx);
/*      */     }
/* 1697 */     return null;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public Time getTime(String parameterName, Calendar cal)
/*      */     throws SQLException
/*      */   {
/*      */     try
/*      */     {
/* 1707 */       if (this.wrappedStmt != null) {
/* 1708 */         return ((CallableStatement)this.wrappedStmt).getTime(parameterName, cal);
/*      */       }
/*      */       
/* 1711 */       throw SQLError.createSQLException("No operations allowed after statement closed", "S1000");
/*      */ 
/*      */     }
/*      */     catch (SQLException sqlEx)
/*      */     {
/* 1716 */       checkAndFireConnectionError(sqlEx);
/*      */     }
/* 1718 */     return null;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public Timestamp getTimestamp(String parameterName, Calendar cal)
/*      */     throws SQLException
/*      */   {
/*      */     try
/*      */     {
/* 1729 */       if (this.wrappedStmt != null) {
/* 1730 */         return ((CallableStatement)this.wrappedStmt).getTimestamp(parameterName, cal);
/*      */       }
/*      */       
/* 1733 */       throw SQLError.createSQLException("No operations allowed after statement closed", "S1000");
/*      */ 
/*      */     }
/*      */     catch (SQLException sqlEx)
/*      */     {
/* 1738 */       checkAndFireConnectionError(sqlEx);
/*      */     }
/* 1740 */     return null;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public URL getURL(String parameterName)
/*      */     throws SQLException
/*      */   {
/*      */     try
/*      */     {
/* 1750 */       if (this.wrappedStmt != null) {
/* 1751 */         return ((CallableStatement)this.wrappedStmt).getURL(parameterName);
/*      */       }
/*      */       
/* 1754 */       throw SQLError.createSQLException("No operations allowed after statement closed", "S1000");
/*      */ 
/*      */     }
/*      */     catch (SQLException sqlEx)
/*      */     {
/* 1759 */       checkAndFireConnectionError(sqlEx);
/*      */     }
/*      */     
/* 1762 */     return null;
/*      */   }
/*      */ }


/* Location:              E:\java\java学习\hutubill\lib\all.jar!\com\mysql\jdbc\jdbc2\optional\CallableStatementWrapper.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version:       0.7.1
 */
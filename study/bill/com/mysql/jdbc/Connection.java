/*      */ package com.mysql.jdbc;
/*      */ 
/*      */ import com.mysql.jdbc.log.Log;
/*      */ import com.mysql.jdbc.log.LogFactory;
/*      */ import com.mysql.jdbc.log.NullLogger;
/*      */ import com.mysql.jdbc.profiler.ProfileEventSink;
/*      */ import com.mysql.jdbc.profiler.ProfilerEvent;
/*      */ import com.mysql.jdbc.util.LRUCache;
/*      */ import java.io.IOException;
/*      */ import java.io.InputStream;
/*      */ import java.io.PrintStream;
/*      */ import java.io.Reader;
/*      */ import java.io.UnsupportedEncodingException;
/*      */ import java.lang.reflect.Constructor;
/*      */ import java.lang.reflect.Method;
/*      */ import java.math.BigDecimal;
/*      */ import java.net.URL;
/*      */ import java.sql.Blob;
/*      */ import java.sql.Clob;
/*      */ import java.sql.Date;
/*      */ import java.sql.ParameterMetaData;
/*      */ import java.sql.Ref;
/*      */ import java.sql.ResultSetMetaData;
/*      */ import java.sql.SQLException;
/*      */ import java.sql.SQLWarning;
/*      */ import java.sql.Savepoint;
/*      */ import java.sql.Time;
/*      */ import java.sql.Timestamp;
/*      */ import java.util.ArrayList;
/*      */ import java.util.Calendar;
/*      */ import java.util.Collection;
/*      */ import java.util.GregorianCalendar;
/*      */ import java.util.HashMap;
/*      */ import java.util.Iterator;
/*      */ import java.util.List;
/*      */ import java.util.Locale;
/*      */ import java.util.Map;
/*      */ import java.util.Map.Entry;
/*      */ import java.util.Properties;
/*      */ import java.util.Set;
/*      */ import java.util.Stack;
/*      */ import java.util.StringTokenizer;
/*      */ import java.util.TimeZone;
/*      */ import java.util.Timer;
/*      */ import java.util.TreeMap;
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
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ public class Connection
/*      */   extends ConnectionProperties
/*      */   implements java.sql.Connection
/*      */ {
/*      */   private static final String JDBC_LOCAL_CHARACTER_SET_RESULTS = "jdbc.local.character_set_results";
/*      */   
/*      */   class CompoundCacheKey
/*      */   {
/*      */     String componentOne;
/*      */     String componentTwo;
/*      */     int hashCode;
/*      */     
/*      */     CompoundCacheKey(String partOne, String partTwo)
/*      */     {
/*  104 */       this.componentOne = partOne;
/*  105 */       this.componentTwo = partTwo;
/*      */       
/*      */ 
/*      */ 
/*  109 */       this.hashCode = ((this.componentOne != null ? this.componentOne : "") + this.componentTwo).hashCode();
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     public boolean equals(Object obj)
/*      */     {
/*  119 */       if ((obj instanceof CompoundCacheKey)) {
/*  120 */         CompoundCacheKey another = (CompoundCacheKey)obj;
/*      */         
/*  122 */         boolean firstPartEqual = false;
/*      */         
/*  124 */         if (this.componentOne == null) {
/*  125 */           firstPartEqual = another.componentOne == null;
/*      */         } else {
/*  127 */           firstPartEqual = this.componentOne.equals(another.componentOne);
/*      */         }
/*      */         
/*      */ 
/*  131 */         return (firstPartEqual) && (this.componentTwo.equals(another.componentTwo));
/*      */       }
/*      */       
/*      */ 
/*  135 */       return false;
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     public int hashCode()
/*      */     {
/*  144 */       return this.hashCode;
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   class UltraDevWorkAround
/*      */     implements java.sql.CallableStatement
/*      */   {
/*  153 */     private java.sql.PreparedStatement delegate = null;
/*      */     
/*      */     UltraDevWorkAround(java.sql.PreparedStatement pstmt) {
/*  156 */       this.delegate = pstmt;
/*      */     }
/*      */     
/*      */     public void addBatch() throws SQLException {
/*  160 */       this.delegate.addBatch();
/*      */     }
/*      */     
/*      */     public void addBatch(String p1) throws SQLException {
/*  164 */       this.delegate.addBatch(p1);
/*      */     }
/*      */     
/*      */     public void cancel() throws SQLException {
/*  168 */       this.delegate.cancel();
/*      */     }
/*      */     
/*      */     public void clearBatch() throws SQLException {
/*  172 */       this.delegate.clearBatch();
/*      */     }
/*      */     
/*      */     public void clearParameters() throws SQLException {
/*  176 */       this.delegate.clearParameters();
/*      */     }
/*      */     
/*      */     public void clearWarnings() throws SQLException {
/*  180 */       this.delegate.clearWarnings();
/*      */     }
/*      */     
/*      */     public void close() throws SQLException {
/*  184 */       this.delegate.close();
/*      */     }
/*      */     
/*      */     public boolean execute() throws SQLException {
/*  188 */       return this.delegate.execute();
/*      */     }
/*      */     
/*      */     public boolean execute(String p1) throws SQLException {
/*  192 */       return this.delegate.execute(p1);
/*      */     }
/*      */     
/*      */ 
/*      */     public boolean execute(String arg0, int arg1)
/*      */       throws SQLException
/*      */     {
/*  199 */       return this.delegate.execute(arg0, arg1);
/*      */     }
/*      */     
/*      */ 
/*      */     public boolean execute(String arg0, int[] arg1)
/*      */       throws SQLException
/*      */     {
/*  206 */       return this.delegate.execute(arg0, arg1);
/*      */     }
/*      */     
/*      */ 
/*      */     public boolean execute(String arg0, String[] arg1)
/*      */       throws SQLException
/*      */     {
/*  213 */       return this.delegate.execute(arg0, arg1);
/*      */     }
/*      */     
/*      */     public int[] executeBatch() throws SQLException {
/*  217 */       return this.delegate.executeBatch();
/*      */     }
/*      */     
/*      */     public java.sql.ResultSet executeQuery() throws SQLException {
/*  221 */       return this.delegate.executeQuery();
/*      */     }
/*      */     
/*      */     public java.sql.ResultSet executeQuery(String p1) throws SQLException
/*      */     {
/*  226 */       return this.delegate.executeQuery(p1);
/*      */     }
/*      */     
/*      */     public int executeUpdate() throws SQLException {
/*  230 */       return this.delegate.executeUpdate();
/*      */     }
/*      */     
/*      */     public int executeUpdate(String p1) throws SQLException {
/*  234 */       return this.delegate.executeUpdate(p1);
/*      */     }
/*      */     
/*      */ 
/*      */     public int executeUpdate(String arg0, int arg1)
/*      */       throws SQLException
/*      */     {
/*  241 */       return this.delegate.executeUpdate(arg0, arg1);
/*      */     }
/*      */     
/*      */ 
/*      */     public int executeUpdate(String arg0, int[] arg1)
/*      */       throws SQLException
/*      */     {
/*  248 */       return this.delegate.executeUpdate(arg0, arg1);
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */     public int executeUpdate(String arg0, String[] arg1)
/*      */       throws SQLException
/*      */     {
/*  256 */       return this.delegate.executeUpdate(arg0, arg1);
/*      */     }
/*      */     
/*      */     public java.sql.Array getArray(int p1) throws SQLException {
/*  260 */       throw SQLError.createSQLException("Not supported");
/*      */     }
/*      */     
/*      */ 
/*      */     public java.sql.Array getArray(String arg0)
/*      */       throws SQLException
/*      */     {
/*  267 */       throw new NotImplemented();
/*      */     }
/*      */     
/*      */     public BigDecimal getBigDecimal(int p1) throws SQLException {
/*  271 */       throw SQLError.createSQLException("Not supported");
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     /**
/*      */      * @deprecated
/*      */      */
/*      */     public BigDecimal getBigDecimal(int p1, int p2)
/*      */       throws SQLException
/*      */     {
/*  288 */       throw SQLError.createSQLException("Not supported");
/*      */     }
/*      */     
/*      */ 
/*      */     public BigDecimal getBigDecimal(String arg0)
/*      */       throws SQLException
/*      */     {
/*  295 */       return null;
/*      */     }
/*      */     
/*      */     public Blob getBlob(int p1) throws SQLException {
/*  299 */       throw SQLError.createSQLException("Not supported");
/*      */     }
/*      */     
/*      */ 
/*      */     public Blob getBlob(String arg0)
/*      */       throws SQLException
/*      */     {
/*  306 */       throw new NotImplemented();
/*      */     }
/*      */     
/*      */     public boolean getBoolean(int p1) throws SQLException {
/*  310 */       throw SQLError.createSQLException("Not supported");
/*      */     }
/*      */     
/*      */ 
/*      */     public boolean getBoolean(String arg0)
/*      */       throws SQLException
/*      */     {
/*  317 */       throw new NotImplemented();
/*      */     }
/*      */     
/*      */     public byte getByte(int p1) throws SQLException {
/*  321 */       throw SQLError.createSQLException("Not supported");
/*      */     }
/*      */     
/*      */ 
/*      */     public byte getByte(String arg0)
/*      */       throws SQLException
/*      */     {
/*  328 */       throw new NotImplemented();
/*      */     }
/*      */     
/*      */     public byte[] getBytes(int p1) throws SQLException {
/*  332 */       throw SQLError.createSQLException("Not supported");
/*      */     }
/*      */     
/*      */ 
/*      */     public byte[] getBytes(String arg0)
/*      */       throws SQLException
/*      */     {
/*  339 */       throw new NotImplemented();
/*      */     }
/*      */     
/*      */     public Clob getClob(int p1) throws SQLException {
/*  343 */       throw SQLError.createSQLException("Not supported");
/*      */     }
/*      */     
/*      */ 
/*      */     public Clob getClob(String arg0)
/*      */       throws SQLException
/*      */     {
/*  350 */       throw new NotImplemented();
/*      */     }
/*      */     
/*      */     public java.sql.Connection getConnection() throws SQLException {
/*  354 */       return this.delegate.getConnection();
/*      */     }
/*      */     
/*      */     public Date getDate(int p1) throws SQLException {
/*  358 */       throw SQLError.createSQLException("Not supported");
/*      */     }
/*      */     
/*      */     public Date getDate(int p1, Calendar p2) throws SQLException
/*      */     {
/*  363 */       throw SQLError.createSQLException("Not supported");
/*      */     }
/*      */     
/*      */ 
/*      */     public Date getDate(String arg0)
/*      */       throws SQLException
/*      */     {
/*  370 */       throw new NotImplemented();
/*      */     }
/*      */     
/*      */ 
/*      */     public Date getDate(String arg0, Calendar arg1)
/*      */       throws SQLException
/*      */     {
/*  377 */       throw new NotImplemented();
/*      */     }
/*      */     
/*      */     public double getDouble(int p1) throws SQLException {
/*  381 */       throw SQLError.createSQLException("Not supported");
/*      */     }
/*      */     
/*      */ 
/*      */     public double getDouble(String arg0)
/*      */       throws SQLException
/*      */     {
/*  388 */       throw new NotImplemented();
/*      */     }
/*      */     
/*      */     public int getFetchDirection() throws SQLException {
/*  392 */       return this.delegate.getFetchDirection();
/*      */     }
/*      */     
/*      */     public int getFetchSize() throws SQLException {
/*  396 */       return this.delegate.getFetchSize();
/*      */     }
/*      */     
/*      */     public float getFloat(int p1) throws SQLException {
/*  400 */       throw SQLError.createSQLException("Not supported");
/*      */     }
/*      */     
/*      */ 
/*      */     public float getFloat(String arg0)
/*      */       throws SQLException
/*      */     {
/*  407 */       throw new NotImplemented();
/*      */     }
/*      */     
/*      */ 
/*      */     public java.sql.ResultSet getGeneratedKeys()
/*      */       throws SQLException
/*      */     {
/*  414 */       return this.delegate.getGeneratedKeys();
/*      */     }
/*      */     
/*      */     public int getInt(int p1) throws SQLException {
/*  418 */       throw SQLError.createSQLException("Not supported");
/*      */     }
/*      */     
/*      */ 
/*      */     public int getInt(String arg0)
/*      */       throws SQLException
/*      */     {
/*  425 */       throw new NotImplemented();
/*      */     }
/*      */     
/*      */     public long getLong(int p1) throws SQLException {
/*  429 */       throw SQLError.createSQLException("Not supported");
/*      */     }
/*      */     
/*      */ 
/*      */     public long getLong(String arg0)
/*      */       throws SQLException
/*      */     {
/*  436 */       throw new NotImplemented();
/*      */     }
/*      */     
/*      */     public int getMaxFieldSize() throws SQLException {
/*  440 */       return this.delegate.getMaxFieldSize();
/*      */     }
/*      */     
/*      */     public int getMaxRows() throws SQLException {
/*  444 */       return this.delegate.getMaxRows();
/*      */     }
/*      */     
/*      */     public ResultSetMetaData getMetaData() throws SQLException {
/*  448 */       throw SQLError.createSQLException("Not supported");
/*      */     }
/*      */     
/*      */     public boolean getMoreResults() throws SQLException {
/*  452 */       return this.delegate.getMoreResults();
/*      */     }
/*      */     
/*      */ 
/*      */     public boolean getMoreResults(int arg0)
/*      */       throws SQLException
/*      */     {
/*  459 */       return this.delegate.getMoreResults();
/*      */     }
/*      */     
/*      */     public Object getObject(int p1) throws SQLException {
/*  463 */       throw SQLError.createSQLException("Not supported");
/*      */     }
/*      */     
/*      */     public Object getObject(int p1, Map p2) throws SQLException
/*      */     {
/*  468 */       throw SQLError.createSQLException("Not supported");
/*      */     }
/*      */     
/*      */ 
/*      */     public Object getObject(String arg0)
/*      */       throws SQLException
/*      */     {
/*  475 */       throw new NotImplemented();
/*      */     }
/*      */     
/*      */ 
/*      */     public Object getObject(String arg0, Map arg1)
/*      */       throws SQLException
/*      */     {
/*  482 */       throw new NotImplemented();
/*      */     }
/*      */     
/*      */ 
/*      */     public ParameterMetaData getParameterMetaData()
/*      */       throws SQLException
/*      */     {
/*  489 */       return this.delegate.getParameterMetaData();
/*      */     }
/*      */     
/*      */     public int getQueryTimeout() throws SQLException {
/*  493 */       return this.delegate.getQueryTimeout();
/*      */     }
/*      */     
/*      */     public Ref getRef(int p1) throws SQLException {
/*  497 */       throw SQLError.createSQLException("Not supported");
/*      */     }
/*      */     
/*      */ 
/*      */     public Ref getRef(String arg0)
/*      */       throws SQLException
/*      */     {
/*  504 */       throw new NotImplemented();
/*      */     }
/*      */     
/*      */     public java.sql.ResultSet getResultSet() throws SQLException {
/*  508 */       return this.delegate.getResultSet();
/*      */     }
/*      */     
/*      */     public int getResultSetConcurrency() throws SQLException {
/*  512 */       return this.delegate.getResultSetConcurrency();
/*      */     }
/*      */     
/*      */ 
/*      */     public int getResultSetHoldability()
/*      */       throws SQLException
/*      */     {
/*  519 */       return this.delegate.getResultSetHoldability();
/*      */     }
/*      */     
/*      */     public int getResultSetType() throws SQLException {
/*  523 */       return this.delegate.getResultSetType();
/*      */     }
/*      */     
/*      */     public short getShort(int p1) throws SQLException {
/*  527 */       throw SQLError.createSQLException("Not supported");
/*      */     }
/*      */     
/*      */ 
/*      */     public short getShort(String arg0)
/*      */       throws SQLException
/*      */     {
/*  534 */       throw new NotImplemented();
/*      */     }
/*      */     
/*      */     public String getString(int p1) throws SQLException {
/*  538 */       throw SQLError.createSQLException("Not supported");
/*      */     }
/*      */     
/*      */ 
/*      */     public String getString(String arg0)
/*      */       throws SQLException
/*      */     {
/*  545 */       throw new NotImplemented();
/*      */     }
/*      */     
/*      */     public Time getTime(int p1) throws SQLException {
/*  549 */       throw SQLError.createSQLException("Not supported");
/*      */     }
/*      */     
/*      */     public Time getTime(int p1, Calendar p2) throws SQLException
/*      */     {
/*  554 */       throw SQLError.createSQLException("Not supported");
/*      */     }
/*      */     
/*      */ 
/*      */     public Time getTime(String arg0)
/*      */       throws SQLException
/*      */     {
/*  561 */       throw new NotImplemented();
/*      */     }
/*      */     
/*      */ 
/*      */     public Time getTime(String arg0, Calendar arg1)
/*      */       throws SQLException
/*      */     {
/*  568 */       throw new NotImplemented();
/*      */     }
/*      */     
/*      */     public Timestamp getTimestamp(int p1) throws SQLException {
/*  572 */       throw SQLError.createSQLException("Not supported");
/*      */     }
/*      */     
/*      */     public Timestamp getTimestamp(int p1, Calendar p2) throws SQLException
/*      */     {
/*  577 */       throw SQLError.createSQLException("Not supported");
/*      */     }
/*      */     
/*      */ 
/*      */     public Timestamp getTimestamp(String arg0)
/*      */       throws SQLException
/*      */     {
/*  584 */       throw new NotImplemented();
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */     public Timestamp getTimestamp(String arg0, Calendar arg1)
/*      */       throws SQLException
/*      */     {
/*  592 */       throw new NotImplemented();
/*      */     }
/*      */     
/*      */     public int getUpdateCount() throws SQLException {
/*  596 */       return this.delegate.getUpdateCount();
/*      */     }
/*      */     
/*      */ 
/*      */     public URL getURL(int arg0)
/*      */       throws SQLException
/*      */     {
/*  603 */       throw new NotImplemented();
/*      */     }
/*      */     
/*      */ 
/*      */     public URL getURL(String arg0)
/*      */       throws SQLException
/*      */     {
/*  610 */       throw new NotImplemented();
/*      */     }
/*      */     
/*      */     public SQLWarning getWarnings() throws SQLException {
/*  614 */       return this.delegate.getWarnings();
/*      */     }
/*      */     
/*      */     public void registerOutParameter(int p1, int p2) throws SQLException {
/*  618 */       throw SQLError.createSQLException("Not supported");
/*      */     }
/*      */     
/*      */     public void registerOutParameter(int p1, int p2, int p3) throws SQLException
/*      */     {
/*  623 */       throw SQLError.createSQLException("Not supported");
/*      */     }
/*      */     
/*      */     public void registerOutParameter(int p1, int p2, String p3) throws SQLException
/*      */     {
/*  628 */       throw SQLError.createSQLException("Not supported");
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */     public void registerOutParameter(String arg0, int arg1)
/*      */       throws SQLException
/*      */     {
/*  636 */       throw new NotImplemented();
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */     public void registerOutParameter(String arg0, int arg1, int arg2)
/*      */       throws SQLException
/*      */     {
/*  644 */       throw new NotImplemented();
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */     public void registerOutParameter(String arg0, int arg1, String arg2)
/*      */       throws SQLException
/*      */     {
/*  652 */       throw new NotImplemented();
/*      */     }
/*      */     
/*      */     public void setArray(int p1, java.sql.Array p2) throws SQLException
/*      */     {
/*  657 */       this.delegate.setArray(p1, p2);
/*      */     }
/*      */     
/*      */     public void setAsciiStream(int p1, InputStream p2, int p3) throws SQLException
/*      */     {
/*  662 */       this.delegate.setAsciiStream(p1, p2, p3);
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */     public void setAsciiStream(String arg0, InputStream arg1, int arg2)
/*      */       throws SQLException
/*      */     {
/*  670 */       throw new NotImplemented();
/*      */     }
/*      */     
/*      */     public void setBigDecimal(int p1, BigDecimal p2) throws SQLException
/*      */     {
/*  675 */       this.delegate.setBigDecimal(p1, p2);
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */     public void setBigDecimal(String arg0, BigDecimal arg1)
/*      */       throws SQLException
/*      */     {
/*  683 */       throw new NotImplemented();
/*      */     }
/*      */     
/*      */     public void setBinaryStream(int p1, InputStream p2, int p3) throws SQLException
/*      */     {
/*  688 */       this.delegate.setBinaryStream(p1, p2, p3);
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */     public void setBinaryStream(String arg0, InputStream arg1, int arg2)
/*      */       throws SQLException
/*      */     {
/*  696 */       throw new NotImplemented();
/*      */     }
/*      */     
/*      */     public void setBlob(int p1, Blob p2) throws SQLException {
/*  700 */       this.delegate.setBlob(p1, p2);
/*      */     }
/*      */     
/*      */     public void setBoolean(int p1, boolean p2) throws SQLException {
/*  704 */       this.delegate.setBoolean(p1, p2);
/*      */     }
/*      */     
/*      */ 
/*      */     public void setBoolean(String arg0, boolean arg1)
/*      */       throws SQLException
/*      */     {
/*  711 */       throw new NotImplemented();
/*      */     }
/*      */     
/*      */     public void setByte(int p1, byte p2) throws SQLException {
/*  715 */       this.delegate.setByte(p1, p2);
/*      */     }
/*      */     
/*      */ 
/*      */     public void setByte(String arg0, byte arg1)
/*      */       throws SQLException
/*      */     {
/*  722 */       throw new NotImplemented();
/*      */     }
/*      */     
/*      */     public void setBytes(int p1, byte[] p2) throws SQLException {
/*  726 */       this.delegate.setBytes(p1, p2);
/*      */     }
/*      */     
/*      */ 
/*      */     public void setBytes(String arg0, byte[] arg1)
/*      */       throws SQLException
/*      */     {
/*  733 */       throw new NotImplemented();
/*      */     }
/*      */     
/*      */     public void setCharacterStream(int p1, Reader p2, int p3) throws SQLException
/*      */     {
/*  738 */       this.delegate.setCharacterStream(p1, p2, p3);
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */     public void setCharacterStream(String arg0, Reader arg1, int arg2)
/*      */       throws SQLException
/*      */     {
/*  746 */       throw new NotImplemented();
/*      */     }
/*      */     
/*      */     public void setClob(int p1, Clob p2) throws SQLException {
/*  750 */       this.delegate.setClob(p1, p2);
/*      */     }
/*      */     
/*      */     public void setCursorName(String p1) throws SQLException {
/*  754 */       throw SQLError.createSQLException("Not supported");
/*      */     }
/*      */     
/*      */     public void setDate(int p1, Date p2) throws SQLException {
/*  758 */       this.delegate.setDate(p1, p2);
/*      */     }
/*      */     
/*      */     public void setDate(int p1, Date p2, Calendar p3) throws SQLException
/*      */     {
/*  763 */       this.delegate.setDate(p1, p2, p3);
/*      */     }
/*      */     
/*      */ 
/*      */     public void setDate(String arg0, Date arg1)
/*      */       throws SQLException
/*      */     {
/*  770 */       throw new NotImplemented();
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */     public void setDate(String arg0, Date arg1, Calendar arg2)
/*      */       throws SQLException
/*      */     {
/*  778 */       throw new NotImplemented();
/*      */     }
/*      */     
/*      */     public void setDouble(int p1, double p2) throws SQLException {
/*  782 */       this.delegate.setDouble(p1, p2);
/*      */     }
/*      */     
/*      */ 
/*      */     public void setDouble(String arg0, double arg1)
/*      */       throws SQLException
/*      */     {
/*  789 */       throw new NotImplemented();
/*      */     }
/*      */     
/*      */     public void setEscapeProcessing(boolean p1) throws SQLException {
/*  793 */       this.delegate.setEscapeProcessing(p1);
/*      */     }
/*      */     
/*      */     public void setFetchDirection(int p1) throws SQLException {
/*  797 */       this.delegate.setFetchDirection(p1);
/*      */     }
/*      */     
/*      */     public void setFetchSize(int p1) throws SQLException {
/*  801 */       this.delegate.setFetchSize(p1);
/*      */     }
/*      */     
/*      */     public void setFloat(int p1, float p2) throws SQLException {
/*  805 */       this.delegate.setFloat(p1, p2);
/*      */     }
/*      */     
/*      */ 
/*      */     public void setFloat(String arg0, float arg1)
/*      */       throws SQLException
/*      */     {
/*  812 */       throw new NotImplemented();
/*      */     }
/*      */     
/*      */     public void setInt(int p1, int p2) throws SQLException {
/*  816 */       this.delegate.setInt(p1, p2);
/*      */     }
/*      */     
/*      */ 
/*      */     public void setInt(String arg0, int arg1)
/*      */       throws SQLException
/*      */     {
/*  823 */       throw new NotImplemented();
/*      */     }
/*      */     
/*      */     public void setLong(int p1, long p2) throws SQLException {
/*  827 */       this.delegate.setLong(p1, p2);
/*      */     }
/*      */     
/*      */ 
/*      */     public void setLong(String arg0, long arg1)
/*      */       throws SQLException
/*      */     {
/*  834 */       throw new NotImplemented();
/*      */     }
/*      */     
/*      */     public void setMaxFieldSize(int p1) throws SQLException {
/*  838 */       this.delegate.setMaxFieldSize(p1);
/*      */     }
/*      */     
/*      */     public void setMaxRows(int p1) throws SQLException {
/*  842 */       this.delegate.setMaxRows(p1);
/*      */     }
/*      */     
/*      */     public void setNull(int p1, int p2) throws SQLException {
/*  846 */       this.delegate.setNull(p1, p2);
/*      */     }
/*      */     
/*      */     public void setNull(int p1, int p2, String p3) throws SQLException
/*      */     {
/*  851 */       this.delegate.setNull(p1, p2, p3);
/*      */     }
/*      */     
/*      */ 
/*      */     public void setNull(String arg0, int arg1)
/*      */       throws SQLException
/*      */     {
/*  858 */       throw new NotImplemented();
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */     public void setNull(String arg0, int arg1, String arg2)
/*      */       throws SQLException
/*      */     {
/*  866 */       throw new NotImplemented();
/*      */     }
/*      */     
/*      */     public void setObject(int p1, Object p2) throws SQLException
/*      */     {
/*  871 */       this.delegate.setObject(p1, p2);
/*      */     }
/*      */     
/*      */     public void setObject(int p1, Object p2, int p3) throws SQLException
/*      */     {
/*  876 */       this.delegate.setObject(p1, p2, p3);
/*      */     }
/*      */     
/*      */     public void setObject(int p1, Object p2, int p3, int p4) throws SQLException
/*      */     {
/*  881 */       this.delegate.setObject(p1, p2, p3, p4);
/*      */     }
/*      */     
/*      */ 
/*      */     public void setObject(String arg0, Object arg1)
/*      */       throws SQLException
/*      */     {
/*  888 */       throw new NotImplemented();
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */     public void setObject(String arg0, Object arg1, int arg2)
/*      */       throws SQLException
/*      */     {
/*  896 */       throw new NotImplemented();
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */     public void setObject(String arg0, Object arg1, int arg2, int arg3)
/*      */       throws SQLException
/*      */     {
/*  904 */       throw new NotImplemented();
/*      */     }
/*      */     
/*      */     public void setQueryTimeout(int p1) throws SQLException {
/*  908 */       throw SQLError.createSQLException("Not supported");
/*      */     }
/*      */     
/*      */     public void setRef(int p1, Ref p2) throws SQLException {
/*  912 */       throw SQLError.createSQLException("Not supported");
/*      */     }
/*      */     
/*      */     public void setShort(int p1, short p2) throws SQLException {
/*  916 */       this.delegate.setShort(p1, p2);
/*      */     }
/*      */     
/*      */ 
/*      */     public void setShort(String arg0, short arg1)
/*      */       throws SQLException
/*      */     {
/*  923 */       throw new NotImplemented();
/*      */     }
/*      */     
/*      */     public void setString(int p1, String p2) throws SQLException
/*      */     {
/*  928 */       this.delegate.setString(p1, p2);
/*      */     }
/*      */     
/*      */ 
/*      */     public void setString(String arg0, String arg1)
/*      */       throws SQLException
/*      */     {
/*  935 */       throw new NotImplemented();
/*      */     }
/*      */     
/*      */     public void setTime(int p1, Time p2) throws SQLException {
/*  939 */       this.delegate.setTime(p1, p2);
/*      */     }
/*      */     
/*      */     public void setTime(int p1, Time p2, Calendar p3) throws SQLException
/*      */     {
/*  944 */       this.delegate.setTime(p1, p2, p3);
/*      */     }
/*      */     
/*      */ 
/*      */     public void setTime(String arg0, Time arg1)
/*      */       throws SQLException
/*      */     {
/*  951 */       throw new NotImplemented();
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */     public void setTime(String arg0, Time arg1, Calendar arg2)
/*      */       throws SQLException
/*      */     {
/*  959 */       throw new NotImplemented();
/*      */     }
/*      */     
/*      */     public void setTimestamp(int p1, Timestamp p2) throws SQLException
/*      */     {
/*  964 */       this.delegate.setTimestamp(p1, p2);
/*      */     }
/*      */     
/*      */     public void setTimestamp(int p1, Timestamp p2, Calendar p3) throws SQLException
/*      */     {
/*  969 */       this.delegate.setTimestamp(p1, p2, p3);
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */     public void setTimestamp(String arg0, Timestamp arg1)
/*      */       throws SQLException
/*      */     {
/*  977 */       throw new NotImplemented();
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */     public void setTimestamp(String arg0, Timestamp arg1, Calendar arg2)
/*      */       throws SQLException
/*      */     {
/*  985 */       throw new NotImplemented();
/*      */     }
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
/*      */     /**
/*      */      * @deprecated
/*      */      */
/*      */     public void setUnicodeStream(int p1, InputStream p2, int p3)
/*      */       throws SQLException
/*      */     {
/* 1003 */       this.delegate.setUnicodeStream(p1, p2, p3);
/*      */     }
/*      */     
/*      */ 
/*      */     public void setURL(int arg0, URL arg1)
/*      */       throws SQLException
/*      */     {
/* 1010 */       this.delegate.setURL(arg0, arg1);
/*      */     }
/*      */     
/*      */ 
/*      */     public void setURL(String arg0, URL arg1)
/*      */       throws SQLException
/*      */     {
/* 1017 */       throw new NotImplemented();
/*      */     }
/*      */     
/*      */     public boolean wasNull() throws SQLException {
/* 1021 */       throw SQLError.createSQLException("Not supported");
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 1029 */   private static final Object CHARSET_CONVERTER_NOT_AVAILABLE_MARKER = new Object();
/*      */   
/*      */ 
/*      */ 
/*      */   public static Map charsetMap;
/*      */   
/*      */ 
/*      */ 
/*      */   protected static final String DEFAULT_LOGGER_CLASS = "com.mysql.jdbc.log.StandardLogger";
/*      */   
/*      */ 
/*      */ 
/*      */   private static final int HISTOGRAM_BUCKETS = 20;
/*      */   
/*      */ 
/*      */ 
/*      */   private static final String LOGGER_INSTANCE_NAME = "MySQL";
/*      */   
/*      */ 
/*      */ 
/* 1049 */   private static Map mapTransIsolationNameToValue = null;
/*      */   
/*      */ 
/* 1052 */   private static final Log NULL_LOGGER = new NullLogger("MySQL");
/*      */   
/*      */   private static Map roundRobinStatsMap;
/*      */   
/* 1056 */   private static final Map serverCollationByUrl = new HashMap();
/*      */   
/* 1058 */   private static final Map serverConfigByUrl = new HashMap();
/*      */   private static Timer cancelTimer;
/*      */   
/*      */   static
/*      */   {
/* 1063 */     mapTransIsolationNameToValue = new HashMap(8);
/* 1064 */     mapTransIsolationNameToValue.put("READ-UNCOMMITED", new Integer(1));
/*      */     
/* 1066 */     mapTransIsolationNameToValue.put("READ-UNCOMMITTED", new Integer(1));
/*      */     
/* 1068 */     mapTransIsolationNameToValue.put("READ-COMMITTED", new Integer(2));
/*      */     
/* 1070 */     mapTransIsolationNameToValue.put("REPEATABLE-READ", new Integer(4));
/*      */     
/* 1072 */     mapTransIsolationNameToValue.put("SERIALIZABLE", new Integer(8));
/*      */     
/*      */ 
/* 1075 */     boolean createdNamedTimer = false;
/*      */     
/*      */ 
/*      */     try
/*      */     {
/* 1080 */       Constructor ctr = class$java$util$Timer.getConstructor(new Class[] { String.class, Boolean.TYPE });
/*      */       
/* 1082 */       cancelTimer = (Timer)ctr.newInstance(new Object[] { "MySQL Statement Cancellation Timer", Boolean.TRUE });
/* 1083 */       createdNamedTimer = true;
/*      */     } catch (Throwable t) {
/* 1085 */       createdNamedTimer = false;
/*      */     }
/*      */     
/* 1088 */     if (!createdNamedTimer) {
/* 1089 */       cancelTimer = new Timer(true);
/*      */     }
/*      */   }
/*      */   
/*      */   protected static SQLException appendMessageToException(SQLException sqlEx, String messageToAppend)
/*      */   {
/* 1095 */     String origMessage = sqlEx.getMessage();
/* 1096 */     String sqlState = sqlEx.getSQLState();
/* 1097 */     int vendorErrorCode = sqlEx.getErrorCode();
/*      */     
/* 1099 */     StringBuffer messageBuf = new StringBuffer(origMessage.length() + messageToAppend.length());
/*      */     
/* 1101 */     messageBuf.append(origMessage);
/* 1102 */     messageBuf.append(messageToAppend);
/*      */     
/* 1104 */     SQLException sqlExceptionWithNewMessage = SQLError.createSQLException(messageBuf.toString(), sqlState, vendorErrorCode);
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     try
/*      */     {
/* 1114 */       Method getStackTraceMethod = null;
/* 1115 */       Method setStackTraceMethod = null;
/* 1116 */       Object theStackTraceAsObject = null;
/*      */       
/* 1118 */       Class stackTraceElementClass = Class.forName("java.lang.StackTraceElement");
/*      */       
/* 1120 */       Class stackTraceElementArrayClass = java.lang.reflect.Array.newInstance(stackTraceElementClass, new int[] { 0 }).getClass();
/*      */       
/*      */ 
/* 1123 */       getStackTraceMethod = Throwable.class.getMethod("getStackTrace", new Class[0]);
/*      */       
/*      */ 
/* 1126 */       setStackTraceMethod = class$java$lang$Throwable.getMethod("setStackTrace", new Class[] { stackTraceElementArrayClass });
/*      */       
/*      */ 
/* 1129 */       if ((getStackTraceMethod != null) && (setStackTraceMethod != null)) {
/* 1130 */         theStackTraceAsObject = getStackTraceMethod.invoke(sqlEx, new Object[0]);
/*      */         
/* 1132 */         setStackTraceMethod.invoke(sqlExceptionWithNewMessage, new Object[] { theStackTraceAsObject });
/*      */       }
/*      */     }
/*      */     catch (NoClassDefFoundError noClassDefFound) {}catch (NoSuchMethodException noSuchMethodEx) {}catch (Throwable catchAll) {}
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 1143 */     return sqlExceptionWithNewMessage;
/*      */   }
/*      */   
/*      */   protected static Timer getCancelTimer() {
/* 1147 */     return cancelTimer;
/*      */   }
/*      */   
/*      */   private static synchronized int getNextRoundRobinHostIndex(String url, List hostList)
/*      */   {
/* 1152 */     if (roundRobinStatsMap == null) {
/* 1153 */       roundRobinStatsMap = new HashMap();
/*      */     }
/*      */     
/* 1156 */     int[] index = (int[])roundRobinStatsMap.get(url);
/*      */     
/* 1158 */     if (index == null) {
/* 1159 */       index = new int[1];
/* 1160 */       index[0] = -1;
/*      */       
/* 1162 */       roundRobinStatsMap.put(url, index);
/*      */     }
/*      */     
/* 1165 */     index[0] += 1;
/*      */     
/* 1167 */     if (index[0] >= hostList.size()) {
/* 1168 */       index[0] = 0;
/*      */     }
/*      */     
/* 1171 */     return index[0];
/*      */   }
/*      */   
/*      */   private static boolean nullSafeCompare(String s1, String s2) {
/* 1175 */     if ((s1 == null) && (s2 == null)) {
/* 1176 */       return true;
/*      */     }
/*      */     
/* 1179 */     if ((s1 == null) && (s2 != null)) {
/* 1180 */       return false;
/*      */     }
/*      */     
/* 1183 */     return s1.equals(s2);
/*      */   }
/*      */   
/*      */ 
/* 1187 */   private boolean autoCommit = true;
/*      */   
/*      */ 
/*      */ 
/*      */   private Map cachedPreparedStatementParams;
/*      */   
/*      */ 
/*      */ 
/* 1195 */   private String characterSetMetadata = null;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 1201 */   private String characterSetResultsOnServer = null;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 1208 */   private Map charsetConverterMap = new HashMap(CharsetMapping.getNumberOfCharsetsConfigured());
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   private Map charsetToNumBytesMap;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/* 1218 */   private long connectionCreationTimeMillis = 0L;
/*      */   
/*      */ 
/*      */   private long connectionId;
/*      */   
/*      */ 
/* 1224 */   private String database = null;
/*      */   
/*      */ 
/* 1227 */   private DatabaseMetaData dbmd = null;
/*      */   
/*      */ 
/*      */   private TimeZone defaultTimeZone;
/*      */   
/*      */   private ProfileEventSink eventSink;
/*      */   
/* 1234 */   private boolean executingFailoverReconnect = false;
/*      */   
/*      */ 
/* 1237 */   private boolean failedOver = false;
/*      */   
/*      */ 
/*      */   private Throwable forceClosedReason;
/*      */   
/*      */ 
/*      */   private Throwable forcedClosedLocation;
/*      */   
/*      */ 
/* 1246 */   private boolean hasIsolationLevels = false;
/*      */   
/*      */ 
/* 1249 */   private boolean hasQuotedIdentifiers = false;
/*      */   
/*      */ 
/* 1252 */   private String host = null;
/*      */   
/*      */ 
/* 1255 */   private List hostList = null;
/*      */   
/*      */ 
/* 1258 */   private int hostListSize = 0;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 1264 */   private String[] indexToCharsetMapping = CharsetMapping.INDEX_TO_CHARSET;
/*      */   
/*      */ 
/* 1267 */   private MysqlIO io = null;
/*      */   
/* 1269 */   private boolean isClientTzUTC = false;
/*      */   
/*      */ 
/* 1272 */   private boolean isClosed = true;
/*      */   
/*      */ 
/* 1275 */   private boolean isInGlobalTx = false;
/*      */   
/*      */ 
/* 1278 */   private boolean isRunningOnJDK13 = false;
/*      */   
/*      */ 
/* 1281 */   private int isolationLevel = 2;
/*      */   
/* 1283 */   private boolean isServerTzUTC = false;
/*      */   
/*      */ 
/* 1286 */   private long lastQueryFinishedTime = 0L;
/*      */   
/*      */ 
/* 1289 */   private Log log = NULL_LOGGER;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 1295 */   private long longestQueryTimeMs = 0L;
/*      */   
/*      */ 
/* 1298 */   private boolean lowerCaseTableNames = false;
/*      */   
/*      */ 
/* 1301 */   private long masterFailTimeMillis = 0L;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 1307 */   private int maxAllowedPacket = 65536;
/*      */   
/* 1309 */   private long maximumNumberTablesAccessed = 0L;
/*      */   
/*      */ 
/* 1312 */   private boolean maxRowsChanged = false;
/*      */   
/*      */ 
/*      */   private long metricsLastReportedMs;
/*      */   
/* 1317 */   private long minimumNumberTablesAccessed = Long.MAX_VALUE;
/*      */   
/*      */ 
/* 1320 */   private final Object mutex = new Object();
/*      */   
/*      */ 
/* 1323 */   private String myURL = null;
/*      */   
/*      */ 
/* 1326 */   private boolean needsPing = false;
/*      */   
/* 1328 */   private int netBufferLength = 16384;
/*      */   
/* 1330 */   private boolean noBackslashEscapes = false;
/*      */   
/* 1332 */   private long numberOfPreparedExecutes = 0L;
/*      */   
/* 1334 */   private long numberOfPrepares = 0L;
/*      */   
/* 1336 */   private long numberOfQueriesIssued = 0L;
/*      */   
/* 1338 */   private long numberOfResultSetsCreated = 0L;
/*      */   
/*      */   private long[] numTablesMetricsHistBreakpoints;
/*      */   
/*      */   private int[] numTablesMetricsHistCounts;
/*      */   
/* 1344 */   private long[] oldHistBreakpoints = null;
/*      */   
/* 1346 */   private int[] oldHistCounts = null;
/*      */   
/*      */ 
/*      */   private Map openStatements;
/*      */   
/*      */   private LRUCache parsedCallableStatementCache;
/*      */   
/* 1353 */   private boolean parserKnowsUnicode = false;
/*      */   
/*      */ 
/* 1356 */   private String password = null;
/*      */   
/*      */ 
/*      */   private long[] perfMetricsHistBreakpoints;
/*      */   
/*      */ 
/*      */   private int[] perfMetricsHistCounts;
/*      */   
/*      */   private Throwable pointOfOrigin;
/*      */   
/* 1366 */   private int port = 3306;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 1372 */   private boolean preferSlaveDuringFailover = false;
/*      */   
/*      */ 
/* 1375 */   private Properties props = null;
/*      */   
/*      */ 
/* 1378 */   private long queriesIssuedFailedOver = 0L;
/*      */   
/*      */ 
/* 1381 */   private boolean readInfoMsg = false;
/*      */   
/*      */ 
/* 1384 */   private boolean readOnly = false;
/*      */   
/*      */ 
/*      */   protected LRUCache resultSetMetadataCache;
/*      */   
/*      */ 
/* 1390 */   private TimeZone serverTimezoneTZ = null;
/*      */   
/*      */ 
/* 1393 */   private Map serverVariables = null;
/*      */   
/* 1395 */   private long shortestQueryTimeMs = Long.MAX_VALUE;
/*      */   
/*      */ 
/*      */   private Map statementsUsingMaxRows;
/*      */   
/* 1400 */   private double totalQueryTimeMs = 0.0D;
/*      */   
/*      */ 
/* 1403 */   private boolean transactionsSupported = false;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   private Map typeMap;
/*      */   
/*      */ 
/*      */ 
/* 1412 */   private boolean useAnsiQuotes = false;
/*      */   
/*      */ 
/* 1415 */   private String user = null;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 1421 */   private boolean useServerPreparedStmts = false;
/*      */   
/*      */   private LRUCache serverSideStatementCheckCache;
/*      */   
/*      */   private LRUCache serverSideStatementCache;
/*      */   
/*      */   private Calendar sessionCalendar;
/*      */   
/*      */   private Calendar utcCalendar;
/*      */   
/*      */   private String origHostToConnectTo;
/*      */   
/*      */   private int origPortToConnectTo;
/*      */   
/*      */   private String origDatabaseToConnectTo;
/*      */   
/* 1437 */   private String errorMessageEncoding = "Cp1252";
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
/*      */   private boolean usePlatformCharsetConverters;
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
/*      */   Connection(String hostToConnectTo, int portToConnectTo, Properties info, String databaseToConnectTo, String url)
/*      */     throws SQLException
/*      */   {
/* 1463 */     this.charsetToNumBytesMap = new HashMap();
/*      */     
/* 1465 */     this.connectionCreationTimeMillis = System.currentTimeMillis();
/* 1466 */     this.pointOfOrigin = new Throwable();
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 1472 */     this.origHostToConnectTo = hostToConnectTo;
/* 1473 */     this.origPortToConnectTo = portToConnectTo;
/* 1474 */     this.origDatabaseToConnectTo = databaseToConnectTo;
/*      */     try
/*      */     {
/* 1477 */       class$java$sql$Blob.getMethod("truncate", new Class[] { Long.TYPE });
/*      */       
/* 1479 */       this.isRunningOnJDK13 = false;
/*      */     } catch (NoSuchMethodException nsme) {
/* 1481 */       this.isRunningOnJDK13 = true;
/*      */     }
/*      */     
/* 1484 */     this.sessionCalendar = new GregorianCalendar();
/* 1485 */     this.utcCalendar = new GregorianCalendar();
/* 1486 */     this.utcCalendar.setTimeZone(TimeZone.getTimeZone("GMT"));
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
/* 1498 */     this.log = LogFactory.getLogger(getLogger(), "MySQL");
/*      */     
/*      */ 
/*      */ 
/* 1502 */     this.defaultTimeZone = Util.getDefaultTimeZone();
/*      */     
/* 1504 */     if ("GMT".equalsIgnoreCase(this.defaultTimeZone.getID())) {
/* 1505 */       this.isClientTzUTC = true;
/*      */     } else {
/* 1507 */       this.isClientTzUTC = false;
/*      */     }
/*      */     
/* 1510 */     this.openStatements = new HashMap();
/* 1511 */     this.serverVariables = new HashMap();
/* 1512 */     this.hostList = new ArrayList();
/*      */     
/* 1514 */     if (hostToConnectTo == null) {
/* 1515 */       this.host = "localhost";
/* 1516 */       this.hostList.add(this.host);
/* 1517 */     } else if (hostToConnectTo.indexOf(",") != -1)
/*      */     {
/* 1519 */       StringTokenizer hostTokenizer = new StringTokenizer(hostToConnectTo, ",", false);
/*      */       
/*      */ 
/* 1522 */       while (hostTokenizer.hasMoreTokens()) {
/* 1523 */         this.hostList.add(hostTokenizer.nextToken().trim());
/*      */       }
/*      */     } else {
/* 1526 */       this.host = hostToConnectTo;
/* 1527 */       this.hostList.add(this.host);
/*      */     }
/*      */     
/* 1530 */     this.hostListSize = this.hostList.size();
/* 1531 */     this.port = portToConnectTo;
/*      */     
/* 1533 */     if (databaseToConnectTo == null) {
/* 1534 */       databaseToConnectTo = "";
/*      */     }
/*      */     
/* 1537 */     this.database = databaseToConnectTo;
/* 1538 */     this.myURL = url;
/* 1539 */     this.user = info.getProperty("user");
/* 1540 */     this.password = info.getProperty("password");
/*      */     
/*      */ 
/* 1543 */     if ((this.user == null) || (this.user.equals(""))) {
/* 1544 */       this.user = "";
/*      */     }
/*      */     
/* 1547 */     if (this.password == null) {
/* 1548 */       this.password = "";
/*      */     }
/*      */     
/* 1551 */     this.props = info;
/* 1552 */     initializeDriverProperties(info);
/*      */     try
/*      */     {
/* 1555 */       createNewIO(false);
/* 1556 */       this.dbmd = new DatabaseMetaData(this, this.database);
/*      */     } catch (SQLException ex) {
/* 1558 */       cleanup(ex);
/*      */       
/*      */ 
/* 1561 */       throw ex;
/*      */     } catch (Exception ex) {
/* 1563 */       cleanup(ex);
/*      */       
/* 1565 */       StringBuffer mesg = new StringBuffer();
/*      */       
/* 1567 */       if (getParanoid()) {
/* 1568 */         mesg.append("Cannot connect to MySQL server on ");
/* 1569 */         mesg.append(this.host);
/* 1570 */         mesg.append(":");
/* 1571 */         mesg.append(this.port);
/* 1572 */         mesg.append(".\n\n");
/* 1573 */         mesg.append("Make sure that there is a MySQL server ");
/* 1574 */         mesg.append("running on the machine/port you are trying ");
/* 1575 */         mesg.append("to connect to and that the machine this software is running on ");
/*      */         
/*      */ 
/* 1578 */         mesg.append("is able to connect to this host/port (i.e. not firewalled). ");
/*      */         
/* 1580 */         mesg.append("Also make sure that the server has not been started with the --skip-networking ");
/*      */         
/*      */ 
/* 1583 */         mesg.append("flag.\n\n");
/*      */       } else {
/* 1585 */         mesg.append("Unable to connect to database.");
/*      */       }
/*      */       
/* 1588 */       mesg.append("Underlying exception: \n\n");
/* 1589 */       mesg.append(ex.getClass().getName());
/*      */       
/* 1591 */       if (!getParanoid()) {
/* 1592 */         mesg.append(Util.stackTraceToString(ex));
/*      */       }
/*      */       
/* 1595 */       throw SQLError.createSQLException(mesg.toString(), "08S01");
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   private void addToHistogram(int[] histogramCounts, long[] histogramBreakpoints, long value, int numberOfTimes, long currentLowerBound, long currentUpperBound)
/*      */   {
/* 1603 */     if (histogramCounts == null) {
/* 1604 */       createInitialHistogram(histogramBreakpoints, currentLowerBound, currentUpperBound);
/*      */     }
/*      */     
/*      */ 
/* 1608 */     for (int i = 0; i < 20; i++) {
/* 1609 */       if (histogramBreakpoints[i] >= value) {
/* 1610 */         histogramCounts[i] += numberOfTimes;
/*      */         
/* 1612 */         break;
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */   private void addToPerformanceHistogram(long value, int numberOfTimes) {
/* 1618 */     checkAndCreatePerformanceHistogram();
/*      */     
/* 1620 */     addToHistogram(this.perfMetricsHistCounts, this.perfMetricsHistBreakpoints, value, numberOfTimes, this.shortestQueryTimeMs == Long.MAX_VALUE ? 0L : this.shortestQueryTimeMs, this.longestQueryTimeMs);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   private void addToTablesAccessedHistogram(long value, int numberOfTimes)
/*      */   {
/* 1627 */     checkAndCreateTablesAccessedHistogram();
/*      */     
/* 1629 */     addToHistogram(this.numTablesMetricsHistCounts, this.numTablesMetricsHistBreakpoints, value, numberOfTimes, this.minimumNumberTablesAccessed == Long.MAX_VALUE ? 0L : this.minimumNumberTablesAccessed, this.maximumNumberTablesAccessed);
/*      */   }
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
/*      */   private void buildCollationMapping()
/*      */     throws SQLException
/*      */   {
/* 1644 */     if (versionMeetsMinimum(4, 1, 0))
/*      */     {
/* 1646 */       TreeMap sortedCollationMap = null;
/*      */       
/* 1648 */       if (getCacheServerConfiguration()) {
/* 1649 */         synchronized (serverConfigByUrl) {
/* 1650 */           sortedCollationMap = (TreeMap)serverCollationByUrl.get(getURL());
/*      */         }
/*      */       }
/*      */       
/*      */ 
/* 1655 */       Statement stmt = null;
/* 1656 */       ResultSet results = null;
/*      */       try
/*      */       {
/* 1659 */         if (sortedCollationMap == null) {
/* 1660 */           sortedCollationMap = new TreeMap();
/*      */           
/* 1662 */           stmt = (Statement)createStatement();
/*      */           
/* 1664 */           if (stmt.getMaxRows() != 0) {
/* 1665 */             stmt.setMaxRows(0);
/*      */           }
/*      */           
/* 1668 */           results = (ResultSet)stmt.executeQuery("SHOW COLLATION");
/*      */           
/*      */ 
/* 1671 */           while (results.next()) {
/* 1672 */             String charsetName = results.getString(2);
/* 1673 */             Integer charsetIndex = new Integer(results.getInt(3));
/*      */             
/* 1675 */             sortedCollationMap.put(charsetIndex, charsetName);
/*      */           }
/*      */           
/* 1678 */           if (getCacheServerConfiguration()) {
/* 1679 */             synchronized (serverConfigByUrl) {
/* 1680 */               serverCollationByUrl.put(getURL(), sortedCollationMap);
/*      */             }
/*      */           }
/*      */         }
/*      */         
/*      */ 
/*      */ 
/*      */ 
/* 1688 */         int highestIndex = ((Integer)sortedCollationMap.lastKey()).intValue();
/*      */         
/*      */ 
/* 1691 */         if (CharsetMapping.INDEX_TO_CHARSET.length > highestIndex) {
/* 1692 */           highestIndex = CharsetMapping.INDEX_TO_CHARSET.length;
/*      */         }
/*      */         
/* 1695 */         this.indexToCharsetMapping = new String[highestIndex + 1];
/*      */         
/* 1697 */         for (int i = 0; i < CharsetMapping.INDEX_TO_CHARSET.length; i++) {
/* 1698 */           this.indexToCharsetMapping[i] = CharsetMapping.INDEX_TO_CHARSET[i];
/*      */         }
/*      */         
/* 1701 */         Iterator indexIter = sortedCollationMap.entrySet().iterator();
/* 1702 */         while (indexIter.hasNext()) {
/* 1703 */           Map.Entry indexEntry = (Map.Entry)indexIter.next();
/*      */           
/* 1705 */           String mysqlCharsetName = (String)indexEntry.getValue();
/*      */           
/* 1707 */           this.indexToCharsetMapping[((Integer)indexEntry.getKey()).intValue()] = CharsetMapping.getJavaEncodingForMysqlEncoding(mysqlCharsetName, this);
/*      */         }
/*      */         
/*      */       }
/*      */       catch (SQLException e)
/*      */       {
/* 1713 */         throw e;
/*      */       } finally {
/* 1715 */         if (results != null) {
/*      */           try {
/* 1717 */             results.close();
/*      */           }
/*      */           catch (SQLException sqlE) {}
/*      */         }
/*      */         
/*      */ 
/* 1723 */         if (stmt != null) {
/*      */           try {
/* 1725 */             stmt.close();
/*      */ 
/*      */           }
/*      */           catch (SQLException sqlE) {}
/*      */         }
/*      */       }
/*      */     }
/*      */     else
/*      */     {
/* 1734 */       this.indexToCharsetMapping = CharsetMapping.INDEX_TO_CHARSET;
/*      */     }
/*      */   }
/*      */   
/*      */   private boolean canHandleAsServerPreparedStatement(String sql) throws SQLException
/*      */   {
/* 1740 */     if ((sql == null) || (sql.length() == 0)) {
/* 1741 */       return true;
/*      */     }
/*      */     
/* 1744 */     if (getCachePreparedStatements()) {
/* 1745 */       synchronized (this.serverSideStatementCheckCache) {
/* 1746 */         Boolean flag = (Boolean)this.serverSideStatementCheckCache.get(sql);
/*      */         
/* 1748 */         if (flag != null) {
/* 1749 */           return flag.booleanValue();
/*      */         }
/*      */         
/* 1752 */         boolean canHandle = canHandleAsServerPreparedStatementNoCache(sql);
/*      */         
/* 1754 */         if (sql.length() < getPreparedStatementCacheSqlLimit()) {
/* 1755 */           this.serverSideStatementCheckCache.put(sql, canHandle ? Boolean.TRUE : Boolean.FALSE);
/*      */         }
/*      */         
/*      */ 
/* 1759 */         return canHandle;
/*      */       }
/*      */     }
/*      */     
/* 1763 */     return canHandleAsServerPreparedStatementNoCache(sql);
/*      */   }
/*      */   
/*      */ 
/*      */   private boolean canHandleAsServerPreparedStatementNoCache(String sql)
/*      */     throws SQLException
/*      */   {
/* 1770 */     if (StringUtils.startsWithIgnoreCaseAndNonAlphaNumeric(sql, "CALL")) {
/* 1771 */       return false;
/*      */     }
/*      */     
/* 1774 */     boolean canHandleAsStatement = true;
/*      */     
/* 1776 */     if ((!versionMeetsMinimum(5, 0, 7)) && ((StringUtils.startsWithIgnoreCaseAndNonAlphaNumeric(sql, "SELECT")) || (StringUtils.startsWithIgnoreCaseAndNonAlphaNumeric(sql, "DELETE")) || (StringUtils.startsWithIgnoreCaseAndNonAlphaNumeric(sql, "INSERT")) || (StringUtils.startsWithIgnoreCaseAndNonAlphaNumeric(sql, "UPDATE")) || (StringUtils.startsWithIgnoreCaseAndNonAlphaNumeric(sql, "REPLACE"))))
/*      */     {
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
/* 1794 */       int currentPos = 0;
/* 1795 */       int statementLength = sql.length();
/* 1796 */       int lastPosToLook = statementLength - 7;
/* 1797 */       boolean allowBackslashEscapes = !this.noBackslashEscapes;
/* 1798 */       char quoteChar = this.useAnsiQuotes ? '"' : '\'';
/* 1799 */       boolean foundLimitWithPlaceholder = false;
/*      */       
/* 1801 */       while (currentPos < lastPosToLook) {
/* 1802 */         int limitStart = StringUtils.indexOfIgnoreCaseRespectQuotes(currentPos, sql, "LIMIT ", quoteChar, allowBackslashEscapes);
/*      */         
/*      */ 
/*      */ 
/* 1806 */         if (limitStart == -1) {
/*      */           break;
/*      */         }
/*      */         
/* 1810 */         currentPos = limitStart + 7;
/*      */         
/* 1812 */         while (currentPos < statementLength) {
/* 1813 */           char c = sql.charAt(currentPos);
/*      */           
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 1820 */           if ((!Character.isDigit(c)) && (!Character.isWhitespace(c)) && (c != ',') && (c != '?')) {
/*      */             break;
/*      */           }
/*      */           
/*      */ 
/* 1825 */           if (c == '?') {
/* 1826 */             foundLimitWithPlaceholder = true;
/* 1827 */             break;
/*      */           }
/*      */           
/* 1830 */           currentPos++;
/*      */         }
/*      */       }
/*      */       
/* 1834 */       canHandleAsStatement = !foundLimitWithPlaceholder;
/* 1835 */     } else if (StringUtils.startsWithIgnoreCaseAndWs(sql, "CREATE TABLE")) {
/* 1836 */       canHandleAsStatement = false;
/* 1837 */     } else if (StringUtils.startsWithIgnoreCaseAndWs(sql, "DO")) {
/* 1838 */       canHandleAsStatement = false;
/* 1839 */     } else if (StringUtils.startsWithIgnoreCaseAndWs(sql, "SET")) {
/* 1840 */       canHandleAsStatement = false;
/*      */     }
/*      */     
/*      */ 
/*      */ 
/* 1845 */     return canHandleAsStatement;
/*      */   }
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
/*      */   public void changeUser(String userName, String newPassword)
/*      */     throws SQLException
/*      */   {
/* 1863 */     if ((userName == null) || (userName.equals(""))) {
/* 1864 */       userName = "";
/*      */     }
/*      */     
/* 1867 */     if (newPassword == null) {
/* 1868 */       newPassword = "";
/*      */     }
/*      */     
/* 1871 */     this.io.changeUser(userName, newPassword, this.database);
/* 1872 */     this.user = userName;
/* 1873 */     this.password = newPassword;
/*      */     
/* 1875 */     if (versionMeetsMinimum(4, 1, 0)) {
/* 1876 */       configureClientCharacterSet();
/*      */     }
/*      */     
/* 1879 */     setupServerForTruncationChecks();
/*      */   }
/*      */   
/*      */   private void checkAndCreatePerformanceHistogram() {
/* 1883 */     if (this.perfMetricsHistCounts == null) {
/* 1884 */       this.perfMetricsHistCounts = new int[20];
/*      */     }
/*      */     
/* 1887 */     if (this.perfMetricsHistBreakpoints == null) {
/* 1888 */       this.perfMetricsHistBreakpoints = new long[20];
/*      */     }
/*      */   }
/*      */   
/*      */   private void checkAndCreateTablesAccessedHistogram() {
/* 1893 */     if (this.numTablesMetricsHistCounts == null) {
/* 1894 */       this.numTablesMetricsHistCounts = new int[20];
/*      */     }
/*      */     
/* 1897 */     if (this.numTablesMetricsHistBreakpoints == null) {
/* 1898 */       this.numTablesMetricsHistBreakpoints = new long[20];
/*      */     }
/*      */   }
/*      */   
/*      */   private void checkClosed() throws SQLException {
/* 1903 */     if (this.isClosed) {
/* 1904 */       StringBuffer messageBuf = new StringBuffer("No operations allowed after connection closed.");
/*      */       
/*      */ 
/* 1907 */       if ((this.forcedClosedLocation != null) || (this.forceClosedReason != null)) {
/* 1908 */         messageBuf.append("Connection was implicitly closed ");
/*      */       }
/*      */       
/*      */ 
/* 1912 */       if (this.forcedClosedLocation != null) {
/* 1913 */         messageBuf.append("\n\n");
/* 1914 */         messageBuf.append(" at (stack trace):\n");
/*      */         
/* 1916 */         messageBuf.append(Util.stackTraceToString(this.forcedClosedLocation));
/*      */       }
/*      */       
/*      */ 
/* 1920 */       if (this.forceClosedReason != null) {
/* 1921 */         if (this.forcedClosedLocation != null) {
/* 1922 */           messageBuf.append("\n\nDue ");
/*      */         } else {
/* 1924 */           messageBuf.append("due ");
/*      */         }
/*      */         
/* 1927 */         messageBuf.append("to underlying exception/error:\n");
/* 1928 */         messageBuf.append(Util.stackTraceToString(this.forceClosedReason));
/*      */       }
/*      */       
/*      */ 
/* 1932 */       throw SQLError.createSQLException(messageBuf.toString(), "08003");
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   private void checkServerEncoding()
/*      */     throws SQLException
/*      */   {
/* 1945 */     if ((getUseUnicode()) && (getEncoding() != null))
/*      */     {
/* 1947 */       return;
/*      */     }
/*      */     
/* 1950 */     String serverEncoding = (String)this.serverVariables.get("character_set");
/*      */     
/*      */ 
/* 1953 */     if (serverEncoding == null)
/*      */     {
/* 1955 */       serverEncoding = (String)this.serverVariables.get("character_set_server");
/*      */     }
/*      */     
/*      */ 
/* 1959 */     String mappedServerEncoding = null;
/*      */     
/* 1961 */     if (serverEncoding != null) {
/* 1962 */       mappedServerEncoding = CharsetMapping.getJavaEncodingForMysqlEncoding(serverEncoding.toUpperCase(Locale.ENGLISH), this);
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 1970 */     if ((!getUseUnicode()) && (mappedServerEncoding != null)) {
/* 1971 */       SingleByteCharsetConverter converter = getCharsetConverter(mappedServerEncoding);
/*      */       
/* 1973 */       if (converter != null) {
/* 1974 */         setUseUnicode(true);
/* 1975 */         setEncoding(mappedServerEncoding);
/*      */         
/* 1977 */         return;
/*      */       }
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 1985 */     if (serverEncoding != null) {
/* 1986 */       if (mappedServerEncoding == null)
/*      */       {
/*      */ 
/* 1989 */         if (Character.isLowerCase(serverEncoding.charAt(0))) {
/* 1990 */           char[] ach = serverEncoding.toCharArray();
/* 1991 */           ach[0] = Character.toUpperCase(serverEncoding.charAt(0));
/* 1992 */           setEncoding(new String(ach));
/*      */         }
/*      */       }
/*      */       
/* 1996 */       if (mappedServerEncoding == null) {
/* 1997 */         throw SQLError.createSQLException("Unknown character encoding on server '" + serverEncoding + "', use 'characterEncoding=' property " + " to provide correct mapping", "01S00");
/*      */       }
/*      */       
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       try
/*      */       {
/* 2009 */         "abc".getBytes(mappedServerEncoding);
/* 2010 */         setEncoding(mappedServerEncoding);
/* 2011 */         setUseUnicode(true);
/*      */       } catch (UnsupportedEncodingException UE) {
/* 2013 */         throw SQLError.createSQLException("The driver can not map the character encoding '" + getEncoding() + "' that your server is using " + "to a character encoding your JVM understands. You " + "can specify this mapping manually by adding \"useUnicode=true\" " + "as well as \"characterEncoding=[an_encoding_your_jvm_understands]\" " + "to your JDBC URL.", "0S100");
/*      */       }
/*      */     }
/*      */   }
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
/*      */   private void checkTransactionIsolationLevel()
/*      */     throws SQLException
/*      */   {
/* 2033 */     String txIsolationName = null;
/*      */     
/* 2035 */     if (versionMeetsMinimum(4, 0, 3)) {
/* 2036 */       txIsolationName = "tx_isolation";
/*      */     } else {
/* 2038 */       txIsolationName = "transaction_isolation";
/*      */     }
/*      */     
/* 2041 */     String s = (String)this.serverVariables.get(txIsolationName);
/*      */     
/* 2043 */     if (s != null) {
/* 2044 */       Integer intTI = (Integer)mapTransIsolationNameToValue.get(s);
/*      */       
/* 2046 */       if (intTI != null) {
/* 2047 */         this.isolationLevel = intTI.intValue();
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   private void cleanup(Throwable whyCleanedUp)
/*      */   {
/*      */     try
/*      */     {
/* 2062 */       if ((this.io != null) && (!isClosed())) {
/* 2063 */         realClose(false, false, false, whyCleanedUp);
/* 2064 */       } else if (this.io != null) {
/* 2065 */         this.io.forceClose();
/*      */       }
/*      */     }
/*      */     catch (SQLException sqlEx) {}
/*      */     
/*      */ 
/*      */ 
/* 2072 */     this.isClosed = true;
/*      */   }
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
/*      */   public PreparedStatement clientPrepareStatement(String sql)
/*      */     throws SQLException
/*      */   {
/* 2097 */     return clientPrepareStatement(sql, 1005, 1007);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public java.sql.PreparedStatement clientPrepareStatement(String sql, int autoGenKeyIndex)
/*      */     throws SQLException
/*      */   {
/* 2107 */     java.sql.PreparedStatement pStmt = clientPrepareStatement(sql);
/*      */     
/* 2109 */     ((PreparedStatement)pStmt).setRetrieveGeneratedKeys(autoGenKeyIndex == 1);
/*      */     
/*      */ 
/* 2112 */     return pStmt;
/*      */   }
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
/*      */   public PreparedStatement clientPrepareStatement(String sql, int resultSetType, int resultSetConcurrency)
/*      */     throws SQLException
/*      */   {
/* 2130 */     return clientPrepareStatement(sql, resultSetType, resultSetConcurrency, true);
/*      */   }
/*      */   
/*      */   protected PreparedStatement clientPrepareStatement(String sql, int resultSetType, int resultSetConcurrency, boolean processEscapeCodesIfNeeded)
/*      */     throws SQLException
/*      */   {
/* 2136 */     checkClosed();
/*      */     
/* 2138 */     String nativeSql = (processEscapeCodesIfNeeded) && (getProcessEscapeCodesForPrepStmts()) ? nativeSQL(sql) : sql;
/*      */     
/* 2140 */     PreparedStatement pStmt = null;
/*      */     
/* 2142 */     if (getCachePreparedStatements()) {
/* 2143 */       synchronized (this.cachedPreparedStatementParams) {
/* 2144 */         PreparedStatement.ParseInfo pStmtInfo = (PreparedStatement.ParseInfo)this.cachedPreparedStatementParams.get(nativeSql);
/*      */         
/*      */ 
/* 2147 */         if (pStmtInfo == null) {
/* 2148 */           pStmt = new PreparedStatement(this, nativeSql, this.database);
/*      */           
/*      */ 
/* 2151 */           PreparedStatement.ParseInfo parseInfo = pStmt.getParseInfo();
/*      */           
/* 2153 */           if (parseInfo.statementLength < getPreparedStatementCacheSqlLimit()) {
/* 2154 */             if (this.cachedPreparedStatementParams.size() >= getPreparedStatementCacheSize()) {
/* 2155 */               Iterator oldestIter = this.cachedPreparedStatementParams.keySet().iterator();
/*      */               
/* 2157 */               long lruTime = Long.MAX_VALUE;
/* 2158 */               String oldestSql = null;
/*      */               
/* 2160 */               while (oldestIter.hasNext()) {
/* 2161 */                 String sqlKey = (String)oldestIter.next();
/* 2162 */                 PreparedStatement.ParseInfo lruInfo = (PreparedStatement.ParseInfo)this.cachedPreparedStatementParams.get(sqlKey);
/*      */                 
/*      */ 
/* 2165 */                 if (lruInfo.lastUsed < lruTime) {
/* 2166 */                   lruTime = lruInfo.lastUsed;
/* 2167 */                   oldestSql = sqlKey;
/*      */                 }
/*      */               }
/*      */               
/* 2171 */               if (oldestSql != null) {
/* 2172 */                 this.cachedPreparedStatementParams.remove(oldestSql);
/*      */               }
/*      */             }
/*      */             
/*      */ 
/* 2177 */             this.cachedPreparedStatementParams.put(nativeSql, pStmt.getParseInfo());
/*      */           }
/*      */         }
/*      */         else {
/* 2181 */           pStmtInfo.lastUsed = System.currentTimeMillis();
/* 2182 */           pStmt = new PreparedStatement(this, nativeSql, this.database, pStmtInfo);
/*      */         }
/*      */       }
/*      */     }
/*      */     
/* 2187 */     pStmt = new PreparedStatement(this, nativeSql, this.database);
/*      */     
/*      */ 
/*      */ 
/* 2191 */     pStmt.setResultSetType(resultSetType);
/* 2192 */     pStmt.setResultSetConcurrency(resultSetConcurrency);
/*      */     
/* 2194 */     return pStmt;
/*      */   }
/*      */   
/*      */   public void close() throws SQLException
/*      */   {
/* 2199 */     realClose(true, true, false, null);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   private void closeAllOpenStatements()
/*      */     throws SQLException
/*      */   {
/* 2209 */     SQLException postponedException = null;
/*      */     
/* 2211 */     if (this.openStatements != null) {
/* 2212 */       List currentlyOpenStatements = new ArrayList();
/*      */       
/*      */ 
/*      */ 
/* 2216 */       Iterator iter = this.openStatements.keySet().iterator();
/* 2217 */       while (iter.hasNext()) {
/* 2218 */         currentlyOpenStatements.add(iter.next());
/*      */       }
/*      */       
/* 2221 */       int numStmts = currentlyOpenStatements.size();
/*      */       
/* 2223 */       for (int i = 0; i < numStmts; i++) {
/* 2224 */         Statement stmt = (Statement)currentlyOpenStatements.get(i);
/*      */         try
/*      */         {
/* 2227 */           stmt.realClose(false, true);
/*      */         } catch (SQLException sqlEx) {
/* 2229 */           postponedException = sqlEx;
/*      */         }
/*      */       }
/*      */       
/*      */ 
/* 2234 */       if (postponedException != null) {
/* 2235 */         throw postponedException;
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */   private void closeStatement(java.sql.Statement stmt) {
/* 2241 */     if (stmt != null) {
/*      */       try {
/* 2243 */         stmt.close();
/*      */       }
/*      */       catch (SQLException sqlEx) {}
/*      */       
/*      */ 
/* 2248 */       stmt = null;
/*      */     }
/*      */   }
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
/*      */   public void commit()
/*      */     throws SQLException
/*      */   {
/* 2269 */     synchronized (getMutex()) {
/* 2270 */       checkClosed();
/*      */       
/*      */       try
/*      */       {
/* 2274 */         if ((this.autoCommit) && (!getRelaxAutoCommit()))
/* 2275 */           throw SQLError.createSQLException("Can't call commit when autocommit=true");
/* 2276 */         if (this.transactionsSupported) {
/* 2277 */           if ((getUseLocalSessionState()) && (versionMeetsMinimum(5, 0, 0)) && 
/* 2278 */             (!this.io.inTransactionOnServer()))
/*      */           {
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
/* 2299 */             this.needsPing = getReconnectAtTxEnd(); return;
/*      */           }
/* 2283 */           execSQL(null, "commit", -1, null, 1003, 1007, false, this.database, true, false);
/*      */         }
/*      */         
/*      */ 
/*      */       }
/*      */       catch (SQLException sqlException)
/*      */       {
/* 2290 */         if ("08S01".equals(sqlException.getSQLState()))
/*      */         {
/* 2292 */           throw SQLError.createSQLException("Communications link failure during commit(). Transaction resolution unknown.", "08007");
/*      */         }
/*      */         
/*      */ 
/*      */ 
/* 2297 */         throw sqlException;
/*      */       } finally {
/* 2299 */         this.needsPing = getReconnectAtTxEnd();
/*      */       }
/*      */       
/* 2302 */       return;
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   private void configureCharsetProperties()
/*      */     throws SQLException
/*      */   {
/* 2313 */     if (getEncoding() != null)
/*      */     {
/*      */       try
/*      */       {
/* 2317 */         String testString = "abc";
/* 2318 */         testString.getBytes(getEncoding());
/*      */       }
/*      */       catch (UnsupportedEncodingException UE) {
/* 2321 */         String oldEncoding = getEncoding();
/*      */         
/* 2323 */         setEncoding(CharsetMapping.getJavaEncodingForMysqlEncoding(oldEncoding, this));
/*      */         
/*      */ 
/* 2326 */         if (getEncoding() == null) {
/* 2327 */           throw SQLError.createSQLException("Java does not support the MySQL character encoding  encoding '" + oldEncoding + "'.", "01S00");
/*      */         }
/*      */         
/*      */ 
/*      */ 
/*      */         try
/*      */         {
/* 2334 */           String testString = "abc";
/* 2335 */           testString.getBytes(getEncoding());
/*      */         } catch (UnsupportedEncodingException encodingEx) {
/* 2337 */           throw SQLError.createSQLException("Unsupported character encoding '" + getEncoding() + "'.", "01S00");
/*      */         }
/*      */       }
/*      */     }
/*      */   }
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
/*      */   private boolean configureClientCharacterSet()
/*      */     throws SQLException
/*      */   {
/* 2359 */     String realJavaEncoding = getEncoding();
/* 2360 */     boolean characterSetAlreadyConfigured = false;
/*      */     try
/*      */     {
/* 2363 */       if (versionMeetsMinimum(4, 1, 0)) {
/* 2364 */         characterSetAlreadyConfigured = true;
/*      */         
/* 2366 */         setUseUnicode(true);
/*      */         
/* 2368 */         configureCharsetProperties();
/* 2369 */         realJavaEncoding = getEncoding();
/*      */         
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */         try
/*      */         {
/* 2377 */           if ((this.props != null) && (this.props.getProperty("com.mysql.jdbc.faultInjection.serverCharsetIndex") != null)) {
/* 2378 */             this.io.serverCharsetIndex = Integer.parseInt(this.props.getProperty("com.mysql.jdbc.faultInjection.serverCharsetIndex"));
/*      */           }
/*      */           
/*      */ 
/*      */ 
/* 2383 */           serverEncodingToSet = CharsetMapping.INDEX_TO_CHARSET[this.io.serverCharsetIndex];
/*      */           
/*      */ 
/* 2386 */           if ((serverEncodingToSet == null) || (serverEncodingToSet.length() == 0)) {
/* 2387 */             if (realJavaEncoding != null)
/*      */             {
/* 2389 */               setEncoding(realJavaEncoding);
/*      */             } else {
/* 2391 */               throw SQLError.createSQLException("Unknown initial character set index '" + this.io.serverCharsetIndex + "' received from server. Initial client character set can be forced via the 'characterEncoding' property.", "S1000");
/*      */             }
/*      */           }
/*      */           
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 2400 */           if ((versionMeetsMinimum(4, 1, 0)) && ("ISO8859_1".equalsIgnoreCase(serverEncodingToSet)))
/*      */           {
/* 2402 */             serverEncodingToSet = "Cp1252";
/*      */           }
/*      */         }
/*      */         catch (ArrayIndexOutOfBoundsException outOfBoundsEx)
/*      */         {
/*      */           String serverEncodingToSet;
/* 2408 */           if (realJavaEncoding != null)
/*      */           {
/* 2410 */             setEncoding(realJavaEncoding);
/*      */           } else {
/* 2412 */             throw SQLError.createSQLException("Unknown initial character set index '" + this.io.serverCharsetIndex + "' received from server. Initial client character set can be forced via the 'characterEncoding' property.", "S1000");
/*      */           }
/*      */         }
/*      */         
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 2420 */         if (getEncoding() == null)
/*      */         {
/* 2422 */           setEncoding("ISO8859_1");
/*      */         }
/*      */         
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 2429 */         if (getUseUnicode()) {
/* 2430 */           if (realJavaEncoding != null)
/*      */           {
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 2436 */             if ((realJavaEncoding.equalsIgnoreCase("UTF-8")) || (realJavaEncoding.equalsIgnoreCase("UTF8")))
/*      */             {
/*      */ 
/*      */ 
/* 2440 */               if ((!getUseOldUTF8Behavior()) && 
/* 2441 */                 (!characterSetNamesMatches("utf8"))) {
/* 2442 */                 execSQL(null, "SET NAMES utf8", -1, null, 1003, 1007, false, this.database, true, false);
/*      */               }
/*      */               
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 2449 */               setEncoding(realJavaEncoding);
/*      */             } else {
/* 2451 */               String mysqlEncodingName = CharsetMapping.getMysqlEncodingForJavaEncoding(realJavaEncoding.toUpperCase(Locale.ENGLISH), this);
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
/* 2466 */               if (mysqlEncodingName != null)
/*      */               {
/* 2468 */                 if (!characterSetNamesMatches(mysqlEncodingName)) {
/* 2469 */                   execSQL(null, "SET NAMES " + mysqlEncodingName, -1, null, 1003, 1007, false, this.database, true, false);
/*      */                 }
/*      */               }
/*      */               
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 2480 */               setEncoding(realJavaEncoding);
/*      */             }
/* 2482 */           } else if (getEncoding() != null)
/*      */           {
/*      */ 
/*      */ 
/* 2486 */             String mysqlEncodingName = CharsetMapping.getMysqlEncodingForJavaEncoding(getEncoding().toUpperCase(Locale.ENGLISH), this);
/*      */             
/*      */ 
/*      */ 
/* 2490 */             if (!characterSetNamesMatches(mysqlEncodingName)) {
/* 2491 */               execSQL(null, "SET NAMES " + mysqlEncodingName, -1, null, 1003, 1007, false, this.database, true, false);
/*      */             }
/*      */             
/*      */ 
/*      */ 
/*      */ 
/* 2497 */             realJavaEncoding = getEncoding();
/*      */           }
/*      */         }
/*      */         
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 2508 */         String onServer = null;
/* 2509 */         boolean isNullOnServer = false;
/*      */         
/* 2511 */         if (this.serverVariables != null) {
/* 2512 */           onServer = (String)this.serverVariables.get("character_set_results");
/*      */           
/* 2514 */           isNullOnServer = (onServer == null) || ("NULL".equalsIgnoreCase(onServer)) || (onServer.length() == 0);
/*      */         }
/*      */         
/* 2517 */         if (getCharacterSetResults() == null)
/*      */         {
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 2524 */           if (!isNullOnServer) {
/* 2525 */             execSQL(null, "SET character_set_results = NULL", -1, null, 1003, 1007, false, this.database, true, false);
/*      */             
/*      */ 
/*      */ 
/*      */ 
/* 2530 */             if (!this.usingCachedConfig) {
/* 2531 */               this.serverVariables.put("jdbc.local.character_set_results", null);
/*      */             }
/*      */           }
/* 2534 */           else if (!this.usingCachedConfig) {
/* 2535 */             this.serverVariables.put("jdbc.local.character_set_results", onServer);
/*      */           }
/*      */         }
/*      */         else {
/* 2539 */           String charsetResults = getCharacterSetResults();
/* 2540 */           String mysqlEncodingName = null;
/*      */           
/* 2542 */           if (("UTF-8".equalsIgnoreCase(charsetResults)) || ("UTF8".equalsIgnoreCase(charsetResults)))
/*      */           {
/* 2544 */             mysqlEncodingName = "utf8";
/*      */           } else {
/* 2546 */             mysqlEncodingName = CharsetMapping.getMysqlEncodingForJavaEncoding(charsetResults.toUpperCase(Locale.ENGLISH), this);
/*      */           }
/*      */           
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 2555 */           if (!mysqlEncodingName.equalsIgnoreCase((String)this.serverVariables.get("character_set_results")))
/*      */           {
/* 2557 */             StringBuffer setBuf = new StringBuffer("SET character_set_results = ".length() + mysqlEncodingName.length());
/*      */             
/*      */ 
/* 2560 */             setBuf.append("SET character_set_results = ").append(mysqlEncodingName);
/*      */             
/*      */ 
/* 2563 */             execSQL(null, setBuf.toString(), -1, null, 1003, 1007, false, this.database, true, false);
/*      */             
/*      */ 
/*      */ 
/*      */ 
/* 2568 */             if (!this.usingCachedConfig) {
/* 2569 */               this.serverVariables.put("jdbc.local.character_set_results", mysqlEncodingName);
/*      */             }
/*      */             
/*      */           }
/* 2573 */           else if (!this.usingCachedConfig) {
/* 2574 */             this.serverVariables.put("jdbc.local.character_set_results", onServer);
/*      */           }
/*      */         }
/*      */         
/*      */ 
/* 2579 */         if (getConnectionCollation() != null) {
/* 2580 */           StringBuffer setBuf = new StringBuffer("SET collation_connection = ".length() + getConnectionCollation().length());
/*      */           
/*      */ 
/* 2583 */           setBuf.append("SET collation_connection = ").append(getConnectionCollation());
/*      */           
/*      */ 
/* 2586 */           execSQL(null, setBuf.toString(), -1, null, 1003, 1007, false, this.database, true, false);
/*      */         }
/*      */         
/*      */ 
/*      */       }
/*      */       else
/*      */       {
/* 2593 */         realJavaEncoding = getEncoding();
/*      */       }
/*      */       
/*      */ 
/*      */     }
/*      */     finally
/*      */     {
/*      */ 
/* 2601 */       setEncoding(realJavaEncoding);
/*      */     }
/*      */     
/* 2604 */     return characterSetAlreadyConfigured;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   private boolean characterSetNamesMatches(String mysqlEncodingName)
/*      */   {
/* 2611 */     return (mysqlEncodingName != null) && (mysqlEncodingName.equalsIgnoreCase((String)this.serverVariables.get("character_set_client"))) && (mysqlEncodingName.equalsIgnoreCase((String)this.serverVariables.get("character_set_connection")));
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   private void configureTimezone()
/*      */     throws SQLException
/*      */   {
/* 2624 */     String configuredTimeZoneOnServer = (String)this.serverVariables.get("timezone");
/*      */     
/*      */ 
/* 2627 */     if (configuredTimeZoneOnServer == null) {
/* 2628 */       configuredTimeZoneOnServer = (String)this.serverVariables.get("time_zone");
/*      */       
/*      */ 
/* 2631 */       if ("SYSTEM".equalsIgnoreCase(configuredTimeZoneOnServer)) {
/* 2632 */         configuredTimeZoneOnServer = (String)this.serverVariables.get("system_time_zone");
/*      */       }
/*      */     }
/*      */     
/*      */ 
/* 2637 */     if ((getUseTimezone()) && (configuredTimeZoneOnServer != null))
/*      */     {
/* 2639 */       String canoncicalTimezone = getServerTimezone();
/*      */       
/* 2641 */       if ((canoncicalTimezone == null) || (canoncicalTimezone.length() == 0))
/*      */       {
/* 2643 */         String serverTimezoneStr = configuredTimeZoneOnServer;
/*      */         try
/*      */         {
/* 2646 */           canoncicalTimezone = TimeUtil.getCanoncialTimezone(serverTimezoneStr);
/*      */           
/*      */ 
/* 2649 */           if (canoncicalTimezone == null) {
/* 2650 */             throw SQLError.createSQLException("Can't map timezone '" + serverTimezoneStr + "' to " + " canonical timezone.", "S1009");
/*      */           }
/*      */           
/*      */         }
/*      */         catch (IllegalArgumentException iae)
/*      */         {
/* 2656 */           throw SQLError.createSQLException(iae.getMessage(), "S1000");
/*      */         }
/*      */       }
/*      */       
/*      */ 
/* 2661 */       this.serverTimezoneTZ = TimeZone.getTimeZone(canoncicalTimezone);
/*      */       
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 2668 */       if ((!canoncicalTimezone.equalsIgnoreCase("GMT")) && (this.serverTimezoneTZ.getID().equals("GMT")))
/*      */       {
/* 2670 */         throw SQLError.createSQLException("No timezone mapping entry for '" + canoncicalTimezone + "'", "S1009");
/*      */       }
/*      */       
/*      */ 
/*      */ 
/* 2675 */       if ("GMT".equalsIgnoreCase(this.serverTimezoneTZ.getID())) {
/* 2676 */         this.isServerTzUTC = true;
/*      */       } else {
/* 2678 */         this.isServerTzUTC = false;
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */   private void createInitialHistogram(long[] breakpoints, long lowerBound, long upperBound)
/*      */   {
/* 2686 */     double bucketSize = (upperBound - lowerBound) / 20.0D * 1.25D;
/*      */     
/* 2688 */     if (bucketSize < 1.0D) {
/* 2689 */       bucketSize = 1.0D;
/*      */     }
/*      */     
/* 2692 */     for (int i = 0; i < 20; i++) {
/* 2693 */       breakpoints[i] = lowerBound;
/* 2694 */       lowerBound = (lowerBound + bucketSize);
/*      */     }
/*      */   }
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
/*      */   protected MysqlIO createNewIO(boolean isForReconnect)
/*      */     throws SQLException
/*      */   {
/* 2711 */     MysqlIO newIo = null;
/*      */     
/* 2713 */     Properties mergedProps = new Properties();
/*      */     
/* 2715 */     mergedProps = exposeAsProperties(this.props);
/*      */     
/* 2717 */     long queriesIssuedFailedOverCopy = this.queriesIssuedFailedOver;
/* 2718 */     this.queriesIssuedFailedOver = 0L;
/*      */     try
/*      */     {
/* 2721 */       if ((!getHighAvailability()) && (!this.failedOver)) {
/* 2722 */         boolean connectionGood = false;
/* 2723 */         Exception connectionNotEstablishedBecause = null;
/*      */         
/* 2725 */         int hostIndex = 0;
/*      */         
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 2733 */         if (getRoundRobinLoadBalance()) {}
/* 2734 */         for (hostIndex = getNextRoundRobinHostIndex(getURL(), this.hostList); 
/*      */             
/*      */ 
/*      */ 
/* 2738 */             hostIndex < this.hostListSize; hostIndex++)
/*      */         {
/* 2740 */           if (hostIndex == 0) {
/* 2741 */             this.hasTriedMasterFlag = true;
/*      */           }
/*      */           try
/*      */           {
/* 2745 */             String newHostPortPair = (String)this.hostList.get(hostIndex);
/*      */             
/*      */ 
/* 2748 */             int newPort = 3306;
/*      */             
/* 2750 */             String[] hostPortPair = NonRegisteringDriver.parseHostPortPair(newHostPortPair);
/*      */             
/* 2752 */             String newHost = hostPortPair[0];
/*      */             
/* 2754 */             if ((newHost == null) || (newHost.trim().length() == 0)) {
/* 2755 */               newHost = "localhost";
/*      */             }
/*      */             
/* 2758 */             if (hostPortPair[1] != null) {
/*      */               try {
/* 2760 */                 newPort = Integer.parseInt(hostPortPair[1]);
/*      */               }
/*      */               catch (NumberFormatException nfe) {
/* 2763 */                 throw SQLError.createSQLException("Illegal connection port value '" + hostPortPair[1] + "'", "01S00");
/*      */               }
/*      */             }
/*      */             
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 2771 */             this.io = new MysqlIO(newHost, newPort, mergedProps, getSocketFactoryClassName(), this, getSocketTimeout());
/*      */             
/*      */ 
/*      */ 
/* 2775 */             this.io.doHandshake(this.user, this.password, this.database);
/*      */             
/* 2777 */             this.connectionId = this.io.getThreadId();
/* 2778 */             this.isClosed = false;
/*      */             
/*      */ 
/* 2781 */             boolean oldAutoCommit = getAutoCommit();
/* 2782 */             int oldIsolationLevel = this.isolationLevel;
/* 2783 */             boolean oldReadOnly = isReadOnly();
/* 2784 */             String oldCatalog = getCatalog();
/*      */             
/*      */ 
/*      */ 
/*      */ 
/* 2789 */             initializePropsFromServer();
/*      */             
/* 2791 */             if (isForReconnect)
/*      */             {
/* 2793 */               setAutoCommit(oldAutoCommit);
/*      */               
/* 2795 */               if (this.hasIsolationLevels) {
/* 2796 */                 setTransactionIsolation(oldIsolationLevel);
/*      */               }
/*      */               
/* 2799 */               setCatalog(oldCatalog);
/*      */             }
/*      */             
/* 2802 */             if (hostIndex != 0) {
/* 2803 */               setFailedOverState();
/* 2804 */               queriesIssuedFailedOverCopy = 0L;
/*      */             } else {
/* 2806 */               this.failedOver = false;
/* 2807 */               queriesIssuedFailedOverCopy = 0L;
/*      */               
/* 2809 */               if (this.hostListSize > 1) {
/* 2810 */                 setReadOnlyInternal(false);
/*      */               } else {
/* 2812 */                 setReadOnlyInternal(oldReadOnly);
/*      */               }
/*      */             }
/*      */             
/* 2816 */             connectionGood = true;
/*      */           }
/*      */           catch (Exception EEE)
/*      */           {
/* 2820 */             if (this.io != null) {
/* 2821 */               this.io.forceClose();
/*      */             }
/*      */             
/* 2824 */             connectionNotEstablishedBecause = EEE;
/*      */             
/* 2826 */             connectionGood = false;
/*      */             
/* 2828 */             if ((EEE instanceof SQLException)) {
/* 2829 */               SQLException sqlEx = (SQLException)EEE;
/*      */               
/* 2831 */               String sqlState = sqlEx.getSQLState();
/*      */               
/*      */ 
/*      */ 
/* 2835 */               if ((sqlState == null) || (!sqlState.equals("08S01")))
/*      */               {
/*      */ 
/* 2838 */                 throw sqlEx;
/*      */               }
/*      */             }
/*      */             
/*      */ 
/* 2843 */             if (getRoundRobinLoadBalance()) {
/* 2844 */               hostIndex = getNextRoundRobinHostIndex(getURL(), this.hostList) - 1;
/*      */             }
/* 2846 */             else if (this.hostListSize - 1 == hostIndex) {
/* 2847 */               throw new CommunicationsException(this, this.io != null ? this.io.getLastPacketSentTimeMs() : 0L, EEE);
/*      */             }
/*      */           }
/*      */         }
/*      */         
/*      */ 
/*      */ 
/*      */ 
/* 2855 */         if (!connectionGood)
/*      */         {
/* 2857 */           throw SQLError.createSQLException("Could not create connection to database server due to underlying exception: '" + connectionNotEstablishedBecause + "'." + (getParanoid() ? "" : Util.stackTraceToString(connectionNotEstablishedBecause)), "08001");
/*      */ 
/*      */         }
/*      */         
/*      */ 
/*      */ 
/*      */       }
/*      */       else
/*      */       {
/*      */ 
/* 2867 */         double timeout = getInitialTimeout();
/* 2868 */         boolean connectionGood = false;
/*      */         
/* 2870 */         Exception connectionException = null;
/*      */         
/* 2872 */         int hostIndex = 0;
/*      */         
/* 2874 */         if (getRoundRobinLoadBalance()) {}
/* 2875 */         for (hostIndex = getNextRoundRobinHostIndex(getURL(), this.hostList); 
/*      */             
/*      */ 
/*      */ 
/* 2879 */             (hostIndex < this.hostListSize) && (!connectionGood); hostIndex++) {
/* 2880 */           if (hostIndex == 0) {
/* 2881 */             this.hasTriedMasterFlag = true;
/*      */           }
/*      */           
/* 2884 */           if ((this.preferSlaveDuringFailover) && (hostIndex == 0)) {
/* 2885 */             hostIndex++;
/*      */           }
/*      */           
/* 2888 */           for (int attemptCount = 0; 
/* 2889 */               (attemptCount < getMaxReconnects()) && (!connectionGood); attemptCount++) {
/*      */             try {
/* 2891 */               if (this.io != null) {
/* 2892 */                 this.io.forceClose();
/*      */               }
/*      */               
/* 2895 */               String newHostPortPair = (String)this.hostList.get(hostIndex);
/*      */               
/*      */ 
/* 2898 */               int newPort = 3306;
/*      */               
/* 2900 */               String[] hostPortPair = NonRegisteringDriver.parseHostPortPair(newHostPortPair);
/*      */               
/* 2902 */               String newHost = hostPortPair[0];
/*      */               
/* 2904 */               if ((newHost == null) || (newHost.trim().length() == 0)) {
/* 2905 */                 newHost = "localhost";
/*      */               }
/*      */               
/* 2908 */               if (hostPortPair[1] != null) {
/*      */                 try {
/* 2910 */                   newPort = Integer.parseInt(hostPortPair[1]);
/*      */                 }
/*      */                 catch (NumberFormatException nfe) {
/* 2913 */                   throw SQLError.createSQLException("Illegal connection port value '" + hostPortPair[1] + "'", "01S00");
/*      */                 }
/*      */               }
/*      */               
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 2921 */               this.io = new MysqlIO(newHost, newPort, mergedProps, getSocketFactoryClassName(), this, getSocketTimeout());
/*      */               
/*      */ 
/* 2924 */               this.io.doHandshake(this.user, this.password, this.database);
/*      */               
/*      */ 
/* 2927 */               pingInternal(false);
/* 2928 */               this.connectionId = this.io.getThreadId();
/* 2929 */               this.isClosed = false;
/*      */               
/*      */ 
/* 2932 */               boolean oldAutoCommit = getAutoCommit();
/* 2933 */               int oldIsolationLevel = this.isolationLevel;
/* 2934 */               boolean oldReadOnly = isReadOnly();
/* 2935 */               String oldCatalog = getCatalog();
/*      */               
/*      */ 
/*      */ 
/*      */ 
/* 2940 */               initializePropsFromServer();
/*      */               
/* 2942 */               if (isForReconnect)
/*      */               {
/* 2944 */                 setAutoCommit(oldAutoCommit);
/*      */                 
/* 2946 */                 if (this.hasIsolationLevels) {
/* 2947 */                   setTransactionIsolation(oldIsolationLevel);
/*      */                 }
/*      */                 
/* 2950 */                 setCatalog(oldCatalog);
/*      */               }
/*      */               
/* 2953 */               connectionGood = true;
/*      */               
/* 2955 */               if (hostIndex != 0) {
/* 2956 */                 setFailedOverState();
/* 2957 */                 queriesIssuedFailedOverCopy = 0L;
/*      */               } else {
/* 2959 */                 this.failedOver = false;
/* 2960 */                 queriesIssuedFailedOverCopy = 0L;
/*      */                 
/* 2962 */                 if (this.hostListSize > 1) {
/* 2963 */                   setReadOnlyInternal(false);
/*      */                 } else {
/* 2965 */                   setReadOnlyInternal(oldReadOnly);
/*      */                 }
/*      */               }
/*      */             }
/*      */             catch (Exception EEE)
/*      */             {
/* 2971 */               connectionException = EEE;
/* 2972 */               connectionGood = false;
/*      */               
/*      */ 
/* 2975 */               if (getRoundRobinLoadBalance()) {
/* 2976 */                 hostIndex = getNextRoundRobinHostIndex(getURL(), this.hostList) - 1;
/*      */               }
/*      */               
/*      */ 
/*      */ 
/* 2981 */               if (!connectionGood) break label996; }
/* 2982 */             break;
/*      */             
/*      */             label996:
/* 2985 */             if (attemptCount > 0) {
/*      */               try {
/* 2987 */                 Thread.sleep(timeout * 1000L);
/*      */               }
/*      */               catch (InterruptedException IE) {}
/*      */             }
/*      */           }
/*      */         }
/*      */         
/*      */ 
/* 2995 */         if (!connectionGood)
/*      */         {
/* 2997 */           throw SQLError.createSQLException("Server connection failure during transaction. Due to underlying exception: '" + connectionException + "'." + (getParanoid() ? "" : Util.stackTraceToString(connectionException)) + "\nAttempted reconnect " + getMaxReconnects() + " times. Giving up.", "08001");
/*      */         }
/*      */       }
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
/* 3010 */       if ((getParanoid()) && (!getHighAvailability()) && (this.hostListSize <= 1))
/*      */       {
/* 3012 */         this.password = null;
/* 3013 */         this.user = null;
/*      */       }
/*      */       Iterator statementIter;
/* 3016 */       if (isForReconnect)
/*      */       {
/*      */ 
/*      */ 
/* 3020 */         statementIter = this.openStatements.values().iterator();
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
/* 3032 */         Stack serverPreparedStatements = null;
/*      */         
/* 3034 */         while (statementIter.hasNext()) {
/* 3035 */           Object statementObj = statementIter.next();
/*      */           
/* 3037 */           if ((statementObj instanceof ServerPreparedStatement)) {
/* 3038 */             if (serverPreparedStatements == null) {
/* 3039 */               serverPreparedStatements = new Stack();
/*      */             }
/*      */             
/* 3042 */             serverPreparedStatements.add(statementObj);
/*      */           }
/*      */         }
/*      */         
/* 3046 */         if (serverPreparedStatements != null) {
/* 3047 */           while (!serverPreparedStatements.isEmpty()) {
/* 3048 */             ((ServerPreparedStatement)serverPreparedStatements.pop()).rePrepare();
/*      */           }
/*      */         }
/*      */       }
/*      */       
/*      */ 
/* 3054 */       return newIo;
/*      */     } finally {
/* 3056 */       this.queriesIssuedFailedOver = queriesIssuedFailedOverCopy;
/*      */     }
/*      */   }
/*      */   
/*      */   private void createPreparedStatementCaches() {
/* 3061 */     int cacheSize = getPreparedStatementCacheSize();
/*      */     
/* 3063 */     this.cachedPreparedStatementParams = new HashMap(cacheSize);
/*      */     
/* 3065 */     this.serverSideStatementCheckCache = new LRUCache(cacheSize);
/*      */     
/* 3067 */     this.serverSideStatementCache = new LRUCache(cacheSize) {
/*      */       protected boolean removeEldestEntry(Map.Entry eldest) {
/* 3069 */         if (this.maxElements <= 1) {
/* 3070 */           return false;
/*      */         }
/*      */         
/* 3073 */         boolean removeIt = super.removeEldestEntry(eldest);
/*      */         
/* 3075 */         if (removeIt) {
/* 3076 */           ServerPreparedStatement ps = (ServerPreparedStatement)eldest.getValue();
/*      */           
/* 3078 */           ps.isCached = false;
/* 3079 */           ps.setClosed(false);
/*      */           try
/*      */           {
/* 3082 */             ps.close();
/*      */           }
/*      */           catch (SQLException sqlEx) {}
/*      */         }
/*      */         
/*      */ 
/* 3088 */         return removeIt;
/*      */       }
/*      */     };
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public java.sql.Statement createStatement()
/*      */     throws SQLException
/*      */   {
/* 3103 */     return createStatement(1003, 1007);
/*      */   }
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
/*      */   public java.sql.Statement createStatement(int resultSetType, int resultSetConcurrency)
/*      */     throws SQLException
/*      */   {
/* 3121 */     checkClosed();
/*      */     
/* 3123 */     Statement stmt = new Statement(this, this.database);
/* 3124 */     stmt.setResultSetType(resultSetType);
/* 3125 */     stmt.setResultSetConcurrency(resultSetConcurrency);
/*      */     
/* 3127 */     return stmt;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public java.sql.Statement createStatement(int resultSetType, int resultSetConcurrency, int resultSetHoldability)
/*      */     throws SQLException
/*      */   {
/* 3136 */     if ((getPedantic()) && 
/* 3137 */       (resultSetHoldability != 1)) {
/* 3138 */       throw SQLError.createSQLException("HOLD_CUSRORS_OVER_COMMIT is only supported holdability level", "S1009");
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/* 3144 */     return createStatement(resultSetType, resultSetConcurrency);
/*      */   }
/*      */   
/*      */   protected void dumpTestcaseQuery(String query) {
/* 3148 */     System.err.println(query);
/*      */   }
/*      */   
/*      */   protected Connection duplicate() throws SQLException {
/* 3152 */     return new Connection(this.origHostToConnectTo, this.origPortToConnectTo, this.props, this.origDatabaseToConnectTo, this.myURL);
/*      */   }
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
/*      */   ResultSet execSQL(Statement callingStatement, String sql, int maxRows, Buffer packet, int resultSetType, int resultSetConcurrency, boolean streamResults, String catalog, boolean unpackFields)
/*      */     throws SQLException
/*      */   {
/* 3206 */     return execSQL(callingStatement, sql, maxRows, packet, resultSetType, resultSetConcurrency, streamResults, catalog, unpackFields, false);
/*      */   }
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
/*      */   ResultSet execSQL(Statement callingStatement, String sql, int maxRows, Buffer packet, int resultSetType, int resultSetConcurrency, boolean streamResults, String catalog, boolean unpackFields, boolean isBatch)
/*      */     throws SQLException
/*      */   {
/* 3221 */     synchronized (this.mutex) {
/* 3222 */       long queryStartTime = 0L;
/*      */       
/* 3224 */       int endOfQueryPacketPosition = 0;
/*      */       
/* 3226 */       if (packet != null) {
/* 3227 */         endOfQueryPacketPosition = packet.getPosition();
/*      */       }
/*      */       
/* 3230 */       if (getGatherPerformanceMetrics()) {
/* 3231 */         queryStartTime = System.currentTimeMillis();
/*      */       }
/*      */       
/* 3234 */       this.lastQueryFinishedTime = 0L;
/*      */       
/* 3236 */       if ((this.failedOver) && (this.autoCommit) && (!isBatch) && 
/* 3237 */         (shouldFallBack()) && (!this.executingFailoverReconnect)) {
/*      */         try {
/* 3239 */           this.executingFailoverReconnect = true;
/*      */           
/* 3241 */           createNewIO(true);
/*      */           
/* 3243 */           String connectedHost = this.io.getHost();
/*      */           
/* 3245 */           if ((connectedHost != null) && (this.hostList.get(0).equals(connectedHost)))
/*      */           {
/* 3247 */             this.failedOver = false;
/* 3248 */             this.queriesIssuedFailedOver = 0L;
/* 3249 */             setReadOnlyInternal(false);
/*      */           }
/*      */         } finally {
/* 3252 */           this.executingFailoverReconnect = false;
/*      */         }
/*      */       }
/*      */       
/*      */ 
/* 3257 */       if (((getHighAvailability()) || (this.failedOver)) && ((this.autoCommit) || (getAutoReconnectForPools())) && (this.needsPing) && (!isBatch))
/*      */       {
/*      */         try
/*      */         {
/* 3261 */           pingInternal(false);
/*      */           
/* 3263 */           this.needsPing = false;
/*      */         } catch (Exception Ex) {
/* 3265 */           createNewIO(true);
/*      */         }
/*      */       }
/*      */       try
/*      */       {
/* 3270 */         if (packet == null) {
/* 3271 */           encoding = null;
/*      */           
/* 3273 */           if (getUseUnicode()) {
/* 3274 */             encoding = getEncoding();
/*      */           }
/*      */           
/* 3277 */           ResultSet localResultSet = this.io.sqlQueryDirect(callingStatement, sql, encoding, null, maxRows, this, resultSetType, resultSetConcurrency, streamResults, catalog, unpackFields);jsr 324;return localResultSet;
/*      */         }
/*      */         
/*      */ 
/*      */ 
/*      */ 
/* 3283 */         String encoding = this.io.sqlQueryDirect(callingStatement, null, null, packet, maxRows, this, resultSetType, resultSetConcurrency, streamResults, catalog, unpackFields);jsr 289;return encoding;
/*      */ 
/*      */ 
/*      */       }
/*      */       catch (SQLException sqlE)
/*      */       {
/*      */ 
/* 3290 */         if (getDumpQueriesOnException()) {
/* 3291 */           String extractedSql = extractSqlFromPacket(sql, packet, endOfQueryPacketPosition);
/*      */           
/* 3293 */           StringBuffer messageBuf = new StringBuffer(extractedSql.length() + 32);
/*      */           
/* 3295 */           messageBuf.append("\n\nQuery being executed when exception was thrown:\n\n");
/*      */           
/* 3297 */           messageBuf.append(extractedSql);
/*      */           
/* 3299 */           sqlE = appendMessageToException(sqlE, messageBuf.toString());
/*      */         }
/*      */         
/* 3302 */         if ((getHighAvailability()) || (this.failedOver)) {
/* 3303 */           this.needsPing = true;
/*      */         } else {
/* 3305 */           String sqlState = sqlE.getSQLState();
/*      */           
/* 3307 */           if ((sqlState != null) && (sqlState.equals("08S01")))
/*      */           {
/*      */ 
/* 3310 */             cleanup(sqlE);
/*      */           }
/*      */         }
/*      */         
/* 3314 */         throw sqlE;
/*      */       } catch (Exception ex) {
/* 3316 */         if ((getHighAvailability()) || (this.failedOver)) {
/* 3317 */           this.needsPing = true;
/* 3318 */         } else if ((ex instanceof IOException)) {
/* 3319 */           cleanup(ex);
/*      */         }
/*      */         
/* 3322 */         String exceptionType = ex.getClass().getName();
/* 3323 */         String exceptionMessage = ex.getMessage();
/*      */         
/* 3325 */         if (!getParanoid()) {
/* 3326 */           exceptionMessage = exceptionMessage + "\n\nNested Stack Trace:\n";
/* 3327 */           exceptionMessage = exceptionMessage + Util.stackTraceToString(ex);
/*      */         }
/*      */         
/* 3330 */         throw new SQLException("Error during query: Unexpected Exception: " + exceptionType + " message given: " + exceptionMessage, "S1000");
/*      */ 
/*      */       }
/*      */       finally
/*      */       {
/*      */ 
/* 3336 */         jsr 6; } localObject3 = returnAddress; if (getMaintainTimeStats()) {
/* 3337 */         this.lastQueryFinishedTime = System.currentTimeMillis();
/*      */       }
/*      */       
/* 3340 */       if (this.failedOver) {
/* 3341 */         this.queriesIssuedFailedOver += 1L;
/*      */       }
/*      */       
/* 3344 */       if (getGatherPerformanceMetrics()) {
/* 3345 */         long queryTime = System.currentTimeMillis() - queryStartTime;
/*      */         
/*      */ 
/* 3348 */         registerQueryExecutionTime(queryTime); } ret;
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   protected String extractSqlFromPacket(String possibleSqlQuery, Buffer queryPacket, int endOfQueryPacketPosition)
/*      */     throws SQLException
/*      */   {
/* 3358 */     String extractedSql = null;
/*      */     
/* 3360 */     if (possibleSqlQuery != null) {
/* 3361 */       if (possibleSqlQuery.length() > getMaxQuerySizeToLog()) {
/* 3362 */         StringBuffer truncatedQueryBuf = new StringBuffer(possibleSqlQuery.substring(0, getMaxQuerySizeToLog()));
/*      */         
/* 3364 */         truncatedQueryBuf.append(Messages.getString("MysqlIO.25"));
/* 3365 */         extractedSql = truncatedQueryBuf.toString();
/*      */       } else {
/* 3367 */         extractedSql = possibleSqlQuery;
/*      */       }
/*      */     }
/*      */     
/* 3371 */     if (extractedSql == null)
/*      */     {
/*      */ 
/*      */ 
/* 3375 */       int extractPosition = endOfQueryPacketPosition;
/*      */       
/* 3377 */       boolean truncated = false;
/*      */       
/* 3379 */       if (endOfQueryPacketPosition > getMaxQuerySizeToLog()) {
/* 3380 */         extractPosition = getMaxQuerySizeToLog();
/* 3381 */         truncated = true;
/*      */       }
/*      */       
/* 3384 */       extractedSql = new String(queryPacket.getByteBuffer(), 5, extractPosition - 5);
/*      */       
/*      */ 
/* 3387 */       if (truncated) {
/* 3388 */         extractedSql = extractedSql + Messages.getString("MysqlIO.25");
/*      */       }
/*      */     }
/*      */     
/* 3392 */     return extractedSql;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected void finalize()
/*      */     throws Throwable
/*      */   {
/* 3403 */     cleanup(null);
/*      */   }
/*      */   
/*      */   protected StringBuffer generateConnectionCommentBlock(StringBuffer buf) {
/* 3407 */     buf.append("/* conn id ");
/* 3408 */     buf.append(getId());
/* 3409 */     buf.append(" */ ");
/*      */     
/* 3411 */     return buf;
/*      */   }
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
/*      */   public boolean getAutoCommit()
/*      */     throws SQLException
/*      */   {
/* 3435 */     return this.autoCommit;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   protected Calendar getCalendarInstanceForSessionOrNew()
/*      */   {
/* 3443 */     if (getDynamicCalendars()) {
/* 3444 */       return Calendar.getInstance();
/*      */     }
/*      */     
/* 3447 */     return getSessionLockedCalendar();
/*      */   }
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
/*      */   public String getCatalog()
/*      */     throws SQLException
/*      */   {
/* 3462 */     return this.database;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   protected String getCharacterSetMetadata()
/*      */   {
/* 3469 */     return this.characterSetMetadata;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   SingleByteCharsetConverter getCharsetConverter(String javaEncodingName)
/*      */     throws SQLException
/*      */   {
/* 3482 */     if (javaEncodingName == null) {
/* 3483 */       return null;
/*      */     }
/*      */     
/* 3486 */     if (this.usePlatformCharsetConverters) {
/* 3487 */       return null;
/*      */     }
/*      */     
/*      */ 
/* 3491 */     SingleByteCharsetConverter converter = null;
/*      */     
/* 3493 */     synchronized (this.charsetConverterMap) {
/* 3494 */       Object asObject = this.charsetConverterMap.get(javaEncodingName);
/*      */       
/*      */ 
/* 3497 */       if (asObject == CHARSET_CONVERTER_NOT_AVAILABLE_MARKER) {
/* 3498 */         return null;
/*      */       }
/*      */       
/* 3501 */       converter = (SingleByteCharsetConverter)asObject;
/*      */       
/* 3503 */       if (converter == null) {
/*      */         try {
/* 3505 */           converter = SingleByteCharsetConverter.getInstance(javaEncodingName, this);
/*      */           
/*      */ 
/* 3508 */           if (converter == null) {
/* 3509 */             this.charsetConverterMap.put(javaEncodingName, CHARSET_CONVERTER_NOT_AVAILABLE_MARKER);
/*      */           }
/*      */           else {
/* 3512 */             this.charsetConverterMap.put(javaEncodingName, converter);
/*      */           }
/*      */         } catch (UnsupportedEncodingException unsupEncEx) {
/* 3515 */           this.charsetConverterMap.put(javaEncodingName, CHARSET_CONVERTER_NOT_AVAILABLE_MARKER);
/*      */           
/*      */ 
/* 3518 */           converter = null;
/*      */         }
/*      */       }
/*      */     }
/*      */     
/* 3523 */     return converter;
/*      */   }
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
/*      */   protected String getCharsetNameForIndex(int charsetIndex)
/*      */     throws SQLException
/*      */   {
/* 3538 */     String charsetName = null;
/*      */     
/* 3540 */     if (getUseOldUTF8Behavior()) {
/* 3541 */       return getEncoding();
/*      */     }
/*      */     
/* 3544 */     if (charsetIndex != -1) {
/*      */       try {
/* 3546 */         charsetName = this.indexToCharsetMapping[charsetIndex];
/*      */         
/* 3548 */         if (("sjis".equalsIgnoreCase(charsetName)) || ("MS932".equalsIgnoreCase(charsetName)))
/*      */         {
/*      */ 
/* 3551 */           if (CharsetMapping.isAliasForSjis(getEncoding())) {
/* 3552 */             charsetName = getEncoding();
/*      */           }
/*      */         }
/*      */       } catch (ArrayIndexOutOfBoundsException outOfBoundsEx) {
/* 3556 */         throw SQLError.createSQLException("Unknown character set index for field '" + charsetIndex + "' received from server.", "S1000");
/*      */       }
/*      */       
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 3563 */       if (charsetName == null) {
/* 3564 */         charsetName = getEncoding();
/*      */       }
/*      */     } else {
/* 3567 */       charsetName = getEncoding();
/*      */     }
/*      */     
/* 3570 */     return charsetName;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected TimeZone getDefaultTimeZone()
/*      */   {
/* 3579 */     return this.defaultTimeZone;
/*      */   }
/*      */   
/*      */ 
/*      */   public int getHoldability()
/*      */     throws SQLException
/*      */   {
/* 3586 */     return 2;
/*      */   }
/*      */   
/*      */   long getId() {
/* 3590 */     return this.connectionId;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public long getIdleFor()
/*      */   {
/* 3602 */     if (this.lastQueryFinishedTime == 0L) {
/* 3603 */       return 0L;
/*      */     }
/*      */     
/* 3606 */     long now = System.currentTimeMillis();
/* 3607 */     long idleTime = now - this.lastQueryFinishedTime;
/*      */     
/* 3609 */     return idleTime;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected MysqlIO getIO()
/*      */     throws SQLException
/*      */   {
/* 3620 */     if ((this.io == null) || (this.isClosed)) {
/* 3621 */       throw SQLError.createSQLException("Operation not allowed on closed connection", "08003");
/*      */     }
/*      */     
/*      */ 
/*      */ 
/* 3626 */     return this.io;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public Log getLog()
/*      */     throws SQLException
/*      */   {
/* 3638 */     return this.log;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   int getMaxAllowedPacket()
/*      */   {
/* 3647 */     return this.maxAllowedPacket;
/*      */   }
/*      */   
/*      */   protected int getMaxBytesPerChar(String javaCharsetName)
/*      */     throws SQLException
/*      */   {
/* 3653 */     String charset = CharsetMapping.getMysqlEncodingForJavaEncoding(javaCharsetName, this);
/*      */     
/*      */ 
/* 3656 */     if (versionMeetsMinimum(4, 1, 0)) {
/* 3657 */       Map mapToCheck = null;
/*      */       
/* 3659 */       if (!getUseDynamicCharsetInfo()) {
/* 3660 */         mapToCheck = CharsetMapping.STATIC_CHARSET_TO_NUM_BYTES_MAP;
/*      */       } else {
/* 3662 */         mapToCheck = this.charsetToNumBytesMap;
/*      */         
/* 3664 */         synchronized (this.charsetToNumBytesMap) {
/* 3665 */           if (this.charsetToNumBytesMap.isEmpty())
/*      */           {
/* 3667 */             java.sql.Statement stmt = null;
/* 3668 */             java.sql.ResultSet rs = null;
/*      */             try
/*      */             {
/* 3671 */               stmt = getMetadataSafeStatement();
/*      */               
/* 3673 */               rs = stmt.executeQuery("SHOW CHARACTER SET");
/*      */               
/* 3675 */               while (rs.next()) {
/* 3676 */                 this.charsetToNumBytesMap.put(rs.getString("Charset"), new Integer(rs.getInt("Maxlen")));
/*      */               }
/*      */               
/*      */ 
/* 3680 */               rs.close();
/* 3681 */               rs = null;
/*      */               
/* 3683 */               stmt.close();
/*      */               
/* 3685 */               stmt = null;
/*      */             } finally {
/* 3687 */               if (rs != null) {
/* 3688 */                 rs.close();
/* 3689 */                 rs = null;
/*      */               }
/*      */               
/* 3692 */               if (stmt != null) {
/* 3693 */                 stmt.close();
/* 3694 */                 stmt = null;
/*      */               }
/*      */             }
/*      */           }
/*      */         }
/*      */       }
/*      */       
/* 3701 */       Integer mbPerChar = (Integer)mapToCheck.get(charset);
/*      */       
/* 3703 */       if (mbPerChar != null) {
/* 3704 */         return mbPerChar.intValue();
/*      */       }
/*      */       
/* 3707 */       return 1;
/*      */     }
/*      */     
/* 3710 */     return 1;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public java.sql.DatabaseMetaData getMetaData()
/*      */     throws SQLException
/*      */   {
/* 3724 */     checkClosed();
/*      */     
/* 3726 */     if ((getUseInformationSchema()) && (versionMeetsMinimum(5, 0, 7)))
/*      */     {
/* 3728 */       return new DatabaseMetaDataUsingInfoSchema(this, this.database);
/*      */     }
/*      */     
/* 3731 */     return new DatabaseMetaData(this, this.database);
/*      */   }
/*      */   
/*      */   protected java.sql.Statement getMetadataSafeStatement() throws SQLException {
/* 3735 */     java.sql.Statement stmt = createStatement();
/*      */     
/* 3737 */     if (stmt.getMaxRows() != 0) {
/* 3738 */       stmt.setMaxRows(0);
/*      */     }
/*      */     
/* 3741 */     stmt.setEscapeProcessing(false);
/*      */     
/* 3743 */     return stmt;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   Object getMutex()
/*      */     throws SQLException
/*      */   {
/* 3754 */     if (this.io == null) {
/* 3755 */       throw SQLError.createSQLException("Connection.close() has already been called. Invalid operation in this state.", "08003");
/*      */     }
/*      */     
/*      */ 
/*      */ 
/* 3760 */     reportMetricsIfNeeded();
/*      */     
/* 3762 */     return this.mutex;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   int getNetBufferLength()
/*      */   {
/* 3771 */     return this.netBufferLength;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected String getServerCharacterEncoding()
/*      */   {
/* 3780 */     if (this.io.versionMeetsMinimum(4, 1, 0)) {
/* 3781 */       return (String)this.serverVariables.get("character_set_server");
/*      */     }
/* 3783 */     return (String)this.serverVariables.get("character_set");
/*      */   }
/*      */   
/*      */   int getServerMajorVersion()
/*      */   {
/* 3788 */     return this.io.getServerMajorVersion();
/*      */   }
/*      */   
/*      */   int getServerMinorVersion() {
/* 3792 */     return this.io.getServerMinorVersion();
/*      */   }
/*      */   
/*      */   int getServerSubMinorVersion() {
/* 3796 */     return this.io.getServerSubMinorVersion();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public TimeZone getServerTimezoneTZ()
/*      */   {
/* 3805 */     return this.serverTimezoneTZ;
/*      */   }
/*      */   
/*      */   String getServerVariable(String variableName) {
/* 3809 */     if (this.serverVariables != null) {
/* 3810 */       return (String)this.serverVariables.get(variableName);
/*      */     }
/*      */     
/* 3813 */     return null;
/*      */   }
/*      */   
/*      */   String getServerVersion() {
/* 3817 */     return this.io.getServerVersion();
/*      */   }
/*      */   
/*      */   protected Calendar getSessionLockedCalendar()
/*      */   {
/* 3822 */     return this.sessionCalendar;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public int getTransactionIsolation()
/*      */     throws SQLException
/*      */   {
/* 3835 */     if ((this.hasIsolationLevels) && (!getUseLocalSessionState())) {
/* 3836 */       java.sql.Statement stmt = null;
/* 3837 */       java.sql.ResultSet rs = null;
/*      */       try
/*      */       {
/* 3840 */         stmt = getMetadataSafeStatement();
/*      */         
/* 3842 */         String query = null;
/*      */         
/* 3844 */         int offset = 0;
/*      */         
/* 3846 */         if (versionMeetsMinimum(4, 0, 3)) {
/* 3847 */           query = "SELECT @@session.tx_isolation";
/* 3848 */           offset = 1;
/*      */         } else {
/* 3850 */           query = "SHOW VARIABLES LIKE 'transaction_isolation'";
/* 3851 */           offset = 2;
/*      */         }
/*      */         
/* 3854 */         rs = stmt.executeQuery(query);
/*      */         
/* 3856 */         if (rs.next()) {
/* 3857 */           String s = rs.getString(offset);
/*      */           
/* 3859 */           if (s != null) {
/* 3860 */             Integer intTI = (Integer)mapTransIsolationNameToValue.get(s);
/*      */             
/*      */ 
/* 3863 */             if (intTI != null) {
/* 3864 */               return intTI.intValue();
/*      */             }
/*      */           }
/*      */           
/* 3868 */           throw SQLError.createSQLException("Could not map transaction isolation '" + s + " to a valid JDBC level.", "S1000");
/*      */         }
/*      */         
/*      */ 
/*      */ 
/*      */ 
/* 3874 */         throw SQLError.createSQLException("Could not retrieve transaction isolation level from server", "S1000");
/*      */ 
/*      */       }
/*      */       finally
/*      */       {
/* 3879 */         if (rs != null) {
/*      */           try {
/* 3881 */             rs.close();
/*      */           }
/*      */           catch (Exception ex) {}
/*      */           
/*      */ 
/*      */ 
/* 3887 */           rs = null;
/*      */         }
/*      */         
/* 3890 */         if (stmt != null) {
/*      */           try {
/* 3892 */             stmt.close();
/*      */           }
/*      */           catch (Exception ex) {}
/*      */           
/*      */ 
/*      */ 
/* 3898 */           stmt = null;
/*      */         }
/*      */       }
/*      */     }
/*      */     
/* 3903 */     return this.isolationLevel;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public synchronized Map getTypeMap()
/*      */     throws SQLException
/*      */   {
/* 3915 */     if (this.typeMap == null) {
/* 3916 */       this.typeMap = new HashMap();
/*      */     }
/*      */     
/* 3919 */     return this.typeMap;
/*      */   }
/*      */   
/*      */   String getURL() {
/* 3923 */     return this.myURL;
/*      */   }
/*      */   
/*      */   String getUser() {
/* 3927 */     return this.user;
/*      */   }
/*      */   
/*      */   protected Calendar getUtcCalendar() {
/* 3931 */     return this.utcCalendar;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public SQLWarning getWarnings()
/*      */     throws SQLException
/*      */   {
/* 3944 */     return null;
/*      */   }
/*      */   
/*      */   public boolean hasSameProperties(Connection c) {
/* 3948 */     return this.props.equals(c.props);
/*      */   }
/*      */   
/*      */   protected void incrementNumberOfPreparedExecutes() {
/* 3952 */     if (getGatherPerformanceMetrics()) {
/* 3953 */       this.numberOfPreparedExecutes += 1L;
/*      */       
/*      */ 
/*      */ 
/*      */ 
/* 3958 */       this.numberOfQueriesIssued += 1L;
/*      */     }
/*      */   }
/*      */   
/*      */   protected void incrementNumberOfPrepares() {
/* 3963 */     if (getGatherPerformanceMetrics()) {
/* 3964 */       this.numberOfPrepares += 1L;
/*      */     }
/*      */   }
/*      */   
/*      */   protected void incrementNumberOfResultSetsCreated() {
/* 3969 */     if (getGatherPerformanceMetrics()) {
/* 3970 */       this.numberOfResultSetsCreated += 1L;
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   private void initializeDriverProperties(Properties info)
/*      */     throws SQLException
/*      */   {
/* 3985 */     initializeProperties(info);
/*      */     
/* 3987 */     this.usePlatformCharsetConverters = getUseJvmCharsetConverters();
/*      */     
/* 3989 */     this.log = LogFactory.getLogger(getLogger(), "MySQL");
/*      */     
/* 3991 */     if ((getProfileSql()) || (getUseUsageAdvisor())) {
/* 3992 */       this.eventSink = ProfileEventSink.getInstance(this);
/*      */     }
/*      */     
/* 3995 */     if (getCachePreparedStatements()) {
/* 3996 */       createPreparedStatementCaches();
/*      */     }
/*      */     
/* 3999 */     if ((getNoDatetimeStringSync()) && (getUseTimezone())) {
/* 4000 */       throw SQLError.createSQLException("Can't enable noDatetimeSync and useTimezone configuration properties at the same time", "01S00");
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/* 4006 */     if (getCacheCallableStatements()) {
/* 4007 */       this.parsedCallableStatementCache = new LRUCache(getCallableStatementCacheSize());
/*      */     }
/*      */     
/*      */ 
/* 4011 */     if (getAllowMultiQueries()) {
/* 4012 */       setCacheResultSetMetadata(false);
/*      */     }
/*      */     
/* 4015 */     if (getCacheResultSetMetadata()) {
/* 4016 */       this.resultSetMetadataCache = new LRUCache(getMetadataCacheSize());
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   private void initializePropsFromServer()
/*      */     throws SQLException
/*      */   {
/* 4031 */     setSessionVariables();
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 4037 */     if (!versionMeetsMinimum(4, 1, 0)) {
/* 4038 */       setTransformedBitIsBoolean(false);
/*      */     }
/*      */     
/* 4041 */     this.parserKnowsUnicode = versionMeetsMinimum(4, 1, 0);
/*      */     
/*      */ 
/*      */ 
/*      */ 
/* 4046 */     if ((getUseServerPreparedStmts()) && (versionMeetsMinimum(4, 1, 0))) {
/* 4047 */       this.useServerPreparedStmts = true;
/*      */       
/* 4049 */       if ((versionMeetsMinimum(5, 0, 0)) && (!versionMeetsMinimum(5, 0, 3))) {
/* 4050 */         this.useServerPreparedStmts = false;
/*      */       }
/*      */     }
/*      */     
/*      */ 
/*      */ 
/* 4056 */     this.serverVariables.clear();
/*      */     
/*      */ 
/*      */ 
/*      */ 
/* 4061 */     if (versionMeetsMinimum(3, 21, 22)) {
/* 4062 */       loadServerVariables();
/*      */       
/* 4064 */       buildCollationMapping();
/*      */       
/* 4066 */       LicenseConfiguration.checkLicenseType(this.serverVariables);
/*      */       
/* 4068 */       String lowerCaseTables = (String)this.serverVariables.get("lower_case_table_names");
/*      */       
/*      */ 
/* 4071 */       this.lowerCaseTableNames = (("on".equalsIgnoreCase(lowerCaseTables)) || ("1".equalsIgnoreCase(lowerCaseTables)) || ("2".equalsIgnoreCase(lowerCaseTables)));
/*      */       
/*      */ 
/*      */ 
/* 4075 */       configureTimezone();
/*      */       
/* 4077 */       if (this.serverVariables.containsKey("max_allowed_packet")) {
/* 4078 */         this.maxAllowedPacket = getServerVariableAsInt("max_allowed_packet", 1048576);
/*      */         
/* 4080 */         int preferredBlobSendChunkSize = getBlobSendChunkSize();
/*      */         
/* 4082 */         int allowedBlobSendChunkSize = Math.min(preferredBlobSendChunkSize, this.maxAllowedPacket) - 8192 - 11;
/*      */         
/*      */ 
/*      */ 
/*      */ 
/* 4087 */         setBlobSendChunkSize(String.valueOf(allowedBlobSendChunkSize));
/*      */       }
/*      */       
/* 4090 */       if (this.serverVariables.containsKey("net_buffer_length")) {
/* 4091 */         this.netBufferLength = getServerVariableAsInt("net_buffer_length", 16384);
/*      */       }
/*      */       
/* 4094 */       checkTransactionIsolationLevel();
/*      */       
/* 4096 */       if (!versionMeetsMinimum(4, 1, 0)) {
/* 4097 */         checkServerEncoding();
/*      */       }
/*      */       
/* 4100 */       this.io.checkForCharsetMismatch();
/*      */       
/* 4102 */       if (this.serverVariables.containsKey("sql_mode")) {
/* 4103 */         int sqlMode = 0;
/*      */         
/* 4105 */         String sqlModeAsString = (String)this.serverVariables.get("sql_mode");
/*      */         try
/*      */         {
/* 4108 */           sqlMode = Integer.parseInt(sqlModeAsString);
/*      */         }
/*      */         catch (NumberFormatException nfe)
/*      */         {
/* 4112 */           sqlMode = 0;
/*      */           
/* 4114 */           if (sqlModeAsString != null) {
/* 4115 */             if (sqlModeAsString.indexOf("ANSI_QUOTES") != -1) {
/* 4116 */               sqlMode |= 0x4;
/*      */             }
/*      */             
/* 4119 */             if (sqlModeAsString.indexOf("NO_BACKSLASH_ESCAPES") != -1) {
/* 4120 */               this.noBackslashEscapes = true;
/*      */             }
/*      */           }
/*      */         }
/*      */         
/* 4125 */         if ((sqlMode & 0x4) > 0) {
/* 4126 */           this.useAnsiQuotes = true;
/*      */         } else {
/* 4128 */           this.useAnsiQuotes = false;
/*      */         }
/*      */       }
/*      */     }
/*      */     
/* 4133 */     this.errorMessageEncoding = CharsetMapping.getCharacterEncodingForErrorMessages(this);
/*      */     
/*      */ 
/*      */ 
/* 4137 */     boolean overrideDefaultAutocommit = isAutoCommitNonDefaultOnServer();
/*      */     
/* 4139 */     configureClientCharacterSet();
/*      */     
/* 4141 */     if (versionMeetsMinimum(3, 23, 15)) {
/* 4142 */       this.transactionsSupported = true;
/*      */       
/* 4144 */       if (!overrideDefaultAutocommit) {
/* 4145 */         setAutoCommit(true);
/*      */       }
/*      */     }
/*      */     else
/*      */     {
/* 4150 */       this.transactionsSupported = false;
/*      */     }
/*      */     
/*      */ 
/* 4154 */     if (versionMeetsMinimum(3, 23, 36)) {
/* 4155 */       this.hasIsolationLevels = true;
/*      */     } else {
/* 4157 */       this.hasIsolationLevels = false;
/*      */     }
/*      */     
/* 4160 */     this.hasQuotedIdentifiers = versionMeetsMinimum(3, 23, 6);
/*      */     
/* 4162 */     this.io.resetMaxBuf();
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 4172 */     if (this.io.versionMeetsMinimum(4, 1, 0)) {
/* 4173 */       String characterSetResultsOnServerMysql = (String)this.serverVariables.get("jdbc.local.character_set_results");
/*      */       
/*      */ 
/* 4176 */       if ((characterSetResultsOnServerMysql == null) || (StringUtils.startsWithIgnoreCaseAndWs(characterSetResultsOnServerMysql, "NULL")) || (characterSetResultsOnServerMysql.length() == 0))
/*      */       {
/*      */ 
/*      */ 
/* 4180 */         String defaultMetadataCharsetMysql = (String)this.serverVariables.get("character_set_system");
/*      */         
/* 4182 */         String defaultMetadataCharset = null;
/*      */         
/* 4184 */         if (defaultMetadataCharsetMysql != null) {
/* 4185 */           defaultMetadataCharset = CharsetMapping.getJavaEncodingForMysqlEncoding(defaultMetadataCharsetMysql, this);
/*      */         }
/*      */         else
/*      */         {
/* 4189 */           defaultMetadataCharset = "UTF-8";
/*      */         }
/*      */         
/* 4192 */         this.characterSetMetadata = defaultMetadataCharset;
/*      */       } else {
/* 4194 */         this.characterSetResultsOnServer = CharsetMapping.getJavaEncodingForMysqlEncoding(characterSetResultsOnServerMysql, this);
/*      */         
/*      */ 
/* 4197 */         this.characterSetMetadata = this.characterSetResultsOnServer;
/*      */       }
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 4205 */     if ((versionMeetsMinimum(4, 1, 0)) && (!versionMeetsMinimum(4, 1, 10)) && (getAllowMultiQueries()))
/*      */     {
/*      */ 
/* 4208 */       if (("ON".equalsIgnoreCase((String)this.serverVariables.get("query_cache_type"))) && (!"0".equalsIgnoreCase((String)this.serverVariables.get("query_cache_size"))))
/*      */       {
/*      */ 
/*      */ 
/* 4212 */         setAllowMultiQueries(false);
/*      */       }
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 4220 */     setupServerForTruncationChecks();
/*      */   }
/*      */   
/*      */   private int getServerVariableAsInt(String variableName, int fallbackValue) throws SQLException
/*      */   {
/*      */     try {
/* 4226 */       return Integer.parseInt((String)this.serverVariables.get(variableName));
/*      */     }
/*      */     catch (NumberFormatException nfe) {
/* 4229 */       getLog().logWarn(Messages.getString("Connection.BadValueInServerVariables", new Object[] { variableName, this.serverVariables.get(variableName), new Integer(fallbackValue) }));
/*      */     }
/*      */     
/* 4232 */     return fallbackValue;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   private boolean isAutoCommitNonDefaultOnServer()
/*      */     throws SQLException
/*      */   {
/* 4245 */     boolean overrideDefaultAutocommit = false;
/*      */     
/* 4247 */     String initConnectValue = (String)this.serverVariables.get("init_connect");
/*      */     
/*      */ 
/* 4250 */     if ((versionMeetsMinimum(4, 1, 2)) && (initConnectValue != null) && (initConnectValue.length() > 0))
/*      */     {
/* 4252 */       if (!getElideSetAutoCommits())
/*      */       {
/* 4254 */         java.sql.ResultSet rs = null;
/* 4255 */         java.sql.Statement stmt = null;
/*      */         try
/*      */         {
/* 4258 */           stmt = getMetadataSafeStatement();
/*      */           
/* 4260 */           rs = stmt.executeQuery("SELECT @@session.autocommit");
/*      */           
/* 4262 */           if (rs.next()) {
/* 4263 */             this.autoCommit = rs.getBoolean(1);
/* 4264 */             if (this.autoCommit != true) {
/* 4265 */               overrideDefaultAutocommit = true;
/*      */             }
/*      */           }
/*      */         }
/*      */         finally {
/* 4270 */           if (rs != null) {
/*      */             try {
/* 4272 */               rs.close();
/*      */             }
/*      */             catch (SQLException sqlEx) {}
/*      */           }
/*      */           
/*      */ 
/* 4278 */           if (stmt != null) {
/*      */             try {
/* 4280 */               stmt.close();
/*      */ 
/*      */             }
/*      */             catch (SQLException sqlEx) {}
/*      */           }
/*      */         }
/*      */       }
/* 4287 */       else if (getIO().isSetNeededForAutoCommitMode(true))
/*      */       {
/* 4289 */         this.autoCommit = false;
/* 4290 */         overrideDefaultAutocommit = true;
/*      */       }
/*      */     }
/*      */     
/*      */ 
/* 4295 */     return overrideDefaultAutocommit;
/*      */   }
/*      */   
/*      */   private void setupServerForTruncationChecks() throws SQLException {
/* 4299 */     if ((getJdbcCompliantTruncation()) && 
/* 4300 */       (versionMeetsMinimum(5, 0, 2)))
/*      */     {
/* 4302 */       String currentSqlMode = (String)this.serverVariables.get("sql_mode");
/*      */       
/*      */ 
/* 4305 */       boolean strictTransTablesIsSet = StringUtils.indexOfIgnoreCase(currentSqlMode, "STRICT_TRANS_TABLES") != -1;
/*      */       
/* 4307 */       if ((currentSqlMode == null) || (currentSqlMode.length() == 0) || (!strictTransTablesIsSet))
/*      */       {
/* 4309 */         StringBuffer commandBuf = new StringBuffer("SET sql_mode='");
/*      */         
/* 4311 */         if ((currentSqlMode != null) && (currentSqlMode.length() > 0)) {
/* 4312 */           commandBuf.append(currentSqlMode);
/* 4313 */           commandBuf.append(",");
/*      */         }
/*      */         
/* 4316 */         commandBuf.append("STRICT_TRANS_TABLES'");
/*      */         
/* 4318 */         execSQL(null, commandBuf.toString(), -1, null, 1003, 1007, false, this.database, true, false);
/*      */         
/*      */ 
/*      */ 
/*      */ 
/* 4323 */         setJdbcCompliantTruncation(false);
/* 4324 */       } else if (strictTransTablesIsSet)
/*      */       {
/* 4326 */         setJdbcCompliantTruncation(false);
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */   protected boolean isClientTzUTC()
/*      */   {
/* 4334 */     return this.isClientTzUTC;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public boolean isClosed()
/*      */   {
/* 4343 */     return this.isClosed;
/*      */   }
/*      */   
/*      */   protected boolean isCursorFetchEnabled() throws SQLException {
/* 4347 */     return (versionMeetsMinimum(5, 0, 2)) && (getUseCursorFetch());
/*      */   }
/*      */   
/*      */   public boolean isInGlobalTx() {
/* 4351 */     return this.isInGlobalTx;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public synchronized boolean isMasterConnection()
/*      */   {
/* 4362 */     return !this.failedOver;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public boolean isNoBackslashEscapesSet()
/*      */   {
/* 4372 */     return this.noBackslashEscapes;
/*      */   }
/*      */   
/*      */   boolean isReadInfoMsgEnabled() {
/* 4376 */     return this.readInfoMsg;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public boolean isReadOnly()
/*      */     throws SQLException
/*      */   {
/* 4389 */     return this.readOnly;
/*      */   }
/*      */   
/*      */   protected boolean isRunningOnJDK13() {
/* 4393 */     return this.isRunningOnJDK13;
/*      */   }
/*      */   
/*      */   public synchronized boolean isSameResource(Connection otherConnection) {
/* 4397 */     if (otherConnection == null) {
/* 4398 */       return false;
/*      */     }
/*      */     
/* 4401 */     boolean directCompare = true;
/*      */     
/* 4403 */     String otherHost = otherConnection.origHostToConnectTo;
/* 4404 */     String otherOrigDatabase = otherConnection.origDatabaseToConnectTo;
/* 4405 */     String otherCurrentCatalog = otherConnection.database;
/*      */     
/* 4407 */     if (!nullSafeCompare(otherHost, this.origHostToConnectTo)) {
/* 4408 */       directCompare = false;
/* 4409 */     } else if ((otherHost != null) && (otherHost.indexOf(",") == -1) && (otherHost.indexOf(":") == -1))
/*      */     {
/*      */ 
/* 4412 */       directCompare = otherConnection.origPortToConnectTo == this.origPortToConnectTo;
/*      */     }
/*      */     
/*      */ 
/* 4416 */     if (directCompare) {
/* 4417 */       if (!nullSafeCompare(otherOrigDatabase, this.origDatabaseToConnectTo)) { directCompare = false;
/* 4418 */         directCompare = false;
/* 4419 */       } else if (!nullSafeCompare(otherCurrentCatalog, this.database)) {
/* 4420 */         directCompare = false;
/*      */       }
/*      */     }
/*      */     
/* 4424 */     if (directCompare) {
/* 4425 */       return true;
/*      */     }
/*      */     
/*      */ 
/* 4429 */     String otherResourceId = otherConnection.getResourceId();
/* 4430 */     String myResourceId = getResourceId();
/*      */     
/* 4432 */     if ((otherResourceId != null) || (myResourceId != null)) {
/* 4433 */       directCompare = nullSafeCompare(otherResourceId, myResourceId);
/*      */       
/* 4435 */       if (directCompare) {
/* 4436 */         return true;
/*      */       }
/*      */     }
/*      */     
/* 4440 */     return false;
/*      */   }
/*      */   
/*      */   protected boolean isServerTzUTC() {
/* 4444 */     return this.isServerTzUTC;
/*      */   }
/*      */   
/* 4447 */   private boolean usingCachedConfig = false;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   private void loadServerVariables()
/*      */     throws SQLException
/*      */   {
/* 4458 */     if (getCacheServerConfiguration()) {
/* 4459 */       synchronized (serverConfigByUrl) {
/* 4460 */         Map cachedVariableMap = (Map)serverConfigByUrl.get(getURL());
/*      */         
/* 4462 */         if (cachedVariableMap != null) {
/* 4463 */           this.serverVariables = cachedVariableMap;
/* 4464 */           this.usingCachedConfig = true;
/*      */           
/* 4466 */           return;
/*      */         }
/*      */       }
/*      */     }
/*      */     
/* 4471 */     Statement stmt = null;
/* 4472 */     ResultSet results = null;
/*      */     try
/*      */     {
/* 4475 */       stmt = (Statement)createStatement();
/* 4476 */       stmt.setEscapeProcessing(false);
/*      */       
/* 4478 */       results = (ResultSet)stmt.executeQuery("SHOW SESSION VARIABLES");
/*      */       
/*      */ 
/* 4481 */       while (results.next()) {
/* 4482 */         this.serverVariables.put(results.getString(1), results.getString(2));
/*      */       }
/*      */       
/*      */ 
/* 4486 */       if (getCacheServerConfiguration()) {
/* 4487 */         synchronized (serverConfigByUrl) {
/* 4488 */           serverConfigByUrl.put(getURL(), this.serverVariables);
/*      */         }
/*      */       }
/*      */     } catch (SQLException e) {
/* 4492 */       throw e;
/*      */     } finally {
/* 4494 */       if (results != null) {
/*      */         try {
/* 4496 */           results.close();
/*      */         }
/*      */         catch (SQLException sqlE) {}
/*      */       }
/*      */       
/*      */ 
/* 4502 */       if (stmt != null) {
/*      */         try {
/* 4504 */           stmt.close();
/*      */         }
/*      */         catch (SQLException sqlE) {}
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public boolean lowerCaseTableNames()
/*      */   {
/* 4518 */     return this.lowerCaseTableNames;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   void maxRowsChanged(Statement stmt)
/*      */   {
/* 4528 */     synchronized (this.mutex) {
/* 4529 */       if (this.statementsUsingMaxRows == null) {
/* 4530 */         this.statementsUsingMaxRows = new HashMap();
/*      */       }
/*      */       
/* 4533 */       this.statementsUsingMaxRows.put(stmt, stmt);
/*      */       
/* 4535 */       this.maxRowsChanged = true;
/*      */     }
/*      */   }
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
/*      */   public String nativeSQL(String sql)
/*      */     throws SQLException
/*      */   {
/* 4552 */     if (sql == null) {
/* 4553 */       return null;
/*      */     }
/*      */     
/* 4556 */     Object escapedSqlResult = EscapeProcessor.escapeSQL(sql, serverSupportsConvertFn(), this);
/*      */     
/*      */ 
/*      */ 
/* 4560 */     if ((escapedSqlResult instanceof String)) {
/* 4561 */       return (String)escapedSqlResult;
/*      */     }
/*      */     
/* 4564 */     return ((EscapeProcessorResult)escapedSqlResult).escapedSql;
/*      */   }
/*      */   
/*      */   private CallableStatement parseCallableStatement(String sql) throws SQLException
/*      */   {
/* 4569 */     Object escapedSqlResult = EscapeProcessor.escapeSQL(sql, serverSupportsConvertFn(), this);
/*      */     
/*      */ 
/* 4572 */     boolean isFunctionCall = false;
/* 4573 */     String parsedSql = null;
/*      */     
/* 4575 */     if ((escapedSqlResult instanceof EscapeProcessorResult)) {
/* 4576 */       parsedSql = ((EscapeProcessorResult)escapedSqlResult).escapedSql;
/* 4577 */       isFunctionCall = ((EscapeProcessorResult)escapedSqlResult).callingStoredFunction;
/*      */     } else {
/* 4579 */       parsedSql = (String)escapedSqlResult;
/* 4580 */       isFunctionCall = false;
/*      */     }
/*      */     
/* 4583 */     return new CallableStatement(this, parsedSql, this.database, isFunctionCall);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public boolean parserKnowsUnicode()
/*      */   {
/* 4593 */     return this.parserKnowsUnicode;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void ping()
/*      */     throws SQLException
/*      */   {
/* 4603 */     pingInternal(true);
/*      */   }
/*      */   
/*      */   private void pingInternal(boolean checkForClosedConnection) throws SQLException
/*      */   {
/* 4608 */     if (checkForClosedConnection) {
/* 4609 */       checkClosed();
/*      */     }
/*      */     
/*      */ 
/* 4613 */     this.io.sendCommand(14, null, null, false, null);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public java.sql.CallableStatement prepareCall(String sql)
/*      */     throws SQLException
/*      */   {
/* 4627 */     if (getUseUltraDevWorkAround()) {
/* 4628 */       return new UltraDevWorkAround(prepareStatement(sql));
/*      */     }
/*      */     
/* 4631 */     return prepareCall(sql, 1003, 1007);
/*      */   }
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
/*      */   public java.sql.CallableStatement prepareCall(String sql, int resultSetType, int resultSetConcurrency)
/*      */     throws SQLException
/*      */   {
/* 4652 */     if (versionMeetsMinimum(5, 0, 0)) {
/* 4653 */       CallableStatement cStmt = null;
/*      */       
/* 4655 */       if (!getCacheCallableStatements())
/*      */       {
/* 4657 */         cStmt = parseCallableStatement(sql);
/*      */       } else {
/* 4659 */         synchronized (this.parsedCallableStatementCache) {
/* 4660 */           CompoundCacheKey key = new CompoundCacheKey(getCatalog(), sql);
/*      */           
/* 4662 */           CallableStatement.CallableStatementParamInfo cachedParamInfo = (CallableStatement.CallableStatementParamInfo)this.parsedCallableStatementCache.get(key);
/*      */           
/*      */ 
/* 4665 */           if (cachedParamInfo != null) {
/* 4666 */             cStmt = new CallableStatement(this, cachedParamInfo);
/*      */           } else {
/* 4668 */             cStmt = parseCallableStatement(sql);
/*      */             
/* 4670 */             cachedParamInfo = cStmt.paramInfo;
/*      */             
/* 4672 */             this.parsedCallableStatementCache.put(key, cachedParamInfo);
/*      */           }
/*      */         }
/*      */       }
/*      */       
/* 4677 */       cStmt.setResultSetType(resultSetType);
/* 4678 */       cStmt.setResultSetConcurrency(resultSetConcurrency);
/*      */       
/* 4680 */       return cStmt;
/*      */     }
/*      */     
/* 4683 */     throw SQLError.createSQLException("Callable statements not supported.", "S1C00");
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public java.sql.CallableStatement prepareCall(String sql, int resultSetType, int resultSetConcurrency, int resultSetHoldability)
/*      */     throws SQLException
/*      */   {
/* 4693 */     if ((getPedantic()) && 
/* 4694 */       (resultSetHoldability != 1)) {
/* 4695 */       throw SQLError.createSQLException("HOLD_CUSRORS_OVER_COMMIT is only supported holdability level", "S1009");
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/* 4701 */     CallableStatement cStmt = (CallableStatement)prepareCall(sql, resultSetType, resultSetConcurrency);
/*      */     
/*      */ 
/* 4704 */     return cStmt;
/*      */   }
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
/*      */   public java.sql.PreparedStatement prepareStatement(String sql)
/*      */     throws SQLException
/*      */   {
/* 4734 */     return prepareStatement(sql, 1003, 1007);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public java.sql.PreparedStatement prepareStatement(String sql, int autoGenKeyIndex)
/*      */     throws SQLException
/*      */   {
/* 4743 */     java.sql.PreparedStatement pStmt = prepareStatement(sql);
/*      */     
/* 4745 */     ((PreparedStatement)pStmt).setRetrieveGeneratedKeys(autoGenKeyIndex == 1);
/*      */     
/*      */ 
/* 4748 */     return pStmt;
/*      */   }
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
/*      */   public java.sql.PreparedStatement prepareStatement(String sql, int resultSetType, int resultSetConcurrency)
/*      */     throws SQLException
/*      */   {
/* 4768 */     checkClosed();
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 4774 */     PreparedStatement pStmt = null;
/*      */     
/* 4776 */     boolean canServerPrepare = true;
/*      */     
/* 4778 */     String nativeSql = getProcessEscapeCodesForPrepStmts() ? nativeSQL(sql) : sql;
/*      */     
/* 4780 */     if (getEmulateUnsupportedPstmts()) {
/* 4781 */       canServerPrepare = canHandleAsServerPreparedStatement(nativeSql);
/*      */     }
/*      */     
/* 4784 */     if ((this.useServerPreparedStmts) && (canServerPrepare)) {
/* 4785 */       if (getCachePreparedStatements()) {
/* 4786 */         synchronized (this.serverSideStatementCache) {
/* 4787 */           pStmt = (ServerPreparedStatement)this.serverSideStatementCache.remove(sql);
/*      */           
/* 4789 */           if (pStmt != null) {
/* 4790 */             ((ServerPreparedStatement)pStmt).setClosed(false);
/* 4791 */             pStmt.clearParameters();
/*      */           }
/*      */           
/* 4794 */           if (pStmt == null) {
/*      */             try {
/* 4796 */               pStmt = new ServerPreparedStatement(this, nativeSql, this.database, resultSetType, resultSetConcurrency);
/*      */               
/* 4798 */               if (sql.length() < getPreparedStatementCacheSqlLimit()) {
/* 4799 */                 ((ServerPreparedStatement)pStmt).isCached = true;
/*      */               }
/*      */             }
/*      */             catch (SQLException sqlEx) {
/* 4803 */               if (getEmulateUnsupportedPstmts()) {
/* 4804 */                 pStmt = clientPrepareStatement(nativeSql, resultSetType, resultSetConcurrency, false);
/*      */                 
/* 4806 */                 if (sql.length() < getPreparedStatementCacheSqlLimit()) {
/* 4807 */                   this.serverSideStatementCheckCache.put(sql, Boolean.FALSE);
/*      */                 }
/*      */               } else {
/* 4810 */                 throw sqlEx;
/*      */               }
/*      */             }
/*      */           }
/*      */         }
/*      */       }
/*      */       try {
/* 4817 */         pStmt = new ServerPreparedStatement(this, nativeSql, this.database, resultSetType, resultSetConcurrency);
/*      */       }
/*      */       catch (SQLException sqlEx)
/*      */       {
/* 4821 */         if (getEmulateUnsupportedPstmts()) {
/* 4822 */           pStmt = clientPrepareStatement(nativeSql, resultSetType, resultSetConcurrency, false);
/*      */         } else {
/* 4824 */           throw sqlEx;
/*      */         }
/*      */       }
/*      */     }
/*      */     else {
/* 4829 */       pStmt = clientPrepareStatement(nativeSql, resultSetType, resultSetConcurrency, false);
/*      */     }
/*      */     
/* 4832 */     return pStmt;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public java.sql.PreparedStatement prepareStatement(String sql, int resultSetType, int resultSetConcurrency, int resultSetHoldability)
/*      */     throws SQLException
/*      */   {
/* 4841 */     if ((getPedantic()) && 
/* 4842 */       (resultSetHoldability != 1)) {
/* 4843 */       throw SQLError.createSQLException("HOLD_CUSRORS_OVER_COMMIT is only supported holdability level", "S1009");
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/* 4849 */     return prepareStatement(sql, resultSetType, resultSetConcurrency);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public java.sql.PreparedStatement prepareStatement(String sql, int[] autoGenKeyIndexes)
/*      */     throws SQLException
/*      */   {
/* 4857 */     java.sql.PreparedStatement pStmt = prepareStatement(sql);
/*      */     
/* 4859 */     ((PreparedStatement)pStmt).setRetrieveGeneratedKeys((autoGenKeyIndexes != null) && (autoGenKeyIndexes.length > 0));
/*      */     
/*      */ 
/*      */ 
/* 4863 */     return pStmt;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public java.sql.PreparedStatement prepareStatement(String sql, String[] autoGenKeyColNames)
/*      */     throws SQLException
/*      */   {
/* 4871 */     java.sql.PreparedStatement pStmt = prepareStatement(sql);
/*      */     
/* 4873 */     ((PreparedStatement)pStmt).setRetrieveGeneratedKeys((autoGenKeyColNames != null) && (autoGenKeyColNames.length > 0));
/*      */     
/*      */ 
/*      */ 
/* 4877 */     return pStmt;
/*      */   }
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
/*      */   protected void realClose(boolean calledExplicitly, boolean issueRollback, boolean skipLocalTeardown, Throwable reason)
/*      */     throws SQLException
/*      */   {
/* 4892 */     SQLException sqlEx = null;
/*      */     
/* 4894 */     if (isClosed()) {
/* 4895 */       return;
/*      */     }
/*      */     
/* 4898 */     this.forceClosedReason = reason;
/*      */     try
/*      */     {
/* 4901 */       if (!skipLocalTeardown) {
/* 4902 */         if ((!getAutoCommit()) && (issueRollback)) {
/*      */           try {
/* 4904 */             rollback();
/*      */           } catch (SQLException ex) {
/* 4906 */             sqlEx = ex;
/*      */           }
/*      */         }
/*      */         
/* 4910 */         reportMetrics();
/*      */         
/* 4912 */         if (getUseUsageAdvisor()) {
/* 4913 */           if (!calledExplicitly) {
/* 4914 */             String message = "Connection implicitly closed by Driver. You should call Connection.close() from your code to free resources more efficiently and avoid resource leaks.";
/*      */             
/* 4916 */             this.eventSink.consumeEvent(new ProfilerEvent((byte)0, "", getCatalog(), getId(), -1, -1, System.currentTimeMillis(), 0L, Constants.MILLIS_I18N, null, this.pointOfOrigin, message));
/*      */           }
/*      */           
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 4924 */           long connectionLifeTime = System.currentTimeMillis() - this.connectionCreationTimeMillis;
/*      */           
/*      */ 
/* 4927 */           if (connectionLifeTime < 500L) {
/* 4928 */             String message = "Connection lifetime of < .5 seconds. You might be un-necessarily creating short-lived connections and should investigate connection pooling to be more efficient.";
/*      */             
/* 4930 */             this.eventSink.consumeEvent(new ProfilerEvent((byte)0, "", getCatalog(), getId(), -1, -1, System.currentTimeMillis(), 0L, Constants.MILLIS_I18N, null, this.pointOfOrigin, message));
/*      */           }
/*      */         }
/*      */         
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */         try
/*      */         {
/* 4940 */           closeAllOpenStatements();
/*      */         } catch (SQLException ex) {
/* 4942 */           sqlEx = ex;
/*      */         }
/*      */         
/* 4945 */         if (this.io != null) {
/*      */           try {
/* 4947 */             this.io.quit();
/*      */           }
/*      */           catch (Exception e) {}
/*      */         }
/*      */       }
/*      */       else
/*      */       {
/* 4954 */         this.io.forceClose();
/*      */       }
/*      */     } finally {
/* 4957 */       this.openStatements = null;
/* 4958 */       this.io = null;
/* 4959 */       ProfileEventSink.removeInstance(this);
/* 4960 */       this.isClosed = true;
/*      */     }
/*      */     
/* 4963 */     if (sqlEx != null) {
/* 4964 */       throw sqlEx;
/*      */     }
/*      */   }
/*      */   
/*      */   protected void recachePreparedStatement(ServerPreparedStatement pstmt)
/*      */   {
/* 4970 */     synchronized (this.serverSideStatementCache) {
/* 4971 */       this.serverSideStatementCache.put(pstmt.originalSql, pstmt);
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected void registerQueryExecutionTime(long queryTimeMs)
/*      */   {
/* 4981 */     if (queryTimeMs > this.longestQueryTimeMs) {
/* 4982 */       this.longestQueryTimeMs = queryTimeMs;
/*      */       
/* 4984 */       repartitionPerformanceHistogram();
/*      */     }
/*      */     
/* 4987 */     addToPerformanceHistogram(queryTimeMs, 1);
/*      */     
/* 4989 */     if (queryTimeMs < this.shortestQueryTimeMs) {
/* 4990 */       this.shortestQueryTimeMs = (queryTimeMs == 0L ? 1L : queryTimeMs);
/*      */     }
/*      */     
/* 4993 */     this.numberOfQueriesIssued += 1L;
/*      */     
/* 4995 */     this.totalQueryTimeMs += queryTimeMs;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   void registerStatement(Statement stmt)
/*      */   {
/* 5005 */     synchronized (this.openStatements) {
/* 5006 */       this.openStatements.put(stmt, stmt);
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   private void repartitionHistogram(int[] histCounts, long[] histBreakpoints, long currentLowerBound, long currentUpperBound)
/*      */   {
/* 5020 */     if (this.oldHistCounts == null) {
/* 5021 */       this.oldHistCounts = new int[histCounts.length];
/* 5022 */       this.oldHistBreakpoints = new long[histBreakpoints.length];
/*      */     }
/*      */     
/* 5025 */     for (int i = 0; i < histCounts.length; i++) {
/* 5026 */       this.oldHistCounts[i] = histCounts[i];
/*      */     }
/*      */     
/* 5029 */     for (int i = 0; i < this.oldHistBreakpoints.length; i++) {
/* 5030 */       this.oldHistBreakpoints[i] = histBreakpoints[i];
/*      */     }
/*      */     
/* 5033 */     createInitialHistogram(histBreakpoints, currentLowerBound, currentUpperBound);
/*      */     
/*      */ 
/* 5036 */     for (int i = 0; i < 20; i++) {
/* 5037 */       addToHistogram(histCounts, histBreakpoints, this.oldHistBreakpoints[i], this.oldHistCounts[i], currentLowerBound, currentUpperBound);
/*      */     }
/*      */   }
/*      */   
/*      */   private void repartitionPerformanceHistogram()
/*      */   {
/* 5043 */     checkAndCreatePerformanceHistogram();
/*      */     
/* 5045 */     repartitionHistogram(this.perfMetricsHistCounts, this.perfMetricsHistBreakpoints, this.shortestQueryTimeMs == Long.MAX_VALUE ? 0L : this.shortestQueryTimeMs, this.longestQueryTimeMs);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   private void repartitionTablesAccessedHistogram()
/*      */   {
/* 5052 */     checkAndCreateTablesAccessedHistogram();
/*      */     
/* 5054 */     repartitionHistogram(this.numTablesMetricsHistCounts, this.numTablesMetricsHistBreakpoints, this.minimumNumberTablesAccessed == Long.MAX_VALUE ? 0L : this.minimumNumberTablesAccessed, this.maximumNumberTablesAccessed);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   private void reportMetrics()
/*      */   {
/* 5062 */     if (getGatherPerformanceMetrics()) {
/* 5063 */       StringBuffer logMessage = new StringBuffer(256);
/*      */       
/* 5065 */       logMessage.append("** Performance Metrics Report **\n");
/* 5066 */       logMessage.append("\nLongest reported query: " + this.longestQueryTimeMs + " ms");
/*      */       
/* 5068 */       logMessage.append("\nShortest reported query: " + this.shortestQueryTimeMs + " ms");
/*      */       
/* 5070 */       logMessage.append("\nAverage query execution time: " + this.totalQueryTimeMs / this.numberOfQueriesIssued + " ms");
/*      */       
/*      */ 
/*      */ 
/* 5074 */       logMessage.append("\nNumber of statements executed: " + this.numberOfQueriesIssued);
/*      */       
/* 5076 */       logMessage.append("\nNumber of result sets created: " + this.numberOfResultSetsCreated);
/*      */       
/* 5078 */       logMessage.append("\nNumber of statements prepared: " + this.numberOfPrepares);
/*      */       
/* 5080 */       logMessage.append("\nNumber of prepared statement executions: " + this.numberOfPreparedExecutes);
/*      */       
/*      */ 
/* 5083 */       if (this.perfMetricsHistBreakpoints != null) {
/* 5084 */         logMessage.append("\n\n\tTiming Histogram:\n");
/* 5085 */         int maxNumPoints = 20;
/* 5086 */         int highestCount = Integer.MIN_VALUE;
/*      */         
/* 5088 */         for (int i = 0; i < 20; i++) {
/* 5089 */           if (this.perfMetricsHistCounts[i] > highestCount) {
/* 5090 */             highestCount = this.perfMetricsHistCounts[i];
/*      */           }
/*      */         }
/*      */         
/* 5094 */         if (highestCount == 0) {
/* 5095 */           highestCount = 1;
/*      */         }
/*      */         
/* 5098 */         for (int i = 0; i < 19; i++)
/*      */         {
/* 5100 */           if (i == 0) {
/* 5101 */             logMessage.append("\n\tless than " + this.perfMetricsHistBreakpoints[(i + 1)] + " ms: \t" + this.perfMetricsHistCounts[i]);
/*      */           }
/*      */           else
/*      */           {
/* 5105 */             logMessage.append("\n\tbetween " + this.perfMetricsHistBreakpoints[i] + " and " + this.perfMetricsHistBreakpoints[(i + 1)] + " ms: \t" + this.perfMetricsHistCounts[i]);
/*      */           }
/*      */           
/*      */ 
/*      */ 
/*      */ 
/* 5111 */           logMessage.append("\t");
/*      */           
/* 5113 */           int numPointsToGraph = (int)(maxNumPoints * (this.perfMetricsHistCounts[i] / highestCount));
/*      */           
/* 5115 */           for (int j = 0; j < numPointsToGraph; j++) {
/* 5116 */             logMessage.append("*");
/*      */           }
/*      */           
/* 5119 */           if (this.longestQueryTimeMs < this.perfMetricsHistCounts[(i + 1)]) {
/*      */             break;
/*      */           }
/*      */         }
/*      */         
/* 5124 */         if (this.perfMetricsHistBreakpoints[18] < this.longestQueryTimeMs) {
/* 5125 */           logMessage.append("\n\tbetween ");
/* 5126 */           logMessage.append(this.perfMetricsHistBreakpoints[18]);
/*      */           
/* 5128 */           logMessage.append(" and ");
/* 5129 */           logMessage.append(this.perfMetricsHistBreakpoints[19]);
/*      */           
/* 5131 */           logMessage.append(" ms: \t");
/* 5132 */           logMessage.append(this.perfMetricsHistCounts[19]);
/*      */         }
/*      */       }
/*      */       
/*      */ 
/* 5137 */       if (this.numTablesMetricsHistBreakpoints != null) {
/* 5138 */         logMessage.append("\n\n\tTable Join Histogram:\n");
/* 5139 */         int maxNumPoints = 20;
/* 5140 */         int highestCount = Integer.MIN_VALUE;
/*      */         
/* 5142 */         for (int i = 0; i < 20; i++) {
/* 5143 */           if (this.numTablesMetricsHistCounts[i] > highestCount) {
/* 5144 */             highestCount = this.numTablesMetricsHistCounts[i];
/*      */           }
/*      */         }
/*      */         
/* 5148 */         if (highestCount == 0) {
/* 5149 */           highestCount = 1;
/*      */         }
/*      */         
/* 5152 */         for (int i = 0; i < 19; i++)
/*      */         {
/* 5154 */           if (i == 0) {
/* 5155 */             logMessage.append("\n\t" + this.numTablesMetricsHistBreakpoints[(i + 1)] + " tables or less: \t\t" + this.numTablesMetricsHistCounts[i]);
/*      */ 
/*      */           }
/*      */           else
/*      */           {
/* 5160 */             logMessage.append("\n\tbetween " + this.numTablesMetricsHistBreakpoints[i] + " and " + this.numTablesMetricsHistBreakpoints[(i + 1)] + " tables: \t" + this.numTablesMetricsHistCounts[i]);
/*      */           }
/*      */           
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 5168 */           logMessage.append("\t");
/*      */           
/* 5170 */           int numPointsToGraph = (int)(maxNumPoints * (this.numTablesMetricsHistCounts[i] / highestCount));
/*      */           
/* 5172 */           for (int j = 0; j < numPointsToGraph; j++) {
/* 5173 */             logMessage.append("*");
/*      */           }
/*      */           
/* 5176 */           if (this.maximumNumberTablesAccessed < this.numTablesMetricsHistBreakpoints[(i + 1)]) {
/*      */             break;
/*      */           }
/*      */         }
/*      */         
/* 5181 */         if (this.numTablesMetricsHistBreakpoints[18] < this.maximumNumberTablesAccessed) {
/* 5182 */           logMessage.append("\n\tbetween ");
/* 5183 */           logMessage.append(this.numTablesMetricsHistBreakpoints[18]);
/*      */           
/* 5185 */           logMessage.append(" and ");
/* 5186 */           logMessage.append(this.numTablesMetricsHistBreakpoints[19]);
/*      */           
/* 5188 */           logMessage.append(" tables: ");
/* 5189 */           logMessage.append(this.numTablesMetricsHistCounts[19]);
/*      */         }
/*      */       }
/*      */       
/*      */ 
/* 5194 */       this.log.logInfo(logMessage);
/*      */       
/* 5196 */       this.metricsLastReportedMs = System.currentTimeMillis();
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   private void reportMetricsIfNeeded()
/*      */   {
/* 5205 */     if ((getGatherPerformanceMetrics()) && 
/* 5206 */       (System.currentTimeMillis() - this.metricsLastReportedMs > getReportMetricsIntervalMillis())) {
/* 5207 */       reportMetrics();
/*      */     }
/*      */   }
/*      */   
/*      */   protected void reportNumberOfTablesAccessed(int numTablesAccessed)
/*      */   {
/* 5213 */     if (numTablesAccessed < this.minimumNumberTablesAccessed) {
/* 5214 */       this.minimumNumberTablesAccessed = numTablesAccessed;
/*      */     }
/*      */     
/* 5217 */     if (numTablesAccessed > this.maximumNumberTablesAccessed) {
/* 5218 */       this.maximumNumberTablesAccessed = numTablesAccessed;
/*      */       
/* 5220 */       repartitionTablesAccessedHistogram();
/*      */     }
/*      */     
/* 5223 */     addToTablesAccessedHistogram(numTablesAccessed, 1);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void resetServerState()
/*      */     throws SQLException
/*      */   {
/* 5235 */     if ((!getParanoid()) && (this.io != null) && (versionMeetsMinimum(4, 0, 6)))
/*      */     {
/* 5237 */       changeUser(this.user, this.password);
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void rollback()
/*      */     throws SQLException
/*      */   {
/* 5251 */     synchronized (getMutex()) {
/* 5252 */       checkClosed();
/*      */       
/*      */       try
/*      */       {
/* 5256 */         if ((this.autoCommit) && (!getRelaxAutoCommit())) {
/* 5257 */           throw SQLError.createSQLException("Can't call rollback when autocommit=true", "08003");
/*      */         }
/*      */         
/* 5260 */         if (this.transactionsSupported) {
/*      */           try {
/* 5262 */             rollbackNoChecks();
/*      */           }
/*      */           catch (SQLException sqlEx) {
/* 5265 */             if ((getIgnoreNonTxTables()) && (sqlEx.getErrorCode() != 1196))
/*      */             {
/* 5267 */               throw sqlEx;
/*      */             }
/*      */           }
/*      */         }
/*      */       } catch (SQLException sqlException) {
/* 5272 */         if ("08S01".equals(sqlException.getSQLState()))
/*      */         {
/* 5274 */           throw SQLError.createSQLException("Communications link failure during rollback(). Transaction resolution unknown.", "08007");
/*      */         }
/*      */         
/*      */ 
/*      */ 
/* 5279 */         throw sqlException;
/*      */       } finally {
/* 5281 */         this.needsPing = getReconnectAtTxEnd();
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public void rollback(Savepoint savepoint)
/*      */     throws SQLException
/*      */   {
/* 5291 */     if ((versionMeetsMinimum(4, 0, 14)) || (versionMeetsMinimum(4, 1, 1))) {
/* 5292 */       synchronized (getMutex()) {
/* 5293 */         checkClosed();
/*      */         try
/*      */         {
/* 5296 */           StringBuffer rollbackQuery = new StringBuffer("ROLLBACK TO SAVEPOINT ");
/*      */           
/* 5298 */           rollbackQuery.append('`');
/* 5299 */           rollbackQuery.append(savepoint.getSavepointName());
/* 5300 */           rollbackQuery.append('`');
/*      */           
/* 5302 */           java.sql.Statement stmt = null;
/*      */           try
/*      */           {
/* 5305 */             stmt = createStatement();
/*      */             
/* 5307 */             stmt.executeUpdate(rollbackQuery.toString());
/*      */           } catch (SQLException sqlEx) {
/* 5309 */             int errno = sqlEx.getErrorCode();
/*      */             
/* 5311 */             if (errno == 1181) {
/* 5312 */               String msg = sqlEx.getMessage();
/*      */               
/* 5314 */               if (msg != null) {
/* 5315 */                 int indexOfError153 = msg.indexOf("153");
/*      */                 
/* 5317 */                 if (indexOfError153 != -1) {
/* 5318 */                   throw SQLError.createSQLException("Savepoint '" + savepoint.getSavepointName() + "' does not exist", "S1009", errno);
/*      */                 }
/*      */               }
/*      */             }
/*      */             
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 5328 */             if ((getIgnoreNonTxTables()) && (sqlEx.getErrorCode() != 1196))
/*      */             {
/* 5330 */               throw sqlEx;
/*      */             }
/*      */             
/* 5333 */             if ("08S01".equals(sqlEx.getSQLState()))
/*      */             {
/* 5335 */               throw SQLError.createSQLException("Communications link failure during rollback(). Transaction resolution unknown.", "08007");
/*      */             }
/*      */             
/*      */ 
/*      */ 
/* 5340 */             throw sqlEx;
/*      */           } finally {
/* 5342 */             closeStatement(stmt);
/*      */           }
/*      */         } finally {
/* 5345 */           this.needsPing = getReconnectAtTxEnd();
/*      */         }
/*      */       }
/*      */     }
/* 5349 */     throw new NotImplemented();
/*      */   }
/*      */   
/*      */   private void rollbackNoChecks() throws SQLException
/*      */   {
/* 5354 */     if ((getUseLocalSessionState()) && (versionMeetsMinimum(5, 0, 0)) && 
/* 5355 */       (!this.io.inTransactionOnServer())) {
/* 5356 */       return;
/*      */     }
/*      */     
/*      */ 
/* 5360 */     execSQL(null, "rollback", -1, null, 1003, 1007, false, this.database, true, false);
/*      */   }
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
/*      */   public ServerPreparedStatement serverPrepare(String sql)
/*      */     throws SQLException
/*      */   {
/* 5378 */     String nativeSql = getProcessEscapeCodesForPrepStmts() ? nativeSQL(sql) : sql;
/*      */     
/* 5380 */     return new ServerPreparedStatement(this, nativeSql, getCatalog(), 1005, 1007);
/*      */   }
/*      */   
/*      */   protected boolean serverSupportsConvertFn()
/*      */     throws SQLException
/*      */   {
/* 5386 */     return versionMeetsMinimum(4, 0, 2);
/*      */   }
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
/*      */   public void setAutoCommit(boolean autoCommitFlag)
/*      */     throws SQLException
/*      */   {
/* 5412 */     synchronized (getMutex()) {
/* 5413 */       checkClosed();
/*      */       
/* 5415 */       if (getAutoReconnectForPools()) {
/* 5416 */         setHighAvailability(true);
/*      */       }
/*      */       try
/*      */       {
/* 5420 */         if (this.transactionsSupported)
/*      */         {
/* 5422 */           boolean needsSetOnServer = true;
/*      */           
/* 5424 */           if ((getUseLocalSessionState()) && (this.autoCommit == autoCommitFlag))
/*      */           {
/* 5426 */             needsSetOnServer = false;
/* 5427 */           } else if (!getHighAvailability()) {
/* 5428 */             needsSetOnServer = getIO().isSetNeededForAutoCommitMode(autoCommitFlag);
/*      */           }
/*      */           
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 5439 */           this.autoCommit = autoCommitFlag;
/*      */           
/* 5441 */           if (needsSetOnServer) {
/* 5442 */             execSQL(null, autoCommitFlag ? "SET autocommit=1" : "SET autocommit=0", -1, null, 1003, 1007, false, this.database, true, false);
/*      */           }
/*      */           
/*      */ 
/*      */         }
/*      */         else
/*      */         {
/*      */ 
/* 5450 */           if ((!autoCommitFlag) && (!getRelaxAutoCommit())) {
/* 5451 */             throw SQLError.createSQLException("MySQL Versions Older than 3.23.15 do not support transactions", "08003");
/*      */           }
/*      */           
/*      */ 
/*      */ 
/* 5456 */           this.autoCommit = autoCommitFlag;
/*      */         }
/*      */       } finally {
/* 5459 */         if (getAutoReconnectForPools()) {
/* 5460 */           setHighAvailability(false);
/*      */         }
/*      */       }
/*      */       
/* 5464 */       return;
/*      */     }
/*      */   }
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
/*      */   public void setCatalog(String catalog)
/*      */     throws SQLException
/*      */   {
/* 5482 */     synchronized (getMutex()) {
/* 5483 */       checkClosed();
/*      */       
/* 5485 */       if (catalog == null) {
/* 5486 */         throw SQLError.createSQLException("Catalog can not be null", "S1009");
/*      */       }
/*      */       
/*      */ 
/* 5490 */       if (getUseLocalSessionState()) {
/* 5491 */         if (this.lowerCaseTableNames) {
/* 5492 */           if (!this.database.equalsIgnoreCase(catalog)) {}
/*      */ 
/*      */ 
/*      */         }
/* 5496 */         else if (this.database.equals(catalog)) {
/* 5497 */           return;
/*      */         }
/*      */       }
/*      */       
/*      */ 
/* 5502 */       String quotedId = this.dbmd.getIdentifierQuoteString();
/*      */       
/* 5504 */       if ((quotedId == null) || (quotedId.equals(" "))) {
/* 5505 */         quotedId = "";
/*      */       }
/*      */       
/* 5508 */       StringBuffer query = new StringBuffer("USE ");
/* 5509 */       query.append(quotedId);
/* 5510 */       query.append(catalog);
/* 5511 */       query.append(quotedId);
/*      */       
/* 5513 */       execSQL(null, query.toString(), -1, null, 1003, 1007, false, this.database, true, false);
/*      */       
/*      */ 
/*      */ 
/*      */ 
/* 5518 */       this.database = catalog;
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public synchronized void setFailedOver(boolean flag)
/*      */   {
/* 5527 */     this.failedOver = flag;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   private void setFailedOverState()
/*      */     throws SQLException
/*      */   {
/* 5537 */     if (getFailOverReadOnly()) {
/* 5538 */       setReadOnlyInternal(true);
/*      */     }
/*      */     
/* 5541 */     this.queriesIssuedFailedOver = 0L;
/* 5542 */     this.failedOver = true;
/* 5543 */     this.masterFailTimeMillis = System.currentTimeMillis();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void setInGlobalTx(boolean flag)
/*      */   {
/* 5554 */     this.isInGlobalTx = flag;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void setPreferSlaveDuringFailover(boolean flag)
/*      */   {
/* 5563 */     this.preferSlaveDuringFailover = flag;
/*      */   }
/*      */   
/*      */   void setReadInfoMsgEnabled(boolean flag) {
/* 5567 */     this.readInfoMsg = flag;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void setReadOnly(boolean readOnlyFlag)
/*      */     throws SQLException
/*      */   {
/* 5581 */     checkClosed();
/*      */     
/*      */ 
/*      */ 
/* 5585 */     if ((this.failedOver) && (getFailOverReadOnly()) && (!readOnlyFlag)) {
/* 5586 */       return;
/*      */     }
/*      */     
/* 5589 */     setReadOnlyInternal(readOnlyFlag);
/*      */   }
/*      */   
/*      */   protected void setReadOnlyInternal(boolean readOnlyFlag) throws SQLException {
/* 5593 */     this.readOnly = readOnlyFlag;
/*      */   }
/*      */   
/*      */ 
/*      */   public Savepoint setSavepoint()
/*      */     throws SQLException
/*      */   {
/* 5600 */     MysqlSavepoint savepoint = new MysqlSavepoint();
/*      */     
/* 5602 */     setSavepoint(savepoint);
/*      */     
/* 5604 */     return savepoint;
/*      */   }
/*      */   
/*      */   private void setSavepoint(MysqlSavepoint savepoint) throws SQLException
/*      */   {
/* 5609 */     if ((versionMeetsMinimum(4, 0, 14)) || (versionMeetsMinimum(4, 1, 1))) {
/* 5610 */       synchronized (getMutex()) {
/* 5611 */         checkClosed();
/*      */         
/* 5613 */         StringBuffer savePointQuery = new StringBuffer("SAVEPOINT ");
/* 5614 */         savePointQuery.append('`');
/* 5615 */         savePointQuery.append(savepoint.getSavepointName());
/* 5616 */         savePointQuery.append('`');
/*      */         
/* 5618 */         java.sql.Statement stmt = null;
/*      */         try
/*      */         {
/* 5621 */           stmt = createStatement();
/*      */           
/* 5623 */           stmt.executeUpdate(savePointQuery.toString());
/*      */         } finally {
/* 5625 */           closeStatement(stmt);
/*      */         }
/*      */       }
/*      */     }
/* 5629 */     throw new NotImplemented();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public synchronized Savepoint setSavepoint(String name)
/*      */     throws SQLException
/*      */   {
/* 5637 */     MysqlSavepoint savepoint = new MysqlSavepoint(name);
/*      */     
/* 5639 */     setSavepoint(savepoint);
/*      */     
/* 5641 */     return savepoint;
/*      */   }
/*      */   
/*      */ 
/*      */   private void setSessionVariables()
/*      */     throws SQLException
/*      */   {
/* 5648 */     if ((versionMeetsMinimum(4, 0, 0)) && (getSessionVariables() != null)) {
/* 5649 */       List variablesToSet = StringUtils.split(getSessionVariables(), ",", "\"'", "\"'", false);
/*      */       
/*      */ 
/* 5652 */       int numVariablesToSet = variablesToSet.size();
/*      */       
/* 5654 */       java.sql.Statement stmt = null;
/*      */       try
/*      */       {
/* 5657 */         stmt = getMetadataSafeStatement();
/*      */         
/* 5659 */         for (int i = 0; i < numVariablesToSet; i++) {
/* 5660 */           String variableValuePair = (String)variablesToSet.get(i);
/*      */           
/* 5662 */           if (variableValuePair.startsWith("@")) {
/* 5663 */             stmt.executeUpdate("SET " + variableValuePair);
/*      */           } else {
/* 5665 */             stmt.executeUpdate("SET SESSION " + variableValuePair);
/*      */           }
/*      */         }
/*      */       } finally {
/* 5669 */         if (stmt != null) {
/* 5670 */           stmt.close();
/*      */         }
/*      */       }
/*      */     }
/*      */   }
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
/*      */   public synchronized void setTransactionIsolation(int level)
/*      */     throws SQLException
/*      */   {
/* 5700 */     checkClosed();
/*      */     
/* 5702 */     if (this.hasIsolationLevels) {
/* 5703 */       String sql = null;
/*      */       
/* 5705 */       boolean shouldSendSet = false;
/*      */       
/* 5707 */       if (getAlwaysSendSetIsolation()) {
/* 5708 */         shouldSendSet = true;
/*      */       }
/* 5710 */       else if (level != this.isolationLevel) {
/* 5711 */         shouldSendSet = true;
/*      */       }
/*      */       
/*      */ 
/* 5715 */       if (getUseLocalSessionState()) {
/* 5716 */         shouldSendSet = this.isolationLevel != level;
/*      */       }
/*      */       
/* 5719 */       if (shouldSendSet) {
/* 5720 */         switch (level) {
/*      */         case 0: 
/* 5722 */           throw SQLError.createSQLException("Transaction isolation level NONE not supported by MySQL");
/*      */         
/*      */ 
/*      */         case 2: 
/* 5726 */           sql = "SET SESSION TRANSACTION ISOLATION LEVEL READ COMMITTED";
/*      */           
/* 5728 */           break;
/*      */         
/*      */         case 1: 
/* 5731 */           sql = "SET SESSION TRANSACTION ISOLATION LEVEL READ UNCOMMITTED";
/*      */           
/* 5733 */           break;
/*      */         
/*      */         case 4: 
/* 5736 */           sql = "SET SESSION TRANSACTION ISOLATION LEVEL REPEATABLE READ";
/*      */           
/* 5738 */           break;
/*      */         
/*      */         case 8: 
/* 5741 */           sql = "SET SESSION TRANSACTION ISOLATION LEVEL SERIALIZABLE";
/*      */           
/* 5743 */           break;
/*      */         case 3: case 5: case 6: 
/*      */         case 7: default: 
/* 5746 */           throw SQLError.createSQLException("Unsupported transaction isolation level '" + level + "'", "S1C00");
/*      */         }
/*      */         
/*      */         
/*      */ 
/* 5751 */         execSQL(null, sql, -1, null, 1003, 1007, false, this.database, true, false);
/*      */         
/*      */ 
/*      */ 
/*      */ 
/* 5756 */         this.isolationLevel = level;
/*      */       }
/*      */     } else {
/* 5759 */       throw SQLError.createSQLException("Transaction Isolation Levels are not supported on MySQL versions older than 3.23.36.", "S1C00");
/*      */     }
/*      */   }
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
/*      */   public synchronized void setTypeMap(Map map)
/*      */     throws SQLException
/*      */   {
/* 5775 */     this.typeMap = map;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   private boolean shouldFallBack()
/*      */   {
/* 5786 */     long secondsSinceFailedOver = (System.currentTimeMillis() - this.masterFailTimeMillis) / 1000L;
/*      */     
/*      */ 
/* 5789 */     boolean tryFallback = (secondsSinceFailedOver >= getSecondsBeforeRetryMaster()) || (this.queriesIssuedFailedOver >= getQueriesBeforeRetryMaster());
/*      */     
/* 5791 */     return tryFallback;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public void shutdownServer()
/*      */     throws SQLException
/*      */   {
/*      */     try
/*      */     {
/* 5802 */       this.io.sendCommand(8, null, null, false, null);
/*      */     } catch (Exception ex) {
/* 5804 */       throw SQLError.createSQLException("Unhandled exception '" + ex.toString() + "'", "S1000");
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public boolean supportsIsolationLevel()
/*      */   {
/* 5815 */     return this.hasIsolationLevels;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public boolean supportsQuotedIdentifiers()
/*      */   {
/* 5824 */     return this.hasQuotedIdentifiers;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public boolean supportsTransactions()
/*      */   {
/* 5833 */     return this.transactionsSupported;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   void unregisterStatement(Statement stmt)
/*      */   {
/* 5843 */     if (this.openStatements != null) {
/* 5844 */       synchronized (this.openStatements) {
/* 5845 */         this.openStatements.remove(stmt);
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   void unsetMaxRows(Statement stmt)
/*      */     throws SQLException
/*      */   {
/* 5861 */     synchronized (this.mutex) {
/* 5862 */       if (this.statementsUsingMaxRows != null) {
/* 5863 */         Object found = this.statementsUsingMaxRows.remove(stmt);
/*      */         
/* 5865 */         if ((found != null) && (this.statementsUsingMaxRows.size() == 0))
/*      */         {
/* 5867 */           execSQL(null, "SET OPTION SQL_SELECT_LIMIT=DEFAULT", -1, null, 1003, 1007, false, this.database, true, false);
/*      */           
/*      */ 
/*      */ 
/*      */ 
/* 5872 */           this.maxRowsChanged = false;
/*      */         }
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */   boolean useAnsiQuotedIdentifiers() {
/* 5879 */     return this.useAnsiQuotes;
/*      */   }
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
/*      */   public boolean versionMeetsMinimum(int major, int minor, int subminor)
/*      */     throws SQLException
/*      */   {
/* 5895 */     checkClosed();
/*      */     
/* 5897 */     return this.io.versionMeetsMinimum(major, minor, subminor);
/*      */   }
/*      */   
/*      */   protected String getErrorMessageEncoding() {
/* 5901 */     return this.errorMessageEncoding;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/* 5907 */   private boolean hasTriedMasterFlag = false;
/*      */   
/*      */   public void clearHasTriedMaster() {
/* 5910 */     this.hasTriedMasterFlag = false;
/*      */   }
/*      */   
/*      */   public boolean hasTriedMaster() {
/* 5914 */     return this.hasTriedMasterFlag;
/*      */   }
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
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected void initializeResultsMetadataFromCache(String sql, CachedResultSetMetaData cachedMetaData, ResultSet resultSet)
/*      */     throws SQLException
/*      */   {
/* 5960 */     if (cachedMetaData == null)
/*      */     {
/*      */ 
/* 5963 */       cachedMetaData = new CachedResultSetMetaData();
/* 5964 */       cachedMetaData.fields = resultSet.fields;
/*      */       
/*      */ 
/*      */ 
/* 5968 */       resultSet.buildIndexMapping();
/* 5969 */       resultSet.initializeWithMetadata();
/*      */       
/* 5971 */       if ((resultSet instanceof UpdatableResultSet)) {
/* 5972 */         ((UpdatableResultSet)resultSet).checkUpdatability();
/*      */       }
/*      */       
/* 5975 */       cachedMetaData.columnNameToIndex = resultSet.columnNameToIndex;
/* 5976 */       cachedMetaData.fullColumnNameToIndex = resultSet.fullColumnNameToIndex;
/*      */       
/* 5978 */       cachedMetaData.metadata = resultSet.getMetaData();
/*      */       
/* 5980 */       this.resultSetMetadataCache.put(sql, cachedMetaData);
/*      */     }
/*      */     else {
/* 5983 */       resultSet.fields = cachedMetaData.fields;
/* 5984 */       resultSet.columnNameToIndex = cachedMetaData.columnNameToIndex;
/* 5985 */       resultSet.fullColumnNameToIndex = cachedMetaData.fullColumnNameToIndex;
/* 5986 */       resultSet.hasBuiltIndexMapping = true;
/* 5987 */       resultSet.initializeWithMetadata();
/*      */       
/* 5989 */       if ((resultSet instanceof UpdatableResultSet)) {
/* 5990 */         ((UpdatableResultSet)resultSet).checkUpdatability();
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */   public void clearWarnings()
/*      */     throws SQLException
/*      */   {}
/*      */   
/*      */   /* Error */
/*      */   public int getActiveStatementCount()
/*      */   {
/*      */     // Byte code:
/*      */     //   0: aload_0
/*      */     //   1: getfield 134	com/mysql/jdbc/Connection:openStatements	Ljava/util/Map;
/*      */     //   4: ifnull +27 -> 31
/*      */     //   7: aload_0
/*      */     //   8: getfield 134	com/mysql/jdbc/Connection:openStatements	Ljava/util/Map;
/*      */     //   11: dup
/*      */     //   12: astore_1
/*      */     //   13: monitorenter
/*      */     //   14: aload_0
/*      */     //   15: getfield 134	com/mysql/jdbc/Connection:openStatements	Ljava/util/Map;
/*      */     //   18: invokeinterface 302 1 0
/*      */     //   23: aload_1
/*      */     //   24: monitorexit
/*      */     //   25: ireturn
/*      */     //   26: astore_2
/*      */     //   27: aload_1
/*      */     //   28: monitorexit
/*      */     //   29: aload_2
/*      */     //   30: athrow
/*      */     //   31: iconst_0
/*      */     //   32: ireturn
/*      */     // Line number table:
/*      */     //   Java source line #3417	-> byte code offset #0
/*      */     //   Java source line #3418	-> byte code offset #7
/*      */     //   Java source line #3419	-> byte code offset #14
/*      */     //   Java source line #3420	-> byte code offset #26
/*      */     //   Java source line #3423	-> byte code offset #31
/*      */     // Local variable table:
/*      */     //   start	length	slot	name	signature
/*      */     //   0	33	0	this	Connection
/*      */     //   12	16	1	Ljava/lang/Object;	Object
/*      */     //   26	4	2	localObject1	Object
/*      */     // Exception table:
/*      */     //   from	to	target	type
/*      */     //   14	25	26	finally
/*      */     //   26	29	26	finally
/*      */   }
/*      */   
/*      */   public void releaseSavepoint(Savepoint arg0)
/*      */     throws SQLException
/*      */   {}
/*      */   
/*      */   public void setHoldability(int arg0)
/*      */     throws SQLException
/*      */   {}
/*      */   
/*      */   /* Error */
/*      */   boolean useMaxRows()
/*      */   {
/*      */     // Byte code:
/*      */     //   0: aload_0
/*      */     //   1: getfield 78	com/mysql/jdbc/Connection:mutex	Ljava/lang/Object;
/*      */     //   4: dup
/*      */     //   5: astore_1
/*      */     //   6: monitorenter
/*      */     //   7: aload_0
/*      */     //   8: getfield 73	com/mysql/jdbc/Connection:maxRowsChanged	Z
/*      */     //   11: aload_1
/*      */     //   12: monitorexit
/*      */     //   13: ireturn
/*      */     //   14: astore_2
/*      */     //   15: aload_1
/*      */     //   16: monitorexit
/*      */     //   17: aload_2
/*      */     //   18: athrow
/*      */     // Line number table:
/*      */     //   Java source line #5888	-> byte code offset #0
/*      */     //   Java source line #5889	-> byte code offset #7
/*      */     //   Java source line #5890	-> byte code offset #14
/*      */     // Local variable table:
/*      */     //   start	length	slot	name	signature
/*      */     //   0	19	0	this	Connection
/*      */     //   5	11	1	Ljava/lang/Object;	Object
/*      */     //   14	4	2	localObject1	Object
/*      */     // Exception table:
/*      */     //   from	to	target	type
/*      */     //   7	13	14	finally
/*      */     //   14	17	14	finally
/*      */   }
/*      */   
/*      */   /* Error */
/*      */   protected CachedResultSetMetaData getCachedMetaData(String sql)
/*      */   {
/*      */     // Byte code:
/*      */     //   0: aload_0
/*      */     //   1: getfield 529	com/mysql/jdbc/Connection:resultSetMetadataCache	Lcom/mysql/jdbc/util/LRUCache;
/*      */     //   4: ifnull +29 -> 33
/*      */     //   7: aload_0
/*      */     //   8: getfield 529	com/mysql/jdbc/Connection:resultSetMetadataCache	Lcom/mysql/jdbc/util/LRUCache;
/*      */     //   11: dup
/*      */     //   12: astore_2
/*      */     //   13: monitorenter
/*      */     //   14: aload_0
/*      */     //   15: getfield 529	com/mysql/jdbc/Connection:resultSetMetadataCache	Lcom/mysql/jdbc/util/LRUCache;
/*      */     //   18: aload_1
/*      */     //   19: invokevirtual 218	com/mysql/jdbc/util/LRUCache:get	(Ljava/lang/Object;)Ljava/lang/Object;
/*      */     //   22: checkcast 716	com/mysql/jdbc/CachedResultSetMetaData
/*      */     //   25: aload_2
/*      */     //   26: monitorexit
/*      */     //   27: areturn
/*      */     //   28: astore_3
/*      */     //   29: aload_2
/*      */     //   30: monitorexit
/*      */     //   31: aload_3
/*      */     //   32: athrow
/*      */     //   33: aconst_null
/*      */     //   34: areturn
/*      */     // Line number table:
/*      */     //   Java source line #5932	-> byte code offset #0
/*      */     //   Java source line #5933	-> byte code offset #7
/*      */     //   Java source line #5934	-> byte code offset #14
/*      */     //   Java source line #5936	-> byte code offset #28
/*      */     //   Java source line #5939	-> byte code offset #33
/*      */     // Local variable table:
/*      */     //   start	length	slot	name	signature
/*      */     //   0	35	0	this	Connection
/*      */     //   0	35	1	sql	String
/*      */     //   12	18	2	Ljava/lang/Object;	Object
/*      */     //   28	4	3	localObject1	Object
/*      */     // Exception table:
/*      */     //   from	to	target	type
/*      */     //   14	27	28	finally
/*      */     //   28	31	28	finally
/*      */   }
/*      */ }


/* Location:              E:\java\java学习\hutubill\lib\all.jar!\com\mysql\jdbc\Connection.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version:       0.7.1
 */
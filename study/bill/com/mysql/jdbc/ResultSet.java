/*      */ package com.mysql.jdbc;
/*      */ 
/*      */ import com.mysql.jdbc.profiler.ProfileEventSink;
/*      */ import com.mysql.jdbc.profiler.ProfilerEvent;
/*      */ import java.io.ByteArrayInputStream;
/*      */ import java.io.IOException;
/*      */ import java.io.InputStream;
/*      */ import java.io.ObjectInputStream;
/*      */ import java.io.Reader;
/*      */ import java.io.StringReader;
/*      */ import java.io.UnsupportedEncodingException;
/*      */ import java.math.BigDecimal;
/*      */ import java.math.BigInteger;
/*      */ import java.net.MalformedURLException;
/*      */ import java.net.URL;
/*      */ import java.sql.Array;
/*      */ import java.sql.Date;
/*      */ import java.sql.Ref;
/*      */ import java.sql.SQLException;
/*      */ import java.sql.SQLWarning;
/*      */ import java.sql.Time;
/*      */ import java.sql.Timestamp;
/*      */ import java.util.Calendar;
/*      */ import java.util.GregorianCalendar;
/*      */ import java.util.HashMap;
/*      */ import java.util.Locale;
/*      */ import java.util.Map;
/*      */ import java.util.StringTokenizer;
/*      */ import java.util.TimeZone;
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
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ public class ResultSet
/*      */   implements java.sql.ResultSet
/*      */ {
/*  124 */   protected static final double MIN_DIFF_PREC = Float.parseFloat(Float.toString(Float.MIN_VALUE)) - Double.parseDouble(Float.toString(Float.MIN_VALUE));
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  130 */   protected static final double MAX_DIFF_PREC = Float.parseFloat(Float.toString(Float.MAX_VALUE)) - Double.parseDouble(Float.toString(Float.MAX_VALUE));
/*      */   
/*      */ 
/*      */ 
/*  134 */   protected static int resultCounter = 1;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   protected static BigInteger convertLongToUlong(long longVal)
/*      */   {
/*  141 */     byte[] asBytes = new byte[8];
/*  142 */     asBytes[7] = ((byte)(int)(longVal & 0xFF));
/*  143 */     asBytes[6] = ((byte)(int)(longVal >>> 8));
/*  144 */     asBytes[5] = ((byte)(int)(longVal >>> 16));
/*  145 */     asBytes[4] = ((byte)(int)(longVal >>> 24));
/*  146 */     asBytes[3] = ((byte)(int)(longVal >>> 32));
/*  147 */     asBytes[2] = ((byte)(int)(longVal >>> 40));
/*  148 */     asBytes[1] = ((byte)(int)(longVal >>> 48));
/*  149 */     asBytes[0] = ((byte)(int)(longVal >>> 56));
/*      */     
/*  151 */     return new BigInteger(1, asBytes);
/*      */   }
/*      */   
/*      */ 
/*  155 */   protected String catalog = null;
/*      */   
/*      */ 
/*  158 */   protected Map columnNameToIndex = null;
/*      */   
/*      */ 
/*  161 */   protected boolean[] columnUsed = null;
/*      */   
/*      */ 
/*      */   protected Connection connection;
/*      */   
/*      */ 
/*  167 */   protected long connectionId = 0L;
/*      */   
/*      */ 
/*  170 */   protected int currentRow = -1;
/*      */   
/*      */ 
/*      */   private TimeZone defaultTimeZone;
/*      */   
/*  175 */   protected boolean doingUpdates = false;
/*      */   
/*  177 */   protected ProfileEventSink eventSink = null;
/*      */   
/*  179 */   private Calendar fastDateCal = null;
/*      */   
/*      */ 
/*  182 */   protected int fetchDirection = 1000;
/*      */   
/*      */ 
/*  185 */   protected int fetchSize = 0;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   protected Field[] fields;
/*      */   
/*      */ 
/*      */ 
/*      */   protected char firstCharOfQuery;
/*      */   
/*      */ 
/*      */ 
/*  198 */   protected Map fullColumnNameToIndex = null;
/*      */   
/*  200 */   protected boolean hasBuiltIndexMapping = false;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  206 */   protected boolean isBinaryEncoded = false;
/*      */   
/*      */ 
/*  209 */   protected boolean isClosed = false;
/*      */   
/*  211 */   protected ResultSet nextResultSet = null;
/*      */   
/*      */ 
/*  214 */   protected boolean onInsertRow = false;
/*      */   
/*      */ 
/*      */ 
/*      */   protected Statement owningStatement;
/*      */   
/*      */ 
/*      */ 
/*      */   protected Throwable pointOfOrigin;
/*      */   
/*      */ 
/*  225 */   protected boolean profileSql = false;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  231 */   protected boolean reallyResult = false;
/*      */   
/*      */ 
/*      */   protected int resultId;
/*      */   
/*      */ 
/*  237 */   protected int resultSetConcurrency = 0;
/*      */   
/*      */ 
/*  240 */   protected int resultSetType = 0;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   protected RowData rowData;
/*      */   
/*      */ 
/*      */ 
/*  249 */   protected String serverInfo = null;
/*      */   
/*      */ 
/*      */   private PreparedStatement statementUsedForFetchingRows;
/*      */   
/*  254 */   protected Object[] thisRow = null;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected long updateCount;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  268 */   protected long updateId = -1L;
/*      */   
/*  270 */   private boolean useStrictFloatingPoint = false;
/*      */   
/*  272 */   protected boolean useUsageAdvisor = false;
/*      */   
/*      */ 
/*  275 */   protected SQLWarning warningChain = null;
/*      */   
/*      */ 
/*  278 */   protected boolean wasNullFlag = false;
/*      */   
/*      */   protected java.sql.Statement wrapperStatement;
/*      */   
/*      */   protected boolean retainOwningStatement;
/*      */   
/*  284 */   protected Calendar gmtCalendar = null;
/*      */   
/*  286 */   protected boolean useFastDateParsing = false;
/*      */   
/*  288 */   private boolean padCharsWithSpace = false;
/*      */   
/*  290 */   protected static final char[] EMPTY_SPACE = new char['ÿ'];
/*      */   
/*      */   static {
/*  293 */     for (int i = 0; i < EMPTY_SPACE.length; i++) {
/*  294 */       EMPTY_SPACE[i] = ' ';
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
/*      */   public ResultSet(long updateCount, long updateID, Connection conn, Statement creatorStmt)
/*      */   {
/*  312 */     this.updateCount = updateCount;
/*  313 */     this.updateId = updateID;
/*  314 */     this.reallyResult = false;
/*  315 */     this.fields = new Field[0];
/*      */     
/*  317 */     this.connection = conn;
/*  318 */     this.owningStatement = creatorStmt;
/*      */     
/*  320 */     this.retainOwningStatement = false;
/*      */     
/*  322 */     if (this.connection != null) {
/*  323 */       this.retainOwningStatement = this.connection.getRetainStatementAfterResultSetClose();
/*      */       
/*      */ 
/*  326 */       this.connectionId = this.connection.getId();
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
/*      */   public ResultSet(String catalog, Field[] fields, RowData tuples, Connection conn, Statement creatorStmt)
/*      */     throws SQLException
/*      */   {
/*  349 */     this.connection = conn;
/*      */     
/*  351 */     if (this.connection != null) {
/*  352 */       this.useStrictFloatingPoint = this.connection.getStrictFloatingPoint();
/*      */       
/*  354 */       setDefaultTimeZone(this.connection.getDefaultTimeZone());
/*  355 */       this.connectionId = this.connection.getId();
/*  356 */       this.useFastDateParsing = this.connection.getUseFastDateParsing();
/*  357 */       this.padCharsWithSpace = this.connection.getPadCharsWithSpace();
/*      */     }
/*      */     
/*  360 */     this.owningStatement = creatorStmt;
/*      */     
/*  362 */     this.catalog = catalog;
/*  363 */     this.profileSql = this.connection.getProfileSql();
/*      */     
/*  365 */     this.fields = fields;
/*  366 */     this.rowData = tuples;
/*  367 */     this.updateCount = this.rowData.size();
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  374 */     this.reallyResult = true;
/*      */     
/*      */ 
/*  377 */     if (this.rowData.size() > 0) {
/*  378 */       if ((this.updateCount == 1L) && 
/*  379 */         (this.thisRow == null)) {
/*  380 */         this.rowData.close();
/*  381 */         this.updateCount = -1L;
/*      */       }
/*      */     }
/*      */     else {
/*  385 */       this.thisRow = null;
/*      */     }
/*      */     
/*  388 */     this.rowData.setOwner(this);
/*      */     
/*  390 */     if (this.fields != null) {
/*  391 */       initializeWithMetadata();
/*      */     }
/*      */     
/*  394 */     this.retainOwningStatement = false;
/*      */     
/*  396 */     if (this.connection != null) {
/*  397 */       this.retainOwningStatement = this.connection.getRetainStatementAfterResultSetClose();
/*      */     }
/*      */   }
/*      */   
/*      */   protected void initializeWithMetadata() throws SQLException
/*      */   {
/*  403 */     if ((this.profileSql) || (this.connection.getUseUsageAdvisor())) {
/*  404 */       this.columnUsed = new boolean[this.fields.length];
/*  405 */       this.pointOfOrigin = new Throwable();
/*  406 */       this.resultId = (resultCounter++);
/*  407 */       this.useUsageAdvisor = this.connection.getUseUsageAdvisor();
/*  408 */       this.eventSink = ProfileEventSink.getInstance(this.connection);
/*      */     }
/*      */     
/*  411 */     if (this.connection.getGatherPerformanceMetrics()) {
/*  412 */       this.connection.incrementNumberOfResultSetsCreated();
/*      */       
/*  414 */       Map tableNamesMap = new HashMap();
/*      */       
/*  416 */       for (int i = 0; i < this.fields.length; i++) {
/*  417 */         Field f = this.fields[i];
/*      */         
/*  419 */         String tableName = f.getOriginalTableName();
/*      */         
/*  421 */         if (tableName == null) {
/*  422 */           tableName = f.getTableName();
/*      */         }
/*      */         
/*  425 */         if (tableName != null) {
/*  426 */           if (this.connection.lowerCaseTableNames()) {
/*  427 */             tableName = tableName.toLowerCase();
/*      */           }
/*      */           
/*      */ 
/*  431 */           tableNamesMap.put(tableName, null);
/*      */         }
/*      */       }
/*      */       
/*  435 */       this.connection.reportNumberOfTablesAccessed(tableNamesMap.size());
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */   private synchronized void createCalendarIfNeeded()
/*      */   {
/*  442 */     if (this.fastDateCal == null) {
/*  443 */       this.fastDateCal = new GregorianCalendar(Locale.US);
/*  444 */       this.fastDateCal.setTimeZone(getDefaultTimeZone());
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
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public boolean absolute(int row)
/*      */     throws SQLException
/*      */   {
/*  487 */     checkClosed();
/*      */     
/*      */     boolean b;
/*      */     boolean b;
/*  491 */     if (this.rowData.size() == 0) {
/*  492 */       b = false;
/*      */     } else {
/*  494 */       if (row == 0) {
/*  495 */         throw SQLError.createSQLException(Messages.getString("ResultSet.Cannot_absolute_position_to_row_0_110"), "S1009");
/*      */       }
/*      */       
/*      */ 
/*      */ 
/*      */ 
/*  501 */       if (this.onInsertRow) {
/*  502 */         this.onInsertRow = false;
/*      */       }
/*      */       
/*  505 */       if (this.doingUpdates) {
/*  506 */         this.doingUpdates = false;
/*      */       }
/*      */       boolean b;
/*  509 */       if (row == 1) {
/*  510 */         b = first(); } else { boolean b;
/*  511 */         if (row == -1) {
/*  512 */           b = last(); } else { boolean b;
/*  513 */           if (row > this.rowData.size()) {
/*  514 */             afterLast();
/*  515 */             b = false;
/*      */           } else { boolean b;
/*  517 */             if (row < 0)
/*      */             {
/*  519 */               int newRowPosition = this.rowData.size() + row + 1;
/*      */               boolean b;
/*  521 */               if (newRowPosition <= 0) {
/*  522 */                 beforeFirst();
/*  523 */                 b = false;
/*      */               } else {
/*  525 */                 b = absolute(newRowPosition);
/*      */               }
/*      */             } else {
/*  528 */               row--;
/*  529 */               this.rowData.setCurrentRow(row);
/*  530 */               this.thisRow = this.rowData.getAt(row);
/*  531 */               b = true;
/*      */             }
/*      */           }
/*      */         }
/*      */       } }
/*  536 */     return b;
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
/*      */   public void afterLast()
/*      */     throws SQLException
/*      */   {
/*  552 */     checkClosed();
/*      */     
/*  554 */     if (this.onInsertRow) {
/*  555 */       this.onInsertRow = false;
/*      */     }
/*      */     
/*  558 */     if (this.doingUpdates) {
/*  559 */       this.doingUpdates = false;
/*      */     }
/*      */     
/*  562 */     if (this.rowData.size() != 0) {
/*  563 */       this.rowData.afterLast();
/*  564 */       this.thisRow = null;
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
/*      */   public void beforeFirst()
/*      */     throws SQLException
/*      */   {
/*  581 */     checkClosed();
/*      */     
/*  583 */     if (this.onInsertRow) {
/*  584 */       this.onInsertRow = false;
/*      */     }
/*      */     
/*  587 */     if (this.doingUpdates) {
/*  588 */       this.doingUpdates = false;
/*      */     }
/*      */     
/*  591 */     if (this.rowData.size() == 0) {
/*  592 */       return;
/*      */     }
/*      */     
/*  595 */     this.rowData.beforeFirst();
/*  596 */     this.thisRow = null;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected void buildIndexMapping()
/*      */     throws SQLException
/*      */   {
/*  607 */     int numFields = this.fields.length;
/*  608 */     this.columnNameToIndex = new TreeMap(String.CASE_INSENSITIVE_ORDER);
/*  609 */     this.fullColumnNameToIndex = new TreeMap(String.CASE_INSENSITIVE_ORDER);
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  624 */     for (int i = numFields - 1; i >= 0; i--) {
/*  625 */       Integer index = new Integer(i);
/*  626 */       String columnName = this.fields[i].getName();
/*  627 */       String fullColumnName = this.fields[i].getFullName();
/*      */       
/*  629 */       if (columnName != null) {
/*  630 */         this.columnNameToIndex.put(columnName, index);
/*      */       }
/*      */       
/*  633 */       if (fullColumnName != null) {
/*  634 */         this.fullColumnNameToIndex.put(fullColumnName, index);
/*      */       }
/*      */     }
/*      */     
/*      */ 
/*  639 */     this.hasBuiltIndexMapping = true;
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
/*      */   public void cancelRowUpdates()
/*      */     throws SQLException
/*      */   {
/*  655 */     throw new NotUpdatable();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected final void checkClosed()
/*      */     throws SQLException
/*      */   {
/*  665 */     if (this.isClosed) {
/*  666 */       throw SQLError.createSQLException(Messages.getString("ResultSet.Operation_not_allowed_after_ResultSet_closed_144"), "S1000");
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
/*      */   protected final void checkColumnBounds(int columnIndex)
/*      */     throws SQLException
/*      */   {
/*  683 */     if (columnIndex < 1) {
/*  684 */       throw SQLError.createSQLException(Messages.getString("ResultSet.Column_Index_out_of_range_low", new Object[] { new Integer(columnIndex), new Integer(this.fields.length) }), "S1009");
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*  689 */     if (columnIndex > this.fields.length) {
/*  690 */       throw SQLError.createSQLException(Messages.getString("ResultSet.Column_Index_out_of_range_high", new Object[] { new Integer(columnIndex), new Integer(this.fields.length) }), "S1009");
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  697 */     if ((this.profileSql) || (this.useUsageAdvisor)) {
/*  698 */       this.columnUsed[(columnIndex - 1)] = true;
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected void checkRowPos()
/*      */     throws SQLException
/*      */   {
/*  710 */     checkClosed();
/*      */     
/*  712 */     if ((!this.rowData.isDynamic()) && (this.rowData.size() == 0)) {
/*  713 */       throw SQLError.createSQLException(Messages.getString("ResultSet.Illegal_operation_on_empty_result_set"), "S1000");
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*  719 */     if (this.rowData.isBeforeFirst()) {
/*  720 */       throw SQLError.createSQLException(Messages.getString("ResultSet.Before_start_of_result_set_146"), "S1000");
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*  725 */     if (this.rowData.isAfterLast()) {
/*  726 */       throw SQLError.createSQLException(Messages.getString("ResultSet.After_end_of_result_set_148"), "S1000");
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected void clearNextResult()
/*      */   {
/*  737 */     this.nextResultSet = null;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void clearWarnings()
/*      */     throws SQLException
/*      */   {
/*  748 */     this.warningChain = null;
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
/*      */   public void close()
/*      */     throws SQLException
/*      */   {
/*  769 */     realClose(true);
/*      */   }
/*      */   
/*      */ 
/*      */   private int convertToZeroWithEmptyCheck()
/*      */     throws SQLException
/*      */   {
/*  776 */     if (this.connection.getEmptyStringsConvertToZero()) {
/*  777 */       return 0;
/*      */     }
/*      */     
/*  780 */     throw SQLError.createSQLException("Can't convert empty string ('') to numeric", "22018");
/*      */   }
/*      */   
/*      */ 
/*      */   private String convertToZeroLiteralStringWithEmptyCheck()
/*      */     throws SQLException
/*      */   {
/*  787 */     if (this.connection.getEmptyStringsConvertToZero()) {
/*  788 */       return "0";
/*      */     }
/*      */     
/*  791 */     throw SQLError.createSQLException("Can't convert empty string ('') to numeric", "22018");
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   protected final ResultSet copy()
/*      */     throws SQLException
/*      */   {
/*  799 */     ResultSet rs = new ResultSet(this.catalog, this.fields, this.rowData, this.connection, this.owningStatement);
/*      */     
/*      */ 
/*  802 */     return rs;
/*      */   }
/*      */   
/*      */   protected void redefineFieldsForDBMD(Field[] f) {
/*  806 */     this.fields = f;
/*      */     
/*  808 */     for (int i = 0; i < this.fields.length; i++) {
/*  809 */       this.fields[i].setUseOldNameMetadata(true);
/*  810 */       this.fields[i].setConnection(this.connection);
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
/*      */   public void deleteRow()
/*      */     throws SQLException
/*      */   {
/*  825 */     throw new NotUpdatable();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   private String extractStringFromNativeColumn(int columnIndex, int mysqlType)
/*      */     throws SQLException
/*      */   {
/*  837 */     int columnIndexMinusOne = columnIndex - 1;
/*      */     
/*  839 */     this.wasNullFlag = false;
/*      */     
/*  841 */     if ((this.thisRow[columnIndexMinusOne] instanceof String)) {
/*  842 */       return (String)this.thisRow[columnIndexMinusOne];
/*      */     }
/*      */     
/*  845 */     if (this.thisRow[columnIndexMinusOne] == null) {
/*  846 */       this.wasNullFlag = true;
/*      */       
/*  848 */       return null;
/*      */     }
/*      */     
/*  851 */     this.wasNullFlag = false;
/*      */     
/*  853 */     String stringVal = null;
/*      */     
/*  855 */     if ((this.connection != null) && (this.connection.getUseUnicode())) {
/*      */       try {
/*  857 */         String encoding = this.fields[columnIndexMinusOne].getCharacterSet();
/*      */         
/*      */ 
/*  860 */         if (encoding == null) {
/*  861 */           stringVal = new String((byte[])this.thisRow[columnIndexMinusOne]);
/*      */         }
/*      */         else {
/*  864 */           SingleByteCharsetConverter converter = this.connection.getCharsetConverter(encoding);
/*      */           
/*      */ 
/*  867 */           if (converter != null) {
/*  868 */             stringVal = converter.toString((byte[])this.thisRow[columnIndexMinusOne]);
/*      */           }
/*      */           else {
/*  871 */             stringVal = new String((byte[])this.thisRow[columnIndexMinusOne], encoding);
/*      */           }
/*      */         }
/*      */       }
/*      */       catch (UnsupportedEncodingException E)
/*      */       {
/*  877 */         throw SQLError.createSQLException(Messages.getString("ResultSet.Unsupported_character_encoding____138") + this.connection.getEncoding() + "'.", "0S100");
/*      */       }
/*      */       
/*      */     }
/*      */     else
/*      */     {
/*  883 */       stringVal = StringUtils.toAsciiString((byte[])this.thisRow[columnIndexMinusOne]);
/*      */     }
/*      */     
/*      */ 
/*  887 */     return stringVal;
/*      */   }
/*      */   
/*      */   private synchronized Date fastDateCreate(Calendar cal, int year, int month, int day)
/*      */   {
/*  892 */     if (cal == null) {
/*  893 */       createCalendarIfNeeded();
/*  894 */       cal = this.fastDateCal;
/*      */     }
/*      */     
/*  897 */     boolean useGmtMillis = this.connection.getUseGmtMillisForDatetimes();
/*      */     
/*  899 */     return TimeUtil.fastDateCreate(useGmtMillis, useGmtMillis ? getGmtCalendar() : null, cal, year, month, day);
/*      */   }
/*      */   
/*      */ 
/*      */   private synchronized Time fastTimeCreate(Calendar cal, int hour, int minute, int second)
/*      */     throws SQLException
/*      */   {
/*  906 */     if (cal == null) {
/*  907 */       createCalendarIfNeeded();
/*  908 */       cal = this.fastDateCal;
/*      */     }
/*      */     
/*  911 */     return TimeUtil.fastTimeCreate(cal, hour, minute, second);
/*      */   }
/*      */   
/*      */ 
/*      */   private synchronized Timestamp fastTimestampCreate(Calendar cal, int year, int month, int day, int hour, int minute, int seconds, int secondsPart)
/*      */   {
/*  917 */     if (cal == null) {
/*  918 */       createCalendarIfNeeded();
/*  919 */       cal = this.fastDateCal;
/*      */     }
/*      */     
/*  922 */     boolean useGmtMillis = this.connection.getUseGmtMillisForDatetimes();
/*      */     
/*  924 */     return TimeUtil.fastTimestampCreate(useGmtMillis, useGmtMillis ? getGmtCalendar() : null, cal, year, month, day, hour, minute, seconds, secondsPart);
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
/*      */   public synchronized int findColumn(String columnName)
/*      */     throws SQLException
/*      */   {
/*  962 */     if (!this.hasBuiltIndexMapping) {
/*  963 */       buildIndexMapping();
/*      */     }
/*      */     
/*  966 */     Integer index = (Integer)this.columnNameToIndex.get(columnName);
/*      */     
/*  968 */     if (index == null) {
/*  969 */       index = (Integer)this.fullColumnNameToIndex.get(columnName);
/*      */     }
/*      */     
/*  972 */     if (index != null) {
/*  973 */       return index.intValue() + 1;
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*  978 */     for (int i = 0; i < this.fields.length; i++) {
/*  979 */       if (this.fields[i].getName().equalsIgnoreCase(columnName))
/*  980 */         return i + 1;
/*  981 */       if (this.fields[i].getFullName().equalsIgnoreCase(columnName))
/*      */       {
/*  983 */         return i + 1;
/*      */       }
/*      */     }
/*      */     
/*  987 */     throw SQLError.createSQLException(Messages.getString("ResultSet.Column____112") + columnName + Messages.getString("ResultSet.___not_found._113"), "S0022");
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
/*      */   public boolean first()
/*      */     throws SQLException
/*      */   {
/* 1007 */     checkClosed();
/*      */     
/* 1009 */     if (this.rowData.isEmpty()) {
/* 1010 */       return false;
/*      */     }
/*      */     
/* 1013 */     if (this.onInsertRow) {
/* 1014 */       this.onInsertRow = false;
/*      */     }
/*      */     
/* 1017 */     if (this.doingUpdates) {
/* 1018 */       this.doingUpdates = false;
/*      */     }
/*      */     
/* 1021 */     this.rowData.beforeFirst();
/* 1022 */     this.thisRow = this.rowData.next();
/*      */     
/* 1024 */     return true;
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
/*      */   public Array getArray(int i)
/*      */     throws SQLException
/*      */   {
/* 1041 */     checkColumnBounds(i);
/*      */     
/* 1043 */     throw new NotImplemented();
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
/*      */   public Array getArray(String colName)
/*      */     throws SQLException
/*      */   {
/* 1060 */     return getArray(findColumn(colName));
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
/*      */   public InputStream getAsciiStream(int columnIndex)
/*      */     throws SQLException
/*      */   {
/* 1089 */     checkRowPos();
/*      */     
/* 1091 */     if (!this.isBinaryEncoded) {
/* 1092 */       return getBinaryStream(columnIndex);
/*      */     }
/*      */     
/* 1095 */     return getNativeBinaryStream(columnIndex);
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
/*      */   public InputStream getAsciiStream(String columnName)
/*      */     throws SQLException
/*      */   {
/* 1110 */     return getAsciiStream(findColumn(columnName));
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
/*      */   public BigDecimal getBigDecimal(int columnIndex)
/*      */     throws SQLException
/*      */   {
/* 1127 */     if (!this.isBinaryEncoded) {
/* 1128 */       String stringVal = getString(columnIndex);
/*      */       
/*      */ 
/* 1131 */       if (stringVal != null) {
/* 1132 */         if (stringVal.length() == 0)
/*      */         {
/* 1134 */           BigDecimal val = new BigDecimal(convertToZeroLiteralStringWithEmptyCheck());
/*      */           
/*      */ 
/* 1137 */           return val;
/*      */         }
/*      */         try
/*      */         {
/* 1141 */           return new BigDecimal(stringVal);
/*      */         }
/*      */         catch (NumberFormatException ex)
/*      */         {
/* 1145 */           throw SQLError.createSQLException(Messages.getString("ResultSet.Bad_format_for_BigDecimal", new Object[] { stringVal, new Integer(columnIndex) }), "S1009");
/*      */         }
/*      */       }
/*      */       
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 1153 */       return null;
/*      */     }
/*      */     
/* 1156 */     return getNativeBigDecimal(columnIndex);
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
/*      */   /**
/*      */    * @deprecated
/*      */    */
/*      */   public BigDecimal getBigDecimal(int columnIndex, int scale)
/*      */     throws SQLException
/*      */   {
/* 1177 */     if (!this.isBinaryEncoded) {
/* 1178 */       String stringVal = getString(columnIndex);
/*      */       
/*      */ 
/* 1181 */       if (stringVal != null) {
/* 1182 */         if (stringVal.length() == 0) {
/* 1183 */           BigDecimal val = new BigDecimal(convertToZeroLiteralStringWithEmptyCheck());
/*      */           
/*      */           try
/*      */           {
/* 1187 */             return val.setScale(scale);
/*      */           } catch (ArithmeticException ex) {
/*      */             try {
/* 1190 */               return val.setScale(scale, 4);
/*      */             }
/*      */             catch (ArithmeticException arEx) {
/* 1193 */               throw SQLError.createSQLException(Messages.getString("ResultSet.Bad_format_for_BigDecimal____124") + stringVal + Messages.getString("ResultSet.___in_column__125") + columnIndex + "(" + this.fields[(columnIndex - 1)] + ").", "S1009");
/*      */             }
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
/*      */ 
/*      */ 
/*      */         try
/*      */         {
/* 1209 */           val = new BigDecimal(stringVal); } catch (NumberFormatException ex) { BigDecimal val;
/*      */           BigDecimal val;
/* 1211 */           if (this.fields[(columnIndex - 1)].getMysqlType() == 16) {
/* 1212 */             long valueAsLong = getNumericRepresentationOfSQLBitType(columnIndex);
/*      */             
/* 1214 */             val = new BigDecimal(valueAsLong);
/*      */           } else {
/* 1216 */             throw SQLError.createSQLException(Messages.getString("ResultSet.Bad_format_for_BigDecimal", new Object[] { new Integer(columnIndex), stringVal }), "S1009");
/*      */           }
/*      */         }
/*      */         
/*      */ 
/*      */ 
/*      */ 
/*      */         try
/*      */         {
/* 1225 */           return val.setScale(scale);
/*      */         } catch (ArithmeticException ex) {
/*      */           try { BigDecimal val;
/* 1228 */             return val.setScale(scale, 4);
/*      */           } catch (ArithmeticException arithEx) {
/* 1230 */             throw SQLError.createSQLException(Messages.getString("ResultSet.Bad_format_for_BigDecimal", new Object[] { new Integer(columnIndex), stringVal }), "S1009");
/*      */           }
/*      */         }
/*      */       }
/*      */       
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 1239 */       return null;
/*      */     }
/*      */     
/* 1242 */     return getNativeBigDecimal(columnIndex, scale);
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
/*      */   public BigDecimal getBigDecimal(String columnName)
/*      */     throws SQLException
/*      */   {
/* 1258 */     return getBigDecimal(findColumn(columnName));
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
/*      */   /**
/*      */    * @deprecated
/*      */    */
/*      */   public BigDecimal getBigDecimal(String columnName, int scale)
/*      */     throws SQLException
/*      */   {
/* 1278 */     return getBigDecimal(findColumn(columnName), scale);
/*      */   }
/*      */   
/*      */ 
/*      */   private final BigDecimal getBigDecimalFromString(String stringVal, int columnIndex, int scale)
/*      */     throws SQLException
/*      */   {
/* 1285 */     if (stringVal != null) {
/* 1286 */       if (stringVal.length() == 0) {
/* 1287 */         BigDecimal bdVal = new BigDecimal(convertToZeroLiteralStringWithEmptyCheck());
/*      */         try
/*      */         {
/* 1290 */           return bdVal.setScale(scale);
/*      */         } catch (ArithmeticException ex) {
/*      */           try {
/* 1293 */             return bdVal.setScale(scale, 4);
/*      */           } catch (ArithmeticException arEx) {
/* 1295 */             throw new SQLException(Messages.getString("ResultSet.Bad_format_for_BigDecimal", new Object[] { stringVal, new Integer(columnIndex) }), "S1009");
/*      */           }
/*      */         }
/*      */       }
/*      */       
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       try
/*      */       {
/* 1306 */         return new BigDecimal(stringVal).setScale(scale);
/*      */       } catch (ArithmeticException ex) {
/*      */         try {
/* 1309 */           return new BigDecimal(stringVal).setScale(scale, 4);
/*      */         }
/*      */         catch (ArithmeticException arEx) {
/* 1312 */           throw new SQLException(Messages.getString("ResultSet.Bad_format_for_BigDecimal", new Object[] { stringVal, new Integer(columnIndex) }), "S1009");
/*      */         }
/*      */         
/*      */ 
/*      */       }
/*      */       catch (NumberFormatException ex)
/*      */       {
/*      */ 
/* 1320 */         if (this.fields[(columnIndex - 1)].getMysqlType() == 16) {
/* 1321 */           long valueAsLong = getNumericRepresentationOfSQLBitType(columnIndex);
/*      */           try
/*      */           {
/* 1324 */             return new BigDecimal(valueAsLong).setScale(scale);
/*      */           } catch (ArithmeticException arEx1) {
/*      */             try {
/* 1327 */               return new BigDecimal(valueAsLong).setScale(scale, 4);
/*      */             }
/*      */             catch (ArithmeticException arEx2) {
/* 1330 */               throw new SQLException(Messages.getString("ResultSet.Bad_format_for_BigDecimal", new Object[] { stringVal, new Integer(columnIndex) }), "S1009");
/*      */             }
/*      */           }
/*      */         }
/*      */         
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 1339 */         if ((this.fields[(columnIndex - 1)].getMysqlType() == 1) && (this.connection.getTinyInt1isBit()) && (this.fields[(columnIndex - 1)].getLength() == 1L))
/*      */         {
/* 1341 */           return new BigDecimal(stringVal.equalsIgnoreCase("true") ? 1.0D : 0.0D).setScale(scale);
/*      */         }
/*      */         
/* 1344 */         throw new SQLException(Messages.getString("ResultSet.Bad_format_for_BigDecimal", new Object[] { stringVal, new Integer(columnIndex) }), "S1009");
/*      */       }
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 1352 */     return null;
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
/*      */   public InputStream getBinaryStream(int columnIndex)
/*      */     throws SQLException
/*      */   {
/* 1373 */     checkRowPos();
/*      */     
/* 1375 */     if (!this.isBinaryEncoded) {
/* 1376 */       byte[] b = getBytes(columnIndex);
/*      */       
/* 1378 */       if (b != null) {
/* 1379 */         return new ByteArrayInputStream(b);
/*      */       }
/*      */       
/* 1382 */       return null;
/*      */     }
/*      */     
/* 1385 */     return getNativeBinaryStream(columnIndex);
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
/*      */   public InputStream getBinaryStream(String columnName)
/*      */     throws SQLException
/*      */   {
/* 1400 */     return getBinaryStream(findColumn(columnName));
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
/*      */   public java.sql.Blob getBlob(int columnIndex)
/*      */     throws SQLException
/*      */   {
/* 1415 */     if (!this.isBinaryEncoded) {
/* 1416 */       checkRowPos();
/*      */       
/* 1418 */       checkColumnBounds(columnIndex);
/*      */       
/* 1420 */       if (this.thisRow[(columnIndex - 1)] == null) {
/* 1421 */         this.wasNullFlag = true;
/*      */       } else {
/* 1423 */         this.wasNullFlag = false;
/*      */       }
/*      */       
/* 1426 */       if (this.wasNullFlag) {
/* 1427 */         return null;
/*      */       }
/*      */       
/* 1430 */       if (!this.connection.getEmulateLocators()) {
/* 1431 */         return new Blob((byte[])this.thisRow[(columnIndex - 1)]);
/*      */       }
/*      */       
/* 1434 */       return new BlobFromLocator(this, columnIndex);
/*      */     }
/*      */     
/* 1437 */     return getNativeBlob(columnIndex);
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
/*      */   public java.sql.Blob getBlob(String colName)
/*      */     throws SQLException
/*      */   {
/* 1452 */     return getBlob(findColumn(colName));
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
/*      */   public boolean getBoolean(int columnIndex)
/*      */     throws SQLException
/*      */   {
/* 1468 */     checkColumnBounds(columnIndex);
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 1475 */     int columnIndexMinusOne = columnIndex - 1;
/*      */     
/* 1477 */     Field field = this.fields[columnIndexMinusOne];
/*      */     
/* 1479 */     if (field.getMysqlType() == 16) {
/* 1480 */       return byteArrayToBoolean(columnIndexMinusOne);
/*      */     }
/*      */     
/* 1483 */     this.wasNullFlag = false;
/*      */     
/* 1485 */     int sqlType = field.getSQLType();
/*      */     
/* 1487 */     switch (sqlType) {
/*      */     case -7: 
/*      */     case -6: 
/*      */     case -5: 
/*      */     case 2: 
/*      */     case 3: 
/*      */     case 4: 
/*      */     case 5: 
/*      */     case 6: 
/*      */     case 7: 
/*      */     case 8: 
/*      */     case 16: 
/* 1499 */       long boolVal = getLong(columnIndex, false);
/*      */       
/* 1501 */       return (boolVal == -1L) || (boolVal > 0L);
/*      */     }
/* 1503 */     if (this.connection.getPedantic())
/*      */     {
/* 1505 */       switch (sqlType) {
/*      */       case -4: 
/*      */       case -3: 
/*      */       case -2: 
/*      */       case 70: 
/*      */       case 91: 
/*      */       case 92: 
/*      */       case 93: 
/*      */       case 2000: 
/*      */       case 2002: 
/*      */       case 2003: 
/*      */       case 2004: 
/*      */       case 2005: 
/*      */       case 2006: 
/* 1519 */         throw SQLError.createSQLException("Required type conversion not allowed", "22018");
/*      */       }
/*      */       
/*      */     }
/*      */     
/* 1524 */     if ((sqlType == -2) || (sqlType == -3) || (sqlType == -4) || (sqlType == 2004))
/*      */     {
/*      */ 
/*      */ 
/* 1528 */       return byteArrayToBoolean(columnIndexMinusOne);
/*      */     }
/*      */     
/* 1531 */     if (this.useUsageAdvisor) {
/* 1532 */       issueConversionViaParsingWarning("getBoolean()", columnIndex, this.thisRow[columnIndex], this.fields[columnIndex], new int[] { 16, 5, 1, 2, 3, 8, 4 });
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
/* 1544 */     String stringVal = getString(columnIndex);
/*      */     
/* 1546 */     return getBooleanFromString(stringVal, columnIndex);
/*      */   }
/*      */   
/*      */   private boolean byteArrayToBoolean(int columnIndexMinusOne)
/*      */   {
/* 1551 */     if (this.thisRow[columnIndexMinusOne] == null) {
/* 1552 */       this.wasNullFlag = true;
/*      */       
/* 1554 */       return false;
/*      */     }
/*      */     
/* 1557 */     this.wasNullFlag = false;
/*      */     
/* 1559 */     if (((byte[])this.thisRow[columnIndexMinusOne]).length == 0) {
/* 1560 */       return false;
/*      */     }
/*      */     
/* 1563 */     byte boolVal = ((byte[])this.thisRow[columnIndexMinusOne])[0];
/*      */     
/* 1565 */     return (boolVal == -1) || (boolVal > 0);
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
/*      */   public boolean getBoolean(String columnName)
/*      */     throws SQLException
/*      */   {
/* 1580 */     return getBoolean(findColumn(columnName));
/*      */   }
/*      */   
/*      */   private final boolean getBooleanFromString(String stringVal, int columnIndex) throws SQLException
/*      */   {
/* 1585 */     if ((stringVal != null) && (stringVal.length() > 0)) {
/* 1586 */       int c = Character.toLowerCase(stringVal.charAt(0));
/*      */       
/* 1588 */       return (c == 116) || (c == 121) || (c == 49) || (stringVal.equals("-1"));
/*      */     }
/*      */     
/*      */ 
/* 1592 */     return false;
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
/*      */   public byte getByte(int columnIndex)
/*      */     throws SQLException
/*      */   {
/* 1607 */     if (!this.isBinaryEncoded) {
/* 1608 */       String stringVal = getString(columnIndex);
/*      */       
/* 1610 */       if ((this.wasNullFlag) || (stringVal == null)) {
/* 1611 */         return 0;
/*      */       }
/*      */       
/* 1614 */       return getByteFromString(stringVal, columnIndex);
/*      */     }
/*      */     
/* 1617 */     return getNativeByte(columnIndex);
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
/*      */   public byte getByte(String columnName)
/*      */     throws SQLException
/*      */   {
/* 1632 */     return getByte(findColumn(columnName));
/*      */   }
/*      */   
/*      */   private final byte getByteFromString(String stringVal, int columnIndex)
/*      */     throws SQLException
/*      */   {
/* 1638 */     if ((stringVal != null) && (stringVal.length() == 0)) {
/* 1639 */       return (byte)convertToZeroWithEmptyCheck();
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
/* 1650 */     stringVal = stringVal.trim();
/*      */     try
/*      */     {
/* 1653 */       int decimalIndex = stringVal.indexOf(".");
/*      */       
/*      */ 
/* 1656 */       if (decimalIndex != -1) {
/* 1657 */         double valueAsDouble = Double.parseDouble(stringVal);
/*      */         
/* 1659 */         if ((this.connection.getJdbcCompliantTruncationForReads()) && (
/* 1660 */           (valueAsDouble < -128.0D) || (valueAsDouble > 127.0D)))
/*      */         {
/* 1662 */           throwRangeException(stringVal, columnIndex, -6);
/*      */         }
/*      */         
/*      */ 
/*      */ 
/* 1667 */         return (byte)(int)valueAsDouble;
/*      */       }
/*      */       
/* 1670 */       long valueAsLong = Long.parseLong(stringVal);
/*      */       
/* 1672 */       if ((this.connection.getJdbcCompliantTruncationForReads()) && (
/* 1673 */         (valueAsLong < -128L) || (valueAsLong > 127L)))
/*      */       {
/* 1675 */         throwRangeException(String.valueOf(valueAsLong), columnIndex, -6);
/*      */       }
/*      */       
/*      */ 
/*      */ 
/* 1680 */       return (byte)(int)valueAsLong;
/*      */     } catch (NumberFormatException NFE) {
/* 1682 */       throw SQLError.createSQLException(Messages.getString("ResultSet.Value____173") + stringVal + Messages.getString("ResultSet.___is_out_of_range_[-127,127]_174"), "S1009");
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
/*      */   public byte[] getBytes(int columnIndex)
/*      */     throws SQLException
/*      */   {
/* 1707 */     return getBytes(columnIndex, false);
/*      */   }
/*      */   
/*      */   protected byte[] getBytes(int columnIndex, boolean noConversion) throws SQLException
/*      */   {
/* 1712 */     if (!this.isBinaryEncoded) {
/* 1713 */       checkRowPos();
/*      */       
/* 1715 */       checkColumnBounds(columnIndex);
/*      */       
/* 1717 */       if (this.thisRow[(columnIndex - 1)] == null) {
/* 1718 */         this.wasNullFlag = true;
/*      */       } else {
/* 1720 */         this.wasNullFlag = false;
/*      */       }
/*      */       
/* 1723 */       if (this.wasNullFlag) {
/* 1724 */         return null;
/*      */       }
/*      */       
/* 1727 */       return (byte[])this.thisRow[(columnIndex - 1)];
/*      */     }
/*      */     
/* 1730 */     return getNativeBytes(columnIndex, noConversion);
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
/*      */   public byte[] getBytes(String columnName)
/*      */     throws SQLException
/*      */   {
/* 1745 */     return getBytes(findColumn(columnName));
/*      */   }
/*      */   
/*      */   private final byte[] getBytesFromString(String stringVal, int columnIndex) throws SQLException
/*      */   {
/* 1750 */     if (stringVal != null) {
/* 1751 */       return StringUtils.getBytes(stringVal, this.connection.getEncoding(), this.connection.getServerCharacterEncoding(), this.connection.parserKnowsUnicode(), this.connection);
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 1758 */     return null;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   private Calendar getCalendarInstanceForSessionOrNew()
/*      */   {
/* 1766 */     if (this.connection != null) {
/* 1767 */       return this.connection.getCalendarInstanceForSessionOrNew();
/*      */     }
/*      */     
/* 1770 */     return new GregorianCalendar();
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
/*      */   public Reader getCharacterStream(int columnIndex)
/*      */     throws SQLException
/*      */   {
/* 1791 */     if (!this.isBinaryEncoded) {
/* 1792 */       String asString = getStringForClob(columnIndex);
/*      */       
/* 1794 */       if (asString == null) {
/* 1795 */         return null;
/*      */       }
/*      */       
/* 1798 */       return new StringReader(asString);
/*      */     }
/*      */     
/* 1801 */     return getNativeCharacterStream(columnIndex);
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
/*      */   public Reader getCharacterStream(String columnName)
/*      */     throws SQLException
/*      */   {
/* 1821 */     return getCharacterStream(findColumn(columnName));
/*      */   }
/*      */   
/*      */   private final Reader getCharacterStreamFromString(String stringVal, int columnIndex) throws SQLException
/*      */   {
/* 1826 */     if (stringVal != null) {
/* 1827 */       return new StringReader(stringVal);
/*      */     }
/*      */     
/* 1830 */     return null;
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
/*      */   public java.sql.Clob getClob(int i)
/*      */     throws SQLException
/*      */   {
/* 1845 */     if (!this.isBinaryEncoded) {
/* 1846 */       String asString = getStringForClob(i);
/*      */       
/* 1848 */       if (asString == null) {
/* 1849 */         return null;
/*      */       }
/*      */       
/* 1852 */       return new Clob(asString);
/*      */     }
/*      */     
/* 1855 */     return getNativeClob(i);
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
/*      */   public java.sql.Clob getClob(String colName)
/*      */     throws SQLException
/*      */   {
/* 1870 */     return getClob(findColumn(colName));
/*      */   }
/*      */   
/*      */   private final java.sql.Clob getClobFromString(String stringVal, int columnIndex) throws SQLException
/*      */   {
/* 1875 */     return new Clob(stringVal);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public int getConcurrency()
/*      */     throws SQLException
/*      */   {
/* 1888 */     return 1007;
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
/*      */   public String getCursorName()
/*      */     throws SQLException
/*      */   {
/* 1917 */     throw SQLError.createSQLException(Messages.getString("ResultSet.Positioned_Update_not_supported"), "S1C00");
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
/*      */   public Date getDate(int columnIndex)
/*      */     throws SQLException
/*      */   {
/* 1934 */     return getDate(columnIndex, null);
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
/*      */   public Date getDate(int columnIndex, Calendar cal)
/*      */     throws SQLException
/*      */   {
/* 1955 */     if (this.isBinaryEncoded) {
/* 1956 */       return getNativeDate(columnIndex, cal != null ? cal.getTimeZone() : getDefaultTimeZone());
/*      */     }
/*      */     
/*      */ 
/* 1960 */     if (!this.useFastDateParsing) {
/* 1961 */       String stringVal = getStringInternal(columnIndex, false);
/*      */       
/* 1963 */       if (stringVal == null) {
/* 1964 */         return null;
/*      */       }
/*      */       
/* 1967 */       return getDateFromString(stringVal, columnIndex);
/*      */     }
/* 1969 */     checkColumnBounds(columnIndex);
/*      */     
/* 1971 */     return getDateFromBytes(((byte[][])this.thisRow)[(columnIndex - 1)], columnIndex);
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
/*      */   public Date getDate(String columnName)
/*      */     throws SQLException
/*      */   {
/* 1988 */     return getDate(findColumn(columnName));
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
/*      */   public Date getDate(String columnName, Calendar cal)
/*      */     throws SQLException
/*      */   {
/* 2008 */     return getDate(findColumn(columnName), cal);
/*      */   }
/*      */   
/*      */   private final Date getDateFromString(String stringVal, int columnIndex) throws SQLException
/*      */   {
/* 2013 */     int year = 0;
/* 2014 */     int month = 0;
/* 2015 */     int day = 0;
/*      */     try
/*      */     {
/* 2018 */       this.wasNullFlag = false;
/*      */       
/* 2020 */       if (stringVal == null) {
/* 2021 */         this.wasNullFlag = true;
/*      */         
/* 2023 */         return null;
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
/* 2034 */       stringVal = stringVal.trim();
/*      */       
/* 2036 */       if ((stringVal.equals("0")) || (stringVal.equals("0000-00-00")) || (stringVal.equals("0000-00-00 00:00:00")) || (stringVal.equals("00000000000000")) || (stringVal.equals("0")))
/*      */       {
/*      */ 
/*      */ 
/*      */ 
/* 2041 */         if ("convertToNull".equals(this.connection.getZeroDateTimeBehavior()))
/*      */         {
/* 2043 */           this.wasNullFlag = true;
/*      */           
/* 2045 */           return null; }
/* 2046 */         if ("exception".equals(this.connection.getZeroDateTimeBehavior()))
/*      */         {
/* 2048 */           throw SQLError.createSQLException("Value '" + stringVal + "' can not be represented as java.sql.Date", "S1009");
/*      */         }
/*      */         
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 2055 */         return fastDateCreate(null, 1, 1, 1);
/*      */       }
/* 2057 */       if (this.fields[(columnIndex - 1)].getMysqlType() == 7)
/*      */       {
/* 2059 */         switch (stringVal.length()) {
/*      */         case 19: 
/*      */         case 21: 
/* 2062 */           year = Integer.parseInt(stringVal.substring(0, 4));
/* 2063 */           month = Integer.parseInt(stringVal.substring(5, 7));
/* 2064 */           day = Integer.parseInt(stringVal.substring(8, 10));
/*      */           
/* 2066 */           return fastDateCreate(null, year, month, day);
/*      */         
/*      */ 
/*      */         case 8: 
/*      */         case 14: 
/* 2071 */           year = Integer.parseInt(stringVal.substring(0, 4));
/* 2072 */           month = Integer.parseInt(stringVal.substring(4, 6));
/* 2073 */           day = Integer.parseInt(stringVal.substring(6, 8));
/*      */           
/* 2075 */           return fastDateCreate(null, year, month, day);
/*      */         
/*      */ 
/*      */         case 6: 
/*      */         case 10: 
/*      */         case 12: 
/* 2081 */           year = Integer.parseInt(stringVal.substring(0, 2));
/*      */           
/* 2083 */           if (year <= 69) {
/* 2084 */             year += 100;
/*      */           }
/*      */           
/* 2087 */           month = Integer.parseInt(stringVal.substring(2, 4));
/* 2088 */           day = Integer.parseInt(stringVal.substring(4, 6));
/*      */           
/* 2090 */           return fastDateCreate(null, year + 1900, month, day);
/*      */         
/*      */ 
/*      */         case 4: 
/* 2094 */           year = Integer.parseInt(stringVal.substring(0, 4));
/*      */           
/* 2096 */           if (year <= 69) {
/* 2097 */             year += 100;
/*      */           }
/*      */           
/* 2100 */           month = Integer.parseInt(stringVal.substring(2, 4));
/*      */           
/* 2102 */           return fastDateCreate(null, year + 1900, month, 1);
/*      */         
/*      */ 
/*      */         case 2: 
/* 2106 */           year = Integer.parseInt(stringVal.substring(0, 2));
/*      */           
/* 2108 */           if (year <= 69) {
/* 2109 */             year += 100;
/*      */           }
/*      */           
/* 2112 */           return fastDateCreate(null, year + 1900, 1, 1);
/*      */         }
/*      */         
/*      */         
/* 2116 */         throw SQLError.createSQLException(Messages.getString("ResultSet.Bad_format_for_Date", new Object[] { stringVal, new Integer(columnIndex) }), "S1009");
/*      */       }
/*      */       
/*      */ 
/*      */ 
/* 2121 */       if (this.fields[(columnIndex - 1)].getMysqlType() == 13)
/*      */       {
/* 2123 */         if ((stringVal.length() == 2) || (stringVal.length() == 1)) {
/* 2124 */           year = Integer.parseInt(stringVal);
/*      */           
/* 2126 */           if (year <= 69) {
/* 2127 */             year += 100;
/*      */           }
/*      */           
/* 2130 */           year += 1900;
/*      */         } else {
/* 2132 */           year = Integer.parseInt(stringVal.substring(0, 4));
/*      */         }
/*      */         
/* 2135 */         return fastDateCreate(null, year, 1, 1); }
/* 2136 */       if (this.fields[(columnIndex - 1)].getMysqlType() == 11) {
/* 2137 */         return fastDateCreate(null, 1970, 1, 1);
/*      */       }
/* 2139 */       if (stringVal.length() < 10) {
/* 2140 */         if (stringVal.length() == 8) {
/* 2141 */           return fastDateCreate(null, 1970, 1, 1);
/*      */         }
/*      */         
/* 2144 */         throw SQLError.createSQLException(Messages.getString("ResultSet.Bad_format_for_Date", new Object[] { stringVal, new Integer(columnIndex) }), "S1009");
/*      */       }
/*      */       
/*      */ 
/*      */ 
/*      */ 
/* 2150 */       if (stringVal.length() != 18) {
/* 2151 */         year = Integer.parseInt(stringVal.substring(0, 4));
/* 2152 */         month = Integer.parseInt(stringVal.substring(5, 7));
/* 2153 */         day = Integer.parseInt(stringVal.substring(8, 10));
/*      */       }
/*      */       else {
/* 2156 */         StringTokenizer st = new StringTokenizer(stringVal, "- ");
/*      */         
/* 2158 */         year = Integer.parseInt(st.nextToken());
/* 2159 */         month = Integer.parseInt(st.nextToken());
/* 2160 */         day = Integer.parseInt(st.nextToken());
/*      */       }
/*      */       
/*      */ 
/* 2164 */       return fastDateCreate(null, year, month, day);
/*      */     } catch (SQLException sqlEx) {
/* 2166 */       throw sqlEx;
/*      */     } catch (Exception e) {
/* 2168 */       throw SQLError.createSQLException(Messages.getString("ResultSet.Bad_format_for_Date", new Object[] { stringVal, new Integer(columnIndex) }), "S1009");
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   private final Date getDateFromBytes(byte[] dateAsBytes, int columnIndex)
/*      */     throws SQLException
/*      */   {
/* 2177 */     checkColumnBounds(columnIndex);
/*      */     
/* 2179 */     int year = 0;
/* 2180 */     int month = 0;
/* 2181 */     int day = 0;
/*      */     try
/*      */     {
/* 2184 */       this.wasNullFlag = false;
/*      */       
/* 2186 */       if (dateAsBytes == null) {
/* 2187 */         this.wasNullFlag = true;
/*      */         
/* 2189 */         return null;
/*      */       }
/*      */       
/*      */ 
/* 2193 */       boolean allZeroDate = true;
/*      */       
/* 2195 */       boolean onlyTimePresent = StringUtils.indexOf(dateAsBytes, ':') != -1;
/*      */       
/* 2197 */       int length = dateAsBytes.length;
/*      */       
/* 2199 */       for (int i = 0; i < length; i++) {
/* 2200 */         byte b = dateAsBytes[i];
/*      */         
/* 2202 */         if ((b == 32) || (b == 45) || (b == 47)) {
/* 2203 */           onlyTimePresent = false;
/*      */         }
/*      */         
/* 2206 */         if ((b != 48) && (b != 32) && (b != 58) && (b != 45) && (b != 47) && (b != 46))
/*      */         {
/* 2208 */           allZeroDate = false;
/*      */           
/* 2210 */           break;
/*      */         }
/*      */       }
/*      */       
/* 2214 */       if ((!onlyTimePresent) && (allZeroDate))
/*      */       {
/* 2216 */         if ("convertToNull".equals(this.connection.getZeroDateTimeBehavior()))
/*      */         {
/* 2218 */           this.wasNullFlag = true;
/*      */           
/* 2220 */           return null; }
/* 2221 */         if ("exception".equals(this.connection.getZeroDateTimeBehavior()))
/*      */         {
/* 2223 */           throw SQLError.createSQLException("Value '" + new String(dateAsBytes) + "' can not be represented as java.sql.Date", "S1009");
/*      */         }
/*      */         
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 2230 */         return fastDateCreate(null, 1, 1, 1);
/*      */       }
/* 2232 */       if (this.fields[(columnIndex - 1)].getMysqlType() == 7)
/*      */       {
/* 2234 */         switch (length) {
/*      */         case 19: 
/*      */         case 21: 
/* 2237 */           year = StringUtils.getInt(dateAsBytes, 0, 4);
/* 2238 */           month = StringUtils.getInt(dateAsBytes, 5, 7);
/* 2239 */           day = StringUtils.getInt(dateAsBytes, 8, 10);
/*      */           
/* 2241 */           return fastDateCreate(null, year, month, day);
/*      */         
/*      */ 
/*      */         case 8: 
/*      */         case 14: 
/* 2246 */           year = StringUtils.getInt(dateAsBytes, 0, 4);
/* 2247 */           month = StringUtils.getInt(dateAsBytes, 4, 6);
/* 2248 */           day = StringUtils.getInt(dateAsBytes, 6, 8);
/*      */           
/* 2250 */           return fastDateCreate(null, year, month, day);
/*      */         
/*      */ 
/*      */         case 6: 
/*      */         case 10: 
/*      */         case 12: 
/* 2256 */           year = StringUtils.getInt(dateAsBytes, 0, 2);
/*      */           
/* 2258 */           if (year <= 69) {
/* 2259 */             year += 100;
/*      */           }
/*      */           
/* 2262 */           month = StringUtils.getInt(dateAsBytes, 2, 4);
/* 2263 */           day = StringUtils.getInt(dateAsBytes, 4, 6);
/*      */           
/* 2265 */           return fastDateCreate(null, year + 1900, month, day);
/*      */         
/*      */ 
/*      */         case 4: 
/* 2269 */           year = StringUtils.getInt(dateAsBytes, 0, 4);
/*      */           
/* 2271 */           if (year <= 69) {
/* 2272 */             year += 100;
/*      */           }
/*      */           
/* 2275 */           month = StringUtils.getInt(dateAsBytes, 2, 4);
/*      */           
/* 2277 */           return fastDateCreate(null, year + 1900, month, 1);
/*      */         
/*      */ 
/*      */         case 2: 
/* 2281 */           year = StringUtils.getInt(dateAsBytes, 0, 2);
/*      */           
/* 2283 */           if (year <= 69) {
/* 2284 */             year += 100;
/*      */           }
/*      */           
/* 2287 */           return fastDateCreate(null, year + 1900, 1, 1);
/*      */         }
/*      */         
/*      */         
/* 2291 */         throw SQLError.createSQLException(Messages.getString("ResultSet.Bad_format_for_Date", new Object[] { new String(dateAsBytes), new Integer(columnIndex) }), "S1009");
/*      */       }
/*      */       
/*      */ 
/*      */ 
/* 2296 */       if (this.fields[(columnIndex - 1)].getMysqlType() == 13)
/*      */       {
/* 2298 */         if ((length == 2) || (length == 1)) {
/* 2299 */           year = StringUtils.getInt(dateAsBytes);
/*      */           
/* 2301 */           if (year <= 69) {
/* 2302 */             year += 100;
/*      */           }
/*      */           
/* 2305 */           year += 1900;
/*      */         } else {
/* 2307 */           year = StringUtils.getInt(dateAsBytes, 0, 4);
/*      */         }
/*      */         
/* 2310 */         return fastDateCreate(null, year, 1, 1); }
/* 2311 */       if (this.fields[(columnIndex - 1)].getMysqlType() == 11) {
/* 2312 */         return fastDateCreate(null, 1970, 1, 1);
/*      */       }
/* 2314 */       if (length < 10) {
/* 2315 */         if (length == 8) {
/* 2316 */           return fastDateCreate(null, 1970, 1, 1);
/*      */         }
/*      */         
/* 2319 */         throw SQLError.createSQLException(Messages.getString("ResultSet.Bad_format_for_Date", new Object[] { new String(dateAsBytes), new Integer(columnIndex) }), "S1009");
/*      */       }
/*      */       
/*      */ 
/*      */ 
/*      */ 
/* 2325 */       if (length != 18) {
/* 2326 */         year = StringUtils.getInt(dateAsBytes, 0, 4);
/* 2327 */         month = StringUtils.getInt(dateAsBytes, 5, 7);
/* 2328 */         day = StringUtils.getInt(dateAsBytes, 8, 10);
/*      */       }
/*      */       else {
/* 2331 */         StringTokenizer st = new StringTokenizer(new String(dateAsBytes), "- ");
/*      */         
/* 2333 */         year = Integer.parseInt(st.nextToken());
/* 2334 */         month = Integer.parseInt(st.nextToken());
/* 2335 */         day = Integer.parseInt(st.nextToken());
/*      */       }
/*      */       
/*      */ 
/* 2339 */       return fastDateCreate(null, year, month, day);
/*      */     } catch (SQLException sqlEx) {
/* 2341 */       throw sqlEx;
/*      */     } catch (Exception e) {
/* 2343 */       throw SQLError.createSQLException(Messages.getString("ResultSet.Bad_format_for_Date", new Object[] { new String(dateAsBytes), new Integer(columnIndex) }), "S1009");
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   private TimeZone getDefaultTimeZone()
/*      */   {
/* 2351 */     return this.connection.getDefaultTimeZone();
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
/*      */   public double getDouble(int columnIndex)
/*      */     throws SQLException
/*      */   {
/* 2366 */     if (!this.isBinaryEncoded) {
/* 2367 */       return getDoubleInternal(columnIndex);
/*      */     }
/*      */     
/* 2370 */     return getNativeDouble(columnIndex);
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
/*      */   public double getDouble(String columnName)
/*      */     throws SQLException
/*      */   {
/* 2385 */     return getDouble(findColumn(columnName));
/*      */   }
/*      */   
/*      */   private final double getDoubleFromString(String stringVal, int columnIndex) throws SQLException
/*      */   {
/* 2390 */     return getDoubleInternal(stringVal, columnIndex);
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
/*      */   protected double getDoubleInternal(int colIndex)
/*      */     throws SQLException
/*      */   {
/* 2406 */     return getDoubleInternal(getString(colIndex), colIndex);
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
/*      */   protected double getDoubleInternal(String stringVal, int colIndex)
/*      */     throws SQLException
/*      */   {
/*      */     try
/*      */     {
/* 2426 */       if (stringVal == null) {
/* 2427 */         return 0.0D;
/*      */       }
/*      */       
/* 2430 */       if (stringVal.length() == 0) {
/* 2431 */         return convertToZeroWithEmptyCheck();
/*      */       }
/*      */       
/* 2434 */       double d = Double.parseDouble(stringVal);
/*      */       
/* 2436 */       if (this.useStrictFloatingPoint)
/*      */       {
/* 2438 */         if (d == 2.147483648E9D)
/*      */         {
/* 2440 */           d = 2.147483647E9D;
/* 2441 */         } else if (d == 1.0000000036275E-15D)
/*      */         {
/* 2443 */           d = 1.0E-15D;
/* 2444 */         } else if (d == 9.999999869911E14D) {
/* 2445 */           d = 9.99999999999999E14D;
/* 2446 */         } else if (d == 1.4012984643248E-45D) {
/* 2447 */           d = 1.4E-45D;
/* 2448 */         } else if (d == 1.4013E-45D) {
/* 2449 */           d = 1.4E-45D;
/* 2450 */         } else if (d == 3.4028234663853E37D) {
/* 2451 */           d = 3.4028235E37D;
/* 2452 */         } else if (d == -2.14748E9D) {
/* 2453 */           d = -2.147483648E9D;
/* 2454 */         } else if (d != 3.40282E37D) {} }
/* 2455 */       return 3.4028235E37D;
/*      */ 
/*      */     }
/*      */     catch (NumberFormatException e)
/*      */     {
/*      */ 
/* 2461 */       if (this.fields[(colIndex - 1)].getMysqlType() == 16) {
/* 2462 */         long valueAsLong = getNumericRepresentationOfSQLBitType(colIndex);
/*      */         
/* 2464 */         return valueAsLong;
/*      */       }
/*      */       
/* 2467 */       throw SQLError.createSQLException(Messages.getString("ResultSet.Bad_format_for_number", new Object[] { stringVal, new Integer(colIndex) }), "S1009");
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
/*      */   public int getFetchDirection()
/*      */     throws SQLException
/*      */   {
/* 2483 */     return this.fetchDirection;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public int getFetchSize()
/*      */     throws SQLException
/*      */   {
/* 2495 */     return this.fetchSize;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected char getFirstCharOfQuery()
/*      */   {
/* 2505 */     return this.firstCharOfQuery;
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
/*      */   public float getFloat(int columnIndex)
/*      */     throws SQLException
/*      */   {
/* 2520 */     if (!this.isBinaryEncoded) {
/* 2521 */       String val = null;
/*      */       
/* 2523 */       val = getString(columnIndex);
/*      */       
/* 2525 */       return getFloatFromString(val, columnIndex);
/*      */     }
/*      */     
/* 2528 */     return getNativeFloat(columnIndex);
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
/*      */   public float getFloat(String columnName)
/*      */     throws SQLException
/*      */   {
/* 2543 */     return getFloat(findColumn(columnName));
/*      */   }
/*      */   
/*      */   private final float getFloatFromString(String val, int columnIndex) throws SQLException
/*      */   {
/*      */     try {
/* 2549 */       if (val != null) {
/* 2550 */         if (val.length() == 0) {
/* 2551 */           return convertToZeroWithEmptyCheck();
/*      */         }
/*      */         
/* 2554 */         float f = Float.parseFloat(val);
/*      */         
/* 2556 */         if ((this.connection.getJdbcCompliantTruncationForReads()) && (
/* 2557 */           (f == Float.MIN_VALUE) || (f == Float.MAX_VALUE))) {
/* 2558 */           double valAsDouble = Double.parseDouble(val);
/*      */           
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 2564 */           if ((valAsDouble < 1.401298464324817E-45D - MIN_DIFF_PREC) || (valAsDouble > 3.4028234663852886E38D - MAX_DIFF_PREC))
/*      */           {
/* 2566 */             throwRangeException(String.valueOf(valAsDouble), columnIndex, 6);
/*      */           }
/*      */         }
/*      */         
/*      */ 
/*      */ 
/* 2572 */         return f;
/*      */       }
/*      */       
/* 2575 */       return 0.0F;
/*      */     } catch (NumberFormatException nfe) {
/*      */       try {
/* 2578 */         Double valueAsDouble = new Double(val);
/* 2579 */         float valueAsFloat = valueAsDouble.floatValue();
/*      */         
/* 2581 */         if (this.connection.getJdbcCompliantTruncationForReads())
/*      */         {
/* 2583 */           if (((this.connection.getJdbcCompliantTruncationForReads()) && (valueAsFloat == Float.NEGATIVE_INFINITY)) || (valueAsFloat == Float.POSITIVE_INFINITY))
/*      */           {
/*      */ 
/* 2586 */             throwRangeException(valueAsDouble.toString(), columnIndex, 6);
/*      */           }
/*      */         }
/*      */         
/*      */ 
/* 2591 */         return valueAsFloat;
/*      */ 
/*      */       }
/*      */       catch (NumberFormatException newNfe)
/*      */       {
/* 2596 */         throw SQLError.createSQLException(Messages.getString("ResultSet.Invalid_value_for_getFloat()_-____200") + val + Messages.getString("ResultSet.___in_column__201") + columnIndex, "S1009");
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
/*      */   public int getInt(int columnIndex)
/*      */     throws SQLException
/*      */   {
/* 2617 */     checkRowPos();
/*      */     
/* 2619 */     if (!this.isBinaryEncoded) {
/* 2620 */       if (this.connection.getUseFastIntParsing()) {
/* 2621 */         checkColumnBounds(columnIndex);
/*      */         
/* 2623 */         if (this.thisRow[(columnIndex - 1)] == null) {
/* 2624 */           this.wasNullFlag = true;
/*      */         } else {
/* 2626 */           this.wasNullFlag = false;
/*      */         }
/*      */         
/* 2629 */         if (this.wasNullFlag) {
/* 2630 */           return 0;
/*      */         }
/*      */         
/* 2633 */         byte[] intAsBytes = (byte[])this.thisRow[(columnIndex - 1)];
/*      */         
/* 2635 */         if (intAsBytes.length == 0) {
/* 2636 */           return convertToZeroWithEmptyCheck();
/*      */         }
/*      */         
/* 2639 */         boolean needsFullParse = false;
/*      */         
/* 2641 */         for (int i = 0; i < intAsBytes.length; i++) {
/* 2642 */           if (((char)intAsBytes[i] == 'e') || ((char)intAsBytes[i] == 'E'))
/*      */           {
/* 2644 */             needsFullParse = true;
/*      */             
/* 2646 */             break;
/*      */           }
/*      */         }
/*      */         
/* 2650 */         if (!needsFullParse) {
/*      */           try {
/* 2652 */             return parseIntWithOverflowCheck(columnIndex, intAsBytes, null);
/*      */           }
/*      */           catch (NumberFormatException nfe)
/*      */           {
/*      */             try {
/* 2657 */               return parseIntAsDouble(columnIndex, new String(intAsBytes));
/*      */ 
/*      */             }
/*      */             catch (NumberFormatException newNfe)
/*      */             {
/*      */ 
/* 2663 */               if (this.fields[(columnIndex - 1)].getMysqlType() == 16) {
/* 2664 */                 long valueAsLong = getNumericRepresentationOfSQLBitType(columnIndex);
/*      */                 
/* 2666 */                 if ((this.connection.getJdbcCompliantTruncationForReads()) && ((valueAsLong < -2147483648L) || (valueAsLong > 2147483647L)))
/*      */                 {
/*      */ 
/* 2669 */                   throwRangeException(String.valueOf(valueAsLong), columnIndex, 4);
/*      */                 }
/*      */                 
/*      */ 
/* 2673 */                 return (int)valueAsLong;
/*      */               }
/*      */               
/* 2676 */               throw SQLError.createSQLException(Messages.getString("ResultSet.Invalid_value_for_getInt()_-____74") + new String(intAsBytes) + "'", "S1009");
/*      */             }
/*      */           }
/*      */         }
/*      */       }
/*      */       
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 2686 */       String val = null;
/*      */       try
/*      */       {
/* 2689 */         val = getString(columnIndex);
/*      */         
/* 2691 */         if (val != null) {
/* 2692 */           if (val.length() == 0) {
/* 2693 */             return convertToZeroWithEmptyCheck();
/*      */           }
/*      */           
/* 2696 */           if ((val.indexOf("e") == -1) && (val.indexOf("E") == -1) && (val.indexOf(".") == -1))
/*      */           {
/* 2698 */             return Integer.parseInt(val);
/*      */           }
/*      */           
/*      */ 
/* 2702 */           return parseIntAsDouble(columnIndex, val);
/*      */         }
/*      */         
/* 2705 */         return 0;
/*      */       } catch (NumberFormatException nfe) {
/*      */         try {
/* 2708 */           return parseIntAsDouble(columnIndex, val);
/*      */ 
/*      */         }
/*      */         catch (NumberFormatException newNfe)
/*      */         {
/* 2713 */           if (this.fields[(columnIndex - 1)].getMysqlType() == 16) {
/* 2714 */             long valueAsLong = getNumericRepresentationOfSQLBitType(columnIndex);
/*      */             
/* 2716 */             if ((this.connection.getJdbcCompliantTruncationForReads()) && ((valueAsLong < -2147483648L) || (valueAsLong > 2147483647L)))
/*      */             {
/*      */ 
/* 2719 */               throwRangeException(String.valueOf(valueAsLong), columnIndex, 4);
/*      */             }
/*      */             
/*      */ 
/* 2723 */             return (int)valueAsLong;
/*      */           }
/*      */           
/* 2726 */           throw SQLError.createSQLException(Messages.getString("ResultSet.Invalid_value_for_getInt()_-____74") + val + "'", "S1009");
/*      */         }
/*      */       }
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/* 2734 */     return getNativeInt(columnIndex);
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
/*      */   public int getInt(String columnName)
/*      */     throws SQLException
/*      */   {
/* 2749 */     return getInt(findColumn(columnName));
/*      */   }
/*      */   
/*      */   private final int getIntFromString(String val, int columnIndex) throws SQLException
/*      */   {
/*      */     try {
/* 2755 */       if (val != null)
/*      */       {
/* 2757 */         if (val.length() == 0) {
/* 2758 */           return convertToZeroWithEmptyCheck();
/*      */         }
/*      */         
/* 2761 */         if ((val.indexOf("e") == -1) && (val.indexOf("E") == -1) && (val.indexOf(".") == -1))
/*      */         {
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 2771 */           val = val.trim();
/*      */           
/* 2773 */           int valueAsInt = Integer.parseInt(val);
/*      */           
/* 2775 */           if ((this.connection.getJdbcCompliantTruncationForReads()) && (
/* 2776 */             (valueAsInt == Integer.MIN_VALUE) || (valueAsInt == Integer.MAX_VALUE)))
/*      */           {
/* 2778 */             long valueAsLong = Long.parseLong(val);
/*      */             
/* 2780 */             if ((valueAsLong < -2147483648L) || (valueAsLong > 2147483647L))
/*      */             {
/* 2782 */               throwRangeException(String.valueOf(valueAsLong), columnIndex, 4);
/*      */             }
/*      */           }
/*      */           
/*      */ 
/*      */ 
/*      */ 
/* 2789 */           return valueAsInt;
/*      */         }
/*      */         
/*      */ 
/*      */ 
/* 2794 */         double valueAsDouble = Double.parseDouble(val);
/*      */         
/* 2796 */         if ((this.connection.getJdbcCompliantTruncationForReads()) && (
/* 2797 */           (valueAsDouble < -2.147483648E9D) || (valueAsDouble > 2.147483647E9D)))
/*      */         {
/* 2799 */           throwRangeException(String.valueOf(valueAsDouble), columnIndex, 4);
/*      */         }
/*      */         
/*      */ 
/*      */ 
/* 2804 */         return (int)valueAsDouble;
/*      */       }
/*      */       
/* 2807 */       return 0;
/*      */     } catch (NumberFormatException nfe) {
/*      */       try {
/* 2810 */         double valueAsDouble = Double.parseDouble(val);
/*      */         
/* 2812 */         if ((this.connection.getJdbcCompliantTruncationForReads()) && (
/* 2813 */           (valueAsDouble < -2.147483648E9D) || (valueAsDouble > 2.147483647E9D)))
/*      */         {
/* 2815 */           throwRangeException(String.valueOf(valueAsDouble), columnIndex, 4);
/*      */         }
/*      */         
/*      */ 
/*      */ 
/* 2820 */         return (int)valueAsDouble;
/*      */ 
/*      */       }
/*      */       catch (NumberFormatException newNfe)
/*      */       {
/* 2825 */         throw SQLError.createSQLException(Messages.getString("ResultSet.Invalid_value_for_getInt()_-____206") + val + Messages.getString("ResultSet.___in_column__207") + columnIndex, "S1009");
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
/*      */   public long getLong(int columnIndex)
/*      */     throws SQLException
/*      */   {
/* 2845 */     return getLong(columnIndex, true);
/*      */   }
/*      */   
/*      */   private long getLong(int columnIndex, boolean overflowCheck) throws SQLException {
/* 2849 */     if (!this.isBinaryEncoded) {
/* 2850 */       checkRowPos();
/*      */       
/* 2852 */       if (this.connection.getUseFastIntParsing())
/*      */       {
/* 2854 */         checkColumnBounds(columnIndex);
/*      */         
/* 2856 */         if (this.thisRow[(columnIndex - 1)] == null) {
/* 2857 */           this.wasNullFlag = true;
/*      */         } else {
/* 2859 */           this.wasNullFlag = false;
/*      */         }
/*      */         
/* 2862 */         if (this.wasNullFlag) {
/* 2863 */           return 0L;
/*      */         }
/*      */         
/* 2866 */         byte[] longAsBytes = (byte[])this.thisRow[(columnIndex - 1)];
/*      */         
/* 2868 */         if (longAsBytes.length == 0) {
/* 2869 */           return convertToZeroWithEmptyCheck();
/*      */         }
/*      */         
/* 2872 */         boolean needsFullParse = false;
/*      */         
/* 2874 */         for (int i = 0; i < longAsBytes.length; i++) {
/* 2875 */           if (((char)longAsBytes[i] == 'e') || ((char)longAsBytes[i] == 'E'))
/*      */           {
/* 2877 */             needsFullParse = true;
/*      */             
/* 2879 */             break;
/*      */           }
/*      */         }
/*      */         
/* 2883 */         if (!needsFullParse) {
/*      */           try {
/* 2885 */             return parseLongWithOverflowCheck(columnIndex, longAsBytes, null, overflowCheck);
/*      */           }
/*      */           catch (NumberFormatException nfe)
/*      */           {
/*      */             try {
/* 2890 */               return parseLongAsDouble(columnIndex, new String(longAsBytes));
/*      */ 
/*      */             }
/*      */             catch (NumberFormatException newNfe)
/*      */             {
/*      */ 
/* 2896 */               if (this.fields[(columnIndex - 1)].getMysqlType() == 16) {
/* 2897 */                 return getNumericRepresentationOfSQLBitType(columnIndex);
/*      */               }
/*      */               
/* 2900 */               throw SQLError.createSQLException(Messages.getString("ResultSet.Invalid_value_for_getLong()_-____79") + new String(longAsBytes) + "'", "S1009");
/*      */             }
/*      */           }
/*      */         }
/*      */       }
/*      */       
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 2910 */       String val = null;
/*      */       try
/*      */       {
/* 2913 */         val = getString(columnIndex);
/*      */         
/* 2915 */         if (val != null) {
/* 2916 */           if (val.length() == 0) {
/* 2917 */             return convertToZeroWithEmptyCheck();
/*      */           }
/*      */           
/* 2920 */           if ((val.indexOf("e") == -1) && (val.indexOf("E") == -1)) {
/* 2921 */             return parseLongWithOverflowCheck(columnIndex, null, val, overflowCheck);
/*      */           }
/*      */           
/*      */ 
/*      */ 
/* 2926 */           return parseLongAsDouble(columnIndex, val);
/*      */         }
/*      */         
/* 2929 */         return 0L;
/*      */       } catch (NumberFormatException nfe) {
/*      */         try {
/* 2932 */           return parseLongAsDouble(columnIndex, val);
/*      */ 
/*      */         }
/*      */         catch (NumberFormatException newNfe)
/*      */         {
/* 2937 */           throw SQLError.createSQLException(Messages.getString("ResultSet.Invalid_value_for_getLong()_-____79") + val + "'", "S1009");
/*      */         }
/*      */       }
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/* 2945 */     return getNativeLong(columnIndex, overflowCheck, true);
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
/*      */   public long getLong(String columnName)
/*      */     throws SQLException
/*      */   {
/* 2960 */     return getLong(findColumn(columnName));
/*      */   }
/*      */   
/*      */   private final long getLongFromString(String val, int columnIndex) throws SQLException
/*      */   {
/*      */     try {
/* 2966 */       if (val != null)
/*      */       {
/* 2968 */         if (val.length() == 0) {
/* 2969 */           return convertToZeroWithEmptyCheck();
/*      */         }
/*      */         
/* 2972 */         if ((val.indexOf("e") == -1) && (val.indexOf("E") == -1)) {
/* 2973 */           return parseLongWithOverflowCheck(columnIndex, null, val, true);
/*      */         }
/*      */         
/*      */ 
/* 2977 */         return parseLongAsDouble(columnIndex, val);
/*      */       }
/*      */       
/* 2980 */       return 0L;
/*      */     }
/*      */     catch (NumberFormatException nfe) {
/*      */       try {
/* 2984 */         return parseLongAsDouble(columnIndex, val);
/*      */ 
/*      */       }
/*      */       catch (NumberFormatException newNfe)
/*      */       {
/* 2989 */         throw SQLError.createSQLException(Messages.getString("ResultSet.Invalid_value_for_getLong()_-____211") + val + Messages.getString("ResultSet.___in_column__212") + columnIndex, "S1009");
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
/*      */   public java.sql.ResultSetMetaData getMetaData()
/*      */     throws SQLException
/*      */   {
/* 3008 */     checkClosed();
/*      */     
/* 3010 */     return new ResultSetMetaData(this.fields, this.connection.getUseOldAliasMetadataBehavior());
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
/*      */   protected Array getNativeArray(int i)
/*      */     throws SQLException
/*      */   {
/* 3028 */     throw new NotImplemented();
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
/*      */   protected InputStream getNativeAsciiStream(int columnIndex)
/*      */     throws SQLException
/*      */   {
/* 3058 */     checkRowPos();
/*      */     
/* 3060 */     return getNativeBinaryStream(columnIndex);
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
/*      */   protected BigDecimal getNativeBigDecimal(int columnIndex)
/*      */     throws SQLException
/*      */   {
/* 3079 */     checkColumnBounds(columnIndex);
/*      */     
/* 3081 */     int scale = this.fields[(columnIndex - 1)].getDecimals();
/*      */     
/* 3083 */     return getNativeBigDecimal(columnIndex, scale);
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
/*      */   protected BigDecimal getNativeBigDecimal(int columnIndex, int scale)
/*      */     throws SQLException
/*      */   {
/* 3102 */     checkColumnBounds(columnIndex);
/*      */     
/* 3104 */     String stringVal = null;
/*      */     
/* 3106 */     Field f = this.fields[(columnIndex - 1)];
/*      */     
/* 3108 */     if (this.thisRow[(columnIndex - 1)] == null) {
/* 3109 */       this.wasNullFlag = true;
/*      */       
/* 3111 */       return null;
/*      */     }
/*      */     
/* 3114 */     this.wasNullFlag = false;
/*      */     
/* 3116 */     switch (f.getSQLType()) {
/*      */     case 2: 
/*      */     case 3: 
/* 3119 */       stringVal = StringUtils.toAsciiString((byte[])this.thisRow[(columnIndex - 1)]);
/*      */       
/* 3121 */       break;
/*      */     default: 
/* 3123 */       stringVal = getNativeString(columnIndex);
/*      */     }
/*      */     
/* 3126 */     return getBigDecimalFromString(stringVal, columnIndex, scale);
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
/*      */   protected InputStream getNativeBinaryStream(int columnIndex)
/*      */     throws SQLException
/*      */   {
/* 3148 */     checkRowPos();
/*      */     
/* 3150 */     byte[] b = getNativeBytes(columnIndex, false);
/*      */     
/* 3152 */     if (b != null) {
/* 3153 */       return new ByteArrayInputStream(b);
/*      */     }
/*      */     
/* 3156 */     return null;
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
/*      */   protected java.sql.Blob getNativeBlob(int columnIndex)
/*      */     throws SQLException
/*      */   {
/* 3171 */     checkRowPos();
/*      */     
/* 3173 */     checkColumnBounds(columnIndex);
/*      */     
/* 3175 */     if (this.thisRow[(columnIndex - 1)] == null) {
/* 3176 */       this.wasNullFlag = true;
/*      */     } else {
/* 3178 */       this.wasNullFlag = false;
/*      */     }
/*      */     
/* 3181 */     if (this.wasNullFlag) {
/* 3182 */       return null;
/*      */     }
/*      */     
/* 3185 */     int mysqlType = this.fields[(columnIndex - 1)].getMysqlType();
/*      */     
/* 3187 */     byte[] dataAsBytes = null;
/*      */     
/* 3189 */     switch (mysqlType) {
/*      */     case 249: 
/*      */     case 250: 
/*      */     case 251: 
/*      */     case 252: 
/* 3194 */       dataAsBytes = (byte[])this.thisRow[(columnIndex - 1)];
/*      */     }
/*      */     
/* 3197 */     dataAsBytes = getNativeBytes(columnIndex, false);
/*      */     
/*      */ 
/* 3200 */     if (!this.connection.getEmulateLocators()) {
/* 3201 */       return new Blob(dataAsBytes);
/*      */     }
/*      */     
/* 3204 */     return new BlobFromLocator(this, columnIndex);
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
/*      */   protected byte getNativeByte(int columnIndex)
/*      */     throws SQLException
/*      */   {
/* 3219 */     return getNativeByte(columnIndex, true);
/*      */   }
/*      */   
/*      */   protected byte getNativeByte(int columnIndex, boolean overflowCheck) throws SQLException {
/* 3223 */     checkRowPos();
/*      */     
/* 3225 */     checkColumnBounds(columnIndex);
/*      */     
/* 3227 */     if (this.thisRow[(columnIndex - 1)] == null) {
/* 3228 */       this.wasNullFlag = true;
/*      */       
/* 3230 */       return 0;
/*      */     }
/*      */     
/* 3233 */     if (this.thisRow[(columnIndex - 1)] == null) {
/* 3234 */       this.wasNullFlag = true;
/*      */     } else {
/* 3236 */       this.wasNullFlag = false;
/*      */     }
/*      */     
/* 3239 */     if (this.wasNullFlag) {
/* 3240 */       return 0;
/*      */     }
/*      */     
/* 3243 */     columnIndex--;
/*      */     
/* 3245 */     Field field = this.fields[columnIndex];
/*      */     
/* 3247 */     switch (field.getMysqlType()) {
/*      */     case 16: 
/* 3249 */       long valueAsLong = getNumericRepresentationOfSQLBitType(columnIndex + 1);
/*      */       
/* 3251 */       if ((overflowCheck) && (this.connection.getJdbcCompliantTruncationForReads()) && ((valueAsLong < -128L) || (valueAsLong > 127L)))
/*      */       {
/*      */ 
/* 3254 */         throwRangeException(String.valueOf(valueAsLong), columnIndex + 1, -6);
/*      */       }
/*      */       
/*      */ 
/* 3258 */       return (byte)(int)valueAsLong;
/*      */     case 1: 
/* 3260 */       byte valueAsByte = ((byte[])this.thisRow[columnIndex])[0];
/*      */       
/* 3262 */       if (!field.isUnsigned()) {
/* 3263 */         return valueAsByte;
/*      */       }
/*      */       
/* 3266 */       short valueAsShort = valueAsByte >= 0 ? (short)valueAsByte : (short)(valueAsByte + 256);
/*      */       
/*      */ 
/* 3269 */       if ((overflowCheck) && (this.connection.getJdbcCompliantTruncationForReads()) && 
/* 3270 */         (valueAsShort > 127)) {
/* 3271 */         throwRangeException(String.valueOf(valueAsShort), columnIndex + 1, -6);
/*      */       }
/*      */       
/*      */ 
/*      */ 
/* 3276 */       return (byte)valueAsShort;
/*      */     
/*      */     case 2: 
/*      */     case 13: 
/* 3280 */       short valueAsShort = getNativeShort(columnIndex + 1);
/*      */       
/* 3282 */       if ((overflowCheck) && (this.connection.getJdbcCompliantTruncationForReads()) && (
/* 3283 */         (valueAsShort < -128) || (valueAsShort > 127)))
/*      */       {
/* 3285 */         throwRangeException(String.valueOf(valueAsShort), columnIndex + 1, -6);
/*      */       }
/*      */       
/*      */ 
/*      */ 
/* 3290 */       return (byte)valueAsShort;
/*      */     case 3: 
/*      */     case 9: 
/* 3293 */       int valueAsInt = getNativeInt(columnIndex + 1, false);
/*      */       
/* 3295 */       if ((overflowCheck) && (this.connection.getJdbcCompliantTruncationForReads()) && (
/* 3296 */         (valueAsInt < -128) || (valueAsInt > 127))) {
/* 3297 */         throwRangeException(String.valueOf(valueAsInt), columnIndex + 1, -6);
/*      */       }
/*      */       
/*      */ 
/*      */ 
/* 3302 */       return (byte)valueAsInt;
/*      */     
/*      */     case 4: 
/* 3305 */       float valueAsFloat = getNativeFloat(columnIndex + 1);
/*      */       
/* 3307 */       if ((overflowCheck) && (this.connection.getJdbcCompliantTruncationForReads()) && (
/* 3308 */         (valueAsFloat < -128.0F) || (valueAsFloat > 127.0F)))
/*      */       {
/*      */ 
/* 3311 */         throwRangeException(String.valueOf(valueAsFloat), columnIndex + 1, -6);
/*      */       }
/*      */       
/*      */ 
/*      */ 
/* 3316 */       return (byte)(int)valueAsFloat;
/*      */     
/*      */     case 5: 
/* 3319 */       double valueAsDouble = getNativeDouble(columnIndex + 1);
/*      */       
/* 3321 */       if ((overflowCheck) && (this.connection.getJdbcCompliantTruncationForReads()) && (
/* 3322 */         (valueAsDouble < -128.0D) || (valueAsDouble > 127.0D)))
/*      */       {
/* 3324 */         throwRangeException(String.valueOf(valueAsDouble), columnIndex + 1, -6);
/*      */       }
/*      */       
/*      */ 
/*      */ 
/* 3329 */       return (byte)(int)valueAsDouble;
/*      */     
/*      */     case 8: 
/* 3332 */       long valueAsLong = getNativeLong(columnIndex + 1, false, true);
/*      */       
/* 3334 */       if ((overflowCheck) && (this.connection.getJdbcCompliantTruncationForReads()) && (
/* 3335 */         (valueAsLong < -128L) || (valueAsLong > 127L)))
/*      */       {
/* 3337 */         throwRangeException(String.valueOf(valueAsLong), columnIndex + 1, -6);
/*      */       }
/*      */       
/*      */ 
/*      */ 
/* 3342 */       return (byte)(int)valueAsLong;
/*      */     }
/*      */     
/* 3345 */     if (this.useUsageAdvisor) {
/* 3346 */       issueConversionViaParsingWarning("getByte()", columnIndex, this.thisRow[columnIndex], this.fields[columnIndex], new int[] { 5, 1, 2, 3, 8, 4 });
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 3356 */     return getByteFromString(getNativeString(columnIndex + 1), columnIndex + 1);
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
/*      */   protected byte[] getNativeBytes(int columnIndex, boolean noConversion)
/*      */     throws SQLException
/*      */   {
/* 3378 */     checkRowPos();
/*      */     
/* 3380 */     checkColumnBounds(columnIndex);
/*      */     
/* 3382 */     if (this.thisRow[(columnIndex - 1)] == null) {
/* 3383 */       this.wasNullFlag = true;
/*      */     } else {
/* 3385 */       this.wasNullFlag = false;
/*      */     }
/*      */     
/* 3388 */     if (this.wasNullFlag) {
/* 3389 */       return null;
/*      */     }
/*      */     
/* 3392 */     Field field = this.fields[(columnIndex - 1)];
/*      */     
/* 3394 */     int mysqlType = field.getMysqlType();
/*      */     
/*      */ 
/*      */ 
/* 3398 */     if (noConversion) {
/* 3399 */       mysqlType = 252;
/*      */     }
/*      */     
/* 3402 */     switch (mysqlType) {
/*      */     case 16: 
/*      */     case 249: 
/*      */     case 250: 
/*      */     case 251: 
/*      */     case 252: 
/* 3408 */       return (byte[])this.thisRow[(columnIndex - 1)];
/*      */     }
/*      */     
/* 3411 */     int sqlType = field.getSQLType();
/*      */     
/* 3413 */     if ((sqlType == -3) || (sqlType == -2)) {
/* 3414 */       return (byte[])this.thisRow[(columnIndex - 1)];
/*      */     }
/*      */     
/* 3417 */     return getBytesFromString(getNativeString(columnIndex), columnIndex);
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
/*      */   protected Reader getNativeCharacterStream(int columnIndex)
/*      */     throws SQLException
/*      */   {
/* 3438 */     String asString = null;
/*      */     
/* 3440 */     asString = getStringForClob(columnIndex);
/*      */     
/* 3442 */     if (asString == null) {
/* 3443 */       return null;
/*      */     }
/* 3445 */     return getCharacterStreamFromString(asString, columnIndex);
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
/*      */   protected java.sql.Clob getNativeClob(int columnIndex)
/*      */     throws SQLException
/*      */   {
/* 3460 */     String stringVal = getStringForClob(columnIndex);
/*      */     
/* 3462 */     if (stringVal == null) {
/* 3463 */       return null;
/*      */     }
/*      */     
/* 3466 */     return getClobFromString(stringVal, columnIndex);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   private String getNativeConvertToString(int columnIndex, Field field)
/*      */     throws SQLException
/*      */   {
/* 3474 */     int sqlType = field.getSQLType();
/* 3475 */     int mysqlType = field.getMysqlType();
/*      */     
/* 3477 */     switch (sqlType) {
/*      */     case -7: 
/* 3479 */       return String.valueOf(getNumericRepresentationOfSQLBitType(columnIndex));
/*      */     case 16: 
/* 3481 */       boolean booleanVal = getBoolean(columnIndex);
/*      */       
/* 3483 */       if (this.wasNullFlag) {
/* 3484 */         return null;
/*      */       }
/*      */       
/* 3487 */       return String.valueOf(booleanVal);
/*      */     
/*      */     case -6: 
/* 3490 */       byte tinyintVal = getNativeByte(columnIndex, false);
/*      */       
/* 3492 */       if (this.wasNullFlag) {
/* 3493 */         return null;
/*      */       }
/*      */       
/* 3496 */       if ((!field.isUnsigned()) || (tinyintVal >= 0)) {
/* 3497 */         return String.valueOf(tinyintVal);
/*      */       }
/*      */       
/* 3500 */       short unsignedTinyVal = (short)(tinyintVal & 0xFF);
/*      */       
/* 3502 */       return String.valueOf(unsignedTinyVal);
/*      */     
/*      */ 
/*      */     case 5: 
/* 3506 */       int intVal = getNativeInt(columnIndex, false);
/*      */       
/* 3508 */       if (this.wasNullFlag) {
/* 3509 */         return null;
/*      */       }
/*      */       
/* 3512 */       if ((!field.isUnsigned()) || (intVal >= 0)) {
/* 3513 */         return String.valueOf(intVal);
/*      */       }
/*      */       
/* 3516 */       intVal &= 0xFFFF;
/*      */       
/* 3518 */       return String.valueOf(intVal);
/*      */     
/*      */     case 4: 
/* 3521 */       int intVal = getNativeInt(columnIndex, false);
/*      */       
/* 3523 */       if (this.wasNullFlag) {
/* 3524 */         return null;
/*      */       }
/*      */       
/* 3527 */       if ((!field.isUnsigned()) || (intVal >= 0) || (field.getMysqlType() == 9))
/*      */       {
/*      */ 
/* 3530 */         return String.valueOf(intVal);
/*      */       }
/*      */       
/* 3533 */       long longVal = intVal & 0xFFFFFFFF;
/*      */       
/* 3535 */       return String.valueOf(longVal);
/*      */     
/*      */ 
/*      */     case -5: 
/* 3539 */       if (!field.isUnsigned()) {
/* 3540 */         long longVal = getNativeLong(columnIndex, false, true);
/*      */         
/* 3542 */         if (this.wasNullFlag) {
/* 3543 */           return null;
/*      */         }
/*      */         
/* 3546 */         return String.valueOf(longVal);
/*      */       }
/*      */       
/* 3549 */       long longVal = getNativeLong(columnIndex, false, false);
/*      */       
/* 3551 */       if (this.wasNullFlag) {
/* 3552 */         return null;
/*      */       }
/*      */       
/* 3555 */       return String.valueOf(convertLongToUlong(longVal));
/*      */     case 7: 
/* 3557 */       float floatVal = getNativeFloat(columnIndex);
/*      */       
/* 3559 */       if (this.wasNullFlag) {
/* 3560 */         return null;
/*      */       }
/*      */       
/* 3563 */       return String.valueOf(floatVal);
/*      */     
/*      */     case 6: 
/*      */     case 8: 
/* 3567 */       double doubleVal = getNativeDouble(columnIndex);
/*      */       
/* 3569 */       if (this.wasNullFlag) {
/* 3570 */         return null;
/*      */       }
/*      */       
/* 3573 */       return String.valueOf(doubleVal);
/*      */     
/*      */     case 2: 
/*      */     case 3: 
/* 3577 */       String stringVal = StringUtils.toAsciiString((byte[])this.thisRow[(columnIndex - 1)]);
/*      */       
/*      */ 
/*      */ 
/*      */ 
/* 3582 */       if (stringVal != null) {
/* 3583 */         this.wasNullFlag = false;
/*      */         
/* 3585 */         if (stringVal.length() == 0) {
/* 3586 */           BigDecimal val = new BigDecimal(0.0D);
/*      */           
/* 3588 */           return val.toString();
/*      */         }
/*      */         try
/*      */         {
/* 3592 */           val = new BigDecimal(stringVal);
/*      */         } catch (NumberFormatException ex) { BigDecimal val;
/* 3594 */           throw SQLError.createSQLException(Messages.getString("ResultSet.Bad_format_for_BigDecimal", new Object[] { stringVal, new Integer(columnIndex) }), "S1009");
/*      */         }
/*      */         
/*      */ 
/*      */         BigDecimal val;
/*      */         
/*      */ 
/* 3601 */         return val.toString();
/*      */       }
/*      */       
/* 3604 */       this.wasNullFlag = true;
/*      */       
/* 3606 */       return null;
/*      */     
/*      */ 
/*      */     case -1: 
/*      */     case 1: 
/*      */     case 12: 
/* 3612 */       return extractStringFromNativeColumn(columnIndex, mysqlType);
/*      */     
/*      */     case -4: 
/*      */     case -3: 
/*      */     case -2: 
/* 3617 */       if (!field.isBlob())
/* 3618 */         return extractStringFromNativeColumn(columnIndex, mysqlType);
/* 3619 */       if (!field.isBinary()) {
/* 3620 */         return extractStringFromNativeColumn(columnIndex, mysqlType);
/*      */       }
/* 3622 */       byte[] data = getBytes(columnIndex);
/* 3623 */       Object obj = data;
/*      */       
/* 3625 */       if ((data != null) && (data.length >= 2)) {
/* 3626 */         if ((data[0] == -84) && (data[1] == -19)) {
/*      */           try
/*      */           {
/* 3629 */             ByteArrayInputStream bytesIn = new ByteArrayInputStream(data);
/*      */             
/* 3631 */             ObjectInputStream objIn = new ObjectInputStream(bytesIn);
/*      */             
/* 3633 */             obj = objIn.readObject();
/* 3634 */             objIn.close();
/* 3635 */             bytesIn.close();
/*      */           } catch (ClassNotFoundException cnfe) {
/* 3637 */             throw SQLError.createSQLException(Messages.getString("ResultSet.Class_not_found___91") + cnfe.toString() + Messages.getString("ResultSet._while_reading_serialized_object_92"));
/*      */ 
/*      */ 
/*      */           }
/*      */           catch (IOException ex)
/*      */           {
/*      */ 
/* 3644 */             obj = data;
/*      */           }
/*      */         }
/*      */         
/* 3648 */         return obj.toString();
/*      */       }
/*      */       
/* 3651 */       return extractStringFromNativeColumn(columnIndex, mysqlType);
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */     case 91: 
/* 3657 */       if (mysqlType == 13) {
/* 3658 */         short shortVal = getNativeShort(columnIndex);
/*      */         
/* 3660 */         if (!this.connection.getYearIsDateType())
/*      */         {
/* 3662 */           if (this.wasNullFlag) {
/* 3663 */             return null;
/*      */           }
/*      */           
/* 3666 */           return String.valueOf(shortVal);
/*      */         }
/*      */         
/* 3669 */         if (field.getLength() == 2L)
/*      */         {
/* 3671 */           if (shortVal <= 69) {
/* 3672 */             shortVal = (short)(shortVal + 100);
/*      */           }
/*      */           
/* 3675 */           shortVal = (short)(shortVal + 1900);
/*      */         }
/*      */         
/* 3678 */         return fastDateCreate(null, shortVal, 1, 1).toString();
/*      */       }
/*      */       
/*      */ 
/* 3682 */       Date dt = getNativeDate(columnIndex);
/*      */       
/* 3684 */       if (dt == null) {
/* 3685 */         return null;
/*      */       }
/*      */       
/* 3688 */       return String.valueOf(dt);
/*      */     
/*      */     case 92: 
/* 3691 */       Time tm = getNativeTime(columnIndex, null, this.defaultTimeZone, false);
/*      */       
/* 3693 */       if (tm == null) {
/* 3694 */         return null;
/*      */       }
/*      */       
/* 3697 */       return String.valueOf(tm);
/*      */     
/*      */     case 93: 
/* 3700 */       Timestamp tstamp = getNativeTimestamp(columnIndex, null, this.defaultTimeZone, false);
/*      */       
/*      */ 
/* 3703 */       if (tstamp == null) {
/* 3704 */         return null;
/*      */       }
/*      */       
/* 3707 */       String result = String.valueOf(tstamp);
/*      */       
/* 3709 */       if (!this.connection.getNoDatetimeStringSync()) {
/* 3710 */         return result;
/*      */       }
/*      */       
/* 3713 */       if (result.endsWith(".0")) {
/* 3714 */         return result.substring(0, result.length() - 2);
/*      */       }
/*      */       break;
/*      */     }
/* 3718 */     return extractStringFromNativeColumn(columnIndex, mysqlType);
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
/*      */   protected Date getNativeDate(int columnIndex)
/*      */     throws SQLException
/*      */   {
/* 3734 */     return getNativeDate(columnIndex, null);
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
/*      */   protected Date getNativeDate(int columnIndex, TimeZone tz)
/*      */     throws SQLException
/*      */   {
/* 3755 */     checkRowPos();
/* 3756 */     checkColumnBounds(columnIndex);
/*      */     
/* 3758 */     int mysqlType = this.fields[(columnIndex - 1)].getMysqlType();
/*      */     
/* 3760 */     if (mysqlType == 10) {
/* 3761 */       byte[] bits = (byte[])this.thisRow[(columnIndex - 1)];
/*      */       
/* 3763 */       if (bits == null) {
/* 3764 */         this.wasNullFlag = true;
/*      */         
/* 3766 */         return null;
/*      */       }
/*      */       
/* 3769 */       this.wasNullFlag = false;
/*      */       
/* 3771 */       Date dateToReturn = null;
/*      */       
/* 3773 */       int year = 0;
/* 3774 */       int month = 0;
/* 3775 */       int day = 0;
/*      */       
/* 3777 */       int hour = 0;
/* 3778 */       int minute = 0;
/* 3779 */       int seconds = 0;
/*      */       
/* 3781 */       if (bits.length != 0) {
/* 3782 */         year = bits[0] & 0xFF | (bits[1] & 0xFF) << 8;
/*      */         
/* 3784 */         month = bits[2];
/* 3785 */         day = bits[3];
/*      */       }
/*      */       
/* 3788 */       if ((year == 0) && (month == 0) && (day == 0)) {
/* 3789 */         if ("convertToNull".equals(this.connection.getZeroDateTimeBehavior()))
/*      */         {
/* 3791 */           this.wasNullFlag = true;
/*      */           
/* 3793 */           return null; }
/* 3794 */         if ("exception".equals(this.connection.getZeroDateTimeBehavior()))
/*      */         {
/* 3796 */           throw SQLError.createSQLException("Value '0000-00-00' can not be represented as java.sql.Date", "S1009");
/*      */         }
/*      */         
/*      */ 
/*      */ 
/* 3801 */         year = 1;
/* 3802 */         month = 1;
/* 3803 */         day = 1;
/*      */       }
/*      */       
/* 3806 */       return fastDateCreate(getCalendarInstanceForSessionOrNew(), year, month, day);
/*      */     }
/*      */     
/*      */ 
/* 3810 */     boolean rollForward = (tz != null) && (!tz.equals(getDefaultTimeZone()));
/*      */     
/* 3812 */     return (Date)getNativeDateTimeValue(columnIndex, null, 91, mysqlType, tz, rollForward);
/*      */   }
/*      */   
/*      */   private Date getNativeDateViaParseConversion(int columnIndex) throws SQLException
/*      */   {
/* 3817 */     if (this.useUsageAdvisor) {
/* 3818 */       issueConversionViaParsingWarning("getDate()", columnIndex, this.thisRow[(columnIndex - 1)], this.fields[(columnIndex - 1)], new int[] { 10 });
/*      */     }
/*      */     
/*      */ 
/*      */ 
/* 3823 */     String stringVal = getNativeString(columnIndex);
/*      */     
/* 3825 */     return getDateFromString(stringVal, columnIndex);
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
/*      */   protected double getNativeDouble(int columnIndex)
/*      */     throws SQLException
/*      */   {
/* 3840 */     checkRowPos();
/* 3841 */     checkColumnBounds(columnIndex);
/*      */     
/* 3843 */     columnIndex--;
/*      */     
/* 3845 */     if (this.thisRow[columnIndex] == null) {
/* 3846 */       this.wasNullFlag = true;
/*      */       
/* 3848 */       return 0.0D;
/*      */     }
/*      */     
/* 3851 */     this.wasNullFlag = false;
/*      */     
/* 3853 */     Field f = this.fields[columnIndex];
/*      */     
/* 3855 */     switch (f.getMysqlType()) {
/*      */     case 5: 
/* 3857 */       byte[] bits = (byte[])this.thisRow[columnIndex];
/*      */       
/* 3859 */       long valueAsLong = bits[0] & 0xFF | (bits[1] & 0xFF) << 8 | (bits[2] & 0xFF) << 16 | (bits[3] & 0xFF) << 24 | (bits[4] & 0xFF) << 32 | (bits[5] & 0xFF) << 40 | (bits[6] & 0xFF) << 48 | (bits[7] & 0xFF) << 56;
/*      */       
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 3868 */       return Double.longBitsToDouble(valueAsLong);
/*      */     case 1: 
/* 3870 */       if (!f.isUnsigned()) {
/* 3871 */         return getNativeByte(columnIndex + 1);
/*      */       }
/*      */       
/* 3874 */       return getNativeShort(columnIndex + 1);
/*      */     case 2: 
/*      */     case 13: 
/* 3877 */       if (!f.isUnsigned()) {
/* 3878 */         return getNativeShort(columnIndex + 1);
/*      */       }
/*      */       
/* 3881 */       return getNativeInt(columnIndex + 1);
/*      */     case 3: 
/*      */     case 9: 
/* 3884 */       if (!f.isUnsigned()) {
/* 3885 */         return getNativeInt(columnIndex + 1);
/*      */       }
/*      */       
/* 3888 */       return getNativeLong(columnIndex + 1);
/*      */     case 8: 
/* 3890 */       long valueAsLong = getNativeLong(columnIndex + 1);
/*      */       
/* 3892 */       if (!f.isUnsigned()) {
/* 3893 */         return valueAsLong;
/*      */       }
/*      */       
/* 3896 */       BigInteger asBigInt = convertLongToUlong(valueAsLong);
/*      */       
/*      */ 
/*      */ 
/* 3900 */       return asBigInt.doubleValue();
/*      */     case 4: 
/* 3902 */       return getNativeFloat(columnIndex + 1);
/*      */     case 16: 
/* 3904 */       return getNumericRepresentationOfSQLBitType(columnIndex + 1);
/*      */     }
/*      */     
/* 3907 */     if (this.useUsageAdvisor) {
/* 3908 */       issueConversionViaParsingWarning("getDouble()", columnIndex, this.thisRow[columnIndex], this.fields[columnIndex], new int[] { 5, 1, 2, 3, 8, 4 });
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 3918 */     String stringVal = getNativeString(columnIndex + 1);
/*      */     
/* 3920 */     return getDoubleFromString(stringVal, columnIndex + 1);
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
/*      */   protected float getNativeFloat(int columnIndex)
/*      */     throws SQLException
/*      */   {
/* 3936 */     checkRowPos();
/* 3937 */     checkColumnBounds(columnIndex);
/*      */     
/* 3939 */     columnIndex--;
/*      */     
/* 3941 */     if (this.thisRow[columnIndex] == null) {
/* 3942 */       this.wasNullFlag = true;
/*      */       
/* 3944 */       return 0.0F;
/*      */     }
/*      */     
/* 3947 */     this.wasNullFlag = false;
/*      */     
/* 3949 */     Field f = this.fields[columnIndex];
/*      */     
/* 3951 */     switch (f.getMysqlType()) {
/*      */     case 16: 
/* 3953 */       long valueAsLong = getNumericRepresentationOfSQLBitType(columnIndex + 1);
/*      */       
/* 3955 */       return (float)valueAsLong;
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     case 5: 
/* 3962 */       Double valueAsDouble = new Double(getNativeDouble(columnIndex + 1));
/*      */       
/* 3964 */       float valueAsFloat = valueAsDouble.floatValue();
/*      */       
/* 3966 */       if (((this.connection.getJdbcCompliantTruncationForReads()) && (valueAsFloat == Float.NEGATIVE_INFINITY)) || (valueAsFloat == Float.POSITIVE_INFINITY))
/*      */       {
/*      */ 
/* 3969 */         throwRangeException(valueAsDouble.toString(), columnIndex + 1, 6);
/*      */       }
/*      */       
/*      */ 
/* 3973 */       return (float)getNativeDouble(columnIndex + 1);
/*      */     case 1: 
/* 3975 */       if (!f.isUnsigned()) {
/* 3976 */         return getNativeByte(columnIndex + 1);
/*      */       }
/*      */       
/* 3979 */       return getNativeShort(columnIndex + 1);
/*      */     case 2: 
/*      */     case 13: 
/* 3982 */       if (!f.isUnsigned()) {
/* 3983 */         return getNativeShort(columnIndex + 1);
/*      */       }
/*      */       
/* 3986 */       return getNativeInt(columnIndex + 1);
/*      */     case 3: 
/*      */     case 9: 
/* 3989 */       if (!f.isUnsigned()) {
/* 3990 */         return getNativeInt(columnIndex + 1);
/*      */       }
/*      */       
/* 3993 */       return (float)getNativeLong(columnIndex + 1);
/*      */     case 8: 
/* 3995 */       long valueAsLong = getNativeLong(columnIndex + 1);
/*      */       
/* 3997 */       if (!f.isUnsigned()) {
/* 3998 */         return (float)valueAsLong;
/*      */       }
/*      */       
/* 4001 */       BigInteger asBigInt = convertLongToUlong(valueAsLong);
/*      */       
/*      */ 
/*      */ 
/* 4005 */       return asBigInt.floatValue();
/*      */     case 4: 
/* 4007 */       byte[] bits = (byte[])this.thisRow[columnIndex];
/*      */       
/* 4009 */       int asInt = bits[0] & 0xFF | (bits[1] & 0xFF) << 8 | (bits[2] & 0xFF) << 16 | (bits[3] & 0xFF) << 24;
/*      */       
/*      */ 
/* 4012 */       return Float.intBitsToFloat(asInt);
/*      */     }
/*      */     
/*      */     
/* 4016 */     if (this.useUsageAdvisor) {
/* 4017 */       issueConversionViaParsingWarning("getFloat()", columnIndex, this.thisRow[columnIndex], this.fields[columnIndex], new int[] { 5, 1, 2, 3, 8, 4 });
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 4027 */     String stringVal = getNativeString(columnIndex + 1);
/*      */     
/* 4029 */     return getFloatFromString(stringVal, columnIndex + 1);
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
/*      */   protected int getNativeInt(int columnIndex)
/*      */     throws SQLException
/*      */   {
/* 4045 */     return getNativeInt(columnIndex, true);
/*      */   }
/*      */   
/*      */   protected int getNativeInt(int columnIndex, boolean overflowCheck) throws SQLException {
/* 4049 */     checkRowPos();
/* 4050 */     checkColumnBounds(columnIndex);
/*      */     
/* 4052 */     columnIndex--;
/*      */     
/* 4054 */     if (this.thisRow[columnIndex] == null) {
/* 4055 */       this.wasNullFlag = true;
/*      */       
/* 4057 */       return 0;
/*      */     }
/*      */     
/* 4060 */     this.wasNullFlag = false;
/*      */     
/* 4062 */     Field f = this.fields[columnIndex];
/*      */     
/* 4064 */     switch (f.getMysqlType()) {
/*      */     case 16: 
/* 4066 */       long valueAsLong = getNumericRepresentationOfSQLBitType(columnIndex + 1);
/*      */       
/* 4068 */       if ((overflowCheck) && (this.connection.getJdbcCompliantTruncationForReads()) && ((valueAsLong < -2147483648L) || (valueAsLong > 2147483647L)))
/*      */       {
/*      */ 
/* 4071 */         throwRangeException(String.valueOf(valueAsLong), columnIndex + 1, 4);
/*      */       }
/*      */       
/*      */ 
/* 4075 */       return (short)(int)valueAsLong;
/*      */     case 1: 
/* 4077 */       byte tinyintVal = getNativeByte(columnIndex + 1, false);
/*      */       
/* 4079 */       if ((!f.isUnsigned()) || (tinyintVal >= 0)) {
/* 4080 */         return tinyintVal;
/*      */       }
/*      */       
/* 4083 */       return tinyintVal + 256;
/*      */     case 2: 
/*      */     case 13: 
/* 4086 */       short asShort = getNativeShort(columnIndex + 1, false);
/*      */       
/* 4088 */       if ((!f.isUnsigned()) || (asShort >= 0)) {
/* 4089 */         return asShort;
/*      */       }
/*      */       
/* 4092 */       return asShort + 65536;
/*      */     case 3: 
/*      */     case 9: 
/* 4095 */       byte[] bits = (byte[])this.thisRow[columnIndex];
/*      */       
/* 4097 */       int valueAsInt = bits[0] & 0xFF | (bits[1] & 0xFF) << 8 | (bits[2] & 0xFF) << 16 | (bits[3] & 0xFF) << 24;
/*      */       
/*      */ 
/* 4100 */       if (!f.isUnsigned()) {
/* 4101 */         return valueAsInt;
/*      */       }
/*      */       
/* 4104 */       long valueAsLong = valueAsInt >= 0 ? valueAsInt : valueAsInt + 4294967296L;
/*      */       
/*      */ 
/* 4107 */       if ((overflowCheck) && (this.connection.getJdbcCompliantTruncationForReads()) && (valueAsLong > 2147483647L))
/*      */       {
/* 4109 */         throwRangeException(String.valueOf(valueAsLong), columnIndex + 1, 4);
/*      */       }
/*      */       
/*      */ 
/* 4113 */       return (int)valueAsLong;
/*      */     case 8: 
/* 4115 */       long valueAsLong = getNativeLong(columnIndex + 1, false, true);
/*      */       
/* 4117 */       if ((overflowCheck) && (this.connection.getJdbcCompliantTruncationForReads()) && (
/* 4118 */         (valueAsLong < -2147483648L) || (valueAsLong > 2147483647L)))
/*      */       {
/* 4120 */         throwRangeException(String.valueOf(valueAsLong), columnIndex + 1, 4);
/*      */       }
/*      */       
/*      */ 
/*      */ 
/* 4125 */       return (int)valueAsLong;
/*      */     case 5: 
/* 4127 */       double valueAsDouble = getNativeDouble(columnIndex + 1);
/*      */       
/* 4129 */       if ((overflowCheck) && (this.connection.getJdbcCompliantTruncationForReads()) && (
/* 4130 */         (valueAsDouble < -2.147483648E9D) || (valueAsDouble > 2.147483647E9D)))
/*      */       {
/* 4132 */         throwRangeException(String.valueOf(valueAsDouble), columnIndex + 1, 4);
/*      */       }
/*      */       
/*      */ 
/*      */ 
/* 4137 */       return (int)valueAsDouble;
/*      */     case 4: 
/* 4139 */       double valueAsDouble = getNativeFloat(columnIndex + 1);
/*      */       
/* 4141 */       if ((overflowCheck) && (this.connection.getJdbcCompliantTruncationForReads()) && (
/* 4142 */         (valueAsDouble < -2.147483648E9D) || (valueAsDouble > 2.147483647E9D)))
/*      */       {
/* 4144 */         throwRangeException(String.valueOf(valueAsDouble), columnIndex + 1, 4);
/*      */       }
/*      */       
/*      */ 
/*      */ 
/* 4149 */       return (int)valueAsDouble;
/*      */     }
/*      */     
/*      */     
/* 4153 */     if (this.useUsageAdvisor) {
/* 4154 */       issueConversionViaParsingWarning("getInt()", columnIndex, this.thisRow[columnIndex], this.fields[columnIndex], new int[] { 5, 1, 2, 3, 8, 4 });
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 4164 */     String stringVal = getNativeString(columnIndex + 1);
/*      */     
/* 4166 */     return getIntFromString(stringVal, columnIndex + 1);
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
/*      */   protected long getNativeLong(int columnIndex)
/*      */     throws SQLException
/*      */   {
/* 4182 */     return getNativeLong(columnIndex, true, true);
/*      */   }
/*      */   
/*      */   protected long getNativeLong(int columnIndex, boolean overflowCheck, boolean expandUnsignedLong) throws SQLException
/*      */   {
/* 4187 */     checkRowPos();
/* 4188 */     checkColumnBounds(columnIndex);
/*      */     
/* 4190 */     columnIndex--;
/*      */     
/* 4192 */     if (this.thisRow[columnIndex] == null) {
/* 4193 */       this.wasNullFlag = true;
/*      */       
/* 4195 */       return 0L;
/*      */     }
/*      */     
/* 4198 */     this.wasNullFlag = false;
/*      */     
/* 4200 */     Field f = this.fields[columnIndex];
/*      */     
/* 4202 */     switch (f.getMysqlType()) {
/*      */     case 16: 
/* 4204 */       return getNumericRepresentationOfSQLBitType(columnIndex + 1);
/*      */     case 1: 
/* 4206 */       if (!f.isUnsigned()) {
/* 4207 */         return getNativeByte(columnIndex + 1);
/*      */       }
/*      */       
/* 4210 */       return getNativeInt(columnIndex + 1);
/*      */     case 2: 
/* 4212 */       if (!f.isUnsigned()) {
/* 4213 */         return getNativeShort(columnIndex + 1);
/*      */       }
/*      */       
/* 4216 */       return getNativeInt(columnIndex + 1, false);
/*      */     
/*      */     case 13: 
/* 4219 */       return getNativeShort(columnIndex + 1);
/*      */     case 3: 
/*      */     case 9: 
/* 4222 */       int asInt = getNativeInt(columnIndex + 1, false);
/*      */       
/* 4224 */       if ((!f.isUnsigned()) || (asInt >= 0)) {
/* 4225 */         return asInt;
/*      */       }
/*      */       
/* 4228 */       return asInt + 4294967296L;
/*      */     
/*      */     case 8: 
/* 4231 */       byte[] bits = (byte[])this.thisRow[columnIndex];
/*      */       
/* 4233 */       long valueAsLong = bits[0] & 0xFF | (bits[1] & 0xFF) << 8 | (bits[2] & 0xFF) << 16 | (bits[3] & 0xFF) << 24 | (bits[4] & 0xFF) << 32 | (bits[5] & 0xFF) << 40 | (bits[6] & 0xFF) << 48 | (bits[7] & 0xFF) << 56;
/*      */       
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 4242 */       if ((!f.isUnsigned()) || (!expandUnsignedLong)) {
/* 4243 */         return valueAsLong;
/*      */       }
/*      */       
/* 4246 */       BigInteger asBigInt = convertLongToUlong(valueAsLong);
/*      */       
/* 4248 */       if ((overflowCheck) && (this.connection.getJdbcCompliantTruncationForReads()) && ((asBigInt.compareTo(new BigInteger(String.valueOf(Long.MAX_VALUE))) > 0) || (asBigInt.compareTo(new BigInteger(String.valueOf(Long.MIN_VALUE))) < 0)))
/*      */       {
/*      */ 
/* 4251 */         throwRangeException(asBigInt.toString(), columnIndex + 1, -5);
/*      */       }
/*      */       
/*      */ 
/* 4255 */       return getLongFromString(asBigInt.toString(), columnIndex + 1);
/*      */     
/*      */     case 5: 
/* 4258 */       double valueAsDouble = getNativeDouble(columnIndex + 1);
/*      */       
/* 4260 */       if ((overflowCheck) && (this.connection.getJdbcCompliantTruncationForReads()) && (
/* 4261 */         (valueAsDouble < -9.223372036854776E18D) || (valueAsDouble > 9.223372036854776E18D)))
/*      */       {
/* 4263 */         throwRangeException(String.valueOf(valueAsDouble), columnIndex + 1, -5);
/*      */       }
/*      */       
/*      */ 
/*      */ 
/* 4268 */       return valueAsDouble;
/*      */     case 4: 
/* 4270 */       double valueAsDouble = getNativeFloat(columnIndex + 1);
/*      */       
/* 4272 */       if ((overflowCheck) && (this.connection.getJdbcCompliantTruncationForReads()) && (
/* 4273 */         (valueAsDouble < -9.223372036854776E18D) || (valueAsDouble > 9.223372036854776E18D)))
/*      */       {
/* 4275 */         throwRangeException(String.valueOf(valueAsDouble), columnIndex + 1, -5);
/*      */       }
/*      */       
/*      */ 
/*      */ 
/* 4280 */       return valueAsDouble;
/*      */     }
/*      */     
/* 4283 */     if (this.useUsageAdvisor) {
/* 4284 */       issueConversionViaParsingWarning("getLong()", columnIndex, this.thisRow[columnIndex], this.fields[columnIndex], new int[] { 5, 1, 2, 3, 8, 4 });
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 4294 */     String stringVal = getNativeString(columnIndex + 1);
/*      */     
/* 4296 */     return getLongFromString(stringVal, columnIndex + 1);
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
/*      */   protected Ref getNativeRef(int i)
/*      */     throws SQLException
/*      */   {
/* 4314 */     throw new NotImplemented();
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
/*      */   protected short getNativeShort(int columnIndex)
/*      */     throws SQLException
/*      */   {
/* 4329 */     return getNativeShort(columnIndex, true);
/*      */   }
/*      */   
/*      */   protected short getNativeShort(int columnIndex, boolean overflowCheck) throws SQLException {
/* 4333 */     checkRowPos();
/* 4334 */     checkColumnBounds(columnIndex);
/*      */     
/* 4336 */     columnIndex--;
/*      */     
/* 4338 */     if (this.thisRow[columnIndex] == null) {
/* 4339 */       this.wasNullFlag = true;
/*      */       
/* 4341 */       return 0;
/*      */     }
/*      */     
/* 4344 */     this.wasNullFlag = false;
/*      */     
/* 4346 */     Field f = this.fields[columnIndex];
/*      */     
/* 4348 */     switch (f.getMysqlType())
/*      */     {
/*      */     case 1: 
/* 4351 */       byte tinyintVal = getNativeByte(columnIndex + 1, false);
/*      */       
/* 4353 */       if ((!f.isUnsigned()) || (tinyintVal >= 0)) {
/* 4354 */         return (short)tinyintVal;
/*      */       }
/*      */       
/* 4357 */       return (short)(tinyintVal + 256);
/*      */     case 2: 
/*      */     case 13: 
/* 4360 */       byte[] bits = (byte[])this.thisRow[columnIndex];
/*      */       
/* 4362 */       short asShort = (short)(bits[0] & 0xFF | (bits[1] & 0xFF) << 8);
/*      */       
/* 4364 */       if (!f.isUnsigned()) {
/* 4365 */         return asShort;
/*      */       }
/*      */       
/* 4368 */       int valueAsInt = asShort & 0xFFFF;
/*      */       
/* 4370 */       if ((overflowCheck) && (this.connection.getJdbcCompliantTruncationForReads()) && (valueAsInt > 32767))
/*      */       {
/* 4372 */         throwRangeException(String.valueOf(valueAsInt), columnIndex + 1, 5);
/*      */       }
/*      */       
/*      */ 
/* 4376 */       return (short)valueAsInt;
/*      */     case 3: 
/*      */     case 9: 
/* 4379 */       if (!f.isUnsigned()) {
/* 4380 */         int valueAsInt = getNativeInt(columnIndex + 1, false);
/*      */         
/* 4382 */         if (((overflowCheck) && (this.connection.getJdbcCompliantTruncationForReads()) && (valueAsInt > 32767)) || (valueAsInt < 32768))
/*      */         {
/*      */ 
/* 4385 */           throwRangeException(String.valueOf(valueAsInt), columnIndex + 1, 5);
/*      */         }
/*      */         
/*      */ 
/* 4389 */         return (short)valueAsInt;
/*      */       }
/*      */       
/* 4392 */       long valueAsLong = getNativeLong(columnIndex + 1, false, true);
/*      */       
/* 4394 */       if ((overflowCheck) && (this.connection.getJdbcCompliantTruncationForReads()) && (valueAsLong > 32767L))
/*      */       {
/* 4396 */         throwRangeException(String.valueOf(valueAsLong), columnIndex + 1, 5);
/*      */       }
/*      */       
/*      */ 
/* 4400 */       return (short)(int)valueAsLong;
/*      */     
/*      */     case 8: 
/* 4403 */       long valueAsLong = getNativeLong(columnIndex + 1, false, false);
/*      */       
/* 4405 */       if (!f.isUnsigned()) {
/* 4406 */         if ((overflowCheck) && (this.connection.getJdbcCompliantTruncationForReads()) && (
/* 4407 */           (valueAsLong < -32768L) || (valueAsLong > 32767L)))
/*      */         {
/* 4409 */           throwRangeException(String.valueOf(valueAsLong), columnIndex + 1, 5);
/*      */         }
/*      */         
/*      */ 
/*      */ 
/* 4414 */         return (short)(int)valueAsLong;
/*      */       }
/*      */       
/* 4417 */       BigInteger asBigInt = convertLongToUlong(valueAsLong);
/*      */       
/* 4419 */       if ((overflowCheck) && (this.connection.getJdbcCompliantTruncationForReads()) && ((asBigInt.compareTo(new BigInteger(String.valueOf(32767))) > 0) || (asBigInt.compareTo(new BigInteger(String.valueOf(32768))) < 0)))
/*      */       {
/*      */ 
/* 4422 */         throwRangeException(asBigInt.toString(), columnIndex + 1, 5);
/*      */       }
/*      */       
/*      */ 
/* 4426 */       return (short)getIntFromString(asBigInt.toString(), columnIndex + 1);
/*      */     
/*      */     case 5: 
/* 4429 */       double valueAsDouble = getNativeDouble(columnIndex + 1);
/*      */       
/* 4431 */       if ((overflowCheck) && (this.connection.getJdbcCompliantTruncationForReads()) && (
/* 4432 */         (valueAsDouble < -32768.0D) || (valueAsDouble > 32767.0D)))
/*      */       {
/* 4434 */         throwRangeException(String.valueOf(valueAsDouble), columnIndex + 1, 5);
/*      */       }
/*      */       
/*      */ 
/*      */ 
/* 4439 */       return (short)(int)valueAsDouble;
/*      */     case 4: 
/* 4441 */       float valueAsFloat = getNativeFloat(columnIndex + 1);
/*      */       
/* 4443 */       if ((overflowCheck) && (this.connection.getJdbcCompliantTruncationForReads()) && (
/* 4444 */         (valueAsFloat < -32768.0F) || (valueAsFloat > 32767.0F)))
/*      */       {
/* 4446 */         throwRangeException(String.valueOf(valueAsFloat), columnIndex + 1, 5);
/*      */       }
/*      */       
/*      */ 
/*      */ 
/* 4451 */       return (short)(int)valueAsFloat;
/*      */     }
/*      */     
/* 4454 */     if (this.useUsageAdvisor) {
/* 4455 */       issueConversionViaParsingWarning("getShort()", columnIndex, this.thisRow[columnIndex], this.fields[columnIndex], new int[] { 5, 1, 2, 3, 8, 4 });
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 4465 */     String stringVal = getNativeString(columnIndex + 1);
/*      */     
/* 4467 */     return getShortFromString(stringVal, columnIndex + 1);
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
/*      */   protected String getNativeString(int columnIndex)
/*      */     throws SQLException
/*      */   {
/* 4483 */     checkRowPos();
/* 4484 */     checkColumnBounds(columnIndex);
/*      */     
/* 4486 */     if (this.fields == null) {
/* 4487 */       throw SQLError.createSQLException(Messages.getString("ResultSet.Query_generated_no_fields_for_ResultSet_133"), "S1002");
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 4494 */     if (this.thisRow[(columnIndex - 1)] == null) {
/* 4495 */       this.wasNullFlag = true;
/*      */       
/* 4497 */       return null;
/*      */     }
/*      */     
/* 4500 */     this.wasNullFlag = false;
/*      */     
/* 4502 */     String stringVal = null;
/*      */     
/* 4504 */     if ((this.thisRow[(columnIndex - 1)] instanceof String)) {
/* 4505 */       return (String)this.thisRow[(columnIndex - 1)];
/*      */     }
/*      */     
/* 4508 */     Field field = this.fields[(columnIndex - 1)];
/*      */     
/*      */ 
/* 4511 */     stringVal = getNativeConvertToString(columnIndex, field);
/*      */     
/* 4513 */     if ((field.isZeroFill()) && (stringVal != null)) {
/* 4514 */       int origLength = stringVal.length();
/*      */       
/* 4516 */       StringBuffer zeroFillBuf = new StringBuffer(origLength);
/*      */       
/* 4518 */       long numZeros = field.getLength() - origLength;
/*      */       
/* 4520 */       for (long i = 0L; i < numZeros; i += 1L) {
/* 4521 */         zeroFillBuf.append('0');
/*      */       }
/*      */       
/* 4524 */       zeroFillBuf.append(stringVal);
/*      */       
/* 4526 */       stringVal = zeroFillBuf.toString();
/*      */     }
/*      */     
/* 4529 */     return stringVal;
/*      */   }
/*      */   
/*      */ 
/*      */   private Time getNativeTime(int columnIndex, Calendar targetCalendar, TimeZone tz, boolean rollForward)
/*      */     throws SQLException
/*      */   {
/* 4536 */     checkRowPos();
/* 4537 */     checkColumnBounds(columnIndex);
/*      */     
/* 4539 */     if (this.thisRow[(columnIndex - 1)] == null) {
/* 4540 */       this.wasNullFlag = true;
/*      */       
/* 4542 */       return null;
/*      */     }
/* 4544 */     this.wasNullFlag = false;
/*      */     
/*      */ 
/* 4547 */     int mysqlType = this.fields[(columnIndex - 1)].getMysqlType();
/*      */     
/* 4549 */     if (mysqlType == 11)
/*      */     {
/* 4551 */       byte[] bits = (byte[])this.thisRow[(columnIndex - 1)];
/*      */       
/* 4553 */       int length = bits.length;
/* 4554 */       int hour = 0;
/* 4555 */       int minute = 0;
/* 4556 */       int seconds = 0;
/*      */       
/* 4558 */       if (length != 0)
/*      */       {
/*      */ 
/* 4561 */         hour = bits[5];
/* 4562 */         minute = bits[6];
/* 4563 */         seconds = bits[7];
/*      */       }
/*      */       
/* 4566 */       Calendar sessionCalendar = getCalendarInstanceForSessionOrNew();
/*      */       
/* 4568 */       synchronized (sessionCalendar) {
/* 4569 */         Time time = TimeUtil.fastTimeCreate(sessionCalendar, hour, minute, seconds);
/*      */         
/*      */ 
/*      */ 
/* 4573 */         Time adjustedTime = TimeUtil.changeTimezone(this.connection, sessionCalendar, targetCalendar, time, this.connection.getServerTimezoneTZ(), tz, rollForward);
/*      */         
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 4579 */         return adjustedTime;
/*      */       }
/*      */     }
/*      */     
/* 4583 */     return (Time)getNativeDateTimeValue(columnIndex, targetCalendar, 92, mysqlType, tz, rollForward);
/*      */   }
/*      */   
/*      */ 
/*      */   private Time getNativeTimeViaParseConversion(int columnIndex, Calendar targetCalendar, TimeZone tz, boolean rollForward)
/*      */     throws SQLException
/*      */   {
/* 4590 */     if (this.useUsageAdvisor) {
/* 4591 */       issueConversionViaParsingWarning("getTime()", columnIndex, this.thisRow[(columnIndex - 1)], this.fields[(columnIndex - 1)], new int[] { 11 });
/*      */     }
/*      */     
/*      */ 
/*      */ 
/* 4596 */     String strTime = getNativeString(columnIndex);
/*      */     
/* 4598 */     return getTimeFromString(strTime, targetCalendar, columnIndex, tz, rollForward);
/*      */   }
/*      */   
/*      */ 
/*      */   private Timestamp getNativeTimestamp(int columnIndex, Calendar targetCalendar, TimeZone tz, boolean rollForward)
/*      */     throws SQLException
/*      */   {
/* 4605 */     checkRowPos();
/* 4606 */     checkColumnBounds(columnIndex);
/*      */     
/* 4608 */     if (this.thisRow[(columnIndex - 1)] == null) {
/* 4609 */       this.wasNullFlag = true;
/*      */       
/* 4611 */       return null;
/*      */     }
/*      */     
/* 4614 */     this.wasNullFlag = false;
/*      */     
/* 4616 */     int mysqlType = this.fields[(columnIndex - 1)].getMysqlType();
/*      */     
/* 4618 */     switch (mysqlType) {
/*      */     case 7: 
/*      */     case 12: 
/* 4621 */       byte[] bits = (byte[])this.thisRow[(columnIndex - 1)];
/*      */       
/* 4623 */       int length = bits.length;
/*      */       
/* 4625 */       int year = 0;
/* 4626 */       int month = 0;
/* 4627 */       int day = 0;
/*      */       
/* 4629 */       int hour = 0;
/* 4630 */       int minute = 0;
/* 4631 */       int seconds = 0;
/*      */       
/* 4633 */       int nanos = 0;
/*      */       
/* 4635 */       if (length != 0) {
/* 4636 */         year = bits[0] & 0xFF | (bits[1] & 0xFF) << 8;
/* 4637 */         month = bits[2];
/* 4638 */         day = bits[3];
/*      */         
/* 4640 */         if (length > 4) {
/* 4641 */           hour = bits[4];
/* 4642 */           minute = bits[5];
/* 4643 */           seconds = bits[6];
/*      */         }
/*      */         
/* 4646 */         if (length > 7) {
/* 4647 */           nanos = bits[7] & 0xFF | (bits[8] & 0xFF) << 8 | (bits[9] & 0xFF) << 16 | (bits[10] & 0xFF) << 24;
/*      */         }
/*      */       }
/*      */       
/*      */ 
/*      */ 
/* 4653 */       if ((year == 0) && (month == 0) && (day == 0)) {
/* 4654 */         if ("convertToNull".equals(this.connection.getZeroDateTimeBehavior()))
/*      */         {
/* 4656 */           this.wasNullFlag = true;
/*      */           
/* 4658 */           return null; }
/* 4659 */         if ("exception".equals(this.connection.getZeroDateTimeBehavior()))
/*      */         {
/* 4661 */           throw SQLError.createSQLException("Value '0000-00-00' can not be represented as java.sql.Timestamp", "S1009");
/*      */         }
/*      */         
/*      */ 
/*      */ 
/* 4666 */         year = 1;
/* 4667 */         month = 1;
/* 4668 */         day = 1;
/*      */       }
/*      */       
/* 4671 */       Calendar sessionCalendar = this.connection.getUseJDBCCompliantTimezoneShift() ? this.connection.getUtcCalendar() : getCalendarInstanceForSessionOrNew();
/*      */       
/*      */ 
/*      */ 
/* 4675 */       synchronized (sessionCalendar) {
/* 4676 */         Timestamp ts = fastTimestampCreate(sessionCalendar, year, month, day, hour, minute, seconds, nanos);
/*      */         
/*      */ 
/*      */ 
/* 4680 */         Timestamp adjustedTs = TimeUtil.changeTimezone(this.connection, sessionCalendar, targetCalendar, ts, this.connection.getServerTimezoneTZ(), tz, rollForward);
/*      */         
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 4686 */         return adjustedTs;
/*      */       }
/*      */     }
/*      */     
/* 4690 */     return (Timestamp)getNativeDateTimeValue(columnIndex, targetCalendar, 93, mysqlType, tz, rollForward);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   private Timestamp getNativeTimestampViaParseConversion(int columnIndex, Calendar targetCalendar, TimeZone tz, boolean rollForward)
/*      */     throws SQLException
/*      */   {
/* 4698 */     if (this.useUsageAdvisor) {
/* 4699 */       issueConversionViaParsingWarning("getTimestamp()", columnIndex, this.thisRow[(columnIndex - 1)], this.fields[(columnIndex - 1)], new int[] { 7, 12 });
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/* 4705 */     String strTimestamp = getNativeString(columnIndex);
/*      */     
/* 4707 */     return getTimestampFromString(columnIndex, targetCalendar, strTimestamp, tz, rollForward);
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
/*      */   protected InputStream getNativeUnicodeStream(int columnIndex)
/*      */     throws SQLException
/*      */   {
/* 4734 */     checkRowPos();
/*      */     
/* 4736 */     return getBinaryStream(columnIndex);
/*      */   }
/*      */   
/*      */ 
/*      */   protected URL getNativeURL(int colIndex)
/*      */     throws SQLException
/*      */   {
/* 4743 */     String val = getString(colIndex);
/*      */     
/* 4745 */     if (val == null) {
/* 4746 */       return null;
/*      */     }
/*      */     try
/*      */     {
/* 4750 */       return new URL(val);
/*      */     } catch (MalformedURLException mfe) {
/* 4752 */       throw SQLError.createSQLException(Messages.getString("ResultSet.Malformed_URL____141") + val + "'", "S1009");
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected ResultSet getNextResultSet()
/*      */   {
/* 4764 */     return this.nextResultSet;
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
/*      */   public Object getObject(int columnIndex)
/*      */     throws SQLException
/*      */   {
/* 4791 */     checkRowPos();
/* 4792 */     checkColumnBounds(columnIndex);
/*      */     
/* 4794 */     if (this.thisRow[(columnIndex - 1)] == null) {
/* 4795 */       this.wasNullFlag = true;
/*      */       
/* 4797 */       return null;
/*      */     }
/*      */     
/* 4800 */     this.wasNullFlag = false;
/*      */     
/*      */ 
/* 4803 */     Field field = this.fields[(columnIndex - 1)];
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 4811 */     if ((this.isBinaryEncoded) && (!(this.thisRow[(columnIndex - 1)] instanceof byte[])))
/*      */     {
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 4819 */       if ((field.getSQLType() == -7) && (field.getLength() > 0L))
/*      */       {
/*      */ 
/*      */ 
/* 4823 */         return new Boolean(getBoolean(columnIndex));
/*      */       }
/*      */       
/* 4826 */       Object columnValue = this.thisRow[(columnIndex - 1)];
/*      */       
/* 4828 */       if (columnValue == null) {
/* 4829 */         this.wasNullFlag = true;
/*      */         
/* 4831 */         return null;
/*      */       }
/*      */       
/* 4834 */       return columnValue;
/*      */     }
/*      */     
/* 4837 */     switch (field.getSQLType()) {
/*      */     case -7: 
/*      */     case 16: 
/* 4840 */       if ((field.getMysqlType() == 16) && (!field.isSingleBit()))
/*      */       {
/* 4842 */         return getBytes(columnIndex);
/*      */       }
/*      */       
/*      */ 
/*      */ 
/*      */ 
/* 4848 */       return new Boolean(getBoolean(columnIndex));
/*      */     
/*      */     case -6: 
/* 4851 */       if (!field.isUnsigned()) {
/* 4852 */         return new Integer(getByte(columnIndex));
/*      */       }
/*      */       
/* 4855 */       return new Integer(getInt(columnIndex));
/*      */     
/*      */ 
/*      */     case 5: 
/* 4859 */       return new Integer(getInt(columnIndex));
/*      */     
/*      */ 
/*      */     case 4: 
/* 4863 */       if ((!field.isUnsigned()) || (field.getMysqlType() == 9))
/*      */       {
/* 4865 */         return new Integer(getInt(columnIndex));
/*      */       }
/*      */       
/* 4868 */       return new Long(getLong(columnIndex));
/*      */     
/*      */ 
/*      */     case -5: 
/* 4872 */       if (!field.isUnsigned()) {
/* 4873 */         return new Long(getLong(columnIndex));
/*      */       }
/*      */       
/* 4876 */       String stringVal = getString(columnIndex);
/*      */       
/* 4878 */       if (stringVal == null) {
/* 4879 */         return null;
/*      */       }
/*      */       try
/*      */       {
/* 4883 */         return new BigInteger(stringVal);
/*      */       } catch (NumberFormatException nfe) {
/* 4885 */         throw SQLError.createSQLException(Messages.getString("ResultSet.Bad_format_for_BigInteger", new Object[] { new Integer(columnIndex), stringVal }), "S1009");
/*      */       }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */     case 2: 
/*      */     case 3: 
/* 4893 */       String stringVal = getString(columnIndex);
/*      */       
/*      */ 
/*      */ 
/* 4897 */       if (stringVal != null) {
/* 4898 */         if (stringVal.length() == 0) {
/* 4899 */           BigDecimal val = new BigDecimal(0.0D);
/*      */           
/* 4901 */           return val;
/*      */         }
/*      */         try
/*      */         {
/* 4905 */           val = new BigDecimal(stringVal);
/*      */         } catch (NumberFormatException ex) { BigDecimal val;
/* 4907 */           throw SQLError.createSQLException(Messages.getString("ResultSet.Bad_format_for_BigDecimal____86") + stringVal + Messages.getString("ResultSet.___in_column__87") + columnIndex + "(" + this.fields[(columnIndex - 1)] + ").", "S1009");
/*      */         }
/*      */         
/*      */ 
/*      */ 
/*      */ 
/*      */         BigDecimal val;
/*      */         
/*      */ 
/*      */ 
/*      */ 
/* 4918 */         return val;
/*      */       }
/*      */       
/* 4921 */       return null;
/*      */     
/*      */     case 7: 
/* 4924 */       return new Float(getFloat(columnIndex));
/*      */     
/*      */     case 6: 
/*      */     case 8: 
/* 4928 */       return new Double(getDouble(columnIndex));
/*      */     
/*      */     case 1: 
/*      */     case 12: 
/* 4932 */       if (!field.isOpaqueBinary()) {
/* 4933 */         return getString(columnIndex);
/*      */       }
/*      */       
/* 4936 */       return getBytes(columnIndex);
/*      */     case -1: 
/* 4938 */       if (!field.isOpaqueBinary()) {
/* 4939 */         return getStringForClob(columnIndex);
/*      */       }
/*      */       
/* 4942 */       return getBytes(columnIndex);
/*      */     
/*      */     case -4: 
/*      */     case -3: 
/*      */     case -2: 
/* 4947 */       if (field.getMysqlType() == 255)
/* 4948 */         return getBytes(columnIndex);
/* 4949 */       if ((field.isBinary()) || (field.isBlob())) {
/* 4950 */         byte[] data = getBytes(columnIndex);
/*      */         
/* 4952 */         if (this.connection.getAutoDeserialize()) {
/* 4953 */           Object obj = data;
/*      */           
/* 4955 */           if ((data != null) && (data.length >= 2)) {
/* 4956 */             if ((data[0] == -84) && (data[1] == -19)) {
/*      */               try
/*      */               {
/* 4959 */                 ByteArrayInputStream bytesIn = new ByteArrayInputStream(data);
/*      */                 
/* 4961 */                 ObjectInputStream objIn = new ObjectInputStream(bytesIn);
/*      */                 
/* 4963 */                 obj = objIn.readObject();
/* 4964 */                 objIn.close();
/* 4965 */                 bytesIn.close();
/*      */               } catch (ClassNotFoundException cnfe) {
/* 4967 */                 throw SQLError.createSQLException(Messages.getString("ResultSet.Class_not_found___91") + cnfe.toString() + Messages.getString("ResultSet._while_reading_serialized_object_92"));
/*      */ 
/*      */ 
/*      */               }
/*      */               catch (IOException ex)
/*      */               {
/*      */ 
/* 4974 */                 obj = data;
/*      */               }
/*      */             } else {
/* 4977 */               return getString(columnIndex);
/*      */             }
/*      */           }
/*      */           
/* 4981 */           return obj;
/*      */         }
/*      */         
/* 4984 */         return data;
/*      */       }
/*      */     
/*      */     case 91: 
/* 4988 */       if ((field.getMysqlType() == 13) && (!this.connection.getYearIsDateType()))
/*      */       {
/* 4990 */         return new Short(getShort(columnIndex));
/*      */       }
/*      */       
/* 4993 */       return getDate(columnIndex);
/*      */     
/*      */     case 92: 
/* 4996 */       return getTime(columnIndex);
/*      */     
/*      */     case 93: 
/* 4999 */       return getTimestamp(columnIndex);
/*      */     }
/*      */     
/* 5002 */     return getString(columnIndex);
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
/*      */   public Object getObject(int i, Map map)
/*      */     throws SQLException
/*      */   {
/* 5022 */     return getObject(i);
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
/*      */   public Object getObject(String columnName)
/*      */     throws SQLException
/*      */   {
/* 5049 */     return getObject(findColumn(columnName));
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
/*      */   public Object getObject(String colName, Map map)
/*      */     throws SQLException
/*      */   {
/* 5069 */     return getObject(findColumn(colName), map);
/*      */   }
/*      */   
/*      */   protected Object getObjectStoredProc(int columnIndex, int desiredSqlType) throws SQLException
/*      */   {
/* 5074 */     checkRowPos();
/* 5075 */     checkColumnBounds(columnIndex);
/*      */     
/* 5077 */     if (this.thisRow[(columnIndex - 1)] == null) {
/* 5078 */       this.wasNullFlag = true;
/*      */       
/* 5080 */       return null;
/*      */     }
/*      */     
/* 5083 */     this.wasNullFlag = false;
/*      */     
/*      */ 
/* 5086 */     Field field = this.fields[(columnIndex - 1)];
/*      */     
/* 5088 */     switch (desiredSqlType)
/*      */     {
/*      */ 
/*      */ 
/*      */     case -7: 
/*      */     case 16: 
/* 5094 */       return new Boolean(getBoolean(columnIndex));
/*      */     
/*      */     case -6: 
/* 5097 */       return new Integer(getInt(columnIndex));
/*      */     
/*      */     case 5: 
/* 5100 */       return new Integer(getInt(columnIndex));
/*      */     
/*      */ 
/*      */     case 4: 
/* 5104 */       if ((!field.isUnsigned()) || (field.getMysqlType() == 9))
/*      */       {
/* 5106 */         return new Integer(getInt(columnIndex));
/*      */       }
/*      */       
/* 5109 */       return new Long(getLong(columnIndex));
/*      */     
/*      */ 
/*      */     case -5: 
/* 5113 */       if (field.isUnsigned()) {
/* 5114 */         return getBigDecimal(columnIndex);
/*      */       }
/*      */       
/* 5117 */       return new Long(getLong(columnIndex));
/*      */     
/*      */ 
/*      */     case 2: 
/*      */     case 3: 
/* 5122 */       String stringVal = getString(columnIndex);
/*      */       
/*      */ 
/* 5125 */       if (stringVal != null) {
/* 5126 */         if (stringVal.length() == 0) {
/* 5127 */           BigDecimal val = new BigDecimal(0.0D);
/*      */           
/* 5129 */           return val;
/*      */         }
/*      */         try
/*      */         {
/* 5133 */           val = new BigDecimal(stringVal);
/*      */         } catch (NumberFormatException ex) { BigDecimal val;
/* 5135 */           throw SQLError.createSQLException(Messages.getString("ResultSet.Bad_format_for_BigDecimal____86") + stringVal + Messages.getString("ResultSet.___in_column__87") + columnIndex + "(" + this.fields[(columnIndex - 1)] + ").", "S1009");
/*      */         }
/*      */         
/*      */ 
/*      */ 
/*      */ 
/*      */         BigDecimal val;
/*      */         
/*      */ 
/*      */ 
/*      */ 
/* 5146 */         return val;
/*      */       }
/*      */       
/* 5149 */       return null;
/*      */     
/*      */     case 7: 
/* 5152 */       return new Float(getFloat(columnIndex));
/*      */     
/*      */ 
/*      */     case 6: 
/* 5156 */       if (!this.connection.getRunningCTS13()) {
/* 5157 */         return new Double(getFloat(columnIndex));
/*      */       }
/* 5159 */       return new Float(getFloat(columnIndex));
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     case 8: 
/* 5166 */       return new Double(getDouble(columnIndex));
/*      */     
/*      */     case 1: 
/*      */     case 12: 
/* 5170 */       return getString(columnIndex);
/*      */     case -1: 
/* 5172 */       return getStringForClob(columnIndex);
/*      */     case -4: 
/*      */     case -3: 
/*      */     case -2: 
/* 5176 */       return getBytes(columnIndex);
/*      */     
/*      */     case 91: 
/* 5179 */       if ((field.getMysqlType() == 13) && (!this.connection.getYearIsDateType()))
/*      */       {
/* 5181 */         return new Short(getShort(columnIndex));
/*      */       }
/*      */       
/* 5184 */       return getDate(columnIndex);
/*      */     
/*      */     case 92: 
/* 5187 */       return getTime(columnIndex);
/*      */     
/*      */     case 93: 
/* 5190 */       return getTimestamp(columnIndex);
/*      */     }
/*      */     
/* 5193 */     return getString(columnIndex);
/*      */   }
/*      */   
/*      */   protected Object getObjectStoredProc(int i, Map map, int desiredSqlType)
/*      */     throws SQLException
/*      */   {
/* 5199 */     return getObjectStoredProc(i, desiredSqlType);
/*      */   }
/*      */   
/*      */   protected Object getObjectStoredProc(String columnName, int desiredSqlType) throws SQLException
/*      */   {
/* 5204 */     return getObjectStoredProc(findColumn(columnName), desiredSqlType);
/*      */   }
/*      */   
/*      */   protected Object getObjectStoredProc(String colName, Map map, int desiredSqlType) throws SQLException
/*      */   {
/* 5209 */     return getObjectStoredProc(findColumn(colName), map, desiredSqlType);
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
/*      */   public Ref getRef(int i)
/*      */     throws SQLException
/*      */   {
/* 5226 */     checkColumnBounds(i);
/* 5227 */     throw new NotImplemented();
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
/*      */   public Ref getRef(String colName)
/*      */     throws SQLException
/*      */   {
/* 5244 */     return getRef(findColumn(colName));
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
/*      */   public int getRow()
/*      */     throws SQLException
/*      */   {
/* 5261 */     checkClosed();
/*      */     
/* 5263 */     int currentRowNumber = this.rowData.getCurrentRowNumber();
/* 5264 */     int row = 0;
/*      */     
/*      */ 
/*      */ 
/* 5268 */     if (!this.rowData.isDynamic()) {
/* 5269 */       if ((currentRowNumber < 0) || (this.rowData.isAfterLast()) || (this.rowData.isEmpty()))
/*      */       {
/* 5271 */         row = 0;
/*      */       } else {
/* 5273 */         row = currentRowNumber + 1;
/*      */       }
/*      */     }
/*      */     else {
/* 5277 */       row = currentRowNumber + 1;
/*      */     }
/*      */     
/* 5280 */     return row;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected String getServerInfo()
/*      */   {
/* 5289 */     return this.serverInfo;
/*      */   }
/*      */   
/*      */   private long getNumericRepresentationOfSQLBitType(int columnIndex) throws SQLException
/*      */   {
/* 5294 */     if ((this.fields[(columnIndex - 1)].isSingleBit()) || (((byte[])this.thisRow[(columnIndex - 1)]).length == 1))
/*      */     {
/* 5296 */       return ((byte[])this.thisRow[(columnIndex - 1)])[0];
/*      */     }
/*      */     
/*      */ 
/* 5300 */     byte[] asBytes = (byte[])this.thisRow[(columnIndex - 1)];
/*      */     
/*      */ 
/* 5303 */     int shift = 0;
/*      */     
/* 5305 */     long[] steps = new long[asBytes.length];
/*      */     
/* 5307 */     for (int i = asBytes.length - 1; i >= 0; i--) {
/* 5308 */       steps[i] = ((asBytes[i] & 0xFF) << shift);
/* 5309 */       shift += 8;
/*      */     }
/*      */     
/* 5312 */     long valueAsLong = 0L;
/*      */     
/* 5314 */     for (int i = 0; i < asBytes.length; i++) {
/* 5315 */       valueAsLong |= steps[i];
/*      */     }
/*      */     
/* 5318 */     return valueAsLong;
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
/*      */   public short getShort(int columnIndex)
/*      */     throws SQLException
/*      */   {
/* 5333 */     if (!this.isBinaryEncoded) {
/* 5334 */       checkRowPos();
/*      */       
/* 5336 */       if (this.connection.getUseFastIntParsing())
/*      */       {
/* 5338 */         checkColumnBounds(columnIndex);
/*      */         
/* 5340 */         if (this.thisRow[(columnIndex - 1)] == null) {
/* 5341 */           this.wasNullFlag = true;
/*      */         } else {
/* 5343 */           this.wasNullFlag = false;
/*      */         }
/*      */         
/* 5346 */         if (this.wasNullFlag) {
/* 5347 */           return 0;
/*      */         }
/*      */         
/* 5350 */         byte[] shortAsBytes = (byte[])this.thisRow[(columnIndex - 1)];
/*      */         
/* 5352 */         if (shortAsBytes.length == 0) {
/* 5353 */           return (short)convertToZeroWithEmptyCheck();
/*      */         }
/*      */         
/* 5356 */         boolean needsFullParse = false;
/*      */         
/* 5358 */         for (int i = 0; i < shortAsBytes.length; i++) {
/* 5359 */           if (((char)shortAsBytes[i] == 'e') || ((char)shortAsBytes[i] == 'E'))
/*      */           {
/* 5361 */             needsFullParse = true;
/*      */             
/* 5363 */             break;
/*      */           }
/*      */         }
/*      */         
/* 5367 */         if (!needsFullParse) {
/*      */           try {
/* 5369 */             return parseShortWithOverflowCheck(columnIndex, shortAsBytes, null);
/*      */           }
/*      */           catch (NumberFormatException nfe)
/*      */           {
/*      */             try {
/* 5374 */               return parseShortAsDouble(columnIndex, new String(shortAsBytes));
/*      */ 
/*      */             }
/*      */             catch (NumberFormatException newNfe)
/*      */             {
/*      */ 
/* 5380 */               if (this.fields[(columnIndex - 1)].getMysqlType() == 16) {
/* 5381 */                 long valueAsLong = getNumericRepresentationOfSQLBitType(columnIndex);
/*      */                 
/* 5383 */                 if ((this.connection.getJdbcCompliantTruncationForReads()) && ((valueAsLong < -32768L) || (valueAsLong > 32767L)))
/*      */                 {
/*      */ 
/* 5386 */                   throwRangeException(String.valueOf(valueAsLong), columnIndex, 5);
/*      */                 }
/*      */                 
/*      */ 
/* 5390 */                 return (short)(int)valueAsLong;
/*      */               }
/*      */               
/* 5393 */               throw SQLError.createSQLException(Messages.getString("ResultSet.Invalid_value_for_getShort()_-____96") + new String(shortAsBytes) + "'", "S1009");
/*      */             }
/*      */           }
/*      */         }
/*      */       }
/*      */       
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 5403 */       String val = null;
/*      */       try
/*      */       {
/* 5406 */         val = getString(columnIndex);
/*      */         
/* 5408 */         if (val != null)
/*      */         {
/* 5410 */           if (val.length() == 0) {
/* 5411 */             return (short)convertToZeroWithEmptyCheck();
/*      */           }
/*      */           
/* 5414 */           if ((val.indexOf("e") == -1) && (val.indexOf("E") == -1) && (val.indexOf(".") == -1))
/*      */           {
/* 5416 */             return parseShortWithOverflowCheck(columnIndex, null, val);
/*      */           }
/*      */           
/*      */ 
/*      */ 
/* 5421 */           return parseShortAsDouble(columnIndex, val);
/*      */         }
/*      */         
/* 5424 */         return 0;
/*      */       } catch (NumberFormatException nfe) {
/*      */         try {
/* 5427 */           return parseShortAsDouble(columnIndex, val);
/*      */ 
/*      */         }
/*      */         catch (NumberFormatException newNfe)
/*      */         {
/* 5432 */           if (this.fields[(columnIndex - 1)].getMysqlType() == 16) {
/* 5433 */             long valueAsLong = getNumericRepresentationOfSQLBitType(columnIndex);
/*      */             
/* 5435 */             if ((this.connection.getJdbcCompliantTruncationForReads()) && ((valueAsLong < -32768L) || (valueAsLong > 32767L)))
/*      */             {
/*      */ 
/* 5438 */               throwRangeException(String.valueOf(valueAsLong), columnIndex, 5);
/*      */             }
/*      */             
/*      */ 
/* 5442 */             return (short)(int)valueAsLong;
/*      */           }
/*      */           
/* 5445 */           throw SQLError.createSQLException(Messages.getString("ResultSet.Invalid_value_for_getShort()_-____96") + val + "'", "S1009");
/*      */         }
/*      */       }
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/* 5453 */     return getNativeShort(columnIndex);
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
/*      */   public short getShort(String columnName)
/*      */     throws SQLException
/*      */   {
/* 5468 */     return getShort(findColumn(columnName));
/*      */   }
/*      */   
/*      */   private final short getShortFromString(String val, int columnIndex) throws SQLException
/*      */   {
/*      */     try {
/* 5474 */       if (val != null)
/*      */       {
/* 5476 */         if (val.length() == 0) {
/* 5477 */           return (short)convertToZeroWithEmptyCheck();
/*      */         }
/*      */         
/* 5480 */         if ((val.indexOf("e") == -1) && (val.indexOf("E") == -1) && (val.indexOf(".") == -1))
/*      */         {
/* 5482 */           return parseShortWithOverflowCheck(columnIndex, null, val);
/*      */         }
/*      */         
/*      */ 
/* 5486 */         return parseShortAsDouble(columnIndex, val);
/*      */       }
/*      */       
/* 5489 */       return 0;
/*      */     } catch (NumberFormatException nfe) {
/*      */       try {
/* 5492 */         return parseShortAsDouble(columnIndex, val);
/*      */ 
/*      */       }
/*      */       catch (NumberFormatException newNfe)
/*      */       {
/* 5497 */         throw SQLError.createSQLException(Messages.getString("ResultSet.Invalid_value_for_getShort()_-____217") + val + Messages.getString("ResultSet.___in_column__218") + columnIndex, "S1009");
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
/*      */   public java.sql.Statement getStatement()
/*      */     throws SQLException
/*      */   {
/* 5516 */     if ((this.isClosed) && (!this.retainOwningStatement)) {
/* 5517 */       throw SQLError.createSQLException("Operation not allowed on closed ResultSet. Statements can be retained over result set closure by setting the connection property \"retainStatementAfterResultSetClose\" to \"true\".", "S1000");
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 5525 */     if (this.wrapperStatement != null) {
/* 5526 */       return this.wrapperStatement;
/*      */     }
/*      */     
/* 5529 */     return this.owningStatement;
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
/*      */   public String getString(int columnIndex)
/*      */     throws SQLException
/*      */   {
/* 5544 */     String stringVal = getStringInternal(columnIndex, true);
/*      */     
/* 5546 */     if ((stringVal != null) && (this.padCharsWithSpace)) {
/* 5547 */       Field f = this.fields[(columnIndex - 1)];
/*      */       
/* 5549 */       if (f.getMysqlType() == 254) {
/* 5550 */         int fieldLength = (int)f.getLength() / f.getMaxBytesPerCharacter();
/*      */         
/*      */ 
/* 5553 */         int currentLength = stringVal.length();
/*      */         
/* 5555 */         if (currentLength < fieldLength) {
/* 5556 */           StringBuffer paddedBuf = new StringBuffer(fieldLength);
/* 5557 */           paddedBuf.append(stringVal);
/*      */           
/* 5559 */           int difference = fieldLength - currentLength;
/*      */           
/* 5561 */           paddedBuf.append(EMPTY_SPACE, 0, difference);
/*      */           
/* 5563 */           stringVal = paddedBuf.toString();
/*      */         }
/*      */       }
/*      */     }
/*      */     
/* 5568 */     return stringVal;
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
/*      */   public String getString(String columnName)
/*      */     throws SQLException
/*      */   {
/* 5584 */     return getString(findColumn(columnName));
/*      */   }
/*      */   
/*      */   private String getStringForClob(int columnIndex) throws SQLException {
/* 5588 */     String asString = null;
/*      */     
/* 5590 */     String forcedEncoding = this.connection.getClobCharacterEncoding();
/*      */     
/*      */ 
/* 5593 */     if (forcedEncoding == null) {
/* 5594 */       if (!this.isBinaryEncoded) {
/* 5595 */         asString = getString(columnIndex);
/*      */       } else {
/* 5597 */         asString = getNativeString(columnIndex);
/*      */       }
/*      */     } else {
/*      */       try {
/* 5601 */         byte[] asBytes = null;
/*      */         
/* 5603 */         if (!this.isBinaryEncoded) {
/* 5604 */           asBytes = getBytes(columnIndex);
/*      */         } else {
/* 5606 */           asBytes = getNativeBytes(columnIndex, true);
/*      */         }
/*      */         
/* 5609 */         if (asBytes != null) {
/* 5610 */           asString = new String(asBytes, forcedEncoding);
/*      */         }
/*      */       } catch (UnsupportedEncodingException uee) {
/* 5613 */         throw SQLError.createSQLException("Unsupported character encoding " + forcedEncoding, "S1009");
/*      */       }
/*      */     }
/*      */     
/*      */ 
/* 5618 */     return asString;
/*      */   }
/*      */   
/*      */   protected String getStringInternal(int columnIndex, boolean checkDateTypes) throws SQLException
/*      */   {
/* 5623 */     if (!this.isBinaryEncoded) {
/* 5624 */       checkRowPos();
/* 5625 */       checkColumnBounds(columnIndex);
/*      */       
/* 5627 */       if (this.fields == null) {
/* 5628 */         throw SQLError.createSQLException(Messages.getString("ResultSet.Query_generated_no_fields_for_ResultSet_99"), "S1002");
/*      */       }
/*      */       
/*      */ 
/*      */ 
/*      */ 
/* 5634 */       if (this.thisRow[(columnIndex - 1)] == null) {
/* 5635 */         this.wasNullFlag = true;
/*      */         
/* 5637 */         return null;
/*      */       }
/*      */       
/* 5640 */       this.wasNullFlag = false;
/*      */       
/* 5642 */       String stringVal = null;
/* 5643 */       columnIndex--;
/*      */       
/* 5645 */       if (this.fields[columnIndex].getMysqlType() == 16) {
/* 5646 */         if (this.fields[columnIndex].isSingleBit()) {
/* 5647 */           byte[] asBytes = (byte[])this.thisRow[columnIndex];
/*      */           
/* 5649 */           if (asBytes.length == 0) {
/* 5650 */             return String.valueOf(convertToZeroWithEmptyCheck());
/*      */           }
/*      */           
/* 5653 */           return String.valueOf(asBytes[0]);
/*      */         }
/*      */         
/* 5656 */         return String.valueOf(getNumericRepresentationOfSQLBitType(columnIndex + 1));
/*      */       }
/*      */       
/* 5659 */       String encoding = this.fields[columnIndex].getCharacterSet();
/*      */       
/* 5661 */       if ((this.connection != null) && (this.connection.getUseUnicode())) {
/*      */         try {
/* 5663 */           if (encoding == null) {
/* 5664 */             stringVal = new String((byte[])this.thisRow[columnIndex]);
/*      */           }
/*      */           else {
/* 5667 */             SingleByteCharsetConverter converter = this.connection.getCharsetConverter(encoding);
/*      */             
/*      */ 
/* 5670 */             if (converter != null) {
/* 5671 */               stringVal = converter.toString((byte[])this.thisRow[columnIndex]);
/*      */             }
/*      */             else {
/* 5674 */               stringVal = new String((byte[])this.thisRow[columnIndex], encoding);
/*      */             }
/*      */           }
/*      */         }
/*      */         catch (UnsupportedEncodingException E)
/*      */         {
/* 5680 */           throw SQLError.createSQLException(Messages.getString("ResultSet.Unsupported_character_encoding____101") + encoding + "'.", "0S100");
/*      */         }
/*      */         
/*      */       }
/*      */       else
/*      */       {
/* 5686 */         stringVal = StringUtils.toAsciiString((byte[])this.thisRow[columnIndex]);
/*      */       }
/*      */       
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 5695 */       if (this.fields[columnIndex].getMysqlType() == 13) {
/* 5696 */         if (!this.connection.getYearIsDateType()) {
/* 5697 */           return stringVal;
/*      */         }
/*      */         
/* 5700 */         Date dt = getDateFromString(stringVal, columnIndex + 1);
/*      */         
/* 5702 */         if (dt == null) {
/* 5703 */           this.wasNullFlag = true;
/*      */           
/* 5705 */           return null;
/*      */         }
/*      */         
/* 5708 */         this.wasNullFlag = false;
/*      */         
/* 5710 */         return dt.toString();
/*      */       }
/*      */       
/*      */ 
/*      */ 
/* 5715 */       if ((checkDateTypes) && (!this.connection.getNoDatetimeStringSync())) {
/* 5716 */         switch (this.fields[columnIndex].getSQLType()) {
/*      */         case 92: 
/* 5718 */           Time tm = getTimeFromString(stringVal, null, columnIndex + 1, getDefaultTimeZone(), false);
/*      */           
/*      */ 
/* 5721 */           if (tm == null) {
/* 5722 */             this.wasNullFlag = true;
/*      */             
/* 5724 */             return null;
/*      */           }
/*      */           
/* 5727 */           this.wasNullFlag = false;
/*      */           
/* 5729 */           return tm.toString();
/*      */         
/*      */         case 91: 
/* 5732 */           Date dt = getDateFromString(stringVal, columnIndex + 1);
/*      */           
/* 5734 */           if (dt == null) {
/* 5735 */             this.wasNullFlag = true;
/*      */             
/* 5737 */             return null;
/*      */           }
/*      */           
/* 5740 */           this.wasNullFlag = false;
/*      */           
/* 5742 */           return dt.toString();
/*      */         case 93: 
/* 5744 */           Timestamp ts = getTimestampFromString(columnIndex + 1, null, stringVal, getDefaultTimeZone(), false);
/*      */           
/*      */ 
/* 5747 */           if (ts == null) {
/* 5748 */             this.wasNullFlag = true;
/*      */             
/* 5750 */             return null;
/*      */           }
/*      */           
/* 5753 */           this.wasNullFlag = false;
/*      */           
/* 5755 */           return ts.toString();
/*      */         }
/*      */         
/*      */       }
/*      */       
/*      */ 
/* 5761 */       return stringVal;
/*      */     }
/*      */     
/* 5764 */     return getNativeString(columnIndex);
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
/*      */   public Time getTime(int columnIndex)
/*      */     throws SQLException
/*      */   {
/* 5779 */     return getTimeInternal(columnIndex, null, getDefaultTimeZone(), false);
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
/*      */   public Time getTime(int columnIndex, Calendar cal)
/*      */     throws SQLException
/*      */   {
/* 5799 */     return getTimeInternal(columnIndex, cal, cal.getTimeZone(), true);
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
/*      */   public Time getTime(String columnName)
/*      */     throws SQLException
/*      */   {
/* 5814 */     return getTime(findColumn(columnName));
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
/*      */   public Time getTime(String columnName, Calendar cal)
/*      */     throws SQLException
/*      */   {
/* 5834 */     return getTime(findColumn(columnName), cal);
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
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   private Time getTimeInternal(int columnIndex, Calendar targetCalendar, TimeZone tz, boolean rollForward)
/*      */     throws SQLException
/*      */   {
/* 6186 */     if (this.isBinaryEncoded) {
/* 6187 */       return getNativeTime(columnIndex, targetCalendar, tz, rollForward);
/*      */     }
/*      */     
/* 6190 */     if (!this.useFastDateParsing) {
/* 6191 */       String timeAsString = getStringInternal(columnIndex, false);
/*      */       
/* 6193 */       return getTimeFromString(timeAsString, targetCalendar, columnIndex, tz, rollForward);
/*      */     }
/*      */     
/* 6196 */     checkColumnBounds(columnIndex);
/*      */     
/* 6198 */     return getTimeFromBytes(((byte[][])this.thisRow)[(columnIndex - 1)], targetCalendar, columnIndex, tz, rollForward);
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
/*      */   public Timestamp getTimestamp(int columnIndex)
/*      */     throws SQLException
/*      */   {
/* 6216 */     checkColumnBounds(columnIndex);
/*      */     
/* 6218 */     return getTimestampInternal(columnIndex, null, getDefaultTimeZone(), false);
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
/*      */   public Timestamp getTimestamp(int columnIndex, Calendar cal)
/*      */     throws SQLException
/*      */   {
/* 6240 */     return getTimestampInternal(columnIndex, cal, cal.getTimeZone(), true);
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
/*      */   public Timestamp getTimestamp(String columnName)
/*      */     throws SQLException
/*      */   {
/* 6256 */     return getTimestamp(findColumn(columnName));
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
/*      */   public Timestamp getTimestamp(String columnName, Calendar cal)
/*      */     throws SQLException
/*      */   {
/* 6277 */     return getTimestamp(findColumn(columnName), cal);
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
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   private Timestamp getTimestampInternal(int columnIndex, Calendar targetCalendar, TimeZone tz, boolean rollForward)
/*      */     throws SQLException
/*      */   {
/* 6887 */     if (this.isBinaryEncoded) {
/* 6888 */       return getNativeTimestamp(columnIndex, targetCalendar, tz, rollForward);
/*      */     }
/*      */     
/*      */ 
/* 6892 */     if (!this.useFastDateParsing) {
/* 6893 */       String timestampValue = getStringInternal(columnIndex, false);
/*      */       
/* 6895 */       return getTimestampFromString(columnIndex, targetCalendar, timestampValue, tz, rollForward);
/*      */     }
/*      */     
/*      */ 
/* 6899 */     return getTimestampFromBytes(columnIndex, targetCalendar, ((byte[][])this.thisRow)[(columnIndex - 1)], tz, rollForward);
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
/*      */   public int getType()
/*      */     throws SQLException
/*      */   {
/* 6916 */     return this.resultSetType;
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
/*      */   /**
/*      */    * @deprecated
/*      */    */
/*      */   public InputStream getUnicodeStream(int columnIndex)
/*      */     throws SQLException
/*      */   {
/* 6938 */     if (!this.isBinaryEncoded) {
/* 6939 */       checkRowPos();
/*      */       
/* 6941 */       return getBinaryStream(columnIndex);
/*      */     }
/*      */     
/* 6944 */     return getNativeBinaryStream(columnIndex);
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
/*      */   /**
/*      */    * @deprecated
/*      */    */
/*      */   public InputStream getUnicodeStream(String columnName)
/*      */     throws SQLException
/*      */   {
/* 6961 */     return getUnicodeStream(findColumn(columnName));
/*      */   }
/*      */   
/*      */   long getUpdateCount() {
/* 6965 */     return this.updateCount;
/*      */   }
/*      */   
/*      */   long getUpdateID() {
/* 6969 */     return this.updateId;
/*      */   }
/*      */   
/*      */ 
/*      */   public URL getURL(int colIndex)
/*      */     throws SQLException
/*      */   {
/* 6976 */     String val = getString(colIndex);
/*      */     
/* 6978 */     if (val == null) {
/* 6979 */       return null;
/*      */     }
/*      */     try
/*      */     {
/* 6983 */       return new URL(val);
/*      */     } catch (MalformedURLException mfe) {
/* 6985 */       throw SQLError.createSQLException(Messages.getString("ResultSet.Malformed_URL____104") + val + "'", "S1009");
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public URL getURL(String colName)
/*      */     throws SQLException
/*      */   {
/* 6995 */     String val = getString(colName);
/*      */     
/* 6997 */     if (val == null) {
/* 6998 */       return null;
/*      */     }
/*      */     try
/*      */     {
/* 7002 */       return new URL(val);
/*      */     } catch (MalformedURLException mfe) {
/* 7004 */       throw SQLError.createSQLException(Messages.getString("ResultSet.Malformed_URL____107") + val + "'", "S1009");
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
/*      */   public SQLWarning getWarnings()
/*      */     throws SQLException
/*      */   {
/* 7031 */     return this.warningChain;
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
/*      */   public void insertRow()
/*      */     throws SQLException
/*      */   {
/* 7046 */     throw new NotUpdatable();
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
/*      */   public boolean isAfterLast()
/*      */     throws SQLException
/*      */   {
/* 7063 */     checkClosed();
/*      */     
/* 7065 */     boolean b = this.rowData.isAfterLast();
/*      */     
/* 7067 */     return b;
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
/*      */   public boolean isBeforeFirst()
/*      */     throws SQLException
/*      */   {
/* 7084 */     checkClosed();
/*      */     
/* 7086 */     return this.rowData.isBeforeFirst();
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
/*      */   public boolean isFirst()
/*      */     throws SQLException
/*      */   {
/* 7102 */     checkClosed();
/*      */     
/* 7104 */     return this.rowData.isFirst();
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
/*      */   public boolean isLast()
/*      */     throws SQLException
/*      */   {
/* 7123 */     checkClosed();
/*      */     
/* 7125 */     return this.rowData.isLast();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   private void issueConversionViaParsingWarning(String methodName, int columnIndex, Object value, Field fieldInfo, int[] typesWithNoParseConversion)
/*      */     throws SQLException
/*      */   {
/* 7137 */     StringBuffer originalQueryBuf = new StringBuffer();
/*      */     
/* 7139 */     if ((this.owningStatement != null) && ((this.owningStatement instanceof PreparedStatement)))
/*      */     {
/* 7141 */       originalQueryBuf.append(Messages.getString("ResultSet.CostlyConversionCreatedFromQuery"));
/* 7142 */       originalQueryBuf.append(((PreparedStatement)this.owningStatement).originalSql);
/*      */       
/* 7144 */       originalQueryBuf.append("\n\n");
/*      */     } else {
/* 7146 */       originalQueryBuf.append(".");
/*      */     }
/*      */     
/* 7149 */     StringBuffer convertibleTypesBuf = new StringBuffer();
/*      */     
/* 7151 */     for (int i = 0; i < typesWithNoParseConversion.length; i++) {
/* 7152 */       convertibleTypesBuf.append(MysqlDefs.typeToName(typesWithNoParseConversion[i]));
/* 7153 */       convertibleTypesBuf.append("\n");
/*      */     }
/*      */     
/* 7156 */     String message = Messages.getString("ResultSet.CostlyConversion", new Object[] { methodName, new Integer(columnIndex + 1), fieldInfo.getOriginalName(), fieldInfo.getOriginalTableName(), originalQueryBuf.toString(), value != null ? value.getClass().getName() : ResultSetMetaData.getClassNameForJavaType(fieldInfo.getSQLType(), fieldInfo.isUnsigned(), fieldInfo.getMysqlType(), (fieldInfo.isBinary()) || (fieldInfo.isBlob()) ? 1 : false, fieldInfo.isOpaqueBinary()), MysqlDefs.typeToName(fieldInfo.getMysqlType()), convertibleTypesBuf.toString() });
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 7171 */     this.eventSink.consumeEvent(new ProfilerEvent((byte)0, "", this.owningStatement == null ? "N/A" : this.owningStatement.currentCatalog, this.connectionId, this.owningStatement == null ? -1 : this.owningStatement.getId(), this.resultId, System.currentTimeMillis(), 0L, Constants.MILLIS_I18N, null, this.pointOfOrigin, message));
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
/*      */   public boolean last()
/*      */     throws SQLException
/*      */   {
/* 7195 */     checkClosed();
/*      */     
/* 7197 */     if (this.rowData.size() == 0) {
/* 7198 */       return false;
/*      */     }
/*      */     
/* 7201 */     if (this.onInsertRow) {
/* 7202 */       this.onInsertRow = false;
/*      */     }
/*      */     
/* 7205 */     if (this.doingUpdates) {
/* 7206 */       this.doingUpdates = false;
/*      */     }
/*      */     
/* 7209 */     this.rowData.beforeLast();
/* 7210 */     this.thisRow = this.rowData.next();
/*      */     
/* 7212 */     return true;
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
/*      */   public void moveToCurrentRow()
/*      */     throws SQLException
/*      */   {
/* 7234 */     throw new NotUpdatable();
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
/*      */   public void moveToInsertRow()
/*      */     throws SQLException
/*      */   {
/* 7255 */     throw new NotUpdatable();
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
/*      */   public boolean next()
/*      */     throws SQLException
/*      */   {
/* 7274 */     checkClosed();
/*      */     
/* 7276 */     if (this.onInsertRow) {
/* 7277 */       this.onInsertRow = false;
/*      */     }
/*      */     
/* 7280 */     if (this.doingUpdates) {
/* 7281 */       this.doingUpdates = false;
/*      */     }
/*      */     
/*      */ 
/*      */ 
/* 7286 */     if (!reallyResult()) {
/* 7287 */       throw SQLError.createSQLException(Messages.getString("ResultSet.ResultSet_is_from_UPDATE._No_Data_115"), "S1000");
/*      */     }
/*      */     
/*      */     boolean b;
/*      */     
/*      */     boolean b;
/* 7293 */     if (this.rowData.size() == 0) {
/* 7294 */       b = false;
/*      */     } else { boolean b;
/* 7296 */       if (!this.rowData.hasNext())
/*      */       {
/* 7298 */         this.rowData.next();
/* 7299 */         b = false;
/*      */       } else {
/* 7301 */         clearWarnings();
/* 7302 */         this.thisRow = this.rowData.next();
/* 7303 */         b = true;
/*      */       }
/*      */     }
/*      */     
/* 7307 */     return b;
/*      */   }
/*      */   
/*      */   private int parseIntAsDouble(int columnIndex, String val) throws NumberFormatException, SQLException
/*      */   {
/* 7312 */     if (val == null) {
/* 7313 */       return 0;
/*      */     }
/*      */     
/* 7316 */     double valueAsDouble = Double.parseDouble(val);
/*      */     
/* 7318 */     if ((this.connection.getJdbcCompliantTruncationForReads()) && (
/* 7319 */       (valueAsDouble < -2.147483648E9D) || (valueAsDouble > 2.147483647E9D)))
/*      */     {
/* 7321 */       throwRangeException(String.valueOf(valueAsDouble), columnIndex, 4);
/*      */     }
/*      */     
/*      */ 
/*      */ 
/* 7326 */     return (int)valueAsDouble;
/*      */   }
/*      */   
/*      */   private int parseIntWithOverflowCheck(int columnIndex, byte[] valueAsBytes, String valueAsString)
/*      */     throws NumberFormatException, SQLException
/*      */   {
/* 7332 */     int intValue = 0;
/*      */     
/* 7334 */     if ((valueAsBytes == null) && (valueAsString == null)) {
/* 7335 */       return 0;
/*      */     }
/*      */     
/* 7338 */     if (valueAsBytes != null) {
/* 7339 */       intValue = StringUtils.getInt(valueAsBytes);
/*      */ 
/*      */ 
/*      */ 
/*      */     }
/*      */     else
/*      */     {
/*      */ 
/*      */ 
/*      */ 
/* 7349 */       valueAsString = valueAsString.trim();
/*      */       
/* 7351 */       intValue = Integer.parseInt(valueAsString);
/*      */     }
/*      */     
/* 7354 */     if ((this.connection.getJdbcCompliantTruncationForReads()) && (
/* 7355 */       (intValue == Integer.MIN_VALUE) || (intValue == Integer.MAX_VALUE))) {
/* 7356 */       long valueAsLong = Long.parseLong(valueAsString == null ? new String(valueAsBytes) : valueAsString);
/*      */       
/*      */ 
/*      */ 
/* 7360 */       if ((valueAsLong < -2147483648L) || (valueAsLong > 2147483647L))
/*      */       {
/* 7362 */         throwRangeException(valueAsString == null ? new String(valueAsBytes) : valueAsString, columnIndex, 4);
/*      */       }
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/* 7369 */     return intValue;
/*      */   }
/*      */   
/*      */   private long parseLongAsDouble(int columnIndex, String val) throws NumberFormatException, SQLException
/*      */   {
/* 7374 */     if (val == null) {
/* 7375 */       return 0L;
/*      */     }
/*      */     
/* 7378 */     double valueAsDouble = Double.parseDouble(val);
/*      */     
/* 7380 */     if ((this.connection.getJdbcCompliantTruncationForReads()) && (
/* 7381 */       (valueAsDouble < -9.223372036854776E18D) || (valueAsDouble > 9.223372036854776E18D)))
/*      */     {
/* 7383 */       throwRangeException(val, columnIndex, -5);
/*      */     }
/*      */     
/*      */ 
/* 7387 */     return valueAsDouble;
/*      */   }
/*      */   
/*      */ 
/*      */   private long parseLongWithOverflowCheck(int columnIndex, byte[] valueAsBytes, String valueAsString, boolean doCheck)
/*      */     throws NumberFormatException, SQLException
/*      */   {
/* 7394 */     long longValue = 0L;
/*      */     
/* 7396 */     if ((valueAsBytes == null) && (valueAsString == null)) {
/* 7397 */       return 0L;
/*      */     }
/*      */     
/* 7400 */     if (valueAsBytes != null) {
/* 7401 */       longValue = StringUtils.getLong(valueAsBytes);
/*      */ 
/*      */ 
/*      */ 
/*      */     }
/*      */     else
/*      */     {
/*      */ 
/*      */ 
/*      */ 
/* 7411 */       valueAsString = valueAsString.trim();
/*      */       
/* 7413 */       longValue = Long.parseLong(valueAsString);
/*      */     }
/*      */     
/* 7416 */     if ((doCheck) && (this.connection.getJdbcCompliantTruncationForReads()) && (
/* 7417 */       (longValue == Long.MIN_VALUE) || (longValue == Long.MAX_VALUE)))
/*      */     {
/* 7419 */       double valueAsDouble = Double.parseDouble(valueAsString == null ? new String(valueAsBytes) : valueAsString);
/*      */       
/*      */ 
/*      */ 
/* 7423 */       if ((valueAsDouble < -9.223372036854776E18D) || (valueAsDouble > 9.223372036854776E18D))
/*      */       {
/* 7425 */         throwRangeException(valueAsString == null ? new String(valueAsBytes) : valueAsString, columnIndex, -5);
/*      */       }
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/* 7432 */     return longValue;
/*      */   }
/*      */   
/*      */   private short parseShortAsDouble(int columnIndex, String val) throws NumberFormatException, SQLException
/*      */   {
/* 7437 */     if (val == null) {
/* 7438 */       return 0;
/*      */     }
/*      */     
/* 7441 */     double valueAsDouble = Double.parseDouble(val);
/*      */     
/* 7443 */     if ((this.connection.getJdbcCompliantTruncationForReads()) && (
/* 7444 */       (valueAsDouble < -32768.0D) || (valueAsDouble > 32767.0D)))
/*      */     {
/* 7446 */       throwRangeException(String.valueOf(valueAsDouble), columnIndex, 5);
/*      */     }
/*      */     
/*      */ 
/*      */ 
/* 7451 */     return (short)(int)valueAsDouble;
/*      */   }
/*      */   
/*      */ 
/*      */   private short parseShortWithOverflowCheck(int columnIndex, byte[] valueAsBytes, String valueAsString)
/*      */     throws NumberFormatException, SQLException
/*      */   {
/* 7458 */     short shortValue = 0;
/*      */     
/* 7460 */     if ((valueAsBytes == null) && (valueAsString == null)) {
/* 7461 */       return 0;
/*      */     }
/*      */     
/* 7464 */     if (valueAsBytes != null) {
/* 7465 */       shortValue = StringUtils.getShort(valueAsBytes);
/*      */ 
/*      */ 
/*      */ 
/*      */     }
/*      */     else
/*      */     {
/*      */ 
/*      */ 
/*      */ 
/* 7475 */       valueAsString = valueAsString.trim();
/*      */       
/* 7477 */       shortValue = Short.parseShort(valueAsString);
/*      */     }
/*      */     
/* 7480 */     if ((this.connection.getJdbcCompliantTruncationForReads()) && (
/* 7481 */       (shortValue == Short.MIN_VALUE) || (shortValue == Short.MAX_VALUE))) {
/* 7482 */       long valueAsLong = Long.parseLong(valueAsString == null ? new String(valueAsBytes) : valueAsString);
/*      */       
/*      */ 
/*      */ 
/* 7486 */       if ((valueAsLong < -32768L) || (valueAsLong > 32767L))
/*      */       {
/* 7488 */         throwRangeException(valueAsString == null ? new String(valueAsBytes) : valueAsString, columnIndex, 5);
/*      */       }
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/* 7495 */     return shortValue;
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
/*      */   public boolean prev()
/*      */     throws SQLException
/*      */   {
/* 7519 */     checkClosed();
/*      */     
/* 7521 */     int rowIndex = this.rowData.getCurrentRowNumber();
/*      */     
/* 7523 */     if (rowIndex - 1 >= 0) {
/* 7524 */       rowIndex--;
/* 7525 */       this.rowData.setCurrentRow(rowIndex);
/* 7526 */       this.thisRow = this.rowData.getAt(rowIndex);
/*      */       
/* 7528 */       return true; }
/* 7529 */     if (rowIndex - 1 == -1) {
/* 7530 */       rowIndex--;
/* 7531 */       this.rowData.setCurrentRow(rowIndex);
/* 7532 */       this.thisRow = null;
/*      */       
/* 7534 */       return false;
/*      */     }
/* 7536 */     return false;
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
/*      */   public boolean previous()
/*      */     throws SQLException
/*      */   {
/* 7559 */     if (this.onInsertRow) {
/* 7560 */       this.onInsertRow = false;
/*      */     }
/*      */     
/* 7563 */     if (this.doingUpdates) {
/* 7564 */       this.doingUpdates = false;
/*      */     }
/*      */     
/* 7567 */     return prev();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected void realClose(boolean calledExplicitly)
/*      */     throws SQLException
/*      */   {
/* 7580 */     if (this.isClosed) {
/* 7581 */       return;
/*      */     }
/*      */     try
/*      */     {
/* 7585 */       if (this.useUsageAdvisor)
/*      */       {
/*      */ 
/*      */ 
/* 7589 */         if (!calledExplicitly) {
/* 7590 */           this.eventSink.consumeEvent(new ProfilerEvent((byte)0, "", this.owningStatement == null ? "N/A" : this.owningStatement.currentCatalog, this.connectionId, this.owningStatement == null ? -1 : this.owningStatement.getId(), this.resultId, System.currentTimeMillis(), 0L, Constants.MILLIS_I18N, null, this.pointOfOrigin, Messages.getString("ResultSet.ResultSet_implicitly_closed_by_driver")));
/*      */         }
/*      */         
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 7609 */         if ((this.rowData instanceof RowDataStatic))
/*      */         {
/*      */ 
/*      */ 
/* 7613 */           if (this.rowData.size() > this.connection.getResultSetSizeThreshold())
/*      */           {
/* 7615 */             this.eventSink.consumeEvent(new ProfilerEvent((byte)0, "", this.owningStatement == null ? Messages.getString("ResultSet.N/A_159") : this.owningStatement.currentCatalog, this.connectionId, this.owningStatement == null ? -1 : this.owningStatement.getId(), this.resultId, System.currentTimeMillis(), 0L, Constants.MILLIS_I18N, null, this.pointOfOrigin, Messages.getString("ResultSet.Too_Large_Result_Set", new Object[] { new Integer(this.rowData.size()), new Integer(this.connection.getResultSetSizeThreshold()) })));
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
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 7643 */           if ((!isLast()) && (!isAfterLast()) && (this.rowData.size() != 0))
/*      */           {
/* 7645 */             this.eventSink.consumeEvent(new ProfilerEvent((byte)0, "", this.owningStatement == null ? Messages.getString("ResultSet.N/A_159") : this.owningStatement.currentCatalog, this.connectionId, this.owningStatement == null ? -1 : this.owningStatement.getId(), this.resultId, System.currentTimeMillis(), 0L, Constants.MILLIS_I18N, null, this.pointOfOrigin, Messages.getString("ResultSet.Possible_incomplete_traversal_of_result_set", new Object[] { new Integer(getRow()), new Integer(this.rowData.size()) })));
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
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 7678 */         if ((this.columnUsed.length > 0) && (!this.rowData.wasEmpty())) {
/* 7679 */           StringBuffer buf = new StringBuffer(Messages.getString("ResultSet.The_following_columns_were_never_referenced"));
/*      */           
/*      */ 
/*      */ 
/* 7683 */           boolean issueWarn = false;
/*      */           
/* 7685 */           for (int i = 0; i < this.columnUsed.length; i++) {
/* 7686 */             if (this.columnUsed[i] == 0) {
/* 7687 */               if (!issueWarn) {
/* 7688 */                 issueWarn = true;
/*      */               } else {
/* 7690 */                 buf.append(", ");
/*      */               }
/*      */               
/* 7693 */               buf.append(this.fields[i].getFullName());
/*      */             }
/*      */           }
/*      */           
/* 7697 */           if (issueWarn) {
/* 7698 */             this.eventSink.consumeEvent(new ProfilerEvent((byte)0, "", this.owningStatement == null ? "N/A" : this.owningStatement.currentCatalog, this.connectionId, this.owningStatement == null ? -1 : this.owningStatement.getId(), 0, System.currentTimeMillis(), 0L, Constants.MILLIS_I18N, null, this.pointOfOrigin, buf.toString()));
/*      */ 
/*      */           }
/*      */           
/*      */ 
/*      */         }
/*      */         
/*      */       }
/*      */       
/*      */ 
/*      */     }
/*      */     finally
/*      */     {
/*      */ 
/* 7712 */       SQLException exceptionDuringClose = null;
/*      */       
/* 7714 */       if (this.rowData != null) {
/*      */         try {
/* 7716 */           this.rowData.close();
/*      */         } catch (SQLException sqlEx) {
/* 7718 */           exceptionDuringClose = sqlEx;
/*      */         }
/*      */       }
/*      */       
/* 7722 */       this.rowData = null;
/* 7723 */       this.defaultTimeZone = null;
/* 7724 */       this.fields = null;
/* 7725 */       this.columnNameToIndex = null;
/* 7726 */       this.fullColumnNameToIndex = null;
/*      */       
/* 7728 */       this.eventSink = null;
/* 7729 */       this.warningChain = null;
/*      */       
/* 7731 */       if (!this.retainOwningStatement) {
/* 7732 */         this.owningStatement = null;
/*      */       }
/*      */       
/* 7735 */       this.catalog = null;
/* 7736 */       this.serverInfo = null;
/* 7737 */       this.thisRow = null;
/* 7738 */       this.fastDateCal = null;
/* 7739 */       this.connection = null;
/*      */       
/* 7741 */       this.isClosed = true;
/*      */       
/* 7743 */       if (exceptionDuringClose != null) {
/* 7744 */         throw exceptionDuringClose;
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */   boolean reallyResult() {
/* 7750 */     if (this.rowData != null) {
/* 7751 */       return true;
/*      */     }
/*      */     
/* 7754 */     return this.reallyResult;
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
/*      */   public void refreshRow()
/*      */     throws SQLException
/*      */   {
/* 7778 */     throw new NotUpdatable();
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
/*      */   public boolean relative(int rows)
/*      */     throws SQLException
/*      */   {
/* 7808 */     checkClosed();
/*      */     
/* 7810 */     if (this.rowData.size() == 0) {
/* 7811 */       return false;
/*      */     }
/*      */     
/* 7814 */     this.rowData.moveRowRelative(rows);
/* 7815 */     this.thisRow = this.rowData.getAt(this.rowData.getCurrentRowNumber());
/*      */     
/* 7817 */     return (!this.rowData.isAfterLast()) && (!this.rowData.isBeforeFirst());
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
/*      */   public boolean rowDeleted()
/*      */     throws SQLException
/*      */   {
/* 7836 */     throw new NotImplemented();
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
/*      */   public boolean rowInserted()
/*      */     throws SQLException
/*      */   {
/* 7854 */     throw new NotImplemented();
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
/*      */   public boolean rowUpdated()
/*      */     throws SQLException
/*      */   {
/* 7872 */     throw new NotImplemented();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   protected void setBinaryEncoded()
/*      */   {
/* 7880 */     this.isBinaryEncoded = true;
/*      */   }
/*      */   
/*      */   private void setDefaultTimeZone(TimeZone defaultTimeZone) {
/* 7884 */     this.defaultTimeZone = defaultTimeZone;
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
/*      */   public void setFetchDirection(int direction)
/*      */     throws SQLException
/*      */   {
/* 7903 */     if ((direction != 1000) && (direction != 1001) && (direction != 1002))
/*      */     {
/* 7905 */       throw SQLError.createSQLException(Messages.getString("ResultSet.Illegal_value_for_fetch_direction_64"), "S1009");
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/* 7911 */     this.fetchDirection = direction;
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
/*      */   public void setFetchSize(int rows)
/*      */     throws SQLException
/*      */   {
/* 7931 */     if (rows < 0) {
/* 7932 */       throw SQLError.createSQLException(Messages.getString("ResultSet.Value_must_be_between_0_and_getMaxRows()_66"), "S1009");
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/* 7938 */     this.fetchSize = rows;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected void setFirstCharOfQuery(char c)
/*      */   {
/* 7949 */     this.firstCharOfQuery = c;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected void setNextResultSet(ResultSet nextResultSet)
/*      */   {
/* 7960 */     this.nextResultSet = nextResultSet;
/*      */   }
/*      */   
/*      */   protected void setOwningStatement(Statement owningStatement) {
/* 7964 */     this.owningStatement = owningStatement;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected void setResultSetConcurrency(int concurrencyFlag)
/*      */   {
/* 7974 */     this.resultSetConcurrency = concurrencyFlag;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected void setResultSetType(int typeFlag)
/*      */   {
/* 7985 */     this.resultSetType = typeFlag;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected void setServerInfo(String info)
/*      */   {
/* 7995 */     this.serverInfo = info;
/*      */   }
/*      */   
/*      */   void setStatementUsedForFetchingRows(PreparedStatement stmt) {
/* 7999 */     this.statementUsedForFetchingRows = stmt;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public void setWrapperStatement(java.sql.Statement wrapperStatement)
/*      */   {
/* 8007 */     this.wrapperStatement = wrapperStatement;
/*      */   }
/*      */   
/*      */   private void throwRangeException(String valueAsString, int columnIndex, int jdbcType) throws SQLException
/*      */   {
/* 8012 */     String datatype = null;
/*      */     
/* 8014 */     switch (jdbcType) {
/*      */     case -6: 
/* 8016 */       datatype = "TINYINT";
/* 8017 */       break;
/*      */     case 5: 
/* 8019 */       datatype = "SMALLINT";
/* 8020 */       break;
/*      */     case 4: 
/* 8022 */       datatype = "INTEGER";
/* 8023 */       break;
/*      */     case -5: 
/* 8025 */       datatype = "BIGINT";
/* 8026 */       break;
/*      */     case 7: 
/* 8028 */       datatype = "REAL";
/* 8029 */       break;
/*      */     case 6: 
/* 8031 */       datatype = "FLOAT";
/* 8032 */       break;
/*      */     case 8: 
/* 8034 */       datatype = "DOUBLE";
/* 8035 */       break;
/*      */     case 3: 
/* 8037 */       datatype = "DECIMAL";
/* 8038 */       break;
/*      */     case -4: case -3: case -2: case -1: case 0: case 1: case 2: default: 
/* 8040 */       datatype = " (JDBC type '" + jdbcType + "')";
/*      */     }
/*      */     
/* 8043 */     throw SQLError.createSQLException("'" + valueAsString + "' in column '" + columnIndex + "' is outside valid range for the datatype " + datatype + ".", "22003");
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public String toString()
/*      */   {
/* 8054 */     if (this.reallyResult) {
/* 8055 */       return super.toString();
/*      */     }
/*      */     
/* 8058 */     return "Result set representing update count of " + this.updateCount;
/*      */   }
/*      */   
/*      */ 
/*      */   public void updateArray(int arg0, Array arg1)
/*      */     throws SQLException
/*      */   {
/* 8065 */     throw new NotImplemented();
/*      */   }
/*      */   
/*      */ 
/*      */   public void updateArray(String arg0, Array arg1)
/*      */     throws SQLException
/*      */   {
/* 8072 */     throw new NotImplemented();
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
/*      */   public void updateAsciiStream(int columnIndex, InputStream x, int length)
/*      */     throws SQLException
/*      */   {
/* 8096 */     throw new NotUpdatable();
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
/*      */   public void updateAsciiStream(String columnName, InputStream x, int length)
/*      */     throws SQLException
/*      */   {
/* 8118 */     updateAsciiStream(findColumn(columnName), x, length);
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
/*      */   public void updateBigDecimal(int columnIndex, BigDecimal x)
/*      */     throws SQLException
/*      */   {
/* 8139 */     throw new NotUpdatable();
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
/*      */   public void updateBigDecimal(String columnName, BigDecimal x)
/*      */     throws SQLException
/*      */   {
/* 8158 */     updateBigDecimal(findColumn(columnName), x);
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
/*      */   public void updateBinaryStream(int columnIndex, InputStream x, int length)
/*      */     throws SQLException
/*      */   {
/* 8182 */     throw new NotUpdatable();
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
/*      */   public void updateBinaryStream(String columnName, InputStream x, int length)
/*      */     throws SQLException
/*      */   {
/* 8204 */     updateBinaryStream(findColumn(columnName), x, length);
/*      */   }
/*      */   
/*      */ 
/*      */   public void updateBlob(int arg0, java.sql.Blob arg1)
/*      */     throws SQLException
/*      */   {
/* 8211 */     throw new NotUpdatable();
/*      */   }
/*      */   
/*      */ 
/*      */   public void updateBlob(String arg0, java.sql.Blob arg1)
/*      */     throws SQLException
/*      */   {
/* 8218 */     throw new NotUpdatable();
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
/*      */   public void updateBoolean(int columnIndex, boolean x)
/*      */     throws SQLException
/*      */   {
/* 8238 */     throw new NotUpdatable();
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
/*      */   public void updateBoolean(String columnName, boolean x)
/*      */     throws SQLException
/*      */   {
/* 8256 */     updateBoolean(findColumn(columnName), x);
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
/*      */   public void updateByte(int columnIndex, byte x)
/*      */     throws SQLException
/*      */   {
/* 8276 */     throw new NotUpdatable();
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
/*      */   public void updateByte(String columnName, byte x)
/*      */     throws SQLException
/*      */   {
/* 8294 */     updateByte(findColumn(columnName), x);
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
/*      */   public void updateBytes(int columnIndex, byte[] x)
/*      */     throws SQLException
/*      */   {
/* 8314 */     throw new NotUpdatable();
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
/*      */   public void updateBytes(String columnName, byte[] x)
/*      */     throws SQLException
/*      */   {
/* 8332 */     updateBytes(findColumn(columnName), x);
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
/*      */   public void updateCharacterStream(int columnIndex, Reader x, int length)
/*      */     throws SQLException
/*      */   {
/* 8356 */     throw new NotUpdatable();
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
/*      */   public void updateCharacterStream(String columnName, Reader reader, int length)
/*      */     throws SQLException
/*      */   {
/* 8378 */     updateCharacterStream(findColumn(columnName), reader, length);
/*      */   }
/*      */   
/*      */ 
/*      */   public void updateClob(int arg0, java.sql.Clob arg1)
/*      */     throws SQLException
/*      */   {
/* 8385 */     throw new NotImplemented();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public void updateClob(String columnName, java.sql.Clob clob)
/*      */     throws SQLException
/*      */   {
/* 8393 */     updateClob(findColumn(columnName), clob);
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
/*      */   public void updateDate(int columnIndex, Date x)
/*      */     throws SQLException
/*      */   {
/* 8414 */     throw new NotUpdatable();
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
/*      */   public void updateDate(String columnName, Date x)
/*      */     throws SQLException
/*      */   {
/* 8433 */     updateDate(findColumn(columnName), x);
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
/*      */   public void updateDouble(int columnIndex, double x)
/*      */     throws SQLException
/*      */   {
/* 8453 */     throw new NotUpdatable();
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
/*      */   public void updateDouble(String columnName, double x)
/*      */     throws SQLException
/*      */   {
/* 8471 */     updateDouble(findColumn(columnName), x);
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
/*      */   public void updateFloat(int columnIndex, float x)
/*      */     throws SQLException
/*      */   {
/* 8491 */     throw new NotUpdatable();
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
/*      */   public void updateFloat(String columnName, float x)
/*      */     throws SQLException
/*      */   {
/* 8509 */     updateFloat(findColumn(columnName), x);
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
/*      */   public void updateInt(int columnIndex, int x)
/*      */     throws SQLException
/*      */   {
/* 8529 */     throw new NotUpdatable();
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
/*      */   public void updateInt(String columnName, int x)
/*      */     throws SQLException
/*      */   {
/* 8547 */     updateInt(findColumn(columnName), x);
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
/*      */   public void updateLong(int columnIndex, long x)
/*      */     throws SQLException
/*      */   {
/* 8567 */     throw new NotUpdatable();
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
/*      */   public void updateLong(String columnName, long x)
/*      */     throws SQLException
/*      */   {
/* 8585 */     updateLong(findColumn(columnName), x);
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
/*      */   public void updateNull(int columnIndex)
/*      */     throws SQLException
/*      */   {
/* 8603 */     throw new NotUpdatable();
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
/*      */   public void updateNull(String columnName)
/*      */     throws SQLException
/*      */   {
/* 8619 */     updateNull(findColumn(columnName));
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
/*      */   public void updateObject(int columnIndex, Object x)
/*      */     throws SQLException
/*      */   {
/* 8639 */     throw new NotUpdatable();
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
/*      */   public void updateObject(int columnIndex, Object x, int scale)
/*      */     throws SQLException
/*      */   {
/* 8664 */     throw new NotUpdatable();
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
/*      */   public void updateObject(String columnName, Object x)
/*      */     throws SQLException
/*      */   {
/* 8682 */     updateObject(findColumn(columnName), x);
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
/*      */   public void updateObject(String columnName, Object x, int scale)
/*      */     throws SQLException
/*      */   {
/* 8705 */     updateObject(findColumn(columnName), x);
/*      */   }
/*      */   
/*      */ 
/*      */   public void updateRef(int arg0, Ref arg1)
/*      */     throws SQLException
/*      */   {
/* 8712 */     throw new NotImplemented();
/*      */   }
/*      */   
/*      */ 
/*      */   public void updateRef(String arg0, Ref arg1)
/*      */     throws SQLException
/*      */   {
/* 8719 */     throw new NotImplemented();
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
/*      */   public void updateRow()
/*      */     throws SQLException
/*      */   {
/* 8733 */     throw new NotUpdatable();
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
/*      */   public void updateShort(int columnIndex, short x)
/*      */     throws SQLException
/*      */   {
/* 8753 */     throw new NotUpdatable();
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
/*      */   public void updateShort(String columnName, short x)
/*      */     throws SQLException
/*      */   {
/* 8771 */     updateShort(findColumn(columnName), x);
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
/*      */   public void updateString(int columnIndex, String x)
/*      */     throws SQLException
/*      */   {
/* 8791 */     throw new NotUpdatable();
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
/*      */   public void updateString(String columnName, String x)
/*      */     throws SQLException
/*      */   {
/* 8809 */     updateString(findColumn(columnName), x);
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
/*      */   public void updateTime(int columnIndex, Time x)
/*      */     throws SQLException
/*      */   {
/* 8830 */     throw new NotUpdatable();
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
/*      */   public void updateTime(String columnName, Time x)
/*      */     throws SQLException
/*      */   {
/* 8849 */     updateTime(findColumn(columnName), x);
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
/*      */   public void updateTimestamp(int columnIndex, Timestamp x)
/*      */     throws SQLException
/*      */   {
/* 8871 */     throw new NotUpdatable();
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
/*      */   public void updateTimestamp(String columnName, Timestamp x)
/*      */     throws SQLException
/*      */   {
/* 8890 */     updateTimestamp(findColumn(columnName), x);
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
/*      */   public boolean wasNull()
/*      */     throws SQLException
/*      */   {
/* 8905 */     return this.wasNullFlag;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   protected Calendar getGmtCalendar()
/*      */   {
/* 8912 */     if (this.gmtCalendar == null) {
/* 8913 */       this.gmtCalendar = Calendar.getInstance(TimeZone.getTimeZone("GMT"));
/*      */     }
/*      */     
/* 8916 */     return this.gmtCalendar;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   private Object getNativeDateTimeValue(int columnIndex, Calendar targetCalendar, int jdbcType, int mysqlType, TimeZone tz, boolean rollForward)
/*      */     throws SQLException
/*      */   {
/* 8924 */     int year = 0;
/* 8925 */     int month = 0;
/* 8926 */     int day = 0;
/*      */     
/* 8928 */     int hour = 0;
/* 8929 */     int minute = 0;
/* 8930 */     int seconds = 0;
/*      */     
/* 8932 */     int nanos = 0;
/*      */     
/* 8934 */     byte[] bits = (byte[])this.thisRow[(columnIndex - 1)];
/*      */     
/* 8936 */     if (bits == null) {
/* 8937 */       this.wasNullFlag = true;
/*      */       
/* 8939 */       return null;
/*      */     }
/*      */     
/* 8942 */     Calendar sessionCalendar = this.connection.getUseJDBCCompliantTimezoneShift() ? this.connection.getUtcCalendar() : getCalendarInstanceForSessionOrNew();
/*      */     
/*      */ 
/*      */ 
/* 8946 */     this.wasNullFlag = false;
/*      */     
/* 8948 */     boolean populatedFromDateTimeValue = false;
/*      */     
/* 8950 */     switch (mysqlType) {
/*      */     case 7: 
/*      */     case 12: 
/* 8953 */       populatedFromDateTimeValue = true;
/*      */       
/* 8955 */       int length = bits.length;
/*      */       
/* 8957 */       if (length != 0) {
/* 8958 */         year = bits[0] & 0xFF | (bits[1] & 0xFF) << 8;
/* 8959 */         month = bits[2];
/* 8960 */         day = bits[3];
/*      */         
/* 8962 */         if (length > 4) {
/* 8963 */           hour = bits[4];
/* 8964 */           minute = bits[5];
/* 8965 */           seconds = bits[6];
/*      */         }
/*      */         
/* 8968 */         if (length > 7) {
/* 8969 */           nanos = bits[7] & 0xFF | (bits[8] & 0xFF) << 8 | (bits[9] & 0xFF) << 16 | (bits[10] & 0xFF) << 24;
/*      */         }
/*      */       }
/*      */       
/*      */ 
/*      */ 
/*      */       break;
/*      */     case 10: 
/* 8977 */       populatedFromDateTimeValue = true;
/*      */       
/* 8979 */       if (bits.length != 0) {
/* 8980 */         year = bits[0] & 0xFF | (bits[1] & 0xFF) << 8;
/* 8981 */         month = bits[2];
/* 8982 */         day = bits[3];
/*      */       }
/*      */       
/*      */       break;
/*      */     case 11: 
/* 8987 */       populatedFromDateTimeValue = true;
/*      */       
/* 8989 */       if (bits.length != 0)
/*      */       {
/*      */ 
/* 8992 */         hour = bits[5];
/* 8993 */         minute = bits[6];
/* 8994 */         seconds = bits[7];
/*      */       }
/*      */       
/* 8997 */       year = 1970;
/* 8998 */       month = 1;
/* 8999 */       day = 1;
/*      */       
/* 9001 */       break;
/*      */     case 8: case 9: default: 
/* 9003 */       populatedFromDateTimeValue = false;
/*      */     }
/*      */     
/* 9006 */     switch (jdbcType) {
/*      */     case 92: 
/* 9008 */       if (populatedFromDateTimeValue) {
/* 9009 */         Time time = TimeUtil.fastTimeCreate(getCalendarInstanceForSessionOrNew(), hour, minute, seconds);
/*      */         
/*      */ 
/*      */ 
/* 9013 */         Time adjustedTime = TimeUtil.changeTimezone(this.connection, sessionCalendar, targetCalendar, time, this.connection.getServerTimezoneTZ(), tz, rollForward);
/*      */         
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 9019 */         return adjustedTime;
/*      */       }
/*      */       
/* 9022 */       return getNativeTimeViaParseConversion(columnIndex, targetCalendar, tz, rollForward);
/*      */     
/*      */ 
/*      */     case 91: 
/* 9026 */       if (populatedFromDateTimeValue) {
/* 9027 */         if ((year == 0) && (month == 0) && (day == 0)) {
/* 9028 */           if ("convertToNull".equals(this.connection.getZeroDateTimeBehavior()))
/*      */           {
/* 9030 */             this.wasNullFlag = true;
/*      */             
/* 9032 */             return null; }
/* 9033 */           if ("exception".equals(this.connection.getZeroDateTimeBehavior()))
/*      */           {
/* 9035 */             throw new SQLException("Value '0000-00-00' can not be represented as java.sql.Date", "S1009");
/*      */           }
/*      */           
/*      */ 
/*      */ 
/* 9040 */           year = 1;
/* 9041 */           month = 1;
/* 9042 */           day = 1;
/*      */         }
/*      */         
/* 9045 */         return fastDateCreate(getCalendarInstanceForSessionOrNew(), year, month, day);
/*      */       }
/*      */       
/*      */ 
/* 9049 */       return getNativeDateViaParseConversion(columnIndex);
/*      */     case 93: 
/* 9051 */       if (populatedFromDateTimeValue) {
/* 9052 */         if ((year == 0) && (month == 0) && (day == 0)) {
/* 9053 */           if ("convertToNull".equals(this.connection.getZeroDateTimeBehavior()))
/*      */           {
/* 9055 */             this.wasNullFlag = true;
/*      */             
/* 9057 */             return null; }
/* 9058 */           if ("exception".equals(this.connection.getZeroDateTimeBehavior()))
/*      */           {
/* 9060 */             throw new SQLException("Value '0000-00-00' can not be represented as java.sql.Timestamp", "S1009");
/*      */           }
/*      */           
/*      */ 
/*      */ 
/* 9065 */           year = 1;
/* 9066 */           month = 1;
/* 9067 */           day = 1;
/*      */         }
/*      */         
/* 9070 */         Timestamp ts = fastTimestampCreate(getCalendarInstanceForSessionOrNew(), year, month, day, hour, minute, seconds, nanos);
/*      */         
/*      */ 
/*      */ 
/* 9074 */         Timestamp adjustedTs = TimeUtil.changeTimezone(this.connection, sessionCalendar, targetCalendar, ts, this.connection.getServerTimezoneTZ(), tz, rollForward);
/*      */         
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 9080 */         return adjustedTs;
/*      */       }
/*      */       
/* 9083 */       return getNativeTimestampViaParseConversion(columnIndex, targetCalendar, tz, rollForward);
/*      */     }
/*      */     
/* 9086 */     throw new SQLException("Internal error - conversion method doesn't support this type", "S1000");
/*      */   }
/*      */   
/*      */   /* Error */
/*      */   private Time getTimeFromString(String timeAsString, Calendar targetCalendar, int columnIndex, TimeZone tz, boolean rollForward)
/*      */     throws SQLException
/*      */   {
/*      */     // Byte code:
/*      */     //   0: iconst_0
/*      */     //   1: istore 6
/*      */     //   3: iconst_0
/*      */     //   4: istore 7
/*      */     //   6: iconst_0
/*      */     //   7: istore 8
/*      */     //   9: aload_1
/*      */     //   10: ifnonnull +10 -> 20
/*      */     //   13: aload_0
/*      */     //   14: iconst_1
/*      */     //   15: putfield 34	com/mysql/jdbc/ResultSet:wasNullFlag	Z
/*      */     //   18: aconst_null
/*      */     //   19: areturn
/*      */     //   20: aload_1
/*      */     //   21: invokevirtual 221	java/lang/String:trim	()Ljava/lang/String;
/*      */     //   24: astore_1
/*      */     //   25: aload_1
/*      */     //   26: ldc 119
/*      */     //   28: invokevirtual 216	java/lang/String:equals	(Ljava/lang/Object;)Z
/*      */     //   31: ifne +33 -> 64
/*      */     //   34: aload_1
/*      */     //   35: ldc_w 265
/*      */     //   38: invokevirtual 216	java/lang/String:equals	(Ljava/lang/Object;)Z
/*      */     //   41: ifne +23 -> 64
/*      */     //   44: aload_1
/*      */     //   45: ldc_w 266
/*      */     //   48: invokevirtual 216	java/lang/String:equals	(Ljava/lang/Object;)Z
/*      */     //   51: ifne +13 -> 64
/*      */     //   54: aload_1
/*      */     //   55: ldc_w 267
/*      */     //   58: invokevirtual 216	java/lang/String:equals	(Ljava/lang/Object;)Z
/*      */     //   61: ifeq +83 -> 144
/*      */     //   64: ldc_w 268
/*      */     //   67: aload_0
/*      */     //   68: getfield 41	com/mysql/jdbc/ResultSet:connection	Lcom/mysql/jdbc/Connection;
/*      */     //   71: invokevirtual 269	com/mysql/jdbc/Connection:getZeroDateTimeBehavior	()Ljava/lang/String;
/*      */     //   74: invokevirtual 216	java/lang/String:equals	(Ljava/lang/Object;)Z
/*      */     //   77: ifeq +10 -> 87
/*      */     //   80: aload_0
/*      */     //   81: iconst_1
/*      */     //   82: putfield 34	com/mysql/jdbc/ResultSet:wasNullFlag	Z
/*      */     //   85: aconst_null
/*      */     //   86: areturn
/*      */     //   87: ldc_w 270
/*      */     //   90: aload_0
/*      */     //   91: getfield 41	com/mysql/jdbc/ResultSet:connection	Lcom/mysql/jdbc/Connection;
/*      */     //   94: invokevirtual 269	com/mysql/jdbc/Connection:getZeroDateTimeBehavior	()Ljava/lang/String;
/*      */     //   97: invokevirtual 216	java/lang/String:equals	(Ljava/lang/Object;)Z
/*      */     //   100: ifeq +35 -> 135
/*      */     //   103: new 133	java/lang/StringBuffer
/*      */     //   106: dup
/*      */     //   107: invokespecial 134	java/lang/StringBuffer:<init>	()V
/*      */     //   110: ldc_w 271
/*      */     //   113: invokevirtual 136	java/lang/StringBuffer:append	(Ljava/lang/String;)Ljava/lang/StringBuffer;
/*      */     //   116: aload_1
/*      */     //   117: invokevirtual 136	java/lang/StringBuffer:append	(Ljava/lang/String;)Ljava/lang/StringBuffer;
/*      */     //   120: ldc_w 526
/*      */     //   123: invokevirtual 136	java/lang/StringBuffer:append	(Ljava/lang/String;)Ljava/lang/StringBuffer;
/*      */     //   126: invokevirtual 139	java/lang/StringBuffer:toString	()Ljava/lang/String;
/*      */     //   129: ldc 83
/*      */     //   131: invokestatic 84	com/mysql/jdbc/SQLError:createSQLException	(Ljava/lang/String;Ljava/lang/String;)Ljava/sql/SQLException;
/*      */     //   134: athrow
/*      */     //   135: aload_0
/*      */     //   136: aconst_null
/*      */     //   137: iconst_0
/*      */     //   138: iconst_0
/*      */     //   139: iconst_0
/*      */     //   140: invokespecial 527	com/mysql/jdbc/ResultSet:fastTimeCreate	(Ljava/util/Calendar;III)Ljava/sql/Time;
/*      */     //   143: areturn
/*      */     //   144: aload_0
/*      */     //   145: iconst_0
/*      */     //   146: putfield 34	com/mysql/jdbc/ResultSet:wasNullFlag	Z
/*      */     //   149: aload_0
/*      */     //   150: getfield 40	com/mysql/jdbc/ResultSet:fields	[Lcom/mysql/jdbc/Field;
/*      */     //   153: iload_3
/*      */     //   154: iconst_1
/*      */     //   155: isub
/*      */     //   156: aaload
/*      */     //   157: astore 9
/*      */     //   159: aload 9
/*      */     //   161: invokevirtual 183	com/mysql/jdbc/Field:getMysqlType	()I
/*      */     //   164: bipush 7
/*      */     //   166: if_icmpne +336 -> 502
/*      */     //   169: aload_1
/*      */     //   170: invokevirtual 167	java/lang/String:length	()I
/*      */     //   173: istore 10
/*      */     //   175: iload 10
/*      */     //   177: tableswitch	default:+194->371, 10:+162->339, 11:+194->371, 12:+109->286, 13:+194->371, 14:+109->286, 15:+194->371, 16:+194->371, 17:+194->371, 18:+194->371, 19:+55->232
/*      */     //   232: aload_1
/*      */     //   233: iload 10
/*      */     //   235: bipush 8
/*      */     //   237: isub
/*      */     //   238: iload 10
/*      */     //   240: bipush 6
/*      */     //   242: isub
/*      */     //   243: invokevirtual 274	java/lang/String:substring	(II)Ljava/lang/String;
/*      */     //   246: invokestatic 275	java/lang/Integer:parseInt	(Ljava/lang/String;)I
/*      */     //   249: istore 6
/*      */     //   251: aload_1
/*      */     //   252: iload 10
/*      */     //   254: iconst_5
/*      */     //   255: isub
/*      */     //   256: iload 10
/*      */     //   258: iconst_3
/*      */     //   259: isub
/*      */     //   260: invokevirtual 274	java/lang/String:substring	(II)Ljava/lang/String;
/*      */     //   263: invokestatic 275	java/lang/Integer:parseInt	(Ljava/lang/String;)I
/*      */     //   266: istore 7
/*      */     //   268: aload_1
/*      */     //   269: iload 10
/*      */     //   271: iconst_2
/*      */     //   272: isub
/*      */     //   273: iload 10
/*      */     //   275: invokevirtual 274	java/lang/String:substring	(II)Ljava/lang/String;
/*      */     //   278: invokestatic 275	java/lang/Integer:parseInt	(Ljava/lang/String;)I
/*      */     //   281: istore 8
/*      */     //   283: goto +138 -> 421
/*      */     //   286: aload_1
/*      */     //   287: iload 10
/*      */     //   289: bipush 6
/*      */     //   291: isub
/*      */     //   292: iload 10
/*      */     //   294: iconst_4
/*      */     //   295: isub
/*      */     //   296: invokevirtual 274	java/lang/String:substring	(II)Ljava/lang/String;
/*      */     //   299: invokestatic 275	java/lang/Integer:parseInt	(Ljava/lang/String;)I
/*      */     //   302: istore 6
/*      */     //   304: aload_1
/*      */     //   305: iload 10
/*      */     //   307: iconst_4
/*      */     //   308: isub
/*      */     //   309: iload 10
/*      */     //   311: iconst_2
/*      */     //   312: isub
/*      */     //   313: invokevirtual 274	java/lang/String:substring	(II)Ljava/lang/String;
/*      */     //   316: invokestatic 275	java/lang/Integer:parseInt	(Ljava/lang/String;)I
/*      */     //   319: istore 7
/*      */     //   321: aload_1
/*      */     //   322: iload 10
/*      */     //   324: iconst_2
/*      */     //   325: isub
/*      */     //   326: iload 10
/*      */     //   328: invokevirtual 274	java/lang/String:substring	(II)Ljava/lang/String;
/*      */     //   331: invokestatic 275	java/lang/Integer:parseInt	(Ljava/lang/String;)I
/*      */     //   334: istore 8
/*      */     //   336: goto +85 -> 421
/*      */     //   339: aload_1
/*      */     //   340: bipush 6
/*      */     //   342: bipush 8
/*      */     //   344: invokevirtual 274	java/lang/String:substring	(II)Ljava/lang/String;
/*      */     //   347: invokestatic 275	java/lang/Integer:parseInt	(Ljava/lang/String;)I
/*      */     //   350: istore 6
/*      */     //   352: aload_1
/*      */     //   353: bipush 8
/*      */     //   355: bipush 10
/*      */     //   357: invokevirtual 274	java/lang/String:substring	(II)Ljava/lang/String;
/*      */     //   360: invokestatic 275	java/lang/Integer:parseInt	(Ljava/lang/String;)I
/*      */     //   363: istore 7
/*      */     //   365: iconst_0
/*      */     //   366: istore 8
/*      */     //   368: goto +53 -> 421
/*      */     //   371: new 133	java/lang/StringBuffer
/*      */     //   374: dup
/*      */     //   375: invokespecial 134	java/lang/StringBuffer:<init>	()V
/*      */     //   378: ldc_w 528
/*      */     //   381: invokestatic 82	com/mysql/jdbc/Messages:getString	(Ljava/lang/String;)Ljava/lang/String;
/*      */     //   384: invokevirtual 136	java/lang/StringBuffer:append	(Ljava/lang/String;)Ljava/lang/StringBuffer;
/*      */     //   387: iload_3
/*      */     //   388: invokevirtual 179	java/lang/StringBuffer:append	(I)Ljava/lang/StringBuffer;
/*      */     //   391: ldc -76
/*      */     //   393: invokevirtual 136	java/lang/StringBuffer:append	(Ljava/lang/String;)Ljava/lang/StringBuffer;
/*      */     //   396: aload_0
/*      */     //   397: getfield 40	com/mysql/jdbc/ResultSet:fields	[Lcom/mysql/jdbc/Field;
/*      */     //   400: iload_3
/*      */     //   401: iconst_1
/*      */     //   402: isub
/*      */     //   403: aaload
/*      */     //   404: invokevirtual 181	java/lang/StringBuffer:append	(Ljava/lang/Object;)Ljava/lang/StringBuffer;
/*      */     //   407: ldc -74
/*      */     //   409: invokevirtual 136	java/lang/StringBuffer:append	(Ljava/lang/String;)Ljava/lang/StringBuffer;
/*      */     //   412: invokevirtual 139	java/lang/StringBuffer:toString	()Ljava/lang/String;
/*      */     //   415: ldc 83
/*      */     //   417: invokestatic 84	com/mysql/jdbc/SQLError:createSQLException	(Ljava/lang/String;Ljava/lang/String;)Ljava/sql/SQLException;
/*      */     //   420: athrow
/*      */     //   421: new 529	java/sql/SQLWarning
/*      */     //   424: dup
/*      */     //   425: new 133	java/lang/StringBuffer
/*      */     //   428: dup
/*      */     //   429: invokespecial 134	java/lang/StringBuffer:<init>	()V
/*      */     //   432: ldc_w 530
/*      */     //   435: invokestatic 82	com/mysql/jdbc/Messages:getString	(Ljava/lang/String;)Ljava/lang/String;
/*      */     //   438: invokevirtual 136	java/lang/StringBuffer:append	(Ljava/lang/String;)Ljava/lang/StringBuffer;
/*      */     //   441: iload_3
/*      */     //   442: invokevirtual 179	java/lang/StringBuffer:append	(I)Ljava/lang/StringBuffer;
/*      */     //   445: ldc -76
/*      */     //   447: invokevirtual 136	java/lang/StringBuffer:append	(Ljava/lang/String;)Ljava/lang/StringBuffer;
/*      */     //   450: aload_0
/*      */     //   451: getfield 40	com/mysql/jdbc/ResultSet:fields	[Lcom/mysql/jdbc/Field;
/*      */     //   454: iload_3
/*      */     //   455: iconst_1
/*      */     //   456: isub
/*      */     //   457: aaload
/*      */     //   458: invokevirtual 181	java/lang/StringBuffer:append	(Ljava/lang/Object;)Ljava/lang/StringBuffer;
/*      */     //   461: ldc -74
/*      */     //   463: invokevirtual 136	java/lang/StringBuffer:append	(Ljava/lang/String;)Ljava/lang/StringBuffer;
/*      */     //   466: invokevirtual 139	java/lang/StringBuffer:toString	()Ljava/lang/String;
/*      */     //   469: invokespecial 531	java/sql/SQLWarning:<init>	(Ljava/lang/String;)V
/*      */     //   472: astore 11
/*      */     //   474: aload_0
/*      */     //   475: getfield 33	com/mysql/jdbc/ResultSet:warningChain	Ljava/sql/SQLWarning;
/*      */     //   478: ifnonnull +12 -> 490
/*      */     //   481: aload_0
/*      */     //   482: aload 11
/*      */     //   484: putfield 33	com/mysql/jdbc/ResultSet:warningChain	Ljava/sql/SQLWarning;
/*      */     //   487: goto +12 -> 499
/*      */     //   490: aload_0
/*      */     //   491: getfield 33	com/mysql/jdbc/ResultSet:warningChain	Ljava/sql/SQLWarning;
/*      */     //   494: aload 11
/*      */     //   496: invokevirtual 532	java/sql/SQLWarning:setNextWarning	(Ljava/sql/SQLWarning;)V
/*      */     //   499: goto +256 -> 755
/*      */     //   502: aload 9
/*      */     //   504: invokevirtual 183	com/mysql/jdbc/Field:getMysqlType	()I
/*      */     //   507: bipush 12
/*      */     //   509: if_icmpne +123 -> 632
/*      */     //   512: aload_1
/*      */     //   513: bipush 11
/*      */     //   515: bipush 13
/*      */     //   517: invokevirtual 274	java/lang/String:substring	(II)Ljava/lang/String;
/*      */     //   520: invokestatic 275	java/lang/Integer:parseInt	(Ljava/lang/String;)I
/*      */     //   523: istore 6
/*      */     //   525: aload_1
/*      */     //   526: bipush 14
/*      */     //   528: bipush 16
/*      */     //   530: invokevirtual 274	java/lang/String:substring	(II)Ljava/lang/String;
/*      */     //   533: invokestatic 275	java/lang/Integer:parseInt	(Ljava/lang/String;)I
/*      */     //   536: istore 7
/*      */     //   538: aload_1
/*      */     //   539: bipush 17
/*      */     //   541: bipush 19
/*      */     //   543: invokevirtual 274	java/lang/String:substring	(II)Ljava/lang/String;
/*      */     //   546: invokestatic 275	java/lang/Integer:parseInt	(Ljava/lang/String;)I
/*      */     //   549: istore 8
/*      */     //   551: new 529	java/sql/SQLWarning
/*      */     //   554: dup
/*      */     //   555: new 133	java/lang/StringBuffer
/*      */     //   558: dup
/*      */     //   559: invokespecial 134	java/lang/StringBuffer:<init>	()V
/*      */     //   562: ldc_w 533
/*      */     //   565: invokestatic 82	com/mysql/jdbc/Messages:getString	(Ljava/lang/String;)Ljava/lang/String;
/*      */     //   568: invokevirtual 136	java/lang/StringBuffer:append	(Ljava/lang/String;)Ljava/lang/StringBuffer;
/*      */     //   571: iload_3
/*      */     //   572: invokevirtual 179	java/lang/StringBuffer:append	(I)Ljava/lang/StringBuffer;
/*      */     //   575: ldc -76
/*      */     //   577: invokevirtual 136	java/lang/StringBuffer:append	(Ljava/lang/String;)Ljava/lang/StringBuffer;
/*      */     //   580: aload_0
/*      */     //   581: getfield 40	com/mysql/jdbc/ResultSet:fields	[Lcom/mysql/jdbc/Field;
/*      */     //   584: iload_3
/*      */     //   585: iconst_1
/*      */     //   586: isub
/*      */     //   587: aaload
/*      */     //   588: invokevirtual 181	java/lang/StringBuffer:append	(Ljava/lang/Object;)Ljava/lang/StringBuffer;
/*      */     //   591: ldc -74
/*      */     //   593: invokevirtual 136	java/lang/StringBuffer:append	(Ljava/lang/String;)Ljava/lang/StringBuffer;
/*      */     //   596: invokevirtual 139	java/lang/StringBuffer:toString	()Ljava/lang/String;
/*      */     //   599: invokespecial 531	java/sql/SQLWarning:<init>	(Ljava/lang/String;)V
/*      */     //   602: astore 10
/*      */     //   604: aload_0
/*      */     //   605: getfield 33	com/mysql/jdbc/ResultSet:warningChain	Ljava/sql/SQLWarning;
/*      */     //   608: ifnonnull +12 -> 620
/*      */     //   611: aload_0
/*      */     //   612: aload 10
/*      */     //   614: putfield 33	com/mysql/jdbc/ResultSet:warningChain	Ljava/sql/SQLWarning;
/*      */     //   617: goto +12 -> 629
/*      */     //   620: aload_0
/*      */     //   621: getfield 33	com/mysql/jdbc/ResultSet:warningChain	Ljava/sql/SQLWarning;
/*      */     //   624: aload 10
/*      */     //   626: invokevirtual 532	java/sql/SQLWarning:setNextWarning	(Ljava/sql/SQLWarning;)V
/*      */     //   629: goto +126 -> 755
/*      */     //   632: aload 9
/*      */     //   634: invokevirtual 183	com/mysql/jdbc/Field:getMysqlType	()I
/*      */     //   637: bipush 10
/*      */     //   639: if_icmpne +12 -> 651
/*      */     //   642: aload_0
/*      */     //   643: aconst_null
/*      */     //   644: iconst_0
/*      */     //   645: iconst_0
/*      */     //   646: iconst_0
/*      */     //   647: invokespecial 527	com/mysql/jdbc/ResultSet:fastTimeCreate	(Ljava/util/Calendar;III)Ljava/sql/Time;
/*      */     //   650: areturn
/*      */     //   651: aload_1
/*      */     //   652: invokevirtual 167	java/lang/String:length	()I
/*      */     //   655: iconst_5
/*      */     //   656: if_icmpeq +54 -> 710
/*      */     //   659: aload_1
/*      */     //   660: invokevirtual 167	java/lang/String:length	()I
/*      */     //   663: bipush 8
/*      */     //   665: if_icmpeq +45 -> 710
/*      */     //   668: new 133	java/lang/StringBuffer
/*      */     //   671: dup
/*      */     //   672: invokespecial 134	java/lang/StringBuffer:<init>	()V
/*      */     //   675: ldc_w 534
/*      */     //   678: invokestatic 82	com/mysql/jdbc/Messages:getString	(Ljava/lang/String;)Ljava/lang/String;
/*      */     //   681: invokevirtual 136	java/lang/StringBuffer:append	(Ljava/lang/String;)Ljava/lang/StringBuffer;
/*      */     //   684: aload_1
/*      */     //   685: invokevirtual 136	java/lang/StringBuffer:append	(Ljava/lang/String;)Ljava/lang/StringBuffer;
/*      */     //   688: ldc_w 535
/*      */     //   691: invokestatic 82	com/mysql/jdbc/Messages:getString	(Ljava/lang/String;)Ljava/lang/String;
/*      */     //   694: invokevirtual 136	java/lang/StringBuffer:append	(Ljava/lang/String;)Ljava/lang/StringBuffer;
/*      */     //   697: iload_3
/*      */     //   698: invokevirtual 179	java/lang/StringBuffer:append	(I)Ljava/lang/StringBuffer;
/*      */     //   701: invokevirtual 139	java/lang/StringBuffer:toString	()Ljava/lang/String;
/*      */     //   704: ldc 83
/*      */     //   706: invokestatic 84	com/mysql/jdbc/SQLError:createSQLException	(Ljava/lang/String;Ljava/lang/String;)Ljava/sql/SQLException;
/*      */     //   709: athrow
/*      */     //   710: aload_1
/*      */     //   711: iconst_0
/*      */     //   712: iconst_2
/*      */     //   713: invokevirtual 274	java/lang/String:substring	(II)Ljava/lang/String;
/*      */     //   716: invokestatic 275	java/lang/Integer:parseInt	(Ljava/lang/String;)I
/*      */     //   719: istore 6
/*      */     //   721: aload_1
/*      */     //   722: iconst_3
/*      */     //   723: iconst_5
/*      */     //   724: invokevirtual 274	java/lang/String:substring	(II)Ljava/lang/String;
/*      */     //   727: invokestatic 275	java/lang/Integer:parseInt	(Ljava/lang/String;)I
/*      */     //   730: istore 7
/*      */     //   732: aload_1
/*      */     //   733: invokevirtual 167	java/lang/String:length	()I
/*      */     //   736: iconst_5
/*      */     //   737: if_icmpne +7 -> 744
/*      */     //   740: iconst_0
/*      */     //   741: goto +12 -> 753
/*      */     //   744: aload_1
/*      */     //   745: bipush 6
/*      */     //   747: invokevirtual 536	java/lang/String:substring	(I)Ljava/lang/String;
/*      */     //   750: invokestatic 275	java/lang/Integer:parseInt	(Ljava/lang/String;)I
/*      */     //   753: istore 8
/*      */     //   755: aload_0
/*      */     //   756: invokespecial 416	com/mysql/jdbc/ResultSet:getCalendarInstanceForSessionOrNew	()Ljava/util/Calendar;
/*      */     //   759: astore 10
/*      */     //   761: aload 10
/*      */     //   763: dup
/*      */     //   764: astore 11
/*      */     //   766: monitorenter
/*      */     //   767: aload_0
/*      */     //   768: getfield 41	com/mysql/jdbc/ResultSet:connection	Lcom/mysql/jdbc/Connection;
/*      */     //   771: aload 10
/*      */     //   773: aload_2
/*      */     //   774: aload_0
/*      */     //   775: aload 10
/*      */     //   777: iload 6
/*      */     //   779: iload 7
/*      */     //   781: iload 8
/*      */     //   783: invokespecial 527	com/mysql/jdbc/ResultSet:fastTimeCreate	(Ljava/util/Calendar;III)Ljava/sql/Time;
/*      */     //   786: aload_0
/*      */     //   787: getfield 41	com/mysql/jdbc/ResultSet:connection	Lcom/mysql/jdbc/Connection;
/*      */     //   790: invokevirtual 467	com/mysql/jdbc/Connection:getServerTimezoneTZ	()Ljava/util/TimeZone;
/*      */     //   793: aload 4
/*      */     //   795: iload 5
/*      */     //   797: invokestatic 468	com/mysql/jdbc/TimeUtil:changeTimezone	(Lcom/mysql/jdbc/Connection;Ljava/util/Calendar;Ljava/util/Calendar;Ljava/sql/Time;Ljava/util/TimeZone;Ljava/util/TimeZone;Z)Ljava/sql/Time;
/*      */     //   800: aload 11
/*      */     //   802: monitorexit
/*      */     //   803: areturn
/*      */     //   804: astore 12
/*      */     //   806: aload 11
/*      */     //   808: monitorexit
/*      */     //   809: aload 12
/*      */     //   811: athrow
/*      */     //   812: astore 9
/*      */     //   814: aload 9
/*      */     //   816: invokevirtual 537	java/lang/Exception:toString	()Ljava/lang/String;
/*      */     //   819: ldc 83
/*      */     //   821: invokestatic 84	com/mysql/jdbc/SQLError:createSQLException	(Ljava/lang/String;Ljava/lang/String;)Ljava/sql/SQLException;
/*      */     //   824: athrow
/*      */     // Line number table:
/*      */     //   Java source line #5841	-> byte code offset #0
/*      */     //   Java source line #5842	-> byte code offset #3
/*      */     //   Java source line #5843	-> byte code offset #6
/*      */     //   Java source line #5847	-> byte code offset #9
/*      */     //   Java source line #5848	-> byte code offset #13
/*      */     //   Java source line #5850	-> byte code offset #18
/*      */     //   Java source line #5861	-> byte code offset #20
/*      */     //   Java source line #5863	-> byte code offset #25
/*      */     //   Java source line #5867	-> byte code offset #64
/*      */     //   Java source line #5869	-> byte code offset #80
/*      */     //   Java source line #5871	-> byte code offset #85
/*      */     //   Java source line #5872	-> byte code offset #87
/*      */     //   Java source line #5874	-> byte code offset #103
/*      */     //   Java source line #5881	-> byte code offset #135
/*      */     //   Java source line #5884	-> byte code offset #144
/*      */     //   Java source line #5886	-> byte code offset #149
/*      */     //   Java source line #5888	-> byte code offset #159
/*      */     //   Java source line #5890	-> byte code offset #169
/*      */     //   Java source line #5892	-> byte code offset #175
/*      */     //   Java source line #5895	-> byte code offset #232
/*      */     //   Java source line #5897	-> byte code offset #251
/*      */     //   Java source line #5899	-> byte code offset #268
/*      */     //   Java source line #5903	-> byte code offset #283
/*      */     //   Java source line #5906	-> byte code offset #286
/*      */     //   Java source line #5908	-> byte code offset #304
/*      */     //   Java source line #5910	-> byte code offset #321
/*      */     //   Java source line #5914	-> byte code offset #336
/*      */     //   Java source line #5917	-> byte code offset #339
/*      */     //   Java source line #5918	-> byte code offset #352
/*      */     //   Java source line #5919	-> byte code offset #365
/*      */     //   Java source line #5922	-> byte code offset #368
/*      */     //   Java source line #5925	-> byte code offset #371
/*      */     //   Java source line #5934	-> byte code offset #421
/*      */     //   Java source line #5941	-> byte code offset #474
/*      */     //   Java source line #5942	-> byte code offset #481
/*      */     //   Java source line #5944	-> byte code offset #490
/*      */     //   Java source line #5946	-> byte code offset #502
/*      */     //   Java source line #5947	-> byte code offset #512
/*      */     //   Java source line #5948	-> byte code offset #525
/*      */     //   Java source line #5949	-> byte code offset #538
/*      */     //   Java source line #5951	-> byte code offset #551
/*      */     //   Java source line #5958	-> byte code offset #604
/*      */     //   Java source line #5959	-> byte code offset #611
/*      */     //   Java source line #5961	-> byte code offset #620
/*      */     //   Java source line #5963	-> byte code offset #632
/*      */     //   Java source line #5964	-> byte code offset #642
/*      */     //   Java source line #5968	-> byte code offset #651
/*      */     //   Java source line #5970	-> byte code offset #668
/*      */     //   Java source line #5977	-> byte code offset #710
/*      */     //   Java source line #5978	-> byte code offset #721
/*      */     //   Java source line #5979	-> byte code offset #732
/*      */     //   Java source line #5983	-> byte code offset #755
/*      */     //   Java source line #5985	-> byte code offset #761
/*      */     //   Java source line #5986	-> byte code offset #767
/*      */     //   Java source line #5993	-> byte code offset #804
/*      */     //   Java source line #5994	-> byte code offset #812
/*      */     //   Java source line #5995	-> byte code offset #814
/*      */     // Local variable table:
/*      */     //   start	length	slot	name	signature
/*      */     //   0	825	0	this	ResultSet
/*      */     //   0	825	1	timeAsString	String
/*      */     //   0	825	2	targetCalendar	Calendar
/*      */     //   0	825	3	columnIndex	int
/*      */     //   0	825	4	tz	TimeZone
/*      */     //   0	825	5	rollForward	boolean
/*      */     //   1	777	6	hr	int
/*      */     //   4	776	7	min	int
/*      */     //   7	775	8	sec	int
/*      */     //   157	476	9	timeColField	Field
/*      */     //   812	3	9	ex	Exception
/*      */     //   173	154	10	length	int
/*      */     //   602	23	10	precisionLost	SQLWarning
/*      */     //   759	17	10	sessionCalendar	Calendar
/*      */     //   472	23	11	precisionLost	SQLWarning
/*      */     //   764	43	11	Ljava/lang/Object;	Object
/*      */     //   804	6	12	localObject1	Object
/*      */     // Exception table:
/*      */     //   from	to	target	type
/*      */     //   767	803	804	finally
/*      */     //   804	809	804	finally
/*      */     //   9	19	812	java/lang/Exception
/*      */     //   20	86	812	java/lang/Exception
/*      */     //   87	143	812	java/lang/Exception
/*      */     //   144	650	812	java/lang/Exception
/*      */     //   651	803	812	java/lang/Exception
/*      */     //   804	812	812	java/lang/Exception
/*      */   }
/*      */   
/*      */   /* Error */
/*      */   private Time getTimeFromBytes(byte[] timeAsBytes, Calendar targetCalendar, int columnIndex, TimeZone tz, boolean rollForward)
/*      */     throws SQLException
/*      */   {
/*      */     // Byte code:
/*      */     //   0: aload_0
/*      */     //   1: iload_3
/*      */     //   2: invokevirtual 157	com/mysql/jdbc/ResultSet:checkColumnBounds	(I)V
/*      */     //   5: iconst_0
/*      */     //   6: istore 6
/*      */     //   8: iconst_0
/*      */     //   9: istore 7
/*      */     //   11: iconst_0
/*      */     //   12: istore 8
/*      */     //   14: aload_1
/*      */     //   15: ifnonnull +10 -> 25
/*      */     //   18: aload_0
/*      */     //   19: iconst_1
/*      */     //   20: putfield 34	com/mysql/jdbc/ResultSet:wasNullFlag	Z
/*      */     //   23: aconst_null
/*      */     //   24: areturn
/*      */     //   25: aload_1
/*      */     //   26: arraylength
/*      */     //   27: istore 9
/*      */     //   29: iconst_1
/*      */     //   30: istore 10
/*      */     //   32: aload_1
/*      */     //   33: bipush 58
/*      */     //   35: invokestatic 282	com/mysql/jdbc/StringUtils:indexOf	([BC)I
/*      */     //   38: iconst_m1
/*      */     //   39: if_icmpeq +7 -> 46
/*      */     //   42: iconst_1
/*      */     //   43: goto +4 -> 47
/*      */     //   46: iconst_0
/*      */     //   47: istore 11
/*      */     //   49: iconst_0
/*      */     //   50: istore 12
/*      */     //   52: iload 12
/*      */     //   54: iload 9
/*      */     //   56: if_icmpge +87 -> 143
/*      */     //   59: aload_1
/*      */     //   60: iload 12
/*      */     //   62: baload
/*      */     //   63: istore 13
/*      */     //   65: iload 13
/*      */     //   67: bipush 32
/*      */     //   69: if_icmpeq +17 -> 86
/*      */     //   72: iload 13
/*      */     //   74: bipush 45
/*      */     //   76: if_icmpeq +10 -> 86
/*      */     //   79: iload 13
/*      */     //   81: bipush 47
/*      */     //   83: if_icmpne +6 -> 89
/*      */     //   86: iconst_0
/*      */     //   87: istore 11
/*      */     //   89: iload 13
/*      */     //   91: bipush 48
/*      */     //   93: if_icmpeq +44 -> 137
/*      */     //   96: iload 13
/*      */     //   98: bipush 32
/*      */     //   100: if_icmpeq +37 -> 137
/*      */     //   103: iload 13
/*      */     //   105: bipush 58
/*      */     //   107: if_icmpeq +30 -> 137
/*      */     //   110: iload 13
/*      */     //   112: bipush 45
/*      */     //   114: if_icmpeq +23 -> 137
/*      */     //   117: iload 13
/*      */     //   119: bipush 47
/*      */     //   121: if_icmpeq +16 -> 137
/*      */     //   124: iload 13
/*      */     //   126: bipush 46
/*      */     //   128: if_icmpeq +9 -> 137
/*      */     //   131: iconst_0
/*      */     //   132: istore 10
/*      */     //   134: goto +9 -> 143
/*      */     //   137: iinc 12 1
/*      */     //   140: goto -88 -> 52
/*      */     //   143: iload 11
/*      */     //   145: ifne +95 -> 240
/*      */     //   148: iload 10
/*      */     //   150: ifeq +90 -> 240
/*      */     //   153: ldc_w 268
/*      */     //   156: aload_0
/*      */     //   157: getfield 41	com/mysql/jdbc/ResultSet:connection	Lcom/mysql/jdbc/Connection;
/*      */     //   160: invokevirtual 269	com/mysql/jdbc/Connection:getZeroDateTimeBehavior	()Ljava/lang/String;
/*      */     //   163: invokevirtual 216	java/lang/String:equals	(Ljava/lang/Object;)Z
/*      */     //   166: ifeq +10 -> 176
/*      */     //   169: aload_0
/*      */     //   170: iconst_1
/*      */     //   171: putfield 34	com/mysql/jdbc/ResultSet:wasNullFlag	Z
/*      */     //   174: aconst_null
/*      */     //   175: areturn
/*      */     //   176: ldc_w 270
/*      */     //   179: aload_0
/*      */     //   180: getfield 41	com/mysql/jdbc/ResultSet:connection	Lcom/mysql/jdbc/Connection;
/*      */     //   183: invokevirtual 269	com/mysql/jdbc/Connection:getZeroDateTimeBehavior	()Ljava/lang/String;
/*      */     //   186: invokevirtual 216	java/lang/String:equals	(Ljava/lang/Object;)Z
/*      */     //   189: ifeq +42 -> 231
/*      */     //   192: new 133	java/lang/StringBuffer
/*      */     //   195: dup
/*      */     //   196: invokespecial 134	java/lang/StringBuffer:<init>	()V
/*      */     //   199: ldc_w 271
/*      */     //   202: invokevirtual 136	java/lang/StringBuffer:append	(Ljava/lang/String;)Ljava/lang/StringBuffer;
/*      */     //   205: new 124	java/lang/String
/*      */     //   208: dup
/*      */     //   209: aload_1
/*      */     //   210: invokespecial 128	java/lang/String:<init>	([B)V
/*      */     //   213: invokevirtual 136	java/lang/StringBuffer:append	(Ljava/lang/String;)Ljava/lang/StringBuffer;
/*      */     //   216: ldc_w 526
/*      */     //   219: invokevirtual 136	java/lang/StringBuffer:append	(Ljava/lang/String;)Ljava/lang/StringBuffer;
/*      */     //   222: invokevirtual 139	java/lang/StringBuffer:toString	()Ljava/lang/String;
/*      */     //   225: ldc 83
/*      */     //   227: invokestatic 84	com/mysql/jdbc/SQLError:createSQLException	(Ljava/lang/String;Ljava/lang/String;)Ljava/sql/SQLException;
/*      */     //   230: athrow
/*      */     //   231: aload_0
/*      */     //   232: aconst_null
/*      */     //   233: iconst_0
/*      */     //   234: iconst_0
/*      */     //   235: iconst_0
/*      */     //   236: invokespecial 527	com/mysql/jdbc/ResultSet:fastTimeCreate	(Ljava/util/Calendar;III)Ljava/sql/Time;
/*      */     //   239: areturn
/*      */     //   240: aload_0
/*      */     //   241: iconst_0
/*      */     //   242: putfield 34	com/mysql/jdbc/ResultSet:wasNullFlag	Z
/*      */     //   245: aload_0
/*      */     //   246: getfield 40	com/mysql/jdbc/ResultSet:fields	[Lcom/mysql/jdbc/Field;
/*      */     //   249: iload_3
/*      */     //   250: iconst_1
/*      */     //   251: isub
/*      */     //   252: aaload
/*      */     //   253: astore 12
/*      */     //   255: aload 12
/*      */     //   257: invokevirtual 183	com/mysql/jdbc/Field:getMysqlType	()I
/*      */     //   260: bipush 7
/*      */     //   262: if_icmpne +304 -> 566
/*      */     //   265: iload 9
/*      */     //   267: tableswitch	default:+168->435, 10:+142->409, 11:+168->435, 12:+98->365, 13:+168->435, 14:+98->365, 15:+168->435, 16:+168->435, 17:+168->435, 18:+168->435, 19:+53->320
/*      */     //   320: aload_1
/*      */     //   321: iload 9
/*      */     //   323: bipush 8
/*      */     //   325: isub
/*      */     //   326: iload 9
/*      */     //   328: bipush 6
/*      */     //   330: isub
/*      */     //   331: invokestatic 283	com/mysql/jdbc/StringUtils:getInt	([BII)I
/*      */     //   334: istore 6
/*      */     //   336: aload_1
/*      */     //   337: iload 9
/*      */     //   339: iconst_5
/*      */     //   340: isub
/*      */     //   341: iload 9
/*      */     //   343: iconst_3
/*      */     //   344: isub
/*      */     //   345: invokestatic 283	com/mysql/jdbc/StringUtils:getInt	([BII)I
/*      */     //   348: istore 7
/*      */     //   350: aload_1
/*      */     //   351: iload 9
/*      */     //   353: iconst_2
/*      */     //   354: isub
/*      */     //   355: iload 9
/*      */     //   357: invokestatic 283	com/mysql/jdbc/StringUtils:getInt	([BII)I
/*      */     //   360: istore 8
/*      */     //   362: goto +123 -> 485
/*      */     //   365: aload_1
/*      */     //   366: iload 9
/*      */     //   368: bipush 6
/*      */     //   370: isub
/*      */     //   371: iload 9
/*      */     //   373: iconst_4
/*      */     //   374: isub
/*      */     //   375: invokestatic 283	com/mysql/jdbc/StringUtils:getInt	([BII)I
/*      */     //   378: istore 6
/*      */     //   380: aload_1
/*      */     //   381: iload 9
/*      */     //   383: iconst_4
/*      */     //   384: isub
/*      */     //   385: iload 9
/*      */     //   387: iconst_2
/*      */     //   388: isub
/*      */     //   389: invokestatic 283	com/mysql/jdbc/StringUtils:getInt	([BII)I
/*      */     //   392: istore 7
/*      */     //   394: aload_1
/*      */     //   395: iload 9
/*      */     //   397: iconst_2
/*      */     //   398: isub
/*      */     //   399: iload 9
/*      */     //   401: invokestatic 283	com/mysql/jdbc/StringUtils:getInt	([BII)I
/*      */     //   404: istore 8
/*      */     //   406: goto +79 -> 485
/*      */     //   409: aload_1
/*      */     //   410: bipush 6
/*      */     //   412: bipush 8
/*      */     //   414: invokestatic 283	com/mysql/jdbc/StringUtils:getInt	([BII)I
/*      */     //   417: istore 6
/*      */     //   419: aload_1
/*      */     //   420: bipush 8
/*      */     //   422: bipush 10
/*      */     //   424: invokestatic 283	com/mysql/jdbc/StringUtils:getInt	([BII)I
/*      */     //   427: istore 7
/*      */     //   429: iconst_0
/*      */     //   430: istore 8
/*      */     //   432: goto +53 -> 485
/*      */     //   435: new 133	java/lang/StringBuffer
/*      */     //   438: dup
/*      */     //   439: invokespecial 134	java/lang/StringBuffer:<init>	()V
/*      */     //   442: ldc_w 528
/*      */     //   445: invokestatic 82	com/mysql/jdbc/Messages:getString	(Ljava/lang/String;)Ljava/lang/String;
/*      */     //   448: invokevirtual 136	java/lang/StringBuffer:append	(Ljava/lang/String;)Ljava/lang/StringBuffer;
/*      */     //   451: iload_3
/*      */     //   452: invokevirtual 179	java/lang/StringBuffer:append	(I)Ljava/lang/StringBuffer;
/*      */     //   455: ldc -76
/*      */     //   457: invokevirtual 136	java/lang/StringBuffer:append	(Ljava/lang/String;)Ljava/lang/StringBuffer;
/*      */     //   460: aload_0
/*      */     //   461: getfield 40	com/mysql/jdbc/ResultSet:fields	[Lcom/mysql/jdbc/Field;
/*      */     //   464: iload_3
/*      */     //   465: iconst_1
/*      */     //   466: isub
/*      */     //   467: aaload
/*      */     //   468: invokevirtual 181	java/lang/StringBuffer:append	(Ljava/lang/Object;)Ljava/lang/StringBuffer;
/*      */     //   471: ldc -74
/*      */     //   473: invokevirtual 136	java/lang/StringBuffer:append	(Ljava/lang/String;)Ljava/lang/StringBuffer;
/*      */     //   476: invokevirtual 139	java/lang/StringBuffer:toString	()Ljava/lang/String;
/*      */     //   479: ldc 83
/*      */     //   481: invokestatic 84	com/mysql/jdbc/SQLError:createSQLException	(Ljava/lang/String;Ljava/lang/String;)Ljava/sql/SQLException;
/*      */     //   484: athrow
/*      */     //   485: new 529	java/sql/SQLWarning
/*      */     //   488: dup
/*      */     //   489: new 133	java/lang/StringBuffer
/*      */     //   492: dup
/*      */     //   493: invokespecial 134	java/lang/StringBuffer:<init>	()V
/*      */     //   496: ldc_w 530
/*      */     //   499: invokestatic 82	com/mysql/jdbc/Messages:getString	(Ljava/lang/String;)Ljava/lang/String;
/*      */     //   502: invokevirtual 136	java/lang/StringBuffer:append	(Ljava/lang/String;)Ljava/lang/StringBuffer;
/*      */     //   505: iload_3
/*      */     //   506: invokevirtual 179	java/lang/StringBuffer:append	(I)Ljava/lang/StringBuffer;
/*      */     //   509: ldc -76
/*      */     //   511: invokevirtual 136	java/lang/StringBuffer:append	(Ljava/lang/String;)Ljava/lang/StringBuffer;
/*      */     //   514: aload_0
/*      */     //   515: getfield 40	com/mysql/jdbc/ResultSet:fields	[Lcom/mysql/jdbc/Field;
/*      */     //   518: iload_3
/*      */     //   519: iconst_1
/*      */     //   520: isub
/*      */     //   521: aaload
/*      */     //   522: invokevirtual 181	java/lang/StringBuffer:append	(Ljava/lang/Object;)Ljava/lang/StringBuffer;
/*      */     //   525: ldc -74
/*      */     //   527: invokevirtual 136	java/lang/StringBuffer:append	(Ljava/lang/String;)Ljava/lang/StringBuffer;
/*      */     //   530: invokevirtual 139	java/lang/StringBuffer:toString	()Ljava/lang/String;
/*      */     //   533: invokespecial 531	java/sql/SQLWarning:<init>	(Ljava/lang/String;)V
/*      */     //   536: astore 13
/*      */     //   538: aload_0
/*      */     //   539: getfield 33	com/mysql/jdbc/ResultSet:warningChain	Ljava/sql/SQLWarning;
/*      */     //   542: ifnonnull +12 -> 554
/*      */     //   545: aload_0
/*      */     //   546: aload 13
/*      */     //   548: putfield 33	com/mysql/jdbc/ResultSet:warningChain	Ljava/sql/SQLWarning;
/*      */     //   551: goto +12 -> 563
/*      */     //   554: aload_0
/*      */     //   555: getfield 33	com/mysql/jdbc/ResultSet:warningChain	Ljava/sql/SQLWarning;
/*      */     //   558: aload 13
/*      */     //   560: invokevirtual 532	java/sql/SQLWarning:setNextWarning	(Ljava/sql/SQLWarning;)V
/*      */     //   563: goto +241 -> 804
/*      */     //   566: aload 12
/*      */     //   568: invokevirtual 183	com/mysql/jdbc/Field:getMysqlType	()I
/*      */     //   571: bipush 12
/*      */     //   573: if_icmpne +114 -> 687
/*      */     //   576: aload_1
/*      */     //   577: bipush 11
/*      */     //   579: bipush 13
/*      */     //   581: invokestatic 283	com/mysql/jdbc/StringUtils:getInt	([BII)I
/*      */     //   584: istore 6
/*      */     //   586: aload_1
/*      */     //   587: bipush 14
/*      */     //   589: bipush 16
/*      */     //   591: invokestatic 283	com/mysql/jdbc/StringUtils:getInt	([BII)I
/*      */     //   594: istore 7
/*      */     //   596: aload_1
/*      */     //   597: bipush 17
/*      */     //   599: bipush 19
/*      */     //   601: invokestatic 283	com/mysql/jdbc/StringUtils:getInt	([BII)I
/*      */     //   604: istore 8
/*      */     //   606: new 529	java/sql/SQLWarning
/*      */     //   609: dup
/*      */     //   610: new 133	java/lang/StringBuffer
/*      */     //   613: dup
/*      */     //   614: invokespecial 134	java/lang/StringBuffer:<init>	()V
/*      */     //   617: ldc_w 533
/*      */     //   620: invokestatic 82	com/mysql/jdbc/Messages:getString	(Ljava/lang/String;)Ljava/lang/String;
/*      */     //   623: invokevirtual 136	java/lang/StringBuffer:append	(Ljava/lang/String;)Ljava/lang/StringBuffer;
/*      */     //   626: iload_3
/*      */     //   627: invokevirtual 179	java/lang/StringBuffer:append	(I)Ljava/lang/StringBuffer;
/*      */     //   630: ldc -76
/*      */     //   632: invokevirtual 136	java/lang/StringBuffer:append	(Ljava/lang/String;)Ljava/lang/StringBuffer;
/*      */     //   635: aload_0
/*      */     //   636: getfield 40	com/mysql/jdbc/ResultSet:fields	[Lcom/mysql/jdbc/Field;
/*      */     //   639: iload_3
/*      */     //   640: iconst_1
/*      */     //   641: isub
/*      */     //   642: aaload
/*      */     //   643: invokevirtual 181	java/lang/StringBuffer:append	(Ljava/lang/Object;)Ljava/lang/StringBuffer;
/*      */     //   646: ldc -74
/*      */     //   648: invokevirtual 136	java/lang/StringBuffer:append	(Ljava/lang/String;)Ljava/lang/StringBuffer;
/*      */     //   651: invokevirtual 139	java/lang/StringBuffer:toString	()Ljava/lang/String;
/*      */     //   654: invokespecial 531	java/sql/SQLWarning:<init>	(Ljava/lang/String;)V
/*      */     //   657: astore 13
/*      */     //   659: aload_0
/*      */     //   660: getfield 33	com/mysql/jdbc/ResultSet:warningChain	Ljava/sql/SQLWarning;
/*      */     //   663: ifnonnull +12 -> 675
/*      */     //   666: aload_0
/*      */     //   667: aload 13
/*      */     //   669: putfield 33	com/mysql/jdbc/ResultSet:warningChain	Ljava/sql/SQLWarning;
/*      */     //   672: goto +12 -> 684
/*      */     //   675: aload_0
/*      */     //   676: getfield 33	com/mysql/jdbc/ResultSet:warningChain	Ljava/sql/SQLWarning;
/*      */     //   679: aload 13
/*      */     //   681: invokevirtual 532	java/sql/SQLWarning:setNextWarning	(Ljava/sql/SQLWarning;)V
/*      */     //   684: goto +120 -> 804
/*      */     //   687: aload 12
/*      */     //   689: invokevirtual 183	com/mysql/jdbc/Field:getMysqlType	()I
/*      */     //   692: bipush 10
/*      */     //   694: if_icmpne +12 -> 706
/*      */     //   697: aload_0
/*      */     //   698: aconst_null
/*      */     //   699: iconst_0
/*      */     //   700: iconst_0
/*      */     //   701: iconst_0
/*      */     //   702: invokespecial 527	com/mysql/jdbc/ResultSet:fastTimeCreate	(Ljava/util/Calendar;III)Ljava/sql/Time;
/*      */     //   705: areturn
/*      */     //   706: iload 9
/*      */     //   708: iconst_5
/*      */     //   709: if_icmpeq +59 -> 768
/*      */     //   712: iload 9
/*      */     //   714: bipush 8
/*      */     //   716: if_icmpeq +52 -> 768
/*      */     //   719: new 133	java/lang/StringBuffer
/*      */     //   722: dup
/*      */     //   723: invokespecial 134	java/lang/StringBuffer:<init>	()V
/*      */     //   726: ldc_w 534
/*      */     //   729: invokestatic 82	com/mysql/jdbc/Messages:getString	(Ljava/lang/String;)Ljava/lang/String;
/*      */     //   732: invokevirtual 136	java/lang/StringBuffer:append	(Ljava/lang/String;)Ljava/lang/StringBuffer;
/*      */     //   735: new 124	java/lang/String
/*      */     //   738: dup
/*      */     //   739: aload_1
/*      */     //   740: invokespecial 128	java/lang/String:<init>	([B)V
/*      */     //   743: invokevirtual 136	java/lang/StringBuffer:append	(Ljava/lang/String;)Ljava/lang/StringBuffer;
/*      */     //   746: ldc_w 535
/*      */     //   749: invokestatic 82	com/mysql/jdbc/Messages:getString	(Ljava/lang/String;)Ljava/lang/String;
/*      */     //   752: invokevirtual 136	java/lang/StringBuffer:append	(Ljava/lang/String;)Ljava/lang/StringBuffer;
/*      */     //   755: iload_3
/*      */     //   756: invokevirtual 179	java/lang/StringBuffer:append	(I)Ljava/lang/StringBuffer;
/*      */     //   759: invokevirtual 139	java/lang/StringBuffer:toString	()Ljava/lang/String;
/*      */     //   762: ldc 83
/*      */     //   764: invokestatic 84	com/mysql/jdbc/SQLError:createSQLException	(Ljava/lang/String;Ljava/lang/String;)Ljava/sql/SQLException;
/*      */     //   767: athrow
/*      */     //   768: aload_1
/*      */     //   769: iconst_0
/*      */     //   770: iconst_2
/*      */     //   771: invokestatic 283	com/mysql/jdbc/StringUtils:getInt	([BII)I
/*      */     //   774: istore 6
/*      */     //   776: aload_1
/*      */     //   777: iconst_3
/*      */     //   778: iconst_5
/*      */     //   779: invokestatic 283	com/mysql/jdbc/StringUtils:getInt	([BII)I
/*      */     //   782: istore 7
/*      */     //   784: iload 9
/*      */     //   786: iconst_5
/*      */     //   787: if_icmpne +7 -> 794
/*      */     //   790: iconst_0
/*      */     //   791: goto +11 -> 802
/*      */     //   794: aload_1
/*      */     //   795: bipush 6
/*      */     //   797: bipush 8
/*      */     //   799: invokestatic 283	com/mysql/jdbc/StringUtils:getInt	([BII)I
/*      */     //   802: istore 8
/*      */     //   804: aload_0
/*      */     //   805: invokespecial 416	com/mysql/jdbc/ResultSet:getCalendarInstanceForSessionOrNew	()Ljava/util/Calendar;
/*      */     //   808: astore 13
/*      */     //   810: aload 13
/*      */     //   812: dup
/*      */     //   813: astore 14
/*      */     //   815: monitorenter
/*      */     //   816: aload_0
/*      */     //   817: getfield 41	com/mysql/jdbc/ResultSet:connection	Lcom/mysql/jdbc/Connection;
/*      */     //   820: aload 13
/*      */     //   822: aload_2
/*      */     //   823: aload_0
/*      */     //   824: aload 13
/*      */     //   826: iload 6
/*      */     //   828: iload 7
/*      */     //   830: iload 8
/*      */     //   832: invokespecial 527	com/mysql/jdbc/ResultSet:fastTimeCreate	(Ljava/util/Calendar;III)Ljava/sql/Time;
/*      */     //   835: aload_0
/*      */     //   836: getfield 41	com/mysql/jdbc/ResultSet:connection	Lcom/mysql/jdbc/Connection;
/*      */     //   839: invokevirtual 467	com/mysql/jdbc/Connection:getServerTimezoneTZ	()Ljava/util/TimeZone;
/*      */     //   842: aload 4
/*      */     //   844: iload 5
/*      */     //   846: invokestatic 468	com/mysql/jdbc/TimeUtil:changeTimezone	(Lcom/mysql/jdbc/Connection;Ljava/util/Calendar;Ljava/util/Calendar;Ljava/sql/Time;Ljava/util/TimeZone;Ljava/util/TimeZone;Z)Ljava/sql/Time;
/*      */     //   849: aload 14
/*      */     //   851: monitorexit
/*      */     //   852: areturn
/*      */     //   853: astore 15
/*      */     //   855: aload 14
/*      */     //   857: monitorexit
/*      */     //   858: aload 15
/*      */     //   860: athrow
/*      */     //   861: astore 9
/*      */     //   863: aload 9
/*      */     //   865: invokevirtual 537	java/lang/Exception:toString	()Ljava/lang/String;
/*      */     //   868: ldc 83
/*      */     //   870: invokestatic 84	com/mysql/jdbc/SQLError:createSQLException	(Ljava/lang/String;Ljava/lang/String;)Ljava/sql/SQLException;
/*      */     //   873: athrow
/*      */     // Line number table:
/*      */     //   Java source line #6004	-> byte code offset #0
/*      */     //   Java source line #6006	-> byte code offset #5
/*      */     //   Java source line #6007	-> byte code offset #8
/*      */     //   Java source line #6008	-> byte code offset #11
/*      */     //   Java source line #6012	-> byte code offset #14
/*      */     //   Java source line #6013	-> byte code offset #18
/*      */     //   Java source line #6015	-> byte code offset #23
/*      */     //   Java source line #6018	-> byte code offset #25
/*      */     //   Java source line #6020	-> byte code offset #29
/*      */     //   Java source line #6021	-> byte code offset #32
/*      */     //   Java source line #6023	-> byte code offset #49
/*      */     //   Java source line #6024	-> byte code offset #59
/*      */     //   Java source line #6026	-> byte code offset #65
/*      */     //   Java source line #6027	-> byte code offset #86
/*      */     //   Java source line #6030	-> byte code offset #89
/*      */     //   Java source line #6032	-> byte code offset #131
/*      */     //   Java source line #6034	-> byte code offset #134
/*      */     //   Java source line #6023	-> byte code offset #137
/*      */     //   Java source line #6038	-> byte code offset #143
/*      */     //   Java source line #6039	-> byte code offset #153
/*      */     //   Java source line #6041	-> byte code offset #169
/*      */     //   Java source line #6043	-> byte code offset #174
/*      */     //   Java source line #6044	-> byte code offset #176
/*      */     //   Java source line #6046	-> byte code offset #192
/*      */     //   Java source line #6053	-> byte code offset #231
/*      */     //   Java source line #6056	-> byte code offset #240
/*      */     //   Java source line #6058	-> byte code offset #245
/*      */     //   Java source line #6060	-> byte code offset #255
/*      */     //   Java source line #6062	-> byte code offset #265
/*      */     //   Java source line #6065	-> byte code offset #320
/*      */     //   Java source line #6067	-> byte code offset #336
/*      */     //   Java source line #6069	-> byte code offset #350
/*      */     //   Java source line #6073	-> byte code offset #362
/*      */     //   Java source line #6076	-> byte code offset #365
/*      */     //   Java source line #6078	-> byte code offset #380
/*      */     //   Java source line #6080	-> byte code offset #394
/*      */     //   Java source line #6084	-> byte code offset #406
/*      */     //   Java source line #6087	-> byte code offset #409
/*      */     //   Java source line #6088	-> byte code offset #419
/*      */     //   Java source line #6089	-> byte code offset #429
/*      */     //   Java source line #6092	-> byte code offset #432
/*      */     //   Java source line #6095	-> byte code offset #435
/*      */     //   Java source line #6104	-> byte code offset #485
/*      */     //   Java source line #6111	-> byte code offset #538
/*      */     //   Java source line #6112	-> byte code offset #545
/*      */     //   Java source line #6114	-> byte code offset #554
/*      */     //   Java source line #6116	-> byte code offset #566
/*      */     //   Java source line #6117	-> byte code offset #576
/*      */     //   Java source line #6118	-> byte code offset #586
/*      */     //   Java source line #6119	-> byte code offset #596
/*      */     //   Java source line #6121	-> byte code offset #606
/*      */     //   Java source line #6128	-> byte code offset #659
/*      */     //   Java source line #6129	-> byte code offset #666
/*      */     //   Java source line #6131	-> byte code offset #675
/*      */     //   Java source line #6133	-> byte code offset #687
/*      */     //   Java source line #6134	-> byte code offset #697
/*      */     //   Java source line #6138	-> byte code offset #706
/*      */     //   Java source line #6140	-> byte code offset #719
/*      */     //   Java source line #6147	-> byte code offset #768
/*      */     //   Java source line #6148	-> byte code offset #776
/*      */     //   Java source line #6149	-> byte code offset #784
/*      */     //   Java source line #6152	-> byte code offset #804
/*      */     //   Java source line #6154	-> byte code offset #810
/*      */     //   Java source line #6155	-> byte code offset #816
/*      */     //   Java source line #6162	-> byte code offset #853
/*      */     //   Java source line #6163	-> byte code offset #861
/*      */     //   Java source line #6164	-> byte code offset #863
/*      */     // Local variable table:
/*      */     //   start	length	slot	name	signature
/*      */     //   0	874	0	this	ResultSet
/*      */     //   0	874	1	timeAsBytes	byte[]
/*      */     //   0	874	2	targetCalendar	Calendar
/*      */     //   0	874	3	columnIndex	int
/*      */     //   0	874	4	tz	TimeZone
/*      */     //   0	874	5	rollForward	boolean
/*      */     //   6	821	6	hr	int
/*      */     //   9	820	7	min	int
/*      */     //   12	819	8	sec	int
/*      */     //   27	758	9	length	int
/*      */     //   861	3	9	ex	Exception
/*      */     //   30	119	10	allZeroTime	boolean
/*      */     //   47	97	11	onlyTimePresent	boolean
/*      */     //   50	88	12	i	int
/*      */     //   253	435	12	timeColField	Field
/*      */     //   63	62	13	b	byte
/*      */     //   536	23	13	precisionLost	SQLWarning
/*      */     //   657	23	13	precisionLost	SQLWarning
/*      */     //   808	17	13	sessionCalendar	Calendar
/*      */     //   813	43	14	Ljava/lang/Object;	Object
/*      */     //   853	6	15	localObject1	Object
/*      */     // Exception table:
/*      */     //   from	to	target	type
/*      */     //   816	852	853	finally
/*      */     //   853	858	853	finally
/*      */     //   14	24	861	java/lang/Exception
/*      */     //   25	175	861	java/lang/Exception
/*      */     //   176	239	861	java/lang/Exception
/*      */     //   240	705	861	java/lang/Exception
/*      */     //   706	852	861	java/lang/Exception
/*      */     //   853	861	861	java/lang/Exception
/*      */   }
/*      */   
/*      */   /* Error */
/*      */   private Timestamp getTimestampFromString(int columnIndex, Calendar targetCalendar, String timestampValue, TimeZone tz, boolean rollForward)
/*      */     throws SQLException
/*      */   {
/*      */     // Byte code:
/*      */     //   0: aload_0
/*      */     //   1: iconst_0
/*      */     //   2: putfield 34	com/mysql/jdbc/ResultSet:wasNullFlag	Z
/*      */     //   5: aload_3
/*      */     //   6: ifnonnull +10 -> 16
/*      */     //   9: aload_0
/*      */     //   10: iconst_1
/*      */     //   11: putfield 34	com/mysql/jdbc/ResultSet:wasNullFlag	Z
/*      */     //   14: aconst_null
/*      */     //   15: areturn
/*      */     //   16: aload_3
/*      */     //   17: invokevirtual 221	java/lang/String:trim	()Ljava/lang/String;
/*      */     //   20: astore_3
/*      */     //   21: aload_3
/*      */     //   22: invokevirtual 167	java/lang/String:length	()I
/*      */     //   25: istore 6
/*      */     //   27: aload_0
/*      */     //   28: getfield 41	com/mysql/jdbc/ResultSet:connection	Lcom/mysql/jdbc/Connection;
/*      */     //   31: invokevirtual 473	com/mysql/jdbc/Connection:getUseJDBCCompliantTimezoneShift	()Z
/*      */     //   34: ifeq +13 -> 47
/*      */     //   37: aload_0
/*      */     //   38: getfield 41	com/mysql/jdbc/ResultSet:connection	Lcom/mysql/jdbc/Connection;
/*      */     //   41: invokevirtual 474	com/mysql/jdbc/Connection:getUtcCalendar	()Ljava/util/Calendar;
/*      */     //   44: goto +7 -> 51
/*      */     //   47: aload_0
/*      */     //   48: invokespecial 416	com/mysql/jdbc/ResultSet:getCalendarInstanceForSessionOrNew	()Ljava/util/Calendar;
/*      */     //   51: astore 7
/*      */     //   53: aload 7
/*      */     //   55: dup
/*      */     //   56: astore 8
/*      */     //   58: monitorenter
/*      */     //   59: iload 6
/*      */     //   61: ifle +142 -> 203
/*      */     //   64: aload_3
/*      */     //   65: iconst_0
/*      */     //   66: invokevirtual 213	java/lang/String:charAt	(I)C
/*      */     //   69: bipush 48
/*      */     //   71: if_icmpne +132 -> 203
/*      */     //   74: aload_3
/*      */     //   75: ldc_w 265
/*      */     //   78: invokevirtual 216	java/lang/String:equals	(Ljava/lang/Object;)Z
/*      */     //   81: ifne +32 -> 113
/*      */     //   84: aload_3
/*      */     //   85: ldc_w 266
/*      */     //   88: invokevirtual 216	java/lang/String:equals	(Ljava/lang/Object;)Z
/*      */     //   91: ifne +22 -> 113
/*      */     //   94: aload_3
/*      */     //   95: ldc_w 267
/*      */     //   98: invokevirtual 216	java/lang/String:equals	(Ljava/lang/Object;)Z
/*      */     //   101: ifne +12 -> 113
/*      */     //   104: aload_3
/*      */     //   105: ldc 119
/*      */     //   107: invokevirtual 216	java/lang/String:equals	(Ljava/lang/Object;)Z
/*      */     //   110: ifeq +93 -> 203
/*      */     //   113: ldc_w 268
/*      */     //   116: aload_0
/*      */     //   117: getfield 41	com/mysql/jdbc/ResultSet:connection	Lcom/mysql/jdbc/Connection;
/*      */     //   120: invokevirtual 269	com/mysql/jdbc/Connection:getZeroDateTimeBehavior	()Ljava/lang/String;
/*      */     //   123: invokevirtual 216	java/lang/String:equals	(Ljava/lang/Object;)Z
/*      */     //   126: ifeq +13 -> 139
/*      */     //   129: aload_0
/*      */     //   130: iconst_1
/*      */     //   131: putfield 34	com/mysql/jdbc/ResultSet:wasNullFlag	Z
/*      */     //   134: aconst_null
/*      */     //   135: aload 8
/*      */     //   137: monitorexit
/*      */     //   138: areturn
/*      */     //   139: ldc_w 270
/*      */     //   142: aload_0
/*      */     //   143: getfield 41	com/mysql/jdbc/ResultSet:connection	Lcom/mysql/jdbc/Connection;
/*      */     //   146: invokevirtual 269	com/mysql/jdbc/Connection:getZeroDateTimeBehavior	()Ljava/lang/String;
/*      */     //   149: invokevirtual 216	java/lang/String:equals	(Ljava/lang/Object;)Z
/*      */     //   152: ifeq +35 -> 187
/*      */     //   155: new 133	java/lang/StringBuffer
/*      */     //   158: dup
/*      */     //   159: invokespecial 134	java/lang/StringBuffer:<init>	()V
/*      */     //   162: ldc_w 271
/*      */     //   165: invokevirtual 136	java/lang/StringBuffer:append	(Ljava/lang/String;)Ljava/lang/StringBuffer;
/*      */     //   168: aload_3
/*      */     //   169: invokevirtual 136	java/lang/StringBuffer:append	(Ljava/lang/String;)Ljava/lang/StringBuffer;
/*      */     //   172: ldc_w 541
/*      */     //   175: invokevirtual 136	java/lang/StringBuffer:append	(Ljava/lang/String;)Ljava/lang/StringBuffer;
/*      */     //   178: invokevirtual 139	java/lang/StringBuffer:toString	()Ljava/lang/String;
/*      */     //   181: ldc 83
/*      */     //   183: invokestatic 84	com/mysql/jdbc/SQLError:createSQLException	(Ljava/lang/String;Ljava/lang/String;)Ljava/sql/SQLException;
/*      */     //   186: athrow
/*      */     //   187: aload_0
/*      */     //   188: aconst_null
/*      */     //   189: iconst_1
/*      */     //   190: iconst_1
/*      */     //   191: iconst_1
/*      */     //   192: iconst_0
/*      */     //   193: iconst_0
/*      */     //   194: iconst_0
/*      */     //   195: iconst_0
/*      */     //   196: invokespecial 475	com/mysql/jdbc/ResultSet:fastTimestampCreate	(Ljava/util/Calendar;IIIIIII)Ljava/sql/Timestamp;
/*      */     //   199: aload 8
/*      */     //   201: monitorexit
/*      */     //   202: areturn
/*      */     //   203: aload_0
/*      */     //   204: getfield 40	com/mysql/jdbc/ResultSet:fields	[Lcom/mysql/jdbc/Field;
/*      */     //   207: iload_1
/*      */     //   208: iconst_1
/*      */     //   209: isub
/*      */     //   210: aaload
/*      */     //   211: invokevirtual 183	com/mysql/jdbc/Field:getMysqlType	()I
/*      */     //   214: bipush 13
/*      */     //   216: if_icmpne +49 -> 265
/*      */     //   219: aload_0
/*      */     //   220: getfield 41	com/mysql/jdbc/ResultSet:connection	Lcom/mysql/jdbc/Connection;
/*      */     //   223: aload 7
/*      */     //   225: aload_2
/*      */     //   226: aload_0
/*      */     //   227: aload 7
/*      */     //   229: aload_3
/*      */     //   230: iconst_0
/*      */     //   231: iconst_4
/*      */     //   232: invokevirtual 274	java/lang/String:substring	(II)Ljava/lang/String;
/*      */     //   235: invokestatic 275	java/lang/Integer:parseInt	(Ljava/lang/String;)I
/*      */     //   238: iconst_1
/*      */     //   239: iconst_1
/*      */     //   240: iconst_0
/*      */     //   241: iconst_0
/*      */     //   242: iconst_0
/*      */     //   243: iconst_0
/*      */     //   244: invokespecial 475	com/mysql/jdbc/ResultSet:fastTimestampCreate	(Ljava/util/Calendar;IIIIIII)Ljava/sql/Timestamp;
/*      */     //   247: aload_0
/*      */     //   248: getfield 41	com/mysql/jdbc/ResultSet:connection	Lcom/mysql/jdbc/Connection;
/*      */     //   251: invokevirtual 467	com/mysql/jdbc/Connection:getServerTimezoneTZ	()Ljava/util/TimeZone;
/*      */     //   254: aload 4
/*      */     //   256: iload 5
/*      */     //   258: invokestatic 476	com/mysql/jdbc/TimeUtil:changeTimezone	(Lcom/mysql/jdbc/Connection;Ljava/util/Calendar;Ljava/util/Calendar;Ljava/sql/Timestamp;Ljava/util/TimeZone;Ljava/util/TimeZone;Z)Ljava/sql/Timestamp;
/*      */     //   261: aload 8
/*      */     //   263: monitorexit
/*      */     //   264: areturn
/*      */     //   265: aload_3
/*      */     //   266: ldc -34
/*      */     //   268: invokevirtual 414	java/lang/String:endsWith	(Ljava/lang/String;)Z
/*      */     //   271: ifeq +15 -> 286
/*      */     //   274: aload_3
/*      */     //   275: iconst_0
/*      */     //   276: aload_3
/*      */     //   277: invokevirtual 167	java/lang/String:length	()I
/*      */     //   280: iconst_1
/*      */     //   281: isub
/*      */     //   282: invokevirtual 274	java/lang/String:substring	(II)Ljava/lang/String;
/*      */     //   285: astore_3
/*      */     //   286: iload 6
/*      */     //   288: tableswitch	default:+1158->1446, 2:+1091->1379, 3:+1158->1446, 4:+1011->1299, 5:+1158->1446, 6:+918->1206, 7:+1158->1446, 8:+746->1034, 9:+1158->1446, 10:+549->837, 11:+1158->1446, 12:+414->702, 13:+1158->1446, 14:+295->583, 15:+1158->1446, 16:+1158->1446, 17:+1158->1446, 18:+1158->1446, 19:+116->404, 20:+116->404, 21:+116->404, 22:+116->404, 23:+116->404, 24:+116->404, 25:+116->404, 26:+116->404
/*      */     //   404: aload_3
/*      */     //   405: iconst_0
/*      */     //   406: iconst_4
/*      */     //   407: invokevirtual 274	java/lang/String:substring	(II)Ljava/lang/String;
/*      */     //   410: invokestatic 275	java/lang/Integer:parseInt	(Ljava/lang/String;)I
/*      */     //   413: istore 9
/*      */     //   415: aload_3
/*      */     //   416: iconst_5
/*      */     //   417: bipush 7
/*      */     //   419: invokevirtual 274	java/lang/String:substring	(II)Ljava/lang/String;
/*      */     //   422: invokestatic 275	java/lang/Integer:parseInt	(Ljava/lang/String;)I
/*      */     //   425: istore 10
/*      */     //   427: aload_3
/*      */     //   428: bipush 8
/*      */     //   430: bipush 10
/*      */     //   432: invokevirtual 274	java/lang/String:substring	(II)Ljava/lang/String;
/*      */     //   435: invokestatic 275	java/lang/Integer:parseInt	(Ljava/lang/String;)I
/*      */     //   438: istore 11
/*      */     //   440: aload_3
/*      */     //   441: bipush 11
/*      */     //   443: bipush 13
/*      */     //   445: invokevirtual 274	java/lang/String:substring	(II)Ljava/lang/String;
/*      */     //   448: invokestatic 275	java/lang/Integer:parseInt	(Ljava/lang/String;)I
/*      */     //   451: istore 12
/*      */     //   453: aload_3
/*      */     //   454: bipush 14
/*      */     //   456: bipush 16
/*      */     //   458: invokevirtual 274	java/lang/String:substring	(II)Ljava/lang/String;
/*      */     //   461: invokestatic 275	java/lang/Integer:parseInt	(Ljava/lang/String;)I
/*      */     //   464: istore 13
/*      */     //   466: aload_3
/*      */     //   467: bipush 17
/*      */     //   469: bipush 19
/*      */     //   471: invokevirtual 274	java/lang/String:substring	(II)Ljava/lang/String;
/*      */     //   474: invokestatic 275	java/lang/Integer:parseInt	(Ljava/lang/String;)I
/*      */     //   477: istore 14
/*      */     //   479: iconst_0
/*      */     //   480: istore 15
/*      */     //   482: iload 6
/*      */     //   484: bipush 19
/*      */     //   486: if_icmple +52 -> 538
/*      */     //   489: aload_3
/*      */     //   490: bipush 46
/*      */     //   492: invokevirtual 542	java/lang/String:lastIndexOf	(I)I
/*      */     //   495: istore 16
/*      */     //   497: iload 16
/*      */     //   499: iconst_m1
/*      */     //   500: if_icmpeq +38 -> 538
/*      */     //   503: iload 16
/*      */     //   505: iconst_2
/*      */     //   506: iadd
/*      */     //   507: aload_3
/*      */     //   508: invokevirtual 167	java/lang/String:length	()I
/*      */     //   511: if_icmpgt +19 -> 530
/*      */     //   514: aload_3
/*      */     //   515: iload 16
/*      */     //   517: iconst_1
/*      */     //   518: iadd
/*      */     //   519: invokevirtual 536	java/lang/String:substring	(I)Ljava/lang/String;
/*      */     //   522: invokestatic 275	java/lang/Integer:parseInt	(Ljava/lang/String;)I
/*      */     //   525: istore 15
/*      */     //   527: goto +11 -> 538
/*      */     //   530: new 543	java/lang/IllegalArgumentException
/*      */     //   533: dup
/*      */     //   534: invokespecial 544	java/lang/IllegalArgumentException:<init>	()V
/*      */     //   537: athrow
/*      */     //   538: aload_0
/*      */     //   539: getfield 41	com/mysql/jdbc/ResultSet:connection	Lcom/mysql/jdbc/Connection;
/*      */     //   542: aload 7
/*      */     //   544: aload_2
/*      */     //   545: aload_0
/*      */     //   546: aload 7
/*      */     //   548: iload 9
/*      */     //   550: iload 10
/*      */     //   552: iload 11
/*      */     //   554: iload 12
/*      */     //   556: iload 13
/*      */     //   558: iload 14
/*      */     //   560: iload 15
/*      */     //   562: invokespecial 475	com/mysql/jdbc/ResultSet:fastTimestampCreate	(Ljava/util/Calendar;IIIIIII)Ljava/sql/Timestamp;
/*      */     //   565: aload_0
/*      */     //   566: getfield 41	com/mysql/jdbc/ResultSet:connection	Lcom/mysql/jdbc/Connection;
/*      */     //   569: invokevirtual 467	com/mysql/jdbc/Connection:getServerTimezoneTZ	()Ljava/util/TimeZone;
/*      */     //   572: aload 4
/*      */     //   574: iload 5
/*      */     //   576: invokestatic 476	com/mysql/jdbc/TimeUtil:changeTimezone	(Lcom/mysql/jdbc/Connection;Ljava/util/Calendar;Ljava/util/Calendar;Ljava/sql/Timestamp;Ljava/util/TimeZone;Ljava/util/TimeZone;Z)Ljava/sql/Timestamp;
/*      */     //   579: aload 8
/*      */     //   581: monitorexit
/*      */     //   582: areturn
/*      */     //   583: aload_3
/*      */     //   584: iconst_0
/*      */     //   585: iconst_4
/*      */     //   586: invokevirtual 274	java/lang/String:substring	(II)Ljava/lang/String;
/*      */     //   589: invokestatic 275	java/lang/Integer:parseInt	(Ljava/lang/String;)I
/*      */     //   592: istore 9
/*      */     //   594: aload_3
/*      */     //   595: iconst_4
/*      */     //   596: bipush 6
/*      */     //   598: invokevirtual 274	java/lang/String:substring	(II)Ljava/lang/String;
/*      */     //   601: invokestatic 275	java/lang/Integer:parseInt	(Ljava/lang/String;)I
/*      */     //   604: istore 10
/*      */     //   606: aload_3
/*      */     //   607: bipush 6
/*      */     //   609: bipush 8
/*      */     //   611: invokevirtual 274	java/lang/String:substring	(II)Ljava/lang/String;
/*      */     //   614: invokestatic 275	java/lang/Integer:parseInt	(Ljava/lang/String;)I
/*      */     //   617: istore 11
/*      */     //   619: aload_3
/*      */     //   620: bipush 8
/*      */     //   622: bipush 10
/*      */     //   624: invokevirtual 274	java/lang/String:substring	(II)Ljava/lang/String;
/*      */     //   627: invokestatic 275	java/lang/Integer:parseInt	(Ljava/lang/String;)I
/*      */     //   630: istore 12
/*      */     //   632: aload_3
/*      */     //   633: bipush 10
/*      */     //   635: bipush 12
/*      */     //   637: invokevirtual 274	java/lang/String:substring	(II)Ljava/lang/String;
/*      */     //   640: invokestatic 275	java/lang/Integer:parseInt	(Ljava/lang/String;)I
/*      */     //   643: istore 13
/*      */     //   645: aload_3
/*      */     //   646: bipush 12
/*      */     //   648: bipush 14
/*      */     //   650: invokevirtual 274	java/lang/String:substring	(II)Ljava/lang/String;
/*      */     //   653: invokestatic 275	java/lang/Integer:parseInt	(Ljava/lang/String;)I
/*      */     //   656: istore 14
/*      */     //   658: aload_0
/*      */     //   659: getfield 41	com/mysql/jdbc/ResultSet:connection	Lcom/mysql/jdbc/Connection;
/*      */     //   662: aload 7
/*      */     //   664: aload_2
/*      */     //   665: aload_0
/*      */     //   666: aload 7
/*      */     //   668: iload 9
/*      */     //   670: iload 10
/*      */     //   672: iload 11
/*      */     //   674: iload 12
/*      */     //   676: iload 13
/*      */     //   678: iload 14
/*      */     //   680: iconst_0
/*      */     //   681: invokespecial 475	com/mysql/jdbc/ResultSet:fastTimestampCreate	(Ljava/util/Calendar;IIIIIII)Ljava/sql/Timestamp;
/*      */     //   684: aload_0
/*      */     //   685: getfield 41	com/mysql/jdbc/ResultSet:connection	Lcom/mysql/jdbc/Connection;
/*      */     //   688: invokevirtual 467	com/mysql/jdbc/Connection:getServerTimezoneTZ	()Ljava/util/TimeZone;
/*      */     //   691: aload 4
/*      */     //   693: iload 5
/*      */     //   695: invokestatic 476	com/mysql/jdbc/TimeUtil:changeTimezone	(Lcom/mysql/jdbc/Connection;Ljava/util/Calendar;Ljava/util/Calendar;Ljava/sql/Timestamp;Ljava/util/TimeZone;Ljava/util/TimeZone;Z)Ljava/sql/Timestamp;
/*      */     //   698: aload 8
/*      */     //   700: monitorexit
/*      */     //   701: areturn
/*      */     //   702: aload_3
/*      */     //   703: iconst_0
/*      */     //   704: iconst_2
/*      */     //   705: invokevirtual 274	java/lang/String:substring	(II)Ljava/lang/String;
/*      */     //   708: invokestatic 275	java/lang/Integer:parseInt	(Ljava/lang/String;)I
/*      */     //   711: istore 9
/*      */     //   713: iload 9
/*      */     //   715: bipush 69
/*      */     //   717: if_icmpgt +10 -> 727
/*      */     //   720: iload 9
/*      */     //   722: bipush 100
/*      */     //   724: iadd
/*      */     //   725: istore 9
/*      */     //   727: aload_3
/*      */     //   728: iconst_2
/*      */     //   729: iconst_4
/*      */     //   730: invokevirtual 274	java/lang/String:substring	(II)Ljava/lang/String;
/*      */     //   733: invokestatic 275	java/lang/Integer:parseInt	(Ljava/lang/String;)I
/*      */     //   736: istore 10
/*      */     //   738: aload_3
/*      */     //   739: iconst_4
/*      */     //   740: bipush 6
/*      */     //   742: invokevirtual 274	java/lang/String:substring	(II)Ljava/lang/String;
/*      */     //   745: invokestatic 275	java/lang/Integer:parseInt	(Ljava/lang/String;)I
/*      */     //   748: istore 11
/*      */     //   750: aload_3
/*      */     //   751: bipush 6
/*      */     //   753: bipush 8
/*      */     //   755: invokevirtual 274	java/lang/String:substring	(II)Ljava/lang/String;
/*      */     //   758: invokestatic 275	java/lang/Integer:parseInt	(Ljava/lang/String;)I
/*      */     //   761: istore 12
/*      */     //   763: aload_3
/*      */     //   764: bipush 8
/*      */     //   766: bipush 10
/*      */     //   768: invokevirtual 274	java/lang/String:substring	(II)Ljava/lang/String;
/*      */     //   771: invokestatic 275	java/lang/Integer:parseInt	(Ljava/lang/String;)I
/*      */     //   774: istore 13
/*      */     //   776: aload_3
/*      */     //   777: bipush 10
/*      */     //   779: bipush 12
/*      */     //   781: invokevirtual 274	java/lang/String:substring	(II)Ljava/lang/String;
/*      */     //   784: invokestatic 275	java/lang/Integer:parseInt	(Ljava/lang/String;)I
/*      */     //   787: istore 14
/*      */     //   789: aload_0
/*      */     //   790: getfield 41	com/mysql/jdbc/ResultSet:connection	Lcom/mysql/jdbc/Connection;
/*      */     //   793: aload 7
/*      */     //   795: aload_2
/*      */     //   796: aload_0
/*      */     //   797: aload 7
/*      */     //   799: iload 9
/*      */     //   801: sipush 1900
/*      */     //   804: iadd
/*      */     //   805: iload 10
/*      */     //   807: iload 11
/*      */     //   809: iload 12
/*      */     //   811: iload 13
/*      */     //   813: iload 14
/*      */     //   815: iconst_0
/*      */     //   816: invokespecial 475	com/mysql/jdbc/ResultSet:fastTimestampCreate	(Ljava/util/Calendar;IIIIIII)Ljava/sql/Timestamp;
/*      */     //   819: aload_0
/*      */     //   820: getfield 41	com/mysql/jdbc/ResultSet:connection	Lcom/mysql/jdbc/Connection;
/*      */     //   823: invokevirtual 467	com/mysql/jdbc/Connection:getServerTimezoneTZ	()Ljava/util/TimeZone;
/*      */     //   826: aload 4
/*      */     //   828: iload 5
/*      */     //   830: invokestatic 476	com/mysql/jdbc/TimeUtil:changeTimezone	(Lcom/mysql/jdbc/Connection;Ljava/util/Calendar;Ljava/util/Calendar;Ljava/sql/Timestamp;Ljava/util/TimeZone;Ljava/util/TimeZone;Z)Ljava/sql/Timestamp;
/*      */     //   833: aload 8
/*      */     //   835: monitorexit
/*      */     //   836: areturn
/*      */     //   837: aload_0
/*      */     //   838: getfield 40	com/mysql/jdbc/ResultSet:fields	[Lcom/mysql/jdbc/Field;
/*      */     //   841: iload_1
/*      */     //   842: iconst_1
/*      */     //   843: isub
/*      */     //   844: aaload
/*      */     //   845: invokevirtual 183	com/mysql/jdbc/Field:getMysqlType	()I
/*      */     //   848: bipush 10
/*      */     //   850: if_icmpeq +14 -> 864
/*      */     //   853: aload_3
/*      */     //   854: ldc_w 545
/*      */     //   857: invokevirtual 223	java/lang/String:indexOf	(Ljava/lang/String;)I
/*      */     //   860: iconst_m1
/*      */     //   861: if_icmpeq +48 -> 909
/*      */     //   864: aload_3
/*      */     //   865: iconst_0
/*      */     //   866: iconst_4
/*      */     //   867: invokevirtual 274	java/lang/String:substring	(II)Ljava/lang/String;
/*      */     //   870: invokestatic 275	java/lang/Integer:parseInt	(Ljava/lang/String;)I
/*      */     //   873: istore 9
/*      */     //   875: aload_3
/*      */     //   876: iconst_5
/*      */     //   877: bipush 7
/*      */     //   879: invokevirtual 274	java/lang/String:substring	(II)Ljava/lang/String;
/*      */     //   882: invokestatic 275	java/lang/Integer:parseInt	(Ljava/lang/String;)I
/*      */     //   885: istore 10
/*      */     //   887: aload_3
/*      */     //   888: bipush 8
/*      */     //   890: bipush 10
/*      */     //   892: invokevirtual 274	java/lang/String:substring	(II)Ljava/lang/String;
/*      */     //   895: invokestatic 275	java/lang/Integer:parseInt	(Ljava/lang/String;)I
/*      */     //   898: istore 11
/*      */     //   900: iconst_0
/*      */     //   901: istore 12
/*      */     //   903: iconst_0
/*      */     //   904: istore 13
/*      */     //   906: goto +85 -> 991
/*      */     //   909: aload_3
/*      */     //   910: iconst_0
/*      */     //   911: iconst_2
/*      */     //   912: invokevirtual 274	java/lang/String:substring	(II)Ljava/lang/String;
/*      */     //   915: invokestatic 275	java/lang/Integer:parseInt	(Ljava/lang/String;)I
/*      */     //   918: istore 9
/*      */     //   920: iload 9
/*      */     //   922: bipush 69
/*      */     //   924: if_icmpgt +10 -> 934
/*      */     //   927: iload 9
/*      */     //   929: bipush 100
/*      */     //   931: iadd
/*      */     //   932: istore 9
/*      */     //   934: aload_3
/*      */     //   935: iconst_2
/*      */     //   936: iconst_4
/*      */     //   937: invokevirtual 274	java/lang/String:substring	(II)Ljava/lang/String;
/*      */     //   940: invokestatic 275	java/lang/Integer:parseInt	(Ljava/lang/String;)I
/*      */     //   943: istore 10
/*      */     //   945: aload_3
/*      */     //   946: iconst_4
/*      */     //   947: bipush 6
/*      */     //   949: invokevirtual 274	java/lang/String:substring	(II)Ljava/lang/String;
/*      */     //   952: invokestatic 275	java/lang/Integer:parseInt	(Ljava/lang/String;)I
/*      */     //   955: istore 11
/*      */     //   957: aload_3
/*      */     //   958: bipush 6
/*      */     //   960: bipush 8
/*      */     //   962: invokevirtual 274	java/lang/String:substring	(II)Ljava/lang/String;
/*      */     //   965: invokestatic 275	java/lang/Integer:parseInt	(Ljava/lang/String;)I
/*      */     //   968: istore 12
/*      */     //   970: aload_3
/*      */     //   971: bipush 8
/*      */     //   973: bipush 10
/*      */     //   975: invokevirtual 274	java/lang/String:substring	(II)Ljava/lang/String;
/*      */     //   978: invokestatic 275	java/lang/Integer:parseInt	(Ljava/lang/String;)I
/*      */     //   981: istore 13
/*      */     //   983: iload 9
/*      */     //   985: sipush 1900
/*      */     //   988: iadd
/*      */     //   989: istore 9
/*      */     //   991: aload_0
/*      */     //   992: getfield 41	com/mysql/jdbc/ResultSet:connection	Lcom/mysql/jdbc/Connection;
/*      */     //   995: aload 7
/*      */     //   997: aload_2
/*      */     //   998: aload_0
/*      */     //   999: aload 7
/*      */     //   1001: iload 9
/*      */     //   1003: iload 10
/*      */     //   1005: iload 11
/*      */     //   1007: iload 12
/*      */     //   1009: iload 13
/*      */     //   1011: iconst_0
/*      */     //   1012: iconst_0
/*      */     //   1013: invokespecial 475	com/mysql/jdbc/ResultSet:fastTimestampCreate	(Ljava/util/Calendar;IIIIIII)Ljava/sql/Timestamp;
/*      */     //   1016: aload_0
/*      */     //   1017: getfield 41	com/mysql/jdbc/ResultSet:connection	Lcom/mysql/jdbc/Connection;
/*      */     //   1020: invokevirtual 467	com/mysql/jdbc/Connection:getServerTimezoneTZ	()Ljava/util/TimeZone;
/*      */     //   1023: aload 4
/*      */     //   1025: iload 5
/*      */     //   1027: invokestatic 476	com/mysql/jdbc/TimeUtil:changeTimezone	(Lcom/mysql/jdbc/Connection;Ljava/util/Calendar;Ljava/util/Calendar;Ljava/sql/Timestamp;Ljava/util/TimeZone;Ljava/util/TimeZone;Z)Ljava/sql/Timestamp;
/*      */     //   1030: aload 8
/*      */     //   1032: monitorexit
/*      */     //   1033: areturn
/*      */     //   1034: aload_3
/*      */     //   1035: ldc_w 546
/*      */     //   1038: invokevirtual 223	java/lang/String:indexOf	(Ljava/lang/String;)I
/*      */     //   1041: iconst_m1
/*      */     //   1042: if_icmpeq +81 -> 1123
/*      */     //   1045: aload_3
/*      */     //   1046: iconst_0
/*      */     //   1047: iconst_2
/*      */     //   1048: invokevirtual 274	java/lang/String:substring	(II)Ljava/lang/String;
/*      */     //   1051: invokestatic 275	java/lang/Integer:parseInt	(Ljava/lang/String;)I
/*      */     //   1054: istore 9
/*      */     //   1056: aload_3
/*      */     //   1057: iconst_3
/*      */     //   1058: iconst_5
/*      */     //   1059: invokevirtual 274	java/lang/String:substring	(II)Ljava/lang/String;
/*      */     //   1062: invokestatic 275	java/lang/Integer:parseInt	(Ljava/lang/String;)I
/*      */     //   1065: istore 10
/*      */     //   1067: aload_3
/*      */     //   1068: bipush 6
/*      */     //   1070: bipush 8
/*      */     //   1072: invokevirtual 274	java/lang/String:substring	(II)Ljava/lang/String;
/*      */     //   1075: invokestatic 275	java/lang/Integer:parseInt	(Ljava/lang/String;)I
/*      */     //   1078: istore 11
/*      */     //   1080: aload_0
/*      */     //   1081: getfield 41	com/mysql/jdbc/ResultSet:connection	Lcom/mysql/jdbc/Connection;
/*      */     //   1084: aload 7
/*      */     //   1086: aload_2
/*      */     //   1087: aload_0
/*      */     //   1088: aload 7
/*      */     //   1090: sipush 1970
/*      */     //   1093: iconst_1
/*      */     //   1094: iconst_1
/*      */     //   1095: iload 9
/*      */     //   1097: iload 10
/*      */     //   1099: iload 11
/*      */     //   1101: iconst_0
/*      */     //   1102: invokespecial 475	com/mysql/jdbc/ResultSet:fastTimestampCreate	(Ljava/util/Calendar;IIIIIII)Ljava/sql/Timestamp;
/*      */     //   1105: aload_0
/*      */     //   1106: getfield 41	com/mysql/jdbc/ResultSet:connection	Lcom/mysql/jdbc/Connection;
/*      */     //   1109: invokevirtual 467	com/mysql/jdbc/Connection:getServerTimezoneTZ	()Ljava/util/TimeZone;
/*      */     //   1112: aload 4
/*      */     //   1114: iload 5
/*      */     //   1116: invokestatic 476	com/mysql/jdbc/TimeUtil:changeTimezone	(Lcom/mysql/jdbc/Connection;Ljava/util/Calendar;Ljava/util/Calendar;Ljava/sql/Timestamp;Ljava/util/TimeZone;Ljava/util/TimeZone;Z)Ljava/sql/Timestamp;
/*      */     //   1119: aload 8
/*      */     //   1121: monitorexit
/*      */     //   1122: areturn
/*      */     //   1123: aload_3
/*      */     //   1124: iconst_0
/*      */     //   1125: iconst_4
/*      */     //   1126: invokevirtual 274	java/lang/String:substring	(II)Ljava/lang/String;
/*      */     //   1129: invokestatic 275	java/lang/Integer:parseInt	(Ljava/lang/String;)I
/*      */     //   1132: istore 9
/*      */     //   1134: aload_3
/*      */     //   1135: iconst_4
/*      */     //   1136: bipush 6
/*      */     //   1138: invokevirtual 274	java/lang/String:substring	(II)Ljava/lang/String;
/*      */     //   1141: invokestatic 275	java/lang/Integer:parseInt	(Ljava/lang/String;)I
/*      */     //   1144: istore 10
/*      */     //   1146: aload_3
/*      */     //   1147: bipush 6
/*      */     //   1149: bipush 8
/*      */     //   1151: invokevirtual 274	java/lang/String:substring	(II)Ljava/lang/String;
/*      */     //   1154: invokestatic 275	java/lang/Integer:parseInt	(Ljava/lang/String;)I
/*      */     //   1157: istore 11
/*      */     //   1159: aload_0
/*      */     //   1160: getfield 41	com/mysql/jdbc/ResultSet:connection	Lcom/mysql/jdbc/Connection;
/*      */     //   1163: aload 7
/*      */     //   1165: aload_2
/*      */     //   1166: aload_0
/*      */     //   1167: aload 7
/*      */     //   1169: iload 9
/*      */     //   1171: sipush 1900
/*      */     //   1174: isub
/*      */     //   1175: iload 10
/*      */     //   1177: iconst_1
/*      */     //   1178: isub
/*      */     //   1179: iload 11
/*      */     //   1181: iconst_0
/*      */     //   1182: iconst_0
/*      */     //   1183: iconst_0
/*      */     //   1184: iconst_0
/*      */     //   1185: invokespecial 475	com/mysql/jdbc/ResultSet:fastTimestampCreate	(Ljava/util/Calendar;IIIIIII)Ljava/sql/Timestamp;
/*      */     //   1188: aload_0
/*      */     //   1189: getfield 41	com/mysql/jdbc/ResultSet:connection	Lcom/mysql/jdbc/Connection;
/*      */     //   1192: invokevirtual 467	com/mysql/jdbc/Connection:getServerTimezoneTZ	()Ljava/util/TimeZone;
/*      */     //   1195: aload 4
/*      */     //   1197: iload 5
/*      */     //   1199: invokestatic 476	com/mysql/jdbc/TimeUtil:changeTimezone	(Lcom/mysql/jdbc/Connection;Ljava/util/Calendar;Ljava/util/Calendar;Ljava/sql/Timestamp;Ljava/util/TimeZone;Ljava/util/TimeZone;Z)Ljava/sql/Timestamp;
/*      */     //   1202: aload 8
/*      */     //   1204: monitorexit
/*      */     //   1205: areturn
/*      */     //   1206: aload_3
/*      */     //   1207: iconst_0
/*      */     //   1208: iconst_2
/*      */     //   1209: invokevirtual 274	java/lang/String:substring	(II)Ljava/lang/String;
/*      */     //   1212: invokestatic 275	java/lang/Integer:parseInt	(Ljava/lang/String;)I
/*      */     //   1215: istore 9
/*      */     //   1217: iload 9
/*      */     //   1219: bipush 69
/*      */     //   1221: if_icmpgt +10 -> 1231
/*      */     //   1224: iload 9
/*      */     //   1226: bipush 100
/*      */     //   1228: iadd
/*      */     //   1229: istore 9
/*      */     //   1231: aload_3
/*      */     //   1232: iconst_2
/*      */     //   1233: iconst_4
/*      */     //   1234: invokevirtual 274	java/lang/String:substring	(II)Ljava/lang/String;
/*      */     //   1237: invokestatic 275	java/lang/Integer:parseInt	(Ljava/lang/String;)I
/*      */     //   1240: istore 10
/*      */     //   1242: aload_3
/*      */     //   1243: iconst_4
/*      */     //   1244: bipush 6
/*      */     //   1246: invokevirtual 274	java/lang/String:substring	(II)Ljava/lang/String;
/*      */     //   1249: invokestatic 275	java/lang/Integer:parseInt	(Ljava/lang/String;)I
/*      */     //   1252: istore 11
/*      */     //   1254: aload_0
/*      */     //   1255: getfield 41	com/mysql/jdbc/ResultSet:connection	Lcom/mysql/jdbc/Connection;
/*      */     //   1258: aload 7
/*      */     //   1260: aload_2
/*      */     //   1261: aload_0
/*      */     //   1262: aload 7
/*      */     //   1264: iload 9
/*      */     //   1266: sipush 1900
/*      */     //   1269: iadd
/*      */     //   1270: iload 10
/*      */     //   1272: iload 11
/*      */     //   1274: iconst_0
/*      */     //   1275: iconst_0
/*      */     //   1276: iconst_0
/*      */     //   1277: iconst_0
/*      */     //   1278: invokespecial 475	com/mysql/jdbc/ResultSet:fastTimestampCreate	(Ljava/util/Calendar;IIIIIII)Ljava/sql/Timestamp;
/*      */     //   1281: aload_0
/*      */     //   1282: getfield 41	com/mysql/jdbc/ResultSet:connection	Lcom/mysql/jdbc/Connection;
/*      */     //   1285: invokevirtual 467	com/mysql/jdbc/Connection:getServerTimezoneTZ	()Ljava/util/TimeZone;
/*      */     //   1288: aload 4
/*      */     //   1290: iload 5
/*      */     //   1292: invokestatic 476	com/mysql/jdbc/TimeUtil:changeTimezone	(Lcom/mysql/jdbc/Connection;Ljava/util/Calendar;Ljava/util/Calendar;Ljava/sql/Timestamp;Ljava/util/TimeZone;Ljava/util/TimeZone;Z)Ljava/sql/Timestamp;
/*      */     //   1295: aload 8
/*      */     //   1297: monitorexit
/*      */     //   1298: areturn
/*      */     //   1299: aload_3
/*      */     //   1300: iconst_0
/*      */     //   1301: iconst_2
/*      */     //   1302: invokevirtual 274	java/lang/String:substring	(II)Ljava/lang/String;
/*      */     //   1305: invokestatic 275	java/lang/Integer:parseInt	(Ljava/lang/String;)I
/*      */     //   1308: istore 9
/*      */     //   1310: iload 9
/*      */     //   1312: bipush 69
/*      */     //   1314: if_icmpgt +10 -> 1324
/*      */     //   1317: iload 9
/*      */     //   1319: bipush 100
/*      */     //   1321: iadd
/*      */     //   1322: istore 9
/*      */     //   1324: aload_3
/*      */     //   1325: iconst_2
/*      */     //   1326: iconst_4
/*      */     //   1327: invokevirtual 274	java/lang/String:substring	(II)Ljava/lang/String;
/*      */     //   1330: invokestatic 275	java/lang/Integer:parseInt	(Ljava/lang/String;)I
/*      */     //   1333: istore 10
/*      */     //   1335: aload_0
/*      */     //   1336: getfield 41	com/mysql/jdbc/ResultSet:connection	Lcom/mysql/jdbc/Connection;
/*      */     //   1339: aload 7
/*      */     //   1341: aload_2
/*      */     //   1342: aload_0
/*      */     //   1343: aload 7
/*      */     //   1345: iload 9
/*      */     //   1347: sipush 1900
/*      */     //   1350: iadd
/*      */     //   1351: iload 10
/*      */     //   1353: iconst_1
/*      */     //   1354: iconst_0
/*      */     //   1355: iconst_0
/*      */     //   1356: iconst_0
/*      */     //   1357: iconst_0
/*      */     //   1358: invokespecial 475	com/mysql/jdbc/ResultSet:fastTimestampCreate	(Ljava/util/Calendar;IIIIIII)Ljava/sql/Timestamp;
/*      */     //   1361: aload_0
/*      */     //   1362: getfield 41	com/mysql/jdbc/ResultSet:connection	Lcom/mysql/jdbc/Connection;
/*      */     //   1365: invokevirtual 467	com/mysql/jdbc/Connection:getServerTimezoneTZ	()Ljava/util/TimeZone;
/*      */     //   1368: aload 4
/*      */     //   1370: iload 5
/*      */     //   1372: invokestatic 476	com/mysql/jdbc/TimeUtil:changeTimezone	(Lcom/mysql/jdbc/Connection;Ljava/util/Calendar;Ljava/util/Calendar;Ljava/sql/Timestamp;Ljava/util/TimeZone;Ljava/util/TimeZone;Z)Ljava/sql/Timestamp;
/*      */     //   1375: aload 8
/*      */     //   1377: monitorexit
/*      */     //   1378: areturn
/*      */     //   1379: aload_3
/*      */     //   1380: iconst_0
/*      */     //   1381: iconst_2
/*      */     //   1382: invokevirtual 274	java/lang/String:substring	(II)Ljava/lang/String;
/*      */     //   1385: invokestatic 275	java/lang/Integer:parseInt	(Ljava/lang/String;)I
/*      */     //   1388: istore 9
/*      */     //   1390: iload 9
/*      */     //   1392: bipush 69
/*      */     //   1394: if_icmpgt +10 -> 1404
/*      */     //   1397: iload 9
/*      */     //   1399: bipush 100
/*      */     //   1401: iadd
/*      */     //   1402: istore 9
/*      */     //   1404: aload_0
/*      */     //   1405: getfield 41	com/mysql/jdbc/ResultSet:connection	Lcom/mysql/jdbc/Connection;
/*      */     //   1408: aload 7
/*      */     //   1410: aload_2
/*      */     //   1411: aload_0
/*      */     //   1412: aconst_null
/*      */     //   1413: iload 9
/*      */     //   1415: sipush 1900
/*      */     //   1418: iadd
/*      */     //   1419: iconst_1
/*      */     //   1420: iconst_1
/*      */     //   1421: iconst_0
/*      */     //   1422: iconst_0
/*      */     //   1423: iconst_0
/*      */     //   1424: iconst_0
/*      */     //   1425: invokespecial 475	com/mysql/jdbc/ResultSet:fastTimestampCreate	(Ljava/util/Calendar;IIIIIII)Ljava/sql/Timestamp;
/*      */     //   1428: aload_0
/*      */     //   1429: getfield 41	com/mysql/jdbc/ResultSet:connection	Lcom/mysql/jdbc/Connection;
/*      */     //   1432: invokevirtual 467	com/mysql/jdbc/Connection:getServerTimezoneTZ	()Ljava/util/TimeZone;
/*      */     //   1435: aload 4
/*      */     //   1437: iload 5
/*      */     //   1439: invokestatic 476	com/mysql/jdbc/TimeUtil:changeTimezone	(Lcom/mysql/jdbc/Connection;Ljava/util/Calendar;Ljava/util/Calendar;Ljava/sql/Timestamp;Ljava/util/TimeZone;Ljava/util/TimeZone;Z)Ljava/sql/Timestamp;
/*      */     //   1442: aload 8
/*      */     //   1444: monitorexit
/*      */     //   1445: areturn
/*      */     //   1446: new 189	java/sql/SQLException
/*      */     //   1449: dup
/*      */     //   1450: new 133	java/lang/StringBuffer
/*      */     //   1453: dup
/*      */     //   1454: invokespecial 134	java/lang/StringBuffer:<init>	()V
/*      */     //   1457: ldc_w 547
/*      */     //   1460: invokevirtual 136	java/lang/StringBuffer:append	(Ljava/lang/String;)Ljava/lang/StringBuffer;
/*      */     //   1463: aload_3
/*      */     //   1464: invokevirtual 136	java/lang/StringBuffer:append	(Ljava/lang/String;)Ljava/lang/StringBuffer;
/*      */     //   1467: ldc_w 548
/*      */     //   1470: invokevirtual 136	java/lang/StringBuffer:append	(Ljava/lang/String;)Ljava/lang/StringBuffer;
/*      */     //   1473: iload_1
/*      */     //   1474: invokevirtual 179	java/lang/StringBuffer:append	(I)Ljava/lang/StringBuffer;
/*      */     //   1477: ldc -34
/*      */     //   1479: invokevirtual 136	java/lang/StringBuffer:append	(Ljava/lang/String;)Ljava/lang/StringBuffer;
/*      */     //   1482: invokevirtual 139	java/lang/StringBuffer:toString	()Ljava/lang/String;
/*      */     //   1485: ldc 83
/*      */     //   1487: invokespecial 190	java/sql/SQLException:<init>	(Ljava/lang/String;Ljava/lang/String;)V
/*      */     //   1490: athrow
/*      */     //   1491: astore 17
/*      */     //   1493: aload 8
/*      */     //   1495: monitorexit
/*      */     //   1496: aload 17
/*      */     //   1498: athrow
/*      */     //   1499: astore 6
/*      */     //   1501: new 189	java/sql/SQLException
/*      */     //   1504: dup
/*      */     //   1505: new 133	java/lang/StringBuffer
/*      */     //   1508: dup
/*      */     //   1509: invokespecial 134	java/lang/StringBuffer:<init>	()V
/*      */     //   1512: ldc_w 549
/*      */     //   1515: invokevirtual 136	java/lang/StringBuffer:append	(Ljava/lang/String;)Ljava/lang/StringBuffer;
/*      */     //   1518: aload_3
/*      */     //   1519: invokevirtual 136	java/lang/StringBuffer:append	(Ljava/lang/String;)Ljava/lang/StringBuffer;
/*      */     //   1522: ldc_w 550
/*      */     //   1525: invokevirtual 136	java/lang/StringBuffer:append	(Ljava/lang/String;)Ljava/lang/StringBuffer;
/*      */     //   1528: iload_1
/*      */     //   1529: invokevirtual 179	java/lang/StringBuffer:append	(I)Ljava/lang/StringBuffer;
/*      */     //   1532: ldc_w 551
/*      */     //   1535: invokevirtual 136	java/lang/StringBuffer:append	(Ljava/lang/String;)Ljava/lang/StringBuffer;
/*      */     //   1538: invokevirtual 139	java/lang/StringBuffer:toString	()Ljava/lang/String;
/*      */     //   1541: ldc 83
/*      */     //   1543: invokespecial 190	java/sql/SQLException:<init>	(Ljava/lang/String;Ljava/lang/String;)V
/*      */     //   1546: athrow
/*      */     // Line number table:
/*      */     //   Java source line #6285	-> byte code offset #0
/*      */     //   Java source line #6287	-> byte code offset #5
/*      */     //   Java source line #6288	-> byte code offset #9
/*      */     //   Java source line #6290	-> byte code offset #14
/*      */     //   Java source line #6301	-> byte code offset #16
/*      */     //   Java source line #6303	-> byte code offset #21
/*      */     //   Java source line #6305	-> byte code offset #27
/*      */     //   Java source line #6309	-> byte code offset #53
/*      */     //   Java source line #6310	-> byte code offset #59
/*      */     //   Java source line #6317	-> byte code offset #113
/*      */     //   Java source line #6319	-> byte code offset #129
/*      */     //   Java source line #6321	-> byte code offset #134
/*      */     //   Java source line #6322	-> byte code offset #139
/*      */     //   Java source line #6324	-> byte code offset #155
/*      */     //   Java source line #6331	-> byte code offset #187
/*      */     //   Java source line #6333	-> byte code offset #203
/*      */     //   Java source line #6335	-> byte code offset #219
/*      */     //   Java source line #6345	-> byte code offset #265
/*      */     //   Java source line #6346	-> byte code offset #274
/*      */     //   Java source line #6351	-> byte code offset #286
/*      */     //   Java source line #6360	-> byte code offset #404
/*      */     //   Java source line #6361	-> byte code offset #415
/*      */     //   Java source line #6363	-> byte code offset #427
/*      */     //   Java source line #6364	-> byte code offset #440
/*      */     //   Java source line #6366	-> byte code offset #453
/*      */     //   Java source line #6368	-> byte code offset #466
/*      */     //   Java source line #6371	-> byte code offset #479
/*      */     //   Java source line #6373	-> byte code offset #482
/*      */     //   Java source line #6374	-> byte code offset #489
/*      */     //   Java source line #6376	-> byte code offset #497
/*      */     //   Java source line #6377	-> byte code offset #503
/*      */     //   Java source line #6378	-> byte code offset #514
/*      */     //   Java source line #6381	-> byte code offset #530
/*      */     //   Java source line #6391	-> byte code offset #538
/*      */     //   Java source line #6400	-> byte code offset #583
/*      */     //   Java source line #6401	-> byte code offset #594
/*      */     //   Java source line #6403	-> byte code offset #606
/*      */     //   Java source line #6404	-> byte code offset #619
/*      */     //   Java source line #6406	-> byte code offset #632
/*      */     //   Java source line #6408	-> byte code offset #645
/*      */     //   Java source line #6411	-> byte code offset #658
/*      */     //   Java source line #6420	-> byte code offset #702
/*      */     //   Java source line #6422	-> byte code offset #713
/*      */     //   Java source line #6423	-> byte code offset #720
/*      */     //   Java source line #6426	-> byte code offset #727
/*      */     //   Java source line #6428	-> byte code offset #738
/*      */     //   Java source line #6429	-> byte code offset #750
/*      */     //   Java source line #6430	-> byte code offset #763
/*      */     //   Java source line #6432	-> byte code offset #776
/*      */     //   Java source line #6435	-> byte code offset #789
/*      */     //   Java source line #6450	-> byte code offset #837
/*      */     //   Java source line #6452	-> byte code offset #864
/*      */     //   Java source line #6453	-> byte code offset #875
/*      */     //   Java source line #6455	-> byte code offset #887
/*      */     //   Java source line #6456	-> byte code offset #900
/*      */     //   Java source line #6457	-> byte code offset #903
/*      */     //   Java source line #6459	-> byte code offset #909
/*      */     //   Java source line #6461	-> byte code offset #920
/*      */     //   Java source line #6462	-> byte code offset #927
/*      */     //   Java source line #6465	-> byte code offset #934
/*      */     //   Java source line #6467	-> byte code offset #945
/*      */     //   Java source line #6468	-> byte code offset #957
/*      */     //   Java source line #6469	-> byte code offset #970
/*      */     //   Java source line #6472	-> byte code offset #983
/*      */     //   Java source line #6475	-> byte code offset #991
/*      */     //   Java source line #6484	-> byte code offset #1034
/*      */     //   Java source line #6485	-> byte code offset #1045
/*      */     //   Java source line #6487	-> byte code offset #1056
/*      */     //   Java source line #6489	-> byte code offset #1067
/*      */     //   Java source line #6492	-> byte code offset #1080
/*      */     //   Java source line #6503	-> byte code offset #1123
/*      */     //   Java source line #6504	-> byte code offset #1134
/*      */     //   Java source line #6506	-> byte code offset #1146
/*      */     //   Java source line #6508	-> byte code offset #1159
/*      */     //   Java source line #6517	-> byte code offset #1206
/*      */     //   Java source line #6519	-> byte code offset #1217
/*      */     //   Java source line #6520	-> byte code offset #1224
/*      */     //   Java source line #6523	-> byte code offset #1231
/*      */     //   Java source line #6525	-> byte code offset #1242
/*      */     //   Java source line #6527	-> byte code offset #1254
/*      */     //   Java source line #6536	-> byte code offset #1299
/*      */     //   Java source line #6538	-> byte code offset #1310
/*      */     //   Java source line #6539	-> byte code offset #1317
/*      */     //   Java source line #6542	-> byte code offset #1324
/*      */     //   Java source line #6545	-> byte code offset #1335
/*      */     //   Java source line #6554	-> byte code offset #1379
/*      */     //   Java source line #6556	-> byte code offset #1390
/*      */     //   Java source line #6557	-> byte code offset #1397
/*      */     //   Java source line #6560	-> byte code offset #1404
/*      */     //   Java source line #6569	-> byte code offset #1446
/*      */     //   Java source line #6575	-> byte code offset #1491
/*      */     //   Java source line #6576	-> byte code offset #1499
/*      */     //   Java source line #6577	-> byte code offset #1501
/*      */     // Local variable table:
/*      */     //   start	length	slot	name	signature
/*      */     //   0	1547	0	this	ResultSet
/*      */     //   0	1547	1	columnIndex	int
/*      */     //   0	1547	2	targetCalendar	Calendar
/*      */     //   0	1547	3	timestampValue	String
/*      */     //   0	1547	4	tz	TimeZone
/*      */     //   0	1547	5	rollForward	boolean
/*      */     //   25	458	6	length	int
/*      */     //   1499	3	6	e	Exception
/*      */     //   51	1358	7	sessionCalendar	Calendar
/*      */     //   413	136	9	year	int
/*      */     //   592	77	9	year	int
/*      */     //   711	89	9	year	int
/*      */     //   873	3	9	year	int
/*      */     //   918	84	9	year	int
/*      */     //   1054	42	9	hour	int
/*      */     //   1132	38	9	year	int
/*      */     //   1215	50	9	year	int
/*      */     //   1308	38	9	year	int
/*      */     //   1388	26	9	year	int
/*      */     //   425	126	10	month	int
/*      */     //   604	67	10	month	int
/*      */     //   736	70	10	month	int
/*      */     //   885	3	10	month	int
/*      */     //   943	61	10	month	int
/*      */     //   1065	33	10	minutes	int
/*      */     //   1144	32	10	month	int
/*      */     //   1240	31	10	month	int
/*      */     //   1333	19	10	month	int
/*      */     //   438	115	11	day	int
/*      */     //   617	56	11	day	int
/*      */     //   748	60	11	day	int
/*      */     //   898	3	11	day	int
/*      */     //   955	51	11	day	int
/*      */     //   1078	22	11	seconds	int
/*      */     //   1157	23	11	day	int
/*      */     //   1252	21	11	day	int
/*      */     //   451	104	12	hour	int
/*      */     //   630	45	12	hour	int
/*      */     //   761	49	12	hour	int
/*      */     //   901	3	12	hour	int
/*      */     //   968	40	12	hour	int
/*      */     //   464	93	13	minutes	int
/*      */     //   643	34	13	minutes	int
/*      */     //   774	38	13	minutes	int
/*      */     //   904	3	13	minutes	int
/*      */     //   981	29	13	minutes	int
/*      */     //   477	82	14	seconds	int
/*      */     //   656	23	14	seconds	int
/*      */     //   787	27	14	seconds	int
/*      */     //   480	81	15	nanos	int
/*      */     //   495	21	16	decimalIndex	int
/*      */     //   1491	6	17	localObject1	Object
/*      */     // Exception table:
/*      */     //   from	to	target	type
/*      */     //   59	138	1491	finally
/*      */     //   139	202	1491	finally
/*      */     //   203	264	1491	finally
/*      */     //   265	582	1491	finally
/*      */     //   583	701	1491	finally
/*      */     //   702	836	1491	finally
/*      */     //   837	1033	1491	finally
/*      */     //   1034	1122	1491	finally
/*      */     //   1123	1205	1491	finally
/*      */     //   1206	1298	1491	finally
/*      */     //   1299	1378	1491	finally
/*      */     //   1379	1445	1491	finally
/*      */     //   1446	1496	1491	finally
/*      */     //   0	15	1499	java/lang/Exception
/*      */     //   16	138	1499	java/lang/Exception
/*      */     //   139	202	1499	java/lang/Exception
/*      */     //   203	264	1499	java/lang/Exception
/*      */     //   265	582	1499	java/lang/Exception
/*      */     //   583	701	1499	java/lang/Exception
/*      */     //   702	836	1499	java/lang/Exception
/*      */     //   837	1033	1499	java/lang/Exception
/*      */     //   1034	1122	1499	java/lang/Exception
/*      */     //   1123	1205	1499	java/lang/Exception
/*      */     //   1206	1298	1499	java/lang/Exception
/*      */     //   1299	1378	1499	java/lang/Exception
/*      */     //   1379	1445	1499	java/lang/Exception
/*      */     //   1446	1499	1499	java/lang/Exception
/*      */   }
/*      */   
/*      */   /* Error */
/*      */   private Timestamp getTimestampFromBytes(int columnIndex, Calendar targetCalendar, byte[] timestampAsBytes, TimeZone tz, boolean rollForward)
/*      */     throws SQLException
/*      */   {
/*      */     // Byte code:
/*      */     //   0: aload_0
/*      */     //   1: iload_1
/*      */     //   2: invokevirtual 157	com/mysql/jdbc/ResultSet:checkColumnBounds	(I)V
/*      */     //   5: aload_0
/*      */     //   6: iconst_0
/*      */     //   7: putfield 34	com/mysql/jdbc/ResultSet:wasNullFlag	Z
/*      */     //   10: aload_3
/*      */     //   11: ifnonnull +10 -> 21
/*      */     //   14: aload_0
/*      */     //   15: iconst_1
/*      */     //   16: putfield 34	com/mysql/jdbc/ResultSet:wasNullFlag	Z
/*      */     //   19: aconst_null
/*      */     //   20: areturn
/*      */     //   21: aload_3
/*      */     //   22: arraylength
/*      */     //   23: istore 6
/*      */     //   25: aload_0
/*      */     //   26: getfield 41	com/mysql/jdbc/ResultSet:connection	Lcom/mysql/jdbc/Connection;
/*      */     //   29: invokevirtual 473	com/mysql/jdbc/Connection:getUseJDBCCompliantTimezoneShift	()Z
/*      */     //   32: ifeq +13 -> 45
/*      */     //   35: aload_0
/*      */     //   36: getfield 41	com/mysql/jdbc/ResultSet:connection	Lcom/mysql/jdbc/Connection;
/*      */     //   39: invokevirtual 474	com/mysql/jdbc/Connection:getUtcCalendar	()Ljava/util/Calendar;
/*      */     //   42: goto +7 -> 49
/*      */     //   45: aload_0
/*      */     //   46: invokespecial 416	com/mysql/jdbc/ResultSet:getCalendarInstanceForSessionOrNew	()Ljava/util/Calendar;
/*      */     //   49: astore 7
/*      */     //   51: aload 7
/*      */     //   53: dup
/*      */     //   54: astore 8
/*      */     //   56: monitorenter
/*      */     //   57: iconst_1
/*      */     //   58: istore 9
/*      */     //   60: aload_3
/*      */     //   61: bipush 58
/*      */     //   63: invokestatic 282	com/mysql/jdbc/StringUtils:indexOf	([BC)I
/*      */     //   66: iconst_m1
/*      */     //   67: if_icmpeq +7 -> 74
/*      */     //   70: iconst_1
/*      */     //   71: goto +4 -> 75
/*      */     //   74: iconst_0
/*      */     //   75: istore 10
/*      */     //   77: iconst_0
/*      */     //   78: istore 11
/*      */     //   80: iload 11
/*      */     //   82: iload 6
/*      */     //   84: if_icmpge +87 -> 171
/*      */     //   87: aload_3
/*      */     //   88: iload 11
/*      */     //   90: baload
/*      */     //   91: istore 12
/*      */     //   93: iload 12
/*      */     //   95: bipush 32
/*      */     //   97: if_icmpeq +17 -> 114
/*      */     //   100: iload 12
/*      */     //   102: bipush 45
/*      */     //   104: if_icmpeq +10 -> 114
/*      */     //   107: iload 12
/*      */     //   109: bipush 47
/*      */     //   111: if_icmpne +6 -> 117
/*      */     //   114: iconst_0
/*      */     //   115: istore 10
/*      */     //   117: iload 12
/*      */     //   119: bipush 48
/*      */     //   121: if_icmpeq +44 -> 165
/*      */     //   124: iload 12
/*      */     //   126: bipush 32
/*      */     //   128: if_icmpeq +37 -> 165
/*      */     //   131: iload 12
/*      */     //   133: bipush 58
/*      */     //   135: if_icmpeq +30 -> 165
/*      */     //   138: iload 12
/*      */     //   140: bipush 45
/*      */     //   142: if_icmpeq +23 -> 165
/*      */     //   145: iload 12
/*      */     //   147: bipush 47
/*      */     //   149: if_icmpeq +16 -> 165
/*      */     //   152: iload 12
/*      */     //   154: bipush 46
/*      */     //   156: if_icmpeq +9 -> 165
/*      */     //   159: iconst_0
/*      */     //   160: istore 9
/*      */     //   162: goto +9 -> 171
/*      */     //   165: iinc 11 1
/*      */     //   168: goto -88 -> 80
/*      */     //   171: iload 10
/*      */     //   173: ifne +98 -> 271
/*      */     //   176: iload 9
/*      */     //   178: ifeq +93 -> 271
/*      */     //   181: ldc_w 268
/*      */     //   184: aload_0
/*      */     //   185: getfield 41	com/mysql/jdbc/ResultSet:connection	Lcom/mysql/jdbc/Connection;
/*      */     //   188: invokevirtual 269	com/mysql/jdbc/Connection:getZeroDateTimeBehavior	()Ljava/lang/String;
/*      */     //   191: invokevirtual 216	java/lang/String:equals	(Ljava/lang/Object;)Z
/*      */     //   194: ifeq +13 -> 207
/*      */     //   197: aload_0
/*      */     //   198: iconst_1
/*      */     //   199: putfield 34	com/mysql/jdbc/ResultSet:wasNullFlag	Z
/*      */     //   202: aconst_null
/*      */     //   203: aload 8
/*      */     //   205: monitorexit
/*      */     //   206: areturn
/*      */     //   207: ldc_w 270
/*      */     //   210: aload_0
/*      */     //   211: getfield 41	com/mysql/jdbc/ResultSet:connection	Lcom/mysql/jdbc/Connection;
/*      */     //   214: invokevirtual 269	com/mysql/jdbc/Connection:getZeroDateTimeBehavior	()Ljava/lang/String;
/*      */     //   217: invokevirtual 216	java/lang/String:equals	(Ljava/lang/Object;)Z
/*      */     //   220: ifeq +35 -> 255
/*      */     //   223: new 133	java/lang/StringBuffer
/*      */     //   226: dup
/*      */     //   227: invokespecial 134	java/lang/StringBuffer:<init>	()V
/*      */     //   230: ldc_w 271
/*      */     //   233: invokevirtual 136	java/lang/StringBuffer:append	(Ljava/lang/String;)Ljava/lang/StringBuffer;
/*      */     //   236: aload_3
/*      */     //   237: invokevirtual 181	java/lang/StringBuffer:append	(Ljava/lang/Object;)Ljava/lang/StringBuffer;
/*      */     //   240: ldc_w 541
/*      */     //   243: invokevirtual 136	java/lang/StringBuffer:append	(Ljava/lang/String;)Ljava/lang/StringBuffer;
/*      */     //   246: invokevirtual 139	java/lang/StringBuffer:toString	()Ljava/lang/String;
/*      */     //   249: ldc 83
/*      */     //   251: invokestatic 84	com/mysql/jdbc/SQLError:createSQLException	(Ljava/lang/String;Ljava/lang/String;)Ljava/sql/SQLException;
/*      */     //   254: athrow
/*      */     //   255: aload_0
/*      */     //   256: aconst_null
/*      */     //   257: iconst_1
/*      */     //   258: iconst_1
/*      */     //   259: iconst_1
/*      */     //   260: iconst_0
/*      */     //   261: iconst_0
/*      */     //   262: iconst_0
/*      */     //   263: iconst_0
/*      */     //   264: invokespecial 475	com/mysql/jdbc/ResultSet:fastTimestampCreate	(Ljava/util/Calendar;IIIIIII)Ljava/sql/Timestamp;
/*      */     //   267: aload 8
/*      */     //   269: monitorexit
/*      */     //   270: areturn
/*      */     //   271: aload_0
/*      */     //   272: getfield 40	com/mysql/jdbc/ResultSet:fields	[Lcom/mysql/jdbc/Field;
/*      */     //   275: iload_1
/*      */     //   276: iconst_1
/*      */     //   277: isub
/*      */     //   278: aaload
/*      */     //   279: invokevirtual 183	com/mysql/jdbc/Field:getMysqlType	()I
/*      */     //   282: bipush 13
/*      */     //   284: if_icmpne +46 -> 330
/*      */     //   287: aload_0
/*      */     //   288: getfield 41	com/mysql/jdbc/ResultSet:connection	Lcom/mysql/jdbc/Connection;
/*      */     //   291: aload 7
/*      */     //   293: aload_2
/*      */     //   294: aload_0
/*      */     //   295: aload 7
/*      */     //   297: aload_3
/*      */     //   298: iconst_0
/*      */     //   299: iconst_4
/*      */     //   300: invokestatic 283	com/mysql/jdbc/StringUtils:getInt	([BII)I
/*      */     //   303: iconst_1
/*      */     //   304: iconst_1
/*      */     //   305: iconst_0
/*      */     //   306: iconst_0
/*      */     //   307: iconst_0
/*      */     //   308: iconst_0
/*      */     //   309: invokespecial 475	com/mysql/jdbc/ResultSet:fastTimestampCreate	(Ljava/util/Calendar;IIIIIII)Ljava/sql/Timestamp;
/*      */     //   312: aload_0
/*      */     //   313: getfield 41	com/mysql/jdbc/ResultSet:connection	Lcom/mysql/jdbc/Connection;
/*      */     //   316: invokevirtual 467	com/mysql/jdbc/Connection:getServerTimezoneTZ	()Ljava/util/TimeZone;
/*      */     //   319: aload 4
/*      */     //   321: iload 5
/*      */     //   323: invokestatic 476	com/mysql/jdbc/TimeUtil:changeTimezone	(Lcom/mysql/jdbc/Connection;Ljava/util/Calendar;Ljava/util/Calendar;Ljava/sql/Timestamp;Ljava/util/TimeZone;Ljava/util/TimeZone;Z)Ljava/sql/Timestamp;
/*      */     //   326: aload 8
/*      */     //   328: monitorexit
/*      */     //   329: areturn
/*      */     //   330: aload_3
/*      */     //   331: iload 6
/*      */     //   333: iconst_1
/*      */     //   334: isub
/*      */     //   335: baload
/*      */     //   336: bipush 46
/*      */     //   338: if_icmpne +6 -> 344
/*      */     //   341: iinc 6 -1
/*      */     //   344: iload 6
/*      */     //   346: tableswitch	default:+1037->1383, 2:+973->1319, 3:+1037->1383, 4:+899->1245, 5:+1037->1383, 6:+815->1161, 7:+1037->1383, 8:+662->1008, 9:+1037->1383, 10:+490->836, 11:+1037->1383, 12:+373->719, 13:+1037->1383, 14:+272->618, 15:+1037->1383, 16:+1037->1383, 17:+1037->1383, 18:+1037->1383, 19:+114->460, 20:+114->460, 21:+114->460, 22:+114->460, 23:+114->460, 24:+114->460, 25:+114->460, 26:+114->460
/*      */     //   460: aload_3
/*      */     //   461: iconst_0
/*      */     //   462: iconst_4
/*      */     //   463: invokestatic 283	com/mysql/jdbc/StringUtils:getInt	([BII)I
/*      */     //   466: istore 11
/*      */     //   468: aload_3
/*      */     //   469: iconst_5
/*      */     //   470: bipush 7
/*      */     //   472: invokestatic 283	com/mysql/jdbc/StringUtils:getInt	([BII)I
/*      */     //   475: istore 12
/*      */     //   477: aload_3
/*      */     //   478: bipush 8
/*      */     //   480: bipush 10
/*      */     //   482: invokestatic 283	com/mysql/jdbc/StringUtils:getInt	([BII)I
/*      */     //   485: istore 13
/*      */     //   487: aload_3
/*      */     //   488: bipush 11
/*      */     //   490: bipush 13
/*      */     //   492: invokestatic 283	com/mysql/jdbc/StringUtils:getInt	([BII)I
/*      */     //   495: istore 14
/*      */     //   497: aload_3
/*      */     //   498: bipush 14
/*      */     //   500: bipush 16
/*      */     //   502: invokestatic 283	com/mysql/jdbc/StringUtils:getInt	([BII)I
/*      */     //   505: istore 15
/*      */     //   507: aload_3
/*      */     //   508: bipush 17
/*      */     //   510: bipush 19
/*      */     //   512: invokestatic 283	com/mysql/jdbc/StringUtils:getInt	([BII)I
/*      */     //   515: istore 16
/*      */     //   517: iconst_0
/*      */     //   518: istore 17
/*      */     //   520: iload 6
/*      */     //   522: bipush 19
/*      */     //   524: if_icmple +49 -> 573
/*      */     //   527: aload_3
/*      */     //   528: bipush 46
/*      */     //   530: invokestatic 552	com/mysql/jdbc/StringUtils:lastIndexOf	([BC)I
/*      */     //   533: istore 18
/*      */     //   535: iload 18
/*      */     //   537: iconst_m1
/*      */     //   538: if_icmpeq +35 -> 573
/*      */     //   541: iload 18
/*      */     //   543: iconst_2
/*      */     //   544: iadd
/*      */     //   545: iload 6
/*      */     //   547: if_icmpgt +18 -> 565
/*      */     //   550: aload_3
/*      */     //   551: iload 18
/*      */     //   553: iconst_1
/*      */     //   554: iadd
/*      */     //   555: iload 6
/*      */     //   557: invokestatic 283	com/mysql/jdbc/StringUtils:getInt	([BII)I
/*      */     //   560: istore 17
/*      */     //   562: goto +11 -> 573
/*      */     //   565: new 543	java/lang/IllegalArgumentException
/*      */     //   568: dup
/*      */     //   569: invokespecial 544	java/lang/IllegalArgumentException:<init>	()V
/*      */     //   572: athrow
/*      */     //   573: aload_0
/*      */     //   574: getfield 41	com/mysql/jdbc/ResultSet:connection	Lcom/mysql/jdbc/Connection;
/*      */     //   577: aload 7
/*      */     //   579: aload_2
/*      */     //   580: aload_0
/*      */     //   581: aload 7
/*      */     //   583: iload 11
/*      */     //   585: iload 12
/*      */     //   587: iload 13
/*      */     //   589: iload 14
/*      */     //   591: iload 15
/*      */     //   593: iload 16
/*      */     //   595: iload 17
/*      */     //   597: invokespecial 475	com/mysql/jdbc/ResultSet:fastTimestampCreate	(Ljava/util/Calendar;IIIIIII)Ljava/sql/Timestamp;
/*      */     //   600: aload_0
/*      */     //   601: getfield 41	com/mysql/jdbc/ResultSet:connection	Lcom/mysql/jdbc/Connection;
/*      */     //   604: invokevirtual 467	com/mysql/jdbc/Connection:getServerTimezoneTZ	()Ljava/util/TimeZone;
/*      */     //   607: aload 4
/*      */     //   609: iload 5
/*      */     //   611: invokestatic 476	com/mysql/jdbc/TimeUtil:changeTimezone	(Lcom/mysql/jdbc/Connection;Ljava/util/Calendar;Ljava/util/Calendar;Ljava/sql/Timestamp;Ljava/util/TimeZone;Ljava/util/TimeZone;Z)Ljava/sql/Timestamp;
/*      */     //   614: aload 8
/*      */     //   616: monitorexit
/*      */     //   617: areturn
/*      */     //   618: aload_3
/*      */     //   619: iconst_0
/*      */     //   620: iconst_4
/*      */     //   621: invokestatic 283	com/mysql/jdbc/StringUtils:getInt	([BII)I
/*      */     //   624: istore 11
/*      */     //   626: aload_3
/*      */     //   627: iconst_4
/*      */     //   628: bipush 6
/*      */     //   630: invokestatic 283	com/mysql/jdbc/StringUtils:getInt	([BII)I
/*      */     //   633: istore 12
/*      */     //   635: aload_3
/*      */     //   636: bipush 6
/*      */     //   638: bipush 8
/*      */     //   640: invokestatic 283	com/mysql/jdbc/StringUtils:getInt	([BII)I
/*      */     //   643: istore 13
/*      */     //   645: aload_3
/*      */     //   646: bipush 8
/*      */     //   648: bipush 10
/*      */     //   650: invokestatic 283	com/mysql/jdbc/StringUtils:getInt	([BII)I
/*      */     //   653: istore 14
/*      */     //   655: aload_3
/*      */     //   656: bipush 10
/*      */     //   658: bipush 12
/*      */     //   660: invokestatic 283	com/mysql/jdbc/StringUtils:getInt	([BII)I
/*      */     //   663: istore 15
/*      */     //   665: aload_3
/*      */     //   666: bipush 12
/*      */     //   668: bipush 14
/*      */     //   670: invokestatic 283	com/mysql/jdbc/StringUtils:getInt	([BII)I
/*      */     //   673: istore 16
/*      */     //   675: aload_0
/*      */     //   676: getfield 41	com/mysql/jdbc/ResultSet:connection	Lcom/mysql/jdbc/Connection;
/*      */     //   679: aload 7
/*      */     //   681: aload_2
/*      */     //   682: aload_0
/*      */     //   683: aload 7
/*      */     //   685: iload 11
/*      */     //   687: iload 12
/*      */     //   689: iload 13
/*      */     //   691: iload 14
/*      */     //   693: iload 15
/*      */     //   695: iload 16
/*      */     //   697: iconst_0
/*      */     //   698: invokespecial 475	com/mysql/jdbc/ResultSet:fastTimestampCreate	(Ljava/util/Calendar;IIIIIII)Ljava/sql/Timestamp;
/*      */     //   701: aload_0
/*      */     //   702: getfield 41	com/mysql/jdbc/ResultSet:connection	Lcom/mysql/jdbc/Connection;
/*      */     //   705: invokevirtual 467	com/mysql/jdbc/Connection:getServerTimezoneTZ	()Ljava/util/TimeZone;
/*      */     //   708: aload 4
/*      */     //   710: iload 5
/*      */     //   712: invokestatic 476	com/mysql/jdbc/TimeUtil:changeTimezone	(Lcom/mysql/jdbc/Connection;Ljava/util/Calendar;Ljava/util/Calendar;Ljava/sql/Timestamp;Ljava/util/TimeZone;Ljava/util/TimeZone;Z)Ljava/sql/Timestamp;
/*      */     //   715: aload 8
/*      */     //   717: monitorexit
/*      */     //   718: areturn
/*      */     //   719: aload_3
/*      */     //   720: iconst_0
/*      */     //   721: iconst_2
/*      */     //   722: invokestatic 283	com/mysql/jdbc/StringUtils:getInt	([BII)I
/*      */     //   725: istore 11
/*      */     //   727: iload 11
/*      */     //   729: bipush 69
/*      */     //   731: if_icmpgt +10 -> 741
/*      */     //   734: iload 11
/*      */     //   736: bipush 100
/*      */     //   738: iadd
/*      */     //   739: istore 11
/*      */     //   741: aload_3
/*      */     //   742: iconst_2
/*      */     //   743: iconst_4
/*      */     //   744: invokestatic 283	com/mysql/jdbc/StringUtils:getInt	([BII)I
/*      */     //   747: istore 12
/*      */     //   749: aload_3
/*      */     //   750: iconst_4
/*      */     //   751: bipush 6
/*      */     //   753: invokestatic 283	com/mysql/jdbc/StringUtils:getInt	([BII)I
/*      */     //   756: istore 13
/*      */     //   758: aload_3
/*      */     //   759: bipush 6
/*      */     //   761: bipush 8
/*      */     //   763: invokestatic 283	com/mysql/jdbc/StringUtils:getInt	([BII)I
/*      */     //   766: istore 14
/*      */     //   768: aload_3
/*      */     //   769: bipush 8
/*      */     //   771: bipush 10
/*      */     //   773: invokestatic 283	com/mysql/jdbc/StringUtils:getInt	([BII)I
/*      */     //   776: istore 15
/*      */     //   778: aload_3
/*      */     //   779: bipush 10
/*      */     //   781: bipush 12
/*      */     //   783: invokestatic 283	com/mysql/jdbc/StringUtils:getInt	([BII)I
/*      */     //   786: istore 16
/*      */     //   788: aload_0
/*      */     //   789: getfield 41	com/mysql/jdbc/ResultSet:connection	Lcom/mysql/jdbc/Connection;
/*      */     //   792: aload 7
/*      */     //   794: aload_2
/*      */     //   795: aload_0
/*      */     //   796: aload 7
/*      */     //   798: iload 11
/*      */     //   800: sipush 1900
/*      */     //   803: iadd
/*      */     //   804: iload 12
/*      */     //   806: iload 13
/*      */     //   808: iload 14
/*      */     //   810: iload 15
/*      */     //   812: iload 16
/*      */     //   814: iconst_0
/*      */     //   815: invokespecial 475	com/mysql/jdbc/ResultSet:fastTimestampCreate	(Ljava/util/Calendar;IIIIIII)Ljava/sql/Timestamp;
/*      */     //   818: aload_0
/*      */     //   819: getfield 41	com/mysql/jdbc/ResultSet:connection	Lcom/mysql/jdbc/Connection;
/*      */     //   822: invokevirtual 467	com/mysql/jdbc/Connection:getServerTimezoneTZ	()Ljava/util/TimeZone;
/*      */     //   825: aload 4
/*      */     //   827: iload 5
/*      */     //   829: invokestatic 476	com/mysql/jdbc/TimeUtil:changeTimezone	(Lcom/mysql/jdbc/Connection;Ljava/util/Calendar;Ljava/util/Calendar;Ljava/sql/Timestamp;Ljava/util/TimeZone;Ljava/util/TimeZone;Z)Ljava/sql/Timestamp;
/*      */     //   832: aload 8
/*      */     //   834: monitorexit
/*      */     //   835: areturn
/*      */     //   836: aload_0
/*      */     //   837: getfield 40	com/mysql/jdbc/ResultSet:fields	[Lcom/mysql/jdbc/Field;
/*      */     //   840: iload_1
/*      */     //   841: iconst_1
/*      */     //   842: isub
/*      */     //   843: aaload
/*      */     //   844: invokevirtual 183	com/mysql/jdbc/Field:getMysqlType	()I
/*      */     //   847: bipush 10
/*      */     //   849: if_icmpeq +13 -> 862
/*      */     //   852: aload_3
/*      */     //   853: bipush 45
/*      */     //   855: invokestatic 282	com/mysql/jdbc/StringUtils:indexOf	([BC)I
/*      */     //   858: iconst_m1
/*      */     //   859: if_icmpeq +39 -> 898
/*      */     //   862: aload_3
/*      */     //   863: iconst_0
/*      */     //   864: iconst_4
/*      */     //   865: invokestatic 283	com/mysql/jdbc/StringUtils:getInt	([BII)I
/*      */     //   868: istore 11
/*      */     //   870: aload_3
/*      */     //   871: iconst_5
/*      */     //   872: bipush 7
/*      */     //   874: invokestatic 283	com/mysql/jdbc/StringUtils:getInt	([BII)I
/*      */     //   877: istore 12
/*      */     //   879: aload_3
/*      */     //   880: bipush 8
/*      */     //   882: bipush 10
/*      */     //   884: invokestatic 283	com/mysql/jdbc/StringUtils:getInt	([BII)I
/*      */     //   887: istore 13
/*      */     //   889: iconst_0
/*      */     //   890: istore 14
/*      */     //   892: iconst_0
/*      */     //   893: istore 15
/*      */     //   895: goto +70 -> 965
/*      */     //   898: aload_3
/*      */     //   899: iconst_0
/*      */     //   900: iconst_2
/*      */     //   901: invokestatic 283	com/mysql/jdbc/StringUtils:getInt	([BII)I
/*      */     //   904: istore 11
/*      */     //   906: iload 11
/*      */     //   908: bipush 69
/*      */     //   910: if_icmpgt +10 -> 920
/*      */     //   913: iload 11
/*      */     //   915: bipush 100
/*      */     //   917: iadd
/*      */     //   918: istore 11
/*      */     //   920: aload_3
/*      */     //   921: iconst_2
/*      */     //   922: iconst_4
/*      */     //   923: invokestatic 283	com/mysql/jdbc/StringUtils:getInt	([BII)I
/*      */     //   926: istore 12
/*      */     //   928: aload_3
/*      */     //   929: iconst_4
/*      */     //   930: bipush 6
/*      */     //   932: invokestatic 283	com/mysql/jdbc/StringUtils:getInt	([BII)I
/*      */     //   935: istore 13
/*      */     //   937: aload_3
/*      */     //   938: bipush 6
/*      */     //   940: bipush 8
/*      */     //   942: invokestatic 283	com/mysql/jdbc/StringUtils:getInt	([BII)I
/*      */     //   945: istore 14
/*      */     //   947: aload_3
/*      */     //   948: bipush 8
/*      */     //   950: bipush 10
/*      */     //   952: invokestatic 283	com/mysql/jdbc/StringUtils:getInt	([BII)I
/*      */     //   955: istore 15
/*      */     //   957: iload 11
/*      */     //   959: sipush 1900
/*      */     //   962: iadd
/*      */     //   963: istore 11
/*      */     //   965: aload_0
/*      */     //   966: getfield 41	com/mysql/jdbc/ResultSet:connection	Lcom/mysql/jdbc/Connection;
/*      */     //   969: aload 7
/*      */     //   971: aload_2
/*      */     //   972: aload_0
/*      */     //   973: aload 7
/*      */     //   975: iload 11
/*      */     //   977: iload 12
/*      */     //   979: iload 13
/*      */     //   981: iload 14
/*      */     //   983: iload 15
/*      */     //   985: iconst_0
/*      */     //   986: iconst_0
/*      */     //   987: invokespecial 475	com/mysql/jdbc/ResultSet:fastTimestampCreate	(Ljava/util/Calendar;IIIIIII)Ljava/sql/Timestamp;
/*      */     //   990: aload_0
/*      */     //   991: getfield 41	com/mysql/jdbc/ResultSet:connection	Lcom/mysql/jdbc/Connection;
/*      */     //   994: invokevirtual 467	com/mysql/jdbc/Connection:getServerTimezoneTZ	()Ljava/util/TimeZone;
/*      */     //   997: aload 4
/*      */     //   999: iload 5
/*      */     //   1001: invokestatic 476	com/mysql/jdbc/TimeUtil:changeTimezone	(Lcom/mysql/jdbc/Connection;Ljava/util/Calendar;Ljava/util/Calendar;Ljava/sql/Timestamp;Ljava/util/TimeZone;Ljava/util/TimeZone;Z)Ljava/sql/Timestamp;
/*      */     //   1004: aload 8
/*      */     //   1006: monitorexit
/*      */     //   1007: areturn
/*      */     //   1008: aload_3
/*      */     //   1009: bipush 58
/*      */     //   1011: invokestatic 282	com/mysql/jdbc/StringUtils:indexOf	([BC)I
/*      */     //   1014: iconst_m1
/*      */     //   1015: if_icmpeq +72 -> 1087
/*      */     //   1018: aload_3
/*      */     //   1019: iconst_0
/*      */     //   1020: iconst_2
/*      */     //   1021: invokestatic 283	com/mysql/jdbc/StringUtils:getInt	([BII)I
/*      */     //   1024: istore 11
/*      */     //   1026: aload_3
/*      */     //   1027: iconst_3
/*      */     //   1028: iconst_5
/*      */     //   1029: invokestatic 283	com/mysql/jdbc/StringUtils:getInt	([BII)I
/*      */     //   1032: istore 12
/*      */     //   1034: aload_3
/*      */     //   1035: bipush 6
/*      */     //   1037: bipush 8
/*      */     //   1039: invokestatic 283	com/mysql/jdbc/StringUtils:getInt	([BII)I
/*      */     //   1042: istore 13
/*      */     //   1044: aload_0
/*      */     //   1045: getfield 41	com/mysql/jdbc/ResultSet:connection	Lcom/mysql/jdbc/Connection;
/*      */     //   1048: aload 7
/*      */     //   1050: aload_2
/*      */     //   1051: aload_0
/*      */     //   1052: aload 7
/*      */     //   1054: sipush 1970
/*      */     //   1057: iconst_1
/*      */     //   1058: iconst_1
/*      */     //   1059: iload 11
/*      */     //   1061: iload 12
/*      */     //   1063: iload 13
/*      */     //   1065: iconst_0
/*      */     //   1066: invokespecial 475	com/mysql/jdbc/ResultSet:fastTimestampCreate	(Ljava/util/Calendar;IIIIIII)Ljava/sql/Timestamp;
/*      */     //   1069: aload_0
/*      */     //   1070: getfield 41	com/mysql/jdbc/ResultSet:connection	Lcom/mysql/jdbc/Connection;
/*      */     //   1073: invokevirtual 467	com/mysql/jdbc/Connection:getServerTimezoneTZ	()Ljava/util/TimeZone;
/*      */     //   1076: aload 4
/*      */     //   1078: iload 5
/*      */     //   1080: invokestatic 476	com/mysql/jdbc/TimeUtil:changeTimezone	(Lcom/mysql/jdbc/Connection;Ljava/util/Calendar;Ljava/util/Calendar;Ljava/sql/Timestamp;Ljava/util/TimeZone;Ljava/util/TimeZone;Z)Ljava/sql/Timestamp;
/*      */     //   1083: aload 8
/*      */     //   1085: monitorexit
/*      */     //   1086: areturn
/*      */     //   1087: aload_3
/*      */     //   1088: iconst_0
/*      */     //   1089: iconst_4
/*      */     //   1090: invokestatic 283	com/mysql/jdbc/StringUtils:getInt	([BII)I
/*      */     //   1093: istore 11
/*      */     //   1095: aload_3
/*      */     //   1096: iconst_4
/*      */     //   1097: bipush 6
/*      */     //   1099: invokestatic 283	com/mysql/jdbc/StringUtils:getInt	([BII)I
/*      */     //   1102: istore 12
/*      */     //   1104: aload_3
/*      */     //   1105: bipush 6
/*      */     //   1107: bipush 8
/*      */     //   1109: invokestatic 283	com/mysql/jdbc/StringUtils:getInt	([BII)I
/*      */     //   1112: istore 13
/*      */     //   1114: aload_0
/*      */     //   1115: getfield 41	com/mysql/jdbc/ResultSet:connection	Lcom/mysql/jdbc/Connection;
/*      */     //   1118: aload 7
/*      */     //   1120: aload_2
/*      */     //   1121: aload_0
/*      */     //   1122: aload 7
/*      */     //   1124: iload 11
/*      */     //   1126: sipush 1900
/*      */     //   1129: isub
/*      */     //   1130: iload 12
/*      */     //   1132: iconst_1
/*      */     //   1133: isub
/*      */     //   1134: iload 13
/*      */     //   1136: iconst_0
/*      */     //   1137: iconst_0
/*      */     //   1138: iconst_0
/*      */     //   1139: iconst_0
/*      */     //   1140: invokespecial 475	com/mysql/jdbc/ResultSet:fastTimestampCreate	(Ljava/util/Calendar;IIIIIII)Ljava/sql/Timestamp;
/*      */     //   1143: aload_0
/*      */     //   1144: getfield 41	com/mysql/jdbc/ResultSet:connection	Lcom/mysql/jdbc/Connection;
/*      */     //   1147: invokevirtual 467	com/mysql/jdbc/Connection:getServerTimezoneTZ	()Ljava/util/TimeZone;
/*      */     //   1150: aload 4
/*      */     //   1152: iload 5
/*      */     //   1154: invokestatic 476	com/mysql/jdbc/TimeUtil:changeTimezone	(Lcom/mysql/jdbc/Connection;Ljava/util/Calendar;Ljava/util/Calendar;Ljava/sql/Timestamp;Ljava/util/TimeZone;Ljava/util/TimeZone;Z)Ljava/sql/Timestamp;
/*      */     //   1157: aload 8
/*      */     //   1159: monitorexit
/*      */     //   1160: areturn
/*      */     //   1161: aload_3
/*      */     //   1162: iconst_0
/*      */     //   1163: iconst_2
/*      */     //   1164: invokestatic 283	com/mysql/jdbc/StringUtils:getInt	([BII)I
/*      */     //   1167: istore 11
/*      */     //   1169: iload 11
/*      */     //   1171: bipush 69
/*      */     //   1173: if_icmpgt +10 -> 1183
/*      */     //   1176: iload 11
/*      */     //   1178: bipush 100
/*      */     //   1180: iadd
/*      */     //   1181: istore 11
/*      */     //   1183: aload_3
/*      */     //   1184: iconst_2
/*      */     //   1185: iconst_4
/*      */     //   1186: invokestatic 283	com/mysql/jdbc/StringUtils:getInt	([BII)I
/*      */     //   1189: istore 12
/*      */     //   1191: aload_3
/*      */     //   1192: iconst_4
/*      */     //   1193: bipush 6
/*      */     //   1195: invokestatic 283	com/mysql/jdbc/StringUtils:getInt	([BII)I
/*      */     //   1198: istore 13
/*      */     //   1200: aload_0
/*      */     //   1201: getfield 41	com/mysql/jdbc/ResultSet:connection	Lcom/mysql/jdbc/Connection;
/*      */     //   1204: aload 7
/*      */     //   1206: aload_2
/*      */     //   1207: aload_0
/*      */     //   1208: aload 7
/*      */     //   1210: iload 11
/*      */     //   1212: sipush 1900
/*      */     //   1215: iadd
/*      */     //   1216: iload 12
/*      */     //   1218: iload 13
/*      */     //   1220: iconst_0
/*      */     //   1221: iconst_0
/*      */     //   1222: iconst_0
/*      */     //   1223: iconst_0
/*      */     //   1224: invokespecial 475	com/mysql/jdbc/ResultSet:fastTimestampCreate	(Ljava/util/Calendar;IIIIIII)Ljava/sql/Timestamp;
/*      */     //   1227: aload_0
/*      */     //   1228: getfield 41	com/mysql/jdbc/ResultSet:connection	Lcom/mysql/jdbc/Connection;
/*      */     //   1231: invokevirtual 467	com/mysql/jdbc/Connection:getServerTimezoneTZ	()Ljava/util/TimeZone;
/*      */     //   1234: aload 4
/*      */     //   1236: iload 5
/*      */     //   1238: invokestatic 476	com/mysql/jdbc/TimeUtil:changeTimezone	(Lcom/mysql/jdbc/Connection;Ljava/util/Calendar;Ljava/util/Calendar;Ljava/sql/Timestamp;Ljava/util/TimeZone;Ljava/util/TimeZone;Z)Ljava/sql/Timestamp;
/*      */     //   1241: aload 8
/*      */     //   1243: monitorexit
/*      */     //   1244: areturn
/*      */     //   1245: aload_3
/*      */     //   1246: iconst_0
/*      */     //   1247: iconst_2
/*      */     //   1248: invokestatic 283	com/mysql/jdbc/StringUtils:getInt	([BII)I
/*      */     //   1251: istore 11
/*      */     //   1253: iload 11
/*      */     //   1255: bipush 69
/*      */     //   1257: if_icmpgt +10 -> 1267
/*      */     //   1260: iload 11
/*      */     //   1262: bipush 100
/*      */     //   1264: iadd
/*      */     //   1265: istore 11
/*      */     //   1267: aload_3
/*      */     //   1268: iconst_2
/*      */     //   1269: iconst_4
/*      */     //   1270: invokestatic 283	com/mysql/jdbc/StringUtils:getInt	([BII)I
/*      */     //   1273: istore 12
/*      */     //   1275: aload_0
/*      */     //   1276: getfield 41	com/mysql/jdbc/ResultSet:connection	Lcom/mysql/jdbc/Connection;
/*      */     //   1279: aload 7
/*      */     //   1281: aload_2
/*      */     //   1282: aload_0
/*      */     //   1283: aload 7
/*      */     //   1285: iload 11
/*      */     //   1287: sipush 1900
/*      */     //   1290: iadd
/*      */     //   1291: iload 12
/*      */     //   1293: iconst_1
/*      */     //   1294: iconst_0
/*      */     //   1295: iconst_0
/*      */     //   1296: iconst_0
/*      */     //   1297: iconst_0
/*      */     //   1298: invokespecial 475	com/mysql/jdbc/ResultSet:fastTimestampCreate	(Ljava/util/Calendar;IIIIIII)Ljava/sql/Timestamp;
/*      */     //   1301: aload_0
/*      */     //   1302: getfield 41	com/mysql/jdbc/ResultSet:connection	Lcom/mysql/jdbc/Connection;
/*      */     //   1305: invokevirtual 467	com/mysql/jdbc/Connection:getServerTimezoneTZ	()Ljava/util/TimeZone;
/*      */     //   1308: aload 4
/*      */     //   1310: iload 5
/*      */     //   1312: invokestatic 476	com/mysql/jdbc/TimeUtil:changeTimezone	(Lcom/mysql/jdbc/Connection;Ljava/util/Calendar;Ljava/util/Calendar;Ljava/sql/Timestamp;Ljava/util/TimeZone;Ljava/util/TimeZone;Z)Ljava/sql/Timestamp;
/*      */     //   1315: aload 8
/*      */     //   1317: monitorexit
/*      */     //   1318: areturn
/*      */     //   1319: aload_3
/*      */     //   1320: iconst_0
/*      */     //   1321: iconst_2
/*      */     //   1322: invokestatic 283	com/mysql/jdbc/StringUtils:getInt	([BII)I
/*      */     //   1325: istore 11
/*      */     //   1327: iload 11
/*      */     //   1329: bipush 69
/*      */     //   1331: if_icmpgt +10 -> 1341
/*      */     //   1334: iload 11
/*      */     //   1336: bipush 100
/*      */     //   1338: iadd
/*      */     //   1339: istore 11
/*      */     //   1341: aload_0
/*      */     //   1342: getfield 41	com/mysql/jdbc/ResultSet:connection	Lcom/mysql/jdbc/Connection;
/*      */     //   1345: aload 7
/*      */     //   1347: aload_2
/*      */     //   1348: aload_0
/*      */     //   1349: aconst_null
/*      */     //   1350: iload 11
/*      */     //   1352: sipush 1900
/*      */     //   1355: iadd
/*      */     //   1356: iconst_1
/*      */     //   1357: iconst_1
/*      */     //   1358: iconst_0
/*      */     //   1359: iconst_0
/*      */     //   1360: iconst_0
/*      */     //   1361: iconst_0
/*      */     //   1362: invokespecial 475	com/mysql/jdbc/ResultSet:fastTimestampCreate	(Ljava/util/Calendar;IIIIIII)Ljava/sql/Timestamp;
/*      */     //   1365: aload_0
/*      */     //   1366: getfield 41	com/mysql/jdbc/ResultSet:connection	Lcom/mysql/jdbc/Connection;
/*      */     //   1369: invokevirtual 467	com/mysql/jdbc/Connection:getServerTimezoneTZ	()Ljava/util/TimeZone;
/*      */     //   1372: aload 4
/*      */     //   1374: iload 5
/*      */     //   1376: invokestatic 476	com/mysql/jdbc/TimeUtil:changeTimezone	(Lcom/mysql/jdbc/Connection;Ljava/util/Calendar;Ljava/util/Calendar;Ljava/sql/Timestamp;Ljava/util/TimeZone;Ljava/util/TimeZone;Z)Ljava/sql/Timestamp;
/*      */     //   1379: aload 8
/*      */     //   1381: monitorexit
/*      */     //   1382: areturn
/*      */     //   1383: new 189	java/sql/SQLException
/*      */     //   1386: dup
/*      */     //   1387: new 133	java/lang/StringBuffer
/*      */     //   1390: dup
/*      */     //   1391: invokespecial 134	java/lang/StringBuffer:<init>	()V
/*      */     //   1394: ldc_w 547
/*      */     //   1397: invokevirtual 136	java/lang/StringBuffer:append	(Ljava/lang/String;)Ljava/lang/StringBuffer;
/*      */     //   1400: new 124	java/lang/String
/*      */     //   1403: dup
/*      */     //   1404: aload_3
/*      */     //   1405: invokespecial 128	java/lang/String:<init>	([B)V
/*      */     //   1408: invokevirtual 136	java/lang/StringBuffer:append	(Ljava/lang/String;)Ljava/lang/StringBuffer;
/*      */     //   1411: ldc_w 548
/*      */     //   1414: invokevirtual 136	java/lang/StringBuffer:append	(Ljava/lang/String;)Ljava/lang/StringBuffer;
/*      */     //   1417: iload_1
/*      */     //   1418: invokevirtual 179	java/lang/StringBuffer:append	(I)Ljava/lang/StringBuffer;
/*      */     //   1421: ldc -34
/*      */     //   1423: invokevirtual 136	java/lang/StringBuffer:append	(Ljava/lang/String;)Ljava/lang/StringBuffer;
/*      */     //   1426: invokevirtual 139	java/lang/StringBuffer:toString	()Ljava/lang/String;
/*      */     //   1429: ldc 83
/*      */     //   1431: invokespecial 190	java/sql/SQLException:<init>	(Ljava/lang/String;Ljava/lang/String;)V
/*      */     //   1434: athrow
/*      */     //   1435: astore 19
/*      */     //   1437: aload 8
/*      */     //   1439: monitorexit
/*      */     //   1440: aload 19
/*      */     //   1442: athrow
/*      */     //   1443: astore 6
/*      */     //   1445: new 189	java/sql/SQLException
/*      */     //   1448: dup
/*      */     //   1449: new 133	java/lang/StringBuffer
/*      */     //   1452: dup
/*      */     //   1453: invokespecial 134	java/lang/StringBuffer:<init>	()V
/*      */     //   1456: ldc_w 549
/*      */     //   1459: invokevirtual 136	java/lang/StringBuffer:append	(Ljava/lang/String;)Ljava/lang/StringBuffer;
/*      */     //   1462: new 124	java/lang/String
/*      */     //   1465: dup
/*      */     //   1466: aload_3
/*      */     //   1467: invokespecial 128	java/lang/String:<init>	([B)V
/*      */     //   1470: invokevirtual 136	java/lang/StringBuffer:append	(Ljava/lang/String;)Ljava/lang/StringBuffer;
/*      */     //   1473: ldc_w 550
/*      */     //   1476: invokevirtual 136	java/lang/StringBuffer:append	(Ljava/lang/String;)Ljava/lang/StringBuffer;
/*      */     //   1479: iload_1
/*      */     //   1480: invokevirtual 179	java/lang/StringBuffer:append	(I)Ljava/lang/StringBuffer;
/*      */     //   1483: ldc_w 551
/*      */     //   1486: invokevirtual 136	java/lang/StringBuffer:append	(Ljava/lang/String;)Ljava/lang/StringBuffer;
/*      */     //   1489: invokevirtual 139	java/lang/StringBuffer:toString	()Ljava/lang/String;
/*      */     //   1492: ldc 83
/*      */     //   1494: invokespecial 190	java/sql/SQLException:<init>	(Ljava/lang/String;Ljava/lang/String;)V
/*      */     //   1497: athrow
/*      */     // Line number table:
/*      */     //   Java source line #6588	-> byte code offset #0
/*      */     //   Java source line #6591	-> byte code offset #5
/*      */     //   Java source line #6593	-> byte code offset #10
/*      */     //   Java source line #6594	-> byte code offset #14
/*      */     //   Java source line #6596	-> byte code offset #19
/*      */     //   Java source line #6599	-> byte code offset #21
/*      */     //   Java source line #6601	-> byte code offset #25
/*      */     //   Java source line #6605	-> byte code offset #51
/*      */     //   Java source line #6606	-> byte code offset #57
/*      */     //   Java source line #6608	-> byte code offset #60
/*      */     //   Java source line #6610	-> byte code offset #77
/*      */     //   Java source line #6611	-> byte code offset #87
/*      */     //   Java source line #6613	-> byte code offset #93
/*      */     //   Java source line #6614	-> byte code offset #114
/*      */     //   Java source line #6617	-> byte code offset #117
/*      */     //   Java source line #6619	-> byte code offset #159
/*      */     //   Java source line #6621	-> byte code offset #162
/*      */     //   Java source line #6610	-> byte code offset #165
/*      */     //   Java source line #6625	-> byte code offset #171
/*      */     //   Java source line #6627	-> byte code offset #181
/*      */     //   Java source line #6629	-> byte code offset #197
/*      */     //   Java source line #6631	-> byte code offset #202
/*      */     //   Java source line #6632	-> byte code offset #207
/*      */     //   Java source line #6634	-> byte code offset #223
/*      */     //   Java source line #6641	-> byte code offset #255
/*      */     //   Java source line #6643	-> byte code offset #271
/*      */     //   Java source line #6645	-> byte code offset #287
/*      */     //   Java source line #6654	-> byte code offset #330
/*      */     //   Java source line #6655	-> byte code offset #341
/*      */     //   Java source line #6659	-> byte code offset #344
/*      */     //   Java source line #6668	-> byte code offset #460
/*      */     //   Java source line #6669	-> byte code offset #468
/*      */     //   Java source line #6670	-> byte code offset #477
/*      */     //   Java source line #6671	-> byte code offset #487
/*      */     //   Java source line #6672	-> byte code offset #497
/*      */     //   Java source line #6673	-> byte code offset #507
/*      */     //   Java source line #6675	-> byte code offset #517
/*      */     //   Java source line #6677	-> byte code offset #520
/*      */     //   Java source line #6678	-> byte code offset #527
/*      */     //   Java source line #6680	-> byte code offset #535
/*      */     //   Java source line #6681	-> byte code offset #541
/*      */     //   Java source line #6682	-> byte code offset #550
/*      */     //   Java source line #6684	-> byte code offset #565
/*      */     //   Java source line #6694	-> byte code offset #573
/*      */     //   Java source line #6703	-> byte code offset #618
/*      */     //   Java source line #6704	-> byte code offset #626
/*      */     //   Java source line #6705	-> byte code offset #635
/*      */     //   Java source line #6706	-> byte code offset #645
/*      */     //   Java source line #6707	-> byte code offset #655
/*      */     //   Java source line #6708	-> byte code offset #665
/*      */     //   Java source line #6710	-> byte code offset #675
/*      */     //   Java source line #6719	-> byte code offset #719
/*      */     //   Java source line #6721	-> byte code offset #727
/*      */     //   Java source line #6722	-> byte code offset #734
/*      */     //   Java source line #6725	-> byte code offset #741
/*      */     //   Java source line #6726	-> byte code offset #749
/*      */     //   Java source line #6727	-> byte code offset #758
/*      */     //   Java source line #6728	-> byte code offset #768
/*      */     //   Java source line #6729	-> byte code offset #778
/*      */     //   Java source line #6731	-> byte code offset #788
/*      */     //   Java source line #6746	-> byte code offset #836
/*      */     //   Java source line #6748	-> byte code offset #862
/*      */     //   Java source line #6749	-> byte code offset #870
/*      */     //   Java source line #6750	-> byte code offset #879
/*      */     //   Java source line #6751	-> byte code offset #889
/*      */     //   Java source line #6752	-> byte code offset #892
/*      */     //   Java source line #6754	-> byte code offset #898
/*      */     //   Java source line #6756	-> byte code offset #906
/*      */     //   Java source line #6757	-> byte code offset #913
/*      */     //   Java source line #6760	-> byte code offset #920
/*      */     //   Java source line #6761	-> byte code offset #928
/*      */     //   Java source line #6762	-> byte code offset #937
/*      */     //   Java source line #6763	-> byte code offset #947
/*      */     //   Java source line #6765	-> byte code offset #957
/*      */     //   Java source line #6768	-> byte code offset #965
/*      */     //   Java source line #6777	-> byte code offset #1008
/*      */     //   Java source line #6778	-> byte code offset #1018
/*      */     //   Java source line #6779	-> byte code offset #1026
/*      */     //   Java source line #6780	-> byte code offset #1034
/*      */     //   Java source line #6782	-> byte code offset #1044
/*      */     //   Java source line #6793	-> byte code offset #1087
/*      */     //   Java source line #6794	-> byte code offset #1095
/*      */     //   Java source line #6795	-> byte code offset #1104
/*      */     //   Java source line #6797	-> byte code offset #1114
/*      */     //   Java source line #6806	-> byte code offset #1161
/*      */     //   Java source line #6808	-> byte code offset #1169
/*      */     //   Java source line #6809	-> byte code offset #1176
/*      */     //   Java source line #6812	-> byte code offset #1183
/*      */     //   Java source line #6813	-> byte code offset #1191
/*      */     //   Java source line #6815	-> byte code offset #1200
/*      */     //   Java source line #6824	-> byte code offset #1245
/*      */     //   Java source line #6826	-> byte code offset #1253
/*      */     //   Java source line #6827	-> byte code offset #1260
/*      */     //   Java source line #6830	-> byte code offset #1267
/*      */     //   Java source line #6832	-> byte code offset #1275
/*      */     //   Java source line #6841	-> byte code offset #1319
/*      */     //   Java source line #6843	-> byte code offset #1327
/*      */     //   Java source line #6844	-> byte code offset #1334
/*      */     //   Java source line #6847	-> byte code offset #1341
/*      */     //   Java source line #6856	-> byte code offset #1383
/*      */     //   Java source line #6862	-> byte code offset #1435
/*      */     //   Java source line #6863	-> byte code offset #1443
/*      */     //   Java source line #6864	-> byte code offset #1445
/*      */     // Local variable table:
/*      */     //   start	length	slot	name	signature
/*      */     //   0	1498	0	this	ResultSet
/*      */     //   0	1498	1	columnIndex	int
/*      */     //   0	1498	2	targetCalendar	Calendar
/*      */     //   0	1498	3	timestampAsBytes	byte[]
/*      */     //   0	1498	4	tz	TimeZone
/*      */     //   0	1498	5	rollForward	boolean
/*      */     //   23	533	6	length	int
/*      */     //   1443	3	6	e	Exception
/*      */     //   49	1297	7	sessionCalendar	Calendar
/*      */     //   58	119	9	allZeroTimestamp	boolean
/*      */     //   75	97	10	onlyTimePresent	boolean
/*      */     //   78	88	11	i	int
/*      */     //   466	118	11	year	int
/*      */     //   624	62	11	year	int
/*      */     //   725	74	11	year	int
/*      */     //   868	3	11	year	int
/*      */     //   904	72	11	year	int
/*      */     //   1024	36	11	hour	int
/*      */     //   1093	32	11	year	int
/*      */     //   1167	44	11	year	int
/*      */     //   1251	35	11	year	int
/*      */     //   1325	26	11	year	int
/*      */     //   91	62	12	b	byte
/*      */     //   475	111	12	month	int
/*      */     //   633	55	12	month	int
/*      */     //   747	58	12	month	int
/*      */     //   877	3	12	month	int
/*      */     //   926	52	12	month	int
/*      */     //   1032	30	12	minutes	int
/*      */     //   1102	29	12	month	int
/*      */     //   1189	28	12	month	int
/*      */     //   1273	19	12	month	int
/*      */     //   485	103	13	day	int
/*      */     //   643	47	13	day	int
/*      */     //   756	51	13	day	int
/*      */     //   887	3	13	day	int
/*      */     //   935	45	13	day	int
/*      */     //   1042	22	13	seconds	int
/*      */     //   1112	23	13	day	int
/*      */     //   1198	21	13	day	int
/*      */     //   495	95	14	hour	int
/*      */     //   653	39	14	hour	int
/*      */     //   766	43	14	hour	int
/*      */     //   890	3	14	hour	int
/*      */     //   945	37	14	hour	int
/*      */     //   505	87	15	minutes	int
/*      */     //   663	31	15	minutes	int
/*      */     //   776	35	15	minutes	int
/*      */     //   893	3	15	minutes	int
/*      */     //   955	29	15	minutes	int
/*      */     //   515	79	16	seconds	int
/*      */     //   673	23	16	seconds	int
/*      */     //   786	27	16	seconds	int
/*      */     //   518	78	17	nanos	int
/*      */     //   533	19	18	decimalIndex	int
/*      */     //   1435	6	19	localObject1	Object
/*      */     // Exception table:
/*      */     //   from	to	target	type
/*      */     //   57	206	1435	finally
/*      */     //   207	270	1435	finally
/*      */     //   271	329	1435	finally
/*      */     //   330	617	1435	finally
/*      */     //   618	718	1435	finally
/*      */     //   719	835	1435	finally
/*      */     //   836	1007	1435	finally
/*      */     //   1008	1086	1435	finally
/*      */     //   1087	1160	1435	finally
/*      */     //   1161	1244	1435	finally
/*      */     //   1245	1318	1435	finally
/*      */     //   1319	1382	1435	finally
/*      */     //   1383	1440	1435	finally
/*      */     //   5	20	1443	java/lang/Exception
/*      */     //   21	206	1443	java/lang/Exception
/*      */     //   207	270	1443	java/lang/Exception
/*      */     //   271	329	1443	java/lang/Exception
/*      */     //   330	617	1443	java/lang/Exception
/*      */     //   618	718	1443	java/lang/Exception
/*      */     //   719	835	1443	java/lang/Exception
/*      */     //   836	1007	1443	java/lang/Exception
/*      */     //   1008	1086	1443	java/lang/Exception
/*      */     //   1087	1160	1443	java/lang/Exception
/*      */     //   1161	1244	1443	java/lang/Exception
/*      */     //   1245	1318	1443	java/lang/Exception
/*      */     //   1319	1382	1443	java/lang/Exception
/*      */     //   1383	1443	1443	java/lang/Exception
/*      */   }
/*      */ }


/* Location:              E:\java\java学习\hutubill\lib\all.jar!\com\mysql\jdbc\ResultSet.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version:       0.7.1
 */
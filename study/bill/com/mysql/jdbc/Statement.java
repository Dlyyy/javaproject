/*      */ package com.mysql.jdbc;
/*      */ 
/*      */ import com.mysql.jdbc.exceptions.MySQLTimeoutException;
/*      */ import com.mysql.jdbc.profiler.ProfileEventSink;
/*      */ import com.mysql.jdbc.profiler.ProfilerEvent;
/*      */ import java.sql.BatchUpdateException;
/*      */ import java.sql.SQLException;
/*      */ import java.sql.SQLWarning;
/*      */ import java.util.ArrayList;
/*      */ import java.util.Calendar;
/*      */ import java.util.GregorianCalendar;
/*      */ import java.util.Iterator;
/*      */ import java.util.List;
/*      */ import java.util.Timer;
/*      */ import java.util.TimerTask;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ public class Statement
/*      */   implements java.sql.Statement
/*      */ {
/*      */   protected static final String PING_MARKER = "/* ping */";
/*      */   
/*      */   class CancelTask
/*      */     extends TimerTask
/*      */   {
/*   75 */     long connectionId = 0L;
/*   76 */     SQLException caughtWhileCancelling = null;
/*      */     
/*      */     CancelTask() throws SQLException {
/*   79 */       this.connectionId = Statement.this.connection.getIO().getThreadId();
/*      */     }
/*      */     
/*      */     public void run()
/*      */     {
/*   84 */       Thread cancelThread = new Statement.1(this);
/*      */       
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  126 */       cancelThread.start();
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*  133 */   protected Object cancelTimeoutMutex = new Object();
/*      */   
/*      */ 
/*  136 */   protected static int statementCounter = 1;
/*      */   
/*      */   public static final byte USES_VARIABLES_FALSE = 0;
/*      */   
/*      */   public static final byte USES_VARIABLES_TRUE = 1;
/*      */   
/*      */   public static final byte USES_VARIABLES_UNKNOWN = -1;
/*      */   
/*  144 */   protected boolean wasCancelled = false;
/*      */   
/*      */ 
/*      */   protected List batchedArgs;
/*      */   
/*      */ 
/*  150 */   protected SingleByteCharsetConverter charConverter = null;
/*      */   
/*      */ 
/*  153 */   protected String charEncoding = null;
/*      */   
/*      */ 
/*  156 */   protected Connection connection = null;
/*      */   
/*  158 */   protected long connectionId = 0L;
/*      */   
/*      */ 
/*  161 */   protected String currentCatalog = null;
/*      */   
/*      */ 
/*  164 */   protected boolean doEscapeProcessing = true;
/*      */   
/*      */ 
/*  167 */   protected ProfileEventSink eventSink = null;
/*      */   
/*      */ 
/*  170 */   private int fetchSize = 0;
/*      */   
/*      */ 
/*  173 */   protected boolean isClosed = false;
/*      */   
/*      */ 
/*  176 */   protected long lastInsertId = -1L;
/*      */   
/*      */ 
/*  179 */   protected int maxFieldSize = MysqlIO.getMaxBuf();
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  185 */   protected int maxRows = -1;
/*      */   
/*      */ 
/*  188 */   protected boolean maxRowsChanged = false;
/*      */   
/*      */ 
/*  191 */   protected List openResults = new ArrayList();
/*      */   
/*      */ 
/*  194 */   protected boolean pedantic = false;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   protected Throwable pointOfOrigin;
/*      */   
/*      */ 
/*      */ 
/*  203 */   protected boolean profileSQL = false;
/*      */   
/*      */ 
/*  206 */   protected ResultSet results = null;
/*      */   
/*      */ 
/*  209 */   protected int resultSetConcurrency = 0;
/*      */   
/*      */ 
/*  212 */   protected int resultSetType = 0;
/*      */   
/*      */ 
/*      */   protected int statementId;
/*      */   
/*      */ 
/*  218 */   protected int timeoutInMillis = 0;
/*      */   
/*      */ 
/*  221 */   protected long updateCount = -1L;
/*      */   
/*      */ 
/*  224 */   protected boolean useUsageAdvisor = false;
/*      */   
/*      */ 
/*  227 */   protected SQLWarning warningChain = null;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  233 */   protected boolean holdResultsOpenOverClose = false;
/*      */   
/*  235 */   protected ArrayList batchedGeneratedKeys = null;
/*      */   
/*  237 */   protected boolean retrieveGeneratedKeys = false;
/*      */   
/*  239 */   protected boolean continueBatchOnError = false;
/*      */   
/*  241 */   protected PingTarget pingTarget = null;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public Statement(Connection c, String catalog)
/*      */     throws SQLException
/*      */   {
/*  256 */     if ((c == null) || (c.isClosed())) {
/*  257 */       throw SQLError.createSQLException(Messages.getString("Statement.0"), "08003");
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*  262 */     this.connection = c;
/*  263 */     this.connectionId = this.connection.getId();
/*      */     
/*  265 */     this.currentCatalog = catalog;
/*  266 */     this.pedantic = this.connection.getPedantic();
/*  267 */     this.continueBatchOnError = this.connection.getContinueBatchOnError();
/*      */     
/*  269 */     if (!this.connection.getDontTrackOpenResources()) {
/*  270 */       this.connection.registerStatement(this);
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  277 */     if (this.connection != null) {
/*  278 */       this.maxFieldSize = this.connection.getMaxAllowedPacket();
/*      */       
/*  280 */       int defaultFetchSize = this.connection.getDefaultFetchSize();
/*      */       
/*  282 */       if (defaultFetchSize != 0) {
/*  283 */         setFetchSize(defaultFetchSize);
/*      */       }
/*      */     }
/*      */     
/*  287 */     if (this.connection.getUseUnicode()) {
/*  288 */       this.charEncoding = this.connection.getEncoding();
/*      */       
/*  290 */       this.charConverter = this.connection.getCharsetConverter(this.charEncoding);
/*      */     }
/*      */     
/*      */ 
/*  294 */     boolean profiling = (this.connection.getProfileSql()) || (this.connection.getUseUsageAdvisor());
/*      */     
/*      */ 
/*  297 */     if ((this.connection.getAutoGenerateTestcaseScript()) || (profiling)) {
/*  298 */       this.statementId = (statementCounter++);
/*      */     }
/*      */     
/*  301 */     if (profiling) {
/*  302 */       this.pointOfOrigin = new Throwable();
/*  303 */       this.profileSQL = this.connection.getProfileSql();
/*  304 */       this.useUsageAdvisor = this.connection.getUseUsageAdvisor();
/*  305 */       this.eventSink = ProfileEventSink.getInstance(this.connection);
/*      */     }
/*      */     
/*  308 */     int maxRowsConn = this.connection.getMaxRows();
/*      */     
/*  310 */     if (maxRowsConn != -1) {
/*  311 */       setMaxRows(maxRowsConn);
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
/*      */   public synchronized void addBatch(String sql)
/*      */     throws SQLException
/*      */   {
/*  325 */     if (this.batchedArgs == null) {
/*  326 */       this.batchedArgs = new ArrayList();
/*      */     }
/*      */     
/*  329 */     if (sql != null) {
/*  330 */       this.batchedArgs.add(sql);
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public void cancel()
/*      */     throws SQLException
/*      */   {
/*  340 */     if ((!this.isClosed) && (this.connection != null) && (this.connection.versionMeetsMinimum(5, 0, 0)))
/*      */     {
/*      */ 
/*  343 */       Connection cancelConn = null;
/*  344 */       java.sql.Statement cancelStmt = null;
/*      */       try
/*      */       {
/*  347 */         synchronized (this.cancelTimeoutMutex) {
/*  348 */           cancelConn = this.connection.duplicate();
/*  349 */           cancelStmt = cancelConn.createStatement();
/*  350 */           cancelStmt.execute("KILL QUERY " + this.connection.getIO().getThreadId());
/*      */           
/*  352 */           this.wasCancelled = true;
/*      */         }
/*      */         
/*      */ 
/*      */       }
/*      */       catch (NullPointerException npe)
/*      */       {
/*  359 */         throw SQLError.createSQLException(Messages.getString("Statement.49"), "08003");
/*      */       }
/*      */       finally
/*      */       {
/*  363 */         if (cancelStmt != null) {
/*  364 */           cancelStmt.close();
/*      */         }
/*      */         
/*  367 */         if (cancelConn != null) {
/*  368 */           cancelConn.close();
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
/*      */   protected void checkClosed()
/*      */     throws SQLException
/*      */   {
/*  384 */     if (this.isClosed) {
/*  385 */       throw SQLError.createSQLException(Messages.getString("Statement.49"), "08003");
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
/*      */   protected void checkForDml(String sql, char firstStatementChar)
/*      */     throws SQLException
/*      */   {
/*  405 */     if ((firstStatementChar == 'I') || (firstStatementChar == 'U') || (firstStatementChar == 'D') || (firstStatementChar == 'A') || (firstStatementChar == 'C'))
/*      */     {
/*      */ 
/*  408 */       String noCommentSql = StringUtils.stripComments(sql, "'\"", "'\"", true, false, true, true);
/*      */       
/*      */ 
/*  411 */       if ((StringUtils.startsWithIgnoreCaseAndWs(noCommentSql, "INSERT")) || (StringUtils.startsWithIgnoreCaseAndWs(noCommentSql, "UPDATE")) || (StringUtils.startsWithIgnoreCaseAndWs(noCommentSql, "DELETE")) || (StringUtils.startsWithIgnoreCaseAndWs(noCommentSql, "DROP")) || (StringUtils.startsWithIgnoreCaseAndWs(noCommentSql, "CREATE")) || (StringUtils.startsWithIgnoreCaseAndWs(noCommentSql, "ALTER")))
/*      */       {
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  417 */         throw SQLError.createSQLException(Messages.getString("Statement.57"), "S1009");
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
/*      */   protected void checkNullOrEmptyQuery(String sql)
/*      */     throws SQLException
/*      */   {
/*  434 */     if (sql == null) {
/*  435 */       throw SQLError.createSQLException(Messages.getString("Statement.59"), "S1009");
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*  440 */     if (sql.length() == 0) {
/*  441 */       throw SQLError.createSQLException(Messages.getString("Statement.61"), "S1009");
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
/*      */   public synchronized void clearBatch()
/*      */     throws SQLException
/*      */   {
/*  456 */     if (this.batchedArgs != null) {
/*  457 */       this.batchedArgs.clear();
/*      */     }
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
/*  469 */     this.warningChain = null;
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
/*      */   public void close()
/*      */     throws SQLException
/*      */   {
/*  488 */     realClose(true, true);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   protected void closeAllOpenResults()
/*      */   {
/*  495 */     if (this.openResults != null) {
/*  496 */       for (Iterator iter = this.openResults.iterator(); iter.hasNext();) {
/*  497 */         ResultSet element = (ResultSet)iter.next();
/*      */         try
/*      */         {
/*  500 */           element.realClose(false);
/*      */         } catch (SQLException sqlEx) {
/*  502 */           AssertionFailedException.shouldNotHappen(sqlEx);
/*      */         }
/*      */       }
/*      */       
/*  506 */       this.openResults.clear();
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   private ResultSet createResultSetUsingServerFetch(String sql)
/*      */     throws SQLException
/*      */   {
/*  516 */     java.sql.PreparedStatement pStmt = this.connection.prepareStatement(sql, this.resultSetType, this.resultSetConcurrency);
/*      */     
/*      */ 
/*  519 */     pStmt.setFetchSize(this.fetchSize);
/*      */     
/*  521 */     if (this.maxRows > -1) {
/*  522 */       pStmt.setMaxRows(this.maxRows);
/*      */     }
/*      */     
/*  525 */     pStmt.execute();
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  531 */     ResultSet rs = ((Statement)pStmt).getResultSetInternal();
/*      */     
/*      */ 
/*  534 */     rs.setStatementUsedForFetchingRows((PreparedStatement)pStmt);
/*      */     
/*      */ 
/*  537 */     this.results = rs;
/*      */     
/*  539 */     return rs;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected boolean createStreamingResultSet()
/*      */   {
/*  550 */     return (this.resultSetType == 1003) && (this.resultSetConcurrency == 1007) && (this.fetchSize == Integer.MIN_VALUE);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void enableStreamingResults()
/*      */     throws SQLException
/*      */   {
/*  561 */     setFetchSize(Integer.MIN_VALUE);
/*  562 */     setResultSetType(1003);
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
/*      */   public boolean execute(String sql)
/*      */     throws SQLException
/*      */   {
/*  580 */     checkClosed();
/*      */     
/*  582 */     Connection locallyScopedConn = this.connection;
/*      */     
/*  584 */     synchronized (locallyScopedConn.getMutex()) {
/*  585 */       synchronized (this.cancelTimeoutMutex) {
/*  586 */         this.wasCancelled = false;
/*      */       }
/*      */       
/*  589 */       checkNullOrEmptyQuery(sql);
/*      */       
/*  591 */       checkClosed();
/*      */       
/*  593 */       char firstNonWsChar = StringUtils.firstNonWsCharUc(sql);
/*      */       
/*  595 */       boolean isSelect = true;
/*      */       
/*  597 */       if (firstNonWsChar != 'S') {
/*  598 */         isSelect = false;
/*      */         
/*  600 */         if (locallyScopedConn.isReadOnly()) {
/*  601 */           throw SQLError.createSQLException(Messages.getString("Statement.27") + Messages.getString("Statement.28"), "S1009");
/*      */         }
/*      */       }
/*      */       
/*      */ 
/*      */ 
/*      */ 
/*  608 */       if (this.doEscapeProcessing) {
/*  609 */         Object escapedSqlResult = EscapeProcessor.escapeSQL(sql, locallyScopedConn.serverSupportsConvertFn(), locallyScopedConn);
/*      */         
/*      */ 
/*  612 */         if ((escapedSqlResult instanceof String)) {
/*  613 */           sql = (String)escapedSqlResult;
/*      */         } else {
/*  615 */           sql = ((EscapeProcessorResult)escapedSqlResult).escapedSql;
/*      */         }
/*      */       }
/*      */       
/*  619 */       if ((this.results != null) && 
/*  620 */         (!locallyScopedConn.getHoldResultsOpenOverStatementClose())) {
/*  621 */         this.results.realClose(false);
/*      */       }
/*      */       
/*      */ 
/*  625 */       if ((firstNonWsChar == '/') && 
/*  626 */         (sql.startsWith("/* ping */"))) {
/*  627 */         doPingInstead();
/*      */         
/*  629 */         return true;
/*      */       }
/*      */       
/*      */ 
/*  633 */       CachedResultSetMetaData cachedMetaData = null;
/*      */       
/*  635 */       ResultSet rs = null;
/*      */       
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  644 */       this.batchedGeneratedKeys = null;
/*      */       
/*  646 */       if (useServerFetch()) {
/*  647 */         rs = createResultSetUsingServerFetch(sql);
/*      */       } else {
/*  649 */         CancelTask timeoutTask = null;
/*      */         
/*  651 */         String oldCatalog = null;
/*      */         try
/*      */         {
/*  654 */           if ((locallyScopedConn.getEnableQueryTimeouts()) && (this.timeoutInMillis != 0) && (locallyScopedConn.versionMeetsMinimum(5, 0, 0)))
/*      */           {
/*      */ 
/*  657 */             timeoutTask = new CancelTask();
/*  658 */             Connection.getCancelTimer().schedule(timeoutTask, this.timeoutInMillis);
/*      */           }
/*      */           
/*      */ 
/*      */ 
/*      */ 
/*  664 */           if (!locallyScopedConn.getCatalog().equals(this.currentCatalog))
/*      */           {
/*  666 */             oldCatalog = locallyScopedConn.getCatalog();
/*  667 */             locallyScopedConn.setCatalog(this.currentCatalog);
/*      */           }
/*      */           
/*      */ 
/*      */ 
/*      */ 
/*  673 */           if (locallyScopedConn.getCacheResultSetMetadata()) {
/*  674 */             cachedMetaData = locallyScopedConn.getCachedMetaData(sql);
/*      */           }
/*      */           
/*      */ 
/*      */ 
/*      */ 
/*  680 */           if (locallyScopedConn.useMaxRows()) {
/*  681 */             int rowLimit = -1;
/*      */             
/*  683 */             if (isSelect) {
/*  684 */               if (StringUtils.indexOfIgnoreCase(sql, "LIMIT") != -1) {
/*  685 */                 rowLimit = this.maxRows;
/*      */               }
/*  687 */               else if (this.maxRows <= 0) {
/*  688 */                 locallyScopedConn.execSQL(this, "SET OPTION SQL_SELECT_LIMIT=DEFAULT", -1, null, 1003, 1007, false, this.currentCatalog, true);
/*      */ 
/*      */ 
/*      */ 
/*      */               }
/*      */               else
/*      */               {
/*      */ 
/*      */ 
/*      */ 
/*  698 */                 locallyScopedConn.execSQL(this, "SET OPTION SQL_SELECT_LIMIT=" + this.maxRows, -1, null, 1003, 1007, false, this.currentCatalog, true);
/*      */ 
/*      */ 
/*      */               }
/*      */               
/*      */ 
/*      */ 
/*      */ 
/*      */             }
/*      */             else
/*      */             {
/*      */ 
/*      */ 
/*  711 */               locallyScopedConn.execSQL(this, "SET OPTION SQL_SELECT_LIMIT=DEFAULT", -1, null, 1003, 1007, false, this.currentCatalog, true);
/*      */             }
/*      */             
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  722 */             rs = locallyScopedConn.execSQL(this, sql, rowLimit, null, this.resultSetType, this.resultSetConcurrency, createStreamingResultSet(), this.currentCatalog, cachedMetaData == null);
/*      */ 
/*      */           }
/*      */           else
/*      */           {
/*  727 */             rs = locallyScopedConn.execSQL(this, sql, -1, null, this.resultSetType, this.resultSetConcurrency, createStreamingResultSet(), this.currentCatalog, cachedMetaData == null);
/*      */           }
/*      */           
/*      */ 
/*      */ 
/*      */ 
/*  733 */           if (timeoutTask != null) {
/*  734 */             if (timeoutTask.caughtWhileCancelling != null) {
/*  735 */               throw timeoutTask.caughtWhileCancelling;
/*      */             }
/*      */             
/*  738 */             timeoutTask.cancel();
/*  739 */             timeoutTask = null;
/*      */           }
/*      */           
/*  742 */           synchronized (this.cancelTimeoutMutex) {
/*  743 */             if (this.wasCancelled) {
/*  744 */               this.wasCancelled = false;
/*  745 */               throw new MySQLTimeoutException();
/*      */             }
/*      */           }
/*      */         } finally {
/*  749 */           if (timeoutTask != null) {
/*  750 */             timeoutTask.cancel();
/*      */           }
/*      */           
/*  753 */           if (oldCatalog != null) {
/*  754 */             locallyScopedConn.setCatalog(oldCatalog);
/*      */           }
/*      */         }
/*      */       }
/*      */       
/*  759 */       this.lastInsertId = rs.getUpdateID();
/*      */       
/*  761 */       if (rs != null) {
/*  762 */         this.results = rs;
/*      */         
/*  764 */         rs.setFirstCharOfQuery(firstNonWsChar);
/*      */         
/*  766 */         if (rs.reallyResult()) {
/*  767 */           if (cachedMetaData != null) {
/*  768 */             locallyScopedConn.initializeResultsMetadataFromCache(sql, cachedMetaData, this.results);
/*      */ 
/*      */           }
/*  771 */           else if (this.connection.getCacheResultSetMetadata()) {
/*  772 */             locallyScopedConn.initializeResultsMetadataFromCache(sql, null, this.results);
/*      */           }
/*      */         }
/*      */       }
/*      */       
/*      */ 
/*      */ 
/*  779 */       return (rs != null) && (rs.reallyResult());
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public boolean execute(String sql, int returnGeneratedKeys)
/*      */     throws SQLException
/*      */   {
/*  790 */     if (returnGeneratedKeys == 1) {
/*  791 */       checkClosed();
/*      */       
/*  793 */       Connection locallyScopedConn = this.connection;
/*      */       
/*  795 */       synchronized (locallyScopedConn.getMutex())
/*      */       {
/*      */ 
/*      */ 
/*  799 */         boolean readInfoMsgState = this.connection.isReadInfoMsgEnabled();
/*      */         
/*  801 */         locallyScopedConn.setReadInfoMsgEnabled(true);
/*      */         try
/*      */         {
/*  804 */           boolean bool1 = execute(sql);
/*      */           
/*  806 */           locallyScopedConn.setReadInfoMsgEnabled(readInfoMsgState);return bool1; } finally { locallyScopedConn.setReadInfoMsgEnabled(readInfoMsgState);
/*      */         }
/*      */       }
/*      */     }
/*      */     
/*  811 */     return execute(sql);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public boolean execute(String sql, int[] generatedKeyIndices)
/*      */     throws SQLException
/*      */   {
/*  819 */     if ((generatedKeyIndices != null) && (generatedKeyIndices.length > 0)) {
/*  820 */       checkClosed();
/*      */       
/*  822 */       Connection locallyScopedConn = this.connection;
/*      */       
/*  824 */       synchronized (locallyScopedConn.getMutex())
/*      */       {
/*      */ 
/*      */ 
/*  828 */         boolean readInfoMsgState = locallyScopedConn.isReadInfoMsgEnabled();
/*      */         
/*  830 */         locallyScopedConn.setReadInfoMsgEnabled(true);
/*      */         try
/*      */         {
/*  833 */           boolean bool1 = execute(sql);
/*      */           
/*  835 */           locallyScopedConn.setReadInfoMsgEnabled(readInfoMsgState);return bool1; } finally { locallyScopedConn.setReadInfoMsgEnabled(readInfoMsgState);
/*      */         }
/*      */       }
/*      */     }
/*      */     
/*  840 */     return execute(sql);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public boolean execute(String sql, String[] generatedKeyNames)
/*      */     throws SQLException
/*      */   {
/*  848 */     if ((generatedKeyNames != null) && (generatedKeyNames.length > 0)) {
/*  849 */       checkClosed();
/*      */       
/*  851 */       Connection locallyScopedConn = this.connection;
/*      */       
/*  853 */       synchronized (locallyScopedConn.getMutex())
/*      */       {
/*      */ 
/*      */ 
/*  857 */         boolean readInfoMsgState = this.connection.isReadInfoMsgEnabled();
/*      */         
/*  859 */         locallyScopedConn.setReadInfoMsgEnabled(true);
/*      */         try
/*      */         {
/*  862 */           boolean bool1 = execute(sql);
/*      */           
/*  864 */           locallyScopedConn.setReadInfoMsgEnabled(readInfoMsgState);return bool1; } finally { locallyScopedConn.setReadInfoMsgEnabled(readInfoMsgState);
/*      */         }
/*      */       }
/*      */     }
/*      */     
/*  869 */     return execute(sql);
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
/*      */   public synchronized int[] executeBatch()
/*      */     throws SQLException
/*      */   {
/*  887 */     checkClosed();
/*      */     
/*  889 */     Connection locallyScopedConn = this.connection;
/*      */     
/*  891 */     if (locallyScopedConn.isReadOnly()) {
/*  892 */       throw SQLError.createSQLException(Messages.getString("Statement.34") + Messages.getString("Statement.35"), "S1009");
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*  898 */     if ((this.results != null) && 
/*  899 */       (!locallyScopedConn.getHoldResultsOpenOverStatementClose())) {
/*  900 */       this.results.realClose(false);
/*      */     }
/*      */     
/*      */ 
/*  904 */     synchronized (locallyScopedConn.getMutex()) {
/*  905 */       if ((this.batchedArgs == null) || (this.batchedArgs.size() == 0)) {
/*  906 */         return new int[0];
/*      */       }
/*      */       try
/*      */       {
/*  910 */         this.retrieveGeneratedKeys = true;
/*      */         
/*  912 */         int[] updateCounts = null;
/*      */         
/*  914 */         if (this.batchedArgs != null) {
/*  915 */           nbrCommands = this.batchedArgs.size();
/*      */           
/*  917 */           this.batchedGeneratedKeys = new ArrayList(this.batchedArgs.size());
/*      */           
/*  919 */           boolean multiQueriesEnabled = locallyScopedConn.getAllowMultiQueries();
/*      */           
/*  921 */           if ((locallyScopedConn.versionMeetsMinimum(4, 1, 1)) && ((multiQueriesEnabled) || ((locallyScopedConn.getRewriteBatchedStatements()) && (nbrCommands > 4))))
/*      */           {
/*      */ 
/*      */ 
/*  925 */             int[] arrayOfInt1 = executeBatchUsingMultiQueries(multiQueriesEnabled, nbrCommands);
/*      */             
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  969 */             this.retrieveGeneratedKeys = false;
/*      */             
/*  971 */             clearBatch();return arrayOfInt1;
/*      */           }
/*  928 */           updateCounts = new int[nbrCommands];
/*      */           
/*  930 */           for (int i = 0; i < nbrCommands; i++) {
/*  931 */             updateCounts[i] = -3;
/*      */           }
/*      */           
/*  934 */           SQLException sqlEx = null;
/*      */           
/*  936 */           int commandIndex = 0;
/*      */           
/*  938 */           for (commandIndex = 0; commandIndex < nbrCommands; commandIndex++) {
/*      */             try {
/*  940 */               updateCounts[commandIndex] = executeUpdate((String)this.batchedArgs.get(commandIndex), true);
/*      */               
/*  942 */               getBatchedGeneratedKeys();
/*      */             } catch (SQLException ex) {
/*  944 */               updateCounts[commandIndex] = -3;
/*      */               
/*  946 */               if (this.continueBatchOnError) {
/*  947 */                 sqlEx = ex;
/*      */               } else {
/*  949 */                 int[] newUpdateCounts = new int[commandIndex];
/*  950 */                 System.arraycopy(updateCounts, 0, newUpdateCounts, 0, commandIndex);
/*      */                 
/*      */ 
/*  953 */                 throw new BatchUpdateException(ex.getMessage(), ex.getSQLState(), ex.getErrorCode(), newUpdateCounts);
/*      */               }
/*      */             }
/*      */           }
/*      */           
/*      */ 
/*      */ 
/*  960 */           if (sqlEx != null) {
/*  961 */             throw new BatchUpdateException(sqlEx.getMessage(), sqlEx.getSQLState(), sqlEx.getErrorCode(), updateCounts);
/*      */           }
/*      */         }
/*      */         
/*      */ 
/*      */ 
/*  967 */         int nbrCommands = updateCounts != null ? updateCounts : new int[0];
/*      */         
/*  969 */         this.retrieveGeneratedKeys = false;
/*      */         
/*  971 */         clearBatch();return nbrCommands;
/*      */       }
/*      */       finally
/*      */       {
/*  969 */         this.retrieveGeneratedKeys = false;
/*      */         
/*  971 */         clearBatch();
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
/*      */   private int[] executeBatchUsingMultiQueries(boolean multiQueriesEnabled, int nbrCommands)
/*      */     throws SQLException
/*      */   {
/*  987 */     Connection locallyScopedConn = this.connection;
/*      */     
/*  989 */     if (!multiQueriesEnabled) {
/*  990 */       locallyScopedConn.getIO().enableMultiQueries();
/*      */     }
/*      */     
/*  993 */     java.sql.Statement batchStmt = null;
/*      */     try
/*      */     {
/*  996 */       int[] updateCounts = new int[nbrCommands];
/*      */       
/*  998 */       for (int i = 0; i < nbrCommands; i++) {
/*  999 */         updateCounts[i] = -3;
/*      */       }
/*      */       
/* 1002 */       int commandIndex = 0;
/*      */       
/* 1004 */       StringBuffer queryBuf = new StringBuffer();
/*      */       
/*      */ 
/*      */ 
/* 1008 */       batchStmt = locallyScopedConn.createStatement();
/*      */       
/*      */ 
/* 1011 */       int counter = 0;
/*      */       
/* 1013 */       int numberOfBytesPerChar = 1;
/*      */       
/* 1015 */       String connectionEncoding = locallyScopedConn.getEncoding();
/*      */       
/* 1017 */       if (StringUtils.startsWithIgnoreCase(connectionEncoding, "utf")) {
/* 1018 */         numberOfBytesPerChar = 3;
/* 1019 */       } else if (CharsetMapping.isMultibyteCharset(connectionEncoding)) {
/* 1020 */         numberOfBytesPerChar = 2;
/*      */       }
/*      */       
/* 1023 */       int escapeAdjust = 1;
/*      */       
/* 1025 */       if (this.doEscapeProcessing) {
/* 1026 */         escapeAdjust = 2;
/*      */       }
/*      */       
/*      */ 
/*      */ 
/* 1031 */       for (commandIndex = 0; commandIndex < nbrCommands; commandIndex++) {
/* 1032 */         String nextQuery = (String)this.batchedArgs.get(commandIndex);
/*      */         
/* 1034 */         if (((queryBuf.length() + nextQuery.length()) * numberOfBytesPerChar + 1 + 4) * escapeAdjust + 32 > this.connection.getMaxAllowedPacket())
/*      */         {
/*      */ 
/*      */ 
/* 1038 */           batchStmt.execute(queryBuf.toString());
/*      */           
/* 1040 */           updateCounts[(counter++)] = batchStmt.getUpdateCount();
/* 1041 */           long generatedKeyStart = ((Statement)batchStmt).getLastInsertID();
/* 1042 */           byte[][] row = new byte[1][];
/* 1043 */           row[0] = Long.toString(generatedKeyStart++).getBytes();
/* 1044 */           this.batchedGeneratedKeys.add(row);
/*      */           
/*      */ 
/* 1047 */           while ((batchStmt.getMoreResults()) || (batchStmt.getUpdateCount() != -1)) {
/* 1048 */             updateCounts[(counter++)] = batchStmt.getUpdateCount();
/* 1049 */             row = new byte[1][];
/* 1050 */             row[0] = Long.toString(generatedKeyStart++).getBytes();
/* 1051 */             this.batchedGeneratedKeys.add(row);
/*      */           }
/*      */           
/* 1054 */           queryBuf = new StringBuffer();
/*      */         }
/*      */         
/* 1057 */         queryBuf.append(nextQuery);
/* 1058 */         queryBuf.append(";");
/*      */       }
/*      */       long generatedKeyStart;
/* 1061 */       if (queryBuf.length() > 0) {
/* 1062 */         batchStmt.execute(queryBuf.toString());
/*      */         
/* 1064 */         generatedKeyStart = ((Statement)batchStmt).getLastInsertID();
/* 1065 */         byte[][] row = new byte[1][];
/* 1066 */         row[0] = Long.toString(generatedKeyStart++).getBytes();
/* 1067 */         this.batchedGeneratedKeys.add(row);
/*      */         
/* 1069 */         updateCounts[(counter++)] = batchStmt.getUpdateCount();
/*      */         
/*      */ 
/* 1072 */         while ((batchStmt.getMoreResults()) || (batchStmt.getUpdateCount() != -1)) {
/* 1073 */           updateCounts[(counter++)] = batchStmt.getUpdateCount();
/* 1074 */           row = new byte[1][];
/* 1075 */           row[0] = Long.toString(generatedKeyStart++).getBytes();
/* 1076 */           this.batchedGeneratedKeys.add(row);
/*      */         }
/*      */       }
/*      */       
/* 1080 */       return updateCounts != null ? updateCounts : new int[0];
/*      */     } finally {
/*      */       try {
/* 1083 */         if (batchStmt != null) {
/* 1084 */           batchStmt.close();
/*      */         }
/*      */       } finally {
/* 1087 */         if (!multiQueriesEnabled) {
/* 1088 */           locallyScopedConn.getIO().disableMultiQueries();
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
/*      */   public java.sql.ResultSet executeQuery(String sql)
/*      */     throws SQLException
/*      */   {
/* 1107 */     checkClosed();
/*      */     
/* 1109 */     Connection locallyScopedConn = this.connection;
/*      */     
/* 1111 */     synchronized (locallyScopedConn.getMutex()) {
/* 1112 */       synchronized (this.cancelTimeoutMutex) {
/* 1113 */         this.wasCancelled = false;
/*      */       }
/*      */       
/* 1116 */       checkNullOrEmptyQuery(sql);
/*      */       
/* 1118 */       if (this.doEscapeProcessing) {
/* 1119 */         Object escapedSqlResult = EscapeProcessor.escapeSQL(sql, locallyScopedConn.serverSupportsConvertFn(), this.connection);
/*      */         
/*      */ 
/* 1122 */         if ((escapedSqlResult instanceof String)) {
/* 1123 */           sql = (String)escapedSqlResult;
/*      */         } else {
/* 1125 */           sql = ((EscapeProcessorResult)escapedSqlResult).escapedSql;
/*      */         }
/*      */       }
/*      */       
/* 1129 */       char firstStatementChar = StringUtils.firstNonWsCharUc(sql, findStartOfStatement(sql));
/*      */       
/*      */ 
/* 1132 */       if ((sql.charAt(0) == '/') && 
/* 1133 */         (sql.startsWith("/* ping */"))) {
/* 1134 */         doPingInstead();
/*      */         
/* 1136 */         return this.results;
/*      */       }
/*      */       
/*      */ 
/* 1140 */       checkForDml(sql, firstStatementChar);
/*      */       
/* 1142 */       if ((this.results != null) && 
/* 1143 */         (!locallyScopedConn.getHoldResultsOpenOverStatementClose())) {
/* 1144 */         this.results.realClose(false);
/*      */       }
/*      */       
/*      */ 
/* 1148 */       CachedResultSetMetaData cachedMetaData = null;
/*      */       
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 1157 */       if (useServerFetch()) {
/* 1158 */         this.results = createResultSetUsingServerFetch(sql);
/*      */         
/* 1160 */         return this.results;
/*      */       }
/*      */       
/* 1163 */       CancelTask timeoutTask = null;
/*      */       
/* 1165 */       String oldCatalog = null;
/*      */       try
/*      */       {
/* 1168 */         if ((locallyScopedConn.getEnableQueryTimeouts()) && (this.timeoutInMillis != 0) && (locallyScopedConn.versionMeetsMinimum(5, 0, 0)))
/*      */         {
/*      */ 
/* 1171 */           timeoutTask = new CancelTask();
/* 1172 */           Connection.getCancelTimer().schedule(timeoutTask, this.timeoutInMillis);
/*      */         }
/*      */         
/*      */ 
/* 1176 */         if (!locallyScopedConn.getCatalog().equals(this.currentCatalog)) {
/* 1177 */           oldCatalog = locallyScopedConn.getCatalog();
/* 1178 */           locallyScopedConn.setCatalog(this.currentCatalog);
/*      */         }
/*      */         
/*      */ 
/*      */ 
/*      */ 
/* 1184 */         if (locallyScopedConn.getCacheResultSetMetadata()) {
/* 1185 */           cachedMetaData = locallyScopedConn.getCachedMetaData(sql);
/*      */         }
/*      */         
/* 1188 */         if (locallyScopedConn.useMaxRows())
/*      */         {
/*      */ 
/*      */ 
/*      */ 
/* 1193 */           if (StringUtils.indexOfIgnoreCase(sql, "LIMIT") != -1) {
/* 1194 */             this.results = locallyScopedConn.execSQL(this, sql, this.maxRows, null, this.resultSetType, this.resultSetConcurrency, createStreamingResultSet(), this.currentCatalog, cachedMetaData == null);
/*      */ 
/*      */           }
/*      */           else
/*      */           {
/*      */ 
/* 1200 */             if (this.maxRows <= 0) {
/* 1201 */               locallyScopedConn.execSQL(this, "SET OPTION SQL_SELECT_LIMIT=DEFAULT", -1, null, 1003, 1007, false, this.currentCatalog, true);
/*      */ 
/*      */ 
/*      */ 
/*      */             }
/*      */             else
/*      */             {
/*      */ 
/*      */ 
/* 1210 */               locallyScopedConn.execSQL(this, "SET OPTION SQL_SELECT_LIMIT=" + this.maxRows, -1, null, 1003, 1007, false, this.currentCatalog, true);
/*      */             }
/*      */             
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 1221 */             this.results = locallyScopedConn.execSQL(this, sql, -1, null, this.resultSetType, this.resultSetConcurrency, createStreamingResultSet(), this.currentCatalog, cachedMetaData == null);
/*      */             
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 1227 */             if (oldCatalog != null) {
/* 1228 */               locallyScopedConn.setCatalog(oldCatalog);
/*      */             }
/*      */           }
/*      */         } else {
/* 1232 */           this.results = locallyScopedConn.execSQL(this, sql, -1, null, this.resultSetType, this.resultSetConcurrency, createStreamingResultSet(), this.currentCatalog, cachedMetaData == null);
/*      */         }
/*      */         
/*      */ 
/*      */ 
/*      */ 
/* 1238 */         if (timeoutTask != null) {
/* 1239 */           if (timeoutTask.caughtWhileCancelling != null) {
/* 1240 */             throw timeoutTask.caughtWhileCancelling;
/*      */           }
/*      */           
/* 1243 */           timeoutTask.cancel();
/* 1244 */           timeoutTask = null;
/*      */         }
/*      */         
/* 1247 */         synchronized (this.cancelTimeoutMutex) {
/* 1248 */           if (this.wasCancelled) {
/* 1249 */             this.wasCancelled = false;
/* 1250 */             throw new MySQLTimeoutException();
/*      */           }
/*      */         }
/*      */       } finally {
/* 1254 */         if (timeoutTask != null) {
/* 1255 */           timeoutTask.cancel();
/*      */         }
/*      */         
/* 1258 */         if (oldCatalog != null) {
/* 1259 */           locallyScopedConn.setCatalog(oldCatalog);
/*      */         }
/*      */       }
/*      */       
/* 1263 */       this.lastInsertId = this.results.getUpdateID();
/*      */       
/* 1265 */       if (cachedMetaData != null) {
/* 1266 */         locallyScopedConn.initializeResultsMetadataFromCache(sql, cachedMetaData, this.results);
/*      */ 
/*      */       }
/* 1269 */       else if (this.connection.getCacheResultSetMetadata()) {
/* 1270 */         locallyScopedConn.initializeResultsMetadataFromCache(sql, null, this.results);
/*      */       }
/*      */       
/*      */ 
/*      */ 
/* 1275 */       return this.results;
/*      */     }
/*      */   }
/*      */   
/*      */   protected void doPingInstead() throws SQLException {
/* 1280 */     if (this.pingTarget != null) {
/* 1281 */       this.pingTarget.doPing();
/*      */     } else {
/* 1283 */       this.connection.ping();
/*      */     }
/*      */     
/* 1286 */     ResultSet fakeSelectOneResultSet = generatePingResultSet();
/* 1287 */     this.results = fakeSelectOneResultSet;
/*      */   }
/*      */   
/*      */   protected ResultSet generatePingResultSet() throws SQLException {
/* 1291 */     Field[] fields = { new Field(null, "1", -5, 1) };
/* 1292 */     ArrayList rows = new ArrayList();
/* 1293 */     byte[] colVal = { 49 };
/*      */     
/* 1295 */     rows.add(new byte[][] { colVal });
/*      */     
/* 1297 */     return (ResultSet)DatabaseMetaData.buildResultSet(fields, rows, this.connection);
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
/*      */   public int executeUpdate(String sql)
/*      */     throws SQLException
/*      */   {
/* 1317 */     return executeUpdate(sql, false);
/*      */   }
/*      */   
/*      */   protected int executeUpdate(String sql, boolean isBatch) throws SQLException
/*      */   {
/* 1322 */     checkClosed();
/*      */     
/* 1324 */     Connection locallyScopedConn = this.connection;
/*      */     
/* 1326 */     char firstStatementChar = StringUtils.firstNonWsCharUc(sql, findStartOfStatement(sql));
/*      */     
/*      */ 
/* 1329 */     ResultSet rs = null;
/*      */     
/* 1331 */     synchronized (locallyScopedConn.getMutex()) {
/* 1332 */       synchronized (this.cancelTimeoutMutex) {
/* 1333 */         this.wasCancelled = false;
/*      */       }
/*      */       
/* 1336 */       checkNullOrEmptyQuery(sql);
/*      */       
/* 1338 */       if (this.doEscapeProcessing) {
/* 1339 */         Object escapedSqlResult = EscapeProcessor.escapeSQL(sql, this.connection.serverSupportsConvertFn(), this.connection);
/*      */         
/*      */ 
/* 1342 */         if ((escapedSqlResult instanceof String)) {
/* 1343 */           sql = (String)escapedSqlResult;
/*      */         } else {
/* 1345 */           sql = ((EscapeProcessorResult)escapedSqlResult).escapedSql;
/*      */         }
/*      */       }
/*      */       
/* 1349 */       if (locallyScopedConn.isReadOnly()) {
/* 1350 */         throw SQLError.createSQLException(Messages.getString("Statement.42") + Messages.getString("Statement.43"), "S1009");
/*      */       }
/*      */       
/*      */ 
/*      */ 
/*      */ 
/* 1356 */       if (StringUtils.startsWithIgnoreCaseAndWs(sql, "select")) {
/* 1357 */         throw SQLError.createSQLException(Messages.getString("Statement.46"), "01S03");
/*      */       }
/*      */       
/*      */ 
/*      */ 
/* 1362 */       if ((this.results != null) && 
/* 1363 */         (!locallyScopedConn.getHoldResultsOpenOverStatementClose())) {
/* 1364 */         this.results.realClose(false);
/*      */       }
/*      */       
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 1372 */       CancelTask timeoutTask = null;
/*      */       
/* 1374 */       String oldCatalog = null;
/*      */       try
/*      */       {
/* 1377 */         if ((locallyScopedConn.getEnableQueryTimeouts()) && (this.timeoutInMillis != 0) && (locallyScopedConn.versionMeetsMinimum(5, 0, 0)))
/*      */         {
/*      */ 
/* 1380 */           timeoutTask = new CancelTask();
/* 1381 */           Connection.getCancelTimer().schedule(timeoutTask, this.timeoutInMillis);
/*      */         }
/*      */         
/*      */ 
/* 1385 */         if (!locallyScopedConn.getCatalog().equals(this.currentCatalog)) {
/* 1386 */           oldCatalog = locallyScopedConn.getCatalog();
/* 1387 */           locallyScopedConn.setCatalog(this.currentCatalog);
/*      */         }
/*      */         
/*      */ 
/*      */ 
/*      */ 
/* 1393 */         if (locallyScopedConn.useMaxRows()) {
/* 1394 */           locallyScopedConn.execSQL(this, "SET OPTION SQL_SELECT_LIMIT=DEFAULT", -1, null, 1003, 1007, false, this.currentCatalog, true);
/*      */         }
/*      */         
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 1402 */         rs = locallyScopedConn.execSQL(this, sql, -1, null, 1003, 1007, false, this.currentCatalog, true, isBatch);
/*      */         
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 1409 */         if (timeoutTask != null) {
/* 1410 */           if (timeoutTask.caughtWhileCancelling != null) {
/* 1411 */             throw timeoutTask.caughtWhileCancelling;
/*      */           }
/*      */           
/* 1414 */           timeoutTask.cancel();
/* 1415 */           timeoutTask = null;
/*      */         }
/*      */         
/* 1418 */         synchronized (this.cancelTimeoutMutex) {
/* 1419 */           if (this.wasCancelled) {
/* 1420 */             this.wasCancelled = false;
/* 1421 */             throw new MySQLTimeoutException();
/*      */           }
/*      */         }
/*      */       } finally {
/* 1425 */         if (timeoutTask != null) {
/* 1426 */           timeoutTask.cancel();
/*      */         }
/*      */         
/* 1429 */         if (oldCatalog != null) {
/* 1430 */           locallyScopedConn.setCatalog(oldCatalog);
/*      */         }
/*      */       }
/*      */     }
/*      */     
/* 1435 */     this.results = rs;
/*      */     
/* 1437 */     rs.setFirstCharOfQuery(firstStatementChar);
/*      */     
/* 1439 */     this.updateCount = rs.getUpdateCount();
/*      */     
/* 1441 */     int truncatedUpdateCount = 0;
/*      */     
/* 1443 */     if (this.updateCount > 2147483647L) {
/* 1444 */       truncatedUpdateCount = Integer.MAX_VALUE;
/*      */     } else {
/* 1446 */       truncatedUpdateCount = (int)this.updateCount;
/*      */     }
/*      */     
/* 1449 */     this.lastInsertId = rs.getUpdateID();
/*      */     
/* 1451 */     return truncatedUpdateCount;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public int executeUpdate(String sql, int returnGeneratedKeys)
/*      */     throws SQLException
/*      */   {
/* 1459 */     if (returnGeneratedKeys == 1) {
/* 1460 */       checkClosed();
/*      */       
/* 1462 */       Connection locallyScopedConn = this.connection;
/*      */       
/* 1464 */       synchronized (locallyScopedConn.getMutex())
/*      */       {
/*      */ 
/*      */ 
/* 1468 */         boolean readInfoMsgState = locallyScopedConn.isReadInfoMsgEnabled();
/*      */         
/* 1470 */         locallyScopedConn.setReadInfoMsgEnabled(true);
/*      */         try
/*      */         {
/* 1473 */           int i = executeUpdate(sql);
/*      */           
/* 1475 */           locallyScopedConn.setReadInfoMsgEnabled(readInfoMsgState);return i; } finally { locallyScopedConn.setReadInfoMsgEnabled(readInfoMsgState);
/*      */         }
/*      */       }
/*      */     }
/*      */     
/* 1480 */     return executeUpdate(sql);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public int executeUpdate(String sql, int[] generatedKeyIndices)
/*      */     throws SQLException
/*      */   {
/* 1488 */     if ((generatedKeyIndices != null) && (generatedKeyIndices.length > 0)) {
/* 1489 */       checkClosed();
/*      */       
/* 1491 */       Connection locallyScopedConn = this.connection;
/*      */       
/* 1493 */       synchronized (locallyScopedConn.getMutex())
/*      */       {
/*      */ 
/*      */ 
/* 1497 */         boolean readInfoMsgState = locallyScopedConn.isReadInfoMsgEnabled();
/*      */         
/* 1499 */         locallyScopedConn.setReadInfoMsgEnabled(true);
/*      */         try
/*      */         {
/* 1502 */           int i = executeUpdate(sql);
/*      */           
/* 1504 */           locallyScopedConn.setReadInfoMsgEnabled(readInfoMsgState);return i; } finally { locallyScopedConn.setReadInfoMsgEnabled(readInfoMsgState);
/*      */         }
/*      */       }
/*      */     }
/*      */     
/* 1509 */     return executeUpdate(sql);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public int executeUpdate(String sql, String[] generatedKeyNames)
/*      */     throws SQLException
/*      */   {
/* 1517 */     if ((generatedKeyNames != null) && (generatedKeyNames.length > 0)) {
/* 1518 */       checkClosed();
/*      */       
/* 1520 */       Connection locallyScopedConn = this.connection;
/*      */       
/* 1522 */       synchronized (locallyScopedConn.getMutex())
/*      */       {
/*      */ 
/*      */ 
/* 1526 */         boolean readInfoMsgState = this.connection.isReadInfoMsgEnabled();
/*      */         
/* 1528 */         locallyScopedConn.setReadInfoMsgEnabled(true);
/*      */         try
/*      */         {
/* 1531 */           int i = executeUpdate(sql);
/*      */           
/* 1533 */           locallyScopedConn.setReadInfoMsgEnabled(readInfoMsgState);return i; } finally { locallyScopedConn.setReadInfoMsgEnabled(readInfoMsgState);
/*      */         }
/*      */       }
/*      */     }
/*      */     
/* 1538 */     return executeUpdate(sql);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected Calendar getCalendarInstanceForSessionOrNew()
/*      */   {
/* 1548 */     if (this.connection != null) {
/* 1549 */       return this.connection.getCalendarInstanceForSessionOrNew();
/*      */     }
/*      */     
/* 1552 */     return new GregorianCalendar();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public java.sql.Connection getConnection()
/*      */     throws SQLException
/*      */   {
/* 1565 */     return this.connection;
/*      */   }
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
/* 1577 */     return 1000;
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
/* 1589 */     return this.fetchSize;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public java.sql.ResultSet getGeneratedKeys()
/*      */     throws SQLException
/*      */   {
/* 1602 */     if (this.batchedGeneratedKeys == null) {
/* 1603 */       return getGeneratedKeysInternal();
/*      */     }
/*      */     
/* 1606 */     Field[] fields = new Field[1];
/* 1607 */     fields[0] = new Field("", "GENERATED_KEY", -5, 17);
/* 1608 */     fields[0].setConnection(this.connection);
/*      */     
/* 1610 */     return new ResultSet(this.currentCatalog, fields, new RowDataStatic(this.batchedGeneratedKeys), this.connection, this);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected java.sql.ResultSet getGeneratedKeysInternal()
/*      */     throws SQLException
/*      */   {
/* 1622 */     Field[] fields = new Field[1];
/* 1623 */     fields[0] = new Field("", "GENERATED_KEY", -5, 17);
/* 1624 */     fields[0].setConnection(this.connection);
/*      */     
/* 1626 */     ArrayList rowSet = new ArrayList();
/*      */     
/* 1628 */     long beginAt = getLastInsertID();
/* 1629 */     int numKeys = getUpdateCount();
/*      */     
/* 1631 */     if (this.results != null) {
/* 1632 */       String serverInfo = this.results.getServerInfo();
/*      */       
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 1638 */       if ((numKeys > 0) && (this.results.getFirstCharOfQuery() == 'R') && (serverInfo != null) && (serverInfo.length() > 0))
/*      */       {
/* 1640 */         numKeys = getRecordCountFromInfo(serverInfo);
/*      */       }
/*      */       
/* 1643 */       if ((beginAt > 0L) && (numKeys > 0)) {
/* 1644 */         for (int i = 0; i < numKeys; i++) {
/* 1645 */           byte[][] row = new byte[1][];
/* 1646 */           row[0] = Long.toString(beginAt++).getBytes();
/* 1647 */           rowSet.add(row);
/*      */         }
/*      */       }
/*      */     }
/*      */     
/* 1652 */     return new ResultSet(this.currentCatalog, fields, new RowDataStatic(rowSet), this.connection, this);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected int getId()
/*      */   {
/* 1662 */     return this.statementId;
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
/*      */   public long getLastInsertID()
/*      */   {
/* 1679 */     return this.lastInsertId;
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
/*      */   public long getLongUpdateCount()
/*      */   {
/* 1695 */     if (this.results == null) {
/* 1696 */       return -1L;
/*      */     }
/*      */     
/* 1699 */     if (this.results.reallyResult()) {
/* 1700 */       return -1L;
/*      */     }
/*      */     
/* 1703 */     return this.updateCount;
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
/*      */   public int getMaxFieldSize()
/*      */     throws SQLException
/*      */   {
/* 1718 */     return this.maxFieldSize;
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
/*      */   public int getMaxRows()
/*      */     throws SQLException
/*      */   {
/* 1732 */     if (this.maxRows <= 0) {
/* 1733 */       return 0;
/*      */     }
/*      */     
/* 1736 */     return this.maxRows;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public boolean getMoreResults()
/*      */     throws SQLException
/*      */   {
/* 1749 */     return getMoreResults(1);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public boolean getMoreResults(int current)
/*      */     throws SQLException
/*      */   {
/* 1757 */     if (this.results == null) {
/* 1758 */       return false;
/*      */     }
/*      */     
/* 1761 */     ResultSet nextResultSet = this.results.getNextResultSet();
/*      */     
/* 1763 */     switch (current)
/*      */     {
/*      */     case 1: 
/* 1766 */       if (this.results != null) {
/* 1767 */         this.results.close();
/* 1768 */         this.results.clearNextResult();
/*      */       }
/*      */       
/*      */ 
/*      */ 
/*      */       break;
/*      */     case 3: 
/* 1775 */       if (this.results != null) {
/* 1776 */         this.results.close();
/* 1777 */         this.results.clearNextResult();
/*      */       }
/*      */       
/* 1780 */       closeAllOpenResults();
/*      */       
/* 1782 */       break;
/*      */     
/*      */     case 2: 
/* 1785 */       if (!this.connection.getDontTrackOpenResources()) {
/* 1786 */         this.openResults.add(this.results);
/*      */       }
/*      */       
/* 1789 */       this.results.clearNextResult();
/*      */       
/* 1791 */       break;
/*      */     
/*      */     default: 
/* 1794 */       throw SQLError.createSQLException(Messages.getString("Statement.19"), "S1009");
/*      */     }
/*      */     
/*      */     
/*      */ 
/* 1799 */     this.results = nextResultSet;
/*      */     
/* 1801 */     if (this.results == null) {
/* 1802 */       this.updateCount = -1L;
/* 1803 */       this.lastInsertId = -1L;
/* 1804 */     } else if (this.results.reallyResult()) {
/* 1805 */       this.updateCount = -1L;
/* 1806 */       this.lastInsertId = -1L;
/*      */     } else {
/* 1808 */       this.updateCount = this.results.getUpdateCount();
/* 1809 */       this.lastInsertId = this.results.getUpdateID();
/*      */     }
/*      */     
/* 1812 */     return (this.results != null) && (this.results.reallyResult());
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
/*      */   public int getQueryTimeout()
/*      */     throws SQLException
/*      */   {
/* 1827 */     return this.timeoutInMillis / 1000;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   private int getRecordCountFromInfo(String serverInfo)
/*      */   {
/* 1839 */     StringBuffer recordsBuf = new StringBuffer();
/* 1840 */     int recordsCount = 0;
/* 1841 */     int duplicatesCount = 0;
/*      */     
/* 1843 */     char c = '\000';
/*      */     
/* 1845 */     int length = serverInfo.length();
/* 1846 */     for (int i = 0; 
/*      */         
/* 1848 */         i < length; i++) {
/* 1849 */       c = serverInfo.charAt(i);
/*      */       
/* 1851 */       if (Character.isDigit(c)) {
/*      */         break;
/*      */       }
/*      */     }
/*      */     
/* 1856 */     recordsBuf.append(c);
/* 1857 */     i++;
/* 1859 */     for (; 
/* 1859 */         i < length; i++) {
/* 1860 */       c = serverInfo.charAt(i);
/*      */       
/* 1862 */       if (!Character.isDigit(c)) {
/*      */         break;
/*      */       }
/*      */       
/* 1866 */       recordsBuf.append(c);
/*      */     }
/*      */     
/* 1869 */     recordsCount = Integer.parseInt(recordsBuf.toString());
/*      */     
/* 1871 */     StringBuffer duplicatesBuf = new StringBuffer();
/* 1873 */     for (; 
/* 1873 */         i < length; i++) {
/* 1874 */       c = serverInfo.charAt(i);
/*      */       
/* 1876 */       if (Character.isDigit(c)) {
/*      */         break;
/*      */       }
/*      */     }
/*      */     
/* 1881 */     duplicatesBuf.append(c);
/* 1882 */     i++;
/* 1884 */     for (; 
/* 1884 */         i < length; i++) {
/* 1885 */       c = serverInfo.charAt(i);
/*      */       
/* 1887 */       if (!Character.isDigit(c)) {
/*      */         break;
/*      */       }
/*      */       
/* 1891 */       duplicatesBuf.append(c);
/*      */     }
/*      */     
/* 1894 */     duplicatesCount = Integer.parseInt(duplicatesBuf.toString());
/*      */     
/* 1896 */     return recordsCount - duplicatesCount;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public java.sql.ResultSet getResultSet()
/*      */     throws SQLException
/*      */   {
/* 1909 */     return (this.results != null) && (this.results.reallyResult()) ? this.results : null;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public int getResultSetConcurrency()
/*      */     throws SQLException
/*      */   {
/* 1922 */     return this.resultSetConcurrency;
/*      */   }
/*      */   
/*      */ 
/*      */   public int getResultSetHoldability()
/*      */     throws SQLException
/*      */   {
/* 1929 */     return 1;
/*      */   }
/*      */   
/*      */   protected ResultSet getResultSetInternal() {
/* 1933 */     return this.results;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public int getResultSetType()
/*      */     throws SQLException
/*      */   {
/* 1945 */     return this.resultSetType;
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
/*      */   public int getUpdateCount()
/*      */     throws SQLException
/*      */   {
/* 1959 */     if (this.results == null) {
/* 1960 */       return -1;
/*      */     }
/*      */     
/* 1963 */     if (this.results.reallyResult()) {
/* 1964 */       return -1;
/*      */     }
/*      */     
/* 1967 */     int truncatedUpdateCount = 0;
/*      */     
/* 1969 */     if (this.results.getUpdateCount() > 2147483647L) {
/* 1970 */       truncatedUpdateCount = Integer.MAX_VALUE;
/*      */     } else {
/* 1972 */       truncatedUpdateCount = (int)this.results.getUpdateCount();
/*      */     }
/*      */     
/* 1975 */     return truncatedUpdateCount;
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
/*      */   public SQLWarning getWarnings()
/*      */     throws SQLException
/*      */   {
/* 2000 */     checkClosed();
/*      */     
/* 2002 */     if ((this.connection != null) && (!this.connection.isClosed()) && (this.connection.versionMeetsMinimum(4, 1, 0)))
/*      */     {
/* 2004 */       SQLWarning pendingWarningsFromServer = SQLError.convertShowWarningsToSQLWarnings(this.connection);
/*      */       
/*      */ 
/* 2007 */       if (this.warningChain != null) {
/* 2008 */         this.warningChain.setNextWarning(pendingWarningsFromServer);
/*      */       } else {
/* 2010 */         this.warningChain = pendingWarningsFromServer;
/*      */       }
/*      */       
/* 2013 */       return this.warningChain;
/*      */     }
/*      */     
/* 2016 */     return this.warningChain;
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
/*      */   protected void realClose(boolean calledExplicitly, boolean closeOpenResults)
/*      */     throws SQLException
/*      */   {
/* 2032 */     if (this.isClosed) {
/* 2033 */       return;
/*      */     }
/*      */     
/* 2036 */     if ((this.useUsageAdvisor) && 
/* 2037 */       (!calledExplicitly)) {
/* 2038 */       String message = Messages.getString("Statement.63") + Messages.getString("Statement.64");
/*      */       
/*      */ 
/* 2041 */       this.eventSink.consumeEvent(new ProfilerEvent((byte)0, "", this.currentCatalog, this.connectionId, getId(), -1, System.currentTimeMillis(), 0L, Constants.MILLIS_I18N, null, this.pointOfOrigin, message));
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 2051 */     if (this.results != null) {
/* 2052 */       if (closeOpenResults) {
/* 2053 */         closeOpenResults = !this.holdResultsOpenOverClose;
/*      */       }
/*      */       
/* 2056 */       if ((closeOpenResults) && (this.connection != null) && (!this.connection.getHoldResultsOpenOverStatementClose()))
/*      */       {
/*      */         try {
/* 2059 */           this.results.close();
/*      */         }
/*      */         catch (Exception ex) {}
/*      */         
/*      */ 
/* 2064 */         closeAllOpenResults();
/*      */       }
/*      */     }
/*      */     
/* 2068 */     if (this.connection != null) {
/* 2069 */       if (this.maxRowsChanged) {
/* 2070 */         this.connection.unsetMaxRows(this);
/*      */       }
/*      */       
/* 2073 */       if (!this.connection.getDontTrackOpenResources()) {
/* 2074 */         this.connection.unregisterStatement(this);
/*      */       }
/*      */     }
/*      */     
/* 2078 */     this.isClosed = true;
/*      */     
/* 2080 */     this.results = null;
/* 2081 */     this.connection = null;
/* 2082 */     this.warningChain = null;
/* 2083 */     this.openResults = null;
/* 2084 */     this.batchedGeneratedKeys = null;
/* 2085 */     this.cancelTimeoutMutex = null;
/* 2086 */     this.pingTarget = null;
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
/*      */   public void setCursorName(String name)
/*      */     throws SQLException
/*      */   {}
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void setEscapeProcessing(boolean enable)
/*      */     throws SQLException
/*      */   {
/* 2122 */     this.doEscapeProcessing = enable;
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
/*      */   public void setFetchDirection(int direction)
/*      */     throws SQLException
/*      */   {
/* 2139 */     switch (direction)
/*      */     {
/*      */     case 1000: 
/*      */     case 1001: 
/*      */     case 1002: 
/*      */       break;
/*      */     default: 
/* 2146 */       throw SQLError.createSQLException(Messages.getString("Statement.5"), "S1009");
/*      */     }
/*      */     
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
/*      */   public void setFetchSize(int rows)
/*      */     throws SQLException
/*      */   {
/* 2167 */     if (((rows < 0) && (rows != Integer.MIN_VALUE)) || ((this.maxRows != 0) && (this.maxRows != -1) && (rows > getMaxRows())))
/*      */     {
/*      */ 
/* 2170 */       throw SQLError.createSQLException(Messages.getString("Statement.7"), "S1009");
/*      */     }
/*      */     
/*      */ 
/*      */ 
/* 2175 */     this.fetchSize = rows;
/*      */   }
/*      */   
/*      */   protected void setHoldResultsOpenOverClose(boolean holdResultsOpenOverClose) {
/* 2179 */     this.holdResultsOpenOverClose = holdResultsOpenOverClose;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void setMaxFieldSize(int max)
/*      */     throws SQLException
/*      */   {
/* 2192 */     if (max < 0) {
/* 2193 */       throw SQLError.createSQLException(Messages.getString("Statement.11"), "S1009");
/*      */     }
/*      */     
/*      */ 
/*      */ 
/* 2198 */     int maxBuf = this.connection != null ? this.connection.getMaxAllowedPacket() : MysqlIO.getMaxBuf();
/*      */     
/*      */ 
/* 2201 */     if (max > maxBuf) {
/* 2202 */       throw SQLError.createSQLException(Messages.getString("Statement.13", new Object[] { new Long(maxBuf) }), "S1009");
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/* 2208 */     this.maxFieldSize = max;
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
/*      */   public void setMaxRows(int max)
/*      */     throws SQLException
/*      */   {
/* 2223 */     if ((max > 50000000) || (max < 0)) {
/* 2224 */       throw SQLError.createSQLException(Messages.getString("Statement.15") + max + " > " + 50000000 + ".", "S1009");
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 2231 */     if (max == 0) {
/* 2232 */       max = -1;
/*      */     }
/*      */     
/* 2235 */     this.maxRows = max;
/* 2236 */     this.maxRowsChanged = true;
/*      */     
/* 2238 */     if (this.maxRows == -1) {
/* 2239 */       this.connection.unsetMaxRows(this);
/* 2240 */       this.maxRowsChanged = false;
/*      */ 
/*      */ 
/*      */     }
/*      */     else
/*      */     {
/*      */ 
/* 2247 */       this.connection.maxRowsChanged(this);
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
/*      */   public void setQueryTimeout(int seconds)
/*      */     throws SQLException
/*      */   {
/* 2261 */     if (seconds < 0) {
/* 2262 */       throw SQLError.createSQLException(Messages.getString("Statement.21"), "S1009");
/*      */     }
/*      */     
/*      */ 
/*      */ 
/* 2267 */     this.timeoutInMillis = (seconds * 1000);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   void setResultSetConcurrency(int concurrencyFlag)
/*      */   {
/* 2277 */     this.resultSetConcurrency = concurrencyFlag;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   void setResultSetType(int typeFlag)
/*      */   {
/* 2287 */     this.resultSetType = typeFlag;
/*      */   }
/*      */   
/*      */   protected void getBatchedGeneratedKeys(java.sql.Statement batchedStatement) throws SQLException {
/* 2291 */     if (this.retrieveGeneratedKeys) {
/* 2292 */       java.sql.ResultSet rs = null;
/*      */       try
/*      */       {
/* 2295 */         rs = batchedStatement.getGeneratedKeys();
/*      */         
/* 2297 */         while (rs.next()) {
/* 2298 */           this.batchedGeneratedKeys.add(new byte[][] { rs.getBytes(1) });
/*      */         }
/*      */       }
/*      */       finally {
/* 2302 */         if (rs != null) {
/* 2303 */           rs.close();
/*      */         }
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */   protected void getBatchedGeneratedKeys() throws SQLException {
/* 2310 */     if (this.retrieveGeneratedKeys) {
/* 2311 */       java.sql.ResultSet rs = null;
/*      */       try
/*      */       {
/* 2314 */         rs = getGeneratedKeysInternal();
/*      */         
/* 2316 */         while (rs.next()) {
/* 2317 */           this.batchedGeneratedKeys.add(new byte[][] { rs.getBytes(1) });
/*      */         }
/*      */       }
/*      */       finally {
/* 2321 */         if (rs != null) {
/* 2322 */           rs.close();
/*      */         }
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   private boolean useServerFetch()
/*      */     throws SQLException
/*      */   {
/* 2333 */     return (this.connection.isCursorFetchEnabled()) && (this.fetchSize > 0) && (this.resultSetConcurrency == 1007) && (this.resultSetType == 1003);
/*      */   }
/*      */   
/*      */ 
/*      */   protected int findStartOfStatement(String sql)
/*      */   {
/* 2339 */     int statementStartPos = 0;
/*      */     
/* 2341 */     if (StringUtils.startsWithIgnoreCaseAndWs(sql, "/*")) {
/* 2342 */       statementStartPos = sql.indexOf("*/");
/*      */       
/* 2344 */       if (statementStartPos == -1) {
/* 2345 */         statementStartPos = 0;
/*      */       } else {
/* 2347 */         statementStartPos += 2;
/*      */       }
/* 2349 */     } else if ((StringUtils.startsWithIgnoreCaseAndWs(sql, "--")) || (StringUtils.startsWithIgnoreCaseAndWs(sql, "#")))
/*      */     {
/* 2351 */       statementStartPos = sql.indexOf('\n');
/*      */       
/* 2353 */       if (statementStartPos == -1) {
/* 2354 */         statementStartPos = sql.indexOf('\r');
/*      */         
/* 2356 */         if (statementStartPos == -1) {
/* 2357 */           statementStartPos = 0;
/*      */         }
/*      */       }
/*      */     }
/*      */     
/* 2362 */     return statementStartPos;
/*      */   }
/*      */   
/*      */   protected synchronized void setPingTarget(PingTarget pingTarget) {
/* 2366 */     this.pingTarget = pingTarget;
/*      */   }
/*      */ }


/* Location:              E:\java\java学习\hutubill\lib\all.jar!\com\mysql\jdbc\Statement.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version:       0.7.1
 */
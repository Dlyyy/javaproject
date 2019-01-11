/*      */ package com.mysql.jdbc;
/*      */ 
/*      */ import com.mysql.jdbc.exceptions.MySQLTimeoutException;
/*      */ import com.mysql.jdbc.profiler.ProfileEventSink;
/*      */ import com.mysql.jdbc.profiler.ProfilerEvent;
/*      */ import java.io.IOException;
/*      */ import java.io.InputStream;
/*      */ import java.io.Reader;
/*      */ import java.io.UnsupportedEncodingException;
/*      */ import java.math.BigDecimal;
/*      */ import java.net.URL;
/*      */ import java.sql.Array;
/*      */ import java.sql.BatchUpdateException;
/*      */ import java.sql.Blob;
/*      */ import java.sql.Clob;
/*      */ import java.sql.ParameterMetaData;
/*      */ import java.sql.Ref;
/*      */ import java.sql.SQLException;
/*      */ import java.sql.Time;
/*      */ import java.sql.Timestamp;
/*      */ import java.util.ArrayList;
/*      */ import java.util.Calendar;
/*      */ import java.util.List;
/*      */ import java.util.TimeZone;
/*      */ import java.util.Timer;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ public class ServerPreparedStatement
/*      */   extends PreparedStatement
/*      */ {
/*      */   protected static final int BLOB_STREAM_READ_BUF_SIZE = 8192;
/*      */   private static final byte MAX_DATE_REP_LENGTH = 5;
/*      */   private static final byte MAX_DATETIME_REP_LENGTH = 12;
/*      */   private static final byte MAX_TIME_REP_LENGTH = 13;
/*      */   
/*      */   static class BatchedBindValues
/*      */   {
/*      */     ServerPreparedStatement.BindValue[] batchedParameterValues;
/*      */     
/*      */     BatchedBindValues(ServerPreparedStatement.BindValue[] paramVals)
/*      */     {
/*   74 */       int numParams = paramVals.length;
/*      */       
/*   76 */       this.batchedParameterValues = new ServerPreparedStatement.BindValue[numParams];
/*      */       
/*   78 */       for (int i = 0; i < numParams; i++) {
/*   79 */         this.batchedParameterValues[i] = new ServerPreparedStatement.BindValue(paramVals[i]);
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */   static class BindValue
/*      */   {
/*   86 */     long boundBeforeExecutionNum = 0L;
/*      */     
/*      */     long bindLength;
/*      */     
/*      */     int bufferType;
/*      */     
/*      */     byte byteBinding;
/*      */     
/*      */     double doubleBinding;
/*      */     
/*      */     float floatBinding;
/*      */     
/*      */     int intBinding;
/*      */     
/*      */     boolean isLongData;
/*      */     
/*      */     boolean isNull;
/*      */     
/*  104 */     boolean isSet = false;
/*      */     
/*      */     long longBinding;
/*      */     
/*      */     short shortBinding;
/*      */     
/*      */     Object value;
/*      */     
/*      */     BindValue() {}
/*      */     
/*      */     BindValue(BindValue copyMe)
/*      */     {
/*  116 */       this.value = copyMe.value;
/*  117 */       this.isSet = copyMe.isSet;
/*  118 */       this.isLongData = copyMe.isLongData;
/*  119 */       this.isNull = copyMe.isNull;
/*  120 */       this.bufferType = copyMe.bufferType;
/*  121 */       this.bindLength = copyMe.bindLength;
/*  122 */       this.byteBinding = copyMe.byteBinding;
/*  123 */       this.shortBinding = copyMe.shortBinding;
/*  124 */       this.intBinding = copyMe.intBinding;
/*  125 */       this.longBinding = copyMe.longBinding;
/*  126 */       this.floatBinding = copyMe.floatBinding;
/*  127 */       this.doubleBinding = copyMe.doubleBinding;
/*      */     }
/*      */     
/*      */     void reset() {
/*  131 */       this.isSet = false;
/*  132 */       this.value = null;
/*  133 */       this.isLongData = false;
/*      */       
/*  135 */       this.byteBinding = 0;
/*  136 */       this.shortBinding = 0;
/*  137 */       this.intBinding = 0;
/*  138 */       this.longBinding = 0L;
/*  139 */       this.floatBinding = 0.0F;
/*  140 */       this.doubleBinding = 0.0D;
/*      */     }
/*      */     
/*      */     public String toString() {
/*  144 */       return toString(false);
/*      */     }
/*      */     
/*      */     public String toString(boolean quoteIfNeeded) {
/*  148 */       if (this.isLongData) {
/*  149 */         return "' STREAM DATA '";
/*      */       }
/*      */       
/*  152 */       switch (this.bufferType) {
/*      */       case 1: 
/*  154 */         return String.valueOf(this.byteBinding);
/*      */       case 2: 
/*  156 */         return String.valueOf(this.shortBinding);
/*      */       case 3: 
/*  158 */         return String.valueOf(this.intBinding);
/*      */       case 8: 
/*  160 */         return String.valueOf(this.longBinding);
/*      */       case 4: 
/*  162 */         return String.valueOf(this.floatBinding);
/*      */       case 5: 
/*  164 */         return String.valueOf(this.doubleBinding);
/*      */       case 7: 
/*      */       case 10: 
/*      */       case 11: 
/*      */       case 12: 
/*      */       case 15: 
/*      */       case 253: 
/*      */       case 254: 
/*  172 */         if (quoteIfNeeded) {
/*  173 */           return "'" + String.valueOf(this.value) + "'";
/*      */         }
/*  175 */         return String.valueOf(this.value);
/*      */       }
/*      */       
/*  178 */       if ((this.value instanceof byte[])) {
/*  179 */         return "byte data";
/*      */       }
/*      */       
/*  182 */       if (quoteIfNeeded) {
/*  183 */         return "'" + String.valueOf(this.value) + "'";
/*      */       }
/*  185 */       return String.valueOf(this.value);
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */     long getBoundLength()
/*      */     {
/*  192 */       if (this.isNull) {
/*  193 */         return 0L;
/*      */       }
/*      */       
/*  196 */       if (this.isLongData) {
/*  197 */         return this.bindLength;
/*      */       }
/*      */       
/*  200 */       switch (this.bufferType)
/*      */       {
/*      */       case 1: 
/*  203 */         return 1L;
/*      */       case 2: 
/*  205 */         return 2L;
/*      */       case 3: 
/*  207 */         return 4L;
/*      */       case 8: 
/*  209 */         return 8L;
/*      */       case 4: 
/*  211 */         return 4L;
/*      */       case 5: 
/*  213 */         return 8L;
/*      */       case 11: 
/*  215 */         return 9L;
/*      */       case 10: 
/*  217 */         return 7L;
/*      */       case 7: 
/*      */       case 12: 
/*  220 */         return 11L;
/*      */       case 0: 
/*      */       case 15: 
/*      */       case 246: 
/*      */       case 253: 
/*      */       case 254: 
/*  226 */         if ((this.value instanceof byte[])) {
/*  227 */           return ((byte[])this.value).length;
/*      */         }
/*  229 */         return ((String)this.value).length();
/*      */       }
/*      */       
/*  232 */       return 0L;
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
/*      */   private void storeTime(Buffer intoBuf, Time tm)
/*      */     throws SQLException
/*      */   {
/*  254 */     intoBuf.ensureCapacity(9);
/*  255 */     intoBuf.writeByte((byte)8);
/*  256 */     intoBuf.writeByte((byte)0);
/*  257 */     intoBuf.writeLong(0L);
/*      */     
/*  259 */     Calendar sessionCalendar = getCalendarInstanceForSessionOrNew();
/*      */     
/*  261 */     synchronized (sessionCalendar) {
/*  262 */       java.util.Date oldTime = sessionCalendar.getTime();
/*      */       try {
/*  264 */         sessionCalendar.setTime(tm);
/*  265 */         intoBuf.writeByte((byte)sessionCalendar.get(11));
/*  266 */         intoBuf.writeByte((byte)sessionCalendar.get(12));
/*  267 */         intoBuf.writeByte((byte)sessionCalendar.get(13));
/*      */       }
/*      */       finally
/*      */       {
/*  271 */         sessionCalendar.setTime(oldTime);
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  281 */   private boolean detectedLongParameterSwitch = false;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   private int fieldCount;
/*      */   
/*      */ 
/*      */ 
/*  290 */   private boolean invalid = false;
/*      */   
/*      */ 
/*      */   private SQLException invalidationException;
/*      */   
/*      */ 
/*      */   private boolean isSelectQuery;
/*      */   
/*      */ 
/*      */   private Buffer outByteBuffer;
/*      */   
/*      */ 
/*      */   private BindValue[] parameterBindings;
/*      */   
/*      */ 
/*      */   private Field[] parameterFields;
/*      */   
/*      */ 
/*      */   private Field[] resultFields;
/*      */   
/*  310 */   private boolean sendTypesToServer = false;
/*      */   
/*      */ 
/*      */   private long serverStatementId;
/*      */   
/*      */ 
/*  316 */   private int stringTypeCode = 254;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   private boolean serverNeedsResetBeforeEachExecution;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public ServerPreparedStatement(Connection conn, String sql, String catalog, int resultSetType, int resultSetConcurrency)
/*      */     throws SQLException
/*      */   {
/*  336 */     super(conn, catalog);
/*      */     
/*  338 */     checkNullOrEmptyQuery(sql);
/*      */     
/*  340 */     this.isSelectQuery = StringUtils.startsWithIgnoreCaseAndWs(sql, "SELECT");
/*      */     
/*      */ 
/*  343 */     if (this.connection.versionMeetsMinimum(5, 0, 0)) {
/*  344 */       this.serverNeedsResetBeforeEachExecution = (!this.connection.versionMeetsMinimum(5, 0, 3));
/*      */     }
/*      */     else {
/*  347 */       this.serverNeedsResetBeforeEachExecution = (!this.connection.versionMeetsMinimum(4, 1, 10));
/*      */     }
/*      */     
/*      */ 
/*  351 */     this.useTrueBoolean = this.connection.versionMeetsMinimum(3, 21, 23);
/*  352 */     this.hasLimitClause = (StringUtils.indexOfIgnoreCase(sql, "LIMIT") != -1);
/*  353 */     this.firstCharOfStmt = StringUtils.firstNonWsCharUc(sql);
/*  354 */     this.originalSql = sql;
/*      */     
/*  356 */     if (this.connection.versionMeetsMinimum(4, 1, 2)) {
/*  357 */       this.stringTypeCode = 253;
/*      */     } else {
/*  359 */       this.stringTypeCode = 254;
/*      */     }
/*      */     try
/*      */     {
/*  363 */       serverPrepare(sql);
/*      */     } catch (SQLException sqlEx) {
/*  365 */       realClose(false, true);
/*      */       
/*  367 */       throw sqlEx;
/*      */     } catch (Exception ex) {
/*  369 */       realClose(false, true);
/*      */       
/*  371 */       throw SQLError.createSQLException(ex.toString(), "S1000");
/*      */     }
/*      */     
/*      */ 
/*  375 */     setResultSetType(resultSetType);
/*  376 */     setResultSetConcurrency(resultSetConcurrency);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public synchronized void addBatch()
/*      */     throws SQLException
/*      */   {
/*  388 */     checkClosed();
/*      */     
/*  390 */     if (this.batchedArgs == null) {
/*  391 */       this.batchedArgs = new ArrayList();
/*      */     }
/*      */     
/*  394 */     this.batchedArgs.add(new BatchedBindValues(this.parameterBindings));
/*      */   }
/*      */   
/*      */   protected String asSql(boolean quoteStreamsAndUnknowns) throws SQLException
/*      */   {
/*  399 */     if (this.isClosed) {
/*  400 */       return "statement has been closed, no further internal information available";
/*      */     }
/*      */     
/*  403 */     PreparedStatement pStmtForSub = null;
/*      */     try
/*      */     {
/*  406 */       pStmtForSub = new PreparedStatement(this.connection, this.originalSql, this.currentCatalog);
/*      */       
/*      */ 
/*  409 */       int numParameters = pStmtForSub.parameterCount;
/*  410 */       int ourNumParameters = this.parameterCount;
/*      */       
/*  412 */       for (int i = 0; (i < numParameters) && (i < ourNumParameters); i++) {
/*  413 */         if (this.parameterBindings[i] != null) {
/*  414 */           if (this.parameterBindings[i].isNull) {
/*  415 */             pStmtForSub.setNull(i + 1, 0);
/*      */           } else {
/*  417 */             BindValue bindValue = this.parameterBindings[i];
/*      */             
/*      */ 
/*      */ 
/*      */ 
/*  422 */             switch (bindValue.bufferType)
/*      */             {
/*      */             case 1: 
/*  425 */               pStmtForSub.setByte(i + 1, bindValue.byteBinding);
/*  426 */               break;
/*      */             case 2: 
/*  428 */               pStmtForSub.setShort(i + 1, bindValue.shortBinding);
/*  429 */               break;
/*      */             case 3: 
/*  431 */               pStmtForSub.setInt(i + 1, bindValue.intBinding);
/*  432 */               break;
/*      */             case 8: 
/*  434 */               pStmtForSub.setLong(i + 1, bindValue.longBinding);
/*  435 */               break;
/*      */             case 4: 
/*  437 */               pStmtForSub.setFloat(i + 1, bindValue.floatBinding);
/*  438 */               break;
/*      */             case 5: 
/*  440 */               pStmtForSub.setDouble(i + 1, bindValue.doubleBinding);
/*      */               
/*  442 */               break;
/*      */             case 6: case 7: default: 
/*  444 */               pStmtForSub.setObject(i + 1, this.parameterBindings[i].value);
/*      */             }
/*      */             
/*      */           }
/*      */         }
/*      */       }
/*      */       
/*      */ 
/*  452 */       return pStmtForSub.asSql(quoteStreamsAndUnknowns);
/*      */     } finally {
/*  454 */       if (pStmtForSub != null) {
/*      */         try {
/*  456 */           pStmtForSub.close();
/*      */         }
/*      */         catch (SQLException sqlEx) {}
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected void checkClosed()
/*      */     throws SQLException
/*      */   {
/*  470 */     if (this.invalid) {
/*  471 */       throw this.invalidationException;
/*      */     }
/*      */     
/*  474 */     super.checkClosed();
/*      */   }
/*      */   
/*      */ 
/*      */   public void clearParameters()
/*      */     throws SQLException
/*      */   {
/*  481 */     checkClosed();
/*  482 */     clearParametersInternal(true);
/*      */   }
/*      */   
/*      */   private void clearParametersInternal(boolean clearServerParameters) throws SQLException
/*      */   {
/*  487 */     boolean hadLongData = false;
/*      */     
/*  489 */     if (this.parameterBindings != null) {
/*  490 */       for (int i = 0; i < this.parameterCount; i++) {
/*  491 */         if ((this.parameterBindings[i] != null) && (this.parameterBindings[i].isLongData))
/*      */         {
/*  493 */           hadLongData = true;
/*      */         }
/*      */         
/*  496 */         this.parameterBindings[i].reset();
/*      */       }
/*      */     }
/*      */     
/*  500 */     if ((clearServerParameters) && (hadLongData)) {
/*  501 */       serverResetStatement();
/*      */       
/*  503 */       this.detectedLongParameterSwitch = false;
/*      */     }
/*      */   }
/*      */   
/*  507 */   protected boolean isCached = false;
/*      */   
/*      */   protected void setClosed(boolean flag) {
/*  510 */     this.isClosed = flag;
/*      */   }
/*      */   
/*      */   public void close()
/*      */     throws SQLException
/*      */   {
/*  516 */     if (this.isCached) {
/*  517 */       this.isClosed = true;
/*  518 */       this.connection.recachePreparedStatement(this);
/*  519 */       return;
/*      */     }
/*      */     
/*  522 */     realClose(true, true);
/*      */   }
/*      */   
/*      */   private void dumpCloseForTestcase() {
/*  526 */     StringBuffer buf = new StringBuffer();
/*  527 */     this.connection.generateConnectionCommentBlock(buf);
/*  528 */     buf.append("DEALLOCATE PREPARE debug_stmt_");
/*  529 */     buf.append(this.statementId);
/*  530 */     buf.append(";\n");
/*      */     
/*  532 */     this.connection.dumpTestcaseQuery(buf.toString());
/*      */   }
/*      */   
/*      */   private void dumpExecuteForTestcase() throws SQLException {
/*  536 */     StringBuffer buf = new StringBuffer();
/*      */     
/*  538 */     for (int i = 0; i < this.parameterCount; i++) {
/*  539 */       this.connection.generateConnectionCommentBlock(buf);
/*      */       
/*  541 */       buf.append("SET @debug_stmt_param");
/*  542 */       buf.append(this.statementId);
/*  543 */       buf.append("_");
/*  544 */       buf.append(i);
/*  545 */       buf.append("=");
/*      */       
/*  547 */       if (this.parameterBindings[i].isNull) {
/*  548 */         buf.append("NULL");
/*      */       } else {
/*  550 */         buf.append(this.parameterBindings[i].toString(true));
/*      */       }
/*      */       
/*  553 */       buf.append(";\n");
/*      */     }
/*      */     
/*  556 */     this.connection.generateConnectionCommentBlock(buf);
/*      */     
/*  558 */     buf.append("EXECUTE debug_stmt_");
/*  559 */     buf.append(this.statementId);
/*      */     
/*  561 */     if (this.parameterCount > 0) {
/*  562 */       buf.append(" USING ");
/*  563 */       for (int i = 0; i < this.parameterCount; i++) {
/*  564 */         if (i > 0) {
/*  565 */           buf.append(", ");
/*      */         }
/*      */         
/*  568 */         buf.append("@debug_stmt_param");
/*  569 */         buf.append(this.statementId);
/*  570 */         buf.append("_");
/*  571 */         buf.append(i);
/*      */       }
/*      */     }
/*      */     
/*      */ 
/*  576 */     buf.append(";\n");
/*      */     
/*  578 */     this.connection.dumpTestcaseQuery(buf.toString());
/*      */   }
/*      */   
/*      */   private void dumpPrepareForTestcase() throws SQLException
/*      */   {
/*  583 */     StringBuffer buf = new StringBuffer(this.originalSql.length() + 64);
/*      */     
/*  585 */     this.connection.generateConnectionCommentBlock(buf);
/*      */     
/*  587 */     buf.append("PREPARE debug_stmt_");
/*  588 */     buf.append(this.statementId);
/*  589 */     buf.append(" FROM \"");
/*  590 */     buf.append(this.originalSql);
/*  591 */     buf.append("\";\n");
/*      */     
/*  593 */     this.connection.dumpTestcaseQuery(buf.toString());
/*      */   }
/*      */   
/*      */ 
/*      */   public synchronized int[] executeBatchSerially()
/*      */     throws SQLException
/*      */   {
/*  600 */     if (this.connection.isReadOnly()) {
/*  601 */       throw SQLError.createSQLException(Messages.getString("ServerPreparedStatement.2") + Messages.getString("ServerPreparedStatement.3"), "S1009");
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*  607 */     checkClosed();
/*      */     
/*  609 */     synchronized (this.connection.getMutex()) {
/*  610 */       clearWarnings();
/*      */       
/*      */ 
/*      */ 
/*  614 */       BindValue[] oldBindValues = this.parameterBindings;
/*      */       try
/*      */       {
/*  617 */         int[] updateCounts = null;
/*      */         
/*  619 */         if (this.batchedArgs != null) {
/*  620 */           nbrCommands = this.batchedArgs.size();
/*  621 */           updateCounts = new int[nbrCommands];
/*      */           
/*  623 */           if (this.retrieveGeneratedKeys) {
/*  624 */             this.batchedGeneratedKeys = new ArrayList(nbrCommands);
/*      */           }
/*      */           
/*  627 */           for (int i = 0; i < nbrCommands; i++) {
/*  628 */             updateCounts[i] = -3;
/*      */           }
/*      */           
/*  631 */           SQLException sqlEx = null;
/*      */           
/*  633 */           int commandIndex = 0;
/*      */           
/*  635 */           BindValue[] previousBindValuesForBatch = null;
/*      */           
/*  637 */           for (commandIndex = 0; commandIndex < nbrCommands; commandIndex++) {
/*  638 */             Object arg = this.batchedArgs.get(commandIndex);
/*      */             
/*  640 */             if ((arg instanceof String)) {
/*  641 */               updateCounts[commandIndex] = executeUpdate((String)arg);
/*      */             } else {
/*  643 */               this.parameterBindings = ((BatchedBindValues)arg).batchedParameterValues;
/*      */               
/*      */ 
/*      */ 
/*      */ 
/*      */               try
/*      */               {
/*  650 */                 if (previousBindValuesForBatch != null) {
/*  651 */                   for (int j = 0; j < this.parameterBindings.length; j++) {
/*  652 */                     if (this.parameterBindings[j].bufferType != previousBindValuesForBatch[j].bufferType) {
/*  653 */                       this.sendTypesToServer = true;
/*      */                       
/*  655 */                       break;
/*      */                     }
/*      */                   }
/*      */                 }
/*      */                 try
/*      */                 {
/*  661 */                   updateCounts[commandIndex] = executeUpdate(false, true);
/*      */                 } finally {
/*  663 */                   previousBindValuesForBatch = this.parameterBindings;
/*      */                 }
/*      */                 
/*  666 */                 if (this.retrieveGeneratedKeys) {
/*  667 */                   java.sql.ResultSet rs = null;
/*      */                   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */                   try
/*      */                   {
/*  679 */                     rs = getGeneratedKeysInternal();
/*      */                     
/*  681 */                     while (rs.next()) {
/*  682 */                       this.batchedGeneratedKeys.add(new byte[][] { rs.getBytes(1) });
/*      */                     }
/*      */                   }
/*      */                   finally
/*      */                   {
/*  687 */                     if (rs != null) {
/*  688 */                       rs.close();
/*      */                     }
/*      */                   }
/*      */                 }
/*      */               } catch (SQLException ex) {
/*  693 */                 updateCounts[commandIndex] = -3;
/*      */                 
/*  695 */                 if (this.continueBatchOnError) {
/*  696 */                   sqlEx = ex;
/*      */                 } else {
/*  698 */                   int[] newUpdateCounts = new int[commandIndex];
/*  699 */                   System.arraycopy(updateCounts, 0, newUpdateCounts, 0, commandIndex);
/*      */                   
/*      */ 
/*  702 */                   throw new BatchUpdateException(ex.getMessage(), ex.getSQLState(), ex.getErrorCode(), newUpdateCounts);
/*      */                 }
/*      */               }
/*      */             }
/*      */           }
/*      */           
/*      */ 
/*      */ 
/*  710 */           if (sqlEx != null) {
/*  711 */             throw new BatchUpdateException(sqlEx.getMessage(), sqlEx.getSQLState(), sqlEx.getErrorCode(), updateCounts);
/*      */           }
/*      */         }
/*      */         
/*      */ 
/*      */ 
/*  717 */         int nbrCommands = updateCounts != null ? updateCounts : new int[0];jsr 16;return nbrCommands;
/*      */       } finally {
/*  719 */         jsr 6; } localObject4 = returnAddress;this.parameterBindings = oldBindValues;
/*  720 */       this.sendTypesToServer = true;
/*      */       
/*  722 */       clearBatch();ret;
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
/*      */   protected ResultSet executeInternal(int maxRowsToRetrieve, Buffer sendPacket, boolean createStreamingResultSet, boolean queryIsSelectOnly, boolean unpackFields, Field[] metadataFromCache, boolean isBatch)
/*      */     throws SQLException
/*      */   {
/*  736 */     this.numberOfExecutions += 1;
/*      */     
/*      */     try
/*      */     {
/*  740 */       return serverExecute(maxRowsToRetrieve, createStreamingResultSet, unpackFields, metadataFromCache);
/*      */     }
/*      */     catch (SQLException sqlEx)
/*      */     {
/*  744 */       if (this.connection.getEnablePacketDebug()) {
/*  745 */         this.connection.getIO().dumpPacketRingBuffer();
/*      */       }
/*      */       
/*  748 */       if (this.connection.getDumpQueriesOnException()) {
/*  749 */         String extractedSql = toString();
/*  750 */         StringBuffer messageBuf = new StringBuffer(extractedSql.length() + 32);
/*      */         
/*  752 */         messageBuf.append("\n\nQuery being executed when exception was thrown:\n\n");
/*      */         
/*  754 */         messageBuf.append(extractedSql);
/*      */         
/*  756 */         sqlEx = Connection.appendMessageToException(sqlEx, messageBuf.toString());
/*      */       }
/*      */       
/*      */ 
/*  760 */       throw sqlEx;
/*      */     } catch (Exception ex) {
/*  762 */       if (this.connection.getEnablePacketDebug()) {
/*  763 */         this.connection.getIO().dumpPacketRingBuffer();
/*      */       }
/*      */       
/*  766 */       SQLException sqlEx = SQLError.createSQLException(ex.toString(), "S1000");
/*      */       
/*      */ 
/*  769 */       if (this.connection.getDumpQueriesOnException()) {
/*  770 */         String extractedSql = toString();
/*  771 */         StringBuffer messageBuf = new StringBuffer(extractedSql.length() + 32);
/*      */         
/*  773 */         messageBuf.append("\n\nQuery being executed when exception was thrown:\n\n");
/*      */         
/*  775 */         messageBuf.append(extractedSql);
/*      */         
/*  777 */         sqlEx = Connection.appendMessageToException(sqlEx, messageBuf.toString());
/*      */       }
/*      */       
/*      */ 
/*  781 */       throw sqlEx;
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */   protected Buffer fillSendPacket()
/*      */     throws SQLException
/*      */   {
/*  789 */     return null;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected Buffer fillSendPacket(byte[][] batchedParameterStrings, InputStream[] batchedParameterStreams, boolean[] batchedIsStream, int[] batchedStreamLengths)
/*      */     throws SQLException
/*      */   {
/*  799 */     return null;
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
/*      */   private BindValue getBinding(int parameterIndex, boolean forLongData)
/*      */     throws SQLException
/*      */   {
/*  813 */     checkClosed();
/*      */     
/*  815 */     if (this.parameterBindings.length == 0) {
/*  816 */       throw SQLError.createSQLException(Messages.getString("ServerPreparedStatement.8"), "S1009");
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*  821 */     parameterIndex--;
/*      */     
/*  823 */     if ((parameterIndex < 0) || (parameterIndex >= this.parameterBindings.length))
/*      */     {
/*  825 */       throw SQLError.createSQLException(Messages.getString("ServerPreparedStatement.9") + (parameterIndex + 1) + Messages.getString("ServerPreparedStatement.10") + this.parameterBindings.length, "S1009");
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  833 */     if (this.parameterBindings[parameterIndex] == null) {
/*  834 */       this.parameterBindings[parameterIndex] = new BindValue();
/*      */     }
/*  836 */     else if ((this.parameterBindings[parameterIndex].isLongData) && (!forLongData))
/*      */     {
/*  838 */       this.detectedLongParameterSwitch = true;
/*      */     }
/*      */     
/*      */ 
/*  842 */     this.parameterBindings[parameterIndex].isSet = true;
/*  843 */     this.parameterBindings[parameterIndex].boundBeforeExecutionNum = this.numberOfExecutions;
/*      */     
/*  845 */     return this.parameterBindings[parameterIndex];
/*      */   }
/*      */   
/*      */ 
/*      */   byte[] getBytes(int parameterIndex)
/*      */     throws SQLException
/*      */   {
/*  852 */     BindValue bindValue = getBinding(parameterIndex, false);
/*      */     
/*  854 */     if (bindValue.isNull)
/*  855 */       return null;
/*  856 */     if (bindValue.isLongData) {
/*  857 */       throw new NotImplemented();
/*      */     }
/*  859 */     if (this.outByteBuffer == null) {
/*  860 */       this.outByteBuffer = new Buffer(this.connection.getNetBufferLength());
/*      */     }
/*      */     
/*      */ 
/*  864 */     this.outByteBuffer.clear();
/*      */     
/*  866 */     int originalPosition = this.outByteBuffer.getPosition();
/*      */     
/*  868 */     storeBinding(this.outByteBuffer, bindValue, this.connection.getIO());
/*      */     
/*  870 */     int newPosition = this.outByteBuffer.getPosition();
/*      */     
/*  872 */     int length = newPosition - originalPosition;
/*      */     
/*  874 */     byte[] valueAsBytes = new byte[length];
/*      */     
/*  876 */     System.arraycopy(this.outByteBuffer.getByteBuffer(), originalPosition, valueAsBytes, 0, length);
/*      */     
/*      */ 
/*  879 */     return valueAsBytes;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public java.sql.ResultSetMetaData getMetaData()
/*      */     throws SQLException
/*      */   {
/*  887 */     checkClosed();
/*      */     
/*  889 */     if (this.resultFields == null) {
/*  890 */       return null;
/*      */     }
/*      */     
/*  893 */     return new ResultSetMetaData(this.resultFields, this.connection.getUseOldAliasMetadataBehavior());
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public ParameterMetaData getParameterMetaData()
/*      */     throws SQLException
/*      */   {
/*  901 */     checkClosed();
/*      */     
/*  903 */     if (this.parameterMetaData == null) {
/*  904 */       this.parameterMetaData = new MysqlParameterMetadata(this.parameterFields, this.parameterCount);
/*      */     }
/*      */     
/*      */ 
/*  908 */     return this.parameterMetaData;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   boolean isNull(int paramIndex)
/*      */   {
/*  915 */     throw new IllegalArgumentException(Messages.getString("ServerPreparedStatement.7"));
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
/*      */   protected void realClose(boolean calledExplicitly, boolean closeOpenResults)
/*      */     throws SQLException
/*      */   {
/*  930 */     if (this.isClosed) {
/*  931 */       return;
/*      */     }
/*      */     
/*  934 */     if (this.connection != null) {
/*  935 */       if (this.connection.getAutoGenerateTestcaseScript()) {
/*  936 */         dumpCloseForTestcase();
/*      */       }
/*      */       
/*  939 */       synchronized (this.connection.getMutex())
/*      */       {
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  952 */         SQLException exceptionDuringClose = null;
/*      */         
/*      */ 
/*  955 */         if (calledExplicitly) {
/*      */           try
/*      */           {
/*  958 */             MysqlIO mysql = this.connection.getIO();
/*      */             
/*  960 */             Buffer packet = mysql.getSharedSendPacket();
/*      */             
/*  962 */             packet.writeByte((byte)25);
/*  963 */             packet.writeLong(this.serverStatementId);
/*      */             
/*  965 */             mysql.sendCommand(25, null, packet, true, null);
/*      */           }
/*      */           catch (SQLException sqlEx) {
/*  968 */             exceptionDuringClose = sqlEx;
/*      */           }
/*      */         }
/*      */         
/*      */ 
/*  973 */         super.realClose(calledExplicitly, closeOpenResults);
/*      */         
/*  975 */         clearParametersInternal(false);
/*  976 */         this.parameterBindings = null;
/*      */         
/*  978 */         this.parameterFields = null;
/*  979 */         this.resultFields = null;
/*      */         
/*  981 */         if (exceptionDuringClose != null) {
/*  982 */           throw exceptionDuringClose;
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
/*      */   protected void rePrepare()
/*      */     throws SQLException
/*      */   {
/*  996 */     this.invalidationException = null;
/*      */     try
/*      */     {
/*  999 */       serverPrepare(this.originalSql);
/*      */     }
/*      */     catch (SQLException sqlEx) {
/* 1002 */       this.invalidationException = sqlEx;
/*      */     } catch (Exception ex) {
/* 1004 */       this.invalidationException = SQLError.createSQLException(ex.toString(), "S1000");
/*      */     }
/*      */     
/*      */ 
/* 1008 */     if (this.invalidationException != null) {
/* 1009 */       this.invalid = true;
/*      */       
/* 1011 */       this.parameterBindings = null;
/*      */       
/* 1013 */       this.parameterFields = null;
/* 1014 */       this.resultFields = null;
/*      */       
/* 1016 */       if (this.results != null) {
/*      */         try {
/* 1018 */           this.results.close();
/*      */         }
/*      */         catch (Exception ex) {}
/*      */       }
/*      */       
/*      */ 
/* 1024 */       if (this.connection != null) {
/* 1025 */         if (this.maxRowsChanged) {
/* 1026 */           this.connection.unsetMaxRows(this);
/*      */         }
/*      */         
/* 1029 */         if (!this.connection.getDontTrackOpenResources()) {
/* 1030 */           this.connection.unregisterStatement(this);
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
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   private ResultSet serverExecute(int maxRowsToRetrieve, boolean createStreamingResultSet, boolean unpackFields, Field[] metadataFromCache)
/*      */     throws SQLException
/*      */   {
/* 1072 */     synchronized (this.connection.getMutex()) {
/* 1073 */       if (this.detectedLongParameterSwitch)
/*      */       {
/* 1075 */         boolean firstFound = false;
/* 1076 */         long boundTimeToCheck = 0L;
/*      */         
/* 1078 */         for (int i = 0; i < this.parameterCount - 1; i++) {
/* 1079 */           if (this.parameterBindings[i].isLongData) {
/* 1080 */             if ((firstFound) && (boundTimeToCheck != this.parameterBindings[i].boundBeforeExecutionNum))
/*      */             {
/* 1082 */               throw SQLError.createSQLException(Messages.getString("ServerPreparedStatement.11") + Messages.getString("ServerPreparedStatement.12"), "S1C00");
/*      */             }
/*      */             
/*      */ 
/*      */ 
/* 1087 */             firstFound = true;
/* 1088 */             boundTimeToCheck = this.parameterBindings[i].boundBeforeExecutionNum;
/*      */           }
/*      */         }
/*      */         
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 1096 */         serverResetStatement();
/*      */       }
/*      */       
/*      */ 
/*      */ 
/* 1101 */       for (int i = 0; i < this.parameterCount; i++) {
/* 1102 */         if (!this.parameterBindings[i].isSet) {
/* 1103 */           throw SQLError.createSQLException(Messages.getString("ServerPreparedStatement.13") + (i + 1) + Messages.getString("ServerPreparedStatement.14"), "S1009");
/*      */         }
/*      */       }
/*      */       
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 1113 */       for (int i = 0; i < this.parameterCount; i++) {
/* 1114 */         if (this.parameterBindings[i].isLongData) {
/* 1115 */           serverLongData(i, this.parameterBindings[i]);
/*      */         }
/*      */       }
/*      */       
/* 1119 */       if (this.connection.getAutoGenerateTestcaseScript()) {
/* 1120 */         dumpExecuteForTestcase();
/*      */       }
/*      */       
/*      */ 
/*      */ 
/*      */ 
/* 1126 */       MysqlIO mysql = this.connection.getIO();
/*      */       
/* 1128 */       Buffer packet = mysql.getSharedSendPacket();
/*      */       
/* 1130 */       packet.clear();
/* 1131 */       packet.writeByte((byte)23);
/* 1132 */       packet.writeLong(this.serverStatementId);
/*      */       
/* 1134 */       boolean usingCursor = false;
/*      */       
/* 1136 */       if (this.connection.versionMeetsMinimum(4, 1, 2))
/*      */       {
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 1143 */         if ((this.resultFields != null) && (this.connection.isCursorFetchEnabled()) && (getResultSetType() == 1003) && (getResultSetConcurrency() == 1007) && (getFetchSize() > 0))
/*      */         {
/*      */ 
/*      */ 
/*      */ 
/* 1148 */           packet.writeByte((byte)1);
/* 1149 */           usingCursor = true;
/*      */         } else {
/* 1151 */           packet.writeByte((byte)0);
/*      */         }
/*      */         
/* 1154 */         packet.writeLong(1L);
/*      */       }
/*      */       
/*      */ 
/*      */ 
/* 1159 */       int nullCount = (this.parameterCount + 7) / 8;
/*      */       
/*      */ 
/*      */ 
/*      */ 
/* 1164 */       int nullBitsPosition = packet.getPosition();
/*      */       
/* 1166 */       for (int i = 0; i < nullCount; i++) {
/* 1167 */         packet.writeByte((byte)0);
/*      */       }
/*      */       
/* 1170 */       byte[] nullBitsBuffer = new byte[nullCount];
/*      */       
/*      */ 
/* 1173 */       packet.writeByte((byte)(this.sendTypesToServer ? 1 : 0));
/*      */       
/* 1175 */       if (this.sendTypesToServer)
/*      */       {
/*      */ 
/*      */ 
/*      */ 
/* 1180 */         for (int i = 0; i < this.parameterCount; i++) {
/* 1181 */           packet.writeInt(this.parameterBindings[i].bufferType);
/*      */         }
/*      */       }
/*      */       
/*      */ 
/*      */ 
/*      */ 
/* 1188 */       for (int i = 0; i < this.parameterCount; i++) {
/* 1189 */         if (!this.parameterBindings[i].isLongData) {
/* 1190 */           if (!this.parameterBindings[i].isNull) {
/* 1191 */             storeBinding(packet, this.parameterBindings[i], mysql);
/*      */           } else {
/* 1193 */             int tmp545_544 = (i / 8); byte[] tmp545_538 = nullBitsBuffer;tmp545_538[tmp545_544] = ((byte)(tmp545_538[tmp545_544] | 1 << (i & 0x7)));
/*      */           }
/*      */         }
/*      */       }
/*      */       
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 1202 */       int endPosition = packet.getPosition();
/* 1203 */       packet.setPosition(nullBitsPosition);
/* 1204 */       packet.writeBytesNoNull(nullBitsBuffer);
/* 1205 */       packet.setPosition(endPosition);
/*      */       
/* 1207 */       long begin = 0L;
/*      */       
/* 1209 */       boolean logSlowQueries = this.connection.getLogSlowQueries();
/* 1210 */       boolean gatherPerformanceMetrics = this.connection.getGatherPerformanceMetrics();
/*      */       
/*      */ 
/* 1213 */       if ((this.profileSQL) || (logSlowQueries) || (gatherPerformanceMetrics)) {
/* 1214 */         begin = mysql.getCurrentTimeNanosOrMillis();
/*      */       }
/*      */       
/* 1217 */       synchronized (this.cancelTimeoutMutex) {
/* 1218 */         this.wasCancelled = false;
/*      */       }
/*      */       
/* 1221 */       Statement.CancelTask timeoutTask = null;
/*      */       try
/*      */       {
/* 1224 */         if ((this.connection.getEnableQueryTimeouts()) && (this.timeoutInMillis != 0) && (this.connection.versionMeetsMinimum(5, 0, 0)))
/*      */         {
/*      */ 
/* 1227 */           timeoutTask = new Statement.CancelTask(this);
/* 1228 */           Connection.getCancelTimer().schedule(timeoutTask, this.timeoutInMillis);
/*      */         }
/*      */         
/*      */ 
/* 1232 */         Buffer resultPacket = mysql.sendCommand(23, null, packet, false, null);
/*      */         
/*      */ 
/* 1235 */         long queryEndTime = 0L;
/*      */         
/* 1237 */         if ((logSlowQueries) || (gatherPerformanceMetrics) || (this.profileSQL)) {
/* 1238 */           queryEndTime = mysql.getCurrentTimeNanosOrMillis();
/*      */         }
/*      */         
/* 1241 */         if (timeoutTask != null) {
/* 1242 */           timeoutTask.cancel();
/*      */           
/* 1244 */           if (timeoutTask.caughtWhileCancelling != null) {
/* 1245 */             throw timeoutTask.caughtWhileCancelling;
/*      */           }
/*      */           
/* 1248 */           timeoutTask = null;
/*      */         }
/*      */         
/* 1251 */         synchronized (this.cancelTimeoutMutex) {
/* 1252 */           if (this.wasCancelled) {
/* 1253 */             this.wasCancelled = false;
/* 1254 */             throw new MySQLTimeoutException();
/*      */           }
/*      */         }
/*      */         
/* 1258 */         boolean queryWasSlow = false;
/*      */         
/* 1260 */         if ((logSlowQueries) || (gatherPerformanceMetrics)) {
/* 1261 */           long elapsedTime = queryEndTime - begin;
/*      */           
/* 1263 */           if ((logSlowQueries) && (elapsedTime >= mysql.getSlowQueryThreshold()))
/*      */           {
/* 1265 */             queryWasSlow = true;
/*      */             
/* 1267 */             StringBuffer mesgBuf = new StringBuffer(48 + this.originalSql.length());
/*      */             
/* 1269 */             mesgBuf.append(Messages.getString("ServerPreparedStatement.15"));
/*      */             
/* 1271 */             mesgBuf.append(mysql.getSlowQueryThreshold());
/* 1272 */             mesgBuf.append(Messages.getString("ServerPreparedStatement.15a"));
/*      */             
/* 1274 */             mesgBuf.append(elapsedTime);
/* 1275 */             mesgBuf.append(Messages.getString("ServerPreparedStatement.16"));
/*      */             
/*      */ 
/* 1278 */             mesgBuf.append("as prepared: ");
/* 1279 */             mesgBuf.append(this.originalSql);
/* 1280 */             mesgBuf.append("\n\n with parameters bound:\n\n");
/* 1281 */             mesgBuf.append(asSql(true));
/*      */             
/* 1283 */             this.eventSink.consumeEvent(new ProfilerEvent((byte)6, "", this.currentCatalog, this.connection.getId(), getId(), 0, System.currentTimeMillis(), elapsedTime, mysql.getQueryTimingUnits(), null, new Throwable(), mesgBuf.toString()));
/*      */           }
/*      */           
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 1293 */           if (gatherPerformanceMetrics) {
/* 1294 */             this.connection.registerQueryExecutionTime(elapsedTime);
/*      */           }
/*      */         }
/*      */         
/* 1298 */         this.connection.incrementNumberOfPreparedExecutes();
/*      */         
/* 1300 */         if (this.profileSQL) {
/* 1301 */           this.eventSink = ProfileEventSink.getInstance(this.connection);
/*      */           
/*      */ 
/* 1304 */           this.eventSink.consumeEvent(new ProfilerEvent((byte)4, "", this.currentCatalog, this.connectionId, this.statementId, -1, System.currentTimeMillis(), (int)(mysql.getCurrentTimeNanosOrMillis() - begin), mysql.getQueryTimingUnits(), null, new Throwable(), truncateQueryToLog(asSql(true))));
/*      */         }
/*      */         
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 1314 */         ResultSet rs = mysql.readAllResults(this, maxRowsToRetrieve, this.resultSetType, this.resultSetConcurrency, createStreamingResultSet, this.currentCatalog, resultPacket, true, this.fieldCount, unpackFields, metadataFromCache);
/*      */         
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 1320 */         if (this.profileSQL) {
/* 1321 */           long fetchEndTime = mysql.getCurrentTimeNanosOrMillis();
/*      */           
/* 1323 */           this.eventSink.consumeEvent(new ProfilerEvent((byte)5, "", this.currentCatalog, this.connection.getId(), getId(), rs.resultId, System.currentTimeMillis(), fetchEndTime - queryEndTime, mysql.getQueryTimingUnits(), null, new Throwable(), null));
/*      */         }
/*      */         
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 1332 */         if ((queryWasSlow) && (this.connection.getExplainSlowQueries())) {
/* 1333 */           queryAsString = asSql(true);
/*      */           
/* 1335 */           mysql.explainSlowQuery(queryAsString.getBytes(), queryAsString);
/*      */         }
/*      */         
/*      */ 
/* 1339 */         if ((!createStreamingResultSet) && (this.serverNeedsResetBeforeEachExecution))
/*      */         {
/* 1341 */           serverResetStatement();
/*      */         }
/*      */         
/*      */ 
/* 1345 */         this.sendTypesToServer = false;
/* 1346 */         this.results = rs;
/*      */         
/* 1348 */         if (mysql.hadWarnings()) {
/* 1349 */           mysql.scanForAndThrowDataTruncation();
/*      */         }
/*      */         
/* 1352 */         String queryAsString = rs;
/*      */         
/* 1354 */         if (timeoutTask != null)
/* 1355 */           timeoutTask.cancel(); return queryAsString;
/*      */       }
/*      */       finally
/*      */       {
/* 1354 */         if (timeoutTask != null) {
/* 1355 */           timeoutTask.cancel();
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
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   private void serverLongData(int parameterIndex, BindValue longData)
/*      */     throws SQLException
/*      */   {
/* 1390 */     synchronized (this.connection.getMutex()) {
/* 1391 */       MysqlIO mysql = this.connection.getIO();
/*      */       
/* 1393 */       Buffer packet = mysql.getSharedSendPacket();
/*      */       
/* 1395 */       Object value = longData.value;
/*      */       
/* 1397 */       if ((value instanceof byte[])) {
/* 1398 */         packet.clear();
/* 1399 */         packet.writeByte((byte)24);
/* 1400 */         packet.writeLong(this.serverStatementId);
/* 1401 */         packet.writeInt(parameterIndex);
/*      */         
/* 1403 */         packet.writeBytesNoNull((byte[])longData.value);
/*      */         
/* 1405 */         mysql.sendCommand(24, null, packet, true, null);
/*      */       }
/* 1407 */       else if ((value instanceof InputStream)) {
/* 1408 */         storeStream(mysql, parameterIndex, packet, (InputStream)value);
/* 1409 */       } else if ((value instanceof Blob)) {
/* 1410 */         storeStream(mysql, parameterIndex, packet, ((Blob)value).getBinaryStream());
/*      */       }
/* 1412 */       else if ((value instanceof Reader)) {
/* 1413 */         storeReader(mysql, parameterIndex, packet, (Reader)value);
/*      */       } else {
/* 1415 */         throw SQLError.createSQLException(Messages.getString("ServerPreparedStatement.18") + value.getClass().getName() + "'", "S1009");
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */   private void serverPrepare(String sql)
/*      */     throws SQLException
/*      */   {
/* 1424 */     synchronized (this.connection.getMutex()) {
/* 1425 */       MysqlIO mysql = this.connection.getIO();
/*      */       
/* 1427 */       if (this.connection.getAutoGenerateTestcaseScript()) {
/* 1428 */         dumpPrepareForTestcase();
/*      */       }
/*      */       try
/*      */       {
/* 1432 */         long begin = 0L;
/*      */         
/* 1434 */         if (StringUtils.startsWithIgnoreCaseAndWs(sql, "LOAD DATA")) {
/* 1435 */           this.isLoadDataQuery = true;
/*      */         } else {
/* 1437 */           this.isLoadDataQuery = false;
/*      */         }
/*      */         
/* 1440 */         if (this.connection.getProfileSql()) {
/* 1441 */           begin = mysql.getCurrentTimeNanosOrMillis();
/*      */         }
/*      */         
/* 1444 */         String characterEncoding = null;
/* 1445 */         String connectionEncoding = this.connection.getEncoding();
/*      */         
/* 1447 */         if ((!this.isLoadDataQuery) && (this.connection.getUseUnicode()) && (connectionEncoding != null))
/*      */         {
/* 1449 */           characterEncoding = connectionEncoding;
/*      */         }
/*      */         
/* 1452 */         Buffer prepareResultPacket = mysql.sendCommand(22, sql, null, false, characterEncoding);
/*      */         
/*      */ 
/*      */ 
/* 1456 */         if (this.connection.versionMeetsMinimum(4, 1, 1))
/*      */         {
/*      */ 
/*      */ 
/*      */ 
/* 1461 */           prepareResultPacket.setPosition(1);
/*      */         }
/*      */         else
/*      */         {
/* 1465 */           prepareResultPacket.setPosition(0);
/*      */         }
/*      */         
/* 1468 */         this.serverStatementId = prepareResultPacket.readLong();
/* 1469 */         this.fieldCount = prepareResultPacket.readInt();
/* 1470 */         this.parameterCount = prepareResultPacket.readInt();
/* 1471 */         this.parameterBindings = new BindValue[this.parameterCount];
/*      */         
/* 1473 */         for (int i = 0; i < this.parameterCount; i++) {
/* 1474 */           this.parameterBindings[i] = new BindValue();
/*      */         }
/*      */         
/* 1477 */         this.connection.incrementNumberOfPrepares();
/*      */         
/* 1479 */         if (this.profileSQL) {
/* 1480 */           this.eventSink.consumeEvent(new ProfilerEvent((byte)2, "", this.currentCatalog, this.connectionId, this.statementId, -1, System.currentTimeMillis(), mysql.getCurrentTimeNanosOrMillis() - begin, mysql.getQueryTimingUnits(), null, new Throwable(), truncateQueryToLog(sql)));
/*      */         }
/*      */         
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 1490 */         if ((this.parameterCount > 0) && 
/* 1491 */           (this.connection.versionMeetsMinimum(4, 1, 2)) && (!mysql.isVersion(5, 0, 0)))
/*      */         {
/* 1493 */           this.parameterFields = new Field[this.parameterCount];
/*      */           
/* 1495 */           Buffer metaDataPacket = mysql.readPacket();
/*      */           
/* 1497 */           int i = 0;
/*      */           
/*      */ 
/* 1500 */           while ((!metaDataPacket.isLastDataPacket()) && (i < this.parameterCount)) {
/* 1501 */             this.parameterFields[(i++)] = mysql.unpackField(metaDataPacket, false);
/*      */             
/* 1503 */             metaDataPacket = mysql.readPacket();
/*      */           }
/*      */         }
/*      */         
/*      */ 
/* 1508 */         if (this.fieldCount > 0) {
/* 1509 */           this.resultFields = new Field[this.fieldCount];
/*      */           
/* 1511 */           Buffer fieldPacket = mysql.readPacket();
/*      */           
/* 1513 */           int i = 0;
/*      */           
/*      */ 
/*      */ 
/* 1517 */           while ((!fieldPacket.isLastDataPacket()) && (i < this.fieldCount)) {
/* 1518 */             this.resultFields[(i++)] = mysql.unpackField(fieldPacket, false);
/*      */             
/* 1520 */             fieldPacket = mysql.readPacket();
/*      */           }
/*      */         }
/*      */       } catch (SQLException sqlEx) {
/* 1524 */         if (this.connection.getDumpQueriesOnException()) {
/* 1525 */           StringBuffer messageBuf = new StringBuffer(this.originalSql.length() + 32);
/*      */           
/* 1527 */           messageBuf.append("\n\nQuery being prepared when exception was thrown:\n\n");
/*      */           
/* 1529 */           messageBuf.append(this.originalSql);
/*      */           
/* 1531 */           sqlEx = Connection.appendMessageToException(sqlEx, messageBuf.toString());
/*      */         }
/*      */         
/*      */ 
/* 1535 */         throw sqlEx;
/*      */ 
/*      */       }
/*      */       finally
/*      */       {
/* 1540 */         this.connection.getIO().clearInputStream();
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */   private String truncateQueryToLog(String sql) {
/* 1546 */     String query = null;
/*      */     
/* 1548 */     if (sql.length() > this.connection.getMaxQuerySizeToLog()) {
/* 1549 */       StringBuffer queryBuf = new StringBuffer(this.connection.getMaxQuerySizeToLog() + 12);
/*      */       
/* 1551 */       queryBuf.append(sql.substring(0, this.connection.getMaxQuerySizeToLog()));
/* 1552 */       queryBuf.append(Messages.getString("MysqlIO.25"));
/*      */       
/* 1554 */       query = queryBuf.toString();
/*      */     } else {
/* 1556 */       query = sql;
/*      */     }
/*      */     
/* 1559 */     return query;
/*      */   }
/*      */   
/*      */   private void serverResetStatement() throws SQLException {
/* 1563 */     synchronized (this.connection.getMutex())
/*      */     {
/* 1565 */       MysqlIO mysql = this.connection.getIO();
/*      */       
/* 1567 */       Buffer packet = mysql.getSharedSendPacket();
/*      */       
/* 1569 */       packet.clear();
/* 1570 */       packet.writeByte((byte)26);
/* 1571 */       packet.writeLong(this.serverStatementId);
/*      */       try
/*      */       {
/* 1574 */         mysql.sendCommand(26, null, packet, !this.connection.versionMeetsMinimum(4, 1, 2), null);
/*      */       }
/*      */       catch (SQLException sqlEx) {
/* 1577 */         throw sqlEx;
/*      */       } catch (Exception ex) {
/* 1579 */         throw SQLError.createSQLException(ex.toString(), "S1000");
/*      */       }
/*      */       finally {
/* 1582 */         mysql.clearInputStream();
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */   public void setArray(int i, Array x)
/*      */     throws SQLException
/*      */   {
/* 1591 */     throw new NotImplemented();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public void setAsciiStream(int parameterIndex, InputStream x, int length)
/*      */     throws SQLException
/*      */   {
/* 1600 */     checkClosed();
/*      */     
/* 1602 */     if (x == null) {
/* 1603 */       setNull(parameterIndex, -2);
/*      */     } else {
/* 1605 */       BindValue binding = getBinding(parameterIndex, true);
/* 1606 */       setType(binding, 252);
/*      */       
/* 1608 */       binding.value = x;
/* 1609 */       binding.isNull = false;
/* 1610 */       binding.isLongData = true;
/*      */       
/* 1612 */       if (this.connection.getUseStreamLengthsInPrepStmts()) {
/* 1613 */         binding.bindLength = length;
/*      */       } else {
/* 1615 */         binding.bindLength = -1L;
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public void setBigDecimal(int parameterIndex, BigDecimal x)
/*      */     throws SQLException
/*      */   {
/* 1625 */     checkClosed();
/*      */     
/* 1627 */     if (x == null) {
/* 1628 */       setNull(parameterIndex, 3);
/*      */     }
/*      */     else {
/* 1631 */       BindValue binding = getBinding(parameterIndex, false);
/*      */       
/* 1633 */       if (this.connection.versionMeetsMinimum(5, 0, 3)) {
/* 1634 */         setType(binding, 246);
/*      */       } else {
/* 1636 */         setType(binding, this.stringTypeCode);
/*      */       }
/*      */       
/* 1639 */       binding.value = StringUtils.fixDecimalExponent(StringUtils.consistentToString(x));
/*      */       
/* 1641 */       binding.isNull = false;
/* 1642 */       binding.isLongData = false;
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public void setBinaryStream(int parameterIndex, InputStream x, int length)
/*      */     throws SQLException
/*      */   {
/* 1652 */     checkClosed();
/*      */     
/* 1654 */     if (x == null) {
/* 1655 */       setNull(parameterIndex, -2);
/*      */     } else {
/* 1657 */       BindValue binding = getBinding(parameterIndex, true);
/* 1658 */       setType(binding, 252);
/*      */       
/* 1660 */       binding.value = x;
/* 1661 */       binding.isNull = false;
/* 1662 */       binding.isLongData = true;
/*      */       
/* 1664 */       if (this.connection.getUseStreamLengthsInPrepStmts()) {
/* 1665 */         binding.bindLength = length;
/*      */       } else {
/* 1667 */         binding.bindLength = -1L;
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */   public void setBlob(int parameterIndex, Blob x)
/*      */     throws SQLException
/*      */   {
/* 1676 */     checkClosed();
/*      */     
/* 1678 */     if (x == null) {
/* 1679 */       setNull(parameterIndex, -2);
/*      */     } else {
/* 1681 */       BindValue binding = getBinding(parameterIndex, true);
/* 1682 */       setType(binding, 252);
/*      */       
/* 1684 */       binding.value = x;
/* 1685 */       binding.isNull = false;
/* 1686 */       binding.isLongData = true;
/*      */       
/* 1688 */       if (this.connection.getUseStreamLengthsInPrepStmts()) {
/* 1689 */         binding.bindLength = x.length();
/*      */       } else {
/* 1691 */         binding.bindLength = -1L;
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */   public void setBoolean(int parameterIndex, boolean x)
/*      */     throws SQLException
/*      */   {
/* 1700 */     setByte(parameterIndex, (byte)(x ? 1 : 0));
/*      */   }
/*      */   
/*      */ 
/*      */   public void setByte(int parameterIndex, byte x)
/*      */     throws SQLException
/*      */   {
/* 1707 */     checkClosed();
/*      */     
/* 1709 */     BindValue binding = getBinding(parameterIndex, false);
/* 1710 */     setType(binding, 1);
/*      */     
/* 1712 */     binding.value = null;
/* 1713 */     binding.byteBinding = x;
/* 1714 */     binding.isNull = false;
/* 1715 */     binding.isLongData = false;
/*      */   }
/*      */   
/*      */ 
/*      */   public void setBytes(int parameterIndex, byte[] x)
/*      */     throws SQLException
/*      */   {
/* 1722 */     checkClosed();
/*      */     
/* 1724 */     if (x == null) {
/* 1725 */       setNull(parameterIndex, -2);
/*      */     } else {
/* 1727 */       BindValue binding = getBinding(parameterIndex, false);
/* 1728 */       setType(binding, 253);
/*      */       
/* 1730 */       binding.value = x;
/* 1731 */       binding.isNull = false;
/* 1732 */       binding.isLongData = false;
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public void setCharacterStream(int parameterIndex, Reader reader, int length)
/*      */     throws SQLException
/*      */   {
/* 1742 */     checkClosed();
/*      */     
/* 1744 */     if (reader == null) {
/* 1745 */       setNull(parameterIndex, -2);
/*      */     } else {
/* 1747 */       BindValue binding = getBinding(parameterIndex, true);
/* 1748 */       setType(binding, 252);
/*      */       
/* 1750 */       binding.value = reader;
/* 1751 */       binding.isNull = false;
/* 1752 */       binding.isLongData = true;
/*      */       
/* 1754 */       if (this.connection.getUseStreamLengthsInPrepStmts()) {
/* 1755 */         binding.bindLength = length;
/*      */       } else {
/* 1757 */         binding.bindLength = -1L;
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */   public void setClob(int parameterIndex, Clob x)
/*      */     throws SQLException
/*      */   {
/* 1766 */     checkClosed();
/*      */     
/* 1768 */     if (x == null) {
/* 1769 */       setNull(parameterIndex, -2);
/*      */     } else {
/* 1771 */       BindValue binding = getBinding(parameterIndex, true);
/* 1772 */       setType(binding, 252);
/*      */       
/* 1774 */       binding.value = x.getCharacterStream();
/* 1775 */       binding.isNull = false;
/* 1776 */       binding.isLongData = true;
/*      */       
/* 1778 */       if (this.connection.getUseStreamLengthsInPrepStmts()) {
/* 1779 */         binding.bindLength = x.length();
/*      */       } else {
/* 1781 */         binding.bindLength = -1L;
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
/*      */   public void setDate(int parameterIndex, java.sql.Date x)
/*      */     throws SQLException
/*      */   {
/* 1799 */     setDate(parameterIndex, x, null);
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
/*      */   public void setDate(int parameterIndex, java.sql.Date x, Calendar cal)
/*      */     throws SQLException
/*      */   {
/* 1818 */     if (x == null) {
/* 1819 */       setNull(parameterIndex, 91);
/*      */     } else {
/* 1821 */       BindValue binding = getBinding(parameterIndex, false);
/* 1822 */       setType(binding, 10);
/*      */       
/* 1824 */       binding.value = x;
/* 1825 */       binding.isNull = false;
/* 1826 */       binding.isLongData = false;
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */   public void setDouble(int parameterIndex, double x)
/*      */     throws SQLException
/*      */   {
/* 1834 */     checkClosed();
/*      */     
/* 1836 */     if ((!this.connection.getAllowNanAndInf()) && ((x == Double.POSITIVE_INFINITY) || (x == Double.NEGATIVE_INFINITY) || (Double.isNaN(x))))
/*      */     {
/*      */ 
/* 1839 */       throw SQLError.createSQLException("'" + x + "' is not a valid numeric or approximate numeric value", "S1009");
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/* 1845 */     BindValue binding = getBinding(parameterIndex, false);
/* 1846 */     setType(binding, 5);
/*      */     
/* 1848 */     binding.value = null;
/* 1849 */     binding.doubleBinding = x;
/* 1850 */     binding.isNull = false;
/* 1851 */     binding.isLongData = false;
/*      */   }
/*      */   
/*      */ 
/*      */   public void setFloat(int parameterIndex, float x)
/*      */     throws SQLException
/*      */   {
/* 1858 */     checkClosed();
/*      */     
/* 1860 */     BindValue binding = getBinding(parameterIndex, false);
/* 1861 */     setType(binding, 4);
/*      */     
/* 1863 */     binding.value = null;
/* 1864 */     binding.floatBinding = x;
/* 1865 */     binding.isNull = false;
/* 1866 */     binding.isLongData = false;
/*      */   }
/*      */   
/*      */ 
/*      */   public void setInt(int parameterIndex, int x)
/*      */     throws SQLException
/*      */   {
/* 1873 */     checkClosed();
/*      */     
/* 1875 */     BindValue binding = getBinding(parameterIndex, false);
/* 1876 */     setType(binding, 3);
/*      */     
/* 1878 */     binding.value = null;
/* 1879 */     binding.intBinding = x;
/* 1880 */     binding.isNull = false;
/* 1881 */     binding.isLongData = false;
/*      */   }
/*      */   
/*      */ 
/*      */   public void setLong(int parameterIndex, long x)
/*      */     throws SQLException
/*      */   {
/* 1888 */     checkClosed();
/*      */     
/* 1890 */     BindValue binding = getBinding(parameterIndex, false);
/* 1891 */     setType(binding, 8);
/*      */     
/* 1893 */     binding.value = null;
/* 1894 */     binding.longBinding = x;
/* 1895 */     binding.isNull = false;
/* 1896 */     binding.isLongData = false;
/*      */   }
/*      */   
/*      */ 
/*      */   public void setNull(int parameterIndex, int sqlType)
/*      */     throws SQLException
/*      */   {
/* 1903 */     checkClosed();
/*      */     
/* 1905 */     BindValue binding = getBinding(parameterIndex, false);
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 1911 */     if (binding.bufferType == 0) {
/* 1912 */       setType(binding, 6);
/*      */     }
/*      */     
/* 1915 */     binding.value = null;
/* 1916 */     binding.isNull = true;
/* 1917 */     binding.isLongData = false;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public void setNull(int parameterIndex, int sqlType, String typeName)
/*      */     throws SQLException
/*      */   {
/* 1925 */     checkClosed();
/*      */     
/* 1927 */     BindValue binding = getBinding(parameterIndex, false);
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 1933 */     if (binding.bufferType == 0) {
/* 1934 */       setType(binding, 6);
/*      */     }
/*      */     
/* 1937 */     binding.value = null;
/* 1938 */     binding.isNull = true;
/* 1939 */     binding.isLongData = false;
/*      */   }
/*      */   
/*      */ 
/*      */   public void setRef(int i, Ref x)
/*      */     throws SQLException
/*      */   {
/* 1946 */     throw new NotImplemented();
/*      */   }
/*      */   
/*      */ 
/*      */   public void setShort(int parameterIndex, short x)
/*      */     throws SQLException
/*      */   {
/* 1953 */     checkClosed();
/*      */     
/* 1955 */     BindValue binding = getBinding(parameterIndex, false);
/* 1956 */     setType(binding, 2);
/*      */     
/* 1958 */     binding.value = null;
/* 1959 */     binding.shortBinding = x;
/* 1960 */     binding.isNull = false;
/* 1961 */     binding.isLongData = false;
/*      */   }
/*      */   
/*      */ 
/*      */   public void setString(int parameterIndex, String x)
/*      */     throws SQLException
/*      */   {
/* 1968 */     checkClosed();
/*      */     
/* 1970 */     if (x == null) {
/* 1971 */       setNull(parameterIndex, 1);
/*      */     } else {
/* 1973 */       BindValue binding = getBinding(parameterIndex, false);
/*      */       
/* 1975 */       setType(binding, this.stringTypeCode);
/*      */       
/* 1977 */       binding.value = x;
/* 1978 */       binding.isNull = false;
/* 1979 */       binding.isLongData = false;
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
/*      */   public void setTime(int parameterIndex, Time x)
/*      */     throws SQLException
/*      */   {
/* 1996 */     setTimeInternal(parameterIndex, x, null, this.connection.getDefaultTimeZone(), false);
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
/*      */   public void setTime(int parameterIndex, Time x, Calendar cal)
/*      */     throws SQLException
/*      */   {
/* 2016 */     setTimeInternal(parameterIndex, x, cal, cal.getTimeZone(), true);
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
/*      */   public void setTimeInternal(int parameterIndex, Time x, Calendar targetCalendar, TimeZone tz, boolean rollForward)
/*      */     throws SQLException
/*      */   {
/* 2037 */     if (x == null) {
/* 2038 */       setNull(parameterIndex, 92);
/*      */     } else {
/* 2040 */       BindValue binding = getBinding(parameterIndex, false);
/* 2041 */       setType(binding, 11);
/*      */       
/* 2043 */       Calendar sessionCalendar = getCalendarInstanceForSessionOrNew();
/*      */       
/* 2045 */       synchronized (sessionCalendar) {
/* 2046 */         binding.value = TimeUtil.changeTimezone(this.connection, sessionCalendar, targetCalendar, x, tz, this.connection.getServerTimezoneTZ(), rollForward);
/*      */       }
/*      */       
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 2054 */       binding.isNull = false;
/* 2055 */       binding.isLongData = false;
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
/*      */   public void setTimestamp(int parameterIndex, Timestamp x)
/*      */     throws SQLException
/*      */   {
/* 2073 */     setTimestampInternal(parameterIndex, x, null, this.connection.getDefaultTimeZone(), false);
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
/*      */   public void setTimestamp(int parameterIndex, Timestamp x, Calendar cal)
/*      */     throws SQLException
/*      */   {
/* 2092 */     setTimestampInternal(parameterIndex, x, cal, cal.getTimeZone(), true);
/*      */   }
/*      */   
/*      */ 
/*      */   protected void setTimestampInternal(int parameterIndex, Timestamp x, Calendar targetCalendar, TimeZone tz, boolean rollForward)
/*      */     throws SQLException
/*      */   {
/* 2099 */     if (x == null) {
/* 2100 */       setNull(parameterIndex, 93);
/*      */     } else {
/* 2102 */       BindValue binding = getBinding(parameterIndex, false);
/* 2103 */       setType(binding, 12);
/*      */       
/* 2105 */       Calendar sessionCalendar = this.connection.getUseJDBCCompliantTimezoneShift() ? this.connection.getUtcCalendar() : getCalendarInstanceForSessionOrNew();
/*      */       
/*      */ 
/*      */ 
/* 2109 */       synchronized (sessionCalendar) {
/* 2110 */         binding.value = TimeUtil.changeTimezone(this.connection, sessionCalendar, targetCalendar, x, tz, this.connection.getServerTimezoneTZ(), rollForward);
/*      */       }
/*      */       
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 2118 */       binding.isNull = false;
/* 2119 */       binding.isLongData = false;
/*      */     }
/*      */   }
/*      */   
/*      */   private void setType(BindValue oldValue, int bufferType) {
/* 2124 */     if (oldValue.bufferType != bufferType) {
/* 2125 */       this.sendTypesToServer = true;
/*      */     }
/*      */     
/* 2128 */     oldValue.bufferType = bufferType;
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
/*      */   /**
/*      */    * @deprecated
/*      */    */
/*      */   public void setUnicodeStream(int parameterIndex, InputStream x, int length)
/*      */     throws SQLException
/*      */   {
/* 2152 */     checkClosed();
/*      */     
/* 2154 */     throw new NotImplemented();
/*      */   }
/*      */   
/*      */ 
/*      */   public void setURL(int parameterIndex, URL x)
/*      */     throws SQLException
/*      */   {
/* 2161 */     checkClosed();
/*      */     
/* 2163 */     setString(parameterIndex, x.toString());
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
/*      */   private void storeBinding(Buffer packet, BindValue bindValue, MysqlIO mysql)
/*      */     throws SQLException
/*      */   {
/*      */     try
/*      */     {
/* 2180 */       Object value = bindValue.value;
/*      */       
/*      */ 
/*      */ 
/*      */ 
/* 2185 */       switch (bindValue.bufferType)
/*      */       {
/*      */       case 1: 
/* 2188 */         packet.writeByte(bindValue.byteBinding);
/* 2189 */         return;
/*      */       case 2: 
/* 2191 */         packet.ensureCapacity(2);
/* 2192 */         packet.writeInt(bindValue.shortBinding);
/* 2193 */         return;
/*      */       case 3: 
/* 2195 */         packet.ensureCapacity(4);
/* 2196 */         packet.writeLong(bindValue.intBinding);
/* 2197 */         return;
/*      */       case 8: 
/* 2199 */         packet.ensureCapacity(8);
/* 2200 */         packet.writeLongLong(bindValue.longBinding);
/* 2201 */         return;
/*      */       case 4: 
/* 2203 */         packet.ensureCapacity(4);
/* 2204 */         packet.writeFloat(bindValue.floatBinding);
/* 2205 */         return;
/*      */       case 5: 
/* 2207 */         packet.ensureCapacity(8);
/* 2208 */         packet.writeDouble(bindValue.doubleBinding);
/* 2209 */         return;
/*      */       case 11: 
/* 2211 */         storeTime(packet, (Time)value);
/* 2212 */         return;
/*      */       case 7: 
/*      */       case 10: 
/*      */       case 12: 
/* 2216 */         storeDateTime(packet, (java.util.Date)value, mysql);
/* 2217 */         return;
/*      */       case 0: 
/*      */       case 15: 
/*      */       case 246: 
/*      */       case 253: 
/*      */       case 254: 
/* 2223 */         if ((value instanceof byte[])) {
/* 2224 */           packet.writeLenBytes((byte[])value);
/* 2225 */         } else if (!this.isLoadDataQuery) {
/* 2226 */           packet.writeLenString((String)value, this.charEncoding, this.connection.getServerCharacterEncoding(), this.charConverter, this.connection.parserKnowsUnicode(), this.connection);
/*      */ 
/*      */         }
/*      */         else
/*      */         {
/*      */ 
/* 2232 */           packet.writeLenBytes(((String)value).getBytes());
/*      */         }
/*      */         
/* 2235 */         return;
/*      */       }
/*      */     }
/*      */     catch (UnsupportedEncodingException uEE)
/*      */     {
/* 2240 */       throw SQLError.createSQLException(Messages.getString("ServerPreparedStatement.22") + this.connection.getEncoding() + "'", "S1000");
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   private void storeDataTime412AndOlder(Buffer intoBuf, java.util.Date dt)
/*      */     throws SQLException
/*      */   {
/* 2250 */     Calendar sessionCalendar = getCalendarInstanceForSessionOrNew();
/*      */     
/* 2252 */     synchronized (sessionCalendar) {
/* 2253 */       java.util.Date oldTime = sessionCalendar.getTime();
/*      */       try
/*      */       {
/* 2256 */         intoBuf.ensureCapacity(8);
/* 2257 */         intoBuf.writeByte((byte)7);
/*      */         
/* 2259 */         sessionCalendar.setTime(dt);
/*      */         
/* 2261 */         int year = sessionCalendar.get(1);
/* 2262 */         int month = sessionCalendar.get(2) + 1;
/* 2263 */         int date = sessionCalendar.get(5);
/*      */         
/* 2265 */         intoBuf.writeInt(year);
/* 2266 */         intoBuf.writeByte((byte)month);
/* 2267 */         intoBuf.writeByte((byte)date);
/*      */         
/* 2269 */         if ((dt instanceof java.sql.Date)) {
/* 2270 */           intoBuf.writeByte((byte)0);
/* 2271 */           intoBuf.writeByte((byte)0);
/* 2272 */           intoBuf.writeByte((byte)0);
/*      */         } else {
/* 2274 */           intoBuf.writeByte((byte)sessionCalendar.get(11));
/*      */           
/* 2276 */           intoBuf.writeByte((byte)sessionCalendar.get(12));
/*      */           
/* 2278 */           intoBuf.writeByte((byte)sessionCalendar.get(13));
/*      */         }
/*      */       }
/*      */       finally {
/* 2282 */         sessionCalendar.setTime(oldTime);
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */   private void storeDateTime(Buffer intoBuf, java.util.Date dt, MysqlIO mysql) throws SQLException
/*      */   {
/* 2289 */     if (this.connection.versionMeetsMinimum(4, 1, 3)) {
/* 2290 */       storeDateTime413AndNewer(intoBuf, dt);
/*      */     } else {
/* 2292 */       storeDataTime412AndOlder(intoBuf, dt);
/*      */     }
/*      */   }
/*      */   
/*      */   private void storeDateTime413AndNewer(Buffer intoBuf, java.util.Date dt) throws SQLException
/*      */   {
/* 2298 */     Calendar sessionCalendar = ((dt instanceof Timestamp)) && (this.connection.getUseJDBCCompliantTimezoneShift()) ? this.connection.getUtcCalendar() : getCalendarInstanceForSessionOrNew();
/*      */     
/*      */ 
/*      */ 
/* 2302 */     synchronized (sessionCalendar) {
/* 2303 */       java.util.Date oldTime = sessionCalendar.getTime();
/*      */       
/*      */       try
/*      */       {
/* 2307 */         sessionCalendar.setTime(dt);
/*      */         
/* 2309 */         if ((dt instanceof java.sql.Date)) {
/* 2310 */           sessionCalendar.set(11, 0);
/* 2311 */           sessionCalendar.set(12, 0);
/* 2312 */           sessionCalendar.set(13, 0);
/*      */         }
/*      */         
/* 2315 */         byte length = 7;
/*      */         
/* 2317 */         if ((dt instanceof Timestamp)) {
/* 2318 */           length = 11;
/*      */         }
/*      */         
/* 2321 */         intoBuf.ensureCapacity(length);
/*      */         
/* 2323 */         intoBuf.writeByte(length);
/*      */         
/* 2325 */         int year = sessionCalendar.get(1);
/* 2326 */         int month = sessionCalendar.get(2) + 1;
/* 2327 */         int date = sessionCalendar.get(5);
/*      */         
/* 2329 */         intoBuf.writeInt(year);
/* 2330 */         intoBuf.writeByte((byte)month);
/* 2331 */         intoBuf.writeByte((byte)date);
/*      */         
/* 2333 */         if ((dt instanceof java.sql.Date)) {
/* 2334 */           intoBuf.writeByte((byte)0);
/* 2335 */           intoBuf.writeByte((byte)0);
/* 2336 */           intoBuf.writeByte((byte)0);
/*      */         } else {
/* 2338 */           intoBuf.writeByte((byte)sessionCalendar.get(11));
/*      */           
/* 2340 */           intoBuf.writeByte((byte)sessionCalendar.get(12));
/*      */           
/* 2342 */           intoBuf.writeByte((byte)sessionCalendar.get(13));
/*      */         }
/*      */         
/*      */ 
/* 2346 */         if (length == 11)
/*      */         {
/* 2348 */           intoBuf.writeLong(((Timestamp)dt).getNanos() / 1000);
/*      */         }
/*      */       }
/*      */       finally {
/* 2352 */         sessionCalendar.setTime(oldTime);
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   private void storeReader(MysqlIO mysql, int parameterIndex, Buffer packet, Reader inStream)
/*      */     throws SQLException
/*      */   {
/* 2362 */     String forcedEncoding = this.connection.getClobCharacterEncoding();
/*      */     
/* 2364 */     String clobEncoding = forcedEncoding == null ? this.connection.getEncoding() : forcedEncoding;
/*      */     
/*      */ 
/* 2367 */     int maxBytesChar = 2;
/*      */     
/* 2369 */     if (clobEncoding != null) {
/* 2370 */       if (!clobEncoding.equals("UTF-16")) {
/* 2371 */         maxBytesChar = this.connection.getMaxBytesPerChar(clobEncoding);
/*      */         
/* 2373 */         if (maxBytesChar == 1) {
/* 2374 */           maxBytesChar = 2;
/*      */         }
/*      */       } else {
/* 2377 */         maxBytesChar = 4;
/*      */       }
/*      */     }
/*      */     
/* 2381 */     char[] buf = new char[8192 / maxBytesChar];
/*      */     
/* 2383 */     int numRead = 0;
/*      */     
/* 2385 */     int bytesInPacket = 0;
/* 2386 */     int totalBytesRead = 0;
/* 2387 */     int bytesReadAtLastSend = 0;
/* 2388 */     int packetIsFullAt = this.connection.getBlobSendChunkSize();
/*      */     
/*      */ 
/*      */     try
/*      */     {
/* 2393 */       packet.clear();
/* 2394 */       packet.writeByte((byte)24);
/* 2395 */       packet.writeLong(this.serverStatementId);
/* 2396 */       packet.writeInt(parameterIndex);
/*      */       
/* 2398 */       boolean readAny = false;
/*      */       
/* 2400 */       while ((numRead = inStream.read(buf)) != -1) {
/* 2401 */         readAny = true;
/*      */         
/* 2403 */         byte[] valueAsBytes = StringUtils.getBytes(buf, null, clobEncoding, this.connection.getServerCharacterEncoding(), 0, numRead, this.connection.parserKnowsUnicode());
/*      */         
/*      */ 
/*      */ 
/*      */ 
/* 2408 */         packet.writeBytesNoNull(valueAsBytes, 0, valueAsBytes.length);
/*      */         
/* 2410 */         bytesInPacket += valueAsBytes.length;
/* 2411 */         totalBytesRead += valueAsBytes.length;
/*      */         
/* 2413 */         if (bytesInPacket >= packetIsFullAt) {
/* 2414 */           bytesReadAtLastSend = totalBytesRead;
/*      */           
/* 2416 */           mysql.sendCommand(24, null, packet, true, null);
/*      */           
/*      */ 
/* 2419 */           bytesInPacket = 0;
/* 2420 */           packet.clear();
/* 2421 */           packet.writeByte((byte)24);
/* 2422 */           packet.writeLong(this.serverStatementId);
/* 2423 */           packet.writeInt(parameterIndex);
/*      */         }
/*      */       }
/*      */       
/* 2427 */       if (totalBytesRead != bytesReadAtLastSend) {
/* 2428 */         mysql.sendCommand(24, null, packet, true, null);
/*      */       }
/*      */       
/*      */ 
/* 2432 */       if (!readAny) {
/* 2433 */         mysql.sendCommand(24, null, packet, true, null);
/*      */       }
/*      */     }
/*      */     catch (IOException ioEx) {
/* 2437 */       throw SQLError.createSQLException(Messages.getString("ServerPreparedStatement.24") + ioEx.toString(), "S1000");
/*      */     }
/*      */     finally
/*      */     {
/* 2441 */       if ((this.connection.getAutoClosePStmtStreams()) && 
/* 2442 */         (inStream != null)) {
/*      */         try {
/* 2444 */           inStream.close();
/*      */         }
/*      */         catch (IOException ioEx) {}
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */   private void storeStream(MysqlIO mysql, int parameterIndex, Buffer packet, InputStream inStream)
/*      */     throws SQLException
/*      */   {
/* 2455 */     byte[] buf = new byte[' '];
/*      */     
/* 2457 */     int numRead = 0;
/*      */     try
/*      */     {
/* 2460 */       int bytesInPacket = 0;
/* 2461 */       int totalBytesRead = 0;
/* 2462 */       int bytesReadAtLastSend = 0;
/* 2463 */       int packetIsFullAt = this.connection.getBlobSendChunkSize();
/*      */       
/* 2465 */       packet.clear();
/* 2466 */       packet.writeByte((byte)24);
/* 2467 */       packet.writeLong(this.serverStatementId);
/* 2468 */       packet.writeInt(parameterIndex);
/*      */       
/* 2470 */       boolean readAny = false;
/*      */       
/* 2472 */       while ((numRead = inStream.read(buf)) != -1)
/*      */       {
/* 2474 */         readAny = true;
/*      */         
/* 2476 */         packet.writeBytesNoNull(buf, 0, numRead);
/* 2477 */         bytesInPacket += numRead;
/* 2478 */         totalBytesRead += numRead;
/*      */         
/* 2480 */         if (bytesInPacket >= packetIsFullAt) {
/* 2481 */           bytesReadAtLastSend = totalBytesRead;
/*      */           
/* 2483 */           mysql.sendCommand(24, null, packet, true, null);
/*      */           
/*      */ 
/* 2486 */           bytesInPacket = 0;
/* 2487 */           packet.clear();
/* 2488 */           packet.writeByte((byte)24);
/* 2489 */           packet.writeLong(this.serverStatementId);
/* 2490 */           packet.writeInt(parameterIndex);
/*      */         }
/*      */       }
/*      */       
/* 2494 */       if (totalBytesRead != bytesReadAtLastSend) {
/* 2495 */         mysql.sendCommand(24, null, packet, true, null);
/*      */       }
/*      */       
/*      */ 
/* 2499 */       if (!readAny) {
/* 2500 */         mysql.sendCommand(24, null, packet, true, null);
/*      */       }
/*      */     }
/*      */     catch (IOException ioEx) {
/* 2504 */       throw SQLError.createSQLException(Messages.getString("ServerPreparedStatement.25") + ioEx.toString(), "S1000");
/*      */     }
/*      */     finally
/*      */     {
/* 2508 */       if ((this.connection.getAutoClosePStmtStreams()) && 
/* 2509 */         (inStream != null)) {
/*      */         try {
/* 2511 */           inStream.close();
/*      */         }
/*      */         catch (IOException ioEx) {}
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public String toString()
/*      */   {
/* 2524 */     StringBuffer toStringBuf = new StringBuffer();
/*      */     
/* 2526 */     toStringBuf.append("com.mysql.jdbc.ServerPreparedStatement[");
/* 2527 */     toStringBuf.append(this.serverStatementId);
/* 2528 */     toStringBuf.append("] - ");
/*      */     try
/*      */     {
/* 2531 */       toStringBuf.append(asSql());
/*      */     } catch (SQLException sqlEx) {
/* 2533 */       toStringBuf.append(Messages.getString("ServerPreparedStatement.6"));
/* 2534 */       toStringBuf.append(sqlEx);
/*      */     }
/*      */     
/* 2537 */     return toStringBuf.toString();
/*      */   }
/*      */   
/*      */   protected long getServerStatementId() {
/* 2541 */     return this.serverStatementId;
/*      */   }
/*      */   
/*      */   public synchronized boolean canRewriteAsMultivalueInsertStatement() {
/* 2545 */     if (!super.canRewriteAsMultivalueInsertStatement()) {
/* 2546 */       return false;
/*      */     }
/*      */     
/* 2549 */     BindValue[] currentBindValues = null;
/* 2550 */     BindValue[] previousBindValues = null;
/*      */     
/* 2552 */     int nbrCommands = this.batchedArgs.size();
/*      */     
/*      */ 
/*      */ 
/* 2556 */     for (int commandIndex = 0; commandIndex < nbrCommands; commandIndex++) {
/* 2557 */       Object arg = this.batchedArgs.get(commandIndex);
/*      */       
/* 2559 */       if (!(arg instanceof String))
/*      */       {
/* 2561 */         currentBindValues = ((BatchedBindValues)arg).batchedParameterValues;
/*      */         
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 2567 */         if (previousBindValues != null) {
/* 2568 */           for (int j = 0; j < this.parameterBindings.length; j++) {
/* 2569 */             if (currentBindValues[j].bufferType != previousBindValues[j].bufferType) {
/* 2570 */               return false;
/*      */             }
/*      */           }
/*      */         }
/*      */       }
/*      */     }
/*      */     
/* 2577 */     return true;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   protected long[] computeMaxParameterSetSizeAndBatchSize(int numBatchedArgs)
/*      */   {
/* 2585 */     long sizeOfEntireBatch = 10L;
/* 2586 */     long maxSizeOfParameterSet = 0L;
/*      */     
/* 2588 */     for (int i = 0; i < numBatchedArgs; i++) {
/* 2589 */       BindValue[] paramArg = ((BatchedBindValues)this.batchedArgs.get(i)).batchedParameterValues;
/*      */       
/* 2591 */       long sizeOfParameterSet = 0L;
/*      */       
/* 2593 */       sizeOfParameterSet += (this.parameterCount + 7) / 8;
/*      */       
/* 2595 */       sizeOfParameterSet += this.parameterCount * 2;
/*      */       
/* 2597 */       for (int j = 0; j < this.parameterBindings.length; j++) {
/* 2598 */         if (!paramArg[j].isNull)
/*      */         {
/* 2600 */           long size = paramArg[j].getBoundLength();
/*      */           
/* 2602 */           if (paramArg[j].isLongData) {
/* 2603 */             if (size != -1L) {
/* 2604 */               sizeOfParameterSet += size;
/*      */             }
/*      */           } else {
/* 2607 */             sizeOfParameterSet += size;
/*      */           }
/*      */         }
/*      */       }
/*      */       
/* 2612 */       sizeOfEntireBatch += sizeOfParameterSet;
/*      */       
/* 2614 */       if (sizeOfParameterSet > maxSizeOfParameterSet) {
/* 2615 */         maxSizeOfParameterSet = sizeOfParameterSet;
/*      */       }
/*      */     }
/*      */     
/* 2619 */     return new long[] { maxSizeOfParameterSet, sizeOfEntireBatch };
/*      */   }
/*      */   
/*      */   protected int setOneBatchedParameterSet(java.sql.PreparedStatement batchedStatement, int batchedParamIndex, Object paramSet)
/*      */     throws SQLException
/*      */   {
/* 2625 */     BindValue[] paramArg = ((BatchedBindValues)paramSet).batchedParameterValues;
/*      */     
/* 2627 */     for (int j = 0; j < paramArg.length; j++) {
/* 2628 */       if (paramArg[j].isNull) {
/* 2629 */         batchedStatement.setNull(batchedParamIndex++, 0);
/*      */       }
/* 2631 */       else if (paramArg[j].isLongData) {
/* 2632 */         Object value = paramArg[j].value;
/*      */         
/* 2634 */         if ((value instanceof InputStream)) {
/* 2635 */           batchedStatement.setBinaryStream(batchedParamIndex++, (InputStream)value, (int)paramArg[j].bindLength);
/*      */         }
/*      */         else
/*      */         {
/* 2639 */           batchedStatement.setCharacterStream(batchedParamIndex++, (Reader)value, (int)paramArg[j].bindLength);
/*      */         }
/*      */         
/*      */       }
/*      */       else
/*      */       {
/* 2645 */         switch (paramArg[j].bufferType)
/*      */         {
/*      */         case 1: 
/* 2648 */           batchedStatement.setByte(batchedParamIndex++, paramArg[j].byteBinding);
/*      */           
/* 2650 */           break;
/*      */         case 2: 
/* 2652 */           batchedStatement.setShort(batchedParamIndex++, paramArg[j].shortBinding);
/*      */           
/* 2654 */           break;
/*      */         case 3: 
/* 2656 */           batchedStatement.setInt(batchedParamIndex++, paramArg[j].intBinding);
/*      */           
/* 2658 */           break;
/*      */         case 8: 
/* 2660 */           batchedStatement.setLong(batchedParamIndex++, paramArg[j].longBinding);
/*      */           
/* 2662 */           break;
/*      */         case 4: 
/* 2664 */           batchedStatement.setFloat(batchedParamIndex++, paramArg[j].floatBinding);
/*      */           
/* 2666 */           break;
/*      */         case 5: 
/* 2668 */           batchedStatement.setDouble(batchedParamIndex++, paramArg[j].doubleBinding);
/*      */           
/* 2670 */           break;
/*      */         case 11: 
/* 2672 */           batchedStatement.setTime(batchedParamIndex++, (Time)paramArg[j].value);
/*      */           
/* 2674 */           break;
/*      */         case 10: 
/* 2676 */           batchedStatement.setDate(batchedParamIndex++, (java.sql.Date)paramArg[j].value);
/*      */           
/* 2678 */           break;
/*      */         case 7: 
/*      */         case 12: 
/* 2681 */           batchedStatement.setTimestamp(batchedParamIndex++, (Timestamp)paramArg[j].value);
/*      */           
/* 2683 */           break;
/*      */         case 0: 
/*      */         case 15: 
/*      */         case 246: 
/*      */         case 253: 
/*      */         case 254: 
/* 2689 */           Object value = paramArg[j].value;
/*      */           
/* 2691 */           if ((value instanceof byte[])) {
/* 2692 */             batchedStatement.setBytes(batchedParamIndex, (byte[])value);
/*      */           }
/*      */           else {
/* 2695 */             batchedStatement.setString(batchedParamIndex, (String)value);
/*      */           }
/*      */           
/*      */ 
/* 2699 */           BindValue asBound = ((ServerPreparedStatement)batchedStatement).getBinding(batchedParamIndex + 1, false);
/*      */           
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 2706 */           asBound.bufferType = paramArg[j].bufferType;
/*      */           
/* 2708 */           batchedParamIndex++;
/*      */           
/* 2710 */           break;
/*      */         default: 
/* 2712 */           throw new IllegalArgumentException("Unknown type when re-binding parameter into batched statement for parameter index " + batchedParamIndex);
/*      */         }
/*      */         
/*      */       }
/*      */     }
/*      */     
/*      */ 
/*      */ 
/* 2720 */     return batchedParamIndex;
/*      */   }
/*      */ }


/* Location:              E:\java\java学习\hutubill\lib\all.jar!\com\mysql\jdbc\ServerPreparedStatement.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version:       0.7.1
 */
/*      */ package com.mysql.jdbc;
/*      */ 
/*      */ import com.mysql.jdbc.exceptions.MySQLTimeoutException;
/*      */ import com.mysql.jdbc.profiler.ProfileEventSink;
/*      */ import com.mysql.jdbc.profiler.ProfilerEvent;
/*      */ import java.io.ByteArrayInputStream;
/*      */ import java.io.ByteArrayOutputStream;
/*      */ import java.io.IOException;
/*      */ import java.io.InputStream;
/*      */ import java.io.ObjectOutputStream;
/*      */ import java.io.Reader;
/*      */ import java.io.StringReader;
/*      */ import java.io.UnsupportedEncodingException;
/*      */ import java.math.BigDecimal;
/*      */ import java.math.BigInteger;
/*      */ import java.net.URL;
/*      */ import java.sql.Array;
/*      */ import java.sql.BatchUpdateException;
/*      */ import java.sql.Blob;
/*      */ import java.sql.Clob;
/*      */ import java.sql.DatabaseMetaData;
/*      */ import java.sql.ParameterMetaData;
/*      */ import java.sql.Ref;
/*      */ import java.sql.SQLException;
/*      */ import java.sql.Time;
/*      */ import java.sql.Timestamp;
/*      */ import java.text.DateFormat;
/*      */ import java.text.ParsePosition;
/*      */ import java.text.SimpleDateFormat;
/*      */ import java.util.ArrayList;
/*      */ import java.util.Calendar;
/*      */ import java.util.List;
/*      */ import java.util.Locale;
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
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ public class PreparedStatement
/*      */   extends Statement
/*      */   implements java.sql.PreparedStatement
/*      */ {
/*      */   class BatchParams
/*      */   {
/*   84 */     boolean[] isNull = null;
/*      */     
/*   86 */     boolean[] isStream = null;
/*      */     
/*   88 */     InputStream[] parameterStreams = null;
/*      */     
/*   90 */     byte[][] parameterStrings = (byte[][])null;
/*      */     
/*   92 */     int[] streamLengths = null;
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */     BatchParams(byte[][] strings, InputStream[] streams, boolean[] isStreamFlags, int[] lengths, boolean[] isNullFlags)
/*      */     {
/*   99 */       this.parameterStrings = new byte[strings.length][];
/*  100 */       this.parameterStreams = new InputStream[streams.length];
/*  101 */       this.isStream = new boolean[isStreamFlags.length];
/*  102 */       this.streamLengths = new int[lengths.length];
/*  103 */       this.isNull = new boolean[isNullFlags.length];
/*  104 */       System.arraycopy(strings, 0, this.parameterStrings, 0, strings.length);
/*      */       
/*  106 */       System.arraycopy(streams, 0, this.parameterStreams, 0, streams.length);
/*      */       
/*  108 */       System.arraycopy(isStreamFlags, 0, this.isStream, 0, isStreamFlags.length);
/*      */       
/*  110 */       System.arraycopy(lengths, 0, this.streamLengths, 0, lengths.length);
/*  111 */       System.arraycopy(isNullFlags, 0, this.isNull, 0, isNullFlags.length);
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */   class EndPoint
/*      */   {
/*      */     int begin;
/*      */     int end;
/*      */     
/*      */     EndPoint(int b, int e)
/*      */     {
/*  123 */       this.begin = b;
/*  124 */       this.end = e;
/*      */     }
/*      */   }
/*      */   
/*      */   class ParseInfo {
/*  129 */     char firstStmtChar = '\000';
/*      */     
/*  131 */     boolean foundLimitClause = false;
/*      */     
/*  133 */     boolean foundLoadData = false;
/*      */     
/*  135 */     long lastUsed = 0L;
/*      */     
/*  137 */     int statementLength = 0;
/*      */     
/*  139 */     int statementStartPos = 0;
/*      */     
/*  141 */     byte[][] staticSql = (byte[][])null;
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     public ParseInfo(String sql, Connection conn, DatabaseMetaData dbmd, String encoding, SingleByteCharsetConverter converter)
/*      */       throws SQLException
/*      */     {
/*  152 */       if (sql == null) {
/*  153 */         throw SQLError.createSQLException(Messages.getString("PreparedStatement.61"), "S1009");
/*      */       }
/*      */       
/*      */ 
/*      */ 
/*  158 */       this.lastUsed = System.currentTimeMillis();
/*      */       
/*  160 */       String quotedIdentifierString = dbmd.getIdentifierQuoteString();
/*      */       
/*  162 */       char quotedIdentifierChar = '\000';
/*      */       
/*  164 */       if ((quotedIdentifierString != null) && (!quotedIdentifierString.equals(" ")) && (quotedIdentifierString.length() > 0))
/*      */       {
/*      */ 
/*  167 */         quotedIdentifierChar = quotedIdentifierString.charAt(0);
/*      */       }
/*      */       
/*  170 */       this.statementLength = sql.length();
/*      */       
/*  172 */       ArrayList endpointList = new ArrayList();
/*  173 */       boolean inQuotes = false;
/*  174 */       char quoteChar = '\000';
/*  175 */       boolean inQuotedId = false;
/*  176 */       int lastParmEnd = 0;
/*      */       
/*      */ 
/*  179 */       int stopLookingForLimitClause = this.statementLength - 5;
/*      */       
/*  181 */       this.foundLimitClause = false;
/*      */       
/*  183 */       boolean noBackslashEscapes = PreparedStatement.this.connection.isNoBackslashEscapesSet();
/*      */       
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  189 */       this.statementStartPos = PreparedStatement.this.findStartOfStatement(sql);
/*      */       
/*  191 */       for (int i = this.statementStartPos; i < this.statementLength; i++) {
/*  192 */         char c = sql.charAt(i);
/*      */         
/*  194 */         if ((this.firstStmtChar == 0) && (!Character.isWhitespace(c)))
/*      */         {
/*      */ 
/*  197 */           this.firstStmtChar = Character.toUpperCase(c);
/*      */         }
/*      */         
/*  200 */         if ((!noBackslashEscapes) && (c == '\\') && (i < this.statementLength - 1))
/*      */         {
/*  202 */           i++;
/*      */ 
/*      */         }
/*      */         else
/*      */         {
/*      */ 
/*  208 */           if ((!inQuotes) && (quotedIdentifierChar != 0) && (c == quotedIdentifierChar))
/*      */           {
/*  210 */             inQuotedId = !inQuotedId;
/*  211 */           } else if (!inQuotedId)
/*      */           {
/*      */ 
/*  214 */             if (inQuotes) {
/*  215 */               if (((c == '\'') || (c == '"')) && (c == quoteChar)) {
/*  216 */                 if ((i < this.statementLength - 1) && (sql.charAt(i + 1) == quoteChar)) {
/*  217 */                   i++;
/*  218 */                   continue;
/*      */                 }
/*      */                 
/*  221 */                 inQuotes = !inQuotes;
/*  222 */                 quoteChar = '\000';
/*  223 */               } else if (((c == '\'') || (c == '"')) && (c == quoteChar)) {
/*  224 */                 inQuotes = !inQuotes;
/*  225 */                 quoteChar = '\000';
/*      */               }
/*      */             } else {
/*  228 */               if ((c == '#') || ((c == '-') && (i + 1 < this.statementLength) && (sql.charAt(i + 1) == '-')))
/*      */               {
/*      */ 
/*      */ 
/*      */ 
/*  233 */                 int endOfStmt = this.statementLength - 1;
/*  235 */                 for (; 
/*  235 */                     i < endOfStmt; i++) {
/*  236 */                   c = sql.charAt(i);
/*      */                   
/*  238 */                   if ((c == '\r') || (c == '\n')) {
/*      */                     break;
/*      */                   }
/*      */                 }
/*      */               }
/*      */               
/*  244 */               if ((c == '/') && (i + 1 < this.statementLength))
/*      */               {
/*  246 */                 char cNext = sql.charAt(i + 1);
/*      */                 
/*  248 */                 if (cNext == '*') {
/*  249 */                   i += 2;
/*      */                   
/*  251 */                   for (int j = i; j < this.statementLength; j++) {
/*  252 */                     i++;
/*  253 */                     cNext = sql.charAt(j);
/*      */                     
/*  255 */                     if ((cNext == '*') && (j + 1 < this.statementLength) && 
/*  256 */                       (sql.charAt(j + 1) == '/')) {
/*  257 */                       i++;
/*      */                       
/*  259 */                       if (i >= this.statementLength) break;
/*  260 */                       c = sql.charAt(i); break;
/*      */                     }
/*      */                     
/*      */                   }
/*      */                   
/*      */                 }
/*      */                 
/*      */               }
/*  268 */               else if ((c == '\'') || (c == '"')) {
/*  269 */                 inQuotes = true;
/*  270 */                 quoteChar = c;
/*      */               }
/*      */             }
/*      */           }
/*      */           
/*  275 */           if ((c == '?') && (!inQuotes) && (!inQuotedId)) {
/*  276 */             endpointList.add(new int[] { lastParmEnd, i });
/*  277 */             lastParmEnd = i + 1;
/*      */           }
/*      */           
/*  280 */           if ((!inQuotes) && (i < stopLookingForLimitClause) && (
/*  281 */             (c == 'L') || (c == 'l'))) {
/*  282 */             char posI1 = sql.charAt(i + 1);
/*      */             
/*  284 */             if ((posI1 == 'I') || (posI1 == 'i')) {
/*  285 */               char posM = sql.charAt(i + 2);
/*      */               
/*  287 */               if ((posM == 'M') || (posM == 'm')) {
/*  288 */                 char posI2 = sql.charAt(i + 3);
/*      */                 
/*  290 */                 if ((posI2 == 'I') || (posI2 == 'i')) {
/*  291 */                   char posT = sql.charAt(i + 4);
/*      */                   
/*  293 */                   if ((posT == 'T') || (posT == 't')) {
/*  294 */                     this.foundLimitClause = true;
/*      */                   }
/*      */                 }
/*      */               }
/*      */             }
/*      */           }
/*      */         }
/*      */       }
/*      */       
/*  303 */       if (this.firstStmtChar == 'L') {
/*  304 */         if (StringUtils.startsWithIgnoreCaseAndWs(sql, "LOAD DATA")) {
/*  305 */           this.foundLoadData = true;
/*      */         } else {
/*  307 */           this.foundLoadData = false;
/*      */         }
/*      */       } else {
/*  310 */         this.foundLoadData = false;
/*      */       }
/*      */       
/*  313 */       endpointList.add(new int[] { lastParmEnd, this.statementLength });
/*  314 */       this.staticSql = new byte[endpointList.size()][];
/*  315 */       char[] asCharArray = sql.toCharArray();
/*      */       
/*  317 */       for (i = 0; i < this.staticSql.length; i++) {
/*  318 */         int[] ep = (int[])endpointList.get(i);
/*  319 */         int end = ep[1];
/*  320 */         int begin = ep[0];
/*  321 */         int len = end - begin;
/*      */         
/*  323 */         if (this.foundLoadData) {
/*  324 */           String temp = new String(asCharArray, begin, len);
/*  325 */           this.staticSql[i] = temp.getBytes();
/*  326 */         } else if (encoding == null) {
/*  327 */           byte[] buf = new byte[len];
/*      */           
/*  329 */           for (int j = 0; j < len; j++) {
/*  330 */             buf[j] = ((byte)sql.charAt(begin + j));
/*      */           }
/*      */           
/*  333 */           this.staticSql[i] = buf;
/*      */         }
/*  335 */         else if (converter != null) {
/*  336 */           this.staticSql[i] = StringUtils.getBytes(sql, converter, encoding, PreparedStatement.this.connection.getServerCharacterEncoding(), begin, len, PreparedStatement.this.connection.parserKnowsUnicode());
/*      */ 
/*      */         }
/*      */         else
/*      */         {
/*  341 */           String temp = new String(asCharArray, begin, len);
/*      */           
/*  343 */           this.staticSql[i] = StringUtils.getBytes(temp, encoding, PreparedStatement.this.connection.getServerCharacterEncoding(), PreparedStatement.this.connection.parserKnowsUnicode(), conn);
/*      */         }
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  353 */   private static final byte[] HEX_DIGITS = { 48, 49, 50, 51, 52, 53, 54, 55, 56, 57, 65, 66, 67, 68, 69, 70 };
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   private static int readFully(Reader reader, char[] buf, int length)
/*      */     throws IOException
/*      */   {
/*  376 */     int numCharsRead = 0;
/*      */     
/*  378 */     while (numCharsRead < length) {
/*  379 */       int count = reader.read(buf, numCharsRead, length - numCharsRead);
/*      */       
/*  381 */       if (count < 0) {
/*      */         break;
/*      */       }
/*      */       
/*  385 */       numCharsRead += count;
/*      */     }
/*      */     
/*  388 */     return numCharsRead;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  397 */   protected boolean batchHasPlainStatements = false;
/*      */   
/*  399 */   private DatabaseMetaData dbmd = null;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  405 */   protected char firstCharOfStmt = '\000';
/*      */   
/*      */ 
/*  408 */   protected boolean hasLimitClause = false;
/*      */   
/*      */ 
/*  411 */   protected boolean isLoadDataQuery = false;
/*      */   
/*  413 */   private boolean[] isNull = null;
/*      */   
/*  415 */   private boolean[] isStream = null;
/*      */   
/*  417 */   protected int numberOfExecutions = 0;
/*      */   
/*      */ 
/*  420 */   protected String originalSql = null;
/*      */   
/*      */ 
/*      */   protected int parameterCount;
/*      */   
/*      */   protected MysqlParameterMetadata parameterMetaData;
/*      */   
/*  427 */   private InputStream[] parameterStreams = null;
/*      */   
/*  429 */   private byte[][] parameterValues = (byte[][])null;
/*      */   
/*      */   private ParseInfo parseInfo;
/*      */   
/*      */   private java.sql.ResultSetMetaData pstmtResultMetaData;
/*      */   
/*  435 */   private byte[][] staticSqlStrings = (byte[][])null;
/*      */   
/*  437 */   private byte[] streamConvertBuf = new byte['က'];
/*      */   
/*  439 */   private int[] streamLengths = null;
/*      */   
/*  441 */   private SimpleDateFormat tsdf = null;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*  446 */   protected boolean useTrueBoolean = false;
/*      */   
/*      */ 
/*      */ 
/*      */   private boolean usingAnsiMode;
/*      */   
/*      */ 
/*      */ 
/*      */   private String batchedValuesClause;
/*      */   
/*      */ 
/*      */ 
/*      */   private int statementAfterCommentsPos;
/*      */   
/*      */ 
/*  461 */   private boolean hasCheckedForRewrite = false;
/*      */   
/*      */ 
/*      */ 
/*  465 */   private boolean canRewrite = false;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   private boolean doPingInstead;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected PreparedStatement(Connection conn, String catalog)
/*      */     throws SQLException
/*      */   {
/*  482 */     super(conn, catalog);
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
/*      */   public PreparedStatement(Connection conn, String sql, String catalog)
/*      */     throws SQLException
/*      */   {
/*  500 */     super(conn, catalog);
/*      */     
/*  502 */     if (sql == null) {
/*  503 */       throw SQLError.createSQLException(Messages.getString("PreparedStatement.0"), "S1009");
/*      */     }
/*      */     
/*      */ 
/*  507 */     this.originalSql = sql;
/*      */     
/*  509 */     if (this.originalSql.startsWith("/* ping */")) {
/*  510 */       this.doPingInstead = true;
/*      */     } else {
/*  512 */       this.doPingInstead = false;
/*      */     }
/*      */     
/*  515 */     this.dbmd = this.connection.getMetaData();
/*      */     
/*  517 */     this.useTrueBoolean = this.connection.versionMeetsMinimum(3, 21, 23);
/*      */     
/*  519 */     this.parseInfo = new ParseInfo(sql, this.connection, this.dbmd, this.charEncoding, this.charConverter);
/*      */     
/*      */ 
/*  522 */     initializeFromParseInfo();
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
/*      */   public PreparedStatement(Connection conn, String sql, String catalog, ParseInfo cachedParseInfo)
/*      */     throws SQLException
/*      */   {
/*  542 */     super(conn, catalog);
/*      */     
/*  544 */     if (sql == null) {
/*  545 */       throw SQLError.createSQLException(Messages.getString("PreparedStatement.1"), "S1009");
/*      */     }
/*      */     
/*      */ 
/*  549 */     this.originalSql = sql;
/*      */     
/*  551 */     this.dbmd = this.connection.getMetaData();
/*      */     
/*  553 */     this.useTrueBoolean = this.connection.versionMeetsMinimum(3, 21, 23);
/*      */     
/*  555 */     this.parseInfo = cachedParseInfo;
/*      */     
/*  557 */     this.usingAnsiMode = (!this.connection.useAnsiQuotedIdentifiers());
/*      */     
/*  559 */     initializeFromParseInfo();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void addBatch()
/*      */     throws SQLException
/*      */   {
/*  571 */     if (this.batchedArgs == null) {
/*  572 */       this.batchedArgs = new ArrayList();
/*      */     }
/*      */     
/*  575 */     this.batchedArgs.add(new BatchParams(this.parameterValues, this.parameterStreams, this.isStream, this.streamLengths, this.isNull));
/*      */   }
/*      */   
/*      */   public synchronized void addBatch(String sql)
/*      */     throws SQLException
/*      */   {
/*  581 */     this.batchHasPlainStatements = true;
/*      */     
/*  583 */     super.addBatch(sql);
/*      */   }
/*      */   
/*      */   protected String asSql() throws SQLException {
/*  587 */     return asSql(false);
/*      */   }
/*      */   
/*      */   protected String asSql(boolean quoteStreamsAndUnknowns) throws SQLException {
/*  591 */     if (this.isClosed) {
/*  592 */       return "statement has been closed, no further internal information available";
/*      */     }
/*      */     
/*  595 */     StringBuffer buf = new StringBuffer();
/*      */     try
/*      */     {
/*  598 */       for (int i = 0; i < this.parameterCount; i++) {
/*  599 */         if (this.charEncoding != null) {
/*  600 */           buf.append(new String(this.staticSqlStrings[i], this.charEncoding));
/*      */         }
/*      */         else {
/*  603 */           buf.append(new String(this.staticSqlStrings[i]));
/*      */         }
/*      */         
/*  606 */         if ((this.parameterValues[i] == null) && (this.isStream[i] == 0)) {
/*  607 */           if (quoteStreamsAndUnknowns) {
/*  608 */             buf.append("'");
/*      */           }
/*      */           
/*  611 */           buf.append("** NOT SPECIFIED **");
/*      */           
/*  613 */           if (quoteStreamsAndUnknowns) {
/*  614 */             buf.append("'");
/*      */           }
/*  616 */         } else if (this.isStream[i] != 0) {
/*  617 */           if (quoteStreamsAndUnknowns) {
/*  618 */             buf.append("'");
/*      */           }
/*      */           
/*  621 */           buf.append("** STREAM DATA **");
/*      */           
/*  623 */           if (quoteStreamsAndUnknowns) {
/*  624 */             buf.append("'");
/*      */           }
/*      */         }
/*  627 */         else if (this.charConverter != null) {
/*  628 */           buf.append(this.charConverter.toString(this.parameterValues[i]));
/*      */ 
/*      */         }
/*  631 */         else if (this.charEncoding != null) {
/*  632 */           buf.append(new String(this.parameterValues[i], this.charEncoding));
/*      */         }
/*      */         else {
/*  635 */           buf.append(StringUtils.toAsciiString(this.parameterValues[i]));
/*      */         }
/*      */       }
/*      */       
/*      */ 
/*      */ 
/*      */ 
/*  642 */       if (this.charEncoding != null) {
/*  643 */         buf.append(new String(this.staticSqlStrings[this.parameterCount], this.charEncoding));
/*      */       }
/*      */       else
/*      */       {
/*  647 */         buf.append(StringUtils.toAsciiString(this.staticSqlStrings[this.parameterCount]));
/*      */       }
/*      */     }
/*      */     catch (UnsupportedEncodingException uue)
/*      */     {
/*  652 */       throw new RuntimeException(Messages.getString("PreparedStatement.32") + this.charEncoding + Messages.getString("PreparedStatement.33"));
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*  658 */     return buf.toString();
/*      */   }
/*      */   
/*      */   public synchronized void clearBatch() throws SQLException {
/*  662 */     this.batchHasPlainStatements = false;
/*      */     
/*  664 */     super.clearBatch();
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
/*      */   public synchronized void clearParameters()
/*      */     throws SQLException
/*      */   {
/*  678 */     checkClosed();
/*      */     
/*  680 */     for (int i = 0; i < this.parameterValues.length; i++) {
/*  681 */       this.parameterValues[i] = null;
/*  682 */       this.parameterStreams[i] = null;
/*  683 */       this.isStream[i] = false;
/*  684 */       this.isNull[i] = false;
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public synchronized void close()
/*      */     throws SQLException
/*      */   {
/*  695 */     realClose(true, true);
/*      */   }
/*      */   
/*      */   private final void escapeblockFast(byte[] buf, Buffer packet, int size) throws SQLException
/*      */   {
/*  700 */     int lastwritten = 0;
/*      */     
/*  702 */     for (int i = 0; i < size; i++) {
/*  703 */       byte b = buf[i];
/*      */       
/*  705 */       if (b == 0)
/*      */       {
/*  707 */         if (i > lastwritten) {
/*  708 */           packet.writeBytesNoNull(buf, lastwritten, i - lastwritten);
/*      */         }
/*      */         
/*      */ 
/*  712 */         packet.writeByte((byte)92);
/*  713 */         packet.writeByte((byte)48);
/*  714 */         lastwritten = i + 1;
/*      */       }
/*  716 */       else if ((b == 92) || (b == 39) || ((!this.usingAnsiMode) && (b == 34)))
/*      */       {
/*      */ 
/*  719 */         if (i > lastwritten) {
/*  720 */           packet.writeBytesNoNull(buf, lastwritten, i - lastwritten);
/*      */         }
/*      */         
/*      */ 
/*      */ 
/*  725 */         packet.writeByte((byte)92);
/*  726 */         lastwritten = i;
/*      */       }
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*  732 */     if (lastwritten < size) {
/*  733 */       packet.writeBytesNoNull(buf, lastwritten, size - lastwritten);
/*      */     }
/*      */   }
/*      */   
/*      */   private final void escapeblockFast(byte[] buf, ByteArrayOutputStream bytesOut, int size)
/*      */   {
/*  739 */     int lastwritten = 0;
/*      */     
/*  741 */     for (int i = 0; i < size; i++) {
/*  742 */       byte b = buf[i];
/*      */       
/*  744 */       if (b == 0)
/*      */       {
/*  746 */         if (i > lastwritten) {
/*  747 */           bytesOut.write(buf, lastwritten, i - lastwritten);
/*      */         }
/*      */         
/*      */ 
/*  751 */         bytesOut.write(92);
/*  752 */         bytesOut.write(48);
/*  753 */         lastwritten = i + 1;
/*      */       }
/*  755 */       else if ((b == 92) || (b == 39) || ((!this.usingAnsiMode) && (b == 34)))
/*      */       {
/*      */ 
/*  758 */         if (i > lastwritten) {
/*  759 */           bytesOut.write(buf, lastwritten, i - lastwritten);
/*      */         }
/*      */         
/*      */ 
/*  763 */         bytesOut.write(92);
/*  764 */         lastwritten = i;
/*      */       }
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*  770 */     if (lastwritten < size) {
/*  771 */       bytesOut.write(buf, lastwritten, size - lastwritten);
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
/*      */   public boolean execute()
/*      */     throws SQLException
/*      */   {
/*  787 */     checkClosed();
/*      */     
/*  789 */     Connection locallyScopedConn = this.connection;
/*      */     
/*  791 */     if ((locallyScopedConn.isReadOnly()) && (this.firstCharOfStmt != 'S')) {
/*  792 */       throw SQLError.createSQLException(Messages.getString("PreparedStatement.20") + Messages.getString("PreparedStatement.21"), "S1009");
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*  797 */     ResultSet rs = null;
/*      */     
/*  799 */     CachedResultSetMetaData cachedMetadata = null;
/*      */     
/*  801 */     synchronized (locallyScopedConn.getMutex()) {
/*  802 */       clearWarnings();
/*      */       
/*  804 */       this.batchedGeneratedKeys = null;
/*      */       
/*  806 */       Buffer sendPacket = fillSendPacket();
/*      */       
/*  808 */       String oldCatalog = null;
/*      */       
/*  810 */       if (!locallyScopedConn.getCatalog().equals(this.currentCatalog)) {
/*  811 */         oldCatalog = locallyScopedConn.getCatalog();
/*  812 */         locallyScopedConn.setCatalog(this.currentCatalog);
/*      */       }
/*      */       
/*      */ 
/*      */ 
/*      */ 
/*  818 */       if (locallyScopedConn.getCacheResultSetMetadata()) {
/*  819 */         cachedMetadata = locallyScopedConn.getCachedMetaData(this.originalSql);
/*      */       }
/*      */       
/*  822 */       Field[] metadataFromCache = null;
/*      */       
/*  824 */       if (cachedMetadata != null) {
/*  825 */         metadataFromCache = cachedMetadata.fields;
/*      */       }
/*      */       
/*  828 */       boolean oldInfoMsgState = false;
/*      */       
/*  830 */       if (this.retrieveGeneratedKeys) {
/*  831 */         oldInfoMsgState = locallyScopedConn.isReadInfoMsgEnabled();
/*  832 */         locallyScopedConn.setReadInfoMsgEnabled(true);
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
/*  844 */       if (locallyScopedConn.useMaxRows()) {
/*  845 */         int rowLimit = -1;
/*      */         
/*  847 */         if (this.firstCharOfStmt == 'S') {
/*  848 */           if (this.hasLimitClause) {
/*  849 */             rowLimit = this.maxRows;
/*      */           }
/*  851 */           else if (this.maxRows <= 0) {
/*  852 */             locallyScopedConn.execSQL(this, "SET OPTION SQL_SELECT_LIMIT=DEFAULT", -1, null, 1003, 1007, false, this.currentCatalog, true);
/*      */ 
/*      */           }
/*      */           else
/*      */           {
/*      */ 
/*  858 */             locallyScopedConn.execSQL(this, "SET OPTION SQL_SELECT_LIMIT=" + this.maxRows, -1, null, 1003, 1007, false, this.currentCatalog, true);
/*      */ 
/*      */ 
/*      */           }
/*      */           
/*      */ 
/*      */ 
/*      */ 
/*      */         }
/*      */         else
/*      */         {
/*      */ 
/*  870 */           locallyScopedConn.execSQL(this, "SET OPTION SQL_SELECT_LIMIT=DEFAULT", -1, null, 1003, 1007, false, this.currentCatalog, true);
/*      */         }
/*      */         
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  878 */         rs = executeInternal(rowLimit, sendPacket, createStreamingResultSet(), this.firstCharOfStmt == 'S', true, metadataFromCache, false);
/*      */       }
/*      */       else
/*      */       {
/*  882 */         rs = executeInternal(-1, sendPacket, createStreamingResultSet(), this.firstCharOfStmt == 'S', true, metadataFromCache, false);
/*      */       }
/*      */       
/*      */ 
/*      */ 
/*  887 */       if (cachedMetadata != null) {
/*  888 */         locallyScopedConn.initializeResultsMetadataFromCache(this.originalSql, cachedMetadata, this.results);
/*      */ 
/*      */       }
/*  891 */       else if ((rs.reallyResult()) && (locallyScopedConn.getCacheResultSetMetadata())) {
/*  892 */         locallyScopedConn.initializeResultsMetadataFromCache(this.originalSql, null, rs);
/*      */       }
/*      */       
/*      */ 
/*      */ 
/*  897 */       if (this.retrieveGeneratedKeys) {
/*  898 */         locallyScopedConn.setReadInfoMsgEnabled(oldInfoMsgState);
/*  899 */         rs.setFirstCharOfQuery(this.firstCharOfStmt);
/*      */       }
/*      */       
/*  902 */       if (oldCatalog != null) {
/*  903 */         locallyScopedConn.setCatalog(oldCatalog);
/*      */       }
/*      */       
/*  906 */       this.lastInsertId = rs.getUpdateID();
/*      */       
/*  908 */       if (rs != null) {
/*  909 */         this.results = rs;
/*      */       }
/*      */     }
/*      */     
/*  913 */     return (rs != null) && (rs.reallyResult());
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
/*      */   public int[] executeBatch()
/*      */     throws SQLException
/*      */   {
/*  931 */     checkClosed();
/*      */     
/*  933 */     if (this.connection.isReadOnly()) {
/*  934 */       throw new SQLException(Messages.getString("PreparedStatement.25") + Messages.getString("PreparedStatement.26"), "S1009");
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*  939 */     synchronized (this.connection.getMutex()) {
/*  940 */       if ((this.batchedArgs == null) || (this.batchedArgs.size() == 0)) {
/*  941 */         return new int[0];
/*      */       }
/*      */       try
/*      */       {
/*  945 */         clearWarnings();
/*      */         
/*  947 */         if ((!this.batchHasPlainStatements) && (this.connection.getRewriteBatchedStatements()))
/*      */         {
/*      */ 
/*  950 */           if (canRewriteAsMultivalueInsertStatement()) {
/*  951 */             arrayOfInt = executeBatchedInserts();
/*      */             
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  965 */             clearBatch();return arrayOfInt;
/*      */           }
/*      */         }
/*  955 */         int[] arrayOfInt = executeBatchSerially();
/*      */         
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  965 */         clearBatch();return arrayOfInt;
/*      */       }
/*      */       catch (NullPointerException npe)
/*      */       {
/*  963 */         throw npe;
/*      */       } finally {
/*  965 */         clearBatch();
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */   public synchronized boolean canRewriteAsMultivalueInsertStatement() {
/*  971 */     if (!this.hasCheckedForRewrite)
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
/*  982 */       this.canRewrite = ((StringUtils.startsWithIgnoreCaseAndWs(this.originalSql, "INSERT", this.statementAfterCommentsPos)) && (StringUtils.indexOfIgnoreCaseRespectMarker(this.statementAfterCommentsPos, this.originalSql, "SELECT", "\"'`", "\"'`", false) == -1) && (StringUtils.indexOfIgnoreCaseRespectMarker(this.statementAfterCommentsPos, this.originalSql, "UPDATE", "\"'`", "\"'`", false) == -1));
/*      */       
/*      */ 
/*      */ 
/*      */ 
/*  987 */       this.hasCheckedForRewrite = true;
/*      */     }
/*      */     
/*  990 */     return this.canRewrite;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected int[] executeBatchedInserts()
/*      */     throws SQLException
/*      */   {
/* 1003 */     String valuesClause = extractValuesClause();
/*      */     
/* 1005 */     Connection locallyScopedConn = this.connection;
/*      */     
/* 1007 */     if (valuesClause == null) {
/* 1008 */       return executeBatchSerially();
/*      */     }
/*      */     
/* 1011 */     int numBatchedArgs = this.batchedArgs.size();
/*      */     
/* 1013 */     if (this.retrieveGeneratedKeys) {
/* 1014 */       this.batchedGeneratedKeys = new ArrayList(numBatchedArgs);
/*      */     }
/*      */     
/* 1017 */     int numValuesPerBatch = computeBatchSize(numBatchedArgs);
/*      */     
/* 1019 */     if (numBatchedArgs < numValuesPerBatch) {
/* 1020 */       numValuesPerBatch = numBatchedArgs;
/*      */     }
/*      */     
/* 1023 */     java.sql.PreparedStatement batchedStatement = null;
/*      */     
/* 1025 */     int batchedParamIndex = 1;
/* 1026 */     int updateCountRunningTotal = 0;
/* 1027 */     int numberToExecuteAsMultiValue = 0;
/* 1028 */     int batchCounter = 0;
/*      */     try
/*      */     {
/* 1031 */       if (this.retrieveGeneratedKeys) {
/* 1032 */         batchedStatement = locallyScopedConn.prepareStatement(generateBatchedInsertSQL(valuesClause, numValuesPerBatch), 1);
/*      */       }
/*      */       else
/*      */       {
/* 1036 */         batchedStatement = locallyScopedConn.prepareStatement(generateBatchedInsertSQL(valuesClause, numValuesPerBatch));
/*      */       }
/*      */       
/*      */ 
/*      */ 
/* 1041 */       if (numBatchedArgs < numValuesPerBatch) {
/* 1042 */         numberToExecuteAsMultiValue = numBatchedArgs;
/*      */       } else {
/* 1044 */         numberToExecuteAsMultiValue = numBatchedArgs / numValuesPerBatch;
/*      */       }
/*      */       
/* 1047 */       int numberArgsToExecute = numberToExecuteAsMultiValue * numValuesPerBatch;
/*      */       
/* 1049 */       for (int i = 0; i < numberArgsToExecute; i++) {
/* 1050 */         if ((i != 0) && (i % numValuesPerBatch == 0)) {
/* 1051 */           updateCountRunningTotal += batchedStatement.executeUpdate();
/*      */           
/* 1053 */           getBatchedGeneratedKeys(batchedStatement);
/* 1054 */           batchedStatement.clearParameters();
/* 1055 */           batchedParamIndex = 1;
/*      */         }
/*      */         
/*      */ 
/* 1059 */         batchedParamIndex = setOneBatchedParameterSet(batchedStatement, batchedParamIndex, this.batchedArgs.get(batchCounter++));
/*      */       }
/*      */       
/*      */ 
/*      */ 
/* 1064 */       updateCountRunningTotal += batchedStatement.executeUpdate();
/* 1065 */       getBatchedGeneratedKeys(batchedStatement);
/*      */       
/* 1067 */       numValuesPerBatch = numBatchedArgs - batchCounter;
/*      */     } finally {
/* 1069 */       if (batchedStatement != null) {
/* 1070 */         batchedStatement.close();
/*      */       }
/*      */     }
/*      */     try
/*      */     {
/* 1075 */       if (numValuesPerBatch > 0)
/*      */       {
/* 1077 */         if (this.retrieveGeneratedKeys) {
/* 1078 */           batchedStatement = locallyScopedConn.prepareStatement(generateBatchedInsertSQL(valuesClause, numValuesPerBatch), 1);
/*      */         }
/*      */         else
/*      */         {
/* 1082 */           batchedStatement = locallyScopedConn.prepareStatement(generateBatchedInsertSQL(valuesClause, numValuesPerBatch));
/*      */         }
/*      */         
/*      */ 
/* 1086 */         batchedParamIndex = 1;
/*      */         
/* 1088 */         while (batchCounter < numBatchedArgs) {
/* 1089 */           batchedParamIndex = setOneBatchedParameterSet(batchedStatement, batchedParamIndex, this.batchedArgs.get(batchCounter++));
/*      */         }
/*      */         
/*      */ 
/*      */ 
/* 1094 */         updateCountRunningTotal += batchedStatement.executeUpdate();
/* 1095 */         getBatchedGeneratedKeys(batchedStatement);
/*      */       }
/*      */       
/* 1098 */       int[] updateCounts = new int[this.batchedArgs.size()];
/*      */       
/* 1100 */       for (int i = 0; i < this.batchedArgs.size(); i++) {
/* 1101 */         updateCounts[i] = 1;
/*      */       }
/*      */       
/* 1104 */       return updateCounts;
/*      */     } finally {
/* 1106 */       if (batchedStatement != null) {
/* 1107 */         batchedStatement.close();
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
/*      */   protected int computeBatchSize(int numBatchedArgs)
/*      */   {
/* 1120 */     long[] combinedValues = computeMaxParameterSetSizeAndBatchSize(numBatchedArgs);
/*      */     
/* 1122 */     long maxSizeOfParameterSet = combinedValues[0];
/* 1123 */     long sizeOfEntireBatch = combinedValues[1];
/*      */     
/* 1125 */     int maxAllowedPacket = this.connection.getMaxAllowedPacket();
/*      */     
/* 1127 */     if (sizeOfEntireBatch < maxAllowedPacket - this.originalSql.length()) {
/* 1128 */       return numBatchedArgs;
/*      */     }
/*      */     
/* 1131 */     return (int)Math.max(1L, (maxAllowedPacket - this.originalSql.length()) / maxSizeOfParameterSet);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   protected long[] computeMaxParameterSetSizeAndBatchSize(int numBatchedArgs)
/*      */   {
/* 1139 */     long sizeOfEntireBatch = 0L;
/* 1140 */     long maxSizeOfParameterSet = 0L;
/*      */     
/* 1142 */     for (int i = 0; i < numBatchedArgs; i++) {
/* 1143 */       BatchParams paramArg = (BatchParams)this.batchedArgs.get(i);
/*      */       
/*      */ 
/* 1146 */       boolean[] isNullBatch = paramArg.isNull;
/* 1147 */       boolean[] isStreamBatch = paramArg.isStream;
/*      */       
/* 1149 */       long sizeOfParameterSet = 0L;
/*      */       
/* 1151 */       for (int j = 0; j < isNullBatch.length; j++) {
/* 1152 */         if (isNullBatch[j] == 0)
/*      */         {
/* 1154 */           if (isStreamBatch[j] != 0) {
/* 1155 */             int streamLength = paramArg.streamLengths[j];
/*      */             
/* 1157 */             if (streamLength != -1) {
/* 1158 */               sizeOfParameterSet += streamLength * 2;
/*      */             }
/*      */           } else {
/* 1161 */             sizeOfParameterSet += paramArg.parameterStrings[j].length;
/*      */           }
/*      */         } else {
/* 1164 */           sizeOfParameterSet += 4L;
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
/* 1176 */       sizeOfParameterSet += this.batchedValuesClause.length() + 1;
/* 1177 */       sizeOfEntireBatch += sizeOfParameterSet;
/*      */       
/* 1179 */       if (sizeOfParameterSet > maxSizeOfParameterSet) {
/* 1180 */         maxSizeOfParameterSet = sizeOfParameterSet;
/*      */       }
/*      */     }
/*      */     
/* 1184 */     return new long[] { maxSizeOfParameterSet, sizeOfEntireBatch };
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected int[] executeBatchSerially()
/*      */     throws SQLException
/*      */   {
/* 1196 */     Connection locallyScopedConn = this.connection;
/*      */     
/* 1198 */     if (locallyScopedConn == null) {
/* 1199 */       checkClosed();
/*      */     }
/*      */     
/* 1202 */     int[] updateCounts = null;
/*      */     
/* 1204 */     if (this.batchedArgs != null) {
/* 1205 */       int nbrCommands = this.batchedArgs.size();
/* 1206 */       updateCounts = new int[nbrCommands];
/*      */       
/* 1208 */       for (int i = 0; i < nbrCommands; i++) {
/* 1209 */         updateCounts[i] = -3;
/*      */       }
/*      */       
/* 1212 */       SQLException sqlEx = null;
/*      */       
/* 1214 */       int commandIndex = 0;
/*      */       
/* 1216 */       if (this.retrieveGeneratedKeys) {
/* 1217 */         this.batchedGeneratedKeys = new ArrayList(nbrCommands);
/*      */       }
/*      */       
/* 1220 */       for (commandIndex = 0; commandIndex < nbrCommands; commandIndex++) {
/* 1221 */         Object arg = this.batchedArgs.get(commandIndex);
/*      */         
/* 1223 */         if ((arg instanceof String)) {
/* 1224 */           updateCounts[commandIndex] = executeUpdate((String)arg);
/*      */         } else {
/* 1226 */           BatchParams paramArg = (BatchParams)arg;
/*      */           try
/*      */           {
/* 1229 */             updateCounts[commandIndex] = executeUpdate(paramArg.parameterStrings, paramArg.parameterStreams, paramArg.isStream, paramArg.streamLengths, paramArg.isNull, true);
/*      */             
/*      */ 
/*      */ 
/*      */ 
/* 1234 */             if (this.retrieveGeneratedKeys) {
/* 1235 */               java.sql.ResultSet rs = null;
/*      */               try
/*      */               {
/* 1238 */                 rs = getGeneratedKeysInternal();
/*      */                 
/* 1240 */                 while (rs.next()) {
/* 1241 */                   this.batchedGeneratedKeys.add(new byte[][] { rs.getBytes(1) });
/*      */                 }
/*      */               }
/*      */               finally {
/* 1245 */                 if (rs != null) {
/* 1246 */                   rs.close();
/*      */                 }
/*      */               }
/*      */             }
/*      */           } catch (SQLException ex) {
/* 1251 */             updateCounts[commandIndex] = -3;
/*      */             
/* 1253 */             if (this.continueBatchOnError) {
/* 1254 */               sqlEx = ex;
/*      */             } else {
/* 1256 */               int[] newUpdateCounts = new int[commandIndex];
/* 1257 */               System.arraycopy(updateCounts, 0, newUpdateCounts, 0, commandIndex);
/*      */               
/*      */ 
/* 1260 */               throw new BatchUpdateException(ex.getMessage(), ex.getSQLState(), ex.getErrorCode(), newUpdateCounts);
/*      */             }
/*      */           }
/*      */         }
/*      */       }
/*      */       
/*      */ 
/*      */ 
/* 1268 */       if (sqlEx != null) {
/* 1269 */         throw new BatchUpdateException(sqlEx.getMessage(), sqlEx.getSQLState(), sqlEx.getErrorCode(), updateCounts);
/*      */       }
/*      */     }
/*      */     
/*      */ 
/* 1274 */     return updateCounts != null ? updateCounts : new int[0];
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
/*      */   protected ResultSet executeInternal(int maxRowsToRetrieve, Buffer sendPacket, boolean createStreamingResultSet, boolean queryIsSelectOnly, boolean unpackFields, Field[] cachedFields, boolean isBatch)
/*      */     throws SQLException
/*      */   {
/*      */     try
/*      */     {
/* 1305 */       synchronized (this.cancelTimeoutMutex) {
/* 1306 */         this.wasCancelled = false;
/*      */       }
/*      */       
/* 1309 */       Connection locallyScopedConnection = this.connection;
/*      */       
/* 1311 */       this.numberOfExecutions += 1;
/*      */       
/* 1313 */       if (this.doPingInstead) {
/* 1314 */         doPingInstead();
/*      */         
/* 1316 */         return this.results;
/*      */       }
/*      */       
/*      */ 
/*      */ 
/* 1321 */       Statement.CancelTask timeoutTask = null;
/*      */       ResultSet rs;
/*      */       try {
/* 1324 */         if ((locallyScopedConnection.getEnableQueryTimeouts()) && (this.timeoutInMillis != 0) && (locallyScopedConnection.versionMeetsMinimum(5, 0, 0)))
/*      */         {
/*      */ 
/* 1327 */           timeoutTask = new Statement.CancelTask(this);
/* 1328 */           Connection.getCancelTimer().schedule(timeoutTask, this.timeoutInMillis);
/*      */         }
/*      */         
/*      */ 
/* 1332 */         rs = locallyScopedConnection.execSQL(this, null, maxRowsToRetrieve, sendPacket, this.resultSetType, this.resultSetConcurrency, createStreamingResultSet, this.currentCatalog, unpackFields, isBatch);
/*      */         
/*      */ 
/*      */ 
/*      */ 
/* 1337 */         if (timeoutTask != null) {
/* 1338 */           timeoutTask.cancel();
/*      */           
/* 1340 */           if (timeoutTask.caughtWhileCancelling != null) {
/* 1341 */             throw timeoutTask.caughtWhileCancelling;
/*      */           }
/*      */           
/* 1344 */           timeoutTask = null;
/*      */         }
/*      */         
/* 1347 */         synchronized (this.cancelTimeoutMutex) {
/* 1348 */           if (this.wasCancelled) {
/* 1349 */             this.wasCancelled = false;
/* 1350 */             throw new MySQLTimeoutException();
/*      */           }
/*      */         }
/*      */       } finally {
/* 1354 */         if (timeoutTask != null) {
/* 1355 */           timeoutTask.cancel();
/*      */         }
/*      */       }
/*      */       
/* 1359 */       return rs;
/*      */     } catch (NullPointerException npe) {
/* 1361 */       checkClosed();
/*      */       
/*      */ 
/*      */ 
/* 1365 */       throw npe;
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
/*      */   public java.sql.ResultSet executeQuery()
/*      */     throws SQLException
/*      */   {
/* 1379 */     checkClosed();
/*      */     
/* 1381 */     Connection locallyScopedConn = this.connection;
/*      */     
/* 1383 */     checkForDml(this.originalSql, this.firstCharOfStmt);
/*      */     
/* 1385 */     CachedResultSetMetaData cachedMetadata = null;
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 1391 */     synchronized (locallyScopedConn.getMutex()) {
/* 1392 */       clearWarnings();
/*      */       
/* 1394 */       this.batchedGeneratedKeys = null;
/*      */       
/* 1396 */       Buffer sendPacket = fillSendPacket();
/*      */       
/* 1398 */       if ((this.results != null) && 
/* 1399 */         (!this.connection.getHoldResultsOpenOverStatementClose()) && 
/* 1400 */         (!this.holdResultsOpenOverClose)) {
/* 1401 */         this.results.realClose(false);
/*      */       }
/*      */       
/*      */ 
/*      */ 
/* 1406 */       String oldCatalog = null;
/*      */       
/* 1408 */       if (!locallyScopedConn.getCatalog().equals(this.currentCatalog)) {
/* 1409 */         oldCatalog = locallyScopedConn.getCatalog();
/* 1410 */         locallyScopedConn.setCatalog(this.currentCatalog);
/*      */       }
/*      */       
/*      */ 
/*      */ 
/*      */ 
/* 1416 */       if (locallyScopedConn.getCacheResultSetMetadata()) {
/* 1417 */         cachedMetadata = locallyScopedConn.getCachedMetaData(this.originalSql);
/*      */       }
/*      */       
/* 1420 */       Field[] metadataFromCache = null;
/*      */       
/* 1422 */       if (cachedMetadata != null) {
/* 1423 */         metadataFromCache = cachedMetadata.fields;
/*      */       }
/*      */       
/* 1426 */       if (locallyScopedConn.useMaxRows())
/*      */       {
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 1433 */         if (this.hasLimitClause) {
/* 1434 */           this.results = executeInternal(this.maxRows, sendPacket, createStreamingResultSet(), true, cachedMetadata == null, metadataFromCache, false);
/*      */         }
/*      */         else
/*      */         {
/* 1438 */           if (this.maxRows <= 0) {
/* 1439 */             locallyScopedConn.execSQL(this, "SET OPTION SQL_SELECT_LIMIT=DEFAULT", -1, null, 1003, 1007, false, this.currentCatalog, true);
/*      */ 
/*      */ 
/*      */           }
/*      */           else
/*      */           {
/*      */ 
/*      */ 
/* 1447 */             locallyScopedConn.execSQL(this, "SET OPTION SQL_SELECT_LIMIT=" + this.maxRows, -1, null, 1003, 1007, false, this.currentCatalog, true);
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
/* 1458 */           this.results = executeInternal(-1, sendPacket, createStreamingResultSet(), true, cachedMetadata == null, metadataFromCache, false);
/*      */           
/*      */ 
/*      */ 
/* 1462 */           if (oldCatalog != null) {
/* 1463 */             this.connection.setCatalog(oldCatalog);
/*      */           }
/*      */         }
/*      */       } else {
/* 1467 */         this.results = executeInternal(-1, sendPacket, createStreamingResultSet(), true, cachedMetadata == null, metadataFromCache, false);
/*      */       }
/*      */       
/*      */ 
/*      */ 
/* 1472 */       if (oldCatalog != null) {
/* 1473 */         locallyScopedConn.setCatalog(oldCatalog);
/*      */       }
/*      */       
/* 1476 */       if (cachedMetadata != null) {
/* 1477 */         locallyScopedConn.initializeResultsMetadataFromCache(this.originalSql, cachedMetadata, this.results);
/*      */ 
/*      */       }
/* 1480 */       else if (locallyScopedConn.getCacheResultSetMetadata()) {
/* 1481 */         locallyScopedConn.initializeResultsMetadataFromCache(this.originalSql, null, this.results);
/*      */       }
/*      */     }
/*      */     
/*      */ 
/*      */ 
/* 1487 */     this.lastInsertId = this.results.getUpdateID();
/*      */     
/* 1489 */     return this.results;
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
/*      */   public int executeUpdate()
/*      */     throws SQLException
/*      */   {
/* 1504 */     return executeUpdate(true, false);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected int executeUpdate(boolean clearBatchedGeneratedKeysAndWarnings, boolean isBatch)
/*      */     throws SQLException
/*      */   {
/* 1514 */     if (clearBatchedGeneratedKeysAndWarnings) {
/* 1515 */       clearWarnings();
/* 1516 */       this.batchedGeneratedKeys = null;
/*      */     }
/*      */     
/* 1519 */     return executeUpdate(this.parameterValues, this.parameterStreams, this.isStream, this.streamLengths, this.isNull, isBatch);
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
/*      */   protected int executeUpdate(byte[][] batchedParameterStrings, InputStream[] batchedParameterStreams, boolean[] batchedIsStream, int[] batchedStreamLengths, boolean[] batchedIsNull, boolean isReallyBatch)
/*      */     throws SQLException
/*      */   {
/* 1547 */     checkClosed();
/*      */     
/* 1549 */     Connection locallyScopedConn = this.connection;
/*      */     
/* 1551 */     if (locallyScopedConn.isReadOnly()) {
/* 1552 */       throw SQLError.createSQLException(Messages.getString("PreparedStatement.34") + Messages.getString("PreparedStatement.35"), "S1009");
/*      */     }
/*      */     
/*      */ 
/*      */ 
/* 1557 */     if ((this.firstCharOfStmt == 'S') && (isSelectQuery()))
/*      */     {
/* 1559 */       throw SQLError.createSQLException(Messages.getString("PreparedStatement.37"), "01S03");
/*      */     }
/*      */     
/*      */ 
/* 1563 */     if ((this.results != null) && 
/* 1564 */       (!locallyScopedConn.getHoldResultsOpenOverStatementClose())) {
/* 1565 */       this.results.realClose(false);
/*      */     }
/*      */     
/*      */ 
/* 1569 */     ResultSet rs = null;
/*      */     
/*      */ 
/*      */ 
/*      */ 
/* 1574 */     synchronized (locallyScopedConn.getMutex()) {
/* 1575 */       Buffer sendPacket = fillSendPacket(batchedParameterStrings, batchedParameterStreams, batchedIsStream, batchedStreamLengths);
/*      */       
/*      */ 
/*      */ 
/* 1579 */       String oldCatalog = null;
/*      */       
/* 1581 */       if (!locallyScopedConn.getCatalog().equals(this.currentCatalog)) {
/* 1582 */         oldCatalog = locallyScopedConn.getCatalog();
/* 1583 */         locallyScopedConn.setCatalog(this.currentCatalog);
/*      */       }
/*      */       
/*      */ 
/*      */ 
/*      */ 
/* 1589 */       if (locallyScopedConn.useMaxRows()) {
/* 1590 */         locallyScopedConn.execSQL(this, "SET OPTION SQL_SELECT_LIMIT=DEFAULT", -1, null, 1003, 1007, false, this.currentCatalog, true);
/*      */       }
/*      */       
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 1597 */       boolean oldInfoMsgState = false;
/*      */       
/* 1599 */       if (this.retrieveGeneratedKeys) {
/* 1600 */         oldInfoMsgState = locallyScopedConn.isReadInfoMsgEnabled();
/* 1601 */         locallyScopedConn.setReadInfoMsgEnabled(true);
/*      */       }
/*      */       
/* 1604 */       rs = executeInternal(-1, sendPacket, false, false, true, null, isReallyBatch);
/*      */       
/*      */ 
/* 1607 */       if (this.retrieveGeneratedKeys) {
/* 1608 */         locallyScopedConn.setReadInfoMsgEnabled(oldInfoMsgState);
/* 1609 */         rs.setFirstCharOfQuery(this.firstCharOfStmt);
/*      */       }
/*      */       
/* 1612 */       if (oldCatalog != null) {
/* 1613 */         locallyScopedConn.setCatalog(oldCatalog);
/*      */       }
/*      */     }
/*      */     
/* 1617 */     this.results = rs;
/*      */     
/* 1619 */     this.updateCount = rs.getUpdateCount();
/*      */     
/* 1621 */     int truncatedUpdateCount = 0;
/*      */     
/* 1623 */     if (this.updateCount > 2147483647L) {
/* 1624 */       truncatedUpdateCount = Integer.MAX_VALUE;
/*      */     } else {
/* 1626 */       truncatedUpdateCount = (int)this.updateCount;
/*      */     }
/*      */     
/* 1629 */     this.lastInsertId = rs.getUpdateID();
/*      */     
/* 1631 */     return truncatedUpdateCount;
/*      */   }
/*      */   
/*      */   private String extractValuesClause() throws SQLException {
/* 1635 */     if (this.batchedValuesClause == null) {
/* 1636 */       String quoteCharStr = this.connection.getMetaData().getIdentifierQuoteString();
/*      */       
/*      */ 
/* 1639 */       int indexOfValues = -1;
/*      */       
/* 1641 */       if (quoteCharStr.length() > 0) {
/* 1642 */         indexOfValues = StringUtils.indexOfIgnoreCaseRespectQuotes(this.statementAfterCommentsPos, this.originalSql, "VALUES ", quoteCharStr.charAt(0), false);
/*      */       }
/*      */       else
/*      */       {
/* 1646 */         indexOfValues = StringUtils.indexOfIgnoreCase(this.statementAfterCommentsPos, this.originalSql, "VALUES ");
/*      */       }
/*      */       
/*      */ 
/*      */ 
/* 1651 */       if (indexOfValues == -1) {
/* 1652 */         return null;
/*      */       }
/*      */       
/* 1655 */       int indexOfFirstParen = this.originalSql.indexOf('(', indexOfValues + 7);
/*      */       
/*      */ 
/* 1658 */       if (indexOfFirstParen == -1) {
/* 1659 */         return null;
/*      */       }
/*      */       
/* 1662 */       int indexOfLastParen = this.originalSql.lastIndexOf(')');
/*      */       
/* 1664 */       if (indexOfLastParen == -1) {
/* 1665 */         return null;
/*      */       }
/*      */       
/* 1668 */       this.batchedValuesClause = this.originalSql.substring(indexOfFirstParen, indexOfLastParen + 1);
/*      */     }
/*      */     
/*      */ 
/* 1672 */     return this.batchedValuesClause;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected Buffer fillSendPacket()
/*      */     throws SQLException
/*      */   {
/* 1685 */     return fillSendPacket(this.parameterValues, this.parameterStreams, this.isStream, this.streamLengths);
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
/*      */   protected Buffer fillSendPacket(byte[][] batchedParameterStrings, InputStream[] batchedParameterStreams, boolean[] batchedIsStream, int[] batchedStreamLengths)
/*      */     throws SQLException
/*      */   {
/* 1709 */     Buffer sendPacket = this.connection.getIO().getSharedSendPacket();
/*      */     
/* 1711 */     sendPacket.clear();
/*      */     
/* 1713 */     sendPacket.writeByte((byte)3);
/*      */     
/* 1715 */     boolean useStreamLengths = this.connection.getUseStreamLengthsInPrepStmts();
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 1722 */     int ensurePacketSize = 0;
/*      */     
/* 1724 */     for (int i = 0; i < batchedParameterStrings.length; i++) {
/* 1725 */       if ((batchedIsStream[i] != 0) && (useStreamLengths)) {
/* 1726 */         ensurePacketSize += batchedStreamLengths[i];
/*      */       }
/*      */     }
/*      */     
/* 1730 */     if (ensurePacketSize != 0) {
/* 1731 */       sendPacket.ensureCapacity(ensurePacketSize);
/*      */     }
/*      */     
/* 1734 */     for (int i = 0; i < batchedParameterStrings.length; i++) {
/* 1735 */       if ((batchedParameterStrings[i] == null) && (batchedParameterStreams[i] == null))
/*      */       {
/* 1737 */         throw SQLError.createSQLException(Messages.getString("PreparedStatement.40") + (i + 1), "07001");
/*      */       }
/*      */       
/*      */ 
/*      */ 
/* 1742 */       sendPacket.writeBytesNoNull(this.staticSqlStrings[i]);
/*      */       
/* 1744 */       if (batchedIsStream[i] != 0) {
/* 1745 */         streamToBytes(sendPacket, batchedParameterStreams[i], true, batchedStreamLengths[i], useStreamLengths);
/*      */       }
/*      */       else {
/* 1748 */         sendPacket.writeBytesNoNull(batchedParameterStrings[i]);
/*      */       }
/*      */     }
/*      */     
/* 1752 */     sendPacket.writeBytesNoNull(this.staticSqlStrings[batchedParameterStrings.length]);
/*      */     
/*      */ 
/* 1755 */     return sendPacket;
/*      */   }
/*      */   
/*      */   private String generateBatchedInsertSQL(String valuesClause, int numBatches) {
/* 1759 */     StringBuffer newStatementSql = new StringBuffer(this.originalSql.length() + numBatches * (valuesClause.length() + 1));
/*      */     
/*      */ 
/*      */ 
/* 1763 */     newStatementSql.append(this.originalSql);
/*      */     
/* 1765 */     for (int i = 0; i < numBatches - 1; i++) {
/* 1766 */       newStatementSql.append(',');
/* 1767 */       newStatementSql.append(valuesClause);
/*      */     }
/*      */     
/* 1770 */     return newStatementSql.toString();
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
/*      */   public byte[] getBytesRepresentation(int parameterIndex)
/*      */     throws SQLException
/*      */   {
/* 1786 */     if (this.isStream[parameterIndex] != 0) {
/* 1787 */       return streamToBytes(this.parameterStreams[parameterIndex], false, this.streamLengths[parameterIndex], this.connection.getUseStreamLengthsInPrepStmts());
/*      */     }
/*      */     
/*      */ 
/*      */ 
/* 1792 */     byte[] parameterVal = this.parameterValues[parameterIndex];
/*      */     
/* 1794 */     if (parameterVal == null) {
/* 1795 */       return null;
/*      */     }
/*      */     
/* 1798 */     if ((parameterVal[0] == 39) && (parameterVal[(parameterVal.length - 1)] == 39))
/*      */     {
/* 1800 */       byte[] valNoQuotes = new byte[parameterVal.length - 2];
/* 1801 */       System.arraycopy(parameterVal, 1, valNoQuotes, 0, parameterVal.length - 2);
/*      */       
/*      */ 
/* 1804 */       return valNoQuotes;
/*      */     }
/*      */     
/* 1807 */     return parameterVal;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   private final String getDateTimePattern(String dt, boolean toTime)
/*      */     throws Exception
/*      */   {
/* 1817 */     int dtLength = dt != null ? dt.length() : 0;
/*      */     
/* 1819 */     if ((dtLength >= 8) && (dtLength <= 10)) {
/* 1820 */       int dashCount = 0;
/* 1821 */       boolean isDateOnly = true;
/*      */       
/* 1823 */       for (int i = 0; i < dtLength; i++) {
/* 1824 */         char c = dt.charAt(i);
/*      */         
/* 1826 */         if ((!Character.isDigit(c)) && (c != '-')) {
/* 1827 */           isDateOnly = false;
/*      */           
/* 1829 */           break;
/*      */         }
/*      */         
/* 1832 */         if (c == '-') {
/* 1833 */           dashCount++;
/*      */         }
/*      */       }
/*      */       
/* 1837 */       if ((isDateOnly) && (dashCount == 2)) {
/* 1838 */         return "yyyy-MM-dd";
/*      */       }
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/* 1845 */     boolean colonsOnly = true;
/*      */     
/* 1847 */     for (int i = 0; i < dtLength; i++) {
/* 1848 */       char c = dt.charAt(i);
/*      */       
/* 1850 */       if ((!Character.isDigit(c)) && (c != ':')) {
/* 1851 */         colonsOnly = false;
/*      */         
/* 1853 */         break;
/*      */       }
/*      */     }
/*      */     
/* 1857 */     if (colonsOnly) {
/* 1858 */       return "HH:mm:ss";
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 1867 */     StringReader reader = new StringReader(dt + " ");
/* 1868 */     ArrayList vec = new ArrayList();
/* 1869 */     ArrayList vecRemovelist = new ArrayList();
/* 1870 */     Object[] nv = new Object[3];
/*      */     
/* 1872 */     nv[0] = new Character('y');
/* 1873 */     nv[1] = new StringBuffer();
/* 1874 */     nv[2] = new Integer(0);
/* 1875 */     vec.add(nv);
/*      */     
/* 1877 */     if (toTime) {
/* 1878 */       nv = new Object[3];
/* 1879 */       nv[0] = new Character('h');
/* 1880 */       nv[1] = new StringBuffer();
/* 1881 */       nv[2] = new Integer(0);
/* 1882 */       vec.add(nv);
/*      */     }
/*      */     int z;
/* 1885 */     while ((z = reader.read()) != -1) {
/* 1886 */       char separator = (char)z;
/* 1887 */       int maxvecs = vec.size();
/*      */       
/* 1889 */       for (int count = 0; count < maxvecs; count++) {
/* 1890 */         Object[] v = (Object[])vec.get(count);
/* 1891 */         int n = ((Integer)v[2]).intValue();
/* 1892 */         char c = getSuccessor(((Character)v[0]).charValue(), n);
/*      */         
/* 1894 */         if (!Character.isLetterOrDigit(separator)) {
/* 1895 */           if ((c == ((Character)v[0]).charValue()) && (c != 'S')) {
/* 1896 */             vecRemovelist.add(v);
/*      */           } else {
/* 1898 */             ((StringBuffer)v[1]).append(separator);
/*      */             
/* 1900 */             if ((c == 'X') || (c == 'Y')) {
/* 1901 */               v[2] = new Integer(4);
/*      */             }
/*      */           }
/*      */         } else {
/* 1905 */           if (c == 'X') {
/* 1906 */             c = 'y';
/* 1907 */             nv = new Object[3];
/* 1908 */             nv[1] = new StringBuffer(((StringBuffer)v[1]).toString()).append('M');
/*      */             
/* 1910 */             nv[0] = new Character('M');
/* 1911 */             nv[2] = new Integer(1);
/* 1912 */             vec.add(nv);
/* 1913 */           } else if (c == 'Y') {
/* 1914 */             c = 'M';
/* 1915 */             nv = new Object[3];
/* 1916 */             nv[1] = new StringBuffer(((StringBuffer)v[1]).toString()).append('d');
/*      */             
/* 1918 */             nv[0] = new Character('d');
/* 1919 */             nv[2] = new Integer(1);
/* 1920 */             vec.add(nv);
/*      */           }
/*      */           
/* 1923 */           ((StringBuffer)v[1]).append(c);
/*      */           
/* 1925 */           if (c == ((Character)v[0]).charValue()) {
/* 1926 */             v[2] = new Integer(n + 1);
/*      */           } else {
/* 1928 */             v[0] = new Character(c);
/* 1929 */             v[2] = new Integer(1);
/*      */           }
/*      */         }
/*      */       }
/*      */       
/* 1934 */       int size = vecRemovelist.size();
/*      */       
/* 1936 */       for (int i = 0; i < size; i++) {
/* 1937 */         Object[] v = (Object[])vecRemovelist.get(i);
/* 1938 */         vec.remove(v);
/*      */       }
/*      */       
/* 1941 */       vecRemovelist.clear();
/*      */     }
/*      */     
/* 1944 */     int size = vec.size();
/*      */     
/* 1946 */     for (int i = 0; i < size; i++) {
/* 1947 */       Object[] v = (Object[])vec.get(i);
/* 1948 */       char c = ((Character)v[0]).charValue();
/* 1949 */       int n = ((Integer)v[2]).intValue();
/*      */       
/* 1951 */       boolean bk = getSuccessor(c, n) != c;
/* 1952 */       boolean atEnd = ((c == 's') || (c == 'm') || ((c == 'h') && (toTime))) && (bk);
/* 1953 */       boolean finishesAtDate = (bk) && (c == 'd') && (!toTime);
/* 1954 */       boolean containsEnd = ((StringBuffer)v[1]).toString().indexOf('W') != -1;
/*      */       
/*      */ 
/* 1957 */       if (((!atEnd) && (!finishesAtDate)) || (containsEnd)) {
/* 1958 */         vecRemovelist.add(v);
/*      */       }
/*      */     }
/*      */     
/* 1962 */     size = vecRemovelist.size();
/*      */     
/* 1964 */     for (int i = 0; i < size; i++) {
/* 1965 */       vec.remove(vecRemovelist.get(i));
/*      */     }
/*      */     
/* 1968 */     vecRemovelist.clear();
/* 1969 */     Object[] v = (Object[])vec.get(0);
/*      */     
/* 1971 */     StringBuffer format = (StringBuffer)v[1];
/* 1972 */     format.setLength(format.length() - 1);
/*      */     
/* 1974 */     return format.toString();
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
/*      */   public java.sql.ResultSetMetaData getMetaData()
/*      */     throws SQLException
/*      */   {
/* 2000 */     if (!isSelectQuery()) {
/* 2001 */       return null;
/*      */     }
/*      */     
/* 2004 */     PreparedStatement mdStmt = null;
/* 2005 */     java.sql.ResultSet mdRs = null;
/*      */     
/* 2007 */     if (this.pstmtResultMetaData == null) {
/*      */       try {
/* 2009 */         mdStmt = new PreparedStatement(this.connection, this.originalSql, this.currentCatalog, this.parseInfo);
/*      */         
/*      */ 
/* 2012 */         mdStmt.setMaxRows(0);
/*      */         
/* 2014 */         int paramCount = this.parameterValues.length;
/*      */         
/* 2016 */         for (int i = 1; i <= paramCount; i++) {
/* 2017 */           mdStmt.setString(i, "");
/*      */         }
/*      */         
/* 2020 */         boolean hadResults = mdStmt.execute();
/*      */         
/* 2022 */         if (hadResults) {
/* 2023 */           mdRs = mdStmt.getResultSet();
/*      */           
/* 2025 */           this.pstmtResultMetaData = mdRs.getMetaData();
/*      */         } else {
/* 2027 */           this.pstmtResultMetaData = new ResultSetMetaData(new Field[0], this.connection.getUseOldAliasMetadataBehavior());
/*      */         }
/*      */       }
/*      */       finally
/*      */       {
/* 2032 */         SQLException sqlExRethrow = null;
/*      */         
/* 2034 */         if (mdRs != null) {
/*      */           try {
/* 2036 */             mdRs.close();
/*      */           } catch (SQLException sqlEx) {
/* 2038 */             sqlExRethrow = sqlEx;
/*      */           }
/*      */           
/* 2041 */           mdRs = null;
/*      */         }
/*      */         
/* 2044 */         if (mdStmt != null) {
/*      */           try {
/* 2046 */             mdStmt.close();
/*      */           } catch (SQLException sqlEx) {
/* 2048 */             sqlExRethrow = sqlEx;
/*      */           }
/*      */           
/* 2051 */           mdStmt = null;
/*      */         }
/*      */         
/* 2054 */         if (sqlExRethrow != null) {
/* 2055 */           throw sqlExRethrow;
/*      */         }
/*      */       }
/*      */     }
/*      */     
/* 2060 */     return this.pstmtResultMetaData;
/*      */   }
/*      */   
/*      */   protected boolean isSelectQuery() {
/* 2064 */     return StringUtils.startsWithIgnoreCaseAndWs(StringUtils.stripComments(this.originalSql, "'\"", "'\"", true, false, true, true), "SELECT");
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public ParameterMetaData getParameterMetaData()
/*      */     throws SQLException
/*      */   {
/* 2075 */     if (this.parameterMetaData == null) {
/* 2076 */       if (this.connection.getGenerateSimpleParameterMetadata()) {
/* 2077 */         this.parameterMetaData = new MysqlParameterMetadata(this.parameterCount);
/*      */       } else {
/* 2079 */         this.parameterMetaData = new MysqlParameterMetadata(null, this.parameterCount);
/*      */       }
/*      */     }
/*      */     
/*      */ 
/* 2084 */     return this.parameterMetaData;
/*      */   }
/*      */   
/*      */   ParseInfo getParseInfo() {
/* 2088 */     return this.parseInfo;
/*      */   }
/*      */   
/*      */   private final char getSuccessor(char c, int n) {
/* 2092 */     return (c == 's') && (n < 2) ? 's' : c == 'm' ? 's' : (c == 'm') && (n < 2) ? 'm' : c == 'H' ? 'm' : (c == 'H') && (n < 2) ? 'H' : c == 'd' ? 'H' : (c == 'd') && (n < 2) ? 'd' : c == 'M' ? 'd' : (c == 'M') && (n < 3) ? 'M' : (c == 'M') && (n == 2) ? 'Y' : c == 'y' ? 'M' : (c == 'y') && (n < 4) ? 'y' : (c == 'y') && (n == 2) ? 'X' : 'W';
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
/*      */   private final void hexEscapeBlock(byte[] buf, Buffer packet, int size)
/*      */     throws SQLException
/*      */   {
/* 2118 */     for (int i = 0; i < size; i++) {
/* 2119 */       byte b = buf[i];
/* 2120 */       int lowBits = (b & 0xFF) / 16;
/* 2121 */       int highBits = (b & 0xFF) % 16;
/*      */       
/* 2123 */       packet.writeByte(HEX_DIGITS[lowBits]);
/* 2124 */       packet.writeByte(HEX_DIGITS[highBits]);
/*      */     }
/*      */   }
/*      */   
/*      */   private void initializeFromParseInfo() throws SQLException {
/* 2129 */     this.staticSqlStrings = this.parseInfo.staticSql;
/* 2130 */     this.hasLimitClause = this.parseInfo.foundLimitClause;
/* 2131 */     this.isLoadDataQuery = this.parseInfo.foundLoadData;
/* 2132 */     this.firstCharOfStmt = this.parseInfo.firstStmtChar;
/*      */     
/* 2134 */     this.parameterCount = (this.staticSqlStrings.length - 1);
/*      */     
/* 2136 */     this.parameterValues = new byte[this.parameterCount][];
/* 2137 */     this.parameterStreams = new InputStream[this.parameterCount];
/* 2138 */     this.isStream = new boolean[this.parameterCount];
/* 2139 */     this.streamLengths = new int[this.parameterCount];
/* 2140 */     this.isNull = new boolean[this.parameterCount];
/*      */     
/* 2142 */     clearParameters();
/*      */     
/* 2144 */     for (int j = 0; j < this.parameterCount; j++) {
/* 2145 */       this.isStream[j] = false;
/*      */     }
/*      */     
/* 2148 */     this.statementAfterCommentsPos = this.parseInfo.statementStartPos;
/*      */   }
/*      */   
/*      */   boolean isNull(int paramIndex) {
/* 2152 */     return this.isNull[paramIndex];
/*      */   }
/*      */   
/*      */   private final int readblock(InputStream i, byte[] b) throws SQLException {
/*      */     try {
/* 2157 */       return i.read(b);
/*      */     } catch (Throwable E) {
/* 2159 */       throw SQLError.createSQLException(Messages.getString("PreparedStatement.56") + E.getClass().getName(), "S1000");
/*      */     }
/*      */   }
/*      */   
/*      */   private final int readblock(InputStream i, byte[] b, int length) throws SQLException
/*      */   {
/*      */     try
/*      */     {
/* 2167 */       int lengthToRead = length;
/*      */       
/* 2169 */       if (lengthToRead > b.length) {
/* 2170 */         lengthToRead = b.length;
/*      */       }
/*      */       
/* 2173 */       return i.read(b, 0, lengthToRead);
/*      */     } catch (Throwable E) {
/* 2175 */       throw SQLError.createSQLException(Messages.getString("PreparedStatement.55") + E.getClass().getName(), "S1000");
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
/*      */   protected void realClose(boolean calledExplicitly, boolean closeOpenResults)
/*      */     throws SQLException
/*      */   {
/* 2191 */     if ((this.useUsageAdvisor) && 
/* 2192 */       (this.numberOfExecutions <= 1)) {
/* 2193 */       String message = Messages.getString("PreparedStatement.43");
/*      */       
/* 2195 */       this.eventSink.consumeEvent(new ProfilerEvent((byte)0, "", this.currentCatalog, this.connectionId, getId(), -1, System.currentTimeMillis(), 0L, Constants.MILLIS_I18N, null, this.pointOfOrigin, message));
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 2204 */     super.realClose(calledExplicitly, closeOpenResults);
/*      */     
/* 2206 */     this.dbmd = null;
/* 2207 */     this.originalSql = null;
/* 2208 */     this.staticSqlStrings = ((byte[][])null);
/* 2209 */     this.parameterValues = ((byte[][])null);
/* 2210 */     this.parameterStreams = null;
/* 2211 */     this.isStream = null;
/* 2212 */     this.streamLengths = null;
/* 2213 */     this.isNull = null;
/* 2214 */     this.streamConvertBuf = null;
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
/*      */   public void setArray(int i, Array x)
/*      */     throws SQLException
/*      */   {
/* 2231 */     throw new NotImplemented();
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
/*      */   public void setAsciiStream(int parameterIndex, InputStream x, int length)
/*      */     throws SQLException
/*      */   {
/* 2258 */     if (x == null) {
/* 2259 */       setNull(parameterIndex, 12);
/*      */     } else {
/* 2261 */       setBinaryStream(parameterIndex, x, length);
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
/*      */   public void setBigDecimal(int parameterIndex, BigDecimal x)
/*      */     throws SQLException
/*      */   {
/* 2279 */     if (x == null) {
/* 2280 */       setNull(parameterIndex, 3);
/*      */     } else {
/* 2282 */       setInternal(parameterIndex, StringUtils.fixDecimalExponent(StringUtils.consistentToString(x)));
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
/*      */   public void setBinaryStream(int parameterIndex, InputStream x, int length)
/*      */     throws SQLException
/*      */   {
/* 2309 */     if (x == null) {
/* 2310 */       setNull(parameterIndex, -2);
/*      */     } else {
/* 2312 */       int parameterIndexOffset = getParameterIndexOffset();
/*      */       
/* 2314 */       if ((parameterIndex < 1) || (parameterIndex > this.staticSqlStrings.length))
/*      */       {
/* 2316 */         throw SQLError.createSQLException(Messages.getString("PreparedStatement.2") + parameterIndex + Messages.getString("PreparedStatement.3") + this.staticSqlStrings.length + Messages.getString("PreparedStatement.4"), "S1009");
/*      */       }
/*      */       
/*      */ 
/*      */ 
/* 2321 */       if ((parameterIndexOffset == -1) && (parameterIndex == 1)) {
/* 2322 */         throw SQLError.createSQLException("Can't set IN parameter for return value of stored function call.", "S1009");
/*      */       }
/*      */       
/*      */ 
/*      */ 
/* 2327 */       this.parameterStreams[(parameterIndex - 1 + parameterIndexOffset)] = x;
/* 2328 */       this.isStream[(parameterIndex - 1 + parameterIndexOffset)] = true;
/* 2329 */       this.streamLengths[(parameterIndex - 1 + parameterIndexOffset)] = length;
/* 2330 */       this.isNull[(parameterIndex - 1 + parameterIndexOffset)] = false;
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
/*      */   public void setBlob(int i, Blob x)
/*      */     throws SQLException
/*      */   {
/* 2346 */     if (x == null) {
/* 2347 */       setNull(i, 2004);
/*      */     } else {
/* 2349 */       ByteArrayOutputStream bytesOut = new ByteArrayOutputStream();
/*      */       
/* 2351 */       bytesOut.write(39);
/* 2352 */       escapeblockFast(x.getBytes(1L, (int)x.length()), bytesOut, (int)x.length());
/*      */       
/* 2354 */       bytesOut.write(39);
/*      */       
/* 2356 */       setInternal(i, bytesOut.toByteArray());
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
/*      */   public void setBoolean(int parameterIndex, boolean x)
/*      */     throws SQLException
/*      */   {
/* 2373 */     if (this.useTrueBoolean) {
/* 2374 */       setInternal(parameterIndex, x ? "1" : "0");
/*      */     } else {
/* 2376 */       setInternal(parameterIndex, x ? "'t'" : "'f'");
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
/*      */   public void setByte(int parameterIndex, byte x)
/*      */     throws SQLException
/*      */   {
/* 2393 */     setInternal(parameterIndex, String.valueOf(x));
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
/*      */   public void setBytes(int parameterIndex, byte[] x)
/*      */     throws SQLException
/*      */   {
/* 2410 */     setBytes(parameterIndex, x, true, true);
/*      */   }
/*      */   
/*      */   protected void setBytes(int parameterIndex, byte[] x, boolean checkForIntroducer, boolean escapeForMBChars)
/*      */     throws SQLException
/*      */   {
/* 2416 */     if (x == null) {
/* 2417 */       setNull(parameterIndex, -2);
/*      */     } else {
/* 2419 */       String connectionEncoding = this.connection.getEncoding();
/*      */       
/* 2421 */       if ((this.connection.isNoBackslashEscapesSet()) || ((escapeForMBChars) && (this.connection.getUseUnicode()) && (connectionEncoding != null) && (CharsetMapping.isMultibyteCharset(connectionEncoding))))
/*      */       {
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 2429 */         ByteArrayOutputStream bOut = new ByteArrayOutputStream(x.length * 2 + 3);
/*      */         
/* 2431 */         bOut.write(120);
/* 2432 */         bOut.write(39);
/*      */         
/* 2434 */         for (int i = 0; i < x.length; i++) {
/* 2435 */           int lowBits = (x[i] & 0xFF) / 16;
/* 2436 */           int highBits = (x[i] & 0xFF) % 16;
/*      */           
/* 2438 */           bOut.write(HEX_DIGITS[lowBits]);
/* 2439 */           bOut.write(HEX_DIGITS[highBits]);
/*      */         }
/*      */         
/* 2442 */         bOut.write(39);
/*      */         
/* 2444 */         setInternal(parameterIndex, bOut.toByteArray());
/*      */         
/* 2446 */         return;
/*      */       }
/*      */       
/*      */ 
/* 2450 */       int numBytes = x.length;
/*      */       
/* 2452 */       int pad = 2;
/*      */       
/* 2454 */       boolean needsIntroducer = (checkForIntroducer) && (this.connection.versionMeetsMinimum(4, 1, 0));
/*      */       
/*      */ 
/* 2457 */       if (needsIntroducer) {
/* 2458 */         pad += 7;
/*      */       }
/*      */       
/* 2461 */       ByteArrayOutputStream bOut = new ByteArrayOutputStream(numBytes + pad);
/*      */       
/*      */ 
/* 2464 */       if (needsIntroducer) {
/* 2465 */         bOut.write(95);
/* 2466 */         bOut.write(98);
/* 2467 */         bOut.write(105);
/* 2468 */         bOut.write(110);
/* 2469 */         bOut.write(97);
/* 2470 */         bOut.write(114);
/* 2471 */         bOut.write(121);
/*      */       }
/* 2473 */       bOut.write(39);
/*      */       
/* 2475 */       for (int i = 0; i < numBytes; i++) {
/* 2476 */         byte b = x[i];
/*      */         
/* 2478 */         switch (b) {
/*      */         case 0: 
/* 2480 */           bOut.write(92);
/* 2481 */           bOut.write(48);
/*      */           
/* 2483 */           break;
/*      */         
/*      */         case 10: 
/* 2486 */           bOut.write(92);
/* 2487 */           bOut.write(110);
/*      */           
/* 2489 */           break;
/*      */         
/*      */         case 13: 
/* 2492 */           bOut.write(92);
/* 2493 */           bOut.write(114);
/*      */           
/* 2495 */           break;
/*      */         
/*      */         case 92: 
/* 2498 */           bOut.write(92);
/* 2499 */           bOut.write(92);
/*      */           
/* 2501 */           break;
/*      */         
/*      */         case 39: 
/* 2504 */           bOut.write(92);
/* 2505 */           bOut.write(39);
/*      */           
/* 2507 */           break;
/*      */         
/*      */         case 34: 
/* 2510 */           bOut.write(92);
/* 2511 */           bOut.write(34);
/*      */           
/* 2513 */           break;
/*      */         
/*      */         case 26: 
/* 2516 */           bOut.write(92);
/* 2517 */           bOut.write(90);
/*      */           
/* 2519 */           break;
/*      */         
/*      */         default: 
/* 2522 */           bOut.write(b);
/*      */         }
/*      */         
/*      */       }
/* 2526 */       bOut.write(39);
/*      */       
/* 2528 */       setInternal(parameterIndex, bOut.toByteArray());
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
/*      */   protected void setBytesNoEscape(int parameterIndex, byte[] parameterAsBytes)
/*      */     throws SQLException
/*      */   {
/* 2546 */     byte[] parameterWithQuotes = new byte[parameterAsBytes.length + 2];
/* 2547 */     parameterWithQuotes[0] = 39;
/* 2548 */     System.arraycopy(parameterAsBytes, 0, parameterWithQuotes, 1, parameterAsBytes.length);
/*      */     
/* 2550 */     parameterWithQuotes[(parameterAsBytes.length + 1)] = 39;
/*      */     
/* 2552 */     setInternal(parameterIndex, parameterWithQuotes);
/*      */   }
/*      */   
/*      */   protected void setBytesNoEscapeNoQuotes(int parameterIndex, byte[] parameterAsBytes) throws SQLException
/*      */   {
/* 2557 */     setInternal(parameterIndex, parameterAsBytes);
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
/*      */   public void setCharacterStream(int parameterIndex, Reader reader, int length)
/*      */     throws SQLException
/*      */   {
/*      */     try
/*      */     {
/* 2585 */       if (reader == null) {
/* 2586 */         setNull(parameterIndex, -1);
/*      */       } else {
/* 2588 */         char[] c = null;
/* 2589 */         int len = 0;
/*      */         
/* 2591 */         boolean useLength = this.connection.getUseStreamLengthsInPrepStmts();
/*      */         
/*      */ 
/* 2594 */         String forcedEncoding = this.connection.getClobCharacterEncoding();
/*      */         
/* 2596 */         if ((useLength) && (length != -1)) {
/* 2597 */           c = new char[length];
/*      */           
/* 2599 */           int numCharsRead = readFully(reader, c, length);
/*      */           
/*      */ 
/*      */ 
/*      */ 
/* 2604 */           if (forcedEncoding == null) {
/* 2605 */             setString(parameterIndex, new String(c, 0, numCharsRead));
/*      */           } else {
/*      */             try {
/* 2608 */               setBytes(parameterIndex, new String(c, 0, numCharsRead).getBytes(forcedEncoding));
/*      */             }
/*      */             catch (UnsupportedEncodingException uee)
/*      */             {
/* 2612 */               throw SQLError.createSQLException("Unsupported character encoding " + forcedEncoding, "S1009");
/*      */             }
/*      */           }
/*      */         }
/*      */         else {
/* 2617 */           c = new char['က'];
/*      */           
/* 2619 */           StringBuffer buf = new StringBuffer();
/*      */           
/* 2621 */           while ((len = reader.read(c)) != -1) {
/* 2622 */             buf.append(c, 0, len);
/*      */           }
/*      */           
/* 2625 */           if (forcedEncoding == null) {
/* 2626 */             setString(parameterIndex, buf.toString());
/*      */           } else {
/*      */             try {
/* 2629 */               setBytes(parameterIndex, buf.toString().getBytes(forcedEncoding));
/*      */             }
/*      */             catch (UnsupportedEncodingException uee) {
/* 2632 */               throw SQLError.createSQLException("Unsupported character encoding " + forcedEncoding, "S1009");
/*      */             }
/*      */           }
/*      */         }
/*      */       }
/*      */     }
/*      */     catch (IOException ioEx) {
/* 2639 */       throw SQLError.createSQLException(ioEx.toString(), "S1000");
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
/*      */   public void setClob(int i, Clob x)
/*      */     throws SQLException
/*      */   {
/* 2656 */     if (x == null) {
/* 2657 */       setNull(i, 2005);
/*      */       
/* 2659 */       return;
/*      */     }
/*      */     
/* 2662 */     String forcedEncoding = this.connection.getClobCharacterEncoding();
/*      */     
/* 2664 */     if (forcedEncoding == null) {
/* 2665 */       setString(i, x.getSubString(1L, (int)x.length()));
/*      */     } else {
/*      */       try {
/* 2668 */         setBytes(i, x.getSubString(1L, (int)x.length()).getBytes(forcedEncoding));
/*      */       }
/*      */       catch (UnsupportedEncodingException uee) {
/* 2671 */         throw SQLError.createSQLException("Unsupported character encoding " + forcedEncoding, "S1009");
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
/*      */   public void setDate(int parameterIndex, java.sql.Date x)
/*      */     throws SQLException
/*      */   {
/* 2691 */     if (x == null) {
/* 2692 */       setNull(parameterIndex, 91);
/*      */     }
/*      */     else
/*      */     {
/* 2696 */       SimpleDateFormat dateFormatter = new SimpleDateFormat("''yyyy-MM-dd''", Locale.US);
/*      */       
/* 2698 */       setInternal(parameterIndex, dateFormatter.format(x));
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
/*      */   public void setDate(int parameterIndex, java.sql.Date x, Calendar cal)
/*      */     throws SQLException
/*      */   {
/* 2718 */     setDate(parameterIndex, x);
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
/*      */   public void setDouble(int parameterIndex, double x)
/*      */     throws SQLException
/*      */   {
/* 2735 */     if ((!this.connection.getAllowNanAndInf()) && ((x == Double.POSITIVE_INFINITY) || (x == Double.NEGATIVE_INFINITY) || (Double.isNaN(x))))
/*      */     {
/*      */ 
/* 2738 */       throw SQLError.createSQLException("'" + x + "' is not a valid numeric or approximate numeric value", "S1009");
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/* 2744 */     setInternal(parameterIndex, StringUtils.fixDecimalExponent(String.valueOf(x)));
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
/*      */   public void setFloat(int parameterIndex, float x)
/*      */     throws SQLException
/*      */   {
/* 2761 */     setInternal(parameterIndex, StringUtils.fixDecimalExponent(String.valueOf(x)));
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
/*      */   public void setInt(int parameterIndex, int x)
/*      */     throws SQLException
/*      */   {
/* 2778 */     setInternal(parameterIndex, String.valueOf(x));
/*      */   }
/*      */   
/*      */   private final void setInternal(int paramIndex, byte[] val) throws SQLException
/*      */   {
/* 2783 */     if (this.isClosed) {
/* 2784 */       throw SQLError.createSQLException(Messages.getString("PreparedStatement.48"), "S1009");
/*      */     }
/*      */     
/*      */ 
/* 2788 */     int parameterIndexOffset = getParameterIndexOffset();
/*      */     
/* 2790 */     if (paramIndex < 1) {
/* 2791 */       throw SQLError.createSQLException(Messages.getString("PreparedStatement.49") + paramIndex + Messages.getString("PreparedStatement.50"), "S1009");
/*      */     }
/*      */     
/*      */ 
/* 2795 */     if (paramIndex > this.parameterCount) {
/* 2796 */       throw SQLError.createSQLException(Messages.getString("PreparedStatement.51") + paramIndex + Messages.getString("PreparedStatement.52") + this.parameterValues.length + Messages.getString("PreparedStatement.53"), "S1009");
/*      */     }
/*      */     
/*      */ 
/*      */ 
/* 2801 */     if ((parameterIndexOffset == -1) && (paramIndex == 1)) {
/* 2802 */       throw SQLError.createSQLException("Can't set IN parameter for return value of stored function call.", "S1009");
/*      */     }
/*      */     
/*      */ 
/* 2806 */     this.isStream[(paramIndex - 1 + parameterIndexOffset)] = false;
/* 2807 */     this.isNull[(paramIndex - 1 + parameterIndexOffset)] = false;
/* 2808 */     this.parameterStreams[(paramIndex - 1 + parameterIndexOffset)] = null;
/* 2809 */     this.parameterValues[(paramIndex - 1 + parameterIndexOffset)] = val;
/*      */   }
/*      */   
/*      */   private final void setInternal(int paramIndex, String val) throws SQLException
/*      */   {
/* 2814 */     checkClosed();
/*      */     
/* 2816 */     byte[] parameterAsBytes = null;
/*      */     
/* 2818 */     if (this.charConverter != null) {
/* 2819 */       parameterAsBytes = this.charConverter.toBytes(val);
/*      */     } else {
/* 2821 */       parameterAsBytes = StringUtils.getBytes(val, this.charConverter, this.charEncoding, this.connection.getServerCharacterEncoding(), this.connection.parserKnowsUnicode());
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/* 2827 */     setInternal(paramIndex, parameterAsBytes);
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
/*      */   public void setLong(int parameterIndex, long x)
/*      */     throws SQLException
/*      */   {
/* 2843 */     setInternal(parameterIndex, String.valueOf(x));
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
/*      */   public void setNull(int parameterIndex, int sqlType)
/*      */     throws SQLException
/*      */   {
/* 2863 */     setInternal(parameterIndex, "null");
/* 2864 */     this.isNull[(parameterIndex - 1)] = true;
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
/*      */   public void setNull(int parameterIndex, int sqlType, String arg)
/*      */     throws SQLException
/*      */   {
/* 2886 */     setNull(parameterIndex, sqlType);
/*      */   }
/*      */   
/*      */   private void setNumericObject(int parameterIndex, Object parameterObj, int targetSqlType, int scale) throws SQLException {
/*      */     Number parameterAsNum;
/*      */     Number parameterAsNum;
/* 2892 */     if ((parameterObj instanceof Boolean)) {
/* 2893 */       parameterAsNum = ((Boolean)parameterObj).booleanValue() ? new Integer(1) : new Integer(0);
/*      */ 
/*      */     }
/* 2896 */     else if ((parameterObj instanceof String)) { Number parameterAsNum;
/* 2897 */       Number parameterAsNum; Number parameterAsNum; Number parameterAsNum; Number parameterAsNum; Number parameterAsNum; switch (targetSqlType) {
/*      */       case -7: 
/* 2899 */         boolean parameterAsBoolean = "true".equalsIgnoreCase((String)parameterObj);
/*      */         
/*      */ 
/* 2902 */         parameterAsNum = parameterAsBoolean ? new Integer(1) : new Integer(0);
/*      */         
/*      */ 
/* 2905 */         break;
/*      */       
/*      */       case -6: 
/*      */       case 4: 
/*      */       case 5: 
/* 2910 */         parameterAsNum = Integer.valueOf((String)parameterObj);
/*      */         
/*      */ 
/* 2913 */         break;
/*      */       
/*      */       case -5: 
/* 2916 */         parameterAsNum = Long.valueOf((String)parameterObj);
/*      */         
/*      */ 
/* 2919 */         break;
/*      */       
/*      */       case 7: 
/* 2922 */         parameterAsNum = Float.valueOf((String)parameterObj);
/*      */         
/*      */ 
/* 2925 */         break;
/*      */       
/*      */       case 6: 
/*      */       case 8: 
/* 2929 */         parameterAsNum = Double.valueOf((String)parameterObj);
/*      */         
/*      */ 
/* 2932 */         break;
/*      */       case -4: case -3: case -2: 
/*      */       case -1: case 0: 
/*      */       case 1: case 2: 
/*      */       case 3: default: 
/* 2937 */         parameterAsNum = new BigDecimal((String)parameterObj);break;
/*      */       }
/*      */     }
/*      */     else {
/* 2941 */       parameterAsNum = (Number)parameterObj;
/*      */     }
/*      */     
/* 2944 */     switch (targetSqlType) {
/*      */     case -7: 
/*      */     case -6: 
/*      */     case 4: 
/*      */     case 5: 
/* 2949 */       setInt(parameterIndex, parameterAsNum.intValue());
/*      */       
/* 2951 */       break;
/*      */     
/*      */     case -5: 
/* 2954 */       setLong(parameterIndex, parameterAsNum.longValue());
/*      */       
/* 2956 */       break;
/*      */     
/*      */     case 7: 
/* 2959 */       setFloat(parameterIndex, parameterAsNum.floatValue());
/*      */       
/* 2961 */       break;
/*      */     
/*      */     case 6: 
/*      */     case 8: 
/* 2965 */       setDouble(parameterIndex, parameterAsNum.doubleValue());
/*      */       
/* 2967 */       break;
/*      */     
/*      */ 
/*      */     case 2: 
/*      */     case 3: 
/* 2972 */       if ((parameterAsNum instanceof BigDecimal)) {
/* 2973 */         BigDecimal scaledBigDecimal = null;
/*      */         try
/*      */         {
/* 2976 */           scaledBigDecimal = ((BigDecimal)parameterAsNum).setScale(scale);
/*      */         }
/*      */         catch (ArithmeticException ex) {
/*      */           try {
/* 2980 */             scaledBigDecimal = ((BigDecimal)parameterAsNum).setScale(scale, 4);
/*      */           }
/*      */           catch (ArithmeticException arEx)
/*      */           {
/* 2984 */             throw SQLError.createSQLException("Can't set scale of '" + scale + "' for DECIMAL argument '" + parameterAsNum + "'", "S1009");
/*      */           }
/*      */         }
/*      */         
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 2993 */         setBigDecimal(parameterIndex, scaledBigDecimal);
/* 2994 */       } else if ((parameterAsNum instanceof BigInteger)) {
/* 2995 */         setBigDecimal(parameterIndex, new BigDecimal((BigInteger)parameterAsNum, scale));
/*      */ 
/*      */       }
/*      */       else
/*      */       {
/*      */ 
/* 3001 */         setBigDecimal(parameterIndex, new BigDecimal(parameterAsNum.doubleValue()));
/*      */       }
/*      */       
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       break;
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
/*      */   public void setObject(int parameterIndex, Object parameterObj)
/*      */     throws SQLException
/*      */   {
/* 3023 */     if (parameterObj == null) {
/* 3024 */       setNull(parameterIndex, 1111);
/*      */     }
/* 3026 */     else if ((parameterObj instanceof Byte)) {
/* 3027 */       setInt(parameterIndex, ((Byte)parameterObj).intValue());
/* 3028 */     } else if ((parameterObj instanceof String)) {
/* 3029 */       setString(parameterIndex, (String)parameterObj);
/* 3030 */     } else if ((parameterObj instanceof BigDecimal)) {
/* 3031 */       setBigDecimal(parameterIndex, (BigDecimal)parameterObj);
/* 3032 */     } else if ((parameterObj instanceof Short)) {
/* 3033 */       setShort(parameterIndex, ((Short)parameterObj).shortValue());
/* 3034 */     } else if ((parameterObj instanceof Integer)) {
/* 3035 */       setInt(parameterIndex, ((Integer)parameterObj).intValue());
/* 3036 */     } else if ((parameterObj instanceof Long)) {
/* 3037 */       setLong(parameterIndex, ((Long)parameterObj).longValue());
/* 3038 */     } else if ((parameterObj instanceof Float)) {
/* 3039 */       setFloat(parameterIndex, ((Float)parameterObj).floatValue());
/* 3040 */     } else if ((parameterObj instanceof Double)) {
/* 3041 */       setDouble(parameterIndex, ((Double)parameterObj).doubleValue());
/* 3042 */     } else if ((parameterObj instanceof byte[])) {
/* 3043 */       setBytes(parameterIndex, (byte[])parameterObj);
/* 3044 */     } else if ((parameterObj instanceof java.sql.Date)) {
/* 3045 */       setDate(parameterIndex, (java.sql.Date)parameterObj);
/* 3046 */     } else if ((parameterObj instanceof Time)) {
/* 3047 */       setTime(parameterIndex, (Time)parameterObj);
/* 3048 */     } else if ((parameterObj instanceof Timestamp)) {
/* 3049 */       setTimestamp(parameterIndex, (Timestamp)parameterObj);
/* 3050 */     } else if ((parameterObj instanceof Boolean)) {
/* 3051 */       setBoolean(parameterIndex, ((Boolean)parameterObj).booleanValue());
/*      */     }
/* 3053 */     else if ((parameterObj instanceof InputStream)) {
/* 3054 */       setBinaryStream(parameterIndex, (InputStream)parameterObj, -1);
/* 3055 */     } else if ((parameterObj instanceof Blob)) {
/* 3056 */       setBlob(parameterIndex, (Blob)parameterObj);
/* 3057 */     } else if ((parameterObj instanceof Clob)) {
/* 3058 */       setClob(parameterIndex, (Clob)parameterObj);
/* 3059 */     } else if ((this.connection.getTreatUtilDateAsTimestamp()) && ((parameterObj instanceof java.util.Date)))
/*      */     {
/* 3061 */       setTimestamp(parameterIndex, new Timestamp(((java.util.Date)parameterObj).getTime()));
/*      */     }
/* 3063 */     else if ((parameterObj instanceof BigInteger)) {
/* 3064 */       setString(parameterIndex, parameterObj.toString());
/*      */     } else {
/* 3066 */       setSerializableObject(parameterIndex, parameterObj);
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
/*      */   public void setObject(int parameterIndex, Object parameterObj, int targetSqlType)
/*      */     throws SQLException
/*      */   {
/* 3087 */     if (!(parameterObj instanceof BigDecimal)) {
/* 3088 */       setObject(parameterIndex, parameterObj, targetSqlType, 0);
/*      */     } else {
/* 3090 */       setObject(parameterIndex, parameterObj, targetSqlType, ((BigDecimal)parameterObj).scale());
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
/*      */   public void setObject(int parameterIndex, Object parameterObj, int targetSqlType, int scale)
/*      */     throws SQLException
/*      */   {
/* 3126 */     if (parameterObj == null) {
/* 3127 */       setNull(parameterIndex, 1111);
/*      */     } else {
/*      */       try {
/* 3130 */         switch (targetSqlType)
/*      */         {
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */         case 16: 
/* 3150 */           if ((parameterObj instanceof Boolean)) {
/* 3151 */             setBoolean(parameterIndex, ((Boolean)parameterObj).booleanValue());
/*      */ 
/*      */           }
/* 3154 */           else if ((parameterObj instanceof String)) {
/* 3155 */             setBoolean(parameterIndex, ("true".equalsIgnoreCase((String)parameterObj)) || (!"0".equalsIgnoreCase((String)parameterObj)));
/*      */ 
/*      */ 
/*      */           }
/* 3159 */           else if ((parameterObj instanceof Number)) {
/* 3160 */             int intValue = ((Number)parameterObj).intValue();
/*      */             
/* 3162 */             setBoolean(parameterIndex, intValue != 0);
/*      */           }
/*      */           else
/*      */           {
/* 3166 */             throw SQLError.createSQLException("No conversion from " + parameterObj.getClass().getName() + " to Types.BOOLEAN possible.", "S1009");
/*      */           }
/*      */           
/*      */ 
/*      */ 
/*      */           break;
/*      */         case -7: 
/*      */         case -6: 
/*      */         case -5: 
/*      */         case 2: 
/*      */         case 3: 
/*      */         case 4: 
/*      */         case 5: 
/*      */         case 6: 
/*      */         case 7: 
/*      */         case 8: 
/* 3182 */           setNumericObject(parameterIndex, parameterObj, targetSqlType, scale);
/*      */           
/* 3184 */           break;
/*      */         
/*      */         case -1: 
/*      */         case 1: 
/*      */         case 12: 
/* 3189 */           if ((parameterObj instanceof BigDecimal)) {
/* 3190 */             setString(parameterIndex, StringUtils.fixDecimalExponent(StringUtils.consistentToString((BigDecimal)parameterObj)));
/*      */ 
/*      */           }
/*      */           else
/*      */           {
/*      */ 
/* 3196 */             setString(parameterIndex, parameterObj.toString());
/*      */           }
/*      */           
/* 3199 */           break;
/*      */         
/*      */ 
/*      */         case 2005: 
/* 3203 */           if ((parameterObj instanceof Clob)) {
/* 3204 */             setClob(parameterIndex, (Clob)parameterObj);
/*      */           } else {
/* 3206 */             setString(parameterIndex, parameterObj.toString());
/*      */           }
/*      */           
/* 3209 */           break;
/*      */         
/*      */ 
/*      */         case -4: 
/*      */         case -3: 
/*      */         case -2: 
/*      */         case 2004: 
/* 3216 */           if ((parameterObj instanceof byte[])) {
/* 3217 */             setBytes(parameterIndex, (byte[])parameterObj);
/* 3218 */           } else if ((parameterObj instanceof Blob)) {
/* 3219 */             setBlob(parameterIndex, (Blob)parameterObj);
/*      */           } else {
/* 3221 */             setBytes(parameterIndex, StringUtils.getBytes(parameterObj.toString(), this.charConverter, this.charEncoding, this.connection.getServerCharacterEncoding(), this.connection.parserKnowsUnicode()));
/*      */           }
/*      */           
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 3228 */           break;
/*      */         case 91: 
/*      */         case 93: 
/*      */           java.util.Date parameterAsDate;
/*      */           
/*      */           java.util.Date parameterAsDate;
/*      */           
/* 3235 */           if ((parameterObj instanceof String)) {
/* 3236 */             ParsePosition pp = new ParsePosition(0);
/* 3237 */             DateFormat sdf = new SimpleDateFormat(getDateTimePattern((String)parameterObj, false), Locale.US);
/*      */             
/* 3239 */             parameterAsDate = sdf.parse((String)parameterObj, pp);
/*      */           } else {
/* 3241 */             parameterAsDate = (java.util.Date)parameterObj;
/*      */           }
/*      */           
/* 3244 */           switch (targetSqlType)
/*      */           {
/*      */           case 91: 
/* 3247 */             if ((parameterAsDate instanceof java.sql.Date)) {
/* 3248 */               setDate(parameterIndex, (java.sql.Date)parameterAsDate);
/*      */             }
/*      */             else {
/* 3251 */               setDate(parameterIndex, new java.sql.Date(parameterAsDate.getTime()));
/*      */             }
/*      */             
/*      */ 
/* 3255 */             break;
/*      */           
/*      */ 
/*      */           case 93: 
/* 3259 */             if ((parameterAsDate instanceof Timestamp)) {
/* 3260 */               setTimestamp(parameterIndex, (Timestamp)parameterAsDate);
/*      */             }
/*      */             else {
/* 3263 */               setTimestamp(parameterIndex, new Timestamp(parameterAsDate.getTime()));
/*      */             }
/*      */             
/*      */ 
/*      */             break;
/*      */           }
/*      */           
/*      */           
/* 3271 */           break;
/*      */         
/*      */ 
/*      */         case 92: 
/* 3275 */           if ((parameterObj instanceof String)) {
/* 3276 */             DateFormat sdf = new SimpleDateFormat(getDateTimePattern((String)parameterObj, true), Locale.US);
/*      */             
/* 3278 */             setTime(parameterIndex, new Time(sdf.parse((String)parameterObj).getTime()));
/*      */           }
/* 3280 */           else if ((parameterObj instanceof Timestamp)) {
/* 3281 */             Timestamp xT = (Timestamp)parameterObj;
/* 3282 */             setTime(parameterIndex, new Time(xT.getTime()));
/*      */           } else {
/* 3284 */             setTime(parameterIndex, (Time)parameterObj);
/*      */           }
/*      */           
/* 3287 */           break;
/*      */         
/*      */         case 1111: 
/* 3290 */           setSerializableObject(parameterIndex, parameterObj);
/*      */           
/* 3292 */           break;
/*      */         
/*      */         default: 
/* 3295 */           throw SQLError.createSQLException(Messages.getString("PreparedStatement.16"), "S1000");
/*      */         }
/*      */         
/*      */       }
/*      */       catch (Exception ex) {
/* 3300 */         if ((ex instanceof SQLException)) {
/* 3301 */           throw ((SQLException)ex);
/*      */         }
/*      */         
/* 3304 */         throw SQLError.createSQLException(Messages.getString("PreparedStatement.17") + parameterObj.getClass().toString() + Messages.getString("PreparedStatement.18") + ex.getClass().getName() + Messages.getString("PreparedStatement.19") + ex.getMessage(), "S1000");
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
/*      */   protected int setOneBatchedParameterSet(java.sql.PreparedStatement batchedStatement, int batchedParamIndex, Object paramSet)
/*      */     throws SQLException
/*      */   {
/* 3318 */     BatchParams paramArg = (BatchParams)paramSet;
/*      */     
/* 3320 */     boolean[] isNullBatch = paramArg.isNull;
/* 3321 */     boolean[] isStreamBatch = paramArg.isStream;
/*      */     
/* 3323 */     for (int j = 0; j < isNullBatch.length; j++) {
/* 3324 */       if (isNullBatch[j] != 0) {
/* 3325 */         batchedStatement.setNull(batchedParamIndex++, 0);
/*      */       }
/* 3327 */       else if (isStreamBatch[j] != 0) {
/* 3328 */         batchedStatement.setBinaryStream(batchedParamIndex++, paramArg.parameterStreams[j], paramArg.streamLengths[j]);
/*      */       }
/*      */       else
/*      */       {
/* 3332 */         ((PreparedStatement)batchedStatement).setBytesNoEscapeNoQuotes(batchedParamIndex++, paramArg.parameterStrings[j]);
/*      */       }
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/* 3339 */     return batchedParamIndex;
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
/*      */   public void setRef(int i, Ref x)
/*      */     throws SQLException
/*      */   {
/* 3356 */     throw new NotImplemented();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   void setResultSetConcurrency(int concurrencyFlag)
/*      */   {
/* 3366 */     this.resultSetConcurrency = concurrencyFlag;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   void setResultSetType(int typeFlag)
/*      */   {
/* 3376 */     this.resultSetType = typeFlag;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected void setRetrieveGeneratedKeys(boolean retrieveGeneratedKeys)
/*      */   {
/* 3385 */     this.retrieveGeneratedKeys = retrieveGeneratedKeys;
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
/*      */   private final void setSerializableObject(int parameterIndex, Object parameterObj)
/*      */     throws SQLException
/*      */   {
/*      */     try
/*      */     {
/* 3403 */       ByteArrayOutputStream bytesOut = new ByteArrayOutputStream();
/* 3404 */       ObjectOutputStream objectOut = new ObjectOutputStream(bytesOut);
/* 3405 */       objectOut.writeObject(parameterObj);
/* 3406 */       objectOut.flush();
/* 3407 */       objectOut.close();
/* 3408 */       bytesOut.flush();
/* 3409 */       bytesOut.close();
/*      */       
/* 3411 */       byte[] buf = bytesOut.toByteArray();
/* 3412 */       ByteArrayInputStream bytesIn = new ByteArrayInputStream(buf);
/* 3413 */       setBinaryStream(parameterIndex, bytesIn, buf.length);
/*      */     } catch (Exception ex) {
/* 3415 */       throw SQLError.createSQLException(Messages.getString("PreparedStatement.54") + ex.getClass().getName(), "S1009");
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
/*      */   public void setShort(int parameterIndex, short x)
/*      */     throws SQLException
/*      */   {
/* 3434 */     setInternal(parameterIndex, String.valueOf(x));
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
/*      */   public void setString(int parameterIndex, String x)
/*      */     throws SQLException
/*      */   {
/* 3452 */     if (x == null) {
/* 3453 */       setNull(parameterIndex, 1);
/*      */     } else {
/* 3455 */       checkClosed();
/*      */       
/* 3457 */       int stringLength = x.length();
/*      */       
/* 3459 */       if (this.connection.isNoBackslashEscapesSet())
/*      */       {
/*      */ 
/* 3462 */         boolean needsHexEscape = false;
/*      */         
/* 3464 */         for (int i = 0; i < stringLength; i++) {
/* 3465 */           char c = x.charAt(i);
/*      */           
/* 3467 */           switch (c)
/*      */           {
/*      */           case '\000': 
/* 3470 */             needsHexEscape = true;
/* 3471 */             break;
/*      */           
/*      */           case '\n': 
/* 3474 */             needsHexEscape = true;
/*      */             
/* 3476 */             break;
/*      */           
/*      */           case '\r': 
/* 3479 */             needsHexEscape = true;
/* 3480 */             break;
/*      */           
/*      */           case '\\': 
/* 3483 */             needsHexEscape = true;
/*      */             
/* 3485 */             break;
/*      */           
/*      */           case '\'': 
/* 3488 */             needsHexEscape = true;
/*      */             
/* 3490 */             break;
/*      */           
/*      */           case '"': 
/* 3493 */             needsHexEscape = true;
/*      */             
/* 3495 */             break;
/*      */           
/*      */           case '\032': 
/* 3498 */             needsHexEscape = true;
/*      */           }
/*      */           
/*      */           
/* 3502 */           if (needsHexEscape) {
/*      */             break;
/*      */           }
/*      */         }
/*      */         
/*      */ 
/*      */ 
/* 3509 */         if (!needsHexEscape) {
/* 3510 */           byte[] parameterAsBytes = null;
/*      */           
/* 3512 */           StringBuffer quotedString = new StringBuffer(x.length() + 2);
/* 3513 */           quotedString.append('\'');
/* 3514 */           quotedString.append(x);
/* 3515 */           quotedString.append('\'');
/*      */           
/* 3517 */           if (!this.isLoadDataQuery) {
/* 3518 */             parameterAsBytes = StringUtils.getBytes(quotedString.toString(), this.charConverter, this.charEncoding, this.connection.getServerCharacterEncoding(), this.connection.parserKnowsUnicode());
/*      */ 
/*      */           }
/*      */           else
/*      */           {
/*      */ 
/* 3524 */             parameterAsBytes = quotedString.toString().getBytes();
/*      */           }
/*      */           
/* 3527 */           setInternal(parameterIndex, parameterAsBytes);
/*      */         } else {
/* 3529 */           byte[] parameterAsBytes = null;
/*      */           
/* 3531 */           if (!this.isLoadDataQuery) {
/* 3532 */             parameterAsBytes = StringUtils.getBytes(x, this.charConverter, this.charEncoding, this.connection.getServerCharacterEncoding(), this.connection.parserKnowsUnicode());
/*      */ 
/*      */           }
/*      */           else
/*      */           {
/*      */ 
/* 3538 */             parameterAsBytes = x.getBytes();
/*      */           }
/*      */           
/* 3541 */           setBytes(parameterIndex, parameterAsBytes);
/*      */         }
/*      */         
/* 3544 */         return;
/*      */       }
/*      */       
/* 3547 */       StringBuffer buf = new StringBuffer((int)(x.length() * 1.1D));
/* 3548 */       buf.append('\'');
/*      */       
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 3557 */       for (int i = 0; i < stringLength; i++) {
/* 3558 */         char c = x.charAt(i);
/*      */         
/* 3560 */         switch (c) {
/*      */         case '\000': 
/* 3562 */           buf.append('\\');
/* 3563 */           buf.append('0');
/*      */           
/* 3565 */           break;
/*      */         
/*      */         case '\n': 
/* 3568 */           buf.append('\\');
/* 3569 */           buf.append('n');
/*      */           
/* 3571 */           break;
/*      */         
/*      */         case '\r': 
/* 3574 */           buf.append('\\');
/* 3575 */           buf.append('r');
/*      */           
/* 3577 */           break;
/*      */         
/*      */         case '\\': 
/* 3580 */           buf.append('\\');
/* 3581 */           buf.append('\\');
/*      */           
/* 3583 */           break;
/*      */         
/*      */         case '\'': 
/* 3586 */           buf.append('\\');
/* 3587 */           buf.append('\'');
/*      */           
/* 3589 */           break;
/*      */         
/*      */         case '"': 
/* 3592 */           if (this.usingAnsiMode) {
/* 3593 */             buf.append('\\');
/*      */           }
/*      */           
/* 3596 */           buf.append('"');
/*      */           
/* 3598 */           break;
/*      */         
/*      */         case '\032': 
/* 3601 */           buf.append('\\');
/* 3602 */           buf.append('Z');
/*      */           
/* 3604 */           break;
/*      */         
/*      */         default: 
/* 3607 */           buf.append(c);
/*      */         }
/*      */         
/*      */       }
/* 3611 */       buf.append('\'');
/*      */       
/* 3613 */       String parameterAsString = buf.toString();
/*      */       
/* 3615 */       byte[] parameterAsBytes = null;
/*      */       
/* 3617 */       if (!this.isLoadDataQuery) {
/* 3618 */         parameterAsBytes = StringUtils.getBytes(parameterAsString, this.charConverter, this.charEncoding, this.connection.getServerCharacterEncoding(), this.connection.parserKnowsUnicode());
/*      */ 
/*      */       }
/*      */       else
/*      */       {
/*      */ 
/* 3624 */         parameterAsBytes = parameterAsString.getBytes();
/*      */       }
/*      */       
/* 3627 */       setInternal(parameterIndex, parameterAsBytes);
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
/*      */   public void setTime(int parameterIndex, Time x, Calendar cal)
/*      */     throws SQLException
/*      */   {
/* 3647 */     setTimeInternal(parameterIndex, x, cal, cal.getTimeZone(), true);
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
/*      */   public void setTime(int parameterIndex, Time x)
/*      */     throws SQLException
/*      */   {
/* 3664 */     setTimeInternal(parameterIndex, x, null, this.connection.getDefaultTimeZone(), false);
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
/*      */   private void setTimeInternal(int parameterIndex, Time x, Calendar targetCalendar, TimeZone tz, boolean rollForward)
/*      */     throws SQLException
/*      */   {
/* 3685 */     if (x == null) {
/* 3686 */       setNull(parameterIndex, 92);
/*      */     } else {
/* 3688 */       checkClosed();
/*      */       
/* 3690 */       Calendar sessionCalendar = getCalendarInstanceForSessionOrNew();
/*      */       
/* 3692 */       synchronized (sessionCalendar) {
/* 3693 */         x = TimeUtil.changeTimezone(this.connection, sessionCalendar, targetCalendar, x, tz, this.connection.getServerTimezoneTZ(), rollForward);
/*      */       }
/*      */       
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 3700 */       setInternal(parameterIndex, "'" + x.toString() + "'");
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
/*      */   public void setTimestamp(int parameterIndex, Timestamp x, Calendar cal)
/*      */     throws SQLException
/*      */   {
/* 3720 */     setTimestampInternal(parameterIndex, x, cal, cal.getTimeZone(), true);
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
/* 3737 */     setTimestampInternal(parameterIndex, x, null, this.connection.getDefaultTimeZone(), false);
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
/*      */   private void setTimestampInternal(int parameterIndex, Timestamp x, Calendar targetCalendar, TimeZone tz, boolean rollForward)
/*      */     throws SQLException
/*      */   {
/* 3757 */     if (x == null) {
/* 3758 */       setNull(parameterIndex, 93);
/*      */     } else {
/* 3760 */       checkClosed();
/*      */       
/* 3762 */       String timestampString = null;
/*      */       
/* 3764 */       Calendar sessionCalendar = this.connection.getUseJDBCCompliantTimezoneShift() ? this.connection.getUtcCalendar() : getCalendarInstanceForSessionOrNew();
/*      */       
/*      */ 
/*      */ 
/* 3768 */       synchronized (sessionCalendar) {
/* 3769 */         x = TimeUtil.changeTimezone(this.connection, sessionCalendar, targetCalendar, x, tz, this.connection.getServerTimezoneTZ(), rollForward);
/*      */       }
/*      */       
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 3777 */       if (this.connection.getUseSSPSCompatibleTimezoneShift()) {
/* 3778 */         doSSPSCompatibleTimezoneShift(parameterIndex, x, sessionCalendar);
/*      */       }
/*      */       else {
/* 3781 */         if (this.tsdf == null) {
/* 3782 */           this.tsdf = new SimpleDateFormat("''yyyy-MM-dd HH:mm:ss''", Locale.US);
/*      */         }
/*      */         
/* 3785 */         timestampString = this.tsdf.format(x);
/*      */         
/* 3787 */         setInternal(parameterIndex, timestampString);
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */   private void doSSPSCompatibleTimezoneShift(int parameterIndex, Timestamp x, Calendar sessionCalendar) throws SQLException
/*      */   {
/* 3794 */     Calendar sessionCalendar2 = this.connection.getUseJDBCCompliantTimezoneShift() ? this.connection.getUtcCalendar() : getCalendarInstanceForSessionOrNew();
/*      */     
/*      */ 
/*      */ 
/*      */ 
/* 3799 */     synchronized (sessionCalendar2) {
/* 3800 */       java.util.Date oldTime = sessionCalendar2.getTime();
/*      */       try
/*      */       {
/* 3803 */         sessionCalendar2.setTime(x);
/*      */         
/* 3805 */         int year = sessionCalendar2.get(1);
/* 3806 */         int month = sessionCalendar2.get(2) + 1;
/* 3807 */         int date = sessionCalendar2.get(5);
/*      */         
/* 3809 */         int hour = sessionCalendar2.get(11);
/* 3810 */         int minute = sessionCalendar2.get(12);
/* 3811 */         int seconds = sessionCalendar2.get(13);
/*      */         
/* 3813 */         StringBuffer tsBuf = new StringBuffer();
/*      */         
/* 3815 */         tsBuf.append('\'');
/* 3816 */         tsBuf.append(year);
/*      */         
/* 3818 */         tsBuf.append("-");
/*      */         
/* 3820 */         if (month < 10) {
/* 3821 */           tsBuf.append('0');
/*      */         }
/*      */         
/* 3824 */         tsBuf.append(month);
/*      */         
/* 3826 */         tsBuf.append('-');
/*      */         
/* 3828 */         if (date < 10) {
/* 3829 */           tsBuf.append('0');
/*      */         }
/*      */         
/* 3832 */         tsBuf.append(date);
/*      */         
/* 3834 */         tsBuf.append(' ');
/*      */         
/* 3836 */         if (hour < 10) {
/* 3837 */           tsBuf.append('0');
/*      */         }
/*      */         
/* 3840 */         tsBuf.append(hour);
/*      */         
/* 3842 */         tsBuf.append(':');
/*      */         
/* 3844 */         if (minute < 10) {
/* 3845 */           tsBuf.append('0');
/*      */         }
/*      */         
/* 3848 */         tsBuf.append(minute);
/*      */         
/* 3850 */         tsBuf.append(':');
/*      */         
/* 3852 */         if (seconds < 10) {
/* 3853 */           tsBuf.append('0');
/*      */         }
/*      */         
/* 3856 */         tsBuf.append(seconds);
/*      */         
/* 3858 */         tsBuf.append('\'');
/*      */         
/* 3860 */         setInternal(parameterIndex, tsBuf.toString());
/*      */       }
/*      */       finally {
/* 3863 */         sessionCalendar.setTime(oldTime);
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
/*      */   /**
/*      */    * @deprecated
/*      */    */
/*      */   public void setUnicodeStream(int parameterIndex, InputStream x, int length)
/*      */     throws SQLException
/*      */   {
/* 3894 */     if (x == null) {
/* 3895 */       setNull(parameterIndex, 12);
/*      */     } else {
/* 3897 */       setBinaryStream(parameterIndex, x, length);
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */   public void setURL(int parameterIndex, URL arg)
/*      */     throws SQLException
/*      */   {
/* 3905 */     if (arg != null) {
/* 3906 */       setString(parameterIndex, arg.toString());
/*      */     } else {
/* 3908 */       setNull(parameterIndex, 1);
/*      */     }
/*      */   }
/*      */   
/*      */   private final void streamToBytes(Buffer packet, InputStream in, boolean escape, int streamLength, boolean useLength) throws SQLException
/*      */   {
/*      */     try
/*      */     {
/* 3916 */       String connectionEncoding = this.connection.getEncoding();
/*      */       
/* 3918 */       boolean hexEscape = false;
/*      */       
/* 3920 */       if ((this.connection.isNoBackslashEscapesSet()) || ((this.connection.getUseUnicode()) && (connectionEncoding != null) && (CharsetMapping.isMultibyteCharset(connectionEncoding)) && (!this.connection.parserKnowsUnicode())))
/*      */       {
/*      */ 
/*      */ 
/*      */ 
/* 3925 */         hexEscape = true;
/*      */       }
/*      */       
/* 3928 */       if (streamLength == -1) {
/* 3929 */         useLength = false;
/*      */       }
/*      */       
/* 3932 */       int bc = -1;
/*      */       
/* 3934 */       if (useLength) {
/* 3935 */         bc = readblock(in, this.streamConvertBuf, streamLength);
/*      */       } else {
/* 3937 */         bc = readblock(in, this.streamConvertBuf);
/*      */       }
/*      */       
/* 3940 */       int lengthLeftToRead = streamLength - bc;
/*      */       
/* 3942 */       if (hexEscape) {
/* 3943 */         packet.writeStringNoNull("x");
/* 3944 */       } else if (this.connection.getIO().versionMeetsMinimum(4, 1, 0)) {
/* 3945 */         packet.writeStringNoNull("_binary");
/*      */       }
/*      */       
/* 3948 */       if (escape) {
/* 3949 */         packet.writeByte((byte)39);
/*      */       }
/*      */       
/* 3952 */       while (bc > 0) {
/* 3953 */         if (hexEscape) {
/* 3954 */           hexEscapeBlock(this.streamConvertBuf, packet, bc);
/* 3955 */         } else if (escape) {
/* 3956 */           escapeblockFast(this.streamConvertBuf, packet, bc);
/*      */         } else {
/* 3958 */           packet.writeBytesNoNull(this.streamConvertBuf, 0, bc);
/*      */         }
/*      */         
/* 3961 */         if (useLength) {
/* 3962 */           bc = readblock(in, this.streamConvertBuf, lengthLeftToRead);
/*      */           
/* 3964 */           if (bc > 0) {
/* 3965 */             lengthLeftToRead -= bc;
/*      */           }
/*      */         } else {
/* 3968 */           bc = readblock(in, this.streamConvertBuf);
/*      */         }
/*      */       }
/*      */       
/* 3972 */       if (escape) {
/* 3973 */         packet.writeByte((byte)39);
/*      */       }
/*      */     } finally {
/* 3976 */       if (this.connection.getAutoClosePStmtStreams()) {
/*      */         try {
/* 3978 */           in.close();
/*      */         }
/*      */         catch (IOException ioEx) {}
/*      */         
/*      */ 
/* 3983 */         in = null;
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */   private final byte[] streamToBytes(InputStream in, boolean escape, int streamLength, boolean useLength) throws SQLException
/*      */   {
/*      */     try {
/* 3991 */       if (streamLength == -1) {
/* 3992 */         useLength = false;
/*      */       }
/*      */       
/* 3995 */       ByteArrayOutputStream bytesOut = new ByteArrayOutputStream();
/*      */       
/* 3997 */       int bc = -1;
/*      */       
/* 3999 */       if (useLength) {
/* 4000 */         bc = readblock(in, this.streamConvertBuf, streamLength);
/*      */       } else {
/* 4002 */         bc = readblock(in, this.streamConvertBuf);
/*      */       }
/*      */       
/* 4005 */       int lengthLeftToRead = streamLength - bc;
/*      */       
/* 4007 */       if (escape) {
/* 4008 */         if (this.connection.versionMeetsMinimum(4, 1, 0)) {
/* 4009 */           bytesOut.write(95);
/* 4010 */           bytesOut.write(98);
/* 4011 */           bytesOut.write(105);
/* 4012 */           bytesOut.write(110);
/* 4013 */           bytesOut.write(97);
/* 4014 */           bytesOut.write(114);
/* 4015 */           bytesOut.write(121);
/*      */         }
/*      */         
/* 4018 */         bytesOut.write(39);
/*      */       }
/*      */       
/* 4021 */       while (bc > 0) {
/* 4022 */         if (escape) {
/* 4023 */           escapeblockFast(this.streamConvertBuf, bytesOut, bc);
/*      */         } else {
/* 4025 */           bytesOut.write(this.streamConvertBuf, 0, bc);
/*      */         }
/*      */         
/* 4028 */         if (useLength) {
/* 4029 */           bc = readblock(in, this.streamConvertBuf, lengthLeftToRead);
/*      */           
/* 4031 */           if (bc > 0) {
/* 4032 */             lengthLeftToRead -= bc;
/*      */           }
/*      */         } else {
/* 4035 */           bc = readblock(in, this.streamConvertBuf);
/*      */         }
/*      */       }
/*      */       
/* 4039 */       if (escape) {
/* 4040 */         bytesOut.write(39);
/*      */       }
/*      */       
/* 4043 */       return bytesOut.toByteArray();
/*      */     } finally {
/* 4045 */       if (this.connection.getAutoClosePStmtStreams()) {
/*      */         try {
/* 4047 */           in.close();
/*      */         }
/*      */         catch (IOException ioEx) {}
/*      */         
/*      */ 
/* 4052 */         in = null;
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
/* 4063 */     StringBuffer buf = new StringBuffer();
/* 4064 */     buf.append(super.toString());
/* 4065 */     buf.append(": ");
/*      */     try
/*      */     {
/* 4068 */       buf.append(asSql());
/*      */     } catch (SQLException sqlEx) {
/* 4070 */       buf.append("EXCEPTION: " + sqlEx.toString());
/*      */     }
/*      */     
/* 4073 */     return buf.toString();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected int getParameterIndexOffset()
/*      */   {
/* 4084 */     return 0;
/*      */   }
/*      */ }


/* Location:              E:\java\java学习\hutubill\lib\all.jar!\com\mysql\jdbc\PreparedStatement.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version:       0.7.1
 */
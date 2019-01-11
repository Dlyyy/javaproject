/*      */ package com.mysql.jdbc;
/*      */ 
/*      */ import com.mysql.jdbc.log.Log;
/*      */ import com.mysql.jdbc.profiler.ProfileEventSink;
/*      */ import com.mysql.jdbc.profiler.ProfilerEvent;
/*      */ import com.mysql.jdbc.util.ReadAheadInputStream;
/*      */ import com.mysql.jdbc.util.ResultSetUtil;
/*      */ import java.io.BufferedInputStream;
/*      */ import java.io.BufferedOutputStream;
/*      */ import java.io.ByteArrayOutputStream;
/*      */ import java.io.EOFException;
/*      */ import java.io.FileInputStream;
/*      */ import java.io.IOException;
/*      */ import java.io.InputStream;
/*      */ import java.io.OutputStreamWriter;
/*      */ import java.lang.ref.SoftReference;
/*      */ import java.math.BigInteger;
/*      */ import java.net.MalformedURLException;
/*      */ import java.net.Socket;
/*      */ import java.net.URL;
/*      */ import java.security.NoSuchAlgorithmException;
/*      */ import java.sql.SQLException;
/*      */ import java.util.ArrayList;
/*      */ import java.util.Calendar;
/*      */ import java.util.Iterator;
/*      */ import java.util.LinkedList;
/*      */ import java.util.List;
/*      */ import java.util.Properties;
/*      */ import java.util.zip.Deflater;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ class MysqlIO
/*      */ {
/*      */   protected static final int NULL_LENGTH = -1;
/*      */   protected static final int COMP_HEADER_LENGTH = 3;
/*      */   protected static final int MIN_COMPRESS_LEN = 50;
/*      */   protected static final int HEADER_LENGTH = 4;
/*      */   protected static final int AUTH_411_OVERHEAD = 33;
/*   72 */   private static int maxBufferSize = 65535;
/*      */   
/*      */   private static final int CLIENT_COMPRESS = 32;
/*      */   
/*      */   protected static final int CLIENT_CONNECT_WITH_DB = 8;
/*      */   
/*      */   private static final int CLIENT_FOUND_ROWS = 2;
/*      */   
/*      */   private static final int CLIENT_LOCAL_FILES = 128;
/*      */   
/*      */   private static final int CLIENT_LONG_FLAG = 4;
/*      */   
/*      */   private static final int CLIENT_LONG_PASSWORD = 1;
/*      */   
/*      */   private static final int CLIENT_PROTOCOL_41 = 512;
/*      */   
/*      */   private static final int CLIENT_INTERACTIVE = 1024;
/*      */   
/*      */   protected static final int CLIENT_SSL = 2048;
/*      */   
/*      */   private static final int CLIENT_TRANSACTIONS = 8192;
/*      */   protected static final int CLIENT_RESERVED = 16384;
/*      */   protected static final int CLIENT_SECURE_CONNECTION = 32768;
/*      */   private static final int CLIENT_MULTI_QUERIES = 65536;
/*      */   private static final int CLIENT_MULTI_RESULTS = 131072;
/*      */   private static final int SERVER_STATUS_IN_TRANS = 1;
/*      */   private static final int SERVER_STATUS_AUTOCOMMIT = 2;
/*      */   private static final int SERVER_MORE_RESULTS_EXISTS = 8;
/*      */   private static final int SERVER_QUERY_NO_GOOD_INDEX_USED = 16;
/*      */   private static final int SERVER_QUERY_NO_INDEX_USED = 32;
/*      */   private static final int SERVER_STATUS_CURSOR_EXISTS = 64;
/*      */   private static final String FALSE_SCRAMBLE = "xxxxxxxx";
/*      */   protected static final int MAX_QUERY_SIZE_TO_LOG = 1024;
/*      */   protected static final int MAX_QUERY_SIZE_TO_EXPLAIN = 1048576;
/*      */   protected static final int INITIAL_PACKET_SIZE = 1024;
/*  107 */   private static String jvmPlatformCharset = null;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*  112 */   private boolean binaryResultsAreUnpacked = true;
/*      */   
/*      */   protected static final String ZERO_DATE_VALUE_MARKER = "0000-00-00";
/*      */   
/*      */   protected static final String ZERO_DATETIME_VALUE_MARKER = "0000-00-00 00:00:00";
/*      */   
/*      */   private static final int MAX_PACKET_DUMP_LENGTH = 1024;
/*      */   
/*      */   static
/*      */   {
/*  122 */     OutputStreamWriter outWriter = null;
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     try
/*      */     {
/*  130 */       outWriter = new OutputStreamWriter(new ByteArrayOutputStream());
/*  131 */       jvmPlatformCharset = outWriter.getEncoding();
/*      */     } finally {
/*      */       try {
/*  134 */         if (outWriter != null) {
/*  135 */           outWriter.close();
/*      */         }
/*      */       }
/*      */       catch (IOException ioEx) {}
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*  145 */   private boolean packetSequenceReset = false;
/*      */   
/*      */ 
/*      */ 
/*      */   protected int serverCharsetIndex;
/*      */   
/*      */ 
/*      */ 
/*  153 */   private Buffer reusablePacket = null;
/*  154 */   private Buffer sendPacket = null;
/*  155 */   private Buffer sharedSendPacket = null;
/*      */   
/*      */ 
/*  158 */   protected BufferedOutputStream mysqlOutput = null;
/*      */   protected Connection connection;
/*  160 */   private Deflater deflater = null;
/*  161 */   protected InputStream mysqlInput = null;
/*  162 */   private LinkedList packetDebugRingBuffer = null;
/*  163 */   private RowData streamingData = null;
/*      */   
/*      */ 
/*  166 */   protected Socket mysqlConnection = null;
/*  167 */   private SocketFactory socketFactory = null;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   private SoftReference loadFileBufRef;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   private SoftReference splitBufRef;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*  183 */   protected String host = null;
/*      */   protected String seed;
/*  185 */   private String serverVersion = null;
/*  186 */   private String socketFactoryClassName = null;
/*  187 */   private byte[] packetHeaderBuf = new byte[4];
/*  188 */   private boolean colDecimalNeedsBump = false;
/*  189 */   private boolean hadWarnings = false;
/*  190 */   private boolean has41NewNewProt = false;
/*      */   
/*      */ 
/*  193 */   private boolean hasLongColumnInfo = false;
/*  194 */   private boolean isInteractiveClient = false;
/*  195 */   private boolean logSlowQueries = false;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  201 */   private boolean platformDbCharsetMatches = true;
/*  202 */   private boolean profileSql = false;
/*  203 */   private boolean queryBadIndexUsed = false;
/*  204 */   private boolean queryNoIndexUsed = false;
/*      */   
/*      */ 
/*  207 */   private boolean use41Extensions = false;
/*  208 */   private boolean useCompression = false;
/*  209 */   private boolean useNewLargePackets = false;
/*  210 */   private boolean useNewUpdateCounts = false;
/*  211 */   private byte packetSequence = 0;
/*  212 */   private byte readPacketSequence = -1;
/*  213 */   private boolean checkPacketSequence = false;
/*  214 */   byte protocolVersion = 0;
/*  215 */   private int maxAllowedPacket = 1048576;
/*  216 */   protected int maxThreeBytes = 16581375;
/*  217 */   protected int port = 3306;
/*      */   protected int serverCapabilities;
/*  219 */   private int serverMajorVersion = 0;
/*  220 */   private int serverMinorVersion = 0;
/*  221 */   private int serverStatus = 0;
/*  222 */   private int serverSubMinorVersion = 0;
/*  223 */   private int warningCount = 0;
/*  224 */   protected long clientParam = 0L;
/*  225 */   protected long lastPacketSentTimeMs = 0L;
/*  226 */   private boolean traceProtocol = false;
/*  227 */   private boolean enablePacketDebug = false;
/*      */   
/*      */ 
/*      */   private Calendar sessionCalendar;
/*      */   
/*      */ 
/*      */   private boolean useConnectWithDb;
/*      */   
/*      */ 
/*      */   private boolean needToGrabQueryFromPacket;
/*      */   
/*      */ 
/*      */   private boolean autoGenerateTestcaseScript;
/*      */   
/*      */ 
/*      */   private long threadId;
/*      */   
/*      */   private boolean useNanosForElapsedTime;
/*      */   
/*      */   private long slowQueryThreshold;
/*      */   
/*      */   private String queryTimingUnits;
/*      */   
/*      */ 
/*      */   public MysqlIO(String host, int port, Properties props, String socketFactoryClassName, Connection conn, int socketTimeout)
/*      */     throws IOException, SQLException
/*      */   {
/*  254 */     this.connection = conn;
/*      */     
/*  256 */     if (this.connection.getEnablePacketDebug()) {
/*  257 */       this.packetDebugRingBuffer = new LinkedList();
/*      */     }
/*      */     
/*  260 */     this.logSlowQueries = this.connection.getLogSlowQueries();
/*      */     
/*  262 */     this.reusablePacket = new Buffer(1024);
/*  263 */     this.sendPacket = new Buffer(1024);
/*      */     
/*  265 */     this.port = port;
/*  266 */     this.host = host;
/*      */     
/*  268 */     this.socketFactoryClassName = socketFactoryClassName;
/*  269 */     this.socketFactory = createSocketFactory();
/*      */     
/*  271 */     this.mysqlConnection = this.socketFactory.connect(this.host, this.port, props);
/*      */     
/*      */ 
/*  274 */     if (socketTimeout != 0) {
/*      */       try {
/*  276 */         this.mysqlConnection.setSoTimeout(socketTimeout);
/*      */       }
/*      */       catch (Exception ex) {}
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*  283 */     this.mysqlConnection = this.socketFactory.beforeHandshake();
/*      */     
/*  285 */     if (this.connection.getUseReadAheadInput()) {
/*  286 */       this.mysqlInput = new ReadAheadInputStream(this.mysqlConnection.getInputStream(), 16384, this.connection.getTraceProtocol(), this.connection.getLog());
/*      */ 
/*      */     }
/*  289 */     else if (this.connection.useUnbufferedInput()) {
/*  290 */       this.mysqlInput = this.mysqlConnection.getInputStream();
/*      */     } else {
/*  292 */       this.mysqlInput = new BufferedInputStream(this.mysqlConnection.getInputStream(), 16384);
/*      */     }
/*      */     
/*      */ 
/*  296 */     this.mysqlOutput = new BufferedOutputStream(this.mysqlConnection.getOutputStream(), 16384);
/*      */     
/*      */ 
/*  299 */     this.isInteractiveClient = this.connection.getInteractiveClient();
/*  300 */     this.profileSql = this.connection.getProfileSql();
/*  301 */     this.sessionCalendar = Calendar.getInstance();
/*  302 */     this.autoGenerateTestcaseScript = this.connection.getAutoGenerateTestcaseScript();
/*      */     
/*      */ 
/*  305 */     this.needToGrabQueryFromPacket = ((this.profileSql) || (this.logSlowQueries) || (this.autoGenerateTestcaseScript));
/*      */     
/*      */ 
/*  308 */     if ((this.connection.getUseNanosForElapsedTime()) && (Util.nanoTimeAvailable()))
/*      */     {
/*  310 */       this.useNanosForElapsedTime = true;
/*      */       
/*  312 */       this.queryTimingUnits = Messages.getString("Nanoseconds");
/*      */     } else {
/*  314 */       this.queryTimingUnits = Messages.getString("Milliseconds");
/*      */     }
/*      */     
/*  317 */     if (this.connection.getLogSlowQueries()) {
/*  318 */       calculateSlowQueryThreshold();
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public boolean hasLongColumnInfo()
/*      */   {
/*  328 */     return this.hasLongColumnInfo;
/*      */   }
/*      */   
/*      */   protected boolean isDataAvailable() throws SQLException {
/*      */     try {
/*  333 */       return this.mysqlInput.available() > 0;
/*      */     } catch (IOException ioEx) {
/*  335 */       throw new CommunicationsException(this.connection, this.lastPacketSentTimeMs, ioEx);
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected long getLastPacketSentTimeMs()
/*      */   {
/*  346 */     return this.lastPacketSentTimeMs;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected ResultSet getResultSet(Statement callingStatement, long columnCount, int maxRows, int resultSetType, int resultSetConcurrency, boolean streamResults, String catalog, boolean isBinaryEncoded, boolean unpackFieldInfo, Field[] metadataFromCache)
/*      */     throws SQLException
/*      */   {
/*  376 */     Field[] fields = null;
/*      */     
/*      */ 
/*      */ 
/*  380 */     if (unpackFieldInfo) {
/*  381 */       fields = new Field[(int)columnCount];
/*      */       
/*  383 */       for (int i = 0; i < columnCount; i++) {
/*  384 */         Buffer fieldPacket = null;
/*      */         
/*  386 */         fieldPacket = readPacket();
/*  387 */         fields[i] = unpackField(fieldPacket, false);
/*      */       }
/*      */     } else {
/*  390 */       for (int i = 0; i < columnCount; i++) {
/*  391 */         skipPacket();
/*      */       }
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*  397 */     Buffer packet = reuseAndReadPacket(this.reusablePacket);
/*      */     
/*  399 */     readServerStatusForResultSets(packet);
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  405 */     if ((this.connection.versionMeetsMinimum(5, 0, 2)) && (this.connection.getUseCursorFetch()) && (isBinaryEncoded) && (callingStatement != null) && (callingStatement.getFetchSize() != 0) && (callingStatement.getResultSetType() == 1003))
/*      */     {
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  411 */       ServerPreparedStatement prepStmt = (ServerPreparedStatement)callingStatement;
/*      */       
/*  413 */       Field[] fieldMetadata = ((ResultSetMetaData)prepStmt.getMetaData()).fields;
/*      */       
/*  415 */       boolean usingCursor = true;
/*      */       
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  423 */       if (this.connection.versionMeetsMinimum(5, 0, 5)) {
/*  424 */         usingCursor = (this.serverStatus & 0x40) != 0;
/*      */       }
/*      */       
/*      */ 
/*  428 */       if (usingCursor) {
/*  429 */         RowData rows = new CursorRowProvider(this, prepStmt, fields);
/*      */         
/*      */ 
/*      */ 
/*      */ 
/*  434 */         ResultSet rs = buildResultSetWithRows(callingStatement, catalog, fields, rows, resultSetType, resultSetConcurrency, isBinaryEncoded);
/*      */         
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  440 */         if (usingCursor) {
/*  441 */           rs.setFetchSize(callingStatement.getFetchSize());
/*      */         }
/*      */         
/*  444 */         return rs;
/*      */       }
/*      */     }
/*      */     
/*  448 */     RowData rowData = null;
/*      */     
/*  450 */     if (!streamResults) {
/*  451 */       rowData = readSingleRowSet(columnCount, maxRows, resultSetConcurrency, isBinaryEncoded, unpackFieldInfo ? fields : metadataFromCache);
/*      */     }
/*      */     else {
/*  454 */       rowData = new RowDataDynamic(this, (int)columnCount, unpackFieldInfo ? fields : metadataFromCache, isBinaryEncoded);
/*      */       
/*  456 */       this.streamingData = rowData;
/*      */     }
/*      */     
/*  459 */     ResultSet rs = buildResultSetWithRows(callingStatement, catalog, fields, rowData, resultSetType, resultSetConcurrency, isBinaryEncoded);
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*  464 */     return rs;
/*      */   }
/*      */   
/*      */ 
/*      */   protected final void forceClose()
/*      */   {
/*      */     try
/*      */     {
/*  472 */       if (this.mysqlInput != null) {
/*  473 */         this.mysqlInput.close();
/*      */       }
/*      */     }
/*      */     catch (IOException ioEx)
/*      */     {
/*  478 */       this.mysqlInput = null;
/*      */     }
/*      */     try
/*      */     {
/*  482 */       if (this.mysqlOutput != null) {
/*  483 */         this.mysqlOutput.close();
/*      */       }
/*      */     }
/*      */     catch (IOException ioEx)
/*      */     {
/*  488 */       this.mysqlOutput = null;
/*      */     }
/*      */     try
/*      */     {
/*  492 */       if (this.mysqlConnection != null) {
/*  493 */         this.mysqlConnection.close();
/*      */       }
/*      */     }
/*      */     catch (IOException ioEx)
/*      */     {
/*  498 */       this.mysqlConnection = null;
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
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected final Field unpackField(Buffer packet, boolean extractDefaultValues)
/*      */     throws SQLException
/*      */   {
/*  665 */     if (this.use41Extensions)
/*      */     {
/*      */ 
/*  668 */       if (this.has41NewNewProt)
/*      */       {
/*  670 */         int catalogNameStart = packet.getPosition() + 1;
/*  671 */         int catalogNameLength = packet.fastSkipLenString();
/*  672 */         catalogNameStart = adjustStartForFieldLength(catalogNameStart, catalogNameLength);
/*      */       }
/*      */       
/*  675 */       int databaseNameStart = packet.getPosition() + 1;
/*  676 */       int databaseNameLength = packet.fastSkipLenString();
/*  677 */       databaseNameStart = adjustStartForFieldLength(databaseNameStart, databaseNameLength);
/*      */       
/*  679 */       int tableNameStart = packet.getPosition() + 1;
/*  680 */       int tableNameLength = packet.fastSkipLenString();
/*  681 */       tableNameStart = adjustStartForFieldLength(tableNameStart, tableNameLength);
/*      */       
/*      */ 
/*  684 */       int originalTableNameStart = packet.getPosition() + 1;
/*  685 */       int originalTableNameLength = packet.fastSkipLenString();
/*  686 */       originalTableNameStart = adjustStartForFieldLength(originalTableNameStart, originalTableNameLength);
/*      */       
/*      */ 
/*  689 */       int nameStart = packet.getPosition() + 1;
/*  690 */       int nameLength = packet.fastSkipLenString();
/*      */       
/*  692 */       nameStart = adjustStartForFieldLength(nameStart, nameLength);
/*      */       
/*      */ 
/*  695 */       int originalColumnNameStart = packet.getPosition() + 1;
/*  696 */       int originalColumnNameLength = packet.fastSkipLenString();
/*  697 */       originalColumnNameStart = adjustStartForFieldLength(originalColumnNameStart, originalColumnNameLength);
/*      */       
/*  699 */       packet.readByte();
/*      */       
/*  701 */       short charSetNumber = (short)packet.readInt();
/*      */       
/*  703 */       long colLength = 0L;
/*      */       
/*  705 */       if (this.has41NewNewProt) {
/*  706 */         colLength = packet.readLong();
/*      */       } else {
/*  708 */         colLength = packet.readLongInt();
/*      */       }
/*      */       
/*  711 */       int colType = packet.readByte() & 0xFF;
/*      */       
/*  713 */       short colFlag = 0;
/*      */       
/*  715 */       if (this.hasLongColumnInfo) {
/*  716 */         colFlag = (short)packet.readInt();
/*      */       } else {
/*  718 */         colFlag = (short)(packet.readByte() & 0xFF);
/*      */       }
/*      */       
/*  721 */       int colDecimals = packet.readByte() & 0xFF;
/*      */       
/*  723 */       int defaultValueStart = -1;
/*  724 */       int defaultValueLength = -1;
/*      */       
/*  726 */       if (extractDefaultValues) {
/*  727 */         defaultValueStart = packet.getPosition() + 1;
/*  728 */         defaultValueLength = packet.fastSkipLenString();
/*      */       }
/*      */       
/*  731 */       Field field = new Field(this.connection, packet.getByteBuffer(), databaseNameStart, databaseNameLength, tableNameStart, tableNameLength, originalTableNameStart, originalTableNameLength, nameStart, nameLength, originalColumnNameStart, originalColumnNameLength, colLength, colType, colFlag, colDecimals, defaultValueStart, defaultValueLength, charSetNumber);
/*      */       
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  739 */       return field;
/*      */     }
/*      */     
/*  742 */     int tableNameStart = packet.getPosition() + 1;
/*  743 */     int tableNameLength = packet.fastSkipLenString();
/*  744 */     tableNameStart = adjustStartForFieldLength(tableNameStart, tableNameLength);
/*      */     
/*  746 */     int nameStart = packet.getPosition() + 1;
/*  747 */     int nameLength = packet.fastSkipLenString();
/*  748 */     nameStart = adjustStartForFieldLength(nameStart, nameLength);
/*      */     
/*  750 */     int colLength = packet.readnBytes();
/*  751 */     int colType = packet.readnBytes();
/*  752 */     packet.readByte();
/*      */     
/*  754 */     short colFlag = 0;
/*      */     
/*  756 */     if (this.hasLongColumnInfo) {
/*  757 */       colFlag = (short)packet.readInt();
/*      */     } else {
/*  759 */       colFlag = (short)(packet.readByte() & 0xFF);
/*      */     }
/*      */     
/*  762 */     int colDecimals = packet.readByte() & 0xFF;
/*      */     
/*  764 */     if (this.colDecimalNeedsBump) {
/*  765 */       colDecimals++;
/*      */     }
/*      */     
/*  768 */     Field field = new Field(this.connection, packet.getByteBuffer(), nameStart, nameLength, tableNameStart, tableNameLength, colLength, colType, colFlag, colDecimals);
/*      */     
/*      */ 
/*      */ 
/*  772 */     return field;
/*      */   }
/*      */   
/*      */   private int adjustStartForFieldLength(int nameStart, int nameLength) {
/*  776 */     if (nameLength < 251) {
/*  777 */       return nameStart;
/*      */     }
/*      */     
/*  780 */     if ((nameLength >= 251) && (nameLength < 65536)) {
/*  781 */       return nameStart + 2;
/*      */     }
/*      */     
/*  784 */     if ((nameLength >= 65536) && (nameLength < 16777216)) {
/*  785 */       return nameStart + 3;
/*      */     }
/*      */     
/*  788 */     return nameStart + 8;
/*      */   }
/*      */   
/*      */   protected boolean isSetNeededForAutoCommitMode(boolean autoCommitFlag) {
/*  792 */     if ((this.use41Extensions) && (this.connection.getElideSetAutoCommits())) {
/*  793 */       boolean autoCommitModeOnServer = (this.serverStatus & 0x2) != 0;
/*      */       
/*      */ 
/*  796 */       if ((!autoCommitFlag) && (versionMeetsMinimum(5, 0, 0)))
/*      */       {
/*      */ 
/*      */ 
/*  800 */         boolean inTransactionOnServer = (this.serverStatus & 0x1) != 0;
/*      */         
/*      */ 
/*  803 */         return !inTransactionOnServer;
/*      */       }
/*      */       
/*  806 */       return autoCommitModeOnServer != autoCommitFlag;
/*      */     }
/*      */     
/*  809 */     return true;
/*      */   }
/*      */   
/*      */   protected boolean inTransactionOnServer() {
/*  813 */     return (this.serverStatus & 0x1) != 0;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected void changeUser(String userName, String password, String database)
/*      */     throws SQLException
/*      */   {
/*  827 */     this.packetSequence = -1;
/*      */     
/*  829 */     int passwordLength = 16;
/*  830 */     int userLength = userName != null ? userName.length() : 0;
/*  831 */     int databaseLength = database != null ? database.length() : 0;
/*      */     
/*  833 */     int packLength = (userLength + passwordLength + databaseLength) * 2 + 7 + 4 + 33;
/*      */     
/*  835 */     if ((this.serverCapabilities & 0x8000) != 0) {
/*  836 */       Buffer changeUserPacket = new Buffer(packLength + 1);
/*  837 */       changeUserPacket.writeByte((byte)17);
/*      */       
/*  839 */       if (versionMeetsMinimum(4, 1, 1)) {
/*  840 */         secureAuth411(changeUserPacket, packLength, userName, password, database, false);
/*      */       }
/*      */       else {
/*  843 */         secureAuth(changeUserPacket, packLength, userName, password, database, false);
/*      */       }
/*      */     }
/*      */     else
/*      */     {
/*  848 */       Buffer packet = new Buffer(packLength);
/*  849 */       packet.writeByte((byte)17);
/*      */       
/*      */ 
/*  852 */       packet.writeString(userName);
/*      */       
/*  854 */       if (this.protocolVersion > 9) {
/*  855 */         packet.writeString(Util.newCrypt(password, this.seed));
/*      */       } else {
/*  857 */         packet.writeString(Util.oldCrypt(password, this.seed));
/*      */       }
/*      */       
/*  860 */       boolean localUseConnectWithDb = (this.useConnectWithDb) && (database != null) && (database.length() > 0);
/*      */       
/*      */ 
/*  863 */       if (localUseConnectWithDb) {
/*  864 */         packet.writeString(database);
/*      */       }
/*      */       
/*  867 */       send(packet, packet.getPosition());
/*  868 */       checkErrorPacket();
/*      */       
/*  870 */       if (!localUseConnectWithDb) {
/*  871 */         changeDatabaseTo(database);
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
/*      */   protected Buffer checkErrorPacket()
/*      */     throws SQLException
/*      */   {
/*  885 */     return checkErrorPacket(-1);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   protected void checkForCharsetMismatch()
/*      */   {
/*  892 */     if ((this.connection.getUseUnicode()) && (this.connection.getEncoding() != null))
/*      */     {
/*  894 */       String encodingToCheck = jvmPlatformCharset;
/*      */       
/*  896 */       if (encodingToCheck == null) {
/*  897 */         encodingToCheck = System.getProperty("file.encoding");
/*      */       }
/*      */       
/*  900 */       if (encodingToCheck == null) {
/*  901 */         this.platformDbCharsetMatches = false;
/*      */       } else {
/*  903 */         this.platformDbCharsetMatches = encodingToCheck.equals(this.connection.getEncoding());
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */   protected void clearInputStream() throws SQLException
/*      */   {
/*      */     try {
/*  911 */       int len = this.mysqlInput.available();
/*      */       
/*  913 */       while (len > 0) {
/*  914 */         this.mysqlInput.skip(len);
/*  915 */         len = this.mysqlInput.available();
/*      */       }
/*      */     } catch (IOException ioEx) {
/*  918 */       throw new CommunicationsException(this.connection, this.lastPacketSentTimeMs, ioEx);
/*      */     }
/*      */   }
/*      */   
/*      */   protected void resetReadPacketSequence()
/*      */   {
/*  924 */     this.readPacketSequence = 0;
/*      */   }
/*      */   
/*      */   protected void dumpPacketRingBuffer() throws SQLException {
/*  928 */     if ((this.packetDebugRingBuffer != null) && (this.connection.getEnablePacketDebug()))
/*      */     {
/*  930 */       StringBuffer dumpBuffer = new StringBuffer();
/*      */       
/*  932 */       dumpBuffer.append("Last " + this.packetDebugRingBuffer.size() + " packets received from server, from oldest->newest:\n");
/*      */       
/*  934 */       dumpBuffer.append("\n");
/*      */       
/*  936 */       Iterator ringBufIter = this.packetDebugRingBuffer.iterator();
/*  937 */       while (ringBufIter.hasNext()) {
/*  938 */         dumpBuffer.append((StringBuffer)ringBufIter.next());
/*  939 */         dumpBuffer.append("\n");
/*      */       }
/*      */       
/*  942 */       this.connection.getLog().logTrace(dumpBuffer.toString());
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
/*      */   protected void explainSlowQuery(byte[] querySQL, String truncatedQuery)
/*      */     throws SQLException
/*      */   {
/*  956 */     if (StringUtils.startsWithIgnoreCaseAndWs(truncatedQuery, "SELECT"))
/*      */     {
/*  958 */       PreparedStatement stmt = null;
/*  959 */       java.sql.ResultSet rs = null;
/*      */       try
/*      */       {
/*  962 */         stmt = this.connection.clientPrepareStatement("EXPLAIN ?");
/*  963 */         stmt.setBytesNoEscapeNoQuotes(1, querySQL);
/*  964 */         rs = stmt.executeQuery();
/*      */         
/*  966 */         StringBuffer explainResults = new StringBuffer(Messages.getString("MysqlIO.8") + truncatedQuery + Messages.getString("MysqlIO.9"));
/*      */         
/*      */ 
/*      */ 
/*  970 */         ResultSetUtil.appendResultSetSlashGStyle(explainResults, rs);
/*      */         
/*  972 */         this.connection.getLog().logWarn(explainResults.toString());
/*      */       }
/*      */       catch (SQLException sqlEx) {}finally {
/*  975 */         if (rs != null) {
/*  976 */           rs.close();
/*      */         }
/*      */         
/*  979 */         if (stmt != null) {
/*  980 */           stmt.close();
/*      */         }
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */   static int getMaxBuf()
/*      */   {
/*  988 */     return maxBufferSize;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   final int getServerMajorVersion()
/*      */   {
/*  997 */     return this.serverMajorVersion;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   final int getServerMinorVersion()
/*      */   {
/* 1006 */     return this.serverMinorVersion;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   final int getServerSubMinorVersion()
/*      */   {
/* 1015 */     return this.serverSubMinorVersion;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   String getServerVersion()
/*      */   {
/* 1024 */     return this.serverVersion;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   void doHandshake(String user, String password, String database)
/*      */     throws SQLException
/*      */   {
/* 1041 */     this.checkPacketSequence = false;
/* 1042 */     this.readPacketSequence = 0;
/*      */     
/* 1044 */     Buffer buf = readPacket();
/*      */     
/*      */ 
/* 1047 */     this.protocolVersion = buf.readByte();
/*      */     
/* 1049 */     if (this.protocolVersion == -1) {
/*      */       try {
/* 1051 */         this.mysqlConnection.close();
/*      */       }
/*      */       catch (Exception e) {}
/*      */       
/*      */ 
/* 1056 */       int errno = 2000;
/*      */       
/* 1058 */       errno = buf.readInt();
/*      */       
/* 1060 */       String serverErrorMessage = buf.readString();
/*      */       
/* 1062 */       StringBuffer errorBuf = new StringBuffer(Messages.getString("MysqlIO.10"));
/*      */       
/* 1064 */       errorBuf.append(serverErrorMessage);
/* 1065 */       errorBuf.append("\"");
/*      */       
/* 1067 */       String xOpen = SQLError.mysqlToSqlState(errno, this.connection.getUseSqlStateCodes());
/*      */       
/*      */ 
/* 1070 */       throw SQLError.createSQLException(SQLError.get(xOpen) + ", " + errorBuf.toString(), xOpen, errno);
/*      */     }
/*      */     
/*      */ 
/* 1074 */     this.serverVersion = buf.readString();
/*      */     
/*      */ 
/* 1077 */     int point = this.serverVersion.indexOf(".");
/*      */     
/* 1079 */     if (point != -1) {
/*      */       try {
/* 1081 */         int n = Integer.parseInt(this.serverVersion.substring(0, point));
/* 1082 */         this.serverMajorVersion = n;
/*      */       }
/*      */       catch (NumberFormatException NFE1) {}
/*      */       
/*      */ 
/* 1087 */       String remaining = this.serverVersion.substring(point + 1, this.serverVersion.length());
/*      */       
/* 1089 */       point = remaining.indexOf(".");
/*      */       
/* 1091 */       if (point != -1) {
/*      */         try {
/* 1093 */           int n = Integer.parseInt(remaining.substring(0, point));
/* 1094 */           this.serverMinorVersion = n;
/*      */         }
/*      */         catch (NumberFormatException nfe) {}
/*      */         
/*      */ 
/* 1099 */         remaining = remaining.substring(point + 1, remaining.length());
/*      */         
/* 1101 */         int pos = 0;
/*      */         
/* 1103 */         while ((pos < remaining.length()) && 
/* 1104 */           (remaining.charAt(pos) >= '0') && (remaining.charAt(pos) <= '9'))
/*      */         {
/*      */ 
/*      */ 
/*      */ 
/* 1109 */           pos++;
/*      */         }
/*      */         try
/*      */         {
/* 1113 */           int n = Integer.parseInt(remaining.substring(0, pos));
/* 1114 */           this.serverSubMinorVersion = n;
/*      */         }
/*      */         catch (NumberFormatException nfe) {}
/*      */       }
/*      */     }
/*      */     
/*      */ 
/* 1121 */     if (versionMeetsMinimum(4, 0, 8)) {
/* 1122 */       this.maxThreeBytes = 16777215;
/* 1123 */       this.useNewLargePackets = true;
/*      */     } else {
/* 1125 */       this.maxThreeBytes = 16581375;
/* 1126 */       this.useNewLargePackets = false;
/*      */     }
/*      */     
/* 1129 */     this.colDecimalNeedsBump = versionMeetsMinimum(3, 23, 0);
/* 1130 */     this.colDecimalNeedsBump = (!versionMeetsMinimum(3, 23, 15));
/* 1131 */     this.useNewUpdateCounts = versionMeetsMinimum(3, 22, 5);
/*      */     
/* 1133 */     this.threadId = buf.readLong();
/* 1134 */     this.seed = buf.readString();
/*      */     
/* 1136 */     this.serverCapabilities = 0;
/*      */     
/* 1138 */     if (buf.getPosition() < buf.getBufLength()) {
/* 1139 */       this.serverCapabilities = buf.readInt();
/*      */     }
/*      */     
/* 1142 */     if (versionMeetsMinimum(4, 1, 1)) {
/* 1143 */       int position = buf.getPosition();
/*      */       
/*      */ 
/* 1146 */       this.serverCharsetIndex = (buf.readByte() & 0xFF);
/* 1147 */       this.serverStatus = buf.readInt();
/* 1148 */       buf.setPosition(position + 16);
/*      */       
/* 1150 */       String seedPart2 = buf.readString();
/* 1151 */       StringBuffer newSeed = new StringBuffer(20);
/* 1152 */       newSeed.append(this.seed);
/* 1153 */       newSeed.append(seedPart2);
/* 1154 */       this.seed = newSeed.toString();
/*      */     }
/*      */     
/* 1157 */     if (((this.serverCapabilities & 0x20) != 0) && (this.connection.getUseCompression()))
/*      */     {
/* 1159 */       this.clientParam |= 0x20;
/*      */     }
/*      */     
/* 1162 */     this.useConnectWithDb = ((database != null) && (database.length() > 0) && (!this.connection.getCreateDatabaseIfNotExist()));
/*      */     
/*      */ 
/*      */ 
/* 1166 */     if (this.useConnectWithDb) {
/* 1167 */       this.clientParam |= 0x8;
/*      */     }
/*      */     
/* 1170 */     if (((this.serverCapabilities & 0x800) == 0) && (this.connection.getUseSSL()))
/*      */     {
/* 1172 */       if (this.connection.getRequireSSL()) {
/* 1173 */         this.connection.close();
/* 1174 */         forceClose();
/* 1175 */         throw SQLError.createSQLException(Messages.getString("MysqlIO.15"), "08001");
/*      */       }
/*      */       
/*      */ 
/* 1179 */       this.connection.setUseSSL(false);
/*      */     }
/*      */     
/* 1182 */     if ((this.serverCapabilities & 0x4) != 0)
/*      */     {
/* 1184 */       this.clientParam |= 0x4;
/* 1185 */       this.hasLongColumnInfo = true;
/*      */     }
/*      */     
/*      */ 
/* 1189 */     this.clientParam |= 0x2;
/*      */     
/* 1191 */     if (this.connection.getAllowLoadLocalInfile()) {
/* 1192 */       this.clientParam |= 0x80;
/*      */     }
/*      */     
/* 1195 */     if (this.isInteractiveClient) {
/* 1196 */       this.clientParam |= 0x400;
/*      */     }
/*      */     
/*      */ 
/* 1200 */     if (this.protocolVersion > 9) {
/* 1201 */       this.clientParam |= 1L;
/*      */     } else {
/* 1203 */       this.clientParam &= 0xFFFFFFFFFFFFFFFE;
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/* 1209 */     if (versionMeetsMinimum(4, 1, 0)) {
/* 1210 */       if (versionMeetsMinimum(4, 1, 1)) {
/* 1211 */         this.clientParam |= 0x200;
/* 1212 */         this.has41NewNewProt = true;
/*      */         
/*      */ 
/* 1215 */         this.clientParam |= 0x2000;
/*      */         
/*      */ 
/* 1218 */         this.clientParam |= 0x20000;
/*      */         
/*      */ 
/*      */ 
/*      */ 
/* 1223 */         if (this.connection.getAllowMultiQueries()) {
/* 1224 */           this.clientParam |= 0x10000;
/*      */         }
/*      */       } else {
/* 1227 */         this.clientParam |= 0x4000;
/* 1228 */         this.has41NewNewProt = false;
/*      */       }
/*      */       
/* 1231 */       this.use41Extensions = true;
/*      */     }
/*      */     
/* 1234 */     int passwordLength = 16;
/* 1235 */     int userLength = user != null ? user.length() : 0;
/* 1236 */     int databaseLength = database != null ? database.length() : 0;
/*      */     
/* 1238 */     int packLength = (userLength + passwordLength + databaseLength) * 2 + 7 + 4 + 33;
/*      */     
/* 1240 */     Buffer packet = null;
/*      */     
/* 1242 */     if (!this.connection.getUseSSL()) {
/* 1243 */       if ((this.serverCapabilities & 0x8000) != 0) {
/* 1244 */         this.clientParam |= 0x8000;
/*      */         
/* 1246 */         if (versionMeetsMinimum(4, 1, 1)) {
/* 1247 */           secureAuth411(null, packLength, user, password, database, true);
/*      */         }
/*      */         else {
/* 1250 */           secureAuth(null, packLength, user, password, database, true);
/*      */         }
/*      */       }
/*      */       else {
/* 1254 */         packet = new Buffer(packLength);
/*      */         
/* 1256 */         if ((this.clientParam & 0x4000) != 0L) {
/* 1257 */           if (versionMeetsMinimum(4, 1, 1)) {
/* 1258 */             packet.writeLong(this.clientParam);
/* 1259 */             packet.writeLong(this.maxThreeBytes);
/*      */             
/*      */ 
/*      */ 
/*      */ 
/* 1264 */             packet.writeByte((byte)8);
/*      */             
/*      */ 
/* 1267 */             packet.writeBytesNoNull(new byte[23]);
/*      */           } else {
/* 1269 */             packet.writeLong(this.clientParam);
/* 1270 */             packet.writeLong(this.maxThreeBytes);
/*      */           }
/*      */         } else {
/* 1273 */           packet.writeInt((int)this.clientParam);
/* 1274 */           packet.writeLongInt(this.maxThreeBytes);
/*      */         }
/*      */         
/*      */ 
/* 1278 */         packet.writeString(user, "Cp1252", this.connection);
/*      */         
/* 1280 */         if (this.protocolVersion > 9) {
/* 1281 */           packet.writeString(Util.newCrypt(password, this.seed), "Cp1252", this.connection);
/*      */         } else {
/* 1283 */           packet.writeString(Util.oldCrypt(password, this.seed), "Cp1252", this.connection);
/*      */         }
/*      */         
/* 1286 */         if (this.useConnectWithDb) {
/* 1287 */           packet.writeString(database, "Cp1252", this.connection);
/*      */         }
/*      */         
/* 1290 */         send(packet, packet.getPosition());
/*      */       }
/*      */     } else {
/* 1293 */       negotiateSSLConnection(user, password, database, packLength);
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/* 1299 */     if (!versionMeetsMinimum(4, 1, 1)) {
/* 1300 */       checkErrorPacket();
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/* 1306 */     if (((this.serverCapabilities & 0x20) != 0) && (this.connection.getUseCompression()))
/*      */     {
/*      */ 
/*      */ 
/* 1310 */       this.deflater = new Deflater();
/* 1311 */       this.useCompression = true;
/* 1312 */       this.mysqlInput = new CompressedInputStream(this.connection, this.mysqlInput);
/*      */     }
/*      */     
/*      */ 
/* 1316 */     if (!this.useConnectWithDb) {
/* 1317 */       changeDatabaseTo(database);
/*      */     }
/*      */   }
/*      */   
/*      */   private void changeDatabaseTo(String database) throws SQLException, CommunicationsException {
/* 1322 */     if ((database == null) || (database.length() == 0)) {
/* 1323 */       return;
/*      */     }
/*      */     try
/*      */     {
/* 1327 */       sendCommand(2, database, null, false, null);
/*      */     } catch (Exception ex) {
/* 1329 */       if (this.connection.getCreateDatabaseIfNotExist()) {
/* 1330 */         sendCommand(3, "CREATE DATABASE IF NOT EXISTS " + database, null, false, null);
/*      */         
/*      */ 
/* 1333 */         sendCommand(2, database, null, false, null);
/*      */       } else {
/* 1335 */         throw new CommunicationsException(this.connection, this.lastPacketSentTimeMs, ex);
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
/*      */   final Object[] nextRow(Field[] fields, int columnCount, boolean isBinaryEncoded, int resultSetConcurrency)
/*      */     throws SQLException
/*      */   {
/* 1360 */     Buffer rowPacket = checkErrorPacket();
/*      */     
/* 1362 */     if (!isBinaryEncoded)
/*      */     {
/*      */ 
/*      */ 
/*      */ 
/* 1367 */       rowPacket.setPosition(rowPacket.getPosition() - 1);
/*      */       
/* 1369 */       if (!rowPacket.isLastDataPacket()) {
/* 1370 */         byte[][] rowData = new byte[columnCount][];
/*      */         
/* 1372 */         int offset = 0;
/*      */         
/* 1374 */         for (int i = 0; i < columnCount; i++) {
/* 1375 */           rowData[i] = rowPacket.readLenByteArray(offset);
/*      */         }
/*      */         
/* 1378 */         return rowData;
/*      */       }
/*      */       
/* 1381 */       readServerStatusForResultSets(rowPacket);
/*      */       
/* 1383 */       return null;
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 1390 */     if (!rowPacket.isLastDataPacket()) {
/* 1391 */       return unpackBinaryResultSetRow(fields, rowPacket, resultSetConcurrency);
/*      */     }
/*      */     
/*      */ 
/* 1395 */     rowPacket.setPosition(rowPacket.getPosition() - 1);
/* 1396 */     readServerStatusForResultSets(rowPacket);
/*      */     
/* 1398 */     return null;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   final void quit()
/*      */     throws SQLException
/*      */   {
/* 1407 */     Buffer packet = new Buffer(6);
/* 1408 */     this.packetSequence = -1;
/* 1409 */     packet.writeByte((byte)1);
/* 1410 */     send(packet, packet.getPosition());
/* 1411 */     forceClose();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   Buffer getSharedSendPacket()
/*      */   {
/* 1421 */     if (this.sharedSendPacket == null) {
/* 1422 */       this.sharedSendPacket = new Buffer(1024);
/*      */     }
/*      */     
/* 1425 */     return this.sharedSendPacket;
/*      */   }
/*      */   
/*      */   void closeStreamer(RowData streamer) throws SQLException {
/* 1429 */     if (this.streamingData == null) {
/* 1430 */       throw SQLError.createSQLException(Messages.getString("MysqlIO.17") + streamer + Messages.getString("MysqlIO.18"));
/*      */     }
/*      */     
/*      */ 
/* 1434 */     if (streamer != this.streamingData) {
/* 1435 */       throw SQLError.createSQLException(Messages.getString("MysqlIO.19") + streamer + Messages.getString("MysqlIO.20") + Messages.getString("MysqlIO.21") + Messages.getString("MysqlIO.22"));
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/* 1441 */     this.streamingData = null;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   ResultSet readAllResults(Statement callingStatement, int maxRows, int resultSetType, int resultSetConcurrency, boolean streamResults, String catalog, Buffer resultPacket, boolean isBinaryEncoded, long preSentColumnCount, boolean unpackFieldInfo, Field[] metadataFromCache)
/*      */     throws SQLException
/*      */   {
/* 1449 */     resultPacket.setPosition(resultPacket.getPosition() - 1);
/*      */     
/* 1451 */     ResultSet topLevelResultSet = readResultsForQueryOrUpdate(callingStatement, maxRows, resultSetType, resultSetConcurrency, streamResults, catalog, resultPacket, isBinaryEncoded, preSentColumnCount, unpackFieldInfo, metadataFromCache);
/*      */     
/*      */ 
/*      */ 
/*      */ 
/* 1456 */     ResultSet currentResultSet = topLevelResultSet;
/*      */     
/* 1458 */     boolean checkForMoreResults = (this.clientParam & 0x20000) != 0L;
/*      */     
/*      */ 
/* 1461 */     boolean serverHasMoreResults = (this.serverStatus & 0x8) != 0;
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 1467 */     if ((serverHasMoreResults) && (streamResults)) {
/* 1468 */       clearInputStream();
/*      */       
/* 1470 */       throw SQLError.createSQLException(Messages.getString("MysqlIO.23"), "S1C00");
/*      */     }
/*      */     
/*      */ 
/* 1474 */     boolean moreRowSetsExist = checkForMoreResults & serverHasMoreResults;
/*      */     
/* 1476 */     while (moreRowSetsExist) {
/* 1477 */       Buffer fieldPacket = checkErrorPacket();
/* 1478 */       fieldPacket.setPosition(0);
/*      */       
/* 1480 */       ResultSet newResultSet = readResultsForQueryOrUpdate(callingStatement, maxRows, resultSetType, resultSetConcurrency, streamResults, catalog, fieldPacket, isBinaryEncoded, preSentColumnCount, unpackFieldInfo, metadataFromCache);
/*      */       
/*      */ 
/*      */ 
/*      */ 
/* 1485 */       currentResultSet.setNextResultSet(newResultSet);
/*      */       
/* 1487 */       currentResultSet = newResultSet;
/*      */       
/* 1489 */       moreRowSetsExist = (this.serverStatus & 0x8) != 0;
/*      */     }
/*      */     
/* 1492 */     if (!streamResults) {
/* 1493 */       clearInputStream();
/*      */     }
/*      */     
/* 1496 */     reclaimLargeReusablePacket();
/*      */     
/* 1498 */     return topLevelResultSet;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   void resetMaxBuf()
/*      */   {
/* 1505 */     this.maxAllowedPacket = this.connection.getMaxAllowedPacket();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   final Buffer sendCommand(int command, String extraData, Buffer queryPacket, boolean skipCheck, String extraDataCharEncoding)
/*      */     throws SQLException
/*      */   {
/* 1536 */     this.enablePacketDebug = this.connection.getEnablePacketDebug();
/* 1537 */     this.traceProtocol = this.connection.getTraceProtocol();
/* 1538 */     this.readPacketSequence = 0;
/*      */     
/*      */     try
/*      */     {
/* 1542 */       checkForOutstandingStreamingData();
/*      */       
/*      */ 
/*      */ 
/*      */ 
/* 1547 */       this.serverStatus = 0;
/* 1548 */       this.hadWarnings = false;
/* 1549 */       this.warningCount = 0;
/*      */       
/* 1551 */       this.queryNoIndexUsed = false;
/* 1552 */       this.queryBadIndexUsed = false;
/*      */       
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 1558 */       if (this.useCompression) {
/* 1559 */         int bytesLeft = this.mysqlInput.available();
/*      */         
/* 1561 */         if (bytesLeft > 0) {
/* 1562 */           this.mysqlInput.skip(bytesLeft);
/*      */         }
/*      */       }
/*      */       try
/*      */       {
/* 1567 */         clearInputStream();
/*      */         
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 1576 */         if (queryPacket == null) {
/* 1577 */           int packLength = 8 + (extraData != null ? extraData.length() : 0) + 2;
/*      */           
/*      */ 
/* 1580 */           if (this.sendPacket == null) {
/* 1581 */             this.sendPacket = new Buffer(packLength);
/*      */           }
/*      */           
/* 1584 */           this.packetSequence = -1;
/* 1585 */           this.readPacketSequence = 0;
/* 1586 */           this.checkPacketSequence = true;
/* 1587 */           this.sendPacket.clear();
/*      */           
/* 1589 */           this.sendPacket.writeByte((byte)command);
/*      */           
/* 1591 */           if ((command == 2) || (command == 5) || (command == 6) || (command == 3) || (command == 22))
/*      */           {
/*      */ 
/*      */ 
/*      */ 
/* 1596 */             if (extraDataCharEncoding == null) {
/* 1597 */               this.sendPacket.writeStringNoNull(extraData);
/*      */             } else {
/* 1599 */               this.sendPacket.writeStringNoNull(extraData, extraDataCharEncoding, this.connection.getServerCharacterEncoding(), this.connection.parserKnowsUnicode(), this.connection);
/*      */             }
/*      */             
/*      */ 
/*      */           }
/* 1604 */           else if (command == 12) {
/* 1605 */             long id = new Long(extraData).longValue();
/* 1606 */             this.sendPacket.writeLong(id);
/*      */           }
/*      */           
/* 1609 */           send(this.sendPacket, this.sendPacket.getPosition());
/*      */         } else {
/* 1611 */           this.packetSequence = -1;
/* 1612 */           send(queryPacket, queryPacket.getPosition());
/*      */         }
/*      */       }
/*      */       catch (SQLException sqlEx) {
/* 1616 */         throw sqlEx;
/*      */       } catch (Exception ex) {
/* 1618 */         throw new CommunicationsException(this.connection, this.lastPacketSentTimeMs, ex);
/*      */       }
/*      */       
/*      */ 
/* 1622 */       Buffer returnPacket = null;
/*      */       
/* 1624 */       if (!skipCheck) {
/* 1625 */         if ((command == 23) || (command == 26))
/*      */         {
/* 1627 */           this.readPacketSequence = 0;
/* 1628 */           this.packetSequenceReset = true;
/*      */         }
/*      */       }
/* 1631 */       return checkErrorPacket(command);
/*      */ 
/*      */     }
/*      */     catch (IOException ioEx)
/*      */     {
/* 1636 */       throw new CommunicationsException(this.connection, this.lastPacketSentTimeMs, ioEx);
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
/*      */   final ResultSet sqlQueryDirect(Statement callingStatement, String query, String characterEncoding, Buffer queryPacket, int maxRows, Connection conn, int resultSetType, int resultSetConcurrency, boolean streamResults, String catalog, boolean unpackFieldInfo)
/*      */     throws Exception
/*      */   {
/* 1665 */     long queryStartTime = 0L;
/* 1666 */     long queryEndTime = 0L;
/*      */     
/* 1668 */     if (query != null)
/*      */     {
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 1674 */       int packLength = 5 + query.length() * 2 + 2;
/*      */       
/* 1676 */       if (this.sendPacket == null) {
/* 1677 */         this.sendPacket = new Buffer(packLength);
/*      */       } else {
/* 1679 */         this.sendPacket.clear();
/*      */       }
/*      */       
/* 1682 */       this.sendPacket.writeByte((byte)3);
/*      */       
/* 1684 */       if (characterEncoding != null) {
/* 1685 */         if (this.platformDbCharsetMatches) {
/* 1686 */           this.sendPacket.writeStringNoNull(query, characterEncoding, this.connection.getServerCharacterEncoding(), this.connection.parserKnowsUnicode(), this.connection);
/*      */ 
/*      */ 
/*      */ 
/*      */         }
/* 1691 */         else if (StringUtils.startsWithIgnoreCaseAndWs(query, "LOAD DATA")) {
/* 1692 */           this.sendPacket.writeBytesNoNull(query.getBytes());
/*      */         } else {
/* 1694 */           this.sendPacket.writeStringNoNull(query, characterEncoding, this.connection.getServerCharacterEncoding(), this.connection.parserKnowsUnicode(), this.connection);
/*      */ 
/*      */         }
/*      */         
/*      */ 
/*      */       }
/*      */       else
/*      */       {
/* 1702 */         this.sendPacket.writeStringNoNull(query);
/*      */       }
/*      */       
/* 1705 */       queryPacket = this.sendPacket;
/*      */     }
/*      */     
/* 1708 */     byte[] queryBuf = null;
/* 1709 */     int oldPacketPosition = 0;
/*      */     
/*      */ 
/*      */ 
/* 1713 */     if (this.needToGrabQueryFromPacket) {
/* 1714 */       queryBuf = queryPacket.getByteBuffer();
/*      */       
/*      */ 
/* 1717 */       oldPacketPosition = queryPacket.getPosition();
/*      */       
/* 1719 */       queryStartTime = getCurrentTimeNanosOrMillis();
/*      */     }
/*      */     
/*      */ 
/* 1723 */     Buffer resultPacket = sendCommand(3, null, queryPacket, false, null);
/*      */     
/*      */ 
/* 1726 */     long fetchBeginTime = 0L;
/* 1727 */     long fetchEndTime = 0L;
/*      */     
/* 1729 */     String profileQueryToLog = null;
/*      */     
/* 1731 */     boolean queryWasSlow = false;
/*      */     
/* 1733 */     if ((this.profileSql) || (this.logSlowQueries)) {
/* 1734 */       queryEndTime = getCurrentTimeNanosOrMillis();
/*      */       
/* 1736 */       boolean shouldExtractQuery = false;
/*      */       
/* 1738 */       if (this.profileSql) {
/* 1739 */         shouldExtractQuery = true;
/* 1740 */       } else if (this.logSlowQueries)
/*      */       {
/* 1742 */         if (queryEndTime - queryStartTime > this.slowQueryThreshold) {
/* 1743 */           shouldExtractQuery = true;
/* 1744 */           queryWasSlow = true;
/*      */         }
/*      */       }
/*      */       
/* 1748 */       if (shouldExtractQuery)
/*      */       {
/* 1750 */         boolean truncated = false;
/*      */         
/* 1752 */         int extractPosition = oldPacketPosition;
/*      */         
/* 1754 */         if (oldPacketPosition > this.connection.getMaxQuerySizeToLog()) {
/* 1755 */           extractPosition = this.connection.getMaxQuerySizeToLog() + 5;
/* 1756 */           truncated = true;
/*      */         }
/*      */         
/* 1759 */         profileQueryToLog = new String(queryBuf, 5, extractPosition - 5);
/*      */         
/*      */ 
/* 1762 */         if (truncated) {
/* 1763 */           profileQueryToLog = profileQueryToLog + Messages.getString("MysqlIO.25");
/*      */         }
/*      */       }
/*      */       
/* 1767 */       fetchBeginTime = queryEndTime;
/*      */     }
/*      */     
/* 1770 */     if (this.autoGenerateTestcaseScript) {
/* 1771 */       String testcaseQuery = null;
/*      */       
/* 1773 */       if (query != null) {
/* 1774 */         testcaseQuery = query;
/*      */       } else {
/* 1776 */         testcaseQuery = new String(queryBuf, 5, oldPacketPosition - 5);
/*      */       }
/*      */       
/*      */ 
/* 1780 */       StringBuffer debugBuf = new StringBuffer(testcaseQuery.length() + 32);
/* 1781 */       this.connection.generateConnectionCommentBlock(debugBuf);
/* 1782 */       debugBuf.append(testcaseQuery);
/* 1783 */       debugBuf.append(';');
/* 1784 */       this.connection.dumpTestcaseQuery(debugBuf.toString());
/*      */     }
/*      */     
/* 1787 */     ResultSet rs = readAllResults(callingStatement, maxRows, resultSetType, resultSetConcurrency, streamResults, catalog, resultPacket, false, -1L, unpackFieldInfo, null);
/*      */     
/*      */ 
/*      */ 
/* 1791 */     if (queryWasSlow) {
/* 1792 */       StringBuffer mesgBuf = new StringBuffer(48 + profileQueryToLog.length());
/*      */       
/*      */ 
/* 1795 */       mesgBuf.append(Messages.getString("MysqlIO.SlowQuery", new Object[] { new Long(this.slowQueryThreshold), this.queryTimingUnits, new Long(queryEndTime - queryStartTime) }));
/*      */       
/*      */ 
/*      */ 
/* 1799 */       mesgBuf.append(profileQueryToLog);
/*      */       
/* 1801 */       ProfileEventSink eventSink = ProfileEventSink.getInstance(this.connection);
/*      */       
/* 1803 */       eventSink.consumeEvent(new ProfilerEvent((byte)6, "", catalog, this.connection.getId(), callingStatement != null ? callingStatement.getId() : 999, rs.resultId, System.currentTimeMillis(), (int)(queryEndTime - queryStartTime), this.queryTimingUnits, null, new Throwable(), mesgBuf.toString()));
/*      */       
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 1810 */       if (this.connection.getExplainSlowQueries()) {
/* 1811 */         if (oldPacketPosition < 1048576) {
/* 1812 */           explainSlowQuery(queryPacket.getBytes(5, oldPacketPosition - 5), profileQueryToLog);
/*      */         }
/*      */         else {
/* 1815 */           this.connection.getLog().logWarn(Messages.getString("MysqlIO.28") + 1048576 + Messages.getString("MysqlIO.29"));
/*      */         }
/*      */       }
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/* 1823 */     if (this.profileSql) {
/* 1824 */       fetchEndTime = getCurrentTimeNanosOrMillis();
/*      */       
/* 1826 */       ProfileEventSink eventSink = ProfileEventSink.getInstance(this.connection);
/*      */       
/* 1828 */       eventSink.consumeEvent(new ProfilerEvent((byte)3, "", catalog, this.connection.getId(), callingStatement != null ? callingStatement.getId() : 999, rs.resultId, System.currentTimeMillis(), queryEndTime - queryStartTime, this.queryTimingUnits, null, new Throwable(), profileQueryToLog));
/*      */       
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 1836 */       eventSink.consumeEvent(new ProfilerEvent((byte)5, "", catalog, this.connection.getId(), callingStatement != null ? callingStatement.getId() : 999, rs.resultId, System.currentTimeMillis(), fetchEndTime - fetchBeginTime, this.queryTimingUnits, null, new Throwable(), null));
/*      */       
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 1844 */       if (this.queryBadIndexUsed) {
/* 1845 */         eventSink.consumeEvent(new ProfilerEvent((byte)6, "", catalog, this.connection.getId(), callingStatement != null ? callingStatement.getId() : 999, rs.resultId, System.currentTimeMillis(), queryEndTime - queryStartTime, this.queryTimingUnits, null, new Throwable(), Messages.getString("MysqlIO.33") + profileQueryToLog));
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
/*      */ 
/* 1858 */       if (this.queryNoIndexUsed) {
/* 1859 */         eventSink.consumeEvent(new ProfilerEvent((byte)6, "", catalog, this.connection.getId(), callingStatement != null ? callingStatement.getId() : 999, rs.resultId, System.currentTimeMillis(), queryEndTime - queryStartTime, this.queryTimingUnits, null, new Throwable(), Messages.getString("MysqlIO.35") + profileQueryToLog));
/*      */       }
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
/*      */ 
/* 1873 */     if (this.hadWarnings) {
/* 1874 */       scanForAndThrowDataTruncation();
/*      */     }
/*      */     
/* 1877 */     return rs;
/*      */   }
/*      */   
/*      */   private void calculateSlowQueryThreshold() {
/* 1881 */     this.slowQueryThreshold = this.connection.getSlowQueryThresholdMillis();
/*      */     
/* 1883 */     if (this.connection.getUseNanosForElapsedTime()) {
/* 1884 */       long nanosThreshold = this.connection.getSlowQueryThresholdNanos();
/*      */       
/* 1886 */       if (nanosThreshold != 0L) {
/* 1887 */         this.slowQueryThreshold = nanosThreshold;
/*      */       } else {
/* 1889 */         this.slowQueryThreshold *= 1000000L;
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */   protected long getCurrentTimeNanosOrMillis() {
/* 1895 */     if (this.useNanosForElapsedTime) {
/* 1896 */       return Util.getCurrentTimeNanosOrMillis();
/*      */     }
/*      */     
/* 1899 */     return System.currentTimeMillis();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   String getHost()
/*      */   {
/* 1908 */     return this.host;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   boolean isVersion(int major, int minor, int subminor)
/*      */   {
/* 1923 */     return (major == getServerMajorVersion()) && (minor == getServerMinorVersion()) && (subminor == getServerSubMinorVersion());
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   boolean versionMeetsMinimum(int major, int minor, int subminor)
/*      */   {
/* 1939 */     if (getServerMajorVersion() >= major) {
/* 1940 */       if (getServerMajorVersion() == major) {
/* 1941 */         if (getServerMinorVersion() >= minor) {
/* 1942 */           if (getServerMinorVersion() == minor) {
/* 1943 */             return getServerSubMinorVersion() >= subminor;
/*      */           }
/*      */           
/*      */ 
/* 1947 */           return true;
/*      */         }
/*      */         
/*      */ 
/* 1951 */         return false;
/*      */       }
/*      */       
/*      */ 
/* 1955 */       return true;
/*      */     }
/*      */     
/* 1958 */     return false;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   private static final String getPacketDumpToLog(Buffer packetToDump, int packetLength)
/*      */   {
/* 1972 */     if (packetLength < 1024) {
/* 1973 */       return packetToDump.dump(packetLength);
/*      */     }
/*      */     
/* 1976 */     StringBuffer packetDumpBuf = new StringBuffer(4096);
/* 1977 */     packetDumpBuf.append(packetToDump.dump(1024));
/* 1978 */     packetDumpBuf.append(Messages.getString("MysqlIO.36"));
/* 1979 */     packetDumpBuf.append(1024);
/* 1980 */     packetDumpBuf.append(Messages.getString("MysqlIO.37"));
/*      */     
/* 1982 */     return packetDumpBuf.toString();
/*      */   }
/*      */   
/*      */   private final int readFully(InputStream in, byte[] b, int off, int len) throws IOException
/*      */   {
/* 1987 */     if (len < 0) {
/* 1988 */       throw new IndexOutOfBoundsException();
/*      */     }
/*      */     
/* 1991 */     int n = 0;
/*      */     
/* 1993 */     while (n < len) {
/* 1994 */       int count = in.read(b, off + n, len - n);
/*      */       
/* 1996 */       if (count < 0) {
/* 1997 */         throw new EOFException(Messages.getString("MysqlIO.EOF", new Object[] { new Integer(len), new Integer(n) }));
/*      */       }
/*      */       
/*      */ 
/* 2001 */       n += count;
/*      */     }
/*      */     
/* 2004 */     return n;
/*      */   }
/*      */   
/*      */   private final long skipFully(InputStream in, long len) throws IOException {
/* 2008 */     if (len < 0L) {
/* 2009 */       throw new IOException("Negative skip length not allowed");
/*      */     }
/*      */     
/* 2012 */     long n = 0L;
/*      */     
/* 2014 */     while (n < len) {
/* 2015 */       long count = in.skip(len - n);
/*      */       
/* 2017 */       if (count < 0L) {
/* 2018 */         throw new EOFException(Messages.getString("MysqlIO.EOF", new Object[] { new Long(len), new Long(n) }));
/*      */       }
/*      */       
/*      */ 
/* 2022 */       n += count;
/*      */     }
/*      */     
/* 2025 */     return n;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   private final ResultSet readResultsForQueryOrUpdate(Statement callingStatement, int maxRows, int resultSetType, int resultSetConcurrency, boolean streamResults, String catalog, Buffer resultPacket, boolean isBinaryEncoded, long preSentColumnCount, boolean unpackFieldInfo, Field[] metadataFromCache)
/*      */     throws SQLException
/*      */   {
/* 2053 */     long columnCount = resultPacket.readFieldLength();
/*      */     
/* 2055 */     if (columnCount == 0L)
/* 2056 */       return buildResultSetWithUpdates(callingStatement, resultPacket);
/* 2057 */     if (columnCount == -1L) {
/* 2058 */       String charEncoding = null;
/*      */       
/* 2060 */       if (this.connection.getUseUnicode()) {
/* 2061 */         charEncoding = this.connection.getEncoding();
/*      */       }
/*      */       
/* 2064 */       String fileName = null;
/*      */       
/* 2066 */       if (this.platformDbCharsetMatches) {
/* 2067 */         fileName = charEncoding != null ? resultPacket.readString(charEncoding) : resultPacket.readString();
/*      */       }
/*      */       else
/*      */       {
/* 2071 */         fileName = resultPacket.readString();
/*      */       }
/*      */       
/* 2074 */       return sendFileToServer(callingStatement, fileName);
/*      */     }
/* 2076 */     ResultSet results = getResultSet(callingStatement, columnCount, maxRows, resultSetType, resultSetConcurrency, streamResults, catalog, isBinaryEncoded, unpackFieldInfo, metadataFromCache);
/*      */     
/*      */ 
/*      */ 
/*      */ 
/* 2081 */     return results;
/*      */   }
/*      */   
/*      */   private int alignPacketSize(int a, int l)
/*      */   {
/* 2086 */     return a + l - 1 & (l - 1 ^ 0xFFFFFFFF);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   private ResultSet buildResultSetWithRows(Statement callingStatement, String catalog, Field[] fields, RowData rows, int resultSetType, int resultSetConcurrency, boolean isBinaryEncoded)
/*      */     throws SQLException
/*      */   {
/* 2094 */     ResultSet rs = null;
/*      */     
/* 2096 */     switch (resultSetConcurrency) {
/*      */     case 1007: 
/* 2098 */       rs = new ResultSet(catalog, fields, rows, this.connection, callingStatement);
/*      */       
/*      */ 
/* 2101 */       if (isBinaryEncoded) {
/* 2102 */         rs.setBinaryEncoded();
/*      */       }
/*      */       
/*      */ 
/*      */       break;
/*      */     case 1008: 
/* 2108 */       rs = new UpdatableResultSet(catalog, fields, rows, this.connection, callingStatement);
/*      */       
/*      */ 
/* 2111 */       break;
/*      */     
/*      */     default: 
/* 2114 */       return new ResultSet(catalog, fields, rows, this.connection, callingStatement);
/*      */     }
/*      */     
/*      */     
/* 2118 */     rs.setResultSetType(resultSetType);
/* 2119 */     rs.setResultSetConcurrency(resultSetConcurrency);
/*      */     
/* 2121 */     return rs;
/*      */   }
/*      */   
/*      */   private ResultSet buildResultSetWithUpdates(Statement callingStatement, Buffer resultPacket)
/*      */     throws SQLException
/*      */   {
/* 2127 */     long updateCount = -1L;
/* 2128 */     long updateID = -1L;
/* 2129 */     String info = null;
/*      */     try
/*      */     {
/* 2132 */       if (this.useNewUpdateCounts) {
/* 2133 */         updateCount = resultPacket.newReadLength();
/* 2134 */         updateID = resultPacket.newReadLength();
/*      */       } else {
/* 2136 */         updateCount = resultPacket.readLength();
/* 2137 */         updateID = resultPacket.readLength();
/*      */       }
/*      */       
/* 2140 */       if (this.use41Extensions) {
/* 2141 */         this.serverStatus = resultPacket.readInt();
/*      */         
/* 2143 */         this.warningCount = resultPacket.readInt();
/*      */         
/* 2145 */         if (this.warningCount > 0) {
/* 2146 */           this.hadWarnings = true;
/*      */         }
/*      */         
/* 2149 */         resultPacket.readByte();
/*      */         
/* 2151 */         if (this.profileSql) {
/* 2152 */           this.queryNoIndexUsed = ((this.serverStatus & 0x10) != 0);
/*      */           
/* 2154 */           this.queryBadIndexUsed = ((this.serverStatus & 0x20) != 0);
/*      */         }
/*      */       }
/*      */       
/*      */ 
/* 2159 */       if (this.connection.isReadInfoMsgEnabled()) {
/* 2160 */         info = resultPacket.readString();
/*      */       }
/*      */     } catch (Exception ex) {
/* 2163 */       throw SQLError.createSQLException(SQLError.get("S1000") + ": " + ex.getClass().getName(), "S1000", -1);
/*      */     }
/*      */     
/*      */ 
/*      */ 
/* 2168 */     ResultSet updateRs = new ResultSet(updateCount, updateID, this.connection, callingStatement);
/*      */     
/*      */ 
/* 2171 */     if (info != null) {
/* 2172 */       updateRs.setServerInfo(info);
/*      */     }
/*      */     
/* 2175 */     return updateRs;
/*      */   }
/*      */   
/*      */   private void checkForOutstandingStreamingData() throws SQLException {
/* 2179 */     if (this.streamingData != null) {
/* 2180 */       if (!this.connection.getClobberStreamingResults()) {
/* 2181 */         throw SQLError.createSQLException(Messages.getString("MysqlIO.39") + this.streamingData + Messages.getString("MysqlIO.40") + Messages.getString("MysqlIO.41") + Messages.getString("MysqlIO.42"));
/*      */       }
/*      */       
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 2189 */       this.streamingData.getOwner().realClose(false);
/*      */       
/*      */ 
/* 2192 */       clearInputStream();
/*      */     }
/*      */   }
/*      */   
/*      */   private Buffer compressPacket(Buffer packet, int offset, int packetLen, int headerLength) throws SQLException
/*      */   {
/* 2198 */     packet.writeLongInt(packetLen - headerLength);
/* 2199 */     packet.writeByte((byte)0);
/*      */     
/* 2201 */     int lengthToWrite = 0;
/* 2202 */     int compressedLength = 0;
/* 2203 */     byte[] bytesToCompress = packet.getByteBuffer();
/* 2204 */     byte[] compressedBytes = null;
/* 2205 */     int offsetWrite = 0;
/*      */     
/* 2207 */     if (packetLen < 50) {
/* 2208 */       lengthToWrite = packetLen;
/* 2209 */       compressedBytes = packet.getByteBuffer();
/* 2210 */       compressedLength = 0;
/* 2211 */       offsetWrite = offset;
/*      */     } else {
/* 2213 */       compressedBytes = new byte[bytesToCompress.length * 2];
/*      */       
/* 2215 */       this.deflater.reset();
/* 2216 */       this.deflater.setInput(bytesToCompress, offset, packetLen);
/* 2217 */       this.deflater.finish();
/*      */       
/* 2219 */       int compLen = this.deflater.deflate(compressedBytes);
/*      */       
/* 2221 */       if (compLen > packetLen) {
/* 2222 */         lengthToWrite = packetLen;
/* 2223 */         compressedBytes = packet.getByteBuffer();
/* 2224 */         compressedLength = 0;
/* 2225 */         offsetWrite = offset;
/*      */       } else {
/* 2227 */         lengthToWrite = compLen;
/* 2228 */         headerLength += 3;
/* 2229 */         compressedLength = packetLen;
/*      */       }
/*      */     }
/*      */     
/* 2233 */     Buffer compressedPacket = new Buffer(packetLen + headerLength);
/*      */     
/* 2235 */     compressedPacket.setPosition(0);
/* 2236 */     compressedPacket.writeLongInt(lengthToWrite);
/* 2237 */     compressedPacket.writeByte(this.packetSequence);
/* 2238 */     compressedPacket.writeLongInt(compressedLength);
/* 2239 */     compressedPacket.writeBytesNoNull(compressedBytes, offsetWrite, lengthToWrite);
/*      */     
/*      */ 
/* 2242 */     return compressedPacket;
/*      */   }
/*      */   
/*      */   private final void readServerStatusForResultSets(Buffer rowPacket) throws SQLException
/*      */   {
/* 2247 */     if (this.use41Extensions) {
/* 2248 */       rowPacket.readByte();
/*      */       
/* 2250 */       this.warningCount = rowPacket.readInt();
/*      */       
/* 2252 */       if (this.warningCount > 0) {
/* 2253 */         this.hadWarnings = true;
/*      */       }
/*      */       
/* 2256 */       this.serverStatus = rowPacket.readInt();
/*      */       
/* 2258 */       if (this.profileSql) {
/* 2259 */         this.queryNoIndexUsed = ((this.serverStatus & 0x10) != 0);
/*      */         
/* 2261 */         this.queryBadIndexUsed = ((this.serverStatus & 0x20) != 0);
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */   private SocketFactory createSocketFactory() throws SQLException
/*      */   {
/*      */     try {
/* 2269 */       if (this.socketFactoryClassName == null) {
/* 2270 */         throw SQLError.createSQLException(Messages.getString("MysqlIO.75"), "08001");
/*      */       }
/*      */       
/*      */ 
/* 2274 */       return (SocketFactory)Class.forName(this.socketFactoryClassName).newInstance();
/*      */     }
/*      */     catch (Exception ex) {
/* 2277 */       throw SQLError.createSQLException(Messages.getString("MysqlIO.76") + this.socketFactoryClassName + Messages.getString("MysqlIO.77") + ex.toString() + (this.connection.getParanoid() ? "" : Util.stackTraceToString(ex)), "08001");
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   private void enqueuePacketForDebugging(boolean isPacketBeingSent, boolean isPacketReused, int sendLength, byte[] header, Buffer packet)
/*      */     throws SQLException
/*      */   {
/* 2289 */     if (this.packetDebugRingBuffer.size() + 1 > this.connection.getPacketDebugBufferSize()) {
/* 2290 */       this.packetDebugRingBuffer.removeFirst();
/*      */     }
/*      */     
/* 2293 */     StringBuffer packetDump = null;
/*      */     
/* 2295 */     if (!isPacketBeingSent) {
/* 2296 */       int bytesToDump = Math.min(1024, packet.getBufLength());
/*      */       
/*      */ 
/* 2299 */       Buffer packetToDump = new Buffer(4 + bytesToDump);
/*      */       
/* 2301 */       packetToDump.setPosition(0);
/* 2302 */       packetToDump.writeBytesNoNull(header);
/* 2303 */       packetToDump.writeBytesNoNull(packet.getBytes(0, bytesToDump));
/*      */       
/* 2305 */       String packetPayload = packetToDump.dump(bytesToDump);
/*      */       
/* 2307 */       packetDump = new StringBuffer(96 + packetPayload.length());
/*      */       
/* 2309 */       packetDump.append("Server ");
/*      */       
/* 2311 */       if (isPacketReused) {
/* 2312 */         packetDump.append("(re-used)");
/*      */       } else {
/* 2314 */         packetDump.append("(new)");
/*      */       }
/*      */       
/* 2317 */       packetDump.append(" ");
/* 2318 */       packetDump.append(packet.toSuperString());
/* 2319 */       packetDump.append(" --------------------> Client\n");
/* 2320 */       packetDump.append("\nPacket payload:\n\n");
/* 2321 */       packetDump.append(packetPayload);
/*      */       
/* 2323 */       if (bytesToDump == 1024) {
/* 2324 */         packetDump.append("\nNote: Packet of " + packet.getBufLength() + " bytes truncated to " + 1024 + " bytes.\n");
/*      */       }
/*      */     }
/*      */     else
/*      */     {
/* 2329 */       int bytesToDump = Math.min(1024, sendLength);
/*      */       
/* 2331 */       String packetPayload = packet.dump(bytesToDump);
/*      */       
/* 2333 */       packetDump = new StringBuffer(68 + packetPayload.length());
/*      */       
/* 2335 */       packetDump.append("Client ");
/* 2336 */       packetDump.append(packet.toSuperString());
/* 2337 */       packetDump.append("--------------------> Server\n");
/* 2338 */       packetDump.append("\nPacket payload:\n\n");
/* 2339 */       packetDump.append(packetPayload);
/*      */       
/* 2341 */       if (bytesToDump == 1024) {
/* 2342 */         packetDump.append("\nNote: Packet of " + sendLength + " bytes truncated to " + 1024 + " bytes.\n");
/*      */       }
/*      */     }
/*      */     
/*      */ 
/*      */ 
/* 2348 */     this.packetDebugRingBuffer.addLast(packetDump);
/*      */   }
/*      */   
/*      */ 
/*      */   private RowData readSingleRowSet(long columnCount, int maxRows, int resultSetConcurrency, boolean isBinaryEncoded, Field[] fields)
/*      */     throws SQLException
/*      */   {
/* 2355 */     ArrayList rows = new ArrayList();
/*      */     
/*      */ 
/* 2358 */     Object rowBytes = nextRow(fields, (int)columnCount, isBinaryEncoded, resultSetConcurrency);
/*      */     
/*      */ 
/* 2361 */     int rowCount = 0;
/*      */     
/* 2363 */     if (rowBytes != null) {
/* 2364 */       rows.add(rowBytes);
/* 2365 */       rowCount = 1;
/*      */     }
/*      */     
/* 2368 */     while (rowBytes != null) {
/* 2369 */       rowBytes = nextRow(fields, (int)columnCount, isBinaryEncoded, resultSetConcurrency);
/*      */       
/*      */ 
/* 2372 */       if ((rowBytes != null) && (
/* 2373 */         (maxRows == -1) || (rowCount < maxRows))) {
/* 2374 */         rows.add(rowBytes);
/* 2375 */         rowCount++;
/*      */       }
/*      */     }
/*      */     
/*      */ 
/* 2380 */     RowData rowData = new RowDataStatic(rows);
/*      */     
/* 2382 */     return rowData;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   private void reclaimLargeReusablePacket()
/*      */   {
/* 2389 */     if ((this.reusablePacket != null) && (this.reusablePacket.getCapacity() > 1048576))
/*      */     {
/* 2391 */       this.reusablePacket = new Buffer(1024);
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
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   private void checkPacketSequencing(byte multiPacketSeq)
/*      */     throws CommunicationsException
/*      */   {
/* 2645 */     if ((multiPacketSeq == Byte.MIN_VALUE) && (this.readPacketSequence != Byte.MAX_VALUE)) {
/* 2646 */       throw new CommunicationsException(this.connection, this.lastPacketSentTimeMs, new IOException("Packets out of order, expected packet # -128, but received packet # " + multiPacketSeq));
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/* 2652 */     if ((this.readPacketSequence == -1) && (multiPacketSeq != 0)) {
/* 2653 */       throw new CommunicationsException(this.connection, this.lastPacketSentTimeMs, new IOException("Packets out of order, expected packet # -1, but received packet # " + multiPacketSeq));
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/* 2659 */     if ((multiPacketSeq != Byte.MIN_VALUE) && (this.readPacketSequence != -1) && (multiPacketSeq != this.readPacketSequence + 1))
/*      */     {
/* 2661 */       throw new CommunicationsException(this.connection, this.lastPacketSentTimeMs, new IOException("Packets out of order, expected packet # " + (this.readPacketSequence + 1) + ", but received packet # " + multiPacketSeq));
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   void enableMultiQueries()
/*      */     throws SQLException
/*      */   {
/* 2670 */     Buffer buf = getSharedSendPacket();
/*      */     
/* 2672 */     buf.clear();
/* 2673 */     buf.writeByte((byte)27);
/* 2674 */     buf.writeInt(0);
/* 2675 */     sendCommand(27, null, buf, false, null);
/*      */   }
/*      */   
/*      */   void disableMultiQueries() throws SQLException {
/* 2679 */     Buffer buf = getSharedSendPacket();
/*      */     
/* 2681 */     buf.clear();
/* 2682 */     buf.writeByte((byte)27);
/* 2683 */     buf.writeInt(1);
/* 2684 */     sendCommand(27, null, buf, false, null);
/*      */   }
/*      */   
/*      */   private final void send(Buffer packet, int packetLen) throws SQLException
/*      */   {
/*      */     try {
/* 2690 */       if (packetLen > this.maxAllowedPacket) {
/* 2691 */         throw new PacketTooBigException(packetLen, this.maxAllowedPacket);
/*      */       }
/*      */       
/* 2694 */       if (this.connection.getMaintainTimeStats()) {
/* 2695 */         this.lastPacketSentTimeMs = System.currentTimeMillis();
/*      */       }
/*      */       
/* 2698 */       if ((this.serverMajorVersion >= 4) && (packetLen >= this.maxThreeBytes))
/*      */       {
/* 2700 */         sendSplitPackets(packet);
/*      */       } else {
/* 2702 */         this.packetSequence = ((byte)(this.packetSequence + 1));
/*      */         
/* 2704 */         Buffer packetToSend = packet;
/*      */         
/* 2706 */         packetToSend.setPosition(0);
/*      */         
/* 2708 */         if (this.useCompression) {
/* 2709 */           int originalPacketLen = packetLen;
/*      */           
/* 2711 */           packetToSend = compressPacket(packet, 0, packetLen, 4);
/*      */           
/* 2713 */           packetLen = packetToSend.getPosition();
/*      */           
/* 2715 */           if (this.traceProtocol) {
/* 2716 */             StringBuffer traceMessageBuf = new StringBuffer();
/*      */             
/* 2718 */             traceMessageBuf.append(Messages.getString("MysqlIO.57"));
/* 2719 */             traceMessageBuf.append(getPacketDumpToLog(packetToSend, packetLen));
/*      */             
/* 2721 */             traceMessageBuf.append(Messages.getString("MysqlIO.58"));
/* 2722 */             traceMessageBuf.append(getPacketDumpToLog(packet, originalPacketLen));
/*      */             
/*      */ 
/* 2725 */             this.connection.getLog().logTrace(traceMessageBuf.toString());
/*      */           }
/*      */         } else {
/* 2728 */           packetToSend.writeLongInt(packetLen - 4);
/* 2729 */           packetToSend.writeByte(this.packetSequence);
/*      */           
/* 2731 */           if (this.traceProtocol) {
/* 2732 */             StringBuffer traceMessageBuf = new StringBuffer();
/*      */             
/* 2734 */             traceMessageBuf.append(Messages.getString("MysqlIO.59"));
/* 2735 */             traceMessageBuf.append(packetToSend.dump(packetLen));
/*      */             
/* 2737 */             this.connection.getLog().logTrace(traceMessageBuf.toString());
/*      */           }
/*      */         }
/*      */         
/*      */ 
/* 2742 */         this.mysqlOutput.write(packetToSend.getByteBuffer(), 0, packetLen);
/*      */         
/* 2744 */         this.mysqlOutput.flush();
/*      */       }
/*      */       
/* 2747 */       if (this.enablePacketDebug) {
/* 2748 */         enqueuePacketForDebugging(true, false, packetLen + 5, this.packetHeaderBuf, packet);
/*      */       }
/*      */       
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 2755 */       if (packet == this.sharedSendPacket) {
/* 2756 */         reclaimLargeSharedSendPacket();
/*      */       }
/*      */     } catch (IOException ioEx) {
/* 2759 */       throw new CommunicationsException(this.connection, this.lastPacketSentTimeMs, ioEx);
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
/*      */   private final ResultSet sendFileToServer(Statement callingStatement, String fileName)
/*      */     throws SQLException
/*      */   {
/* 2777 */     Buffer filePacket = this.loadFileBufRef == null ? null : (Buffer)this.loadFileBufRef.get();
/*      */     
/*      */ 
/* 2780 */     int bigPacketLength = Math.min(this.connection.getMaxAllowedPacket() - 12, alignPacketSize(this.connection.getMaxAllowedPacket() - 16, 4096) - 12);
/*      */     
/*      */ 
/*      */ 
/*      */ 
/* 2785 */     int oneMeg = 1048576;
/*      */     
/* 2787 */     int smallerPacketSizeAligned = Math.min(oneMeg - 12, alignPacketSize(oneMeg - 16, 4096) - 12);
/*      */     
/*      */ 
/* 2790 */     int packetLength = Math.min(smallerPacketSizeAligned, bigPacketLength);
/*      */     
/* 2792 */     if (filePacket == null) {
/*      */       try {
/* 2794 */         filePacket = new Buffer(packetLength + 4);
/* 2795 */         this.loadFileBufRef = new SoftReference(filePacket);
/*      */       } catch (OutOfMemoryError oom) {
/* 2797 */         throw SQLError.createSQLException("Could not allocate packet of " + packetLength + " bytes required for LOAD DATA LOCAL INFILE operation." + " Try increasing max heap allocation for JVM or decreasing server variable " + "'max_allowed_packet'", "S1001");
/*      */       }
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 2805 */     filePacket.clear();
/* 2806 */     send(filePacket, 0);
/*      */     
/* 2808 */     byte[] fileBuf = new byte[packetLength];
/*      */     
/* 2810 */     BufferedInputStream fileIn = null;
/*      */     try
/*      */     {
/* 2813 */       if (!this.connection.getAllowLoadLocalInfile()) {
/* 2814 */         throw SQLError.createSQLException(Messages.getString("MysqlIO.LoadDataLocalNotAllowed"), "S1000");
/*      */       }
/*      */       
/*      */ 
/*      */ 
/* 2819 */       if (!this.connection.getAllowUrlInLocalInfile()) {
/* 2820 */         fileIn = new BufferedInputStream(new FileInputStream(fileName));
/*      */ 
/*      */       }
/* 2823 */       else if (fileName.indexOf(":") != -1) {
/*      */         try {
/* 2825 */           URL urlFromFileName = new URL(fileName);
/* 2826 */           fileIn = new BufferedInputStream(urlFromFileName.openStream());
/*      */         }
/*      */         catch (MalformedURLException badUrlEx) {
/* 2829 */           fileIn = new BufferedInputStream(new FileInputStream(fileName));
/*      */         }
/*      */         
/*      */       } else {
/* 2833 */         fileIn = new BufferedInputStream(new FileInputStream(fileName));
/*      */       }
/*      */       
/*      */ 
/*      */ 
/* 2838 */       int bytesRead = 0;
/*      */       
/* 2840 */       while ((bytesRead = fileIn.read(fileBuf)) != -1) {
/* 2841 */         filePacket.clear();
/* 2842 */         filePacket.writeBytesNoNull(fileBuf, 0, bytesRead);
/* 2843 */         send(filePacket, filePacket.getPosition());
/*      */       }
/*      */     } catch (IOException ioEx) {
/* 2846 */       StringBuffer messageBuf = new StringBuffer(Messages.getString("MysqlIO.60"));
/*      */       
/*      */ 
/* 2849 */       if (!this.connection.getParanoid()) {
/* 2850 */         messageBuf.append("'");
/*      */         
/* 2852 */         if (fileName != null) {
/* 2853 */           messageBuf.append(fileName);
/*      */         }
/*      */         
/* 2856 */         messageBuf.append("'");
/*      */       }
/*      */       
/* 2859 */       messageBuf.append(Messages.getString("MysqlIO.63"));
/*      */       
/* 2861 */       if (!this.connection.getParanoid()) {
/* 2862 */         messageBuf.append(Messages.getString("MysqlIO.64"));
/* 2863 */         messageBuf.append(Util.stackTraceToString(ioEx));
/*      */       }
/*      */       
/* 2866 */       throw SQLError.createSQLException(messageBuf.toString(), "S1009");
/*      */     }
/*      */     finally {
/* 2869 */       if (fileIn != null) {
/*      */         try {
/* 2871 */           fileIn.close();
/*      */         } catch (Exception ex) {
/* 2873 */           throw SQLError.createSQLException(Messages.getString("MysqlIO.65"), "S1000");
/*      */         }
/*      */         
/*      */ 
/* 2877 */         fileIn = null;
/*      */       }
/*      */       else {
/* 2880 */         filePacket.clear();
/* 2881 */         send(filePacket, filePacket.getPosition());
/* 2882 */         checkErrorPacket();
/*      */       }
/*      */     }
/*      */     
/*      */ 
/* 2887 */     filePacket.clear();
/* 2888 */     send(filePacket, filePacket.getPosition());
/*      */     
/* 2890 */     Buffer resultPacket = checkErrorPacket();
/*      */     
/* 2892 */     return buildResultSetWithUpdates(callingStatement, resultPacket);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   private Buffer checkErrorPacket(int command)
/*      */     throws SQLException
/*      */   {
/* 2907 */     int statusCode = 0;
/* 2908 */     Buffer resultPacket = null;
/* 2909 */     this.serverStatus = 0;
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */     try
/*      */     {
/* 2916 */       resultPacket = reuseAndReadPacket(this.reusablePacket);
/* 2917 */       statusCode = resultPacket.readByte();
/*      */     }
/*      */     catch (SQLException sqlEx) {
/* 2920 */       throw sqlEx;
/*      */     } catch (Exception fallThru) {
/* 2922 */       throw new CommunicationsException(this.connection, this.lastPacketSentTimeMs, fallThru);
/*      */     }
/*      */     
/*      */ 
/*      */ 
/* 2927 */     if (statusCode == -1)
/*      */     {
/* 2929 */       int errno = 2000;
/*      */       
/* 2931 */       if (this.protocolVersion > 9) {
/* 2932 */         errno = resultPacket.readInt();
/*      */         
/* 2934 */         String xOpen = null;
/*      */         
/* 2936 */         String serverErrorMessage = resultPacket.readString(this.connection.getErrorMessageEncoding());
/*      */         
/*      */ 
/* 2939 */         if (serverErrorMessage.startsWith("#"))
/*      */         {
/*      */ 
/* 2942 */           if (serverErrorMessage.length() > 6) {
/* 2943 */             xOpen = serverErrorMessage.substring(1, 6);
/* 2944 */             serverErrorMessage = serverErrorMessage.substring(6);
/*      */             
/* 2946 */             if (xOpen.equals("HY000")) {
/* 2947 */               xOpen = SQLError.mysqlToSqlState(errno, this.connection.getUseSqlStateCodes());
/*      */             }
/*      */           }
/*      */           else {
/* 2951 */             xOpen = SQLError.mysqlToSqlState(errno, this.connection.getUseSqlStateCodes());
/*      */           }
/*      */         }
/*      */         else {
/* 2955 */           xOpen = SQLError.mysqlToSqlState(errno, this.connection.getUseSqlStateCodes());
/*      */         }
/*      */         
/*      */ 
/* 2959 */         clearInputStream();
/*      */         
/* 2961 */         StringBuffer errorBuf = new StringBuffer();
/*      */         
/* 2963 */         String xOpenErrorMessage = SQLError.get(xOpen);
/*      */         
/* 2965 */         if ((!this.connection.getUseOnlyServerErrorMessages()) && 
/* 2966 */           (xOpenErrorMessage != null)) {
/* 2967 */           errorBuf.append(xOpenErrorMessage);
/* 2968 */           errorBuf.append(Messages.getString("MysqlIO.68"));
/*      */         }
/*      */         
/*      */ 
/* 2972 */         errorBuf.append(serverErrorMessage);
/*      */         
/* 2974 */         if ((!this.connection.getUseOnlyServerErrorMessages()) && 
/* 2975 */           (xOpenErrorMessage != null)) {
/* 2976 */           errorBuf.append("\"");
/*      */         }
/*      */         
/*      */ 
/* 2980 */         appendInnodbStatusInformation(xOpen, errorBuf);
/*      */         
/* 2982 */         if ((xOpen != null) && (xOpen.startsWith("22"))) {
/* 2983 */           throw new MysqlDataTruncation(errorBuf.toString(), 0, true, false, 0, 0);
/*      */         }
/* 2985 */         throw SQLError.createSQLException(errorBuf.toString(), xOpen, errno);
/*      */       }
/*      */       
/*      */ 
/* 2989 */       String serverErrorMessage = resultPacket.readString(this.connection.getErrorMessageEncoding());
/*      */       
/* 2991 */       clearInputStream();
/*      */       
/* 2993 */       if (serverErrorMessage.indexOf(Messages.getString("MysqlIO.70")) != -1) {
/* 2994 */         throw SQLError.createSQLException(SQLError.get("S0022") + ", " + serverErrorMessage, "S0022", -1);
/*      */       }
/*      */       
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 3001 */       StringBuffer errorBuf = new StringBuffer(Messages.getString("MysqlIO.72"));
/*      */       
/* 3003 */       errorBuf.append(serverErrorMessage);
/* 3004 */       errorBuf.append("\"");
/*      */       
/* 3006 */       throw SQLError.createSQLException(SQLError.get("S1000") + ", " + errorBuf.toString(), "S1000", -1);
/*      */     }
/*      */     
/*      */ 
/*      */ 
/* 3011 */     return resultPacket;
/*      */   }
/*      */   
/*      */   private void appendInnodbStatusInformation(String xOpen, StringBuffer errorBuf) throws SQLException
/*      */   {
/* 3016 */     if ((this.connection.getIncludeInnodbStatusInDeadlockExceptions()) && (xOpen != null) && ((xOpen.startsWith("40")) || (xOpen.startsWith("41"))) && (this.streamingData == null))
/*      */     {
/*      */ 
/*      */ 
/* 3020 */       ResultSet rs = null;
/*      */       try
/*      */       {
/* 3023 */         rs = sqlQueryDirect(null, "SHOW ENGINE INNODB STATUS", this.connection.getEncoding(), null, -1, this.connection, 1003, 1007, false, this.connection.getCatalog(), true);
/*      */         
/*      */ 
/*      */ 
/*      */ 
/* 3028 */         if (rs.next()) {
/* 3029 */           errorBuf.append("\n\n");
/* 3030 */           errorBuf.append(rs.getString(1));
/*      */         } else {
/* 3032 */           errorBuf.append(Messages.getString("MysqlIO.NoInnoDBStatusFound"));
/*      */         }
/*      */       }
/*      */       catch (Exception ex) {
/* 3036 */         errorBuf.append(Messages.getString("MysqlIO.InnoDBStatusFailed"));
/*      */         
/* 3038 */         errorBuf.append("\n\n");
/* 3039 */         errorBuf.append(Util.stackTraceToString(ex));
/*      */       } finally {
/* 3041 */         if (rs != null) {
/* 3042 */           rs.close();
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
/*      */   private final void sendSplitPackets(Buffer packet)
/*      */     throws SQLException
/*      */   {
/*      */     try
/*      */     {
/* 3071 */       Buffer headerPacket = this.splitBufRef == null ? null : (Buffer)this.splitBufRef.get();
/*      */       
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 3079 */       if (headerPacket == null) {
/* 3080 */         headerPacket = new Buffer(this.maxThreeBytes + 4);
/*      */         
/* 3082 */         this.splitBufRef = new SoftReference(headerPacket);
/*      */       }
/*      */       
/* 3085 */       int len = packet.getPosition();
/* 3086 */       int splitSize = this.maxThreeBytes;
/* 3087 */       int originalPacketPos = 4;
/* 3088 */       byte[] origPacketBytes = packet.getByteBuffer();
/* 3089 */       byte[] headerPacketBytes = headerPacket.getByteBuffer();
/*      */       
/* 3091 */       while (len >= this.maxThreeBytes) {
/* 3092 */         this.packetSequence = ((byte)(this.packetSequence + 1));
/*      */         
/* 3094 */         headerPacket.setPosition(0);
/* 3095 */         headerPacket.writeLongInt(splitSize);
/*      */         
/* 3097 */         headerPacket.writeByte(this.packetSequence);
/* 3098 */         System.arraycopy(origPacketBytes, originalPacketPos, headerPacketBytes, 4, splitSize);
/*      */         
/*      */ 
/* 3101 */         int packetLen = splitSize + 4;
/*      */         
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 3107 */         if (!this.useCompression) {
/* 3108 */           this.mysqlOutput.write(headerPacketBytes, 0, splitSize + 4);
/*      */           
/* 3110 */           this.mysqlOutput.flush();
/*      */         }
/*      */         else
/*      */         {
/* 3114 */           headerPacket.setPosition(0);
/* 3115 */           Buffer packetToSend = compressPacket(headerPacket, 4, splitSize, 4);
/*      */           
/* 3117 */           packetLen = packetToSend.getPosition();
/*      */           
/* 3119 */           this.mysqlOutput.write(packetToSend.getByteBuffer(), 0, packetLen);
/*      */           
/* 3121 */           this.mysqlOutput.flush();
/*      */         }
/*      */         
/* 3124 */         originalPacketPos += splitSize;
/* 3125 */         len -= splitSize;
/*      */       }
/*      */       
/*      */ 
/*      */ 
/*      */ 
/* 3131 */       headerPacket.clear();
/* 3132 */       headerPacket.setPosition(0);
/* 3133 */       headerPacket.writeLongInt(len - 4);
/* 3134 */       this.packetSequence = ((byte)(this.packetSequence + 1));
/* 3135 */       headerPacket.writeByte(this.packetSequence);
/*      */       
/* 3137 */       if (len != 0) {
/* 3138 */         System.arraycopy(origPacketBytes, originalPacketPos, headerPacketBytes, 4, len - 4);
/*      */       }
/*      */       
/*      */ 
/* 3142 */       int packetLen = len - 4;
/*      */       
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 3148 */       if (!this.useCompression) {
/* 3149 */         this.mysqlOutput.write(headerPacket.getByteBuffer(), 0, len);
/* 3150 */         this.mysqlOutput.flush();
/*      */       }
/*      */       else
/*      */       {
/* 3154 */         headerPacket.setPosition(0);
/* 3155 */         Buffer packetToSend = compressPacket(headerPacket, 4, packetLen, 4);
/*      */         
/* 3157 */         packetLen = packetToSend.getPosition();
/*      */         
/* 3159 */         this.mysqlOutput.write(packetToSend.getByteBuffer(), 0, packetLen);
/*      */         
/* 3161 */         this.mysqlOutput.flush();
/*      */       }
/*      */     } catch (IOException ioEx) {
/* 3164 */       throw new CommunicationsException(this.connection, this.lastPacketSentTimeMs, ioEx);
/*      */     }
/*      */   }
/*      */   
/*      */   private void reclaimLargeSharedSendPacket()
/*      */   {
/* 3170 */     if ((this.sharedSendPacket != null) && (this.sharedSendPacket.getCapacity() > 1048576))
/*      */     {
/* 3172 */       this.sharedSendPacket = new Buffer(1024);
/*      */     }
/*      */   }
/*      */   
/*      */   boolean hadWarnings() {
/* 3177 */     return this.hadWarnings;
/*      */   }
/*      */   
/*      */   void scanForAndThrowDataTruncation() throws SQLException {
/* 3181 */     if ((this.streamingData == null) && (versionMeetsMinimum(4, 1, 0)) && (this.connection.getJdbcCompliantTruncation()) && (this.warningCount > 0))
/*      */     {
/* 3183 */       SQLError.convertShowWarningsToSQLWarnings(this.connection, this.warningCount, true);
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
/*      */   private void secureAuth(Buffer packet, int packLength, String user, String password, String database, boolean writeClientParams)
/*      */     throws SQLException
/*      */   {
/* 3204 */     if (packet == null) {
/* 3205 */       packet = new Buffer(packLength);
/*      */     }
/*      */     
/* 3208 */     if (writeClientParams) {
/* 3209 */       if (this.use41Extensions) {
/* 3210 */         if (versionMeetsMinimum(4, 1, 1)) {
/* 3211 */           packet.writeLong(this.clientParam);
/* 3212 */           packet.writeLong(this.maxThreeBytes);
/*      */           
/*      */ 
/*      */ 
/*      */ 
/* 3217 */           packet.writeByte((byte)8);
/*      */           
/*      */ 
/* 3220 */           packet.writeBytesNoNull(new byte[23]);
/*      */         } else {
/* 3222 */           packet.writeLong(this.clientParam);
/* 3223 */           packet.writeLong(this.maxThreeBytes);
/*      */         }
/*      */       } else {
/* 3226 */         packet.writeInt((int)this.clientParam);
/* 3227 */         packet.writeLongInt(this.maxThreeBytes);
/*      */       }
/*      */     }
/*      */     
/*      */ 
/* 3232 */     packet.writeString(user, "Cp1252", this.connection);
/*      */     
/* 3234 */     if (password.length() != 0)
/*      */     {
/* 3236 */       packet.writeString("xxxxxxxx", "Cp1252", this.connection);
/*      */     }
/*      */     else {
/* 3239 */       packet.writeString("", "Cp1252", this.connection);
/*      */     }
/*      */     
/* 3242 */     if (this.useConnectWithDb) {
/* 3243 */       packet.writeString(database, "Cp1252", this.connection);
/*      */     }
/*      */     
/* 3246 */     send(packet, packet.getPosition());
/*      */     
/*      */ 
/*      */ 
/*      */ 
/* 3251 */     if (password.length() > 0) {
/* 3252 */       Buffer b = readPacket();
/*      */       
/* 3254 */       b.setPosition(0);
/*      */       
/* 3256 */       byte[] replyAsBytes = b.getByteBuffer();
/*      */       
/* 3258 */       if ((replyAsBytes.length == 25) && (replyAsBytes[0] != 0))
/*      */       {
/* 3260 */         if (replyAsBytes[0] != 42) {
/*      */           try
/*      */           {
/* 3263 */             byte[] buff = Security.passwordHashStage1(password);
/*      */             
/*      */ 
/* 3266 */             byte[] passwordHash = new byte[buff.length];
/* 3267 */             System.arraycopy(buff, 0, passwordHash, 0, buff.length);
/*      */             
/*      */ 
/* 3270 */             passwordHash = Security.passwordHashStage2(passwordHash, replyAsBytes);
/*      */             
/*      */ 
/* 3273 */             byte[] packetDataAfterSalt = new byte[replyAsBytes.length - 5];
/*      */             
/*      */ 
/* 3276 */             System.arraycopy(replyAsBytes, 4, packetDataAfterSalt, 0, replyAsBytes.length - 5);
/*      */             
/*      */ 
/* 3279 */             byte[] mysqlScrambleBuff = new byte[20];
/*      */             
/*      */ 
/* 3282 */             Security.passwordCrypt(packetDataAfterSalt, mysqlScrambleBuff, passwordHash, 20);
/*      */             
/*      */ 
/*      */ 
/* 3286 */             Security.passwordCrypt(mysqlScrambleBuff, buff, buff, 20);
/*      */             
/* 3288 */             Buffer packet2 = new Buffer(25);
/* 3289 */             packet2.writeBytesNoNull(buff);
/*      */             
/* 3291 */             this.packetSequence = ((byte)(this.packetSequence + 1));
/*      */             
/* 3293 */             send(packet2, 24);
/*      */           } catch (NoSuchAlgorithmException nse) {
/* 3295 */             throw SQLError.createSQLException(Messages.getString("MysqlIO.91") + Messages.getString("MysqlIO.92"), "S1000");
/*      */           }
/*      */           
/*      */         }
/*      */         else {
/*      */           try
/*      */           {
/* 3302 */             byte[] passwordHash = Security.createKeyFromOldPassword(password);
/*      */             
/*      */ 
/* 3305 */             byte[] netReadPos4 = new byte[replyAsBytes.length - 5];
/*      */             
/* 3307 */             System.arraycopy(replyAsBytes, 4, netReadPos4, 0, replyAsBytes.length - 5);
/*      */             
/*      */ 
/* 3310 */             byte[] mysqlScrambleBuff = new byte[20];
/*      */             
/*      */ 
/* 3313 */             Security.passwordCrypt(netReadPos4, mysqlScrambleBuff, passwordHash, 20);
/*      */             
/*      */ 
/*      */ 
/* 3317 */             String scrambledPassword = Util.scramble(new String(mysqlScrambleBuff), password);
/*      */             
/*      */ 
/* 3320 */             Buffer packet2 = new Buffer(packLength);
/* 3321 */             packet2.writeString(scrambledPassword, "Cp1252", this.connection);
/* 3322 */             this.packetSequence = ((byte)(this.packetSequence + 1));
/*      */             
/* 3324 */             send(packet2, 24);
/*      */           } catch (NoSuchAlgorithmException nse) {
/* 3326 */             throw SQLError.createSQLException(Messages.getString("MysqlIO.93") + Messages.getString("MysqlIO.94"), "S1000");
/*      */           }
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
/*      */   void secureAuth411(Buffer packet, int packLength, String user, String password, String database, boolean writeClientParams)
/*      */     throws SQLException
/*      */   {
/* 3368 */     if (packet == null) {
/* 3369 */       packet = new Buffer(packLength);
/*      */     }
/*      */     
/* 3372 */     if (writeClientParams) {
/* 3373 */       if (this.use41Extensions) {
/* 3374 */         if (versionMeetsMinimum(4, 1, 1)) {
/* 3375 */           packet.writeLong(this.clientParam);
/* 3376 */           packet.writeLong(this.maxThreeBytes);
/*      */           
/*      */ 
/*      */ 
/*      */ 
/* 3381 */           packet.writeByte((byte)8);
/*      */           
/*      */ 
/* 3384 */           packet.writeBytesNoNull(new byte[23]);
/*      */         } else {
/* 3386 */           packet.writeLong(this.clientParam);
/* 3387 */           packet.writeLong(this.maxThreeBytes);
/*      */         }
/*      */       } else {
/* 3390 */         packet.writeInt((int)this.clientParam);
/* 3391 */         packet.writeLongInt(this.maxThreeBytes);
/*      */       }
/*      */     }
/*      */     
/*      */ 
/* 3396 */     packet.writeString(user);
/*      */     
/* 3398 */     if (password.length() != 0) {
/* 3399 */       packet.writeByte((byte)20);
/*      */       try
/*      */       {
/* 3402 */         packet.writeBytesNoNull(Security.scramble411(password, this.seed));
/*      */       } catch (NoSuchAlgorithmException nse) {
/* 3404 */         throw SQLError.createSQLException(Messages.getString("MysqlIO.95") + Messages.getString("MysqlIO.96"), "S1000");
/*      */       }
/*      */       
/*      */     }
/*      */     else
/*      */     {
/* 3410 */       packet.writeByte((byte)0);
/*      */     }
/*      */     
/* 3413 */     if (this.useConnectWithDb) {
/* 3414 */       packet.writeString(database);
/*      */     }
/*      */     
/* 3417 */     send(packet, packet.getPosition());
/*      */     
/* 3419 */     byte savePacketSequence = this.packetSequence++;
/*      */     
/* 3421 */     Buffer reply = checkErrorPacket();
/*      */     
/* 3423 */     if (reply.isLastDataPacket())
/*      */     {
/*      */ 
/*      */ 
/*      */ 
/* 3428 */       savePacketSequence = (byte)(savePacketSequence + 1);this.packetSequence = savePacketSequence;
/* 3429 */       packet.clear();
/*      */       
/* 3431 */       String seed323 = this.seed.substring(0, 8);
/* 3432 */       packet.writeString(Util.newCrypt(password, seed323));
/* 3433 */       send(packet, packet.getPosition());
/*      */       
/*      */ 
/* 3436 */       checkErrorPacket();
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
/*      */   private final Object[] unpackBinaryResultSetRow(Field[] fields, Buffer binaryData, int resultSetConcurrency)
/*      */     throws SQLException
/*      */   {
/* 3453 */     int numFields = fields.length;
/*      */     
/* 3455 */     Object[] unpackedRowData = new Object[numFields];
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 3462 */     int nullCount = (numFields + 9) / 8;
/*      */     
/* 3464 */     byte[] nullBitMask = new byte[nullCount];
/*      */     
/* 3466 */     for (int i = 0; i < nullCount; i++) {
/* 3467 */       nullBitMask[i] = binaryData.readByte();
/*      */     }
/*      */     
/* 3470 */     int nullMaskPos = 0;
/* 3471 */     int bit = 4;
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 3478 */     for (int i = 0; i < numFields; i++) {
/* 3479 */       if ((nullBitMask[nullMaskPos] & bit) != 0) {
/* 3480 */         unpackedRowData[i] = null;
/*      */       }
/* 3482 */       else if (resultSetConcurrency != 1008) {
/* 3483 */         extractNativeEncodedColumn(binaryData, fields, i, unpackedRowData);
/*      */       }
/*      */       else {
/* 3486 */         unpackNativeEncodedColumn(binaryData, fields, i, unpackedRowData);
/*      */       }
/*      */       
/*      */ 
/*      */ 
/* 3491 */       if ((bit <<= 1 & 0xFF) == 0) {
/* 3492 */         bit = 1;
/*      */         
/* 3494 */         nullMaskPos++;
/*      */       }
/*      */     }
/*      */     
/* 3498 */     return unpackedRowData;
/*      */   }
/*      */   
/*      */   private final void extractNativeEncodedColumn(Buffer binaryData, Field[] fields, int columnIndex, Object[] unpackedRowData)
/*      */     throws SQLException
/*      */   {
/* 3504 */     Field curField = fields[columnIndex];
/*      */     
/* 3506 */     switch (curField.getMysqlType())
/*      */     {
/*      */     case 6: 
/*      */       break;
/*      */     
/*      */     case 1: 
/* 3512 */       unpackedRowData[columnIndex] = { binaryData.readByte() };
/* 3513 */       break;
/*      */     
/*      */ 
/*      */     case 2: 
/*      */     case 13: 
/* 3518 */       unpackedRowData[columnIndex] = binaryData.getBytes(2);
/* 3519 */       break;
/*      */     
/*      */     case 3: 
/*      */     case 9: 
/* 3523 */       unpackedRowData[columnIndex] = binaryData.getBytes(4);
/* 3524 */       break;
/*      */     
/*      */     case 8: 
/* 3527 */       unpackedRowData[columnIndex] = binaryData.getBytes(8);
/* 3528 */       break;
/*      */     
/*      */     case 4: 
/* 3531 */       unpackedRowData[columnIndex] = binaryData.getBytes(4);
/* 3532 */       break;
/*      */     
/*      */     case 5: 
/* 3535 */       unpackedRowData[columnIndex] = binaryData.getBytes(8);
/* 3536 */       break;
/*      */     
/*      */     case 11: 
/* 3539 */       int length = (int)binaryData.readFieldLength();
/*      */       
/* 3541 */       unpackedRowData[columnIndex] = binaryData.getBytes(length);
/*      */       
/* 3543 */       break;
/*      */     
/*      */     case 10: 
/* 3546 */       int length = (int)binaryData.readFieldLength();
/*      */       
/* 3548 */       unpackedRowData[columnIndex] = binaryData.getBytes(length);
/*      */       
/* 3550 */       break;
/*      */     case 7: 
/*      */     case 12: 
/* 3553 */       int length = (int)binaryData.readFieldLength();
/*      */       
/* 3555 */       unpackedRowData[columnIndex] = binaryData.getBytes(length);
/* 3556 */       break;
/*      */     case 0: 
/*      */     case 15: 
/*      */     case 246: 
/*      */     case 249: 
/*      */     case 250: 
/*      */     case 251: 
/*      */     case 252: 
/*      */     case 253: 
/*      */     case 254: 
/*      */     case 255: 
/* 3567 */       unpackedRowData[columnIndex] = binaryData.readLenByteArray(0);
/*      */       
/* 3569 */       break;
/*      */     case 16: 
/* 3571 */       unpackedRowData[columnIndex] = binaryData.readLenByteArray(0);
/*      */       
/* 3573 */       break;
/*      */     default: 
/* 3575 */       throw SQLError.createSQLException(Messages.getString("MysqlIO.97") + curField.getMysqlType() + Messages.getString("MysqlIO.98") + columnIndex + Messages.getString("MysqlIO.99") + fields.length + Messages.getString("MysqlIO.100"), "S1000");
/*      */     }
/*      */     
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   private final void unpackNativeEncodedColumn(Buffer binaryData, Field[] fields, int columnIndex, Object[] unpackedRowData)
/*      */     throws SQLException
/*      */   {
/* 3587 */     Field curField = fields[columnIndex];
/*      */     
/* 3589 */     switch (curField.getMysqlType())
/*      */     {
/*      */     case 6: 
/*      */       break;
/*      */     
/*      */     case 1: 
/* 3595 */       byte tinyVal = binaryData.readByte();
/*      */       
/* 3597 */       if (!curField.isUnsigned()) {
/* 3598 */         unpackedRowData[columnIndex] = String.valueOf(tinyVal).getBytes();
/*      */       }
/*      */       else {
/* 3601 */         short unsignedTinyVal = (short)(tinyVal & 0xFF);
/*      */         
/* 3603 */         unpackedRowData[columnIndex] = String.valueOf(unsignedTinyVal).getBytes();
/*      */       }
/*      */       
/*      */ 
/* 3607 */       break;
/*      */     
/*      */ 
/*      */     case 2: 
/*      */     case 13: 
/* 3612 */       short shortVal = (short)binaryData.readInt();
/*      */       
/* 3614 */       if (!curField.isUnsigned()) {
/* 3615 */         unpackedRowData[columnIndex] = String.valueOf(shortVal).getBytes();
/*      */       }
/*      */       else {
/* 3618 */         int unsignedShortVal = shortVal & 0xFFFF;
/*      */         
/* 3620 */         unpackedRowData[columnIndex] = String.valueOf(unsignedShortVal).getBytes();
/*      */       }
/*      */       
/*      */ 
/* 3624 */       break;
/*      */     
/*      */ 
/*      */     case 3: 
/*      */     case 9: 
/* 3629 */       int intVal = (int)binaryData.readLong();
/*      */       
/* 3631 */       if (!curField.isUnsigned()) {
/* 3632 */         unpackedRowData[columnIndex] = String.valueOf(intVal).getBytes();
/*      */       }
/*      */       else {
/* 3635 */         long longVal = intVal & 0xFFFFFFFF;
/*      */         
/* 3637 */         unpackedRowData[columnIndex] = String.valueOf(longVal).getBytes();
/*      */       }
/*      */       
/*      */ 
/* 3641 */       break;
/*      */     
/*      */ 
/*      */     case 8: 
/* 3645 */       long longVal = binaryData.readLongLong();
/*      */       
/* 3647 */       if (!curField.isUnsigned()) {
/* 3648 */         unpackedRowData[columnIndex] = String.valueOf(longVal).getBytes();
/*      */       }
/*      */       else {
/* 3651 */         BigInteger asBigInteger = ResultSet.convertLongToUlong(longVal);
/*      */         
/* 3653 */         unpackedRowData[columnIndex] = asBigInteger.toString().getBytes();
/*      */       }
/*      */       
/*      */ 
/* 3657 */       break;
/*      */     
/*      */ 
/*      */     case 4: 
/* 3661 */       float floatVal = Float.intBitsToFloat(binaryData.readIntAsLong());
/*      */       
/* 3663 */       unpackedRowData[columnIndex] = String.valueOf(floatVal).getBytes();
/*      */       
/* 3665 */       break;
/*      */     
/*      */ 
/*      */     case 5: 
/* 3669 */       double doubleVal = Double.longBitsToDouble(binaryData.readLongLong());
/*      */       
/* 3671 */       unpackedRowData[columnIndex] = String.valueOf(doubleVal).getBytes();
/*      */       
/* 3673 */       break;
/*      */     
/*      */ 
/*      */     case 11: 
/* 3677 */       int length = (int)binaryData.readFieldLength();
/*      */       
/* 3679 */       int hour = 0;
/* 3680 */       int minute = 0;
/* 3681 */       int seconds = 0;
/*      */       
/* 3683 */       if (length != 0) {
/* 3684 */         binaryData.readByte();
/* 3685 */         binaryData.readLong();
/* 3686 */         hour = binaryData.readByte();
/* 3687 */         minute = binaryData.readByte();
/* 3688 */         seconds = binaryData.readByte();
/*      */         
/* 3690 */         if (length > 8) {
/* 3691 */           binaryData.readLong();
/*      */         }
/*      */       }
/*      */       
/*      */ 
/* 3696 */       byte[] timeAsBytes = new byte[8];
/*      */       
/* 3698 */       timeAsBytes[0] = ((byte)Character.forDigit(hour / 10, 10));
/* 3699 */       timeAsBytes[1] = ((byte)Character.forDigit(hour % 10, 10));
/*      */       
/* 3701 */       timeAsBytes[2] = 58;
/*      */       
/* 3703 */       timeAsBytes[3] = ((byte)Character.forDigit(minute / 10, 10));
/*      */       
/* 3705 */       timeAsBytes[4] = ((byte)Character.forDigit(minute % 10, 10));
/*      */       
/*      */ 
/* 3708 */       timeAsBytes[5] = 58;
/*      */       
/* 3710 */       timeAsBytes[6] = ((byte)Character.forDigit(seconds / 10, 10));
/*      */       
/* 3712 */       timeAsBytes[7] = ((byte)Character.forDigit(seconds % 10, 10));
/*      */       
/*      */ 
/* 3715 */       unpackedRowData[columnIndex] = timeAsBytes;
/*      */       
/*      */ 
/* 3718 */       break;
/*      */     
/*      */     case 10: 
/* 3721 */       int length = (int)binaryData.readFieldLength();
/*      */       
/* 3723 */       int year = 0;
/* 3724 */       int month = 0;
/* 3725 */       int day = 0;
/*      */       
/* 3727 */       int hour = 0;
/* 3728 */       int minute = 0;
/* 3729 */       int seconds = 0;
/*      */       
/* 3731 */       if (length != 0) {
/* 3732 */         year = binaryData.readInt();
/* 3733 */         month = binaryData.readByte();
/* 3734 */         day = binaryData.readByte();
/*      */       }
/*      */       
/* 3737 */       if ((year == 0) && (month == 0) && (day == 0)) {
/* 3738 */         if ("convertToNull".equals(this.connection.getZeroDateTimeBehavior()))
/*      */         {
/* 3740 */           unpackedRowData[columnIndex] = null;
/*      */         }
/*      */         else {
/* 3743 */           if ("exception".equals(this.connection.getZeroDateTimeBehavior()))
/*      */           {
/* 3745 */             throw SQLError.createSQLException("Value '0000-00-00' can not be represented as java.sql.Date", "S1009");
/*      */           }
/*      */           
/*      */ 
/* 3749 */           year = 1;
/* 3750 */           month = 1;
/* 3751 */           day = 1;
/*      */         }
/*      */       }
/*      */       else {
/* 3755 */         byte[] dateAsBytes = new byte[10];
/*      */         
/* 3757 */         dateAsBytes[0] = ((byte)Character.forDigit(year / 1000, 10));
/*      */         
/*      */ 
/* 3760 */         int after1000 = year % 1000;
/*      */         
/* 3762 */         dateAsBytes[1] = ((byte)Character.forDigit(after1000 / 100, 10));
/*      */         
/*      */ 
/* 3765 */         int after100 = after1000 % 100;
/*      */         
/* 3767 */         dateAsBytes[2] = ((byte)Character.forDigit(after100 / 10, 10));
/*      */         
/* 3769 */         dateAsBytes[3] = ((byte)Character.forDigit(after100 % 10, 10));
/*      */         
/*      */ 
/* 3772 */         dateAsBytes[4] = 45;
/*      */         
/* 3774 */         dateAsBytes[5] = ((byte)Character.forDigit(month / 10, 10));
/*      */         
/* 3776 */         dateAsBytes[6] = ((byte)Character.forDigit(month % 10, 10));
/*      */         
/*      */ 
/* 3779 */         dateAsBytes[7] = 45;
/*      */         
/* 3781 */         dateAsBytes[8] = ((byte)Character.forDigit(day / 10, 10));
/* 3782 */         dateAsBytes[9] = ((byte)Character.forDigit(day % 10, 10));
/*      */         
/* 3784 */         unpackedRowData[columnIndex] = dateAsBytes;
/*      */       }
/*      */       
/* 3787 */       break;
/*      */     
/*      */     case 7: 
/*      */     case 12: 
/* 3791 */       int length = (int)binaryData.readFieldLength();
/*      */       
/* 3793 */       int year = 0;
/* 3794 */       int month = 0;
/* 3795 */       int day = 0;
/*      */       
/* 3797 */       int hour = 0;
/* 3798 */       int minute = 0;
/* 3799 */       int seconds = 0;
/*      */       
/* 3801 */       int nanos = 0;
/*      */       
/* 3803 */       if (length != 0) {
/* 3804 */         year = binaryData.readInt();
/* 3805 */         month = binaryData.readByte();
/* 3806 */         day = binaryData.readByte();
/*      */         
/* 3808 */         if (length > 4) {
/* 3809 */           hour = binaryData.readByte();
/* 3810 */           minute = binaryData.readByte();
/* 3811 */           seconds = binaryData.readByte();
/*      */         }
/*      */       }
/*      */       
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 3819 */       if ((year == 0) && (month == 0) && (day == 0)) {
/* 3820 */         if ("convertToNull".equals(this.connection.getZeroDateTimeBehavior()))
/*      */         {
/* 3822 */           unpackedRowData[columnIndex] = null;
/*      */         }
/*      */         else {
/* 3825 */           if ("exception".equals(this.connection.getZeroDateTimeBehavior()))
/*      */           {
/* 3827 */             throw SQLError.createSQLException("Value '0000-00-00' can not be represented as java.sql.Timestamp", "S1009");
/*      */           }
/*      */           
/*      */ 
/* 3831 */           year = 1;
/* 3832 */           month = 1;
/* 3833 */           day = 1;
/*      */         }
/*      */       }
/*      */       else {
/* 3837 */         int stringLength = 19;
/*      */         
/* 3839 */         byte[] nanosAsBytes = Integer.toString(nanos).getBytes();
/*      */         
/* 3841 */         stringLength += 1 + nanosAsBytes.length;
/*      */         
/* 3843 */         byte[] datetimeAsBytes = new byte[stringLength];
/*      */         
/* 3845 */         datetimeAsBytes[0] = ((byte)Character.forDigit(year / 1000, 10));
/*      */         
/*      */ 
/* 3848 */         int after1000 = year % 1000;
/*      */         
/* 3850 */         datetimeAsBytes[1] = ((byte)Character.forDigit(after1000 / 100, 10));
/*      */         
/*      */ 
/* 3853 */         int after100 = after1000 % 100;
/*      */         
/* 3855 */         datetimeAsBytes[2] = ((byte)Character.forDigit(after100 / 10, 10));
/*      */         
/* 3857 */         datetimeAsBytes[3] = ((byte)Character.forDigit(after100 % 10, 10));
/*      */         
/*      */ 
/* 3860 */         datetimeAsBytes[4] = 45;
/*      */         
/* 3862 */         datetimeAsBytes[5] = ((byte)Character.forDigit(month / 10, 10));
/*      */         
/* 3864 */         datetimeAsBytes[6] = ((byte)Character.forDigit(month % 10, 10));
/*      */         
/*      */ 
/* 3867 */         datetimeAsBytes[7] = 45;
/*      */         
/* 3869 */         datetimeAsBytes[8] = ((byte)Character.forDigit(day / 10, 10));
/*      */         
/* 3871 */         datetimeAsBytes[9] = ((byte)Character.forDigit(day % 10, 10));
/*      */         
/*      */ 
/* 3874 */         datetimeAsBytes[10] = 32;
/*      */         
/* 3876 */         datetimeAsBytes[11] = ((byte)Character.forDigit(hour / 10, 10));
/*      */         
/* 3878 */         datetimeAsBytes[12] = ((byte)Character.forDigit(hour % 10, 10));
/*      */         
/*      */ 
/* 3881 */         datetimeAsBytes[13] = 58;
/*      */         
/* 3883 */         datetimeAsBytes[14] = ((byte)Character.forDigit(minute / 10, 10));
/*      */         
/* 3885 */         datetimeAsBytes[15] = ((byte)Character.forDigit(minute % 10, 10));
/*      */         
/*      */ 
/* 3888 */         datetimeAsBytes[16] = 58;
/*      */         
/* 3890 */         datetimeAsBytes[17] = ((byte)Character.forDigit(seconds / 10, 10));
/*      */         
/* 3892 */         datetimeAsBytes[18] = ((byte)Character.forDigit(seconds % 10, 10));
/*      */         
/*      */ 
/* 3895 */         datetimeAsBytes[19] = 46;
/*      */         
/* 3897 */         int nanosOffset = 20;
/*      */         
/* 3899 */         for (int j = 0; j < nanosAsBytes.length; j++) {
/* 3900 */           datetimeAsBytes[(nanosOffset + j)] = nanosAsBytes[j];
/*      */         }
/*      */         
/* 3903 */         unpackedRowData[columnIndex] = datetimeAsBytes;
/*      */       }
/*      */       
/* 3906 */       break;
/*      */     
/*      */     case 0: 
/*      */     case 15: 
/*      */     case 16: 
/*      */     case 246: 
/*      */     case 249: 
/*      */     case 250: 
/*      */     case 251: 
/*      */     case 252: 
/*      */     case 253: 
/*      */     case 254: 
/* 3918 */       unpackedRowData[columnIndex] = binaryData.readLenByteArray(0);
/*      */       
/* 3920 */       break;
/*      */     
/*      */     default: 
/* 3923 */       throw SQLError.createSQLException(Messages.getString("MysqlIO.97") + curField.getMysqlType() + Messages.getString("MysqlIO.98") + columnIndex + Messages.getString("MysqlIO.99") + fields.length + Messages.getString("MysqlIO.100"), "S1000");
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
/*      */   private Calendar getCalendarInstanceForSessionOrNew()
/*      */   {
/* 3937 */     if (this.connection.getDynamicCalendars()) {
/* 3938 */       return Calendar.getInstance();
/*      */     }
/* 3940 */     return this.sessionCalendar;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   private void negotiateSSLConnection(String user, String password, String database, int packLength)
/*      */     throws SQLException, CommunicationsException
/*      */   {
/* 3958 */     if (!ExportControlled.enabled()) {
/* 3959 */       throw new ConnectionFeatureNotAvailableException(this.connection, this.lastPacketSentTimeMs, null);
/*      */     }
/*      */     
/*      */ 
/* 3963 */     boolean doSecureAuth = false;
/*      */     
/* 3965 */     if ((this.serverCapabilities & 0x8000) != 0) {
/* 3966 */       this.clientParam |= 0x8000;
/* 3967 */       doSecureAuth = true;
/*      */     }
/*      */     
/* 3970 */     this.clientParam |= 0x800;
/*      */     
/* 3972 */     Buffer packet = new Buffer(packLength);
/*      */     
/* 3974 */     if (this.use41Extensions) {
/* 3975 */       packet.writeLong(this.clientParam);
/*      */     } else {
/* 3977 */       packet.writeInt((int)this.clientParam);
/*      */     }
/*      */     
/* 3980 */     send(packet, packet.getPosition());
/*      */     
/* 3982 */     ExportControlled.transformSocketToSSLSocket(this);
/*      */     
/* 3984 */     packet.clear();
/*      */     
/* 3986 */     if (doSecureAuth) {
/* 3987 */       if (versionMeetsMinimum(4, 1, 1)) {
/* 3988 */         secureAuth411(null, packLength, user, password, database, true);
/*      */       } else {
/* 3990 */         secureAuth411(null, packLength, user, password, database, true);
/*      */       }
/*      */     } else {
/* 3993 */       if (this.use41Extensions) {
/* 3994 */         packet.writeLong(this.clientParam);
/* 3995 */         packet.writeLong(this.maxThreeBytes);
/*      */       } else {
/* 3997 */         packet.writeInt((int)this.clientParam);
/* 3998 */         packet.writeLongInt(this.maxThreeBytes);
/*      */       }
/*      */       
/*      */ 
/* 4002 */       packet.writeString(user);
/*      */       
/* 4004 */       if (this.protocolVersion > 9) {
/* 4005 */         packet.writeString(Util.newCrypt(password, this.seed));
/*      */       } else {
/* 4007 */         packet.writeString(Util.oldCrypt(password, this.seed));
/*      */       }
/*      */       
/* 4010 */       if (((this.serverCapabilities & 0x8) != 0) && (database != null) && (database.length() > 0))
/*      */       {
/* 4012 */         packet.writeString(database);
/*      */       }
/*      */       
/* 4015 */       send(packet, packet.getPosition());
/*      */     }
/*      */   }
/*      */   
/*      */   protected int getServerStatus() {
/* 4020 */     return this.serverStatus;
/*      */   }
/*      */   
/*      */   protected List fetchRowsViaCursor(List fetchedRows, long statementId, Field[] columnTypes, int fetchSize)
/*      */     throws SQLException
/*      */   {
/* 4026 */     if (fetchedRows == null) {
/* 4027 */       fetchedRows = new ArrayList(fetchSize);
/*      */     } else {
/* 4029 */       fetchedRows.clear();
/*      */     }
/*      */     
/* 4032 */     this.sharedSendPacket.clear();
/*      */     
/* 4034 */     this.sharedSendPacket.writeByte((byte)28);
/* 4035 */     this.sharedSendPacket.writeLong(statementId);
/* 4036 */     this.sharedSendPacket.writeLong(fetchSize);
/*      */     
/* 4038 */     sendCommand(28, null, this.sharedSendPacket, true, null);
/*      */     
/*      */ 
/* 4041 */     Object[] row = null;
/*      */     
/*      */ 
/* 4044 */     while ((row = nextRow(columnTypes, columnTypes.length, true, 1007)) != null) {
/* 4045 */       fetchedRows.add(row);
/*      */     }
/*      */     
/* 4048 */     return fetchedRows;
/*      */   }
/*      */   
/*      */   protected long getThreadId() {
/* 4052 */     return this.threadId;
/*      */   }
/*      */   
/*      */   protected boolean useNanosForElapsedTime() {
/* 4056 */     return this.useNanosForElapsedTime;
/*      */   }
/*      */   
/*      */   protected long getSlowQueryThreshold() {
/* 4060 */     return this.slowQueryThreshold;
/*      */   }
/*      */   
/*      */   protected String getQueryTimingUnits() {
/* 4064 */     return this.queryTimingUnits;
/*      */   }
/*      */   
/*      */   /* Error */
/*      */   protected final void skipPacket()
/*      */     throws SQLException
/*      */   {
/*      */     // Byte code:
/*      */     //   0: aload_0
/*      */     //   1: aload_0
/*      */     //   2: getfield 9	com/mysql/jdbc/MysqlIO:mysqlInput	Ljava/io/InputStream;
/*      */     //   5: aload_0
/*      */     //   6: getfield 17	com/mysql/jdbc/MysqlIO:packetHeaderBuf	[B
/*      */     //   9: iconst_0
/*      */     //   10: iconst_4
/*      */     //   11: invokespecial 117	com/mysql/jdbc/MysqlIO:readFully	(Ljava/io/InputStream;[BII)I
/*      */     //   14: istore_1
/*      */     //   15: iload_1
/*      */     //   16: iconst_4
/*      */     //   17: if_icmpge +20 -> 37
/*      */     //   20: aload_0
/*      */     //   21: invokevirtual 118	com/mysql/jdbc/MysqlIO:forceClose	()V
/*      */     //   24: new 90	java/io/IOException
/*      */     //   27: dup
/*      */     //   28: ldc 119
/*      */     //   30: invokestatic 85	com/mysql/jdbc/Messages:getString	(Ljava/lang/String;)Ljava/lang/String;
/*      */     //   33: invokespecial 120	java/io/IOException:<init>	(Ljava/lang/String;)V
/*      */     //   36: athrow
/*      */     //   37: aload_0
/*      */     //   38: getfield 17	com/mysql/jdbc/MysqlIO:packetHeaderBuf	[B
/*      */     //   41: iconst_0
/*      */     //   42: baload
/*      */     //   43: sipush 255
/*      */     //   46: iand
/*      */     //   47: aload_0
/*      */     //   48: getfield 17	com/mysql/jdbc/MysqlIO:packetHeaderBuf	[B
/*      */     //   51: iconst_1
/*      */     //   52: baload
/*      */     //   53: sipush 255
/*      */     //   56: iand
/*      */     //   57: bipush 8
/*      */     //   59: ishl
/*      */     //   60: iadd
/*      */     //   61: aload_0
/*      */     //   62: getfield 17	com/mysql/jdbc/MysqlIO:packetHeaderBuf	[B
/*      */     //   65: iconst_2
/*      */     //   66: baload
/*      */     //   67: sipush 255
/*      */     //   70: iand
/*      */     //   71: bipush 16
/*      */     //   73: ishl
/*      */     //   74: iadd
/*      */     //   75: istore_2
/*      */     //   76: aload_0
/*      */     //   77: getfield 48	com/mysql/jdbc/MysqlIO:traceProtocol	Z
/*      */     //   80: ifeq +66 -> 146
/*      */     //   83: new 121	java/lang/StringBuffer
/*      */     //   86: dup
/*      */     //   87: invokespecial 122	java/lang/StringBuffer:<init>	()V
/*      */     //   90: astore_3
/*      */     //   91: aload_3
/*      */     //   92: ldc 123
/*      */     //   94: invokestatic 85	com/mysql/jdbc/Messages:getString	(Ljava/lang/String;)Ljava/lang/String;
/*      */     //   97: invokevirtual 124	java/lang/StringBuffer:append	(Ljava/lang/String;)Ljava/lang/StringBuffer;
/*      */     //   100: pop
/*      */     //   101: aload_3
/*      */     //   102: iload_2
/*      */     //   103: invokevirtual 125	java/lang/StringBuffer:append	(I)Ljava/lang/StringBuffer;
/*      */     //   106: pop
/*      */     //   107: aload_3
/*      */     //   108: ldc 126
/*      */     //   110: invokestatic 85	com/mysql/jdbc/Messages:getString	(Ljava/lang/String;)Ljava/lang/String;
/*      */     //   113: invokevirtual 124	java/lang/StringBuffer:append	(Ljava/lang/String;)Ljava/lang/StringBuffer;
/*      */     //   116: pop
/*      */     //   117: aload_3
/*      */     //   118: aload_0
/*      */     //   119: getfield 17	com/mysql/jdbc/MysqlIO:packetHeaderBuf	[B
/*      */     //   122: iconst_4
/*      */     //   123: invokestatic 127	com/mysql/jdbc/StringUtils:dumpAsHex	([BI)Ljava/lang/String;
/*      */     //   126: invokevirtual 124	java/lang/StringBuffer:append	(Ljava/lang/String;)Ljava/lang/StringBuffer;
/*      */     //   129: pop
/*      */     //   130: aload_0
/*      */     //   131: getfield 50	com/mysql/jdbc/MysqlIO:connection	Lcom/mysql/jdbc/Connection;
/*      */     //   134: invokevirtual 66	com/mysql/jdbc/Connection:getLog	()Lcom/mysql/jdbc/log/Log;
/*      */     //   137: aload_3
/*      */     //   138: invokevirtual 128	java/lang/StringBuffer:toString	()Ljava/lang/String;
/*      */     //   141: invokeinterface 129 2 0
/*      */     //   146: aload_0
/*      */     //   147: getfield 17	com/mysql/jdbc/MysqlIO:packetHeaderBuf	[B
/*      */     //   150: iconst_3
/*      */     //   151: baload
/*      */     //   152: istore_3
/*      */     //   153: aload_0
/*      */     //   154: getfield 3	com/mysql/jdbc/MysqlIO:packetSequenceReset	Z
/*      */     //   157: ifne +25 -> 182
/*      */     //   160: aload_0
/*      */     //   161: getfield 49	com/mysql/jdbc/MysqlIO:enablePacketDebug	Z
/*      */     //   164: ifeq +23 -> 187
/*      */     //   167: aload_0
/*      */     //   168: getfield 34	com/mysql/jdbc/MysqlIO:checkPacketSequence	Z
/*      */     //   171: ifeq +16 -> 187
/*      */     //   174: aload_0
/*      */     //   175: iload_3
/*      */     //   176: invokespecial 130	com/mysql/jdbc/MysqlIO:checkPacketSequencing	(B)V
/*      */     //   179: goto +8 -> 187
/*      */     //   182: aload_0
/*      */     //   183: iconst_0
/*      */     //   184: putfield 3	com/mysql/jdbc/MysqlIO:packetSequenceReset	Z
/*      */     //   187: aload_0
/*      */     //   188: iload_3
/*      */     //   189: putfield 33	com/mysql/jdbc/MysqlIO:readPacketSequence	B
/*      */     //   192: aload_0
/*      */     //   193: aload_0
/*      */     //   194: getfield 9	com/mysql/jdbc/MysqlIO:mysqlInput	Ljava/io/InputStream;
/*      */     //   197: iload_2
/*      */     //   198: i2l
/*      */     //   199: invokespecial 131	com/mysql/jdbc/MysqlIO:skipFully	(Ljava/io/InputStream;J)J
/*      */     //   202: pop2
/*      */     //   203: goto +39 -> 242
/*      */     //   206: astore_1
/*      */     //   207: new 91	com/mysql/jdbc/CommunicationsException
/*      */     //   210: dup
/*      */     //   211: aload_0
/*      */     //   212: getfield 50	com/mysql/jdbc/MysqlIO:connection	Lcom/mysql/jdbc/Connection;
/*      */     //   215: aload_0
/*      */     //   216: getfield 47	com/mysql/jdbc/MysqlIO:lastPacketSentTimeMs	J
/*      */     //   219: aload_1
/*      */     //   220: invokespecial 92	com/mysql/jdbc/CommunicationsException:<init>	(Lcom/mysql/jdbc/Connection;JLjava/lang/Exception;)V
/*      */     //   223: athrow
/*      */     //   224: astore_1
/*      */     //   225: aload_0
/*      */     //   226: getfield 50	com/mysql/jdbc/MysqlIO:connection	Lcom/mysql/jdbc/Connection;
/*      */     //   229: iconst_0
/*      */     //   230: iconst_0
/*      */     //   231: iconst_1
/*      */     //   232: aload_1
/*      */     //   233: invokevirtual 133	com/mysql/jdbc/Connection:realClose	(ZZZLjava/lang/Throwable;)V
/*      */     //   236: aload_1
/*      */     //   237: athrow
/*      */     //   238: astore 4
/*      */     //   240: aload_1
/*      */     //   241: athrow
/*      */     //   242: return
/*      */     // Line number table:
/*      */     //   Java source line #511	-> byte code offset #0
/*      */     //   Java source line #514	-> byte code offset #15
/*      */     //   Java source line #515	-> byte code offset #20
/*      */     //   Java source line #516	-> byte code offset #24
/*      */     //   Java source line #519	-> byte code offset #37
/*      */     //   Java source line #523	-> byte code offset #76
/*      */     //   Java source line #524	-> byte code offset #83
/*      */     //   Java source line #526	-> byte code offset #91
/*      */     //   Java source line #527	-> byte code offset #101
/*      */     //   Java source line #528	-> byte code offset #107
/*      */     //   Java source line #529	-> byte code offset #117
/*      */     //   Java source line #532	-> byte code offset #130
/*      */     //   Java source line #535	-> byte code offset #146
/*      */     //   Java source line #537	-> byte code offset #153
/*      */     //   Java source line #538	-> byte code offset #160
/*      */     //   Java source line #539	-> byte code offset #174
/*      */     //   Java source line #542	-> byte code offset #182
/*      */     //   Java source line #545	-> byte code offset #187
/*      */     //   Java source line #547	-> byte code offset #192
/*      */     //   Java source line #557	-> byte code offset #203
/*      */     //   Java source line #548	-> byte code offset #206
/*      */     //   Java source line #549	-> byte code offset #207
/*      */     //   Java source line #551	-> byte code offset #224
/*      */     //   Java source line #553	-> byte code offset #225
/*      */     //   Java source line #555	-> byte code offset #236
/*      */     //   Java source line #558	-> byte code offset #242
/*      */     // Local variable table:
/*      */     //   start	length	slot	name	signature
/*      */     //   0	243	0	this	MysqlIO
/*      */     //   14	2	1	lengthRead	int
/*      */     //   206	14	1	ioEx	IOException
/*      */     //   224	17	1	oom	OutOfMemoryError
/*      */     //   75	123	2	packetLength	int
/*      */     //   90	48	3	traceMessageBuf	StringBuffer
/*      */     //   152	37	3	multiPacketSeq	byte
/*      */     //   238	1	4	localObject	Object
/*      */     // Exception table:
/*      */     //   from	to	target	type
/*      */     //   0	203	206	java/io/IOException
/*      */     //   0	203	224	java/lang/OutOfMemoryError
/*      */     //   225	236	238	finally
/*      */     //   238	240	238	finally
/*      */   }
/*      */   
/*      */   /* Error */
/*      */   protected final Buffer readPacket()
/*      */     throws SQLException
/*      */   {
/*      */     // Byte code:
/*      */     //   0: aload_0
/*      */     //   1: aload_0
/*      */     //   2: getfield 9	com/mysql/jdbc/MysqlIO:mysqlInput	Ljava/io/InputStream;
/*      */     //   5: aload_0
/*      */     //   6: getfield 17	com/mysql/jdbc/MysqlIO:packetHeaderBuf	[B
/*      */     //   9: iconst_0
/*      */     //   10: iconst_4
/*      */     //   11: invokespecial 117	com/mysql/jdbc/MysqlIO:readFully	(Ljava/io/InputStream;[BII)I
/*      */     //   14: istore_1
/*      */     //   15: iload_1
/*      */     //   16: iconst_4
/*      */     //   17: if_icmpge +20 -> 37
/*      */     //   20: aload_0
/*      */     //   21: invokevirtual 118	com/mysql/jdbc/MysqlIO:forceClose	()V
/*      */     //   24: new 90	java/io/IOException
/*      */     //   27: dup
/*      */     //   28: ldc 119
/*      */     //   30: invokestatic 85	com/mysql/jdbc/Messages:getString	(Ljava/lang/String;)Ljava/lang/String;
/*      */     //   33: invokespecial 120	java/io/IOException:<init>	(Ljava/lang/String;)V
/*      */     //   36: athrow
/*      */     //   37: aload_0
/*      */     //   38: getfield 17	com/mysql/jdbc/MysqlIO:packetHeaderBuf	[B
/*      */     //   41: iconst_0
/*      */     //   42: baload
/*      */     //   43: sipush 255
/*      */     //   46: iand
/*      */     //   47: aload_0
/*      */     //   48: getfield 17	com/mysql/jdbc/MysqlIO:packetHeaderBuf	[B
/*      */     //   51: iconst_1
/*      */     //   52: baload
/*      */     //   53: sipush 255
/*      */     //   56: iand
/*      */     //   57: bipush 8
/*      */     //   59: ishl
/*      */     //   60: iadd
/*      */     //   61: aload_0
/*      */     //   62: getfield 17	com/mysql/jdbc/MysqlIO:packetHeaderBuf	[B
/*      */     //   65: iconst_2
/*      */     //   66: baload
/*      */     //   67: sipush 255
/*      */     //   70: iand
/*      */     //   71: bipush 16
/*      */     //   73: ishl
/*      */     //   74: iadd
/*      */     //   75: istore_2
/*      */     //   76: aload_0
/*      */     //   77: getfield 48	com/mysql/jdbc/MysqlIO:traceProtocol	Z
/*      */     //   80: ifeq +66 -> 146
/*      */     //   83: new 121	java/lang/StringBuffer
/*      */     //   86: dup
/*      */     //   87: invokespecial 122	java/lang/StringBuffer:<init>	()V
/*      */     //   90: astore_3
/*      */     //   91: aload_3
/*      */     //   92: ldc 123
/*      */     //   94: invokestatic 85	com/mysql/jdbc/Messages:getString	(Ljava/lang/String;)Ljava/lang/String;
/*      */     //   97: invokevirtual 124	java/lang/StringBuffer:append	(Ljava/lang/String;)Ljava/lang/StringBuffer;
/*      */     //   100: pop
/*      */     //   101: aload_3
/*      */     //   102: iload_2
/*      */     //   103: invokevirtual 125	java/lang/StringBuffer:append	(I)Ljava/lang/StringBuffer;
/*      */     //   106: pop
/*      */     //   107: aload_3
/*      */     //   108: ldc 126
/*      */     //   110: invokestatic 85	com/mysql/jdbc/Messages:getString	(Ljava/lang/String;)Ljava/lang/String;
/*      */     //   113: invokevirtual 124	java/lang/StringBuffer:append	(Ljava/lang/String;)Ljava/lang/StringBuffer;
/*      */     //   116: pop
/*      */     //   117: aload_3
/*      */     //   118: aload_0
/*      */     //   119: getfield 17	com/mysql/jdbc/MysqlIO:packetHeaderBuf	[B
/*      */     //   122: iconst_4
/*      */     //   123: invokestatic 127	com/mysql/jdbc/StringUtils:dumpAsHex	([BI)Ljava/lang/String;
/*      */     //   126: invokevirtual 124	java/lang/StringBuffer:append	(Ljava/lang/String;)Ljava/lang/StringBuffer;
/*      */     //   129: pop
/*      */     //   130: aload_0
/*      */     //   131: getfield 50	com/mysql/jdbc/MysqlIO:connection	Lcom/mysql/jdbc/Connection;
/*      */     //   134: invokevirtual 66	com/mysql/jdbc/Connection:getLog	()Lcom/mysql/jdbc/log/Log;
/*      */     //   137: aload_3
/*      */     //   138: invokevirtual 128	java/lang/StringBuffer:toString	()Ljava/lang/String;
/*      */     //   141: invokeinterface 129 2 0
/*      */     //   146: aload_0
/*      */     //   147: getfield 17	com/mysql/jdbc/MysqlIO:packetHeaderBuf	[B
/*      */     //   150: iconst_3
/*      */     //   151: baload
/*      */     //   152: istore_3
/*      */     //   153: aload_0
/*      */     //   154: getfield 3	com/mysql/jdbc/MysqlIO:packetSequenceReset	Z
/*      */     //   157: ifne +25 -> 182
/*      */     //   160: aload_0
/*      */     //   161: getfield 49	com/mysql/jdbc/MysqlIO:enablePacketDebug	Z
/*      */     //   164: ifeq +23 -> 187
/*      */     //   167: aload_0
/*      */     //   168: getfield 34	com/mysql/jdbc/MysqlIO:checkPacketSequence	Z
/*      */     //   171: ifeq +16 -> 187
/*      */     //   174: aload_0
/*      */     //   175: iload_3
/*      */     //   176: invokespecial 130	com/mysql/jdbc/MysqlIO:checkPacketSequencing	(B)V
/*      */     //   179: goto +8 -> 187
/*      */     //   182: aload_0
/*      */     //   183: iconst_0
/*      */     //   184: putfield 3	com/mysql/jdbc/MysqlIO:packetSequenceReset	Z
/*      */     //   187: aload_0
/*      */     //   188: iload_3
/*      */     //   189: putfield 33	com/mysql/jdbc/MysqlIO:readPacketSequence	B
/*      */     //   192: iload_2
/*      */     //   193: iconst_1
/*      */     //   194: iadd
/*      */     //   195: newarray <illegal type>
/*      */     //   197: astore 4
/*      */     //   199: aload_0
/*      */     //   200: aload_0
/*      */     //   201: getfield 9	com/mysql/jdbc/MysqlIO:mysqlInput	Ljava/io/InputStream;
/*      */     //   204: aload 4
/*      */     //   206: iconst_0
/*      */     //   207: iload_2
/*      */     //   208: invokespecial 117	com/mysql/jdbc/MysqlIO:readFully	(Ljava/io/InputStream;[BII)I
/*      */     //   211: istore 5
/*      */     //   213: iload 5
/*      */     //   215: iload_2
/*      */     //   216: if_icmpeq +40 -> 256
/*      */     //   219: new 90	java/io/IOException
/*      */     //   222: dup
/*      */     //   223: new 121	java/lang/StringBuffer
/*      */     //   226: dup
/*      */     //   227: invokespecial 122	java/lang/StringBuffer:<init>	()V
/*      */     //   230: ldc -122
/*      */     //   232: invokevirtual 124	java/lang/StringBuffer:append	(Ljava/lang/String;)Ljava/lang/StringBuffer;
/*      */     //   235: iload_2
/*      */     //   236: invokevirtual 125	java/lang/StringBuffer:append	(I)Ljava/lang/StringBuffer;
/*      */     //   239: ldc -121
/*      */     //   241: invokevirtual 124	java/lang/StringBuffer:append	(Ljava/lang/String;)Ljava/lang/StringBuffer;
/*      */     //   244: iload 5
/*      */     //   246: invokevirtual 125	java/lang/StringBuffer:append	(I)Ljava/lang/StringBuffer;
/*      */     //   249: invokevirtual 128	java/lang/StringBuffer:toString	()Ljava/lang/String;
/*      */     //   252: invokespecial 120	java/io/IOException:<init>	(Ljava/lang/String;)V
/*      */     //   255: athrow
/*      */     //   256: aload 4
/*      */     //   258: iload_2
/*      */     //   259: iconst_0
/*      */     //   260: bastore
/*      */     //   261: new 55	com/mysql/jdbc/Buffer
/*      */     //   264: dup
/*      */     //   265: aload 4
/*      */     //   267: invokespecial 136	com/mysql/jdbc/Buffer:<init>	([B)V
/*      */     //   270: astore 6
/*      */     //   272: aload 6
/*      */     //   274: iload_2
/*      */     //   275: iconst_1
/*      */     //   276: iadd
/*      */     //   277: invokevirtual 137	com/mysql/jdbc/Buffer:setBufLength	(I)V
/*      */     //   280: aload_0
/*      */     //   281: getfield 48	com/mysql/jdbc/MysqlIO:traceProtocol	Z
/*      */     //   284: ifeq +52 -> 336
/*      */     //   287: new 121	java/lang/StringBuffer
/*      */     //   290: dup
/*      */     //   291: invokespecial 122	java/lang/StringBuffer:<init>	()V
/*      */     //   294: astore 7
/*      */     //   296: aload 7
/*      */     //   298: ldc -118
/*      */     //   300: invokestatic 85	com/mysql/jdbc/Messages:getString	(Ljava/lang/String;)Ljava/lang/String;
/*      */     //   303: invokevirtual 124	java/lang/StringBuffer:append	(Ljava/lang/String;)Ljava/lang/StringBuffer;
/*      */     //   306: pop
/*      */     //   307: aload 7
/*      */     //   309: aload 6
/*      */     //   311: iload_2
/*      */     //   312: invokestatic 139	com/mysql/jdbc/MysqlIO:getPacketDumpToLog	(Lcom/mysql/jdbc/Buffer;I)Ljava/lang/String;
/*      */     //   315: invokevirtual 124	java/lang/StringBuffer:append	(Ljava/lang/String;)Ljava/lang/StringBuffer;
/*      */     //   318: pop
/*      */     //   319: aload_0
/*      */     //   320: getfield 50	com/mysql/jdbc/MysqlIO:connection	Lcom/mysql/jdbc/Connection;
/*      */     //   323: invokevirtual 66	com/mysql/jdbc/Connection:getLog	()Lcom/mysql/jdbc/log/Log;
/*      */     //   326: aload 7
/*      */     //   328: invokevirtual 128	java/lang/StringBuffer:toString	()Ljava/lang/String;
/*      */     //   331: invokeinterface 129 2 0
/*      */     //   336: aload_0
/*      */     //   337: getfield 49	com/mysql/jdbc/MysqlIO:enablePacketDebug	Z
/*      */     //   340: ifeq +16 -> 356
/*      */     //   343: aload_0
/*      */     //   344: iconst_0
/*      */     //   345: iconst_0
/*      */     //   346: iconst_0
/*      */     //   347: aload_0
/*      */     //   348: getfield 17	com/mysql/jdbc/MysqlIO:packetHeaderBuf	[B
/*      */     //   351: aload 6
/*      */     //   353: invokespecial 140	com/mysql/jdbc/MysqlIO:enqueuePacketForDebugging	(ZZI[BLcom/mysql/jdbc/Buffer;)V
/*      */     //   356: aload 6
/*      */     //   358: areturn
/*      */     //   359: astore_1
/*      */     //   360: new 91	com/mysql/jdbc/CommunicationsException
/*      */     //   363: dup
/*      */     //   364: aload_0
/*      */     //   365: getfield 50	com/mysql/jdbc/MysqlIO:connection	Lcom/mysql/jdbc/Connection;
/*      */     //   368: aload_0
/*      */     //   369: getfield 47	com/mysql/jdbc/MysqlIO:lastPacketSentTimeMs	J
/*      */     //   372: aload_1
/*      */     //   373: invokespecial 92	com/mysql/jdbc/CommunicationsException:<init>	(Lcom/mysql/jdbc/Connection;JLjava/lang/Exception;)V
/*      */     //   376: athrow
/*      */     //   377: astore_1
/*      */     //   378: aload_0
/*      */     //   379: getfield 50	com/mysql/jdbc/MysqlIO:connection	Lcom/mysql/jdbc/Connection;
/*      */     //   382: iconst_0
/*      */     //   383: iconst_0
/*      */     //   384: iconst_1
/*      */     //   385: aload_1
/*      */     //   386: invokevirtual 133	com/mysql/jdbc/Connection:realClose	(ZZZLjava/lang/Throwable;)V
/*      */     //   389: aload_1
/*      */     //   390: athrow
/*      */     //   391: astore 8
/*      */     //   393: aload_1
/*      */     //   394: athrow
/*      */     // Line number table:
/*      */     //   Java source line #573	-> byte code offset #0
/*      */     //   Java source line #576	-> byte code offset #15
/*      */     //   Java source line #577	-> byte code offset #20
/*      */     //   Java source line #578	-> byte code offset #24
/*      */     //   Java source line #581	-> byte code offset #37
/*      */     //   Java source line #585	-> byte code offset #76
/*      */     //   Java source line #586	-> byte code offset #83
/*      */     //   Java source line #588	-> byte code offset #91
/*      */     //   Java source line #589	-> byte code offset #101
/*      */     //   Java source line #590	-> byte code offset #107
/*      */     //   Java source line #591	-> byte code offset #117
/*      */     //   Java source line #594	-> byte code offset #130
/*      */     //   Java source line #597	-> byte code offset #146
/*      */     //   Java source line #599	-> byte code offset #153
/*      */     //   Java source line #600	-> byte code offset #160
/*      */     //   Java source line #601	-> byte code offset #174
/*      */     //   Java source line #604	-> byte code offset #182
/*      */     //   Java source line #607	-> byte code offset #187
/*      */     //   Java source line #610	-> byte code offset #192
/*      */     //   Java source line #611	-> byte code offset #199
/*      */     //   Java source line #614	-> byte code offset #213
/*      */     //   Java source line #615	-> byte code offset #219
/*      */     //   Java source line #619	-> byte code offset #256
/*      */     //   Java source line #621	-> byte code offset #261
/*      */     //   Java source line #622	-> byte code offset #272
/*      */     //   Java source line #624	-> byte code offset #280
/*      */     //   Java source line #625	-> byte code offset #287
/*      */     //   Java source line #627	-> byte code offset #296
/*      */     //   Java source line #628	-> byte code offset #307
/*      */     //   Java source line #631	-> byte code offset #319
/*      */     //   Java source line #634	-> byte code offset #336
/*      */     //   Java source line #635	-> byte code offset #343
/*      */     //   Java source line #639	-> byte code offset #356
/*      */     //   Java source line #640	-> byte code offset #359
/*      */     //   Java source line #641	-> byte code offset #360
/*      */     //   Java source line #643	-> byte code offset #377
/*      */     //   Java source line #645	-> byte code offset #378
/*      */     //   Java source line #647	-> byte code offset #389
/*      */     // Local variable table:
/*      */     //   start	length	slot	name	signature
/*      */     //   0	395	0	this	MysqlIO
/*      */     //   14	2	1	lengthRead	int
/*      */     //   359	14	1	ioEx	IOException
/*      */     //   377	17	1	oom	OutOfMemoryError
/*      */     //   75	237	2	packetLength	int
/*      */     //   90	48	3	traceMessageBuf	StringBuffer
/*      */     //   152	37	3	multiPacketSeq	byte
/*      */     //   197	69	4	buffer	byte[]
/*      */     //   211	34	5	numBytesRead	int
/*      */     //   270	87	6	packet	Buffer
/*      */     //   294	33	7	traceMessageBuf	StringBuffer
/*      */     //   391	1	8	localObject	Object
/*      */     // Exception table:
/*      */     //   from	to	target	type
/*      */     //   0	358	359	java/io/IOException
/*      */     //   0	358	377	java/lang/OutOfMemoryError
/*      */     //   378	389	391	finally
/*      */     //   391	393	391	finally
/*      */   }
/*      */   
/*      */   /* Error */
/*      */   private final Buffer reuseAndReadPacket(Buffer reuse)
/*      */     throws SQLException
/*      */   {
/*      */     // Byte code:
/*      */     //   0: aload_1
/*      */     //   1: iconst_0
/*      */     //   2: invokevirtual 421	com/mysql/jdbc/Buffer:setWasMultiPacket	(Z)V
/*      */     //   5: aload_0
/*      */     //   6: aload_0
/*      */     //   7: getfield 9	com/mysql/jdbc/MysqlIO:mysqlInput	Ljava/io/InputStream;
/*      */     //   10: aload_0
/*      */     //   11: getfield 17	com/mysql/jdbc/MysqlIO:packetHeaderBuf	[B
/*      */     //   14: iconst_0
/*      */     //   15: iconst_4
/*      */     //   16: invokespecial 117	com/mysql/jdbc/MysqlIO:readFully	(Ljava/io/InputStream;[BII)I
/*      */     //   19: istore_2
/*      */     //   20: iload_2
/*      */     //   21: iconst_4
/*      */     //   22: if_icmpge +21 -> 43
/*      */     //   25: aload_0
/*      */     //   26: invokevirtual 118	com/mysql/jdbc/MysqlIO:forceClose	()V
/*      */     //   29: new 90	java/io/IOException
/*      */     //   32: dup
/*      */     //   33: ldc_w 422
/*      */     //   36: invokestatic 85	com/mysql/jdbc/Messages:getString	(Ljava/lang/String;)Ljava/lang/String;
/*      */     //   39: invokespecial 120	java/io/IOException:<init>	(Ljava/lang/String;)V
/*      */     //   42: athrow
/*      */     //   43: aload_0
/*      */     //   44: getfield 17	com/mysql/jdbc/MysqlIO:packetHeaderBuf	[B
/*      */     //   47: iconst_0
/*      */     //   48: baload
/*      */     //   49: sipush 255
/*      */     //   52: iand
/*      */     //   53: aload_0
/*      */     //   54: getfield 17	com/mysql/jdbc/MysqlIO:packetHeaderBuf	[B
/*      */     //   57: iconst_1
/*      */     //   58: baload
/*      */     //   59: sipush 255
/*      */     //   62: iand
/*      */     //   63: bipush 8
/*      */     //   65: ishl
/*      */     //   66: iadd
/*      */     //   67: aload_0
/*      */     //   68: getfield 17	com/mysql/jdbc/MysqlIO:packetHeaderBuf	[B
/*      */     //   71: iconst_2
/*      */     //   72: baload
/*      */     //   73: sipush 255
/*      */     //   76: iand
/*      */     //   77: bipush 16
/*      */     //   79: ishl
/*      */     //   80: iadd
/*      */     //   81: istore_3
/*      */     //   82: aload_0
/*      */     //   83: getfield 48	com/mysql/jdbc/MysqlIO:traceProtocol	Z
/*      */     //   86: ifeq +74 -> 160
/*      */     //   89: new 121	java/lang/StringBuffer
/*      */     //   92: dup
/*      */     //   93: invokespecial 122	java/lang/StringBuffer:<init>	()V
/*      */     //   96: astore 4
/*      */     //   98: aload 4
/*      */     //   100: ldc_w 423
/*      */     //   103: invokestatic 85	com/mysql/jdbc/Messages:getString	(Ljava/lang/String;)Ljava/lang/String;
/*      */     //   106: invokevirtual 124	java/lang/StringBuffer:append	(Ljava/lang/String;)Ljava/lang/StringBuffer;
/*      */     //   109: pop
/*      */     //   110: aload 4
/*      */     //   112: iload_3
/*      */     //   113: invokevirtual 125	java/lang/StringBuffer:append	(I)Ljava/lang/StringBuffer;
/*      */     //   116: pop
/*      */     //   117: aload 4
/*      */     //   119: ldc_w 424
/*      */     //   122: invokestatic 85	com/mysql/jdbc/Messages:getString	(Ljava/lang/String;)Ljava/lang/String;
/*      */     //   125: invokevirtual 124	java/lang/StringBuffer:append	(Ljava/lang/String;)Ljava/lang/StringBuffer;
/*      */     //   128: pop
/*      */     //   129: aload 4
/*      */     //   131: aload_0
/*      */     //   132: getfield 17	com/mysql/jdbc/MysqlIO:packetHeaderBuf	[B
/*      */     //   135: iconst_4
/*      */     //   136: invokestatic 127	com/mysql/jdbc/StringUtils:dumpAsHex	([BI)Ljava/lang/String;
/*      */     //   139: invokevirtual 124	java/lang/StringBuffer:append	(Ljava/lang/String;)Ljava/lang/StringBuffer;
/*      */     //   142: pop
/*      */     //   143: aload_0
/*      */     //   144: getfield 50	com/mysql/jdbc/MysqlIO:connection	Lcom/mysql/jdbc/Connection;
/*      */     //   147: invokevirtual 66	com/mysql/jdbc/Connection:getLog	()Lcom/mysql/jdbc/log/Log;
/*      */     //   150: aload 4
/*      */     //   152: invokevirtual 128	java/lang/StringBuffer:toString	()Ljava/lang/String;
/*      */     //   155: invokeinterface 129 2 0
/*      */     //   160: aload_0
/*      */     //   161: getfield 17	com/mysql/jdbc/MysqlIO:packetHeaderBuf	[B
/*      */     //   164: iconst_3
/*      */     //   165: baload
/*      */     //   166: istore 4
/*      */     //   168: aload_0
/*      */     //   169: getfield 3	com/mysql/jdbc/MysqlIO:packetSequenceReset	Z
/*      */     //   172: ifne +26 -> 198
/*      */     //   175: aload_0
/*      */     //   176: getfield 49	com/mysql/jdbc/MysqlIO:enablePacketDebug	Z
/*      */     //   179: ifeq +24 -> 203
/*      */     //   182: aload_0
/*      */     //   183: getfield 34	com/mysql/jdbc/MysqlIO:checkPacketSequence	Z
/*      */     //   186: ifeq +17 -> 203
/*      */     //   189: aload_0
/*      */     //   190: iload 4
/*      */     //   192: invokespecial 130	com/mysql/jdbc/MysqlIO:checkPacketSequencing	(B)V
/*      */     //   195: goto +8 -> 203
/*      */     //   198: aload_0
/*      */     //   199: iconst_0
/*      */     //   200: putfield 3	com/mysql/jdbc/MysqlIO:packetSequenceReset	Z
/*      */     //   203: aload_0
/*      */     //   204: iload 4
/*      */     //   206: putfield 33	com/mysql/jdbc/MysqlIO:readPacketSequence	B
/*      */     //   209: aload_1
/*      */     //   210: iconst_0
/*      */     //   211: invokevirtual 219	com/mysql/jdbc/Buffer:setPosition	(I)V
/*      */     //   214: aload_1
/*      */     //   215: invokevirtual 148	com/mysql/jdbc/Buffer:getByteBuffer	()[B
/*      */     //   218: arraylength
/*      */     //   219: iload_3
/*      */     //   220: if_icmpgt +12 -> 232
/*      */     //   223: aload_1
/*      */     //   224: iload_3
/*      */     //   225: iconst_1
/*      */     //   226: iadd
/*      */     //   227: newarray <illegal type>
/*      */     //   229: invokevirtual 425	com/mysql/jdbc/Buffer:setByteBuffer	([B)V
/*      */     //   232: aload_1
/*      */     //   233: iload_3
/*      */     //   234: invokevirtual 137	com/mysql/jdbc/Buffer:setBufLength	(I)V
/*      */     //   237: aload_0
/*      */     //   238: aload_0
/*      */     //   239: getfield 9	com/mysql/jdbc/MysqlIO:mysqlInput	Ljava/io/InputStream;
/*      */     //   242: aload_1
/*      */     //   243: invokevirtual 148	com/mysql/jdbc/Buffer:getByteBuffer	()[B
/*      */     //   246: iconst_0
/*      */     //   247: iload_3
/*      */     //   248: invokespecial 117	com/mysql/jdbc/MysqlIO:readFully	(Ljava/io/InputStream;[BII)I
/*      */     //   251: istore 5
/*      */     //   253: iload 5
/*      */     //   255: iload_3
/*      */     //   256: if_icmpeq +40 -> 296
/*      */     //   259: new 90	java/io/IOException
/*      */     //   262: dup
/*      */     //   263: new 121	java/lang/StringBuffer
/*      */     //   266: dup
/*      */     //   267: invokespecial 122	java/lang/StringBuffer:<init>	()V
/*      */     //   270: ldc -122
/*      */     //   272: invokevirtual 124	java/lang/StringBuffer:append	(Ljava/lang/String;)Ljava/lang/StringBuffer;
/*      */     //   275: iload_3
/*      */     //   276: invokevirtual 125	java/lang/StringBuffer:append	(I)Ljava/lang/StringBuffer;
/*      */     //   279: ldc -121
/*      */     //   281: invokevirtual 124	java/lang/StringBuffer:append	(Ljava/lang/String;)Ljava/lang/StringBuffer;
/*      */     //   284: iload 5
/*      */     //   286: invokevirtual 125	java/lang/StringBuffer:append	(I)Ljava/lang/StringBuffer;
/*      */     //   289: invokevirtual 128	java/lang/StringBuffer:toString	()Ljava/lang/String;
/*      */     //   292: invokespecial 120	java/io/IOException:<init>	(Ljava/lang/String;)V
/*      */     //   295: athrow
/*      */     //   296: aload_0
/*      */     //   297: getfield 48	com/mysql/jdbc/MysqlIO:traceProtocol	Z
/*      */     //   300: ifeq +52 -> 352
/*      */     //   303: new 121	java/lang/StringBuffer
/*      */     //   306: dup
/*      */     //   307: invokespecial 122	java/lang/StringBuffer:<init>	()V
/*      */     //   310: astore 6
/*      */     //   312: aload 6
/*      */     //   314: ldc_w 426
/*      */     //   317: invokestatic 85	com/mysql/jdbc/Messages:getString	(Ljava/lang/String;)Ljava/lang/String;
/*      */     //   320: invokevirtual 124	java/lang/StringBuffer:append	(Ljava/lang/String;)Ljava/lang/StringBuffer;
/*      */     //   323: pop
/*      */     //   324: aload 6
/*      */     //   326: aload_1
/*      */     //   327: iload_3
/*      */     //   328: invokestatic 139	com/mysql/jdbc/MysqlIO:getPacketDumpToLog	(Lcom/mysql/jdbc/Buffer;I)Ljava/lang/String;
/*      */     //   331: invokevirtual 124	java/lang/StringBuffer:append	(Ljava/lang/String;)Ljava/lang/StringBuffer;
/*      */     //   334: pop
/*      */     //   335: aload_0
/*      */     //   336: getfield 50	com/mysql/jdbc/MysqlIO:connection	Lcom/mysql/jdbc/Connection;
/*      */     //   339: invokevirtual 66	com/mysql/jdbc/Connection:getLog	()Lcom/mysql/jdbc/log/Log;
/*      */     //   342: aload 6
/*      */     //   344: invokevirtual 128	java/lang/StringBuffer:toString	()Ljava/lang/String;
/*      */     //   347: invokeinterface 129 2 0
/*      */     //   352: aload_0
/*      */     //   353: getfield 49	com/mysql/jdbc/MysqlIO:enablePacketDebug	Z
/*      */     //   356: ifeq +15 -> 371
/*      */     //   359: aload_0
/*      */     //   360: iconst_0
/*      */     //   361: iconst_1
/*      */     //   362: iconst_0
/*      */     //   363: aload_0
/*      */     //   364: getfield 17	com/mysql/jdbc/MysqlIO:packetHeaderBuf	[B
/*      */     //   367: aload_1
/*      */     //   368: invokespecial 140	com/mysql/jdbc/MysqlIO:enqueuePacketForDebugging	(ZZI[BLcom/mysql/jdbc/Buffer;)V
/*      */     //   371: iconst_0
/*      */     //   372: istore 6
/*      */     //   374: iload_3
/*      */     //   375: aload_0
/*      */     //   376: getfield 39	com/mysql/jdbc/MysqlIO:maxThreeBytes	I
/*      */     //   379: if_icmpne +550 -> 929
/*      */     //   382: aload_1
/*      */     //   383: aload_0
/*      */     //   384: getfield 39	com/mysql/jdbc/MysqlIO:maxThreeBytes	I
/*      */     //   387: invokevirtual 219	com/mysql/jdbc/Buffer:setPosition	(I)V
/*      */     //   390: iload_3
/*      */     //   391: istore 7
/*      */     //   393: iconst_1
/*      */     //   394: istore 6
/*      */     //   396: aload_0
/*      */     //   397: aload_0
/*      */     //   398: getfield 9	com/mysql/jdbc/MysqlIO:mysqlInput	Ljava/io/InputStream;
/*      */     //   401: aload_0
/*      */     //   402: getfield 17	com/mysql/jdbc/MysqlIO:packetHeaderBuf	[B
/*      */     //   405: iconst_0
/*      */     //   406: iconst_4
/*      */     //   407: invokespecial 117	com/mysql/jdbc/MysqlIO:readFully	(Ljava/io/InputStream;[BII)I
/*      */     //   410: istore_2
/*      */     //   411: iload_2
/*      */     //   412: iconst_4
/*      */     //   413: if_icmpge +21 -> 434
/*      */     //   416: aload_0
/*      */     //   417: invokevirtual 118	com/mysql/jdbc/MysqlIO:forceClose	()V
/*      */     //   420: new 90	java/io/IOException
/*      */     //   423: dup
/*      */     //   424: ldc_w 427
/*      */     //   427: invokestatic 85	com/mysql/jdbc/Messages:getString	(Ljava/lang/String;)Ljava/lang/String;
/*      */     //   430: invokespecial 120	java/io/IOException:<init>	(Ljava/lang/String;)V
/*      */     //   433: athrow
/*      */     //   434: aload_0
/*      */     //   435: getfield 17	com/mysql/jdbc/MysqlIO:packetHeaderBuf	[B
/*      */     //   438: iconst_0
/*      */     //   439: baload
/*      */     //   440: sipush 255
/*      */     //   443: iand
/*      */     //   444: aload_0
/*      */     //   445: getfield 17	com/mysql/jdbc/MysqlIO:packetHeaderBuf	[B
/*      */     //   448: iconst_1
/*      */     //   449: baload
/*      */     //   450: sipush 255
/*      */     //   453: iand
/*      */     //   454: bipush 8
/*      */     //   456: ishl
/*      */     //   457: iadd
/*      */     //   458: aload_0
/*      */     //   459: getfield 17	com/mysql/jdbc/MysqlIO:packetHeaderBuf	[B
/*      */     //   462: iconst_2
/*      */     //   463: baload
/*      */     //   464: sipush 255
/*      */     //   467: iand
/*      */     //   468: bipush 16
/*      */     //   470: ishl
/*      */     //   471: iadd
/*      */     //   472: istore_3
/*      */     //   473: new 55	com/mysql/jdbc/Buffer
/*      */     //   476: dup
/*      */     //   477: iload_3
/*      */     //   478: invokespecial 56	com/mysql/jdbc/Buffer:<init>	(I)V
/*      */     //   481: astore 8
/*      */     //   483: iconst_1
/*      */     //   484: istore 9
/*      */     //   486: iload 9
/*      */     //   488: ifne +83 -> 571
/*      */     //   491: aload_0
/*      */     //   492: aload_0
/*      */     //   493: getfield 9	com/mysql/jdbc/MysqlIO:mysqlInput	Ljava/io/InputStream;
/*      */     //   496: aload_0
/*      */     //   497: getfield 17	com/mysql/jdbc/MysqlIO:packetHeaderBuf	[B
/*      */     //   500: iconst_0
/*      */     //   501: iconst_4
/*      */     //   502: invokespecial 117	com/mysql/jdbc/MysqlIO:readFully	(Ljava/io/InputStream;[BII)I
/*      */     //   505: istore_2
/*      */     //   506: iload_2
/*      */     //   507: iconst_4
/*      */     //   508: if_icmpge +21 -> 529
/*      */     //   511: aload_0
/*      */     //   512: invokevirtual 118	com/mysql/jdbc/MysqlIO:forceClose	()V
/*      */     //   515: new 90	java/io/IOException
/*      */     //   518: dup
/*      */     //   519: ldc_w 428
/*      */     //   522: invokestatic 85	com/mysql/jdbc/Messages:getString	(Ljava/lang/String;)Ljava/lang/String;
/*      */     //   525: invokespecial 120	java/io/IOException:<init>	(Ljava/lang/String;)V
/*      */     //   528: athrow
/*      */     //   529: aload_0
/*      */     //   530: getfield 17	com/mysql/jdbc/MysqlIO:packetHeaderBuf	[B
/*      */     //   533: iconst_0
/*      */     //   534: baload
/*      */     //   535: sipush 255
/*      */     //   538: iand
/*      */     //   539: aload_0
/*      */     //   540: getfield 17	com/mysql/jdbc/MysqlIO:packetHeaderBuf	[B
/*      */     //   543: iconst_1
/*      */     //   544: baload
/*      */     //   545: sipush 255
/*      */     //   548: iand
/*      */     //   549: bipush 8
/*      */     //   551: ishl
/*      */     //   552: iadd
/*      */     //   553: aload_0
/*      */     //   554: getfield 17	com/mysql/jdbc/MysqlIO:packetHeaderBuf	[B
/*      */     //   557: iconst_2
/*      */     //   558: baload
/*      */     //   559: sipush 255
/*      */     //   562: iand
/*      */     //   563: bipush 16
/*      */     //   565: ishl
/*      */     //   566: iadd
/*      */     //   567: istore_3
/*      */     //   568: goto +6 -> 574
/*      */     //   571: iconst_0
/*      */     //   572: istore 9
/*      */     //   574: aload_0
/*      */     //   575: getfield 30	com/mysql/jdbc/MysqlIO:useNewLargePackets	Z
/*      */     //   578: ifne +15 -> 593
/*      */     //   581: iload_3
/*      */     //   582: iconst_1
/*      */     //   583: if_icmpne +10 -> 593
/*      */     //   586: aload_0
/*      */     //   587: invokevirtual 284	com/mysql/jdbc/MysqlIO:clearInputStream	()V
/*      */     //   590: goto +329 -> 919
/*      */     //   593: iload_3
/*      */     //   594: aload_0
/*      */     //   595: getfield 39	com/mysql/jdbc/MysqlIO:maxThreeBytes	I
/*      */     //   598: if_icmpge +162 -> 760
/*      */     //   601: aload_0
/*      */     //   602: getfield 17	com/mysql/jdbc/MysqlIO:packetHeaderBuf	[B
/*      */     //   605: iconst_3
/*      */     //   606: baload
/*      */     //   607: istore 10
/*      */     //   609: iload 10
/*      */     //   611: iload 4
/*      */     //   613: iconst_1
/*      */     //   614: iadd
/*      */     //   615: if_icmpeq +17 -> 632
/*      */     //   618: new 90	java/io/IOException
/*      */     //   621: dup
/*      */     //   622: ldc_w 429
/*      */     //   625: invokestatic 85	com/mysql/jdbc/Messages:getString	(Ljava/lang/String;)Ljava/lang/String;
/*      */     //   628: invokespecial 120	java/io/IOException:<init>	(Ljava/lang/String;)V
/*      */     //   631: athrow
/*      */     //   632: iload 10
/*      */     //   634: istore 4
/*      */     //   636: aload 8
/*      */     //   638: iconst_0
/*      */     //   639: invokevirtual 219	com/mysql/jdbc/Buffer:setPosition	(I)V
/*      */     //   642: aload 8
/*      */     //   644: iload_3
/*      */     //   645: invokevirtual 137	com/mysql/jdbc/Buffer:setBufLength	(I)V
/*      */     //   648: aload 8
/*      */     //   650: invokevirtual 148	com/mysql/jdbc/Buffer:getByteBuffer	()[B
/*      */     //   653: astore 11
/*      */     //   655: iload_3
/*      */     //   656: istore 12
/*      */     //   658: aload_0
/*      */     //   659: aload_0
/*      */     //   660: getfield 9	com/mysql/jdbc/MysqlIO:mysqlInput	Ljava/io/InputStream;
/*      */     //   663: aload 11
/*      */     //   665: iconst_0
/*      */     //   666: iload_3
/*      */     //   667: invokespecial 117	com/mysql/jdbc/MysqlIO:readFully	(Ljava/io/InputStream;[BII)I
/*      */     //   670: istore 13
/*      */     //   672: iload 13
/*      */     //   674: iload 12
/*      */     //   676: if_icmpeq +65 -> 741
/*      */     //   679: new 91	com/mysql/jdbc/CommunicationsException
/*      */     //   682: dup
/*      */     //   683: aload_0
/*      */     //   684: getfield 50	com/mysql/jdbc/MysqlIO:connection	Lcom/mysql/jdbc/Connection;
/*      */     //   687: aload_0
/*      */     //   688: getfield 47	com/mysql/jdbc/MysqlIO:lastPacketSentTimeMs	J
/*      */     //   691: new 121	java/lang/StringBuffer
/*      */     //   694: dup
/*      */     //   695: invokespecial 122	java/lang/StringBuffer:<init>	()V
/*      */     //   698: ldc_w 430
/*      */     //   701: invokestatic 85	com/mysql/jdbc/Messages:getString	(Ljava/lang/String;)Ljava/lang/String;
/*      */     //   704: invokevirtual 124	java/lang/StringBuffer:append	(Ljava/lang/String;)Ljava/lang/StringBuffer;
/*      */     //   707: iload 12
/*      */     //   709: invokevirtual 125	java/lang/StringBuffer:append	(I)Ljava/lang/StringBuffer;
/*      */     //   712: ldc_w 431
/*      */     //   715: invokestatic 85	com/mysql/jdbc/Messages:getString	(Ljava/lang/String;)Ljava/lang/String;
/*      */     //   718: invokevirtual 124	java/lang/StringBuffer:append	(Ljava/lang/String;)Ljava/lang/StringBuffer;
/*      */     //   721: iload 13
/*      */     //   723: invokevirtual 125	java/lang/StringBuffer:append	(I)Ljava/lang/StringBuffer;
/*      */     //   726: ldc -47
/*      */     //   728: invokevirtual 124	java/lang/StringBuffer:append	(Ljava/lang/String;)Ljava/lang/StringBuffer;
/*      */     //   731: invokevirtual 128	java/lang/StringBuffer:toString	()Ljava/lang/String;
/*      */     //   734: invokestatic 278	com/mysql/jdbc/SQLError:createSQLException	(Ljava/lang/String;)Ljava/sql/SQLException;
/*      */     //   737: invokespecial 92	com/mysql/jdbc/CommunicationsException:<init>	(Lcom/mysql/jdbc/Connection;JLjava/lang/Exception;)V
/*      */     //   740: athrow
/*      */     //   741: aload_1
/*      */     //   742: aload 11
/*      */     //   744: iconst_0
/*      */     //   745: iload 12
/*      */     //   747: invokevirtual 388	com/mysql/jdbc/Buffer:writeBytesNoNull	([BII)V
/*      */     //   750: iload 7
/*      */     //   752: iload 12
/*      */     //   754: iadd
/*      */     //   755: istore 7
/*      */     //   757: goto +162 -> 919
/*      */     //   760: aload_0
/*      */     //   761: getfield 17	com/mysql/jdbc/MysqlIO:packetHeaderBuf	[B
/*      */     //   764: iconst_3
/*      */     //   765: baload
/*      */     //   766: istore 10
/*      */     //   768: iload 10
/*      */     //   770: iload 4
/*      */     //   772: iconst_1
/*      */     //   773: iadd
/*      */     //   774: if_icmpeq +17 -> 791
/*      */     //   777: new 90	java/io/IOException
/*      */     //   780: dup
/*      */     //   781: ldc_w 432
/*      */     //   784: invokestatic 85	com/mysql/jdbc/Messages:getString	(Ljava/lang/String;)Ljava/lang/String;
/*      */     //   787: invokespecial 120	java/io/IOException:<init>	(Ljava/lang/String;)V
/*      */     //   790: athrow
/*      */     //   791: iload 10
/*      */     //   793: istore 4
/*      */     //   795: aload 8
/*      */     //   797: iconst_0
/*      */     //   798: invokevirtual 219	com/mysql/jdbc/Buffer:setPosition	(I)V
/*      */     //   801: aload 8
/*      */     //   803: iload_3
/*      */     //   804: invokevirtual 137	com/mysql/jdbc/Buffer:setBufLength	(I)V
/*      */     //   807: aload 8
/*      */     //   809: invokevirtual 148	com/mysql/jdbc/Buffer:getByteBuffer	()[B
/*      */     //   812: astore 11
/*      */     //   814: iload_3
/*      */     //   815: istore 12
/*      */     //   817: aload_0
/*      */     //   818: aload_0
/*      */     //   819: getfield 9	com/mysql/jdbc/MysqlIO:mysqlInput	Ljava/io/InputStream;
/*      */     //   822: aload 11
/*      */     //   824: iconst_0
/*      */     //   825: iload_3
/*      */     //   826: invokespecial 117	com/mysql/jdbc/MysqlIO:readFully	(Ljava/io/InputStream;[BII)I
/*      */     //   829: istore 13
/*      */     //   831: iload 13
/*      */     //   833: iload 12
/*      */     //   835: if_icmpeq +65 -> 900
/*      */     //   838: new 91	com/mysql/jdbc/CommunicationsException
/*      */     //   841: dup
/*      */     //   842: aload_0
/*      */     //   843: getfield 50	com/mysql/jdbc/MysqlIO:connection	Lcom/mysql/jdbc/Connection;
/*      */     //   846: aload_0
/*      */     //   847: getfield 47	com/mysql/jdbc/MysqlIO:lastPacketSentTimeMs	J
/*      */     //   850: new 121	java/lang/StringBuffer
/*      */     //   853: dup
/*      */     //   854: invokespecial 122	java/lang/StringBuffer:<init>	()V
/*      */     //   857: ldc_w 433
/*      */     //   860: invokestatic 85	com/mysql/jdbc/Messages:getString	(Ljava/lang/String;)Ljava/lang/String;
/*      */     //   863: invokevirtual 124	java/lang/StringBuffer:append	(Ljava/lang/String;)Ljava/lang/StringBuffer;
/*      */     //   866: iload 12
/*      */     //   868: invokevirtual 125	java/lang/StringBuffer:append	(I)Ljava/lang/StringBuffer;
/*      */     //   871: ldc_w 434
/*      */     //   874: invokestatic 85	com/mysql/jdbc/Messages:getString	(Ljava/lang/String;)Ljava/lang/String;
/*      */     //   877: invokevirtual 124	java/lang/StringBuffer:append	(Ljava/lang/String;)Ljava/lang/StringBuffer;
/*      */     //   880: iload 13
/*      */     //   882: invokevirtual 125	java/lang/StringBuffer:append	(I)Ljava/lang/StringBuffer;
/*      */     //   885: ldc -47
/*      */     //   887: invokevirtual 124	java/lang/StringBuffer:append	(Ljava/lang/String;)Ljava/lang/StringBuffer;
/*      */     //   890: invokevirtual 128	java/lang/StringBuffer:toString	()Ljava/lang/String;
/*      */     //   893: invokestatic 278	com/mysql/jdbc/SQLError:createSQLException	(Ljava/lang/String;)Ljava/sql/SQLException;
/*      */     //   896: invokespecial 92	com/mysql/jdbc/CommunicationsException:<init>	(Lcom/mysql/jdbc/Connection;JLjava/lang/Exception;)V
/*      */     //   899: athrow
/*      */     //   900: aload_1
/*      */     //   901: aload 11
/*      */     //   903: iconst_0
/*      */     //   904: iload 12
/*      */     //   906: invokevirtual 388	com/mysql/jdbc/Buffer:writeBytesNoNull	([BII)V
/*      */     //   909: iload 7
/*      */     //   911: iload 12
/*      */     //   913: iadd
/*      */     //   914: istore 7
/*      */     //   916: goto -430 -> 486
/*      */     //   919: aload_1
/*      */     //   920: iconst_0
/*      */     //   921: invokevirtual 219	com/mysql/jdbc/Buffer:setPosition	(I)V
/*      */     //   924: aload_1
/*      */     //   925: iconst_1
/*      */     //   926: invokevirtual 421	com/mysql/jdbc/Buffer:setWasMultiPacket	(Z)V
/*      */     //   929: iload 6
/*      */     //   931: ifne +10 -> 941
/*      */     //   934: aload_1
/*      */     //   935: invokevirtual 148	com/mysql/jdbc/Buffer:getByteBuffer	()[B
/*      */     //   938: iload_3
/*      */     //   939: iconst_0
/*      */     //   940: bastore
/*      */     //   941: aload_1
/*      */     //   942: areturn
/*      */     //   943: astore_2
/*      */     //   944: new 91	com/mysql/jdbc/CommunicationsException
/*      */     //   947: dup
/*      */     //   948: aload_0
/*      */     //   949: getfield 50	com/mysql/jdbc/MysqlIO:connection	Lcom/mysql/jdbc/Connection;
/*      */     //   952: aload_0
/*      */     //   953: getfield 47	com/mysql/jdbc/MysqlIO:lastPacketSentTimeMs	J
/*      */     //   956: aload_2
/*      */     //   957: invokespecial 92	com/mysql/jdbc/CommunicationsException:<init>	(Lcom/mysql/jdbc/Connection;JLjava/lang/Exception;)V
/*      */     //   960: athrow
/*      */     //   961: astore_2
/*      */     //   962: aload_0
/*      */     //   963: invokevirtual 284	com/mysql/jdbc/MysqlIO:clearInputStream	()V
/*      */     //   966: jsr +14 -> 980
/*      */     //   969: goto +30 -> 999
/*      */     //   972: astore 14
/*      */     //   974: jsr +6 -> 980
/*      */     //   977: aload 14
/*      */     //   979: athrow
/*      */     //   980: astore 15
/*      */     //   982: aload_0
/*      */     //   983: getfield 50	com/mysql/jdbc/MysqlIO:connection	Lcom/mysql/jdbc/Connection;
/*      */     //   986: iconst_0
/*      */     //   987: iconst_0
/*      */     //   988: iconst_1
/*      */     //   989: aload_2
/*      */     //   990: invokevirtual 133	com/mysql/jdbc/Connection:realClose	(ZZZLjava/lang/Throwable;)V
/*      */     //   993: aload_2
/*      */     //   994: athrow
/*      */     //   995: astore 16
/*      */     //   997: aload_2
/*      */     //   998: athrow
/*      */     //   999: goto +0 -> 999
/*      */     // Line number table:
/*      */     //   Java source line #2409	-> byte code offset #0
/*      */     //   Java source line #2411	-> byte code offset #5
/*      */     //   Java source line #2414	-> byte code offset #20
/*      */     //   Java source line #2415	-> byte code offset #25
/*      */     //   Java source line #2416	-> byte code offset #29
/*      */     //   Java source line #2419	-> byte code offset #43
/*      */     //   Java source line #2423	-> byte code offset #82
/*      */     //   Java source line #2424	-> byte code offset #89
/*      */     //   Java source line #2426	-> byte code offset #98
/*      */     //   Java source line #2427	-> byte code offset #110
/*      */     //   Java source line #2428	-> byte code offset #117
/*      */     //   Java source line #2429	-> byte code offset #129
/*      */     //   Java source line #2432	-> byte code offset #143
/*      */     //   Java source line #2435	-> byte code offset #160
/*      */     //   Java source line #2437	-> byte code offset #168
/*      */     //   Java source line #2438	-> byte code offset #175
/*      */     //   Java source line #2439	-> byte code offset #189
/*      */     //   Java source line #2442	-> byte code offset #198
/*      */     //   Java source line #2445	-> byte code offset #203
/*      */     //   Java source line #2448	-> byte code offset #209
/*      */     //   Java source line #2456	-> byte code offset #214
/*      */     //   Java source line #2457	-> byte code offset #223
/*      */     //   Java source line #2461	-> byte code offset #232
/*      */     //   Java source line #2464	-> byte code offset #237
/*      */     //   Java source line #2467	-> byte code offset #253
/*      */     //   Java source line #2468	-> byte code offset #259
/*      */     //   Java source line #2472	-> byte code offset #296
/*      */     //   Java source line #2473	-> byte code offset #303
/*      */     //   Java source line #2475	-> byte code offset #312
/*      */     //   Java source line #2476	-> byte code offset #324
/*      */     //   Java source line #2479	-> byte code offset #335
/*      */     //   Java source line #2482	-> byte code offset #352
/*      */     //   Java source line #2483	-> byte code offset #359
/*      */     //   Java source line #2487	-> byte code offset #371
/*      */     //   Java source line #2489	-> byte code offset #374
/*      */     //   Java source line #2490	-> byte code offset #382
/*      */     //   Java source line #2492	-> byte code offset #390
/*      */     //   Java source line #2495	-> byte code offset #393
/*      */     //   Java source line #2497	-> byte code offset #396
/*      */     //   Java source line #2500	-> byte code offset #411
/*      */     //   Java source line #2501	-> byte code offset #416
/*      */     //   Java source line #2502	-> byte code offset #420
/*      */     //   Java source line #2505	-> byte code offset #434
/*      */     //   Java source line #2509	-> byte code offset #473
/*      */     //   Java source line #2510	-> byte code offset #483
/*      */     //   Java source line #2513	-> byte code offset #486
/*      */     //   Java source line #2514	-> byte code offset #491
/*      */     //   Java source line #2517	-> byte code offset #506
/*      */     //   Java source line #2518	-> byte code offset #511
/*      */     //   Java source line #2519	-> byte code offset #515
/*      */     //   Java source line #2523	-> byte code offset #529
/*      */     //   Java source line #2527	-> byte code offset #571
/*      */     //   Java source line #2530	-> byte code offset #574
/*      */     //   Java source line #2531	-> byte code offset #586
/*      */     //   Java source line #2533	-> byte code offset #590
/*      */     //   Java source line #2534	-> byte code offset #593
/*      */     //   Java source line #2535	-> byte code offset #601
/*      */     //   Java source line #2537	-> byte code offset #609
/*      */     //   Java source line #2538	-> byte code offset #618
/*      */     //   Java source line #2542	-> byte code offset #632
/*      */     //   Java source line #2545	-> byte code offset #636
/*      */     //   Java source line #2548	-> byte code offset #642
/*      */     //   Java source line #2551	-> byte code offset #648
/*      */     //   Java source line #2552	-> byte code offset #655
/*      */     //   Java source line #2554	-> byte code offset #658
/*      */     //   Java source line #2557	-> byte code offset #672
/*      */     //   Java source line #2558	-> byte code offset #679
/*      */     //   Java source line #2568	-> byte code offset #741
/*      */     //   Java source line #2570	-> byte code offset #750
/*      */     //   Java source line #2572	-> byte code offset #757
/*      */     //   Java source line #2575	-> byte code offset #760
/*      */     //   Java source line #2577	-> byte code offset #768
/*      */     //   Java source line #2578	-> byte code offset #777
/*      */     //   Java source line #2582	-> byte code offset #791
/*      */     //   Java source line #2585	-> byte code offset #795
/*      */     //   Java source line #2588	-> byte code offset #801
/*      */     //   Java source line #2591	-> byte code offset #807
/*      */     //   Java source line #2592	-> byte code offset #814
/*      */     //   Java source line #2594	-> byte code offset #817
/*      */     //   Java source line #2597	-> byte code offset #831
/*      */     //   Java source line #2598	-> byte code offset #838
/*      */     //   Java source line #2607	-> byte code offset #900
/*      */     //   Java source line #2609	-> byte code offset #909
/*      */     //   Java source line #2612	-> byte code offset #919
/*      */     //   Java source line #2613	-> byte code offset #924
/*      */     //   Java source line #2616	-> byte code offset #929
/*      */     //   Java source line #2617	-> byte code offset #934
/*      */     //   Java source line #2620	-> byte code offset #941
/*      */     //   Java source line #2621	-> byte code offset #943
/*      */     //   Java source line #2622	-> byte code offset #944
/*      */     //   Java source line #2624	-> byte code offset #961
/*      */     //   Java source line #2627	-> byte code offset #962
/*      */     //   Java source line #2628	-> byte code offset #966
/*      */     //   Java source line #2634	-> byte code offset #969
/*      */     //   Java source line #2629	-> byte code offset #972
/*      */     //   Java source line #2630	-> byte code offset #982
/*      */     //   Java source line #2632	-> byte code offset #993
/*      */     //   Java source line #2637	-> byte code offset #999
/*      */     // Local variable table:
/*      */     //   start	length	slot	name	signature
/*      */     //   0	1002	0	this	MysqlIO
/*      */     //   0	1002	1	reuse	Buffer
/*      */     //   19	488	2	lengthRead	int
/*      */     //   943	14	2	ioEx	IOException
/*      */     //   961	37	2	oom	OutOfMemoryError
/*      */     //   81	858	3	packetLength	int
/*      */     //   96	55	4	traceMessageBuf	StringBuffer
/*      */     //   166	628	4	multiPacketSeq	byte
/*      */     //   251	34	5	numBytesRead	int
/*      */     //   310	33	6	traceMessageBuf	StringBuffer
/*      */     //   372	558	6	isMultiPacket	boolean
/*      */     //   391	524	7	packetEndPoint	int
/*      */     //   481	327	8	multiPacket	Buffer
/*      */     //   484	89	9	firstMultiPkt	boolean
/*      */     //   607	26	10	newPacketSeq	byte
/*      */     //   766	26	10	newPacketSeq	byte
/*      */     //   653	90	11	byteBuf	byte[]
/*      */     //   812	90	11	byteBuf	byte[]
/*      */     //   656	97	12	lengthToWrite	int
/*      */     //   815	97	12	lengthToWrite	int
/*      */     //   670	52	13	bytesRead	int
/*      */     //   829	52	13	bytesRead	int
/*      */     //   972	6	14	localObject1	Object
/*      */     //   980	1	15	localObject2	Object
/*      */     //   995	1	16	localObject3	Object
/*      */     // Exception table:
/*      */     //   from	to	target	type
/*      */     //   0	942	943	java/io/IOException
/*      */     //   0	942	961	java/lang/OutOfMemoryError
/*      */     //   962	969	972	finally
/*      */     //   972	977	972	finally
/*      */     //   982	993	995	finally
/*      */     //   995	997	995	finally
/*      */   }
/*      */ }


/* Location:              E:\java\java学习\hutubill\lib\all.jar!\com\mysql\jdbc\MysqlIO.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version:       0.7.1
 */
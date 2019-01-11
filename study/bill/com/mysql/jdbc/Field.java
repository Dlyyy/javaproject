/*     */ package com.mysql.jdbc;
/*     */ 
/*     */ import java.io.UnsupportedEncodingException;
/*     */ import java.sql.DatabaseMetaData;
/*     */ import java.sql.ResultSet;
/*     */ import java.sql.SQLException;
/*     */ import java.sql.Statement;
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
/*     */ public class Field
/*     */ {
/*     */   private static final int AUTO_INCREMENT_FLAG = 512;
/*     */   private static final int NO_CHARSET_INFO = -1;
/*     */   private byte[] buffer;
/*  45 */   private int charsetIndex = 0;
/*     */   
/*  47 */   private String charsetName = null;
/*     */   
/*     */   private int colDecimals;
/*     */   
/*     */   private short colFlag;
/*     */   
/*  53 */   private String collationName = null;
/*     */   
/*  55 */   private Connection connection = null;
/*     */   
/*  57 */   private String databaseName = null;
/*     */   
/*  59 */   private int databaseNameLength = -1;
/*     */   
/*     */ 
/*  62 */   private int databaseNameStart = -1;
/*     */   
/*  64 */   private int defaultValueLength = -1;
/*     */   
/*     */ 
/*  67 */   private int defaultValueStart = -1;
/*     */   
/*  69 */   private String fullName = null;
/*     */   
/*  71 */   private String fullOriginalName = null;
/*     */   
/*  73 */   private boolean isImplicitTempTable = false;
/*     */   
/*     */   private long length;
/*     */   
/*  77 */   private int mysqlType = -1;
/*     */   
/*     */   private String name;
/*     */   
/*     */   private int nameLength;
/*     */   
/*     */   private int nameStart;
/*     */   
/*  85 */   private String originalColumnName = null;
/*     */   
/*  87 */   private int originalColumnNameLength = -1;
/*     */   
/*     */ 
/*  90 */   private int originalColumnNameStart = -1;
/*     */   
/*  92 */   private String originalTableName = null;
/*     */   
/*  94 */   private int originalTableNameLength = -1;
/*     */   
/*     */ 
/*  97 */   private int originalTableNameStart = -1;
/*     */   
/*  99 */   private int precisionAdjustFactor = 0;
/*     */   
/* 101 */   private int sqlType = -1;
/*     */   
/*     */   private String tableName;
/*     */   
/*     */   private int tableNameLength;
/*     */   
/*     */   private int tableNameStart;
/*     */   
/* 109 */   private boolean useOldNameMetadata = false;
/*     */   
/*     */ 
/*     */ 
/*     */   private boolean isSingleBit;
/*     */   
/*     */ 
/*     */ 
/*     */   private int maxBytesPerChar;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   Field(Connection conn, byte[] buffer, int databaseNameStart, int databaseNameLength, int tableNameStart, int tableNameLength, int originalTableNameStart, int originalTableNameLength, int nameStart, int nameLength, int originalColumnNameStart, int originalColumnNameLength, long length, int mysqlType, short colFlag, int colDecimals, int defaultValueStart, int defaultValueLength, int charsetIndex)
/*     */     throws SQLException
/*     */   {
/* 125 */     this.connection = conn;
/* 126 */     this.buffer = buffer;
/* 127 */     this.nameStart = nameStart;
/* 128 */     this.nameLength = nameLength;
/* 129 */     this.tableNameStart = tableNameStart;
/* 130 */     this.tableNameLength = tableNameLength;
/* 131 */     this.length = length;
/* 132 */     this.colFlag = colFlag;
/* 133 */     this.colDecimals = colDecimals;
/* 134 */     this.mysqlType = mysqlType;
/*     */     
/*     */ 
/* 137 */     this.databaseNameStart = databaseNameStart;
/* 138 */     this.databaseNameLength = databaseNameLength;
/*     */     
/* 140 */     this.originalTableNameStart = originalTableNameStart;
/* 141 */     this.originalTableNameLength = originalTableNameLength;
/*     */     
/* 143 */     this.originalColumnNameStart = originalColumnNameStart;
/* 144 */     this.originalColumnNameLength = originalColumnNameLength;
/*     */     
/* 146 */     this.defaultValueStart = defaultValueStart;
/* 147 */     this.defaultValueLength = defaultValueLength;
/*     */     
/*     */ 
/*     */ 
/* 151 */     this.charsetIndex = charsetIndex;
/*     */     
/*     */ 
/*     */ 
/* 155 */     this.sqlType = MysqlDefs.mysqlToJavaType(this.mysqlType);
/*     */     
/* 157 */     checkForImplicitTemporaryTable();
/*     */     
/*     */ 
/* 160 */     if (this.mysqlType == 252) {
/* 161 */       boolean isFromFunction = this.originalTableNameLength == 0;
/*     */       
/* 163 */       if (((this.connection != null) && (this.connection.getBlobsAreStrings())) || ((this.connection.getFunctionsNeverReturnBlobs()) && (isFromFunction)))
/*     */       {
/* 165 */         this.sqlType = 12;
/* 166 */         this.mysqlType = 15;
/* 167 */       } else if ((this.charsetIndex == 63) || (!this.connection.versionMeetsMinimum(4, 1, 0)))
/*     */       {
/* 169 */         setBlobTypeBasedOnLength();
/* 170 */         this.sqlType = MysqlDefs.mysqlToJavaType(this.mysqlType);
/*     */       }
/*     */       else {
/* 173 */         this.mysqlType = 253;
/* 174 */         this.sqlType = -1;
/*     */       }
/*     */     }
/*     */     
/* 178 */     if ((this.sqlType == -6) && (this.length == 1L) && (this.connection.getTinyInt1isBit()))
/*     */     {
/*     */ 
/* 181 */       if (conn.getTinyInt1isBit()) {
/* 182 */         if (conn.getTransformedBitIsBoolean()) {
/* 183 */           this.sqlType = 16;
/*     */         } else {
/* 185 */           this.sqlType = -7;
/*     */         }
/*     */       }
/*     */     }
/*     */     
/*     */ 
/* 191 */     if ((!isNativeNumericType()) && (!isNativeDateTimeType())) {
/* 192 */       this.charsetName = this.connection.getCharsetNameForIndex(this.charsetIndex);
/*     */       
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 199 */       boolean isBinary = isBinary();
/*     */       
/* 201 */       if ((this.connection.versionMeetsMinimum(4, 1, 0)) && (this.mysqlType == 253) && (isBinary) && (this.charsetIndex == 63))
/*     */       {
/*     */ 
/*     */ 
/* 205 */         if (isOpaqueBinary()) {
/* 206 */           this.sqlType = -3;
/*     */         }
/*     */       }
/*     */       
/* 210 */       if ((this.connection.versionMeetsMinimum(4, 1, 0)) && (this.mysqlType == 254) && (isBinary) && (this.charsetIndex == 63))
/*     */       {
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 220 */         if ((isOpaqueBinary()) && (!this.connection.getBlobsAreStrings())) {
/* 221 */           this.sqlType = -2;
/*     */         }
/*     */       }
/*     */       
/*     */ 
/*     */ 
/* 227 */       if (this.mysqlType == 16) {
/* 228 */         this.isSingleBit = (this.length == 0L);
/*     */         
/* 230 */         if ((this.connection != null) && ((this.connection.versionMeetsMinimum(5, 0, 21)) || (this.connection.versionMeetsMinimum(5, 1, 10))) && (this.length == 1L))
/*     */         {
/* 232 */           this.isSingleBit = true;
/*     */         }
/*     */         
/* 235 */         if (this.isSingleBit) {
/* 236 */           this.sqlType = -7;
/*     */         } else {
/* 238 */           this.sqlType = -3;
/* 239 */           this.colFlag = ((short)(this.colFlag | 0x80));
/* 240 */           this.colFlag = ((short)(this.colFlag | 0x10));
/* 241 */           isBinary = true;
/*     */         }
/*     */       }
/*     */       
/*     */ 
/*     */ 
/*     */ 
/* 248 */       if ((this.sqlType == -4) && (!isBinary)) {
/* 249 */         this.sqlType = -1;
/* 250 */       } else if ((this.sqlType == -3) && (!isBinary)) {
/* 251 */         this.sqlType = 12;
/*     */       }
/*     */     } else {
/* 254 */       this.charsetName = "US-ASCII";
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/* 260 */     if (!isUnsigned()) {
/* 261 */       switch (this.mysqlType) {
/*     */       case 0: 
/*     */       case 246: 
/* 264 */         this.precisionAdjustFactor = -1;
/*     */         
/* 266 */         break;
/*     */       case 4: 
/*     */       case 5: 
/* 269 */         this.precisionAdjustFactor = 1;
/*     */       
/*     */       }
/*     */       
/*     */     } else {
/* 274 */       switch (this.mysqlType) {
/*     */       case 4: 
/*     */       case 5: 
/* 277 */         this.precisionAdjustFactor = 1;
/*     */       }
/*     */       
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   Field(Connection conn, byte[] buffer, int nameStart, int nameLength, int tableNameStart, int tableNameLength, int length, int mysqlType, short colFlag, int colDecimals)
/*     */     throws SQLException
/*     */   {
/* 290 */     this(conn, buffer, -1, -1, tableNameStart, tableNameLength, -1, -1, nameStart, nameLength, -1, -1, length, mysqlType, colFlag, colDecimals, -1, -1, -1);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   Field(String tableName, String columnName, int jdbcType, int length)
/*     */   {
/* 299 */     this.tableName = tableName;
/* 300 */     this.name = columnName;
/* 301 */     this.length = length;
/* 302 */     this.sqlType = jdbcType;
/* 303 */     this.colFlag = 0;
/* 304 */     this.colDecimals = 0;
/*     */   }
/*     */   
/*     */   private void checkForImplicitTemporaryTable() {
/* 308 */     this.isImplicitTempTable = ((this.tableNameLength > 5) && (this.buffer[this.tableNameStart] == 35) && (this.buffer[(this.tableNameStart + 1)] == 115) && (this.buffer[(this.tableNameStart + 2)] == 113) && (this.buffer[(this.tableNameStart + 3)] == 108) && (this.buffer[(this.tableNameStart + 4)] == 95));
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
/*     */   public String getCharacterSet()
/*     */     throws SQLException
/*     */   {
/* 322 */     return this.charsetName;
/*     */   }
/*     */   
/*     */   public synchronized String getCollation() throws SQLException {
/* 326 */     if ((this.collationName == null) && 
/* 327 */       (this.connection != null) && 
/* 328 */       (this.connection.versionMeetsMinimum(4, 1, 0))) {
/* 329 */       if (this.connection.getUseDynamicCharsetInfo()) {
/* 330 */         DatabaseMetaData dbmd = this.connection.getMetaData();
/*     */         
/*     */ 
/* 333 */         String quotedIdStr = dbmd.getIdentifierQuoteString();
/*     */         
/* 335 */         if (" ".equals(quotedIdStr)) {
/* 336 */           quotedIdStr = "";
/*     */         }
/*     */         
/* 339 */         String csCatalogName = getDatabaseName();
/* 340 */         String csTableName = getOriginalTableName();
/* 341 */         String csColumnName = getOriginalName();
/*     */         
/* 343 */         if ((csCatalogName != null) && (csCatalogName.length() != 0) && (csTableName != null) && (csTableName.length() != 0) && (csColumnName != null) && (csColumnName.length() != 0))
/*     */         {
/*     */ 
/*     */ 
/* 347 */           StringBuffer queryBuf = new StringBuffer(csCatalogName.length() + csTableName.length() + 28);
/*     */           
/*     */ 
/* 350 */           queryBuf.append("SHOW FULL COLUMNS FROM ");
/* 351 */           queryBuf.append(quotedIdStr);
/* 352 */           queryBuf.append(csCatalogName);
/* 353 */           queryBuf.append(quotedIdStr);
/* 354 */           queryBuf.append(".");
/* 355 */           queryBuf.append(quotedIdStr);
/* 356 */           queryBuf.append(csTableName);
/* 357 */           queryBuf.append(quotedIdStr);
/*     */           
/* 359 */           Statement collationStmt = null;
/* 360 */           ResultSet collationRs = null;
/*     */           try
/*     */           {
/* 363 */             collationStmt = this.connection.createStatement();
/*     */             
/* 365 */             collationRs = collationStmt.executeQuery(queryBuf.toString());
/*     */             
/*     */ 
/* 368 */             while (collationRs.next()) {
/* 369 */               if (csColumnName.equals(collationRs.getString("Field")))
/*     */               {
/* 371 */                 this.collationName = collationRs.getString("Collation");
/*     */               }
/*     */               
/*     */             }
/*     */           }
/*     */           finally
/*     */           {
/* 378 */             if (collationRs != null) {
/* 379 */               collationRs.close();
/* 380 */               collationRs = null;
/*     */             }
/*     */             
/* 383 */             if (collationStmt != null) {
/* 384 */               collationStmt.close();
/* 385 */               collationStmt = null;
/*     */             }
/*     */           }
/*     */         }
/*     */       } else {
/* 390 */         this.collationName = CharsetMapping.INDEX_TO_COLLATION[this.charsetIndex];
/*     */       }
/*     */     }
/*     */     
/*     */ 
/*     */ 
/* 396 */     return this.collationName;
/*     */   }
/*     */   
/*     */   public String getColumnLabel() throws SQLException {
/* 400 */     return getName();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public String getDatabaseName()
/*     */     throws SQLException
/*     */   {
/* 409 */     if ((this.databaseName == null) && (this.databaseNameStart != -1) && (this.databaseNameLength != -1))
/*     */     {
/* 411 */       this.databaseName = getStringFromBytes(this.databaseNameStart, this.databaseNameLength);
/*     */     }
/*     */     
/*     */ 
/* 415 */     return this.databaseName;
/*     */   }
/*     */   
/*     */   int getDecimals() {
/* 419 */     return this.colDecimals;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public String getFullName()
/*     */     throws SQLException
/*     */   {
/* 428 */     if (this.fullName == null) {
/* 429 */       StringBuffer fullNameBuf = new StringBuffer(getTableName().length() + 1 + getName().length());
/*     */       
/* 431 */       fullNameBuf.append(this.tableName);
/*     */       
/*     */ 
/* 434 */       fullNameBuf.append('.');
/* 435 */       fullNameBuf.append(this.name);
/* 436 */       this.fullName = fullNameBuf.toString();
/* 437 */       fullNameBuf = null;
/*     */     }
/*     */     
/* 440 */     return this.fullName;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public String getFullOriginalName()
/*     */     throws SQLException
/*     */   {
/* 449 */     getOriginalName();
/*     */     
/* 451 */     if (this.originalColumnName == null) {
/* 452 */       return null;
/*     */     }
/*     */     
/* 455 */     if (this.fullName == null) {
/* 456 */       StringBuffer fullOriginalNameBuf = new StringBuffer(getOriginalTableName().length() + 1 + getOriginalName().length());
/*     */       
/*     */ 
/* 459 */       fullOriginalNameBuf.append(this.originalTableName);
/*     */       
/*     */ 
/* 462 */       fullOriginalNameBuf.append('.');
/* 463 */       fullOriginalNameBuf.append(this.originalColumnName);
/* 464 */       this.fullOriginalName = fullOriginalNameBuf.toString();
/* 465 */       fullOriginalNameBuf = null;
/*     */     }
/*     */     
/* 468 */     return this.fullOriginalName;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public long getLength()
/*     */   {
/* 477 */     return this.length;
/*     */   }
/*     */   
/*     */   public synchronized int getMaxBytesPerCharacter() throws SQLException {
/* 481 */     if (this.maxBytesPerChar == 0) {
/* 482 */       this.maxBytesPerChar = this.connection.getMaxBytesPerChar(getCharacterSet());
/*     */     }
/*     */     
/* 485 */     return this.maxBytesPerChar;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public int getMysqlType()
/*     */   {
/* 494 */     return this.mysqlType;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public String getName()
/*     */     throws SQLException
/*     */   {
/* 503 */     if (this.name == null) {
/* 504 */       this.name = getStringFromBytes(this.nameStart, this.nameLength);
/*     */     }
/*     */     
/* 507 */     return this.name;
/*     */   }
/*     */   
/*     */   public String getNameNoAliases() throws SQLException {
/* 511 */     if (this.useOldNameMetadata) {
/* 512 */       return getName();
/*     */     }
/*     */     
/* 515 */     if ((this.connection != null) && (this.connection.versionMeetsMinimum(4, 1, 0)))
/*     */     {
/* 517 */       return getOriginalName();
/*     */     }
/*     */     
/* 520 */     return getName();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public String getOriginalName()
/*     */     throws SQLException
/*     */   {
/* 529 */     if ((this.originalColumnName == null) && (this.originalColumnNameStart != -1) && (this.originalColumnNameLength != -1))
/*     */     {
/*     */ 
/* 532 */       this.originalColumnName = getStringFromBytes(this.originalColumnNameStart, this.originalColumnNameLength);
/*     */     }
/*     */     
/*     */ 
/* 536 */     return this.originalColumnName;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public String getOriginalTableName()
/*     */     throws SQLException
/*     */   {
/* 545 */     if ((this.originalTableName == null) && (this.originalTableNameStart != -1) && (this.originalTableNameLength != -1))
/*     */     {
/*     */ 
/* 548 */       this.originalTableName = getStringFromBytes(this.originalTableNameStart, this.originalTableNameLength);
/*     */     }
/*     */     
/*     */ 
/* 552 */     return this.originalTableName;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public int getPrecisionAdjustFactor()
/*     */   {
/* 564 */     return this.precisionAdjustFactor;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public int getSQLType()
/*     */   {
/* 573 */     return this.sqlType;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   private String getStringFromBytes(int stringStart, int stringLength)
/*     */     throws SQLException
/*     */   {
/* 582 */     if ((stringStart == -1) || (stringLength == -1)) {
/* 583 */       return null;
/*     */     }
/*     */     
/* 586 */     String stringVal = null;
/*     */     
/* 588 */     if (this.connection != null) {
/* 589 */       if (this.connection.getUseUnicode()) {
/* 590 */         String encoding = this.connection.getCharacterSetMetadata();
/*     */         
/* 592 */         if (encoding == null) {
/* 593 */           encoding = this.connection.getEncoding();
/*     */         }
/*     */         
/* 596 */         if (encoding != null) {
/* 597 */           SingleByteCharsetConverter converter = null;
/*     */           
/* 599 */           if (this.connection != null) {
/* 600 */             converter = this.connection.getCharsetConverter(encoding);
/*     */           }
/*     */           
/*     */ 
/* 604 */           if (converter != null) {
/* 605 */             stringVal = converter.toString(this.buffer, stringStart, stringLength);
/*     */           }
/*     */           else
/*     */           {
/* 609 */             byte[] stringBytes = new byte[stringLength];
/*     */             
/* 611 */             int endIndex = stringStart + stringLength;
/* 612 */             int pos = 0;
/*     */             
/* 614 */             for (int i = stringStart; i < endIndex; i++) {
/* 615 */               stringBytes[(pos++)] = this.buffer[i];
/*     */             }
/*     */             try
/*     */             {
/* 619 */               stringVal = new String(stringBytes, encoding);
/*     */             } catch (UnsupportedEncodingException ue) {
/* 621 */               throw new RuntimeException(Messages.getString("Field.12") + encoding + Messages.getString("Field.13"));
/*     */             }
/*     */             
/*     */           }
/*     */         }
/*     */         else
/*     */         {
/* 628 */           stringVal = StringUtils.toAsciiString(this.buffer, stringStart, stringLength);
/*     */         }
/*     */       }
/*     */       else
/*     */       {
/* 633 */         stringVal = StringUtils.toAsciiString(this.buffer, stringStart, stringLength);
/*     */       }
/*     */       
/*     */     }
/*     */     else {
/* 638 */       stringVal = StringUtils.toAsciiString(this.buffer, stringStart, stringLength);
/*     */     }
/*     */     
/*     */ 
/* 642 */     return stringVal;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public String getTable()
/*     */     throws SQLException
/*     */   {
/* 651 */     return getTableName();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public String getTableName()
/*     */     throws SQLException
/*     */   {
/* 660 */     if (this.tableName == null) {
/* 661 */       this.tableName = getStringFromBytes(this.tableNameStart, this.tableNameLength);
/*     */     }
/*     */     
/*     */ 
/* 665 */     return this.tableName;
/*     */   }
/*     */   
/*     */   public String getTableNameNoAliases() throws SQLException {
/* 669 */     if (this.connection.versionMeetsMinimum(4, 1, 0)) {
/* 670 */       return getOriginalTableName();
/*     */     }
/*     */     
/* 673 */     return getTableName();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean isAutoIncrement()
/*     */   {
/* 682 */     return (this.colFlag & 0x200) > 0;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean isBinary()
/*     */   {
/* 691 */     return (this.colFlag & 0x80) > 0;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean isBlob()
/*     */   {
/* 700 */     return (this.colFlag & 0x10) > 0;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private boolean isImplicitTemporaryTable()
/*     */   {
/* 709 */     return this.isImplicitTempTable;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean isMultipleKey()
/*     */   {
/* 718 */     return (this.colFlag & 0x8) > 0;
/*     */   }
/*     */   
/*     */   boolean isNotNull() {
/* 722 */     return (this.colFlag & 0x1) > 0;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   boolean isOpaqueBinary()
/*     */     throws SQLException
/*     */   {
/* 732 */     if ((this.charsetIndex == 63) && (isBinary()) && ((getMysqlType() == 254) || (getMysqlType() == 253)))
/*     */     {
/*     */ 
/*     */ 
/* 736 */       if ((this.originalTableNameLength == 0) && (this.connection != null) && (!this.connection.versionMeetsMinimum(5, 0, 25)))
/*     */       {
/* 738 */         return false;
/*     */       }
/*     */       
/*     */ 
/*     */ 
/*     */ 
/* 744 */       return !isImplicitTemporaryTable();
/*     */     }
/*     */     
/* 747 */     return (this.connection.versionMeetsMinimum(4, 1, 0)) && ("binary".equalsIgnoreCase(getCharacterSet()));
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean isPrimaryKey()
/*     */   {
/* 758 */     return (this.colFlag & 0x2) > 0;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   boolean isReadOnly()
/*     */     throws SQLException
/*     */   {
/* 768 */     if (this.connection.versionMeetsMinimum(4, 1, 0)) {
/* 769 */       String orgColumnName = getOriginalName();
/* 770 */       String orgTableName = getOriginalTableName();
/*     */       
/* 772 */       return (orgColumnName == null) || (orgColumnName.length() <= 0) || (orgTableName == null) || (orgTableName.length() <= 0);
/*     */     }
/*     */     
/*     */ 
/* 776 */     return false;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean isUniqueKey()
/*     */   {
/* 785 */     return (this.colFlag & 0x4) > 0;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean isUnsigned()
/*     */   {
/* 794 */     return (this.colFlag & 0x20) > 0;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean isZeroFill()
/*     */   {
/* 803 */     return (this.colFlag & 0x40) > 0;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private void setBlobTypeBasedOnLength()
/*     */   {
/* 812 */     if (this.length == 255L) {
/* 813 */       this.mysqlType = 249;
/* 814 */     } else if (this.length == 65535L) {
/* 815 */       this.mysqlType = 252;
/* 816 */     } else if (this.length == 16777215L) {
/* 817 */       this.mysqlType = 250;
/* 818 */     } else if (this.length == 4294967295L) {
/* 819 */       this.mysqlType = 251;
/*     */     }
/*     */   }
/*     */   
/*     */   private boolean isNativeNumericType() {
/* 824 */     return ((this.mysqlType >= 1) && (this.mysqlType <= 5)) || (this.mysqlType == 8) || (this.mysqlType == 13);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   private boolean isNativeDateTimeType()
/*     */   {
/* 831 */     return (this.mysqlType == 10) || (this.mysqlType == 14) || (this.mysqlType == 12) || (this.mysqlType == 11) || (this.mysqlType == 7);
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
/*     */   public void setConnection(Connection conn)
/*     */   {
/* 845 */     this.connection = conn;
/*     */     
/* 847 */     this.charsetName = this.connection.getEncoding();
/*     */   }
/*     */   
/*     */   void setMysqlType(int type) {
/* 851 */     this.mysqlType = type;
/* 852 */     this.sqlType = MysqlDefs.mysqlToJavaType(this.mysqlType);
/*     */   }
/*     */   
/*     */   protected void setUseOldNameMetadata(boolean useOldNameMetadata) {
/* 856 */     this.useOldNameMetadata = useOldNameMetadata;
/*     */   }
/*     */   
/*     */   public String toString() {
/*     */     try {
/* 861 */       StringBuffer asString = new StringBuffer(128);
/* 862 */       asString.append(super.toString());
/*     */       
/* 864 */       asString.append("\n  catalog: ");
/* 865 */       asString.append(getDatabaseName());
/* 866 */       asString.append("\n  table name: ");
/* 867 */       asString.append(getTableName());
/* 868 */       asString.append("\n  original table name: ");
/* 869 */       asString.append(getOriginalTableName());
/* 870 */       asString.append("\n  column name: ");
/* 871 */       asString.append(getName());
/* 872 */       asString.append("\n  original column name: ");
/* 873 */       asString.append(getOriginalName());
/* 874 */       asString.append("\n  MySQL data type: ");
/* 875 */       asString.append(getMysqlType());
/* 876 */       asString.append("(");
/* 877 */       asString.append(MysqlDefs.typeToName(getMysqlType()));
/* 878 */       asString.append(")");
/*     */       
/* 880 */       if (this.buffer != null) {
/* 881 */         asString.append("\n\nData as received from server:\n\n");
/* 882 */         asString.append(StringUtils.dumpAsHex(this.buffer, this.buffer.length));
/*     */       }
/*     */       
/*     */ 
/* 886 */       return asString.toString();
/*     */     } catch (Throwable t) {}
/* 888 */     return super.toString();
/*     */   }
/*     */   
/*     */   protected boolean isSingleBit()
/*     */   {
/* 893 */     return this.isSingleBit;
/*     */   }
/*     */ }


/* Location:              E:\java\java学习\hutubill\lib\all.jar!\com\mysql\jdbc\Field.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version:       0.7.1
 */
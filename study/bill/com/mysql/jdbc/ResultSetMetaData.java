/*     */ package com.mysql.jdbc;
/*     */ 
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class ResultSetMetaData
/*     */   implements java.sql.ResultSetMetaData
/*     */ {
/*     */   Field[] fields;
/*     */   
/*     */   private static int clampedGetLength(Field f)
/*     */   {
/*  45 */     long fieldLength = f.getLength();
/*     */     
/*  47 */     if (fieldLength > 2147483647L) {
/*  48 */       fieldLength = 2147483647L;
/*     */     }
/*     */     
/*  51 */     return (int)fieldLength;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private static final boolean isDecimalType(int type)
/*     */   {
/*  63 */     switch (type) {
/*     */     case -7: 
/*     */     case -6: 
/*     */     case -5: 
/*     */     case 2: 
/*     */     case 3: 
/*     */     case 4: 
/*     */     case 5: 
/*     */     case 6: 
/*     */     case 7: 
/*     */     case 8: 
/*  74 */       return true;
/*     */     }
/*     */     
/*  77 */     return false;
/*     */   }
/*     */   
/*     */ 
/*  81 */   boolean useOldAliasBehavior = false;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public ResultSetMetaData(Field[] fields, boolean useOldAliasBehavior)
/*     */   {
/*  90 */     this.fields = fields;
/*  91 */     this.useOldAliasBehavior = useOldAliasBehavior;
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
/*     */   public String getCatalogName(int column)
/*     */     throws SQLException
/*     */   {
/* 106 */     Field f = getField(column);
/*     */     
/* 108 */     String database = f.getDatabaseName();
/*     */     
/* 110 */     return database == null ? "" : database;
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
/*     */   public String getColumnCharacterEncoding(int column)
/*     */     throws SQLException
/*     */   {
/* 127 */     String mysqlName = getColumnCharacterSet(column);
/*     */     
/* 129 */     String javaName = null;
/*     */     
/* 131 */     if (mysqlName != null) {
/* 132 */       javaName = CharsetMapping.getJavaEncodingForMysqlEncoding(mysqlName, null);
/*     */     }
/*     */     
/*     */ 
/* 136 */     return javaName;
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
/*     */   public String getColumnCharacterSet(int column)
/*     */     throws SQLException
/*     */   {
/* 151 */     return getField(column).getCharacterSet();
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public String getColumnClassName(int column)
/*     */     throws SQLException
/*     */   {
/* 177 */     Field f = getField(column);
/*     */     
/* 179 */     return getClassNameForJavaType(f.getSQLType(), f.isUnsigned(), f.getMysqlType(), (f.isBinary()) || (f.isBlob()), f.isOpaqueBinary());
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
/*     */   public int getColumnCount()
/*     */     throws SQLException
/*     */   {
/* 195 */     return this.fields.length;
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
/*     */   public int getColumnDisplaySize(int column)
/*     */     throws SQLException
/*     */   {
/* 210 */     Field f = getField(column);
/*     */     
/* 212 */     int lengthInBytes = clampedGetLength(f);
/*     */     
/* 214 */     return lengthInBytes / f.getMaxBytesPerCharacter();
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
/*     */   public String getColumnLabel(int column)
/*     */     throws SQLException
/*     */   {
/* 229 */     if (this.useOldAliasBehavior) {
/* 230 */       return getColumnName(column);
/*     */     }
/*     */     
/* 233 */     return getField(column).getColumnLabel();
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
/*     */   public String getColumnName(int column)
/*     */     throws SQLException
/*     */   {
/* 248 */     if (this.useOldAliasBehavior) {
/* 249 */       return getField(column).getName();
/*     */     }
/*     */     
/* 252 */     String name = getField(column).getNameNoAliases();
/*     */     
/* 254 */     if ((name != null) && (name.length() == 0)) {
/* 255 */       return getField(column).getName();
/*     */     }
/*     */     
/* 258 */     return name;
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
/*     */   public int getColumnType(int column)
/*     */     throws SQLException
/*     */   {
/* 275 */     return getField(column).getSQLType();
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
/*     */   public String getColumnTypeName(int column)
/*     */     throws SQLException
/*     */   {
/* 290 */     Field field = getField(column);
/*     */     
/* 292 */     int mysqlType = field.getMysqlType();
/* 293 */     int jdbcType = field.getSQLType();
/*     */     
/* 295 */     switch (mysqlType) {
/*     */     case 16: 
/* 297 */       return "BIT";
/*     */     case 0: 
/*     */     case 246: 
/* 300 */       return field.isUnsigned() ? "DECIMAL UNSIGNED" : "DECIMAL";
/*     */     
/*     */     case 1: 
/* 303 */       return field.isUnsigned() ? "TINYINT UNSIGNED" : "TINYINT";
/*     */     
/*     */     case 2: 
/* 306 */       return field.isUnsigned() ? "SMALLINT UNSIGNED" : "SMALLINT";
/*     */     
/*     */     case 3: 
/* 309 */       return field.isUnsigned() ? "INT UNSIGNED" : "INT";
/*     */     
/*     */     case 4: 
/* 312 */       return field.isUnsigned() ? "FLOAT UNSIGNED" : "FLOAT";
/*     */     
/*     */     case 5: 
/* 315 */       return field.isUnsigned() ? "DOUBLE UNSIGNED" : "DOUBLE";
/*     */     
/*     */     case 6: 
/* 318 */       return "NULL";
/*     */     
/*     */     case 7: 
/* 321 */       return "TIMESTAMP";
/*     */     
/*     */     case 8: 
/* 324 */       return field.isUnsigned() ? "BIGINT UNSIGNED" : "BIGINT";
/*     */     
/*     */     case 9: 
/* 327 */       return field.isUnsigned() ? "MEDIUMINT UNSIGNED" : "MEDIUMINT";
/*     */     
/*     */     case 10: 
/* 330 */       return "DATE";
/*     */     
/*     */     case 11: 
/* 333 */       return "TIME";
/*     */     
/*     */     case 12: 
/* 336 */       return "DATETIME";
/*     */     
/*     */     case 249: 
/* 339 */       return "TINYBLOB";
/*     */     
/*     */     case 250: 
/* 342 */       return "MEDIUMBLOB";
/*     */     
/*     */     case 251: 
/* 345 */       return "LONGBLOB";
/*     */     
/*     */     case 252: 
/* 348 */       if (getField(column).isBinary()) {
/* 349 */         return "BLOB";
/*     */       }
/*     */       
/* 352 */       return "TEXT";
/*     */     
/*     */     case 15: 
/* 355 */       return "VARCHAR";
/*     */     
/*     */     case 253: 
/* 358 */       if (jdbcType == -3) {
/* 359 */         return "VARBINARY";
/*     */       }
/*     */       
/* 362 */       return "VARCHAR";
/*     */     
/*     */     case 254: 
/* 365 */       if (jdbcType == -2) {
/* 366 */         return "BINARY";
/*     */       }
/*     */       
/* 369 */       return "CHAR";
/*     */     
/*     */     case 247: 
/* 372 */       return "ENUM";
/*     */     
/*     */     case 13: 
/* 375 */       return "YEAR";
/*     */     
/*     */     case 248: 
/* 378 */       return "SET";
/*     */     }
/*     */     
/* 381 */     return "UNKNOWN";
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
/*     */   protected Field getField(int columnIndex)
/*     */     throws SQLException
/*     */   {
/* 397 */     if ((columnIndex < 1) || (columnIndex > this.fields.length)) {
/* 398 */       throw SQLError.createSQLException(Messages.getString("ResultSetMetaData.46"), "S1002");
/*     */     }
/*     */     
/*     */ 
/* 402 */     return this.fields[(columnIndex - 1)];
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
/*     */   public int getPrecision(int column)
/*     */     throws SQLException
/*     */   {
/* 417 */     Field f = getField(column);
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 423 */     if (isDecimalType(f.getSQLType())) {
/* 424 */       if (f.getDecimals() > 0) {
/* 425 */         return clampedGetLength(f) - 1 + f.getPrecisionAdjustFactor();
/*     */       }
/*     */       
/* 428 */       return clampedGetLength(f) + f.getPrecisionAdjustFactor();
/*     */     }
/*     */     
/* 431 */     switch (f.getMysqlType()) {
/*     */     case 249: 
/*     */     case 250: 
/*     */     case 251: 
/*     */     case 252: 
/* 436 */       return clampedGetLength(f);
/*     */     }
/*     */     
/*     */     
/*     */ 
/*     */ 
/*     */ 
/* 443 */     return clampedGetLength(f) / f.getMaxBytesPerCharacter();
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
/*     */   public int getScale(int column)
/*     */     throws SQLException
/*     */   {
/* 460 */     Field f = getField(column);
/*     */     
/* 462 */     if (isDecimalType(f.getSQLType())) {
/* 463 */       return f.getDecimals();
/*     */     }
/*     */     
/* 466 */     return 0;
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
/*     */   public String getSchemaName(int column)
/*     */     throws SQLException
/*     */   {
/* 483 */     return "";
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
/*     */   public String getTableName(int column)
/*     */     throws SQLException
/*     */   {
/* 498 */     if (this.useOldAliasBehavior) {
/* 499 */       return getField(column).getTableName();
/*     */     }
/*     */     
/* 502 */     return getField(column).getTableNameNoAliases();
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
/*     */   public boolean isAutoIncrement(int column)
/*     */     throws SQLException
/*     */   {
/* 517 */     Field f = getField(column);
/*     */     
/* 519 */     return f.isAutoIncrement();
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
/*     */   public boolean isCaseSensitive(int column)
/*     */     throws SQLException
/*     */   {
/* 534 */     Field field = getField(column);
/*     */     
/* 536 */     int sqlType = field.getSQLType();
/*     */     
/* 538 */     switch (sqlType) {
/*     */     case -7: 
/*     */     case -6: 
/*     */     case -5: 
/*     */     case 4: 
/*     */     case 5: 
/*     */     case 6: 
/*     */     case 7: 
/*     */     case 8: 
/*     */     case 91: 
/*     */     case 92: 
/*     */     case 93: 
/* 550 */       return false;
/*     */     
/*     */ 
/*     */     case -1: 
/*     */     case 1: 
/*     */     case 12: 
/* 556 */       if (field.isBinary()) {
/* 557 */         return true;
/*     */       }
/*     */       
/* 560 */       String collationName = field.getCollation();
/*     */       
/* 562 */       return (collationName != null) && (!collationName.endsWith("_ci"));
/*     */     }
/*     */     
/* 565 */     return true;
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
/*     */   public boolean isCurrency(int column)
/*     */     throws SQLException
/*     */   {
/* 581 */     return false;
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
/*     */   public boolean isDefinitelyWritable(int column)
/*     */     throws SQLException
/*     */   {
/* 596 */     return isWritable(column);
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
/*     */   public int isNullable(int column)
/*     */     throws SQLException
/*     */   {
/* 611 */     if (!getField(column).isNotNull()) {
/* 612 */       return 1;
/*     */     }
/*     */     
/* 615 */     return 0;
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
/*     */   public boolean isReadOnly(int column)
/*     */     throws SQLException
/*     */   {
/* 630 */     return getField(column).isReadOnly();
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
/*     */ 
/*     */   public boolean isSearchable(int column)
/*     */     throws SQLException
/*     */   {
/* 649 */     return true;
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
/*     */   public boolean isSigned(int column)
/*     */     throws SQLException
/*     */   {
/* 664 */     Field f = getField(column);
/* 665 */     int sqlType = f.getSQLType();
/*     */     
/* 667 */     switch (sqlType) {
/*     */     case -6: 
/*     */     case -5: 
/*     */     case 2: 
/*     */     case 3: 
/*     */     case 4: 
/*     */     case 5: 
/*     */     case 6: 
/*     */     case 7: 
/*     */     case 8: 
/* 677 */       return !f.isUnsigned();
/*     */     
/*     */     case 91: 
/*     */     case 92: 
/*     */     case 93: 
/* 682 */       return false;
/*     */     }
/*     */     
/* 685 */     return false;
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
/*     */   public boolean isWritable(int column)
/*     */     throws SQLException
/*     */   {
/* 701 */     return !isReadOnly(column);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public String toString()
/*     */   {
/* 710 */     StringBuffer toStringBuf = new StringBuffer();
/* 711 */     toStringBuf.append(super.toString());
/* 712 */     toStringBuf.append(" - Field level information: ");
/*     */     
/* 714 */     for (int i = 0; i < this.fields.length; i++) {
/* 715 */       toStringBuf.append("\n\t");
/* 716 */       toStringBuf.append(this.fields[i].toString());
/*     */     }
/*     */     
/* 719 */     return toStringBuf.toString();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   static String getClassNameForJavaType(int javaType, boolean isUnsigned, int mysqlTypeIfKnown, boolean isBinaryOrBlob, boolean isOpaqueBinary)
/*     */   {
/* 726 */     switch (javaType) {
/*     */     case -7: 
/*     */     case 16: 
/* 729 */       return "java.lang.Boolean";
/*     */     
/*     */ 
/*     */     case -6: 
/* 733 */       if (isUnsigned) {
/* 734 */         return "java.lang.Integer";
/*     */       }
/*     */       
/* 737 */       return "java.lang.Integer";
/*     */     
/*     */ 
/*     */     case 5: 
/* 741 */       if (isUnsigned) {
/* 742 */         return "java.lang.Integer";
/*     */       }
/*     */       
/* 745 */       return "java.lang.Integer";
/*     */     
/*     */ 
/*     */     case 4: 
/* 749 */       if ((!isUnsigned) || (mysqlTypeIfKnown == 9))
/*     */       {
/* 751 */         return "java.lang.Integer";
/*     */       }
/*     */       
/* 754 */       return "java.lang.Long";
/*     */     
/*     */ 
/*     */     case -5: 
/* 758 */       if (!isUnsigned) {
/* 759 */         return "java.lang.Long";
/*     */       }
/*     */       
/* 762 */       return "java.math.BigInteger";
/*     */     
/*     */     case 2: 
/*     */     case 3: 
/* 766 */       return "java.math.BigDecimal";
/*     */     
/*     */     case 7: 
/* 769 */       return "java.lang.Float";
/*     */     
/*     */     case 6: 
/*     */     case 8: 
/* 773 */       return "java.lang.Double";
/*     */     
/*     */     case -1: 
/*     */     case 1: 
/*     */     case 12: 
/* 778 */       if (!isOpaqueBinary) {
/* 779 */         return "java.lang.String";
/*     */       }
/*     */       
/* 782 */       return "[B";
/*     */     
/*     */ 
/*     */     case -4: 
/*     */     case -3: 
/*     */     case -2: 
/* 788 */       if (mysqlTypeIfKnown == 255)
/* 789 */         return "[B";
/* 790 */       if (isBinaryOrBlob) {
/* 791 */         return "[B";
/*     */       }
/* 793 */       return "java.lang.String";
/*     */     
/*     */ 
/*     */     case 91: 
/* 797 */       return "java.sql.Date";
/*     */     
/*     */     case 92: 
/* 800 */       return "java.sql.Time";
/*     */     
/*     */     case 93: 
/* 803 */       return "java.sql.Timestamp";
/*     */     }
/*     */     
/* 806 */     return "java.lang.Object";
/*     */   }
/*     */ }


/* Location:              E:\java\java学习\hutubill\lib\all.jar!\com\mysql\jdbc\ResultSetMetaData.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version:       0.7.1
 */
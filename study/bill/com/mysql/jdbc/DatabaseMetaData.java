/*      */ package com.mysql.jdbc;
/*      */ 
/*      */ import java.io.UnsupportedEncodingException;
/*      */ import java.sql.ResultSetMetaData;
/*      */ import java.sql.SQLException;
/*      */ import java.sql.Statement;
/*      */ import java.util.ArrayList;
/*      */ import java.util.Collection;
/*      */ import java.util.Collections;
/*      */ import java.util.HashMap;
/*      */ import java.util.Iterator;
/*      */ import java.util.List;
/*      */ import java.util.Locale;
/*      */ import java.util.Map;
/*      */ import java.util.Set;
/*      */ import java.util.StringTokenizer;
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
/*      */ public class DatabaseMetaData
/*      */   implements java.sql.DatabaseMetaData
/*      */ {
/*      */   private static String mysqlKeywordsThatArentSQL92;
/*      */   private static final int DEFERRABILITY = 13;
/*      */   private static final int DELETE_RULE = 10;
/*      */   private static final int FK_NAME = 11;
/*      */   private static final int FKCOLUMN_NAME = 7;
/*      */   private static final int FKTABLE_CAT = 4;
/*      */   private static final int FKTABLE_NAME = 6;
/*      */   private static final int FKTABLE_SCHEM = 5;
/*      */   private static final int KEY_SEQ = 8;
/*      */   private static final int PK_NAME = 12;
/*      */   private static final int PKCOLUMN_NAME = 3;
/*      */   private static final int PKTABLE_CAT = 0;
/*      */   private static final int PKTABLE_NAME = 2;
/*      */   private static final int PKTABLE_SCHEM = 1;
/*      */   private static final String SUPPORTS_FK = "SUPPORTS_FK";
/*      */   
/*      */   protected abstract class IterateBlock
/*      */   {
/*      */     DatabaseMetaData.IteratorWithCleanup iterator;
/*      */     
/*      */     IterateBlock(DatabaseMetaData.IteratorWithCleanup i)
/*      */     {
/*   70 */       this.iterator = i;
/*      */     }
/*      */     
/*      */     public void doForAll() throws SQLException {
/*      */       try {
/*   75 */         while (this.iterator.hasNext()) {
/*   76 */           forEach(this.iterator.next());
/*      */         }
/*      */       } finally {
/*   79 */         this.iterator.close();
/*      */       }
/*      */     }
/*      */     
/*      */     abstract void forEach(Object paramObject) throws SQLException;
/*      */   }
/*      */   
/*      */   protected abstract class IteratorWithCleanup
/*      */   {
/*      */     protected IteratorWithCleanup() {}
/*      */     
/*      */     abstract void close() throws SQLException;
/*      */     
/*      */     abstract boolean hasNext() throws SQLException;
/*      */     
/*      */     abstract Object next() throws SQLException;
/*      */   }
/*      */   
/*      */   class LocalAndReferencedColumns
/*      */   {
/*      */     String constraintName;
/*      */     List localColumnsList;
/*      */     String referencedCatalog;
/*      */     List referencedColumnsList;
/*      */     String referencedTable;
/*      */     
/*      */     LocalAndReferencedColumns(List localColumns, List refColumns, String constName, String refCatalog, String refTable)
/*      */     {
/*  107 */       this.localColumnsList = localColumns;
/*  108 */       this.referencedColumnsList = refColumns;
/*  109 */       this.constraintName = constName;
/*  110 */       this.referencedTable = refTable;
/*  111 */       this.referencedCatalog = refCatalog;
/*      */     }
/*      */   }
/*      */   
/*      */   protected class ResultSetIterator extends DatabaseMetaData.IteratorWithCleanup {
/*      */     int colIndex;
/*      */     java.sql.ResultSet resultSet;
/*      */     
/*      */     ResultSetIterator(java.sql.ResultSet rs, int index) {
/*  120 */       super();
/*  121 */       this.resultSet = rs;
/*  122 */       this.colIndex = index;
/*      */     }
/*      */     
/*      */     void close() throws SQLException {
/*  126 */       this.resultSet.close();
/*      */     }
/*      */     
/*      */     boolean hasNext() throws SQLException {
/*  130 */       return this.resultSet.next();
/*      */     }
/*      */     
/*      */     Object next() throws SQLException {
/*  134 */       return this.resultSet.getObject(this.colIndex);
/*      */     }
/*      */   }
/*      */   
/*      */   protected class SingleStringIterator extends DatabaseMetaData.IteratorWithCleanup {
/*  139 */     boolean onFirst = true;
/*      */     String value;
/*      */     
/*      */     SingleStringIterator(String s) {
/*  143 */       super();
/*  144 */       this.value = s;
/*      */     }
/*      */     
/*      */     void close()
/*      */       throws SQLException
/*      */     {}
/*      */     
/*      */     boolean hasNext() throws SQLException
/*      */     {
/*  153 */       return this.onFirst;
/*      */     }
/*      */     
/*      */     Object next() throws SQLException {
/*  157 */       this.onFirst = false;
/*  158 */       return this.value;
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   class TypeDescriptor
/*      */   {
/*      */     int bufferLength;
/*      */     
/*      */ 
/*      */     int charOctetLength;
/*      */     
/*      */     Integer columnSize;
/*      */     
/*      */     short dataType;
/*      */     
/*      */     Integer decimalDigits;
/*      */     
/*      */     String isNullable;
/*      */     
/*      */     int nullability;
/*      */     
/*  181 */     int numPrecRadix = 10;
/*      */     String typeName;
/*      */     
/*      */     TypeDescriptor(String typeInfo, String nullabilityInfo)
/*      */       throws SQLException
/*      */     {
/*  187 */       String mysqlType = "";
/*  188 */       String fullMysqlType = null;
/*      */       
/*  190 */       if (typeInfo.indexOf("(") != -1) {
/*  191 */         mysqlType = typeInfo.substring(0, typeInfo.indexOf("("));
/*      */       } else {
/*  193 */         mysqlType = typeInfo;
/*      */       }
/*      */       
/*  196 */       int indexOfUnsignedInMysqlType = StringUtils.indexOfIgnoreCase(mysqlType, "unsigned");
/*      */       
/*      */ 
/*  199 */       if (indexOfUnsignedInMysqlType != -1) {
/*  200 */         mysqlType = mysqlType.substring(0, indexOfUnsignedInMysqlType - 1);
/*      */       }
/*      */       
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  207 */       boolean isUnsigned = false;
/*      */       
/*  209 */       if (StringUtils.indexOfIgnoreCase(typeInfo, "unsigned") != -1) {
/*  210 */         fullMysqlType = mysqlType + " unsigned";
/*  211 */         isUnsigned = true;
/*      */       } else {
/*  213 */         fullMysqlType = mysqlType;
/*      */       }
/*      */       
/*  216 */       if (DatabaseMetaData.this.conn.getCapitalizeTypeNames()) {
/*  217 */         fullMysqlType = fullMysqlType.toUpperCase(Locale.ENGLISH);
/*      */       }
/*      */       
/*  220 */       this.dataType = ((short)MysqlDefs.mysqlToJavaType(mysqlType));
/*      */       
/*  222 */       this.typeName = fullMysqlType;
/*      */       
/*      */ 
/*  225 */       if (typeInfo != null) {
/*  226 */         if (StringUtils.startsWithIgnoreCase(typeInfo, "enum")) {
/*  227 */           String temp = typeInfo.substring(typeInfo.indexOf("("), typeInfo.lastIndexOf(")"));
/*      */           
/*  229 */           StringTokenizer tokenizer = new StringTokenizer(temp, ",");
/*      */           
/*  231 */           int maxLength = 0;
/*      */           
/*  233 */           while (tokenizer.hasMoreTokens()) {
/*  234 */             maxLength = Math.max(maxLength, tokenizer.nextToken().length() - 2);
/*      */           }
/*      */           
/*      */ 
/*  238 */           this.columnSize = new Integer(maxLength);
/*  239 */           this.decimalDigits = null;
/*  240 */         } else if (StringUtils.startsWithIgnoreCase(typeInfo, "set")) {
/*  241 */           String temp = typeInfo.substring(typeInfo.indexOf("("), typeInfo.lastIndexOf(")"));
/*      */           
/*  243 */           StringTokenizer tokenizer = new StringTokenizer(temp, ",");
/*      */           
/*  245 */           int maxLength = 0;
/*      */           
/*  247 */           while (tokenizer.hasMoreTokens()) {
/*  248 */             String setMember = tokenizer.nextToken().trim();
/*      */             
/*  250 */             if ((setMember.startsWith("'")) && (setMember.endsWith("'")))
/*      */             {
/*  252 */               maxLength += setMember.length() - 2;
/*      */             } else {
/*  254 */               maxLength += setMember.length();
/*      */             }
/*      */           }
/*      */           
/*  258 */           this.columnSize = new Integer(maxLength);
/*  259 */           this.decimalDigits = null;
/*  260 */         } else if (typeInfo.indexOf(",") != -1)
/*      */         {
/*  262 */           this.columnSize = new Integer(typeInfo.substring(typeInfo.indexOf("(") + 1, typeInfo.indexOf(",")).trim());
/*      */           
/*  264 */           this.decimalDigits = new Integer(typeInfo.substring(typeInfo.indexOf(",") + 1, typeInfo.indexOf(")")).trim());
/*      */         }
/*      */         else
/*      */         {
/*  268 */           this.columnSize = null;
/*  269 */           this.decimalDigits = null;
/*      */           
/*      */ 
/*  272 */           if (((StringUtils.indexOfIgnoreCase(typeInfo, "char") != -1) || (StringUtils.indexOfIgnoreCase(typeInfo, "text") != -1) || (StringUtils.indexOfIgnoreCase(typeInfo, "blob") != -1) || (StringUtils.indexOfIgnoreCase(typeInfo, "binary") != -1) || (StringUtils.indexOfIgnoreCase(typeInfo, "bit") != -1)) && (typeInfo.indexOf("(") != -1))
/*      */           {
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  279 */             int endParenIndex = typeInfo.indexOf(")");
/*      */             
/*  281 */             if (endParenIndex == -1) {
/*  282 */               endParenIndex = typeInfo.length();
/*      */             }
/*      */             
/*  285 */             this.columnSize = new Integer(typeInfo.substring(typeInfo.indexOf("(") + 1, endParenIndex).trim());
/*      */             
/*      */ 
/*      */ 
/*  289 */             if ((DatabaseMetaData.this.conn.getTinyInt1isBit()) && (this.columnSize.intValue() == 1) && (StringUtils.startsWithIgnoreCase(typeInfo, 0, "tinyint")))
/*      */             {
/*      */ 
/*      */ 
/*  293 */               if (DatabaseMetaData.this.conn.getTransformedBitIsBoolean()) {
/*  294 */                 this.dataType = 16;
/*  295 */                 this.typeName = "BOOLEAN";
/*      */               } else {
/*  297 */                 this.dataType = -7;
/*  298 */                 this.typeName = "BIT";
/*      */               }
/*      */             }
/*  301 */           } else if (StringUtils.startsWithIgnoreCaseAndWs(typeInfo, "tinyint"))
/*      */           {
/*  303 */             if ((DatabaseMetaData.this.conn.getTinyInt1isBit()) && (typeInfo.indexOf("(1)") != -1)) {
/*  304 */               if (DatabaseMetaData.this.conn.getTransformedBitIsBoolean()) {
/*  305 */                 this.dataType = 16;
/*  306 */                 this.typeName = "BOOLEAN";
/*      */               } else {
/*  308 */                 this.dataType = -7;
/*  309 */                 this.typeName = "BIT";
/*      */               }
/*      */             } else {
/*  312 */               this.columnSize = new Integer(3);
/*  313 */               this.decimalDigits = new Integer(0);
/*      */             }
/*  315 */           } else if (StringUtils.startsWithIgnoreCaseAndWs(typeInfo, "smallint"))
/*      */           {
/*  317 */             this.columnSize = new Integer(5);
/*  318 */             this.decimalDigits = new Integer(0);
/*  319 */           } else if (StringUtils.startsWithIgnoreCaseAndWs(typeInfo, "mediumint"))
/*      */           {
/*  321 */             this.columnSize = new Integer(isUnsigned ? 8 : 7);
/*  322 */             this.decimalDigits = new Integer(0);
/*  323 */           } else if (StringUtils.startsWithIgnoreCaseAndWs(typeInfo, "int"))
/*      */           {
/*  325 */             this.columnSize = new Integer(10);
/*  326 */             this.decimalDigits = new Integer(0);
/*  327 */           } else if (StringUtils.startsWithIgnoreCaseAndWs(typeInfo, "integer"))
/*      */           {
/*  329 */             this.columnSize = new Integer(10);
/*  330 */             this.decimalDigits = new Integer(0);
/*  331 */           } else if (StringUtils.startsWithIgnoreCaseAndWs(typeInfo, "bigint"))
/*      */           {
/*  333 */             this.columnSize = new Integer(isUnsigned ? 20 : 19);
/*  334 */             this.decimalDigits = new Integer(0);
/*  335 */           } else if (StringUtils.startsWithIgnoreCaseAndWs(typeInfo, "int24"))
/*      */           {
/*  337 */             this.columnSize = new Integer(19);
/*  338 */             this.decimalDigits = new Integer(0);
/*  339 */           } else if (StringUtils.startsWithIgnoreCaseAndWs(typeInfo, "real"))
/*      */           {
/*  341 */             this.columnSize = new Integer(12);
/*  342 */           } else if (StringUtils.startsWithIgnoreCaseAndWs(typeInfo, "float"))
/*      */           {
/*  344 */             this.columnSize = new Integer(12);
/*  345 */           } else if (StringUtils.startsWithIgnoreCaseAndWs(typeInfo, "decimal"))
/*      */           {
/*  347 */             this.columnSize = new Integer(12);
/*  348 */           } else if (StringUtils.startsWithIgnoreCaseAndWs(typeInfo, "numeric"))
/*      */           {
/*  350 */             this.columnSize = new Integer(12);
/*  351 */           } else if (StringUtils.startsWithIgnoreCaseAndWs(typeInfo, "double"))
/*      */           {
/*  353 */             this.columnSize = new Integer(22);
/*  354 */           } else if (StringUtils.startsWithIgnoreCaseAndWs(typeInfo, "char"))
/*      */           {
/*  356 */             this.columnSize = new Integer(1);
/*  357 */           } else if (StringUtils.startsWithIgnoreCaseAndWs(typeInfo, "varchar"))
/*      */           {
/*  359 */             this.columnSize = new Integer(255);
/*  360 */           } else if (StringUtils.startsWithIgnoreCaseAndWs(typeInfo, "date"))
/*      */           {
/*  362 */             this.columnSize = null;
/*  363 */           } else if (StringUtils.startsWithIgnoreCaseAndWs(typeInfo, "time"))
/*      */           {
/*  365 */             this.columnSize = null;
/*  366 */           } else if (StringUtils.startsWithIgnoreCaseAndWs(typeInfo, "timestamp"))
/*      */           {
/*  368 */             this.columnSize = null;
/*  369 */           } else if (StringUtils.startsWithIgnoreCaseAndWs(typeInfo, "datetime"))
/*      */           {
/*  371 */             this.columnSize = null;
/*  372 */           } else if (StringUtils.startsWithIgnoreCaseAndWs(typeInfo, "tinyblob"))
/*      */           {
/*  374 */             this.columnSize = new Integer(255);
/*  375 */           } else if (StringUtils.startsWithIgnoreCaseAndWs(typeInfo, "blob"))
/*      */           {
/*  377 */             this.columnSize = new Integer(65535);
/*  378 */           } else if (StringUtils.startsWithIgnoreCaseAndWs(typeInfo, "mediumblob"))
/*      */           {
/*  380 */             this.columnSize = new Integer(16777215);
/*  381 */           } else if (StringUtils.startsWithIgnoreCaseAndWs(typeInfo, "longblob"))
/*      */           {
/*  383 */             this.columnSize = new Integer(Integer.MAX_VALUE);
/*  384 */           } else if (StringUtils.startsWithIgnoreCaseAndWs(typeInfo, "tinytext"))
/*      */           {
/*  386 */             this.columnSize = new Integer(255);
/*  387 */           } else if (StringUtils.startsWithIgnoreCaseAndWs(typeInfo, "text"))
/*      */           {
/*  389 */             this.columnSize = new Integer(65535);
/*  390 */           } else if (StringUtils.startsWithIgnoreCaseAndWs(typeInfo, "mediumtext"))
/*      */           {
/*  392 */             this.columnSize = new Integer(16777215);
/*  393 */           } else if (StringUtils.startsWithIgnoreCaseAndWs(typeInfo, "longtext"))
/*      */           {
/*  395 */             this.columnSize = new Integer(Integer.MAX_VALUE);
/*  396 */           } else if (StringUtils.startsWithIgnoreCaseAndWs(typeInfo, "enum"))
/*      */           {
/*  398 */             this.columnSize = new Integer(255);
/*  399 */           } else if (StringUtils.startsWithIgnoreCaseAndWs(typeInfo, "set"))
/*      */           {
/*  401 */             this.columnSize = new Integer(255);
/*      */           }
/*      */         }
/*      */       }
/*      */       else {
/*  406 */         this.decimalDigits = null;
/*  407 */         this.columnSize = null;
/*      */       }
/*      */       
/*      */ 
/*  411 */       this.bufferLength = MysqlIO.getMaxBuf();
/*      */       
/*      */ 
/*  414 */       this.numPrecRadix = 10;
/*      */       
/*      */ 
/*  417 */       if (nullabilityInfo != null) {
/*  418 */         if (nullabilityInfo.equals("YES")) {
/*  419 */           this.nullability = 1;
/*  420 */           this.isNullable = "YES";
/*      */         }
/*      */         else
/*      */         {
/*  424 */           this.nullability = 0;
/*  425 */           this.isNullable = "NO";
/*      */         }
/*      */       } else {
/*  428 */         this.nullability = 0;
/*  429 */         this.isNullable = "NO";
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
/*      */ 
/*      */ 
/*  469 */   private static final byte[] TABLE_AS_BYTES = "TABLE".getBytes();
/*      */   
/*      */   private static final int UPDATE_RULE = 9;
/*      */   
/*  473 */   private static final byte[] VIEW_AS_BYTES = "VIEW".getBytes();
/*      */   protected Connection conn;
/*      */   
/*      */   static {
/*  477 */     String[] allMySQLKeywords = { "ACCESSIBLE", "ADD", "ALL", "ALTER", "ANALYZE", "AND", "AS", "ASC", "ASENSITIVE", "BEFORE", "BETWEEN", "BIGINT", "BINARY", "BLOB", "BOTH", "BY", "CALL", "CASCADE", "CASE", "CHANGE", "CHAR", "CHARACTER", "CHECK", "COLLATE", "COLUMN", "CONDITION", "CONNECTION", "CONSTRAINT", "CONTINUE", "CONVERT", "CREATE", "CROSS", "CURRENT_DATE", "CURRENT_TIME", "CURRENT_TIMESTAMP", "CURRENT_USER", "CURSOR", "DATABASE", "DATABASES", "DAY_HOUR", "DAY_MICROSECOND", "DAY_MINUTE", "DAY_SECOND", "DEC", "DECIMAL", "DECLARE", "DEFAULT", "DELAYED", "DELETE", "DESC", "DESCRIBE", "DETERMINISTIC", "DISTINCT", "DISTINCTROW", "DIV", "DOUBLE", "DROP", "DUAL", "EACH", "ELSE", "ELSEIF", "ENCLOSED", "ESCAPED", "EXISTS", "EXIT", "EXPLAIN", "FALSE", "FETCH", "FLOAT", "FLOAT4", "FLOAT8", "FOR", "FORCE", "FOREIGN", "FROM", "FULLTEXT", "GRANT", "GROUP", "HAVING", "HIGH_PRIORITY", "HOUR_MICROSECOND", "HOUR_MINUTE", "HOUR_SECOND", "IF", "IGNORE", "IN", "INDEX", "INFILE", "INNER", "INOUT", "INSENSITIVE", "INSERT", "INT", "INT1", "INT2", "INT3", "INT4", "INT8", "INTEGER", "INTERVAL", "INTO", "IS", "ITERATE", "JOIN", "KEY", "KEYS", "KILL", "LEADING", "LEAVE", "LEFT", "LIKE", "LIMIT", "LINEAR", "LINES", "LOAD", "LOCALTIME", "LOCALTIMESTAMP", "LOCK", "LONG", "LONGBLOB", "LONGTEXT", "LOOP", "LOW_PRIORITY", "MATCH", "MEDIUMBLOB", "MEDIUMINT", "MEDIUMTEXT", "MIDDLEINT", "MINUTE_MICROSECOND", "MINUTE_SECOND", "MOD", "MODIFIES", "NATURAL", "NOT", "NO_WRITE_TO_BINLOG", "NULL", "NUMERIC", "ON", "OPTIMIZE", "OPTION", "OPTIONALLY", "OR", "ORDER", "OUT", "OUTER", "OUTFILE", "PRECISION", "PRIMARY", "PROCEDURE", "PURGE", "RANGE", "READ", "READS", "READ_ONLY", "READ_WRITE", "REAL", "REFERENCES", "REGEXP", "RELEASE", "RENAME", "REPEAT", "REPLACE", "REQUIRE", "RESTRICT", "RETURN", "REVOKE", "RIGHT", "RLIKE", "SCHEMA", "SCHEMAS", "SECOND_MICROSECOND", "SELECT", "SENSITIVE", "SEPARATOR", "SET", "SHOW", "SMALLINT", "SPATIAL", "SPECIFIC", "SQL", "SQLEXCEPTION", "SQLSTATE", "SQLWARNING", "SQL_BIG_RESULT", "SQL_CALC_FOUND_ROWS", "SQL_SMALL_RESULT", "SSL", "STARTING", "STRAIGHT_JOIN", "TABLE", "TERMINATED", "THEN", "TINYBLOB", "TINYINT", "TINYTEXT", "TO", "TRAILING", "TRIGGER", "TRUE", "UNDO", "UNION", "UNIQUE", "UNLOCK", "UNSIGNED", "UPDATE", "USAGE", "USE", "USING", "UTC_DATE", "UTC_TIME", "UTC_TIMESTAMP", "VALUES", "VARBINARY", "VARCHAR", "VARCHARACTER", "VARYING", "WHEN", "WHERE", "WHILE", "WITH", "WRITE", "X509", "XOR", "YEAR_MONTH", "ZEROFILL" };
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  520 */     String[] sql92Keywords = { "ABSOLUTE", "EXEC", "OVERLAPS", "ACTION", "EXECUTE", "PAD", "ADA", "EXISTS", "PARTIAL", "ADD", "EXTERNAL", "PASCAL", "ALL", "EXTRACT", "POSITION", "ALLOCATE", "FALSE", "PRECISION", "ALTER", "FETCH", "PREPARE", "AND", "FIRST", "PRESERVE", "ANY", "FLOAT", "PRIMARY", "ARE", "FOR", "PRIOR", "AS", "FOREIGN", "PRIVILEGES", "ASC", "FORTRAN", "PROCEDURE", "ASSERTION", "FOUND", "PUBLIC", "AT", "FROM", "READ", "AUTHORIZATION", "FULL", "REAL", "AVG", "GET", "REFERENCES", "BEGIN", "GLOBAL", "RELATIVE", "BETWEEN", "GO", "RESTRICT", "BIT", "GOTO", "REVOKE", "BIT_LENGTH", "GRANT", "RIGHT", "BOTH", "GROUP", "ROLLBACK", "BY", "HAVING", "ROWS", "CASCADE", "HOUR", "SCHEMA", "CASCADED", "IDENTITY", "SCROLL", "CASE", "IMMEDIATE", "SECOND", "CAST", "IN", "SECTION", "CATALOG", "INCLUDE", "SELECT", "CHAR", "INDEX", "SESSION", "CHAR_LENGTH", "INDICATOR", "SESSION_USER", "CHARACTER", "INITIALLY", "SET", "CHARACTER_LENGTH", "INNER", "SIZE", "CHECK", "INPUT", "SMALLINT", "CLOSE", "INSENSITIVE", "SOME", "COALESCE", "INSERT", "SPACE", "COLLATE", "INT", "SQL", "COLLATION", "INTEGER", "SQLCA", "COLUMN", "INTERSECT", "SQLCODE", "COMMIT", "INTERVAL", "SQLERROR", "CONNECT", "INTO", "SQLSTATE", "CONNECTION", "IS", "SQLWARNING", "CONSTRAINT", "ISOLATION", "SUBSTRING", "CONSTRAINTS", "JOIN", "SUM", "CONTINUE", "KEY", "SYSTEM_USER", "CONVERT", "LANGUAGE", "TABLE", "CORRESPONDING", "LAST", "TEMPORARY", "COUNT", "LEADING", "THEN", "CREATE", "LEFT", "TIME", "CROSS", "LEVEL", "TIMESTAMP", "CURRENT", "LIKE", "TIMEZONE_HOUR", "CURRENT_DATE", "LOCAL", "TIMEZONE_MINUTE", "CURRENT_TIME", "LOWER", "TO", "CURRENT_TIMESTAMP", "MATCH", "TRAILING", "CURRENT_USER", "MAX", "TRANSACTION", "CURSOR", "MIN", "TRANSLATE", "DATE", "MINUTE", "TRANSLATION", "DAY", "MODULE", "TRIM", "DEALLOCATE", "MONTH", "TRUE", "DEC", "NAMES", "UNION", "DECIMAL", "NATIONAL", "UNIQUE", "DECLARE", "NATURAL", "UNKNOWN", "DEFAULT", "NCHAR", "UPDATE", "DEFERRABLE", "NEXT", "UPPER", "DEFERRED", "NO", "USAGE", "DELETE", "NONE", "USER", "DESC", "NOT", "USING", "DESCRIBE", "NULL", "VALUE", "DESCRIPTOR", "NULLIF", "VALUES", "DIAGNOSTICS", "NUMERIC", "VARCHAR", "DISCONNECT", "OCTET_LENGTH", "VARYING", "DISTINCT", "OF", "VIEW", "DOMAIN", "ON", "WHEN", "DOUBLE", "ONLY", "WHENEVER", "DROP", "OPEN", "WHERE", "ELSE", "OPTION", "WITH", "END", "OR", "WORK", "END-EXEC", "ORDER", "WRITE", "ESCAPE", "OUTER", "YEAR", "EXCEPT", "OUTPUT", "ZONE", "EXCEPTION" };
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  562 */     TreeMap mySQLKeywordMap = new TreeMap();
/*      */     
/*  564 */     for (int i = 0; i < allMySQLKeywords.length; i++) {
/*  565 */       mySQLKeywordMap.put(allMySQLKeywords[i], null);
/*      */     }
/*      */     
/*  568 */     HashMap sql92KeywordMap = new HashMap(sql92Keywords.length);
/*      */     
/*  570 */     for (int i = 0; i < sql92Keywords.length; i++) {
/*  571 */       sql92KeywordMap.put(sql92Keywords[i], null);
/*      */     }
/*      */     
/*  574 */     Iterator it = sql92KeywordMap.keySet().iterator();
/*      */     
/*  576 */     while (it.hasNext()) {
/*  577 */       mySQLKeywordMap.remove(it.next());
/*      */     }
/*      */     
/*  580 */     StringBuffer keywordBuf = new StringBuffer();
/*      */     
/*  582 */     it = mySQLKeywordMap.keySet().iterator();
/*      */     
/*  584 */     if (it.hasNext()) {
/*  585 */       keywordBuf.append(it.next().toString());
/*      */     }
/*      */     
/*  588 */     while (it.hasNext()) {
/*  589 */       keywordBuf.append(",");
/*  590 */       keywordBuf.append(it.next().toString());
/*      */     }
/*      */     
/*  593 */     mysqlKeywordsThatArentSQL92 = keywordBuf.toString();
/*      */   }
/*      */   
/*      */   static java.sql.ResultSet buildResultSet(Field[] fields, ArrayList rows, Connection c) throws SQLException
/*      */   {
/*  598 */     int fieldsLength = fields.length;
/*      */     
/*  600 */     for (int i = 0; i < fieldsLength; i++) {
/*  601 */       fields[i].setConnection(c);
/*  602 */       fields[i].setUseOldNameMetadata(true);
/*      */     }
/*      */     
/*  605 */     return new ResultSet(c.getCatalog(), fields, new RowDataStatic(rows), c, null);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  613 */   protected String database = null;
/*      */   
/*      */ 
/*  616 */   protected String quotedId = null;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public DatabaseMetaData(Connection connToSet, String databaseToSet)
/*      */   {
/*  627 */     this.conn = connToSet;
/*  628 */     this.database = databaseToSet;
/*      */     try
/*      */     {
/*  631 */       this.quotedId = (this.conn.supportsQuotedIdentifiers() ? getIdentifierQuoteString() : "");
/*      */ 
/*      */     }
/*      */     catch (SQLException sqlEx)
/*      */     {
/*      */ 
/*  637 */       AssertionFailedException.shouldNotHappen(sqlEx);
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public boolean allProceduresAreCallable()
/*      */     throws SQLException
/*      */   {
/*  650 */     return false;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public boolean allTablesAreSelectable()
/*      */     throws SQLException
/*      */   {
/*  661 */     return false;
/*      */   }
/*      */   
/*      */   private java.sql.ResultSet buildResultSet(Field[] fields, ArrayList rows) throws SQLException
/*      */   {
/*  666 */     return buildResultSet(fields, rows, this.conn);
/*      */   }
/*      */   
/*      */   private void convertToJdbcFunctionList(String catalog, java.sql.ResultSet proceduresRs, boolean needsClientFiltering, String db, Map procedureRowsOrderedByName, int nameIndex)
/*      */     throws SQLException
/*      */   {
/*  672 */     while (proceduresRs.next()) {
/*  673 */       boolean shouldAdd = true;
/*      */       
/*  675 */       if (needsClientFiltering) {
/*  676 */         shouldAdd = false;
/*      */         
/*  678 */         String procDb = proceduresRs.getString(1);
/*      */         
/*  680 */         if ((db == null) && (procDb == null)) {
/*  681 */           shouldAdd = true;
/*  682 */         } else if ((db != null) && (db.equals(procDb))) {
/*  683 */           shouldAdd = true;
/*      */         }
/*      */       }
/*      */       
/*  687 */       if (shouldAdd) {
/*  688 */         String functionName = proceduresRs.getString(nameIndex);
/*  689 */         byte[][] rowData = new byte[8][];
/*  690 */         rowData[0] = (catalog == null ? null : s2b(catalog));
/*  691 */         rowData[1] = null;
/*  692 */         rowData[2] = s2b(functionName);
/*  693 */         rowData[3] = null;
/*  694 */         rowData[4] = null;
/*  695 */         rowData[5] = null;
/*  696 */         rowData[6] = null;
/*  697 */         rowData[7] = s2b(Integer.toString(2));
/*      */         
/*  699 */         procedureRowsOrderedByName.put(functionName, rowData);
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */   private void convertToJdbcProcedureList(boolean fromSelect, String catalog, java.sql.ResultSet proceduresRs, boolean needsClientFiltering, String db, Map procedureRowsOrderedByName, int nameIndex)
/*      */     throws SQLException
/*      */   {
/*  707 */     while (proceduresRs.next()) {
/*  708 */       boolean shouldAdd = true;
/*      */       
/*  710 */       if (needsClientFiltering) {
/*  711 */         shouldAdd = false;
/*      */         
/*  713 */         String procDb = proceduresRs.getString(1);
/*      */         
/*  715 */         if ((db == null) && (procDb == null)) {
/*  716 */           shouldAdd = true;
/*  717 */         } else if ((db != null) && (db.equals(procDb))) {
/*  718 */           shouldAdd = true;
/*      */         }
/*      */       }
/*      */       
/*  722 */       if (shouldAdd) {
/*  723 */         String procedureName = proceduresRs.getString(nameIndex);
/*  724 */         byte[][] rowData = new byte[8][];
/*  725 */         rowData[0] = (catalog == null ? null : s2b(catalog));
/*  726 */         rowData[1] = null;
/*  727 */         rowData[2] = s2b(procedureName);
/*  728 */         rowData[3] = null;
/*  729 */         rowData[4] = null;
/*  730 */         rowData[5] = null;
/*  731 */         rowData[6] = null;
/*      */         
/*  733 */         boolean isFunction = fromSelect ? "FUNCTION".equalsIgnoreCase(proceduresRs.getString("type")) : false;
/*      */         
/*      */ 
/*  736 */         rowData[7] = s2b(isFunction ? Integer.toString(2) : Integer.toString(0));
/*      */         
/*      */ 
/*      */ 
/*  740 */         procedureRowsOrderedByName.put(procedureName, rowData);
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */   private byte[][] convertTypeDescriptorToProcedureRow(byte[] procNameAsBytes, String paramName, boolean isOutParam, boolean isInParam, boolean isReturnParam, TypeDescriptor typeDesc)
/*      */     throws SQLException
/*      */   {
/*  749 */     byte[][] row = new byte[14][];
/*  750 */     row[0] = null;
/*  751 */     row[1] = null;
/*  752 */     row[2] = procNameAsBytes;
/*  753 */     row[3] = s2b(paramName);
/*      */     
/*  755 */     if ((isInParam) && (isOutParam)) {
/*  756 */       row[4] = s2b(String.valueOf(2));
/*  757 */     } else if (isInParam) {
/*  758 */       row[4] = s2b(String.valueOf(1));
/*  759 */     } else if (isOutParam) {
/*  760 */       row[4] = s2b(String.valueOf(4));
/*  761 */     } else if (isReturnParam) {
/*  762 */       row[4] = s2b(String.valueOf(5));
/*      */     } else {
/*  764 */       row[4] = s2b(String.valueOf(0));
/*      */     }
/*  766 */     row[5] = s2b(Short.toString(typeDesc.dataType));
/*  767 */     row[6] = s2b(typeDesc.typeName);
/*  768 */     row[7] = (typeDesc.columnSize == null ? null : s2b(typeDesc.columnSize.toString()));
/*      */     
/*  770 */     row[8] = s2b(Integer.toString(typeDesc.bufferLength));
/*  771 */     row[9] = (typeDesc.decimalDigits == null ? null : s2b(typeDesc.decimalDigits.toString()));
/*      */     
/*  773 */     row[10] = s2b(Integer.toString(typeDesc.numPrecRadix));
/*      */     
/*  775 */     switch (typeDesc.nullability) {
/*      */     case 0: 
/*  777 */       row[11] = s2b(Integer.toString(0));
/*      */       
/*  779 */       break;
/*      */     
/*      */     case 1: 
/*  782 */       row[11] = s2b(Integer.toString(1));
/*      */       
/*  784 */       break;
/*      */     
/*      */     case 2: 
/*  787 */       row[11] = s2b(Integer.toString(2));
/*      */       
/*  789 */       break;
/*      */     
/*      */     default: 
/*  792 */       throw SQLError.createSQLException("Internal error while parsing callable statement metadata (unknown nullability value fount)", "S1000");
/*      */     }
/*      */     
/*      */     
/*      */ 
/*  797 */     row[12] = null;
/*  798 */     return row;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public boolean dataDefinitionCausesTransactionCommit()
/*      */     throws SQLException
/*      */   {
/*  810 */     return true;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public boolean dataDefinitionIgnoredInTransactions()
/*      */     throws SQLException
/*      */   {
/*  821 */     return false;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public boolean deletesAreDetected(int type)
/*      */     throws SQLException
/*      */   {
/*  836 */     return false;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public boolean doesMaxRowSizeIncludeBlobs()
/*      */     throws SQLException
/*      */   {
/*  849 */     return true;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   private int endPositionOfParameterDeclaration(int beginIndex, String procedureDef, String quoteChar)
/*      */     throws SQLException
/*      */   {
/*  870 */     int currentPos = beginIndex + 1;
/*  871 */     int parenDepth = 1;
/*      */     
/*  873 */     while ((parenDepth > 0) && (currentPos < procedureDef.length())) {
/*  874 */       int closedParenIndex = StringUtils.indexOfIgnoreCaseRespectQuotes(currentPos, procedureDef, ")", quoteChar.charAt(0), !this.conn.isNoBackslashEscapesSet());
/*      */       
/*      */ 
/*      */ 
/*  878 */       if (closedParenIndex != -1) {
/*  879 */         int nextOpenParenIndex = StringUtils.indexOfIgnoreCaseRespectQuotes(currentPos, procedureDef, "(", quoteChar.charAt(0), !this.conn.isNoBackslashEscapesSet());
/*      */         
/*      */ 
/*      */ 
/*      */ 
/*  884 */         if ((nextOpenParenIndex != -1) && (nextOpenParenIndex < closedParenIndex))
/*      */         {
/*  886 */           parenDepth++;
/*  887 */           currentPos = closedParenIndex + 1;
/*      */         }
/*      */         else
/*      */         {
/*  891 */           parenDepth--;
/*  892 */           currentPos = closedParenIndex;
/*      */         }
/*      */       }
/*      */       else
/*      */       {
/*  897 */         throw SQLError.createSQLException("Internal error when parsing callable statement metadata", "S1000");
/*      */       }
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*  904 */     return currentPos;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public List extractForeignKeyForTable(ArrayList rows, java.sql.ResultSet rs, String catalog)
/*      */     throws SQLException
/*      */   {
/*  922 */     byte[][] row = new byte[3][];
/*  923 */     row[0] = rs.getBytes(1);
/*  924 */     row[1] = s2b("SUPPORTS_FK");
/*      */     
/*  926 */     String createTableString = rs.getString(2);
/*  927 */     StringTokenizer lineTokenizer = new StringTokenizer(createTableString, "\n");
/*      */     
/*  929 */     StringBuffer commentBuf = new StringBuffer("comment; ");
/*  930 */     boolean firstTime = true;
/*      */     
/*  932 */     String quoteChar = getIdentifierQuoteString();
/*      */     
/*  934 */     if (quoteChar == null) {
/*  935 */       quoteChar = "`";
/*      */     }
/*      */     
/*  938 */     while (lineTokenizer.hasMoreTokens()) {
/*  939 */       String line = lineTokenizer.nextToken().trim();
/*      */       
/*  941 */       String constraintName = null;
/*      */       
/*  943 */       if (StringUtils.startsWithIgnoreCase(line, "CONSTRAINT")) {
/*  944 */         boolean usingBackTicks = true;
/*  945 */         int beginPos = line.indexOf(quoteChar);
/*      */         
/*  947 */         if (beginPos == -1) {
/*  948 */           beginPos = line.indexOf("\"");
/*  949 */           usingBackTicks = false;
/*      */         }
/*      */         
/*  952 */         if (beginPos != -1) {
/*  953 */           int endPos = -1;
/*      */           
/*  955 */           if (usingBackTicks) {
/*  956 */             endPos = line.indexOf(quoteChar, beginPos + 1);
/*      */           } else {
/*  958 */             endPos = line.indexOf("\"", beginPos + 1);
/*      */           }
/*      */           
/*  961 */           if (endPos != -1) {
/*  962 */             constraintName = line.substring(beginPos + 1, endPos);
/*  963 */             line = line.substring(endPos + 1, line.length()).trim();
/*      */           }
/*      */         }
/*      */       }
/*      */       
/*  968 */       if (line.startsWith("FOREIGN KEY")) {
/*  969 */         if (line.endsWith(",")) {
/*  970 */           line = line.substring(0, line.length() - 1);
/*      */         }
/*      */         
/*  973 */         char quote = this.quotedId.charAt(0);
/*      */         
/*  975 */         int indexOfFK = line.indexOf("FOREIGN KEY");
/*      */         
/*  977 */         String localColumnName = null;
/*  978 */         String referencedCatalogName = this.quotedId + catalog + this.quotedId;
/*      */         
/*  980 */         String referencedTableName = null;
/*  981 */         String referencedColumnName = null;
/*      */         
/*  983 */         if (indexOfFK != -1) {
/*  984 */           int afterFk = indexOfFK + "FOREIGN KEY".length();
/*      */           
/*  986 */           int indexOfRef = StringUtils.indexOfIgnoreCaseRespectQuotes(afterFk, line, "REFERENCES", quote, true);
/*      */           
/*      */ 
/*      */ 
/*  990 */           if (indexOfRef != -1)
/*      */           {
/*  992 */             int indexOfParenOpen = line.indexOf('(', afterFk);
/*  993 */             int indexOfParenClose = StringUtils.indexOfIgnoreCaseRespectQuotes(indexOfParenOpen, line, ")", quote, true);
/*      */             
/*      */ 
/*      */ 
/*      */ 
/*  998 */             if ((indexOfParenOpen != -1) && (indexOfParenClose == -1)) {}
/*      */             
/*      */ 
/*      */ 
/* 1002 */             localColumnName = line.substring(indexOfParenOpen + 1, indexOfParenClose);
/*      */             
/*      */ 
/* 1005 */             int afterRef = indexOfRef + "REFERENCES".length();
/*      */             
/* 1007 */             int referencedColumnBegin = StringUtils.indexOfIgnoreCaseRespectQuotes(afterRef, line, "(", quote, true);
/*      */             
/*      */ 
/*      */ 
/* 1011 */             if (referencedColumnBegin != -1) {
/* 1012 */               referencedTableName = line.substring(afterRef, referencedColumnBegin);
/*      */               
/*      */ 
/* 1015 */               int referencedColumnEnd = StringUtils.indexOfIgnoreCaseRespectQuotes(referencedColumnBegin + 1, line, ")", quote, true);
/*      */               
/*      */ 
/*      */ 
/*      */ 
/* 1020 */               if (referencedColumnEnd != -1) {
/* 1021 */                 referencedColumnName = line.substring(referencedColumnBegin + 1, referencedColumnEnd);
/*      */               }
/*      */               
/*      */ 
/*      */ 
/* 1026 */               int indexOfCatalogSep = StringUtils.indexOfIgnoreCaseRespectQuotes(0, referencedTableName, ".", quote, true);
/*      */               
/*      */ 
/*      */ 
/*      */ 
/* 1031 */               if (indexOfCatalogSep != -1) {
/* 1032 */                 referencedCatalogName = referencedTableName.substring(0, indexOfCatalogSep);
/*      */                 
/* 1034 */                 referencedTableName = referencedTableName.substring(indexOfCatalogSep + 1);
/*      */               }
/*      */             }
/*      */           }
/*      */         }
/*      */         
/*      */ 
/* 1041 */         if (!firstTime) {
/* 1042 */           commentBuf.append("; ");
/*      */         } else {
/* 1044 */           firstTime = false;
/*      */         }
/*      */         
/* 1047 */         if (constraintName != null) {
/* 1048 */           commentBuf.append(constraintName);
/*      */         } else {
/* 1050 */           commentBuf.append("not_available");
/*      */         }
/*      */         
/* 1053 */         commentBuf.append("(");
/* 1054 */         commentBuf.append(localColumnName);
/* 1055 */         commentBuf.append(") REFER ");
/* 1056 */         commentBuf.append(referencedCatalogName);
/* 1057 */         commentBuf.append("/");
/* 1058 */         commentBuf.append(referencedTableName);
/* 1059 */         commentBuf.append("(");
/* 1060 */         commentBuf.append(referencedColumnName);
/* 1061 */         commentBuf.append(")");
/*      */         
/* 1063 */         int lastParenIndex = line.lastIndexOf(")");
/*      */         
/* 1065 */         if (lastParenIndex != line.length() - 1) {
/* 1066 */           String cascadeOptions = cascadeOptions = line.substring(lastParenIndex + 1);
/*      */           
/* 1068 */           commentBuf.append(" ");
/* 1069 */           commentBuf.append(cascadeOptions);
/*      */         }
/*      */       }
/*      */     }
/*      */     
/* 1074 */     row[2] = s2b(commentBuf.toString());
/* 1075 */     rows.add(row);
/*      */     
/* 1077 */     return rows;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public java.sql.ResultSet extractForeignKeyFromCreateTable(String catalog, String tableName)
/*      */     throws SQLException
/*      */   {
/* 1098 */     ArrayList tableList = new ArrayList();
/* 1099 */     java.sql.ResultSet rs = null;
/* 1100 */     Statement stmt = null;
/*      */     
/* 1102 */     if (tableName != null) {
/* 1103 */       tableList.add(tableName);
/*      */     } else {
/*      */       try {
/* 1106 */         rs = getTables(catalog, "", "%", new String[] { "TABLE" });
/*      */         
/* 1108 */         while (rs.next()) {
/* 1109 */           tableList.add(rs.getString("TABLE_NAME"));
/*      */         }
/*      */       } finally {
/* 1112 */         if (rs != null) {
/* 1113 */           rs.close();
/*      */         }
/*      */         
/* 1116 */         rs = null;
/*      */       }
/*      */     }
/*      */     
/* 1120 */     ArrayList rows = new ArrayList();
/* 1121 */     Field[] fields = new Field[3];
/* 1122 */     fields[0] = new Field("", "Name", 1, Integer.MAX_VALUE);
/* 1123 */     fields[1] = new Field("", "Type", 1, 255);
/* 1124 */     fields[2] = new Field("", "Comment", 1, Integer.MAX_VALUE);
/*      */     
/* 1126 */     int numTables = tableList.size();
/* 1127 */     stmt = this.conn.getMetadataSafeStatement();
/*      */     
/* 1129 */     String quoteChar = getIdentifierQuoteString();
/*      */     
/* 1131 */     if (quoteChar == null) {
/* 1132 */       quoteChar = "`";
/*      */     }
/*      */     try
/*      */     {
/* 1136 */       for (int i = 0; i < numTables; i++) {
/* 1137 */         String tableToExtract = (String)tableList.get(i);
/*      */         
/* 1139 */         String query = "SHOW CREATE TABLE " + quoteChar + catalog + quoteChar + "." + quoteChar + tableToExtract + quoteChar;
/*      */         
/*      */ 
/*      */         try
/*      */         {
/* 1144 */           rs = stmt.executeQuery(query);
/*      */         }
/*      */         catch (SQLException sqlEx) {
/* 1147 */           String sqlState = sqlEx.getSQLState();
/*      */           
/* 1149 */           if ((!"42S02".equals(sqlState)) && (sqlEx.getErrorCode() != 1146))
/*      */           {
/* 1151 */             throw sqlEx;
/*      */           }
/*      */           
/* 1154 */           continue;
/*      */         }
/*      */         
/* 1157 */         while (rs.next()) {
/* 1158 */           extractForeignKeyForTable(rows, rs, catalog);
/*      */         }
/*      */       }
/*      */     } finally {
/* 1162 */       if (rs != null) {
/* 1163 */         rs.close();
/*      */       }
/*      */       
/* 1166 */       rs = null;
/*      */       
/* 1168 */       if (stmt != null) {
/* 1169 */         stmt.close();
/*      */       }
/*      */       
/* 1172 */       stmt = null;
/*      */     }
/*      */     
/* 1175 */     return buildResultSet(fields, rows);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   private int findEndOfReturnsClause(String procedureDefn, String quoteChar, int positionOfReturnKeyword)
/*      */     throws SQLException
/*      */   {
/* 1200 */     String[] tokens = { "LANGUAGE", "NOT", "DETERMINISTIC", "CONTAINS", "NO", "READ", "MODIFIES", "SQL", "COMMENT", "BEGIN", "RETURN" };
/*      */     
/*      */ 
/*      */ 
/* 1204 */     int startLookingAt = positionOfReturnKeyword + "RETURNS".length() + 1;
/*      */     
/* 1206 */     for (int i = 0; i < tokens.length; i++) {
/* 1207 */       int endOfReturn = StringUtils.indexOfIgnoreCaseRespectQuotes(startLookingAt, procedureDefn, tokens[i], quoteChar.charAt(0), !this.conn.isNoBackslashEscapesSet());
/*      */       
/*      */ 
/*      */ 
/* 1211 */       if (endOfReturn != -1) {
/* 1212 */         return endOfReturn;
/*      */       }
/*      */     }
/*      */     
/*      */ 
/* 1217 */     int endOfReturn = StringUtils.indexOfIgnoreCaseRespectQuotes(startLookingAt, procedureDefn, ":", quoteChar.charAt(0), !this.conn.isNoBackslashEscapesSet());
/*      */     
/*      */ 
/*      */ 
/* 1221 */     if (endOfReturn != -1)
/*      */     {
/* 1223 */       for (int i = endOfReturn; i > 0; i--) {
/* 1224 */         if (Character.isWhitespace(procedureDefn.charAt(i))) {
/* 1225 */           return i;
/*      */         }
/*      */       }
/*      */     }
/*      */     
/*      */ 
/*      */ 
/* 1232 */     throw SQLError.createSQLException("Internal error when parsing callable statement metadata", "S1000");
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public java.sql.ResultSet getAttributes(String arg0, String arg1, String arg2, String arg3)
/*      */     throws SQLException
/*      */   {
/* 1242 */     Field[] fields = new Field[21];
/* 1243 */     fields[0] = new Field("", "TYPE_CAT", 1, 32);
/* 1244 */     fields[1] = new Field("", "TYPE_SCHEM", 1, 32);
/* 1245 */     fields[2] = new Field("", "TYPE_NAME", 1, 32);
/* 1246 */     fields[3] = new Field("", "ATTR_NAME", 1, 32);
/* 1247 */     fields[4] = new Field("", "DATA_TYPE", 5, 32);
/* 1248 */     fields[5] = new Field("", "ATTR_TYPE_NAME", 1, 32);
/* 1249 */     fields[6] = new Field("", "ATTR_SIZE", 4, 32);
/* 1250 */     fields[7] = new Field("", "DECIMAL_DIGITS", 4, 32);
/* 1251 */     fields[8] = new Field("", "NUM_PREC_RADIX", 4, 32);
/* 1252 */     fields[9] = new Field("", "NULLABLE ", 4, 32);
/* 1253 */     fields[10] = new Field("", "REMARKS", 1, 32);
/* 1254 */     fields[11] = new Field("", "ATTR_DEF", 1, 32);
/* 1255 */     fields[12] = new Field("", "SQL_DATA_TYPE", 4, 32);
/* 1256 */     fields[13] = new Field("", "SQL_DATETIME_SUB", 4, 32);
/* 1257 */     fields[14] = new Field("", "CHAR_OCTET_LENGTH", 4, 32);
/* 1258 */     fields[15] = new Field("", "ORDINAL_POSITION", 4, 32);
/* 1259 */     fields[16] = new Field("", "IS_NULLABLE", 1, 32);
/* 1260 */     fields[17] = new Field("", "SCOPE_CATALOG", 1, 32);
/* 1261 */     fields[18] = new Field("", "SCOPE_SCHEMA", 1, 32);
/* 1262 */     fields[19] = new Field("", "SCOPE_TABLE", 1, 32);
/* 1263 */     fields[20] = new Field("", "SOURCE_DATA_TYPE", 5, 32);
/*      */     
/* 1265 */     return buildResultSet(fields, new ArrayList());
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public java.sql.ResultSet getBestRowIdentifier(String catalog, String schema, String table, int scope, boolean nullable)
/*      */     throws SQLException
/*      */   {
/* 1316 */     if (table == null) {
/* 1317 */       throw SQLError.createSQLException("Table not specified.", "S1009");
/*      */     }
/*      */     
/*      */ 
/* 1321 */     Field[] fields = new Field[8];
/* 1322 */     fields[0] = new Field("", "SCOPE", 5, 5);
/* 1323 */     fields[1] = new Field("", "COLUMN_NAME", 1, 32);
/* 1324 */     fields[2] = new Field("", "DATA_TYPE", 5, 32);
/* 1325 */     fields[3] = new Field("", "TYPE_NAME", 1, 32);
/* 1326 */     fields[4] = new Field("", "COLUMN_SIZE", 4, 10);
/* 1327 */     fields[5] = new Field("", "BUFFER_LENGTH", 4, 10);
/* 1328 */     fields[6] = new Field("", "DECIMAL_DIGITS", 4, 10);
/* 1329 */     fields[7] = new Field("", "PSEUDO_COLUMN", 5, 5);
/*      */     
/* 1331 */     ArrayList rows = new ArrayList();
/* 1332 */     Statement stmt = this.conn.getMetadataSafeStatement();
/*      */     
/*      */     try
/*      */     {
/* 1336 */       new IterateBlock(getCatalogIterator(catalog), table) { private final String val$table;
/*      */         
/* 1338 */         void forEach(Object catalogStr) throws SQLException { java.sql.ResultSet results = null;
/*      */           try
/*      */           {
/* 1341 */             StringBuffer queryBuf = new StringBuffer("SHOW COLUMNS FROM ");
/*      */             
/* 1343 */             queryBuf.append(DatabaseMetaData.this.quotedId);
/* 1344 */             queryBuf.append(this.val$table);
/* 1345 */             queryBuf.append(DatabaseMetaData.this.quotedId);
/* 1346 */             queryBuf.append(" FROM ");
/* 1347 */             queryBuf.append(DatabaseMetaData.this.quotedId);
/* 1348 */             queryBuf.append(catalogStr.toString());
/* 1349 */             queryBuf.append(DatabaseMetaData.this.quotedId);
/*      */             
/* 1351 */             results = this.val$stmt.executeQuery(queryBuf.toString());
/*      */             
/* 1353 */             while (results.next()) {
/* 1354 */               String keyType = results.getString("Key");
/*      */               
/* 1356 */               if ((keyType != null) && 
/* 1357 */                 (StringUtils.startsWithIgnoreCase(keyType, "PRI")))
/*      */               {
/* 1359 */                 byte[][] rowVal = new byte[8][];
/* 1360 */                 rowVal[0] = Integer.toString(2).getBytes();
/*      */                 
/*      */ 
/*      */ 
/* 1364 */                 rowVal[1] = results.getBytes("Field");
/*      */                 
/* 1366 */                 String type = results.getString("Type");
/* 1367 */                 int size = MysqlIO.getMaxBuf();
/* 1368 */                 int decimals = 0;
/*      */                 
/*      */ 
/*      */ 
/*      */ 
/* 1373 */                 if (type.indexOf("enum") != -1) {
/* 1374 */                   String temp = type.substring(type.indexOf("("), type.indexOf(")"));
/*      */                   
/*      */ 
/* 1377 */                   StringTokenizer tokenizer = new StringTokenizer(temp, ",");
/*      */                   
/* 1379 */                   int maxLength = 0;
/*      */                   
/* 1381 */                   while (tokenizer.hasMoreTokens()) {
/* 1382 */                     maxLength = Math.max(maxLength, tokenizer.nextToken().length() - 2);
/*      */                   }
/*      */                   
/*      */ 
/*      */ 
/* 1387 */                   size = maxLength;
/* 1388 */                   decimals = 0;
/* 1389 */                   type = "enum";
/* 1390 */                 } else if (type.indexOf("(") != -1) {
/* 1391 */                   if (type.indexOf(",") != -1) {
/* 1392 */                     size = Integer.parseInt(type.substring(type.indexOf("(") + 1, type.indexOf(",")));
/*      */                     
/*      */ 
/*      */ 
/* 1396 */                     decimals = Integer.parseInt(type.substring(type.indexOf(",") + 1, type.indexOf(")")));
/*      */ 
/*      */                   }
/*      */                   else
/*      */                   {
/* 1401 */                     size = Integer.parseInt(type.substring(type.indexOf("(") + 1, type.indexOf(")")));
/*      */                   }
/*      */                   
/*      */ 
/*      */ 
/*      */ 
/* 1407 */                   type = type.substring(0, type.indexOf("("));
/*      */                 }
/*      */                 
/*      */ 
/* 1411 */                 rowVal[2] = DatabaseMetaData.this.s2b(String.valueOf(MysqlDefs.mysqlToJavaType(type)));
/*      */                 
/* 1413 */                 rowVal[3] = DatabaseMetaData.this.s2b(type);
/* 1414 */                 rowVal[4] = Integer.toString(size + decimals).getBytes();
/*      */                 
/* 1416 */                 rowVal[5] = Integer.toString(size + decimals).getBytes();
/*      */                 
/* 1418 */                 rowVal[6] = Integer.toString(decimals).getBytes();
/*      */                 
/* 1420 */                 rowVal[7] = Integer.toString(1).getBytes();
/*      */                 
/*      */ 
/*      */ 
/*      */ 
/* 1425 */                 this.val$rows.add(rowVal);
/*      */               }
/*      */             }
/*      */           }
/*      */           finally
/*      */           {
/* 1431 */             if (results != null) {
/*      */               try {
/* 1433 */                 results.close();
/*      */               }
/*      */               catch (Exception ex) {}
/*      */               
/*      */ 
/* 1438 */               results = null;
/*      */             }
/*      */           }
/*      */         }
/*      */       }.doForAll();
/*      */     } finally {
/* 1444 */       if (stmt != null) {
/* 1445 */         stmt.close();
/*      */       }
/*      */     }
/*      */     
/* 1449 */     java.sql.ResultSet results = buildResultSet(fields, rows);
/*      */     
/* 1451 */     return results;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   private void getCallStmtParameterTypes(String catalog, String procName, String parameterNamePattern, List resultRows)
/*      */     throws SQLException
/*      */   {
/* 1489 */     Statement paramRetrievalStmt = null;
/* 1490 */     java.sql.ResultSet paramRetrievalRs = null;
/*      */     
/* 1492 */     if (parameterNamePattern == null) {
/* 1493 */       if (this.conn.getNullNamePatternMatchesAll()) {
/* 1494 */         parameterNamePattern = "%";
/*      */       } else {
/* 1496 */         throw SQLError.createSQLException("Parameter/Column name pattern can not be NULL or empty.", "S1009");
/*      */       }
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/* 1503 */     byte[] procNameAsBytes = null;
/*      */     try
/*      */     {
/* 1506 */       procNameAsBytes = procName.getBytes("UTF-8");
/*      */     } catch (UnsupportedEncodingException ueEx) {
/* 1508 */       procNameAsBytes = s2b(procName);
/*      */     }
/*      */     
/* 1511 */     String quoteChar = getIdentifierQuoteString();
/*      */     
/* 1513 */     String parameterDef = null;
/*      */     
/* 1515 */     boolean isProcedureInAnsiMode = false;
/* 1516 */     String storageDefnDelims = null;
/* 1517 */     String storageDefnClosures = null;
/*      */     try
/*      */     {
/* 1520 */       paramRetrievalStmt = this.conn.getMetadataSafeStatement();
/*      */       
/* 1522 */       if ((this.conn.lowerCaseTableNames()) && (catalog != null) && (catalog.length() != 0))
/*      */       {
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 1528 */         String oldCatalog = this.conn.getCatalog();
/* 1529 */         java.sql.ResultSet rs = null;
/*      */         try
/*      */         {
/* 1532 */           this.conn.setCatalog(catalog);
/* 1533 */           rs = paramRetrievalStmt.executeQuery("SELECT DATABASE()");
/* 1534 */           rs.next();
/*      */           
/* 1536 */           catalog = rs.getString(1);
/*      */         }
/*      */         finally
/*      */         {
/* 1540 */           this.conn.setCatalog(oldCatalog);
/*      */           
/* 1542 */           if (rs != null) {
/* 1543 */             rs.close();
/*      */           }
/*      */         }
/*      */       }
/*      */       
/* 1548 */       if (paramRetrievalStmt.getMaxRows() != 0) {
/* 1549 */         paramRetrievalStmt.setMaxRows(0);
/*      */       }
/*      */       
/* 1552 */       int dotIndex = -1;
/*      */       
/* 1554 */       if (!" ".equals(quoteChar)) {
/* 1555 */         dotIndex = StringUtils.indexOfIgnoreCaseRespectQuotes(0, procName, ".", quoteChar.charAt(0), !this.conn.isNoBackslashEscapesSet());
/*      */       }
/*      */       else
/*      */       {
/* 1559 */         dotIndex = procName.indexOf(".");
/*      */       }
/*      */       
/* 1562 */       String dbName = null;
/*      */       
/* 1564 */       if ((dotIndex != -1) && (dotIndex + 1 < procName.length())) {
/* 1565 */         dbName = procName.substring(0, dotIndex);
/* 1566 */         procName = procName.substring(dotIndex + 1);
/*      */       } else {
/* 1568 */         dbName = catalog;
/*      */       }
/*      */       
/* 1571 */       StringBuffer procNameBuf = new StringBuffer();
/*      */       
/* 1573 */       if (dbName != null) {
/* 1574 */         if ((!" ".equals(quoteChar)) && (!dbName.startsWith(quoteChar))) {
/* 1575 */           procNameBuf.append(quoteChar);
/*      */         }
/*      */         
/* 1578 */         procNameBuf.append(dbName);
/*      */         
/* 1580 */         if ((!" ".equals(quoteChar)) && (!dbName.startsWith(quoteChar))) {
/* 1581 */           procNameBuf.append(quoteChar);
/*      */         }
/*      */         
/* 1584 */         procNameBuf.append(".");
/*      */       }
/*      */       
/* 1587 */       boolean procNameIsNotQuoted = !procName.startsWith(quoteChar);
/*      */       
/* 1589 */       if ((!" ".equals(quoteChar)) && (procNameIsNotQuoted)) {
/* 1590 */         procNameBuf.append(quoteChar);
/*      */       }
/*      */       
/* 1593 */       procNameBuf.append(procName);
/*      */       
/* 1595 */       if ((!" ".equals(quoteChar)) && (procNameIsNotQuoted)) {
/* 1596 */         procNameBuf.append(quoteChar);
/*      */       }
/*      */       
/* 1599 */       boolean parsingFunction = false;
/*      */       try
/*      */       {
/* 1602 */         paramRetrievalRs = paramRetrievalStmt.executeQuery("SHOW CREATE PROCEDURE " + procNameBuf.toString());
/*      */         
/*      */ 
/* 1605 */         parsingFunction = false;
/*      */       } catch (SQLException sqlEx) {
/* 1607 */         paramRetrievalRs = paramRetrievalStmt.executeQuery("SHOW CREATE FUNCTION " + procNameBuf.toString());
/*      */         
/*      */ 
/* 1610 */         parsingFunction = true;
/*      */       }
/*      */       
/* 1613 */       if (paramRetrievalRs.next()) {
/* 1614 */         String procedureDef = parsingFunction ? paramRetrievalRs.getString("Create Function") : paramRetrievalRs.getString("Create Procedure");
/*      */         
/*      */ 
/*      */ 
/* 1618 */         if ((procedureDef == null) || (procedureDef.length() == 0)) {
/* 1619 */           throw SQLError.createSQLException("User does not have access to metadata required to determine stored procedure parameter types. If rights can not be granted, configure connection with \"noAccessToProcedureBodies=true\" to have driver generate parameters that represent INOUT strings irregardless of actual parameter types.", "S1000");
/*      */         }
/*      */         
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */         try
/*      */         {
/* 1628 */           String sqlMode = paramRetrievalRs.getString("sql_mode");
/*      */           
/* 1630 */           if (StringUtils.indexOfIgnoreCase(sqlMode, "ANSI") != -1) {
/* 1631 */             isProcedureInAnsiMode = true;
/*      */           }
/*      */         }
/*      */         catch (SQLException sqlEx) {}
/*      */         
/*      */ 
/* 1637 */         String identifierMarkers = isProcedureInAnsiMode ? "`\"" : "`";
/* 1638 */         String identifierAndStringMarkers = "'" + identifierMarkers;
/* 1639 */         storageDefnDelims = "(" + identifierMarkers;
/* 1640 */         storageDefnClosures = ")" + identifierMarkers;
/*      */         
/*      */ 
/* 1643 */         procedureDef = StringUtils.stripComments(procedureDef, identifierAndStringMarkers, identifierAndStringMarkers, true, false, true, true);
/*      */         
/*      */ 
/* 1646 */         int openParenIndex = StringUtils.indexOfIgnoreCaseRespectQuotes(0, procedureDef, "(", quoteChar.charAt(0), !this.conn.isNoBackslashEscapesSet());
/*      */         
/*      */ 
/*      */ 
/* 1650 */         int endOfParamDeclarationIndex = 0;
/*      */         
/* 1652 */         endOfParamDeclarationIndex = endPositionOfParameterDeclaration(openParenIndex, procedureDef, quoteChar);
/*      */         
/*      */ 
/* 1655 */         if (parsingFunction)
/*      */         {
/*      */ 
/*      */ 
/* 1659 */           int returnsIndex = StringUtils.indexOfIgnoreCaseRespectQuotes(0, procedureDef, " RETURNS ", quoteChar.charAt(0), !this.conn.isNoBackslashEscapesSet());
/*      */           
/*      */ 
/*      */ 
/*      */ 
/* 1664 */           int endReturnsDef = findEndOfReturnsClause(procedureDef, quoteChar, returnsIndex);
/*      */           
/*      */ 
/*      */ 
/*      */ 
/* 1669 */           int declarationStart = returnsIndex + "RETURNS ".length();
/*      */           
/* 1671 */           while ((declarationStart < procedureDef.length()) && 
/* 1672 */             (Character.isWhitespace(procedureDef.charAt(declarationStart)))) {
/* 1673 */             declarationStart++;
/*      */           }
/*      */           
/*      */ 
/*      */ 
/*      */ 
/* 1679 */           String returnsDefn = procedureDef.substring(declarationStart, endReturnsDef).trim();
/* 1680 */           TypeDescriptor returnDescriptor = new TypeDescriptor(returnsDefn, null);
/*      */           
/*      */ 
/* 1683 */           resultRows.add(convertTypeDescriptorToProcedureRow(procNameAsBytes, "", false, false, true, returnDescriptor));
/*      */         }
/*      */         
/*      */ 
/*      */ 
/* 1688 */         if ((openParenIndex == -1) || (endOfParamDeclarationIndex == -1))
/*      */         {
/*      */ 
/* 1691 */           throw SQLError.createSQLException("Internal error when parsing callable statement metadata", "S1000");
/*      */         }
/*      */         
/*      */ 
/*      */ 
/*      */ 
/* 1697 */         parameterDef = procedureDef.substring(openParenIndex + 1, endOfParamDeclarationIndex);
/*      */       }
/*      */     }
/*      */     finally {
/* 1701 */       SQLException sqlExRethrow = null;
/*      */       
/* 1703 */       if (paramRetrievalRs != null) {
/*      */         try {
/* 1705 */           paramRetrievalRs.close();
/*      */         } catch (SQLException sqlEx) {
/* 1707 */           sqlExRethrow = sqlEx;
/*      */         }
/*      */         
/* 1710 */         paramRetrievalRs = null;
/*      */       }
/*      */       
/* 1713 */       if (paramRetrievalStmt != null) {
/*      */         try {
/* 1715 */           paramRetrievalStmt.close();
/*      */         } catch (SQLException sqlEx) {
/* 1717 */           sqlExRethrow = sqlEx;
/*      */         }
/*      */         
/* 1720 */         paramRetrievalStmt = null;
/*      */       }
/*      */       
/* 1723 */       if (sqlExRethrow != null) {
/* 1724 */         throw sqlExRethrow;
/*      */       }
/*      */     }
/*      */     
/* 1728 */     if (parameterDef != null)
/*      */     {
/* 1730 */       List parseList = StringUtils.split(parameterDef, ",", storageDefnDelims, storageDefnClosures, true);
/*      */       
/*      */ 
/* 1733 */       int parseListLen = parseList.size();
/*      */       
/* 1735 */       for (int i = 0; i < parseListLen; i++) {
/* 1736 */         String declaration = (String)parseList.get(i);
/*      */         
/* 1738 */         if (declaration.trim().length() == 0) {
/*      */           break;
/*      */         }
/*      */         
/*      */ 
/* 1743 */         StringTokenizer declarationTok = new StringTokenizer(declaration, " \t");
/*      */         
/*      */ 
/* 1746 */         String paramName = null;
/* 1747 */         boolean isOutParam = false;
/* 1748 */         boolean isInParam = false;
/*      */         
/* 1750 */         if (declarationTok.hasMoreTokens()) {
/* 1751 */           String possibleParamName = declarationTok.nextToken();
/*      */           
/* 1753 */           if (possibleParamName.equalsIgnoreCase("OUT")) {
/* 1754 */             isOutParam = true;
/*      */             
/* 1756 */             if (declarationTok.hasMoreTokens()) {
/* 1757 */               paramName = declarationTok.nextToken();
/*      */             } else {
/* 1759 */               throw SQLError.createSQLException("Internal error when parsing callable statement metadata (missing parameter name)", "S1000");
/*      */             }
/*      */             
/*      */ 
/*      */           }
/* 1764 */           else if (possibleParamName.equalsIgnoreCase("INOUT")) {
/* 1765 */             isOutParam = true;
/* 1766 */             isInParam = true;
/*      */             
/* 1768 */             if (declarationTok.hasMoreTokens()) {
/* 1769 */               paramName = declarationTok.nextToken();
/*      */             } else {
/* 1771 */               throw SQLError.createSQLException("Internal error when parsing callable statement metadata (missing parameter name)", "S1000");
/*      */             }
/*      */             
/*      */ 
/*      */           }
/* 1776 */           else if (possibleParamName.equalsIgnoreCase("IN")) {
/* 1777 */             isOutParam = false;
/* 1778 */             isInParam = true;
/*      */             
/* 1780 */             if (declarationTok.hasMoreTokens()) {
/* 1781 */               paramName = declarationTok.nextToken();
/*      */             } else {
/* 1783 */               throw SQLError.createSQLException("Internal error when parsing callable statement metadata (missing parameter name)", "S1000");
/*      */             }
/*      */             
/*      */           }
/*      */           else
/*      */           {
/* 1789 */             isOutParam = false;
/* 1790 */             isInParam = true;
/*      */             
/* 1792 */             paramName = possibleParamName;
/*      */           }
/*      */           
/* 1795 */           TypeDescriptor typeDesc = null;
/*      */           
/* 1797 */           if (declarationTok.hasMoreTokens()) {
/* 1798 */             StringBuffer typeInfoBuf = new StringBuffer(declarationTok.nextToken());
/*      */             
/*      */ 
/* 1801 */             while (declarationTok.hasMoreTokens()) {
/* 1802 */               typeInfoBuf.append(" ");
/* 1803 */               typeInfoBuf.append(declarationTok.nextToken());
/*      */             }
/*      */             
/* 1806 */             String typeInfo = typeInfoBuf.toString();
/*      */             
/* 1808 */             typeDesc = new TypeDescriptor(typeInfo, null);
/*      */           } else {
/* 1810 */             throw SQLError.createSQLException("Internal error when parsing callable statement metadata (missing parameter type)", "S1000");
/*      */           }
/*      */           
/*      */ 
/*      */ 
/*      */ 
/* 1816 */           if (((paramName.startsWith("`")) && (paramName.endsWith("`"))) || ((isProcedureInAnsiMode) && (paramName.startsWith("\"")) && (paramName.endsWith("\""))))
/*      */           {
/* 1818 */             paramName = paramName.substring(1, paramName.length() - 1);
/*      */           }
/*      */           
/* 1821 */           int wildCompareRes = StringUtils.wildCompare(paramName, parameterNamePattern);
/*      */           
/*      */ 
/* 1824 */           if (wildCompareRes != -1) {
/* 1825 */             byte[][] row = convertTypeDescriptorToProcedureRow(procNameAsBytes, paramName, isOutParam, isInParam, false, typeDesc);
/*      */             
/*      */ 
/*      */ 
/* 1829 */             resultRows.add(row);
/*      */           }
/*      */         } else {
/* 1832 */           throw SQLError.createSQLException("Internal error when parsing callable statement metadata (unknown output from 'SHOW CREATE PROCEDURE')", "S1000");
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
/*      */   private int getCascadeDeleteOption(String cascadeOptions)
/*      */   {
/* 1854 */     int onDeletePos = cascadeOptions.indexOf("ON DELETE");
/*      */     
/* 1856 */     if (onDeletePos != -1) {
/* 1857 */       String deleteOptions = cascadeOptions.substring(onDeletePos, cascadeOptions.length());
/*      */       
/*      */ 
/* 1860 */       if (deleteOptions.startsWith("ON DELETE CASCADE"))
/* 1861 */         return 0;
/* 1862 */       if (deleteOptions.startsWith("ON DELETE SET NULL"))
/* 1863 */         return 2;
/* 1864 */       if (deleteOptions.startsWith("ON DELETE RESTRICT"))
/* 1865 */         return 1;
/* 1866 */       if (deleteOptions.startsWith("ON DELETE NO ACTION")) {
/* 1867 */         return 3;
/*      */       }
/*      */     }
/*      */     
/* 1871 */     return 3;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   private int getCascadeUpdateOption(String cascadeOptions)
/*      */   {
/* 1883 */     int onUpdatePos = cascadeOptions.indexOf("ON UPDATE");
/*      */     
/* 1885 */     if (onUpdatePos != -1) {
/* 1886 */       String updateOptions = cascadeOptions.substring(onUpdatePos, cascadeOptions.length());
/*      */       
/*      */ 
/* 1889 */       if (updateOptions.startsWith("ON UPDATE CASCADE"))
/* 1890 */         return 0;
/* 1891 */       if (updateOptions.startsWith("ON UPDATE SET NULL"))
/* 1892 */         return 2;
/* 1893 */       if (updateOptions.startsWith("ON UPDATE RESTRICT"))
/* 1894 */         return 1;
/* 1895 */       if (updateOptions.startsWith("ON UPDATE NO ACTION")) {
/* 1896 */         return 3;
/*      */       }
/*      */     }
/*      */     
/* 1900 */     return 3;
/*      */   }
/*      */   
/*      */   protected IteratorWithCleanup getCatalogIterator(String catalogSpec) throws SQLException {
/*      */     IteratorWithCleanup allCatalogsIter;
/*      */     IteratorWithCleanup allCatalogsIter;
/* 1906 */     if (catalogSpec != null) { IteratorWithCleanup allCatalogsIter;
/* 1907 */       if (!catalogSpec.equals("")) {
/* 1908 */         allCatalogsIter = new SingleStringIterator(catalogSpec);
/*      */       }
/*      */       else
/* 1911 */         allCatalogsIter = new SingleStringIterator(this.database);
/*      */     } else { IteratorWithCleanup allCatalogsIter;
/* 1913 */       if (this.conn.getNullCatalogMeansCurrent()) {
/* 1914 */         allCatalogsIter = new SingleStringIterator(this.database);
/*      */       } else {
/* 1916 */         allCatalogsIter = new ResultSetIterator(getCatalogs(), 1);
/*      */       }
/*      */     }
/* 1919 */     return allCatalogsIter;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public java.sql.ResultSet getCatalogs()
/*      */     throws SQLException
/*      */   {
/* 1938 */     java.sql.ResultSet results = null;
/* 1939 */     Statement stmt = null;
/*      */     try
/*      */     {
/* 1942 */       stmt = this.conn.createStatement();
/* 1943 */       stmt.setEscapeProcessing(false);
/* 1944 */       results = stmt.executeQuery("SHOW DATABASES");
/*      */       
/* 1946 */       ResultSetMetaData resultsMD = results.getMetaData();
/* 1947 */       Field[] fields = new Field[1];
/* 1948 */       fields[0] = new Field("", "TABLE_CAT", 12, resultsMD.getColumnDisplaySize(1));
/*      */       
/*      */ 
/* 1951 */       ArrayList tuples = new ArrayList();
/*      */       byte[][] rowVal;
/* 1953 */       while (results.next()) {
/* 1954 */         rowVal = new byte[1][];
/* 1955 */         rowVal[0] = results.getBytes(1);
/* 1956 */         tuples.add(rowVal);
/*      */       }
/*      */       
/* 1959 */       return buildResultSet(fields, tuples);
/*      */     } finally {
/* 1961 */       if (results != null) {
/*      */         try {
/* 1963 */           results.close();
/*      */         } catch (SQLException sqlEx) {
/* 1965 */           AssertionFailedException.shouldNotHappen(sqlEx);
/*      */         }
/*      */         
/* 1968 */         results = null;
/*      */       }
/*      */       
/* 1971 */       if (stmt != null) {
/*      */         try {
/* 1973 */           stmt.close();
/*      */         } catch (SQLException sqlEx) {
/* 1975 */           AssertionFailedException.shouldNotHappen(sqlEx);
/*      */         }
/*      */         
/* 1978 */         stmt = null;
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public String getCatalogSeparator()
/*      */     throws SQLException
/*      */   {
/* 1991 */     return ".";
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public String getCatalogTerm()
/*      */     throws SQLException
/*      */   {
/* 2008 */     return "database";
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public java.sql.ResultSet getColumnPrivileges(String catalog, String schema, String table, String columnNamePattern)
/*      */     throws SQLException
/*      */   {
/* 2049 */     Field[] fields = new Field[8];
/* 2050 */     fields[0] = new Field("", "TABLE_CAT", 1, 64);
/* 2051 */     fields[1] = new Field("", "TABLE_SCHEM", 1, 1);
/* 2052 */     fields[2] = new Field("", "TABLE_NAME", 1, 64);
/* 2053 */     fields[3] = new Field("", "COLUMN_NAME", 1, 64);
/* 2054 */     fields[4] = new Field("", "GRANTOR", 1, 77);
/* 2055 */     fields[5] = new Field("", "GRANTEE", 1, 77);
/* 2056 */     fields[6] = new Field("", "PRIVILEGE", 1, 64);
/* 2057 */     fields[7] = new Field("", "IS_GRANTABLE", 1, 3);
/*      */     
/* 2059 */     StringBuffer grantQuery = new StringBuffer("SELECT c.host, c.db, t.grantor, c.user, c.table_name, c.column_name, c.column_priv from mysql.columns_priv c, mysql.tables_priv t where c.host = t.host and c.db = t.db and c.table_name = t.table_name ");
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 2066 */     if ((catalog != null) && (catalog.length() != 0)) {
/* 2067 */       grantQuery.append(" AND c.db='");
/* 2068 */       grantQuery.append(catalog);
/* 2069 */       grantQuery.append("' ");
/*      */     }
/*      */     
/*      */ 
/* 2073 */     grantQuery.append(" AND c.table_name ='");
/* 2074 */     grantQuery.append(table);
/* 2075 */     grantQuery.append("' AND c.column_name like '");
/* 2076 */     grantQuery.append(columnNamePattern);
/* 2077 */     grantQuery.append("'");
/*      */     
/* 2079 */     Statement stmt = null;
/* 2080 */     java.sql.ResultSet results = null;
/* 2081 */     ArrayList grantRows = new ArrayList();
/*      */     try
/*      */     {
/* 2084 */       stmt = this.conn.createStatement();
/* 2085 */       stmt.setEscapeProcessing(false);
/* 2086 */       results = stmt.executeQuery(grantQuery.toString());
/*      */       
/* 2088 */       while (results.next()) {
/* 2089 */         String host = results.getString(1);
/* 2090 */         String db = results.getString(2);
/* 2091 */         String grantor = results.getString(3);
/* 2092 */         String user = results.getString(4);
/*      */         
/* 2094 */         if ((user == null) || (user.length() == 0)) {
/* 2095 */           user = "%";
/*      */         }
/*      */         
/* 2098 */         StringBuffer fullUser = new StringBuffer(user);
/*      */         
/* 2100 */         if ((host != null) && (this.conn.getUseHostsInPrivileges())) {
/* 2101 */           fullUser.append("@");
/* 2102 */           fullUser.append(host);
/*      */         }
/*      */         
/* 2105 */         String columnName = results.getString(6);
/* 2106 */         String allPrivileges = results.getString(7);
/*      */         
/* 2108 */         if (allPrivileges != null) {
/* 2109 */           allPrivileges = allPrivileges.toUpperCase(Locale.ENGLISH);
/*      */           
/* 2111 */           StringTokenizer st = new StringTokenizer(allPrivileges, ",");
/*      */           
/* 2113 */           while (st.hasMoreTokens()) {
/* 2114 */             String privilege = st.nextToken().trim();
/* 2115 */             byte[][] tuple = new byte[8][];
/* 2116 */             tuple[0] = s2b(db);
/* 2117 */             tuple[1] = null;
/* 2118 */             tuple[2] = s2b(table);
/* 2119 */             tuple[3] = s2b(columnName);
/*      */             
/* 2121 */             if (grantor != null) {
/* 2122 */               tuple[4] = s2b(grantor);
/*      */             } else {
/* 2124 */               tuple[4] = null;
/*      */             }
/*      */             
/* 2127 */             tuple[5] = s2b(fullUser.toString());
/* 2128 */             tuple[6] = s2b(privilege);
/* 2129 */             tuple[7] = null;
/* 2130 */             grantRows.add(tuple);
/*      */           }
/*      */         }
/*      */       }
/*      */     } finally {
/* 2135 */       if (results != null) {
/*      */         try {
/* 2137 */           results.close();
/*      */         }
/*      */         catch (Exception ex) {}
/*      */         
/*      */ 
/* 2142 */         results = null;
/*      */       }
/*      */       
/* 2145 */       if (stmt != null) {
/*      */         try {
/* 2147 */           stmt.close();
/*      */         }
/*      */         catch (Exception ex) {}
/*      */         
/*      */ 
/* 2152 */         stmt = null;
/*      */       }
/*      */     }
/*      */     
/* 2156 */     return buildResultSet(fields, grantRows);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   private final Statement val$stmt;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   private final ArrayList val$rows;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public java.sql.ResultSet getColumns(String catalog, String schemaPattern, String tableNamePattern, String columnNamePattern)
/*      */     throws SQLException
/*      */   {
/* 2220 */     if (columnNamePattern == null) {
/* 2221 */       if (this.conn.getNullNamePatternMatchesAll()) {
/* 2222 */         columnNamePattern = "%";
/*      */       } else {
/* 2224 */         throw SQLError.createSQLException("Column name pattern can not be NULL or empty.", "S1009");
/*      */       }
/*      */     }
/*      */     
/*      */ 
/*      */ 
/* 2230 */     String colPattern = columnNamePattern;
/*      */     
/* 2232 */     Field[] fields = new Field[23];
/* 2233 */     fields[0] = new Field("", "TABLE_CAT", 1, 255);
/* 2234 */     fields[1] = new Field("", "TABLE_SCHEM", 1, 0);
/* 2235 */     fields[2] = new Field("", "TABLE_NAME", 1, 255);
/* 2236 */     fields[3] = new Field("", "COLUMN_NAME", 1, 32);
/* 2237 */     fields[4] = new Field("", "DATA_TYPE", 5, 5);
/* 2238 */     fields[5] = new Field("", "TYPE_NAME", 1, 16);
/* 2239 */     fields[6] = new Field("", "COLUMN_SIZE", 4, Integer.toString(Integer.MAX_VALUE).length());
/*      */     
/* 2241 */     fields[7] = new Field("", "BUFFER_LENGTH", 4, 10);
/* 2242 */     fields[8] = new Field("", "DECIMAL_DIGITS", 4, 10);
/* 2243 */     fields[9] = new Field("", "NUM_PREC_RADIX", 4, 10);
/* 2244 */     fields[10] = new Field("", "NULLABLE", 4, 10);
/* 2245 */     fields[11] = new Field("", "REMARKS", 1, 0);
/* 2246 */     fields[12] = new Field("", "COLUMN_DEF", 1, 0);
/* 2247 */     fields[13] = new Field("", "SQL_DATA_TYPE", 4, 10);
/* 2248 */     fields[14] = new Field("", "SQL_DATETIME_SUB", 4, 10);
/* 2249 */     fields[15] = new Field("", "CHAR_OCTET_LENGTH", 4, Integer.toString(Integer.MAX_VALUE).length());
/*      */     
/* 2251 */     fields[16] = new Field("", "ORDINAL_POSITION", 4, 10);
/* 2252 */     fields[17] = new Field("", "IS_NULLABLE", 1, 3);
/* 2253 */     fields[18] = new Field("", "SCOPE_CATALOG", 1, 255);
/* 2254 */     fields[19] = new Field("", "SCOPE_SCHEMA", 1, 255);
/* 2255 */     fields[20] = new Field("", "SCOPE_TABLE", 1, 255);
/* 2256 */     fields[21] = new Field("", "SOURCE_DATA_TYPE", 5, 10);
/* 2257 */     fields[22] = new Field("", "IS_AUTOINCREMENT", 1, 3);
/*      */     
/* 2259 */     ArrayList rows = new ArrayList();
/* 2260 */     Statement stmt = this.conn.getMetadataSafeStatement();
/*      */     
/*      */     try
/*      */     {
/* 2264 */       new IterateBlock(getCatalogIterator(catalog), tableNamePattern) {
/*      */         private final String val$tableNamePattern;
/*      */         
/* 2267 */         void forEach(Object catalogStr) throws SQLException { ArrayList tableNameList = new ArrayList();
/*      */           
/* 2269 */           if (this.val$tableNamePattern == null)
/*      */           {
/* 2271 */             java.sql.ResultSet tables = null;
/*      */             try
/*      */             {
/* 2274 */               tables = DatabaseMetaData.this.getTables(this.val$catalog, this.val$schemaPattern, "%", new String[0]);
/*      */               
/*      */ 
/* 2277 */               while (tables.next()) {
/* 2278 */                 String tableNameFromList = tables.getString("TABLE_NAME");
/*      */                 
/* 2280 */                 tableNameList.add(tableNameFromList);
/*      */               }
/*      */             } finally {
/* 2283 */               if (tables != null) {
/*      */                 try {
/* 2285 */                   tables.close();
/*      */                 } catch (Exception sqlEx) {
/* 2287 */                   AssertionFailedException.shouldNotHappen(sqlEx);
/*      */                 }
/*      */                 
/*      */ 
/* 2291 */                 tables = null;
/*      */               }
/*      */             }
/*      */           } else {
/* 2295 */             java.sql.ResultSet tables = null;
/*      */             try
/*      */             {
/* 2298 */               tables = DatabaseMetaData.this.getTables(this.val$catalog, this.val$schemaPattern, this.val$tableNamePattern, new String[0]);
/*      */               
/*      */ 
/* 2301 */               while (tables.next()) {
/* 2302 */                 String tableNameFromList = tables.getString("TABLE_NAME");
/*      */                 
/* 2304 */                 tableNameList.add(tableNameFromList);
/*      */               }
/*      */             } finally {
/* 2307 */               if (tables != null) {
/*      */                 try {
/* 2309 */                   tables.close();
/*      */                 } catch (SQLException sqlEx) {
/* 2311 */                   AssertionFailedException.shouldNotHappen(sqlEx);
/*      */                 }
/*      */                 
/*      */ 
/* 2315 */                 tables = null;
/*      */               }
/*      */             }
/*      */           }
/*      */           
/* 2320 */           Iterator tableNames = tableNameList.iterator();
/*      */           
/* 2322 */           while (tableNames.hasNext()) {
/* 2323 */             String tableName = (String)tableNames.next();
/*      */             
/* 2325 */             java.sql.ResultSet results = null;
/*      */             try
/*      */             {
/* 2328 */               StringBuffer queryBuf = new StringBuffer("SHOW ");
/*      */               
/* 2330 */               if (DatabaseMetaData.this.conn.versionMeetsMinimum(4, 1, 0)) {
/* 2331 */                 queryBuf.append("FULL ");
/*      */               }
/*      */               
/* 2334 */               queryBuf.append("COLUMNS FROM ");
/* 2335 */               queryBuf.append(DatabaseMetaData.this.quotedId);
/* 2336 */               queryBuf.append(tableName);
/* 2337 */               queryBuf.append(DatabaseMetaData.this.quotedId);
/* 2338 */               queryBuf.append(" FROM ");
/* 2339 */               queryBuf.append(DatabaseMetaData.this.quotedId);
/* 2340 */               queryBuf.append(catalogStr.toString());
/* 2341 */               queryBuf.append(DatabaseMetaData.this.quotedId);
/* 2342 */               queryBuf.append(" LIKE '");
/* 2343 */               queryBuf.append(this.val$colPattern);
/* 2344 */               queryBuf.append("'");
/*      */               
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 2351 */               boolean fixUpOrdinalsRequired = false;
/* 2352 */               Object ordinalFixUpMap = null;
/*      */               
/* 2354 */               if (!this.val$colPattern.equals("%")) {
/* 2355 */                 fixUpOrdinalsRequired = true;
/*      */                 
/* 2357 */                 StringBuffer fullColumnQueryBuf = new StringBuffer("SHOW ");
/*      */                 
/*      */ 
/* 2360 */                 if (DatabaseMetaData.this.conn.versionMeetsMinimum(4, 1, 0)) {
/* 2361 */                   fullColumnQueryBuf.append("FULL ");
/*      */                 }
/*      */                 
/* 2364 */                 fullColumnQueryBuf.append("COLUMNS FROM ");
/* 2365 */                 fullColumnQueryBuf.append(DatabaseMetaData.this.quotedId);
/* 2366 */                 fullColumnQueryBuf.append(tableName);
/* 2367 */                 fullColumnQueryBuf.append(DatabaseMetaData.this.quotedId);
/* 2368 */                 fullColumnQueryBuf.append(" FROM ");
/* 2369 */                 fullColumnQueryBuf.append(DatabaseMetaData.this.quotedId);
/* 2370 */                 fullColumnQueryBuf.append(catalogStr.toString());
/*      */                 
/* 2372 */                 fullColumnQueryBuf.append(DatabaseMetaData.this.quotedId);
/*      */                 
/* 2374 */                 results = this.val$stmt.executeQuery(fullColumnQueryBuf.toString());
/*      */                 
/*      */ 
/* 2377 */                 ordinalFixUpMap = new HashMap();
/*      */                 
/* 2379 */                 int fullOrdinalPos = 1;
/*      */                 
/* 2381 */                 while (results.next()) {
/* 2382 */                   String fullOrdColName = results.getString("Field");
/*      */                   
/*      */ 
/* 2385 */                   ((Map)ordinalFixUpMap).put(fullOrdColName, new Integer(fullOrdinalPos++));
/*      */                 }
/*      */               }
/*      */               
/*      */ 
/* 2390 */               results = this.val$stmt.executeQuery(queryBuf.toString());
/*      */               
/* 2392 */               int ordPos = 1;
/*      */               
/* 2394 */               while (results.next()) {
/* 2395 */                 byte[][] rowVal = new byte[23][];
/* 2396 */                 rowVal[0] = DatabaseMetaData.this.s2b(this.val$catalog);
/* 2397 */                 rowVal[1] = null;
/*      */                 
/*      */ 
/* 2400 */                 rowVal[2] = DatabaseMetaData.this.s2b(tableName);
/* 2401 */                 rowVal[3] = results.getBytes("Field");
/*      */                 
/* 2403 */                 DatabaseMetaData.TypeDescriptor typeDesc = new DatabaseMetaData.TypeDescriptor(DatabaseMetaData.this, results.getString("Type"), results.getString("Null"));
/*      */                 
/*      */ 
/*      */ 
/* 2407 */                 rowVal[4] = Short.toString(typeDesc.dataType).getBytes();
/*      */                 
/*      */ 
/*      */ 
/* 2411 */                 rowVal[5] = DatabaseMetaData.this.s2b(typeDesc.typeName);
/*      */                 
/* 2413 */                 rowVal[6] = (typeDesc.columnSize == null ? null : DatabaseMetaData.this.s2b(typeDesc.columnSize.toString()));
/*      */                 
/* 2415 */                 rowVal[7] = DatabaseMetaData.this.s2b(Integer.toString(typeDesc.bufferLength));
/*      */                 
/* 2417 */                 rowVal[8] = (typeDesc.decimalDigits == null ? null : DatabaseMetaData.this.s2b(typeDesc.decimalDigits.toString()));
/*      */                 
/* 2419 */                 rowVal[9] = DatabaseMetaData.this.s2b(Integer.toString(typeDesc.numPrecRadix));
/*      */                 
/* 2421 */                 rowVal[10] = DatabaseMetaData.this.s2b(Integer.toString(typeDesc.nullability));
/*      */                 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */                 try
/*      */                 {
/* 2432 */                   if (DatabaseMetaData.this.conn.versionMeetsMinimum(4, 1, 0)) {
/* 2433 */                     rowVal[11] = results.getBytes("Comment");
/*      */                   }
/*      */                   else {
/* 2436 */                     rowVal[11] = results.getBytes("Extra");
/*      */                   }
/*      */                 } catch (Exception E) {
/* 2439 */                   rowVal[11] = new byte[0];
/*      */                 }
/*      */                 
/*      */ 
/* 2443 */                 rowVal[12] = results.getBytes("Default");
/*      */                 
/* 2445 */                 rowVal[13] = { 48 };
/* 2446 */                 rowVal[14] = { 48 };
/*      */                 
/* 2448 */                 if ((StringUtils.indexOfIgnoreCase(typeDesc.typeName, "CHAR") != -1) || (StringUtils.indexOfIgnoreCase(typeDesc.typeName, "BLOB") != -1) || (StringUtils.indexOfIgnoreCase(typeDesc.typeName, "TEXT") != -1) || (StringUtils.indexOfIgnoreCase(typeDesc.typeName, "BINARY") != -1))
/*      */                 {
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 2456 */                   rowVal[15] = rowVal[6];
/*      */                 } else {
/* 2458 */                   rowVal[15] = null;
/*      */                 }
/*      */                 
/*      */ 
/* 2462 */                 if (!fixUpOrdinalsRequired) {
/* 2463 */                   rowVal[16] = Integer.toString(ordPos++).getBytes();
/*      */                 }
/*      */                 else {
/* 2466 */                   String origColName = results.getString("Field");
/*      */                   
/* 2468 */                   Integer realOrdinal = (Integer)((Map)ordinalFixUpMap).get(origColName);
/*      */                   
/*      */ 
/* 2471 */                   if (realOrdinal != null) {
/* 2472 */                     rowVal[16] = realOrdinal.toString().getBytes();
/*      */                   }
/*      */                   else {
/* 2475 */                     throw SQLError.createSQLException("Can not find column in full column list to determine true ordinal position.", "S1000");
/*      */                   }
/*      */                 }
/*      */                 
/*      */ 
/*      */ 
/*      */ 
/* 2482 */                 rowVal[17] = DatabaseMetaData.this.s2b(typeDesc.isNullable);
/*      */                 
/*      */ 
/* 2485 */                 rowVal[18] = null;
/* 2486 */                 rowVal[19] = null;
/* 2487 */                 rowVal[20] = null;
/* 2488 */                 rowVal[21] = null;
/*      */                 
/* 2490 */                 rowVal[22] = DatabaseMetaData.this.s2b("");
/*      */                 
/* 2492 */                 String extra = results.getString("Extra");
/*      */                 
/* 2494 */                 if (extra != null) {
/* 2495 */                   rowVal[22] = DatabaseMetaData.this.s2b(StringUtils.indexOfIgnoreCase(extra, "auto_increment") != -1 ? "YES" : "NO");
/*      */                 }
/*      */                 
/*      */ 
/*      */ 
/*      */ 
/* 2501 */                 this.val$rows.add(rowVal);
/*      */               }
/*      */             } finally {
/* 2504 */               if (results != null) {
/*      */                 try {
/* 2506 */                   results.close();
/*      */                 }
/*      */                 catch (Exception ex) {}
/*      */                 
/*      */ 
/* 2511 */                 results = null;
/*      */               }
/*      */             }
/*      */           }
/*      */         }
/*      */       }.doForAll();
/*      */     } finally {
/* 2518 */       if (stmt != null) {
/* 2519 */         stmt.close();
/*      */       }
/*      */     }
/*      */     
/* 2523 */     java.sql.ResultSet results = buildResultSet(fields, rows);
/*      */     
/* 2525 */     return results;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public java.sql.Connection getConnection()
/*      */     throws SQLException
/*      */   {
/* 2536 */     return this.conn;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   private final String val$catalog;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   private final String val$schemaPattern;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   private final String val$colPattern;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   private final Statement val$stmt;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   private final ArrayList val$rows;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public java.sql.ResultSet getCrossReference(String primaryCatalog, String primarySchema, String primaryTable, String foreignCatalog, String foreignSchema, String foreignTable)
/*      */     throws SQLException
/*      */   {
/* 2610 */     if (primaryTable == null) {
/* 2611 */       throw SQLError.createSQLException("Table not specified.", "S1009");
/*      */     }
/*      */     
/*      */ 
/* 2615 */     Field[] fields = new Field[14];
/* 2616 */     fields[0] = new Field("", "PKTABLE_CAT", 1, 255);
/* 2617 */     fields[1] = new Field("", "PKTABLE_SCHEM", 1, 0);
/* 2618 */     fields[2] = new Field("", "PKTABLE_NAME", 1, 255);
/* 2619 */     fields[3] = new Field("", "PKCOLUMN_NAME", 1, 32);
/* 2620 */     fields[4] = new Field("", "FKTABLE_CAT", 1, 255);
/* 2621 */     fields[5] = new Field("", "FKTABLE_SCHEM", 1, 0);
/* 2622 */     fields[6] = new Field("", "FKTABLE_NAME", 1, 255);
/* 2623 */     fields[7] = new Field("", "FKCOLUMN_NAME", 1, 32);
/* 2624 */     fields[8] = new Field("", "KEY_SEQ", 5, 2);
/* 2625 */     fields[9] = new Field("", "UPDATE_RULE", 5, 2);
/* 2626 */     fields[10] = new Field("", "DELETE_RULE", 5, 2);
/* 2627 */     fields[11] = new Field("", "FK_NAME", 1, 0);
/* 2628 */     fields[12] = new Field("", "PK_NAME", 1, 0);
/* 2629 */     fields[13] = new Field("", "DEFERRABILITY", 4, 2);
/*      */     
/* 2631 */     ArrayList tuples = new ArrayList();
/*      */     
/* 2633 */     if (this.conn.versionMeetsMinimum(3, 23, 0))
/*      */     {
/* 2635 */       Statement stmt = this.conn.getMetadataSafeStatement();
/*      */       
/*      */       try
/*      */       {
/* 2639 */         new IterateBlock(getCatalogIterator(foreignCatalog), stmt) { private final Statement val$stmt;
/*      */           private final String val$foreignTable;
/*      */           
/* 2642 */           void forEach(Object catalogStr) throws SQLException { java.sql.ResultSet fkresults = null;
/*      */             
/*      */ 
/*      */ 
/*      */ 
/*      */             try
/*      */             {
/* 2649 */               if (DatabaseMetaData.this.conn.versionMeetsMinimum(3, 23, 50)) {
/* 2650 */                 fkresults = DatabaseMetaData.this.extractForeignKeyFromCreateTable(catalogStr.toString(), null);
/*      */               }
/*      */               else {
/* 2653 */                 StringBuffer queryBuf = new StringBuffer("SHOW TABLE STATUS FROM ");
/*      */                 
/* 2655 */                 queryBuf.append(DatabaseMetaData.this.quotedId);
/* 2656 */                 queryBuf.append(catalogStr.toString());
/* 2657 */                 queryBuf.append(DatabaseMetaData.this.quotedId);
/*      */                 
/* 2659 */                 fkresults = this.val$stmt.executeQuery(queryBuf.toString());
/*      */               }
/*      */               
/*      */ 
/* 2663 */               String foreignTableWithCase = DatabaseMetaData.this.getTableNameWithCase(this.val$foreignTable);
/* 2664 */               String primaryTableWithCase = DatabaseMetaData.this.getTableNameWithCase(this.val$primaryTable);
/*      */               
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 2672 */               while (fkresults.next()) {
/* 2673 */                 String tableType = fkresults.getString("Type");
/*      */                 
/* 2675 */                 if ((tableType != null) && ((tableType.equalsIgnoreCase("innodb")) || (tableType.equalsIgnoreCase("SUPPORTS_FK"))))
/*      */                 {
/*      */ 
/*      */ 
/* 2679 */                   String comment = fkresults.getString("Comment").trim();
/*      */                   
/*      */ 
/* 2682 */                   if (comment != null) {
/* 2683 */                     StringTokenizer commentTokens = new StringTokenizer(comment, ";", false);
/*      */                     
/*      */                     String dummy;
/* 2686 */                     if (commentTokens.hasMoreTokens()) {
/* 2687 */                       dummy = commentTokens.nextToken();
/*      */                     }
/*      */                     
/*      */ 
/*      */ 
/* 2692 */                     while (commentTokens.hasMoreTokens()) {
/* 2693 */                       String keys = commentTokens.nextToken();
/*      */                       
/* 2695 */                       DatabaseMetaData.LocalAndReferencedColumns parsedInfo = DatabaseMetaData.this.parseTableStatusIntoLocalAndReferencedColumns(keys);
/*      */                       
/* 2697 */                       int keySeq = 0;
/*      */                       
/* 2699 */                       Iterator referencingColumns = parsedInfo.localColumnsList.iterator();
/*      */                       
/* 2701 */                       Iterator referencedColumns = parsedInfo.referencedColumnsList.iterator();
/*      */                       
/*      */ 
/* 2704 */                       while (referencingColumns.hasNext()) {
/* 2705 */                         String referencingColumn = DatabaseMetaData.this.removeQuotedId(referencingColumns.next().toString());
/*      */                         
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 2711 */                         byte[][] tuple = new byte[14][];
/* 2712 */                         tuple[4] = (this.val$foreignCatalog == null ? null : DatabaseMetaData.this.s2b(this.val$foreignCatalog));
/*      */                         
/* 2714 */                         tuple[5] = (this.val$foreignSchema == null ? null : DatabaseMetaData.this.s2b(this.val$foreignSchema));
/*      */                         
/* 2716 */                         String dummy = fkresults.getString("Name");
/*      */                         
/*      */ 
/* 2719 */                         if (dummy.compareTo(foreignTableWithCase) == 0)
/*      */                         {
/*      */ 
/*      */ 
/*      */ 
/* 2724 */                           tuple[6] = DatabaseMetaData.this.s2b(dummy);
/*      */                           
/* 2726 */                           tuple[7] = DatabaseMetaData.this.s2b(referencingColumn);
/* 2727 */                           tuple[0] = (this.val$primaryCatalog == null ? null : DatabaseMetaData.this.s2b(this.val$primaryCatalog));
/*      */                           
/* 2729 */                           tuple[1] = (this.val$primarySchema == null ? null : DatabaseMetaData.this.s2b(this.val$primarySchema));
/*      */                           
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 2735 */                           if (parsedInfo.referencedTable.compareTo(primaryTableWithCase) == 0)
/*      */                           {
/*      */ 
/*      */ 
/*      */ 
/* 2740 */                             tuple[2] = DatabaseMetaData.this.s2b(parsedInfo.referencedTable);
/* 2741 */                             tuple[3] = DatabaseMetaData.this.s2b(DatabaseMetaData.access$300(DatabaseMetaData.this, referencedColumns.next().toString()));
/*      */                             
/* 2743 */                             tuple[8] = Integer.toString(keySeq).getBytes();
/*      */                             
/*      */ 
/* 2746 */                             int[] actions = DatabaseMetaData.this.getForeignKeyActions(keys);
/*      */                             
/* 2748 */                             tuple[9] = Integer.toString(actions[1]).getBytes();
/*      */                             
/* 2750 */                             tuple[10] = Integer.toString(actions[0]).getBytes();
/*      */                             
/* 2752 */                             tuple[11] = null;
/* 2753 */                             tuple[12] = null;
/* 2754 */                             tuple[13] = Integer.toString(7).getBytes();
/*      */                             
/*      */ 
/*      */ 
/* 2758 */                             this.val$tuples.add(tuple);
/* 2759 */                             keySeq++;
/*      */                           }
/*      */                         }
/*      */                       }
/*      */                     }
/*      */                   }
/*      */                 }
/*      */               }
/* 2767 */             } finally { if (fkresults != null) {
/*      */                 try {
/* 2769 */                   fkresults.close();
/*      */                 } catch (Exception sqlEx) {
/* 2771 */                   AssertionFailedException.shouldNotHappen(sqlEx);
/*      */                 }
/*      */                 
/*      */ 
/* 2775 */                 fkresults = null;
/*      */               }
/*      */             }
/*      */           }
/*      */         }.doForAll();
/*      */       } finally {
/* 2781 */         if (stmt != null) {
/* 2782 */           stmt.close();
/*      */         }
/*      */       }
/*      */     }
/*      */     
/* 2787 */     java.sql.ResultSet results = buildResultSet(fields, tuples);
/*      */     
/* 2789 */     return results;
/*      */   }
/*      */   
/*      */ 
/*      */   public int getDatabaseMajorVersion()
/*      */     throws SQLException
/*      */   {
/* 2796 */     return this.conn.getServerMajorVersion();
/*      */   }
/*      */   
/*      */ 
/*      */   public int getDatabaseMinorVersion()
/*      */     throws SQLException
/*      */   {
/* 2803 */     return this.conn.getServerMinorVersion();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public String getDatabaseProductName()
/*      */     throws SQLException
/*      */   {
/* 2814 */     return "MySQL";
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public String getDatabaseProductVersion()
/*      */     throws SQLException
/*      */   {
/* 2825 */     return this.conn.getServerVersion();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public int getDefaultTransactionIsolation()
/*      */     throws SQLException
/*      */   {
/* 2838 */     if (this.conn.supportsIsolationLevel()) {
/* 2839 */       return 2;
/*      */     }
/*      */     
/* 2842 */     return 0;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public int getDriverMajorVersion()
/*      */   {
/* 2851 */     return NonRegisteringDriver.getMajorVersionInternal();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public int getDriverMinorVersion()
/*      */   {
/* 2860 */     return NonRegisteringDriver.getMinorVersionInternal();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public String getDriverName()
/*      */     throws SQLException
/*      */   {
/* 2871 */     return "MySQL-AB JDBC Driver";
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public String getDriverVersion()
/*      */     throws SQLException
/*      */   {
/* 2882 */     return "mysql-connector-java-5.0.8 ( Revision: ${svn.Revision} )";
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public java.sql.ResultSet getExportedKeys(String catalog, String schema, String table)
/*      */     throws SQLException
/*      */   {
/* 2946 */     if (table == null) {
/* 2947 */       throw SQLError.createSQLException("Table not specified.", "S1009");
/*      */     }
/*      */     
/*      */ 
/* 2951 */     Field[] fields = new Field[14];
/* 2952 */     fields[0] = new Field("", "PKTABLE_CAT", 1, 255);
/* 2953 */     fields[1] = new Field("", "PKTABLE_SCHEM", 1, 0);
/* 2954 */     fields[2] = new Field("", "PKTABLE_NAME", 1, 255);
/* 2955 */     fields[3] = new Field("", "PKCOLUMN_NAME", 1, 32);
/* 2956 */     fields[4] = new Field("", "FKTABLE_CAT", 1, 255);
/* 2957 */     fields[5] = new Field("", "FKTABLE_SCHEM", 1, 0);
/* 2958 */     fields[6] = new Field("", "FKTABLE_NAME", 1, 255);
/* 2959 */     fields[7] = new Field("", "FKCOLUMN_NAME", 1, 32);
/* 2960 */     fields[8] = new Field("", "KEY_SEQ", 5, 2);
/* 2961 */     fields[9] = new Field("", "UPDATE_RULE", 5, 2);
/* 2962 */     fields[10] = new Field("", "DELETE_RULE", 5, 2);
/* 2963 */     fields[11] = new Field("", "FK_NAME", 1, 255);
/* 2964 */     fields[12] = new Field("", "PK_NAME", 1, 0);
/* 2965 */     fields[13] = new Field("", "DEFERRABILITY", 4, 2);
/*      */     
/* 2967 */     ArrayList rows = new ArrayList();
/*      */     
/* 2969 */     if (this.conn.versionMeetsMinimum(3, 23, 0))
/*      */     {
/* 2971 */       Statement stmt = this.conn.getMetadataSafeStatement();
/*      */       
/*      */       try
/*      */       {
/* 2975 */         new IterateBlock(getCatalogIterator(catalog), stmt) { private final Statement val$stmt;
/*      */           
/* 2977 */           void forEach(Object catalogStr) throws SQLException { java.sql.ResultSet fkresults = null;
/*      */             
/*      */ 
/*      */ 
/*      */ 
/*      */             try
/*      */             {
/* 2984 */               if (DatabaseMetaData.this.conn.versionMeetsMinimum(3, 23, 50))
/*      */               {
/*      */ 
/* 2987 */                 fkresults = DatabaseMetaData.this.extractForeignKeyFromCreateTable(catalogStr.toString(), null);
/*      */               }
/*      */               else {
/* 2990 */                 StringBuffer queryBuf = new StringBuffer("SHOW TABLE STATUS FROM ");
/*      */                 
/* 2992 */                 queryBuf.append(DatabaseMetaData.this.quotedId);
/* 2993 */                 queryBuf.append(catalogStr.toString());
/* 2994 */                 queryBuf.append(DatabaseMetaData.this.quotedId);
/*      */                 
/* 2996 */                 fkresults = this.val$stmt.executeQuery(queryBuf.toString());
/*      */               }
/*      */               
/*      */ 
/*      */ 
/* 3001 */               String tableNameWithCase = DatabaseMetaData.this.getTableNameWithCase(this.val$table);
/*      */               
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 3007 */               while (fkresults.next()) {
/* 3008 */                 String tableType = fkresults.getString("Type");
/*      */                 
/* 3010 */                 if ((tableType != null) && ((tableType.equalsIgnoreCase("innodb")) || (tableType.equalsIgnoreCase("SUPPORTS_FK"))))
/*      */                 {
/*      */ 
/*      */ 
/* 3014 */                   String comment = fkresults.getString("Comment").trim();
/*      */                   
/*      */ 
/* 3017 */                   if (comment != null) {
/* 3018 */                     StringTokenizer commentTokens = new StringTokenizer(comment, ";", false);
/*      */                     
/*      */ 
/* 3021 */                     if (commentTokens.hasMoreTokens()) {
/* 3022 */                       commentTokens.nextToken();
/*      */                       
/*      */ 
/*      */ 
/*      */ 
/* 3027 */                       while (commentTokens.hasMoreTokens()) {
/* 3028 */                         String keys = commentTokens.nextToken();
/*      */                         
/* 3030 */                         DatabaseMetaData.this.getExportKeyResults(catalogStr.toString(), tableNameWithCase, keys, this.val$rows, fkresults.getString("Name"));
/*      */                       }
/*      */                       
/*      */                     }
/*      */                     
/*      */                   }
/*      */                   
/*      */                 }
/*      */                 
/*      */               }
/*      */               
/*      */             }
/*      */             finally
/*      */             {
/* 3044 */               if (fkresults != null) {
/*      */                 try {
/* 3046 */                   fkresults.close();
/*      */                 } catch (SQLException sqlEx) {
/* 3048 */                   AssertionFailedException.shouldNotHappen(sqlEx);
/*      */                 }
/*      */                 
/*      */ 
/* 3052 */                 fkresults = null;
/*      */               }
/*      */             }
/*      */           }
/*      */         }.doForAll();
/*      */       } finally {
/* 3058 */         if (stmt != null) {
/* 3059 */           stmt.close();
/*      */         }
/*      */       }
/*      */     }
/*      */     
/* 3064 */     java.sql.ResultSet results = buildResultSet(fields, rows);
/*      */     
/* 3066 */     return results;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   private void getExportKeyResults(String catalog, String exportingTable, String keysComment, List tuples, String fkTableName)
/*      */     throws SQLException
/*      */   {
/* 3090 */     getResultsImpl(catalog, exportingTable, keysComment, tuples, fkTableName, true);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public String getExtraNameCharacters()
/*      */     throws SQLException
/*      */   {
/* 3103 */     return "#@";
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   private int[] getForeignKeyActions(String commentString)
/*      */   {
/* 3116 */     int[] actions = { 3, 3 };
/*      */     
/*      */ 
/*      */ 
/* 3120 */     int lastParenIndex = commentString.lastIndexOf(")");
/*      */     
/* 3122 */     if (lastParenIndex != commentString.length() - 1) {
/* 3123 */       String cascadeOptions = commentString.substring(lastParenIndex + 1).trim().toUpperCase(Locale.ENGLISH);
/*      */       
/*      */ 
/* 3126 */       actions[0] = getCascadeDeleteOption(cascadeOptions);
/* 3127 */       actions[1] = getCascadeUpdateOption(cascadeOptions);
/*      */     }
/*      */     
/* 3130 */     return actions;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public String getIdentifierQuoteString()
/*      */     throws SQLException
/*      */   {
/* 3143 */     if (this.conn.supportsQuotedIdentifiers()) {
/* 3144 */       if (!this.conn.useAnsiQuotedIdentifiers()) {
/* 3145 */         return "`";
/*      */       }
/*      */       
/* 3148 */       return "\"";
/*      */     }
/*      */     
/* 3151 */     return " ";
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public java.sql.ResultSet getImportedKeys(String catalog, String schema, String table)
/*      */     throws SQLException
/*      */   {
/* 3215 */     if (table == null) {
/* 3216 */       throw SQLError.createSQLException("Table not specified.", "S1009");
/*      */     }
/*      */     
/*      */ 
/* 3220 */     Field[] fields = new Field[14];
/* 3221 */     fields[0] = new Field("", "PKTABLE_CAT", 1, 255);
/* 3222 */     fields[1] = new Field("", "PKTABLE_SCHEM", 1, 0);
/* 3223 */     fields[2] = new Field("", "PKTABLE_NAME", 1, 255);
/* 3224 */     fields[3] = new Field("", "PKCOLUMN_NAME", 1, 32);
/* 3225 */     fields[4] = new Field("", "FKTABLE_CAT", 1, 255);
/* 3226 */     fields[5] = new Field("", "FKTABLE_SCHEM", 1, 0);
/* 3227 */     fields[6] = new Field("", "FKTABLE_NAME", 1, 255);
/* 3228 */     fields[7] = new Field("", "FKCOLUMN_NAME", 1, 32);
/* 3229 */     fields[8] = new Field("", "KEY_SEQ", 5, 2);
/* 3230 */     fields[9] = new Field("", "UPDATE_RULE", 5, 2);
/* 3231 */     fields[10] = new Field("", "DELETE_RULE", 5, 2);
/* 3232 */     fields[11] = new Field("", "FK_NAME", 1, 255);
/* 3233 */     fields[12] = new Field("", "PK_NAME", 1, 0);
/* 3234 */     fields[13] = new Field("", "DEFERRABILITY", 4, 2);
/*      */     
/* 3236 */     ArrayList rows = new ArrayList();
/*      */     
/* 3238 */     if (this.conn.versionMeetsMinimum(3, 23, 0))
/*      */     {
/* 3240 */       Statement stmt = this.conn.getMetadataSafeStatement();
/*      */       
/*      */       try
/*      */       {
/* 3244 */         new IterateBlock(getCatalogIterator(catalog), table) { private final String val$table;
/*      */           
/* 3246 */           void forEach(Object catalogStr) throws SQLException { java.sql.ResultSet fkresults = null;
/*      */             
/*      */ 
/*      */ 
/*      */ 
/*      */             try
/*      */             {
/* 3253 */               if (DatabaseMetaData.this.conn.versionMeetsMinimum(3, 23, 50))
/*      */               {
/*      */ 
/* 3256 */                 fkresults = DatabaseMetaData.this.extractForeignKeyFromCreateTable(catalogStr.toString(), this.val$table);
/*      */               }
/*      */               else {
/* 3259 */                 StringBuffer queryBuf = new StringBuffer("SHOW TABLE STATUS ");
/*      */                 
/* 3261 */                 queryBuf.append(" FROM ");
/* 3262 */                 queryBuf.append(DatabaseMetaData.this.quotedId);
/* 3263 */                 queryBuf.append(catalogStr.toString());
/* 3264 */                 queryBuf.append(DatabaseMetaData.this.quotedId);
/* 3265 */                 queryBuf.append(" LIKE '");
/* 3266 */                 queryBuf.append(this.val$table);
/* 3267 */                 queryBuf.append("'");
/*      */                 
/* 3269 */                 fkresults = this.val$stmt.executeQuery(queryBuf.toString());
/*      */               }
/*      */               
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 3277 */               while (fkresults.next()) {
/* 3278 */                 String tableType = fkresults.getString("Type");
/*      */                 
/* 3280 */                 if ((tableType != null) && ((tableType.equalsIgnoreCase("innodb")) || (tableType.equalsIgnoreCase("SUPPORTS_FK"))))
/*      */                 {
/*      */ 
/*      */ 
/* 3284 */                   String comment = fkresults.getString("Comment").trim();
/*      */                   
/*      */ 
/* 3287 */                   if (comment != null) {
/* 3288 */                     StringTokenizer commentTokens = new StringTokenizer(comment, ";", false);
/*      */                     
/*      */ 
/* 3291 */                     if (commentTokens.hasMoreTokens()) {
/* 3292 */                       commentTokens.nextToken();
/*      */                       
/*      */ 
/*      */ 
/*      */ 
/* 3297 */                       while (commentTokens.hasMoreTokens()) {
/* 3298 */                         String keys = commentTokens.nextToken();
/*      */                         
/* 3300 */                         DatabaseMetaData.this.getImportKeyResults(catalogStr.toString(), this.val$table, keys, this.val$rows);
/*      */                       }
/*      */                     }
/*      */                   }
/*      */                 }
/*      */               }
/*      */             }
/*      */             finally
/*      */             {
/* 3309 */               if (fkresults != null) {
/*      */                 try {
/* 3311 */                   fkresults.close();
/*      */                 } catch (SQLException sqlEx) {
/* 3313 */                   AssertionFailedException.shouldNotHappen(sqlEx);
/*      */                 }
/*      */                 
/*      */ 
/* 3317 */                 fkresults = null;
/*      */               }
/*      */             }
/*      */           }
/*      */         }.doForAll();
/*      */       } finally {
/* 3323 */         if (stmt != null) {
/* 3324 */           stmt.close();
/*      */         }
/*      */       }
/*      */     }
/*      */     
/* 3329 */     java.sql.ResultSet results = buildResultSet(fields, rows);
/*      */     
/* 3331 */     return results;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   private void getImportKeyResults(String catalog, String importingTable, String keysComment, List tuples)
/*      */     throws SQLException
/*      */   {
/* 3353 */     getResultsImpl(catalog, importingTable, keysComment, tuples, null, false);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   private final Statement val$stmt;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   private final ArrayList val$rows;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   private final String val$table;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   private final ArrayList val$rows;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public java.sql.ResultSet getIndexInfo(String catalog, String schema, String table, boolean unique, boolean approximate)
/*      */     throws SQLException
/*      */   {
/* 3424 */     Field[] fields = new Field[13];
/* 3425 */     fields[0] = new Field("", "TABLE_CAT", 1, 255);
/* 3426 */     fields[1] = new Field("", "TABLE_SCHEM", 1, 0);
/* 3427 */     fields[2] = new Field("", "TABLE_NAME", 1, 255);
/* 3428 */     fields[3] = new Field("", "NON_UNIQUE", 1, 4);
/* 3429 */     fields[4] = new Field("", "INDEX_QUALIFIER", 1, 1);
/* 3430 */     fields[5] = new Field("", "INDEX_NAME", 1, 32);
/* 3431 */     fields[6] = new Field("", "TYPE", 1, 32);
/* 3432 */     fields[7] = new Field("", "ORDINAL_POSITION", 5, 5);
/* 3433 */     fields[8] = new Field("", "COLUMN_NAME", 1, 32);
/* 3434 */     fields[9] = new Field("", "ASC_OR_DESC", 1, 1);
/* 3435 */     fields[10] = new Field("", "CARDINALITY", 4, 10);
/* 3436 */     fields[11] = new Field("", "PAGES", 4, 10);
/* 3437 */     fields[12] = new Field("", "FILTER_CONDITION", 1, 32);
/*      */     
/* 3439 */     ArrayList rows = new ArrayList();
/* 3440 */     Statement stmt = this.conn.getMetadataSafeStatement();
/*      */     
/*      */     try
/*      */     {
/* 3444 */       new IterateBlock(getCatalogIterator(catalog), table) { private final String val$table;
/*      */         private final Statement val$stmt;
/*      */         
/* 3447 */         void forEach(Object catalogStr) throws SQLException { java.sql.ResultSet results = null;
/*      */           try
/*      */           {
/* 3450 */             StringBuffer queryBuf = new StringBuffer("SHOW INDEX FROM ");
/*      */             
/* 3452 */             queryBuf.append(DatabaseMetaData.this.quotedId);
/* 3453 */             queryBuf.append(this.val$table);
/* 3454 */             queryBuf.append(DatabaseMetaData.this.quotedId);
/* 3455 */             queryBuf.append(" FROM ");
/* 3456 */             queryBuf.append(DatabaseMetaData.this.quotedId);
/* 3457 */             queryBuf.append(catalogStr.toString());
/* 3458 */             queryBuf.append(DatabaseMetaData.this.quotedId);
/*      */             try
/*      */             {
/* 3461 */               results = this.val$stmt.executeQuery(queryBuf.toString());
/*      */             } catch (SQLException sqlEx) {
/* 3463 */               int errorCode = sqlEx.getErrorCode();
/*      */               
/*      */ 
/*      */ 
/* 3467 */               if (!"42S02".equals(sqlEx.getSQLState()))
/*      */               {
/*      */ 
/* 3470 */                 if (errorCode != 1146) {
/* 3471 */                   throw sqlEx;
/*      */                 }
/*      */               }
/*      */             }
/*      */             
/* 3476 */             while ((results != null) && (results.next())) {
/* 3477 */               byte[][] row = new byte[14][];
/* 3478 */               row[0] = (catalogStr.toString() == null ? new byte[0] : DatabaseMetaData.this.s2b(catalogStr.toString()));
/*      */               
/*      */ 
/* 3481 */               row[1] = null;
/* 3482 */               row[2] = results.getBytes("Table");
/*      */               
/* 3484 */               boolean indexIsUnique = results.getInt("Non_unique") == 0;
/*      */               
/*      */ 
/* 3487 */               row[3] = (!indexIsUnique ? DatabaseMetaData.this.s2b("true") : DatabaseMetaData.this.s2b("false"));
/*      */               
/* 3489 */               row[4] = new byte[0];
/* 3490 */               row[5] = results.getBytes("Key_name");
/* 3491 */               row[6] = Integer.toString(3).getBytes();
/*      */               
/*      */ 
/* 3494 */               row[7] = results.getBytes("Seq_in_index");
/* 3495 */               row[8] = results.getBytes("Column_name");
/* 3496 */               row[9] = results.getBytes("Collation");
/* 3497 */               row[10] = results.getBytes("Cardinality");
/* 3498 */               row[11] = DatabaseMetaData.this.s2b("0");
/* 3499 */               row[12] = null;
/*      */               
/* 3501 */               if (this.val$unique) {
/* 3502 */                 if (indexIsUnique) {
/* 3503 */                   this.val$rows.add(row);
/*      */                 }
/*      */               }
/*      */               else {
/* 3507 */                 this.val$rows.add(row);
/*      */               }
/*      */             }
/*      */           } finally {
/* 3511 */             if (results != null) {
/*      */               try {
/* 3513 */                 results.close();
/*      */               }
/*      */               catch (Exception ex) {}
/*      */               
/*      */ 
/* 3518 */               results = null;
/*      */             }
/*      */             
/*      */           }
/*      */         }
/* 3523 */       }.doForAll();
/* 3524 */       java.sql.ResultSet indexInfo = buildResultSet(fields, rows);
/*      */       
/* 3526 */       return indexInfo;
/*      */     } finally {
/* 3528 */       if (stmt != null) {
/* 3529 */         stmt.close();
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */   public int getJDBCMajorVersion()
/*      */     throws SQLException
/*      */   {
/* 3538 */     return 3;
/*      */   }
/*      */   
/*      */ 
/*      */   public int getJDBCMinorVersion()
/*      */     throws SQLException
/*      */   {
/* 3545 */     return 0;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public int getMaxBinaryLiteralLength()
/*      */     throws SQLException
/*      */   {
/* 3556 */     return 16777208;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public int getMaxCatalogNameLength()
/*      */     throws SQLException
/*      */   {
/* 3567 */     return 32;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public int getMaxCharLiteralLength()
/*      */     throws SQLException
/*      */   {
/* 3578 */     return 16777208;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public int getMaxColumnNameLength()
/*      */     throws SQLException
/*      */   {
/* 3589 */     return 64;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public int getMaxColumnsInGroupBy()
/*      */     throws SQLException
/*      */   {
/* 3600 */     return 64;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public int getMaxColumnsInIndex()
/*      */     throws SQLException
/*      */   {
/* 3611 */     return 16;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public int getMaxColumnsInOrderBy()
/*      */     throws SQLException
/*      */   {
/* 3622 */     return 64;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public int getMaxColumnsInSelect()
/*      */     throws SQLException
/*      */   {
/* 3633 */     return 256;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public int getMaxColumnsInTable()
/*      */     throws SQLException
/*      */   {
/* 3644 */     return 512;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public int getMaxConnections()
/*      */     throws SQLException
/*      */   {
/* 3655 */     return 0;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public int getMaxCursorNameLength()
/*      */     throws SQLException
/*      */   {
/* 3666 */     return 64;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public int getMaxIndexLength()
/*      */     throws SQLException
/*      */   {
/* 3677 */     return 256;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public int getMaxProcedureNameLength()
/*      */     throws SQLException
/*      */   {
/* 3688 */     return 0;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public int getMaxRowSize()
/*      */     throws SQLException
/*      */   {
/* 3699 */     return 2147483639;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public int getMaxSchemaNameLength()
/*      */     throws SQLException
/*      */   {
/* 3710 */     return 0;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public int getMaxStatementLength()
/*      */     throws SQLException
/*      */   {
/* 3721 */     return MysqlIO.getMaxBuf() - 4;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public int getMaxStatements()
/*      */     throws SQLException
/*      */   {
/* 3732 */     return 0;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public int getMaxTableNameLength()
/*      */     throws SQLException
/*      */   {
/* 3743 */     return 64;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public int getMaxTablesInSelect()
/*      */     throws SQLException
/*      */   {
/* 3754 */     return 256;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public int getMaxUserNameLength()
/*      */     throws SQLException
/*      */   {
/* 3765 */     return 16;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public String getNumericFunctions()
/*      */     throws SQLException
/*      */   {
/* 3776 */     return "ABS,ACOS,ASIN,ATAN,ATAN2,BIT_COUNT,CEILING,COS,COT,DEGREES,EXP,FLOOR,LOG,LOG10,MAX,MIN,MOD,PI,POW,POWER,RADIANS,RAND,ROUND,SIN,SQRT,TAN,TRUNCATE";
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public java.sql.ResultSet getPrimaryKeys(String catalog, String schema, String table)
/*      */     throws SQLException
/*      */   {
/* 3808 */     Field[] fields = new Field[6];
/* 3809 */     fields[0] = new Field("", "TABLE_CAT", 1, 255);
/* 3810 */     fields[1] = new Field("", "TABLE_SCHEM", 1, 0);
/* 3811 */     fields[2] = new Field("", "TABLE_NAME", 1, 255);
/* 3812 */     fields[3] = new Field("", "COLUMN_NAME", 1, 32);
/* 3813 */     fields[4] = new Field("", "KEY_SEQ", 5, 5);
/* 3814 */     fields[5] = new Field("", "PK_NAME", 1, 32);
/*      */     
/* 3816 */     if (table == null) {
/* 3817 */       throw SQLError.createSQLException("Table not specified.", "S1009");
/*      */     }
/*      */     
/*      */ 
/* 3821 */     ArrayList rows = new ArrayList();
/* 3822 */     Statement stmt = this.conn.getMetadataSafeStatement();
/*      */     
/*      */     try
/*      */     {
/* 3826 */       new IterateBlock(getCatalogIterator(catalog), table) { private final String val$table;
/*      */         
/* 3828 */         void forEach(Object catalogStr) throws SQLException { java.sql.ResultSet rs = null;
/*      */           
/*      */           try
/*      */           {
/* 3832 */             StringBuffer queryBuf = new StringBuffer("SHOW KEYS FROM ");
/*      */             
/* 3834 */             queryBuf.append(DatabaseMetaData.this.quotedId);
/* 3835 */             queryBuf.append(this.val$table);
/* 3836 */             queryBuf.append(DatabaseMetaData.this.quotedId);
/* 3837 */             queryBuf.append(" FROM ");
/* 3838 */             queryBuf.append(DatabaseMetaData.this.quotedId);
/* 3839 */             queryBuf.append(catalogStr.toString());
/* 3840 */             queryBuf.append(DatabaseMetaData.this.quotedId);
/*      */             
/* 3842 */             rs = this.val$stmt.executeQuery(queryBuf.toString());
/*      */             
/* 3844 */             ArrayList tuples = new ArrayList();
/* 3845 */             TreeMap sortMap = new TreeMap();
/*      */             
/* 3847 */             while (rs.next()) {
/* 3848 */               String keyType = rs.getString("Key_name");
/*      */               
/* 3850 */               if ((keyType != null) && (
/* 3851 */                 (keyType.equalsIgnoreCase("PRIMARY")) || (keyType.equalsIgnoreCase("PRI"))))
/*      */               {
/* 3853 */                 byte[][] tuple = new byte[6][];
/* 3854 */                 tuple[0] = (catalogStr.toString() == null ? new byte[0] : DatabaseMetaData.this.s2b(catalogStr.toString()));
/*      */                 
/* 3856 */                 tuple[1] = null;
/* 3857 */                 tuple[2] = DatabaseMetaData.this.s2b(this.val$table);
/*      */                 
/* 3859 */                 String columnName = rs.getString("Column_name");
/*      */                 
/* 3861 */                 tuple[3] = DatabaseMetaData.this.s2b(columnName);
/* 3862 */                 tuple[4] = DatabaseMetaData.this.s2b(rs.getString("Seq_in_index"));
/* 3863 */                 tuple[5] = DatabaseMetaData.this.s2b(keyType);
/* 3864 */                 sortMap.put(columnName, tuple);
/*      */               }
/*      */             }
/*      */             
/*      */ 
/*      */ 
/* 3870 */             Iterator sortedIterator = sortMap.values().iterator();
/*      */             
/* 3872 */             while (sortedIterator.hasNext()) {
/* 3873 */               this.val$rows.add(sortedIterator.next());
/*      */             }
/*      */           }
/*      */           finally {
/* 3877 */             if (rs != null) {
/*      */               try {
/* 3879 */                 rs.close();
/*      */               }
/*      */               catch (Exception ex) {}
/*      */               
/*      */ 
/* 3884 */               rs = null;
/*      */             }
/*      */           }
/*      */         }
/*      */       }.doForAll();
/*      */     } finally {
/* 3890 */       if (stmt != null) {
/* 3891 */         stmt.close();
/*      */       }
/*      */     }
/*      */     
/* 3895 */     java.sql.ResultSet results = buildResultSet(fields, rows);
/*      */     
/* 3897 */     return results;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   private final Statement val$stmt;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   private final ArrayList val$rows;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   private final boolean val$unique;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   private final ArrayList val$rows;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   private final String val$primaryTable;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   private final String val$foreignCatalog;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public java.sql.ResultSet getProcedureColumns(String catalog, String schemaPattern, String procedureNamePattern, String columnNamePattern)
/*      */     throws SQLException
/*      */   {
/* 3970 */     Field[] fields = new Field[13];
/*      */     
/* 3972 */     fields[0] = new Field("", "PROCEDURE_CAT", 1, 0);
/* 3973 */     fields[1] = new Field("", "PROCEDURE_SCHEM", 1, 0);
/* 3974 */     fields[2] = new Field("", "PROCEDURE_NAME", 1, 0);
/* 3975 */     fields[3] = new Field("", "COLUMN_NAME", 1, 0);
/* 3976 */     fields[4] = new Field("", "COLUMN_TYPE", 1, 0);
/* 3977 */     fields[5] = new Field("", "DATA_TYPE", 5, 0);
/* 3978 */     fields[6] = new Field("", "TYPE_NAME", 1, 0);
/* 3979 */     fields[7] = new Field("", "PRECISION", 4, 0);
/* 3980 */     fields[8] = new Field("", "LENGTH", 4, 0);
/* 3981 */     fields[9] = new Field("", "SCALE", 5, 0);
/* 3982 */     fields[10] = new Field("", "RADIX", 5, 0);
/* 3983 */     fields[11] = new Field("", "NULLABLE", 5, 0);
/* 3984 */     fields[12] = new Field("", "REMARKS", 1, 0);
/*      */     
/* 3986 */     List proceduresToExtractList = new ArrayList();
/*      */     
/* 3988 */     if (supportsStoredProcedures()) {
/* 3989 */       if ((procedureNamePattern.indexOf("%") == -1) && (procedureNamePattern.indexOf("?") == -1))
/*      */       {
/* 3991 */         proceduresToExtractList.add(procedureNamePattern);
/*      */       }
/*      */       else {
/* 3994 */         java.sql.ResultSet procedureNameRs = null;
/*      */         
/*      */         try
/*      */         {
/* 3998 */           procedureNameRs = getProcedures(catalog, schemaPattern, procedureNamePattern);
/*      */           
/*      */ 
/* 4001 */           while (procedureNameRs.next()) {
/* 4002 */             proceduresToExtractList.add(procedureNameRs.getString(3));
/*      */           }
/*      */           
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 4010 */           Collections.sort(proceduresToExtractList);
/*      */         } finally {
/* 4012 */           SQLException rethrowSqlEx = null;
/*      */           
/* 4014 */           if (procedureNameRs != null) {
/*      */             try {
/* 4016 */               procedureNameRs.close();
/*      */             } catch (SQLException sqlEx) {
/* 4018 */               rethrowSqlEx = sqlEx;
/*      */             }
/*      */           }
/*      */           
/* 4022 */           if (rethrowSqlEx != null) {
/* 4023 */             throw rethrowSqlEx;
/*      */           }
/*      */         }
/*      */       }
/*      */     }
/*      */     
/* 4029 */     ArrayList resultRows = new ArrayList();
/*      */     
/* 4031 */     for (Iterator iter = proceduresToExtractList.iterator(); iter.hasNext();) {
/* 4032 */       String procName = (String)iter.next();
/*      */       
/* 4034 */       getCallStmtParameterTypes(catalog, procName, columnNamePattern, resultRows);
/*      */     }
/*      */     
/*      */ 
/* 4038 */     return buildResultSet(fields, resultRows);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public java.sql.ResultSet getProcedures(String catalog, String schemaPattern, String procedureNamePattern)
/*      */     throws SQLException
/*      */   {
/* 4084 */     return getProceduresAndOrFunctions(catalog, schemaPattern, procedureNamePattern, true, true);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   protected java.sql.ResultSet getProceduresAndOrFunctions(String catalog, String schemaPattern, String procedureNamePattern, boolean returnProcedures, boolean returnFunctions)
/*      */     throws SQLException
/*      */   {
/* 4092 */     if ((procedureNamePattern == null) || (procedureNamePattern.length() == 0))
/*      */     {
/* 4094 */       if (this.conn.getNullNamePatternMatchesAll()) {
/* 4095 */         procedureNamePattern = "%";
/*      */       } else {
/* 4097 */         throw SQLError.createSQLException("Procedure name pattern can not be NULL or empty.", "S1009");
/*      */       }
/*      */     }
/*      */     
/*      */ 
/*      */ 
/* 4103 */     Field[] fields = new Field[8];
/* 4104 */     fields[0] = new Field("", "PROCEDURE_CAT", 1, 0);
/* 4105 */     fields[1] = new Field("", "PROCEDURE_SCHEM", 1, 0);
/* 4106 */     fields[2] = new Field("", "PROCEDURE_NAME", 1, 0);
/* 4107 */     fields[3] = new Field("", "reserved1", 1, 0);
/* 4108 */     fields[4] = new Field("", "reserved2", 1, 0);
/* 4109 */     fields[5] = new Field("", "reserved3", 1, 0);
/* 4110 */     fields[6] = new Field("", "REMARKS", 1, 0);
/* 4111 */     fields[7] = new Field("", "PROCEDURE_TYPE", 5, 0);
/*      */     
/* 4113 */     ArrayList procedureRows = new ArrayList();
/*      */     
/* 4115 */     if (supportsStoredProcedures()) {
/* 4116 */       String procNamePattern = procedureNamePattern;
/*      */       
/* 4118 */       Map procedureRowsOrderedByName = new TreeMap();
/*      */       
/* 4120 */       new IterateBlock(getCatalogIterator(catalog), procNamePattern) { private final String val$procNamePattern;
/*      */         
/* 4122 */         void forEach(Object catalogStr) throws SQLException { String db = catalogStr.toString();
/*      */           
/* 4124 */           boolean fromSelect = false;
/* 4125 */           java.sql.ResultSet proceduresRs = null;
/* 4126 */           boolean needsClientFiltering = true;
/* 4127 */           PreparedStatement proceduresStmt = DatabaseMetaData.this.conn.clientPrepareStatement("SELECT name, type FROM mysql.proc WHERE name like ? and db <=> ? ORDER BY name");
/*      */           
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */           try
/*      */           {
/* 4136 */             boolean hasTypeColumn = false;
/*      */             
/* 4138 */             if (db != null) {
/* 4139 */               proceduresStmt.setString(2, db);
/*      */             } else {
/* 4141 */               proceduresStmt.setNull(2, 12);
/*      */             }
/*      */             
/* 4144 */             int nameIndex = 1;
/*      */             
/* 4146 */             if (proceduresStmt.getMaxRows() != 0) {
/* 4147 */               proceduresStmt.setMaxRows(0);
/*      */             }
/*      */             
/* 4150 */             proceduresStmt.setString(1, this.val$procNamePattern);
/*      */             try
/*      */             {
/* 4153 */               proceduresRs = proceduresStmt.executeQuery();
/* 4154 */               fromSelect = true;
/* 4155 */               needsClientFiltering = false;
/* 4156 */               hasTypeColumn = true;
/*      */ 
/*      */ 
/*      */             }
/*      */             catch (SQLException sqlEx)
/*      */             {
/*      */ 
/*      */ 
/* 4164 */               proceduresStmt.close();
/*      */               
/* 4166 */               fromSelect = false;
/*      */               
/* 4168 */               if (DatabaseMetaData.this.conn.versionMeetsMinimum(5, 0, 1)) {
/* 4169 */                 nameIndex = 2;
/*      */               } else {
/* 4171 */                 nameIndex = 1;
/*      */               }
/*      */               
/* 4174 */               proceduresStmt = DatabaseMetaData.this.conn.clientPrepareStatement("SHOW PROCEDURE STATUS LIKE ?");
/*      */               
/*      */ 
/* 4177 */               if (proceduresStmt.getMaxRows() != 0) {
/* 4178 */                 proceduresStmt.setMaxRows(0);
/*      */               }
/*      */               
/* 4181 */               proceduresStmt.setString(1, this.val$procNamePattern);
/*      */               
/* 4183 */               proceduresRs = proceduresStmt.executeQuery();
/*      */             }
/*      */             
/* 4186 */             if (this.val$returnProcedures) {
/* 4187 */               DatabaseMetaData.this.convertToJdbcProcedureList(fromSelect, db, proceduresRs, needsClientFiltering, db, this.val$procedureRowsOrderedByName, nameIndex);
/*      */             }
/*      */             
/*      */ 
/*      */ 
/* 4192 */             if (!hasTypeColumn)
/*      */             {
/* 4194 */               if (proceduresStmt != null) {
/* 4195 */                 proceduresStmt.close();
/*      */               }
/*      */               
/* 4198 */               proceduresStmt = DatabaseMetaData.this.conn.clientPrepareStatement("SHOW FUNCTION STATUS LIKE ?");
/*      */               
/*      */ 
/* 4201 */               if (proceduresStmt.getMaxRows() != 0) {
/* 4202 */                 proceduresStmt.setMaxRows(0);
/*      */               }
/*      */               
/* 4205 */               proceduresStmt.setString(1, this.val$procNamePattern);
/*      */               
/* 4207 */               proceduresRs = proceduresStmt.executeQuery();
/*      */               
/* 4209 */               if (this.val$returnFunctions) {
/* 4210 */                 DatabaseMetaData.this.convertToJdbcFunctionList(db, proceduresRs, needsClientFiltering, db, this.val$procedureRowsOrderedByName, nameIndex);
/*      */               }
/*      */             }
/*      */             
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 4218 */             Iterator proceduresIter = this.val$procedureRowsOrderedByName.values().iterator();
/*      */             
/*      */ 
/* 4221 */             while (proceduresIter.hasNext()) {
/* 4222 */               this.val$procedureRows.add(proceduresIter.next());
/*      */             }
/*      */           } finally {
/* 4225 */             SQLException rethrowSqlEx = null;
/*      */             
/* 4227 */             if (proceduresRs != null) {
/*      */               try {
/* 4229 */                 proceduresRs.close();
/*      */               } catch (SQLException sqlEx) {
/* 4231 */                 rethrowSqlEx = sqlEx;
/*      */               }
/*      */             }
/*      */             
/* 4235 */             if (proceduresStmt != null) {
/*      */               try {
/* 4237 */                 proceduresStmt.close();
/*      */               } catch (SQLException sqlEx) {
/* 4239 */                 rethrowSqlEx = sqlEx;
/*      */               }
/*      */             }
/*      */             
/* 4243 */             if (rethrowSqlEx != null) {
/* 4244 */               throw rethrowSqlEx;
/*      */             }
/*      */           }
/*      */         }
/*      */       }.doForAll();
/*      */     }
/*      */     
/* 4251 */     return buildResultSet(fields, procedureRows);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public String getProcedureTerm()
/*      */     throws SQLException
/*      */   {
/* 4262 */     return "PROCEDURE";
/*      */   }
/*      */   
/*      */ 
/*      */   public int getResultSetHoldability()
/*      */     throws SQLException
/*      */   {
/* 4269 */     return 1;
/*      */   }
/*      */   
/*      */ 
/*      */   private void getResultsImpl(String catalog, String table, String keysComment, List tuples, String fkTableName, boolean isExport)
/*      */     throws SQLException
/*      */   {
/* 4276 */     LocalAndReferencedColumns parsedInfo = parseTableStatusIntoLocalAndReferencedColumns(keysComment);
/*      */     
/* 4278 */     if ((isExport) && (!parsedInfo.referencedTable.equals(table))) {
/* 4279 */       return;
/*      */     }
/*      */     
/* 4282 */     if (parsedInfo.localColumnsList.size() != parsedInfo.referencedColumnsList.size())
/*      */     {
/* 4284 */       throw SQLError.createSQLException("Error parsing foreign keys definition,number of local and referenced columns is not the same.", "S1000");
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 4291 */     Iterator localColumnNames = parsedInfo.localColumnsList.iterator();
/* 4292 */     Iterator referColumnNames = parsedInfo.referencedColumnsList.iterator();
/*      */     
/* 4294 */     int keySeqIndex = 1;
/*      */     
/* 4296 */     while (localColumnNames.hasNext()) {
/* 4297 */       byte[][] tuple = new byte[14][];
/* 4298 */       String lColumnName = removeQuotedId(localColumnNames.next().toString());
/*      */       
/* 4300 */       String rColumnName = removeQuotedId(referColumnNames.next().toString());
/*      */       
/* 4302 */       tuple[4] = (catalog == null ? new byte[0] : s2b(catalog));
/*      */       
/* 4304 */       tuple[5] = null;
/* 4305 */       tuple[6] = s2b(isExport ? fkTableName : table);
/* 4306 */       tuple[7] = s2b(lColumnName);
/* 4307 */       tuple[0] = s2b(parsedInfo.referencedCatalog);
/* 4308 */       tuple[1] = null;
/* 4309 */       tuple[2] = s2b(isExport ? table : parsedInfo.referencedTable);
/*      */       
/* 4311 */       tuple[3] = s2b(rColumnName);
/* 4312 */       tuple[8] = s2b(Integer.toString(keySeqIndex++));
/*      */       
/* 4314 */       int[] actions = getForeignKeyActions(keysComment);
/*      */       
/* 4316 */       tuple[9] = s2b(Integer.toString(actions[1]));
/* 4317 */       tuple[10] = s2b(Integer.toString(actions[0]));
/* 4318 */       tuple[11] = s2b(parsedInfo.constraintName);
/* 4319 */       tuple[12] = null;
/* 4320 */       tuple[13] = s2b(Integer.toString(7));
/*      */       
/* 4322 */       tuples.add(tuple);
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
/*      */   public java.sql.ResultSet getSchemas()
/*      */     throws SQLException
/*      */   {
/* 4342 */     Field[] fields = new Field[2];
/* 4343 */     fields[0] = new Field("", "TABLE_SCHEM", 1, 0);
/* 4344 */     fields[1] = new Field("", "TABLE_CATALOG", 1, 0);
/*      */     
/* 4346 */     ArrayList tuples = new ArrayList();
/* 4347 */     java.sql.ResultSet results = buildResultSet(fields, tuples);
/*      */     
/* 4349 */     return results;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public String getSchemaTerm()
/*      */     throws SQLException
/*      */   {
/* 4360 */     return "";
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public String getSearchStringEscape()
/*      */     throws SQLException
/*      */   {
/* 4378 */     return "\\";
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public String getSQLKeywords()
/*      */     throws SQLException
/*      */   {
/* 4390 */     return mysqlKeywordsThatArentSQL92;
/*      */   }
/*      */   
/*      */ 
/*      */   public int getSQLStateType()
/*      */     throws SQLException
/*      */   {
/* 4397 */     if (this.conn.versionMeetsMinimum(4, 1, 0)) {
/* 4398 */       return 2;
/*      */     }
/*      */     
/* 4401 */     if (this.conn.getUseSqlStateCodes()) {
/* 4402 */       return 2;
/*      */     }
/*      */     
/* 4405 */     return 1;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public String getStringFunctions()
/*      */     throws SQLException
/*      */   {
/* 4416 */     return "ASCII,BIN,BIT_LENGTH,CHAR,CHARACTER_LENGTH,CHAR_LENGTH,CONCAT,CONCAT_WS,CONV,ELT,EXPORT_SET,FIELD,FIND_IN_SET,HEX,INSERT,INSTR,LCASE,LEFT,LENGTH,LOAD_FILE,LOCATE,LOCATE,LOWER,LPAD,LTRIM,MAKE_SET,MATCH,MID,OCT,OCTET_LENGTH,ORD,POSITION,QUOTE,REPEAT,REPLACE,REVERSE,RIGHT,RPAD,RTRIM,SOUNDEX,SPACE,STRCMP,SUBSTRING,SUBSTRING,SUBSTRING,SUBSTRING,SUBSTRING_INDEX,TRIM,UCASE,UPPER";
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public java.sql.ResultSet getSuperTables(String arg0, String arg1, String arg2)
/*      */     throws SQLException
/*      */   {
/* 4430 */     Field[] fields = new Field[4];
/* 4431 */     fields[0] = new Field("", "TABLE_CAT", 1, 32);
/* 4432 */     fields[1] = new Field("", "TABLE_SCHEM", 1, 32);
/* 4433 */     fields[2] = new Field("", "TABLE_NAME", 1, 32);
/* 4434 */     fields[3] = new Field("", "SUPERTABLE_NAME", 1, 32);
/*      */     
/* 4436 */     return buildResultSet(fields, new ArrayList());
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public java.sql.ResultSet getSuperTypes(String arg0, String arg1, String arg2)
/*      */     throws SQLException
/*      */   {
/* 4444 */     Field[] fields = new Field[6];
/* 4445 */     fields[0] = new Field("", "TABLE_CAT", 1, 32);
/* 4446 */     fields[1] = new Field("", "TABLE_SCHEM", 1, 32);
/* 4447 */     fields[2] = new Field("", "TYPE_NAME", 1, 32);
/* 4448 */     fields[3] = new Field("", "SUPERTYPE_CAT", 1, 32);
/* 4449 */     fields[4] = new Field("", "SUPERTYPE_SCHEM", 1, 32);
/* 4450 */     fields[5] = new Field("", "SUPERTYPE_NAME", 1, 32);
/*      */     
/* 4452 */     return buildResultSet(fields, new ArrayList());
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public String getSystemFunctions()
/*      */     throws SQLException
/*      */   {
/* 4463 */     return "DATABASE,USER,SYSTEM_USER,SESSION_USER,PASSWORD,ENCRYPT,LAST_INSERT_ID,VERSION";
/*      */   }
/*      */   
/*      */   private String getTableNameWithCase(String table) {
/* 4467 */     String tableNameWithCase = this.conn.lowerCaseTableNames() ? table.toLowerCase() : table;
/*      */     
/*      */ 
/* 4470 */     return tableNameWithCase;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public java.sql.ResultSet getTablePrivileges(String catalog, String schemaPattern, String tableNamePattern)
/*      */     throws SQLException
/*      */   {
/* 4510 */     if (tableNamePattern == null) {
/* 4511 */       if (this.conn.getNullNamePatternMatchesAll()) {
/* 4512 */         tableNamePattern = "%";
/*      */       } else {
/* 4514 */         throw SQLError.createSQLException("Table name pattern can not be NULL or empty.", "S1009");
/*      */       }
/*      */     }
/*      */     
/*      */ 
/*      */ 
/* 4520 */     Field[] fields = new Field[7];
/* 4521 */     fields[0] = new Field("", "TABLE_CAT", 1, 64);
/* 4522 */     fields[1] = new Field("", "TABLE_SCHEM", 1, 1);
/* 4523 */     fields[2] = new Field("", "TABLE_NAME", 1, 64);
/* 4524 */     fields[3] = new Field("", "GRANTOR", 1, 77);
/* 4525 */     fields[4] = new Field("", "GRANTEE", 1, 77);
/* 4526 */     fields[5] = new Field("", "PRIVILEGE", 1, 64);
/* 4527 */     fields[6] = new Field("", "IS_GRANTABLE", 1, 3);
/*      */     
/* 4529 */     StringBuffer grantQuery = new StringBuffer("SELECT host,db,table_name,grantor,user,table_priv from mysql.tables_priv ");
/*      */     
/* 4531 */     grantQuery.append(" WHERE ");
/*      */     
/* 4533 */     if ((catalog != null) && (catalog.length() != 0)) {
/* 4534 */       grantQuery.append(" db='");
/* 4535 */       grantQuery.append(catalog);
/* 4536 */       grantQuery.append("' AND ");
/*      */     }
/*      */     
/* 4539 */     grantQuery.append("table_name like '");
/* 4540 */     grantQuery.append(tableNamePattern);
/* 4541 */     grantQuery.append("'");
/*      */     
/* 4543 */     java.sql.ResultSet results = null;
/* 4544 */     ArrayList grantRows = new ArrayList();
/* 4545 */     Statement stmt = null;
/*      */     try
/*      */     {
/* 4548 */       stmt = this.conn.createStatement();
/* 4549 */       stmt.setEscapeProcessing(false);
/*      */       
/* 4551 */       results = stmt.executeQuery(grantQuery.toString());
/*      */       
/* 4553 */       while (results.next()) {
/* 4554 */         String host = results.getString(1);
/* 4555 */         String db = results.getString(2);
/* 4556 */         String table = results.getString(3);
/* 4557 */         String grantor = results.getString(4);
/* 4558 */         String user = results.getString(5);
/*      */         
/* 4560 */         if ((user == null) || (user.length() == 0)) {
/* 4561 */           user = "%";
/*      */         }
/*      */         
/* 4564 */         StringBuffer fullUser = new StringBuffer(user);
/*      */         
/* 4566 */         if ((host != null) && (this.conn.getUseHostsInPrivileges())) {
/* 4567 */           fullUser.append("@");
/* 4568 */           fullUser.append(host);
/*      */         }
/*      */         
/* 4571 */         String allPrivileges = results.getString(6);
/*      */         
/* 4573 */         if (allPrivileges != null) {
/* 4574 */           allPrivileges = allPrivileges.toUpperCase(Locale.ENGLISH);
/*      */           
/* 4576 */           StringTokenizer st = new StringTokenizer(allPrivileges, ",");
/*      */           
/* 4578 */           while (st.hasMoreTokens()) {
/* 4579 */             String privilege = st.nextToken().trim();
/*      */             
/*      */ 
/* 4582 */             java.sql.ResultSet columnResults = null;
/*      */             try
/*      */             {
/* 4585 */               columnResults = getColumns(catalog, schemaPattern, table, "%");
/*      */               
/*      */ 
/* 4588 */               while (columnResults.next()) {
/* 4589 */                 byte[][] tuple = new byte[8][];
/* 4590 */                 tuple[0] = s2b(db);
/* 4591 */                 tuple[1] = null;
/* 4592 */                 tuple[2] = s2b(table);
/*      */                 
/* 4594 */                 if (grantor != null) {
/* 4595 */                   tuple[3] = s2b(grantor);
/*      */                 } else {
/* 4597 */                   tuple[3] = null;
/*      */                 }
/*      */                 
/* 4600 */                 tuple[4] = s2b(fullUser.toString());
/* 4601 */                 tuple[5] = s2b(privilege);
/* 4602 */                 tuple[6] = null;
/* 4603 */                 grantRows.add(tuple);
/*      */               }
/*      */             } finally {
/* 4606 */               if (columnResults != null) {
/*      */                 try {
/* 4608 */                   columnResults.close();
/*      */                 }
/*      */                 catch (Exception ex) {}
/*      */               }
/*      */             }
/*      */           }
/*      */         }
/*      */       }
/*      */     }
/*      */     finally {
/* 4618 */       if (results != null) {
/*      */         try {
/* 4620 */           results.close();
/*      */         }
/*      */         catch (Exception ex) {}
/*      */         
/*      */ 
/* 4625 */         results = null;
/*      */       }
/*      */       
/* 4628 */       if (stmt != null) {
/*      */         try {
/* 4630 */           stmt.close();
/*      */         }
/*      */         catch (Exception ex) {}
/*      */         
/*      */ 
/* 4635 */         stmt = null;
/*      */       }
/*      */     }
/*      */     
/* 4639 */     return buildResultSet(fields, grantRows);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   private final boolean val$returnProcedures;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public java.sql.ResultSet getTables(String catalog, String schemaPattern, String tableNamePattern, String[] types)
/*      */     throws SQLException
/*      */   {
/* 4681 */     if (tableNamePattern == null) {
/* 4682 */       if (this.conn.getNullNamePatternMatchesAll()) {
/* 4683 */         tableNamePattern = "%";
/*      */       } else {
/* 4685 */         throw SQLError.createSQLException("Table name pattern can not be NULL or empty.", "S1009");
/*      */       }
/*      */     }
/*      */     
/*      */ 
/*      */ 
/* 4691 */     Field[] fields = new Field[5];
/* 4692 */     fields[0] = new Field("", "TABLE_CAT", 12, 255);
/* 4693 */     fields[1] = new Field("", "TABLE_SCHEM", 12, 0);
/* 4694 */     fields[2] = new Field("", "TABLE_NAME", 12, 255);
/* 4695 */     fields[3] = new Field("", "TABLE_TYPE", 12, 5);
/* 4696 */     fields[4] = new Field("", "REMARKS", 12, 0);
/*      */     
/* 4698 */     ArrayList tuples = new ArrayList();
/*      */     
/* 4700 */     Statement stmt = this.conn.getMetadataSafeStatement();
/*      */     
/* 4702 */     String tableNamePat = tableNamePattern;
/*      */     
/*      */     try
/*      */     {
/* 4706 */       new IterateBlock(getCatalogIterator(catalog), stmt) { private final Statement val$stmt;
/*      */         
/* 4708 */         void forEach(Object catalogStr) throws SQLException { java.sql.ResultSet results = null;
/*      */           
/*      */           try
/*      */           {
/* 4712 */             if (!DatabaseMetaData.this.conn.versionMeetsMinimum(5, 0, 2)) {
/*      */               try {
/* 4714 */                 results = this.val$stmt.executeQuery("SHOW TABLES FROM " + DatabaseMetaData.this.quotedId + catalogStr.toString() + DatabaseMetaData.this.quotedId + " LIKE '" + this.val$tableNamePat + "'");
/*      */ 
/*      */               }
/*      */               catch (SQLException sqlEx)
/*      */               {
/* 4719 */                 if ("08S01".equals(sqlEx.getSQLState()))
/*      */                 {
/* 4721 */                   throw sqlEx;
/*      */                 }
/*      */                 
/* 4724 */                 return;
/*      */               }
/*      */             } else {
/*      */               try {
/* 4728 */                 results = this.val$stmt.executeQuery("SHOW FULL TABLES FROM " + DatabaseMetaData.this.quotedId + catalogStr.toString() + DatabaseMetaData.this.quotedId + " LIKE '" + this.val$tableNamePat + "'");
/*      */ 
/*      */ 
/*      */               }
/*      */               catch (SQLException sqlEx)
/*      */               {
/*      */ 
/* 4735 */                 if ("08S01".equals(sqlEx.getSQLState()))
/*      */                 {
/* 4737 */                   throw sqlEx;
/*      */                 }
/*      */                 
/* 4740 */                 return;
/*      */               }
/*      */             }
/*      */             
/* 4744 */             boolean shouldReportTables = false;
/* 4745 */             boolean shouldReportViews = false;
/*      */             
/* 4747 */             if ((this.val$types == null) || (this.val$types.length == 0)) {
/* 4748 */               shouldReportTables = true;
/* 4749 */               shouldReportViews = true;
/*      */             } else {
/* 4751 */               for (int i = 0; i < this.val$types.length; i++) {
/* 4752 */                 if ("TABLE".equalsIgnoreCase(this.val$types[i])) {
/* 4753 */                   shouldReportTables = true;
/*      */                 }
/*      */                 
/* 4756 */                 if ("VIEW".equalsIgnoreCase(this.val$types[i])) {
/* 4757 */                   shouldReportViews = true;
/*      */                 }
/*      */               }
/*      */             }
/*      */             
/* 4762 */             int typeColumnIndex = 0;
/* 4763 */             boolean hasTableTypes = false;
/*      */             
/* 4765 */             if (DatabaseMetaData.this.conn.versionMeetsMinimum(5, 0, 2))
/*      */             {
/*      */               try
/*      */               {
/*      */ 
/* 4770 */                 typeColumnIndex = results.findColumn("table_type");
/*      */                 
/* 4772 */                 hasTableTypes = true;
/*      */ 
/*      */ 
/*      */               }
/*      */               catch (SQLException sqlEx)
/*      */               {
/*      */ 
/*      */ 
/*      */                 try
/*      */                 {
/*      */ 
/*      */ 
/* 4784 */                   typeColumnIndex = results.findColumn("Type");
/*      */                   
/* 4786 */                   hasTableTypes = true;
/*      */                 } catch (SQLException sqlEx2) {
/* 4788 */                   hasTableTypes = false;
/*      */                 }
/*      */               }
/*      */             }
/*      */             
/* 4793 */             TreeMap tablesOrderedByName = null;
/* 4794 */             TreeMap viewsOrderedByName = null;
/*      */             
/* 4796 */             while (results.next()) {
/* 4797 */               byte[][] row = new byte[5][];
/* 4798 */               row[0] = (catalogStr.toString() == null ? null : DatabaseMetaData.this.s2b(catalogStr.toString()));
/*      */               
/* 4800 */               row[1] = null;
/* 4801 */               row[2] = results.getBytes(1);
/* 4802 */               row[4] = new byte[0];
/*      */               
/* 4804 */               if (hasTableTypes) {
/* 4805 */                 String tableType = results.getString(typeColumnIndex);
/*      */                 
/*      */ 
/* 4808 */                 if ((("table".equalsIgnoreCase(tableType)) || ("base table".equalsIgnoreCase(tableType))) && (shouldReportTables))
/*      */                 {
/*      */ 
/* 4811 */                   row[3] = DatabaseMetaData.TABLE_AS_BYTES;
/*      */                   
/* 4813 */                   if (tablesOrderedByName == null) {
/* 4814 */                     tablesOrderedByName = new TreeMap();
/*      */                   }
/*      */                   
/* 4817 */                   tablesOrderedByName.put(results.getString(1), row);
/*      */                 }
/* 4819 */                 else if (("view".equalsIgnoreCase(tableType)) && (shouldReportViews))
/*      */                 {
/* 4821 */                   row[3] = DatabaseMetaData.VIEW_AS_BYTES;
/*      */                   
/* 4823 */                   if (viewsOrderedByName == null) {
/* 4824 */                     viewsOrderedByName = new TreeMap();
/*      */                   }
/*      */                   
/* 4827 */                   viewsOrderedByName.put(results.getString(1), row);
/*      */                 }
/* 4829 */                 else if (!hasTableTypes)
/*      */                 {
/* 4831 */                   row[3] = DatabaseMetaData.TABLE_AS_BYTES;
/*      */                   
/* 4833 */                   if (tablesOrderedByName == null) {
/* 4834 */                     tablesOrderedByName = new TreeMap();
/*      */                   }
/*      */                   
/* 4837 */                   tablesOrderedByName.put(results.getString(1), row);
/*      */                 }
/*      */                 
/*      */               }
/* 4841 */               else if (shouldReportTables)
/*      */               {
/* 4843 */                 row[3] = DatabaseMetaData.TABLE_AS_BYTES;
/*      */                 
/* 4845 */                 if (tablesOrderedByName == null) {
/* 4846 */                   tablesOrderedByName = new TreeMap();
/*      */                 }
/*      */                 
/* 4849 */                 tablesOrderedByName.put(results.getString(1), row);
/*      */               }
/*      */             }
/*      */             
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 4858 */             if (tablesOrderedByName != null) {
/* 4859 */               Iterator tablesIter = tablesOrderedByName.values().iterator();
/*      */               
/*      */ 
/* 4862 */               while (tablesIter.hasNext()) {
/* 4863 */                 this.val$tuples.add(tablesIter.next());
/*      */               }
/*      */             }
/*      */             
/* 4867 */             if (viewsOrderedByName != null) {
/* 4868 */               Iterator viewsIter = viewsOrderedByName.values().iterator();
/*      */               
/*      */ 
/* 4871 */               while (viewsIter.hasNext()) {
/* 4872 */                 this.val$tuples.add(viewsIter.next());
/*      */               }
/*      */             }
/*      */           }
/*      */           finally {
/* 4877 */             if (results != null) {
/*      */               try {
/* 4879 */                 results.close();
/*      */               }
/*      */               catch (Exception ex) {}
/*      */               
/*      */ 
/* 4884 */               results = null;
/*      */             }
/*      */           }
/*      */         }
/*      */       }.doForAll();
/*      */     }
/*      */     finally {
/* 4891 */       if (stmt != null) {
/* 4892 */         stmt.close();
/*      */       }
/*      */     }
/*      */     
/* 4896 */     java.sql.ResultSet tables = buildResultSet(fields, tuples);
/*      */     
/* 4898 */     return tables;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public java.sql.ResultSet getTableTypes()
/*      */     throws SQLException
/*      */   {
/* 4919 */     ArrayList tuples = new ArrayList();
/* 4920 */     Field[] fields = new Field[1];
/* 4921 */     fields[0] = new Field("", "TABLE_TYPE", 12, 5);
/*      */     
/* 4923 */     byte[][] tableTypeRow = new byte[1][];
/* 4924 */     tableTypeRow[0] = TABLE_AS_BYTES;
/* 4925 */     tuples.add(tableTypeRow);
/*      */     
/* 4927 */     if (this.conn.versionMeetsMinimum(5, 0, 1)) {
/* 4928 */       byte[][] viewTypeRow = new byte[1][];
/* 4929 */       viewTypeRow[0] = VIEW_AS_BYTES;
/* 4930 */       tuples.add(viewTypeRow);
/*      */     }
/*      */     
/* 4933 */     byte[][] tempTypeRow = new byte[1][];
/* 4934 */     tempTypeRow[0] = s2b("LOCAL TEMPORARY");
/* 4935 */     tuples.add(tempTypeRow);
/*      */     
/* 4937 */     return buildResultSet(fields, tuples);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public String getTimeDateFunctions()
/*      */     throws SQLException
/*      */   {
/* 4948 */     return "DAYOFWEEK,WEEKDAY,DAYOFMONTH,DAYOFYEAR,MONTH,DAYNAME,MONTHNAME,QUARTER,WEEK,YEAR,HOUR,MINUTE,SECOND,PERIOD_ADD,PERIOD_DIFF,TO_DAYS,FROM_DAYS,DATE_FORMAT,TIME_FORMAT,CURDATE,CURRENT_DATE,CURTIME,CURRENT_TIME,NOW,SYSDATE,CURRENT_TIMESTAMP,UNIX_TIMESTAMP,FROM_UNIXTIME,SEC_TO_TIME,TIME_TO_SEC";
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   private final String val$tableNamePat;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   private final String[] val$types;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   private final ArrayList val$tuples;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   private final Map val$procedureRowsOrderedByName;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   private final boolean val$returnFunctions;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   private final ArrayList val$procedureRows;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   private final String val$foreignSchema;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   private final String val$primaryCatalog;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   private final String val$primarySchema;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   private final ArrayList val$tuples;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public java.sql.ResultSet getTypeInfo()
/*      */     throws SQLException
/*      */   {
/* 5057 */     Field[] fields = new Field[18];
/* 5058 */     fields[0] = new Field("", "TYPE_NAME", 1, 32);
/* 5059 */     fields[1] = new Field("", "DATA_TYPE", 5, 5);
/* 5060 */     fields[2] = new Field("", "PRECISION", 4, 10);
/* 5061 */     fields[3] = new Field("", "LITERAL_PREFIX", 1, 4);
/* 5062 */     fields[4] = new Field("", "LITERAL_SUFFIX", 1, 4);
/* 5063 */     fields[5] = new Field("", "CREATE_PARAMS", 1, 32);
/* 5064 */     fields[6] = new Field("", "NULLABLE", 5, 5);
/* 5065 */     fields[7] = new Field("", "CASE_SENSITIVE", 1, 3);
/* 5066 */     fields[8] = new Field("", "SEARCHABLE", 5, 3);
/* 5067 */     fields[9] = new Field("", "UNSIGNED_ATTRIBUTE", 1, 3);
/* 5068 */     fields[10] = new Field("", "FIXED_PREC_SCALE", 1, 3);
/* 5069 */     fields[11] = new Field("", "AUTO_INCREMENT", 1, 3);
/* 5070 */     fields[12] = new Field("", "LOCAL_TYPE_NAME", 1, 32);
/* 5071 */     fields[13] = new Field("", "MINIMUM_SCALE", 5, 5);
/* 5072 */     fields[14] = new Field("", "MAXIMUM_SCALE", 5, 5);
/* 5073 */     fields[15] = new Field("", "SQL_DATA_TYPE", 4, 10);
/* 5074 */     fields[16] = new Field("", "SQL_DATETIME_SUB", 4, 10);
/* 5075 */     fields[17] = new Field("", "NUM_PREC_RADIX", 4, 10);
/*      */     
/* 5077 */     byte[][] rowVal = (byte[][])null;
/* 5078 */     ArrayList tuples = new ArrayList();
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 5087 */     rowVal = new byte[18][];
/* 5088 */     rowVal[0] = s2b("BIT");
/* 5089 */     rowVal[1] = Integer.toString(-7).getBytes();
/*      */     
/*      */ 
/* 5092 */     rowVal[2] = s2b("1");
/* 5093 */     rowVal[3] = s2b("");
/* 5094 */     rowVal[4] = s2b("");
/* 5095 */     rowVal[5] = s2b("");
/* 5096 */     rowVal[6] = Integer.toString(1).getBytes();
/*      */     
/*      */ 
/*      */ 
/* 5100 */     rowVal[7] = s2b("true");
/* 5101 */     rowVal[8] = Integer.toString(3).getBytes();
/*      */     
/*      */ 
/*      */ 
/* 5105 */     rowVal[9] = s2b("false");
/* 5106 */     rowVal[10] = s2b("false");
/* 5107 */     rowVal[11] = s2b("false");
/* 5108 */     rowVal[12] = s2b("BIT");
/* 5109 */     rowVal[13] = s2b("0");
/* 5110 */     rowVal[14] = s2b("0");
/* 5111 */     rowVal[15] = s2b("0");
/* 5112 */     rowVal[16] = s2b("0");
/* 5113 */     rowVal[17] = s2b("10");
/* 5114 */     tuples.add(rowVal);
/*      */     
/*      */ 
/*      */ 
/*      */ 
/* 5119 */     rowVal = new byte[18][];
/* 5120 */     rowVal[0] = s2b("BOOL");
/* 5121 */     rowVal[1] = Integer.toString(-7).getBytes();
/*      */     
/*      */ 
/* 5124 */     rowVal[2] = s2b("1");
/* 5125 */     rowVal[3] = s2b("");
/* 5126 */     rowVal[4] = s2b("");
/* 5127 */     rowVal[5] = s2b("");
/* 5128 */     rowVal[6] = Integer.toString(1).getBytes();
/*      */     
/*      */ 
/*      */ 
/* 5132 */     rowVal[7] = s2b("true");
/* 5133 */     rowVal[8] = Integer.toString(3).getBytes();
/*      */     
/*      */ 
/*      */ 
/* 5137 */     rowVal[9] = s2b("false");
/* 5138 */     rowVal[10] = s2b("false");
/* 5139 */     rowVal[11] = s2b("false");
/* 5140 */     rowVal[12] = s2b("BOOL");
/* 5141 */     rowVal[13] = s2b("0");
/* 5142 */     rowVal[14] = s2b("0");
/* 5143 */     rowVal[15] = s2b("0");
/* 5144 */     rowVal[16] = s2b("0");
/* 5145 */     rowVal[17] = s2b("10");
/* 5146 */     tuples.add(rowVal);
/*      */     
/*      */ 
/*      */ 
/*      */ 
/* 5151 */     rowVal = new byte[18][];
/* 5152 */     rowVal[0] = s2b("TINYINT");
/* 5153 */     rowVal[1] = Integer.toString(-6).getBytes();
/*      */     
/*      */ 
/* 5156 */     rowVal[2] = s2b("3");
/* 5157 */     rowVal[3] = s2b("");
/* 5158 */     rowVal[4] = s2b("");
/* 5159 */     rowVal[5] = s2b("[(M)] [UNSIGNED] [ZEROFILL]");
/* 5160 */     rowVal[6] = Integer.toString(1).getBytes();
/*      */     
/*      */ 
/*      */ 
/* 5164 */     rowVal[7] = s2b("false");
/* 5165 */     rowVal[8] = Integer.toString(3).getBytes();
/*      */     
/*      */ 
/*      */ 
/* 5169 */     rowVal[9] = s2b("true");
/* 5170 */     rowVal[10] = s2b("false");
/* 5171 */     rowVal[11] = s2b("true");
/* 5172 */     rowVal[12] = s2b("TINYINT");
/* 5173 */     rowVal[13] = s2b("0");
/* 5174 */     rowVal[14] = s2b("0");
/* 5175 */     rowVal[15] = s2b("0");
/* 5176 */     rowVal[16] = s2b("0");
/* 5177 */     rowVal[17] = s2b("10");
/* 5178 */     tuples.add(rowVal);
/*      */     
/* 5180 */     rowVal = new byte[18][];
/* 5181 */     rowVal[0] = s2b("TINYINT UNSIGNED");
/* 5182 */     rowVal[1] = Integer.toString(-6).getBytes();
/*      */     
/*      */ 
/* 5185 */     rowVal[2] = s2b("3");
/* 5186 */     rowVal[3] = s2b("");
/* 5187 */     rowVal[4] = s2b("");
/* 5188 */     rowVal[5] = s2b("[(M)] [UNSIGNED] [ZEROFILL]");
/* 5189 */     rowVal[6] = Integer.toString(1).getBytes();
/*      */     
/*      */ 
/*      */ 
/* 5193 */     rowVal[7] = s2b("false");
/* 5194 */     rowVal[8] = Integer.toString(3).getBytes();
/*      */     
/*      */ 
/*      */ 
/* 5198 */     rowVal[9] = s2b("true");
/* 5199 */     rowVal[10] = s2b("false");
/* 5200 */     rowVal[11] = s2b("true");
/* 5201 */     rowVal[12] = s2b("TINYINT UNSIGNED");
/* 5202 */     rowVal[13] = s2b("0");
/* 5203 */     rowVal[14] = s2b("0");
/* 5204 */     rowVal[15] = s2b("0");
/* 5205 */     rowVal[16] = s2b("0");
/* 5206 */     rowVal[17] = s2b("10");
/* 5207 */     tuples.add(rowVal);
/*      */     
/*      */ 
/*      */ 
/*      */ 
/* 5212 */     rowVal = new byte[18][];
/* 5213 */     rowVal[0] = s2b("BIGINT");
/* 5214 */     rowVal[1] = Integer.toString(-5).getBytes();
/*      */     
/*      */ 
/* 5217 */     rowVal[2] = s2b("19");
/* 5218 */     rowVal[3] = s2b("");
/* 5219 */     rowVal[4] = s2b("");
/* 5220 */     rowVal[5] = s2b("[(M)] [UNSIGNED] [ZEROFILL]");
/* 5221 */     rowVal[6] = Integer.toString(1).getBytes();
/*      */     
/*      */ 
/*      */ 
/* 5225 */     rowVal[7] = s2b("false");
/* 5226 */     rowVal[8] = Integer.toString(3).getBytes();
/*      */     
/*      */ 
/*      */ 
/* 5230 */     rowVal[9] = s2b("true");
/* 5231 */     rowVal[10] = s2b("false");
/* 5232 */     rowVal[11] = s2b("true");
/* 5233 */     rowVal[12] = s2b("BIGINT");
/* 5234 */     rowVal[13] = s2b("0");
/* 5235 */     rowVal[14] = s2b("0");
/* 5236 */     rowVal[15] = s2b("0");
/* 5237 */     rowVal[16] = s2b("0");
/* 5238 */     rowVal[17] = s2b("10");
/* 5239 */     tuples.add(rowVal);
/*      */     
/* 5241 */     rowVal = new byte[18][];
/* 5242 */     rowVal[0] = s2b("BIGINT UNSIGNED");
/* 5243 */     rowVal[1] = Integer.toString(-5).getBytes();
/*      */     
/*      */ 
/* 5246 */     rowVal[2] = s2b("20");
/* 5247 */     rowVal[3] = s2b("");
/* 5248 */     rowVal[4] = s2b("");
/* 5249 */     rowVal[5] = s2b("[(M)] [ZEROFILL]");
/* 5250 */     rowVal[6] = Integer.toString(1).getBytes();
/*      */     
/*      */ 
/*      */ 
/* 5254 */     rowVal[7] = s2b("false");
/* 5255 */     rowVal[8] = Integer.toString(3).getBytes();
/*      */     
/*      */ 
/*      */ 
/* 5259 */     rowVal[9] = s2b("true");
/* 5260 */     rowVal[10] = s2b("false");
/* 5261 */     rowVal[11] = s2b("true");
/* 5262 */     rowVal[12] = s2b("BIGINT UNSIGNED");
/* 5263 */     rowVal[13] = s2b("0");
/* 5264 */     rowVal[14] = s2b("0");
/* 5265 */     rowVal[15] = s2b("0");
/* 5266 */     rowVal[16] = s2b("0");
/* 5267 */     rowVal[17] = s2b("10");
/* 5268 */     tuples.add(rowVal);
/*      */     
/*      */ 
/*      */ 
/*      */ 
/* 5273 */     rowVal = new byte[18][];
/* 5274 */     rowVal[0] = s2b("LONG VARBINARY");
/* 5275 */     rowVal[1] = Integer.toString(-4).getBytes();
/*      */     
/*      */ 
/* 5278 */     rowVal[2] = s2b("16777215");
/* 5279 */     rowVal[3] = s2b("'");
/* 5280 */     rowVal[4] = s2b("'");
/* 5281 */     rowVal[5] = s2b("");
/* 5282 */     rowVal[6] = Integer.toString(1).getBytes();
/*      */     
/*      */ 
/*      */ 
/* 5286 */     rowVal[7] = s2b("true");
/* 5287 */     rowVal[8] = Integer.toString(3).getBytes();
/*      */     
/*      */ 
/*      */ 
/* 5291 */     rowVal[9] = s2b("false");
/* 5292 */     rowVal[10] = s2b("false");
/* 5293 */     rowVal[11] = s2b("false");
/* 5294 */     rowVal[12] = s2b("LONG VARBINARY");
/* 5295 */     rowVal[13] = s2b("0");
/* 5296 */     rowVal[14] = s2b("0");
/* 5297 */     rowVal[15] = s2b("0");
/* 5298 */     rowVal[16] = s2b("0");
/* 5299 */     rowVal[17] = s2b("10");
/* 5300 */     tuples.add(rowVal);
/*      */     
/*      */ 
/*      */ 
/*      */ 
/* 5305 */     rowVal = new byte[18][];
/* 5306 */     rowVal[0] = s2b("MEDIUMBLOB");
/* 5307 */     rowVal[1] = Integer.toString(-4).getBytes();
/*      */     
/*      */ 
/* 5310 */     rowVal[2] = s2b("16777215");
/* 5311 */     rowVal[3] = s2b("'");
/* 5312 */     rowVal[4] = s2b("'");
/* 5313 */     rowVal[5] = s2b("");
/* 5314 */     rowVal[6] = Integer.toString(1).getBytes();
/*      */     
/*      */ 
/*      */ 
/* 5318 */     rowVal[7] = s2b("true");
/* 5319 */     rowVal[8] = Integer.toString(3).getBytes();
/*      */     
/*      */ 
/*      */ 
/* 5323 */     rowVal[9] = s2b("false");
/* 5324 */     rowVal[10] = s2b("false");
/* 5325 */     rowVal[11] = s2b("false");
/* 5326 */     rowVal[12] = s2b("MEDIUMBLOB");
/* 5327 */     rowVal[13] = s2b("0");
/* 5328 */     rowVal[14] = s2b("0");
/* 5329 */     rowVal[15] = s2b("0");
/* 5330 */     rowVal[16] = s2b("0");
/* 5331 */     rowVal[17] = s2b("10");
/* 5332 */     tuples.add(rowVal);
/*      */     
/*      */ 
/*      */ 
/*      */ 
/* 5337 */     rowVal = new byte[18][];
/* 5338 */     rowVal[0] = s2b("LONGBLOB");
/* 5339 */     rowVal[1] = Integer.toString(-4).getBytes();
/*      */     
/*      */ 
/* 5342 */     rowVal[2] = Integer.toString(Integer.MAX_VALUE).getBytes();
/*      */     
/*      */ 
/* 5345 */     rowVal[3] = s2b("'");
/* 5346 */     rowVal[4] = s2b("'");
/* 5347 */     rowVal[5] = s2b("");
/* 5348 */     rowVal[6] = Integer.toString(1).getBytes();
/*      */     
/*      */ 
/*      */ 
/* 5352 */     rowVal[7] = s2b("true");
/* 5353 */     rowVal[8] = Integer.toString(3).getBytes();
/*      */     
/*      */ 
/*      */ 
/* 5357 */     rowVal[9] = s2b("false");
/* 5358 */     rowVal[10] = s2b("false");
/* 5359 */     rowVal[11] = s2b("false");
/* 5360 */     rowVal[12] = s2b("LONGBLOB");
/* 5361 */     rowVal[13] = s2b("0");
/* 5362 */     rowVal[14] = s2b("0");
/* 5363 */     rowVal[15] = s2b("0");
/* 5364 */     rowVal[16] = s2b("0");
/* 5365 */     rowVal[17] = s2b("10");
/* 5366 */     tuples.add(rowVal);
/*      */     
/*      */ 
/*      */ 
/*      */ 
/* 5371 */     rowVal = new byte[18][];
/* 5372 */     rowVal[0] = s2b("BLOB");
/* 5373 */     rowVal[1] = Integer.toString(-4).getBytes();
/*      */     
/*      */ 
/* 5376 */     rowVal[2] = s2b("65535");
/* 5377 */     rowVal[3] = s2b("'");
/* 5378 */     rowVal[4] = s2b("'");
/* 5379 */     rowVal[5] = s2b("");
/* 5380 */     rowVal[6] = Integer.toString(1).getBytes();
/*      */     
/*      */ 
/*      */ 
/* 5384 */     rowVal[7] = s2b("true");
/* 5385 */     rowVal[8] = Integer.toString(3).getBytes();
/*      */     
/*      */ 
/*      */ 
/* 5389 */     rowVal[9] = s2b("false");
/* 5390 */     rowVal[10] = s2b("false");
/* 5391 */     rowVal[11] = s2b("false");
/* 5392 */     rowVal[12] = s2b("BLOB");
/* 5393 */     rowVal[13] = s2b("0");
/* 5394 */     rowVal[14] = s2b("0");
/* 5395 */     rowVal[15] = s2b("0");
/* 5396 */     rowVal[16] = s2b("0");
/* 5397 */     rowVal[17] = s2b("10");
/* 5398 */     tuples.add(rowVal);
/*      */     
/*      */ 
/*      */ 
/*      */ 
/* 5403 */     rowVal = new byte[18][];
/* 5404 */     rowVal[0] = s2b("TINYBLOB");
/* 5405 */     rowVal[1] = Integer.toString(-4).getBytes();
/*      */     
/*      */ 
/* 5408 */     rowVal[2] = s2b("255");
/* 5409 */     rowVal[3] = s2b("'");
/* 5410 */     rowVal[4] = s2b("'");
/* 5411 */     rowVal[5] = s2b("");
/* 5412 */     rowVal[6] = Integer.toString(1).getBytes();
/*      */     
/*      */ 
/*      */ 
/* 5416 */     rowVal[7] = s2b("true");
/* 5417 */     rowVal[8] = Integer.toString(3).getBytes();
/*      */     
/*      */ 
/*      */ 
/* 5421 */     rowVal[9] = s2b("false");
/* 5422 */     rowVal[10] = s2b("false");
/* 5423 */     rowVal[11] = s2b("false");
/* 5424 */     rowVal[12] = s2b("TINYBLOB");
/* 5425 */     rowVal[13] = s2b("0");
/* 5426 */     rowVal[14] = s2b("0");
/* 5427 */     rowVal[15] = s2b("0");
/* 5428 */     rowVal[16] = s2b("0");
/* 5429 */     rowVal[17] = s2b("10");
/* 5430 */     tuples.add(rowVal);
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 5436 */     rowVal = new byte[18][];
/* 5437 */     rowVal[0] = s2b("VARBINARY");
/* 5438 */     rowVal[1] = Integer.toString(-3).getBytes();
/*      */     
/*      */ 
/* 5441 */     rowVal[2] = s2b("255");
/* 5442 */     rowVal[3] = s2b("'");
/* 5443 */     rowVal[4] = s2b("'");
/* 5444 */     rowVal[5] = s2b("(M)");
/* 5445 */     rowVal[6] = Integer.toString(1).getBytes();
/*      */     
/*      */ 
/*      */ 
/* 5449 */     rowVal[7] = s2b("true");
/* 5450 */     rowVal[8] = Integer.toString(3).getBytes();
/*      */     
/*      */ 
/*      */ 
/* 5454 */     rowVal[9] = s2b("false");
/* 5455 */     rowVal[10] = s2b("false");
/* 5456 */     rowVal[11] = s2b("false");
/* 5457 */     rowVal[12] = s2b("VARBINARY");
/* 5458 */     rowVal[13] = s2b("0");
/* 5459 */     rowVal[14] = s2b("0");
/* 5460 */     rowVal[15] = s2b("0");
/* 5461 */     rowVal[16] = s2b("0");
/* 5462 */     rowVal[17] = s2b("10");
/* 5463 */     tuples.add(rowVal);
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 5469 */     rowVal = new byte[18][];
/* 5470 */     rowVal[0] = s2b("BINARY");
/* 5471 */     rowVal[1] = Integer.toString(-2).getBytes();
/*      */     
/*      */ 
/* 5474 */     rowVal[2] = s2b("255");
/* 5475 */     rowVal[3] = s2b("'");
/* 5476 */     rowVal[4] = s2b("'");
/* 5477 */     rowVal[5] = s2b("(M)");
/* 5478 */     rowVal[6] = Integer.toString(1).getBytes();
/*      */     
/*      */ 
/*      */ 
/* 5482 */     rowVal[7] = s2b("true");
/* 5483 */     rowVal[8] = Integer.toString(3).getBytes();
/*      */     
/*      */ 
/*      */ 
/* 5487 */     rowVal[9] = s2b("false");
/* 5488 */     rowVal[10] = s2b("false");
/* 5489 */     rowVal[11] = s2b("false");
/* 5490 */     rowVal[12] = s2b("BINARY");
/* 5491 */     rowVal[13] = s2b("0");
/* 5492 */     rowVal[14] = s2b("0");
/* 5493 */     rowVal[15] = s2b("0");
/* 5494 */     rowVal[16] = s2b("0");
/* 5495 */     rowVal[17] = s2b("10");
/* 5496 */     tuples.add(rowVal);
/*      */     
/*      */ 
/*      */ 
/*      */ 
/* 5501 */     rowVal = new byte[18][];
/* 5502 */     rowVal[0] = s2b("LONG VARCHAR");
/* 5503 */     rowVal[1] = Integer.toString(-1).getBytes();
/*      */     
/*      */ 
/* 5506 */     rowVal[2] = s2b("16777215");
/* 5507 */     rowVal[3] = s2b("'");
/* 5508 */     rowVal[4] = s2b("'");
/* 5509 */     rowVal[5] = s2b("");
/* 5510 */     rowVal[6] = Integer.toString(1).getBytes();
/*      */     
/*      */ 
/*      */ 
/* 5514 */     rowVal[7] = s2b("false");
/* 5515 */     rowVal[8] = Integer.toString(3).getBytes();
/*      */     
/*      */ 
/*      */ 
/* 5519 */     rowVal[9] = s2b("false");
/* 5520 */     rowVal[10] = s2b("false");
/* 5521 */     rowVal[11] = s2b("false");
/* 5522 */     rowVal[12] = s2b("LONG VARCHAR");
/* 5523 */     rowVal[13] = s2b("0");
/* 5524 */     rowVal[14] = s2b("0");
/* 5525 */     rowVal[15] = s2b("0");
/* 5526 */     rowVal[16] = s2b("0");
/* 5527 */     rowVal[17] = s2b("10");
/* 5528 */     tuples.add(rowVal);
/*      */     
/*      */ 
/*      */ 
/*      */ 
/* 5533 */     rowVal = new byte[18][];
/* 5534 */     rowVal[0] = s2b("MEDIUMTEXT");
/* 5535 */     rowVal[1] = Integer.toString(-1).getBytes();
/*      */     
/*      */ 
/* 5538 */     rowVal[2] = s2b("16777215");
/* 5539 */     rowVal[3] = s2b("'");
/* 5540 */     rowVal[4] = s2b("'");
/* 5541 */     rowVal[5] = s2b("");
/* 5542 */     rowVal[6] = Integer.toString(1).getBytes();
/*      */     
/*      */ 
/*      */ 
/* 5546 */     rowVal[7] = s2b("false");
/* 5547 */     rowVal[8] = Integer.toString(3).getBytes();
/*      */     
/*      */ 
/*      */ 
/* 5551 */     rowVal[9] = s2b("false");
/* 5552 */     rowVal[10] = s2b("false");
/* 5553 */     rowVal[11] = s2b("false");
/* 5554 */     rowVal[12] = s2b("MEDIUMTEXT");
/* 5555 */     rowVal[13] = s2b("0");
/* 5556 */     rowVal[14] = s2b("0");
/* 5557 */     rowVal[15] = s2b("0");
/* 5558 */     rowVal[16] = s2b("0");
/* 5559 */     rowVal[17] = s2b("10");
/* 5560 */     tuples.add(rowVal);
/*      */     
/*      */ 
/*      */ 
/*      */ 
/* 5565 */     rowVal = new byte[18][];
/* 5566 */     rowVal[0] = s2b("LONGTEXT");
/* 5567 */     rowVal[1] = Integer.toString(-1).getBytes();
/*      */     
/*      */ 
/* 5570 */     rowVal[2] = Integer.toString(Integer.MAX_VALUE).getBytes();
/*      */     
/*      */ 
/* 5573 */     rowVal[3] = s2b("'");
/* 5574 */     rowVal[4] = s2b("'");
/* 5575 */     rowVal[5] = s2b("");
/* 5576 */     rowVal[6] = Integer.toString(1).getBytes();
/*      */     
/*      */ 
/*      */ 
/* 5580 */     rowVal[7] = s2b("false");
/* 5581 */     rowVal[8] = Integer.toString(3).getBytes();
/*      */     
/*      */ 
/*      */ 
/* 5585 */     rowVal[9] = s2b("false");
/* 5586 */     rowVal[10] = s2b("false");
/* 5587 */     rowVal[11] = s2b("false");
/* 5588 */     rowVal[12] = s2b("LONGTEXT");
/* 5589 */     rowVal[13] = s2b("0");
/* 5590 */     rowVal[14] = s2b("0");
/* 5591 */     rowVal[15] = s2b("0");
/* 5592 */     rowVal[16] = s2b("0");
/* 5593 */     rowVal[17] = s2b("10");
/* 5594 */     tuples.add(rowVal);
/*      */     
/*      */ 
/*      */ 
/*      */ 
/* 5599 */     rowVal = new byte[18][];
/* 5600 */     rowVal[0] = s2b("TEXT");
/* 5601 */     rowVal[1] = Integer.toString(-1).getBytes();
/*      */     
/*      */ 
/* 5604 */     rowVal[2] = s2b("65535");
/* 5605 */     rowVal[3] = s2b("'");
/* 5606 */     rowVal[4] = s2b("'");
/* 5607 */     rowVal[5] = s2b("");
/* 5608 */     rowVal[6] = Integer.toString(1).getBytes();
/*      */     
/*      */ 
/*      */ 
/* 5612 */     rowVal[7] = s2b("false");
/* 5613 */     rowVal[8] = Integer.toString(3).getBytes();
/*      */     
/*      */ 
/*      */ 
/* 5617 */     rowVal[9] = s2b("false");
/* 5618 */     rowVal[10] = s2b("false");
/* 5619 */     rowVal[11] = s2b("false");
/* 5620 */     rowVal[12] = s2b("TEXT");
/* 5621 */     rowVal[13] = s2b("0");
/* 5622 */     rowVal[14] = s2b("0");
/* 5623 */     rowVal[15] = s2b("0");
/* 5624 */     rowVal[16] = s2b("0");
/* 5625 */     rowVal[17] = s2b("10");
/* 5626 */     tuples.add(rowVal);
/*      */     
/*      */ 
/*      */ 
/*      */ 
/* 5631 */     rowVal = new byte[18][];
/* 5632 */     rowVal[0] = s2b("TINYTEXT");
/* 5633 */     rowVal[1] = Integer.toString(-1).getBytes();
/*      */     
/*      */ 
/* 5636 */     rowVal[2] = s2b("255");
/* 5637 */     rowVal[3] = s2b("'");
/* 5638 */     rowVal[4] = s2b("'");
/* 5639 */     rowVal[5] = s2b("");
/* 5640 */     rowVal[6] = Integer.toString(1).getBytes();
/*      */     
/*      */ 
/*      */ 
/* 5644 */     rowVal[7] = s2b("false");
/* 5645 */     rowVal[8] = Integer.toString(3).getBytes();
/*      */     
/*      */ 
/*      */ 
/* 5649 */     rowVal[9] = s2b("false");
/* 5650 */     rowVal[10] = s2b("false");
/* 5651 */     rowVal[11] = s2b("false");
/* 5652 */     rowVal[12] = s2b("TINYTEXT");
/* 5653 */     rowVal[13] = s2b("0");
/* 5654 */     rowVal[14] = s2b("0");
/* 5655 */     rowVal[15] = s2b("0");
/* 5656 */     rowVal[16] = s2b("0");
/* 5657 */     rowVal[17] = s2b("10");
/* 5658 */     tuples.add(rowVal);
/*      */     
/*      */ 
/*      */ 
/*      */ 
/* 5663 */     rowVal = new byte[18][];
/* 5664 */     rowVal[0] = s2b("CHAR");
/* 5665 */     rowVal[1] = Integer.toString(1).getBytes();
/*      */     
/*      */ 
/* 5668 */     rowVal[2] = s2b("255");
/* 5669 */     rowVal[3] = s2b("'");
/* 5670 */     rowVal[4] = s2b("'");
/* 5671 */     rowVal[5] = s2b("(M)");
/* 5672 */     rowVal[6] = Integer.toString(1).getBytes();
/*      */     
/*      */ 
/*      */ 
/* 5676 */     rowVal[7] = s2b("false");
/* 5677 */     rowVal[8] = Integer.toString(3).getBytes();
/*      */     
/*      */ 
/*      */ 
/* 5681 */     rowVal[9] = s2b("false");
/* 5682 */     rowVal[10] = s2b("false");
/* 5683 */     rowVal[11] = s2b("false");
/* 5684 */     rowVal[12] = s2b("CHAR");
/* 5685 */     rowVal[13] = s2b("0");
/* 5686 */     rowVal[14] = s2b("0");
/* 5687 */     rowVal[15] = s2b("0");
/* 5688 */     rowVal[16] = s2b("0");
/* 5689 */     rowVal[17] = s2b("10");
/* 5690 */     tuples.add(rowVal);
/*      */     
/*      */ 
/*      */ 
/* 5694 */     int decimalPrecision = 254;
/*      */     
/* 5696 */     if (this.conn.versionMeetsMinimum(5, 0, 3)) {
/* 5697 */       if (this.conn.versionMeetsMinimum(5, 0, 6)) {
/* 5698 */         decimalPrecision = 65;
/*      */       } else {
/* 5700 */         decimalPrecision = 64;
/*      */       }
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 5708 */     rowVal = new byte[18][];
/* 5709 */     rowVal[0] = s2b("NUMERIC");
/* 5710 */     rowVal[1] = Integer.toString(2).getBytes();
/*      */     
/*      */ 
/* 5713 */     rowVal[2] = s2b(String.valueOf(decimalPrecision));
/* 5714 */     rowVal[3] = s2b("");
/* 5715 */     rowVal[4] = s2b("");
/* 5716 */     rowVal[5] = s2b("[(M[,D])] [ZEROFILL]");
/* 5717 */     rowVal[6] = Integer.toString(1).getBytes();
/*      */     
/*      */ 
/*      */ 
/* 5721 */     rowVal[7] = s2b("false");
/* 5722 */     rowVal[8] = Integer.toString(3).getBytes();
/*      */     
/*      */ 
/*      */ 
/* 5726 */     rowVal[9] = s2b("false");
/* 5727 */     rowVal[10] = s2b("false");
/* 5728 */     rowVal[11] = s2b("true");
/* 5729 */     rowVal[12] = s2b("NUMERIC");
/* 5730 */     rowVal[13] = s2b("-308");
/* 5731 */     rowVal[14] = s2b("308");
/* 5732 */     rowVal[15] = s2b("0");
/* 5733 */     rowVal[16] = s2b("0");
/* 5734 */     rowVal[17] = s2b("10");
/* 5735 */     tuples.add(rowVal);
/*      */     
/*      */ 
/*      */ 
/*      */ 
/* 5740 */     rowVal = new byte[18][];
/* 5741 */     rowVal[0] = s2b("DECIMAL");
/* 5742 */     rowVal[1] = Integer.toString(3).getBytes();
/*      */     
/*      */ 
/* 5745 */     rowVal[2] = s2b(String.valueOf(decimalPrecision));
/* 5746 */     rowVal[3] = s2b("");
/* 5747 */     rowVal[4] = s2b("");
/* 5748 */     rowVal[5] = s2b("[(M[,D])] [ZEROFILL]");
/* 5749 */     rowVal[6] = Integer.toString(1).getBytes();
/*      */     
/*      */ 
/*      */ 
/* 5753 */     rowVal[7] = s2b("false");
/* 5754 */     rowVal[8] = Integer.toString(3).getBytes();
/*      */     
/*      */ 
/*      */ 
/* 5758 */     rowVal[9] = s2b("false");
/* 5759 */     rowVal[10] = s2b("false");
/* 5760 */     rowVal[11] = s2b("true");
/* 5761 */     rowVal[12] = s2b("DECIMAL");
/* 5762 */     rowVal[13] = s2b("-308");
/* 5763 */     rowVal[14] = s2b("308");
/* 5764 */     rowVal[15] = s2b("0");
/* 5765 */     rowVal[16] = s2b("0");
/* 5766 */     rowVal[17] = s2b("10");
/* 5767 */     tuples.add(rowVal);
/*      */     
/*      */ 
/*      */ 
/*      */ 
/* 5772 */     rowVal = new byte[18][];
/* 5773 */     rowVal[0] = s2b("INTEGER");
/* 5774 */     rowVal[1] = Integer.toString(4).getBytes();
/*      */     
/*      */ 
/* 5777 */     rowVal[2] = s2b("10");
/* 5778 */     rowVal[3] = s2b("");
/* 5779 */     rowVal[4] = s2b("");
/* 5780 */     rowVal[5] = s2b("[(M)] [UNSIGNED] [ZEROFILL]");
/* 5781 */     rowVal[6] = Integer.toString(1).getBytes();
/*      */     
/*      */ 
/*      */ 
/* 5785 */     rowVal[7] = s2b("false");
/* 5786 */     rowVal[8] = Integer.toString(3).getBytes();
/*      */     
/*      */ 
/*      */ 
/* 5790 */     rowVal[9] = s2b("true");
/* 5791 */     rowVal[10] = s2b("false");
/* 5792 */     rowVal[11] = s2b("true");
/* 5793 */     rowVal[12] = s2b("INTEGER");
/* 5794 */     rowVal[13] = s2b("0");
/* 5795 */     rowVal[14] = s2b("0");
/* 5796 */     rowVal[15] = s2b("0");
/* 5797 */     rowVal[16] = s2b("0");
/* 5798 */     rowVal[17] = s2b("10");
/* 5799 */     tuples.add(rowVal);
/*      */     
/* 5801 */     rowVal = new byte[18][];
/* 5802 */     rowVal[0] = s2b("INTEGER UNSIGNED");
/* 5803 */     rowVal[1] = Integer.toString(4).getBytes();
/*      */     
/*      */ 
/* 5806 */     rowVal[2] = s2b("10");
/* 5807 */     rowVal[3] = s2b("");
/* 5808 */     rowVal[4] = s2b("");
/* 5809 */     rowVal[5] = s2b("[(M)] [ZEROFILL]");
/* 5810 */     rowVal[6] = Integer.toString(1).getBytes();
/*      */     
/*      */ 
/*      */ 
/* 5814 */     rowVal[7] = s2b("false");
/* 5815 */     rowVal[8] = Integer.toString(3).getBytes();
/*      */     
/*      */ 
/*      */ 
/* 5819 */     rowVal[9] = s2b("true");
/* 5820 */     rowVal[10] = s2b("false");
/* 5821 */     rowVal[11] = s2b("true");
/* 5822 */     rowVal[12] = s2b("INTEGER UNSIGNED");
/* 5823 */     rowVal[13] = s2b("0");
/* 5824 */     rowVal[14] = s2b("0");
/* 5825 */     rowVal[15] = s2b("0");
/* 5826 */     rowVal[16] = s2b("0");
/* 5827 */     rowVal[17] = s2b("10");
/* 5828 */     tuples.add(rowVal);
/*      */     
/*      */ 
/*      */ 
/*      */ 
/* 5833 */     rowVal = new byte[18][];
/* 5834 */     rowVal[0] = s2b("INT");
/* 5835 */     rowVal[1] = Integer.toString(4).getBytes();
/*      */     
/*      */ 
/* 5838 */     rowVal[2] = s2b("10");
/* 5839 */     rowVal[3] = s2b("");
/* 5840 */     rowVal[4] = s2b("");
/* 5841 */     rowVal[5] = s2b("[(M)] [UNSIGNED] [ZEROFILL]");
/* 5842 */     rowVal[6] = Integer.toString(1).getBytes();
/*      */     
/*      */ 
/*      */ 
/* 5846 */     rowVal[7] = s2b("false");
/* 5847 */     rowVal[8] = Integer.toString(3).getBytes();
/*      */     
/*      */ 
/*      */ 
/* 5851 */     rowVal[9] = s2b("true");
/* 5852 */     rowVal[10] = s2b("false");
/* 5853 */     rowVal[11] = s2b("true");
/* 5854 */     rowVal[12] = s2b("INT");
/* 5855 */     rowVal[13] = s2b("0");
/* 5856 */     rowVal[14] = s2b("0");
/* 5857 */     rowVal[15] = s2b("0");
/* 5858 */     rowVal[16] = s2b("0");
/* 5859 */     rowVal[17] = s2b("10");
/* 5860 */     tuples.add(rowVal);
/*      */     
/* 5862 */     rowVal = new byte[18][];
/* 5863 */     rowVal[0] = s2b("INT UNSIGNED");
/* 5864 */     rowVal[1] = Integer.toString(4).getBytes();
/*      */     
/*      */ 
/* 5867 */     rowVal[2] = s2b("10");
/* 5868 */     rowVal[3] = s2b("");
/* 5869 */     rowVal[4] = s2b("");
/* 5870 */     rowVal[5] = s2b("[(M)] [ZEROFILL]");
/* 5871 */     rowVal[6] = Integer.toString(1).getBytes();
/*      */     
/*      */ 
/*      */ 
/* 5875 */     rowVal[7] = s2b("false");
/* 5876 */     rowVal[8] = Integer.toString(3).getBytes();
/*      */     
/*      */ 
/*      */ 
/* 5880 */     rowVal[9] = s2b("true");
/* 5881 */     rowVal[10] = s2b("false");
/* 5882 */     rowVal[11] = s2b("true");
/* 5883 */     rowVal[12] = s2b("INT UNSIGNED");
/* 5884 */     rowVal[13] = s2b("0");
/* 5885 */     rowVal[14] = s2b("0");
/* 5886 */     rowVal[15] = s2b("0");
/* 5887 */     rowVal[16] = s2b("0");
/* 5888 */     rowVal[17] = s2b("10");
/* 5889 */     tuples.add(rowVal);
/*      */     
/*      */ 
/*      */ 
/*      */ 
/* 5894 */     rowVal = new byte[18][];
/* 5895 */     rowVal[0] = s2b("MEDIUMINT");
/* 5896 */     rowVal[1] = Integer.toString(4).getBytes();
/*      */     
/*      */ 
/* 5899 */     rowVal[2] = s2b("7");
/* 5900 */     rowVal[3] = s2b("");
/* 5901 */     rowVal[4] = s2b("");
/* 5902 */     rowVal[5] = s2b("[(M)] [UNSIGNED] [ZEROFILL]");
/* 5903 */     rowVal[6] = Integer.toString(1).getBytes();
/*      */     
/*      */ 
/*      */ 
/* 5907 */     rowVal[7] = s2b("false");
/* 5908 */     rowVal[8] = Integer.toString(3).getBytes();
/*      */     
/*      */ 
/*      */ 
/* 5912 */     rowVal[9] = s2b("true");
/* 5913 */     rowVal[10] = s2b("false");
/* 5914 */     rowVal[11] = s2b("true");
/* 5915 */     rowVal[12] = s2b("MEDIUMINT");
/* 5916 */     rowVal[13] = s2b("0");
/* 5917 */     rowVal[14] = s2b("0");
/* 5918 */     rowVal[15] = s2b("0");
/* 5919 */     rowVal[16] = s2b("0");
/* 5920 */     rowVal[17] = s2b("10");
/* 5921 */     tuples.add(rowVal);
/*      */     
/* 5923 */     rowVal = new byte[18][];
/* 5924 */     rowVal[0] = s2b("MEDIUMINT UNSIGNED");
/* 5925 */     rowVal[1] = Integer.toString(4).getBytes();
/*      */     
/*      */ 
/* 5928 */     rowVal[2] = s2b("8");
/* 5929 */     rowVal[3] = s2b("");
/* 5930 */     rowVal[4] = s2b("");
/* 5931 */     rowVal[5] = s2b("[(M)] [ZEROFILL]");
/* 5932 */     rowVal[6] = Integer.toString(1).getBytes();
/*      */     
/*      */ 
/*      */ 
/* 5936 */     rowVal[7] = s2b("false");
/* 5937 */     rowVal[8] = Integer.toString(3).getBytes();
/*      */     
/*      */ 
/*      */ 
/* 5941 */     rowVal[9] = s2b("true");
/* 5942 */     rowVal[10] = s2b("false");
/* 5943 */     rowVal[11] = s2b("true");
/* 5944 */     rowVal[12] = s2b("MEDIUMINT UNSIGNED");
/* 5945 */     rowVal[13] = s2b("0");
/* 5946 */     rowVal[14] = s2b("0");
/* 5947 */     rowVal[15] = s2b("0");
/* 5948 */     rowVal[16] = s2b("0");
/* 5949 */     rowVal[17] = s2b("10");
/* 5950 */     tuples.add(rowVal);
/*      */     
/*      */ 
/*      */ 
/*      */ 
/* 5955 */     rowVal = new byte[18][];
/* 5956 */     rowVal[0] = s2b("SMALLINT");
/* 5957 */     rowVal[1] = Integer.toString(5).getBytes();
/*      */     
/*      */ 
/* 5960 */     rowVal[2] = s2b("5");
/* 5961 */     rowVal[3] = s2b("");
/* 5962 */     rowVal[4] = s2b("");
/* 5963 */     rowVal[5] = s2b("[(M)] [UNSIGNED] [ZEROFILL]");
/* 5964 */     rowVal[6] = Integer.toString(1).getBytes();
/*      */     
/*      */ 
/*      */ 
/* 5968 */     rowVal[7] = s2b("false");
/* 5969 */     rowVal[8] = Integer.toString(3).getBytes();
/*      */     
/*      */ 
/*      */ 
/* 5973 */     rowVal[9] = s2b("true");
/* 5974 */     rowVal[10] = s2b("false");
/* 5975 */     rowVal[11] = s2b("true");
/* 5976 */     rowVal[12] = s2b("SMALLINT");
/* 5977 */     rowVal[13] = s2b("0");
/* 5978 */     rowVal[14] = s2b("0");
/* 5979 */     rowVal[15] = s2b("0");
/* 5980 */     rowVal[16] = s2b("0");
/* 5981 */     rowVal[17] = s2b("10");
/* 5982 */     tuples.add(rowVal);
/*      */     
/* 5984 */     rowVal = new byte[18][];
/* 5985 */     rowVal[0] = s2b("SMALLINT UNSIGNED");
/* 5986 */     rowVal[1] = Integer.toString(5).getBytes();
/*      */     
/*      */ 
/* 5989 */     rowVal[2] = s2b("5");
/* 5990 */     rowVal[3] = s2b("");
/* 5991 */     rowVal[4] = s2b("");
/* 5992 */     rowVal[5] = s2b("[(M)] [ZEROFILL]");
/* 5993 */     rowVal[6] = Integer.toString(1).getBytes();
/*      */     
/*      */ 
/*      */ 
/* 5997 */     rowVal[7] = s2b("false");
/* 5998 */     rowVal[8] = Integer.toString(3).getBytes();
/*      */     
/*      */ 
/*      */ 
/* 6002 */     rowVal[9] = s2b("true");
/* 6003 */     rowVal[10] = s2b("false");
/* 6004 */     rowVal[11] = s2b("true");
/* 6005 */     rowVal[12] = s2b("SMALLINT UNSIGNED");
/* 6006 */     rowVal[13] = s2b("0");
/* 6007 */     rowVal[14] = s2b("0");
/* 6008 */     rowVal[15] = s2b("0");
/* 6009 */     rowVal[16] = s2b("0");
/* 6010 */     rowVal[17] = s2b("10");
/* 6011 */     tuples.add(rowVal);
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 6017 */     rowVal = new byte[18][];
/* 6018 */     rowVal[0] = s2b("FLOAT");
/* 6019 */     rowVal[1] = Integer.toString(7).getBytes();
/*      */     
/*      */ 
/* 6022 */     rowVal[2] = s2b("10");
/* 6023 */     rowVal[3] = s2b("");
/* 6024 */     rowVal[4] = s2b("");
/* 6025 */     rowVal[5] = s2b("[(M,D)] [ZEROFILL]");
/* 6026 */     rowVal[6] = Integer.toString(1).getBytes();
/*      */     
/*      */ 
/*      */ 
/* 6030 */     rowVal[7] = s2b("false");
/* 6031 */     rowVal[8] = Integer.toString(3).getBytes();
/*      */     
/*      */ 
/*      */ 
/* 6035 */     rowVal[9] = s2b("false");
/* 6036 */     rowVal[10] = s2b("false");
/* 6037 */     rowVal[11] = s2b("true");
/* 6038 */     rowVal[12] = s2b("FLOAT");
/* 6039 */     rowVal[13] = s2b("-38");
/* 6040 */     rowVal[14] = s2b("38");
/* 6041 */     rowVal[15] = s2b("0");
/* 6042 */     rowVal[16] = s2b("0");
/* 6043 */     rowVal[17] = s2b("10");
/* 6044 */     tuples.add(rowVal);
/*      */     
/*      */ 
/*      */ 
/*      */ 
/* 6049 */     rowVal = new byte[18][];
/* 6050 */     rowVal[0] = s2b("DOUBLE");
/* 6051 */     rowVal[1] = Integer.toString(8).getBytes();
/*      */     
/*      */ 
/* 6054 */     rowVal[2] = s2b("17");
/* 6055 */     rowVal[3] = s2b("");
/* 6056 */     rowVal[4] = s2b("");
/* 6057 */     rowVal[5] = s2b("[(M,D)] [ZEROFILL]");
/* 6058 */     rowVal[6] = Integer.toString(1).getBytes();
/*      */     
/*      */ 
/*      */ 
/* 6062 */     rowVal[7] = s2b("false");
/* 6063 */     rowVal[8] = Integer.toString(3).getBytes();
/*      */     
/*      */ 
/*      */ 
/* 6067 */     rowVal[9] = s2b("false");
/* 6068 */     rowVal[10] = s2b("false");
/* 6069 */     rowVal[11] = s2b("true");
/* 6070 */     rowVal[12] = s2b("DOUBLE");
/* 6071 */     rowVal[13] = s2b("-308");
/* 6072 */     rowVal[14] = s2b("308");
/* 6073 */     rowVal[15] = s2b("0");
/* 6074 */     rowVal[16] = s2b("0");
/* 6075 */     rowVal[17] = s2b("10");
/* 6076 */     tuples.add(rowVal);
/*      */     
/*      */ 
/*      */ 
/*      */ 
/* 6081 */     rowVal = new byte[18][];
/* 6082 */     rowVal[0] = s2b("DOUBLE PRECISION");
/* 6083 */     rowVal[1] = Integer.toString(8).getBytes();
/*      */     
/*      */ 
/* 6086 */     rowVal[2] = s2b("17");
/* 6087 */     rowVal[3] = s2b("");
/* 6088 */     rowVal[4] = s2b("");
/* 6089 */     rowVal[5] = s2b("[(M,D)] [ZEROFILL]");
/* 6090 */     rowVal[6] = Integer.toString(1).getBytes();
/*      */     
/*      */ 
/*      */ 
/* 6094 */     rowVal[7] = s2b("false");
/* 6095 */     rowVal[8] = Integer.toString(3).getBytes();
/*      */     
/*      */ 
/*      */ 
/* 6099 */     rowVal[9] = s2b("false");
/* 6100 */     rowVal[10] = s2b("false");
/* 6101 */     rowVal[11] = s2b("true");
/* 6102 */     rowVal[12] = s2b("DOUBLE PRECISION");
/* 6103 */     rowVal[13] = s2b("-308");
/* 6104 */     rowVal[14] = s2b("308");
/* 6105 */     rowVal[15] = s2b("0");
/* 6106 */     rowVal[16] = s2b("0");
/* 6107 */     rowVal[17] = s2b("10");
/* 6108 */     tuples.add(rowVal);
/*      */     
/*      */ 
/*      */ 
/*      */ 
/* 6113 */     rowVal = new byte[18][];
/* 6114 */     rowVal[0] = s2b("REAL");
/* 6115 */     rowVal[1] = Integer.toString(8).getBytes();
/*      */     
/*      */ 
/* 6118 */     rowVal[2] = s2b("17");
/* 6119 */     rowVal[3] = s2b("");
/* 6120 */     rowVal[4] = s2b("");
/* 6121 */     rowVal[5] = s2b("[(M,D)] [ZEROFILL]");
/* 6122 */     rowVal[6] = Integer.toString(1).getBytes();
/*      */     
/*      */ 
/*      */ 
/* 6126 */     rowVal[7] = s2b("false");
/* 6127 */     rowVal[8] = Integer.toString(3).getBytes();
/*      */     
/*      */ 
/*      */ 
/* 6131 */     rowVal[9] = s2b("false");
/* 6132 */     rowVal[10] = s2b("false");
/* 6133 */     rowVal[11] = s2b("true");
/* 6134 */     rowVal[12] = s2b("REAL");
/* 6135 */     rowVal[13] = s2b("-308");
/* 6136 */     rowVal[14] = s2b("308");
/* 6137 */     rowVal[15] = s2b("0");
/* 6138 */     rowVal[16] = s2b("0");
/* 6139 */     rowVal[17] = s2b("10");
/* 6140 */     tuples.add(rowVal);
/*      */     
/*      */ 
/*      */ 
/*      */ 
/* 6145 */     rowVal = new byte[18][];
/* 6146 */     rowVal[0] = s2b("VARCHAR");
/* 6147 */     rowVal[1] = Integer.toString(12).getBytes();
/*      */     
/*      */ 
/* 6150 */     rowVal[2] = s2b("255");
/* 6151 */     rowVal[3] = s2b("'");
/* 6152 */     rowVal[4] = s2b("'");
/* 6153 */     rowVal[5] = s2b("(M)");
/* 6154 */     rowVal[6] = Integer.toString(1).getBytes();
/*      */     
/*      */ 
/*      */ 
/* 6158 */     rowVal[7] = s2b("false");
/* 6159 */     rowVal[8] = Integer.toString(3).getBytes();
/*      */     
/*      */ 
/*      */ 
/* 6163 */     rowVal[9] = s2b("false");
/* 6164 */     rowVal[10] = s2b("false");
/* 6165 */     rowVal[11] = s2b("false");
/* 6166 */     rowVal[12] = s2b("VARCHAR");
/* 6167 */     rowVal[13] = s2b("0");
/* 6168 */     rowVal[14] = s2b("0");
/* 6169 */     rowVal[15] = s2b("0");
/* 6170 */     rowVal[16] = s2b("0");
/* 6171 */     rowVal[17] = s2b("10");
/* 6172 */     tuples.add(rowVal);
/*      */     
/*      */ 
/*      */ 
/*      */ 
/* 6177 */     rowVal = new byte[18][];
/* 6178 */     rowVal[0] = s2b("ENUM");
/* 6179 */     rowVal[1] = Integer.toString(12).getBytes();
/*      */     
/*      */ 
/* 6182 */     rowVal[2] = s2b("65535");
/* 6183 */     rowVal[3] = s2b("'");
/* 6184 */     rowVal[4] = s2b("'");
/* 6185 */     rowVal[5] = s2b("");
/* 6186 */     rowVal[6] = Integer.toString(1).getBytes();
/*      */     
/*      */ 
/*      */ 
/* 6190 */     rowVal[7] = s2b("false");
/* 6191 */     rowVal[8] = Integer.toString(3).getBytes();
/*      */     
/*      */ 
/*      */ 
/* 6195 */     rowVal[9] = s2b("false");
/* 6196 */     rowVal[10] = s2b("false");
/* 6197 */     rowVal[11] = s2b("false");
/* 6198 */     rowVal[12] = s2b("ENUM");
/* 6199 */     rowVal[13] = s2b("0");
/* 6200 */     rowVal[14] = s2b("0");
/* 6201 */     rowVal[15] = s2b("0");
/* 6202 */     rowVal[16] = s2b("0");
/* 6203 */     rowVal[17] = s2b("10");
/* 6204 */     tuples.add(rowVal);
/*      */     
/*      */ 
/*      */ 
/*      */ 
/* 6209 */     rowVal = new byte[18][];
/* 6210 */     rowVal[0] = s2b("SET");
/* 6211 */     rowVal[1] = Integer.toString(12).getBytes();
/*      */     
/*      */ 
/* 6214 */     rowVal[2] = s2b("64");
/* 6215 */     rowVal[3] = s2b("'");
/* 6216 */     rowVal[4] = s2b("'");
/* 6217 */     rowVal[5] = s2b("");
/* 6218 */     rowVal[6] = Integer.toString(1).getBytes();
/*      */     
/*      */ 
/*      */ 
/* 6222 */     rowVal[7] = s2b("false");
/* 6223 */     rowVal[8] = Integer.toString(3).getBytes();
/*      */     
/*      */ 
/*      */ 
/* 6227 */     rowVal[9] = s2b("false");
/* 6228 */     rowVal[10] = s2b("false");
/* 6229 */     rowVal[11] = s2b("false");
/* 6230 */     rowVal[12] = s2b("SET");
/* 6231 */     rowVal[13] = s2b("0");
/* 6232 */     rowVal[14] = s2b("0");
/* 6233 */     rowVal[15] = s2b("0");
/* 6234 */     rowVal[16] = s2b("0");
/* 6235 */     rowVal[17] = s2b("10");
/* 6236 */     tuples.add(rowVal);
/*      */     
/*      */ 
/*      */ 
/*      */ 
/* 6241 */     rowVal = new byte[18][];
/* 6242 */     rowVal[0] = s2b("DATE");
/* 6243 */     rowVal[1] = Integer.toString(91).getBytes();
/*      */     
/*      */ 
/* 6246 */     rowVal[2] = s2b("0");
/* 6247 */     rowVal[3] = s2b("'");
/* 6248 */     rowVal[4] = s2b("'");
/* 6249 */     rowVal[5] = s2b("");
/* 6250 */     rowVal[6] = Integer.toString(1).getBytes();
/*      */     
/*      */ 
/*      */ 
/* 6254 */     rowVal[7] = s2b("false");
/* 6255 */     rowVal[8] = Integer.toString(3).getBytes();
/*      */     
/*      */ 
/*      */ 
/* 6259 */     rowVal[9] = s2b("false");
/* 6260 */     rowVal[10] = s2b("false");
/* 6261 */     rowVal[11] = s2b("false");
/* 6262 */     rowVal[12] = s2b("DATE");
/* 6263 */     rowVal[13] = s2b("0");
/* 6264 */     rowVal[14] = s2b("0");
/* 6265 */     rowVal[15] = s2b("0");
/* 6266 */     rowVal[16] = s2b("0");
/* 6267 */     rowVal[17] = s2b("10");
/* 6268 */     tuples.add(rowVal);
/*      */     
/*      */ 
/*      */ 
/*      */ 
/* 6273 */     rowVal = new byte[18][];
/* 6274 */     rowVal[0] = s2b("TIME");
/* 6275 */     rowVal[1] = Integer.toString(92).getBytes();
/*      */     
/*      */ 
/* 6278 */     rowVal[2] = s2b("0");
/* 6279 */     rowVal[3] = s2b("'");
/* 6280 */     rowVal[4] = s2b("'");
/* 6281 */     rowVal[5] = s2b("");
/* 6282 */     rowVal[6] = Integer.toString(1).getBytes();
/*      */     
/*      */ 
/*      */ 
/* 6286 */     rowVal[7] = s2b("false");
/* 6287 */     rowVal[8] = Integer.toString(3).getBytes();
/*      */     
/*      */ 
/*      */ 
/* 6291 */     rowVal[9] = s2b("false");
/* 6292 */     rowVal[10] = s2b("false");
/* 6293 */     rowVal[11] = s2b("false");
/* 6294 */     rowVal[12] = s2b("TIME");
/* 6295 */     rowVal[13] = s2b("0");
/* 6296 */     rowVal[14] = s2b("0");
/* 6297 */     rowVal[15] = s2b("0");
/* 6298 */     rowVal[16] = s2b("0");
/* 6299 */     rowVal[17] = s2b("10");
/* 6300 */     tuples.add(rowVal);
/*      */     
/*      */ 
/*      */ 
/*      */ 
/* 6305 */     rowVal = new byte[18][];
/* 6306 */     rowVal[0] = s2b("DATETIME");
/* 6307 */     rowVal[1] = Integer.toString(93).getBytes();
/*      */     
/*      */ 
/* 6310 */     rowVal[2] = s2b("0");
/* 6311 */     rowVal[3] = s2b("'");
/* 6312 */     rowVal[4] = s2b("'");
/* 6313 */     rowVal[5] = s2b("");
/* 6314 */     rowVal[6] = Integer.toString(1).getBytes();
/*      */     
/*      */ 
/*      */ 
/* 6318 */     rowVal[7] = s2b("false");
/* 6319 */     rowVal[8] = Integer.toString(3).getBytes();
/*      */     
/*      */ 
/*      */ 
/* 6323 */     rowVal[9] = s2b("false");
/* 6324 */     rowVal[10] = s2b("false");
/* 6325 */     rowVal[11] = s2b("false");
/* 6326 */     rowVal[12] = s2b("DATETIME");
/* 6327 */     rowVal[13] = s2b("0");
/* 6328 */     rowVal[14] = s2b("0");
/* 6329 */     rowVal[15] = s2b("0");
/* 6330 */     rowVal[16] = s2b("0");
/* 6331 */     rowVal[17] = s2b("10");
/* 6332 */     tuples.add(rowVal);
/*      */     
/*      */ 
/*      */ 
/*      */ 
/* 6337 */     rowVal = new byte[18][];
/* 6338 */     rowVal[0] = s2b("TIMESTAMP");
/* 6339 */     rowVal[1] = Integer.toString(93).getBytes();
/*      */     
/*      */ 
/* 6342 */     rowVal[2] = s2b("0");
/* 6343 */     rowVal[3] = s2b("'");
/* 6344 */     rowVal[4] = s2b("'");
/* 6345 */     rowVal[5] = s2b("[(M)]");
/* 6346 */     rowVal[6] = Integer.toString(1).getBytes();
/*      */     
/*      */ 
/*      */ 
/* 6350 */     rowVal[7] = s2b("false");
/* 6351 */     rowVal[8] = Integer.toString(3).getBytes();
/*      */     
/*      */ 
/*      */ 
/* 6355 */     rowVal[9] = s2b("false");
/* 6356 */     rowVal[10] = s2b("false");
/* 6357 */     rowVal[11] = s2b("false");
/* 6358 */     rowVal[12] = s2b("TIMESTAMP");
/* 6359 */     rowVal[13] = s2b("0");
/* 6360 */     rowVal[14] = s2b("0");
/* 6361 */     rowVal[15] = s2b("0");
/* 6362 */     rowVal[16] = s2b("0");
/* 6363 */     rowVal[17] = s2b("10");
/* 6364 */     tuples.add(rowVal);
/*      */     
/* 6366 */     return buildResultSet(fields, tuples);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public java.sql.ResultSet getUDTs(String catalog, String schemaPattern, String typeNamePattern, int[] types)
/*      */     throws SQLException
/*      */   {
/* 6412 */     Field[] fields = new Field[6];
/* 6413 */     fields[0] = new Field("", "TYPE_CAT", 12, 32);
/* 6414 */     fields[1] = new Field("", "TYPE_SCHEM", 12, 32);
/* 6415 */     fields[2] = new Field("", "TYPE_NAME", 12, 32);
/* 6416 */     fields[3] = new Field("", "CLASS_NAME", 12, 32);
/* 6417 */     fields[4] = new Field("", "DATA_TYPE", 12, 32);
/* 6418 */     fields[5] = new Field("", "REMARKS", 12, 32);
/*      */     
/* 6420 */     ArrayList tuples = new ArrayList();
/*      */     
/* 6422 */     return buildResultSet(fields, tuples);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public String getURL()
/*      */     throws SQLException
/*      */   {
/* 6433 */     return this.conn.getURL();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public String getUserName()
/*      */     throws SQLException
/*      */   {
/* 6444 */     if (this.conn.getUseHostsInPrivileges()) {
/* 6445 */       Statement stmt = null;
/* 6446 */       java.sql.ResultSet rs = null;
/*      */       try
/*      */       {
/* 6449 */         stmt = this.conn.createStatement();
/* 6450 */         stmt.setEscapeProcessing(false);
/*      */         
/* 6452 */         rs = stmt.executeQuery("SELECT USER()");
/* 6453 */         rs.next();
/*      */         
/* 6455 */         return rs.getString(1);
/*      */       } finally {
/* 6457 */         if (rs != null) {
/*      */           try {
/* 6459 */             rs.close();
/*      */           } catch (Exception ex) {
/* 6461 */             AssertionFailedException.shouldNotHappen(ex);
/*      */           }
/*      */           
/* 6464 */           rs = null;
/*      */         }
/*      */         
/* 6467 */         if (stmt != null) {
/*      */           try {
/* 6469 */             stmt.close();
/*      */           } catch (Exception ex) {
/* 6471 */             AssertionFailedException.shouldNotHappen(ex);
/*      */           }
/*      */           
/* 6474 */           stmt = null;
/*      */         }
/*      */       }
/*      */     }
/*      */     
/* 6479 */     return this.conn.getUser();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public java.sql.ResultSet getVersionColumns(String catalog, String schema, String table)
/*      */     throws SQLException
/*      */   {
/* 6518 */     Field[] fields = new Field[8];
/* 6519 */     fields[0] = new Field("", "SCOPE", 5, 5);
/* 6520 */     fields[1] = new Field("", "COLUMN_NAME", 1, 32);
/* 6521 */     fields[2] = new Field("", "DATA_TYPE", 5, 5);
/* 6522 */     fields[3] = new Field("", "TYPE_NAME", 1, 16);
/* 6523 */     fields[4] = new Field("", "COLUMN_SIZE", 1, 16);
/* 6524 */     fields[5] = new Field("", "BUFFER_LENGTH", 1, 16);
/* 6525 */     fields[6] = new Field("", "DECIMAL_DIGITS", 1, 16);
/* 6526 */     fields[7] = new Field("", "PSEUDO_COLUMN", 5, 5);
/*      */     
/* 6528 */     return buildResultSet(fields, new ArrayList());
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public boolean insertsAreDetected(int type)
/*      */     throws SQLException
/*      */   {
/* 6544 */     return false;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public boolean isCatalogAtStart()
/*      */     throws SQLException
/*      */   {
/* 6556 */     return true;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public boolean isReadOnly()
/*      */     throws SQLException
/*      */   {
/* 6567 */     return false;
/*      */   }
/*      */   
/*      */ 
/*      */   public boolean locatorsUpdateCopy()
/*      */     throws SQLException
/*      */   {
/* 6574 */     return !this.conn.getEmulateLocators();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public boolean nullPlusNonNullIsNull()
/*      */     throws SQLException
/*      */   {
/* 6586 */     return true;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public boolean nullsAreSortedAtEnd()
/*      */     throws SQLException
/*      */   {
/* 6597 */     return false;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public boolean nullsAreSortedAtStart()
/*      */     throws SQLException
/*      */   {
/* 6608 */     return (this.conn.versionMeetsMinimum(4, 0, 2)) && (!this.conn.versionMeetsMinimum(4, 0, 11));
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public boolean nullsAreSortedHigh()
/*      */     throws SQLException
/*      */   {
/* 6620 */     return false;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public boolean nullsAreSortedLow()
/*      */     throws SQLException
/*      */   {
/* 6631 */     return !nullsAreSortedHigh();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public boolean othersDeletesAreVisible(int type)
/*      */     throws SQLException
/*      */   {
/* 6644 */     return false;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public boolean othersInsertsAreVisible(int type)
/*      */     throws SQLException
/*      */   {
/* 6657 */     return false;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public boolean othersUpdatesAreVisible(int type)
/*      */     throws SQLException
/*      */   {
/* 6670 */     return false;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public boolean ownDeletesAreVisible(int type)
/*      */     throws SQLException
/*      */   {
/* 6683 */     return false;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public boolean ownInsertsAreVisible(int type)
/*      */     throws SQLException
/*      */   {
/* 6696 */     return false;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public boolean ownUpdatesAreVisible(int type)
/*      */     throws SQLException
/*      */   {
/* 6709 */     return false;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   private LocalAndReferencedColumns parseTableStatusIntoLocalAndReferencedColumns(String keysComment)
/*      */     throws SQLException
/*      */   {
/* 6730 */     String columnsDelimitter = ",";
/*      */     
/* 6732 */     char quoteChar = this.quotedId.length() == 0 ? '\000' : this.quotedId.charAt(0);
/*      */     
/*      */ 
/* 6735 */     int indexOfOpenParenLocalColumns = StringUtils.indexOfIgnoreCaseRespectQuotes(0, keysComment, "(", quoteChar, true);
/*      */     
/*      */ 
/*      */ 
/* 6739 */     if (indexOfOpenParenLocalColumns == -1) {
/* 6740 */       throw SQLError.createSQLException("Error parsing foreign keys definition, couldn't find start of local columns list.", "S1000");
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/* 6746 */     String constraintName = removeQuotedId(keysComment.substring(0, indexOfOpenParenLocalColumns).trim());
/*      */     
/* 6748 */     keysComment = keysComment.substring(indexOfOpenParenLocalColumns, keysComment.length());
/*      */     
/*      */ 
/* 6751 */     String keysCommentTrimmed = keysComment.trim();
/*      */     
/* 6753 */     int indexOfCloseParenLocalColumns = StringUtils.indexOfIgnoreCaseRespectQuotes(0, keysCommentTrimmed, ")", quoteChar, true);
/*      */     
/*      */ 
/*      */ 
/* 6757 */     if (indexOfCloseParenLocalColumns == -1) {
/* 6758 */       throw SQLError.createSQLException("Error parsing foreign keys definition, couldn't find end of local columns list.", "S1000");
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/* 6764 */     String localColumnNamesString = keysCommentTrimmed.substring(1, indexOfCloseParenLocalColumns);
/*      */     
/*      */ 
/* 6767 */     int indexOfRefer = StringUtils.indexOfIgnoreCaseRespectQuotes(0, keysCommentTrimmed, "REFER ", this.quotedId.charAt(0), true);
/*      */     
/*      */ 
/* 6770 */     if (indexOfRefer == -1) {
/* 6771 */       throw SQLError.createSQLException("Error parsing foreign keys definition, couldn't find start of referenced tables list.", "S1000");
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 6778 */     int indexOfOpenParenReferCol = StringUtils.indexOfIgnoreCaseRespectQuotes(indexOfRefer, keysCommentTrimmed, "(", quoteChar, false);
/*      */     
/*      */ 
/*      */ 
/* 6782 */     if (indexOfOpenParenReferCol == -1) {
/* 6783 */       throw SQLError.createSQLException("Error parsing foreign keys definition, couldn't find start of referenced columns list.", "S1000");
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 6790 */     String referCatalogTableString = keysCommentTrimmed.substring(indexOfRefer + "REFER ".length(), indexOfOpenParenReferCol);
/*      */     
/*      */ 
/* 6793 */     int indexOfSlash = StringUtils.indexOfIgnoreCaseRespectQuotes(0, referCatalogTableString, "/", this.quotedId.charAt(0), false);
/*      */     
/*      */ 
/* 6796 */     if (indexOfSlash == -1) {
/* 6797 */       throw SQLError.createSQLException("Error parsing foreign keys definition, couldn't find name of referenced catalog.", "S1000");
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/* 6803 */     String referCatalog = removeQuotedId(referCatalogTableString.substring(0, indexOfSlash));
/*      */     
/* 6805 */     String referTable = removeQuotedId(referCatalogTableString.substring(indexOfSlash + 1).trim());
/*      */     
/*      */ 
/* 6808 */     int indexOfCloseParenRefer = StringUtils.indexOfIgnoreCaseRespectQuotes(indexOfOpenParenReferCol, keysCommentTrimmed, ")", quoteChar, true);
/*      */     
/*      */ 
/*      */ 
/* 6812 */     if (indexOfCloseParenRefer == -1) {
/* 6813 */       throw SQLError.createSQLException("Error parsing foreign keys definition, couldn't find end of referenced columns list.", "S1000");
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/* 6819 */     String referColumnNamesString = keysCommentTrimmed.substring(indexOfOpenParenReferCol + 1, indexOfCloseParenRefer);
/*      */     
/*      */ 
/* 6822 */     List referColumnsList = StringUtils.split(referColumnNamesString, columnsDelimitter, this.quotedId, this.quotedId, false);
/*      */     
/* 6824 */     List localColumnsList = StringUtils.split(localColumnNamesString, columnsDelimitter, this.quotedId, this.quotedId, false);
/*      */     
/*      */ 
/* 6827 */     return new LocalAndReferencedColumns(localColumnsList, referColumnsList, constraintName, referCatalog, referTable);
/*      */   }
/*      */   
/*      */   private String removeQuotedId(String s)
/*      */   {
/* 6832 */     if (s == null) {
/* 6833 */       return null;
/*      */     }
/*      */     
/* 6836 */     if (this.quotedId.equals("")) {
/* 6837 */       return s;
/*      */     }
/*      */     
/* 6840 */     s = s.trim();
/*      */     
/* 6842 */     int frontOffset = 0;
/* 6843 */     int backOffset = s.length();
/* 6844 */     int quoteLength = this.quotedId.length();
/*      */     
/* 6846 */     if (s.startsWith(this.quotedId)) {
/* 6847 */       frontOffset = quoteLength;
/*      */     }
/*      */     
/* 6850 */     if (s.endsWith(this.quotedId)) {
/* 6851 */       backOffset -= quoteLength;
/*      */     }
/*      */     
/* 6854 */     return s.substring(frontOffset, backOffset);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   private byte[] s2b(String s)
/*      */     throws SQLException
/*      */   {
/* 6866 */     return StringUtils.s2b(s, this.conn);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public boolean storesLowerCaseIdentifiers()
/*      */     throws SQLException
/*      */   {
/* 6878 */     return this.conn.lowerCaseTableNames();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public boolean storesLowerCaseQuotedIdentifiers()
/*      */     throws SQLException
/*      */   {
/* 6890 */     return this.conn.lowerCaseTableNames();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public boolean storesMixedCaseIdentifiers()
/*      */     throws SQLException
/*      */   {
/* 6902 */     return !this.conn.lowerCaseTableNames();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public boolean storesMixedCaseQuotedIdentifiers()
/*      */     throws SQLException
/*      */   {
/* 6914 */     return !this.conn.lowerCaseTableNames();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public boolean storesUpperCaseIdentifiers()
/*      */     throws SQLException
/*      */   {
/* 6926 */     return false;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public boolean storesUpperCaseQuotedIdentifiers()
/*      */     throws SQLException
/*      */   {
/* 6938 */     return true;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public boolean supportsAlterTableWithAddColumn()
/*      */     throws SQLException
/*      */   {
/* 6949 */     return true;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public boolean supportsAlterTableWithDropColumn()
/*      */     throws SQLException
/*      */   {
/* 6960 */     return true;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public boolean supportsANSI92EntryLevelSQL()
/*      */     throws SQLException
/*      */   {
/* 6972 */     return true;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public boolean supportsANSI92FullSQL()
/*      */     throws SQLException
/*      */   {
/* 6983 */     return false;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public boolean supportsANSI92IntermediateSQL()
/*      */     throws SQLException
/*      */   {
/* 6994 */     return false;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public boolean supportsBatchUpdates()
/*      */     throws SQLException
/*      */   {
/* 7006 */     return true;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public boolean supportsCatalogsInDataManipulation()
/*      */     throws SQLException
/*      */   {
/* 7018 */     return this.conn.versionMeetsMinimum(3, 22, 0);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public boolean supportsCatalogsInIndexDefinitions()
/*      */     throws SQLException
/*      */   {
/* 7030 */     return this.conn.versionMeetsMinimum(3, 22, 0);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public boolean supportsCatalogsInPrivilegeDefinitions()
/*      */     throws SQLException
/*      */   {
/* 7042 */     return this.conn.versionMeetsMinimum(3, 22, 0);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public boolean supportsCatalogsInProcedureCalls()
/*      */     throws SQLException
/*      */   {
/* 7054 */     return this.conn.versionMeetsMinimum(3, 22, 0);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public boolean supportsCatalogsInTableDefinitions()
/*      */     throws SQLException
/*      */   {
/* 7066 */     return this.conn.versionMeetsMinimum(3, 22, 0);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public boolean supportsColumnAliasing()
/*      */     throws SQLException
/*      */   {
/* 7082 */     return true;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public boolean supportsConvert()
/*      */     throws SQLException
/*      */   {
/* 7093 */     return false;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public boolean supportsConvert(int fromType, int toType)
/*      */     throws SQLException
/*      */   {
/* 7110 */     switch (fromType)
/*      */     {
/*      */ 
/*      */ 
/*      */ 
/*      */     case -4: 
/*      */     case -3: 
/*      */     case -2: 
/*      */     case -1: 
/*      */     case 1: 
/*      */     case 12: 
/* 7121 */       switch (toType) {
/*      */       case -6: 
/*      */       case -5: 
/*      */       case -4: 
/*      */       case -3: 
/*      */       case -2: 
/*      */       case -1: 
/*      */       case 1: 
/*      */       case 2: 
/*      */       case 3: 
/*      */       case 4: 
/*      */       case 5: 
/*      */       case 6: 
/*      */       case 7: 
/*      */       case 8: 
/*      */       case 12: 
/*      */       case 91: 
/*      */       case 92: 
/*      */       case 93: 
/*      */       case 1111: 
/* 7141 */         return true;
/*      */       }
/*      */       
/* 7144 */       return false;
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     case -7: 
/* 7151 */       return false;
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     case -6: 
/*      */     case -5: 
/*      */     case 2: 
/*      */     case 3: 
/*      */     case 4: 
/*      */     case 5: 
/*      */     case 6: 
/*      */     case 7: 
/*      */     case 8: 
/* 7167 */       switch (toType) {
/*      */       case -6: 
/*      */       case -5: 
/*      */       case -4: 
/*      */       case -3: 
/*      */       case -2: 
/*      */       case -1: 
/*      */       case 1: 
/*      */       case 2: 
/*      */       case 3: 
/*      */       case 4: 
/*      */       case 5: 
/*      */       case 6: 
/*      */       case 7: 
/*      */       case 8: 
/*      */       case 12: 
/* 7183 */         return true;
/*      */       }
/*      */       
/* 7186 */       return false;
/*      */     
/*      */ 
/*      */ 
/*      */     case 0: 
/* 7191 */       return false;
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     case 1111: 
/* 7199 */       switch (toType) {
/*      */       case -4: 
/*      */       case -3: 
/*      */       case -2: 
/*      */       case -1: 
/*      */       case 1: 
/*      */       case 12: 
/* 7206 */         return true;
/*      */       }
/*      */       
/* 7209 */       return false;
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */     case 91: 
/* 7215 */       switch (toType) {
/*      */       case -4: 
/*      */       case -3: 
/*      */       case -2: 
/*      */       case -1: 
/*      */       case 1: 
/*      */       case 12: 
/* 7222 */         return true;
/*      */       }
/*      */       
/* 7225 */       return false;
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */     case 92: 
/* 7231 */       switch (toType) {
/*      */       case -4: 
/*      */       case -3: 
/*      */       case -2: 
/*      */       case -1: 
/*      */       case 1: 
/*      */       case 12: 
/* 7238 */         return true;
/*      */       }
/*      */       
/* 7241 */       return false;
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     case 93: 
/* 7250 */       switch (toType) {
/*      */       case -4: 
/*      */       case -3: 
/*      */       case -2: 
/*      */       case -1: 
/*      */       case 1: 
/*      */       case 12: 
/*      */       case 91: 
/*      */       case 92: 
/* 7259 */         return true;
/*      */       }
/*      */       
/* 7262 */       return false;
/*      */     }
/*      */     
/*      */     
/*      */ 
/* 7267 */     return false;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public boolean supportsCoreSQLGrammar()
/*      */     throws SQLException
/*      */   {
/* 7279 */     return true;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public boolean supportsCorrelatedSubqueries()
/*      */     throws SQLException
/*      */   {
/* 7291 */     return this.conn.versionMeetsMinimum(4, 1, 0);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public boolean supportsDataDefinitionAndDataManipulationTransactions()
/*      */     throws SQLException
/*      */   {
/* 7304 */     return false;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public boolean supportsDataManipulationTransactionsOnly()
/*      */     throws SQLException
/*      */   {
/* 7316 */     return false;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public boolean supportsDifferentTableCorrelationNames()
/*      */     throws SQLException
/*      */   {
/* 7329 */     return true;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public boolean supportsExpressionsInOrderBy()
/*      */     throws SQLException
/*      */   {
/* 7340 */     return true;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public boolean supportsExtendedSQLGrammar()
/*      */     throws SQLException
/*      */   {
/* 7351 */     return false;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public boolean supportsFullOuterJoins()
/*      */     throws SQLException
/*      */   {
/* 7362 */     return false;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public boolean supportsGetGeneratedKeys()
/*      */   {
/* 7371 */     return true;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public boolean supportsGroupBy()
/*      */     throws SQLException
/*      */   {
/* 7382 */     return true;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public boolean supportsGroupByBeyondSelect()
/*      */     throws SQLException
/*      */   {
/* 7394 */     return true;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public boolean supportsGroupByUnrelated()
/*      */     throws SQLException
/*      */   {
/* 7405 */     return true;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public boolean supportsIntegrityEnhancementFacility()
/*      */     throws SQLException
/*      */   {
/* 7416 */     if (!this.conn.getOverrideSupportsIntegrityEnhancementFacility()) {
/* 7417 */       return false;
/*      */     }
/*      */     
/* 7420 */     return true;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public boolean supportsLikeEscapeClause()
/*      */     throws SQLException
/*      */   {
/* 7432 */     return true;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public boolean supportsLimitedOuterJoins()
/*      */     throws SQLException
/*      */   {
/* 7444 */     return true;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public boolean supportsMinimumSQLGrammar()
/*      */     throws SQLException
/*      */   {
/* 7456 */     return true;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public boolean supportsMixedCaseIdentifiers()
/*      */     throws SQLException
/*      */   {
/* 7467 */     return !this.conn.lowerCaseTableNames();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public boolean supportsMixedCaseQuotedIdentifiers()
/*      */     throws SQLException
/*      */   {
/* 7479 */     return !this.conn.lowerCaseTableNames();
/*      */   }
/*      */   
/*      */ 
/*      */   public boolean supportsMultipleOpenResults()
/*      */     throws SQLException
/*      */   {
/* 7486 */     return true;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public boolean supportsMultipleResultSets()
/*      */     throws SQLException
/*      */   {
/* 7497 */     return false;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public boolean supportsMultipleTransactions()
/*      */     throws SQLException
/*      */   {
/* 7509 */     return true;
/*      */   }
/*      */   
/*      */ 
/*      */   public boolean supportsNamedParameters()
/*      */     throws SQLException
/*      */   {
/* 7516 */     return false;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public boolean supportsNonNullableColumns()
/*      */     throws SQLException
/*      */   {
/* 7528 */     return true;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public boolean supportsOpenCursorsAcrossCommit()
/*      */     throws SQLException
/*      */   {
/* 7540 */     return false;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public boolean supportsOpenCursorsAcrossRollback()
/*      */     throws SQLException
/*      */   {
/* 7552 */     return false;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public boolean supportsOpenStatementsAcrossCommit()
/*      */     throws SQLException
/*      */   {
/* 7564 */     return false;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public boolean supportsOpenStatementsAcrossRollback()
/*      */     throws SQLException
/*      */   {
/* 7576 */     return false;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public boolean supportsOrderByUnrelated()
/*      */     throws SQLException
/*      */   {
/* 7587 */     return false;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public boolean supportsOuterJoins()
/*      */     throws SQLException
/*      */   {
/* 7598 */     return true;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public boolean supportsPositionedDelete()
/*      */     throws SQLException
/*      */   {
/* 7609 */     return false;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public boolean supportsPositionedUpdate()
/*      */     throws SQLException
/*      */   {
/* 7620 */     return false;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public boolean supportsResultSetConcurrency(int type, int concurrency)
/*      */     throws SQLException
/*      */   {
/* 7638 */     switch (type) {
/*      */     case 1004: 
/* 7640 */       if ((concurrency == 1007) || (concurrency == 1008))
/*      */       {
/* 7642 */         return true;
/*      */       }
/* 7644 */       throw SQLError.createSQLException("Illegal arguments to supportsResultSetConcurrency()", "S1009");
/*      */     
/*      */ 
/*      */ 
/*      */     case 1003: 
/* 7649 */       if ((concurrency == 1007) || (concurrency == 1008))
/*      */       {
/* 7651 */         return true;
/*      */       }
/* 7653 */       throw SQLError.createSQLException("Illegal arguments to supportsResultSetConcurrency()", "S1009");
/*      */     
/*      */ 
/*      */ 
/*      */     case 1005: 
/* 7658 */       return false;
/*      */     }
/* 7660 */     throw SQLError.createSQLException("Illegal arguments to supportsResultSetConcurrency()", "S1009");
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public boolean supportsResultSetHoldability(int holdability)
/*      */     throws SQLException
/*      */   {
/* 7672 */     return holdability == 1;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public boolean supportsResultSetType(int type)
/*      */     throws SQLException
/*      */   {
/* 7686 */     return type == 1004;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public boolean supportsSavepoints()
/*      */     throws SQLException
/*      */   {
/* 7694 */     return (this.conn.versionMeetsMinimum(4, 0, 14)) || (this.conn.versionMeetsMinimum(4, 1, 1));
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public boolean supportsSchemasInDataManipulation()
/*      */     throws SQLException
/*      */   {
/* 7706 */     return false;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public boolean supportsSchemasInIndexDefinitions()
/*      */     throws SQLException
/*      */   {
/* 7717 */     return false;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public boolean supportsSchemasInPrivilegeDefinitions()
/*      */     throws SQLException
/*      */   {
/* 7728 */     return false;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public boolean supportsSchemasInProcedureCalls()
/*      */     throws SQLException
/*      */   {
/* 7739 */     return false;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public boolean supportsSchemasInTableDefinitions()
/*      */     throws SQLException
/*      */   {
/* 7750 */     return false;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public boolean supportsSelectForUpdate()
/*      */     throws SQLException
/*      */   {
/* 7761 */     return this.conn.versionMeetsMinimum(4, 0, 0);
/*      */   }
/*      */   
/*      */ 
/*      */   public boolean supportsStatementPooling()
/*      */     throws SQLException
/*      */   {
/* 7768 */     return false;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public boolean supportsStoredProcedures()
/*      */     throws SQLException
/*      */   {
/* 7780 */     return this.conn.versionMeetsMinimum(5, 0, 0);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public boolean supportsSubqueriesInComparisons()
/*      */     throws SQLException
/*      */   {
/* 7792 */     return this.conn.versionMeetsMinimum(4, 1, 0);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public boolean supportsSubqueriesInExists()
/*      */     throws SQLException
/*      */   {
/* 7804 */     return this.conn.versionMeetsMinimum(4, 1, 0);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public boolean supportsSubqueriesInIns()
/*      */     throws SQLException
/*      */   {
/* 7816 */     return this.conn.versionMeetsMinimum(4, 1, 0);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public boolean supportsSubqueriesInQuantifieds()
/*      */     throws SQLException
/*      */   {
/* 7828 */     return this.conn.versionMeetsMinimum(4, 1, 0);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public boolean supportsTableCorrelationNames()
/*      */     throws SQLException
/*      */   {
/* 7840 */     return true;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public boolean supportsTransactionIsolationLevel(int level)
/*      */     throws SQLException
/*      */   {
/* 7855 */     if (this.conn.supportsIsolationLevel()) {
/* 7856 */       switch (level) {
/*      */       case 1: 
/*      */       case 2: 
/*      */       case 4: 
/*      */       case 8: 
/* 7861 */         return true;
/*      */       }
/*      */       
/* 7864 */       return false;
/*      */     }
/*      */     
/*      */ 
/* 7868 */     return false;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public boolean supportsTransactions()
/*      */     throws SQLException
/*      */   {
/* 7880 */     return this.conn.supportsTransactions();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public boolean supportsUnion()
/*      */     throws SQLException
/*      */   {
/* 7891 */     return this.conn.versionMeetsMinimum(4, 0, 0);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public boolean supportsUnionAll()
/*      */     throws SQLException
/*      */   {
/* 7902 */     return this.conn.versionMeetsMinimum(4, 0, 0);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public boolean updatesAreDetected(int type)
/*      */     throws SQLException
/*      */   {
/* 7916 */     return false;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public boolean usesLocalFilePerTable()
/*      */     throws SQLException
/*      */   {
/* 7927 */     return false;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public boolean usesLocalFiles()
/*      */     throws SQLException
/*      */   {
/* 7938 */     return false;
/*      */   }
/*      */ }


/* Location:              E:\java\java学习\hutubill\lib\all.jar!\com\mysql\jdbc\DatabaseMetaData.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version:       0.7.1
 */
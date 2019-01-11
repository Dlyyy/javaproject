/*      */ package com.mysql.jdbc;
/*      */ 
/*      */ import java.sql.SQLException;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ public class DatabaseMetaDataUsingInfoSchema
/*      */   extends DatabaseMetaData
/*      */ {
/*      */   public DatabaseMetaDataUsingInfoSchema(Connection connToSet, String databaseToSet)
/*      */   {
/*   40 */     super(connToSet, databaseToSet);
/*      */   }
/*      */   
/*      */   private java.sql.ResultSet executeMetadataQuery(PreparedStatement pStmt) throws SQLException
/*      */   {
/*   45 */     java.sql.ResultSet rs = pStmt.executeQuery();
/*   46 */     ((ResultSet)rs).setOwningStatement(null);
/*      */     
/*   48 */     return rs;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
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
/*   89 */     if (columnNamePattern == null) {
/*   90 */       if (this.conn.getNullNamePatternMatchesAll()) {
/*   91 */         columnNamePattern = "%";
/*      */       } else {
/*   93 */         throw SQLError.createSQLException("Column name pattern can not be NULL or empty.", "S1009");
/*      */       }
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*   99 */     if ((catalog == null) && 
/*  100 */       (this.conn.getNullCatalogMeansCurrent())) {
/*  101 */       catalog = this.database;
/*      */     }
/*      */     
/*      */ 
/*  105 */     String sql = "SELECT TABLE_SCHEMA AS TABLE_CAT, NULL AS TABLE_SCHEM, TABLE_NAME,COLUMN_NAME, NULL AS GRANTOR, GRANTEE, PRIVILEGE_TYPE AS PRIVILEGE, IS_GRANTABLE FROM INFORMATION_SCHEMA.COLUMN_PRIVILEGES WHERE TABLE_SCHEMA LIKE ? AND TABLE_NAME =? AND COLUMN_NAME LIKE ? ORDER BY COLUMN_NAME, PRIVILEGE_TYPE";
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  112 */     PreparedStatement pStmt = null;
/*      */     try
/*      */     {
/*  115 */       pStmt = prepareMetaDataSafeStatement(sql);
/*      */       
/*  117 */       if (catalog != null) {
/*  118 */         pStmt.setString(1, catalog);
/*      */       } else {
/*  120 */         pStmt.setString(1, "%");
/*      */       }
/*      */       
/*  123 */       pStmt.setString(2, table);
/*  124 */       pStmt.setString(3, columnNamePattern);
/*      */       
/*  126 */       java.sql.ResultSet rs = executeMetadataQuery(pStmt);
/*  127 */       ((ResultSet)rs).redefineFieldsForDBMD(new Field[] { new Field("", "TABLE_CAT", 1, 64), new Field("", "TABLE_SCHEM", 1, 1), new Field("", "TABLE_NAME", 1, 64), new Field("", "COLUMN_NAME", 1, 64), new Field("", "GRANTOR", 1, 77), new Field("", "GRANTEE", 1, 77), new Field("", "PRIVILEGE", 1, 64), new Field("", "IS_GRANTABLE", 1, 3) });
/*      */       
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  137 */       return rs;
/*      */     } finally {
/*  139 */       if (pStmt != null) {
/*  140 */         pStmt.close();
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
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public java.sql.ResultSet getColumns(String catalog, String schemaPattern, String tableName, String columnNamePattern)
/*      */     throws SQLException
/*      */   {
/*  191 */     if (columnNamePattern == null) {
/*  192 */       if (this.conn.getNullNamePatternMatchesAll()) {
/*  193 */         columnNamePattern = "%";
/*      */       } else {
/*  195 */         throw SQLError.createSQLException("Column name pattern can not be NULL or empty.", "S1009");
/*      */       }
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*  201 */     if ((catalog == null) && 
/*  202 */       (this.conn.getNullCatalogMeansCurrent())) {
/*  203 */       catalog = this.database;
/*      */     }
/*      */     
/*      */ 
/*  207 */     StringBuffer sqlBuf = new StringBuffer("SELECT TABLE_SCHEMA AS TABLE_CAT, NULL AS TABLE_SCHEM,TABLE_NAME,COLUMN_NAME,");
/*      */     
/*      */ 
/*  210 */     MysqlDefs.appendJdbcTypeMappingQuery(sqlBuf, "DATA_TYPE");
/*      */     
/*  212 */     sqlBuf.append(" AS DATA_TYPE, ");
/*      */     
/*  214 */     if (this.conn.getCapitalizeTypeNames()) {
/*  215 */       sqlBuf.append("UPPER(CASE WHEN LOCATE('unsigned', COLUMN_TYPE) != 0 AND LOCATE('unsigned', DATA_TYPE) = 0 THEN CONCAT(DATA_TYPE, ' unsigned') ELSE DATA_TYPE END) AS TYPE_NAME,");
/*      */     } else {
/*  217 */       sqlBuf.append("CASE WHEN LOCATE('unsigned', COLUMN_TYPE) != 0 AND LOCATE('unsigned', DATA_TYPE) = 0 THEN CONCAT(DATA_TYPE, ' unsigned') ELSE DATA_TYPE END AS TYPE_NAME,");
/*      */     }
/*      */     
/*  220 */     sqlBuf.append("CASE WHEN CHARACTER_MAXIMUM_LENGTH IS NULL THEN NUMERIC_PRECISION ELSE CASE WHEN CHARACTER_MAXIMUM_LENGTH > 2147483647 THEN 2147483647 ELSE CHARACTER_MAXIMUM_LENGTH END END AS COLUMN_SIZE, " + MysqlIO.getMaxBuf() + " AS BUFFER_LENGTH," + "NUMERIC_SCALE AS DECIMAL_DIGITS," + "10 AS NUM_PREC_RADIX," + "CASE WHEN IS_NULLABLE='NO' THEN " + 0 + " ELSE CASE WHEN IS_NULLABLE='YES' THEN " + 1 + " ELSE " + 2 + " END END AS NULLABLE," + "COLUMN_COMMENT AS REMARKS," + "COLUMN_DEFAULT AS COLUMN_DEF," + "0 AS SQL_DATA_TYPE," + "0 AS SQL_DATETIME_SUB," + "CASE WHEN CHARACTER_OCTET_LENGTH > " + Integer.MAX_VALUE + " THEN " + Integer.MAX_VALUE + " ELSE CHARACTER_OCTET_LENGTH END AS CHAR_OCTET_LENGTH," + "ORDINAL_POSITION," + "IS_NULLABLE," + "NULL AS SCOPE_CATALOG," + "NULL AS SCOPE_SCHEMA," + "NULL AS SCOPE_TABLE," + "NULL AS SOURCE_DATA_TYPE," + "IF (EXTRA LIKE '%auto_increment%','YES','NO') AS IS_AUTOINCREMENT " + "FROM INFORMATION_SCHEMA.COLUMNS WHERE " + "TABLE_SCHEMA LIKE ? AND " + "TABLE_NAME LIKE ? AND COLUMN_NAME LIKE ? " + "ORDER BY TABLE_SCHEMA, TABLE_NAME, ORDINAL_POSITION");
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  245 */     PreparedStatement pStmt = null;
/*      */     try
/*      */     {
/*  248 */       pStmt = prepareMetaDataSafeStatement(sqlBuf.toString());
/*      */       
/*  250 */       if (catalog != null) {
/*  251 */         pStmt.setString(1, catalog);
/*      */       } else {
/*  253 */         pStmt.setString(1, "%");
/*      */       }
/*      */       
/*  256 */       pStmt.setString(2, tableName);
/*  257 */       pStmt.setString(3, columnNamePattern);
/*      */       
/*  259 */       java.sql.ResultSet rs = executeMetadataQuery(pStmt);
/*      */       
/*  261 */       ((ResultSet)rs).redefineFieldsForDBMD(new Field[] { new Field("", "TABLE_CAT", 1, 255), new Field("", "TABLE_SCHEM", 1, 0), new Field("", "TABLE_NAME", 1, 255), new Field("", "COLUMN_NAME", 1, 32), new Field("", "DATA_TYPE", 5, 5), new Field("", "TYPE_NAME", 1, 16), new Field("", "COLUMN_SIZE", 4, Integer.toString(Integer.MAX_VALUE).length()), new Field("", "BUFFER_LENGTH", 4, 10), new Field("", "DECIMAL_DIGITS", 4, 10), new Field("", "NUM_PREC_RADIX", 4, 10), new Field("", "NULLABLE", 4, 10), new Field("", "REMARKS", 1, 0), new Field("", "COLUMN_DEF", 1, 0), new Field("", "SQL_DATA_TYPE", 4, 10), new Field("", "SQL_DATETIME_SUB", 4, 10), new Field("", "CHAR_OCTET_LENGTH", 4, Integer.toString(Integer.MAX_VALUE).length()), new Field("", "ORDINAL_POSITION", 4, 10), new Field("", "IS_NULLABLE", 1, 3), new Field("", "SCOPE_CATALOG", 1, 255), new Field("", "SCOPE_SCHEMA", 1, 255), new Field("", "SCOPE_TABLE", 1, 255), new Field("", "SOURCE_DATA_TYPE", 5, 10), new Field("", "IS_AUTOINCREMENT", 1, 3) });
/*      */       
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  287 */       return rs;
/*      */     } finally {
/*  289 */       if (pStmt != null) {
/*  290 */         pStmt.close();
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
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
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
/*  365 */     if (primaryTable == null) {
/*  366 */       throw SQLError.createSQLException("Table not specified.", "S1009");
/*      */     }
/*      */     
/*      */ 
/*  370 */     if ((primaryCatalog == null) && 
/*  371 */       (this.conn.getNullCatalogMeansCurrent())) {
/*  372 */       primaryCatalog = this.database;
/*      */     }
/*      */     
/*      */ 
/*  376 */     if ((foreignCatalog == null) && 
/*  377 */       (this.conn.getNullCatalogMeansCurrent())) {
/*  378 */       foreignCatalog = this.database;
/*      */     }
/*      */     
/*      */ 
/*  382 */     Field[] fields = new Field[14];
/*  383 */     fields[0] = new Field("", "PKTABLE_CAT", 1, 255);
/*  384 */     fields[1] = new Field("", "PKTABLE_SCHEM", 1, 0);
/*  385 */     fields[2] = new Field("", "PKTABLE_NAME", 1, 255);
/*  386 */     fields[3] = new Field("", "PKCOLUMN_NAME", 1, 32);
/*  387 */     fields[4] = new Field("", "FKTABLE_CAT", 1, 255);
/*  388 */     fields[5] = new Field("", "FKTABLE_SCHEM", 1, 0);
/*  389 */     fields[6] = new Field("", "FKTABLE_NAME", 1, 255);
/*  390 */     fields[7] = new Field("", "FKCOLUMN_NAME", 1, 32);
/*  391 */     fields[8] = new Field("", "KEY_SEQ", 5, 2);
/*  392 */     fields[9] = new Field("", "UPDATE_RULE", 5, 2);
/*  393 */     fields[10] = new Field("", "DELETE_RULE", 5, 2);
/*  394 */     fields[11] = new Field("", "FK_NAME", 1, 0);
/*  395 */     fields[12] = new Field("", "PK_NAME", 1, 0);
/*  396 */     fields[13] = new Field("", "DEFERRABILITY", 4, 2);
/*      */     
/*  398 */     String sql = "SELECT A.REFERENCED_TABLE_SCHEMA AS PKTABLE_CAT,NULL AS PKTABLE_SCHEM,A.REFERENCED_TABLE_NAME AS PKTABLE_NAME,A.REFERENCED_COLUMN_NAME AS PKCOLUMN_NAME,A.TABLE_SCHEMA AS FKTABLE_CAT,NULL AS FKTABLE_SCHEM,A.TABLE_NAME AS FKTABLE_NAME, A.COLUMN_NAME AS FKCOLUMN_NAME, A.ORDINAL_POSITION AS KEY_SEQ,1 AS UPDATE_RULE,1 AS DELETE_RULE,A.CONSTRAINT_NAME AS FK_NAME,NULL AS PK_NAME,7 AS DEFERRABILITY FROM INFORMATION_SCHEMA.KEY_COLUMN_USAGE A,INFORMATION_SCHEMA.TABLE_CONSTRAINTS B WHERE A.TABLE_SCHEMA=B.TABLE_SCHEMA AND A.TABLE_NAME=B.TABLE_NAME AND A.CONSTRAINT_NAME=B.CONSTRAINT_NAME AND B.CONSTRAINT_TYPE IS NOT NULL AND A.REFERENCED_TABLE_SCHEMA LIKE ? AND A.REFERENCED_TABLE_NAME=? AND A.TABLE_SCHEMA LIKE ? AND A.TABLE_NAME=? ORDER BY A.TABLE_SCHEMA, A.TABLE_NAME, A.ORDINAL_POSITION";
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  427 */     PreparedStatement pStmt = null;
/*      */     try
/*      */     {
/*  430 */       pStmt = prepareMetaDataSafeStatement(sql);
/*  431 */       if (primaryCatalog != null) {
/*  432 */         pStmt.setString(1, primaryCatalog);
/*      */       } else {
/*  434 */         pStmt.setString(1, "%");
/*      */       }
/*      */       
/*  437 */       pStmt.setString(2, primaryTable);
/*      */       
/*  439 */       if (foreignCatalog != null) {
/*  440 */         pStmt.setString(3, foreignCatalog);
/*      */       } else {
/*  442 */         pStmt.setString(3, "%");
/*      */       }
/*      */       
/*  445 */       pStmt.setString(4, foreignTable);
/*      */       
/*  447 */       java.sql.ResultSet rs = executeMetadataQuery(pStmt);
/*  448 */       ((ResultSet)rs).redefineFieldsForDBMD(new Field[] { new Field("", "PKTABLE_CAT", 1, 255), new Field("", "PKTABLE_SCHEM", 1, 0), new Field("", "PKTABLE_NAME", 1, 255), new Field("", "PKCOLUMN_NAME", 1, 32), new Field("", "FKTABLE_CAT", 1, 255), new Field("", "FKTABLE_SCHEM", 1, 0), new Field("", "FKTABLE_NAME", 1, 255), new Field("", "FKCOLUMN_NAME", 1, 32), new Field("", "KEY_SEQ", 5, 2), new Field("", "UPDATE_RULE", 5, 2), new Field("", "DELETE_RULE", 5, 2), new Field("", "FK_NAME", 1, 0), new Field("", "PK_NAME", 1, 0), new Field("", "DEFERRABILITY", 4, 2) });
/*      */       
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  464 */       return rs;
/*      */     } finally {
/*  466 */       if (pStmt != null) {
/*  467 */         pStmt.close();
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
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
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
/*  535 */     if (table == null) {
/*  536 */       throw SQLError.createSQLException("Table not specified.", "S1009");
/*      */     }
/*      */     
/*      */ 
/*  540 */     if ((catalog == null) && 
/*  541 */       (this.conn.getNullCatalogMeansCurrent())) {
/*  542 */       catalog = this.database;
/*      */     }
/*      */     
/*      */ 
/*  546 */     String sql = "SELECT A.REFERENCED_TABLE_SCHEMA AS PKTABLE_CAT,NULL AS PKTABLE_SCHEM,A.REFERENCED_TABLE_NAME AS PKTABLE_NAME, A.REFERENCED_COLUMN_NAME AS PKCOLUMN_NAME, A.TABLE_SCHEMA AS FKTABLE_CAT,NULL AS FKTABLE_SCHEM,A.TABLE_NAME AS FKTABLE_NAME,A.COLUMN_NAME AS FKCOLUMN_NAME, A.ORDINAL_POSITION AS KEY_SEQ,1 AS UPDATE_RULE,1 AS DELETE_RULE,A.CONSTRAINT_NAME AS FK_NAME,NULL AS PK_NAME,7 AS DEFERRABILITY FROM INFORMATION_SCHEMA.KEY_COLUMN_USAGE A,INFORMATION_SCHEMA.TABLE_CONSTRAINTS B WHERE A.TABLE_SCHEMA=B.TABLE_SCHEMA AND A.TABLE_NAME=B.TABLE_NAME AND A.CONSTRAINT_NAME=B.CONSTRAINT_NAME AND B.CONSTRAINT_TYPE IS NOT NULL AND A.REFERENCED_TABLE_SCHEMA LIKE ? AND A.REFERENCED_TABLE_NAME=? ORDER BY A.TABLE_SCHEMA, A.TABLE_NAME, A.ORDINAL_POSITION";
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  574 */     PreparedStatement pStmt = null;
/*      */     try
/*      */     {
/*  577 */       pStmt = prepareMetaDataSafeStatement(sql);
/*      */       
/*  579 */       if (catalog != null) {
/*  580 */         pStmt.setString(1, catalog);
/*      */       } else {
/*  582 */         pStmt.setString(1, "%");
/*      */       }
/*      */       
/*  585 */       pStmt.setString(2, table);
/*      */       
/*  587 */       java.sql.ResultSet rs = executeMetadataQuery(pStmt);
/*      */       
/*  589 */       ((ResultSet)rs).redefineFieldsForDBMD(new Field[] { new Field("", "PKTABLE_CAT", 1, 255), new Field("", "PKTABLE_SCHEM", 1, 0), new Field("", "PKTABLE_NAME", 1, 255), new Field("", "PKCOLUMN_NAME", 1, 32), new Field("", "FKTABLE_CAT", 1, 255), new Field("", "FKTABLE_SCHEM", 1, 0), new Field("", "FKTABLE_NAME", 1, 255), new Field("", "FKCOLUMN_NAME", 1, 32), new Field("", "KEY_SEQ", 5, 2), new Field("", "UPDATE_RULE", 5, 2), new Field("", "DELETE_RULE", 5, 2), new Field("", "FK_NAME", 1, 255), new Field("", "PK_NAME", 1, 0), new Field("", "DEFERRABILITY", 4, 2) });
/*      */       
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  605 */       return rs;
/*      */     } finally {
/*  607 */       if (pStmt != null) {
/*  608 */         pStmt.close();
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
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
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
/*  701 */     if (table == null) {
/*  702 */       throw SQLError.createSQLException("Table not specified.", "S1009");
/*      */     }
/*      */     
/*      */ 
/*  706 */     if ((catalog == null) && 
/*  707 */       (this.conn.getNullCatalogMeansCurrent())) {
/*  708 */       catalog = this.database;
/*      */     }
/*      */     
/*      */ 
/*  712 */     String sql = "SELECT A.REFERENCED_TABLE_SCHEMA AS PKTABLE_CAT,NULL AS PKTABLE_SCHEM,A.REFERENCED_TABLE_NAME AS PKTABLE_NAME,A.REFERENCED_COLUMN_NAME AS PKCOLUMN_NAME,A.TABLE_SCHEMA AS FKTABLE_CAT,NULL AS FKTABLE_SCHEM,A.TABLE_NAME AS FKTABLE_NAME, A.COLUMN_NAME AS FKCOLUMN_NAME, A.ORDINAL_POSITION AS KEY_SEQ,1 AS UPDATE_RULE,1 AS DELETE_RULE,A.CONSTRAINT_NAME AS FK_NAME,NULL AS PK_NAME, 7 AS DEFERRABILITY FROM INFORMATION_SCHEMA.KEY_COLUMN_USAGE A, INFORMATION_SCHEMA.TABLE_CONSTRAINTS B WHERE A.TABLE_SCHEMA LIKE ? AND A.CONSTRAINT_NAME=B.CONSTRAINT_NAME AND A.TABLE_NAME=? AND B.TABLE_NAME=? AND A.REFERENCED_TABLE_SCHEMA IS NOT NULL  ORDER BY A.REFERENCED_TABLE_SCHEMA, A.REFERENCED_TABLE_NAME, A.ORDINAL_POSITION";
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  740 */     PreparedStatement pStmt = null;
/*      */     try
/*      */     {
/*  743 */       pStmt = prepareMetaDataSafeStatement(sql);
/*      */       
/*  745 */       if (catalog != null) {
/*  746 */         pStmt.setString(1, catalog);
/*      */       } else {
/*  748 */         pStmt.setString(1, "%");
/*      */       }
/*      */       
/*  751 */       pStmt.setString(2, table);
/*  752 */       pStmt.setString(3, table);
/*      */       
/*  754 */       java.sql.ResultSet rs = executeMetadataQuery(pStmt);
/*      */       
/*  756 */       ((ResultSet)rs).redefineFieldsForDBMD(new Field[] { new Field("", "PKTABLE_CAT", 1, 255), new Field("", "PKTABLE_SCHEM", 1, 0), new Field("", "PKTABLE_NAME", 1, 255), new Field("", "PKCOLUMN_NAME", 1, 32), new Field("", "FKTABLE_CAT", 1, 255), new Field("", "FKTABLE_SCHEM", 1, 0), new Field("", "FKTABLE_NAME", 1, 255), new Field("", "FKCOLUMN_NAME", 1, 32), new Field("", "KEY_SEQ", 5, 2), new Field("", "UPDATE_RULE", 5, 2), new Field("", "DELETE_RULE", 5, 2), new Field("", "FK_NAME", 1, 255), new Field("", "PK_NAME", 1, 0), new Field("", "DEFERRABILITY", 4, 2) });
/*      */       
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  772 */       return rs;
/*      */     } finally {
/*  774 */       if (pStmt != null) {
/*  775 */         pStmt.close();
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
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
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
/*  840 */     StringBuffer sqlBuf = new StringBuffer("SELECT TABLE_SCHEMA AS TABLE_CAT, NULL AS TABLE_SCHEM,TABLE_NAME,NON_UNIQUE,TABLE_SCHEMA AS INDEX_QUALIFIER,INDEX_NAME,3 AS TYPE,SEQ_IN_INDEX AS ORDINAL_POSITION,COLUMN_NAME,COLLATION AS ASC_OR_DESC,CARDINALITY,NULL AS PAGES,NULL AS FILTER_CONDITION FROM INFORMATION_SCHEMA.STATISTICS WHERE TABLE_SCHEMA LIKE ? AND TABLE_NAME LIKE ?");
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  851 */     if (unique) {
/*  852 */       sqlBuf.append(" AND NON_UNIQUE=0 ");
/*      */     }
/*      */     
/*  855 */     sqlBuf.append("ORDER BY NON_UNIQUE, INDEX_NAME, SEQ_IN_INDEX");
/*      */     
/*  857 */     PreparedStatement pStmt = null;
/*      */     try
/*      */     {
/*  860 */       if ((catalog == null) && 
/*  861 */         (this.conn.getNullCatalogMeansCurrent())) {
/*  862 */         catalog = this.database;
/*      */       }
/*      */       
/*      */ 
/*  866 */       pStmt = prepareMetaDataSafeStatement(sqlBuf.toString());
/*      */       
/*  868 */       if (catalog != null) {
/*  869 */         pStmt.setString(1, catalog);
/*      */       } else {
/*  871 */         pStmt.setString(1, "%");
/*      */       }
/*      */       
/*  874 */       pStmt.setString(2, table);
/*      */       
/*  876 */       java.sql.ResultSet rs = executeMetadataQuery(pStmt);
/*      */       
/*  878 */       ((ResultSet)rs).redefineFieldsForDBMD(new Field[] { new Field("", "TABLE_CAT", 1, 255), new Field("", "TABLE_SCHEM", 1, 0), new Field("", "TABLE_NAME", 1, 255), new Field("", "NON_UNIQUE", 1, 4), new Field("", "INDEX_QUALIFIER", 1, 1), new Field("", "INDEX_NAME", 1, 32), new Field("", "TYPE", 1, 32), new Field("", "ORDINAL_POSITION", 5, 5), new Field("", "COLUMN_NAME", 1, 32), new Field("", "ASC_OR_DESC", 1, 1), new Field("", "CARDINALITY", 4, 10), new Field("", "PAGES", 4, 10), new Field("", "FILTER_CONDITION", 1, 32) });
/*      */       
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  893 */       return rs;
/*      */     } finally {
/*  895 */       if (pStmt != null) {
/*  896 */         pStmt.close();
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
/*      */   public java.sql.ResultSet getPrimaryKeys(String catalog, String schema, String table)
/*      */     throws SQLException
/*      */   {
/*  929 */     if ((catalog == null) && 
/*  930 */       (this.conn.getNullCatalogMeansCurrent())) {
/*  931 */       catalog = this.database;
/*      */     }
/*      */     
/*      */ 
/*  935 */     if (table == null) {
/*  936 */       throw SQLError.createSQLException("Table not specified.", "S1009");
/*      */     }
/*      */     
/*      */ 
/*  940 */     String sql = "SELECT TABLE_SCHEMA AS TABLE_CAT, NULL AS TABLE_SCHEM, TABLE_NAME, COLUMN_NAME, SEQ_IN_INDEX AS KEY_SEQ, 'PRIMARY' AS PK_NAME FROM INFORMATION_SCHEMA.STATISTICS WHERE TABLE_SCHEMA LIKE ? AND TABLE_NAME LIKE ? AND INDEX_NAME='PRIMARY' ORDER BY TABLE_SCHEMA, TABLE_NAME, INDEX_NAME, SEQ_IN_INDEX";
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*  945 */     PreparedStatement pStmt = null;
/*      */     try
/*      */     {
/*  948 */       pStmt = prepareMetaDataSafeStatement(sql);
/*      */       
/*  950 */       if (catalog != null) {
/*  951 */         pStmt.setString(1, catalog);
/*      */       } else {
/*  953 */         pStmt.setString(1, "%");
/*      */       }
/*      */       
/*  956 */       pStmt.setString(2, table);
/*      */       
/*  958 */       java.sql.ResultSet rs = executeMetadataQuery(pStmt);
/*  959 */       ((ResultSet)rs).redefineFieldsForDBMD(new Field[] { new Field("", "TABLE_CAT", 1, 255), new Field("", "TABLE_SCHEM", 1, 0), new Field("", "TABLE_NAME", 1, 255), new Field("", "COLUMN_NAME", 1, 32), new Field("", "KEY_SEQ", 5, 5), new Field("", "PK_NAME", 1, 32) });
/*      */       
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  967 */       return rs;
/*      */     } finally {
/*  969 */       if (pStmt != null) {
/*  970 */         pStmt.close();
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
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public java.sql.ResultSet getProcedures(String catalog, String schemaPattern, String procedureNamePattern)
/*      */     throws SQLException
/*      */   {
/* 1018 */     if ((procedureNamePattern == null) || (procedureNamePattern.length() == 0))
/*      */     {
/* 1020 */       if (this.conn.getNullNamePatternMatchesAll()) {
/* 1021 */         procedureNamePattern = "%";
/*      */       } else {
/* 1023 */         throw SQLError.createSQLException("Procedure name pattern can not be NULL or empty.", "S1009");
/*      */       }
/*      */     }
/*      */     
/*      */ 
/*      */ 
/* 1029 */     String db = null;
/*      */     
/* 1031 */     if ((catalog == null) && 
/* 1032 */       (this.conn.getNullCatalogMeansCurrent())) {
/* 1033 */       db = this.database;
/*      */     }
/*      */     
/*      */ 
/* 1037 */     String sql = "SELECT ROUTINE_SCHEMA AS PROCEDURE_CAT, NULL AS PROCEDURE_SCHEM, ROUTINE_NAME AS PROCEDURE_NAME, NULL AS RESERVED_1, NULL AS RESERVED_2, NULL AS RESERVED_3, ROUTINE_COMMENT AS REMARKS, CASE WHEN ROUTINE_TYPE = 'PROCEDURE' THEN 1 WHEN ROUTINE_TYPE='FUNCTION' THEN 2 ELSE 0 END AS PROCEDURE_TYPE FROM INFORMATION_SCHEMA.ROUTINES WHERE ROUTINE_SCHEMA LIKE ? AND ROUTINE_NAME LIKE ? ORDER BY ROUTINE_SCHEMA, ROUTINE_NAME";
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 1050 */     PreparedStatement pStmt = null;
/*      */     try
/*      */     {
/* 1053 */       pStmt = prepareMetaDataSafeStatement(sql);
/*      */       
/* 1055 */       if (db != null) {
/* 1056 */         pStmt.setString(1, db);
/*      */       } else {
/* 1058 */         pStmt.setString(1, "%");
/*      */       }
/*      */       
/* 1061 */       pStmt.setString(2, procedureNamePattern);
/*      */       
/* 1063 */       java.sql.ResultSet rs = executeMetadataQuery(pStmt);
/* 1064 */       ((ResultSet)rs).redefineFieldsForDBMD(new Field[] { new Field("", "PROCEDURE_CAT", 1, 0), new Field("", "PROCEDURE_SCHEM", 1, 0), new Field("", "PROCEDURE_NAME", 1, 0), new Field("", "reserved1", 1, 0), new Field("", "reserved2", 1, 0), new Field("", "reserved3", 1, 0), new Field("", "REMARKS", 1, 0), new Field("", "PROCEDURE_TYPE", 5, 0) });
/*      */       
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 1074 */       return rs;
/*      */     } finally {
/* 1076 */       if (pStmt != null) {
/* 1077 */         pStmt.close();
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
/*      */   public java.sql.ResultSet getTables(String catalog, String schemaPattern, String tableNamePattern, String[] types)
/*      */     throws SQLException
/*      */   {
/* 1120 */     if ((catalog == null) && 
/* 1121 */       (this.conn.getNullCatalogMeansCurrent())) {
/* 1122 */       catalog = this.database;
/*      */     }
/*      */     
/*      */ 
/* 1126 */     if (tableNamePattern == null) {
/* 1127 */       if (this.conn.getNullNamePatternMatchesAll()) {
/* 1128 */         tableNamePattern = "%";
/*      */       } else {
/* 1130 */         throw SQLError.createSQLException("Table name pattern can not be NULL or empty.", "S1009");
/*      */       }
/*      */     }
/*      */     
/*      */ 
/*      */ 
/* 1136 */     PreparedStatement pStmt = null;
/*      */     
/* 1138 */     String sql = "SELECT TABLE_SCHEMA AS TABLE_CAT, NULL AS TABLE_SCHEM, TABLE_NAME, CASE WHEN TABLE_TYPE='BASE TABLE' THEN 'TABLE' WHEN TABLE_TYPE='TEMPORARY' THEN 'LOCAL_TEMPORARY' ELSE TABLE_TYPE END AS TABLE_TYPE, TABLE_COMMENT AS REMARKS FROM INFORMATION_SCHEMA.TABLES WHERE TABLE_SCHEMA LIKE ? AND TABLE_NAME LIKE ? AND TABLE_TYPE IN (?,?,?) ORDER BY TABLE_TYPE, TABLE_SCHEMA, TABLE_NAME";
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     try
/*      */     {
/* 1146 */       pStmt = prepareMetaDataSafeStatement(sql);
/*      */       
/* 1148 */       if (catalog != null) {
/* 1149 */         pStmt.setString(1, catalog);
/*      */       } else {
/* 1151 */         pStmt.setString(1, "%");
/*      */       }
/*      */       
/* 1154 */       pStmt.setString(2, tableNamePattern);
/*      */       
/*      */ 
/*      */ 
/* 1158 */       if ((types == null) || (types.length == 0)) {
/* 1159 */         pStmt.setString(3, "BASE TABLE");
/* 1160 */         pStmt.setString(4, "VIEW");
/* 1161 */         pStmt.setString(5, "TEMPORARY");
/*      */       } else {
/* 1163 */         pStmt.setNull(3, 12);
/* 1164 */         pStmt.setNull(4, 12);
/* 1165 */         pStmt.setNull(5, 12);
/*      */         
/* 1167 */         for (int i = 0; i < types.length; i++) {
/* 1168 */           if ("TABLE".equalsIgnoreCase(types[i])) {
/* 1169 */             pStmt.setString(3, "BASE TABLE");
/*      */           }
/*      */           
/* 1172 */           if ("VIEW".equalsIgnoreCase(types[i])) {
/* 1173 */             pStmt.setString(4, "VIEW");
/*      */           }
/*      */           
/* 1176 */           if ("LOCAL TEMPORARY".equalsIgnoreCase(types[i])) {
/* 1177 */             pStmt.setString(5, "TEMPORARY");
/*      */           }
/*      */         }
/*      */       }
/*      */       
/* 1182 */       java.sql.ResultSet rs = executeMetadataQuery(pStmt);
/*      */       
/* 1184 */       ((ResultSet)rs).redefineFieldsForDBMD(new Field[] { new Field("", "TABLE_CAT", 12, catalog == null ? 0 : catalog.length()), new Field("", "TABLE_SCHEM", 12, 0), new Field("", "TABLE_NAME", 12, 255), new Field("", "TABLE_TYPE", 12, 5), new Field("", "REMARKS", 12, 0) });
/*      */       
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 1192 */       return rs;
/*      */     } finally {
/* 1194 */       if (pStmt != null) {
/* 1195 */         pStmt.close();
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */   private PreparedStatement prepareMetaDataSafeStatement(String sql)
/*      */     throws SQLException
/*      */   {
/* 1204 */     PreparedStatement pStmt = this.conn.clientPrepareStatement(sql);
/*      */     
/* 1206 */     if (pStmt.getMaxRows() != 0) {
/* 1207 */       pStmt.setMaxRows(0);
/*      */     }
/*      */     
/* 1210 */     pStmt.setHoldResultsOpenOverClose(true);
/*      */     
/* 1212 */     return pStmt;
/*      */   }
/*      */ }


/* Location:              E:\java\java学习\hutubill\lib\all.jar!\com\mysql\jdbc\DatabaseMetaDataUsingInfoSchema.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version:       0.7.1
 */
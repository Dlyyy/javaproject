/*      */ package com.mysql.jdbc;
/*      */ 
/*      */ import com.mysql.jdbc.profiler.ProfileEventSink;
/*      */ import com.mysql.jdbc.profiler.ProfilerEvent;
/*      */ import java.io.InputStream;
/*      */ import java.io.Reader;
/*      */ import java.io.UnsupportedEncodingException;
/*      */ import java.math.BigDecimal;
/*      */ import java.sql.Blob;
/*      */ import java.sql.Clob;
/*      */ import java.sql.DatabaseMetaData;
/*      */ import java.sql.Date;
/*      */ import java.sql.ResultSetMetaData;
/*      */ import java.sql.SQLException;
/*      */ import java.sql.Time;
/*      */ import java.sql.Timestamp;
/*      */ import java.util.ArrayList;
/*      */ import java.util.HashMap;
/*      */ import java.util.List;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ public class UpdatableResultSet
/*      */   extends ResultSet
/*      */ {
/*   45 */   private static final byte[] STREAM_DATA_MARKER = "** STREAM DATA **".getBytes();
/*      */   
/*      */ 
/*      */   private SingleByteCharsetConverter charConverter;
/*      */   
/*      */ 
/*      */   private String charEncoding;
/*      */   
/*      */ 
/*      */   private byte[][] defaultColumnValue;
/*      */   
/*   56 */   private PreparedStatement deleter = null;
/*      */   
/*   58 */   private String deleteSQL = null;
/*      */   
/*   60 */   private boolean initializedCharConverter = false;
/*      */   
/*      */ 
/*   63 */   private PreparedStatement inserter = null;
/*      */   
/*   65 */   private String insertSQL = null;
/*      */   
/*      */ 
/*   68 */   private boolean isUpdatable = false;
/*      */   
/*      */ 
/*   71 */   private String notUpdatableReason = null;
/*      */   
/*      */ 
/*   74 */   private List primaryKeyIndicies = null;
/*      */   
/*      */   private String qualifiedAndQuotedTableName;
/*      */   
/*   78 */   private String quotedIdChar = null;
/*      */   
/*      */ 
/*      */   private PreparedStatement refresher;
/*      */   
/*   83 */   private String refreshSQL = null;
/*      */   
/*      */ 
/*      */   private Object[] savedCurrentRow;
/*      */   
/*      */ 
/*      */   private String tableOnlyName;
/*      */   
/*   91 */   private PreparedStatement updater = null;
/*      */   
/*      */ 
/*   94 */   private String updateSQL = null;
/*      */   
/*   96 */   private boolean populateInserterWithDefaultValues = false;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public UpdatableResultSet(long updateCount, long updateID, Connection conn, Statement creatorStmt)
/*      */     throws SQLException
/*      */   {
/*  115 */     super(updateCount, updateID, conn, creatorStmt);
/*  116 */     checkUpdatability();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public UpdatableResultSet(String catalog, Field[] fields, RowData tuples, Connection conn, Statement creatorStmt)
/*      */     throws SQLException
/*      */   {
/*  138 */     super(catalog, fields, tuples, conn, creatorStmt);
/*  139 */     checkUpdatability();
/*  140 */     this.populateInserterWithDefaultValues = this.connection.getPopulateInsertRowWithDefaultValues();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public synchronized boolean absolute(int row)
/*      */     throws SQLException
/*      */   {
/*  183 */     return super.absolute(row);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public synchronized void afterLast()
/*      */     throws SQLException
/*      */   {
/*  199 */     super.afterLast();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public synchronized void beforeFirst()
/*      */     throws SQLException
/*      */   {
/*  215 */     super.beforeFirst();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public synchronized void cancelRowUpdates()
/*      */     throws SQLException
/*      */   {
/*  229 */     checkClosed();
/*      */     
/*  231 */     if (this.doingUpdates) {
/*  232 */       this.doingUpdates = false;
/*  233 */       this.updater.clearParameters();
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   protected void checkRowPos()
/*      */     throws SQLException
/*      */   {
/*  243 */     checkClosed();
/*      */     
/*  245 */     if (!this.onInsertRow) {
/*  246 */       super.checkRowPos();
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected void checkUpdatability()
/*      */     throws SQLException
/*      */   {
/*  257 */     if (this.fields == null)
/*      */     {
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  264 */       return;
/*      */     }
/*      */     
/*  267 */     String singleTableName = null;
/*  268 */     String catalogName = null;
/*      */     
/*  270 */     int primaryKeyCount = 0;
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  277 */     if ((this.catalog == null) || (this.catalog.length() == 0)) {
/*  278 */       this.catalog = this.fields[0].getDatabaseName();
/*      */       
/*  280 */       if ((this.catalog == null) || (this.catalog.length() == 0)) {
/*  281 */         throw SQLError.createSQLException(Messages.getString("UpdatableResultSet.43"), "S1009");
/*      */       }
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*  287 */     if (this.fields.length > 0) {
/*  288 */       singleTableName = this.fields[0].getOriginalTableName();
/*  289 */       catalogName = this.fields[0].getDatabaseName();
/*      */       
/*  291 */       if (singleTableName == null) {
/*  292 */         singleTableName = this.fields[0].getTableName();
/*  293 */         catalogName = this.catalog;
/*      */       }
/*      */       
/*  296 */       if ((singleTableName != null) && (singleTableName.length() == 0)) {
/*  297 */         this.isUpdatable = false;
/*  298 */         this.notUpdatableReason = Messages.getString("NotUpdatableReason.3");
/*      */         
/*  300 */         return;
/*      */       }
/*      */       
/*  303 */       if (this.fields[0].isPrimaryKey()) {
/*  304 */         primaryKeyCount++;
/*      */       }
/*      */       
/*      */ 
/*      */ 
/*      */ 
/*  310 */       for (int i = 1; i < this.fields.length; i++) {
/*  311 */         String otherTableName = this.fields[i].getOriginalTableName();
/*  312 */         String otherCatalogName = this.fields[i].getDatabaseName();
/*      */         
/*  314 */         if (otherTableName == null) {
/*  315 */           otherTableName = this.fields[i].getTableName();
/*  316 */           otherCatalogName = this.catalog;
/*      */         }
/*      */         
/*  319 */         if ((otherTableName != null) && (otherTableName.length() == 0)) {
/*  320 */           this.isUpdatable = false;
/*  321 */           this.notUpdatableReason = Messages.getString("NotUpdatableReason.3");
/*      */           
/*  323 */           return;
/*      */         }
/*      */         
/*  326 */         if ((singleTableName == null) || (!otherTableName.equals(singleTableName)))
/*      */         {
/*  328 */           this.isUpdatable = false;
/*  329 */           this.notUpdatableReason = Messages.getString("NotUpdatableReason.0");
/*      */           
/*  331 */           return;
/*      */         }
/*      */         
/*      */ 
/*  335 */         if ((catalogName == null) || (!otherCatalogName.equals(catalogName)))
/*      */         {
/*  337 */           this.isUpdatable = false;
/*  338 */           this.notUpdatableReason = Messages.getString("NotUpdatableReason.1");
/*      */           
/*  340 */           return;
/*      */         }
/*      */         
/*  343 */         if (this.fields[i].isPrimaryKey()) {
/*  344 */           primaryKeyCount++;
/*      */         }
/*      */       }
/*      */       
/*  348 */       if ((singleTableName == null) || (singleTableName.length() == 0)) {
/*  349 */         this.isUpdatable = false;
/*  350 */         this.notUpdatableReason = Messages.getString("NotUpdatableReason.2");
/*      */       }
/*      */     }
/*      */     else
/*      */     {
/*  355 */       this.isUpdatable = false;
/*  356 */       this.notUpdatableReason = Messages.getString("NotUpdatableReason.3");
/*      */       
/*  358 */       return;
/*      */     }
/*      */     
/*  361 */     if (this.connection.getStrictUpdates()) {
/*  362 */       DatabaseMetaData dbmd = this.connection.getMetaData();
/*      */       
/*  364 */       java.sql.ResultSet rs = null;
/*  365 */       HashMap primaryKeyNames = new HashMap();
/*      */       try
/*      */       {
/*  368 */         rs = dbmd.getPrimaryKeys(catalogName, null, singleTableName);
/*      */         
/*  370 */         while (rs.next()) {
/*  371 */           String keyName = rs.getString(4);
/*  372 */           keyName = keyName.toUpperCase();
/*  373 */           primaryKeyNames.put(keyName, keyName);
/*      */         }
/*      */       } finally {
/*  376 */         if (rs != null) {
/*      */           try {
/*  378 */             rs.close();
/*      */           } catch (Exception ex) {
/*  380 */             AssertionFailedException.shouldNotHappen(ex);
/*      */           }
/*      */           
/*  383 */           rs = null;
/*      */         }
/*      */       }
/*      */       
/*  387 */       int existingPrimaryKeysCount = primaryKeyNames.size();
/*      */       
/*  389 */       if (existingPrimaryKeysCount == 0) {
/*  390 */         this.isUpdatable = false;
/*  391 */         this.notUpdatableReason = Messages.getString("NotUpdatableReason.5");
/*      */         
/*  393 */         return;
/*      */       }
/*      */       
/*      */ 
/*      */ 
/*      */ 
/*  399 */       for (int i = 0; i < this.fields.length; i++) {
/*  400 */         if (this.fields[i].isPrimaryKey()) {
/*  401 */           String columnNameUC = this.fields[i].getName().toUpperCase();
/*      */           
/*      */ 
/*  404 */           if (primaryKeyNames.remove(columnNameUC) == null)
/*      */           {
/*  406 */             String originalName = this.fields[i].getOriginalName();
/*      */             
/*  408 */             if ((originalName != null) && 
/*  409 */               (primaryKeyNames.remove(originalName.toUpperCase()) == null))
/*      */             {
/*      */ 
/*  412 */               this.isUpdatable = false;
/*  413 */               this.notUpdatableReason = Messages.getString("NotUpdatableReason.6", new Object[] { originalName });
/*      */               
/*      */ 
/*  416 */               return;
/*      */             }
/*      */           }
/*      */         }
/*      */       }
/*      */       
/*      */ 
/*  423 */       this.isUpdatable = primaryKeyNames.isEmpty();
/*      */       
/*  425 */       if (!this.isUpdatable) {
/*  426 */         if (existingPrimaryKeysCount > 1) {
/*  427 */           this.notUpdatableReason = Messages.getString("NotUpdatableReason.7");
/*      */         } else {
/*  429 */           this.notUpdatableReason = Messages.getString("NotUpdatableReason.4");
/*      */         }
/*      */         
/*  432 */         return;
/*      */       }
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*  439 */     if (primaryKeyCount == 0) {
/*  440 */       this.isUpdatable = false;
/*  441 */       this.notUpdatableReason = Messages.getString("NotUpdatableReason.4");
/*      */       
/*  443 */       return;
/*      */     }
/*      */     
/*  446 */     this.isUpdatable = true;
/*  447 */     this.notUpdatableReason = null;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public synchronized void deleteRow()
/*      */     throws SQLException
/*      */   {
/*  463 */     checkClosed();
/*      */     
/*  465 */     if (!this.isUpdatable) {
/*  466 */       throw new NotUpdatable(this.notUpdatableReason);
/*      */     }
/*      */     
/*  469 */     if (this.onInsertRow)
/*  470 */       throw SQLError.createSQLException(Messages.getString("UpdatableResultSet.1"));
/*  471 */     if (this.rowData.size() == 0)
/*  472 */       throw SQLError.createSQLException(Messages.getString("UpdatableResultSet.2"));
/*  473 */     if (isBeforeFirst())
/*  474 */       throw SQLError.createSQLException(Messages.getString("UpdatableResultSet.3"));
/*  475 */     if (isAfterLast()) {
/*  476 */       throw SQLError.createSQLException(Messages.getString("UpdatableResultSet.4"));
/*      */     }
/*      */     
/*  479 */     if (this.deleter == null) {
/*  480 */       if (this.deleteSQL == null) {
/*  481 */         generateStatements();
/*      */       }
/*      */       
/*  484 */       this.deleter = this.connection.clientPrepareStatement(this.deleteSQL);
/*      */     }
/*      */     
/*      */ 
/*  488 */     this.deleter.clearParameters();
/*      */     
/*  490 */     String characterEncoding = null;
/*      */     
/*  492 */     if (this.connection.getUseUnicode()) {
/*  493 */       characterEncoding = this.connection.getEncoding();
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */     try
/*      */     {
/*  500 */       int numKeys = this.primaryKeyIndicies.size();
/*      */       
/*  502 */       if (numKeys == 1) {
/*  503 */         int index = ((Integer)this.primaryKeyIndicies.get(0)).intValue();
/*      */         
/*  505 */         String currentVal = characterEncoding == null ? new String((byte[])this.thisRow[index]) : new String((byte[])this.thisRow[index], characterEncoding);
/*      */         
/*      */ 
/*  508 */         this.deleter.setString(1, currentVal);
/*      */       } else {
/*  510 */         for (int i = 0; i < numKeys; i++) {
/*  511 */           int index = ((Integer)this.primaryKeyIndicies.get(i)).intValue();
/*      */           
/*  513 */           String currentVal = characterEncoding == null ? new String((byte[])this.thisRow[index]) : new String((byte[])this.thisRow[index], characterEncoding);
/*      */           
/*      */ 
/*      */ 
/*  517 */           this.deleter.setString(i + 1, currentVal);
/*      */         }
/*      */       }
/*      */       
/*  521 */       this.deleter.executeUpdate();
/*  522 */       this.rowData.removeRow(this.rowData.getCurrentRowNumber());
/*      */     } catch (UnsupportedEncodingException encodingEx) {
/*  524 */       throw SQLError.createSQLException(Messages.getString("UpdatableResultSet.39", new Object[] { this.charEncoding }), "S1009");
/*      */     }
/*      */   }
/*      */   
/*      */   private synchronized void extractDefaultValues()
/*      */     throws SQLException
/*      */   {
/*  531 */     DatabaseMetaData dbmd = this.connection.getMetaData();
/*      */     
/*  533 */     java.sql.ResultSet columnsResultSet = null;
/*      */     try
/*      */     {
/*  536 */       columnsResultSet = dbmd.getColumns(this.catalog, null, this.tableOnlyName, "%");
/*      */       
/*      */ 
/*  539 */       HashMap columnNameToDefaultValueMap = new HashMap(this.fields.length);
/*      */       
/*      */ 
/*  542 */       while (columnsResultSet.next()) {
/*  543 */         String columnName = columnsResultSet.getString("COLUMN_NAME");
/*  544 */         byte[] defaultValue = columnsResultSet.getBytes("COLUMN_DEF");
/*      */         
/*  546 */         columnNameToDefaultValueMap.put(columnName, defaultValue);
/*      */       }
/*      */       
/*  549 */       int numFields = this.fields.length;
/*      */       
/*  551 */       this.defaultColumnValue = new byte[numFields][];
/*      */       
/*  553 */       for (int i = 0; i < numFields; i++) {
/*  554 */         String defValTableName = this.fields[i].getOriginalName();
/*      */         
/*  556 */         if ((defValTableName == null) || (defValTableName.length() == 0))
/*      */         {
/*  558 */           defValTableName = this.fields[i].getName();
/*      */         }
/*      */         
/*  561 */         if (defValTableName != null) {
/*  562 */           byte[] defaultVal = (byte[])columnNameToDefaultValueMap.get(defValTableName);
/*      */           
/*      */ 
/*  565 */           this.defaultColumnValue[i] = defaultVal;
/*      */         }
/*      */       }
/*      */     } finally {
/*  569 */       if (columnsResultSet != null) {
/*  570 */         columnsResultSet.close();
/*      */         
/*  572 */         columnsResultSet = null;
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
/*      */   public synchronized boolean first()
/*      */     throws SQLException
/*      */   {
/*  591 */     return super.first();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected synchronized void generateStatements()
/*      */     throws SQLException
/*      */   {
/*  604 */     if (!this.isUpdatable) {
/*  605 */       this.doingUpdates = false;
/*  606 */       this.onInsertRow = false;
/*      */       
/*  608 */       throw new NotUpdatable(this.notUpdatableReason);
/*      */     }
/*      */     
/*  611 */     String quotedId = getQuotedIdChar();
/*      */     
/*  613 */     if (this.fields[0].getOriginalTableName() != null) {
/*  614 */       StringBuffer tableNameBuffer = new StringBuffer();
/*      */       
/*  616 */       String databaseName = this.fields[0].getDatabaseName();
/*      */       
/*  618 */       if ((databaseName != null) && (databaseName.length() > 0)) {
/*  619 */         tableNameBuffer.append(quotedId);
/*  620 */         tableNameBuffer.append(databaseName);
/*  621 */         tableNameBuffer.append(quotedId);
/*  622 */         tableNameBuffer.append('.');
/*      */       }
/*      */       
/*  625 */       this.tableOnlyName = this.fields[0].getOriginalTableName();
/*      */       
/*  627 */       tableNameBuffer.append(quotedId);
/*  628 */       tableNameBuffer.append(this.tableOnlyName);
/*  629 */       tableNameBuffer.append(quotedId);
/*      */       
/*  631 */       this.qualifiedAndQuotedTableName = tableNameBuffer.toString();
/*      */     } else {
/*  633 */       StringBuffer tableNameBuffer = new StringBuffer();
/*      */       
/*  635 */       this.tableOnlyName = this.fields[0].getTableName();
/*      */       
/*  637 */       tableNameBuffer.append(quotedId);
/*  638 */       tableNameBuffer.append(this.tableOnlyName);
/*  639 */       tableNameBuffer.append(quotedId);
/*      */       
/*  641 */       this.qualifiedAndQuotedTableName = tableNameBuffer.toString();
/*      */     }
/*      */     
/*  644 */     this.primaryKeyIndicies = new ArrayList();
/*      */     
/*  646 */     StringBuffer fieldValues = new StringBuffer();
/*  647 */     StringBuffer keyValues = new StringBuffer();
/*  648 */     StringBuffer columnNames = new StringBuffer();
/*  649 */     StringBuffer insertPlaceHolders = new StringBuffer();
/*  650 */     boolean firstTime = true;
/*  651 */     boolean keysFirstTime = true;
/*      */     
/*  653 */     String equalsStr = this.connection.versionMeetsMinimum(3, 23, 0) ? "<=>" : "=";
/*      */     
/*      */ 
/*  656 */     for (int i = 0; i < this.fields.length; i++) {
/*  657 */       String originalColumnName = this.fields[i].getOriginalName();
/*  658 */       String columnName = null;
/*      */       
/*  660 */       if ((this.connection.getIO().hasLongColumnInfo()) && (originalColumnName != null) && (originalColumnName.length() > 0))
/*      */       {
/*      */ 
/*  663 */         columnName = originalColumnName;
/*      */       } else {
/*  665 */         columnName = this.fields[i].getName();
/*      */       }
/*      */       
/*  668 */       if (this.fields[i].isPrimaryKey()) {
/*  669 */         this.primaryKeyIndicies.add(new Integer(i));
/*      */         
/*  671 */         if (!keysFirstTime) {
/*  672 */           keyValues.append(" AND ");
/*      */         } else {
/*  674 */           keysFirstTime = false;
/*      */         }
/*      */         
/*  677 */         keyValues.append(quotedId);
/*  678 */         keyValues.append(columnName);
/*  679 */         keyValues.append(quotedId);
/*  680 */         keyValues.append(equalsStr);
/*  681 */         keyValues.append("?");
/*      */       }
/*      */       
/*  684 */       if (firstTime) {
/*  685 */         firstTime = false;
/*  686 */         fieldValues.append("SET ");
/*      */       } else {
/*  688 */         fieldValues.append(",");
/*  689 */         columnNames.append(",");
/*  690 */         insertPlaceHolders.append(",");
/*      */       }
/*      */       
/*  693 */       insertPlaceHolders.append("?");
/*      */       
/*  695 */       columnNames.append(quotedId);
/*  696 */       columnNames.append(columnName);
/*  697 */       columnNames.append(quotedId);
/*      */       
/*  699 */       fieldValues.append(quotedId);
/*  700 */       fieldValues.append(columnName);
/*  701 */       fieldValues.append(quotedId);
/*  702 */       fieldValues.append("=?");
/*      */     }
/*      */     
/*  705 */     this.updateSQL = ("UPDATE " + this.qualifiedAndQuotedTableName + " " + fieldValues.toString() + " WHERE " + keyValues.toString());
/*      */     
/*      */ 
/*  708 */     this.insertSQL = ("INSERT INTO " + this.qualifiedAndQuotedTableName + " (" + columnNames.toString() + ") VALUES (" + insertPlaceHolders.toString() + ")");
/*      */     
/*      */ 
/*  711 */     this.refreshSQL = ("SELECT " + columnNames.toString() + " FROM " + this.qualifiedAndQuotedTableName + " WHERE " + keyValues.toString());
/*      */     
/*      */ 
/*  714 */     this.deleteSQL = ("DELETE FROM " + this.qualifiedAndQuotedTableName + " WHERE " + keyValues.toString());
/*      */   }
/*      */   
/*      */ 
/*      */   private synchronized SingleByteCharsetConverter getCharConverter()
/*      */     throws SQLException
/*      */   {
/*  721 */     if (!this.initializedCharConverter) {
/*  722 */       this.initializedCharConverter = true;
/*      */       
/*  724 */       if (this.connection.getUseUnicode()) {
/*  725 */         this.charEncoding = this.connection.getEncoding();
/*  726 */         this.charConverter = this.connection.getCharsetConverter(this.charEncoding);
/*      */       }
/*      */     }
/*      */     
/*      */ 
/*  731 */     return this.charConverter;
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
/*  744 */     return this.isUpdatable ? 1008 : 1007;
/*      */   }
/*      */   
/*      */   private synchronized String getQuotedIdChar() throws SQLException {
/*  748 */     if (this.quotedIdChar == null) {
/*  749 */       boolean useQuotedIdentifiers = this.connection.supportsQuotedIdentifiers();
/*      */       
/*      */ 
/*  752 */       if (useQuotedIdentifiers) {
/*  753 */         DatabaseMetaData dbmd = this.connection.getMetaData();
/*  754 */         this.quotedIdChar = dbmd.getIdentifierQuoteString();
/*      */       } else {
/*  756 */         this.quotedIdChar = "";
/*      */       }
/*      */     }
/*      */     
/*  760 */     return this.quotedIdChar;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public synchronized void insertRow()
/*      */     throws SQLException
/*      */   {
/*  773 */     checkClosed();
/*      */     
/*  775 */     if (!this.onInsertRow) {
/*  776 */       throw SQLError.createSQLException(Messages.getString("UpdatableResultSet.7"));
/*      */     }
/*      */     
/*  779 */     this.inserter.executeUpdate();
/*      */     
/*  781 */     long autoIncrementId = this.inserter.getLastInsertID();
/*  782 */     int numFields = this.fields.length;
/*  783 */     byte[][] newRow = new byte[numFields][];
/*      */     
/*  785 */     for (int i = 0; i < numFields; i++) {
/*  786 */       if (this.inserter.isNull(i)) {
/*  787 */         newRow[i] = null;
/*      */       } else {
/*  789 */         newRow[i] = this.inserter.getBytesRepresentation(i);
/*      */       }
/*      */       
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  796 */       if ((this.fields[i].isAutoIncrement()) && (autoIncrementId > 0L)) {
/*  797 */         newRow[i] = String.valueOf(autoIncrementId).getBytes();
/*  798 */         this.inserter.setBytesNoEscapeNoQuotes(i + 1, newRow[i]);
/*      */       }
/*      */     }
/*      */     
/*  802 */     refreshRow(this.inserter, newRow);
/*      */     
/*  804 */     this.rowData.addRow(newRow);
/*  805 */     resetInserter();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public synchronized boolean isAfterLast()
/*      */     throws SQLException
/*      */   {
/*  823 */     return super.isAfterLast();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public synchronized boolean isBeforeFirst()
/*      */     throws SQLException
/*      */   {
/*  840 */     return super.isBeforeFirst();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public synchronized boolean isFirst()
/*      */     throws SQLException
/*      */   {
/*  856 */     return super.isFirst();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public synchronized boolean isLast()
/*      */     throws SQLException
/*      */   {
/*  875 */     return super.isLast();
/*      */   }
/*      */   
/*      */   boolean isUpdatable() {
/*  879 */     return this.isUpdatable;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public synchronized boolean last()
/*      */     throws SQLException
/*      */   {
/*  896 */     return super.last();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public synchronized void moveToCurrentRow()
/*      */     throws SQLException
/*      */   {
/*  910 */     checkClosed();
/*      */     
/*  912 */     if (!this.isUpdatable) {
/*  913 */       throw new NotUpdatable(this.notUpdatableReason);
/*      */     }
/*      */     
/*  916 */     if (this.onInsertRow) {
/*  917 */       this.onInsertRow = false;
/*  918 */       this.thisRow = this.savedCurrentRow;
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
/*      */   public synchronized void moveToInsertRow()
/*      */     throws SQLException
/*      */   {
/*  940 */     checkClosed();
/*      */     
/*  942 */     if (!this.isUpdatable) {
/*  943 */       throw new NotUpdatable(this.notUpdatableReason);
/*      */     }
/*      */     
/*  946 */     if (this.inserter == null) {
/*  947 */       if (this.insertSQL == null) {
/*  948 */         generateStatements();
/*      */       }
/*      */       
/*  951 */       this.inserter = this.connection.clientPrepareStatement(this.insertSQL);
/*      */       
/*  953 */       if (this.populateInserterWithDefaultValues) {
/*  954 */         extractDefaultValues();
/*      */       }
/*      */       
/*  957 */       resetInserter();
/*      */     } else {
/*  959 */       resetInserter();
/*      */     }
/*      */     
/*  962 */     int numFields = this.fields.length;
/*      */     
/*  964 */     this.onInsertRow = true;
/*  965 */     this.doingUpdates = false;
/*  966 */     this.savedCurrentRow = this.thisRow;
/*  967 */     this.thisRow = new byte[numFields][];
/*      */     
/*  969 */     for (int i = 0; i < numFields; i++) {
/*  970 */       if (!this.populateInserterWithDefaultValues) {
/*  971 */         this.inserter.setBytesNoEscapeNoQuotes(i + 1, "DEFAULT".getBytes());
/*      */         
/*  973 */         this.thisRow[i] = null;
/*      */       }
/*  975 */       else if (this.defaultColumnValue[i] != null) {
/*  976 */         Field f = this.fields[i];
/*      */         
/*  978 */         switch (f.getMysqlType())
/*      */         {
/*      */         case 7: 
/*      */         case 10: 
/*      */         case 11: 
/*      */         case 12: 
/*      */         case 14: 
/*  985 */           if ((this.defaultColumnValue[i].length > 7) && (this.defaultColumnValue[i][0] == 67) && (this.defaultColumnValue[i][1] == 85) && (this.defaultColumnValue[i][2] == 82) && (this.defaultColumnValue[i][3] == 82) && (this.defaultColumnValue[i][4] == 69) && (this.defaultColumnValue[i][5] == 78) && (this.defaultColumnValue[i][6] == 84) && (this.defaultColumnValue[i][7] == 95))
/*      */           {
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  994 */             this.inserter.setBytesNoEscapeNoQuotes(i + 1, this.defaultColumnValue[i]);
/*      */           }
/*      */           
/*  997 */           break;
/*      */         }
/*      */         
/* 1000 */         this.inserter.setBytes(i + 1, this.defaultColumnValue[i], false, false);
/*      */         
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 1006 */         byte[] defaultValueCopy = new byte[this.defaultColumnValue[i].length];
/* 1007 */         System.arraycopy(this.defaultColumnValue[i], 0, defaultValueCopy, 0, defaultValueCopy.length);
/*      */         
/* 1009 */         this.thisRow[i] = defaultValueCopy;
/*      */       } else {
/* 1011 */         this.inserter.setNull(i + 1, 0);
/* 1012 */         this.thisRow[i] = null;
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
/*      */   public synchronized boolean next()
/*      */     throws SQLException
/*      */   {
/* 1034 */     return super.next();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public synchronized boolean prev()
/*      */     throws SQLException
/*      */   {
/* 1053 */     return super.prev();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public synchronized boolean previous()
/*      */     throws SQLException
/*      */   {
/* 1075 */     return super.previous();
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
/* 1088 */     if (this.isClosed) {
/* 1089 */       return;
/*      */     }
/*      */     
/* 1092 */     SQLException sqlEx = null;
/*      */     
/* 1094 */     if ((this.useUsageAdvisor) && 
/* 1095 */       (this.deleter == null) && (this.inserter == null) && (this.refresher == null) && (this.updater == null))
/*      */     {
/* 1097 */       this.eventSink = ProfileEventSink.getInstance(this.connection);
/*      */       
/* 1099 */       String message = Messages.getString("UpdatableResultSet.34");
/*      */       
/* 1101 */       this.eventSink.consumeEvent(new ProfilerEvent((byte)0, "", this.owningStatement == null ? "N/A" : this.owningStatement.currentCatalog, this.connectionId, this.owningStatement == null ? -1 : this.owningStatement.getId(), this.resultId, System.currentTimeMillis(), 0L, Constants.MILLIS_I18N, null, this.pointOfOrigin, message));
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
/*      */     try
/*      */     {
/* 1115 */       if (this.deleter != null) {
/* 1116 */         this.deleter.close();
/*      */       }
/*      */     } catch (SQLException ex) {
/* 1119 */       sqlEx = ex;
/*      */     }
/*      */     try
/*      */     {
/* 1123 */       if (this.inserter != null) {
/* 1124 */         this.inserter.close();
/*      */       }
/*      */     } catch (SQLException ex) {
/* 1127 */       sqlEx = ex;
/*      */     }
/*      */     try
/*      */     {
/* 1131 */       if (this.refresher != null) {
/* 1132 */         this.refresher.close();
/*      */       }
/*      */     } catch (SQLException ex) {
/* 1135 */       sqlEx = ex;
/*      */     }
/*      */     try
/*      */     {
/* 1139 */       if (this.updater != null) {
/* 1140 */         this.updater.close();
/*      */       }
/*      */     } catch (SQLException ex) {
/* 1143 */       sqlEx = ex;
/*      */     }
/*      */     
/* 1146 */     super.realClose(calledExplicitly);
/*      */     
/* 1148 */     if (sqlEx != null) {
/* 1149 */       throw sqlEx;
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
/*      */   public synchronized void refreshRow()
/*      */     throws SQLException
/*      */   {
/* 1174 */     checkClosed();
/*      */     
/* 1176 */     if (!this.isUpdatable) {
/* 1177 */       throw new NotUpdatable();
/*      */     }
/*      */     
/* 1180 */     if (this.onInsertRow)
/* 1181 */       throw SQLError.createSQLException(Messages.getString("UpdatableResultSet.8"));
/* 1182 */     if (this.rowData.size() == 0)
/* 1183 */       throw SQLError.createSQLException(Messages.getString("UpdatableResultSet.9"));
/* 1184 */     if (isBeforeFirst())
/* 1185 */       throw SQLError.createSQLException(Messages.getString("UpdatableResultSet.10"));
/* 1186 */     if (isAfterLast()) {
/* 1187 */       throw SQLError.createSQLException(Messages.getString("UpdatableResultSet.11"));
/*      */     }
/*      */     
/* 1190 */     refreshRow(this.updater, this.thisRow);
/*      */   }
/*      */   
/*      */   private synchronized void refreshRow(PreparedStatement updateInsertStmt, Object[] rowToRefresh) throws SQLException
/*      */   {
/* 1195 */     if (this.refresher == null) {
/* 1196 */       if (this.refreshSQL == null) {
/* 1197 */         generateStatements();
/*      */       }
/*      */       
/* 1200 */       this.refresher = this.connection.clientPrepareStatement(this.refreshSQL);
/*      */     }
/*      */     
/*      */ 
/* 1204 */     this.refresher.clearParameters();
/*      */     
/* 1206 */     int numKeys = this.primaryKeyIndicies.size();
/*      */     
/* 1208 */     if (numKeys == 1) {
/* 1209 */       byte[] dataFrom = null;
/* 1210 */       int index = ((Integer)this.primaryKeyIndicies.get(0)).intValue();
/*      */       
/* 1212 */       if ((!this.doingUpdates) && (!this.onInsertRow)) {
/* 1213 */         dataFrom = (byte[])rowToRefresh[index];
/*      */       } else {
/* 1215 */         dataFrom = updateInsertStmt.getBytesRepresentation(index);
/*      */         
/*      */ 
/* 1218 */         if ((updateInsertStmt.isNull(index)) || (dataFrom.length == 0)) {
/* 1219 */           dataFrom = (byte[])rowToRefresh[index];
/*      */         } else {
/* 1221 */           dataFrom = stripBinaryPrefix(dataFrom);
/*      */         }
/*      */       }
/*      */       
/* 1225 */       this.refresher.setBytesNoEscape(1, dataFrom);
/*      */     } else {
/* 1227 */       for (int i = 0; i < numKeys; i++) {
/* 1228 */         byte[] dataFrom = null;
/* 1229 */         int index = ((Integer)this.primaryKeyIndicies.get(i)).intValue();
/*      */         
/*      */ 
/* 1232 */         if ((!this.doingUpdates) && (!this.onInsertRow)) {
/* 1233 */           dataFrom = (byte[])rowToRefresh[index];
/*      */         } else {
/* 1235 */           dataFrom = updateInsertStmt.getBytesRepresentation(index);
/*      */           
/*      */ 
/* 1238 */           if ((updateInsertStmt.isNull(index)) || (dataFrom.length == 0)) {
/* 1239 */             dataFrom = (byte[])this.thisRow[index];
/*      */           } else {
/* 1241 */             dataFrom = stripBinaryPrefix(dataFrom);
/*      */           }
/*      */         }
/*      */         
/* 1245 */         this.refresher.setBytesNoEscape(i + 1, dataFrom);
/*      */       }
/*      */     }
/*      */     
/* 1249 */     java.sql.ResultSet rs = null;
/*      */     try
/*      */     {
/* 1252 */       rs = this.refresher.executeQuery();
/*      */       
/* 1254 */       int numCols = rs.getMetaData().getColumnCount();
/*      */       
/* 1256 */       if (rs.next()) {
/* 1257 */         for (int i = 0; i < numCols; i++) {
/* 1258 */           byte[] val = rs.getBytes(i + 1);
/*      */           
/* 1260 */           if ((val == null) || (rs.wasNull())) {
/* 1261 */             rowToRefresh[i] = null;
/*      */           } else {
/* 1263 */             rowToRefresh[i] = rs.getBytes(i + 1);
/*      */           }
/*      */         }
/*      */       } else {
/* 1267 */         throw SQLError.createSQLException(Messages.getString("UpdatableResultSet.12"), "S1000");
/*      */       }
/*      */     }
/*      */     finally
/*      */     {
/* 1272 */       if (rs != null) {
/*      */         try {
/* 1274 */           rs.close();
/*      */         }
/*      */         catch (SQLException ex) {}
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
/*      */   public synchronized boolean relative(int rows)
/*      */     throws SQLException
/*      */   {
/* 1309 */     return super.relative(rows);
/*      */   }
/*      */   
/*      */   private void resetInserter() throws SQLException {
/* 1313 */     this.inserter.clearParameters();
/*      */     
/* 1315 */     for (int i = 0; i < this.fields.length; i++) {
/* 1316 */       this.inserter.setNull(i + 1, 0);
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
/*      */   public synchronized boolean rowDeleted()
/*      */     throws SQLException
/*      */   {
/* 1336 */     throw new NotImplemented();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public synchronized boolean rowInserted()
/*      */     throws SQLException
/*      */   {
/* 1354 */     throw new NotImplemented();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public synchronized boolean rowUpdated()
/*      */     throws SQLException
/*      */   {
/* 1372 */     throw new NotImplemented();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected void setResultSetConcurrency(int concurrencyFlag)
/*      */   {
/* 1382 */     super.setResultSetConcurrency(concurrencyFlag);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   private byte[] stripBinaryPrefix(byte[] dataFrom)
/*      */   {
/* 1396 */     return StringUtils.stripEnclosure(dataFrom, "_binary'", "'");
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   synchronized void syncUpdate()
/*      */     throws SQLException
/*      */   {
/* 1407 */     if (this.updater == null) {
/* 1408 */       if (this.updateSQL == null) {
/* 1409 */         generateStatements();
/*      */       }
/*      */       
/* 1412 */       this.updater = this.connection.clientPrepareStatement(this.updateSQL);
/*      */     }
/*      */     
/*      */ 
/* 1416 */     int numFields = this.fields.length;
/* 1417 */     this.updater.clearParameters();
/*      */     
/* 1419 */     for (int i = 0; i < numFields; i++) {
/* 1420 */       if (this.thisRow[i] != null) {
/* 1421 */         this.updater.setBytes(i + 1, (byte[])this.thisRow[i], this.fields[i].isBinary(), false);
/*      */       }
/*      */       else {
/* 1424 */         this.updater.setNull(i + 1, 0);
/*      */       }
/*      */     }
/*      */     
/* 1428 */     int numKeys = this.primaryKeyIndicies.size();
/*      */     
/* 1430 */     if (numKeys == 1) {
/* 1431 */       int index = ((Integer)this.primaryKeyIndicies.get(0)).intValue();
/* 1432 */       byte[] keyData = (byte[])this.thisRow[index];
/* 1433 */       this.updater.setBytes(numFields + 1, keyData, false, false);
/*      */     } else {
/* 1435 */       for (int i = 0; i < numKeys; i++) {
/* 1436 */         byte[] currentVal = (byte[])this.thisRow[((Integer)this.primaryKeyIndicies.get(i)).intValue()];
/*      */         
/*      */ 
/* 1439 */         if (currentVal != null) {
/* 1440 */           this.updater.setBytes(numFields + i + 1, currentVal, false, false);
/*      */         }
/*      */         else {
/* 1443 */           this.updater.setNull(numFields + i + 1, 0);
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
/*      */   public synchronized void updateAsciiStream(int columnIndex, InputStream x, int length)
/*      */     throws SQLException
/*      */   {
/* 1468 */     if (!this.onInsertRow) {
/* 1469 */       if (!this.doingUpdates) {
/* 1470 */         this.doingUpdates = true;
/* 1471 */         syncUpdate();
/*      */       }
/*      */       
/* 1474 */       this.updater.setAsciiStream(columnIndex, x, length);
/*      */     } else {
/* 1476 */       this.inserter.setAsciiStream(columnIndex, x, length);
/* 1477 */       this.thisRow[(columnIndex - 1)] = STREAM_DATA_MARKER;
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
/*      */   public synchronized void updateAsciiStream(String columnName, InputStream x, int length)
/*      */     throws SQLException
/*      */   {
/* 1500 */     updateAsciiStream(findColumn(columnName), x, length);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public synchronized void updateBigDecimal(int columnIndex, BigDecimal x)
/*      */     throws SQLException
/*      */   {
/* 1519 */     if (!this.onInsertRow) {
/* 1520 */       if (!this.doingUpdates) {
/* 1521 */         this.doingUpdates = true;
/* 1522 */         syncUpdate();
/*      */       }
/*      */       
/* 1525 */       this.updater.setBigDecimal(columnIndex, x);
/*      */     } else {
/* 1527 */       this.inserter.setBigDecimal(columnIndex, x);
/*      */       
/* 1529 */       if (x == null) {
/* 1530 */         this.thisRow[(columnIndex - 1)] = null;
/*      */       } else {
/* 1532 */         this.thisRow[(columnIndex - 1)] = x.toString().getBytes();
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
/*      */   public synchronized void updateBigDecimal(String columnName, BigDecimal x)
/*      */     throws SQLException
/*      */   {
/* 1553 */     updateBigDecimal(findColumn(columnName), x);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public synchronized void updateBinaryStream(int columnIndex, InputStream x, int length)
/*      */     throws SQLException
/*      */   {
/* 1575 */     if (!this.onInsertRow) {
/* 1576 */       if (!this.doingUpdates) {
/* 1577 */         this.doingUpdates = true;
/* 1578 */         syncUpdate();
/*      */       }
/*      */       
/* 1581 */       this.updater.setBinaryStream(columnIndex, x, length);
/*      */     } else {
/* 1583 */       this.inserter.setBinaryStream(columnIndex, x, length);
/*      */       
/* 1585 */       if (x == null) {
/* 1586 */         this.thisRow[(columnIndex - 1)] = null;
/*      */       } else {
/* 1588 */         this.thisRow[(columnIndex - 1)] = STREAM_DATA_MARKER;
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
/*      */   public synchronized void updateBinaryStream(String columnName, InputStream x, int length)
/*      */     throws SQLException
/*      */   {
/* 1612 */     updateBinaryStream(findColumn(columnName), x, length);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public synchronized void updateBlob(int columnIndex, Blob blob)
/*      */     throws SQLException
/*      */   {
/* 1620 */     if (!this.onInsertRow) {
/* 1621 */       if (!this.doingUpdates) {
/* 1622 */         this.doingUpdates = true;
/* 1623 */         syncUpdate();
/*      */       }
/*      */       
/* 1626 */       this.updater.setBlob(columnIndex, blob);
/*      */     } else {
/* 1628 */       this.inserter.setBlob(columnIndex, blob);
/*      */       
/* 1630 */       if (blob == null) {
/* 1631 */         this.thisRow[(columnIndex - 1)] = null;
/*      */       } else {
/* 1633 */         this.thisRow[(columnIndex - 1)] = STREAM_DATA_MARKER;
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public synchronized void updateBlob(String columnName, Blob blob)
/*      */     throws SQLException
/*      */   {
/* 1643 */     updateBlob(findColumn(columnName), blob);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public synchronized void updateBoolean(int columnIndex, boolean x)
/*      */     throws SQLException
/*      */   {
/* 1662 */     if (!this.onInsertRow) {
/* 1663 */       if (!this.doingUpdates) {
/* 1664 */         this.doingUpdates = true;
/* 1665 */         syncUpdate();
/*      */       }
/*      */       
/* 1668 */       this.updater.setBoolean(columnIndex, x);
/*      */     } else {
/* 1670 */       this.inserter.setBoolean(columnIndex, x);
/*      */       
/* 1672 */       this.thisRow[(columnIndex - 1)] = this.inserter.getBytesRepresentation(columnIndex - 1);
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
/*      */   public synchronized void updateBoolean(String columnName, boolean x)
/*      */     throws SQLException
/*      */   {
/* 1693 */     updateBoolean(findColumn(columnName), x);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public synchronized void updateByte(int columnIndex, byte x)
/*      */     throws SQLException
/*      */   {
/* 1712 */     if (!this.onInsertRow) {
/* 1713 */       if (!this.doingUpdates) {
/* 1714 */         this.doingUpdates = true;
/* 1715 */         syncUpdate();
/*      */       }
/*      */       
/* 1718 */       this.updater.setByte(columnIndex, x);
/*      */     } else {
/* 1720 */       this.inserter.setByte(columnIndex, x);
/*      */       
/* 1722 */       this.thisRow[(columnIndex - 1)] = this.inserter.getBytesRepresentation(columnIndex - 1);
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
/*      */   public synchronized void updateByte(String columnName, byte x)
/*      */     throws SQLException
/*      */   {
/* 1743 */     updateByte(findColumn(columnName), x);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public synchronized void updateBytes(int columnIndex, byte[] x)
/*      */     throws SQLException
/*      */   {
/* 1762 */     if (!this.onInsertRow) {
/* 1763 */       if (!this.doingUpdates) {
/* 1764 */         this.doingUpdates = true;
/* 1765 */         syncUpdate();
/*      */       }
/*      */       
/* 1768 */       this.updater.setBytes(columnIndex, x);
/*      */     } else {
/* 1770 */       this.inserter.setBytes(columnIndex, x);
/*      */       
/* 1772 */       this.thisRow[(columnIndex - 1)] = x;
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
/*      */   public synchronized void updateBytes(String columnName, byte[] x)
/*      */     throws SQLException
/*      */   {
/* 1792 */     updateBytes(findColumn(columnName), x);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public synchronized void updateCharacterStream(int columnIndex, Reader x, int length)
/*      */     throws SQLException
/*      */   {
/* 1814 */     if (!this.onInsertRow) {
/* 1815 */       if (!this.doingUpdates) {
/* 1816 */         this.doingUpdates = true;
/* 1817 */         syncUpdate();
/*      */       }
/*      */       
/* 1820 */       this.updater.setCharacterStream(columnIndex, x, length);
/*      */     } else {
/* 1822 */       this.inserter.setCharacterStream(columnIndex, x, length);
/*      */       
/* 1824 */       if (x == null) {
/* 1825 */         this.thisRow[(columnIndex - 1)] = null;
/*      */       } else {
/* 1827 */         this.thisRow[(columnIndex - 1)] = STREAM_DATA_MARKER;
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
/*      */   public synchronized void updateCharacterStream(String columnName, Reader reader, int length)
/*      */     throws SQLException
/*      */   {
/* 1851 */     updateCharacterStream(findColumn(columnName), reader, length);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public void updateClob(int columnIndex, Clob clob)
/*      */     throws SQLException
/*      */   {
/* 1859 */     if (clob == null) {
/* 1860 */       updateNull(columnIndex);
/*      */     } else {
/* 1862 */       updateCharacterStream(columnIndex, clob.getCharacterStream(), (int)clob.length());
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
/*      */   public synchronized void updateDate(int columnIndex, Date x)
/*      */     throws SQLException
/*      */   {
/* 1883 */     if (!this.onInsertRow) {
/* 1884 */       if (!this.doingUpdates) {
/* 1885 */         this.doingUpdates = true;
/* 1886 */         syncUpdate();
/*      */       }
/*      */       
/* 1889 */       this.updater.setDate(columnIndex, x);
/*      */     } else {
/* 1891 */       this.inserter.setDate(columnIndex, x);
/*      */       
/* 1893 */       this.thisRow[(columnIndex - 1)] = this.inserter.getBytesRepresentation(columnIndex - 1);
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
/*      */   public synchronized void updateDate(String columnName, Date x)
/*      */     throws SQLException
/*      */   {
/* 1914 */     updateDate(findColumn(columnName), x);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public synchronized void updateDouble(int columnIndex, double x)
/*      */     throws SQLException
/*      */   {
/* 1933 */     if (!this.onInsertRow) {
/* 1934 */       if (!this.doingUpdates) {
/* 1935 */         this.doingUpdates = true;
/* 1936 */         syncUpdate();
/*      */       }
/*      */       
/* 1939 */       this.updater.setDouble(columnIndex, x);
/*      */     } else {
/* 1941 */       this.inserter.setDouble(columnIndex, x);
/*      */       
/* 1943 */       this.thisRow[(columnIndex - 1)] = this.inserter.getBytesRepresentation(columnIndex - 1);
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
/*      */   public synchronized void updateDouble(String columnName, double x)
/*      */     throws SQLException
/*      */   {
/* 1964 */     updateDouble(findColumn(columnName), x);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public synchronized void updateFloat(int columnIndex, float x)
/*      */     throws SQLException
/*      */   {
/* 1983 */     if (!this.onInsertRow) {
/* 1984 */       if (!this.doingUpdates) {
/* 1985 */         this.doingUpdates = true;
/* 1986 */         syncUpdate();
/*      */       }
/*      */       
/* 1989 */       this.updater.setFloat(columnIndex, x);
/*      */     } else {
/* 1991 */       this.inserter.setFloat(columnIndex, x);
/*      */       
/* 1993 */       this.thisRow[(columnIndex - 1)] = this.inserter.getBytesRepresentation(columnIndex - 1);
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
/*      */   public synchronized void updateFloat(String columnName, float x)
/*      */     throws SQLException
/*      */   {
/* 2014 */     updateFloat(findColumn(columnName), x);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public synchronized void updateInt(int columnIndex, int x)
/*      */     throws SQLException
/*      */   {
/* 2033 */     if (!this.onInsertRow) {
/* 2034 */       if (!this.doingUpdates) {
/* 2035 */         this.doingUpdates = true;
/* 2036 */         syncUpdate();
/*      */       }
/*      */       
/* 2039 */       this.updater.setInt(columnIndex, x);
/*      */     } else {
/* 2041 */       this.inserter.setInt(columnIndex, x);
/*      */       
/* 2043 */       this.thisRow[(columnIndex - 1)] = this.inserter.getBytesRepresentation(columnIndex - 1);
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
/*      */   public synchronized void updateInt(String columnName, int x)
/*      */     throws SQLException
/*      */   {
/* 2064 */     updateInt(findColumn(columnName), x);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public synchronized void updateLong(int columnIndex, long x)
/*      */     throws SQLException
/*      */   {
/* 2083 */     if (!this.onInsertRow) {
/* 2084 */       if (!this.doingUpdates) {
/* 2085 */         this.doingUpdates = true;
/* 2086 */         syncUpdate();
/*      */       }
/*      */       
/* 2089 */       this.updater.setLong(columnIndex, x);
/*      */     } else {
/* 2091 */       this.inserter.setLong(columnIndex, x);
/*      */       
/* 2093 */       this.thisRow[(columnIndex - 1)] = this.inserter.getBytesRepresentation(columnIndex - 1);
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
/*      */   public synchronized void updateLong(String columnName, long x)
/*      */     throws SQLException
/*      */   {
/* 2114 */     updateLong(findColumn(columnName), x);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public synchronized void updateNull(int columnIndex)
/*      */     throws SQLException
/*      */   {
/* 2130 */     if (!this.onInsertRow) {
/* 2131 */       if (!this.doingUpdates) {
/* 2132 */         this.doingUpdates = true;
/* 2133 */         syncUpdate();
/*      */       }
/*      */       
/* 2136 */       this.updater.setNull(columnIndex, 0);
/*      */     } else {
/* 2138 */       this.inserter.setNull(columnIndex, 0);
/*      */       
/* 2140 */       this.thisRow[(columnIndex - 1)] = null;
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
/*      */   public synchronized void updateNull(String columnName)
/*      */     throws SQLException
/*      */   {
/* 2157 */     updateNull(findColumn(columnName));
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public synchronized void updateObject(int columnIndex, Object x)
/*      */     throws SQLException
/*      */   {
/* 2176 */     if (!this.onInsertRow) {
/* 2177 */       if (!this.doingUpdates) {
/* 2178 */         this.doingUpdates = true;
/* 2179 */         syncUpdate();
/*      */       }
/*      */       
/* 2182 */       this.updater.setObject(columnIndex, x);
/*      */     } else {
/* 2184 */       this.inserter.setObject(columnIndex, x);
/*      */       
/* 2186 */       this.thisRow[(columnIndex - 1)] = this.inserter.getBytesRepresentation(columnIndex - 1);
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
/*      */   public synchronized void updateObject(int columnIndex, Object x, int scale)
/*      */     throws SQLException
/*      */   {
/* 2211 */     if (!this.onInsertRow) {
/* 2212 */       if (!this.doingUpdates) {
/* 2213 */         this.doingUpdates = true;
/* 2214 */         syncUpdate();
/*      */       }
/*      */       
/* 2217 */       this.updater.setObject(columnIndex, x);
/*      */     } else {
/* 2219 */       this.inserter.setObject(columnIndex, x);
/*      */       
/* 2221 */       this.thisRow[(columnIndex - 1)] = this.inserter.getBytesRepresentation(columnIndex - 1);
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
/*      */   public synchronized void updateObject(String columnName, Object x)
/*      */     throws SQLException
/*      */   {
/* 2242 */     updateObject(findColumn(columnName), x);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public synchronized void updateObject(String columnName, Object x, int scale)
/*      */     throws SQLException
/*      */   {
/* 2265 */     updateObject(findColumn(columnName), x);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public synchronized void updateRow()
/*      */     throws SQLException
/*      */   {
/* 2279 */     if (!this.isUpdatable) {
/* 2280 */       throw new NotUpdatable(this.notUpdatableReason);
/*      */     }
/*      */     
/* 2283 */     if (this.doingUpdates) {
/* 2284 */       this.updater.executeUpdate();
/* 2285 */       refreshRow();
/* 2286 */       this.doingUpdates = false;
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/* 2292 */     syncUpdate();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public synchronized void updateShort(int columnIndex, short x)
/*      */     throws SQLException
/*      */   {
/* 2311 */     if (!this.onInsertRow) {
/* 2312 */       if (!this.doingUpdates) {
/* 2313 */         this.doingUpdates = true;
/* 2314 */         syncUpdate();
/*      */       }
/*      */       
/* 2317 */       this.updater.setShort(columnIndex, x);
/*      */     } else {
/* 2319 */       this.inserter.setShort(columnIndex, x);
/*      */       
/* 2321 */       this.thisRow[(columnIndex - 1)] = this.inserter.getBytesRepresentation(columnIndex - 1);
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
/*      */   public synchronized void updateShort(String columnName, short x)
/*      */     throws SQLException
/*      */   {
/* 2342 */     updateShort(findColumn(columnName), x);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public synchronized void updateString(int columnIndex, String x)
/*      */     throws SQLException
/*      */   {
/* 2361 */     checkClosed();
/*      */     
/* 2363 */     if (!this.onInsertRow) {
/* 2364 */       if (!this.doingUpdates) {
/* 2365 */         this.doingUpdates = true;
/* 2366 */         syncUpdate();
/*      */       }
/*      */       
/* 2369 */       this.updater.setString(columnIndex, x);
/*      */     } else {
/* 2371 */       this.inserter.setString(columnIndex, x);
/*      */       
/* 2373 */       if (x == null) {
/* 2374 */         this.thisRow[(columnIndex - 1)] = null;
/*      */       }
/* 2376 */       else if (getCharConverter() != null) {
/* 2377 */         this.thisRow[(columnIndex - 1)] = StringUtils.getBytes(x, this.charConverter, this.charEncoding, this.connection.getServerCharacterEncoding(), this.connection.parserKnowsUnicode());
/*      */ 
/*      */       }
/*      */       else
/*      */       {
/* 2382 */         this.thisRow[(columnIndex - 1)] = x.getBytes();
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
/*      */   public synchronized void updateString(String columnName, String x)
/*      */     throws SQLException
/*      */   {
/* 2404 */     updateString(findColumn(columnName), x);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public synchronized void updateTime(int columnIndex, Time x)
/*      */     throws SQLException
/*      */   {
/* 2423 */     if (!this.onInsertRow) {
/* 2424 */       if (!this.doingUpdates) {
/* 2425 */         this.doingUpdates = true;
/* 2426 */         syncUpdate();
/*      */       }
/*      */       
/* 2429 */       this.updater.setTime(columnIndex, x);
/*      */     } else {
/* 2431 */       this.inserter.setTime(columnIndex, x);
/*      */       
/* 2433 */       this.thisRow[(columnIndex - 1)] = this.inserter.getBytesRepresentation(columnIndex - 1);
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
/*      */   public synchronized void updateTime(String columnName, Time x)
/*      */     throws SQLException
/*      */   {
/* 2454 */     updateTime(findColumn(columnName), x);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public synchronized void updateTimestamp(int columnIndex, Timestamp x)
/*      */     throws SQLException
/*      */   {
/* 2473 */     if (!this.onInsertRow) {
/* 2474 */       if (!this.doingUpdates) {
/* 2475 */         this.doingUpdates = true;
/* 2476 */         syncUpdate();
/*      */       }
/*      */       
/* 2479 */       this.updater.setTimestamp(columnIndex, x);
/*      */     } else {
/* 2481 */       this.inserter.setTimestamp(columnIndex, x);
/*      */       
/* 2483 */       this.thisRow[(columnIndex - 1)] = this.inserter.getBytesRepresentation(columnIndex - 1);
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
/*      */   public synchronized void updateTimestamp(String columnName, Timestamp x)
/*      */     throws SQLException
/*      */   {
/* 2504 */     updateTimestamp(findColumn(columnName), x);
/*      */   }
/*      */ }


/* Location:              E:\java\java学习\hutubill\lib\all.jar!\com\mysql\jdbc\UpdatableResultSet.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version:       0.7.1
 */
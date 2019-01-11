/*     */ package com.mysql.jdbc;
/*     */ 
/*     */ import java.io.BufferedInputStream;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.io.OutputStream;
/*     */ import java.sql.Blob;
/*     */ import java.sql.DatabaseMetaData;
/*     */ import java.sql.PreparedStatement;
/*     */ import java.sql.SQLException;
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class BlobFromLocator
/*     */   implements Blob
/*     */ {
/*  55 */   private List primaryKeyColumns = null;
/*     */   
/*  57 */   private List primaryKeyValues = null;
/*     */   
/*     */ 
/*     */   private ResultSet creatorResultSet;
/*     */   
/*  62 */   private String blobColumnName = null;
/*     */   
/*  64 */   private String tableName = null;
/*     */   
/*  66 */   private int numColsInResultSet = 0;
/*     */   
/*  68 */   private int numPrimaryKeys = 0;
/*     */   
/*     */ 
/*     */   private String quotedId;
/*     */   
/*     */ 
/*     */   BlobFromLocator(ResultSet creatorResultSetToSet, int blobColumnIndex)
/*     */     throws SQLException
/*     */   {
/*  77 */     this.creatorResultSet = creatorResultSetToSet;
/*     */     
/*  79 */     this.numColsInResultSet = this.creatorResultSet.fields.length;
/*  80 */     this.quotedId = this.creatorResultSet.connection.getMetaData().getIdentifierQuoteString();
/*     */     
/*     */ 
/*  83 */     if (this.numColsInResultSet > 1) {
/*  84 */       this.primaryKeyColumns = new ArrayList();
/*  85 */       this.primaryKeyValues = new ArrayList();
/*     */       
/*  87 */       for (int i = 0; i < this.numColsInResultSet; i++) {
/*  88 */         if (this.creatorResultSet.fields[i].isPrimaryKey()) {
/*  89 */           StringBuffer keyName = new StringBuffer();
/*  90 */           keyName.append(this.quotedId);
/*     */           
/*  92 */           String originalColumnName = this.creatorResultSet.fields[i].getOriginalName();
/*     */           
/*     */ 
/*  95 */           if ((originalColumnName != null) && (originalColumnName.length() > 0))
/*     */           {
/*  97 */             keyName.append(originalColumnName);
/*     */           } else {
/*  99 */             keyName.append(this.creatorResultSet.fields[i].getName());
/*     */           }
/*     */           
/*     */ 
/* 103 */           keyName.append(this.quotedId);
/*     */           
/* 105 */           this.primaryKeyColumns.add(keyName.toString());
/* 106 */           this.primaryKeyValues.add(this.creatorResultSet.getString(i + 1));
/*     */         }
/*     */       }
/*     */     }
/*     */     else {
/* 111 */       notEnoughInformationInQuery();
/*     */     }
/*     */     
/* 114 */     this.numPrimaryKeys = this.primaryKeyColumns.size();
/*     */     
/* 116 */     if (this.numPrimaryKeys == 0) {
/* 117 */       notEnoughInformationInQuery();
/*     */     }
/*     */     
/* 120 */     if (this.creatorResultSet.fields[0].getOriginalTableName() != null) {
/* 121 */       StringBuffer tableNameBuffer = new StringBuffer();
/*     */       
/* 123 */       String databaseName = this.creatorResultSet.fields[0].getDatabaseName();
/*     */       
/*     */ 
/* 126 */       if ((databaseName != null) && (databaseName.length() > 0)) {
/* 127 */         tableNameBuffer.append(this.quotedId);
/* 128 */         tableNameBuffer.append(databaseName);
/* 129 */         tableNameBuffer.append(this.quotedId);
/* 130 */         tableNameBuffer.append('.');
/*     */       }
/*     */       
/* 133 */       tableNameBuffer.append(this.quotedId);
/* 134 */       tableNameBuffer.append(this.creatorResultSet.fields[0].getOriginalTableName());
/*     */       
/* 136 */       tableNameBuffer.append(this.quotedId);
/*     */       
/* 138 */       this.tableName = tableNameBuffer.toString();
/*     */     } else {
/* 140 */       StringBuffer tableNameBuffer = new StringBuffer();
/*     */       
/* 142 */       tableNameBuffer.append(this.quotedId);
/* 143 */       tableNameBuffer.append(this.creatorResultSet.fields[0].getTableName());
/*     */       
/* 145 */       tableNameBuffer.append(this.quotedId);
/*     */       
/* 147 */       this.tableName = tableNameBuffer.toString();
/*     */     }
/*     */     
/* 150 */     this.blobColumnName = (this.quotedId + this.creatorResultSet.getString(blobColumnIndex) + this.quotedId);
/*     */   }
/*     */   
/*     */   private void notEnoughInformationInQuery() throws SQLException
/*     */   {
/* 155 */     throw SQLError.createSQLException("Emulated BLOB locators must come from a ResultSet with only one table selected, and all primary keys selected", "S1000");
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public OutputStream setBinaryStream(long indexToWriteAt)
/*     */     throws SQLException
/*     */   {
/* 165 */     throw new NotImplemented();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public InputStream getBinaryStream()
/*     */     throws SQLException
/*     */   {
/* 178 */     return new BufferedInputStream(new LocatorInputStream(), this.creatorResultSet.connection.getLocatorFetchBufferSize());
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public int setBytes(long writeAt, byte[] bytes, int offset, int length)
/*     */     throws SQLException
/*     */   {
/* 187 */     PreparedStatement pStmt = null;
/*     */     
/* 189 */     if (offset + length > bytes.length) {
/* 190 */       length = bytes.length - offset;
/*     */     }
/*     */     
/* 193 */     byte[] bytesToWrite = new byte[length];
/* 194 */     System.arraycopy(bytes, offset, bytesToWrite, 0, length);
/*     */     
/*     */ 
/* 197 */     StringBuffer query = new StringBuffer("UPDATE ");
/* 198 */     query.append(this.tableName);
/* 199 */     query.append(" SET ");
/* 200 */     query.append(this.blobColumnName);
/* 201 */     query.append(" = INSERT(");
/* 202 */     query.append(this.blobColumnName);
/* 203 */     query.append(", ");
/* 204 */     query.append(writeAt);
/* 205 */     query.append(", ");
/* 206 */     query.append(length);
/* 207 */     query.append(", ?) WHERE ");
/*     */     
/* 209 */     query.append((String)this.primaryKeyColumns.get(0));
/* 210 */     query.append(" = ?");
/*     */     
/* 212 */     for (int i = 1; i < this.numPrimaryKeys; i++) {
/* 213 */       query.append(" AND ");
/* 214 */       query.append((String)this.primaryKeyColumns.get(i));
/* 215 */       query.append(" = ?");
/*     */     }
/*     */     
/*     */     try
/*     */     {
/* 220 */       pStmt = this.creatorResultSet.connection.prepareStatement(query.toString());
/*     */       
/*     */ 
/* 223 */       pStmt.setBytes(1, bytesToWrite);
/*     */       
/* 225 */       for (int i = 0; i < this.numPrimaryKeys; i++) {
/* 226 */         pStmt.setString(i + 2, (String)this.primaryKeyValues.get(i));
/*     */       }
/*     */       
/* 229 */       int rowsUpdated = pStmt.executeUpdate();
/*     */       
/* 231 */       if (rowsUpdated != 1) {
/* 232 */         throw SQLError.createSQLException("BLOB data not found! Did primary keys change?", "S1000");
/*     */       }
/*     */     }
/*     */     finally
/*     */     {
/* 237 */       if (pStmt != null) {
/*     */         try {
/* 239 */           pStmt.close();
/*     */         }
/*     */         catch (SQLException sqlEx) {}
/*     */         
/*     */ 
/* 244 */         pStmt = null;
/*     */       }
/*     */     }
/*     */     
/* 248 */     return (int)length();
/*     */   }
/*     */   
/*     */ 
/*     */   public int setBytes(long writeAt, byte[] bytes)
/*     */     throws SQLException
/*     */   {
/* 255 */     return setBytes(writeAt, bytes, 0, bytes.length);
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
/*     */   public byte[] getBytes(long pos, int length)
/*     */     throws SQLException
/*     */   {
/* 274 */     PreparedStatement pStmt = null;
/*     */     
/*     */     try
/*     */     {
/* 278 */       pStmt = createGetBytesStatement();
/*     */       
/* 280 */       return getBytesInternal(pStmt, pos, length);
/*     */     } finally {
/* 282 */       if (pStmt != null) {
/*     */         try {
/* 284 */           pStmt.close();
/*     */         }
/*     */         catch (SQLException sqlEx) {}
/*     */         
/*     */ 
/* 289 */         pStmt = null;
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public long length()
/*     */     throws SQLException
/*     */   {
/* 304 */     java.sql.ResultSet blobRs = null;
/* 305 */     PreparedStatement pStmt = null;
/*     */     
/*     */ 
/* 308 */     StringBuffer query = new StringBuffer("SELECT LENGTH(");
/* 309 */     query.append(this.blobColumnName);
/* 310 */     query.append(") FROM ");
/* 311 */     query.append(this.tableName);
/* 312 */     query.append(" WHERE ");
/*     */     
/* 314 */     query.append((String)this.primaryKeyColumns.get(0));
/* 315 */     query.append(" = ?");
/*     */     
/* 317 */     for (int i = 1; i < this.numPrimaryKeys; i++) {
/* 318 */       query.append(" AND ");
/* 319 */       query.append((String)this.primaryKeyColumns.get(i));
/* 320 */       query.append(" = ?");
/*     */     }
/*     */     
/*     */     try
/*     */     {
/* 325 */       pStmt = this.creatorResultSet.connection.prepareStatement(query.toString());
/*     */       
/*     */ 
/* 328 */       for (int i = 0; i < this.numPrimaryKeys; i++) {
/* 329 */         pStmt.setString(i + 1, (String)this.primaryKeyValues.get(i));
/*     */       }
/*     */       
/* 332 */       blobRs = pStmt.executeQuery();
/*     */       
/* 334 */       if (blobRs.next()) {
/* 335 */         return blobRs.getLong(1);
/*     */       }
/*     */       
/* 338 */       throw SQLError.createSQLException("BLOB data not found! Did primary keys change?", "S1000");
/*     */     }
/*     */     finally
/*     */     {
/* 342 */       if (blobRs != null) {
/*     */         try {
/* 344 */           blobRs.close();
/*     */         }
/*     */         catch (SQLException sqlEx) {}
/*     */         
/*     */ 
/* 349 */         blobRs = null;
/*     */       }
/*     */       
/* 352 */       if (pStmt != null) {
/*     */         try {
/* 354 */           pStmt.close();
/*     */         }
/*     */         catch (SQLException sqlEx) {}
/*     */         
/*     */ 
/* 359 */         pStmt = null;
/*     */       }
/*     */     }
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
/*     */   public long position(Blob pattern, long start)
/*     */     throws SQLException
/*     */   {
/* 379 */     return position(pattern.getBytes(0L, (int)pattern.length()), start);
/*     */   }
/*     */   
/*     */ 
/*     */   public long position(byte[] pattern, long start)
/*     */     throws SQLException
/*     */   {
/* 386 */     java.sql.ResultSet blobRs = null;
/* 387 */     PreparedStatement pStmt = null;
/*     */     
/*     */ 
/* 390 */     StringBuffer query = new StringBuffer("SELECT LOCATE(");
/* 391 */     query.append("?, ");
/* 392 */     query.append(this.blobColumnName);
/* 393 */     query.append(", ");
/* 394 */     query.append(start);
/* 395 */     query.append(") FROM ");
/* 396 */     query.append(this.tableName);
/* 397 */     query.append(" WHERE ");
/*     */     
/* 399 */     query.append((String)this.primaryKeyColumns.get(0));
/* 400 */     query.append(" = ?");
/*     */     
/* 402 */     for (int i = 1; i < this.numPrimaryKeys; i++) {
/* 403 */       query.append(" AND ");
/* 404 */       query.append((String)this.primaryKeyColumns.get(i));
/* 405 */       query.append(" = ?");
/*     */     }
/*     */     
/*     */     try
/*     */     {
/* 410 */       pStmt = this.creatorResultSet.connection.prepareStatement(query.toString());
/*     */       
/* 412 */       pStmt.setBytes(1, pattern);
/*     */       
/* 414 */       for (int i = 0; i < this.numPrimaryKeys; i++) {
/* 415 */         pStmt.setString(i + 2, (String)this.primaryKeyValues.get(i));
/*     */       }
/*     */       
/* 418 */       blobRs = pStmt.executeQuery();
/*     */       
/* 420 */       if (blobRs.next()) {
/* 421 */         return blobRs.getLong(1);
/*     */       }
/*     */       
/* 424 */       throw SQLError.createSQLException("BLOB data not found! Did primary keys change?", "S1000");
/*     */     }
/*     */     finally
/*     */     {
/* 428 */       if (blobRs != null) {
/*     */         try {
/* 430 */           blobRs.close();
/*     */         }
/*     */         catch (SQLException sqlEx) {}
/*     */         
/*     */ 
/* 435 */         blobRs = null;
/*     */       }
/*     */       
/* 438 */       if (pStmt != null) {
/*     */         try {
/* 440 */           pStmt.close();
/*     */         }
/*     */         catch (SQLException sqlEx) {}
/*     */         
/*     */ 
/* 445 */         pStmt = null;
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */   public void truncate(long length)
/*     */     throws SQLException
/*     */   {
/* 454 */     PreparedStatement pStmt = null;
/*     */     
/*     */ 
/* 457 */     StringBuffer query = new StringBuffer("UPDATE ");
/* 458 */     query.append(this.tableName);
/* 459 */     query.append(" SET ");
/* 460 */     query.append(this.blobColumnName);
/* 461 */     query.append(" = LEFT(");
/* 462 */     query.append(this.blobColumnName);
/* 463 */     query.append(", ");
/* 464 */     query.append(length);
/* 465 */     query.append(") WHERE ");
/*     */     
/* 467 */     query.append((String)this.primaryKeyColumns.get(0));
/* 468 */     query.append(" = ?");
/*     */     
/* 470 */     for (int i = 1; i < this.numPrimaryKeys; i++) {
/* 471 */       query.append(" AND ");
/* 472 */       query.append((String)this.primaryKeyColumns.get(i));
/* 473 */       query.append(" = ?");
/*     */     }
/*     */     
/*     */     try
/*     */     {
/* 478 */       pStmt = this.creatorResultSet.connection.prepareStatement(query.toString());
/*     */       
/*     */ 
/* 481 */       for (int i = 0; i < this.numPrimaryKeys; i++) {
/* 482 */         pStmt.setString(i + 1, (String)this.primaryKeyValues.get(i));
/*     */       }
/*     */       
/* 485 */       int rowsUpdated = pStmt.executeUpdate();
/*     */       
/* 487 */       if (rowsUpdated != 1) {
/* 488 */         throw SQLError.createSQLException("BLOB data not found! Did primary keys change?", "S1000");
/*     */       }
/*     */     }
/*     */     finally
/*     */     {
/* 493 */       if (pStmt != null) {
/*     */         try {
/* 495 */           pStmt.close();
/*     */         }
/*     */         catch (SQLException sqlEx) {}
/*     */         
/*     */ 
/* 500 */         pStmt = null;
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   PreparedStatement createGetBytesStatement() throws SQLException {
/* 506 */     StringBuffer query = new StringBuffer("SELECT SUBSTRING(");
/*     */     
/* 508 */     query.append(this.blobColumnName);
/* 509 */     query.append(", ");
/* 510 */     query.append("?");
/* 511 */     query.append(", ");
/* 512 */     query.append("?");
/* 513 */     query.append(") FROM ");
/* 514 */     query.append(this.tableName);
/* 515 */     query.append(" WHERE ");
/*     */     
/* 517 */     query.append((String)this.primaryKeyColumns.get(0));
/* 518 */     query.append(" = ?");
/*     */     
/* 520 */     for (int i = 1; i < this.numPrimaryKeys; i++) {
/* 521 */       query.append(" AND ");
/* 522 */       query.append((String)this.primaryKeyColumns.get(i));
/* 523 */       query.append(" = ?");
/*     */     }
/*     */     
/* 526 */     return this.creatorResultSet.connection.prepareStatement(query.toString());
/*     */   }
/*     */   
/*     */ 
/*     */   byte[] getBytesInternal(PreparedStatement pStmt, long pos, int length)
/*     */     throws SQLException
/*     */   {
/* 533 */     java.sql.ResultSet blobRs = null;
/*     */     
/*     */     try
/*     */     {
/* 537 */       pStmt.setLong(1, pos);
/* 538 */       pStmt.setInt(2, length);
/*     */       
/* 540 */       for (int i = 0; i < this.numPrimaryKeys; i++) {
/* 541 */         pStmt.setString(i + 3, (String)this.primaryKeyValues.get(i));
/*     */       }
/*     */       
/* 544 */       blobRs = pStmt.executeQuery();
/*     */       
/* 546 */       if (blobRs.next()) {
/* 547 */         return ((ResultSet)blobRs).getBytes(1, true);
/*     */       }
/*     */       
/* 550 */       throw SQLError.createSQLException("BLOB data not found! Did primary keys change?", "S1000");
/*     */     }
/*     */     finally
/*     */     {
/* 554 */       if (blobRs != null) {
/*     */         try {
/* 556 */           blobRs.close();
/*     */         }
/*     */         catch (SQLException sqlEx) {}
/*     */         
/*     */ 
/* 561 */         blobRs = null;
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   class LocatorInputStream extends InputStream {
/* 567 */     long currentPositionInBlob = 0L;
/*     */     
/* 569 */     long length = 0L;
/*     */     
/* 571 */     PreparedStatement pStmt = null;
/*     */     
/*     */     LocatorInputStream() throws SQLException {
/* 574 */       this.length = BlobFromLocator.this.length();
/* 575 */       this.pStmt = BlobFromLocator.this.createGetBytesStatement();
/*     */     }
/*     */     
/*     */     public int read() throws IOException {
/* 579 */       if (this.currentPositionInBlob + 1L > this.length) {
/* 580 */         return -1;
/*     */       }
/*     */       try
/*     */       {
/* 584 */         byte[] asBytes = BlobFromLocator.this.getBytesInternal(this.pStmt, this.currentPositionInBlob++ + 1L, 1);
/*     */         
/*     */ 
/* 587 */         if (asBytes == null) {
/* 588 */           return -1;
/*     */         }
/*     */         
/* 591 */         return asBytes[0];
/*     */       } catch (SQLException sqlEx) {
/* 593 */         throw new IOException(sqlEx.toString());
/*     */       }
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */     public int read(byte[] b, int off, int len)
/*     */       throws IOException
/*     */     {
/* 603 */       if (this.currentPositionInBlob + 1L > this.length) {
/* 604 */         return -1;
/*     */       }
/*     */       try
/*     */       {
/* 608 */         byte[] asBytes = BlobFromLocator.this.getBytesInternal(this.pStmt, this.currentPositionInBlob + 1L, len);
/*     */         
/*     */ 
/* 611 */         if (asBytes == null) {
/* 612 */           return -1;
/*     */         }
/*     */         
/* 615 */         System.arraycopy(asBytes, 0, b, off, asBytes.length);
/*     */         
/* 617 */         this.currentPositionInBlob += asBytes.length;
/*     */         
/* 619 */         return asBytes.length;
/*     */       } catch (SQLException sqlEx) {
/* 621 */         throw new IOException(sqlEx.toString());
/*     */       }
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */     public int read(byte[] b)
/*     */       throws IOException
/*     */     {
/* 631 */       if (this.currentPositionInBlob + 1L > this.length) {
/* 632 */         return -1;
/*     */       }
/*     */       try
/*     */       {
/* 636 */         byte[] asBytes = BlobFromLocator.this.getBytesInternal(this.pStmt, this.currentPositionInBlob + 1L, b.length);
/*     */         
/*     */ 
/* 639 */         if (asBytes == null) {
/* 640 */           return -1;
/*     */         }
/*     */         
/* 643 */         System.arraycopy(asBytes, 0, b, 0, asBytes.length);
/*     */         
/* 645 */         this.currentPositionInBlob += asBytes.length;
/*     */         
/* 647 */         return asBytes.length;
/*     */       } catch (SQLException sqlEx) {
/* 649 */         throw new IOException(sqlEx.toString());
/*     */       }
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */     public void close()
/*     */       throws IOException
/*     */     {
/* 659 */       if (this.pStmt != null) {
/*     */         try {
/* 661 */           this.pStmt.close();
/*     */         } catch (SQLException sqlEx) {
/* 663 */           throw new IOException(sqlEx.toString());
/*     */         }
/*     */       }
/*     */       
/* 667 */       super.close();
/*     */     }
/*     */   }
/*     */ }


/* Location:              E:\java\java学习\hutubill\lib\all.jar!\com\mysql\jdbc\BlobFromLocator.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version:       0.7.1
 */
/*     */ package com.mysql.jdbc;
/*     */ 
/*     */ import java.io.ByteArrayInputStream;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.io.OutputStream;
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
/*     */ public class Blob
/*     */   implements java.sql.Blob, OutputStreamWatcher
/*     */ {
/*  59 */   private byte[] binaryData = null;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   Blob(byte[] data)
/*     */   {
/*  68 */     setBinaryData(data);
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
/*     */   Blob(byte[] data, ResultSet creatorResultSetToSet, int columnIndexToSet)
/*     */   {
/*  82 */     setBinaryData(data);
/*     */   }
/*     */   
/*     */   private byte[] getBinaryData() {
/*  86 */     return this.binaryData;
/*     */   }
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
/*  98 */     return new ByteArrayInputStream(getBinaryData());
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
/* 117 */     if (pos < 1L) {
/* 118 */       throw SQLError.createSQLException(Messages.getString("Blob.2"), "S1009");
/*     */     }
/*     */     
/*     */ 
/* 122 */     byte[] newData = new byte[length];
/* 123 */     System.arraycopy(getBinaryData(), (int)(pos - 1L), newData, 0, length);
/*     */     
/* 125 */     return newData;
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
/* 138 */     return getBinaryData().length;
/*     */   }
/*     */   
/*     */ 
/*     */   public long position(byte[] pattern, long start)
/*     */     throws SQLException
/*     */   {
/* 145 */     throw SQLError.createSQLException("Not implemented");
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
/*     */   public long position(java.sql.Blob pattern, long start)
/*     */     throws SQLException
/*     */   {
/* 163 */     return position(pattern.getBytes(0L, (int)pattern.length()), start);
/*     */   }
/*     */   
/*     */   private void setBinaryData(byte[] newBinaryData) {
/* 167 */     this.binaryData = newBinaryData;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public OutputStream setBinaryStream(long indexToWriteAt)
/*     */     throws SQLException
/*     */   {
/* 175 */     if (indexToWriteAt < 1L) {
/* 176 */       throw SQLError.createSQLException(Messages.getString("Blob.0"), "S1009");
/*     */     }
/*     */     
/*     */ 
/* 180 */     WatchableOutputStream bytesOut = new WatchableOutputStream();
/* 181 */     bytesOut.setWatcher(this);
/*     */     
/* 183 */     if (indexToWriteAt > 0L) {
/* 184 */       bytesOut.write(this.binaryData, 0, (int)(indexToWriteAt - 1L));
/*     */     }
/*     */     
/* 187 */     return bytesOut;
/*     */   }
/*     */   
/*     */ 
/*     */   public int setBytes(long writeAt, byte[] bytes)
/*     */     throws SQLException
/*     */   {
/* 194 */     return setBytes(writeAt, bytes, 0, bytes.length);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public int setBytes(long writeAt, byte[] bytes, int offset, int length)
/*     */     throws SQLException
/*     */   {
/* 202 */     OutputStream bytesOut = setBinaryStream(writeAt);
/*     */     try
/*     */     {
/* 205 */       bytesOut.write(bytes, offset, length);
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
/* 217 */       return length;
/*     */     }
/*     */     catch (IOException ioEx)
/*     */     {
/* 207 */       throw SQLError.createSQLException(Messages.getString("Blob.1"), "S1000");
/*     */     }
/*     */     finally {
/*     */       try {
/* 211 */         bytesOut.close();
/*     */       }
/*     */       catch (IOException doNothing) {}
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void streamClosed(byte[] byteData)
/*     */   {
/* 224 */     this.binaryData = byteData;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public void streamClosed(WatchableOutputStream out)
/*     */   {
/* 231 */     int streamSize = out.size();
/*     */     
/* 233 */     if (streamSize < this.binaryData.length) {
/* 234 */       out.write(this.binaryData, streamSize, this.binaryData.length - streamSize);
/*     */     }
/*     */     
/*     */ 
/* 238 */     this.binaryData = out.toByteArray();
/*     */   }
/*     */   
/*     */ 
/*     */   public void truncate(long arg0)
/*     */     throws SQLException
/*     */   {
/* 245 */     throw new NotImplemented();
/*     */   }
/*     */ }


/* Location:              E:\java\java学习\hutubill\lib\all.jar!\com\mysql\jdbc\Blob.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version:       0.7.1
 */
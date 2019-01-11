/*     */ package com.mysql.jdbc;
/*     */ 
/*     */ import java.io.ByteArrayInputStream;
/*     */ import java.io.InputStream;
/*     */ import java.io.OutputStream;
/*     */ import java.io.Reader;
/*     */ import java.io.StringReader;
/*     */ import java.io.Writer;
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
/*     */ public class Clob
/*     */   implements java.sql.Clob, OutputStreamWatcher, WriterWatcher
/*     */ {
/*     */   private String charData;
/*     */   
/*     */   Clob(String charDataInit)
/*     */   {
/*  46 */     this.charData = charDataInit;
/*     */   }
/*     */   
/*     */ 
/*     */   public InputStream getAsciiStream()
/*     */     throws SQLException
/*     */   {
/*  53 */     if (this.charData != null) {
/*  54 */       return new ByteArrayInputStream(this.charData.getBytes());
/*     */     }
/*     */     
/*  57 */     return null;
/*     */   }
/*     */   
/*     */ 
/*     */   public Reader getCharacterStream()
/*     */     throws SQLException
/*     */   {
/*  64 */     if (this.charData != null) {
/*  65 */       return new StringReader(this.charData);
/*     */     }
/*     */     
/*  68 */     return null;
/*     */   }
/*     */   
/*     */ 
/*     */   public String getSubString(long startPos, int length)
/*     */     throws SQLException
/*     */   {
/*  75 */     if (startPos < 1L) {
/*  76 */       throw SQLError.createSQLException(Messages.getString("Clob.6"), "S1009");
/*     */     }
/*     */     
/*     */ 
/*  80 */     int adjustedStartPos = (int)startPos - 1;
/*  81 */     int adjustedEndIndex = adjustedStartPos + length;
/*     */     
/*  83 */     if (this.charData != null) {
/*  84 */       if (adjustedEndIndex > this.charData.length()) {
/*  85 */         throw SQLError.createSQLException(Messages.getString("Clob.7"), "S1009");
/*     */       }
/*     */       
/*     */ 
/*  89 */       return this.charData.substring(adjustedStartPos, adjustedEndIndex);
/*     */     }
/*     */     
/*     */ 
/*  93 */     return null;
/*     */   }
/*     */   
/*     */ 
/*     */   public long length()
/*     */     throws SQLException
/*     */   {
/* 100 */     if (this.charData != null) {
/* 101 */       return this.charData.length();
/*     */     }
/*     */     
/* 104 */     return 0L;
/*     */   }
/*     */   
/*     */ 
/*     */   public long position(java.sql.Clob arg0, long arg1)
/*     */     throws SQLException
/*     */   {
/* 111 */     return position(arg0.getSubString(0L, (int)arg0.length()), arg1);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public long position(String stringToFind, long startPos)
/*     */     throws SQLException
/*     */   {
/* 119 */     if (startPos < 1L) {
/* 120 */       throw SQLError.createSQLException(Messages.getString("Clob.8") + startPos + Messages.getString("Clob.9"), "S1009");
/*     */     }
/*     */     
/*     */ 
/*     */ 
/* 125 */     if (this.charData != null) {
/* 126 */       if (startPos - 1L > this.charData.length()) {
/* 127 */         throw SQLError.createSQLException(Messages.getString("Clob.10"), "S1009");
/*     */       }
/*     */       
/*     */ 
/* 131 */       int pos = this.charData.indexOf(stringToFind, (int)(startPos - 1L));
/*     */       
/* 133 */       return pos == -1 ? -1L : pos + 1;
/*     */     }
/*     */     
/* 136 */     return -1L;
/*     */   }
/*     */   
/*     */ 
/*     */   public OutputStream setAsciiStream(long indexToWriteAt)
/*     */     throws SQLException
/*     */   {
/* 143 */     if (indexToWriteAt < 1L) {
/* 144 */       throw SQLError.createSQLException(Messages.getString("Clob.0"), "S1009");
/*     */     }
/*     */     
/*     */ 
/* 148 */     WatchableOutputStream bytesOut = new WatchableOutputStream();
/* 149 */     bytesOut.setWatcher(this);
/*     */     
/* 151 */     if (indexToWriteAt > 0L) {
/* 152 */       bytesOut.write(this.charData.getBytes(), 0, (int)(indexToWriteAt - 1L));
/*     */     }
/*     */     
/*     */ 
/* 156 */     return bytesOut;
/*     */   }
/*     */   
/*     */ 
/*     */   public Writer setCharacterStream(long indexToWriteAt)
/*     */     throws SQLException
/*     */   {
/* 163 */     if (indexToWriteAt < 1L) {
/* 164 */       throw SQLError.createSQLException(Messages.getString("Clob.1"), "S1009");
/*     */     }
/*     */     
/*     */ 
/* 168 */     WatchableWriter writer = new WatchableWriter();
/* 169 */     writer.setWatcher(this);
/*     */     
/*     */ 
/*     */ 
/*     */ 
/* 174 */     if (indexToWriteAt > 1L) {
/* 175 */       writer.write(this.charData, 0, (int)(indexToWriteAt - 1L));
/*     */     }
/*     */     
/* 178 */     return writer;
/*     */   }
/*     */   
/*     */ 
/*     */   public int setString(long pos, String str)
/*     */     throws SQLException
/*     */   {
/* 185 */     if (pos < 1L) {
/* 186 */       throw SQLError.createSQLException(Messages.getString("Clob.2"), "S1009");
/*     */     }
/*     */     
/*     */ 
/* 190 */     if (str == null) {
/* 191 */       throw SQLError.createSQLException(Messages.getString("Clob.3"), "S1009");
/*     */     }
/*     */     
/*     */ 
/* 195 */     StringBuffer charBuf = new StringBuffer(this.charData);
/*     */     
/* 197 */     pos -= 1L;
/*     */     
/* 199 */     int strLength = str.length();
/*     */     
/* 201 */     charBuf.replace((int)pos, (int)(pos + strLength), str);
/*     */     
/* 203 */     this.charData = charBuf.toString();
/*     */     
/* 205 */     return strLength;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public int setString(long pos, String str, int offset, int len)
/*     */     throws SQLException
/*     */   {
/* 213 */     if (pos < 1L) {
/* 214 */       throw SQLError.createSQLException(Messages.getString("Clob.4"), "S1009");
/*     */     }
/*     */     
/*     */ 
/* 218 */     if (str == null) {
/* 219 */       throw SQLError.createSQLException(Messages.getString("Clob.5"), "S1009");
/*     */     }
/*     */     
/*     */ 
/* 223 */     StringBuffer charBuf = new StringBuffer(this.charData);
/*     */     
/* 225 */     pos -= 1L;
/*     */     
/* 227 */     String replaceString = str.substring(offset, len);
/*     */     
/* 229 */     charBuf.replace((int)pos, (int)(pos + replaceString.length()), replaceString);
/*     */     
/*     */ 
/* 232 */     this.charData = charBuf.toString();
/*     */     
/* 234 */     return len;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public void streamClosed(WatchableOutputStream out)
/*     */   {
/* 241 */     int streamSize = out.size();
/*     */     
/* 243 */     if (streamSize < this.charData.length()) {
/*     */       try {
/* 245 */         out.write(StringUtils.getBytes(this.charData, null, null, false, null), streamSize, this.charData.length() - streamSize);
/*     */       }
/*     */       catch (SQLException ex) {}
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/* 253 */     this.charData = StringUtils.toAsciiString(out.toByteArray());
/*     */   }
/*     */   
/*     */ 
/*     */   public void truncate(long length)
/*     */     throws SQLException
/*     */   {
/* 260 */     if (length > this.charData.length()) {
/* 261 */       throw SQLError.createSQLException(Messages.getString("Clob.11") + this.charData.length() + Messages.getString("Clob.12") + length + Messages.getString("Clob.13"));
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/* 267 */     this.charData = this.charData.substring(0, (int)length);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public void writerClosed(char[] charDataBeingWritten)
/*     */   {
/* 274 */     this.charData = new String(charDataBeingWritten);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public void writerClosed(WatchableWriter out)
/*     */   {
/* 281 */     int dataLength = out.size();
/*     */     
/* 283 */     if (dataLength < this.charData.length()) {
/* 284 */       out.write(this.charData, dataLength, this.charData.length() - dataLength);
/*     */     }
/*     */     
/*     */ 
/* 288 */     this.charData = out.toString();
/*     */   }
/*     */ }


/* Location:              E:\java\java学习\hutubill\lib\all.jar!\com\mysql\jdbc\Clob.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version:       0.7.1
 */
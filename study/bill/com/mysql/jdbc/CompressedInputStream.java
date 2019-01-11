/*     */ package com.mysql.jdbc;
/*     */ 
/*     */ import com.mysql.jdbc.log.Log;
/*     */ import java.io.EOFException;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.sql.SQLException;
/*     */ import java.util.zip.DataFormatException;
/*     */ import java.util.zip.Inflater;
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
/*     */ class CompressedInputStream
/*     */   extends InputStream
/*     */ {
/*     */   private byte[] buffer;
/*     */   private Connection connection;
/*     */   private InputStream in;
/*     */   private Inflater inflater;
/*  61 */   private byte[] packetHeaderBuffer = new byte[7];
/*     */   
/*     */ 
/*  64 */   private int pos = 0;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public CompressedInputStream(Connection conn, InputStream streamFromServer)
/*     */   {
/*  75 */     this.connection = conn;
/*  76 */     this.in = streamFromServer;
/*  77 */     this.inflater = new Inflater();
/*     */   }
/*     */   
/*     */ 
/*     */   public int available()
/*     */     throws IOException
/*     */   {
/*  84 */     if (this.buffer == null) {
/*  85 */       return this.in.available();
/*     */     }
/*     */     
/*  88 */     return this.buffer.length - this.pos + this.in.available();
/*     */   }
/*     */   
/*     */ 
/*     */   public void close()
/*     */     throws IOException
/*     */   {
/*  95 */     this.in.close();
/*  96 */     this.buffer = null;
/*  97 */     this.inflater = null;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private void getNextPacketFromServer()
/*     */     throws IOException
/*     */   {
/* 108 */     byte[] uncompressedData = null;
/*     */     
/* 110 */     int lengthRead = readFully(this.packetHeaderBuffer, 0, 7);
/*     */     
/* 112 */     if (lengthRead < 7) {
/* 113 */       throw new IOException("Unexpected end of input stream");
/*     */     }
/*     */     
/* 116 */     int compressedPacketLength = (this.packetHeaderBuffer[0] & 0xFF) + ((this.packetHeaderBuffer[1] & 0xFF) << 8) + ((this.packetHeaderBuffer[2] & 0xFF) << 16);
/*     */     
/*     */ 
/*     */ 
/* 120 */     int uncompressedLength = (this.packetHeaderBuffer[4] & 0xFF) + ((this.packetHeaderBuffer[5] & 0xFF) << 8) + ((this.packetHeaderBuffer[6] & 0xFF) << 16);
/*     */     
/*     */ 
/*     */ 
/* 124 */     if (this.connection.getTraceProtocol()) {
/*     */       try {
/* 126 */         this.connection.getLog().logTrace("Reading compressed packet of length " + compressedPacketLength + " uncompressed to " + uncompressedLength);
/*     */ 
/*     */       }
/*     */       catch (SQLException sqlEx)
/*     */       {
/* 131 */         throw new IOException(sqlEx.toString());
/*     */       }
/*     */     }
/*     */     
/*     */ 
/* 136 */     if (uncompressedLength > 0) {
/* 137 */       uncompressedData = new byte[uncompressedLength];
/*     */       
/* 139 */       byte[] compressedBuffer = new byte[compressedPacketLength];
/*     */       
/* 141 */       readFully(compressedBuffer, 0, compressedPacketLength);
/*     */       try
/*     */       {
/* 144 */         this.inflater.reset();
/*     */       } catch (NullPointerException npe) {
/* 146 */         this.inflater = new Inflater();
/*     */       }
/*     */       
/* 149 */       this.inflater.setInput(compressedBuffer);
/*     */       try
/*     */       {
/* 152 */         this.inflater.inflate(uncompressedData);
/*     */       } catch (DataFormatException dfe) {
/* 154 */         throw new IOException("Error while uncompressing packet from server.");
/*     */       }
/*     */       
/*     */ 
/* 158 */       this.inflater.end();
/*     */     } else {
/* 160 */       if (this.connection.getTraceProtocol()) {
/*     */         try {
/* 162 */           this.connection.getLog().logTrace("Packet didn't meet compression threshold, not uncompressing...");
/*     */ 
/*     */         }
/*     */         catch (SQLException sqlEx)
/*     */         {
/* 167 */           throw new IOException(sqlEx.toString());
/*     */         }
/*     */       }
/*     */       
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 176 */       uncompressedData = new byte[compressedPacketLength];
/* 177 */       readFully(uncompressedData, 0, compressedPacketLength);
/*     */     }
/*     */     
/* 180 */     if (this.connection.getTraceProtocol()) {
/*     */       try {
/* 182 */         this.connection.getLog().logTrace("Uncompressed packet: \n" + StringUtils.dumpAsHex(uncompressedData, compressedPacketLength));
/*     */ 
/*     */       }
/*     */       catch (SQLException sqlEx)
/*     */       {
/* 187 */         throw new IOException(sqlEx.toString());
/*     */       }
/*     */     }
/*     */     
/*     */ 
/* 192 */     if ((this.buffer != null) && (this.pos < this.buffer.length)) {
/* 193 */       if (this.connection.getTraceProtocol()) {
/*     */         try {
/* 195 */           this.connection.getLog().logTrace("Combining remaining packet with new: ");
/*     */         }
/*     */         catch (SQLException sqlEx) {
/* 198 */           throw new IOException(sqlEx.toString());
/*     */         }
/*     */       }
/*     */       
/*     */ 
/* 203 */       int remaining = this.buffer.length - this.pos;
/* 204 */       byte[] newBuffer = new byte[remaining + uncompressedData.length];
/*     */       
/* 206 */       int newIndex = 0;
/*     */       
/* 208 */       for (int i = this.pos; i < this.buffer.length; i++) {
/* 209 */         newBuffer[(newIndex++)] = this.buffer[i];
/*     */       }
/* 211 */       System.arraycopy(uncompressedData, 0, newBuffer, newIndex, uncompressedData.length);
/*     */       
/*     */ 
/* 214 */       uncompressedData = newBuffer;
/*     */     }
/*     */     
/* 217 */     this.pos = 0;
/* 218 */     this.buffer = uncompressedData;
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
/*     */   private void getNextPacketIfRequired(int numBytes)
/*     */     throws IOException
/*     */   {
/* 234 */     if ((this.buffer == null) || (this.pos + numBytes > this.buffer.length))
/*     */     {
/* 236 */       getNextPacketFromServer();
/*     */     }
/*     */   }
/*     */   
/*     */   public int read()
/*     */     throws IOException
/*     */   {
/*     */     try
/*     */     {
/* 245 */       getNextPacketIfRequired(1);
/*     */     } catch (IOException ioEx) {
/* 247 */       return -1;
/*     */     }
/*     */     
/* 250 */     return this.buffer[(this.pos++)] & 0xFF;
/*     */   }
/*     */   
/*     */ 
/*     */   public int read(byte[] b)
/*     */     throws IOException
/*     */   {
/* 257 */     return read(b, 0, b.length);
/*     */   }
/*     */   
/*     */ 
/*     */   public int read(byte[] b, int off, int len)
/*     */     throws IOException
/*     */   {
/* 264 */     if (b == null)
/* 265 */       throw new NullPointerException();
/* 266 */     if ((off < 0) || (off > b.length) || (len < 0) || (off + len > b.length) || (off + len < 0))
/*     */     {
/* 268 */       throw new IndexOutOfBoundsException();
/*     */     }
/*     */     
/* 271 */     if (len <= 0) {
/* 272 */       return 0;
/*     */     }
/*     */     try
/*     */     {
/* 276 */       getNextPacketIfRequired(len);
/*     */     } catch (IOException ioEx) {
/* 278 */       return -1;
/*     */     }
/*     */     
/* 281 */     System.arraycopy(this.buffer, this.pos, b, off, len);
/* 282 */     this.pos += len;
/*     */     
/* 284 */     return len;
/*     */   }
/*     */   
/*     */   private final int readFully(byte[] b, int off, int len) throws IOException {
/* 288 */     if (len < 0) {
/* 289 */       throw new IndexOutOfBoundsException();
/*     */     }
/*     */     
/* 292 */     int n = 0;
/*     */     
/* 294 */     while (n < len) {
/* 295 */       int count = this.in.read(b, off + n, len - n);
/*     */       
/* 297 */       if (count < 0) {
/* 298 */         throw new EOFException();
/*     */       }
/*     */       
/* 301 */       n += count;
/*     */     }
/*     */     
/* 304 */     return n;
/*     */   }
/*     */   
/*     */ 
/*     */   public long skip(long n)
/*     */     throws IOException
/*     */   {
/* 311 */     long count = 0L;
/*     */     
/* 313 */     for (long i = 0L; i < n; i += 1L) {
/* 314 */       int bytesRead = read();
/*     */       
/* 316 */       if (bytesRead == -1) {
/*     */         break;
/*     */       }
/*     */       
/* 320 */       count += 1L;
/*     */     }
/*     */     
/* 323 */     return count;
/*     */   }
/*     */ }


/* Location:              E:\java\java学习\hutubill\lib\all.jar!\com\mysql\jdbc\CompressedInputStream.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version:       0.7.1
 */
/*     */ package com.mysql.jdbc.util;
/*     */ 
/*     */ import com.mysql.jdbc.log.Log;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.io.PrintStream;
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
/*     */ public class ReadAheadInputStream
/*     */   extends InputStream
/*     */ {
/*     */   private static final int DEFAULT_BUFFER_SIZE = 4096;
/*     */   private InputStream underlyingStream;
/*     */   private byte[] buf;
/*     */   protected int endOfCurrentData;
/*     */   protected int currentPosition;
/*  53 */   protected boolean doDebug = false;
/*     */   protected Log log;
/*     */   
/*     */   private void fill(int readAtLeastTheseManyBytes) throws IOException
/*     */   {
/*  58 */     checkClosed();
/*     */     
/*  60 */     this.currentPosition = 0;
/*     */     
/*  62 */     this.endOfCurrentData = this.currentPosition;
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*  68 */     int bytesToRead = Math.min(this.buf.length - this.currentPosition, readAtLeastTheseManyBytes);
/*     */     
/*     */ 
/*  71 */     int bytesAvailable = this.underlyingStream.available();
/*     */     
/*  73 */     if (bytesAvailable > bytesToRead)
/*     */     {
/*     */ 
/*     */ 
/*     */ 
/*  78 */       bytesToRead = Math.min(this.buf.length - this.currentPosition, bytesAvailable);
/*     */     }
/*     */     
/*     */ 
/*  82 */     if (this.doDebug) {
/*  83 */       StringBuffer debugBuf = new StringBuffer();
/*  84 */       debugBuf.append("  ReadAheadInputStream.fill(");
/*  85 */       debugBuf.append(readAtLeastTheseManyBytes);
/*  86 */       debugBuf.append("), buffer_size=");
/*  87 */       debugBuf.append(this.buf.length);
/*  88 */       debugBuf.append(", current_position=");
/*  89 */       debugBuf.append(this.currentPosition);
/*  90 */       debugBuf.append(", need to read ");
/*  91 */       debugBuf.append(Math.min(this.buf.length - this.currentPosition, readAtLeastTheseManyBytes));
/*     */       
/*  93 */       debugBuf.append(" bytes to fill request,");
/*     */       
/*  95 */       if (bytesAvailable > 0) {
/*  96 */         debugBuf.append(" underlying InputStream reports ");
/*  97 */         debugBuf.append(bytesAvailable);
/*     */         
/*  99 */         debugBuf.append(" total bytes available,");
/*     */       }
/*     */       
/* 102 */       debugBuf.append(" attempting to read ");
/* 103 */       debugBuf.append(bytesToRead);
/* 104 */       debugBuf.append(" bytes.");
/*     */       
/* 106 */       if (this.log != null) {
/* 107 */         this.log.logTrace(debugBuf.toString());
/*     */       } else {
/* 109 */         System.err.println(debugBuf.toString());
/*     */       }
/*     */     }
/*     */     
/* 113 */     int n = this.underlyingStream.read(this.buf, this.currentPosition, bytesToRead);
/*     */     
/*     */ 
/* 116 */     if (n > 0) {
/* 117 */       this.endOfCurrentData = (n + this.currentPosition);
/*     */     }
/*     */   }
/*     */   
/*     */   private int readFromUnderlyingStreamIfNecessary(byte[] b, int off, int len) throws IOException
/*     */   {
/* 123 */     checkClosed();
/*     */     
/* 125 */     int avail = this.endOfCurrentData - this.currentPosition;
/*     */     
/* 127 */     if (this.doDebug) {
/* 128 */       StringBuffer debugBuf = new StringBuffer();
/* 129 */       debugBuf.append("ReadAheadInputStream.readIfNecessary(");
/* 130 */       debugBuf.append(b);
/* 131 */       debugBuf.append(",");
/* 132 */       debugBuf.append(off);
/* 133 */       debugBuf.append(",");
/* 134 */       debugBuf.append(len);
/* 135 */       debugBuf.append(")");
/*     */       
/* 137 */       if (avail <= 0) {
/* 138 */         debugBuf.append(" not all data available in buffer, must read from stream");
/*     */         
/*     */ 
/* 141 */         if (len >= this.buf.length) {
/* 142 */           debugBuf.append(", amount requested > buffer, returning direct read() from stream");
/*     */         }
/*     */       }
/*     */       
/*     */ 
/* 147 */       if (this.log != null) {
/* 148 */         this.log.logTrace(debugBuf.toString());
/*     */       } else {
/* 150 */         System.err.println(debugBuf.toString());
/*     */       }
/*     */     }
/*     */     
/* 154 */     if (avail <= 0)
/*     */     {
/* 156 */       if (len >= this.buf.length) {
/* 157 */         return this.underlyingStream.read(b, off, len);
/*     */       }
/*     */       
/* 160 */       fill(len);
/*     */       
/* 162 */       avail = this.endOfCurrentData - this.currentPosition;
/*     */       
/* 164 */       if (avail <= 0) {
/* 165 */         return -1;
/*     */       }
/*     */     }
/* 168 */     int bytesActuallyRead = avail < len ? avail : len;
/*     */     
/* 170 */     System.arraycopy(this.buf, this.currentPosition, b, off, bytesActuallyRead);
/*     */     
/* 172 */     this.currentPosition += bytesActuallyRead;
/*     */     
/* 174 */     return bytesActuallyRead;
/*     */   }
/*     */   
/*     */   public synchronized int read(byte[] b, int off, int len) throws IOException {
/* 178 */     checkClosed();
/* 179 */     if ((off | len | off + len | b.length - (off + len)) < 0)
/* 180 */       throw new IndexOutOfBoundsException();
/* 181 */     if (len == 0) {
/* 182 */       return 0;
/*     */     }
/*     */     
/* 185 */     int totalBytesRead = 0;
/*     */     for (;;)
/*     */     {
/* 188 */       int bytesReadThisRound = readFromUnderlyingStreamIfNecessary(b, off + totalBytesRead, len - totalBytesRead);
/*     */       
/*     */ 
/*     */ 
/* 192 */       if (bytesReadThisRound <= 0) {
/* 193 */         if (totalBytesRead == 0) {
/* 194 */           totalBytesRead = bytesReadThisRound;
/*     */         }
/*     */         
/*     */       }
/*     */       else
/*     */       {
/* 200 */         totalBytesRead += bytesReadThisRound;
/*     */         
/*     */ 
/* 203 */         if (totalBytesRead < len)
/*     */         {
/*     */ 
/*     */ 
/*     */ 
/* 208 */           if (this.underlyingStream.available() <= 0)
/*     */             break;
/*     */         }
/*     */       }
/*     */     }
/* 213 */     return totalBytesRead;
/*     */   }
/*     */   
/*     */   public int read() throws IOException {
/* 217 */     checkClosed();
/*     */     
/* 219 */     if (this.currentPosition >= this.endOfCurrentData) {
/* 220 */       fill(1);
/* 221 */       if (this.currentPosition >= this.endOfCurrentData) {
/* 222 */         return -1;
/*     */       }
/*     */     }
/* 225 */     return this.buf[(this.currentPosition++)] & 0xFF;
/*     */   }
/*     */   
/*     */   public int available() throws IOException {
/* 229 */     checkClosed();
/*     */     
/* 231 */     return this.underlyingStream.available() + (this.endOfCurrentData - this.currentPosition);
/*     */   }
/*     */   
/*     */   private void checkClosed()
/*     */     throws IOException
/*     */   {
/* 237 */     if (this.buf == null) {
/* 238 */       throw new IOException("Stream closed");
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public ReadAheadInputStream(InputStream toBuffer, boolean debug, Log logTo)
/*     */   {
/* 246 */     this(toBuffer, 4096, debug, logTo);
/*     */   }
/*     */   
/*     */ 
/*     */   public ReadAheadInputStream(InputStream toBuffer, int bufferSize, boolean debug, Log logTo)
/*     */   {
/* 252 */     this.underlyingStream = toBuffer;
/* 253 */     this.buf = new byte[bufferSize];
/* 254 */     this.doDebug = debug;
/* 255 */     this.log = logTo;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void close()
/*     */     throws IOException
/*     */   {
/* 264 */     if (this.underlyingStream != null) {
/*     */       try {
/* 266 */         this.underlyingStream.close();
/*     */       } finally {
/* 268 */         this.underlyingStream = null;
/* 269 */         this.buf = null;
/* 270 */         this.log = null;
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean markSupported()
/*     */   {
/* 281 */     return false;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public long skip(long n)
/*     */     throws IOException
/*     */   {
/* 290 */     checkClosed();
/* 291 */     if (n <= 0L) {
/* 292 */       return 0L;
/*     */     }
/*     */     
/* 295 */     long bytesAvailInBuffer = this.endOfCurrentData - this.currentPosition;
/*     */     
/* 297 */     if (bytesAvailInBuffer <= 0L)
/*     */     {
/* 299 */       fill((int)n);
/* 300 */       bytesAvailInBuffer = this.endOfCurrentData - this.currentPosition;
/* 301 */       if (bytesAvailInBuffer <= 0L) {
/* 302 */         return 0L;
/*     */       }
/*     */     }
/* 305 */     long bytesSkipped = bytesAvailInBuffer < n ? bytesAvailInBuffer : n;
/* 306 */     this.currentPosition = ((int)(this.currentPosition + bytesSkipped));
/* 307 */     return bytesSkipped;
/*     */   }
/*     */ }


/* Location:              E:\java\java学习\hutubill\lib\all.jar!\com\mysql\jdbc\util\ReadAheadInputStream.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version:       0.7.1
 */
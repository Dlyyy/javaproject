/*     */ package com.mysql.jdbc;
/*     */ 
/*     */ import java.io.PrintStream;
/*     */ import java.io.UnsupportedEncodingException;
/*     */ import java.nio.ByteBuffer;
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
/*     */ class Buffer
/*     */ {
/*     */   static final int MAX_BYTES_TO_DUMP = 512;
/*     */   static final int NO_LENGTH_LIMIT = -1;
/*     */   static final long NULL_LENGTH = -1L;
/*  46 */   private int bufLength = 0;
/*     */   
/*     */   private byte[] byteBuffer;
/*     */   
/*  50 */   private int position = 0;
/*     */   
/*  52 */   protected boolean wasMultiPacket = false;
/*     */   
/*     */   Buffer(byte[] buf) {
/*  55 */     this.byteBuffer = buf;
/*  56 */     setBufLength(buf.length);
/*     */   }
/*     */   
/*     */   Buffer(int size) {
/*  60 */     this.byteBuffer = new byte[size];
/*  61 */     setBufLength(this.byteBuffer.length);
/*  62 */     this.position = 4;
/*     */   }
/*     */   
/*     */   final void clear() {
/*  66 */     this.position = 4;
/*     */   }
/*     */   
/*     */   final void dump() {
/*  70 */     dump(getBufLength());
/*     */   }
/*     */   
/*     */   final String dump(int numBytes) {
/*  74 */     return StringUtils.dumpAsHex(getBytes(0, numBytes > getBufLength() ? getBufLength() : numBytes), numBytes > getBufLength() ? getBufLength() : numBytes);
/*     */   }
/*     */   
/*     */ 
/*     */   final String dumpClampedBytes(int numBytes)
/*     */   {
/*  80 */     int numBytesToDump = numBytes < 512 ? numBytes : 512;
/*     */     
/*     */ 
/*  83 */     String dumped = StringUtils.dumpAsHex(getBytes(0, numBytesToDump > getBufLength() ? getBufLength() : numBytesToDump), numBytesToDump > getBufLength() ? getBufLength() : numBytesToDump);
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*  89 */     if (numBytesToDump < numBytes) {
/*  90 */       return dumped + " ....(packet exceeds max. dump length)";
/*     */     }
/*     */     
/*  93 */     return dumped;
/*     */   }
/*     */   
/*     */   final void dumpHeader() {
/*  97 */     for (int i = 0; i < 4; i++) {
/*  98 */       String hexVal = Integer.toHexString(readByte(i) & 0xFF);
/*     */       
/* 100 */       if (hexVal.length() == 1) {
/* 101 */         hexVal = "0" + hexVal;
/*     */       }
/*     */       
/* 104 */       System.out.print(hexVal + " ");
/*     */     }
/*     */   }
/*     */   
/*     */   final void dumpNBytes(int start, int nBytes) {
/* 109 */     StringBuffer asciiBuf = new StringBuffer();
/*     */     
/* 111 */     for (int i = start; (i < start + nBytes) && (i < getBufLength()); i++) {
/* 112 */       String hexVal = Integer.toHexString(readByte(i) & 0xFF);
/*     */       
/* 114 */       if (hexVal.length() == 1) {
/* 115 */         hexVal = "0" + hexVal;
/*     */       }
/*     */       
/* 118 */       System.out.print(hexVal + " ");
/*     */       
/* 120 */       if ((readByte(i) > 32) && (readByte(i) < Byte.MAX_VALUE)) {
/* 121 */         asciiBuf.append((char)readByte(i));
/*     */       } else {
/* 123 */         asciiBuf.append(".");
/*     */       }
/*     */       
/* 126 */       asciiBuf.append(" ");
/*     */     }
/*     */     
/* 129 */     System.out.println("    " + asciiBuf.toString());
/*     */   }
/*     */   
/*     */   final void ensureCapacity(int additionalData) throws SQLException {
/* 133 */     if (this.position + additionalData > getBufLength()) {
/* 134 */       if (this.position + additionalData < this.byteBuffer.length)
/*     */       {
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 140 */         setBufLength(this.byteBuffer.length);
/*     */ 
/*     */       }
/*     */       else
/*     */       {
/*     */ 
/* 146 */         int newLength = (int)(this.byteBuffer.length * 1.25D);
/*     */         
/* 148 */         if (newLength < this.byteBuffer.length + additionalData) {
/* 149 */           newLength = this.byteBuffer.length + (int)(additionalData * 1.25D);
/*     */         }
/*     */         
/*     */ 
/* 153 */         if (newLength < this.byteBuffer.length) {
/* 154 */           newLength = this.byteBuffer.length + additionalData;
/*     */         }
/*     */         
/* 157 */         byte[] newBytes = new byte[newLength];
/*     */         
/* 159 */         System.arraycopy(this.byteBuffer, 0, newBytes, 0, this.byteBuffer.length);
/*     */         
/* 161 */         this.byteBuffer = newBytes;
/* 162 */         setBufLength(this.byteBuffer.length);
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public int fastSkipLenString()
/*     */   {
/* 173 */     long len = readFieldLength();
/*     */     
/* 175 */     this.position = ((int)(this.position + len));
/*     */     
/* 177 */     return (int)len;
/*     */   }
/*     */   
/*     */   protected final byte[] getBufferSource() {
/* 181 */     return this.byteBuffer;
/*     */   }
/*     */   
/*     */   int getBufLength() {
/* 185 */     return this.bufLength;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public byte[] getByteBuffer()
/*     */   {
/* 194 */     return this.byteBuffer;
/*     */   }
/*     */   
/*     */   final byte[] getBytes(int len) {
/* 198 */     byte[] b = new byte[len];
/* 199 */     System.arraycopy(this.byteBuffer, this.position, b, 0, len);
/* 200 */     this.position += len;
/*     */     
/* 202 */     return b;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   byte[] getBytes(int offset, int len)
/*     */   {
/* 211 */     byte[] dest = new byte[len];
/* 212 */     System.arraycopy(this.byteBuffer, offset, dest, 0, len);
/*     */     
/* 214 */     return dest;
/*     */   }
/*     */   
/*     */   int getCapacity() {
/* 218 */     return this.byteBuffer.length;
/*     */   }
/*     */   
/*     */   public ByteBuffer getNioBuffer() {
/* 222 */     throw new IllegalArgumentException(Messages.getString("ByteArrayBuffer.0"));
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public int getPosition()
/*     */   {
/* 232 */     return this.position;
/*     */   }
/*     */   
/*     */   final boolean isLastDataPacket()
/*     */   {
/* 237 */     return (getBufLength() < 9) && ((this.byteBuffer[0] & 0xFF) == 254);
/*     */   }
/*     */   
/*     */   final long newReadLength() {
/* 241 */     int sw = this.byteBuffer[(this.position++)] & 0xFF;
/*     */     
/* 243 */     switch (sw) {
/*     */     case 251: 
/* 245 */       return 0L;
/*     */     
/*     */     case 252: 
/* 248 */       return readInt();
/*     */     
/*     */     case 253: 
/* 251 */       return readLongInt();
/*     */     
/*     */     case 254: 
/* 254 */       return readLongLong();
/*     */     }
/*     */     
/* 257 */     return sw;
/*     */   }
/*     */   
/*     */   final byte readByte()
/*     */   {
/* 262 */     return this.byteBuffer[(this.position++)];
/*     */   }
/*     */   
/*     */   final byte readByte(int readAt) {
/* 266 */     return this.byteBuffer[readAt];
/*     */   }
/*     */   
/*     */   final long readFieldLength() {
/* 270 */     int sw = this.byteBuffer[(this.position++)] & 0xFF;
/*     */     
/* 272 */     switch (sw) {
/*     */     case 251: 
/* 274 */       return -1L;
/*     */     
/*     */     case 252: 
/* 277 */       return readInt();
/*     */     
/*     */     case 253: 
/* 280 */       return readLongInt();
/*     */     
/*     */     case 254: 
/* 283 */       return readLongLong();
/*     */     }
/*     */     
/* 286 */     return sw;
/*     */   }
/*     */   
/*     */ 
/*     */   final int readInt()
/*     */   {
/* 292 */     byte[] b = this.byteBuffer;
/*     */     
/* 294 */     return b[(this.position++)] & 0xFF | (b[(this.position++)] & 0xFF) << 8;
/*     */   }
/*     */   
/*     */   final int readIntAsLong() {
/* 298 */     byte[] b = this.byteBuffer;
/*     */     
/* 300 */     return b[(this.position++)] & 0xFF | (b[(this.position++)] & 0xFF) << 8 | (b[(this.position++)] & 0xFF) << 16 | (b[(this.position++)] & 0xFF) << 24;
/*     */   }
/*     */   
/*     */ 
/*     */   final byte[] readLenByteArray(int offset)
/*     */   {
/* 306 */     long len = readFieldLength();
/*     */     
/* 308 */     if (len == -1L) {
/* 309 */       return null;
/*     */     }
/*     */     
/* 312 */     if (len == 0L) {
/* 313 */       return Constants.EMPTY_BYTE_ARRAY;
/*     */     }
/*     */     
/* 316 */     this.position += offset;
/*     */     
/* 318 */     return getBytes((int)len);
/*     */   }
/*     */   
/*     */   final long readLength() {
/* 322 */     int sw = this.byteBuffer[(this.position++)] & 0xFF;
/*     */     
/* 324 */     switch (sw) {
/*     */     case 251: 
/* 326 */       return 0L;
/*     */     
/*     */     case 252: 
/* 329 */       return readInt();
/*     */     
/*     */     case 253: 
/* 332 */       return readLongInt();
/*     */     
/*     */     case 254: 
/* 335 */       return readLong();
/*     */     }
/*     */     
/* 338 */     return sw;
/*     */   }
/*     */   
/*     */ 
/*     */   final long readLong()
/*     */   {
/* 344 */     byte[] b = this.byteBuffer;
/*     */     
/* 346 */     return b[(this.position++)] & 0xFF | (b[(this.position++)] & 0xFF) << 8 | (b[(this.position++)] & 0xFF) << 16 | (b[(this.position++)] & 0xFF) << 24;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   final int readLongInt()
/*     */   {
/* 354 */     byte[] b = this.byteBuffer;
/*     */     
/* 356 */     return b[(this.position++)] & 0xFF | (b[(this.position++)] & 0xFF) << 8 | (b[(this.position++)] & 0xFF) << 16;
/*     */   }
/*     */   
/*     */ 
/*     */   final long readLongLong()
/*     */   {
/* 362 */     byte[] b = this.byteBuffer;
/*     */     
/* 364 */     return b[(this.position++)] & 0xFF | (b[(this.position++)] & 0xFF) << 8 | (b[(this.position++)] & 0xFF) << 16 | (b[(this.position++)] & 0xFF) << 24 | (b[(this.position++)] & 0xFF) << 32 | (b[(this.position++)] & 0xFF) << 40 | (b[(this.position++)] & 0xFF) << 48 | (b[(this.position++)] & 0xFF) << 56;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   final int readnBytes()
/*     */   {
/* 375 */     int sw = this.byteBuffer[(this.position++)] & 0xFF;
/*     */     
/* 377 */     switch (sw) {
/*     */     case 1: 
/* 379 */       return this.byteBuffer[(this.position++)] & 0xFF;
/*     */     
/*     */     case 2: 
/* 382 */       return readInt();
/*     */     
/*     */     case 3: 
/* 385 */       return readLongInt();
/*     */     
/*     */     case 4: 
/* 388 */       return (int)readLong();
/*     */     }
/*     */     
/* 391 */     return 255;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   final String readString()
/*     */   {
/* 402 */     int i = this.position;
/* 403 */     int len = 0;
/* 404 */     int maxLen = getBufLength();
/*     */     
/* 406 */     while ((i < maxLen) && (this.byteBuffer[i] != 0)) {
/* 407 */       len++;
/* 408 */       i++;
/*     */     }
/*     */     
/* 411 */     String s = new String(this.byteBuffer, this.position, len);
/* 412 */     this.position += len + 1;
/*     */     
/* 414 */     return s;
/*     */   }
/*     */   
/*     */   final String readString(String encoding) throws SQLException {
/* 418 */     int i = this.position;
/* 419 */     int len = 0;
/* 420 */     int maxLen = getBufLength();
/*     */     
/* 422 */     while ((i < maxLen) && (this.byteBuffer[i] != 0)) {
/* 423 */       len++;
/* 424 */       i++;
/*     */     }
/*     */     try
/*     */     {
/* 428 */       return new String(this.byteBuffer, this.position, len, encoding);
/*     */     } catch (UnsupportedEncodingException uEE) {
/* 430 */       throw SQLError.createSQLException(Messages.getString("ByteArrayBuffer.1") + encoding + "'", "S1009");
/*     */     }
/*     */     finally {
/* 433 */       this.position += len + 1;
/*     */     }
/*     */   }
/*     */   
/*     */   void setBufLength(int bufLengthToSet) {
/* 438 */     this.bufLength = bufLengthToSet;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setByteBuffer(byte[] byteBufferToSet)
/*     */   {
/* 448 */     this.byteBuffer = byteBufferToSet;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setPosition(int positionToSet)
/*     */   {
/* 458 */     this.position = positionToSet;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setWasMultiPacket(boolean flag)
/*     */   {
/* 468 */     this.wasMultiPacket = flag;
/*     */   }
/*     */   
/*     */   public String toString() {
/* 472 */     return dumpClampedBytes(getPosition());
/*     */   }
/*     */   
/*     */   public String toSuperString() {
/* 476 */     return super.toString();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean wasMultiPacket()
/*     */   {
/* 485 */     return this.wasMultiPacket;
/*     */   }
/*     */   
/*     */   final void writeByte(byte b) throws SQLException {
/* 489 */     ensureCapacity(1);
/*     */     
/* 491 */     this.byteBuffer[(this.position++)] = b;
/*     */   }
/*     */   
/*     */   final void writeBytesNoNull(byte[] bytes) throws SQLException
/*     */   {
/* 496 */     int len = bytes.length;
/* 497 */     ensureCapacity(len);
/* 498 */     System.arraycopy(bytes, 0, this.byteBuffer, this.position, len);
/* 499 */     this.position += len;
/*     */   }
/*     */   
/*     */   final void writeBytesNoNull(byte[] bytes, int offset, int length)
/*     */     throws SQLException
/*     */   {
/* 505 */     ensureCapacity(length);
/* 506 */     System.arraycopy(bytes, offset, this.byteBuffer, this.position, length);
/* 507 */     this.position += length;
/*     */   }
/*     */   
/*     */   final void writeDouble(double d) throws SQLException {
/* 511 */     long l = Double.doubleToLongBits(d);
/* 512 */     writeLongLong(l);
/*     */   }
/*     */   
/*     */   final void writeFieldLength(long length) throws SQLException {
/* 516 */     if (length < 251L) {
/* 517 */       writeByte((byte)(int)length);
/* 518 */     } else if (length < 65536L) {
/* 519 */       ensureCapacity(3);
/* 520 */       writeByte((byte)-4);
/* 521 */       writeInt((int)length);
/* 522 */     } else if (length < 16777216L) {
/* 523 */       ensureCapacity(4);
/* 524 */       writeByte((byte)-3);
/* 525 */       writeLongInt((int)length);
/*     */     } else {
/* 527 */       ensureCapacity(9);
/* 528 */       writeByte((byte)-2);
/* 529 */       writeLongLong(length);
/*     */     }
/*     */   }
/*     */   
/*     */   final void writeFloat(float f) throws SQLException {
/* 534 */     ensureCapacity(4);
/*     */     
/* 536 */     int i = Float.floatToIntBits(f);
/* 537 */     byte[] b = this.byteBuffer;
/* 538 */     b[(this.position++)] = ((byte)(i & 0xFF));
/* 539 */     b[(this.position++)] = ((byte)(i >>> 8));
/* 540 */     b[(this.position++)] = ((byte)(i >>> 16));
/* 541 */     b[(this.position++)] = ((byte)(i >>> 24));
/*     */   }
/*     */   
/*     */   final void writeInt(int i) throws SQLException
/*     */   {
/* 546 */     ensureCapacity(2);
/*     */     
/* 548 */     byte[] b = this.byteBuffer;
/* 549 */     b[(this.position++)] = ((byte)(i & 0xFF));
/* 550 */     b[(this.position++)] = ((byte)(i >>> 8));
/*     */   }
/*     */   
/*     */   final void writeLenBytes(byte[] b)
/*     */     throws SQLException
/*     */   {
/* 556 */     int len = b.length;
/* 557 */     ensureCapacity(len + 9);
/* 558 */     writeFieldLength(len);
/* 559 */     System.arraycopy(b, 0, this.byteBuffer, this.position, len);
/* 560 */     this.position += len;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   final void writeLenString(String s, String encoding, String serverEncoding, SingleByteCharsetConverter converter, boolean parserKnowsUnicode, Connection conn)
/*     */     throws UnsupportedEncodingException, SQLException
/*     */   {
/* 569 */     byte[] b = null;
/*     */     
/* 571 */     if (converter != null) {
/* 572 */       b = converter.toBytes(s);
/*     */     } else {
/* 574 */       b = StringUtils.getBytes(s, encoding, serverEncoding, parserKnowsUnicode, conn);
/*     */     }
/*     */     
/*     */ 
/* 578 */     int len = b.length;
/* 579 */     ensureCapacity(len + 9);
/* 580 */     writeFieldLength(len);
/* 581 */     System.arraycopy(b, 0, this.byteBuffer, this.position, len);
/* 582 */     this.position += len;
/*     */   }
/*     */   
/*     */   final void writeLong(long i) throws SQLException
/*     */   {
/* 587 */     ensureCapacity(4);
/*     */     
/* 589 */     byte[] b = this.byteBuffer;
/* 590 */     b[(this.position++)] = ((byte)(int)(i & 0xFF));
/* 591 */     b[(this.position++)] = ((byte)(int)(i >>> 8));
/* 592 */     b[(this.position++)] = ((byte)(int)(i >>> 16));
/* 593 */     b[(this.position++)] = ((byte)(int)(i >>> 24));
/*     */   }
/*     */   
/*     */   final void writeLongInt(int i) throws SQLException
/*     */   {
/* 598 */     ensureCapacity(3);
/* 599 */     byte[] b = this.byteBuffer;
/* 600 */     b[(this.position++)] = ((byte)(i & 0xFF));
/* 601 */     b[(this.position++)] = ((byte)(i >>> 8));
/* 602 */     b[(this.position++)] = ((byte)(i >>> 16));
/*     */   }
/*     */   
/*     */   final void writeLongLong(long i) throws SQLException {
/* 606 */     ensureCapacity(8);
/* 607 */     byte[] b = this.byteBuffer;
/* 608 */     b[(this.position++)] = ((byte)(int)(i & 0xFF));
/* 609 */     b[(this.position++)] = ((byte)(int)(i >>> 8));
/* 610 */     b[(this.position++)] = ((byte)(int)(i >>> 16));
/* 611 */     b[(this.position++)] = ((byte)(int)(i >>> 24));
/* 612 */     b[(this.position++)] = ((byte)(int)(i >>> 32));
/* 613 */     b[(this.position++)] = ((byte)(int)(i >>> 40));
/* 614 */     b[(this.position++)] = ((byte)(int)(i >>> 48));
/* 615 */     b[(this.position++)] = ((byte)(int)(i >>> 56));
/*     */   }
/*     */   
/*     */   final void writeString(String s) throws SQLException
/*     */   {
/* 620 */     ensureCapacity(s.length() * 2 + 1);
/* 621 */     writeStringNoNull(s);
/* 622 */     this.byteBuffer[(this.position++)] = 0;
/*     */   }
/*     */   
/*     */   final void writeString(String s, String encoding, Connection conn) throws SQLException
/*     */   {
/* 627 */     ensureCapacity(s.length() * 2 + 1);
/*     */     try {
/* 629 */       writeStringNoNull(s, encoding, encoding, false, conn);
/*     */     } catch (UnsupportedEncodingException ue) {
/* 631 */       throw new SQLException(ue.toString(), "S1000");
/*     */     }
/*     */     
/* 634 */     this.byteBuffer[(this.position++)] = 0;
/*     */   }
/*     */   
/*     */   final void writeStringNoNull(String s) throws SQLException
/*     */   {
/* 639 */     int len = s.length();
/* 640 */     ensureCapacity(len * 2);
/* 641 */     System.arraycopy(s.getBytes(), 0, this.byteBuffer, this.position, len);
/* 642 */     this.position += len;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   final void writeStringNoNull(String s, String encoding, String serverEncoding, boolean parserKnowsUnicode, Connection conn)
/*     */     throws UnsupportedEncodingException, SQLException
/*     */   {
/* 655 */     byte[] b = StringUtils.getBytes(s, encoding, serverEncoding, parserKnowsUnicode, conn);
/*     */     
/*     */ 
/* 658 */     int len = b.length;
/* 659 */     ensureCapacity(len);
/* 660 */     System.arraycopy(b, 0, this.byteBuffer, this.position, len);
/* 661 */     this.position += len;
/*     */   }
/*     */ }


/* Location:              E:\java\java学习\hutubill\lib\all.jar!\com\mysql\jdbc\Buffer.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version:       0.7.1
 */
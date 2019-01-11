/*     */ package com.mysql.jdbc.profiler;
/*     */ 
/*     */ import com.mysql.jdbc.Util;
/*     */ import java.util.Date;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class ProfilerEvent
/*     */ {
/*     */   public static final byte TYPE_WARN = 0;
/*     */   public static final byte TYPE_OBJECT_CREATION = 1;
/*     */   public static final byte TYPE_PREPARE = 2;
/*     */   public static final byte TYPE_QUERY = 3;
/*     */   public static final byte TYPE_EXECUTE = 4;
/*     */   public static final byte TYPE_FETCH = 5;
/*     */   public static final byte TYPE_SLOW_QUERY = 6;
/*     */   protected byte eventType;
/*     */   protected long connectionId;
/*     */   protected int statementId;
/*     */   protected int resultSetId;
/*     */   protected long eventCreationTime;
/*     */   protected long eventDuration;
/*     */   protected String durationUnits;
/*     */   protected int hostNameIndex;
/*     */   protected String hostName;
/*     */   protected int catalogIndex;
/*     */   protected String catalog;
/*     */   protected int eventCreationPointIndex;
/*     */   protected Throwable eventCreationPoint;
/*     */   protected String eventCreationPointDesc;
/*     */   protected String message;
/*     */   
/*     */   public ProfilerEvent(byte eventType, String hostName, String catalog, long connectionId, int statementId, int resultSetId, long eventCreationTime, long eventDuration, String durationUnits, String eventCreationPointDesc, Throwable eventCreationPoint, String message)
/*     */   {
/* 180 */     this.eventType = eventType;
/* 181 */     this.connectionId = connectionId;
/* 182 */     this.statementId = statementId;
/* 183 */     this.resultSetId = resultSetId;
/* 184 */     this.eventCreationTime = eventCreationTime;
/* 185 */     this.eventDuration = eventDuration;
/* 186 */     this.durationUnits = durationUnits;
/* 187 */     this.eventCreationPoint = eventCreationPoint;
/* 188 */     this.eventCreationPointDesc = eventCreationPointDesc;
/* 189 */     this.message = message;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public String getEventCreationPointAsString()
/*     */   {
/* 198 */     if (this.eventCreationPointDesc == null) {
/* 199 */       this.eventCreationPointDesc = Util.stackTraceToString(this.eventCreationPoint);
/*     */     }
/*     */     
/*     */ 
/* 203 */     return this.eventCreationPointDesc;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public String toString()
/*     */   {
/* 212 */     StringBuffer buf = new StringBuffer(32);
/*     */     
/* 214 */     switch (this.eventType) {
/*     */     case 4: 
/* 216 */       buf.append("EXECUTE");
/* 217 */       break;
/*     */     
/*     */     case 5: 
/* 220 */       buf.append("FETCH");
/* 221 */       break;
/*     */     
/*     */     case 1: 
/* 224 */       buf.append("CONSTRUCT");
/* 225 */       break;
/*     */     
/*     */     case 2: 
/* 228 */       buf.append("PREPARE");
/* 229 */       break;
/*     */     
/*     */     case 3: 
/* 232 */       buf.append("QUERY");
/* 233 */       break;
/*     */     
/*     */     case 0: 
/* 236 */       buf.append("WARN");
/* 237 */       break;
/*     */     case 6: 
/* 239 */       buf.append("SLOW QUERY");
/* 240 */       break;
/*     */     default: 
/* 242 */       buf.append("UNKNOWN");
/*     */     }
/*     */     
/* 245 */     buf.append(" created: ");
/* 246 */     buf.append(new Date(this.eventCreationTime));
/* 247 */     buf.append(" duration: ");
/* 248 */     buf.append(this.eventDuration);
/* 249 */     buf.append(" connection: ");
/* 250 */     buf.append(this.connectionId);
/* 251 */     buf.append(" statement: ");
/* 252 */     buf.append(this.statementId);
/* 253 */     buf.append(" resultset: ");
/* 254 */     buf.append(this.resultSetId);
/*     */     
/* 256 */     if (this.message != null) {
/* 257 */       buf.append(" message: ");
/* 258 */       buf.append(this.message);
/*     */     }
/*     */     
/*     */ 
/* 262 */     if (this.eventCreationPointDesc != null) {
/* 263 */       buf.append("\n\nEvent Created at:\n");
/* 264 */       buf.append(this.eventCreationPointDesc);
/*     */     }
/*     */     
/* 267 */     return buf.toString();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static ProfilerEvent unpack(byte[] buf)
/*     */     throws Exception
/*     */   {
/* 280 */     int pos = 0;
/*     */     
/* 282 */     byte eventType = buf[(pos++)];
/* 283 */     long connectionId = readInt(buf, pos);
/* 284 */     pos += 8;
/* 285 */     int statementId = readInt(buf, pos);
/* 286 */     pos += 4;
/* 287 */     int resultSetId = readInt(buf, pos);
/* 288 */     pos += 4;
/* 289 */     long eventCreationTime = readLong(buf, pos);
/* 290 */     pos += 8;
/* 291 */     long eventDuration = readLong(buf, pos);
/* 292 */     pos += 4;
/*     */     
/* 294 */     byte[] eventDurationUnits = readBytes(buf, pos);
/* 295 */     pos += 4;
/*     */     
/* 297 */     if (eventDurationUnits != null) {
/* 298 */       pos += eventDurationUnits.length;
/*     */     }
/*     */     
/* 301 */     int eventCreationPointIndex = readInt(buf, pos);
/* 302 */     pos += 4;
/* 303 */     byte[] eventCreationAsBytes = readBytes(buf, pos);
/* 304 */     pos += 4;
/*     */     
/* 306 */     if (eventCreationAsBytes != null) {
/* 307 */       pos += eventCreationAsBytes.length;
/*     */     }
/*     */     
/* 310 */     byte[] message = readBytes(buf, pos);
/* 311 */     pos += 4;
/*     */     
/* 313 */     if (message != null) {
/* 314 */       pos += message.length;
/*     */     }
/*     */     
/* 317 */     return new ProfilerEvent(eventType, "", "", connectionId, statementId, resultSetId, eventCreationTime, eventDuration, new String(eventDurationUnits, "ISO8859_1"), new String(eventCreationAsBytes, "ISO8859_1"), null, new String(message, "ISO8859_1"));
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
/*     */   public byte[] pack()
/*     */     throws Exception
/*     */   {
/* 333 */     int len = 29;
/*     */     
/* 335 */     byte[] eventCreationAsBytes = null;
/*     */     
/* 337 */     getEventCreationPointAsString();
/*     */     
/* 339 */     if (this.eventCreationPointDesc != null) {
/* 340 */       eventCreationAsBytes = this.eventCreationPointDesc.getBytes("ISO8859_1");
/*     */       
/* 342 */       len += 4 + eventCreationAsBytes.length;
/*     */     } else {
/* 344 */       len += 4;
/*     */     }
/*     */     
/* 347 */     byte[] messageAsBytes = null;
/*     */     
/* 349 */     if (messageAsBytes != null) {
/* 350 */       messageAsBytes = this.message.getBytes("ISO8859_1");
/* 351 */       len += 4 + messageAsBytes.length;
/*     */     } else {
/* 353 */       len += 4;
/*     */     }
/*     */     
/* 356 */     byte[] durationUnitsAsBytes = null;
/*     */     
/* 358 */     if (this.durationUnits != null) {
/* 359 */       durationUnitsAsBytes = this.durationUnits.getBytes("ISO8859_1");
/* 360 */       len += 4 + durationUnitsAsBytes.length;
/*     */     } else {
/* 362 */       len += 4;
/*     */     }
/*     */     
/* 365 */     byte[] buf = new byte[len];
/*     */     
/* 367 */     int pos = 0;
/*     */     
/* 369 */     buf[(pos++)] = this.eventType;
/* 370 */     pos = writeLong(this.connectionId, buf, pos);
/* 371 */     pos = writeInt(this.statementId, buf, pos);
/* 372 */     pos = writeInt(this.resultSetId, buf, pos);
/* 373 */     pos = writeLong(this.eventCreationTime, buf, pos);
/* 374 */     pos = writeLong(this.eventDuration, buf, pos);
/* 375 */     pos = writeBytes(durationUnitsAsBytes, buf, pos);
/* 376 */     pos = writeInt(this.eventCreationPointIndex, buf, pos);
/*     */     
/* 378 */     if (eventCreationAsBytes != null) {
/* 379 */       pos = writeBytes(eventCreationAsBytes, buf, pos);
/*     */     } else {
/* 381 */       pos = writeInt(0, buf, pos);
/*     */     }
/*     */     
/* 384 */     if (messageAsBytes != null) {
/* 385 */       pos = writeBytes(messageAsBytes, buf, pos);
/*     */     } else {
/* 387 */       pos = writeInt(0, buf, pos);
/*     */     }
/*     */     
/* 390 */     return buf;
/*     */   }
/*     */   
/*     */   private static int writeInt(int i, byte[] buf, int pos)
/*     */   {
/* 395 */     buf[(pos++)] = ((byte)(i & 0xFF));
/* 396 */     buf[(pos++)] = ((byte)(i >>> 8));
/* 397 */     buf[(pos++)] = ((byte)(i >>> 16));
/* 398 */     buf[(pos++)] = ((byte)(i >>> 24));
/*     */     
/* 400 */     return pos;
/*     */   }
/*     */   
/*     */   private static int writeLong(long l, byte[] buf, int pos) {
/* 404 */     buf[(pos++)] = ((byte)(int)(l & 0xFF));
/* 405 */     buf[(pos++)] = ((byte)(int)(l >>> 8));
/* 406 */     buf[(pos++)] = ((byte)(int)(l >>> 16));
/* 407 */     buf[(pos++)] = ((byte)(int)(l >>> 24));
/* 408 */     buf[(pos++)] = ((byte)(int)(l >>> 32));
/* 409 */     buf[(pos++)] = ((byte)(int)(l >>> 40));
/* 410 */     buf[(pos++)] = ((byte)(int)(l >>> 48));
/* 411 */     buf[(pos++)] = ((byte)(int)(l >>> 56));
/*     */     
/* 413 */     return pos;
/*     */   }
/*     */   
/*     */   private static int writeBytes(byte[] msg, byte[] buf, int pos) {
/* 417 */     pos = writeInt(msg.length, buf, pos);
/*     */     
/* 419 */     System.arraycopy(msg, 0, buf, pos, msg.length);
/*     */     
/* 421 */     return pos + msg.length;
/*     */   }
/*     */   
/*     */   private static int readInt(byte[] buf, int pos) {
/* 425 */     return buf[(pos++)] & 0xFF | (buf[(pos++)] & 0xFF) << 8 | (buf[(pos++)] & 0xFF) << 16 | (buf[(pos++)] & 0xFF) << 24;
/*     */   }
/*     */   
/*     */ 
/*     */   private static long readLong(byte[] buf, int pos)
/*     */   {
/* 431 */     return buf[(pos++)] & 0xFF | (buf[(pos++)] & 0xFF) << 8 | (buf[(pos++)] & 0xFF) << 16 | (buf[(pos++)] & 0xFF) << 24 | (buf[(pos++)] & 0xFF) << 32 | (buf[(pos++)] & 0xFF) << 40 | (buf[(pos++)] & 0xFF) << 48 | (buf[(pos++)] & 0xFF) << 56;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private static byte[] readBytes(byte[] buf, int pos)
/*     */   {
/* 441 */     int length = readInt(buf, pos);
/*     */     
/* 443 */     pos += 4;
/*     */     
/* 445 */     byte[] msg = new byte[length];
/* 446 */     System.arraycopy(buf, pos, msg, 0, length);
/*     */     
/* 448 */     return msg;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public String getCatalog()
/*     */   {
/* 457 */     return this.catalog;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public long getConnectionId()
/*     */   {
/* 466 */     return this.connectionId;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public Throwable getEventCreationPoint()
/*     */   {
/* 476 */     return this.eventCreationPoint;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public long getEventCreationTime()
/*     */   {
/* 486 */     return this.eventCreationTime;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public long getEventDuration()
/*     */   {
/* 495 */     return this.eventDuration;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public String getDurationUnits()
/*     */   {
/* 502 */     return this.durationUnits;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public byte getEventType()
/*     */   {
/* 511 */     return this.eventType;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public int getResultSetId()
/*     */   {
/* 520 */     return this.resultSetId;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public int getStatementId()
/*     */   {
/* 529 */     return this.statementId;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public String getMessage()
/*     */   {
/* 538 */     return this.message;
/*     */   }
/*     */ }


/* Location:              E:\java\java学习\hutubill\lib\all.jar!\com\mysql\jdbc\profiler\ProfilerEvent.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version:       0.7.1
 */
/*     */ package com.mysql.jdbc;
/*     */ 
/*     */ import java.io.UnsupportedEncodingException;
/*     */ import java.sql.SQLException;
/*     */ import java.util.HashMap;
/*     */ import java.util.Map;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class SingleByteCharsetConverter
/*     */ {
/*     */   private static final int BYTE_RANGE = 256;
/*  45 */   private static byte[] allBytes = new byte['Ā'];
/*  46 */   private static final Map CONVERTER_MAP = new HashMap();
/*     */   
/*  48 */   private static final byte[] EMPTY_BYTE_ARRAY = new byte[0];
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*  53 */   private static byte[] unknownCharsMap = new byte[65536];
/*     */   
/*     */   static {
/*  56 */     for (int i = -128; i <= 127; i++) {
/*  57 */       allBytes[(i - -128)] = ((byte)i);
/*     */     }
/*     */     
/*  60 */     for (int i = 0; i < unknownCharsMap.length; i++) {
/*  61 */       unknownCharsMap[i] = 63;
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
/*     */ 
/*     */ 
/*     */   public static synchronized SingleByteCharsetConverter getInstance(String encodingName, Connection conn)
/*     */     throws UnsupportedEncodingException, SQLException
/*     */   {
/*  82 */     SingleByteCharsetConverter instance = (SingleByteCharsetConverter)CONVERTER_MAP.get(encodingName);
/*     */     
/*     */ 
/*  85 */     if (instance == null) {
/*  86 */       instance = initCharset(encodingName);
/*     */     }
/*     */     
/*  89 */     return instance;
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
/*     */   public static SingleByteCharsetConverter initCharset(String javaEncodingName)
/*     */     throws UnsupportedEncodingException, SQLException
/*     */   {
/* 104 */     if (CharsetMapping.isMultibyteCharset(javaEncodingName)) {
/* 105 */       return null;
/*     */     }
/*     */     
/* 108 */     SingleByteCharsetConverter converter = new SingleByteCharsetConverter(javaEncodingName);
/*     */     
/*     */ 
/* 111 */     CONVERTER_MAP.put(javaEncodingName, converter);
/*     */     
/* 113 */     return converter;
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
/*     */ 
/*     */ 
/*     */   public static String toStringDefaultEncoding(byte[] buffer, int startPos, int length)
/*     */   {
/* 133 */     return new String(buffer, startPos, length);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/* 139 */   private char[] byteToChars = new char['Ā'];
/*     */   
/* 141 */   private byte[] charToByteMap = new byte[65536];
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private SingleByteCharsetConverter(String encodingName)
/*     */     throws UnsupportedEncodingException
/*     */   {
/* 153 */     String allBytesString = new String(allBytes, 0, 256, encodingName);
/*     */     
/* 155 */     int allBytesLen = allBytesString.length();
/*     */     
/* 157 */     System.arraycopy(unknownCharsMap, 0, this.charToByteMap, 0, this.charToByteMap.length);
/*     */     
/*     */ 
/* 160 */     for (int i = 0; (i < 256) && (i < allBytesLen); i++) {
/* 161 */       char c = allBytesString.charAt(i);
/* 162 */       this.byteToChars[i] = c;
/* 163 */       this.charToByteMap[c] = allBytes[i];
/*     */     }
/*     */   }
/*     */   
/*     */   public final byte[] toBytes(char[] c) {
/* 168 */     if (c == null) {
/* 169 */       return null;
/*     */     }
/*     */     
/* 172 */     int length = c.length;
/* 173 */     byte[] bytes = new byte[length];
/*     */     
/* 175 */     for (int i = 0; i < length; i++) {
/* 176 */       bytes[i] = this.charToByteMap[c[i]];
/*     */     }
/*     */     
/* 179 */     return bytes;
/*     */   }
/*     */   
/*     */   public final byte[] toBytes(char[] chars, int offset, int length) {
/* 183 */     if (chars == null) {
/* 184 */       return null;
/*     */     }
/*     */     
/* 187 */     if (length == 0) {
/* 188 */       return EMPTY_BYTE_ARRAY;
/*     */     }
/*     */     
/* 191 */     byte[] bytes = new byte[length];
/*     */     
/* 193 */     for (int i = 0; i < length; i++) {
/* 194 */       bytes[i] = this.charToByteMap[chars[(i + offset)]];
/*     */     }
/*     */     
/* 197 */     return bytes;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public final byte[] toBytes(String s)
/*     */   {
/* 208 */     if (s == null) {
/* 209 */       return null;
/*     */     }
/*     */     
/* 212 */     int length = s.length();
/* 213 */     byte[] bytes = new byte[length];
/*     */     
/* 215 */     for (int i = 0; i < length; i++) {
/* 216 */       bytes[i] = this.charToByteMap[s.charAt(i)];
/*     */     }
/*     */     
/* 219 */     return bytes;
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
/*     */   public final byte[] toBytes(String s, int offset, int length)
/*     */   {
/* 235 */     if (s == null) {
/* 236 */       return null;
/*     */     }
/*     */     
/* 239 */     if (length == 0) {
/* 240 */       return EMPTY_BYTE_ARRAY;
/*     */     }
/*     */     
/* 243 */     byte[] bytes = new byte[length];
/*     */     
/* 245 */     for (int i = 0; i < length; i++) {
/* 246 */       char c = s.charAt(i + offset);
/* 247 */       bytes[i] = this.charToByteMap[c];
/*     */     }
/*     */     
/* 250 */     return bytes;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public final String toString(byte[] buffer)
/*     */   {
/* 262 */     return toString(buffer, 0, buffer.length);
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
/*     */   public final String toString(byte[] buffer, int startPos, int length)
/*     */   {
/* 278 */     char[] charArray = new char[length];
/* 279 */     int readpoint = startPos;
/*     */     
/* 281 */     for (int i = 0; i < length; i++) {
/* 282 */       charArray[i] = this.byteToChars[(buffer[readpoint] - Byte.MIN_VALUE)];
/* 283 */       readpoint++;
/*     */     }
/*     */     
/* 286 */     return new String(charArray);
/*     */   }
/*     */ }


/* Location:              E:\java\java学习\hutubill\lib\all.jar!\com\mysql\jdbc\SingleByteCharsetConverter.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version:       0.7.1
 */
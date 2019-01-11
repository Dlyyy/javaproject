/*     */ package com.mysql.jdbc;
/*     */ 
/*     */ import java.sql.SQLException;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collections;
/*     */ import java.util.HashMap;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import java.util.Locale;
/*     */ import java.util.Map;
/*     */ import java.util.Properties;
/*     */ import java.util.Set;
/*     */ import java.util.TreeMap;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class CharsetMapping
/*     */ {
/*  48 */   private static final Properties CHARSET_CONFIG = new Properties();
/*     */   
/*     */ 
/*     */   public static final String[] INDEX_TO_CHARSET;
/*     */   
/*     */ 
/*     */   public static final String[] INDEX_TO_COLLATION;
/*     */   
/*     */ 
/*     */   private static final Map JAVA_TO_MYSQL_CHARSET_MAP;
/*     */   
/*     */ 
/*     */   private static final Map JAVA_UC_TO_MYSQL_CHARSET_MAP;
/*     */   
/*     */ 
/*     */   private static final Map ERROR_MESSAGE_FILE_TO_MYSQL_CHARSET_MAP;
/*     */   
/*     */ 
/*     */   private static final Map MULTIBYTE_CHARSETS;
/*     */   
/*     */   private static final Map MYSQL_TO_JAVA_CHARSET_MAP;
/*     */   
/*     */   private static final String NOT_USED = "ISO8859_1";
/*     */   
/*     */   public static final Map STATIC_CHARSET_TO_NUM_BYTES_MAP;
/*     */   
/*     */ 
/*     */   static
/*     */   {
/*  77 */     HashMap tempNumBytesMap = new HashMap();
/*     */     
/*  79 */     tempNumBytesMap.put("big5", new Integer(2));
/*  80 */     tempNumBytesMap.put("dec8", new Integer(1));
/*  81 */     tempNumBytesMap.put("cp850", new Integer(1));
/*  82 */     tempNumBytesMap.put("hp8", new Integer(1));
/*  83 */     tempNumBytesMap.put("koi8r", new Integer(1));
/*  84 */     tempNumBytesMap.put("latin1", new Integer(1));
/*  85 */     tempNumBytesMap.put("latin2", new Integer(1));
/*  86 */     tempNumBytesMap.put("swe7", new Integer(1));
/*  87 */     tempNumBytesMap.put("ascii", new Integer(1));
/*  88 */     tempNumBytesMap.put("ujis", new Integer(3));
/*  89 */     tempNumBytesMap.put("sjis", new Integer(2));
/*  90 */     tempNumBytesMap.put("hebrew", new Integer(1));
/*  91 */     tempNumBytesMap.put("tis620", new Integer(1));
/*  92 */     tempNumBytesMap.put("euckr", new Integer(2));
/*  93 */     tempNumBytesMap.put("koi8u", new Integer(1));
/*  94 */     tempNumBytesMap.put("gb2312", new Integer(2));
/*  95 */     tempNumBytesMap.put("greek", new Integer(1));
/*  96 */     tempNumBytesMap.put("cp1250", new Integer(1));
/*  97 */     tempNumBytesMap.put("gbk", new Integer(2));
/*  98 */     tempNumBytesMap.put("latin5", new Integer(1));
/*  99 */     tempNumBytesMap.put("armscii8", new Integer(1));
/* 100 */     tempNumBytesMap.put("utf8", new Integer(3));
/* 101 */     tempNumBytesMap.put("ucs2", new Integer(2));
/* 102 */     tempNumBytesMap.put("cp866", new Integer(1));
/* 103 */     tempNumBytesMap.put("keybcs2", new Integer(1));
/* 104 */     tempNumBytesMap.put("macce", new Integer(1));
/* 105 */     tempNumBytesMap.put("macroman", new Integer(1));
/* 106 */     tempNumBytesMap.put("cp852", new Integer(1));
/* 107 */     tempNumBytesMap.put("latin7", new Integer(1));
/* 108 */     tempNumBytesMap.put("cp1251", new Integer(1));
/* 109 */     tempNumBytesMap.put("cp1256", new Integer(1));
/* 110 */     tempNumBytesMap.put("cp1257", new Integer(1));
/* 111 */     tempNumBytesMap.put("binary", new Integer(1));
/* 112 */     tempNumBytesMap.put("geostd8", new Integer(1));
/* 113 */     tempNumBytesMap.put("cp932", new Integer(2));
/* 114 */     tempNumBytesMap.put("eucjpms", new Integer(3));
/*     */     
/* 116 */     STATIC_CHARSET_TO_NUM_BYTES_MAP = Collections.unmodifiableMap(tempNumBytesMap);
/*     */     
/*     */ 
/* 119 */     CHARSET_CONFIG.setProperty("javaToMysqlMappings", "US-ASCII =\t\t\tusa7,US-ASCII =\t\t\t>4.1.0 ascii,Big5 = \t\t\t\tbig5,GBK = \t\t\t\tgbk,SJIS = \t\t\t\tsjis,EUC_CN = \t\t\tgb2312,EUC_JP = \t\t\tujis,EUC_JP_Solaris = \t>5.0.3 eucjpms,EUC_KR = \t\t\teuc_kr,EUC_KR = \t\t\t>4.1.0 euckr,ISO8859_1 =\t\t\t*latin1,ISO8859_1 =\t\t\tlatin1_de,ISO8859_1 =\t\t\tgerman1,ISO8859_1 =\t\t\tdanish,ISO8859_2 =\t\t\tlatin2,ISO8859_2 =\t\t\tczech,ISO8859_2 =\t\t\thungarian,ISO8859_2  =\t\tcroat,ISO8859_7  =\t\tgreek,ISO8859_7  =\t\tlatin7,ISO8859_8  = \t\thebrew,ISO8859_9  =\t\tlatin5,ISO8859_13 =\t\tlatvian,ISO8859_13 =\t\tlatvian1,ISO8859_13 =\t\testonia,Cp437 =             *>4.1.0 cp850,Cp437 =\t\t\t\tdos,Cp850 =\t\t\t\tcp850,Cp852 = \t\t\tcp852,Cp866 = \t\t\tcp866,KOI8_R = \t\t\tkoi8_ru,KOI8_R = \t\t\t>4.1.0 koi8r,TIS620 = \t\t\ttis620,Cp1250 = \t\t\tcp1250,Cp1250 = \t\t\twin1250,Cp1251 = \t\t\t*>4.1.0 cp1251,Cp1251 = \t\t\twin1251,Cp1251 = \t\t\tcp1251cias,Cp1251 = \t\t\tcp1251csas,Cp1256 = \t\t\tcp1256,Cp1251 = \t\t\twin1251ukr,Cp1252 =             latin1,Cp1257 = \t\t\tcp1257,MacRoman = \t\t\tmacroman,MacCentralEurope = \tmacce,UTF-8 = \t\tutf8,UnicodeBig = \tucs2,US-ASCII =\t\tbinary,Cp943 =        \tsjis,MS932 =\t\t\tsjis,MS932 =        \t>4.1.11 cp932,WINDOWS-31J =\tsjis,WINDOWS-31J = \t>4.1.11 cp932,CP932 =\t\t\tsjis,CP932 =\t\t\t*>4.1.11 cp932,SHIFT_JIS = \tsjis,ASCII =\t\t\tascii,LATIN5 =\t\tlatin5,LATIN7 =\t\tlatin7,HEBREW =\t\thebrew,GREEK =\t\t\tgreek,EUCKR =\t\t\teuckr,GB2312 =\t\tgb2312,LATIN2 =\t\tlatin2");
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 194 */     HashMap javaToMysqlMap = new HashMap();
/*     */     
/* 196 */     populateMapWithKeyValuePairs("javaToMysqlMappings", javaToMysqlMap, true, false);
/*     */     
/* 198 */     JAVA_TO_MYSQL_CHARSET_MAP = Collections.unmodifiableMap(javaToMysqlMap);
/*     */     
/* 200 */     HashMap mysqlToJavaMap = new HashMap();
/*     */     
/* 202 */     Set keySet = JAVA_TO_MYSQL_CHARSET_MAP.keySet();
/*     */     
/* 204 */     Iterator javaCharsets = keySet.iterator();
/*     */     
/* 206 */     while (javaCharsets.hasNext()) {
/* 207 */       Object javaEncodingName = javaCharsets.next();
/* 208 */       List mysqlEncodingList = (List)JAVA_TO_MYSQL_CHARSET_MAP.get(javaEncodingName);
/*     */       
/*     */ 
/* 211 */       Iterator mysqlEncodings = mysqlEncodingList.iterator();
/*     */       
/* 213 */       String mysqlEncodingName = null;
/*     */       
/* 215 */       while (mysqlEncodings.hasNext()) {
/* 216 */         VersionedStringProperty mysqlProp = (VersionedStringProperty)mysqlEncodings.next();
/*     */         
/* 218 */         mysqlEncodingName = mysqlProp.toString();
/*     */         
/* 220 */         mysqlToJavaMap.put(mysqlEncodingName, javaEncodingName);
/* 221 */         mysqlToJavaMap.put(mysqlEncodingName.toUpperCase(Locale.ENGLISH), javaEncodingName);
/*     */       }
/*     */     }
/*     */     
/*     */ 
/*     */ 
/* 227 */     mysqlToJavaMap.put("cp932", "Windows-31J");
/* 228 */     mysqlToJavaMap.put("CP932", "Windows-31J");
/*     */     
/* 230 */     MYSQL_TO_JAVA_CHARSET_MAP = Collections.unmodifiableMap(mysqlToJavaMap);
/*     */     
/* 232 */     TreeMap ucMap = new TreeMap(String.CASE_INSENSITIVE_ORDER);
/*     */     
/* 234 */     Iterator javaNamesKeys = JAVA_TO_MYSQL_CHARSET_MAP.keySet().iterator();
/*     */     
/* 236 */     while (javaNamesKeys.hasNext()) {
/* 237 */       String key = (String)javaNamesKeys.next();
/*     */       
/* 239 */       ucMap.put(key.toUpperCase(Locale.ENGLISH), JAVA_TO_MYSQL_CHARSET_MAP.get(key));
/*     */     }
/*     */     
/*     */ 
/* 243 */     JAVA_UC_TO_MYSQL_CHARSET_MAP = Collections.unmodifiableMap(ucMap);
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 249 */     HashMap tempMapMulti = new HashMap();
/*     */     
/* 251 */     CHARSET_CONFIG.setProperty("multibyteCharsets", "Big5 = \t\t\tbig5,GBK = \t\t\tgbk,SJIS = \t\t\tsjis,EUC_CN = \t\tgb2312,EUC_JP = \t\tujis,EUC_JP_Solaris = eucjpms,EUC_KR = \t\teuc_kr,EUC_KR = \t\t>4.1.0 euckr,Cp943 =        \tsjis,Cp943 = \t\tcp943,WINDOWS-31J =\tsjis,WINDOWS-31J = \tcp932,CP932 =\t\t\tcp932,MS932 =\t\t\tsjis,MS932 =        \tcp932,SHIFT_JIS = \tsjis,EUCKR =\t\t\teuckr,GB2312 =\t\tgb2312,UTF-8 = \t\tutf8,utf8 =          utf8,UnicodeBig = \tucs2");
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 283 */     populateMapWithKeyValuePairs("multibyteCharsets", tempMapMulti, false, true);
/*     */     
/*     */ 
/* 286 */     MULTIBYTE_CHARSETS = Collections.unmodifiableMap(tempMapMulti);
/*     */     
/* 288 */     INDEX_TO_CHARSET = new String['Ó'];
/*     */     try
/*     */     {
/* 291 */       INDEX_TO_CHARSET[1] = getJavaEncodingForMysqlEncoding("big5", null);
/* 292 */       INDEX_TO_CHARSET[2] = getJavaEncodingForMysqlEncoding("czech", null);
/* 293 */       INDEX_TO_CHARSET[3] = "ISO8859_1";
/* 294 */       INDEX_TO_CHARSET[4] = "ISO8859_1";
/* 295 */       INDEX_TO_CHARSET[5] = getJavaEncodingForMysqlEncoding("german1", null);
/*     */       
/* 297 */       INDEX_TO_CHARSET[6] = "ISO8859_1";
/* 298 */       INDEX_TO_CHARSET[7] = getJavaEncodingForMysqlEncoding("koi8_ru", null);
/*     */       
/* 300 */       INDEX_TO_CHARSET[8] = getJavaEncodingForMysqlEncoding("latin1", null);
/*     */       
/* 302 */       INDEX_TO_CHARSET[9] = getJavaEncodingForMysqlEncoding("latin2", null);
/*     */       
/* 304 */       INDEX_TO_CHARSET[10] = "ISO8859_1";
/* 305 */       INDEX_TO_CHARSET[11] = getJavaEncodingForMysqlEncoding("usa7", null);
/* 306 */       INDEX_TO_CHARSET[12] = getJavaEncodingForMysqlEncoding("ujis", null);
/* 307 */       INDEX_TO_CHARSET[13] = getJavaEncodingForMysqlEncoding("sjis", null);
/* 308 */       INDEX_TO_CHARSET[14] = getJavaEncodingForMysqlEncoding("cp1251", null);
/*     */       
/* 310 */       INDEX_TO_CHARSET[15] = getJavaEncodingForMysqlEncoding("danish", null);
/*     */       
/* 312 */       INDEX_TO_CHARSET[16] = getJavaEncodingForMysqlEncoding("hebrew", null);
/*     */       
/*     */ 
/* 315 */       INDEX_TO_CHARSET[17] = "ISO8859_1";
/*     */       
/* 317 */       INDEX_TO_CHARSET[18] = getJavaEncodingForMysqlEncoding("tis620", null);
/*     */       
/* 319 */       INDEX_TO_CHARSET[19] = getJavaEncodingForMysqlEncoding("euc_kr", null);
/*     */       
/* 321 */       INDEX_TO_CHARSET[20] = getJavaEncodingForMysqlEncoding("estonia", null);
/*     */       
/* 323 */       INDEX_TO_CHARSET[21] = getJavaEncodingForMysqlEncoding("hungarian", null);
/*     */       
/* 325 */       INDEX_TO_CHARSET[22] = "KOI8_R";
/* 326 */       INDEX_TO_CHARSET[23] = getJavaEncodingForMysqlEncoding("win1251ukr", null);
/*     */       
/* 328 */       INDEX_TO_CHARSET[24] = getJavaEncodingForMysqlEncoding("gb2312", null);
/*     */       
/* 330 */       INDEX_TO_CHARSET[25] = getJavaEncodingForMysqlEncoding("greek", null);
/*     */       
/* 332 */       INDEX_TO_CHARSET[26] = getJavaEncodingForMysqlEncoding("win1250", null);
/*     */       
/* 334 */       INDEX_TO_CHARSET[27] = getJavaEncodingForMysqlEncoding("croat", null);
/*     */       
/* 336 */       INDEX_TO_CHARSET[28] = getJavaEncodingForMysqlEncoding("gbk", null);
/* 337 */       INDEX_TO_CHARSET[29] = getJavaEncodingForMysqlEncoding("cp1257", null);
/*     */       
/* 339 */       INDEX_TO_CHARSET[30] = getJavaEncodingForMysqlEncoding("latin5", null);
/*     */       
/* 341 */       INDEX_TO_CHARSET[31] = getJavaEncodingForMysqlEncoding("latin1_de", null);
/*     */       
/* 343 */       INDEX_TO_CHARSET[32] = "ISO8859_1";
/* 344 */       INDEX_TO_CHARSET[33] = getJavaEncodingForMysqlEncoding("utf8", null);
/* 345 */       INDEX_TO_CHARSET[34] = "Cp1250";
/* 346 */       INDEX_TO_CHARSET[35] = getJavaEncodingForMysqlEncoding("ucs2", null);
/* 347 */       INDEX_TO_CHARSET[36] = getJavaEncodingForMysqlEncoding("cp866", null);
/*     */       
/* 349 */       INDEX_TO_CHARSET[37] = "Cp895";
/* 350 */       INDEX_TO_CHARSET[38] = getJavaEncodingForMysqlEncoding("macce", null);
/*     */       
/* 352 */       INDEX_TO_CHARSET[39] = getJavaEncodingForMysqlEncoding("macroman", null);
/*     */       
/* 354 */       INDEX_TO_CHARSET[40] = "latin2";
/* 355 */       INDEX_TO_CHARSET[41] = getJavaEncodingForMysqlEncoding("latvian", null);
/*     */       
/* 357 */       INDEX_TO_CHARSET[42] = getJavaEncodingForMysqlEncoding("latvian1", null);
/*     */       
/* 359 */       INDEX_TO_CHARSET[43] = getJavaEncodingForMysqlEncoding("macce", null);
/*     */       
/* 361 */       INDEX_TO_CHARSET[44] = getJavaEncodingForMysqlEncoding("macce", null);
/*     */       
/* 363 */       INDEX_TO_CHARSET[45] = getJavaEncodingForMysqlEncoding("macce", null);
/*     */       
/* 365 */       INDEX_TO_CHARSET[46] = getJavaEncodingForMysqlEncoding("macce", null);
/*     */       
/* 367 */       INDEX_TO_CHARSET[47] = getJavaEncodingForMysqlEncoding("latin1", null);
/*     */       
/* 369 */       INDEX_TO_CHARSET[48] = getJavaEncodingForMysqlEncoding("latin1", null);
/*     */       
/* 371 */       INDEX_TO_CHARSET[49] = getJavaEncodingForMysqlEncoding("latin1", null);
/*     */       
/* 373 */       INDEX_TO_CHARSET[50] = getJavaEncodingForMysqlEncoding("cp1251", null);
/*     */       
/* 375 */       INDEX_TO_CHARSET[51] = getJavaEncodingForMysqlEncoding("cp1251", null);
/*     */       
/* 377 */       INDEX_TO_CHARSET[52] = getJavaEncodingForMysqlEncoding("cp1251", null);
/*     */       
/* 379 */       INDEX_TO_CHARSET[53] = getJavaEncodingForMysqlEncoding("macroman", null);
/*     */       
/* 381 */       INDEX_TO_CHARSET[54] = getJavaEncodingForMysqlEncoding("macroman", null);
/*     */       
/* 383 */       INDEX_TO_CHARSET[55] = getJavaEncodingForMysqlEncoding("macroman", null);
/*     */       
/* 385 */       INDEX_TO_CHARSET[56] = getJavaEncodingForMysqlEncoding("macroman", null);
/*     */       
/* 387 */       INDEX_TO_CHARSET[57] = getJavaEncodingForMysqlEncoding("cp1256", null);
/*     */       
/*     */ 
/* 390 */       INDEX_TO_CHARSET[58] = "ISO8859_1";
/* 391 */       INDEX_TO_CHARSET[59] = "ISO8859_1";
/* 392 */       INDEX_TO_CHARSET[60] = "ISO8859_1";
/* 393 */       INDEX_TO_CHARSET[61] = "ISO8859_1";
/* 394 */       INDEX_TO_CHARSET[62] = "ISO8859_1";
/*     */       
/* 396 */       INDEX_TO_CHARSET[63] = getJavaEncodingForMysqlEncoding("binary", null);
/*     */       
/* 398 */       INDEX_TO_CHARSET[64] = "ISO8859_2";
/* 399 */       INDEX_TO_CHARSET[65] = getJavaEncodingForMysqlEncoding("ascii", null);
/*     */       
/* 401 */       INDEX_TO_CHARSET[66] = getJavaEncodingForMysqlEncoding("cp1250", null);
/*     */       
/* 403 */       INDEX_TO_CHARSET[67] = getJavaEncodingForMysqlEncoding("cp1256", null);
/*     */       
/* 405 */       INDEX_TO_CHARSET[68] = getJavaEncodingForMysqlEncoding("cp866", null);
/*     */       
/* 407 */       INDEX_TO_CHARSET[69] = "US-ASCII";
/* 408 */       INDEX_TO_CHARSET[70] = getJavaEncodingForMysqlEncoding("greek", null);
/*     */       
/* 410 */       INDEX_TO_CHARSET[71] = getJavaEncodingForMysqlEncoding("hebrew", null);
/*     */       
/* 412 */       INDEX_TO_CHARSET[72] = "US-ASCII";
/* 413 */       INDEX_TO_CHARSET[73] = "Cp895";
/* 414 */       INDEX_TO_CHARSET[74] = getJavaEncodingForMysqlEncoding("koi8r", null);
/*     */       
/* 416 */       INDEX_TO_CHARSET[75] = "KOI8_r";
/*     */       
/* 418 */       INDEX_TO_CHARSET[76] = "ISO8859_1";
/*     */       
/* 420 */       INDEX_TO_CHARSET[77] = getJavaEncodingForMysqlEncoding("latin2", null);
/*     */       
/* 422 */       INDEX_TO_CHARSET[78] = getJavaEncodingForMysqlEncoding("latin5", null);
/*     */       
/* 424 */       INDEX_TO_CHARSET[79] = getJavaEncodingForMysqlEncoding("latin7", null);
/*     */       
/* 426 */       INDEX_TO_CHARSET[80] = getJavaEncodingForMysqlEncoding("cp850", null);
/*     */       
/* 428 */       INDEX_TO_CHARSET[81] = getJavaEncodingForMysqlEncoding("cp852", null);
/*     */       
/* 430 */       INDEX_TO_CHARSET[82] = "ISO8859_1";
/* 431 */       INDEX_TO_CHARSET[83] = getJavaEncodingForMysqlEncoding("utf8", null);
/* 432 */       INDEX_TO_CHARSET[84] = getJavaEncodingForMysqlEncoding("big5", null);
/* 433 */       INDEX_TO_CHARSET[85] = getJavaEncodingForMysqlEncoding("euckr", null);
/*     */       
/* 435 */       INDEX_TO_CHARSET[86] = getJavaEncodingForMysqlEncoding("gb2312", null);
/*     */       
/* 437 */       INDEX_TO_CHARSET[87] = getJavaEncodingForMysqlEncoding("gbk", null);
/* 438 */       INDEX_TO_CHARSET[88] = getJavaEncodingForMysqlEncoding("sjis", null);
/* 439 */       INDEX_TO_CHARSET[89] = getJavaEncodingForMysqlEncoding("tis620", null);
/*     */       
/* 441 */       INDEX_TO_CHARSET[90] = getJavaEncodingForMysqlEncoding("ucs2", null);
/* 442 */       INDEX_TO_CHARSET[91] = getJavaEncodingForMysqlEncoding("ujis", null);
/* 443 */       INDEX_TO_CHARSET[92] = "US-ASCII";
/* 444 */       INDEX_TO_CHARSET[93] = "US-ASCII";
/* 445 */       INDEX_TO_CHARSET[94] = getJavaEncodingForMysqlEncoding("latin1", null);
/*     */       
/* 447 */       INDEX_TO_CHARSET[95] = getJavaEncodingForMysqlEncoding("cp932", null);
/*     */       
/* 449 */       INDEX_TO_CHARSET[96] = getJavaEncodingForMysqlEncoding("cp932", null);
/*     */       
/* 451 */       INDEX_TO_CHARSET[97] = getJavaEncodingForMysqlEncoding("eucjpms", null);
/*     */       
/* 453 */       INDEX_TO_CHARSET[98] = getJavaEncodingForMysqlEncoding("eucjpms", null);
/*     */       
/*     */ 
/* 456 */       for (int i = 99; i < 128; i++) {
/* 457 */         INDEX_TO_CHARSET[i] = "ISO8859_1";
/*     */       }
/*     */       
/* 460 */       INDEX_TO_CHARSET[''] = getJavaEncodingForMysqlEncoding("ucs2", null);
/*     */       
/* 462 */       INDEX_TO_CHARSET[''] = getJavaEncodingForMysqlEncoding("ucs2", null);
/*     */       
/* 464 */       INDEX_TO_CHARSET[''] = getJavaEncodingForMysqlEncoding("ucs2", null);
/*     */       
/* 466 */       INDEX_TO_CHARSET[''] = getJavaEncodingForMysqlEncoding("ucs2", null);
/*     */       
/* 468 */       INDEX_TO_CHARSET[''] = getJavaEncodingForMysqlEncoding("ucs2", null);
/*     */       
/* 470 */       INDEX_TO_CHARSET[''] = getJavaEncodingForMysqlEncoding("ucs2", null);
/*     */       
/* 472 */       INDEX_TO_CHARSET[''] = getJavaEncodingForMysqlEncoding("ucs2", null);
/*     */       
/* 474 */       INDEX_TO_CHARSET[''] = getJavaEncodingForMysqlEncoding("ucs2", null);
/*     */       
/* 476 */       INDEX_TO_CHARSET[''] = getJavaEncodingForMysqlEncoding("ucs2", null);
/*     */       
/* 478 */       INDEX_TO_CHARSET[''] = getJavaEncodingForMysqlEncoding("ucs2", null);
/*     */       
/* 480 */       INDEX_TO_CHARSET[''] = getJavaEncodingForMysqlEncoding("ucs2", null);
/*     */       
/* 482 */       INDEX_TO_CHARSET[''] = getJavaEncodingForMysqlEncoding("ucs2", null);
/*     */       
/* 484 */       INDEX_TO_CHARSET[''] = getJavaEncodingForMysqlEncoding("ucs2", null);
/*     */       
/* 486 */       INDEX_TO_CHARSET[''] = getJavaEncodingForMysqlEncoding("ucs2", null);
/*     */       
/* 488 */       INDEX_TO_CHARSET[''] = getJavaEncodingForMysqlEncoding("ucs2", null);
/*     */       
/* 490 */       INDEX_TO_CHARSET[''] = getJavaEncodingForMysqlEncoding("ucs2", null);
/*     */       
/* 492 */       INDEX_TO_CHARSET[''] = getJavaEncodingForMysqlEncoding("ucs2", null);
/*     */       
/* 494 */       INDEX_TO_CHARSET[''] = getJavaEncodingForMysqlEncoding("ucs2", null);
/*     */       
/* 496 */       INDEX_TO_CHARSET[''] = getJavaEncodingForMysqlEncoding("ucs2", null);
/*     */       
/*     */ 
/* 499 */       for (int i = 147; i < 192; i++) {
/* 500 */         INDEX_TO_CHARSET[i] = "ISO8859_1";
/*     */       }
/*     */       
/* 503 */       INDEX_TO_CHARSET['À'] = getJavaEncodingForMysqlEncoding("utf8", null);
/*     */       
/* 505 */       INDEX_TO_CHARSET['Á'] = getJavaEncodingForMysqlEncoding("utf8", null);
/*     */       
/* 507 */       INDEX_TO_CHARSET['Â'] = getJavaEncodingForMysqlEncoding("utf8", null);
/*     */       
/* 509 */       INDEX_TO_CHARSET['Ã'] = getJavaEncodingForMysqlEncoding("utf8", null);
/*     */       
/* 511 */       INDEX_TO_CHARSET['Ä'] = getJavaEncodingForMysqlEncoding("utf8", null);
/*     */       
/* 513 */       INDEX_TO_CHARSET['Å'] = getJavaEncodingForMysqlEncoding("utf8", null);
/*     */       
/* 515 */       INDEX_TO_CHARSET['Æ'] = getJavaEncodingForMysqlEncoding("utf8", null);
/*     */       
/* 517 */       INDEX_TO_CHARSET['Ç'] = getJavaEncodingForMysqlEncoding("utf8", null);
/*     */       
/* 519 */       INDEX_TO_CHARSET['È'] = getJavaEncodingForMysqlEncoding("utf8", null);
/*     */       
/* 521 */       INDEX_TO_CHARSET['É'] = getJavaEncodingForMysqlEncoding("utf8", null);
/*     */       
/* 523 */       INDEX_TO_CHARSET['Ê'] = getJavaEncodingForMysqlEncoding("utf8", null);
/*     */       
/* 525 */       INDEX_TO_CHARSET['Ë'] = getJavaEncodingForMysqlEncoding("utf8", null);
/*     */       
/* 527 */       INDEX_TO_CHARSET['Ì'] = getJavaEncodingForMysqlEncoding("utf8", null);
/*     */       
/* 529 */       INDEX_TO_CHARSET['Í'] = getJavaEncodingForMysqlEncoding("utf8", null);
/*     */       
/* 531 */       INDEX_TO_CHARSET['Î'] = getJavaEncodingForMysqlEncoding("utf8", null);
/*     */       
/* 533 */       INDEX_TO_CHARSET['Ï'] = getJavaEncodingForMysqlEncoding("utf8", null);
/*     */       
/* 535 */       INDEX_TO_CHARSET['Ð'] = getJavaEncodingForMysqlEncoding("utf8", null);
/*     */       
/* 537 */       INDEX_TO_CHARSET['Ñ'] = getJavaEncodingForMysqlEncoding("utf8", null);
/*     */       
/* 539 */       INDEX_TO_CHARSET['Ò'] = getJavaEncodingForMysqlEncoding("utf8", null);
/*     */       
/*     */ 
/*     */ 
/*     */ 
/* 544 */       for (int i = 1; i < INDEX_TO_CHARSET.length; i++) {
/* 545 */         if (INDEX_TO_CHARSET[i] == null) {
/* 546 */           throw new RuntimeException("Assertion failure: No mapping from charset index " + i + " to a Java character set");
/*     */         }
/*     */       }
/*     */     }
/*     */     catch (SQLException sqlEx) {}
/*     */     
/*     */ 
/* 553 */     INDEX_TO_COLLATION = new String['Ó'];
/*     */     
/* 555 */     INDEX_TO_COLLATION[1] = "big5_chinese_ci";
/* 556 */     INDEX_TO_COLLATION[2] = "latin2_czech_cs";
/* 557 */     INDEX_TO_COLLATION[3] = "dec8_swedish_ci";
/* 558 */     INDEX_TO_COLLATION[4] = "cp850_general_ci";
/* 559 */     INDEX_TO_COLLATION[5] = "latin1_german1_ci";
/* 560 */     INDEX_TO_COLLATION[6] = "hp8_english_ci";
/* 561 */     INDEX_TO_COLLATION[7] = "koi8r_general_ci";
/* 562 */     INDEX_TO_COLLATION[8] = "latin1_swedish_ci";
/* 563 */     INDEX_TO_COLLATION[9] = "latin2_general_ci";
/* 564 */     INDEX_TO_COLLATION[10] = "swe7_swedish_ci";
/* 565 */     INDEX_TO_COLLATION[11] = "ascii_general_ci";
/* 566 */     INDEX_TO_COLLATION[12] = "ujis_japanese_ci";
/* 567 */     INDEX_TO_COLLATION[13] = "sjis_japanese_ci";
/* 568 */     INDEX_TO_COLLATION[14] = "cp1251_bulgarian_ci";
/* 569 */     INDEX_TO_COLLATION[15] = "latin1_danish_ci";
/* 570 */     INDEX_TO_COLLATION[16] = "hebrew_general_ci";
/* 571 */     INDEX_TO_COLLATION[18] = "tis620_thai_ci";
/* 572 */     INDEX_TO_COLLATION[19] = "euckr_korean_ci";
/* 573 */     INDEX_TO_COLLATION[20] = "latin7_estonian_cs";
/* 574 */     INDEX_TO_COLLATION[21] = "latin2_hungarian_ci";
/* 575 */     INDEX_TO_COLLATION[22] = "koi8u_general_ci";
/* 576 */     INDEX_TO_COLLATION[23] = "cp1251_ukrainian_ci";
/* 577 */     INDEX_TO_COLLATION[24] = "gb2312_chinese_ci";
/* 578 */     INDEX_TO_COLLATION[25] = "greek_general_ci";
/* 579 */     INDEX_TO_COLLATION[26] = "cp1250_general_ci";
/* 580 */     INDEX_TO_COLLATION[27] = "latin2_croatian_ci";
/* 581 */     INDEX_TO_COLLATION[28] = "gbk_chinese_ci";
/* 582 */     INDEX_TO_COLLATION[29] = "cp1257_lithuanian_ci";
/* 583 */     INDEX_TO_COLLATION[30] = "latin5_turkish_ci";
/* 584 */     INDEX_TO_COLLATION[31] = "latin1_german2_ci";
/* 585 */     INDEX_TO_COLLATION[32] = "armscii8_general_ci";
/* 586 */     INDEX_TO_COLLATION[33] = "utf8_general_ci";
/* 587 */     INDEX_TO_COLLATION[34] = "cp1250_czech_cs";
/* 588 */     INDEX_TO_COLLATION[35] = "ucs2_general_ci";
/* 589 */     INDEX_TO_COLLATION[36] = "cp866_general_ci";
/* 590 */     INDEX_TO_COLLATION[37] = "keybcs2_general_ci";
/* 591 */     INDEX_TO_COLLATION[38] = "macce_general_ci";
/* 592 */     INDEX_TO_COLLATION[39] = "macroman_general_ci";
/* 593 */     INDEX_TO_COLLATION[40] = "cp852_general_ci";
/* 594 */     INDEX_TO_COLLATION[41] = "latin7_general_ci";
/* 595 */     INDEX_TO_COLLATION[42] = "latin7_general_cs";
/* 596 */     INDEX_TO_COLLATION[43] = "macce_bin";
/* 597 */     INDEX_TO_COLLATION[44] = "cp1250_croatian_ci";
/* 598 */     INDEX_TO_COLLATION[47] = "latin1_bin";
/* 599 */     INDEX_TO_COLLATION[48] = "latin1_general_ci";
/* 600 */     INDEX_TO_COLLATION[49] = "latin1_general_cs";
/* 601 */     INDEX_TO_COLLATION[50] = "cp1251_bin";
/* 602 */     INDEX_TO_COLLATION[51] = "cp1251_general_ci";
/* 603 */     INDEX_TO_COLLATION[52] = "cp1251_general_cs";
/* 604 */     INDEX_TO_COLLATION[53] = "macroman_bin";
/* 605 */     INDEX_TO_COLLATION[57] = "cp1256_general_ci";
/* 606 */     INDEX_TO_COLLATION[58] = "cp1257_bin";
/* 607 */     INDEX_TO_COLLATION[59] = "cp1257_general_ci";
/* 608 */     INDEX_TO_COLLATION[63] = "binary";
/* 609 */     INDEX_TO_COLLATION[64] = "armscii8_bin";
/* 610 */     INDEX_TO_COLLATION[65] = "ascii_bin";
/* 611 */     INDEX_TO_COLLATION[66] = "cp1250_bin";
/* 612 */     INDEX_TO_COLLATION[67] = "cp1256_bin";
/* 613 */     INDEX_TO_COLLATION[68] = "cp866_bin";
/* 614 */     INDEX_TO_COLLATION[69] = "dec8_bin";
/* 615 */     INDEX_TO_COLLATION[70] = "greek_bin";
/* 616 */     INDEX_TO_COLLATION[71] = "hebrew_bin";
/* 617 */     INDEX_TO_COLLATION[72] = "hp8_bin";
/* 618 */     INDEX_TO_COLLATION[73] = "keybcs2_bin";
/* 619 */     INDEX_TO_COLLATION[74] = "koi8r_bin";
/* 620 */     INDEX_TO_COLLATION[75] = "koi8u_bin";
/* 621 */     INDEX_TO_COLLATION[77] = "latin2_bin";
/* 622 */     INDEX_TO_COLLATION[78] = "latin5_bin";
/* 623 */     INDEX_TO_COLLATION[79] = "latin7_bin";
/* 624 */     INDEX_TO_COLLATION[80] = "cp850_bin";
/* 625 */     INDEX_TO_COLLATION[81] = "cp852_bin";
/* 626 */     INDEX_TO_COLLATION[82] = "swe7_bin";
/* 627 */     INDEX_TO_COLLATION[83] = "utf8_bin";
/* 628 */     INDEX_TO_COLLATION[84] = "big5_bin";
/* 629 */     INDEX_TO_COLLATION[85] = "euckr_bin";
/* 630 */     INDEX_TO_COLLATION[86] = "gb2312_bin";
/* 631 */     INDEX_TO_COLLATION[87] = "gbk_bin";
/* 632 */     INDEX_TO_COLLATION[88] = "sjis_bin";
/* 633 */     INDEX_TO_COLLATION[89] = "tis620_bin";
/* 634 */     INDEX_TO_COLLATION[90] = "ucs2_bin";
/* 635 */     INDEX_TO_COLLATION[91] = "ujis_bin";
/* 636 */     INDEX_TO_COLLATION[92] = "geostd8_general_ci";
/* 637 */     INDEX_TO_COLLATION[93] = "geostd8_bin";
/* 638 */     INDEX_TO_COLLATION[94] = "latin1_spanish_ci";
/* 639 */     INDEX_TO_COLLATION[95] = "cp932_japanese_ci";
/* 640 */     INDEX_TO_COLLATION[96] = "cp932_bin";
/* 641 */     INDEX_TO_COLLATION[97] = "eucjpms_japanese_ci";
/* 642 */     INDEX_TO_COLLATION[98] = "eucjpms_bin";
/* 643 */     INDEX_TO_COLLATION[99] = "cp1250_polish_ci";
/* 644 */     INDEX_TO_COLLATION[''] = "ucs2_unicode_ci";
/* 645 */     INDEX_TO_COLLATION[''] = "ucs2_icelandic_ci";
/* 646 */     INDEX_TO_COLLATION[''] = "ucs2_latvian_ci";
/* 647 */     INDEX_TO_COLLATION[''] = "ucs2_romanian_ci";
/* 648 */     INDEX_TO_COLLATION[''] = "ucs2_slovenian_ci";
/* 649 */     INDEX_TO_COLLATION[''] = "ucs2_polish_ci";
/* 650 */     INDEX_TO_COLLATION[''] = "ucs2_estonian_ci";
/* 651 */     INDEX_TO_COLLATION[''] = "ucs2_spanish_ci";
/* 652 */     INDEX_TO_COLLATION[''] = "ucs2_swedish_ci";
/* 653 */     INDEX_TO_COLLATION[''] = "ucs2_turkish_ci";
/* 654 */     INDEX_TO_COLLATION[''] = "ucs2_czech_ci";
/* 655 */     INDEX_TO_COLLATION[''] = "ucs2_danish_ci";
/* 656 */     INDEX_TO_COLLATION[''] = "ucs2_lithuanian_ci ";
/* 657 */     INDEX_TO_COLLATION[''] = "ucs2_slovak_ci";
/* 658 */     INDEX_TO_COLLATION[''] = "ucs2_spanish2_ci";
/* 659 */     INDEX_TO_COLLATION[''] = "ucs2_roman_ci";
/* 660 */     INDEX_TO_COLLATION[''] = "ucs2_persian_ci";
/* 661 */     INDEX_TO_COLLATION[''] = "ucs2_esperanto_ci";
/* 662 */     INDEX_TO_COLLATION[''] = "ucs2_hungarian_ci";
/* 663 */     INDEX_TO_COLLATION['À'] = "utf8_unicode_ci";
/* 664 */     INDEX_TO_COLLATION['Á'] = "utf8_icelandic_ci";
/* 665 */     INDEX_TO_COLLATION['Â'] = "utf8_latvian_ci";
/* 666 */     INDEX_TO_COLLATION['Ã'] = "utf8_romanian_ci";
/* 667 */     INDEX_TO_COLLATION['Ä'] = "utf8_slovenian_ci";
/* 668 */     INDEX_TO_COLLATION['Å'] = "utf8_polish_ci";
/* 669 */     INDEX_TO_COLLATION['Æ'] = "utf8_estonian_ci";
/* 670 */     INDEX_TO_COLLATION['Ç'] = "utf8_spanish_ci";
/* 671 */     INDEX_TO_COLLATION['È'] = "utf8_swedish_ci";
/* 672 */     INDEX_TO_COLLATION['É'] = "utf8_turkish_ci";
/* 673 */     INDEX_TO_COLLATION['Ê'] = "utf8_czech_ci";
/* 674 */     INDEX_TO_COLLATION['Ë'] = "utf8_danish_ci";
/* 675 */     INDEX_TO_COLLATION['Ì'] = "utf8_lithuanian_ci ";
/* 676 */     INDEX_TO_COLLATION['Í'] = "utf8_slovak_ci";
/* 677 */     INDEX_TO_COLLATION['Î'] = "utf8_spanish2_ci";
/* 678 */     INDEX_TO_COLLATION['Ï'] = "utf8_roman_ci";
/* 679 */     INDEX_TO_COLLATION['Ð'] = "utf8_persian_ci";
/* 680 */     INDEX_TO_COLLATION['Ñ'] = "utf8_esperanto_ci";
/* 681 */     INDEX_TO_COLLATION['Ò'] = "utf8_hungarian_ci";
/*     */     
/* 683 */     Map tempMap = new HashMap();
/*     */     
/* 685 */     tempMap.put("czech", "latin2");
/* 686 */     tempMap.put("danish", "latin1");
/* 687 */     tempMap.put("dutch", "latin1");
/* 688 */     tempMap.put("english", "latin1");
/* 689 */     tempMap.put("estonian", "latin7");
/* 690 */     tempMap.put("french", "latin1");
/* 691 */     tempMap.put("german", "latin1");
/* 692 */     tempMap.put("greek", "greek");
/* 693 */     tempMap.put("hungarian", "latin2");
/* 694 */     tempMap.put("italian", "latin1");
/* 695 */     tempMap.put("japanese", "ujis");
/* 696 */     tempMap.put("japanese-sjis", "sjis");
/* 697 */     tempMap.put("korean", "euckr");
/* 698 */     tempMap.put("norwegian", "latin1");
/* 699 */     tempMap.put("norwegian-ny", "latin1");
/* 700 */     tempMap.put("polish", "latin2");
/* 701 */     tempMap.put("portuguese", "latin1");
/* 702 */     tempMap.put("romanian", "latin2");
/* 703 */     tempMap.put("russian", "koi8r");
/* 704 */     tempMap.put("serbian", "cp1250");
/* 705 */     tempMap.put("slovak", "latin2");
/* 706 */     tempMap.put("spanish", "latin1");
/* 707 */     tempMap.put("swedish", "latin1");
/* 708 */     tempMap.put("ukrainian", "koi8u");
/*     */     
/* 710 */     ERROR_MESSAGE_FILE_TO_MYSQL_CHARSET_MAP = Collections.unmodifiableMap(tempMap);
/*     */   }
/*     */   
/*     */ 
/*     */   public static final String getJavaEncodingForMysqlEncoding(String mysqlEncoding, Connection conn)
/*     */     throws SQLException
/*     */   {
/* 717 */     if ((conn != null) && (conn.versionMeetsMinimum(4, 1, 0)) && ("latin1".equalsIgnoreCase(mysqlEncoding)))
/*     */     {
/* 719 */       return "Cp1252";
/*     */     }
/*     */     
/* 722 */     return (String)MYSQL_TO_JAVA_CHARSET_MAP.get(mysqlEncoding);
/*     */   }
/*     */   
/*     */   public static final String getMysqlEncodingForJavaEncoding(String javaEncodingUC, Connection conn) throws SQLException
/*     */   {
/* 727 */     List mysqlEncodings = (List)JAVA_UC_TO_MYSQL_CHARSET_MAP.get(javaEncodingUC);
/*     */     
/*     */ 
/*     */ 
/* 731 */     if (mysqlEncodings != null) {
/* 732 */       Iterator iter = mysqlEncodings.iterator();
/*     */       
/* 734 */       VersionedStringProperty versionedProp = null;
/*     */       
/* 736 */       while (iter.hasNext()) {
/* 737 */         VersionedStringProperty propToCheck = (VersionedStringProperty)iter.next();
/*     */         
/*     */ 
/* 740 */         if (conn == null)
/*     */         {
/*     */ 
/* 743 */           return propToCheck.toString();
/*     */         }
/*     */         
/* 746 */         if ((versionedProp != null) && (!versionedProp.preferredValue) && 
/* 747 */           (versionedProp.majorVersion == propToCheck.majorVersion) && (versionedProp.minorVersion == propToCheck.minorVersion) && (versionedProp.subminorVersion == propToCheck.subminorVersion))
/*     */         {
/*     */ 
/* 750 */           return versionedProp.toString();
/*     */         }
/*     */         
/*     */ 
/* 754 */         if (!propToCheck.isOkayForVersion(conn)) break;
/* 755 */         if (propToCheck.preferredValue) {
/* 756 */           return propToCheck.toString();
/*     */         }
/*     */         
/* 759 */         versionedProp = propToCheck;
/*     */       }
/*     */       
/*     */ 
/*     */ 
/*     */ 
/* 765 */       if (versionedProp != null) {
/* 766 */         return versionedProp.toString();
/*     */       }
/*     */     }
/*     */     
/* 770 */     return null;
/*     */   }
/*     */   
/*     */   static final int getNumberOfCharsetsConfigured() {
/* 774 */     return MYSQL_TO_JAVA_CHARSET_MAP.size() / 2;
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
/*     */   static final String getCharacterEncodingForErrorMessages(Connection conn)
/*     */     throws SQLException
/*     */   {
/* 790 */     String errorMessageFile = conn.getServerVariable("language");
/*     */     
/* 792 */     if ((errorMessageFile == null) || (errorMessageFile.length() == 0))
/*     */     {
/* 794 */       return "Cp1252";
/*     */     }
/*     */     
/* 797 */     int endWithoutSlash = errorMessageFile.length();
/*     */     
/* 799 */     if ((errorMessageFile.endsWith("/")) || (errorMessageFile.endsWith("\\"))) {
/* 800 */       endWithoutSlash--;
/*     */     }
/*     */     
/* 803 */     int lastSlashIndex = errorMessageFile.lastIndexOf('/', endWithoutSlash - 1);
/*     */     
/* 805 */     if (lastSlashIndex == -1) {
/* 806 */       lastSlashIndex = errorMessageFile.lastIndexOf('\\', endWithoutSlash - 1);
/*     */     }
/*     */     
/* 809 */     if (lastSlashIndex == -1) {
/* 810 */       lastSlashIndex = 0;
/*     */     }
/*     */     
/* 813 */     if ((lastSlashIndex == endWithoutSlash) || (endWithoutSlash < lastSlashIndex))
/*     */     {
/* 815 */       return "Cp1252";
/*     */     }
/*     */     
/* 818 */     errorMessageFile = errorMessageFile.substring(lastSlashIndex + 1, endWithoutSlash);
/*     */     
/* 820 */     String errorMessageEncodingMysql = (String)ERROR_MESSAGE_FILE_TO_MYSQL_CHARSET_MAP.get(errorMessageFile);
/*     */     
/* 822 */     if (errorMessageEncodingMysql == null)
/*     */     {
/* 824 */       return "Cp1252";
/*     */     }
/*     */     
/* 827 */     String javaEncoding = getJavaEncodingForMysqlEncoding(errorMessageEncodingMysql, conn);
/*     */     
/* 829 */     if (javaEncoding == null)
/*     */     {
/* 831 */       return "Cp1252";
/*     */     }
/*     */     
/* 834 */     return javaEncoding;
/*     */   }
/*     */   
/*     */   static final boolean isAliasForSjis(String encoding) {
/* 838 */     return ("SJIS".equalsIgnoreCase(encoding)) || ("WINDOWS-31J".equalsIgnoreCase(encoding)) || ("MS932".equalsIgnoreCase(encoding)) || ("SHIFT_JIS".equalsIgnoreCase(encoding)) || ("CP943".equalsIgnoreCase(encoding));
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   static final boolean isMultibyteCharset(String javaEncodingName)
/*     */   {
/* 847 */     String javaEncodingNameUC = javaEncodingName.toUpperCase(Locale.ENGLISH);
/*     */     
/*     */ 
/* 850 */     return MULTIBYTE_CHARSETS.containsKey(javaEncodingNameUC);
/*     */   }
/*     */   
/*     */ 
/*     */   private static void populateMapWithKeyValuePairs(String configKey, Map mapToPopulate, boolean addVersionedProperties, boolean addUppercaseKeys)
/*     */   {
/* 856 */     String javaToMysqlConfig = CHARSET_CONFIG.getProperty(configKey);
/*     */     
/* 858 */     if (javaToMysqlConfig != null) {
/* 859 */       List mappings = StringUtils.split(javaToMysqlConfig, ",", true);
/*     */       
/* 861 */       if (mappings != null) {
/* 862 */         Iterator mappingsIter = mappings.iterator();
/*     */         
/* 864 */         while (mappingsIter.hasNext()) {
/* 865 */           String aMapping = (String)mappingsIter.next();
/*     */           
/* 867 */           List parsedPair = StringUtils.split(aMapping, "=", true);
/*     */           
/* 869 */           if (parsedPair.size() == 2) {
/* 870 */             String key = parsedPair.get(0).toString();
/* 871 */             String value = parsedPair.get(1).toString();
/*     */             
/* 873 */             if (addVersionedProperties) {
/* 874 */               List versionedProperties = (List)mapToPopulate.get(key);
/*     */               
/*     */ 
/* 877 */               if (versionedProperties == null) {
/* 878 */                 versionedProperties = new ArrayList();
/* 879 */                 mapToPopulate.put(key, versionedProperties);
/*     */               }
/*     */               
/* 882 */               VersionedStringProperty verProp = new VersionedStringProperty(value);
/*     */               
/* 884 */               versionedProperties.add(verProp);
/*     */               
/* 886 */               if (addUppercaseKeys) {
/* 887 */                 String keyUc = key.toUpperCase(Locale.ENGLISH);
/*     */                 
/* 889 */                 versionedProperties = (List)mapToPopulate.get(keyUc);
/*     */                 
/*     */ 
/* 892 */                 if (versionedProperties == null) {
/* 893 */                   versionedProperties = new ArrayList();
/* 894 */                   mapToPopulate.put(keyUc, versionedProperties);
/*     */                 }
/*     */                 
/*     */ 
/* 898 */                 versionedProperties.add(verProp);
/*     */               }
/*     */             } else {
/* 901 */               mapToPopulate.put(key, value);
/*     */               
/* 903 */               if (addUppercaseKeys) {
/* 904 */                 mapToPopulate.put(key.toUpperCase(Locale.ENGLISH), value);
/*     */               }
/*     */             }
/*     */           }
/*     */           else {
/* 909 */             throw new RuntimeException("Syntax error in Charsets.properties resource for token \"" + aMapping + "\".");
/*     */           }
/*     */           
/*     */         }
/*     */       }
/*     */       else
/*     */       {
/* 916 */         throw new RuntimeException("Missing/corrupt entry for \"" + configKey + "\" in Charsets.properties.");
/*     */       }
/*     */     }
/*     */     else {
/* 920 */       throw new RuntimeException("Could not find configuration value \"" + configKey + "\" in Charsets.properties resource");
/*     */     }
/*     */   }
/*     */ }


/* Location:              E:\java\java学习\hutubill\lib\all.jar!\com\mysql\jdbc\CharsetMapping.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version:       0.7.1
 */
/*     */ package com.mysql.jdbc;
/*     */ 
/*     */ import java.util.HashMap;
/*     */ import java.util.Iterator;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ final class MysqlDefs
/*     */ {
/*     */   static final int COM_BINLOG_DUMP = 18;
/*     */   static final int COM_CHANGE_USER = 17;
/*     */   static final int COM_CLOSE_STATEMENT = 25;
/*     */   static final int COM_CONNECT_OUT = 20;
/*     */   static final int COM_END = 29;
/*     */   static final int COM_EXECUTE = 23;
/*     */   static final int COM_FETCH = 28;
/*     */   static final int COM_LONG_DATA = 24;
/*     */   static final int COM_PREPARE = 22;
/*     */   static final int COM_REGISTER_SLAVE = 21;
/*     */   static final int COM_RESET_STMT = 26;
/*     */   static final int COM_SET_OPTION = 27;
/*     */   static final int COM_TABLE_DUMP = 19;
/*     */   static final int CONNECT = 11;
/*     */   static final int CREATE_DB = 5;
/*     */   static final int DEBUG = 13;
/*     */   static final int DELAYED_INSERT = 16;
/*     */   static final int DROP_DB = 6;
/*     */   static final int FIELD_LIST = 4;
/*     */   static final int FIELD_TYPE_BIT = 16;
/*     */   static final int FIELD_TYPE_BLOB = 252;
/*     */   static final int FIELD_TYPE_DATE = 10;
/*     */   static final int FIELD_TYPE_DATETIME = 12;
/*     */   static final int FIELD_TYPE_DECIMAL = 0;
/*     */   static final int FIELD_TYPE_DOUBLE = 5;
/*     */   static final int FIELD_TYPE_ENUM = 247;
/*     */   static final int FIELD_TYPE_FLOAT = 4;
/*     */   static final int FIELD_TYPE_GEOMETRY = 255;
/*     */   static final int FIELD_TYPE_INT24 = 9;
/*     */   static final int FIELD_TYPE_LONG = 3;
/*     */   static final int FIELD_TYPE_LONG_BLOB = 251;
/*     */   static final int FIELD_TYPE_LONGLONG = 8;
/*     */   static final int FIELD_TYPE_MEDIUM_BLOB = 250;
/*     */   static final int FIELD_TYPE_NEW_DECIMAL = 246;
/*     */   static final int FIELD_TYPE_NEWDATE = 14;
/*     */   static final int FIELD_TYPE_NULL = 6;
/*     */   static final int FIELD_TYPE_SET = 248;
/*     */   static final int FIELD_TYPE_SHORT = 2;
/*     */   static final int FIELD_TYPE_STRING = 254;
/*     */   static final int FIELD_TYPE_TIME = 11;
/*     */   static final int FIELD_TYPE_TIMESTAMP = 7;
/*     */   static final int FIELD_TYPE_TINY = 1;
/*     */   static final int FIELD_TYPE_TINY_BLOB = 249;
/*     */   static final int FIELD_TYPE_VAR_STRING = 253;
/*     */   static final int FIELD_TYPE_VARCHAR = 15;
/*     */   static final int FIELD_TYPE_YEAR = 13;
/*     */   static final int INIT_DB = 2;
/*     */   static final long LENGTH_BLOB = 65535L;
/*     */   static final long LENGTH_LONGBLOB = 4294967295L;
/*     */   static final long LENGTH_MEDIUMBLOB = 16777215L;
/*     */   static final long LENGTH_TINYBLOB = 255L;
/*     */   static final int MAX_ROWS = 50000000;
/*     */   public static final int NO_CHARSET_INFO = -1;
/*     */   static final byte OPEN_CURSOR_FLAG = 1;
/*     */   static final int PING = 14;
/*     */   static final int PROCESS_INFO = 10;
/*     */   static final int PROCESS_KILL = 12;
/*     */   static final int QUERY = 3;
/*     */   static final int QUIT = 1;
/*     */   static final int RELOAD = 7;
/*     */   static final int SHUTDOWN = 8;
/*     */   static final int SLEEP = 0;
/*     */   static final int STATISTICS = 9;
/*     */   static final int TIME = 15;
/*     */   
/*     */   static int mysqlToJavaType(int mysqlType)
/*     */   {
/*     */     int jdbcType;
/*     */     int jdbcType;
/*     */     int jdbcType;
/*     */     int jdbcType;
/*     */     int jdbcType;
/*     */     int jdbcType;
/*     */     int jdbcType;
/*     */     int jdbcType;
/*     */     int jdbcType;
/*     */     int jdbcType;
/*     */     int jdbcType;
/*     */     int jdbcType;
/*     */     int jdbcType;
/*     */     int jdbcType;
/*     */     int jdbcType;
/*     */     int jdbcType;
/*     */     int jdbcType;
/*     */     int jdbcType;
/*     */     int jdbcType;
/*     */     int jdbcType;
/*     */     int jdbcType;
/*     */     int jdbcType;
/*     */     int jdbcType;
/*     */     int jdbcType;
/*     */     int jdbcType;
/*     */     int jdbcType;
/* 194 */     switch (mysqlType) {
/*     */     case 0: 
/*     */     case 246: 
/* 197 */       jdbcType = 3;
/*     */       
/* 199 */       break;
/*     */     
/*     */     case 1: 
/* 202 */       jdbcType = -6;
/*     */       
/* 204 */       break;
/*     */     
/*     */     case 2: 
/* 207 */       jdbcType = 5;
/*     */       
/* 209 */       break;
/*     */     
/*     */     case 3: 
/* 212 */       jdbcType = 4;
/*     */       
/* 214 */       break;
/*     */     
/*     */     case 4: 
/* 217 */       jdbcType = 7;
/*     */       
/* 219 */       break;
/*     */     
/*     */     case 5: 
/* 222 */       jdbcType = 8;
/*     */       
/* 224 */       break;
/*     */     
/*     */     case 6: 
/* 227 */       jdbcType = 0;
/*     */       
/* 229 */       break;
/*     */     
/*     */     case 7: 
/* 232 */       jdbcType = 93;
/*     */       
/* 234 */       break;
/*     */     
/*     */     case 8: 
/* 237 */       jdbcType = -5;
/*     */       
/* 239 */       break;
/*     */     
/*     */     case 9: 
/* 242 */       jdbcType = 4;
/*     */       
/* 244 */       break;
/*     */     
/*     */     case 10: 
/* 247 */       jdbcType = 91;
/*     */       
/* 249 */       break;
/*     */     
/*     */     case 11: 
/* 252 */       jdbcType = 92;
/*     */       
/* 254 */       break;
/*     */     
/*     */     case 12: 
/* 257 */       jdbcType = 93;
/*     */       
/* 259 */       break;
/*     */     
/*     */     case 13: 
/* 262 */       jdbcType = 91;
/*     */       
/* 264 */       break;
/*     */     
/*     */     case 14: 
/* 267 */       jdbcType = 91;
/*     */       
/* 269 */       break;
/*     */     
/*     */     case 247: 
/* 272 */       jdbcType = 1;
/*     */       
/* 274 */       break;
/*     */     
/*     */     case 248: 
/* 277 */       jdbcType = 1;
/*     */       
/* 279 */       break;
/*     */     
/*     */     case 249: 
/* 282 */       jdbcType = -3;
/*     */       
/* 284 */       break;
/*     */     
/*     */     case 250: 
/* 287 */       jdbcType = -4;
/*     */       
/* 289 */       break;
/*     */     
/*     */     case 251: 
/* 292 */       jdbcType = -4;
/*     */       
/* 294 */       break;
/*     */     
/*     */     case 252: 
/* 297 */       jdbcType = -4;
/*     */       
/* 299 */       break;
/*     */     
/*     */     case 15: 
/*     */     case 253: 
/* 303 */       jdbcType = 12;
/*     */       
/* 305 */       break;
/*     */     
/*     */     case 254: 
/* 308 */       jdbcType = 1;
/*     */       
/* 310 */       break;
/*     */     case 255: 
/* 312 */       jdbcType = -2;
/*     */       
/* 314 */       break;
/*     */     case 16: 
/* 316 */       jdbcType = -7;
/*     */       
/* 318 */       break;
/*     */     default: 
/* 320 */       jdbcType = 12;
/*     */     }
/*     */     
/* 323 */     return jdbcType;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   static int mysqlToJavaType(String mysqlType)
/*     */   {
/* 330 */     if (mysqlType.equalsIgnoreCase("BIT"))
/* 331 */       return mysqlToJavaType(16);
/* 332 */     if (mysqlType.equalsIgnoreCase("TINYINT"))
/* 333 */       return mysqlToJavaType(1);
/* 334 */     if (mysqlType.equalsIgnoreCase("SMALLINT"))
/* 335 */       return mysqlToJavaType(2);
/* 336 */     if (mysqlType.equalsIgnoreCase("MEDIUMINT"))
/* 337 */       return mysqlToJavaType(9);
/* 338 */     if ((mysqlType.equalsIgnoreCase("INT")) || (mysqlType.equalsIgnoreCase("INTEGER")))
/* 339 */       return mysqlToJavaType(3);
/* 340 */     if (mysqlType.equalsIgnoreCase("BIGINT"))
/* 341 */       return mysqlToJavaType(8);
/* 342 */     if (mysqlType.equalsIgnoreCase("INT24"))
/* 343 */       return mysqlToJavaType(9);
/* 344 */     if (mysqlType.equalsIgnoreCase("REAL"))
/* 345 */       return mysqlToJavaType(5);
/* 346 */     if (mysqlType.equalsIgnoreCase("FLOAT"))
/* 347 */       return mysqlToJavaType(4);
/* 348 */     if (mysqlType.equalsIgnoreCase("DECIMAL"))
/* 349 */       return mysqlToJavaType(0);
/* 350 */     if (mysqlType.equalsIgnoreCase("NUMERIC"))
/* 351 */       return mysqlToJavaType(0);
/* 352 */     if (mysqlType.equalsIgnoreCase("DOUBLE"))
/* 353 */       return mysqlToJavaType(5);
/* 354 */     if (mysqlType.equalsIgnoreCase("CHAR"))
/* 355 */       return mysqlToJavaType(254);
/* 356 */     if (mysqlType.equalsIgnoreCase("VARCHAR"))
/* 357 */       return mysqlToJavaType(253);
/* 358 */     if (mysqlType.equalsIgnoreCase("DATE"))
/* 359 */       return mysqlToJavaType(10);
/* 360 */     if (mysqlType.equalsIgnoreCase("TIME"))
/* 361 */       return mysqlToJavaType(11);
/* 362 */     if (mysqlType.equalsIgnoreCase("YEAR"))
/* 363 */       return mysqlToJavaType(13);
/* 364 */     if (mysqlType.equalsIgnoreCase("TIMESTAMP"))
/* 365 */       return mysqlToJavaType(7);
/* 366 */     if (mysqlType.equalsIgnoreCase("DATETIME"))
/* 367 */       return mysqlToJavaType(12);
/* 368 */     if (mysqlType.equalsIgnoreCase("TINYBLOB"))
/* 369 */       return -2;
/* 370 */     if (mysqlType.equalsIgnoreCase("BLOB"))
/* 371 */       return -4;
/* 372 */     if (mysqlType.equalsIgnoreCase("MEDIUMBLOB"))
/* 373 */       return -4;
/* 374 */     if (mysqlType.equalsIgnoreCase("LONGBLOB"))
/* 375 */       return -4;
/* 376 */     if (mysqlType.equalsIgnoreCase("TINYTEXT"))
/* 377 */       return 12;
/* 378 */     if (mysqlType.equalsIgnoreCase("TEXT"))
/* 379 */       return -1;
/* 380 */     if (mysqlType.equalsIgnoreCase("MEDIUMTEXT"))
/* 381 */       return -1;
/* 382 */     if (mysqlType.equalsIgnoreCase("LONGTEXT"))
/* 383 */       return -1;
/* 384 */     if (mysqlType.equalsIgnoreCase("ENUM"))
/* 385 */       return mysqlToJavaType(247);
/* 386 */     if (mysqlType.equalsIgnoreCase("SET"))
/* 387 */       return mysqlToJavaType(248);
/* 388 */     if (mysqlType.equalsIgnoreCase("GEOMETRY"))
/* 389 */       return mysqlToJavaType(255);
/* 390 */     if (mysqlType.equalsIgnoreCase("BINARY"))
/* 391 */       return -2;
/* 392 */     if (mysqlType.equalsIgnoreCase("VARBINARY"))
/* 393 */       return -3;
/* 394 */     if (mysqlType.equalsIgnoreCase("BIT")) {
/* 395 */       return mysqlToJavaType(16);
/*     */     }
/*     */     
/*     */ 
/* 399 */     return 1111;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public static String typeToName(int mysqlType)
/*     */   {
/* 407 */     switch (mysqlType) {
/*     */     case 0: 
/* 409 */       return "FIELD_TYPE_DECIMAL";
/*     */     
/*     */     case 1: 
/* 412 */       return "FIELD_TYPE_TINY";
/*     */     
/*     */     case 2: 
/* 415 */       return "FIELD_TYPE_SHORT";
/*     */     
/*     */     case 3: 
/* 418 */       return "FIELD_TYPE_LONG";
/*     */     
/*     */     case 4: 
/* 421 */       return "FIELD_TYPE_FLOAT";
/*     */     
/*     */     case 5: 
/* 424 */       return "FIELD_TYPE_DOUBLE";
/*     */     
/*     */     case 6: 
/* 427 */       return "FIELD_TYPE_NULL";
/*     */     
/*     */     case 7: 
/* 430 */       return "FIELD_TYPE_TIMESTAMP";
/*     */     
/*     */     case 8: 
/* 433 */       return "FIELD_TYPE_LONGLONG";
/*     */     
/*     */     case 9: 
/* 436 */       return "FIELD_TYPE_INT24";
/*     */     
/*     */     case 10: 
/* 439 */       return "FIELD_TYPE_DATE";
/*     */     
/*     */     case 11: 
/* 442 */       return "FIELD_TYPE_TIME";
/*     */     
/*     */     case 12: 
/* 445 */       return "FIELD_TYPE_DATETIME";
/*     */     
/*     */     case 13: 
/* 448 */       return "FIELD_TYPE_YEAR";
/*     */     
/*     */     case 14: 
/* 451 */       return "FIELD_TYPE_NEWDATE";
/*     */     
/*     */     case 247: 
/* 454 */       return "FIELD_TYPE_ENUM";
/*     */     
/*     */     case 248: 
/* 457 */       return "FIELD_TYPE_SET";
/*     */     
/*     */     case 249: 
/* 460 */       return "FIELD_TYPE_TINY_BLOB";
/*     */     
/*     */     case 250: 
/* 463 */       return "FIELD_TYPE_MEDIUM_BLOB";
/*     */     
/*     */     case 251: 
/* 466 */       return "FIELD_TYPE_LONG_BLOB";
/*     */     
/*     */     case 252: 
/* 469 */       return "FIELD_TYPE_BLOB";
/*     */     
/*     */     case 253: 
/* 472 */       return "FIELD_TYPE_VAR_STRING";
/*     */     
/*     */     case 254: 
/* 475 */       return "FIELD_TYPE_STRING";
/*     */     
/*     */     case 15: 
/* 478 */       return "FIELD_TYPE_VARCHAR";
/*     */     
/*     */     case 255: 
/* 481 */       return "FIELD_TYPE_GEOMETRY";
/*     */     }
/*     */     
/* 484 */     return " Unknown MySQL Type # " + mysqlType;
/*     */   }
/*     */   
/*     */ 
/* 488 */   private static Map mysqlToJdbcTypesMap = new HashMap();
/*     */   
/*     */   static {
/* 491 */     mysqlToJdbcTypesMap.put("BIT", new Integer(mysqlToJavaType(16)));
/*     */     
/*     */ 
/* 494 */     mysqlToJdbcTypesMap.put("TINYINT", new Integer(mysqlToJavaType(1)));
/*     */     
/* 496 */     mysqlToJdbcTypesMap.put("SMALLINT", new Integer(mysqlToJavaType(2)));
/*     */     
/* 498 */     mysqlToJdbcTypesMap.put("MEDIUMINT", new Integer(mysqlToJavaType(9)));
/*     */     
/* 500 */     mysqlToJdbcTypesMap.put("INT", new Integer(mysqlToJavaType(3)));
/*     */     
/* 502 */     mysqlToJdbcTypesMap.put("INTEGER", new Integer(mysqlToJavaType(3)));
/*     */     
/* 504 */     mysqlToJdbcTypesMap.put("BIGINT", new Integer(mysqlToJavaType(8)));
/*     */     
/* 506 */     mysqlToJdbcTypesMap.put("INT24", new Integer(mysqlToJavaType(9)));
/*     */     
/* 508 */     mysqlToJdbcTypesMap.put("REAL", new Integer(mysqlToJavaType(5)));
/*     */     
/* 510 */     mysqlToJdbcTypesMap.put("FLOAT", new Integer(mysqlToJavaType(4)));
/*     */     
/* 512 */     mysqlToJdbcTypesMap.put("DECIMAL", new Integer(mysqlToJavaType(0)));
/*     */     
/* 514 */     mysqlToJdbcTypesMap.put("NUMERIC", new Integer(mysqlToJavaType(0)));
/*     */     
/* 516 */     mysqlToJdbcTypesMap.put("DOUBLE", new Integer(mysqlToJavaType(5)));
/*     */     
/* 518 */     mysqlToJdbcTypesMap.put("CHAR", new Integer(mysqlToJavaType(254)));
/*     */     
/* 520 */     mysqlToJdbcTypesMap.put("VARCHAR", new Integer(mysqlToJavaType(253)));
/*     */     
/* 522 */     mysqlToJdbcTypesMap.put("DATE", new Integer(mysqlToJavaType(10)));
/*     */     
/* 524 */     mysqlToJdbcTypesMap.put("TIME", new Integer(mysqlToJavaType(11)));
/*     */     
/* 526 */     mysqlToJdbcTypesMap.put("YEAR", new Integer(mysqlToJavaType(13)));
/*     */     
/* 528 */     mysqlToJdbcTypesMap.put("TIMESTAMP", new Integer(mysqlToJavaType(7)));
/*     */     
/* 530 */     mysqlToJdbcTypesMap.put("DATETIME", new Integer(mysqlToJavaType(12)));
/*     */     
/* 532 */     mysqlToJdbcTypesMap.put("TINYBLOB", new Integer(-2));
/* 533 */     mysqlToJdbcTypesMap.put("BLOB", new Integer(-4));
/*     */     
/* 535 */     mysqlToJdbcTypesMap.put("MEDIUMBLOB", new Integer(-4));
/*     */     
/* 537 */     mysqlToJdbcTypesMap.put("LONGBLOB", new Integer(-4));
/*     */     
/* 539 */     mysqlToJdbcTypesMap.put("TINYTEXT", new Integer(12));
/*     */     
/* 541 */     mysqlToJdbcTypesMap.put("TEXT", new Integer(-1));
/*     */     
/* 543 */     mysqlToJdbcTypesMap.put("MEDIUMTEXT", new Integer(-1));
/*     */     
/* 545 */     mysqlToJdbcTypesMap.put("LONGTEXT", new Integer(-1));
/*     */     
/* 547 */     mysqlToJdbcTypesMap.put("ENUM", new Integer(mysqlToJavaType(247)));
/*     */     
/* 549 */     mysqlToJdbcTypesMap.put("SET", new Integer(mysqlToJavaType(248)));
/*     */     
/* 551 */     mysqlToJdbcTypesMap.put("GEOMETRY", new Integer(mysqlToJavaType(255)));
/*     */   }
/*     */   
/*     */ 
/*     */   static final void appendJdbcTypeMappingQuery(StringBuffer buf, String mysqlTypeColumnName)
/*     */   {
/* 557 */     buf.append("CASE ");
/* 558 */     Map typesMap = new HashMap();
/* 559 */     typesMap.putAll(mysqlToJdbcTypesMap);
/* 560 */     typesMap.put("BINARY", new Integer(-2));
/* 561 */     typesMap.put("VARBINARY", new Integer(-3));
/*     */     
/* 563 */     Iterator mysqlTypes = typesMap.keySet().iterator();
/*     */     
/* 565 */     while (mysqlTypes.hasNext()) {
/* 566 */       String mysqlTypeName = (String)mysqlTypes.next();
/* 567 */       buf.append(" WHEN ");
/* 568 */       buf.append(mysqlTypeColumnName);
/* 569 */       buf.append("='");
/* 570 */       buf.append(mysqlTypeName);
/* 571 */       buf.append("' THEN ");
/* 572 */       buf.append(typesMap.get(mysqlTypeName));
/*     */       
/* 574 */       if ((mysqlTypeName.equalsIgnoreCase("DOUBLE")) || (mysqlTypeName.equalsIgnoreCase("FLOAT")) || (mysqlTypeName.equalsIgnoreCase("DECIMAL")) || (mysqlTypeName.equalsIgnoreCase("NUMERIC")))
/*     */       {
/*     */ 
/*     */ 
/* 578 */         buf.append(" WHEN ");
/* 579 */         buf.append(mysqlTypeColumnName);
/* 580 */         buf.append("='");
/* 581 */         buf.append(mysqlTypeName);
/* 582 */         buf.append(" unsigned' THEN ");
/* 583 */         buf.append(typesMap.get(mysqlTypeName));
/*     */       }
/*     */     }
/*     */     
/* 587 */     buf.append(" ELSE ");
/* 588 */     buf.append(1111);
/* 589 */     buf.append(" END ");
/*     */   }
/*     */ }


/* Location:              E:\java\java学习\hutubill\lib\all.jar!\com\mysql\jdbc\MysqlDefs.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version:       0.7.1
 */
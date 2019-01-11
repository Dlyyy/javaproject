/*     */ package com.mysql.jdbc;
/*     */ 
/*     */ import com.mysql.jdbc.exceptions.MySQLDataException;
/*     */ import com.mysql.jdbc.exceptions.MySQLIntegrityConstraintViolationException;
/*     */ import com.mysql.jdbc.exceptions.MySQLNonTransientConnectionException;
/*     */ import com.mysql.jdbc.exceptions.MySQLSyntaxErrorException;
/*     */ import com.mysql.jdbc.exceptions.MySQLTransactionRollbackException;
/*     */ import java.io.PrintStream;
/*     */ import java.lang.reflect.Field;
/*     */ import java.sql.DataTruncation;
/*     */ import java.sql.ResultSet;
/*     */ import java.sql.SQLException;
/*     */ import java.sql.SQLWarning;
/*     */ import java.sql.Statement;
/*     */ import java.util.HashMap;
/*     */ import java.util.Hashtable;
/*     */ import java.util.Iterator;
/*     */ import java.util.Map;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class SQLError
/*     */ {
/*     */   static final int ER_WARNING_NOT_COMPLETE_ROLLBACK = 1196;
/*     */   private static Map mysqlToSql99State;
/*     */   private static Map mysqlToSqlState;
/*     */   public static final String SQL_STATE_BASE_TABLE_NOT_FOUND = "S0002";
/*     */   public static final String SQL_STATE_BASE_TABLE_OR_VIEW_ALREADY_EXISTS = "S0001";
/*     */   public static final String SQL_STATE_BASE_TABLE_OR_VIEW_NOT_FOUND = "42S02";
/*     */   public static final String SQL_STATE_COLUMN_ALREADY_EXISTS = "S0021";
/*     */   public static final String SQL_STATE_COLUMN_NOT_FOUND = "S0022";
/*     */   public static final String SQL_STATE_COMMUNICATION_LINK_FAILURE = "08S01";
/*     */   public static final String SQL_STATE_CONNECTION_FAIL_DURING_TX = "08007";
/*     */   public static final String SQL_STATE_CONNECTION_IN_USE = "08002";
/*     */   public static final String SQL_STATE_CONNECTION_NOT_OPEN = "08003";
/*     */   public static final String SQL_STATE_CONNECTION_REJECTED = "08004";
/*     */   public static final String SQL_STATE_DATE_TRUNCATED = "01004";
/*     */   public static final String SQL_STATE_DATETIME_FIELD_OVERFLOW = "22008";
/*     */   public static final String SQL_STATE_DEADLOCK = "41000";
/*     */   public static final String SQL_STATE_DISCONNECT_ERROR = "01002";
/*     */   public static final String SQL_STATE_DIVISION_BY_ZERO = "22012";
/*     */   public static final String SQL_STATE_DRIVER_NOT_CAPABLE = "S1C00";
/*     */   public static final String SQL_STATE_ERROR_IN_ROW = "01S01";
/*     */   public static final String SQL_STATE_GENERAL_ERROR = "S1000";
/*     */   public static final String SQL_STATE_ILLEGAL_ARGUMENT = "S1009";
/*     */   public static final String SQL_STATE_INDEX_ALREADY_EXISTS = "S0011";
/*     */   public static final String SQL_STATE_INDEX_NOT_FOUND = "S0012";
/*     */   public static final String SQL_STATE_INSERT_VALUE_LIST_NO_MATCH_COL_LIST = "21S01";
/*     */   public static final String SQL_STATE_INVALID_AUTH_SPEC = "28000";
/*     */   public static final String SQL_STATE_INVALID_CHARACTER_VALUE_FOR_CAST = "22018";
/*     */   public static final String SQL_STATE_INVALID_COLUMN_NUMBER = "S1002";
/*     */   public static final String SQL_STATE_INVALID_CONNECTION_ATTRIBUTE = "01S00";
/*     */   public static final String SQL_STATE_MEMORY_ALLOCATION_FAILURE = "S1001";
/*     */   public static final String SQL_STATE_MORE_THAN_ONE_ROW_UPDATED_OR_DELETED = "01S04";
/*     */   public static final String SQL_STATE_NO_DEFAULT_FOR_COLUMN = "S0023";
/*     */   public static final String SQL_STATE_NO_ROWS_UPDATED_OR_DELETED = "01S03";
/*     */   public static final String SQL_STATE_NUMERIC_VALUE_OUT_OF_RANGE = "22003";
/*     */   public static final String SQL_STATE_PRIVILEGE_NOT_REVOKED = "01006";
/*     */   public static final String SQL_STATE_SYNTAX_ERROR = "42000";
/*     */   public static final String SQL_STATE_TIMEOUT_EXPIRED = "S1T00";
/*     */   public static final String SQL_STATE_TRANSACTION_RESOLUTION_UNKNOWN = "08007";
/*     */   public static final String SQL_STATE_UNABLE_TO_CONNECT_TO_DATASOURCE = "08001";
/*     */   public static final String SQL_STATE_WRONG_NO_OF_PARAMETERS = "07001";
/*     */   public static final String SQL_STATE_INVALID_TRANSACTION_TERMINATION = "2D000";
/* 136 */   private static Map sqlStateMessages = new HashMap();
/* 137 */   static { sqlStateMessages.put("01002", Messages.getString("SQLError.35"));
/*     */     
/* 139 */     sqlStateMessages.put("01004", Messages.getString("SQLError.36"));
/*     */     
/* 141 */     sqlStateMessages.put("01006", Messages.getString("SQLError.37"));
/*     */     
/* 143 */     sqlStateMessages.put("01S00", Messages.getString("SQLError.38"));
/*     */     
/* 145 */     sqlStateMessages.put("01S01", Messages.getString("SQLError.39"));
/*     */     
/* 147 */     sqlStateMessages.put("01S03", Messages.getString("SQLError.40"));
/*     */     
/* 149 */     sqlStateMessages.put("01S04", Messages.getString("SQLError.41"));
/*     */     
/* 151 */     sqlStateMessages.put("07001", Messages.getString("SQLError.42"));
/*     */     
/* 153 */     sqlStateMessages.put("08001", Messages.getString("SQLError.43"));
/*     */     
/* 155 */     sqlStateMessages.put("08002", Messages.getString("SQLError.44"));
/*     */     
/* 157 */     sqlStateMessages.put("08003", Messages.getString("SQLError.45"));
/*     */     
/* 159 */     sqlStateMessages.put("08004", Messages.getString("SQLError.46"));
/*     */     
/* 161 */     sqlStateMessages.put("08007", Messages.getString("SQLError.47"));
/*     */     
/* 163 */     sqlStateMessages.put("08S01", Messages.getString("SQLError.48"));
/*     */     
/* 165 */     sqlStateMessages.put("21S01", Messages.getString("SQLError.49"));
/*     */     
/* 167 */     sqlStateMessages.put("22003", Messages.getString("SQLError.50"));
/*     */     
/* 169 */     sqlStateMessages.put("22008", Messages.getString("SQLError.51"));
/*     */     
/* 171 */     sqlStateMessages.put("22012", Messages.getString("SQLError.52"));
/*     */     
/* 173 */     sqlStateMessages.put("41000", Messages.getString("SQLError.53"));
/*     */     
/* 175 */     sqlStateMessages.put("28000", Messages.getString("SQLError.54"));
/*     */     
/* 177 */     sqlStateMessages.put("42000", Messages.getString("SQLError.55"));
/*     */     
/* 179 */     sqlStateMessages.put("42S02", Messages.getString("SQLError.56"));
/*     */     
/* 181 */     sqlStateMessages.put("S0001", Messages.getString("SQLError.57"));
/*     */     
/* 183 */     sqlStateMessages.put("S0002", Messages.getString("SQLError.58"));
/*     */     
/* 185 */     sqlStateMessages.put("S0011", Messages.getString("SQLError.59"));
/*     */     
/* 187 */     sqlStateMessages.put("S0012", Messages.getString("SQLError.60"));
/*     */     
/* 189 */     sqlStateMessages.put("S0021", Messages.getString("SQLError.61"));
/*     */     
/* 191 */     sqlStateMessages.put("S0022", Messages.getString("SQLError.62"));
/*     */     
/* 193 */     sqlStateMessages.put("S0023", Messages.getString("SQLError.63"));
/*     */     
/* 195 */     sqlStateMessages.put("S1000", Messages.getString("SQLError.64"));
/*     */     
/* 197 */     sqlStateMessages.put("S1001", Messages.getString("SQLError.65"));
/*     */     
/* 199 */     sqlStateMessages.put("S1002", Messages.getString("SQLError.66"));
/*     */     
/* 201 */     sqlStateMessages.put("S1009", Messages.getString("SQLError.67"));
/*     */     
/* 203 */     sqlStateMessages.put("S1C00", Messages.getString("SQLError.68"));
/*     */     
/* 205 */     sqlStateMessages.put("S1T00", Messages.getString("SQLError.69"));
/*     */     
/*     */ 
/* 208 */     mysqlToSqlState = new Hashtable();
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 219 */     mysqlToSqlState.put(new Integer(1040), "08004");
/* 220 */     mysqlToSqlState.put(new Integer(1042), "08004");
/* 221 */     mysqlToSqlState.put(new Integer(1043), "08004");
/* 222 */     mysqlToSqlState.put(new Integer(1047), "08S01");
/*     */     
/* 224 */     mysqlToSqlState.put(new Integer(1081), "08S01");
/*     */     
/*     */ 
/*     */ 
/*     */ 
/* 229 */     mysqlToSqlState.put(new Integer(1129), "08004");
/* 230 */     mysqlToSqlState.put(new Integer(1130), "08004");
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 237 */     mysqlToSqlState.put(new Integer(1045), "28000");
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 254 */     mysqlToSqlState.put(new Integer(1037), "S1001");
/*     */     
/* 256 */     mysqlToSqlState.put(new Integer(1038), "S1001");
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 265 */     mysqlToSqlState.put(new Integer(1064), "42000");
/* 266 */     mysqlToSqlState.put(new Integer(1065), "42000");
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 293 */     mysqlToSqlState.put(new Integer(1055), "S1009");
/* 294 */     mysqlToSqlState.put(new Integer(1056), "S1009");
/* 295 */     mysqlToSqlState.put(new Integer(1057), "S1009");
/* 296 */     mysqlToSqlState.put(new Integer(1059), "S1009");
/* 297 */     mysqlToSqlState.put(new Integer(1060), "S1009");
/* 298 */     mysqlToSqlState.put(new Integer(1061), "S1009");
/* 299 */     mysqlToSqlState.put(new Integer(1062), "S1009");
/* 300 */     mysqlToSqlState.put(new Integer(1063), "S1009");
/* 301 */     mysqlToSqlState.put(new Integer(1066), "S1009");
/* 302 */     mysqlToSqlState.put(new Integer(1067), "S1009");
/* 303 */     mysqlToSqlState.put(new Integer(1068), "S1009");
/* 304 */     mysqlToSqlState.put(new Integer(1069), "S1009");
/* 305 */     mysqlToSqlState.put(new Integer(1070), "S1009");
/* 306 */     mysqlToSqlState.put(new Integer(1071), "S1009");
/* 307 */     mysqlToSqlState.put(new Integer(1072), "S1009");
/* 308 */     mysqlToSqlState.put(new Integer(1073), "S1009");
/* 309 */     mysqlToSqlState.put(new Integer(1074), "S1009");
/* 310 */     mysqlToSqlState.put(new Integer(1075), "S1009");
/* 311 */     mysqlToSqlState.put(new Integer(1082), "S1009");
/* 312 */     mysqlToSqlState.put(new Integer(1083), "S1009");
/* 313 */     mysqlToSqlState.put(new Integer(1084), "S1009");
/*     */     
/*     */ 
/*     */ 
/*     */ 
/* 318 */     mysqlToSqlState.put(new Integer(1058), "21S01");
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 354 */     mysqlToSqlState.put(new Integer(1051), "42S02");
/*     */     
/*     */ 
/*     */ 
/*     */ 
/* 359 */     mysqlToSqlState.put(new Integer(1054), "S0022");
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 371 */     mysqlToSqlState.put(new Integer(1205), "41000");
/* 372 */     mysqlToSqlState.put(new Integer(1213), "41000");
/*     */     
/* 374 */     mysqlToSql99State = new HashMap();
/*     */     
/* 376 */     mysqlToSql99State.put(new Integer(1205), "41000");
/* 377 */     mysqlToSql99State.put(new Integer(1213), "41000");
/* 378 */     mysqlToSql99State.put(new Integer(1022), "23000");
/*     */     
/* 380 */     mysqlToSql99State.put(new Integer(1037), "HY001");
/*     */     
/* 382 */     mysqlToSql99State.put(new Integer(1038), "HY001");
/*     */     
/* 384 */     mysqlToSql99State.put(new Integer(1040), "08004");
/*     */     
/* 386 */     mysqlToSql99State.put(new Integer(1042), "08S01");
/*     */     
/* 388 */     mysqlToSql99State.put(new Integer(1043), "08S01");
/*     */     
/* 390 */     mysqlToSql99State.put(new Integer(1044), "42000");
/*     */     
/* 392 */     mysqlToSql99State.put(new Integer(1045), "28000");
/*     */     
/* 394 */     mysqlToSql99State.put(new Integer(1050), "42S01");
/*     */     
/* 396 */     mysqlToSql99State.put(new Integer(1051), "42S02");
/*     */     
/* 398 */     mysqlToSql99State.put(new Integer(1052), "23000");
/*     */     
/* 400 */     mysqlToSql99State.put(new Integer(1053), "08S01");
/*     */     
/* 402 */     mysqlToSql99State.put(new Integer(1054), "42S22");
/*     */     
/* 404 */     mysqlToSql99State.put(new Integer(1055), "42000");
/*     */     
/* 406 */     mysqlToSql99State.put(new Integer(1056), "42000");
/*     */     
/* 408 */     mysqlToSql99State.put(new Integer(1057), "42000");
/*     */     
/* 410 */     mysqlToSql99State.put(new Integer(1058), "21S01");
/*     */     
/* 412 */     mysqlToSql99State.put(new Integer(1059), "42000");
/*     */     
/* 414 */     mysqlToSql99State.put(new Integer(1060), "42S21");
/*     */     
/* 416 */     mysqlToSql99State.put(new Integer(1061), "42000");
/*     */     
/* 418 */     mysqlToSql99State.put(new Integer(1062), "23000");
/*     */     
/* 420 */     mysqlToSql99State.put(new Integer(1063), "42000");
/*     */     
/* 422 */     mysqlToSql99State.put(new Integer(1064), "42000");
/*     */     
/* 424 */     mysqlToSql99State.put(new Integer(1065), "42000");
/*     */     
/* 426 */     mysqlToSql99State.put(new Integer(1066), "42000");
/*     */     
/* 428 */     mysqlToSql99State.put(new Integer(1067), "42000");
/*     */     
/* 430 */     mysqlToSql99State.put(new Integer(1068), "42000");
/*     */     
/* 432 */     mysqlToSql99State.put(new Integer(1069), "42000");
/*     */     
/* 434 */     mysqlToSql99State.put(new Integer(1070), "42000");
/*     */     
/* 436 */     mysqlToSql99State.put(new Integer(1071), "42000");
/*     */     
/* 438 */     mysqlToSql99State.put(new Integer(1072), "42000");
/*     */     
/* 440 */     mysqlToSql99State.put(new Integer(1073), "42000");
/*     */     
/* 442 */     mysqlToSql99State.put(new Integer(1074), "42000");
/*     */     
/* 444 */     mysqlToSql99State.put(new Integer(1075), "42000");
/*     */     
/* 446 */     mysqlToSql99State.put(new Integer(1080), "08S01");
/*     */     
/* 448 */     mysqlToSql99State.put(new Integer(1081), "08S01");
/*     */     
/* 450 */     mysqlToSql99State.put(new Integer(1082), "42S12");
/*     */     
/* 452 */     mysqlToSql99State.put(new Integer(1083), "42000");
/*     */     
/* 454 */     mysqlToSql99State.put(new Integer(1084), "42000");
/*     */     
/* 456 */     mysqlToSql99State.put(new Integer(1090), "42000");
/*     */     
/* 458 */     mysqlToSql99State.put(new Integer(1091), "42000");
/*     */     
/* 460 */     mysqlToSql99State.put(new Integer(1101), "42000");
/*     */     
/* 462 */     mysqlToSql99State.put(new Integer(1102), "42000");
/*     */     
/* 464 */     mysqlToSql99State.put(new Integer(1103), "42000");
/*     */     
/* 466 */     mysqlToSql99State.put(new Integer(1104), "42000");
/*     */     
/* 468 */     mysqlToSql99State.put(new Integer(1106), "42000");
/*     */     
/* 470 */     mysqlToSql99State.put(new Integer(1107), "42000");
/*     */     
/* 472 */     mysqlToSql99State.put(new Integer(1109), "42S02");
/*     */     
/* 474 */     mysqlToSql99State.put(new Integer(1110), "42000");
/*     */     
/* 476 */     mysqlToSql99State.put(new Integer(1112), "42000");
/*     */     
/* 478 */     mysqlToSql99State.put(new Integer(1113), "42000");
/*     */     
/* 480 */     mysqlToSql99State.put(new Integer(1115), "42000");
/*     */     
/* 482 */     mysqlToSql99State.put(new Integer(1118), "42000");
/*     */     
/* 484 */     mysqlToSql99State.put(new Integer(1120), "42000");
/*     */     
/* 486 */     mysqlToSql99State.put(new Integer(1121), "42000");
/*     */     
/* 488 */     mysqlToSql99State.put(new Integer(1131), "42000");
/*     */     
/* 490 */     mysqlToSql99State.put(new Integer(1132), "42000");
/*     */     
/* 492 */     mysqlToSql99State.put(new Integer(1133), "42000");
/*     */     
/* 494 */     mysqlToSql99State.put(new Integer(1136), "21S01");
/*     */     
/* 496 */     mysqlToSql99State.put(new Integer(1138), "42000");
/*     */     
/* 498 */     mysqlToSql99State.put(new Integer(1139), "42000");
/*     */     
/* 500 */     mysqlToSql99State.put(new Integer(1140), "42000");
/*     */     
/* 502 */     mysqlToSql99State.put(new Integer(1141), "42000");
/*     */     
/* 504 */     mysqlToSql99State.put(new Integer(1142), "42000");
/*     */     
/* 506 */     mysqlToSql99State.put(new Integer(1143), "42000");
/*     */     
/* 508 */     mysqlToSql99State.put(new Integer(1144), "42000");
/*     */     
/* 510 */     mysqlToSql99State.put(new Integer(1145), "42000");
/*     */     
/* 512 */     mysqlToSql99State.put(new Integer(1146), "42S02");
/*     */     
/* 514 */     mysqlToSql99State.put(new Integer(1147), "42000");
/*     */     
/* 516 */     mysqlToSql99State.put(new Integer(1148), "42000");
/*     */     
/* 518 */     mysqlToSql99State.put(new Integer(1149), "42000");
/*     */     
/* 520 */     mysqlToSql99State.put(new Integer(1152), "08S01");
/*     */     
/* 522 */     mysqlToSql99State.put(new Integer(1153), "08S01");
/*     */     
/* 524 */     mysqlToSql99State.put(new Integer(1154), "08S01");
/*     */     
/* 526 */     mysqlToSql99State.put(new Integer(1155), "08S01");
/*     */     
/* 528 */     mysqlToSql99State.put(new Integer(1156), "08S01");
/*     */     
/* 530 */     mysqlToSql99State.put(new Integer(1157), "08S01");
/*     */     
/* 532 */     mysqlToSql99State.put(new Integer(1158), "08S01");
/*     */     
/* 534 */     mysqlToSql99State.put(new Integer(1159), "08S01");
/*     */     
/* 536 */     mysqlToSql99State.put(new Integer(1160), "08S01");
/*     */     
/* 538 */     mysqlToSql99State.put(new Integer(1161), "08S01");
/*     */     
/* 540 */     mysqlToSql99State.put(new Integer(1162), "42000");
/*     */     
/* 542 */     mysqlToSql99State.put(new Integer(1163), "42000");
/*     */     
/* 544 */     mysqlToSql99State.put(new Integer(1164), "42000");
/*     */     
/*     */ 
/*     */ 
/* 548 */     mysqlToSql99State.put(new Integer(1166), "42000");
/*     */     
/* 550 */     mysqlToSql99State.put(new Integer(1167), "42000");
/*     */     
/* 552 */     mysqlToSql99State.put(new Integer(1169), "23000");
/*     */     
/* 554 */     mysqlToSql99State.put(new Integer(1170), "42000");
/*     */     
/* 556 */     mysqlToSql99State.put(new Integer(1171), "42000");
/*     */     
/* 558 */     mysqlToSql99State.put(new Integer(1172), "42000");
/*     */     
/* 560 */     mysqlToSql99State.put(new Integer(1173), "42000");
/*     */     
/* 562 */     mysqlToSql99State.put(new Integer(1177), "42000");
/*     */     
/* 564 */     mysqlToSql99State.put(new Integer(1178), "42000");
/*     */     
/* 566 */     mysqlToSql99State.put(new Integer(1179), "25000");
/*     */     
/*     */ 
/* 569 */     mysqlToSql99State.put(new Integer(1184), "08S01");
/*     */     
/* 571 */     mysqlToSql99State.put(new Integer(1189), "08S01");
/*     */     
/* 573 */     mysqlToSql99State.put(new Integer(1190), "08S01");
/*     */     
/* 575 */     mysqlToSql99State.put(new Integer(1203), "42000");
/*     */     
/* 577 */     mysqlToSql99State.put(new Integer(1207), "25000");
/*     */     
/* 579 */     mysqlToSql99State.put(new Integer(1211), "42000");
/*     */     
/* 581 */     mysqlToSql99State.put(new Integer(1213), "40001");
/*     */     
/* 583 */     mysqlToSql99State.put(new Integer(1216), "23000");
/*     */     
/* 585 */     mysqlToSql99State.put(new Integer(1217), "23000");
/*     */     
/* 587 */     mysqlToSql99State.put(new Integer(1218), "08S01");
/*     */     
/* 589 */     mysqlToSql99State.put(new Integer(1222), "21000");
/*     */     
/*     */ 
/* 592 */     mysqlToSql99State.put(new Integer(1226), "42000");
/*     */     
/* 594 */     mysqlToSql99State.put(new Integer(1230), "42000");
/*     */     
/* 596 */     mysqlToSql99State.put(new Integer(1231), "42000");
/*     */     
/* 598 */     mysqlToSql99State.put(new Integer(1232), "42000");
/*     */     
/* 600 */     mysqlToSql99State.put(new Integer(1234), "42000");
/*     */     
/* 602 */     mysqlToSql99State.put(new Integer(1235), "42000");
/*     */     
/* 604 */     mysqlToSql99State.put(new Integer(1239), "42000");
/*     */     
/* 606 */     mysqlToSql99State.put(new Integer(1241), "21000");
/*     */     
/* 608 */     mysqlToSql99State.put(new Integer(1242), "21000");
/*     */     
/* 610 */     mysqlToSql99State.put(new Integer(1247), "42S22");
/*     */     
/* 612 */     mysqlToSql99State.put(new Integer(1248), "42000");
/*     */     
/* 614 */     mysqlToSql99State.put(new Integer(1249), "01000");
/*     */     
/* 616 */     mysqlToSql99State.put(new Integer(1250), "42000");
/*     */     
/* 618 */     mysqlToSql99State.put(new Integer(1251), "08004");
/*     */     
/* 620 */     mysqlToSql99State.put(new Integer(1252), "42000");
/*     */     
/* 622 */     mysqlToSql99State.put(new Integer(1253), "42000");
/*     */     
/* 624 */     mysqlToSql99State.put(new Integer(1261), "01000");
/*     */     
/* 626 */     mysqlToSql99State.put(new Integer(1262), "01000");
/*     */     
/* 628 */     mysqlToSql99State.put(new Integer(1263), "01000");
/*     */     
/* 630 */     mysqlToSql99State.put(new Integer(1264), "01000");
/*     */     
/* 632 */     mysqlToSql99State.put(new Integer(1265), "01000");
/*     */     
/* 634 */     mysqlToSql99State.put(new Integer(1280), "42000");
/*     */     
/* 636 */     mysqlToSql99State.put(new Integer(1281), "42000");
/*     */     
/* 638 */     mysqlToSql99State.put(new Integer(1286), "42000");
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
/*     */   static SQLWarning convertShowWarningsToSQLWarnings(Connection connection)
/*     */     throws SQLException
/*     */   {
/* 658 */     return convertShowWarningsToSQLWarnings(connection, 0, false);
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   static SQLWarning convertShowWarningsToSQLWarnings(Connection connection, int warningCountIfKnown, boolean forTruncationOnly)
/*     */     throws SQLException
/*     */   {
/* 683 */     Statement stmt = null;
/* 684 */     ResultSet warnRs = null;
/*     */     
/* 686 */     SQLWarning currentWarning = null;
/*     */     try
/*     */     {
/* 689 */       if (warningCountIfKnown < 100) {
/* 690 */         stmt = connection.createStatement();
/*     */         
/* 692 */         if (stmt.getMaxRows() != 0) {
/* 693 */           stmt.setMaxRows(0);
/*     */         }
/*     */       }
/*     */       else {
/* 697 */         stmt = connection.createStatement(1003, 1007);
/*     */         
/*     */ 
/* 700 */         stmt.setFetchSize(Integer.MIN_VALUE);
/*     */       }
/*     */       
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 710 */       warnRs = stmt.executeQuery("SHOW WARNINGS");
/*     */       int code;
/* 712 */       while (warnRs.next()) {
/* 713 */         code = warnRs.getInt("Code");
/*     */         
/* 715 */         if (forTruncationOnly) {
/* 716 */           if ((code == 1265) || (code == 1264)) {
/* 717 */             DataTruncation newTruncation = new MysqlDataTruncation(warnRs.getString("Message"), 0, false, false, 0, 0);
/*     */             
/*     */ 
/* 720 */             if (currentWarning == null) {
/* 721 */               currentWarning = newTruncation;
/*     */             } else {
/* 723 */               currentWarning.setNextWarning(newTruncation);
/*     */             }
/*     */           }
/*     */         } else {
/* 727 */           String level = warnRs.getString("Level");
/* 728 */           String message = warnRs.getString("Message");
/*     */           
/* 730 */           SQLWarning newWarning = new SQLWarning(message, mysqlToSqlState(code, connection.getUseSqlStateCodes()), code);
/*     */           
/*     */ 
/*     */ 
/* 734 */           if (currentWarning == null) {
/* 735 */             currentWarning = newWarning;
/*     */           } else {
/* 737 */             currentWarning.setNextWarning(newWarning);
/*     */           }
/*     */         }
/*     */       }
/*     */       
/* 742 */       if ((forTruncationOnly) && (currentWarning != null)) {
/* 743 */         throw currentWarning;
/*     */       }
/*     */       
/* 746 */       return currentWarning;
/*     */     } finally {
/* 748 */       SQLException reThrow = null;
/*     */       
/* 750 */       if (warnRs != null) {
/*     */         try {
/* 752 */           warnRs.close();
/*     */         } catch (SQLException sqlEx) {
/* 754 */           reThrow = sqlEx;
/*     */         }
/*     */       }
/*     */       
/* 758 */       if (stmt != null) {
/*     */         try {
/* 760 */           stmt.close();
/*     */ 
/*     */         }
/*     */         catch (SQLException sqlEx)
/*     */         {
/* 765 */           reThrow = sqlEx;
/*     */         }
/*     */       }
/*     */       
/* 769 */       if (reThrow != null) {
/* 770 */         throw reThrow;
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   public static void dumpSqlStatesMappingsAsXml() throws Exception {
/* 776 */     TreeMap allErrorNumbers = new TreeMap();
/* 777 */     Map mysqlErrorNumbersToNames = new HashMap();
/*     */     
/* 779 */     Integer errorNumber = null;
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 785 */     Iterator mysqlErrorNumbers = mysqlToSql99State.keySet().iterator();
/* 786 */     while (mysqlErrorNumbers.hasNext()) {
/* 787 */       errorNumber = (Integer)mysqlErrorNumbers.next();
/* 788 */       allErrorNumbers.put(errorNumber, errorNumber);
/*     */     }
/*     */     
/* 791 */     Iterator mysqlErrorNumbers = mysqlToSqlState.keySet().iterator();
/* 792 */     while (mysqlErrorNumbers.hasNext()) {
/* 793 */       errorNumber = (Integer)mysqlErrorNumbers.next();
/* 794 */       allErrorNumbers.put(errorNumber, errorNumber);
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/* 800 */     Field[] possibleFields = MysqlErrorNumbers.class.getDeclaredFields();
/*     */     
/*     */ 
/* 803 */     for (int i = 0; i < possibleFields.length; i++) {
/* 804 */       String fieldName = possibleFields[i].getName();
/*     */       
/* 806 */       if (fieldName.startsWith("ER_")) {
/* 807 */         mysqlErrorNumbersToNames.put(possibleFields[i].get(null), fieldName);
/*     */       }
/*     */     }
/*     */     
/*     */ 
/* 812 */     System.out.println("<ErrorMappings>");
/*     */     
/* 814 */     Iterator allErrorNumbersIter = allErrorNumbers.keySet().iterator();
/* 815 */     while (allErrorNumbersIter.hasNext()) {
/* 816 */       errorNumber = (Integer)allErrorNumbersIter.next();
/*     */       
/* 818 */       String sql92State = mysqlToSql99(errorNumber.intValue());
/* 819 */       String oldSqlState = mysqlToXOpen(errorNumber.intValue());
/*     */       
/* 821 */       System.out.println("   <ErrorMapping mysqlErrorNumber=\"" + errorNumber + "\" mysqlErrorName=\"" + mysqlErrorNumbersToNames.get(errorNumber) + "\" legacySqlState=\"" + (oldSqlState == null ? "" : oldSqlState) + "\" sql92SqlState=\"" + (sql92State == null ? "" : sql92State) + "\"/>");
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 830 */     System.out.println("</ErrorMappings>");
/*     */   }
/*     */   
/*     */   static String get(String stateCode) {
/* 834 */     return (String)sqlStateMessages.get(stateCode);
/*     */   }
/*     */   
/*     */   private static String mysqlToSql99(int errno) {
/* 838 */     Integer err = new Integer(errno);
/*     */     
/* 840 */     if (mysqlToSql99State.containsKey(err)) {
/* 841 */       return (String)mysqlToSql99State.get(err);
/*     */     }
/*     */     
/* 844 */     return "HY000";
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   static String mysqlToSqlState(int errno, boolean useSql92States)
/*     */   {
/* 856 */     if (useSql92States) {
/* 857 */       return mysqlToSql99(errno);
/*     */     }
/*     */     
/* 860 */     return mysqlToXOpen(errno);
/*     */   }
/*     */   
/*     */   private static String mysqlToXOpen(int errno) {
/* 864 */     Integer err = new Integer(errno);
/*     */     
/* 866 */     if (mysqlToSqlState.containsKey(err)) {
/* 867 */       return (String)mysqlToSqlState.get(err);
/*     */     }
/*     */     
/* 870 */     return "S1000";
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
/*     */   public static SQLException createSQLException(String message, String sqlState)
/*     */   {
/* 886 */     if (sqlState != null) {
/* 887 */       if (sqlState.startsWith("08")) {
/* 888 */         return new MySQLNonTransientConnectionException(message, sqlState);
/*     */       }
/*     */       
/*     */ 
/* 892 */       if (sqlState.startsWith("22")) {
/* 893 */         return new MySQLDataException(message, sqlState);
/*     */       }
/*     */       
/* 896 */       if (sqlState.startsWith("23")) {
/* 897 */         return new MySQLIntegrityConstraintViolationException(message, sqlState);
/*     */       }
/*     */       
/*     */ 
/* 901 */       if (sqlState.startsWith("42")) {
/* 902 */         return new MySQLSyntaxErrorException(message, sqlState);
/*     */       }
/*     */       
/* 905 */       if (sqlState.startsWith("40")) {
/* 906 */         return new MySQLTransactionRollbackException(message, sqlState);
/*     */       }
/*     */     }
/*     */     
/* 910 */     return new SQLException(message, sqlState);
/*     */   }
/*     */   
/*     */   public static SQLException createSQLException(String message) {
/* 914 */     return new SQLException(message);
/*     */   }
/*     */   
/*     */   public static SQLException createSQLException(String message, String sqlState, int vendorErrorCode)
/*     */   {
/* 919 */     if (sqlState != null) {
/* 920 */       if (sqlState.startsWith("08")) {
/* 921 */         return new MySQLNonTransientConnectionException(message, sqlState, vendorErrorCode);
/*     */       }
/*     */       
/*     */ 
/* 925 */       if (sqlState.startsWith("22")) {
/* 926 */         return new MySQLDataException(message, sqlState, vendorErrorCode);
/*     */       }
/*     */       
/*     */ 
/* 930 */       if (sqlState.startsWith("23")) {
/* 931 */         return new MySQLIntegrityConstraintViolationException(message, sqlState, vendorErrorCode);
/*     */       }
/*     */       
/*     */ 
/* 935 */       if (sqlState.startsWith("42")) {
/* 936 */         return new MySQLSyntaxErrorException(message, sqlState, vendorErrorCode);
/*     */       }
/*     */       
/*     */ 
/* 940 */       if (sqlState.startsWith("40")) {
/* 941 */         return new MySQLTransactionRollbackException(message, sqlState, vendorErrorCode);
/*     */       }
/*     */     }
/*     */     
/*     */ 
/* 946 */     return new SQLException(message, sqlState, vendorErrorCode);
/*     */   }
/*     */ }


/* Location:              E:\java\java学习\hutubill\lib\all.jar!\com\mysql\jdbc\SQLError.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version:       0.7.1
 */
/*     */ package com.mysql.jdbc;
/*     */ 
/*     */ import java.sql.SQLException;
/*     */ import java.sql.Time;
/*     */ import java.sql.Timestamp;
/*     */ import java.util.Calendar;
/*     */ import java.util.Collections;
/*     */ import java.util.GregorianCalendar;
/*     */ import java.util.HashMap;
/*     */ import java.util.Locale;
/*     */ import java.util.Map;
/*     */ import java.util.NoSuchElementException;
/*     */ import java.util.StringTokenizer;
/*     */ import java.util.TimeZone;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ class EscapeProcessor
/*     */ {
/*     */   private static Map JDBC_CONVERT_TO_MYSQL_TYPE_MAP;
/*     */   private static Map JDBC_NO_CONVERT_TO_MYSQL_EXPRESSION_MAP;
/*     */   
/*     */   static
/*     */   {
/*  51 */     Map tempMap = new HashMap();
/*     */     
/*  53 */     tempMap.put("BIGINT", "0 + ?");
/*  54 */     tempMap.put("BINARY", "BINARY");
/*  55 */     tempMap.put("BIT", "0 + ?");
/*  56 */     tempMap.put("CHAR", "CHAR");
/*  57 */     tempMap.put("DATE", "DATE");
/*  58 */     tempMap.put("DECIMAL", "0.0 + ?");
/*  59 */     tempMap.put("DOUBLE", "0.0 + ?");
/*  60 */     tempMap.put("FLOAT", "0.0 + ?");
/*  61 */     tempMap.put("INTEGER", "0 + ?");
/*  62 */     tempMap.put("LONGVARBINARY", "BINARY");
/*  63 */     tempMap.put("LONGVARCHAR", "CONCAT(?)");
/*  64 */     tempMap.put("REAL", "0.0 + ?");
/*  65 */     tempMap.put("SMALLINT", "CONCAT(?)");
/*  66 */     tempMap.put("TIME", "TIME");
/*  67 */     tempMap.put("TIMESTAMP", "DATETIME");
/*  68 */     tempMap.put("TINYINT", "CONCAT(?)");
/*  69 */     tempMap.put("VARBINARY", "BINARY");
/*  70 */     tempMap.put("VARCHAR", "CONCAT(?)");
/*     */     
/*  72 */     JDBC_CONVERT_TO_MYSQL_TYPE_MAP = Collections.unmodifiableMap(tempMap);
/*     */     
/*  74 */     tempMap = new HashMap(JDBC_CONVERT_TO_MYSQL_TYPE_MAP);
/*     */     
/*  76 */     tempMap.put("BINARY", "CONCAT(?)");
/*  77 */     tempMap.put("CHAR", "CONCAT(?)");
/*  78 */     tempMap.remove("DATE");
/*  79 */     tempMap.put("LONGVARBINARY", "CONCAT(?)");
/*  80 */     tempMap.remove("TIME");
/*  81 */     tempMap.remove("TIMESTAMP");
/*  82 */     tempMap.put("VARBINARY", "CONCAT(?)");
/*     */     
/*  84 */     JDBC_NO_CONVERT_TO_MYSQL_EXPRESSION_MAP = Collections.unmodifiableMap(tempMap);
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
/*     */   public static final Object escapeSQL(String sql, boolean serverSupportsConvertFn, Connection conn)
/*     */     throws SQLException
/*     */   {
/* 105 */     boolean replaceEscapeSequence = false;
/* 106 */     String escapeSequence = null;
/*     */     
/* 108 */     if (sql == null) {
/* 109 */       return null;
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 116 */     int beginBrace = sql.indexOf('{');
/* 117 */     int nextEndBrace = beginBrace == -1 ? -1 : sql.indexOf('}', beginBrace);
/*     */     
/*     */ 
/* 120 */     if (nextEndBrace == -1) {
/* 121 */       return sql;
/*     */     }
/*     */     
/* 124 */     StringBuffer newSql = new StringBuffer();
/*     */     
/* 126 */     EscapeTokenizer escapeTokenizer = new EscapeTokenizer(sql);
/*     */     
/* 128 */     byte usesVariables = 0;
/* 129 */     boolean callingStoredFunction = false;
/*     */     
/* 131 */     while (escapeTokenizer.hasMoreTokens()) {
/* 132 */       String token = escapeTokenizer.nextToken();
/*     */       
/* 134 */       if (token.length() != 0) {
/* 135 */         if (token.charAt(0) == '{')
/*     */         {
/* 137 */           if (!token.endsWith("}")) {
/* 138 */             throw SQLError.createSQLException("Not a valid escape sequence: " + token);
/*     */           }
/*     */           
/*     */ 
/* 142 */           if (token.length() > 2) {
/* 143 */             int nestedBrace = token.indexOf('{', 2);
/*     */             
/* 145 */             if (nestedBrace != -1) {
/* 146 */               StringBuffer buf = new StringBuffer(token.substring(0, 1));
/*     */               
/*     */ 
/* 149 */               Object remainingResults = escapeSQL(token.substring(1, token.length() - 1), serverSupportsConvertFn, conn);
/*     */               
/*     */ 
/*     */ 
/* 153 */               String remaining = null;
/*     */               
/* 155 */               if ((remainingResults instanceof String)) {
/* 156 */                 remaining = (String)remainingResults;
/*     */               } else {
/* 158 */                 remaining = ((EscapeProcessorResult)remainingResults).escapedSql;
/*     */                 
/* 160 */                 if (usesVariables != 1) {
/* 161 */                   usesVariables = ((EscapeProcessorResult)remainingResults).usesVariables;
/*     */                 }
/*     */               }
/*     */               
/* 165 */               buf.append(remaining);
/*     */               
/* 167 */               buf.append('}');
/*     */               
/* 169 */               token = buf.toString();
/*     */             }
/*     */           }
/*     */           
/*     */ 
/*     */ 
/* 175 */           String collapsedToken = removeWhitespace(token);
/*     */           
/*     */ 
/*     */ 
/*     */ 
/* 180 */           if (StringUtils.startsWithIgnoreCase(collapsedToken, "{escape"))
/*     */           {
/*     */             try {
/* 183 */               StringTokenizer st = new StringTokenizer(token, " '");
/*     */               
/* 185 */               st.nextToken();
/* 186 */               escapeSequence = st.nextToken();
/*     */               
/* 188 */               if (escapeSequence.length() < 3) {
/* 189 */                 newSql.append(token);
/*     */               }
/*     */               else
/*     */               {
/* 193 */                 escapeSequence = escapeSequence.substring(1, escapeSequence.length() - 1);
/*     */                 
/* 195 */                 replaceEscapeSequence = true;
/*     */               }
/*     */             } catch (NoSuchElementException e) {
/* 198 */               newSql.append(token);
/*     */             }
/* 200 */           } else if (StringUtils.startsWithIgnoreCase(collapsedToken, "{fn"))
/*     */           {
/* 202 */             int startPos = token.toLowerCase().indexOf("fn ") + 3;
/* 203 */             int endPos = token.length() - 1;
/*     */             
/* 205 */             String fnToken = token.substring(startPos, endPos);
/*     */             
/*     */ 
/*     */ 
/* 209 */             if (StringUtils.startsWithIgnoreCaseAndWs(fnToken, "convert"))
/*     */             {
/* 211 */               newSql.append(processConvertToken(fnToken, serverSupportsConvertFn));
/*     */             }
/*     */             else
/*     */             {
/* 215 */               newSql.append(fnToken);
/*     */             }
/* 217 */           } else if (StringUtils.startsWithIgnoreCase(collapsedToken, "{d"))
/*     */           {
/* 219 */             int startPos = token.indexOf('\'') + 1;
/* 220 */             int endPos = token.lastIndexOf('\'');
/*     */             
/* 222 */             if ((startPos == -1) || (endPos == -1)) {
/* 223 */               newSql.append(token);
/*     */             }
/*     */             else {
/* 226 */               String argument = token.substring(startPos, endPos);
/*     */               try
/*     */               {
/* 229 */                 StringTokenizer st = new StringTokenizer(argument, " -");
/*     */                 
/* 231 */                 String year4 = st.nextToken();
/* 232 */                 String month2 = st.nextToken();
/* 233 */                 String day2 = st.nextToken();
/* 234 */                 String dateString = "'" + year4 + "-" + month2 + "-" + day2 + "'";
/*     */                 
/* 236 */                 newSql.append(dateString);
/*     */               } catch (NoSuchElementException e) {
/* 238 */                 throw SQLError.createSQLException("Syntax error for DATE escape sequence '" + argument + "'", "42000");
/*     */               }
/*     */               
/*     */             }
/*     */           }
/* 243 */           else if (StringUtils.startsWithIgnoreCase(collapsedToken, "{ts"))
/*     */           {
/* 245 */             int startPos = token.indexOf('\'') + 1;
/* 246 */             int endPos = token.lastIndexOf('\'');
/*     */             
/* 248 */             if ((startPos == -1) || (endPos == -1)) {
/* 249 */               newSql.append(token);
/*     */             }
/*     */             else {
/* 252 */               String argument = token.substring(startPos, endPos);
/*     */               try
/*     */               {
/* 255 */                 StringTokenizer st = new StringTokenizer(argument, " .-:");
/*     */                 
/* 257 */                 String year4 = st.nextToken();
/* 258 */                 String month2 = st.nextToken();
/* 259 */                 String day2 = st.nextToken();
/* 260 */                 String hour = st.nextToken();
/* 261 */                 String minute = st.nextToken();
/* 262 */                 String second = st.nextToken();
/*     */                 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 288 */                 if ((!conn.getUseTimezone()) && (!conn.getUseJDBCCompliantTimezoneShift())) {
/* 289 */                   newSql.append("'").append(year4).append("-").append(month2).append("-").append(day2).append(" ").append(hour).append(":").append(minute).append(":").append(second).append("'");
/*     */                 }
/*     */                 else
/*     */                 {
/*     */                   Calendar sessionCalendar;
/*     */                   
/*     */                   Calendar sessionCalendar;
/*     */                   
/* 297 */                   if (conn != null) {
/* 298 */                     sessionCalendar = conn.getCalendarInstanceForSessionOrNew();
/*     */                   } else {
/* 300 */                     sessionCalendar = new GregorianCalendar();
/* 301 */                     sessionCalendar.setTimeZone(TimeZone.getTimeZone("GMT"));
/*     */                   }
/*     */                   try
/*     */                   {
/* 305 */                     int year4Int = Integer.parseInt(year4);
/* 306 */                     int month2Int = Integer.parseInt(month2);
/* 307 */                     int day2Int = Integer.parseInt(day2);
/* 308 */                     int hourInt = Integer.parseInt(hour);
/* 309 */                     int minuteInt = Integer.parseInt(minute);
/* 310 */                     int secondInt = Integer.parseInt(second);
/*     */                     
/* 312 */                     synchronized (sessionCalendar) {
/* 313 */                       boolean useGmtMillis = conn.getUseGmtMillisForDatetimes();
/*     */                       
/* 315 */                       Timestamp toBeAdjusted = TimeUtil.fastTimestampCreate(useGmtMillis, useGmtMillis ? Calendar.getInstance(TimeZone.getTimeZone("GMT")) : null, sessionCalendar, year4Int, month2Int, day2Int, hourInt, minuteInt, secondInt, 0);
/*     */                       
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 326 */                       Timestamp inServerTimezone = TimeUtil.changeTimezone(conn, sessionCalendar, null, toBeAdjusted, sessionCalendar.getTimeZone(), conn.getServerTimezoneTZ(), false);
/*     */                       
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 336 */                       newSql.append("'");
/*     */                       
/* 338 */                       String timezoneLiteral = inServerTimezone.toString();
/*     */                       
/* 340 */                       int indexOfDot = timezoneLiteral.indexOf(".");
/*     */                       
/* 342 */                       if (indexOfDot != -1) {
/* 343 */                         timezoneLiteral = timezoneLiteral.substring(0, indexOfDot);
/*     */                       }
/*     */                       
/* 346 */                       newSql.append(timezoneLiteral);
/*     */                     }
/*     */                     
/* 349 */                     newSql.append("'");
/*     */                   }
/*     */                   catch (NumberFormatException nfe)
/*     */                   {
/* 353 */                     throw SQLError.createSQLException("Syntax error in TIMESTAMP escape sequence '" + token + "'.", "S1009");
/*     */                   }
/*     */                 }
/*     */               }
/*     */               catch (NoSuchElementException e)
/*     */               {
/* 359 */                 throw SQLError.createSQLException("Syntax error for TIMESTAMP escape sequence '" + argument + "'", "42000");
/*     */               }
/*     */               
/*     */             }
/*     */           }
/* 364 */           else if (StringUtils.startsWithIgnoreCase(collapsedToken, "{t"))
/*     */           {
/* 366 */             int startPos = token.indexOf('\'') + 1;
/* 367 */             int endPos = token.lastIndexOf('\'');
/*     */             
/* 369 */             if ((startPos == -1) || (endPos == -1)) {
/* 370 */               newSql.append(token);
/*     */             }
/*     */             else {
/* 373 */               String argument = token.substring(startPos, endPos);
/*     */               try
/*     */               {
/* 376 */                 StringTokenizer st = new StringTokenizer(argument, " :");
/*     */                 
/* 378 */                 String hour = st.nextToken();
/* 379 */                 String minute = st.nextToken();
/* 380 */                 String second = st.nextToken();
/*     */                 
/* 382 */                 if (!conn.getUseTimezone()) {
/* 383 */                   String timeString = "'" + hour + ":" + minute + ":" + second + "'";
/*     */                   
/* 385 */                   newSql.append(timeString);
/*     */                 } else {
/* 387 */                   Calendar sessionCalendar = null;
/*     */                   
/* 389 */                   if (conn != null) {
/* 390 */                     sessionCalendar = conn.getCalendarInstanceForSessionOrNew();
/*     */                   } else {
/* 392 */                     sessionCalendar = new GregorianCalendar();
/*     */                   }
/*     */                   try
/*     */                   {
/* 396 */                     int hourInt = Integer.parseInt(hour);
/* 397 */                     int minuteInt = Integer.parseInt(minute);
/* 398 */                     int secondInt = Integer.parseInt(second);
/*     */                     
/* 400 */                     synchronized (sessionCalendar) {
/* 401 */                       Time toBeAdjusted = TimeUtil.fastTimeCreate(sessionCalendar, hourInt, minuteInt, secondInt);
/*     */                       
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 407 */                       Time inServerTimezone = TimeUtil.changeTimezone(conn, sessionCalendar, null, toBeAdjusted, sessionCalendar.getTimeZone(), conn.getServerTimezoneTZ(), false);
/*     */                       
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 416 */                       newSql.append("'");
/* 417 */                       newSql.append(inServerTimezone.toString());
/* 418 */                       newSql.append("'");
/*     */                     }
/*     */                   }
/*     */                   catch (NumberFormatException nfe) {
/* 422 */                     throw SQLError.createSQLException("Syntax error in TIMESTAMP escape sequence '" + token + "'.", "S1009");
/*     */                   }
/*     */                 }
/*     */               }
/*     */               catch (NoSuchElementException e)
/*     */               {
/* 428 */                 throw SQLError.createSQLException("Syntax error for escape sequence '" + argument + "'", "42000");
/*     */               }
/*     */               
/*     */             }
/*     */           }
/* 433 */           else if ((StringUtils.startsWithIgnoreCase(collapsedToken, "{call")) || (StringUtils.startsWithIgnoreCase(collapsedToken, "{?=call")))
/*     */           {
/*     */ 
/*     */ 
/*     */ 
/* 438 */             int startPos = StringUtils.indexOfIgnoreCase(token, "CALL") + 5;
/*     */             
/* 440 */             int endPos = token.length() - 1;
/*     */             
/* 442 */             if (StringUtils.startsWithIgnoreCase(collapsedToken, "{?=call"))
/*     */             {
/* 444 */               callingStoredFunction = true;
/* 445 */               newSql.append("SELECT ");
/* 446 */               newSql.append(token.substring(startPos, endPos));
/*     */             } else {
/* 448 */               callingStoredFunction = false;
/* 449 */               newSql.append("CALL ");
/* 450 */               newSql.append(token.substring(startPos, endPos));
/*     */             }
/*     */             
/* 453 */             for (int i = endPos - 1; i >= startPos; i--) {
/* 454 */               char c = token.charAt(i);
/*     */               
/* 456 */               if (!Character.isWhitespace(c))
/*     */               {
/*     */ 
/*     */ 
/* 460 */                 if (c == ')') break;
/* 461 */                 newSql.append("()"); break;
/*     */               }
/*     */               
/*     */             }
/*     */             
/*     */           }
/* 467 */           else if (StringUtils.startsWithIgnoreCase(collapsedToken, "{oj"))
/*     */           {
/*     */ 
/*     */ 
/* 471 */             newSql.append(token);
/*     */           }
/*     */         } else {
/* 474 */           newSql.append(token);
/*     */         }
/*     */       }
/*     */     }
/*     */     
/* 479 */     String escapedSql = newSql.toString();
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 485 */     if (replaceEscapeSequence) {
/* 486 */       String currentSql = escapedSql;
/*     */       
/* 488 */       while (currentSql.indexOf(escapeSequence) != -1) {
/* 489 */         int escapePos = currentSql.indexOf(escapeSequence);
/* 490 */         String lhs = currentSql.substring(0, escapePos);
/* 491 */         String rhs = currentSql.substring(escapePos + 1, currentSql.length());
/*     */         
/* 493 */         currentSql = lhs + "\\" + rhs;
/*     */       }
/*     */       
/* 496 */       escapedSql = currentSql;
/*     */     }
/*     */     
/* 499 */     EscapeProcessorResult epr = new EscapeProcessorResult();
/* 500 */     epr.escapedSql = escapedSql;
/* 501 */     epr.callingStoredFunction = callingStoredFunction;
/*     */     
/* 503 */     if (usesVariables != 1) {
/* 504 */       if (escapeTokenizer.sawVariableUse()) {
/* 505 */         epr.usesVariables = 1;
/*     */       } else {
/* 507 */         epr.usesVariables = 0;
/*     */       }
/*     */     }
/*     */     
/* 511 */     return epr;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private static String processConvertToken(String functionToken, boolean serverSupportsConvertFn)
/*     */     throws SQLException
/*     */   {
/* 554 */     int firstIndexOfParen = functionToken.indexOf("(");
/*     */     
/* 556 */     if (firstIndexOfParen == -1) {
/* 557 */       throw SQLError.createSQLException("Syntax error while processing {fn convert (... , ...)} token, missing opening parenthesis in token '" + functionToken + "'.", "42000");
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/* 563 */     int tokenLength = functionToken.length();
/*     */     
/* 565 */     int indexOfComma = functionToken.lastIndexOf(",");
/*     */     
/* 567 */     if (indexOfComma == -1) {
/* 568 */       throw SQLError.createSQLException("Syntax error while processing {fn convert (... , ...)} token, missing comma in token '" + functionToken + "'.", "42000");
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/* 574 */     int indexOfCloseParen = functionToken.indexOf(')', indexOfComma);
/*     */     
/* 576 */     if (indexOfCloseParen == -1) {
/* 577 */       throw SQLError.createSQLException("Syntax error while processing {fn convert (... , ...)} token, missing closing parenthesis in token '" + functionToken + "'.", "42000");
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 584 */     String expression = functionToken.substring(firstIndexOfParen + 1, indexOfComma);
/*     */     
/* 586 */     String type = functionToken.substring(indexOfComma + 1, indexOfCloseParen);
/*     */     
/*     */ 
/* 589 */     String newType = null;
/*     */     
/* 591 */     String trimmedType = type.trim();
/*     */     
/* 593 */     if (StringUtils.startsWithIgnoreCase(trimmedType, "SQL_")) {
/* 594 */       trimmedType = trimmedType.substring(4, trimmedType.length());
/*     */     }
/*     */     
/* 597 */     if (serverSupportsConvertFn) {
/* 598 */       newType = (String)JDBC_CONVERT_TO_MYSQL_TYPE_MAP.get(trimmedType.toUpperCase(Locale.ENGLISH));
/*     */     }
/*     */     else {
/* 601 */       newType = (String)JDBC_NO_CONVERT_TO_MYSQL_EXPRESSION_MAP.get(trimmedType.toUpperCase(Locale.ENGLISH));
/*     */       
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 611 */       if (newType == null) {
/* 612 */         throw SQLError.createSQLException("Can't find conversion re-write for type '" + type + "' that is applicable for this server version while processing escape tokens.", "S1000");
/*     */       }
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 620 */     if (newType == null) {
/* 621 */       throw SQLError.createSQLException("Unsupported conversion type '" + type.trim() + "' found while processing escape token.", "S1000");
/*     */     }
/*     */     
/*     */ 
/*     */ 
/* 626 */     int replaceIndex = newType.indexOf("?");
/*     */     
/* 628 */     if (replaceIndex != -1) {
/* 629 */       StringBuffer convertRewrite = new StringBuffer(newType.substring(0, replaceIndex));
/*     */       
/* 631 */       convertRewrite.append(expression);
/* 632 */       convertRewrite.append(newType.substring(replaceIndex + 1, newType.length()));
/*     */       
/*     */ 
/* 635 */       return convertRewrite.toString();
/*     */     }
/*     */     
/* 638 */     StringBuffer castRewrite = new StringBuffer("CAST(");
/* 639 */     castRewrite.append(expression);
/* 640 */     castRewrite.append(" AS ");
/* 641 */     castRewrite.append(newType);
/* 642 */     castRewrite.append(")");
/*     */     
/* 644 */     return castRewrite.toString();
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
/*     */   private static String removeWhitespace(String toCollapse)
/*     */   {
/* 658 */     if (toCollapse == null) {
/* 659 */       return null;
/*     */     }
/*     */     
/* 662 */     int length = toCollapse.length();
/*     */     
/* 664 */     StringBuffer collapsed = new StringBuffer(length);
/*     */     
/* 666 */     for (int i = 0; i < length; i++) {
/* 667 */       char c = toCollapse.charAt(i);
/*     */       
/* 669 */       if (!Character.isWhitespace(c)) {
/* 670 */         collapsed.append(c);
/*     */       }
/*     */     }
/*     */     
/* 674 */     return collapsed.toString();
/*     */   }
/*     */ }


/* Location:              E:\java\java学习\hutubill\lib\all.jar!\com\mysql\jdbc\EscapeProcessor.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version:       0.7.1
 */
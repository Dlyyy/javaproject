/*      */ package com.mysql.jdbc;
/*      */ 
/*      */ import java.io.ByteArrayOutputStream;
/*      */ import java.io.IOException;
/*      */ import java.io.StringReader;
/*      */ import java.io.UnsupportedEncodingException;
/*      */ import java.lang.reflect.InvocationTargetException;
/*      */ import java.lang.reflect.Method;
/*      */ import java.math.BigDecimal;
/*      */ import java.sql.SQLException;
/*      */ import java.util.ArrayList;
/*      */ import java.util.List;
/*      */ import java.util.StringTokenizer;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ public class StringUtils
/*      */ {
/*      */   private static final int BYTE_RANGE = 256;
/*   51 */   private static byte[] allBytes = new byte['Ā'];
/*      */   
/*   53 */   private static char[] byteToChars = new char['Ā'];
/*      */   
/*      */   private static Method toPlainStringMethod;
/*      */   
/*      */   static final int WILD_COMPARE_MATCH_NO_WILD = 0;
/*      */   
/*      */   static final int WILD_COMPARE_MATCH_WITH_WILD = 1;
/*      */   static final int WILD_COMPARE_NO_MATCH = -1;
/*      */   
/*      */   static
/*      */   {
/*   64 */     for (int i = -128; i <= 127; i++) {
/*   65 */       allBytes[(i - -128)] = ((byte)i);
/*      */     }
/*      */     
/*   68 */     String allBytesString = new String(allBytes, 0, 255);
/*      */     
/*      */ 
/*   71 */     int allBytesStringLen = allBytesString.length();
/*      */     
/*   73 */     for (int i = 0; 
/*   74 */         (i < 255) && (i < allBytesStringLen); i++) {
/*   75 */       byteToChars[i] = allBytesString.charAt(i);
/*      */     }
/*      */     try
/*      */     {
/*   79 */       toPlainStringMethod = BigDecimal.class.getMethod("toPlainString", new Class[0]);
/*      */     }
/*      */     catch (NoSuchMethodException nsme) {}
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public static String consistentToString(BigDecimal decimal)
/*      */   {
/*   96 */     if (decimal == null) {
/*   97 */       return null;
/*      */     }
/*      */     
/*  100 */     if (toPlainStringMethod != null) {
/*      */       try {
/*  102 */         return (String)toPlainStringMethod.invoke(decimal, null);
/*      */       }
/*      */       catch (InvocationTargetException invokeEx) {}catch (IllegalAccessException accessEx) {}
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*  110 */     return decimal.toString();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public static final String dumpAsHex(byte[] byteBuffer, int length)
/*      */   {
/*  124 */     StringBuffer outputBuf = new StringBuffer(length * 4);
/*      */     
/*  126 */     int p = 0;
/*  127 */     int rows = length / 8;
/*      */     
/*  129 */     for (int i = 0; (i < rows) && (p < length); i++) {
/*  130 */       int ptemp = p;
/*      */       
/*  132 */       for (int j = 0; j < 8; j++) {
/*  133 */         String hexVal = Integer.toHexString(byteBuffer[ptemp] & 0xFF);
/*      */         
/*  135 */         if (hexVal.length() == 1) {
/*  136 */           hexVal = "0" + hexVal;
/*      */         }
/*      */         
/*  139 */         outputBuf.append(hexVal + " ");
/*  140 */         ptemp++;
/*      */       }
/*      */       
/*  143 */       outputBuf.append("    ");
/*      */       
/*  145 */       for (int j = 0; j < 8; j++) {
/*  146 */         if ((byteBuffer[p] > 32) && (byteBuffer[p] < Byte.MAX_VALUE)) {
/*  147 */           outputBuf.append((char)byteBuffer[p] + " ");
/*      */         } else {
/*  149 */           outputBuf.append(". ");
/*      */         }
/*      */         
/*  152 */         p++;
/*      */       }
/*      */       
/*  155 */       outputBuf.append("\n");
/*      */     }
/*      */     
/*  158 */     int n = 0;
/*      */     
/*  160 */     for (int i = p; i < length; i++) {
/*  161 */       String hexVal = Integer.toHexString(byteBuffer[i] & 0xFF);
/*      */       
/*  163 */       if (hexVal.length() == 1) {
/*  164 */         hexVal = "0" + hexVal;
/*      */       }
/*      */       
/*  167 */       outputBuf.append(hexVal + " ");
/*  168 */       n++;
/*      */     }
/*      */     
/*  171 */     for (int i = n; i < 8; i++) {
/*  172 */       outputBuf.append("   ");
/*      */     }
/*      */     
/*  175 */     outputBuf.append("    ");
/*      */     
/*  177 */     for (int i = p; i < length; i++) {
/*  178 */       if ((byteBuffer[i] > 32) && (byteBuffer[i] < Byte.MAX_VALUE)) {
/*  179 */         outputBuf.append((char)byteBuffer[i] + " ");
/*      */       } else {
/*  181 */         outputBuf.append(". ");
/*      */       }
/*      */     }
/*      */     
/*  185 */     outputBuf.append("\n");
/*      */     
/*  187 */     return outputBuf.toString();
/*      */   }
/*      */   
/*      */   private static boolean endsWith(byte[] dataFrom, String suffix) {
/*  191 */     for (int i = 1; i <= suffix.length(); i++) {
/*  192 */       int dfOffset = dataFrom.length - i;
/*  193 */       int suffixOffset = suffix.length() - i;
/*  194 */       if (dataFrom[dfOffset] != suffix.charAt(suffixOffset)) {
/*  195 */         return false;
/*      */       }
/*      */     }
/*  198 */     return true;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public static byte[] escapeEasternUnicodeByteStream(byte[] origBytes, String origString, int offset, int length)
/*      */   {
/*  218 */     if ((origBytes == null) || (origBytes.length == 0)) {
/*  219 */       return origBytes;
/*      */     }
/*      */     
/*  222 */     int bytesLen = origBytes.length;
/*  223 */     int bufIndex = 0;
/*  224 */     int strIndex = 0;
/*      */     
/*  226 */     ByteArrayOutputStream bytesOut = new ByteArrayOutputStream(bytesLen);
/*      */     for (;;)
/*      */     {
/*  229 */       if (origString.charAt(strIndex) == '\\')
/*      */       {
/*  231 */         bytesOut.write(origBytes[(bufIndex++)]);
/*      */ 
/*      */       }
/*      */       else
/*      */       {
/*  236 */         int loByte = origBytes[bufIndex];
/*      */         
/*  238 */         if (loByte < 0) {
/*  239 */           loByte += 256;
/*      */         }
/*      */         
/*      */ 
/*  243 */         bytesOut.write(loByte);
/*      */         
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  261 */         if (loByte >= 128) {
/*  262 */           if (bufIndex < bytesLen - 1) {
/*  263 */             int hiByte = origBytes[(bufIndex + 1)];
/*      */             
/*  265 */             if (hiByte < 0) {
/*  266 */               hiByte += 256;
/*      */             }
/*      */             
/*      */ 
/*      */ 
/*  271 */             bytesOut.write(hiByte);
/*  272 */             bufIndex++;
/*      */             
/*      */ 
/*  275 */             if (hiByte == 92) {
/*  276 */               bytesOut.write(hiByte);
/*      */             }
/*      */           }
/*  279 */         } else if ((loByte == 92) && 
/*  280 */           (bufIndex < bytesLen - 1)) {
/*  281 */           int hiByte = origBytes[(bufIndex + 1)];
/*      */           
/*  283 */           if (hiByte < 0) {
/*  284 */             hiByte += 256;
/*      */           }
/*      */           
/*  287 */           if (hiByte == 98)
/*      */           {
/*  289 */             bytesOut.write(92);
/*  290 */             bytesOut.write(98);
/*  291 */             bufIndex++;
/*      */           }
/*      */         }
/*      */         
/*      */ 
/*  296 */         bufIndex++;
/*      */       }
/*      */       
/*  299 */       if (bufIndex >= bytesLen) {
/*      */         break;
/*      */       }
/*      */       
/*      */ 
/*  304 */       strIndex++;
/*      */     }
/*      */     
/*  307 */     return bytesOut.toByteArray();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public static char firstNonWsCharUc(String searchIn)
/*      */   {
/*  319 */     return firstNonWsCharUc(searchIn, 0);
/*      */   }
/*      */   
/*      */   public static char firstNonWsCharUc(String searchIn, int startAt) {
/*  323 */     if (searchIn == null) {
/*  324 */       return '\000';
/*      */     }
/*      */     
/*  327 */     int length = searchIn.length();
/*      */     
/*  329 */     for (int i = startAt; i < length; i++) {
/*  330 */       char c = searchIn.charAt(i);
/*      */       
/*  332 */       if (!Character.isWhitespace(c)) {
/*  333 */         return Character.toUpperCase(c);
/*      */       }
/*      */     }
/*      */     
/*  337 */     return '\000';
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public static final String fixDecimalExponent(String dString)
/*      */   {
/*  350 */     int ePos = dString.indexOf("E");
/*      */     
/*  352 */     if (ePos == -1) {
/*  353 */       ePos = dString.indexOf("e");
/*      */     }
/*      */     
/*  356 */     if ((ePos != -1) && 
/*  357 */       (dString.length() > ePos + 1)) {
/*  358 */       char maybeMinusChar = dString.charAt(ePos + 1);
/*      */       
/*  360 */       if ((maybeMinusChar != '-') && (maybeMinusChar != '+')) {
/*  361 */         StringBuffer buf = new StringBuffer(dString.length() + 1);
/*  362 */         buf.append(dString.substring(0, ePos + 1));
/*  363 */         buf.append('+');
/*  364 */         buf.append(dString.substring(ePos + 1, dString.length()));
/*  365 */         dString = buf.toString();
/*      */       }
/*      */     }
/*      */     
/*      */ 
/*  370 */     return dString;
/*      */   }
/*      */   
/*      */   public static final byte[] getBytes(char[] c, SingleByteCharsetConverter converter, String encoding, String serverEncoding, boolean parserKnowsUnicode)
/*      */     throws SQLException
/*      */   {
/*      */     try
/*      */     {
/*  378 */       byte[] b = null;
/*      */       String s;
/*  380 */       if (converter != null) {
/*  381 */         b = converter.toBytes(c);
/*  382 */       } else if (encoding == null) {
/*  383 */         b = new String(c).getBytes();
/*      */       } else {
/*  385 */         s = new String(c);
/*      */         
/*  387 */         b = s.getBytes(encoding);
/*      */         
/*  389 */         if ((!parserKnowsUnicode) && ((encoding.equalsIgnoreCase("SJIS")) || (encoding.equalsIgnoreCase("BIG5")) || (encoding.equalsIgnoreCase("GBK"))))
/*      */         {
/*      */ 
/*      */ 
/*  393 */           if (encoding.equalsIgnoreCase(serverEncoding)) {} } }
/*  394 */       return escapeEasternUnicodeByteStream(b, s, 0, s.length());
/*      */ 
/*      */ 
/*      */     }
/*      */     catch (UnsupportedEncodingException uee)
/*      */     {
/*      */ 
/*  401 */       throw SQLError.createSQLException(Messages.getString("StringUtils.5") + encoding + Messages.getString("StringUtils.6"), "S1009");
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public static final byte[] getBytes(char[] c, SingleByteCharsetConverter converter, String encoding, String serverEncoding, int offset, int length, boolean parserKnowsUnicode)
/*      */     throws SQLException
/*      */   {
/*      */     try
/*      */     {
/*  412 */       byte[] b = null;
/*      */       String s;
/*  414 */       if (converter != null) {
/*  415 */         b = converter.toBytes(c, offset, length);
/*  416 */       } else if (encoding == null) {
/*  417 */         byte[] temp = new String(c, offset, length).getBytes();
/*      */         
/*  419 */         length = temp.length;
/*      */         
/*  421 */         b = new byte[length];
/*  422 */         System.arraycopy(temp, 0, b, 0, length);
/*      */       } else {
/*  424 */         s = new String(c, offset, length);
/*      */         
/*  426 */         byte[] temp = s.getBytes(encoding);
/*      */         
/*  428 */         length = temp.length;
/*      */         
/*  430 */         b = new byte[length];
/*  431 */         System.arraycopy(temp, 0, b, 0, length);
/*      */         
/*  433 */         if ((!parserKnowsUnicode) && ((encoding.equalsIgnoreCase("SJIS")) || (encoding.equalsIgnoreCase("BIG5")) || (encoding.equalsIgnoreCase("GBK"))))
/*      */         {
/*      */ 
/*      */ 
/*  437 */           if (encoding.equalsIgnoreCase(serverEncoding)) {} } }
/*  438 */       return escapeEasternUnicodeByteStream(b, s, offset, length);
/*      */ 
/*      */ 
/*      */     }
/*      */     catch (UnsupportedEncodingException uee)
/*      */     {
/*      */ 
/*  445 */       throw SQLError.createSQLException(Messages.getString("StringUtils.10") + encoding + Messages.getString("StringUtils.11"), "S1009");
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public static final byte[] getBytes(char[] c, String encoding, String serverEncoding, boolean parserKnowsUnicode, Connection conn)
/*      */     throws SQLException
/*      */   {
/*      */     try
/*      */     {
/*  456 */       SingleByteCharsetConverter converter = null;
/*      */       
/*  458 */       if (conn != null) {
/*  459 */         converter = conn.getCharsetConverter(encoding);
/*      */       } else {
/*  461 */         converter = SingleByteCharsetConverter.getInstance(encoding, null);
/*      */       }
/*      */       
/*  464 */       return getBytes(c, converter, encoding, serverEncoding, parserKnowsUnicode);
/*      */     }
/*      */     catch (UnsupportedEncodingException uee) {
/*  467 */       throw SQLError.createSQLException(Messages.getString("StringUtils.0") + encoding + Messages.getString("StringUtils.1"), "S1009");
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public static final byte[] getBytes(String s, SingleByteCharsetConverter converter, String encoding, String serverEncoding, boolean parserKnowsUnicode)
/*      */     throws SQLException
/*      */   {
/*      */     try
/*      */     {
/*  498 */       byte[] b = null;
/*      */       
/*  500 */       if (converter != null) {
/*  501 */         b = converter.toBytes(s);
/*  502 */       } else if (encoding == null) {
/*  503 */         b = s.getBytes();
/*      */       } else {
/*  505 */         b = s.getBytes(encoding);
/*      */         
/*  507 */         if ((!parserKnowsUnicode) && ((encoding.equalsIgnoreCase("SJIS")) || (encoding.equalsIgnoreCase("BIG5")) || (encoding.equalsIgnoreCase("GBK"))))
/*      */         {
/*      */ 
/*      */ 
/*  511 */           if (encoding.equalsIgnoreCase(serverEncoding)) {} } }
/*  512 */       return escapeEasternUnicodeByteStream(b, s, 0, s.length());
/*      */ 
/*      */ 
/*      */     }
/*      */     catch (UnsupportedEncodingException uee)
/*      */     {
/*      */ 
/*  519 */       throw SQLError.createSQLException(Messages.getString("StringUtils.5") + encoding + Messages.getString("StringUtils.6"), "S1009");
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public static final byte[] getBytes(String s, SingleByteCharsetConverter converter, String encoding, String serverEncoding, int offset, int length, boolean parserKnowsUnicode)
/*      */     throws SQLException
/*      */   {
/*      */     try
/*      */     {
/*  553 */       byte[] b = null;
/*      */       
/*  555 */       if (converter != null) {
/*  556 */         b = converter.toBytes(s, offset, length);
/*  557 */       } else if (encoding == null) {
/*  558 */         byte[] temp = s.substring(offset, offset + length).getBytes();
/*      */         
/*  560 */         length = temp.length;
/*      */         
/*  562 */         b = new byte[length];
/*  563 */         System.arraycopy(temp, 0, b, 0, length);
/*      */       }
/*      */       else {
/*  566 */         byte[] temp = s.substring(offset, offset + length).getBytes(encoding);
/*      */         
/*      */ 
/*  569 */         length = temp.length;
/*      */         
/*  571 */         b = new byte[length];
/*  572 */         System.arraycopy(temp, 0, b, 0, length);
/*      */         
/*  574 */         if ((!parserKnowsUnicode) && ((encoding.equalsIgnoreCase("SJIS")) || (encoding.equalsIgnoreCase("BIG5")) || (encoding.equalsIgnoreCase("GBK"))))
/*      */         {
/*      */ 
/*      */ 
/*  578 */           if (encoding.equalsIgnoreCase(serverEncoding)) {} } }
/*  579 */       return escapeEasternUnicodeByteStream(b, s, offset, length);
/*      */ 
/*      */ 
/*      */     }
/*      */     catch (UnsupportedEncodingException uee)
/*      */     {
/*      */ 
/*  586 */       throw SQLError.createSQLException(Messages.getString("StringUtils.10") + encoding + Messages.getString("StringUtils.11"), "S1009");
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public static final byte[] getBytes(String s, String encoding, String serverEncoding, boolean parserKnowsUnicode, Connection conn)
/*      */     throws SQLException
/*      */   {
/*      */     try
/*      */     {
/*  612 */       SingleByteCharsetConverter converter = null;
/*      */       
/*  614 */       if (conn != null) {
/*  615 */         converter = conn.getCharsetConverter(encoding);
/*      */       } else {
/*  617 */         converter = SingleByteCharsetConverter.getInstance(encoding, null);
/*      */       }
/*      */       
/*  620 */       return getBytes(s, converter, encoding, serverEncoding, parserKnowsUnicode);
/*      */     }
/*      */     catch (UnsupportedEncodingException uee) {
/*  623 */       throw SQLError.createSQLException(Messages.getString("StringUtils.0") + encoding + Messages.getString("StringUtils.1"), "S1009");
/*      */     }
/*      */   }
/*      */   
/*      */   public static int getInt(byte[] buf, int offset, int endPos)
/*      */     throws NumberFormatException
/*      */   {
/*  630 */     int base = 10;
/*      */     
/*  632 */     int s = offset;
/*      */     
/*      */ 
/*  635 */     while ((Character.isWhitespace((char)buf[s])) && (s < endPos)) {
/*  636 */       s++;
/*      */     }
/*      */     
/*  639 */     if (s == endPos) {
/*  640 */       throw new NumberFormatException(new String(buf));
/*      */     }
/*      */     
/*      */ 
/*  644 */     boolean negative = false;
/*      */     
/*  646 */     if ((char)buf[s] == '-') {
/*  647 */       negative = true;
/*  648 */       s++;
/*  649 */     } else if ((char)buf[s] == '+') {
/*  650 */       s++;
/*      */     }
/*      */     
/*      */ 
/*  654 */     int save = s;
/*      */     
/*  656 */     int cutoff = Integer.MAX_VALUE / base;
/*  657 */     int cutlim = Integer.MAX_VALUE % base;
/*      */     
/*  659 */     if (negative) {
/*  660 */       cutlim++;
/*      */     }
/*      */     
/*  663 */     boolean overflow = false;
/*      */     
/*  665 */     int i = 0;
/*  667 */     for (; 
/*  667 */         s < endPos; s++) {
/*  668 */       char c = (char)buf[s];
/*      */       
/*  670 */       if (Character.isDigit(c)) {
/*  671 */         c = (char)(c - '0');
/*  672 */       } else { if (!Character.isLetter(c)) break;
/*  673 */         c = (char)(Character.toUpperCase(c) - 'A' + 10);
/*      */       }
/*      */       
/*      */ 
/*      */ 
/*  678 */       if (c >= base) {
/*      */         break;
/*      */       }
/*      */       
/*      */ 
/*  683 */       if ((i > cutoff) || ((i == cutoff) && (c > cutlim))) {
/*  684 */         overflow = true;
/*      */       } else {
/*  686 */         i *= base;
/*  687 */         i += c;
/*      */       }
/*      */     }
/*      */     
/*  691 */     if (s == save) {
/*  692 */       throw new NumberFormatException(new String(buf));
/*      */     }
/*      */     
/*  695 */     if (overflow) {
/*  696 */       throw new NumberFormatException(new String(buf));
/*      */     }
/*      */     
/*      */ 
/*  700 */     return negative ? -i : i;
/*      */   }
/*      */   
/*      */   public static int getInt(byte[] buf) throws NumberFormatException {
/*  704 */     return getInt(buf, 0, buf.length);
/*      */   }
/*      */   
/*      */   public static long getLong(byte[] buf) throws NumberFormatException {
/*  708 */     int base = 10;
/*      */     
/*  710 */     int s = 0;
/*      */     
/*      */ 
/*  713 */     while ((Character.isWhitespace((char)buf[s])) && (s < buf.length)) {
/*  714 */       s++;
/*      */     }
/*      */     
/*  717 */     if (s == buf.length) {
/*  718 */       throw new NumberFormatException(new String(buf));
/*      */     }
/*      */     
/*      */ 
/*  722 */     boolean negative = false;
/*      */     
/*  724 */     if ((char)buf[s] == '-') {
/*  725 */       negative = true;
/*  726 */       s++;
/*  727 */     } else if ((char)buf[s] == '+') {
/*  728 */       s++;
/*      */     }
/*      */     
/*      */ 
/*  732 */     int save = s;
/*      */     
/*  734 */     long cutoff = Long.MAX_VALUE / base;
/*  735 */     long cutlim = (int)(Long.MAX_VALUE % base);
/*      */     
/*  737 */     if (negative) {
/*  738 */       cutlim += 1L;
/*      */     }
/*      */     
/*  741 */     boolean overflow = false;
/*  742 */     long i = 0L;
/*  744 */     for (; 
/*  744 */         s < buf.length; s++) {
/*  745 */       char c = (char)buf[s];
/*      */       
/*  747 */       if (Character.isDigit(c)) {
/*  748 */         c = (char)(c - '0');
/*  749 */       } else { if (!Character.isLetter(c)) break;
/*  750 */         c = (char)(Character.toUpperCase(c) - 'A' + 10);
/*      */       }
/*      */       
/*      */ 
/*      */ 
/*  755 */       if (c >= base) {
/*      */         break;
/*      */       }
/*      */       
/*      */ 
/*  760 */       if ((i > cutoff) || ((i == cutoff) && (c > cutlim))) {
/*  761 */         overflow = true;
/*      */       } else {
/*  763 */         i *= base;
/*  764 */         i += c;
/*      */       }
/*      */     }
/*      */     
/*  768 */     if (s == save) {
/*  769 */       throw new NumberFormatException(new String(buf));
/*      */     }
/*      */     
/*  772 */     if (overflow) {
/*  773 */       throw new NumberFormatException(new String(buf));
/*      */     }
/*      */     
/*      */ 
/*  777 */     return negative ? -i : i;
/*      */   }
/*      */   
/*      */   public static short getShort(byte[] buf) throws NumberFormatException {
/*  781 */     short base = 10;
/*      */     
/*  783 */     int s = 0;
/*      */     
/*      */ 
/*  786 */     while ((Character.isWhitespace((char)buf[s])) && (s < buf.length)) {
/*  787 */       s++;
/*      */     }
/*      */     
/*  790 */     if (s == buf.length) {
/*  791 */       throw new NumberFormatException(new String(buf));
/*      */     }
/*      */     
/*      */ 
/*  795 */     boolean negative = false;
/*      */     
/*  797 */     if ((char)buf[s] == '-') {
/*  798 */       negative = true;
/*  799 */       s++;
/*  800 */     } else if ((char)buf[s] == '+') {
/*  801 */       s++;
/*      */     }
/*      */     
/*      */ 
/*  805 */     int save = s;
/*      */     
/*  807 */     short cutoff = (short)(Short.MAX_VALUE / base);
/*  808 */     short cutlim = (short)(Short.MAX_VALUE % base);
/*      */     
/*  810 */     if (negative) {
/*  811 */       cutlim = (short)(cutlim + 1);
/*      */     }
/*      */     
/*  814 */     boolean overflow = false;
/*  815 */     short i = 0;
/*  817 */     for (; 
/*  817 */         s < buf.length; s++) {
/*  818 */       char c = (char)buf[s];
/*      */       
/*  820 */       if (Character.isDigit(c)) {
/*  821 */         c = (char)(c - '0');
/*  822 */       } else { if (!Character.isLetter(c)) break;
/*  823 */         c = (char)(Character.toUpperCase(c) - 'A' + 10);
/*      */       }
/*      */       
/*      */ 
/*      */ 
/*  828 */       if (c >= base) {
/*      */         break;
/*      */       }
/*      */       
/*      */ 
/*  833 */       if ((i > cutoff) || ((i == cutoff) && (c > cutlim))) {
/*  834 */         overflow = true;
/*      */       } else {
/*  836 */         i = (short)(i * base);
/*  837 */         i = (short)(i + c);
/*      */       }
/*      */     }
/*      */     
/*  841 */     if (s == save) {
/*  842 */       throw new NumberFormatException(new String(buf));
/*      */     }
/*      */     
/*  845 */     if (overflow) {
/*  846 */       throw new NumberFormatException(new String(buf));
/*      */     }
/*      */     
/*      */ 
/*  850 */     return negative ? (short)-i : i;
/*      */   }
/*      */   
/*      */   public static final int indexOfIgnoreCase(int startingPosition, String searchIn, String searchFor)
/*      */   {
/*  855 */     if ((searchIn == null) || (searchFor == null) || (startingPosition > searchIn.length()))
/*      */     {
/*  857 */       return -1;
/*      */     }
/*      */     
/*  860 */     int patternLength = searchFor.length();
/*  861 */     int stringLength = searchIn.length();
/*  862 */     int stopSearchingAt = stringLength - patternLength;
/*      */     
/*  864 */     int i = startingPosition;
/*      */     
/*  866 */     if (patternLength == 0) {
/*  867 */       return -1;
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*  872 */     char firstCharOfPatternUc = Character.toUpperCase(searchFor.charAt(0));
/*  873 */     char firstCharOfPatternLc = Character.toLowerCase(searchFor.charAt(0));
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*  878 */     while ((i < stopSearchingAt) && (Character.toUpperCase(searchIn.charAt(i)) != firstCharOfPatternUc) && (Character.toLowerCase(searchIn.charAt(i)) != firstCharOfPatternLc)) {
/*  879 */       i++;
/*      */     }
/*      */     
/*  882 */     if (i > stopSearchingAt) {
/*  883 */       return -1;
/*      */     }
/*      */     
/*  886 */     int j = i + 1;
/*  887 */     int end = j + patternLength - 1;
/*      */     
/*  889 */     int k = 1;
/*      */     for (;;) {
/*  891 */       if (j >= end) break label209;
/*  892 */       int searchInPos = j++;
/*  893 */       int searchForPos = k++;
/*      */       
/*  895 */       if (Character.toUpperCase(searchIn.charAt(searchInPos)) != Character.toUpperCase(searchFor.charAt(searchForPos)))
/*      */       {
/*  897 */         i++;
/*      */         
/*      */ 
/*  900 */         break;
/*      */       }
/*      */       
/*      */ 
/*      */ 
/*      */ 
/*  906 */       if (Character.toLowerCase(searchIn.charAt(searchInPos)) != Character.toLowerCase(searchFor.charAt(searchForPos)))
/*      */       {
/*  908 */         i++;
/*      */         
/*      */ 
/*  911 */         break;
/*      */       }
/*      */     }
/*      */     label209:
/*  915 */     return i;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public static final int indexOfIgnoreCase(String searchIn, String searchFor)
/*      */   {
/*  930 */     return indexOfIgnoreCase(0, searchIn, searchFor);
/*      */   }
/*      */   
/*      */ 
/*      */   public static int indexOfIgnoreCaseRespectMarker(int startAt, String src, String target, String marker, String markerCloses, boolean allowBackslashEscapes)
/*      */   {
/*  936 */     char contextMarker = '\000';
/*  937 */     boolean escaped = false;
/*  938 */     int markerTypeFound = -1;
/*  939 */     int srcLength = src.length();
/*  940 */     int ind = 0;
/*      */     
/*  942 */     for (int i = startAt; i < srcLength; i++) {
/*  943 */       char c = src.charAt(i);
/*      */       
/*  945 */       if ((allowBackslashEscapes) && (c == '\\')) {
/*  946 */         escaped = !escaped;
/*  947 */       } else if ((markerTypeFound != -1) && (c == markerCloses.charAt(markerTypeFound)) && (!escaped)) {
/*  948 */         contextMarker = '\000';
/*  949 */         markerTypeFound = -1;
/*  950 */       } else if (((ind = marker.indexOf(c)) != -1) && (!escaped) && (contextMarker == 0))
/*      */       {
/*  952 */         markerTypeFound = ind;
/*  953 */         contextMarker = c;
/*  954 */       } else if ((c == target.charAt(0)) && (!escaped) && (contextMarker == 0))
/*      */       {
/*  956 */         if (indexOfIgnoreCase(i, src, target) != -1) {
/*  957 */           return i;
/*      */         }
/*      */       }
/*      */     }
/*  961 */     return -1;
/*      */   }
/*      */   
/*      */ 
/*      */   public static int indexOfIgnoreCaseRespectQuotes(int startAt, String src, String target, char quoteChar, boolean allowBackslashEscapes)
/*      */   {
/*  967 */     char contextMarker = '\000';
/*  968 */     boolean escaped = false;
/*      */     
/*  970 */     int srcLength = src.length();
/*      */     
/*  972 */     for (int i = startAt; i < srcLength; i++) {
/*  973 */       char c = src.charAt(i);
/*      */       
/*  975 */       if ((allowBackslashEscapes) && (c == '\\')) {
/*  976 */         escaped = !escaped;
/*  977 */       } else if ((c == contextMarker) && (!escaped)) {
/*  978 */         contextMarker = '\000';
/*  979 */       } else if ((c == quoteChar) && (!escaped) && (contextMarker == 0))
/*      */       {
/*  981 */         contextMarker = c;
/*      */ 
/*      */       }
/*  984 */       else if (((Character.toUpperCase(c) == Character.toUpperCase(target.charAt(0))) || (Character.toLowerCase(c) == Character.toLowerCase(target.charAt(0)))) && (!escaped) && (contextMarker == 0))
/*      */       {
/*      */ 
/*  987 */         if (startsWithIgnoreCase(src, i, target)) {
/*  988 */           return i;
/*      */         }
/*      */       }
/*      */     }
/*  992 */     return -1;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public static final List split(String stringToSplit, String delimitter, boolean trim)
/*      */   {
/* 1013 */     if (stringToSplit == null) {
/* 1014 */       return new ArrayList();
/*      */     }
/*      */     
/* 1017 */     if (delimitter == null) {
/* 1018 */       throw new IllegalArgumentException();
/*      */     }
/*      */     
/* 1021 */     StringTokenizer tokenizer = new StringTokenizer(stringToSplit, delimitter, false);
/*      */     
/*      */ 
/* 1024 */     List splitTokens = new ArrayList(tokenizer.countTokens());
/*      */     
/* 1026 */     while (tokenizer.hasMoreTokens()) {
/* 1027 */       String token = tokenizer.nextToken();
/*      */       
/* 1029 */       if (trim) {
/* 1030 */         token = token.trim();
/*      */       }
/*      */       
/* 1033 */       splitTokens.add(token);
/*      */     }
/*      */     
/* 1036 */     return splitTokens;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public static final List split(String stringToSplit, String delimiter, String markers, String markerCloses, boolean trim)
/*      */   {
/* 1056 */     if (stringToSplit == null) {
/* 1057 */       return new ArrayList();
/*      */     }
/*      */     
/* 1060 */     if (delimiter == null) {
/* 1061 */       throw new IllegalArgumentException();
/*      */     }
/*      */     
/* 1064 */     int delimPos = 0;
/* 1065 */     int currentPos = 0;
/*      */     
/* 1067 */     List splitTokens = new ArrayList();
/*      */     
/*      */ 
/* 1070 */     while ((delimPos = indexOfIgnoreCaseRespectMarker(currentPos, stringToSplit, delimiter, markers, markerCloses, false)) != -1) {
/* 1071 */       String token = stringToSplit.substring(currentPos, delimPos);
/*      */       
/* 1073 */       if (trim) {
/* 1074 */         token = token.trim();
/*      */       }
/*      */       
/* 1077 */       splitTokens.add(token);
/* 1078 */       currentPos = delimPos + 1;
/*      */     }
/*      */     
/* 1081 */     if (currentPos < stringToSplit.length()) {
/* 1082 */       String token = stringToSplit.substring(currentPos);
/*      */       
/* 1084 */       if (trim) {
/* 1085 */         token = token.trim();
/*      */       }
/*      */       
/* 1088 */       splitTokens.add(token);
/*      */     }
/*      */     
/* 1091 */     return splitTokens;
/*      */   }
/*      */   
/*      */   private static boolean startsWith(byte[] dataFrom, String chars) {
/* 1095 */     for (int i = 0; i < chars.length(); i++) {
/* 1096 */       if (dataFrom[i] != chars.charAt(i)) {
/* 1097 */         return false;
/*      */       }
/*      */     }
/* 1100 */     return true;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public static boolean startsWithIgnoreCase(String searchIn, int startAt, String searchFor)
/*      */   {
/* 1119 */     return searchIn.regionMatches(true, startAt, searchFor, 0, searchFor.length());
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public static boolean startsWithIgnoreCase(String searchIn, String searchFor)
/*      */   {
/* 1135 */     return startsWithIgnoreCase(searchIn, 0, searchFor);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public static boolean startsWithIgnoreCaseAndNonAlphaNumeric(String searchIn, String searchFor)
/*      */   {
/* 1152 */     if (searchIn == null) {
/* 1153 */       return searchFor == null;
/*      */     }
/*      */     
/* 1156 */     int beginPos = 0;
/*      */     
/* 1158 */     int inLength = searchIn.length();
/*      */     
/* 1160 */     for (beginPos = 0; beginPos < inLength; beginPos++) {
/* 1161 */       char c = searchIn.charAt(beginPos);
/*      */       
/* 1163 */       if (Character.isLetterOrDigit(c)) {
/*      */         break;
/*      */       }
/*      */     }
/*      */     
/* 1168 */     return startsWithIgnoreCase(searchIn, beginPos, searchFor);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public static boolean startsWithIgnoreCaseAndWs(String searchIn, String searchFor)
/*      */   {
/* 1184 */     return startsWithIgnoreCaseAndWs(searchIn, searchFor, 0);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public static boolean startsWithIgnoreCaseAndWs(String searchIn, String searchFor, int beginPos)
/*      */   {
/* 1203 */     if (searchIn == null) {
/* 1204 */       return searchFor == null;
/*      */     }
/*      */     
/* 1207 */     int inLength = searchIn.length();
/* 1209 */     for (; 
/* 1209 */         beginPos < inLength; beginPos++) {
/* 1210 */       if (!Character.isWhitespace(searchIn.charAt(beginPos))) {
/*      */         break;
/*      */       }
/*      */     }
/*      */     
/* 1215 */     return startsWithIgnoreCase(searchIn, beginPos, searchFor);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public static byte[] stripEnclosure(byte[] source, String prefix, String suffix)
/*      */   {
/* 1226 */     if ((source.length >= prefix.length() + suffix.length()) && (startsWith(source, prefix)) && (endsWith(source, suffix)))
/*      */     {
/*      */ 
/* 1229 */       int totalToStrip = prefix.length() + suffix.length();
/* 1230 */       int enclosedLength = source.length - totalToStrip;
/* 1231 */       byte[] enclosed = new byte[enclosedLength];
/*      */       
/* 1233 */       int startPos = prefix.length();
/* 1234 */       int numToCopy = enclosed.length;
/* 1235 */       System.arraycopy(source, startPos, enclosed, 0, numToCopy);
/*      */       
/* 1237 */       return enclosed;
/*      */     }
/* 1239 */     return source;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public static final String toAsciiString(byte[] buffer)
/*      */   {
/* 1251 */     return toAsciiString(buffer, 0, buffer.length);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public static final String toAsciiString(byte[] buffer, int startPos, int length)
/*      */   {
/* 1268 */     char[] charArray = new char[length];
/* 1269 */     int readpoint = startPos;
/*      */     
/* 1271 */     for (int i = 0; i < length; i++) {
/* 1272 */       charArray[i] = ((char)buffer[readpoint]);
/* 1273 */       readpoint++;
/*      */     }
/*      */     
/* 1276 */     return new String(charArray);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public static int wildCompare(String searchIn, String searchForWildcard)
/*      */   {
/* 1294 */     if ((searchIn == null) || (searchForWildcard == null)) {
/* 1295 */       return -1;
/*      */     }
/*      */     
/* 1298 */     if (searchForWildcard.equals("%"))
/*      */     {
/* 1300 */       return 1;
/*      */     }
/*      */     
/* 1303 */     int result = -1;
/*      */     
/* 1305 */     char wildcardMany = '%';
/* 1306 */     char wildcardOne = '_';
/* 1307 */     char wildcardEscape = '\\';
/*      */     
/* 1309 */     int searchForPos = 0;
/* 1310 */     int searchForEnd = searchForWildcard.length();
/*      */     
/* 1312 */     int searchInPos = 0;
/* 1313 */     int searchInEnd = searchIn.length();
/*      */     
/* 1315 */     while (searchForPos != searchForEnd) {
/* 1316 */       char wildstrChar = searchForWildcard.charAt(searchForPos);
/*      */       
/*      */ 
/* 1319 */       while ((searchForWildcard.charAt(searchForPos) != wildcardMany) && (wildstrChar != wildcardOne)) {
/* 1320 */         if ((searchForWildcard.charAt(searchForPos) == wildcardEscape) && (searchForPos + 1 != searchForEnd))
/*      */         {
/* 1322 */           searchForPos++;
/*      */         }
/*      */         
/* 1325 */         if ((searchInPos == searchInEnd) || (Character.toUpperCase(searchForWildcard.charAt(searchForPos++)) != Character.toUpperCase(searchIn.charAt(searchInPos++))))
/*      */         {
/*      */ 
/*      */ 
/* 1329 */           return 1;
/*      */         }
/*      */         
/* 1332 */         if (searchForPos == searchForEnd) {
/* 1333 */           return searchInPos != searchInEnd ? 1 : 0;
/*      */         }
/*      */         
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 1340 */         result = 1;
/*      */       }
/*      */       
/* 1343 */       if (searchForWildcard.charAt(searchForPos) == wildcardOne) {
/*      */         do {
/* 1345 */           if (searchInPos == searchInEnd)
/*      */           {
/*      */ 
/*      */ 
/*      */ 
/* 1350 */             return result;
/*      */           }
/*      */           
/* 1353 */           searchInPos++;
/*      */           
/* 1355 */           searchForPos++; } while ((searchForPos < searchForEnd) && (searchForWildcard.charAt(searchForPos) == wildcardOne));
/*      */         
/* 1357 */         if (searchForPos == searchForEnd) {
/*      */           break;
/*      */         }
/*      */       }
/*      */       
/* 1362 */       if (searchForWildcard.charAt(searchForPos) == wildcardMany)
/*      */       {
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 1369 */         searchForPos++;
/* 1372 */         for (; 
/*      */             
/* 1372 */             searchForPos != searchForEnd; searchForPos++) {
/* 1373 */           if (searchForWildcard.charAt(searchForPos) != wildcardMany)
/*      */           {
/*      */ 
/*      */ 
/* 1377 */             if (searchForWildcard.charAt(searchForPos) != wildcardOne) break;
/* 1378 */             if (searchInPos == searchInEnd) {
/* 1379 */               return -1;
/*      */             }
/*      */             
/* 1382 */             searchInPos++;
/*      */           }
/*      */         }
/*      */         
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 1390 */         if (searchForPos == searchForEnd) {
/* 1391 */           return 0;
/*      */         }
/*      */         
/* 1394 */         if (searchInPos == searchInEnd) {
/* 1395 */           return -1;
/*      */         }
/*      */         char cmp;
/* 1398 */         if (((cmp = searchForWildcard.charAt(searchForPos)) == wildcardEscape) && (searchForPos + 1 != searchForEnd))
/*      */         {
/* 1400 */           cmp = searchForWildcard.charAt(++searchForPos);
/*      */         }
/*      */         
/* 1403 */         searchForPos++;
/*      */         
/*      */         do
/*      */         {
/* 1407 */           while ((searchInPos != searchInEnd) && (Character.toUpperCase(searchIn.charAt(searchInPos)) != Character.toUpperCase(cmp)))
/*      */           {
/*      */ 
/* 1410 */             searchInPos++;
/*      */           }
/* 1412 */           if (searchInPos++ == searchInEnd) {
/* 1413 */             return -1;
/*      */           }
/*      */           
/*      */ 
/* 1417 */           int tmp = wildCompare(searchIn, searchForWildcard);
/*      */           
/* 1419 */           if (tmp <= 0) {
/* 1420 */             return tmp;
/*      */           }
/*      */           
/*      */         }
/* 1424 */         while ((searchInPos != searchInEnd) && (searchForWildcard.charAt(0) != wildcardMany));
/*      */         
/* 1426 */         return -1;
/*      */       }
/*      */     }
/*      */     
/* 1430 */     return searchInPos != searchInEnd ? 1 : 0;
/*      */   }
/*      */   
/*      */   static byte[] s2b(String s, Connection conn) throws SQLException
/*      */   {
/* 1435 */     if (s == null) {
/* 1436 */       return null;
/*      */     }
/*      */     
/* 1439 */     if ((conn != null) && (conn.getUseUnicode())) {
/*      */       try {
/* 1441 */         String encoding = conn.getEncoding();
/*      */         
/* 1443 */         if (encoding == null) {
/* 1444 */           return s.getBytes();
/*      */         }
/*      */         
/* 1447 */         SingleByteCharsetConverter converter = conn.getCharsetConverter(encoding);
/*      */         
/*      */ 
/* 1450 */         if (converter != null) {
/* 1451 */           return converter.toBytes(s);
/*      */         }
/*      */         
/* 1454 */         return s.getBytes(encoding);
/*      */       } catch (UnsupportedEncodingException E) {
/* 1456 */         return s.getBytes();
/*      */       }
/*      */     }
/*      */     
/* 1460 */     return s.getBytes();
/*      */   }
/*      */   
/*      */   public static int lastIndexOf(byte[] s, char c) {
/* 1464 */     if (s == null) {
/* 1465 */       return -1;
/*      */     }
/*      */     
/* 1468 */     for (int i = s.length - 1; i >= 0; i--) {
/* 1469 */       if (s[i] == c) {
/* 1470 */         return i;
/*      */       }
/*      */     }
/*      */     
/* 1474 */     return -1;
/*      */   }
/*      */   
/*      */   public static int indexOf(byte[] s, char c) {
/* 1478 */     if (s == null) {
/* 1479 */       return -1;
/*      */     }
/*      */     
/* 1482 */     int length = s.length;
/*      */     
/* 1484 */     for (int i = 0; i < length; i++) {
/* 1485 */       if (s[i] == c) {
/* 1486 */         return i;
/*      */       }
/*      */     }
/*      */     
/* 1490 */     return -1;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public static String stripComments(String src, String stringOpens, String stringCloses, boolean slashStarComments, boolean slashSlashComments, boolean hashComments, boolean dashDashComments)
/*      */   {
/* 1517 */     if (src == null) {
/* 1518 */       return null;
/*      */     }
/*      */     
/* 1521 */     StringBuffer buf = new StringBuffer(src.length());
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 1530 */     StringReader sourceReader = new StringReader(src);
/*      */     
/* 1532 */     int contextMarker = 0;
/* 1533 */     boolean escaped = false;
/* 1534 */     int markerTypeFound = -1;
/*      */     
/* 1536 */     int ind = 0;
/*      */     
/* 1538 */     int currentChar = 0;
/*      */     try
/*      */     {
/* 1541 */       while ((currentChar = sourceReader.read()) != -1)
/*      */       {
/*      */ 
/*      */ 
/* 1545 */         if ((markerTypeFound != -1) && (currentChar == stringCloses.charAt(markerTypeFound)) && (!escaped))
/*      */         {
/* 1547 */           contextMarker = 0;
/* 1548 */           markerTypeFound = -1;
/* 1549 */         } else if (((ind = stringOpens.indexOf(currentChar)) != -1) && (!escaped) && (contextMarker == 0))
/*      */         {
/* 1551 */           markerTypeFound = ind;
/* 1552 */           contextMarker = currentChar;
/*      */         }
/*      */         
/* 1555 */         if ((contextMarker == 0) && (currentChar == 47) && ((slashSlashComments) || (slashStarComments)))
/*      */         {
/* 1557 */           currentChar = sourceReader.read();
/* 1558 */           if ((currentChar == 42) && (slashStarComments)) {
/* 1559 */             int prevChar = 0;
/*      */             
/* 1561 */             while (((currentChar = sourceReader.read()) != 47) || (prevChar != 42)) {
/* 1562 */               if (currentChar == 13)
/*      */               {
/* 1564 */                 currentChar = sourceReader.read();
/* 1565 */                 if (currentChar == 10) {
/* 1566 */                   currentChar = sourceReader.read();
/*      */                 }
/*      */               }
/* 1569 */               else if (currentChar == 10)
/*      */               {
/* 1571 */                 currentChar = sourceReader.read();
/*      */               }
/*      */               
/* 1574 */               if (currentChar < 0)
/*      */                 break;
/* 1576 */               prevChar = currentChar;
/*      */             }
/*      */           }
/* 1579 */           if ((currentChar != 47) || (!slashSlashComments)) {}
/*      */         } else {
/* 1581 */           while (((currentChar = sourceReader.read()) != 10) && (currentChar != 13) && (currentChar >= 0))
/*      */           {
/*      */             continue;
/* 1584 */             if ((contextMarker == 0) && (currentChar == 35) && (hashComments)) {}
/*      */             
/*      */             for (;;)
/*      */             {
/* 1588 */               if (((currentChar = sourceReader.read()) != 10) && (currentChar != 13) && (currentChar >= 0)) {
/*      */                 continue;
/* 1590 */                 if ((contextMarker == 0) && (currentChar == 45) && (dashDashComments))
/*      */                 {
/* 1592 */                   currentChar = sourceReader.read();
/*      */                   
/* 1594 */                   if ((currentChar == -1) || (currentChar != 45)) {
/* 1595 */                     buf.append('-');
/*      */                     
/* 1597 */                     if (currentChar == -1) break;
/* 1598 */                     buf.append(currentChar); break;
/*      */                   }
/*      */                   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 1607 */                   while (((currentChar = sourceReader.read()) != 10) && (currentChar != 13) && (currentChar >= 0)) {}
/*      */                 }
/*      */               }
/*      */             } } }
/* 1611 */         if (currentChar != -1) {
/* 1612 */           buf.append((char)currentChar);
/*      */         }
/*      */       }
/*      */     }
/*      */     catch (IOException ioEx) {}
/*      */     
/*      */ 
/* 1619 */     return buf.toString();
/*      */   }
/*      */ }


/* Location:              E:\java\java学习\hutubill\lib\all.jar!\com\mysql\jdbc\StringUtils.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version:       0.7.1
 */
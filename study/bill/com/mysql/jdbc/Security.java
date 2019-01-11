/*     */ package com.mysql.jdbc;
/*     */ 
/*     */ import java.security.MessageDigest;
/*     */ import java.security.NoSuchAlgorithmException;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ class Security
/*     */ {
/*     */   private static final char PVERSION41_CHAR = '*';
/*     */   private static final int SHA1_HASH_SIZE = 20;
/*     */   
/*     */   private static int charVal(char c)
/*     */   {
/*  46 */     return (c >= 'A') && (c <= 'Z') ? c - 'A' + 10 : (c >= '0') && (c <= '9') ? c - '0' : c - 'a' + 10;
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
/*     */   static byte[] createKeyFromOldPassword(String passwd)
/*     */     throws NoSuchAlgorithmException
/*     */   {
/*  77 */     passwd = makeScrambledPassword(passwd);
/*     */     
/*     */ 
/*  80 */     int[] salt = getSaltFromPassword(passwd);
/*     */     
/*     */ 
/*  83 */     return getBinaryPassword(salt, false);
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
/*     */   static byte[] getBinaryPassword(int[] salt, boolean usingNewPasswords)
/*     */     throws NoSuchAlgorithmException
/*     */   {
/* 101 */     int val = 0;
/*     */     
/* 103 */     byte[] binaryPassword = new byte[20];
/*     */     
/*     */ 
/*     */ 
/*     */ 
/* 108 */     if (usingNewPasswords) {
/* 109 */       int pos = 0;
/*     */       
/* 111 */       for (int i = 0; i < 4; i++) {
/* 112 */         val = salt[i];
/*     */         
/* 114 */         for (int t = 3; t >= 0; t--) {
/* 115 */           binaryPassword[(pos++)] = ((byte)(val & 0xFF));
/* 116 */           val >>= 8;
/*     */         }
/*     */       }
/*     */       
/* 120 */       return binaryPassword;
/*     */     }
/*     */     
/* 123 */     int offset = 0;
/*     */     
/* 125 */     for (int i = 0; i < 2; i++) {
/* 126 */       val = salt[i];
/*     */       
/* 128 */       for (int t = 3; t >= 0; t--) {
/* 129 */         binaryPassword[(t + offset)] = ((byte)(val % 256));
/* 130 */         val >>= 8;
/*     */       }
/*     */       
/* 133 */       offset += 4;
/*     */     }
/*     */     
/* 136 */     MessageDigest md = MessageDigest.getInstance("SHA-1");
/*     */     
/* 138 */     md.update(binaryPassword, 0, 8);
/*     */     
/* 140 */     return md.digest();
/*     */   }
/*     */   
/*     */   private static int[] getSaltFromPassword(String password) {
/* 144 */     int[] result = new int[6];
/*     */     
/* 146 */     if ((password == null) || (password.length() == 0)) {
/* 147 */       return result;
/*     */     }
/*     */     
/* 150 */     if (password.charAt(0) == '*')
/*     */     {
/* 152 */       String saltInHex = password.substring(1, 5);
/*     */       
/* 154 */       int val = 0;
/*     */       
/* 156 */       for (int i = 0; i < 4; i++) {
/* 157 */         val = (val << 4) + charVal(saltInHex.charAt(i));
/*     */       }
/*     */       
/* 160 */       return result;
/*     */     }
/*     */     
/* 163 */     int resultPos = 0;
/* 164 */     int pos = 0;
/* 165 */     int length = password.length();
/*     */     
/* 167 */     while (pos < length) {
/* 168 */       int val = 0;
/*     */       
/* 170 */       for (int i = 0; i < 8; i++) {
/* 171 */         val = (val << 4) + charVal(password.charAt(pos++));
/*     */       }
/*     */       
/* 174 */       result[(resultPos++)] = val;
/*     */     }
/*     */     
/* 177 */     return result;
/*     */   }
/*     */   
/*     */   private static String longToHex(long val) {
/* 181 */     String longHex = Long.toHexString(val);
/*     */     
/* 183 */     int length = longHex.length();
/*     */     
/* 185 */     if (length < 8) {
/* 186 */       int padding = 8 - length;
/* 187 */       StringBuffer buf = new StringBuffer();
/*     */       
/* 189 */       for (int i = 0; i < padding; i++) {
/* 190 */         buf.append("0");
/*     */       }
/*     */       
/* 193 */       buf.append(longHex);
/*     */       
/* 195 */       return buf.toString();
/*     */     }
/*     */     
/* 198 */     return longHex.substring(0, 8);
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
/*     */   static String makeScrambledPassword(String password)
/*     */     throws NoSuchAlgorithmException
/*     */   {
/* 216 */     long[] passwordHash = Util.newHash(password);
/* 217 */     StringBuffer scramble = new StringBuffer();
/*     */     
/* 219 */     scramble.append(longToHex(passwordHash[0]));
/* 220 */     scramble.append(longToHex(passwordHash[1]));
/*     */     
/* 222 */     return scramble.toString();
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
/*     */   static void passwordCrypt(byte[] from, byte[] to, byte[] password, int length)
/*     */   {
/* 241 */     int pos = 0;
/*     */     
/* 243 */     while ((pos < from.length) && (pos < length)) {
/* 244 */       to[pos] = ((byte)(from[pos] ^ password[pos]));
/* 245 */       pos++;
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
/*     */   static byte[] passwordHashStage1(String password)
/*     */     throws NoSuchAlgorithmException
/*     */   {
/* 262 */     MessageDigest md = MessageDigest.getInstance("SHA-1");
/* 263 */     StringBuffer cleansedPassword = new StringBuffer();
/*     */     
/* 265 */     int passwordLength = password.length();
/*     */     
/* 267 */     for (int i = 0; i < passwordLength; i++) {
/* 268 */       char c = password.charAt(i);
/*     */       
/* 270 */       if ((c != ' ') && (c != '\t'))
/*     */       {
/*     */ 
/*     */ 
/* 274 */         cleansedPassword.append(c);
/*     */       }
/*     */     }
/* 277 */     return md.digest(cleansedPassword.toString().getBytes());
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
/*     */   static byte[] passwordHashStage2(byte[] hashedPassword, byte[] salt)
/*     */     throws NoSuchAlgorithmException
/*     */   {
/* 295 */     MessageDigest md = MessageDigest.getInstance("SHA-1");
/*     */     
/*     */ 
/* 298 */     md.update(salt, 0, 4);
/*     */     
/* 300 */     md.update(hashedPassword, 0, 20);
/*     */     
/* 302 */     return md.digest();
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
/*     */   static byte[] scramble411(String password, String seed)
/*     */     throws NoSuchAlgorithmException
/*     */   {
/* 324 */     MessageDigest md = MessageDigest.getInstance("SHA-1");
/*     */     
/* 326 */     byte[] passwordHashStage1 = md.digest(password.getBytes());
/* 327 */     md.reset();
/*     */     
/* 329 */     byte[] passwordHashStage2 = md.digest(passwordHashStage1);
/* 330 */     md.reset();
/*     */     
/* 332 */     byte[] seedAsBytes = seed.getBytes();
/* 333 */     md.update(seedAsBytes);
/* 334 */     md.update(passwordHashStage2);
/*     */     
/* 336 */     byte[] toBeXord = md.digest();
/*     */     
/* 338 */     int numToXor = toBeXord.length;
/*     */     
/* 340 */     for (int i = 0; i < numToXor; i++) {
/* 341 */       toBeXord[i] = ((byte)(toBeXord[i] ^ passwordHashStage1[i]));
/*     */     }
/*     */     
/* 344 */     return toBeXord;
/*     */   }
/*     */ }


/* Location:              E:\java\java学习\hutubill\lib\all.jar!\com\mysql\jdbc\Security.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version:       0.7.1
 */
/*     */ package com.mysql.jdbc;
/*     */ 
/*     */ import java.io.ObjectInputStream;
/*     */ import java.io.PrintWriter;
/*     */ import java.io.StringWriter;
/*     */ import java.lang.reflect.InvocationTargetException;
/*     */ import java.lang.reflect.Method;
/*     */ import java.sql.ResultSet;
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
/*     */ 
/*     */ public class Util
/*     */ {
/*     */   protected static Method systemNanoTimeMethod;
/*  43 */   private static boolean isColdFusion = false;
/*     */   
/*     */   static {
/*     */     try {
/*  47 */       systemNanoTimeMethod = System.class.getMethod("nanoTime", null);
/*     */     } catch (SecurityException e) {
/*  49 */       systemNanoTimeMethod = null;
/*     */     } catch (NoSuchMethodException e) {
/*  51 */       systemNanoTimeMethod = null;
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*  61 */     String loadedFrom = stackTraceToString(new Throwable());
/*     */     
/*  63 */     if (loadedFrom != null) {
/*  64 */       isColdFusion = loadedFrom.indexOf("coldfusion") != -1;
/*     */     } else {
/*  66 */       isColdFusion = false;
/*     */     }
/*     */   }
/*     */   
/*     */   public static boolean isColdFusion() {
/*  71 */     return isColdFusion;
/*     */   }
/*     */   
/*     */   protected static boolean nanoTimeAvailable() {
/*  75 */     return systemNanoTimeMethod != null;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*  80 */   private static final TimeZone DEFAULT_TIMEZONE = TimeZone.getDefault();
/*     */   
/*     */   static final TimeZone getDefaultTimeZone() {
/*  83 */     return (TimeZone)DEFAULT_TIMEZONE.clone();
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
/*  97 */   private static Util enclosingInstance = new Util();
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   static String newCrypt(String password, String seed)
/*     */   {
/* 104 */     if ((password == null) || (password.length() == 0)) {
/* 105 */       return password;
/*     */     }
/*     */     
/* 108 */     long[] pw = newHash(seed);
/* 109 */     long[] msg = newHash(password);
/* 110 */     long max = 1073741823L;
/* 111 */     long seed1 = (pw[0] ^ msg[0]) % max;
/* 112 */     long seed2 = (pw[1] ^ msg[1]) % max;
/* 113 */     char[] chars = new char[seed.length()];
/*     */     
/* 115 */     for (int i = 0; i < seed.length(); i++) {
/* 116 */       seed1 = (seed1 * 3L + seed2) % max;
/* 117 */       seed2 = (seed1 + seed2 + 33L) % max;
/* 118 */       double d = seed1 / max;
/* 119 */       byte b = (byte)(int)Math.floor(d * 31.0D + 64.0D);
/* 120 */       chars[i] = ((char)b);
/*     */     }
/*     */     
/* 123 */     seed1 = (seed1 * 3L + seed2) % max;
/* 124 */     seed2 = (seed1 + seed2 + 33L) % max;
/* 125 */     double d = seed1 / max;
/* 126 */     byte b = (byte)(int)Math.floor(d * 31.0D);
/*     */     
/* 128 */     for (int i = 0; i < seed.length(); tmp205_203++) {
/* 129 */       int tmp205_203 = i; char[] tmp205_201 = chars;tmp205_201[tmp205_203] = ((char)(tmp205_201[tmp205_203] ^ (char)b));
/*     */     }
/*     */     
/* 132 */     return new String(chars);
/*     */   }
/*     */   
/*     */   static long[] newHash(String password) {
/* 136 */     long nr = 1345345333L;
/* 137 */     long add = 7L;
/* 138 */     long nr2 = 305419889L;
/*     */     
/*     */ 
/* 141 */     for (int i = 0; i < password.length(); i++) {
/* 142 */       if ((password.charAt(i) != ' ') && (password.charAt(i) != '\t'))
/*     */       {
/*     */ 
/*     */ 
/* 146 */         long tmp = 0xFF & password.charAt(i);
/* 147 */         nr ^= ((nr & 0x3F) + add) * tmp + (nr << 8);
/* 148 */         nr2 += (nr2 << 8 ^ nr);
/* 149 */         add += tmp;
/*     */       }
/*     */     }
/* 152 */     long[] result = new long[2];
/* 153 */     result[0] = (nr & 0x7FFFFFFF);
/* 154 */     result[1] = (nr2 & 0x7FFFFFFF);
/*     */     
/* 156 */     return result;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   static String oldCrypt(String password, String seed)
/*     */   {
/* 164 */     long max = 33554431L;
/*     */     
/*     */ 
/*     */ 
/* 168 */     if ((password == null) || (password.length() == 0)) {
/* 169 */       return password;
/*     */     }
/*     */     
/* 172 */     long hp = oldHash(seed);
/* 173 */     long hm = oldHash(password);
/*     */     
/* 175 */     long nr = hp ^ hm;
/* 176 */     nr %= max;
/* 177 */     long s1 = nr;
/* 178 */     long s2 = nr / 2L;
/*     */     
/* 180 */     char[] chars = new char[seed.length()];
/*     */     
/* 182 */     for (int i = 0; i < seed.length(); i++) {
/* 183 */       s1 = (s1 * 3L + s2) % max;
/* 184 */       s2 = (s1 + s2 + 33L) % max;
/* 185 */       double d = s1 / max;
/* 186 */       byte b = (byte)(int)Math.floor(d * 31.0D + 64.0D);
/* 187 */       chars[i] = ((char)b);
/*     */     }
/*     */     
/* 190 */     return new String(chars);
/*     */   }
/*     */   
/*     */   static long oldHash(String password) {
/* 194 */     long nr = 1345345333L;
/* 195 */     long nr2 = 7L;
/*     */     
/*     */ 
/* 198 */     for (int i = 0; i < password.length(); i++) {
/* 199 */       if ((password.charAt(i) != ' ') && (password.charAt(i) != '\t'))
/*     */       {
/*     */ 
/*     */ 
/* 203 */         long tmp = password.charAt(i);
/* 204 */         nr ^= ((nr & 0x3F) + nr2) * tmp + (nr << 8);
/* 205 */         nr2 += tmp;
/*     */       }
/*     */     }
/* 208 */     return nr & 0x7FFFFFFF;
/*     */   }
/*     */   
/*     */   private static RandStructcture randomInit(long seed1, long seed2) {
/* 212 */     Util tmp7_4 = enclosingInstance;tmp7_4.getClass();RandStructcture randStruct = new RandStructcture(tmp7_4);
/*     */     
/* 214 */     randStruct.maxValue = 1073741823L;
/* 215 */     randStruct.maxValueDbl = randStruct.maxValue;
/* 216 */     randStruct.seed1 = (seed1 % randStruct.maxValue);
/* 217 */     randStruct.seed2 = (seed2 % randStruct.maxValue);
/*     */     
/* 219 */     return randStruct;
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
/*     */   public static Object readObject(ResultSet resultSet, int index)
/*     */     throws Exception
/*     */   {
/* 237 */     ObjectInputStream objIn = new ObjectInputStream(resultSet.getBinaryStream(index));
/*     */     
/* 239 */     Object obj = objIn.readObject();
/* 240 */     objIn.close();
/*     */     
/* 242 */     return obj;
/*     */   }
/*     */   
/*     */   private static double rnd(RandStructcture randStruct) {
/* 246 */     randStruct.seed1 = ((randStruct.seed1 * 3L + randStruct.seed2) % randStruct.maxValue);
/*     */     
/* 248 */     randStruct.seed2 = ((randStruct.seed1 + randStruct.seed2 + 33L) % randStruct.maxValue);
/*     */     
/*     */ 
/* 251 */     return randStruct.seed1 / randStruct.maxValueDbl;
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
/*     */   public static String scramble(String message, String password)
/*     */   {
/* 267 */     byte[] to = new byte[8];
/* 268 */     String val = "";
/*     */     
/* 270 */     message = message.substring(0, 8);
/*     */     
/* 272 */     if ((password != null) && (password.length() > 0)) {
/* 273 */       long[] hashPass = newHash(password);
/* 274 */       long[] hashMessage = newHash(message);
/*     */       
/* 276 */       RandStructcture randStruct = randomInit(hashPass[0] ^ hashMessage[0], hashPass[1] ^ hashMessage[1]);
/*     */       
/*     */ 
/* 279 */       int msgPos = 0;
/* 280 */       int msgLength = message.length();
/* 281 */       int toPos = 0;
/*     */       
/* 283 */       while (msgPos++ < msgLength) {
/* 284 */         to[(toPos++)] = ((byte)(int)(Math.floor(rnd(randStruct) * 31.0D) + 64.0D));
/*     */       }
/*     */       
/*     */ 
/* 288 */       byte extra = (byte)(int)Math.floor(rnd(randStruct) * 31.0D);
/*     */       
/* 290 */       for (int i = 0; i < to.length; i++) {
/* 291 */         int tmp140_138 = i; byte[] tmp140_136 = to;tmp140_136[tmp140_138] = ((byte)(tmp140_136[tmp140_138] ^ extra));
/*     */       }
/*     */       
/* 294 */       val = new String(to);
/*     */     }
/*     */     
/* 297 */     return val;
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
/*     */   public static String stackTraceToString(Throwable ex)
/*     */   {
/* 313 */     StringBuffer traceBuf = new StringBuffer();
/* 314 */     traceBuf.append(Messages.getString("Util.1"));
/*     */     
/* 316 */     if (ex != null) {
/* 317 */       traceBuf.append(ex.getClass().getName());
/*     */       
/* 319 */       String message = ex.getMessage();
/*     */       
/* 321 */       if (message != null) {
/* 322 */         traceBuf.append(Messages.getString("Util.2"));
/* 323 */         traceBuf.append(message);
/*     */       }
/*     */       
/* 326 */       StringWriter out = new StringWriter();
/*     */       
/* 328 */       PrintWriter printOut = new PrintWriter(out);
/*     */       
/* 330 */       ex.printStackTrace(printOut);
/*     */       
/* 332 */       traceBuf.append(Messages.getString("Util.3"));
/* 333 */       traceBuf.append(out.toString());
/*     */     }
/*     */     
/* 336 */     traceBuf.append(Messages.getString("Util.4"));
/*     */     
/* 338 */     return traceBuf.toString();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static boolean interfaceExists(String hostname)
/*     */   {
/*     */     try
/*     */     {
/* 350 */       Class networkInterfaceClass = Class.forName("java.net.NetworkInterface");
/* 351 */       return networkInterfaceClass.getMethod("getByName", null).invoke(networkInterfaceClass, new Object[] { hostname }) != null;
/*     */     } catch (Throwable t) {}
/* 353 */     return false;
/*     */   }
/*     */   
/*     */   public static long getCurrentTimeNanosOrMillis()
/*     */   {
/* 358 */     if (systemNanoTimeMethod != null) {
/*     */       try {
/* 360 */         return ((Long)systemNanoTimeMethod.invoke(null, null)).longValue();
/*     */       }
/*     */       catch (IllegalArgumentException e) {}catch (IllegalAccessException e) {}catch (InvocationTargetException e) {}
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 370 */     return System.currentTimeMillis();
/*     */   }
/*     */   
/*     */   class RandStructcture
/*     */   {
/*     */     long maxValue;
/*     */     double maxValueDbl;
/*     */     long seed1;
/*     */     long seed2;
/*     */     
/*     */     RandStructcture() {}
/*     */   }
/*     */ }


/* Location:              E:\java\java学习\hutubill\lib\all.jar!\com\mysql\jdbc\Util.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version:       0.7.1
 */
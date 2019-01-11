/*     */ package org.jdesktop.swingx.calendar;
/*     */ 
/*     */ import java.util.Calendar;
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
/*     */ public class DateUtils
/*     */ {
/*  40 */   private static Calendar CALENDAR = ;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static Date endOfDay(Date date)
/*     */   {
/*  49 */     Calendar calendar = CALENDAR;
/*  50 */     synchronized (calendar) {
/*  51 */       calendar.setTime(date);
/*  52 */       calendar.set(11, 23);
/*  53 */       calendar.set(14, 999);
/*  54 */       calendar.set(13, 59);
/*  55 */       calendar.set(12, 59);
/*  56 */       return calendar.getTime();
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
/*     */   public static Date startOfDay(Date date)
/*     */   {
/*  69 */     Calendar calendar = CALENDAR;
/*  70 */     synchronized (calendar) {
/*  71 */       calendar.setTime(date);
/*  72 */       calendar.set(11, 0);
/*  73 */       calendar.set(14, 0);
/*  74 */       calendar.set(13, 0);
/*  75 */       calendar.set(12, 0);
/*  76 */       return calendar.getTime();
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static long startOfDayInMillis(long date)
/*     */   {
/*  88 */     Calendar calendar = CALENDAR;
/*  89 */     synchronized (calendar) {
/*  90 */       calendar.setTimeInMillis(date);
/*  91 */       calendar.set(11, 0);
/*  92 */       calendar.set(14, 0);
/*  93 */       calendar.set(13, 0);
/*  94 */       calendar.set(12, 0);
/*  95 */       return calendar.getTimeInMillis();
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static long endOfDayInMillis(long date)
/*     */   {
/* 106 */     Calendar calendar = CALENDAR;
/* 107 */     synchronized (calendar) {
/* 108 */       calendar.setTimeInMillis(date);
/* 109 */       calendar.set(11, 23);
/* 110 */       calendar.set(14, 999);
/* 111 */       calendar.set(13, 59);
/* 112 */       calendar.set(12, 59);
/* 113 */       return calendar.getTimeInMillis();
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static Date nextDay(Date date)
/*     */   {
/* 125 */     return new Date(addDays(date.getTime(), 1));
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
/*     */   public static long addDays(long time, int amount)
/*     */   {
/* 138 */     Calendar calendar = CALENDAR;
/* 139 */     synchronized (calendar) {
/* 140 */       calendar.setTimeInMillis(time);
/* 141 */       calendar.add(5, amount);
/* 142 */       return calendar.getTimeInMillis();
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static long nextDay(long date)
/*     */   {
/* 153 */     return addDays(date, 1);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static long nextWeek(long date)
/*     */   {
/* 163 */     return addDays(date, 7);
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
/*     */   public static int getDaysDiff(long t1, long t2, boolean checkOverflow)
/*     */   {
/* 177 */     if (t1 > t2) {
/* 178 */       long tmp = t1;
/* 179 */       t1 = t2;
/* 180 */       t2 = tmp;
/*     */     }
/* 182 */     Calendar calendar = CALENDAR;
/* 183 */     synchronized (calendar) {
/* 184 */       calendar.setTimeInMillis(t1);
/* 185 */       int delta = 0;
/* 186 */       while (calendar.getTimeInMillis() < t2) {
/* 187 */         calendar.add(5, 1);
/* 188 */         delta++;
/*     */       }
/* 190 */       if ((checkOverflow) && (calendar.getTimeInMillis() > t2)) {
/* 191 */         delta--;
/*     */       }
/* 193 */       return delta;
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
/*     */   public static int getDaysDiff(long t1, long t2)
/*     */   {
/* 206 */     return getDaysDiff(t1, t2, true);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static boolean isFirstOfYear(long date)
/*     */   {
/* 218 */     boolean ret = false;
/* 219 */     Calendar calendar = CALENDAR;
/* 220 */     synchronized (calendar) {
/* 221 */       calendar.setTimeInMillis(date);
/* 222 */       int currentYear = calendar.get(1);
/*     */       
/* 224 */       calendar.add(5, -1);
/* 225 */       int yesterdayYear = calendar.get(1);
/* 226 */       ret = currentYear != yesterdayYear;
/*     */     }
/* 228 */     return ret;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static boolean isFirstOfMonth(long date)
/*     */   {
/* 240 */     boolean ret = false;
/* 241 */     Calendar calendar = CALENDAR;
/* 242 */     synchronized (calendar) {
/* 243 */       calendar.setTimeInMillis(date);
/* 244 */       int currentMonth = calendar.get(2);
/*     */       
/* 246 */       calendar.add(5, -1);
/* 247 */       int yesterdayMonth = calendar.get(2);
/* 248 */       ret = currentMonth != yesterdayMonth;
/*     */     }
/* 250 */     return ret;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static long previousDay(long date)
/*     */   {
/* 261 */     return addDays(date, -1);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static long previousWeek(long date)
/*     */   {
/* 271 */     return addDays(date, -7);
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
/*     */   public static long getPreviousDay(long date, int startOfWeek)
/*     */   {
/* 288 */     return getDay(date, startOfWeek, -1);
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
/*     */   public static long getNextDay(long date, int startOfWeek)
/*     */   {
/* 304 */     return getDay(date, startOfWeek, 1);
/*     */   }
/*     */   
/*     */   private static long getDay(long date, int startOfWeek, int increment) {
/* 308 */     Calendar calendar = CALENDAR;
/* 309 */     synchronized (calendar) {
/* 310 */       calendar.setTimeInMillis(date);
/* 311 */       int day = calendar.get(7);
/*     */       
/* 313 */       while (day != startOfWeek) {
/* 314 */         calendar.add(5, increment);
/* 315 */         day = calendar.get(7);
/*     */       }
/* 317 */       return startOfDayInMillis(calendar.getTimeInMillis());
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static long getPreviousMonth(long date)
/*     */   {
/* 328 */     return incrementMonth(date, -1);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static long getNextMonth(long date)
/*     */   {
/* 338 */     return incrementMonth(date, 1);
/*     */   }
/*     */   
/*     */   private static long incrementMonth(long date, int increment) {
/* 342 */     Calendar calendar = CALENDAR;
/* 343 */     synchronized (calendar) {
/* 344 */       calendar.setTimeInMillis(date);
/* 345 */       calendar.add(2, increment);
/* 346 */       return calendar.getTimeInMillis();
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static long getStartOfMonth(long date)
/*     */   {
/* 357 */     return getMonth(date, -1);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static long getEndOfMonth(long date)
/*     */   {
/* 367 */     return getMonth(date, 1);
/*     */   }
/*     */   
/*     */   private static long getMonth(long date, int increment)
/*     */   {
/* 372 */     Calendar calendar = CALENDAR;
/* 373 */     long result; synchronized (calendar) {
/* 374 */       calendar.setTimeInMillis(date);
/* 375 */       long result; if (increment == -1) {
/* 376 */         calendar.set(5, 1);
/* 377 */         result = startOfDayInMillis(calendar.getTimeInMillis());
/*     */       } else {
/* 379 */         calendar.add(2, 1);
/* 380 */         calendar.set(5, 1);
/* 381 */         calendar.set(11, 0);
/* 382 */         calendar.set(14, 0);
/* 383 */         calendar.set(13, 0);
/* 384 */         calendar.set(12, 0);
/* 385 */         calendar.add(14, -1);
/* 386 */         result = calendar.getTimeInMillis();
/*     */       }
/*     */     }
/* 389 */     return result;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static int getDayOfWeek(long date)
/*     */   {
/* 399 */     Calendar calendar = CALENDAR;
/* 400 */     synchronized (calendar) {
/* 401 */       calendar.setTimeInMillis(date);
/* 402 */       return calendar.get(7);
/*     */     }
/*     */   }
/*     */ }


/* Location:              E:\java\java学习\hutubill\lib\all.jar!\org\jdesktop\swingx\calendar\DateUtils.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */
/*     */ package org.jdesktop.swingx.calendar;
/*     */ 
/*     */ import java.util.Calendar;
/*     */ import java.util.Date;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class CalendarUtils
/*     */ {
/*     */   public static final int ONE_MINUTE = 60000;
/*     */   public static final int ONE_HOUR = 3600000;
/*     */   public static final int THREE_HOURS = 10800000;
/*     */   public static final int ONE_DAY = 86400000;
/*     */   public static final int DECADE = 5467;
/*     */   public static final int YEAR_IN_DECADE = 5468;
/*     */   
/*     */   public static void add(Calendar calendar, int field, int amount)
/*     */   {
/*  60 */     if (isNativeField(field)) {
/*  61 */       calendar.add(field, amount);
/*     */     } else {
/*  63 */       switch (field) {
/*     */       case 5467: 
/*  65 */         calendar.add(1, amount * 10);
/*  66 */         break;
/*     */       default: 
/*  68 */         throw new IllegalArgumentException("unsupported field: " + field);
/*     */       }
/*     */       
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
/*     */   public static int get(Calendar calendar, int field)
/*     */   {
/*  84 */     if (isNativeField(field)) {
/*  85 */       return calendar.get(field);
/*     */     }
/*  87 */     switch (field) {
/*     */     case 5467: 
/*  89 */       return decade(calendar.get(1));
/*     */     case 5468: 
/*  91 */       return calendar.get(1) % 10;
/*     */     }
/*  93 */     throw new IllegalArgumentException("unsupported field: " + field);
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
/*     */   public static void set(Calendar calendar, int field, int value)
/*     */   {
/* 116 */     if (isNativeField(field)) {
/* 117 */       calendar.set(field, value);
/*     */     } else {
/* 119 */       switch (field) {
/*     */       case 5467: 
/* 121 */         if (value <= 0) {
/* 122 */           throw new IllegalArgumentException("value must be a positive but was: " + value);
/*     */         }
/* 124 */         if (value % 10 != 0) {
/* 125 */           throw new IllegalArgumentException("value must be a multiple of 10 but was: " + value);
/*     */         }
/* 127 */         int yearInDecade = get(calendar, 5468);
/* 128 */         calendar.set(1, value + yearInDecade);
/* 129 */         break;
/*     */       case 5468: 
/* 131 */         int decade = get(calendar, 5467);
/* 132 */         calendar.set(1, value + decade);
/* 133 */         break;
/*     */       default: 
/* 135 */         throw new IllegalArgumentException("unsupported field: " + field);
/*     */       }
/*     */       
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private static boolean isNativeField(int calendarField)
/*     */   {
/* 147 */     return calendarField < 5467;
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
/*     */   public static Date getEndOfDST(Calendar calendar)
/*     */   {
/* 160 */     if (!calendar.getTimeZone().useDaylightTime()) return null;
/* 161 */     long old = calendar.getTimeInMillis();
/* 162 */     calendar.set(2, 11);
/* 163 */     endOfMonth(calendar);
/* 164 */     startOfDay(calendar);
/* 165 */     for (int i = 0; i < 366; i++) {
/* 166 */       calendar.add(5, -1);
/* 167 */       if (calendar.getTimeZone().inDaylightTime(calendar.getTime())) {
/* 168 */         endOfDay(calendar);
/* 169 */         return calendar.getTime();
/*     */       }
/*     */     }
/* 172 */     calendar.setTimeInMillis(old);
/* 173 */     return null;
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
/*     */   public static Date getStartOfDST(Calendar calendar)
/*     */   {
/* 188 */     if (!calendar.getTimeZone().useDaylightTime()) return null;
/* 189 */     long old = calendar.getTimeInMillis();
/* 190 */     calendar.set(2, 0);
/* 191 */     startOfMonth(calendar);
/* 192 */     endOfDay(calendar);
/* 193 */     for (int i = 0; i < 366; i++) {
/* 194 */       calendar.add(5, 1);
/* 195 */       if (calendar.getTimeZone().inDaylightTime(calendar.getTime())) {
/* 196 */         endOfDay(calendar);
/* 197 */         return calendar.getTime();
/*     */       }
/*     */     }
/* 200 */     calendar.setTimeInMillis(old);
/* 201 */     return null;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static boolean isStartOfDay(Calendar calendar)
/*     */   {
/* 213 */     Calendar temp = (Calendar)calendar.clone();
/* 214 */     temp.add(14, -1);
/* 215 */     return temp.get(5) != calendar.get(5);
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
/*     */   public static boolean isEndOfDay(Calendar calendar)
/*     */   {
/* 228 */     Calendar temp = (Calendar)calendar.clone();
/* 229 */     temp.add(14, 1);
/* 230 */     return temp.get(5) != calendar.get(5);
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
/*     */   public static boolean isStartOfMonth(Calendar calendar)
/*     */   {
/* 244 */     Calendar temp = (Calendar)calendar.clone();
/* 245 */     temp.add(14, -1);
/* 246 */     return temp.get(2) != calendar.get(2);
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
/*     */   public static boolean isEndOfMonth(Calendar calendar)
/*     */   {
/* 260 */     Calendar temp = (Calendar)calendar.clone();
/* 261 */     temp.add(14, 1);
/* 262 */     return temp.get(2) != calendar.get(2);
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
/*     */   public static boolean isStartOfWeek(Calendar calendar)
/*     */   {
/* 276 */     Calendar temp = (Calendar)calendar.clone();
/* 277 */     temp.add(14, -1);
/* 278 */     return temp.get(3) != calendar.get(3);
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
/*     */   public static boolean isEndOfWeek(Calendar calendar)
/*     */   {
/* 292 */     Calendar temp = (Calendar)calendar.clone();
/* 293 */     temp.add(14, 1);
/* 294 */     return temp.get(3) != calendar.get(3);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static void startOfWeek(Calendar calendar)
/*     */   {
/* 304 */     calendar.set(7, calendar.getFirstDayOfWeek());
/* 305 */     startOfDay(calendar);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static void endOfWeek(Calendar calendar)
/*     */   {
/* 314 */     startOfWeek(calendar);
/* 315 */     calendar.add(5, 7);
/* 316 */     calendar.add(14, -1);
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
/*     */   public static Date endOfWeek(Calendar calendar, Date date)
/*     */   {
/* 330 */     calendar.setTime(date);
/* 331 */     endOfWeek(calendar);
/* 332 */     return calendar.getTime();
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
/*     */   public static Date startOfWeek(Calendar calendar, Date date)
/*     */   {
/* 346 */     calendar.setTime(date);
/* 347 */     startOfWeek(calendar);
/* 348 */     return calendar.getTime();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static void startOfDecade(Calendar calendar)
/*     */   {
/* 359 */     calendar.set(1, decade(calendar.get(1)));
/* 360 */     startOfYear(calendar);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private static int decade(int year)
/*     */   {
/* 369 */     return year / 10 * 10;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static Date startOfDecade(Calendar calendar, Date date)
/*     */   {
/* 380 */     calendar.setTime(date);
/* 381 */     startOfDecade(calendar);
/* 382 */     return calendar.getTime();
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
/*     */   public static boolean isStartOfDecade(Calendar calendar)
/*     */   {
/* 396 */     Calendar temp = (Calendar)calendar.clone();
/* 397 */     temp.add(14, -1);
/* 398 */     return decade(temp.get(1)) != decade(calendar.get(1));
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static void startOfYear(Calendar calendar)
/*     */   {
/* 408 */     calendar.set(2, 0);
/* 409 */     startOfMonth(calendar);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static Date startOfYear(Calendar calendar, Date date)
/*     */   {
/* 420 */     calendar.setTime(date);
/* 421 */     startOfDecade(calendar);
/* 422 */     return calendar.getTime();
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
/*     */   public static boolean isStartOfYear(Calendar calendar)
/*     */   {
/* 436 */     Calendar temp = (Calendar)calendar.clone();
/* 437 */     temp.add(14, -1);
/* 438 */     return temp.get(1) != calendar.get(1);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static void startOfMonth(Calendar calendar)
/*     */   {
/* 447 */     calendar.set(5, 1);
/* 448 */     startOfDay(calendar);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static void endOfMonth(Calendar calendar)
/*     */   {
/* 460 */     calendar.add(2, 1);
/* 461 */     startOfMonth(calendar);
/*     */     
/* 463 */     calendar.add(14, -1);
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
/*     */   public static Date startOfDay(Calendar calendar, Date date)
/*     */   {
/* 478 */     calendar.setTime(date);
/* 479 */     startOfDay(calendar);
/* 480 */     return calendar.getTime();
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
/*     */   public static Date endOfDay(Calendar calendar, Date date)
/*     */   {
/* 494 */     calendar.setTime(date);
/* 495 */     endOfDay(calendar);
/* 496 */     return calendar.getTime();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static void startOfDay(Calendar calendar)
/*     */   {
/* 507 */     calendar.set(11, 0);
/* 508 */     calendar.set(14, 0);
/* 509 */     calendar.set(13, 0);
/* 510 */     calendar.set(12, 0);
/* 511 */     calendar.getTimeInMillis();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static void endOfDay(Calendar calendar)
/*     */   {
/* 520 */     calendar.add(5, 1);
/* 521 */     startOfDay(calendar);
/* 522 */     calendar.add(14, -1);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static void startOf(Calendar calendar, int field)
/*     */   {
/* 534 */     switch (field) {
/*     */     case 5: 
/* 536 */       startOfDay(calendar);
/* 537 */       break;
/*     */     case 2: 
/* 539 */       startOfMonth(calendar);
/* 540 */       break;
/*     */     case 3: 
/* 542 */       startOfWeek(calendar);
/* 543 */       break;
/*     */     case 1: 
/* 545 */       startOfYear(calendar);
/* 546 */       break;
/*     */     case 5467: 
/* 548 */       startOfDecade(calendar);
/* 549 */       break;
/*     */     default: 
/* 551 */       throw new IllegalArgumentException("unsupported field: " + field);
/*     */     }
/*     */     
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
/*     */   public static boolean isStartOf(Calendar calendar, int field)
/*     */   {
/* 568 */     switch (field) {
/*     */     case 5: 
/* 570 */       return isStartOfDay(calendar);
/*     */     case 2: 
/* 572 */       return isStartOfMonth(calendar);
/*     */     case 3: 
/* 574 */       return isStartOfWeek(calendar);
/*     */     case 1: 
/* 576 */       return isStartOfYear(calendar);
/*     */     case 5467: 
/* 578 */       return isStartOfDecade(calendar);
/*     */     }
/* 580 */     throw new IllegalArgumentException("unsupported field: " + field);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static boolean areEqual(Date current, Date date)
/*     */   {
/* 592 */     if ((date == null) && (current == null)) {
/* 593 */       return true;
/*     */     }
/* 595 */     if (date != null) {
/* 596 */       return date.equals(current);
/*     */     }
/* 598 */     return false;
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
/*     */   public static boolean isSameDay(Calendar today, Date now)
/*     */   {
/* 611 */     Calendar temp = (Calendar)today.clone();
/* 612 */     startOfDay(temp);
/* 613 */     Date start = temp.getTime();
/* 614 */     temp.setTime(now);
/* 615 */     startOfDay(temp);
/* 616 */     return start.equals(temp.getTime());
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
/*     */   public static boolean isSame(Calendar today, Date now, int field)
/*     */   {
/* 630 */     Calendar temp = (Calendar)today.clone();
/* 631 */     startOf(temp, field);
/* 632 */     Date start = temp.getTime();
/* 633 */     temp.setTime(now);
/* 634 */     startOf(temp, field);
/* 635 */     return start.equals(temp.getTime());
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
/*     */   public static boolean isFlushed(Calendar calendar)
/*     */   {
/* 653 */     return !calendar.toString().contains("time=?");
/*     */   }
/*     */ }


/* Location:              E:\java\java学习\hutubill\lib\all.jar!\org\jdesktop\swingx\calendar\CalendarUtils.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */
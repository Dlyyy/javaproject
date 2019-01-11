/*    */ package util;
/*    */ 
/*    */ import java.util.Calendar;
/*    */ 
/*    */ public class DateUtil
/*    */ {
/*  7 */   static long millisecondsOfOneDay = 86400000L;
/*    */   
/*    */   public static java.sql.Date util2sql(java.util.Date d) {
/* 10 */     return new java.sql.Date(d.getTime());
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */   public static java.util.Date today()
/*    */   {
/* 18 */     Calendar c = Calendar.getInstance();
/* 19 */     c.setTime(new java.util.Date());
/* 20 */     c.set(10, 0);
/* 21 */     c.set(12, 0);
/* 22 */     c.set(13, 0);
/* 23 */     return c.getTime();
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   public static java.util.Date monthBegin()
/*    */   {
/* 33 */     Calendar c = Calendar.getInstance();
/* 34 */     c.setTime(new java.util.Date());
/* 35 */     c.set(5, 1);
/*    */     
/* 37 */     c.set(11, 0);
/* 38 */     c.set(12, 0);
/* 39 */     c.set(13, 0);
/* 40 */     c.set(14, 0);
/*    */     
/* 42 */     return c.getTime();
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */   public static java.util.Date monthEnd()
/*    */   {
/* 50 */     Calendar c = Calendar.getInstance();
/* 51 */     c.setTime(new java.util.Date());
/* 52 */     c.set(10, 0);
/* 53 */     c.set(12, 0);
/* 54 */     c.set(13, 0);
/*    */     
/* 56 */     c.set(5, 1);
/* 57 */     c.add(2, 1);
/* 58 */     c.add(5, -1);
/* 59 */     return c.getTime();
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   public static int thisMonthTotalDay()
/*    */   {
/* 68 */     long lastDayMilliSeconds = monthEnd().getTime();
/* 69 */     long firstDayMilliSeconds = monthBegin().getTime();
/*    */     
/* 71 */     return (int)((lastDayMilliSeconds - firstDayMilliSeconds) / millisecondsOfOneDay) + 1;
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   public static int thisMonthLeftDay()
/*    */   {
/* 80 */     long lastDayMilliSeconds = monthEnd().getTime();
/* 81 */     long toDayMilliSeconds = today().getTime();
/* 82 */     return (int)((lastDayMilliSeconds - toDayMilliSeconds) / millisecondsOfOneDay) + 1;
/*    */   }
/*    */   
/*    */   public static void main(String[] args) {
/* 86 */     System.out.println(today());
/* 87 */     System.out.println(monthBegin());
/* 88 */     System.out.println(monthEnd());
/* 89 */     System.out.println(thisMonthLeftDay());
/* 90 */     System.out.println(thisMonthTotalDay());
/*    */   }
/*    */ }


/* Location:              E:\java\java学习\hutubill\lib\all.jar!\util\DateUtil.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */
/*    */ package com.mysql.jdbc.util;
/*    */ 
/*    */ import com.mysql.jdbc.TimeUtil;
/*    */ import java.io.PrintStream;
/*    */ import java.sql.Connection;
/*    */ import java.sql.DriverManager;
/*    */ import java.sql.ResultSet;
/*    */ import java.sql.Statement;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class TimezoneDump
/*    */ {
/*    */   private static final String DEFAULT_URL = "jdbc:mysql:///test";
/*    */   
/*    */   public static void main(String[] args)
/*    */     throws Exception
/*    */   {
/* 66 */     String jdbcUrl = "jdbc:mysql:///test";
/*    */     
/* 68 */     if ((args.length == 1) && (args[0] != null)) {
/* 69 */       jdbcUrl = args[0];
/*    */     }
/*    */     
/* 72 */     Class.forName("com.mysql.jdbc.Driver").newInstance();
/*    */     
/* 74 */     ResultSet rs = DriverManager.getConnection(jdbcUrl).createStatement().executeQuery("SHOW VARIABLES LIKE 'timezone'");
/*    */     
/*    */ 
/* 77 */     while (rs.next()) {
/* 78 */       String timezoneFromServer = rs.getString(2);
/* 79 */       System.out.println("MySQL timezone name: " + timezoneFromServer);
/*    */       
/* 81 */       String canonicalTimezone = TimeUtil.getCanoncialTimezone(timezoneFromServer);
/*    */       
/* 83 */       System.out.println("Java timezone name: " + canonicalTimezone);
/*    */     }
/*    */   }
/*    */ }


/* Location:              E:\java\java学习\hutubill\lib\all.jar!\com\mysql\jdbc\util\TimezoneDump.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version:       0.7.1
 */
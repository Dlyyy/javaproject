/*    */ package com.mysql.jdbc.util;
/*    */ 
/*    */ import java.sql.ResultSet;
/*    */ import java.sql.ResultSetMetaData;
/*    */ import java.sql.SQLException;
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
/*    */ public class ResultSetUtil
/*    */ {
/*    */   public static StringBuffer appendResultSetSlashGStyle(StringBuffer appendTo, ResultSet rs)
/*    */     throws SQLException
/*    */   {
/* 42 */     ResultSetMetaData rsmd = rs.getMetaData();
/*    */     
/* 44 */     int numFields = rsmd.getColumnCount();
/* 45 */     int maxWidth = 0;
/*    */     
/* 47 */     String[] fieldNames = new String[numFields];
/*    */     
/* 49 */     for (int i = 0; i < numFields; i++) {
/* 50 */       fieldNames[i] = rsmd.getColumnLabel(i + 1);
/*    */       
/* 52 */       if (fieldNames[i].length() > maxWidth) {
/* 53 */         maxWidth = fieldNames[i].length();
/*    */       }
/*    */     }
/*    */     
/* 57 */     int rowCount = 1;
/*    */     
/* 59 */     while (rs.next()) {
/* 60 */       appendTo.append("*************************** ");
/* 61 */       appendTo.append(rowCount++);
/* 62 */       appendTo.append(". row ***************************\n");
/*    */       
/* 64 */       for (int i = 0; i < numFields; i++) {
/* 65 */         int leftPad = maxWidth - fieldNames[i].length();
/*    */         
/* 67 */         for (int j = 0; j < leftPad; j++) {
/* 68 */           appendTo.append(" ");
/*    */         }
/*    */         
/* 71 */         appendTo.append(fieldNames[i]);
/* 72 */         appendTo.append(": ");
/*    */         
/* 74 */         String stringVal = rs.getString(i + 1);
/*    */         
/* 76 */         if (stringVal != null) {
/* 77 */           appendTo.append(stringVal);
/*    */         } else {
/* 79 */           appendTo.append("NULL");
/*    */         }
/*    */         
/* 82 */         appendTo.append("\n");
/*    */       }
/*    */       
/* 85 */       appendTo.append("\n");
/*    */     }
/*    */     
/* 88 */     return appendTo;
/*    */   }
/*    */ }


/* Location:              E:\java\java学习\hutubill\lib\all.jar!\com\mysql\jdbc\util\ResultSetUtil.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version:       0.7.1
 */
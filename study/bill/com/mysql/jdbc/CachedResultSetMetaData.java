/*    */ package com.mysql.jdbc;
/*    */ 
/*    */ import java.sql.ResultSetMetaData;
/*    */ import java.util.Map;
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
/*    */ class CachedResultSetMetaData
/*    */ {
/* 30 */   Map columnNameToIndex = null;
/*    */   
/*    */ 
/*    */   Field[] fields;
/*    */   
/*    */ 
/* 36 */   Map fullColumnNameToIndex = null;
/*    */   ResultSetMetaData metadata;
/*    */ }


/* Location:              E:\java\java学习\hutubill\lib\all.jar!\com\mysql\jdbc\CachedResultSetMetaData.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version:       0.7.1
 */
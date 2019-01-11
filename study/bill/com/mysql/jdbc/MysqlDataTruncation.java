/*    */ package com.mysql.jdbc;
/*    */ 
/*    */ import java.sql.DataTruncation;
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
/*    */ public class MysqlDataTruncation
/*    */   extends DataTruncation
/*    */ {
/*    */   private String message;
/*    */   
/*    */   public MysqlDataTruncation(String message, int index, boolean parameter, boolean read, int dataSize, int transferSize)
/*    */   {
/* 60 */     super(index, parameter, read, dataSize, transferSize);
/*    */     
/* 62 */     this.message = message;
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   public String getMessage()
/*    */   {
/* 72 */     return super.getMessage() + ": " + this.message;
/*    */   }
/*    */ }


/* Location:              E:\java\java学习\hutubill\lib\all.jar!\com\mysql\jdbc\MysqlDataTruncation.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version:       0.7.1
 */
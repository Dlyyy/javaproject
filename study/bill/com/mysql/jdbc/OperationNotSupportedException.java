/*    */ package com.mysql.jdbc;
/*    */ 
/*    */ import java.sql.SQLException;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ class OperationNotSupportedException
/*    */   extends SQLException
/*    */ {
/*    */   OperationNotSupportedException()
/*    */   {
/* 13 */     super(Messages.getString("RowDataDynamic.10"), "S1009");
/*    */   }
/*    */ }


/* Location:              E:\java\java学习\hutubill\lib\all.jar!\com\mysql\jdbc\OperationNotSupportedException.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version:       0.7.1
 */
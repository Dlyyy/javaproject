/*    */ package com.mysql.jdbc;
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
/*    */ public class ConnectionFeatureNotAvailableException
/*    */   extends CommunicationsException
/*    */ {
/*    */   public ConnectionFeatureNotAvailableException(Connection conn, long lastPacketSentTimeMs, Exception underlyingException)
/*    */   {
/* 47 */     super(conn, lastPacketSentTimeMs, underlyingException);
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   public String getMessage()
/*    */   {
/* 56 */     return "Feature not available in this distribution of Connector/J";
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   public String getSQLState()
/*    */   {
/* 65 */     return "01S00";
/*    */   }
/*    */ }


/* Location:              E:\java\java学习\hutubill\lib\all.jar!\com\mysql\jdbc\ConnectionFeatureNotAvailableException.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version:       0.7.1
 */
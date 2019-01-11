/*    */ package com.mysql.jdbc.profiler;
/*    */ 
/*    */ import com.mysql.jdbc.Connection;
/*    */ import com.mysql.jdbc.log.Log;
/*    */ import java.sql.SQLException;
/*    */ import java.util.HashMap;
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
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class ProfileEventSink
/*    */ {
/* 38 */   private static final Map CONNECTIONS_TO_SINKS = new HashMap();
/*    */   
/* 40 */   private Connection ownerConnection = null;
/*    */   
/* 42 */   private Log log = null;
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   public static synchronized ProfileEventSink getInstance(Connection conn)
/*    */   {
/* 53 */     ProfileEventSink sink = (ProfileEventSink)CONNECTIONS_TO_SINKS.get(conn);
/*    */     
/*    */ 
/* 56 */     if (sink == null) {
/* 57 */       sink = new ProfileEventSink(conn);
/* 58 */       CONNECTIONS_TO_SINKS.put(conn, sink);
/*    */     }
/*    */     
/* 61 */     return sink;
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   public void consumeEvent(ProfilerEvent evt)
/*    */   {
/* 71 */     if (evt.eventType == 0) {
/* 72 */       this.log.logWarn(evt);
/*    */     } else {
/* 74 */       this.log.logInfo(evt);
/*    */     }
/*    */   }
/*    */   
/*    */   public static synchronized void removeInstance(Connection conn) {
/* 79 */     CONNECTIONS_TO_SINKS.remove(conn);
/*    */   }
/*    */   
/*    */   private ProfileEventSink(Connection conn) {
/* 83 */     this.ownerConnection = conn;
/*    */     try
/*    */     {
/* 86 */       this.log = this.ownerConnection.getLog();
/*    */     } catch (SQLException sqlEx) {
/* 88 */       throw new RuntimeException("Unable to get logger from connection");
/*    */     }
/*    */   }
/*    */ }


/* Location:              E:\java\java学习\hutubill\lib\all.jar!\com\mysql\jdbc\profiler\ProfileEventSink.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version:       0.7.1
 */
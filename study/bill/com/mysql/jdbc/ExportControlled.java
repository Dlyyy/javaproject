/*    */ package com.mysql.jdbc;
/*    */ 
/*    */ import java.io.BufferedInputStream;
/*    */ import java.io.BufferedOutputStream;
/*    */ import java.io.IOException;
/*    */ import java.net.Socket;
/*    */ import javax.net.ssl.SSLSocket;
/*    */ import javax.net.ssl.SSLSocketFactory;
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
/*    */ public class ExportControlled
/*    */ {
/*    */   protected static boolean enabled()
/*    */   {
/* 43 */     return true;
/*    */   }
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
/*    */   protected static void transformSocketToSSLSocket(MysqlIO mysqlIO)
/*    */     throws CommunicationsException
/*    */   {
/* 61 */     SSLSocketFactory sslFact = (SSLSocketFactory)SSLSocketFactory.getDefault();
/*    */     
/*    */     try
/*    */     {
/* 65 */       mysqlIO.mysqlConnection = sslFact.createSocket(mysqlIO.mysqlConnection, mysqlIO.host, mysqlIO.port, true);
/*    */       
/*    */ 
/*    */ 
/*    */ 
/* 70 */       ((SSLSocket)mysqlIO.mysqlConnection).setEnabledProtocols(new String[] { "TLSv1" });
/*    */       
/* 72 */       ((SSLSocket)mysqlIO.mysqlConnection).startHandshake();
/*    */       
/*    */ 
/* 75 */       if (mysqlIO.connection.getUseUnbufferedInput()) {
/* 76 */         mysqlIO.mysqlInput = mysqlIO.mysqlConnection.getInputStream();
/*    */       } else {
/* 78 */         mysqlIO.mysqlInput = new BufferedInputStream(mysqlIO.mysqlConnection.getInputStream(), 16384);
/*    */       }
/*    */       
/*    */ 
/* 82 */       mysqlIO.mysqlOutput = new BufferedOutputStream(mysqlIO.mysqlConnection.getOutputStream(), 16384);
/*    */       
/*    */ 
/* 85 */       mysqlIO.mysqlOutput.flush();
/*    */     } catch (IOException ioEx) {
/* 87 */       throw new CommunicationsException(mysqlIO.connection, mysqlIO.lastPacketSentTimeMs, ioEx);
/*    */     }
/*    */   }
/*    */ }


/* Location:              E:\java\java学习\hutubill\lib\all.jar!\com\mysql\jdbc\ExportControlled.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version:       0.7.1
 */
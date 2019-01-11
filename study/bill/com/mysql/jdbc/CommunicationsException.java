/*     */ package com.mysql.jdbc;
/*     */ 
/*     */ import java.net.BindException;
/*     */ import java.sql.SQLException;
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
/*     */ public class CommunicationsException
/*     */   extends SQLException
/*     */ {
/*     */   private static final long DEFAULT_WAIT_TIMEOUT_SECONDS = 28800L;
/*     */   private static final int DUE_TO_TIMEOUT_FALSE = 0;
/*     */   private static final int DUE_TO_TIMEOUT_MAYBE = 2;
/*     */   private static final int DUE_TO_TIMEOUT_TRUE = 1;
/*     */   private String exceptionMessage;
/*  55 */   private boolean streamingResultSetInPlay = false;
/*     */   
/*     */ 
/*     */   public CommunicationsException(Connection conn, long lastPacketSentTimeMs, Exception underlyingException)
/*     */   {
/*  60 */     long serverTimeoutSeconds = 0L;
/*  61 */     boolean isInteractiveClient = false;
/*     */     
/*  63 */     if (conn != null) {
/*  64 */       isInteractiveClient = conn.getInteractiveClient();
/*     */       
/*  66 */       String serverTimeoutSecondsStr = null;
/*     */       
/*  68 */       if (isInteractiveClient) {
/*  69 */         serverTimeoutSecondsStr = conn.getServerVariable("interactive_timeout");
/*     */       }
/*     */       else {
/*  72 */         serverTimeoutSecondsStr = conn.getServerVariable("wait_timeout");
/*     */       }
/*     */       
/*     */ 
/*  76 */       if (serverTimeoutSecondsStr != null) {
/*     */         try {
/*  78 */           serverTimeoutSeconds = Long.parseLong(serverTimeoutSecondsStr);
/*     */         }
/*     */         catch (NumberFormatException nfe) {
/*  81 */           serverTimeoutSeconds = 0L;
/*     */         }
/*     */       }
/*     */     }
/*     */     
/*  86 */     StringBuffer exceptionMessageBuf = new StringBuffer();
/*     */     
/*  88 */     if (lastPacketSentTimeMs == 0L) {
/*  89 */       lastPacketSentTimeMs = System.currentTimeMillis();
/*     */     }
/*     */     
/*  92 */     long timeSinceLastPacket = (System.currentTimeMillis() - lastPacketSentTimeMs) / 1000L;
/*     */     
/*  94 */     int dueToTimeout = 0;
/*     */     
/*  96 */     StringBuffer timeoutMessageBuf = null;
/*     */     
/*  98 */     if (this.streamingResultSetInPlay) {
/*  99 */       exceptionMessageBuf.append(Messages.getString("CommunicationsException.ClientWasStreaming"));
/*     */     }
/*     */     else {
/* 102 */       if (serverTimeoutSeconds != 0L) {
/* 103 */         if (timeSinceLastPacket > serverTimeoutSeconds) {
/* 104 */           dueToTimeout = 1;
/*     */           
/* 106 */           timeoutMessageBuf = new StringBuffer();
/*     */           
/* 108 */           timeoutMessageBuf.append(Messages.getString("CommunicationsException.2"));
/*     */           
/*     */ 
/* 111 */           if (!isInteractiveClient) {
/* 112 */             timeoutMessageBuf.append(Messages.getString("CommunicationsException.3"));
/*     */           }
/*     */           else {
/* 115 */             timeoutMessageBuf.append(Messages.getString("CommunicationsException.4"));
/*     */           }
/*     */           
/*     */         }
/*     */       }
/* 120 */       else if (timeSinceLastPacket > 28800L) {
/* 121 */         dueToTimeout = 2;
/*     */         
/* 123 */         timeoutMessageBuf = new StringBuffer();
/*     */         
/* 125 */         timeoutMessageBuf.append(Messages.getString("CommunicationsException.5"));
/*     */         
/* 127 */         timeoutMessageBuf.append(Messages.getString("CommunicationsException.6"));
/*     */         
/* 129 */         timeoutMessageBuf.append(Messages.getString("CommunicationsException.7"));
/*     */         
/* 131 */         timeoutMessageBuf.append(Messages.getString("CommunicationsException.8"));
/*     */       }
/*     */       
/*     */ 
/* 135 */       if ((dueToTimeout == 1) || (dueToTimeout == 2))
/*     */       {
/*     */ 
/* 138 */         exceptionMessageBuf.append(Messages.getString("CommunicationsException.9"));
/*     */         
/* 140 */         exceptionMessageBuf.append(timeSinceLastPacket);
/* 141 */         exceptionMessageBuf.append(Messages.getString("CommunicationsException.10"));
/*     */         
/*     */ 
/* 144 */         if (timeoutMessageBuf != null) {
/* 145 */           exceptionMessageBuf.append(timeoutMessageBuf);
/*     */         }
/*     */         
/* 148 */         exceptionMessageBuf.append(Messages.getString("CommunicationsException.11"));
/*     */         
/* 150 */         exceptionMessageBuf.append(Messages.getString("CommunicationsException.12"));
/*     */         
/* 152 */         exceptionMessageBuf.append(Messages.getString("CommunicationsException.13"));
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       }
/* 161 */       else if ((underlyingException instanceof BindException)) {
/* 162 */         if ((conn.getLocalSocketAddress() != null) && (!Util.interfaceExists(conn.getLocalSocketAddress())))
/*     */         {
/* 164 */           exceptionMessageBuf.append(Messages.getString("CommunicationsException.19a"));
/*     */         }
/*     */         else
/*     */         {
/* 168 */           exceptionMessageBuf.append(Messages.getString("CommunicationsException.14"));
/*     */           
/* 170 */           exceptionMessageBuf.append(Messages.getString("CommunicationsException.15"));
/*     */           
/* 172 */           exceptionMessageBuf.append(Messages.getString("CommunicationsException.16"));
/*     */           
/* 174 */           exceptionMessageBuf.append(Messages.getString("CommunicationsException.17"));
/*     */           
/* 176 */           exceptionMessageBuf.append(Messages.getString("CommunicationsException.18"));
/*     */           
/* 178 */           exceptionMessageBuf.append(Messages.getString("CommunicationsException.19"));
/*     */         }
/*     */       }
/*     */     }
/*     */     
/*     */ 
/*     */ 
/* 185 */     if (exceptionMessageBuf.length() == 0)
/*     */     {
/* 187 */       exceptionMessageBuf.append(Messages.getString("CommunicationsException.20"));
/*     */       
/*     */ 
/* 190 */       if (underlyingException != null) {
/* 191 */         exceptionMessageBuf.append(Messages.getString("CommunicationsException.21"));
/*     */         
/* 193 */         exceptionMessageBuf.append(Util.stackTraceToString(underlyingException));
/*     */       }
/*     */       
/*     */ 
/* 197 */       if ((conn != null) && (conn.getMaintainTimeStats()) && (!conn.getParanoid()))
/*     */       {
/* 199 */         exceptionMessageBuf.append("\n\nLast packet sent to the server was ");
/* 200 */         exceptionMessageBuf.append(System.currentTimeMillis() - lastPacketSentTimeMs);
/* 201 */         exceptionMessageBuf.append(" ms ago.");
/*     */       }
/*     */     }
/*     */     
/* 205 */     this.exceptionMessage = exceptionMessageBuf.toString();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public String getMessage()
/*     */   {
/* 214 */     return this.exceptionMessage;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public String getSQLState()
/*     */   {
/* 223 */     return "08S01";
/*     */   }
/*     */   
/*     */   protected void setWasStreamingResults() {
/* 227 */     this.streamingResultSetInPlay = true;
/*     */   }
/*     */ }


/* Location:              E:\java\java学习\hutubill\lib\all.jar!\com\mysql\jdbc\CommunicationsException.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version:       0.7.1
 */
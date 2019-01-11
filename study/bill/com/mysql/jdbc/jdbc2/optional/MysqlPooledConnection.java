/*     */ package com.mysql.jdbc.jdbc2.optional;
/*     */ 
/*     */ import com.mysql.jdbc.SQLError;
/*     */ import java.sql.SQLException;
/*     */ import java.util.Enumeration;
/*     */ import java.util.Hashtable;
/*     */ import javax.sql.ConnectionEvent;
/*     */ import javax.sql.ConnectionEventListener;
/*     */ import javax.sql.PooledConnection;
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
/*     */ public class MysqlPooledConnection
/*     */   implements PooledConnection
/*     */ {
/*     */   public static final int CONNECTION_ERROR_EVENT = 1;
/*     */   public static final int CONNECTION_CLOSED_EVENT = 2;
/*     */   private Hashtable eventListeners;
/*     */   private java.sql.Connection logicalHandle;
/*     */   private com.mysql.jdbc.Connection physicalConn;
/*     */   
/*     */   public MysqlPooledConnection(com.mysql.jdbc.Connection connection)
/*     */   {
/*  77 */     this.logicalHandle = null;
/*  78 */     this.physicalConn = connection;
/*  79 */     this.eventListeners = new Hashtable(10);
/*     */   }
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
/*     */   public synchronized void addConnectionEventListener(ConnectionEventListener connectioneventlistener)
/*     */   {
/*  94 */     if (this.eventListeners != null) {
/*  95 */       this.eventListeners.put(connectioneventlistener, connectioneventlistener);
/*     */     }
/*     */   }
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
/*     */   public synchronized void removeConnectionEventListener(ConnectionEventListener connectioneventlistener)
/*     */   {
/* 110 */     if (this.eventListeners != null) {
/* 111 */       this.eventListeners.remove(connectioneventlistener);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public synchronized java.sql.Connection getConnection()
/*     */     throws SQLException
/*     */   {
/* 122 */     return getConnection(true, false);
/*     */   }
/*     */   
/*     */ 
/*     */   protected synchronized java.sql.Connection getConnection(boolean resetServerState, boolean forXa)
/*     */     throws SQLException
/*     */   {
/* 129 */     if (this.physicalConn == null)
/*     */     {
/* 131 */       SQLException sqlException = SQLError.createSQLException("Physical Connection doesn't exist");
/*     */       
/* 133 */       callListener(1, sqlException);
/*     */       
/* 135 */       throw sqlException;
/*     */     }
/*     */     
/*     */     try
/*     */     {
/* 140 */       if (this.logicalHandle != null) {
/* 141 */         ((ConnectionWrapper)this.logicalHandle).close(false);
/*     */       }
/*     */       
/* 144 */       if (resetServerState) {
/* 145 */         this.physicalConn.resetServerState();
/*     */       }
/*     */       
/* 148 */       this.logicalHandle = new ConnectionWrapper(this, this.physicalConn, forXa);
/*     */     } catch (SQLException sqlException) {
/* 150 */       callListener(1, sqlException);
/*     */       
/* 152 */       throw sqlException;
/*     */     }
/*     */     
/* 155 */     return this.logicalHandle;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public synchronized void close()
/*     */     throws SQLException
/*     */   {
/* 166 */     if (this.physicalConn != null) {
/* 167 */       this.physicalConn.close();
/*     */     }
/*     */     
/* 170 */     this.physicalConn = null;
/*     */   }
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
/*     */   protected synchronized void callListener(int eventType, SQLException sqlException)
/*     */   {
/* 188 */     if (this.eventListeners == null)
/*     */     {
/* 190 */       return;
/*     */     }
/*     */     
/* 193 */     Enumeration enumeration = this.eventListeners.keys();
/* 194 */     ConnectionEvent connectionevent = new ConnectionEvent(this, sqlException);
/*     */     
/*     */ 
/* 197 */     while (enumeration.hasMoreElements())
/*     */     {
/* 199 */       ConnectionEventListener connectioneventlistener = (ConnectionEventListener)enumeration.nextElement();
/*     */       
/* 201 */       ConnectionEventListener connectioneventlistener1 = (ConnectionEventListener)this.eventListeners.get(connectioneventlistener);
/*     */       
/*     */ 
/* 204 */       if (eventType == 2) {
/* 205 */         connectioneventlistener1.connectionClosed(connectionevent);
/* 206 */       } else if (eventType == 1) {
/* 207 */         connectioneventlistener1.connectionErrorOccurred(connectionevent);
/*     */       }
/*     */     }
/*     */   }
/*     */ }


/* Location:              E:\java\java学习\hutubill\lib\all.jar!\com\mysql\jdbc\jdbc2\optional\MysqlPooledConnection.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version:       0.7.1
 */
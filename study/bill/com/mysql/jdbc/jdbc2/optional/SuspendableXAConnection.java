/*     */ package com.mysql.jdbc.jdbc2.optional;
/*     */ 
/*     */ import java.sql.SQLException;
/*     */ import java.util.HashMap;
/*     */ import java.util.Map;
/*     */ import javax.sql.XAConnection;
/*     */ import javax.transaction.xa.XAException;
/*     */ import javax.transaction.xa.XAResource;
/*     */ import javax.transaction.xa.Xid;
/*     */ 
/*     */ 
/*     */ 
/*     */ public class SuspendableXAConnection
/*     */   extends MysqlPooledConnection
/*     */   implements XAConnection, XAResource
/*     */ {
/*     */   public SuspendableXAConnection(com.mysql.jdbc.Connection connection)
/*     */   {
/*  19 */     super(connection);
/*  20 */     this.underlyingConnection = connection;
/*     */   }
/*     */   
/*  23 */   private static final Map XIDS_TO_PHYSICAL_CONNECTIONS = new HashMap();
/*     */   
/*     */ 
/*     */   private Xid currentXid;
/*     */   
/*     */ 
/*     */   private XAConnection currentXAConnection;
/*     */   
/*     */ 
/*     */   private XAResource currentXAResource;
/*     */   
/*     */   private com.mysql.jdbc.Connection underlyingConnection;
/*     */   
/*     */ 
/*     */   private static synchronized XAConnection findConnectionForXid(com.mysql.jdbc.Connection connectionToWrap, Xid xid)
/*     */     throws SQLException
/*     */   {
/*  40 */     XAConnection conn = (XAConnection)XIDS_TO_PHYSICAL_CONNECTIONS.get(xid);
/*     */     
/*  42 */     if (conn == null) {
/*  43 */       conn = new MysqlXAConnection(connectionToWrap, connectionToWrap.getLogXaCommands());
/*     */     }
/*     */     
/*     */ 
/*  47 */     return conn;
/*     */   }
/*     */   
/*     */   private static synchronized void removeXAConnectionMapping(Xid xid) {
/*  51 */     XIDS_TO_PHYSICAL_CONNECTIONS.remove(xid);
/*     */   }
/*     */   
/*     */   private synchronized void switchToXid(Xid xid) throws XAException {
/*  55 */     if (xid == null) {
/*  56 */       throw new XAException();
/*     */     }
/*     */     try
/*     */     {
/*  60 */       if (!xid.equals(this.currentXid)) {
/*  61 */         XAConnection toSwitchTo = findConnectionForXid(this.underlyingConnection, xid);
/*  62 */         this.currentXAConnection = toSwitchTo;
/*  63 */         this.currentXid = xid;
/*  64 */         this.currentXAResource = toSwitchTo.getXAResource();
/*     */       }
/*     */     } catch (SQLException sqlEx) {
/*  67 */       throw new XAException();
/*     */     }
/*     */   }
/*     */   
/*     */   public XAResource getXAResource() throws SQLException {
/*  72 */     return this;
/*     */   }
/*     */   
/*     */   public void commit(Xid xid, boolean arg1) throws XAException {
/*  76 */     switchToXid(xid);
/*  77 */     this.currentXAResource.commit(xid, arg1);
/*  78 */     removeXAConnectionMapping(xid);
/*     */   }
/*     */   
/*     */   public void end(Xid xid, int arg1) throws XAException {
/*  82 */     switchToXid(xid);
/*  83 */     this.currentXAResource.end(xid, arg1);
/*     */   }
/*     */   
/*     */   public void forget(Xid xid) throws XAException {
/*  87 */     switchToXid(xid);
/*  88 */     this.currentXAResource.forget(xid);
/*     */     
/*  90 */     removeXAConnectionMapping(xid);
/*     */   }
/*     */   
/*     */   public int getTransactionTimeout() throws XAException
/*     */   {
/*  95 */     return 0;
/*     */   }
/*     */   
/*     */   public boolean isSameRM(XAResource xaRes) throws XAException {
/*  99 */     return xaRes == this;
/*     */   }
/*     */   
/*     */   public int prepare(Xid xid) throws XAException {
/* 103 */     switchToXid(xid);
/* 104 */     return this.currentXAResource.prepare(xid);
/*     */   }
/*     */   
/*     */   public Xid[] recover(int flag) throws XAException {
/* 108 */     return MysqlXAConnection.recover(this.underlyingConnection, flag);
/*     */   }
/*     */   
/*     */   public void rollback(Xid xid) throws XAException {
/* 112 */     switchToXid(xid);
/* 113 */     this.currentXAResource.rollback(xid);
/* 114 */     removeXAConnectionMapping(xid);
/*     */   }
/*     */   
/*     */   public boolean setTransactionTimeout(int arg0) throws XAException
/*     */   {
/* 119 */     return false;
/*     */   }
/*     */   
/*     */   public void start(Xid xid, int arg1) throws XAException {
/* 123 */     switchToXid(xid);
/*     */     
/* 125 */     if (arg1 != 2097152) {
/* 126 */       this.currentXAResource.start(xid, arg1);
/*     */       
/* 128 */       return;
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 135 */     this.currentXAResource.start(xid, 134217728);
/*     */   }
/*     */   
/*     */   public synchronized java.sql.Connection getConnection() throws SQLException {
/* 139 */     if (this.currentXAConnection == null) {
/* 140 */       return getConnection(false, true);
/*     */     }
/*     */     
/* 143 */     return this.currentXAConnection.getConnection();
/*     */   }
/*     */   
/*     */   public void close() throws SQLException {
/* 147 */     if (this.currentXAConnection == null) {
/* 148 */       super.close();
/*     */     } else {
/* 150 */       removeXAConnectionMapping(this.currentXid);
/* 151 */       this.currentXAConnection.close();
/*     */     }
/*     */   }
/*     */ }


/* Location:              E:\java\java学习\hutubill\lib\all.jar!\com\mysql\jdbc\jdbc2\optional\SuspendableXAConnection.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version:       0.7.1
 */
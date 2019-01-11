/*     */ package com.mysql.jdbc.jdbc2.optional;
/*     */ 
/*     */ import com.mysql.jdbc.log.Log;
/*     */ import java.sql.ResultSet;
/*     */ import java.sql.SQLException;
/*     */ import java.sql.Statement;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collections;
/*     */ import java.util.HashMap;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import javax.sql.XAConnection;
/*     */ import javax.transaction.xa.XAException;
/*     */ import javax.transaction.xa.XAResource;
/*     */ import javax.transaction.xa.Xid;
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
/*     */ public class MysqlXAConnection
/*     */   extends MysqlPooledConnection
/*     */   implements XAConnection, XAResource
/*     */ {
/*     */   private com.mysql.jdbc.Connection underlyingConnection;
/*     */   private static final Map MYSQL_ERROR_CODES_TO_XA_ERROR_CODES;
/*     */   private Log log;
/*     */   protected boolean logXaCommands;
/*     */   
/*     */   static
/*     */   {
/*  73 */     HashMap temp = new HashMap();
/*     */     
/*  75 */     temp.put(new Integer(1397), new Integer(-4));
/*  76 */     temp.put(new Integer(1398), new Integer(-5));
/*  77 */     temp.put(new Integer(1399), new Integer(-7));
/*  78 */     temp.put(new Integer(1400), new Integer(-9));
/*  79 */     temp.put(new Integer(1401), new Integer(-3));
/*  80 */     temp.put(new Integer(1402), new Integer(100));
/*     */     
/*  82 */     MYSQL_ERROR_CODES_TO_XA_ERROR_CODES = Collections.unmodifiableMap(temp);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public MysqlXAConnection(com.mysql.jdbc.Connection connection, boolean logXaCommands)
/*     */     throws SQLException
/*     */   {
/*  90 */     super(connection);
/*  91 */     this.underlyingConnection = connection;
/*  92 */     this.log = connection.getLog();
/*  93 */     this.logXaCommands = logXaCommands;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public XAResource getXAResource()
/*     */     throws SQLException
/*     */   {
/* 106 */     return this;
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
/*     */   public int getTransactionTimeout()
/*     */     throws XAException
/*     */   {
/* 124 */     return 0;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean setTransactionTimeout(int arg0)
/*     */     throws XAException
/*     */   {
/* 150 */     return false;
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
/*     */ 
/*     */   public boolean isSameRM(XAResource xares)
/*     */     throws XAException
/*     */   {
/* 170 */     if ((xares instanceof MysqlXAConnection)) {
/* 171 */       return this.underlyingConnection.isSameResource(((MysqlXAConnection)xares).underlyingConnection);
/*     */     }
/*     */     
/*     */ 
/* 175 */     return false;
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
/*     */   public Xid[] recover(int flag)
/*     */     throws XAException
/*     */   {
/* 216 */     return recover(this.underlyingConnection, flag);
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected static Xid[] recover(java.sql.Connection c, int flag)
/*     */     throws XAException
/*     */   {
/* 240 */     boolean startRscan = (flag & 0x1000000) > 0;
/* 241 */     boolean endRscan = (flag & 0x800000) > 0;
/*     */     
/* 243 */     if ((!startRscan) && (!endRscan) && (flag != 0)) {
/* 244 */       throw new MysqlXAException(-5, "Invalid flag, must use TMNOFLAGS, or any combination of TMSTARTRSCAN and TMENDRSCAN", null);
/*     */     }
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
/* 257 */     if (!startRscan) {
/* 258 */       return new Xid[0];
/*     */     }
/*     */     
/* 261 */     ResultSet rs = null;
/* 262 */     Statement stmt = null;
/*     */     
/* 264 */     List recoveredXidList = new ArrayList();
/*     */     
/*     */     try
/*     */     {
/* 268 */       stmt = c.createStatement();
/*     */       
/* 270 */       rs = stmt.executeQuery("XA RECOVER");
/*     */       
/* 272 */       while (rs.next()) {
/* 273 */         int formatId = rs.getInt(1);
/* 274 */         int gtridLength = rs.getInt(2);
/* 275 */         int bqualLength = rs.getInt(3);
/* 276 */         byte[] gtridAndBqual = rs.getBytes(4);
/*     */         
/* 278 */         byte[] gtrid = new byte[gtridLength];
/* 279 */         byte[] bqual = new byte[bqualLength];
/*     */         
/* 281 */         if (gtridAndBqual.length != gtridLength + bqualLength) {
/* 282 */           throw new MysqlXAException(105, "Error while recovering XIDs from RM. GTRID and BQUAL are wrong sizes", null);
/*     */         }
/*     */         
/*     */ 
/*     */ 
/* 287 */         System.arraycopy(gtridAndBqual, 0, gtrid, 0, gtridLength);
/*     */         
/* 289 */         System.arraycopy(gtridAndBqual, gtridLength, bqual, 0, bqualLength);
/*     */         
/*     */ 
/* 292 */         recoveredXidList.add(new MysqlXid(gtrid, bqual, formatId));
/*     */       }
/*     */     }
/*     */     catch (SQLException sqlEx) {
/* 296 */       throw mapXAExceptionFromSQLException(sqlEx);
/*     */     } finally {
/* 298 */       if (rs != null) {
/*     */         try {
/* 300 */           rs.close();
/*     */         } catch (SQLException sqlEx) {
/* 302 */           throw mapXAExceptionFromSQLException(sqlEx);
/*     */         }
/*     */       }
/*     */       
/* 306 */       if (stmt != null) {
/*     */         try {
/* 308 */           stmt.close();
/*     */         } catch (SQLException sqlEx) {
/* 310 */           throw mapXAExceptionFromSQLException(sqlEx);
/*     */         }
/*     */       }
/*     */     }
/*     */     
/* 315 */     int numXids = recoveredXidList.size();
/*     */     
/* 317 */     Xid[] asXids = new Xid[numXids];
/* 318 */     Object[] asObjects = recoveredXidList.toArray();
/*     */     
/* 320 */     for (int i = 0; i < numXids; i++) {
/* 321 */       asXids[i] = ((Xid)asObjects[i]);
/*     */     }
/*     */     
/* 324 */     return asXids;
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
/*     */ 
/*     */ 
/*     */ 
/*     */   public int prepare(Xid xid)
/*     */     throws XAException
/*     */   {
/* 346 */     StringBuffer commandBuf = new StringBuffer();
/* 347 */     commandBuf.append("XA PREPARE ");
/* 348 */     commandBuf.append(xidToString(xid));
/*     */     
/* 350 */     dispatchCommand(commandBuf.toString());
/*     */     
/* 352 */     return 0;
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
/*     */   public void rollback(Xid xid)
/*     */     throws XAException
/*     */   {
/* 388 */     StringBuffer commandBuf = new StringBuffer();
/* 389 */     commandBuf.append("XA ROLLBACK ");
/* 390 */     commandBuf.append(xidToString(xid));
/*     */     try
/*     */     {
/* 393 */       dispatchCommand(commandBuf.toString());
/*     */     } finally {
/* 395 */       this.underlyingConnection.setInGlobalTx(false);
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
/*     */   public void end(Xid xid, int flags)
/*     */     throws XAException
/*     */   {
/* 427 */     StringBuffer commandBuf = new StringBuffer();
/* 428 */     commandBuf.append("XA END ");
/* 429 */     commandBuf.append(xidToString(xid));
/*     */     
/* 431 */     switch (flags) {
/*     */     case 67108864: 
/*     */       break;
/*     */     case 33554432: 
/* 435 */       commandBuf.append(" SUSPEND");
/* 436 */       break;
/*     */     case 536870912: 
/*     */       break;
/*     */     default: 
/* 440 */       throw new XAException(-5);
/*     */     }
/*     */     
/* 443 */     dispatchCommand(commandBuf.toString());
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void start(Xid xid, int flags)
/*     */     throws XAException
/*     */   {
/* 470 */     StringBuffer commandBuf = new StringBuffer();
/* 471 */     commandBuf.append("XA START ");
/* 472 */     commandBuf.append(xidToString(xid));
/*     */     
/* 474 */     switch (flags) {
/*     */     case 2097152: 
/* 476 */       commandBuf.append(" JOIN");
/* 477 */       break;
/*     */     case 134217728: 
/* 479 */       commandBuf.append(" RESUME");
/* 480 */       break;
/*     */     case 0: 
/*     */       break;
/*     */     
/*     */     default: 
/* 485 */       throw new XAException(-5);
/*     */     }
/*     */     
/* 488 */     dispatchCommand(commandBuf.toString());
/*     */     
/* 490 */     this.underlyingConnection.setInGlobalTx(true);
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void commit(Xid xid, boolean onePhase)
/*     */     throws XAException
/*     */   {
/* 515 */     StringBuffer commandBuf = new StringBuffer();
/* 516 */     commandBuf.append("XA COMMIT ");
/* 517 */     commandBuf.append(xidToString(xid));
/*     */     
/* 519 */     if (onePhase) {
/* 520 */       commandBuf.append(" ONE PHASE");
/*     */     }
/*     */     try
/*     */     {
/* 524 */       dispatchCommand(commandBuf.toString());
/*     */     } finally {
/* 526 */       this.underlyingConnection.setInGlobalTx(false);
/*     */     }
/*     */   }
/*     */   
/*     */   private ResultSet dispatchCommand(String command) throws XAException {
/* 531 */     Statement stmt = null;
/*     */     try
/*     */     {
/* 534 */       if (this.logXaCommands) {
/* 535 */         this.log.logDebug("Executing XA statement: " + command);
/*     */       }
/*     */       
/*     */ 
/* 539 */       stmt = this.underlyingConnection.createStatement();
/*     */       
/* 541 */       stmt.execute(command);
/*     */       
/* 543 */       ResultSet rs = stmt.getResultSet();
/*     */       
/* 545 */       return rs;
/*     */     } catch (SQLException sqlEx) {
/* 547 */       throw mapXAExceptionFromSQLException(sqlEx);
/*     */     } finally {
/* 549 */       if (stmt != null) {
/*     */         try {
/* 551 */           stmt.close();
/*     */         }
/*     */         catch (SQLException sqlEx) {}
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   protected static XAException mapXAExceptionFromSQLException(SQLException sqlEx)
/*     */   {
/* 560 */     Integer xaCode = (Integer)MYSQL_ERROR_CODES_TO_XA_ERROR_CODES.get(new Integer(sqlEx.getErrorCode()));
/*     */     
/*     */ 
/* 563 */     if (xaCode != null) {
/* 564 */       return new MysqlXAException(xaCode.intValue(), sqlEx.getMessage(), null);
/*     */     }
/*     */     
/*     */ 
/* 568 */     return new MysqlXAException(sqlEx.getMessage(), null);
/*     */   }
/*     */   
/*     */   private static String xidToString(Xid xid) {
/* 572 */     byte[] gtrid = xid.getGlobalTransactionId();
/*     */     
/* 574 */     byte[] btrid = xid.getBranchQualifier();
/*     */     
/* 576 */     int lengthAsString = 6;
/*     */     
/* 578 */     if (gtrid != null) {
/* 579 */       lengthAsString += 2 * gtrid.length;
/*     */     }
/*     */     
/* 582 */     if (btrid != null) {
/* 583 */       lengthAsString += 2 * btrid.length;
/*     */     }
/*     */     
/* 586 */     String formatIdInHex = Integer.toHexString(xid.getFormatId());
/*     */     
/* 588 */     lengthAsString += formatIdInHex.length();
/* 589 */     lengthAsString += 3;
/*     */     
/* 591 */     StringBuffer asString = new StringBuffer(lengthAsString);
/*     */     
/* 593 */     asString.append("0x");
/*     */     
/* 595 */     if (gtrid != null) {
/* 596 */       for (int i = 0; i < gtrid.length; i++) {
/* 597 */         String asHex = Integer.toHexString(gtrid[i] & 0xFF);
/*     */         
/* 599 */         if (asHex.length() == 1) {
/* 600 */           asString.append("0");
/*     */         }
/*     */         
/* 603 */         asString.append(asHex);
/*     */       }
/*     */     }
/*     */     
/* 607 */     asString.append(",");
/*     */     
/* 609 */     if (btrid != null) {
/* 610 */       asString.append("0x");
/*     */       
/* 612 */       for (int i = 0; i < btrid.length; i++) {
/* 613 */         String asHex = Integer.toHexString(btrid[i] & 0xFF);
/*     */         
/* 615 */         if (asHex.length() == 1) {
/* 616 */           asString.append("0");
/*     */         }
/*     */         
/* 619 */         asString.append(asHex);
/*     */       }
/*     */     }
/*     */     
/* 623 */     asString.append(",0x");
/* 624 */     asString.append(formatIdInHex);
/*     */     
/* 626 */     return asString.toString();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public synchronized java.sql.Connection getConnection()
/*     */     throws SQLException
/*     */   {
/* 635 */     java.sql.Connection connToWrap = getConnection(false, true);
/*     */     
/* 637 */     return connToWrap;
/*     */   }
/*     */   
/*     */   public void forget(Xid xid)
/*     */     throws XAException
/*     */   {}
/*     */ }


/* Location:              E:\java\java学习\hutubill\lib\all.jar!\com\mysql\jdbc\jdbc2\optional\MysqlXAConnection.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version:       0.7.1
 */
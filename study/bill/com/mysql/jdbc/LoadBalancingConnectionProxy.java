/*     */ package com.mysql.jdbc;
/*     */ 
/*     */ import java.lang.reflect.InvocationHandler;
/*     */ import java.lang.reflect.InvocationTargetException;
/*     */ import java.lang.reflect.Method;
/*     */ import java.lang.reflect.Proxy;
/*     */ import java.sql.SQLException;
/*     */ import java.util.Collection;
/*     */ import java.util.HashMap;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Properties;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class LoadBalancingConnectionProxy
/*     */   implements InvocationHandler, PingTarget
/*     */ {
/*     */   private static Method getLocalTimeMethod;
/*     */   private Connection currentConn;
/*     */   private List hostList;
/*     */   private Map liveConnections;
/*     */   private Map connectionsToHostsMap;
/*     */   private long[] responseTimes;
/*     */   private Map hostsToListIndexMap;
/*     */   
/*     */   static
/*     */   {
/*     */     try
/*     */     {
/*  63 */       getLocalTimeMethod = System.class.getMethod("nanoTime", new Class[0]);
/*     */     }
/*     */     catch (SecurityException e) {}catch (NoSuchMethodException e) {}
/*     */   }
/*     */   
/*     */   static abstract interface BalanceStrategy
/*     */   {
/*     */     public abstract Connection pickConnection() throws SQLException;
/*     */   }
/*     */   
/*     */   class BestResponseTimeBalanceStrategy implements LoadBalancingConnectionProxy.BalanceStrategy
/*     */   {
/*     */     BestResponseTimeBalanceStrategy() {}
/*     */     
/*     */     public Connection pickConnection() throws SQLException
/*     */     {
/*  79 */       long minResponseTime = Long.MAX_VALUE;
/*     */       
/*  81 */       int bestHostIndex = 0;
/*     */       
/*  83 */       long[] localResponseTimes = new long[LoadBalancingConnectionProxy.this.responseTimes.length];
/*     */       
/*  85 */       synchronized (LoadBalancingConnectionProxy.this.responseTimes) {
/*  86 */         System.arraycopy(LoadBalancingConnectionProxy.this.responseTimes, 0, localResponseTimes, 0, LoadBalancingConnectionProxy.this.responseTimes.length);
/*     */       }
/*     */       
/*  89 */       SQLException ex = null;
/*     */       
/*  91 */       for (int attempts = 0; attempts < 1200; attempts++) {
/*  92 */         for (int i = 0; i < localResponseTimes.length; i++) {
/*  93 */           long candidateResponseTime = localResponseTimes[i];
/*     */           
/*  95 */           if (candidateResponseTime < minResponseTime) {
/*  96 */             if (candidateResponseTime == 0L) {
/*  97 */               bestHostIndex = i;
/*     */               
/*  99 */               break;
/*     */             }
/*     */             
/* 102 */             bestHostIndex = i;
/* 103 */             minResponseTime = candidateResponseTime;
/*     */           }
/*     */         }
/*     */         
/* 107 */         if (bestHostIndex == localResponseTimes.length - 1)
/*     */         {
/*     */ 
/*     */ 
/* 111 */           synchronized (LoadBalancingConnectionProxy.this.responseTimes) {
/* 112 */             System.arraycopy(LoadBalancingConnectionProxy.this.responseTimes, 0, localResponseTimes, 0, LoadBalancingConnectionProxy.this.responseTimes.length);
/*     */           }
/*     */         }
/*     */         
/*     */ 
/* 117 */         String bestHost = (String)LoadBalancingConnectionProxy.this.hostList.get(bestHostIndex);
/*     */         
/* 119 */         Connection conn = (Connection)LoadBalancingConnectionProxy.this.liveConnections.get(bestHost);
/*     */         
/* 121 */         if (conn == null) {
/*     */           try {
/* 123 */             conn = LoadBalancingConnectionProxy.this.createConnectionForHost(bestHost);
/*     */           } catch (SQLException sqlEx) {
/* 125 */             ex = sqlEx;
/*     */             
/* 127 */             if (((sqlEx instanceof CommunicationsException)) || ("08S01".equals(sqlEx.getSQLState()))) {
/* 128 */               localResponseTimes[bestHostIndex] = Long.MAX_VALUE;
/*     */               try
/*     */               {
/* 131 */                 Thread.sleep(250L);
/*     */               }
/*     */               catch (InterruptedException e) {}
/*     */               
/* 135 */               continue;
/*     */             }
/* 137 */             throw sqlEx;
/*     */           }
/*     */         }
/*     */         
/*     */ 
/* 142 */         return conn;
/*     */       }
/*     */       
/* 145 */       if (ex != null) {
/* 146 */         throw ex;
/*     */       }
/*     */       
/* 149 */       return null;
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */   protected class ConnectionErrorFiringInvocationHandler
/*     */     implements InvocationHandler
/*     */   {
/* 157 */     Object invokeOn = null;
/*     */     
/*     */     public ConnectionErrorFiringInvocationHandler(Object toInvokeOn) {
/* 160 */       this.invokeOn = toInvokeOn;
/*     */     }
/*     */     
/*     */     public Object invoke(Object proxy, Method method, Object[] args) throws Throwable
/*     */     {
/* 165 */       Object result = null;
/*     */       try
/*     */       {
/* 168 */         result = method.invoke(this.invokeOn, args);
/*     */         
/* 170 */         if (result != null) {
/* 171 */           result = LoadBalancingConnectionProxy.this.proxyIfInterfaceIsJdbc(result, result.getClass());
/*     */         }
/*     */       } catch (InvocationTargetException e) {
/* 174 */         LoadBalancingConnectionProxy.this.dealWithInvocationException(e);
/*     */       }
/*     */       
/* 177 */       return result;
/*     */     }
/*     */   }
/*     */   
/*     */   class RandomBalanceStrategy implements LoadBalancingConnectionProxy.BalanceStrategy {
/*     */     RandomBalanceStrategy() {}
/*     */     
/* 184 */     public Connection pickConnection() throws SQLException { int random = (int)(Math.random() * LoadBalancingConnectionProxy.this.hostList.size());
/*     */       
/* 186 */       if (random == LoadBalancingConnectionProxy.this.hostList.size()) {
/* 187 */         random--;
/*     */       }
/*     */       
/* 190 */       String hostPortSpec = (String)LoadBalancingConnectionProxy.this.hostList.get(random);
/*     */       
/* 192 */       SQLException ex = null;
/*     */       
/* 194 */       for (int attempts = 0; attempts < 1200; attempts++) {
/* 195 */         Connection conn = (Connection)LoadBalancingConnectionProxy.this.liveConnections.get(hostPortSpec);
/*     */         
/* 197 */         if (conn == null) {
/*     */           try {
/* 199 */             conn = LoadBalancingConnectionProxy.this.createConnectionForHost(hostPortSpec);
/*     */           } catch (SQLException sqlEx) {
/* 201 */             ex = sqlEx;
/*     */             
/* 203 */             if (((sqlEx instanceof CommunicationsException)) || ("08S01".equals(sqlEx.getSQLState())))
/*     */             {
/*     */               try {
/* 206 */                 Thread.sleep(250L);
/*     */               }
/*     */               catch (InterruptedException e) {}
/*     */               
/* 210 */               continue;
/*     */             }
/* 212 */             throw sqlEx;
/*     */           }
/*     */         }
/*     */         
/*     */ 
/* 217 */         return conn;
/*     */       }
/*     */       
/* 220 */       if (ex != null) {
/* 221 */         throw ex;
/*     */       }
/*     */       
/* 224 */       return null;
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
/* 241 */   boolean inTransaction = false;
/*     */   
/* 243 */   long transactionStartTime = 0L;
/*     */   
/*     */   Properties localProps;
/*     */   
/* 247 */   boolean isClosed = false;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   BalanceStrategy balancer;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   LoadBalancingConnectionProxy(List hosts, Properties props)
/*     */     throws SQLException
/*     */   {
/* 262 */     this.hostList = hosts;
/*     */     
/* 264 */     int numHosts = this.hostList.size();
/*     */     
/* 266 */     this.liveConnections = new HashMap(numHosts);
/* 267 */     this.connectionsToHostsMap = new HashMap(numHosts);
/* 268 */     this.responseTimes = new long[numHosts];
/* 269 */     this.hostsToListIndexMap = new HashMap(numHosts);
/*     */     
/* 271 */     for (int i = 0; i < numHosts; i++) {
/* 272 */       this.hostsToListIndexMap.put(this.hostList.get(i), new Integer(i));
/*     */     }
/*     */     
/* 275 */     this.localProps = ((Properties)props.clone());
/* 276 */     this.localProps.remove("HOST");
/* 277 */     this.localProps.remove("PORT");
/* 278 */     this.localProps.setProperty("useLocalSessionState", "true");
/*     */     
/* 280 */     String strategy = this.localProps.getProperty("loadBalanceStrategy", "random");
/*     */     
/*     */ 
/* 283 */     if ("random".equals(strategy)) {
/* 284 */       this.balancer = new RandomBalanceStrategy();
/* 285 */     } else if ("bestResponseTime".equals(strategy)) {
/* 286 */       this.balancer = new BestResponseTimeBalanceStrategy();
/*     */     } else {
/* 288 */       throw SQLError.createSQLException(Messages.getString("InvalidLoadBalanceStrategy", new Object[] { strategy }), "S1009");
/*     */     }
/*     */     
/*     */ 
/*     */ 
/* 293 */     pickNewConnection();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private synchronized Connection createConnectionForHost(String hostPortSpec)
/*     */     throws SQLException
/*     */   {
/* 306 */     Properties connProps = (Properties)this.localProps.clone();
/*     */     
/* 308 */     String[] hostPortPair = NonRegisteringDriver.parseHostPortPair(hostPortSpec);
/*     */     
/*     */ 
/* 311 */     if (hostPortPair[1] == null) {
/* 312 */       hostPortPair[1] = "3306";
/*     */     }
/*     */     
/* 315 */     connProps.setProperty("HOST", hostPortSpec);
/*     */     
/* 317 */     connProps.setProperty("PORT", hostPortPair[1]);
/*     */     
/*     */ 
/* 320 */     Connection conn = new Connection(hostPortSpec, Integer.parseInt(hostPortPair[1]), connProps, connProps.getProperty("DBNAME"), "jdbc:mysql://" + hostPortPair[0] + ":" + hostPortPair[1] + "/");
/*     */     
/*     */ 
/*     */ 
/*     */ 
/* 325 */     this.liveConnections.put(hostPortSpec, conn);
/* 326 */     this.connectionsToHostsMap.put(conn, hostPortSpec);
/*     */     
/* 328 */     return conn;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   void dealWithInvocationException(InvocationTargetException e)
/*     */     throws SQLException, Throwable, InvocationTargetException
/*     */   {
/* 339 */     Throwable t = e.getTargetException();
/*     */     
/* 341 */     if (t != null) {
/* 342 */       if ((t instanceof SQLException)) {
/* 343 */         String sqlState = ((SQLException)t).getSQLState();
/*     */         
/* 345 */         if ((sqlState != null) && 
/* 346 */           (sqlState.startsWith("08")))
/*     */         {
/*     */ 
/* 349 */           invalidateCurrentConnection();
/*     */         }
/*     */       }
/*     */       
/*     */ 
/* 354 */       throw t;
/*     */     }
/*     */     
/* 357 */     throw e;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   synchronized void invalidateCurrentConnection()
/*     */     throws SQLException
/*     */   {
/*     */     try
/*     */     {
/* 367 */       if (!this.currentConn.isClosed()) {
/* 368 */         this.currentConn.close();
/*     */       }
/*     */     }
/*     */     finally {
/* 372 */       this.liveConnections.remove(this.connectionsToHostsMap.get(this.currentConn));
/*     */       
/* 374 */       this.connectionsToHostsMap.remove(this.currentConn);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public Object invoke(Object proxy, Method method, Object[] args)
/*     */     throws Throwable
/*     */   {
/* 386 */     String methodName = method.getName();
/*     */     
/* 388 */     if ("close".equals(methodName)) {
/* 389 */       synchronized (this.liveConnections)
/*     */       {
/* 391 */         Iterator allConnections = this.liveConnections.values().iterator();
/*     */         
/*     */ 
/* 394 */         while (allConnections.hasNext()) {
/* 395 */           ((Connection)allConnections.next()).close();
/*     */         }
/*     */         
/* 398 */         this.liveConnections.clear();
/* 399 */         this.connectionsToHostsMap.clear();
/*     */       }
/*     */       
/* 402 */       return null;
/*     */     }
/*     */     
/* 405 */     if ("isClosed".equals(methodName)) {
/* 406 */       return Boolean.valueOf(this.isClosed);
/*     */     }
/*     */     
/* 409 */     if (this.isClosed) {
/* 410 */       throw SQLError.createSQLException("No operations allowed after connection closed.", "08003");
/*     */     }
/*     */     
/*     */ 
/*     */ 
/* 415 */     if (!this.inTransaction) {
/* 416 */       this.inTransaction = true;
/* 417 */       this.transactionStartTime = getLocalTimeBestResolution();
/*     */     }
/*     */     
/* 420 */     Object result = null;
/*     */     try
/*     */     {
/* 423 */       result = method.invoke(this.currentConn, args);
/*     */       
/* 425 */       if (result != null) {
/* 426 */         if ((result instanceof Statement)) {
/* 427 */           ((Statement)result).setPingTarget(this);
/*     */         }
/*     */         
/* 430 */         result = proxyIfInterfaceIsJdbc(result, result.getClass());
/*     */       }
/*     */     } catch (InvocationTargetException e) {
/* 433 */       dealWithInvocationException(e);
/*     */     } finally {
/* 435 */       if (("commit".equals(methodName)) || ("rollback".equals(methodName))) {
/* 436 */         this.inTransaction = false;
/*     */         
/*     */ 
/* 439 */         int hostIndex = ((Integer)this.hostsToListIndexMap.get(this.connectionsToHostsMap.get(this.currentConn))).intValue();
/*     */         
/*     */ 
/*     */ 
/* 443 */         synchronized (this.responseTimes) {
/* 444 */           this.responseTimes[hostIndex] = (getLocalTimeBestResolution() - this.transactionStartTime);
/*     */         }
/*     */         
/*     */ 
/* 448 */         pickNewConnection();
/*     */       }
/*     */     }
/*     */     
/* 452 */     return result;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private synchronized void pickNewConnection()
/*     */     throws SQLException
/*     */   {
/* 462 */     if (this.currentConn == null) {
/* 463 */       this.currentConn = this.balancer.pickConnection();
/*     */       
/* 465 */       return;
/*     */     }
/*     */     
/* 468 */     Connection newConn = this.balancer.pickConnection();
/*     */     
/* 470 */     newConn.setTransactionIsolation(this.currentConn.getTransactionIsolation());
/*     */     
/* 472 */     newConn.setAutoCommit(this.currentConn.getAutoCommit());
/* 473 */     this.currentConn = newConn;
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
/*     */   Object proxyIfInterfaceIsJdbc(Object toProxy, Class clazz)
/*     */   {
/* 486 */     Class[] interfaces = clazz.getInterfaces();
/*     */     
/* 488 */     int i = 0; if (i < interfaces.length) {
/* 489 */       String packageName = interfaces[i].getPackage().getName();
/*     */       
/* 491 */       if (("java.sql".equals(packageName)) || ("javax.sql".equals(packageName)))
/*     */       {
/* 493 */         return Proxy.newProxyInstance(toProxy.getClass().getClassLoader(), interfaces, new ConnectionErrorFiringInvocationHandler(toProxy));
/*     */       }
/*     */       
/*     */ 
/*     */ 
/* 498 */       return proxyIfInterfaceIsJdbc(toProxy, interfaces[i]);
/*     */     }
/*     */     
/* 501 */     return toProxy;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   private static long getLocalTimeBestResolution()
/*     */   {
/* 509 */     if (getLocalTimeMethod != null) {
/*     */       try {
/* 511 */         return ((Long)getLocalTimeMethod.invoke(null, null)).longValue();
/*     */       }
/*     */       catch (IllegalArgumentException e) {}catch (IllegalAccessException e) {}catch (InvocationTargetException e) {}
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 522 */     return System.currentTimeMillis();
/*     */   }
/*     */   
/*     */   public synchronized void doPing() throws SQLException {
/* 526 */     Iterator allConns = this.liveConnections.values().iterator();
/*     */     
/* 528 */     while (allConns.hasNext()) {
/* 529 */       ((Connection)allConns.next()).ping();
/*     */     }
/*     */   }
/*     */ }


/* Location:              E:\java\java学习\hutubill\lib\all.jar!\com\mysql\jdbc\LoadBalancingConnectionProxy.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version:       0.7.1
 */
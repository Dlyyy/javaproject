/*     */ package com.mysql.jdbc;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.io.UnsupportedEncodingException;
/*     */ import java.lang.reflect.Proxy;
/*     */ import java.net.URLDecoder;
/*     */ import java.sql.Driver;
/*     */ import java.sql.DriverPropertyInfo;
/*     */ import java.sql.SQLException;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import java.util.Properties;
/*     */ import java.util.Set;
/*     */ import java.util.StringTokenizer;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class NonRegisteringDriver
/*     */   implements Driver
/*     */ {
/*     */   private static final String REPLICATION_URL_PREFIX = "jdbc:mysql:replication://";
/*     */   private static final String URL_PREFIX = "jdbc:mysql://";
/*     */   private static final String MXJ_URL_PREFIX = "jdbc:mysql:mxj://";
/*     */   private static final String LOADBALANCE_URL_PREFIX = "jdbc:mysql:loadbalance://";
/*     */   public static final String DBNAME_PROPERTY_KEY = "DBNAME";
/*     */   public static final boolean DEBUG = false;
/*     */   public static final int HOST_NAME_INDEX = 0;
/*     */   public static final String HOST_PROPERTY_KEY = "HOST";
/*     */   public static final String PASSWORD_PROPERTY_KEY = "password";
/*     */   public static final int PORT_NUMBER_INDEX = 1;
/*     */   public static final String PORT_PROPERTY_KEY = "PORT";
/*     */   public static final String PROPERTIES_TRANSFORM_KEY = "propertiesTransform";
/*     */   public static final boolean TRACE = false;
/*     */   public static final String USE_CONFIG_PROPERTY_KEY = "useConfigs";
/*     */   public static final String USER_PROPERTY_KEY = "user";
/*     */   
/*     */   static int getMajorVersionInternal()
/*     */   {
/* 131 */     return safeIntParse("5");
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   static int getMinorVersionInternal()
/*     */   {
/* 140 */     return safeIntParse("0");
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
/*     */   protected static String[] parseHostPortPair(String hostPortPair)
/*     */     throws SQLException
/*     */   {
/* 159 */     int portIndex = hostPortPair.indexOf(":");
/*     */     
/* 161 */     String[] splitValues = new String[2];
/*     */     
/* 163 */     String hostname = null;
/*     */     
/* 165 */     if (portIndex != -1) {
/* 166 */       if (portIndex + 1 < hostPortPair.length()) {
/* 167 */         String portAsString = hostPortPair.substring(portIndex + 1);
/* 168 */         hostname = hostPortPair.substring(0, portIndex);
/*     */         
/* 170 */         splitValues[0] = hostname;
/*     */         
/* 172 */         splitValues[1] = portAsString;
/*     */       } else {
/* 174 */         throw SQLError.createSQLException(Messages.getString("NonRegisteringDriver.37"), "01S00");
/*     */       }
/*     */     }
/*     */     else
/*     */     {
/* 179 */       splitValues[0] = hostPortPair;
/* 180 */       splitValues[1] = null;
/*     */     }
/*     */     
/* 183 */     return splitValues;
/*     */   }
/*     */   
/*     */   private static int safeIntParse(String intAsString) {
/*     */     try {
/* 188 */       return Integer.parseInt(intAsString);
/*     */     } catch (NumberFormatException nfe) {}
/* 190 */     return 0;
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
/*     */   public NonRegisteringDriver()
/*     */     throws SQLException
/*     */   {}
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean acceptsURL(String url)
/*     */     throws SQLException
/*     */   {
/* 220 */     return parseURL(url, null) != null;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public java.sql.Connection connect(String url, Properties info)
/*     */     throws SQLException
/*     */   {
/* 269 */     if (url != null) {
/* 270 */       if (StringUtils.startsWithIgnoreCase(url, "jdbc:mysql:loadbalance://"))
/* 271 */         return connectLoadBalanced(url, info);
/* 272 */       if (StringUtils.startsWithIgnoreCase(url, "jdbc:mysql:replication://"))
/*     */       {
/* 274 */         return connectReplicationConnection(url, info);
/*     */       }
/*     */     }
/*     */     
/* 278 */     Properties props = null;
/*     */     
/* 280 */     if ((props = parseURL(url, info)) == null) {
/* 281 */       return null;
/*     */     }
/*     */     try
/*     */     {
/* 285 */       return new Connection(host(props), port(props), props, database(props), url);
/*     */ 
/*     */ 
/*     */     }
/*     */     catch (SQLException sqlEx)
/*     */     {
/*     */ 
/* 292 */       throw sqlEx;
/*     */     } catch (Exception ex) {
/* 294 */       throw SQLError.createSQLException(Messages.getString("NonRegisteringDriver.17") + ex.toString() + Messages.getString("NonRegisteringDriver.18"), "08001");
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   private java.sql.Connection connectLoadBalanced(String url, Properties info)
/*     */     throws SQLException
/*     */   {
/* 304 */     Properties parsedProps = parseURL(url, info);
/*     */     
/* 306 */     if (parsedProps == null) {
/* 307 */       return null;
/*     */     }
/*     */     
/* 310 */     String hostValues = parsedProps.getProperty("HOST");
/*     */     
/* 312 */     List hostList = null;
/*     */     
/* 314 */     if (hostValues != null) {
/* 315 */       hostList = StringUtils.split(hostValues, ",", true);
/*     */     }
/*     */     
/* 318 */     if (hostList == null) {
/* 319 */       hostList = new ArrayList();
/* 320 */       hostList.add("localhost:3306");
/*     */     }
/*     */     
/* 323 */     LoadBalancingConnectionProxy proxyBal = new LoadBalancingConnectionProxy(hostList, parsedProps);
/*     */     
/*     */ 
/* 326 */     return (java.sql.Connection)Proxy.newProxyInstance(getClass().getClassLoader(), new Class[] { java.sql.Connection.class }, proxyBal);
/*     */   }
/*     */   
/*     */ 
/*     */   private java.sql.Connection connectReplicationConnection(String url, Properties info)
/*     */     throws SQLException
/*     */   {
/* 333 */     Properties parsedProps = parseURL(url, info);
/*     */     
/* 335 */     if (parsedProps == null) {
/* 336 */       return null;
/*     */     }
/*     */     
/* 339 */     Properties masterProps = (Properties)parsedProps.clone();
/* 340 */     Properties slavesProps = (Properties)parsedProps.clone();
/*     */     
/*     */ 
/*     */ 
/* 344 */     slavesProps.setProperty("com.mysql.jdbc.ReplicationConnection.isSlave", "true");
/*     */     
/*     */ 
/* 347 */     String hostValues = parsedProps.getProperty("HOST");
/*     */     
/* 349 */     if (hostValues != null) {
/* 350 */       StringTokenizer st = new StringTokenizer(hostValues, ",");
/*     */       
/* 352 */       StringBuffer masterHost = new StringBuffer();
/* 353 */       StringBuffer slaveHosts = new StringBuffer();
/*     */       
/* 355 */       if (st.hasMoreTokens()) {
/* 356 */         String[] hostPortPair = parseHostPortPair(st.nextToken());
/*     */         
/* 358 */         if (hostPortPair[0] != null) {
/* 359 */           masterHost.append(hostPortPair[0]);
/*     */         }
/*     */         
/* 362 */         if (hostPortPair[1] != null) {
/* 363 */           masterHost.append(":");
/* 364 */           masterHost.append(hostPortPair[1]);
/*     */         }
/*     */       }
/*     */       
/* 368 */       boolean firstSlaveHost = true;
/*     */       
/* 370 */       while (st.hasMoreTokens()) {
/* 371 */         String[] hostPortPair = parseHostPortPair(st.nextToken());
/*     */         
/* 373 */         if (!firstSlaveHost) {
/* 374 */           slaveHosts.append(",");
/*     */         } else {
/* 376 */           firstSlaveHost = false;
/*     */         }
/*     */         
/* 379 */         if (hostPortPair[0] != null) {
/* 380 */           slaveHosts.append(hostPortPair[0]);
/*     */         }
/*     */         
/* 383 */         if (hostPortPair[1] != null) {
/* 384 */           slaveHosts.append(":");
/* 385 */           slaveHosts.append(hostPortPair[1]);
/*     */         }
/*     */       }
/*     */       
/* 389 */       if (slaveHosts.length() == 0) {
/* 390 */         throw SQLError.createSQLException("Must specify at least one slave host to connect to for master/slave replication load-balancing functionality", "01S00");
/*     */       }
/*     */       
/*     */ 
/*     */ 
/*     */ 
/* 396 */       masterProps.setProperty("HOST", masterHost.toString());
/* 397 */       slavesProps.setProperty("HOST", slaveHosts.toString());
/*     */     }
/*     */     
/* 400 */     return new ReplicationConnection(masterProps, slavesProps);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public String database(Properties props)
/*     */   {
/* 412 */     return props.getProperty("DBNAME");
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public int getMajorVersion()
/*     */   {
/* 421 */     return getMajorVersionInternal();
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
/*     */   public int getMinorVersion()
/*     */   {
/* 434 */     return getMinorVersionInternal();
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
/*     */   public DriverPropertyInfo[] getPropertyInfo(String url, Properties info)
/*     */     throws SQLException
/*     */   {
/* 465 */     if (info == null) {
/* 466 */       info = new Properties();
/*     */     }
/*     */     
/* 469 */     if ((url != null) && (url.startsWith("jdbc:mysql://"))) {
/* 470 */       info = parseURL(url, info);
/*     */     }
/*     */     
/* 473 */     DriverPropertyInfo hostProp = new DriverPropertyInfo("HOST", info.getProperty("HOST"));
/*     */     
/* 475 */     hostProp.required = true;
/* 476 */     hostProp.description = Messages.getString("NonRegisteringDriver.3");
/*     */     
/* 478 */     DriverPropertyInfo portProp = new DriverPropertyInfo("PORT", info.getProperty("PORT", "3306"));
/*     */     
/* 480 */     portProp.required = false;
/* 481 */     portProp.description = Messages.getString("NonRegisteringDriver.7");
/*     */     
/* 483 */     DriverPropertyInfo dbProp = new DriverPropertyInfo("DBNAME", info.getProperty("DBNAME"));
/*     */     
/* 485 */     dbProp.required = false;
/* 486 */     dbProp.description = "Database name";
/*     */     
/* 488 */     DriverPropertyInfo userProp = new DriverPropertyInfo("user", info.getProperty("user"));
/*     */     
/* 490 */     userProp.required = true;
/* 491 */     userProp.description = Messages.getString("NonRegisteringDriver.13");
/*     */     
/* 493 */     DriverPropertyInfo passwordProp = new DriverPropertyInfo("password", info.getProperty("password"));
/*     */     
/*     */ 
/* 496 */     passwordProp.required = true;
/* 497 */     passwordProp.description = Messages.getString("NonRegisteringDriver.16");
/*     */     
/*     */ 
/* 500 */     DriverPropertyInfo[] dpi = ConnectionProperties.exposeAsDriverPropertyInfo(info, 5);
/*     */     
/*     */ 
/* 503 */     dpi[0] = hostProp;
/* 504 */     dpi[1] = portProp;
/* 505 */     dpi[2] = dbProp;
/* 506 */     dpi[3] = userProp;
/* 507 */     dpi[4] = passwordProp;
/*     */     
/* 509 */     return dpi;
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
/*     */   public String host(Properties props)
/*     */   {
/* 522 */     return props.getProperty("HOST", "localhost");
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
/*     */   public boolean jdbcCompliant()
/*     */   {
/* 538 */     return false;
/*     */   }
/*     */   
/*     */   public Properties parseURL(String url, Properties defaults) throws SQLException
/*     */   {
/* 543 */     Properties urlProps = defaults != null ? new Properties(defaults) : new Properties();
/*     */     
/*     */ 
/* 546 */     if (url == null) {
/* 547 */       return null;
/*     */     }
/*     */     
/* 550 */     if ((!StringUtils.startsWithIgnoreCase(url, "jdbc:mysql://")) && (!StringUtils.startsWithIgnoreCase(url, "jdbc:mysql:mxj://")) && (!StringUtils.startsWithIgnoreCase(url, "jdbc:mysql:loadbalance://")) && (!StringUtils.startsWithIgnoreCase(url, "jdbc:mysql:replication://")))
/*     */     {
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 557 */       return null;
/*     */     }
/*     */     
/* 560 */     int beginningOfSlashes = url.indexOf("//");
/*     */     
/* 562 */     if (StringUtils.startsWithIgnoreCase(url, "jdbc:mysql:mxj://")) {
/* 563 */       urlProps.setProperty("socketFactory", "com.mysql.management.driverlaunched.ServerLauncherSocketFactory");
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 572 */     int index = url.indexOf("?");
/*     */     
/* 574 */     if (index != -1) {
/* 575 */       String paramString = url.substring(index + 1, url.length());
/* 576 */       url = url.substring(0, index);
/*     */       
/* 578 */       StringTokenizer queryParams = new StringTokenizer(paramString, "&");
/*     */       
/* 580 */       while (queryParams.hasMoreTokens()) {
/* 581 */         String parameterValuePair = queryParams.nextToken();
/*     */         
/* 583 */         int indexOfEquals = StringUtils.indexOfIgnoreCase(0, parameterValuePair, "=");
/*     */         
/*     */ 
/* 586 */         String parameter = null;
/* 587 */         String value = null;
/*     */         
/* 589 */         if (indexOfEquals != -1) {
/* 590 */           parameter = parameterValuePair.substring(0, indexOfEquals);
/*     */           
/* 592 */           if (indexOfEquals + 1 < parameterValuePair.length()) {
/* 593 */             value = parameterValuePair.substring(indexOfEquals + 1);
/*     */           }
/*     */         }
/*     */         
/* 597 */         if ((value != null) && (value.length() > 0) && (parameter != null) && (parameter.length() > 0)) {
/*     */           try
/*     */           {
/* 600 */             urlProps.put(parameter, URLDecoder.decode(value, "UTF-8"));
/*     */           }
/*     */           catch (UnsupportedEncodingException badEncoding)
/*     */           {
/* 604 */             urlProps.put(parameter, URLDecoder.decode(value));
/*     */           }
/*     */           catch (NoSuchMethodError nsme) {
/* 607 */             urlProps.put(parameter, URLDecoder.decode(value));
/*     */           }
/*     */         }
/*     */       }
/*     */     }
/*     */     
/* 613 */     url = url.substring(beginningOfSlashes + 2);
/*     */     
/* 615 */     String hostStuff = null;
/*     */     
/* 617 */     int slashIndex = url.indexOf("/");
/*     */     
/* 619 */     if (slashIndex != -1) {
/* 620 */       hostStuff = url.substring(0, slashIndex);
/*     */       
/* 622 */       if (slashIndex + 1 < url.length()) {
/* 623 */         urlProps.put("DBNAME", url.substring(slashIndex + 1, url.length()));
/*     */       }
/*     */     }
/*     */     else {
/* 627 */       hostStuff = url;
/*     */     }
/*     */     
/* 630 */     if ((hostStuff != null) && (hostStuff.length() > 0)) {
/* 631 */       urlProps.put("HOST", hostStuff);
/*     */     }
/*     */     
/* 634 */     String propertiesTransformClassName = urlProps.getProperty("propertiesTransform");
/*     */     
/*     */ 
/* 637 */     if (propertiesTransformClassName != null) {
/*     */       try {
/* 639 */         ConnectionPropertiesTransform propTransformer = (ConnectionPropertiesTransform)Class.forName(propertiesTransformClassName).newInstance();
/*     */         
/*     */ 
/* 642 */         urlProps = propTransformer.transformProperties(urlProps);
/*     */       } catch (InstantiationException e) {
/* 644 */         throw SQLError.createSQLException("Unable to create properties transform instance '" + propertiesTransformClassName + "' due to underlying exception: " + e.toString(), "01S00");
/*     */ 
/*     */ 
/*     */       }
/*     */       catch (IllegalAccessException e)
/*     */       {
/*     */ 
/* 651 */         throw SQLError.createSQLException("Unable to create properties transform instance '" + propertiesTransformClassName + "' due to underlying exception: " + e.toString(), "01S00");
/*     */ 
/*     */ 
/*     */       }
/*     */       catch (ClassNotFoundException e)
/*     */       {
/*     */ 
/* 658 */         throw SQLError.createSQLException("Unable to create properties transform instance '" + propertiesTransformClassName + "' due to underlying exception: " + e.toString(), "01S00");
/*     */       }
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 667 */     if ((Util.isColdFusion()) && (urlProps.getProperty("autoConfigureForColdFusion", "true").equalsIgnoreCase("true")))
/*     */     {
/* 669 */       String configs = urlProps.getProperty("useConfigs");
/*     */       
/* 671 */       StringBuffer newConfigs = new StringBuffer();
/*     */       
/* 673 */       if (configs != null) {
/* 674 */         newConfigs.append(configs);
/* 675 */         newConfigs.append(",");
/*     */       }
/*     */       
/* 678 */       newConfigs.append("coldFusion");
/*     */       
/* 680 */       urlProps.setProperty("useConfigs", newConfigs.toString());
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/* 686 */     String configNames = null;
/*     */     
/* 688 */     if (defaults != null) {
/* 689 */       configNames = defaults.getProperty("useConfigs");
/*     */     }
/*     */     
/* 692 */     if (configNames == null) {
/* 693 */       configNames = urlProps.getProperty("useConfigs");
/*     */     }
/*     */     
/* 696 */     if (configNames != null) {
/* 697 */       List splitNames = StringUtils.split(configNames, ",", true);
/*     */       
/* 699 */       Properties configProps = new Properties();
/*     */       
/* 701 */       Iterator namesIter = splitNames.iterator();
/*     */       
/* 703 */       while (namesIter.hasNext()) {
/* 704 */         String configName = (String)namesIter.next();
/*     */         try
/*     */         {
/* 707 */           InputStream configAsStream = getClass().getResourceAsStream("configs/" + configName + ".properties");
/*     */           
/*     */ 
/*     */ 
/* 711 */           if (configAsStream == null) {
/* 712 */             throw SQLError.createSQLException("Can't find configuration template named '" + configName + "'", "01S00");
/*     */           }
/*     */           
/*     */ 
/*     */ 
/*     */ 
/* 718 */           configProps.load(configAsStream);
/*     */         } catch (IOException ioEx) {
/* 720 */           throw SQLError.createSQLException("Unable to load configuration template '" + configName + "' due to underlying IOException: " + ioEx, "01S00");
/*     */         }
/*     */       }
/*     */       
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 729 */       Iterator propsIter = urlProps.keySet().iterator();
/*     */       
/* 731 */       while (propsIter.hasNext()) {
/* 732 */         String key = propsIter.next().toString();
/* 733 */         String property = urlProps.getProperty(key);
/* 734 */         configProps.setProperty(key, property);
/*     */       }
/*     */       
/* 737 */       urlProps = configProps;
/*     */     }
/*     */     
/*     */ 
/*     */ 
/* 742 */     if (defaults != null) {
/* 743 */       Iterator propsIter = defaults.keySet().iterator();
/*     */       
/* 745 */       while (propsIter.hasNext()) {
/* 746 */         String key = propsIter.next().toString();
/* 747 */         String property = defaults.getProperty(key);
/* 748 */         urlProps.setProperty(key, property);
/*     */       }
/*     */     }
/*     */     
/* 752 */     return urlProps;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public int port(Properties props)
/*     */   {
/* 764 */     return Integer.parseInt(props.getProperty("PORT", "3306"));
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
/*     */   public String property(String name, Properties props)
/*     */   {
/* 778 */     return props.getProperty(name);
/*     */   }
/*     */ }


/* Location:              E:\java\java学习\hutubill\lib\all.jar!\com\mysql\jdbc\NonRegisteringDriver.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version:       0.7.1
 */
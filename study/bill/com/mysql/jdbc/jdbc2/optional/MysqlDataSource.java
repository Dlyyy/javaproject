/*     */ package com.mysql.jdbc.jdbc2.optional;
/*     */ 
/*     */ import com.mysql.jdbc.ConnectionProperties;
/*     */ import com.mysql.jdbc.Driver;
/*     */ import java.io.PrintWriter;
/*     */ import java.io.Serializable;
/*     */ import java.sql.Connection;
/*     */ import java.sql.SQLException;
/*     */ import java.util.Properties;
/*     */ import javax.naming.NamingException;
/*     */ import javax.naming.Reference;
/*     */ import javax.naming.Referenceable;
/*     */ import javax.naming.StringRefAddr;
/*     */ import javax.sql.DataSource;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class MysqlDataSource
/*     */   extends ConnectionProperties
/*     */   implements DataSource, Referenceable, Serializable
/*     */ {
/*  52 */   protected static Driver mysqlDriver = null;
/*     */   
/*     */   static {
/*     */     try {
/*  56 */       mysqlDriver = (Driver)Class.forName("com.mysql.jdbc.Driver").newInstance();
/*     */     }
/*     */     catch (Exception E) {
/*  59 */       throw new RuntimeException("Can not load Driver class com.mysql.jdbc.Driver");
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*  65 */   protected PrintWriter logWriter = null;
/*     */   
/*     */ 
/*  68 */   protected String databaseName = null;
/*     */   
/*     */ 
/*  71 */   protected String encoding = null;
/*     */   
/*     */ 
/*  74 */   protected String hostName = null;
/*     */   
/*     */ 
/*  77 */   protected String password = null;
/*     */   
/*     */ 
/*  80 */   protected String profileSql = "false";
/*     */   
/*     */ 
/*  83 */   protected String url = null;
/*     */   
/*     */ 
/*  86 */   protected String user = null;
/*     */   
/*     */ 
/*  89 */   protected boolean explicitUrl = false;
/*     */   
/*     */ 
/*  92 */   protected int port = 3306;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public Connection getConnection()
/*     */     throws SQLException
/*     */   {
/* 110 */     return getConnection(this.user, this.password);
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
/*     */   public Connection getConnection(String userID, String pass)
/*     */     throws SQLException
/*     */   {
/* 128 */     Properties props = new Properties();
/*     */     
/* 130 */     if (userID != null) {
/* 131 */       props.setProperty("user", userID);
/*     */     }
/*     */     
/* 134 */     if (pass != null) {
/* 135 */       props.setProperty("password", pass);
/*     */     }
/*     */     
/* 138 */     exposeAsProperties(props);
/*     */     
/* 140 */     return getConnection(props);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setDatabaseName(String dbName)
/*     */   {
/* 150 */     this.databaseName = dbName;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public String getDatabaseName()
/*     */   {
/* 159 */     return this.databaseName != null ? this.databaseName : "";
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setLogWriter(PrintWriter output)
/*     */     throws SQLException
/*     */   {
/* 168 */     this.logWriter = output;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public PrintWriter getLogWriter()
/*     */   {
/* 177 */     return this.logWriter;
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
/*     */   public int getLoginTimeout()
/*     */   {
/* 198 */     return 0;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setPassword(String pass)
/*     */   {
/* 208 */     this.password = pass;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setPort(int p)
/*     */   {
/* 218 */     this.port = p;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public int getPort()
/*     */   {
/* 227 */     return this.port;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setPortNumber(int p)
/*     */   {
/* 239 */     setPort(p);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public int getPortNumber()
/*     */   {
/* 248 */     return getPort();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setPropertiesViaRef(Reference ref)
/*     */     throws SQLException
/*     */   {
/* 261 */     super.initializeFromRef(ref);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public Reference getReference()
/*     */     throws NamingException
/*     */   {
/* 273 */     String factoryName = "com.mysql.jdbc.jdbc2.optional.MysqlDataSourceFactory";
/* 274 */     Reference ref = new Reference(getClass().getName(), factoryName, null);
/* 275 */     ref.add(new StringRefAddr("user", getUser()));
/*     */     
/* 277 */     ref.add(new StringRefAddr("password", this.password));
/*     */     
/* 279 */     ref.add(new StringRefAddr("serverName", getServerName()));
/* 280 */     ref.add(new StringRefAddr("port", "" + getPort()));
/* 281 */     ref.add(new StringRefAddr("databaseName", getDatabaseName()));
/* 282 */     ref.add(new StringRefAddr("url", getUrl()));
/* 283 */     ref.add(new StringRefAddr("explicitUrl", String.valueOf(this.explicitUrl)));
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */     try
/*     */     {
/* 290 */       storeToRef(ref);
/*     */     } catch (SQLException sqlEx) {
/* 292 */       throw new NamingException(sqlEx.getMessage());
/*     */     }
/*     */     
/* 295 */     return ref;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setServerName(String serverName)
/*     */   {
/* 305 */     this.hostName = serverName;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public String getServerName()
/*     */   {
/* 314 */     return this.hostName != null ? this.hostName : "";
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
/*     */   public void setURL(String url)
/*     */   {
/* 329 */     setUrl(url);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public String getURL()
/*     */   {
/* 338 */     return getUrl();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setUrl(String url)
/*     */   {
/* 350 */     this.url = url;
/* 351 */     this.explicitUrl = true;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public String getUrl()
/*     */   {
/* 360 */     if (!this.explicitUrl) {
/* 361 */       String builtUrl = "jdbc:mysql://";
/* 362 */       builtUrl = builtUrl + getServerName() + ":" + getPort() + "/" + getDatabaseName();
/*     */       
/*     */ 
/* 365 */       return builtUrl;
/*     */     }
/*     */     
/* 368 */     return this.url;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setUser(String userID)
/*     */   {
/* 378 */     this.user = userID;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public String getUser()
/*     */   {
/* 387 */     return this.user;
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
/*     */   protected Connection getConnection(Properties props)
/*     */     throws SQLException
/*     */   {
/* 403 */     String jdbcUrlToUse = null;
/*     */     
/* 405 */     if (!this.explicitUrl) {
/* 406 */       StringBuffer jdbcUrl = new StringBuffer("jdbc:mysql://");
/*     */       
/* 408 */       if (this.hostName != null) {
/* 409 */         jdbcUrl.append(this.hostName);
/*     */       }
/*     */       
/* 412 */       jdbcUrl.append(":");
/* 413 */       jdbcUrl.append(this.port);
/* 414 */       jdbcUrl.append("/");
/*     */       
/* 416 */       if (this.databaseName != null) {
/* 417 */         jdbcUrl.append(this.databaseName);
/*     */       }
/*     */       
/* 420 */       jdbcUrlToUse = jdbcUrl.toString();
/*     */     } else {
/* 422 */       jdbcUrlToUse = this.url;
/*     */     }
/*     */     
/* 425 */     return mysqlDriver.connect(jdbcUrlToUse, props);
/*     */   }
/*     */   
/*     */   public void setLoginTimeout(int seconds)
/*     */     throws SQLException
/*     */   {}
/*     */ }


/* Location:              E:\java\java学习\hutubill\lib\all.jar!\com\mysql\jdbc\jdbc2\optional\MysqlDataSource.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version:       0.7.1
 */
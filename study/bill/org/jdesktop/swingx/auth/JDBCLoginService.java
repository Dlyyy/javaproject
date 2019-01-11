/*     */ package org.jdesktop.swingx.auth;
/*     */ 
/*     */ import java.sql.Connection;
/*     */ import java.sql.DriverManager;
/*     */ import java.util.Properties;
/*     */ import java.util.logging.Level;
/*     */ import java.util.logging.Logger;
/*     */ import javax.naming.InitialContext;
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
/*     */ public class JDBCLoginService
/*     */   extends LoginService
/*     */ {
/*  35 */   private static final Logger LOG = Logger.getLogger(JDBCLoginService.class.getName());
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private Connection conn;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private String jndiContext;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   private Properties properties;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public JDBCLoginService(String driver, String url)
/*     */   {
/*  60 */     super(url);
/*     */     try {
/*  62 */       Class.forName(driver);
/*     */     } catch (Exception e) {
/*  64 */       LOG.log(Level.WARNING, "The driver passed to the JDBCLoginService constructor could not be loaded. This may be due to the driver not being on the classpath", e);
/*     */     }
/*     */     
/*     */ 
/*  68 */     setUrl(url);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public JDBCLoginService(String driver, String url, Properties props)
/*     */   {
/*  79 */     super(url);
/*     */     try {
/*  81 */       Class.forName(driver);
/*     */     } catch (Exception e) {
/*  83 */       LOG.log(Level.WARNING, "The driver passed to the JDBCLoginService constructor could not be loaded. This may be due to the driver not being on the classpath", e);
/*     */     }
/*     */     
/*     */ 
/*  87 */     setUrl(url);
/*  88 */     setProperties(props);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public JDBCLoginService(String jndiContext)
/*     */   {
/*  97 */     super(jndiContext);
/*  98 */     this.jndiContext = jndiContext;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public JDBCLoginService() {}
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public String getUrl()
/*     */   {
/* 112 */     return getServer();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public void setUrl(String url)
/*     */   {
/* 119 */     String old = getUrl();
/* 120 */     setServer(url);
/* 121 */     firePropertyChange("url", old, getUrl());
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public Properties getProperties()
/*     */   {
/* 128 */     return this.properties;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setProperties(Properties properties)
/*     */   {
/* 136 */     Properties old = getProperties();
/* 137 */     this.properties = properties;
/* 138 */     firePropertyChange("properties", old, getProperties());
/*     */   }
/*     */   
/*     */   public Connection getConnection() {
/* 142 */     return this.conn;
/*     */   }
/*     */   
/*     */   public void setConnection(Connection conn) {
/* 146 */     Connection old = getConnection();
/* 147 */     this.conn = conn;
/* 148 */     firePropertyChange("connection", old, getConnection());
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   private void connectByJNDI(String userName, char[] password)
/*     */     throws Exception
/*     */   {
/* 157 */     InitialContext ctx = new InitialContext();
/* 158 */     DataSource ds = (DataSource)ctx.lookup(this.jndiContext);
/* 159 */     this.conn = ds.getConnection(userName, new String(password));
/* 160 */     this.conn.setTransactionIsolation(4);
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
/*     */   private void connectByDriverManager(String userName, char[] password)
/*     */     throws Exception
/*     */   {
/* 175 */     if (getProperties() != null) {
/*     */       try {
/* 177 */         this.conn = DriverManager.getConnection(getUrl(), getProperties());
/* 178 */         this.conn.setTransactionIsolation(4);
/*     */       } catch (Exception e) {
/*     */         try {
/* 181 */           this.conn = DriverManager.getConnection(getUrl(), userName, new String(password));
/* 182 */           this.conn.setTransactionIsolation(4);
/*     */         } catch (Exception ex) {
/* 184 */           this.conn = DriverManager.getConnection(getUrl());
/* 185 */           this.conn.setTransactionIsolation(4);
/*     */         }
/*     */       }
/*     */     } else {
/*     */       try {
/* 190 */         this.conn = DriverManager.getConnection(getUrl(), userName, new String(password));
/*     */       } catch (Exception e) {
/* 192 */         LOG.log(Level.WARNING, "Connection with properties failed. Tryint to connect without.", e);
/*     */         
/*     */ 
/* 195 */         this.conn = DriverManager.getConnection(getUrl());
/*     */       }
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
/*     */   public boolean authenticate(String name, char[] password, String server)
/*     */     throws Exception
/*     */   {
/* 212 */     if (this.jndiContext != null) {
/*     */       try {
/* 214 */         connectByJNDI(name, password);
/*     */       } catch (Exception e) {
/*     */         try {
/* 217 */           connectByDriverManager(name, password);
/*     */         } catch (Exception ex) {
/* 219 */           LOG.log(Level.WARNING, "Login failed", ex);
/*     */           
/* 221 */           return false;
/*     */         }
/*     */       }
/*     */     } else {
/*     */       try {
/* 226 */         connectByDriverManager(name, password);
/*     */       } catch (Exception ex) {
/* 228 */         LOG.log(Level.WARNING, "", ex);
/* 229 */         return false;
/*     */       }
/*     */     }
/* 232 */     return true;
/*     */   }
/*     */ }


/* Location:              E:\java\java学习\hutubill\lib\all.jar!\org\jdesktop\swingx\auth\JDBCLoginService.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */
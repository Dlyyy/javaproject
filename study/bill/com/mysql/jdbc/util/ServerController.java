/*     */ package com.mysql.jdbc.util;
/*     */ 
/*     */ import com.mysql.jdbc.StringUtils;
/*     */ import java.io.File;
/*     */ import java.io.IOException;
/*     */ import java.io.PrintStream;
/*     */ import java.util.Iterator;
/*     */ import java.util.Properties;
/*     */ import java.util.Set;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class ServerController
/*     */ {
/*     */   public static final String BASEDIR_KEY = "basedir";
/*     */   public static final String DATADIR_KEY = "datadir";
/*     */   public static final String DEFAULTS_FILE_KEY = "defaults-file";
/*     */   public static final String EXECUTABLE_NAME_KEY = "executable";
/*     */   public static final String EXECUTABLE_PATH_KEY = "executablePath";
/*  80 */   private Process serverProcess = null;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*  85 */   private Properties serverProps = null;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*  90 */   private Properties systemProps = null;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public ServerController(String baseDir)
/*     */   {
/* 101 */     setBaseDir(baseDir);
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
/*     */   public ServerController(String basedir, String datadir) {}
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setBaseDir(String baseDir)
/*     */   {
/* 123 */     getServerProps().setProperty("basedir", baseDir);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setDataDir(String dataDir)
/*     */   {
/* 133 */     getServerProps().setProperty("datadir", dataDir);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public Process start()
/*     */     throws IOException
/*     */   {
/* 146 */     if (this.serverProcess != null) {
/* 147 */       throw new IllegalArgumentException("Server already started");
/*     */     }
/* 149 */     this.serverProcess = Runtime.getRuntime().exec(getCommandLine());
/*     */     
/* 151 */     return this.serverProcess;
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
/*     */   public void stop(boolean forceIfNecessary)
/*     */     throws IOException
/*     */   {
/* 165 */     if (this.serverProcess != null)
/*     */     {
/* 167 */       String basedir = getServerProps().getProperty("basedir");
/*     */       
/* 169 */       StringBuffer pathBuf = new StringBuffer(basedir);
/*     */       
/* 171 */       if (!basedir.endsWith(File.separator)) {
/* 172 */         pathBuf.append(File.separator);
/*     */       }
/*     */       
/* 175 */       String defaultsFilePath = getServerProps().getProperty("defaults-file");
/*     */       
/*     */ 
/* 178 */       pathBuf.append("bin");
/* 179 */       pathBuf.append(File.separator);
/* 180 */       pathBuf.append("mysqladmin shutdown");
/*     */       
/* 182 */       System.out.println(pathBuf.toString());
/*     */       
/* 184 */       Process mysqladmin = Runtime.getRuntime().exec(pathBuf.toString());
/*     */       
/* 186 */       int exitStatus = -1;
/*     */       try
/*     */       {
/* 189 */         exitStatus = mysqladmin.waitFor();
/*     */       }
/*     */       catch (InterruptedException ie) {}
/*     */       
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 198 */       if ((exitStatus != 0) && (forceIfNecessary)) {
/* 199 */         forceStop();
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public void forceStop()
/*     */   {
/* 208 */     if (this.serverProcess != null) {
/* 209 */       this.serverProcess.destroy();
/* 210 */       this.serverProcess = null;
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public synchronized Properties getServerProps()
/*     */   {
/* 221 */     if (this.serverProps == null) {
/* 222 */       this.serverProps = new Properties();
/*     */     }
/*     */     
/* 225 */     return this.serverProps;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private String getCommandLine()
/*     */   {
/* 235 */     StringBuffer commandLine = new StringBuffer(getFullExecutablePath());
/* 236 */     commandLine.append(buildOptionalCommandLine());
/*     */     
/* 238 */     return commandLine.toString();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private String getFullExecutablePath()
/*     */   {
/* 247 */     StringBuffer pathBuf = new StringBuffer();
/*     */     
/* 249 */     String optionalExecutablePath = getServerProps().getProperty("executablePath");
/*     */     
/*     */ 
/* 252 */     if (optionalExecutablePath == null)
/*     */     {
/* 254 */       String basedir = getServerProps().getProperty("basedir");
/* 255 */       pathBuf.append(basedir);
/*     */       
/* 257 */       if (!basedir.endsWith(File.separator)) {
/* 258 */         pathBuf.append(File.separatorChar);
/*     */       }
/*     */       
/* 261 */       if (runningOnWindows()) {
/* 262 */         pathBuf.append("bin");
/*     */       } else {
/* 264 */         pathBuf.append("libexec");
/*     */       }
/*     */       
/* 267 */       pathBuf.append(File.separatorChar);
/*     */     } else {
/* 269 */       pathBuf.append(optionalExecutablePath);
/*     */       
/* 271 */       if (!optionalExecutablePath.endsWith(File.separator)) {
/* 272 */         pathBuf.append(File.separatorChar);
/*     */       }
/*     */     }
/*     */     
/* 276 */     String executableName = getServerProps().getProperty("executable", "mysqld");
/*     */     
/*     */ 
/* 279 */     pathBuf.append(executableName);
/*     */     
/* 281 */     return pathBuf.toString();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private String buildOptionalCommandLine()
/*     */   {
/* 291 */     StringBuffer commandLineBuf = new StringBuffer();
/*     */     
/* 293 */     if (this.serverProps != null)
/*     */     {
/* 295 */       Iterator iter = this.serverProps.keySet().iterator();
/* 296 */       while (iter.hasNext()) {
/* 297 */         String key = (String)iter.next();
/* 298 */         String value = this.serverProps.getProperty(key);
/*     */         
/* 300 */         if (!isNonCommandLineArgument(key)) {
/* 301 */           if ((value != null) && (value.length() > 0)) {
/* 302 */             commandLineBuf.append(" \"");
/* 303 */             commandLineBuf.append("--");
/* 304 */             commandLineBuf.append(key);
/* 305 */             commandLineBuf.append("=");
/* 306 */             commandLineBuf.append(value);
/* 307 */             commandLineBuf.append("\"");
/*     */           } else {
/* 309 */             commandLineBuf.append(" --");
/* 310 */             commandLineBuf.append(key);
/*     */           }
/*     */         }
/*     */       }
/*     */     }
/*     */     
/* 316 */     return commandLineBuf.toString();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private boolean isNonCommandLineArgument(String propName)
/*     */   {
/* 325 */     return (propName.equals("executable")) || (propName.equals("executablePath"));
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private synchronized Properties getSystemProperties()
/*     */   {
/* 335 */     if (this.systemProps == null) {
/* 336 */       this.systemProps = System.getProperties();
/*     */     }
/*     */     
/* 339 */     return this.systemProps;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private boolean runningOnWindows()
/*     */   {
/* 348 */     return StringUtils.indexOfIgnoreCase(getSystemProperties().getProperty("os.name"), "WINDOWS") != -1;
/*     */   }
/*     */ }


/* Location:              E:\java\java学习\hutubill\lib\all.jar!\com\mysql\jdbc\util\ServerController.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version:       0.7.1
 */
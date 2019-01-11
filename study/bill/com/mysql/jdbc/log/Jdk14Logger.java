/*     */ package com.mysql.jdbc.log;
/*     */ 
/*     */ import com.mysql.jdbc.profiler.ProfilerEvent;
/*     */ import java.util.logging.Level;
/*     */ import java.util.logging.Logger;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class Jdk14Logger
/*     */   implements Log
/*     */ {
/*  40 */   private static final Level DEBUG = Level.FINE;
/*     */   
/*  42 */   private static final Level ERROR = Level.SEVERE;
/*     */   
/*  44 */   private static final Level FATAL = Level.SEVERE;
/*     */   
/*  46 */   private static final Level INFO = Level.INFO;
/*     */   
/*  48 */   private static final Level TRACE = Level.FINEST;
/*     */   
/*  50 */   private static final Level WARN = Level.WARNING;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*  55 */   protected Logger jdkLogger = null;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public Jdk14Logger(String name)
/*     */   {
/*  64 */     this.jdkLogger = Logger.getLogger(name);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public boolean isDebugEnabled()
/*     */   {
/*  71 */     return this.jdkLogger.isLoggable(Level.FINE);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public boolean isErrorEnabled()
/*     */   {
/*  78 */     return this.jdkLogger.isLoggable(Level.SEVERE);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public boolean isFatalEnabled()
/*     */   {
/*  85 */     return this.jdkLogger.isLoggable(Level.SEVERE);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public boolean isInfoEnabled()
/*     */   {
/*  92 */     return this.jdkLogger.isLoggable(Level.INFO);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public boolean isTraceEnabled()
/*     */   {
/*  99 */     return this.jdkLogger.isLoggable(Level.FINEST);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public boolean isWarnEnabled()
/*     */   {
/* 106 */     return this.jdkLogger.isLoggable(Level.WARNING);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void logDebug(Object message)
/*     */   {
/* 116 */     logInternal(DEBUG, message, null);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void logDebug(Object message, Throwable exception)
/*     */   {
/* 128 */     logInternal(DEBUG, message, exception);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void logError(Object message)
/*     */   {
/* 138 */     logInternal(ERROR, message, null);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void logError(Object message, Throwable exception)
/*     */   {
/* 150 */     logInternal(ERROR, message, exception);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void logFatal(Object message)
/*     */   {
/* 160 */     logInternal(FATAL, message, null);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void logFatal(Object message, Throwable exception)
/*     */   {
/* 172 */     logInternal(FATAL, message, exception);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void logInfo(Object message)
/*     */   {
/* 182 */     logInternal(INFO, message, null);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void logInfo(Object message, Throwable exception)
/*     */   {
/* 194 */     logInternal(INFO, message, exception);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void logTrace(Object message)
/*     */   {
/* 204 */     logInternal(TRACE, message, null);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void logTrace(Object message, Throwable exception)
/*     */   {
/* 216 */     logInternal(TRACE, message, exception);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void logWarn(Object message)
/*     */   {
/* 226 */     logInternal(WARN, message, null);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void logWarn(Object message, Throwable exception)
/*     */   {
/* 238 */     logInternal(WARN, message, exception);
/*     */   }
/*     */   
/*     */   private static final int findCallerStackDepth(StackTraceElement[] stackTrace) {
/* 242 */     int numFrames = stackTrace.length;
/*     */     
/* 244 */     for (int i = 0; i < numFrames; i++) {
/* 245 */       String callerClassName = stackTrace[i].getClassName();
/*     */       
/* 247 */       if ((!callerClassName.startsWith("com.mysql.jdbc")) || (callerClassName.startsWith("com.mysql.jdbc.compliance")))
/*     */       {
/* 249 */         return i;
/*     */       }
/*     */     }
/*     */     
/* 253 */     return 0;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private void logInternal(Level level, Object msg, Throwable exception)
/*     */   {
/* 262 */     if (this.jdkLogger.isLoggable(level)) {
/* 263 */       String messageAsString = null;
/* 264 */       String callerMethodName = "N/A";
/* 265 */       String callerClassName = "N/A";
/* 266 */       int lineNumber = 0;
/* 267 */       String fileName = "N/A";
/*     */       
/* 269 */       if ((msg instanceof ProfilerEvent)) {
/* 270 */         messageAsString = LogUtils.expandProfilerEventIfNecessary(msg).toString();
/*     */       }
/*     */       else {
/* 273 */         Throwable locationException = new Throwable();
/* 274 */         StackTraceElement[] locations = locationException.getStackTrace();
/*     */         
/*     */ 
/* 277 */         int frameIdx = findCallerStackDepth(locations);
/*     */         
/* 279 */         if (frameIdx != 0) {
/* 280 */           callerClassName = locations[frameIdx].getClassName();
/* 281 */           callerMethodName = locations[frameIdx].getMethodName();
/* 282 */           lineNumber = locations[frameIdx].getLineNumber();
/* 283 */           fileName = locations[frameIdx].getFileName();
/*     */         }
/*     */         
/* 286 */         messageAsString = String.valueOf(msg);
/*     */       }
/*     */       
/* 289 */       if (exception == null) {
/* 290 */         this.jdkLogger.logp(level, callerClassName, callerMethodName, messageAsString);
/*     */       }
/*     */       else {
/* 293 */         this.jdkLogger.logp(level, callerClassName, callerMethodName, messageAsString, exception);
/*     */       }
/*     */     }
/*     */   }
/*     */ }


/* Location:              E:\java\java学习\hutubill\lib\all.jar!\com\mysql\jdbc\log\Jdk14Logger.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version:       0.7.1
 */
/*     */ package com.mysql.jdbc.log;
/*     */ 
/*     */ import org.apache.log4j.Level;
/*     */ import org.apache.log4j.Logger;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class Log4JLogger
/*     */   implements Log
/*     */ {
/*     */   private Logger logger;
/*     */   
/*     */   public Log4JLogger(String instanceName)
/*     */   {
/*  42 */     this.logger = Logger.getLogger(instanceName);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean isDebugEnabled()
/*     */   {
/*  51 */     return this.logger.isDebugEnabled();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean isErrorEnabled()
/*     */   {
/*  60 */     return this.logger.isEnabledFor(Level.ERROR);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean isFatalEnabled()
/*     */   {
/*  69 */     return this.logger.isEnabledFor(Level.FATAL);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean isInfoEnabled()
/*     */   {
/*  78 */     return this.logger.isInfoEnabled();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean isTraceEnabled()
/*     */   {
/*  87 */     return this.logger.isDebugEnabled();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean isWarnEnabled()
/*     */   {
/*  96 */     return this.logger.isEnabledFor(Level.WARN);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void logDebug(Object msg)
/*     */   {
/* 105 */     this.logger.debug(LogUtils.expandProfilerEventIfNecessary(LogUtils.expandProfilerEventIfNecessary(msg)));
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void logDebug(Object msg, Throwable thrown)
/*     */   {
/* 116 */     this.logger.debug(LogUtils.expandProfilerEventIfNecessary(msg), thrown);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void logError(Object msg)
/*     */   {
/* 125 */     this.logger.error(LogUtils.expandProfilerEventIfNecessary(msg));
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void logError(Object msg, Throwable thrown)
/*     */   {
/* 135 */     this.logger.error(LogUtils.expandProfilerEventIfNecessary(msg), thrown);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void logFatal(Object msg)
/*     */   {
/* 144 */     this.logger.fatal(LogUtils.expandProfilerEventIfNecessary(msg));
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void logFatal(Object msg, Throwable thrown)
/*     */   {
/* 154 */     this.logger.fatal(LogUtils.expandProfilerEventIfNecessary(msg), thrown);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void logInfo(Object msg)
/*     */   {
/* 163 */     this.logger.info(LogUtils.expandProfilerEventIfNecessary(msg));
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void logInfo(Object msg, Throwable thrown)
/*     */   {
/* 173 */     this.logger.info(LogUtils.expandProfilerEventIfNecessary(msg), thrown);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void logTrace(Object msg)
/*     */   {
/* 182 */     this.logger.debug(LogUtils.expandProfilerEventIfNecessary(msg));
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void logTrace(Object msg, Throwable thrown)
/*     */   {
/* 192 */     this.logger.debug(LogUtils.expandProfilerEventIfNecessary(msg), thrown);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void logWarn(Object msg)
/*     */   {
/* 201 */     this.logger.warn(LogUtils.expandProfilerEventIfNecessary(msg));
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void logWarn(Object msg, Throwable thrown)
/*     */   {
/* 211 */     this.logger.warn(LogUtils.expandProfilerEventIfNecessary(msg), thrown);
/*     */   }
/*     */ }


/* Location:              E:\java\java学习\hutubill\lib\all.jar!\com\mysql\jdbc\log\Log4JLogger.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version:       0.7.1
 */
/*     */ package com.mysql.jdbc.log;
/*     */ 
/*     */ import org.apache.commons.logging.LogFactory;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class CommonsLogger
/*     */   implements Log
/*     */ {
/*     */   private org.apache.commons.logging.Log logger;
/*     */   
/*     */   public CommonsLogger(String instanceName)
/*     */   {
/*  33 */     this.logger = LogFactory.getLog(instanceName);
/*     */   }
/*     */   
/*     */   public boolean isDebugEnabled() {
/*  37 */     return this.logger.isInfoEnabled();
/*     */   }
/*     */   
/*     */   public boolean isErrorEnabled() {
/*  41 */     return this.logger.isErrorEnabled();
/*     */   }
/*     */   
/*     */   public boolean isFatalEnabled() {
/*  45 */     return this.logger.isFatalEnabled();
/*     */   }
/*     */   
/*     */   public boolean isInfoEnabled() {
/*  49 */     return this.logger.isInfoEnabled();
/*     */   }
/*     */   
/*     */   public boolean isTraceEnabled() {
/*  53 */     return this.logger.isTraceEnabled();
/*     */   }
/*     */   
/*     */   public boolean isWarnEnabled() {
/*  57 */     return this.logger.isWarnEnabled();
/*     */   }
/*     */   
/*     */   public void logDebug(Object msg) {
/*  61 */     this.logger.debug(LogUtils.expandProfilerEventIfNecessary(msg));
/*     */   }
/*     */   
/*     */   public void logDebug(Object msg, Throwable thrown) {
/*  65 */     this.logger.debug(LogUtils.expandProfilerEventIfNecessary(msg), thrown);
/*     */   }
/*     */   
/*     */   public void logError(Object msg) {
/*  69 */     this.logger.error(LogUtils.expandProfilerEventIfNecessary(msg));
/*     */   }
/*     */   
/*     */   public void logError(Object msg, Throwable thrown) {
/*  73 */     this.logger.fatal(LogUtils.expandProfilerEventIfNecessary(msg), thrown);
/*     */   }
/*     */   
/*     */   public void logFatal(Object msg) {
/*  77 */     this.logger.fatal(LogUtils.expandProfilerEventIfNecessary(msg));
/*     */   }
/*     */   
/*     */   public void logFatal(Object msg, Throwable thrown) {
/*  81 */     this.logger.fatal(LogUtils.expandProfilerEventIfNecessary(msg), thrown);
/*     */   }
/*     */   
/*     */   public void logInfo(Object msg) {
/*  85 */     this.logger.info(LogUtils.expandProfilerEventIfNecessary(msg));
/*     */   }
/*     */   
/*     */   public void logInfo(Object msg, Throwable thrown) {
/*  89 */     this.logger.info(LogUtils.expandProfilerEventIfNecessary(msg), thrown);
/*     */   }
/*     */   
/*     */   public void logTrace(Object msg) {
/*  93 */     this.logger.trace(LogUtils.expandProfilerEventIfNecessary(msg));
/*     */   }
/*     */   
/*     */   public void logTrace(Object msg, Throwable thrown) {
/*  97 */     this.logger.trace(LogUtils.expandProfilerEventIfNecessary(msg), thrown);
/*     */   }
/*     */   
/*     */   public void logWarn(Object msg) {
/* 101 */     this.logger.warn(LogUtils.expandProfilerEventIfNecessary(msg));
/*     */   }
/*     */   
/*     */   public void logWarn(Object msg, Throwable thrown) {
/* 105 */     this.logger.warn(LogUtils.expandProfilerEventIfNecessary(msg), thrown);
/*     */   }
/*     */ }


/* Location:              E:\java\java学习\hutubill\lib\all.jar!\com\mysql\jdbc\log\CommonsLogger.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version:       0.7.1
 */
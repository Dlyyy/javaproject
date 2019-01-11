/*     */ package com.mysql.jdbc.log;
/*     */ 
/*     */ import com.mysql.jdbc.Util;
/*     */ import com.mysql.jdbc.profiler.ProfilerEvent;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class LogUtils
/*     */ {
/*     */   public static final String CALLER_INFORMATION_NOT_AVAILABLE = "Caller information not available";
/*  32 */   private static final String LINE_SEPARATOR = System.getProperty("line.separator");
/*     */   
/*     */ 
/*  35 */   private static final int LINE_SEPARATOR_LENGTH = LINE_SEPARATOR.length();
/*     */   
/*     */ 
/*     */   public static Object expandProfilerEventIfNecessary(Object possibleProfilerEvent)
/*     */   {
/*  40 */     if ((possibleProfilerEvent instanceof ProfilerEvent)) {
/*  41 */       StringBuffer msgBuf = new StringBuffer();
/*     */       
/*  43 */       ProfilerEvent evt = (ProfilerEvent)possibleProfilerEvent;
/*     */       
/*  45 */       Throwable locationException = evt.getEventCreationPoint();
/*     */       
/*  47 */       if (locationException == null) {
/*  48 */         locationException = new Throwable();
/*     */       }
/*     */       
/*  51 */       msgBuf.append("Profiler Event: [");
/*     */       
/*  53 */       boolean appendLocationInfo = false;
/*     */       
/*  55 */       switch (evt.getEventType()) {
/*     */       case 4: 
/*  57 */         msgBuf.append("EXECUTE");
/*     */         
/*  59 */         break;
/*     */       
/*     */       case 5: 
/*  62 */         msgBuf.append("FETCH");
/*     */         
/*  64 */         break;
/*     */       
/*     */       case 1: 
/*  67 */         msgBuf.append("CONSTRUCT");
/*     */         
/*  69 */         break;
/*     */       
/*     */       case 2: 
/*  72 */         msgBuf.append("PREPARE");
/*     */         
/*  74 */         break;
/*     */       
/*     */       case 3: 
/*  77 */         msgBuf.append("QUERY");
/*     */         
/*  79 */         break;
/*     */       
/*     */       case 0: 
/*  82 */         msgBuf.append("WARN");
/*  83 */         appendLocationInfo = true;
/*     */         
/*  85 */         break;
/*     */       
/*     */       case 6: 
/*  88 */         msgBuf.append("SLOW QUERY");
/*  89 */         appendLocationInfo = false;
/*     */         
/*  91 */         break;
/*     */       
/*     */       default: 
/*  94 */         msgBuf.append("UNKNOWN");
/*     */       }
/*     */       
/*  97 */       msgBuf.append("] ");
/*  98 */       msgBuf.append(findCallingClassAndMethod(locationException));
/*  99 */       msgBuf.append(" duration: ");
/* 100 */       msgBuf.append(evt.getEventDuration());
/* 101 */       msgBuf.append(" ");
/* 102 */       msgBuf.append(evt.getDurationUnits());
/* 103 */       msgBuf.append(", connection-id: ");
/* 104 */       msgBuf.append(evt.getConnectionId());
/* 105 */       msgBuf.append(", statement-id: ");
/* 106 */       msgBuf.append(evt.getStatementId());
/* 107 */       msgBuf.append(", resultset-id: ");
/* 108 */       msgBuf.append(evt.getResultSetId());
/*     */       
/* 110 */       String evtMessage = evt.getMessage();
/*     */       
/* 112 */       if (evtMessage != null) {
/* 113 */         msgBuf.append(", message: ");
/* 114 */         msgBuf.append(evtMessage);
/*     */       }
/*     */       
/* 117 */       if (appendLocationInfo) {
/* 118 */         msgBuf.append("\n\nFull stack trace of location where event occurred:\n\n");
/*     */         
/* 120 */         msgBuf.append(Util.stackTraceToString(locationException));
/* 121 */         msgBuf.append("\n");
/*     */       }
/*     */       
/* 124 */       return msgBuf;
/*     */     }
/*     */     
/* 127 */     return possibleProfilerEvent;
/*     */   }
/*     */   
/*     */   public static String findCallingClassAndMethod(Throwable t) {
/* 131 */     String stackTraceAsString = Util.stackTraceToString(t);
/*     */     
/* 133 */     String callingClassAndMethod = "Caller information not available";
/*     */     
/* 135 */     int endInternalMethods = stackTraceAsString.lastIndexOf("com.mysql.jdbc");
/*     */     
/*     */ 
/* 138 */     if (endInternalMethods != -1) {
/* 139 */       int endOfLine = -1;
/* 140 */       int compliancePackage = stackTraceAsString.indexOf("com.mysql.jdbc.compliance", endInternalMethods);
/*     */       
/*     */ 
/* 143 */       if (compliancePackage != -1) {
/* 144 */         endOfLine = compliancePackage - LINE_SEPARATOR_LENGTH;
/*     */       } else {
/* 146 */         endOfLine = stackTraceAsString.indexOf(LINE_SEPARATOR, endInternalMethods);
/*     */       }
/*     */       
/*     */ 
/* 150 */       if (endOfLine != -1) {
/* 151 */         int nextEndOfLine = stackTraceAsString.indexOf(LINE_SEPARATOR, endOfLine + LINE_SEPARATOR_LENGTH);
/*     */         
/*     */ 
/* 154 */         if (nextEndOfLine != -1) {
/* 155 */           callingClassAndMethod = stackTraceAsString.substring(endOfLine + LINE_SEPARATOR_LENGTH, nextEndOfLine);
/*     */         }
/*     */         else {
/* 158 */           callingClassAndMethod = stackTraceAsString.substring(endOfLine + LINE_SEPARATOR_LENGTH);
/*     */         }
/*     */       }
/*     */     }
/*     */     
/*     */ 
/* 164 */     if ((!callingClassAndMethod.startsWith("\tat ")) && (!callingClassAndMethod.startsWith("at ")))
/*     */     {
/* 166 */       return "at " + callingClassAndMethod;
/*     */     }
/*     */     
/* 169 */     return callingClassAndMethod;
/*     */   }
/*     */ }


/* Location:              E:\java\java学习\hutubill\lib\all.jar!\com\mysql\jdbc\log\LogUtils.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version:       0.7.1
 */
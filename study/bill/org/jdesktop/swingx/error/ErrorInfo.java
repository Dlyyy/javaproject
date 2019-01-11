/*     */ package org.jdesktop.swingx.error;
/*     */ 
/*     */ import java.util.HashMap;
/*     */ import java.util.Map;
/*     */ import java.util.Map.Entry;
/*     */ import java.util.Properties;
/*     */ import java.util.logging.Level;
/*     */ import javax.swing.SwingUtilities;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class ErrorInfo
/*     */ {
/*     */   private String title;
/*     */   private String basicErrorMessage;
/*     */   private String detailedErrorMessage;
/*     */   private String category;
/*     */   private Throwable errorException;
/*     */   private Level errorLevel;
/*     */   private Map<String, String> state;
/*     */   
/*     */   public ErrorInfo(String title, String basicErrorMessage, String detailedErrorMessage, String category, Throwable errorException, Level errorLevel, Map<String, String> state)
/*     */   {
/* 136 */     this.title = title;
/* 137 */     this.basicErrorMessage = basicErrorMessage;
/* 138 */     this.detailedErrorMessage = detailedErrorMessage;
/* 139 */     this.category = category;
/* 140 */     this.errorException = errorException;
/* 141 */     this.errorLevel = (errorLevel == null ? Level.SEVERE : errorLevel);
/* 142 */     this.state = new HashMap();
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     try
/*     */     {
/* 150 */       Properties props = System.getProperties();
/* 151 */       for (Map.Entry<Object, Object> entry : props.entrySet()) {
/* 152 */         String key = entry.getKey() == null ? null : entry.getKey().toString();
/* 153 */         String val = entry.getKey() == null ? null : entry.getValue().toString();
/* 154 */         if (key != null) {
/* 155 */           this.state.put(key, val);
/*     */         }
/*     */       }
/*     */     }
/*     */     catch (SecurityException e) {}
/*     */     
/*     */ 
/*     */ 
/* 163 */     this.state.put("System.currentTimeMillis", "" + System.currentTimeMillis());
/* 164 */     this.state.put("isOnEDT", "" + SwingUtilities.isEventDispatchThread());
/*     */     
/*     */ 
/*     */ 
/* 168 */     if (state != null) {
/* 169 */       for (Map.Entry<String, String> entry : state.entrySet()) {
/* 170 */         this.state.put(entry.getKey(), entry.getValue());
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
/*     */   public String getTitle()
/*     */   {
/* 183 */     return this.title;
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
/*     */   public String getBasicErrorMessage()
/*     */   {
/* 207 */     return this.basicErrorMessage;
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
/*     */   public String getDetailedErrorMessage()
/*     */   {
/* 221 */     return this.detailedErrorMessage;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public String getCategory()
/*     */   {
/* 232 */     return this.category;
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
/*     */   public Throwable getErrorException()
/*     */   {
/* 245 */     return this.errorException;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public Level getErrorLevel()
/*     */   {
/* 256 */     return this.errorLevel;
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
/*     */   public Map<String, String> getState()
/*     */   {
/* 276 */     return new HashMap(this.state);
/*     */   }
/*     */ }


/* Location:              E:\java\java学习\hutubill\lib\all.jar!\org\jdesktop\swingx\error\ErrorInfo.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */
/*     */ package com.mysql.jdbc;
/*     */ 
/*     */ import java.text.MessageFormat;
/*     */ import java.util.Locale;
/*     */ import java.util.MissingResourceException;
/*     */ import java.util.ResourceBundle;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class Messages
/*     */ {
/*     */   private static final String BUNDLE_NAME = "com.mysql.jdbc.LocalizedErrorMessages";
/*     */   private static final ResourceBundle RESOURCE_BUNDLE;
/*     */   
/*     */   static
/*     */   {
/*  45 */     ResourceBundle temp = null;
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     try
/*     */     {
/*  54 */       temp = ResourceBundle.getBundle("com.mysql.jdbc.LocalizedErrorMessages", Locale.getDefault(), Messages.class.getClassLoader());
/*     */     }
/*     */     catch (Throwable t) {
/*     */       try {
/*  58 */         temp = ResourceBundle.getBundle("com.mysql.jdbc.LocalizedErrorMessages");
/*     */       } catch (Throwable t2) {
/*  60 */         throw new RuntimeException("Can't load resource bundle due to underlying exception " + t.toString());
/*     */       }
/*     */     }
/*     */     finally
/*     */     {
/*  65 */       RESOURCE_BUNDLE = temp;
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static String getString(String key)
/*     */   {
/*  77 */     if (RESOURCE_BUNDLE == null) {
/*  78 */       throw new RuntimeException("Localized messages from resource bundle 'com.mysql.jdbc.LocalizedErrorMessages' not loaded during initialization of driver.");
/*     */     }
/*     */     
/*     */ 
/*     */     try
/*     */     {
/*  84 */       if (key == null) {
/*  85 */         throw new IllegalArgumentException("Message key can not be null");
/*     */       }
/*     */       
/*     */ 
/*  89 */       String message = RESOURCE_BUNDLE.getString(key);
/*     */       
/*  91 */       if (message == null) {}
/*  92 */       return "Missing error message for key '" + key + "'";
/*     */     }
/*     */     catch (MissingResourceException e) {}
/*     */     
/*     */ 
/*  97 */     return '!' + key + '!';
/*     */   }
/*     */   
/*     */   public static String getString(String key, Object[] args)
/*     */   {
/* 102 */     return MessageFormat.format(getString(key), args);
/*     */   }
/*     */ }


/* Location:              E:\java\java学习\hutubill\lib\all.jar!\com\mysql\jdbc\Messages.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version:       0.7.1
 */
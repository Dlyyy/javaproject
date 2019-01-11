/*     */ package com.mysql.jdbc.log;
/*     */ 
/*     */ import com.mysql.jdbc.SQLError;
/*     */ import java.lang.reflect.Constructor;
/*     */ import java.lang.reflect.InvocationTargetException;
/*     */ import java.sql.SQLException;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class LogFactory
/*     */ {
/*     */   public static Log getLogger(String className, String instanceName)
/*     */     throws SQLException
/*     */   {
/*  57 */     if (className == null) {
/*  58 */       throw SQLError.createSQLException("Logger class can not be NULL", "S1009");
/*     */     }
/*     */     
/*     */ 
/*  62 */     if (instanceName == null) {
/*  63 */       throw SQLError.createSQLException("Logger instance name can not be NULL", "S1009");
/*     */     }
/*     */     
/*     */     try
/*     */     {
/*  68 */       Class loggerClass = null;
/*     */       try
/*     */       {
/*  71 */         loggerClass = Class.forName(className);
/*     */       } catch (ClassNotFoundException nfe) {
/*  73 */         loggerClass = Class.forName(Log.class.getPackage().getName() + "." + className);
/*     */       }
/*     */       
/*  76 */       Constructor constructor = loggerClass.getConstructor(new Class[] { String.class });
/*     */       
/*     */ 
/*  79 */       return (Log)constructor.newInstance(new Object[] { instanceName });
/*     */     } catch (ClassNotFoundException cnfe) {
/*  81 */       throw SQLError.createSQLException("Unable to load class for logger '" + className + "'", "S1009");
/*     */     }
/*     */     catch (NoSuchMethodException nsme) {
/*  84 */       throw SQLError.createSQLException("Logger class does not have a single-arg constructor that takes an instance name", "S1009");
/*     */     }
/*     */     catch (InstantiationException inse)
/*     */     {
/*  88 */       throw SQLError.createSQLException("Unable to instantiate logger class '" + className + "', exception in constructor?", "S1009");
/*     */     }
/*     */     catch (InvocationTargetException ite)
/*     */     {
/*  92 */       throw SQLError.createSQLException("Unable to instantiate logger class '" + className + "', exception in constructor?", "S1009");
/*     */     }
/*     */     catch (IllegalAccessException iae)
/*     */     {
/*  96 */       throw SQLError.createSQLException("Unable to instantiate logger class '" + className + "', constructor not public", "S1009");
/*     */     }
/*     */     catch (ClassCastException cce)
/*     */     {
/* 100 */       throw SQLError.createSQLException("Logger class '" + className + "' does not implement the '" + Log.class.getName() + "' interface", "S1009");
/*     */     }
/*     */   }
/*     */ }


/* Location:              E:\java\java学习\hutubill\lib\all.jar!\com\mysql\jdbc\log\LogFactory.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version:       0.7.1
 */
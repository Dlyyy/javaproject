/*    */ package com.mysql.jdbc.util;
/*    */ 
/*    */ import com.mysql.jdbc.ConnectionProperties;
/*    */ import java.io.PrintStream;
/*    */ import java.sql.SQLException;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class PropertiesDocGenerator
/*    */   extends ConnectionProperties
/*    */ {
/*    */   public static void main(String[] args)
/*    */     throws SQLException
/*    */   {
/* 38 */     System.out.println(new PropertiesDocGenerator().exposeAsXml());
/*    */   }
/*    */ }


/* Location:              E:\java\java学习\hutubill\lib\all.jar!\com\mysql\jdbc\util\PropertiesDocGenerator.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version:       0.7.1
 */
/*    */ package org.jdesktop.swingx.auth;
/*    */ 
/*    */ import java.util.Arrays;
/*    */ import java.util.HashMap;
/*    */ import java.util.Map;
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
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public final class SimpleLoginService
/*    */   extends LoginService
/*    */ {
/*    */   private Map<String, char[]> passwordMap;
/*    */   
/*    */   public SimpleLoginService(Map<String, char[]> passwordMap)
/*    */   {
/* 46 */     if (passwordMap == null) {
/* 47 */       passwordMap = new HashMap();
/*    */     }
/* 49 */     this.passwordMap = passwordMap;
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */   public boolean authenticate(String name, char[] password, String server)
/*    */     throws Exception
/*    */   {
/* 57 */     char[] p = (char[])this.passwordMap.get(name);
/* 58 */     return Arrays.equals(password, p);
/*    */   }
/*    */ }


/* Location:              E:\java\java学习\hutubill\lib\all.jar!\org\jdesktop\swingx\auth\SimpleLoginService.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */
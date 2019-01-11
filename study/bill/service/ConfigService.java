/*    */ package service;
/*    */ 
/*    */ import dao.ConfigDAO;
/*    */ import entity.Config;
/*    */ 
/*    */ public class ConfigService
/*    */ {
/*    */   public static final String budget = "budget";
/*    */   public static final String mysqlPath = "mysqlPath";
/*    */   public static final String default_budget = "500";
/* 11 */   static ConfigDAO dao = new ConfigDAO();
/*    */   
/* 13 */   static { init(); }
/*    */   
/*    */   public static void init()
/*    */   {
/* 17 */     init("budget", "500");
/* 18 */     init("mysqlPath", "");
/*    */   }
/*    */   
/*    */   private static void init(String key, String value)
/*    */   {
/* 23 */     Config config = dao.getByKey(key);
/* 24 */     if (config == null) {
/* 25 */       Config c = new Config();
/* 26 */       c.setKey(key);
/* 27 */       c.setValue(value);
/* 28 */       dao.add(c);
/*    */     }
/*    */   }
/*    */   
/*    */   public String get(String key) {
/* 33 */     Config config = dao.getByKey(key);
/* 34 */     return config.getValue();
/*    */   }
/*    */   
/*    */   public void update(String key, String value) {
/* 38 */     Config config = dao.getByKey(key);
/* 39 */     config.setValue(value);
/* 40 */     dao.update(config);
/*    */   }
/*    */   
/*    */   public int getIntBudget() {
/* 44 */     return Integer.parseInt(get("budget"));
/*    */   }
/*    */ }


/* Location:              E:\java\java学习\hutubill\lib\all.jar!\service\ConfigService.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */
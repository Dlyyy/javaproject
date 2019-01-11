/*    */ package service;
/*    */ 
/*    */ import dao.RecordDAO;
/*    */ import entity.Record;
/*    */ import gui.page.SpendPage;
/*    */ import java.util.List;
/*    */ import util.DateUtil;
/*    */ 
/*    */ public class SpendService
/*    */ {
/*    */   public SpendPage getSpendPage()
/*    */   {
/* 13 */     RecordDAO dao = new RecordDAO();
/*    */     
/* 15 */     List<Record> thisMonthRecords = dao.listThisMonth();
/*    */     
/* 17 */     List<Record> toDayRecords = dao.listToday();
/*    */     
/* 19 */     int thisMonthTotalDay = DateUtil.thisMonthTotalDay();
/*    */     
/* 21 */     int monthSpend = 0;
/* 22 */     int todaySpend = 0;
/* 23 */     int avgSpendPerDay = 0;
/* 24 */     int monthAvailable = 0;
/* 25 */     int dayAvgAvailable = 0;
/* 26 */     int monthLeftDay = 0;
/* 27 */     int usagePercentage = 0;
/*    */     
/*    */ 
/* 30 */     int monthBudget = new ConfigService().getIntBudget();
/*    */     
/*    */ 
/* 33 */     for (Record record : thisMonthRecords) {
/* 34 */       monthSpend += record.getSpend();
/*    */     }
/*    */     
/*    */ 
/* 38 */     for (Record record : toDayRecords) {
/* 39 */       todaySpend += record.getSpend();
/*    */     }
/*    */     
/* 42 */     avgSpendPerDay = monthSpend / thisMonthTotalDay;
/*    */     
/* 44 */     monthAvailable = monthBudget - monthSpend;
/*    */     
/*    */ 
/* 47 */     monthLeftDay = DateUtil.thisMonthLeftDay();
/*    */     
/*    */ 
/* 50 */     dayAvgAvailable = monthAvailable / monthLeftDay;
/*    */     
/*    */ 
/* 53 */     usagePercentage = monthSpend * 100 / monthBudget;
/*    */     
/*    */ 
/*    */ 
/* 57 */     return new SpendPage(monthSpend, todaySpend, avgSpendPerDay, monthAvailable, dayAvgAvailable, monthLeftDay, 
/* 58 */       usagePercentage);
/*    */   }
/*    */ }


/* Location:              E:\java\java学习\hutubill\lib\all.jar!\service\SpendService.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */
/*    */ package service;
/*    */ 
/*    */ import dao.RecordDAO;
/*    */ import entity.Record;
/*    */ import java.util.ArrayList;
/*    */ import java.util.Calendar;
/*    */ import java.util.Date;
/*    */ import java.util.List;
/*    */ import util.DateUtil;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class ReportService
/*    */ {
/*    */   public int getDaySpend(Date d, List<Record> monthRawData)
/*    */   {
/* 21 */     int daySpend = 0;
/* 22 */     for (Record record : monthRawData) {
/* 23 */       if (record.date.equals(d))
/* 24 */         daySpend += record.spend;
/*    */     }
/* 26 */     return daySpend;
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */   public List<Record> listThisMonthRecords()
/*    */   {
/* 34 */     RecordDAO dao = new RecordDAO();
/* 35 */     List<Record> monthRawData = dao.listThisMonth();
/* 36 */     List<Record> result = new ArrayList();
/* 37 */     Date monthBegin = DateUtil.monthBegin();
/* 38 */     int monthTotalDay = DateUtil.thisMonthTotalDay();
/* 39 */     Calendar c = Calendar.getInstance();
/* 40 */     for (int i = 0; i < monthTotalDay; i++) {
/* 41 */       Record r = new Record();
/* 42 */       c.setTime(monthBegin);
/* 43 */       c.add(5, i);
/* 44 */       Date eachDayOfThisMonth = c.getTime();
/* 45 */       int daySpend = getDaySpend(eachDayOfThisMonth, monthRawData);
/* 46 */       r.spend = daySpend;
/* 47 */       result.add(r);
/*    */     }
/* 49 */     return result;
/*    */   }
/*    */ }


/* Location:              E:\java\java学习\hutubill\lib\all.jar!\service\ReportService.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */
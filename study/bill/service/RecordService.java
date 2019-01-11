/*    */ package service;
/*    */ 
/*    */ import dao.RecordDAO;
/*    */ import entity.Category;
/*    */ import entity.Record;
/*    */ import java.util.Date;
/*    */ 
/*    */ public class RecordService
/*    */ {
/* 10 */   RecordDAO recordDao = new RecordDAO();
/*    */   
/* 12 */   public void add(int spend, Category c, String comment, Date date) { Record r = new Record();
/* 13 */     r.spend = spend;
/* 14 */     r.cid = c.id;
/* 15 */     r.comment = comment;
/* 16 */     r.date = date;
/* 17 */     this.recordDao.add(r);
/*    */   }
/*    */ }


/* Location:              E:\java\java学习\hutubill\lib\all.jar!\service\RecordService.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */
/*    */ package entity;
/*    */ 
/*    */ import java.util.Date;
/*    */ 
/*    */ public class Record {
/*    */   public int spend;
/*    */   public int id;
/*    */   public int cid;
/*    */   public String comment;
/*    */   public Date date;
/*    */   
/*    */   public int getId() {
/* 13 */     return this.id;
/*    */   }
/*    */   
/* 16 */   public void setId(int id) { this.id = id; }
/*    */   
/*    */   public int getCid() {
/* 19 */     return this.cid;
/*    */   }
/*    */   
/* 22 */   public void setCid(int cid) { this.cid = cid; }
/*    */   
/*    */   public String getComment() {
/* 25 */     return this.comment;
/*    */   }
/*    */   
/* 28 */   public void setComment(String comment) { this.comment = comment; }
/*    */   
/*    */   public Date getDate() {
/* 31 */     return this.date;
/*    */   }
/*    */   
/* 34 */   public void setDate(Date date) { this.date = date; }
/*    */   
/*    */   public int getSpend() {
/* 37 */     return this.spend;
/*    */   }
/*    */   
/* 40 */   public void setSpend(int spend) { this.spend = spend; }
/*    */ }


/* Location:              E:\java\java学习\hutubill\lib\all.jar!\entity\Record.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */
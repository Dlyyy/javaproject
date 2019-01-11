/*    */ package entity;
/*    */ 
/*    */ public class Category
/*    */ {
/*    */   public int id;
/*    */   public String name;
/*    */   public int recordNumber;
/*    */   
/*    */   public int getRecordNumber() {
/* 10 */     return this.recordNumber;
/*    */   }
/*    */   
/* 13 */   public void setRecordNumber(int recordNumber) { this.recordNumber = recordNumber; }
/*    */   
/*    */   public int getId() {
/* 16 */     return this.id;
/*    */   }
/*    */   
/* 19 */   public void setId(int id) { this.id = id; }
/*    */   
/*    */   public String getName() {
/* 22 */     return this.name;
/*    */   }
/*    */   
/* 25 */   public void setName(String name) { this.name = name; }
/*    */   
/*    */   public String toString()
/*    */   {
/* 29 */     return this.name;
/*    */   }
/*    */ }


/* Location:              E:\java\java学习\hutubill\lib\all.jar!\entity\Category.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */
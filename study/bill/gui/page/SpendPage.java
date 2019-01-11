/*    */ package gui.page;
/*    */ 
/*    */ 
/*    */ public class SpendPage
/*    */ {
/*    */   public String monthSpend;
/*    */   
/*    */   public String todaySpend;
/*    */   
/*    */   public String avgSpendPerDay;
/*    */   
/*    */   public String monthAvailable;
/*    */   
/*    */   public String dayAvgAvailable;
/*    */   
/*    */   public String monthLeftDay;
/*    */   
/*    */   public int usagePercentage;
/* 19 */   public boolean isOverSpend = false;
/*    */   
/*    */   public SpendPage(int monthSpend, int todaySpend, int avgSpendPerDay, int monthAvailable, int dayAvgAvailable, int monthLeftDay, int usagePercentage)
/*    */   {
/* 23 */     this.monthSpend = ("￥" + monthSpend);
/* 24 */     this.todaySpend = ("￥" + todaySpend);
/* 25 */     this.avgSpendPerDay = ("￥" + avgSpendPerDay);
/* 26 */     if (monthAvailable < 0) {
/* 27 */       this.isOverSpend = true;
/*    */     }
/* 29 */     if (!this.isOverSpend) {
/* 30 */       this.monthAvailable = ("￥" + monthAvailable);
/* 31 */       this.dayAvgAvailable = ("￥" + dayAvgAvailable);
/*    */     } else {
/* 33 */       this.monthAvailable = ("超支" + (0 - monthAvailable));
/* 34 */       this.dayAvgAvailable = "￥0";
/*    */     }
/*    */     
/* 37 */     this.monthLeftDay = (monthLeftDay + "天");
/* 38 */     this.usagePercentage = usagePercentage;
/*    */   }
/*    */ }


/* Location:              E:\java\java学习\hutubill\lib\all.jar!\gui\page\SpendPage.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */
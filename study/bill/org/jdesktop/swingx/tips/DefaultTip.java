/*    */ package org.jdesktop.swingx.tips;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class DefaultTip
/*    */   implements TipOfTheDayModel.Tip
/*    */ {
/*    */   private String name;
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   private Object tip;
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   public DefaultTip() {}
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   public DefaultTip(String name, Object tip)
/*    */   {
/* 39 */     this.name = name;
/* 40 */     this.tip = tip;
/*    */   }
/*    */   
/*    */   public Object getTip() {
/* 44 */     return this.tip;
/*    */   }
/*    */   
/*    */   public void setTip(Object tip) {
/* 48 */     this.tip = tip;
/*    */   }
/*    */   
/*    */   public String getTipName() {
/* 52 */     return this.name;
/*    */   }
/*    */   
/*    */   public void setTipName(String name) {
/* 56 */     this.name = name;
/*    */   }
/*    */   
/*    */   public String toString()
/*    */   {
/* 61 */     return getTipName();
/*    */   }
/*    */ }


/* Location:              E:\java\java学习\hutubill\lib\all.jar!\org\jdesktop\swingx\tips\DefaultTip.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */
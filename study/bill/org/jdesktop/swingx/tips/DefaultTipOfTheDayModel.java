/*    */ package org.jdesktop.swingx.tips;
/*    */ 
/*    */ import java.util.ArrayList;
/*    */ import java.util.Arrays;
/*    */ import java.util.Collection;
/*    */ import java.util.List;
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
/*    */ public class DefaultTipOfTheDayModel
/*    */   implements TipOfTheDayModel
/*    */ {
/* 35 */   private List<TipOfTheDayModel.Tip> tips = new ArrayList();
/*    */   
/*    */   public DefaultTipOfTheDayModel() {}
/*    */   
/*    */   public DefaultTipOfTheDayModel(TipOfTheDayModel.Tip[] tips)
/*    */   {
/* 41 */     this(Arrays.asList(tips));
/*    */   }
/*    */   
/*    */   public DefaultTipOfTheDayModel(Collection<TipOfTheDayModel.Tip> tips) {
/* 45 */     this.tips.addAll(tips);
/*    */   }
/*    */   
/*    */   public TipOfTheDayModel.Tip getTipAt(int index) {
/* 49 */     return (TipOfTheDayModel.Tip)this.tips.get(index);
/*    */   }
/*    */   
/*    */   public int getTipCount() {
/* 53 */     return this.tips.size();
/*    */   }
/*    */   
/*    */   public void add(TipOfTheDayModel.Tip tip) {
/* 57 */     this.tips.add(tip);
/*    */   }
/*    */   
/*    */   public void remove(TipOfTheDayModel.Tip tip) {
/* 61 */     this.tips.remove(tip);
/*    */   }
/*    */   
/*    */   public TipOfTheDayModel.Tip[] getTips() {
/* 65 */     return (TipOfTheDayModel.Tip[])this.tips.toArray(new TipOfTheDayModel.Tip[this.tips.size()]);
/*    */   }
/*    */   
/*    */   public void setTips(TipOfTheDayModel.Tip[] tips) {
/* 69 */     this.tips.clear();
/* 70 */     this.tips.addAll(Arrays.asList(tips));
/*    */   }
/*    */ }


/* Location:              E:\java\java学习\hutubill\lib\all.jar!\org\jdesktop\swingx\tips\DefaultTipOfTheDayModel.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */
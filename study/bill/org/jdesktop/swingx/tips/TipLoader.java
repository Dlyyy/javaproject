/*    */ package org.jdesktop.swingx.tips;
/*    */ 
/*    */ import java.util.ArrayList;
/*    */ import java.util.List;
/*    */ import java.util.Properties;
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
/*    */ public class TipLoader
/*    */ {
/*    */   public static TipOfTheDayModel load(Properties props)
/*    */   {
/* 60 */     List<TipOfTheDayModel.Tip> tips = new ArrayList();
/*    */     
/* 62 */     int count = 1;
/*    */     for (;;) {
/* 64 */       String nameKey = "tip." + count + ".name";
/* 65 */       String nameValue = props.getProperty(nameKey);
/*    */       
/* 67 */       String descriptionKey = "tip." + count + ".description";
/* 68 */       String descriptionValue = props.getProperty(descriptionKey);
/*    */       
/* 70 */       if ((nameValue != null) && (descriptionValue == null)) { throw new IllegalArgumentException("No description for name " + nameValue);
/*    */       }
/*    */       
/* 73 */       if (descriptionValue == null) {
/*    */         break;
/*    */       }
/*    */       
/* 77 */       DefaultTip tip = new DefaultTip(nameValue, descriptionValue);
/* 78 */       tips.add(tip);
/*    */       
/* 80 */       count++;
/*    */     }
/*    */     
/* 83 */     return new DefaultTipOfTheDayModel(tips);
/*    */   }
/*    */ }


/* Location:              E:\java\java学习\hutubill\lib\all.jar!\org\jdesktop\swingx\tips\TipLoader.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */
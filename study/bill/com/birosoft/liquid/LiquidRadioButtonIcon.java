/*    */ package com.birosoft.liquid;
/*    */ 
/*    */ import com.birosoft.liquid.skin.Skin;
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
/*    */ class LiquidRadioButtonIcon
/*    */   extends LiquidCheckBoxIcon
/*    */ {
/*    */   static Skin skin;
/*    */   
/*    */   public Skin getSkin()
/*    */   {
/* 23 */     if (skin == null)
/* 24 */       skin = new Skin("radiobutton.png", 8, 0);
/* 25 */     return skin;
/*    */   }
/*    */ }


/* Location:              E:\java\java学习\hutubill\lib\all.jar!\com\birosoft\liquid\LiquidRadioButtonIcon.class
 * Java compiler version: 4 (48.0)
 * JD-Core Version:       0.7.1
 */
/*    */ package com.birosoft.liquid;
/*    */ 
/*    */ import javax.swing.JComponent;
/*    */ import javax.swing.plaf.ComponentUI;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class LiquidCheckBoxMenuItemUI
/*    */   extends LiquidMenuItemUI
/*    */ {
/*    */   public static ComponentUI createUI(JComponent c)
/*    */   {
/* 19 */     return new LiquidCheckBoxMenuItemUI();
/*    */   }
/*    */   
/*    */   protected String getPropertyPrefix() {
/* 23 */     return "CheckBoxMenuItem";
/*    */   }
/*    */ }


/* Location:              E:\java\java学习\hutubill\lib\all.jar!\com\birosoft\liquid\LiquidCheckBoxMenuItemUI.class
 * Java compiler version: 4 (48.0)
 * JD-Core Version:       0.7.1
 */
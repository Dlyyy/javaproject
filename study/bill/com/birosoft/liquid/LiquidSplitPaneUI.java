/*    */ package com.birosoft.liquid;
/*    */ 
/*    */ import javax.swing.JComponent;
/*    */ import javax.swing.plaf.ComponentUI;
/*    */ import javax.swing.plaf.basic.BasicSplitPaneDivider;
/*    */ import javax.swing.plaf.basic.BasicSplitPaneUI;
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
/*    */ public class LiquidSplitPaneUI
/*    */   extends BasicSplitPaneUI
/*    */ {
/*    */   public static ComponentUI createUI(JComponent x)
/*    */   {
/* 25 */     return new LiquidSplitPaneUI();
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */   public BasicSplitPaneDivider createDefaultDivider()
/*    */   {
/* 33 */     return new LiquidSplitPaneDivider(this);
/*    */   }
/*    */ }


/* Location:              E:\java\java学习\hutubill\lib\all.jar!\com\birosoft\liquid\LiquidSplitPaneUI.class
 * Java compiler version: 4 (48.0)
 * JD-Core Version:       0.7.1
 */
/*    */ package org.jdesktop.swingx.plaf;
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
/*    */ public class MultiThumbSliderAddon
/*    */   extends AbstractComponentAddon
/*    */ {
/*    */   public MultiThumbSliderAddon()
/*    */   {
/* 33 */     super("JXMultiThumbSlider");
/*    */   }
/*    */   
/*    */   protected void addBasicDefaults(LookAndFeelAddons addon, DefaultsList defaults)
/*    */   {
/* 38 */     super.addBasicDefaults(addon, defaults);
/*    */     
/* 40 */     defaults.add("MultiThumbSliderUI", "org.jdesktop.swingx.plaf.basic.BasicMultiThumbSliderUI");
/*    */   }
/*    */ }


/* Location:              E:\java\java学习\hutubill\lib\all.jar!\org\jdesktop\swingx\plaf\MultiThumbSliderAddon.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */
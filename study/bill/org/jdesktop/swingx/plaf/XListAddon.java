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
/*    */ 
/*    */ 
/*    */ public class XListAddon
/*    */   extends AbstractComponentAddon
/*    */ {
/*    */   public XListAddon()
/*    */   {
/* 35 */     super("JXList");
/*    */   }
/*    */   
/*    */ 
/*    */   protected void addBasicDefaults(LookAndFeelAddons addon, DefaultsList defaults)
/*    */   {
/* 41 */     defaults.add("XListUI", "org.jdesktop.swingx.plaf.basic.core.BasicXListUI");
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */   protected void addNimbusDefaults(LookAndFeelAddons addon, DefaultsList defaults)
/*    */   {
/* 48 */     defaults.add("XListUI", "org.jdesktop.swingx.plaf.synth.SynthXListUI");
/*    */   }
/*    */ }


/* Location:              E:\java\java学习\hutubill\lib\all.jar!\org\jdesktop\swingx\plaf\XListAddon.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */
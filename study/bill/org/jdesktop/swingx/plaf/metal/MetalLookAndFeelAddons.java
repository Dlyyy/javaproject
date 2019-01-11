/*    */ package org.jdesktop.swingx.plaf.metal;
/*    */ 
/*    */ import org.jdesktop.swingx.plaf.basic.BasicLookAndFeelAddons;
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
/*    */ public class MetalLookAndFeelAddons
/*    */   extends BasicLookAndFeelAddons
/*    */ {
/*    */   public void initialize()
/*    */   {
/* 33 */     super.initialize();
/* 34 */     loadDefaults(getDefaults());
/*    */   }
/*    */   
/*    */   public void uninitialize()
/*    */   {
/* 39 */     super.uninitialize();
/* 40 */     unloadDefaults(getDefaults());
/*    */   }
/*    */   
/*    */   private Object[] getDefaults() {
/* 44 */     return new Object[0];
/*    */   }
/*    */ }


/* Location:              E:\java\java学习\hutubill\lib\all.jar!\org\jdesktop\swingx\plaf\metal\MetalLookAndFeelAddons.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */
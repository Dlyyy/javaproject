/*    */ package org.jdesktop.swingx.plaf.basic;
/*    */ 
/*    */ import org.jdesktop.swingx.plaf.LookAndFeelAddons;
/*    */ import org.jdesktop.swingx.plaf.UIManagerExt;
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
/*    */ public class BasicLookAndFeelAddons
/*    */   extends LookAndFeelAddons
/*    */ {
/*    */   public void initialize()
/*    */   {
/* 37 */     super.initialize();
/*    */     
/* 39 */     UIManagerExt.addResourceBundle("org.jdesktop.swingx.plaf.basic.resources.swingx");
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   public void uninitialize()
/*    */   {
/* 49 */     UIManagerExt.removeResourceBundle("org.jdesktop.swingx.plaf.basic.resources.swingx");
/*    */     
/* 51 */     super.uninitialize();
/*    */   }
/*    */ }


/* Location:              E:\java\java学习\hutubill\lib\all.jar!\org\jdesktop\swingx\plaf\basic\BasicLookAndFeelAddons.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */
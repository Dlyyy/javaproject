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
/*    */ public class ErrorPaneAddon
/*    */   extends AbstractComponentAddon
/*    */ {
/*    */   public ErrorPaneAddon()
/*    */   {
/* 34 */     super("JXErrorPane");
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */   protected void addBasicDefaults(LookAndFeelAddons addon, DefaultsList defaults)
/*    */   {
/* 42 */     super.addBasicDefaults(addon, defaults);
/*    */     
/* 44 */     defaults.add("ErrorPaneUI", "org.jdesktop.swingx.plaf.basic.BasicErrorPaneUI");
/*    */     
/* 46 */     UIManagerExt.addResourceBundle("org.jdesktop.swingx.plaf.basic.resources.ErrorPane");
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   protected void addMacDefaults(LookAndFeelAddons addon, DefaultsList defaults)
/*    */   {
/* 55 */     super.addMacDefaults(addon, defaults);
/*    */     
/* 57 */     defaults.add("ErrorPaneUI", "org.jdesktop.swingx.plaf.macosx.MacOSXErrorPaneUI");
/*    */     
/* 59 */     UIManagerExt.addResourceBundle("org.jdesktop.swingx.plaf.macosx.resources.ErrorPane");
/*    */   }
/*    */ }


/* Location:              E:\java\java学习\hutubill\lib\all.jar!\org\jdesktop\swingx\plaf\ErrorPaneAddon.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */
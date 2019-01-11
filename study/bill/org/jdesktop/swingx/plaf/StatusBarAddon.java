/*    */ package org.jdesktop.swingx.plaf;
/*    */ 
/*    */ import org.jdesktop.swingx.util.OS;
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
/*    */ public class StatusBarAddon
/*    */   extends AbstractComponentAddon
/*    */ {
/*    */   public StatusBarAddon()
/*    */   {
/* 34 */     super("JXStatusBar");
/*    */   }
/*    */   
/*    */   protected void addBasicDefaults(LookAndFeelAddons addon, DefaultsList defaults)
/*    */   {
/* 39 */     super.addBasicDefaults(addon, defaults);
/*    */     
/* 41 */     defaults.add("StatusBarUI", "org.jdesktop.swingx.plaf.basic.BasicStatusBarUI");
/*    */   }
/*    */   
/*    */ 
/*    */   protected void addMacDefaults(LookAndFeelAddons addon, DefaultsList defaults)
/*    */   {
/* 47 */     super.addMacDefaults(addon, defaults);
/*    */     
/* 49 */     defaults.add("StatusBarUI", "org.jdesktop.swingx.plaf.macosx.MacOSXStatusBarUI");
/*    */   }
/*    */   
/*    */ 
/*    */   protected void addMetalDefaults(LookAndFeelAddons addon, DefaultsList defaults)
/*    */   {
/* 55 */     super.addMetalDefaults(addon, defaults);
/*    */     
/* 57 */     defaults.add("StatusBarUI", "org.jdesktop.swingx.plaf.metal.MetalStatusBarUI");
/*    */   }
/*    */   
/*    */ 
/*    */   protected void addWindowsDefaults(LookAndFeelAddons addon, DefaultsList defaults)
/*    */   {
/* 63 */     super.addWindowsDefaults(addon, defaults);
/* 64 */     if (OS.isUsingWindowsVisualStyles()) {
/* 65 */       defaults.add("StatusBarUI", "org.jdesktop.swingx.plaf.windows.WindowsStatusBarUI");
/*    */       
/*    */ 
/* 68 */       String xpStyle = OS.getWindowsVisualStyle();
/*    */       
/* 70 */       if (("Metallic".equalsIgnoreCase(xpStyle)) || ("NormalColor".equalsIgnoreCase(xpStyle)))
/*    */       {
/* 72 */         defaults.add("StatusBar.leftImage", "resources/silver-statusbar-left.png");
/* 73 */         defaults.add("StatusBar.middleImage", "resources/silver-statusbar-middle.png");
/* 74 */         defaults.add("StatusBar.rightImage", "resources/silver-statusbar-right.png");
/*    */       } else {
/* 76 */         defaults.add("StatusBar.leftImage", "resources/statusbar-left.png");
/* 77 */         defaults.add("StatusBar.middleImage", "resources/statusbar-middle.png");
/* 78 */         defaults.add("StatusBar.rightImage", "resources/statusbar-right.png");
/*    */       }
/*    */     } else {
/* 81 */       defaults.add("StatusBarUI", "org.jdesktop.swingx.plaf.windows.WindowsClassicStatusBarUI");
/*    */     }
/*    */   }
/*    */ }


/* Location:              E:\java\java学习\hutubill\lib\all.jar!\org\jdesktop\swingx\plaf\StatusBarAddon.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */
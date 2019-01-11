/*     */ package org.jdesktop.swingx.plaf;
/*     */ 
/*     */ import javax.swing.UIManager;
/*     */ import org.jdesktop.swingx.plaf.linux.LinuxLookAndFeelAddons;
/*     */ import org.jdesktop.swingx.plaf.macosx.MacOSXLookAndFeelAddons;
/*     */ import org.jdesktop.swingx.plaf.metal.MetalLookAndFeelAddons;
/*     */ import org.jdesktop.swingx.plaf.motif.MotifLookAndFeelAddons;
/*     */ import org.jdesktop.swingx.plaf.nimbus.NimbusLookAndFeelAddons;
/*     */ import org.jdesktop.swingx.plaf.windows.WindowsLookAndFeelAddons;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public abstract class AbstractComponentAddon
/*     */   implements ComponentAddon
/*     */ {
/*     */   private String name;
/*     */   
/*     */   protected AbstractComponentAddon(String name)
/*     */   {
/*  43 */     this.name = name;
/*     */   }
/*     */   
/*     */   public final String getName() {
/*  47 */     return this.name;
/*     */   }
/*     */   
/*     */   public void initialize(LookAndFeelAddons addon) {
/*  51 */     addon.loadDefaults(getDefaults(addon));
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void uninitialize(LookAndFeelAddons addon) {}
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected void addBasicDefaults(LookAndFeelAddons addon, DefaultsList defaults) {}
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected void addMacDefaults(LookAndFeelAddons addon, DefaultsList defaults)
/*     */   {
/*  76 */     addBasicDefaults(addon, defaults);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected void addMetalDefaults(LookAndFeelAddons addon, DefaultsList defaults)
/*     */   {
/*  86 */     addBasicDefaults(addon, defaults);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected void addMotifDefaults(LookAndFeelAddons addon, DefaultsList defaults)
/*     */   {
/*  96 */     addBasicDefaults(addon, defaults);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected void addWindowsDefaults(LookAndFeelAddons addon, DefaultsList defaults)
/*     */   {
/* 106 */     addBasicDefaults(addon, defaults);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected void addLinuxDefaults(LookAndFeelAddons addon, DefaultsList defaults)
/*     */   {
/* 116 */     addBasicDefaults(addon, defaults);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected void addNimbusDefaults(LookAndFeelAddons addon, DefaultsList defaults)
/*     */   {
/* 126 */     addBasicDefaults(addon, defaults);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private Object[] getDefaults(LookAndFeelAddons addon)
/*     */   {
/* 153 */     DefaultsList defaults = new DefaultsList();
/* 154 */     if (isWindows(addon)) {
/* 155 */       addWindowsDefaults(addon, defaults);
/* 156 */     } else if (isMetal(addon)) {
/* 157 */       addMetalDefaults(addon, defaults);
/* 158 */     } else if (isMac(addon)) {
/* 159 */       addMacDefaults(addon, defaults);
/* 160 */     } else if (isMotif(addon)) {
/* 161 */       addMotifDefaults(addon, defaults);
/* 162 */     } else if (isLinux(addon)) {
/* 163 */       addLinuxDefaults(addon, defaults);
/* 164 */     } else if (isNimbus(addon)) {
/* 165 */       addNimbusDefaults(addon, defaults);
/*     */     }
/*     */     else {
/* 168 */       addBasicDefaults(addon, defaults);
/*     */     }
/* 170 */     return defaults.toArray();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected boolean isWindows(LookAndFeelAddons addon)
/*     */   {
/* 181 */     return addon instanceof WindowsLookAndFeelAddons;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   protected boolean isMetal(LookAndFeelAddons addon)
/*     */   {
/* 188 */     return addon instanceof MetalLookAndFeelAddons;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   protected boolean isMac(LookAndFeelAddons addon)
/*     */   {
/* 195 */     return addon instanceof MacOSXLookAndFeelAddons;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   protected boolean isMotif(LookAndFeelAddons addon)
/*     */   {
/* 202 */     return addon instanceof MotifLookAndFeelAddons;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   protected boolean isLinux(LookAndFeelAddons addon)
/*     */   {
/* 209 */     return addon instanceof LinuxLookAndFeelAddons;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   protected boolean isNimbus(LookAndFeelAddons addon)
/*     */   {
/* 216 */     return addon instanceof NimbusLookAndFeelAddons;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   protected boolean isPlastic()
/*     */   {
/* 223 */     return UIManager.getLookAndFeel().getClass().getName().contains("Plastic");
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   protected boolean isSynth()
/*     */   {
/* 230 */     return UIManager.getLookAndFeel().getClass().getName().contains("ynth");
/*     */   }
/*     */ }


/* Location:              E:\java\java学习\hutubill\lib\all.jar!\org\jdesktop\swingx\plaf\AbstractComponentAddon.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */
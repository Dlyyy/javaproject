/*     */ package org.jdesktop.swingx.plaf;
/*     */ 
/*     */ import java.awt.Color;
/*     */ import java.awt.Font;
/*     */ import java.awt.SystemColor;
/*     */ import java.awt.Toolkit;
/*     */ import javax.swing.UIDefaults.LazyInputMap;
/*     */ import javax.swing.UIManager;
/*     */ import javax.swing.plaf.ColorUIResource;
/*     */ import javax.swing.plaf.FontUIResource;
/*     */ import javax.swing.plaf.metal.MetalLookAndFeel;
/*     */ import javax.swing.plaf.metal.OceanTheme;
/*     */ import org.jdesktop.swingx.plaf.windows.WindowsClassicLookAndFeelAddons;
/*     */ import org.jdesktop.swingx.plaf.windows.WindowsLookAndFeelAddons;
/*     */ import org.jdesktop.swingx.util.OS;
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
/*     */ 
/*     */ 
/*     */ public class TaskPaneAddon
/*     */   extends AbstractComponentAddon
/*     */ {
/*     */   public TaskPaneAddon()
/*     */   {
/*  49 */     super("JXTaskPane");
/*     */   }
/*     */   
/*     */   protected void addBasicDefaults(LookAndFeelAddons addon, DefaultsList defaults)
/*     */   {
/*  54 */     Font taskPaneFont = UIManagerExt.getSafeFont("Label.font", new Font("Dialog", 0, 12));
/*     */     
/*  56 */     taskPaneFont = taskPaneFont.deriveFont(1);
/*     */     
/*  58 */     Color menuBackground = new ColorUIResource(SystemColor.menu);
/*     */     
/*  60 */     defaults.add("swingx/TaskPaneUI", "org.jdesktop.swingx.plaf.basic.BasicTaskPaneUI");
/*  61 */     defaults.add("TaskPane.font", new FontUIResource(taskPaneFont));
/*  62 */     defaults.add("TaskPane.background", UIManagerExt.getSafeColor("List.background", new ColorUIResource(Color.decode("#005C5C"))));
/*     */     
/*  64 */     defaults.add("TaskPane.specialTitleBackground", new ColorUIResource(menuBackground.darker()));
/*  65 */     defaults.add("TaskPane.titleBackgroundGradientStart", menuBackground);
/*  66 */     defaults.add("TaskPane.titleBackgroundGradientEnd", menuBackground);
/*  67 */     defaults.add("TaskPane.titleForeground", new ColorUIResource(SystemColor.menuText));
/*  68 */     defaults.add("TaskPane.specialTitleForeground", new ColorUIResource(SystemColor.menuText.brighter()));
/*  69 */     defaults.add("TaskPane.animate", Boolean.TRUE);
/*  70 */     defaults.add("TaskPane.focusInputMap", new UIDefaults.LazyInputMap(new Object[] { "ENTER", "toggleCollapsed", "SPACE", "toggleCollapsed" }));
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   protected void addLinuxDefaults(LookAndFeelAddons addon, DefaultsList defaults)
/*     */   {
/*  77 */     addMetalDefaults(addon, defaults);
/*     */   }
/*     */   
/*     */   protected void addMetalDefaults(LookAndFeelAddons addon, DefaultsList defaults)
/*     */   {
/*  82 */     super.addMetalDefaults(addon, defaults);
/*     */     
/*  84 */     if ((MetalLookAndFeel.getCurrentTheme() instanceof OceanTheme)) {
/*  85 */       defaults.add("swingx/TaskPaneUI", "org.jdesktop.swingx.plaf.misc.GlossyTaskPaneUI");
/*     */     } else {
/*  87 */       defaults.add("swingx/TaskPaneUI", "org.jdesktop.swingx.plaf.metal.MetalTaskPaneUI");
/*     */     }
/*     */     
/*     */ 
/*  91 */     defaults.add("TaskPane.foreground", UIManager.getColor("activeCaptionText"));
/*  92 */     defaults.add("TaskPane.background", MetalLookAndFeel.getControl());
/*  93 */     defaults.add("TaskPane.specialTitleBackground", MetalLookAndFeel.getPrimaryControl());
/*  94 */     defaults.add("TaskPane.titleBackgroundGradientStart", MetalLookAndFeel.getPrimaryControl());
/*  95 */     defaults.add("TaskPane.titleBackgroundGradientEnd", MetalLookAndFeel.getPrimaryControlHighlight());
/*  96 */     defaults.add("TaskPane.titleForeground", MetalLookAndFeel.getControlTextColor());
/*  97 */     defaults.add("TaskPane.specialTitleForeground", MetalLookAndFeel.getControlTextColor());
/*  98 */     defaults.add("TaskPane.borderColor", MetalLookAndFeel.getPrimaryControl());
/*  99 */     defaults.add("TaskPane.titleOver", new ColorUIResource(MetalLookAndFeel.getControl().darker()));
/* 100 */     defaults.add("TaskPane.specialTitleOver", MetalLookAndFeel.getPrimaryControlHighlight());
/*     */   }
/*     */   
/*     */   protected void addWindowsDefaults(LookAndFeelAddons addon, DefaultsList defaults)
/*     */   {
/* 105 */     super.addWindowsDefaults(addon, defaults);
/*     */     
/* 107 */     if ((addon instanceof WindowsLookAndFeelAddons)) {
/* 108 */       defaults.add("swingx/TaskPaneUI", "org.jdesktop.swingx.plaf.windows.WindowsTaskPaneUI");
/*     */       
/* 110 */       String xpStyle = OS.getWindowsVisualStyle();
/* 111 */       if ("HomeStead".equalsIgnoreCase(xpStyle))
/*     */       {
/* 113 */         defaults.add("TaskPane.foreground", new ColorUIResource(86, 102, 45));
/* 114 */         defaults.add("TaskPane.background", new ColorUIResource(246, 246, 236));
/* 115 */         defaults.add("TaskPane.specialTitleBackground", new ColorUIResource(224, 231, 184));
/* 116 */         defaults.add("TaskPane.titleBackgroundGradientStart", new ColorUIResource(Color.WHITE));
/* 117 */         defaults.add("TaskPane.titleBackgroundGradientEnd", new ColorUIResource(224, 231, 184));
/* 118 */         defaults.add("TaskPane.titleForeground", new ColorUIResource(86, 102, 45));
/* 119 */         defaults.add("TaskPane.titleOver", new ColorUIResource(114, 146, 29));
/* 120 */         defaults.add("TaskPane.specialTitleForeground", new ColorUIResource(86, 102, 45));
/* 121 */         defaults.add("TaskPane.specialTitleOver", new ColorUIResource(114, 146, 29));
/* 122 */         defaults.add("TaskPane.borderColor", new ColorUIResource(Color.WHITE));
/* 123 */       } else if ("Metallic".equalsIgnoreCase(xpStyle))
/*     */       {
/* 125 */         defaults.add("TaskPane.foreground", new ColorUIResource(Color.BLACK));
/* 126 */         defaults.add("TaskPane.background", new ColorUIResource(240, 241, 245));
/* 127 */         defaults.add("TaskPane.specialTitleBackground", new ColorUIResource(222, 222, 222));
/* 128 */         defaults.add("TaskPane.titleBackgroundGradientStart", new ColorUIResource(Color.WHITE));
/* 129 */         defaults.add("TaskPane.titleBackgroundGradientEnd", new ColorUIResource(214, 215, 224));
/* 130 */         defaults.add("TaskPane.titleForeground", new ColorUIResource(Color.BLACK));
/* 131 */         defaults.add("TaskPane.titleOver", new ColorUIResource(126, 124, 124));
/* 132 */         defaults.add("TaskPane.specialTitleForeground", new ColorUIResource(Color.BLACK));
/* 133 */         defaults.add("TaskPane.specialTitleOver", new ColorUIResource(126, 124, 124));
/* 134 */         defaults.add("TaskPane.borderColor", new ColorUIResource(Color.WHITE));
/* 135 */       } else if (OS.isWindowsVista())
/*     */       {
/* 137 */         Toolkit toolkit = Toolkit.getDefaultToolkit();
/*     */         
/* 139 */         defaults.add("TaskPane.foreground", new ColorUIResource(Color.WHITE));
/* 140 */         defaults.add("TaskPane.background", new ColorUIResource((Color)toolkit.getDesktopProperty("win.3d.backgroundColor")));
/*     */         
/* 142 */         defaults.add("TaskPane.specialTitleBackground", new ColorUIResource(33, 89, 201));
/* 143 */         defaults.add("TaskPane.titleBackgroundGradientStart", new ColorUIResource(Color.WHITE));
/* 144 */         defaults.add("TaskPane.titleBackgroundGradientEnd", new ColorUIResource((Color)toolkit.getDesktopProperty("win.frame.inactiveCaptionColor")));
/*     */         
/* 146 */         defaults.add("TaskPane.titleForeground", new ColorUIResource((Color)toolkit.getDesktopProperty("win.frame.inactiveCaptionTextColor")));
/*     */         
/* 148 */         defaults.add("TaskPane.specialTitleForeground", new ColorUIResource(Color.WHITE));
/* 149 */         defaults.add("TaskPane.borderColor", new ColorUIResource(Color.WHITE));
/*     */       } else {
/* 151 */         defaults.add("TaskPane.foreground", new ColorUIResource(Color.WHITE));
/* 152 */         defaults.add("TaskPane.background", new ColorUIResource(214, 223, 247));
/* 153 */         defaults.add("TaskPane.specialTitleBackground", new ColorUIResource(33, 89, 201));
/* 154 */         defaults.add("TaskPane.titleBackgroundGradientStart", new ColorUIResource(Color.WHITE));
/* 155 */         defaults.add("TaskPane.titleBackgroundGradientEnd", new ColorUIResource(199, 212, 247));
/* 156 */         defaults.add("TaskPane.titleForeground", new ColorUIResource(33, 89, 201));
/* 157 */         defaults.add("TaskPane.specialTitleForeground", new ColorUIResource(Color.WHITE));
/* 158 */         defaults.add("TaskPane.borderColor", new ColorUIResource(Color.WHITE));
/*     */       }
/*     */     }
/*     */     
/* 162 */     if ((addon instanceof WindowsClassicLookAndFeelAddons)) {
/* 163 */       defaults.add("swingx/TaskPaneUI", "org.jdesktop.swingx.plaf.windows.WindowsClassicTaskPaneUI");
/* 164 */       defaults.add("TaskPane.foreground", new ColorUIResource(Color.BLACK));
/* 165 */       defaults.add("TaskPane.background", new ColorUIResource(Color.WHITE));
/* 166 */       defaults.add("TaskPane.specialTitleBackground", new ColorUIResource(10, 36, 106));
/* 167 */       defaults.add("TaskPane.titleBackgroundGradientStart", new ColorUIResource(212, 208, 200));
/* 168 */       defaults.add("TaskPane.titleBackgroundGradientEnd", new ColorUIResource(212, 208, 200));
/* 169 */       defaults.add("TaskPane.titleForeground", new ColorUIResource(Color.BLACK));
/* 170 */       defaults.add("TaskPane.specialTitleForeground", new ColorUIResource(Color.WHITE));
/* 171 */       defaults.add("TaskPane.borderColor", new ColorUIResource(212, 208, 200));
/*     */     }
/*     */   }
/*     */   
/*     */   protected void addMacDefaults(LookAndFeelAddons addon, DefaultsList defaults)
/*     */   {
/* 177 */     super.addMacDefaults(addon, defaults);
/*     */     
/* 179 */     defaults.add("swingx/TaskPaneUI", "org.jdesktop.swingx.plaf.misc.GlossyTaskPaneUI");
/* 180 */     defaults.add("TaskPane.background", new ColorUIResource(245, 245, 245));
/* 181 */     defaults.add("TaskPane.titleForeground", new ColorUIResource(Color.BLACK));
/* 182 */     defaults.add("TaskPane.specialTitleBackground", new ColorUIResource(188, 188, 188));
/* 183 */     defaults.add("TaskPane.specialTitleForeground", new ColorUIResource(Color.BLACK));
/* 184 */     defaults.add("TaskPane.titleBackgroundGradientStart", new ColorUIResource(250, 250, 250));
/* 185 */     defaults.add("TaskPane.titleBackgroundGradientEnd", new ColorUIResource(188, 188, 188));
/* 186 */     defaults.add("TaskPane.borderColor", new ColorUIResource(97, 97, 97));
/* 187 */     defaults.add("TaskPane.titleOver", new ColorUIResource(125, 125, 97));
/* 188 */     defaults.add("TaskPane.specialTitleOver", new ColorUIResource(125, 125, 97));
/*     */   }
/*     */   
/*     */ 
/*     */   protected void addNimbusDefaults(LookAndFeelAddons addon, DefaultsList defaults)
/*     */   {
/* 194 */     super.addNimbusDefaults(addon, defaults);
/*     */     
/* 196 */     defaults.add("swingx/TaskPaneUI", "org.jdesktop.swingx.plaf.nimbus.NimbusTaskPaneUI");
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 203 */     defaults.add("TaskPane.foreground", new ColorUIResource(186, 190, 198));
/*     */     
/*     */ 
/* 206 */     defaults.add("TaskPane.background", new ColorUIResource(214, 217, 223));
/*     */     
/*     */ 
/* 209 */     defaults.add("TaskPane.specialTitleBackground", new ColorUIResource(169, 176, 190));
/*     */     
/*     */ 
/*     */ 
/* 213 */     defaults.add("TaskPane.titleBackgroundGradientStart", new ColorUIResource(214, 217, 223));
/*     */     
/*     */ 
/*     */ 
/* 217 */     defaults.add("TaskPane.titleBackgroundGradientEnd", new ColorUIResource(247, 248, 250));
/*     */     
/* 219 */     defaults.add("TaskPane.titleForeground", new ColorUIResource(Color.BLACK));
/*     */     
/* 221 */     defaults.add("TaskPane.specialTitleForeground", new ColorUIResource(Color.BLACK));
/*     */     
/*     */ 
/*     */ 
/* 225 */     defaults.add("TaskPane.borderColor", new ColorUIResource(146, 151, 161));
/*     */     
/*     */ 
/*     */ 
/* 229 */     defaults.add("TaskPane.titleOver", new ColorUIResource(57, 105, 138));
/*     */     
/*     */ 
/* 232 */     defaults.add("TaskPane.specialTitleOver", new ColorUIResource(57, 105, 138));
/*     */   }
/*     */ }


/* Location:              E:\java\java学习\hutubill\lib\all.jar!\org\jdesktop\swingx\plaf\TaskPaneAddon.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */
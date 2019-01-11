/*     */ package org.jdesktop.swingx.plaf;
/*     */ 
/*     */ import java.awt.Color;
/*     */ import java.awt.GradientPaint;
/*     */ import java.awt.Toolkit;
/*     */ import javax.swing.BorderFactory;
/*     */ import javax.swing.plaf.BorderUIResource;
/*     */ import javax.swing.plaf.ColorUIResource;
/*     */ import javax.swing.plaf.metal.MetalLookAndFeel;
/*     */ import org.jdesktop.swingx.painter.MattePainter;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class TaskPaneContainerAddon
/*     */   extends AbstractComponentAddon
/*     */ {
/*     */   public TaskPaneContainerAddon()
/*     */   {
/*  54 */     super("JXTaskPaneContainer");
/*     */   }
/*     */   
/*     */   protected void addBasicDefaults(LookAndFeelAddons addon, DefaultsList defaults)
/*     */   {
/*  59 */     super.addBasicDefaults(addon, defaults);
/*     */     
/*  61 */     defaults.add("swingx/TaskPaneContainerUI", "org.jdesktop.swingx.plaf.basic.BasicTaskPaneContainerUI");
/*  62 */     defaults.add("TaskPaneContainer.background", UIManagerExt.getSafeColor("Desktop.background", new ColorUIResource(Color.decode("#005C5C"))));
/*     */     
/*  64 */     defaults.add("TaskPaneContainer.border", new BorderUIResource(BorderFactory.createEmptyBorder(10, 10, 0, 10)));
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   protected void addMetalDefaults(LookAndFeelAddons addon, DefaultsList defaults)
/*     */   {
/*  72 */     super.addMetalDefaults(addon, defaults);
/*     */     
/*  74 */     defaults.add("TaskPaneContainer.background", MetalLookAndFeel.getDesktopColor());
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   protected void addWindowsDefaults(LookAndFeelAddons addon, DefaultsList defaults)
/*     */   {
/*  82 */     super.addWindowsDefaults(addon, defaults);
/*  83 */     if ((addon instanceof WindowsClassicLookAndFeelAddons)) {
/*  84 */       defaults.add("TaskPaneContainer.background", UIManagerExt.getSafeColor("List.background", new ColorUIResource(Color.decode("#005C5C"))));
/*     */     }
/*  86 */     else if ((addon instanceof WindowsLookAndFeelAddons)) {
/*  87 */       String xpStyle = OS.getWindowsVisualStyle();
/*     */       Color backgroundGradientEnd;
/*     */       ColorUIResource background;
/*     */       Color backgroundGradientStart;
/*     */       Color backgroundGradientEnd;
/*  92 */       if ("HomeStead".equalsIgnoreCase(xpStyle))
/*     */       {
/*  94 */         ColorUIResource background = new ColorUIResource(201, 215, 170);
/*  95 */         Color backgroundGradientStart = new Color(204, 217, 173);
/*  96 */         backgroundGradientEnd = new Color(165, 189, 132); } else { Color backgroundGradientEnd;
/*  97 */         if ("Metallic".equalsIgnoreCase(xpStyle))
/*     */         {
/*  99 */           ColorUIResource background = new ColorUIResource(192, 195, 209);
/* 100 */           Color backgroundGradientStart = new Color(196, 200, 212);
/* 101 */           backgroundGradientEnd = new Color(177, 179, 200);
/*     */         } else { Color backgroundGradientEnd;
/* 103 */           if (OS.isWindowsVista()) {
/* 104 */             Toolkit toolkit = Toolkit.getDefaultToolkit();
/* 105 */             ColorUIResource background = new ColorUIResource((Color)toolkit.getDesktopProperty("win.3d.backgroundColor"));
/* 106 */             Color backgroundGradientStart = (Color)toolkit.getDesktopProperty("win.frame.activeCaptionColor");
/* 107 */             backgroundGradientEnd = (Color)toolkit.getDesktopProperty("win.frame.inactiveCaptionColor");
/*     */           } else {
/* 109 */             background = new ColorUIResource(117, 150, 227);
/* 110 */             backgroundGradientStart = new ColorUIResource(123, 162, 231);
/* 111 */             backgroundGradientEnd = new ColorUIResource(99, 117, 214);
/*     */           }
/*     */         }
/*     */       }
/* 115 */       defaults.add("TaskPaneContainer.backgroundPainter", new PainterUIResource(new MattePainter(new GradientPaint(0.0F, 0.0F, backgroundGradientStart, 0.0F, 1.0F, backgroundGradientEnd), true)));
/*     */       
/*     */ 
/*     */ 
/*     */ 
/* 120 */       defaults.add("TaskPaneContainer.background", background);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   protected void addMacDefaults(LookAndFeelAddons addon, DefaultsList defaults)
/*     */   {
/* 129 */     super.addMacDefaults(addon, defaults);
/*     */     
/* 131 */     defaults.add("TaskPaneContainer.background", new ColorUIResource(238, 238, 238));
/*     */   }
/*     */   
/*     */ 
/*     */   protected void addNimbusDefaults(LookAndFeelAddons addon, DefaultsList defaults)
/*     */   {
/* 137 */     super.addNimbusDefaults(addon, defaults);
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 143 */     defaults.add("TaskPaneContainer.background", new ColorUIResource(214, 217, 223));
/*     */   }
/*     */ }


/* Location:              E:\java\java学习\hutubill\lib\all.jar!\org\jdesktop\swingx\plaf\TaskPaneContainerAddon.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */
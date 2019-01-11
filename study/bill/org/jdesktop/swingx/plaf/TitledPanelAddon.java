/*     */ package org.jdesktop.swingx.plaf;
/*     */ 
/*     */ import java.awt.Color;
/*     */ import java.awt.GradientPaint;
/*     */ import javax.swing.plaf.ColorUIResource;
/*     */ import javax.swing.plaf.FontUIResource;
/*     */ import javax.swing.plaf.InsetsUIResource;
/*     */ import javax.swing.plaf.metal.MetalLookAndFeel;
/*     */ import javax.swing.plaf.metal.MetalTheme;
/*     */ import org.jdesktop.swingx.painter.MattePainter;
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
/*     */ public class TitledPanelAddon
/*     */   extends AbstractComponentAddon
/*     */ {
/*     */   public TitledPanelAddon()
/*     */   {
/*  43 */     super("JXTitledPanel");
/*     */   }
/*     */   
/*     */   protected void addBasicDefaults(LookAndFeelAddons addon, DefaultsList defaults)
/*     */   {
/*  48 */     super.addBasicDefaults(addon, defaults);
/*     */     
/*  50 */     defaults.add("TitledPanelUI", "org.jdesktop.swingx.plaf.basic.BasicTitledPanelUI");
/*  51 */     defaults.add("JXTitledPanel.titleFont", UIManagerExt.getSafeFont("Button.font", new FontUIResource("Dialog", 0, 12)));
/*     */     
/*  53 */     defaults.add("JXTitledPanel.titleForeground", new ColorUIResource(Color.WHITE));
/*  54 */     defaults.add("JXTitledPanel.titlePainter", new PainterUIResource(new MattePainter(new GradientPaint(0.0F, 0.0F, Color.LIGHT_GRAY, 0.0F, 1.0F, Color.GRAY), true)));
/*     */     
/*     */ 
/*  57 */     defaults.add("JXTitledPanel.captionInsets", new InsetsUIResource(4, 12, 4, 12));
/*  58 */     defaults.add("JXTitledPanel.rightDecorationInsets", new InsetsUIResource(1, 1, 1, 1));
/*  59 */     defaults.add("JXTitledPanel.leftDecorationInsets", new InsetsUIResource(1, 1, 1, 1));
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   protected void addLinuxDefaults(LookAndFeelAddons addon, DefaultsList defaults)
/*     */   {
/*  67 */     addMetalDefaults(addon, defaults);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   protected void addMetalDefaults(LookAndFeelAddons addon, DefaultsList defaults)
/*     */   {
/*  75 */     super.addMetalDefaults(addon, defaults);
/*     */     
/*  77 */     if (isPlastic()) {
/*  78 */       defaults.add("JXTitledPanel.titleForeground", new ColorUIResource(Color.WHITE));
/*  79 */       defaults.add("JXTitledPanel.titlePainter", new PainterUIResource(new MattePainter(new GradientPaint(0.0F, 0.0F, new Color(49, 121, 242), 0.0F, 1.0F, new Color(198, 211, 247)), true)));
/*     */     }
/*     */     else
/*     */     {
/*  83 */       defaults.add("JXTitledPanel.titleForeground", new ColorUIResource(Color.WHITE));
/*  84 */       defaults.add("JXTitledPanel.titlePainter", new PainterUIResource(new MattePainter(new GradientPaint(0.0F, 0.0F, MetalLookAndFeel.getCurrentTheme().getPrimaryControl(), 0.0F, 1.0F, MetalLookAndFeel.getCurrentTheme().getPrimaryControlDarkShadow()), true)));
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   protected void addWindowsDefaults(LookAndFeelAddons addon, DefaultsList defaults)
/*     */   {
/*  93 */     super.addWindowsDefaults(addon, defaults);
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 101 */     defaults.add("JXTitledPanel.titleForeground", UIManagerExt.getSafeColor("InternalFrame.activeTitleForeground", new ColorUIResource(Color.WHITE)));
/*     */     
/* 103 */     defaults.add("JXTitledPanel.titlePainter", new PainterUIResource(new MattePainter(new GradientPaint(0.0F, 0.0F, UIManagerExt.getSafeColor("InternalFrame.inactiveTitleGradient", new ColorUIResource(49, 121, 242)), 0.0F, 1.0F, UIManagerExt.getSafeColor("InternalFrame.activeTitleBackground", new ColorUIResource(198, 211, 247))), true)));
/*     */   }
/*     */ }


/* Location:              E:\java\java学习\hutubill\lib\all.jar!\org\jdesktop\swingx\plaf\TitledPanelAddon.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */
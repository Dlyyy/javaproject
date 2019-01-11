/*     */ package org.jdesktop.swingx.plaf;
/*     */ 
/*     */ import java.awt.Color;
/*     */ import java.util.logging.Logger;
/*     */ import javax.swing.UIDefaults;
/*     */ import javax.swing.UIManager;
/*     */ import javax.swing.plaf.ColorUIResource;
/*     */ import javax.swing.plaf.metal.MetalLookAndFeel;
/*     */ import javax.swing.plaf.metal.OceanTheme;
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
/*     */ public class UIColorHighlighterAddon
/*     */   extends AbstractComponentAddon
/*     */ {
/*  47 */   private static final Logger LOG = Logger.getLogger(UIColorHighlighterAddon.class.getName());
/*     */   
/*     */   public UIColorHighlighterAddon()
/*     */   {
/*  51 */     super("UIColorHighlighter");
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   protected void addBasicDefaults(LookAndFeelAddons addon, DefaultsList defaults)
/*     */   {
/*  59 */     super.addBasicDefaults(addon, defaults);
/*     */     
/*  61 */     defaults.add("UIColorHighlighter.stripingBackground", new ColorUIResource(229, 229, 229));
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   protected void addMacDefaults(LookAndFeelAddons addon, DefaultsList defaults)
/*     */   {
/*  69 */     super.addMacDefaults(addon, defaults);
/*     */     
/*  71 */     defaults.add("UIColorHighlighter.stripingBackground", new ColorUIResource(237, 243, 254));
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   protected void addMetalDefaults(LookAndFeelAddons addon, DefaultsList defaults)
/*     */   {
/*  79 */     super.addMetalDefaults(addon, defaults);
/*     */     
/*  81 */     if ((MetalLookAndFeel.getCurrentTheme() instanceof OceanTheme)) {
/*  82 */       defaults.add("UIColorHighlighter.stripingBackground", new ColorUIResource(230, 238, 246));
/*     */     } else {
/*  84 */       defaults.add("UIColorHighlighter.stripingBackground", new ColorUIResource(235, 235, 255));
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
/*  95 */     if (OS.isUsingWindowsVisualStyles()) {
/*  96 */       String xpStyle = OS.getWindowsVisualStyle();
/*     */       
/*  98 */       if ("HomeStead".equalsIgnoreCase(xpStyle))
/*     */       {
/* 100 */         defaults.add("UIColorHighlighter.stripingBackground", new ColorUIResource(228, 231, 219));
/* 101 */       } else if ("Metallic".equalsIgnoreCase(xpStyle))
/*     */       {
/* 103 */         defaults.add("UIColorHighlighter.stripingBackground", new ColorUIResource(235, 235, 236));
/*     */       }
/*     */       else {
/* 106 */         defaults.add("UIColorHighlighter.stripingBackground", new ColorUIResource(224, 233, 246));
/*     */       }
/*     */     }
/*     */     else {
/* 110 */       defaults.add("UIColorHighlighter.stripingBackground", new ColorUIResource(218, 222, 233));
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */   protected void addNimbusDefaults(LookAndFeelAddons addon, DefaultsList defaults)
/*     */   {
/* 117 */     super.addNimbusDefaults(addon, defaults);
/*     */     
/*     */ 
/*     */ 
/* 121 */     if (Boolean.TRUE.equals(UIManager.get("Nimbus.keepAlternateRowColor"))) { return;
/*     */     }
/*     */     
/*     */ 
/* 125 */     Object value = UIManager.getLookAndFeelDefaults().remove("Table.alternateRowColor");
/* 126 */     if ((value instanceof Color)) {
/* 127 */       defaults.add("UIColorHighlighter.stripingBackground", value, false);
/*     */     }
/*     */   }
/*     */ }


/* Location:              E:\java\java学习\hutubill\lib\all.jar!\org\jdesktop\swingx\plaf\UIColorHighlighterAddon.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */
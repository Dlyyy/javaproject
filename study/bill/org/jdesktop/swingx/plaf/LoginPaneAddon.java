/*     */ package org.jdesktop.swingx.plaf;
/*     */ 
/*     */ import java.awt.Color;
/*     */ import java.awt.Font;
/*     */ import javax.swing.BorderFactory;
/*     */ import javax.swing.LookAndFeel;
/*     */ import javax.swing.UIManager;
/*     */ import javax.swing.plaf.BorderUIResource;
/*     */ import javax.swing.plaf.ColorUIResource;
/*     */ import javax.swing.plaf.FontUIResource;
/*     */ import javax.swing.plaf.metal.MetalLookAndFeel;
/*     */ import javax.swing.plaf.metal.MetalTheme;
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
/*     */ public class LoginPaneAddon
/*     */   extends AbstractComponentAddon
/*     */ {
/*     */   public LoginPaneAddon()
/*     */   {
/*  45 */     super("JXLoginPane");
/*     */   }
/*     */   
/*     */   protected void addBasicDefaults(LookAndFeelAddons addon, DefaultsList defaults)
/*     */   {
/*  50 */     super.addBasicDefaults(addon, defaults);
/*  51 */     Color errorBG = new Color(255, 215, 215);
/*     */     
/*  53 */     defaults.add("LoginPaneUI", "org.jdesktop.swingx.plaf.basic.BasicLoginPaneUI");
/*  54 */     defaults.add("JXLoginPane.errorIcon", LookAndFeel.makeIcon(LoginPaneAddon.class, "basic/resources/error16.png"));
/*     */     
/*  56 */     defaults.add("JXLoginPane.bannerFont", new FontUIResource("Arial Bold", 0, 36));
/*     */     
/*  58 */     Font labelFont = UIManager.getFont("Label.font");
/*  59 */     Font boldLabel = labelFont != null ? labelFont.deriveFont(1) : new Font("SansSerif", 1, 12);
/*  60 */     defaults.add("JXLoginPane.pleaseWaitFont", new FontUIResource(boldLabel));
/*     */     
/*  62 */     defaults.add("JXLoginPane.bannerForeground", new ColorUIResource(Color.WHITE));
/*  63 */     defaults.add("JXLoginPane.bannerDarkBackground", new ColorUIResource(Color.GRAY));
/*  64 */     defaults.add("JXLoginPane.bannerLightBackground", new ColorUIResource(Color.LIGHT_GRAY));
/*  65 */     defaults.add("JXLoginPane.errorBackground", new ColorUIResource(errorBG));
/*  66 */     defaults.add("JXLoginPane.errorBorder", new BorderUIResource(BorderFactory.createCompoundBorder(BorderFactory.createEmptyBorder(0, 36, 0, 11), BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(Color.GRAY.darker()), BorderFactory.createMatteBorder(5, 7, 5, 5, errorBG)))));
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*  73 */     UIManagerExt.addResourceBundle("org.jdesktop.swingx.plaf.basic.resources.LoginPane");
/*     */   }
/*     */   
/*     */ 
/*     */   protected void addMetalDefaults(LookAndFeelAddons addon, DefaultsList defaults)
/*     */   {
/*  79 */     super.addMetalDefaults(addon, defaults);
/*     */     
/*  81 */     if (isPlastic()) {
/*  82 */       defaults.add("JXLoginPane.bannerForeground", new ColorUIResource(Color.WHITE));
/*  83 */       defaults.add("JXLoginPane.bannerDarkBackground", new ColorUIResource(Color.GRAY));
/*  84 */       defaults.add("JXLoginPane.bannerLightBackground", new ColorUIResource(Color.LIGHT_GRAY));
/*     */     } else {
/*  86 */       defaults.add("JXLoginPane.bannerForeground", new ColorUIResource(Color.WHITE));
/*  87 */       defaults.add("JXLoginPane.bannerDarkBackground", MetalLookAndFeel.getCurrentTheme().getPrimaryControlDarkShadow());
/*     */       
/*  89 */       defaults.add("JXLoginPane.bannerLightBackground", MetalLookAndFeel.getCurrentTheme().getPrimaryControl());
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */   protected void addWindowsDefaults(LookAndFeelAddons addon, DefaultsList defaults)
/*     */   {
/*  96 */     super.addWindowsDefaults(addon, defaults);
/*     */     
/*  98 */     defaults.add("JXLoginPane.bannerForeground", new ColorUIResource(Color.WHITE));
/*  99 */     defaults.add("JXLoginPane.bannerDarkBackground", new ColorUIResource(49, 121, 242));
/* 100 */     defaults.add("JXLoginPane.bannerLightBackground", new ColorUIResource(198, 211, 247));
/*     */   }
/*     */ }


/* Location:              E:\java\java学习\hutubill\lib\all.jar!\org\jdesktop\swingx\plaf\LoginPaneAddon.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */
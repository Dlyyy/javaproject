/*     */ package org.jdesktop.swingx.plaf;
/*     */ 
/*     */ import javax.swing.BorderFactory;
/*     */ import javax.swing.LookAndFeel;
/*     */ import javax.swing.UIManager;
/*     */ import javax.swing.border.LineBorder;
/*     */ import javax.swing.plaf.BorderUIResource;
/*     */ import org.jdesktop.swingx.plaf.basic.BasicDatePickerUI;
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
/*     */ public class DatePickerAddon
/*     */   extends AbstractComponentAddon
/*     */ {
/*     */   public DatePickerAddon()
/*     */   {
/*  38 */     super("JXDatePicker");
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   protected void addBasicDefaults(LookAndFeelAddons addon, DefaultsList defaults)
/*     */   {
/*  46 */     super.addBasicDefaults(addon, defaults);
/*     */     
/*  48 */     defaults.add("DatePickerUI", BasicDatePickerUI.class.getName());
/*  49 */     defaults.add("JXDatePicker.border", new BorderUIResource(BorderFactory.createCompoundBorder(LineBorder.createGrayLineBorder(), BorderFactory.createEmptyBorder(3, 3, 3, 3))));
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*  54 */     UIManagerExt.addResourceBundle("org.jdesktop.swingx.plaf.basic.resources.DatePicker");
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected void addWindowsDefaults(LookAndFeelAddons addon, DefaultsList defaults)
/*     */   {
/*  63 */     super.addWindowsDefaults(addon, defaults);
/*  64 */     if ((OS.isWindowsXP()) && (OS.isUsingWindowsVisualStyles())) {
/*  65 */       defaults.add("JXDatePicker.arrowIcon", LookAndFeel.makeIcon(DatePickerAddon.class, "windows/resources/combo-xp.png"));
/*     */     }
/*     */     else {
/*  68 */       defaults.add("JXDatePicker.arrowIcon", LookAndFeel.makeIcon(DatePickerAddon.class, "windows/resources/combo-w2k.png"));
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected void addLinuxDefaults(LookAndFeelAddons addon, DefaultsList defaults)
/*     */   {
/*  78 */     super.addLinuxDefaults(addon, defaults);
/*     */     
/*  80 */     defaults.add("JXDatePicker.arrowIcon", LookAndFeel.makeIcon(DatePickerAddon.class, "linux/resources/combo-gtk.png"));
/*     */     
/*     */ 
/*  83 */     if (isGTK())
/*     */     {
/*     */ 
/*  86 */       defaults.add("JXDatePicker.border", null);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   private boolean isGTK()
/*     */   {
/*  95 */     return "GTK".equals(UIManager.getLookAndFeel().getID());
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   protected void addMacDefaults(LookAndFeelAddons addon, DefaultsList defaults)
/*     */   {
/* 103 */     super.addMacDefaults(addon, defaults);
/*     */     
/* 105 */     defaults.add("JXDatePicker.arrowIcon", LookAndFeel.makeIcon(DatePickerAddon.class, "macosx/resources/combo-osx.png"));
/*     */     
/*     */ 
/* 108 */     defaults.add("JXDatePicker.border", "none");
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   protected void addNimbusDefaults(LookAndFeelAddons addon, DefaultsList defaults)
/*     */   {
/* 116 */     super.addNimbusDefaults(addon, defaults);
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 124 */     defaults.add("JXDatePicker.arrowIcon", LookAndFeel.makeIcon(DatePickerAddon.class, "macosx/resources/combo-osx.png"));
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 131 */     defaults.add("JXDatePicker.border", null);
/*     */   }
/*     */ }


/* Location:              E:\java\java学习\hutubill\lib\all.jar!\org\jdesktop\swingx\plaf\DatePickerAddon.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */
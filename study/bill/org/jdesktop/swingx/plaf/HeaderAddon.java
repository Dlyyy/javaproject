/*    */ package org.jdesktop.swingx.plaf;
/*    */ 
/*    */ import java.awt.Color;
/*    */ import java.awt.Font;
/*    */ import javax.swing.LookAndFeel;
/*    */ import javax.swing.UIManager;
/*    */ import javax.swing.plaf.ColorUIResource;
/*    */ import javax.swing.plaf.FontUIResource;
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
/*    */ public class HeaderAddon
/*    */   extends AbstractComponentAddon
/*    */ {
/*    */   public HeaderAddon()
/*    */   {
/* 40 */     super("JXHeader");
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */   protected void addBasicDefaults(LookAndFeelAddons addon, DefaultsList defaults)
/*    */   {
/* 48 */     super.addBasicDefaults(addon, defaults);
/*    */     
/* 50 */     defaults.add("HeaderUI", "org.jdesktop.swingx.plaf.basic.BasicHeaderUI");
/*    */     
/* 52 */     defaults.add("JXHeader.defaultIcon", LookAndFeel.makeIcon(HeaderAddon.class, "basic/resources/header-default.png"));
/*    */     
/*    */ 
/* 55 */     defaults.add("JXHeader.titleFont", new FontUIResource(UIManager.getFont("Label.font").deriveFont(1)));
/* 56 */     defaults.add("JXHeader.titleForeground", UIManager.getColor("Label.foreground"));
/* 57 */     defaults.add("JXHeader.descriptionFont", UIManager.getFont("Label.font"));
/* 58 */     defaults.add("JXHeader.descriptionForeground", UIManager.getColor("Label.foreground"));
/* 59 */     defaults.add("JXHeader.background", UIManagerExt.getSafeColor("control", new ColorUIResource(Color.decode("#C0C0C0"))));
/*    */     
/* 61 */     defaults.add("JXHeader.startBackground", new ColorUIResource(Color.WHITE));
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */   protected void addMacDefaults(LookAndFeelAddons addon, DefaultsList defaults)
/*    */   {
/* 69 */     super.addMacDefaults(addon, defaults);
/*    */     
/* 71 */     defaults.add("JXHeader.background", new ColorUIResource(new Color(218, 218, 218)));
/* 72 */     defaults.add("JXHeader.startBackground", new ColorUIResource(new Color(235, 235, 235)));
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */   protected void addNimbusDefaults(LookAndFeelAddons addon, DefaultsList defaults)
/*    */   {
/* 80 */     super.addNimbusDefaults(addon, defaults);
/*    */     
/* 82 */     defaults.add("JXHeader.background", new ColorUIResource(new Color(214, 217, 223, 255)));
/*    */   }
/*    */ }


/* Location:              E:\java\java学习\hutubill\lib\all.jar!\org\jdesktop\swingx\plaf\HeaderAddon.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */
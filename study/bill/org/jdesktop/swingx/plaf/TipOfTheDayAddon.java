/*    */ package org.jdesktop.swingx.plaf;
/*    */ 
/*    */ import java.awt.Color;
/*    */ import java.awt.Font;
/*    */ import javax.swing.BorderFactory;
/*    */ import javax.swing.LookAndFeel;
/*    */ import javax.swing.plaf.BorderUIResource;
/*    */ import javax.swing.plaf.ColorUIResource;
/*    */ import javax.swing.plaf.FontUIResource;
/*    */ import org.jdesktop.swingx.plaf.basic.BasicTipOfTheDayUI;
/*    */ import org.jdesktop.swingx.plaf.windows.WindowsTipOfTheDayUI;
/*    */ import org.jdesktop.swingx.plaf.windows.WindowsTipOfTheDayUI.TipAreaBorder;
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
/*    */ public class TipOfTheDayAddon
/*    */   extends AbstractComponentAddon
/*    */ {
/*    */   public TipOfTheDayAddon()
/*    */   {
/* 44 */     super("JXTipOfTheDay");
/*    */   }
/*    */   
/*    */   protected void addBasicDefaults(LookAndFeelAddons addon, DefaultsList defaults)
/*    */   {
/* 49 */     super.addBasicDefaults(addon, defaults);
/*    */     
/* 51 */     Font font = UIManagerExt.getSafeFont("Label.font", new Font("Dialog", 0, 12));
/* 52 */     font = font.deriveFont(1, 13.0F);
/*    */     
/* 54 */     defaults.add("swingx/TipOfTheDayUI", BasicTipOfTheDayUI.class.getName());
/* 55 */     defaults.add("TipOfTheDay.font", UIManagerExt.getSafeFont("TextPane.font", new FontUIResource("Serif", 0, 12)));
/*    */     
/* 57 */     defaults.add("TipOfTheDay.tipFont", new FontUIResource(font));
/* 58 */     defaults.add("TipOfTheDay.background", new ColorUIResource(Color.WHITE));
/* 59 */     defaults.add("TipOfTheDay.icon", LookAndFeel.makeIcon(BasicTipOfTheDayUI.class, "resources/TipOfTheDay24.gif"));
/*    */     
/* 61 */     defaults.add("TipOfTheDay.border", new BorderUIResource(BorderFactory.createLineBorder(new Color(117, 117, 117))));
/*    */     
/*    */ 
/* 64 */     UIManagerExt.addResourceBundle("org.jdesktop.swingx.plaf.basic.resources.TipOfTheDay");
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   protected void addWindowsDefaults(LookAndFeelAddons addon, DefaultsList defaults)
/*    */   {
/* 73 */     super.addWindowsDefaults(addon, defaults);
/*    */     
/* 75 */     Font font = UIManagerExt.getSafeFont("Label.font", new Font("Dialog", 0, 12));
/*    */     
/* 77 */     font = font.deriveFont(13.0F);
/*    */     
/* 79 */     defaults.add("swingx/TipOfTheDayUI", WindowsTipOfTheDayUI.class.getName());
/* 80 */     defaults.add("TipOfTheDay.background", new ColorUIResource(Color.GRAY));
/* 81 */     defaults.add("TipOfTheDay.font", new FontUIResource(font));
/* 82 */     defaults.add("TipOfTheDay.icon", LookAndFeel.makeIcon(WindowsTipOfTheDayUI.class, "resources/tipoftheday.png"));
/*    */     
/* 84 */     defaults.add("TipOfTheDay.border", new BorderUIResource(new WindowsTipOfTheDayUI.TipAreaBorder()));
/*    */     
/* 86 */     UIManagerExt.addResourceBundle("org.jdesktop.swingx.plaf.windows.resources.TipOfTheDay");
/*    */   }
/*    */ }


/* Location:              E:\java\java学习\hutubill\lib\all.jar!\org\jdesktop\swingx\plaf\TipOfTheDayAddon.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */
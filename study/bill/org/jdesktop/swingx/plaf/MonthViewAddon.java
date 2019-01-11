/*    */ package org.jdesktop.swingx.plaf;
/*    */ 
/*    */ import java.awt.Color;
/*    */ import javax.swing.LookAndFeel;
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
/*    */ public class MonthViewAddon
/*    */   extends AbstractComponentAddon
/*    */ {
/*    */   public MonthViewAddon()
/*    */   {
/* 32 */     super("JXMonthView");
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */   protected void addBasicDefaults(LookAndFeelAddons addon, DefaultsList defaults)
/*    */   {
/* 40 */     super.addBasicDefaults(addon, defaults);
/*    */     
/* 42 */     defaults.add("MonthViewUI", "org.jdesktop.swingx.plaf.basic.BasicMonthViewUI");
/* 43 */     defaults.add("JXMonthView.background", new ColorUIResource(Color.WHITE));
/* 44 */     defaults.add("JXMonthView.monthStringBackground", new ColorUIResource(138, 173, 209));
/* 45 */     defaults.add("JXMonthView.monthStringForeground", new ColorUIResource(68, 68, 68));
/* 46 */     defaults.add("JXMonthView.daysOfTheWeekForeground", new ColorUIResource(68, 68, 68));
/* 47 */     defaults.add("JXMonthView.weekOfTheYearForeground", new ColorUIResource(68, 68, 68));
/* 48 */     defaults.add("JXMonthView.unselectableDayForeground", new ColorUIResource(Color.RED));
/* 49 */     defaults.add("JXMonthView.selectedBackground", new ColorUIResource(197, 220, 240));
/* 50 */     defaults.add("JXMonthView.flaggedDayForeground", new ColorUIResource(Color.RED));
/* 51 */     defaults.add("JXMonthView.leadingDayForeground", new ColorUIResource(Color.LIGHT_GRAY));
/* 52 */     defaults.add("JXMonthView.trailingDayForeground", new ColorUIResource(Color.LIGHT_GRAY));
/* 53 */     defaults.add("JXMonthView.font", UIManagerExt.getSafeFont("Button.font", new FontUIResource("Dialog", 0, 12)));
/*    */     
/* 55 */     defaults.add("JXMonthView.monthDownFileName", LookAndFeel.makeIcon(MonthViewAddon.class, "basic/resources/month-down.png"));
/*    */     
/* 57 */     defaults.add("JXMonthView.monthUpFileName", LookAndFeel.makeIcon(MonthViewAddon.class, "basic/resources/month-up.png"));
/*    */     
/* 59 */     defaults.add("JXMonthView.boxPaddingX", Integer.valueOf(3));
/* 60 */     defaults.add("JXMonthView.boxPaddingY", Integer.valueOf(3));
/*    */   }
/*    */ }


/* Location:              E:\java\java学习\hutubill\lib\all.jar!\org\jdesktop\swingx\plaf\MonthViewAddon.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */
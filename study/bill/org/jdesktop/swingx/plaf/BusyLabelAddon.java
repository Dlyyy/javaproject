/*    */ package org.jdesktop.swingx.plaf;
/*    */ 
/*    */ import java.awt.Color;
/*    */ import java.awt.geom.Ellipse2D.Float;
/*    */ import java.awt.geom.RoundRectangle2D.Float;
/*    */ import javax.swing.plaf.ColorUIResource;
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
/*    */ 
/*    */ public class BusyLabelAddon
/*    */   extends AbstractComponentAddon
/*    */ {
/*    */   public BusyLabelAddon()
/*    */   {
/* 39 */     super("JXBusyLabel");
/*    */   }
/*    */   
/*    */   protected void addBasicDefaults(LookAndFeelAddons addon, DefaultsList defaults)
/*    */   {
/* 44 */     defaults.add("BusyLabelUI", "org.jdesktop.swingx.plaf.basic.BasicBusyLabelUI");
/* 45 */     defaults.add("JXBusyLabel.delay", Integer.valueOf(100));
/* 46 */     defaults.add("JXBusyLabel.baseColor", new ColorUIResource(Color.LIGHT_GRAY));
/* 47 */     defaults.add("JXBusyLabel.highlightColor", new ColorUIResource(UIManagerExt.getSafeColor("Label.foreground", Color.BLACK)));
/* 48 */     float barLength = 8.0F;
/* 49 */     float barWidth = 4.0F;
/* 50 */     float height = 26.0F;
/* 51 */     defaults.add("JXBusyLabel.pointShape", new ShapeUIResource(new RoundRectangle2D.Float(0.0F, 0.0F, barLength, barWidth, barWidth, barWidth)));
/*    */     
/*    */ 
/* 54 */     defaults.add("JXBusyLabel.trajectoryShape", new ShapeUIResource(new Ellipse2D.Float(barLength / 2.0F, barLength / 2.0F, height - barLength, height - barLength)));
/*    */   }
/*    */ }


/* Location:              E:\java\java学习\hutubill\lib\all.jar!\org\jdesktop\swingx\plaf\BusyLabelAddon.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */
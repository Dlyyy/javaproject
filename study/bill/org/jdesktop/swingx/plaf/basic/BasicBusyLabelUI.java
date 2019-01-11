/*    */ package org.jdesktop.swingx.plaf.basic;
/*    */ 
/*    */ import java.awt.Color;
/*    */ import java.awt.Dimension;
/*    */ import java.awt.Shape;
/*    */ import javax.swing.JComponent;
/*    */ import javax.swing.UIManager;
/*    */ import javax.swing.plaf.ComponentUI;
/*    */ import javax.swing.plaf.basic.BasicLabelUI;
/*    */ import org.jdesktop.swingx.JXBusyLabel;
/*    */ import org.jdesktop.swingx.painter.BusyPainter;
/*    */ import org.jdesktop.swingx.plaf.BusyLabelUI;
/*    */ import org.jdesktop.swingx.plaf.UIManagerExt;
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
/*    */ public class BasicBusyLabelUI
/*    */   extends BasicLabelUI
/*    */   implements BusyLabelUI
/*    */ {
/*    */   public BasicBusyLabelUI(JXBusyLabel lbl) {}
/*    */   
/*    */   public static ComponentUI createUI(JComponent c)
/*    */   {
/* 48 */     return new BasicBusyLabelUI((JXBusyLabel)c);
/*    */   }
/*    */   
/*    */   public BusyPainter getBusyPainter(final Dimension dim) {
/* 52 */     BusyPainter p = new BusyPainter()
/*    */     {
/*    */       protected void init(Shape point, Shape trajectory, Color b, Color h) {
/* 55 */         super.init(dim == null ? UIManagerExt.getShape("JXBusyLabel.pointShape") : getScaledDefaultPoint(dim.height), dim == null ? UIManagerExt.getShape("JXBusyLabel.trajectoryShape") : getScaledDefaultTrajectory(dim.height), UIManagerExt.getSafeColor("JXBusyLabel.baseColor", Color.LIGHT_GRAY), UIManagerExt.getSafeColor("JXBusyLabel.highlightColor", Color.BLACK));
/*    */ 
/*    */       }
/*    */       
/*    */ 
/* 60 */     };
/* 61 */     return p;
/*    */   }
/*    */   
/*    */   public int getDelay() {
/* 65 */     return UIManager.getInt("JXBusyLabel.delay");
/*    */   }
/*    */ }


/* Location:              E:\java\java学习\hutubill\lib\all.jar!\org\jdesktop\swingx\plaf\basic\BasicBusyLabelUI.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */
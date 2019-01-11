/*    */ package org.jdesktop.swingx.plaf.metal;
/*    */ 
/*    */ import java.awt.Graphics;
/*    */ import java.awt.Graphics2D;
/*    */ import java.awt.RenderingHints;
/*    */ import javax.swing.JComponent;
/*    */ import javax.swing.LookAndFeel;
/*    */ import javax.swing.border.Border;
/*    */ import javax.swing.plaf.ComponentUI;
/*    */ import org.jdesktop.swingx.JXTaskPane;
/*    */ import org.jdesktop.swingx.plaf.basic.BasicTaskPaneUI;
/*    */ import org.jdesktop.swingx.plaf.basic.BasicTaskPaneUI.PaneBorder;
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
/*    */ public class MetalTaskPaneUI
/*    */   extends BasicTaskPaneUI
/*    */ {
/*    */   public static ComponentUI createUI(JComponent c)
/*    */   {
/* 43 */     return new MetalTaskPaneUI();
/*    */   }
/*    */   
/*    */   protected void installDefaults()
/*    */   {
/* 48 */     super.installDefaults();
/*    */     
/* 50 */     LookAndFeel.installProperty(this.group, "opaque", Boolean.valueOf(false));
/*    */   }
/*    */   
/*    */   protected Border createPaneBorder()
/*    */   {
/* 55 */     return new MetalPaneBorder();
/*    */   }
/*    */   
/*    */   class MetalPaneBorder
/*    */     extends BasicTaskPaneUI.PaneBorder
/*    */   {
/*    */     MetalPaneBorder()
/*    */     {
/* 63 */       super();
/*    */     }
/*    */     
/*    */     protected void paintExpandedControls(JXTaskPane group, Graphics g, int x, int y, int width, int height)
/*    */     {
/* 68 */       ((Graphics2D)g).setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
/*    */       
/*    */ 
/*    */ 
/* 72 */       g.setColor(getPaintColor(group));
/* 73 */       paintRectAroundControls(group, g, x, y, width, height, g.getColor(), g.getColor());
/*    */       
/* 75 */       paintChevronControls(group, g, x, y, width, height);
/*    */       
/* 77 */       ((Graphics2D)g).setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_OFF);
/*    */     }
/*    */     
/*    */ 
/*    */ 
/*    */     protected boolean isMouseOverBorder()
/*    */     {
/* 84 */       return true;
/*    */     }
/*    */   }
/*    */ }


/* Location:              E:\java\java学习\hutubill\lib\all.jar!\org\jdesktop\swingx\plaf\metal\MetalTaskPaneUI.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */
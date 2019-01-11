/*    */ package org.jdesktop.swingx.plaf.windows;
/*    */ 
/*    */ import java.awt.Color;
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
/*    */ 
/*    */ public class WindowsClassicTaskPaneUI
/*    */   extends BasicTaskPaneUI
/*    */ {
/*    */   public static ComponentUI createUI(JComponent c)
/*    */   {
/* 45 */     return new WindowsClassicTaskPaneUI();
/*    */   }
/*    */   
/*    */   protected void installDefaults()
/*    */   {
/* 50 */     super.installDefaults();
/*    */     
/* 52 */     LookAndFeel.installProperty(this.group, "opaque", Boolean.valueOf(false));
/*    */   }
/*    */   
/*    */   protected Border createPaneBorder()
/*    */   {
/* 57 */     return new ClassicPaneBorder();
/*    */   }
/*    */   
/*    */   class ClassicPaneBorder
/*    */     extends BasicTaskPaneUI.PaneBorder
/*    */   {
/*    */     ClassicPaneBorder()
/*    */     {
/* 65 */       super();
/*    */     }
/*    */     
/*    */     protected void paintExpandedControls(JXTaskPane group, Graphics g, int x, int y, int width, int height)
/*    */     {
/* 70 */       ((Graphics2D)g).setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
/*    */       
/*    */ 
/*    */ 
/* 74 */       paintRectAroundControls(group, g, x, y, width, height, Color.white, Color.gray);
/*    */       
/* 76 */       g.setColor(getPaintColor(group));
/* 77 */       paintChevronControls(group, g, x, y, width, height);
/*    */       
/* 79 */       ((Graphics2D)g).setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_OFF);
/*    */     }
/*    */   }
/*    */ }


/* Location:              E:\java\java学习\hutubill\lib\all.jar!\org\jdesktop\swingx\plaf\windows\WindowsClassicTaskPaneUI.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */
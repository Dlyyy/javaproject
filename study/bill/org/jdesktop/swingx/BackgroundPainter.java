/*    */ package org.jdesktop.swingx;
/*    */ 
/*    */ import java.awt.Color;
/*    */ import java.awt.Graphics2D;
/*    */ import javax.swing.JComponent;
/*    */ import javax.swing.LookAndFeel;
/*    */ import javax.swing.UIManager;
/*    */ import org.jdesktop.swingx.painter.Painter;
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
/*    */ class BackgroundPainter
/*    */   implements Painter<JComponent>
/*    */ {
/*    */   private final Color color;
/*    */   
/*    */   public BackgroundPainter(Color color)
/*    */   {
/* 40 */     this.color = color;
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */   public void paint(Graphics2D g, JComponent object, int width, int height)
/*    */   {
/* 48 */     if (this.color == null) {
/* 49 */       return;
/*    */     }
/*    */     
/* 52 */     if ((object.isOpaque()) || (UIManager.getLookAndFeel().getID().equals("Nimbus"))) {
/* 53 */       g.setColor(this.color);
/* 54 */       g.fillRect(0, 0, width, height);
/*    */     }
/*    */   }
/*    */ }


/* Location:              E:\java\java学习\hutubill\lib\all.jar!\org\jdesktop\swingx\BackgroundPainter.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */
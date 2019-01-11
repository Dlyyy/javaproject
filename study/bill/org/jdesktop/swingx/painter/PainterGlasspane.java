/*    */ package org.jdesktop.swingx.painter;
/*    */ 
/*    */ import java.awt.Graphics;
/*    */ import java.awt.Graphics2D;
/*    */ import java.awt.Point;
/*    */ import java.util.ArrayList;
/*    */ import java.util.List;
/*    */ import javax.swing.JComponent;
/*    */ import javax.swing.SwingUtilities;
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
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class PainterGlasspane
/*    */   extends JComponent
/*    */ {
/*    */   private Painter painter;
/*    */   private List<JComponent> targets;
/*    */   
/*    */   public PainterGlasspane()
/*    */   {
/* 52 */     this.targets = new ArrayList();
/*    */   }
/*    */   
/*    */   public void addTarget(JComponent comp) {
/* 56 */     this.targets.add(comp);
/* 57 */     repaint();
/*    */   }
/*    */   
/* 60 */   public void removeTarget(JComponent comp) { this.targets.remove(comp);
/* 61 */     repaint();
/*    */   }
/*    */   
/*    */   protected void paintComponent(Graphics gfx)
/*    */   {
/* 66 */     Graphics2D g = (Graphics2D)gfx;
/* 67 */     if (getPainter() != null) {
/* 68 */       for (JComponent target : this.targets) {
/* 69 */         Point offset = calcOffset(target);
/* 70 */         g.translate(offset.x, offset.y);
/* 71 */         getPainter().paint(g, target, target.getWidth(), target.getHeight());
/* 72 */         g.translate(-offset.x, -offset.y);
/*    */       }
/*    */     }
/*    */   }
/*    */   
/*    */   private Point calcOffset(JComponent target) {
/* 78 */     if (target == null) {
/* 79 */       return new Point(0, 0);
/*    */     }
/*    */     
/* 82 */     if (target.getParent() == SwingUtilities.getWindowAncestor(target)) {
/* 83 */       return new Point(0, 0);
/*    */     }
/*    */     
/* 86 */     Point parent = calcOffset((JComponent)target.getParent());
/* 87 */     Point self = target.getLocation();
/* 88 */     return new Point(parent.x + self.x, parent.y + self.y);
/*    */   }
/*    */   
/*    */   public Painter getPainter() {
/* 92 */     return this.painter;
/*    */   }
/*    */   
/*    */   public void setPainter(Painter painter) {
/* 96 */     Painter old = getPainter();
/* 97 */     this.painter = painter;
/* 98 */     firePropertyChange("painter", old, getPainter());
/* 99 */     repaint();
/*    */   }
/*    */ }


/* Location:              E:\java\java学习\hutubill\lib\all.jar!\org\jdesktop\swingx\painter\PainterGlasspane.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */
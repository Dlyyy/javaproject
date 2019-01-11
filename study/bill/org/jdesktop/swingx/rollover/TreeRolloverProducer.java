/*    */ package org.jdesktop.swingx.rollover;
/*    */ 
/*    */ import java.awt.Point;
/*    */ import java.awt.Rectangle;
/*    */ import java.awt.event.MouseEvent;
/*    */ import javax.swing.JComponent;
/*    */ import org.jdesktop.swingx.JXTree;
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
/*    */ 
/*    */ 
/*    */ public class TreeRolloverProducer
/*    */   extends RolloverProducer
/*    */ {
/*    */   public void mousePressed(MouseEvent e)
/*    */   {
/* 49 */     JXTree tree = (JXTree)e.getComponent();
/* 50 */     Point mousePoint = e.getPoint();
/* 51 */     int labelRow = tree.getRowForLocation(mousePoint.x, mousePoint.y);
/*    */     
/* 53 */     if (labelRow >= 0)
/* 54 */       return;
/* 55 */     int row = tree.getClosestRowForLocation(mousePoint.x, mousePoint.y);
/* 56 */     Rectangle bounds = tree.getRowBounds(row);
/* 57 */     if (bounds == null) {
/* 58 */       row = -1;
/*    */     }
/* 60 */     else if ((bounds.y + bounds.height < mousePoint.y) || (bounds.x > mousePoint.x))
/*    */     {
/* 62 */       row = -1;
/*    */     }
/*    */     
/*    */ 
/* 66 */     if (row < 0)
/* 67 */       return;
/* 68 */     tree.dispatchEvent(new MouseEvent(tree, e.getID(), e.getWhen(), e.getModifiers(), bounds.x + bounds.width - 2, mousePoint.y, e.getClickCount(), e.isPopupTrigger(), e.getButton()));
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */   protected void updateRolloverPoint(JComponent component, Point mousePoint)
/*    */   {
/* 75 */     JXTree tree = (JXTree)component;
/* 76 */     int row = tree.getClosestRowForLocation(mousePoint.x, mousePoint.y);
/* 77 */     Rectangle bounds = tree.getRowBounds(row);
/* 78 */     if (bounds == null) {
/* 79 */       row = -1;
/*    */     }
/* 81 */     else if ((bounds.y + bounds.height < mousePoint.y) || (bounds.x > mousePoint.x))
/*    */     {
/* 83 */       row = -1;
/*    */     }
/*    */     
/* 86 */     int col = row < 0 ? -1 : 0;
/* 87 */     this.rollover.x = col;
/* 88 */     this.rollover.y = row;
/*    */   }
/*    */ }


/* Location:              E:\java\java学习\hutubill\lib\all.jar!\org\jdesktop\swingx\rollover\TreeRolloverProducer.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */
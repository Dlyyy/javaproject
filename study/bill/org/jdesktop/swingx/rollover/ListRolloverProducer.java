/*    */ package org.jdesktop.swingx.rollover;
/*    */ 
/*    */ import java.awt.Point;
/*    */ import java.awt.Rectangle;
/*    */ import javax.swing.JComponent;
/*    */ import javax.swing.JList;
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
/*    */ public class ListRolloverProducer
/*    */   extends RolloverProducer
/*    */ {
/*    */   protected void updateRolloverPoint(JComponent component, Point mousePoint)
/*    */   {
/* 39 */     JList list = (JList)component;
/* 40 */     int row = list.locationToIndex(mousePoint);
/* 41 */     if (row >= 0) {
/* 42 */       Rectangle cellBounds = list.getCellBounds(row, row);
/* 43 */       if (!cellBounds.contains(mousePoint)) {
/* 44 */         row = -1;
/*    */       }
/*    */     }
/* 47 */     int col = row < 0 ? -1 : 0;
/* 48 */     this.rollover.x = col;
/* 49 */     this.rollover.y = row;
/*    */   }
/*    */ }


/* Location:              E:\java\java学习\hutubill\lib\all.jar!\org\jdesktop\swingx\rollover\ListRolloverProducer.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */
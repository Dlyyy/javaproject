/*    */ package org.jdesktop.swingx.rollover;
/*    */ 
/*    */ import java.awt.Point;
/*    */ import javax.swing.JComponent;
/*    */ import javax.swing.JTable;
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
/*    */ public class TableRolloverProducer
/*    */   extends RolloverProducer
/*    */ {
/*    */   protected void updateRolloverPoint(JComponent component, Point mousePoint)
/*    */   {
/* 38 */     JTable table = (JTable)component;
/* 39 */     int col = table.columnAtPoint(mousePoint);
/* 40 */     int row = table.rowAtPoint(mousePoint);
/* 41 */     if ((col < 0) || (row < 0)) {
/* 42 */       row = -1;
/* 43 */       col = -1;
/*    */     }
/* 45 */     this.rollover.x = col;
/* 46 */     this.rollover.y = row;
/*    */   }
/*    */ }


/* Location:              E:\java\java学习\hutubill\lib\all.jar!\org\jdesktop\swingx\rollover\TableRolloverProducer.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */
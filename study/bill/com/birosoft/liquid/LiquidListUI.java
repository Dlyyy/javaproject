/*    */ package com.birosoft.liquid;
/*    */ 
/*    */ import java.awt.Color;
/*    */ import java.awt.Component;
/*    */ import java.awt.Graphics;
/*    */ import java.awt.Rectangle;
/*    */ import javax.swing.CellRendererPane;
/*    */ import javax.swing.JComponent;
/*    */ import javax.swing.JList;
/*    */ import javax.swing.ListCellRenderer;
/*    */ import javax.swing.ListModel;
/*    */ import javax.swing.ListSelectionModel;
/*    */ import javax.swing.plaf.ComponentUI;
/*    */ import javax.swing.plaf.basic.BasicListUI;
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
/*    */ public class LiquidListUI
/*    */   extends BasicListUI
/*    */ {
/*    */   private Color defaultBackground;
/*    */   
/*    */   protected void paintCell(Graphics g, int row, Rectangle rowBounds, ListCellRenderer cellRenderer, ListModel dataModel, ListSelectionModel selModel, int leadIndex)
/*    */   {
/* 46 */     Object value = dataModel.getElementAt(row);
/* 47 */     boolean cellHasFocus = (this.list.hasFocus()) && (row == leadIndex);
/* 48 */     boolean isSelected = selModel.isSelectedIndex(row);
/*    */     
/* 50 */     Component rendererComponent = cellRenderer.getListCellRendererComponent(this.list, value, row, isSelected, cellHasFocus);
/*    */     
/*    */ 
/* 53 */     if (LiquidLookAndFeel.defaultRowBackgroundMode) {
/* 54 */       if (row % 2 == 0) {
/* 55 */         if (LiquidLookAndFeel.getDesktopColor().equals(rendererComponent.getBackground())) {
/* 56 */           rendererComponent.setBackground(this.defaultBackground);
/*    */         }
/*    */       }
/* 59 */       else if (this.defaultBackground.equals(rendererComponent.getBackground())) {
/* 60 */         rendererComponent.setBackground(LiquidLookAndFeel.getDesktopColor());
/*    */       }
/*    */     }
/*    */     
/*    */ 
/* 65 */     int cx = rowBounds.x;
/* 66 */     int cy = rowBounds.y;
/* 67 */     int cw = rowBounds.width;
/* 68 */     int ch = rowBounds.height;
/* 69 */     this.rendererPane.paintComponent(g, rendererComponent, this.list, cx, cy, cw, ch, true);
/*    */   }
/*    */   
/*    */   public void paint(Graphics g, JComponent c)
/*    */   {
/* 74 */     if ((LiquidLookAndFeel.defaultRowBackgroundMode & this.defaultBackground == null))
/*    */     {
/* 76 */       this.defaultBackground = c.getBackground();
/*    */     }
/*    */     
/* 79 */     super.paint(g, c);
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   public static ComponentUI createUI(JComponent list)
/*    */   {
/* 89 */     return new LiquidListUI();
/*    */   }
/*    */ }


/* Location:              E:\java\java学习\hutubill\lib\all.jar!\com\birosoft\liquid\LiquidListUI.class
 * Java compiler version: 4 (48.0)
 * JD-Core Version:       0.7.1
 */
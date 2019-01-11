/*    */ package org.jdesktop.swingx.icon;
/*    */ 
/*    */ import java.awt.Color;
/*    */ import java.awt.Component;
/*    */ import java.awt.Container;
/*    */ import java.awt.Graphics;
/*    */ import javax.swing.Icon;
/*    */ import javax.swing.JFrame;
/*    */ import javax.swing.JLabel;
/*    */ import javax.swing.plaf.UIResource;
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
/*    */ public class ColumnControlIcon
/*    */   implements Icon, UIResource
/*    */ {
/* 41 */   private int width = 10;
/* 42 */   private int height = 10;
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */   public int getIconWidth()
/*    */   {
/* 49 */     return this.width;
/*    */   }
/*    */   
/*    */   public int getIconHeight() {
/* 53 */     return this.height;
/*    */   }
/*    */   
/*    */   public void paintIcon(Component c, Graphics g, int x, int y) {
/* 57 */     Color color = c.getForeground();
/* 58 */     g.setColor(color);
/*    */     
/*    */ 
/* 61 */     g.drawLine(x, y, x + 8, y);
/* 62 */     g.drawLine(x, y + 2, x + 8, y + 2);
/* 63 */     g.drawLine(x, y + 8, x + 2, y + 8);
/*    */     
/*    */ 
/* 66 */     g.drawLine(x, y + 1, x, y + 7);
/* 67 */     g.drawLine(x + 4, y + 1, x + 4, y + 4);
/* 68 */     g.drawLine(x + 8, y + 1, x + 8, y + 4);
/*    */     
/*    */ 
/* 71 */     g.drawLine(x + 3, y + 6, x + 9, y + 6);
/* 72 */     g.drawLine(x + 4, y + 7, x + 8, y + 7);
/* 73 */     g.drawLine(x + 5, y + 8, x + 7, y + 8);
/* 74 */     g.drawLine(x + 6, y + 9, x + 6, y + 9);
/*    */   }
/*    */   
/*    */   public static void main(String[] args)
/*    */   {
/* 79 */     JFrame frame = new JFrame();
/* 80 */     frame.setDefaultCloseOperation(3);
/* 81 */     JLabel label = new JLabel(new ColumnControlIcon());
/* 82 */     frame.getContentPane().add("Center", label);
/* 83 */     frame.pack();
/* 84 */     frame.setVisible(true);
/*    */   }
/*    */ }


/* Location:              E:\java\java学习\hutubill\lib\all.jar!\org\jdesktop\swingx\icon\ColumnControlIcon.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */
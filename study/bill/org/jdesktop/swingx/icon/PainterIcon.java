/*    */ package org.jdesktop.swingx.icon;
/*    */ 
/*    */ import java.awt.Component;
/*    */ import java.awt.Dimension;
/*    */ import java.awt.Graphics;
/*    */ import java.awt.Graphics2D;
/*    */ import javax.swing.Icon;
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
/*    */ public class PainterIcon
/*    */   implements Icon
/*    */ {
/*    */   Dimension size;
/*    */   private Painter painter;
/*    */   
/*    */   public PainterIcon(Dimension size)
/*    */   {
/* 37 */     this.size = size;
/*    */   }
/*    */   
/*    */   public int getIconHeight() {
/* 41 */     return this.size.height;
/*    */   }
/*    */   
/*    */   public int getIconWidth() {
/* 45 */     return this.size.width;
/*    */   }
/*    */   
/*    */   public void paintIcon(Component c, Graphics g, int x, int y)
/*    */   {
/* 50 */     if ((getPainter() != null) && ((g instanceof Graphics2D))) {
/* 51 */       g = g.create();
/*    */       try
/*    */       {
/* 54 */         g.translate(x, y);
/* 55 */         getPainter().paint((Graphics2D)g, c, this.size.width, this.size.height);
/* 56 */         g.translate(-x, -y);
/*    */       } finally {
/* 58 */         g.dispose();
/*    */       }
/*    */     }
/*    */   }
/*    */   
/*    */   public Painter getPainter() {
/* 64 */     return this.painter;
/*    */   }
/*    */   
/*    */   public void setPainter(Painter painter) {
/* 68 */     this.painter = painter;
/*    */   }
/*    */ }


/* Location:              E:\java\java学习\hutubill\lib\all.jar!\org\jdesktop\swingx\icon\PainterIcon.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */
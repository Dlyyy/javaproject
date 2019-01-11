/*    */ package org.jdesktop.swingx.icon;
/*    */ 
/*    */ import java.awt.Component;
/*    */ import java.awt.Graphics;
/*    */ import java.io.Serializable;
/*    */ import javax.swing.Icon;
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
/*    */ public final class EmptyIcon
/*    */   implements Icon, Serializable
/*    */ {
/*    */   private int width;
/*    */   private int height;
/*    */   
/*    */   public EmptyIcon()
/*    */   {
/* 38 */     this(0, 0);
/*    */   }
/*    */   
/*    */   public EmptyIcon(int width, int height) {
/* 42 */     this.width = width;
/* 43 */     this.height = height;
/*    */   }
/*    */   
/*    */   public int getIconHeight() {
/* 47 */     return this.height;
/*    */   }
/*    */   
/*    */   public int getIconWidth() {
/* 51 */     return this.width;
/*    */   }
/*    */   
/*    */   public void paintIcon(Component c, Graphics g, int x, int y) {}
/*    */ }


/* Location:              E:\java\java学习\hutubill\lib\all.jar!\org\jdesktop\swingx\icon\EmptyIcon.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */
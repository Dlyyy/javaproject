/*    */ package com.birosoft.liquid.borders;
/*    */ 
/*    */ import java.awt.Color;
/*    */ import java.awt.Component;
/*    */ import java.awt.Graphics;
/*    */ import java.awt.Insets;
/*    */ import javax.swing.border.AbstractBorder;
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
/*    */ public class LiquidProgressBarBorder
/*    */   extends AbstractBorder
/*    */   implements UIResource
/*    */ {
/* 26 */   private static final Insets defaultInsets = new Insets(3, 5, 3, 5);
/*    */   private Insets insets;
/*    */   
/*    */   public LiquidProgressBarBorder()
/*    */   {
/* 31 */     this.insets = defaultInsets;
/*    */   }
/*    */   
/*    */   public LiquidProgressBarBorder(Insets insets) {
/* 35 */     this.insets = insets;
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   public Insets getBorderInsets(Component c)
/*    */   {
/* 45 */     return this.insets;
/*    */   }
/*    */   
/*    */   public void setInsets(Insets i) {
/* 49 */     this.insets = i;
/*    */   }
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
/*    */   public void paintBorder(Component c, Graphics g, int x, int y, int w, int h)
/*    */   {
/* 68 */     g.setColor(Color.RED);
/* 69 */     g.drawLine(0, 0, 0, h);
/* 70 */     g.drawLine(w, w, w, h);
/*    */   }
/*    */ }


/* Location:              E:\java\java学习\hutubill\lib\all.jar!\com\birosoft\liquid\borders\LiquidProgressBarBorder.class
 * Java compiler version: 4 (48.0)
 * JD-Core Version:       0.7.1
 */
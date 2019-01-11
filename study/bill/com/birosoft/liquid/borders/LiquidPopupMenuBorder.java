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
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class LiquidPopupMenuBorder
/*    */   extends AbstractBorder
/*    */   implements UIResource
/*    */ {
/* 32 */   protected static Insets insets = new Insets(2, 2, 2, 2);
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
/* 46 */     g.setColor(new Color(255, 255, 255));
/* 47 */     g.drawRect(1, 1, w - 3, h - 3);
/* 48 */     g.setColor(new Color(175, 174, 174));
/* 49 */     g.drawRect(0, 0, w - 1, h - 1);
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   public Insets getBorderInsets(Component c)
/*    */   {
/* 60 */     return insets;
/*    */   }
/*    */ }


/* Location:              E:\java\java学习\hutubill\lib\all.jar!\com\birosoft\liquid\borders\LiquidPopupMenuBorder.class
 * Java compiler version: 4 (48.0)
 * JD-Core Version:       0.7.1
 */
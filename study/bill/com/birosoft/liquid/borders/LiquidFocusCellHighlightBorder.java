/*    */ package com.birosoft.liquid.borders;
/*    */ 
/*    */ import java.awt.BasicStroke;
/*    */ import java.awt.Color;
/*    */ import java.awt.Component;
/*    */ import java.awt.Graphics;
/*    */ import java.awt.Graphics2D;
/*    */ import javax.swing.border.LineBorder;
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
/*    */ public class LiquidFocusCellHighlightBorder
/*    */   extends LineBorder
/*    */   implements UIResource
/*    */ {
/* 27 */   static final float[] dash1 = { 1.0F };
/* 28 */   static final BasicStroke dashed = new BasicStroke(1.0F, 0, 0, 10.0F, dash1, 0.0F);
/*    */   
/*    */ 
/*    */   private Color color;
/*    */   
/*    */ 
/*    */ 
/*    */   public LiquidFocusCellHighlightBorder(Color c)
/*    */   {
/* 37 */     super(c);
/* 38 */     this.color = c;
/*    */   }
/*    */   
/*    */   public LiquidFocusCellHighlightBorder(Color c, int thickness)
/*    */   {
/* 43 */     super(c, thickness);
/* 44 */     this.color = c;
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
/*    */ 
/*    */   public void paintBorder(Component c, Graphics g, int x, int y, int w, int h)
/*    */   {
/* 64 */     Color oldColor = g.getColor();
/*    */     
/* 66 */     Graphics2D g2 = (Graphics2D)g;
/* 67 */     g2.setColor(this.color);
/* 68 */     g2.setStroke(dashed);
/* 69 */     g2.drawRect(x, y + 1, w - 1, h - 1);
/* 70 */     g2.setColor(oldColor);
/*    */   }
/*    */ }


/* Location:              E:\java\java学习\hutubill\lib\all.jar!\com\birosoft\liquid\borders\LiquidFocusCellHighlightBorder.class
 * Java compiler version: 4 (48.0)
 * JD-Core Version:       0.7.1
 */
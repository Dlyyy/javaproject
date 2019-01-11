/*    */ package com.birosoft.liquid.borders;
/*    */ 
/*    */ import com.birosoft.liquid.skin.Skin;
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
/*    */ public class LiquidTextFieldBorder
/*    */   extends AbstractBorder
/*    */   implements UIResource
/*    */ {
/* 25 */   private static final Insets defaultInsets = new Insets(3, 5, 3, 5);
/*    */   static Skin skin;
/*    */   private Insets insets;
/*    */   
/*    */   public LiquidTextFieldBorder() {
/* 30 */     this.insets = defaultInsets;
/*    */   }
/*    */   
/*    */   public LiquidTextFieldBorder(Insets insets) {
/* 34 */     this.insets = insets;
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   public Insets getBorderInsets(Component c)
/*    */   {
/* 44 */     return this.insets;
/*    */   }
/*    */   
/*    */   public void setInsets(Insets i) {
/* 48 */     this.insets = i;
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */   public Skin getSkin()
/*    */   {
/* 55 */     if (skin == null) {
/* 56 */       skin = new Skin("textbox.png", 2, 3);
/*    */     }
/*    */     
/* 59 */     return skin;
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */   public void paintBorder(Component c, Graphics g, int x, int y, int w, int h)
/*    */   {
/* 67 */     int index = c.isEnabled() ? 0 : 1;
/* 68 */     getSkin().draw(g, index, w, h);
/*    */   }
/*    */ }


/* Location:              E:\java\java学习\hutubill\lib\all.jar!\com\birosoft\liquid\borders\LiquidTextFieldBorder.class
 * Java compiler version: 4 (48.0)
 * JD-Core Version:       0.7.1
 */
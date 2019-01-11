/*    */ package com.birosoft.liquid;
/*    */ 
/*    */ import com.birosoft.liquid.skin.Skin;
/*    */ import com.birosoft.liquid.util.Colors;
/*    */ import java.awt.Graphics;
/*    */ import javax.swing.JComponent;
/*    */ import javax.swing.border.EmptyBorder;
/*    */ import javax.swing.plaf.ComponentUI;
/*    */ import javax.swing.plaf.basic.BasicMenuBarUI;
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
/*    */ public class LiquidMenuBarUI
/*    */   extends BasicMenuBarUI
/*    */ {
/*    */   static Skin skin;
/*    */   
/*    */   public static ComponentUI createUI(JComponent c)
/*    */   {
/* 42 */     c.setBorder(new EmptyBorder(0, 5, 2, 0));
/* 43 */     return new LiquidMenuBarUI();
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   public void paint(Graphics g, JComponent c)
/*    */   {
/* 54 */     int width = c.getWidth();
/* 55 */     int height = c.getHeight();
/* 56 */     getSkin().draw(g, 2, width, height - 2);
/* 57 */     if (LiquidLookAndFeel.areStipplesUsed()) {
/* 58 */       Colors.drawStipples(g, c, c.getBackground());
/*    */     }
/*    */   }
/*    */   
/*    */   public Skin getSkin()
/*    */   {
/* 64 */     if (skin == null)
/*    */     {
/* 66 */       skin = new Skin("menu_top.png", 3, 0);
/*    */     }
/*    */     
/* 69 */     return skin;
/*    */   }
/*    */ }


/* Location:              E:\java\java学习\hutubill\lib\all.jar!\com\birosoft\liquid\LiquidMenuBarUI.class
 * Java compiler version: 4 (48.0)
 * JD-Core Version:       0.7.1
 */
/*    */ package com.birosoft.liquid;
/*    */ 
/*    */ import com.birosoft.liquid.skin.Skin;
/*    */ import com.birosoft.liquid.skin.SkinToggleButtonIndexModel;
/*    */ import java.awt.Component;
/*    */ import java.awt.Graphics;
/*    */ import java.io.Serializable;
/*    */ import javax.swing.AbstractButton;
/*    */ import javax.swing.Icon;
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
/*    */ public class LiquidCheckBoxIcon
/*    */   implements Icon, UIResource, Serializable
/*    */ {
/*    */   protected static Skin skin;
/* 32 */   private SkinToggleButtonIndexModel indexModel = new SkinToggleButtonIndexModel();
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   protected int getControlSize()
/*    */   {
/* 43 */     return getIconWidth();
/*    */   }
/*    */   
/*    */   public void paintIcon(Component c, Graphics g, int x, int y) {
/* 47 */     AbstractButton button = (AbstractButton)c;
/*    */     
/* 49 */     this.indexModel.setButton(button);
/*    */     
/* 51 */     int index = this.indexModel.getIndexForState();
/* 52 */     getSkin().draw(g, index, x, y, getSkin().getHsize(), getSkin().getVsize());
/*    */   }
/*    */   
/*    */   public int getIconWidth()
/*    */   {
/* 57 */     return getSkin().getHsize();
/*    */   }
/*    */   
/*    */   public int getIconHeight() {
/* 61 */     return getSkin().getVsize();
/*    */   }
/*    */   
/*    */   public Skin getSkin() {
/* 65 */     if (skin == null) {
/* 66 */       skin = new Skin("checkbox.png", 8, 0);
/*    */     }
/*    */     
/* 69 */     return skin;
/*    */   }
/*    */ }


/* Location:              E:\java\java学习\hutubill\lib\all.jar!\com\birosoft\liquid\LiquidCheckBoxIcon.class
 * Java compiler version: 4 (48.0)
 * JD-Core Version:       0.7.1
 */
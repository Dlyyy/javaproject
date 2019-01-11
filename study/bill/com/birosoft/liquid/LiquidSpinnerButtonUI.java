/*     */ package com.birosoft.liquid;
/*     */ 
/*     */ import com.birosoft.liquid.skin.Skin;
/*     */ import com.birosoft.liquid.skin.SkinSimpleButtonIndexModel;
/*     */ import java.awt.Dimension;
/*     */ import java.awt.Graphics;
/*     */ import javax.swing.AbstractButton;
/*     */ import javax.swing.JComponent;
/*     */ import javax.swing.plaf.ComponentUI;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class LiquidSpinnerButtonUI
/*     */   extends LiquidButtonUI
/*     */ {
/*     */   int type;
/*  38 */   private static final String[] arrowfiles = { "spinneruparrows.png", "spinnerdownarrows.png" };
/*     */   
/*     */ 
/*  41 */   private static SkinSimpleButtonIndexModel indexModel = new SkinSimpleButtonIndexModel(0, 1, 2, 3);
/*     */   
/*  43 */   static Skin skin = null;
/*  44 */   static Skin[] arrowSkins = new Skin[2];
/*     */   
/*     */   public static ComponentUI createUI(JComponent c)
/*     */   {
/*  48 */     throw new IllegalStateException("Must not be used this way.");
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   LiquidSpinnerButtonUI(int type)
/*     */   {
/*  58 */     this.type = type;
/*     */   }
/*     */   
/*     */ 
/*     */   public void paint(Graphics g, JComponent c)
/*     */   {
/*  64 */     AbstractButton button = (AbstractButton)c;
/*     */     
/*  66 */     indexModel.setButton(button);
/*  67 */     int index = indexModel.getIndexForState();
/*     */     
/*  69 */     getSkin().draw(g, index, button.getWidth(), button.getHeight());
/*  70 */     getArrowSkin(this.type).drawCentered(g, index, button.getWidth(), button.getHeight());
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static Skin getSkin()
/*     */   {
/*  81 */     if (skin == null)
/*     */     {
/*  83 */       skin = new Skin("spinnerarrowborder.png", 4, 2);
/*     */     }
/*     */     
/*  86 */     return skin;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected Skin getArrowSkin(int type)
/*     */   {
/*  96 */     switch (type)
/*     */     {
/*     */     case 1: 
/*  99 */       arrowSkins[0] = new Skin(arrowfiles[0], 4, 2);
/* 100 */       return arrowSkins[0];
/*     */     case 5: 
/* 102 */       arrowSkins[1] = new Skin(arrowfiles[1], 4, 2);
/* 103 */       return arrowSkins[1];
/*     */     }
/* 105 */     throw new IllegalStateException("type must be either SwingConstants.SOUTH or SwingConstants.NORTH for XPSpinnerButton");
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public Dimension getPreferredSize(JComponent c)
/*     */   {
/* 114 */     return new Dimension(getSkin().getHsize(), getSkin().getVsize());
/*     */   }
/*     */ }


/* Location:              E:\java\java学习\hutubill\lib\all.jar!\com\birosoft\liquid\LiquidSpinnerButtonUI.class
 * Java compiler version: 4 (48.0)
 * JD-Core Version:       0.7.1
 */
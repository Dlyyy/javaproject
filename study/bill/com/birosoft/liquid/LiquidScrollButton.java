/*     */ package com.birosoft.liquid;
/*     */ 
/*     */ import com.birosoft.liquid.skin.Skin;
/*     */ import com.birosoft.liquid.skin.SkinSimpleButtonIndexModel;
/*     */ import java.awt.Dimension;
/*     */ import java.awt.Graphics;
/*     */ import java.awt.Insets;
/*     */ import javax.swing.plaf.basic.BasicArrowButton;
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
/*     */ public class LiquidScrollButton
/*     */   extends BasicArrowButton
/*     */ {
/*  28 */   private boolean isFreeStanding = false;
/*     */   
/*     */   private int buttonWidth;
/*     */   
/*  32 */   private static Skin skinUp = new Skin("scrollbuttonsup.png", 4, 0);
/*     */   
/*  34 */   private static Skin skinDown = new Skin("scrollbuttonsdown.png", 4, 0);
/*     */   
/*  36 */   private static Skin skinLeft = new Skin("scrollbuttonsleft.png", 4, 0);
/*     */   
/*  38 */   private static Skin skinRight = new Skin("scrollbuttonsright.png", 4, 0);
/*     */   
/*     */ 
/*     */   private Skin skin;
/*     */   
/*  43 */   private SkinSimpleButtonIndexModel indexModel = new SkinSimpleButtonIndexModel();
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public LiquidScrollButton(int orientation, int width, boolean freeStanding)
/*     */   {
/*  51 */     super(orientation);
/*  52 */     this.buttonWidth = width;
/*  53 */     this.isFreeStanding = freeStanding;
/*  54 */     putClientProperty("JScrollBar.isFreeStanding", this.isFreeStanding ? Boolean.TRUE : Boolean.FALSE);
/*     */     
/*  56 */     setBorder(null);
/*  57 */     setRolloverEnabled(true);
/*  58 */     setMargin(new Insets(0, 0, 0, 0));
/*  59 */     setBorder(null);
/*  60 */     this.indexModel.setButton(this);
/*     */     
/*  62 */     switch (orientation)
/*     */     {
/*     */     case 1: 
/*  65 */       this.skin = skinUp;
/*  66 */       break;
/*     */     case 5: 
/*  68 */       this.skin = skinDown;
/*  69 */       break;
/*     */     case 3: 
/*  71 */       this.skin = skinRight;
/*  72 */       break;
/*     */     case 7: 
/*  74 */       this.skin = skinLeft;
/*     */     }
/*     */     
/*     */   }
/*     */   
/*     */   public void setFreeStanding(boolean freeStanding)
/*     */   {
/*  81 */     this.isFreeStanding = freeStanding;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void paint(Graphics g)
/*     */   {
/*  90 */     this.skin.draw(g, this.indexModel.getIndexForState(), getWidth(), getHeight());
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public Dimension getPreferredSize()
/*     */   {
/*  99 */     return new Dimension(skinUp.getHsize(), skinUp.getVsize());
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static Skin getSkinUp()
/*     */   {
/* 108 */     return skinUp;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static Skin getSkinDown()
/*     */   {
/* 117 */     return skinDown;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static Skin getSkinLeft()
/*     */   {
/* 126 */     return skinLeft;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static Skin getSkinRight()
/*     */   {
/* 135 */     return skinRight;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public Skin getSkin()
/*     */   {
/* 144 */     return this.skin;
/*     */   }
/*     */ }


/* Location:              E:\java\java学习\hutubill\lib\all.jar!\com\birosoft\liquid\LiquidScrollButton.class
 * Java compiler version: 4 (48.0)
 * JD-Core Version:       0.7.1
 */
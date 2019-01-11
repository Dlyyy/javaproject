/*     */ package com.birosoft.liquid.skin;
/*     */ 
/*     */ import javax.swing.AbstractButton;
/*     */ import javax.swing.ButtonModel;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class SkinSimpleButtonIndexModel
/*     */ {
/*     */   private AbstractButton button;
/*     */   private int normal;
/*     */   private int rollover;
/*     */   private int pressed;
/*     */   private int disabled;
/*     */   
/*     */   public SkinSimpleButtonIndexModel()
/*     */   {
/*  43 */     this.normal = 0;
/*  44 */     this.rollover = 1;
/*  45 */     this.pressed = 2;
/*  46 */     this.disabled = 3;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public SkinSimpleButtonIndexModel(int normal, int rollover, int pressed, int disabled)
/*     */   {
/*  59 */     this.normal = normal;
/*  60 */     this.rollover = rollover;
/*  61 */     this.pressed = pressed;
/*  62 */     this.disabled = disabled;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public int getIndexForState()
/*     */   {
/*  73 */     if (!this.button.isEnabled())
/*  74 */       return this.disabled;
/*  75 */     if (this.button.getModel().isPressed())
/*  76 */       return this.pressed;
/*  77 */     if (this.button.getModel().isRollover()) {
/*  78 */       return this.rollover;
/*     */     }
/*  80 */     return this.normal;
/*     */   }
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
/*     */   public int getIndexForState(boolean isEnabled, boolean isRollover, boolean isPressed)
/*     */   {
/*  94 */     if (!isEnabled)
/*  95 */       return this.disabled;
/*  96 */     if (isPressed)
/*  97 */       return this.pressed;
/*  98 */     if (isRollover) {
/*  99 */       return this.rollover;
/*     */     }
/* 101 */     return this.normal;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public AbstractButton getButton()
/*     */   {
/* 109 */     return this.button;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setButton(AbstractButton button)
/*     */   {
/* 117 */     this.button = button;
/*     */   }
/*     */ }


/* Location:              E:\java\java学习\hutubill\lib\all.jar!\com\birosoft\liquid\skin\SkinSimpleButtonIndexModel.class
 * Java compiler version: 4 (48.0)
 * JD-Core Version:       0.7.1
 */
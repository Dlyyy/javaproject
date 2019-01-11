/*     */ package com.birosoft.liquid.skin;
/*     */ 
/*     */ import javax.swing.AbstractButton;
/*     */ import javax.swing.ButtonModel;
/*     */ import javax.swing.JButton;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class SkinToggleButtonIndexModel
/*     */ {
/*     */   private AbstractButton button;
/*     */   private int normal;
/*     */   private int rollover;
/*     */   private int pressed;
/*     */   private int disabled;
/*     */   private int selected;
/*     */   private int selectedRollover;
/*     */   private int selectedPressed;
/*     */   private int selectedDisabled;
/*     */   private int defaultButton;
/*     */   boolean checkForDefaultButton;
/*     */   
/*     */   public SkinToggleButtonIndexModel()
/*     */   {
/*  58 */     this.normal = 0;
/*  59 */     this.rollover = 1;
/*  60 */     this.pressed = 2;
/*  61 */     this.disabled = 3;
/*     */     
/*  63 */     this.selected = 4;
/*  64 */     this.selectedRollover = 5;
/*  65 */     this.selectedPressed = 6;
/*  66 */     this.selectedDisabled = 7;
/*     */     
/*  68 */     this.defaultButton = 8;
/*  69 */     this.checkForDefaultButton = false;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public SkinToggleButtonIndexModel(boolean checkForDefaultButton)
/*     */   {
/*  81 */     this();
/*  82 */     this.checkForDefaultButton = checkForDefaultButton;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public SkinToggleButtonIndexModel(int normal, int rollover, int pressed, int disabled, int selected, int selectedRollover, int selectedPressed, int selectedDisabled)
/*     */   {
/*  92 */     this.normal = normal;
/*  93 */     this.rollover = rollover;
/*  94 */     this.pressed = pressed;
/*  95 */     this.disabled = disabled;
/*     */     
/*  97 */     this.selected = selected;
/*  98 */     this.selectedRollover = selectedRollover;
/*  99 */     this.selectedPressed = selectedPressed;
/* 100 */     this.selectedDisabled = selectedDisabled;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public int getIndexForState()
/*     */   {
/* 107 */     if (this.button == null) return 0;
/* 108 */     if (!this.button.isSelected())
/*     */     {
/* 110 */       if (!this.button.isEnabled())
/* 111 */         return this.disabled;
/* 112 */       if (this.button.getModel().isPressed())
/* 113 */         return this.pressed;
/* 114 */       if (this.button.getModel().isRollover())
/* 115 */         return this.rollover;
/* 116 */       if (this.checkForDefaultButton)
/*     */       {
/* 118 */         JButton jb = null;
/* 119 */         if ((this.button instanceof JButton))
/*     */         {
/* 121 */           jb = (JButton)this.button;
/* 122 */           if (jb.isDefaultButton())
/*     */           {
/* 124 */             return this.defaultButton;
/*     */           }
/*     */         }
/*     */       }
/* 128 */       return this.normal;
/*     */     }
/*     */     
/*     */ 
/* 132 */     if (!this.button.isEnabled())
/* 133 */       return this.selectedDisabled;
/* 134 */     if (this.button.getModel().isPressed())
/* 135 */       return this.selectedPressed;
/* 136 */     if (this.button.getModel().isRollover())
/* 137 */       return this.selectedRollover;
/* 138 */     return this.selected;
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
/*     */ 
/*     */   public int getIndexForState(boolean isSelected, boolean isEnabled, boolean isPressed, boolean isRollover)
/*     */   {
/* 153 */     if (!isSelected)
/*     */     {
/* 155 */       if (!isEnabled)
/* 156 */         return this.disabled;
/* 157 */       if (isPressed)
/* 158 */         return this.pressed;
/* 159 */       if (isRollover)
/* 160 */         return this.rollover;
/* 161 */       return this.normal;
/*     */     }
/*     */     
/*     */ 
/* 165 */     if (!isEnabled)
/* 166 */       return this.selectedDisabled;
/* 167 */     if (isPressed)
/* 168 */       return this.selectedPressed;
/* 169 */     if (isRollover)
/* 170 */       return this.selectedRollover;
/* 171 */     return this.selected;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public AbstractButton getButton()
/*     */   {
/* 181 */     return this.button;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setButton(AbstractButton button)
/*     */   {
/* 190 */     this.button = button;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean isCheckForDefaultButton()
/*     */   {
/* 199 */     return this.checkForDefaultButton;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setCheckForDefaultButton(boolean hasToggleButton)
/*     */   {
/* 208 */     this.checkForDefaultButton = hasToggleButton;
/*     */   }
/*     */ }


/* Location:              E:\java\java学习\hutubill\lib\all.jar!\com\birosoft\liquid\skin\SkinToggleButtonIndexModel.class
 * Java compiler version: 4 (48.0)
 * JD-Core Version:       0.7.1
 */
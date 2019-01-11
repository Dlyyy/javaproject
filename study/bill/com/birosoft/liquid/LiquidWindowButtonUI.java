/*     */ package com.birosoft.liquid;
/*     */ 
/*     */ import com.birosoft.liquid.skin.Skin;
/*     */ import com.birosoft.liquid.skin.SkinSimpleButtonIndexModel;
/*     */ import java.awt.Dimension;
/*     */ import java.awt.Graphics;
/*     */ import java.awt.Rectangle;
/*     */ import java.awt.Window;
/*     */ import javax.swing.AbstractButton;
/*     */ import javax.swing.JComponent;
/*     */ import javax.swing.JInternalFrame;
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
/*     */ public class LiquidWindowButtonUI
/*     */   extends LiquidButtonUI
/*     */ {
/*     */   public static final int CLOSE = 0;
/*     */   public static final int MAXIMIZE = 1;
/*     */   public static final int MINIMIZE = 2;
/*     */   public static final int RESTORE = 3;
/*     */   public static final int SYSMENU = 4;
/*  43 */   private static final String[] files = { "closebutton.png", "maximizebutton.png", "minimizebutton.png", "restorebutton.png" };
/*     */   
/*     */ 
/*     */ 
/*  47 */   private static final String[] pantherFiles = { "panther-closebutton.png", "panther-maximizebutton.png", "panther-minimizebutton.png", "panther-restorebutton.png", "menu-button.png" };
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*  54 */   private static SkinSimpleButtonIndexModel indexModel = new SkinSimpleButtonIndexModel(0, 1, 2, 4);
/*     */   
/*  56 */   static Skin[] skins = new Skin[5];
/*  57 */   boolean isRestore = true;
/*     */   int type;
/*     */   
/*     */   LiquidWindowButtonUI(int type) {
/*  61 */     this.type = type;
/*     */   }
/*     */   
/*     */   public static ComponentUI createUI(JComponent c) {
/*  65 */     throw new IllegalStateException("Must not be used this way.");
/*     */   }
/*     */   
/*     */ 
/*     */   protected void paintFocus(Graphics g, AbstractButton b, Rectangle viewRect, Rectangle textRect, Rectangle iconRect) {}
/*     */   
/*     */   public void paint(Graphics g, JComponent c)
/*     */   {
/*  73 */     SpecialUIButton button = (SpecialUIButton)c;
/*     */     
/*  75 */     indexModel.setButton(button);
/*     */     
/*  77 */     int index = indexModel.getIndexForState();
/*     */     
/*  79 */     if (button.frame != null) {
/*  80 */       if ((LiquidLookAndFeel.winDecoPanther) && (!button.frame.isSelected()) && (index == 0))
/*     */       {
/*  82 */         index = 1;
/*     */       }
/*     */       
/*  85 */       if (((button.frame.isMaximum()) && (this.type == 1)) || ((button.frame.isIcon()) && (this.type == 2)))
/*     */       {
/*  87 */         getSkin(3).draw(g, index, button.getWidth(), button.getHeight());
/*     */       }
/*     */       else {
/*  90 */         getSkin(this.type).draw(g, index, button.getWidth(), button.getHeight());
/*     */       }
/*     */     }
/*     */     else {
/*  94 */       if ((LiquidLookAndFeel.winDecoPanther) && (!button.window.isActive()) && (index == 0))
/*     */       {
/*  96 */         index = 4;
/*     */       }
/*     */       
/*  99 */       getSkin(this.type).draw(g, index, button.getWidth(), button.getHeight());
/*     */     }
/*     */   }
/*     */   
/*     */   protected static Skin getSkin(int type) {
/* 104 */     if (skins[type] == null) {
/* 105 */       if (LiquidLookAndFeel.winDecoPanther) {
/* 106 */         skins[type] = new Skin(pantherFiles[type], 5, 0);
/*     */       } else {
/* 108 */         skins[type] = new Skin(files[type], 5, 2);
/*     */       }
/*     */     }
/*     */     
/* 112 */     return skins[type];
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static LiquidWindowButtonUI createButtonUIForType(int type)
/*     */   {
/* 121 */     return new LiquidWindowButtonUI(type);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public Dimension getPreferredSize(JComponent c)
/*     */   {
/* 128 */     return new Dimension(getSkin(this.type).getHsize(), getSkin(this.type).getVsize());
/*     */   }
/*     */ }


/* Location:              E:\java\java学习\hutubill\lib\all.jar!\com\birosoft\liquid\LiquidWindowButtonUI.class
 * Java compiler version: 4 (48.0)
 * JD-Core Version:       0.7.1
 */
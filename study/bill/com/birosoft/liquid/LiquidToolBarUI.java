/*     */ package com.birosoft.liquid;
/*     */ 
/*     */ import com.birosoft.liquid.skin.Skin;
/*     */ import com.birosoft.liquid.util.Colors;
/*     */ import java.awt.Component;
/*     */ import java.awt.Graphics;
/*     */ import java.awt.Insets;
/*     */ import javax.swing.AbstractButton;
/*     */ import javax.swing.JComponent;
/*     */ import javax.swing.JPanel;
/*     */ import javax.swing.JToolBar;
/*     */ import javax.swing.border.Border;
/*     */ import javax.swing.border.EmptyBorder;
/*     */ import javax.swing.plaf.ComponentUI;
/*     */ import javax.swing.plaf.basic.BasicBorders.MarginBorder;
/*     */ import javax.swing.plaf.basic.BasicToolBarUI;
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
/*     */ public class LiquidToolBarUI
/*     */   extends BasicToolBarUI
/*     */ {
/*  36 */   private Border border = new EmptyBorder(4, 4, 4, 4);
/*  37 */   private int orientation = -1;
/*  38 */   private boolean changeBorder = true;
/*     */   
/*     */ 
/*  41 */   private Skin vbarHandler = new Skin("vtoolbarhandler.png", 1, 8, 3, 8, 3);
/*  42 */   private Skin hbarHandler = new Skin("htoolbarhandler.png", 1, 3, 8, 3, 8);
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*  47 */   private Insets insets = new Insets(2, 2, 2, 2);
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static ComponentUI createUI(JComponent c)
/*     */   {
/*  56 */     return new LiquidToolBarUI();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void installUI(JComponent c)
/*     */   {
/*  66 */     super.installUI(c);
/*  67 */     c.putClientProperty("JToolBar.isRollover", Boolean.TRUE);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void paint(Graphics g, JComponent c)
/*     */   {
/*  77 */     if (LiquidLookAndFeel.panelTransparency) {
/*  78 */       if ((LiquidLookAndFeel.areStipplesUsed()) && ((c.getParent() instanceof JPanel)))
/*     */       {
/*  80 */         c.setOpaque(false);
/*     */       } else {
/*  82 */         c.setOpaque(true);
/*     */       }
/*     */     }
/*     */     
/*     */ 
/*  87 */     if ((c.isOpaque()) && (LiquidLookAndFeel.areStipplesUsed())) {
/*  88 */       Colors.drawStipples(g, c, c.getBackground());
/*     */     }
/*     */     
/*  91 */     if (!isFloating()) {
/*  92 */       if (this.toolBar.getOrientation() != this.orientation) {
/*  93 */         if (this.toolBar.getOrientation() == 0) {
/*  94 */           if (this.toolBar.isFloatable()) {
/*  95 */             this.toolBar.setBorder(new EmptyBorder(2, 11, 2, 2));
/*     */           } else {
/*  97 */             this.toolBar.setBorder(new EmptyBorder(2, 2, 2, 2));
/*     */           }
/*     */         }
/* 100 */         else if (this.toolBar.isFloatable()) {
/* 101 */           this.toolBar.setBorder(new EmptyBorder(12, 2, 2, 2));
/*     */         } else {
/* 103 */           this.toolBar.setBorder(new EmptyBorder(2, 2, 2, 2));
/*     */         }
/*     */         
/*     */ 
/* 107 */         this.orientation = this.toolBar.getOrientation();
/* 108 */         this.changeBorder = true;
/*     */       }
/*     */       
/* 111 */       if (this.toolBar.getOrientation() == 0) {
/* 112 */         if (this.toolBar.isFloatable()) {
/* 113 */           this.vbarHandler.draw(g, 0, 1, 2, 8, c.getHeight() - 4);
/*     */         }
/*     */       }
/* 116 */       else if (this.toolBar.isFloatable()) {
/* 117 */         this.hbarHandler.draw(g, 0, 1, 2, c.getWidth() - 4, 8);
/*     */       }
/*     */       
/*     */     }
/* 121 */     else if (this.changeBorder) {
/* 122 */       this.toolBar.setBorder(new EmptyBorder(1, 1, 1, 1));
/* 123 */       this.changeBorder = false;
/* 124 */       this.orientation = -1;
/*     */     }
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
/*     */   protected void setBorderToRollover(Component c)
/*     */   {
/* 190 */     if ((c instanceof AbstractButton)) {
/* 191 */       AbstractButton b = (AbstractButton)c;
/* 192 */       if ((b.getBorder() instanceof BasicBorders.MarginBorder)) {
/* 193 */         b.setBorder(this.border);
/*     */       }
/*     */       
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 200 */       if (LiquidLookAndFeel.toolbarButtonsFocusable) {
/* 201 */         b.setFocusable(true);
/*     */       }
/*     */       else {
/* 204 */         b.setFocusable(false);
/*     */       }
/* 206 */       b.putClientProperty("JToolBar.isToolbarButton", Boolean.TRUE);
/*     */     }
/*     */   }
/*     */   
/*     */   protected void setBorderToNormal(Component c) {
/* 211 */     if ((c instanceof AbstractButton)) {
/* 212 */       AbstractButton b = (AbstractButton)c;
/* 213 */       if ((b.getBorder() instanceof BasicBorders.MarginBorder)) {
/* 214 */         b.setBorder(this.border);
/*     */       }
/*     */       
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 221 */       if (LiquidLookAndFeel.toolbarButtonsFocusable) {
/* 222 */         b.setFocusable(true);
/*     */       }
/*     */       else {
/* 225 */         b.setFocusable(false);
/*     */       }
/* 227 */       b.putClientProperty("JToolBar.isToolbarButton", Boolean.TRUE);
/*     */     }
/*     */   }
/*     */ }


/* Location:              E:\java\java学习\hutubill\lib\all.jar!\com\birosoft\liquid\LiquidToolBarUI.class
 * Java compiler version: 4 (48.0)
 * JD-Core Version:       0.7.1
 */
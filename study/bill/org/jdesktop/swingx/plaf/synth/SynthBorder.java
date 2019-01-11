/*     */ package org.jdesktop.swingx.plaf.synth;
/*     */ 
/*     */ import java.awt.Component;
/*     */ import java.awt.Graphics;
/*     */ import java.awt.Insets;
/*     */ import javax.swing.JComponent;
/*     */ import javax.swing.border.AbstractBorder;
/*     */ import javax.swing.plaf.UIResource;
/*     */ import javax.swing.plaf.synth.SynthContext;
/*     */ import javax.swing.plaf.synth.SynthStyle;
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
/*     */ class SynthBorder
/*     */   extends AbstractBorder
/*     */   implements UIResource
/*     */ {
/*     */   private SynthUI ui;
/*     */   private Insets insets;
/*     */   
/*     */   SynthBorder(SynthUI ui, Insets insets)
/*     */   {
/*  55 */     this.ui = ui;
/*  56 */     this.insets = insets;
/*     */   }
/*     */   
/*     */   SynthBorder(SynthUI ui) {
/*  60 */     this(ui, null);
/*     */   }
/*     */   
/*     */ 
/*     */   public void paintBorder(Component c, Graphics g, int x, int y, int width, int height)
/*     */   {
/*  66 */     JComponent jc = (JComponent)c;
/*  67 */     SynthContext context = this.ui.getContext(jc);
/*  68 */     SynthStyle style = context.getStyle();
/*  69 */     if (style == null) {
/*  70 */       if (!$assertionsDisabled) { throw new AssertionError("SynthBorder is being used outside after the UI has been uninstalled");
/*     */       }
/*  72 */       return;
/*     */     }
/*  74 */     this.ui.paintBorder(context, g, x, y, width, height);
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
/*     */   public Insets getBorderInsets(Component c)
/*     */   {
/*  87 */     return getBorderInsets(c, null);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public Insets getBorderInsets(Component c, Insets insets)
/*     */   {
/*  98 */     if (this.insets != null) {
/*  99 */       if (insets == null) {
/* 100 */         insets = new Insets(this.insets.top, this.insets.left, this.insets.bottom, this.insets.right);
/*     */       }
/*     */       else
/*     */       {
/* 104 */         insets.top = this.insets.top;
/* 105 */         insets.bottom = this.insets.bottom;
/* 106 */         insets.left = this.insets.left;
/* 107 */         insets.right = this.insets.right;
/*     */       }
/*     */     }
/* 110 */     else if (insets == null) {
/* 111 */       insets = new Insets(0, 0, 0, 0);
/*     */     }
/*     */     else {
/* 114 */       insets.top = (insets.bottom = insets.left = insets.right = 0);
/*     */     }
/* 116 */     return insets;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean isBorderOpaque()
/*     */   {
/* 125 */     return false;
/*     */   }
/*     */ }


/* Location:              E:\java\java学习\hutubill\lib\all.jar!\org\jdesktop\swingx\plaf\synth\SynthBorder.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */
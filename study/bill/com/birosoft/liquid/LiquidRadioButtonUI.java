/*     */ package com.birosoft.liquid;
/*     */ 
/*     */ import java.awt.BasicStroke;
/*     */ import java.awt.Color;
/*     */ import java.awt.Dimension;
/*     */ import java.awt.Graphics;
/*     */ import java.awt.Graphics2D;
/*     */ import java.awt.Rectangle;
/*     */ import java.beans.PropertyChangeEvent;
/*     */ import java.beans.PropertyChangeListener;
/*     */ import javax.swing.AbstractButton;
/*     */ import javax.swing.JComponent;
/*     */ import javax.swing.JRadioButton;
/*     */ import javax.swing.plaf.ComponentUI;
/*     */ import javax.swing.plaf.basic.BasicRadioButtonUI;
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
/*     */ public class LiquidRadioButtonUI
/*     */   extends BasicRadioButtonUI
/*     */ {
/*  32 */   private static final LiquidRadioButtonUI metouiaRadioButtonUI = new LiquidRadioButtonUI();
/*     */   
/*  34 */   private static BasicStroke focusStroke = new BasicStroke(1.0F, 0, 2, 1.0F, new float[] { 1.0F, 1.0F }, 0.0F);
/*     */   
/*     */ 
/*     */ 
/*     */   private static LiquidRadioButtonIcon skinnedIcon;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public static ComponentUI createUI(JComponent c)
/*     */   {
/*  45 */     if ((c instanceof JRadioButton))
/*     */     {
/*  47 */       JRadioButton jb = (JRadioButton)c;
/*  48 */       jb.setRolloverEnabled(true);
/*     */       
/*  50 */       jb.addPropertyChangeListener("opaque", new PropertyChangeListener() {
/*     */         private final JRadioButton val$jb;
/*     */         
/*     */         public void propertyChange(PropertyChangeEvent evt) {
/*  54 */           this.val$jb.setOpaque(false);
/*     */         }
/*     */       });
/*     */     }
/*     */     
/*  59 */     return metouiaRadioButtonUI;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void installUI(JComponent arg0)
/*     */   {
/*  68 */     super.installUI(arg0);
/*  69 */     this.icon = getSkinnedIcon();
/*     */   }
/*     */   
/*     */   protected void installListeners(AbstractButton b) {
/*  73 */     super.installListeners(b);
/*     */   }
/*     */   
/*     */   protected void uninstallListeners(AbstractButton b)
/*     */   {
/*  78 */     super.uninstallListeners(b);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected LiquidRadioButtonIcon getSkinnedIcon()
/*     */   {
/*  88 */     if (skinnedIcon == null)
/*  89 */       skinnedIcon = new LiquidRadioButtonIcon();
/*  90 */     return skinnedIcon;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected void paintFocus(Graphics g, Rectangle t, Dimension arg2)
/*     */   {
/* 100 */     Graphics2D g2d = (Graphics2D)g;
/* 101 */     g2d.setColor(Color.black);
/*     */     
/* 103 */     g2d.setStroke(focusStroke);
/* 104 */     g2d.drawLine(t.x - 1, t.y - 1, t.x - 1 + t.width + 1, t.y - 1);
/* 105 */     g2d.drawLine(t.x - 1, t.y - 1 + t.height + 1, t.x - 1 + t.width + 1, t.y - 1 + t.height + 1);
/* 106 */     g2d.drawLine(t.x - 1, t.y - 1, t.x - 1, t.y - 1 + t.height + 1);
/* 107 */     g2d.drawLine(t.x - 1 + t.width + 1, t.y - 1, t.x - 1 + t.width + 1, t.y - 1 + t.height + 1);
/*     */   }
/*     */ }


/* Location:              E:\java\java学习\hutubill\lib\all.jar!\com\birosoft\liquid\LiquidRadioButtonUI.class
 * Java compiler version: 4 (48.0)
 * JD-Core Version:       0.7.1
 */
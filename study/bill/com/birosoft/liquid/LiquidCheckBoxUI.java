/*     */ package com.birosoft.liquid;
/*     */ 
/*     */ import java.awt.BasicStroke;
/*     */ import java.awt.Color;
/*     */ import java.awt.Dimension;
/*     */ import java.awt.Font;
/*     */ import java.awt.FontMetrics;
/*     */ import java.awt.Graphics;
/*     */ import java.awt.Graphics2D;
/*     */ import java.awt.Insets;
/*     */ import java.awt.Rectangle;
/*     */ import javax.swing.AbstractButton;
/*     */ import javax.swing.ButtonModel;
/*     */ import javax.swing.CellRendererPane;
/*     */ import javax.swing.Icon;
/*     */ import javax.swing.JComponent;
/*     */ import javax.swing.SwingUtilities;
/*     */ import javax.swing.plaf.ComponentUI;
/*     */ import javax.swing.plaf.basic.BasicCheckBoxUI;
/*     */ import javax.swing.text.View;
/*     */ 
/*     */ 
/*     */ 
/*     */ public class LiquidCheckBoxUI
/*     */   extends BasicCheckBoxUI
/*     */ {
/*  27 */   private static Dimension size = new Dimension();
/*  28 */   private static Rectangle viewRect = new Rectangle();
/*  29 */   private static Rectangle iconRect = new Rectangle();
/*  30 */   private static Rectangle textRect = new Rectangle();
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*  35 */   private static final LiquidCheckBoxUI checkBoxUI = new LiquidCheckBoxUI();
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*  43 */   static LiquidCheckBoxIcon skinnedIcon = new LiquidCheckBoxIcon();
/*  44 */   static BasicStroke focusStroke = new BasicStroke(1.0F, 0, 2, 1.0F, new float[] { 1.0F }, 1.0F);
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static ComponentUI createUI(JComponent c)
/*     */   {
/*  55 */     return checkBoxUI;
/*     */   }
/*     */   
/*     */   public void installDefaults(AbstractButton button) {
/*  59 */     super.installDefaults(button);
/*  60 */     this.icon = skinnedIcon;
/*  61 */     button.setRolloverEnabled(true);
/*     */   }
/*     */   
/*     */   protected void paintFocus(Graphics g, Rectangle t, Dimension arg2) {
/*  65 */     Graphics2D g2d = (Graphics2D)g;
/*  66 */     g2d.setColor(Color.black);
/*  67 */     g2d.setStroke(focusStroke);
/*     */     
/*     */ 
/*  70 */     g2d.drawLine(t.x - 1, t.y - 1, t.x - 1 + t.width + 1, t.y - 1);
/*  71 */     g2d.drawLine(t.x - 1, t.y - 1 + t.height + 1, t.x - 1 + t.width + 1, t.y - 1 + t.height + 1);
/*     */     
/*  73 */     g2d.drawLine(t.x - 1, t.y - 1, t.x - 1, t.y - 1 + t.height + 1);
/*  74 */     g2d.drawLine(t.x - 1 + t.width + 1, t.y - 1, t.x - 1 + t.width + 1, t.y - 1 + t.height + 1);
/*     */   }
/*     */   
/*     */   public synchronized void paint(Graphics g, JComponent c)
/*     */   {
/*  79 */     AbstractButton b = (AbstractButton)c;
/*  80 */     ButtonModel model = b.getModel();
/*     */     
/*  82 */     Font f = c.getFont();
/*  83 */     g.setFont(f);
/*     */     
/*  85 */     FontMetrics fm = g.getFontMetrics();
/*     */     
/*  87 */     Insets i = c.getInsets();
/*  88 */     size = b.getSize(size);
/*  89 */     viewRect.x = i.left;
/*  90 */     viewRect.y = i.top;
/*  91 */     viewRect.width = (size.width - (i.right + viewRect.x));
/*  92 */     viewRect.height = (size.height - (i.bottom + viewRect.y));
/*  93 */     iconRect.x = (iconRect.y = iconRect.width = iconRect.height = 0);
/*  94 */     textRect.x = (textRect.y = textRect.width = textRect.height = 0);
/*     */     
/*  96 */     Icon altIcon = b.getIcon();
/*  97 */     Icon selectedIcon = null;
/*  98 */     Icon disabledIcon = null;
/*     */     
/* 100 */     String text = SwingUtilities.layoutCompoundLabel(c, fm, b.getText(), altIcon != null ? altIcon : getDefaultIcon(), b.getVerticalAlignment(), b.getHorizontalAlignment(), b.getVerticalTextPosition(), b.getHorizontalTextPosition(), viewRect, iconRect, textRect, b.getText() == null ? 0 : b.getIconTextGap());
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 107 */     if ((c.getParent() instanceof CellRendererPane)) {
/* 108 */       c.setOpaque(true);
/* 109 */     } else if (c.isOpaque()) {
/* 110 */       c.setOpaque(false);
/* 111 */       c.repaint();
/*     */     }
/*     */     
/*     */ 
/* 115 */     if (altIcon != null) {
/* 116 */       if (!model.isEnabled()) {
/* 117 */         if (model.isSelected()) {
/* 118 */           altIcon = b.getDisabledSelectedIcon();
/*     */         } else {
/* 120 */           altIcon = b.getDisabledIcon();
/*     */         }
/* 122 */       } else if ((model.isPressed()) && (model.isArmed())) {
/* 123 */         altIcon = b.getPressedIcon();
/*     */         
/* 125 */         if (altIcon == null)
/*     */         {
/* 127 */           altIcon = b.getSelectedIcon();
/*     */         }
/* 129 */       } else if (model.isSelected()) {
/* 130 */         if ((b.isRolloverEnabled()) && (model.isRollover())) {
/* 131 */           altIcon = b.getRolloverSelectedIcon();
/*     */           
/* 133 */           if (altIcon == null) {
/* 134 */             altIcon = b.getSelectedIcon();
/*     */           }
/*     */         } else {
/* 137 */           altIcon = b.getSelectedIcon();
/*     */         }
/* 139 */       } else if ((b.isRolloverEnabled()) && (model.isRollover())) {
/* 140 */         altIcon = b.getRolloverIcon();
/*     */       }
/*     */       
/* 143 */       if (altIcon == null) {
/* 144 */         altIcon = b.getIcon();
/*     */       }
/*     */       
/* 147 */       altIcon.paintIcon(c, g, iconRect.x, iconRect.y);
/*     */     } else {
/* 149 */       getDefaultIcon().paintIcon(c, g, iconRect.x, iconRect.y);
/*     */     }
/*     */     
/*     */ 
/* 153 */     if (text != null) {
/* 154 */       View v = (View)c.getClientProperty("html");
/*     */       
/* 156 */       if (v != null) {
/* 157 */         v.paint(g, textRect);
/*     */       } else {
/* 159 */         paintText(g, b, textRect, text);
/*     */         
/* 161 */         if ((b.hasFocus()) && (b.isFocusPainted()) && (textRect.width > 0) && (textRect.height > 0))
/*     */         {
/* 163 */           paintFocus(g, textRect, size);
/*     */         }
/*     */       }
/*     */     }
/*     */   }
/*     */ }


/* Location:              E:\java\java学习\hutubill\lib\all.jar!\com\birosoft\liquid\LiquidCheckBoxUI.class
 * Java compiler version: 4 (48.0)
 * JD-Core Version:       0.7.1
 */
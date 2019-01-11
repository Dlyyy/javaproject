/*     */ package org.jdesktop.swingx.plaf.windows;
/*     */ 
/*     */ import java.awt.BorderLayout;
/*     */ import java.awt.Color;
/*     */ import java.awt.Component;
/*     */ import java.awt.Dimension;
/*     */ import java.awt.Font;
/*     */ import java.awt.Graphics;
/*     */ import java.awt.Insets;
/*     */ import javax.swing.BorderFactory;
/*     */ import javax.swing.JComponent;
/*     */ import javax.swing.JDialog;
/*     */ import javax.swing.JLabel;
/*     */ import javax.swing.JPanel;
/*     */ import javax.swing.UIManager;
/*     */ import javax.swing.border.Border;
/*     */ import javax.swing.border.CompoundBorder;
/*     */ import javax.swing.plaf.ComponentUI;
/*     */ import org.jdesktop.swingx.JXTipOfTheDay;
/*     */ import org.jdesktop.swingx.JXTipOfTheDay.ShowOnStartupChoice;
/*     */ import org.jdesktop.swingx.plaf.UIManagerExt;
/*     */ import org.jdesktop.swingx.plaf.basic.BasicTipOfTheDayUI;
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
/*     */ public class WindowsTipOfTheDayUI
/*     */   extends BasicTipOfTheDayUI
/*     */ {
/*     */   public static ComponentUI createUI(JComponent c)
/*     */   {
/*  54 */     return new WindowsTipOfTheDayUI((JXTipOfTheDay)c);
/*     */   }
/*     */   
/*     */   public WindowsTipOfTheDayUI(JXTipOfTheDay tipPane) {
/*  58 */     super(tipPane);
/*     */   }
/*     */   
/*     */ 
/*     */   public JDialog createDialog(Component parentComponent, JXTipOfTheDay.ShowOnStartupChoice choice)
/*     */   {
/*  64 */     return createDialog(parentComponent, choice, false);
/*     */   }
/*     */   
/*     */   protected void installComponents()
/*     */   {
/*  69 */     this.tipPane.setLayout(new BorderLayout());
/*     */     
/*     */ 
/*  72 */     JLabel tipIcon = new JLabel();
/*  73 */     tipIcon.setPreferredSize(new Dimension(60, 100));
/*  74 */     tipIcon.setIcon(UIManager.getIcon("TipOfTheDay.icon"));
/*  75 */     tipIcon.setHorizontalAlignment(0);
/*  76 */     tipIcon.setVerticalAlignment(1);
/*  77 */     tipIcon.setBorder(BorderFactory.createEmptyBorder(24, 0, 0, 0));
/*  78 */     this.tipPane.add("West", tipIcon);
/*     */     
/*     */ 
/*  81 */     JPanel rightPane = new JPanel(new BorderLayout());
/*  82 */     JLabel didYouKnow = new JLabel(UIManagerExt.getString("TipOfTheDay.didYouKnowText", this.tipPane.getLocale()));
/*     */     
/*  84 */     didYouKnow.setPreferredSize(new Dimension(50, 32));
/*  85 */     didYouKnow.setOpaque(true);
/*  86 */     didYouKnow.setBackground(UIManager.getColor("TextArea.background"));
/*  87 */     didYouKnow.setBorder(new CompoundBorder(BorderFactory.createMatteBorder(0, 0, 2, 0, this.tipPane.getBackground()), BorderFactory.createEmptyBorder(4, 4, 4, 4)));
/*     */     
/*     */ 
/*  90 */     didYouKnow.setFont(this.tipPane.getFont().deriveFont(1, 15.0F));
/*  91 */     rightPane.add("North", didYouKnow);
/*     */     
/*  93 */     this.tipArea = new JPanel(new BorderLayout());
/*  94 */     this.tipArea.setOpaque(true);
/*  95 */     this.tipArea.setBorder(BorderFactory.createEmptyBorder(4, 4, 4, 4));
/*  96 */     this.tipArea.setBackground(UIManager.getColor("TextArea.background"));
/*  97 */     rightPane.add("Center", this.tipArea);
/*     */     
/*  99 */     this.tipPane.add("Center", rightPane);
/*     */   }
/*     */   
/*     */   public static class TipAreaBorder implements Border {
/*     */     public Insets getBorderInsets(Component c) {
/* 104 */       return new Insets(2, 2, 2, 2);
/*     */     }
/*     */     
/* 107 */     public boolean isBorderOpaque() { return false; }
/*     */     
/*     */     public void paintBorder(Component c, Graphics g, int x, int y, int width, int height)
/*     */     {
/* 111 */       g.setColor(UIManager.getColor("TipOfTheDay.background"));
/* 112 */       g.drawLine(x, y, x + width - 1, y);
/* 113 */       g.drawLine(x, y, x, y + height - 1);
/*     */       
/* 115 */       g.setColor(Color.black);
/* 116 */       g.drawLine(x + 1, y + 1, x + width - 3, y + 1);
/* 117 */       g.drawLine(x + 1, y + 1, x + 1, y + height - 3);
/*     */       
/* 119 */       g.setColor(Color.white);
/* 120 */       g.drawLine(x, y + height - 1, x + width, y + height - 1);
/* 121 */       g.drawLine(x + width - 1, y, x + width - 1, y + height - 1);
/*     */     }
/*     */   }
/*     */ }


/* Location:              E:\java\java学习\hutubill\lib\all.jar!\org\jdesktop\swingx\plaf\windows\WindowsTipOfTheDayUI.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */
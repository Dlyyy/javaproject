/*     */ package org.jdesktop.swingx.plaf.nimbus;
/*     */ 
/*     */ import java.awt.Color;
/*     */ import java.awt.Container;
/*     */ import java.awt.Graphics;
/*     */ import java.awt.Graphics2D;
/*     */ import java.awt.LinearGradientPaint;
/*     */ import java.awt.Paint;
/*     */ import java.awt.RenderingHints;
/*     */ import javax.swing.JComponent;
/*     */ import javax.swing.JLabel;
/*     */ import javax.swing.border.Border;
/*     */ import javax.swing.plaf.ComponentUI;
/*     */ import javax.swing.plaf.FontUIResource;
/*     */ import org.jdesktop.swingx.JXTaskPane;
/*     */ import org.jdesktop.swingx.plaf.basic.BasicTaskPaneUI;
/*     */ import org.jdesktop.swingx.plaf.basic.BasicTaskPaneUI.PaneBorder;
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
/*     */ public class NimbusTaskPaneUI
/*     */   extends BasicTaskPaneUI
/*     */ {
/*     */   public static ComponentUI createUI(JComponent c)
/*     */   {
/*  47 */     return new NimbusTaskPaneUI();
/*     */   }
/*     */   
/*     */   protected Border createPaneBorder()
/*     */   {
/*  52 */     return new NimbusPaneBorder();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void update(Graphics g, JComponent c)
/*     */   {
/*  61 */     if (c.isOpaque()) {
/*  62 */       g.setColor(c.getParent().getBackground());
/*  63 */       g.fillRect(0, 0, c.getWidth(), c.getHeight());
/*  64 */       g.setColor(c.getBackground());
/*  65 */       g.fillRect(0, getRoundHeight(), c.getWidth(), c.getHeight() - getRoundHeight());
/*     */     }
/*     */     
/*  68 */     paint(g, c);
/*     */   }
/*     */   
/*     */   class NimbusPaneBorder
/*     */     extends BasicTaskPaneUI.PaneBorder
/*     */   {
/*     */     NimbusPaneBorder()
/*     */     {
/*  76 */       super();
/*     */     }
/*     */     
/*     */     protected void paintTitleBackground(JXTaskPane group, Graphics g)
/*     */     {
/*  81 */       Paint oldPaint = ((Graphics2D)g).getPaint();
/*     */       
/*  83 */       NimbusTaskPaneUI.this.roundHeight = 7;
/*     */       
/*  85 */       if (group.isSpecial()) {
/*  86 */         g.setColor(this.specialTitleBackground);
/*     */         
/*  88 */         g.fillRoundRect(0, 0, group.getWidth(), NimbusTaskPaneUI.this.getRoundHeight() * 2, NimbusTaskPaneUI.this.getRoundHeight(), NimbusTaskPaneUI.this.getRoundHeight());
/*     */         
/*  90 */         g.fillRect(0, NimbusTaskPaneUI.this.getRoundHeight(), group.getWidth(), NimbusTaskPaneUI.this.getTitleHeight(group) - NimbusTaskPaneUI.this.getRoundHeight());
/*     */       }
/*     */       else
/*     */       {
/*  94 */         Color[] colors = { this.titleBackgroundGradientStart, this.titleBackgroundGradientEnd };
/*     */         
/*     */ 
/*  97 */         float[] fractions = { 0.0F, 1.0F };
/*     */         
/*  99 */         LinearGradientPaint gradient = new LinearGradientPaint(group.getWidth() / 2, 0.0F, group.getWidth() / 2, NimbusTaskPaneUI.this.getTitleHeight(group), fractions, colors);
/*     */         
/*     */ 
/*     */ 
/* 103 */         ((Graphics2D)g).setPaint(gradient);
/*     */         
/* 105 */         ((Graphics2D)g).setRenderingHint(RenderingHints.KEY_COLOR_RENDERING, RenderingHints.VALUE_COLOR_RENDER_QUALITY);
/*     */         
/*     */ 
/* 108 */         ((Graphics2D)g).setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
/*     */         
/*     */ 
/* 111 */         ((Graphics2D)g).setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
/*     */         
/* 113 */         ((Graphics2D)g).setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
/*     */         
/*     */ 
/*     */ 
/* 117 */         g.fillRoundRect(0, 0, group.getWidth(), NimbusTaskPaneUI.this.getTitleHeight(group) / 2, NimbusTaskPaneUI.this.getRoundHeight(), NimbusTaskPaneUI.this.getRoundHeight());
/*     */         
/*     */ 
/*     */ 
/* 121 */         g.fillRect(0, NimbusTaskPaneUI.this.getRoundHeight(), group.getWidth(), NimbusTaskPaneUI.this.getTitleHeight(group) - NimbusTaskPaneUI.this.getRoundHeight());
/*     */       }
/*     */       
/*     */ 
/*     */ 
/*     */ 
/* 127 */       g.setColor(this.borderColor);
/*     */       
/* 129 */       g.drawRoundRect(0, 0, group.getWidth() - 1, NimbusTaskPaneUI.this.getTitleHeight(group) + NimbusTaskPaneUI.this.getRoundHeight(), NimbusTaskPaneUI.this.getRoundHeight(), NimbusTaskPaneUI.this.getRoundHeight());
/*     */       
/* 131 */       g.drawLine(0, NimbusTaskPaneUI.this.getTitleHeight(group) - 1, group.getWidth(), NimbusTaskPaneUI.this.getTitleHeight(group) - 1);
/*     */       
/*     */ 
/* 134 */       ((Graphics2D)g).setPaint(oldPaint);
/*     */     }
/*     */     
/*     */ 
/*     */     protected void paintExpandedControls(JXTaskPane group, Graphics g, int x, int y, int width, int height)
/*     */     {
/* 140 */       ((Graphics2D)g).setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
/*     */       
/*     */ 
/* 143 */       g.setColor(getPaintColor(group));
/* 144 */       paintChevronControls(group, g, x, y, width, height);
/*     */       
/* 146 */       ((Graphics2D)g).setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_OFF);
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */     protected void paintTitle(JXTaskPane group, Graphics g, Color textColor, int x, int y, int width, int height)
/*     */     {
/* 153 */       configureLabel(group);
/*     */       
/* 155 */       this.label.setForeground(new Color(textColor.getRGB()));
/* 156 */       if ((group.getFont() != null) && (!(group.getFont() instanceof FontUIResource)))
/*     */       {
/* 158 */         this.label.setFont(group.getFont());
/*     */       }
/* 160 */       g.translate(x, y);
/* 161 */       this.label.setBounds(0, 0, width, height);
/* 162 */       this.label.paint(g);
/* 163 */       g.translate(-x, -y);
/*     */     }
/*     */     
/*     */     protected boolean isMouseOverBorder()
/*     */     {
/* 168 */       return true;
/*     */     }
/*     */   }
/*     */ }


/* Location:              E:\java\java学习\hutubill\lib\all.jar!\org\jdesktop\swingx\plaf\nimbus\NimbusTaskPaneUI.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */
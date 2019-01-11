/*     */ package org.jdesktop.swingx.plaf.misc;
/*     */ 
/*     */ import java.awt.Container;
/*     */ import java.awt.GradientPaint;
/*     */ import java.awt.Graphics;
/*     */ import java.awt.Graphics2D;
/*     */ import java.awt.Paint;
/*     */ import java.awt.RenderingHints;
/*     */ import javax.swing.JComponent;
/*     */ import javax.swing.border.Border;
/*     */ import javax.swing.plaf.ComponentUI;
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
/*     */ public class GlossyTaskPaneUI
/*     */   extends BasicTaskPaneUI
/*     */ {
/*     */   public static ComponentUI createUI(JComponent c)
/*     */   {
/*  44 */     return new GlossyTaskPaneUI();
/*     */   }
/*     */   
/*     */   protected Border createPaneBorder()
/*     */   {
/*  49 */     return new GlossyPaneBorder();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void update(Graphics g, JComponent c)
/*     */   {
/*  58 */     if (c.isOpaque()) {
/*  59 */       g.setColor(c.getParent().getBackground());
/*  60 */       g.fillRect(0, 0, c.getWidth(), c.getHeight());
/*  61 */       g.setColor(c.getBackground());
/*  62 */       g.fillRect(0, getRoundHeight(), c.getWidth(), c.getHeight() - getRoundHeight());
/*     */     }
/*  64 */     paint(g, c);
/*     */   }
/*     */   
/*     */   class GlossyPaneBorder
/*     */     extends BasicTaskPaneUI.PaneBorder
/*     */   {
/*     */     GlossyPaneBorder()
/*     */     {
/*  72 */       super();
/*     */     }
/*     */     
/*     */     protected void paintTitleBackground(JXTaskPane group, Graphics g) {
/*  76 */       if (group.isSpecial()) {
/*  77 */         g.setColor(this.specialTitleBackground);
/*  78 */         g.fillRoundRect(0, 0, group.getWidth(), GlossyTaskPaneUI.this.getRoundHeight() * 2, GlossyTaskPaneUI.this.getRoundHeight(), GlossyTaskPaneUI.this.getRoundHeight());
/*     */         
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*  85 */         g.fillRect(0, GlossyTaskPaneUI.this.getRoundHeight(), group.getWidth(), GlossyTaskPaneUI.this.getTitleHeight(group) - GlossyTaskPaneUI.this.getRoundHeight());
/*     */ 
/*     */       }
/*     */       else
/*     */       {
/*     */ 
/*  91 */         Paint oldPaint = ((Graphics2D)g).getPaint();
/*  92 */         GradientPaint gradient = new GradientPaint(0.0F, 0.0F, this.titleBackgroundGradientStart, 0.0F, GlossyTaskPaneUI.this.getTitleHeight(group), this.titleBackgroundGradientEnd);
/*     */         
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 101 */         ((Graphics2D)g).setRenderingHint(RenderingHints.KEY_COLOR_RENDERING, RenderingHints.VALUE_COLOR_RENDER_QUALITY);
/*     */         
/*     */ 
/* 104 */         ((Graphics2D)g).setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
/*     */         
/*     */ 
/* 107 */         ((Graphics2D)g).setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
/*     */         
/*     */ 
/* 110 */         ((Graphics2D)g).setPaint(gradient);
/*     */         
/* 112 */         g.fillRoundRect(0, 0, group.getWidth(), GlossyTaskPaneUI.this.getRoundHeight() * 2, GlossyTaskPaneUI.this.getRoundHeight(), GlossyTaskPaneUI.this.getRoundHeight());
/*     */         
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 119 */         g.fillRect(0, GlossyTaskPaneUI.this.getRoundHeight(), group.getWidth(), GlossyTaskPaneUI.this.getTitleHeight(group) - GlossyTaskPaneUI.this.getRoundHeight());
/*     */         
/*     */ 
/*     */ 
/*     */ 
/* 124 */         ((Graphics2D)g).setPaint(oldPaint);
/*     */       }
/*     */       
/* 127 */       g.setColor(this.borderColor);
/* 128 */       g.drawRoundRect(0, 0, group.getWidth() - 1, GlossyTaskPaneUI.this.getTitleHeight(group) + GlossyTaskPaneUI.this.getRoundHeight(), GlossyTaskPaneUI.this.getRoundHeight(), GlossyTaskPaneUI.this.getRoundHeight());
/*     */       
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 135 */       g.drawLine(0, GlossyTaskPaneUI.this.getTitleHeight(group) - 1, group.getWidth(), GlossyTaskPaneUI.this.getTitleHeight(group) - 1);
/*     */     }
/*     */     
/*     */ 
/*     */     protected void paintExpandedControls(JXTaskPane group, Graphics g, int x, int y, int width, int height)
/*     */     {
/* 141 */       ((Graphics2D)g).setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
/*     */       
/*     */ 
/*     */ 
/* 145 */       paintOvalAroundControls(group, g, x, y, width, height);
/* 146 */       g.setColor(getPaintColor(group));
/* 147 */       paintChevronControls(group, g, x, y, width, height);
/*     */       
/* 149 */       ((Graphics2D)g).setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_OFF);
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */     protected boolean isMouseOverBorder()
/*     */     {
/* 156 */       return true;
/*     */     }
/*     */   }
/*     */ }


/* Location:              E:\java\java学习\hutubill\lib\all.jar!\org\jdesktop\swingx\plaf\misc\GlossyTaskPaneUI.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */
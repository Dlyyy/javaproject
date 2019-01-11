/*     */ package org.jdesktop.swingx.plaf.windows;
/*     */ 
/*     */ import java.awt.ComponentOrientation;
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
/*     */ public class WindowsTaskPaneUI
/*     */   extends BasicTaskPaneUI
/*     */ {
/*     */   public static ComponentUI createUI(JComponent c)
/*     */   {
/*  44 */     return new WindowsTaskPaneUI();
/*     */   }
/*     */   
/*     */   protected Border createPaneBorder()
/*     */   {
/*  49 */     return new XPPaneBorder();
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
/*     */   class XPPaneBorder
/*     */     extends BasicTaskPaneUI.PaneBorder
/*     */   {
/*     */     XPPaneBorder()
/*     */     {
/*  72 */       super();
/*     */     }
/*     */     
/*     */     protected void paintTitleBackground(JXTaskPane group, Graphics g) {
/*  76 */       if (group.isSpecial()) {
/*  77 */         g.setColor(this.specialTitleBackground);
/*  78 */         g.fillRoundRect(0, 0, group.getWidth(), WindowsTaskPaneUI.this.getRoundHeight() * 2, WindowsTaskPaneUI.this.getRoundHeight(), WindowsTaskPaneUI.this.getRoundHeight());
/*     */         
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*  85 */         g.fillRect(0, WindowsTaskPaneUI.this.getRoundHeight(), group.getWidth(), WindowsTaskPaneUI.this.getTitleHeight(group) - WindowsTaskPaneUI.this.getRoundHeight());
/*     */ 
/*     */       }
/*     */       else
/*     */       {
/*     */ 
/*  91 */         Paint oldPaint = ((Graphics2D)g).getPaint();
/*  92 */         GradientPaint gradient = new GradientPaint(0.0F, group.getWidth() / 2, group.getComponentOrientation().isLeftToRight() ? this.titleBackgroundGradientStart : this.titleBackgroundGradientEnd, group.getWidth(), WindowsTaskPaneUI.this.getTitleHeight(group), group.getComponentOrientation().isLeftToRight() ? this.titleBackgroundGradientEnd : this.titleBackgroundGradientStart);
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
/* 104 */         ((Graphics2D)g).setRenderingHint(RenderingHints.KEY_COLOR_RENDERING, RenderingHints.VALUE_COLOR_RENDER_QUALITY);
/*     */         
/*     */ 
/* 107 */         ((Graphics2D)g).setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
/*     */         
/*     */ 
/* 110 */         ((Graphics2D)g).setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
/*     */         
/*     */ 
/* 113 */         ((Graphics2D)g).setPaint(gradient);
/* 114 */         g.fillRoundRect(0, 0, group.getWidth(), WindowsTaskPaneUI.this.getRoundHeight() * 2, WindowsTaskPaneUI.this.getRoundHeight(), WindowsTaskPaneUI.this.getRoundHeight());
/*     */         
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 121 */         g.fillRect(0, WindowsTaskPaneUI.this.getRoundHeight(), group.getWidth(), WindowsTaskPaneUI.this.getTitleHeight(group) - WindowsTaskPaneUI.this.getRoundHeight());
/*     */         
/*     */ 
/*     */ 
/*     */ 
/* 126 */         ((Graphics2D)g).setPaint(oldPaint);
/*     */       }
/*     */     }
/*     */     
/*     */ 
/*     */     protected void paintExpandedControls(JXTaskPane group, Graphics g, int x, int y, int width, int height)
/*     */     {
/* 133 */       ((Graphics2D)g).setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
/*     */       
/*     */ 
/*     */ 
/* 137 */       paintOvalAroundControls(group, g, x, y, width, height);
/* 138 */       g.setColor(getPaintColor(group));
/* 139 */       paintChevronControls(group, g, x, y, width, height);
/*     */       
/* 141 */       ((Graphics2D)g).setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_OFF);
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */     protected boolean isMouseOverBorder()
/*     */     {
/* 148 */       return true;
/*     */     }
/*     */   }
/*     */ }


/* Location:              E:\java\java学习\hutubill\lib\all.jar!\org\jdesktop\swingx\plaf\windows\WindowsTaskPaneUI.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */
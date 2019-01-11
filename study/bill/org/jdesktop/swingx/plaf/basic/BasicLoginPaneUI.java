/*     */ package org.jdesktop.swingx.plaf.basic;
/*     */ 
/*     */ import java.awt.ComponentOrientation;
/*     */ import java.awt.Font;
/*     */ import java.awt.GradientPaint;
/*     */ import java.awt.Graphics2D;
/*     */ import java.awt.Image;
/*     */ import java.awt.RenderingHints;
/*     */ import java.awt.geom.GeneralPath;
/*     */ import java.awt.geom.Rectangle2D;
/*     */ import java.awt.image.BufferedImage;
/*     */ import java.beans.PropertyChangeEvent;
/*     */ import java.beans.PropertyChangeListener;
/*     */ import javax.swing.JComponent;
/*     */ import javax.swing.UIManager;
/*     */ import javax.swing.plaf.ComponentUI;
/*     */ import org.jdesktop.swingx.JXLoginPane;
/*     */ import org.jdesktop.swingx.plaf.LoginPaneUI;
/*     */ import org.jdesktop.swingx.plaf.UIManagerExt;
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
/*     */ public class BasicLoginPaneUI
/*     */   extends LoginPaneUI
/*     */ {
/*     */   private JXLoginPane dlg;
/*     */   
/*     */   private class LocaleHandler
/*     */     implements PropertyChangeListener
/*     */   {
/*     */     private LocaleHandler() {}
/*     */     
/*     */     public void propertyChange(PropertyChangeEvent evt)
/*     */     {
/*  50 */       Object src = evt.getSource();
/*     */       
/*  52 */       if ((src instanceof JComponent)) {
/*  53 */         ((JComponent)src).updateUI();
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public BasicLoginPaneUI(JXLoginPane dlg)
/*     */   {
/*  62 */     this.dlg = dlg;
/*     */   }
/*     */   
/*     */   public static ComponentUI createUI(JComponent c)
/*     */   {
/*  67 */     return new BasicLoginPaneUI((JXLoginPane)c);
/*     */   }
/*     */   
/*     */   public void installUI(JComponent c)
/*     */   {
/*  72 */     installDefaults();
/*     */   }
/*     */   
/*     */   protected void installDefaults() {
/*  76 */     String s = this.dlg.getBannerText();
/*  77 */     if ((s == null) || (s.equals(""))) {
/*  78 */       this.dlg.setBannerText(UIManagerExt.getString("JXLoginPane.bannerString", this.dlg.getLocale()));
/*     */     }
/*     */     
/*  81 */     s = this.dlg.getErrorMessage();
/*  82 */     if ((s == null) || (s.equals(""))) {
/*  83 */       this.dlg.setErrorMessage(UIManagerExt.getString("JXLoginPane.errorMessage", this.dlg.getLocale()));
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public Image getBanner()
/*     */   {
/*  93 */     int w = 400;
/*  94 */     int h = 60;
/*  95 */     float loginStringX = w * 0.05F;
/*  96 */     float loginStringY = h * 0.75F;
/*     */     
/*  98 */     BufferedImage img = new BufferedImage(w, h, 1);
/*  99 */     Graphics2D g2 = img.createGraphics();
/*     */     try {
/* 101 */       Font font = UIManager.getFont("JXLoginPane.bannerFont");
/* 102 */       g2.setFont(font);
/* 103 */       Graphics2D originalGraphics = g2;
/*     */       try
/*     */       {
/* 106 */         if (!this.dlg.getComponentOrientation().isLeftToRight()) {
/* 107 */           originalGraphics = (Graphics2D)g2.create();
/* 108 */           g2.scale(-1.0D, 1.0D);
/* 109 */           g2.translate(-w, 0);
/* 110 */           loginStringX = w - ((float)font.getStringBounds(this.dlg.getBannerText(), originalGraphics.getFontRenderContext()).getWidth() + w * 0.05F);
/*     */         }
/*     */         
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 117 */         g2.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
/*     */         
/* 119 */         g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
/*     */         
/* 121 */         g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
/*     */         
/* 123 */         g2.setRenderingHint(RenderingHints.KEY_FRACTIONALMETRICS, RenderingHints.VALUE_FRACTIONALMETRICS_ON);
/*     */         
/*     */ 
/*     */ 
/* 127 */         g2.setColor(UIManager.getColor("JXLoginPane.bannerDarkBackground"));
/*     */         
/* 129 */         g2.fillRect(0, 0, w, h);
/*     */         
/*     */ 
/* 132 */         GeneralPath curveShape = new GeneralPath(1);
/*     */         
/* 134 */         curveShape.moveTo(0.0F, h * 0.6F);
/* 135 */         curveShape.curveTo(w * 0.167F, h * 1.2F, w * 0.667F, h * -0.5F, w, h * 0.75F);
/*     */         
/* 137 */         curveShape.lineTo(w, h);
/* 138 */         curveShape.lineTo(0.0F, h);
/* 139 */         curveShape.lineTo(0.0F, h * 0.8F);
/* 140 */         curveShape.closePath();
/*     */         
/*     */ 
/*     */ 
/* 144 */         GradientPaint gp = new GradientPaint(0.0F, h, UIManager.getColor("JXLoginPane.bannerDarkBackground"), 0.0F, 0.0F, UIManager.getColor("JXLoginPane.bannerLightBackground"));
/*     */         
/*     */ 
/* 147 */         g2.setPaint(gp);
/* 148 */         g2.fill(curveShape);
/*     */         
/* 150 */         originalGraphics.setColor(UIManager.getColor("JXLoginPane.bannerForeground"));
/*     */         
/* 152 */         originalGraphics.drawString(this.dlg.getBannerText(), loginStringX, loginStringY);
/*     */       }
/*     */       finally {}
/*     */     }
/*     */     finally
/*     */     {
/* 158 */       g2.dispose();
/*     */     }
/* 160 */     return img;
/*     */   }
/*     */ }


/* Location:              E:\java\java学习\hutubill\lib\all.jar!\org\jdesktop\swingx\plaf\basic\BasicLoginPaneUI.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */
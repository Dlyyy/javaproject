/*     */ package org.jdesktop.swingx.renderer;
/*     */ 
/*     */ import java.awt.BorderLayout;
/*     */ import java.awt.Color;
/*     */ import java.awt.ComponentOrientation;
/*     */ import java.awt.Font;
/*     */ import java.awt.Rectangle;
/*     */ import javax.swing.BorderFactory;
/*     */ import javax.swing.Icon;
/*     */ import javax.swing.JComponent;
/*     */ import javax.swing.JLabel;
/*     */ import javax.swing.border.Border;
/*     */ import org.jdesktop.swingx.JXPanel;
/*     */ import org.jdesktop.swingx.painter.Painter;
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
/*     */ public class WrappingIconPanel
/*     */   extends JXPanel
/*     */   implements PainterAware
/*     */ {
/*     */   protected JComponent delegate;
/*     */   JLabel iconLabel;
/*  51 */   String labelPosition = "Center";
/*     */   
/*     */   int iconLabelGap;
/*     */   
/*     */   private Border ltorBorder;
/*     */   
/*     */   private Border rtolBorder;
/*     */   
/*     */   private boolean dropHackEnabled;
/*     */   
/*     */ 
/*     */   public WrappingIconPanel()
/*     */   {
/*  64 */     this(true);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public WrappingIconPanel(boolean dropHackEnabled)
/*     */   {
/*  76 */     setOpaque(false);
/*  77 */     this.iconLabel = new JRendererLabel();
/*  78 */     this.iconLabelGap = this.iconLabel.getIconTextGap();
/*  79 */     this.iconLabel.setOpaque(false);
/*  80 */     updateIconBorder();
/*  81 */     setBorder(null);
/*  82 */     setLayout(new BorderLayout());
/*  83 */     add(this.iconLabel, "Before");
/*  84 */     setDropHackEnabled(dropHackEnabled);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setComponentOrientation(ComponentOrientation o)
/*     */   {
/*  94 */     super.setComponentOrientation(o);
/*  95 */     updateIconBorder();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   private void updateIconBorder()
/*     */   {
/* 102 */     if (this.ltorBorder == null) {
/* 103 */       this.ltorBorder = BorderFactory.createEmptyBorder(0, 0, 0, this.iconLabelGap);
/* 104 */       this.rtolBorder = BorderFactory.createEmptyBorder(0, this.iconLabelGap, 0, 0);
/*     */     }
/* 106 */     if (getComponentOrientation().isLeftToRight()) {
/* 107 */       this.iconLabel.setBorder(this.ltorBorder);
/*     */     } else {
/* 109 */       this.iconLabel.setBorder(this.rtolBorder);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setIcon(Icon icon)
/*     */   {
/* 119 */     this.iconLabel.setIcon(icon);
/* 120 */     this.iconLabel.setText(null);
/* 121 */     validate();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public Icon getIcon()
/*     */   {
/* 130 */     return this.iconLabel.getIcon();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setComponent(JComponent comp)
/*     */   {
/* 140 */     JComponent old = getComponent();
/* 141 */     if (this.delegate != null) {
/* 142 */       remove(this.delegate);
/*     */     }
/* 144 */     this.delegate = comp;
/* 145 */     add(this.delegate, this.labelPosition);
/* 146 */     validate();
/* 147 */     firePropertyChange("component", old, getComponent());
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public JComponent getComponent()
/*     */   {
/* 156 */     return this.delegate;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setBackground(Color bg)
/*     */   {
/* 166 */     super.setBackground(bg);
/* 167 */     if (this.iconLabel != null) {
/* 168 */       this.iconLabel.setBackground(bg);
/*     */     }
/* 170 */     if (this.delegate != null) {
/* 171 */       this.delegate.setBackground(bg);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setForeground(Color bg)
/*     */   {
/* 182 */     super.setForeground(bg);
/* 183 */     if (this.iconLabel != null) {
/* 184 */       this.iconLabel.setForeground(bg);
/*     */     }
/* 186 */     if (this.delegate != null) {
/* 187 */       this.delegate.setForeground(bg);
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
/*     */   public void setFont(Font font)
/*     */   {
/* 201 */     if (this.delegate != null) {
/* 202 */       this.delegate.setFont(font);
/*     */     }
/* 204 */     super.setFont(font);
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
/*     */   public boolean isVisible()
/*     */   {
/* 224 */     return this.dropHackEnabled ? false : super.isVisible();
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
/*     */   public Painter<?> getPainter()
/*     */   {
/* 238 */     if ((this.delegate instanceof PainterAware)) {
/* 239 */       return ((PainterAware)this.delegate).getPainter();
/*     */     }
/* 241 */     return null;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setPainter(Painter<?> painter)
/*     */   {
/* 251 */     if ((this.delegate instanceof PainterAware)) {
/* 252 */       ((PainterAware)this.delegate).setPainter(painter);
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
/*     */   public Rectangle getDelegateBounds()
/*     */   {
/* 266 */     if (this.delegate == null) return null;
/* 267 */     return this.delegate.getBounds();
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
/*     */   public void setDropHackEnabled(boolean dropHackEnabled)
/*     */   {
/* 281 */     this.dropHackEnabled = dropHackEnabled;
/*     */   }
/*     */ }


/* Location:              E:\java\java学习\hutubill\lib\all.jar!\org\jdesktop\swingx\renderer\WrappingIconPanel.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */
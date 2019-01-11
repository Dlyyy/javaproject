/*     */ package org.jdesktop.swingx.plaf;
/*     */ 
/*     */ import java.awt.Component;
/*     */ import java.awt.Container;
/*     */ import java.awt.Dimension;
/*     */ import java.awt.Graphics;
/*     */ import java.awt.Insets;
/*     */ import java.awt.LayoutManager;
/*     */ import java.awt.Rectangle;
/*     */ import java.beans.PropertyChangeEvent;
/*     */ import java.beans.PropertyChangeListener;
/*     */ import javax.swing.JTextField;
/*     */ import javax.swing.SwingUtilities;
/*     */ import javax.swing.border.Border;
/*     */ import javax.swing.plaf.UIResource;
/*     */ import org.jdesktop.swingx.prompt.BuddySupport;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class BuddyLayoutAndBorder
/*     */   implements LayoutManager, Border, PropertyChangeListener, UIResource
/*     */ {
/*     */   private JTextField textField;
/*     */   private Border borderDelegate;
/*     */   
/*     */   protected void install(JTextField textField)
/*     */   {
/*  33 */     uninstall();
/*  34 */     this.textField = textField;
/*     */     
/*  36 */     textField.setLayout(this);
/*     */     
/*  38 */     replaceBorderIfNecessary();
/*  39 */     textField.addPropertyChangeListener("border", this);
/*     */   }
/*     */   
/*     */   public Border getBorderDelegate() {
/*  43 */     return this.borderDelegate;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected void replaceBorderIfNecessary()
/*     */   {
/*  52 */     Border original = this.textField.getBorder();
/*     */     
/*  54 */     if (!(original instanceof BuddyLayoutAndBorder)) {
/*  55 */       this.borderDelegate = original;
/*  56 */       this.textField.setBorder(this);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void addLayoutComponent(String name, Component comp) {}
/*     */   
/*     */ 
/*     */ 
/*     */   public Dimension minimumLayoutSize(Container parent)
/*     */   {
/*  69 */     return preferredLayoutSize(parent);
/*     */   }
/*     */   
/*     */   public Dimension preferredLayoutSize(Container parent) {
/*  73 */     Dimension d = new Dimension();
/*     */     
/*     */ 
/*  76 */     for (Component c : BuddySupport.getLeft(this.textField)) {
/*  77 */       d.height = Math.max(d.height, c.getPreferredSize().height);
/*     */     }
/*  79 */     for (Component c : BuddySupport.getRight(this.textField)) {
/*  80 */       d.height = Math.max(d.height, c.getPreferredSize().height);
/*     */     }
/*     */     
/*  83 */     Insets insets = getRealBorderInsets();
/*  84 */     d.height += insets.top + insets.bottom;
/*  85 */     d.width += insets.left + insets.right;
/*     */     
/*  87 */     Insets outerMargin = BuddySupport.getOuterMargin(this.textField);
/*  88 */     if (outerMargin != null) {
/*  89 */       d.width += outerMargin.left + outerMargin.right;
/*  90 */       d.height += outerMargin.bottom + outerMargin.top;
/*     */     }
/*     */     
/*  93 */     return d;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void removeLayoutComponent(Component comp) {}
/*     */   
/*     */ 
/*     */ 
/*     */   public void layoutContainer(Container parent)
/*     */   {
/* 105 */     Rectangle visibleRect = getVisibleRect();
/*     */     
/*     */ 
/* 108 */     for (Component comp : BuddySupport.getLeft(this.textField)) {
/* 109 */       if (comp.isVisible())
/*     */       {
/*     */ 
/* 112 */         Dimension size = comp.getPreferredSize();
/* 113 */         comp.setBounds(visibleRect.x, centerY(visibleRect, size), size.width, size.height);
/*     */         
/* 115 */         visibleRect.x += size.width;
/* 116 */         visibleRect.width -= size.width;
/*     */       }
/*     */     }
/* 119 */     for (Component comp : BuddySupport.getRight(this.textField))
/* 120 */       if (comp.isVisible())
/*     */       {
/*     */ 
/*     */ 
/* 124 */         Dimension size = comp.getPreferredSize();
/* 125 */         comp.setBounds(visibleRect.x + visibleRect.width - size.width, centerY(visibleRect, size), size.width, size.height);
/*     */         
/* 127 */         visibleRect.width -= size.width;
/*     */       }
/*     */   }
/*     */   
/*     */   protected int centerY(Rectangle rect, Dimension size) {
/* 132 */     return (int)(rect.getCenterY() - size.height / 2);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected Rectangle getVisibleRect()
/*     */   {
/* 142 */     Rectangle alloc = SwingUtilities.getLocalBounds(this.textField);
/*     */     
/* 144 */     substractInsets(alloc, getRealBorderInsets());
/* 145 */     substractInsets(alloc, BuddySupport.getOuterMargin(this.textField));
/*     */     
/* 147 */     return alloc;
/*     */   }
/*     */   
/*     */   private void substractInsets(Rectangle alloc, Insets insets) {
/* 151 */     if (insets != null) {
/* 152 */       alloc.x += insets.left;
/* 153 */       alloc.y += insets.top;
/* 154 */       alloc.width -= insets.left + insets.right;
/* 155 */       alloc.height -= insets.top + insets.bottom;
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public Insets getBorderInsets(Component c)
/*     */   {
/* 166 */     Insets insets = null;
/* 167 */     if (this.borderDelegate != null)
/*     */     {
/*     */ 
/*     */ 
/*     */ 
/* 172 */       insets = (Insets)this.borderDelegate.getBorderInsets(this.textField).clone();
/*     */     } else {
/* 174 */       insets = new Insets(0, 0, 0, 0);
/*     */     }
/*     */     
/* 177 */     if (this.textField == null) {
/* 178 */       return insets;
/*     */     }
/*     */     
/* 181 */     for (Component comp : BuddySupport.getLeft(this.textField)) {
/* 182 */       insets.left += (comp.isVisible() ? comp.getPreferredSize().width : 0);
/*     */     }
/* 184 */     for (Component comp : BuddySupport.getRight(this.textField)) {
/* 185 */       insets.right += (comp.isVisible() ? comp.getPreferredSize().width : 0);
/*     */     }
/*     */     
/* 188 */     Insets outerMargin = BuddySupport.getOuterMargin(this.textField);
/* 189 */     if (outerMargin != null) {
/* 190 */       insets.left += outerMargin.left;
/* 191 */       insets.right += outerMargin.right;
/* 192 */       insets.top += outerMargin.top;
/* 193 */       insets.bottom += outerMargin.bottom;
/*     */     }
/*     */     
/* 196 */     return insets;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public Insets getRealBorderInsets()
/*     */   {
/* 206 */     if (this.borderDelegate == null)
/*     */     {
/* 208 */       return new Insets(0, 0, 0, 0);
/*     */     }
/*     */     
/* 211 */     Insets insets = this.borderDelegate.getBorderInsets(this.textField);
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 220 */     Insets margin = this.textField.getMargin();
/* 221 */     if (margin != null) {
/* 222 */       insets.left -= margin.left;
/* 223 */       insets.right -= margin.right;
/* 224 */       insets.top -= margin.top;
/* 225 */       insets.bottom -= margin.bottom;
/*     */     }
/*     */     
/* 228 */     return insets;
/*     */   }
/*     */   
/*     */   public boolean isBorderOpaque() {
/* 232 */     if (this.borderDelegate == null) {
/* 233 */       return false;
/*     */     }
/* 235 */     return this.borderDelegate.isBorderOpaque();
/*     */   }
/*     */   
/*     */   public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
/* 239 */     if (this.borderDelegate != null) {
/* 240 */       this.borderDelegate.paintBorder(c, g, x, y, width, height);
/*     */     }
/*     */   }
/*     */   
/*     */   public void propertyChange(PropertyChangeEvent evt) {
/* 245 */     replaceBorderIfNecessary();
/*     */   }
/*     */   
/*     */   public void uninstall() {
/* 249 */     if (this.textField != null) {
/* 250 */       this.textField.removePropertyChangeListener("border", this);
/* 251 */       if (this.textField.getBorder() == this) {
/* 252 */         this.textField.setBorder(this.borderDelegate);
/*     */       }
/* 254 */       this.textField.setLayout(null);
/* 255 */       this.textField = null;
/*     */     }
/*     */   }
/*     */   
/*     */   public String toString()
/*     */   {
/* 261 */     return String.format("%s (%s): %s", new Object[] { getClass().getName(), getBorderInsets(null), this.borderDelegate });
/*     */   }
/*     */ }


/* Location:              E:\java\java学习\hutubill\lib\all.jar!\org\jdesktop\swingx\plaf\BuddyLayoutAndBorder.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */
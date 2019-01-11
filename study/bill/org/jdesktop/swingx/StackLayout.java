/*     */ package org.jdesktop.swingx;
/*     */ 
/*     */ import java.awt.Component;
/*     */ import java.awt.Container;
/*     */ import java.awt.Dimension;
/*     */ import java.awt.Insets;
/*     */ import java.awt.LayoutManager2;
/*     */ import java.awt.Rectangle;
/*     */ import java.util.LinkedList;
/*     */ import java.util.List;
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
/*     */ public class StackLayout
/*     */   implements LayoutManager2
/*     */ {
/*     */   public static final String BOTTOM = "bottom";
/*     */   public static final String TOP = "top";
/*  66 */   private List<Component> components = new LinkedList();
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void addLayoutComponent(Component comp, Object constraints)
/*     */   {
/*  73 */     synchronized (comp.getTreeLock()) {
/*  74 */       if ("bottom".equals(constraints)) {
/*  75 */         this.components.add(0, comp);
/*  76 */       } else if ("top".equals(constraints)) {
/*  77 */         this.components.add(comp);
/*     */       } else {
/*  79 */         this.components.add(comp);
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public void addLayoutComponent(String name, Component comp)
/*     */   {
/*  88 */     addLayoutComponent(comp, "top");
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public void removeLayoutComponent(Component comp)
/*     */   {
/*  95 */     synchronized (comp.getTreeLock()) {
/*  96 */       this.components.remove(comp);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public float getLayoutAlignmentX(Container target)
/*     */   {
/* 104 */     return 0.5F;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public float getLayoutAlignmentY(Container target)
/*     */   {
/* 111 */     return 0.5F;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void invalidateLayout(Container target) {}
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public Dimension preferredLayoutSize(Container parent)
/*     */   {
/* 124 */     synchronized (parent.getTreeLock()) {
/* 125 */       int width = 0;
/* 126 */       int height = 0;
/*     */       
/* 128 */       for (Component comp : this.components) {
/* 129 */         Dimension size = comp.getPreferredSize();
/* 130 */         width = Math.max(size.width, width);
/* 131 */         height = Math.max(size.height, height);
/*     */       }
/*     */       
/* 134 */       Insets insets = parent.getInsets();
/* 135 */       width += insets.left + insets.right;
/* 136 */       height += insets.top + insets.bottom;
/*     */       
/* 138 */       return new Dimension(width, height);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public Dimension minimumLayoutSize(Container parent)
/*     */   {
/* 146 */     synchronized (parent.getTreeLock()) {
/* 147 */       int width = 0;
/* 148 */       int height = 0;
/*     */       
/* 150 */       for (Component comp : this.components) {
/* 151 */         Dimension size = comp.getMinimumSize();
/* 152 */         width = Math.max(size.width, width);
/* 153 */         height = Math.max(size.height, height);
/*     */       }
/*     */       
/* 156 */       Insets insets = parent.getInsets();
/* 157 */       width += insets.left + insets.right;
/* 158 */       height += insets.top + insets.bottom;
/*     */       
/* 160 */       return new Dimension(width, height);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public Dimension maximumLayoutSize(Container target)
/*     */   {
/* 168 */     return new Dimension(Integer.MAX_VALUE, Integer.MAX_VALUE);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void layoutContainer(Container parent)
/*     */   {
/* 176 */     synchronized (parent.getTreeLock()) {
/* 177 */       int width = parent.getWidth();
/* 178 */       int height = parent.getHeight();
/*     */       
/* 180 */       Rectangle bounds = new Rectangle(0, 0, width, height);
/*     */       
/* 182 */       int componentsCount = this.components.size();
/*     */       
/* 184 */       for (int i = 0; i < componentsCount; i++) {
/* 185 */         Component comp = (Component)this.components.get(i);
/* 186 */         comp.setBounds(bounds);
/* 187 */         parent.setComponentZOrder(comp, componentsCount - i - 1);
/*     */       }
/*     */     }
/*     */   }
/*     */ }


/* Location:              E:\java\java学习\hutubill\lib\all.jar!\org\jdesktop\swingx\StackLayout.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */
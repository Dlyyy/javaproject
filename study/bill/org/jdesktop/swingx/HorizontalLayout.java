/*    */ package org.jdesktop.swingx;
/*    */ 
/*    */ import java.awt.Component;
/*    */ import java.awt.Container;
/*    */ import java.awt.Dimension;
/*    */ import java.awt.Insets;
/*    */ import java.awt.LayoutManager;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class HorizontalLayout
/*    */   implements LayoutManager
/*    */ {
/* 36 */   private int gap = 0;
/*    */   
/*    */   public HorizontalLayout() {}
/*    */   
/*    */   public HorizontalLayout(int gap)
/*    */   {
/* 42 */     this.gap = gap;
/*    */   }
/*    */   
/*    */   public int getGap() {
/* 46 */     return this.gap;
/*    */   }
/*    */   
/*    */   public void setGap(int gap) {
/* 50 */     this.gap = gap;
/*    */   }
/*    */   
/*    */   public void addLayoutComponent(String name, Component c) {}
/*    */   
/*    */   public void layoutContainer(Container parent)
/*    */   {
/* 57 */     Insets insets = parent.getInsets();
/* 58 */     Dimension size = parent.getSize();
/* 59 */     int height = size.height - insets.top - insets.bottom;
/* 60 */     int width = insets.left;
/* 61 */     int i = 0; for (int c = parent.getComponentCount(); i < c; i++) {
/* 62 */       Component m = parent.getComponent(i);
/* 63 */       if (m.isVisible()) {
/* 64 */         m.setBounds(width, insets.top, m.getPreferredSize().width, height);
/*    */         
/* 66 */         width += m.getSize().width + this.gap;
/*    */       }
/*    */     }
/*    */   }
/*    */   
/*    */   public Dimension minimumLayoutSize(Container parent) {
/* 72 */     return preferredLayoutSize(parent);
/*    */   }
/*    */   
/*    */   public Dimension preferredLayoutSize(Container parent) {
/* 76 */     Insets insets = parent.getInsets();
/* 77 */     Dimension pref = new Dimension(0, 0);
/* 78 */     int i = 0; for (int c = parent.getComponentCount(); i < c; i++) {
/* 79 */       Component m = parent.getComponent(i);
/* 80 */       if (m.isVisible()) {
/* 81 */         Dimension componentPreferredSize = parent.getComponent(i).getPreferredSize();
/*    */         
/* 83 */         pref.height = Math.max(pref.height, componentPreferredSize.height);
/* 84 */         pref.width += componentPreferredSize.width + this.gap;
/*    */       }
/*    */     }
/* 87 */     pref.width += insets.left + insets.right;
/* 88 */     pref.height += insets.top + insets.bottom;
/* 89 */     return pref;
/*    */   }
/*    */   
/*    */   public void removeLayoutComponent(Component c) {}
/*    */ }


/* Location:              E:\java\java学习\hutubill\lib\all.jar!\org\jdesktop\swingx\HorizontalLayout.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */
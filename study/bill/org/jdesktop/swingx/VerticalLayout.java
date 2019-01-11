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
/*    */ public class VerticalLayout
/*    */   implements LayoutManager
/*    */ {
/* 36 */   private int gap = 0;
/*    */   
/*    */   public VerticalLayout() {}
/*    */   
/*    */   public VerticalLayout(int gap) {
/* 41 */     this.gap = gap;
/*    */   }
/*    */   
/*    */   public int getGap() {
/* 45 */     return this.gap;
/*    */   }
/*    */   
/*    */   public void setGap(int gap) {
/* 49 */     this.gap = gap;
/*    */   }
/*    */   
/*    */   public void addLayoutComponent(String name, Component c) {}
/*    */   
/*    */   public void layoutContainer(Container parent) {
/* 55 */     Insets insets = parent.getInsets();
/* 56 */     Dimension size = parent.getSize();
/* 57 */     int width = size.width - insets.left - insets.right;
/* 58 */     int height = insets.top;
/*    */     
/* 60 */     int i = 0; for (int c = parent.getComponentCount(); i < c; i++) {
/* 61 */       Component m = parent.getComponent(i);
/* 62 */       if (m.isVisible()) {
/* 63 */         m.setBounds(insets.left, height, width, m.getPreferredSize().height);
/* 64 */         height += m.getSize().height + this.gap;
/*    */       }
/*    */     }
/*    */   }
/*    */   
/*    */   public Dimension minimumLayoutSize(Container parent) {
/* 70 */     return preferredLayoutSize(parent);
/*    */   }
/*    */   
/*    */   public Dimension preferredLayoutSize(Container parent) {
/* 74 */     Insets insets = parent.getInsets();
/* 75 */     Dimension pref = new Dimension(0, 0);
/*    */     
/* 77 */     int i = 0; for (int c = parent.getComponentCount(); i < c; i++) {
/* 78 */       Component m = parent.getComponent(i);
/* 79 */       if (m.isVisible()) {
/* 80 */         Dimension componentPreferredSize = parent.getComponent(i).getPreferredSize();
/*    */         
/* 82 */         pref.height += componentPreferredSize.height + this.gap;
/* 83 */         pref.width = Math.max(pref.width, componentPreferredSize.width);
/*    */       }
/*    */     }
/*    */     
/* 87 */     pref.width += insets.left + insets.right;
/* 88 */     pref.height += insets.top + insets.bottom;
/*    */     
/* 90 */     return pref;
/*    */   }
/*    */   
/*    */   public void removeLayoutComponent(Component c) {}
/*    */ }


/* Location:              E:\java\java学习\hutubill\lib\all.jar!\org\jdesktop\swingx\VerticalLayout.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */
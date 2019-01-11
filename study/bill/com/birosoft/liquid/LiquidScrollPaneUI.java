/*     */ package com.birosoft.liquid;
/*     */ 
/*     */ import java.awt.event.MouseWheelEvent;
/*     */ import java.awt.event.MouseWheelListener;
/*     */ import java.beans.PropertyChangeEvent;
/*     */ import java.beans.PropertyChangeListener;
/*     */ import javax.swing.JComponent;
/*     */ import javax.swing.JScrollBar;
/*     */ import javax.swing.JScrollPane;
/*     */ import javax.swing.plaf.ComponentUI;
/*     */ import javax.swing.plaf.basic.BasicScrollPaneUI;
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
/*     */ public class LiquidScrollPaneUI
/*     */   extends BasicScrollPaneUI
/*     */   implements PropertyChangeListener
/*     */ {
/*     */   public static ComponentUI createUI(JComponent c)
/*     */   {
/*  37 */     return new LiquidScrollPaneUI();
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
/*     */   protected MouseWheelListener createMouseWheelListener()
/*     */   {
/*  51 */     return new MouseWheelHandler();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void installUI(JComponent c)
/*     */   {
/*  61 */     super.installUI(c);
/*     */     
/*  63 */     this.scrollpane.getHorizontalScrollBar().putClientProperty("JScrollBar.isFreeStanding", Boolean.FALSE);
/*     */     
/*  65 */     this.scrollpane.getVerticalScrollBar().putClientProperty("JScrollBar.isFreeStanding", Boolean.FALSE);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected PropertyChangeListener createScrollBarSwapListener()
/*     */   {
/*  76 */     return this;
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
/*     */   public void propertyChange(PropertyChangeEvent event) {}
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected class MouseWheelHandler
/*     */     implements MouseWheelListener
/*     */   {
/*     */     protected MouseWheelHandler() {}
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     public void mouseWheelMoved(MouseWheelEvent e)
/*     */     {
/* 110 */       if ((LiquidScrollPaneUI.this.scrollpane.isWheelScrollingEnabled()) && (e.getScrollAmount() != 0))
/*     */       {
/* 112 */         JScrollBar toScroll = LiquidScrollPaneUI.this.scrollpane.getVerticalScrollBar();
/* 113 */         int direction = 0;
/* 114 */         int length = toScroll.getHeight();
/*     */         
/*     */ 
/* 117 */         if ((toScroll == null) || (!toScroll.isVisible()) || (e.getModifiers() == 8))
/*     */         {
/* 119 */           toScroll = LiquidScrollPaneUI.this.scrollpane.getHorizontalScrollBar();
/*     */           
/* 121 */           if ((toScroll == null) || (!toScroll.isVisible())) {
/* 122 */             return;
/*     */           }
/*     */           
/* 125 */           length = toScroll.getWidth();
/*     */         }
/*     */         
/* 128 */         direction = e.getWheelRotation() < 0 ? -1 : 1;
/*     */         
/* 130 */         if (e.getScrollType() == 0) {
/* 131 */           int newValue = toScroll.getValue() + e.getWheelRotation() * length / (toScroll.getUnitIncrement() * 2);
/*     */           
/* 133 */           toScroll.setValue(newValue);
/* 134 */         } else if (e.getScrollType() == 1) {
/* 135 */           int newValue = toScroll.getValue() + e.getWheelRotation() * length / (toScroll.getBlockIncrement() * 2);
/*     */           
/* 137 */           toScroll.setValue(newValue);
/*     */         }
/*     */       }
/*     */     }
/*     */   }
/*     */ }


/* Location:              E:\java\java学习\hutubill\lib\all.jar!\com\birosoft\liquid\LiquidScrollPaneUI.class
 * Java compiler version: 4 (48.0)
 * JD-Core Version:       0.7.1
 */
/*    */ package com.birosoft.liquid;
/*    */ 
/*    */ import com.birosoft.liquid.util.Colors;
/*    */ import java.awt.Color;
/*    */ import java.awt.Container;
/*    */ import java.awt.Graphics;
/*    */ import java.util.ArrayList;
/*    */ import java.util.Iterator;
/*    */ import javax.swing.JComponent;
/*    */ import javax.swing.JPanel;
/*    */ import javax.swing.plaf.ComponentUI;
/*    */ import javax.swing.plaf.basic.BasicPanelUI;
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
/*    */ public class LiquidPanelUI
/*    */   extends BasicPanelUI
/*    */ {
/*    */   private static LiquidPanelUI panelUI;
/* 27 */   private static ArrayList panels = new ArrayList();
/*    */   
/*    */   public static ComponentUI createUI(JComponent c) {
/* 30 */     if (panelUI == null) {
/* 31 */       panelUI = new LiquidPanelUI();
/*    */     }
/*    */     
/* 34 */     return panelUI;
/*    */   }
/*    */   
/*    */   public void installUI(JComponent c) {
/* 38 */     JPanel p = (JPanel)c;
/* 39 */     super.installUI(p);
/* 40 */     installDefaults(p);
/*    */   }
/*    */   
/*    */   public void uninstallUI(JComponent c) {
/* 44 */     super.uninstallUI(c);
/*    */     
/* 46 */     Iterator i = panels.iterator();
/*    */     
/* 48 */     while (i.hasNext()) {
/* 49 */       ((JPanel)i.next()).setOpaque(true);
/*    */     }
/*    */     
/* 52 */     panels.removeAll(panels);
/*    */   }
/*    */   
/*    */   public void paint(Graphics g, JComponent c) {
/* 56 */     Color bg = LiquidLookAndFeel.getBackgroundColor();
/*    */     
/* 58 */     if (LiquidLookAndFeel.areStipplesUsed()) {
/* 59 */       Container container = c.getParent();
/*    */       
/* 61 */       if ((LiquidLookAndFeel.panelTransparency) && ((container instanceof JPanel)) && (c.isOpaque()) && (((JPanel)c).getClientProperty("panelTransparency") == null))
/*    */       {
/*    */ 
/*    */ 
/* 65 */         panels.add(c);
/* 66 */         c.setOpaque(false);
/*    */         
/* 68 */         if (c.isOpaque()) {
/* 69 */           ((JPanel)c).putClientProperty("panelTransparency", null);
/*    */         }
/* 71 */         container.invalidate();
/* 72 */         container.repaint();
/*    */       }
/*    */       
/* 75 */       if ((LiquidLookAndFeel.getBackgroundColor().equals(c.getBackground())) && (c.isOpaque()))
/*    */       {
/* 77 */         Colors.drawStipples(g, c, bg);
/*    */       }
/*    */     }
/*    */     
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/* 87 */     super.paint(g, c);
/*    */   }
/*    */ }


/* Location:              E:\java\java学习\hutubill\lib\all.jar!\com\birosoft\liquid\LiquidPanelUI.class
 * Java compiler version: 4 (48.0)
 * JD-Core Version:       0.7.1
 */
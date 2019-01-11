/*    */ package com.birosoft.liquid;
/*    */ 
/*    */ import com.birosoft.liquid.skin.Skin;
/*    */ import com.birosoft.liquid.skin.SkinToggleButtonIndexModel;
/*    */ import java.awt.Graphics;
/*    */ import java.awt.Rectangle;
/*    */ import java.beans.PropertyChangeEvent;
/*    */ import java.beans.PropertyChangeListener;
/*    */ import javax.swing.AbstractButton;
/*    */ import javax.swing.ButtonModel;
/*    */ import javax.swing.JComponent;
/*    */ import javax.swing.JToggleButton;
/*    */ import javax.swing.plaf.ComponentUI;
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
/*    */ public class LiquidToggleButtonUI
/*    */   extends LiquidButtonUI
/*    */ {
/* 35 */   private static final LiquidToggleButtonUI toggleButtonUI = new LiquidToggleButtonUI();
/*    */   private static final String propertyPrefix = "ToggleButton.";
/*    */   
/*    */   protected String getPropertyPrefix()
/*    */   {
/* 40 */     return "ToggleButton.";
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   public static ComponentUI createUI(JComponent c)
/*    */   {
/* 50 */     JToggleButton b = (JToggleButton)c;
/* 51 */     b.setRolloverEnabled(true);
/*    */     
/*    */ 
/* 54 */     c.setOpaque(false);
/* 55 */     c.addPropertyChangeListener("opaque", new PropertyChangeListener() { private final JComponent val$c;
/*    */       
/* 57 */       public void propertyChange(PropertyChangeEvent evt) { this.val$c.setOpaque(false);
/*    */       }
/* 59 */     });
/* 60 */     return toggleButtonUI;
/*    */   }
/*    */   
/*    */   public void paint(Graphics g, JComponent c)
/*    */   {
/* 65 */     AbstractButton button = (AbstractButton)c;
/* 66 */     ButtonModel model = button.getModel();
/*    */     
/* 68 */     this.buttonIndexModel.setButton(button);
/* 69 */     this.buttonIndexModel.setCheckForDefaultButton(false);
/* 70 */     int index = this.buttonIndexModel.getIndexForState();
/* 71 */     if (index > 3) index -= 4;
/* 72 */     if (((model.isArmed()) && (model.isPressed())) || (model.isSelected()))
/* 73 */       index = 2;
/* 74 */     if ((button.hasFocus()) && (index == 0)) index = 1;
/* 75 */     if ((button.getHeight() < 21) || (button.getWidth() < 21)) {
/* 76 */       getSkinToolbar().draw(g, index, button.getWidth(), button.getHeight());
/*    */       
/* 78 */       button.setFocusPainted(false);
/*    */     }
/* 80 */     else if (button.getClientProperty("JToolBar.isToolbarButton") == Boolean.TRUE) {
/* 81 */       getSkinToolbar().draw(g, index, button.getWidth(), button.getHeight());
/*    */     } else {
/* 83 */       getSkinButton().draw(g, index, button.getWidth(), button.getHeight());
/*    */     }
/*    */     
/* 86 */     if ((index == 4) && (button.isFocusPainted())) {
/* 87 */       Rectangle bounds = button.getBounds();
/* 88 */       paintFocus(g, bounds.height / 2 - 5);
/*    */     }
/* 90 */     super.paint(g, c);
/*    */   }
/*    */ }


/* Location:              E:\java\java学习\hutubill\lib\all.jar!\com\birosoft\liquid\LiquidToggleButtonUI.class
 * Java compiler version: 4 (48.0)
 * JD-Core Version:       0.7.1
 */
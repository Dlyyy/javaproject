/*     */ package com.birosoft.liquid;
/*     */ 
/*     */ import com.birosoft.liquid.skin.Skin;
/*     */ import com.birosoft.liquid.skin.SkinToggleButtonIndexModel;
/*     */ import java.awt.BasicStroke;
/*     */ import java.awt.Color;
/*     */ import java.awt.Graphics;
/*     */ import java.awt.Graphics2D;
/*     */ import java.awt.Rectangle;
/*     */ import java.beans.PropertyChangeEvent;
/*     */ import java.beans.PropertyChangeListener;
/*     */ import javax.swing.AbstractButton;
/*     */ import javax.swing.JButton;
/*     */ import javax.swing.JComponent;
/*     */ import javax.swing.JToggleButton;
/*     */ import javax.swing.plaf.ComponentUI;
/*     */ import javax.swing.plaf.basic.BasicButtonUI;
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
/*     */ public class LiquidButtonUI
/*     */   extends BasicButtonUI
/*     */ {
/*     */   public static final boolean HINT_DO_NOT_PAINT_TOOLBARBUTTON_IF_NO_MOUSE_OVER = true;
/*  45 */   private static final LiquidButtonUI buttonUI = new LiquidButtonUI();
/*     */   
/*     */   static Skin skinButton;
/*     */   
/*     */   private static Skin skinToolbar;
/*     */   
/*     */   private static Skin skinSmallButton;
/*     */   
/*  53 */   static BasicStroke focusStroke = new BasicStroke(1.0F, 0, 2, 1.0F, new float[] { 1.0F, 1.0F }, 1.0F);
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*  58 */   SkinToggleButtonIndexModel buttonIndexModel = new SkinToggleButtonIndexModel(true);
/*     */   
/*     */ 
/*  61 */   SkinToggleButtonIndexModel toolbarIndexModel = new SkinToggleButtonIndexModel();
/*     */   
/*     */ 
/*     */ 
/*     */   public void installDefaults(AbstractButton b)
/*     */   {
/*  67 */     super.installDefaults(b);
/*     */   }
/*     */   
/*     */   public void uninstallDefaults(AbstractButton b) {
/*  71 */     super.uninstallDefaults(b);
/*     */   }
/*     */   
/*     */   protected void paintFocus(Graphics g, AbstractButton b, Rectangle viewRect, Rectangle textRect, Rectangle iconRect)
/*     */   {
/*  76 */     Rectangle bounds = b.getBounds();
/*  77 */     int offset = bounds.height / 2 - 5;
/*     */     
/*  79 */     if (this.buttonIndexModel.getIndexForState() == 2) {
/*  80 */       return;
/*     */     }
/*     */     
/*  83 */     if ((b.getClientProperty("JToolBar.isToolbarButton") != Boolean.TRUE) && (b.isFocusPainted()))
/*     */     {
/*  85 */       paintFocus(g, offset);
/*     */     }
/*     */   }
/*     */   
/*     */   public void paintFocus(Graphics g, int offset) {
/*  90 */     Graphics2D g2d = (Graphics2D)g;
/*  91 */     g.setColor(new Color(196, 195, 194));
/*  92 */     g2d.drawLine(6, offset, 11, offset + 5);
/*  93 */     g.setColor(new Color(175, 174, 174));
/*  94 */     g2d.drawLine(6, offset + 1, 6, offset + 11);
/*  95 */     g2d.drawLine(6, offset + 11, 11, offset + 6);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static ComponentUI createUI(JComponent c)
/*     */   {
/* 105 */     JButton b = (JButton)c;
/* 106 */     b.setRolloverEnabled(true);
/*     */     
/*     */ 
/* 109 */     c.setOpaque(false);
/*     */     
/* 111 */     c.addPropertyChangeListener("opaque", new PropertyChangeListener() {
/*     */       private final JComponent val$c;
/*     */       
/* 114 */       public void propertyChange(PropertyChangeEvent evt) { this.val$c.setOpaque(false);
/*     */       }
/*     */ 
/* 117 */     });
/* 118 */     return buttonUI;
/*     */   }
/*     */   
/*     */   protected void installListeners(AbstractButton b) {
/* 122 */     super.installListeners(b);
/*     */   }
/*     */   
/*     */   protected void uninstallListeners(AbstractButton b)
/*     */   {
/* 127 */     super.uninstallListeners(b);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   protected void paintButtonPressed(Graphics g, AbstractButton b) {}
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void paint(Graphics g, JComponent c)
/*     */   {
/* 140 */     if ((c instanceof JToggleButton)) {
/* 141 */       super.paint(g, c);
/*     */       
/* 143 */       return;
/*     */     }
/*     */     
/* 146 */     AbstractButton button = (AbstractButton)c;
/*     */     
/* 148 */     if (button.getClientProperty("JToolBar.isToolbarButton") == Boolean.TRUE) {
/* 149 */       this.toolbarIndexModel.setButton(button);
/*     */       
/* 151 */       int index = this.toolbarIndexModel.getIndexForState();
/*     */       
/* 153 */       if ((button.hasFocus()) && (index == 0)) {
/* 154 */         index = 1;
/*     */       }
/* 156 */       else if ((!button.hasFocus()) && (index == 1) && (!LiquidLookAndFeel.toolbarFlattedButtonsRollover)) {
/* 157 */         index = 0;
/*     */       }
/*     */       
/* 160 */       getSkinToolbar().draw(g, index, button.getWidth(), button.getHeight());
/*     */     }
/*     */     else {
/* 163 */       this.buttonIndexModel.setButton(button);
/* 164 */       this.buttonIndexModel.setCheckForDefaultButton(button instanceof JButton);
/*     */       
/* 166 */       int index = this.buttonIndexModel.getIndexForState();
/*     */       
/* 168 */       if (index > 3) {
/* 169 */         index -= 4;
/*     */       }
/*     */       
/* 172 */       if ((button.hasFocus()) && (index == 0)) {
/* 173 */         index = 1;
/*     */       }
/*     */       
/* 176 */       if ((button.getHeight() < 21) || (button.getWidth() < 21)) {
/* 177 */         getSkinSmallButton().draw(g, index, button.getWidth(), button.getHeight());
/*     */         
/*     */ 
/*     */ 
/* 181 */         button.setFocusPainted(false);
/*     */       } else {
/* 183 */         getSkinButton().draw(g, index, button.getWidth(), button.getHeight());
/*     */       }
/*     */       
/*     */ 
/* 187 */       if ((index == 4) && (button.isFocusPainted())) {
/* 188 */         Rectangle bounds = button.getBounds();
/* 189 */         paintFocus(g, bounds.height / 2 - 5);
/*     */       }
/*     */     }
/*     */     
/* 193 */     super.paint(g, c);
/*     */   }
/*     */   
/*     */   public Skin getSkinButton() {
/* 197 */     if (skinButton == null) {
/* 198 */       skinButton = new Skin("button.png", 5, 10, 10, 12, 12);
/* 199 */       skinButton.colourImage();
/*     */     }
/*     */     
/* 202 */     return skinButton;
/*     */   }
/*     */   
/*     */   public Skin getSkinToolbar() {
/* 206 */     if (skinToolbar == null) {
/* 207 */       if (LiquidLookAndFeel.toolbarFlattedButtons) {
/* 208 */         skinToolbar = new Skin("toolbar.png", 8, 4, 13, 4, 10);
/*     */       } else {
/* 210 */         skinToolbar = new Skin("toolbar-nonflatted.png", 8, 4, 13, 4, 10);
/*     */       }
/*     */     }
/*     */     
/* 214 */     return skinToolbar;
/*     */   }
/*     */   
/*     */   public Skin getSkinSmallButton() {
/* 218 */     if (skinSmallButton == null) {
/* 219 */       skinSmallButton = new Skin("toolbar-nonflatted.png", 8, 4, 13, 4, 10);
/*     */     }
/*     */     
/* 222 */     return skinSmallButton;
/*     */   }
/*     */   
/*     */   public void update(Graphics g, JComponent c) {
/* 226 */     paint(g, c);
/*     */   }
/*     */ }


/* Location:              E:\java\java学习\hutubill\lib\all.jar!\com\birosoft\liquid\LiquidButtonUI.class
 * Java compiler version: 4 (48.0)
 * JD-Core Version:       0.7.1
 */
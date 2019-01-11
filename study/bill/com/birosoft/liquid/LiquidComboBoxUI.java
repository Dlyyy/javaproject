/*     */ package com.birosoft.liquid;
/*     */ 
/*     */ import com.birosoft.liquid.skin.Skin;
/*     */ import java.awt.Color;
/*     */ import java.awt.Component;
/*     */ import java.awt.ComponentOrientation;
/*     */ import java.awt.Container;
/*     */ import java.awt.Dimension;
/*     */ import java.awt.Graphics;
/*     */ import java.awt.Insets;
/*     */ import java.awt.LayoutManager;
/*     */ import java.awt.Rectangle;
/*     */ import java.awt.Toolkit;
/*     */ import java.awt.event.ActionEvent;
/*     */ import java.awt.event.ActionListener;
/*     */ import java.awt.event.MouseEvent;
/*     */ import java.beans.PropertyChangeEvent;
/*     */ import java.beans.PropertyChangeListener;
/*     */ import javax.swing.ComboBoxEditor;
/*     */ import javax.swing.Icon;
/*     */ import javax.swing.JButton;
/*     */ import javax.swing.JComboBox;
/*     */ import javax.swing.JComponent;
/*     */ import javax.swing.JList;
/*     */ import javax.swing.KeyStroke;
/*     */ import javax.swing.plaf.ComponentUI;
/*     */ import javax.swing.plaf.basic.BasicComboBoxUI;
/*     */ import javax.swing.plaf.basic.BasicComboBoxUI.PropertyChangeHandler;
/*     */ import javax.swing.plaf.basic.BasicComboPopup;
/*     */ import javax.swing.plaf.basic.ComboPopup;
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
/*     */ public class LiquidComboBoxUI
/*     */   extends BasicComboBoxUI
/*     */ {
/*  44 */   static int comboBoxButtonSize = 18;
/*     */   private int prevSelectedItem;
/*     */   private Skin skinCombo;
/*     */   
/*     */   public static ComponentUI createUI(JComponent c)
/*     */   {
/*  50 */     c.setOpaque(false);
/*     */     
/*  52 */     c.addPropertyChangeListener("opaque", new PropertyChangeListener() {
/*     */       private final JComponent val$c;
/*     */       
/*  55 */       public void propertyChange(PropertyChangeEvent evt) { this.val$c.setOpaque(false);
/*     */       }
/*     */ 
/*  58 */     });
/*  59 */     return new LiquidComboBoxUI();
/*     */   }
/*     */   
/*     */   protected void installListeners() {
/*  63 */     super.installListeners();
/*     */   }
/*     */   
/*     */   protected void uninstallListeners()
/*     */   {
/*  68 */     super.uninstallListeners();
/*     */   }
/*     */   
/*     */   public void installUI(JComponent c)
/*     */   {
/*  73 */     super.installUI(c);
/*  74 */     addKeyboardActions((JComboBox)c);
/*     */   }
/*     */   
/*     */   public void uninstallUI(JComponent c) {
/*  78 */     super.uninstallUI(c);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void paint(Graphics g, JComponent c)
/*     */   {
/*  90 */     int index = ((LiquidComboBoxButton)this.arrowButton).getIndexForState();
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*  96 */     getSkinCombo().draw(g, index, this.comboBox.getWidth(), this.comboBox.getHeight());
/*     */   }
/*     */   
/*     */   protected ComboBoxEditor createEditor() {
/* 100 */     return new LiquidComboBoxEditor.UIResource();
/*     */   }
/*     */   
/*     */   protected ComboPopup createPopup() {
/* 104 */     return new LiquidComboPopup(this.comboBox);
/*     */   }
/*     */   
/*     */   protected JButton createArrowButton() {
/* 108 */     JButton button = new LiquidComboBoxButton(this.comboBox, new LiquidCheckBoxIcon(), this.comboBox.isEditable(), this.currentValuePane, this.listBox);
/*     */     
/*     */ 
/* 111 */     button.setMargin(new Insets(0, 0, 0, 0));
/* 112 */     button.setFocusable(false);
/*     */     
/* 114 */     return button;
/*     */   }
/*     */   
/*     */   public PropertyChangeListener createPropertyChangeListener() {
/* 118 */     return new LiquidPropertyChangeListener();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private void addKeyboardActions(final JComboBox cb)
/*     */   {
/* 127 */     KeyStroke ksEnter = KeyStroke.getKeyStroke(10, 0);
/*     */     
/* 129 */     cb.registerKeyboardAction(new ActionListener() {
/*     */       public void actionPerformed(ActionEvent e) {
/* 131 */         LiquidComboBoxUI.this.prevSelectedItem = cb.getSelectedIndex();
/*     */         
/* 133 */         if (cb.isPopupVisible()) {
/* 134 */           cb.hidePopup();
/*     */         } else
/* 136 */           cb.showPopup(); } }, ksEnter, 0);
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 142 */     KeyStroke ksESC = KeyStroke.getKeyStroke(27, 0);
/*     */     
/* 144 */     cb.registerKeyboardAction(new ActionListener() {
/*     */       public void actionPerformed(ActionEvent e) {
/* 146 */         cb.hidePopup();
/* 147 */         cb.setSelectedIndex(LiquidComboBoxUI.this.prevSelectedItem); } }, ksESC, 0);
/*     */     
/*     */ 
/*     */ 
/*     */ 
/* 152 */     KeyStroke ksDown = KeyStroke.getKeyStroke(40, 0);
/*     */     
/* 154 */     cb.registerKeyboardAction(new ActionListener() {
/*     */       public void actionPerformed(ActionEvent e) {
/* 156 */         if (!cb.isPopupVisible()) {
/* 157 */           cb.showPopup();
/* 158 */           return;
/*     */         }
/* 160 */         if (cb.getSelectedIndex() <= cb.getItemCount() - 2) {
/* 161 */           cb.setSelectedIndex(cb.getSelectedIndex() + 1);
/* 162 */         } else if ((cb.getSelectedIndex() == -1) && (cb.getItemCount() > 0))
/*     */         {
/* 164 */           cb.setSelectedIndex(0);
/*     */         } else
/* 166 */           Toolkit.getDefaultToolkit().beep(); } }, ksDown, 0);
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 172 */     KeyStroke ksRight = KeyStroke.getKeyStroke(39, 0);
/*     */     
/* 174 */     cb.registerKeyboardAction(new ActionListener() {
/*     */       public void actionPerformed(ActionEvent e) {
/* 176 */         if (cb.getSelectedIndex() <= cb.getItemCount() - 2) {
/* 177 */           cb.setSelectedIndex(cb.getSelectedIndex() + 1);
/* 178 */         } else if ((cb.getSelectedIndex() == -1) && (cb.getItemCount() > 0))
/*     */         {
/* 180 */           cb.setSelectedIndex(0);
/*     */         } else
/* 182 */           Toolkit.getDefaultToolkit().beep(); } }, ksRight, 0);
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 188 */     KeyStroke ksUp = KeyStroke.getKeyStroke(38, 0);
/*     */     
/* 190 */     cb.registerKeyboardAction(new ActionListener() {
/*     */       public void actionPerformed(ActionEvent e) {
/* 192 */         if (cb.getSelectedIndex() > 0) {
/* 193 */           cb.setSelectedIndex(cb.getSelectedIndex() - 1);
/* 194 */         } else if ((cb.getSelectedIndex() == -1) && (cb.getItemCount() > 0))
/*     */         {
/* 196 */           cb.setSelectedIndex(0);
/*     */         } else
/* 198 */           Toolkit.getDefaultToolkit().beep(); } }, ksUp, 0);
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 204 */     KeyStroke ksLeft = KeyStroke.getKeyStroke(37, 0);
/*     */     
/* 206 */     cb.registerKeyboardAction(new ActionListener() {
/*     */       public void actionPerformed(ActionEvent e) {
/* 208 */         if (cb.getSelectedIndex() > 0) {
/* 209 */           cb.setSelectedIndex(cb.getSelectedIndex() - 1);
/* 210 */         } else if ((cb.getSelectedIndex() == -1) && (cb.getItemCount() > 0))
/*     */         {
/* 212 */           cb.setSelectedIndex(0);
/*     */         } else
/* 214 */           Toolkit.getDefaultToolkit().beep(); } }, ksLeft, 0);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   /**
/*     */    * @deprecated
/*     */    */
/*     */   protected void editablePropertyChanged(PropertyChangeEvent e) {}
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   protected LayoutManager createLayoutManager()
/*     */   {
/* 231 */     return new MetouiaComboBoxLayoutManager();
/*     */   }
/*     */   
/*     */   protected Rectangle rectangleForCurrentValue2() {
/* 235 */     int width = this.comboBox.getWidth();
/* 236 */     int height = this.comboBox.getHeight();
/* 237 */     Insets insets = getInsets();
/* 238 */     int buttonSize = height - (insets.top + insets.bottom);
/*     */     
/* 240 */     if (this.arrowButton != null) {
/* 241 */       buttonSize = comboBoxButtonSize;
/*     */     }
/*     */     
/* 244 */     if (this.comboBox.getComponentOrientation().isLeftToRight())
/*     */     {
/* 246 */       return new Rectangle(insets.left + 8, insets.top, width - (insets.left + insets.right + buttonSize) - 7, height - (insets.top + insets.bottom) - 2);
/*     */     }
/*     */     
/*     */ 
/* 250 */     return new Rectangle(insets.left + buttonSize, insets.top, width - (insets.left + insets.right + buttonSize), height - (insets.top + insets.bottom));
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   /**
/*     */    * @deprecated
/*     */    */
/*     */   protected void removeListeners()
/*     */   {
/* 263 */     if (this.propertyChangeListener != null) {
/* 264 */       this.comboBox.removePropertyChangeListener(this.propertyChangeListener);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void configureEditor()
/*     */   {
/* 273 */     super.configureEditor();
/*     */   }
/*     */   
/*     */   public void unconfigureEditor() {
/* 277 */     super.unconfigureEditor();
/*     */   }
/*     */   
/*     */   public Dimension getMinimumSize(JComponent c) {
/* 281 */     if (c == null) {
/* 282 */       return new Dimension();
/*     */     }
/*     */     
/* 285 */     if (!this.isMinimumSizeDirty) {
/* 286 */       return new Dimension(this.cachedMinimumSize);
/*     */     }
/*     */     
/* 289 */     Dimension size = null;
/*     */     
/* 291 */     if ((!this.comboBox.isEditable()) && (this.arrowButton != null) && ((this.arrowButton instanceof LiquidComboBoxButton)))
/*     */     {
/* 293 */       LiquidComboBoxButton button = (LiquidComboBoxButton)this.arrowButton;
/* 294 */       Insets buttonInsets = new Insets(0, 0, 0, 0);
/* 295 */       Insets insets = this.comboBox.getInsets();
/*     */       
/* 297 */       size = getDisplaySize();
/* 298 */       size.width += comboBoxButtonSize + insets.left + insets.right;
/* 299 */       size.width += buttonInsets.left + buttonInsets.right;
/* 300 */       size.width += buttonInsets.right + button.getComboIcon().getIconWidth();
/*     */       
/* 302 */       size.height += insets.top + insets.bottom;
/* 303 */       size.height += buttonInsets.top + buttonInsets.bottom;
/* 304 */       size.height = Math.max(21, size.height);
/* 305 */     } else if ((this.comboBox.isEditable()) && (this.arrowButton != null) && (this.editor != null))
/*     */     {
/* 307 */       size = super.getMinimumSize(c);
/*     */       
/* 309 */       Insets margin = this.arrowButton.getMargin();
/* 310 */       Insets insets = this.comboBox.getInsets();
/*     */       Insets editorInsets;
/* 312 */       if ((this.editor instanceof JComponent)) {
/* 313 */         editorInsets = ((JComponent)this.editor).getInsets();
/*     */       }
/*     */       
/* 316 */       size.height += margin.top + margin.bottom;
/* 317 */       size.height += insets.top + insets.bottom;
/*     */     }
/*     */     else
/*     */     {
/* 321 */       size = super.getMinimumSize(c);
/*     */     }
/*     */     
/* 324 */     this.cachedMinimumSize.setSize(size.width, size.height);
/* 325 */     this.isMinimumSizeDirty = false;
/*     */     
/* 327 */     return new Dimension(this.cachedMinimumSize);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public class LiquidPropertyChangeListener
/*     */     extends BasicComboBoxUI.PropertyChangeHandler
/*     */   {
/* 335 */     public LiquidPropertyChangeListener() { super(); }
/*     */     
/*     */     public void propertyChange(PropertyChangeEvent e) {
/* 338 */       super.propertyChange(e);
/*     */       
/* 340 */       String propertyName = e.getPropertyName();
/*     */       
/* 342 */       if (propertyName.equals("editable")) {
/* 343 */         LiquidComboBoxButton button = (LiquidComboBoxButton)LiquidComboBoxUI.this.arrowButton;
/* 344 */         button.setIconOnly(LiquidComboBoxUI.this.comboBox.isEditable());
/* 345 */         LiquidComboBoxUI.this.comboBox.repaint();
/* 346 */       } else if (propertyName.equals("background")) {
/* 347 */         Color color = (Color)e.getNewValue();
/* 348 */         LiquidComboBoxUI.this.listBox.setBackground(color);
/* 349 */       } else if (propertyName.equals("foreground")) {
/* 350 */         Color color = (Color)e.getNewValue();
/* 351 */         LiquidComboBoxUI.this.listBox.setForeground(color);
/* 352 */       } else if (propertyName.equals("componentOrientation")) {
/* 353 */         LiquidComboBoxButton button = (LiquidComboBoxButton)LiquidComboBoxUI.this.arrowButton;
/* 354 */         button.setComponentOrientation((ComponentOrientation)e.getNewValue());
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */   public class MetouiaComboBoxLayoutManager
/*     */     implements LayoutManager
/*     */   {
/*     */     public MetouiaComboBoxLayoutManager() {}
/*     */     
/*     */ 
/*     */     public void addLayoutComponent(String name, Component comp) {}
/*     */     
/*     */     public void removeLayoutComponent(Component comp) {}
/*     */     
/*     */     public Dimension preferredLayoutSize(Container parent)
/*     */     {
/* 372 */       JComboBox cb = (JComboBox)parent;
/*     */       
/* 374 */       return parent.getPreferredSize();
/*     */     }
/*     */     
/*     */     public Dimension minimumLayoutSize(Container parent) {
/* 378 */       JComboBox cb = (JComboBox)parent;
/*     */       
/* 380 */       return parent.getMinimumSize();
/*     */     }
/*     */     
/*     */     public void layoutContainer(Container parent) {
/* 384 */       JComboBox cb = (JComboBox)parent;
/* 385 */       int width = cb.getWidth();
/* 386 */       int height = cb.getHeight();
/*     */       
/* 388 */       Rectangle cvb = null;
/*     */       
/* 390 */       if (LiquidComboBoxUI.this.comboBox.isEditable()) {
/* 391 */         if (LiquidComboBoxUI.this.editor != null) {
/* 392 */           cvb = LiquidComboBoxUI.this.rectangleForCurrentValue2();
/* 393 */           LiquidComboBoxUI.this.editor.setBounds(cvb);
/*     */         }
/*     */         
/* 396 */         if (LiquidComboBoxUI.this.arrowButton != null) {
/* 397 */           if ((LiquidComboBoxUI.this.editor != null) && (cvb != null)) {
/* 398 */             int start = cvb.x + cvb.width;
/* 399 */             LiquidComboBoxUI.this.arrowButton.setBounds(start, 0, width - start, height);
/*     */           } else {
/* 401 */             LiquidComboBoxUI.this.arrowButton.setBounds(0, 0, width, height);
/*     */           }
/*     */         }
/*     */       } else {
/* 405 */         LiquidComboBoxUI.this.arrowButton.setBounds(0, 0, width, height);
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   /**
/*     */    * @deprecated
/*     */    */
/*     */   public class LiquidComboPopup
/*     */     extends BasicComboPopup
/*     */   {
/*     */     public LiquidComboPopup(JComboBox cBox)
/*     */     {
/* 423 */       super();
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */     public void delegateFocus(MouseEvent e)
/*     */     {
/* 431 */       super.delegateFocus(e);
/*     */     }
/*     */   }
/*     */   
/*     */   public Skin getSkinCombo()
/*     */   {
/* 437 */     if (this.skinCombo == null)
/*     */     {
/*     */ 
/*     */ 
/*     */ 
/* 442 */       this.skinCombo = new Skin("combobox.png", 4, 10, 6, 18, 4);
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 449 */     return this.skinCombo;
/*     */   }
/*     */ }


/* Location:              E:\java\java学习\hutubill\lib\all.jar!\com\birosoft\liquid\LiquidComboBoxUI.class
 * Java compiler version: 4 (48.0)
 * JD-Core Version:       0.7.1
 */
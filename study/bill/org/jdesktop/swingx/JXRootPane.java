/*     */ package org.jdesktop.swingx;
/*     */ 
/*     */ import java.awt.BorderLayout;
/*     */ import java.awt.Component;
/*     */ import java.awt.Container;
/*     */ import java.awt.Dimension;
/*     */ import java.awt.Insets;
/*     */ import java.awt.LayoutManager;
/*     */ import java.awt.LayoutManager2;
/*     */ import java.awt.Rectangle;
/*     */ import java.awt.event.ActionEvent;
/*     */ import javax.swing.AbstractAction;
/*     */ import javax.swing.Action;
/*     */ import javax.swing.ActionMap;
/*     */ import javax.swing.InputMap;
/*     */ import javax.swing.JButton;
/*     */ import javax.swing.JComponent;
/*     */ import javax.swing.JMenuBar;
/*     */ import javax.swing.JRootPane;
/*     */ import javax.swing.JRootPane.RootLayout;
/*     */ import javax.swing.JToolBar;
/*     */ import javax.swing.KeyStroke;
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
/*     */ public class JXRootPane
/*     */   extends JRootPane
/*     */ {
/*     */   protected JXStatusBar statusBar;
/*     */   private JToolBar toolBar;
/*     */   private JButton cancelButton;
/*     */   
/*     */   protected class XRootLayout
/*     */     extends JRootPane.RootLayout
/*     */   {
/*     */     LayoutManager2 delegate;
/*     */     
/*     */     protected XRootLayout()
/*     */     {
/*  61 */       super();
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     public void setLayoutManager(LayoutManager2 delegate)
/*     */     {
/*  73 */       this.delegate = delegate;
/*     */     }
/*     */     
/*     */     private Dimension delegatePreferredLayoutSize(Container parent) {
/*  77 */       if (this.delegate == null)
/*  78 */         return super.preferredLayoutSize(parent);
/*  79 */       return this.delegate.preferredLayoutSize(parent);
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */     public Dimension preferredLayoutSize(Container parent)
/*     */     {
/*  87 */       Dimension pref = delegatePreferredLayoutSize(parent);
/*  88 */       if ((JXRootPane.this.statusBar != null) && (JXRootPane.this.statusBar.isVisible())) {
/*  89 */         Dimension statusPref = JXRootPane.this.statusBar.getPreferredSize();
/*  90 */         pref.width = Math.max(pref.width, statusPref.width);
/*  91 */         pref.height += statusPref.height;
/*     */       }
/*  93 */       return pref;
/*     */     }
/*     */     
/*     */     private Dimension delegateMinimumLayoutSize(Container parent) {
/*  97 */       if (this.delegate == null)
/*  98 */         return super.minimumLayoutSize(parent);
/*  99 */       return this.delegate.minimumLayoutSize(parent);
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */     public Dimension minimumLayoutSize(Container parent)
/*     */     {
/* 107 */       Dimension pref = delegateMinimumLayoutSize(parent);
/* 108 */       if ((JXRootPane.this.statusBar != null) && (JXRootPane.this.statusBar.isVisible())) {
/* 109 */         Dimension statusPref = JXRootPane.this.statusBar.getMinimumSize();
/* 110 */         pref.width = Math.max(pref.width, statusPref.width);
/* 111 */         pref.height += statusPref.height;
/*     */       }
/* 113 */       return pref;
/*     */     }
/*     */     
/*     */     private Dimension delegateMaximumLayoutSize(Container parent)
/*     */     {
/* 118 */       if (this.delegate == null)
/*     */       {
/* 120 */         return super.maximumLayoutSize(parent); }
/* 121 */       return this.delegate.maximumLayoutSize(parent);
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */     public Dimension maximumLayoutSize(Container target)
/*     */     {
/* 129 */       Dimension pref = delegateMaximumLayoutSize(target);
/* 130 */       if ((JXRootPane.this.statusBar != null) && (JXRootPane.this.statusBar.isVisible())) {
/* 131 */         Dimension statusPref = JXRootPane.this.statusBar.getMaximumSize();
/* 132 */         pref.width = Math.max(pref.width, statusPref.width);
/*     */         
/* 134 */         pref.height += statusPref.height;
/*     */       }
/* 136 */       return pref;
/*     */     }
/*     */     
/*     */     private void delegateLayoutContainer(Container parent) {
/* 140 */       if (this.delegate == null) {
/* 141 */         super.layoutContainer(parent);
/*     */       } else {
/* 143 */         this.delegate.layoutContainer(parent);
/*     */       }
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */     public void layoutContainer(Container parent)
/*     */     {
/* 152 */       delegateLayoutContainer(parent);
/* 153 */       if ((JXRootPane.this.statusBar == null) || (!JXRootPane.this.statusBar.isVisible()))
/* 154 */         return;
/* 155 */       Rectangle b = parent.getBounds();
/* 156 */       Insets i = JXRootPane.this.getInsets();
/* 157 */       int w = b.width - i.right - i.left;
/* 158 */       int h = b.height - i.top - i.bottom;
/* 159 */       Dimension statusPref = JXRootPane.this.statusBar.getPreferredSize();
/* 160 */       JXRootPane.this.statusBar.setBounds(i.right, b.height - i.bottom - statusPref.height, w, statusPref.height);
/*     */       
/* 162 */       if (JXRootPane.this.contentPane != null) {
/* 163 */         Rectangle bounds = JXRootPane.this.contentPane.getBounds();
/* 164 */         JXRootPane.this.contentPane.setBounds(bounds.x, bounds.y, bounds.width, bounds.height - statusPref.height);
/*     */       }
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public JXRootPane()
/*     */   {
/* 188 */     installKeyboardActions();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   protected Container createContentPane()
/*     */   {
/* 196 */     JComponent c = new JXPanel()
/*     */     {
/*     */ 
/*     */       protected void addImpl(Component comp, Object constraints, int index)
/*     */       {
/*     */ 
/* 202 */         synchronized (getTreeLock()) {
/* 203 */           super.addImpl(comp, constraints, index);
/* 204 */           JXRootPane.this.registerStatusBar(comp);
/*     */         }
/*     */       }
/*     */       
/*     */ 
/*     */ 
/*     */ 
/*     */       public void remove(int index)
/*     */       {
/* 213 */         synchronized (getTreeLock()) {
/* 214 */           JXRootPane.this.unregisterStatusBar(getComponent(index));
/* 215 */           super.remove(index);
/*     */         }
/*     */       }
/*     */       
/*     */ 
/*     */ 
/*     */ 
/*     */       public void removeAll()
/*     */       {
/* 224 */         synchronized (getTreeLock()) {
/* 225 */           for (Component c : getComponents()) {
/* 226 */             JXRootPane.this.unregisterStatusBar(c);
/*     */           }
/*     */           
/* 229 */           super.removeAll();
/*     */         }
/*     */       }
/* 232 */     };
/* 233 */     c.setName(getName() + ".contentPane");
/* 234 */     c.setLayout(new BorderLayout()
/*     */     {
/*     */ 
/*     */ 
/*     */       public void addLayoutComponent(Component comp, Object constraints)
/*     */       {
/*     */ 
/* 241 */         if (constraints == null) {
/* 242 */           constraints = "Center";
/*     */         }
/* 244 */         super.addLayoutComponent(comp, constraints);
/*     */       }
/* 246 */     });
/* 247 */     return c;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setLayout(LayoutManager layout)
/*     */   {
/* 256 */     if ((layout instanceof XRootLayout))
/*     */     {
/* 258 */       if ((layout != null) && (layout == getLayout())) {
/* 259 */         ((XRootLayout)layout).setLayoutManager(null);
/*     */       }
/* 261 */       super.setLayout(layout);
/*     */     }
/* 263 */     else if ((layout instanceof LayoutManager2)) {
/* 264 */       ((XRootLayout)getLayout()).setLayoutManager((LayoutManager2)layout);
/* 265 */       if (!isValid()) {
/* 266 */         invalidate();
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected LayoutManager createRootLayout()
/*     */   {
/* 277 */     return new XRootLayout();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   private void installKeyboardActions()
/*     */   {
/* 285 */     Action escAction = new AbstractAction() {
/*     */       public void actionPerformed(ActionEvent evt) {
/* 287 */         JButton cancelButton = JXRootPane.this.getCancelButton();
/* 288 */         if (cancelButton != null) {
/* 289 */           cancelButton.doClick(20);
/*     */         }
/*     */       }
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
/*     */       public boolean isEnabled()
/*     */       {
/* 308 */         return (JXRootPane.this.cancelButton != null) && (JXRootPane.this.cancelButton.isEnabled());
/*     */       }
/* 310 */     };
/* 311 */     getActionMap().put("esc-action", escAction);
/* 312 */     InputMap im = getInputMap(1);
/* 313 */     KeyStroke key = KeyStroke.getKeyStroke(27, 0);
/* 314 */     im.put(key, "esc-action");
/*     */   }
/*     */   
/*     */   private void registerStatusBar(Component comp) {
/* 318 */     if ((this.statusBar == null) || (comp == null)) {
/* 319 */       return;
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 329 */     if ((comp instanceof Container)) {
/* 330 */       Component[] comps = ((Container)comp).getComponents();
/* 331 */       for (int i = 0; i < comps.length; i++) {
/* 332 */         registerStatusBar(comps[i]);
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   private void unregisterStatusBar(Component comp) {
/* 338 */     if ((this.statusBar == null) || (comp == null)) {
/* 339 */       return;
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 349 */     if ((comp instanceof Container)) {
/* 350 */       Component[] comps = ((Container)comp).getComponents();
/* 351 */       for (int i = 0; i < comps.length; i++) {
/* 352 */         unregisterStatusBar(comps[i]);
/*     */       }
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
/*     */   public void setStatusBar(JXStatusBar statusBar)
/*     */   {
/* 366 */     JXStatusBar oldStatusBar = this.statusBar;
/* 367 */     this.statusBar = statusBar;
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
/* 385 */     Component[] comps = getContentPane().getComponents();
/* 386 */     for (int i = 0; i < comps.length; i++)
/*     */     {
/* 388 */       unregisterStatusBar(comps[i]);
/*     */       
/*     */ 
/* 391 */       registerStatusBar(comps[i]);
/*     */     }
/* 393 */     if (oldStatusBar != null) {
/* 394 */       remove(oldStatusBar);
/*     */     }
/* 396 */     if (statusBar != null) {
/* 397 */       add(statusBar);
/*     */     }
/* 399 */     firePropertyChange("statusBar", oldStatusBar, getStatusBar());
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public JXStatusBar getStatusBar()
/*     */   {
/* 408 */     return this.statusBar;
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
/*     */   public void setToolBar(JToolBar toolBar)
/*     */   {
/* 424 */     JToolBar oldToolBar = getToolBar();
/* 425 */     this.toolBar = toolBar;
/*     */     
/* 427 */     if (oldToolBar != null) {
/* 428 */       getContentPane().remove(oldToolBar);
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 439 */     getContentPane().add("North", this.toolBar);
/*     */     
/*     */ 
/* 442 */     getContentPane().validate();
/*     */     
/* 444 */     firePropertyChange("toolBar", oldToolBar, getToolBar());
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public JToolBar getToolBar()
/*     */   {
/* 453 */     return this.toolBar;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setJMenuBar(JMenuBar menuBar)
/*     */   {
/* 461 */     JMenuBar oldMenuBar = this.menuBar;
/*     */     
/* 463 */     super.setJMenuBar(menuBar);
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
/*     */   public void setCancelButton(JButton cancelButton)
/*     */   {
/* 495 */     JButton old = this.cancelButton;
/*     */     
/* 497 */     if (old != cancelButton) {
/* 498 */       this.cancelButton = cancelButton;
/*     */       
/* 500 */       if (old != null) {
/* 501 */         old.repaint();
/*     */       }
/* 503 */       if (cancelButton != null) {
/* 504 */         cancelButton.repaint();
/*     */       }
/*     */     }
/*     */     
/* 508 */     firePropertyChange("cancelButton", old, cancelButton);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public JButton getCancelButton()
/*     */   {
/* 517 */     return this.cancelButton;
/*     */   }
/*     */ }


/* Location:              E:\java\java学习\hutubill\lib\all.jar!\org\jdesktop\swingx\JXRootPane.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */
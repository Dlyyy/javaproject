/*     */ package com.birosoft.liquid;
/*     */ 
/*     */ import java.awt.BorderLayout;
/*     */ import java.awt.Component;
/*     */ import java.awt.Container;
/*     */ import java.awt.Dimension;
/*     */ import java.awt.Insets;
/*     */ import java.awt.Point;
/*     */ import java.awt.Rectangle;
/*     */ import java.awt.event.MouseEvent;
/*     */ import java.beans.PropertyVetoException;
/*     */ import javax.swing.DefaultDesktopManager;
/*     */ import javax.swing.DesktopManager;
/*     */ import javax.swing.JComponent;
/*     */ import javax.swing.JDesktopPane;
/*     */ import javax.swing.JInternalFrame;
/*     */ import javax.swing.JInternalFrame.JDesktopIcon;
/*     */ import javax.swing.JLayeredPane;
/*     */ import javax.swing.LookAndFeel;
/*     */ import javax.swing.SwingUtilities;
/*     */ import javax.swing.UIManager;
/*     */ import javax.swing.border.Border;
/*     */ import javax.swing.event.MouseInputAdapter;
/*     */ import javax.swing.event.MouseInputListener;
/*     */ import javax.swing.plaf.ComponentUI;
/*     */ import javax.swing.plaf.basic.BasicDesktopIconUI;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class LiquidDesktopIconUI
/*     */   extends BasicDesktopIconUI
/*     */ {
/*     */   protected JInternalFrame.JDesktopIcon desktopIcon;
/*     */   protected JInternalFrame frame;
/*     */   protected JComponent iconPane;
/*     */   MouseInputListener mouseInputListener;
/*     */   
/*     */   public static ComponentUI createUI(JComponent c)
/*     */   {
/*  46 */     return new LiquidDesktopIconUI();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public void installUI(JComponent c)
/*     */   {
/*  53 */     this.desktopIcon = ((JInternalFrame.JDesktopIcon)c);
/*  54 */     this.frame = this.desktopIcon.getInternalFrame();
/*  55 */     installDefaults();
/*  56 */     installComponents();
/*     */     
/*     */ 
/*  59 */     JInternalFrame f = this.desktopIcon.getInternalFrame();
/*  60 */     if ((f.isIcon()) && (f.getParent() == null)) {
/*  61 */       JDesktopPane desktop = this.desktopIcon.getDesktopPane();
/*  62 */       if (desktop != null) {
/*  63 */         DesktopManager desktopManager = desktop.getDesktopManager();
/*  64 */         if ((desktopManager instanceof DefaultDesktopManager)) {
/*  65 */           desktopManager.iconifyFrame(f);
/*     */         }
/*     */       }
/*     */     }
/*     */     
/*  70 */     installListeners();
/*  71 */     JLayeredPane.putLayer(this.desktopIcon, JLayeredPane.getLayer(this.frame));
/*     */   }
/*     */   
/*     */   public void uninstallUI(JComponent c) {
/*  75 */     uninstallDefaults();
/*  76 */     uninstallComponents();
/*     */     
/*     */ 
/*  79 */     JInternalFrame f = this.desktopIcon.getInternalFrame();
/*  80 */     if (f.isIcon()) {
/*  81 */       JDesktopPane desktop = this.desktopIcon.getDesktopPane();
/*  82 */       if (desktop != null) {
/*  83 */         DesktopManager desktopManager = desktop.getDesktopManager();
/*  84 */         if ((desktopManager instanceof DefaultDesktopManager))
/*     */         {
/*  86 */           f.putClientProperty("wasIconOnce", null);
/*     */           
/*  88 */           this.desktopIcon.setLocation(Integer.MIN_VALUE, 0);
/*     */         }
/*     */       }
/*     */     }
/*     */     
/*  93 */     uninstallListeners();
/*  94 */     this.frame = null;
/*  95 */     this.desktopIcon = null;
/*     */   }
/*     */   
/*     */   protected void installComponents() {
/*  99 */     this.iconPane = new LiquidInternalFrameTitlePane(this.frame);
/* 100 */     this.desktopIcon.setLayout(new BorderLayout());
/* 101 */     this.desktopIcon.add(this.iconPane, "Center");
/* 102 */     this.desktopIcon.setToolTipText(this.frame.getTitle());
/*     */   }
/*     */   
/*     */   protected void uninstallComponents() {
/* 106 */     this.desktopIcon.remove(this.iconPane);
/* 107 */     this.desktopIcon.setLayout(null);
/* 108 */     this.iconPane = null;
/*     */   }
/*     */   
/*     */   protected void installListeners() {
/* 112 */     this.mouseInputListener = createMouseInputListener();
/* 113 */     this.desktopIcon.addMouseMotionListener(this.mouseInputListener);
/* 114 */     this.desktopIcon.addMouseListener(this.mouseInputListener);
/*     */   }
/*     */   
/*     */   protected void uninstallListeners() {
/* 118 */     this.desktopIcon.removeMouseMotionListener(this.mouseInputListener);
/* 119 */     this.desktopIcon.removeMouseListener(this.mouseInputListener);
/* 120 */     this.mouseInputListener = null;
/*     */   }
/*     */   
/*     */   protected void installDefaults()
/*     */   {
/* 125 */     this.desktopIcon.setOpaque(true);
/*     */     
/*     */ 
/* 128 */     if (LiquidLookAndFeel.winDecoPanther) {
/* 129 */       this.desktopIcon.setBorder(UIManager.getBorder("RootPane.frameBorder"));
/*     */     }
/*     */     else {
/* 132 */       this.desktopIcon.setBorder(new LiquidInternalFrameBorder());
/*     */     }
/*     */   }
/*     */   
/*     */   protected void uninstallDefaults() {
/* 137 */     LookAndFeel.uninstallBorder(this.desktopIcon);
/*     */   }
/*     */   
/*     */   protected MouseInputListener createMouseInputListener() {
/* 141 */     return new MouseInputHandler();
/*     */   }
/*     */   
/*     */   public Dimension getPreferredSize(JComponent c)
/*     */   {
/* 146 */     return getMaximumSize(c);
/*     */   }
/*     */   
/*     */   public Dimension getMinimumSize(JComponent c)
/*     */   {
/* 151 */     Dimension dim = new Dimension(this.iconPane.getMinimumSize());
/* 152 */     Border border = this.frame.getBorder();
/*     */     
/* 154 */     if (border != null) {
/* 155 */       dim.height += border.getBorderInsets(this.frame).bottom + border.getBorderInsets(this.frame).top;
/*     */     }
/*     */     
/* 158 */     return dim;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public Dimension getMaximumSize(JComponent c)
/*     */   {
/* 168 */     return getMinimumSize(c);
/*     */   }
/*     */   
/*     */   public Insets getInsets(JComponent c) {
/* 172 */     JInternalFrame iframe = this.desktopIcon.getInternalFrame();
/* 173 */     Border border = iframe.getBorder();
/* 174 */     if (border != null) {
/* 175 */       return border.getBorderInsets(iframe);
/*     */     }
/* 177 */     return new Insets(0, 0, 0, 0);
/*     */   }
/*     */   
/*     */   public void deiconize() {
/* 181 */     try { this.frame.setIcon(false);
/*     */     }
/*     */     catch (PropertyVetoException e2) {}
/*     */   }
/*     */   
/*     */ 
/*     */   public class MouseInputHandler
/*     */     extends MouseInputAdapter
/*     */   {
/*     */     int _x;
/*     */     int _y;
/*     */     int __x;
/*     */     int __y;
/*     */     Rectangle startingBounds;
/*     */     
/*     */     public MouseInputHandler() {}
/*     */     
/*     */     public void mouseReleased(MouseEvent e)
/*     */     {
/* 200 */       this._x = 0;
/* 201 */       this._y = 0;
/* 202 */       this.__x = 0;
/* 203 */       this.__y = 0;
/* 204 */       this.startingBounds = null;
/*     */       
/*     */       JDesktopPane d;
/* 207 */       if ((d = LiquidDesktopIconUI.this.desktopIcon.getDesktopPane()) != null) {
/* 208 */         DesktopManager dm = d.getDesktopManager();
/* 209 */         dm.endDraggingFrame(LiquidDesktopIconUI.this.desktopIcon);
/*     */       }
/*     */     }
/*     */     
/*     */     public void mousePressed(MouseEvent e)
/*     */     {
/* 215 */       Point p = SwingUtilities.convertPoint((Component)e.getSource(), e.getX(), e.getY(), null);
/*     */       
/* 217 */       this.__x = e.getX();
/* 218 */       this.__y = e.getY();
/* 219 */       this._x = p.x;
/* 220 */       this._y = p.y;
/* 221 */       this.startingBounds = LiquidDesktopIconUI.this.desktopIcon.getBounds();
/*     */       
/*     */       JDesktopPane d;
/* 224 */       if ((d = LiquidDesktopIconUI.this.desktopIcon.getDesktopPane()) != null) {
/* 225 */         DesktopManager dm = d.getDesktopManager();
/* 226 */         dm.beginDraggingFrame(LiquidDesktopIconUI.this.desktopIcon);
/*     */       }
/*     */       try {
/* 229 */         LiquidDesktopIconUI.this.frame.setSelected(true); } catch (PropertyVetoException e1) {}
/* 230 */       if ((LiquidDesktopIconUI.this.desktopIcon.getParent() instanceof JLayeredPane)) {
/* 231 */         ((JLayeredPane)LiquidDesktopIconUI.this.desktopIcon.getParent()).moveToFront(LiquidDesktopIconUI.this.desktopIcon);
/*     */       }
/*     */       
/* 234 */       if ((e.getClickCount() > 1) && 
/* 235 */         (LiquidDesktopIconUI.this.frame.isIconifiable()) && (LiquidDesktopIconUI.this.frame.isIcon())) {
/* 236 */         LiquidDesktopIconUI.this.deiconize();
/*     */       }
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     public void mouseMoved(MouseEvent e) {}
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */     public void mouseDragged(MouseEvent e)
/*     */     {
/* 251 */       Point p = SwingUtilities.convertPoint((Component)e.getSource(), e.getX(), e.getY(), null);
/*     */       
/*     */ 
/* 254 */       Insets i = LiquidDesktopIconUI.this.desktopIcon.getInsets();
/*     */       
/* 256 */       int pWidth = ((JComponent)LiquidDesktopIconUI.this.desktopIcon.getParent()).getWidth();
/* 257 */       int pHeight = ((JComponent)LiquidDesktopIconUI.this.desktopIcon.getParent()).getHeight();
/*     */       
/* 259 */       if (this.startingBounds == null)
/*     */       {
/* 261 */         return;
/*     */       }
/* 263 */       int newX = this.startingBounds.x - (this._x - p.x);
/* 264 */       int newY = this.startingBounds.y - (this._y - p.y);
/*     */       
/* 266 */       if (newX + i.left <= -this.__x)
/* 267 */         newX = -this.__x - i.left;
/* 268 */       if (newY + i.top <= -this.__y)
/* 269 */         newY = -this.__y - i.top;
/* 270 */       if (newX + this.__x + i.right > pWidth)
/* 271 */         newX = pWidth - this.__x - i.right;
/* 272 */       if (newY + this.__y + i.bottom > pHeight) {
/* 273 */         newY = pHeight - this.__y - i.bottom;
/*     */       }
/*     */       JDesktopPane d;
/* 276 */       if ((d = LiquidDesktopIconUI.this.desktopIcon.getDesktopPane()) != null) {
/* 277 */         DesktopManager dm = d.getDesktopManager();
/* 278 */         dm.dragFrame(LiquidDesktopIconUI.this.desktopIcon, newX, newY);
/*     */       } else {
/* 280 */         moveAndRepaint(LiquidDesktopIconUI.this.desktopIcon, newX, newY, LiquidDesktopIconUI.this.desktopIcon.getWidth(), LiquidDesktopIconUI.this.desktopIcon.getHeight());
/*     */       }
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */     public void moveAndRepaint(JComponent f, int newX, int newY, int newWidth, int newHeight)
/*     */     {
/* 288 */       Rectangle r = f.getBounds();
/* 289 */       f.setBounds(newX, newY, newWidth, newHeight);
/* 290 */       SwingUtilities.computeUnion(newX, newY, newWidth, newHeight, r);
/* 291 */       f.getParent().repaint(r.x, r.y, r.width, r.height);
/*     */     }
/*     */   }
/*     */ }


/* Location:              E:\java\java学习\hutubill\lib\all.jar!\com\birosoft\liquid\LiquidDesktopIconUI.class
 * Java compiler version: 4 (48.0)
 * JD-Core Version:       0.7.1
 */
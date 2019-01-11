/*     */ package org.jdesktop.swingx.util;
/*     */ 
/*     */ import java.awt.Component;
/*     */ import java.awt.Container;
/*     */ import java.awt.GraphicsConfiguration;
/*     */ import java.awt.GraphicsDevice;
/*     */ import java.awt.GraphicsEnvironment;
/*     */ import java.awt.Insets;
/*     */ import java.awt.MouseInfo;
/*     */ import java.awt.Point;
/*     */ import java.awt.PointerInfo;
/*     */ import java.awt.Rectangle;
/*     */ import java.awt.Toolkit;
/*     */ import java.awt.Window;
/*     */ import java.awt.event.ComponentAdapter;
/*     */ import java.awt.event.ComponentEvent;
/*     */ import java.awt.event.ComponentListener;
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ import java.util.logging.Level;
/*     */ import java.util.logging.Logger;
/*     */ import javax.swing.JInternalFrame;
/*     */ import javax.swing.JOptionPane;
/*     */ import javax.swing.SwingUtilities;
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
/*     */ public final class WindowUtils
/*     */ {
/*  53 */   private static final Logger LOG = Logger.getLogger(WindowUtils.class.getName());
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
/*     */   public static Point getPointForCentering(Window window)
/*     */   {
/*  80 */     Rectangle usableBounds = getUsableDeviceBounds(window);
/*  81 */     int screenWidth = usableBounds.width;
/*  82 */     int screenHeight = usableBounds.height;
/*  83 */     int width = window.getWidth();
/*  84 */     int height = window.getHeight();
/*     */     
/*  86 */     return new Point((screenWidth - width) / 2 + usableBounds.x, (screenHeight - height) / 2 + usableBounds.y);
/*     */   }
/*     */   
/*     */   private static Rectangle getUsableDeviceBounds(Window window)
/*     */   {
/*  91 */     Window owner = window.getOwner();
/*  92 */     GraphicsConfiguration gc = null;
/*     */     
/*  94 */     if (owner == null) {
/*  95 */       gc = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice().getDefaultConfiguration();
/*     */     }
/*     */     else {
/*  98 */       gc = owner.getGraphicsConfiguration();
/*     */     }
/*     */     
/* 101 */     Insets insets = Toolkit.getDefaultToolkit().getScreenInsets(gc);
/* 102 */     Rectangle bounds = gc.getBounds();
/* 103 */     bounds.x += insets.left;
/* 104 */     bounds.y += insets.top;
/* 105 */     bounds.width -= insets.left + insets.right;
/* 106 */     bounds.height -= insets.top + insets.bottom;
/*     */     
/* 108 */     return bounds;
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
/*     */   public static Point getPointForCentering(JInternalFrame window)
/*     */   {
/*     */     try
/*     */     {
/* 131 */       Point mousePoint = MouseInfo.getPointerInfo().getLocation();
/* 132 */       GraphicsDevice[] devices = GraphicsEnvironment.getLocalGraphicsEnvironment().getScreenDevices();
/*     */       
/* 134 */       for (GraphicsDevice device : devices) {
/* 135 */         Rectangle bounds = device.getDefaultConfiguration().getBounds();
/*     */         
/* 137 */         if ((mousePoint.x >= bounds.x) && (mousePoint.y >= bounds.y) && (mousePoint.x <= bounds.x + bounds.width) && (mousePoint.y <= bounds.y + bounds.height))
/*     */         {
/*     */ 
/*     */ 
/* 141 */           int screenWidth = bounds.width;
/* 142 */           int screenHeight = bounds.height;
/* 143 */           int width = window.getWidth();
/* 144 */           int height = window.getHeight();
/* 145 */           return new Point((screenWidth - width) / 2 + bounds.x, (screenHeight - height) / 2 + bounds.y);
/*     */         }
/*     */       }
/*     */     }
/*     */     catch (Exception e)
/*     */     {
/* 151 */       LOG.log(Level.FINE, e.getLocalizedMessage() + " - this can occur do to a Security exception in sandboxed apps");
/*     */     }
/*     */     
/* 154 */     return new Point(0, 0);
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
/*     */   public static Point getPointForStaggering(Window originWindow)
/*     */   {
/* 170 */     Point origin = originWindow.getLocation();
/* 171 */     Insets insets = originWindow.getInsets();
/* 172 */     origin.x += insets.top;
/* 173 */     origin.y += insets.top;
/* 174 */     return origin;
/*     */   }
/*     */   
/*     */   public static Window findWindow(Component c) {
/* 178 */     if (c == null)
/* 179 */       return JOptionPane.getRootFrame();
/* 180 */     if ((c instanceof Window)) {
/* 181 */       return (Window)c;
/*     */     }
/* 183 */     return findWindow(c.getParent());
/*     */   }
/*     */   
/*     */   public static List<Component> getAllComponents(Container c)
/*     */   {
/* 188 */     Component[] comps = c.getComponents();
/* 189 */     List<Component> compList = new ArrayList();
/* 190 */     for (Component comp : comps) {
/* 191 */       compList.add(comp);
/* 192 */       if ((comp instanceof Container)) {
/* 193 */         compList.addAll(getAllComponents((Container)comp));
/*     */       }
/*     */     }
/* 196 */     return compList;
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
/*     */   @Deprecated
/*     */   public static void setMinimumSizeManager(Window window, int minWidth, int minHeight)
/*     */   {
/* 211 */     ComponentListener[] listeners = window.getComponentListeners();
/* 212 */     ComponentListener listener = null;
/* 213 */     for (ComponentListener l : listeners) {
/* 214 */       if ((l instanceof MinSizeComponentListener)) {
/* 215 */         listener = l;
/* 216 */         break;
/*     */       }
/*     */     }
/* 219 */     if (listener == null) {
/* 220 */       window.addComponentListener(new MinSizeComponentListener(window, minWidth, minHeight));
/*     */     }
/*     */     else {
/* 223 */       ((MinSizeComponentListener)listener).resetSizes(minWidth, minHeight);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */   @Deprecated
/*     */   public static class MinSizeComponentListener
/*     */     extends ComponentAdapter
/*     */   {
/*     */     private Window window;
/*     */     
/*     */     private int minHeight;
/*     */     
/*     */     private int minWidth;
/*     */     
/*     */ 
/*     */     MinSizeComponentListener(Window frame, int minWidth, int minHeight)
/*     */     {
/* 241 */       this.window = frame;
/* 242 */       resetSizes(minWidth, minHeight);
/*     */     }
/*     */     
/*     */     public void resetSizes(int minWidth, int minHeight) {
/* 246 */       this.minWidth = minWidth;
/* 247 */       this.minHeight = minHeight;
/* 248 */       adjustIfNeeded(this.window);
/*     */     }
/*     */     
/*     */     public void componentResized(ComponentEvent evt)
/*     */     {
/* 253 */       adjustIfNeeded((Window)evt.getComponent());
/*     */     }
/*     */     
/*     */     private void adjustIfNeeded(final Window window) {
/* 257 */       boolean doSize = false;
/* 258 */       int newWidth = window.getWidth();
/* 259 */       int newHeight = window.getHeight();
/* 260 */       if (newWidth < this.minWidth) {
/* 261 */         newWidth = this.minWidth;
/* 262 */         doSize = true;
/*     */       }
/* 264 */       if (newHeight < this.minHeight) {
/* 265 */         newHeight = this.minHeight;
/* 266 */         doSize = true;
/*     */       }
/* 268 */       if (doSize) {
/* 269 */         final int w = newWidth;
/* 270 */         final int h = newHeight;
/* 271 */         SwingUtilities.invokeLater(new Runnable() {
/*     */           public void run() {
/* 273 */             window.setSize(w, h);
/*     */           }
/*     */         });
/*     */       }
/*     */     }
/*     */   }
/*     */ }


/* Location:              E:\java\java学习\hutubill\lib\all.jar!\org\jdesktop\swingx\util\WindowUtils.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */
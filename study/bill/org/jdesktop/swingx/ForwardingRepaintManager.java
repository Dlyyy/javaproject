/*     */ package org.jdesktop.swingx;
/*     */ 
/*     */ import java.applet.Applet;
/*     */ import java.awt.Component;
/*     */ import java.awt.Dimension;
/*     */ import java.awt.Image;
/*     */ import java.awt.Rectangle;
/*     */ import java.awt.Window;
/*     */ import javax.swing.JComponent;
/*     */ import javax.swing.RepaintManager;
/*     */ import org.jdesktop.swingx.util.Contract;
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
/*     */ public class ForwardingRepaintManager
/*     */   extends RepaintManager
/*     */ {
/*     */   private RepaintManager delegate;
/*     */   
/*     */   public ForwardingRepaintManager(RepaintManager delegate)
/*     */   {
/*  64 */     this.delegate = ((RepaintManager)Contract.asNotNull(delegate, "delegate is null"));
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void addDirtyRegion(Applet applet, int x, int y, int w, int h)
/*     */   {
/*  72 */     this.delegate.addDirtyRegion(applet, x, y, w, h);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void addDirtyRegion(JComponent c, int x, int y, int w, int h)
/*     */   {
/*  80 */     this.delegate.addDirtyRegion(c, x, y, w, h);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void addDirtyRegion(Window window, int x, int y, int w, int h)
/*     */   {
/*  88 */     this.delegate.addDirtyRegion(window, x, y, w, h);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public synchronized void addInvalidComponent(JComponent invalidComponent)
/*     */   {
/*  96 */     this.delegate.addInvalidComponent(invalidComponent);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public Rectangle getDirtyRegion(JComponent component)
/*     */   {
/* 104 */     return this.delegate.getDirtyRegion(component);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public Dimension getDoubleBufferMaximumSize()
/*     */   {
/* 112 */     return this.delegate.getDoubleBufferMaximumSize();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public Image getOffscreenBuffer(Component c, int proposedWidth, int proposedHeight)
/*     */   {
/* 120 */     return this.delegate.getOffscreenBuffer(c, proposedWidth, proposedHeight);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public Image getVolatileOffscreenBuffer(Component c, int proposedWidth, int proposedHeight)
/*     */   {
/* 128 */     return this.delegate.getVolatileOffscreenBuffer(c, proposedWidth, proposedHeight);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean isCompletelyDirty(JComponent component)
/*     */   {
/* 136 */     return this.delegate.isCompletelyDirty(component);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean isDoubleBufferingEnabled()
/*     */   {
/* 144 */     return this.delegate.isDoubleBufferingEnabled();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void markCompletelyClean(JComponent component)
/*     */   {
/* 152 */     this.delegate.markCompletelyClean(component);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void markCompletelyDirty(JComponent component)
/*     */   {
/* 160 */     this.delegate.markCompletelyDirty(component);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void paintDirtyRegions()
/*     */   {
/* 168 */     this.delegate.paintDirtyRegions();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public synchronized void removeInvalidComponent(JComponent component)
/*     */   {
/* 176 */     this.delegate.removeInvalidComponent(component);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setDoubleBufferingEnabled(boolean flag)
/*     */   {
/* 184 */     this.delegate.setDoubleBufferingEnabled(flag);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setDoubleBufferMaximumSize(Dimension d)
/*     */   {
/* 192 */     this.delegate.setDoubleBufferMaximumSize(d);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public synchronized String toString()
/*     */   {
/* 200 */     return this.delegate.toString();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void validateInvalidComponents()
/*     */   {
/* 208 */     this.delegate.validateInvalidComponents();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public final RepaintManager getDelegateManager()
/*     */   {
/* 218 */     return this.delegate;
/*     */   }
/*     */ }


/* Location:              E:\java\java学习\hutubill\lib\all.jar!\org\jdesktop\swingx\ForwardingRepaintManager.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */
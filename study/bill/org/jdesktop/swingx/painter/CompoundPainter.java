/*     */ package org.jdesktop.swingx.painter;
/*     */ 
/*     */ import java.awt.Graphics2D;
/*     */ import java.awt.geom.AffineTransform;
/*     */ import java.beans.PropertyChangeEvent;
/*     */ import java.beans.PropertyChangeListener;
/*     */ import java.lang.ref.WeakReference;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ public class CompoundPainter<T>
/*     */   extends AbstractPainter<T>
/*     */ {
/*     */   private Handler handler;
/*     */   
/*     */   private static class Handler
/*     */     implements PropertyChangeListener
/*     */   {
/*     */     private final WeakReference<CompoundPainter<?>> ref;
/*     */     
/*     */     public Handler(CompoundPainter<?> painter)
/*     */     {
/*  68 */       this.ref = new WeakReference(painter);
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */     public void propertyChange(PropertyChangeEvent evt)
/*     */     {
/*  76 */       CompoundPainter<?> painter = (CompoundPainter)this.ref.get();
/*     */       
/*  78 */       if (painter == null) {
/*  79 */         AbstractPainter<?> src = (AbstractPainter)evt.getSource();
/*  80 */         src.removePropertyChangeListener(this);
/*     */       } else {
/*  82 */         String property = evt.getPropertyName();
/*     */         
/*  84 */         if (("dirty".equals(property)) && (evt.getNewValue() == Boolean.FALSE)) {
/*  85 */           return;
/*     */         }
/*     */         
/*  88 */         painter.setDirty(true);
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*  95 */   private Painter[] painters = new Painter[0];
/*     */   private AffineTransform transform;
/*  97 */   private boolean clipPreserved = false;
/*     */   
/*  99 */   private boolean checkForDirtyChildPainters = true;
/*     */   
/*     */   public CompoundPainter()
/*     */   {
/* 103 */     this(null);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public CompoundPainter(Painter... painters)
/*     */   {
/* 114 */     this.handler = new Handler(this);
/*     */     
/* 116 */     setPainters(painters);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setPainters(Painter... painters)
/*     */   {
/* 128 */     Painter[] old = getPainters();
/*     */     
/* 130 */     for (Painter p : old) {
/* 131 */       if ((p instanceof AbstractPainter)) {
/* 132 */         ((AbstractPainter)p).removePropertyChangeListener(this.handler);
/*     */       }
/*     */     }
/*     */     
/* 136 */     this.painters = new Painter[painters == null ? 0 : painters.length];
/* 137 */     if (painters != null) {
/* 138 */       System.arraycopy(painters, 0, this.painters, 0, this.painters.length);
/*     */     }
/*     */     
/* 141 */     for (Painter p : this.painters) {
/* 142 */       if ((p instanceof AbstractPainter)) {
/* 143 */         ((AbstractPainter)p).addPropertyChangeListener(this.handler);
/*     */       }
/*     */     }
/*     */     
/* 147 */     setDirty(true);
/* 148 */     firePropertyChange("painters", old, getPainters());
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public final Painter[] getPainters()
/*     */   {
/* 157 */     Painter[] results = new Painter[this.painters.length];
/* 158 */     System.arraycopy(this.painters, 0, results, 0, results.length);
/* 159 */     return results;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean isClipPreserved()
/*     */   {
/* 171 */     return this.clipPreserved;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setClipPreserved(boolean shouldRestoreState)
/*     */   {
/* 183 */     boolean oldShouldRestoreState = isClipPreserved();
/* 184 */     this.clipPreserved = shouldRestoreState;
/* 185 */     setDirty(true);
/* 186 */     firePropertyChange("clipPreserved", Boolean.valueOf(oldShouldRestoreState), Boolean.valueOf(shouldRestoreState));
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public AffineTransform getTransform()
/*     */   {
/* 194 */     return this.transform;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setTransform(AffineTransform transform)
/*     */   {
/* 202 */     AffineTransform old = getTransform();
/* 203 */     this.transform = transform;
/* 204 */     setDirty(true);
/* 205 */     firePropertyChange("transform", old, transform);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected void validate(T object)
/*     */   {
/* 217 */     boolean dirty = false;
/* 218 */     for (Painter p : this.painters) {
/* 219 */       if ((p instanceof AbstractPainter)) {
/* 220 */         AbstractPainter ap = (AbstractPainter)p;
/* 221 */         ap.validate(object);
/* 222 */         if (ap.isDirty()) {
/* 223 */           dirty = true;
/* 224 */           break;
/*     */         }
/*     */       }
/*     */     }
/* 228 */     this.clearLocalCacheOnly = true;
/* 229 */     setDirty(dirty);
/* 230 */     this.clearLocalCacheOnly = false;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/* 236 */   private boolean clearLocalCacheOnly = false;
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
/*     */   public boolean isCheckingDirtyChildPainters()
/*     */   {
/* 254 */     return this.checkForDirtyChildPainters;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setCheckingDirtyChildPainters(boolean b)
/*     */   {
/* 265 */     boolean old = isCheckingDirtyChildPainters();
/* 266 */     this.checkForDirtyChildPainters = b;
/* 267 */     firePropertyChange("checkingDirtyChildPainters", Boolean.valueOf(old), Boolean.valueOf(isCheckingDirtyChildPainters()));
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
/*     */   protected boolean isDirty()
/*     */   {
/* 284 */     boolean dirty = super.isDirty();
/* 285 */     if (dirty) {
/* 286 */       return true;
/*     */     }
/* 288 */     if (isCheckingDirtyChildPainters()) {
/* 289 */       for (Painter p : this.painters) {
/* 290 */         if ((p instanceof AbstractPainter)) {
/* 291 */           AbstractPainter ap = (AbstractPainter)p;
/* 292 */           if (ap.isDirty()) {
/* 293 */             return true;
/*     */           }
/*     */         }
/*     */       }
/*     */     }
/* 298 */     return false;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   protected void setDirty(boolean d)
/*     */   {
/* 306 */     boolean old = super.isDirty();
/* 307 */     boolean ours = isDirty();
/*     */     
/* 309 */     super.setDirty(d);
/*     */     
/*     */ 
/* 312 */     if ((d != old) && (d == ours)) {
/* 313 */       firePropertyChange("dirty", Boolean.valueOf(old), Boolean.valueOf(isDirty()));
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
/*     */   public void clearCache()
/*     */   {
/* 330 */     if (!this.clearLocalCacheOnly) {
/* 331 */       for (Painter p : this.painters) {
/* 332 */         if ((p instanceof AbstractPainter)) {
/* 333 */           AbstractPainter ap = (AbstractPainter)p;
/* 334 */           ap.clearCache();
/*     */         }
/*     */       }
/*     */     }
/* 338 */     super.clearCache();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public void clearLocalCache()
/*     */   {
/* 345 */     super.clearCache();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   protected void doPaint(Graphics2D g, T component, int width, int height)
/*     */   {
/* 353 */     for (Painter p : getPainters()) {
/* 354 */       Graphics2D temp = (Graphics2D)g.create();
/*     */       try
/*     */       {
/* 357 */         p.paint(temp, component, width, height);
/* 358 */         if (isClipPreserved()) {
/* 359 */           g.setClip(temp.getClip());
/*     */         }
/*     */       } finally {
/* 362 */         temp.dispose();
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected void configureGraphics(Graphics2D g)
/*     */   {
/* 373 */     AffineTransform tx = getTransform();
/* 374 */     if (tx != null) {
/* 375 */       g.setTransform(tx);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   protected boolean shouldUseCache()
/*     */   {
/* 384 */     return ((isCacheable()) && (this.painters != null) && (this.painters.length > 0)) || (super.shouldUseCache());
/*     */   }
/*     */ }


/* Location:              E:\java\java学习\hutubill\lib\all.jar!\org\jdesktop\swingx\painter\CompoundPainter.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */
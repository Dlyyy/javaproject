/*     */ package org.jdesktop.swingx.painter;
/*     */ 
/*     */ import java.awt.AlphaComposite;
/*     */ import java.awt.Composite;
/*     */ import java.awt.Graphics2D;
/*     */ import java.awt.RenderingHints;
/*     */ import java.awt.image.BufferedImage;
/*     */ import java.awt.image.BufferedImageOp;
/*     */ import java.lang.ref.SoftReference;
/*     */ import org.jdesktop.beans.AbstractBean;
/*     */ import org.jdesktop.swingx.graphics.GraphicsUtilities;
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
/*     */ public abstract class AbstractPainter<T>
/*     */   extends AbstractBean
/*     */   implements Painter<T>
/*     */ {
/*     */   private transient SoftReference<BufferedImage> cachedImage;
/*     */   public AbstractPainter() {}
/*     */   
/*     */   public static enum Interpolation
/*     */   {
/*  69 */     Bicubic(RenderingHints.VALUE_INTERPOLATION_BICUBIC), 
/*     */     
/*     */ 
/*     */ 
/*  73 */     Bilinear(RenderingHints.VALUE_INTERPOLATION_BILINEAR), 
/*     */     
/*     */ 
/*     */ 
/*  77 */     NearestNeighbor(RenderingHints.VALUE_INTERPOLATION_NEAREST_NEIGHBOR);
/*     */     
/*     */     private Object value;
/*     */     
/*  81 */     private Interpolation(Object value) { this.value = value; }
/*     */     
/*     */     private void configureGraphics(Graphics2D g) {
/*  84 */       g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, this.value);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*  93 */   private boolean cacheCleared = true;
/*  94 */   private boolean cacheable = false;
/*  95 */   private boolean dirty = false;
/*  96 */   private BufferedImageOp[] filters = new BufferedImageOp[0];
/*  97 */   private boolean antialiasing = true;
/*  98 */   private Interpolation interpolation = Interpolation.NearestNeighbor;
/*  99 */   private boolean visible = true;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   private boolean inPaintContext;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public AbstractPainter(boolean cacheable)
/*     */   {
/* 112 */     setCacheable(cacheable);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public final BufferedImageOp[] getFilters()
/*     */   {
/* 122 */     BufferedImageOp[] results = new BufferedImageOp[this.filters.length];
/* 123 */     System.arraycopy(this.filters, 0, results, 0, results.length);
/* 124 */     return results;
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
/*     */   public void setFilters(BufferedImageOp... effects)
/*     */   {
/* 137 */     if (effects == null) effects = new BufferedImageOp[0];
/* 138 */     BufferedImageOp[] old = getFilters();
/* 139 */     this.filters = new BufferedImageOp[effects == null ? 0 : effects.length];
/* 140 */     System.arraycopy(effects, 0, this.filters, 0, this.filters.length);
/* 141 */     setDirty(true);
/* 142 */     firePropertyChange("filters", old, getFilters());
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean isAntialiasing()
/*     */   {
/* 151 */     return this.antialiasing;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public void setAntialiasing(boolean value)
/*     */   {
/* 158 */     boolean old = isAntialiasing();
/* 159 */     this.antialiasing = value;
/* 160 */     if (old != value) setDirty(true);
/* 161 */     firePropertyChange("antialiasing", Boolean.valueOf(old), Boolean.valueOf(isAntialiasing()));
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public Interpolation getInterpolation()
/*     */   {
/* 170 */     return this.interpolation;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setInterpolation(Interpolation value)
/*     */   {
/* 179 */     Object old = getInterpolation();
/* 180 */     this.interpolation = (value == null ? Interpolation.NearestNeighbor : value);
/* 181 */     if (old != value) setDirty(true);
/* 182 */     firePropertyChange("interpolation", old, getInterpolation());
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean isVisible()
/*     */   {
/* 194 */     return this.visible;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setVisible(boolean visible)
/*     */   {
/* 206 */     boolean old = isVisible();
/* 207 */     this.visible = visible;
/* 208 */     if (old != visible) { setDirty(true);
/*     */     }
/*     */     
/* 211 */     firePropertyChange("visible", Boolean.valueOf(old), Boolean.valueOf(isVisible()));
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
/*     */   public boolean isCacheable()
/*     */   {
/* 224 */     return this.cacheable;
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
/*     */   public void setCacheable(boolean cacheable)
/*     */   {
/* 239 */     boolean old = isCacheable();
/* 240 */     this.cacheable = cacheable;
/* 241 */     firePropertyChange("cacheable", Boolean.valueOf(old), Boolean.valueOf(isCacheable()));
/* 242 */     if (!isCacheable()) {
/* 243 */       clearCache();
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
/*     */   public void clearCache()
/*     */   {
/* 257 */     BufferedImage cache = this.cachedImage == null ? null : (BufferedImage)this.cachedImage.get();
/* 258 */     if (cache != null) {
/* 259 */       cache.flush();
/*     */     }
/* 261 */     this.cacheCleared = true;
/* 262 */     if (!isCacheable()) {
/* 263 */       this.cachedImage = null;
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   boolean isCacheCleared()
/*     */   {
/* 272 */     return this.cacheCleared;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected void validate(T object) {}
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
/* 293 */     return this.dirty;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected void setDirty(boolean d)
/*     */   {
/* 303 */     boolean old = isDirty();
/* 304 */     this.dirty = d;
/* 305 */     firePropertyChange("dirty", Boolean.valueOf(old), Boolean.valueOf(isDirty()));
/* 306 */     if (isDirty()) {
/* 307 */       clearCache();
/*     */     }
/*     */   }
/*     */   
/*     */   boolean isInPaintContext() {
/* 312 */     return this.inPaintContext;
/*     */   }
/*     */   
/*     */   void setInPaintContext(boolean inPaintContext) {
/* 316 */     this.inPaintContext = inPaintContext;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected boolean shouldUseCache()
/*     */   {
/* 328 */     return (isCacheable()) || (this.filters.length > 0);
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
/*     */   protected void configureGraphics(Graphics2D g)
/*     */   {
/* 345 */     if (isAntialiasing()) {
/* 346 */       g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
/*     */     }
/*     */     else {
/* 349 */       g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_OFF);
/*     */     }
/*     */     
/*     */ 
/* 353 */     getInterpolation().configureGraphics(g);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected abstract void doPaint(Graphics2D paramGraphics2D, T paramT, int paramInt1, int paramInt2);
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public final void paint(Graphics2D g, T obj, int width, int height)
/*     */   {
/* 371 */     if (g == null) {
/* 372 */       throw new NullPointerException("The Graphics2D must be supplied");
/*     */     }
/*     */     
/* 375 */     if ((!isVisible()) || (width < 1) || (height < 1)) {
/* 376 */       return;
/*     */     }
/*     */     
/* 379 */     configureGraphics(g);
/*     */     
/*     */ 
/* 382 */     if ((shouldUseCache()) || (this.filters.length > 0)) {
/* 383 */       validate(obj);
/* 384 */       BufferedImage cache = this.cachedImage == null ? null : (BufferedImage)this.cachedImage.get();
/* 385 */       boolean invalidCache = (null == cache) || (cache.getWidth() != width) || (cache.getHeight() != height);
/*     */       
/*     */ 
/*     */ 
/* 389 */       if ((this.cacheCleared) || (invalidCache) || (isDirty()))
/*     */       {
/*     */ 
/* 392 */         if (invalidCache) {
/* 393 */           cache = GraphicsUtilities.createCompatibleTranslucentImage(width, height);
/*     */         }
/* 395 */         Graphics2D gfx = cache.createGraphics();
/*     */         try
/*     */         {
/* 398 */           gfx.setClip(0, 0, width, height);
/*     */           
/* 400 */           if (!invalidCache)
/*     */           {
/*     */ 
/*     */ 
/* 404 */             Composite composite = gfx.getComposite();
/* 405 */             gfx.setComposite(AlphaComposite.Clear);
/* 406 */             gfx.fillRect(0, 0, width, height);
/* 407 */             gfx.setComposite(composite);
/*     */           }
/*     */           
/* 410 */           configureGraphics(gfx);
/* 411 */           doPaint(gfx, obj, width, height);
/*     */         } finally {
/* 413 */           gfx.dispose();
/*     */         }
/*     */         
/* 416 */         if (!isInPaintContext()) {
/* 417 */           for (BufferedImageOp f : getFilters()) {
/* 418 */             cache = f.filter(cache, null);
/*     */           }
/*     */         }
/*     */         
/*     */ 
/* 423 */         if (shouldUseCache()) {
/* 424 */           this.cachedImage = new SoftReference(cache);
/* 425 */           this.cacheCleared = false;
/*     */         }
/*     */       }
/*     */       
/* 429 */       g.drawImage(cache, 0, 0, null);
/*     */     }
/*     */     else {
/* 432 */       doPaint(g, obj, width, height);
/*     */     }
/*     */     
/*     */ 
/* 436 */     setDirty(false);
/*     */   }
/*     */ }


/* Location:              E:\java\java学习\hutubill\lib\all.jar!\org\jdesktop\swingx\painter\AbstractPainter.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */
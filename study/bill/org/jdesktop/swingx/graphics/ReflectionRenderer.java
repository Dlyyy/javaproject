/*     */ package org.jdesktop.swingx.graphics;
/*     */ 
/*     */ import java.awt.AlphaComposite;
/*     */ import java.awt.Color;
/*     */ import java.awt.GradientPaint;
/*     */ import java.awt.Graphics2D;
/*     */ import java.awt.image.BufferedImage;
/*     */ import java.beans.PropertyChangeListener;
/*     */ import java.beans.PropertyChangeSupport;
/*     */ import org.jdesktop.swingx.image.StackBlurFilter;
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
/*     */ public class ReflectionRenderer
/*     */ {
/*     */   public static final String OPACITY_CHANGED_PROPERTY = "reflection_opacity";
/*     */   public static final String LENGTH_CHANGED_PROPERTY = "reflection_length";
/*     */   public static final String BLUR_ENABLED_CHANGED_PROPERTY = "reflection_blur";
/*     */   private float opacity;
/*     */   private float length;
/*     */   private boolean blurEnabled;
/*     */   private PropertyChangeSupport changeSupport;
/*     */   private StackBlurFilter stackBlurFilter;
/*     */   
/*     */   public ReflectionRenderer()
/*     */   {
/* 156 */     this(0.35F, 0.4F, false);
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
/*     */   public ReflectionRenderer(float opacity)
/*     */   {
/* 179 */     this(opacity, 0.4F, false);
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
/*     */   public ReflectionRenderer(float opacity, float length, boolean blurEnabled)
/*     */   {
/* 203 */     this.changeSupport = new PropertyChangeSupport(this);
/* 204 */     this.stackBlurFilter = new StackBlurFilter(1);
/*     */     
/* 206 */     setOpacity(opacity);
/* 207 */     setLength(length);
/* 208 */     setBlurEnabled(blurEnabled);
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
/*     */   public void addPropertyChangeListener(PropertyChangeListener listener)
/*     */   {
/* 221 */     this.changeSupport.addPropertyChangeListener(listener);
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
/*     */   public void removePropertyChangeListener(PropertyChangeListener listener)
/*     */   {
/* 235 */     this.changeSupport.removePropertyChangeListener(listener);
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
/*     */   public float getOpacity()
/*     */   {
/* 249 */     return this.opacity;
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
/*     */   public void setOpacity(float opacity)
/*     */   {
/* 266 */     float oldOpacity = this.opacity;
/*     */     
/* 268 */     if (opacity < 0.0F) {
/* 269 */       opacity = 0.0F;
/* 270 */     } else if (opacity > 1.0F) {
/* 271 */       opacity = 1.0F;
/*     */     }
/*     */     
/* 274 */     if (oldOpacity != opacity) {
/* 275 */       this.opacity = opacity;
/* 276 */       this.changeSupport.firePropertyChange("reflection_opacity", Float.valueOf(oldOpacity), Float.valueOf(this.opacity));
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
/*     */   public float getLength()
/*     */   {
/* 294 */     return this.length;
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
/*     */   public void setLength(float length)
/*     */   {
/* 313 */     float oldLength = this.length;
/*     */     
/* 315 */     if (length < 0.0F) {
/* 316 */       length = 0.0F;
/* 317 */     } else if (length > 1.0F) {
/* 318 */       length = 1.0F;
/*     */     }
/*     */     
/* 321 */     if (oldLength != length) {
/* 322 */       this.length = length;
/* 323 */       this.changeSupport.firePropertyChange("reflection_length", Float.valueOf(oldLength), Float.valueOf(this.length));
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
/*     */   public boolean isBlurEnabled()
/*     */   {
/* 340 */     return this.blurEnabled;
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
/*     */   public void setBlurEnabled(boolean blurEnabled)
/*     */   {
/* 358 */     if (blurEnabled != this.blurEnabled) {
/* 359 */       boolean oldBlur = this.blurEnabled;
/* 360 */       this.blurEnabled = blurEnabled;
/*     */       
/* 362 */       this.changeSupport.firePropertyChange("reflection_blur", oldBlur, this.blurEnabled);
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
/*     */   public int getEffectiveBlurRadius()
/*     */   {
/* 380 */     return this.stackBlurFilter.getEffectiveRadius();
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
/*     */   public int getBlurRadius()
/*     */   {
/* 395 */     return this.stackBlurFilter.getRadius();
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
/*     */   public void setBlurRadius(int radius)
/*     */   {
/* 409 */     this.stackBlurFilter = new StackBlurFilter(radius);
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
/*     */   public BufferedImage appendReflection(BufferedImage image)
/*     */   {
/* 434 */     BufferedImage reflection = createReflection(image);
/* 435 */     BufferedImage buffer = GraphicsUtilities.createCompatibleTranslucentImage(reflection.getWidth(), image.getHeight() + reflection.getHeight());
/*     */     
/* 437 */     Graphics2D g2 = buffer.createGraphics();
/*     */     try
/*     */     {
/* 440 */       int effectiveRadius = isBlurEnabled() ? this.stackBlurFilter.getEffectiveRadius() : 0;
/*     */       
/* 442 */       g2.drawImage(image, effectiveRadius, 0, null);
/* 443 */       g2.drawImage(reflection, 0, image.getHeight() - effectiveRadius, null);
/*     */     }
/*     */     finally {
/* 446 */       g2.dispose();
/*     */     }
/*     */     
/* 449 */     reflection.flush();
/*     */     
/* 451 */     return buffer;
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
/*     */   public BufferedImage createReflection(BufferedImage image)
/*     */   {
/* 479 */     if (this.length == 0.0F) {
/* 480 */       return GraphicsUtilities.createCompatibleTranslucentImage(1, 1);
/*     */     }
/*     */     
/* 483 */     int blurOffset = isBlurEnabled() ? this.stackBlurFilter.getEffectiveRadius() : 0;
/*     */     
/* 485 */     int height = (int)(image.getHeight() * this.length);
/*     */     
/* 487 */     BufferedImage buffer = GraphicsUtilities.createCompatibleTranslucentImage(image.getWidth() + blurOffset * 2, height + blurOffset * 2);
/*     */     
/*     */ 
/*     */ 
/* 491 */     Graphics2D g2 = buffer.createGraphics();
/*     */     try
/*     */     {
/* 494 */       g2.translate(0, image.getHeight());
/* 495 */       g2.scale(1.0D, -1.0D);
/*     */       
/* 497 */       g2.drawImage(image, blurOffset, -blurOffset, null);
/*     */       
/* 499 */       g2.scale(1.0D, -1.0D);
/* 500 */       g2.translate(0, -image.getHeight());
/*     */       
/* 502 */       g2.setComposite(AlphaComposite.DstIn);
/* 503 */       g2.setPaint(new GradientPaint(0.0F, 0.0F, new Color(0.0F, 0.0F, 0.0F, getOpacity()), 0.0F, buffer.getHeight(), new Color(0.0F, 0.0F, 0.0F, 0.0F), true));
/*     */       
/*     */ 
/* 506 */       g2.fillRect(0, 0, buffer.getWidth(), buffer.getHeight());
/*     */     } finally {
/* 508 */       g2.dispose();
/*     */     }
/*     */     
/* 511 */     return isBlurEnabled() ? this.stackBlurFilter.filter(buffer, null) : buffer;
/*     */   }
/*     */ }


/* Location:              E:\java\java学习\hutubill\lib\all.jar!\org\jdesktop\swingx\graphics\ReflectionRenderer.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */
/*     */ package org.jdesktop.swingx.graphics;
/*     */ 
/*     */ import java.awt.Color;
/*     */ import java.awt.image.BufferedImage;
/*     */ import java.beans.PropertyChangeListener;
/*     */ import java.beans.PropertyChangeSupport;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class ShadowRenderer
/*     */ {
/*     */   public static final String SIZE_CHANGED_PROPERTY = "shadow_size";
/*     */   public static final String OPACITY_CHANGED_PROPERTY = "shadow_opacity";
/*     */   public static final String COLOR_CHANGED_PROPERTY = "shadow_color";
/* 106 */   private int size = 5;
/*     */   
/*     */ 
/* 109 */   private float opacity = 0.5F;
/*     */   
/*     */ 
/* 112 */   private Color color = Color.BLACK;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private PropertyChangeSupport changeSupport;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public ShadowRenderer()
/*     */   {
/* 128 */     this(5, 0.5F, Color.BLACK);
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
/*     */   public ShadowRenderer(int size, float opacity, Color color)
/*     */   {
/* 147 */     this.changeSupport = new PropertyChangeSupport(this);
/*     */     
/* 149 */     setSize(size);
/* 150 */     setOpacity(opacity);
/* 151 */     setColor(color);
/*     */   }
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
/* 163 */     this.changeSupport.addPropertyChangeListener(listener);
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
/*     */   public void removePropertyChangeListener(PropertyChangeListener listener)
/*     */   {
/* 176 */     this.changeSupport.removePropertyChangeListener(listener);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public Color getColor()
/*     */   {
/* 184 */     return this.color;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setColor(Color shadowColor)
/*     */   {
/* 195 */     if (shadowColor != null) {
/* 196 */       Color oldColor = this.color;
/* 197 */       this.color = shadowColor;
/* 198 */       this.changeSupport.firePropertyChange("shadow_color", oldColor, this.color);
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
/*     */   public float getOpacity()
/*     */   {
/* 211 */     return this.opacity;
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
/*     */   public void setOpacity(float shadowOpacity)
/*     */   {
/* 224 */     float oldOpacity = this.opacity;
/*     */     
/* 226 */     if (shadowOpacity < 0.0D) {
/* 227 */       this.opacity = 0.0F;
/* 228 */     } else if (shadowOpacity > 1.0F) {
/* 229 */       this.opacity = 1.0F;
/*     */     } else {
/* 231 */       this.opacity = shadowOpacity;
/*     */     }
/*     */     
/* 234 */     this.changeSupport.firePropertyChange("shadow_opacity", Float.valueOf(oldOpacity), Float.valueOf(this.opacity));
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public int getSize()
/*     */   {
/* 244 */     return this.size;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setSize(int shadowSize)
/*     */   {
/* 256 */     int oldSize = this.size;
/*     */     
/* 258 */     if (shadowSize < 0) {
/* 259 */       this.size = 0;
/*     */     } else {
/* 261 */       this.size = shadowSize;
/*     */     }
/*     */     
/* 264 */     this.changeSupport.firePropertyChange("shadow_size", new Integer(oldSize), new Integer(this.size));
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
/*     */   public BufferedImage createShadow(BufferedImage image)
/*     */   {
/* 282 */     int shadowSize = this.size * 2;
/*     */     
/* 284 */     int srcWidth = image.getWidth();
/* 285 */     int srcHeight = image.getHeight();
/*     */     
/* 287 */     int dstWidth = srcWidth + shadowSize;
/* 288 */     int dstHeight = srcHeight + shadowSize;
/*     */     
/* 290 */     int left = this.size;
/* 291 */     int right = shadowSize - left;
/*     */     
/* 293 */     int yStop = dstHeight - right;
/*     */     
/* 295 */     int shadowRgb = this.color.getRGB() & 0xFFFFFF;
/* 296 */     int[] aHistory = new int[shadowSize];
/*     */     
/*     */ 
/*     */ 
/*     */ 
/* 301 */     BufferedImage dst = new BufferedImage(dstWidth, dstHeight, 2);
/*     */     
/*     */ 
/* 304 */     int[] dstBuffer = new int[dstWidth * dstHeight];
/* 305 */     int[] srcBuffer = new int[srcWidth * srcHeight];
/*     */     
/* 307 */     GraphicsUtilities.getPixels(image, 0, 0, srcWidth, srcHeight, srcBuffer);
/*     */     
/* 309 */     int lastPixelOffset = right * dstWidth;
/* 310 */     float hSumDivider = 1.0F / shadowSize;
/* 311 */     float vSumDivider = this.opacity / shadowSize;
/*     */     
/* 313 */     int[] hSumLookup = new int[256 * shadowSize];
/* 314 */     for (int i = 0; i < hSumLookup.length; i++) {
/* 315 */       hSumLookup[i] = ((int)(i * hSumDivider));
/*     */     }
/*     */     
/* 318 */     int[] vSumLookup = new int[256 * shadowSize];
/* 319 */     for (int i = 0; i < vSumLookup.length; i++) {
/* 320 */       vSumLookup[i] = ((int)(i * vSumDivider));
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 327 */     int srcY = 0; for (int dstOffset = left * dstWidth; srcY < srcHeight; srcY++)
/*     */     {
/*     */ 
/* 330 */       for (int historyIdx = 0; historyIdx < shadowSize;) {
/* 331 */         aHistory[(historyIdx++)] = 0;
/*     */       }
/*     */       
/* 334 */       int aSum = 0;
/* 335 */       historyIdx = 0;
/* 336 */       int srcOffset = srcY * srcWidth;
/*     */       
/*     */ 
/* 339 */       for (int srcX = 0; srcX < srcWidth; srcX++)
/*     */       {
/* 341 */         int a = hSumLookup[aSum];
/* 342 */         dstBuffer[(dstOffset++)] = (a << 24);
/*     */         
/*     */ 
/* 345 */         aSum -= aHistory[historyIdx];
/*     */         
/*     */ 
/* 348 */         a = srcBuffer[(srcOffset + srcX)] >>> 24;
/* 349 */         aHistory[historyIdx] = a;
/* 350 */         aSum += a;
/*     */         
/* 352 */         historyIdx++; if (historyIdx >= shadowSize) {
/* 353 */           historyIdx -= shadowSize;
/*     */         }
/*     */       }
/*     */       
/*     */ 
/* 358 */       for (int i = 0; i < shadowSize; i++)
/*     */       {
/* 360 */         int a = hSumLookup[aSum];
/* 361 */         dstBuffer[(dstOffset++)] = (a << 24);
/*     */         
/*     */ 
/* 364 */         aSum -= aHistory[historyIdx];
/*     */         
/* 366 */         historyIdx++; if (historyIdx >= shadowSize) {
/* 367 */           historyIdx -= shadowSize;
/*     */         }
/*     */       }
/*     */     }
/*     */     
/*     */ 
/* 373 */     int x = 0; for (int bufferOffset = 0; x < dstWidth; bufferOffset = x)
/*     */     {
/* 375 */       int aSum = 0;
/*     */       
/*     */ 
/* 378 */       for (int historyIdx = 0; historyIdx < left;) {
/* 379 */         aHistory[(historyIdx++)] = 0;
/*     */       }
/*     */       
/*     */ 
/* 383 */       for (int y = 0; y < right; bufferOffset += dstWidth) {
/* 384 */         int a = dstBuffer[bufferOffset] >>> 24;
/* 385 */         aHistory[(historyIdx++)] = a;
/* 386 */         aSum += a;y++;
/*     */       }
/*     */       
/*     */ 
/*     */ 
/* 389 */       bufferOffset = x;
/* 390 */       historyIdx = 0;
/*     */       
/*     */ 
/* 393 */       for (int y = 0; y < yStop; bufferOffset += dstWidth)
/*     */       {
/* 395 */         int a = vSumLookup[aSum];
/* 396 */         dstBuffer[bufferOffset] = (a << 24 | shadowRgb);
/*     */         
/* 398 */         aSum -= aHistory[historyIdx];
/*     */         
/* 400 */         a = dstBuffer[(bufferOffset + lastPixelOffset)] >>> 24;
/* 401 */         aHistory[historyIdx] = a;
/* 402 */         aSum += a;
/*     */         
/* 404 */         historyIdx++; if (historyIdx >= shadowSize) {
/* 405 */           historyIdx -= shadowSize;
/*     */         }
/* 393 */         y++;
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
/*     */ 
/* 410 */       for (int y = yStop; y < dstHeight; bufferOffset += dstWidth)
/*     */       {
/* 412 */         int a = vSumLookup[aSum];
/* 413 */         dstBuffer[bufferOffset] = (a << 24 | shadowRgb);
/*     */         
/* 415 */         aSum -= aHistory[historyIdx];
/*     */         
/* 417 */         historyIdx++; if (historyIdx >= shadowSize) {
/* 418 */           historyIdx -= shadowSize;
/*     */         }
/* 410 */         y++;
/*     */       }
/* 373 */       x++;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 423 */     GraphicsUtilities.setPixels(dst, 0, 0, dstWidth, dstHeight, dstBuffer);
/* 424 */     return dst;
/*     */   }
/*     */ }


/* Location:              E:\java\java学习\hutubill\lib\all.jar!\org\jdesktop\swingx\graphics\ShadowRenderer.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */
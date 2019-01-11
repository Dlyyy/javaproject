/*     */ package org.jdesktop.swingx.painter;
/*     */ 
/*     */ import java.awt.BasicStroke;
/*     */ import java.awt.Graphics2D;
/*     */ import java.awt.Image;
/*     */ import java.awt.Insets;
/*     */ import java.awt.Paint;
/*     */ import java.awt.Rectangle;
/*     */ import java.awt.Shape;
/*     */ import java.awt.TexturePaint;
/*     */ import java.awt.geom.Area;
/*     */ import java.awt.image.BufferedImage;
/*     */ import java.io.IOException;
/*     */ import java.net.URL;
/*     */ import java.util.logging.Logger;
/*     */ import javax.imageio.ImageIO;
/*     */ import org.jdesktop.swingx.painter.effects.AreaEffect;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class ImagePainter
/*     */   extends AbstractAreaPainter<Object>
/*     */ {
/*  75 */   private static final Logger LOG = Logger.getLogger(ImagePainter.class.getName());
/*     */   
/*     */ 
/*     */   private transient BufferedImage img;
/*     */   
/*     */ 
/*     */   private boolean horizontalRepeat;
/*     */   
/*     */   private boolean verticalRepeat;
/*     */   
/*  85 */   private boolean scaleToFit = false;
/*  86 */   private ScaleType scaleType = ScaleType.InsideFit;
/*     */   
/*  88 */   public static enum ScaleType { InsideFit,  OutsideFit,  Distort;
/*     */     
/*     */     private ScaleType() {}
/*     */   }
/*     */   
/*     */   public ImagePainter()
/*     */   {
/*  95 */     this((BufferedImage)null);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public ImagePainter(BufferedImage image)
/*     */   {
/* 105 */     this(image, AbstractLayoutPainter.HorizontalAlignment.CENTER, AbstractLayoutPainter.VerticalAlignment.CENTER);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public ImagePainter(BufferedImage image, AbstractLayoutPainter.HorizontalAlignment horizontal, AbstractLayoutPainter.VerticalAlignment vertical)
/*     */   {
/* 116 */     setCacheable(true);
/* 117 */     this.img = image;
/* 118 */     setVerticalAlignment(vertical);
/* 119 */     setHorizontalAlignment(horizontal);
/* 120 */     setFillPaint(null);
/* 121 */     setBorderPaint(null);
/*     */   }
/*     */   
/*     */ 
/*     */   @Deprecated
/*     */   public ImagePainter(URL url)
/*     */     throws IOException
/*     */   {
/* 129 */     this(ImageIO.read(url));
/*     */   }
/*     */   
/*     */ 
/*     */   @Deprecated
/*     */   public ImagePainter(URL url, AbstractLayoutPainter.HorizontalAlignment horizontal, AbstractLayoutPainter.VerticalAlignment vertical)
/*     */     throws IOException
/*     */   {
/* 137 */     this(ImageIO.read(url), horizontal, vertical);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setImage(BufferedImage image)
/*     */   {
/* 146 */     if (image != this.img) {
/* 147 */       Image oldImage = this.img;
/* 148 */       this.img = image;
/* 149 */       setDirty(true);
/* 150 */       firePropertyChange("image", oldImage, this.img);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public BufferedImage getImage()
/*     */   {
/* 159 */     return this.img;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   protected void doPaint(Graphics2D g, Object component, int width, int height)
/*     */   {
/* 167 */     Shape shape = provideShape(g, component, width, height);
/*     */     
/* 169 */     switch (getStyle()) {
/*     */     case BOTH: 
/* 171 */       drawBackground(g, shape, width, height);
/* 172 */       drawBorder(g, shape, width, height);
/* 173 */       break;
/*     */     case FILLED: 
/* 175 */       drawBackground(g, shape, width, height);
/* 176 */       break;
/*     */     case OUTLINE: 
/* 178 */       drawBorder(g, shape, width, height);
/* 179 */       break;
/*     */     }
/*     */     
/*     */   }
/*     */   
/*     */   private void drawBackground(Graphics2D g, Shape shape, int width, int height)
/*     */   {
/* 186 */     Paint p = getFillPaint();
/*     */     
/* 188 */     if (p != null) {
/* 189 */       if (isPaintStretched()) {
/* 190 */         p = calculateSnappedPaint(p, width, height);
/*     */       }
/* 192 */       g.setPaint(p);
/* 193 */       g.fill(shape);
/*     */     }
/*     */     
/* 196 */     if (getAreaEffects() != null) {
/* 197 */       for (AreaEffect ef : getAreaEffects()) {
/* 198 */         ef.apply(g, shape, width, height);
/*     */       }
/*     */     }
/*     */     
/*     */ 
/* 203 */     if (this.img != null) {
/* 204 */       int imgWidth = this.img.getWidth(null);
/* 205 */       int imgHeight = this.img.getHeight(null);
/* 206 */       if ((imgWidth != -1) && (imgHeight != -1))
/*     */       {
/*     */ 
/* 209 */         Rectangle rect = shape.getBounds();
/*     */         
/* 211 */         if ((this.verticalRepeat) || (this.horizontalRepeat)) {
/* 212 */           Shape oldClip = g.getClip();
/* 213 */           Shape clip = g.getClip();
/* 214 */           if (clip == null) {
/* 215 */             clip = new Rectangle(0, 0, width, height);
/*     */           }
/* 217 */           Area area = new Area(clip);
/* 218 */           Insets insets = getInsets();
/* 219 */           area.intersect(new Area(new Rectangle(insets.left, insets.top, width - insets.left - insets.right, height - insets.top - insets.bottom)));
/*     */           
/* 221 */           if ((this.verticalRepeat) && (this.horizontalRepeat)) {
/* 222 */             area.intersect(new Area(new Rectangle(0, 0, width, height)));
/* 223 */             g.setClip(area);
/* 224 */           } else if (this.verticalRepeat) {
/* 225 */             area.intersect(new Area(new Rectangle(rect.x, 0, rect.width, height)));
/* 226 */             g.setClip(area);
/*     */           } else {
/* 228 */             area.intersect(new Area(new Rectangle(0, rect.y, width, rect.height)));
/* 229 */             g.setClip(area);
/*     */           }
/*     */           
/* 232 */           TexturePaint tp = new TexturePaint(this.img, rect);
/* 233 */           g.setPaint(tp);
/* 234 */           g.fillRect(0, 0, width, height);
/* 235 */           g.setClip(oldClip);
/*     */         }
/* 237 */         else if (this.scaleToFit) {
/* 238 */           int sw = imgWidth;
/* 239 */           int sh = imgHeight;
/* 240 */           if (this.scaleType == ScaleType.InsideFit) {
/* 241 */             if (sw > width) {
/* 242 */               float scale = width / sw;
/* 243 */               sw = (int)(sw * scale);
/* 244 */               sh = (int)(sh * scale);
/*     */             }
/* 246 */             if (sh > height) {
/* 247 */               float scale = height / sh;
/* 248 */               sw = (int)(sw * scale);
/* 249 */               sh = (int)(sh * scale);
/*     */             }
/*     */           }
/* 252 */           if (this.scaleType == ScaleType.OutsideFit) {
/* 253 */             if (sw > width) {
/* 254 */               float scale = width / sw;
/* 255 */               sw = (int)(sw * scale);
/* 256 */               sh = (int)(sh * scale);
/*     */             }
/* 258 */             if (sh < height) {
/* 259 */               float scale = height / sh;
/* 260 */               sw = (int)(sw * scale);
/* 261 */               sh = (int)(sh * scale);
/*     */             }
/*     */           }
/* 264 */           if (this.scaleType == ScaleType.Distort) {
/* 265 */             sw = width;
/* 266 */             sh = height;
/*     */           }
/* 268 */           int x = 0;
/* 269 */           int y = 0;
/* 270 */           switch (getHorizontalAlignment()) {
/*     */           case CENTER: 
/* 272 */             x = width / 2 - sw / 2;
/* 273 */             break;
/*     */           case RIGHT: 
/* 275 */             x = width - sw;
/*     */           }
/*     */           
/* 278 */           switch (getVerticalAlignment()) {
/*     */           case CENTER: 
/* 280 */             y = height / 2 - sh / 2;
/* 281 */             break;
/*     */           case BOTTOM: 
/* 283 */             y = height - sh;
/*     */           }
/*     */           
/* 286 */           g.drawImage(this.img, x, y, sw, sh, null);
/*     */         } else {
/* 288 */           int sw = rect.width;
/* 289 */           int sh = rect.height;
/* 290 */           if (this.imageScale != 1.0D) {
/* 291 */             sw = (int)(sw * this.imageScale);
/* 292 */             sh = (int)(sh * this.imageScale);
/*     */           }
/* 294 */           g.drawImage(this.img, rect.x, rect.y, sw, sh, null);
/*     */         }
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */   private void drawBorder(Graphics2D g, Shape shape, int width, int height)
/*     */   {
/* 303 */     if (getBorderPaint() != null) {
/* 304 */       g.setPaint(getBorderPaint());
/* 305 */       g.setStroke(new BasicStroke(getBorderWidth()));
/* 306 */       g.draw(shape);
/*     */     }
/*     */   }
/*     */   
/*     */   public void setScaleToFit(boolean scaleToFit) {
/* 311 */     boolean old = isScaleToFit();
/* 312 */     this.scaleToFit = scaleToFit;
/* 313 */     setDirty(true);
/* 314 */     firePropertyChange("scaleToFit", Boolean.valueOf(old), Boolean.valueOf(isScaleToFit()));
/*     */   }
/*     */   
/*     */   public boolean isScaleToFit()
/*     */   {
/* 319 */     return this.scaleToFit;
/*     */   }
/*     */   
/*     */ 
/* 323 */   private double imageScale = 1.0D;
/*     */   
/* 325 */   private Logger log = Logger.getLogger(ImagePainter.class.getName());
/*     */   
/*     */   private String imageString;
/*     */   
/*     */ 
/*     */   public void setImageScale(double imageScale)
/*     */   {
/* 332 */     double old = getImageScale();
/* 333 */     this.imageScale = imageScale;
/* 334 */     setDirty(true);
/* 335 */     firePropertyChange("imageScale", Double.valueOf(old), Double.valueOf(this.imageScale));
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public double getImageScale()
/*     */   {
/* 342 */     return this.imageScale;
/*     */   }
/*     */   
/*     */   @Deprecated
/*     */   private void loadImage() {
/*     */     try {
/* 348 */       String img = getImageString();
/*     */       
/* 350 */       if (img != null) {
/* 351 */         URL url = new URL(img);
/* 352 */         setImage(ImageIO.read(url));
/*     */       }
/*     */     } catch (IOException ex) {
/* 355 */       this.log.severe("ex: " + ex.getMessage());
/* 356 */       ex.printStackTrace();
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   @Deprecated
/*     */   public String getImageString()
/*     */   {
/* 369 */     return this.imageString;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   @Deprecated
/*     */   public void setImageString(String imageString)
/*     */   {
/* 379 */     this.log.fine("setting image string to: " + imageString);
/* 380 */     String old = getImageString();
/* 381 */     this.imageString = imageString;
/* 382 */     loadImage();
/* 383 */     setDirty(true);
/* 384 */     firePropertyChange("imageString", old, imageString);
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
/*     */   public boolean isHorizontalRepeat()
/*     */   {
/* 402 */     return this.horizontalRepeat;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setHorizontalRepeat(boolean horizontalRepeat)
/*     */   {
/* 410 */     boolean old = isHorizontalRepeat();
/* 411 */     this.horizontalRepeat = horizontalRepeat;
/* 412 */     setDirty(true);
/* 413 */     firePropertyChange("horizontalRepeat", Boolean.valueOf(old), Boolean.valueOf(this.horizontalRepeat));
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean isVerticalRepeat()
/*     */   {
/* 421 */     return this.verticalRepeat;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setVerticalRepeat(boolean verticalRepeat)
/*     */   {
/* 429 */     boolean old = isVerticalRepeat();
/* 430 */     this.verticalRepeat = verticalRepeat;
/* 431 */     setDirty(true);
/* 432 */     firePropertyChange("verticalRepeat", Boolean.valueOf(old), Boolean.valueOf(this.verticalRepeat));
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   protected Shape provideShape(Graphics2D g, Object comp, int width, int height)
/*     */   {
/* 440 */     if (getImage() != null) {
/* 441 */       BufferedImage img = getImage();
/* 442 */       int imgWidth = img.getWidth();
/* 443 */       int imgHeight = img.getHeight();
/*     */       
/* 445 */       return calculateLayout(imgWidth, imgHeight, width, height);
/*     */     }
/* 447 */     return new Rectangle(0, 0, 0, 0);
/*     */   }
/*     */   
/*     */   public ScaleType getScaleType()
/*     */   {
/* 452 */     return this.scaleType;
/*     */   }
/*     */   
/*     */   public void setScaleType(ScaleType scaleType) {
/* 456 */     ScaleType old = getScaleType();
/* 457 */     this.scaleType = scaleType;
/* 458 */     setDirty(true);
/* 459 */     firePropertyChange("scaleType", old, getScaleType());
/*     */   }
/*     */ }


/* Location:              E:\java\java学习\hutubill\lib\all.jar!\org\jdesktop\swingx\painter\ImagePainter.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */
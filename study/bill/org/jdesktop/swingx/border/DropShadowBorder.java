/*     */ package org.jdesktop.swingx.border;
/*     */ 
/*     */ import java.awt.Color;
/*     */ import java.awt.Component;
/*     */ import java.awt.Graphics;
/*     */ import java.awt.Graphics2D;
/*     */ import java.awt.Image;
/*     */ import java.awt.Insets;
/*     */ import java.awt.Point;
/*     */ import java.awt.Rectangle;
/*     */ import java.awt.RenderingHints;
/*     */ import java.awt.geom.RoundRectangle2D;
/*     */ import java.awt.geom.RoundRectangle2D.Double;
/*     */ import java.awt.image.BufferedImage;
/*     */ import java.awt.image.ConvolveOp;
/*     */ import java.awt.image.Kernel;
/*     */ import java.io.PrintStream;
/*     */ import java.io.Serializable;
/*     */ import java.util.HashMap;
/*     */ import java.util.Map;
/*     */ import javax.swing.border.Border;
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
/*     */ public class DropShadowBorder
/*     */   implements Border, Serializable
/*     */ {
/*     */   private static final long serialVersionUID = 715287754750604058L;
/*     */   
/*     */   private static enum Position
/*     */   {
/*  66 */     TOP,  TOP_LEFT,  LEFT,  BOTTOM_LEFT, 
/*  67 */     BOTTOM,  BOTTOM_RIGHT,  RIGHT,  TOP_RIGHT;
/*     */     private Position() {} }
/*  69 */   private static final Map<Double, Map<Position, BufferedImage>> CACHE = new HashMap();
/*     */   
/*     */   private Color shadowColor;
/*     */   private int shadowSize;
/*     */   private float shadowOpacity;
/*     */   private int cornerSize;
/*     */   private boolean showTopShadow;
/*     */   private boolean showLeftShadow;
/*     */   private boolean showBottomShadow;
/*     */   private boolean showRightShadow;
/*     */   
/*     */   public DropShadowBorder()
/*     */   {
/*  82 */     this(Color.BLACK, 5);
/*     */   }
/*     */   
/*     */   public DropShadowBorder(Color shadowColor, int shadowSize) {
/*  86 */     this(shadowColor, shadowSize, 0.5F, 12, false, false, true, true);
/*     */   }
/*     */   
/*     */   public DropShadowBorder(boolean showLeftShadow) {
/*  90 */     this(Color.BLACK, 5, 0.5F, 12, false, showLeftShadow, true, true);
/*     */   }
/*     */   
/*     */ 
/*     */   public DropShadowBorder(Color shadowColor, int shadowSize, float shadowOpacity, int cornerSize, boolean showTopShadow, boolean showLeftShadow, boolean showBottomShadow, boolean showRightShadow)
/*     */   {
/*  96 */     this.shadowColor = shadowColor;
/*  97 */     this.shadowSize = shadowSize;
/*  98 */     this.shadowOpacity = shadowOpacity;
/*  99 */     this.cornerSize = cornerSize;
/* 100 */     this.showTopShadow = showTopShadow;
/* 101 */     this.showLeftShadow = showLeftShadow;
/* 102 */     this.showBottomShadow = showBottomShadow;
/* 103 */     this.showRightShadow = showRightShadow;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public void paintBorder(Component c, Graphics graphics, int x, int y, int width, int height)
/*     */   {
/* 110 */     long time = System.currentTimeMillis();
/*     */     
/*     */ 
/*     */ 
/*     */ 
/* 115 */     Map<Position, BufferedImage> images = getImages((Graphics2D)graphics);
/*     */     
/* 117 */     Graphics2D g2 = (Graphics2D)graphics.create();
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     try
/*     */     {
/* 127 */       int shadowOffset = 2;
/*     */       
/* 129 */       Point topLeftShadowPoint = null;
/* 130 */       if ((this.showLeftShadow) || (this.showTopShadow)) {
/* 131 */         topLeftShadowPoint = new Point();
/* 132 */         if ((this.showLeftShadow) && (!this.showTopShadow)) {
/* 133 */           topLeftShadowPoint.setLocation(x, y + shadowOffset);
/* 134 */         } else if ((this.showLeftShadow) && (this.showTopShadow)) {
/* 135 */           topLeftShadowPoint.setLocation(x, y);
/* 136 */         } else if ((!this.showLeftShadow) && (this.showTopShadow)) {
/* 137 */           topLeftShadowPoint.setLocation(x + this.shadowSize, y);
/*     */         }
/*     */       }
/*     */       
/* 141 */       Point bottomLeftShadowPoint = null;
/* 142 */       if ((this.showLeftShadow) || (this.showBottomShadow)) {
/* 143 */         bottomLeftShadowPoint = new Point();
/* 144 */         if ((this.showLeftShadow) && (!this.showBottomShadow)) {
/* 145 */           bottomLeftShadowPoint.setLocation(x, y + height - this.shadowSize - this.shadowSize);
/* 146 */         } else if ((this.showLeftShadow) && (this.showBottomShadow)) {
/* 147 */           bottomLeftShadowPoint.setLocation(x, y + height - this.shadowSize);
/* 148 */         } else if ((!this.showLeftShadow) && (this.showBottomShadow)) {
/* 149 */           bottomLeftShadowPoint.setLocation(x + this.shadowSize, y + height - this.shadowSize);
/*     */         }
/*     */       }
/*     */       
/* 153 */       Point bottomRightShadowPoint = null;
/* 154 */       if ((this.showRightShadow) || (this.showBottomShadow)) {
/* 155 */         bottomRightShadowPoint = new Point();
/* 156 */         if ((this.showRightShadow) && (!this.showBottomShadow)) {
/* 157 */           bottomRightShadowPoint.setLocation(x + width - this.shadowSize, y + height - this.shadowSize - this.shadowSize);
/* 158 */         } else if ((this.showRightShadow) && (this.showBottomShadow)) {
/* 159 */           bottomRightShadowPoint.setLocation(x + width - this.shadowSize, y + height - this.shadowSize);
/* 160 */         } else if ((!this.showRightShadow) && (this.showBottomShadow)) {
/* 161 */           bottomRightShadowPoint.setLocation(x + width - this.shadowSize - this.shadowSize, y + height - this.shadowSize);
/*     */         }
/*     */       }
/*     */       
/* 165 */       Point topRightShadowPoint = null;
/* 166 */       if ((this.showRightShadow) || (this.showTopShadow)) {
/* 167 */         topRightShadowPoint = new Point();
/* 168 */         if ((this.showRightShadow) && (!this.showTopShadow)) {
/* 169 */           topRightShadowPoint.setLocation(x + width - this.shadowSize, y + shadowOffset);
/* 170 */         } else if ((this.showRightShadow) && (this.showTopShadow)) {
/* 171 */           topRightShadowPoint.setLocation(x + width - this.shadowSize, y);
/* 172 */         } else if ((!this.showRightShadow) && (this.showTopShadow)) {
/* 173 */           topRightShadowPoint.setLocation(x + width - this.shadowSize - this.shadowSize, y);
/*     */         }
/*     */       }
/*     */       
/* 177 */       g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
/*     */       
/* 179 */       g2.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_SPEED);
/*     */       
/*     */ 
/* 182 */       if (this.showLeftShadow) {
/* 183 */         Rectangle leftShadowRect = new Rectangle(x, topLeftShadowPoint.y + this.shadowSize, this.shadowSize, bottomLeftShadowPoint.y - topLeftShadowPoint.y - this.shadowSize);
/*     */         
/*     */ 
/*     */ 
/*     */ 
/* 188 */         g2.drawImage((Image)images.get(Position.LEFT), leftShadowRect.x, leftShadowRect.y, leftShadowRect.width, leftShadowRect.height, null);
/*     */       }
/*     */       
/*     */ 
/*     */ 
/* 193 */       if (this.showBottomShadow) {
/* 194 */         Rectangle bottomShadowRect = new Rectangle(bottomLeftShadowPoint.x + this.shadowSize, y + height - this.shadowSize, bottomRightShadowPoint.x - bottomLeftShadowPoint.x - this.shadowSize, this.shadowSize);
/*     */         
/*     */ 
/*     */ 
/*     */ 
/* 199 */         g2.drawImage((Image)images.get(Position.BOTTOM), bottomShadowRect.x, bottomShadowRect.y, bottomShadowRect.width, bottomShadowRect.height, null);
/*     */       }
/*     */       
/*     */ 
/*     */ 
/* 204 */       if (this.showRightShadow) {
/* 205 */         Rectangle rightShadowRect = new Rectangle(x + width - this.shadowSize, topRightShadowPoint.y + this.shadowSize, this.shadowSize, bottomRightShadowPoint.y - topRightShadowPoint.y - this.shadowSize);
/*     */         
/*     */ 
/*     */ 
/*     */ 
/* 210 */         g2.drawImage((Image)images.get(Position.RIGHT), rightShadowRect.x, rightShadowRect.y, rightShadowRect.width, rightShadowRect.height, null);
/*     */       }
/*     */       
/*     */ 
/*     */ 
/* 215 */       if (this.showTopShadow) {
/* 216 */         Rectangle topShadowRect = new Rectangle(topLeftShadowPoint.x + this.shadowSize, y, topRightShadowPoint.x - topLeftShadowPoint.x - this.shadowSize, this.shadowSize);
/*     */         
/*     */ 
/*     */ 
/*     */ 
/* 221 */         g2.drawImage((Image)images.get(Position.TOP), topShadowRect.x, topShadowRect.y, topShadowRect.width, topShadowRect.height, null);
/*     */       }
/*     */       
/*     */ 
/*     */ 
/* 226 */       if ((this.showLeftShadow) || (this.showTopShadow)) {
/* 227 */         g2.drawImage((Image)images.get(Position.TOP_LEFT), topLeftShadowPoint.x, topLeftShadowPoint.y, null);
/*     */       }
/*     */       
/* 230 */       if ((this.showLeftShadow) || (this.showBottomShadow)) {
/* 231 */         g2.drawImage((Image)images.get(Position.BOTTOM_LEFT), bottomLeftShadowPoint.x, bottomLeftShadowPoint.y, null);
/*     */       }
/*     */       
/* 234 */       if ((this.showRightShadow) || (this.showBottomShadow)) {
/* 235 */         g2.drawImage((Image)images.get(Position.BOTTOM_RIGHT), bottomRightShadowPoint.x, bottomRightShadowPoint.y, null);
/*     */       }
/*     */       
/* 238 */       if ((this.showRightShadow) || (this.showTopShadow)) {
/* 239 */         g2.drawImage((Image)images.get(Position.TOP_RIGHT), topRightShadowPoint.x, topRightShadowPoint.y, null);
/*     */       }
/*     */     }
/*     */     finally {
/* 243 */       g2.dispose();
/*     */     }
/* 245 */     System.out.println(System.currentTimeMillis() - time + "ms");
/*     */   }
/*     */   
/*     */ 
/*     */   private Map<Position, BufferedImage> getImages(Graphics2D g2)
/*     */   {
/* 251 */     Map<Position, BufferedImage> images = (Map)CACHE.get(Double.valueOf(this.shadowSize + this.shadowColor.hashCode() * 0.3D + this.shadowOpacity * 0.12D));
/* 252 */     if (images == null) {
/* 253 */       images = new HashMap();
/*     */       
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 269 */       int rectWidth = this.cornerSize + 1;
/* 270 */       RoundRectangle2D rect = new RoundRectangle2D.Double(0.0D, 0.0D, rectWidth, rectWidth, this.cornerSize, this.cornerSize);
/* 271 */       int imageWidth = rectWidth + this.shadowSize * 2;
/* 272 */       BufferedImage image = GraphicsUtilities.createCompatibleTranslucentImage(imageWidth, imageWidth);
/* 273 */       Graphics2D buffer = (Graphics2D)image.getGraphics();
/*     */       try
/*     */       {
/* 276 */         buffer.setPaint(new Color(this.shadowColor.getRed(), this.shadowColor.getGreen(), this.shadowColor.getBlue(), (int)(this.shadowOpacity * 255.0F)));
/*     */         
/*     */ 
/* 279 */         buffer.translate(this.shadowSize, this.shadowSize);
/* 280 */         buffer.fill(rect);
/*     */       } finally {
/* 282 */         buffer.dispose();
/*     */       }
/*     */       
/* 285 */       float blurry = 1.0F / (this.shadowSize * this.shadowSize);
/* 286 */       float[] blurKernel = new float[this.shadowSize * this.shadowSize];
/* 287 */       for (int i = 0; i < blurKernel.length; i++) {
/* 288 */         blurKernel[i] = blurry;
/*     */       }
/* 290 */       ConvolveOp blur = new ConvolveOp(new Kernel(this.shadowSize, this.shadowSize, blurKernel));
/* 291 */       BufferedImage targetImage = GraphicsUtilities.createCompatibleTranslucentImage(imageWidth, imageWidth);
/* 292 */       ((Graphics2D)targetImage.getGraphics()).drawImage(image, blur, -(this.shadowSize / 2), -(this.shadowSize / 2));
/*     */       
/* 294 */       int x = 1;
/* 295 */       int y = 1;
/* 296 */       int w = this.shadowSize;
/* 297 */       int h = this.shadowSize;
/* 298 */       images.put(Position.TOP_LEFT, getSubImage(targetImage, x, y, w, h));
/* 299 */       x = 1;
/* 300 */       y = h;
/* 301 */       w = this.shadowSize;
/* 302 */       h = 1;
/* 303 */       images.put(Position.LEFT, getSubImage(targetImage, x, y, w, h));
/* 304 */       x = 1;
/* 305 */       y = rectWidth;
/* 306 */       w = this.shadowSize;
/* 307 */       h = this.shadowSize;
/* 308 */       images.put(Position.BOTTOM_LEFT, getSubImage(targetImage, x, y, w, h));
/* 309 */       x = this.cornerSize + 1;
/* 310 */       y = rectWidth;
/* 311 */       w = 1;
/* 312 */       h = this.shadowSize;
/* 313 */       images.put(Position.BOTTOM, getSubImage(targetImage, x, y, w, h));
/* 314 */       x = rectWidth;
/* 315 */       y = x;
/* 316 */       w = this.shadowSize;
/* 317 */       h = this.shadowSize;
/* 318 */       images.put(Position.BOTTOM_RIGHT, getSubImage(targetImage, x, y, w, h));
/* 319 */       x = rectWidth;
/* 320 */       y = this.cornerSize + 1;
/* 321 */       w = this.shadowSize;
/* 322 */       h = 1;
/* 323 */       images.put(Position.RIGHT, getSubImage(targetImage, x, y, w, h));
/* 324 */       x = rectWidth;
/* 325 */       y = 1;
/* 326 */       w = this.shadowSize;
/* 327 */       h = this.shadowSize;
/* 328 */       images.put(Position.TOP_RIGHT, getSubImage(targetImage, x, y, w, h));
/* 329 */       x = this.shadowSize;
/* 330 */       y = 1;
/* 331 */       w = 1;
/* 332 */       h = this.shadowSize;
/* 333 */       images.put(Position.TOP, getSubImage(targetImage, x, y, w, h));
/*     */       
/* 335 */       image.flush();
/* 336 */       CACHE.put(Double.valueOf(this.shadowSize + this.shadowColor.hashCode() * 0.3D + this.shadowOpacity * 0.12D), images);
/*     */     }
/* 338 */     return images;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private BufferedImage getSubImage(BufferedImage img, int x, int y, int w, int h)
/*     */   {
/* 349 */     BufferedImage ret = GraphicsUtilities.createCompatibleTranslucentImage(w, h);
/* 350 */     Graphics2D g2 = ret.createGraphics();
/*     */     try
/*     */     {
/* 353 */       g2.drawImage(img, 0, 0, w, h, x, y, x + w, y + h, null);
/*     */ 
/*     */     }
/*     */     finally
/*     */     {
/* 358 */       g2.dispose();
/*     */     }
/*     */     
/* 361 */     return ret;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public Insets getBorderInsets(Component c)
/*     */   {
/* 368 */     int top = this.showTopShadow ? this.shadowSize : 0;
/* 369 */     int left = this.showLeftShadow ? this.shadowSize : 0;
/* 370 */     int bottom = this.showBottomShadow ? this.shadowSize : 0;
/* 371 */     int right = this.showRightShadow ? this.shadowSize : 0;
/* 372 */     return new Insets(top, left, bottom, right);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public boolean isBorderOpaque()
/*     */   {
/* 379 */     return false;
/*     */   }
/*     */   
/*     */   public boolean isShowTopShadow() {
/* 383 */     return this.showTopShadow;
/*     */   }
/*     */   
/*     */   public boolean isShowLeftShadow() {
/* 387 */     return this.showLeftShadow;
/*     */   }
/*     */   
/*     */   public boolean isShowRightShadow() {
/* 391 */     return this.showRightShadow;
/*     */   }
/*     */   
/*     */   public boolean isShowBottomShadow() {
/* 395 */     return this.showBottomShadow;
/*     */   }
/*     */   
/*     */   public int getShadowSize() {
/* 399 */     return this.shadowSize;
/*     */   }
/*     */   
/*     */   public Color getShadowColor() {
/* 403 */     return this.shadowColor;
/*     */   }
/*     */   
/*     */   public float getShadowOpacity() {
/* 407 */     return this.shadowOpacity;
/*     */   }
/*     */   
/*     */   public int getCornerSize() {
/* 411 */     return this.cornerSize;
/*     */   }
/*     */   
/*     */   public void setShadowColor(Color shadowColor) {
/* 415 */     this.shadowColor = shadowColor;
/*     */   }
/*     */   
/*     */   public void setShadowSize(int shadowSize) {
/* 419 */     this.shadowSize = shadowSize;
/*     */   }
/*     */   
/*     */   public void setShadowOpacity(float shadowOpacity) {
/* 423 */     this.shadowOpacity = shadowOpacity;
/*     */   }
/*     */   
/*     */   public void setCornerSize(int cornerSize) {
/* 427 */     this.cornerSize = cornerSize;
/*     */   }
/*     */   
/*     */   public void setShowTopShadow(boolean showTopShadow) {
/* 431 */     this.showTopShadow = showTopShadow;
/*     */   }
/*     */   
/*     */   public void setShowLeftShadow(boolean showLeftShadow) {
/* 435 */     this.showLeftShadow = showLeftShadow;
/*     */   }
/*     */   
/*     */   public void setShowBottomShadow(boolean showBottomShadow) {
/* 439 */     this.showBottomShadow = showBottomShadow;
/*     */   }
/*     */   
/*     */   public void setShowRightShadow(boolean showRightShadow) {
/* 443 */     this.showRightShadow = showRightShadow;
/*     */   }
/*     */ }


/* Location:              E:\java\java学习\hutubill\lib\all.jar!\org\jdesktop\swingx\border\DropShadowBorder.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */
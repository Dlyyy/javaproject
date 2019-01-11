/*     */ package org.jdesktop.swingx.graphics;
/*     */ 
/*     */ import java.awt.Graphics;
/*     */ import java.awt.Graphics2D;
/*     */ import java.awt.GraphicsConfiguration;
/*     */ import java.awt.GraphicsDevice;
/*     */ import java.awt.GraphicsEnvironment;
/*     */ import java.awt.Image;
/*     */ import java.awt.RenderingHints;
/*     */ import java.awt.Shape;
/*     */ import java.awt.geom.Area;
/*     */ import java.awt.image.BufferedImage;
/*     */ import java.awt.image.ColorModel;
/*     */ import java.awt.image.Raster;
/*     */ import java.awt.image.WritableRaster;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.net.URL;
/*     */ import javax.imageio.ImageIO;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class GraphicsUtilities
/*     */ {
/*     */   private static GraphicsConfiguration getGraphicsConfiguration()
/*     */   {
/*  95 */     return GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice().getDefaultConfiguration();
/*     */   }
/*     */   
/*     */   private static boolean isHeadless()
/*     */   {
/* 100 */     return GraphicsEnvironment.isHeadless();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static BufferedImage convertToBufferedImage(Image img)
/*     */   {
/* 111 */     BufferedImage buff = createCompatibleTranslucentImage(img.getWidth(null), img.getHeight(null));
/*     */     
/* 113 */     Graphics2D g2 = buff.createGraphics();
/*     */     try
/*     */     {
/* 116 */       g2.drawImage(img, 0, 0, null);
/*     */     } finally {
/* 118 */       g2.dispose();
/*     */     }
/*     */     
/* 121 */     return buff;
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
/*     */   public static BufferedImage createColorModelCompatibleImage(BufferedImage image)
/*     */   {
/* 136 */     ColorModel cm = image.getColorModel();
/* 137 */     return new BufferedImage(cm, cm.createCompatibleWritableRaster(image.getWidth(), image.getHeight()), cm.isAlphaPremultiplied(), null);
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
/*     */   public static BufferedImage createCompatibleImage(BufferedImage image)
/*     */   {
/* 163 */     return createCompatibleImage(image, image.getWidth(), image.getHeight());
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
/*     */   public static BufferedImage createCompatibleImage(BufferedImage image, int width, int height)
/*     */   {
/* 189 */     return isHeadless() ? new BufferedImage(width, height, image.getType()) : getGraphicsConfiguration().createCompatibleImage(width, height, image.getTransparency());
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
/*     */   public static BufferedImage createCompatibleImage(int width, int height)
/*     */   {
/* 213 */     return isHeadless() ? new BufferedImage(width, height, 1) : getGraphicsConfiguration().createCompatibleImage(width, height);
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
/*     */   public static BufferedImage createCompatibleTranslucentImage(int width, int height)
/*     */   {
/* 237 */     return isHeadless() ? new BufferedImage(width, height, 2) : getGraphicsConfiguration().createCompatibleImage(width, height, 3);
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
/*     */   public static BufferedImage loadCompatibleImage(InputStream in)
/*     */     throws IOException
/*     */   {
/* 263 */     BufferedImage image = ImageIO.read(in);
/* 264 */     if (image == null) return null;
/* 265 */     return toCompatibleImage(image);
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
/*     */   public static BufferedImage loadCompatibleImage(URL resource)
/*     */     throws IOException
/*     */   {
/* 285 */     BufferedImage image = ImageIO.read(resource);
/* 286 */     return toCompatibleImage(image);
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
/*     */   public static BufferedImage toCompatibleImage(BufferedImage image)
/*     */   {
/* 307 */     if (isHeadless()) {
/* 308 */       return image;
/*     */     }
/*     */     
/* 311 */     if (image.getColorModel().equals(getGraphicsConfiguration().getColorModel()))
/*     */     {
/* 313 */       return image;
/*     */     }
/*     */     
/* 316 */     BufferedImage compatibleImage = getGraphicsConfiguration().createCompatibleImage(image.getWidth(), image.getHeight(), image.getTransparency());
/*     */     
/*     */ 
/*     */ 
/* 320 */     Graphics g = compatibleImage.getGraphics();
/*     */     try
/*     */     {
/* 323 */       g.drawImage(image, 0, 0, null);
/*     */     } finally {
/* 325 */       g.dispose();
/*     */     }
/*     */     
/* 328 */     return compatibleImage;
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
/*     */   public static BufferedImage createThumbnailFast(BufferedImage image, int newSize)
/*     */   {
/* 356 */     int width = image.getWidth();
/* 357 */     int height = image.getHeight();
/*     */     
/* 359 */     if (width > height) {
/* 360 */       if (newSize >= width) {
/* 361 */         throw new IllegalArgumentException("newSize must be lower than the image width");
/*     */       }
/* 363 */       if (newSize <= 0) {
/* 364 */         throw new IllegalArgumentException("newSize must be greater than 0");
/*     */       }
/*     */       
/*     */ 
/* 368 */       float ratio = width / height;
/* 369 */       width = newSize;
/* 370 */       height = (int)(newSize / ratio);
/*     */     } else {
/* 372 */       if (newSize >= height) {
/* 373 */         throw new IllegalArgumentException("newSize must be lower than the image height");
/*     */       }
/* 375 */       if (newSize <= 0) {
/* 376 */         throw new IllegalArgumentException("newSize must be greater than 0");
/*     */       }
/*     */       
/*     */ 
/* 380 */       float ratio = height / width;
/* 381 */       height = newSize;
/* 382 */       width = (int)(newSize / ratio);
/*     */     }
/*     */     
/* 385 */     BufferedImage temp = createCompatibleImage(image, width, height);
/* 386 */     Graphics2D g2 = temp.createGraphics();
/*     */     try
/*     */     {
/* 389 */       g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
/*     */       
/* 391 */       g2.drawImage(image, 0, 0, temp.getWidth(), temp.getHeight(), null);
/*     */     } finally {
/* 393 */       g2.dispose();
/*     */     }
/*     */     
/* 396 */     return temp;
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
/*     */   public static BufferedImage createThumbnailFast(BufferedImage image, int newWidth, int newHeight)
/*     */   {
/* 423 */     if ((newWidth >= image.getWidth()) || (newHeight >= image.getHeight()))
/*     */     {
/* 425 */       throw new IllegalArgumentException("newWidth and newHeight cannot be greater than the image dimensions");
/*     */     }
/*     */     
/* 428 */     if ((newWidth <= 0) || (newHeight <= 0)) {
/* 429 */       throw new IllegalArgumentException("newWidth and newHeight must be greater than 0");
/*     */     }
/*     */     
/*     */ 
/* 433 */     BufferedImage temp = createCompatibleImage(image, newWidth, newHeight);
/* 434 */     Graphics2D g2 = temp.createGraphics();
/*     */     try
/*     */     {
/* 437 */       g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
/*     */       
/* 439 */       g2.drawImage(image, 0, 0, temp.getWidth(), temp.getHeight(), null);
/*     */     } finally {
/* 441 */       g2.dispose();
/*     */     }
/*     */     
/* 444 */     return temp;
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
/*     */   public static BufferedImage createThumbnail(BufferedImage image, int newSize)
/*     */   {
/* 470 */     int width = image.getWidth();
/* 471 */     int height = image.getHeight();
/*     */     
/* 473 */     boolean isTranslucent = image.getTransparency() != 1;
/* 474 */     boolean isWidthGreater = width > height;
/*     */     
/* 476 */     if (isWidthGreater) {
/* 477 */       if (newSize >= width) {
/* 478 */         throw new IllegalArgumentException("newSize must be lower than the image width");
/*     */       }
/*     */     }
/* 481 */     else if (newSize >= height) {
/* 482 */       throw new IllegalArgumentException("newSize must be lower than the image height");
/*     */     }
/*     */     
/*     */ 
/* 486 */     if (newSize <= 0) {
/* 487 */       throw new IllegalArgumentException("newSize must be greater than 0");
/*     */     }
/*     */     
/*     */ 
/* 491 */     float ratioWH = width / height;
/* 492 */     float ratioHW = height / width;
/*     */     
/* 494 */     BufferedImage thumb = image;
/* 495 */     BufferedImage temp = null;
/*     */     
/* 497 */     Graphics2D g2 = null;
/*     */     try
/*     */     {
/* 500 */       int previousWidth = width;
/* 501 */       int previousHeight = height;
/*     */       do
/*     */       {
/* 504 */         if (isWidthGreater) {
/* 505 */           width /= 2;
/* 506 */           if (width < newSize) {
/* 507 */             width = newSize;
/*     */           }
/* 509 */           height = (int)(width / ratioWH);
/*     */         } else {
/* 511 */           height /= 2;
/* 512 */           if (height < newSize) {
/* 513 */             height = newSize;
/*     */           }
/* 515 */           width = (int)(height / ratioHW);
/*     */         }
/*     */         
/* 518 */         if ((temp == null) || (isTranslucent)) {
/* 519 */           if (g2 != null)
/*     */           {
/*     */ 
/*     */ 
/* 523 */             g2.dispose();
/*     */           }
/* 525 */           temp = createCompatibleImage(image, width, height);
/* 526 */           g2 = temp.createGraphics();
/* 527 */           g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
/*     */         }
/*     */         
/* 530 */         g2.drawImage(thumb, 0, 0, width, height, 0, 0, previousWidth, previousHeight, null);
/*     */         
/*     */ 
/* 533 */         previousWidth = width;
/* 534 */         previousHeight = height;
/*     */         
/* 536 */         thumb = temp;
/* 537 */       } while (newSize != (isWidthGreater ? width : height));
/*     */     } finally {
/* 539 */       g2.dispose();
/*     */     }
/*     */     
/* 542 */     if ((width != thumb.getWidth()) || (height != thumb.getHeight())) {
/* 543 */       temp = createCompatibleImage(image, width, height);
/* 544 */       g2 = temp.createGraphics();
/*     */       try
/*     */       {
/* 547 */         g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
/*     */         
/* 549 */         g2.drawImage(thumb, 0, 0, width, height, 0, 0, width, height, null);
/*     */       } finally {
/* 551 */         g2.dispose();
/*     */       }
/*     */       
/* 554 */       thumb = temp;
/*     */     }
/*     */     
/* 557 */     return thumb;
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
/*     */   public static BufferedImage createThumbnail(BufferedImage image, int newWidth, int newHeight)
/*     */   {
/* 582 */     int width = image.getWidth();
/* 583 */     int height = image.getHeight();
/*     */     
/* 585 */     boolean isTranslucent = image.getTransparency() != 1;
/*     */     
/* 587 */     if ((newWidth >= width) || (newHeight >= height)) {
/* 588 */       throw new IllegalArgumentException("newWidth and newHeight cannot be greater than the image dimensions");
/*     */     }
/*     */     
/* 591 */     if ((newWidth <= 0) || (newHeight <= 0)) {
/* 592 */       throw new IllegalArgumentException("newWidth and newHeight must be greater than 0");
/*     */     }
/*     */     
/*     */ 
/* 596 */     BufferedImage thumb = image;
/* 597 */     BufferedImage temp = null;
/*     */     
/* 599 */     Graphics2D g2 = null;
/*     */     try
/*     */     {
/* 602 */       int previousWidth = width;
/* 603 */       int previousHeight = height;
/*     */       do
/*     */       {
/* 606 */         if (width > newWidth) {
/* 607 */           width /= 2;
/* 608 */           if (width < newWidth) {
/* 609 */             width = newWidth;
/*     */           }
/*     */         }
/*     */         
/* 613 */         if (height > newHeight) {
/* 614 */           height /= 2;
/* 615 */           if (height < newHeight) {
/* 616 */             height = newHeight;
/*     */           }
/*     */         }
/*     */         
/* 620 */         if ((temp == null) || (isTranslucent)) {
/* 621 */           if (g2 != null)
/*     */           {
/*     */ 
/*     */ 
/* 625 */             g2.dispose();
/*     */           }
/* 627 */           temp = createCompatibleImage(image, width, height);
/* 628 */           g2 = temp.createGraphics();
/* 629 */           g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
/*     */         }
/*     */         
/* 632 */         g2.drawImage(thumb, 0, 0, width, height, 0, 0, previousWidth, previousHeight, null);
/*     */         
/*     */ 
/* 635 */         previousWidth = width;
/* 636 */         previousHeight = height;
/*     */         
/* 638 */         thumb = temp;
/* 639 */       } while ((width != newWidth) || (height != newHeight));
/*     */     } finally {
/* 641 */       g2.dispose();
/*     */     }
/*     */     
/* 644 */     if ((width != thumb.getWidth()) || (height != thumb.getHeight())) {
/* 645 */       temp = createCompatibleImage(image, width, height);
/* 646 */       g2 = temp.createGraphics();
/*     */       try
/*     */       {
/* 649 */         g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
/*     */         
/* 651 */         g2.drawImage(thumb, 0, 0, width, height, 0, 0, width, height, null);
/*     */       } finally {
/* 653 */         g2.dispose();
/*     */       }
/*     */       
/* 656 */       thumb = temp;
/*     */     }
/*     */     
/* 659 */     return thumb;
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
/*     */   public static int[] getPixels(BufferedImage img, int x, int y, int w, int h, int[] pixels)
/*     */   {
/* 682 */     if ((w == 0) || (h == 0)) {
/* 683 */       return new int[0];
/*     */     }
/*     */     
/* 686 */     if (pixels == null) {
/* 687 */       pixels = new int[w * h];
/* 688 */     } else if (pixels.length < w * h) {
/* 689 */       throw new IllegalArgumentException("pixels array must have a length >= w*h");
/*     */     }
/*     */     
/*     */ 
/* 693 */     int imageType = img.getType();
/* 694 */     if ((imageType == 2) || (imageType == 1))
/*     */     {
/* 696 */       Raster raster = img.getRaster();
/* 697 */       return (int[])raster.getDataElements(x, y, w, h, pixels);
/*     */     }
/*     */     
/*     */ 
/* 701 */     return img.getRGB(x, y, w, h, pixels, 0, w);
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
/*     */   public static void setPixels(BufferedImage img, int x, int y, int w, int h, int[] pixels)
/*     */   {
/* 721 */     if ((pixels == null) || (w == 0) || (h == 0))
/* 722 */       return;
/* 723 */     if (pixels.length < w * h) {
/* 724 */       throw new IllegalArgumentException("pixels array must have a length >= w*h");
/*     */     }
/*     */     
/*     */ 
/* 728 */     int imageType = img.getType();
/* 729 */     if ((imageType == 2) || (imageType == 1))
/*     */     {
/* 731 */       WritableRaster raster = img.getRaster();
/* 732 */       raster.setDataElements(x, y, w, h, pixels);
/*     */     }
/*     */     else {
/* 735 */       img.setRGB(x, y, w, h, pixels, 0, w);
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
/*     */   public static Shape mergeClip(Graphics g, Shape clip)
/*     */   {
/* 754 */     Shape oldClip = g.getClip();
/* 755 */     if (oldClip == null) {
/* 756 */       g.setClip(clip);
/* 757 */       return null;
/*     */     }
/* 759 */     Area area = new Area(oldClip);
/* 760 */     area.intersect(new Area(clip));
/* 761 */     g.setClip(area);
/* 762 */     return oldClip;
/*     */   }
/*     */ }


/* Location:              E:\java\java学习\hutubill\lib\all.jar!\org\jdesktop\swingx\graphics\GraphicsUtilities.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */
/*     */ package org.jdesktop.swingx;
/*     */ 
/*     */ import java.awt.Cursor;
/*     */ import java.awt.Dimension;
/*     */ import java.awt.Graphics;
/*     */ import java.awt.Graphics2D;
/*     */ import java.awt.Image;
/*     */ import java.awt.Insets;
/*     */ import java.awt.Rectangle;
/*     */ import java.awt.event.MouseAdapter;
/*     */ import java.awt.event.MouseEvent;
/*     */ import java.io.File;
/*     */ import java.lang.ref.SoftReference;
/*     */ import java.net.URI;
/*     */ import java.net.URL;
/*     */ import java.util.concurrent.Callable;
/*     */ import java.util.concurrent.ExecutionException;
/*     */ import java.util.concurrent.ExecutorService;
/*     */ import java.util.concurrent.Executors;
/*     */ import java.util.concurrent.FutureTask;
/*     */ import java.util.logging.Level;
/*     */ import java.util.logging.Logger;
/*     */ import javax.imageio.ImageIO;
/*     */ import javax.swing.ImageIcon;
/*     */ import javax.swing.JFileChooser;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ @Deprecated
/*     */ public class JXImagePanel
/*     */   extends JXPanel
/*     */ {
/*     */   public JXImagePanel() {}
/*     */   
/*     */   public static enum Style
/*     */   {
/*  98 */     CENTERED,  TILED,  SCALED,  SCALED_KEEP_ASPECT_RATIO;
/*     */     
/*     */     private Style() {} }
/* 101 */   private static final Logger LOG = Logger.getLogger(JXImagePanel.class.getName());
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private static final String TEXT = "<html><i><b>Click here<br>to set the image</b></i></html>";
/*     */   
/*     */ 
/*     */ 
/*     */ 
/* 112 */   private SoftReference<Image> img = new SoftReference(null);
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 119 */   private boolean editable = false;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/* 124 */   private MouseHandler mhandler = new MouseHandler(null);
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 130 */   private Style style = Style.CENTERED;
/*     */   
/*     */   private Image defaultImage;
/*     */   
/*     */   private Callable<Image> imageLoader;
/*     */   
/* 136 */   private static final ExecutorService service = Executors.newFixedThreadPool(5);
/*     */   
/*     */ 
/*     */ 
/*     */   public JXImagePanel(URL imageUrl)
/*     */   {
/*     */     try
/*     */     {
/* 144 */       setImage(ImageIO.read(imageUrl));
/*     */     }
/*     */     catch (Exception e) {
/* 147 */       LOG.log(Level.WARNING, "", e);
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
/*     */   public void setImage(Image image)
/*     */   {
/* 161 */     if (image != this.img.get()) {
/* 162 */       Image oldImage = (Image)this.img.get();
/* 163 */       this.img = new SoftReference(image);
/* 164 */       firePropertyChange("image", oldImage, this.img);
/* 165 */       invalidate();
/* 166 */       repaint();
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public Image getImage()
/*     */   {
/* 174 */     Image image = (Image)this.img.get();
/*     */     
/*     */ 
/* 177 */     if ((image == null) && (this.imageLoader != null)) {
/*     */       try {
/* 179 */         image = (Image)this.imageLoader.call();
/* 180 */         this.img = new SoftReference(image);
/*     */       } catch (Exception e) {
/* 182 */         LOG.log(Level.WARNING, "", e);
/*     */       }
/*     */     }
/* 185 */     return image;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public void setEditable(boolean editable)
/*     */   {
/* 192 */     if (editable != this.editable)
/*     */     {
/* 194 */       if (this.editable) {
/* 195 */         removeMouseListener(this.mhandler);
/*     */       }
/* 197 */       this.editable = editable;
/*     */       
/* 199 */       if (this.editable) {
/* 200 */         addMouseListener(this.mhandler);
/*     */       }
/* 202 */       setToolTipText(editable ? "<html><i><b>Click here<br>to set the image</b></i></html>" : "");
/* 203 */       firePropertyChange("editable", !editable, editable);
/* 204 */       repaint();
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean isEditable()
/*     */   {
/* 214 */     return this.editable;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setStyle(Style s)
/*     */   {
/* 223 */     if (this.style != s) {
/* 224 */       Style oldStyle = this.style;
/* 225 */       this.style = s;
/* 226 */       firePropertyChange("style", oldStyle, s);
/* 227 */       repaint();
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public Style getStyle()
/*     */   {
/* 235 */     return this.style;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public Dimension getPreferredSize()
/*     */   {
/* 244 */     if ((!isPreferredSizeSet()) && (this.img != null)) {
/* 245 */       Image img = (Image)this.img.get();
/*     */       
/* 247 */       if (img != null)
/*     */       {
/*     */ 
/* 250 */         int width = img.getWidth(null);
/* 251 */         int height = img.getHeight(null);
/* 252 */         if ((width == -1) || (height == -1)) {
/* 253 */           return super.getPreferredSize();
/*     */         }
/* 255 */         Insets insets = getInsets();
/* 256 */         width += insets.left + insets.right;
/* 257 */         height += insets.top + insets.bottom;
/* 258 */         return new Dimension(width, height);
/*     */       }
/*     */     }
/* 261 */     return super.getPreferredSize();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected void paintComponent(Graphics g)
/*     */   {
/* 271 */     super.paintComponent(g);
/* 272 */     Graphics2D g2 = (Graphics2D)g;
/* 273 */     Image img = (Image)this.img.get();
/* 274 */     if ((img == null) && (this.imageLoader != null))
/*     */     {
/*     */ 
/*     */ 
/*     */ 
/* 279 */       service.execute(new FutureTask(this.imageLoader)
/*     */       {
/*     */         protected void done()
/*     */         {
/* 283 */           super.done();
/*     */           
/* 285 */           SwingUtilities.invokeLater(new Runnable() {
/*     */             public void run() {
/*     */               try {
/* 288 */                 JXImagePanel.this.setImage((Image)JXImagePanel.1.this.get());
/*     */               }
/*     */               catch (InterruptedException e) {}catch (ExecutionException e)
/*     */               {
/* 292 */                 JXImagePanel.LOG.log(Level.WARNING, "", e);
/*     */               }
/*     */               
/*     */             }
/*     */           });
/*     */         }
/* 298 */       });
/* 299 */       img = this.defaultImage;
/*     */     }
/* 301 */     if (img != null) {
/* 302 */       int imgWidth = img.getWidth(null);
/* 303 */       int imgHeight = img.getHeight(null);
/* 304 */       if ((imgWidth == -1) || (imgHeight == -1))
/*     */       {
/* 306 */         return;
/*     */       }
/*     */       
/* 309 */       Insets insets = getInsets();
/* 310 */       int pw = getWidth() - insets.left - insets.right;
/* 311 */       int ph = getHeight() - insets.top - insets.bottom;
/*     */       
/* 313 */       switch (this.style) {
/*     */       case CENTERED: 
/* 315 */         Rectangle clipRect = g2.getClipBounds();
/* 316 */         int imageX = (pw - imgWidth) / 2 + insets.left;
/* 317 */         int imageY = (ph - imgHeight) / 2 + insets.top;
/* 318 */         Rectangle r = SwingUtilities.computeIntersection(imageX, imageY, imgWidth, imgHeight, clipRect);
/* 319 */         if ((r.x == 0) && (r.y == 0) && ((r.width == 0) || (r.height == 0))) {
/* 320 */           return;
/*     */         }
/*     */         
/*     */ 
/* 324 */         clipRect = r;
/*     */         
/*     */ 
/* 327 */         int txClipX = clipRect.x - imageX;
/* 328 */         int txClipY = clipRect.y - imageY;
/* 329 */         int txClipW = clipRect.width;
/* 330 */         int txClipH = clipRect.height;
/*     */         
/* 332 */         g2.drawImage(img, clipRect.x, clipRect.y, clipRect.x + clipRect.width, clipRect.y + clipRect.height, txClipX, txClipY, txClipX + txClipW, txClipY + txClipH, null);
/* 333 */         break;
/*     */       case TILED: 
/* 335 */         g2.translate(insets.left, insets.top);
/* 336 */         Rectangle clip = g2.getClipBounds();
/* 337 */         g2.setClip(0, 0, pw, ph);
/*     */         
/* 339 */         int totalH = 0;
/*     */         
/* 341 */         while (totalH < ph) {
/* 342 */           int totalW = 0;
/*     */           
/* 344 */           while (totalW < pw) {
/* 345 */             g2.drawImage(img, totalW, totalH, null);
/* 346 */             totalW += img.getWidth(null);
/*     */           }
/*     */           
/* 349 */           totalH += img.getHeight(null);
/*     */         }
/*     */         
/* 352 */         g2.setClip(clip);
/* 353 */         g2.translate(-insets.left, -insets.top);
/* 354 */         break;
/*     */       case SCALED: 
/* 356 */         g2.drawImage(img, insets.left, insets.top, pw, ph, null);
/* 357 */         break;
/*     */       case SCALED_KEEP_ASPECT_RATIO: 
/* 359 */         int w = pw;
/* 360 */         int h = ph;
/* 361 */         float ratioW = w / imgWidth;
/* 362 */         float ratioH = h / imgHeight;
/*     */         
/* 364 */         if (ratioW < ratioH) {
/* 365 */           h = (int)(imgHeight * ratioW);
/*     */         } else {
/* 367 */           w = (int)(imgWidth * ratioH);
/*     */         }
/*     */         
/* 370 */         int x = (pw - w) / 2 + insets.left;
/* 371 */         int y = (ph - h) / 2 + insets.top;
/* 372 */         g2.drawImage(img, x, y, w, h, null);
/* 373 */         break;
/*     */       default: 
/* 375 */         LOG.fine("unimplemented");
/* 376 */         g2.drawImage(img, insets.left, insets.top, this);
/*     */       }
/*     */       
/*     */     }
/*     */   }
/*     */   
/*     */   private class MouseHandler
/*     */     extends MouseAdapter
/*     */   {
/*     */     private Cursor oldCursor;
/*     */     private JFileChooser chooser;
/*     */     
/*     */     private MouseHandler() {}
/*     */     
/*     */     public void mouseClicked(MouseEvent evt)
/*     */     {
/* 392 */       if (this.chooser == null) {
/* 393 */         this.chooser = new JFileChooser();
/*     */       }
/* 395 */       int retVal = this.chooser.showOpenDialog(JXImagePanel.this);
/* 396 */       if (retVal == 0) {
/* 397 */         File file = this.chooser.getSelectedFile();
/*     */         try {
/* 399 */           JXImagePanel.this.setImage(new ImageIcon(file.toURI().toURL()).getImage());
/*     */         }
/*     */         catch (Exception ex) {}
/*     */       }
/*     */     }
/*     */     
/*     */     public void mouseEntered(MouseEvent evt)
/*     */     {
/* 407 */       if (this.oldCursor == null) {
/* 408 */         this.oldCursor = JXImagePanel.this.getCursor();
/* 409 */         JXImagePanel.this.setCursor(Cursor.getPredefinedCursor(12));
/*     */       }
/*     */     }
/*     */     
/*     */     public void mouseExited(MouseEvent evt)
/*     */     {
/* 415 */       if (this.oldCursor != null) {
/* 416 */         JXImagePanel.this.setCursor(this.oldCursor);
/* 417 */         this.oldCursor = null;
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   public void setDefaultImage(Image def) {
/* 423 */     this.defaultImage = def;
/*     */   }
/*     */   
/*     */   public void setImageLoader(Callable<Image> loadImage) {
/* 427 */     this.imageLoader = loadImage;
/*     */   }
/*     */ }


/* Location:              E:\java\java学习\hutubill\lib\all.jar!\org\jdesktop\swingx\JXImagePanel.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */
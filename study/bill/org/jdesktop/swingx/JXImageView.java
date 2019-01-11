/*     */ package org.jdesktop.swingx;
/*     */ 
/*     */ import java.awt.Color;
/*     */ import java.awt.Component;
/*     */ import java.awt.Cursor;
/*     */ import java.awt.Dialog;
/*     */ import java.awt.FileDialog;
/*     */ import java.awt.Frame;
/*     */ import java.awt.Graphics;
/*     */ import java.awt.Graphics2D;
/*     */ import java.awt.Image;
/*     */ import java.awt.Paint;
/*     */ import java.awt.Point;
/*     */ import java.awt.RenderingHints;
/*     */ import java.awt.Window;
/*     */ import java.awt.datatransfer.DataFlavor;
/*     */ import java.awt.datatransfer.Transferable;
/*     */ import java.awt.datatransfer.UnsupportedFlavorException;
/*     */ import java.awt.event.ActionEvent;
/*     */ import java.awt.event.InputEvent;
/*     */ import java.awt.event.MouseEvent;
/*     */ import java.awt.geom.AffineTransform;
/*     */ import java.awt.geom.Point2D;
/*     */ import java.awt.geom.Point2D.Double;
/*     */ import java.awt.image.AffineTransformOp;
/*     */ import java.awt.image.BufferedImage;
/*     */ import java.awt.image.BufferedImageOp;
/*     */ import java.io.File;
/*     */ import java.io.IOException;
/*     */ import java.net.URI;
/*     */ import java.net.URL;
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ import java.util.logging.Logger;
/*     */ import javax.imageio.ImageIO;
/*     */ import javax.swing.AbstractAction;
/*     */ import javax.swing.Action;
/*     */ import javax.swing.JComponent;
/*     */ import javax.swing.SwingUtilities;
/*     */ import javax.swing.TransferHandler;
/*     */ import javax.swing.event.MouseInputAdapter;
/*     */ import org.jdesktop.swingx.color.ColorUtil;
/*     */ import org.jdesktop.swingx.error.ErrorListener;
/*     */ import org.jdesktop.swingx.error.ErrorSupport;
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
/*     */ public class JXImageView
/*     */   extends JXPanel
/*     */ {
/*  89 */   private Logger log = Logger.getLogger(JXImageView.class.getName());
/*     */   
/*     */ 
/*     */   private Image image;
/*     */   
/*     */ 
/*     */   private URL imageURL;
/*     */   
/*  97 */   private ErrorSupport errorSupport = new ErrorSupport(this);
/*     */   
/*     */ 
/*     */   private Point2D imageLocation;
/*     */   
/*     */   private Paint checkerPaint;
/*     */   
/* 104 */   private double scale = 1.0D;
/*     */   
/* 106 */   private boolean editable = true;
/*     */   
/* 108 */   private MoveHandler moveHandler = new MoveHandler(this);
/*     */   
/* 110 */   private boolean dragEnabled = false;
/*     */   
/* 112 */   private String exportName = "UntitledImage";
/*     */   
/* 114 */   private String exportFormat = "png";
/*     */   
/*     */   public JXImageView()
/*     */   {
/* 118 */     this.checkerPaint = ColorUtil.getCheckerPaint(Color.white, new Color(250, 250, 250), 50);
/* 119 */     setEditable(true);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public Point2D getImageLocation()
/*     */   {
/* 131 */     return this.imageLocation;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setImageLocation(Point2D imageLocation)
/*     */   {
/* 139 */     Point2D old = getImageLocation();
/* 140 */     this.imageLocation = imageLocation;
/* 141 */     firePropertyChange("imageLocation", old, getImageLocation());
/* 142 */     repaint();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public Image getImage()
/*     */   {
/* 150 */     return this.image;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setImage(Image image)
/*     */   {
/* 158 */     Image oldImage = getImage();
/* 159 */     this.image = image;
/* 160 */     setImageLocation(null);
/* 161 */     setScale(1.0D);
/* 162 */     firePropertyChange("image", oldImage, image);
/* 163 */     repaint();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setImage(URL url)
/*     */     throws IOException
/*     */   {
/* 172 */     setImageURL(url);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setImage(File file)
/*     */     throws IOException
/*     */   {
/* 182 */     setImageURL(file.toURI().toURL());
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public double getScale()
/*     */   {
/* 193 */     return this.scale;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setScale(double scale)
/*     */   {
/* 204 */     double oldScale = this.scale;
/* 205 */     this.scale = scale;
/* 206 */     firePropertyChange("scale", oldScale, scale);
/* 207 */     repaint();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean isEditable()
/*     */   {
/* 215 */     return this.editable;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setEditable(boolean editable)
/*     */   {
/* 226 */     boolean old = isEditable();
/* 227 */     this.editable = editable;
/* 228 */     if (editable) {
/* 229 */       addMouseMotionListener(this.moveHandler);
/* 230 */       addMouseListener(this.moveHandler);
/* 231 */       setCursor(Cursor.getPredefinedCursor(12));
/*     */       try {
/* 233 */         setTransferHandler(new DnDHandler());
/*     */       } catch (ClassNotFoundException ex) {
/* 235 */         ex.printStackTrace();
/* 236 */         fireError(ex);
/*     */       }
/*     */     } else {
/* 239 */       removeMouseMotionListener(this.moveHandler);
/* 240 */       removeMouseListener(this.moveHandler);
/* 241 */       setCursor(Cursor.getDefaultCursor());
/* 242 */       setTransferHandler(null);
/*     */     }
/* 244 */     firePropertyChange("editable", old, isEditable());
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setDragEnabled(boolean dragEnabled)
/*     */   {
/* 256 */     boolean old = isDragEnabled();
/* 257 */     this.dragEnabled = dragEnabled;
/* 258 */     firePropertyChange("dragEnabled", old, isDragEnabled());
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean isDragEnabled()
/*     */   {
/* 266 */     return this.dragEnabled;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void addErrorListener(ErrorListener el)
/*     */   {
/* 275 */     this.errorSupport.addErrorListener(el);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void removeErrorListener(ErrorListener el)
/*     */   {
/* 283 */     this.errorSupport.removeErrorListener(el);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   protected void fireError(Throwable throwable)
/*     */   {
/* 291 */     this.errorSupport.fireErrorEvent(throwable);
/*     */   }
/*     */   
/*     */   private static FileDialog getSafeFileDialog(Component comp)
/*     */   {
/* 296 */     Window win = SwingUtilities.windowForComponent(comp);
/* 297 */     if ((win instanceof Dialog)) {
/* 298 */       return new FileDialog((Dialog)win);
/*     */     }
/* 300 */     if ((win instanceof Frame)) {
/* 301 */       return new FileDialog((Frame)win);
/*     */     }
/* 303 */     return null;
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
/*     */   public Action getOpenAction()
/*     */   {
/* 318 */     Action action = new AbstractAction() {
/*     */       public void actionPerformed(ActionEvent actionEvent) {
/* 320 */         FileDialog fd = JXImageView.getSafeFileDialog(JXImageView.this);
/* 321 */         fd.setMode(0);
/* 322 */         fd.setVisible(true);
/* 323 */         if (fd.getFile() != null) {
/*     */           try {
/* 325 */             JXImageView.this.setImage(new File(fd.getDirectory(), fd.getFile()));
/*     */           } catch (IOException ex) {
/* 327 */             JXImageView.this.fireError(ex);
/*     */ 
/*     */ 
/*     */ 
/*     */           }
/*     */           
/*     */ 
/*     */ 
/*     */ 
/*     */         }
/*     */         
/*     */ 
/*     */ 
/*     */       }
/*     */       
/*     */ 
/*     */ 
/*     */ 
/* 345 */     };
/* 346 */     action.putValue("Name", "Open");
/* 347 */     return action;
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
/*     */   @Deprecated
/*     */   public Action getSaveAction()
/*     */   {
/* 361 */     Action action = new AbstractAction() {
/*     */       public void actionPerformed(ActionEvent evt) {
/* 363 */         Image img = JXImageView.this.getImage();
/* 364 */         BufferedImage dst = new BufferedImage(img.getWidth(null), img.getHeight(null), 2);
/*     */         
/*     */ 
/*     */ 
/* 368 */         Graphics2D g = (Graphics2D)dst.getGraphics();
/*     */         
/*     */         try
/*     */         {
/* 372 */           g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);
/*     */           
/* 374 */           g.drawImage(img, 0, 0, null);
/*     */         } finally {
/* 376 */           g.dispose();
/*     */         }
/* 378 */         FileDialog fd = new FileDialog((Frame)SwingUtilities.windowForComponent(JXImageView.this));
/* 379 */         fd.setMode(1);
/* 380 */         fd.setVisible(true);
/* 381 */         if (fd.getFile() != null) {
/*     */           try {
/* 383 */             ImageIO.write(dst, "png", new File(fd.getDirectory(), fd.getFile()));
/*     */           } catch (IOException ex) {
/* 385 */             JXImageView.this.fireError(ex);
/*     */ 
/*     */ 
/*     */ 
/*     */           }
/*     */           
/*     */ 
/*     */ 
/*     */ 
/*     */         }
/*     */         
/*     */ 
/*     */ 
/*     */ 
/*     */       }
/*     */       
/*     */ 
/*     */ 
/*     */ 
/* 404 */     };
/* 405 */     action.putValue("Name", "Save");
/* 406 */     return action;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   @Deprecated
/*     */   public Action getRotateClockwiseAction()
/*     */   {
/* 416 */     Action action = new AbstractAction() {
/*     */       public void actionPerformed(ActionEvent evt) {
/* 418 */         Image img = JXImageView.this.getImage();
/* 419 */         BufferedImage src = new BufferedImage(img.getWidth(null), img.getHeight(null), 2);
/*     */         
/*     */ 
/*     */ 
/* 423 */         BufferedImage dst = new BufferedImage(img.getHeight(null), img.getWidth(null), 2);
/*     */         
/*     */ 
/*     */ 
/* 427 */         Graphics2D g = (Graphics2D)src.getGraphics();
/*     */         
/*     */         try
/*     */         {
/* 431 */           g.drawImage(img, 0, 0, null);
/*     */         } finally {
/* 433 */           g.dispose();
/*     */         }
/*     */         
/* 436 */         AffineTransform trans = AffineTransform.getRotateInstance(1.5707963267948966D, 0.0D, 0.0D);
/* 437 */         trans.translate(0.0D, -src.getHeight());
/* 438 */         BufferedImageOp op = new AffineTransformOp(trans, 1);
/* 439 */         op.filter(src, dst);
/* 440 */         JXImageView.this.setImage(dst);
/*     */       }
/* 442 */     };
/* 443 */     action.putValue("Name", "Rotate Clockwise");
/* 444 */     return action;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   @Deprecated
/*     */   public Action getRotateCounterClockwiseAction()
/*     */   {
/* 454 */     Action action = new AbstractAction() {
/*     */       public void actionPerformed(ActionEvent evt) {
/* 456 */         Image img = JXImageView.this.getImage();
/* 457 */         BufferedImage src = new BufferedImage(img.getWidth(null), img.getHeight(null), 2);
/*     */         
/*     */ 
/*     */ 
/* 461 */         BufferedImage dst = new BufferedImage(img.getHeight(null), img.getWidth(null), 2);
/*     */         
/*     */ 
/*     */ 
/* 465 */         Graphics2D g = (Graphics2D)src.getGraphics();
/*     */         
/*     */         try
/*     */         {
/* 469 */           g.drawImage(img, 0, 0, null);
/*     */         } finally {
/* 471 */           g.dispose();
/*     */         }
/* 473 */         AffineTransform trans = AffineTransform.getRotateInstance(-1.5707963267948966D, 0.0D, 0.0D);
/* 474 */         trans.translate(-src.getWidth(), 0.0D);
/* 475 */         BufferedImageOp op = new AffineTransformOp(trans, 1);
/* 476 */         op.filter(src, dst);
/* 477 */         JXImageView.this.setImage(dst);
/*     */       }
/* 479 */     };
/* 480 */     action.putValue("Name", "Rotate CounterClockwise");
/* 481 */     return action;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   @Deprecated
/*     */   public Action getZoomOutAction()
/*     */   {
/* 491 */     Action action = new AbstractAction() {
/*     */       public void actionPerformed(ActionEvent actionEvent) {
/* 493 */         JXImageView.this.setScale(JXImageView.this.getScale() * 0.5D);
/*     */       }
/* 495 */     };
/* 496 */     action.putValue("Name", "Zoom Out");
/* 497 */     return action;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   @Deprecated
/*     */   public Action getZoomInAction()
/*     */   {
/* 507 */     Action action = new AbstractAction() {
/*     */       public void actionPerformed(ActionEvent actionEvent) {
/* 509 */         JXImageView.this.setScale(JXImageView.this.getScale() * 2.0D);
/*     */       }
/* 511 */     };
/* 512 */     action.putValue("Name", "Zoom In");
/* 513 */     return action;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected void paintComponent(Graphics g)
/*     */   {
/* 523 */     ((Graphics2D)g).setPaint(this.checkerPaint);
/*     */     
/* 525 */     g.fillRect(0, 0, getWidth(), getHeight());
/* 526 */     if (getImage() != null) {
/* 527 */       Point2D center = new Point2D.Double(getWidth() / 2, getHeight() / 2);
/* 528 */       if (getImageLocation() != null) {
/* 529 */         center = getImageLocation();
/*     */       }
/* 531 */       Point2D loc = new Point2D.Double();
/* 532 */       double width = getImage().getWidth(null) * getScale();
/* 533 */       double height = getImage().getHeight(null) * getScale();
/* 534 */       loc.setLocation(center.getX() - width / 2.0D, center.getY() - height / 2.0D);
/* 535 */       g.drawImage(getImage(), (int)loc.getX(), (int)loc.getY(), (int)width, (int)height, null);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   private class MoveHandler
/*     */     extends MouseInputAdapter
/*     */   {
/*     */     private JXImageView panel;
/*     */     
/* 546 */     private Point prev = null;
/* 547 */     private Point start = null;
/*     */     
/* 549 */     public MoveHandler(JXImageView panel) { this.panel = panel; }
/*     */     
/*     */ 
/*     */     public void mousePressed(MouseEvent evt)
/*     */     {
/* 554 */       this.prev = evt.getPoint();
/* 555 */       this.start = this.prev;
/*     */     }
/*     */     
/*     */     public void mouseDragged(MouseEvent evt)
/*     */     {
/* 560 */       Point curr = evt.getPoint();
/*     */       
/* 562 */       if (JXImageView.this.isDragEnabled())
/*     */       {
/*     */ 
/* 565 */         if (curr.distance(this.start) > 5.0D) {
/* 566 */           JXImageView.this.log.fine("starting the drag: ");
/* 567 */           this.panel.getTransferHandler().exportAsDrag((JComponent)evt.getSource(), evt, 1);
/* 568 */           return;
/*     */         }
/*     */       }
/*     */       
/* 572 */       int offx = curr.x - this.prev.x;
/* 573 */       int offy = curr.y - this.prev.y;
/* 574 */       Point2D offset = JXImageView.this.getImageLocation();
/* 575 */       if (offset == null) {
/* 576 */         if (JXImageView.this.image != null) {
/* 577 */           offset = new Point2D.Double(JXImageView.this.getWidth() / 2, JXImageView.this.getHeight() / 2);
/*     */         } else {
/* 579 */           offset = new Point2D.Double(0.0D, 0.0D);
/*     */         }
/*     */       }
/* 582 */       offset = new Point2D.Double(offset.getX() + offx, offset.getY() + offy);
/* 583 */       JXImageView.this.setImageLocation(offset);
/* 584 */       this.prev = curr;
/* 585 */       JXImageView.this.repaint();
/*     */     }
/*     */     
/*     */     public void mouseReleased(MouseEvent evt)
/*     */     {
/* 590 */       this.prev = null;
/*     */     }
/*     */   }
/*     */   
/*     */   private class DnDHandler extends TransferHandler {
/*     */     DataFlavor urlFlavor;
/*     */     
/*     */     public DnDHandler() throws ClassNotFoundException {
/* 598 */       this.urlFlavor = new DataFlavor("application/x-java-url;class=java.net.URL");
/*     */     }
/*     */     
/*     */ 
/*     */     public void exportAsDrag(JComponent c, InputEvent evt, int action)
/*     */     {
/* 604 */       super.exportAsDrag(c, evt, action);
/*     */     }
/*     */     
/*     */     public int getSourceActions(JComponent c)
/*     */     {
/* 609 */       return 1;
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */     protected void exportDone(JComponent source, Transferable data, int action) {}
/*     */     
/*     */ 
/*     */     public boolean canImport(JComponent c, DataFlavor[] flavors)
/*     */     {
/* 619 */       for (int i = 0; i < flavors.length; i++)
/*     */       {
/* 621 */         if (DataFlavor.javaFileListFlavor.equals(flavors[i])) {
/* 622 */           return true;
/*     */         }
/* 624 */         if (DataFlavor.imageFlavor.equals(flavors[i])) {
/* 625 */           return true;
/*     */         }
/* 627 */         if (this.urlFlavor.match(flavors[i])) {
/* 628 */           return true;
/*     */         }
/*     */       }
/*     */       
/* 632 */       return false;
/*     */     }
/*     */     
/*     */     protected Transferable createTransferable(JComponent c)
/*     */     {
/* 637 */       JXImageView view = (JXImageView)c;
/* 638 */       return new JXImageView.ImageTransferable(JXImageView.this, view.getImage(), view.getExportName(), view.getExportFormat());
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */     public boolean importData(JComponent comp, Transferable t)
/*     */     {
/* 645 */       if (canImport(comp, t.getTransferDataFlavors())) {
/*     */         try {
/* 647 */           if (t.isDataFlavorSupported(DataFlavor.javaFileListFlavor)) {
/* 648 */             List<File> files = (List)t.getTransferData(DataFlavor.javaFileListFlavor);
/*     */             
/* 650 */             if (files.size() > 0) {
/* 651 */               File file = (File)files.get(0);
/*     */               
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 657 */               JXImageView.this.setImageString(file.toURI().toURL().toString());
/*     */               
/*     */ 
/* 660 */               return true;
/*     */             }
/*     */           }
/*     */           
/* 664 */           Object obj = t.getTransferData(this.urlFlavor);
/*     */           
/*     */ 
/* 667 */           if ((obj instanceof URL)) {
/* 668 */             JXImageView.this.setImageString(((URL)obj).toString());
/*     */           }
/* 670 */           return true;
/*     */         } catch (Exception ex) {
/* 672 */           JXImageView.this.log.severe(ex.getMessage());
/* 673 */           ex.printStackTrace();
/* 674 */           JXImageView.this.fireError(ex);
/*     */         }
/*     */       }
/* 677 */       return false;
/*     */     }
/*     */   }
/*     */   
/*     */   private class ImageTransferable implements Transferable {
/*     */     private Image img;
/*     */     private List<File> files;
/*     */     private String exportName;
/*     */     private String exportFormat;
/*     */     
/*     */     public ImageTransferable(Image img, String exportName, String exportFormat) {
/* 688 */       this.img = img;
/* 689 */       this.exportName = exportName;
/* 690 */       this.exportFormat = exportFormat;
/*     */     }
/*     */     
/*     */     public DataFlavor[] getTransferDataFlavors() {
/* 694 */       return new DataFlavor[] { DataFlavor.imageFlavor, DataFlavor.javaFileListFlavor };
/*     */     }
/*     */     
/*     */     public boolean isDataFlavorSupported(DataFlavor flavor)
/*     */     {
/* 699 */       if (flavor == DataFlavor.imageFlavor) {
/* 700 */         return true;
/*     */       }
/* 702 */       return flavor == DataFlavor.javaFileListFlavor;
/*     */     }
/*     */     
/*     */     public Object getTransferData(DataFlavor flavor) throws UnsupportedFlavorException, IOException
/*     */     {
/* 707 */       if (flavor == DataFlavor.imageFlavor) {
/* 708 */         return this.img;
/*     */       }
/* 710 */       if (flavor == DataFlavor.javaFileListFlavor) {
/* 711 */         if (this.files == null) {
/* 712 */           this.files = new ArrayList();
/* 713 */           File file = File.createTempFile(this.exportName, "." + this.exportFormat);
/*     */           
/* 715 */           ImageIO.write(GraphicsUtilities.convertToBufferedImage(this.img), this.exportFormat, file);
/* 716 */           this.files.add(file);
/*     */         }
/*     */         
/* 719 */         return this.files;
/*     */       }
/* 721 */       return null;
/*     */     }
/*     */   }
/*     */   
/*     */   public String getExportName() {
/* 726 */     return this.exportName;
/*     */   }
/*     */   
/*     */   public void setExportName(String exportName) {
/* 730 */     String old = getExportName();
/* 731 */     this.exportName = exportName;
/* 732 */     firePropertyChange("exportName", old, getExportName());
/*     */   }
/*     */   
/*     */   public String getExportFormat() {
/* 736 */     return this.exportFormat;
/*     */   }
/*     */   
/*     */   public void setExportFormat(String exportFormat) {
/* 740 */     String old = getExportFormat();
/* 741 */     this.exportFormat = exportFormat;
/* 742 */     firePropertyChange("exportFormat", old, getExportFormat());
/*     */   }
/*     */   
/*     */   public URL getImageURL() {
/* 746 */     return this.imageURL;
/*     */   }
/*     */   
/*     */   public void setImageURL(URL imageURL) throws IOException {
/* 750 */     URL old = getImageURL();
/* 751 */     this.imageURL = imageURL;
/* 752 */     firePropertyChange("imageURL", old, getImageURL());
/* 753 */     setImage(ImageIO.read(getImageURL()));
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public String getImageString()
/*     */   {
/* 762 */     if (getImageURL() == null) {
/* 763 */       return null;
/*     */     }
/* 765 */     return getImageURL().toString();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setImageString(String url)
/*     */     throws IOException
/*     */   {
/* 774 */     String old = getImageString();
/* 775 */     setImageURL(new URL(url));
/* 776 */     firePropertyChange("imageString", old, url);
/*     */   }
/*     */ }


/* Location:              E:\java\java学习\hutubill\lib\all.jar!\org\jdesktop\swingx\JXImageView.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */
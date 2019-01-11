/*     */ package org.jdesktop.swingx.plaf.windows;
/*     */ 
/*     */ import java.awt.Graphics2D;
/*     */ import java.awt.Insets;
/*     */ import java.awt.image.BufferedImage;
/*     */ import java.util.logging.Level;
/*     */ import java.util.logging.Logger;
/*     */ import javax.imageio.ImageIO;
/*     */ import javax.swing.JComponent;
/*     */ import javax.swing.plaf.ComponentUI;
/*     */ import org.jdesktop.swingx.JXStatusBar;
/*     */ import org.jdesktop.swingx.plaf.UIManagerExt;
/*     */ import org.jdesktop.swingx.plaf.basic.BasicStatusBarUI;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class WindowsStatusBarUI
/*     */   extends BasicStatusBarUI
/*     */ {
/*  42 */   private static final Logger log = Logger.getLogger(WindowsStatusBarUI.class.getName());
/*     */   
/*     */   private BufferedImage leftImage;
/*     */   
/*     */   private BufferedImage middleImage;
/*     */   private BufferedImage rightImage;
/*     */   
/*     */   public WindowsStatusBarUI()
/*     */   {
/*     */     try
/*     */     {
/*  53 */       this.leftImage = ImageIO.read(WindowsStatusBarUI.class.getResource(UIManagerExt.getString("StatusBar.leftImage")));
/*  54 */       this.middleImage = ImageIO.read(WindowsStatusBarUI.class.getResource(UIManagerExt.getString("StatusBar.middleImage")));
/*  55 */       this.rightImage = ImageIO.read(WindowsStatusBarUI.class.getResource(UIManagerExt.getString("StatusBar.rightImage")));
/*     */     }
/*     */     catch (Exception e) {
/*  58 */       log.log(Level.FINE, e.getLocalizedMessage(), e);
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
/*     */   public static ComponentUI createUI(JComponent c)
/*     */   {
/*  73 */     return new WindowsStatusBarUI();
/*     */   }
/*     */   
/*     */   protected void paintBackground(Graphics2D g, JXStatusBar statusBar) {
/*  77 */     if ((this.leftImage == null) || (this.middleImage == null) || (this.rightImage == null)) {
/*  78 */       log.severe("Failed to initialize necessary assets. Set logging to FINE to see more details.");
/*  79 */       return;
/*     */     }
/*     */     
/*     */ 
/*  83 */     g.drawImage(this.leftImage, 0, 0, this.leftImage.getWidth(), statusBar.getHeight(), null);
/*     */     
/*  85 */     if (statusBar.isResizeHandleEnabled()) {
/*  86 */       g.drawImage(this.middleImage, this.leftImage.getWidth(), 0, statusBar.getWidth() - this.leftImage.getWidth() - this.rightImage.getWidth(), statusBar.getHeight(), null);
/*  87 */       g.drawImage(this.rightImage, statusBar.getWidth() - this.rightImage.getWidth(), 0, this.rightImage.getWidth(), statusBar.getHeight(), null);
/*     */     } else {
/*  89 */       g.drawImage(this.middleImage, this.leftImage.getWidth(), 0, statusBar.getWidth() - this.leftImage.getWidth(), statusBar.getHeight(), null);
/*     */     }
/*     */   }
/*     */   
/*     */   protected Insets getSeparatorInsets(Insets insets) {
/*  94 */     if (insets == null) {
/*  95 */       insets = new Insets(0, 0, 0, 0);
/*     */     }
/*  97 */     insets.top = 1;
/*  98 */     insets.left = 4;
/*  99 */     insets.bottom = 0;
/* 100 */     insets.right = 4;
/* 101 */     return insets;
/*     */   }
/*     */ }


/* Location:              E:\java\java学习\hutubill\lib\all.jar!\org\jdesktop\swingx\plaf\windows\WindowsStatusBarUI.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */
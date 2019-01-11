/*     */ package org.jdesktop.swingx.action;
/*     */ 
/*     */ import java.awt.Desktop;
/*     */ import java.awt.Desktop.Action;
/*     */ import java.awt.event.ActionEvent;
/*     */ import java.io.IOException;
/*     */ import java.net.URI;
/*     */ import java.net.URISyntaxException;
/*     */ import java.net.URL;
/*     */ import java.util.logging.Level;
/*     */ import java.util.logging.Logger;
/*     */ import javax.swing.AbstractAction;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class OpenBrowserAction
/*     */   extends AbstractAction
/*     */ {
/*  42 */   private static Logger log = Logger.getLogger(OpenBrowserAction.class.getName());
/*     */   
/*     */   private URI uri;
/*     */   
/*     */   public OpenBrowserAction()
/*     */   {
/*  48 */     this((URI)null);
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
/*     */   public OpenBrowserAction(String uri)
/*     */   {
/*  62 */     this(URI.create(uri));
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public OpenBrowserAction(URL url)
/*     */     throws URISyntaxException
/*     */   {
/*  74 */     this(url.toURI());
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public OpenBrowserAction(URI uri)
/*     */   {
/*  84 */     setURI(uri);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public URI getURI()
/*     */   {
/*  93 */     return this.uri;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setURI(URI uri)
/*     */   {
/* 103 */     this.uri = uri;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public void actionPerformed(ActionEvent e)
/*     */   {
/* 110 */     if ((this.uri == null) || (!Desktop.isDesktopSupported())) {
/* 111 */       return;
/*     */     }
/*     */     
/* 114 */     if (Desktop.getDesktop().isSupported(Desktop.Action.BROWSE)) {
/*     */       try {
/* 116 */         Desktop.getDesktop().browse(this.uri);
/*     */       } catch (IOException ioe) {
/* 118 */         log.log(Level.WARNING, "unable to browse: " + this.uri, ioe);
/*     */       }
/*     */     }
/*     */   }
/*     */ }


/* Location:              E:\java\java学习\hutubill\lib\all.jar!\org\jdesktop\swingx\action\OpenBrowserAction.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */
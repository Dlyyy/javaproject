/*     */ package org.jdesktop.swingx.hyperlink;
/*     */ 
/*     */ import java.awt.Desktop;
/*     */ import java.awt.Desktop.Action;
/*     */ import java.awt.event.ActionEvent;
/*     */ import java.io.IOException;
/*     */ import java.net.URI;
/*     */ import java.util.logging.Logger;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class HyperlinkAction
/*     */   extends AbstractHyperlinkAction<URI>
/*     */ {
/*  41 */   private static final Logger LOG = Logger.getLogger(HyperlinkAction.class.getName());
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private Desktop.Action desktopAction;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private URIVisitor visitor;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static HyperlinkAction createHyperlinkAction(URI uri)
/*     */   {
/*  61 */     Desktop.Action type = isMailURI(uri) ? Desktop.Action.MAIL : Desktop.Action.BROWSE;
/*  62 */     return createHyperlinkAction(uri, type);
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
/*     */   public static HyperlinkAction createHyperlinkAction(URI uri, Desktop.Action type)
/*     */   {
/*  78 */     return new HyperlinkAction(uri, type);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   private static boolean isMailURI(URI uri)
/*     */   {
/*  86 */     return (uri != null) && ("mailto".equalsIgnoreCase(uri.getScheme()));
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
/*     */   public HyperlinkAction()
/*     */   {
/*  99 */     this(Desktop.Action.BROWSE);
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
/*     */   public HyperlinkAction(Desktop.Action desktopAction)
/*     */   {
/* 114 */     this(null, desktopAction);
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
/*     */   public HyperlinkAction(URI uri, Desktop.Action desktopAction)
/*     */   {
/* 130 */     if (!Desktop.isDesktopSupported()) {
/* 131 */       throw new UnsupportedOperationException("Desktop API is not supported on the current platform");
/*     */     }
/*     */     
/* 134 */     if ((desktopAction != Desktop.Action.BROWSE) && (desktopAction != Desktop.Action.MAIL)) {
/* 135 */       throw new IllegalArgumentException("Illegal action type: " + desktopAction + ". Must be BROWSE or MAIL");
/*     */     }
/*     */     
/* 138 */     this.desktopAction = desktopAction;
/* 139 */     getURIVisitor();
/* 140 */     setTarget(uri);
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
/*     */   public void actionPerformed(ActionEvent e)
/*     */   {
/* 154 */     if (!getURIVisitor().isEnabled((URI)getTarget())) return;
/*     */     try {
/* 156 */       getURIVisitor().visit((URI)getTarget());
/* 157 */       setVisited(true);
/*     */     } catch (IOException e1) {
/* 159 */       setVisited(false);
/* 160 */       LOG.fine("cant visit Desktop " + e);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public Desktop.Action getDesktopAction()
/*     */   {
/* 168 */     return this.desktopAction;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   protected void installTarget()
/*     */   {
/* 176 */     if (this.visitor == null) return;
/* 177 */     super.installTarget();
/* 178 */     updateEnabled();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   private void updateEnabled()
/*     */   {
/* 185 */     setEnabled(getURIVisitor().isEnabled((URI)getTarget()));
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   private URIVisitor getURIVisitor()
/*     */   {
/* 192 */     if (this.visitor == null) {
/* 193 */       this.visitor = createURIVisitor();
/*     */     }
/* 195 */     return this.visitor;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   private URIVisitor createURIVisitor()
/*     */   {
/* 202 */     return getDesktopAction() == Desktop.Action.BROWSE ? new BrowseVisitor(null) : new MailVisitor(null);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private abstract class URIVisitor
/*     */   {
/* 212 */     protected boolean desktopSupported = Desktop.isDesktopSupported();
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     private URIVisitor() {}
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     public boolean isEnabled(URI uri)
/*     */     {
/* 228 */       return (this.desktopSupported) && (isActionSupported());
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */     public abstract void visit(URI paramURI)
/*     */       throws IOException;
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */     protected abstract boolean isActionSupported();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   private class BrowseVisitor
/*     */     extends HyperlinkAction.URIVisitor
/*     */   {
/*     */     private BrowseVisitor()
/*     */     {
/* 251 */       super(null);
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */     public void visit(URI uri)
/*     */       throws IOException
/*     */     {
/* 260 */       Desktop.getDesktop().browse(uri);
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     protected boolean isActionSupported()
/*     */     {
/* 270 */       return Desktop.getDesktop().isSupported(Desktop.Action.BROWSE);
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     public boolean isEnabled(URI uri)
/*     */     {
/* 280 */       return (uri != null) && (super.isEnabled(uri));
/*     */     }
/*     */   }
/*     */   
/*     */   private class MailVisitor extends HyperlinkAction.URIVisitor {
/*     */     private MailVisitor() {
/* 286 */       super(null);
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */     public void visit(URI uri)
/*     */       throws IOException
/*     */     {
/* 295 */       if (uri == null) {
/* 296 */         Desktop.getDesktop().mail();
/*     */       } else {
/* 298 */         Desktop.getDesktop().mail(uri);
/*     */       }
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     protected boolean isActionSupported()
/*     */     {
/* 308 */       return Desktop.getDesktop().isSupported(Desktop.Action.MAIL);
/*     */     }
/*     */   }
/*     */ }


/* Location:              E:\java\java学习\hutubill\lib\all.jar!\org\jdesktop\swingx\hyperlink\HyperlinkAction.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */
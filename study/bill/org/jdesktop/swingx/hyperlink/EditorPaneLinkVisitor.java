/*     */ package org.jdesktop.swingx.hyperlink;
/*     */ 
/*     */ import java.awt.event.ActionEvent;
/*     */ import java.awt.event.ActionListener;
/*     */ import java.io.IOException;
/*     */ import java.net.URL;
/*     */ import javax.swing.SwingUtilities;
/*     */ import javax.swing.event.HyperlinkEvent;
/*     */ import javax.swing.event.HyperlinkEvent.EventType;
/*     */ import javax.swing.event.HyperlinkListener;
/*     */ import javax.swing.text.Document;
/*     */ import org.jdesktop.swingx.JXEditorPane;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class EditorPaneLinkVisitor
/*     */   implements ActionListener
/*     */ {
/*     */   private JXEditorPane editorPane;
/*     */   private HyperlinkListener hyperlinkListener;
/*     */   private LinkModel internalLink;
/*     */   
/*     */   public EditorPaneLinkVisitor()
/*     */   {
/*  50 */     this(null);
/*     */   }
/*     */   
/*     */   public EditorPaneLinkVisitor(JXEditorPane pane) {
/*  54 */     if (pane == null) {
/*  55 */       pane = createDefaultEditorPane();
/*     */     }
/*  57 */     this.editorPane = pane;
/*  58 */     pane.addHyperlinkListener(getHyperlinkListener());
/*     */   }
/*     */   
/*     */   public JXEditorPane getOutputComponent()
/*     */   {
/*  63 */     return this.editorPane;
/*     */   }
/*     */   
/*     */   public void actionPerformed(ActionEvent e) {
/*  67 */     if ((e.getSource() instanceof LinkModel)) {
/*  68 */       final LinkModel link = (LinkModel)e.getSource();
/*  69 */       SwingUtilities.invokeLater(new Runnable() {
/*     */         public void run() {
/*  71 */           EditorPaneLinkVisitor.this.visit(link);
/*     */         }
/*     */       });
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */   public void visit(LinkModel link)
/*     */   {
/*     */     try
/*     */     {
/*  82 */       this.editorPane.getDocument().putProperty("stream", null);
/*     */       
/*     */ 
/*  85 */       this.editorPane.setPage(link.getURL());
/*  86 */       link.setVisited(true);
/*     */     } catch (IOException e1) {
/*  88 */       this.editorPane.setText("<html>Error 404: couldn't show " + link.getURL() + " </html>");
/*     */     }
/*     */   }
/*     */   
/*     */   protected JXEditorPane createDefaultEditorPane() {
/*  93 */     JXEditorPane editorPane = new JXEditorPane();
/*  94 */     editorPane.setEditable(false);
/*  95 */     editorPane.setContentType("text/html");
/*  96 */     return editorPane;
/*     */   }
/*     */   
/*     */   protected HyperlinkListener getHyperlinkListener() {
/* 100 */     if (this.hyperlinkListener == null) {
/* 101 */       this.hyperlinkListener = createHyperlinkListener();
/*     */     }
/* 103 */     return this.hyperlinkListener;
/*     */   }
/*     */   
/*     */   protected HyperlinkListener createHyperlinkListener() {
/* 107 */     new HyperlinkListener() {
/*     */       public void hyperlinkUpdate(HyperlinkEvent e) {
/* 109 */         if (HyperlinkEvent.EventType.ACTIVATED == e.getEventType()) {
/* 110 */           EditorPaneLinkVisitor.this.visitInternal(e.getURL());
/*     */         }
/*     */       }
/*     */     };
/*     */   }
/*     */   
/*     */ 
/*     */   protected LinkModel getInternalLink()
/*     */   {
/* 119 */     if (this.internalLink == null) {
/* 120 */       this.internalLink = new LinkModel("internal");
/*     */     }
/* 122 */     return this.internalLink;
/*     */   }
/*     */   
/*     */   protected void visitInternal(URL url) {
/*     */     try {
/* 127 */       getInternalLink().setURL(url);
/* 128 */       visit(getInternalLink());
/*     */     }
/*     */     catch (Exception e) {}
/*     */   }
/*     */ }


/* Location:              E:\java\java学习\hutubill\lib\all.jar!\org\jdesktop\swingx\hyperlink\EditorPaneLinkVisitor.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */
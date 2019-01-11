/*     */ package org.jdesktop.swingx.hyperlink;
/*     */ 
/*     */ import java.awt.event.ActionEvent;
/*     */ import java.awt.event.ActionListener;
/*     */ import java.beans.PropertyChangeEvent;
/*     */ import java.beans.PropertyChangeListener;
/*     */ import java.net.URL;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class LinkModelAction<T extends LinkModel>
/*     */   extends AbstractHyperlinkAction<T>
/*     */ {
/*     */   private ActionListener delegate;
/*     */   public static final String VISIT_ACTION = "visit";
/*     */   private PropertyChangeListener linkListener;
/*     */   
/*     */   public LinkModelAction()
/*     */   {
/*  49 */     this((LinkModel)null);
/*     */   }
/*     */   
/*     */   public LinkModelAction(ActionListener visitingDelegate) {
/*  53 */     this(null, visitingDelegate);
/*     */   }
/*     */   
/*     */   public LinkModelAction(T target) {
/*  57 */     this(target, null);
/*     */   }
/*     */   
/*     */   public LinkModelAction(T target, ActionListener visitingDelegate) {
/*  61 */     super(target);
/*  62 */     setVisitingDelegate(visitingDelegate);
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
/*     */   public void setVisitingDelegate(ActionListener delegate)
/*     */   {
/*  78 */     this.delegate = delegate;
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
/*  92 */     if ((this.delegate != null) && (getTarget() != null)) {
/*  93 */       this.delegate.actionPerformed(new ActionEvent(getTarget(), 1001, "visit"));
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected void installTarget()
/*     */   {
/* 104 */     if (getTarget() != null) {
/* 105 */       ((LinkModel)getTarget()).addPropertyChangeListener(getTargetListener());
/*     */     }
/* 107 */     updateFromTarget();
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
/*     */   protected void uninstallTarget()
/*     */   {
/* 120 */     if (getTarget() == null) return;
/* 121 */     ((LinkModel)getTarget()).removePropertyChangeListener(getTargetListener());
/*     */   }
/*     */   
/*     */   protected void updateFromTarget() {
/* 125 */     if (getTarget() != null) {
/* 126 */       putValue("Name", ((LinkModel)getTarget()).getText());
/* 127 */       putValue("ShortDescription", ((LinkModel)getTarget()).getURL().toString());
/* 128 */       putValue("visited", new Boolean(((LinkModel)getTarget()).getVisited()));
/*     */     } else {
/* 130 */       Object[] keys = getKeys();
/* 131 */       if (keys == null) return;
/* 132 */       for (int i = 0; i < keys.length; i++) {
/* 133 */         putValue(keys[i].toString(), null);
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   private PropertyChangeListener getTargetListener() {
/* 139 */     if (this.linkListener == null) {
/* 140 */       this.linkListener = new PropertyChangeListener()
/*     */       {
/*     */         public void propertyChange(PropertyChangeEvent evt) {
/* 143 */           LinkModelAction.this.updateFromTarget();
/*     */         }
/*     */       };
/*     */     }
/*     */     
/* 148 */     return this.linkListener;
/*     */   }
/*     */ }


/* Location:              E:\java\java学习\hutubill\lib\all.jar!\org\jdesktop\swingx\hyperlink\LinkModelAction.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */
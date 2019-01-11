/*     */ package org.jdesktop.swingx.event;
/*     */ 
/*     */ import javax.swing.event.EventListenerList;
/*     */ import javax.swing.event.TreeExpansionEvent;
/*     */ import javax.swing.event.TreeExpansionListener;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class TreeExpansionBroadcaster
/*     */   implements TreeExpansionListener
/*     */ {
/*     */   private Object source;
/*     */   private EventListenerList listeners;
/*     */   
/*     */   public TreeExpansionBroadcaster(Object source)
/*     */   {
/*  40 */     this.source = source;
/*     */   }
/*     */   
/*     */   public void addTreeExpansionListener(TreeExpansionListener l) {
/*  44 */     getEventListenerList().add(TreeExpansionListener.class, l);
/*     */   }
/*     */   
/*     */   public void removeTreeExpansionListener(TreeExpansionListener l) {
/*  48 */     if (!hasListeners())
/*  49 */       return;
/*  50 */     this.listeners.remove(TreeExpansionListener.class, l);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   private EventListenerList getEventListenerList()
/*     */   {
/*  57 */     if (this.listeners == null) {
/*  58 */       this.listeners = new EventListenerList();
/*     */     }
/*  60 */     return this.listeners;
/*     */   }
/*     */   
/*     */ 
/*     */   public void treeExpanded(TreeExpansionEvent event)
/*     */   {
/*  66 */     if (!hasListeners())
/*  67 */       return;
/*  68 */     fireTreeExpanded(retarget(event));
/*     */   }
/*     */   
/*     */   public void treeCollapsed(TreeExpansionEvent event)
/*     */   {
/*  73 */     if (!hasListeners())
/*  74 */       return;
/*  75 */     fireTreeCollapsed(retarget(event));
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   private void fireTreeExpanded(TreeExpansionEvent event)
/*     */   {
/*  82 */     TreeExpansionListener[] ls = (TreeExpansionListener[])this.listeners.getListeners(TreeExpansionListener.class);
/*     */     
/*  84 */     for (int i = ls.length - 1; i >= 0; i--) {
/*  85 */       ls[i].treeExpanded(event);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   private void fireTreeCollapsed(TreeExpansionEvent event)
/*     */   {
/*  93 */     TreeExpansionListener[] ls = (TreeExpansionListener[])this.listeners.getListeners(TreeExpansionListener.class);
/*     */     
/*  95 */     for (int i = ls.length - 1; i >= 0; i--) {
/*  96 */       ls[i].treeCollapsed(event);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   private TreeExpansionEvent retarget(TreeExpansionEvent event)
/*     */   {
/* 105 */     return new TreeExpansionEvent(this.source, event.getPath());
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   private boolean hasListeners()
/*     */   {
/* 112 */     return (this.listeners != null) && (this.listeners.getListenerCount() > 0);
/*     */   }
/*     */ }


/* Location:              E:\java\java学习\hutubill\lib\all.jar!\org\jdesktop\swingx\event\TreeExpansionBroadcaster.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */
/*     */ package org.jdesktop.swingx.hyperlink;
/*     */ 
/*     */ import java.awt.event.ItemEvent;
/*     */ import org.jdesktop.swingx.action.AbstractActionExt;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public abstract class AbstractHyperlinkAction<T>
/*     */   extends AbstractActionExt
/*     */ {
/*     */   public static final String VISITED_KEY = "visited";
/*     */   protected T target;
/*     */   
/*     */   public AbstractHyperlinkAction()
/*     */   {
/*  50 */     this(null);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public AbstractHyperlinkAction(T target)
/*     */   {
/*  60 */     setTarget(target);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setVisited(boolean visited)
/*     */   {
/*  69 */     putValue("visited", Boolean.valueOf(visited));
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean isVisited()
/*     */   {
/*  77 */     Boolean visited = (Boolean)getValue("visited");
/*  78 */     return Boolean.TRUE.equals(visited);
/*     */   }
/*     */   
/*     */   public T getTarget()
/*     */   {
/*  83 */     return (T)this.target;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setTarget(T target)
/*     */   {
/*  91 */     T oldTarget = getTarget();
/*  92 */     uninstallTarget();
/*  93 */     this.target = target;
/*  94 */     installTarget();
/*  95 */     firePropertyChange("target", oldTarget, getTarget());
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
/*     */   protected void installTarget()
/*     */   {
/* 112 */     setName(this.target != null ? this.target.toString() : "");
/* 113 */     setVisited(false);
/*     */   }
/*     */   
/*     */   protected void uninstallTarget() {}
/*     */   
/*     */   public void itemStateChanged(ItemEvent e) {}
/*     */   
/*     */   public void setStateAction(boolean state) {}
/*     */ }


/* Location:              E:\java\java学习\hutubill\lib\all.jar!\org\jdesktop\swingx\hyperlink\AbstractHyperlinkAction.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */
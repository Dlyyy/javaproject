/*     */ package org.jdesktop.swingx.decorator;
/*     */ 
/*     */ import java.awt.Component;
/*     */ import javax.swing.event.ChangeEvent;
/*     */ import javax.swing.event.ChangeListener;
/*     */ import org.jdesktop.swingx.event.WeakEventListenerList;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public abstract class AbstractHighlighter
/*     */   implements Highlighter
/*     */ {
/*     */   private transient ChangeEvent changeEvent;
/* 101 */   protected WeakEventListenerList listenerList = new WeakEventListenerList();
/*     */   
/*     */ 
/*     */ 
/*     */   private HighlightPredicate predicate;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public AbstractHighlighter()
/*     */   {
/* 112 */     this(null);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public AbstractHighlighter(HighlightPredicate predicate)
/*     */   {
/* 124 */     setHighlightPredicate(predicate);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setHighlightPredicate(HighlightPredicate predicate)
/*     */   {
/* 136 */     if (predicate == null) {
/* 137 */       predicate = HighlightPredicate.ALWAYS;
/*     */     }
/* 139 */     if (areEqual(predicate, getHighlightPredicate())) return;
/* 140 */     this.predicate = predicate;
/* 141 */     fireStateChanged();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public HighlightPredicate getHighlightPredicate()
/*     */   {
/* 151 */     return this.predicate;
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
/*     */   public Component highlight(Component component, ComponentAdapter adapter)
/*     */   {
/* 170 */     if ((canHighlight(component, adapter)) && (getHighlightPredicate().isHighlighted(component, adapter)))
/*     */     {
/* 172 */       component = doHighlight(component, adapter);
/*     */     }
/* 174 */     return component;
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
/*     */   protected boolean canHighlight(Component component, ComponentAdapter adapter)
/*     */   {
/* 190 */     return true;
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
/*     */   protected abstract Component doHighlight(Component paramComponent, ComponentAdapter paramComponentAdapter);
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected boolean areEqual(Object oneItem, Object anotherItem)
/*     */   {
/* 215 */     if ((oneItem == null) && (anotherItem == null)) return true;
/* 216 */     if (anotherItem != null) {
/* 217 */       return anotherItem.equals(oneItem);
/*     */     }
/* 219 */     return false;
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
/*     */   public final void addChangeListener(ChangeListener l)
/*     */   {
/* 232 */     this.listenerList.add(ChangeListener.class, l);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public final void removeChangeListener(ChangeListener l)
/*     */   {
/* 242 */     this.listenerList.remove(ChangeListener.class, l);
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
/*     */   public final ChangeListener[] getChangeListeners()
/*     */   {
/* 259 */     return (ChangeListener[])this.listenerList.getListeners(ChangeListener.class);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected final void fireStateChanged()
/*     */   {
/* 271 */     Object[] listeners = this.listenerList.getListenerList();
/* 272 */     for (int i = listeners.length - 2; i >= 0; i -= 2) {
/* 273 */       if (listeners[i] == ChangeListener.class) {
/* 274 */         if (this.changeEvent == null) {
/* 275 */           this.changeEvent = new ChangeEvent(this);
/*     */         }
/* 277 */         ((ChangeListener)listeners[(i + 1)]).stateChanged(this.changeEvent);
/*     */       }
/*     */     }
/*     */   }
/*     */ }


/* Location:              E:\java\java学习\hutubill\lib\all.jar!\org\jdesktop\swingx\decorator\AbstractHighlighter.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */
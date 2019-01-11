/*     */ package org.jdesktop.swingx.action;
/*     */ 
/*     */ import java.beans.PropertyChangeEvent;
/*     */ import java.beans.PropertyChangeListener;
/*     */ import java.lang.ref.WeakReference;
/*     */ import javax.swing.AbstractAction;
/*     */ import javax.swing.AbstractButton;
/*     */ import javax.swing.Action;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ class ToggleActionPropertyChangeListener
/*     */   implements PropertyChangeListener
/*     */ {
/*     */   private WeakReference<AbstractButton> buttonRef;
/*     */   
/*     */   public ToggleActionPropertyChangeListener(Action action, AbstractButton button)
/*     */   {
/*  51 */     if (shouldAddListener(action, button)) {
/*  52 */       this.buttonRef = new WeakReference(button);
/*  53 */       action.addPropertyChangeListener(this);
/*     */     }
/*     */   }
/*     */   
/*     */   protected synchronized boolean shouldAddListener(Action action, AbstractButton button)
/*     */   {
/*  59 */     releasePCLs(action);
/*     */     
/*     */ 
/*     */ 
/*  63 */     return !isToggling(action, button);
/*     */   }
/*     */   
/*     */ 
/*     */   protected boolean isToggling(Action action, AbstractButton button)
/*     */   {
/*  69 */     if (!(action instanceof AbstractAction)) return false;
/*  70 */     PropertyChangeListener[] listeners = ((AbstractAction)action).getPropertyChangeListeners();
/*  71 */     for (int i = listeners.length - 1; i >= 0; i--) {
/*  72 */       if (((listeners[i] instanceof ToggleActionPropertyChangeListener)) && 
/*  73 */         (((ToggleActionPropertyChangeListener)listeners[i]).isToggling(button))) { return true;
/*     */       }
/*     */     }
/*  76 */     return false;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected void releasePCLs(Action action)
/*     */   {
/*  86 */     if (!(action instanceof AbstractAction)) return;
/*  87 */     PropertyChangeListener[] listeners = ((AbstractAction)action).getPropertyChangeListeners();
/*  88 */     for (int i = listeners.length - 1; i >= 0; i--) {
/*  89 */       if ((listeners[i] instanceof ToggleActionPropertyChangeListener)) {
/*  90 */         ((ToggleActionPropertyChangeListener)listeners[i]).checkReferent(action);
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   public void propertyChange(PropertyChangeEvent evt)
/*     */   {
/*  97 */     AbstractButton button = checkReferent((Action)evt.getSource());
/*  98 */     if (button == null) return;
/*  99 */     String propertyName = evt.getPropertyName();
/*     */     
/* 101 */     if (propertyName.equals("selected")) {
/* 102 */       Boolean selected = (Boolean)evt.getNewValue();
/* 103 */       button.setSelected(selected.booleanValue());
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
/*     */ 
/*     */ 
/*     */   protected AbstractButton checkReferent(Action action)
/*     */   {
/* 120 */     AbstractButton button = null;
/* 121 */     if (this.buttonRef != null) {
/* 122 */       button = (AbstractButton)this.buttonRef.get();
/*     */     }
/* 124 */     if (button == null) {
/* 125 */       if (action != null) {
/* 126 */         action.removePropertyChangeListener(this);
/*     */       }
/* 128 */       this.buttonRef = null;
/*     */     }
/* 130 */     return button;
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
/*     */   public boolean isToggling(AbstractButton button)
/*     */   {
/* 147 */     return button.equals(checkReferent(null));
/*     */   }
/*     */ }


/* Location:              E:\java\java学习\hutubill\lib\all.jar!\org\jdesktop\swingx\action\ToggleActionPropertyChangeListener.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */
/*     */ package org.jdesktop.beans;
/*     */ 
/*     */ import java.beans.PropertyChangeEvent;
/*     */ import java.beans.PropertyChangeListener;
/*     */ import java.beans.PropertyChangeSupport;
/*     */ import java.beans.PropertyVetoException;
/*     */ import java.beans.VetoableChangeListener;
/*     */ import java.beans.VetoableChangeSupport;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public abstract class AbstractBean
/*     */ {
/*     */   private transient PropertyChangeSupport pcs;
/*     */   private transient VetoableChangeSupport vcs;
/*     */   
/*     */   protected AbstractBean()
/*     */   {
/* 156 */     this.pcs = new PropertyChangeSupport(this);
/* 157 */     this.vcs = new VetoableChangeSupport(this);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   protected AbstractBean(PropertyChangeSupport pcs, VetoableChangeSupport vcs)
/*     */   {
/* 165 */     if (pcs == null) {
/* 166 */       throw new NullPointerException("PropertyChangeSupport must not be null");
/*     */     }
/* 168 */     if (vcs == null) {
/* 169 */       throw new NullPointerException("VetoableChangeSupport must not be null");
/*     */     }
/*     */     
/* 172 */     this.pcs = pcs;
/* 173 */     this.vcs = vcs;
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
/*     */   public final void addPropertyChangeListener(PropertyChangeListener listener)
/*     */   {
/* 187 */     this.pcs.addPropertyChangeListener(listener);
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
/*     */   public final void removePropertyChangeListener(PropertyChangeListener listener)
/*     */   {
/* 202 */     this.pcs.removePropertyChangeListener(listener);
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public final PropertyChangeListener[] getPropertyChangeListeners()
/*     */   {
/* 236 */     return this.pcs.getPropertyChangeListeners();
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
/*     */   public final void addPropertyChangeListener(String propertyName, PropertyChangeListener listener)
/*     */   {
/* 253 */     this.pcs.addPropertyChangeListener(propertyName, listener);
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
/*     */   public final void removePropertyChangeListener(String propertyName, PropertyChangeListener listener)
/*     */   {
/* 270 */     this.pcs.removePropertyChangeListener(propertyName, listener);
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
/*     */   public final PropertyChangeListener[] getPropertyChangeListeners(String propertyName)
/*     */   {
/* 284 */     return this.pcs.getPropertyChangeListeners(propertyName);
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
/*     */   protected final void firePropertyChange(String propertyName, Object oldValue, Object newValue)
/*     */   {
/* 302 */     this.pcs.firePropertyChange(propertyName, oldValue, newValue);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected final void firePropertyChange(PropertyChangeEvent evt)
/*     */   {
/* 312 */     this.pcs.firePropertyChange(evt);
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected final void fireIndexedPropertyChange(String propertyName, int index, Object oldValue, Object newValue)
/*     */   {
/* 335 */     this.pcs.fireIndexedPropertyChange(propertyName, index, oldValue, newValue);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected final boolean hasPropertyChangeListeners(String propertyName)
/*     */   {
/* 347 */     return this.pcs.hasListeners(propertyName);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected final boolean hasVetoableChangeListeners(String propertyName)
/*     */   {
/* 359 */     return this.vcs.hasListeners(propertyName);
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
/*     */   public final void addVetoableChangeListener(VetoableChangeListener listener)
/*     */   {
/* 374 */     this.vcs.addVetoableChangeListener(listener);
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
/*     */   public final void removeVetoableChangeListener(VetoableChangeListener listener)
/*     */   {
/* 389 */     this.vcs.removeVetoableChangeListener(listener);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public final VetoableChangeListener[] getVetoableChangeListeners()
/*     */   {
/* 400 */     return this.vcs.getVetoableChangeListeners();
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
/*     */   public final void addVetoableChangeListener(String propertyName, VetoableChangeListener listener)
/*     */   {
/* 419 */     this.vcs.addVetoableChangeListener(propertyName, listener);
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
/*     */   public final void removeVetoableChangeListener(String propertyName, VetoableChangeListener listener)
/*     */   {
/* 438 */     this.vcs.removeVetoableChangeListener(propertyName, listener);
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
/*     */   public final VetoableChangeListener[] getVetoableChangeListeners(String propertyName)
/*     */   {
/* 452 */     return this.vcs.getVetoableChangeListeners(propertyName);
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
/*     */   protected final void fireVetoableChange(String propertyName, Object oldValue, Object newValue)
/*     */     throws PropertyVetoException
/*     */   {
/* 472 */     this.vcs.fireVetoableChange(propertyName, oldValue, newValue);
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
/*     */   protected final void fireVetoableChange(PropertyChangeEvent evt)
/*     */     throws PropertyVetoException
/*     */   {
/* 488 */     this.vcs.fireVetoableChange(evt);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public Object clone()
/*     */     throws CloneNotSupportedException
/*     */   {
/* 496 */     AbstractBean result = (AbstractBean)super.clone();
/* 497 */     result.pcs = new PropertyChangeSupport(result);
/* 498 */     result.vcs = new VetoableChangeSupport(result);
/* 499 */     return result;
/*     */   }
/*     */ }


/* Location:              E:\java\java学习\hutubill\lib\all.jar!\org\jdesktop\beans\AbstractBean.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */
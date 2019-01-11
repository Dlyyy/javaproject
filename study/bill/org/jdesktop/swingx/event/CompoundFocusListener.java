/*     */ package org.jdesktop.swingx.event;
/*     */ 
/*     */ import java.awt.Component;
/*     */ import java.awt.KeyboardFocusManager;
/*     */ import java.beans.PropertyChangeEvent;
/*     */ import java.beans.PropertyChangeListener;
/*     */ import javax.swing.JComponent;
/*     */ import org.jdesktop.beans.AbstractBean;
/*     */ import org.jdesktop.swingx.SwingXUtilities;
/*     */ import org.jdesktop.swingx.util.Contract;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class CompoundFocusListener
/*     */   extends AbstractBean
/*     */ {
/*     */   private JComponent root;
/*     */   private PropertyChangeListener managerListener;
/*     */   private boolean focused;
/*     */   
/*     */   public CompoundFocusListener(JComponent root)
/*     */   {
/*  72 */     this.root = ((JComponent)Contract.asNotNull(root, "root must not be null"));
/*  73 */     KeyboardFocusManager manager = KeyboardFocusManager.getCurrentKeyboardFocusManager();
/*  74 */     addManagerListener(manager);
/*  75 */     permanentFocusOwnerChanged(manager.getPermanentFocusOwner());
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
/*     */   public boolean isFocused()
/*     */   {
/*  88 */     return this.focused;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void release()
/*     */   {
/*  98 */     removeManagerListener(KeyboardFocusManager.getCurrentKeyboardFocusManager());
/*  99 */     removeAllListeners();
/* 100 */     this.root = null;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   private void removeAllListeners()
/*     */   {
/* 107 */     for (PropertyChangeListener l : getPropertyChangeListeners()) {
/* 108 */       removePropertyChangeListener(l);
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
/*     */   protected void permanentFocusOwnerChanged(Component focusOwner)
/*     */   {
/* 123 */     if (focusOwner == null) return;
/* 124 */     setFocused(SwingXUtilities.isDescendingFrom(focusOwner, this.root));
/*     */   }
/*     */   
/*     */   private void setFocused(boolean focused) {
/* 128 */     boolean old = isFocused();
/* 129 */     this.focused = focused;
/* 130 */     firePropertyChange("focused", Boolean.valueOf(old), Boolean.valueOf(isFocused()));
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private void addManagerListener(KeyboardFocusManager manager)
/*     */   {
/* 141 */     manager.addPropertyChangeListener("permanentFocusOwner", getManagerListener());
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private void removeManagerListener(KeyboardFocusManager manager)
/*     */   {
/* 151 */     manager.removePropertyChangeListener("permanentFocusOwner", getManagerListener());
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private PropertyChangeListener getManagerListener()
/*     */   {
/* 161 */     if (this.managerListener == null) {
/* 162 */       this.managerListener = new PropertyChangeListener()
/*     */       {
/*     */         public void propertyChange(PropertyChangeEvent evt) {
/* 165 */           if ("permanentFocusOwner".equals(evt.getPropertyName())) {
/* 166 */             CompoundFocusListener.this.permanentFocusOwnerChanged((Component)evt.getNewValue());
/*     */           }
/*     */         }
/*     */       };
/*     */     }
/* 171 */     return this.managerListener;
/*     */   }
/*     */ }


/* Location:              E:\java\java学习\hutubill\lib\all.jar!\org\jdesktop\swingx\event\CompoundFocusListener.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */
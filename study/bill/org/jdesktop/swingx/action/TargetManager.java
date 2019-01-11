/*     */ package org.jdesktop.swingx.action;
/*     */ 
/*     */ import java.awt.Component;
/*     */ import java.awt.KeyboardFocusManager;
/*     */ import java.awt.event.ActionEvent;
/*     */ import java.beans.PropertyChangeListener;
/*     */ import java.beans.PropertyChangeSupport;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import javax.swing.Action;
/*     */ import javax.swing.ActionMap;
/*     */ import javax.swing.JComponent;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class TargetManager
/*     */ {
/*     */   private static TargetManager INSTANCE;
/*     */   private List<Targetable> targetList;
/*     */   private Targetable target;
/*     */   private PropertyChangeSupport propertySupport;
/*     */   
/*     */   public TargetManager()
/*     */   {
/*  98 */     this.propertySupport = new PropertyChangeSupport(this);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public static TargetManager getInstance()
/*     */   {
/* 105 */     if (INSTANCE == null) {
/* 106 */       INSTANCE = new TargetManager();
/*     */     }
/* 108 */     return INSTANCE;
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
/*     */   public void addTarget(Targetable target, boolean prepend)
/*     */   {
/* 123 */     if (this.targetList == null) {
/* 124 */       this.targetList = new ArrayList();
/*     */     }
/* 126 */     if (prepend) {
/* 127 */       this.targetList.add(0, target);
/*     */     } else {
/* 129 */       this.targetList.add(target);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void addTarget(Targetable target)
/*     */   {
/* 139 */     addTarget(target, false);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public void removeTarget(Targetable target)
/*     */   {
/* 146 */     if (this.targetList != null) {
/* 147 */       this.targetList.remove(target);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public Targetable[] getTargets()
/*     */   {
/*     */     Targetable[] targets;
/*     */     
/*     */ 
/*     */     Targetable[] targets;
/*     */     
/* 160 */     if (this.targetList == null) {
/* 161 */       targets = new Targetable[0];
/*     */     } else {
/* 163 */       targets = new Targetable[this.targetList.size()];
/* 164 */       targets = (Targetable[])this.targetList.toArray(new Targetable[this.targetList.size()]);
/*     */     }
/* 166 */     return targets;
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
/*     */   public void setTarget(Targetable newTarget)
/*     */   {
/* 182 */     Targetable oldTarget = this.target;
/* 183 */     if (oldTarget != newTarget) {
/* 184 */       this.target = newTarget;
/* 185 */       this.propertySupport.firePropertyChange("target", oldTarget, newTarget);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public Targetable getTarget()
/*     */   {
/* 196 */     return this.target;
/*     */   }
/*     */   
/*     */   public void addPropertyChangeListener(PropertyChangeListener listener) {
/* 200 */     this.propertySupport.addPropertyChangeListener(listener);
/*     */   }
/*     */   
/*     */   public void removePropertyChangeListener(PropertyChangeListener listener) {
/* 204 */     this.propertySupport.removePropertyChangeListener(listener);
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
/*     */   public boolean doCommand(Object command, Object value)
/*     */   {
/* 221 */     if ((this.target != null) && 
/* 222 */       (this.target.hasCommand(command)) && (this.target.doCommand(command, value))) {
/* 223 */       return true;
/*     */     }
/*     */     
/*     */ 
/*     */ 
/* 228 */     if (this.targetList != null) {
/* 229 */       Iterator<Targetable> iter = this.targetList.iterator();
/* 230 */       while (iter.hasNext()) {
/* 231 */         Targetable target = (Targetable)iter.next();
/* 232 */         if ((target.hasCommand(command)) && (target.doCommand(command, value)))
/*     */         {
/* 234 */           return true;
/*     */         }
/*     */       }
/*     */     }
/*     */     
/* 239 */     ActionEvent evt = null;
/* 240 */     if ((value instanceof ActionEvent)) {
/* 241 */       evt = (ActionEvent)value;
/*     */     }
/*     */     
/*     */ 
/*     */ 
/* 246 */     Component comp = KeyboardFocusManager.getCurrentKeyboardFocusManager().getPermanentFocusOwner();
/* 247 */     while (comp != null) {
/* 248 */       if ((comp instanceof JComponent)) {
/* 249 */         ActionMap map = ((JComponent)comp).getActionMap();
/* 250 */         Action action = map.get(command);
/* 251 */         if (action != null) {
/* 252 */           if (evt == null) {
/* 253 */             evt = new ActionEvent(comp, 0, command.toString());
/*     */           }
/* 255 */           action.actionPerformed(evt);
/*     */           
/* 257 */           return true;
/*     */         }
/*     */       }
/* 260 */       comp = comp.getParent();
/*     */     }
/*     */     
/* 263 */     return false;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   void reset()
/*     */   {
/* 271 */     if (this.targetList != null) {
/* 272 */       this.targetList.clear();
/* 273 */       this.targetList = null;
/*     */     }
/* 275 */     this.target = null;
/*     */     
/* 277 */     PropertyChangeListener[] listeners = this.propertySupport.getPropertyChangeListeners();
/* 278 */     for (int i = 0; i < listeners.length; i++) {
/* 279 */       this.propertySupport.removePropertyChangeListener(listeners[i]);
/*     */     }
/* 281 */     INSTANCE = null;
/*     */   }
/*     */ }


/* Location:              E:\java\java学习\hutubill\lib\all.jar!\org\jdesktop\swingx\action\TargetManager.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */
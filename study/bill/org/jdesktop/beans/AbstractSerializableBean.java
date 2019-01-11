/*    */ package org.jdesktop.beans;
/*    */ 
/*    */ import java.beans.PropertyChangeListener;
/*    */ import java.beans.PropertyChangeSupport;
/*    */ import java.beans.VetoableChangeListener;
/*    */ import java.beans.VetoableChangeSupport;
/*    */ import java.io.IOException;
/*    */ import java.io.ObjectInputStream;
/*    */ import java.io.ObjectOutputStream;
/*    */ import java.io.Serializable;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public abstract class AbstractSerializableBean
/*    */   extends AbstractBean
/*    */   implements Serializable
/*    */ {
/*    */   protected AbstractSerializableBean() {}
/*    */   
/*    */   protected AbstractSerializableBean(PropertyChangeSupport pcs, VetoableChangeSupport vcs)
/*    */   {
/* 69 */     super(pcs, vcs);
/*    */   }
/*    */   
/*    */   private void writeObject(ObjectOutputStream s) throws IOException {
/* 73 */     s.defaultWriteObject();
/*    */     
/* 75 */     for (PropertyChangeListener l : getPropertyChangeListeners()) {
/* 76 */       if ((l instanceof Serializable)) {
/* 77 */         s.writeObject(l);
/*    */       }
/*    */     }
/*    */     
/* 81 */     for (VetoableChangeListener l : getVetoableChangeListeners()) {
/* 82 */       if ((l instanceof Serializable)) {
/* 83 */         s.writeObject(l);
/*    */       }
/*    */     }
/*    */     
/* 87 */     s.writeObject(null);
/*    */   }
/*    */   
/*    */   private void readObject(ObjectInputStream s) throws ClassNotFoundException, IOException
/*    */   {
/* 92 */     s.defaultReadObject();
/*    */     
/*    */     Object listenerOrNull;
/* 95 */     while (null != (listenerOrNull = s.readObject())) {
/* 96 */       if ((listenerOrNull instanceof PropertyChangeListener)) {
/* 97 */         addPropertyChangeListener((PropertyChangeListener)listenerOrNull);
/* 98 */       } else if ((listenerOrNull instanceof VetoableChangeListener)) {
/* 99 */         addVetoableChangeListener((VetoableChangeListener)listenerOrNull);
/*    */       }
/*    */     }
/*    */   }
/*    */ }


/* Location:              E:\java\java学习\hutubill\lib\all.jar!\org\jdesktop\beans\AbstractSerializableBean.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */
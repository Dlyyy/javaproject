/*     */ package org.jdesktop.swingx.event;
/*     */ 
/*     */ import java.io.Serializable;
/*     */ import java.lang.ref.WeakReference;
/*     */ import java.lang.reflect.Array;
/*     */ import java.util.ArrayList;
/*     */ import java.util.EventListener;
/*     */ import java.util.List;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class WeakEventListenerList
/*     */   implements Serializable
/*     */ {
/*     */   protected transient List<WeakReference<? extends EventListener>> weakReferences;
/*     */   protected transient List<Class<? extends EventListener>> classes;
/*     */   
/*     */   public Object[] getListenerList()
/*     */   {
/* 114 */     List<? extends EventListener> listeners = cleanReferences();
/* 115 */     Object[] result = new Object[listeners.size() * 2];
/* 116 */     for (int i = 0; i < listeners.size(); i++) {
/* 117 */       result[(2 * i + 1)] = listeners.get(i);
/* 118 */       result[(2 * i)] = getClasses().get(i);
/*     */     }
/* 120 */     return result;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private synchronized <T extends EventListener> List<T> cleanReferences()
/*     */   {
/* 131 */     List<T> listeners = new ArrayList();
/* 132 */     for (int i = getReferences().size() - 1; i >= 0; i--)
/*     */     {
/* 134 */       Object listener = ((WeakReference)getReferences().get(i)).get();
/* 135 */       if (listener == null) {
/* 136 */         getReferences().remove(i);
/* 137 */         getClasses().remove(i);
/*     */       } else {
/* 139 */         listeners.add(0, (EventListener)listener);
/*     */       }
/*     */     }
/* 142 */     return listeners;
/*     */   }
/*     */   
/*     */   private List<WeakReference<? extends EventListener>> getReferences() {
/* 146 */     if (this.weakReferences == null) {
/* 147 */       this.weakReferences = new ArrayList();
/*     */     }
/* 149 */     return this.weakReferences;
/*     */   }
/*     */   
/*     */   private List<Class<? extends EventListener>> getClasses() {
/* 153 */     if (this.classes == null) {
/* 154 */       this.classes = new ArrayList();
/*     */     }
/*     */     
/* 157 */     return this.classes;
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
/*     */   public <T extends EventListener> T[] getListeners(Class<T> t)
/*     */   {
/* 171 */     List<T> liveListeners = cleanReferences();
/* 172 */     List<T> listeners = new ArrayList();
/* 173 */     for (int i = 0; i < liveListeners.size(); i++) {
/* 174 */       if (getClasses().get(i) == t) {
/* 175 */         listeners.add(liveListeners.get(i));
/*     */       }
/*     */     }
/* 178 */     T[] result = (EventListener[])Array.newInstance(t, listeners.size());
/* 179 */     return (EventListener[])listeners.toArray(result);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public synchronized <T extends EventListener> void add(Class<T> t, T l)
/*     */   {
/* 190 */     if (l == null)
/*     */     {
/*     */ 
/*     */ 
/* 194 */       return;
/*     */     }
/* 196 */     if (!t.isInstance(l)) {
/* 197 */       throw new IllegalArgumentException("Listener " + l + " is not of type " + t);
/*     */     }
/*     */     
/* 200 */     cleanReferences();
/* 201 */     getReferences().add(new WeakReference(l));
/* 202 */     getClasses().add(t);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public synchronized <T extends EventListener> void remove(Class<T> t, T l)
/*     */   {
/* 211 */     if (l == null)
/*     */     {
/*     */ 
/*     */ 
/* 215 */       return;
/*     */     }
/* 217 */     if (!t.isInstance(l)) {
/* 218 */       throw new IllegalArgumentException("Listener " + l + " is not of type " + t);
/*     */     }
/*     */     
/* 221 */     for (int i = 0; i < getReferences().size(); i++) {
/* 222 */       if ((l.equals(((WeakReference)getReferences().get(i)).get())) && (t == getClasses().get(i)))
/*     */       {
/* 224 */         getReferences().remove(i);
/* 225 */         getClasses().remove(i);
/* 226 */         break;
/*     */       }
/*     */     }
/*     */   }
/*     */ }


/* Location:              E:\java\java学习\hutubill\lib\all.jar!\org\jdesktop\swingx\event\WeakEventListenerList.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */
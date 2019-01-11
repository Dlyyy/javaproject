/*     */ package org.jdesktop.swingx.event;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import java.util.EventListener;
/*     */ import java.util.HashMap;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class EventListenerMap
/*     */ {
/*  37 */   private final Map<Class<? extends EventListener>, List<? extends EventListener>> listenerList = new HashMap();
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public List<EventListener> getListeners()
/*     */   {
/*  46 */     List<EventListener> listeners = new ArrayList();
/*     */     
/*  48 */     for (List<? extends EventListener> list : this.listenerList.values()) {
/*  49 */       listeners.addAll(list);
/*     */     }
/*     */     
/*  52 */     return listeners;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public <T extends EventListener> List<T> getListeners(Class<T> clazz)
/*     */   {
/*  62 */     List<T> list = (List)this.listenerList.get(clazz);
/*  63 */     if (list == null) {
/*  64 */       list = new ArrayList();
/*     */     }
/*  66 */     return list;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public int getListenerCount()
/*     */   {
/*  74 */     int count = 0;
/*     */     
/*  76 */     for (List<? extends EventListener> list : this.listenerList.values()) {
/*  77 */       count += list.size();
/*     */     }
/*     */     
/*  80 */     return count;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public <T extends EventListener> int getListenerCount(Class<T> clazz)
/*     */   {
/*  88 */     List<T> list = (List)this.listenerList.get(clazz);
/*  89 */     if (list != null) {
/*  90 */       return list.size();
/*     */     }
/*  92 */     return 0;
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
/*     */   public synchronized <T extends EventListener> void add(Class<T> clazz, T listener)
/*     */   {
/* 107 */     if (listener == null) {
/* 108 */       return;
/*     */     }
/*     */     
/* 111 */     List<T> list = (List)this.listenerList.get(clazz);
/* 112 */     if (list == null) {
/* 113 */       list = new ArrayList();
/* 114 */       this.listenerList.put(clazz, list);
/*     */     }
/* 116 */     list.add(listener);
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
/*     */   public synchronized <T extends EventListener> void remove(Class<T> clazz, T listener)
/*     */   {
/* 131 */     if (listener == null) {
/* 132 */       return;
/*     */     }
/*     */     
/* 135 */     List<T> list = (List)this.listenerList.get(clazz);
/* 136 */     if (list != null) {
/* 137 */       list.remove(listener);
/*     */     }
/*     */   }
/*     */ }


/* Location:              E:\java\java学习\hutubill\lib\all.jar!\org\jdesktop\swingx\event\EventListenerMap.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */
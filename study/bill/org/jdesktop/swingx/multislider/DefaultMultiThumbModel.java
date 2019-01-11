/*     */ package org.jdesktop.swingx.multislider;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collections;
/*     */ import java.util.Comparator;
/*     */ import java.util.Iterator;
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
/*     */ public class DefaultMultiThumbModel<E>
/*     */   extends AbstractMultiThumbModel<E>
/*     */   implements MultiThumbModel<E>
/*     */ {
/*  37 */   protected List<Thumb<E>> thumbs = new ArrayList();
/*     */   
/*     */   public DefaultMultiThumbModel()
/*     */   {
/*  41 */     setMinimumValue(0.0F);
/*  42 */     setMaximumValue(1.0F);
/*     */   }
/*     */   
/*     */   public int addThumb(float value, E obj) {
/*  46 */     Thumb<E> thumb = new Thumb(this);
/*  47 */     thumb.setPosition(value);
/*  48 */     thumb.setObject(obj);
/*  49 */     this.thumbs.add(thumb);
/*  50 */     int n = this.thumbs.size();
/*  51 */     ThumbDataEvent evt = new ThumbDataEvent(this, -1, this.thumbs.size() - 1, thumb);
/*  52 */     for (ThumbDataListener tdl : this.thumbDataListeners) {
/*  53 */       tdl.thumbAdded(evt);
/*     */     }
/*  55 */     return n - 1;
/*     */   }
/*     */   
/*     */   public void insertThumb(float value, E obj, int index) {
/*  59 */     Thumb<E> thumb = new Thumb(this);
/*  60 */     thumb.setPosition(value);
/*  61 */     thumb.setObject(obj);
/*  62 */     this.thumbs.add(index, thumb);
/*  63 */     ThumbDataEvent evt = new ThumbDataEvent(this, -1, index, thumb);
/*  64 */     for (ThumbDataListener tdl : this.thumbDataListeners) {
/*  65 */       tdl.thumbAdded(evt);
/*     */     }
/*     */   }
/*     */   
/*     */   public void removeThumb(int index) {
/*  70 */     Thumb<E> thumb = (Thumb)this.thumbs.remove(index);
/*  71 */     ThumbDataEvent evt = new ThumbDataEvent(this, -1, index, thumb);
/*  72 */     for (ThumbDataListener tdl : this.thumbDataListeners) {
/*  73 */       tdl.thumbRemoved(evt);
/*     */     }
/*     */   }
/*     */   
/*     */   public int getThumbCount() {
/*  78 */     return this.thumbs.size();
/*     */   }
/*     */   
/*     */   public Thumb<E> getThumbAt(int index) {
/*  82 */     return (Thumb)this.thumbs.get(index);
/*     */   }
/*     */   
/*     */   public List<Thumb<E>> getSortedThumbs() {
/*  86 */     List<Thumb<E>> list = new ArrayList();
/*  87 */     list.addAll(this.thumbs);
/*  88 */     Collections.sort(list, new Comparator() {
/*     */       public int compare(Thumb<E> o1, Thumb<E> o2) {
/*  90 */         float f1 = o1.getPosition();
/*  91 */         float f2 = o2.getPosition();
/*  92 */         if (f1 < f2) {
/*  93 */           return -1;
/*     */         }
/*  95 */         if (f1 > f2) {
/*  96 */           return 1;
/*     */         }
/*  98 */         return 0;
/*     */       }
/* 100 */     });
/* 101 */     return list;
/*     */   }
/*     */   
/*     */   public Iterator<Thumb<E>> iterator() {
/* 105 */     return this.thumbs.iterator();
/*     */   }
/*     */   
/*     */   public int getThumbIndex(Thumb<E> thumb) {
/* 109 */     return this.thumbs.indexOf(thumb);
/*     */   }
/*     */ }


/* Location:              E:\java\java学习\hutubill\lib\all.jar!\org\jdesktop\swingx\multislider\DefaultMultiThumbModel.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */
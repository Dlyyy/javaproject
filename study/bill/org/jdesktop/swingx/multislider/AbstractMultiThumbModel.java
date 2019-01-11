/*    */ package org.jdesktop.swingx.multislider;
/*    */ 
/*    */ import java.util.ArrayList;
/*    */ import java.util.List;
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
/*    */ public abstract class AbstractMultiThumbModel<E>
/*    */   implements MultiThumbModel<E>
/*    */ {
/* 36 */   protected float maximumValue = 1.0F;
/* 37 */   protected float minimumValue = 0.0F;
/*    */   
/*    */   public float getMaximumValue() {
/* 40 */     return this.maximumValue;
/*    */   }
/*    */   
/*    */   public float getMinimumValue() {
/* 44 */     return this.minimumValue;
/*    */   }
/*    */   
/*    */   public void setMaximumValue(float maximumValue) {
/* 48 */     this.maximumValue = maximumValue;
/*    */   }
/*    */   
/*    */   public void setMinimumValue(float minimumValue) {
/* 52 */     this.minimumValue = minimumValue;
/*    */   }
/*    */   
/* 55 */   protected List<ThumbDataListener> thumbDataListeners = new ArrayList();
/*    */   
/*    */   public void addThumbDataListener(ThumbDataListener listener) {
/* 58 */     this.thumbDataListeners.add(listener);
/*    */   }
/*    */   
/*    */   public void removeThumbDataListener(ThumbDataListener listener) {
/* 62 */     this.thumbDataListeners.remove(listener);
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/* 68 */   public void thumbPositionChanged(Thumb<E> thumb) { fireThumbPositionChanged(thumb); }
/*    */   
/*    */   protected void fireThumbPositionChanged(Thumb<E> thumb) {
/*    */     ThumbDataEvent evt;
/* 72 */     if (getThumbIndex(thumb) >= 0) {
/* 73 */       evt = new ThumbDataEvent(this, -1, getThumbIndex(thumb), thumb);
/* 74 */       for (ThumbDataListener l : this.thumbDataListeners)
/* 75 */         l.positionChanged(evt);
/*    */     }
/*    */   }
/*    */   
/*    */   public void thumbValueChanged(Thumb<E> thumb) {
/* 80 */     fireThumbValueChanged(thumb);
/*    */   }
/*    */   
/*    */   protected void fireThumbValueChanged(Thumb<E> thumb) {
/* 84 */     ThumbDataEvent evt = new ThumbDataEvent(this, -1, getThumbIndex(thumb), thumb);
/* 85 */     for (ThumbDataListener l : this.thumbDataListeners) {
/* 86 */       l.valueChanged(evt);
/*    */     }
/*    */   }
/*    */ }


/* Location:              E:\java\java学习\hutubill\lib\all.jar!\org\jdesktop\swingx\multislider\AbstractMultiThumbModel.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */
/*    */ package org.jdesktop.swingx.multislider;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class Thumb<E>
/*    */ {
/*    */   private float position;
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   private E object;
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   private MultiThumbModel<E> model;
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   public Thumb(MultiThumbModel<E> model)
/*    */   {
/* 35 */     this.model = model;
/*    */   }
/*    */   
/*    */   public float getPosition() {
/* 39 */     return this.position;
/*    */   }
/*    */   
/*    */   public void setPosition(float position) {
/* 43 */     this.position = position;
/* 44 */     this.model.thumbPositionChanged(this);
/*    */   }
/*    */   
/*    */   public E getObject() {
/* 48 */     return (E)this.object;
/*    */   }
/*    */   
/*    */   public void setObject(E object) {
/* 52 */     this.object = object;
/* 53 */     this.model.thumbValueChanged(this);
/*    */   }
/*    */ }


/* Location:              E:\java\java学习\hutubill\lib\all.jar!\org\jdesktop\swingx\multislider\Thumb.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */
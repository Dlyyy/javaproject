/*    */ package org.jdesktop.swingx.multislider;
/*    */ 
/*    */ import java.util.EventObject;
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
/*    */ public class ThumbDataEvent
/*    */   extends EventObject
/*    */ {
/*    */   private int type;
/*    */   private int index;
/*    */   private Thumb<?> thumb;
/*    */   
/*    */   public ThumbDataEvent(Object source, int type, int index, Thumb<?> thumb)
/*    */   {
/* 35 */     super(source);
/* 36 */     this.type = type;
/* 37 */     this.thumb = thumb;
/* 38 */     this.index = index;
/*    */   }
/*    */   
/*    */   public int getType() {
/* 42 */     return this.type;
/*    */   }
/*    */   
/*    */   public int getIndex() {
/* 46 */     return this.index;
/*    */   }
/*    */   
/*    */   public Thumb<?> getThumb() {
/* 50 */     return this.thumb;
/*    */   }
/*    */   
/*    */   public String toString()
/*    */   {
/* 55 */     return getClass().getName() + " : " + this.type + " " + this.index + " " + this.thumb;
/*    */   }
/*    */ }


/* Location:              E:\java\java学习\hutubill\lib\all.jar!\org\jdesktop\swingx\multislider\ThumbDataEvent.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */
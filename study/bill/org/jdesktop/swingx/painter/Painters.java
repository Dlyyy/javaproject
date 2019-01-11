/*    */ package org.jdesktop.swingx.painter;
/*    */ 
/*    */ import java.awt.Graphics2D;
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
/*    */ public final class Painters
/*    */ {
/* 30 */   public static final Painter<Object> EMPTY_PAINTER = new Painter()
/*    */   {
/*    */     public void paint(Graphics2D g, Object object, int width, int height) {}
/*    */   };
/*    */ }


/* Location:              E:\java\java学习\hutubill\lib\all.jar!\org\jdesktop\swingx\painter\Painters.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */
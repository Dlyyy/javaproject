/*    */ package org.jdesktop.swingx.painter.effects;
/*    */ 
/*    */ import java.awt.Color;
/*    */ import java.awt.Point;
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
/*    */ public class ShadowPathEffect
/*    */   extends AbstractAreaEffect
/*    */ {
/*    */   public ShadowPathEffect()
/*    */   {
/* 37 */     setBrushColor(Color.BLACK);
/* 38 */     setRenderInsideShape(false);
/* 39 */     setShouldFillShape(true);
/* 40 */     setOffset(new Point(3, 3));
/*    */   }
/*    */ }


/* Location:              E:\java\java学习\hutubill\lib\all.jar!\org\jdesktop\swingx\painter\effects\ShadowPathEffect.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */
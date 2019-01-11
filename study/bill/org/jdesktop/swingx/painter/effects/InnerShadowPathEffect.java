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
/*    */ public class InnerShadowPathEffect
/*    */   extends AbstractAreaEffect
/*    */ {
/*    */   public InnerShadowPathEffect()
/*    */   {
/* 37 */     setRenderInsideShape(true);
/* 38 */     setBrushColor(Color.BLACK);
/* 39 */     setOffset(new Point(2, 2));
/*    */   }
/*    */ }


/* Location:              E:\java\java学习\hutubill\lib\all.jar!\org\jdesktop\swingx\painter\effects\InnerShadowPathEffect.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */
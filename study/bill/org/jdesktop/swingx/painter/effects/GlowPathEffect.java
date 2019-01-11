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
/*    */ 
/*    */ 
/*    */ public class GlowPathEffect
/*    */   extends AbstractAreaEffect
/*    */ {
/*    */   public GlowPathEffect()
/*    */   {
/* 39 */     setBrushColor(Color.WHITE);
/* 40 */     setBrushSteps(10);
/* 41 */     setEffectWidth(10);
/* 42 */     setShouldFillShape(false);
/* 43 */     setOffset(new Point(0, 0));
/*    */   }
/*    */ }


/* Location:              E:\java\java学习\hutubill\lib\all.jar!\org\jdesktop\swingx\painter\effects\GlowPathEffect.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */
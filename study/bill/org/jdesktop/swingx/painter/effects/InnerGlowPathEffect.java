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
/*    */ public class InnerGlowPathEffect
/*    */   extends AbstractAreaEffect
/*    */ {
/*    */   public InnerGlowPathEffect()
/*    */   {
/* 37 */     setBrushColor(Color.WHITE);
/* 38 */     setBrushSteps(10);
/* 39 */     setEffectWidth(10);
/* 40 */     setShouldFillShape(false);
/* 41 */     setOffset(new Point(0, 0));
/* 42 */     setRenderInsideShape(true);
/*    */   }
/*    */ }


/* Location:              E:\java\java学习\hutubill\lib\all.jar!\org\jdesktop\swingx\painter\effects\InnerGlowPathEffect.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */
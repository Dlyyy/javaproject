/*    */ package org.jdesktop.swingx.decorator;
/*    */ 
/*    */ import java.awt.Color;
/*    */ import java.awt.Component;
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
/*    */ 
/*    */ public class ShadingColorHighlighter
/*    */   extends ColorHighlighter
/*    */ {
/*    */   public ShadingColorHighlighter()
/*    */   {
/* 40 */     this(null);
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   public ShadingColorHighlighter(HighlightPredicate predicate)
/*    */   {
/* 50 */     super(predicate, null, null);
/*    */   }
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
/*    */   protected void applyBackground(Component renderer, ComponentAdapter adapter)
/*    */   {
/* 65 */     if (adapter.isSelected()) {
/* 66 */       return;
/*    */     }
/*    */     
/* 69 */     Color background = getBackground();
/* 70 */     if (background == null) {
/* 71 */       background = renderer.getBackground();
/*    */     }
/*    */     
/*    */ 
/* 75 */     if (background != null) {
/* 76 */       renderer.setBackground(computeBackgroundSeed(background));
/*    */     }
/*    */   }
/*    */   
/*    */   protected Color computeBackgroundSeed(Color seed) {
/* 81 */     return new Color(Math.max((int)(seed.getRed() * 0.95D), 0), Math.max((int)(seed.getGreen() * 0.95D), 0), Math.max((int)(seed.getBlue() * 0.95D), 0));
/*    */   }
/*    */ }


/* Location:              E:\java\java学习\hutubill\lib\all.jar!\org\jdesktop\swingx\decorator\ShadingColorHighlighter.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */
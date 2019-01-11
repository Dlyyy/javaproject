/*     */ package org.jdesktop.swingx.decorator;
/*     */ 
/*     */ import java.awt.Color;
/*     */ import java.awt.Component;
/*     */ import javax.swing.JComponent;
/*     */ import javax.swing.table.DefaultTableCellRenderer;
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
/*     */ public class ResetDTCRColorHighlighter
/*     */   extends ColorHighlighter
/*     */ {
/*     */   public ResetDTCRColorHighlighter()
/*     */   {
/*  55 */     super(null, null);
/*     */   }
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
/*     */   public Component highlight(Component renderer, ComponentAdapter adapter)
/*     */   {
/*  73 */     if ((renderer instanceof DefaultTableCellRenderer)) {
/*  74 */       return super.highlight(renderer, adapter);
/*     */     }
/*  76 */     return renderer;
/*     */   }
/*     */   
/*     */   protected void applyBackground(Component renderer, ComponentAdapter adapter)
/*     */   {
/*  81 */     if (!adapter.isSelected()) {
/*  82 */       Object colorMemory = ((JComponent)renderer).getClientProperty("rendererColorMemory.background");
/*  83 */       if ((colorMemory instanceof ColorMemory)) {
/*  84 */         renderer.setBackground(((ColorMemory)colorMemory).color);
/*     */       } else {
/*  86 */         ((JComponent)renderer).putClientProperty("rendererColorMemory.background", new ColorMemory(renderer.getBackground()));
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   protected void applyForeground(Component renderer, ComponentAdapter adapter)
/*     */   {
/*  93 */     if (!adapter.isSelected()) {
/*  94 */       Object colorMemory = ((JComponent)renderer).getClientProperty("rendererColorMemory.foreground");
/*  95 */       if ((colorMemory instanceof ColorMemory)) {
/*  96 */         renderer.setForeground(((ColorMemory)colorMemory).color);
/*     */       } else
/*  98 */         ((JComponent)renderer).putClientProperty("rendererColorMemory.foreground", new ColorMemory(renderer.getForeground()));
/*     */     }
/*     */   }
/*     */   
/*     */   private static class ColorMemory {
/*     */     Color color;
/*     */     
/* 105 */     public ColorMemory(Color color) { this.color = color; }
/*     */   }
/*     */ }


/* Location:              E:\java\java学习\hutubill\lib\all.jar!\org\jdesktop\swingx\decorator\ResetDTCRColorHighlighter.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */
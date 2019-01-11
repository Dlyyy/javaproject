/*     */ package org.jdesktop.swingx.decorator;
/*     */ 
/*     */ import java.awt.Component;
/*     */ import java.awt.Font;
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
/*     */ public class FontHighlighter
/*     */   extends AbstractHighlighter
/*     */ {
/*     */   private Font font;
/*     */   
/*     */   public FontHighlighter()
/*     */   {
/*  45 */     this((HighlightPredicate)null);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public FontHighlighter(Font font)
/*     */   {
/*  55 */     this(null, font);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public FontHighlighter(HighlightPredicate predicate)
/*     */   {
/*  64 */     this(predicate, null);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public FontHighlighter(HighlightPredicate predicate, Font font)
/*     */   {
/*  74 */     super(predicate);
/*  75 */     this.font = font;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public Font getFont()
/*     */   {
/*  86 */     return this.font;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setFont(Font font)
/*     */   {
/*  97 */     if (areEqual(font, getFont())) return;
/*  98 */     this.font = font;
/*  99 */     fireStateChanged();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected boolean canHighlight(Component component, ComponentAdapter adapter)
/*     */   {
/* 109 */     return this.font != null;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected Component doHighlight(Component component, ComponentAdapter adapter)
/*     */   {
/* 119 */     component.setFont(this.font);
/* 120 */     return component;
/*     */   }
/*     */ }


/* Location:              E:\java\java学习\hutubill\lib\all.jar!\org\jdesktop\swingx\decorator\FontHighlighter.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */
/*     */ package org.jdesktop.swingx.decorator;
/*     */ 
/*     */ import java.awt.Component;
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
/*     */ public class EnabledHighlighter
/*     */   extends AbstractHighlighter
/*     */ {
/*     */   private boolean enabled;
/*     */   
/*     */   public EnabledHighlighter()
/*     */   {
/*  47 */     this(null);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public EnabledHighlighter(boolean enabled)
/*     */   {
/*  57 */     this(null, enabled);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public EnabledHighlighter(HighlightPredicate predicate)
/*     */   {
/*  66 */     this(predicate, false);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public EnabledHighlighter(HighlightPredicate predicate, boolean enabled)
/*     */   {
/*  76 */     super(predicate);
/*  77 */     this.enabled = enabled;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean isEnabled()
/*     */   {
/*  86 */     return this.enabled;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setEnabled(boolean enabled)
/*     */   {
/*  95 */     if (isEnabled() == enabled) return;
/*  96 */     this.enabled = enabled;
/*  97 */     fireStateChanged();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected Component doHighlight(Component renderer, ComponentAdapter adapter)
/*     */   {
/* 107 */     renderer.setEnabled(this.enabled);
/* 108 */     return renderer;
/*     */   }
/*     */ }


/* Location:              E:\java\java学习\hutubill\lib\all.jar!\org\jdesktop\swingx\decorator\EnabledHighlighter.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */
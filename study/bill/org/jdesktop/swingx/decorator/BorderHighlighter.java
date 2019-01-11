/*     */ package org.jdesktop.swingx.decorator;
/*     */ 
/*     */ import java.awt.Component;
/*     */ import javax.swing.BorderFactory;
/*     */ import javax.swing.JComponent;
/*     */ import javax.swing.border.Border;
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
/*     */ public class BorderHighlighter
/*     */   extends AbstractHighlighter
/*     */ {
/*     */   private Border paddingBorder;
/*     */   private boolean inner;
/*     */   private boolean compound;
/*     */   
/*     */   public BorderHighlighter()
/*     */   {
/*  53 */     this((HighlightPredicate)null, null);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public BorderHighlighter(HighlightPredicate predicate)
/*     */   {
/*  65 */     this(predicate, null);
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
/*     */   public BorderHighlighter(Border paddingBorder)
/*     */   {
/*  78 */     this(null, paddingBorder);
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
/*     */   public BorderHighlighter(HighlightPredicate predicate, Border paddingBorder)
/*     */   {
/*  93 */     this(predicate, paddingBorder, true);
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
/*     */   public BorderHighlighter(HighlightPredicate predicate, Border paddingBorder, boolean compound)
/*     */   {
/* 110 */     this(predicate, paddingBorder, compound, false);
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
/*     */   public BorderHighlighter(HighlightPredicate predicate, Border paddingBorder, boolean compound, boolean inner)
/*     */   {
/* 126 */     super(predicate);
/* 127 */     this.paddingBorder = paddingBorder;
/* 128 */     this.compound = compound;
/* 129 */     this.inner = inner;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected Component doHighlight(Component renderer, ComponentAdapter adapter)
/*     */   {
/* 140 */     ((JComponent)renderer).setBorder(compoundBorder(((JComponent)renderer).getBorder()));
/*     */     
/* 142 */     return renderer;
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
/*     */   protected boolean canHighlight(Component component, ComponentAdapter adapter)
/*     */   {
/* 158 */     return (getBorder() != null) && ((component instanceof JComponent));
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
/*     */   public void setCompound(boolean compound)
/*     */   {
/* 172 */     if (isCompound() == compound) return;
/* 173 */     this.compound = compound;
/* 174 */     fireStateChanged();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean isCompound()
/*     */   {
/* 183 */     return this.compound;
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
/*     */   public void setInner(boolean inner)
/*     */   {
/* 198 */     if (isInner() == inner) return;
/* 199 */     this.inner = inner;
/* 200 */     fireStateChanged();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean isInner()
/*     */   {
/* 210 */     return this.inner;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setBorder(Border padding)
/*     */   {
/* 221 */     if (areEqual(padding, getBorder())) return;
/* 222 */     this.paddingBorder = padding;
/* 223 */     fireStateChanged();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public Border getBorder()
/*     */   {
/* 233 */     return this.paddingBorder;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private Border compoundBorder(Border border)
/*     */   {
/* 243 */     if ((this.compound) && 
/* 244 */       (border != null)) {
/* 245 */       if (this.inner) {
/* 246 */         return BorderFactory.createCompoundBorder(border, this.paddingBorder);
/*     */       }
/*     */       
/* 249 */       return BorderFactory.createCompoundBorder(this.paddingBorder, border);
/*     */     }
/*     */     
/*     */ 
/* 253 */     return this.paddingBorder;
/*     */   }
/*     */ }


/* Location:              E:\java\java学习\hutubill\lib\all.jar!\org\jdesktop\swingx\decorator\BorderHighlighter.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */
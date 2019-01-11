/*     */ package org.jdesktop.swingx.decorator;
/*     */ 
/*     */ import java.awt.Component;
/*     */ import javax.swing.AbstractButton;
/*     */ import javax.swing.JLabel;
/*     */ import javax.swing.JTextField;
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
/*     */ public class AlignmentHighlighter
/*     */   extends AbstractHighlighter
/*     */ {
/*     */   private static final int defaultAlignment = 10;
/*     */   private int alignment;
/*     */   
/*     */   public AlignmentHighlighter()
/*     */   {
/*  47 */     this(10);
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
/*     */   public AlignmentHighlighter(int alignment)
/*     */   {
/*  60 */     this(null, alignment);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public AlignmentHighlighter(HighlightPredicate predicate)
/*     */   {
/*  71 */     this(predicate, 10);
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
/*     */   public AlignmentHighlighter(HighlightPredicate predicate, int alignment)
/*     */   {
/*  84 */     super(predicate);
/*  85 */     this.alignment = checkHorizontalAlignment(alignment);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public int getHorizontalAlignment()
/*     */   {
/*  93 */     return this.alignment;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setHorizontalAlignment(int alignment)
/*     */   {
/* 105 */     if (getHorizontalAlignment() == alignment) return;
/* 106 */     this.alignment = checkHorizontalAlignment(alignment);
/* 107 */     fireStateChanged();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private int checkHorizontalAlignment(int alignment)
/*     */   {
/* 119 */     if ((alignment == 2) || (alignment == 0) || (alignment == 4) || (alignment == 10) || (alignment == 11))
/*     */     {
/*     */ 
/*     */ 
/*     */ 
/* 124 */       return alignment;
/*     */     }
/*     */     
/* 127 */     throw new IllegalArgumentException("invalid horizontal alignment, expected one of 2 / 0 / 4 / 10 / 11 but was: " + alignment);
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
/*     */   protected Component doHighlight(Component renderer, ComponentAdapter adapter)
/*     */   {
/* 141 */     if ((renderer instanceof JLabel)) {
/* 142 */       ((JLabel)renderer).setHorizontalAlignment(getHorizontalAlignment());
/* 143 */     } else if ((renderer instanceof AbstractButton)) {
/* 144 */       ((AbstractButton)renderer).setHorizontalAlignment(getHorizontalAlignment());
/*     */     } else {
/* 146 */       ((JTextField)renderer).setHorizontalAlignment(getHorizontalAlignment());
/*     */     }
/* 148 */     return renderer;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected boolean canHighlight(Component component, ComponentAdapter adapter)
/*     */   {
/* 159 */     return ((component instanceof JLabel)) || ((component instanceof AbstractButton)) || ((component instanceof JTextField));
/*     */   }
/*     */ }


/* Location:              E:\java\java学习\hutubill\lib\all.jar!\org\jdesktop\swingx\decorator\AlignmentHighlighter.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */
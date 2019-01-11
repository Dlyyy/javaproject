/*     */ package org.jdesktop.swingx.decorator;
/*     */ 
/*     */ import java.awt.Component;
/*     */ import java.util.regex.Matcher;
/*     */ import java.util.regex.Pattern;
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
/*     */ public class PatternPredicate
/*     */   implements HighlightPredicate
/*     */ {
/*     */   public static final int ALL = -1;
/*     */   private int highlightColumn;
/*     */   private int testColumn;
/*     */   private Pattern pattern;
/*     */   
/*     */   public PatternPredicate(Pattern pattern, int testColumn)
/*     */   {
/*  51 */     this(pattern, testColumn, -1);
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
/*     */   public PatternPredicate(Pattern pattern, int testColumn, int decorateColumn)
/*     */   {
/*  68 */     this.pattern = pattern;
/*  69 */     this.testColumn = testColumn;
/*  70 */     this.highlightColumn = decorateColumn;
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
/*     */   public PatternPredicate(String regex, int testColumn, int decorateColumn)
/*     */   {
/*  87 */     this(Pattern.compile(regex), testColumn, decorateColumn);
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
/*     */   public PatternPredicate(String regex, int testColumn)
/*     */   {
/* 102 */     this(Pattern.compile(regex), testColumn);
/*     */   }
/*     */   
/*     */   public boolean isHighlighted(Component renderer, ComponentAdapter adapter) {
/* 106 */     if (isHighlightCandidate(renderer, adapter)) {
/* 107 */       return test(renderer, adapter);
/*     */     }
/* 109 */     return false;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private boolean test(Component renderer, ComponentAdapter adapter)
/*     */   {
/* 121 */     if (!adapter.isTestable(this.testColumn))
/* 122 */       return false;
/* 123 */     String value = adapter.getString(this.testColumn);
/*     */     
/* 125 */     if ((value == null) || (value.length() == 0)) {
/* 126 */       return false;
/*     */     }
/* 128 */     return this.pattern.matcher(value).find();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private boolean isHighlightCandidate(Component renderer, ComponentAdapter adapter)
/*     */   {
/* 139 */     return (this.pattern != null) && ((this.highlightColumn < 0) || (this.highlightColumn == adapter.convertColumnIndexToModel(adapter.column)));
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public int getHighlightColumn()
/*     */   {
/* 149 */     return this.highlightColumn;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public Pattern getPattern()
/*     */   {
/* 157 */     return this.pattern;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public int getTestColumn()
/*     */   {
/* 165 */     return this.testColumn;
/*     */   }
/*     */ }


/* Location:              E:\java\java学习\hutubill\lib\all.jar!\org\jdesktop\swingx\decorator\PatternPredicate.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */
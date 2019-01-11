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
/*     */ 
/*     */ 
/*     */ 
/*     */ public class SearchPredicate
/*     */   implements HighlightPredicate
/*     */ {
/*     */   public static final int ALL = -1;
/*     */   public static final String MATCH_ALL = ".*";
/*     */   private int highlightColumn;
/*     */   private int highlightRow;
/*     */   private Pattern pattern;
/*     */   
/*     */   public SearchPredicate(Pattern pattern)
/*     */   {
/*  55 */     this(pattern, -1, -1);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public SearchPredicate(Pattern pattern, int column)
/*     */   {
/*  66 */     this(pattern, -1, column);
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
/*     */ 
/*     */ 
/*     */ 
/*     */   public SearchPredicate(Pattern pattern, int row, int column)
/*     */   {
/*  87 */     this.pattern = pattern;
/*  88 */     this.highlightColumn = column;
/*  89 */     this.highlightRow = row;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public SearchPredicate(String regex)
/*     */   {
/* 100 */     this(regex, -1, -1);
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
/*     */   public SearchPredicate(String regex, int column)
/*     */   {
/* 114 */     this(regex, -1, column);
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public SearchPredicate(String regex, int row, int column)
/*     */   {
/* 137 */     this((regex != null) && (regex.length() > 0) ? Pattern.compile(regex) : null, row, column);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public int getHighlightColumn()
/*     */   {
/* 146 */     return this.highlightColumn;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public int getHighlightRow()
/*     */   {
/* 154 */     return this.highlightRow;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public Pattern getPattern()
/*     */   {
/* 162 */     return this.pattern;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean isHighlighted(Component renderer, ComponentAdapter adapter)
/*     */   {
/* 170 */     if (isHighlightCandidate(renderer, adapter)) {
/* 171 */       return test(renderer, adapter);
/*     */     }
/* 173 */     return false;
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
/*     */   private boolean test(Component renderer, ComponentAdapter adapter)
/*     */   {
/* 187 */     int columnToTest = adapter.convertColumnIndexToModel(adapter.column);
/* 188 */     String value = adapter.getString(columnToTest);
/*     */     
/* 190 */     if ((value == null) || (value.length() == 0)) {
/* 191 */       return false;
/*     */     }
/* 193 */     return this.pattern.matcher(value).find();
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
/* 204 */     if (!isEnabled()) return false;
/* 205 */     if ((this.highlightRow >= 0) && (adapter.row != this.highlightRow)) {
/* 206 */       return false;
/*     */     }
/* 208 */     return (this.highlightColumn < 0) || (this.highlightColumn == adapter.convertColumnIndexToModel(adapter.column));
/*     */   }
/*     */   
/*     */ 
/*     */   private boolean isEnabled()
/*     */   {
/* 214 */     Pattern pattern = getPattern();
/* 215 */     if ((pattern == null) || (".*".equals(pattern.pattern()))) {
/* 216 */       return false;
/*     */     }
/* 218 */     return true;
/*     */   }
/*     */ }


/* Location:              E:\java\java学习\hutubill\lib\all.jar!\org\jdesktop\swingx\decorator\SearchPredicate.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */
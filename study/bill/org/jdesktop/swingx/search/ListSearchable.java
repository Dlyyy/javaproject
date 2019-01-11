/*     */ package org.jdesktop.swingx.search;
/*     */ 
/*     */ import java.util.regex.Matcher;
/*     */ import java.util.regex.Pattern;
/*     */ import org.jdesktop.swingx.JXList;
/*     */ import org.jdesktop.swingx.decorator.AbstractHighlighter;
/*     */ import org.jdesktop.swingx.decorator.Highlighter;
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
/*     */ public class ListSearchable
/*     */   extends AbstractSearchable
/*     */ {
/*     */   protected JXList list;
/*     */   
/*     */   public ListSearchable(JXList list)
/*     */   {
/*  35 */     this.list = list;
/*     */   }
/*     */   
/*     */   protected void findMatchAndUpdateState(Pattern pattern, int startRow, boolean backwards)
/*     */   {
/*  40 */     AbstractSearchable.SearchResult searchResult = null;
/*  41 */     if (backwards) {
/*  42 */       for (int index = startRow; (index >= 0) && (searchResult == null); index--) {
/*  43 */         searchResult = findMatchAt(pattern, index);
/*     */       }
/*     */     } else {
/*  46 */       for (int index = startRow; (index < getSize()) && (searchResult == null); index++) {
/*  47 */         searchResult = findMatchAt(pattern, index);
/*     */       }
/*     */     }
/*  50 */     updateState(searchResult);
/*     */   }
/*     */   
/*     */ 
/*     */   protected AbstractSearchable.SearchResult findExtendedMatch(Pattern pattern, int row)
/*     */   {
/*  56 */     return findMatchAt(pattern, row);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected AbstractSearchable.SearchResult findMatchAt(Pattern pattern, int row)
/*     */   {
/*  68 */     String text = this.list.getStringAt(row);
/*  69 */     if ((text != null) && (text.length() > 0)) {
/*  70 */       Matcher matcher = pattern.matcher(text);
/*  71 */       if (matcher.find()) {
/*  72 */         return createSearchResult(matcher, row, 0);
/*     */       }
/*     */     }
/*  75 */     return null;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   protected int getSize()
/*     */   {
/*  83 */     return this.list.getElementCount();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public JXList getTarget()
/*     */   {
/*  92 */     return this.list;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   protected void moveMatchMarker()
/*     */   {
/* 100 */     if (markByHighlighter()) {
/* 101 */       moveMatchByHighlighter();
/*     */     } else {
/* 103 */       moveMatchBySelection();
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   protected void moveMatchBySelection()
/*     */   {
/* 112 */     if (!hasMatch()) {
/* 113 */       return;
/*     */     }
/* 115 */     this.list.setSelectedIndex(this.lastSearchResult.foundRow);
/* 116 */     this.list.ensureIndexIsVisible(this.lastSearchResult.foundRow);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected void moveMatchByHighlighter()
/*     */   {
/* 126 */     AbstractHighlighter searchHL = getConfiguredMatchHighlighter();
/*     */     
/* 128 */     if (!hasMatch()) {
/* 129 */       return;
/*     */     }
/* 131 */     ensureInsertedSearchHighlighters(searchHL);
/* 132 */     this.list.ensureIndexIsVisible(this.lastSearchResult.foundRow);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected void removeHighlighter(Highlighter searchHighlighter)
/*     */   {
/* 143 */     this.list.removeHighlighter(searchHighlighter);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   protected Highlighter[] getHighlighters()
/*     */   {
/* 151 */     return this.list.getHighlighters();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   protected void addHighlighter(Highlighter highlighter)
/*     */   {
/* 159 */     this.list.addHighlighter(highlighter);
/*     */   }
/*     */ }


/* Location:              E:\java\java学习\hutubill\lib\all.jar!\org\jdesktop\swingx\search\ListSearchable.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */
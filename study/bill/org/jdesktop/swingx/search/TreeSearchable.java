/*     */ package org.jdesktop.swingx.search;
/*     */ 
/*     */ import java.util.regex.Matcher;
/*     */ import java.util.regex.Pattern;
/*     */ import org.jdesktop.swingx.JXTree;
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
/*     */ public class TreeSearchable
/*     */   extends AbstractSearchable
/*     */ {
/*     */   protected JXTree tree;
/*     */   
/*     */   public TreeSearchable(JXTree tree)
/*     */   {
/*  46 */     this.tree = tree;
/*     */   }
/*     */   
/*     */ 
/*     */   protected void findMatchAndUpdateState(Pattern pattern, int startRow, boolean backwards)
/*     */   {
/*  52 */     AbstractSearchable.SearchResult searchResult = null;
/*  53 */     if (backwards) {
/*  54 */       for (int index = startRow; (index >= 0) && (searchResult == null); index--) {
/*  55 */         searchResult = findMatchAt(pattern, index);
/*     */       }
/*     */     } else {
/*  58 */       for (int index = startRow; 
/*  59 */           (index < getSize()) && (searchResult == null); index++) {
/*  60 */         searchResult = findMatchAt(pattern, index);
/*     */       }
/*     */     }
/*  63 */     updateState(searchResult);
/*     */   }
/*     */   
/*     */ 
/*     */   protected AbstractSearchable.SearchResult findExtendedMatch(Pattern pattern, int row)
/*     */   {
/*  69 */     return findMatchAt(pattern, row);
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
/*     */   protected AbstractSearchable.SearchResult findMatchAt(Pattern pattern, int row)
/*     */   {
/*  85 */     String text = this.tree.getStringAt(row);
/*  86 */     if ((text != null) && (text.length() > 0)) {
/*  87 */       Matcher matcher = pattern.matcher(text);
/*  88 */       if (matcher.find()) {
/*  89 */         return createSearchResult(matcher, row, 0);
/*     */       }
/*     */     }
/*  92 */     return null;
/*     */   }
/*     */   
/*     */   protected int getSize()
/*     */   {
/*  97 */     return this.tree.getRowCount();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public JXTree getTarget()
/*     */   {
/* 105 */     return this.tree;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected void moveMatchMarker()
/*     */   {
/* 114 */     if (markByHighlighter()) {
/* 115 */       moveMatchByHighlighter();
/*     */     } else {
/* 117 */       moveMatchBySelection();
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */   protected void moveMatchBySelection()
/*     */   {
/* 124 */     if (!hasMatch()) {
/* 125 */       return;
/*     */     }
/* 127 */     this.tree.setSelectionRow(this.lastSearchResult.foundRow);
/* 128 */     this.tree.scrollRowToVisible(this.lastSearchResult.foundRow);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected void moveMatchByHighlighter()
/*     */   {
/* 138 */     AbstractHighlighter searchHL = getConfiguredMatchHighlighter();
/*     */     
/* 140 */     if (!hasMatch()) {
/* 141 */       return;
/*     */     }
/* 143 */     ensureInsertedSearchHighlighters(searchHL);
/* 144 */     this.tree.scrollRowToVisible(this.lastSearchResult.foundRow);
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
/* 155 */     this.tree.removeHighlighter(searchHighlighter);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   protected Highlighter[] getHighlighters()
/*     */   {
/* 163 */     return this.tree.getHighlighters();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   protected void addHighlighter(Highlighter highlighter)
/*     */   {
/* 171 */     this.tree.addHighlighter(highlighter);
/*     */   }
/*     */ }


/* Location:              E:\java\java学习\hutubill\lib\all.jar!\org\jdesktop\swingx\search\TreeSearchable.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */
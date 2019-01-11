/*     */ package org.jdesktop.swingx.search;
/*     */ 
/*     */ import java.awt.Color;
/*     */ import java.util.regex.MatchResult;
/*     */ import java.util.regex.Matcher;
/*     */ import java.util.regex.Pattern;
/*     */ import javax.swing.JComponent;
/*     */ import org.jdesktop.swingx.decorator.AbstractHighlighter;
/*     */ import org.jdesktop.swingx.decorator.ColorHighlighter;
/*     */ import org.jdesktop.swingx.decorator.HighlightPredicate;
/*     */ import org.jdesktop.swingx.decorator.Highlighter;
/*     */ import org.jdesktop.swingx.decorator.SearchPredicate;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public abstract class AbstractSearchable
/*     */   implements Searchable
/*     */ {
/*     */   protected final SearchResult lastSearchResult;
/*     */   private AbstractHighlighter matchHighlighter;
/*     */   public static final String MATCH_HIGHLIGHTER = "match.highlighter";
/*     */   
/*     */   public AbstractSearchable()
/*     */   {
/*  52 */     this.lastSearchResult = new SearchResult();
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
/*     */   public int search(String searchString)
/*     */   {
/*  70 */     return search(searchString, -1);
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
/*     */   public int search(String searchString, int startIndex)
/*     */   {
/*  85 */     return search(searchString, startIndex, false);
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
/*     */   public int search(String searchString, int startIndex, boolean backward)
/*     */   {
/* 102 */     Pattern pattern = null;
/* 103 */     if (!isEmpty(searchString)) {
/* 104 */       pattern = Pattern.compile(searchString, 0);
/*     */     }
/* 106 */     return search(pattern, startIndex, backward);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public int search(Pattern pattern)
/*     */   {
/* 118 */     return search(pattern, -1);
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
/*     */   public int search(Pattern pattern, int startIndex)
/*     */   {
/* 132 */     return search(pattern, startIndex, false);
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
/*     */   public int search(Pattern pattern, int startIndex, boolean backwards)
/*     */   {
/* 151 */     int matchingRow = doSearch(pattern, startIndex, backwards);
/* 152 */     moveMatchMarker();
/* 153 */     return matchingRow;
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
/*     */   protected int doSearch(Pattern pattern, int startIndex, boolean backwards)
/*     */   {
/* 172 */     if (isTrivialNoMatch(pattern, startIndex)) {
/* 173 */       updateState(null);
/* 174 */       return this.lastSearchResult.foundRow;
/*     */     }
/*     */     int startRow;
/*     */     int startRow;
/* 178 */     if (isEqualStartIndex(startIndex)) {
/* 179 */       if (!isEqualPattern(pattern)) {
/* 180 */         SearchResult searchResult = findExtendedMatch(pattern, startIndex);
/* 181 */         if (searchResult != null) {
/* 182 */           updateState(searchResult);
/* 183 */           return this.lastSearchResult.foundRow;
/*     */         }
/*     */       }
/*     */       
/*     */ 
/*     */ 
/* 189 */       startRow = moveStartPosition(startIndex, backwards);
/*     */ 
/*     */     }
/*     */     else
/*     */     {
/* 194 */       startRow = adjustStartPosition(startIndex, backwards);
/*     */     }
/* 196 */     findMatchAndUpdateState(pattern, startRow, backwards);
/* 197 */     return this.lastSearchResult.foundRow;
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
/*     */   protected abstract void findMatchAndUpdateState(Pattern paramPattern, int paramInt, boolean paramBoolean);
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected boolean isTrivialNoMatch(Pattern pattern, int startIndex)
/*     */   {
/* 224 */     return (pattern == null) || (startIndex >= getSize());
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
/*     */   protected int adjustStartPosition(int startIndex, boolean backwards)
/*     */   {
/* 238 */     if (startIndex < 0) {
/* 239 */       if (backwards) {
/* 240 */         return getSize() - 1;
/*     */       }
/* 242 */       return 0;
/*     */     }
/*     */     
/* 245 */     return startIndex;
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
/*     */   protected int moveStartPosition(int startIndex, boolean backwards)
/*     */   {
/* 263 */     if (backwards) {
/* 264 */       startIndex--;
/*     */     } else {
/* 266 */       startIndex++;
/*     */     }
/* 268 */     return startIndex;
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
/*     */   protected boolean isEqualPattern(Pattern pattern)
/*     */   {
/* 283 */     return pattern.pattern().equals(this.lastSearchResult.getRegEx());
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected boolean isEqualStartIndex(int startIndex)
/*     */   {
/* 295 */     return (isValidIndex(startIndex)) && (startIndex == this.lastSearchResult.foundRow);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected boolean isEmpty(String searchString)
/*     */   {
/* 307 */     return (searchString == null) || (searchString.length() == 0);
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
/*     */   protected abstract SearchResult findExtendedMatch(Pattern paramPattern, int paramInt);
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected SearchResult createSearchResult(Matcher matcher, int row, int column)
/*     */   {
/* 331 */     return new SearchResult(matcher.pattern(), matcher.toMatchResult(), row, column);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected boolean isValidIndex(int index)
/*     */   {
/* 342 */     return (index >= 0) && (index < getSize());
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected abstract int getSize();
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected void updateState(SearchResult searchResult)
/*     */   {
/* 359 */     this.lastSearchResult.updateFrom(searchResult);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected abstract void moveMatchMarker();
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public abstract JComponent getTarget();
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected abstract void removeHighlighter(Highlighter paramHighlighter);
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected abstract Highlighter[] getHighlighters();
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected abstract void addHighlighter(Highlighter paramHighlighter);
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected void ensureInsertedSearchHighlighters(Highlighter highlighter)
/*     */   {
/* 402 */     if (!isInPipeline(highlighter)) {
/* 403 */       addHighlighter(highlighter);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private boolean isInPipeline(Highlighter searchHighlighter)
/*     */   {
/* 416 */     Highlighter[] inPipeline = getHighlighters();
/* 417 */     if ((inPipeline.length > 0) && (searchHighlighter.equals(inPipeline[(inPipeline.length - 1)])))
/*     */     {
/* 419 */       return true;
/*     */     }
/* 421 */     removeHighlighter(searchHighlighter);
/* 422 */     return false;
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
/*     */   protected int convertColumnIndexToModel(int viewColumn)
/*     */   {
/* 437 */     return viewColumn;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private boolean hasMatch(SearchResult result)
/*     */   {
/* 447 */     boolean noMatch = (result.getFoundRow() < 0) || (result.getFoundColumn() < 0);
/* 448 */     return !noMatch;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected boolean hasMatch()
/*     */   {
/* 458 */     return hasMatch(this.lastSearchResult);
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
/*     */   protected boolean markByHighlighter()
/*     */   {
/* 479 */     return Boolean.TRUE.equals(getTarget().getClientProperty("match.highlighter"));
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setMatchHighlighter(AbstractHighlighter hl)
/*     */   {
/* 490 */     removeHighlighter(this.matchHighlighter);
/* 491 */     this.matchHighlighter = hl;
/* 492 */     if (markByHighlighter()) {
/* 493 */       moveMatchMarker();
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected AbstractHighlighter getMatchHighlighter()
/*     */   {
/* 503 */     if (this.matchHighlighter == null) {
/* 504 */       this.matchHighlighter = createMatchHighlighter();
/*     */     }
/* 506 */     return this.matchHighlighter;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected AbstractHighlighter createMatchHighlighter()
/*     */   {
/* 515 */     return new ColorHighlighter(HighlightPredicate.NEVER, Color.YELLOW.brighter(), null, Color.YELLOW.brighter(), null);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected AbstractHighlighter getConfiguredMatchHighlighter()
/*     */   {
/* 527 */     AbstractHighlighter searchHL = getMatchHighlighter();
/* 528 */     searchHL.setHighlightPredicate(createMatchPredicate());
/* 529 */     return searchHL;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected HighlightPredicate createMatchPredicate()
/*     */   {
/* 539 */     return hasMatch() ? new SearchPredicate(this.lastSearchResult.pattern, this.lastSearchResult.foundRow, convertColumnIndexToModel(this.lastSearchResult.foundColumn)) : HighlightPredicate.NEVER;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public static class SearchResult
/*     */   {
/*     */     int foundRow;
/*     */     
/*     */ 
/*     */     int foundColumn;
/*     */     
/*     */ 
/*     */     MatchResult matchResult;
/*     */     
/*     */ 
/*     */     Pattern pattern;
/*     */     
/*     */ 
/*     */ 
/*     */     public SearchResult()
/*     */     {
/* 561 */       reset();
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     public SearchResult(Pattern ex, MatchResult result, int row, int column)
/*     */     {
/* 573 */       this.pattern = ex;
/* 574 */       this.matchResult = result;
/* 575 */       this.foundRow = row;
/* 576 */       this.foundColumn = column;
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     public void updateFrom(SearchResult searchResult)
/*     */     {
/* 586 */       if (searchResult == null) {
/* 587 */         reset();
/* 588 */         return;
/*     */       }
/* 590 */       this.foundRow = searchResult.foundRow;
/* 591 */       this.foundColumn = searchResult.foundColumn;
/* 592 */       this.matchResult = searchResult.matchResult;
/* 593 */       this.pattern = searchResult.pattern;
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     public String getRegEx()
/*     */     {
/* 602 */       return this.pattern != null ? this.pattern.pattern() : null;
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */     public void reset()
/*     */     {
/* 609 */       this.foundRow = -1;
/* 610 */       this.foundColumn = -1;
/* 611 */       this.matchResult = null;
/* 612 */       this.pattern = null;
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */     public void resetFoundColumn()
/*     */     {
/* 619 */       this.foundColumn = -1;
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     public int getFoundColumn()
/*     */     {
/* 628 */       return this.foundColumn;
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     public int getFoundRow()
/*     */     {
/* 637 */       return this.foundRow;
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     public MatchResult getMatchResult()
/*     */     {
/* 646 */       return this.matchResult;
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     public Pattern getPattern()
/*     */     {
/* 655 */       return this.pattern;
/*     */     }
/*     */   }
/*     */ }


/* Location:              E:\java\java学习\hutubill\lib\all.jar!\org\jdesktop\swingx\search\AbstractSearchable.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */
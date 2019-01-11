/*     */ package org.jdesktop.swingx.search;
/*     */ 
/*     */ import java.awt.Rectangle;
/*     */ import java.util.regex.Matcher;
/*     */ import java.util.regex.Pattern;
/*     */ import org.jdesktop.swingx.JXTable;
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
/*     */ public class TableSearchable
/*     */   extends AbstractSearchable
/*     */ {
/*     */   protected JXTable table;
/*     */   
/*     */   public TableSearchable(JXTable table)
/*     */   {
/*  47 */     this.table = table;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected void findMatchAndUpdateState(Pattern pattern, int startRow, boolean backwards)
/*     */   {
/*  59 */     AbstractSearchable.SearchResult matchRow = null;
/*  60 */     if (backwards)
/*     */     {
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*  66 */       for (int r = startRow; (r >= -1) && (matchRow == null); r--) {
/*  67 */         matchRow = findMatchBackwardsInRow(pattern, r);
/*  68 */         updateState(matchRow);
/*     */       }
/*     */     } else {
/*  71 */       for (int r = startRow; (r <= getSize()) && (matchRow == null); r++) {
/*  72 */         matchRow = findMatchForwardInRow(pattern, r);
/*  73 */         updateState(matchRow);
/*     */       }
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected AbstractSearchable.SearchResult findExtendedMatch(Pattern pattern, int row)
/*     */   {
/*  92 */     return findMatchAt(pattern, row, this.lastSearchResult.foundColumn);
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
/*     */   private AbstractSearchable.SearchResult findMatchForwardInRow(Pattern pattern, int row)
/*     */   {
/* 107 */     int startColumn = this.lastSearchResult.foundColumn < 0 ? 0 : this.lastSearchResult.foundColumn;
/*     */     
/* 109 */     if (isValidIndex(row)) {
/* 110 */       for (int column = startColumn; column < this.table.getColumnCount(); column++) {
/* 111 */         AbstractSearchable.SearchResult result = findMatchAt(pattern, row, column);
/* 112 */         if (result != null)
/* 113 */           return result;
/*     */       }
/*     */     }
/* 116 */     return null;
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
/*     */   private AbstractSearchable.SearchResult findMatchBackwardsInRow(Pattern pattern, int row)
/*     */   {
/* 131 */     int startColumn = this.lastSearchResult.foundColumn < 0 ? this.table.getColumnCount() - 1 : this.lastSearchResult.foundColumn;
/*     */     
/* 133 */     if (isValidIndex(row)) {
/* 134 */       for (int column = startColumn; column >= 0; column--) {
/* 135 */         AbstractSearchable.SearchResult result = findMatchAt(pattern, row, column);
/* 136 */         if (result != null)
/* 137 */           return result;
/*     */       }
/*     */     }
/* 140 */     return null;
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
/*     */   protected AbstractSearchable.SearchResult findMatchAt(Pattern pattern, int row, int column)
/*     */   {
/* 153 */     String text = this.table.getStringAt(row, column);
/* 154 */     if ((text != null) && (text.length() > 0)) {
/* 155 */       Matcher matcher = pattern.matcher(text);
/* 156 */       if (matcher.find()) {
/* 157 */         return createSearchResult(matcher, row, column);
/*     */       }
/*     */     }
/* 160 */     return null;
/*     */   }
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
/* 172 */     this.lastSearchResult.foundColumn = -1;
/* 173 */     return super.adjustStartPosition(startIndex, backwards);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected int moveStartPosition(int startRow, boolean backwards)
/*     */   {
/* 184 */     if (backwards) {
/* 185 */       this.lastSearchResult.foundColumn -= 1;
/* 186 */       if (this.lastSearchResult.foundColumn < 0) {
/* 187 */         startRow--;
/*     */       }
/*     */     } else {
/* 190 */       this.lastSearchResult.foundColumn += 1;
/* 191 */       if (this.lastSearchResult.foundColumn >= this.table.getColumnCount()) {
/* 192 */         this.lastSearchResult.foundColumn = -1;
/* 193 */         startRow++;
/*     */       }
/*     */     }
/* 196 */     return startRow;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected boolean isEqualStartIndex(int startIndex)
/*     */   {
/* 207 */     return (super.isEqualStartIndex(startIndex)) && (isValidColumn(this.lastSearchResult.foundColumn));
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private boolean isValidColumn(int column)
/*     */   {
/* 218 */     return (column >= 0) && (column < this.table.getColumnCount());
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   protected int getSize()
/*     */   {
/* 226 */     return this.table.getRowCount();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public JXTable getTarget()
/*     */   {
/* 234 */     return this.table;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected void moveMatchByHighlighter()
/*     */   {
/* 245 */     AbstractHighlighter searchHL = getConfiguredMatchHighlighter();
/*     */     
/* 247 */     if (!hasMatch()) {
/* 248 */       return;
/*     */     }
/* 250 */     ensureInsertedSearchHighlighters(searchHL);
/* 251 */     this.table.scrollCellToVisible(this.lastSearchResult.foundRow, this.lastSearchResult.foundColumn);
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
/*     */   protected int convertColumnIndexToModel(int viewColumn)
/*     */   {
/* 271 */     return getTarget().convertColumnIndexToModel(viewColumn);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected void moveMatchBySelection()
/*     */   {
/* 280 */     if (!hasMatch()) {
/* 281 */       return;
/*     */     }
/* 283 */     int row = this.lastSearchResult.foundRow;
/* 284 */     int column = this.lastSearchResult.foundColumn;
/* 285 */     this.table.changeSelection(row, column, false, false);
/* 286 */     if (!this.table.getAutoscrolls())
/*     */     {
/* 288 */       Rectangle cellRect = this.table.getCellRect(row, column, true);
/* 289 */       if (cellRect != null) {
/* 290 */         this.table.scrollRectToVisible(cellRect);
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected void moveMatchMarker()
/*     */   {
/* 301 */     if (markByHighlighter()) {
/* 302 */       moveMatchByHighlighter();
/*     */     } else {
/* 304 */       moveMatchBySelection();
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected void removeHighlighter(Highlighter searchHighlighter)
/*     */   {
/* 314 */     this.table.removeHighlighter(searchHighlighter);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected Highlighter[] getHighlighters()
/*     */   {
/* 323 */     return this.table.getHighlighters();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected void addHighlighter(Highlighter highlighter)
/*     */   {
/* 332 */     this.table.addHighlighter(highlighter);
/*     */   }
/*     */ }


/* Location:              E:\java\java学习\hutubill\lib\all.jar!\org\jdesktop\swingx\search\TableSearchable.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */
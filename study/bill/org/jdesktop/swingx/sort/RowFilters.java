/*     */ package org.jdesktop.swingx.sort;
/*     */ 
/*     */ import java.util.regex.Matcher;
/*     */ import java.util.regex.Pattern;
/*     */ import javax.swing.RowFilter;
/*     */ import javax.swing.RowFilter.Entry;
/*     */ import org.jdesktop.swingx.util.Contract;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class RowFilters
/*     */ {
/*     */   public static <M, I> RowFilter<M, I> regexFilter(String regex, int... indices)
/*     */   {
/*  77 */     return regexFilter(0, regex, indices);
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static <M, I> RowFilter<M, I> regexFilter(int matchFlags, String regex, int... indices)
/*     */   {
/* 122 */     return regexFilter(Pattern.compile(regex, matchFlags), indices);
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
/*     */   public static <M, I> RowFilter<M, I> regexFilter(Pattern pattern, int... indices)
/*     */   {
/* 139 */     return new RegexFilter(pattern, indices);
/*     */   }
/*     */   
/*     */   public static abstract class GeneralFilter
/*     */     extends RowFilter<Object, Object>
/*     */   {
/*     */     private int[] columns;
/*     */     
/*     */     protected GeneralFilter(int... columns)
/*     */     {
/* 149 */       checkIndices(columns);
/* 150 */       this.columns = columns;
/*     */     }
/*     */     
/*     */     public boolean include(RowFilter.Entry<? extends Object, ? extends Object> value)
/*     */     {
/* 155 */       int count = value.getValueCount();
/* 156 */       if (this.columns.length > 0) {
/* 157 */         for (int i = this.columns.length - 1; i >= 0; i--) {
/* 158 */           int index = this.columns[i];
/* 159 */           if ((index < count) && 
/* 160 */             (include(value, index))) {
/* 161 */             return true;
/*     */           }
/*     */         }
/*     */       }
/*     */       else {
/*     */         do {
/* 167 */           count--; if (count < 0) break;
/* 168 */         } while (!include(value, count));
/* 169 */         return true;
/*     */       }
/*     */       
/*     */ 
/* 173 */       return false;
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */     protected abstract boolean include(RowFilter.Entry<? extends Object, ? extends Object> paramEntry, int paramInt);
/*     */     
/*     */ 
/*     */     protected void checkIndices(int[] columns)
/*     */     {
/* 183 */       for (int i = columns.length - 1; i >= 0; i--) {
/* 184 */         if (columns[i] < 0) {
/* 185 */           throw new IllegalArgumentException("Index must be >= 0");
/*     */         }
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   private static class RegexFilter
/*     */     extends RowFilters.GeneralFilter
/*     */   {
/*     */     private Matcher matcher;
/*     */     
/*     */     RegexFilter(Pattern regex, int[] columns)
/*     */     {
/* 198 */       super();
/* 199 */       if (regex == null)
/*     */       {
/* 201 */         Contract.asNotNull(regex, "Pattern must be non-null");
/*     */       }
/*     */       
/* 204 */       this.matcher = regex.matcher("");
/*     */     }
/*     */     
/*     */ 
/*     */     protected boolean include(RowFilter.Entry<? extends Object, ? extends Object> value, int index)
/*     */     {
/* 210 */       this.matcher.reset(value.getStringValue(index));
/* 211 */       return this.matcher.find();
/*     */     }
/*     */   }
/*     */ }


/* Location:              E:\java\java学习\hutubill\lib\all.jar!\org\jdesktop\swingx\sort\RowFilters.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */
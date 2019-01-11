/*     */ package com.mysql.jdbc;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class RowDataStatic
/*     */   implements RowData
/*     */ {
/*     */   private int index;
/*     */   ResultSet owner;
/*     */   private List rows;
/*     */   
/*     */   public RowDataStatic(ArrayList rows)
/*     */   {
/*  50 */     this.index = -1;
/*  51 */     this.rows = rows;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void addRow(byte[][] row)
/*     */   {
/*  61 */     this.rows.add(row);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public void afterLast()
/*     */   {
/*  68 */     this.index = this.rows.size();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public void beforeFirst()
/*     */   {
/*  75 */     this.index = -1;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public void beforeLast()
/*     */   {
/*  82 */     this.index = (this.rows.size() - 2);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void close() {}
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public Object[] getAt(int atIndex)
/*     */   {
/* 100 */     if ((atIndex < 0) || (atIndex >= this.rows.size())) {
/* 101 */       return null;
/*     */     }
/*     */     
/* 104 */     return (Object[])this.rows.get(atIndex);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public int getCurrentRowNumber()
/*     */   {
/* 113 */     return this.index;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public ResultSet getOwner()
/*     */   {
/* 120 */     return this.owner;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean hasNext()
/*     */   {
/* 129 */     boolean hasMore = this.index + 1 < this.rows.size();
/*     */     
/* 131 */     return hasMore;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean isAfterLast()
/*     */   {
/* 140 */     return this.index >= this.rows.size();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean isBeforeFirst()
/*     */   {
/* 149 */     return (this.index == -1) && (this.rows.size() != 0);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean isDynamic()
/*     */   {
/* 158 */     return false;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean isEmpty()
/*     */   {
/* 167 */     return this.rows.size() == 0;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean isFirst()
/*     */   {
/* 176 */     return this.index == 0;
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
/*     */   public boolean isLast()
/*     */   {
/* 189 */     if (this.rows.size() == 0) {
/* 190 */       return false;
/*     */     }
/*     */     
/* 193 */     return this.index == this.rows.size() - 1;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void moveRowRelative(int rowsToMove)
/*     */   {
/* 203 */     this.index += rowsToMove;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public Object[] next()
/*     */   {
/* 212 */     this.index += 1;
/*     */     
/* 214 */     if (this.index < this.rows.size()) {
/* 215 */       return (Object[])this.rows.get(this.index);
/*     */     }
/*     */     
/* 218 */     return null;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void removeRow(int atIndex)
/*     */   {
/* 228 */     this.rows.remove(atIndex);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setCurrentRow(int newIndex)
/*     */   {
/* 238 */     this.index = newIndex;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public void setOwner(ResultSet rs)
/*     */   {
/* 245 */     this.owner = rs;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public int size()
/*     */   {
/* 254 */     return this.rows.size();
/*     */   }
/*     */   
/*     */   public boolean wasEmpty() {
/* 258 */     return (this.rows != null) && (this.rows.size() == 0);
/*     */   }
/*     */ }


/* Location:              E:\java\java学习\hutubill\lib\all.jar!\com\mysql\jdbc\RowDataStatic.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version:       0.7.1
 */
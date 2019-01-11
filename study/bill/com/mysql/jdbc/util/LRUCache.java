/*    */ package com.mysql.jdbc.util;
/*    */ 
/*    */ import java.util.LinkedHashMap;
/*    */ import java.util.Map.Entry;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class LRUCache
/*    */   extends LinkedHashMap
/*    */ {
/*    */   private static final long serialVersionUID = 1L;
/*    */   protected int maxElements;
/*    */   
/*    */   public LRUCache(int maxSize)
/*    */   {
/* 39 */     super(maxSize);
/* 40 */     this.maxElements = maxSize;
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   protected boolean removeEldestEntry(Map.Entry eldest)
/*    */   {
/* 49 */     return size() > this.maxElements;
/*    */   }
/*    */ }


/* Location:              E:\java\java学习\hutubill\lib\all.jar!\com\mysql\jdbc\util\LRUCache.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version:       0.7.1
 */
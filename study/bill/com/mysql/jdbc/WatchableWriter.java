/*    */ package com.mysql.jdbc;
/*    */ 
/*    */ import java.io.CharArrayWriter;
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
/*    */ class WatchableWriter
/*    */   extends CharArrayWriter
/*    */ {
/*    */   private WriterWatcher watcher;
/*    */   
/*    */   public void close()
/*    */   {
/* 47 */     super.close();
/*    */     
/*    */ 
/* 50 */     if (this.watcher != null) {
/* 51 */       this.watcher.writerClosed(this);
/*    */     }
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   public void setWatcher(WriterWatcher watcher)
/*    */   {
/* 62 */     this.watcher = watcher;
/*    */   }
/*    */ }


/* Location:              E:\java\java学习\hutubill\lib\all.jar!\com\mysql\jdbc\WatchableWriter.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version:       0.7.1
 */
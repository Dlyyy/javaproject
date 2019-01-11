/*    */ package com.mysql.jdbc;
/*    */ 
/*    */ import java.io.ByteArrayOutputStream;
/*    */ import java.io.IOException;
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
/*    */ class WatchableOutputStream
/*    */   extends ByteArrayOutputStream
/*    */ {
/*    */   private OutputStreamWatcher watcher;
/*    */   
/*    */   public void close()
/*    */     throws IOException
/*    */   {
/* 48 */     super.close();
/*    */     
/* 50 */     if (this.watcher != null) {
/* 51 */       this.watcher.streamClosed(this);
/*    */     }
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   public void setWatcher(OutputStreamWatcher watcher)
/*    */   {
/* 62 */     this.watcher = watcher;
/*    */   }
/*    */ }


/* Location:              E:\java\java学习\hutubill\lib\all.jar!\com\mysql\jdbc\WatchableOutputStream.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version:       0.7.1
 */
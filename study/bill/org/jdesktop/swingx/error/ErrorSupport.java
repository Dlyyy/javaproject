/*    */ package org.jdesktop.swingx.error;
/*    */ 
/*    */ import java.util.ArrayList;
/*    */ import java.util.List;
/*    */ import javax.swing.SwingUtilities;
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
/*    */ public class ErrorSupport
/*    */ {
/*    */   private List<ErrorListener> listeners;
/*    */   private Object source;
/*    */   
/*    */   public ErrorSupport(Object source)
/*    */   {
/* 44 */     this.source = source;
/* 45 */     this.listeners = new ArrayList();
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */   public void addErrorListener(ErrorListener listener)
/*    */   {
/* 53 */     this.listeners.add(listener);
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */   public void removeErrorListener(ErrorListener listener)
/*    */   {
/* 61 */     this.listeners.remove(listener);
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   public ErrorListener[] getErrorListeners()
/*    */   {
/* 71 */     return (ErrorListener[])this.listeners.toArray(null);
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */   public void fireErrorEvent(Throwable throwable)
/*    */   {
/* 79 */     final ErrorEvent evt = new ErrorEvent(throwable, this.source);
/* 80 */     SwingUtilities.invokeLater(new Runnable() {
/*    */       public void run() {
/* 82 */         for (ErrorListener el : ErrorSupport.this.listeners) {
/* 83 */           el.errorOccured(evt);
/*    */         }
/*    */       }
/*    */     });
/*    */   }
/*    */ }


/* Location:              E:\java\java学习\hutubill\lib\all.jar!\org\jdesktop\swingx\error\ErrorSupport.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */
/*    */ package org.jdesktop.swingx;
/*    */ 
/*    */ import java.beans.PropertyChangeListener;
/*    */ import javax.swing.Action;
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
/*    */ public abstract class UIAction
/*    */   implements Action
/*    */ {
/*    */   private String name;
/*    */   
/*    */   public UIAction(String name)
/*    */   {
/* 63 */     this.name = name;
/*    */   }
/*    */   
/*    */   public final String getName() {
/* 67 */     return this.name;
/*    */   }
/*    */   
/*    */   public Object getValue(String key) {
/* 71 */     return "Name".equals(key) ? this.name : null;
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */   public void putValue(String key, Object value) {}
/*    */   
/*    */ 
/*    */ 
/*    */   public void setEnabled(boolean b) {}
/*    */   
/*    */ 
/*    */ 
/*    */   public final boolean isEnabled()
/*    */   {
/* 86 */     return isEnabled(null);
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   public boolean isEnabled(Object sender)
/*    */   {
/* 96 */     return true;
/*    */   }
/*    */   
/*    */   public void addPropertyChangeListener(PropertyChangeListener listener) {}
/*    */   
/*    */   public void removePropertyChangeListener(PropertyChangeListener listener) {}
/*    */ }


/* Location:              E:\java\java学习\hutubill\lib\all.jar!\org\jdesktop\swingx\UIAction.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */
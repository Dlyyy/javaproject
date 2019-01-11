/*    */ package org.jdesktop.swingx.plaf;
/*    */ 
/*    */ import java.beans.PropertyChangeListener;
/*    */ import java.util.Map;
/*    */ import java.util.WeakHashMap;
/*    */ import javax.swing.JComponent;
/*    */ 
/*    */ public abstract class AbstractUIChangeHandler
/*    */   implements PropertyChangeListener
/*    */ {
/* 11 */   private final Map<JComponent, Boolean> installed = new WeakHashMap();
/*    */   
/*    */   public void install(JComponent c) {
/* 14 */     if (isInstalled(c)) {
/* 15 */       return;
/*    */     }
/*    */     
/* 18 */     c.addPropertyChangeListener("UI", this);
/* 19 */     this.installed.put(c, Boolean.FALSE);
/*    */   }
/*    */   
/*    */   public boolean isInstalled(JComponent c) {
/* 23 */     return this.installed.containsKey(c);
/*    */   }
/*    */   
/*    */   public void uninstall(JComponent c) {
/* 27 */     c.removePropertyChangeListener("UI", this);
/* 28 */     this.installed.remove(c);
/*    */   }
/*    */ }


/* Location:              E:\java\java学习\hutubill\lib\all.jar!\org\jdesktop\swingx\plaf\AbstractUIChangeHandler.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */
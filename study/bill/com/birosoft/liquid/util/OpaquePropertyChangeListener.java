/*    */ package com.birosoft.liquid.util;
/*    */ 
/*    */ import java.beans.PropertyChangeEvent;
/*    */ import java.beans.PropertyChangeListener;
/*    */ import javax.swing.JComponent;
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
/*    */ public class OpaquePropertyChangeListener
/*    */   implements PropertyChangeListener
/*    */ {
/*    */   private static final String PROPERTY_NAME = "opaque";
/* 23 */   private JComponent component = null;
/*    */   
/*    */   public OpaquePropertyChangeListener(JComponent component)
/*    */   {
/* 27 */     this.component = component;
/*    */   }
/*    */   
/*    */   public void propertyChange(PropertyChangeEvent evt) {
/* 31 */     if ((evt.getPropertyName().equals("opaque")) && (evt.getSource() != this)) {
/* 32 */       this.component.setOpaque(false);
/*    */     }
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */   public static PropertyChangeListener getComponentListener(JComponent comp)
/*    */   {
/* 41 */     PropertyChangeListener[] listeners = comp.getPropertyChangeListeners("opaque");
/*    */     
/* 43 */     if (listeners != null) {
/* 44 */       for (int counter = 0; counter < listeners.length; counter++) {
/* 45 */         if ((listeners[counter] instanceof OpaquePropertyChangeListener)) {
/* 46 */           return (OpaquePropertyChangeListener)listeners[counter];
/*    */         }
/*    */       }
/*    */     }
/* 50 */     return null;
/*    */   }
/*    */ }


/* Location:              E:\java\java学习\hutubill\lib\all.jar!\com\birosoft\liquid\util\OpaquePropertyChangeListener.class
 * Java compiler version: 4 (48.0)
 * JD-Core Version:       0.7.1
 */
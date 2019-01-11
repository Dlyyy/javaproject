/*    */ package org.jdesktop.swingx.action;
/*    */ 
/*    */ import java.awt.event.ActionEvent;
/*    */ import javax.swing.Action;
/*    */ import javax.swing.ActionMap;
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
/*    */ public class TargetableSupport
/*    */ {
/*    */   private JComponent component;
/*    */   
/*    */   public TargetableSupport(JComponent component)
/*    */   {
/* 38 */     this.component = component;
/*    */   }
/*    */   
/*    */   public boolean doCommand(Object command, Object value)
/*    */   {
/* 43 */     ActionMap map = this.component.getActionMap();
/* 44 */     Action action = map.get(command);
/*    */     
/* 46 */     if (action != null) {
/* 47 */       if ((value instanceof ActionEvent)) {
/* 48 */         action.actionPerformed((ActionEvent)value);
/*    */       }
/*    */       else
/*    */       {
/* 52 */         action.actionPerformed(new ActionEvent(value, 0, command.toString()));
/*    */       }
/*    */       
/* 55 */       return true;
/*    */     }
/* 57 */     return false;
/*    */   }
/*    */   
/*    */   public Object[] getCommands() {
/* 61 */     ActionMap map = this.component.getActionMap();
/* 62 */     return map.allKeys();
/*    */   }
/*    */   
/*    */   public boolean hasCommand(Object command) {
/* 66 */     Object[] commands = getCommands();
/* 67 */     for (int i = 0; i < commands.length; i++) {
/* 68 */       if (commands[i].equals(command)) {
/* 69 */         return true;
/*    */       }
/*    */     }
/* 72 */     return false;
/*    */   }
/*    */ }


/* Location:              E:\java\java学习\hutubill\lib\all.jar!\org\jdesktop\swingx\action\TargetableSupport.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */
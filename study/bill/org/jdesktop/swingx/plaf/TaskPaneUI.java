/*    */ package org.jdesktop.swingx.plaf;
/*    */ 
/*    */ import java.awt.Component;
/*    */ import javax.swing.Action;
/*    */ import javax.swing.JButton;
/*    */ import javax.swing.plaf.PanelUI;
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
/*    */ public abstract class TaskPaneUI
/*    */   extends PanelUI
/*    */ {
/*    */   public Component createAction(Action action)
/*    */   {
/* 44 */     return new JButton(action);
/*    */   }
/*    */ }


/* Location:              E:\java\java学习\hutubill\lib\all.jar!\org\jdesktop\swingx\plaf\TaskPaneUI.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */
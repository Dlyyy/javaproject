/*     */ package org.jdesktop.swingx.action;
/*     */ 
/*     */ import java.awt.event.ActionEvent;
/*     */ import java.awt.event.ItemEvent;
/*     */ import javax.swing.Icon;
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
/*     */ public class TargetableAction
/*     */   extends AbstractActionExt
/*     */ {
/*     */   private TargetManager targetManager;
/*     */   
/*     */   public TargetableAction()
/*     */   {
/*  46 */     this("action");
/*     */   }
/*     */   
/*     */   public TargetableAction(String name) {
/*  50 */     super(name);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public TargetableAction(String name, String command)
/*     */   {
/*  58 */     super(name, command);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public TargetableAction(String name, String command, Icon icon)
/*     */   {
/*  67 */     super(name, command, icon);
/*     */   }
/*     */   
/*     */   public TargetableAction(String name, Icon icon) {
/*  71 */     super(name, icon);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setTargetManager(TargetManager tm)
/*     */   {
/*  81 */     this.targetManager = tm;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public TargetManager getTargetManager()
/*     */   {
/*  92 */     if (this.targetManager == null) {
/*  93 */       this.targetManager = TargetManager.getInstance();
/*     */     }
/*  95 */     return this.targetManager;
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
/*     */   public void actionPerformed(ActionEvent evt)
/*     */   {
/* 108 */     if (!isStateAction())
/*     */     {
/* 110 */       getTargetManager().doCommand(getActionCommand(), evt);
/*     */     }
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
/*     */ 
/*     */   public void itemStateChanged(ItemEvent evt)
/*     */   {
/* 125 */     boolean oldValue = isSelected();
/*     */     
/* 127 */     boolean newValue = evt.getStateChange() == 1;
/*     */     
/* 129 */     if (oldValue != newValue) {
/* 130 */       setSelected(newValue);
/*     */       
/* 132 */       getTargetManager().doCommand(getActionCommand(), evt);
/*     */     }
/*     */   }
/*     */   
/*     */   public String toString()
/*     */   {
/* 138 */     return super.toString();
/*     */   }
/*     */ }


/* Location:              E:\java\java学习\hutubill\lib\all.jar!\org\jdesktop\swingx\action\TargetableAction.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */
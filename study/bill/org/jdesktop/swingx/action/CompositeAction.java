/*     */ package org.jdesktop.swingx.action;
/*     */ 
/*     */ import java.awt.event.ActionEvent;
/*     */ import java.awt.event.ItemEvent;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import javax.swing.Action;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ public class CompositeAction
/*     */   extends AbstractActionExt
/*     */ {
/*     */   private static final String LIST_IDS = "action-list-ids";
/*     */   
/*     */   public CompositeAction()
/*     */   {
/*  53 */     this("CompositeAction");
/*     */   }
/*     */   
/*     */   public CompositeAction(String name) {
/*  57 */     super(name);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public CompositeAction(String name, String command)
/*     */   {
/*  65 */     super(name, command);
/*     */   }
/*     */   
/*     */   public CompositeAction(String name, Icon icon) {
/*  69 */     super(name, icon);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public CompositeAction(String name, String command, Icon icon)
/*     */   {
/*  78 */     super(name, command, icon);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void addAction(String id)
/*     */   {
/*  87 */     List<String> list = (List)getValue("action-list-ids");
/*  88 */     if (list == null) {
/*  89 */       list = new ArrayList();
/*  90 */       putValue("action-list-ids", list);
/*     */     }
/*  92 */     list.add(id);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public List<String> getActionIDs()
/*     */   {
/* 102 */     return (List)getValue("action-list-ids");
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void actionPerformed(ActionEvent evt)
/*     */   {
/* 110 */     ActionManager manager = ActionManager.getInstance();
/*     */     
/* 112 */     Iterator<String> iter = getActionIDs().iterator();
/* 113 */     while (iter.hasNext()) {
/* 114 */       String id = (String)iter.next();
/* 115 */       Action action = manager.getAction(id);
/* 116 */       if (action != null) {
/* 117 */         action.actionPerformed(evt);
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void itemStateChanged(ItemEvent evt)
/*     */   {
/* 127 */     ActionManager manager = ActionManager.getInstance();
/*     */     
/* 129 */     Iterator<String> iter = getActionIDs().iterator();
/* 130 */     while (iter.hasNext()) {
/* 131 */       String id = (String)iter.next();
/* 132 */       Action action = manager.getAction(id);
/* 133 */       if ((action != null) && ((action instanceof AbstractActionExt))) {
/* 134 */         ((AbstractActionExt)action).itemStateChanged(evt);
/*     */       }
/*     */     }
/*     */   }
/*     */ }


/* Location:              E:\java\java学习\hutubill\lib\all.jar!\org\jdesktop\swingx\action\CompositeAction.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */
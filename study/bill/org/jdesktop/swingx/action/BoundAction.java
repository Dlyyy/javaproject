/*     */ package org.jdesktop.swingx.action;
/*     */ 
/*     */ import java.awt.event.ActionEvent;
/*     */ import java.awt.event.ActionListener;
/*     */ import java.awt.event.ItemEvent;
/*     */ import java.awt.event.ItemListener;
/*     */ import java.beans.EventHandler;
/*     */ import java.beans.Statement;
/*     */ import java.util.EventListener;
/*     */ import java.util.logging.Level;
/*     */ import java.util.logging.Logger;
/*     */ import javax.swing.Icon;
/*     */ import javax.swing.event.EventListenerList;
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
/*     */ public class BoundAction
/*     */   extends AbstractActionExt
/*     */ {
/*  47 */   private static final Logger LOG = Logger.getLogger(BoundAction.class.getName());
/*     */   
/*     */   private EventListenerList listeners;
/*     */   
/*     */   public BoundAction()
/*     */   {
/*  53 */     this("BoundAction");
/*     */   }
/*     */   
/*     */   public BoundAction(String name) {
/*  57 */     super(name);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public BoundAction(String name, String command)
/*     */   {
/*  65 */     super(name, command);
/*     */   }
/*     */   
/*     */   public BoundAction(String name, Icon icon) {
/*  69 */     super(name, icon);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public BoundAction(String name, String command, Icon icon)
/*     */   {
/*  78 */     super(name, command, icon);
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
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setCallback(String callback)
/*     */   {
/*  95 */     String[] elems = callback.split("#", 2);
/*  96 */     if (elems.length == 2) {
/*     */       try {
/*  98 */         Class<?> clz = Class.forName(elems[0]);
/*     */         
/*     */ 
/*     */ 
/* 102 */         Object obj = clz.newInstance();
/*     */         
/* 104 */         registerCallback(obj, elems[1]);
/*     */       } catch (Exception ex) {
/* 106 */         LOG.fine("ERROR: setCallback(" + callback + ") - " + ex.getMessage());
/*     */       }
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void registerCallback(Object handler, String method)
/*     */   {
/* 126 */     if (isStateAction())
/*     */     {
/* 128 */       addItemListener(new BooleanInvocationHandler(handler, method));
/*     */     }
/*     */     else {
/* 131 */       addActionListener((ActionListener)EventHandler.create(ActionListener.class, handler, method));
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   private class BooleanInvocationHandler
/*     */     implements ItemListener
/*     */   {
/*     */     private Statement falseStatement;
/*     */     
/*     */ 
/*     */     private Statement trueStatement;
/*     */     
/*     */ 
/*     */ 
/*     */     public BooleanInvocationHandler(Object target, String methodName)
/*     */     {
/* 149 */       this.falseStatement = new Statement(target, methodName, new Object[] { Boolean.FALSE });
/*     */       
/*     */ 
/* 152 */       this.trueStatement = new Statement(target, methodName, new Object[] { Boolean.TRUE });
/*     */     }
/*     */     
/*     */     public void itemStateChanged(ItemEvent evt)
/*     */     {
/* 157 */       Statement statement = evt.getStateChange() == 2 ? this.falseStatement : this.trueStatement;
/*     */       
/*     */       try
/*     */       {
/* 161 */         statement.execute();
/*     */       } catch (Exception ex) {
/* 163 */         BoundAction.LOG.log(Level.FINE, "Couldn't execute boolean method via Statement " + statement, ex);
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   private <T extends EventListener> void addListener(Class<T> clz, T listener)
/*     */   {
/* 173 */     if (this.listeners == null) {
/* 174 */       this.listeners = new EventListenerList();
/*     */     }
/* 176 */     this.listeners.add(clz, listener);
/*     */   }
/*     */   
/*     */   private <T extends EventListener> void removeListener(Class<T> clz, T listener) {
/* 180 */     if (this.listeners != null) {
/* 181 */       this.listeners.remove(clz, listener);
/*     */     }
/*     */   }
/*     */   
/*     */   private EventListener[] getListeners(Class<? extends EventListener> clz) {
/* 186 */     if (this.listeners == null) {
/* 187 */       return null;
/*     */     }
/* 189 */     return this.listeners.getListeners(clz);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public void addActionListener(ActionListener listener)
/*     */   {
/* 196 */     addListener(ActionListener.class, listener);
/*     */   }
/*     */   
/*     */   public void removeActionListener(ActionListener listener) {
/* 200 */     removeListener(ActionListener.class, listener);
/*     */   }
/*     */   
/*     */   public ActionListener[] getActionListeners() {
/* 204 */     return (ActionListener[])getListeners(ActionListener.class);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public void addItemListener(ItemListener listener)
/*     */   {
/* 211 */     addListener(ItemListener.class, listener);
/*     */   }
/*     */   
/*     */   public void removeItemListener(ItemListener listener) {
/* 215 */     removeListener(ItemListener.class, listener);
/*     */   }
/*     */   
/*     */   public ItemListener[] getItemListeners() {
/* 219 */     return (ItemListener[])getListeners(ItemListener.class);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void actionPerformed(ActionEvent evt)
/*     */   {
/* 228 */     ActionListener[] alist = getActionListeners();
/* 229 */     if (alist != null) {
/* 230 */       for (int i = 0; i < alist.length; i++) {
/* 231 */         alist[i].actionPerformed(evt);
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void itemStateChanged(ItemEvent evt)
/*     */   {
/* 243 */     boolean oldValue = isSelected();
/*     */     
/* 245 */     boolean newValue = evt.getStateChange() == 1;
/*     */     
/* 247 */     if (oldValue != newValue) {
/* 248 */       setSelected(newValue);
/*     */       
/*     */ 
/* 251 */       ItemListener[] ilist = getItemListeners();
/* 252 */       if (ilist != null) {
/* 253 */         for (int i = 0; i < ilist.length; i++) {
/* 254 */           ilist[i].itemStateChanged(evt);
/*     */         }
/*     */       }
/*     */     }
/*     */   }
/*     */ }


/* Location:              E:\java\java学习\hutubill\lib\all.jar!\org\jdesktop\swingx\action\BoundAction.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */
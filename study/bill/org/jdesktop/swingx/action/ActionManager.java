/*     */ package org.jdesktop.swingx.action;
/*     */ 
/*     */ import java.io.PrintStream;
/*     */ import java.util.Arrays;
/*     */ import java.util.HashSet;
/*     */ import java.util.Set;
/*     */ import javax.swing.AbstractAction;
/*     */ import javax.swing.Action;
/*     */ import javax.swing.ActionMap;
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
/*     */ public class ActionManager
/*     */   extends ActionMap
/*     */ {
/*     */   private static ActionManager INSTANCE;
/*     */   
/*     */   public static ActionManager getInstance()
/*     */   {
/* 113 */     if (INSTANCE == null) {
/* 114 */       INSTANCE = new ActionManager();
/*     */     }
/* 116 */     return INSTANCE;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public static void setInstance(ActionManager manager)
/*     */   {
/* 123 */     INSTANCE = manager;
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
/*     */   public Set<Object> getActionIDs()
/*     */   {
/* 138 */     Object[] keys = keys();
/* 139 */     if (keys == null) {
/* 140 */       return null;
/*     */     }
/*     */     
/* 143 */     return new HashSet(Arrays.asList(keys));
/*     */   }
/*     */   
/*     */   public Action addAction(Action action) {
/* 147 */     return addAction(action.getValue("ActionCommandKey"), action);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public Action addAction(Object id, Action action)
/*     */   {
/* 157 */     put(id, action);
/* 158 */     return action;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public Action getAction(Object id)
/*     */   {
/* 168 */     return get(id);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public TargetableAction getTargetableAction(Object id)
/*     */   {
/* 178 */     Action a = getAction(id);
/* 179 */     if ((a instanceof TargetableAction)) {
/* 180 */       return (TargetableAction)a;
/*     */     }
/* 182 */     return null;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public BoundAction getBoundAction(Object id)
/*     */   {
/* 192 */     Action a = getAction(id);
/* 193 */     if ((a instanceof BoundAction)) {
/* 194 */       return (BoundAction)a;
/*     */     }
/* 196 */     return null;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public ServerAction getServerAction(Object id)
/*     */   {
/* 206 */     Action a = getAction(id);
/* 207 */     if ((a instanceof ServerAction)) {
/* 208 */       return (ServerAction)a;
/*     */     }
/* 210 */     return null;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public CompositeAction getCompositeAction(Object id)
/*     */   {
/* 220 */     Action a = getAction(id);
/* 221 */     if ((a instanceof CompositeAction)) {
/* 222 */       return (CompositeAction)a;
/*     */     }
/* 224 */     return null;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private AbstractActionExt getStateChangeAction(Object id)
/*     */   {
/* 234 */     Action a = getAction(id);
/* 235 */     if ((a != null) && ((a instanceof AbstractActionExt))) {
/* 236 */       AbstractActionExt aa = (AbstractActionExt)a;
/* 237 */       if (aa.isStateAction()) {
/* 238 */         return aa;
/*     */       }
/*     */     }
/* 241 */     return null;
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
/*     */   public void setEnabled(Object id, boolean enabled)
/*     */   {
/* 254 */     Action action = getAction(id);
/* 255 */     if (action != null) {
/* 256 */       action.setEnabled(enabled);
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
/*     */   public boolean isEnabled(Object id)
/*     */   {
/* 271 */     Action action = getAction(id);
/* 272 */     if (action != null) {
/* 273 */       return action.isEnabled();
/*     */     }
/* 275 */     return false;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setSelected(Object id, boolean selected)
/*     */   {
/* 286 */     AbstractActionExt action = getStateChangeAction(id);
/* 287 */     if (action != null) {
/* 288 */       action.setSelected(selected);
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
/*     */   public boolean isSelected(Object id)
/*     */   {
/* 301 */     AbstractActionExt action = getStateChangeAction(id);
/* 302 */     if (action != null) {
/* 303 */       return action.isSelected();
/*     */     }
/* 305 */     return false;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   static void printAction(PrintStream stream, Action action)
/*     */   {
/* 313 */     stream.println("Attributes for " + action.getValue("ActionCommandKey"));
/*     */     
/* 315 */     if ((action instanceof AbstractAction)) {
/* 316 */       Object[] keys = ((AbstractAction)action).getKeys();
/*     */       
/* 318 */       for (int i = 0; i < keys.length; i++) {
/* 319 */         stream.println("\tkey: " + keys[i] + "\tvalue: " + action.getValue((String)keys[i]));
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
/*     */   public void registerCallback(Object id, Object handler, String method)
/*     */   {
/* 334 */     BoundAction action = getBoundAction(id);
/* 335 */     if (action != null) {
/* 336 */       action.registerCallback(handler, method);
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
/*     */   public boolean isStateAction(Object id)
/*     */   {
/* 352 */     Action action = getAction(id);
/* 353 */     if ((action != null) && ((action instanceof AbstractActionExt))) {
/* 354 */       return ((AbstractActionExt)action).isStateAction();
/*     */     }
/* 356 */     return false;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public boolean isTargetableAction(Object id)
/*     */   {
/* 363 */     return getTargetableAction(id) != null;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public boolean isBoundAction(Object id)
/*     */   {
/* 370 */     return getBoundAction(id) != null;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public boolean isCompositeAction(Object id)
/*     */   {
/* 377 */     return getCompositeAction(id) != null;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public boolean isServerAction(Object id)
/*     */   {
/* 384 */     return getServerAction(id) != null;
/*     */   }
/*     */ }


/* Location:              E:\java\java学习\hutubill\lib\all.jar!\org\jdesktop\swingx\action\ActionManager.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */
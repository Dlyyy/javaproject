/*     */ package org.jdesktop.swingx.action;
/*     */ 
/*     */ import javax.swing.AbstractAction;
/*     */ import javax.swing.Action;
/*     */ import javax.swing.Icon;
/*     */ import javax.swing.KeyStroke;
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
/*     */ public class ActionFactory
/*     */ {
/*     */   public static BoundAction createBoundAction(String id, String name, String mnemonic)
/*     */   {
/*  43 */     return createBoundAction(id, name, mnemonic, false);
/*     */   }
/*     */   
/*     */   public static BoundAction createBoundAction(String id, String name, String mnemonic, boolean toggle)
/*     */   {
/*  48 */     return createBoundAction(id, name, mnemonic, toggle, null);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public static BoundAction createBoundAction(String id, String name, String mnemonic, boolean toggle, String group)
/*     */   {
/*  55 */     return (BoundAction)configureAction(new BoundAction(name, id), mnemonic, toggle, group);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static CompositeAction createCompositeAction(String id, String name, String mnemonic)
/*     */   {
/*  65 */     return createCompositeAction(id, name, mnemonic, false);
/*     */   }
/*     */   
/*     */   public static CompositeAction createCompositeAction(String id, String name, String mnemonic, boolean toggle)
/*     */   {
/*  70 */     return createCompositeAction(id, name, mnemonic, toggle, null);
/*     */   }
/*     */   
/*     */ 
/*     */   public static CompositeAction createCompositeAction(String id, String name, String mnemonic, boolean toggle, String group)
/*     */   {
/*  76 */     return (CompositeAction)configureAction(new CompositeAction(name, id), mnemonic, toggle, group);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public static ServerAction createServerAction(String id, String name, String mnemonic)
/*     */   {
/*  83 */     ServerAction action = new ServerAction(name, id);
/*  84 */     if ((mnemonic != null) && (!mnemonic.equals(""))) {
/*  85 */       action.putValue("MnemonicKey", new Integer(mnemonic.charAt(0)));
/*     */     }
/*  87 */     return action;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public static TargetableAction createTargetableAction(String id, String name)
/*     */   {
/*  95 */     return createTargetableAction(id, name, null);
/*     */   }
/*     */   
/*     */   public static TargetableAction createTargetableAction(String id, String name, String mnemonic)
/*     */   {
/* 100 */     return createTargetableAction(id, name, mnemonic, false);
/*     */   }
/*     */   
/*     */   public static TargetableAction createTargetableAction(String id, String name, String mnemonic, boolean toggle)
/*     */   {
/* 105 */     return createTargetableAction(id, name, mnemonic, toggle, null);
/*     */   }
/*     */   
/*     */ 
/*     */   public static TargetableAction createTargetableAction(String id, String name, String mnemonic, boolean toggle, String group)
/*     */   {
/* 111 */     return (TargetableAction)configureAction(new TargetableAction(name, id), mnemonic, toggle, group);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   private static Action configureAction(AbstractActionExt action, String mnemonic, boolean toggle, String group)
/*     */   {
/* 118 */     action.setMnemonic(mnemonic);
/* 119 */     String description = action.getName() + " action with comand " + action.getActionCommand();
/* 120 */     action.setShortDescription(description);
/* 121 */     action.setLongDescription(description);
/*     */     
/* 123 */     if (toggle) {
/* 124 */       action.setStateAction();
/*     */     }
/* 126 */     if (group != null) {
/* 127 */       action.setGroup(group);
/*     */     }
/* 129 */     return action;
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
/*     */   public static void decorateAction(AbstractAction action, String shortDesc, String longDesc, Icon smallIcon, Icon largeIcon, KeyStroke accel)
/*     */   {
/* 144 */     if ((action instanceof AbstractActionExt)) {
/* 145 */       AbstractActionExt a = (AbstractActionExt)action;
/* 146 */       a.setShortDescription(shortDesc);
/* 147 */       a.setLongDescription(longDesc);
/* 148 */       a.setSmallIcon(smallIcon);
/* 149 */       a.setLargeIcon(largeIcon);
/* 150 */       a.setAccelerator(accel);
/*     */     }
/*     */     else {
/* 153 */       action.putValue("ShortDescription", shortDesc);
/* 154 */       action.putValue("LongDescription", longDesc);
/* 155 */       action.putValue("SmallIcon", smallIcon);
/* 156 */       action.putValue("__LargeIcon__", largeIcon);
/* 157 */       action.putValue("AcceleratorKey", accel);
/*     */     }
/*     */   }
/*     */ }


/* Location:              E:\java\java学习\hutubill\lib\all.jar!\org\jdesktop\swingx\action\ActionFactory.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */
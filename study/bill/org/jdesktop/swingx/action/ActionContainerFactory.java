/*     */ package org.jdesktop.swingx.action;
/*     */ 
/*     */ import java.awt.Insets;
/*     */ import java.beans.PropertyChangeListener;
/*     */ import java.util.Arrays;
/*     */ import java.util.HashMap;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import javax.swing.AbstractButton;
/*     */ import javax.swing.Action;
/*     */ import javax.swing.ActionMap;
/*     */ import javax.swing.ButtonGroup;
/*     */ import javax.swing.Icon;
/*     */ import javax.swing.JButton;
/*     */ import javax.swing.JCheckBoxMenuItem;
/*     */ import javax.swing.JComponent;
/*     */ import javax.swing.JMenu;
/*     */ import javax.swing.JMenuBar;
/*     */ import javax.swing.JMenuItem;
/*     */ import javax.swing.JPopupMenu;
/*     */ import javax.swing.JRadioButtonMenuItem;
/*     */ import javax.swing.JToggleButton;
/*     */ import javax.swing.JToolBar;
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
/*     */ public class ActionContainerFactory
/*     */ {
/*  77 */   private static Insets TOOLBAR_BUTTON_MARGIN = new Insets(1, 1, 1, 1);
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   private ActionMap manager;
/*     */   
/*     */ 
/*     */ 
/*     */   private Map<Integer, ButtonGroup> groupMap;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public ActionContainerFactory() {}
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public ActionContainerFactory(ActionMap manager)
/*     */   {
/*  98 */     setActionManager(manager);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public ActionMap getActionManager()
/*     */   {
/* 109 */     if (this.manager == null) {
/* 110 */       this.manager = ActionManager.getInstance();
/*     */     }
/* 112 */     return this.manager;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setActionManager(ActionMap manager)
/*     */   {
/* 120 */     this.manager = manager;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public JToolBar createToolBar(Object[] list)
/*     */   {
/* 131 */     return createToolBar(Arrays.asList(list));
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public JToolBar createToolBar(List<?> list)
/*     */   {
/* 142 */     JToolBar toolbar = new JToolBar();
/* 143 */     Iterator<?> iter = list.iterator();
/* 144 */     while (iter.hasNext()) {
/* 145 */       Object element = iter.next();
/*     */       
/* 147 */       if (element == null) {
/* 148 */         toolbar.addSeparator();
/*     */       } else {
/* 150 */         AbstractButton button = createButton(element, toolbar);
/*     */         
/* 152 */         button.setFocusable(false);
/*     */         
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 159 */         button.setMargin(TOOLBAR_BUTTON_MARGIN);
/* 160 */         button.setBorderPainted(false);
/*     */         
/* 162 */         toolbar.add(button);
/*     */       }
/*     */     }
/* 165 */     return toolbar;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public JPopupMenu createPopup(Object[] list)
/*     */   {
/* 176 */     return createPopup(Arrays.asList(list));
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public JPopupMenu createPopup(List<?> list)
/*     */   {
/* 186 */     JPopupMenu popup = new JPopupMenu();
/* 187 */     Iterator<?> iter = list.iterator();
/* 188 */     while (iter.hasNext()) {
/* 189 */       Object element = iter.next();
/*     */       
/* 191 */       if (element == null) {
/* 192 */         popup.addSeparator();
/* 193 */       } else if ((element instanceof List)) {
/* 194 */         JMenu newMenu = createMenu((List)element);
/* 195 */         if (newMenu != null) {
/* 196 */           popup.add(newMenu);
/*     */         }
/*     */       } else {
/* 199 */         popup.add(createMenuItem(element, popup));
/*     */       }
/*     */     }
/* 202 */     return popup;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public JMenuBar createMenuBar(Object[] actionIds)
/*     */   {
/* 212 */     return createMenuBar(Arrays.asList(actionIds));
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public JMenuBar createMenuBar(List<?> list)
/*     */   {
/* 222 */     JMenuBar menubar = new JMenuBar();
/*     */     
/* 224 */     for (Object element : list) {
/* 225 */       if (element != null)
/*     */       {
/*     */         JMenuItem menu;
/*     */         
/*     */         JMenuItem menu;
/*     */         
/* 231 */         if ((element instanceof Object[])) {
/* 232 */           menu = createMenu((Object[])element); } else { JMenuItem menu;
/* 233 */           if ((element instanceof List)) {
/* 234 */             menu = createMenu((List)element);
/*     */           } else {
/* 236 */             menu = createMenuItem(element, menubar);
/*     */           }
/*     */         }
/* 239 */         if (menu != null) {
/* 240 */           menubar.add(menu);
/*     */         }
/*     */       }
/*     */     }
/* 244 */     return menubar;
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
/*     */   public JMenu createMenu(Object[] actionIds)
/*     */   {
/* 259 */     return createMenu(Arrays.asList(actionIds));
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
/*     */   public JMenu createMenu(List<?> list)
/*     */   {
/* 274 */     Action action = getAction(list.get(0));
/*     */     
/* 276 */     if (action == null) {
/* 277 */       return null;
/*     */     }
/*     */     
/* 280 */     JMenu menu = new JMenu(action);
/*     */     
/*     */ 
/* 283 */     for (Object element : list.subList(1, list.size())) {
/* 284 */       if (element == null) {
/* 285 */         menu.addSeparator();
/*     */       } else {
/*     */         JMenuItem newMenu;
/*     */         JMenuItem newMenu;
/* 289 */         if ((element instanceof Object[])) {
/* 290 */           newMenu = createMenu((Object[])element); } else { JMenuItem newMenu;
/* 291 */           if ((element instanceof List)) {
/* 292 */             newMenu = createMenu((List)element);
/*     */           } else {
/* 294 */             newMenu = createMenuItem(element, menu);
/*     */           }
/*     */         }
/* 297 */         if (newMenu != null) {
/* 298 */           menu.add(newMenu);
/*     */         }
/*     */       }
/*     */     }
/*     */     
/* 303 */     return menu;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   private Action getAction(Object id)
/*     */   {
/* 310 */     return getActionManager().get(id);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private ButtonGroup getGroup(String groupid, JComponent container)
/*     */   {
/* 320 */     if (this.groupMap == null) {
/* 321 */       this.groupMap = new HashMap();
/*     */     }
/* 323 */     int intCode = groupid.hashCode();
/* 324 */     if (container != null) {
/* 325 */       intCode ^= container.hashCode();
/*     */     }
/* 327 */     Integer hashCode = new Integer(intCode);
/*     */     
/* 329 */     ButtonGroup group = (ButtonGroup)this.groupMap.get(hashCode);
/* 330 */     if (group == null) {
/* 331 */       group = new ButtonGroup();
/* 332 */       this.groupMap.put(hashCode, group);
/*     */     }
/* 334 */     return group;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private JMenuItem createMenuItem(Object id, JComponent container)
/*     */   {
/* 345 */     return createMenuItem(getAction(id), container);
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
/*     */   @Deprecated
/*     */   public JMenuItem createMenuItem(Action action, JComponent container)
/*     */   {
/* 361 */     JMenuItem menuItem = null;
/* 362 */     if ((action instanceof AbstractActionExt)) {
/* 363 */       AbstractActionExt ta = (AbstractActionExt)action;
/*     */       
/* 365 */       if (ta.isStateAction()) {
/* 366 */         String groupid = (String)ta.getGroup();
/* 367 */         if (groupid != null)
/*     */         {
/*     */ 
/* 370 */           menuItem = createRadioButtonMenuItem(getGroup(groupid, container), (AbstractActionExt)action);
/*     */         }
/*     */         else {
/* 373 */           menuItem = createCheckBoxMenuItem((AbstractActionExt)action);
/*     */         }
/*     */       }
/*     */     }
/*     */     
/* 378 */     if (menuItem == null) {
/* 379 */       menuItem = new JMenuItem(action);
/* 380 */       configureMenuItemFromExtActionProperties(menuItem, action);
/*     */     }
/* 382 */     return menuItem;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public JMenuItem createMenuItem(Action action)
/*     */   {
/* 394 */     return createMenuItem(action, null);
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
/*     */   public AbstractButton createButton(Object id, JComponent container)
/*     */   {
/* 410 */     return createButton(getAction(id), container);
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
/*     */   public AbstractButton createButton(Action action, JComponent container)
/*     */   {
/* 425 */     if (action == null) {
/* 426 */       return null;
/*     */     }
/*     */     
/* 429 */     AbstractButton button = null;
/* 430 */     if ((action instanceof AbstractActionExt))
/*     */     {
/* 432 */       AbstractActionExt ta = (AbstractActionExt)action;
/*     */       
/* 434 */       if (ta.isStateAction())
/*     */       {
/*     */ 
/* 437 */         String groupid = (String)ta.getGroup();
/* 438 */         if (groupid == null) {
/* 439 */           button = createToggleButton(ta);
/*     */         } else {
/* 441 */           button = createToggleButton(ta, getGroup(groupid, container));
/*     */         }
/*     */       }
/*     */     }
/*     */     
/* 446 */     if (button == null)
/*     */     {
/* 448 */       button = new JButton(action);
/* 449 */       configureButtonFromExtActionProperties(button, action);
/*     */     }
/* 451 */     return button;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public AbstractButton createButton(Action action)
/*     */   {
/* 461 */     return createButton(action, null);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   private JToggleButton createToggleButton(AbstractActionExt a)
/*     */   {
/* 469 */     return createToggleButton(a, null);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private JToggleButton createToggleButton(AbstractActionExt a, ButtonGroup group)
/*     */   {
/* 478 */     JToggleButton button = new JToggleButton();
/* 479 */     configureButton(button, a, group);
/* 480 */     return button;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void configureButton(JToggleButton button, AbstractActionExt a, ButtonGroup group)
/*     */   {
/* 490 */     configureSelectableButton(button, a, group);
/* 491 */     configureButtonFromExtActionProperties(button, a);
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void configureSelectableButton(AbstractButton button, AbstractActionExt a, ButtonGroup group)
/*     */   {
/* 513 */     if ((a != null) && (!a.isStateAction())) { throw new IllegalArgumentException("the Action must be a stateAction");
/*     */     }
/*     */     
/* 516 */     if (button.getAction() == a) { return;
/*     */     }
/*     */     
/*     */ 
/*     */ 
/* 521 */     Action oldAction = button.getAction();
/* 522 */     if ((oldAction instanceof AbstractActionExt)) {
/* 523 */       AbstractActionExt actionExt = (AbstractActionExt)oldAction;
/*     */       
/* 525 */       button.removeItemListener(actionExt);
/*     */       
/* 527 */       PropertyChangeListener[] l = actionExt.getPropertyChangeListeners();
/* 528 */       for (int i = l.length - 1; i >= 0; i--) {
/* 529 */         if ((l[i] instanceof ToggleActionPropertyChangeListener)) {
/* 530 */           ToggleActionPropertyChangeListener togglePCL = (ToggleActionPropertyChangeListener)l[i];
/* 531 */           if (togglePCL.isToggling(button)) {
/* 532 */             actionExt.removePropertyChangeListener(togglePCL);
/*     */           }
/*     */         }
/*     */       }
/*     */     }
/*     */     
/* 538 */     button.setAction(a);
/* 539 */     if (group != null) {
/* 540 */       group.add(button);
/*     */     }
/* 542 */     if (a != null) {
/* 543 */       button.addItemListener(a);
/*     */       
/* 545 */       button.setSelected(a.isSelected());
/* 546 */       new ToggleActionPropertyChangeListener(a, button);
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
/*     */   protected void configureButtonFromExtActionProperties(AbstractButton button, Action action)
/*     */   {
/* 560 */     if (action.getValue("ShortDescription") == null) {
/* 561 */       button.setToolTipText((String)action.getValue("Name"));
/*     */     }
/*     */     
/* 564 */     if (action.getValue("__LargeIcon__") != null) {
/* 565 */       button.setIcon((Icon)action.getValue("__LargeIcon__"));
/*     */     }
/*     */     
/* 568 */     if (button.getIcon() != null) {
/* 569 */       button.setText("");
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected void configureMenuItemFromExtActionProperties(JMenuItem menuItem, Action action) {}
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private JCheckBoxMenuItem createCheckBoxMenuItem(AbstractActionExt a)
/*     */   {
/* 588 */     JCheckBoxMenuItem mi = new JCheckBoxMenuItem();
/* 589 */     configureSelectableButton(mi, a, null);
/* 590 */     configureMenuItemFromExtActionProperties(mi, a);
/* 591 */     return mi;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   private JRadioButtonMenuItem createRadioButtonMenuItem(ButtonGroup group, AbstractActionExt a)
/*     */   {
/* 599 */     JRadioButtonMenuItem mi = new JRadioButtonMenuItem();
/* 600 */     configureSelectableButton(mi, a, group);
/* 601 */     configureMenuItemFromExtActionProperties(mi, a);
/* 602 */     return mi;
/*     */   }
/*     */ }


/* Location:              E:\java\java学习\hutubill\lib\all.jar!\org\jdesktop\swingx\action\ActionContainerFactory.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */
/*     */ package org.jdesktop.swingx.table;
/*     */ 
/*     */ import java.awt.ComponentOrientation;
/*     */ import java.awt.Dimension;
/*     */ import java.awt.Insets;
/*     */ import java.awt.event.ActionEvent;
/*     */ import java.awt.event.ItemEvent;
/*     */ import java.beans.PropertyChangeEvent;
/*     */ import java.beans.PropertyChangeListener;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collections;
/*     */ import java.util.List;
/*     */ import javax.swing.AbstractAction;
/*     */ import javax.swing.Action;
/*     */ import javax.swing.ActionMap;
/*     */ import javax.swing.Icon;
/*     */ import javax.swing.JButton;
/*     */ import javax.swing.JComboBox;
/*     */ import javax.swing.JComponent;
/*     */ import javax.swing.JMenuItem;
/*     */ import javax.swing.JPopupMenu;
/*     */ import javax.swing.SwingUtilities;
/*     */ import javax.swing.UIManager;
/*     */ import javax.swing.event.ChangeEvent;
/*     */ import javax.swing.event.ListSelectionEvent;
/*     */ import javax.swing.event.TableColumnModelEvent;
/*     */ import javax.swing.event.TableColumnModelListener;
/*     */ import javax.swing.plaf.UIResource;
/*     */ import javax.swing.table.TableColumn;
/*     */ import javax.swing.table.TableColumnModel;
/*     */ import org.jdesktop.swingx.JXTable;
/*     */ import org.jdesktop.swingx.action.AbstractActionExt;
/*     */ import org.jdesktop.swingx.action.ActionContainerFactory;
/*     */ import org.jdesktop.swingx.plaf.ColumnControlButtonAddon;
/*     */ import org.jdesktop.swingx.plaf.LookAndFeelAddons;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class ColumnControlButton
/*     */   extends JButton
/*     */ {
/*     */   public static final String COLUMN_CONTROL_MARKER = "column.";
/*     */   public static final String COLUMN_CONTROL_BUTTON_ICON_KEY = "ColumnControlButton.actionIcon";
/*     */   public static final String COLUMN_CONTROL_BUTTON_MARGIN_KEY = "ColumnControlButton.margin";
/*     */   protected ColumnControlPopup popup;
/*     */   private JXTable table;
/*     */   private PropertyChangeListener tablePropertyChangeListener;
/*     */   TableColumnModelListener columnModelListener;
/*     */   private List<ColumnVisibilityAction> columnVisibilityActions;
/*     */   
/*     */   static
/*     */   {
/*  99 */     LookAndFeelAddons.contribute(new ColumnControlButtonAddon());
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
/*     */ 
/*     */   public ColumnControlButton(JXTable table)
/*     */   {
/* 122 */     this(table, null);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public ColumnControlButton(JXTable table, Icon icon)
/*     */   {
/* 133 */     init();
/*     */     
/* 135 */     setAction(createControlAction(icon));
/* 136 */     updateActionUI();
/* 137 */     updateButtonUI();
/* 138 */     installTable(table);
/*     */   }
/*     */   
/*     */ 
/*     */   public void updateUI()
/*     */   {
/* 144 */     super.updateUI();
/*     */     
/* 146 */     updateActionUI();
/* 147 */     updateButtonUI();
/* 148 */     getColumnControlPopup().updateUI();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected void updateButtonUI()
/*     */   {
/* 157 */     if ((getMargin() == null) || ((getMargin() instanceof UIResource))) {
/* 158 */       Insets insets = UIManager.getInsets("ColumnControlButton.margin");
/* 159 */       setMargin(insets);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected void updateActionUI()
/*     */   {
/* 169 */     if (getAction() == null) return;
/* 170 */     Icon icon = (Icon)getAction().getValue("SmallIcon");
/* 171 */     if ((icon == null) || ((icon instanceof UIResource))) {
/* 172 */       icon = UIManager.getIcon("ColumnControlButton.actionIcon");
/* 173 */       getAction().putValue("SmallIcon", icon);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void togglePopup()
/*     */   {
/* 184 */     getColumnControlPopup().toggleVisibility(this);
/*     */   }
/*     */   
/*     */   public void applyComponentOrientation(ComponentOrientation o)
/*     */   {
/* 189 */     super.applyComponentOrientation(o);
/* 190 */     getColumnControlPopup().applyComponentOrientation(o);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public class ColumnVisibilityAction
/*     */     extends AbstractActionExt
/*     */   {
/*     */     private TableColumn column;
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     private PropertyChangeListener columnListener;
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */     private boolean fromColumn;
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     public ColumnVisibilityAction(TableColumn column)
/*     */     {
/* 220 */       super();
/* 221 */       setStateAction();
/* 222 */       installColumn(column);
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     public void releaseColumn()
/*     */     {
/* 232 */       this.column.removePropertyChangeListener(this.columnListener);
/* 233 */       this.column = null;
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     public boolean isEnabled()
/*     */     {
/* 246 */       return (super.isEnabled()) && (canControlColumn());
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     protected boolean canControlColumn()
/*     */     {
/* 259 */       return this.column instanceof TableColumnExt;
/*     */     }
/*     */     
/*     */     public void itemStateChanged(ItemEvent e)
/*     */     {
/* 264 */       if (canControlColumn()) {
/* 265 */         if ((e.getStateChange() == 2) && (ColumnControlButton.this.table.getColumnCount() <= 1) && (!this.fromColumn))
/*     */         {
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 277 */           reselect();
/*     */         } else {
/* 279 */           setSelected(e.getStateChange() == 1);
/*     */         }
/*     */       }
/*     */     }
/*     */     
/*     */ 
/*     */     public synchronized void setSelected(boolean newValue)
/*     */     {
/* 287 */       super.setSelected(newValue);
/* 288 */       if (canControlColumn()) {
/* 289 */         ((TableColumnExt)this.column).setVisible(newValue);
/*     */       }
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     public void actionPerformed(ActionEvent e) {}
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     private void updateFromColumnVisible(boolean visible)
/*     */     {
/* 312 */       this.fromColumn = true;
/* 313 */       setSelected(visible);
/* 314 */       this.fromColumn = false;
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     private void updateFromColumnHeader(Object value)
/*     */     {
/* 324 */       setName(String.valueOf(value));
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     private void reselect()
/*     */     {
/* 333 */       firePropertyChange("selected", null, Boolean.TRUE);
/*     */     }
/*     */     
/*     */     private void installColumn(TableColumn column)
/*     */     {
/* 338 */       this.column = column;
/* 339 */       column.addPropertyChangeListener(getColumnListener());
/* 340 */       updateFromColumnHeader(column.getHeaderValue());
/*     */       
/* 342 */       if (column.getIdentifier() != null) {
/* 343 */         setActionCommand(column.getIdentifier().toString());
/*     */       }
/* 345 */       boolean visible = (column instanceof TableColumnExt) ? ((TableColumnExt)column).isVisible() : true;
/*     */       
/* 347 */       updateFromColumnVisible(visible);
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     protected PropertyChangeListener getColumnListener()
/*     */     {
/* 359 */       if (this.columnListener == null) {
/* 360 */         this.columnListener = createPropertyChangeListener();
/*     */       }
/* 362 */       return this.columnListener;
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     protected PropertyChangeListener createPropertyChangeListener()
/*     */     {
/* 377 */       new PropertyChangeListener() {
/*     */         public void propertyChange(PropertyChangeEvent evt) {
/* 379 */           if ("visible".equals(evt.getPropertyName())) {
/* 380 */             ColumnControlButton.ColumnVisibilityAction.this.updateFromColumnVisible(((Boolean)evt.getNewValue()).booleanValue());
/* 381 */           } else if ("headerValue".equals(evt.getPropertyName())) {
/* 382 */             ColumnControlButton.ColumnVisibilityAction.this.updateFromColumnHeader(evt.getNewValue());
/*     */           }
/*     */         }
/*     */       };
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public class DefaultColumnControlPopup
/*     */     implements ColumnControlPopup
/*     */   {
/*     */     private JPopupMenu popupMenu;
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     public DefaultColumnControlPopup() {}
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */     public void updateUI()
/*     */     {
/* 409 */       SwingUtilities.updateComponentTreeUI(getPopupMenu());
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */     public void toggleVisibility(JComponent owner)
/*     */     {
/* 417 */       JPopupMenu popupMenu = getPopupMenu();
/* 418 */       if (popupMenu.isVisible()) {
/* 419 */         popupMenu.setVisible(false);
/* 420 */       } else if (popupMenu.getComponentCount() > 0) {
/* 421 */         Dimension buttonSize = owner.getSize();
/* 422 */         int xPos = owner.getComponentOrientation().isLeftToRight() ? buttonSize.width - popupMenu.getPreferredSize().width : 0;
/*     */         
/*     */ 
/* 425 */         popupMenu.show(owner, xPos, buttonSize.height);
/*     */       }
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     public void applyComponentOrientation(ComponentOrientation o)
/*     */     {
/* 439 */       getPopupMenu().applyComponentOrientation(o);
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     public void removeAll()
/*     */     {
/* 450 */       getPopupMenu().removeAll();
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     public void addVisibilityActionItems(List<? extends AbstractActionExt> actions)
/*     */     {
/* 460 */       addItems(new ArrayList(actions));
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     public void addAdditionalActionItems(List<? extends Action> actions)
/*     */     {
/* 470 */       if (actions.size() == 0) {
/* 471 */         return;
/*     */       }
/*     */       
/*     */ 
/* 475 */       if (ColumnControlButton.this.canControl()) {
/* 476 */         addSeparator();
/*     */       }
/* 478 */       addItems(actions);
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     protected void addItems(List<? extends Action> actions)
/*     */     {
/* 495 */       ActionContainerFactory factory = new ActionContainerFactory(null);
/* 496 */       for (Action action : actions) {
/* 497 */         addItem(factory.createMenuItem(action));
/*     */       }
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     protected void addSeparator()
/*     */     {
/* 507 */       getPopupMenu().addSeparator();
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */     protected void addItem(JMenuItem item)
/*     */     {
/* 515 */       getPopupMenu().add(item);
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */     protected JPopupMenu getPopupMenu()
/*     */     {
/* 523 */       if (this.popupMenu == null) {
/* 524 */         this.popupMenu = new JPopupMenu();
/*     */       }
/* 526 */       return this.popupMenu;
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
/*     */   protected ColumnControlPopup getColumnControlPopup()
/*     */   {
/* 541 */     if (this.popup == null) {
/* 542 */       this.popup = createColumnControlPopup();
/*     */     }
/* 544 */     return this.popup;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected ColumnControlPopup createColumnControlPopup()
/*     */   {
/* 554 */     return new DefaultColumnControlPopup();
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
/*     */   protected void updateFromColumnModelChange(TableColumnModel oldModel)
/*     */   {
/* 569 */     if (oldModel != null) {
/* 570 */       oldModel.removeColumnModelListener(this.columnModelListener);
/*     */     }
/* 572 */     populatePopup();
/* 573 */     if (canControl()) {
/* 574 */       this.table.getColumnModel().addColumnModelListener(getColumnModelListener());
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   protected void updateFromTableEnabledChanged()
/*     */   {
/* 583 */     getAction().setEnabled(this.table.isEnabled());
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected boolean canControl()
/*     */   {
/* 594 */     return this.table.getColumnModel() instanceof TableColumnModelExt;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected void populatePopup()
/*     */   {
/* 605 */     clearAll();
/* 606 */     if (canControl()) {
/* 607 */       createVisibilityActions();
/* 608 */       addVisibilityActionItems();
/*     */     }
/* 610 */     addAdditionalActionItems();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected void clearAll()
/*     */   {
/* 620 */     clearColumnVisibilityActions();
/* 621 */     getColumnControlPopup().removeAll();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected void clearColumnVisibilityActions()
/*     */   {
/* 630 */     if (this.columnVisibilityActions == null)
/* 631 */       return;
/* 632 */     for (ColumnVisibilityAction action : this.columnVisibilityActions) {
/* 633 */       action.releaseColumn();
/*     */     }
/* 635 */     this.columnVisibilityActions.clear();
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
/*     */   protected void addVisibilityActionItems()
/*     */   {
/* 648 */     getColumnControlPopup().addVisibilityActionItems(Collections.unmodifiableList(getColumnVisibilityActions()));
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected void addAdditionalActionItems()
/*     */   {
/* 660 */     getColumnControlPopup().addAdditionalActionItems(Collections.unmodifiableList(getAdditionalActions()));
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
/*     */   protected void createVisibilityActions()
/*     */   {
/* 679 */     List<TableColumn> columns = this.table.getColumns(true);
/* 680 */     for (TableColumn column : columns) {
/* 681 */       ColumnVisibilityAction action = createColumnVisibilityAction(column);
/* 682 */       if (action != null) {
/* 683 */         getColumnVisibilityActions().add(action);
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
/*     */   protected ColumnVisibilityAction createColumnVisibilityAction(TableColumn column)
/*     */   {
/* 699 */     return new ColumnVisibilityAction(column);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected List<ColumnVisibilityAction> getColumnVisibilityActions()
/*     */   {
/* 708 */     if (this.columnVisibilityActions == null) {
/* 709 */       this.columnVisibilityActions = new ArrayList();
/*     */     }
/* 711 */     return this.columnVisibilityActions;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected List<Action> getAdditionalActions()
/*     */   {
/* 723 */     List<?> actionKeys = getColumnControlActionKeys();
/* 724 */     List<Action> actions = new ArrayList();
/* 725 */     for (Object key : actionKeys) {
/* 726 */       actions.add(this.table.getActionMap().get(key));
/*     */     }
/* 728 */     return actions;
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
/*     */   protected List getColumnControlActionKeys()
/*     */   {
/* 743 */     Object[] allKeys = this.table.getActionMap().allKeys();
/* 744 */     List columnKeys = new ArrayList();
/* 745 */     for (int i = 0; i < allKeys.length; i++) {
/* 746 */       if (isColumnControlActionKey(allKeys[i])) {
/* 747 */         columnKeys.add(allKeys[i]);
/*     */       }
/*     */     }
/*     */     
/*     */ 
/*     */ 
/* 753 */     Collections.sort(columnKeys);
/* 754 */     return columnKeys;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected boolean isColumnControlActionKey(Object actionKey)
/*     */   {
/* 766 */     return ((actionKey instanceof String)) && (((String)actionKey).startsWith("column."));
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   private void installTable(JXTable table)
/*     */   {
/* 774 */     this.table = table;
/* 775 */     table.addPropertyChangeListener(getTablePropertyChangeListener());
/* 776 */     updateFromColumnModelChange(null);
/* 777 */     updateFromTableEnabledChanged();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   private void init()
/*     */   {
/* 785 */     setFocusPainted(false);
/* 786 */     setFocusable(false);
/*     */     
/*     */ 
/* 789 */     JComboBox box = new JComboBox();
/* 790 */     Object preventHide = box.getClientProperty("doNotCancelPopup");
/* 791 */     putClientProperty("doNotCancelPopup", preventHide);
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
/*     */   private Action createControlAction(Icon icon)
/*     */   {
/* 804 */     Action control = new AbstractAction()
/*     */     {
/*     */       public void actionPerformed(ActionEvent e) {
/* 807 */         ColumnControlButton.this.togglePopup();
/*     */       }
/*     */       
/* 810 */     };
/* 811 */     control.putValue("SmallIcon", icon);
/* 812 */     return control;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected PropertyChangeListener getTablePropertyChangeListener()
/*     */   {
/* 824 */     if (this.tablePropertyChangeListener == null) {
/* 825 */       this.tablePropertyChangeListener = createTablePropertyChangeListener();
/*     */     }
/* 827 */     return this.tablePropertyChangeListener;
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
/*     */   protected PropertyChangeListener createTablePropertyChangeListener()
/*     */   {
/* 840 */     new PropertyChangeListener() {
/*     */       public void propertyChange(PropertyChangeEvent evt) {
/* 842 */         if ("columnModel".equals(evt.getPropertyName())) {
/* 843 */           ColumnControlButton.this.updateFromColumnModelChange((TableColumnModel)evt.getOldValue());
/*     */         }
/* 845 */         else if ("enabled".equals(evt.getPropertyName())) {
/* 846 */           ColumnControlButton.this.updateFromTableEnabledChanged();
/*     */         }
/*     */       }
/*     */     };
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected TableColumnModelListener getColumnModelListener()
/*     */   {
/* 859 */     if (this.columnModelListener == null) {
/* 860 */       this.columnModelListener = createColumnModelListener();
/*     */     }
/* 862 */     return this.columnModelListener;
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
/*     */   protected TableColumnModelListener createColumnModelListener()
/*     */   {
/* 876 */     new TableColumnModelListener()
/*     */     {
/*     */       public void columnAdded(TableColumnModelEvent e)
/*     */       {
/* 880 */         if (!isVisibilityChange(e, true)) {
/* 881 */           ColumnControlButton.this.populatePopup();
/*     */         }
/*     */       }
/*     */       
/*     */       public void columnRemoved(TableColumnModelEvent e)
/*     */       {
/* 887 */         if (!isVisibilityChange(e, false)) {
/* 888 */           ColumnControlButton.this.populatePopup();
/*     */         }
/*     */       }
/*     */       
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       private boolean isVisibilityChange(TableColumnModelEvent e, boolean added)
/*     */       {
/* 907 */         if (!(e.getSource() instanceof DefaultTableColumnModelExt))
/* 908 */           return false;
/* 909 */         DefaultTableColumnModelExt model = (DefaultTableColumnModelExt)e.getSource();
/*     */         
/* 911 */         if (added) {
/* 912 */           return model.isAddedFromInvisibleEvent(e.getToIndex());
/*     */         }
/* 914 */         return model.isRemovedToInvisibleEvent(e.getFromIndex());
/*     */       }
/*     */       
/*     */       public void columnMoved(TableColumnModelEvent e) {}
/*     */       
/*     */       public void columnMarginChanged(ChangeEvent e) {}
/*     */       
/*     */       public void columnSelectionChanged(ListSelectionEvent e) {}
/*     */     };
/*     */   }
/*     */ }


/* Location:              E:\java\java学习\hutubill\lib\all.jar!\org\jdesktop\swingx\table\ColumnControlButton.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */
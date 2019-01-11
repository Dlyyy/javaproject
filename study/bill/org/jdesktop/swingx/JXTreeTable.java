/*      */ package org.jdesktop.swingx;
/*      */ 
/*      */ import java.awt.Color;
/*      */ import java.awt.Component;
/*      */ import java.awt.ComponentOrientation;
/*      */ import java.awt.Graphics;
/*      */ import java.awt.Point;
/*      */ import java.awt.Rectangle;
/*      */ import java.awt.event.ActionEvent;
/*      */ import java.awt.event.MouseEvent;
/*      */ import java.beans.PropertyChangeEvent;
/*      */ import java.beans.PropertyChangeListener;
/*      */ import java.util.ArrayList;
/*      */ import java.util.Enumeration;
/*      */ import java.util.EventObject;
/*      */ import java.util.List;
/*      */ import java.util.logging.Logger;
/*      */ import javax.swing.ActionMap;
/*      */ import javax.swing.DefaultListSelectionModel;
/*      */ import javax.swing.Icon;
/*      */ import javax.swing.JComponent;
/*      */ import javax.swing.JTable;
/*      */ import javax.swing.JTree;
/*      */ import javax.swing.ListSelectionModel;
/*      */ import javax.swing.RowSorter;
/*      */ import javax.swing.SwingUtilities;
/*      */ import javax.swing.UIManager;
/*      */ import javax.swing.border.Border;
/*      */ import javax.swing.event.ChangeEvent;
/*      */ import javax.swing.event.ListSelectionEvent;
/*      */ import javax.swing.event.ListSelectionListener;
/*      */ import javax.swing.event.TableModelEvent;
/*      */ import javax.swing.event.TreeExpansionEvent;
/*      */ import javax.swing.event.TreeExpansionListener;
/*      */ import javax.swing.event.TreeModelEvent;
/*      */ import javax.swing.event.TreeModelListener;
/*      */ import javax.swing.event.TreeSelectionListener;
/*      */ import javax.swing.event.TreeWillExpandListener;
/*      */ import javax.swing.plaf.basic.BasicTreeUI;
/*      */ import javax.swing.table.AbstractTableModel;
/*      */ import javax.swing.table.TableCellEditor;
/*      */ import javax.swing.table.TableCellRenderer;
/*      */ import javax.swing.table.TableColumn;
/*      */ import javax.swing.table.TableColumnModel;
/*      */ import javax.swing.table.TableModel;
/*      */ import javax.swing.tree.DefaultTreeCellRenderer;
/*      */ import javax.swing.tree.DefaultTreeSelectionModel;
/*      */ import javax.swing.tree.TreeCellRenderer;
/*      */ import javax.swing.tree.TreeModel;
/*      */ import javax.swing.tree.TreePath;
/*      */ import javax.swing.tree.TreeSelectionModel;
/*      */ import org.jdesktop.swingx.decorator.ComponentAdapter;
/*      */ import org.jdesktop.swingx.event.TreeExpansionBroadcaster;
/*      */ import org.jdesktop.swingx.renderer.StringValue;
/*      */ import org.jdesktop.swingx.renderer.StringValues;
/*      */ import org.jdesktop.swingx.rollover.RolloverProducer;
/*      */ import org.jdesktop.swingx.rollover.RolloverRenderer;
/*      */ import org.jdesktop.swingx.tree.DefaultXTreeCellRenderer;
/*      */ import org.jdesktop.swingx.treetable.DefaultTreeTableModel;
/*      */ import org.jdesktop.swingx.treetable.TreeTableCellEditor;
/*      */ import org.jdesktop.swingx.treetable.TreeTableModel;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ public class JXTreeTable
/*      */   extends JXTable
/*      */ {
/*  123 */   private static final Logger LOG = Logger.getLogger(JXTreeTable.class.getName());
/*      */   
/*      */ 
/*      */ 
/*      */   public static final String DRAG_HACK_FLAG_KEY = "treeTable.dragHackFlag";
/*      */   
/*      */ 
/*      */ 
/*      */   public static final String DROP_HACK_FLAG_KEY = "treeTable.dropHackFlag";
/*      */   
/*      */ 
/*      */ 
/*      */   private TreeTableCellRenderer renderer;
/*      */   
/*      */ 
/*      */ 
/*      */   private TreeTableCellEditor hierarchicalEditor;
/*      */   
/*      */ 
/*      */ 
/*      */   private TreeTableHacker treeTableHacker;
/*      */   
/*      */ 
/*      */   private boolean consumedOnPress;
/*      */   
/*      */ 
/*      */   private TreeExpansionBroadcaster treeExpansionBroadcaster;
/*      */   
/*      */ 
/*      */ 
/*      */   public JXTreeTable()
/*      */   {
/*  155 */     this(new DefaultTreeTableModel());
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public JXTreeTable(TreeTableModel treeModel)
/*      */   {
/*  165 */     this(new TreeTableCellRenderer(treeModel));
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   private JXTreeTable(TreeTableCellRenderer renderer)
/*      */   {
/*  183 */     super(new TreeTableModelAdapter(renderer));
/*      */     
/*      */ 
/*  186 */     init(renderer);
/*  187 */     initActions();
/*      */     
/*  189 */     super.setSortable(false);
/*  190 */     super.setAutoCreateRowSorter(false);
/*  191 */     super.setRowSorter(null);
/*      */     
/*  193 */     setShowGrid(false, false);
/*      */     
/*  195 */     this.hierarchicalEditor = new TreeTableCellEditor(renderer);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   private void init(TreeTableCellRenderer renderer)
/*      */   {
/*  213 */     this.renderer = renderer;
/*  214 */     assert (((TreeTableModelAdapter)getModel()).tree == this.renderer);
/*      */     
/*      */ 
/*  217 */     ListToTreeSelectionModelWrapper selectionWrapper = new ListToTreeSelectionModelWrapper();
/*      */     
/*      */ 
/*      */ 
/*  221 */     if (renderer != null) {
/*  222 */       renderer.bind(this);
/*  223 */       renderer.setSelectionModel(selectionWrapper);
/*      */     }
/*      */     
/*  226 */     adjustTreeRowHeight(getRowHeight());
/*  227 */     adjustTreeBounds();
/*  228 */     setSelectionModel(selectionWrapper.getListSelectionModel());
/*      */     
/*      */ 
/*  231 */     PropertyChangeListener l = new PropertyChangeListener()
/*      */     {
/*      */       public void propertyChange(PropertyChangeEvent evt) {
/*  234 */         JXTreeTable.this.renderer.putClientProperty(evt.getPropertyName(), evt.getNewValue());
/*      */       }
/*      */       
/*      */ 
/*  238 */     };
/*  239 */     addPropertyChangeListener("JTree.lineStyle", l);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   private void initActions()
/*      */   {
/*  246 */     ActionMap map = getActionMap();
/*  247 */     map.put("expand-all", new Actions("expand-all"));
/*  248 */     map.put("collapse-all", new Actions("collapse-all"));
/*      */   }
/*      */   
/*      */   public void setSortable(boolean sortable) {}
/*      */   
/*      */   public void setAutoCreateRowSorter(boolean autoCreateRowSorter) {}
/*      */   
/*      */   public void setRowSorter(RowSorter<? extends TableModel> sorter) {}
/*      */   
/*  257 */   private class Actions extends UIAction { Actions(String name) { super(); }
/*      */     
/*      */     public void actionPerformed(ActionEvent evt)
/*      */     {
/*  261 */       if ("expand-all".equals(getName())) {
/*  262 */         JXTreeTable.this.expandAll();
/*      */       }
/*  264 */       else if ("collapse-all".equals(getName())) {
/*  265 */         JXTreeTable.this.collapseAll();
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void setEnabled(boolean enabled)
/*      */   {
/*  319 */     this.renderer.setEnabled(enabled);
/*  320 */     super.setEnabled(enabled);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void setSelectionBackground(Color selectionBackground)
/*      */   {
/*  332 */     if (this.renderer != null)
/*  333 */       this.renderer.setSelectionBackground(selectionBackground);
/*  334 */     super.setSelectionBackground(selectionBackground);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void setSelectionForeground(Color selectionForeground)
/*      */   {
/*  345 */     if (this.renderer != null)
/*  346 */       this.renderer.setSelectionForeground(selectionForeground);
/*  347 */     super.setSelectionForeground(selectionForeground);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public boolean editCellAt(int row, int column, EventObject e)
/*      */   {
/*  364 */     getTreeTableHacker().hitHandleDetectionFromEditCell(column, e);
/*  365 */     boolean canEdit = super.editCellAt(row, column, e);
/*  366 */     if ((canEdit) && (isHierarchical(column))) {
/*  367 */       repaint(getCellRect(row, column, false));
/*      */     }
/*  369 */     return canEdit;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected void processMouseEvent(MouseEvent e)
/*      */   {
/*  381 */     if ((e.getID() == 502) && (this.consumedOnPress)) {
/*  382 */       this.consumedOnPress = false;
/*  383 */       e.consume();
/*  384 */       return;
/*      */     }
/*  386 */     if (getTreeTableHacker().hitHandleDetectionFromProcessMouse(e))
/*      */     {
/*      */ 
/*      */ 
/*      */ 
/*  391 */       this.consumedOnPress = true;
/*  392 */       e.consume();
/*  393 */       return;
/*      */     }
/*  395 */     this.consumedOnPress = false;
/*  396 */     super.processMouseEvent(e);
/*      */   }
/*      */   
/*      */   protected TreeTableHacker getTreeTableHacker()
/*      */   {
/*  401 */     if (this.treeTableHacker == null) {
/*  402 */       this.treeTableHacker = createTreeTableHacker();
/*      */     }
/*  404 */     return this.treeTableHacker;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected TreeTableHacker createTreeTableHacker()
/*      */   {
/*  422 */     return new TreeTableHackerExt5();
/*      */   }
/*      */   
/*  425 */   private boolean processMouseMotion = true;
/*      */   
/*      */   protected void processMouseMotionEvent(MouseEvent e)
/*      */   {
/*  429 */     if (this.processMouseMotion) {
/*  430 */       super.processMouseMotionEvent(e);
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public class TreeTableHackerExt4
/*      */     extends JXTreeTable.TreeTableHackerExt
/*      */   {
/*      */     public TreeTableHackerExt4()
/*      */     {
/*  491 */       super();
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     protected boolean isTreeHandleEventType(MouseEvent e)
/*      */     {
/*  502 */       switch (e.getID()) {
/*      */       case 500: 
/*      */       case 501: 
/*      */       case 502: 
/*  506 */         return !e.isPopupTrigger();
/*      */       }
/*  508 */       return false;
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     protected MouseEvent getEventForTreeRenderer(MouseEvent e)
/*      */     {
/*  520 */       Point pt = e.getPoint();
/*  521 */       int col = JXTreeTable.this.columnAtPoint(pt);
/*  522 */       if ((col >= 0) && (JXTreeTable.this.isHierarchical(col))) {
/*  523 */         int row = JXTreeTable.this.rowAtPoint(pt);
/*  524 */         if (row >= 0)
/*      */         {
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  530 */           Rectangle cellBounds = JXTreeTable.this.getCellRect(row, col, false);
/*  531 */           int x = e.getX() - cellBounds.x;
/*  532 */           Rectangle nodeBounds = JXTreeTable.this.renderer.getRowBounds(row);
/*      */           
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  540 */           if (JXTreeTable.this.renderer.getComponentOrientation().isLeftToRight() ? x < nodeBounds.x : x > nodeBounds.x + nodeBounds.width)
/*      */           {
/*  542 */             return new MouseEvent(JXTreeTable.this.renderer, e.getID(), e.getWhen(), e.getModifiers(), x, e.getY(), e.getXOnScreen(), e.getYOnScreen(), e.getClickCount(), false, e.getButton());
/*      */           }
/*      */         }
/*      */       }
/*      */       
/*      */ 
/*      */ 
/*  549 */       return null;
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     public boolean hitHandleDetectionFromProcessMouse(MouseEvent e)
/*      */     {
/*  559 */       if (!isHitDetectionFromProcessMouse())
/*  560 */         return false;
/*  561 */       if (isTreeHandleEventType(e)) {
/*  562 */         MouseEvent newE = getEventForTreeRenderer(e);
/*  563 */         if (newE != null) {
/*  564 */           JXTreeTable.this.renderer.dispatchEvent(newE);
/*  565 */           if (JXTreeTable.this.processMouseMotion)
/*      */           {
/*      */ 
/*      */ 
/*  569 */             JXTreeTable.this.processMouseMotion = false;
/*      */             
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  575 */             JXTreeTable.this.requestFocusInWindow();
/*      */           }
/*  577 */           e.consume();
/*      */           
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  583 */           return false;
/*      */         }
/*      */       }
/*      */       
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  593 */       JXTreeTable.this.processMouseMotion = true;
/*  594 */       return false;
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public class TreeTableHackerExt5
/*      */     extends JXTreeTable.TreeTableHackerExt4
/*      */   {
/*      */     public TreeTableHackerExt5()
/*      */     {
/*  605 */       super();
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     protected int getTreeHandleWidth()
/*      */     {
/*  614 */       if ((JXTreeTable.this.renderer.getUI() instanceof BasicTreeUI)) {
/*  615 */         BasicTreeUI ui = (BasicTreeUI)JXTreeTable.this.renderer.getUI();
/*  616 */         return ui.getLeftChildIndent() + ui.getRightChildIndent();
/*      */       }
/*  618 */       return -1;
/*      */     }
/*      */     
/*      */ 
/*      */     protected MouseEvent getEventForTreeRenderer(MouseEvent e)
/*      */     {
/*  624 */       Point pt = e.getPoint();
/*  625 */       int col = JXTreeTable.this.columnAtPoint(pt);
/*  626 */       if ((col >= 0) && (JXTreeTable.this.isHierarchical(col))) {
/*  627 */         int row = JXTreeTable.this.rowAtPoint(pt);
/*      */         
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  633 */         if (row >= 0) {
/*  634 */           TreePath path = JXTreeTable.this.getPathForRow(row);
/*  635 */           Object node = path.getLastPathComponent();
/*      */           
/*      */ 
/*  638 */           if ((!JXTreeTable.this.getTreeTableModel().isLeaf(node)) && ((JXTreeTable.this.getTreeTableModel().getChildCount(node) > 0) || (!JXTreeTable.this.renderer.hasBeenExpanded(path))))
/*      */           {
/*      */ 
/*  641 */             Rectangle cellBounds = JXTreeTable.this.getCellRect(row, col, false);
/*  642 */             int x = e.getX() - cellBounds.x;
/*  643 */             Rectangle nb = JXTreeTable.this.renderer.getRowBounds(row);
/*  644 */             int thw = getTreeHandleWidth();
/*      */             
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  652 */             if (JXTreeTable.this.renderer.getComponentOrientation().isLeftToRight() ? (x >= nb.x) && ((thw >= 0) || (x <= nb.x - thw)) : (x > nb.x + nb.width) && ((thw < 0) || (x < nb.x + nb.width + thw)))
/*      */             {
/*      */ 
/*      */ 
/*      */ 
/*  657 */               return new MouseEvent(JXTreeTable.this.renderer, e.getID(), e.getWhen(), e.getModifiers(), x, e.getY(), e.getXOnScreen(), e.getYOnScreen(), e.getClickCount(), false, e.getButton());
/*      */             }
/*      */           }
/*      */         }
/*      */       }
/*      */       
/*      */ 
/*      */ 
/*      */ 
/*  666 */       return null;
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public class TreeTableHacker
/*      */   {
/*      */     protected boolean expansionChangedFlag;
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     public TreeTableHacker() {}
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     protected boolean isHitDetectionFromProcessMouse()
/*      */     {
/*  698 */       return false;
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     public void hitHandleDetectionFromEditCell(int column, EventObject e)
/*      */     {
/*  708 */       if (!isHitDetectionFromProcessMouse()) {
/*  709 */         expandOrCollapseNode(column, e);
/*      */       }
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     public boolean hitHandleDetectionFromProcessMouse(MouseEvent e)
/*      */     {
/*  723 */       if (!isHitDetectionFromProcessMouse())
/*  724 */         return false;
/*  725 */       int col = JXTreeTable.this.columnAtPoint(e.getPoint());
/*  726 */       return (col >= 0) && (expandOrCollapseNode(JXTreeTable.this.columnAtPoint(e.getPoint()), e));
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     protected void completeEditing()
/*      */     {
/*  751 */       if (JXTreeTable.this.isEditing()) {
/*  752 */         boolean success = JXTreeTable.this.getCellEditor().stopCellEditing();
/*  753 */         if (!success) {
/*  754 */           JXTreeTable.this.getCellEditor().cancelCellEditing();
/*      */         }
/*      */       }
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     protected boolean expandOrCollapseNode(int column, EventObject e)
/*      */     {
/*  785 */       if (!JXTreeTable.this.isHierarchical(column))
/*  786 */         return false;
/*  787 */       if (!mightBeExpansionTrigger(e))
/*  788 */         return false;
/*  789 */       boolean changedExpansion = false;
/*  790 */       MouseEvent me = (MouseEvent)e;
/*  791 */       if (JXTreeTable.this.hackAroundDragEnabled(me))
/*      */       {
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  799 */         me = new MouseEvent((Component)me.getSource(), 501, me.getWhen(), me.getModifiers(), me.getX(), me.getY(), me.getClickCount(), me.isPopupTrigger());
/*      */       }
/*      */       
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  811 */       if ((me.getModifiers() == 0) || (me.getModifiers() == 16))
/*      */       {
/*  813 */         MouseEvent pressed = new MouseEvent(JXTreeTable.this.renderer, me.getID(), me.getWhen(), me.getModifiers(), me.getX() - JXTreeTable.this.getCellRect(0, column, false).x, me.getY(), me.getClickCount(), me.isPopupTrigger());
/*      */         
/*      */ 
/*      */ 
/*  817 */         JXTreeTable.this.renderer.dispatchEvent(pressed);
/*      */         
/*  819 */         MouseEvent released = new MouseEvent(JXTreeTable.this.renderer, 502, pressed.getWhen(), pressed.getModifiers(), pressed.getX(), pressed.getY(), pressed.getClickCount(), pressed.isPopupTrigger());
/*      */         
/*      */ 
/*      */ 
/*      */ 
/*  824 */         JXTreeTable.this.renderer.dispatchEvent(released);
/*  825 */         if (this.expansionChangedFlag) {
/*  826 */           changedExpansion = true;
/*      */         }
/*      */       }
/*  829 */       this.expansionChangedFlag = false;
/*  830 */       return changedExpansion;
/*      */     }
/*      */     
/*      */     protected boolean mightBeExpansionTrigger(EventObject e) {
/*  834 */       if (!(e instanceof MouseEvent)) return false;
/*  835 */       MouseEvent me = (MouseEvent)e;
/*  836 */       if (!SwingUtilities.isLeftMouseButton(me)) return false;
/*  837 */       return me.getID() == 501;
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     protected void expansionChanged()
/*      */     {
/*  846 */       this.expansionChangedFlag = true;
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public class TreeTableHackerExt
/*      */     extends JXTreeTable.TreeTableHacker
/*      */   {
/*      */     public TreeTableHackerExt()
/*      */     {
/*  862 */       super();
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     protected boolean isHitDetectionFromProcessMouse()
/*      */     {
/*  871 */       return true;
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public class TreeTableHackerExt2
/*      */     extends JXTreeTable.TreeTableHackerExt
/*      */   {
/*  888 */     public TreeTableHackerExt2() { super(); }
/*      */     
/*      */     protected boolean expandOrCollapseNode(int column, EventObject e) {
/*  891 */       if (!JXTreeTable.this.isHierarchical(column))
/*  892 */         return false;
/*  893 */       if (!mightBeExpansionTrigger(e))
/*  894 */         return false;
/*  895 */       boolean changedExpansion = false;
/*  896 */       MouseEvent me = (MouseEvent)e;
/*  897 */       if (JXTreeTable.this.hackAroundDragEnabled(me))
/*      */       {
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  905 */         me = new MouseEvent((Component)me.getSource(), 501, me.getWhen(), me.getModifiers(), me.getX(), me.getY(), me.getClickCount(), me.isPopupTrigger());
/*      */       }
/*      */       
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  916 */       if ((me.getModifiers() == 0) || (me.getModifiers() == 16))
/*      */       {
/*      */ 
/*      */ 
/*      */ 
/*  921 */         Point treeMousePoint = getTreeMousePoint(column, me);
/*  922 */         int treeRow = JXTreeTable.this.renderer.getRowForLocation(treeMousePoint.x, treeMousePoint.y);
/*      */         
/*  924 */         int row = 0;
/*      */         
/*  926 */         if (treeRow < 0)
/*      */         {
/*  928 */           row = JXTreeTable.this.renderer.getClosestRowForLocation(treeMousePoint.x, treeMousePoint.y);
/*      */           
/*      */ 
/*  931 */           Rectangle bounds = JXTreeTable.this.renderer.getRowBounds(row);
/*  932 */           if (bounds == null) {
/*  933 */             row = -1;
/*      */ 
/*      */ 
/*      */ 
/*      */           }
/*  938 */           else if (JXTreeTable.this.getComponentOrientation().isLeftToRight())
/*      */           {
/*  940 */             if ((bounds.y + bounds.height < treeMousePoint.y) || (bounds.x > treeMousePoint.x))
/*      */             {
/*  942 */               row = -1;
/*      */             }
/*      */           }
/*  945 */           else if ((bounds.y + bounds.height < treeMousePoint.y) || (bounds.x + bounds.width < treeMousePoint.x))
/*      */           {
/*  947 */             row = -1;
/*      */           }
/*      */           
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  954 */           this.expansionChangedFlag = false;
/*      */         }
/*      */         
/*  957 */         if ((treeRow >= 0) || ((treeRow < 0) && (row < 0)))
/*      */         {
/*  959 */           if (treeRow >= 0) {
/*  960 */             JXTreeTable.this.getColumnModel().getSelectionModel().setLeadSelectionIndex(column);
/*      */           }
/*      */           
/*      */ 
/*      */ 
/*  965 */           MouseEvent pressed = new MouseEvent(JXTreeTable.this.renderer, me.getID(), me.getWhen(), me.getModifiers(), treeMousePoint.x, treeMousePoint.y, me.getClickCount(), me.isPopupTrigger());
/*      */           
/*      */ 
/*      */ 
/*  969 */           JXTreeTable.this.renderer.dispatchEvent(pressed);
/*      */           
/*      */ 
/*  972 */           MouseEvent released = new MouseEvent(JXTreeTable.this.renderer, 502, pressed.getWhen(), pressed.getModifiers(), pressed.getX(), pressed.getY(), pressed.getClickCount(), pressed.isPopupTrigger());
/*      */           
/*      */ 
/*      */ 
/*      */ 
/*  977 */           JXTreeTable.this.renderer.dispatchEvent(released);
/*      */           
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  983 */           JXTreeTable.this.requestFocusInWindow();
/*      */         }
/*  985 */         if (this.expansionChangedFlag) {
/*  986 */           changedExpansion = true;
/*      */         }
/*      */       }
/*      */       
/*  990 */       this.expansionChangedFlag = false;
/*  991 */       return changedExpansion;
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     protected Point getTreeMousePoint(int column, MouseEvent me)
/*      */     {
/* 1011 */       return new Point(me.getX() - JXTreeTable.this.getCellRect(0, column, false).x, me.getY());
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public class TreeTableHackerExt3
/*      */     extends JXTreeTable.TreeTableHackerExt2
/*      */   {
/* 1033 */     public TreeTableHackerExt3() { super(); }
/*      */     
/*      */     protected boolean expandOrCollapseNode(int column, EventObject e) {
/* 1036 */       if (!JXTreeTable.this.isHierarchical(column))
/* 1037 */         return false;
/* 1038 */       if (!mightBeExpansionTrigger(e))
/* 1039 */         return false;
/* 1040 */       boolean changedExpansion = false;
/* 1041 */       MouseEvent me = (MouseEvent)e;
/*      */       
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 1048 */       if ((me.getModifiers() == 0) || (me.getModifiers() == 16))
/*      */       {
/*      */ 
/*      */ 
/*      */ 
/* 1053 */         Point treeMousePoint = getTreeMousePoint(column, me);
/* 1054 */         int treeRow = JXTreeTable.this.renderer.getRowForLocation(treeMousePoint.x, treeMousePoint.y);
/*      */         
/* 1056 */         int row = 0;
/*      */         
/* 1058 */         if (treeRow < 0)
/*      */         {
/* 1060 */           row = JXTreeTable.this.renderer.getClosestRowForLocation(treeMousePoint.x, treeMousePoint.y);
/*      */           
/*      */ 
/* 1063 */           Rectangle bounds = JXTreeTable.this.renderer.getRowBounds(row);
/* 1064 */           if (bounds == null) {
/* 1065 */             row = -1;
/*      */ 
/*      */ 
/*      */ 
/*      */           }
/* 1070 */           else if (JXTreeTable.this.getComponentOrientation().isLeftToRight())
/*      */           {
/* 1072 */             if ((bounds.y + bounds.height < treeMousePoint.y) || (bounds.x > treeMousePoint.x))
/*      */             {
/* 1074 */               row = -1;
/*      */             }
/*      */           }
/* 1077 */           else if ((bounds.y + bounds.height < treeMousePoint.y) || (bounds.x + bounds.width < treeMousePoint.x))
/*      */           {
/* 1079 */             row = -1;
/*      */           }
/*      */         }
/*      */         
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 1087 */         this.expansionChangedFlag = false;
/*      */         
/* 1089 */         if ((treeRow < 0) && (row < 0))
/*      */         {
/*      */ 
/*      */ 
/* 1093 */           MouseEvent pressed = new MouseEvent(JXTreeTable.this.renderer, me.getID(), me.getWhen(), me.getModifiers(), treeMousePoint.x, treeMousePoint.y, me.getClickCount(), me.isPopupTrigger());
/*      */           
/*      */ 
/*      */ 
/* 1097 */           JXTreeTable.this.renderer.dispatchEvent(pressed);
/*      */           
/*      */ 
/* 1100 */           MouseEvent released = new MouseEvent(JXTreeTable.this.renderer, 502, pressed.getWhen(), pressed.getModifiers(), pressed.getX(), pressed.getY(), pressed.getClickCount(), pressed.isPopupTrigger());
/*      */           
/*      */ 
/*      */ 
/*      */ 
/* 1105 */           JXTreeTable.this.renderer.dispatchEvent(released);
/*      */           
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 1111 */           JXTreeTable.this.requestFocusInWindow();
/*      */         }
/* 1113 */         if (this.expansionChangedFlag) {
/* 1114 */           changedExpansion = true;
/*      */         }
/*      */       }
/*      */       
/* 1118 */       this.expansionChangedFlag = false;
/* 1119 */       return changedExpansion;
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */     protected boolean mightBeExpansionTrigger(EventObject e)
/*      */     {
/* 1126 */       if (!(e instanceof MouseEvent)) return false;
/* 1127 */       MouseEvent me = (MouseEvent)e;
/* 1128 */       if (!SwingUtilities.isLeftMouseButton(me)) return false;
/* 1129 */       if (me.getClickCount() > 1) return false;
/* 1130 */       return me.getID() == 501;
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected boolean hackAroundDragEnabled(MouseEvent me)
/*      */   {
/* 1147 */     Boolean dragHackFlag = (Boolean)getClientProperty("treeTable.dragHackFlag");
/* 1148 */     return (getDragEnabled()) && (Boolean.TRUE.equals(dragHackFlag));
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public int getEditingRow()
/*      */   {
/* 1162 */     return isHierarchical(this.editingColumn) ? -1 : this.editingRow;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   private int realEditingRow()
/*      */   {
/* 1170 */     return this.editingRow;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void setTreeTableModel(TreeTableModel treeModel)
/*      */   {
/* 1181 */     TreeTableModel old = getTreeTableModel();
/*      */     
/*      */ 
/* 1184 */     this.renderer.setModel(treeModel);
/*      */     
/*      */ 
/* 1187 */     firePropertyChange("treeTableModel", old, getTreeTableModel());
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public TreeTableModel getTreeTableModel()
/*      */   {
/* 1196 */     return (TreeTableModel)this.renderer.getModel();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public final void setModel(TableModel tableModel)
/*      */   {
/* 1223 */     if ((tableModel instanceof TreeTableModelAdapter)) {
/* 1224 */       if (((TreeTableModelAdapter)tableModel).getTreeTable() == null)
/*      */       {
/*      */ 
/*      */ 
/* 1228 */         super.setModel(tableModel);
/*      */         
/* 1230 */         ((TreeTableModelAdapter)tableModel).bind(this);
/*      */ 
/*      */ 
/*      */       }
/*      */       else
/*      */       {
/*      */ 
/* 1237 */         throw new IllegalArgumentException("model already bound");
/*      */       }
/*      */     }
/*      */     else {
/* 1241 */       throw new IllegalArgumentException("unsupported model type");
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public void tableChanged(TableModelEvent e)
/*      */   {
/* 1249 */     if ((isStructureChanged(e)) || (isUpdate(e))) {
/* 1250 */       super.tableChanged(e);
/*      */     } else {
/* 1252 */       resizeAndRepaint();
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public final void setRowHeight(int row, int rowHeight)
/*      */   {
/* 1267 */     throw new UnsupportedOperationException("variable height rows not supported");
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void setRowHeight(int rowHeight)
/*      */   {
/* 1278 */     super.setRowHeight(rowHeight);
/* 1279 */     adjustTreeRowHeight(getRowHeight());
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected void adjustTreeRowHeight(int tableRowHeight)
/*      */   {
/* 1288 */     if ((this.renderer != null) && (this.renderer.getRowHeight() != tableRowHeight)) {
/* 1289 */       this.renderer.setRowHeight(tableRowHeight);
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected void adjustTableRowHeight(int treeRowHeight)
/*      */   {
/* 1301 */     if (getRowHeight() != treeRowHeight) {
/* 1302 */       adminSetRowHeight(treeRowHeight);
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void columnMarginChanged(ChangeEvent e)
/*      */   {
/* 1313 */     super.columnMarginChanged(e);
/* 1314 */     adjustTreeBounds();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   private void adjustTreeBounds()
/*      */   {
/* 1321 */     if (this.renderer != null) {
/* 1322 */       this.renderer.setBounds(0, 0, 0, 0);
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void setSelectionMode(int mode)
/*      */   {
/* 1347 */     if (this.renderer != null) {
/* 1348 */       switch (mode) {
/*      */       case 1: 
/* 1350 */         this.renderer.getSelectionModel().setSelectionMode(2);
/*      */         
/* 1352 */         break;
/*      */       
/*      */       case 2: 
/* 1355 */         this.renderer.getSelectionModel().setSelectionMode(4);
/*      */         
/* 1357 */         break;
/*      */       
/*      */       default: 
/* 1360 */         this.renderer.getSelectionModel().setSelectionMode(1);
/*      */       }
/*      */       
/*      */     }
/*      */     
/*      */ 
/* 1366 */     super.setSelectionMode(mode);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public Component prepareRenderer(TableCellRenderer renderer, int row, int column)
/*      */   {
/* 1385 */     Component component = super.prepareRenderer(renderer, row, column);
/* 1386 */     return applyRenderer(component, getComponentAdapter(row, column));
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected Component applyRenderer(Component component, ComponentAdapter adapter)
/*      */   {
/* 1407 */     if (component == null) {
/* 1408 */       throw new IllegalArgumentException("null component");
/*      */     }
/* 1410 */     if (adapter == null) {
/* 1411 */       throw new IllegalArgumentException("null component data adapter");
/*      */     }
/*      */     
/* 1414 */     if (isHierarchical(adapter.column))
/*      */     {
/*      */ 
/*      */ 
/* 1418 */       TreeCellRenderer tcr = this.renderer.getCellRenderer();
/* 1419 */       if ((tcr instanceof JXTree.DelegatingRenderer)) {
/* 1420 */         tcr = ((JXTree.DelegatingRenderer)tcr).getDelegateRenderer();
/*      */       }
/*      */       
/* 1423 */       if ((tcr instanceof DefaultTreeCellRenderer))
/*      */       {
/* 1425 */         DefaultTreeCellRenderer dtcr = (DefaultTreeCellRenderer)tcr;
/*      */         
/* 1427 */         if (adapter.isSelected()) {
/* 1428 */           dtcr.setTextSelectionColor(component.getForeground());
/* 1429 */           dtcr.setBackgroundSelectionColor(component.getBackground());
/*      */         } else {
/* 1431 */           dtcr.setTextNonSelectionColor(component.getForeground());
/* 1432 */           dtcr.setBackgroundNonSelectionColor(component.getBackground());
/*      */         }
/*      */       }
/*      */     }
/*      */     
/* 1437 */     return component;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void setTreeCellRenderer(TreeCellRenderer cellRenderer)
/*      */   {
/* 1446 */     if (this.renderer != null) {
/* 1447 */       this.renderer.setCellRenderer(cellRenderer);
/*      */     }
/*      */   }
/*      */   
/*      */   public TreeCellRenderer getTreeCellRenderer() {
/* 1452 */     return this.renderer.getCellRenderer();
/*      */   }
/*      */   
/*      */ 
/*      */   public String getToolTipText(MouseEvent event)
/*      */   {
/* 1458 */     int column = columnAtPoint(event.getPoint());
/* 1459 */     if (isHierarchical(column)) {
/* 1460 */       int row = rowAtPoint(event.getPoint());
/* 1461 */       return this.renderer.getToolTipText(event, row, column);
/*      */     }
/* 1463 */     return super.getToolTipText(event);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void setCollapsedIcon(Icon icon)
/*      */   {
/* 1474 */     this.renderer.setCollapsedIcon(icon);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void setExpandedIcon(Icon icon)
/*      */   {
/* 1485 */     this.renderer.setExpandedIcon(icon);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void setOpenIcon(Icon icon)
/*      */   {
/* 1496 */     this.renderer.setOpenIcon(icon);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void setClosedIcon(Icon icon)
/*      */   {
/* 1507 */     this.renderer.setClosedIcon(icon);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void setLeafIcon(Icon icon)
/*      */   {
/* 1518 */     this.renderer.setLeafIcon(icon);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void setOverwriteRendererIcons(boolean overwrite)
/*      */   {
/* 1537 */     this.renderer.setOverwriteRendererIcons(overwrite);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public boolean isOverwriteRendererIcons()
/*      */   {
/* 1556 */     return this.renderer.isOverwriteRendererIcons();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void clearSelection()
/*      */   {
/* 1567 */     if (this.renderer != null) {
/* 1568 */       this.renderer.clearSelection();
/*      */     }
/* 1570 */     super.clearSelection();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public void collapseAll()
/*      */   {
/* 1577 */     this.renderer.collapseAll();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public void expandAll()
/*      */   {
/* 1584 */     this.renderer.expandAll();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void collapsePath(TreePath path)
/*      */   {
/* 1593 */     this.renderer.collapsePath(path);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void expandPath(TreePath path)
/*      */   {
/* 1602 */     this.renderer.expandPath(path);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void scrollPathToVisible(TreePath path)
/*      */   {
/* 1620 */     this.renderer.scrollPathToVisible(path);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void collapseRow(int row)
/*      */   {
/* 1633 */     this.renderer.collapseRow(row);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public void expandRow(int row)
/*      */   {
/* 1641 */     this.renderer.expandRow(row);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public boolean isVisible(TreePath path)
/*      */   {
/* 1654 */     return this.renderer.isVisible(path);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public boolean isExpanded(TreePath path)
/*      */   {
/* 1666 */     return this.renderer.isExpanded(path);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public boolean isExpanded(int row)
/*      */   {
/* 1678 */     return this.renderer.isExpanded(row);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public boolean isCollapsed(TreePath path)
/*      */   {
/* 1691 */     return this.renderer.isCollapsed(path);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public boolean isCollapsed(int row)
/*      */   {
/* 1702 */     return this.renderer.isCollapsed(row);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public Enumeration<?> getExpandedDescendants(TreePath parent)
/*      */   {
/* 1723 */     return this.renderer.getExpandedDescendants(parent);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public TreePath getPathForLocation(int x, int y)
/*      */   {
/* 1736 */     int row = rowAtPoint(new Point(x, y));
/* 1737 */     if (row == -1) {
/* 1738 */       return null;
/*      */     }
/* 1740 */     return this.renderer.getPathForRow(row);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public TreePath getPathForRow(int row)
/*      */   {
/* 1751 */     return this.renderer.getPathForRow(row);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public int getRowForPath(TreePath path)
/*      */   {
/* 1761 */     return this.renderer.getRowForPath(path);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void setRootVisible(boolean visible)
/*      */   {
/* 1772 */     this.renderer.setRootVisible(visible);
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 1780 */     revalidate();
/* 1781 */     repaint();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public boolean isRootVisible()
/*      */   {
/* 1790 */     return this.renderer.isRootVisible();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void setScrollsOnExpand(boolean scroll)
/*      */   {
/* 1804 */     this.renderer.setScrollsOnExpand(scroll);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public boolean getScrollsOnExpand()
/*      */   {
/* 1813 */     return this.renderer.getScrollsOnExpand();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void setShowsRootHandles(boolean visible)
/*      */   {
/* 1825 */     this.renderer.setShowsRootHandles(visible);
/* 1826 */     repaint();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public boolean getShowsRootHandles()
/*      */   {
/* 1835 */     return this.renderer.getShowsRootHandles();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void setExpandsSelectedPaths(boolean expand)
/*      */   {
/* 1845 */     this.renderer.setExpandsSelectedPaths(expand);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public boolean getExpandsSelectedPaths()
/*      */   {
/* 1854 */     return this.renderer.getExpandsSelectedPaths();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public int getToggleClickCount()
/*      */   {
/* 1864 */     return this.renderer.getToggleClickCount();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void setToggleClickCount(int clickCount)
/*      */   {
/* 1874 */     this.renderer.setToggleClickCount(clickCount);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public boolean isLargeModel()
/*      */   {
/* 1885 */     return this.renderer.isLargeModel();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void setLargeModel(boolean newValue)
/*      */   {
/* 1901 */     this.renderer.setLargeModel(newValue);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void addTreeExpansionListener(TreeExpansionListener tel)
/*      */   {
/* 1917 */     getTreeExpansionBroadcaster().addTreeExpansionListener(tel);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   private TreeExpansionBroadcaster getTreeExpansionBroadcaster()
/*      */   {
/* 1924 */     if (this.treeExpansionBroadcaster == null) {
/* 1925 */       this.treeExpansionBroadcaster = new TreeExpansionBroadcaster(this);
/* 1926 */       this.renderer.addTreeExpansionListener(this.treeExpansionBroadcaster);
/*      */     }
/* 1928 */     return this.treeExpansionBroadcaster;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public void removeTreeExpansionListener(TreeExpansionListener tel)
/*      */   {
/* 1936 */     if (this.treeExpansionBroadcaster == null) return;
/* 1937 */     this.treeExpansionBroadcaster.removeTreeExpansionListener(tel);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void addTreeSelectionListener(TreeSelectionListener tsl)
/*      */   {
/* 1948 */     this.renderer.addTreeSelectionListener(tsl);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public void removeTreeSelectionListener(TreeSelectionListener tsl)
/*      */   {
/* 1956 */     this.renderer.removeTreeSelectionListener(tsl);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void addTreeWillExpandListener(TreeWillExpandListener tel)
/*      */   {
/* 1967 */     this.renderer.addTreeWillExpandListener(tel);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public void removeTreeWillExpandListener(TreeWillExpandListener tel)
/*      */   {
/* 1975 */     this.renderer.removeTreeWillExpandListener(tel);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public TreeSelectionModel getTreeSelectionModel()
/*      */   {
/* 1985 */     return this.renderer.getSelectionModel();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void sizeColumnsToFit(int resizingColumn)
/*      */   {
/* 1998 */     super.sizeColumnsToFit(resizingColumn);
/*      */     
/* 2000 */     if ((getEditingColumn() != -1) && (isHierarchical(this.editingColumn))) {
/* 2001 */       Rectangle cellRect = getCellRect(realEditingRow(), getEditingColumn(), false);
/*      */       
/* 2003 */       Component component = getEditorComponent();
/* 2004 */       component.setBounds(cellRect);
/* 2005 */       component.validate();
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public boolean isHierarchical(int column)
/*      */   {
/* 2021 */     if ((column < 0) || (column >= getColumnCount())) {
/* 2022 */       throw new IllegalArgumentException("column must be valid, was" + column);
/*      */     }
/*      */     
/* 2025 */     return getHierarchicalColumn() == column;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public int getHierarchicalColumn()
/*      */   {
/* 2037 */     return convertColumnIndexToView(((TreeTableModel)this.renderer.getModel()).getHierarchicalColumn());
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public TableCellRenderer getCellRenderer(int row, int column)
/*      */   {
/* 2045 */     if (isHierarchical(column)) {
/* 2046 */       return this.renderer;
/*      */     }
/*      */     
/* 2049 */     return super.getCellRenderer(row, column);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public TableCellEditor getCellEditor(int row, int column)
/*      */   {
/* 2057 */     if (isHierarchical(column)) {
/* 2058 */       return this.hierarchicalEditor;
/*      */     }
/*      */     
/* 2061 */     return super.getCellEditor(row, column);
/*      */   }
/*      */   
/*      */ 
/*      */   public void updateUI()
/*      */   {
/* 2067 */     super.updateUI();
/* 2068 */     updateHierarchicalRendererEditor();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   protected void updateHierarchicalRendererEditor()
/*      */   {
/* 2076 */     if (this.renderer != null) {
/* 2077 */       SwingUtilities.updateComponentTreeUI(this.renderer);
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public String getStringAt(int row, int column)
/*      */   {
/* 2092 */     if (isHierarchical(column)) {
/* 2093 */       return getHierarchicalStringAt(row);
/*      */     }
/* 2095 */     return super.getStringAt(row, column);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   private String getHierarchicalStringAt(int row)
/*      */   {
/* 2108 */     return this.renderer.getStringAt(row);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   class ListToTreeSelectionModelWrapper
/*      */     extends DefaultTreeSelectionModel
/*      */   {
/*      */     protected boolean updatingListSelectionModel;
/*      */     
/*      */ 
/*      */ 
/*      */     public ListToTreeSelectionModelWrapper()
/*      */     {
/* 2123 */       getListSelectionModel().addListSelectionListener(createListSelectionListener());
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     ListSelectionModel getListSelectionModel()
/*      */     {
/* 2133 */       return this.listSelectionModel;
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     public void resetRowSelection()
/*      */     {
/* 2143 */       if (!this.updatingListSelectionModel) {
/* 2144 */         this.updatingListSelectionModel = true;
/*      */         try {
/* 2146 */           super.resetRowSelection();
/*      */         }
/*      */         finally {
/* 2149 */           this.updatingListSelectionModel = false;
/*      */         }
/*      */       }
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     protected ListSelectionListener createListSelectionListener()
/*      */     {
/* 2163 */       return new ListSelectionHandler();
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     protected void updateSelectedPathsFromSelectedRows()
/*      */     {
/* 2172 */       if (!this.updatingListSelectionModel) {
/* 2173 */         this.updatingListSelectionModel = true;
/*      */         try {
/* 2175 */           if (this.listSelectionModel.isSelectionEmpty()) {
/* 2176 */             clearSelection();
/*      */           }
/*      */           else
/*      */           {
/* 2180 */             int min = this.listSelectionModel.getMinSelectionIndex();
/* 2181 */             int max = this.listSelectionModel.getMaxSelectionIndex();
/*      */             
/* 2183 */             List<TreePath> paths = new ArrayList();
/* 2184 */             for (int counter = min; counter <= max; counter++) {
/* 2185 */               if (this.listSelectionModel.isSelectedIndex(counter)) {
/* 2186 */                 TreePath selPath = JXTreeTable.this.renderer.getPathForRow(counter);
/*      */                 
/*      */ 
/* 2189 */                 if (selPath != null) {
/* 2190 */                   paths.add(selPath);
/*      */                 }
/*      */               }
/*      */             }
/* 2194 */             setSelectionPaths((TreePath[])paths.toArray(new TreePath[paths.size()]));
/*      */             
/*      */ 
/* 2197 */             this.leadRow = this.leadIndex;
/*      */           }
/*      */         }
/*      */         finally {
/* 2201 */           this.updatingListSelectionModel = false;
/*      */         }
/*      */       }
/*      */     }
/*      */     
/*      */     class ListSelectionHandler implements ListSelectionListener
/*      */     {
/*      */       ListSelectionHandler() {}
/*      */       
/*      */       public void valueChanged(ListSelectionEvent e)
/*      */       {
/* 2212 */         if (!e.getValueIsAdjusting()) {
/* 2213 */           JXTreeTable.ListToTreeSelectionModelWrapper.this.updateSelectedPathsFromSelectedRows();
/*      */         }
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected static class TreeTableModelAdapter
/*      */     extends AbstractTableModel
/*      */   {
/*      */     private TreeModelListener treeModelListener;
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */     private final JTree tree;
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */     TreeTableModelAdapter(JTree tree)
/*      */     {
/* 2238 */       assert (tree != null);
/*      */       
/* 2240 */       this.tree = tree;
/* 2241 */       tree.getModel().addTreeModelListener(getTreeModelListener());
/* 2242 */       tree.addTreeExpansionListener(new TreeExpansionListener()
/*      */       {
/*      */         public void treeExpanded(TreeExpansionEvent event)
/*      */         {
/* 2246 */           JXTreeTable.TreeTableModelAdapter.this.updateAfterExpansionEvent(event);
/*      */         }
/*      */         
/*      */         public void treeCollapsed(TreeExpansionEvent event) {
/* 2250 */           JXTreeTable.TreeTableModelAdapter.this.updateAfterExpansionEvent(event);
/*      */         }
/* 2252 */       });
/* 2253 */       tree.addPropertyChangeListener("model", new PropertyChangeListener() {
/*      */         public void propertyChange(PropertyChangeEvent evt) {
/* 2255 */           TreeTableModel model = (TreeTableModel)evt.getOldValue();
/* 2256 */           model.removeTreeModelListener(JXTreeTable.TreeTableModelAdapter.this.getTreeModelListener());
/*      */           
/* 2258 */           model = (TreeTableModel)evt.getNewValue();
/* 2259 */           model.addTreeModelListener(JXTreeTable.TreeTableModelAdapter.this.getTreeModelListener());
/*      */           
/* 2261 */           JXTreeTable.TreeTableModelAdapter.this.fireTableStructureChanged();
/*      */         }
/*      */       });
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     protected void updateAfterExpansionEvent(TreeExpansionEvent event)
/*      */     {
/* 2275 */       fireTableDataChanged();
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     protected JXTreeTable getTreeTable()
/*      */     {
/* 2286 */       return this.treeTable;
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     protected final void bind(JXTreeTable treeTable)
/*      */     {
/* 2297 */       if (treeTable == null) {
/* 2298 */         throw new IllegalArgumentException("null treeTable");
/*      */       }
/*      */       
/* 2301 */       if (this.treeTable == null) {
/* 2302 */         this.treeTable = treeTable;
/*      */       }
/*      */       else {
/* 2305 */         throw new IllegalArgumentException("adapter already bound");
/*      */       }
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */     public Class<?> getColumnClass(int column)
/*      */     {
/* 2314 */       return ((TreeTableModel)this.tree.getModel()).getColumnClass(column);
/*      */     }
/*      */     
/*      */     public int getColumnCount() {
/* 2318 */       return ((TreeTableModel)this.tree.getModel()).getColumnCount();
/*      */     }
/*      */     
/*      */     public String getColumnName(int column)
/*      */     {
/* 2323 */       return ((TreeTableModel)this.tree.getModel()).getColumnName(column);
/*      */     }
/*      */     
/*      */     public int getRowCount() {
/* 2327 */       return this.tree.getRowCount();
/*      */     }
/*      */     
/*      */     public Object getValueAt(int row, int column)
/*      */     {
/* 2332 */       Object node = nodeForRow(row);
/* 2333 */       return node != null ? ((TreeTableModel)this.tree.getModel()).getValueAt(node, column) : null;
/*      */     }
/*      */     
/*      */ 
/*      */     public boolean isCellEditable(int row, int column)
/*      */     {
/* 2339 */       Object node = nodeForRow(row);
/* 2340 */       return node != null ? ((TreeTableModel)this.tree.getModel()).isCellEditable(node, column) : false;
/*      */     }
/*      */     
/*      */ 
/*      */     public void setValueAt(Object value, int row, int column)
/*      */     {
/* 2346 */       Object node = nodeForRow(row);
/* 2347 */       if (node != null) {
/* 2348 */         ((TreeTableModel)this.tree.getModel()).setValueAt(value, node, column);
/*      */       }
/*      */     }
/*      */     
/*      */     protected Object nodeForRow(int row)
/*      */     {
/* 2354 */       TreePath path = this.tree.getPathForRow(row);
/* 2355 */       return path != null ? path.getLastPathComponent() : null;
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */     private TreeModelListener getTreeModelListener()
/*      */     {
/* 2362 */       if (this.treeModelListener == null) {
/* 2363 */         this.treeModelListener = new TreeModelListener()
/*      */         {
/*      */           public void treeNodesChanged(TreeModelEvent e)
/*      */           {
/* 2367 */             JXTreeTable.TreeTableModelAdapter.this.delayedFireTableDataUpdated(e);
/*      */           }
/*      */           
/*      */ 
/*      */ 
/*      */           public void treeNodesInserted(TreeModelEvent e)
/*      */           {
/* 2374 */             JXTreeTable.TreeTableModelAdapter.this.delayedFireTableDataChanged(e, 1);
/*      */           }
/*      */           
/*      */           public void treeNodesRemoved(TreeModelEvent e)
/*      */           {
/* 2379 */             JXTreeTable.TreeTableModelAdapter.this.delayedFireTableDataChanged(e, 2);
/*      */           }
/*      */           
/*      */           public void treeStructureChanged(TreeModelEvent e)
/*      */           {
/* 2384 */             if (JXTreeTable.TreeTableModelAdapter.this.isTableStructureChanged(e)) {
/* 2385 */               JXTreeTable.TreeTableModelAdapter.this.delayedFireTableStructureChanged();
/*      */             } else {
/* 2387 */               JXTreeTable.TreeTableModelAdapter.this.delayedFireTableDataChanged();
/*      */             }
/*      */           }
/*      */         };
/*      */       }
/*      */       
/* 2393 */       return this.treeModelListener;
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     private boolean isTableStructureChanged(TreeModelEvent e)
/*      */     {
/* 2409 */       if ((e.getTreePath() == null) || (e.getTreePath().getParentPath() == null))
/* 2410 */         return true;
/* 2411 */       return false;
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */     private void delayedFireTableStructureChanged()
/*      */     {
/* 2419 */       SwingUtilities.invokeLater(new Runnable() {
/*      */         public void run() {
/* 2421 */           JXTreeTable.TreeTableModelAdapter.this.fireTableStructureChanged();
/*      */         }
/*      */       });
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */     private void delayedFireTableDataChanged()
/*      */     {
/* 2431 */       SwingUtilities.invokeLater(new Runnable() {
/*      */         public void run() {
/* 2433 */           JXTreeTable.TreeTableModelAdapter.this.fireTableDataChanged();
/*      */         }
/*      */       });
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     private void delayedFireTableDataChanged(final TreeModelEvent tme, final int typeChange)
/*      */     {
/* 2444 */       if ((typeChange < 1) || (typeChange > 2)) {
/* 2445 */         throw new IllegalArgumentException("Event type must be 1 or 2, was " + typeChange);
/*      */       }
/*      */       
/* 2448 */       final boolean expanded = this.tree.isExpanded(tme.getTreePath());
/*      */       
/*      */ 
/* 2451 */       SwingUtilities.invokeLater(new Runnable() {
/*      */         public void run() {
/* 2453 */           int[] indices = tme.getChildIndices();
/* 2454 */           TreePath path = tme.getTreePath();
/*      */           
/*      */ 
/* 2457 */           if (indices != null) {
/* 2458 */             if (expanded)
/*      */             {
/*      */ 
/* 2461 */               int min = indices[0];
/* 2462 */               int max = indices[(indices.length - 1)];
/* 2463 */               int startingRow = JXTreeTable.TreeTableModelAdapter.this.tree.getRowForPath(path) + 1;
/* 2464 */               min = startingRow + min;
/* 2465 */               max = startingRow + max;
/* 2466 */               switch (typeChange)
/*      */               {
/*      */ 
/*      */               case 1: 
/* 2470 */                 JXTreeTable.TreeTableModelAdapter.this.fireTableRowsInserted(min, max);
/* 2471 */                 break;
/*      */               
/*      */ 
/*      */               case 2: 
/* 2475 */                 JXTreeTable.TreeTableModelAdapter.this.fireTableRowsDeleted(min, max);
/*      */               
/*      */               }
/*      */               
/*      */             }
/*      */             else
/*      */             {
/* 2482 */               int row = JXTreeTable.TreeTableModelAdapter.this.tree.getRowForPath(path);
/*      */               
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 2488 */               if (row >= 0) {
/* 2489 */                 JXTreeTable.TreeTableModelAdapter.this.fireTableRowsUpdated(row, row);
/*      */               }
/*      */             }
/*      */           } else {
/* 2493 */             JXTreeTable.TreeTableModelAdapter.this.fireTableDataChanged();
/*      */           }
/*      */         }
/*      */       });
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     protected void delayedFireTableDataUpdated(final TreeModelEvent tme)
/*      */     {
/* 2506 */       final boolean expanded = this.tree.isExpanded(tme.getTreePath());
/* 2507 */       SwingUtilities.invokeLater(new Runnable() {
/*      */         public void run() {
/* 2509 */           int[] indices = tme.getChildIndices();
/* 2510 */           TreePath path = tme.getTreePath();
/* 2511 */           if (indices != null) {
/* 2512 */             if (expanded)
/*      */             {
/* 2514 */               Object[] children = tme.getChildren();
/*      */               
/*      */ 
/*      */ 
/* 2518 */               int min = Integer.MAX_VALUE;
/* 2519 */               int max = Integer.MIN_VALUE;
/* 2520 */               for (int i = 0; i < indices.length; i++) {
/* 2521 */                 Object child = children[i];
/* 2522 */                 TreePath childPath = path.pathByAddingChild(child);
/*      */                 
/* 2524 */                 int index = JXTreeTable.TreeTableModelAdapter.this.tree.getRowForPath(childPath);
/* 2525 */                 if (index < min) {
/* 2526 */                   min = index;
/*      */                 }
/* 2528 */                 if (index > max) {
/* 2529 */                   max = index;
/*      */                 }
/*      */               }
/*      */               
/*      */ 
/* 2534 */               JXTreeTable.TreeTableModelAdapter.this.fireTableRowsUpdated(Math.max(0, min), Math.max(0, max));
/*      */             }
/*      */             else
/*      */             {
/* 2538 */               int row = JXTreeTable.TreeTableModelAdapter.this.tree.getRowForPath(path);
/*      */               
/*      */ 
/*      */ 
/* 2542 */               if (row >= 0) {
/* 2543 */                 JXTreeTable.TreeTableModelAdapter.this.fireTableRowsUpdated(row, row);
/*      */               }
/*      */             }
/*      */           } else {
/* 2547 */             JXTreeTable.TreeTableModelAdapter.this.fireTableDataChanged();
/*      */           }
/*      */         }
/*      */       });
/*      */     }
/*      */     
/*      */ 
/*      */ 
/* 2555 */     private JXTreeTable treeTable = null;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   static class TreeTableCellRenderer
/*      */     extends JXTree
/*      */     implements TableCellRenderer
/*      */   {
/*      */     private PropertyChangeListener rolloverListener;
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */     public TreeTableCellRenderer(TreeTableModel model)
/*      */     {
/* 2573 */       super();
/* 2574 */       putClientProperty("JTree.lineStyle", "None");
/* 2575 */       setRootVisible(false);
/* 2576 */       setShowsRootHandles(true);
/*      */       
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 2584 */       setCellRenderer(new ClippedTreeCellRenderer(null));
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     public boolean isVisible()
/*      */     {
/* 2603 */       return shouldApplyDropHack() ? false : super.isVisible();
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     protected boolean shouldApplyDropHack()
/*      */     {
/* 2613 */       return !Boolean.FALSE.equals(this.treeTable.getClientProperty("treeTable.dropHackFlag"));
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     private String getToolTipText(MouseEvent event, int row, int column)
/*      */     {
/* 2632 */       if (row < 0) return null;
/* 2633 */       String toolTip = null;
/* 2634 */       TreeCellRenderer renderer = getCellRenderer();
/* 2635 */       TreePath path = getPathForRow(row);
/* 2636 */       Object lastPath = path.getLastPathComponent();
/* 2637 */       Component rComponent = renderer.getTreeCellRendererComponent(this, lastPath, isRowSelected(row), isExpanded(row), getModel().isLeaf(lastPath), row, true);
/*      */       
/*      */ 
/*      */ 
/*      */ 
/* 2642 */       if ((rComponent instanceof JComponent)) {
/* 2643 */         Rectangle pathBounds = getPathBounds(path);
/* 2644 */         Rectangle cellRect = this.treeTable.getCellRect(row, column, false);
/*      */         
/*      */ 
/*      */ 
/* 2648 */         Point mousePoint = event.getPoint();
/*      */         
/* 2650 */         mousePoint.translate(-cellRect.x, -cellRect.y);
/*      */         
/* 2652 */         mousePoint.translate(-pathBounds.x, 0);
/*      */         
/*      */ 
/*      */ 
/* 2656 */         MouseEvent newEvent = new MouseEvent(rComponent, event.getID(), event.getWhen(), event.getModifiers(), mousePoint.x, mousePoint.y, event.getClickCount(), event.isPopupTrigger());
/*      */         
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 2665 */         toolTip = ((JComponent)rComponent).getToolTipText(newEvent);
/*      */       }
/* 2667 */       if (toolTip != null) {
/* 2668 */         return toolTip;
/*      */       }
/* 2670 */       return getToolTipText();
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     public void setToolTipText(String text)
/*      */     {
/* 2682 */       putClientProperty("ToolTipText", text);
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     public final void bind(JXTreeTable treeTable)
/*      */     {
/* 2694 */       if (treeTable == null) {
/* 2695 */         throw new IllegalArgumentException("null treeTable");
/*      */       }
/*      */       
/* 2698 */       if (this.treeTable == null) {
/* 2699 */         this.treeTable = treeTable;
/*      */ 
/*      */       }
/*      */       else
/*      */       {
/* 2704 */         throw new IllegalArgumentException("renderer already bound");
/*      */       }
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     private void bindRollover()
/*      */     {
/* 2719 */       setRolloverEnabled(this.treeTable.isRolloverEnabled());
/* 2720 */       this.treeTable.addPropertyChangeListener(getRolloverListener());
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */     private PropertyChangeListener getRolloverListener()
/*      */     {
/* 2728 */       if (this.rolloverListener == null) {
/* 2729 */         this.rolloverListener = createRolloverListener();
/*      */       }
/* 2731 */       return this.rolloverListener;
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     protected PropertyChangeListener createRolloverListener()
/*      */     {
/* 2746 */       PropertyChangeListener l = new PropertyChangeListener()
/*      */       {
/*      */         public void propertyChange(PropertyChangeEvent evt) {
/* 2749 */           if ((JXTreeTable.TreeTableCellRenderer.this.treeTable == null) || (JXTreeTable.TreeTableCellRenderer.this.treeTable != evt.getSource()))
/* 2750 */             return;
/* 2751 */           if ("rolloverEnabled".equals(evt.getPropertyName())) {
/* 2752 */             JXTreeTable.TreeTableCellRenderer.this.setRolloverEnabled(((Boolean)evt.getNewValue()).booleanValue());
/*      */           }
/* 2754 */           if ("swingx.rollover".equals(evt.getPropertyName())) {
/* 2755 */             rollover(evt);
/*      */           }
/*      */         }
/*      */         
/*      */         private void rollover(PropertyChangeEvent evt) {
/* 2760 */           boolean isHierarchical = isHierarchical((Point)evt.getNewValue());
/* 2761 */           JXTreeTable.TreeTableCellRenderer.this.putClientProperty(evt.getPropertyName(), isHierarchical ? new Point((Point)evt.getNewValue()) : null);
/*      */         }
/*      */         
/*      */         private boolean isHierarchical(Point point)
/*      */         {
/* 2766 */           if (point != null) {
/* 2767 */             int column = point.x;
/* 2768 */             if (column >= 0) {
/* 2769 */               return JXTreeTable.TreeTableCellRenderer.this.treeTable.isHierarchical(column);
/*      */             }
/*      */           }
/* 2772 */           return false; }
/*      */         
/* 2774 */         Point rollover = new Point(-1, -1);
/*      */ 
/* 2776 */       };
/* 2777 */       return l;
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     protected RolloverProducer createRolloverProducer()
/*      */     {
/* 2790 */       new RolloverProducer()
/*      */       {
/*      */ 
/*      */ 
/*      */ 
/*      */         protected void updateRollover(MouseEvent e, String property, boolean fireAlways)
/*      */         {
/*      */ 
/*      */ 
/*      */ 
/* 2800 */           if ("swingx.clicked".equals(property)) {
/* 2801 */             super.updateRollover(e, property, fireAlways);
/*      */           }
/*      */         }
/*      */         
/*      */         protected void updateRolloverPoint(JComponent component, Point mousePoint)
/*      */         {
/* 2807 */           JXTree tree = (JXTree)component;
/* 2808 */           int row = tree.getClosestRowForLocation(mousePoint.x, mousePoint.y);
/* 2809 */           Rectangle bounds = tree.getRowBounds(row);
/* 2810 */           if (bounds == null) {
/* 2811 */             row = -1;
/*      */           }
/* 2813 */           else if ((bounds.y + bounds.height < mousePoint.y) || (bounds.x > mousePoint.x))
/*      */           {
/* 2815 */             row = -1;
/*      */           }
/*      */           
/* 2818 */           int col = row < 0 ? -1 : 0;
/* 2819 */           this.rollover.x = col;
/* 2820 */           this.rollover.y = row;
/*      */         }
/*      */       };
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */     public void scrollRectToVisible(Rectangle aRect)
/*      */     {
/* 2829 */       this.treeTable.scrollRectToVisible(aRect);
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     protected void setExpandedState(TreePath path, boolean state)
/*      */     {
/* 2839 */       if (isExpanded(path) == state) { return;
/*      */       }
/*      */       
/* 2842 */       this.treeTable.getTreeTableHacker().completeEditing();
/* 2843 */       super.setExpandedState(path, state);
/* 2844 */       this.treeTable.getTreeTableHacker().expansionChanged();
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     public void updateUI()
/*      */     {
/* 2854 */       super.updateUI();
/*      */       
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 2863 */       TreeCellRenderer tcr = getCellRenderer();
/* 2864 */       if ((tcr instanceof DefaultTreeCellRenderer)) {
/* 2865 */         DefaultTreeCellRenderer dtcr = (DefaultTreeCellRenderer)tcr;
/*      */         
/*      */ 
/* 2868 */         dtcr.setBorderSelectionColor(null);
/* 2869 */         dtcr.setTextSelectionColor(UIManager.getColor("Table.selectionForeground"));
/*      */         
/* 2871 */         dtcr.setBackgroundSelectionColor(UIManager.getColor("Table.selectionBackground"));
/*      */       }
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     public void setRowHeight(int rowHeight)
/*      */     {
/* 2888 */       super.setRowHeight(rowHeight);
/* 2889 */       if ((rowHeight > 0) && 
/* 2890 */         (this.treeTable != null)) {
/* 2891 */         this.treeTable.adjustTableRowHeight(rowHeight);
/*      */       }
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     public void setBounds(int x, int y, int w, int h)
/*      */     {
/* 2905 */       y = 0;
/* 2906 */       x = 0;
/* 2907 */       if (this.treeTable != null)
/*      */       {
/*      */ 
/*      */ 
/* 2911 */         h = this.treeTable.getRowCount() * getRowHeight();
/* 2912 */         int hierarchicalC = this.treeTable.getHierarchicalColumn();
/*      */         
/* 2914 */         if (hierarchicalC >= 0) {
/* 2915 */           TableColumn column = this.treeTable.getColumn(hierarchicalC);
/*      */           
/* 2917 */           w = column.getWidth();
/*      */         }
/*      */       }
/* 2920 */       super.setBounds(x, y, w, h);
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     public void paint(Graphics g)
/*      */     {
/* 2929 */       Rectangle cellRect = this.treeTable.getCellRect(this.visibleRow, 0, false);
/* 2930 */       g.translate(0, -cellRect.y);
/*      */       
/* 2932 */       this.hierarchicalColumnWidth = getWidth();
/* 2933 */       super.paint(g);
/*      */       
/*      */ 
/* 2936 */       if (this.highlightBorder != null)
/*      */       {
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 2942 */         this.highlightBorder.paintBorder(this, g, 0, cellRect.y, getWidth(), cellRect.height);
/*      */       }
/*      */     }
/*      */     
/*      */     public void doClick()
/*      */     {
/* 2948 */       if (((getCellRenderer() instanceof RolloverRenderer)) && (((RolloverRenderer)getCellRenderer()).isEnabled()))
/*      */       {
/* 2950 */         ((RolloverRenderer)getCellRenderer()).doClick();
/*      */       }
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */     public boolean isRowSelected(int row)
/*      */     {
/* 2958 */       if ((this.treeTable == null) || (this.treeTable.getHierarchicalColumn() < 0)) return false;
/* 2959 */       return this.treeTable.isCellSelected(row, this.treeTable.getHierarchicalColumn());
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */     public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column)
/*      */     {
/* 2966 */       assert (table == this.treeTable);
/*      */       
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 2973 */       if (isSelected) {
/* 2974 */         setBackground(table.getSelectionBackground());
/* 2975 */         setForeground(table.getSelectionForeground());
/*      */       }
/*      */       else {
/* 2978 */         setBackground(table.getBackground());
/* 2979 */         setForeground(table.getForeground());
/*      */       }
/*      */       
/* 2982 */       this.highlightBorder = null;
/* 2983 */       if ((this.treeTable != null) && (
/* 2984 */         (this.treeTable.realEditingRow() != row) || (this.treeTable.getEditingColumn() != column)))
/*      */       {
/*      */ 
/* 2987 */         if (hasFocus) {
/* 2988 */           this.highlightBorder = UIManager.getBorder("Table.focusCellHighlightBorder");
/*      */         }
/*      */       }
/*      */       
/*      */ 
/* 2993 */       this.visibleRow = row;
/*      */       
/* 2995 */       return this;
/*      */     }
/*      */     
/*      */     private class ClippedTreeCellRenderer extends DefaultXTreeCellRenderer implements StringValue
/*      */     {
/*      */       private boolean inpainting;
/*      */       private String shortText;
/*      */       
/*      */       private ClippedTreeCellRenderer() {}
/*      */       
/*      */       public void paint(Graphics g) {
/* 3006 */         String fullText = super.getText();
/*      */         
/* 3008 */         this.shortText = SwingUtilities.layoutCompoundLabel(this, g.getFontMetrics(), fullText, getIcon(), getVerticalAlignment(), getHorizontalAlignment(), getVerticalTextPosition(), getHorizontalTextPosition(), getItemRect(this.itemRect), this.iconRect, this.textRect, getIconTextGap());
/*      */         
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */         try
/*      */         {
/* 3020 */           this.inpainting = true;
/*      */           
/*      */ 
/* 3023 */           setText(this.shortText);
/* 3024 */           super.paint(g);
/*      */         } finally {
/* 3026 */           this.inpainting = false;
/* 3027 */           setText(fullText);
/*      */         }
/*      */       }
/*      */       
/*      */       private Rectangle getItemRect(Rectangle itemRect)
/*      */       {
/* 3033 */         getBounds(itemRect);
/*      */         
/* 3035 */         itemRect.width = (JXTreeTable.TreeTableCellRenderer.this.hierarchicalColumnWidth - itemRect.x);
/* 3036 */         return itemRect;
/*      */       }
/*      */       
/*      */       public Component getTreeCellRendererComponent(JTree tree, Object value, boolean sel, boolean expanded, boolean leaf, int row, boolean hasFocus)
/*      */       {
/* 3041 */         return super.getTreeCellRendererComponent(tree, getHierarchicalTableValue(value), sel, expanded, leaf, row, hasFocus);
/*      */       }
/*      */       
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       private Object getHierarchicalTableValue(Object node)
/*      */       {
/* 3052 */         Object val = node;
/*      */         
/* 3054 */         if (JXTreeTable.TreeTableCellRenderer.this.treeTable != null) {
/* 3055 */           int treeColumn = JXTreeTable.TreeTableCellRenderer.this.treeTable.getTreeTableModel().getHierarchicalColumn();
/* 3056 */           Object o = null;
/* 3057 */           if (treeColumn >= 0)
/*      */           {
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 3065 */             o = JXTreeTable.TreeTableCellRenderer.this.treeTable.getTreeTableModel().getValueAt(node, treeColumn);
/*      */           }
/* 3067 */           val = o;
/*      */         }
/* 3069 */         return val;
/*      */       }
/*      */       
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       public String getString(Object node)
/*      */       {
/* 3080 */         return StringValues.TO_STRING.getString(getHierarchicalTableValue(node));
/*      */       }
/*      */       
/*      */ 
/* 3084 */       private final Rectangle iconRect = new Rectangle();
/* 3085 */       private final Rectangle textRect = new Rectangle();
/*      */       
/* 3087 */       private final Rectangle itemRect = new Rectangle();
/*      */     }
/*      */     
/*      */ 
/*      */ 
/* 3092 */     protected Border highlightBorder = null;
/* 3093 */     protected JXTreeTable treeTable = null;
/* 3094 */     protected int visibleRow = 0;
/*      */     
/*      */ 
/* 3097 */     private int hierarchicalColumnWidth = 0;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected ComponentAdapter getComponentAdapter()
/*      */   {
/* 3109 */     if (this.dataAdapter == null) {
/* 3110 */       this.dataAdapter = new TreeTableDataAdapter(this);
/*      */     }
/* 3112 */     return this.dataAdapter;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   protected static class TreeTableDataAdapter
/*      */     extends JXTable.TableAdapter
/*      */   {
/*      */     private final JXTreeTable table;
/*      */     
/*      */ 
/*      */ 
/*      */     public TreeTableDataAdapter(JXTreeTable component)
/*      */     {
/* 3126 */       super();
/* 3127 */       this.table = component;
/*      */     }
/*      */     
/*      */     public JXTreeTable getTreeTable() {
/* 3131 */       return this.table;
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */     public boolean isExpanded()
/*      */     {
/* 3139 */       return this.table.isExpanded(this.row);
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */     public int getDepth()
/*      */     {
/* 3147 */       return this.table.getPathForRow(this.row).getPathCount() - 1;
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     public boolean isLeaf()
/*      */     {
/* 3156 */       TreePath path = this.table.getPathForRow(this.row);
/* 3157 */       if (path != null) {
/* 3158 */         return this.table.getTreeTableModel().isLeaf(path.getLastPathComponent());
/*      */       }
/*      */       
/*      */ 
/* 3162 */       return true;
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     public boolean isHierarchical()
/*      */     {
/* 3171 */       return this.table.isHierarchical(this.column);
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     public String getFilteredStringAt(int row, int column)
/*      */     {
/* 3184 */       if (this.table.getTreeTableModel().getHierarchicalColumn() == column) {
/* 3185 */         if (convertColumnIndexToView(column) < 0) {}
/*      */         
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 3191 */         return this.table.getHierarchicalStringAt(row);
/*      */       }
/* 3193 */       return super.getFilteredStringAt(row, column);
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     public String getStringAt(int row, int column)
/*      */     {
/* 3206 */       if (this.table.getTreeTableModel().getHierarchicalColumn() == column) {
/* 3207 */         if (convertColumnIndexToView(column) < 0) {}
/*      */         
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 3213 */         return this.table.getHierarchicalStringAt(row);
/*      */       }
/* 3215 */       return super.getStringAt(row, column);
/*      */     }
/*      */   }
/*      */ }


/* Location:              E:\java\java学习\hutubill\lib\all.jar!\org\jdesktop\swingx\JXTreeTable.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */
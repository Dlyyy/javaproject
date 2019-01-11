/*     */ package org.jdesktop.swingx;
/*     */ 
/*     */ import java.awt.Component;
/*     */ import java.awt.Dimension;
/*     */ import java.awt.event.MouseEvent;
/*     */ import java.beans.PropertyChangeEvent;
/*     */ import java.beans.PropertyChangeListener;
/*     */ import java.io.Serializable;
/*     */ import java.util.logging.Logger;
/*     */ import javax.swing.JTable;
/*     */ import javax.swing.SortOrder;
/*     */ import javax.swing.SwingUtilities;
/*     */ import javax.swing.event.MouseInputListener;
/*     */ import javax.swing.table.JTableHeader;
/*     */ import javax.swing.table.TableCellRenderer;
/*     */ import javax.swing.table.TableColumn;
/*     */ import javax.swing.table.TableColumnModel;
/*     */ import org.jdesktop.swingx.event.TableColumnModelExtListener;
/*     */ import org.jdesktop.swingx.plaf.LookAndFeelAddons;
/*     */ import org.jdesktop.swingx.plaf.TableHeaderAddon;
/*     */ import org.jdesktop.swingx.sort.SortController;
/*     */ import org.jdesktop.swingx.table.TableColumnExt;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class JXTableHeader
/*     */   extends JTableHeader
/*     */   implements TableColumnModelExtListener
/*     */ {
/*  93 */   private static final Logger LOG = Logger.getLogger(JXTableHeader.class.getName());
/*     */   @Deprecated
/*     */   private SortGestureRecognizer sortGestureRecognizer;
/*     */   
/*  97 */   static { LookAndFeelAddons.contribute(new TableHeaderAddon()); }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private PropertyChangeListener tablePropertyChangeListener;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private MouseInputListener headerListener;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public JXTableHeader(TableColumnModel columnModel)
/*     */   {
/* 128 */     super(columnModel);
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
/*     */   public void setTable(JTable table)
/*     */   {
/* 141 */     uninstallTable();
/* 142 */     super.setTable(table);
/* 143 */     installTable();
/*     */     
/*     */ 
/*     */ 
/* 147 */     if (getXTable() != null) {
/* 148 */       installHeaderListener();
/*     */     } else {
/* 150 */       uninstallHeaderListener();
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   protected void installTable()
/*     */   {
/* 159 */     updateEnabledFromTable();
/* 160 */     if (getTable() == null) return;
/* 161 */     getTable().addPropertyChangeListener(getTablePropertyChangeListener());
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   protected void updateEnabledFromTable()
/*     */   {
/* 168 */     setEnabled(getTable() != null ? getTable().isEnabled() : true);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   protected void uninstallTable()
/*     */   {
/* 176 */     if (getTable() == null) return;
/* 177 */     getTable().removePropertyChangeListener(getTablePropertyChangeListener());
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
/*     */   public void columnPropertyChange(PropertyChangeEvent event)
/*     */   {
/* 195 */     if (isColumnEvent(event)) return;
/* 196 */     resizeAndRepaint();
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
/*     */   protected boolean isColumnEvent(PropertyChangeEvent event)
/*     */   {
/* 213 */     return ("width".equals(event.getPropertyName())) || ("preferredWidth".equals(event.getPropertyName())) || ("visible".equals(event.getPropertyName()));
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
/*     */   public String getToolTipText(MouseEvent event)
/*     */   {
/* 228 */     String columnToolTipText = getColumnToolTipText(event);
/* 229 */     return columnToolTipText != null ? columnToolTipText : super.getToolTipText(event);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected String getColumnToolTipText(MouseEvent event)
/*     */   {
/* 241 */     if (getXTable() == null) return null;
/* 242 */     int column = columnAtPoint(event.getPoint());
/* 243 */     if (column < 0) return null;
/* 244 */     TableColumnExt columnExt = getXTable().getColumnExt(column);
/* 245 */     return columnExt != null ? columnExt.getToolTipText() : null;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public JXTable getXTable()
/*     */   {
/* 254 */     if (!(getTable() instanceof JXTable))
/* 255 */       return null;
/* 256 */     return (JXTable)getTable();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public TableCellRenderer getCellRenderer(int columnIndex)
/*     */   {
/* 268 */     TableCellRenderer renderer = getColumnModel().getColumn(columnIndex).getHeaderRenderer();
/* 269 */     return renderer != null ? renderer : getDefaultRenderer();
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
/*     */   public Dimension getPreferredSize()
/*     */   {
/* 285 */     Dimension pref = super.getPreferredSize();
/* 286 */     pref = getPreferredSize(pref);
/* 287 */     pref.height = getMinimumHeight(pref.height);
/* 288 */     return pref;
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
/*     */   protected Dimension getPreferredSize(Dimension pref)
/*     */   {
/* 303 */     int height = pref.height;
/* 304 */     for (int i = 0; i < getColumnModel().getColumnCount(); i++) {
/* 305 */       TableCellRenderer renderer = getCellRenderer(i);
/* 306 */       Component comp = renderer.getTableCellRendererComponent(this.table, getColumnModel().getColumn(i).getHeaderValue(), false, false, -1, i);
/*     */       
/* 308 */       height = Math.max(height, comp.getPreferredSize().height);
/*     */     }
/* 310 */     pref.height = height;
/* 311 */     return pref;
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
/*     */   protected int getMinimumHeight(int height)
/*     */   {
/* 331 */     if (height == 0)
/*     */     {
/*     */ 
/* 334 */       TableCellRenderer renderer = getDefaultRenderer();
/* 335 */       Component comp = renderer.getTableCellRendererComponent(getTable(), "dummy", false, false, -1, -1);
/*     */       
/* 337 */       height = comp.getPreferredSize().height;
/*     */     }
/* 339 */     return height;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setDraggedColumn(TableColumn column)
/*     */   {
/* 350 */     if (getDraggedColumn() == column) return;
/* 351 */     TableColumn old = getDraggedColumn();
/* 352 */     super.setDraggedColumn(column);
/* 353 */     firePropertyChange("draggedColumn", old, getDraggedColumn());
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setResizingColumn(TableColumn aColumn)
/*     */   {
/* 364 */     if (getResizingColumn() == aColumn) return;
/* 365 */     TableColumn old = getResizingColumn();
/* 366 */     super.setResizingColumn(aColumn);
/* 367 */     firePropertyChange("resizingColumn", old, getResizingColumn());
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
/*     */   public void setDraggedDistance(int distance)
/*     */   {
/* 386 */     int old = getDraggedDistance();
/* 387 */     super.setDraggedDistance(distance);
/*     */     
/* 389 */     firePropertyChange("draggedDistance", old, getDraggedDistance());
/* 390 */     if ((!getAutoscrolls()) || (getXTable() == null)) return;
/* 391 */     TableColumn column = getDraggedColumn();
/*     */     
/*     */ 
/* 394 */     if (column != null) {
/* 395 */       getXTable().scrollColumnToVisible(getViewIndexForColumn(column));
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
/*     */   public TableColumn getDraggedColumn()
/*     */   {
/* 409 */     return isVisible(this.draggedColumn) ? this.draggedColumn : null;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private boolean isVisible(TableColumn column)
/*     */   {
/* 419 */     return getViewIndexForColumn(column) >= 0;
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
/*     */   private int getViewIndexForColumn(TableColumn aColumn)
/*     */   {
/* 433 */     if (aColumn == null)
/* 434 */       return -1;
/* 435 */     TableColumnModel cm = getColumnModel();
/* 436 */     for (int column = 0; column < cm.getColumnCount(); column++) {
/* 437 */       if (cm.getColumn(column) == aColumn) {
/* 438 */         return column;
/*     */       }
/*     */     }
/* 441 */     return -1;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected PropertyChangeListener getTablePropertyChangeListener()
/*     */   {
/* 451 */     if (this.tablePropertyChangeListener == null) {
/* 452 */       this.tablePropertyChangeListener = createTablePropertyChangeListener();
/*     */     }
/* 454 */     return this.tablePropertyChangeListener;
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
/* 467 */     PropertyChangeListener l = new PropertyChangeListener()
/*     */     {
/*     */       public void propertyChange(PropertyChangeEvent evt)
/*     */       {
/* 471 */         if ("enabled".equals(evt.getPropertyName())) {
/* 472 */           JXTableHeader.this.updateEnabledFromTable();
/*     */         }
/*     */       }
/* 475 */     };
/* 476 */     return l;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected void installHeaderListener()
/*     */   {
/* 485 */     if (this.headerListener == null) {
/* 486 */       this.headerListener = new HeaderListener(null);
/* 487 */       addMouseListener(this.headerListener);
/* 488 */       addMouseMotionListener(this.headerListener);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   protected void uninstallHeaderListener()
/*     */   {
/* 497 */     if (this.headerListener != null) {
/* 498 */       removeMouseListener(this.headerListener);
/* 499 */       removeMouseMotionListener(this.headerListener);
/* 500 */       this.headerListener = null;
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private class HeaderListener
/*     */     implements MouseInputListener, Serializable
/*     */   {
/*     */     private TableColumn cachedResizingColumn;
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     private SortOrder[] cachedSortOrderCycle;
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     private HeaderListener() {}
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     public void mouseClicked(MouseEvent e)
/*     */     {
/* 536 */       if (shouldIgnore(e)) {
/* 537 */         return;
/*     */       }
/* 539 */       doResize(e);
/* 540 */       uncacheResizingColumn();
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */     public void mousePressed(MouseEvent e)
/*     */     {
/* 547 */       resetToggleSortOrder(e);
/* 548 */       if (shouldIgnore(e)) {
/* 549 */         return;
/*     */       }
/* 551 */       cacheResizingColumn(e);
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */     public void mouseReleased(MouseEvent e)
/*     */     {
/* 559 */       if (shouldIgnore(e)) {
/* 560 */         return;
/*     */       }
/* 562 */       cacheResizingColumn(e);
/* 563 */       if ((isInResizeRegion(e)) && (e.getClickCount() % 2 == 1)) {
/* 564 */         disableToggleSortOrder(e);
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
/*     */     private boolean shouldIgnore(MouseEvent e)
/*     */     {
/* 577 */       return (!SwingUtilities.isLeftMouseButton(e)) || (!JXTableHeader.this.table.isEnabled());
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     private void doResize(MouseEvent e)
/*     */     {
/* 588 */       if (e.getClickCount() != 2)
/* 589 */         return;
/* 590 */       int column = JXTableHeader.this.getViewIndexForColumn(this.cachedResizingColumn);
/* 591 */       if (column >= 0) {
/* 592 */         JXTableHeader.this.getXTable().packColumn(column, 5);
/*     */       }
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     private void disableToggleSortOrder(MouseEvent e)
/*     */     {
/* 602 */       if (!(JXTableHeader.this.getXTable().getRowSorter() instanceof SortController)) return;
/* 603 */       SortController<?> controller = (SortController)JXTableHeader.this.getXTable().getRowSorter();
/* 604 */       this.cachedSortOrderCycle = controller.getSortOrderCycle();
/* 605 */       controller.setSortOrderCycle(new SortOrder[0]);
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */     private void resetToggleSortOrder(MouseEvent e)
/*     */     {
/* 612 */       if (this.cachedSortOrderCycle == null) return;
/* 613 */       ((SortController)JXTableHeader.this.getXTable().getRowSorter()).setSortOrderCycle(this.cachedSortOrderCycle);
/* 614 */       this.cachedSortOrderCycle = null;
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     private void cacheResizingColumn(MouseEvent e)
/*     */     {
/* 624 */       TableColumn column = JXTableHeader.this.getResizingColumn();
/* 625 */       if (column != null) {
/* 626 */         this.cachedResizingColumn = column;
/*     */       }
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */     private void uncacheResizingColumn()
/*     */     {
/* 634 */       this.cachedResizingColumn = null;
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     private boolean isInResizeRegion(MouseEvent e)
/*     */     {
/* 644 */       return this.cachedResizingColumn != null;
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */     public void mouseEntered(MouseEvent e) {}
/*     */     
/*     */ 
/*     */     public void mouseExited(MouseEvent e)
/*     */     {
/* 654 */       uncacheResizingColumn();
/* 655 */       resetToggleSortOrder(e);
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */     public void mouseDragged(MouseEvent e)
/*     */     {
/* 662 */       uncacheResizingColumn();
/* 663 */       resetToggleSortOrder(e);
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */     public void mouseMoved(MouseEvent e)
/*     */     {
/* 670 */       resetToggleSortOrder(e);
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
/*     */   public SortGestureRecognizer getSortGestureRecognizer()
/*     */   {
/* 699 */     if (this.sortGestureRecognizer == null) {
/* 700 */       this.sortGestureRecognizer = createSortGestureRecognizer();
/*     */     }
/* 702 */     return this.sortGestureRecognizer;
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
/*     */   @Deprecated
/*     */   public void setSortGestureRecognizer(SortGestureRecognizer recognizer)
/*     */   {
/* 723 */     SortGestureRecognizer old = getSortGestureRecognizer();
/* 724 */     this.sortGestureRecognizer = recognizer;
/* 725 */     firePropertyChange("sortGestureRecognizer", old, getSortGestureRecognizer());
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
/*     */   @Deprecated
/*     */   protected SortGestureRecognizer createSortGestureRecognizer()
/*     */   {
/* 740 */     return new SortGestureRecognizer();
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
/*     */   public JXTableHeader() {}
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
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
/*     */   public static class SortGestureRecognizer
/*     */   {
/*     */     public boolean isResetSortOrderGesture(MouseEvent e)
/*     */     {
/* 784 */       return (isSortOrderGesture(e)) && (isResetModifier(e));
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     public boolean isToggleSortOrderGesture(MouseEvent e)
/*     */     {
/* 794 */       return (isSortOrderGesture(e)) && (!isResetModifier(e));
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     public boolean isSortOrderGesture(MouseEvent e)
/*     */     {
/* 804 */       return e.getClickCount() == 1;
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     protected boolean isResetModifier(MouseEvent e)
/*     */     {
/* 816 */       return (e.getModifiersEx() & 0x40) == 64;
/*     */     }
/*     */   }
/*     */ }


/* Location:              E:\java\java学习\hutubill\lib\all.jar!\org\jdesktop\swingx\JXTableHeader.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */
/*      */ package org.jdesktop.swingx.plaf.basic.core;
/*      */ 
/*      */ import java.awt.Color;
/*      */ import java.awt.Component;
/*      */ import java.awt.Component.BaselineResizeBehavior;
/*      */ import java.awt.ComponentOrientation;
/*      */ import java.awt.Dimension;
/*      */ import java.awt.Font;
/*      */ import java.awt.Graphics;
/*      */ import java.awt.Insets;
/*      */ import java.awt.Point;
/*      */ import java.awt.Rectangle;
/*      */ import java.awt.Shape;
/*      */ import java.awt.datatransfer.Transferable;
/*      */ import java.awt.event.ActionEvent;
/*      */ import java.awt.event.FocusEvent;
/*      */ import java.awt.event.FocusListener;
/*      */ import java.awt.event.KeyEvent;
/*      */ import java.awt.event.KeyListener;
/*      */ import java.awt.event.MouseEvent;
/*      */ import java.awt.geom.Point2D;
/*      */ import java.beans.PropertyChangeEvent;
/*      */ import java.beans.PropertyChangeListener;
/*      */ import javax.swing.Action;
/*      */ import javax.swing.CellRendererPane;
/*      */ import javax.swing.DefaultListCellRenderer;
/*      */ import javax.swing.DefaultListSelectionModel;
/*      */ import javax.swing.InputMap;
/*      */ import javax.swing.JComponent;
/*      */ import javax.swing.JList;
/*      */ import javax.swing.JList.DropLocation;
/*      */ import javax.swing.KeyStroke;
/*      */ import javax.swing.ListCellRenderer;
/*      */ import javax.swing.ListModel;
/*      */ import javax.swing.ListSelectionModel;
/*      */ import javax.swing.LookAndFeel;
/*      */ import javax.swing.SwingUtilities;
/*      */ import javax.swing.TransferHandler;
/*      */ import javax.swing.UIDefaults;
/*      */ import javax.swing.UIManager;
/*      */ import javax.swing.event.ListDataEvent;
/*      */ import javax.swing.event.ListDataListener;
/*      */ import javax.swing.event.ListSelectionEvent;
/*      */ import javax.swing.event.ListSelectionListener;
/*      */ import javax.swing.event.MouseInputListener;
/*      */ import javax.swing.plaf.ComponentUI;
/*      */ import javax.swing.plaf.UIResource;
/*      */ import javax.swing.plaf.basic.BasicListUI;
/*      */ import javax.swing.text.Position.Bias;
/*      */ import org.jdesktop.swingx.JXList;
/*      */ import org.jdesktop.swingx.SwingXUtilities;
/*      */ import org.jdesktop.swingx.UIAction;
/*      */ import org.jdesktop.swingx.plaf.LookAndFeelUtils;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ public class BasicXListUI
/*      */   extends BasicListUI
/*      */ {
/*  139 */   private static final StringBuilder BASELINE_COMPONENT_KEY = new StringBuilder("List.baselineComponent");
/*      */   protected JXList list;
/*      */   
/*  142 */   public BasicXListUI() { this.list = null;
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  153 */     this.cellHeights = null;
/*  154 */     this.cellHeight = -1;
/*  155 */     this.cellWidth = -1;
/*  156 */     this.updateLayoutStateNeeded = 1;
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  195 */     this.timeFactor = 1000L;
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*  200 */     this.isFileList = false;
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*  205 */     this.isLeftToRight = true;
/*      */   }
/*      */   
/*      */ 
/*      */   protected CellRendererPane rendererPane;
/*      */   
/*      */   protected FocusListener focusListener;
/*      */   protected MouseInputListener mouseInputListener;
/*      */   protected ListSelectionListener listSelectionListener;
/*      */   protected ListDataListener listDataListener;
/*      */   protected PropertyChangeListener propertyChangeListener;
/*      */   private Handler handler;
/*      */   protected int[] cellHeights;
/*      */   protected int cellHeight;
/*      */   protected int cellWidth;
/*      */   protected int updateLayoutStateNeeded;
/*      */   private int listHeight;
/*      */   private int listWidth;
/*      */   private int layoutOrientation;
/*      */   private int columnCount;
/*      */   private int preferredHeight;
/*      */   private int rowsPerColumn;
/*      */   private long timeFactor;
/*      */   public static void loadActionMap(LazyActionMap map)
/*      */   {
/*  230 */     map.put(new Actions("selectPreviousColumn"));
/*  231 */     map.put(new Actions("selectPreviousColumnExtendSelection"));
/*  232 */     map.put(new Actions("selectPreviousColumnChangeLead"));
/*  233 */     map.put(new Actions("selectNextColumn"));
/*  234 */     map.put(new Actions("selectNextColumnExtendSelection"));
/*  235 */     map.put(new Actions("selectNextColumnChangeLead"));
/*  236 */     map.put(new Actions("selectPreviousRow"));
/*  237 */     map.put(new Actions("selectPreviousRowExtendSelection"));
/*  238 */     map.put(new Actions("selectPreviousRowChangeLead"));
/*  239 */     map.put(new Actions("selectNextRow"));
/*  240 */     map.put(new Actions("selectNextRowExtendSelection"));
/*  241 */     map.put(new Actions("selectNextRowChangeLead"));
/*  242 */     map.put(new Actions("selectFirstRow"));
/*  243 */     map.put(new Actions("selectFirstRowExtendSelection"));
/*  244 */     map.put(new Actions("selectFirstRowChangeLead"));
/*  245 */     map.put(new Actions("selectLastRow"));
/*  246 */     map.put(new Actions("selectLastRowExtendSelection"));
/*  247 */     map.put(new Actions("selectLastRowChangeLead"));
/*  248 */     map.put(new Actions("scrollUp"));
/*  249 */     map.put(new Actions("scrollUpExtendSelection"));
/*  250 */     map.put(new Actions("scrollUpChangeLead"));
/*  251 */     map.put(new Actions("scrollDown"));
/*  252 */     map.put(new Actions("scrollDownExtendSelection"));
/*  253 */     map.put(new Actions("scrollDownChangeLead"));
/*  254 */     map.put(new Actions("selectAll"));
/*  255 */     map.put(new Actions("clearSelection"));
/*  256 */     map.put(new Actions("addToSelection"));
/*  257 */     map.put(new Actions("toggleAndAnchor"));
/*  258 */     map.put(new Actions("extendTo"));
/*  259 */     map.put(new Actions("moveSelectionTo"));
/*      */     
/*  261 */     map.put(TransferHandler.getCutAction().getValue("Name"), TransferHandler.getCutAction());
/*      */     
/*  263 */     map.put(TransferHandler.getCopyAction().getValue("Name"), TransferHandler.getCopyAction());
/*      */     
/*  265 */     map.put(TransferHandler.getPasteAction().getValue("Name"), TransferHandler.getPasteAction());
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
/*      */   protected ListModel getViewModel()
/*      */   {
/*  279 */     if (this.modelX == null) {
/*  280 */       this.modelX = new ListModel()
/*      */       {
/*      */         public int getSize()
/*      */         {
/*  284 */           return BasicXListUI.this.list.getElementCount();
/*      */         }
/*      */         
/*      */         public Object getElementAt(int index)
/*      */         {
/*  289 */           return BasicXListUI.this.list.getElementAt(index);
/*      */         }
/*      */         
/*      */         public void addListDataListener(ListDataListener l)
/*      */         {
/*  294 */           throw new UnsupportedOperationException("this is a synthetic model wrapper");
/*      */         }
/*      */         
/*      */         public void removeListDataListener(ListDataListener l) {
/*  298 */           throw new UnsupportedOperationException("this is a synthetic model wrapper");
/*      */         }
/*      */       };
/*      */     }
/*      */     
/*      */ 
/*  304 */     return this.modelX;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   protected int getElementCount()
/*      */   {
/*  311 */     return this.list.getElementCount();
/*      */   }
/*      */   
/*      */   protected Object getElementAt(int viewIndex) {
/*  315 */     return this.list.getElementAt(viewIndex);
/*      */   }
/*      */   
/*      */ 
/*      */   protected ListSortUI getSortUI()
/*      */   {
/*  321 */     return this.sortUI;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   protected void installSortUI()
/*      */   {
/*  328 */     if (this.list.getRowSorter() == null) return;
/*  329 */     this.sortUI = new ListSortUI(this.list, this.list.getRowSorter());
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   protected void uninstallSortUI()
/*      */   {
/*  336 */     if (this.sortUI == null) return;
/*  337 */     this.sortUI.dispose();
/*  338 */     this.sortUI = null;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected void updateSortUI(String property)
/*      */   {
/*  347 */     if ("rowSorter".equals(property)) {
/*  348 */       updateSortUIToRowSorterProperty();
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */   private void updateSortUIToRowSorterProperty()
/*      */   {
/*  355 */     uninstallSortUI();
/*  356 */     installSortUI();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected boolean processedBySortUI(ListDataEvent e)
/*      */   {
/*  366 */     if (this.sortUI == null)
/*  367 */       return false;
/*  368 */     this.sortUI.modelChanged(e);
/*  369 */     this.updateLayoutStateNeeded = 1;
/*  370 */     redrawList();
/*  371 */     return true;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected boolean processedBySortUI(ListSelectionEvent e)
/*      */   {
/*  381 */     if (this.sortUI == null) return false;
/*  382 */     this.sortUI.viewSelectionChanged(e);
/*  383 */     this.list.repaint();
/*  384 */     return true;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void invalidateCellSizeCache()
/*      */   {
/*  394 */     this.updateLayoutStateNeeded |= 0x1;
/*  395 */     redrawList();
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
/*      */   protected void paintCell(Graphics g, int row, Rectangle rowBounds, ListCellRenderer cellRenderer, ListModel dataModel, ListSelectionModel selModel, int leadIndex)
/*      */   {
/*  417 */     Object value = dataModel.getElementAt(row);
/*  418 */     boolean cellHasFocus = (this.list.hasFocus()) && (row == leadIndex);
/*  419 */     boolean isSelected = selModel.isSelectedIndex(row);
/*      */     
/*  421 */     Component rendererComponent = cellRenderer.getListCellRendererComponent(this.list, value, row, isSelected, cellHasFocus);
/*      */     
/*      */ 
/*  424 */     int cx = rowBounds.x;
/*  425 */     int cy = rowBounds.y;
/*  426 */     int cw = rowBounds.width;
/*  427 */     int ch = rowBounds.height;
/*      */     
/*  429 */     if (this.isFileList)
/*      */     {
/*      */ 
/*      */ 
/*  433 */       int w = Math.min(cw, rendererComponent.getPreferredSize().width + 4);
/*  434 */       if (!this.isLeftToRight) {
/*  435 */         cx += cw - w;
/*      */       }
/*  437 */       cw = w;
/*      */     }
/*      */     
/*  440 */     this.rendererPane.paintComponent(g, rendererComponent, this.list, cx, cy, cw, ch, true);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void paint(Graphics g, JComponent c)
/*      */   {
/*  452 */     Shape clip = g.getClip();
/*  453 */     paintImpl(g, c);
/*  454 */     g.setClip(clip);
/*      */     
/*  456 */     paintDropLine(g);
/*      */   }
/*      */   
/*      */   private void paintImpl(Graphics g, JComponent c)
/*      */   {
/*  461 */     switch (this.layoutOrientation) {
/*      */     case 1: 
/*  463 */       if (this.list.getHeight() != this.listHeight) {
/*  464 */         this.updateLayoutStateNeeded |= 0x100;
/*  465 */         redrawList();
/*      */       }
/*      */       break;
/*      */     case 2: 
/*  469 */       if (this.list.getWidth() != this.listWidth) {
/*  470 */         this.updateLayoutStateNeeded |= 0x200;
/*  471 */         redrawList();
/*      */       }
/*      */       
/*      */       break;
/*      */     }
/*      */     
/*  477 */     maybeUpdateLayoutState();
/*      */     
/*  479 */     ListCellRenderer renderer = this.list.getCellRenderer();
/*  480 */     ListModel dataModel = getViewModel();
/*  481 */     ListSelectionModel selModel = this.list.getSelectionModel();
/*      */     
/*      */     int size;
/*  484 */     if ((renderer == null) || ((size = dataModel.getSize()) == 0)) {
/*      */       return;
/*      */     }
/*      */     
/*      */     int size;
/*  489 */     Rectangle paintBounds = g.getClipBounds();
/*      */     int endColumn;
/*      */     int startColumn;
/*  492 */     int endColumn; if (c.getComponentOrientation().isLeftToRight()) {
/*  493 */       int startColumn = convertLocationToColumn(paintBounds.x, paintBounds.y);
/*      */       
/*  495 */       endColumn = convertLocationToColumn(paintBounds.x + paintBounds.width, paintBounds.y);
/*      */     }
/*      */     else
/*      */     {
/*  499 */       startColumn = convertLocationToColumn(paintBounds.x + paintBounds.width, paintBounds.y);
/*      */       
/*      */ 
/*  502 */       endColumn = convertLocationToColumn(paintBounds.x, paintBounds.y);
/*      */     }
/*      */     
/*  505 */     int maxY = paintBounds.y + paintBounds.height;
/*  506 */     int leadIndex = adjustIndex(this.list.getLeadSelectionIndex(), this.list);
/*  507 */     int rowIncrement = this.layoutOrientation == 2 ? this.columnCount : 1;
/*      */     
/*      */ 
/*      */ 
/*  511 */     for (int colCounter = startColumn; colCounter <= endColumn; 
/*  512 */         colCounter++)
/*      */     {
/*  514 */       int row = convertLocationToRowInColumn(paintBounds.y, colCounter);
/*  515 */       int rowCount = getRowCount(colCounter);
/*  516 */       int index = getModelIndex(colCounter, row);
/*  517 */       Rectangle rowBounds = getCellBounds(this.list, index, index);
/*      */       
/*  519 */       if (rowBounds == null)
/*      */       {
/*  521 */         return;
/*      */       }
/*      */       
/*  524 */       while ((row < rowCount) && (rowBounds.y < maxY) && (index < size)) {
/*  525 */         rowBounds.height = getHeight(colCounter, row);
/*  526 */         g.setClip(rowBounds.x, rowBounds.y, rowBounds.width, rowBounds.height);
/*      */         
/*  528 */         g.clipRect(paintBounds.x, paintBounds.y, paintBounds.width, paintBounds.height);
/*      */         
/*  530 */         paintCell(g, index, rowBounds, renderer, dataModel, selModel, leadIndex);
/*      */         
/*  532 */         rowBounds.y += rowBounds.height;
/*  533 */         index += rowIncrement;
/*  534 */         row++;
/*      */       }
/*      */     }
/*      */     
/*  538 */     this.rendererPane.removeAll();
/*      */   }
/*      */   
/*      */ 
/*      */   private void paintDropLine(Graphics g)
/*      */   {
/*  544 */     JList.DropLocation loc = this.list.getDropLocation();
/*  545 */     if ((loc == null) || (!loc.isInsert())) {
/*  546 */       return;
/*      */     }
/*      */     
/*      */ 
/*  550 */     Color c = UIManager.getColor("List.dropLineColor");
/*  551 */     if (c != null) {
/*  552 */       g.setColor(c);
/*  553 */       Rectangle rect = getDropLineRect(loc);
/*  554 */       g.fillRect(rect.x, rect.y, rect.width, rect.height);
/*      */     }
/*      */   }
/*      */   
/*      */   private Rectangle getDropLineRect(JList.DropLocation loc) {
/*  559 */     int size = getElementCount();
/*      */     
/*  561 */     if (size == 0) {
/*  562 */       Insets insets = this.list.getInsets();
/*  563 */       if (this.layoutOrientation == 2) {
/*  564 */         if (this.isLeftToRight) {
/*  565 */           return new Rectangle(insets.left, insets.top, 2, 20);
/*      */         }
/*  567 */         return new Rectangle(this.list.getWidth() - 2 - insets.right, insets.top, 2, 20);
/*      */       }
/*      */       
/*      */ 
/*  571 */       return new Rectangle(insets.left, insets.top, this.list.getWidth() - insets.left - insets.right, 2);
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*  577 */     Rectangle rect = null;
/*  578 */     int index = loc.getIndex();
/*  579 */     boolean decr = false;
/*      */     
/*  581 */     if (this.layoutOrientation == 2) {
/*  582 */       if (index == size) {
/*  583 */         decr = true;
/*  584 */       } else if ((index != 0) && (convertModelToRow(index) != convertModelToRow(index - 1)))
/*      */       {
/*      */ 
/*  587 */         Rectangle prev = getCellBounds(this.list, index - 1);
/*  588 */         Rectangle me = getCellBounds(this.list, index);
/*  589 */         Point p = loc.getDropPoint();
/*      */         
/*  591 */         if (this.isLeftToRight) {
/*  592 */           decr = Point2D.distance(prev.x + prev.width, prev.y + (int)(prev.height / 2.0D), p.x, p.y) < Point2D.distance(me.x, me.y + (int)(me.height / 2.0D), p.x, p.y);
/*      */ 
/*      */ 
/*      */         }
/*      */         else
/*      */         {
/*      */ 
/*  599 */           decr = Point2D.distance(prev.x, prev.y + (int)(prev.height / 2.0D), p.x, p.y) < Point2D.distance(me.x + me.width, me.y + (int)(prev.height / 2.0D), p.x, p.y);
/*      */         }
/*      */       }
/*      */       
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  608 */       if (decr) {
/*  609 */         index--;
/*  610 */         rect = getCellBounds(this.list, index);
/*  611 */         if (this.isLeftToRight) {
/*  612 */           rect.x += rect.width;
/*      */         } else {
/*  614 */           rect.x -= 2;
/*      */         }
/*      */       } else {
/*  617 */         rect = getCellBounds(this.list, index);
/*  618 */         if (!this.isLeftToRight) {
/*  619 */           rect.x += rect.width - 2;
/*      */         }
/*      */       }
/*      */       
/*  623 */       if (rect.x >= this.list.getWidth()) {
/*  624 */         rect.x = (this.list.getWidth() - 2);
/*  625 */       } else if (rect.x < 0) {
/*  626 */         rect.x = 0;
/*      */       }
/*      */       
/*  629 */       rect.width = 2;
/*  630 */     } else if (this.layoutOrientation == 1) {
/*  631 */       if (index == size) {
/*  632 */         index--;
/*  633 */         rect = getCellBounds(this.list, index);
/*  634 */         rect.y += rect.height;
/*  635 */       } else if ((index != 0) && (convertModelToColumn(index) != convertModelToColumn(index - 1)))
/*      */       {
/*      */ 
/*  638 */         Rectangle prev = getCellBounds(this.list, index - 1);
/*  639 */         Rectangle me = getCellBounds(this.list, index);
/*  640 */         Point p = loc.getDropPoint();
/*  641 */         if (Point2D.distance(prev.x + (int)(prev.width / 2.0D), prev.y + prev.height, p.x, p.y) < Point2D.distance(me.x + (int)(me.width / 2.0D), me.y, p.x, p.y))
/*      */         {
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  648 */           index--;
/*  649 */           rect = getCellBounds(this.list, index);
/*  650 */           rect.y += rect.height;
/*      */         } else {
/*  652 */           rect = getCellBounds(this.list, index);
/*      */         }
/*      */       } else {
/*  655 */         rect = getCellBounds(this.list, index);
/*      */       }
/*      */       
/*  658 */       if (rect.y >= this.list.getHeight()) {
/*  659 */         rect.y = (this.list.getHeight() - 2);
/*      */       }
/*      */       
/*  662 */       rect.height = 2;
/*      */     } else {
/*  664 */       if (index == size) {
/*  665 */         index--;
/*  666 */         rect = getCellBounds(this.list, index);
/*  667 */         rect.y += rect.height;
/*      */       } else {
/*  669 */         rect = getCellBounds(this.list, index);
/*      */       }
/*      */       
/*  672 */       if (rect.y >= this.list.getHeight()) {
/*  673 */         rect.y = (this.list.getHeight() - 2);
/*      */       }
/*      */       
/*  676 */       rect.height = 2;
/*      */     }
/*      */     
/*  679 */     return rect;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public int getBaseline(JComponent c, int width, int height)
/*      */   {
/*  691 */     super.getBaseline(c, width, height);
/*  692 */     int rowHeight = this.list.getFixedCellHeight();
/*  693 */     UIDefaults lafDefaults = UIManager.getLookAndFeelDefaults();
/*  694 */     Component renderer = (Component)lafDefaults.get(BASELINE_COMPONENT_KEY);
/*      */     
/*  696 */     if (renderer == null) {
/*  697 */       ListCellRenderer lcr = (ListCellRenderer)UIManager.get("List.cellRenderer");
/*      */       
/*      */ 
/*      */ 
/*      */ 
/*  702 */       if (lcr == null) {
/*  703 */         lcr = new DefaultListCellRenderer();
/*      */       }
/*      */       
/*  706 */       renderer = lcr.getListCellRendererComponent(this.list, "a", -1, false, false);
/*      */       
/*  708 */       lafDefaults.put(BASELINE_COMPONENT_KEY, renderer);
/*      */     }
/*  710 */     renderer.setFont(this.list.getFont());
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  718 */     if (rowHeight == -1) {
/*  719 */       rowHeight = renderer.getPreferredSize().height;
/*      */     }
/*  721 */     return renderer.getBaseline(Integer.MAX_VALUE, rowHeight) + this.list.getInsets().top;
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
/*      */   public Component.BaselineResizeBehavior getBaselineResizeBehavior(JComponent c)
/*      */   {
/*  735 */     super.getBaselineResizeBehavior(c);
/*  736 */     return Component.BaselineResizeBehavior.CONSTANT_ASCENT;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   private boolean isFileList;
/*      */   
/*      */ 
/*      */   private boolean isLeftToRight;
/*      */   
/*      */ 
/*      */   protected static final int modelChanged = 1;
/*      */   
/*      */ 
/*      */   protected static final int selectionModelChanged = 2;
/*      */   
/*      */ 
/*      */   protected static final int fontChanged = 4;
/*      */   
/*      */ 
/*      */   protected static final int fixedCellWidthChanged = 8;
/*      */   
/*      */ 
/*      */   protected static final int fixedCellHeightChanged = 16;
/*      */   
/*      */ 
/*      */   protected static final int prototypeCellValueChanged = 32;
/*      */   
/*      */ 
/*      */   protected static final int cellRendererChanged = 64;
/*      */   
/*      */ 
/*      */   private static final int layoutOrientationChanged = 128;
/*      */   
/*      */ 
/*      */   private static final int heightChanged = 256;
/*      */   
/*      */ 
/*      */   private static final int widthChanged = 512;
/*      */   
/*      */ 
/*      */   private static final int componentOrientationChanged = 1024;
/*      */   
/*      */ 
/*      */   private static final int DROP_LINE_THICKNESS = 2;
/*      */   
/*      */ 
/*      */   private ListModel modelX;
/*      */   
/*      */   private ListSortUI sortUI;
/*      */   
/*      */   private static final int CHANGE_LEAD = 0;
/*      */   
/*      */   private static final int CHANGE_SELECTION = 1;
/*      */   
/*      */   private static final int EXTEND_SELECTION = 2;
/*      */   
/*      */   public Dimension getPreferredSize(JComponent c)
/*      */   {
/*  795 */     maybeUpdateLayoutState();
/*      */     
/*  797 */     int lastRow = getElementCount() - 1;
/*  798 */     if (lastRow < 0) {
/*  799 */       return new Dimension(0, 0);
/*      */     }
/*      */     
/*  802 */     Insets insets = this.list.getInsets();
/*  803 */     int width = this.cellWidth * this.columnCount + insets.left + insets.right;
/*      */     int height;
/*      */     int height;
/*  806 */     if (this.layoutOrientation != 0) {
/*  807 */       height = this.preferredHeight;
/*      */     }
/*      */     else {
/*  810 */       Rectangle bounds = getCellBounds(this.list, lastRow);
/*      */       int height;
/*  812 */       if (bounds != null) {
/*  813 */         height = bounds.y + bounds.height + insets.bottom;
/*      */       }
/*      */       else {
/*  816 */         height = 0;
/*      */       }
/*      */     }
/*  819 */     return new Dimension(width, height);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected void selectPreviousIndex()
/*      */   {
/*  829 */     int s = this.list.getSelectedIndex();
/*  830 */     if (s > 0) {
/*  831 */       s--;
/*  832 */       this.list.setSelectedIndex(s);
/*  833 */       this.list.ensureIndexIsVisible(s);
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected void selectNextIndex()
/*      */   {
/*  845 */     int s = this.list.getSelectedIndex();
/*  846 */     if (s + 1 < getElementCount()) {
/*  847 */       s++;
/*  848 */       this.list.setSelectedIndex(s);
/*  849 */       this.list.ensureIndexIsVisible(s);
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
/*      */   protected void installKeyboardActions()
/*      */   {
/*  862 */     InputMap inputMap = getInputMap(0);
/*      */     
/*  864 */     SwingUtilities.replaceUIInputMap(this.list, 0, inputMap);
/*      */     
/*      */ 
/*  867 */     LazyActionMap.installLazyActionMap(this.list, BasicXListUI.class, "XList.actionMap");
/*      */   }
/*      */   
/*      */   InputMap getInputMap(int condition)
/*      */   {
/*  872 */     if (condition == 0)
/*      */     {
/*  874 */       InputMap keyMap = (InputMap)UIManager.get("List.focusInputMap");
/*      */       
/*      */ 
/*      */       InputMap rtlKeyMap;
/*      */       
/*  879 */       if ((this.isLeftToRight) || ((rtlKeyMap = (InputMap)UIManager.get("List.focusInputMap.RightToLeft")) == null))
/*      */       {
/*      */ 
/*      */ 
/*      */ 
/*  884 */         return keyMap; }
/*      */       InputMap rtlKeyMap;
/*  886 */       rtlKeyMap.setParent(keyMap);
/*  887 */       return rtlKeyMap;
/*      */     }
/*      */     
/*  890 */     return null;
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
/*      */   protected void uninstallKeyboardActions()
/*      */   {
/*  903 */     SwingUtilities.replaceUIActionMap(this.list, null);
/*  904 */     SwingUtilities.replaceUIInputMap(this.list, 0, null);
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
/*      */   protected void installListeners()
/*      */   {
/*  917 */     TransferHandler th = this.list.getTransferHandler();
/*  918 */     if ((th == null) || ((th instanceof UIResource))) {
/*  919 */       this.list.setTransferHandler(defaultTransferHandler);
/*      */       
/*      */ 
/*  922 */       if ((this.list.getDropTarget() instanceof UIResource)) {
/*  923 */         this.list.setDropTarget(null);
/*      */       }
/*      */     }
/*      */     
/*  927 */     this.focusListener = createFocusListener();
/*  928 */     this.mouseInputListener = createMouseInputListener();
/*  929 */     this.propertyChangeListener = createPropertyChangeListener();
/*  930 */     this.listSelectionListener = createListSelectionListener();
/*  931 */     this.listDataListener = createListDataListener();
/*      */     
/*  933 */     this.list.addFocusListener(this.focusListener);
/*  934 */     this.list.addMouseListener(this.mouseInputListener);
/*  935 */     this.list.addMouseMotionListener(this.mouseInputListener);
/*  936 */     this.list.addPropertyChangeListener(this.propertyChangeListener);
/*  937 */     this.list.addKeyListener(getHandler());
/*      */     
/*  939 */     ListModel model = this.list.getModel();
/*  940 */     if (model != null) {
/*  941 */       model.addListDataListener(this.listDataListener);
/*      */     }
/*      */     
/*  944 */     ListSelectionModel selectionModel = this.list.getSelectionModel();
/*  945 */     if (selectionModel != null) {
/*  946 */       selectionModel.addListSelectionListener(this.listSelectionListener);
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
/*      */   protected void uninstallListeners()
/*      */   {
/*  962 */     this.list.removeFocusListener(this.focusListener);
/*  963 */     this.list.removeMouseListener(this.mouseInputListener);
/*  964 */     this.list.removeMouseMotionListener(this.mouseInputListener);
/*  965 */     this.list.removePropertyChangeListener(this.propertyChangeListener);
/*  966 */     this.list.removeKeyListener(getHandler());
/*      */     
/*  968 */     ListModel model = this.list.getModel();
/*  969 */     if (model != null) {
/*  970 */       model.removeListDataListener(this.listDataListener);
/*      */     }
/*      */     
/*  973 */     ListSelectionModel selectionModel = this.list.getSelectionModel();
/*  974 */     if (selectionModel != null) {
/*  975 */       selectionModel.removeListSelectionListener(this.listSelectionListener);
/*      */     }
/*      */     
/*  978 */     this.focusListener = null;
/*  979 */     this.mouseInputListener = null;
/*  980 */     this.listSelectionListener = null;
/*  981 */     this.listDataListener = null;
/*  982 */     this.propertyChangeListener = null;
/*  983 */     this.handler = null;
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
/*      */   protected void installDefaults()
/*      */   {
/* 1000 */     this.list.setLayout(null);
/*      */     
/* 1002 */     LookAndFeel.installBorder(this.list, "List.border");
/*      */     
/* 1004 */     LookAndFeel.installColorsAndFont(this.list, "List.background", "List.foreground", "List.font");
/*      */     
/* 1006 */     LookAndFeel.installProperty(this.list, "opaque", Boolean.TRUE);
/*      */     
/* 1008 */     if (this.list.getCellRenderer() == null) {
/* 1009 */       this.list.setCellRenderer((ListCellRenderer)UIManager.get("List.cellRenderer"));
/*      */     }
/*      */     
/* 1012 */     Color sbg = this.list.getSelectionBackground();
/* 1013 */     if ((sbg == null) || ((sbg instanceof UIResource))) {
/* 1014 */       this.list.setSelectionBackground(UIManager.getColor("List.selectionBackground"));
/*      */     }
/*      */     
/* 1017 */     Color sfg = this.list.getSelectionForeground();
/* 1018 */     if ((sfg == null) || ((sfg instanceof UIResource))) {
/* 1019 */       this.list.setSelectionForeground(UIManager.getColor("List.selectionForeground"));
/*      */     }
/*      */     
/* 1022 */     Long l = (Long)UIManager.get("List.timeFactor");
/* 1023 */     this.timeFactor = (l != null ? l.longValue() : 1000L);
/*      */     
/* 1025 */     updateIsFileList();
/*      */   }
/*      */   
/*      */   private void updateIsFileList() {
/* 1029 */     boolean b = Boolean.TRUE.equals(this.list.getClientProperty("List.isFileList"));
/* 1030 */     if (b != this.isFileList) {
/* 1031 */       this.isFileList = b;
/* 1032 */       Font oldFont = this.list.getFont();
/* 1033 */       if ((oldFont == null) || ((oldFont instanceof UIResource))) {
/* 1034 */         Font newFont = UIManager.getFont(b ? "FileChooser.listFont" : "List.font");
/* 1035 */         if ((newFont != null) && (newFont != oldFont)) {
/* 1036 */           this.list.setFont(newFont);
/*      */         }
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
/*      */   protected void uninstallDefaults()
/*      */   {
/* 1054 */     LookAndFeel.uninstallBorder(this.list);
/* 1055 */     if ((this.list.getFont() instanceof UIResource)) {
/* 1056 */       this.list.setFont(null);
/*      */     }
/* 1058 */     if ((this.list.getForeground() instanceof UIResource)) {
/* 1059 */       this.list.setForeground(null);
/*      */     }
/* 1061 */     if ((this.list.getBackground() instanceof UIResource)) {
/* 1062 */       this.list.setBackground(null);
/*      */     }
/* 1064 */     if ((this.list.getSelectionBackground() instanceof UIResource)) {
/* 1065 */       this.list.setSelectionBackground(null);
/*      */     }
/* 1067 */     if ((this.list.getSelectionForeground() instanceof UIResource)) {
/* 1068 */       this.list.setSelectionForeground(null);
/*      */     }
/* 1070 */     if ((this.list.getCellRenderer() instanceof UIResource)) {
/* 1071 */       this.list.setCellRenderer(null);
/*      */     }
/* 1073 */     if ((this.list.getTransferHandler() instanceof UIResource)) {
/* 1074 */       this.list.setTransferHandler(null);
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
/*      */   public void installUI(JComponent c)
/*      */   {
/* 1090 */     this.list = ((JXList)c);
/*      */     
/* 1092 */     this.layoutOrientation = this.list.getLayoutOrientation();
/*      */     
/* 1094 */     this.rendererPane = new CellRendererPane();
/* 1095 */     this.list.add(this.rendererPane);
/*      */     
/* 1097 */     this.columnCount = 1;
/*      */     
/* 1099 */     this.updateLayoutStateNeeded = 1;
/* 1100 */     this.isLeftToRight = this.list.getComponentOrientation().isLeftToRight();
/*      */     
/* 1102 */     installDefaults();
/* 1103 */     installListeners();
/* 1104 */     installKeyboardActions();
/* 1105 */     installSortUI();
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
/*      */   public void uninstallUI(JComponent c)
/*      */   {
/* 1120 */     uninstallSortUI();
/* 1121 */     uninstallListeners();
/* 1122 */     uninstallDefaults();
/* 1123 */     uninstallKeyboardActions();
/*      */     
/* 1125 */     this.cellWidth = (this.cellHeight = -1);
/* 1126 */     this.cellHeights = null;
/*      */     
/* 1128 */     this.listWidth = (this.listHeight = -1);
/*      */     
/* 1130 */     this.list.remove(this.rendererPane);
/* 1131 */     this.rendererPane = null;
/* 1132 */     this.list = null;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public static ComponentUI createUI(JComponent list)
/*      */   {
/* 1143 */     return new BasicXListUI();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public int locationToIndex(JList list, Point location)
/*      */   {
/* 1152 */     maybeUpdateLayoutState();
/* 1153 */     return convertLocationToModel(location.x, location.y);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public Point indexToLocation(JList list, int index)
/*      */   {
/* 1161 */     maybeUpdateLayoutState();
/* 1162 */     Rectangle rect = getCellBounds(list, index, index);
/*      */     
/* 1164 */     if (rect != null) {
/* 1165 */       return new Point(rect.x, rect.y);
/*      */     }
/* 1167 */     return null;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public Rectangle getCellBounds(JList list, int index1, int index2)
/*      */   {
/* 1175 */     maybeUpdateLayoutState();
/*      */     
/* 1177 */     int minIndex = Math.min(index1, index2);
/* 1178 */     int maxIndex = Math.max(index1, index2);
/*      */     
/* 1180 */     if (minIndex >= getElementCount()) {
/* 1181 */       return null;
/*      */     }
/*      */     
/* 1184 */     Rectangle minBounds = getCellBounds(list, minIndex);
/*      */     
/* 1186 */     if (minBounds == null) {
/* 1187 */       return null;
/*      */     }
/* 1189 */     if (minIndex == maxIndex) {
/* 1190 */       return minBounds;
/*      */     }
/* 1192 */     Rectangle maxBounds = getCellBounds(list, maxIndex);
/*      */     
/* 1194 */     if (maxBounds != null) {
/* 1195 */       if (this.layoutOrientation == 2) {
/* 1196 */         int minRow = convertModelToRow(minIndex);
/* 1197 */         int maxRow = convertModelToRow(maxIndex);
/*      */         
/* 1199 */         if (minRow != maxRow) {
/* 1200 */           minBounds.x = 0;
/* 1201 */           minBounds.width = list.getWidth();
/*      */         }
/*      */       }
/* 1204 */       else if (minBounds.x != maxBounds.x)
/*      */       {
/* 1206 */         minBounds.y = 0;
/* 1207 */         minBounds.height = list.getHeight();
/*      */       }
/* 1209 */       minBounds.add(maxBounds);
/*      */     }
/* 1211 */     return minBounds;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   private Rectangle getCellBounds(JList list, int index)
/*      */   {
/* 1219 */     maybeUpdateLayoutState();
/*      */     
/* 1221 */     int row = convertModelToRow(index);
/* 1222 */     int column = convertModelToColumn(index);
/*      */     
/* 1224 */     if ((row == -1) || (column == -1)) {
/* 1225 */       return null;
/*      */     }
/*      */     
/* 1228 */     Insets insets = list.getInsets();
/*      */     
/* 1230 */     int w = this.cellWidth;
/* 1231 */     int y = insets.top;
/*      */     int x;
/* 1233 */     int h; switch (this.layoutOrientation) {
/*      */     case 1: case 2: 
/*      */       int x;
/* 1236 */       if (this.isLeftToRight) {
/* 1237 */         x = insets.left + column * this.cellWidth;
/*      */       } else {
/* 1239 */         x = list.getWidth() - insets.right - (column + 1) * this.cellWidth;
/*      */       }
/* 1241 */       y += this.cellHeight * row;
/* 1242 */       h = this.cellHeight;
/* 1243 */       break;
/*      */     default: 
/* 1245 */       x = insets.left;
/* 1246 */       if (this.cellHeights == null) {
/* 1247 */         y += this.cellHeight * row;
/*      */       }
/* 1249 */       else if (row >= this.cellHeights.length) {
/* 1250 */         y = 0;
/*      */       }
/*      */       else {
/* 1253 */         for (int i = 0; i < row; i++) {
/* 1254 */           y += this.cellHeights[i];
/*      */         }
/*      */       }
/* 1257 */       w = list.getWidth() - (insets.left + insets.right);
/* 1258 */       h = getRowHeight(index);
/*      */     }
/*      */     
/* 1261 */     return new Rectangle(x, y, w, h);
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
/*      */   protected int getRowHeight(int row)
/*      */   {
/* 1274 */     return getHeight(0, row);
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
/*      */   protected int convertYToRow(int y0)
/*      */   {
/* 1289 */     return convertLocationToRow(0, y0, false);
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
/*      */   protected int convertRowToY(int row)
/*      */   {
/* 1303 */     if ((row >= getRowCount(0)) || (row < 0)) {
/* 1304 */       return -1;
/*      */     }
/* 1306 */     Rectangle bounds = getCellBounds(this.list, row, row);
/* 1307 */     return bounds.y;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   private int getHeight(int column, int row)
/*      */   {
/* 1314 */     if ((column < 0) || (column > this.columnCount) || (row < 0)) {
/* 1315 */       return -1;
/*      */     }
/* 1317 */     if (this.layoutOrientation != 0) {
/* 1318 */       return this.cellHeight;
/*      */     }
/* 1320 */     if (row >= getElementCount()) {
/* 1321 */       return -1;
/*      */     }
/* 1323 */     return row < this.cellHeights.length ? this.cellHeights[row] : this.cellHeights == null ? this.cellHeight : -1;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   private int convertLocationToRow(int x, int y0, boolean closest)
/*      */   {
/* 1334 */     int size = getElementCount();
/*      */     
/* 1336 */     if (size <= 0) {
/* 1337 */       return -1;
/*      */     }
/* 1339 */     Insets insets = this.list.getInsets();
/* 1340 */     if (this.cellHeights == null) {
/* 1341 */       int row = this.cellHeight == 0 ? 0 : (y0 - insets.top) / this.cellHeight;
/*      */       
/* 1343 */       if (closest) {
/* 1344 */         if (row < 0) {
/* 1345 */           row = 0;
/*      */         }
/* 1347 */         else if (row >= size) {
/* 1348 */           row = size - 1;
/*      */         }
/*      */       }
/* 1351 */       return row;
/*      */     }
/* 1353 */     if (size > this.cellHeights.length) {
/* 1354 */       return -1;
/*      */     }
/*      */     
/* 1357 */     int y = insets.top;
/* 1358 */     int row = 0;
/*      */     
/* 1360 */     if ((closest) && (y0 < y)) {
/* 1361 */       return 0;
/*      */     }
/*      */     
/* 1364 */     for (int i = 0; i < size; i++) {
/* 1365 */       if ((y0 >= y) && (y0 < y + this.cellHeights[i])) {
/* 1366 */         return row;
/*      */       }
/* 1368 */       y += this.cellHeights[i];
/* 1369 */       row++;
/*      */     }
/* 1371 */     return i - 1;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   private int convertLocationToRowInColumn(int y, int column)
/*      */   {
/* 1380 */     int x = 0;
/*      */     
/* 1382 */     if (this.layoutOrientation != 0) {
/* 1383 */       if (this.isLeftToRight) {
/* 1384 */         x = column * this.cellWidth;
/*      */       } else {
/* 1386 */         x = this.list.getWidth() - (column + 1) * this.cellWidth - this.list.getInsets().right;
/*      */       }
/*      */     }
/* 1389 */     return convertLocationToRow(x, y, true);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   private int convertLocationToModel(int x, int y)
/*      */   {
/* 1397 */     int row = convertLocationToRow(x, y, true);
/* 1398 */     int column = convertLocationToColumn(x, y);
/*      */     
/* 1400 */     if ((row >= 0) && (column >= 0)) {
/* 1401 */       return getModelIndex(column, row);
/*      */     }
/* 1403 */     return -1;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   private int getRowCount(int column)
/*      */   {
/* 1410 */     if ((column < 0) || (column >= this.columnCount)) {
/* 1411 */       return -1;
/*      */     }
/* 1413 */     if ((this.layoutOrientation == 0) || ((column == 0) && (this.columnCount == 1)))
/*      */     {
/* 1415 */       return getElementCount();
/*      */     }
/* 1417 */     if (column >= this.columnCount) {
/* 1418 */       return -1;
/*      */     }
/* 1420 */     if (this.layoutOrientation == 1) {
/* 1421 */       if (column < this.columnCount - 1) {
/* 1422 */         return this.rowsPerColumn;
/*      */       }
/* 1424 */       return getElementCount() - (this.columnCount - 1) * this.rowsPerColumn;
/*      */     }
/*      */     
/*      */ 
/* 1428 */     int diff = this.columnCount - (this.columnCount * this.rowsPerColumn - getElementCount());
/*      */     
/*      */ 
/* 1431 */     if (column >= diff) {
/* 1432 */       return Math.max(0, this.rowsPerColumn - 1);
/*      */     }
/* 1434 */     return this.rowsPerColumn;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   private int getModelIndex(int column, int row)
/*      */   {
/* 1443 */     switch (this.layoutOrientation) {
/*      */     case 1: 
/* 1445 */       return Math.min(getElementCount() - 1, this.rowsPerColumn * column + Math.min(row, this.rowsPerColumn - 1));
/*      */     
/*      */     case 2: 
/* 1448 */       return Math.min(getElementCount() - 1, row * this.columnCount + column);
/*      */     }
/*      */     
/* 1451 */     return row;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   private int convertLocationToColumn(int x, int y)
/*      */   {
/* 1459 */     if (this.cellWidth > 0) {
/* 1460 */       if (this.layoutOrientation == 0) {
/* 1461 */         return 0;
/*      */       }
/* 1463 */       Insets insets = this.list.getInsets();
/*      */       int col;
/* 1465 */       int col; if (this.isLeftToRight) {
/* 1466 */         col = (x - insets.left) / this.cellWidth;
/*      */       } else {
/* 1468 */         col = (this.list.getWidth() - x - insets.right - 1) / this.cellWidth;
/*      */       }
/* 1470 */       if (col < 0) {
/* 1471 */         return 0;
/*      */       }
/* 1473 */       if (col >= this.columnCount) {
/* 1474 */         return this.columnCount - 1;
/*      */       }
/* 1476 */       return col;
/*      */     }
/* 1478 */     return 0;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   private int convertModelToRow(int index)
/*      */   {
/* 1486 */     int size = getElementCount();
/*      */     
/* 1488 */     if ((index < 0) || (index >= size)) {
/* 1489 */       return -1;
/*      */     }
/*      */     
/* 1492 */     if ((this.layoutOrientation != 0) && (this.columnCount > 1) && (this.rowsPerColumn > 0))
/*      */     {
/* 1494 */       if (this.layoutOrientation == 1) {
/* 1495 */         return index % this.rowsPerColumn;
/*      */       }
/* 1497 */       return index / this.columnCount;
/*      */     }
/* 1499 */     return index;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   private int convertModelToColumn(int index)
/*      */   {
/* 1507 */     int size = getElementCount();
/*      */     
/* 1509 */     if ((index < 0) || (index >= size)) {
/* 1510 */       return -1;
/*      */     }
/*      */     
/* 1513 */     if ((this.layoutOrientation != 0) && (this.rowsPerColumn > 0) && (this.columnCount > 1))
/*      */     {
/* 1515 */       if (this.layoutOrientation == 1) {
/* 1516 */         return index / this.rowsPerColumn;
/*      */       }
/* 1518 */       return index % this.columnCount;
/*      */     }
/* 1520 */     return 0;
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
/*      */   protected void maybeUpdateLayoutState()
/*      */   {
/* 1533 */     if (this.updateLayoutStateNeeded != 0) {
/* 1534 */       updateLayoutState();
/* 1535 */       this.updateLayoutStateNeeded = 0;
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
/*      */   protected void updateLayoutState()
/*      */   {
/* 1554 */     int fixedCellHeight = this.list.getFixedCellHeight();
/* 1555 */     int fixedCellWidth = this.list.getFixedCellWidth();
/*      */     
/* 1557 */     this.cellWidth = (fixedCellWidth != -1 ? fixedCellWidth : -1);
/*      */     
/* 1559 */     if (fixedCellHeight != -1) {
/* 1560 */       this.cellHeight = fixedCellHeight;
/* 1561 */       this.cellHeights = null;
/*      */     }
/*      */     else {
/* 1564 */       this.cellHeight = -1;
/* 1565 */       this.cellHeights = new int[getElementCount()];
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 1575 */     if ((fixedCellWidth == -1) || (fixedCellHeight == -1))
/*      */     {
/* 1577 */       ListModel dataModel = getViewModel();
/* 1578 */       int dataModelSize = dataModel.getSize();
/* 1579 */       ListCellRenderer renderer = this.list.getCellRenderer();
/*      */       
/* 1581 */       if (renderer != null) {
/* 1582 */         for (int index = 0; index < dataModelSize; index++) {
/* 1583 */           Object value = dataModel.getElementAt(index);
/* 1584 */           Component c = renderer.getListCellRendererComponent(this.list, value, index, false, false);
/* 1585 */           this.rendererPane.add(c);
/* 1586 */           Dimension cellSize = c.getPreferredSize();
/* 1587 */           if (fixedCellWidth == -1) {
/* 1588 */             this.cellWidth = Math.max(cellSize.width, this.cellWidth);
/*      */           }
/* 1590 */           if (fixedCellHeight == -1) {
/* 1591 */             this.cellHeights[index] = cellSize.height;
/*      */           }
/*      */         }
/*      */       }
/*      */       else {
/* 1596 */         if (this.cellWidth == -1) {
/* 1597 */           this.cellWidth = 0;
/*      */         }
/* 1599 */         if (this.cellHeights == null) {
/* 1600 */           this.cellHeights = new int[dataModelSize];
/*      */         }
/* 1602 */         for (int index = 0; index < dataModelSize; index++) {
/* 1603 */           this.cellHeights[index] = 0;
/*      */         }
/*      */       }
/*      */     }
/*      */     
/* 1608 */     this.columnCount = 1;
/* 1609 */     if (this.layoutOrientation != 0) {
/* 1610 */       updateHorizontalLayoutState(fixedCellWidth, fixedCellHeight);
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
/*      */   private void updateHorizontalLayoutState(int fixedCellWidth, int fixedCellHeight)
/*      */   {
/* 1624 */     int visRows = this.list.getVisibleRowCount();
/* 1625 */     int dataModelSize = getElementCount();
/* 1626 */     Insets insets = this.list.getInsets();
/*      */     
/* 1628 */     this.listHeight = this.list.getHeight();
/* 1629 */     this.listWidth = this.list.getWidth();
/*      */     
/* 1631 */     if (dataModelSize == 0) {
/* 1632 */       this.rowsPerColumn = (this.columnCount = 0);
/* 1633 */       this.preferredHeight = (insets.top + insets.bottom); return;
/*      */     }
/*      */     
/*      */     int height;
/*      */     
/*      */     int height;
/* 1639 */     if (fixedCellHeight != -1) {
/* 1640 */       height = fixedCellHeight;
/*      */     }
/*      */     else
/*      */     {
/* 1644 */       int maxHeight = 0;
/* 1645 */       if (this.cellHeights.length > 0) {
/* 1646 */         maxHeight = this.cellHeights[(this.cellHeights.length - 1)];
/* 1647 */         for (int counter = this.cellHeights.length - 2; 
/* 1648 */             counter >= 0; counter--) {
/* 1649 */           maxHeight = Math.max(maxHeight, this.cellHeights[counter]);
/*      */         }
/*      */       }
/* 1652 */       height = this.cellHeight = maxHeight;
/* 1653 */       this.cellHeights = null;
/*      */     }
/*      */     
/*      */ 
/* 1657 */     this.rowsPerColumn = dataModelSize;
/* 1658 */     if (visRows > 0) {
/* 1659 */       this.rowsPerColumn = visRows;
/* 1660 */       this.columnCount = Math.max(1, dataModelSize / this.rowsPerColumn);
/* 1661 */       if ((dataModelSize > 0) && (dataModelSize > this.rowsPerColumn) && (dataModelSize % this.rowsPerColumn != 0))
/*      */       {
/* 1663 */         this.columnCount += 1;
/*      */       }
/* 1665 */       if (this.layoutOrientation == 2)
/*      */       {
/*      */ 
/* 1668 */         this.rowsPerColumn = (dataModelSize / this.columnCount);
/* 1669 */         if (dataModelSize % this.columnCount > 0) {
/* 1670 */           this.rowsPerColumn += 1;
/*      */         }
/*      */       }
/*      */     }
/* 1674 */     else if ((this.layoutOrientation == 1) && (height != 0)) {
/* 1675 */       this.rowsPerColumn = Math.max(1, (this.listHeight - insets.top - insets.bottom) / height);
/*      */       
/* 1677 */       this.columnCount = Math.max(1, dataModelSize / this.rowsPerColumn);
/* 1678 */       if ((dataModelSize > 0) && (dataModelSize > this.rowsPerColumn) && (dataModelSize % this.rowsPerColumn != 0))
/*      */       {
/* 1680 */         this.columnCount += 1;
/*      */       }
/*      */     }
/* 1683 */     else if ((this.layoutOrientation == 2) && (this.cellWidth > 0) && (this.listWidth > 0))
/*      */     {
/* 1685 */       this.columnCount = Math.max(1, (this.listWidth - insets.left - insets.right) / this.cellWidth);
/*      */       
/* 1687 */       this.rowsPerColumn = (dataModelSize / this.columnCount);
/* 1688 */       if (dataModelSize % this.columnCount > 0) {
/* 1689 */         this.rowsPerColumn += 1;
/*      */       }
/*      */     }
/* 1692 */     this.preferredHeight = (this.rowsPerColumn * this.cellHeight + insets.top + insets.bottom);
/*      */   }
/*      */   
/*      */   private Handler getHandler()
/*      */   {
/* 1697 */     if (this.handler == null) {
/* 1698 */       this.handler = new Handler(null);
/*      */     }
/* 1700 */     return this.handler;
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
/*      */   public class MouseInputHandler
/*      */     implements MouseInputListener
/*      */   {
/*      */     public MouseInputHandler() {}
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     public void mouseClicked(MouseEvent e)
/*      */     {
/* 1725 */       BasicXListUI.this.getHandler().mouseClicked(e);
/*      */     }
/*      */     
/*      */     public void mouseEntered(MouseEvent e) {
/* 1729 */       BasicXListUI.this.getHandler().mouseEntered(e);
/*      */     }
/*      */     
/*      */     public void mouseExited(MouseEvent e) {
/* 1733 */       BasicXListUI.this.getHandler().mouseExited(e);
/*      */     }
/*      */     
/*      */     public void mousePressed(MouseEvent e) {
/* 1737 */       BasicXListUI.this.getHandler().mousePressed(e);
/*      */     }
/*      */     
/*      */     public void mouseDragged(MouseEvent e) {
/* 1741 */       BasicXListUI.this.getHandler().mouseDragged(e);
/*      */     }
/*      */     
/*      */     public void mouseMoved(MouseEvent e) {
/* 1745 */       BasicXListUI.this.getHandler().mouseMoved(e);
/*      */     }
/*      */     
/*      */     public void mouseReleased(MouseEvent e) {
/* 1749 */       BasicXListUI.this.getHandler().mouseReleased(e);
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
/*      */   protected MouseInputListener createMouseInputListener()
/*      */   {
/* 1777 */     return getHandler();
/*      */   }
/*      */   
/*      */ 
/*      */   public class FocusHandler
/*      */     implements FocusListener
/*      */   {
/*      */     public FocusHandler() {}
/*      */     
/*      */ 
/*      */     protected void repaintCellFocus()
/*      */     {
/* 1789 */       BasicXListUI.this.getHandler().repaintCellFocus();
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */     public void focusGained(FocusEvent e)
/*      */     {
/* 1797 */       BasicXListUI.this.getHandler().focusGained(e);
/*      */     }
/*      */     
/*      */     public void focusLost(FocusEvent e) {
/* 1801 */       BasicXListUI.this.getHandler().focusLost(e);
/*      */     }
/*      */   }
/*      */   
/*      */   protected FocusListener createFocusListener() {
/* 1806 */     return getHandler();
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
/*      */   public class ListSelectionHandler
/*      */     implements ListSelectionListener
/*      */   {
/*      */     public ListSelectionHandler() {}
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     public void valueChanged(ListSelectionEvent e)
/*      */     {
/* 1831 */       if (BasicXListUI.this.processedBySortUI(e)) return;
/* 1832 */       BasicXListUI.this.getHandler().valueChanged(e);
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
/*      */   protected ListSelectionListener createListSelectionListener()
/*      */   {
/* 1859 */     return new ListSelectionHandler();
/*      */   }
/*      */   
/*      */   private void redrawList()
/*      */   {
/* 1864 */     this.list.revalidate();
/* 1865 */     this.list.repaint();
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
/*      */   public class ListDataHandler
/*      */     implements ListDataListener
/*      */   {
/*      */     public ListDataHandler() {}
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     public void intervalAdded(ListDataEvent e)
/*      */     {
/* 1890 */       if (BasicXListUI.this.processedBySortUI(e))
/* 1891 */         return;
/* 1892 */       BasicXListUI.this.getHandler().intervalAdded(e);
/*      */     }
/*      */     
/*      */     public void intervalRemoved(ListDataEvent e)
/*      */     {
/* 1897 */       if (BasicXListUI.this.processedBySortUI(e))
/* 1898 */         return;
/* 1899 */       BasicXListUI.this.getHandler().intervalRemoved(e);
/*      */     }
/*      */     
/*      */     public void contentsChanged(ListDataEvent e)
/*      */     {
/* 1904 */       if (BasicXListUI.this.processedBySortUI(e))
/* 1905 */         return;
/* 1906 */       BasicXListUI.this.getHandler().contentsChanged(e);
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
/*      */   protected ListDataListener createListDataListener()
/*      */   {
/* 1934 */     return new ListDataHandler();
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
/*      */   public class PropertyChangeHandler
/*      */     implements PropertyChangeListener
/*      */   {
/*      */     public PropertyChangeHandler() {}
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     public void propertyChange(PropertyChangeEvent e)
/*      */     {
/* 1961 */       BasicXListUI.this.getHandler().propertyChange(e);
/* 1962 */       BasicXListUI.this.updateSortUI(e.getPropertyName());
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
/*      */   protected PropertyChangeListener createPropertyChangeListener()
/*      */   {
/* 1990 */     return new PropertyChangeHandler();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   private static class Actions
/*      */     extends UIAction
/*      */   {
/*      */     private static final String SELECT_PREVIOUS_COLUMN = "selectPreviousColumn";
/*      */     
/*      */ 
/*      */ 
/*      */     private static final String SELECT_PREVIOUS_COLUMN_EXTEND = "selectPreviousColumnExtendSelection";
/*      */     
/*      */     private static final String SELECT_PREVIOUS_COLUMN_CHANGE_LEAD = "selectPreviousColumnChangeLead";
/*      */     
/*      */     private static final String SELECT_NEXT_COLUMN = "selectNextColumn";
/*      */     
/*      */     private static final String SELECT_NEXT_COLUMN_EXTEND = "selectNextColumnExtendSelection";
/*      */     
/*      */     private static final String SELECT_NEXT_COLUMN_CHANGE_LEAD = "selectNextColumnChangeLead";
/*      */     
/*      */     private static final String SELECT_PREVIOUS_ROW = "selectPreviousRow";
/*      */     
/*      */     private static final String SELECT_PREVIOUS_ROW_EXTEND = "selectPreviousRowExtendSelection";
/*      */     
/*      */     private static final String SELECT_PREVIOUS_ROW_CHANGE_LEAD = "selectPreviousRowChangeLead";
/*      */     
/*      */     private static final String SELECT_NEXT_ROW = "selectNextRow";
/*      */     
/*      */     private static final String SELECT_NEXT_ROW_EXTEND = "selectNextRowExtendSelection";
/*      */     
/*      */     private static final String SELECT_NEXT_ROW_CHANGE_LEAD = "selectNextRowChangeLead";
/*      */     
/*      */     private static final String SELECT_FIRST_ROW = "selectFirstRow";
/*      */     
/*      */     private static final String SELECT_FIRST_ROW_EXTEND = "selectFirstRowExtendSelection";
/*      */     
/*      */     private static final String SELECT_FIRST_ROW_CHANGE_LEAD = "selectFirstRowChangeLead";
/*      */     
/*      */     private static final String SELECT_LAST_ROW = "selectLastRow";
/*      */     
/*      */     private static final String SELECT_LAST_ROW_EXTEND = "selectLastRowExtendSelection";
/*      */     
/*      */     private static final String SELECT_LAST_ROW_CHANGE_LEAD = "selectLastRowChangeLead";
/*      */     
/*      */     private static final String SCROLL_UP = "scrollUp";
/*      */     
/*      */     private static final String SCROLL_UP_EXTEND = "scrollUpExtendSelection";
/*      */     
/*      */     private static final String SCROLL_UP_CHANGE_LEAD = "scrollUpChangeLead";
/*      */     
/*      */     private static final String SCROLL_DOWN = "scrollDown";
/*      */     
/*      */     private static final String SCROLL_DOWN_EXTEND = "scrollDownExtendSelection";
/*      */     
/*      */     private static final String SCROLL_DOWN_CHANGE_LEAD = "scrollDownChangeLead";
/*      */     
/*      */     private static final String SELECT_ALL = "selectAll";
/*      */     
/*      */     private static final String CLEAR_SELECTION = "clearSelection";
/*      */     
/*      */     private static final String ADD_TO_SELECTION = "addToSelection";
/*      */     
/*      */     private static final String TOGGLE_AND_ANCHOR = "toggleAndAnchor";
/*      */     
/*      */     private static final String EXTEND_TO = "extendTo";
/*      */     
/*      */     private static final String MOVE_SELECTION_TO = "moveSelectionTo";
/*      */     
/*      */ 
/* 2062 */     Actions(String name) { super(); }
/*      */     
/*      */     public void actionPerformed(ActionEvent e) {
/* 2065 */       String name = getName();
/* 2066 */       JList list = (JList)e.getSource();
/* 2067 */       BasicXListUI ui = (BasicXListUI)LookAndFeelUtils.getUIOfType(list.getUI(), BasicXListUI.class);
/*      */       
/*      */ 
/* 2070 */       if (name == "selectPreviousColumn") {
/* 2071 */         changeSelection(list, 1, getNextColumnIndex(list, ui, -1), -1);
/*      */ 
/*      */       }
/* 2074 */       else if (name == "selectPreviousColumnExtendSelection") {
/* 2075 */         changeSelection(list, 2, getNextColumnIndex(list, ui, -1), -1);
/*      */ 
/*      */       }
/* 2078 */       else if (name == "selectPreviousColumnChangeLead") {
/* 2079 */         changeSelection(list, 0, getNextColumnIndex(list, ui, -1), -1);
/*      */ 
/*      */       }
/* 2082 */       else if (name == "selectNextColumn") {
/* 2083 */         changeSelection(list, 1, getNextColumnIndex(list, ui, 1), 1);
/*      */ 
/*      */       }
/* 2086 */       else if (name == "selectNextColumnExtendSelection") {
/* 2087 */         changeSelection(list, 2, getNextColumnIndex(list, ui, 1), 1);
/*      */ 
/*      */       }
/* 2090 */       else if (name == "selectNextColumnChangeLead") {
/* 2091 */         changeSelection(list, 0, getNextColumnIndex(list, ui, 1), 1);
/*      */ 
/*      */       }
/* 2094 */       else if (name == "selectPreviousRow") {
/* 2095 */         changeSelection(list, 1, getNextIndex(list, ui, -1), -1);
/*      */ 
/*      */       }
/* 2098 */       else if (name == "selectPreviousRowExtendSelection") {
/* 2099 */         changeSelection(list, 2, getNextIndex(list, ui, -1), -1);
/*      */ 
/*      */       }
/* 2102 */       else if (name == "selectPreviousRowChangeLead") {
/* 2103 */         changeSelection(list, 0, getNextIndex(list, ui, -1), -1);
/*      */ 
/*      */       }
/* 2106 */       else if (name == "selectNextRow") {
/* 2107 */         changeSelection(list, 1, getNextIndex(list, ui, 1), 1);
/*      */ 
/*      */       }
/* 2110 */       else if (name == "selectNextRowExtendSelection") {
/* 2111 */         changeSelection(list, 2, getNextIndex(list, ui, 1), 1);
/*      */ 
/*      */       }
/* 2114 */       else if (name == "selectNextRowChangeLead") {
/* 2115 */         changeSelection(list, 0, getNextIndex(list, ui, 1), 1);
/*      */ 
/*      */       }
/* 2118 */       else if (name == "selectFirstRow") {
/* 2119 */         changeSelection(list, 1, 0, -1);
/*      */       }
/* 2121 */       else if (name == "selectFirstRowExtendSelection") {
/* 2122 */         changeSelection(list, 2, 0, -1);
/*      */       }
/* 2124 */       else if (name == "selectFirstRowChangeLead") {
/* 2125 */         changeSelection(list, 0, 0, -1);
/*      */       }
/* 2127 */       else if (name == "selectLastRow") {
/* 2128 */         changeSelection(list, 1, getElementCount(list) - 1, 1);
/*      */ 
/*      */       }
/* 2131 */       else if (name == "selectLastRowExtendSelection") {
/* 2132 */         changeSelection(list, 2, getElementCount(list) - 1, 1);
/*      */ 
/*      */       }
/* 2135 */       else if (name == "selectLastRowChangeLead") {
/* 2136 */         changeSelection(list, 0, getElementCount(list) - 1, 1);
/*      */ 
/*      */       }
/* 2139 */       else if (name == "scrollUp") {
/* 2140 */         changeSelection(list, 1, getNextPageIndex(list, -1), -1);
/*      */ 
/*      */       }
/* 2143 */       else if (name == "scrollUpExtendSelection") {
/* 2144 */         changeSelection(list, 2, getNextPageIndex(list, -1), -1);
/*      */ 
/*      */       }
/* 2147 */       else if (name == "scrollUpChangeLead") {
/* 2148 */         changeSelection(list, 0, getNextPageIndex(list, -1), -1);
/*      */ 
/*      */       }
/* 2151 */       else if (name == "scrollDown") {
/* 2152 */         changeSelection(list, 1, getNextPageIndex(list, 1), 1);
/*      */ 
/*      */       }
/* 2155 */       else if (name == "scrollDownExtendSelection") {
/* 2156 */         changeSelection(list, 2, getNextPageIndex(list, 1), 1);
/*      */ 
/*      */       }
/* 2159 */       else if (name == "scrollDownChangeLead") {
/* 2160 */         changeSelection(list, 0, getNextPageIndex(list, 1), 1);
/*      */ 
/*      */       }
/* 2163 */       else if (name == "selectAll") {
/* 2164 */         selectAll(list);
/*      */       }
/* 2166 */       else if (name == "clearSelection") {
/* 2167 */         clearSelection(list);
/*      */       }
/* 2169 */       else if (name == "addToSelection") {
/* 2170 */         int index = BasicXListUI.adjustIndex(list.getSelectionModel().getLeadSelectionIndex(), list);
/*      */         
/*      */ 
/* 2173 */         if (!list.isSelectedIndex(index)) {
/* 2174 */           int oldAnchor = list.getSelectionModel().getAnchorSelectionIndex();
/* 2175 */           list.setValueIsAdjusting(true);
/* 2176 */           list.addSelectionInterval(index, index);
/* 2177 */           list.getSelectionModel().setAnchorSelectionIndex(oldAnchor);
/* 2178 */           list.setValueIsAdjusting(false);
/*      */         }
/*      */       }
/* 2181 */       else if (name == "toggleAndAnchor") {
/* 2182 */         int index = BasicXListUI.adjustIndex(list.getSelectionModel().getLeadSelectionIndex(), list);
/*      */         
/*      */ 
/* 2185 */         if (list.isSelectedIndex(index)) {
/* 2186 */           list.removeSelectionInterval(index, index);
/*      */         } else {
/* 2188 */           list.addSelectionInterval(index, index);
/*      */         }
/*      */       }
/* 2191 */       else if (name == "extendTo") {
/* 2192 */         changeSelection(list, 2, BasicXListUI.adjustIndex(list.getSelectionModel().getLeadSelectionIndex(), list), 0);
/*      */ 
/*      */ 
/*      */ 
/*      */       }
/* 2197 */       else if (name == "moveSelectionTo") {
/* 2198 */         changeSelection(list, 1, BasicXListUI.adjustIndex(list.getSelectionModel().getLeadSelectionIndex(), list), 0);
/*      */       }
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     private int getElementCount(JList list)
/*      */     {
/* 2209 */       return ((JXList)list).getElementCount();
/*      */     }
/*      */     
/*      */     public boolean isEnabled(Object c) {
/* 2213 */       Object name = getName();
/* 2214 */       if ((name == "selectPreviousColumnChangeLead") || (name == "selectNextColumnChangeLead") || (name == "selectPreviousRowChangeLead") || (name == "selectNextRowChangeLead") || (name == "selectFirstRowChangeLead") || (name == "selectLastRowChangeLead") || (name == "scrollUpChangeLead") || (name == "scrollDownChangeLead"))
/*      */       {
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 2225 */         return (c != null) && ((((JList)c).getSelectionModel() instanceof DefaultListSelectionModel));
/*      */       }
/*      */       
/*      */ 
/* 2229 */       return true;
/*      */     }
/*      */     
/*      */     private void clearSelection(JList list) {
/* 2233 */       list.clearSelection();
/*      */     }
/*      */     
/*      */     private void selectAll(JList list) {
/* 2237 */       int size = getElementCount(list);
/* 2238 */       if (size > 0) {
/* 2239 */         ListSelectionModel lsm = list.getSelectionModel();
/* 2240 */         int lead = BasicXListUI.adjustIndex(lsm.getLeadSelectionIndex(), list);
/*      */         
/* 2242 */         if (lsm.getSelectionMode() == 0) {
/* 2243 */           if (lead == -1) {
/* 2244 */             int min = BasicXListUI.adjustIndex(list.getMinSelectionIndex(), list);
/* 2245 */             lead = min == -1 ? 0 : min;
/*      */           }
/*      */           
/* 2248 */           list.setSelectionInterval(lead, lead);
/* 2249 */           list.ensureIndexIsVisible(lead);
/*      */         } else {
/* 2251 */           list.setValueIsAdjusting(true);
/*      */           
/* 2253 */           int anchor = BasicXListUI.adjustIndex(lsm.getAnchorSelectionIndex(), list);
/*      */           
/* 2255 */           list.setSelectionInterval(0, size - 1);
/*      */           
/*      */ 
/* 2258 */           SwingXUtilities.setLeadAnchorWithoutSelection(lsm, anchor, lead);
/*      */           
/* 2260 */           list.setValueIsAdjusting(false);
/*      */         }
/*      */       }
/*      */     }
/*      */     
/*      */     private int getNextPageIndex(JList list, int direction) {
/* 2266 */       if (getElementCount(list) == 0) {
/* 2267 */         return -1;
/*      */       }
/*      */       
/* 2270 */       int index = -1;
/* 2271 */       Rectangle visRect = list.getVisibleRect();
/* 2272 */       ListSelectionModel lsm = list.getSelectionModel();
/* 2273 */       int lead = BasicXListUI.adjustIndex(lsm.getLeadSelectionIndex(), list);
/* 2274 */       Rectangle leadRect = lead == -1 ? new Rectangle() : list.getCellBounds(lead, lead);
/*      */       
/*      */ 
/* 2277 */       if ((list.getLayoutOrientation() == 1) && (list.getVisibleRowCount() <= 0))
/*      */       {
/* 2279 */         if (!list.getComponentOrientation().isLeftToRight()) {
/* 2280 */           direction = -direction;
/*      */         }
/*      */         
/*      */ 
/* 2284 */         if (direction < 0)
/*      */         {
/* 2286 */           visRect.x = (leadRect.x + leadRect.width - visRect.width);
/* 2287 */           Point p = new Point(visRect.x - 1, leadRect.y);
/* 2288 */           index = list.locationToIndex(p);
/* 2289 */           Rectangle cellBounds = list.getCellBounds(index, index);
/* 2290 */           if (visRect.intersects(cellBounds)) {
/* 2291 */             p.x = (cellBounds.x - 1);
/* 2292 */             index = list.locationToIndex(p);
/* 2293 */             cellBounds = list.getCellBounds(index, index);
/*      */           }
/*      */           
/* 2296 */           if (cellBounds.y != leadRect.y) {
/* 2297 */             p.x = (cellBounds.x + cellBounds.width);
/* 2298 */             index = list.locationToIndex(p);
/*      */           }
/*      */         }
/*      */         else
/*      */         {
/* 2303 */           visRect.x = leadRect.x;
/* 2304 */           Point p = new Point(visRect.x + visRect.width, leadRect.y);
/* 2305 */           index = list.locationToIndex(p);
/* 2306 */           Rectangle cellBounds = list.getCellBounds(index, index);
/* 2307 */           if (visRect.intersects(cellBounds)) {
/* 2308 */             p.x = (cellBounds.x + cellBounds.width);
/* 2309 */             index = list.locationToIndex(p);
/* 2310 */             cellBounds = list.getCellBounds(index, index);
/*      */           }
/* 2312 */           if (cellBounds.y != leadRect.y) {
/* 2313 */             p.x = (cellBounds.x - 1);
/* 2314 */             index = list.locationToIndex(p);
/*      */           }
/*      */           
/*      */         }
/*      */       }
/* 2319 */       else if (direction < 0)
/*      */       {
/*      */ 
/* 2322 */         Point p = new Point(leadRect.x, visRect.y);
/* 2323 */         index = list.locationToIndex(p);
/* 2324 */         if (lead <= index)
/*      */         {
/*      */ 
/* 2327 */           visRect.y = (leadRect.y + leadRect.height - visRect.height);
/* 2328 */           p.y = visRect.y;
/* 2329 */           index = list.locationToIndex(p);
/* 2330 */           Rectangle cellBounds = list.getCellBounds(index, index);
/*      */           
/*      */ 
/* 2333 */           if (cellBounds.y < visRect.y) {
/* 2334 */             p.y = (cellBounds.y + cellBounds.height);
/* 2335 */             index = list.locationToIndex(p);
/* 2336 */             cellBounds = list.getCellBounds(index, index);
/*      */           }
/*      */           
/*      */ 
/* 2340 */           if (cellBounds.y >= leadRect.y) {
/* 2341 */             p.y = (leadRect.y - 1);
/* 2342 */             index = list.locationToIndex(p);
/*      */           }
/*      */           
/*      */         }
/*      */       }
/*      */       else
/*      */       {
/* 2349 */         Point p = new Point(leadRect.x, visRect.y + visRect.height - 1);
/*      */         
/* 2351 */         index = list.locationToIndex(p);
/* 2352 */         Rectangle cellBounds = list.getCellBounds(index, index);
/*      */         
/*      */ 
/* 2355 */         if (cellBounds.y + cellBounds.height > visRect.y + visRect.height)
/*      */         {
/* 2357 */           p.y = (cellBounds.y - 1);
/* 2358 */           index = list.locationToIndex(p);
/* 2359 */           cellBounds = list.getCellBounds(index, index);
/* 2360 */           index = Math.max(index, lead);
/*      */         }
/*      */         
/* 2363 */         if (lead >= index)
/*      */         {
/*      */ 
/* 2366 */           visRect.y = leadRect.y;
/* 2367 */           p.y = (visRect.y + visRect.height - 1);
/* 2368 */           index = list.locationToIndex(p);
/* 2369 */           cellBounds = list.getCellBounds(index, index);
/*      */           
/*      */ 
/* 2372 */           if (cellBounds.y + cellBounds.height > visRect.y + visRect.height)
/*      */           {
/* 2374 */             p.y = (cellBounds.y - 1);
/* 2375 */             index = list.locationToIndex(p);
/* 2376 */             cellBounds = list.getCellBounds(index, index);
/*      */           }
/*      */           
/*      */ 
/* 2380 */           if (cellBounds.y <= leadRect.y) {
/* 2381 */             p.y = (leadRect.y + leadRect.height);
/* 2382 */             index = list.locationToIndex(p);
/*      */           }
/*      */         }
/*      */       }
/*      */       
/* 2387 */       return index;
/*      */     }
/*      */     
/*      */     private void changeSelection(JList list, int type, int index, int direction)
/*      */     {
/* 2392 */       if ((index >= 0) && (index < getElementCount(list))) {
/* 2393 */         ListSelectionModel lsm = list.getSelectionModel();
/*      */         
/*      */ 
/* 2396 */         if ((type == 0) && (list.getSelectionMode() != 2))
/*      */         {
/*      */ 
/*      */ 
/* 2400 */           type = 1;
/*      */         }
/*      */         
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 2407 */         adjustScrollPositionIfNecessary(list, index, direction);
/*      */         
/* 2409 */         if (type == 2) {
/* 2410 */           int anchor = BasicXListUI.adjustIndex(lsm.getAnchorSelectionIndex(), list);
/* 2411 */           if (anchor == -1) {
/* 2412 */             anchor = 0;
/*      */           }
/*      */           
/* 2415 */           list.setSelectionInterval(anchor, index);
/*      */         }
/* 2417 */         else if (type == 1) {
/* 2418 */           list.setSelectedIndex(index);
/*      */ 
/*      */ 
/*      */ 
/*      */         }
/* 2423 */         else if ((lsm instanceof DefaultListSelectionModel)) {
/* 2424 */           ((DefaultListSelectionModel)lsm).moveLeadSelectionIndex(index);
/*      */         }
/*      */       }
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     private void adjustScrollPositionIfNecessary(JList list, int index, int direction)
/*      */     {
/* 2436 */       if (direction == 0) {
/* 2437 */         return;
/*      */       }
/* 2439 */       Rectangle cellBounds = list.getCellBounds(index, index);
/* 2440 */       Rectangle visRect = list.getVisibleRect();
/* 2441 */       if ((cellBounds != null) && (!visRect.contains(cellBounds))) {
/* 2442 */         if ((list.getLayoutOrientation() == 1) && (list.getVisibleRowCount() <= 0))
/*      */         {
/*      */ 
/* 2445 */           if (list.getComponentOrientation().isLeftToRight()) {
/* 2446 */             if (direction > 0)
/*      */             {
/* 2448 */               int x = Math.max(0, cellBounds.x + cellBounds.width - visRect.width);
/*      */               
/* 2450 */               int startIndex = list.locationToIndex(new Point(x, cellBounds.y));
/*      */               
/* 2452 */               Rectangle startRect = list.getCellBounds(startIndex, startIndex);
/*      */               
/* 2454 */               if ((startRect.x < x) && (startRect.x < cellBounds.x)) {
/* 2455 */                 startRect.x += startRect.width;
/* 2456 */                 startIndex = list.locationToIndex(startRect.getLocation());
/*      */                 
/* 2458 */                 startRect = list.getCellBounds(startIndex, startIndex);
/*      */               }
/*      */               
/* 2461 */               cellBounds = startRect;
/*      */             }
/* 2463 */             cellBounds.width = visRect.width;
/*      */ 
/*      */           }
/* 2466 */           else if (direction > 0)
/*      */           {
/* 2468 */             int x = cellBounds.x + visRect.width;
/* 2469 */             int rightIndex = list.locationToIndex(new Point(x, cellBounds.y));
/*      */             
/* 2471 */             Rectangle rightRect = list.getCellBounds(rightIndex, rightIndex);
/*      */             
/* 2473 */             if ((rightRect.x + rightRect.width > x) && (rightRect.x > cellBounds.x))
/*      */             {
/* 2475 */               rightRect.width = 0;
/*      */             }
/* 2477 */             cellBounds.x = Math.max(0, rightRect.x + rightRect.width - visRect.width);
/*      */             
/* 2479 */             cellBounds.width = visRect.width;
/*      */           }
/*      */           else {
/* 2482 */             cellBounds.x += Math.max(0, cellBounds.width - visRect.width);
/*      */             
/*      */ 
/* 2485 */             cellBounds.width = Math.min(cellBounds.width, visRect.width);
/*      */ 
/*      */           }
/*      */           
/*      */ 
/*      */ 
/*      */         }
/* 2492 */         else if ((direction > 0) && ((cellBounds.y < visRect.y) || (cellBounds.y + cellBounds.height > visRect.y + visRect.height)))
/*      */         {
/*      */ 
/*      */ 
/*      */ 
/* 2497 */           int y = Math.max(0, cellBounds.y + cellBounds.height - visRect.height);
/*      */           
/* 2499 */           int startIndex = list.locationToIndex(new Point(cellBounds.x, y));
/*      */           
/* 2501 */           Rectangle startRect = list.getCellBounds(startIndex, startIndex);
/*      */           
/* 2503 */           if ((startRect.y < y) && (startRect.y < cellBounds.y)) {
/* 2504 */             startRect.y += startRect.height;
/* 2505 */             startIndex = list.locationToIndex(startRect.getLocation());
/*      */             
/* 2507 */             startRect = list.getCellBounds(startIndex, startIndex);
/*      */           }
/*      */           
/* 2510 */           cellBounds = startRect;
/* 2511 */           cellBounds.height = visRect.height;
/*      */         }
/*      */         else
/*      */         {
/* 2515 */           cellBounds.height = Math.min(cellBounds.height, visRect.height);
/*      */         }
/*      */         
/* 2518 */         list.scrollRectToVisible(cellBounds);
/*      */       }
/*      */     }
/*      */     
/*      */     private int getNextColumnIndex(JList list, BasicXListUI ui, int amount)
/*      */     {
/* 2524 */       if (list.getLayoutOrientation() != 0) {
/* 2525 */         int index = BasicXListUI.adjustIndex(list.getLeadSelectionIndex(), list);
/* 2526 */         int size = getElementCount(list);
/*      */         
/* 2528 */         if (index == -1)
/* 2529 */           return 0;
/* 2530 */         if (size == 1)
/*      */         {
/* 2532 */           return 0; }
/* 2533 */         if ((ui == null) || (ui.columnCount <= 1)) {
/* 2534 */           return -1;
/*      */         }
/*      */         
/* 2537 */         int column = ui.convertModelToColumn(index);
/* 2538 */         int row = ui.convertModelToRow(index);
/*      */         
/* 2540 */         column += amount;
/* 2541 */         if ((column >= ui.columnCount) || (column < 0))
/*      */         {
/* 2543 */           return -1;
/*      */         }
/* 2545 */         int maxRowCount = ui.getRowCount(column);
/* 2546 */         if (row >= maxRowCount) {
/* 2547 */           return -1;
/*      */         }
/* 2549 */         return ui.getModelIndex(column, row);
/*      */       }
/*      */       
/* 2552 */       return -1;
/*      */     }
/*      */     
/*      */     private int getNextIndex(JList list, BasicXListUI ui, int amount) {
/* 2556 */       int index = BasicXListUI.adjustIndex(list.getLeadSelectionIndex(), list);
/* 2557 */       int size = getElementCount(list);
/*      */       
/* 2559 */       if (index == -1) {
/* 2560 */         if (size > 0) {
/* 2561 */           if (amount > 0) {
/* 2562 */             index = 0;
/*      */           }
/*      */           else {
/* 2565 */             index = size - 1;
/*      */           }
/*      */         }
/* 2568 */       } else if (size == 1)
/*      */       {
/* 2570 */         index = 0;
/* 2571 */       } else if (list.getLayoutOrientation() == 2) {
/* 2572 */         if (ui != null) {
/* 2573 */           index += ui.columnCount * amount;
/*      */         }
/*      */       } else {
/* 2576 */         index += amount;
/*      */       }
/*      */       
/* 2579 */       return index;
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   private class Handler
/*      */     implements FocusListener, KeyListener, ListDataListener, ListSelectionListener, MouseInputListener, PropertyChangeListener, DragRecognitionSupport.BeforeDrag
/*      */   {
/* 2591 */     private String prefix = "";
/* 2592 */     private String typedString = "";
/* 2593 */     private long lastTime = 0L;
/*      */     
/*      */ 
/*      */ 
/*      */     private boolean dragPressDidSelection;
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */     private Handler() {}
/*      */     
/*      */ 
/*      */ 
/*      */     public void keyTyped(KeyEvent e)
/*      */     {
/* 2608 */       JList src = (JList)e.getSource();
/*      */       
/* 2610 */       if ((BasicXListUI.this.getElementCount() == 0) || (e.isAltDown()) || (e.isControlDown()) || (e.isMetaDown()) || (isNavigationKey(e)))
/*      */       {
/*      */ 
/* 2613 */         return;
/*      */       }
/* 2615 */       boolean startingFromSelection = true;
/*      */       
/* 2617 */       char c = e.getKeyChar();
/*      */       
/* 2619 */       long time = e.getWhen();
/* 2620 */       int startIndex = BasicXListUI.adjustIndex(src.getLeadSelectionIndex(), BasicXListUI.this.list);
/* 2621 */       if (time - this.lastTime < BasicXListUI.this.timeFactor) {
/* 2622 */         this.typedString += c;
/* 2623 */         if ((this.prefix.length() == 1) && (c == this.prefix.charAt(0)))
/*      */         {
/*      */ 
/* 2626 */           startIndex++;
/*      */         } else {
/* 2628 */           this.prefix = this.typedString;
/*      */         }
/*      */       } else {
/* 2631 */         startIndex++;
/* 2632 */         this.typedString = ("" + c);
/* 2633 */         this.prefix = this.typedString;
/*      */       }
/* 2635 */       this.lastTime = time;
/*      */       
/* 2637 */       if ((startIndex < 0) || (startIndex >= BasicXListUI.this.getElementCount())) {
/* 2638 */         startingFromSelection = false;
/* 2639 */         startIndex = 0;
/*      */       }
/* 2641 */       int index = src.getNextMatch(this.prefix, startIndex, Position.Bias.Forward);
/*      */       
/* 2643 */       if (index >= 0) {
/* 2644 */         src.setSelectedIndex(index);
/* 2645 */         src.ensureIndexIsVisible(index);
/* 2646 */       } else if (startingFromSelection) {
/* 2647 */         index = src.getNextMatch(this.prefix, 0, Position.Bias.Forward);
/*      */         
/* 2649 */         if (index >= 0) {
/* 2650 */           src.setSelectedIndex(index);
/* 2651 */           src.ensureIndexIsVisible(index);
/*      */         }
/*      */       }
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     public void keyPressed(KeyEvent e)
/*      */     {
/* 2663 */       if (isNavigationKey(e)) {
/* 2664 */         this.prefix = "";
/* 2665 */         this.typedString = "";
/* 2666 */         this.lastTime = 0L;
/*      */       }
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     public void keyReleased(KeyEvent e) {}
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     private boolean isNavigationKey(KeyEvent event)
/*      */     {
/* 2684 */       InputMap inputMap = BasicXListUI.this.list.getInputMap(1);
/* 2685 */       KeyStroke key = KeyStroke.getKeyStrokeForEvent(event);
/*      */       
/* 2687 */       if ((inputMap != null) && (inputMap.get(key) != null)) {
/* 2688 */         return true;
/*      */       }
/* 2690 */       return false;
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */     public void propertyChange(PropertyChangeEvent e)
/*      */     {
/* 2697 */       String propertyName = e.getPropertyName();
/*      */       
/*      */ 
/*      */ 
/*      */ 
/* 2702 */       if (propertyName == "model") {
/* 2703 */         ListModel oldModel = (ListModel)e.getOldValue();
/* 2704 */         ListModel newModel = (ListModel)e.getNewValue();
/* 2705 */         if (oldModel != null) {
/* 2706 */           oldModel.removeListDataListener(BasicXListUI.this.listDataListener);
/*      */         }
/* 2708 */         if (newModel != null) {
/* 2709 */           newModel.addListDataListener(BasicXListUI.this.listDataListener);
/*      */         }
/* 2711 */         BasicXListUI.this.updateLayoutStateNeeded |= 0x1;
/* 2712 */         BasicXListUI.this.redrawList();
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       }
/* 2718 */       else if (propertyName == "selectionModel") {
/* 2719 */         ListSelectionModel oldModel = (ListSelectionModel)e.getOldValue();
/* 2720 */         ListSelectionModel newModel = (ListSelectionModel)e.getNewValue();
/* 2721 */         if (oldModel != null) {
/* 2722 */           oldModel.removeListSelectionListener(BasicXListUI.this.listSelectionListener);
/*      */         }
/* 2724 */         if (newModel != null) {
/* 2725 */           newModel.addListSelectionListener(BasicXListUI.this.listSelectionListener);
/*      */         }
/* 2727 */         BasicXListUI.this.updateLayoutStateNeeded |= 0x1;
/* 2728 */         BasicXListUI.this.redrawList();
/*      */       }
/* 2730 */       else if (propertyName == "cellRenderer") {
/* 2731 */         BasicXListUI.this.updateLayoutStateNeeded |= 0x40;
/* 2732 */         BasicXListUI.this.redrawList();
/*      */       }
/* 2734 */       else if (propertyName == "font") {
/* 2735 */         BasicXListUI.this.updateLayoutStateNeeded |= 0x4;
/* 2736 */         BasicXListUI.this.redrawList();
/*      */       }
/* 2738 */       else if (propertyName == "prototypeCellValue") {
/* 2739 */         BasicXListUI.this.updateLayoutStateNeeded |= 0x20;
/* 2740 */         BasicXListUI.this.redrawList();
/*      */       }
/* 2742 */       else if (propertyName == "fixedCellHeight") {
/* 2743 */         BasicXListUI.this.updateLayoutStateNeeded |= 0x10;
/* 2744 */         BasicXListUI.this.redrawList();
/*      */       }
/* 2746 */       else if (propertyName == "fixedCellWidth") {
/* 2747 */         BasicXListUI.this.updateLayoutStateNeeded |= 0x8;
/* 2748 */         BasicXListUI.this.redrawList();
/*      */       }
/* 2750 */       else if (propertyName == "cellRenderer") {
/* 2751 */         BasicXListUI.this.updateLayoutStateNeeded |= 0x40;
/* 2752 */         BasicXListUI.this.redrawList();
/*      */       }
/* 2754 */       else if (propertyName == "selectionForeground") {
/* 2755 */         BasicXListUI.this.list.repaint();
/*      */       }
/* 2757 */       else if (propertyName == "selectionBackground") {
/* 2758 */         BasicXListUI.this.list.repaint();
/*      */       }
/* 2760 */       else if ("layoutOrientation" == propertyName) {
/* 2761 */         BasicXListUI.this.updateLayoutStateNeeded |= 0x80;
/* 2762 */         BasicXListUI.this.layoutOrientation = BasicXListUI.this.list.getLayoutOrientation();
/* 2763 */         BasicXListUI.this.redrawList();
/*      */       }
/* 2765 */       else if ("visibleRowCount" == propertyName) {
/* 2766 */         if (BasicXListUI.this.layoutOrientation != 0) {
/* 2767 */           BasicXListUI.this.updateLayoutStateNeeded |= 0x80;
/* 2768 */           BasicXListUI.this.redrawList();
/*      */         }
/*      */       }
/* 2771 */       else if ("componentOrientation" == propertyName) {
/* 2772 */         BasicXListUI.this.isLeftToRight = BasicXListUI.this.list.getComponentOrientation().isLeftToRight();
/* 2773 */         BasicXListUI.this.updateLayoutStateNeeded |= 0x400;
/* 2774 */         BasicXListUI.this.redrawList();
/*      */         
/* 2776 */         InputMap inputMap = BasicXListUI.this.getInputMap(0);
/* 2777 */         SwingUtilities.replaceUIInputMap(BasicXListUI.this.list, 0, inputMap);
/*      */       }
/* 2779 */       else if ("List.isFileList" == propertyName) {
/* 2780 */         BasicXListUI.this.updateIsFileList();
/* 2781 */         BasicXListUI.this.redrawList();
/* 2782 */       } else if ("dropLocation" == propertyName) {
/* 2783 */         JList.DropLocation oldValue = (JList.DropLocation)e.getOldValue();
/* 2784 */         repaintDropLocation(oldValue);
/* 2785 */         repaintDropLocation(BasicXListUI.this.list.getDropLocation());
/*      */       }
/*      */     }
/*      */     
/*      */     private void repaintDropLocation(JList.DropLocation loc) {
/* 2790 */       if (loc == null) {
/*      */         return;
/*      */       }
/*      */       
/*      */       Rectangle r;
/*      */       Rectangle r;
/* 2796 */       if (loc.isInsert()) {
/* 2797 */         r = BasicXListUI.this.getDropLineRect(loc);
/*      */       } else {
/* 2799 */         r = BasicXListUI.this.getCellBounds(BasicXListUI.this.list, loc.getIndex());
/*      */       }
/*      */       
/* 2802 */       if (r != null) {
/* 2803 */         BasicXListUI.this.list.repaint(r);
/*      */       }
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */     public void intervalAdded(ListDataEvent e)
/*      */     {
/* 2811 */       BasicXListUI.this.updateLayoutStateNeeded = 1;
/*      */       
/* 2813 */       int minIndex = Math.min(e.getIndex0(), e.getIndex1());
/* 2814 */       int maxIndex = Math.max(e.getIndex0(), e.getIndex1());
/*      */       
/*      */ 
/*      */ 
/*      */ 
/* 2819 */       ListSelectionModel sm = BasicXListUI.this.list.getSelectionModel();
/* 2820 */       if (sm != null) {
/* 2821 */         sm.insertIndexInterval(minIndex, maxIndex - minIndex + 1, true);
/*      */       }
/*      */       
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 2828 */       BasicXListUI.this.redrawList();
/*      */     }
/*      */     
/*      */ 
/*      */     public void intervalRemoved(ListDataEvent e)
/*      */     {
/* 2834 */       BasicXListUI.this.updateLayoutStateNeeded = 1;
/*      */       
/*      */ 
/*      */ 
/*      */ 
/* 2839 */       ListSelectionModel sm = BasicXListUI.this.list.getSelectionModel();
/* 2840 */       if (sm != null) {
/* 2841 */         sm.removeIndexInterval(e.getIndex0(), e.getIndex1());
/*      */       }
/*      */       
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 2849 */       BasicXListUI.this.redrawList();
/*      */     }
/*      */     
/*      */     public void contentsChanged(ListDataEvent e)
/*      */     {
/* 2854 */       BasicXListUI.this.updateLayoutStateNeeded = 1;
/* 2855 */       BasicXListUI.this.redrawList();
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */     public void valueChanged(ListSelectionEvent e)
/*      */     {
/* 2863 */       BasicXListUI.this.maybeUpdateLayoutState();
/* 2864 */       int size = BasicXListUI.this.getElementCount();
/* 2865 */       int firstIndex = Math.min(size - 1, Math.max(e.getFirstIndex(), 0));
/* 2866 */       int lastIndex = Math.min(size - 1, Math.max(e.getLastIndex(), 0));
/*      */       
/* 2868 */       Rectangle bounds = BasicXListUI.this.getCellBounds(BasicXListUI.this.list, firstIndex, lastIndex);
/*      */       
/* 2870 */       if (bounds != null) {
/* 2871 */         BasicXListUI.this.list.repaint(bounds.x, bounds.y, bounds.width, bounds.height);
/*      */       }
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */     public void mouseClicked(MouseEvent e) {}
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */     public void mouseEntered(MouseEvent e) {}
/*      */     
/*      */ 
/*      */ 
/*      */     public void mouseExited(MouseEvent e) {}
/*      */     
/*      */ 
/*      */ 
/*      */     public void mousePressed(MouseEvent e)
/*      */     {
/* 2893 */       if (SwingXUtilities.shouldIgnore(e, BasicXListUI.this.list)) {
/* 2894 */         return;
/*      */       }
/*      */       
/* 2897 */       boolean dragEnabled = BasicXListUI.this.list.getDragEnabled();
/* 2898 */       boolean grabFocus = true;
/*      */       
/*      */ 
/* 2901 */       if (dragEnabled)
/*      */       {
/* 2903 */         int row = SwingXUtilities.loc2IndexFileList(BasicXListUI.this.list, e.getPoint());
/*      */         
/* 2905 */         if ((row != -1) && (DragRecognitionSupport.mousePressed(e))) {
/* 2906 */           this.dragPressDidSelection = false;
/*      */           
/* 2908 */           if (e.isControlDown())
/*      */           {
/*      */ 
/* 2911 */             return; }
/* 2912 */           if ((!e.isShiftDown()) && (BasicXListUI.this.list.isSelectedIndex(row)))
/*      */           {
/*      */ 
/* 2915 */             BasicXListUI.this.list.addSelectionInterval(row, row);
/* 2916 */             return;
/*      */           }
/*      */           
/*      */ 
/* 2920 */           grabFocus = false;
/*      */           
/* 2922 */           this.dragPressDidSelection = true;
/*      */         }
/*      */         
/*      */       }
/*      */       else
/*      */       {
/* 2928 */         BasicXListUI.this.list.setValueIsAdjusting(true);
/*      */       }
/*      */       
/* 2931 */       if (grabFocus) {
/* 2932 */         SwingXUtilities.adjustFocus(BasicXListUI.this.list);
/*      */       }
/*      */       
/* 2935 */       adjustSelection(e);
/*      */     }
/*      */     
/*      */     private void adjustSelection(MouseEvent e)
/*      */     {
/* 2940 */       int row = SwingXUtilities.loc2IndexFileList(BasicXListUI.this.list, e.getPoint());
/* 2941 */       if (row < 0)
/*      */       {
/*      */ 
/* 2944 */         if ((BasicXListUI.this.isFileList) && (e.getID() == 501) && ((!e.isShiftDown()) || (BasicXListUI.this.list.getSelectionMode() == 0)))
/*      */         {
/*      */ 
/*      */ 
/* 2948 */           BasicXListUI.this.list.clearSelection();
/*      */         }
/*      */       }
/*      */       else {
/* 2952 */         int anchorIndex = BasicXListUI.adjustIndex(BasicXListUI.this.list.getAnchorSelectionIndex(), BasicXListUI.this.list);
/*      */         boolean anchorSelected;
/* 2954 */         boolean anchorSelected; if (anchorIndex == -1) {
/* 2955 */           anchorIndex = 0;
/* 2956 */           anchorSelected = false;
/*      */         } else {
/* 2958 */           anchorSelected = BasicXListUI.this.list.isSelectedIndex(anchorIndex);
/*      */         }
/*      */         
/* 2961 */         if (e.isControlDown()) {
/* 2962 */           if (e.isShiftDown()) {
/* 2963 */             if (anchorSelected) {
/* 2964 */               BasicXListUI.this.list.addSelectionInterval(anchorIndex, row);
/*      */             } else {
/* 2966 */               BasicXListUI.this.list.removeSelectionInterval(anchorIndex, row);
/* 2967 */               if (BasicXListUI.this.isFileList) {
/* 2968 */                 BasicXListUI.this.list.addSelectionInterval(row, row);
/* 2969 */                 BasicXListUI.this.list.getSelectionModel().setAnchorSelectionIndex(anchorIndex);
/*      */               }
/*      */             }
/* 2972 */           } else if (BasicXListUI.this.list.isSelectedIndex(row)) {
/* 2973 */             BasicXListUI.this.list.removeSelectionInterval(row, row);
/*      */           } else {
/* 2975 */             BasicXListUI.this.list.addSelectionInterval(row, row);
/*      */           }
/* 2977 */         } else if (e.isShiftDown()) {
/* 2978 */           BasicXListUI.this.list.setSelectionInterval(anchorIndex, row);
/*      */         } else {
/* 2980 */           BasicXListUI.this.list.setSelectionInterval(row, row);
/*      */         }
/*      */       }
/*      */     }
/*      */     
/*      */     public void dragStarting(MouseEvent me) {
/* 2986 */       if (me.isControlDown())
/*      */       {
/* 2988 */         int row = SwingXUtilities.loc2IndexFileList(BasicXListUI.this.list, me.getPoint());
/* 2989 */         BasicXListUI.this.list.addSelectionInterval(row, row);
/*      */       }
/*      */     }
/*      */     
/*      */     public void mouseDragged(MouseEvent e) {
/* 2994 */       if (SwingXUtilities.shouldIgnore(e, BasicXListUI.this.list)) {
/* 2995 */         return;
/*      */       }
/*      */       
/* 2998 */       if (BasicXListUI.this.list.getDragEnabled()) {
/* 2999 */         DragRecognitionSupport.mouseDragged(e, this);
/* 3000 */         return;
/*      */       }
/*      */       
/* 3003 */       if ((e.isShiftDown()) || (e.isControlDown())) {
/* 3004 */         return;
/*      */       }
/*      */       
/* 3007 */       int row = BasicXListUI.this.locationToIndex(BasicXListUI.this.list, e.getPoint());
/* 3008 */       if (row != -1)
/*      */       {
/* 3010 */         if (BasicXListUI.this.isFileList) {
/* 3011 */           return;
/*      */         }
/* 3013 */         Rectangle cellBounds = BasicXListUI.this.getCellBounds(BasicXListUI.this.list, row, row);
/* 3014 */         if (cellBounds != null) {
/* 3015 */           BasicXListUI.this.list.scrollRectToVisible(cellBounds);
/* 3016 */           BasicXListUI.this.list.setSelectionInterval(row, row);
/*      */         }
/*      */       }
/*      */     }
/*      */     
/*      */     public void mouseMoved(MouseEvent e) {}
/*      */     
/*      */     public void mouseReleased(MouseEvent e)
/*      */     {
/* 3025 */       if (SwingXUtilities.shouldIgnore(e, BasicXListUI.this.list)) {
/* 3026 */         return;
/*      */       }
/*      */       
/* 3029 */       if (BasicXListUI.this.list.getDragEnabled()) {
/* 3030 */         MouseEvent me = DragRecognitionSupport.mouseReleased(e);
/* 3031 */         if (me != null) {
/* 3032 */           SwingXUtilities.adjustFocus(BasicXListUI.this.list);
/* 3033 */           if (!this.dragPressDidSelection) {
/* 3034 */             adjustSelection(me);
/*      */           }
/*      */         }
/*      */       } else {
/* 3038 */         BasicXListUI.this.list.setValueIsAdjusting(false);
/*      */       }
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */     protected void repaintCellFocus()
/*      */     {
/* 3047 */       int leadIndex = BasicXListUI.adjustIndex(BasicXListUI.this.list.getLeadSelectionIndex(), BasicXListUI.this.list);
/* 3048 */       if (leadIndex != -1) {
/* 3049 */         Rectangle r = BasicXListUI.this.getCellBounds(BasicXListUI.this.list, leadIndex, leadIndex);
/* 3050 */         if (r != null) {
/* 3051 */           BasicXListUI.this.list.repaint(r.x, r.y, r.width, r.height);
/*      */         }
/*      */       }
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */     public void focusGained(FocusEvent e)
/*      */     {
/* 3061 */       repaintCellFocus();
/*      */     }
/*      */     
/*      */     public void focusLost(FocusEvent e) {
/* 3065 */       repaintCellFocus();
/*      */     }
/*      */   }
/*      */   
/*      */   private static int adjustIndex(int index, JList list) {
/* 3070 */     return index < ((JXList)list).getElementCount() ? index : -1;
/*      */   }
/*      */   
/* 3073 */   private static final TransferHandler defaultTransferHandler = new ListTransferHandler();
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   static class ListTransferHandler
/*      */     extends TransferHandler
/*      */     implements UIResource
/*      */   {
/*      */     protected Transferable createTransferable(JComponent c)
/*      */     {
/* 3087 */       if ((c instanceof JList)) {
/* 3088 */         JList list = (JList)c;
/* 3089 */         Object[] values = list.getSelectedValues();
/*      */         
/* 3091 */         if ((values == null) || (values.length == 0)) {
/* 3092 */           return null;
/*      */         }
/*      */         
/* 3095 */         StringBuffer plainBuf = new StringBuffer();
/* 3096 */         StringBuffer htmlBuf = new StringBuffer();
/*      */         
/* 3098 */         htmlBuf.append("<html>\n<body>\n<ul>\n");
/*      */         
/* 3100 */         for (int i = 0; i < values.length; i++) {
/* 3101 */           Object obj = values[i];
/* 3102 */           String val = obj == null ? "" : obj.toString();
/* 3103 */           plainBuf.append(val + "\n");
/* 3104 */           htmlBuf.append("  <li>" + val + "\n");
/*      */         }
/*      */         
/*      */ 
/* 3108 */         plainBuf.deleteCharAt(plainBuf.length() - 1);
/* 3109 */         htmlBuf.append("</ul>\n</body>\n</html>");
/*      */         
/* 3111 */         return new BasicTransferable(plainBuf.toString(), htmlBuf.toString());
/*      */       }
/*      */       
/* 3114 */       return null;
/*      */     }
/*      */     
/*      */     public int getSourceActions(JComponent c) {
/* 3118 */       return 1;
/*      */     }
/*      */   }
/*      */ }


/* Location:              E:\java\java学习\hutubill\lib\all.jar!\org\jdesktop\swingx\plaf\basic\core\BasicXListUI.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */
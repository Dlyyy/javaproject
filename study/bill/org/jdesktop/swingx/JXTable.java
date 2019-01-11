/*      */ package org.jdesktop.swingx;
/*      */ 
/*      */ import java.applet.Applet;
/*      */ import java.awt.Color;
/*      */ import java.awt.Component;
/*      */ import java.awt.ComponentOrientation;
/*      */ import java.awt.Container;
/*      */ import java.awt.Dimension;
/*      */ import java.awt.FontMetrics;
/*      */ import java.awt.KeyboardFocusManager;
/*      */ import java.awt.Point;
/*      */ import java.awt.Rectangle;
/*      */ import java.awt.Window;
/*      */ import java.awt.event.ActionEvent;
/*      */ import java.awt.print.PrinterException;
/*      */ import java.beans.PropertyChangeEvent;
/*      */ import java.beans.PropertyChangeListener;
/*      */ import java.lang.reflect.Constructor;
/*      */ import java.util.Arrays;
/*      */ import java.util.Collections;
/*      */ import java.util.Comparator;
/*      */ import java.util.Date;
/*      */ import java.util.Enumeration;
/*      */ import java.util.EventObject;
/*      */ import java.util.HashMap;
/*      */ import java.util.Hashtable;
/*      */ import java.util.Iterator;
/*      */ import java.util.List;
/*      */ import java.util.Locale;
/*      */ import java.util.Map;
/*      */ import java.util.TreeSet;
/*      */ import java.util.Vector;
/*      */ import java.util.logging.Level;
/*      */ import java.util.logging.Logger;
/*      */ import javax.swing.Action;
/*      */ import javax.swing.ActionMap;
/*      */ import javax.swing.DefaultCellEditor;
/*      */ import javax.swing.Icon;
/*      */ import javax.swing.ImageIcon;
/*      */ import javax.swing.InputMap;
/*      */ import javax.swing.JCheckBox;
/*      */ import javax.swing.JComponent;
/*      */ import javax.swing.JPopupMenu;
/*      */ import javax.swing.JScrollPane;
/*      */ import javax.swing.JTable;
/*      */ import javax.swing.JTextField;
/*      */ import javax.swing.JViewport;
/*      */ import javax.swing.KeyStroke;
/*      */ import javax.swing.ListSelectionModel;
/*      */ import javax.swing.RowFilter;
/*      */ import javax.swing.RowSorter;
/*      */ import javax.swing.RowSorter.SortKey;
/*      */ import javax.swing.SortOrder;
/*      */ import javax.swing.SwingUtilities;
/*      */ import javax.swing.UIDefaults;
/*      */ import javax.swing.UIDefaults.ProxyLazyValue;
/*      */ import javax.swing.UIManager;
/*      */ import javax.swing.border.LineBorder;
/*      */ import javax.swing.event.ChangeEvent;
/*      */ import javax.swing.event.ChangeListener;
/*      */ import javax.swing.event.ListSelectionEvent;
/*      */ import javax.swing.event.RowSorterEvent;
/*      */ import javax.swing.event.RowSorterEvent.Type;
/*      */ import javax.swing.event.TableColumnModelEvent;
/*      */ import javax.swing.event.TableModelEvent;
/*      */ import javax.swing.table.JTableHeader;
/*      */ import javax.swing.table.TableCellEditor;
/*      */ import javax.swing.table.TableCellRenderer;
/*      */ import javax.swing.table.TableColumn;
/*      */ import javax.swing.table.TableColumnModel;
/*      */ import javax.swing.table.TableModel;
/*      */ import org.jdesktop.swingx.action.AbstractActionExt;
/*      */ import org.jdesktop.swingx.action.BoundAction;
/*      */ import org.jdesktop.swingx.decorator.ComponentAdapter;
/*      */ import org.jdesktop.swingx.decorator.CompoundHighlighter;
/*      */ import org.jdesktop.swingx.decorator.Highlighter;
/*      */ import org.jdesktop.swingx.decorator.ResetDTCRColorHighlighter;
/*      */ import org.jdesktop.swingx.event.TableColumnModelExtListener;
/*      */ import org.jdesktop.swingx.plaf.LookAndFeelAddons;
/*      */ import org.jdesktop.swingx.plaf.TableAddon;
/*      */ import org.jdesktop.swingx.plaf.UIDependent;
/*      */ import org.jdesktop.swingx.plaf.UIManagerExt;
/*      */ import org.jdesktop.swingx.renderer.AbstractRenderer;
/*      */ import org.jdesktop.swingx.renderer.CheckBoxProvider;
/*      */ import org.jdesktop.swingx.renderer.ComponentProvider;
/*      */ import org.jdesktop.swingx.renderer.DefaultTableRenderer;
/*      */ import org.jdesktop.swingx.renderer.IconValues;
/*      */ import org.jdesktop.swingx.renderer.MappedValue;
/*      */ import org.jdesktop.swingx.renderer.StringValue;
/*      */ import org.jdesktop.swingx.renderer.StringValues;
/*      */ import org.jdesktop.swingx.rollover.RolloverProducer;
/*      */ import org.jdesktop.swingx.rollover.TableRolloverController;
/*      */ import org.jdesktop.swingx.rollover.TableRolloverProducer;
/*      */ import org.jdesktop.swingx.search.SearchFactory;
/*      */ import org.jdesktop.swingx.search.Searchable;
/*      */ import org.jdesktop.swingx.search.TableSearchable;
/*      */ import org.jdesktop.swingx.sort.DefaultSortController;
/*      */ import org.jdesktop.swingx.sort.SortController;
/*      */ import org.jdesktop.swingx.sort.SortUtils;
/*      */ import org.jdesktop.swingx.sort.StringValueRegistry;
/*      */ import org.jdesktop.swingx.sort.TableSortController;
/*      */ import org.jdesktop.swingx.table.ColumnControlButton;
/*      */ import org.jdesktop.swingx.table.ColumnFactory;
/*      */ import org.jdesktop.swingx.table.DefaultTableColumnModelExt;
/*      */ import org.jdesktop.swingx.table.NumberEditorExt;
/*      */ import org.jdesktop.swingx.table.TableColumnExt;
/*      */ import org.jdesktop.swingx.table.TableColumnModelExt;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ public class JXTable
/*      */   extends JTable
/*      */   implements TableColumnModelExtListener
/*      */ {
/*      */   public static final String FOCUS_PREVIOUS_COMPONENT = "focusPreviousComponent";
/*      */   public static final String FOCUS_NEXT_COMPONENT = "focusNextComponent";
/*  355 */   private static final Logger LOG = Logger.getLogger(JXTable.class.getName());
/*      */   
/*      */ 
/*      */   public static final String HORIZONTALSCROLL_ACTION_COMMAND = "column.horizontalScroll";
/*      */   
/*      */ 
/*      */   public static final String PACKALL_ACTION_COMMAND = "column.packAll";
/*      */   
/*      */ 
/*      */   public static final String PACKSELECTED_ACTION_COMMAND = "column.packSelected";
/*      */   
/*      */ 
/*      */   public static final String UIPREFIX = "JXTable.";
/*      */   
/*      */ 
/*      */   public static final String MATCH_HIGHLIGHTER = "match.highlighter";
/*      */   
/*      */ 
/*      */   protected CompoundHighlighter compoundHighlighter;
/*      */   
/*      */ 
/*      */   public static final String USE_DTCR_COLORMEMORY_HACK = "useDTCRColorMemoryHack";
/*      */   
/*      */ 
/*      */   protected Highlighter resetDefaultTableCellRendererHighlighter;
/*      */   
/*      */   protected ComponentAdapter dataAdapter;
/*      */   
/*      */   private ChangeListener highlighterChangeListener;
/*      */   
/*      */   private ColumnFactory columnFactory;
/*      */   
/*      */ 
/*      */   static
/*      */   {
/*  390 */     LookAndFeelAddons.getAddon();
/*  391 */     LookAndFeelAddons.contribute(new TableAddon());
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
/*  423 */   private int visibleRowCount = 20;
/*      */   
/*      */ 
/*  426 */   private int visibleColumnCount = -1;
/*      */   
/*      */ 
/*      */ 
/*      */   private boolean columnControlVisible;
/*      */   
/*      */ 
/*      */ 
/*      */   private int verticalScrollPolicy;
/*      */   
/*      */ 
/*      */ 
/*      */   private JComponent columnControlButton;
/*      */   
/*      */ 
/*      */ 
/*      */   private RolloverProducer rolloverProducer;
/*      */   
/*      */ 
/*      */ 
/*      */   private TableRolloverController<JXTable> linkController;
/*      */   
/*      */ 
/*      */ 
/*      */   private int oldAutoResizeMode;
/*      */   
/*      */ 
/*      */ 
/*      */   private boolean intelliMode;
/*      */   
/*      */ 
/*      */ 
/*      */   private boolean inLayout;
/*      */   
/*      */ 
/*      */ 
/*      */   protected boolean isXTableRowHeightSet;
/*      */   
/*      */ 
/*      */ 
/*      */   protected Searchable searchable;
/*      */   
/*      */ 
/*      */ 
/*      */   private boolean editable;
/*      */   
/*      */ 
/*      */ 
/*      */   private Dimension calculatedPrefScrollableViewportSize;
/*      */   
/*      */ 
/*      */ 
/*      */   private boolean autoCreateRowSorter;
/*      */   
/*      */ 
/*      */ 
/*      */   private boolean sortable;
/*      */   
/*      */ 
/*      */ 
/*      */   private boolean sortsOnUpdates;
/*      */   
/*      */ 
/*      */   private boolean ignoreAddColumn;
/*      */   
/*      */ 
/*      */   private StringValueRegistry stringValueRegistry;
/*      */   
/*      */ 
/*      */   private SortOrder[] sortOrderCycle;
/*      */   
/*      */ 
/*      */   protected boolean forceRevalidate;
/*      */   
/*      */ 
/*      */   protected boolean filteredRowCountChanged;
/*      */   
/*      */ 
/*      */   protected transient CellEditorRemover editorRemover;
/*      */   
/*      */ 
/*      */ 
/*      */   public JXTable()
/*      */   {
/*  510 */     init();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public JXTable(TableModel dm)
/*      */   {
/*  519 */     super(dm);
/*  520 */     init();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public JXTable(TableModel dm, TableColumnModel cm)
/*      */   {
/*  529 */     super(dm, cm);
/*  530 */     init();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public JXTable(TableModel dm, TableColumnModel cm, ListSelectionModel sm)
/*      */   {
/*  542 */     super(dm, cm, sm);
/*  543 */     init();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public JXTable(int numRows, int numColumns)
/*      */   {
/*  553 */     super(numRows, numColumns);
/*  554 */     init();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public JXTable(Vector<?> rowData, Vector<?> columnNames)
/*      */   {
/*  564 */     super(rowData, columnNames);
/*  565 */     init();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public JXTable(Object[][] rowData, Object[] columnNames)
/*      */   {
/*  576 */     super(rowData, columnNames);
/*  577 */     init();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   private void init()
/*      */   {
/*  585 */     putClientProperty("useDTCRColorMemoryHack", Boolean.TRUE);
/*  586 */     initDefaultStringValues();
/*  587 */     this.sortOrderCycle = DefaultSortController.getDefaultSortOrderCycle();
/*  588 */     setSortsOnUpdates(true);
/*  589 */     setSortable(true);
/*  590 */     setAutoCreateRowSorter(true);
/*  591 */     setRolloverEnabled(true);
/*  592 */     setEditable(true);
/*  593 */     setTerminateEditOnFocusLost(true);
/*  594 */     initActionsAndBindings();
/*  595 */     initFocusBindings();
/*      */     
/*  597 */     updateRowHeightUI(false);
/*      */     
/*  599 */     setPreferredScrollableViewportSize(null);
/*      */     
/*      */ 
/*  602 */     initializeColumnWidths();
/*  603 */     setFillsViewportHeight(true);
/*  604 */     updateLocaleState(getLocale());
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
/*      */   public void setRolloverEnabled(boolean rolloverEnabled)
/*      */   {
/*  627 */     boolean old = isRolloverEnabled();
/*  628 */     if (rolloverEnabled == old)
/*  629 */       return;
/*  630 */     if (rolloverEnabled) {
/*  631 */       this.rolloverProducer = createRolloverProducer();
/*  632 */       this.rolloverProducer.install(this);
/*  633 */       getLinkController().install(this);
/*      */     }
/*      */     else {
/*  636 */       this.rolloverProducer.release(this);
/*  637 */       this.rolloverProducer = null;
/*  638 */       getLinkController().release();
/*      */     }
/*  640 */     firePropertyChange("rolloverEnabled", old, isRolloverEnabled());
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public boolean isRolloverEnabled()
/*      */   {
/*  651 */     return this.rolloverProducer != null;
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
/*      */   protected TableRolloverController<JXTable> getLinkController()
/*      */   {
/*  668 */     if (this.linkController == null) {
/*  669 */       this.linkController = createLinkController();
/*      */     }
/*  671 */     return this.linkController;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected TableRolloverController<JXTable> createLinkController()
/*      */   {
/*  683 */     return new TableRolloverController();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected RolloverProducer createRolloverProducer()
/*      */   {
/*  695 */     return new TableRolloverProducer();
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
/*      */   public boolean isColumnControlVisible()
/*      */   {
/*  708 */     return this.columnControlVisible;
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
/*      */   public void setColumnControlVisible(boolean visible)
/*      */   {
/*  730 */     if (isColumnControlVisible() == visible)
/*  731 */       return;
/*  732 */     boolean old = isColumnControlVisible();
/*  733 */     if (old) {
/*  734 */       unconfigureColumnControl();
/*      */     }
/*  736 */     this.columnControlVisible = visible;
/*  737 */     if (isColumnControlVisible()) {
/*  738 */       configureColumnControl();
/*      */     }
/*  740 */     firePropertyChange("columnControlVisible", old, !old);
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
/*      */   public JComponent getColumnControl()
/*      */   {
/*  753 */     if (this.columnControlButton == null) {
/*  754 */       this.columnControlButton = createDefaultColumnControl();
/*      */     }
/*  756 */     return this.columnControlButton;
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
/*      */   public void setColumnControl(JComponent columnControl)
/*      */   {
/*  784 */     JComponent old = this.columnControlButton;
/*  785 */     this.columnControlButton = columnControl;
/*  786 */     configureColumnControl();
/*  787 */     firePropertyChange("columnControl", old, getColumnControl());
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
/*      */   protected JComponent createDefaultColumnControl()
/*      */   {
/*  801 */     return new ColumnControlButton(this);
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
/*      */   public void setComponentOrientation(ComponentOrientation o)
/*      */   {
/*  819 */     super.setComponentOrientation(o);
/*  820 */     configureColumnControl();
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
/*      */   protected void configureEnclosingScrollPane()
/*      */   {
/*  835 */     super.configureEnclosingScrollPane();
/*  836 */     configureColumnControl();
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
/*      */   protected void unconfigureEnclosingScrollPane()
/*      */   {
/*  851 */     unconfigureColumnControl();
/*  852 */     super.unconfigureEnclosingScrollPane();
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
/*      */   protected void unconfigureColumnControl()
/*      */   {
/*  865 */     Container p = getParent();
/*  866 */     if ((p instanceof JViewport)) {
/*  867 */       Container gp = p.getParent();
/*  868 */       if ((gp instanceof JScrollPane)) {
/*  869 */         JScrollPane scrollPane = (JScrollPane)gp;
/*      */         
/*      */ 
/*      */ 
/*  873 */         JViewport viewport = scrollPane.getViewport();
/*  874 */         if ((viewport == null) || (viewport.getView() != this)) {
/*  875 */           return;
/*      */         }
/*  877 */         if (this.verticalScrollPolicy != 0)
/*      */         {
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  883 */           scrollPane.setVerticalScrollBarPolicy(this.verticalScrollPolicy);
/*  884 */           this.verticalScrollPolicy = 0;
/*      */         }
/*  886 */         if (isColumnControlVisible()) {
/*  887 */           scrollPane.setCorner("UPPER_TRAILING_CORNER", null);
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
/*      */ 
/*      */ 
/*      */   protected void configureColumnControl()
/*      */   {
/*  907 */     Container p = getParent();
/*  908 */     if ((p instanceof JViewport)) {
/*  909 */       Container gp = p.getParent();
/*  910 */       if ((gp instanceof JScrollPane)) {
/*  911 */         JScrollPane scrollPane = (JScrollPane)gp;
/*      */         
/*      */ 
/*      */ 
/*  915 */         JViewport viewport = scrollPane.getViewport();
/*  916 */         if ((viewport == null) || (viewport.getView() != this)) {
/*  917 */           return;
/*      */         }
/*  919 */         if (isColumnControlVisible()) {
/*  920 */           if (this.verticalScrollPolicy == 0) {
/*  921 */             this.verticalScrollPolicy = scrollPane.getVerticalScrollBarPolicy();
/*      */           }
/*      */           
/*  924 */           scrollPane.setCorner("UPPER_TRAILING_CORNER", getColumnControl());
/*      */           
/*      */ 
/*  927 */           scrollPane.setVerticalScrollBarPolicy(22);
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
/*      */   private void initFocusBindings()
/*      */   {
/*  940 */     setFocusTraversalKeys(0, new TreeSet());
/*      */     
/*  942 */     setFocusTraversalKeys(1, new TreeSet());
/*      */     
/*  944 */     getInputMap(1).put(KeyStroke.getKeyStroke("ctrl TAB"), "focusNextComponent");
/*      */     
/*  946 */     getInputMap(1).put(KeyStroke.getKeyStroke("shift ctrl TAB"), "focusPreviousComponent");
/*      */     
/*      */ 
/*  949 */     getActionMap().put("focusNextComponent", createFocusTransferAction(true));
/*      */     
/*  951 */     getActionMap().put("focusPreviousComponent", createFocusTransferAction(false));
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
/*      */   private Action createFocusTransferAction(boolean forward)
/*      */   {
/*  964 */     BoundAction action = new BoundAction(null, forward ? "focusNextComponent" : "focusPreviousComponent");
/*      */     
/*  966 */     action.registerCallback(this, forward ? "transferFocus" : "transferFocusBackward");
/*      */     
/*  968 */     return action;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   private class Actions
/*      */     extends UIAction
/*      */   {
/*      */     Actions(String name)
/*      */     {
/*  981 */       super();
/*      */     }
/*      */     
/*      */     public void actionPerformed(ActionEvent evt) {
/*  985 */       if ("print".equals(getName())) {
/*      */         try {
/*  987 */           JXTable.this.print();
/*      */         }
/*      */         catch (PrinterException ex)
/*      */         {
/*  991 */           JXTable.LOG.log(Level.WARNING, "", ex);
/*      */         }
/*  993 */       } else if ("find".equals(getName())) {
/*  994 */         JXTable.this.doFind();
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
/*      */   private void initActionsAndBindings()
/*      */   {
/* 1009 */     ActionMap map = getActionMap();
/* 1010 */     map.put("print", new Actions("print"));
/* 1011 */     map.put("find", new Actions("find"));
/*      */     
/*      */ 
/* 1014 */     map.put("cancel", createCancelAction());
/* 1015 */     map.put("column.packAll", createPackAllAction());
/* 1016 */     map.put("column.packSelected", createPackSelectedAction());
/* 1017 */     map.put("column.horizontalScroll", createHorizontalScrollAction());
/*      */     
/*      */ 
/*      */ 
/* 1021 */     KeyStroke findStroke = SearchFactory.getInstance().getSearchAccelerator();
/*      */     
/* 1023 */     getInputMap(1).put(findStroke, "find");
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
/*      */   private Action createCancelAction()
/*      */   {
/* 1037 */     Action action = new AbstractActionExt()
/*      */     {
/*      */       public void actionPerformed(ActionEvent e) {
/* 1040 */         if (!JXTable.this.isEditing())
/* 1041 */           return;
/* 1042 */         JXTable.this.getCellEditor().cancelCellEditing();
/*      */       }
/*      */       
/*      */       public boolean isEnabled()
/*      */       {
/* 1047 */         return JXTable.this.isEditing();
/*      */       }
/*      */       
/* 1050 */     };
/* 1051 */     return action;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   private Action createHorizontalScrollAction()
/*      */   {
/* 1059 */     BoundAction action = new BoundAction(null, "column.horizontalScroll");
/*      */     
/* 1061 */     action.setStateAction();
/* 1062 */     action.registerCallback(this, "setHorizontalScrollEnabled");
/* 1063 */     action.setSelected(isHorizontalScrollEnabled());
/* 1064 */     return action;
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
/*      */   protected String getUIString(String key)
/*      */   {
/* 1077 */     return getUIString(key, getLocale());
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
/*      */   protected String getUIString(String key, Locale locale)
/*      */   {
/* 1091 */     String text = UIManagerExt.getString("JXTable." + key, locale);
/* 1092 */     return text != null ? text : key;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   private Action createPackSelectedAction()
/*      */   {
/* 1100 */     BoundAction action = new BoundAction(null, "column.packSelected");
/* 1101 */     action.registerCallback(this, "packSelected");
/* 1102 */     action.setEnabled(getSelectedColumnCount() > 0);
/* 1103 */     return action;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   private Action createPackAllAction()
/*      */   {
/* 1110 */     BoundAction action = new BoundAction(null, "column.packAll");
/* 1111 */     action.registerCallback(this, "packAll");
/* 1112 */     return action;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void setLocale(Locale locale)
/*      */   {
/* 1124 */     updateLocaleState(locale);
/* 1125 */     super.setLocale(locale);
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
/*      */   protected void updateLocaleState(Locale locale)
/*      */   {
/* 1142 */     updateLocaleActionState("column.horizontalScroll", locale);
/* 1143 */     updateLocaleActionState("column.packAll", locale);
/* 1144 */     updateLocaleActionState("column.packSelected", locale);
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
/*      */   protected void updateLocaleActionState(String key, Locale locale)
/*      */   {
/* 1158 */     Action action = getActionMap().get(key);
/* 1159 */     if (action == null)
/* 1160 */       return;
/* 1161 */     action.putValue("Name", getUIString(key, locale));
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
/*      */   public void packAll()
/*      */   {
/* 1175 */     packTable(-1);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void packSelected()
/*      */   {
/* 1186 */     int selected = getColumnModel().getSelectionModel().getLeadSelectionIndex();
/*      */     
/* 1188 */     if (selected >= 0) {
/* 1189 */       packColumn(selected, -1);
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
/*      */   public void columnSelectionChanged(ListSelectionEvent e)
/*      */   {
/* 1202 */     super.columnSelectionChanged(e);
/* 1203 */     if (e.getValueIsAdjusting())
/* 1204 */       return;
/* 1205 */     Action packSelected = getActionMap().get("column.packSelected");
/* 1206 */     if (packSelected != null) {
/* 1207 */       packSelected.setEnabled(!((ListSelectionModel)e.getSource()).isSelectionEmpty());
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
/*      */   public void setHorizontalScrollEnabled(boolean enabled)
/*      */   {
/* 1246 */     if (enabled == isHorizontalScrollEnabled()) {
/* 1247 */       return;
/*      */     }
/* 1249 */     boolean old = isHorizontalScrollEnabled();
/* 1250 */     if (enabled)
/*      */     {
/* 1252 */       if (getAutoResizeMode() != 0) {
/* 1253 */         this.oldAutoResizeMode = getAutoResizeMode();
/*      */       }
/* 1255 */       setAutoResizeMode(0);
/*      */       
/*      */ 
/* 1258 */       this.intelliMode = true;
/* 1259 */       updateHorizontalAction();
/*      */     } else {
/* 1261 */       setAutoResizeMode(this.oldAutoResizeMode);
/*      */     }
/* 1263 */     firePropertyChange("horizontalScrollEnabled", old, isHorizontalScrollEnabled());
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public boolean isHorizontalScrollEnabled()
/*      */   {
/* 1274 */     return (this.intelliMode) && (getAutoResizeMode() == 0);
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
/*      */   public void setAutoResizeMode(int mode)
/*      */   {
/* 1293 */     if (mode != 0) {
/* 1294 */       this.oldAutoResizeMode = mode;
/*      */     }
/* 1296 */     this.intelliMode = false;
/* 1297 */     super.setAutoResizeMode(mode);
/* 1298 */     updateHorizontalAction();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   protected void updateHorizontalAction()
/*      */   {
/* 1306 */     Action showHorizontal = getActionMap().get("column.horizontalScroll");
/*      */     
/* 1308 */     if ((showHorizontal instanceof BoundAction)) {
/* 1309 */       ((BoundAction)showHorizontal).setSelected(isHorizontalScrollEnabled());
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
/*      */   public boolean getScrollableTracksViewportWidth()
/*      */   {
/* 1325 */     boolean shouldTrack = super.getScrollableTracksViewportWidth();
/* 1326 */     if (isHorizontalScrollEnabled()) {
/* 1327 */       return hasExcessWidth();
/*      */     }
/* 1329 */     return shouldTrack;
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
/*      */   public void doLayout()
/*      */   {
/* 1344 */     int resizeMode = getAutoResizeMode();
/*      */     
/* 1346 */     if ((isHorizontalScrollEnabled()) && (hasRealizedParent()) && (hasExcessWidth()))
/*      */     {
/* 1348 */       this.autoResizeMode = this.oldAutoResizeMode;
/*      */     }
/* 1350 */     this.inLayout = true;
/* 1351 */     super.doLayout();
/* 1352 */     this.inLayout = false;
/* 1353 */     this.autoResizeMode = resizeMode;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   private boolean hasRealizedParent()
/*      */   {
/* 1361 */     return (getWidth() > 0) && (getParent() != null) && (getParent().getWidth() > 0);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   private boolean hasExcessWidth()
/*      */   {
/* 1372 */     return getPreferredSize().width < getParent().getWidth();
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
/*      */   public void columnMarginChanged(ChangeEvent e)
/*      */   {
/* 1386 */     if (isEditing()) {
/* 1387 */       removeEditor();
/*      */     }
/* 1389 */     TableColumn resizingColumn = getResizingColumn();
/*      */     
/*      */ 
/* 1392 */     if ((resizingColumn != null) && (this.autoResizeMode == 0) && (!this.inLayout))
/*      */     {
/* 1394 */       resizingColumn.setPreferredWidth(resizingColumn.getWidth());
/*      */     }
/* 1396 */     resizeAndRepaint();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   private TableColumn getResizingColumn()
/*      */   {
/* 1406 */     return this.tableHeader == null ? null : this.tableHeader.getResizingColumn();
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
/*      */   public void setFillsViewportHeight(boolean fillsViewportHeight)
/*      */   {
/* 1420 */     if (fillsViewportHeight == getFillsViewportHeight())
/* 1421 */       return;
/* 1422 */     super.setFillsViewportHeight(fillsViewportHeight);
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
/*      */   public void setValueAt(Object aValue, int row, int column)
/*      */   {
/* 1439 */     if (!isCellEditable(row, column))
/* 1440 */       return;
/* 1441 */     super.setValueAt(aValue, row, column);
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
/*      */   public boolean isCellEditable(int row, int column)
/*      */   {
/* 1471 */     if (!isEditable())
/* 1472 */       return false;
/* 1473 */     boolean editable = super.isCellEditable(row, column);
/* 1474 */     if (editable) {
/* 1475 */       TableColumnExt tableColumn = getColumnExt(column);
/* 1476 */       if (tableColumn != null) {
/* 1477 */         editable = tableColumn.isEditable();
/*      */       }
/*      */     }
/* 1480 */     return editable;
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
/*      */   public boolean getAutoCreateColumnsFromModel()
/*      */   {
/* 1507 */     return super.getAutoCreateColumnsFromModel();
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
/*      */   public void tableChanged(TableModelEvent e)
/*      */   {
/* 1528 */     preprocessModelChange(e);
/* 1529 */     super.tableChanged(e);
/* 1530 */     if ((isStructureChanged(e)) && (getAutoCreateColumnsFromModel())) {
/* 1531 */       initializeColumnWidths();
/* 1532 */       resetCalculatedScrollableSize(true);
/*      */     }
/* 1534 */     if (isStructureChanged(e)) {
/* 1535 */       updateStringValueRegistryColumnClasses();
/*      */     }
/* 1537 */     postprocessModelChange(e);
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
/*      */   public void sorterChanged(RowSorterEvent e)
/*      */   {
/* 1551 */     super.sorterChanged(e);
/* 1552 */     postprocessSorterChanged(e);
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
/*      */   protected void preprocessModelChange(TableModelEvent e)
/*      */   {
/* 1567 */     this.forceRevalidate = ((getSortsOnUpdates()) && (getRowFilter() != null) && (isUpdate(e)));
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected void postprocessModelChange(TableModelEvent e)
/*      */   {
/* 1577 */     if ((this.forceRevalidate) && (this.filteredRowCountChanged)) {
/* 1578 */       resizeAndRepaint();
/*      */     }
/* 1580 */     this.filteredRowCountChanged = false;
/* 1581 */     this.forceRevalidate = false;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected void postprocessSorterChanged(RowSorterEvent e)
/*      */   {
/* 1590 */     this.filteredRowCountChanged = false;
/* 1591 */     if ((this.forceRevalidate) && (e.getType() == RowSorterEvent.Type.SORTED)) {
/* 1592 */       this.filteredRowCountChanged = (e.getPreviousRowCount() != getRowCount());
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
/*      */   public void setModel(TableModel dataModel)
/*      */   {
/* 1605 */     boolean old = getAutoCreateRowSorter();
/*      */     try {
/* 1607 */       this.autoCreateRowSorter = false;
/* 1608 */       this.ignoreAddColumn = true;
/* 1609 */       super.setModel(dataModel);
/*      */     } finally {
/* 1611 */       this.autoCreateRowSorter = old;
/* 1612 */       this.ignoreAddColumn = false;
/*      */     }
/* 1614 */     if (getAutoCreateRowSorter()) {
/* 1615 */       setRowSorter(createDefaultRowSorter());
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void setColumnModel(TableColumnModel columnModel)
/*      */   {
/* 1627 */     super.setColumnModel(columnModel);
/* 1628 */     configureSorterProperties();
/* 1629 */     initPerColumnStringValues();
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
/*      */   public void setAutoCreateRowSorter(boolean autoCreateRowSorter)
/*      */   {
/* 1643 */     if (getAutoCreateRowSorter() == autoCreateRowSorter) return;
/* 1644 */     boolean oldValue = getAutoCreateRowSorter();
/* 1645 */     this.autoCreateRowSorter = autoCreateRowSorter;
/* 1646 */     if (autoCreateRowSorter) {
/* 1647 */       setRowSorter(createDefaultRowSorter());
/*      */     }
/* 1649 */     firePropertyChange("autoCreateRowSorter", oldValue, getAutoCreateRowSorter());
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public boolean getAutoCreateRowSorter()
/*      */   {
/* 1660 */     return this.autoCreateRowSorter;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void setRowSorter(RowSorter<? extends TableModel> sorter)
/*      */   {
/* 1671 */     super.setRowSorter(sorter);
/* 1672 */     configureSorterProperties();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected void configureSorterProperties()
/*      */   {
/* 1683 */     if ((this.ignoreAddColumn) || (!getControlsSorterProperties())) return;
/* 1684 */     getSortController().setStringValueProvider(getStringValueRegistry());
/*      */     
/* 1686 */     getSortController().setSortable(this.sortable);
/* 1687 */     getSortController().setSortsOnUpdates(this.sortsOnUpdates);
/* 1688 */     getSortController().setSortOrderCycle(getSortOrderCycle());
/*      */     
/* 1690 */     List<TableColumn> columns = getColumns(true);
/* 1691 */     for (TableColumn tableColumn : columns) {
/* 1692 */       int modelIndex = tableColumn.getModelIndex();
/* 1693 */       getSortController().setSortable(modelIndex, (tableColumn instanceof TableColumnExt) ? ((TableColumnExt)tableColumn).isSortable() : true);
/*      */       
/*      */ 
/* 1696 */       getSortController().setComparator(modelIndex, (tableColumn instanceof TableColumnExt) ? ((TableColumnExt)tableColumn).getComparator() : null);
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
/*      */   protected RowSorter<? extends TableModel> createDefaultRowSorter()
/*      */   {
/* 1714 */     return new TableSortController(getModel());
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected boolean isDataChanged(TableModelEvent e)
/*      */   {
/* 1726 */     if (e == null)
/* 1727 */       return false;
/* 1728 */     return (e.getType() == 0) && (e.getFirstRow() == 0) && (e.getLastRow() == Integer.MAX_VALUE);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected boolean isUpdate(TableModelEvent e)
/*      */   {
/* 1740 */     if (isStructureChanged(e))
/* 1741 */       return false;
/* 1742 */     return (e.getType() == 0) && (e.getLastRow() < Integer.MAX_VALUE);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected boolean isStructureChanged(TableModelEvent e)
/*      */   {
/* 1754 */     return (e == null) || (e.getFirstRow() == -1);
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
/*      */   public void setSortable(boolean sortable)
/*      */   {
/* 1781 */     boolean old = isSortable();
/* 1782 */     this.sortable = sortable;
/* 1783 */     if (getControlsSorterProperties()) {
/* 1784 */       getSortController().setSortable(sortable);
/*      */     }
/* 1786 */     firePropertyChange("sortable", old, isSortable());
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public boolean isSortable()
/*      */   {
/* 1796 */     return this.sortable;
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
/*      */   public void setSortsOnUpdates(boolean sortsOnUpdates)
/*      */   {
/* 1816 */     boolean old = getSortsOnUpdates();
/* 1817 */     this.sortsOnUpdates = sortsOnUpdates;
/* 1818 */     if (getControlsSorterProperties()) {
/* 1819 */       getSortController().setSortsOnUpdates(sortsOnUpdates);
/*      */     }
/* 1821 */     firePropertyChange("sortsOnUpdates", old, getSortsOnUpdates());
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public boolean getSortsOnUpdates()
/*      */   {
/* 1831 */     return this.sortsOnUpdates;
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
/*      */   public void setSortOrderCycle(SortOrder... cycle)
/*      */   {
/* 1844 */     SortOrder[] old = getSortOrderCycle();
/* 1845 */     if (getControlsSorterProperties()) {
/* 1846 */       getSortController().setSortOrderCycle(cycle);
/*      */     }
/* 1848 */     this.sortOrderCycle = ((SortOrder[])Arrays.copyOf(cycle, cycle.length));
/* 1849 */     firePropertyChange("sortOrderCycle", old, getSortOrderCycle());
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public SortOrder[] getSortOrderCycle()
/*      */   {
/* 1859 */     return (SortOrder[])Arrays.copyOf(this.sortOrderCycle, this.sortOrderCycle.length);
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
/*      */   public void setRowFilter(RowFilter<? super TableModel, ? super Integer> filter)
/*      */   {
/* 1874 */     if (hasSortController()) {
/* 1875 */       getSortController().setRowFilter(filter);
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
/*      */   public RowFilter<?, ?> getRowFilter()
/*      */   {
/* 1890 */     return hasSortController() ? getSortController().getRowFilter() : null;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void resetSortOrder()
/*      */   {
/* 1901 */     if (!hasSortController()) return;
/* 1902 */     getSortController().resetSortOrders();
/*      */     
/* 1904 */     if (getTableHeader() != null) {
/* 1905 */       getTableHeader().repaint();
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
/*      */   public void toggleSortOrder(int columnIndex)
/*      */   {
/* 1926 */     if (hasSortController()) {
/* 1927 */       getSortController().toggleSortOrder(convertColumnIndexToModel(columnIndex));
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
/*      */   public void setSortOrder(int columnIndex, SortOrder sortOrder)
/*      */   {
/* 1944 */     if (hasSortController()) {
/* 1945 */       getSortController().setSortOrder(convertColumnIndexToModel(columnIndex), sortOrder);
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
/*      */   public SortOrder getSortOrder(int columnIndex)
/*      */   {
/* 1959 */     if (hasSortController()) {
/* 1960 */       return getSortController().getSortOrder(convertColumnIndexToModel(columnIndex));
/*      */     }
/* 1962 */     return SortOrder.UNSORTED;
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
/*      */   public void toggleSortOrder(Object identifier)
/*      */   {
/* 1984 */     if (!hasSortController())
/* 1985 */       return;
/* 1986 */     TableColumn columnExt = getColumnByIdentifier(identifier);
/* 1987 */     if (columnExt == null)
/* 1988 */       return;
/* 1989 */     getSortController().toggleSortOrder(columnExt.getModelIndex());
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
/*      */   public void setSortOrder(Object identifier, SortOrder sortOrder)
/*      */   {
/* 2008 */     if (!hasSortController())
/* 2009 */       return;
/* 2010 */     TableColumn columnExt = getColumnByIdentifier(identifier);
/* 2011 */     if (columnExt == null)
/* 2012 */       return;
/* 2013 */     getSortController().setSortOrder(columnExt.getModelIndex(), sortOrder);
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
/*      */   public SortOrder getSortOrder(Object identifier)
/*      */   {
/* 2030 */     if (!hasSortController())
/* 2031 */       return SortOrder.UNSORTED;
/* 2032 */     TableColumn columnExt = getColumnByIdentifier(identifier);
/* 2033 */     if (columnExt == null)
/* 2034 */       return SortOrder.UNSORTED;
/* 2035 */     int modelIndex = columnExt.getModelIndex();
/* 2036 */     return getSortController().getSortOrder(modelIndex);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   private TableColumn getColumnByIdentifier(Object identifier)
/*      */   {
/*      */     TableColumn columnExt;
/*      */     
/*      */ 
/*      */ 
/*      */     try
/*      */     {
/* 2051 */       columnExt = getColumn(identifier);
/*      */     }
/*      */     catch (IllegalArgumentException e)
/*      */     {
/* 2055 */       columnExt = getColumnExt(identifier);
/*      */     }
/* 2057 */     return columnExt;
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
/*      */   @Deprecated
/*      */   protected boolean isSortable(int columnIndex)
/*      */   {
/* 2080 */     if (hasSortController()) {
/* 2081 */       return getSortController().isSortable(convertColumnIndexToModel(columnIndex));
/*      */     }
/*      */     
/*      */ 
/* 2085 */     boolean sortable = isSortable();
/* 2086 */     TableColumnExt tableColumnExt = getColumnExt(columnIndex);
/* 2087 */     if (tableColumnExt != null) {
/* 2088 */       sortable = (sortable) && (tableColumnExt.isSortable());
/*      */     }
/* 2090 */     return sortable;
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
/*      */   @Deprecated
/*      */   protected boolean isSortable(Object identifier)
/*      */   {
/* 2113 */     if (hasSortController()) {
/* 2114 */       TableColumn columnExt = null;
/* 2115 */       columnExt = getColumnByIdentifier(identifier);
/* 2116 */       if (columnExt != null) {
/* 2117 */         return getSortController().isSortable(columnExt.getModelIndex());
/*      */       }
/* 2119 */       return getSortController().isSortable();
/*      */     }
/*      */     
/*      */ 
/* 2123 */     boolean sortable = isSortable();
/* 2124 */     TableColumnExt tableColumnExt = getColumnExt(identifier);
/* 2125 */     if (tableColumnExt != null) {
/* 2126 */       sortable = (sortable) && (tableColumnExt.isSortable());
/*      */     }
/* 2128 */     return sortable;
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
/*      */   protected SortController<? extends TableModel> getSortController()
/*      */   {
/* 2146 */     if (hasSortController())
/*      */     {
/*      */ 
/* 2149 */       return (SortController)getRowSorter();
/*      */     }
/* 2151 */     return null;
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
/*      */   protected boolean hasSortController()
/*      */   {
/* 2164 */     return getRowSorter() instanceof SortController;
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
/*      */   protected boolean getControlsSorterProperties()
/*      */   {
/* 2183 */     return (hasSortController()) && (getAutoCreateRowSorter());
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
/*      */   public TableColumn getSortedColumn()
/*      */   {
/* 2197 */     RowSorter<?> controller = getRowSorter();
/* 2198 */     if (controller != null)
/*      */     {
/* 2200 */       RowSorter.SortKey sortKey = SortUtils.getFirstSortingKey(controller.getSortKeys());
/*      */       
/* 2202 */       if (sortKey != null) {
/* 2203 */         int sorterColumn = sortKey.getColumn();
/* 2204 */         List<TableColumn> columns = getColumns(true);
/* 2205 */         Iterator<TableColumn> iter = columns.iterator();
/* 2206 */         while (iter.hasNext()) {
/* 2207 */           TableColumn column = (TableColumn)iter.next();
/* 2208 */           if (column.getModelIndex() == sorterColumn) {
/* 2209 */             return column;
/*      */           }
/*      */         }
/*      */       }
/*      */     }
/*      */     
/* 2215 */     return null;
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
/*      */   public void columnAdded(TableColumnModelEvent e)
/*      */   {
/* 2229 */     super.columnAdded(e);
/*      */     
/* 2231 */     TableColumn column = getColumn(e.getToIndex());
/* 2232 */     updateStringValueForColumn(column, column.getCellRenderer());
/* 2233 */     if (this.ignoreAddColumn) return;
/* 2234 */     updateSortableAfterColumnChanged(column, (column instanceof TableColumnExt) ? ((TableColumnExt)column).isSortable() : true);
/* 2235 */     updateComparatorAfterColumnChanged(column, (column instanceof TableColumnExt) ? ((TableColumnExt)column).getComparator() : null);
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
/*      */   public TableColumn getColumn(int viewColumnIndex)
/*      */   {
/* 2265 */     return getColumnModel().getColumn(viewColumnIndex);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public List<TableColumn> getColumns()
/*      */   {
/* 2275 */     return Collections.list(getColumnModel().getColumns());
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
/*      */   public int getColumnMargin()
/*      */   {
/* 2291 */     return getColumnModel().getColumnMargin();
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
/*      */   public void setColumnMargin(int value)
/*      */   {
/* 2306 */     getColumnModel().setColumnMargin(value);
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
/*      */   public int getColumnCount(boolean includeHidden)
/*      */   {
/* 2327 */     if ((getColumnModel() instanceof TableColumnModelExt)) {
/* 2328 */       return ((TableColumnModelExt)getColumnModel()).getColumnCount(includeHidden);
/*      */     }
/*      */     
/* 2331 */     return getColumnCount();
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
/*      */   public List<TableColumn> getColumns(boolean includeHidden)
/*      */   {
/* 2356 */     if ((getColumnModel() instanceof TableColumnModelExt)) {
/* 2357 */       return ((TableColumnModelExt)getColumnModel()).getColumns(includeHidden);
/*      */     }
/*      */     
/* 2360 */     return getColumns();
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
/*      */   public TableColumnExt getColumnExt(Object identifier)
/*      */   {
/* 2379 */     if ((getColumnModel() instanceof TableColumnModelExt)) {
/* 2380 */       return ((TableColumnModelExt)getColumnModel()).getColumnExt(identifier);
/*      */     }
/*      */     
/*      */     try
/*      */     {
/* 2385 */       TableColumn column = getColumn(identifier);
/* 2386 */       if ((column instanceof TableColumnExt)) {
/* 2387 */         return (TableColumnExt)column;
/*      */       }
/*      */     }
/*      */     catch (Exception e) {}
/*      */     
/*      */ 
/* 2393 */     return null;
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
/*      */   public TableColumnExt getColumnExt(int viewColumnIndex)
/*      */   {
/* 2415 */     TableColumn column = getColumn(viewColumnIndex);
/* 2416 */     if ((column instanceof TableColumnExt)) {
/* 2417 */       return (TableColumnExt)column;
/*      */     }
/* 2419 */     return null;
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
/*      */   public void setColumnSequence(Object[] identifiers)
/*      */   {
/* 2439 */     List<TableColumn> columns = getColumns(true);
/* 2440 */     Map<Object, TableColumn> map = new HashMap();
/* 2441 */     for (Iterator<TableColumn> iter = columns.iterator(); iter.hasNext();)
/*      */     {
/* 2443 */       TableColumn column = (TableColumn)iter.next();
/* 2444 */       map.put(column.getIdentifier(), column);
/* 2445 */       getColumnModel().removeColumn(column);
/*      */     }
/* 2447 */     for (int i = 0; i < identifiers.length; i++) {
/* 2448 */       TableColumn column = (TableColumn)map.get(identifiers[i]);
/* 2449 */       if (column != null) {
/* 2450 */         getColumnModel().addColumn(column);
/* 2451 */         columns.remove(column);
/*      */       }
/*      */     }
/* 2454 */     for (Iterator<TableColumn> iter = columns.iterator(); iter.hasNext();) {
/* 2455 */       TableColumn column = (TableColumn)iter.next();
/* 2456 */       getColumnModel().addColumn(column);
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
/*      */   public void columnPropertyChange(PropertyChangeEvent event)
/*      */   {
/* 2469 */     if (event.getPropertyName().equals("editable")) {
/* 2470 */       updateEditingAfterColumnChanged((TableColumn)event.getSource(), ((Boolean)event.getNewValue()).booleanValue());
/*      */     }
/* 2472 */     else if (event.getPropertyName().equals("sortable")) {
/* 2473 */       updateSortableAfterColumnChanged((TableColumn)event.getSource(), ((Boolean)event.getNewValue()).booleanValue());
/*      */     }
/* 2475 */     else if (event.getPropertyName().equals("comparator")) {
/* 2476 */       updateComparatorAfterColumnChanged((TableColumn)event.getSource(), (Comparator)event.getNewValue());
/*      */     }
/* 2478 */     else if (event.getPropertyName().equals("cellRenderer")) {
/* 2479 */       updateStringValueForColumn((TableColumn)event.getSource(), (TableCellRenderer)event.getNewValue());
/*      */     }
/* 2481 */     else if (event.getPropertyName().startsWith("highlighter")) {
/* 2482 */       if (((event.getSource() instanceof TableColumnExt)) && (getRowCount() > 0))
/*      */       {
/* 2484 */         TableColumnExt column = (TableColumnExt)event.getSource();
/*      */         
/* 2486 */         Rectangle r = getCellRect(0, convertColumnIndexToView(column.getModelIndex()), true);
/*      */         
/* 2488 */         r.height = getHeight();
/* 2489 */         repaint(r);
/*      */       } else {
/* 2491 */         repaint();
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
/*      */   private void updateEditingAfterColumnChanged(TableColumn column, boolean editable)
/*      */   {
/* 2509 */     if (!isEditing())
/* 2510 */       return;
/* 2511 */     int viewIndex = convertColumnIndexToView(column.getModelIndex());
/* 2512 */     if ((viewIndex < 0) || (viewIndex != getEditingColumn()))
/* 2513 */       return;
/* 2514 */     getCellEditor().cancelCellEditing();
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
/*      */   private void updateSortableAfterColumnChanged(TableColumn column, boolean sortable)
/*      */   {
/* 2528 */     if (getControlsSorterProperties()) {
/* 2529 */       getSortController().setSortable(column.getModelIndex(), sortable);
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
/*      */   private void updateComparatorAfterColumnChanged(TableColumn column, Comparator<?> comparator)
/*      */   {
/* 2543 */     if (getControlsSorterProperties()) {
/* 2544 */       getSortController().setComparator(column.getModelIndex(), comparator);
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
/*      */   public final void createDefaultColumnsFromModel()
/*      */   {
/* 2571 */     if (getModel() == null) {
/* 2572 */       return;
/*      */     }
/* 2574 */     removeColumns();
/* 2575 */     createAndAddColumns();
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
/*      */   private void createAndAddColumns()
/*      */   {
/* 2594 */     for (int i = 0; i < getModel().getColumnCount(); i++)
/*      */     {
/*      */ 
/*      */ 
/* 2598 */       TableColumnExt tableColumn = getColumnFactory().createAndConfigureTableColumn(getModel(), i);
/*      */       
/* 2600 */       if (tableColumn != null) {
/* 2601 */         getColumnModel().addColumn(tableColumn);
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
/*      */   private void removeColumns()
/*      */   {
/* 2615 */     List<TableColumn> columns = getColumns(true);
/* 2616 */     for (Iterator<TableColumn> iter = columns.iterator(); iter.hasNext();) {
/* 2617 */       getColumnModel().removeColumn((TableColumn)iter.next());
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
/*      */   public ColumnFactory getColumnFactory()
/*      */   {
/* 2641 */     if (this.columnFactory == null) {
/* 2642 */       return ColumnFactory.getInstance();
/*      */     }
/*      */     
/* 2645 */     return this.columnFactory;
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
/*      */   public void setColumnFactory(ColumnFactory columnFactory)
/*      */   {
/* 2670 */     ColumnFactory old = getColumnFactory();
/* 2671 */     this.columnFactory = columnFactory;
/* 2672 */     firePropertyChange("columnFactory", old, getColumnFactory());
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
/*      */   public void packTable(int margin)
/*      */   {
/* 2687 */     for (int c = 0; c < getColumnCount(); c++) {
/* 2688 */       packColumn(c, margin, -1);
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
/*      */   public void packColumn(int column, int margin)
/*      */   {
/* 2701 */     packColumn(column, margin, -1);
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
/*      */   public void packColumn(int column, int margin, int max)
/*      */   {
/* 2719 */     getColumnFactory().packColumn(this, getColumnExt(column), margin, max);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public int getVisibleRowCount()
/*      */   {
/* 2730 */     return this.visibleRowCount;
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
/*      */   public void setVisibleRowCount(int visibleRowCount)
/*      */   {
/* 2749 */     if (visibleRowCount < 0) {
/* 2750 */       throw new IllegalArgumentException("visible row count must not be negative " + visibleRowCount);
/*      */     }
/* 2752 */     if (getVisibleRowCount() == visibleRowCount)
/* 2753 */       return;
/* 2754 */     int old = getVisibleRowCount();
/* 2755 */     this.visibleRowCount = visibleRowCount;
/* 2756 */     resetCalculatedScrollableSize(false);
/* 2757 */     firePropertyChange("visibleRowCount", old, getVisibleRowCount());
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public int getVisibleColumnCount()
/*      */   {
/* 2769 */     return this.visibleColumnCount;
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
/*      */   public void setVisibleColumnCount(int visibleColumnCount)
/*      */   {
/* 2786 */     if (getVisibleColumnCount() == visibleColumnCount)
/* 2787 */       return;
/* 2788 */     int old = getVisibleColumnCount();
/* 2789 */     this.visibleColumnCount = visibleColumnCount;
/* 2790 */     resetCalculatedScrollableSize(true);
/* 2791 */     firePropertyChange("visibleColumnCount", old, getVisibleColumnCount());
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   private void resetCalculatedScrollableSize(boolean isColumn)
/*      */   {
/* 2802 */     if (this.calculatedPrefScrollableViewportSize != null) {
/* 2803 */       if (isColumn) {
/* 2804 */         this.calculatedPrefScrollableViewportSize.width = -1;
/*      */       } else {
/* 2806 */         this.calculatedPrefScrollableViewportSize.height = -1;
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
/*      */   public void setPreferredScrollableViewportSize(Dimension size)
/*      */   {
/* 2827 */     super.setPreferredScrollableViewportSize(size);
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
/*      */   public Dimension getPreferredScrollableViewportSize()
/*      */   {
/* 2851 */     Dimension prefSize = super.getPreferredScrollableViewportSize();
/* 2852 */     if (prefSize != null) {
/* 2853 */       return new Dimension(prefSize);
/*      */     }
/* 2855 */     if (this.calculatedPrefScrollableViewportSize == null) {
/* 2856 */       this.calculatedPrefScrollableViewportSize = new Dimension();
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 2863 */     if (this.calculatedPrefScrollableViewportSize.width <= 0) {
/* 2864 */       this.calculatedPrefScrollableViewportSize.width = getColumnFactory().getPreferredScrollableViewportWidth(this);
/*      */     }
/*      */     
/*      */ 
/* 2868 */     if (this.calculatedPrefScrollableViewportSize.height <= 0) {
/* 2869 */       this.calculatedPrefScrollableViewportSize.height = (getVisibleRowCount() * getRowHeight());
/*      */     }
/*      */     
/* 2872 */     return new Dimension(this.calculatedPrefScrollableViewportSize);
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
/*      */   protected void initializeColumnWidths()
/*      */   {
/* 2892 */     for (TableColumn column : getColumns(true)) {
/* 2893 */       initializeColumnPreferredWidth(column);
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
/*      */   protected void initializeColumnPreferredWidth(TableColumn column)
/*      */   {
/* 2910 */     if ((column instanceof TableColumnExt)) {
/* 2911 */       getColumnFactory().configureColumnWidths(this, (TableColumnExt)column);
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
/*      */   public void scrollRowToVisible(int row)
/*      */   {
/* 2932 */     Rectangle cellRect = getCellRect(row, 0, false);
/* 2933 */     Rectangle visibleRect = getVisibleRect();
/* 2934 */     cellRect.x = visibleRect.x;
/* 2935 */     cellRect.width = visibleRect.width;
/* 2936 */     scrollRectToVisible(cellRect);
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
/*      */   public void scrollColumnToVisible(int column)
/*      */   {
/* 2954 */     Rectangle cellRect = getCellRect(0, column, false);
/* 2955 */     Rectangle visibleRect = getVisibleRect();
/* 2956 */     cellRect.y = visibleRect.y;
/* 2957 */     cellRect.height = visibleRect.height;
/* 2958 */     scrollRectToVisible(cellRect);
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
/*      */   public void scrollCellToVisible(int row, int column)
/*      */   {
/* 2977 */     Rectangle cellRect = getCellRect(row, column, false);
/* 2978 */     scrollRectToVisible(cellRect);
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
/*      */   public int getSelectionMode()
/*      */   {
/* 2991 */     return getSelectionModel().getSelectionMode();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected void doFind()
/*      */   {
/* 3001 */     SearchFactory.getInstance().showFindInput(this, getSearchable());
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
/*      */   public Searchable getSearchable()
/*      */   {
/* 3015 */     if (this.searchable == null) {
/* 3016 */       this.searchable = new TableSearchable(this);
/*      */     }
/* 3018 */     return this.searchable;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void setSearchable(Searchable searchable)
/*      */   {
/* 3029 */     this.searchable = searchable;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   protected ComponentAdapter getComponentAdapter()
/*      */   {
/* 3037 */     if (this.dataAdapter == null) {
/* 3038 */       this.dataAdapter = new TableAdapter(this);
/*      */     }
/* 3040 */     return this.dataAdapter;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected ComponentAdapter getComponentAdapter(int row, int column)
/*      */   {
/* 3051 */     ComponentAdapter adapter = getComponentAdapter();
/* 3052 */     adapter.row = row;
/* 3053 */     adapter.column = column;
/* 3054 */     return adapter;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   protected static class TableAdapter
/*      */     extends ComponentAdapter
/*      */   {
/*      */     private final JXTable table;
/*      */     
/*      */ 
/*      */     public TableAdapter(JXTable component)
/*      */     {
/* 3067 */       super();
/* 3068 */       this.table = component;
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     public JXTable getTable()
/*      */     {
/* 3077 */       return this.table;
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */     public String getColumnName(int columnIndex)
/*      */     {
/* 3085 */       TableColumn column = getColumnByModelIndex(columnIndex);
/* 3086 */       return column == null ? "" : column.getHeaderValue().toString();
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
/*      */     protected TableColumn getColumnByModelIndex(int modelColumn)
/*      */     {
/* 3099 */       if ((modelColumn < 0) || (modelColumn >= getColumnCount())) {
/* 3100 */         throw new IllegalArgumentException("invalid column index, must be positive and less than " + getColumnCount() + " was: " + modelColumn);
/*      */       }
/*      */       
/* 3103 */       List<TableColumn> columns = this.table.getColumns(true);
/* 3104 */       Iterator<TableColumn> iter = columns.iterator();
/* 3105 */       while (iter.hasNext()) {
/* 3106 */         TableColumn column = (TableColumn)iter.next();
/* 3107 */         if (column.getModelIndex() == modelColumn) {
/* 3108 */           return column;
/*      */         }
/*      */       }
/* 3111 */       return null;
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */     public Object getColumnIdentifierAt(int columnIndex)
/*      */     {
/* 3119 */       if ((columnIndex < 0) || (columnIndex >= getColumnCount())) {
/* 3120 */         throw new ArrayIndexOutOfBoundsException("invalid column index: " + columnIndex);
/*      */       }
/*      */       
/* 3123 */       TableColumn column = getColumnByModelIndex(columnIndex);
/* 3124 */       Object identifier = column != null ? column.getIdentifier() : null;
/* 3125 */       return identifier;
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */     public int getColumnIndex(Object identifier)
/*      */     {
/* 3133 */       TableColumn column = this.table.getColumnExt(identifier);
/* 3134 */       return column != null ? column.getModelIndex() : -1;
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */     public int getColumnCount()
/*      */     {
/* 3142 */       return this.table.getModel().getColumnCount();
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */     public int getRowCount()
/*      */     {
/* 3150 */       return this.table.getModel().getRowCount();
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */     public Object getValueAt(int row, int column)
/*      */     {
/* 3158 */       return this.table.getModel().getValueAt(row, column);
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */     public boolean isCellEditable(int row, int column)
/*      */     {
/* 3166 */       return this.table.getModel().isCellEditable(row, column);
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */     public boolean isTestable(int column)
/*      */     {
/* 3174 */       return getColumnByModelIndex(column) != null;
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
/* 3187 */       StringValue sv = this.table.getStringValueRegistry().getStringValue(row, column);
/* 3188 */       return sv.getString(getValueAt(row, column));
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */     public Rectangle getCellBounds()
/*      */     {
/* 3196 */       return this.table.getCellRect(this.row, this.column, false);
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */     public boolean isEditable()
/*      */     {
/* 3204 */       return this.table.isCellEditable(this.row, this.column);
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */     public boolean isSelected()
/*      */     {
/* 3212 */       return this.table.isCellSelected(this.row, this.column);
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */     public boolean hasFocus()
/*      */     {
/* 3220 */       boolean rowIsLead = this.table.getSelectionModel().getLeadSelectionIndex() == this.row;
/*      */       
/* 3222 */       boolean colIsLead = this.table.getColumnModel().getSelectionModel().getLeadSelectionIndex() == this.column;
/*      */       
/* 3224 */       return (this.table.isFocusOwner()) && (rowIsLead) && (colIsLead);
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */     public int convertColumnIndexToView(int columnIndex)
/*      */     {
/* 3232 */       return this.table.convertColumnIndexToView(columnIndex);
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */     public int convertColumnIndexToModel(int columnIndex)
/*      */     {
/* 3240 */       return this.table.convertColumnIndexToModel(columnIndex);
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */     public int convertRowIndexToView(int rowModelIndex)
/*      */     {
/* 3248 */       return this.table.convertRowIndexToView(rowModelIndex);
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */     public int convertRowIndexToModel(int rowViewIndex)
/*      */     {
/* 3256 */       return this.table.convertRowIndexToModel(rowViewIndex);
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
/*      */   public void setHighlighters(Highlighter... highlighters)
/*      */   {
/* 3285 */     Highlighter[] old = getHighlighters();
/* 3286 */     getCompoundHighlighter().setHighlighters(highlighters);
/* 3287 */     firePropertyChange("highlighters", old, getHighlighters());
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public Highlighter[] getHighlighters()
/*      */   {
/* 3298 */     return getCompoundHighlighter().getHighlighters();
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
/*      */   public void addHighlighter(Highlighter highlighter)
/*      */   {
/* 3313 */     Highlighter[] old = getHighlighters();
/* 3314 */     getCompoundHighlighter().addHighlighter(highlighter);
/* 3315 */     firePropertyChange("highlighters", old, getHighlighters());
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
/*      */   public void removeHighlighter(Highlighter highlighter)
/*      */   {
/* 3329 */     Highlighter[] old = getHighlighters();
/* 3330 */     getCompoundHighlighter().removeHighlighter(highlighter);
/* 3331 */     firePropertyChange("highlighters", old, getHighlighters());
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected CompoundHighlighter getCompoundHighlighter()
/*      */   {
/* 3341 */     if (this.compoundHighlighter == null) {
/* 3342 */       this.compoundHighlighter = new CompoundHighlighter(new Highlighter[0]);
/* 3343 */       this.compoundHighlighter.addChangeListener(getHighlighterChangeListener());
/*      */     }
/*      */     
/* 3346 */     return this.compoundHighlighter;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected ChangeListener getHighlighterChangeListener()
/*      */   {
/* 3357 */     if (this.highlighterChangeListener == null) {
/* 3358 */       this.highlighterChangeListener = createHighlighterChangeListener();
/*      */     }
/* 3360 */     return this.highlighterChangeListener;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected ChangeListener createHighlighterChangeListener()
/*      */   {
/* 3372 */     new ChangeListener() {
/*      */       public void stateChanged(ChangeEvent e) {
/* 3374 */         JXTable.this.repaint();
/*      */       }
/*      */     };
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
/*      */   protected StringValueRegistry getStringValueRegistry()
/*      */   {
/* 3389 */     if (this.stringValueRegistry == null) {
/* 3390 */       this.stringValueRegistry = createDefaultStringValueRegistry();
/*      */     }
/* 3392 */     return this.stringValueRegistry;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected StringValueRegistry createDefaultStringValueRegistry()
/*      */   {
/* 3401 */     return new StringValueRegistry();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   private void updateStringValueRegistryColumnClasses()
/*      */   {
/* 3410 */     getStringValueRegistry().setColumnClasses(null);
/* 3411 */     for (int i = 0; i < getModel().getColumnCount(); i++) {
/* 3412 */       getStringValueRegistry().setColumnClass(getModel().getColumnClass(i), i);
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
/*      */   private void updateStringValueForColumn(TableColumn tableColumn, TableCellRenderer renderer)
/*      */   {
/* 3426 */     getStringValueRegistry().setStringValue((renderer instanceof StringValue) ? (StringValue)renderer : null, tableColumn.getModelIndex());
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   private void initDefaultStringValues()
/*      */   {
/* 3434 */     for (Object clazz : this.defaultRenderersByColumnClass.keySet()) {
/* 3435 */       Object renderer = this.defaultRenderersByColumnClass.get(clazz);
/* 3436 */       if ((renderer instanceof StringValue)) {
/* 3437 */         getStringValueRegistry().setStringValue((StringValue)renderer, (Class)clazz);
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   private void initPerColumnStringValues()
/*      */   {
/* 3446 */     getStringValueRegistry().clearColumnStringValues();
/* 3447 */     for (TableColumn tableColumn : getColumns(true)) {
/* 3448 */       updateStringValueForColumn(tableColumn, tableColumn.getCellRenderer());
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
/*      */   public void setDefaultRenderer(Class<?> columnClass, TableCellRenderer renderer)
/*      */   {
/* 3461 */     super.setDefaultRenderer(columnClass, renderer);
/* 3462 */     getStringValueRegistry().setStringValue((renderer instanceof StringValue) ? (StringValue)renderer : null, columnClass);
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
/*      */   public String getStringAt(int row, int column)
/*      */   {
/* 3478 */     StringValue stringValue = getStringValueRegistry().getStringValue(convertRowIndexToModel(row), convertColumnIndexToModel(column));
/*      */     
/* 3480 */     return stringValue.getString(getValueAt(row, column));
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
/*      */   public TableCellRenderer getCellRenderer(int row, int column)
/*      */   {
/* 3501 */     TableCellRenderer renderer = super.getCellRenderer(row, column);
/* 3502 */     if (renderer == null) {
/* 3503 */       renderer = getDefaultRenderer(Object.class);
/*      */     }
/* 3505 */     return renderer;
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
/*      */   public Component prepareRenderer(TableCellRenderer renderer, int row, int column)
/*      */   {
/* 3545 */     Component stamp = super.prepareRenderer(renderer, row, column);
/*      */     
/* 3547 */     adjustComponentOrientation(stamp);
/*      */     
/* 3549 */     resetDefaultTableCellRendererColors(stamp, row, column);
/*      */     
/* 3551 */     ComponentAdapter adapter = getComponentAdapter(row, column);
/*      */     
/*      */ 
/* 3554 */     if (this.compoundHighlighter != null) {
/* 3555 */       stamp = this.compoundHighlighter.highlight(stamp, adapter);
/*      */     }
/*      */     
/* 3558 */     TableColumnExt columnExt = getColumnExt(column);
/*      */     
/* 3560 */     if (columnExt != null)
/*      */     {
/*      */ 
/*      */ 
/* 3564 */       for (Highlighter highlighter : columnExt.getHighlighters()) {
/* 3565 */         stamp = highlighter.highlight(stamp, adapter);
/*      */       }
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 3573 */     return stamp;
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
/*      */   protected void resetDefaultTableCellRendererColors(Component renderer, int row, int column)
/*      */   {
/* 3606 */     if (!Boolean.TRUE.equals(getClientProperty("useDTCRColorMemoryHack")))
/* 3607 */       return;
/* 3608 */     ComponentAdapter adapter = getComponentAdapter(row, column);
/* 3609 */     if (this.resetDefaultTableCellRendererHighlighter == null) {
/* 3610 */       this.resetDefaultTableCellRendererHighlighter = new ResetDTCRColorHighlighter();
/*      */     }
/*      */     
/* 3613 */     this.resetDefaultTableCellRendererHighlighter.highlight(renderer, adapter);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public Component prepareEditor(TableCellEditor editor, int row, int column)
/*      */   {
/* 3624 */     Component comp = super.prepareEditor(editor, row, column);
/*      */     
/*      */ 
/* 3627 */     if (comp != null) {
/* 3628 */       adjustComponentOrientation(comp);
/*      */     }
/* 3630 */     return comp;
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
/*      */   protected void adjustComponentOrientation(Component stamp)
/*      */   {
/* 3645 */     if (stamp.getComponentOrientation().equals(getComponentOrientation()))
/* 3646 */       return;
/* 3647 */     stamp.applyComponentOrientation(getComponentOrientation());
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
/*      */   @Deprecated
/*      */   public TableCellRenderer getNewDefaultRenderer(Class<?> columnClass)
/*      */   {
/* 3670 */     TableCellRenderer renderer = getDefaultRenderer(columnClass);
/* 3671 */     if (renderer != null) {
/*      */       try {
/* 3673 */         return (TableCellRenderer)renderer.getClass().newInstance();
/*      */       } catch (Exception e) {
/* 3675 */         LOG.fine("could not create renderer for " + columnClass);
/*      */       }
/*      */     }
/*      */     
/* 3679 */     return null;
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
/*      */   protected void createDefaultRenderers()
/*      */   {
/* 3697 */     super.createDefaultRenderers();
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 3707 */     Object[] dummies = { Integer.valueOf(1), Integer.valueOf(0), Integer.valueOf(2), Integer.valueOf(0), Integer.valueOf(3), Integer.valueOf(0), Integer.valueOf(4), Integer.valueOf(0), Integer.valueOf(5), Integer.valueOf(0), Integer.valueOf(6), Integer.valueOf(0), Integer.valueOf(7), Integer.valueOf(0), Integer.valueOf(8), Integer.valueOf(0), Integer.valueOf(9), Integer.valueOf(0), Integer.valueOf(10), Integer.valueOf(0) };
/*      */     
/* 3709 */     this.defaultRenderersByColumnClass = new UIDefaults(dummies);
/* 3710 */     this.defaultRenderersByColumnClass.clear();
/*      */     
/* 3712 */     setDefaultRenderer(Object.class, new DefaultTableRenderer());
/* 3713 */     setDefaultRenderer(Number.class, new DefaultTableRenderer(StringValues.NUMBER_TO_STRING, 4));
/*      */     
/* 3715 */     setDefaultRenderer(Date.class, new DefaultTableRenderer(StringValues.DATE_TO_STRING));
/*      */     
/*      */ 
/* 3718 */     TableCellRenderer renderer = new DefaultTableRenderer(new MappedValue(StringValues.EMPTY, IconValues.ICON), 0);
/*      */     
/* 3720 */     setDefaultRenderer(Icon.class, renderer);
/* 3721 */     setDefaultRenderer(ImageIcon.class, renderer);
/*      */     
/* 3723 */     setDefaultRenderer(Boolean.class, new DefaultTableRenderer(new CheckBoxProvider()));
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   private void setLazyValue(Hashtable h, Class c, String s)
/*      */   {
/* 3731 */     h.put(c, new UIDefaults.ProxyLazyValue(s));
/*      */   }
/*      */   
/*      */   private void setLazyEditor(Class<?> c, String s)
/*      */   {
/* 3736 */     setLazyValue(this.defaultEditorsByColumnClass, c, s);
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
/*      */   protected void createDefaultEditors()
/*      */   {
/* 3751 */     Object[] dummies = { Integer.valueOf(1), Integer.valueOf(0), Integer.valueOf(2), Integer.valueOf(0), Integer.valueOf(3), Integer.valueOf(0), Integer.valueOf(4), Integer.valueOf(0), Integer.valueOf(5), Integer.valueOf(0), Integer.valueOf(6), Integer.valueOf(0), Integer.valueOf(7), Integer.valueOf(0), Integer.valueOf(8), Integer.valueOf(0), Integer.valueOf(9), Integer.valueOf(0), Integer.valueOf(10), Integer.valueOf(0) };
/*      */     
/*      */ 
/*      */ 
/* 3755 */     this.defaultEditorsByColumnClass = new UIDefaults(dummies);
/* 3756 */     this.defaultEditorsByColumnClass.clear();
/*      */     
/*      */ 
/*      */ 
/* 3760 */     setLazyEditor(Object.class, "org.jdesktop.swingx.JXTable$GenericEditor");
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 3770 */     this.defaultEditorsByColumnClass.put(Number.class, new NumberEditorExt(true));
/*      */     
/*      */ 
/* 3773 */     setLazyEditor(Boolean.class, "org.jdesktop.swingx.JXTable$BooleanEditor");
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
/*      */   public static class GenericEditor
/*      */     extends DefaultCellEditor
/*      */   {
/* 3790 */     Class<?>[] argTypes = { String.class };
/*      */     
/*      */     Constructor<?> constructor;
/*      */     Object value;
/*      */     
/*      */     public GenericEditor()
/*      */     {
/* 3797 */       this(new JTextField());
/*      */     }
/*      */     
/*      */     public GenericEditor(JTextField textField) {
/* 3801 */       super();
/* 3802 */       getComponent().setName("Table.editor");
/*      */     }
/*      */     
/*      */     public boolean stopCellEditing()
/*      */     {
/* 3807 */       String s = (String)super.getCellEditorValue();
/*      */       
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 3814 */       if ("".equals(s)) {
/* 3815 */         if (this.constructor.getDeclaringClass() == String.class) {
/* 3816 */           this.value = s;
/*      */         }
/* 3818 */         super.stopCellEditing();
/*      */       }
/*      */       try
/*      */       {
/* 3822 */         this.value = this.constructor.newInstance(new Object[] { s });
/*      */       } catch (Exception e) {
/* 3824 */         ((JComponent)getComponent()).setBorder(new LineBorder(Color.red));
/*      */         
/* 3826 */         return false;
/*      */       }
/* 3828 */       return super.stopCellEditing();
/*      */     }
/*      */     
/*      */ 
/*      */     public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column)
/*      */     {
/* 3834 */       this.value = null;
/* 3835 */       ((JComponent)getComponent()).setBorder(new LineBorder(Color.black));
/*      */       try
/*      */       {
/* 3838 */         Class<?> type = table.getColumnClass(column);
/*      */         
/*      */ 
/*      */ 
/*      */ 
/* 3843 */         if (type == Object.class) {
/* 3844 */           type = String.class;
/*      */         }
/* 3846 */         this.constructor = type.getConstructor(this.argTypes);
/*      */       } catch (Exception e) {
/* 3848 */         return null;
/*      */       }
/* 3850 */       return super.getTableCellEditorComponent(table, value, isSelected, row, column);
/*      */     }
/*      */     
/*      */ 
/*      */     public Object getCellEditorValue()
/*      */     {
/* 3856 */       return this.value;
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
/*      */   public static class NumberEditor
/*      */     extends JXTable.GenericEditor
/*      */   {
/*      */     public NumberEditor()
/*      */     {
/* 3872 */       ((JTextField)getComponent()).setHorizontalAlignment(4);
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */   public static class BooleanEditor
/*      */     extends DefaultCellEditor
/*      */   {
/*      */     public BooleanEditor()
/*      */     {
/* 3882 */       super();
/* 3883 */       JCheckBox checkBox = (JCheckBox)getComponent();
/* 3884 */       checkBox.setHorizontalAlignment(0);
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
/*      */   public boolean isEditable()
/*      */   {
/* 3897 */     return this.editable;
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
/*      */   public void setEditable(boolean editable)
/*      */   {
/* 3916 */     boolean old = isEditable();
/* 3917 */     this.editable = editable;
/* 3918 */     firePropertyChange("editable", old, isEditable());
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public boolean isTerminateEditOnFocusLost()
/*      */   {
/* 3930 */     return Boolean.TRUE.equals(getClientProperty("terminateEditOnFocusLost"));
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
/*      */   public void setTerminateEditOnFocusLost(boolean terminate)
/*      */   {
/* 3952 */     putClientProperty("terminateEditOnFocusLost", Boolean.valueOf(terminate));
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public boolean isAutoStartEditOnKeyStroke()
/*      */   {
/* 3963 */     return !Boolean.FALSE.equals(getClientProperty("JTable.autoStartsEdit"));
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
/*      */   public void setAutoStartEditOnKeyStroke(boolean autoStart)
/*      */   {
/* 3980 */     boolean old = isAutoStartEditOnKeyStroke();
/*      */     
/*      */ 
/*      */ 
/*      */ 
/* 3985 */     putClientProperty("JTable.autoStartsEdit", Boolean.valueOf(autoStart));
/* 3986 */     firePropertyChange("autoStartEditOnKeyStroke", old, isAutoStartEditOnKeyStroke());
/*      */   }
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
/* 3998 */     boolean started = super.editCellAt(row, column, e);
/* 3999 */     if (started) {
/* 4000 */       hackEditorRemover();
/*      */     }
/* 4002 */     return started;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public void removeEditor()
/*      */   {
/* 4010 */     boolean isFocusOwnerInTheTable = isFocusOwnerDescending();
/*      */     
/* 4012 */     super.removeEditor();
/* 4013 */     if (isFocusOwnerInTheTable) {
/* 4014 */       requestFocusInWindow();
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   private boolean isFocusOwnerDescending()
/*      */   {
/* 4026 */     if (!isEditing())
/* 4027 */       return false;
/* 4028 */     Component focusOwner = KeyboardFocusManager.getCurrentKeyboardFocusManager().getFocusOwner();
/*      */     
/*      */ 
/* 4031 */     if (focusOwner == null)
/* 4032 */       return false;
/* 4033 */     if (SwingXUtilities.isDescendingFrom(focusOwner, this)) {
/* 4034 */       return true;
/*      */     }
/* 4036 */     Component permanent = KeyboardFocusManager.getCurrentKeyboardFocusManager().getPermanentFocusOwner();
/*      */     
/* 4038 */     return SwingXUtilities.isDescendingFrom(permanent, this);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   private void hackEditorRemover()
/*      */   {
/* 4048 */     KeyboardFocusManager manager = KeyboardFocusManager.getCurrentKeyboardFocusManager();
/*      */     
/* 4050 */     PropertyChangeListener[] listeners = manager.getPropertyChangeListeners("permanentFocusOwner");
/*      */     
/* 4052 */     for (int i = listeners.length - 1; i >= 0; i--) {
/* 4053 */       if (listeners[i].getClass().getName().startsWith("javax.swing.JTable"))
/*      */       {
/* 4055 */         manager.removePropertyChangeListener("permanentFocusOwner", listeners[i]);
/*      */         
/* 4057 */         break;
/*      */       }
/*      */     }
/* 4060 */     if (this.editorRemover == null) {
/* 4061 */       this.editorRemover = new CellEditorRemover();
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void removeNotify()
/*      */   {
/* 4073 */     if (this.editorRemover != null) {
/* 4074 */       this.editorRemover.uninstall();
/* 4075 */       this.editorRemover = null;
/*      */     }
/* 4077 */     super.removeNotify();
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
/*      */   public boolean isFocusCycleRoot()
/*      */   {
/* 4091 */     if (isEditingFocusCycleRoot()) {
/* 4092 */       return true;
/*      */     }
/* 4094 */     return super.isFocusCycleRoot();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void transferFocus()
/*      */   {
/* 4106 */     if ((isEditingFocusCycleRoot()) && (!getCellEditor().stopCellEditing()))
/* 4107 */       return;
/* 4108 */     super.transferFocus();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void transferFocusBackward()
/*      */   {
/* 4120 */     if ((isEditingFocusCycleRoot()) && (!getCellEditor().stopCellEditing()))
/* 4121 */       return;
/* 4122 */     super.transferFocusBackward();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   private boolean isEditingFocusCycleRoot()
/*      */   {
/* 4131 */     return (isEditing()) && (isTerminateEditOnFocusLost());
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   class CellEditorRemover
/*      */     implements PropertyChangeListener
/*      */   {
/*      */     KeyboardFocusManager focusManager;
/*      */     
/*      */ 
/*      */     public CellEditorRemover()
/*      */     {
/* 4144 */       install();
/*      */     }
/*      */     
/*      */     private void install() {
/* 4148 */       this.focusManager = KeyboardFocusManager.getCurrentKeyboardFocusManager();
/*      */       
/* 4150 */       this.focusManager.addPropertyChangeListener("permanentFocusOwner", this);
/* 4151 */       this.focusManager.addPropertyChangeListener("managingFocus", this);
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */     public void uninstall()
/*      */     {
/* 4159 */       this.focusManager.removePropertyChangeListener("permanentFocusOwner", this);
/*      */       
/* 4161 */       this.focusManager.removePropertyChangeListener("managingFocus", this);
/* 4162 */       this.focusManager = null;
/*      */     }
/*      */     
/*      */     public void propertyChange(PropertyChangeEvent ev) {
/* 4166 */       if (ev == null)
/* 4167 */         return;
/* 4168 */       if ("permanentFocusOwner".equals(ev.getPropertyName())) {
/* 4169 */         permanentFocusOwnerChange();
/* 4170 */       } else if (!"managingFocus".equals(ev.getPropertyName())) {}
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     private void permanentFocusOwnerChange()
/*      */     {
/* 4179 */       if ((!JXTable.this.isEditing()) || (!JXTable.this.isTerminateEditOnFocusLost())) {
/* 4180 */         return;
/*      */       }
/*      */       
/* 4183 */       Component c = this.focusManager.getPermanentFocusOwner();
/* 4184 */       while (c != null)
/*      */       {
/* 4186 */         if ((c instanceof JPopupMenu)) {
/* 4187 */           c = ((JPopupMenu)c).getInvoker();
/*      */         } else {
/* 4189 */           if (c == JXTable.this)
/*      */           {
/* 4191 */             return; }
/* 4192 */           if (!(c instanceof JPopupMenu))
/*      */           {
/*      */ 
/* 4195 */             if (((c instanceof Window)) || (((c instanceof Applet)) && (c.getParent() == null)))
/*      */             {
/* 4197 */               if ((c != SwingUtilities.getRoot(JXTable.this)) || 
/* 4198 */                 (JXTable.this.getCellEditor().stopCellEditing())) break;
/* 4199 */               JXTable.this.getCellEditor().cancelCellEditing(); break;
/*      */             }
/*      */           }
/*      */           
/*      */ 
/* 4204 */           c = c.getParent();
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
/*      */   public void updateUI()
/*      */   {
/* 4222 */     super.updateUI();
/* 4223 */     updateColumnControlUI();
/* 4224 */     Enumeration<?> defaultEditors = this.defaultEditorsByColumnClass.elements();
/* 4225 */     while (defaultEditors.hasMoreElements()) {
/* 4226 */       updateEditorUI(defaultEditors.nextElement());
/*      */     }
/*      */     
/* 4229 */     Enumeration<?> defaultRenderers = this.defaultRenderersByColumnClass.elements();
/* 4230 */     while (defaultRenderers.hasMoreElements()) {
/* 4231 */       updateRendererUI(defaultRenderers.nextElement());
/*      */     }
/* 4233 */     for (TableColumn column : getColumns(true)) {
/* 4234 */       updateColumnUI(column);
/*      */     }
/* 4236 */     updateRowHeightUI(true);
/* 4237 */     updateHighlighterUI();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   protected void updateColumnControlUI()
/*      */   {
/* 4244 */     if ((this.columnControlButton != null) && (this.columnControlButton.getParent() == null))
/*      */     {
/* 4246 */       SwingUtilities.updateComponentTreeUI(this.columnControlButton);
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   private void updateEditorUI(Object maybeEditor)
/*      */   {
/* 4258 */     if (!(maybeEditor instanceof TableCellEditor)) {
/* 4259 */       return;
/*      */     }
/* 4261 */     if (((maybeEditor instanceof JComponent)) || ((maybeEditor instanceof DefaultCellEditor)))
/*      */     {
/* 4263 */       return;
/*      */     }
/*      */     try {
/* 4266 */       Component comp = ((TableCellEditor)maybeEditor).getTableCellEditorComponent(this, null, false, -1, -1);
/*      */       
/* 4268 */       if (comp != null) {
/* 4269 */         SwingUtilities.updateComponentTreeUI(comp);
/*      */       }
/*      */     }
/*      */     catch (Exception e) {}
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   private void updateRendererUI(Object maybeRenderer)
/*      */   {
/* 4284 */     if (!(maybeRenderer instanceof TableCellRenderer)) {
/* 4285 */       return;
/*      */     }
/* 4287 */     if ((maybeRenderer instanceof JComponent))
/* 4288 */       return;
/* 4289 */     Component comp = null;
/* 4290 */     if ((maybeRenderer instanceof AbstractRenderer)) {
/* 4291 */       comp = ((AbstractRenderer)maybeRenderer).getComponentProvider().getRendererComponent(null);
/*      */     }
/*      */     else {
/*      */       try
/*      */       {
/* 4296 */         comp = ((TableCellRenderer)maybeRenderer).getTableCellRendererComponent(this, null, false, false, -1, -1);
/*      */       }
/*      */       catch (Exception e) {}
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/* 4304 */     if (comp != null) {
/* 4305 */       SwingUtilities.updateComponentTreeUI(comp);
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected void updateColumnUI(TableColumn column)
/*      */   {
/* 4317 */     if ((column instanceof UIDependent)) {
/* 4318 */       ((UIDependent)column).updateUI();
/*      */     } else {
/* 4320 */       updateEditorUI(column.getCellEditor());
/* 4321 */       updateRendererUI(column.getCellRenderer());
/* 4322 */       updateRendererUI(column.getHeaderRenderer());
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected void updateHighlighterUI()
/*      */   {
/* 4332 */     if (this.compoundHighlighter == null)
/* 4333 */       return;
/* 4334 */     this.compoundHighlighter.updateUI();
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
/*      */   protected void updateRowHeightUI(boolean respectRowSetFlag)
/*      */   {
/* 4355 */     if ((respectRowSetFlag) && (this.isXTableRowHeightSet))
/* 4356 */       return;
/* 4357 */     int uiHeight = UIManager.getInt("JXTable.rowHeight");
/* 4358 */     if (uiHeight > 0) {
/* 4359 */       setRowHeight(uiHeight);
/*      */     } else {
/* 4361 */       int fontBasedHeight = getFontMetrics(getFont()).getHeight() + 2;
/* 4362 */       int magicMinimum = 18;
/* 4363 */       setRowHeight(Math.max(fontBasedHeight, magicMinimum));
/*      */     }
/* 4365 */     this.isXTableRowHeightSet = false;
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
/*      */   public void setShowGrid(boolean showHorizontalLines, boolean showVerticalLines)
/*      */   {
/* 4383 */     int defaultRowMargin = showHorizontalLines ? 1 : 0;
/* 4384 */     setRowMargin(defaultRowMargin);
/* 4385 */     setShowHorizontalLines(showHorizontalLines);
/* 4386 */     int defaultColumnMargin = showVerticalLines ? 1 : 0;
/* 4387 */     setColumnMargin(defaultColumnMargin);
/* 4388 */     setShowVerticalLines(showVerticalLines);
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
/*      */   public void setShowGrid(boolean showGrid)
/*      */   {
/* 4406 */     super.setShowGrid(showGrid);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void setRowHeight(int rowHeight)
/*      */   {
/* 4418 */     super.setRowHeight(rowHeight);
/* 4419 */     if (rowHeight > 0) {
/* 4420 */       this.isXTableRowHeightSet = true;
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
/*      */   @Deprecated
/*      */   public boolean isRowHeightEnabled()
/*      */   {
/* 4453 */     return true;
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
/*      */   protected void adminSetRowHeight(int rowHeight)
/*      */   {
/* 4467 */     boolean heightSet = this.isXTableRowHeightSet;
/* 4468 */     setRowHeight(rowHeight);
/* 4469 */     this.isXTableRowHeightSet = heightSet;
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
/*      */   public int rowAtPoint(Point point)
/*      */   {
/* 4482 */     if (point.y < 0)
/* 4483 */       return -1;
/* 4484 */     return super.rowAtPoint(point);
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
/*      */   protected JTableHeader createDefaultTableHeader()
/*      */   {
/* 4498 */     return new JXTableHeader(this.columnModel);
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
/*      */   protected TableColumnModel createDefaultColumnModel()
/*      */   {
/* 4512 */     return new DefaultTableColumnModelExt();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void setSelectionBackground(Color selectionBackground)
/*      */   {
/* 4522 */     Color old = getSelectionBackground();
/* 4523 */     this.selectionBackground = selectionBackground;
/* 4524 */     firePropertyChange("selectionBackground", old, getSelectionBackground());
/* 4525 */     repaint();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void setSelectionForeground(Color selectionForeground)
/*      */   {
/* 4535 */     Color old = getSelectionForeground();
/* 4536 */     this.selectionForeground = selectionForeground;
/* 4537 */     firePropertyChange("selectionForeground", old, getSelectionForeground());
/* 4538 */     repaint();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void setGridColor(Color gridColor)
/*      */   {
/* 4548 */     Color old = getGridColor();
/* 4549 */     this.gridColor = gridColor;
/* 4550 */     firePropertyChange("gridColor", old, getGridColor());
/* 4551 */     repaint();
/*      */   }
/*      */   
/*      */   @Deprecated
/*      */   public void setRowHeightEnabled(boolean enabled) {}
/*      */ }


/* Location:              E:\java\java学习\hutubill\lib\all.jar!\org\jdesktop\swingx\JXTable.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */
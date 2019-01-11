/*      */ package org.jdesktop.swingx;
/*      */ 
/*      */ import java.awt.Component;
/*      */ import java.awt.Rectangle;
/*      */ import java.awt.event.ActionEvent;
/*      */ import java.util.Arrays;
/*      */ import java.util.Comparator;
/*      */ import java.util.Vector;
/*      */ import java.util.logging.Logger;
/*      */ import java.util.regex.Pattern;
/*      */ import javax.swing.Action;
/*      */ import javax.swing.ActionMap;
/*      */ import javax.swing.InputMap;
/*      */ import javax.swing.JList;
/*      */ import javax.swing.KeyStroke;
/*      */ import javax.swing.ListCellRenderer;
/*      */ import javax.swing.ListModel;
/*      */ import javax.swing.RowFilter;
/*      */ import javax.swing.RowSorter;
/*      */ import javax.swing.SortOrder;
/*      */ import javax.swing.SwingUtilities;
/*      */ import javax.swing.event.ChangeEvent;
/*      */ import javax.swing.event.ChangeListener;
/*      */ import javax.swing.plaf.ListUI;
/*      */ import javax.swing.text.Position.Bias;
/*      */ import org.jdesktop.swingx.decorator.ComponentAdapter;
/*      */ import org.jdesktop.swingx.decorator.CompoundHighlighter;
/*      */ import org.jdesktop.swingx.decorator.Highlighter;
/*      */ import org.jdesktop.swingx.plaf.LookAndFeelAddons;
/*      */ import org.jdesktop.swingx.plaf.XListAddon;
/*      */ import org.jdesktop.swingx.plaf.basic.core.BasicXListUI;
/*      */ import org.jdesktop.swingx.renderer.AbstractRenderer;
/*      */ import org.jdesktop.swingx.renderer.ComponentProvider;
/*      */ import org.jdesktop.swingx.renderer.DefaultListRenderer;
/*      */ import org.jdesktop.swingx.renderer.StringValue;
/*      */ import org.jdesktop.swingx.rollover.ListRolloverController;
/*      */ import org.jdesktop.swingx.rollover.ListRolloverProducer;
/*      */ import org.jdesktop.swingx.rollover.RolloverProducer;
/*      */ import org.jdesktop.swingx.rollover.RolloverRenderer;
/*      */ import org.jdesktop.swingx.search.ListSearchable;
/*      */ import org.jdesktop.swingx.search.SearchFactory;
/*      */ import org.jdesktop.swingx.search.Searchable;
/*      */ import org.jdesktop.swingx.sort.DefaultSortController;
/*      */ import org.jdesktop.swingx.sort.ListSortController;
/*      */ import org.jdesktop.swingx.sort.SortController;
/*      */ import org.jdesktop.swingx.sort.StringValueRegistry;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ public class JXList
/*      */   extends JList
/*      */ {
/*  180 */   private static final Logger LOG = Logger.getLogger(JXList.class.getName());
/*      */   public static final String uiClassID = "XListUI";
/*      */   public static final String EXECUTE_BUTTON_ACTIONCOMMAND = "executeButtonAction";
/*      */   protected CompoundHighlighter compoundHighlighter;
/*      */   private ChangeListener highlighterChangeListener;
/*      */   protected ComponentAdapter dataAdapter;
/*      */   private RolloverProducer rolloverProducer;
/*      */   private ListRolloverController<JXList> linkController;
/*      */   private DelegatingRenderer delegatingRenderer;
/*      */   
/*      */   static
/*      */   {
/*  192 */     LookAndFeelAddons.contribute(new XListAddon());
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   private Searchable searchable;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   private Comparator<?> comparator;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   private boolean autoCreateRowSorter;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   private RowSorter<? extends ListModel> rowSorter;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   private boolean sortable;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   private boolean sortsOnUpdates;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   private StringValueRegistry stringValueRegistry;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   private SortOrder[] sortOrderCycle;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public JXList()
/*      */   {
/*  245 */     this(false);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public JXList(ListModel dataModel)
/*      */   {
/*  257 */     this(dataModel, false);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public JXList(Object[] listData)
/*      */   {
/*  269 */     this(listData, false);
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
/*      */   public JXList(Vector<?> listData)
/*      */   {
/*  282 */     this(listData, false);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public JXList(boolean autoCreateRowSorter)
/*      */   {
/*  294 */     init(autoCreateRowSorter);
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
/*      */   public JXList(ListModel dataModel, boolean autoCreateRowSorter)
/*      */   {
/*  308 */     super(dataModel);
/*  309 */     init(autoCreateRowSorter);
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
/*      */   public JXList(Object[] listData, boolean autoCreateRowSorter)
/*      */   {
/*  323 */     super(listData);
/*  324 */     if (listData == null)
/*  325 */       throw new IllegalArgumentException("listData must not be null");
/*  326 */     init(autoCreateRowSorter);
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
/*      */   public JXList(Vector<?> listData, boolean autoCreateRowSorter)
/*      */   {
/*  340 */     super(listData);
/*  341 */     if (listData == null)
/*  342 */       throw new IllegalArgumentException("listData must not be null");
/*  343 */     init(autoCreateRowSorter);
/*      */   }
/*      */   
/*      */   private void init(boolean autoCreateRowSorter)
/*      */   {
/*  348 */     this.sortOrderCycle = DefaultSortController.getDefaultSortOrderCycle();
/*  349 */     setSortable(true);
/*  350 */     setSortsOnUpdates(true);
/*  351 */     setAutoCreateRowSorter(autoCreateRowSorter);
/*  352 */     Action findAction = createFindAction();
/*  353 */     getActionMap().put("find", findAction);
/*      */     
/*  355 */     KeyStroke findStroke = SearchFactory.getInstance().getSearchAccelerator();
/*  356 */     getInputMap(1).put(findStroke, "find");
/*      */   }
/*      */   
/*      */   private Action createFindAction() {
/*  360 */     new UIAction("find") {
/*      */       public void actionPerformed(ActionEvent e) {
/*  362 */         JXList.this.doFind();
/*      */       }
/*      */     };
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   protected void doFind()
/*      */   {
/*  372 */     SearchFactory.getInstance().showFindInput(this, getSearchable());
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
/*      */   public Searchable getSearchable()
/*      */   {
/*  385 */     if (this.searchable == null) {
/*  386 */       this.searchable = new ListSearchable(this);
/*      */     }
/*  388 */     return this.searchable;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void setSearchable(Searchable searchable)
/*      */   {
/*  400 */     this.searchable = searchable;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public int getNextMatch(String prefix, int startIndex, Position.Bias bias)
/*      */   {
/*  411 */     Pattern pattern = Pattern.compile("^" + prefix, 2);
/*  412 */     return getSearchable().search(pattern, startIndex, bias == Position.Bias.Backward);
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
/*      */   public void setRolloverEnabled(boolean rolloverEnabled)
/*      */   {
/*  436 */     boolean old = isRolloverEnabled();
/*  437 */     if (rolloverEnabled == old)
/*  438 */       return;
/*  439 */     if (rolloverEnabled) {
/*  440 */       this.rolloverProducer = createRolloverProducer();
/*  441 */       this.rolloverProducer.install(this);
/*  442 */       getLinkController().install(this);
/*      */     } else {
/*  444 */       this.rolloverProducer.release(this);
/*  445 */       this.rolloverProducer = null;
/*  446 */       getLinkController().release();
/*      */     }
/*  448 */     firePropertyChange("rolloverEnabled", old, isRolloverEnabled());
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
/*  459 */     return this.rolloverProducer != null;
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
/*      */   protected ListRolloverController<JXList> getLinkController()
/*      */   {
/*  476 */     if (this.linkController == null) {
/*  477 */       this.linkController = createLinkController();
/*      */     }
/*  479 */     return this.linkController;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected ListRolloverController<JXList> createLinkController()
/*      */   {
/*  491 */     return new ListRolloverController();
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
/*      */   protected RolloverProducer createRolloverProducer()
/*      */   {
/*  504 */     return new ListRolloverProducer();
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
/*      */   public boolean getAutoCreateRowSorter()
/*      */   {
/*  519 */     return this.autoCreateRowSorter;
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
/*      */   public void setAutoCreateRowSorter(boolean autoCreateRowSorter)
/*      */   {
/*  542 */     if (getAutoCreateRowSorter() == autoCreateRowSorter) return;
/*  543 */     boolean oldValue = getAutoCreateRowSorter();
/*  544 */     this.autoCreateRowSorter = autoCreateRowSorter;
/*  545 */     if (autoCreateRowSorter) {
/*  546 */       setRowSorter(createDefaultRowSorter());
/*      */     }
/*  548 */     firePropertyChange("autoCreateRowSorter", oldValue, getAutoCreateRowSorter());
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
/*      */   protected RowSorter<? extends ListModel> createDefaultRowSorter()
/*      */   {
/*  562 */     return new ListSortController(getModel());
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public RowSorter<? extends ListModel> getRowSorter()
/*      */   {
/*  571 */     return this.rowSorter;
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
/*      */   public void setRowSorter(RowSorter<? extends ListModel> sorter)
/*      */   {
/*  587 */     RowSorter<? extends ListModel> oldRowSorter = getRowSorter();
/*  588 */     this.rowSorter = sorter;
/*  589 */     configureSorterProperties();
/*  590 */     firePropertyChange("rowSorter", oldRowSorter, sorter);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected void configureSorterProperties()
/*      */   {
/*  599 */     if (!getControlsSorterProperties()) { return;
/*      */     }
/*  601 */     getSortController().setSortable(this.sortable);
/*  602 */     getSortController().setSortsOnUpdates(this.sortsOnUpdates);
/*  603 */     getSortController().setComparator(0, this.comparator);
/*  604 */     getSortController().setSortOrderCycle(getSortOrderCycle());
/*  605 */     getSortController().setStringValueProvider(getStringValueRegistry());
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
/*      */   public void setSortable(boolean sortable)
/*      */   {
/*  622 */     boolean old = isSortable();
/*  623 */     this.sortable = sortable;
/*  624 */     if (getControlsSorterProperties()) {
/*  625 */       getSortController().setSortable(sortable);
/*      */     }
/*  627 */     firePropertyChange("sortable", old, isSortable());
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public boolean isSortable()
/*      */   {
/*  636 */     return this.sortable;
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
/*      */   public void setSortsOnUpdates(boolean sortsOnUpdates)
/*      */   {
/*  649 */     boolean old = getSortsOnUpdates();
/*  650 */     this.sortsOnUpdates = sortsOnUpdates;
/*  651 */     if (getControlsSorterProperties()) {
/*  652 */       getSortController().setSortsOnUpdates(sortsOnUpdates);
/*      */     }
/*  654 */     firePropertyChange("sortsOnUpdates", old, getSortsOnUpdates());
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public boolean getSortsOnUpdates()
/*      */   {
/*  664 */     return this.sortsOnUpdates;
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
/*  677 */     SortOrder[] old = getSortOrderCycle();
/*  678 */     if (getControlsSorterProperties()) {
/*  679 */       getSortController().setSortOrderCycle(cycle);
/*      */     }
/*  681 */     this.sortOrderCycle = ((SortOrder[])Arrays.copyOf(cycle, cycle.length));
/*  682 */     firePropertyChange("sortOrderCycle", old, getSortOrderCycle());
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public SortOrder[] getSortOrderCycle()
/*      */   {
/*  692 */     return (SortOrder[])Arrays.copyOf(this.sortOrderCycle, this.sortOrderCycle.length);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public Comparator<?> getComparator()
/*      */   {
/*  701 */     return this.comparator;
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
/*      */   public void setComparator(Comparator<?> comparator)
/*      */   {
/*  716 */     Comparator<?> old = getComparator();
/*  717 */     this.comparator = comparator;
/*  718 */     updateSortAfterComparatorChange();
/*  719 */     firePropertyChange("comparator", old, getComparator());
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   protected void updateSortAfterComparatorChange()
/*      */   {
/*  727 */     if (getControlsSorterProperties()) {
/*  728 */       getSortController().setComparator(0, getComparator());
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
/*      */   public void setRowFilter(RowFilter<? super ListModel, ? super Integer> filter)
/*      */   {
/*  743 */     if (hasSortController()) {
/*  744 */       getSortController().setRowFilter(filter);
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
/*      */   public RowFilter<?, ?> getRowFilter()
/*      */   {
/*  758 */     return hasSortController() ? getSortController().getRowFilter() : null;
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
/*  769 */     if (hasSortController()) {
/*  770 */       getSortController().resetSortOrders();
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
/*      */   public void toggleSortOrder()
/*      */   {
/*  787 */     if (hasSortController()) {
/*  788 */       getSortController().toggleSortOrder(0);
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void setSortOrder(SortOrder sortOrder)
/*      */   {
/*  799 */     if (hasSortController()) {
/*  800 */       getSortController().setSortOrder(0, sortOrder);
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public SortOrder getSortOrder()
/*      */   {
/*  811 */     if (hasSortController())
/*  812 */       return getSortController().getSortOrder(0);
/*  813 */     return SortOrder.UNSORTED;
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
/*      */   protected SortController<? extends ListModel> getSortController()
/*      */   {
/*  829 */     if (hasSortController())
/*      */     {
/*      */ 
/*  832 */       return (SortController)getRowSorter();
/*      */     }
/*  834 */     return null;
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
/*  847 */     return getRowSorter() instanceof SortController;
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
/*  866 */     return (hasSortController()) && (getAutoCreateRowSorter());
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
/*      */   public Object getElementAt(int viewIndex)
/*      */   {
/*  882 */     return getModel().getElementAt(convertIndexToModel(viewIndex));
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
/*      */   public Object getSelectedValue()
/*      */   {
/*  901 */     int i = getSelectedIndex();
/*  902 */     return i == -1 ? null : getElementAt(i);
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
/*      */   public void setSelectedValue(Object anObject, boolean shouldScroll)
/*      */   {
/*  918 */     if (anObject == null) {
/*  919 */       setSelectedIndex(-1);
/*  920 */     } else if (!anObject.equals(getSelectedValue()))
/*      */     {
/*  922 */       int i = 0; for (int c = getElementCount(); i < c; i++)
/*  923 */         if (anObject.equals(getElementAt(i))) {
/*  924 */           setSelectedIndex(i);
/*  925 */           if (shouldScroll)
/*  926 */             ensureIndexIsVisible(i);
/*  927 */           repaint();
/*  928 */           return;
/*      */         }
/*  930 */       setSelectedIndex(-1);
/*      */     }
/*  932 */     repaint();
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
/*      */   public Object[] getSelectedValues()
/*      */   {
/*  946 */     int[] selectedIndexes = getSelectedIndices();
/*  947 */     Object[] selectedValues = new Object[selectedIndexes.length];
/*  948 */     for (int i = 0; i < selectedIndexes.length; i++) {
/*  949 */       selectedValues[i] = getElementAt(selectedIndexes[i]);
/*      */     }
/*  951 */     return selectedValues;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public int getElementCount()
/*      */   {
/*  961 */     return getRowSorter() != null ? getRowSorter().getViewRowCount() : getModel().getSize();
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
/*      */   public int convertIndexToModel(int viewIndex)
/*      */   {
/*  974 */     return getRowSorter() != null ? getRowSorter().convertRowIndexToModel(viewIndex) : viewIndex;
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
/*      */   public int convertIndexToView(int modelIndex)
/*      */   {
/*  988 */     return getRowSorter() != null ? getRowSorter().convertRowIndexToView(modelIndex) : modelIndex;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   @Deprecated
/*      */   public ListModel getWrappedModel()
/*      */   {
/* 1000 */     return getModel();
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
/*      */   public void setModel(ListModel model)
/*      */   {
/* 1015 */     super.setModel(model);
/* 1016 */     if (getAutoCreateRowSorter()) {
/* 1017 */       setRowSorter(createDefaultRowSorter());
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected ComponentAdapter getComponentAdapter()
/*      */   {
/* 1028 */     if (this.dataAdapter == null) {
/* 1029 */       this.dataAdapter = new ListAdapter(this);
/*      */     }
/* 1031 */     return this.dataAdapter;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected ComponentAdapter getComponentAdapter(int index)
/*      */   {
/* 1042 */     ComponentAdapter adapter = getComponentAdapter();
/* 1043 */     adapter.column = 0;
/* 1044 */     adapter.row = index;
/* 1045 */     return adapter;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   protected static class ListAdapter
/*      */     extends ComponentAdapter
/*      */   {
/*      */     private final JXList list;
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */     public ListAdapter(JXList component)
/*      */     {
/* 1061 */       super();
/* 1062 */       this.list = component;
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     public JXList getList()
/*      */     {
/* 1071 */       return this.list;
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     public boolean hasFocus()
/*      */     {
/* 1080 */       return (this.list.isFocusOwner()) && (this.row == this.list.getLeadSelectionIndex());
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */     public int getRowCount()
/*      */     {
/* 1088 */       return this.list.getModel().getSize();
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */     public Object getValueAt(int row, int column)
/*      */     {
/* 1096 */       return this.list.getModel().getElementAt(row);
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     public String getStringAt(int row, int column)
/*      */     {
/* 1106 */       StringValue sv = this.list.getStringValueRegistry().getStringValue(row, column);
/* 1107 */       return sv.getString(getValueAt(row, column));
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */     public Rectangle getCellBounds()
/*      */     {
/* 1115 */       return this.list.getCellBounds(this.row, this.row);
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */     public boolean isCellEditable(int row, int column)
/*      */     {
/* 1123 */       return false;
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */     public boolean isEditable()
/*      */     {
/* 1131 */       return false;
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     public boolean isSelected()
/*      */     {
/* 1140 */       return this.list.isSelectedIndex(this.row);
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */     public int convertRowIndexToView(int rowModelIndex)
/*      */     {
/* 1148 */       return this.list.convertIndexToView(rowModelIndex);
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */     public int convertRowIndexToModel(int rowViewIndex)
/*      */     {
/* 1156 */       return this.list.convertIndexToModel(rowViewIndex);
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
/*      */   public void setHighlighters(Highlighter... highlighters)
/*      */   {
/* 1182 */     Highlighter[] old = getHighlighters();
/* 1183 */     getCompoundHighlighter().setHighlighters(highlighters);
/* 1184 */     firePropertyChange("highlighters", old, getHighlighters());
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
/* 1195 */     return getCompoundHighlighter().getHighlighters();
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
/*      */   public void addHighlighter(Highlighter highlighter)
/*      */   {
/* 1209 */     Highlighter[] old = getHighlighters();
/* 1210 */     getCompoundHighlighter().addHighlighter(highlighter);
/* 1211 */     firePropertyChange("highlighters", old, getHighlighters());
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
/*      */   public void removeHighlighter(Highlighter highlighter)
/*      */   {
/* 1224 */     Highlighter[] old = getHighlighters();
/* 1225 */     getCompoundHighlighter().removeHighlighter(highlighter);
/* 1226 */     firePropertyChange("highlighters", old, getHighlighters());
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected CompoundHighlighter getCompoundHighlighter()
/*      */   {
/* 1236 */     if (this.compoundHighlighter == null) {
/* 1237 */       this.compoundHighlighter = new CompoundHighlighter(new Highlighter[0]);
/* 1238 */       this.compoundHighlighter.addChangeListener(getHighlighterChangeListener());
/*      */     }
/* 1240 */     return this.compoundHighlighter;
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
/* 1251 */     if (this.highlighterChangeListener == null) {
/* 1252 */       this.highlighterChangeListener = createHighlighterChangeListener();
/*      */     }
/* 1254 */     return this.highlighterChangeListener;
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
/* 1266 */     new ChangeListener() {
/*      */       public void stateChanged(ChangeEvent e) {
/* 1268 */         JXList.this.repaint();
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
/* 1283 */     if (this.stringValueRegistry == null) {
/* 1284 */       this.stringValueRegistry = createDefaultStringValueRegistry();
/*      */     }
/* 1286 */     return this.stringValueRegistry;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected StringValueRegistry createDefaultStringValueRegistry()
/*      */   {
/* 1295 */     return new StringValueRegistry();
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
/*      */   public String getStringAt(int row)
/*      */   {
/* 1309 */     StringValue stringValue = getStringValueRegistry().getStringValue(convertIndexToModel(row), 0);
/*      */     
/* 1311 */     return stringValue.getString(getElementAt(row));
/*      */   }
/*      */   
/*      */   private DelegatingRenderer getDelegatingRenderer() {
/* 1315 */     if (this.delegatingRenderer == null)
/*      */     {
/* 1317 */       this.delegatingRenderer = new DelegatingRenderer();
/*      */     }
/* 1319 */     return this.delegatingRenderer;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected ListCellRenderer createDefaultCellRenderer()
/*      */   {
/* 1329 */     return new DefaultListRenderer();
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
/*      */   public ListCellRenderer getCellRenderer()
/*      */   {
/* 1344 */     return getDelegatingRenderer();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public ListCellRenderer getWrappedCellRenderer()
/*      */   {
/* 1355 */     return getDelegatingRenderer().getDelegateRenderer();
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
/*      */   public void setCellRenderer(ListCellRenderer renderer)
/*      */   {
/* 1380 */     boolean forceFire = this.delegatingRenderer != null;
/*      */     
/*      */ 
/*      */ 
/* 1384 */     getDelegatingRenderer().setDelegateRenderer(renderer);
/* 1385 */     getStringValueRegistry().setStringValue((renderer instanceof StringValue) ? (StringValue)renderer : null, 0);
/*      */     
/*      */ 
/* 1388 */     super.setCellRenderer(this.delegatingRenderer);
/* 1389 */     if (forceFire) {
/* 1390 */       firePropertyChange("cellRenderer", null, this.delegatingRenderer);
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public class DelegatingRenderer
/*      */     implements ListCellRenderer, RolloverRenderer
/*      */   {
/*      */     private ListCellRenderer delegateRenderer;
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */     public DelegatingRenderer()
/*      */     {
/* 1407 */       this(null);
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     public DelegatingRenderer(ListCellRenderer delegate)
/*      */     {
/* 1418 */       setDelegateRenderer(delegate);
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     public void setDelegateRenderer(ListCellRenderer delegate)
/*      */     {
/* 1429 */       if (delegate == null) {
/* 1430 */         delegate = JXList.this.createDefaultCellRenderer();
/*      */       }
/* 1432 */       this.delegateRenderer = delegate;
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     public ListCellRenderer getDelegateRenderer()
/*      */     {
/* 1442 */       return this.delegateRenderer;
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */     public void updateUI()
/*      */     {
/* 1449 */       updateRendererUI(this.delegateRenderer);
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */     private void updateRendererUI(ListCellRenderer renderer)
/*      */     {
/* 1457 */       if (renderer == null) return;
/* 1458 */       Component comp = null;
/* 1459 */       if ((renderer instanceof AbstractRenderer)) {
/* 1460 */         comp = ((AbstractRenderer)renderer).getComponentProvider().getRendererComponent(null);
/* 1461 */       } else if ((renderer instanceof Component)) {
/* 1462 */         comp = (Component)renderer;
/*      */       } else {
/*      */         try {
/* 1465 */           comp = renderer.getListCellRendererComponent(JXList.this, null, -1, false, false);
/*      */         }
/*      */         catch (Exception e) {}
/*      */       }
/*      */       
/*      */ 
/* 1471 */       if (comp != null) {
/* 1472 */         SwingUtilities.updateComponentTreeUI(comp);
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
/*      */     public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus)
/*      */     {
/* 1486 */       Component comp = this.delegateRenderer.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
/*      */       
/* 1488 */       if ((JXList.this.compoundHighlighter != null) && (index >= 0) && (index < JXList.this.getElementCount())) {
/* 1489 */         comp = JXList.this.compoundHighlighter.highlight(comp, JXList.this.getComponentAdapter(index));
/*      */       }
/* 1491 */       return comp;
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     public boolean isEnabled()
/*      */     {
/* 1502 */       return ((this.delegateRenderer instanceof RolloverRenderer)) && (((RolloverRenderer)this.delegateRenderer).isEnabled());
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */     public void doClick()
/*      */     {
/* 1510 */       if (isEnabled()) {
/* 1511 */         ((RolloverRenderer)this.delegateRenderer).doClick();
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
/*      */   public void invalidateCellSizeCache()
/*      */   {
/* 1526 */     if ((getUI() instanceof BasicXListUI)) {
/* 1527 */       ((BasicXListUI)getUI()).invalidateCellSizeCache();
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
/*      */   public void updateUI()
/*      */   {
/* 1542 */     if (getUIClassID() == super.getUIClassID()) {
/* 1543 */       super.updateUI();
/*      */     } else {
/* 1545 */       setUI((ListUI)LookAndFeelAddons.getUI(this, ListUI.class));
/*      */     }
/* 1547 */     updateRendererUI();
/* 1548 */     updateHighlighterUI();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public String getUIClassID()
/*      */   {
/* 1555 */     return "XListUI";
/*      */   }
/*      */   
/*      */   private void updateRendererUI() {
/* 1559 */     if (this.delegatingRenderer != null) {
/* 1560 */       this.delegatingRenderer.updateUI();
/*      */     } else {
/* 1562 */       ListCellRenderer renderer = getCellRenderer();
/* 1563 */       if ((renderer instanceof Component)) {
/* 1564 */         SwingUtilities.updateComponentTreeUI((Component)renderer);
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected void updateHighlighterUI()
/*      */   {
/* 1575 */     if (this.compoundHighlighter == null) return;
/* 1576 */     this.compoundHighlighter.updateUI();
/*      */   }
/*      */ }


/* Location:              E:\java\java学习\hutubill\lib\all.jar!\org\jdesktop\swingx\JXList.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */
/*      */ package org.jdesktop.swingx;
/*      */ 
/*      */ import java.applet.Applet;
/*      */ import java.awt.Color;
/*      */ import java.awt.Component;
/*      */ import java.awt.KeyboardFocusManager;
/*      */ import java.awt.Rectangle;
/*      */ import java.awt.Window;
/*      */ import java.awt.event.ActionEvent;
/*      */ import java.beans.PropertyChangeEvent;
/*      */ import java.beans.PropertyChangeListener;
/*      */ import java.util.Hashtable;
/*      */ import java.util.Vector;
/*      */ import java.util.logging.Logger;
/*      */ import javax.swing.Action;
/*      */ import javax.swing.ActionMap;
/*      */ import javax.swing.CellEditor;
/*      */ import javax.swing.Icon;
/*      */ import javax.swing.InputMap;
/*      */ import javax.swing.JPopupMenu;
/*      */ import javax.swing.JTree;
/*      */ import javax.swing.JTree.TreeModelHandler;
/*      */ import javax.swing.KeyStroke;
/*      */ import javax.swing.SwingUtilities;
/*      */ import javax.swing.UIManager;
/*      */ import javax.swing.event.CellEditorListener;
/*      */ import javax.swing.event.ChangeEvent;
/*      */ import javax.swing.event.ChangeListener;
/*      */ import javax.swing.event.TreeModelEvent;
/*      */ import javax.swing.event.TreeModelListener;
/*      */ import javax.swing.plaf.basic.BasicTreeUI;
/*      */ import javax.swing.tree.DefaultTreeCellRenderer;
/*      */ import javax.swing.tree.TreeCellEditor;
/*      */ import javax.swing.tree.TreeCellRenderer;
/*      */ import javax.swing.tree.TreeModel;
/*      */ import javax.swing.tree.TreeNode;
/*      */ import javax.swing.tree.TreePath;
/*      */ import org.jdesktop.swingx.decorator.ComponentAdapter;
/*      */ import org.jdesktop.swingx.decorator.CompoundHighlighter;
/*      */ import org.jdesktop.swingx.decorator.Highlighter;
/*      */ import org.jdesktop.swingx.plaf.UIDependent;
/*      */ import org.jdesktop.swingx.renderer.StringValue;
/*      */ import org.jdesktop.swingx.renderer.StringValues;
/*      */ import org.jdesktop.swingx.rollover.RolloverProducer;
/*      */ import org.jdesktop.swingx.rollover.RolloverRenderer;
/*      */ import org.jdesktop.swingx.rollover.TreeRolloverController;
/*      */ import org.jdesktop.swingx.rollover.TreeRolloverProducer;
/*      */ import org.jdesktop.swingx.search.SearchFactory;
/*      */ import org.jdesktop.swingx.search.Searchable;
/*      */ import org.jdesktop.swingx.search.TreeSearchable;
/*      */ import org.jdesktop.swingx.tree.DefaultXTreeCellEditor;
/*      */ import org.jdesktop.swingx.tree.DefaultXTreeCellRenderer;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ public class JXTree
/*      */   extends JTree
/*      */ {
/*  176 */   private static final Logger LOG = Logger.getLogger(JXTree.class.getName());
/*      */   
/*      */ 
/*      */ 
/*  180 */   private static final int[] EMPTY_INT_ARRAY = new int[0];
/*      */   
/*  182 */   private static final TreePath[] EMPTY_TREEPATH_ARRAY = new TreePath[0];
/*      */   
/*      */ 
/*      */ 
/*      */   protected CompoundHighlighter compoundHighlighter;
/*      */   
/*      */ 
/*      */ 
/*      */   private ChangeListener highlighterChangeListener;
/*      */   
/*      */ 
/*      */ 
/*      */   private DelegatingRenderer delegatingRenderer;
/*      */   
/*      */ 
/*      */ 
/*      */   private RolloverProducer rolloverProducer;
/*      */   
/*      */ 
/*      */ 
/*      */   private TreeRolloverController<JXTree> linkController;
/*      */   
/*      */ 
/*      */   private boolean overwriteIcons;
/*      */   
/*      */ 
/*      */   private Searchable searchable;
/*      */   
/*      */ 
/*      */   private CellEditorRemover editorRemover;
/*      */   
/*      */ 
/*      */   private CellEditorListener editorListener;
/*      */   
/*      */ 
/*      */   private Color selectionForeground;
/*      */   
/*      */ 
/*      */   private Color selectionBackground;
/*      */   
/*      */ 
/*      */   protected ComponentAdapter dataAdapter;
/*      */   
/*      */ 
/*      */ 
/*      */   public JXTree()
/*      */   {
/*  229 */     init();
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
/*      */   public JXTree(Object[] value)
/*      */   {
/*  243 */     super(value);
/*  244 */     init();
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
/*      */   public JXTree(Vector<?> value)
/*      */   {
/*  258 */     super(value);
/*  259 */     init();
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
/*      */   public JXTree(Hashtable<?, ?> value)
/*      */   {
/*  274 */     super(value);
/*  275 */     init();
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
/*      */   public JXTree(TreeNode root)
/*      */   {
/*  289 */     super(root, false);
/*  290 */     init();
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
/*      */   public JXTree(TreeNode root, boolean asksAllowsChildren)
/*      */   {
/*  307 */     super(root, asksAllowsChildren);
/*  308 */     init();
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
/*      */   public JXTree(TreeModel newModel)
/*      */   {
/*  322 */     super(newModel);
/*  323 */     init();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   private void init()
/*      */   {
/*  335 */     setCellRenderer(createDefaultCellRenderer());
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  348 */     if ((getWrappedCellRenderer() instanceof DefaultTreeCellRenderer)) {
/*  349 */       setCellEditor(new DefaultXTreeCellEditor(this, (DefaultTreeCellRenderer)getWrappedCellRenderer()));
/*      */     }
/*      */     
/*  352 */     ActionMap map = getActionMap();
/*  353 */     map.put("expand-all", new Actions("expand-all"));
/*  354 */     map.put("collapse-all", new Actions("collapse-all"));
/*  355 */     map.put("find", createFindAction());
/*      */     
/*  357 */     KeyStroke findStroke = SearchFactory.getInstance().getSearchAccelerator();
/*  358 */     getInputMap(1).put(findStroke, "find");
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected class XTreeModelHandler
/*      */     extends JTree.TreeModelHandler
/*      */   {
/*      */     protected XTreeModelHandler()
/*      */     {
/*  370 */       super();
/*      */     }
/*      */     
/*      */ 
/*      */     public void treeNodesInserted(TreeModelEvent e)
/*      */     {
/*  376 */       TreePath path = e.getTreePath();
/*      */       
/*      */ 
/*  379 */       if ((path.getParentPath() == null) && (!JXTree.this.isRootVisible()) && (JXTree.this.isCollapsed(path)))
/*      */       {
/*  381 */         JXTree.this.expandPath(path);
/*      */       }
/*      */       
/*  384 */       super.treeNodesInserted(e);
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   protected TreeModelListener createTreeModelListener()
/*      */   {
/*  393 */     return new XTreeModelHandler();
/*      */   }
/*      */   
/*      */ 
/*      */   private class Actions
/*      */     extends UIAction
/*      */   {
/*      */     Actions(String name)
/*      */     {
/*  402 */       super();
/*      */     }
/*      */     
/*      */     public void actionPerformed(ActionEvent evt) {
/*  406 */       if ("expand-all".equals(getName())) {
/*  407 */         JXTree.this.expandAll();
/*      */       }
/*  409 */       else if ("collapse-all".equals(getName())) {
/*  410 */         JXTree.this.collapseAll();
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
/*      */   private Action createFindAction()
/*      */   {
/*  424 */     new UIAction("find") {
/*      */       public void actionPerformed(ActionEvent e) {
/*  426 */         JXTree.this.doFind();
/*      */       }
/*      */     };
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   protected void doFind()
/*      */   {
/*  436 */     SearchFactory.getInstance().showFindInput(this, getSearchable());
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
/*  450 */     if (this.searchable == null) {
/*  451 */       this.searchable = new TreeSearchable(this);
/*      */     }
/*  453 */     return this.searchable;
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
/*      */   public void setSearchable(Searchable searchable)
/*      */   {
/*  466 */     this.searchable = searchable;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public String getStringAt(int row)
/*      */   {
/*  477 */     return getStringAt(getPathForRow(row));
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public String getStringAt(TreePath path)
/*      */   {
/*  488 */     if (path == null) return null;
/*  489 */     TreeCellRenderer renderer = getDelegatingRenderer().getDelegateRenderer();
/*  490 */     if ((renderer instanceof StringValue)) {
/*  491 */       return ((StringValue)renderer).getString(path.getLastPathComponent());
/*      */     }
/*  493 */     return StringValues.TO_STRING.getString(path.getLastPathComponent());
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void collapseAll()
/*      */   {
/*  503 */     for (int i = getRowCount() - 1; i >= 0; i--) {
/*  504 */       collapseRow(i);
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public void expandAll()
/*      */   {
/*  512 */     if (getRowCount() == 0) {
/*  513 */       expandRoot();
/*      */     }
/*  515 */     for (int i = 0; i < getRowCount(); i++) {
/*  516 */       expandRow(i);
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   private void expandRoot()
/*      */   {
/*  525 */     TreeModel model = getModel();
/*  526 */     if ((model != null) && (model.getRoot() != null)) {
/*  527 */       expandPath(new TreePath(model.getRoot()));
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
/*      */   public int[] getSelectionRows()
/*      */   {
/*  540 */     int[] rows = super.getSelectionRows();
/*  541 */     return rows != null ? rows : EMPTY_INT_ARRAY;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public TreePath[] getSelectionPaths()
/*      */   {
/*  553 */     TreePath[] paths = super.getSelectionPaths();
/*  554 */     return paths != null ? paths : EMPTY_TREEPATH_ARRAY;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public Color getSelectionBackground()
/*      */   {
/*  566 */     return this.selectionBackground;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public Color getSelectionForeground()
/*      */   {
/*  577 */     return this.selectionForeground;
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
/*      */   public void setSelectionForeground(Color selectionForeground)
/*      */   {
/*  603 */     Object oldValue = getSelectionForeground();
/*  604 */     this.selectionForeground = selectionForeground;
/*  605 */     firePropertyChange("selectionForeground", oldValue, getSelectionForeground());
/*  606 */     repaint();
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
/*      */   public void setSelectionBackground(Color selectionBackground)
/*      */   {
/*  631 */     Object oldValue = getSelectionBackground();
/*  632 */     this.selectionBackground = selectionBackground;
/*  633 */     firePropertyChange("selectionBackground", oldValue, getSelectionBackground());
/*  634 */     repaint();
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
/*  648 */     uninstallSelectionColors();
/*  649 */     super.updateUI();
/*  650 */     installSelectionColors();
/*  651 */     updateHighlighterUI();
/*  652 */     updateRendererEditorUI();
/*  653 */     invalidateCellSizeCache();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   protected void updateRendererEditorUI()
/*      */   {
/*  661 */     if ((getCellEditor() instanceof UIDependent)) {
/*  662 */       ((UIDependent)getCellEditor()).updateUI();
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
/*      */   private void installSelectionColors()
/*      */   {
/*  679 */     if (SwingXUtilities.isUIInstallable(getSelectionBackground())) {
/*  680 */       setSelectionBackground(UIManager.getColor("Tree.selectionBackground"));
/*      */     }
/*  682 */     if (SwingXUtilities.isUIInstallable(getSelectionForeground())) {
/*  683 */       setSelectionForeground(UIManager.getColor("Tree.selectionForeground"));
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   private void uninstallSelectionColors()
/*      */   {
/*  694 */     if (SwingXUtilities.isUIInstallable(getSelectionBackground())) {
/*  695 */       setSelectionBackground(null);
/*      */     }
/*  697 */     if (SwingXUtilities.isUIInstallable(getSelectionForeground())) {
/*  698 */       setSelectionForeground(null);
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected void updateHighlighterUI()
/*      */   {
/*  708 */     if (this.compoundHighlighter == null) return;
/*  709 */     this.compoundHighlighter.updateUI();
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
/*      */   public void setRolloverEnabled(boolean rolloverEnabled)
/*      */   {
/*  735 */     boolean old = isRolloverEnabled();
/*  736 */     if (rolloverEnabled == old) return;
/*  737 */     if (rolloverEnabled) {
/*  738 */       this.rolloverProducer = createRolloverProducer();
/*  739 */       this.rolloverProducer.install(this);
/*  740 */       getLinkController().install(this);
/*      */     } else {
/*  742 */       this.rolloverProducer.release(this);
/*  743 */       this.rolloverProducer = null;
/*  744 */       getLinkController().release();
/*      */     }
/*  746 */     firePropertyChange("rolloverEnabled", old, isRolloverEnabled());
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
/*  757 */     return this.rolloverProducer != null;
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
/*      */   protected TreeRolloverController<JXTree> getLinkController()
/*      */   {
/*  774 */     if (this.linkController == null) {
/*  775 */       this.linkController = createLinkController();
/*      */     }
/*  777 */     return this.linkController;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected TreeRolloverController<JXTree> createLinkController()
/*      */   {
/*  789 */     return new TreeRolloverController();
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
/*  801 */     return new TreeRolloverProducer();
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
/*      */   public void setHighlighters(Highlighter... highlighters)
/*      */   {
/*  825 */     Highlighter[] old = getHighlighters();
/*  826 */     getCompoundHighlighter().setHighlighters(highlighters);
/*  827 */     firePropertyChange("highlighters", old, getHighlighters());
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
/*  838 */     return getCompoundHighlighter().getHighlighters();
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
/*  853 */     Highlighter[] old = getHighlighters();
/*  854 */     getCompoundHighlighter().addHighlighter(highlighter);
/*  855 */     firePropertyChange("highlighters", old, getHighlighters());
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
/*  868 */     Highlighter[] old = getHighlighters();
/*  869 */     getCompoundHighlighter().removeHighlighter(highlighter);
/*  870 */     firePropertyChange("highlighters", old, getHighlighters());
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected CompoundHighlighter getCompoundHighlighter()
/*      */   {
/*  880 */     if (this.compoundHighlighter == null) {
/*  881 */       this.compoundHighlighter = new CompoundHighlighter(new Highlighter[0]);
/*  882 */       this.compoundHighlighter.addChangeListener(getHighlighterChangeListener());
/*      */     }
/*  884 */     return this.compoundHighlighter;
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
/*  895 */     if (this.highlighterChangeListener == null) {
/*  896 */       this.highlighterChangeListener = createHighlighterChangeListener();
/*      */     }
/*  898 */     return this.highlighterChangeListener;
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
/*  910 */     new ChangeListener() {
/*      */       public void stateChanged(ChangeEvent e) {
/*  912 */         JXTree.this.repaint();
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
/*      */ 
/*      */   public void setExpandedIcon(Icon expandedIcon)
/*      */   {
/*  928 */     if ((getUI() instanceof BasicTreeUI)) {
/*  929 */       ((BasicTreeUI)getUI()).setExpandedIcon(expandedIcon);
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
/*      */   public void setCollapsedIcon(Icon collapsedIcon)
/*      */   {
/*  944 */     if ((getUI() instanceof BasicTreeUI)) {
/*  945 */       ((BasicTreeUI)getUI()).setCollapsedIcon(collapsedIcon);
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
/*      */   public void setLeafIcon(Icon leafIcon)
/*      */   {
/*  964 */     getDelegatingRenderer().setLeafIcon(leafIcon);
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
/*      */   public void setOpenIcon(Icon openIcon)
/*      */   {
/*  978 */     getDelegatingRenderer().setOpenIcon(openIcon);
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
/*      */   public void setClosedIcon(Icon closedIcon)
/*      */   {
/*  992 */     getDelegatingRenderer().setClosedIcon(closedIcon);
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
/*      */   public void setOverwriteRendererIcons(boolean overwrite)
/*      */   {
/* 1013 */     if (this.overwriteIcons == overwrite) return;
/* 1014 */     boolean old = this.overwriteIcons;
/* 1015 */     this.overwriteIcons = overwrite;
/* 1016 */     firePropertyChange("overwriteRendererIcons", old, overwrite);
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
/*      */   public boolean isOverwriteRendererIcons()
/*      */   {
/* 1033 */     return this.overwriteIcons;
/*      */   }
/*      */   
/*      */   private DelegatingRenderer getDelegatingRenderer() {
/* 1037 */     if (this.delegatingRenderer == null)
/*      */     {
/* 1039 */       this.delegatingRenderer = new DelegatingRenderer();
/*      */     }
/* 1041 */     return this.delegatingRenderer;
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
/*      */   protected TreeCellRenderer createDefaultCellRenderer()
/*      */   {
/* 1058 */     return new DefaultXTreeCellRenderer();
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
/*      */   public TreeCellRenderer getCellRenderer()
/*      */   {
/* 1076 */     return getDelegatingRenderer();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public TreeCellRenderer getWrappedCellRenderer()
/*      */   {
/* 1087 */     return getDelegatingRenderer().getDelegateRenderer();
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
/*      */   public void setCellRenderer(TreeCellRenderer renderer)
/*      */   {
/* 1107 */     getDelegatingRenderer().setDelegateRenderer(renderer);
/* 1108 */     super.setCellRenderer(this.delegatingRenderer);
/*      */     
/* 1110 */     if (((renderer instanceof DefaultTreeCellRenderer)) && ((getCellEditor() instanceof DefaultXTreeCellEditor)))
/*      */     {
/* 1112 */       ((DefaultXTreeCellEditor)getCellEditor()).setRenderer((DefaultTreeCellRenderer)renderer);
/*      */     }
/* 1114 */     firePropertyChange("cellRenderer", null, this.delegatingRenderer);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public class DelegatingRenderer
/*      */     implements TreeCellRenderer, RolloverRenderer
/*      */   {
/* 1127 */     private Icon closedIcon = null;
/* 1128 */     private Icon openIcon = null;
/* 1129 */     private Icon leafIcon = null;
/*      */     
/*      */ 
/*      */     private TreeCellRenderer delegate;
/*      */     
/*      */ 
/*      */     public DelegatingRenderer()
/*      */     {
/* 1137 */       this(null);
/* 1138 */       initIcons(new DefaultTreeCellRenderer());
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     public DelegatingRenderer(TreeCellRenderer delegate)
/*      */     {
/* 1149 */       initIcons((DefaultTreeCellRenderer)((delegate instanceof DefaultTreeCellRenderer) ? delegate : new DefaultTreeCellRenderer()));
/*      */       
/* 1151 */       setDelegateRenderer(delegate);
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     private void initIcons(DefaultTreeCellRenderer renderer)
/*      */     {
/* 1161 */       this.closedIcon = renderer.getDefaultClosedIcon();
/* 1162 */       this.openIcon = renderer.getDefaultOpenIcon();
/* 1163 */       this.leafIcon = renderer.getDefaultLeafIcon();
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
/*      */     public void setDelegateRenderer(TreeCellRenderer delegate)
/*      */     {
/* 1179 */       if (delegate == null) {
/* 1180 */         delegate = JXTree.this.createDefaultCellRenderer();
/*      */       }
/* 1182 */       this.delegate = delegate;
/* 1183 */       updateIcons();
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     private void updateIcons()
/*      */     {
/* 1195 */       if (!JXTree.this.isOverwriteRendererIcons()) return;
/* 1196 */       setClosedIcon(this.closedIcon);
/* 1197 */       setOpenIcon(this.openIcon);
/* 1198 */       setLeafIcon(this.leafIcon);
/*      */     }
/*      */     
/*      */     public void setClosedIcon(Icon closedIcon) {
/* 1202 */       if ((this.delegate instanceof DefaultTreeCellRenderer)) {
/* 1203 */         ((DefaultTreeCellRenderer)this.delegate).setClosedIcon(closedIcon);
/*      */       }
/* 1205 */       this.closedIcon = closedIcon;
/*      */     }
/*      */     
/*      */     public void setOpenIcon(Icon openIcon) {
/* 1209 */       if ((this.delegate instanceof DefaultTreeCellRenderer)) {
/* 1210 */         ((DefaultTreeCellRenderer)this.delegate).setOpenIcon(openIcon);
/*      */       }
/* 1212 */       this.openIcon = openIcon;
/*      */     }
/*      */     
/*      */     public void setLeafIcon(Icon leafIcon) {
/* 1216 */       if ((this.delegate instanceof DefaultTreeCellRenderer)) {
/* 1217 */         ((DefaultTreeCellRenderer)this.delegate).setLeafIcon(leafIcon);
/*      */       }
/* 1219 */       this.leafIcon = leafIcon;
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     public TreeCellRenderer getDelegateRenderer()
/*      */     {
/* 1231 */       return this.delegate;
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     public Component getTreeCellRendererComponent(JTree tree, Object value, boolean selected, boolean expanded, boolean leaf, int row, boolean hasFocus)
/*      */     {
/* 1243 */       Component result = this.delegate.getTreeCellRendererComponent(tree, value, selected, expanded, leaf, row, hasFocus);
/*      */       
/*      */ 
/* 1246 */       if ((JXTree.this.compoundHighlighter != null) && (row < JXTree.this.getRowCount()) && (row >= 0))
/*      */       {
/* 1248 */         result = JXTree.this.compoundHighlighter.highlight(result, JXTree.this.getComponentAdapter(row));
/*      */       }
/*      */       
/*      */ 
/* 1252 */       return result;
/*      */     }
/*      */     
/*      */ 
/*      */     public boolean isEnabled()
/*      */     {
/* 1258 */       return ((this.delegate instanceof RolloverRenderer)) && (((RolloverRenderer)this.delegate).isEnabled());
/*      */     }
/*      */     
/*      */     public void doClick()
/*      */     {
/* 1263 */       if (isEnabled()) {
/* 1264 */         ((RolloverRenderer)this.delegate).doClick();
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
/*      */   public void invalidateCellSizeCache()
/*      */   {
/* 1281 */     if ((getUI() instanceof BasicTreeUI)) {
/* 1282 */       BasicTreeUI ui = (BasicTreeUI)getUI();
/* 1283 */       ui.setLeftChildIndent(ui.getLeftChildIndent());
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
/*      */   public void startEditingAtPath(TreePath path)
/*      */   {
/* 1301 */     super.startEditingAtPath(path);
/* 1302 */     if (isEditing()) {
/* 1303 */       updateEditorListener();
/* 1304 */       updateEditorRemover();
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   private void updateEditorListener()
/*      */   {
/* 1313 */     if (this.editorListener == null) {
/* 1314 */       this.editorListener = new CellEditorListener()
/*      */       {
/*      */         public void editingCanceled(ChangeEvent e) {
/* 1317 */           terminated(e);
/*      */         }
/*      */         
/*      */ 
/*      */ 
/*      */         private void terminated(ChangeEvent e)
/*      */         {
/* 1324 */           JXTree.this.analyseFocus();
/* 1325 */           ((CellEditor)e.getSource()).removeCellEditorListener(JXTree.this.editorListener);
/*      */         }
/*      */         
/*      */         public void editingStopped(ChangeEvent e) {
/* 1329 */           terminated(e);
/*      */         }
/*      */       };
/*      */     }
/*      */     
/* 1334 */     getCellEditor().addCellEditorListener(this.editorListener);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected void analyseFocus()
/*      */   {
/* 1346 */     if (isFocusOwnerDescending()) {
/* 1347 */       requestFocusInWindow();
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
/*      */   private boolean isFocusOwnerDescending()
/*      */   {
/* 1365 */     if (!isEditing()) return false;
/* 1366 */     Component focusOwner = KeyboardFocusManager.getCurrentKeyboardFocusManager().getFocusOwner();
/*      */     
/*      */ 
/* 1369 */     if (focusOwner == null) return false;
/* 1370 */     if (SwingXUtilities.isDescendingFrom(focusOwner, this)) { return true;
/*      */     }
/* 1372 */     Component permanent = KeyboardFocusManager.getCurrentKeyboardFocusManager().getPermanentFocusOwner();
/*      */     
/* 1374 */     return SwingXUtilities.isDescendingFrom(permanent, this);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void removeNotify()
/*      */   {
/* 1384 */     if (this.editorRemover != null) {
/* 1385 */       this.editorRemover.release();
/* 1386 */       this.editorRemover = null;
/*      */     }
/* 1388 */     super.removeNotify();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   private void updateEditorRemover()
/*      */   {
/* 1397 */     if (this.editorRemover == null) {
/* 1398 */       this.editorRemover = new CellEditorRemover();
/*      */     }
/* 1400 */     this.editorRemover.updateKeyboardFocusManager();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public class CellEditorRemover
/*      */     implements PropertyChangeListener
/*      */   {
/*      */     KeyboardFocusManager focusManager;
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */     public CellEditorRemover()
/*      */     {
/* 1417 */       updateKeyboardFocusManager();
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */     public void updateKeyboardFocusManager()
/*      */     {
/* 1425 */       KeyboardFocusManager current = KeyboardFocusManager.getCurrentKeyboardFocusManager();
/* 1426 */       setKeyboardFocusManager(current);
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */     public void release()
/*      */     {
/* 1434 */       setKeyboardFocusManager(null);
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     private void setKeyboardFocusManager(KeyboardFocusManager current)
/*      */     {
/* 1445 */       if (this.focusManager == current)
/* 1446 */         return;
/* 1447 */       KeyboardFocusManager old = this.focusManager;
/* 1448 */       if (old != null) {
/* 1449 */         old.removePropertyChangeListener("permanentFocusOwner", this);
/*      */       }
/* 1451 */       this.focusManager = current;
/* 1452 */       if (this.focusManager != null) {
/* 1453 */         this.focusManager.addPropertyChangeListener("permanentFocusOwner", this);
/*      */       }
/*      */     }
/*      */     
/*      */     public void propertyChange(PropertyChangeEvent ev)
/*      */     {
/* 1459 */       if (!JXTree.this.isEditing()) {
/* 1460 */         return;
/*      */       }
/*      */       
/* 1463 */       Component c = this.focusManager.getPermanentFocusOwner();
/* 1464 */       JXTree tree = JXTree.this;
/* 1465 */       while (c != null) {
/* 1466 */         if ((c instanceof JPopupMenu)) {
/* 1467 */           c = ((JPopupMenu)c).getInvoker();
/*      */         }
/*      */         else {
/* 1470 */           if (c == tree)
/*      */           {
/* 1472 */             return; }
/* 1473 */           if (((c instanceof Window)) || (((c instanceof Applet)) && (c.getParent() == null)))
/*      */           {
/* 1475 */             if (c != SwingUtilities.getRoot(tree)) break;
/* 1476 */             if (tree.getInvokesStopCellEditing()) {
/* 1477 */               tree.stopEditing();
/*      */             }
/* 1479 */             if (!tree.isEditing()) break;
/* 1480 */             tree.cancelEditing(); break;
/*      */           }
/*      */           
/*      */ 
/*      */ 
/* 1485 */           c = c.getParent();
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
/*      */   public void setModel(TreeModel newModel)
/*      */   {
/* 1501 */     super.setModel(newModel);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected ComponentAdapter getComponentAdapter()
/*      */   {
/* 1511 */     if (this.dataAdapter == null) {
/* 1512 */       this.dataAdapter = new TreeAdapter(this);
/*      */     }
/* 1514 */     return this.dataAdapter;
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
/* 1525 */     ComponentAdapter adapter = getComponentAdapter();
/* 1526 */     adapter.column = 0;
/* 1527 */     adapter.row = index;
/* 1528 */     return adapter;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   protected static class TreeAdapter
/*      */     extends ComponentAdapter
/*      */   {
/*      */     private final JXTree tree;
/*      */     
/*      */ 
/*      */ 
/*      */     public TreeAdapter(JXTree component)
/*      */     {
/* 1543 */       super();
/* 1544 */       this.tree = component;
/*      */     }
/*      */     
/*      */     public JXTree getTree() {
/* 1548 */       return this.tree;
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */     public boolean hasFocus()
/*      */     {
/* 1556 */       return (this.tree.isFocusOwner()) && (this.tree.getLeadSelectionRow() == this.row);
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */     public Object getValueAt(int row, int column)
/*      */     {
/* 1564 */       TreePath path = this.tree.getPathForRow(row);
/* 1565 */       return path.getLastPathComponent();
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */     public String getStringAt(int row, int column)
/*      */     {
/* 1573 */       return this.tree.getStringAt(row);
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */     public Rectangle getCellBounds()
/*      */     {
/* 1581 */       return this.tree.getRowBounds(this.row);
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     public boolean isEditable()
/*      */     {
/* 1590 */       return this.tree.isPathEditable(this.tree.getPathForRow(this.row));
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */     public boolean isSelected()
/*      */     {
/* 1598 */       return this.tree.isRowSelected(this.row);
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */     public boolean isExpanded()
/*      */     {
/* 1606 */       return this.tree.isExpanded(this.row);
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */     public int getDepth()
/*      */     {
/* 1614 */       return this.tree.getPathForRow(this.row).getPathCount() - 1;
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */     public boolean isHierarchical()
/*      */     {
/* 1622 */       return true;
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */     public boolean isLeaf()
/*      */     {
/* 1630 */       return this.tree.getModel().isLeaf(getValue());
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */     public boolean isCellEditable(int row, int column)
/*      */     {
/* 1638 */       return false;
/*      */     }
/*      */   }
/*      */ }


/* Location:              E:\java\java学习\hutubill\lib\all.jar!\org\jdesktop\swingx\JXTree.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */
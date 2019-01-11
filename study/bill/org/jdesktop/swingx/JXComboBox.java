/*     */ package org.jdesktop.swingx;
/*     */ 
/*     */ import java.awt.BorderLayout;
/*     */ import java.awt.Component;
/*     */ import java.awt.Rectangle;
/*     */ import java.awt.event.KeyEvent;
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ import java.util.Vector;
/*     */ import javax.accessibility.Accessible;
/*     */ import javax.swing.ComboBoxEditor;
/*     */ import javax.swing.ComboBoxModel;
/*     */ import javax.swing.JComboBox;
/*     */ import javax.swing.JList;
/*     */ import javax.swing.JTable;
/*     */ import javax.swing.KeyStroke;
/*     */ import javax.swing.ListCellRenderer;
/*     */ import javax.swing.SwingUtilities;
/*     */ import javax.swing.event.ChangeEvent;
/*     */ import javax.swing.event.ChangeListener;
/*     */ import javax.swing.plaf.ComboBoxUI;
/*     */ import javax.swing.plaf.basic.ComboPopup;
/*     */ import org.jdesktop.swingx.decorator.ComponentAdapter;
/*     */ import org.jdesktop.swingx.decorator.CompoundHighlighter;
/*     */ import org.jdesktop.swingx.decorator.Highlighter;
/*     */ import org.jdesktop.swingx.plaf.UIDependent;
/*     */ import org.jdesktop.swingx.renderer.DefaultListRenderer;
/*     */ import org.jdesktop.swingx.renderer.JRendererPanel;
/*     */ import org.jdesktop.swingx.renderer.StringValue;
/*     */ import org.jdesktop.swingx.rollover.RolloverRenderer;
/*     */ import org.jdesktop.swingx.util.Contract;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class JXComboBox
/*     */   extends JComboBox
/*     */ {
/*     */   private ComboBoxAdapter dataAdapter;
/*     */   private DelegatingRenderer delegatingRenderer;
/*     */   public JXComboBox() {}
/*     */   
/*     */   public class DelegatingRenderer
/*     */     implements ListCellRenderer, RolloverRenderer, UIDependent
/*     */   {
/*     */     private ListCellRenderer delegateRenderer;
/*     */     private JRendererPanel wrapper;
/*     */     
/*     */     public DelegatingRenderer()
/*     */     {
/*  82 */       this(null);
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     public DelegatingRenderer(ListCellRenderer delegate)
/*     */     {
/*  93 */       this.wrapper = new JRendererPanel(new BorderLayout());
/*  94 */       setDelegateRenderer(delegate);
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     public void setDelegateRenderer(ListCellRenderer delegate)
/*     */     {
/* 105 */       if (delegate == null) {
/* 106 */         delegate = JXComboBox.this.createDefaultCellRenderer();
/*     */       }
/* 108 */       this.delegateRenderer = delegate;
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     public ListCellRenderer getDelegateRenderer()
/*     */     {
/* 118 */       return this.delegateRenderer;
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */     public void updateUI()
/*     */     {
/* 125 */       this.wrapper.updateUI();
/*     */       
/* 127 */       if ((this.delegateRenderer instanceof UIDependent)) {
/* 128 */         ((UIDependent)this.delegateRenderer).updateUI();
/* 129 */       } else if ((this.delegateRenderer instanceof Component)) {
/* 130 */         SwingUtilities.updateComponentTreeUI((Component)this.delegateRenderer);
/* 131 */       } else if (this.delegateRenderer != null) {
/*     */         try {
/* 133 */           Component comp = this.delegateRenderer.getListCellRendererComponent(JXComboBox.getPopupListFor(JXComboBox.this), null, -1, false, false);
/*     */           
/* 135 */           SwingUtilities.updateComponentTreeUI(comp);
/*     */         }
/*     */         catch (Exception e) {}
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
/*     */     public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus)
/*     */     {
/* 151 */       Component comp = null;
/*     */       
/* 153 */       if (index == -1) {
/* 154 */         comp = this.delegateRenderer.getListCellRendererComponent(list, value, JXComboBox.this.getSelectedIndex(), isSelected, cellHasFocus);
/*     */         
/*     */ 
/* 157 */         if ((JXComboBox.this.isUseHighlightersForCurrentValue()) && (JXComboBox.this.compoundHighlighter != null) && (JXComboBox.this.getSelectedIndex() != -1)) {
/* 158 */           comp = JXComboBox.this.compoundHighlighter.highlight(comp, JXComboBox.this.getComponentAdapter(JXComboBox.this.getSelectedIndex()));
/*     */           
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 164 */           this.wrapper.add(comp);
/* 165 */           comp = this.wrapper;
/*     */         }
/*     */       } else {
/* 168 */         comp = this.delegateRenderer.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
/*     */         
/*     */ 
/* 171 */         if ((JXComboBox.this.compoundHighlighter != null) && (index >= 0) && (index < JXComboBox.this.getItemCount())) {
/* 172 */           comp = JXComboBox.this.compoundHighlighter.highlight(comp, JXComboBox.this.getComponentAdapter(index));
/*     */         }
/*     */       }
/*     */       
/* 176 */       return comp;
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     public boolean isEnabled()
/*     */     {
/* 186 */       return ((this.delegateRenderer instanceof RolloverRenderer)) && (((RolloverRenderer)this.delegateRenderer).isEnabled());
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */     public void doClick()
/*     */     {
/* 194 */       if (isEnabled()) {
/* 195 */         ((RolloverRenderer)this.delegateRenderer).doClick();
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   protected static class ComboBoxAdapter
/*     */     extends ComponentAdapter
/*     */   {
/*     */     private final JComboBox comboBox;
/*     */     
/*     */ 
/*     */     public ComboBoxAdapter(JComboBox component)
/*     */     {
/* 210 */       super();
/* 211 */       this.comboBox = component;
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     public JComboBox getComboBox()
/*     */     {
/* 220 */       return this.comboBox;
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */     public boolean hasFocus()
/*     */     {
/* 228 */       if (this.comboBox.isPopupVisible()) {
/* 229 */         JList list = JXComboBox.getPopupListFor(this.comboBox);
/*     */         
/* 231 */         return (list != null) && (list.isFocusOwner()) && (this.row == list.getLeadSelectionIndex());
/*     */       }
/*     */       
/* 234 */       return this.comboBox.isFocusOwner();
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */     public int getRowCount()
/*     */     {
/* 242 */       return this.comboBox.getModel().getSize();
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */     public Object getValueAt(int row, int column)
/*     */     {
/* 250 */       return this.comboBox.getModel().getElementAt(row);
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     public String getStringAt(int row, int column)
/*     */     {
/* 260 */       ListCellRenderer renderer = this.comboBox.getRenderer();
/*     */       
/* 262 */       if ((renderer instanceof JXComboBox.DelegatingRenderer)) {
/* 263 */         renderer = ((JXComboBox.DelegatingRenderer)renderer).getDelegateRenderer();
/*     */       }
/*     */       
/* 266 */       if ((renderer instanceof StringValue)) {
/* 267 */         return ((StringValue)renderer).getString(getValueAt(row, column));
/*     */       }
/*     */       
/* 270 */       return super.getStringAt(row, column);
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */     public Rectangle getCellBounds()
/*     */     {
/* 278 */       JList list = JXComboBox.getPopupListFor(this.comboBox);
/*     */       
/* 280 */       if (list == null) {
/* 281 */         if (!$assertionsDisabled) throw new AssertionError();
/* 282 */         return new Rectangle(this.comboBox.getSize());
/*     */       }
/*     */       
/* 285 */       return list.getCellBounds(this.row, this.row);
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */     public boolean isCellEditable(int row, int column)
/*     */     {
/* 293 */       return (row == -1) && (this.comboBox.isEditable());
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */     public boolean isEditable()
/*     */     {
/* 301 */       return isCellEditable(this.row, this.column);
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */     public boolean isSelected()
/*     */     {
/* 309 */       if (this.comboBox.isPopupVisible()) {
/* 310 */         JList list = JXComboBox.getPopupListFor(this.comboBox);
/*     */         
/* 312 */         return (list != null) && (this.row == list.getLeadSelectionIndex());
/*     */       }
/*     */       
/* 315 */       return this.comboBox.isFocusOwner();
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 323 */   private boolean useHighlightersForCurrentValue = true;
/*     */   
/*     */   private CompoundHighlighter compoundHighlighter;
/*     */   
/*     */   private ChangeListener highlighterChangeListener;
/*     */   
/* 329 */   private List<KeyEvent> pendingEvents = new ArrayList();
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private boolean isDispatching;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public JXComboBox(ComboBoxModel model)
/*     */   {
/* 355 */     super(model);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public JXComboBox(Object[] items)
/*     */   {
/* 367 */     super(items);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public JXComboBox(Vector<?> items)
/*     */   {
/* 379 */     super(items);
/*     */   }
/*     */   
/*     */   protected static JList getPopupListFor(JComboBox comboBox) {
/* 383 */     int count = comboBox.getUI().getAccessibleChildrenCount(comboBox);
/*     */     
/* 385 */     for (int i = 0; i < count; i++) {
/* 386 */       Accessible a = comboBox.getUI().getAccessibleChild(comboBox, i);
/*     */       
/* 388 */       if ((a instanceof ComboPopup)) {
/* 389 */         return ((ComboPopup)a).getList();
/*     */       }
/*     */     }
/*     */     
/* 393 */     return null;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected boolean processKeyBinding(KeyStroke ks, KeyEvent e, int condition, boolean pressed)
/*     */   {
/* 402 */     boolean retValue = super.processKeyBinding(ks, e, condition, pressed);
/*     */     
/* 404 */     if ((!retValue) && (this.editor != null)) {
/* 405 */       if (isStartingCellEdit(e)) {
/* 406 */         this.pendingEvents.add(e);
/* 407 */       } else if (this.pendingEvents.size() == 2) {
/* 408 */         this.pendingEvents.add(e);
/* 409 */         this.isDispatching = true;
/*     */         
/* 411 */         SwingUtilities.invokeLater(new Runnable() {
/*     */           public void run() {
/*     */             try {
/* 414 */               for (KeyEvent event : JXComboBox.this.pendingEvents) {
/* 415 */                 JXComboBox.this.editor.getEditorComponent().dispatchEvent(event);
/*     */               }
/*     */               
/* 418 */               JXComboBox.this.pendingEvents.clear();
/*     */             } finally {
/* 420 */               JXComboBox.this.isDispatching = false;
/*     */             }
/*     */           }
/*     */         });
/*     */       }
/*     */     }
/* 426 */     return retValue;
/*     */   }
/*     */   
/*     */   private boolean isStartingCellEdit(KeyEvent e) {
/* 430 */     if (this.isDispatching) {
/* 431 */       return false;
/*     */     }
/*     */     
/* 434 */     JTable table = (JTable)SwingUtilities.getAncestorOfClass(JTable.class, this);
/* 435 */     boolean isOwned = (table != null) && (!Boolean.FALSE.equals(table.getClientProperty("JTable.autoStartsEdit")));
/*     */     
/*     */ 
/* 438 */     return (isOwned) && (e.getComponent() == table);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   protected ComponentAdapter getComponentAdapter()
/*     */   {
/* 445 */     if (this.dataAdapter == null) {
/* 446 */       this.dataAdapter = new ComboBoxAdapter(this);
/*     */     }
/* 448 */     return this.dataAdapter;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected ComponentAdapter getComponentAdapter(int index)
/*     */   {
/* 459 */     ComponentAdapter adapter = getComponentAdapter();
/* 460 */     adapter.column = 0;
/* 461 */     adapter.row = index;
/* 462 */     return adapter;
/*     */   }
/*     */   
/*     */   private DelegatingRenderer getDelegatingRenderer() {
/* 466 */     if (this.delegatingRenderer == null)
/*     */     {
/* 468 */       this.delegatingRenderer = new DelegatingRenderer();
/*     */     }
/* 470 */     return this.delegatingRenderer;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected ListCellRenderer createDefaultCellRenderer()
/*     */   {
/* 480 */     return new DefaultListRenderer();
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
/*     */   public ListCellRenderer getRenderer()
/*     */   {
/* 498 */     return getDelegatingRenderer();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public ListCellRenderer getWrappedRenderer()
/*     */   {
/* 509 */     return getDelegatingRenderer().getDelegateRenderer();
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
/*     */   public void setRenderer(ListCellRenderer renderer)
/*     */   {
/* 529 */     ListCellRenderer oldValue = super.getRenderer();
/* 530 */     getDelegatingRenderer().setDelegateRenderer(renderer);
/* 531 */     super.setRenderer(this.delegatingRenderer);
/*     */     
/* 533 */     if (oldValue == this.delegatingRenderer) {
/* 534 */       firePropertyChange("renderer", null, this.delegatingRenderer);
/*     */     }
/*     */   }
/*     */   
/*     */   public boolean isUseHighlightersForCurrentValue() {
/* 539 */     return this.useHighlightersForCurrentValue;
/*     */   }
/*     */   
/*     */   public void setUseHighlightersForCurrentValue(boolean useHighlightersForCurrentValue) {
/* 543 */     boolean oldValue = isUseHighlightersForCurrentValue();
/* 544 */     this.useHighlightersForCurrentValue = useHighlightersForCurrentValue;
/* 545 */     firePropertyChange("useHighlightersForCurrentValue", oldValue, isUseHighlightersForCurrentValue());
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private CompoundHighlighter getCompoundHighlighter()
/*     */   {
/* 557 */     return this.compoundHighlighter;
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
/*     */   private void setCompoundHighlighter(CompoundHighlighter pipeline)
/*     */   {
/* 577 */     CompoundHighlighter old = getCompoundHighlighter();
/* 578 */     if (old != null) {
/* 579 */       old.removeChangeListener(getHighlighterChangeListener());
/*     */     }
/* 581 */     this.compoundHighlighter = pipeline;
/* 582 */     if (this.compoundHighlighter != null) {
/* 583 */       this.compoundHighlighter.addChangeListener(getHighlighterChangeListener());
/*     */     }
/*     */     
/*     */ 
/* 587 */     firePropertyChange("highlighters", old, getCompoundHighlighter());
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
/*     */   public void setHighlighters(Highlighter... highlighters)
/*     */   {
/* 604 */     Contract.asNotNull(highlighters, "highlighters cannot be null or contain null");
/*     */     
/* 606 */     CompoundHighlighter pipeline = null;
/* 607 */     if (highlighters.length > 0) {
/* 608 */       pipeline = new CompoundHighlighter(highlighters);
/*     */     }
/*     */     
/* 611 */     setCompoundHighlighter(pipeline);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public Highlighter[] getHighlighters()
/*     */   {
/* 622 */     return getCompoundHighlighter() != null ? getCompoundHighlighter().getHighlighters() : CompoundHighlighter.EMPTY_HIGHLIGHTERS;
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
/*     */   public void addHighlighter(Highlighter highlighter)
/*     */   {
/* 639 */     CompoundHighlighter pipeline = getCompoundHighlighter();
/* 640 */     if (pipeline == null) {
/* 641 */       setCompoundHighlighter(new CompoundHighlighter(new Highlighter[] { highlighter }));
/*     */     } else {
/* 643 */       pipeline.addHighlighter(highlighter);
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
/*     */   public void removeHighlighter(Highlighter highlighter)
/*     */   {
/* 659 */     if (getCompoundHighlighter() == null) {
/* 660 */       return;
/*     */     }
/* 662 */     getCompoundHighlighter().removeHighlighter(highlighter);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected ChangeListener getHighlighterChangeListener()
/*     */   {
/* 673 */     if (this.highlighterChangeListener == null) {
/* 674 */       this.highlighterChangeListener = createHighlighterChangeListener();
/*     */     }
/*     */     
/* 677 */     return this.highlighterChangeListener;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected ChangeListener createHighlighterChangeListener()
/*     */   {
/* 688 */     new ChangeListener()
/*     */     {
/*     */       public void stateChanged(ChangeEvent e) {
/* 691 */         JXComboBox.this.firePropertyChange("highlighters", null, JXComboBox.this.getHighlighters());
/* 692 */         JXComboBox.this.repaint();
/*     */       }
/*     */     };
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void updateUI()
/*     */   {
/* 704 */     super.updateUI();
/*     */     
/* 706 */     ListCellRenderer renderer = getRenderer();
/*     */     
/* 708 */     if ((renderer instanceof UIDependent)) {
/* 709 */       ((UIDependent)renderer).updateUI();
/* 710 */     } else if ((renderer instanceof Component)) {
/* 711 */       SwingUtilities.updateComponentTreeUI((Component)renderer);
/*     */     }
/*     */     
/* 714 */     if (this.compoundHighlighter != null) {
/* 715 */       this.compoundHighlighter.updateUI();
/*     */     }
/*     */   }
/*     */ }


/* Location:              E:\java\java学习\hutubill\lib\all.jar!\org\jdesktop\swingx\JXComboBox.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */
/*     */ package org.jdesktop.swingx.table;
/*     */ 
/*     */ import java.awt.Component;
/*     */ import java.beans.PropertyChangeEvent;
/*     */ import java.beans.PropertyChangeListener;
/*     */ import java.util.Comparator;
/*     */ import java.util.Hashtable;
/*     */ import javax.swing.DefaultCellEditor;
/*     */ import javax.swing.JComponent;
/*     */ import javax.swing.SwingUtilities;
/*     */ import javax.swing.event.ChangeEvent;
/*     */ import javax.swing.event.ChangeListener;
/*     */ import javax.swing.table.TableCellEditor;
/*     */ import javax.swing.table.TableCellRenderer;
/*     */ import javax.swing.table.TableColumn;
/*     */ import org.jdesktop.swingx.decorator.CompoundHighlighter;
/*     */ import org.jdesktop.swingx.decorator.Highlighter;
/*     */ import org.jdesktop.swingx.plaf.UIDependent;
/*     */ import org.jdesktop.swingx.renderer.AbstractRenderer;
/*     */ import org.jdesktop.swingx.renderer.ComponentProvider;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class TableColumnExt
/*     */   extends TableColumn
/*     */   implements UIDependent
/*     */ {
/* 111 */   protected boolean visible = true;
/*     */   
/*     */ 
/*     */   protected Object prototypeValue;
/*     */   
/*     */ 
/*     */   protected Comparator<?> comparator;
/*     */   
/*     */ 
/* 120 */   protected boolean sortable = true;
/*     */   
/* 122 */   protected boolean editable = true;
/*     */   
/*     */ 
/*     */   private String toolTipText;
/*     */   
/*     */ 
/*     */   protected Hashtable<Object, Object> clientProperties;
/*     */   
/*     */ 
/*     */   protected CompoundHighlighter compoundHighlighter;
/*     */   
/*     */ 
/*     */   private ChangeListener highlighterChangeListener;
/*     */   
/*     */ 
/*     */   private boolean ignoreHighlighterStateChange;
/*     */   
/*     */ 
/*     */   public TableColumnExt()
/*     */   {
/* 142 */     this(0);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public TableColumnExt(int modelIndex)
/*     */   {
/* 151 */     this(modelIndex, 75);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public TableColumnExt(int modelIndex, int width)
/*     */   {
/* 161 */     this(modelIndex, width, null, null);
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
/*     */   public TableColumnExt(int modelIndex, int width, TableCellRenderer cellRenderer, TableCellEditor cellEditor)
/*     */   {
/* 176 */     super(modelIndex, width, cellRenderer, cellEditor);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public TableColumnExt(TableColumnExt columnExt)
/*     */   {
/* 187 */     this(columnExt.getModelIndex(), columnExt.getWidth(), columnExt.getCellRenderer(), columnExt.getCellEditor());
/*     */     
/* 189 */     copyFrom(columnExt);
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
/*     */   public void setHighlighters(Highlighter... highlighters)
/*     */   {
/* 211 */     this.ignoreHighlighterStateChange = true;
/* 212 */     Highlighter[] old = getHighlighters();
/* 213 */     getCompoundHighlighter().setHighlighters(highlighters);
/* 214 */     firePropertyChange("highlighters", old, getHighlighters());
/* 215 */     this.ignoreHighlighterStateChange = false;
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
/* 226 */     return getCompoundHighlighter().getHighlighters();
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
/*     */   public void addHighlighter(Highlighter highlighter)
/*     */   {
/* 240 */     this.ignoreHighlighterStateChange = true;
/* 241 */     Highlighter[] old = getHighlighters();
/* 242 */     getCompoundHighlighter().addHighlighter(highlighter);
/* 243 */     firePropertyChange("highlighters", old, getHighlighters());
/* 244 */     this.ignoreHighlighterStateChange = false;
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
/*     */   public void removeHighlighter(Highlighter highlighter)
/*     */   {
/* 257 */     this.ignoreHighlighterStateChange = true;
/* 258 */     Highlighter[] old = getHighlighters();
/* 259 */     getCompoundHighlighter().removeHighlighter(highlighter);
/* 260 */     firePropertyChange("highlighters", old, getHighlighters());
/* 261 */     this.ignoreHighlighterStateChange = false;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected CompoundHighlighter getCompoundHighlighter()
/*     */   {
/* 271 */     if (this.compoundHighlighter == null) {
/* 272 */       this.compoundHighlighter = new CompoundHighlighter(new Highlighter[0]);
/* 273 */       this.compoundHighlighter.addChangeListener(getHighlighterChangeListener());
/*     */     }
/* 275 */     return this.compoundHighlighter;
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
/* 286 */     if (this.highlighterChangeListener == null) {
/* 287 */       this.highlighterChangeListener = createHighlighterChangeListener();
/*     */     }
/* 289 */     return this.highlighterChangeListener;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected ChangeListener createHighlighterChangeListener()
/*     */   {
/* 301 */     new ChangeListener() {
/*     */       public void stateChanged(ChangeEvent e) {
/* 303 */         if (TableColumnExt.this.ignoreHighlighterStateChange) return;
/* 304 */         TableColumnExt.this.firePropertyChange("highlighterStateChanged", Boolean.valueOf(false), Boolean.valueOf(true));
/*     */       }
/*     */     };
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
/*     */   public boolean getResizable()
/*     */   {
/* 322 */     return (super.getResizable()) && (getMinWidth() < getMaxWidth());
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
/*     */   public void setEditable(boolean editable)
/*     */   {
/* 338 */     boolean oldEditable = this.editable;
/* 339 */     this.editable = editable;
/* 340 */     firePropertyChange("editable", Boolean.valueOf(oldEditable), Boolean.valueOf(editable));
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
/*     */   public boolean isEditable()
/*     */   {
/* 354 */     return this.editable;
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
/*     */   public void setPrototypeValue(Object value)
/*     */   {
/* 371 */     Object oldPrototypeValue = this.prototypeValue;
/* 372 */     this.prototypeValue = value;
/* 373 */     firePropertyChange("prototypeValue", oldPrototypeValue, value);
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
/*     */   public Object getPrototypeValue()
/*     */   {
/* 388 */     return this.prototypeValue;
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
/*     */   public void setComparator(Comparator<?> comparator)
/*     */   {
/* 404 */     Comparator<?> old = getComparator();
/* 405 */     this.comparator = comparator;
/* 406 */     firePropertyChange("comparator", old, getComparator());
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public Comparator<?> getComparator()
/*     */   {
/* 417 */     return this.comparator;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setSortable(boolean sortable)
/*     */   {
/* 429 */     boolean old = isSortable();
/* 430 */     this.sortable = sortable;
/* 431 */     firePropertyChange("sortable", Boolean.valueOf(old), Boolean.valueOf(isSortable()));
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean isSortable()
/*     */   {
/* 442 */     return this.sortable;
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
/*     */   public void setToolTipText(String toolTipText)
/*     */   {
/* 455 */     String old = getToolTipText();
/* 456 */     this.toolTipText = toolTipText;
/* 457 */     firePropertyChange("toolTipText", old, getToolTipText());
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public String getToolTipText()
/*     */   {
/* 468 */     return this.toolTipText;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setTitle(String title)
/*     */   {
/* 478 */     setHeaderValue(title);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public String getTitle()
/*     */   {
/* 488 */     Object header = getHeaderValue();
/* 489 */     return header != null ? header.toString() : null;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setVisible(boolean visible)
/*     */   {
/* 501 */     boolean oldVisible = this.visible;
/* 502 */     this.visible = visible;
/* 503 */     firePropertyChange("visible", Boolean.valueOf(oldVisible), Boolean.valueOf(visible));
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
/*     */   public boolean isVisible()
/*     */   {
/* 517 */     return this.visible;
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
/*     */   public void putClientProperty(Object key, Object value)
/*     */   {
/* 540 */     if (key == null) {
/* 541 */       throw new IllegalArgumentException("null key");
/*     */     }
/* 543 */     if ((value == null) && (getClientProperty(key) == null)) {
/* 544 */       return;
/*     */     }
/*     */     
/* 547 */     Object old = getClientProperty(key);
/* 548 */     if (value == null) {
/* 549 */       getClientProperties().remove(key);
/*     */     }
/*     */     else {
/* 552 */       getClientProperties().put(key, value);
/*     */     }
/*     */     
/* 555 */     firePropertyChange(key.toString(), old, value);
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
/*     */   public Object getClientProperty(Object key)
/*     */   {
/* 570 */     return (key == null) || (this.clientProperties == null) ? null : this.clientProperties.get(key);
/*     */   }
/*     */   
/*     */   private Hashtable<Object, Object> getClientProperties()
/*     */   {
/* 575 */     if (this.clientProperties == null) {
/* 576 */       this.clientProperties = new Hashtable();
/*     */     }
/* 578 */     return this.clientProperties;
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
/*     */   protected void copyFrom(TableColumnExt original)
/*     */   {
/* 592 */     setEditable(original.isEditable());
/* 593 */     setHeaderValue(original.getHeaderValue());
/* 594 */     setToolTipText(original.getToolTipText());
/* 595 */     setIdentifier(original.getIdentifier());
/* 596 */     setMaxWidth(original.getMaxWidth());
/* 597 */     setMinWidth(original.getMinWidth());
/* 598 */     setPreferredWidth(original.getPreferredWidth());
/* 599 */     setPrototypeValue(original.getPrototypeValue());
/*     */     
/* 601 */     setResizable(original.isResizable);
/* 602 */     setVisible(original.isVisible());
/* 603 */     setSortable(original.isSortable());
/* 604 */     setComparator(original.getComparator());
/* 605 */     copyClientPropertiesFrom(original);
/*     */     
/* 607 */     if (original.compoundHighlighter != null) {
/* 608 */       setHighlighters(original.getHighlighters());
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected void copyClientPropertiesFrom(TableColumnExt original)
/*     */   {
/* 620 */     if (original.clientProperties == null) return;
/* 621 */     for (Object key : original.clientProperties.keySet()) {
/* 622 */       putClientProperty(key, original.getClientProperty(key));
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
/*     */   protected void firePropertyChange(String propertyName, Object oldValue, Object newValue)
/*     */   {
/* 641 */     if (((oldValue != null) && (!oldValue.equals(newValue))) || ((oldValue == null) && (newValue != null)))
/*     */     {
/* 643 */       PropertyChangeListener[] pcl = getPropertyChangeListeners();
/* 644 */       if ((pcl != null) && (pcl.length != 0)) {
/* 645 */         PropertyChangeEvent pce = new PropertyChangeEvent(this, propertyName, oldValue, newValue);
/*     */         
/*     */ 
/*     */ 
/* 649 */         for (int i = 0; i < pcl.length; i++) {
/* 650 */           pcl[i].propertyChange(pce);
/*     */         }
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
/*     */   public void updateUI()
/*     */   {
/* 664 */     updateHighlighterUI();
/* 665 */     updateRendererUI(getCellRenderer());
/* 666 */     updateRendererUI(getHeaderRenderer());
/* 667 */     updateEditorUI(getCellEditor());
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   private void updateEditorUI(TableCellEditor editor)
/*     */   {
/* 675 */     if (editor == null) { return;
/*     */     }
/* 677 */     if (((editor instanceof JComponent)) || ((editor instanceof DefaultCellEditor)))
/*     */     {
/* 679 */       return; }
/*     */     try {
/* 681 */       Component comp = editor.getTableCellEditorComponent(null, null, false, -1, -1);
/*     */       
/* 683 */       if (comp != null) {
/* 684 */         SwingUtilities.updateComponentTreeUI(comp);
/*     */       }
/*     */     }
/*     */     catch (Exception e) {}
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private void updateRendererUI(TableCellRenderer renderer)
/*     */   {
/* 696 */     if (renderer == null) { return;
/*     */     }
/* 698 */     if ((renderer instanceof JComponent)) {
/* 699 */       return;
/*     */     }
/* 701 */     Component comp = null;
/* 702 */     if ((renderer instanceof AbstractRenderer)) {
/* 703 */       comp = ((AbstractRenderer)renderer).getComponentProvider().getRendererComponent(null);
/*     */     } else {
/*     */       try {
/* 706 */         comp = renderer.getTableCellRendererComponent(null, null, false, false, -1, -1);
/*     */       }
/*     */       catch (Exception e) {}
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/* 714 */     if (comp != null) {
/* 715 */       SwingUtilities.updateComponentTreeUI(comp);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   private void updateHighlighterUI()
/*     */   {
/* 723 */     if (this.compoundHighlighter == null) return;
/* 724 */     this.compoundHighlighter.updateUI();
/*     */   }
/*     */ }


/* Location:              E:\java\java学习\hutubill\lib\all.jar!\org\jdesktop\swingx\table\TableColumnExt.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */
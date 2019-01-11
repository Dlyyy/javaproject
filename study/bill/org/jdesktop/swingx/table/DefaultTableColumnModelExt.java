/*     */ package org.jdesktop.swingx.table;
/*     */ 
/*     */ import java.beans.PropertyChangeEvent;
/*     */ import java.beans.PropertyChangeListener;
/*     */ import java.io.Serializable;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collections;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import java.util.Vector;
/*     */ import javax.swing.event.EventListenerList;
/*     */ import javax.swing.event.TableColumnModelListener;
/*     */ import javax.swing.table.DefaultTableColumnModel;
/*     */ import javax.swing.table.TableColumn;
/*     */ import org.jdesktop.swingx.event.TableColumnModelExtListener;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class DefaultTableColumnModelExt
/*     */   extends DefaultTableColumnModel
/*     */   implements TableColumnModelExt
/*     */ {
/*     */   private boolean isVisibilityChange;
/*  64 */   private List<TableColumn> initialColumns = new ArrayList();
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*  70 */   private List<TableColumn> currentColumns = new ArrayList();
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*  76 */   private VisibilityListener visibilityListener = new VisibilityListener(null);
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public List<TableColumn> getColumns(boolean includeHidden)
/*     */   {
/*  91 */     if (includeHidden) {
/*  92 */       return new ArrayList(this.initialColumns);
/*     */     }
/*  94 */     return Collections.list(getColumns());
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public int getColumnCount(boolean includeHidden)
/*     */   {
/* 101 */     if (includeHidden) {
/* 102 */       return this.initialColumns.size();
/*     */     }
/* 104 */     return getColumnCount();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public TableColumnExt getColumnExt(Object identifier)
/*     */   {
/* 111 */     for (Iterator<TableColumn> iter = this.initialColumns.iterator(); iter.hasNext();) {
/* 112 */       TableColumn column = (TableColumn)iter.next();
/* 113 */       if (((column instanceof TableColumnExt)) && (identifier.equals(column.getIdentifier()))) {
/* 114 */         return (TableColumnExt)column;
/*     */       }
/*     */     }
/* 117 */     return null;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public TableColumnExt getColumnExt(int columnIndex)
/*     */   {
/* 124 */     TableColumn column = getColumn(columnIndex);
/* 125 */     if ((column instanceof TableColumnExt)) {
/* 126 */       return (TableColumnExt)column;
/*     */     }
/* 128 */     return null;
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
/*     */   public boolean isRemovedToInvisibleEvent(int oldIndex)
/*     */   {
/* 141 */     return this.isVisibilityChange;
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
/*     */   public boolean isAddedFromInvisibleEvent(int newIndex)
/*     */   {
/* 154 */     return this.isVisibilityChange;
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
/*     */   public void removeColumn(TableColumn column)
/*     */   {
/* 167 */     if ((column instanceof TableColumnExt)) {
/* 168 */       ((TableColumnExt)column).setVisible(true);
/* 169 */       ((TableColumnExt)column).removePropertyChangeListener(this.visibilityListener);
/*     */     }
/* 171 */     this.currentColumns.remove(column);
/* 172 */     this.initialColumns.remove(column);
/*     */     
/* 174 */     super.removeColumn(column);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void addColumn(TableColumn aColumn)
/*     */   {
/* 186 */     boolean oldVisible = true;
/*     */     
/* 188 */     if ((aColumn instanceof TableColumnExt)) {
/* 189 */       TableColumnExt xColumn = (TableColumnExt)aColumn;
/* 190 */       oldVisible = xColumn.isVisible();
/* 191 */       xColumn.setVisible(true);
/* 192 */       xColumn.addPropertyChangeListener(this.visibilityListener);
/*     */     }
/*     */     
/* 195 */     this.currentColumns.add(aColumn);
/* 196 */     this.initialColumns.add(aColumn);
/*     */     
/* 198 */     super.addColumn(aColumn);
/* 199 */     if ((aColumn instanceof TableColumnExt))
/*     */     {
/* 201 */       ((TableColumnExt)aColumn).setVisible(oldVisible);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void moveColumn(int columnIndex, int newIndex)
/*     */   {
/* 213 */     if (columnIndex != newIndex) {
/* 214 */       updateCurrentColumns(columnIndex, newIndex);
/*     */     }
/* 216 */     super.moveColumn(columnIndex, newIndex);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private void updateCurrentColumns(int oldIndex, int newIndex)
/*     */   {
/* 226 */     TableColumn movedColumn = (TableColumn)this.tableColumns.elementAt(oldIndex);
/* 227 */     int oldPosition = this.currentColumns.indexOf(movedColumn);
/* 228 */     TableColumn targetColumn = (TableColumn)this.tableColumns.elementAt(newIndex);
/* 229 */     int newPosition = this.currentColumns.indexOf(targetColumn);
/* 230 */     this.currentColumns.remove(oldPosition);
/* 231 */     this.currentColumns.add(newPosition, movedColumn);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected void moveToInvisible(TableColumnExt col)
/*     */   {
/* 243 */     this.isVisibilityChange = true;
/* 244 */     super.removeColumn(col);
/* 245 */     this.isVisibilityChange = false;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected void moveToVisible(TableColumnExt col)
/*     */   {
/* 257 */     this.isVisibilityChange = true;
/*     */     
/*     */ 
/*     */ 
/* 261 */     super.addColumn(col);
/*     */     
/*     */ 
/* 264 */     Integer addIndex = Integer.valueOf(this.currentColumns.indexOf(col));
/* 265 */     for (int i = 0; i < getColumnCount() - 1; i++) {
/* 266 */       TableColumn tableCol = getColumn(i);
/* 267 */       int actualPosition = this.currentColumns.indexOf(tableCol);
/* 268 */       if (actualPosition > addIndex.intValue()) {
/* 269 */         super.moveColumn(getColumnCount() - 1, i);
/* 270 */         break;
/*     */       }
/*     */     }
/* 273 */     this.isVisibilityChange = false;
/*     */   }
/*     */   
/*     */ 
/*     */   private class VisibilityListener
/*     */     implements PropertyChangeListener, Serializable
/*     */   {
/*     */     private VisibilityListener() {}
/*     */     
/*     */     public void propertyChange(PropertyChangeEvent evt)
/*     */     {
/* 284 */       if ("visible".equals(evt.getPropertyName())) {
/* 285 */         TableColumnExt columnExt = (TableColumnExt)evt.getSource();
/*     */         
/* 287 */         if (columnExt.isVisible()) {
/* 288 */           DefaultTableColumnModelExt.this.moveToVisible(columnExt);
/* 289 */           DefaultTableColumnModelExt.this.fireColumnPropertyChange(evt);
/*     */         } else {
/* 291 */           DefaultTableColumnModelExt.this.moveToInvisible(columnExt);
/*     */         }
/* 293 */       } else if (!((TableColumnExt)evt.getSource()).isVisible()) {
/* 294 */         DefaultTableColumnModelExt.this.fireColumnPropertyChange(evt);
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
/*     */   protected EventListenerList getEventListenerList()
/*     */   {
/* 307 */     return this.listenerList;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void propertyChange(PropertyChangeEvent evt)
/*     */   {
/* 317 */     super.propertyChange(evt);
/* 318 */     fireColumnPropertyChange(evt);
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
/*     */   protected void fireColumnPropertyChange(PropertyChangeEvent evt)
/*     */   {
/* 331 */     Object[] listeners = this.listenerList.getListenerList();
/*     */     
/*     */ 
/* 334 */     for (int i = listeners.length - 2; i >= 0; i -= 2) {
/* 335 */       if (listeners[i] == TableColumnModelExtListener.class) {
/* 336 */         ((TableColumnModelExtListener)listeners[(i + 1)]).columnPropertyChange(evt);
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
/*     */ 
/*     */   public void addColumnModelListener(TableColumnModelListener x)
/*     */   {
/* 353 */     super.addColumnModelListener(x);
/* 354 */     if ((x instanceof TableColumnModelExtListener)) {
/* 355 */       this.listenerList.add(TableColumnModelExtListener.class, (TableColumnModelExtListener)x);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void removeColumnModelListener(TableColumnModelListener x)
/*     */   {
/* 367 */     super.removeColumnModelListener(x);
/* 368 */     if ((x instanceof TableColumnModelExtListener)) {
/* 369 */       this.listenerList.remove(TableColumnModelExtListener.class, (TableColumnModelExtListener)x);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public TableColumnModelExtListener[] getTableColumnModelExtListeners()
/*     */   {
/* 377 */     return (TableColumnModelExtListener[])this.listenerList.getListeners(TableColumnModelExtListener.class);
/*     */   }
/*     */ }


/* Location:              E:\java\java学习\hutubill\lib\all.jar!\org\jdesktop\swingx\table\DefaultTableColumnModelExt.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */
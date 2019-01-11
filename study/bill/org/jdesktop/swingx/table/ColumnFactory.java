/*     */ package org.jdesktop.swingx.table;
/*     */ 
/*     */ import java.awt.Component;
/*     */ import java.awt.Dimension;
/*     */ import javax.swing.table.JTableHeader;
/*     */ import javax.swing.table.TableCellRenderer;
/*     */ import javax.swing.table.TableColumn;
/*     */ import javax.swing.table.TableColumnModel;
/*     */ import javax.swing.table.TableModel;
/*     */ import org.jdesktop.swingx.JXTable;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class ColumnFactory
/*     */ {
/*     */   private static ColumnFactory columnFactory;
/*  89 */   private int packMargin = 4;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static synchronized ColumnFactory getInstance()
/*     */   {
/*  98 */     if (columnFactory == null) {
/*  99 */       columnFactory = new ColumnFactory();
/*     */     }
/* 101 */     return columnFactory;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static synchronized void setInstance(ColumnFactory factory)
/*     */   {
/* 113 */     columnFactory = factory;
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
/*     */   public TableColumnExt createAndConfigureTableColumn(TableModel model, int modelIndex)
/*     */   {
/* 132 */     TableColumnExt column = createTableColumn(modelIndex);
/* 133 */     if (column != null) {
/* 134 */       configureTableColumn(model, column);
/*     */     }
/* 136 */     return column;
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
/*     */   public TableColumnExt createTableColumn(int modelIndex)
/*     */   {
/* 152 */     return new TableColumnExt(modelIndex);
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
/*     */   public void configureTableColumn(TableModel model, TableColumnExt columnExt)
/*     */   {
/* 174 */     if ((columnExt.getModelIndex() < 0) || (columnExt.getModelIndex() >= model.getColumnCount()))
/*     */     {
/* 176 */       throw new IllegalStateException("column must have valid modelIndex"); }
/* 177 */     columnExt.setHeaderValue(model.getColumnName(columnExt.getModelIndex()));
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void configureColumnWidths(JXTable table, TableColumnExt columnExt)
/*     */   {
/* 212 */     int prefWidth = 75 - table.getColumnMargin();
/* 213 */     int prototypeWidth = calcPrototypeWidth(table, columnExt);
/* 214 */     if (prototypeWidth > 0) {
/* 215 */       prefWidth = prototypeWidth;
/*     */     }
/* 217 */     int headerWidth = calcHeaderWidth(table, columnExt);
/* 218 */     prefWidth = Math.max(prefWidth, headerWidth);
/* 219 */     prefWidth += table.getColumnModel().getColumnMargin();
/* 220 */     columnExt.setPreferredWidth(prefWidth);
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
/*     */   public int getPreferredScrollableViewportWidth(JXTable table)
/*     */   {
/* 236 */     int w = 0;
/*     */     int count;
/* 238 */     int count; if (table.getVisibleColumnCount() < 0) {
/* 239 */       count = table.getColumnCount();
/*     */     } else {
/* 241 */       count = Math.min(table.getColumnCount(), table.getVisibleColumnCount());
/*     */     }
/* 243 */     for (int i = 0; i < count; i++)
/*     */     {
/*     */ 
/* 246 */       w += table.getColumn(i).getPreferredWidth();
/*     */     }
/* 248 */     if (count < table.getVisibleColumnCount()) {
/* 249 */       w += (table.getVisibleColumnCount() - count) * 75;
/*     */     }
/* 251 */     return w;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected int calcHeaderWidth(JXTable table, TableColumnExt columnExt)
/*     */   {
/* 263 */     int prototypeWidth = -1;
/*     */     
/* 265 */     TableCellRenderer renderer = getHeaderRenderer(table, columnExt);
/* 266 */     if (renderer != null) {
/* 267 */       Component comp = renderer.getTableCellRendererComponent(table, columnExt.getHeaderValue(), false, false, -1, -1);
/*     */       
/*     */ 
/* 270 */       prototypeWidth = comp.getPreferredSize().width;
/*     */     }
/* 272 */     return prototypeWidth;
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
/*     */   protected int calcPrototypeWidth(JXTable table, TableColumnExt columnExt)
/*     */   {
/* 285 */     int prototypeWidth = -1;
/* 286 */     Object prototypeValue = columnExt.getPrototypeValue();
/* 287 */     if (prototypeValue != null)
/*     */     {
/* 289 */       TableCellRenderer cellRenderer = getCellRenderer(table, columnExt);
/* 290 */       Component comp = cellRenderer.getTableCellRendererComponent(table, prototypeValue, false, false, 0, -1);
/*     */       
/* 292 */       prototypeWidth = comp.getPreferredSize().width;
/*     */     }
/* 294 */     return prototypeWidth;
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
/*     */   protected TableCellRenderer getCellRenderer(JXTable table, TableColumnExt columnExt)
/*     */   {
/* 308 */     int viewIndex = table.convertColumnIndexToView(columnExt.getModelIndex());
/*     */     
/* 310 */     if (viewIndex >= 0)
/*     */     {
/*     */ 
/* 313 */       return table.getCellRenderer(0, viewIndex);
/*     */     }
/*     */     
/*     */ 
/*     */ 
/* 318 */     TableCellRenderer renderer = columnExt.getCellRenderer();
/* 319 */     if (renderer == null) {
/* 320 */       renderer = table.getDefaultRenderer(table.getModel().getColumnClass(columnExt.getModelIndex()));
/*     */     }
/* 322 */     return renderer;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected TableCellRenderer getHeaderRenderer(JXTable table, TableColumnExt columnExt)
/*     */   {
/* 333 */     TableCellRenderer renderer = columnExt.getHeaderRenderer();
/* 334 */     if (renderer == null) {
/* 335 */       JTableHeader header = table.getTableHeader();
/* 336 */       if (header != null) {
/* 337 */         renderer = header.getDefaultRenderer();
/*     */       }
/*     */     }
/*     */     
/*     */ 
/* 342 */     return renderer;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void packColumn(JXTable table, TableColumnExt columnExt, int margin, int max)
/*     */   {
/* 381 */     if (!columnExt.isVisible()) {
/* 382 */       throw new IllegalStateException("column must be visible to pack");
/*     */     }
/* 384 */     int column = table.convertColumnIndexToView(columnExt.getModelIndex());
/* 385 */     int width = 0;
/* 386 */     TableCellRenderer headerRenderer = getHeaderRenderer(table, columnExt);
/* 387 */     if (headerRenderer != null) {
/* 388 */       Component comp = headerRenderer.getTableCellRendererComponent(table, columnExt.getHeaderValue(), false, false, 0, column);
/*     */       
/* 390 */       width = comp.getPreferredSize().width;
/*     */     }
/*     */     
/*     */ 
/*     */ 
/* 395 */     TableCellRenderer renderer = getCellRenderer(table, columnExt);
/* 396 */     for (int r = 0; r < getRowCount(table); r++)
/*     */     {
/* 398 */       Component comp = table.prepareRenderer(renderer, r, column);
/*     */       
/*     */ 
/* 401 */       width = Math.max(width, comp.getPreferredSize().width);
/*     */     }
/* 403 */     if (margin < 0) {
/* 404 */       margin = getDefaultPackMargin();
/*     */     }
/* 406 */     width += 2 * margin;
/*     */     
/*     */ 
/* 409 */     if ((max != -1) && (width > max)) {
/* 410 */       width = max;
/*     */     }
/* 412 */     columnExt.setPreferredWidth(width);
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
/*     */   protected int getRowCount(JXTable table)
/*     */   {
/* 433 */     return table.getRowCount();
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
/*     */   public int getDefaultPackMargin()
/*     */   {
/* 446 */     return this.packMargin;
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
/*     */   public void setDefaultPackMargin(int margin)
/*     */   {
/* 462 */     this.packMargin = margin;
/*     */   }
/*     */ }


/* Location:              E:\java\java学习\hutubill\lib\all.jar!\org\jdesktop\swingx\table\ColumnFactory.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */
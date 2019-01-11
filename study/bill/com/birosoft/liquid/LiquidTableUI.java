/*     */ package com.birosoft.liquid;
/*     */ 
/*     */ import java.awt.Color;
/*     */ import java.awt.Component;
/*     */ import java.awt.ComponentOrientation;
/*     */ import java.awt.Container;
/*     */ import java.awt.Dimension;
/*     */ import java.awt.Graphics;
/*     */ import java.awt.Point;
/*     */ import java.awt.Rectangle;
/*     */ import javax.swing.CellRendererPane;
/*     */ import javax.swing.JComponent;
/*     */ import javax.swing.JTable;
/*     */ import javax.swing.plaf.ComponentUI;
/*     */ import javax.swing.plaf.basic.BasicTableUI;
/*     */ import javax.swing.table.JTableHeader;
/*     */ import javax.swing.table.TableCellRenderer;
/*     */ import javax.swing.table.TableColumn;
/*     */ import javax.swing.table.TableColumnModel;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class LiquidTableUI
/*     */   extends BasicTableUI
/*     */ {
/*     */   private Color defaultBackground;
/*     */   
/*     */   public static ComponentUI createUI(JComponent c)
/*     */   {
/*  39 */     return new LiquidTableUI();
/*     */   }
/*     */   
/*     */   public void installUI(JComponent c)
/*     */   {
/*  44 */     super.installUI(c);
/*     */   }
/*     */   
/*     */   public void uninstallUI(JComponent c)
/*     */   {
/*  49 */     super.uninstallUI(c);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void paint(Graphics g, JComponent c)
/*     */   {
/*  57 */     if ((this.table.getRowCount() <= 0) || (this.table.getColumnCount() <= 0)) {
/*  58 */       return;
/*     */     }
/*     */     
/*  61 */     if ((LiquidLookAndFeel.defaultRowBackgroundMode & this.defaultBackground == null))
/*     */     {
/*  63 */       this.defaultBackground = this.table.getBackground();
/*     */       
/*  65 */       if (!LiquidLookAndFeel.showTableGrids) {
/*  66 */         this.table.setIntercellSpacing(new Dimension());
/*     */       }
/*     */     }
/*     */     
/*  70 */     Rectangle clip = g.getClipBounds();
/*  71 */     Point upperLeft = clip.getLocation();
/*  72 */     Point lowerRight = new Point(clip.x + clip.width - 1, clip.y + clip.height - 1);
/*     */     
/*  74 */     int rMin = this.table.rowAtPoint(upperLeft);
/*  75 */     int rMax = this.table.rowAtPoint(lowerRight);
/*     */     
/*  77 */     if (rMin == -1) {
/*  78 */       rMin = 0;
/*     */     }
/*     */     
/*  81 */     if (rMax == -1) {
/*  82 */       rMax = this.table.getRowCount() - 1;
/*     */     }
/*     */     
/*  85 */     boolean ltr = this.table.getComponentOrientation().isLeftToRight();
/*  86 */     int cMin = this.table.columnAtPoint(ltr ? upperLeft : lowerRight);
/*  87 */     int cMax = this.table.columnAtPoint(ltr ? lowerRight : upperLeft);
/*     */     
/*  89 */     if (cMin == -1) {
/*  90 */       cMin = 0;
/*     */     }
/*     */     
/*  93 */     if (cMax == -1) {
/*  94 */       cMax = this.table.getColumnCount() - 1;
/*     */     }
/*     */     
/*  97 */     if (LiquidLookAndFeel.showTableGrids) {
/*  98 */       paintGrid(g, rMin, rMax, cMin, cMax);
/*     */     }
/*     */     
/* 101 */     paintCells(g, rMin, rMax, cMin, cMax);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private void paintGrid(Graphics g, int rMin, int rMax, int cMin, int cMax)
/*     */   {
/* 112 */     g.setColor(this.table.getGridColor());
/*     */     
/* 114 */     Rectangle minCell = this.table.getCellRect(rMin, cMin, true);
/* 115 */     Rectangle maxCell = this.table.getCellRect(rMax, cMax, true);
/* 116 */     Rectangle damagedArea = minCell.union(maxCell);
/*     */     
/* 118 */     if (this.table.getShowHorizontalLines()) {
/* 119 */       int tableWidth = damagedArea.x + damagedArea.width;
/* 120 */       int y = damagedArea.y;
/*     */       
/* 122 */       for (int row = rMin; row <= rMax; row++) {
/* 123 */         y += this.table.getRowHeight(row);
/* 124 */         g.drawLine(damagedArea.x, y - 1, tableWidth - 1, y - 1);
/*     */       }
/*     */     }
/*     */     
/* 128 */     if (this.table.getShowVerticalLines()) {
/* 129 */       TableColumnModel cm = this.table.getColumnModel();
/* 130 */       int tableHeight = damagedArea.y + damagedArea.height;
/*     */       
/*     */ 
/* 133 */       if (this.table.getComponentOrientation().isLeftToRight()) {
/* 134 */         int x = damagedArea.x;
/*     */         
/* 136 */         for (int column = cMin; column <= cMax; column++) {
/* 137 */           int w = cm.getColumn(column).getWidth();
/* 138 */           x += w;
/* 139 */           g.drawLine(x - 1, 0, x - 1, tableHeight - 1);
/*     */         }
/*     */       } else {
/* 142 */         int x = damagedArea.x + damagedArea.width;
/*     */         
/* 144 */         for (int column = cMin; column < cMax; column++) {
/* 145 */           int w = cm.getColumn(column).getWidth();
/* 146 */           x -= w;
/* 147 */           g.drawLine(x - 1, 0, x - 1, tableHeight - 1);
/*     */         }
/*     */         
/* 150 */         x -= cm.getColumn(cMax).getWidth();
/* 151 */         g.drawLine(x, 0, x, tableHeight - 1);
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   private int viewIndexForColumn(TableColumn aColumn) {
/* 157 */     TableColumnModel cm = this.table.getColumnModel();
/*     */     
/* 159 */     for (int column = 0; column < cm.getColumnCount(); column++) {
/* 160 */       if (cm.getColumn(column) == aColumn) {
/* 161 */         return column;
/*     */       }
/*     */     }
/*     */     
/* 165 */     return -1;
/*     */   }
/*     */   
/*     */   private void paintCells(Graphics g, int rMin, int rMax, int cMin, int cMax) {
/* 169 */     JTableHeader header = this.table.getTableHeader();
/* 170 */     TableColumn draggedColumn = header == null ? null : header.getDraggedColumn();
/*     */     
/*     */ 
/* 173 */     TableColumnModel cm = this.table.getColumnModel();
/* 174 */     int columnMargin = cm.getColumnMargin();
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 180 */     if (this.table.getComponentOrientation().isLeftToRight()) {
/* 181 */       for (int row = rMin; row <= rMax; row++) {
/* 182 */         Rectangle cellRect = this.table.getCellRect(row, cMin, false);
/*     */         
/* 184 */         for (int column = cMin; column <= cMax; column++) {
/* 185 */           TableColumn aColumn = cm.getColumn(column);
/* 186 */           int columnWidth = aColumn.getWidth();
/* 187 */           cellRect.width = (columnWidth - columnMargin);
/*     */           
/* 189 */           if (aColumn != draggedColumn) {
/* 190 */             paintCell(g, cellRect, row, column);
/*     */           }
/*     */           
/* 193 */           cellRect.x += columnWidth;
/*     */         }
/*     */       }
/*     */     } else {
/* 197 */       for (int row = rMin; row <= rMax; row++) {
/* 198 */         Rectangle cellRect = this.table.getCellRect(row, cMin, false);
/* 199 */         TableColumn aColumn = cm.getColumn(cMin);
/*     */         
/* 201 */         if (aColumn != draggedColumn) {
/* 202 */           int columnWidth = aColumn.getWidth();
/* 203 */           cellRect.width = (columnWidth - columnMargin);
/* 204 */           paintCell(g, cellRect, row, cMin);
/*     */         }
/*     */         
/* 207 */         for (int column = cMin + 1; column <= cMax; column++) {
/* 208 */           aColumn = cm.getColumn(column);
/* 209 */           int columnWidth = aColumn.getWidth();
/* 210 */           cellRect.width = (columnWidth - columnMargin);
/* 211 */           cellRect.x -= columnWidth;
/*     */           
/* 213 */           if (aColumn != draggedColumn) {
/* 214 */             paintCell(g, cellRect, row, column);
/*     */           }
/*     */         }
/*     */       }
/*     */     }
/*     */     
/*     */ 
/* 221 */     if (draggedColumn != null) {
/* 222 */       paintDraggedArea(g, rMin, rMax, draggedColumn, header.getDraggedDistance());
/*     */     }
/*     */     
/*     */ 
/*     */ 
/* 227 */     this.rendererPane.removeAll();
/*     */   }
/*     */   
/*     */   private void paintDraggedArea(Graphics g, int rMin, int rMax, TableColumn draggedColumn, int distance)
/*     */   {
/* 232 */     int draggedColumnIndex = viewIndexForColumn(draggedColumn);
/*     */     
/* 234 */     Rectangle minCell = this.table.getCellRect(rMin, draggedColumnIndex, true);
/* 235 */     Rectangle maxCell = this.table.getCellRect(rMax, draggedColumnIndex, true);
/*     */     
/* 237 */     Rectangle vacatedColumnRect = minCell.union(maxCell);
/*     */     
/*     */ 
/* 240 */     g.setColor(this.table.getParent().getBackground());
/* 241 */     g.fillRect(vacatedColumnRect.x, vacatedColumnRect.y, vacatedColumnRect.width, vacatedColumnRect.height);
/*     */     
/*     */ 
/*     */ 
/* 245 */     vacatedColumnRect.x += distance;
/*     */     
/*     */ 
/* 248 */     g.setColor(this.table.getBackground());
/* 249 */     g.fillRect(vacatedColumnRect.x, vacatedColumnRect.y, vacatedColumnRect.width, vacatedColumnRect.height);
/*     */     
/*     */ 
/*     */ 
/* 253 */     if (this.table.getShowVerticalLines()) {
/* 254 */       g.setColor(this.table.getGridColor());
/*     */       
/* 256 */       int x1 = vacatedColumnRect.x;
/* 257 */       int y1 = vacatedColumnRect.y;
/* 258 */       int x2 = x1 + vacatedColumnRect.width - 1;
/* 259 */       int y2 = y1 + vacatedColumnRect.height - 1;
/*     */       
/*     */ 
/* 262 */       g.drawLine(x1 - 1, y1, x1 - 1, y2);
/*     */       
/*     */ 
/* 265 */       g.drawLine(x2, y1, x2, y2);
/*     */     }
/*     */     
/* 268 */     for (int row = rMin; row <= rMax; row++)
/*     */     {
/* 270 */       Rectangle r = this.table.getCellRect(row, draggedColumnIndex, false);
/* 271 */       r.x += distance;
/* 272 */       paintCell(g, r, row, draggedColumnIndex);
/*     */       
/*     */ 
/* 275 */       if (this.table.getShowHorizontalLines()) {
/* 276 */         g.setColor(this.table.getGridColor());
/*     */         
/* 278 */         Rectangle rcr = this.table.getCellRect(row, draggedColumnIndex, true);
/* 279 */         rcr.x += distance;
/*     */         
/* 281 */         int x1 = rcr.x;
/* 282 */         int y1 = rcr.y;
/* 283 */         int x2 = x1 + rcr.width - 1;
/* 284 */         int y2 = y1 + rcr.height - 1;
/* 285 */         g.drawLine(x1, y2, x2, y2);
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   private void paintCell(Graphics g, Rectangle cellRect, int row, int column) {
/* 291 */     if ((this.table.isEditing()) && (this.table.getEditingRow() == row) && (this.table.getEditingColumn() == column))
/*     */     {
/* 293 */       Component component = this.table.getEditorComponent();
/* 294 */       component.setBounds(cellRect);
/* 295 */       component.validate();
/*     */     } else {
/* 297 */       TableCellRenderer renderer = this.table.getCellRenderer(row, column);
/* 298 */       Component component = this.table.prepareRenderer(renderer, row, column);
/*     */       
/* 300 */       if ((LiquidLookAndFeel.defaultRowBackgroundMode & !this.table.isCellSelected(row, column)))
/*     */       {
/* 302 */         if (row % 2 == 0) {
/* 303 */           if (LiquidLookAndFeel.getDesktopColor().equals(component.getBackground())) {
/* 304 */             component.setBackground(this.defaultBackground);
/*     */           }
/*     */         }
/* 307 */         else if (this.defaultBackground.equals(component.getBackground())) {
/* 308 */           component.setBackground(LiquidLookAndFeel.getDesktopColor());
/*     */         }
/*     */       }
/*     */       
/*     */ 
/* 313 */       this.rendererPane.paintComponent(g, component, this.table, cellRect.x, cellRect.y, cellRect.width, cellRect.height, true);
/*     */     }
/*     */   }
/*     */ }


/* Location:              E:\java\java学习\hutubill\lib\all.jar!\com\birosoft\liquid\LiquidTableUI.class
 * Java compiler version: 4 (48.0)
 * JD-Core Version:       0.7.1
 */
/*     */ package org.jdesktop.swingx.rollover;
/*     */ 
/*     */ import java.awt.Cursor;
/*     */ import java.awt.Point;
/*     */ import java.awt.Rectangle;
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ import javax.swing.JTable;
/*     */ import javax.swing.ListSelectionModel;
/*     */ import javax.swing.table.TableCellRenderer;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class TableRolloverController<T extends JTable>
/*     */   extends RolloverController<T>
/*     */ {
/*     */   private Cursor oldCursor;
/*     */   
/*     */   protected void rollover(Point oldLocation, Point newLocation)
/*     */   {
/*  48 */     boolean paintOldRow = hasRow(oldLocation);
/*  49 */     boolean paintNewRow = hasRow(newLocation);
/*  50 */     if ((paintOldRow) && (paintNewRow) && 
/*  51 */       (oldLocation.y == newLocation.y))
/*     */     {
/*  53 */       paintOldRow = false;
/*  54 */       paintNewRow = false;
/*     */     }
/*     */     
/*     */ 
/*  58 */     boolean paintOldColumn = hasColumn(oldLocation);
/*  59 */     boolean paintNewColumn = hasColumn(newLocation);
/*  60 */     if ((paintOldColumn) && (paintNewColumn) && 
/*  61 */       (oldLocation.x == newLocation.x))
/*     */     {
/*  63 */       paintOldColumn = false;
/*  64 */       paintNewColumn = false;
/*     */     }
/*     */     
/*     */ 
/*  68 */     List<Rectangle> rectangles = getPaintRectangles(null, oldLocation, paintOldRow, paintOldColumn);
/*  69 */     rectangles = getPaintRectangles(rectangles, newLocation, paintNewRow, paintNewColumn);
/*  70 */     if (rectangles != null) {
/*  71 */       for (Rectangle rectangle : rectangles) {
/*  72 */         ((JTable)this.component).repaint(rectangle);
/*     */       }
/*     */     }
/*  75 */     setRolloverCursor(newLocation);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private List<Rectangle> getPaintRectangles(List<Rectangle> rectangles, Point cellLocation, boolean paintRow, boolean paintColumn)
/*     */   {
/*  87 */     if ((!paintRow) && (!paintColumn)) return rectangles;
/*  88 */     if (rectangles == null) {
/*  89 */       rectangles = new ArrayList();
/*     */     }
/*  91 */     Rectangle r = ((JTable)this.component).getCellRect(cellLocation.y, cellLocation.x, false);
/*     */     
/*  93 */     if (paintRow) {
/*  94 */       rectangles.add(new Rectangle(0, r.y, ((JTable)this.component).getWidth(), r.height));
/*     */     }
/*     */     
/*  97 */     if (paintColumn) {
/*  98 */       rectangles.add(new Rectangle(r.x, 0, r.width, ((JTable)this.component).getHeight()));
/*     */     }
/*     */     
/* 101 */     return rectangles;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private boolean hasColumn(Point cellLocation)
/*     */   {
/* 110 */     return (cellLocation != null) && (cellLocation.x >= 0);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private boolean hasRow(Point cellLocation)
/*     */   {
/* 119 */     return (cellLocation != null) && (cellLocation.y >= 0);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   protected boolean isClickable(Point location)
/*     */   {
/* 127 */     return (super.isClickable(location)) && (!((JTable)this.component).isCellEditable(location.y, location.x));
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   protected RolloverRenderer getRolloverRenderer(Point location, boolean prepare)
/*     */   {
/* 134 */     TableCellRenderer renderer = ((JTable)this.component).getCellRenderer(location.y, location.x);
/*     */     
/* 136 */     RolloverRenderer rollover = (renderer instanceof RolloverRenderer) ? (RolloverRenderer)renderer : null;
/*     */     
/* 138 */     if ((rollover != null) && (!rollover.isEnabled())) {
/* 139 */       rollover = null;
/*     */     }
/* 141 */     if ((rollover != null) && (prepare)) {
/* 142 */       ((JTable)this.component).prepareRenderer(renderer, location.y, location.x);
/*     */     }
/* 144 */     return rollover;
/*     */   }
/*     */   
/*     */   private void setRolloverCursor(Point location) {
/* 148 */     if (hasRollover(location)) {
/* 149 */       if (this.oldCursor == null) {
/* 150 */         this.oldCursor = ((JTable)this.component).getCursor();
/* 151 */         ((JTable)this.component).setCursor(Cursor.getPredefinedCursor(12));
/*     */       }
/*     */       
/*     */     }
/* 155 */     else if (this.oldCursor != null) {
/* 156 */       ((JTable)this.component).setCursor(this.oldCursor);
/* 157 */       this.oldCursor = null;
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   protected Point getFocusedCell()
/*     */   {
/* 165 */     int leadRow = ((JTable)this.component).getSelectionModel().getLeadSelectionIndex();
/* 166 */     int leadColumn = ((JTable)this.component).getColumnModel().getSelectionModel().getLeadSelectionIndex();
/*     */     
/* 168 */     return new Point(leadColumn, leadRow);
/*     */   }
/*     */ }


/* Location:              E:\java\java学习\hutubill\lib\all.jar!\org\jdesktop\swingx\rollover\TableRolloverController.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */
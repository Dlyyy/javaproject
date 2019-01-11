/*     */ package org.jdesktop.swingx.rollover;
/*     */ 
/*     */ import java.awt.Cursor;
/*     */ import java.awt.Point;
/*     */ import java.awt.Rectangle;
/*     */ import javax.swing.JList;
/*     */ import javax.swing.ListCellRenderer;
/*     */ import javax.swing.ListModel;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class ListRolloverController<T extends JList>
/*     */   extends RolloverController<T>
/*     */ {
/*     */   private Cursor oldCursor;
/*     */   
/*     */   protected void rollover(Point oldLocation, Point newLocation)
/*     */   {
/*  46 */     if (oldLocation != null) {
/*  47 */       Rectangle r = ((JList)this.component).getCellBounds(oldLocation.y, oldLocation.y);
/*     */       
/*  49 */       if (r != null) {
/*  50 */         ((JList)this.component).repaint(r);
/*     */       }
/*     */     }
/*  53 */     if (newLocation != null) {
/*  54 */       Rectangle r = ((JList)this.component).getCellBounds(newLocation.y, newLocation.y);
/*     */       
/*  56 */       if (r != null) {
/*  57 */         ((JList)this.component).repaint(r);
/*     */       }
/*     */     }
/*  60 */     setRolloverCursor(newLocation);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private void setRolloverCursor(Point location)
/*     */   {
/*  69 */     if (hasRollover(location)) {
/*  70 */       if (this.oldCursor == null) {
/*  71 */         this.oldCursor = ((JList)this.component).getCursor();
/*  72 */         ((JList)this.component).setCursor(Cursor.getPredefinedCursor(12));
/*     */       }
/*     */       
/*     */     }
/*  76 */     else if (this.oldCursor != null) {
/*  77 */       ((JList)this.component).setCursor(this.oldCursor);
/*  78 */       this.oldCursor = null;
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
/*     */   protected RolloverRenderer getRolloverRenderer(Point location, boolean prepare)
/*     */   {
/*  94 */     ListCellRenderer renderer = ((JList)this.component).getCellRenderer();
/*  95 */     RolloverRenderer rollover = (renderer instanceof RolloverRenderer) ? (RolloverRenderer)renderer : null;
/*     */     
/*  97 */     if ((rollover != null) && (!rollover.isEnabled())) {
/*  98 */       rollover = null;
/*     */     }
/* 100 */     if ((rollover != null) && (prepare)) {
/* 101 */       Object element = ((JList)this.component).getModel().getElementAt(location.y);
/* 102 */       renderer.getListCellRendererComponent((JList)this.component, element, location.y, false, true);
/*     */     }
/*     */     
/* 105 */     return rollover;
/*     */   }
/*     */   
/*     */   protected Point getFocusedCell()
/*     */   {
/* 110 */     int leadRow = ((JList)this.component).getLeadSelectionIndex();
/* 111 */     if (leadRow < 0)
/* 112 */       return null;
/* 113 */     return new Point(0, leadRow);
/*     */   }
/*     */ }


/* Location:              E:\java\java学习\hutubill\lib\all.jar!\org\jdesktop\swingx\rollover\ListRolloverController.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */
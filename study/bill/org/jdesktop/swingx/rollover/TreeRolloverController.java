/*     */ package org.jdesktop.swingx.rollover;
/*     */ 
/*     */ import java.awt.Cursor;
/*     */ import java.awt.Point;
/*     */ import java.awt.Rectangle;
/*     */ import javax.swing.JTree;
/*     */ import javax.swing.tree.TreeCellRenderer;
/*     */ import javax.swing.tree.TreePath;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class TreeRolloverController<T extends JTree>
/*     */   extends RolloverController<T>
/*     */ {
/*     */   private Cursor oldCursor;
/*     */   
/*     */   protected void rollover(Point oldLocation, Point newLocation)
/*     */   {
/*  49 */     if (oldLocation != null) {
/*  50 */       Rectangle r = ((JTree)this.component).getRowBounds(oldLocation.y);
/*  51 */       if (r != null) {
/*  52 */         r.x = 0;
/*  53 */         r.width = ((JTree)this.component).getWidth();
/*  54 */         ((JTree)this.component).repaint(r);
/*     */       }
/*     */     }
/*  57 */     if (newLocation != null) {
/*  58 */       Rectangle r = ((JTree)this.component).getRowBounds(newLocation.y);
/*  59 */       if (r != null) {
/*  60 */         r.x = 0;
/*  61 */         r.width = ((JTree)this.component).getWidth();
/*  62 */         ((JTree)this.component).repaint(r);
/*     */       }
/*     */     }
/*  65 */     setRolloverCursor(newLocation);
/*     */   }
/*     */   
/*     */   private void setRolloverCursor(Point location)
/*     */   {
/*  70 */     if (hasRollover(location)) {
/*  71 */       if (this.oldCursor == null) {
/*  72 */         this.oldCursor = ((JTree)this.component).getCursor();
/*  73 */         ((JTree)this.component).setCursor(Cursor.getPredefinedCursor(12));
/*     */       }
/*     */       
/*     */     }
/*  77 */     else if (this.oldCursor != null) {
/*  78 */       ((JTree)this.component).setCursor(this.oldCursor);
/*  79 */       this.oldCursor = null;
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   protected RolloverRenderer getRolloverRenderer(Point location, boolean prepare)
/*     */   {
/*  88 */     TreeCellRenderer renderer = ((JTree)this.component).getCellRenderer();
/*  89 */     RolloverRenderer rollover = (renderer instanceof RolloverRenderer) ? (RolloverRenderer)renderer : null;
/*     */     
/*  91 */     if ((rollover != null) && (!rollover.isEnabled())) {
/*  92 */       rollover = null;
/*     */     }
/*  94 */     if ((rollover != null) && (prepare)) {
/*  95 */       TreePath path = ((JTree)this.component).getPathForRow(location.y);
/*  96 */       Object element = path != null ? path.getLastPathComponent() : null;
/*  97 */       renderer.getTreeCellRendererComponent((JTree)this.component, element, false, false, false, location.y, false);
/*     */     }
/*     */     
/*     */ 
/* 101 */     return rollover;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   protected Point getFocusedCell()
/*     */   {
/* 108 */     return null;
/*     */   }
/*     */ }


/* Location:              E:\java\java学习\hutubill\lib\all.jar!\org\jdesktop\swingx\rollover\TreeRolloverController.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */
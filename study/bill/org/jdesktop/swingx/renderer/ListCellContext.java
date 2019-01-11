/*     */ package org.jdesktop.swingx.renderer;
/*     */ 
/*     */ import java.awt.Color;
/*     */ import javax.swing.JList;
/*     */ import javax.swing.JList.DropLocation;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class ListCellContext
/*     */   extends CellContext
/*     */ {
/*     */   public void installContext(JList component, Object value, int row, int column, boolean selected, boolean focused, boolean expanded, boolean leaf)
/*     */   {
/*  48 */     this.component = component;
/*  49 */     installState(value, row, column, selected, focused, expanded, leaf);
/*  50 */     this.dropOn = checkDropOnState();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   private boolean checkDropOnState()
/*     */   {
/*  57 */     if (getComponent() == null) {
/*  58 */       return false;
/*     */     }
/*  60 */     JList.DropLocation dropLocation = getComponent().getDropLocation();
/*  61 */     if ((dropLocation != null) && (!dropLocation.isInsert()) && (dropLocation.getIndex() == this.row))
/*     */     {
/*     */ 
/*  64 */       return true;
/*     */     }
/*  66 */     return false;
/*     */   }
/*     */   
/*     */ 
/*     */   public JList getComponent()
/*     */   {
/*  72 */     return (JList)super.getComponent();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   protected Color getSelectionBackground()
/*     */   {
/*  80 */     Color selection = null;
/*  81 */     if (isDropOn()) {
/*  82 */       selection = getDropCellBackground();
/*  83 */       if (selection != null) return selection;
/*     */     }
/*  85 */     return getComponent() != null ? getComponent().getSelectionBackground() : null;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   protected Color getSelectionForeground()
/*     */   {
/*  93 */     Color selection = null;
/*  94 */     if (isDropOn()) {
/*  95 */       selection = getDropCellForeground();
/*  96 */       if (selection != null) return selection;
/*     */     }
/*  98 */     return getComponent() != null ? getComponent().getSelectionForeground() : null;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   protected String getUIPrefix()
/*     */   {
/* 106 */     return "List.";
/*     */   }
/*     */ }


/* Location:              E:\java\java学习\hutubill\lib\all.jar!\org\jdesktop\swingx\renderer\ListCellContext.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */
/*     */ package org.jdesktop.swingx.renderer;
/*     */ 
/*     */ import java.awt.Color;
/*     */ import javax.swing.JTable;
/*     */ import javax.swing.JTable.DropLocation;
/*     */ import javax.swing.UIManager;
/*     */ import org.jdesktop.swingx.plaf.UIManagerExt;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class TableCellContext
/*     */   extends CellContext
/*     */ {
/*     */   public static final String HANDLE_ALTERNATE_ROW_BACKGROUND = "TableCellContext.handleAlternateRowBackground";
/*     */   
/*     */   public void installContext(JTable component, Object value, int row, int column, boolean selected, boolean focused, boolean expanded, boolean leaf)
/*     */   {
/*  56 */     this.component = component;
/*  57 */     installState(value, row, column, selected, focused, expanded, leaf);
/*  58 */     this.dropOn = checkDropOnState();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   private boolean checkDropOnState()
/*     */   {
/*  66 */     if ((getComponent() == null) || (!isValidRow()) || (!isValidColumn())) {
/*  67 */       return false;
/*     */     }
/*  69 */     JTable.DropLocation dropLocation = getComponent().getDropLocation();
/*  70 */     if ((dropLocation != null) && (!dropLocation.isInsertRow()) && (!dropLocation.isInsertColumn()) && (dropLocation.getRow() == this.row) && (dropLocation.getColumn() == this.column))
/*     */     {
/*     */ 
/*     */ 
/*     */ 
/*  75 */       return true;
/*     */     }
/*  77 */     return false;
/*     */   }
/*     */   
/*     */ 
/*     */   public JTable getComponent()
/*     */   {
/*  83 */     return (JTable)super.getComponent();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean isEditable()
/*     */   {
/*  94 */     if ((getComponent() == null) || (!isValidRow()) || (!isValidColumn())) {
/*  95 */       return false;
/*     */     }
/*  97 */     return getComponent().isCellEditable(getRow(), getColumn());
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected Color getBackground()
/*     */   {
/* 107 */     if (isDropOn()) {
/* 108 */       return getSelectionBackground();
/*     */     }
/* 110 */     if (getComponent() == null) return null;
/* 111 */     Color color = getAlternateRowColor();
/*     */     
/*     */ 
/* 114 */     if ((color != null) && (getRow() >= 0) && (getRow() % 2 == 1)) {
/* 115 */       return color;
/*     */     }
/* 117 */     return getComponent().getBackground();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected Color getAlternateRowColor()
/*     */   {
/* 129 */     if (!Boolean.TRUE.equals(UIManager.get("TableCellContext.handleAlternateRowBackground"))) return null;
/* 130 */     return UIManagerExt.getColor(getUIPrefix() + "alternateRowColor");
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected Color getSelectionBackground()
/*     */   {
/* 139 */     Color selection = null;
/* 140 */     if (isDropOn()) {
/* 141 */       selection = getDropCellBackground();
/* 142 */       if (selection != null) return selection;
/*     */     }
/* 144 */     return getComponent() != null ? getComponent().getSelectionBackground() : null;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected Color getSelectionForeground()
/*     */   {
/* 153 */     Color selection = null;
/* 154 */     if (isDropOn()) {
/* 155 */       selection = getDropCellForeground();
/* 156 */       if (selection != null) return selection;
/*     */     }
/* 158 */     return getComponent() != null ? getComponent().getSelectionForeground() : null;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected String getUIPrefix()
/*     */   {
/* 167 */     return "Table.";
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected boolean isValidColumn()
/*     */   {
/* 176 */     return (getColumn() >= 0) && (getColumn() < getComponent().getColumnCount());
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected boolean isValidRow()
/*     */   {
/* 185 */     return (getRow() >= 0) && (getRow() < getComponent().getRowCount());
/*     */   }
/*     */ }


/* Location:              E:\java\java学习\hutubill\lib\all.jar!\org\jdesktop\swingx\renderer\TableCellContext.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */
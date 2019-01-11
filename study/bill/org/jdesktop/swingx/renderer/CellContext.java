/*     */ package org.jdesktop.swingx.renderer;
/*     */ 
/*     */ import java.awt.Color;
/*     */ import java.awt.Font;
/*     */ import java.io.Serializable;
/*     */ import javax.swing.Icon;
/*     */ import javax.swing.JComponent;
/*     */ import javax.swing.UIManager;
/*     */ import javax.swing.border.Border;
/*     */ import javax.swing.border.EmptyBorder;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class CellContext
/*     */   implements Serializable
/*     */ {
/*  86 */   protected static Border noFocusBorder = new EmptyBorder(1, 1, 1, 1);
/*     */   
/*     */ 
/*  89 */   private static final Border SAFE_NO_FOCUS_BORDER = new EmptyBorder(1, 1, 1, 1);
/*     */   protected transient JComponent component;
/*     */   protected transient Object value;
/*     */   protected transient int row;
/*     */   protected transient int column;
/*     */   protected transient boolean selected;
/*     */   protected transient boolean focused;
/*     */   protected transient boolean expanded;
/*     */   protected transient boolean leaf;
/*     */   protected transient boolean dropOn;
/*     */   
/* 100 */   private static Border getNoFocusBorder() { if (System.getSecurityManager() != null) {
/* 101 */       return SAFE_NO_FOCUS_BORDER;
/*     */     }
/* 103 */     return noFocusBorder;
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
/*     */ 
/*     */   protected void installState(Object value, int row, int column, boolean selected, boolean focused, boolean expanded, boolean leaf)
/*     */   {
/* 143 */     this.value = value;
/* 144 */     this.row = row;
/* 145 */     this.column = column;
/* 146 */     this.selected = selected;
/* 147 */     this.focused = focused;
/* 148 */     this.expanded = expanded;
/* 149 */     this.leaf = leaf;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public Object replaceValue(Object value)
/*     */   {
/* 160 */     Object old = getValue();
/* 161 */     this.value = value;
/* 162 */     return old;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public JComponent getComponent()
/*     */   {
/* 174 */     return this.component;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public Object getValue()
/*     */   {
/* 183 */     return this.value;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public int getRow()
/*     */   {
/* 192 */     return this.row;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public int getColumn()
/*     */   {
/* 202 */     return this.column;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean isSelected()
/*     */   {
/* 211 */     return this.selected;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean isFocused()
/*     */   {
/* 220 */     return this.focused;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean isExpanded()
/*     */   {
/* 229 */     return this.expanded;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean isLeaf()
/*     */   {
/* 238 */     return this.leaf;
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
/* 252 */     return false;
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
/*     */   public Icon getIcon()
/*     */   {
/* 265 */     return null;
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
/*     */   protected boolean isDropOn()
/*     */   {
/* 279 */     return this.dropOn;
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
/*     */   protected Color getForeground()
/*     */   {
/* 292 */     if (isDropOn()) {
/* 293 */       return getSelectionForeground();
/*     */     }
/* 295 */     return getComponent() != null ? getComponent().getForeground() : null;
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
/*     */   protected Color getBackground()
/*     */   {
/* 308 */     if (isDropOn()) {
/* 309 */       return getSelectionBackground();
/*     */     }
/* 311 */     return getComponent() != null ? getComponent().getBackground() : null;
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
/*     */   protected Color getSelectionBackground()
/*     */   {
/* 325 */     return null;
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
/*     */   protected Color getSelectionForeground()
/*     */   {
/* 339 */     return null;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected Border getFocusBorder()
/*     */   {
/* 349 */     Border border = null;
/* 350 */     if (isSelected()) {
/* 351 */       border = UIManager.getBorder(getUIKey("focusSelectedCellHighlightBorder"));
/*     */     }
/*     */     
/* 354 */     if (border == null) {
/* 355 */       border = UIManager.getBorder(getUIKey("focusCellHighlightBorder"));
/*     */     }
/* 357 */     return border;
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
/*     */   protected Border getBorder()
/*     */   {
/* 371 */     if (isFocused()) {
/* 372 */       return getFocusBorder();
/*     */     }
/* 374 */     Border border = UIManager.getBorder(getUIKey("cellNoFocusBorder"));
/* 375 */     return border != null ? border : getNoFocusBorder();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected Color getFocusForeground()
/*     */   {
/* 385 */     return UIManager.getColor(getUIKey("focusCellForeground"));
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected Color getFocusBackground()
/*     */   {
/* 395 */     return UIManager.getColor(getUIKey("focusCellBackground"));
/*     */   }
/*     */   
/*     */   protected Color getDropCellForeground() {
/* 399 */     return UIManager.getColor(getUIKey("dropCellForeground"));
/*     */   }
/*     */   
/*     */   protected Color getDropCellBackground() {
/* 403 */     return UIManager.getColor(getUIKey("dropCellBackground"));
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected String getUIKey(String key)
/*     */   {
/* 415 */     return getUIPrefix() + key;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected String getUIPrefix()
/*     */   {
/* 425 */     return "";
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   protected Font getFont()
/*     */   {
/* 433 */     return getComponent() != null ? getComponent().getFont() : null;
/*     */   }
/*     */   
/*     */   public String getCellRendererName() {
/* 437 */     return getUIPrefix() + "cellRenderer";
/*     */   }
/*     */ }


/* Location:              E:\java\java学习\hutubill\lib\all.jar!\org\jdesktop\swingx\renderer\CellContext.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */
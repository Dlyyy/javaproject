/*     */ package org.jdesktop.swingx.renderer;
/*     */ 
/*     */ import java.awt.Color;
/*     */ import java.awt.Component;
/*     */ import java.awt.Graphics;
/*     */ import javax.swing.Icon;
/*     */ import javax.swing.JTree;
/*     */ import javax.swing.JTree.DropLocation;
/*     */ import javax.swing.UIManager;
/*     */ import javax.swing.border.Border;
/*     */ import javax.swing.border.LineBorder;
/*     */ import javax.swing.plaf.basic.BasicGraphicsUtils;
/*     */ import javax.swing.tree.TreePath;
/*     */ import org.jdesktop.swingx.JXTree;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class TreeCellContext
/*     */   extends CellContext
/*     */ {
/*     */   protected Icon leafIcon;
/*     */   protected Icon closedIcon;
/*     */   protected Icon openIcon;
/*     */   private Border treeFocusBorder;
/*     */   
/*     */   public void installContext(JTree component, Object value, int row, int column, boolean selected, boolean focused, boolean expanded, boolean leaf)
/*     */   {
/*  79 */     this.component = component;
/*  80 */     installState(value, row, column, selected, focused, expanded, leaf);
/*  81 */     this.dropOn = checkDropOnState();
/*     */   }
/*     */   
/*     */   private boolean checkDropOnState() {
/*  85 */     if (getComponent() == null) {
/*  86 */       return false;
/*     */     }
/*  88 */     JTree.DropLocation dropLocation = getComponent().getDropLocation();
/*  89 */     if ((dropLocation != null) && (dropLocation.getChildIndex() == -1) && (getComponent().getRowForPath(dropLocation.getPath()) == this.row))
/*     */     {
/*     */ 
/*  92 */       return true;
/*     */     }
/*  94 */     return false;
/*     */   }
/*     */   
/*     */   public JTree getComponent()
/*     */   {
/*  99 */     return (JTree)super.getComponent();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public TreePath getTreePath()
/*     */   {
/* 109 */     if (getComponent() == null) return null;
/* 110 */     if ((this.row < 0) || (this.row >= getComponent().getRowCount())) return null;
/* 111 */     return getComponent().getPathForRow(this.row);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean isEditable()
/*     */   {
/* 120 */     return false;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected Color getSelectionBackground()
/*     */   {
/* 130 */     Color selection = null;
/* 131 */     if (isDropOn()) {
/* 132 */       selection = getDropCellBackground();
/* 133 */       if (selection != null) return selection;
/*     */     }
/* 135 */     if ((getComponent() instanceof JXTree)) {
/* 136 */       return ((JXTree)getComponent()).getSelectionBackground();
/*     */     }
/* 138 */     return UIManager.getColor("Tree.selectionBackground");
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   protected Color getSelectionForeground()
/*     */   {
/* 146 */     Color selection = null;
/* 147 */     if (isDropOn()) {
/* 148 */       selection = getDropCellForeground();
/* 149 */       if (selection != null) return selection;
/*     */     }
/* 151 */     if ((getComponent() instanceof JXTree)) {
/* 152 */       return ((JXTree)getComponent()).getSelectionForeground();
/*     */     }
/* 154 */     return UIManager.getColor("Tree.selectionForeground");
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   protected String getUIPrefix()
/*     */   {
/* 162 */     return "Tree.";
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected Icon getLeafIcon()
/*     */   {
/* 171 */     return this.leafIcon != null ? this.leafIcon : UIManager.getIcon(getUIKey("leafIcon"));
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected Icon getOpenIcon()
/*     */   {
/* 181 */     return this.openIcon != null ? this.openIcon : UIManager.getIcon(getUIKey("openIcon"));
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected Icon getClosedIcon()
/*     */   {
/* 191 */     return this.closedIcon != null ? this.closedIcon : UIManager.getIcon(getUIKey("closedIcon"));
/*     */   }
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
/* 203 */     if (isLeaf()) {
/* 204 */       return getLeafIcon();
/*     */     }
/* 206 */     if (isExpanded()) {
/* 207 */       return getOpenIcon();
/*     */     }
/* 209 */     return getClosedIcon();
/*     */   }
/*     */   
/*     */   protected Border getFocusBorder()
/*     */   {
/* 214 */     if (this.treeFocusBorder == null) {
/* 215 */       this.treeFocusBorder = new TreeFocusBorder();
/*     */     }
/* 217 */     return this.treeFocusBorder;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public class TreeFocusBorder
/*     */     extends LineBorder
/*     */   {
/*     */     private Color treeBackground;
/*     */     
/*     */     private Color focusColor;
/*     */     
/*     */ 
/*     */     public TreeFocusBorder()
/*     */     {
/* 232 */       super();
/* 233 */       this.treeBackground = TreeCellContext.this.getBackground();
/* 234 */       if (this.treeBackground != null) {
/* 235 */         this.focusColor = new Color(this.treeBackground.getRGB() ^ 0xFFFFFFFF);
/*     */       }
/*     */     }
/*     */     
/*     */ 
/*     */     public void paintBorder(Component c, Graphics g, int x, int y, int width, int height)
/*     */     {
/* 242 */       Color color = UIManager.getColor("Tree.selectionBorderColor");
/* 243 */       if (color != null) {
/* 244 */         this.lineColor = color;
/*     */       }
/* 246 */       if (isDashed()) {
/* 247 */         if (this.treeBackground != c.getBackground()) {
/* 248 */           this.treeBackground = c.getBackground();
/* 249 */           this.focusColor = new Color(this.treeBackground.getRGB() ^ 0xFFFFFFFF);
/*     */         }
/*     */         
/* 252 */         Color old = g.getColor();
/* 253 */         g.setColor(this.focusColor);
/* 254 */         BasicGraphicsUtils.drawDashedRect(g, x, y, width, height);
/* 255 */         g.setColor(old);
/*     */       }
/*     */       else {
/* 258 */         super.paintBorder(c, g, x, y, width, height);
/*     */       }
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     private boolean isDashed()
/*     */     {
/* 268 */       return Boolean.TRUE.equals(UIManager.get("Tree.drawDashedFocusIndicator"));
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     public boolean isBorderOpaque()
/*     */     {
/* 278 */       return false;
/*     */     }
/*     */   }
/*     */ }


/* Location:              E:\java\java学习\hutubill\lib\all.jar!\org\jdesktop\swingx\renderer\TreeCellContext.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */
/*     */ package org.jdesktop.swingx.treetable;
/*     */ 
/*     */ import java.awt.Component;
/*     */ import java.awt.ComponentOrientation;
/*     */ import java.awt.Rectangle;
/*     */ import java.awt.event.MouseEvent;
/*     */ import java.util.EventObject;
/*     */ import java.util.logging.Logger;
/*     */ import javax.swing.DefaultCellEditor;
/*     */ import javax.swing.Icon;
/*     */ import javax.swing.JLabel;
/*     */ import javax.swing.JTable;
/*     */ import javax.swing.JTextField;
/*     */ import javax.swing.JTree;
/*     */ import javax.swing.tree.TreeCellRenderer;
/*     */ import javax.swing.tree.TreeModel;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class TreeTableCellEditor
/*     */   extends DefaultCellEditor
/*     */ {
/*  80 */   private static final Logger LOG = Logger.getLogger(TreeTableCellEditor.class.getName());
/*     */   private final JTree tree;
/*     */   
/*     */   public TreeTableCellEditor(JTree tree) {
/*  84 */     super(new TreeTableTextField());
/*  85 */     if (tree == null) {
/*  86 */       throw new IllegalArgumentException("null tree");
/*     */     }
/*     */     
/*  89 */     this.tree = tree;
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
/*     */   public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column)
/*     */   {
/* 103 */     Component component = super.getTableCellEditorComponent(table, value, isSelected, row, column);
/*     */     
/*     */ 
/* 106 */     initEditorOffset(table, row, column, isSelected);
/* 107 */     return component;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected void initEditorOffset(JTable table, int row, int column, boolean isSelected)
/*     */   {
/* 116 */     if (this.tree == null) {
/* 117 */       return;
/*     */     }
/*     */     
/* 120 */     Object node = this.tree.getPathForRow(row).getLastPathComponent();
/* 121 */     boolean leaf = this.tree.getModel().isLeaf(node);
/* 122 */     boolean expanded = this.tree.isExpanded(row);
/* 123 */     TreeCellRenderer tcr = this.tree.getCellRenderer();
/* 124 */     Component editorComponent = tcr.getTreeCellRendererComponent(this.tree, node, isSelected, expanded, leaf, row, false);
/*     */     
/*     */ 
/* 127 */     ((TreeTableTextField)getComponent()).init(row, column, table, this.tree, editorComponent);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean isCellEditable(EventObject e)
/*     */   {
/* 139 */     if ((e instanceof MouseEvent)) {
/* 140 */       return ((MouseEvent)e).getClickCount() >= this.clickCountToStart;
/*     */     }
/* 142 */     return true;
/*     */   }
/*     */   
/*     */   static class TreeTableTextField extends JTextField
/*     */   {
/*     */     private int iconWidth;
/*     */     private int column;
/*     */     private int row;
/*     */     private JTable table;
/*     */     private JTree tree;
/*     */     
/*     */     void init(int row, int column, JTable table, JTree tree, Component editorComponent) {
/* 154 */       this.column = column;
/* 155 */       this.row = row;
/* 156 */       this.table = table;
/* 157 */       this.tree = tree;
/* 158 */       updateIconWidth(editorComponent);
/* 159 */       setComponentOrientation(table.getComponentOrientation());
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */     private void updateIconWidth(Component treeComponent)
/*     */     {
/* 166 */       this.iconWidth = 0;
/* 167 */       if (!(treeComponent instanceof JLabel)) return;
/* 168 */       Icon icon = ((JLabel)treeComponent).getIcon();
/* 169 */       if (icon != null) {
/* 170 */         this.iconWidth = (icon.getIconWidth() + ((JLabel)treeComponent).getIconTextGap());
/*     */       }
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     public void reshape(int x, int y, int width, int height)
/*     */     {
/* 198 */       Rectangle cellRect = this.table.getCellRect(0, this.column, false);
/* 199 */       Rectangle nodeRect = this.tree.getRowBounds(this.row);
/* 200 */       nodeRect.width -= this.iconWidth;
/* 201 */       if (this.table.getComponentOrientation().isLeftToRight()) {
/* 202 */         int nodeStart = cellRect.x + nodeRect.x + this.iconWidth;
/* 203 */         int nodeEnd = cellRect.x + cellRect.width;
/* 204 */         super.reshape(nodeStart, y, nodeEnd - nodeStart, height);
/*     */       }
/*     */       else
/*     */       {
/* 208 */         int nodeRightX = nodeRect.x + nodeRect.width;
/* 209 */         nodeRect.x = 0;
/*     */         
/* 211 */         width = nodeRightX - nodeRect.x;
/* 212 */         super.reshape(cellRect.x + nodeRect.x, y, width, height);
/*     */       }
/*     */     }
/*     */   }
/*     */ }


/* Location:              E:\java\java学习\hutubill\lib\all.jar!\org\jdesktop\swingx\treetable\TreeTableCellEditor.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */
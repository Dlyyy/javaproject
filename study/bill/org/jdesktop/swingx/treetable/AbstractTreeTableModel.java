/*     */ package org.jdesktop.swingx.treetable;
/*     */ 
/*     */ import javax.swing.event.TreeModelListener;
/*     */ import javax.swing.tree.TreePath;
/*     */ import org.jdesktop.swingx.tree.TreeModelSupport;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public abstract class AbstractTreeTableModel
/*     */   implements TreeTableModel
/*     */ {
/*     */   protected Object root;
/*     */   protected TreeModelSupport modelSupport;
/*     */   
/*     */   public AbstractTreeTableModel()
/*     */   {
/*  70 */     this(null);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public AbstractTreeTableModel(Object root)
/*     */   {
/*  81 */     this.root = root;
/*  82 */     this.modelSupport = new TreeModelSupport(this);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public Class<?> getColumnClass(int column)
/*     */   {
/*  90 */     return Object.class;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public String getColumnName(int column)
/*     */   {
/* 100 */     String result = "";
/* 102 */     for (; 
/* 102 */         column >= 0; column = column / 26 - 1) {
/* 103 */       result = (char)((char)(column % 26) + 'A') + result;
/*     */     }
/*     */     
/* 106 */     return result;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public int getHierarchicalColumn()
/*     */   {
/* 114 */     if (getColumnCount() == 0) {
/* 115 */       return -1;
/*     */     }
/*     */     
/* 118 */     return 0;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public Object getRoot()
/*     */   {
/* 126 */     return this.root;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean isCellEditable(Object node, int column)
/*     */   {
/* 136 */     return false;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean isLeaf(Object node)
/*     */   {
/* 148 */     return getChildCount(node) == 0;
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
/*     */   public void setValueAt(Object value, Object node, int column) {}
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void valueForPathChanged(TreePath path, Object newValue) {}
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void addTreeModelListener(TreeModelListener l)
/*     */   {
/* 193 */     this.modelSupport.addTreeModelListener(l);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void removeTreeModelListener(TreeModelListener l)
/*     */   {
/* 201 */     this.modelSupport.removeTreeModelListener(l);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public TreeModelListener[] getTreeModelListeners()
/*     */   {
/* 212 */     return this.modelSupport.getTreeModelListeners();
/*     */   }
/*     */ }


/* Location:              E:\java\java学习\hutubill\lib\all.jar!\org\jdesktop\swingx\treetable\AbstractTreeTableModel.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */
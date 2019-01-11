/*     */ package org.jdesktop.swingx.treetable;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
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
/*     */ public class DefaultTreeTableModel
/*     */   extends AbstractTreeTableModel
/*     */ {
/*     */   protected List<?> columnIdentifiers;
/*     */   private boolean useAutoCalculatedIdentifiers;
/*     */   
/*     */   public DefaultTreeTableModel()
/*     */   {
/*  60 */     this(null);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public DefaultTreeTableModel(TreeTableNode root)
/*     */   {
/*  71 */     this(root, null);
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
/*     */   public DefaultTreeTableModel(TreeTableNode root, List<?> columnNames)
/*     */   {
/*  85 */     super(root);
/*     */     
/*  87 */     setColumnIdentifiers(columnNames);
/*     */   }
/*     */   
/*     */   private boolean isValidTreeTableNode(Object node) {
/*  91 */     boolean result = false;
/*     */     
/*  93 */     if ((node instanceof TreeTableNode)) {
/*  94 */       TreeTableNode ttn = (TreeTableNode)node;
/*     */       
/*  96 */       while ((!result) && (ttn != null)) {
/*  97 */         result = ttn == this.root;
/*     */         
/*  99 */         ttn = ttn.getParent();
/*     */       }
/*     */     }
/*     */     
/* 103 */     return result;
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
/*     */   public void setColumnIdentifiers(List<?> columnIdentifiers)
/*     */   {
/* 121 */     this.useAutoCalculatedIdentifiers = (columnIdentifiers == null);
/*     */     
/* 123 */     this.columnIdentifiers = (this.useAutoCalculatedIdentifiers ? getAutoCalculatedIdentifiers(getRoot()) : columnIdentifiers);
/*     */     
/*     */ 
/*     */ 
/* 127 */     this.modelSupport.fireNewRoot();
/*     */   }
/*     */   
/*     */   private static List<String> getAutoCalculatedIdentifiers(TreeTableNode exemplar)
/*     */   {
/* 132 */     List<String> autoCalculatedIndentifiers = new ArrayList();
/*     */     
/* 134 */     if (exemplar != null) {
/* 135 */       int i = 0; for (int len = exemplar.getColumnCount(); i < len; i++)
/*     */       {
/* 137 */         autoCalculatedIndentifiers.add(null);
/*     */       }
/*     */     }
/*     */     
/* 141 */     return autoCalculatedIndentifiers;
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
/*     */   public TreeTableNode getRoot()
/*     */   {
/* 159 */     return (TreeTableNode)this.root;
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
/*     */   public Object getValueAt(Object node, int column)
/*     */   {
/* 178 */     if (!isValidTreeTableNode(node)) {
/* 179 */       throw new IllegalArgumentException("node must be a valid node managed by this model");
/*     */     }
/*     */     
/*     */ 
/* 183 */     if ((column < 0) || (column >= getColumnCount())) {
/* 184 */       throw new IllegalArgumentException("column must be a valid index");
/*     */     }
/*     */     
/* 187 */     TreeTableNode ttn = (TreeTableNode)node;
/*     */     
/* 189 */     if (column >= ttn.getColumnCount()) {
/* 190 */       return null;
/*     */     }
/*     */     
/* 193 */     return ttn.getValueAt(column);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setValueAt(Object value, Object node, int column)
/*     */   {
/* 201 */     if (!isValidTreeTableNode(node)) {
/* 202 */       throw new IllegalArgumentException("node must be a valid node managed by this model");
/*     */     }
/*     */     
/*     */ 
/* 206 */     if ((column < 0) || (column >= getColumnCount())) {
/* 207 */       throw new IllegalArgumentException("column must be a valid index");
/*     */     }
/*     */     
/* 210 */     TreeTableNode ttn = (TreeTableNode)node;
/*     */     
/* 212 */     if (column < ttn.getColumnCount()) {
/* 213 */       ttn.setValueAt(value, column);
/*     */       
/* 215 */       this.modelSupport.firePathChanged(new TreePath(getPathToRoot(ttn)));
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public int getColumnCount()
/*     */   {
/* 224 */     return this.columnIdentifiers.size();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public String getColumnName(int column)
/*     */   {
/* 234 */     Object id = null;
/*     */     
/*     */ 
/*     */ 
/* 238 */     if ((column < this.columnIdentifiers.size()) && (column >= 0)) {
/* 239 */       id = this.columnIdentifiers.get(column);
/*     */     }
/*     */     
/* 242 */     return id == null ? super.getColumnName(column) : id.toString();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public Object getChild(Object parent, int index)
/*     */   {
/* 250 */     if (!isValidTreeTableNode(parent)) {
/* 251 */       throw new IllegalArgumentException("parent must be a TreeTableNode managed by this model");
/*     */     }
/*     */     
/*     */ 
/* 255 */     return ((TreeTableNode)parent).getChildAt(index);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public int getChildCount(Object parent)
/*     */   {
/* 263 */     if (!isValidTreeTableNode(parent)) {
/* 264 */       throw new IllegalArgumentException("parent must be a TreeTableNode managed by this model");
/*     */     }
/*     */     
/*     */ 
/* 268 */     return ((TreeTableNode)parent).getChildCount();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public int getIndexOfChild(Object parent, Object child)
/*     */   {
/* 276 */     if ((!isValidTreeTableNode(parent)) || (!isValidTreeTableNode(child))) {
/* 277 */       return -1;
/*     */     }
/*     */     
/* 280 */     return ((TreeTableNode)parent).getIndex((TreeTableNode)child);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean isCellEditable(Object node, int column)
/*     */   {
/* 288 */     if (!isValidTreeTableNode(node)) {
/* 289 */       throw new IllegalArgumentException("node must be a valid node managed by this model");
/*     */     }
/*     */     
/*     */ 
/* 293 */     if ((column < 0) || (column >= getColumnCount())) {
/* 294 */       throw new IllegalArgumentException("column must be a valid index");
/*     */     }
/*     */     
/* 297 */     TreeTableNode ttn = (TreeTableNode)node;
/*     */     
/* 299 */     if (column >= ttn.getColumnCount()) {
/* 300 */       return false;
/*     */     }
/*     */     
/* 303 */     return ttn.isEditable(column);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean isLeaf(Object node)
/*     */   {
/* 311 */     if (!isValidTreeTableNode(node)) {
/* 312 */       throw new IllegalArgumentException("node must be a TreeTableNode managed by this model");
/*     */     }
/*     */     
/*     */ 
/* 316 */     return ((TreeTableNode)node).isLeaf();
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
/*     */   public TreeTableNode[] getPathToRoot(TreeTableNode aNode)
/*     */   {
/* 332 */     List<TreeTableNode> path = new ArrayList();
/* 333 */     TreeTableNode node = aNode;
/*     */     
/* 335 */     while (node != this.root) {
/* 336 */       path.add(0, node);
/*     */       
/* 338 */       node = node.getParent();
/*     */     }
/*     */     
/* 341 */     if (node == this.root) {
/* 342 */       path.add(0, node);
/*     */     }
/*     */     
/* 345 */     return (TreeTableNode[])path.toArray(new TreeTableNode[0]);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setRoot(TreeTableNode root)
/*     */   {
/* 357 */     this.root = root;
/*     */     
/* 359 */     if (this.useAutoCalculatedIdentifiers)
/*     */     {
/*     */ 
/* 362 */       setColumnIdentifiers(null);
/*     */     } else {
/* 364 */       this.modelSupport.fireNewRoot();
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void insertNodeInto(MutableTreeTableNode newChild, MutableTreeTableNode parent, int index)
/*     */   {
/* 376 */     parent.insert(newChild, index);
/*     */     
/* 378 */     this.modelSupport.fireChildAdded(new TreePath(getPathToRoot(parent)), index, newChild);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void removeNodeFromParent(MutableTreeTableNode node)
/*     */   {
/* 388 */     MutableTreeTableNode parent = (MutableTreeTableNode)node.getParent();
/*     */     
/* 390 */     if (parent == null) {
/* 391 */       throw new IllegalArgumentException("node does not have a parent.");
/*     */     }
/*     */     
/* 394 */     int index = parent.getIndex(node);
/* 395 */     node.removeFromParent();
/*     */     
/* 397 */     this.modelSupport.fireChildRemoved(new TreePath(getPathToRoot(parent)), index, node);
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
/*     */   public void valueForPathChanged(TreePath path, Object newValue)
/*     */   {
/* 425 */     if (path.getPathComponent(0) != this.root) {
/* 426 */       throw new IllegalArgumentException("invalid path");
/*     */     }
/*     */     
/* 429 */     TreeTableNode node = (TreeTableNode)path.getLastPathComponent();
/* 430 */     node.setUserObject(newValue);
/*     */     
/* 432 */     this.modelSupport.firePathChanged(path);
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
/*     */   public void setUserObject(TreeTableNode node, Object userObject)
/*     */   {
/* 452 */     valueForPathChanged(new TreePath(getPathToRoot(node)), userObject);
/*     */   }
/*     */ }


/* Location:              E:\java\java学习\hutubill\lib\all.jar!\org\jdesktop\swingx\treetable\DefaultTreeTableModel.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */
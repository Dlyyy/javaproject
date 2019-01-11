/*     */ package org.jdesktop.swingx.tree;
/*     */ 
/*     */ import javax.swing.event.EventListenerList;
/*     */ import javax.swing.event.TreeModelEvent;
/*     */ import javax.swing.event.TreeModelListener;
/*     */ import javax.swing.tree.TreeModel;
/*     */ import javax.swing.tree.TreePath;
/*     */ import org.jdesktop.swingx.util.Contract;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public final class TreeModelSupport
/*     */ {
/*     */   protected EventListenerList listeners;
/*     */   private TreeModel treeModel;
/*     */   
/*     */   public TreeModelSupport(TreeModel model)
/*     */   {
/*  56 */     if (model == null)
/*  57 */       throw new NullPointerException("model must not be null");
/*  58 */     this.listeners = new EventListenerList();
/*  59 */     this.treeModel = model;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void fireNewRoot()
/*     */   {
/*  70 */     Object root = this.treeModel.getRoot();
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*  77 */     TreePath path = root != null ? new TreePath(root) : null;
/*  78 */     fireTreeStructureChanged(path);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void firePathLeafStateChanged(TreePath path)
/*     */   {
/*  88 */     fireTreeStructureChanged(path);
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
/*     */   public void fireTreeStructureChanged(TreePath subTreePath)
/*     */   {
/* 105 */     if (subTreePath != null) {
/* 106 */       Contract.asNotNull(subTreePath.getPath(), "path must not contain null elements");
/*     */     }
/*     */     
/* 109 */     Object[] pairs = this.listeners.getListenerList();
/*     */     
/* 111 */     TreeModelEvent e = null;
/*     */     
/* 113 */     for (int i = pairs.length - 2; i >= 0; i -= 2) {
/* 114 */       if (pairs[i] == TreeModelListener.class) {
/* 115 */         if (e == null) {
/* 116 */           e = createStructureChangedEvent(subTreePath);
/*     */         }
/* 118 */         ((TreeModelListener)pairs[(i + 1)]).treeStructureChanged(e);
/*     */       }
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
/*     */   public void firePathChanged(TreePath path)
/*     */   {
/* 134 */     Object node = path.getLastPathComponent();
/* 135 */     TreePath parentPath = path.getParentPath();
/*     */     
/* 137 */     if (parentPath == null) {
/* 138 */       fireChildrenChanged(path, null, null);
/*     */     } else {
/* 140 */       Object parent = parentPath.getLastPathComponent();
/*     */       
/* 142 */       fireChildChanged(parentPath, this.treeModel.getIndexOfChild(parent, node), node);
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
/*     */   public void fireChildChanged(TreePath parentPath, int index, Object child)
/*     */   {
/* 158 */     fireChildrenChanged(parentPath, new int[] { index }, new Object[] { child });
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
/*     */   public void fireChildrenChanged(TreePath parentPath, int[] indices, Object[] children)
/*     */   {
/* 181 */     Contract.asNotNull(parentPath.getPath(), "path must not be null and must not contain null elements");
/*     */     
/* 183 */     Object[] pairs = this.listeners.getListenerList();
/*     */     
/* 185 */     TreeModelEvent e = null;
/*     */     
/* 187 */     for (int i = pairs.length - 2; i >= 0; i -= 2) {
/* 188 */       if (pairs[i] == TreeModelListener.class) {
/* 189 */         if (e == null) {
/* 190 */           e = createTreeModelEvent(parentPath, indices, children);
/*     */         }
/* 192 */         ((TreeModelListener)pairs[(i + 1)]).treeNodesChanged(e);
/*     */       }
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
/*     */ 
/*     */ 
/*     */   public void fireChildAdded(TreePath parentPath, int index, Object child)
/*     */   {
/* 211 */     fireChildrenAdded(parentPath, new int[] { index }, new Object[] { child });
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
/*     */   public void fireChildRemoved(TreePath parentPath, int index, Object child)
/*     */   {
/* 225 */     fireChildrenRemoved(parentPath, new int[] { index }, new Object[] { child });
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
/*     */   public void fireChildrenAdded(TreePath parentPath, int[] indices, Object[] children)
/*     */   {
/* 244 */     Object[] pairs = this.listeners.getListenerList();
/*     */     
/* 246 */     TreeModelEvent e = null;
/*     */     
/* 248 */     for (int i = pairs.length - 2; i >= 0; i -= 2) {
/* 249 */       if (pairs[i] == TreeModelListener.class) {
/* 250 */         if (e == null) {
/* 251 */           e = createTreeModelEvent(parentPath, indices, children);
/*     */         }
/* 253 */         ((TreeModelListener)pairs[(i + 1)]).treeNodesInserted(e);
/*     */       }
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
/*     */ 
/*     */ 
/*     */ 
/*     */   public void fireChildrenRemoved(TreePath parentPath, int[] indices, Object[] children)
/*     */   {
/* 273 */     Object[] pairs = this.listeners.getListenerList();
/*     */     
/* 275 */     TreeModelEvent e = null;
/*     */     
/* 277 */     for (int i = pairs.length - 2; i >= 0; i -= 2) {
/* 278 */       if (pairs[i] == TreeModelListener.class) {
/* 279 */         if (e == null)
/* 280 */           e = createTreeModelEvent(parentPath, indices, children);
/* 281 */         ((TreeModelListener)pairs[(i + 1)]).treeNodesRemoved(e);
/*     */       }
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private TreeModelEvent createStructureChangedEvent(TreePath parentPath)
/*     */   {
/* 304 */     return createTreeModelEvent(parentPath, null, null);
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
/*     */   private TreeModelEvent createTreeModelEvent(TreePath parentPath, int[] indices, Object[] children)
/*     */   {
/* 321 */     return new TreeModelEvent(this.treeModel, parentPath, indices, children);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public void addTreeModelListener(TreeModelListener l)
/*     */   {
/* 328 */     this.listeners.add(TreeModelListener.class, l);
/*     */   }
/*     */   
/*     */   public TreeModelListener[] getTreeModelListeners() {
/* 332 */     return (TreeModelListener[])this.listeners.getListeners(TreeModelListener.class);
/*     */   }
/*     */   
/*     */   public void removeTreeModelListener(TreeModelListener l) {
/* 336 */     this.listeners.remove(TreeModelListener.class, l);
/*     */   }
/*     */ }


/* Location:              E:\java\java学习\hutubill\lib\all.jar!\org\jdesktop\swingx\tree\TreeModelSupport.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */
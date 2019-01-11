/*     */ package org.jdesktop.swingx.treetable;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collections;
/*     */ import java.util.Enumeration;
/*     */ import java.util.List;
/*     */ import javax.swing.tree.TreeNode;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public abstract class AbstractMutableTreeTableNode
/*     */   implements MutableTreeTableNode
/*     */ {
/*     */   protected MutableTreeTableNode parent;
/*     */   protected final List<MutableTreeTableNode> children;
/*     */   protected transient Object userObject;
/*     */   protected boolean allowsChildren;
/*     */   
/*     */   public AbstractMutableTreeTableNode()
/*     */   {
/*  53 */     this(null);
/*     */   }
/*     */   
/*     */   public AbstractMutableTreeTableNode(Object userObject) {
/*  57 */     this(userObject, true);
/*     */   }
/*     */   
/*     */   public AbstractMutableTreeTableNode(Object userObject, boolean allowsChildren)
/*     */   {
/*  62 */     this.userObject = userObject;
/*  63 */     this.allowsChildren = allowsChildren;
/*  64 */     this.children = createChildrenList();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected List<MutableTreeTableNode> createChildrenList()
/*     */   {
/*  75 */     return new ArrayList();
/*     */   }
/*     */   
/*     */   public void add(MutableTreeTableNode child) {
/*  79 */     insert(child, getChildCount());
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void insert(MutableTreeTableNode child, int index)
/*     */   {
/*  87 */     if (!this.allowsChildren) {
/*  88 */       throw new IllegalStateException("this node cannot accept children");
/*     */     }
/*     */     
/*  91 */     if (this.children.contains(child)) {
/*  92 */       this.children.remove(child);
/*  93 */       index--;
/*     */     }
/*     */     
/*  96 */     this.children.add(index, child);
/*     */     
/*  98 */     if (child.getParent() != this) {
/*  99 */       child.setParent(this);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void remove(int index)
/*     */   {
/* 108 */     ((MutableTreeTableNode)this.children.remove(index)).setParent(null);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void remove(MutableTreeTableNode node)
/*     */   {
/* 116 */     this.children.remove(node);
/* 117 */     node.setParent(null);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void removeFromParent()
/*     */   {
/* 125 */     this.parent.remove(this);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setParent(MutableTreeTableNode newParent)
/*     */   {
/* 133 */     if ((newParent == null) || (newParent.getAllowsChildren())) {
/* 134 */       if ((this.parent != null) && (this.parent.getIndex(this) != -1)) {
/* 135 */         this.parent.remove(this);
/*     */       }
/*     */     } else {
/* 138 */       throw new IllegalArgumentException("newParent does not allow children");
/*     */     }
/*     */     
/*     */ 
/* 142 */     this.parent = newParent;
/*     */     
/* 144 */     if ((this.parent != null) && (this.parent.getIndex(this) == -1)) {
/* 145 */       this.parent.insert(this, this.parent.getChildCount());
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
/*     */   public Object getUserObject()
/*     */   {
/* 158 */     return this.userObject;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setUserObject(Object object)
/*     */   {
/* 166 */     this.userObject = object;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public TreeTableNode getChildAt(int childIndex)
/*     */   {
/* 174 */     return (TreeTableNode)this.children.get(childIndex);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public int getIndex(TreeNode node)
/*     */   {
/* 182 */     return this.children.indexOf(node);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public TreeTableNode getParent()
/*     */   {
/* 190 */     return this.parent;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public Enumeration<? extends MutableTreeTableNode> children()
/*     */   {
/* 198 */     return Collections.enumeration(this.children);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean getAllowsChildren()
/*     */   {
/* 206 */     return this.allowsChildren;
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
/*     */   public void setAllowsChildren(boolean allowsChildren)
/*     */   {
/* 220 */     this.allowsChildren = allowsChildren;
/*     */     
/* 222 */     if (!this.allowsChildren) {
/* 223 */       this.children.clear();
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public int getChildCount()
/*     */   {
/* 232 */     return this.children.size();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean isLeaf()
/*     */   {
/* 240 */     return getChildCount() == 0;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean isEditable(int column)
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
/*     */ 
/*     */   public void setValueAt(Object aValue, int column) {}
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public String toString()
/*     */   {
/* 277 */     if (this.userObject == null) {
/* 278 */       return "";
/*     */     }
/* 280 */     return this.userObject.toString();
/*     */   }
/*     */ }


/* Location:              E:\java\java学习\hutubill\lib\all.jar!\org\jdesktop\swingx\treetable\AbstractMutableTreeTableNode.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */
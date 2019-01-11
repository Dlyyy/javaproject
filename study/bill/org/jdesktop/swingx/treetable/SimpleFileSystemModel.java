/*     */ package org.jdesktop.swingx.treetable;
/*     */ 
/*     */ import java.io.File;
/*     */ import java.util.Date;
/*     */ import javax.swing.event.EventListenerList;
/*     */ import javax.swing.event.TreeModelListener;
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
/*     */ public class SimpleFileSystemModel
/*     */   implements TreeTableModel
/*     */ {
/*     */   protected EventListenerList listenerList;
/*  55 */   private static final Long ZERO = Long.valueOf(0L);
/*     */   
/*     */ 
/*     */   private File root;
/*     */   
/*     */ 
/*     */   public SimpleFileSystemModel()
/*     */   {
/*  63 */     this(new File(File.separator));
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public SimpleFileSystemModel(File root)
/*     */   {
/*  71 */     this.root = root;
/*  72 */     this.listenerList = new EventListenerList();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public File getChild(Object parent, int index)
/*     */   {
/*  79 */     if ((parent instanceof File)) {
/*  80 */       File parentFile = (File)parent;
/*  81 */       File[] files = parentFile.listFiles();
/*     */       
/*  83 */       if (files != null) {
/*  84 */         return files[index];
/*     */       }
/*     */     }
/*     */     
/*  88 */     return null;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public int getChildCount(Object parent)
/*     */   {
/*  95 */     if ((parent instanceof File)) {
/*  96 */       String[] children = ((File)parent).list();
/*     */       
/*  98 */       if (children != null) {
/*  99 */         return children.length;
/*     */       }
/*     */     }
/*     */     
/* 103 */     return 0;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public Class<?> getColumnClass(int column)
/*     */   {
/* 110 */     switch (column) {
/*     */     case 0: 
/* 112 */       return String.class;
/*     */     case 1: 
/* 114 */       return Long.class;
/*     */     case 2: 
/* 116 */       return Boolean.class;
/*     */     case 3: 
/* 118 */       return Date.class;
/*     */     }
/* 120 */     return Object.class;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public int getColumnCount()
/*     */   {
/* 128 */     return 4;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public String getColumnName(int column)
/*     */   {
/* 135 */     switch (column) {
/*     */     case 0: 
/* 137 */       return "Name";
/*     */     case 1: 
/* 139 */       return "Size";
/*     */     case 2: 
/* 141 */       return "Directory";
/*     */     case 3: 
/* 143 */       return "Modification Date";
/*     */     }
/* 145 */     return "Column " + column;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public Object getValueAt(Object node, int column)
/*     */   {
/* 153 */     if ((node instanceof File)) {
/* 154 */       File file = (File)node;
/* 155 */       switch (column) {
/*     */       case 0: 
/* 157 */         return file.getName();
/*     */       case 1: 
/* 159 */         return Long.valueOf(file.isFile() ? file.length() : ZERO.longValue());
/*     */       case 2: 
/* 161 */         return Boolean.valueOf(file.isDirectory());
/*     */       case 3: 
/* 163 */         return new Date(file.lastModified());
/*     */       }
/*     */       
/*     */     }
/* 167 */     return null;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public int getHierarchicalColumn()
/*     */   {
/* 174 */     return 0;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public boolean isCellEditable(Object node, int column)
/*     */   {
/* 181 */     return false;
/*     */   }
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
/*     */   public void addTreeModelListener(TreeModelListener l)
/*     */   {
/* 195 */     this.listenerList.add(TreeModelListener.class, l);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public int getIndexOfChild(Object parent, Object child)
/*     */   {
/* 202 */     if (((parent instanceof File)) && ((child instanceof File))) {
/* 203 */       File parentFile = (File)parent;
/* 204 */       File[] files = parentFile.listFiles();
/*     */       
/* 206 */       int i = 0; for (int len = files.length; i < len; i++) {
/* 207 */         if (files[i].equals(child)) {
/* 208 */           return i;
/*     */         }
/*     */       }
/*     */     }
/*     */     
/* 213 */     return -1;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public File getRoot()
/*     */   {
/* 220 */     return this.root;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public boolean isLeaf(Object node)
/*     */   {
/* 227 */     if ((node instanceof File))
/*     */     {
/* 229 */       return ((File)node).list() == null;
/*     */     }
/*     */     
/* 232 */     return true;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public void removeTreeModelListener(TreeModelListener l)
/*     */   {
/* 239 */     this.listenerList.remove(TreeModelListener.class, l);
/*     */   }
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
/*     */   public TreeModelListener[] getTreeModelListeners()
/*     */   {
/* 256 */     return (TreeModelListener[])this.listenerList.getListeners(TreeModelListener.class);
/*     */   }
/*     */ }


/* Location:              E:\java\java学习\hutubill\lib\all.jar!\org\jdesktop\swingx\treetable\SimpleFileSystemModel.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */
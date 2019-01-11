/*     */ package org.jdesktop.swingx.treetable;
/*     */ 
/*     */ import java.io.File;
/*     */ import java.util.Arrays;
/*     */ import java.util.Date;
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
/*     */ public class FileSystemModel
/*     */   extends AbstractTreeTableModel
/*     */ {
/*  50 */   private static final Long DIRECTORY = Long.valueOf(0L);
/*     */   
/*     */ 
/*     */ 
/*     */   public FileSystemModel()
/*     */   {
/*  56 */     this(new File(File.separator));
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public FileSystemModel(File root)
/*     */   {
/*  67 */     super(root);
/*     */   }
/*     */   
/*     */   private boolean isValidFileNode(Object file) {
/*  71 */     boolean result = false;
/*     */     
/*  73 */     if ((file instanceof File)) {
/*  74 */       File f = (File)file;
/*     */       
/*  76 */       while ((!result) && (f != null)) {
/*  77 */         result = f.equals(this.root);
/*     */         
/*  79 */         f = f.getParentFile();
/*     */       }
/*     */     }
/*     */     
/*  83 */     return result;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public File getChild(Object parent, int index)
/*     */   {
/*  90 */     if (!isValidFileNode(parent)) {
/*  91 */       throw new IllegalArgumentException("parent is not a file governed by this model");
/*     */     }
/*     */     
/*  94 */     File parentFile = (File)parent;
/*  95 */     String[] children = parentFile.list();
/*     */     
/*  97 */     if (children != null) {
/*  98 */       return new File(parentFile, children[index]);
/*     */     }
/*     */     
/* 101 */     return null;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public int getChildCount(Object parent)
/*     */   {
/* 108 */     if ((parent instanceof File)) {
/* 109 */       String[] children = ((File)parent).list();
/*     */       
/* 111 */       if (children != null) {
/* 112 */         return children.length;
/*     */       }
/*     */     }
/*     */     
/* 116 */     return 0;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public Class<?> getColumnClass(int column)
/*     */   {
/* 124 */     switch (column) {
/*     */     case 0: 
/* 126 */       return String.class;
/*     */     case 1: 
/* 128 */       return Long.class;
/*     */     case 2: 
/* 130 */       return Boolean.class;
/*     */     case 3: 
/* 132 */       return Date.class;
/*     */     }
/* 134 */     return super.getColumnClass(column);
/*     */   }
/*     */   
/*     */   public int getColumnCount()
/*     */   {
/* 139 */     return 4;
/*     */   }
/*     */   
/*     */   public String getColumnName(int column)
/*     */   {
/* 144 */     switch (column) {
/*     */     case 0: 
/* 146 */       return "Name";
/*     */     case 1: 
/* 148 */       return "Size";
/*     */     case 2: 
/* 150 */       return "Directory";
/*     */     case 3: 
/* 152 */       return "Modification Date";
/*     */     }
/* 154 */     return super.getColumnName(column);
/*     */   }
/*     */   
/*     */   public Object getValueAt(Object node, int column)
/*     */   {
/* 159 */     if ((node instanceof File)) {
/* 160 */       File file = (File)node;
/* 161 */       switch (column) {
/*     */       case 0: 
/* 163 */         return file.getName();
/*     */       case 1: 
/* 165 */         return Long.valueOf(isLeaf(node) ? file.length() : DIRECTORY.longValue());
/*     */       case 2: 
/* 167 */         return Boolean.valueOf(file.isDirectory());
/*     */       case 3: 
/* 169 */         return new Date(file.lastModified());
/*     */       }
/*     */       
/*     */     }
/* 173 */     return null;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public int getIndexOfChild(Object parent, Object child)
/*     */   {
/* 180 */     if (((parent instanceof File)) && ((child instanceof File))) {
/* 181 */       File parentFile = (File)parent;
/* 182 */       File[] files = parentFile.listFiles();
/*     */       
/* 184 */       Arrays.sort(files);
/*     */       
/* 186 */       int i = 0; for (int len = files.length; i < len; i++) {
/* 187 */         if (files[i].equals(child)) {
/* 188 */           return i;
/*     */         }
/*     */       }
/*     */     }
/*     */     
/* 193 */     return -1;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public File getRoot()
/*     */   {
/* 201 */     return (File)this.root;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setRoot(File root)
/*     */   {
/* 212 */     this.root = root;
/*     */     
/* 214 */     this.modelSupport.fireNewRoot();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean isLeaf(Object node)
/*     */   {
/* 222 */     if ((node instanceof File))
/*     */     {
/* 224 */       return ((File)node).list() == null;
/*     */     }
/*     */     
/* 227 */     return true;
/*     */   }
/*     */ }


/* Location:              E:\java\java学习\hutubill\lib\all.jar!\org\jdesktop\swingx\treetable\FileSystemModel.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */
/*     */ package org.jdesktop.swingx.tree;
/*     */ 
/*     */ import java.awt.Component;
/*     */ import java.awt.ComponentOrientation;
/*     */ import java.awt.Container;
/*     */ import java.awt.Dimension;
/*     */ import java.awt.Graphics;
/*     */ import javax.swing.Icon;
/*     */ import javax.swing.JComponent;
/*     */ import javax.swing.JTree;
/*     */ import javax.swing.SwingUtilities;
/*     */ import javax.swing.tree.DefaultTreeCellEditor;
/*     */ import javax.swing.tree.DefaultTreeCellEditor.EditorContainer;
/*     */ import javax.swing.tree.DefaultTreeCellRenderer;
/*     */ import javax.swing.tree.TreeCellEditor;
/*     */ import org.jdesktop.swingx.plaf.UIDependent;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class DefaultXTreeCellEditor
/*     */   extends DefaultTreeCellEditor
/*     */   implements UIDependent
/*     */ {
/*     */   public DefaultXTreeCellEditor(JTree tree, DefaultTreeCellRenderer renderer)
/*     */   {
/*  54 */     super(tree, renderer);
/*     */   }
/*     */   
/*     */   public DefaultXTreeCellEditor(JTree tree, DefaultTreeCellRenderer renderer, TreeCellEditor editor)
/*     */   {
/*  59 */     super(tree, renderer, editor);
/*     */   }
/*     */   
/*     */   public void setRenderer(DefaultTreeCellRenderer renderer) {
/*  63 */     this.renderer = renderer;
/*     */   }
/*     */   
/*     */ 
/*  67 */   public DefaultTreeCellRenderer getRenderer() { return this.renderer; }
/*     */   
/*     */   public class XEditorContainer extends DefaultTreeCellEditor.EditorContainer {
/*  70 */     public XEditorContainer() { super(); }
/*     */     
/*     */     public Dimension getPreferredSize()
/*     */     {
/*  74 */       if (DefaultXTreeCellEditor.this.isRightToLeft()) {
/*  75 */         if (DefaultXTreeCellEditor.this.editingComponent != null) {
/*  76 */           Dimension pSize = DefaultXTreeCellEditor.this.editingComponent.getPreferredSize();
/*     */           
/*  78 */           pSize.width += DefaultXTreeCellEditor.this.offset + 5;
/*     */           
/*  80 */           Dimension rSize = DefaultXTreeCellEditor.this.renderer != null ? DefaultXTreeCellEditor.this.renderer.getPreferredSize() : null;
/*     */           
/*     */ 
/*  83 */           if (rSize != null)
/*  84 */             pSize.height = Math.max(pSize.height, rSize.height);
/*  85 */           if (DefaultXTreeCellEditor.this.editingIcon != null) {
/*  86 */             pSize.height = Math.max(pSize.height, DefaultXTreeCellEditor.this.editingIcon.getIconHeight());
/*     */           }
/*     */           
/*     */ 
/*     */ 
/*     */ 
/*  92 */           return pSize;
/*     */         }
/*  94 */         return new Dimension(0, 0);
/*     */       }
/*  96 */       return super.getPreferredSize();
/*     */     }
/*     */     
/*     */ 
/*     */     public void doLayout()
/*     */     {
/* 102 */       if (DefaultXTreeCellEditor.this.isRightToLeft()) {
/* 103 */         Dimension cSize = getSize();
/*     */         
/* 105 */         DefaultXTreeCellEditor.this.editingComponent.getPreferredSize();
/* 106 */         DefaultXTreeCellEditor.this.editingComponent.setLocation(0, 0);
/* 107 */         DefaultXTreeCellEditor.this.editingComponent.setBounds(0, 0, cSize.width - DefaultXTreeCellEditor.this.offset, cSize.height);
/*     */ 
/*     */       }
/*     */       else
/*     */       {
/* 112 */         super.doLayout();
/*     */       }
/*     */     }
/*     */     
/*     */ 
/*     */     public void paint(Graphics g)
/*     */     {
/* 119 */       if (DefaultXTreeCellEditor.this.isRightToLeft()) {
/* 120 */         Dimension size = getSize();
/*     */         
/*     */ 
/* 123 */         if (DefaultXTreeCellEditor.this.editingIcon != null) {
/* 124 */           int yLoc = Math.max(0, (size.height - DefaultXTreeCellEditor.this.editingIcon.getIconHeight()) / 2);
/*     */           
/* 126 */           int xLoc = Math.max(0, size.width - DefaultXTreeCellEditor.this.offset);
/* 127 */           DefaultXTreeCellEditor.this.editingIcon.paintIcon(this, g, xLoc, yLoc);
/*     */         }
/*     */         
/* 130 */         Icon rememberIcon = DefaultXTreeCellEditor.this.editingIcon;
/* 131 */         DefaultXTreeCellEditor.this.editingIcon = null;
/* 132 */         super.paint(g);
/* 133 */         DefaultXTreeCellEditor.this.editingIcon = rememberIcon;
/*     */       }
/*     */       else {
/* 136 */         super.paint(g);
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   protected Container createContainer()
/*     */   {
/* 145 */     return new XEditorContainer();
/*     */   }
/*     */   
/*     */   protected void prepareForEditing()
/*     */   {
/* 150 */     super.prepareForEditing();
/* 151 */     applyComponentOrientation();
/*     */   }
/*     */   
/*     */   protected void applyComponentOrientation() {
/* 155 */     if (this.tree != null) {
/* 156 */       this.editingContainer.applyComponentOrientation(this.tree.getComponentOrientation());
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   private boolean isRightToLeft()
/*     */   {
/* 165 */     return (this.tree != null) && (!this.tree.getComponentOrientation().isLeftToRight());
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public void updateUI()
/*     */   {
/* 172 */     if (getRenderer() != null) {
/* 173 */       SwingUtilities.updateComponentTreeUI(getRenderer());
/*     */     }
/* 175 */     if ((this.realEditor instanceof JComponent)) {
/* 176 */       SwingUtilities.updateComponentTreeUI((JComponent)this.realEditor);
/* 177 */     } else if ((this.realEditor instanceof UIDependent)) {
/* 178 */       ((UIDependent)this.realEditor).updateUI();
/*     */     }
/*     */   }
/*     */ }


/* Location:              E:\java\java学习\hutubill\lib\all.jar!\org\jdesktop\swingx\tree\DefaultXTreeCellEditor.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */
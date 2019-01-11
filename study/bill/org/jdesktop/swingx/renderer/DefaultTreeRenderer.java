/*     */ package org.jdesktop.swingx.renderer;
/*     */ 
/*     */ import java.awt.Component;
/*     */ import javax.swing.JTree;
/*     */ import javax.swing.tree.TreeCellRenderer;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class DefaultTreeRenderer
/*     */   extends AbstractRenderer
/*     */   implements TreeCellRenderer
/*     */ {
/*     */   private TreeCellContext cellContext;
/*     */   
/*     */   public DefaultTreeRenderer()
/*     */   {
/*  50 */     this((ComponentProvider)null);
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
/*     */   public DefaultTreeRenderer(ComponentProvider<?> componentProvider)
/*     */   {
/*  63 */     super(componentProvider);
/*  64 */     this.cellContext = new TreeCellContext();
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
/*     */   public DefaultTreeRenderer(IconValue iv)
/*     */   {
/*  77 */     this(new WrappingProvider(iv));
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public DefaultTreeRenderer(StringValue sv)
/*     */   {
/*  89 */     this(new WrappingProvider(sv));
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
/*     */   public DefaultTreeRenderer(IconValue iv, StringValue sv)
/*     */   {
/* 106 */     this(new WrappingProvider(iv, sv));
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
/*     */   public DefaultTreeRenderer(IconValue iv, StringValue sv, boolean unwrapUserObject)
/*     */   {
/* 124 */     this(new WrappingProvider(iv, sv, unwrapUserObject));
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
/*     */   public Component getTreeCellRendererComponent(JTree tree, Object value, boolean selected, boolean expanded, boolean leaf, int row, boolean hasFocus)
/*     */   {
/* 145 */     this.cellContext.installContext(tree, value, row, 0, selected, hasFocus, expanded, leaf);
/*     */     
/* 147 */     Component comp = this.componentController.getRendererComponent(this.cellContext);
/*     */     
/* 149 */     this.cellContext.replaceValue(null);
/* 150 */     return comp;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected ComponentProvider<?> createDefaultComponentProvider()
/*     */   {
/* 159 */     return new WrappingProvider();
/*     */   }
/*     */ }


/* Location:              E:\java\java学习\hutubill\lib\all.jar!\org\jdesktop\swingx\renderer\DefaultTreeRenderer.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */
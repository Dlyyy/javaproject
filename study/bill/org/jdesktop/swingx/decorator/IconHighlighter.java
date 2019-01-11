/*     */ package org.jdesktop.swingx.decorator;
/*     */ 
/*     */ import java.awt.Component;
/*     */ import javax.swing.Icon;
/*     */ import javax.swing.JLabel;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class IconHighlighter
/*     */   extends AbstractHighlighter
/*     */ {
/*     */   private Icon icon;
/*     */   
/*     */   public IconHighlighter()
/*     */   {
/*  55 */     this((HighlightPredicate)null);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public IconHighlighter(HighlightPredicate predicate)
/*     */   {
/*  64 */     this(predicate, null);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public IconHighlighter(Icon icon)
/*     */   {
/*  75 */     this(null, icon);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public IconHighlighter(HighlightPredicate predicate, Icon icon)
/*     */   {
/*  87 */     super(predicate);
/*  88 */     setIcon(icon);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setIcon(Icon icon)
/*     */   {
/* 100 */     if (areEqual(icon, getIcon())) return;
/* 101 */     this.icon = icon;
/* 102 */     fireStateChanged();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public Icon getIcon()
/*     */   {
/* 112 */     return this.icon;
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
/*     */   protected Component doHighlight(Component component, ComponentAdapter adapter)
/*     */   {
/* 126 */     if (getIcon() != null) {
/* 127 */       ((JLabel)component).setIcon(getIcon());
/*     */     }
/* 129 */     return component;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected boolean canHighlight(Component component, ComponentAdapter adapter)
/*     */   {
/* 139 */     return component instanceof JLabel;
/*     */   }
/*     */ }


/* Location:              E:\java\java学习\hutubill\lib\all.jar!\org\jdesktop\swingx\decorator\IconHighlighter.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */
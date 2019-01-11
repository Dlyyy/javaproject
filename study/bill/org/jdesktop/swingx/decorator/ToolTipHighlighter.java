/*     */ package org.jdesktop.swingx.decorator;
/*     */ 
/*     */ import java.awt.Component;
/*     */ import javax.swing.JComponent;
/*     */ import org.jdesktop.swingx.renderer.StringValue;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class ToolTipHighlighter
/*     */   extends AbstractHighlighter
/*     */ {
/*     */   private StringValue toolTipValue;
/*     */   
/*     */   public ToolTipHighlighter()
/*     */   {
/*  43 */     this((HighlightPredicate)null);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public ToolTipHighlighter(StringValue toolTipValue)
/*     */   {
/*  54 */     this(null, toolTipValue);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public ToolTipHighlighter(HighlightPredicate predicate)
/*     */   {
/*  65 */     this(predicate, null);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public ToolTipHighlighter(HighlightPredicate predicate, StringValue toolTipValue)
/*     */   {
/*  77 */     super(predicate);
/*     */     
/*  79 */     this.toolTipValue = toolTipValue;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public StringValue getToolTipValue()
/*     */   {
/*  90 */     return this.toolTipValue;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setToolTipValue(StringValue toolTipValue)
/*     */   {
/* 101 */     if (areEqual(toolTipValue, getToolTipValue())) return;
/* 102 */     this.toolTipValue = toolTipValue;
/* 103 */     fireStateChanged();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected boolean canHighlight(Component component, ComponentAdapter adapter)
/*     */   {
/* 113 */     return component instanceof JComponent;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   protected Component doHighlight(Component component, ComponentAdapter adapter)
/*     */   {
/* 121 */     String toolTipText = null;
/*     */     
/* 123 */     if (this.toolTipValue == null) {
/* 124 */       toolTipText = adapter.getString();
/*     */     } else {
/* 126 */       toolTipText = this.toolTipValue.getString(adapter.getValue());
/*     */     }
/*     */     
/* 129 */     ((JComponent)component).setToolTipText(toolTipText);
/*     */     
/* 131 */     return component;
/*     */   }
/*     */ }


/* Location:              E:\java\java学习\hutubill\lib\all.jar!\org\jdesktop\swingx\decorator\ToolTipHighlighter.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */
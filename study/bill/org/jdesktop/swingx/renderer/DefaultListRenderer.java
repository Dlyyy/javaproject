/*     */ package org.jdesktop.swingx.renderer;
/*     */ 
/*     */ import java.awt.Component;
/*     */ import javax.swing.JList;
/*     */ import javax.swing.ListCellRenderer;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class DefaultListRenderer
/*     */   extends AbstractRenderer
/*     */   implements ListCellRenderer
/*     */ {
/*     */   protected ListCellContext cellContext;
/*     */   
/*     */   public DefaultListRenderer()
/*     */   {
/*  83 */     this((ComponentProvider)null);
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
/*     */   public DefaultListRenderer(ComponentProvider<?> componentProvider)
/*     */   {
/* 100 */     super(componentProvider);
/* 101 */     this.cellContext = new ListCellContext();
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
/*     */   public DefaultListRenderer(StringValue converter)
/*     */   {
/* 116 */     this(new LabelProvider(converter));
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
/*     */   public DefaultListRenderer(StringValue converter, int alignment)
/*     */   {
/* 133 */     this(new LabelProvider(converter, alignment));
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public DefaultListRenderer(StringValue stringValue, IconValue iconValue)
/*     */   {
/* 145 */     this(new MappedValue(stringValue, iconValue));
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
/*     */   public DefaultListRenderer(StringValue stringValue, IconValue iconValue, int alignment)
/*     */   {
/* 158 */     this(new MappedValue(stringValue, iconValue), alignment);
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
/*     */   public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus)
/*     */   {
/* 180 */     this.cellContext.installContext(list, value, index, 0, isSelected, cellHasFocus, true, true);
/*     */     
/* 182 */     Component comp = this.componentController.getRendererComponent(this.cellContext);
/*     */     
/* 184 */     this.cellContext.replaceValue(null);
/* 185 */     return comp;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   protected ComponentProvider<?> createDefaultComponentProvider()
/*     */   {
/* 193 */     return new LabelProvider(createDefaultStringValue());
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private StringValue createDefaultStringValue()
/*     */   {
/* 205 */     return MappedValues.STRING_OR_ICON_ONLY;
/*     */   }
/*     */ }


/* Location:              E:\java\java学习\hutubill\lib\all.jar!\org\jdesktop\swingx\renderer\DefaultListRenderer.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */
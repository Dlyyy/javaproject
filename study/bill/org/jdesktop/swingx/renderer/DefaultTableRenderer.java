/*     */ package org.jdesktop.swingx.renderer;
/*     */ 
/*     */ import java.awt.Component;
/*     */ import javax.swing.JTable;
/*     */ import javax.swing.table.TableCellRenderer;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class DefaultTableRenderer
/*     */   extends AbstractRenderer
/*     */   implements TableCellRenderer
/*     */ {
/*     */   private TableCellContext cellContext;
/*     */   
/*     */   public DefaultTableRenderer()
/*     */   {
/*  80 */     this((ComponentProvider)null);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public DefaultTableRenderer(ComponentProvider<?> componentProvider)
/*     */   {
/*  92 */     super(componentProvider);
/*  93 */     this.cellContext = new TableCellContext();
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
/*     */   public DefaultTableRenderer(StringValue converter)
/*     */   {
/* 106 */     this(new LabelProvider(converter));
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
/*     */   public DefaultTableRenderer(StringValue converter, int alignment)
/*     */   {
/* 120 */     this(new LabelProvider(converter, alignment));
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public DefaultTableRenderer(StringValue stringValue, IconValue iconValue)
/*     */   {
/* 131 */     this(new MappedValue(stringValue, iconValue));
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
/*     */   public DefaultTableRenderer(StringValue stringValue, IconValue iconValue, int alignment)
/*     */   {
/* 144 */     this(new MappedValue(stringValue, iconValue), alignment);
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
/*     */   public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column)
/*     */   {
/* 167 */     this.cellContext.installContext(table, value, row, column, isSelected, hasFocus, true, true);
/*     */     
/* 169 */     Component comp = this.componentController.getRendererComponent(this.cellContext);
/*     */     
/* 171 */     this.cellContext.replaceValue(null);
/* 172 */     return comp;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   protected ComponentProvider<?> createDefaultComponentProvider()
/*     */   {
/* 180 */     return new LabelProvider();
/*     */   }
/*     */ }


/* Location:              E:\java\java学习\hutubill\lib\all.jar!\org\jdesktop\swingx\renderer\DefaultTableRenderer.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */
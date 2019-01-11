/*     */ package org.jdesktop.swingx.renderer;
/*     */ 
/*     */ import java.io.Serializable;
/*     */ import javax.swing.Icon;
/*     */ import javax.swing.JComponent;
/*     */ import javax.swing.SwingUtilities;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public abstract class ComponentProvider<T extends JComponent>
/*     */   implements Serializable, UIDependent
/*     */ {
/*     */   protected T rendererComponent;
/*     */   protected DefaultVisuals<T> defaultVisuals;
/*     */   protected int alignment;
/*     */   protected StringValue formatter;
/*     */   
/*     */   public ComponentProvider()
/*     */   {
/* 136 */     this(null, 10);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public ComponentProvider(StringValue converter)
/*     */   {
/* 147 */     this(converter, 10);
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
/*     */   public ComponentProvider(StringValue converter, int alignment)
/*     */   {
/* 160 */     setHorizontalAlignment(alignment);
/* 161 */     setStringValue(converter);
/* 162 */     this.rendererComponent = createRendererComponent();
/* 163 */     this.defaultVisuals = createDefaultVisuals();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public T getRendererComponent(CellContext context)
/*     */   {
/* 175 */     if (context != null) {
/* 176 */       configureVisuals(context);
/* 177 */       configureContent(context);
/*     */     }
/* 179 */     return this.rendererComponent;
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
/*     */   public void setHorizontalAlignment(int alignment)
/*     */   {
/* 194 */     this.alignment = alignment;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public int getHorizontalAlignment()
/*     */   {
/* 206 */     return this.alignment;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setStringValue(StringValue formatter)
/*     */   {
/* 216 */     if (formatter == null) {
/* 217 */       formatter = StringValues.TO_STRING;
/*     */     }
/* 219 */     this.formatter = formatter;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public StringValue getStringValue()
/*     */   {
/* 230 */     return this.formatter;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public String getString(Object value)
/*     */   {
/* 262 */     return this.formatter.getString(value);
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
/*     */   protected String getValueAsString(CellContext context)
/*     */   {
/* 276 */     Object value = context.getValue();
/* 277 */     return this.formatter.getString(value);
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
/*     */   protected Icon getValueAsIcon(CellContext context)
/*     */   {
/* 292 */     Object value = context.getValue();
/* 293 */     if ((this.formatter instanceof IconValue)) {
/* 294 */       return ((IconValue)this.formatter).getIcon(value);
/*     */     }
/* 296 */     return null;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected void configureVisuals(CellContext context)
/*     */   {
/* 308 */     this.defaultVisuals.configureVisuals(this.rendererComponent, context);
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
/*     */   protected void configureContent(CellContext context)
/*     */   {
/* 321 */     configureState(context);
/* 322 */     format(context);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected abstract void format(CellContext paramCellContext);
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected abstract void configureState(CellContext paramCellContext);
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected abstract T createRendererComponent();
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected DefaultVisuals<T> createDefaultVisuals()
/*     */   {
/* 356 */     return new DefaultVisuals();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected DefaultVisuals<T> getDefaultVisuals()
/*     */   {
/* 365 */     return this.defaultVisuals;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public void updateUI()
/*     */   {
/* 372 */     SwingUtilities.updateComponentTreeUI(this.rendererComponent);
/*     */   }
/*     */ }


/* Location:              E:\java\java学习\hutubill\lib\all.jar!\org\jdesktop\swingx\renderer\ComponentProvider.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */
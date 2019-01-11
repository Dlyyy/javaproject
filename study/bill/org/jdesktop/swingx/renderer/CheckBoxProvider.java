/*     */ package org.jdesktop.swingx.renderer;
/*     */ 
/*     */ import javax.swing.AbstractButton;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class CheckBoxProvider
/*     */   extends ComponentProvider<AbstractButton>
/*     */ {
/*     */   private boolean borderPainted;
/*     */   
/*     */   public CheckBoxProvider()
/*     */   {
/*  76 */     this(null);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public CheckBoxProvider(StringValue stringValue)
/*     */   {
/*  86 */     this(stringValue, 0);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public CheckBoxProvider(StringValue stringValue, int alignment)
/*     */   {
/*  97 */     super(stringValue == null ? StringValues.EMPTY : stringValue, alignment);
/*  98 */     setBorderPainted(true);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean isBorderPainted()
/*     */   {
/* 108 */     return this.borderPainted;
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
/*     */   public void setBorderPainted(boolean borderPainted)
/*     */   {
/* 123 */     this.borderPainted = borderPainted;
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
/*     */   protected void format(CellContext context)
/*     */   {
/* 137 */     ((AbstractButton)this.rendererComponent).setSelected(getValueAsBoolean(context));
/* 138 */     ((AbstractButton)this.rendererComponent).setText(getValueAsString(context));
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
/*     */   protected boolean getValueAsBoolean(CellContext context)
/*     */   {
/* 157 */     if ((this.formatter instanceof BooleanValue)) {
/* 158 */       return ((BooleanValue)this.formatter).getBoolean(context.getValue());
/*     */     }
/* 160 */     return Boolean.TRUE.equals(context.getValue());
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected void configureState(CellContext context)
/*     */   {
/* 171 */     ((AbstractButton)this.rendererComponent).setBorderPainted(isBorderPainted());
/* 172 */     ((AbstractButton)this.rendererComponent).setHorizontalAlignment(getHorizontalAlignment());
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected AbstractButton createRendererComponent()
/*     */   {
/* 182 */     return new JRendererCheckBox();
/*     */   }
/*     */ }


/* Location:              E:\java\java学习\hutubill\lib\all.jar!\org\jdesktop\swingx\renderer\CheckBoxProvider.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */
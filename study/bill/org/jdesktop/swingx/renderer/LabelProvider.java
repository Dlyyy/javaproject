/*     */ package org.jdesktop.swingx.renderer;
/*     */ 
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
/*     */ public class LabelProvider
/*     */   extends ComponentProvider<JLabel>
/*     */ {
/*     */   public LabelProvider()
/*     */   {
/*  47 */     this(null);
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
/*     */   public LabelProvider(StringValue converter)
/*     */   {
/*  60 */     this(converter, 10);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public LabelProvider(int alignment)
/*     */   {
/*  70 */     this(null, alignment);
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
/*     */   public LabelProvider(StringValue converter, int alignment)
/*     */   {
/*  83 */     super(converter, alignment);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   protected JLabel createRendererComponent()
/*     */   {
/*  91 */     return new JRendererLabel();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected void configureState(CellContext context)
/*     */   {
/* 101 */     ((JLabel)this.rendererComponent).setHorizontalAlignment(getHorizontalAlignment());
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
/*     */   protected void format(CellContext context)
/*     */   {
/* 116 */     ((JLabel)this.rendererComponent).setIcon(getValueAsIcon(context));
/* 117 */     ((JLabel)this.rendererComponent).setText(getValueAsString(context));
/*     */   }
/*     */ }


/* Location:              E:\java\java学习\hutubill\lib\all.jar!\org\jdesktop\swingx\renderer\LabelProvider.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */
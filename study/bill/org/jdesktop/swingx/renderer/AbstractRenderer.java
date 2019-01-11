/*     */ package org.jdesktop.swingx.renderer;
/*     */ 
/*     */ import java.awt.Color;
/*     */ import java.io.Serializable;
/*     */ import org.jdesktop.swingx.plaf.UIDependent;
/*     */ import org.jdesktop.swingx.rollover.RolloverRenderer;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public abstract class AbstractRenderer
/*     */   implements RolloverRenderer, StringValue, Serializable, UIDependent
/*     */ {
/*     */   protected ComponentProvider<?> componentController;
/*     */   
/*     */   public AbstractRenderer(ComponentProvider<?> provider)
/*     */   {
/*  48 */     if (provider == null) {
/*  49 */       provider = createDefaultComponentProvider();
/*     */     }
/*  51 */     this.componentController = provider;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public ComponentProvider<?> getComponentProvider()
/*     */   {
/*  60 */     return this.componentController;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected abstract ComponentProvider<?> createDefaultComponentProvider();
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public String getString(Object value)
/*     */   {
/*  76 */     return this.componentController.getString(value);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void doClick()
/*     */   {
/*  85 */     if (isEnabled()) {
/*  86 */       ((RolloverRenderer)this.componentController).doClick();
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public boolean isEnabled()
/*     */   {
/*  94 */     return ((this.componentController instanceof RolloverRenderer)) && (((RolloverRenderer)this.componentController).isEnabled());
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void updateUI()
/*     */   {
/* 102 */     this.componentController.updateUI();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setBackground(Color background)
/*     */   {
/* 110 */     this.componentController.getDefaultVisuals().setBackground(background);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setForeground(Color foreground)
/*     */   {
/* 118 */     this.componentController.getDefaultVisuals().setForeground(foreground);
/*     */   }
/*     */ }


/* Location:              E:\java\java学习\hutubill\lib\all.jar!\org\jdesktop\swingx\renderer\AbstractRenderer.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */
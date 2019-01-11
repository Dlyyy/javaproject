/*     */ package org.jdesktop.swingx.renderer;
/*     */ 
/*     */ import javax.swing.BorderFactory;
/*     */ import javax.swing.Icon;
/*     */ import javax.swing.tree.DefaultMutableTreeNode;
/*     */ import org.jdesktop.swingx.rollover.RolloverRenderer;
/*     */ import org.jdesktop.swingx.treetable.TreeTableNode;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class WrappingProvider
/*     */   extends ComponentProvider<WrappingIconPanel>
/*     */   implements RolloverRenderer
/*     */ {
/*     */   protected ComponentProvider<?> wrappee;
/*     */   private boolean unwrapUserObject;
/*     */   
/*     */   public WrappingProvider()
/*     */   {
/*  62 */     this((ComponentProvider)null);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public WrappingProvider(IconValue iconValue, StringValue wrappeeStringValue)
/*     */   {
/*  74 */     this(iconValue, wrappeeStringValue, true);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public WrappingProvider(IconValue iconValue)
/*     */   {
/*  84 */     this(iconValue, null);
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
/*     */   public WrappingProvider(StringValue wrappeeStringValue)
/*     */   {
/*  98 */     this(null, wrappeeStringValue);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public WrappingProvider(ComponentProvider<?> delegate)
/*     */   {
/* 109 */     this(delegate, true);
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
/*     */   public WrappingProvider(ComponentProvider<?> delegate, boolean unwrapUserObject)
/*     */   {
/* 122 */     this(null, delegate, unwrapUserObject);
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
/*     */   public WrappingProvider(IconValue iv, ComponentProvider<?> delegate, boolean unwrapUserObject)
/*     */   {
/* 136 */     super(iv != null ? new MappedValue(null, iv) : StringValues.EMPTY);
/* 137 */     setWrappee(delegate);
/* 138 */     setUnwrapUserObject(unwrapUserObject);
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
/*     */   public WrappingProvider(IconValue iv, StringValue delegateStringValue, boolean unwrapUserObject)
/*     */   {
/* 152 */     this(iv, (ComponentProvider)null, unwrapUserObject);
/* 153 */     getWrappee().setStringValue(delegateStringValue);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setWrappee(ComponentProvider<?> delegate)
/*     */   {
/* 165 */     if (delegate == null) {
/* 166 */       delegate = new LabelProvider();
/*     */     }
/* 168 */     this.wrappee = delegate;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public ComponentProvider<?> getWrappee()
/*     */   {
/* 177 */     return this.wrappee;
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
/*     */   public void setUnwrapUserObject(boolean unwrap)
/*     */   {
/* 191 */     this.unwrapUserObject = unwrap;
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
/*     */   public boolean getUnwrapUserObject()
/*     */   {
/* 204 */     return this.unwrapUserObject;
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
/*     */   public String getString(Object value)
/*     */   {
/* 223 */     value = getUnwrappedValue(value);
/* 224 */     return this.wrappee.getString(value);
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
/*     */   protected Object getUnwrappedValue(Object value)
/*     */   {
/* 244 */     if (!getUnwrapUserObject()) return value;
/* 245 */     if ((value instanceof DefaultMutableTreeNode)) {
/* 246 */       value = ((DefaultMutableTreeNode)value).getUserObject();
/* 247 */     } else if ((value instanceof TreeTableNode)) {
/* 248 */       TreeTableNode node = (TreeTableNode)value;
/* 249 */       value = node.getUserObject();
/*     */     }
/* 251 */     return value;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public WrappingIconPanel getRendererComponent(CellContext context)
/*     */   {
/* 259 */     if (context != null) {
/* 260 */       ((WrappingIconPanel)this.rendererComponent).setComponent(this.wrappee.rendererComponent);
/* 261 */       Object oldValue = adjustContextValue(context);
/*     */       
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 267 */       super.getRendererComponent(context);
/* 268 */       this.wrappee.getRendererComponent(context);
/* 269 */       restoreContextValue(context, oldValue);
/* 270 */       return (WrappingIconPanel)this.rendererComponent;
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 277 */     return (WrappingIconPanel)super.getRendererComponent(context);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected void restoreContextValue(CellContext context, Object oldValue)
/*     */   {
/* 287 */     context.replaceValue(oldValue);
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
/*     */   protected Object adjustContextValue(CellContext context)
/*     */   {
/* 305 */     Object oldValue = context.getValue();
/* 306 */     if (getUnwrapUserObject()) {
/* 307 */       context.replaceValue(getUnwrappedValue(oldValue));
/*     */     }
/* 309 */     return oldValue;
/*     */   }
/*     */   
/*     */   protected void configureState(CellContext context)
/*     */   {
/* 314 */     ((WrappingIconPanel)this.rendererComponent).setBorder(BorderFactory.createEmptyBorder());
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected WrappingIconPanel createRendererComponent()
/*     */   {
/* 326 */     return new WrappingIconPanel();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected void format(CellContext context)
/*     */   {
/* 336 */     ((WrappingIconPanel)this.rendererComponent).setIcon(getValueAsIcon(context));
/*     */   }
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
/* 348 */     Icon icon = super.getValueAsIcon(context);
/* 349 */     if (icon == null) {
/* 350 */       return context.getIcon();
/*     */     }
/* 352 */     return IconValue.NULL_ICON == icon ? null : icon;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void doClick()
/*     */   {
/* 362 */     if (isEnabled()) {
/* 363 */       ((RolloverRenderer)this.wrappee).doClick();
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public boolean isEnabled()
/*     */   {
/* 371 */     return ((this.wrappee instanceof RolloverRenderer)) && (((RolloverRenderer)this.wrappee).isEnabled());
/*     */   }
/*     */ }


/* Location:              E:\java\java学习\hutubill\lib\all.jar!\org\jdesktop\swingx\renderer\WrappingProvider.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */
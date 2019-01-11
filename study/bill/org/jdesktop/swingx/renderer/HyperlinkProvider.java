/*     */ package org.jdesktop.swingx.renderer;
/*     */ 
/*     */ import java.awt.Point;
/*     */ import java.awt.event.ActionEvent;
/*     */ import javax.swing.ButtonModel;
/*     */ import javax.swing.JComponent;
/*     */ import org.jdesktop.swingx.JXHyperlink;
/*     */ import org.jdesktop.swingx.hyperlink.AbstractHyperlinkAction;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class HyperlinkProvider
/*     */   extends ComponentProvider<JXHyperlink>
/*     */   implements RolloverRenderer
/*     */ {
/*     */   private AbstractHyperlinkAction<Object> linkAction;
/*     */   protected Class<?> targetClass;
/*     */   
/*     */   public HyperlinkProvider()
/*     */   {
/*  73 */     this(null, null);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public HyperlinkProvider(AbstractHyperlinkAction linkAction)
/*     */   {
/*  83 */     this(linkAction, null);
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
/*     */   public HyperlinkProvider(AbstractHyperlinkAction linkAction, Class<?> targetClass)
/*     */   {
/*  98 */     setLinkAction(linkAction, targetClass);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setTargetClass(Class<?> targetClass)
/*     */   {
/* 109 */     this.targetClass = targetClass;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setLinkAction(AbstractHyperlinkAction linkAction)
/*     */   {
/* 121 */     setLinkAction(linkAction, null);
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
/*     */   public void setLinkAction(AbstractHyperlinkAction linkAction, Class<?> targetClass)
/*     */   {
/* 134 */     if (linkAction == null) {
/* 135 */       linkAction = createDefaultLinkAction();
/*     */     }
/* 137 */     setTargetClass(targetClass);
/* 138 */     this.linkAction = linkAction;
/* 139 */     ((JXHyperlink)this.rendererComponent).setAction(linkAction);
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
/*     */   public boolean isTargetable(Object target)
/*     */   {
/* 156 */     if (this.targetClass == null) return true;
/* 157 */     if (target == null) return true;
/* 158 */     return this.targetClass.isAssignableFrom(target.getClass());
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected AbstractHyperlinkAction createDefaultLinkAction()
/*     */   {
/* 169 */     new AbstractHyperlinkAction(null)
/*     */     {
/*     */       public void actionPerformed(ActionEvent e) {}
/*     */     };
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean isEnabled()
/*     */   {
/* 182 */     return true;
/*     */   }
/*     */   
/*     */   public void doClick() {
/* 186 */     ((JXHyperlink)this.rendererComponent).doClick();
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
/*     */   public String getString(Object value)
/*     */   {
/* 201 */     if (isTargetable(value)) {
/* 202 */       Object oldTarget = this.linkAction.getTarget();
/* 203 */       this.linkAction.setTarget(value);
/* 204 */       String text = this.linkAction.getName();
/* 205 */       this.linkAction.setTarget(oldTarget);
/* 206 */       return text;
/*     */     }
/* 208 */     return super.getString(value);
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
/*     */   protected void configureState(CellContext context)
/*     */   {
/* 221 */     if (context.getComponent() != null) {
/* 222 */       Point p = (Point)context.getComponent().getClientProperty("swingx.rollover");
/*     */       
/* 224 */       if ((p != null) && (p.x >= 0) && (p.x == context.getColumn()) && (p.y == context.getRow()))
/*     */       {
/* 226 */         if (!((JXHyperlink)this.rendererComponent).getModel().isRollover()) {
/* 227 */           ((JXHyperlink)this.rendererComponent).getModel().setRollover(true);
/*     */         }
/* 229 */       } else if (((JXHyperlink)this.rendererComponent).getModel().isRollover()) {
/* 230 */         ((JXHyperlink)this.rendererComponent).getModel().setRollover(false);
/*     */       }
/*     */     }
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
/*     */   protected void format(CellContext context)
/*     */   {
/* 254 */     Object value = context.getValue();
/* 255 */     if (isTargetable(value)) {
/* 256 */       this.linkAction.setTarget(value);
/*     */     } else {
/* 258 */       this.linkAction.setTarget(null);
/*     */     }
/*     */     
/*     */ 
/*     */ 
/* 263 */     if (!context.isSelected()) {
/* 264 */       ((JXHyperlink)this.rendererComponent).setForeground(this.linkAction.isVisited() ? ((JXHyperlink)this.rendererComponent).getClickedColor() : ((JXHyperlink)this.rendererComponent).getUnclickedColor());
/*     */ 
/*     */     }
/*     */     else
/*     */     {
/* 269 */       ((JXHyperlink)this.rendererComponent).setForeground(context.getSelectionForeground());
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   protected JXHyperlink createRendererComponent()
/*     */   {
/* 278 */     return new JXRendererHyperlink();
/*     */   }
/*     */ }


/* Location:              E:\java\java学习\hutubill\lib\all.jar!\org\jdesktop\swingx\renderer\HyperlinkProvider.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */
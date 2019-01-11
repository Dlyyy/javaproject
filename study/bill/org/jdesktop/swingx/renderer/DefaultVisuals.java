/*     */ package org.jdesktop.swingx.renderer;
/*     */ 
/*     */ import java.awt.Color;
/*     */ import java.io.Serializable;
/*     */ import javax.swing.JComponent;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class DefaultVisuals<T extends JComponent>
/*     */   implements Serializable
/*     */ {
/*     */   private Color unselectedForeground;
/*     */   private Color unselectedBackground;
/*     */   
/*     */   public void setForeground(Color c)
/*     */   {
/*  82 */     this.unselectedForeground = c;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setBackground(Color c)
/*     */   {
/*  93 */     this.unselectedBackground = c;
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
/*     */   public void configureVisuals(T renderingComponent, CellContext context)
/*     */   {
/* 108 */     configureState(renderingComponent, context);
/* 109 */     configureColors(renderingComponent, context);
/* 110 */     configureBorder(renderingComponent, context);
/* 111 */     configurePainter(renderingComponent, context);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected void configurePainter(T renderingComponent, CellContext context)
/*     */   {
/* 121 */     if ((renderingComponent instanceof PainterAware)) {
/* 122 */       ((PainterAware)renderingComponent).setPainter(null);
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
/*     */ 
/*     */   protected void configureState(T renderingComponent, CellContext context)
/*     */   {
/* 146 */     renderingComponent.setName(context.getCellRendererName());
/* 147 */     renderingComponent.setToolTipText(null);
/* 148 */     configureSizes(renderingComponent, context);
/*     */     
/*     */ 
/*     */ 
/* 152 */     renderingComponent.setFont(context.getFont());
/* 153 */     if (context.getComponent() != null)
/*     */     {
/*     */ 
/*     */ 
/*     */ 
/* 158 */       renderingComponent.setEnabled(context.getComponent().isEnabled());
/* 159 */       renderingComponent.applyComponentOrientation(context.getComponent().getComponentOrientation());
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
/*     */   protected void configureSizes(T renderingComponent, CellContext context)
/*     */   {
/* 173 */     renderingComponent.setPreferredSize(null);
/* 174 */     renderingComponent.setMinimumSize(null);
/* 175 */     renderingComponent.setMaximumSize(null);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected void configureColors(T renderingComponent, CellContext context)
/*     */   {
/* 185 */     if (context.isSelected()) {
/* 186 */       renderingComponent.setForeground(context.getSelectionForeground());
/* 187 */       renderingComponent.setBackground(context.getSelectionBackground());
/*     */     } else {
/* 189 */       renderingComponent.setForeground(getForeground(context));
/* 190 */       renderingComponent.setBackground(getBackground(context));
/*     */     }
/* 192 */     if (context.isFocused()) {
/* 193 */       configureFocusColors(renderingComponent, context);
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
/*     */   protected void configureFocusColors(T renderingComponent, CellContext context)
/*     */   {
/* 206 */     if ((!context.isSelected()) && (context.isEditable())) {
/* 207 */       Color col = context.getFocusForeground();
/* 208 */       if (col != null) {
/* 209 */         renderingComponent.setForeground(col);
/*     */       }
/* 211 */       col = context.getFocusBackground();
/* 212 */       if (col != null) {
/* 213 */         renderingComponent.setBackground(col);
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
/*     */   protected void configureBorder(T renderingComponent, CellContext context)
/*     */   {
/* 226 */     renderingComponent.setBorder(context.getBorder());
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
/*     */   protected Color getForeground(CellContext context)
/*     */   {
/* 241 */     if (this.unselectedForeground != null)
/* 242 */       return this.unselectedForeground;
/* 243 */     return context.getForeground();
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
/*     */   protected Color getBackground(CellContext context)
/*     */   {
/* 258 */     if (this.unselectedBackground != null)
/* 259 */       return this.unselectedBackground;
/* 260 */     return context.getBackground();
/*     */   }
/*     */ }


/* Location:              E:\java\java学习\hutubill\lib\all.jar!\org\jdesktop\swingx\renderer\DefaultVisuals.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */
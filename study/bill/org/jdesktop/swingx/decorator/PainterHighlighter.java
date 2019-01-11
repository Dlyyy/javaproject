/*     */ package org.jdesktop.swingx.decorator;
/*     */ 
/*     */ import java.awt.Component;
/*     */ import java.beans.PropertyChangeEvent;
/*     */ import java.beans.PropertyChangeListener;
/*     */ import org.jdesktop.swingx.painter.AbstractPainter;
/*     */ import org.jdesktop.swingx.painter.Painter;
/*     */ import org.jdesktop.swingx.renderer.PainterAware;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class PainterHighlighter
/*     */   extends AbstractHighlighter
/*     */ {
/*     */   private Painter painter;
/*     */   private PropertyChangeListener painterListener;
/*     */   private boolean isAdjusting;
/*     */   
/*     */   public PainterHighlighter()
/*     */   {
/*  93 */     this(null, null);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public PainterHighlighter(HighlightPredicate predicate)
/*     */   {
/* 103 */     this(predicate, null);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public PainterHighlighter(Painter painter)
/*     */   {
/* 113 */     this(null, painter);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public PainterHighlighter(HighlightPredicate predicate, Painter painter)
/*     */   {
/* 123 */     super(predicate);
/* 124 */     setPainter(painter);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public Painter getPainter()
/*     */   {
/* 135 */     return this.painter;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setPainter(Painter painter)
/*     */   {
/* 145 */     if (areEqual(painter, getPainter())) return;
/* 146 */     uninstallPainterListener();
/* 147 */     this.painter = painter;
/* 148 */     installPainterListener();
/* 149 */     fireStateChanged();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected void installPainterListener()
/*     */   {
/* 158 */     if ((getPainter() instanceof AbstractPainter)) {
/* 159 */       ((AbstractPainter)getPainter()).addPropertyChangeListener(getPainterListener());
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected void uninstallPainterListener()
/*     */   {
/* 169 */     if ((getPainter() instanceof AbstractPainter)) {
/* 170 */       ((AbstractPainter)getPainter()).removePropertyChangeListener(this.painterListener);
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
/*     */   protected final PropertyChangeListener getPainterListener()
/*     */   {
/* 183 */     if (this.painterListener == null) {
/* 184 */       this.painterListener = createPainterListener();
/*     */     }
/* 186 */     return this.painterListener;
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
/*     */   protected PropertyChangeListener createPainterListener()
/*     */   {
/* 201 */     PropertyChangeListener l = new PropertyChangeListener()
/*     */     {
/*     */       public void propertyChange(PropertyChangeEvent evt) {
/* 204 */         if (PainterHighlighter.this.isAdjusting) return;
/* 205 */         PainterHighlighter.this.fireStateChanged();
/*     */       }
/*     */       
/* 208 */     };
/* 209 */     return l;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public Component highlight(Component component, ComponentAdapter adapter)
/*     */   {
/* 221 */     this.isAdjusting = true;
/* 222 */     Component stamp = super.highlight(component, adapter);
/* 223 */     this.isAdjusting = false;
/* 224 */     return stamp;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected Component doHighlight(Component component, ComponentAdapter adapter)
/*     */   {
/* 236 */     ((PainterAware)component).setPainter(this.painter);
/* 237 */     return component;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected boolean canHighlight(Component component, ComponentAdapter adapter)
/*     */   {
/* 248 */     return (getPainter() != null) && ((component instanceof PainterAware));
/*     */   }
/*     */ }


/* Location:              E:\java\java学习\hutubill\lib\all.jar!\org\jdesktop\swingx\decorator\PainterHighlighter.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */
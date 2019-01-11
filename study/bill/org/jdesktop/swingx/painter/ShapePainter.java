/*     */ package org.jdesktop.swingx.painter;
/*     */ 
/*     */ import java.awt.BasicStroke;
/*     */ import java.awt.Color;
/*     */ import java.awt.Graphics2D;
/*     */ import java.awt.Paint;
/*     */ import java.awt.Rectangle;
/*     */ import java.awt.Shape;
/*     */ import java.awt.Stroke;
/*     */ import java.awt.geom.Ellipse2D.Double;
/*     */ import javax.swing.JComponent;
/*     */ import org.jdesktop.swingx.painter.effects.AreaEffect;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class ShapePainter
/*     */   extends AbstractAreaPainter<Object>
/*     */ {
/*     */   private Shape shape;
/*     */   
/*     */   public ShapePainter()
/*     */   {
/*  66 */     this.shape = new Ellipse2D.Double(0.0D, 0.0D, 100.0D, 100.0D);
/*  67 */     setBorderWidth(3.0F);
/*  68 */     setFillPaint(Color.RED);
/*  69 */     setBorderPaint(Color.BLACK);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public ShapePainter(Shape shape)
/*     */   {
/*  80 */     this.shape = shape;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public ShapePainter(Shape shape, Paint paint)
/*     */   {
/*  92 */     this.shape = shape;
/*  93 */     setFillPaint(paint);
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
/*     */   public ShapePainter(Shape shape, Paint paint, AbstractAreaPainter.Style style)
/*     */   {
/* 108 */     this.shape = shape;
/* 109 */     setFillPaint(paint);
/* 110 */     setStyle(style == null ? AbstractAreaPainter.Style.BOTH : style);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setShape(Shape s)
/*     */   {
/* 122 */     Shape old = getShape();
/* 123 */     this.shape = s;
/* 124 */     setDirty(true);
/* 125 */     firePropertyChange("shape", old, getShape());
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public Shape getShape()
/*     */   {
/* 133 */     return this.shape;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected void doPaint(Graphics2D g, Object component, int w, int h)
/*     */   {
/* 142 */     Stroke s = new BasicStroke(getBorderWidth());
/* 143 */     g.setStroke(s);
/*     */     
/* 145 */     if (getShape() != null) {
/* 146 */       Shape shape = provideShape(g, component, w, h);
/* 147 */       Rectangle bounds = shape.getBounds();
/* 148 */       Rectangle rect = calculateLayout(bounds.width, bounds.height, w, h);
/*     */       
/* 150 */       g = (Graphics2D)g.create();
/*     */       try
/*     */       {
/* 153 */         g.translate(rect.x, rect.y);
/*     */         
/* 155 */         drawPathEffects(g, shape, rect.width, rect.height);
/* 156 */         switch (getStyle()) {
/*     */         case BOTH: 
/* 158 */           drawShape(g, shape, component, rect.width, rect.height);
/* 159 */           fillShape(g, shape, component, rect.width, rect.height);
/* 160 */           break;
/*     */         case FILLED: 
/* 162 */           fillShape(g, shape, component, rect.width, rect.height);
/* 163 */           break;
/*     */         case OUTLINE: 
/* 165 */           drawShape(g, shape, component, rect.width, rect.height);
/*     */         }
/*     */       }
/*     */       finally {
/* 169 */         g.dispose();
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   private void drawShape(Graphics2D g, Shape shape, Object component, int w, int h) {
/* 175 */     g.setPaint(calculateStrokePaint(component, w, h));
/* 176 */     g.draw(shape);
/*     */   }
/*     */   
/*     */   private void fillShape(Graphics2D g, Shape shape, Object component, int w, int h) {
/* 180 */     g.setPaint(calculateFillPaint(component, w, h));
/* 181 */     g.fill(shape);
/*     */   }
/*     */   
/*     */ 
/*     */   protected Shape provideShape(Graphics2D g, Object comp, int width, int height)
/*     */   {
/* 187 */     return getShape();
/*     */   }
/*     */   
/*     */   private Paint calculateStrokePaint(Object component, int width, int height) {
/* 191 */     Paint p = getBorderPaint();
/* 192 */     if ((p == null) && 
/* 193 */       ((component instanceof JComponent))) {
/* 194 */       p = ((JComponent)component).getForeground();
/*     */     }
/*     */     
/* 197 */     if (isPaintStretched()) {
/* 198 */       p = calculateSnappedPaint(p, width, height);
/*     */     }
/* 200 */     return p;
/*     */   }
/*     */   
/*     */   private Paint calculateFillPaint(Object component, int width, int height)
/*     */   {
/* 205 */     Paint p = getFillPaint();
/* 206 */     if (isPaintStretched()) {
/* 207 */       p = calculateSnappedPaint(p, width, height);
/*     */     }
/*     */     
/* 210 */     if ((p == null) && 
/* 211 */       ((component instanceof JComponent))) {
/* 212 */       p = ((JComponent)component).getBackground();
/*     */     }
/*     */     
/* 215 */     return p;
/*     */   }
/*     */   
/*     */   private void drawPathEffects(Graphics2D g, Shape shape, int w, int h) {
/* 219 */     if (getAreaEffects() != null)
/*     */     {
/* 221 */       for (AreaEffect ef : getAreaEffects()) {
/* 222 */         ef.apply(g, shape, w, h);
/*     */       }
/*     */     }
/*     */   }
/*     */ }


/* Location:              E:\java\java学习\hutubill\lib\all.jar!\org\jdesktop\swingx\painter\ShapePainter.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */
/*     */ package org.jdesktop.swingx.painter;
/*     */ 
/*     */ import java.awt.BasicStroke;
/*     */ import java.awt.Color;
/*     */ import java.awt.Graphics2D;
/*     */ import java.awt.Insets;
/*     */ import java.awt.Paint;
/*     */ import java.awt.Rectangle;
/*     */ import java.awt.Shape;
/*     */ import java.awt.geom.Rectangle2D;
/*     */ import java.awt.geom.Rectangle2D.Double;
/*     */ import java.awt.geom.RectangularShape;
/*     */ import java.awt.geom.RoundRectangle2D;
/*     */ import java.awt.geom.RoundRectangle2D.Double;
/*     */ import org.jdesktop.swingx.graphics.GraphicsUtilities;
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
/*     */ public class RectanglePainter
/*     */   extends AbstractAreaPainter<Object>
/*     */ {
/*  46 */   private boolean rounded = false;
/*     */   
/*  48 */   private int roundWidth = 20;
/*  49 */   private int roundHeight = 20;
/*  50 */   private int width = -1;
/*  51 */   private int height = -1;
/*     */   
/*     */ 
/*     */   public RectanglePainter()
/*     */   {
/*  56 */     this(0, 0, 0, 0, 0, 0, false, Color.RED, 1.0F, Color.BLACK);
/*     */   }
/*     */   
/*     */   public RectanglePainter(Color fillPaint, Color borderPaint) {
/*  60 */     this(0, 0, 0, 0, 0, 0, false, fillPaint, 1.0F, borderPaint);
/*     */   }
/*     */   
/*     */   public RectanglePainter(Paint fillPaint, Paint borderPaint, float borderWidth, AbstractAreaPainter.Style style) {
/*  64 */     this();
/*  65 */     setFillPaint(fillPaint);
/*  66 */     setBorderPaint(borderPaint);
/*  67 */     setBorderWidth(borderWidth);
/*  68 */     setStyle(style);
/*     */   }
/*     */   
/*  71 */   public RectanglePainter(int top, int left, int bottom, int right) { this(top, left, bottom, right, 0, 0, false, Color.RED, 1.0F, Color.BLACK); }
/*     */   
/*     */   public RectanglePainter(int top, int left, int bottom, int right, int roundWidth, int roundHeight)
/*     */   {
/*  75 */     this(top, left, bottom, right, roundWidth, roundHeight, true, Color.RED, 1.0F, Color.BLACK);
/*     */   }
/*     */   
/*     */   public RectanglePainter(int width, int height, int cornerRadius, Paint fillPaint) {
/*  79 */     this(new Insets(0, 0, 0, 0), width, height, cornerRadius, cornerRadius, true, fillPaint, 1.0F, Color.BLACK);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public RectanglePainter(Insets insets, int width, int height, int roundWidth, int roundHeight, boolean rounded, Paint fillPaint, float strokeWidth, Paint borderPaint)
/*     */   {
/*  88 */     this.width = width;
/*  89 */     this.height = height;
/*  90 */     setFillHorizontal(false);
/*  91 */     setFillVertical(false);
/*  92 */     setInsets(insets);
/*  93 */     this.roundWidth = roundWidth;
/*  94 */     this.roundHeight = roundHeight;
/*  95 */     this.rounded = rounded;
/*  96 */     setFillPaint(fillPaint);
/*  97 */     setBorderWidth(strokeWidth);
/*  98 */     setBorderPaint(borderPaint);
/*     */   }
/*     */   
/*     */ 
/*     */   public RectanglePainter(int top, int left, int bottom, int right, int roundWidth, int roundHeight, boolean rounded, Paint fillPaint, float strokeWidth, Paint borderPaint)
/*     */   {
/* 104 */     setInsets(new Insets(top, left, bottom, right));
/* 105 */     setFillVertical(true);
/* 106 */     setFillHorizontal(true);
/* 107 */     this.roundWidth = roundWidth;
/* 108 */     this.roundHeight = roundHeight;
/* 109 */     this.rounded = rounded;
/* 110 */     setFillPaint(fillPaint);
/* 111 */     setBorderWidth(strokeWidth);
/* 112 */     setBorderPaint(borderPaint);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean isRounded()
/*     */   {
/* 123 */     return this.rounded;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setRounded(boolean rounded)
/*     */   {
/* 131 */     boolean oldRounded = isRounded();
/* 132 */     this.rounded = rounded;
/* 133 */     setDirty(true);
/* 134 */     firePropertyChange("rounded", Boolean.valueOf(oldRounded), Boolean.valueOf(rounded));
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public int getRoundWidth()
/*     */   {
/* 142 */     return this.roundWidth;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setRoundWidth(int roundWidth)
/*     */   {
/* 150 */     int oldRoundWidth = getRoundWidth();
/* 151 */     this.roundWidth = roundWidth;
/* 152 */     setDirty(true);
/* 153 */     firePropertyChange("roundWidth", Integer.valueOf(oldRoundWidth), Integer.valueOf(roundWidth));
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public int getRoundHeight()
/*     */   {
/* 161 */     return this.roundHeight;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setRoundHeight(int roundHeight)
/*     */   {
/* 169 */     int oldRoundHeight = getRoundHeight();
/* 170 */     this.roundHeight = roundHeight;
/* 171 */     setDirty(true);
/* 172 */     firePropertyChange("roundHeight", Integer.valueOf(oldRoundHeight), Integer.valueOf(roundHeight));
/*     */   }
/*     */   
/*     */ 
/*     */   protected RectangularShape calculateShape(int width, int height)
/*     */   {
/* 178 */     Insets insets = getInsets();
/* 179 */     int x = insets.left;
/* 180 */     int y = insets.top;
/*     */     
/*     */ 
/* 183 */     Rectangle bounds = calculateLayout(this.width, this.height, width, height);
/* 184 */     if ((this.width != -1) && (!isFillHorizontal())) {
/* 185 */       width = this.width;
/* 186 */       x = bounds.x;
/*     */     }
/* 188 */     if ((this.height != -1) && (!isFillVertical())) {
/* 189 */       height = this.height;
/* 190 */       y = bounds.y;
/*     */     }
/*     */     
/* 193 */     if (isFillHorizontal()) {
/* 194 */       width = width - insets.left - insets.right;
/*     */     }
/* 196 */     if (isFillVertical()) {
/* 197 */       height = height - insets.top - insets.bottom;
/*     */     }
/*     */     
/*     */ 
/* 201 */     RectangularShape shape = new Rectangle2D.Double(x, y, width, height);
/* 202 */     if (this.rounded) {
/* 203 */       shape = new RoundRectangle2D.Double(x, y, width, height, this.roundWidth, this.roundHeight);
/*     */     }
/* 205 */     return shape;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   protected void doPaint(Graphics2D g, Object component, int width, int height)
/*     */   {
/* 212 */     Shape shape = provideShape(g, component, width, height);
/* 213 */     switch (getStyle()) {
/*     */     case BOTH: 
/* 215 */       drawBackground(g, shape, width, height);
/* 216 */       drawBorder(g, shape, width, height);
/* 217 */       break;
/*     */     case FILLED: 
/* 219 */       drawBackground(g, shape, width, height);
/* 220 */       break;
/*     */     case OUTLINE: 
/* 222 */       drawBorder(g, shape, width, height);
/* 223 */       break;
/*     */     }
/*     */     
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 231 */     GraphicsUtilities.mergeClip(g, shape);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private void drawBorder(Graphics2D g, Shape shape, int width, int height)
/*     */   {
/* 240 */     Paint p = getBorderPaint();
/* 241 */     if (isPaintStretched()) {
/* 242 */       p = calculateSnappedPaint(p, width, height);
/*     */     }
/*     */     
/* 245 */     g.setPaint(p);
/*     */     
/* 247 */     g.setStroke(new BasicStroke(getBorderWidth()));
/*     */     
/*     */ 
/* 250 */     if ((shape instanceof Rectangle2D)) {
/* 251 */       Rectangle2D rect = (Rectangle2D)shape;
/*     */       
/* 253 */       g.draw(new Rectangle2D.Double(rect.getX(), rect.getY(), rect.getWidth() - 1.0D, rect.getHeight() - 1.0D));
/*     */     }
/* 255 */     else if ((shape instanceof RoundRectangle2D)) {
/* 256 */       RoundRectangle2D rect = (RoundRectangle2D)shape;
/*     */       
/* 258 */       g.draw(new RoundRectangle2D.Double(rect.getX(), rect.getY(), rect.getWidth() - 1.0D, rect.getHeight() - 1.0D, rect.getArcWidth(), rect.getArcHeight()));
/*     */     }
/*     */     else
/*     */     {
/* 262 */       g.draw(shape);
/*     */     }
/*     */   }
/*     */   
/*     */   private void drawBackground(Graphics2D g, Shape shape, int width, int height) {
/* 267 */     Paint p = getFillPaint();
/* 268 */     if (isPaintStretched()) {
/* 269 */       p = calculateSnappedPaint(p, width, height);
/*     */     }
/*     */     
/* 272 */     g.setPaint(p);
/*     */     
/* 274 */     g.fill(shape);
/* 275 */     if (getAreaEffects() != null) {
/* 276 */       for (AreaEffect ef : getAreaEffects()) {
/* 277 */         ef.apply(g, shape, width, height);
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   protected Shape provideShape(Graphics2D g, Object comp, int width, int height)
/*     */   {
/* 284 */     return calculateShape(width, height);
/*     */   }
/*     */ }


/* Location:              E:\java\java学习\hutubill\lib\all.jar!\org\jdesktop\swingx\painter\RectanglePainter.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */
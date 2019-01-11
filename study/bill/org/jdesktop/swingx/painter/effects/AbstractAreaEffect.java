/*     */ package org.jdesktop.swingx.painter.effects;
/*     */ 
/*     */ import java.awt.AlphaComposite;
/*     */ import java.awt.BasicStroke;
/*     */ import java.awt.Color;
/*     */ import java.awt.Graphics;
/*     */ import java.awt.Graphics2D;
/*     */ import java.awt.Point;
/*     */ import java.awt.Rectangle;
/*     */ import java.awt.RenderingHints;
/*     */ import java.awt.Shape;
/*     */ import java.awt.geom.Area;
/*     */ import java.awt.geom.Point2D;
/*     */ import java.awt.geom.Rectangle2D;
/*     */ import java.awt.image.BufferedImage;
/*     */ import java.beans.PropertyChangeListener;
/*     */ import java.beans.PropertyChangeSupport;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class AbstractAreaEffect
/*     */   implements AreaEffect
/*     */ {
/*     */   private static final boolean debug = false;
/*     */   
/*     */   public AbstractAreaEffect()
/*     */   {
/*  50 */     setBrushColor(Color.BLACK);
/*  51 */     setBrushSteps(10);
/*  52 */     setEffectWidth(8);
/*  53 */     setRenderInsideShape(false);
/*  54 */     setOffset(new Point(4, 4));
/*  55 */     setShouldFillShape(true);
/*  56 */     setShapeMasked(true);
/*     */   }
/*     */   
/*     */   public void apply(Graphics2D g, Shape clipShape, int width, int height)
/*     */   {
/*  61 */     width = (int)(clipShape.getBounds2D().getWidth() + clipShape.getBounds2D().getX());
/*  62 */     height = (int)(clipShape.getBounds2D().getHeight() + clipShape.getBounds2D().getY());
/*  63 */     Rectangle effectBounds = new Rectangle(0, 0, width + getEffectWidth() * 2 + 1, height + getEffectWidth() * 2 + 1);
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*  68 */     if (isShapeMasked()) {
/*  69 */       BufferedImage clipImage = getClipImage(effectBounds);
/*  70 */       Graphics2D g2 = clipImage.createGraphics();
/*     */       
/*     */       try
/*     */       {
/*  74 */         g2.setPaint(Color.BLACK);
/*  75 */         g2.setComposite(AlphaComposite.Clear);
/*  76 */         g2.fillRect(0, 0, effectBounds.width, effectBounds.height);
/*     */         
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*  86 */         g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
/*     */         
/*  88 */         g2.translate(getEffectWidth() - getOffset().getX(), getEffectWidth() - getOffset().getY());
/*     */         
/*  90 */         paintBorderGlow(g2, clipShape, width, height);
/*     */         
/*     */ 
/*  93 */         g2.setComposite(AlphaComposite.Clear);
/*  94 */         g2.setColor(Color.WHITE);
/*  95 */         if (isRenderInsideShape())
/*     */         {
/*  97 */           Area area = new Area(effectBounds);
/*  98 */           area.subtract(new Area(clipShape));
/*  99 */           g2.fill(area);
/*     */         }
/*     */         else {
/* 102 */           g2.fill(clipShape);
/*     */         }
/*     */       }
/*     */       finally {
/* 106 */         g2.dispose();
/*     */       }
/*     */       
/* 109 */       g.drawImage(clipImage, -getEffectWidth() + (int)getOffset().getX(), -getEffectWidth() + (int)getOffset().getY(), null);
/*     */     } else {
/* 111 */       g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
/* 112 */       paintBorderGlow(g, clipShape, width, height);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 121 */   BufferedImage _clipImage = null;
/*     */   private Color brushColor;
/*     */   
/* 124 */   private BufferedImage getClipImage(Rectangle effectBounds) { if ((this._clipImage == null) || (this._clipImage.getWidth() != effectBounds.width) || (this._clipImage.getHeight() != effectBounds.height))
/*     */     {
/*     */ 
/* 127 */       this._clipImage = new BufferedImage(effectBounds.width, effectBounds.height, 2);
/*     */     }
/*     */     
/*     */ 
/* 131 */     this._clipImage.getGraphics().clearRect(0, 0, this._clipImage.getWidth(), this._clipImage.getHeight());
/* 132 */     return this._clipImage;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected void paintBorderGlow(Graphics2D g2, Shape clipShape, int width, int height)
/*     */   {
/* 175 */     int steps = getBrushSteps();
/* 176 */     float brushAlpha = 1.0F / steps;
/*     */     
/* 178 */     boolean inside = isRenderInsideShape();
/*     */     
/* 180 */     g2.setPaint(getBrushColor());
/*     */     
/* 182 */     g2.translate(this.offset.getX(), this.offset.getY());
/*     */     
/* 184 */     if (isShouldFillShape())
/*     */     {
/* 186 */       if (inside) {
/* 187 */         g2.setComposite(AlphaComposite.getInstance(10, 1.0F));
/* 188 */         Area a1 = new Area(new Rectangle((int)-this.offset.getX() - 20, (int)-this.offset.getY() - 20, width + 40, height + 40));
/*     */         
/*     */ 
/*     */ 
/* 192 */         Area a2 = new Area(clipShape);
/* 193 */         a1.subtract(a2);
/* 194 */         g2.fill(a1);
/*     */       } else {
/* 196 */         g2.setComposite(AlphaComposite.getInstance(4, 1.0F));
/* 197 */         g2.fill(clipShape);
/*     */       }
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 209 */     g2.setComposite(AlphaComposite.getInstance(4, brushAlpha));
/*     */     
/*     */ 
/* 212 */     for (float i = 0.0F; i < steps; i += 1.0F) {
/* 213 */       float brushWidth = i * this.effectWidth / steps;
/* 214 */       g2.setStroke(new BasicStroke(brushWidth, 1, 1));
/*     */       
/* 216 */       g2.draw(clipShape);
/*     */     }
/* 218 */     g2.translate(-this.offset.getX(), -this.offset.getY());
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
/* 230 */   private PropertyChangeSupport propertyChangeSupport = new PropertyChangeSupport(this);
/*     */   private int brushSteps;
/*     */   private int effectWidth;
/*     */   private boolean renderInsideShape;
/*     */   
/*     */   public void addPropertyChangeListener(PropertyChangeListener l)
/*     */   {
/* 237 */     this.propertyChangeSupport.addPropertyChangeListener(l);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void removePropertyChangeListener(PropertyChangeListener l)
/*     */   {
/* 245 */     this.propertyChangeSupport.removePropertyChangeListener(l);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public Color getBrushColor()
/*     */   {
/* 253 */     return this.brushColor;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setBrushColor(Color brushColor)
/*     */   {
/* 261 */     Color oldBrushColor = this.brushColor;
/* 262 */     this.brushColor = brushColor;
/* 263 */     this.propertyChangeSupport.firePropertyChange("brushColor", oldBrushColor, brushColor);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   private Point2D offset;
/*     */   
/*     */   private boolean shouldFillShape;
/*     */   
/*     */   private boolean shapeMasked;
/*     */   
/*     */   public int getBrushSteps()
/*     */   {
/* 276 */     return this.brushSteps;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setBrushSteps(int brushSteps)
/*     */   {
/* 284 */     int oldBrushSteps = this.brushSteps;
/* 285 */     this.brushSteps = brushSteps;
/* 286 */     this.propertyChangeSupport.firePropertyChange("brushSteps", new Integer(oldBrushSteps), new Integer(brushSteps));
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
/*     */   public int getEffectWidth()
/*     */   {
/* 299 */     return this.effectWidth;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setEffectWidth(int effectWidth)
/*     */   {
/* 307 */     int oldEffectWidth = this.effectWidth;
/* 308 */     this.effectWidth = effectWidth;
/* 309 */     this.propertyChangeSupport.firePropertyChange("effectWidth", new Integer(oldEffectWidth), new Integer(effectWidth));
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
/*     */   public boolean isRenderInsideShape()
/*     */   {
/* 322 */     return this.renderInsideShape;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setRenderInsideShape(boolean renderInsideShape)
/*     */   {
/* 330 */     boolean oldRenderInsideShape = this.renderInsideShape;
/* 331 */     this.renderInsideShape = renderInsideShape;
/* 332 */     this.propertyChangeSupport.firePropertyChange("renderInsideShape", new Boolean(oldRenderInsideShape), new Boolean(renderInsideShape));
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
/*     */   public Point2D getOffset()
/*     */   {
/* 345 */     return this.offset;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setOffset(Point2D offset)
/*     */   {
/* 353 */     Point2D oldOffset = this.offset;
/* 354 */     this.offset = offset;
/* 355 */     this.propertyChangeSupport.firePropertyChange("offset", oldOffset, offset);
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
/*     */   public boolean isShouldFillShape()
/*     */   {
/* 368 */     return this.shouldFillShape;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setShouldFillShape(boolean shouldFillShape)
/*     */   {
/* 376 */     boolean oldShouldFillShape = this.shouldFillShape;
/* 377 */     this.shouldFillShape = shouldFillShape;
/* 378 */     this.propertyChangeSupport.firePropertyChange("shouldFillShape", new Boolean(oldShouldFillShape), new Boolean(shouldFillShape));
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
/*     */   public boolean isShapeMasked()
/*     */   {
/* 391 */     return this.shapeMasked;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setShapeMasked(boolean shapeMasked)
/*     */   {
/* 399 */     boolean oldShapeMasked = this.shapeMasked;
/* 400 */     this.shapeMasked = shapeMasked;
/* 401 */     this.propertyChangeSupport.firePropertyChange("shapeMasked", new Boolean(oldShapeMasked), new Boolean(shapeMasked));
/*     */   }
/*     */ }


/* Location:              E:\java\java学习\hutubill\lib\all.jar!\org\jdesktop\swingx\painter\effects\AbstractAreaEffect.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */
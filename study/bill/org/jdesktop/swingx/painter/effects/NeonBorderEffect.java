/*     */ package org.jdesktop.swingx.painter.effects;
/*     */ 
/*     */ import java.awt.AlphaComposite;
/*     */ import java.awt.BasicStroke;
/*     */ import java.awt.Color;
/*     */ import java.awt.Graphics2D;
/*     */ import java.awt.Point;
/*     */ import java.awt.Shape;
/*     */ import java.awt.geom.Point2D;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class NeonBorderEffect
/*     */   extends AbstractAreaEffect
/*     */ {
/*     */   private Color edgeColor;
/*     */   private Color centerColor;
/*  41 */   private BorderPosition borderPosition = BorderPosition.Outside;
/*     */   
/*     */ 
/*     */   public static enum BorderPosition
/*     */   {
/*  46 */     Inside,  Centered,  Outside;
/*     */     
/*     */     private BorderPosition() {}
/*     */   }
/*     */   
/*     */   public NeonBorderEffect()
/*     */   {
/*  53 */     this(Color.GREEN, Color.WHITE, 10);
/*     */   }
/*     */   
/*     */ 
/*     */   public NeonBorderEffect(Color edgeColor, Color centerColor, int effectWidth)
/*     */   {
/*  59 */     setEffectWidth(effectWidth);
/*  60 */     setEdgeColor(edgeColor);
/*  61 */     setCenterColor(centerColor);
/*  62 */     setRenderInsideShape(false);
/*  63 */     setShouldFillShape(false);
/*  64 */     setOffset(new Point(0, 0));
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
/*     */   protected void paintBorderGlow(Graphics2D gfx, Shape clipShape, int width, int height)
/*     */   {
/*  90 */     gfx.translate(getOffset().getX(), getOffset().getY());
/*  91 */     gfx.setComposite(AlphaComposite.SrcOver);
/*  92 */     int steps = getEffectWidth();
/*  93 */     if (this.borderPosition == BorderPosition.Centered) {
/*  94 */       steps /= 2;
/*     */     }
/*  96 */     for (int i = 0; i < steps; i++)
/*     */     {
/*     */ 
/*  99 */       float brushWidth = steps + 1 - i;
/* 100 */       float half = steps / 2;
/*     */       
/* 102 */       if (this.borderPosition == BorderPosition.Centered) {
/* 103 */         gfx.setPaint(interpolateColor((steps - i) / steps, getEdgeColor(), getCenterColor()));
/*     */       }
/* 105 */       else if (i < half) {
/* 106 */         gfx.setPaint(interpolateColor((half - i) / half, getEdgeColor(), getCenterColor()));
/*     */       } else {
/* 108 */         gfx.setPaint(interpolateColor((i - half) / half, getEdgeColor(), getCenterColor()));
/*     */       }
/*     */       
/*     */ 
/*     */ 
/* 113 */       gfx.setStroke(new BasicStroke(brushWidth, 1, 1));
/*     */       
/*     */ 
/* 116 */       gfx.draw(clipShape);
/*     */     }
/* 118 */     gfx.translate(-getOffset().getX(), -getOffset().getY());
/*     */   }
/*     */   
/*     */   protected Color interpolateColor(float t, Color start, Color end)
/*     */   {
/* 123 */     float[] partsS = start.getRGBComponents(null);
/* 124 */     float[] partsE = end.getRGBComponents(null);
/* 125 */     float[] partsR = new float[4];
/* 126 */     for (int i = 0; i < 4; i++) {
/* 127 */       partsR[i] = ((partsS[i] - partsE[i]) * t + partsE[i]);
/*     */     }
/* 129 */     return new Color(partsR[0], partsR[1], partsR[2], partsR[3]);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public Color getEdgeColor()
/*     */   {
/* 137 */     return this.edgeColor;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setEdgeColor(Color edgeColor)
/*     */   {
/* 145 */     this.edgeColor = edgeColor;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public Color getCenterColor()
/*     */   {
/* 153 */     return this.centerColor;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setCenterColor(Color centerColor)
/*     */   {
/* 162 */     this.centerColor = centerColor;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public BorderPosition getBorderPosition()
/*     */   {
/* 171 */     return this.borderPosition;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setBorderPosition(BorderPosition borderPosition)
/*     */   {
/* 181 */     this.borderPosition = borderPosition;
/* 182 */     switch (borderPosition) {
/*     */     case Centered: 
/* 184 */       setShapeMasked(false);
/* 185 */       break;
/*     */     case Inside: 
/* 187 */       setShapeMasked(true);
/* 188 */       setRenderInsideShape(true);
/* 189 */       break;
/*     */     case Outside: 
/* 191 */       setShapeMasked(true);
/* 192 */       setRenderInsideShape(false);
/*     */     }
/*     */     
/* 195 */     if (borderPosition == BorderPosition.Centered) {}
/*     */   }
/*     */ }


/* Location:              E:\java\java学习\hutubill\lib\all.jar!\org\jdesktop\swingx\painter\effects\NeonBorderEffect.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */
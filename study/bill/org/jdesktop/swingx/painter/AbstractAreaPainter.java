/*     */ package org.jdesktop.swingx.painter;
/*     */ 
/*     */ import java.awt.Color;
/*     */ import java.awt.Graphics2D;
/*     */ import java.awt.Paint;
/*     */ import java.awt.Shape;
/*     */ import org.jdesktop.swingx.painter.effects.AreaEffect;
/*     */ import org.jdesktop.swingx.util.PaintUtils;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public abstract class AbstractAreaPainter<T>
/*     */   extends AbstractLayoutPainter<T>
/*     */ {
/*     */   private boolean stretchPaint;
/*     */   
/*     */   public static enum Style
/*     */   {
/*  65 */     BOTH,  FILLED,  OUTLINE,  NONE;
/*     */     
/*     */     private Style() {}
/*     */   }
/*     */   
/*  70 */   private AreaEffect[] areaEffects = new AreaEffect[0];
/*     */   
/*  72 */   private Style style = Style.BOTH;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   private float borderWidth;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   private Paint fillPaint;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   private Paint borderPaint;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public AbstractAreaPainter()
/*     */   {
/*  94 */     this.fillPaint = Color.RED;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public AbstractAreaPainter(Paint paint)
/*     */   {
/* 102 */     this.fillPaint = paint;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public Paint getFillPaint()
/*     */   {
/* 110 */     return this.fillPaint;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setFillPaint(Paint p)
/*     */   {
/* 118 */     Paint old = getFillPaint();
/* 119 */     this.fillPaint = p;
/* 120 */     setDirty(true);
/* 121 */     firePropertyChange("fillPaint", old, getFillPaint());
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean isPaintStretched()
/*     */   {
/* 131 */     return this.stretchPaint;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setPaintStretched(boolean paintStretched)
/*     */   {
/* 143 */     boolean old = isPaintStretched();
/* 144 */     this.stretchPaint = paintStretched;
/* 145 */     setDirty(true);
/* 146 */     firePropertyChange("paintStretched", Boolean.valueOf(old), Boolean.valueOf(isPaintStretched()));
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setBorderPaint(Paint p)
/*     */   {
/* 157 */     Paint old = getBorderPaint();
/* 158 */     this.borderPaint = p;
/* 159 */     setDirty(true);
/* 160 */     firePropertyChange("borderPaint", old, getBorderPaint());
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public Paint getBorderPaint()
/*     */   {
/* 170 */     return this.borderPaint;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setStyle(Style s)
/*     */   {
/* 180 */     Style old = getStyle();
/* 181 */     this.style = (s == null ? Style.BOTH : s);
/* 182 */     setDirty(true);
/* 183 */     firePropertyChange("style", old, getStyle());
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public Style getStyle()
/*     */   {
/* 193 */     return this.style;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setBorderWidth(float s)
/*     */   {
/* 202 */     float old = getBorderWidth();
/* 203 */     this.borderWidth = s;
/* 204 */     setDirty(true);
/* 205 */     firePropertyChange("borderWidth", Float.valueOf(old), Float.valueOf(getBorderWidth()));
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public float getBorderWidth()
/*     */   {
/* 213 */     return this.borderWidth;
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
/*     */   Paint calculateSnappedPaint(Paint p, int width, int height)
/*     */   {
/* 226 */     return PaintUtils.resizeGradient(p, width, height);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected abstract Shape provideShape(Graphics2D paramGraphics2D, T paramT, int paramInt1, int paramInt2);
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setAreaEffects(AreaEffect... areaEffects)
/*     */   {
/* 247 */     AreaEffect[] old = getAreaEffects();
/* 248 */     this.areaEffects = new AreaEffect[areaEffects == null ? 0 : areaEffects.length];
/* 249 */     if (areaEffects != null) {
/* 250 */       System.arraycopy(areaEffects, 0, this.areaEffects, 0, this.areaEffects.length);
/*     */     }
/* 252 */     setDirty(true);
/* 253 */     firePropertyChange("areaEffects", old, getAreaEffects());
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public AreaEffect[] getAreaEffects()
/*     */   {
/* 261 */     AreaEffect[] results = new AreaEffect[this.areaEffects.length];
/* 262 */     System.arraycopy(this.areaEffects, 0, results, 0, results.length);
/* 263 */     return results;
/*     */   }
/*     */ }


/* Location:              E:\java\java学习\hutubill\lib\all.jar!\org\jdesktop\swingx\painter\AbstractAreaPainter.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */
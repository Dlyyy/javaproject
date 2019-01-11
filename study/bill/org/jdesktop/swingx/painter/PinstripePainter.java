/*     */ package org.jdesktop.swingx.painter;
/*     */ 
/*     */ import java.awt.BasicStroke;
/*     */ import java.awt.Graphics2D;
/*     */ import java.awt.Paint;
/*     */ import java.awt.Rectangle;
/*     */ import java.awt.Shape;
/*     */ import java.awt.geom.Area;
/*     */ import java.awt.geom.Line2D;
/*     */ import java.awt.geom.Line2D.Double;
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
/*     */ public class PinstripePainter
/*     */   extends AbstractPainter<Object>
/*     */ {
/*  59 */   private double angle = 45.0D;
/*     */   
/*     */ 
/*     */ 
/*  63 */   private double spacing = 8.0D;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*  68 */   private double stripeWidth = 1.0D;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private Paint paint;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public PinstripePainter() {}
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public PinstripePainter(Paint paint)
/*     */   {
/*  90 */     this(paint, 45.0D);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public PinstripePainter(Paint paint, double angle)
/*     */   {
/* 101 */     this.paint = paint;
/* 102 */     this.angle = angle;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public PinstripePainter(double angle)
/*     */   {
/* 112 */     this.angle = angle;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public PinstripePainter(Paint paint, double angle, double stripeWidth, double spacing)
/*     */   {
/* 123 */     this.paint = paint;
/* 124 */     this.angle = angle;
/* 125 */     this.stripeWidth = stripeWidth;
/* 126 */     this.spacing = spacing;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setPaint(Paint p)
/*     */   {
/* 135 */     Paint old = getPaint();
/* 136 */     this.paint = p;
/* 137 */     firePropertyChange("paint", old, getPaint());
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public Paint getPaint()
/*     */   {
/* 145 */     return this.paint;
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
/*     */   public void setAngle(double angle)
/*     */   {
/* 158 */     if (angle > 360.0D) {
/* 159 */       angle %= 360.0D;
/*     */     }
/*     */     
/* 162 */     if (angle < 0.0D) {
/* 163 */       angle = 360.0D - angle * -1.0D % 360.0D;
/*     */     }
/*     */     
/* 166 */     double old = getAngle();
/* 167 */     this.angle = angle;
/* 168 */     firePropertyChange("angle", Double.valueOf(old), Double.valueOf(getAngle()));
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public double getAngle()
/*     */   {
/* 176 */     return this.angle;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setSpacing(double spacing)
/*     */   {
/* 185 */     double old = getSpacing();
/* 186 */     this.spacing = spacing;
/* 187 */     firePropertyChange("spacing", Double.valueOf(old), Double.valueOf(getSpacing()));
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public double getSpacing()
/*     */   {
/* 195 */     return this.spacing;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected void doPaint(Graphics2D g, Object component, int width, int height)
/*     */   {
/* 205 */     Shape oldClip = g.getClip();
/* 206 */     Area area = new Area(new Rectangle(0, 0, width, height));
/* 207 */     if (oldClip != null) {
/* 208 */       area = new Area(oldClip);
/*     */     }
/* 210 */     area.intersect(new Area(new Rectangle(0, 0, width, height)));
/* 211 */     g.setClip(area);
/*     */     
/* 213 */     Paint p = getPaint();
/* 214 */     if (p == null) {
/* 215 */       if ((component instanceof JComponent)) {
/* 216 */         g.setColor(((JComponent)component).getForeground());
/*     */       }
/*     */     } else {
/* 219 */       g.setPaint(p);
/*     */     }
/*     */     
/* 222 */     g.setStroke(new BasicStroke((float)getStripeWidth()));
/*     */     
/* 224 */     double hypLength = Math.sqrt(width * width + height * height);
/*     */     
/*     */ 
/* 227 */     double radians = Math.toRadians(getAngle());
/* 228 */     g.rotate(radians);
/*     */     
/* 230 */     double spacing = getSpacing();
/* 231 */     spacing += getStripeWidth();
/* 232 */     int numLines = (int)(hypLength / spacing);
/*     */     
/* 234 */     for (int i = 0; i < numLines; i++) {
/* 235 */       double x = i * spacing;
/* 236 */       Line2D line = new Line2D.Double(x, -hypLength, x, hypLength);
/* 237 */       g.draw(line);
/*     */     }
/* 239 */     g.setClip(oldClip);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public double getStripeWidth()
/*     */   {
/* 247 */     return this.stripeWidth;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setStripeWidth(double stripeWidth)
/*     */   {
/* 255 */     double oldSripeWidth = getStripeWidth();
/* 256 */     this.stripeWidth = stripeWidth;
/* 257 */     firePropertyChange("stripeWidth", new Double(oldSripeWidth), new Double(stripeWidth));
/*     */   }
/*     */ }


/* Location:              E:\java\java学习\hutubill\lib\all.jar!\org\jdesktop\swingx\painter\PinstripePainter.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */
/*     */ package org.jdesktop.swingx.color;
/*     */ 
/*     */ import java.awt.Color;
/*     */ import java.awt.Graphics;
/*     */ import java.awt.Graphics2D;
/*     */ import java.awt.LinearGradientPaint;
/*     */ import java.awt.MultipleGradientPaint;
/*     */ import java.awt.MultipleGradientPaint.ColorSpaceType;
/*     */ import java.awt.MultipleGradientPaint.CycleMethod;
/*     */ import java.awt.Paint;
/*     */ import java.awt.Point;
/*     */ import java.awt.RadialGradientPaint;
/*     */ import java.awt.RenderingHints;
/*     */ import java.awt.event.MouseEvent;
/*     */ import java.awt.geom.Point2D;
/*     */ import java.awt.geom.Point2D.Double;
/*     */ import java.awt.geom.Point2D.Float;
/*     */ import java.util.List;
/*     */ import java.util.logging.Logger;
/*     */ import javax.swing.event.MouseInputAdapter;
/*     */ import org.jdesktop.swingx.JXGradientChooser;
/*     */ import org.jdesktop.swingx.JXPanel;
/*     */ import org.jdesktop.swingx.multislider.MultiThumbModel;
/*     */ import org.jdesktop.swingx.multislider.Thumb;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class GradientPreviewPanel
/*     */   extends JXPanel
/*     */ {
/*  51 */   private Paint checker_texture = null;
/*     */   private Point2D start;
/*     */   private Point2D end;
/*  54 */   public JXGradientChooser picker; boolean moving_start = false;
/*  55 */   boolean moving_end = false;
/*  56 */   private boolean radial = false;
/*  57 */   private boolean reversed = false;
/*  58 */   private boolean reflected = false;
/*  59 */   private boolean repeated = false;
/*     */   private MultipleGradientPaint gradient;
/*     */   private MultiThumbModel model;
/*     */   
/*  63 */   public GradientPreviewPanel() { this.start = new Point2D.Float(10.0F, 10.0F);
/*  64 */     this.end = new Point2D.Float(80.0F, 10.0F);
/*  65 */     this.checker_texture = ColorUtil.getCheckerPaint();
/*  66 */     MouseInputAdapter ma = new GradientMouseHandler(null);
/*  67 */     addMouseListener(ma);
/*  68 */     addMouseMotionListener(ma);
/*     */   }
/*     */   
/*     */   public void setGradient() {
/*  72 */     repaint();
/*     */   }
/*     */   
/*     */   public void setGradient(MultipleGradientPaint grad) {
/*  76 */     MultipleGradientPaint old = getGradient();
/*  77 */     if ((grad instanceof LinearGradientPaint)) {
/*  78 */       LinearGradientPaint paint = (LinearGradientPaint)grad;
/*  79 */       this.start = paint.getStartPoint();
/*  80 */       this.end = paint.getEndPoint();
/*     */     } else {
/*  82 */       RadialGradientPaint paint = (RadialGradientPaint)grad;
/*  83 */       this.start = paint.getCenterPoint();
/*  84 */       this.end = new Point2D.Double(this.start.getX(), this.start.getY() + paint.getRadius());
/*     */     }
/*  86 */     this.gradient = grad;
/*  87 */     firePropertyChange("gradient", old, getGradient());
/*  88 */     repaint();
/*     */   }
/*     */   
/*     */   public MultipleGradientPaint getGradient() {
/*  92 */     return this.gradient;
/*     */   }
/*     */   
/*     */   public MultipleGradientPaint calculateGradient() {
/*  96 */     List<Thumb<Color>> stops = getStops();
/*  97 */     int len = stops.size();
/*     */     
/*     */ 
/* 100 */     float[] fractions = new float[len];
/* 101 */     Color[] colors = new Color[len];
/* 102 */     int i = 0;
/* 103 */     for (Thumb<Color> thumb : stops) {
/* 104 */       colors[i] = ((Color)thumb.getObject());
/* 105 */       fractions[i] = thumb.getPosition();
/* 106 */       i++;
/*     */     }
/*     */     
/*     */ 
/* 110 */     setGradient(calculateGradient(fractions, colors));
/* 111 */     return getGradient();
/*     */   }
/*     */   
/*     */ 
/* 115 */   private Logger log = Logger.getLogger(GradientPreviewPanel.class.getName());
/*     */   
/*     */   private List<Thumb<Color>> getStops()
/*     */   {
/* 119 */     return this.model == null ? null : this.model.getSortedThumbs();
/*     */   }
/*     */   
/*     */   public void setMultiThumbModel(MultiThumbModel model) {
/* 123 */     MultiThumbModel old = getMultiThumbModel();
/* 124 */     this.model = model;
/* 125 */     firePropertyChange("multiThumbModel", old, getMultiThumbModel());
/*     */   }
/*     */   
/*     */   public MultiThumbModel getMultiThumbModel() {
/* 129 */     return this.model;
/*     */   }
/*     */   
/*     */   protected void paintComponent(Graphics g)
/*     */   {
/*     */     try {
/* 135 */       Graphics2D g2 = (Graphics2D)g;
/*     */       
/*     */ 
/* 138 */       g2.setPaint(this.checker_texture);
/* 139 */       g.fillRect(0, 0, getWidth(), getHeight());
/*     */       
/*     */ 
/* 142 */       Paint paint = getGradient();
/*     */       
/* 144 */       if (paint != null) {
/* 145 */         g2.setPaint(paint);
/*     */       } else {
/* 147 */         g2.setPaint(Color.black);
/*     */       }
/*     */       
/* 150 */       g.fillRect(0, 0, getWidth(), getHeight());
/*     */       
/* 152 */       drawHandles(g2);
/*     */     } catch (Exception ex) {
/* 154 */       this.log.severe("ex: " + ex);
/*     */     }
/*     */   }
/*     */   
/*     */   private MultipleGradientPaint calculateGradient(float[] fractions, Color[] colors)
/*     */   {
/* 160 */     Point2D start = this.start;
/* 161 */     Point2D end = this.end;
/* 162 */     if (isReversed())
/*     */     {
/* 164 */       start = this.end;
/* 165 */       end = this.start;
/*     */     }
/*     */     
/*     */ 
/* 169 */     MultipleGradientPaint.CycleMethod cycle = MultipleGradientPaint.CycleMethod.NO_CYCLE;
/* 170 */     if (isRepeated())
/*     */     {
/* 172 */       cycle = MultipleGradientPaint.CycleMethod.REPEAT;
/*     */     }
/* 174 */     if (isReflected())
/*     */     {
/* 176 */       cycle = MultipleGradientPaint.CycleMethod.REFLECT;
/*     */     }
/*     */     
/*     */ 
/* 180 */     MultipleGradientPaint paint = null;
/* 181 */     if (isRadial()) {
/* 182 */       paint = new RadialGradientPaint(start, (float)start.distance(end), start, fractions, colors, cycle, MultipleGradientPaint.ColorSpaceType.SRGB, null);
/*     */ 
/*     */     }
/*     */     else
/*     */     {
/* 187 */       paint = new LinearGradientPaint((float)start.getX(), (float)start.getY(), (float)end.getX(), (float)end.getY(), fractions, colors, cycle);
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 194 */     return paint;
/*     */   }
/*     */   
/*     */   private void drawHandles(Graphics2D g2) {
/* 198 */     g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
/*     */     
/*     */ 
/* 201 */     g2.setColor(Color.black);
/* 202 */     g2.drawOval((int)this.start.getX() - 5, (int)this.start.getY() - 5, 10, 10);
/* 203 */     g2.setColor(Color.white);
/* 204 */     g2.drawOval((int)this.start.getX() - 4, (int)this.start.getY() - 4, 8, 8);
/*     */     
/* 206 */     g2.setColor(Color.black);
/* 207 */     g2.drawOval((int)this.end.getX() - 5, (int)this.end.getY() - 5, 10, 10);
/* 208 */     g2.setColor(Color.white);
/* 209 */     g2.drawOval((int)this.end.getX() - 4, (int)this.end.getY() - 4, 8, 8);
/*     */     
/* 211 */     g2.setColor(Color.darkGray);
/* 212 */     g2.drawLine((int)this.start.getX(), (int)this.start.getY(), (int)this.end.getX(), (int)this.end.getY());
/*     */     
/* 214 */     g2.setColor(Color.gray);
/* 215 */     g2.drawLine((int)this.start.getX() - 1, (int)this.start.getY() - 1, (int)this.end.getX() - 1, (int)this.end.getY() - 1);
/*     */   }
/*     */   
/*     */   private class GradientMouseHandler extends MouseInputAdapter
/*     */   {
/*     */     private GradientMouseHandler() {}
/*     */     
/*     */     public void mousePressed(MouseEvent evt) {
/* 223 */       GradientPreviewPanel.this.moving_start = false;
/* 224 */       GradientPreviewPanel.this.moving_end = false;
/* 225 */       if (evt.getPoint().distance(GradientPreviewPanel.this.start) < 5.0D) {
/* 226 */         GradientPreviewPanel.this.moving_start = true;
/* 227 */         GradientPreviewPanel.this.start = evt.getPoint();
/* 228 */         return;
/*     */       }
/*     */       
/* 231 */       if (evt.getPoint().distance(GradientPreviewPanel.this.end) < 5.0D) {
/* 232 */         GradientPreviewPanel.this.moving_end = true;
/* 233 */         GradientPreviewPanel.this.end = evt.getPoint();
/* 234 */         return;
/*     */       }
/*     */       
/* 237 */       GradientPreviewPanel.this.start = evt.getPoint();
/*     */     }
/*     */     
/*     */     public void mouseDragged(MouseEvent evt)
/*     */     {
/* 242 */       if (GradientPreviewPanel.this.moving_start) {
/* 243 */         GradientPreviewPanel.this.start = evt.getPoint();
/*     */       } else {
/* 245 */         GradientPreviewPanel.this.end = evt.getPoint();
/*     */       }
/* 247 */       GradientPreviewPanel.this.calculateGradient();
/* 248 */       GradientPreviewPanel.this.repaint();
/*     */     }
/*     */   }
/*     */   
/*     */   public boolean isRadial() {
/* 253 */     return this.radial;
/*     */   }
/*     */   
/*     */   public void setRadial(boolean radial) {
/* 257 */     boolean old = isRadial();
/* 258 */     this.radial = radial;
/* 259 */     firePropertyChange("radial", old, isRadial());
/*     */   }
/*     */   
/*     */   public boolean isReversed() {
/* 263 */     return this.reversed;
/*     */   }
/*     */   
/*     */   public void setReversed(boolean reversed) {
/* 267 */     boolean old = isReversed();
/* 268 */     this.reversed = reversed;
/* 269 */     firePropertyChange("reversed", old, isReversed());
/*     */   }
/*     */   
/*     */   public boolean isReflected() {
/* 273 */     return this.reflected;
/*     */   }
/*     */   
/*     */   public void setReflected(boolean reflected) {
/* 277 */     boolean old = isReflected();
/* 278 */     this.reflected = reflected;
/* 279 */     firePropertyChange("reflected", old, isReflected());
/*     */   }
/*     */   
/*     */   public boolean isRepeated() {
/* 283 */     return this.repeated;
/*     */   }
/*     */   
/*     */   public void setRepeated(boolean repeated) {
/* 287 */     boolean old = isRepeated();
/* 288 */     this.repeated = repeated;
/* 289 */     firePropertyChange("repeated", old, isRepeated());
/*     */   }
/*     */ }


/* Location:              E:\java\java学习\hutubill\lib\all.jar!\org\jdesktop\swingx\color\GradientPreviewPanel.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */